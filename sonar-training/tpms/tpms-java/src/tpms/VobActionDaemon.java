package tpms;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import tol.LogWriter;
import tol.oneConnDbWrtr;
import tpms.utils.DBTrack;
import tpms.utils.TpmsConfiguration;
import tpms.utils.QueryUtils;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Vector;


public class VobActionDaemon extends AfsCommonStaticClass implements Runnable {
    String REQID;
    HttpSession session;
    TpAction tpAction;
    String actionTxt;
    LogWriter log;
    boolean debug;
    boolean dvlBool;
    String dvlUnixHost;
    String unixUser;
    String dvlCcInOutDir;
    Vector inFileNameVect;
    Vector outFileNameVect;
    long timeOut;
    Vector cmds;
    String webAppDir;
    String dvlWebAppDir;
    protected TpmsConfiguration tpmsConfiguration = null;

    String trackMsg;
    protected oneConnDbWrtr dbwrt = null;

    public VobActionDaemon(HttpSession session,
                           LogWriter log,
                           TpAction tpAction,
                           String REQID,
                           String actionTxt,
                           boolean debug,
                           Vector inFileNameVect,
                           Vector outFileNameVect,
                           long timeOut,
                           Vector cmds,
                           boolean dvlBool,
                           String dvlUnixHost,
                           String dvlCcInOutDir,
                           String unixUser,
                           String webAppDir,
                           String dvlWebAppDir) {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.session = session;
        this.log = log;
        this.tpAction = tpAction;
        this.REQID = REQID;
        this.actionTxt = actionTxt;
        this.debug = debug;
        this.dvlBool = dvlBool;
        this.dvlUnixHost = dvlUnixHost;
        this.dvlCcInOutDir = dvlCcInOutDir;
        this.inFileNameVect = inFileNameVect;
        this.outFileNameVect = outFileNameVect;
        this.timeOut = timeOut;
        this.cmds = cmds;
        this.unixUser = unixUser;
        this.webAppDir = webAppDir;
        this.dvlWebAppDir = dvlWebAppDir;
        debugLog("VobActionDaemon :: VobActionDaemon : action class name = " + tpAction.getClass().getName() + " request_id = " + REQID);
    }

    public VobActionDaemon(HttpSession session,
                           LogWriter log,
                           TpAction tpAction,
                           String REQID,
                           String actionTxt,
                           boolean debug,
                           String inFileName,
                           String outFileName,
                           long timeOut,
                           String cmd,
                           boolean dvlBool,
                           String dvlUnixHost,
                           String dvlCcInOutDir,
                           String unixUser,
                           String webAppDir,
                           String dvlWebAppDir) {

        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.session = session;
        this.log = log;
        this.tpAction = tpAction;
        this.REQID = REQID;
        this.actionTxt = actionTxt;
        this.debug = debug;
        this.dvlBool = dvlBool;
        this.dvlUnixHost = dvlUnixHost;
        this.dvlCcInOutDir = dvlCcInOutDir;
        this.inFileNameVect = new Vector();
        inFileNameVect.addElement(inFileName);
        this.outFileNameVect = new Vector();
        outFileNameVect.addElement(outFileName);
        this.timeOut = timeOut;
        this.cmds = new Vector();
        cmds.addElement(cmd);
        this.unixUser = unixUser;
        this.webAppDir = webAppDir;
        this.dvlWebAppDir = dvlWebAppDir;
        if (tpAction != null)
            debugLog("VobActionDaemon :: VobActionDaemon :  action class name = " + tpAction.getClass().getName() + " request_id = " + REQID);
        else
            debugLog("VobActionDaemon :: VobActionDaemon :  action class unable to determinate caller: request_id = " + REQID);
    }

