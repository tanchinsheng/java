package it.txt.tpms.tp.managers;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.tp.TP;
import it.txt.tpms.tp.TPTestDetails;
import it.txt.tpms.tp.list.TPList;
import tol.oneConnDbWrtr;
import tpms.TpmsException;
import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;
import tpms.utils.UserUtils;
import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 18-mag-2006
 * Time: 11.45.41
 * Manage TP data from and To database.
 */

public class TPDbManager extends QueryUtils {
	
	protected static oneConnDbWrtr dbwrt = null;
	
	/**
     * @param tpmsLogin
     * @return the list of tps owned by the tpmsLogin user
     */
    public static TPList getMyTPs(String tpmsLogin) throws TpmsException {
        return getMyTPs(tpmsLogin, null);
    }

     /**
     * @param tpmsLogin
     * @param line
     * @return the list of tps owned by the tpmsLogin user filtered according to line attribute (line == null or line.equals("") means not filter)
     */
    public static TPList getMyTPs(String tpmsLogin, String line) throws TpmsException {
        //Da cui (tp.origin=tp lo vedo se login ut corrente = tp.valid_login && tp.to_plant= local_plant) or (tp.origin= ls allora lo vedo se tp.owner = login ut corrente and tp.from_plant=local_plant)
        TPList result = new TPList();
        
        if (!GeneralStringUtils.isEmptyString(tpmsLogin)) {
            // starting from the version 5.0 the unix login and the tpms login shoul be equal:
            // in the past this is not mandatory so I'll put the following line just to be sure
            String unixUser = UserUtils.getUserUnixLogin(tpmsLogin);
            //select tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, MAX(tp.TPMS_VER) as TPMS_VER, tp.LINESET_NAME, TO_CHAR(MAX(DISTRIB_DATE),'" + ORACLE_DATE_FORMAT + "') as DISTRIB_DATE, tp.LINE, tc.COMMENT_BODY
            //"select tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, MAX(tp.TPMS_VER) as TPMS_VER, tp.LINESET_NAME, TO_CHAR(MAX(LAST_ACTION_DATE), '" + ORACLE_DATE_FORMAT + "') as LAST_ACTION_DATE, TO_CHAR(MAX(DISTRIB_DATE),'" + ORACLE_DATE_FORMAT + "') as DISTRIB_DATE, TO_CHAR(MAX(PROD_DATE),'" + ORACLE_DATE_FORMAT + "') as PROD_DATE, tp.LINE, tc.COMMENT_BODY " +
            String query = "select tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER , TO_CHAR(MAX(DISTRIB_DATE),'" + ORACLE_DATE_FORMAT + "') as DISTRIB_DATE, tp.LINE, tc.COMMENT_BODY, tp.PRODUCTION_AREA_ID " +
                    "from tp_plant tp, tp_comments tc " +
                    "where tc.JOBNAME (+)= tp.JOBNAME and tc.JOB_RELEASE (+)= tp.JOB_RELEASE and tc.JOB_REVISION (+)= tp.JOB_REVISION and tc.TPMS_VER (+)= tp.TPMS_VER and " +
                    "((tp.ORIGIN = 'tp' and tp.TO_PLANT = " + getStringValueForQuery(tpmsConfiguration.getLocalPlant()) + " and tp.VALID_LOGIN = " + getStringValueForQuery(unixUser) + ") " +
                    "or " +
                    "(tp.ORIGIN = 'ls' and tp.FROM_PLANT = " + getStringValueForQuery(tpmsConfiguration.getLocalPlant()) + " and tp.OWNER_LOGIN = " + getStringValueForQuery(unixUser) + " )) " +
                    "and tp.STATUS NOT IN('Obsolete', 'Ghost', 'Rejected') AND tp.VOB_STATUS != 'OffLine'";

            String otherSqlStatemts = "group by tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tc.COMMENT_BODY, tp.PRODUCTION_AREA_ID " +
                    "order by DISTRIB_DATE DESC, tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE ";

            if (!GeneralStringUtils.isEmptyString(line)) {
                query = query + " " + " and tp.LINE = " + getStringValueForQuery(line) + " ";
            }
            query = query + " " + otherSqlStatemts;
            debugLog("TPDbManager :: getMyTPs : query = " + query);

            //ResultSet myTpData = executeSelectQuery(query);
            SQLInterface iface = new SQLInterface();
            CachedRowSet myTpData = iface.execQuery(query);
            if (myTpData != null) {
                Date distributionDate = null;
                TP tmpTP;
                try {
                    while (myTpData.next()) {
                        /************DATE FIELDS MANAGEMENT*************/
                        if (!GeneralStringUtils.isEmptyString(myTpData.getString("DISTRIB_DATE"))) {
                            try {
                                distributionDate = simpleDateFormat.parse(myTpData.getString("DISTRIB_DATE"));
                            } catch (ParseException e) {
                                throw new TpmsException("TPDbManager :: getMyTPs : fetching tp data unable to parse distribution date " + myTpData.getString("DISTRIB_DATE"), "", e);
                            }
                        }
                        tmpTP = new TP(myTpData.getString("JOBNAME"), myTpData.getInt("JOB_RELEASE"),
                                myTpData.getString("JOB_REVISION"), myTpData.getInt("TPMS_VER"));
                        tmpTP.setDistributionDate(distributionDate);
                        tmpTP.setDbComments(myTpData.getString("COMMENT_BODY"));
                        tmpTP.setLine(myTpData.getString("LINE"));
                        result.addElement(tmpTP);
                    }
                } catch (SQLException e) {
                    String msg = "TPDbManager :: getMyTPs : general error while fetching data " + e.getMessage();
                    throw new TpmsException(msg, "", e);
                } finally {  
                	try {
                		myTpData.close();
                	} catch (SQLException se) {
                		errorLog("TPDbManager :: getMyTPs : error while closing resultset! ",se);
                	}
                }
            }

        } else {
            throw new TpmsException("TPDbManager :: getMyTPs : missing data : tpmsLogin = " + tpmsLogin);
        }
        return result;
    }
  
