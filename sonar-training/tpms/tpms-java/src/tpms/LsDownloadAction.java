/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 26, 2003
 * Time: 2:48:46 PM
 * To change this template use Options | File Templates.
 */

package tpms;

import org.w3c.dom.Element;
import tol.LogWriter;
import tol.dateRd;
import tol.oneConnDbWrtr;
import tol.xmlRdr;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

public class LsDownloadAction extends LsAction {

    public LsDownloadAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "lineset");
        out.println("action=" + this.getCCAction());
        out.println("linesetname=" + xmlRdr.getVal(lsRec, "LS_NAME"));
        out.println("baseline=" + xmlRdr.getVal(lsRec, "BASELINE"));
        out.println("outfile=" + repFileName);
        String vob = (xmlRdr.getChild(lsRec, "VOB") != null ? xmlRdr.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + TpActionServlet._cc_debug);
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

    public void doDbTransaction() throws Exception {
    }

    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION:" + this.getAction().toUpperCase() + " ";
            s = s + "USER:" + this.userName + " ";
            s = s + "JOB:" + xmlRdr.getVal(lsRec, "TP_LABEL") + " ";
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
        for (int i = 0; ((!commitBool) && (i < 3)); i++) {
            commitBool = true;
            try {
                this.doDbTransaction();
            }
            catch (Exception e) {
                DbException = e;
                commitBool = false;
                dbwrt.checkConn();
                this.dbwrt.rollback();
                Thread.currentThread().sleep(2000);
            }
        }
        if (commitBool) this.dbwrt.commit();
        else {
            debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
            if (DbException != null) throw DbException;

        }
    }

    String getTpDBID() throws Exception {
        Vector v = new Vector();
        dbwrt.getRows(v,
                "SELECT DBID " +
                        "FROM   TP_PLANT " +
                        "WHERE  JOBNAME = '" + xmlRdr.getVal(lsRec, "JOBNAME") + "' " +
                        "AND    REVISION = " + xmlRdr.getVal(lsRec, "JOB_REL") + " " +
                        "AND    LPAD(USER_VER,2,'0') = " + "LPAD(" + "'" + xmlRdr.getVal(lsRec, "JOB_REV") + "'" + ",2,'0') " +
                        "AND    LPAD(VERSION_,2,'0') = " + "LPAD(" + "'" + xmlRdr.getVal(lsRec, "JOB_VER") + "'" + ",2,'0') "
        );

        if (v.size() == 0) throw new TpmsException("TP NOT FOUND", "DB UPDATE ABORTED");
        String DBID = (String) v.elementAt(0);
        return DBID;
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


