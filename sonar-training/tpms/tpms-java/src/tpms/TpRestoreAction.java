package tpms;

import it.txt.general.utils.GeneralStringUtils;
import org.w3c.dom.Element;
import tol.LogWriter;
import tol.xmlRdr;
import tpms.utils.DBTrack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 30, 2003
 * Time: 11:34:19 AM
 * To change this template use Options | File Templates.
 * FP rev5 - nuova classe per la gestione del TP Delete
 */
public class TpRestoreAction extends TpAction {
    public TpRestoreAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }


    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "tp");
        out.println("action=" + this.getCCAction());
        out.println("outfile=" + repFileName);
        String linesetName = getLineSetName();
        log.p("linesetName>" + linesetName);
        out.println("linesetname=" + linesetName);
        out.println("jobname=" + xmlRdr.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + xmlRdr.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + xmlRdr.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=" + xmlRdr.getVal(tpRec, "JOB_VER"));
        out.println("facility=" + xmlRdr.getVal(tpRec, "FACILITY"));
        out.println("tester_info=" + xmlRdr.getVal(tpRec, "TESTER_INFO"));
        out.println("line=" + xmlRdr.getVal(tpRec, "LINE"));
        out.println("from_mail=" + xmlRdr.getVal(tpRec, "OWNER_EMAIL"));
        out.println("from_plant=" + xmlRdr.getVal(tpRec, "FROM_PLANT"));
        log.p("START getVobName()>");
        String vob = getVobName(linesetName);
        log.p("VOB>" + vob);
        out.println("vobname=" + vob);
        out.println("to_vob=" + VobManager.getTvobFromPlant(vob, (String) xmlRdr.getVal(tpRec, "TO_PLANT")));
        out.println("session_id=" + SID);
        log.p("SID>" + SID);
        out.println("debug=" + TpActionServlet._cc_debug);

        log.p("search user>");
        Element userData = CtrlServlet.getUserData(xmlRdr.getVal(tpRec, "OWNER_LOGIN"));
        log.p("user ok>");
        String UnixGroup = xmlRdr.getVal(userData, "UNIX_GROUP");
        out.println("unix_group=" + UnixGroup);
        log.p("unix_group>" + UnixGroup);
        //start FP REV 5
        String Division = xmlRdr.getVal(userData, "DIVISION");
        out.println("division=" + Division);
        log.p("division>" + Division);
        //stop
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        log.p("start writhing In_file >");
        out.flush();
        log.p("Close In_file >");
        out.close();
        debug("CC IN FILE CREATED>" + fName);
    }


    private String generateQueryUpdateTpPlant() {
        String query = null;
        try {
            query = "UPDATE TP_PLANT SET" +
                    " VOB_STATUS ='OnLine'" +
                    " WHERE " +
                    " JOBNAME='" + xmlRdr.getVal(tpRec, "JOBNAME") + "' AND " +
                    " JOB_RELEASE=" + xmlRdr.getVal(tpRec, "JOB_REL") + " AND " +
                    " JOB_REVISION='" + xmlRdr.getVal(tpRec, "JOB_REV") + "' AND " +
                    " TPMS_VER=" + xmlRdr.getVal(tpRec, "JOB_VER") + " AND " +
                    " OWNER_LOGIN='" + xmlRdr.getVal(tpRec, "OWNER_LOGIN") + "' AND " +
                    " FROM_PLANT='" + xmlRdr.getVal(tpRec, "FROM_PLANT") + "'";
        } catch (Exception e) {
            debug("TpRestoreAction :: generateQueryUpdateTpPlant : unable to generate query");
        }

        return query;
    }

    private String generateQueryUpdateTpHistory() {
        String query = null;
        try {
            query = "UPDATE TP_HISTORY SET" +
                    " VOB_STATUS ='OnLine'" +
                    " WHERE " +
                    " JOBNAME='" + xmlRdr.getVal(tpRec, "JOBNAME") + "' AND " +
                    " JOB_RELEASE=" + xmlRdr.getVal(tpRec, "JOB_REL") + " AND " +
                    " JOB_REVISION='" + xmlRdr.getVal(tpRec, "JOB_REV") + "' AND " +
                    " TPMS_VER=" + xmlRdr.getVal(tpRec, "JOB_VER") + " AND " +
                    " OWNER_LOGIN='" + xmlRdr.getVal(tpRec, "OWNER_LOGIN") + "' AND " +
                    " INSTALLATION_ID='" + xmlRdr.getVal(tpRec, "FROM_PLANT") + "'";
        } catch (Exception e) {
            debug("TpRestoreAction :: generateQueryUpdateTpPlant : unable to generate query");
        }

        return query;
    }

    private void doDbTransaction(String queryUpdateTpPlant, String queryUpdateTpHistory) throws Exception {
        try {
            dbwrt.submit(queryUpdateTpPlant);
            dbwrt.submit(queryUpdateTpHistory);
            dbwrt.commit();
        } catch (Exception e) {
            if (dbwrt != null) {
                try {
                    dbwrt.rollback();
                } catch (SQLException e1) {
                    debug("TpRestoreAction :: doDbTransaction : unable to rollback!");
                }
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
        debug("Update OnLine into TP_HISTORY ENDED");
    }


    public void updateDb() throws Exception {
        Exception DbException = null;
        String queryUpdateTpPlant = generateQueryUpdateTpPlant();
        String queryUpdateTpHistory = generateQueryUpdateTpHistory();
        boolean commitBool = false;
        if (!GeneralStringUtils.isEmptyString(queryUpdateTpPlant) && !GeneralStringUtils.isEmptyString(queryUpdateTpHistory)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(queryUpdateTpPlant, queryUpdateTpHistory);
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
                String [] queries = new String [2];
                queries[0] = queryUpdateTpPlant;
                queries[1] = queryUpdateTpHistory;

                DBTrack.trackQueries(this.sessionId, this.userName, queries);
                debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                if (DbException != null) throw DbException;
            }
        } else {
            debug("TpRestoreAction :: updateDb : unable to get queries ( queryDeleteFromTpPlant = " + queryUpdateTpPlant + " queryDeleteFromTpHistory = " + queryUpdateTpHistory);
        }
    }


    String getVobName(String linesetName) throws Exception {
        Vector v = new Vector();
        String query = "SELECT VOB_NAME " +
                "FROM LINESET" +
                " WHERE" +
                " LINESET_NAME='" + linesetName + "' AND " +
                " OWNER='" + xmlRdr.getVal(tpRec, "OWNER_LOGIN") + "' AND " +
                " INSTALLATION_ID='" + xmlRdr.getVal(tpRec, "FROM_PLANT") + "'";
        dbwrt.getRows(v, query);
        log.p("query getVobName()>" + query);

        return (String) v.elementAt(0);
    }

    String getLineSetName() throws Exception {
        Vector v = new Vector();
        String query = "SELECT LINESET_NAME " +
                "FROM TP_PLANT" +
                " WHERE" +
                " JOBNAME='" + xmlRdr.getVal(tpRec, "JOBNAME") + "' AND " +
                " JOB_RELEASE=" + xmlRdr.getVal(tpRec, "JOB_REL") + " AND " +
                " JOB_REVISION='" + xmlRdr.getVal(tpRec, "JOB_REV") + "' AND " +
                " TPMS_VER=" + xmlRdr.getVal(tpRec, "JOB_VER") + " AND " +
                " OWNER_LOGIN='" + xmlRdr.getVal(tpRec, "OWNER_LOGIN") + "' AND " +
                " FROM_PLANT='" + xmlRdr.getVal(tpRec, "FROM_PLANT") + "'";
        dbwrt.getRows(v, query);
        log.p("query getLineSetName()>" + query);

        return (String) v.elementAt(0);
    }

}