    public static List getTPTestDetails (String jobName, int jobRelease, String jobRevision, int tpmsVersion) throws TpmsException {
    	List result = new ArrayList();
        if (!GeneralStringUtils.isEmptyString(jobName) && jobRelease >= 0 && !GeneralStringUtils.isEmptyString(jobRevision) && tpmsVersion >= 0) {
            String query = "select TEST_NO_ID, TEST_NO,NVL(NEW_FLAG,'N'),NVL(OLD_LSL,-99999.99),NVL(OLD_USL,-99999.99),NVL(UNIT,'.'),NVL(NEW_LSL,-99999.99),NVL(NEW_USL,-99999.99),NVL(TESTS_COMMENTS,'.') " +
                    "from TP_TEST_DELIVERY " +
                    "where (JOBNAME = " + getStringValueForQuery(jobName) + " and JOB_RELEASE = " + jobRelease + " and JOB_REVISION = " + getStringValueForQuery(jobRevision) + " and TPMS_VER = " + tpmsVersion + ")";
            debugLog("TPDbManager :: getTPTestDetails : query = " + query);
           
            //ResultSet rs = executeSelectQuery(query);
            SQLInterface iface = new SQLInterface();
            CachedRowSet rs = iface.execQuery(query);
            if (rs != null) {
            	try {
            		while (rs.next()) {
            			TPTestDetails testDetails = new TPTestDetails();
            			testDetails.setTestNoID(rs.getInt(1));
            			testDetails.setTestNo(rs.getString(2));
            			testDetails.setNewFlag(rs.getString(3));
            			testDetails.setOldLSL(rs.getDouble(4));
            			testDetails.setOldUSL(rs.getDouble(5));
            			testDetails.setUnit(rs.getString(6));
            			testDetails.setNewLSL(rs.getDouble(7));
            			testDetails.setNewUSL(rs.getDouble(8));
            			testDetails.setTestsComments(rs.getString(9));
            	     	result.add(testDetails);
            	     	testDetails = null;
            	    }
            	} catch (SQLException e) {
            		throw new TpmsException("TPDbManager :: getTPTestDetails : error while fetching data: job name =" + jobName + " job release = " + jobRelease + " job revision = " + jobRevision + " tpms version = " + tpmsVersion, "", e);
            	} finally {
            		if (rs != null) {
            			try {
            				rs.close();
            			} catch (SQLException e) {
            				errorLog("TPDbManager :: getTPTestDetails : error while closing resultset", e);
            			}
            		}
            	}
            	return result;
            } else {
            	return null;
            }
        }
        return result;
    }
    
