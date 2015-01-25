package tpms;

import tol.LogWriter;
import tol.repSect;
import tpms.utils.TpmsConfiguration;

import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbActionDaemon implements Runnable {
    HttpSession session;
    String REQID;
    repSect repObj;
    LogWriter log;
    boolean debug;
    String repFileName;
    String actionTxt;
    long timeOut;
    String addWhrStr;
    TpmsConfiguration tpmsConfiguration = null;

    public void setLogWriter(LogWriter log) {
        this.log = log;
    }

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
        if (log != null) log.p(msg);
    }


    public DbActionDaemon(
            HttpSession session,
            String REQID,
            LogWriter log,
            repSect repObj,
            String actionTxt,
            String addWhrStr,
            boolean debug,
            String repFileName,
            long timeOut
    ) {

        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.session = session;
        this.REQID = REQID;
        this.log = log;
        this.repObj = repObj;
        this.actionTxt = actionTxt;
        this.debug = debug;
        this.repFileName = repFileName;
        this.timeOut = timeOut;
        this.addWhrStr = addWhrStr;
    }

    public void run() {
        try {
            FileWriter fout = new FileWriter(this.repFileName);
            repObj.fetch(true, fout, addWhrStr);
            fout.flush();
            fout.close();
        }
        catch (Exception e) {
            String action = actionTxt + " FAILURE";
            String msg = e.getClass().getName();
            stop(new TpmsException(msg, action, e));
            return;
        }
        stop();
    }

    void stop() {
        stop(null);
    }

    void stop(Exception e) {
        try {
            if (e != null) {
                session.setAttribute("exception" + "_" + REQID, e);
            } else {
                if (session.getAttribute("exception" + "_" + REQID) != null) {
                    session.removeAttribute("exception" + "_" + REQID);
                }
            }
            session.setAttribute("startBool" + "_" + REQID, new Boolean(false));
        }
        catch (Exception exc) {
        }
    }
}