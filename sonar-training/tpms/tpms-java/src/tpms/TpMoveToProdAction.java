package tpms;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.TP;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import tol.LogWriter;
import tpms.utils.DBTrack;
import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;
import tpms.utils.TpmsConfiguration;
public class TpMoveToProdAction extends TpAction {
	
    private String division;

    public TpMoveToProdAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException {
    	
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("user=" + this.getUserName());
        out.println("rectype=" + "tp");
        out.println("action=" + this.getCCAction());
        out.println("submit=1");
        out.println("outfile=" + repFileName);
        out.println("diff_outfile=" + repFileName);
        String vob = (XmlUtils.getChild(tpRec, "VOB") != null ? XmlUtils.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + 0);
        out.println("jobname=" + XmlUtils.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + XmlUtils.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + XmlUtils.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        out.println("tester_info=" + TesterInfoMgr.getTesterInfoValFromDesk(XmlUtils.getVal(tpRec, "TO_PLANT"), XmlUtils.getVal(tpRec, "TESTER_INFO")));
        /**
         * Actual available properties list:
         * - extract_dir
         * - from_mail=from@txt.it
         * - cc_mail1=cc1@txt.it
         * - cc_mail2=cc2@txt.it
         */
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        out.flush();
        out.close();
        debugLog("TpMoveToProdAction :: writeBackEndInFile : CC IN FILE CREATED>" + fName);
        
    }

    /** Caller: putToProduction() 
     * <p>UPDATE from 'In_Production' to 'Obsolete' based on JOBNAME, JOB_REL and TO_PLANT */
    private String generateQuerySetTpObsoleteInTpPlant() {
    	
        String query = "";
        try {
            query = "UPDATE TP_PLANT SET" +
                    " LAST_ACTION_ACTOR ='" + userName +
                    "', LAST_ACTION_DATE = SYSDATE" +
                    ", STATUS ='Obsolete'" +
                    " WHERE " +
                    " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                    " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                    " TO_PLANT='" + XmlUtils.getVal(tpRec, "TO_PLANT") + "' AND " +
                    " STATUS=" + "'In_Production'";
        } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQuerySetTpObsoleteInTpPlant : unable to generate query " 
            		+ e.getMessage(), e);
        }