    /**
     * Griven a TpList remove any comments associated with TPs contained in the list.
     * @param tpList
     * @param sessionId
     * @param tpmsLogin
     * @return true if removal is succesfully completed, false otherwise
     */
    public static boolean removeTPsComments(TPList tpList, String sessionId, String tpmsLogin) {
        boolean result = false;
        if (tpList != null && !tpList.isEmpty() && !GeneralStringUtils.isEmptyString(sessionId) && !GeneralStringUtils.isEmptyString(tpmsLogin))
        {
            String query = "delete tp_comments " + " where ";
            StringBuffer whereCondition = new StringBuffer("");
            TP tmpTP;
            while (tpList.hasNext()) {
                tmpTP = tpList.next();

                if (!GeneralStringUtils.isEmptyString(whereCondition.toString())) {
                    whereCondition.append(" or ");
                }
                whereCondition.append("( jobname = ").append(getStringValueForQuery(tmpTP.getJobName()));
                whereCondition.append(" and job_release = ").append(tmpTP.getJobRelease());
                whereCondition.append(" and job_revision = ").append(getStringValueForQuery(tmpTP.getJobRevision()));
                whereCondition.append(" and tpms_ver = ").append(tmpTP.getTpmsVersion()).append(")");
            }
            if (!GeneralStringUtils.isEmptyString(whereCondition.toString())) {
                query = query + whereCondition.toString();
                debugLog("TPDbManager :: removeTPsComments : query = " + query);
                result = executeDeleteQuery(query, sessionId, tpmsLogin);
            }
            tpList.rewind();
        }
        return result;
    }

    /**
     * given a TPList and a comment modify (i.e add/update or remove) the comment assciated to all tp in the tp list.
     * @param tpList
     * @param comment
     * @param sessionId
     * @param tpmsLogin
     * @return true if modifications is succesfully completed, false otherwise
     * @throws TpmsException
     */
    public static boolean modifyTPsComment(TPList tpList, String comment, String sessionId, String tpmsLogin) throws TpmsException {
        boolean result = false;
        if (tpList != null && !tpList.isEmpty() && !GeneralStringUtils.isEmptyString(sessionId) && !GeneralStringUtils.isEmptyString(tpmsLogin))
        {
            debugLog("TPDbManager :: modifyTPsComment : starting...." + comment);
            if (removeTPsComments(tpList, sessionId, tpmsLogin)) {
                debugLog("TPDbManager :: modifyTPsComment : tp comments removed....");
                TP tmpTP;
                String query;
                String queryTemplate = "insert into TP_COMMENTS (JOBNAME, JOB_RELEASE, JOB_REVISION, TPMS_VER, COMMENT_BODY) " +
                        " values " + " (<$$$VALUES$$$>)";
                String values;
                debugLog("Query for TP_COMMENTS insert : " + queryTemplate);
                boolean tmpResult = true;
                while (tpList.hasNext()) {
                    debugLog("TPDbManager :: modifyTPsComment : processing tp...");
                    tmpTP = tpList.next();
                    values = getStringValueForQuery(tmpTP.getJobName()) + ", " + tmpTP.getJobRelease() + ", " + getStringValueForQuery(tmpTP.getJobRevision()) + ", " + tmpTP.getTpmsVersion() + ", " +getStringValueForQuery(comment);
                    debugLog("TPDbManager :: modifyTPsComment : processing tp 1 values = " + values);
                    query = GeneralStringUtils.replaceAllIgnoreCase(queryTemplate, "<$$$VALUES$$$>", values);
                    debugLog("TPDbManager :: modifyTPsComment :processing tp 2 query = " + query);
                    tmpResult = tmpResult && executeUpdateQuery(query, sessionId, tpmsLogin);
                    debugLog("TPDbManager :: modifyTPsComment :processing tp 3 query executed tmpResult = " + tmpResult);
                }
                result = tmpResult;
                tpList.rewind();
            } else {
                debugLog("TPDbManager :: modifyTPsComment : error while tp comments removal....");
                throw new TpmsException("General error while removing TPs comment", "TPDbManager :: modifyTPsComment");
            }
        }
        return result;
    }