    public void run() {
        boolean beResult = false;
        int i;
        dbwrt = CtrlServlet.dbWriter;
        //IF THE DB CONNECTIO IS NULL TRY TO RETRIEVE IT.
        if (dbwrt == null) {
            try {
                debugLog("VobActionDaemon :: run : db connection is null, trying to retrieve it");
                this.dbwrt = QueryUtils.getDbConnection();
                if (dbwrt == null) {
                    debugLog("VobActionDaemon :: run : no error while retrieve connection trial BUT the connection is still null");
                } else {
                    debugLog("VobActionDaemon :: run : db connection successfully retrieved");
                }
            } catch (Exception e) {
                errorLog("VobActionDaemon :: run : no way to get the connection, it is null!!! " + e.getMessage(), e);
            }
        } else {
            debugLog("VobActionDaemon :: run : db connection is NOT null");
        }

        for (i = 0; i < inFileNameVect.size(); i++) {
            //FOR EACH In file....
            debugLog("CMD-ACTION-START-" + i + "> cmd = " + cmds.elementAt(i));
            //execute the command...
            beResult = executeCmd((String) inFileNameVect.elementAt(i), (String) outFileNameVect.elementAt(i), (String) cmds.elementAt(i), dvlBool, dvlUnixHost, dvlCcInOutDir, unixUser);
            if (!debug) {
                try {
                    //if we are not in debug track the action to db
                    trackDb(trackMsg, session.getId(), unixUser);
                    debugLog("DB-TRACK-ACTIONS-END-" + REQID + ">");
                } catch (Throwable e) {
                    errorLog("Exception while tracking the action into db", e);
                }
            }

            if (!beResult) {
                //if the clearcase action has a negative result (we got errors....)
                errorLog("CMD-ACTION-END-" + i + ">");
                try {
                    if (tpAction != null && tpAction.hasDbAction()) {
                        //if the action has db actions....
                        debugLog("VobActionDaemon :: run : action reports KO Result --> updateDbKoResult() ...");
                        tpAction.updateDbKoResult();
                        debugLog("VobActionDaemon :: run : action reports KO Result --> updateDbKoResult() ended");
                    }
                } catch (Exception e) {
                    errorLog("VobActionDaemon :: run : " + e.getMessage(), e);
                }
                //stop();
                return;
            }
            debugLog("CMD-ACTION-END-" + i + ">");
        }
        if (tpAction != null) {
            debugLog("VobActionDaemon :: run : tpAction.hasDbAction() = " + tpAction.hasDbAction());
            if (tpAction.hasDbAction() && beResult) {
                //if the action has OK result and we have to perform operations to the database. 
                try {
                    debugLog("VobActionDaemon :: run : action reports OK Result --> updateDb() ...");
                    //aggiorna le info nel db relative al tracciamento degli utenti
                    tpAction.updateDb();
                    debugLog("VobActionDaemon :: run : action reports OK Result --> updateDb() ended");
                } catch (Throwable e) {
                    errorLog("VobActionDaemon :: run : error while tpAction.updateDb() " + e.getMessage());
                }
                debugLog("DB-ACTIONS-END-" + REQID + ">");
                debugLog("TP-ACTION-END-" + REQID + "> result = " + beResult);
            }
        }
        stop();
        if (!debug) {
            removeFileVector(inFileNameVect, outFileNameVect);
        }

    }

    public void removeFileVector(Vector inFileNameVect, Vector outFileNameVect) {
        try {
            for (int i = 0; i < inFileNameVect.size(); i++) {
                debugLog("VobActionDaemon :: deleting in and out files...");
                File fi = new File((String) inFileNameVect.elementAt(i));
                fi.delete();
                File fo = new File((String) outFileNameVect.elementAt(i));
                fo.delete();
            }

        } catch (Exception e) {
            errorLog("VobActionDaemon :: removeFileVector : exception " + e.getMessage(), e);
        }
    }

