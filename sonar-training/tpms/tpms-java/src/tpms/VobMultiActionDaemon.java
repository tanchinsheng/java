package tpms;

import org.w3c.dom.Element;
import tol.LogWriter;
import tol.xmlRdr;
import tpms.utils.TpmsConfiguration;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 30, 2003
 * Time: 2:41:24 PM
 * To change this template use Options | File Templates.
 */

public class VobMultiActionDaemon implements Runnable {
    LogWriter log;
    String REQID;
    HttpSession session;
    String actionTxt;
    Vector tpActions;
    Vector inFileNames;
    Vector outFileNames;
    boolean debug;
    long timeOut;
    Vector cmds;

    boolean dvlBool;
    String dvlUnixHost;
    String unixUser;
    String dvlCcInOutDir;
    String webAppDir;
    String dvlWebAppDir;

    protected TpmsConfiguration tpmsConfiguration = null;

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
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public VobMultiActionDaemon(
            HttpSession session,
            LogWriter log,
            Vector tpActions,
            String REQID,
            String actionTxt,
            boolean debug,
            Vector inFileNames,
            Vector outFileNames,
            long timeOut,
            Vector cmds,
            boolean dvlBool,
            String dvlUnixHost,
            String dvlCcInOutDir,
            String unixUser,
            String webAppDir,
            String dvlWebAppDir
    ) {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.session = session;
        this.log = log;
        this.tpActions = tpActions;
        this.REQID = REQID;
        this.actionTxt = actionTxt;
        this.debug = debug;
        this.timeOut = timeOut;
        this.inFileNames = inFileNames;
        this.outFileNames = outFileNames;
        this.cmds = cmds;
        this.dvlBool = dvlBool;
        this.dvlUnixHost = dvlUnixHost;
        this.dvlCcInOutDir = dvlCcInOutDir;
        this.unixUser = unixUser;
        this.webAppDir = webAppDir;
        this.dvlWebAppDir = dvlWebAppDir;
    }

    public void run() {
        Exception exc = null;
        for (int i = 0; i < tpActions.size(); i++) {
            TpAction tpAction = (TpAction) tpActions.elementAt(i);
            Element tpRec = tpAction.getTpRec();
            try {
                VobActionDaemon vobDaemon = new VobActionDaemon(session,
                        log,
                        tpAction,
                        tpAction.getReqID(),
                        actionTxt,
                        debug,
                        (String) inFileNames.elementAt(i),
                        (String) outFileNames.elementAt(i),
                        timeOut,
                        (String) cmds.elementAt(i),
                        dvlBool,
                        dvlUnixHost,
                        dvlCcInOutDir,
                        unixUser,
                        webAppDir,
                        dvlWebAppDir
                );
                vobDaemon.run();
                Exception e = null;
                //the error in a single Tpaction add a element ERROR=Y with messages
                if ((e = (Exception) session.getAttribute("exception" + "_" + tpAction.getReqID())) != null) {
                    if (exc == null) exc = e;
                    xmlRdr.addEl(tpRec, "ERROR", "Y");
                    if (exc instanceof TpmsException) {
                        TpmsException texc = (TpmsException) e;
                        Element el = xmlRdr.addEl(tpRec, "ERR_ACTION_TXT", texc.getAction());
                        el.setAttribute("urlEncoded", java.net.URLEncoder.encode(xmlRdr.getVal(el), "UTF-8"));
                        el = xmlRdr.addEl(tpRec, "ERR_MSG_TXT", (texc.getMessage() != null ? texc.getMessage() : ""));
                        el.setAttribute("urlEncoded", java.net.URLEncoder.encode(xmlRdr.getVal(el), "UTF-8"));
                        el = xmlRdr.addEl(tpRec, "ERR_DETAIL_TXT", (texc.getDetail() != null ? texc.getDetail() : ""));
                        el.setAttribute("urlEncoded", java.net.URLEncoder.encode(xmlRdr.getVal(el), "UTF-8"));
                        el = xmlRdr.addEl(tpRec, "ERR_SYS_DETAIL_TXT", (texc.getSysDetail() != null ? texc.getSysDetail() : ""));
                        el.setAttribute("urlEncoded", java.net.URLEncoder.encode(xmlRdr.getVal(el), "UTF-8"));
                        xmlRdr.print(tpRec.getOwnerDocument(), System.out);
                    }
                } else //no error-->add a element ERROR=N
                {
                    xmlRdr.addEl(tpRec, "ERROR", "N");
                }
            }
            catch (Exception e) {
                exc = e;
            }
        }
        stop(exc);
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

    public static void doAction(LogWriter log, boolean _DEBUG, boolean dvlBool, String dvlUnixHost, String dvlCcInOutDir, String dvlUnixWebAppDir, String webAppDir, String _execMode, boolean backgrndBool, Vector tpActions, String actionTxt, HttpSession session, String GLOBAL_REQID, String repFileName, String scriptDir, String ccInOutDir, String unixUser, long timeOut) throws TpmsException, Exception {
        String SID = session.getId();
        Vector ccInFiles = new Vector();
        Vector ccOutFiles = new Vector();
        Vector cmds = new Vector();
        //for each tpAction in vector toActions create a vector with cmd clear case
        for (int i = 0; i < tpActions.size(); i++) {
            TpAction tpAction = (TpAction) tpActions.elementAt(i);
            String REQID = tpAction.getReqID();
            String inFileName = ccInOutDir + "/" + REQID + "_in";
            String outFileName = ccInOutDir + "/" + REQID + "_out";
            ccInFiles.addElement(inFileName);
            ccOutFiles.addElement(outFileName);
            String shellScriptName = scriptDir + "/" + CtrlServlet._tpmsShellScriptName;
            log.p("OUT-FILE-NAME-" + REQID + ">" + outFileName);
            try {
                tpAction.writeBackEndInFile(SID, inFileName, repFileName);
            }
            catch (Exception e) {
                String action = actionTxt + " ABORTED";
                String msg = "CC INPUT FILE CREATION FAILURE";
                throw new TpmsException(msg, action, e);
            }
            String cmd = VobActionDaemon.getUnixClearCaseCmd(_execMode, unixUser, shellScriptName, inFileName, outFileName);
            cmds.addElement(cmd);
            log.p("UNIX-COMMAND-" + REQID + ">" + cmd);
        }
        //create istance VobMultiActionDaemon with vector cmds and vector tpActions and vector ccInFiles  and vector ccOutFiles
        try {
            VobMultiActionDaemon daemon = new VobMultiActionDaemon(session, log, tpActions, GLOBAL_REQID, actionTxt, _DEBUG, ccInFiles, ccOutFiles, timeOut, cmds, dvlBool, dvlUnixHost, dvlCcInOutDir, unixUser, webAppDir, dvlUnixWebAppDir);
            if (backgrndBool) {
                //run VobMultiActionDaemon
                Thread daemonThread = new Thread(daemon);
                daemonThread.start();
            } else {
                daemon.run();
            }
        }
        catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "CC COMMAND EXECUTION FAILURE<!--Vob Multi Action daemon-->";
            throw new TpmsException(msg, action, e);
        }

    }

}