    /**
     * given a tp remove the comments assciated.
     * @param tp
     * @param sessionId
     * @param tpmsLogin
     * @return true if removal is succesfully completed, false otherwise
     */
    public static boolean removeTPComments(TP tp, String sessionId, String tpmsLogin) {
        TPList oneTpList = new TPList();
        oneTpList.addElement(tp);
        return removeTPsComments(oneTpList, sessionId, tpmsLogin);
    }

    /**
     * given a TP and a comment modify (i.e add/update or remove) the comment assciated .
     * @param tp
     * @param comment
     * @param sessionId
     * @param tpmsLogin
     * @return true if modifications is succesfully completed, false otherwise
     * @throws TpmsException
     */
    public static boolean modifyTPComment(TP tp, String comment, String sessionId, String tpmsLogin) throws TpmsException {
        TPList oneTpList = new TPList();
        oneTpList.addElement(tp);
        return modifyTPsComment(oneTpList, comment, sessionId, tpmsLogin);
    }

    /**
     * Given a tp update the tp_comment into db
     *
     * @param tp the tp that where the comments will be updated.
     * @return true if the operation succedes false otherwise
     */
    public static boolean updateTPComment(TP tp, String sessionId, String tpmsLogin) throws TpmsException {
        boolean result;
        if (tp != null && !GeneralStringUtils.isEmptyString(sessionId) && !GeneralStringUtils.isEmptyString(tpmsLogin))
        {
            String query = "UPDATE tp_plant tp " +
                    "SET TP.TP_COMMENT = " + getStringValueForQuery(tp.getDbComments()) + " " +
                    "where " +
                    "((tp.ORIGIN = 'tp' and tp.TO_PLANT = " + getStringValueForQuery(tpmsConfiguration.getLocalPlant()) + " and tp.VALID_LOGIN = " + getStringValueForQuery(tpmsLogin) + ") " +
                    "or " +
                    "(tp.ORIGIN = 'ls' and tp.FROM_PLANT = " + getStringValueForQuery(tpmsConfiguration.getLocalPlant()) + " and tp.OWNER_LOGIN = " + getStringValueForQuery(tpmsLogin) + " )) " +
                    "and " +
                    "(tp.JOBNAME = " + getStringValueForQuery(tp.getJobName()) + " and tp.JOB_RELEASE = " + tp.getJobRelease() + " and tp.JOB_REVISION = " + getStringValueForQuery(tp.getJobRevision()) + " and tp.TPMS_VER = " + tp.getTpmsVersion() + ")";
            debugLog("TPDbManager :: updateTPComment : query = " + query);
            result = executeUpdateQuery(query, sessionId, tpmsLogin);
        } else {
            throw new TpmsException("TPDbManager :: updateTPComment : missing data : tp == null? " + (tp == null) + " sessionId = " + sessionId + " tpmsLogin = " + tpmsLogin);
        }
        return result;
    }

