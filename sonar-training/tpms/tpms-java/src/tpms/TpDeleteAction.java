package tpms;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
//import org.w3c.dom.Element;
import tol.LogWriter;
import tpms.utils.DBTrack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 30, 2003
 * Time: 11:34:19 AM
 * To change this template use Options | File Templates.
 * FP rev5 - nuova classe per la gestione del TP Delete
 */
public class TpDeleteAction extends TpAction {
    public TpDeleteAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }


    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "tp");
        out.println("action=" + this.getCCAction());
        out.println("linesetname=" + XmlUtils.getVal(tpRec, "LINESET"));
        out.println("baseline=" + XmlUtils.getVal(tpRec, "LASTBASELINE"));
        out.println("jobname=" + XmlUtils.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + XmlUtils.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + XmlUtils.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        out.println("facility=" + XmlUtils.getVal(tpRec, "FACILITY"));
        out.println("tester_info=" + XmlUtils.getVal(tpRec, "TESTER_INFO"));
        out.println("line=" + XmlUtils.getVal(tpRec, "LINE"));
        out.println("xfer_path=" + XmlUtils.getVal(tpRec, "XFER_PATH"));
        out.println("to_plant=" + XmlUtils.getVal(tpRec, "TO_PLANT"));
        out.println("valid_login=" + XmlUtils.getVal(tpRec, "VALID_LOGIN"));
        out.println("prod_login=" + XmlUtils.getVal(tpRec, "PROD_LOGIN"));
        out.println("from_mail=" + XmlUtils.getVal(tpRec, "OWNER_EMAIL"));
        out.println("from_plant=" + XmlUtils.getVal(tpRec, "FROM_PLANT"));
        out.println("certification=" + XmlUtils.getVal(tpRec, "CERTIFICATION"));

        out.println("outfile=" + XmlUtils.getVal(tpRec, "repFileName"));
        String vob = (XmlUtils.getChild(tpRec, "VOB") != null ? XmlUtils.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));
        debugLog("TpDeleteAction :: writeBackEndInFile : VOB>" + vob);
        out.println("trans_vob_list=" + VobManager.getTvobsListAsString(vob));
        debugLog("TpDeleteAction :: writeBackEndInFile : VOB LST>" + VobManager.getTvobsListAsString(vob));
        //12 May 2006 FF: following line commented out because not needed for delete action: due to bug "id=ccam0025451 - TP Delete failure"
        //out.println("to_vob=" + VobManager.getTvobFromPlantForTpDeleteAction(vob, XmlUtils.getVal(tpRec, "TO_PLANT")));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        debugLog("TpDeleteAction :: writeBackEndInFile : SID>" + SID);
        debugLog("TpDeleteAction :: writeBackEndInFile : debug=" + TpActionServlet._cc_debug);

        debugLog("TpDeleteAction :: writeBackEndInFile : search owner user>");
        /*
        commeted out due to ccam0005451
        Element userData = CtrlServlet.getUserData(XmlUtils.getVal(tpRec, "OWNER_LOGIN"));
        debugLog("TpDeleteAction :: writeBackEndInFile : user owner ok>");
        String UnixGroup = XmlUtils.getVal(userData, "UNIX_GROUP");
        out.println("unix_group=" + UnixGroup);

        debugLog("TpDeleteAction :: writeBackEndInFile : unix_group>" + UnixGroup);
        //start FP REV 5
        String Division = XmlUtils.getVal(userData, "DIVISION");
        out.println("division=" + Division);
        debugLog("TpDeleteAction :: writeBackEndInFile : division>" + Division);
        */
        //stop
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        //FF 15/09/2005 aggiunta cancellazione per utente dei tp: necessario passare anche il ruolo

        debugLog("current user role>" + this.getUserRole());

        out.println("user_role=" + this.getUserRole());
        debugLog("start writhing In_file >");
        out.flush();
        debugLog("Close In_file >");
        out.close();
        debugLog("CC IN FILE CREATED>" + fName);
    }


    private String generateQueryDeleteFromTpPlant() {
        String query = "";
        try {
            query = "DELETE FROM TP_PLANT" +
                    " WHERE " +
                    " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                    " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                    " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                    " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                    " OWNER_LOGIN='" + XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "' AND " +
                    " FROM_PLANT='" + XmlUtils.getVal(tpRec, "FROM_PLANT") + "'";
        } catch (Exception e) {
            debug("TpDeleteAction :: generateQueryDeleteFromTpPlant : unable to generate query!!");
        }
        return query;
    }

    private String generateQueryDeleteFromTpHistory() {
        String query = "";
        try {
            query = "DELETE TP_HISTORY" +
                    " WHERE " +
                    " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                    " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                    " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                    " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
                    " OWNER_LOGIN='" + XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "' AND " +
                    " INSTALLATION_ID='" + XmlUtils.getVal(tpRec, "FROM_PLANT") + "'";

        } catch (Exception e) {
            debug("TpDeleteAction :: generateQueryDeleteFromTpHistory : unable to generate query!!");
        }
        return query;
    }
    
    private String generateQueryDeleteFromTp_Test_Delivery() {
        String query = "";
        try {
            query = "DELETE TP_TEST_DELIVERY" +
                    " WHERE " +
                    " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                    " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                    " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                    " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + "";
            debug("The Query is " + query); 

        } catch (Exception e) {
            debug("TpDeleteAction :: generateQueryDeleteFromTp_Test_Delivery : unable to generate query!!");
        }
        return query;
    }

    private String generateQueryDeleteFromAction_Comments() {
        String query = "";
        try {
            query = "DELETE ACTION_COMMENTS" +
                    " WHERE " +
                    " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                    " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                    " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                    " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + "";
            debug("The Query is " + query); 

        } catch (Exception e) {
            debug("TpDeleteAction :: generateQueryDeleteFromAction_Comments : unable to generate query!!");
        }
        return query;
    }
    
    public void doDbTransaction(String queryDeleteFromTpPlant, String queryDeleteFromTpHistory, String queryDeleteFromTp_Test_Delivery, String queryDeleteFromAction_Comments) throws Exception {
        debug("-START DB TRANSACTION-->Delete TP into TP_PLANT");
        try {
            dbwrt.submit(queryDeleteFromTpPlant);
            dbwrt.submit(queryDeleteFromTpHistory);
            dbwrt.submit(queryDeleteFromTp_Test_Delivery);
            dbwrt.submit(queryDeleteFromAction_Comments);
            dbwrt.commit();
        } catch (Exception e) {
            if (dbwrt != null) {
                try {
                    dbwrt.rollback();
                } catch (SQLException e1) {
                    debug("TpDeleteAction :: doDbTransaction : unable to rollback!");
                }
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
    }

    public void updateDb() throws Exception {
        Exception DbException = null;
        String queryDeleteFromTpPlant = generateQueryDeleteFromTpPlant();
        String queryDeleteFromTpHistory = generateQueryDeleteFromTpHistory();
        String queryDeleteFromTp_Test_Delivery = generateQueryDeleteFromTp_Test_Delivery();
        String queryDeleteFromAction_Comments = generateQueryDeleteFromAction_Comments();
        
        boolean commitBool = false;
        if (!GeneralStringUtils.isEmptyString(queryDeleteFromTpPlant) && !GeneralStringUtils.isEmptyString(queryDeleteFromTpHistory)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(queryDeleteFromTpPlant, queryDeleteFromTpHistory, queryDeleteFromTp_Test_Delivery,queryDeleteFromAction_Comments);
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
                String [] queries = new String [4];
                queries[0] = queryDeleteFromTpPlant;
                queries[1] = queryDeleteFromTpHistory;
                queries[2] = queryDeleteFromTp_Test_Delivery;
                queries[3] = queryDeleteFromAction_Comments;
                
                DBTrack.trackQueries(this.sessionId, this.userName, queries);
                debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                if (DbException != null) throw DbException;
            }
        } else {
            debug("TpDeleteAction :: updateDb : unable to get queries ( queryDeleteFromTpPlant = " + queryDeleteFromTpPlant + " queryDeleteFromTpHistory = " + queryDeleteFromTpHistory +  " queryDeleteFromTp_Test_Delivery = " + queryDeleteFromTp_Test_Delivery);
        }
    }

}
