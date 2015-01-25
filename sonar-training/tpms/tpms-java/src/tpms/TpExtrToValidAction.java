package tpms;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.TP;
import tol.LogWriter;
import tpms.utils.DBTrack;
import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;

import java.sql.SQLException;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

public class TpExtrToValidAction extends TpAction {
	
    private String division;

    public TpExtrToValidAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }

    public String getCCAction() {
        return "tp_extract";
    }

    private String generateQueryUpdateTpPlant() {
        String query = "";
        try {
            if (searchTp() == 0) {
                query = "INSERT INTO TP_PLANT " +
                        "( " +
                        " JOBNAME, " +
                        " JOB_RELEASE, " +
                        " JOB_REVISION, " +
                        " TPMS_VER, " +
                        " LINE, " +
                        " FACILITY, " +
                        " FROM_PLANT, " +
                        " OWNER_LOGIN," +
                        " OWNER_EMAIL," +
                        " ORIGIN," +
                        " ORIGIN_LBL," +
                        " LINESET_NAME," +
                        " TO_PLANT," +
                        " TESTER_INFO," +
                        " VALID_LOGIN," +
                        " PROD_LOGIN," +
                        " LAST_ACTION_ACTOR," +
                        " LAST_ACTION_DATE," +
                        " STATUS," +
                        " DISTRIB_DATE," +
                        " PROD_DATE," +
                        " DIVISION," +
                        " VOB_STATUS," +
                        " INSTALLATION_ID" +
                        ") " +
                        "VALUES " +
                        "( '" +
                        XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                        XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                        XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                        XmlUtils.getVal(tpRec, "JOB_VER") + ",'" +
                        XmlUtils.getVal(tpRec, "LINE") + "','" +
                        XmlUtils.getVal(tpRec, "FACILITY") + "','" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                        XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                        XmlUtils.getVal(tpRec, "OWNER_EMAIL") + "','" +
                        "','" +
                        "','" +
                        XmlUtils.getVal(tpRec, "LINESET") + "','" +
                        XmlUtils.getVal(tpRec, "TO_PLANT") + "','" +
                        XmlUtils.getVal(tpRec, "TESTER_INFO") + "','" +
                        XmlUtils.getVal(tpRec, "VALID_LOGIN") + "','" +
                        XmlUtils.getVal(tpRec, "PROD_LOGIN") + "','" +
                        userName + "'," +
                        "SYSDATE" + ",'" +
                        "In_Validation'," +
                        "SYSDATE,'" +
                        "','" +
                        division + "','" +
                        "OnLine" + "','" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") +
                        "')";
            } else {
                query = "UPDATE TP_PLANT SET" +
                        " LAST_ACTION_ACTOR ='" + userName +
                        "', LAST_ACTION_DATE = SYSDATE" +
                        ", STATUS ='In_Validation'" +
                        " WHERE " +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER");
            }
        } catch (Exception e) {
        	errorLog("TpExtrToValidAction :: generateQueryUpdateTpPlant unable to generate query...",e);
        }
        return query;
    }


    private String generateQueryUpdateTpHistory(String newVersion, String division, String userName) {
        String query = "";
        try {
            query = "INSERT INTO TP_HISTORY " +
                    "( " +
                    " JOBNAME, " +
                    " JOB_RELEASE, " +
                    " JOB_REVISION, " +
                    " TPMS_VER, " +
                    " ACTOR," +
                    " ACTION_DATE," +
                    " STATUS," +
                    " DIVISION," +
                    " VOB_STATUS," +
                    " OWNER_LOGIN," +
                    " INSTALLATION_ID," +
                    " LINESET_NAME," +
                    " PRODUCTION_AREA_ID " +
                    ") " +
                    "VALUES " +
                    "( '" +
                    XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                    XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                    XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                    newVersion + "," +
                    "'" + userName + "'," +
                    "SYSDATE,'" +
                    "In_Validation','" +
                    division + "','" +
                    "OnLine" + "','" +
                    XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    XmlUtils.getVal(tpRec, "LINESET") + "','"+
                    XmlUtils.getVal(tpRec, "AREA_PROD") +
                    "')";
        } catch (Exception e) {	
        	errorLog("TpExtrToValidAction :: generateQueryUpdateTpHistory : unable to generate query...",e);
        }
        return query;
    }

    private String generateQueryInsertIntoActionComment(String userName) {
    	
        String query = "";
        String temp_extToValidate_action_Comment = TP.getDbFormatCommentsFromFieldFormat(this.getExtToValidateCmt());
        if (temp_extToValidate_action_Comment.length() > TpAction.MAX_COMMENT_LENGTH){
        	temp_extToValidate_action_Comment = temp_extToValidate_action_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	} 
        debugLog("TpExtrToValidAction :: generateQueryInsertIntoActionComment : Action Comments =" + temp_extToValidate_action_Comment);
        try { 
        	if (searchTpActionCmt() == 0) {
        		query = "INSERT INTO ACTION_COMMENTS " +
        		"( " +
        		" JOBNAME, " +
        		" JOB_RELEASE, " +
        		" JOB_REVISION, " +
        		" TPMS_VER, " +
        		" COMMENT_BODY, " +
        		" CREATION_DATE, " + 
        		" STATUS," +
        		" LINESET_NAME," +
        		" FROM_PLANT" +
        		") " +
        		"VALUES " +
        		"( '" +
        		XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
        		XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
	            XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
	            XmlUtils.getVal(tpRec, "JOB_VER") + ",'" +
	            temp_extToValidate_action_Comment + "'," +
	            "SYSDATE" + ",'" +   
	            "In_Validation" + "','" +
	            XmlUtils.getVal(tpRec, "LINESET") + "','" +
	            XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
        	} else {
                query = "UPDATE ACTION_COMMENTS SET" +
                " COMMENT_BODY ='" + temp_extToValidate_action_Comment +
                "', CREATION_DATE = SYSDATE" +
                " WHERE " +
                " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                " STATUS='In_Validation'";
        	}
         } catch (Exception e) {
            errorLog("TpExtrToValidAction :: generateQueryInsertIntoActionComment : unable to generate query...", e);
        }
        debugLog("TpExtrToValidAction :: generateQueryInsertIntoActionComment("+ userName + ", " + division +") : " + query);
        return query;
    }    
    
    private void doDbTransaction(String queryUpdateTpPlant, String queryUpdateTpHistory, String insertActionComment) throws Exception {
    	
        debugLog("TpExtrToValidAction :: doDbTransaction : STARTED");

        try {
            dbwrt.submit(queryUpdateTpPlant);
            dbwrt.submit(queryUpdateTpHistory);
            dbwrt.submit(insertActionComment);
            dbwrt.commit();
        } catch (Exception e) {
            if (dbwrt != null) {
                try {
                    dbwrt.rollback();
                } catch (SQLException e1) {
                	errorLog("TpExtrToValidAction :: doDbTransaction : unable to rollback statements!");
                }
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
        debugLog("TpExtrToValidAction :: doDbTransaction : ENDED");
    }

    public void updateDb() throws Exception {
    	
        Exception DbException = null;
        
        boolean dbConnectionAvailable = QueryUtils.checkDbConnection(dbwrt);
        if (dbConnectionAvailable) {
            division = getDivision();
        } else {
            division = divisionNotAvailableValue;
        }
        
        String newVersion = XmlUtils.getVal(tpRec, "JOB_VER");
        String queryUpdateTpPlant = generateQueryUpdateTpPlant();
        
        //String newVersion, String division, String userName
        String queryUpdateTpHistory = generateQueryUpdateTpHistory(newVersion, this.division, this.userName);
        String insertActionComment = generateQueryInsertIntoActionComment(this.userName);
        
        boolean commitBool = false;
        // Take note that Action Comment may not contain a valid SQL statement if DB inaccessible.
        if (!GeneralStringUtils.isEmptyString(queryUpdateTpPlant) && !GeneralStringUtils.isEmptyString(queryUpdateTpHistory)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(queryUpdateTpPlant, queryUpdateTpHistory,insertActionComment);
                } catch (Exception e) {
                    DbException = e;
                    commitBool = false;
                    if (dbwrt != null) {
                        try {
                            dbwrt.checkConn();
                        } catch (SQLException e1) {
                        }
                    }
                    Thread.sleep(2000);
                }
            }
            if (!commitBool) {
                String [] queries = new String [3];
                queries[0] = queryUpdateTpPlant;
                queries[1] = queryUpdateTpHistory;
                queries[2] = insertActionComment;

                DBTrack.trackQueries(this.sessionId, this.userName, queries);
                debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                if (DbException != null) throw DbException;
            }
        } else {
            debugLog("TpExtrToValidAction :: updateDb : unable to get queries ( queryUpdateTpPlant = " + queryUpdateTpPlant + " queryUpdateTpHistory = " + queryUpdateTpHistory + " insertActionComment = " + insertActionComment);
        }
    }

    /* Do not throw exception here, assume old TP exists */
    private int searchTp(){
    	
        int result = -1;
        try {
        	Vector v = new Vector();
        	dbwrt.getRows(v,
                    "SELECT JOBNAME " +
                            "FROM TP_PLANT" +
                            " WHERE" +
                            " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                            " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                            " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                            " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        	result = v.size();
        } catch (Exception e) {
            errorLog("TpExtrToValidAction :: searchTp : unable to get tp number", e);
        }
        return result;
    }
    
    private int searchTpActionCmt() throws Exception {
    	
        int result = -1;

        Vector v = new Vector();
        dbwrt.getRows(v,
        		"SELECT JOBNAME " +
                "FROM ACTION_COMMENTS" +
                " WHERE" +
                " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                " STATUS='In_Validation'");
           
            result = v.size();
        return result;
    }
    
    private String getDivision() throws Exception {
    	
        String query = "SELECT DIVISION FROM USERS where TPMS_LOGIN='" +
                        XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "' AND INSTALLATION_ID='" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") + "'";

        debugLog("TpExtrToValidAction :: getDivision : QUERY = " + query);

        SQLInterface iface = new SQLInterface();
        CachedRowSet rs = iface.execQuery(query);
        String  result = "";
        if (rs != null){
        	while (rs.next()) { 
        		result = rs.getString(1);
        	}
        }
        return result;
    }
}