    /**
     * Given a list of tp update the tp_comment into db for all tp setting it to tpComment
     *
     * @param tpList      the list of tp where the comments will be updated.
     * @param tpDbComment is the comment that will be associated with all the tp in the list: the format must be db format
     */
    public static void updateTPsComment(TPList tpList, String tpDbComment, String sessionId, String tpmsLogin) throws TpmsException {
        if (tpList != null && !tpList.isEmpty() && !GeneralStringUtils.isEmptyString(sessionId) && !GeneralStringUtils.isEmptyString(tpmsLogin))
        {
            TP tmpTp;
            while (tpList.hasNext()) {
                tmpTp = tpList.next();
                tmpTp.setDbComments(tpDbComment);
                try {
                    updateTPComment(tmpTp, sessionId, tpmsLogin);
                } catch (TpmsException e) {
                    errorLog("TPDbManager :: updateTPsComment", e);
                }
            }
        } else {
            throw new TpmsException("TPDbManager :: updateTPsComment : missing data : (tpList == null) or (tpList.isEmpty())? " + ((tpList == null) || (tpList.isEmpty())) +
                    " sessionId = " + sessionId + " tpmsLogin = " + tpmsLogin + " tpDbComment = " + tpDbComment);
        }
    }

    /**
     * Given a Tp return the comments from db, the comment will be setted also in tp object.
     *
     * @param tp
     * @return Given a Tp return the comments from db
     */
    public static String getTPDbComment(TP tp) throws TpmsException {
        if (tp != null) {
            String result = getTPDbComment(tp.getJobName(), tp.getJobRelease(), tp.getJobRevision(), tp.getTpmsVersion());
            tp.setDbComments(result);
            return result;
        } else {
            return null;
        }
    }

