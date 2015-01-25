package tpms;


import it.txt.afs.security.AfsSecurityManager;
import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.general.SessionObjects;
import it.txt.ldap.LdapAuthentication;
import it.txt.tpms.users.manager.TpmsUsersManager;
import it.txt.tpms.users.TpmsUser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tol.*;
import tpms.utils.StringMasking;
import tpms.utils.TpmsConfiguration;
import tpms.utils.UserUtils;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;


public class CtrlServlet extends AfsGeneralServlet {
    private static TpmsConfiguration tpmsConfiguration = TpmsConfiguration.getInstance();


    static String _webAppDir = tpmsConfiguration.getWebAppDir();
    static String _plant = tpmsConfiguration.getLocalPlant();
    static String _ccScriptsDir = tpmsConfiguration.getCcScriptsDir();
    static String _ccInOutDir = tpmsConfiguration.getCcInOutDir();
    // dvl execution
    static String _dvlCcInOutDir = tpmsConfiguration.getDvlCcInOutDir();
    static String _dvlBool = tpmsConfiguration.getDvlBoolAsString();
    static String _dvlUnixHost = tpmsConfiguration.getDvlUnixHost();
    static String _dvlUnixAppDir = tpmsConfiguration.getDvlUnixWebAppDir();

    static String _execMode = tpmsConfiguration.getUnixScriptExecMode();


    static String _AccntsFileName = _webAppDir + "/" + "cfg" + "/" + "local_cfg" + "/" + "accounts.xml";
    //static String _AccntsFileName = "D:\\jakarta-tomcat-3.2.3\\webapps\\tpms51.COOPERATIVE_DEVELOPMENT\\cfg\\local_cfg\\accounts.xml";
    static String _vobsFileName = _webAppDir + "/" + "cfg" + "/" + "local_cfg" + "/" + "vobs.xml";
    static String _testerInfosFileName = _webAppDir + "/" + "cfg" + "/" + "local_cfg" + "/" + "tester_info.xml";
    public static String _facilityFileName = _webAppDir + "/" + "cfg" + "/" + "local_cfg" + "/" + "facility.xml";

    static Element accntsRoot = null;
    // static dbRdrMgr dbmgr = null;
    static dbRdr dbr = null;
    Exception dbConnExc = null;
    public static oneConnDbWrtr dbWriter;
    static String _caughtErrPage = "/caughtErr.jsp";
    public static String _tpmsShellScriptName = "webmain_tpmsw.sh";
    static dirCleaner imgDirCleaner;

    LogWriter log = null;

    public static String getAccounntsFileName() {
        return _AccntsFileName;
    }

    public void init() throws ServletException {

        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();


        String logName = _webAppDir + "/" + "logs" + "/" + "tpms.log";
        try {
            this.log = new LogWriter(logName, true);
            this.getServletContext().setAttribute("log", log);
        } catch (IOException e) {
            System.out.println("CtrlServlet :: init : Unable to initialize log file (" + logName + ")");
            return;
        }

        try {
            imgDirCleaner = new dirCleaner(_webAppDir + "/" + "images", Integer.parseInt(getServletContext().getInitParameter("tempFileLifeTime")));
        } catch (Exception e) {
            errorLog("ERROR: IMG-DIR-CLEANER-STARTUP-ERROR>" + e, e);
        }

        try {
            String plantInitDir = _webAppDir + File.separator + "cfg" + File.separator + "local_cfg";
            String modelInitDir = _webAppDir + File.separator + "cfg" + File.separator + "db_int" + File.separator + "MAIN";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(plantInitDir + File.separator + "plants.xml"));
            Element dbInfoElement = (Element) doc.getDocumentElement().getElementsByTagName("PLANTS").item(0);
            String username = XmlUtils.getTextValue(dbInfoElement, "USERNAME");
            String pwd = XmlUtils.getTextValue(dbInfoElement, "PASSWORD");
            pwd = StringMasking.decriptString(pwd);
            String connectionString = XmlUtils.getTextValue(dbInfoElement, "CONN_STR");
        
            if (dbr == null)
            {	
            	dbr = new dbRdr(modelInitDir, TpmsConfiguration.getInstance().getNDbConns(), "ORACLE", connectionString, username, pwd);
        	}
            if (dbWriter == null)
            {
            	dbWriter = new oneConnDbWrtr(connectionString, username, pwd);
            }

            this.getServletContext().setAttribute("dbr", dbr);
            this.getServletContext().setAttribute("dbWriter", dbWriter);

        } catch (Exception e) {
            errorLog("CtrlServlet :: init : dataStrutsInit " + e.getMessage(), e);
            this.dbConnExc = e;
        }
        debugLog("DBMGR-STARTUP>OK");

