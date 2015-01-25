/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 4, 2003
 * Time: 2:05:04 PM
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
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;


public class LsAction extends TpAction {
    protected String REQID;
    protected String repFileName;
    protected String action;

    //protected String userName = null;
    protected Properties params = null;
    protected Element lsRec = null;
    protected boolean hasDbActionBool = true;
    protected oneConnDbWrtr dbwrt = null;
    protected String localPlant = null;
    protected String webAppDir = null;
    protected String userRole = null;


    public LsAction(String action, LogWriter log) {
        super(action, log);
        this.action = action;
        this.log = log;
    }


    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAction() {
        return this.action;
    }

    public String getCCAction() {
        return this.action;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "lineset");
        out.println("action=" + this.getCCAction());
        out.println("unix_home=" + XmlUtils.getVal(lsRec, "HOME_DIR"));
        out.println("email=" + XmlUtils.getVal(lsRec, "OWNER_EMAIL"));
        out.println("in_dir=" + XmlUtils.getVal(lsRec, "SYNCRODIR"));
        out.println("linesetname=" + XmlUtils.getVal(lsRec, "LS_NAME"));
        out.println("tst_family=" + XmlUtils.getVal(lsRec, "TESTERFAM"));
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

  /*  public void setUserName(String user) {
        this.userName = user;
    }*/

    public void setLsRec(Element lsRec) {
        this.lsRec = lsRec;
    }

    public void setParams(Properties params) {
        this.params = params;
    }

    public void setDbWrtr(oneConnDbWrtr dbwrt) {
        this.dbwrt = dbwrt;
    }


    private String generateQuery(String vob) {
        String query = null;
        try {

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
                    XmlUtils.getVal(lsRec, "LS_OWNER") + "','" +
                    XmlUtils.getVal(lsRec, "LS_NAME") + "','" +
                    XmlUtils.getVal(lsRec, "TESTERFAM") + "','" +
                    XmlUtils.getVal(lsRec, "SYNCRODIR") + "'," +
                    "0," +
                    "SYSDATE," +
                    ("".equals(XmlUtils.getVal(lsRec, "NB_OF_FILES"))  ? "0" : XmlUtils.getVal(lsRec, "NB_OF_FILES")) + "," +
                    ("".equals(XmlUtils.getVal(lsRec, "TOTAL_MB_SIZE")) ? "0" : XmlUtils.getVal(lsRec, "TOTAL_MB_SIZE")) +
                    ")";
        } catch (Exception e) {
            debug("LsAction :: generateQuery : unable to generate query!!");
        }
        return query;
    }


    public void doDbTransaction(String query) throws Exception {
        debug("-START DB TRANSACTION-->INSERT LINESET ");


        debug("QUERY = " + query);

        //dbWriter.submit(query);
        //dbWriter.commit();
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            if (dbwrt != null) {
                this.dbwrt.rollback();
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
        debug("-END DB TRANSACTION-->INSERT LINESET ");
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
        String vob = (XmlUtils.getChild(lsRec, "VOB") != null ? XmlUtils.getVal(lsRec, "VOB") : lsRec.getAttribute("VOB"));
        String query = generateQuery(vob);
        if (!GeneralStringUtils.isEmptyString(query)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(query);
                } catch (Exception e) {
                    DbException = e;
                    commitBool = false;
                    if (dbwrt != null) {
                        try {
                            dbwrt.checkConn();
                        } catch (SQLException e1) {
                            debug("LsAction :: updateDb : unable to rollback statements!!");
                        }
                    }
                    Thread.sleep(2000);
                }
            }
            if (commitBool && (dbwrt != null)) {
                this.dbwrt.commit();
            } else {
                DBTrack.trackQuery(this.sessionId, this.userName, query);
                debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                if (DbException != null) throw DbException;
            }
        } else {
            debug("LsAction :: updateDb : unable to get queries!!");
        }
    }

    String getTpDBID() throws Exception {
        Vector v = new Vector();
        dbwrt.getRows(v,
                "SELECT DBID " +
                        "FROM   TP_PLANT " +
                        "WHERE  JOBNAME = '" + XmlUtils.getVal(lsRec, "JOBNAME") + "' " +
                        "AND    REVISION = " + XmlUtils.getVal(lsRec, "JOB_REL") + " " +
                        "AND    LPAD(USER_VER,2,'0') = " + "LPAD(" + "'" + XmlUtils.getVal(lsRec, "JOB_REV") + "'" + ",2,'0') " +
                        "AND    LPAD(VERSION_,2,'0') = " + "LPAD(" + "'" + XmlUtils.getVal(lsRec, "JOB_VER") + "'" + ",2,'0') ");

        if (v.size() == 0) throw new TpmsException("TP NOT FOUND", "DB UPDATE ABORTED");

        return (String) v.elementAt(0);
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

    public void setWebAppDir(String w) {
        this.webAppDir = w;
    }

    public String getWebAppDir() {
        return this.webAppDir;
    }

    public String getReqID() {
        return this.REQID;
    }

    public void setLocalPlant(String localPlant) {
        this.localPlant = localPlant;
    }

}
