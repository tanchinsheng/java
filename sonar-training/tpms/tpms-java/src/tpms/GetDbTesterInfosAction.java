package tpms;

import org.w3c.dom.Element;
import tol.LogWriter;
import tol.dateRd;
import tol.oneConnDbWrtr;
import tol.xmlRdr;
import tpms.utils.TpmsConfiguration;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Puglisi
 * Date: May 17, 2004
 * Time: 3:19:44 PM
 * To change this template use Options | File Templates.
 */

public class GetDbTesterInfosAction {
    protected Element testerData = null;
    protected oneConnDbWrtr dbwrt = null;
    protected String localPlant = null;

    protected LogWriter log = null;

    private TpmsConfiguration tpmsConfiguration = null;

    public void debug(Object msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public void debug(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public GetDbTesterInfosAction(LogWriter log, Element testerData, String localPlant, oneConnDbWrtr dbwrt) {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.log = log;
        this.testerData = testerData;
        this.localPlant = localPlant;
        this.dbwrt = dbwrt;

    }


    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION: INSERT DB TESTER INFO ACTION";
            s = s + " NEW USER:" + xmlRdr.getVal(testerData, "NAME") + " ";
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


    public Vector getPlantList(String localPlant) throws Exception {
        Exception DbException = null;
        boolean commitBool = false;
        Vector v = new Vector();
        for (int i = 0; ((!commitBool) && (i < 3)); i++) {
            commitBool = true;
            try {
                String query = "SELECT DISTINCT INSTALLATION_ID " +
                        "FROM TESTER_INFO" +
                        " WHERE INSTALLATION_ID <> '" + localPlant + "'";
                dbwrt.getRows(v, query);
                debug("query get plant list>" + query);
            }
            catch (Exception e) {
                DbException = e;
                commitBool = false;
                dbwrt.checkConn();
                this.dbwrt.rollback();
                Thread.sleep(2000);
                return null;
            }
        }
        if (commitBool) this.dbwrt.commit();
        else {
            debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
        }
        return v;
    }

    public ResultSet getTesterInfos(String localPlant) throws Exception {
        Exception DbException = null;
        boolean commitBool = false;
        ResultSet v = null;
        for (int i = 0; ((!commitBool) && (i < 3)); i++) {
            commitBool = true;
            try {
                String query = "SELECT * " +
                        "FROM TESTER_INFO" +
                        " WHERE INSTALLATION_ID = '" + localPlant + "'";
                v = dbwrt.getRecordset(query);
                debug("query get testerinfos>" + query);
            }
            catch (Exception e) {
                DbException = e;
                commitBool = false;
                dbwrt.checkConn();
                this.dbwrt.rollback();
                Thread.sleep(2000);
            }
        }
        if (commitBool) this.dbwrt.commit();
        else {
            debug("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
        }
        return v;
    }

}