        try {

            this.dataStrutsInit();

        } catch (Exception e) {
            errorLog("CtrlServlet :: init : dataStrutsInit " + e.getMessage(), e);
        }
    }

    public void dataStrutsInit() throws Exception {
        accntsInit();
        this.vobsInit();
        this.testerInfosInit();
        this.facilityInit();
    }

    public static void accntsInit()  {
        try {
            debugLog("CtrlServlet :: accntsInit : before _AccntsFileName = " + _AccntsFileName);
            if (GeneralStringUtils.isEmptyString(_AccntsFileName)) {
                getAccntsFileName();
            }
            debugLog("CtrlServlet :: accntsInit : after _AccntsFileName = " + _AccntsFileName);
            File f = new File(_AccntsFileName);
            accntsRoot = XmlUtils.getRoot(f);

        } catch (Exception e) {
            errorLog("ERROR: ACCONTS-XML-FILE-LOADING-ERROR>" + e.getMessage(), e);
        }
        debugLog("ACCOUNTS-XML-FILE-LOADING>OK");
    }

    public void vobsInit() {
        try {
            VobManager.setVobsRoot(XmlUtils.getRoot(_vobsFileName));
        } catch (Exception e) {
            debugLog("ERROR: VOBS-XML-FILE-LOADING-ERROR>" + e);
        }
        debugLog("VOBS-XML-FILE-LOADING>OK");
    }

    public void testerInfosInit() throws Exception {
        try {
            TesterInfoMgr.setTesterInfosRoot(XmlUtils.getRoot(_testerInfosFileName));
        } catch (Exception e) {
            debugLog("ERROR: TESTER-INFOS-XML-FILE-LOADING-ERROR>" + e);
        }
        debugLog("TESTER-INFOS-XML-FILE-LOADING>OK");
    }
    
    public void facilityInit() throws Exception {
        try {
            FacilityMgr.setFacilityRoot(XmlUtils.getRoot(_facilityFileName));
        } catch (Exception e) {
            debugLog("ERROR: FACILITY-XML-FILE-LOADING-ERROR>" + e);
        }
        debugLog("FACILITY-XML-FILE-LOADING>OK");
    }


    public synchronized static void accntsSave() throws TpmsException {
        try {
            FileWriter fout = new FileWriter(_AccntsFileName);
            XmlUtils.print(accntsRoot.getOwnerDocument(), fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            throw new TpmsException("ACCOUNTS XML FILE SAVE FAILURE", "", e);
        }
    }

    public void destroy() {
    	if (dbr != null)
    	{
    		try {
    			//dbmgr.close();
    			dbr.close();
    		} catch (Exception e) {
    			debugLog("ERROR: DB-CONN(dbr)-SHUTDOWN-ERROR>" + e);
    		}
    		debugLog("DB-CONN(dbr)-SHUTDOWN>OK");
    	}
        try {
            log.close();
        } catch (Exception e) {
        	debugLog("ERROR: DB-CONN(dbr)-CANNOT CLOSE LOG-ERROR>" + e); 
        	e.printStackTrace();
        }
    }
    
    public static String getActualKey(String user) throws TpmsException, IOException {
        Element el = getUserData(user);
        return XmlUtils.getVal(el, "PWD");
    }

    static String getAccntsFileName() {
        _AccntsFileName = TpmsConfiguration.getInstance().getWebAppDir() + "/" + "cfg" + "/" + "local_cfg" + "/" + "accounts.xml";
        debugLog(_AccntsFileName);
        return _AccntsFileName;
    }

    public static Element getUserData(String user) throws TpmsException, IOException {
        if (accntsRoot == null) {
            accntsInit();
            if (accntsRoot == null) {
                throw new TpmsException("UNABLE TO PARSE THE ACCOUNTS FILE "  + _AccntsFileName, "LOGIN FAILED", _AccntsFileName);
            }
        }
        NodeList nl = accntsRoot.getElementsByTagName("USER");
        String currentName;
        for (int i = 0; i < nl.getLength(); i++) {
            Element el = (Element) nl.item(i);
            currentName = XmlUtils.getVal(el, "NAME");
            if (!GeneralStringUtils.isEmptyString(currentName) && currentName.equals(user)) return el;
        }
        throw new TpmsException("USER " + user + " UNKNOWN", "LOGIN FAILED");
    }

    public static Element getAdminData() throws TpmsException, IOException {
        if (accntsRoot == null) {
            accntsInit();
            if (accntsRoot == null) {
                throw new TpmsException("UNABLE TO PARSE THE ACCOUNTS FILE " + _AccntsFileName, "GET ADMIN DATA FAILED", _AccntsFileName);
            }
        }
        NodeList nl = accntsRoot.getElementsByTagName("USER");
        for (int i = 0; i < nl.getLength(); i++) {
            Element el = (Element) nl.item(i);
            if (XmlUtils.getVal(el, "ROLE").equals("ADMIN")) return el;
        }
        throw new TpmsException("ADMIN UNKNOWN", "GET ADMIN DATA FAILED");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nextPage = getServletConfig().getInitParameter("nextPage");
        String action = getServletConfig().getInitParameter("action");
        boolean debug = Boolean.valueOf(getServletContext().getInitParameter("debug")).booleanValue();
        HttpSession session = request.getSession();
        try {
            if (action.equals("login")) {
            	String givenLogin = request.getParameter("user");
                String password = request.getParameter("pwd");
                //let's look for the login inside of accounts.xml in LDAP_LOGIN element
                boolean isUserLdapInfoPresentInLocalAccounts = UserUtils.existsUserByLdapLogin(givenLogin);
                boolean isUserAuthenticated;
                String user;
                if ( isUserLdapInfoPresentInLocalAccounts ) {
                    //if the given login present in LDAP_LOGIN of one local user try to authenticate the iser using ldap data.
                    isUserAuthenticated = LdapAuthentication.authenticateUser(givenLogin, password);
                    //lets retrieve the old login in order to make all the situation the same as
                    //before the ldap authentication.
                    user = UserUtils.getTpmsLoginFromLdapLogin(givenLogin);
                } else {
                    //otherwise try to authenticate the user using the local xml accounts file data
                    isUserAuthenticated = isUserAuthenticated(givenLogin, password);
                    user = givenLogin;
                }
                debugLog("LOGIN>" + user);
                if (isUserAuthenticated) {
                    session.setAttribute("user", user);
                    TpmsUser tpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin( user );
                    tpmsUser.setInstallationId( tpmsConfiguration.getLocalPlant() );
                    session.setAttribute( SessionObjects.TPMS_USER, tpmsUser );
                    Element userData = getUserData(user);
                    String role = XmlUtils.getVal(userData, "ROLE");
                    debugLog("User Role>" + role);
                    session.setAttribute("role", role);
                    debugLog("User Role from session>" + session.getAttribute("role"));
                    /* FP rev5 -aggiunto il parametro userDiv nella sessione*/
                    String division = XmlUtils.getVal(userData, "DIVISION");
                    debugLog("User DIvision>" + division);
                    session.setAttribute("division", division);

                    session.setAttribute("unixUser", (XmlUtils.getVal(userData, "UNIX_USER") != null ? XmlUtils.getVal(userData, "UNIX_USER") : ""));
                    String workMode = ChangeModeServlet.getDefaultMode(role, userData);
                    ChangeModeServlet.checkPermissions(userData, workMode);
                    session.setAttribute("workMode", workMode);
                    request.setAttribute("Authenticated", Boolean.TRUE);
                    session.setAttribute("Authenticated", Boolean.TRUE);
                    debugLog("INIT-USER-SESSION>START");
                    initUserSession(session);
                    debugLog("INIT-USER-SESSION>END");
                    TpmsConfiguration.getInstance().setSessionId(request.getSession().getId());
                    String unixUser = (String) session.getAttribute("unixUser");
                    TpmsConfiguration.getInstance().setUserName(unixUser);
                    //SessionSingleTon.getInstance().setUserName(unixUser);
                    //SessionSingleTon.getInstance().setSessionId(request.getSession().getId());
                    if (CtrlServlet.isUserAdmin(userData)) {
                        dataStrutsInit();
                    }
                    debugLog("CtrlServlet :: doPost : data structure initialized");
                } else {
                    request.setAttribute("Authenticated", Boolean.FALSE);
                    session.setAttribute("Authenticated", Boolean.FALSE);
                    throw new TpmsException("WRONG PASSWORD", "LOGIN FAILED");
                }
            } else if (action.equals("logout")) {
                destroyUserSession(session);
                session.invalidate();
            }
            if (!debug) {
                try {
                    imgDirCleaner.delFilesIfOld();
                } catch (Exception e) {
                    errorLog("CtrlServlet :: doPost : cleaning old files...");
                }
            }
            debugLog("redirect..... nextPage = " + nextPage);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);
        } catch (TpmsException e) {
            errorLog("CtrlServlet :: doPost " + e.getMessage(), e);
            request.setAttribute("Authenticated", Boolean.FALSE);
            session.setAttribute("Authenticated", Boolean.FALSE);
            request.setAttribute("errorBackButton", Boolean.FALSE);
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(_caughtErrPage);
            rDsp.forward(request, response);
        } catch (Exception e) {
            request.setAttribute("Authenticated", Boolean.FALSE);
            session.setAttribute("Authenticated", Boolean.FALSE);
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(_caughtErrPage);
            rDsp.forward(request, response);
        }
    }

    public static boolean isUserAuthenticated(String user, String pwd) throws TpmsException, IOException {
        String inputKey = getPwdHash(pwd);
        String actualKey = getActualKey(user);
        return inputKey.length() >= 6 && inputKey.equals(actualKey);
    }

    public static String getPwdHash(String pwd) {
        return getKey(pwd + "Agrate-TPA");
    }

    public static String getKey(String src) {
        int keyLen = 9;
        int groupLen = 3;
        StringBuffer sb = new StringBuffer();
        int m = src.length() / keyLen;
        int r = src.length() % keyLen;

        for (int j = 0; j < keyLen; j++) {
            int n = 0;
            for (int i = 0; i < m; i++) {
                n = n + (int) src.charAt(i * keyLen + j);
            }
            if (j < r) n = n + (int) src.charAt(m * keyLen + j);
            n = n % 13 + (n + j) % 3;
            n = (n < 10 ? n + ((int) '0') : n - 10 + ((int) 'A'));
            sb.append((char) n);
            if (((j + 1) % groupLen == 0) && (j < keyLen - 1)) sb.append('-');
        }
        return sb.toString();
    }

    void destroyUserSession(HttpSession session) throws TpmsException {
        debugLog("SESSION DEALLOCATION>");
        if (((Boolean) session.getAttribute("isVobAccessEnabled")).booleanValue()) {
            try {
                String REQID = session.getId() + "_" + Long.toString(System.currentTimeMillis());
                String unixUser = (String) session.getAttribute("unixUser");
                boolean _DEBUG = Boolean.valueOf(this.getServletContext().getInitParameter("debug")).booleanValue();
                boolean backgrndBool = false;
                String action = "logout";
                destroy();
                TpAction tpAction = new VobSessionDestroyAction(action, this.log);
                tpAction.setReqID(REQID);
                tpAction.setUserName(unixUser);
                VobActionDaemon.doAction(this.log, backgrndBool, tpAction, "VOB SESSION INITIALIZATION", session, (!_DEBUG ? REQID : action), "", unixUser, 9);
            } catch (Exception e) {
                debugLog("CC TEMP STRUCT DELETION FAILURE>");
            }
            debugLog("CC TEMP STRUCT DELETION>OK");
        }
    }

    Vector getPlantList(reportSel repsel) throws Exception {
        slctLst fromPlant = repsel.get("FROM_PLANT");
        slctLst toPlant = repsel.get("TO_PLANT");
        fromPlant.fetch();
        toPlant.fetch();
        Vector fromPlantLst = (Vector) fromPlant.getVector().clone();
        Vector toPlantLst = toPlant.getVector();
        for (int i = 0; i < toPlantLst.size(); i++) {
            boolean newBool = true;
            for (int j = 0; j < fromPlantLst.size(); j++) {
                if ((fromPlantLst.elementAt(j)).equals(toPlantLst.elementAt(i))) {
                    newBool = false;
                    break;
                }
            }
            if (newBool) fromPlantLst.addElement(toPlantLst.elementAt(i));
        }
        boolean localPlantFound = false;
        for (int i = 0; i < fromPlantLst.size(); i++) {
            if ((fromPlantLst.elementAt(i)).equals(_plant)) localPlantFound = true;
        }
        if (!localPlantFound) fromPlantLst.addElement(_plant);
        debugLog(fromPlantLst);
        return fromPlantLst;
    }

    void initUserSession(HttpSession session) throws TpmsException, IOException {
        debugLog("INIT-SESSION>");
        String user = (String) session.getAttribute("user");
        Element userData = getUserData(user);
        try {
            ChangeModeServlet.setDefaultVobForCurrentMode(session, (String) session.getAttribute("workMode"), userData);
        } catch (Exception e) {
            throw new TpmsException("VOB AUTOMATIC SELECTION FAILURE", "CRITICAL ERROR", e);
        }

        try {
            String role = (String) session.getAttribute("role");
            debugLog("SET-AV-QRYS>");
            ChangeModeServlet.setAvailableQueries(session, (String) session.getAttribute("workMode"), role);
            ChangeModeServlet.setAvailableOptions(session, (String) session.getAttribute("workMode"));
            debugLog("SET-AV-MODES>");
            ChangeModeServlet.setAvailableModes(session, role);
            debugLog("SET-FLAGS>");
            if ((((Boolean) session.getAttribute("isSendWorkModeEnabled")).booleanValue()) ||
                 (((Boolean) session.getAttribute("isRecWorkModeEnabled")).booleanValue()) ||
                 (((Boolean) session.getAttribute("isLocRepModeEnabled")).booleanValue())  ||
                 (((Boolean) session.getAttribute("isAidedFtpServiceEnabled")).booleanValue())) {
                session.setAttribute("isVobAccessEnabled", Boolean.TRUE);
                session.setAttribute("isWorkDirBrowseEnabled", Boolean.TRUE);
            } else {
                session.setAttribute("isVobAccessEnabled", Boolean.FALSE);
                session.setAttribute("isWorkDirBrowseEnabled", Boolean.FALSE);
            }
            debugLog("ROLE-PREMISSIONS-SETUP>OK");
        } catch (Exception e) {
            throw new TpmsException("ROLE PERMISSIONS SET UP FAILURE", "CRITICAL ERROR", e);
        }

        try {
            String repInitDir = _webAppDir + "/cfg/reports";
            reportSel repsel = new reportSel(repInitDir, dbr, log, "report.xml");
            session.setAttribute("repsel", repsel);

            //session.setAttribute("plantLst",getPlantList(repsel));

            debugLog("DB CONNECTION AND DB REPORTS-SETUP>OK");
        } catch (Exception e) {
            session.setAttribute("isGlobRepModeEnabled", Boolean.FALSE);
            if (dbr == null)
                throw new TpmsException("Kindly inform your admin and continue with your work", "DB CONNECTION UNAVAILABLE", this.dbConnExc);
            else
                throw new TpmsException("DB REPORTS WRONGLY SETUP", "DB ACCESS UNAVAILABLE", e);
        }

        if (((Boolean) session.getAttribute("isVobAccessEnabled")).booleanValue()) {
            try {
                String REQID = session.getId() + "_" + Long.toString(System.currentTimeMillis());
                String unixUser = (String) session.getAttribute("unixUser");
                boolean _DEBUG = Boolean.valueOf(this.getServletContext().getInitParameter("debug")).booleanValue();
                boolean _DVLBOOL = Boolean.valueOf(this.getServletContext().getInitParameter("dvlBool")).booleanValue();
                boolean backgrndBool = false;
                String action = "login";
                TpAction tpAction = new VobSessionInitAction(action, this.log);
                tpAction.setReqID(REQID);
                tpAction.setUserName(unixUser);
                debugLog("DVL_BOOL=" + _DVLBOOL);
                debugLog("_DEBUG=" + _DEBUG);
                debugLog("_dvlUnixHost=" + _dvlUnixHost);
                debugLog("_dvlCcInOutDir=" + _dvlCcInOutDir);
                debugLog("_dvlUnixAppDir=" + _dvlUnixAppDir);
                VobActionDaemon.doAction(this.log, backgrndBool, tpAction, "VOB SESSION INITIALIZATION", session, (!_DEBUG ? REQID : action), "", unixUser, 9);
            } catch (Exception e) {
                debugLog("CC TEMP STRUCT INITIALIZATION FAILURE>");

            }
            debugLog("CC TEMP STRUCT INITIALIZATION>OK");
        }

        try {
            String tOutStr = this.getServletContext().getInitParameter("sessionTimeOut");
            int tOut = (tOutStr != null ? Integer.parseInt(tOutStr) : 30);
            session.setMaxInactiveInterval(60 * tOut); //([time]=[sec])
            debugLog("TIME-OUT-SETUP>OK " + new Integer(tOut) + " [min]");
        } catch (Exception e) {
            throw new TpmsException("TIME OUT SETUP FAILURE", "CRITICAL ERROR", e);
        }
    }


    public static boolean isUserAdmin(String user) throws Exception {
        return isUserAdmin(getUserData(user));
    }

    public static boolean isUserAdmin(Element userData) throws Exception {
        NodeList roles = userData.getElementsByTagName("ROLE");
        for (int i = 0; i < roles.getLength(); i++) {
            if (XmlUtils.getVal((Element) roles.item(i)).equals("ADMIN")) return true;
        }
        return false;
    }

    public static Element getAccntsRoot() {
        return accntsRoot;
    }

    public static void setAttributes(HttpServletRequest request) {
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String parnam = (String) e.nextElement();
            String parval = request.getParameter(parnam);
            if (request.getAttribute(parnam) == null) {
                request.setAttribute(parnam, parval);
            }
        }
    }

    public static String getRoleDesc(String role) {
        if (role.equals("ADMIN")) return "Admin";
        if (role.equals("ENGINEER")) return "Eng";
        if (role.equals("CONTROLLER")) return "Control";
        if (role.equals("QUERY_USER")) return "Query";
        if (role.equals(AfsSecurityManager.AIDED_FTP_SERVICE_ROLE)) return "Aided ftp";
        return "";
    }
}