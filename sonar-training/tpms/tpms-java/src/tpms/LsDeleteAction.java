/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 16, 2003
 * Time: 11:38:07 AM
 * To change this template use Options | File Templates.
 */

/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 15, 2003
 * Time: 11:11:23 AM
 * To change this template use Options | File Templates.
 */
package tpms;


import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import tol.LogWriter;
import tol.dateRd;
import tol.oneConnDbWrtr;
import tpms.utils.DBTrack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

public class LsDeleteAction extends LsAction {

    public LsDeleteAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "lineset");
        out.println("action=" + this.getCCAction());
        out.println("linesetname=" + XmlUtils.getVal(lsRec, "LS_NAME"));
        out.println("outfile=" + repFileName);
        String vob = ( XmlUtils.getChild(lsRec, "VOB") != null ? XmlUtils.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("trans_vob_list=" + VobManager.getTvobsListAsString(vob));
        out.println("session_id=" + SID);
        out.println("debug=" + TpActionServlet._cc_debug);
        //FF 15/09/2005
        out.println("user_role=" + this.getUserRole());


        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }


        out.flush();
        out.close();
        debug("CC IN FILE CREATED>" + fName);
    }

    public static String bindQry(String qryStr, Properties params) throws TpmsParamException {
        String qryStr2 = "";
        StringTokenizer tok = new StringTokenizer(qryStr, "$#<>=!^*+-/()' \n\t\r\f", true);
        while (tok.hasMoreTokens()) {
            String s = tok.nextToken();
            if (s.equals("$")) {
                String parName = tok.nextToken();
                s = params.getProperty(parName);
                if (s == null) throw new TpmsParamException("ACTION QUERY BINDER CAN'T BIND THE '" + parName + "' PARAMETER", parName, TpmsParamException._MISSING);
            }
            qryStr2 = qryStr2.concat(s);
        }
        return qryStr2;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public void setLsRec(Element lsRec) {
        this.lsRec = lsRec;
    }

    public void setParams(Properties params) {
        this.params = params;
    }

    public void setDbWrtr(oneConnDbWrtr dbwrt) {
        this.dbwrt = dbwrt;
    }

    private String generateQueryDeleteFromLineset(String vob) {
        String query = "";
        try {
            query = "DELETE FROM LINESET " +
                    "WHERE INSTALLATION_ID = '" +
                    localPlant + "' AND " +
                    " VOB_NAME = '" + vob + "' AND " +
                    " LINESET_NAME = '" +
                    XmlUtils.getVal(lsRec, "LS_NAME") + "'";
        } catch (Exception e) {
            debug("LsDeleteAction :: generateQueryDeleteFromLineset : unable to generate query!!");
        }
        return query;
    }

    public String generateQueryDeleteFromTpHistory() {
        String query = "";
        try {
            query = "DELETE FROM TP_HISTORY " +
                    " WHERE " +
                    " INSTALLATION_ID = '" +
                    localPlant + "' AND " +
                    " LINESET_NAME = '" +
                    XmlUtils.getVal(lsRec, "LS_NAME") + "'";
        } catch (Exception e) {
            debug("LsDeleteAction :: generateQueryDeleteFromTpHistory : unable to generate query!!");
        }
        return query;
    }

    public String generateQueryDeleteFromTpPlant() {
        String query = "";
        try {
            query = "DELETE FROM TP_PLANT " +
                    " WHERE " +
                    " FROM_PLANT = '" +
                    localPlant + "' AND " +
                    " LINESET_NAME = '" +
                    XmlUtils.getVal(lsRec, "LS_NAME") + "'";
        } catch (Exception e) {
            debug("LsDeleteAction :: generateQueryDeleteFromTpPlant : unable to generate query!!");
        }
        return query;
    }

    private String generateQueryDeleteFromTp_Test_Delivery() {
        String query = "";
        try {
            query = "DELETE FROM TP_TEST_DELIVERY " +
            " WHERE " +
            " FROM_PLANT = '" +
            localPlant + "' AND " +
            " LINESET_NAME = '" +
            XmlUtils.getVal(lsRec, "LS_NAME") + "'";
        } catch (Exception e) {
            debug("LsDeleteAction :: generateQueryDeleteFromTp_Test_Delivery : unable to generate query!!");
        }
        return query;
    }
    private String generateQueryDeleteFromAction_Comments() {
        String query = "";
        try {
            query = "DELETE FROM ACTION_COMMENTS " +
            " WHERE " +
            " FROM_PLANT = '" +
            localPlant + "' AND " +
            " LINESET_NAME = '" +
            XmlUtils.getVal(lsRec, "LS_NAME") + "'";
        } catch (Exception e) {
            debug("LsDeleteAction :: generateQueryDeleteFromAction_Comments : unable to generate query!!");
        }
        return query;
    }
    public void doDbTransaction(String queryDeleteFromTpPlant, String queryDeleteFromTpHistory, String queryDeleteFromLineset, String queryDeleteFromTp_Test_Delivery, String queryDeleteFromAction_Comments, String vob) throws Exception {
        debug("-START DB TRANSACTION-->DELETE LINESET ");
        if (getNumLineSet(vob) > 0) {
            try {
                dbwrt.submit(queryDeleteFromTpPlant);
                dbwrt.submit(queryDeleteFromTpHistory);
                dbwrt.submit(queryDeleteFromLineset);
                dbwrt.submit(queryDeleteFromTp_Test_Delivery);
                dbwrt.submit(queryDeleteFromAction_Comments);
                dbwrt.commit();
                debug("LINESET DELETE- Delete Lineset completed");
            } catch (Exception e) {
                try {
                    dbwrt.rollback();
                } catch (Exception e1) {
                }
                String action = "DB UPDATE";
                String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
                throw new TpmsException(msg, action, e);
            }
        } else {
            debug("-NOT DB TRANSACTION-->LINESET NOT FOUND");
        }
    }

    public String getCcActionUnixLogin(String userLogin, String adminLogin) {
        return adminLogin;
    }

    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION:" + this.getAction().toUpperCase() + " ";
            s = s + "USER:" + this.userName + " ";
            s = s + "LS_NAME:" + XmlUtils.getVal(lsRec, "LS_NAME") + " ";
            s = s + "DATE:" + dateRd.getCurDateTime() + " ";
            if (e != null) {
                s = s + "ERRTYP:" + e.getClass().getName() + " ";
                s = s + "ERRMSG:" + (e.getMessage() != null ? e.getMessage() : "");
            }
        } catch (Exception exc) {
            return null;
        }
        return s;
    }

    public void updateDb() throws Exception {
        Exception DbException = null;
        boolean commitBool = false;
        String vob = null;
        vob = (XmlUtils.getChild(lsRec, "VOB") != null ? XmlUtils.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
        if (!GeneralStringUtils.isEmptyString(vob)) {
            String queryDeleteFromLineset = this.generateQueryDeleteFromLineset(vob);
            String queryDeleteFromTpHistory = this.generateQueryDeleteFromTpHistory();
            String queryDeleteFromTpPlant = this.generateQueryDeleteFromTpPlant();
            String queryDeleteFromTp_Test_Delivery = this.generateQueryDeleteFromTp_Test_Delivery();
            String queryDeleteFromAction_Comments = this.generateQueryDeleteFromAction_Comments();

            if (!GeneralStringUtils.isEmptyString(queryDeleteFromTpPlant) &&
                    !GeneralStringUtils.isEmptyString(queryDeleteFromTpHistory) &&
                    !GeneralStringUtils.isEmptyString(queryDeleteFromLineset) && 
                    !GeneralStringUtils.isEmptyString(queryDeleteFromTp_Test_Delivery)&& 
                    !GeneralStringUtils.isEmptyString(queryDeleteFromAction_Comments)) {
                for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                    commitBool = true;
                    try {
                        this.doDbTransaction(queryDeleteFromTpPlant, queryDeleteFromTpHistory, queryDeleteFromLineset, queryDeleteFromTp_Test_Delivery, queryDeleteFromAction_Comments,  vob);
                    } catch (Exception e) {
                        DbException = e;
                        commitBool = false;
                        try {
                            dbwrt.checkConn();
                        } catch (Exception e1) {
                        }

                        Thread.sleep(2000);
                    }
                }
                if (!commitBool) {
                    String [] queryList = new String[5];
                    queryList[0] = queryDeleteFromTpPlant;
                    queryList[1] = queryDeleteFromTpHistory;
                    queryList[2] = queryDeleteFromLineset;
                    queryList[3] = queryDeleteFromTp_Test_Delivery;
                    queryList[4] = queryDeleteFromAction_Comments;
                    DBTrack.trackQueries(sessionId, this.userName, queryList);
                    debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                    if (DbException != null) throw DbException;
                }
            } else {
                debug("LsDeleteAction :: updateDb : unable to get queries!! ( " + queryDeleteFromTpPlant + " - " + queryDeleteFromTpHistory + " - " + queryDeleteFromLineset +  " - " + queryDeleteFromTp_Test_Delivery + " )");
            }
        } else {
            debug("LsDeleteAction :: updateDb : unable to get current vob!!");
        }
    }

    int getNumLineSet(String vob) throws Exception {

        debug("LINESET DELETE- start GetNumLineSet()");
        Vector v = new Vector();
        String query = "SELECT LINESET_NAME " +
                "FROM   LINESET" +
                " WHERE INSTALLATION_ID = '" +
                localPlant + "' AND " +
                " VOB_NAME = '" + vob + "' AND " +
                " LINESET_NAME = '" +
                XmlUtils.getVal(lsRec, "LS_NAME") + "'";
        dbwrt.getRows(v, query);
        debug("LINESET DELETE- end GetNumLineSet()");
        return v.size();

    }

    public boolean hasDbAction() {
        return hasDbActionBool;
    }

    public void setReqID(String reqID) {
        this.REQID = reqID;
    }

    public void setRepFileName(String s) {
        this.repFileName = s;
    }

    public String getReqID() {
        return this.REQID;
    }

}