        debugLog("TpMoveToProdAction :: generateQuerySetTpObsoleteInTpPlant : QUERY = " + query);
        return query;
        
    }

    /** Caller: putToProduction() 
     *  <p>Insert Into TP_HISTORY (JOB_REVISION,TPMS_VER,STATUS) VALUES (jobRevision,tpmsVersion,'Obsolete') */
    private String generateQuerySetTpObsoleteInTpHistory(String tpmsVersion, String jobRevision) {
    	
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
                    jobRevision + "'," +
                    tpmsVersion + "," +
                    "'" + userName + "'," +
                    "SYSDATE,'" +
                    "Obsolete','" +
                    division + "','" +
                    "OnLine','" +
                    XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    XmlUtils.getVal(tpRec, "AREA_PROD") +
                    "')";
        } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQuerySetTpObsoleteInTpHistory : unable to generate query: " + e.getMessage(), e);
        }
        debugLog("TpMoveToProdAction :: generateQuerySetTpObsoleteInTpHistory(" + tpmsVersion + ") : " + query);
        return query;
    }

    /** Caller: putToProduction() 
     *  <p>Check SELECT JOBNAME FROM TP_PLANT based on JOBNAME,JOB_RELEASE,TPMS_VER and TO_PLANT 
     *  <p>If none found, INSERT INTO TP_PLANT (STATUS) VALUES ('In_Production') 
     *  <p>else UPDATE TP_PLANT with STATUS ='In_Production' */
    private String generateQueryUpdateTpPlant(String userName, String division) {
    	
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
                        "In_Production'," +
                        "''," +
                        "SYSDATE,'" +
                        division + "','" +
                        "OnLine','" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") +
                        "')";
            } else {
                query = "UPDATE TP_PLANT SET" +
                        " LAST_ACTION_ACTOR ='" + userName +
                        "', LAST_ACTION_DATE = SYSDATE" +
                        ", STATUS ='In_Production'" +
                        ", PROD_DATE = SYSDATE" +
                        ", VOB_STATUS = 'OnLine'" +
                        " WHERE " +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER");
            }
        } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQueryUpdateTpPlant : unable to generate query: " + e.getMessage(), e);
        }
        debugLog("TpMoveToProdAction :: generateQueryUpdateTpPlant(" + userName + ", " + division + ") : " + query);
        return query;
        
    }

    /** Caller : putToProduction() 
     *  <p>INSERT INTO TP_HISTORY (STATUS) VALUES ('In_Production') */
    private String generateQueryInsertIntoTpToHistory(String userName, String division) {
    	
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
                    " PRODUCTION_AREA_ID "+
                    ") " +
                    "VALUES " +
                    "( '" +
                    XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                    XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                    XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                    XmlUtils.getVal(tpRec, "JOB_VER") + "," +
                    "'" + userName + "'," +
                    "SYSDATE,'" +
                    "In_Production','" +
                    division + "','" +
                    "OnLine','" +
                    XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    XmlUtils.getVal(tpRec, "AREA_PROD") +
                    "')";
        } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQueryInsertIntoTpToHistory : unable to generate query: " + e.getMessage(), e);
        }
        debugLog("TpMoveToProdAction :: generateQueryInsertIntoTpToHistory(" + userName + ", " + division + ") : " + query);
        return query;
        
    }

    /** Caller : putToProduction() 
     *  <p>Call searchTpActionCmt() to check 
     *  <p>.....SELECT JOBNAME FROM ACTION_COMMENTS based on JOBNAME,JOB_RELEASE,JOB_REVISION, TPMS_VER, STATUS='In_Production'
     *  <p>If none found,.....INSERT INTO ACTION_COMMENTS (STATUS) VALUES ('In_Production') 
     *  <p>else.....UPDATE ACTION_COMMENTS with new COMMENT_BODY */
    private String generateQueryInsertIntoActionComment(String userName) {
    	
        String query = "";
        String temp_putInProdCmt_action_Comment = TP.getDbFormatCommentsFromFieldFormat(this.getPutInProdCmt());
        if (temp_putInProdCmt_action_Comment.length() > TpAction.MAX_COMMENT_LENGTH){
        	temp_putInProdCmt_action_Comment = temp_putInProdCmt_action_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	} 
        debugLog("TpMoveToProdAction :: generateQueryInsertIntoActionComment : Action Comment =" + temp_putInProdCmt_action_Comment);
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
	            temp_putInProdCmt_action_Comment + "'," +
	            "SYSDATE" + ",'" +   
	            "In_Production" + "','" +
	            XmlUtils.getVal(tpRec, "LINESET") + "','" +
	            XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
        	} else {
                query = "UPDATE ACTION_COMMENTS SET" +
                " COMMENT_BODY ='" + temp_putInProdCmt_action_Comment +
                "', CREATION_DATE = SYSDATE" +
                " WHERE " +
                " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                " STATUS='In_Production'";
        	}
         } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQueryInsertIntoActionComment : no exception thrown : unable to generate query", e);
        }
        debugLog("TpMoveToProdAction :: generateQueryInsertIntoActionComment("+ userName + ", " + division +") : " + query);
        return query;
    }        
    
    /** Caller : generateQueryInsertIntoActionComment(String)
     *  SELECT JOBNAME FROM ACTION_COMMENTS based on
     *  JOBNAME,JOB_RELEASE,JOB_REVISION, TPMS_VER, STATUS='In_Production' */
    private int searchTpActionCmt() throws Exception {
    	
        int result = -1;
        try {
            Vector v = new Vector();
            dbwrt.getRows(v,
                    "SELECT JOBNAME " +
                            "FROM ACTION_COMMENTS" +
                            " WHERE" +
                            " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                            " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                            " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                            " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                            " STATUS='In_Production'");
            result = v.size();
        } catch (Exception e) {
        	errorLog("TpMoveToProdAction :: searchTpActionCmt : unable to count TP!");
        }
        return result;
    }
    
    /** Caller : Sent2TPLib() 
     *  <p>Do searchTpSent2TPLibActionCmt() to check
     *  <p>....SELECT JOBNAME FROM ACTION_COMMENTS based on JOBNAME,JOB_RELEASE,TPMS_VER,STATUS='Sent2TPLib' 
     *  <p>If none found, ...INSERT INTO ACTION_COMMENTS (STATUS) VALUES ('Sent2TPLib') 
     *  <p>else ...UPDATE ACTION_COMMENTS with new COMMENT_BODY */
    private String generateQueryInsertIntoSent2TPLibActionComment(String userName) {
    	
        String query = "";
        String temp_putInProdCmt_action_Comment = TP.getDbFormatCommentsFromFieldFormat(this.getPutInProdCmt());
        if (temp_putInProdCmt_action_Comment.length() > TpAction.MAX_COMMENT_LENGTH){
        	temp_putInProdCmt_action_Comment = temp_putInProdCmt_action_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	} 
        try { 
        	if (searchTpSent2TPLibActionCmt() == 0) {
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
	            temp_putInProdCmt_action_Comment + "'," +
	            "SYSDATE" + ",'" +   
	            "Sent2TPLib" + "','" +
	            XmlUtils.getVal(tpRec, "LINESET") + "','" +
	            XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
        	} else {
                query = "UPDATE ACTION_COMMENTS SET" +
                " COMMENT_BODY ='" + temp_putInProdCmt_action_Comment +
                "', CREATION_DATE = SYSDATE" +
                " WHERE " +
                " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                " STATUS='Sent2TPLib'";
        	}
         } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQueryInsertIntoSent2TPLibActionComment : no exception thrown : unable to generate query", e);
        }
        debugLog("TpMoveToProdAction :: generateQueryInsertIntoSent2TPLibActionComment("+ userName + ", " + division +") : " + query);
        return query;
    }        
    
    /** Caller : generateQueryInsertIntoSent2TPLibActionComment(String) 
     *  <p>SELECT JOBNAME FROM ACTION_COMMENTS based on
     *  <p>JOBNAME,JOB_RELEASE,TPMS_VER,STATUS='Sent2TPLib' */
    private int searchTpSent2TPLibActionCmt() throws Exception {
    	
        int result = -1;
        try {
            Vector v = new Vector();
            dbwrt.getRows(v,
                    "SELECT JOBNAME " +
                            "FROM ACTION_COMMENTS" +
                            " WHERE" +
                            " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                            " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                            " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                            " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                            " STATUS='Sent2TPLib'");
            result = v.size();
        } catch (Exception e) {
        	errorLog("TpMoveToProdAction :: searchTpActionCmt : unable to count TP");
        }
        return result;
    }
    
    /** Caller: putToProduction() */
    private void doDbTransaction(String querySetTpObsoleteInTpPlant, 
    	Vector querySetTpObsoleteInTpHistory, String queryUpdateTpPlant, 
    	String queryInsertIntoTpToHistory, String queryInsertIntoActionComment) throws Exception {
        try {
            dbwrt.submit(querySetTpObsoleteInTpPlant);
            debugLog("TpMoveToProdAction :: doDbTransaction : querySetTpObsoleteInTpPlant = " + querySetTpObsoleteInTpPlant);
            int querySetTpObsoleteInTpPlantCount = querySetTpObsoleteInTpHistory.size();
            for (int i = 0; i < querySetTpObsoleteInTpPlantCount; i++) {
                String qry = (String) querySetTpObsoleteInTpHistory.get(i);
                dbwrt.submit(qry);
                debugLog("TpMoveToProdAction :: doDbTransaction : " + i + " querySetTpObsoleteInTpHistory = " + qry);
            }
            dbwrt.submit(queryUpdateTpPlant);
            debugLog("TpMoveToProdAction :: doDbTransaction : queryUpdateTpPlant = " + queryUpdateTpPlant);
            dbwrt.submit(queryInsertIntoTpToHistory);
            debugLog("TpMoveToProdAction :: doDbTransaction : queryInsertIntoTpToHistory = " + queryInsertIntoTpToHistory);
            dbwrt.submit(queryInsertIntoActionComment);
            debugLog("TpMoveToProdAction :: doDbTransaction : queryInsertIntoActionComment = " + queryInsertIntoActionComment);
            dbwrt.commit();
            debugLog("TpMoveToProdAction :: doDbTransaction : commit done.");
        } catch (Exception e) {
            errorLog("TpMoveToProdAction :: doDbTransaction exception = " + e.getMessage(), e);
            if (dbwrt != null) {
                try {
                    dbwrt.rollback();
                } catch (SQLException e1) {
                    debugLog("TpMoveToProdAction :: doDbTransaction A : unable to rollback!");
                }
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
    }

    /** Caller : sent2TPLib(),putToProduction()  */
    private void doDbTransaction(String queryUpdateTpPlant, 
    		String queryInsertIntoTpToHistory, 
    		String queryInsertIntoActionComment) throws Exception {
    	
        try {
            dbwrt.submit(queryUpdateTpPlant);
            dbwrt.submit(queryInsertIntoTpToHistory);
            dbwrt.submit(queryInsertIntoActionComment);
            dbwrt.commit();
        } catch (Exception e) {
            if (dbwrt != null) {
                try {
                    dbwrt.rollback();
                } catch (SQLException e1) {
                    debugLog("TpMoveToProdAction :: doDbTransaction B : unable to rollback!");
                }
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
    }

    /** Caller : VobActionDaemon.run() */
    public void updateDb() throws Exception{
    	
    	TpmsConfiguration tpmsConfig = TpmsConfiguration.getInstance();
        boolean dbConnectionAvailable = QueryUtils.checkDbConnection(dbwrt);
        if (dbConnectionAvailable) {
            division = getDivision();
        } else {
            division = divisionNotAvailableValue;
        }
    	if( tpmsConfig.isTPLibAvailable() ){
    		debugLog("TpMoveToProdAction :: updateDb : send2TPLib");
    		sent2TPLib(); //TPLIB Available    		
    	}else{
    		debugLog("TpMoveToProdAction :: updateDb : putToProduction");
    		putToProduction(); //TPLIB Not available  
    	}
    }

    /** Caller: putToProduction() 
     *  <p>SELECT TPMS_VER, JOB_REVISION FROM TP_PLANT based on 
     *  <p>JOBNAME, JOB_RELEASE, TO_PLANT, STATUS='In_Production' */
    private Vector searchTpInProduction() throws Exception{
    	
        Vector result = new Vector();
        try {
            debugLog("TpMoveToProdAction :: searchTpInProduction : STARTED");

            ResultSet rs = dbwrt.getRecordset(
                    "SELECT TPMS_VER, JOB_REVISION " +
                            "FROM TP_PLANT" +
                            " WHERE" +
                            " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                            " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                            " TO_PLANT='" + XmlUtils.getVal(tpRec, "TO_PLANT") + "' AND " +
                            " STATUS=" + "'In_Production'");
            debugLog("TpMoveToProdAction :: searchTpInProduction : ENDED");
            result = VectorUtils.dumpResultSetToVectorOfHashtable(rs);
            rs.close();

        } catch (Exception e) {
            debugLog("TpMoveToProdAction :: searchTpInProduction : unable to get tps");
        }
        return result;
    }

    /** Caller: generateQueryUpdateTpPlant(String,String), generateQuerySetTpSent2TPLibInTpPlant(String,String) 
     *  <p>SELECT JOBNAME FROM TP_PLANT based on JOBNAME,JOB_RELEASE,JOB_REVISION, TPMS_VER and TO_PLANT */
    private int searchTp() throws Exception{
    	
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
                            " TO_PLANT='" + XmlUtils.getVal(tpRec, "TO_PLANT") + "' AND " +
                            " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER"));
            result = v.size();
        } catch (Exception e) {
            debugLog("TpMoveToProdAction :: searchTp : unable to count tp");
        }
        return result;
    }

    private String getDivision() throws Exception {
    	 	
        String query = "SELECT DIVISION FROM USERS where TPMS_LOGIN='" +
                        XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "' AND INSTALLATION_ID='" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") + "'";

        debugLog("TpMoveToProdAction :: getDivision : QUERY = " + query);
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
     
    /** Caller : updateDb() */
     private void sent2TPLib() throws Exception {
    	
        boolean dbConnectionAvailable = QueryUtils.checkDbConnection(dbwrt);
        boolean commitBool = false;
    	String querySetTpSent2TPLibInTpPlant = generateQuerySetTpSent2TPLibInTpPlant(userName, division);
    	String querySetTpSent2TPLibInTpHistory = generateQuerySetTpSent2TPLibInTpHistory(userName, division);
    	String queryInsertIntoSent2TPLibActionComment = generateQueryInsertIntoSent2TPLibActionComment(userName);
    	
        if (dbConnectionAvailable) { 
            if (!GeneralStringUtils.isEmptyString(getDivision()) && 
            	!GeneralStringUtils.isEmptyString(querySetTpSent2TPLibInTpPlant) && 
            	!GeneralStringUtils.isEmptyString(querySetTpSent2TPLibInTpHistory) && 
            	!GeneralStringUtils.isEmptyString(queryInsertIntoSent2TPLibActionComment)){
            	for (int i = 0; ((!commitBool) && (i < 3)); i++){
            		commitBool = true;
                    try {
                    	this.doDbTransaction(querySetTpSent2TPLibInTpPlant, 
                    			querySetTpSent2TPLibInTpHistory,
                    			queryInsertIntoSent2TPLibActionComment);
                    } catch (Exception e) {
                    	commitBool = false;
                        if (dbwrt != null) {
                        	try {
                        		dbwrt.checkConn();
                            } catch (SQLException e1) {
                                debugLog("TpMoveToProdAction :: sent2TPLib : no db connection track queries into log files");
                                String [] queries = new String[3];
                                queries[0] = querySetTpSent2TPLibInTpPlant;
                                queries[1] = querySetTpSent2TPLibInTpHistory;
                                queries[2] = queryInsertIntoSent2TPLibActionComment;
                                DBTrack.trackQueries(sessionId, userName, queries);
                            }
                        }
                        Thread.sleep(2000);
                    }
            	}
            } else {
            	debugLog("TpMoveToProdAction :: sent2TPLib : unable to get queries ( querySetTpSent2TPLibInTpPlant = " 
            											 + querySetTpSent2TPLibInTpPlant 
            	+ "\nquerySetTpSent2TPLibInTpHistory = " + querySetTpSent2TPLibInTpHistory
            	+ "\nqueryInsertIntoSent2TPLibActionComment = " + queryInsertIntoSent2TPLibActionComment);
            }          
        } else {
            // the database is not available.... track the queries to log file!
            debugLog("TpMoveToProdAction :: sent2TPLib : no db connection track queries into log files");
            String [] queries = new String[3];
            queries[0] = querySetTpSent2TPLibInTpPlant;
            queries[1] = querySetTpSent2TPLibInTpHistory;
            queries[2] = queryInsertIntoSent2TPLibActionComment;
            DBTrack.trackQueries(sessionId, userName, queries);
        }
    }
    
    /** Caller : sent2TPLib() 
     *  <p>Do searchTP () to check 
     *  <p>....SELECT JOBNAME FROM TP_PLANT based on JOBNAME,JOB_RELEASE,
     *  <p>....JOB_REVISION, TPMS_VER and TO_PLANT 
     *	<p>If none found, INSERT INTO TP_PLANT (STATUS) VALUES ('Sent2TPLib') 
     *  <p>else UPDATE TP_PLANT STATUS ='Sent2TPLib' */
    private String generateQuerySetTpSent2TPLibInTpPlant(String userName, String division){
    	
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
                        "Sent2TPLib'," +
                        "''," +
                        "SYSDATE,'" +
                        division + "','" +
                        "OnLine','" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") +
                        "')";
            } else {
                query = "UPDATE TP_PLANT SET" +
                        " LAST_ACTION_ACTOR ='" + userName +
                        "', LAST_ACTION_DATE = SYSDATE" +
                        ", STATUS ='Sent2TPLib'" +
                        ", PROD_DATE = SYSDATE" +
                        ", VOB_STATUS = 'OnLine'" +
                        " WHERE " +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER");
            }
        } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQuerySetTpSent2TPLibInTpPlant : unable to generate query: " + e.getMessage(), e);
        }
        debugLog("TpMoveToProdAction :: generateQuerySetTpSent2TPLibInTpPlant(" + userName + ", " + division + ") : " + query);
        
        return query;
    }
    
    /** Caller: send2TPLib() 
     *  <p>INSERT INTO TP_HISTORY (STATUS) VALUES ('Sent2TPLib') */
    private String generateQuerySetTpSent2TPLibInTpHistory(String userName, String division){
    	
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
                    " PRODUCTION_AREA_ID "+
                    ") " +
                    "VALUES " +
                    "( '" +
                    XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                    XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                    XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                    XmlUtils.getVal(tpRec, "JOB_VER") + "," +
                    "'" + userName + "'," +
                    "SYSDATE,'" +
                    "Sent2TPLib','" +
                    division + "','" +
                    "OnLine','" +
                    XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    XmlUtils.getVal(tpRec, "AREA_PROD") +
                    "')";
        } catch (Exception e) {
            errorLog("TpMoveToProdAction :: generateQuerySetTpSent2TPLibInTpHistory : unable to generate query: " + e.getMessage(), e);
        }
        debugLog("TpMoveToProdAction :: generateQuerySetTpSent2TPLibInTpHistory(" + userName + ", " + division + ") : " + query);
        
        return query;
    }
    
    /** Caller: updateDb() */
    private void putToProduction() throws Exception{
    	
        boolean dbConnectionAvailable = QueryUtils.checkDbConnection(dbwrt);
        
        String querySetTpObsoleteInTpPlant = generateQuerySetTpObsoleteInTpPlant();
        String queryUpdateTpPlant = generateQueryUpdateTpPlant(userName, division);
        String queryInsertIntoTpToHistory = generateQueryInsertIntoTpToHistory(userName, division);
        String queryInsertIntoActionComment = generateQueryInsertIntoActionComment(userName);
        
        Vector querySetTpObsoleteInTpHistory = new Vector();
        boolean commitBool = false;
        
        if (dbConnectionAvailable) {
            Vector tpInProd = searchTpInProduction();
            debugLog("TpMoveToProdAction :: putToProduction : TpInProd Founded = " + tpInProd.size());
            if (tpInProd.size() != 0) {
                //if this is not the first time the TP is been put in production....
                String tpmsVersion;
                String jobRevision;
                for (int z = 0; z < tpInProd.size(); z++) {
                    tpmsVersion = (String) ((Hashtable) tpInProd.get(z)).get("TPMS_VER");
                    jobRevision = (String) ((Hashtable) tpInProd.get(z)).get("JOB_REVISION");
                    debugLog("TpMoveToProdAction :: putToProduction : JOB_REVISION = " + jobRevision 
                    		+ " TPMS_VER = " + tpmsVersion);
                    querySetTpObsoleteInTpHistory.add(generateQuerySetTpObsoleteInTpHistory(tpmsVersion, 
                    		jobRevision));
                }
                if (!GeneralStringUtils.isEmptyString(querySetTpObsoleteInTpPlant) && 
                	querySetTpObsoleteInTpHistory != null &&
                    !GeneralStringUtils.isEmptyString(queryUpdateTpPlant) && 
                    !GeneralStringUtils.isEmptyString(queryInsertIntoTpToHistory) &&
                    !GeneralStringUtils.isEmptyString(queryInsertIntoActionComment)){
                    for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                        commitBool = true;
                        try {
                        	/* UPDATE all TPs in TP_PLANT for same release */
                            this.doDbTransaction(querySetTpObsoleteInTpPlant, 
                            /*INSERT INTO all TPs in TP_HISTORY for same release*/
                            querySetTpObsoleteInTpHistory, 
                            queryUpdateTpPlant, 
                            queryInsertIntoTpToHistory,
                            queryInsertIntoActionComment);
                        } catch (Exception e) {
                            errorLog("TpMoveToProdAction :: putToProduction : Exception = " 
                            		+ e.getMessage(), e);
                            commitBool = false;
                            if (dbwrt != null) {
                                try {
                                    dbwrt.checkConn();
                                } catch (SQLException e1) {
                                    debugLog("TpMoveToProdAction :: putToProduction A : unable to roll back statements");
                                }
                            }
                            Thread.sleep(2000);
                        }
                    }
                } else {
                    debugLog("TpMoveToProdAction :: putToProduction : unable to get queries ( querySetTpObsoleteInTpPlant = "
                    		+ querySetTpObsoleteInTpPlant +
                            "\nquerySetTpObsoleteInTpHistory = " + querySetTpObsoleteInTpHistory +
                            "\nqueryUpdateTpPlant = " + queryUpdateTpPlant +
                            "\nqueryInsertIntoTpToHistory = " + queryInsertIntoTpToHistory + 
                            "\nqueryInsertIntoActionComment = " + queryInsertIntoActionComment);
                }
            } else {
                /* if this is the first time we have to put the TP in production....*/
                if (!GeneralStringUtils.isEmptyString(queryUpdateTpPlant) && 
                	!GeneralStringUtils.isEmptyString(queryInsertIntoTpToHistory) && 
                	!GeneralStringUtils.isEmptyString(queryInsertIntoActionComment))
                {
                    for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                        commitBool = true;
                        try {
                            this.doDbTransaction(queryUpdateTpPlant, queryInsertIntoTpToHistory,queryInsertIntoActionComment);
                        } catch (Exception e) {
                            commitBool = false;
                            if (dbwrt != null) {
                                try {
                                    dbwrt.checkConn();
                                } catch (SQLException e1) {
                                    debugLog("TpMoveToProdAction :: putToProduction B : unable to roll back statements");
                                }
                            }
                            Thread.sleep(2000);
                        }
                    }
                } else {
                    debugLog("TpMoveToProdAction :: putToProduction : unable to get queries ( queryUpdateTpPlant = "
                    										    + queryUpdateTpPlant +
                            "\nqueryInsertIntoTpToHistory = "   + queryInsertIntoTpToHistory + 
                    		"\nqueryInsertIntoActionComment = " + queryInsertIntoActionComment + " )");
                }
            }
        } else {
            /* the database is not available.... track the queries to log files! */
            debugLog("TpMoveToProdAction :: putToProduction : no db connection track queries into log files");
            String [] queries = new String[4];
            queries[0] = querySetTpObsoleteInTpPlant;
            queries[1] = queryUpdateTpPlant;
            queries[2] = queryInsertIntoTpToHistory;
            queries[3] = queryInsertIntoActionComment;
            DBTrack.trackQueries(sessionId, userName, queries);
        }
    }
}