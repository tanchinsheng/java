package tpms;

import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.TP;
import tol.LogWriter;
import tpms.utils.DBTrack;
import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 15-mag-2006
 * Time: 11.48.49
 * To change this template use File | Settings | File Templates.
 */
public class TpRejectAction extends TpAction {

    public TpRejectAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
        this.action = TpActionServlet.TP_REJECT_ACTION_VALUE;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {

        /**
         * user=vobadm
         rectype=tp
         action=tp_reject
         submit=1
         outfile=/export/home/vobadm/jakarta-tomcat-3.2.3/webapps/tpms/images/zthrv15et1_1144673022672_rep.xml
         vobname=REPLICA_TXT2MILANO
         session_id=zthrv15et1
         debug=0
         jobname=prova
         release_nb=00
         revision_nb=00
         version_nb=00
         to_mail=support.txt@st.com
         cc_mail2=
         cc_mail1=support.txt@st.com
         from_mail=support.txt@st.com
         comment1=
         comment0=
         */

        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("user=" + this.getUserName());
        out.println("rectype=tp");
        out.println("action=" + this.getCCAction());
        out.println("submit=1");
        out.println("outfile=" + repFileName);
        out.println("diff_outfile=" + repFileName);
        String vob = (XmlUtils.getChild(tpRec, "VOB") != null ? XmlUtils.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + tpmsConfiguration.getCCDebugClearcaseInterfaceValue());
        out.println("jobname=" + XmlUtils.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + XmlUtils.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + XmlUtils.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=" + XmlUtils.getVal(tpRec, "JOB_VER"));
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
        debugLog("TpRejectAction :: writeBackEndInFile : CC IN FILE CREATED>" + fName);
    }

    private String generateQueryUpdateTpPlant(String userName, String division) {
    	
        String query = "";
        try {
            if (countTps() == 0) {
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
                        "Rejected'," +
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
                        ", STATUS ='Rejected'" +
                        ", PROD_DATE = SYSDATE" +
                        ", VOB_STATUS = 'OnLine'" +
                        " WHERE " +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER");
            }
        } catch (Exception e) {
            errorLog("TpRejectAction :: generateQueryUpdateTpPlant : no exception thrown, unable to generate query...", e);
        }
        debugLog("TpRejectAction :: generateQueryUpdateTpPlant("+ userName + ", " + division +") : " + query);
        return query;
    }

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
                    "Rejected','" +
                    division + "','" +
                    "OnLine','" +
                    XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    XmlUtils.getVal(tpRec, "AREA_PROD") +
                    "')";
        } catch (Exception e) {
            errorLog("TpRejectAction :: generateQueryInsertIntoTpToHistory : no exception thrown : unable to generate query", e);
        }
        debugLog("TpRejectAction :: generateQueryInsertIntoTpToHistory("+ userName + ", " + division +") : " + query);
        return query;
    }
  
    private String generateQueryInsertIntoActionComment(String userName, String division) {
        
    	String query = "";
        String temp_reject_action_Comment = TP.getDbFormatCommentsFromFieldFormat(this.getRejectCmt());
        if (temp_reject_action_Comment.length() > TpAction.MAX_COMMENT_LENGTH){
        	temp_reject_action_Comment = temp_reject_action_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	} 
        debugLog("TpRejectAction  :: generateQueryInsertIntoActionComment : Action Comment =" + temp_reject_action_Comment);
        try { 
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
            temp_reject_action_Comment + "'," +
            "SYSDATE" + ",'" +   
            "Rejected" + "','" +
            XmlUtils.getVal(tpRec, "LINESET") + "','" +
            XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
         } catch (Exception e) {
            errorLog("TpRejectAction :: generateQueryInsertIntoActionComment : no exception thrown : unable to generate query", e);
        }
        debugLog("TpRejectAction :: generateQueryInsertIntoActionComment("+ userName + ", " + division +") : " + query);
        return query;
    }
    
    public void doDbTransaction() throws Exception {
    	
        debugLog("TpRejectAction :: doDbTransaction : STARTED");
        String division;
        boolean dbConnectionAvailable = QueryUtils.checkDbConnection(dbwrt);
        if (dbConnectionAvailable) {
            division = getDivision();
        } else {
            division = divisionNotAvailableValue;
        }
        String insertTpInTpHistory = generateQueryInsertIntoTpToHistory(userName, division);
        debugLog("TpRejectAction :: doDbTransaction : tp_history query = " + insertTpInTpHistory);
        
        String updateTpInTpPlant = generateQueryUpdateTpPlant(userName, division);
        debugLog("TpRejectAction :: doDbTransaction : tp_plant query = " + updateTpInTpPlant);

        String insertActionComment = generateQueryInsertIntoActionComment(userName, division);
        debugLog("TpRejectAction :: doDbTransaction : action_comments query = " + insertActionComment);
        
        if (dbConnectionAvailable) {
            executeUpdateQuery(insertTpInTpHistory, sessionId, userName, false);
            executeUpdateQuery(updateTpInTpPlant, sessionId, userName, false);
            executeUpdateQuery(insertActionComment, sessionId, userName, false);
            dbWriter.commit();
            debugLog("TpRejectAction :: doDbTransaction : commit done");
        } else {
           String [] queries = new String[3];
           queries[0] = insertTpInTpHistory;
           queries[1] = updateTpInTpPlant;
           queries[2] = insertActionComment;
            try {
                DBTrack.trackQueries(sessionId, userName, queries);
            } catch (IOException e) {
                errorLog("TpRejectAction :: doDbTransaction : unable to track lost queries : " + e.getMessage(), e);
            }
        }

        debugLog("TpRejectAction :: doDbTransaction : ENDED");
    }

    public void updateDb() throws Exception {
        doDbTransaction();
    }

    private int countTps() throws Exception {
    	
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
            errorLog("TpRejectAction :: countTps : unable to get tp number", e);
        }
        return result;
    }

    private String getDivision() throws Exception {
    	
        String query = "SELECT DIVISION FROM USERS where TPMS_LOGIN='" +
                        XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "' AND INSTALLATION_ID='" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") + "'";

        debugLog("TpRejectAction :: getDivision : query = " + query);

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
