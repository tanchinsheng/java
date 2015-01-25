/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 4, 2003
 * Time: 2:09:29 PM
 * To change this template use Options | File Templates.
 */
package tpms;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.users.TpmsUser;
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
import java.sql.SQLException;


public class LsAlignAction extends LsAction {

    public LsAlignAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "lineset");
        out.println("action=" + this.getCCAction());
        out.println("linesetname=" + XmlUtils.getVal(lsRec, "LS_NAME"));
        out.println("outfile=" + repFileName);
        String vob = (XmlUtils.getChild(lsRec, "VOB") != null ? XmlUtils.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + TpActionServlet._cc_debug);
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        out.flush();
        out.close();
        debugLog("CC IN FILE CREATED>" + fName);
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


    private String generateQuery() {
        String query = null;
        TpmsUser tpmsUser = getTpmsUser();
        try {
            String vob = (XmlUtils.getChild(lsRec, "VOB") != null ? XmlUtils.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
            Integer baseline = new Integer(XmlUtils.getVal(lsRec, "BASELINE1"));
            int newBaseline = baseline.intValue() + 1;
            if (getNumLineSet(vob) == 0) {
                query = "INSERT INTO LINESET " +
                        "( " +
                        " INSTALLATION_ID, " +
                        " VOB_NAME, " +
                        " OWNER, " +
                        " LINESET_NAME, " +
                        " TESTER_FAMILY, " +
                        " FILE_SYSTEM_DIR, " +
                        " BASELINE_LAST, " +
                        " DATE_LAST, " +
                        " NB_OF_FILES," +
                        " MBYTES" +
                        ") " +
                        "VALUES " +
                        "( '" +
                        localPlant + "','" +
                        vob + "','" +
                        tpmsUser.getName() + "','" +
                        XmlUtils.getVal(lsRec, "LS_NAME") + "','" +
                        XmlUtils.getVal(lsRec, "TESTERFAM") + "','" +
                        XmlUtils.getVal(lsRec, "SYNCRODIR") + "'," +
                        newBaseline + "," +
                        "SYSDATE," +
                        ("".equals(XmlUtils.getVal(lsRec, "NB_OF_FILES")) ? "0" : XmlUtils.getVal(lsRec, "NB_OF_FILES")) + "," +
                        ("".equals(XmlUtils.getVal(lsRec, "TOTAL_MB_SIZE")) ? "0" : XmlUtils.getVal(lsRec, "TOTAL_MB_SIZE")) +
                        ")";
            } else {
                query = "UPDATE LINESET SET " +
                        " BASELINE_LAST=" + newBaseline + "," +
                        " DATE_LAST= SYSDATE," +
                        " NB_OF_FILES=" + XmlUtils.getVal(lsRec, "NB_OF_FILES") + "," +
                        " MBYTES=" + XmlUtils.getVal(lsRec, "TOTAL_MB_SIZE") + "," +
                        " OWNER='" + tpmsUser.getName() + "'" +
                        " WHERE INSTALLATION_ID = '" +
                        localPlant + "' AND " +
                        " VOB_NAME = '" + vob + "' AND " +
                        " LINESET_NAME = '" +
                        XmlUtils.getVal(lsRec, "LS_NAME") + "'";
            }
        } catch (Exception e) {
            errorLog("LsAlignAction :: generateQuery : unable to generate query!!", e);
        }
        return query;
    }


    public void doDbTransaction(String query) throws Exception {
        debugLog("-START DB TRANSACTION-->UPDATE LINESET ");

        debugLog("QUERY = " + query);
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (SQLException e) {
            try {
                dbwrt.rollback();
            } catch (SQLException e1) {
                errorLog("LsAlignAction :: doDbTransaction : unable to rollback (query = " + query + ") " + e.getMessage(), e);
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }

    }

    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION:" + this.getAction().toUpperCase() + " ";
            s = s + "USER:" + this.userName + " ";
            s = s + "JOB:" + XmlUtils.getVal(lsRec, "TP_LABEL") + " ";
            s = s + "DATE:" + dateRd.getCurDateTime() + " ";
            if (e != null) {
                s = s + "ERRTYP:" + e.getClass().getName() + " ";
                s = s + "ERRMSG:" + (e.getMessage() != null ? e.getMessage() : "");
            }
        }
        catch (Exception exc) {
            return null;
        }
        return s;
    }

    public void updateDb() throws Exception {
        Exception DbException = null;
        boolean commitBool = false;
        String query = this.generateQuery();

        if (!GeneralStringUtils.isEmptyString(query)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(query);
                }
                catch (Exception e) {
                    DbException = e;
                    commitBool = false;
                    try {
                        dbwrt.checkConn();
                    } catch (SQLException e1) {
                        errorLog("LsAlignAction :: updateDb : unable to checkConn (query = " + query + ") " + e.getMessage(), e);
                    }
                    Thread.sleep(2000);
                }
            }
            if (commitBool) this.dbwrt.commit();
            else {
                DBTrack.trackQuery(this.REQID, this.userName, query);
                debugLog("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                if (DbException != null) throw DbException;

            }
        } else {
            debugLog("LsAlignAction :: updateDb : unable to get query");
        }
    }

    int getNumLineSet(String vob) {
        try {
            Vector v = new Vector();
            dbwrt.getRows(v,
                    "SELECT LINESET_NAME " +
                            "FROM   LINESET" +
                            " WHERE INSTALLATION_ID = '" +
                            localPlant + "' AND " +
                            " VOB_NAME = '" + vob + "' AND " +
                            " LINESET_NAME = '" +
                            XmlUtils.getVal(lsRec, "LS_NAME") + "'");


            return v.size();
        } catch (SQLException e) {
            errorLog("LsAlignAction :: getNumLineSet : SQLException " + e.getMessage(), e);
            return -1;
        }
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