    /**
     * given tp data needed to identify the comment retruns the tp comment
     * @param jobName
     * @param jobRelease
     * @param jobRevision
     * @param tpmsVersion
     * @return given tp data needed to identify the comment retruns the tp comment
     * @throws TpmsException
     */
    public static String getTPDbComment(String jobName, int jobRelease, String jobRevision, int tpmsVersion) throws TpmsException {
        if (!GeneralStringUtils.isEmptyString(jobName) && jobRelease >= 0 && !GeneralStringUtils.isEmptyString(jobRevision) && tpmsVersion >= 0) {
            String query = "select COMMENT_BODY " +
                    "from TP_COMMENTS " +
                    "where (JOBNAME = " + getStringValueForQuery(jobName) + " and JOB_RELEASE = " + jobRelease + " and JOB_REVISION = " + getStringValueForQuery(jobRevision) + " and TPMS_VER = " + tpmsVersion + ")";
            debugLog("TPDbManager :: getTPDbComment : query = " + query);
            // ResultSet rs = executeSelectQuery(query);
            // CachedRowSet rs = executeSelectCacheQuery(query);
            SQLInterface iface = new SQLInterface();
            CachedRowSet rs = iface.execQuery(query);
            String result = "";
            try {
                if (rs != null && rs.next()) {
                    result = rs.getString("COMMENT_BODY");
                }
            } catch (SQLException e) {
                throw new TpmsException("TPDbManager :: getTPDbComment : error while fetching data: job name =" + jobName + " job release = " + jobRelease + " job revision = " + jobRevision + " tpms version = " + tpmsVersion, "", e);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                        rs = null;
                    } catch (SQLException e) {
                        errorLog("TPDbManager :: getTPDbComment : error while closing resultset", e);
                    }
                }
            }
            return result;
        } else {
            return null;
        }
    }
    
    public static String getTPActionComment(String jobName, int jobRelease, String jobRevision, int tpmsVersion, String status) throws TpmsException {
        if (!GeneralStringUtils.isEmptyString(jobName) && jobRelease >= 0 && !GeneralStringUtils.isEmptyString(jobRevision) && tpmsVersion >= 0) {
            String query = "select COMMENT_BODY " +
                    "from ACTION_COMMENTS " +
                    "where (JOBNAME = " + getStringValueForQuery(jobName) + " and JOB_RELEASE = " + jobRelease + " and JOB_REVISION = " + getStringValueForQuery(jobRevision) + " and TPMS_VER = " + tpmsVersion +  " and STATUS = '" + status +"')";
            debugLog("TPDbManager :: getTPActionComment : query = " + query);
            // ResultSet rs = executeSelectQuery(query);
            //CachedRowSet rs = executeSelectCacheQuery(query);
            SQLInterface iface = new SQLInterface();
            CachedRowSet rs = iface.execQuery(query);
            String result = "";
            try {
                if (rs != null && rs.next()) {
                    result = rs.getString("COMMENT_BODY");
                }
            } catch (SQLException e) {
                throw new TpmsException("TPDbManager :: getTPActionComment : error while fetching data: job name =" + jobName + " job release = " + jobRelease + " job revision = " + jobRevision + " tpms version = " + tpmsVersion + " status = " + status, "", e);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                        rs = null;
                    } catch (SQLException e) {
                        errorLog("TPDbManager :: getTPActionComment : error while closing resultset", e);
                    }
                }
            }
            return result;
        } else {
            return null;
        }

    }
  
    public static TPList getNewAttribute(String jobName, int jobRelease, String jobRevision, int tpmsVersion) throws TpmsException {
    	TPList result = new TPList();
    	if (!GeneralStringUtils.isEmptyString(jobName) && jobRelease >= 0 && !GeneralStringUtils.isEmptyString(jobRevision)&& tpmsVersion >= 0) {
    		String query = "select DELIVERY_COMMENTS,HW_MODIFICATIONS,EXP_AVG_YV,ZERO_YW,NEW_TT,IS_TEMP,VALID_TILL,EMAIL " +
                "from TP_PLANT " +
                "where (JOBNAME = " + getStringValueForQuery(jobName) + " and JOB_RELEASE = " + jobRelease + " and JOB_REVISION = " + getStringValueForQuery(jobRevision) + " and TPMS_VER = " + tpmsVersion + ")";
        	debugLog("TPDbManager :: getNewAttribute : query = " + query);
        	// ResultSet rs = executeSelectQuery(query);
        	// CachedRowSet rs = executeSelectCacheQuery(query);
            SQLInterface iface = new SQLInterface();
            CachedRowSet rs = iface.execQuery(query);
        	try {
        		TP tmpTp;
        		if (rs != null && rs.next()) {
        			 java.sql.Timestamp tillTimeStamp = null;
        			 java.sql.Date tillDate = null;
        			 if( rs.getString("VALID_TILL")!=null && !rs.getString("VALID_TILL").trim().equals("")){
        				 tillTimeStamp =  rs.getTimestamp("VALID_TILL");
        				 tillDate = new java.sql.Date(tillTimeStamp.getTime());      				 
        			 }
            		 tmpTp = new TP(rs.getString("DELIVERY_COMMENTS"), rs.getString("HW_MODIFICATIONS"),
            		 rs.getFloat("EXP_AVG_YV"), rs.getFloat("ZERO_YW"), rs.getInt("NEW_TT"), 
            		 rs.getString("IS_TEMP"), tillDate,rs.getString("EMAIL"));
            		 result.addElement(tmpTp);
        		}
        	}
        	catch (Exception e) {
        		throw new TpmsException("TPDbManager ::  getNewAttribute : error while fetching data: job name =" + jobName + " job release = " + jobRelease + " job revision = " + jobRevision + " tpms version = " + tpmsVersion, "", e); 
        	} finally {
        		if (rs != null) {
        			try {
        				rs.close();
         				rs = null;
        			} catch (SQLException e) {
        				errorLog("TPDbManager :: getNewAttribute : error while closing resultset", e);
        			}
        		}
        	}
        	return result;
    	} else {
    		return null;
    	}
    }
}