    public boolean executeCmd(String inFileName, String outFileName, String cmd, boolean dvlBool, String dvlUnixHost, String dvlCcInOutDir, String unixUser) {
        debugLog("VobActionDaemon :: executeCmd : debug = " + debug);
        if (!debug) {
            try {
                if (dvlBool) {
                    rcpInFile(inFileName, dvlUnixHost, dvlCcInOutDir, unixUser);
                }
                debugLog("CC COMMAND ->" + cmd);
                Runtime.getRuntime().exec(cmd);
            } catch (Exception e) {
                String action = actionTxt + " ABORTED";
                String msg = "CC COMMAND EXECUTION FAILURE";
                stop(new TpmsException(msg, action, e));
                errorLog("VobActionDaemon :: executeCmd : " + e.getMessage(), e);
                return false;
            }
        }
        if (debug) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                errorLog("VobActionDaemon :: executeCmd : exception while sleeping...", e);
            }
        }
        try {
            waitForBackEndOutFile(outFileName, timeOut);
        } catch (TpmsException e) {
            stop(e);
            return false;
        } catch (Exception e) {
            String action = actionTxt + " ESIT UNDEFINED";
            String msg = "CC1 OUTPUT FILE DETECTION FAILURE";
            stop(new TpmsException(msg, action, e));
            return false;
        }

        try {
            TpmsException ActionFailedExc = new TpmsException("RETCODE = 1", actionTxt + " FAILED");
            if (!backEndOutFileIsOk(outFileName, ActionFailedExc)) {
                stop(ActionFailedExc);
                return false;
            }
        } catch (TpmsException e) {
            stop(e);
            return false;
        } catch (Exception e) {
            String action = actionTxt + " ESIT UNDEFINED";
            String msg = "CC2 OUT FILE PARSING FAILURE";
            debugLog("Exception>" + e.getMessage());
            stop(new TpmsException(msg, action, e));
            return false;
        }
        debugLog("VOB-ACTION-END-" + REQID + ">");
        return true;

    }

    public void rcpInFile(String inFileName, String dvlUnixHost, String dvlCcInOutDir, String unixUser) {
        try {
            String inFileNameCmd = inFileName.replace('/', '\\');
            String inFileTemp = inFileNameCmd.substring(0, 3);
            if (inFileTemp.indexOf(":") != -1) {
                inFileNameCmd = inFileNameCmd.substring(2);

            }
            debugLog("inFileNameMod ->" + inFileNameCmd);
            String cmd = "rcp -a " + inFileNameCmd + " " + dvlUnixHost + "." + unixUser + ":" + dvlCcInOutDir;
            debugLog("RCP IN COMMAND ->" + cmd);
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "UNIX RCP IN COMMAND EXECUTION FAILURE";
            stop(new TpmsException(msg, action, e));
        }

    }

    public void rcpOutFile(String outFileName, String dvlUnixHost, String dvlCcInOutDir, String unixUser) {
        try {
            String fNameUnix;
            String fileName;
            int index = outFileName.lastIndexOf('/');
            fileName = outFileName.substring(index);
            fNameUnix = dvlUnixHost + "." + unixUser + ":" + dvlCcInOutDir + fileName;
            //debug("CC OUT FILE TO UNIX >"+fNameUnix);
            if (!outFileName.startsWith("/")) {
                int index2 = outFileName.lastIndexOf(":");
                outFileName = outFileName.substring(index2 + 1);
            }
            index = outFileName.lastIndexOf("/");
            String outFilePath = outFileName.substring(0, index);
            String cmd = "rcp -a " + fNameUnix + " " + outFilePath;
            //debug("RCP OUT COMMAND ->"+cmd);
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "UNIX RCP OUT COMMAND EXECUTION FAILURE";
            stop(new TpmsException(msg, action, e));
        }

    }

    void stop() {
        stop(null);
    }

    void stop(Exception e) {
        if (e != null) {
            session.setAttribute("exception" + "_" + REQID, e);
            errorLog("VOB-ACTION-FAILURE-" + REQID + ">" + (e.getMessage() != null ? e.getMessage() : ""), e);
        } else {
            if (session.getAttribute("exception" + "_" + REQID) != null) {
                session.removeAttribute("exception" + "_" + REQID);
            }
        }
        session.setAttribute("startBool" + "_" + REQID, Boolean.FALSE);

    }

    public void rcpRepFile(String dvlUnixHost, String unixUser) {
        try {
            String repFileName = dvlWebAppDir + "/" + "images" + "/" + REQID + "_rep.xml";
            debugLog("REP FILE NAME >" + repFileName);
            String fRepNameUnix = dvlUnixHost + "." + unixUser + ":" + repFileName;
            debugLog("CC REP FILE TO UNIX >" + fRepNameUnix);
            String repFilePath = webAppDir + "/images";
            if (!repFilePath.startsWith("/")) {
                int index2 = repFilePath.lastIndexOf(":");
                repFilePath = repFilePath.substring(index2 + 1);
            }
            String cmd = "rcp -a " + fRepNameUnix + " " + repFilePath;
            debugLog("RCP REPFILE COMMAND ->" + cmd);
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "UNIX RCP REPFILE COMMAND EXECUTION FAILURE";
            errorLog("VobActionDaemon :: rcpRepFile : " + e.getMessage(), e);
            stop(new TpmsException(msg, action, e));
        }

    }

    public void waitForBackEndOutFile(String fName, long timeOut) throws IOException, TpmsException, InterruptedException {
        File outFile = new File(fName);
        long myTimeOut = timeOut*2;
        debugLog("VobActionDaemon :: waitForBackEndOutFile : timeOut = " + timeOut);
        for (long i = 0; (i < myTimeOut) && (!outFile.exists()); i++) {
            /**
             * aspetto che scada il time-out per la risposta
             */
            //debugLog("VobActionDaemon :: waitForBackEndOutFile : wait for time-out expiration");
            if (dvlBool) {
                rcpOutFile(fName, dvlUnixHost, dvlCcInOutDir, unixUser);
            }
            Thread.sleep(500);
        }

        debugLog("VobActionDaemon :: waitForBackEndOutFile : fName = " + outFile.getCanonicalPath());

        if (outFile.exists()) {
            debugLog("CC OUT FILE RECEIVED>" + fName);
            if (dvlBool) {
                rcpRepFile(dvlUnixHost, unixUser);
            }
            debugLog("REPFILE RECEIVED>");
        } else {
            throw new TpmsException("TIMEOUT EXPIRED", actionTxt + " ABORTED");
        }
    }


    public boolean backEndOutFileIsOk(String fName, TpmsException ActionFailedExc) throws IOException, TpmsException {
        debugLog("file1 --> start definizione in nel Buffer");
        BufferedReader in = new BufferedReader(new FileReader(fName));
        debugLog("file1 --> end definizione in nel Buffer");
        String s;
        String userMsg = "";
        String sysMsg = "";
        boolean parseOkBool = false;
        boolean esitBool = false;
        while ((s = in.readLine()) != null) {
            debugLog("started read outFile");
            debugLog("riga letta--> " + s);
            if (s.startsWith("track=")) this.trackMsg = s.substring(6);
            for (int i = 0; i < 3; i++) {
                debugLog("ciclo-->" + i);
                if (s.equals("esit=0")) {
                    debugLog("esit = 0");
                    parseOkBool = true;
                    esitBool = true;
                    break;
                }
                if (s.equals("esit=1")) {
                    debugLog("esit = 1");
                    parseOkBool = true;
                    esitBool = false;
                }
                if (s.startsWith("usermsg=")) userMsg = s.substring(8);
                if (s.startsWith("sysmsg=")) sysMsg = s.substring(7);
            }
        }
        debugLog("ended read outFile");
        in.close();
        if (parseOkBool) {
            ActionFailedExc.setDetail(userMsg);
            ActionFailedExc.setSysDetail(sysMsg);
            return esitBool;
        } else {
            throw new TpmsException("CC3 OUT FILE PARSING FAILURE", actionTxt + " ESIT UNDEFINED");
        }
    }

    /**
     * ******************
     * /*  Metod use only for LineSet Action
     * *******************
     */
    public static void doAction(LogWriter log, boolean _DEBUG, boolean _dvlBool, String _dvlUnixHost, String _dvlCcInOutDir, String _dvlUnixWebAppDir, String _webAppDir, String _execMode, boolean backgrndBool, LsAction lsAction, String actionTxt, HttpSession session, String REQID, String repFileName, String scriptDir, String ccInOutDir, String unixUser, long timeOut) throws TpmsException, Exception {
        log.p("DoAction LS  STARTED->");
        log.p("REP FILE NAME->" + repFileName);

        String SID = session.getId();

        Vector inFileNameVect = new Vector();

        Vector outFileNameVect = new Vector();

        Vector cmds = new Vector();
        String inFileName = ccInOutDir + "/" + REQID + "_in";
        String outFileName = ccInOutDir + "/" + REQID + "_out";

        // dvl execution

        String inFileNameDvl = _dvlCcInOutDir + "/" + REQID + "_in";
        String outFileNameDvl = _dvlCcInOutDir + "/" + REQID + "_out";

        String shellScriptName = scriptDir + "/" + CtrlServlet._tpmsShellScriptName;


        try {
            lsAction.writeBackEndInFile(SID, inFileName, repFileName);
            log.p("CREATED WRITE BACK END IN FILE -" + inFileName);
        } catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "CC INPUT FILE CREATION FAILURE<!--ls action in file creation-->";
            throw new TpmsException(msg, action, e);
        }

        try {
            String cmd;
            if (_dvlBool) {
                /**
                 * caso con tomcat  e cc sono su macchine diverse (rcp, rsh ecc ecc....)
                 */
                cmd = VobActionDaemon.getUnixClearCaseCmdDvl(_execMode, unixUser, shellScriptName, inFileNameDvl, outFileNameDvl, _dvlUnixHost);
                log.p("UNIX-COMMAND-DVl" + REQID + ">" + cmd);
            } else {
                /**
                 * caso con tomcat e cc sono sulla stessa macchina
                 */
                cmd = VobActionDaemon.getUnixClearCaseCmd(_execMode, unixUser, shellScriptName, inFileName, outFileName);
                log.p("UNIX-COMMAND-" + REQID + ">" + cmd);
            }
            debugLog("VobActionDaemon :: doAction (ls) : cmd = " + cmd);
            inFileNameVect.addElement(inFileName);
            outFileNameVect.addElement(outFileName);

            cmds.addElement(cmd);

            VobActionDaemon daemon = new VobActionDaemon(session, log, lsAction, REQID, actionTxt, _DEBUG, inFileNameVect, outFileNameVect, timeOut, cmds, _dvlBool, _dvlUnixHost, _dvlCcInOutDir, unixUser, _webAppDir, _dvlUnixWebAppDir);
            if (backgrndBool) {
                Thread daemonThread = new Thread(daemon);
                daemonThread.start();
            } else {
                daemon.run();
            }
        } catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "CC COMMAND EXECUTION FAILURE";
            throw new TpmsException(msg, action, e);
        }
    }

    /**
     * ******************
     * /*  Metod use only for TP Action
     * /* - is different from action LS because for TP check if the VOB is 'R' and call cmd2
     * *******************
     */
    public static void doAction(LogWriter log, boolean backgrndBool, TpAction tpAction, String actionTxt, HttpSession session, String REQID, String repFileName, String unixUser, long timeOut) throws TpmsException, Exception {
        log.p("DoAction TP  STARTED->");
        log.p("REP FILE NAME->" + repFileName);
        TpmsConfiguration tpmsConfiguration = TpmsConfiguration.getInstance();
        boolean _DEBUG = tpmsConfiguration.getDebug();
        boolean _dvlBool = tpmsConfiguration.getDvlBool();
        String _dvlUnixHost = tpmsConfiguration.getDvlUnixHost();
        String _dvlCcInOutDir = tpmsConfiguration.getDvlCcInOutDir();
        String _dvlUnixWebAppDir = tpmsConfiguration.getDvlUnixWebAppDir();
        String _webAppDir = tpmsConfiguration.getWebAppDir();
        String _execMode = tpmsConfiguration.getUnixScriptExecMode();
        String scriptDir = tpmsConfiguration.getCcScriptsDir();
        String ccInOutDir = tpmsConfiguration.getCcInOutDir();
        String SID = session.getId();

        Vector inFileNameVect = new Vector();

        Vector outFileNameVect = new Vector();
        Vector cmds = new Vector();
        String inFileName = ccInOutDir + "/" + REQID + "_in";
        String outFileName = ccInOutDir + "/" + REQID + "_out";

        // dvl execution
        String inFileNameDvl = _dvlCcInOutDir + "/" + REQID + "_in";
        String outFileNameDvl = _dvlCcInOutDir + "/" + REQID + "_out";

        log.p("START GET VOB NAME");
        log.p("ACTION NAME=" + tpAction.action);
        // call cmd 2 only for VOB TYPE 'R' and only for action extract, mark, put production - get VobType
        String vobName = "";
        if (!tpAction.action.equals("login") && (!tpAction.action.equals("logout") && (!tpAction.action.equals("tp_restore")))) {
            vobName = (XmlUtils.getChild(tpAction.tpRec, "VOB") != null ? XmlUtils.getVal(tpAction.tpRec, "VOB") : tpAction.tpRec.getAttribute("VOB"));
        }

        log.p("GET VOB NAME ENDED->" + vobName);
        String vobType;
        if (!vobName.equals("")) {
            Element currentVob = VobManager.getVobData(vobName);
            vobType = XmlUtils.getVal(currentVob, "TYPE");
            log.p("VOB TYPE->" + vobType);
        }
        String shellScriptName = scriptDir + "/" + CtrlServlet._tpmsShellScriptName;


        try {

            tpAction.writeBackEndInFile(SID, inFileName, repFileName);
            log.p("CREATED WRITE BACK END IN FILE -" + inFileName);
        } catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "CC INPUT FILE CREATION FAILURE";
            throw new TpmsException(msg, action, e);
        }

        try {
            String cmd;
            if (_dvlBool) {
                cmd = VobActionDaemon.getUnixClearCaseCmdDvl(_execMode, unixUser, shellScriptName, inFileNameDvl, outFileNameDvl, _dvlUnixHost);
                log.p("UNIX-COMMAND-DVl" + REQID + ">" + cmd);
            } else {
                cmd = VobActionDaemon.getUnixClearCaseCmd(_execMode, unixUser, shellScriptName, inFileName, outFileName);
                log.p("UNIX-COMMAND-" + REQID + ">" + cmd);
            }
            inFileNameVect.addElement(inFileName);
            outFileNameVect.addElement(outFileName);

            cmds.addElement(cmd);
            VobActionDaemon daemon = new VobActionDaemon(session, log, tpAction, REQID, actionTxt, _DEBUG, inFileNameVect, outFileNameVect, timeOut, cmds, _dvlBool, _dvlUnixHost, _dvlCcInOutDir, unixUser, _webAppDir, _dvlUnixWebAppDir);
            if (backgrndBool) {
                Thread daemonThread = new Thread(daemon);
                daemonThread.start();
            } else {
                daemon.run();
            }
        } catch (Exception e) {
            String action = actionTxt + " ABORTED";
            String msg = "CC COMMAND EXECUTION FAILURE";
            throw new TpmsException(msg, action, e);
        }
    }

    public static String getUnixClearCaseCmd(String unixScriptExecMode, String unixUser, String shellScriptName, String inFileName, String outFileName) {
        String cmd = shellScriptName + " " + inFileName + " " + outFileName;
        String rshCmd = (unixScriptExecMode.equals("rsh") ? "rsh 127.0.0.1" : unixScriptExecMode) + " -n -l " + unixUser + " " + cmd;
        String suCmd = "su -l " + unixUser + " -c \"" + cmd + "\"";
        return (unixScriptExecMode.equals("su") ? suCmd : rshCmd);
    }

    public static String getUnixClearCaseCmdDvl(String unixScriptExecMode, String unixUser, String shellScriptName, String inFileName, String outFileName, String dvlUnixHost) {
        String cmd = shellScriptName + " " + inFileName + " " + outFileName;
        String rshCmd = (unixScriptExecMode.equals("rsh") ? "rsh " + dvlUnixHost : unixScriptExecMode) + " -n -l " + unixUser + " " + cmd;
        String suCmd = "su -l " + unixUser + " -c \"" + cmd + "\"";
        return (unixScriptExecMode.equals("su") ? suCmd : rshCmd);
    }


    public void trackDb(String trackMsg, String sessionId, String userId) throws Exception {


        if (!GeneralStringUtils.isEmptyString(trackMsg)) {
            debugLog("TRACK ACTION DB STARTED");
            Exception DbException = null;
            boolean commitBool = false;
            //FF 20/09/2005: changes track message separator from ',' to '##' to be more free in return messages content.
            final String trackMsgSeparator = "##";
            debugLog("start parse track file-->" + trackMsg);
            int index1 = 0;
            int index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            debugLog("index2 = " + index2);
            String esit = trackMsg.substring(index1, index2);
            debugLog("esit = " + esit);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String err_msg = trackMsg.substring(index1, index2);
            debugLog("err_msg = " + err_msg);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String intallation_id = trackMsg.substring(index1, index2);
            debugLog("inst_id = " + intallation_id);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String start_date = trackMsg.substring(index1, index2);
            debugLog("start_date = " + start_date);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String vob_name = trackMsg.substring(index1, index2);
            debugLog("vob_name = " + vob_name);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String actor = trackMsg.substring(index1, index2);
            debugLog("actor = " + actor);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String session_id = trackMsg.substring(index1, index2);
            debugLog("session = " + session_id);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String rec_type = trackMsg.substring(index1, index2);
            debugLog("rec_type = " + rec_type);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String action = trackMsg.substring(index1, index2);
            debugLog("action = " + action);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String entry_name = trackMsg.substring(index1, index2);
            debugLog("entry = " + entry_name);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String att_1 = trackMsg.substring(index1, index2);
            debugLog("att1 = " + att_1);
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf(trackMsgSeparator, index1);
            String att_2 = trackMsg.substring(index1, index2);
            debugLog("att2 = " + att_2);
            index1 = index2 + trackMsgSeparator.length();
            String end_date = trackMsg.substring(index1);
            debugLog("end_date = " + end_date);

            String query = "INSERT INTO TRACK_DETAILS " +
                    "( " +
                    " ESIT, " +
                    " ERR_MSG, " +
                    " INSTALLATION_ID, " +
                    " START_DATE, " +
                    " VOB_NAME, " +
                    " ACTOR, " +
                    " SESSION_ID, " +
                    " REC_TYPE," +
                    " ACTION," +
                    " ENTRY_NAME," +
                    " ATT_1," +
                    " ATT_2," +
                    " END_DATE" +
                    ") " +
                    "VALUES " +
                    "( '" +
                    QueryUtils.duplicateQuotes(esit) + "','" +
                    QueryUtils.duplicateQuotes(err_msg) + "','" +
                    QueryUtils.duplicateQuotes(intallation_id) + "'," +
                    "TO_DATE('" + start_date + "','DDMONYYYY HH24:MI:SS'),'" +
                    QueryUtils.duplicateQuotes(vob_name) + "','" +
                    QueryUtils.duplicateQuotes(actor) + "','" +
                    QueryUtils.duplicateQuotes(session_id) + "','" +
                    QueryUtils.duplicateQuotes(rec_type) + "','" +
                    QueryUtils.duplicateQuotes(action) + "','" +
                    QueryUtils.duplicateQuotes(entry_name) + "','" +
                    QueryUtils.duplicateQuotes(att_1) + "','" +
                    QueryUtils.duplicateQuotes(att_2) + "'," +
                    "TO_DATE('" + end_date + "','DDMONYYYY HH24:MI:SS'))";

            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    debugLog("i = " + i + "QUERY = " + query);
                    dbwrt.submit(query);
                    debugLog("VobActionDaemon :: trackDb : track query executed");
                    dbwrt.commit();
                    debugLog("VobActionDaemon :: trackDb : track query committed");
                } catch (Exception e) {
                    errorLog("VobActionDaemon :: trackDb : track query failed " + e.getMessage(), e);
                    DbException = e;
                    commitBool = false;
                    try {
                        dbwrt.checkConn();
                        this.dbwrt.rollback();
                    } catch (Exception e1) {
                        errorLog("VobActionDaemon :: trackDb : track query failed error while checkConn or rollback " + e.getMessage(), e);
                    }
                    Thread.sleep(2000);
                }
            }
            if (!commitBool) {
            	DBTrack.trackQuery(sessionId, userId, query);
            }
            /*
            if (commitBool) {
                try {
                    this.dbwrt.commit();
                    debugLog("In DB TRACK second commit ");
                } catch (Exception e) {
                    errorLog("VobActionDaemon :: trackDb : the query executed but there was an error during commit action " + e.getMessage(), e);
                }
            } else {
                DBTrack.trackQuery(sessionId, userId, query);
                if (DbException != null) throw DbException;
            }
            */
        } else {
            debugLog("VobActionDamon :: trackDb : trackMsg is null!");
        }
    }
}