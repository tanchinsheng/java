package tpms;

import tpms.utils.EMailTrack;
import tpms.utils.MailUtils;
import tpms.utils.TpmsConfiguration;

import java.io.IOException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;
import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.XmlUtils;
import it.txt.general.SessionObjects;
import it.txt.tpms.users.TpmsUser;
import it.txt.tpms.tp.utils.TpUtils;
import tol.LogWriter;
import tol.oneConnDbWrtr;
 
public class TpActionServlet extends AfsGeneralServlet {
    boolean _DEBUG;
    boolean backgrndBool = true;
    String _webAppDir;
    String _ccScriptsDir;
    String _ccInOutDir;
    public static String _cc_debug;
    String _execMode;
    long _timeOut; //[sec]
    String _timeOutStr;
    String _localPlant;

    // dvl execution
    static String _dvlCcInOutDir;
    static boolean _dvlBool;
    static String _dvlUnixHost;
    static String _dvlUnixAppDir;

    LogWriter log = null;
    protected TpmsConfiguration tpmsConfiguration = null;

    public static String TP_REJECT_ACTION_VALUE = "tp_reject";
    public static String TP_UPDATE_STEPS_ACTION_VALUE = "tp_update_steps";

    public void init() throws ServletException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.log = (LogWriter) this.getServletContext().getAttribute("log");
        _DEBUG = tpmsConfiguration.getDebug();
        _webAppDir = tpmsConfiguration.getWebAppDir();
        _ccScriptsDir = tpmsConfiguration.getCcScriptsDir();
        _ccInOutDir = tpmsConfiguration.getCcInOutDir();
        _cc_debug = (tpmsConfiguration.getCCDebug().equalsIgnoreCase("true") ? "1" : "0");
        _execMode = tpmsConfiguration.getUnixScriptExecMode();
        _timeOutStr = getServletConfig().getInitParameter("timeOut");
        _timeOut = Long.parseLong(_timeOutStr);
        _localPlant = tpmsConfiguration.getLocalPlant();

        /* DVL EXECUTION  (Java Windows  - CC Unix)
        *  _dvlCcInOutDir --> Unix dir with CCInOut FILE
        *  _dvlBool       --> (true) dvl execution
        *  _dvlUnixHost   --> Unix host of machine for rsh
        *  _dvlUnixAppDir --> Unix Web App Dir
        */
        _dvlCcInOutDir = tpmsConfiguration.getDvlCcInOutDir();
        _dvlBool = tpmsConfiguration.getDvlBool();
        _dvlUnixHost = tpmsConfiguration.getDvlUnixHost();
        _dvlUnixAppDir = tpmsConfiguration.getDvlUnixWebAppDir();
    }

    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String REQID = (!_DEBUG ? session.getId() + "_" + Long.toString(System.currentTimeMillis()) : action);
        //String owner = (String) session.getAttribute("unixUser");
        String xmlFileName = (request.getParameter("xmlFileName") != null ? request.getParameter("xmlFileName") : (request.getParameter("repFileName") != null ? request.getParameter("repFileName") : null));
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        String unixUser = (String) session.getAttribute("unixUser");
        //String repFileName=(!_DEBUG ? _webAppDir+"/"+"images"+"/"+REQID+"_rep.xml" : _webAppDir+"/"+"images"+"/"+action+"_rep.xml");
        // RepFileName to create from CC Command
        String repFileNameCc;
        // RepFileName for Out Page
        String repFileNameOut;
        repFileNameOut = (!_DEBUG ? _webAppDir + "/" + "images" + "/" + REQID + "_rep.xml" : _webAppDir + "/" + "images" + "/" + action + "_rep.xml");
        /*
        DVL Execution
        *  if dvlBool=true --> RepFileNameCc producec in Unix System
        *  if dvlBool=false --> RepFileNameCc producec in Windows System
        */ 
        if (_DEBUG) {
            repFileNameCc = _webAppDir + "/" + "images" + "/" + action + "_rep.xml";
        } else if (_dvlBool) {
            repFileNameCc = _dvlUnixAppDir + "/" + "images" + "/" + REQID + "_rep.xml";
        } else {
            repFileNameCc = _webAppDir + "/" + "images" + "/" + REQID + "_rep.xml";
        }
        String nextPage = getServletConfig().getInitParameter("nextPage"); 

        String outPage = action.toLowerCase() + "_out.jsp";
        debugLog("TpActionServlet :: doPost : " + outPage);
        String actionTxt = getServletConfig().getInitParameter("actionTxt");

        TpAction tpAction = null;
        if (action.equals("extr_to_valid")) {
        	tpAction = new TpExtrToValidAction(action, log);
        	debugLog("TpActionServlet :: doPost : extr_to_valid");
        }
        if (action.equals("tp_update")) {
        	tpAction = new TpMarkAsValidAction(action, log);
        	debugLog("TpActionServlet :: doPost : tp_update");
        }
        if (action.equals("tp_toprod")) {
        	tpAction = new TpMoveToProdAction(action, log);
        	debugLog("TpActionServlet :: doPost : tp_toprod");
        }
        if (action.equals("tp_submit")) {
        	tpAction = new TpSubmitAction(action, log);
        	debugLog("TpActionServlet :: doPost : tp_submit");
        }
        if (action.equals("tp_restore")) tpAction = new TpRestoreAction(action, log);
        if (action.equals(TP_REJECT_ACTION_VALUE)) {
            tpAction = new TpRejectAction(action, log);
            debugLog("TpActionServlet :: doPost : " + TP_REJECT_ACTION_VALUE);
        }
        if (action.equals(TP_UPDATE_STEPS_ACTION_VALUE)) {
            tpAction = new TpMarkAsValidAction(action, log);
            debugLog("TpActionServlet :: doPost : " + TP_UPDATE_STEPS_ACTION_VALUE);
        }

        if (tpAction == null) tpAction = new TpAction(action, log);
        try {    	 
            Element userData = CtrlServlet.getUserData(currentUser.getTpmsLogin());
            //Element adminData = CtrlServlet.getAdminData();
            String defltWorkDir = NavDirServlet.getDefaultWorkDirPath(userData);
            String fromEmail = XmlUtils.getVal(userData, "EMAIL");
            //String adminLogin = XmlUtils.getVal(adminData, "UNIX_USER");
            /**
             * TP DATA FETCH FROM THE META DATA RECORDSET (XML)
             */
            Properties params = new Properties();
            String jobName;
            String jobRel;
            String jobRev;
            String jobVer;
            Element tpRec;
            Element tpRec2;
            if ((xmlFileName != null) && (request.getParameter("jobname") != null)) {
                jobName = request.getParameter("jobname");
                jobRel = request.getParameter("job_rel");
                jobRev = request.getParameter("job_rev");
                jobVer = request.getParameter("job_ver");
                debugLog("TpActionServlet :: doPost : Get DATA of jobName=" + jobName + ", jobRel=" + jobRel + ", jobRev=" + jobRev + ", jobVer=" + jobVer);
                if (action.equals("tp_restore")) {
                    tpRec = TpDetailViewServlet.getTpData(xmlFileName, "db", jobName, jobRel, jobRev, jobVer);
                } else {
                    tpRec = TpDetailViewServlet.getTpData(xmlFileName, "vob", jobName, jobRel, jobRev, jobVer);
                }
                debugLog("TpActionServlet :: doPost : Loaded jobname=" + 
                		XmlUtils.getVal(tpRec, "JOBNAME") +
                		", jobRel=" + XmlUtils.getVal(tpRec, "JOB_REL") + 
                		", jobRev=" + XmlUtils.getVal(tpRec, "JOB_REV") + 
                		", jobVer=" + XmlUtils.getVal(tpRec, "JOB_VER"));
            } else if ((xmlFileName != null) && (request.getParameter("jobLabel1") != null)) {
                String label1 = request.getParameter("jobLabel1");
                debugLog("TpActionServlet :: doPost : JOB-LABEL>" + label1);
                tpRec = TpDetailViewServlet.getTpData(xmlFileName, "vob", label1);
                if (request.getParameter("jobLabel2") != null) {
                    String label2 = request.getParameter("jobLabel2");
                    debugLog("TpActionServlet :: doPost : JOB-LABEL2>" + label2);
                    tpRec2 = TpDetailViewServlet.getTpData(xmlFileName, "vob", label2);
                    params.setProperty("jobname2", XmlUtils.getVal(tpRec2, "JOBNAME"));
                    params.setProperty("release_nb2", XmlUtils.getVal(tpRec2, "JOB_REL"));
                    params.setProperty("revision_nb2", XmlUtils.getVal(tpRec2, "JOB_REV"));
                    params.setProperty("version_nb2", XmlUtils.getVal(tpRec2, "JOB_VER"));
                }
            } else {
                debugLog("TpActionServlet :: doPost : TP DATA INITIALIZATION...");
                if (action.equals("tp_restore")) {
                    tpRec = (Element) session.getAttribute("tpRec");
                    debugLog("TpActionServlet :: doPost : xml file name=" + xmlFileName);
                } else {
                    tpRec = TpDetailViewServlet.getTpData(request);
                }
                session.setAttribute("newTpRec", tpRec);
                debugLog("TpActionServlet :: doPost : TP DATA INITIALIZED");
            }
            String ownerEmail = XmlUtils.getVal(tpRec, "OWNER_EMAIL");
            String facility = XmlUtils.getVal(tpRec, "FACILITY");
            String testerInfo = XmlUtils.getVal(tpRec, "TESTER_INFO");
            if (request.getParameter("submitAction") == null) {
                //if the currentUser.getTpmsLogin() was displaing the read-only page containing tp data gather some usefull informations and redirect to the rigth jsp.
            	nextPage = action.toLowerCase() + "_form.jsp";
            	String testrowcount = request.getParameter("testcount");
            	if (testrowcount!=null){
            		tpRec = (Element) session.getAttribute("tpRec");
            		request.setAttribute("testcount", testrowcount);
            		String tlineset = request.getParameter("LINE_SET");
           		 	request.setAttribute("LINE_SET",tlineset);
           		 	String ttesterfam = request.getParameter("TESTER_FAMILY");
           		 	request.setAttribute("TESTER_FAMILY",ttesterfam);
            		String tjobName = request.getParameter("tempJobname");
             	    request.setAttribute("tempJobname",tjobName);
            	    String tjobrel = request.getParameter("tempjob_rel");
            		request.setAttribute("tempjob_rel",tjobrel);
            	    String tjobrev = request.getParameter("tempjob_rev");
            		request.setAttribute("tempjob_rev",tjobrev);
            		String tfacility = request.getParameter("tempfacility");
            		request.setAttribute("tempfacility",tfacility);
            		String ttesterInfo = request.getParameter("temptesterInfo");
            		request.setAttribute("temptesterInfo",ttesterInfo);
            		String ttoPlant = request.getParameter("temptoPlant");
            		request.setAttribute("temptoPlant",ttoPlant);
            		String tline = request.getParameter("templine");
            		request.setAttribute("templine",tline);
            		String tarea_prod = request.getParameter("FIELD.AREA_PROD");
            		request.setAttribute("FIELD.AREA_PROD",tarea_prod);
            		String tvalid_login = request.getParameter("tempvalid_login");
            		request.setAttribute("tempvalid_login",tvalid_login);
            		String tprod_login = request.getParameter("tempprod_login");
            		request.setAttribute("tempprod_login",tprod_login);
            		String temail_to = request.getParameter("tempemail_to");
            		request.setAttribute("tempemail_to",temail_to);
            		String temail_cc = request.getParameter("tempemail_cc");
            		request.setAttribute("tempemail_cc",temail_cc);
               		String tsingleDeliveryCmt = request.getParameter("tempsingleDeliveryCmt");
            		request.setAttribute("tempsingleDeliveryCmt",tsingleDeliveryCmt);
            		String tdeliveryComm = request.getParameter("tempdeliveryComm");
            		request.setAttribute("tempdeliveryComm",tdeliveryComm);
            		String thw_modification = request.getParameter("temphw_modification");
            		request.setAttribute("temphw_modification",thw_modification);
            		String texp_avg_yv = request.getParameter("tempexp_avg_yv");
            		request.setAttribute("tempexp_avg_yv",texp_avg_yv);
            		String tzero_yw = request.getParameter("tempzero_yw");
            		request.setAttribute("tempzero_yw",tzero_yw);
            		String tnew_testtime = request.getParameter("tempnew_testtime");
            		request.setAttribute("tempnew_testtime",tnew_testtime);
            		String tis_temp = request.getParameter("tempis_temp");
            		request.setAttribute("tempis_temp",tis_temp);
            		String tvalid_till = request.getParameter("tempvalid_till");
            		request.setAttribute("tempvalid_till",tvalid_till);
            		String txferFileDir = request.getParameter("tempxferFileDir");
            		request.setAttribute("tempxferFileDir",txferFileDir);
            		String txferFile = request.getParameter("tempxferFile");
            		request.setAttribute("tempxferFile",txferFile);
            		String tdisemail = request.getParameter("tempdisEmail");
            		request.setAttribute("tempdisEmail",tdisemail);
            		String ttempAddRow = request.getParameter("tempAddRow");
            		request.setAttribute("tempAddRow",ttempAddRow);
            		//Array definition
            		StringTokenizer addrow = new StringTokenizer(ttempAddRow, "~");
            		int rowNo = 1;
            		while(addrow.hasMoreTokens()) {
            			String Row = addrow.nextToken();
            			StringTokenizer addrow1 = new StringTokenizer(Row, "|");
             			while(addrow1.hasMoreTokens()){
             				String testNo = addrow1.nextToken();
            				if (testNo.equals("EMPTY")){
            					testNo="";
            				}
            				String newFlag = addrow1.nextToken();
            				String oldLSL = addrow1.nextToken();
            				if(oldLSL.equals("EMPTY")){
            					oldLSL="";
            				}
            				String oldUSL = addrow1.nextToken();
            				if(oldUSL.equals("EMPTY")){
            					oldUSL="";
            				}
            				String unit = addrow1.nextToken();
            				if(unit.equals("EMPTY")){
            					unit="";
            				}
             				String newLSL = addrow1.nextToken();
            				if (newLSL.equals("EMPTY")){
            					newLSL="";
            				}
            				String newUSL = addrow1.nextToken();
            				if	(newUSL.equals("EMPTY")){
            					newUSL="";
            				}
            				String testComment = addrow1.nextToken();
            				if (testComment.equals("EMPTY")){
            					testComment="";
            				}
            				BeanTest beanTest = new BeanTest();
                			beanTest.setTestNo(testNo);  
                			beanTest.setOldUSL(oldUSL);
                			beanTest.setNewFlag(newFlag);
                			beanTest.setUnit(unit);
                			beanTest.setOldLSL(oldLSL); 
                			beanTest.setNewLSL(newLSL);
                			beanTest.setNewUSL(newUSL); 
                			beanTest.setTestComment(testComment);
                			beanTest.setRowNo(rowNo);
                			request.setAttribute(rowNo + "", beanTest);			 
                			session.setAttribute(rowNo + "", beanTest);
            			}
            			rowNo++;            			 
            		} 
            	} else {
            		/* default one row appear in tp_submit_form.jsp, so start from 1 */
            		testrowcount = "1";
            		request.setAttribute("testcount", testrowcount);
            	}
                request.setAttribute("workDir", defltWorkDir);
                request.setAttribute("fromEmail", fromEmail);
                request.setAttribute("ownerEmail", ownerEmail);
                request.setAttribute("facility", facility);
                request.setAttribute("testerInfo", testerInfo);
                request.setAttribute("tpRec", tpRec);
                if (action.equals("tp_restore")) {
                    request.setAttribute("jobname", XmlUtils.getVal(tpRec, "JOBNAME"));
                    request.setAttribute("job_rel", XmlUtils.getVal(tpRec, "JOB_REL"));
                    request.setAttribute("job_rev", XmlUtils.getVal(tpRec, "JOB_REV"));
                    request.setAttribute("job_ver", XmlUtils.getVal(tpRec, "JOB_VER"));
                    debugLog("TpActionServlet :: doPost : Put jobname=" + 
                    		XmlUtils.getVal(tpRec, "JOBNAME") +
                    		", jobRel=" + XmlUtils.getVal(tpRec, "JOB_REL") + 
                    		", jobRev=" + XmlUtils.getVal(tpRec, "JOB_REV") + 
                    		", jobVer=" + XmlUtils.getVal(tpRec, "JOB_VER"));
                }
            } else {
                //TpFlowMgr.checkPermForAction(unixUser, tpRec, action);
            	//String AddrowUser = (String) session.getAttribute("user");
            	tpAction.setReqID(REQID);
                tpAction.setRepFileName(repFileNameCc);
                tpAction.setUserName(unixUser);
                tpAction.setUserRole((String) session.getAttribute("role"));
                tpAction.setTpRec(tpRec);
                tpAction.setParams(params);
                tpAction.setWebAppDir(_webAppDir);
                tpAction.setSessionId(session.getId());
                if (action.equals("tp_submit")){
                	String addRowArray = request.getParameter("addRowValue");
                 	if (!addRowArray.equals("")) {
                 		tpAction.setAddRow(addRowArray);
                 		tpAction.setTestRowCount(Integer.parseInt(request.getParameter("testrowcount")));
                	}
                }

                if (tpAction.hasDbAction()) {
                    tpAction.setDbWrtr((oneConnDbWrtr) this.getServletContext().getAttribute("dbWriter"));
                }
                debugLog("TpActionServlet :: doPost : TP-ACTION-START-" + REQID + 
                		", ACTION-TYPE=" + action + ", USER=" + unixUser);
                if (!action.equals("tp_restore")) {
                    debugLog("TpActionServlet :: doPost : TP-" + REQID + ">" + XmlUtils.getVal(tpRec, "TP_LABEL"));
                    String workDir = request.getParameter("workDir");
                    debugLog("TpActionServlet :: doPost : WORKDIR-" + REQID + ">" + workDir);
                    params.setProperty("extract_dir", workDir);
                    params.setProperty("update_dir", workDir);
                    params.setProperty("work_dir", workDir);
                    params.setProperty("from_mail", (request.getParameter("fromEmail") != null ? request.getParameter("fromEmail") : ""));

                    //if action == 'tp_submit' values of  'cc_email' e 'to_email' Comes from the form related to TP creation
                    if (!action.equals("tp_submit")) {
                        params.setProperty("to_mail", (request.getParameter("toEmail") != null ? request.getParameter("toEmail") : ""));
                        params.setProperty("cc_mail1", (request.getParameter("toEmail") != null ? request.getParameter("toEmail") : ""));
                        params.setProperty("cc_mail2", (request.getParameter("ccEmail") != null ? request.getParameter("ccEmail") : ""));
                        if (action.equals("tp_update")){
                        	setMultiLineProperty(params, "comment", (request.getParameterValues("markAsValCmt") != null ? request.getParameterValues("markAsValCmt")[0] : ""));
                        	String markAsValCmt = request.getParameter("markAsValCmt");
                        	tpAction.setMarkAsValCmt(markAsValCmt);
                        }
                        if (action.equals(TP_REJECT_ACTION_VALUE)){
                            setMultiLineProperty(params, "comment", (request.getParameterValues("rejectCmt") != null ? request.getParameterValues("rejectCmt")[0] : ""));
                            String rejectCmt = request.getParameter("rejectCmt"); 
                            tpAction.setRejectCmt(rejectCmt);
                        }
                        if (action.equals("extr_to_valid")) {
                        	setMultiLineProperty(params, "comment", (request.getParameterValues("extToValidateCmt") != null ? request.getParameterValues("extToValidateCmt")[0] : ""));
                        	String extToValidateCmt = request.getParameter("extToValidateCmt");
                            tpAction.setExtToValidateCmt(extToValidateCmt);
                        }
                        if (action.equals("tp_toprod")) {
                        	setMultiLineProperty(params, "comment", (request.getParameterValues("putInProdCmt") != null ? request.getParameterValues("putInProdCmt")[0] : ""));
                        	String putInProdCmt = request.getParameter("putInProdCmt");
                            tpAction.setPutInProdCmt(putInProdCmt);
                        }
                     }
                  
                    setMultiLineProperty(params, "comment", (request.getParameterValues("FIELD.SINGLEDELIVERYCMT") != null ? request.getParameterValues("FIELD.SINGLEDELIVERYCMT")[0] : ""));
 
                    for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                        String parnam = (String) e.nextElement();
                        if (parnam.startsWith("tpFile")) {
                            if (parnam.equals("tpFile")) {
                                params.setProperty("tp_file", request.getParameter(parnam));
                            } else {
                                String index = parnam.substring(6);
                                if (request.getParameter("chk" + index) != null) {
                                    params.setProperty("tp_file_" + index, request.getParameter(parnam));
                                }
                            }
                        }
                    }
                }
                if (session.getAttribute("exception" + "_" + REQID) != null) session.removeAttribute("exception" + "_" + REQID);
                session.setAttribute("startBool" + "_" + REQID, Boolean.TRUE);
                //FF 26 Oct 2005: if vob type is R  AND the action executed is (extr_to_valid OR tp_update OR tp_toprod )
                //the unix currentUser.getTpmsLogin() used to call clear case interfare MUST BE the administrator (vobadm)
                //FF 28 Apr 2006 the new condition to execute the action as vobadm if (R Or D) AND the action executed is (extr_to_valid OR tp_update OR tp_toprod )
                // D vob type added
                String vobType = (String) session.getAttribute("vobType");
                debugLog("TpActionServlet :: doPost : vobType=" + vobType);
                //String myUnixUser = tpAction.getCcActionUnixLogin(owner, adminLogin);
                //if ((vobType.equals("R") || vobType.equals("D")) && (action.equals("extr_to_valid") || action.equals("tp_update") || action.equals("tp_toprod")) ) {
                String myUnixUser = TpUtils.getUserForActionExecution(currentUser.getTpmsLogin(), vobType, action);

                debugLog("TpActionServlet :: doPost : VobActionDaemon.doAction....");
                VobActionDaemon.doAction(this.log, backgrndBool, tpAction, actionTxt, session, (!_DEBUG ? REQID : action), repFileNameCc, myUnixUser, _timeOut);
                debugLog("TpActionServlet :: doPost : VobActionDaemon.doAction started");
                if (xmlFileName != null) request.setAttribute("xmlFileName", xmlFileName);
                 request.setAttribute("outPage", outPage);
                request.setAttribute("actionTxt", actionTxt);
                request.setAttribute("refreshTime", getServletConfig().getInitParameter("refreshTime"));
                request.setAttribute("reqId", REQID);
                request.setAttribute("repFileName", repFileNameOut);
                request.setAttribute("fromEmail", fromEmail);
                //Send Race info
                String toAddress = XmlUtils.getVal(tpRec, "DIS_EMAIL");
                if(action != null && action.equals("tp_submit") && !toAddress.equals("")){
                    sendMail(request, tpRec);
                  }
                if ((action.equals("tp_submit")) ||(action.equals(TP_REJECT_ACTION_VALUE)) ||(action.equals("tp_update")) ){
                	nextPage = getServletConfig().getInitParameter("nextPageSubmit"); 
                	debugLog("TpActionServlet :: doPost : next page :" + nextPage);
                }
            }
            CtrlServlet.setAttributes(request);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);
        }
        catch (Exception e) {
            session.setAttribute("startBool" + "_" + REQID, Boolean.FALSE);
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }
    }
       
    
    public static void setMultiLineProperty(Properties props, String name, String val) {
        StringTokenizer tknzr = new StringTokenizer(val, "\n", false);
        for (int i = 0; (i < 10) && (tknzr.hasMoreTokens()); i++) {
            props.setProperty(name + Integer.toString(i), tknzr.nextToken().trim());
        }
    }
     
	
    private void sendMail(HttpServletRequest request, Element tpRec) throws TpmsException { 
    	HttpSession session = request.getSession();
    	StringBuffer subjectBuffer = new StringBuffer(); 
    	StringBuffer contentBuffer = new StringBuffer();
    	String content = "";
    	try {
    		String mailHost = getServletContext().getInitParameter("mailServerName");
    		String fromAddress = getServletContext().getInitParameter("mailFromAddress");
    		String fromPlant = getServletContext().getInitParameter("TpmsInstName");
    		String tpmsUser = (String) session.getAttribute("user");
    		if ((mailHost == null) || (fromAddress == null)) {
    			debugLog("TpActionServlet :: sendMail : Configuration setting (mailServerName/mailFromAddress) not complete!");
    			return;
    		}
    		String toAddress = XmlUtils.getVal(tpRec, "DIS_EMAIL");
    		//For Mail subject design
    		String jobname = XmlUtils.getVal(tpRec, "JOBNAME");
    		String releaseNb = XmlUtils.getVal(tpRec, "JOB_REL");
    		String revisionNb = XmlUtils.getVal(tpRec, "JOB_REV");
    		String destinationPlant = XmlUtils.getVal(tpRec, "TO_PLANT");
    		// For Mail body content design
    		String deliveryComm = XmlUtils.getVal(tpRec, "DELIVERYCOMM");
    		String hwModification = XmlUtils.getVal(tpRec, "HW_MODIFICATION");
    		String expAvgYv = XmlUtils.getVal(tpRec, "EXP_AVG_YV");
    		String newTesttime = XmlUtils.getVal(tpRec, "NEW_TESTTIME");
    		String zeroYw = XmlUtils.getVal(tpRec, "ZERO_YW");
    		String isTemp = XmlUtils.getVal(tpRec, "IS_TEMP");
     		String validTill = XmlUtils.getVal(tpRec, "VALID_TILL");  		
    		Vector toAddresses = new Vector();
    		toAddresses.add(toAddress);
    		subjectBuffer.append("TPMSw RACE Notification TP ");
    		subjectBuffer.append(jobname);
    		subjectBuffer.append(".");
    		subjectBuffer.append(releaseNb);
    		subjectBuffer.append(".");
    		subjectBuffer.append(revisionNb);
    		String subject = subjectBuffer.toString();
    		contentBuffer.append("TPMSw RACE Notification.\n");
    		contentBuffer.append("=====================================\n");
    		contentBuffer.append("TPMS USERS  :  ");
    		contentBuffer.append(tpmsUser);
    		contentBuffer.append("\nTP Delivered  :  ");
    		contentBuffer.append(jobname + "." + releaseNb + "." + revisionNb);
    		contentBuffer.append("\nfrom plant  :  ");
    		contentBuffer.append(fromPlant);
    		contentBuffer.append("\nto plant  :  ");
    		contentBuffer.append(destinationPlant);
    		contentBuffer.append("\n\n" + "RACE Comment\n");
    		contentBuffer.append("------------------------------------- \n");
    		contentBuffer.append(deliveryComm);
    		contentBuffer.append("\n\n" + "TESTS Information\n");
    		contentBuffer.append("------------------------------------- \n");
    		String [] testNos = request.getParameterValues("FIELD.TEST_NO");
    		String [] newFlags = request.getParameterValues("FIELD.NEW_FLAG");
    		String [] oldLsls = request.getParameterValues("FIELD.OLD_LSL");
    		String [] oldUsls = request.getParameterValues("FIELD.OLD_USL");
    		String [] units = request.getParameterValues("FIELD.UNIT");
    		String [] newLsls = request.getParameterValues("FIELD.NEW_LSL");
    		String [] newUsls = request.getParameterValues("FIELD.NEW_USL");
    		String [] testComments = request.getParameterValues("FIELD.TESTS_COMMENTS");
    		
    		String testNo = null;
    		String newFlag = null;
    		String oldLsl = null;
    		String oldUsl = null;
    		String unit = null;
    		String newLsl = null;
    		String newUsl = null;
    		String testComment = null;
    		
    		for (int index = 1; index < testNos.length; index++) {
    			testNo = testNos[index];
    			newFlag = newFlags[index];
    			oldLsl = oldLsls[index];
    			oldUsl = oldUsls[index];
    			unit = units[index];
    			newLsl = newLsls[index];
    			newUsl = newUsls[index];
    			testComment = testComments[index];
    			if (!testNo.equals("")) {
	    			contentBuffer.append(index + " TEST#:[");
	    			contentBuffer.append(testNo);
	    			contentBuffer.append("]\nNew Flag=[");
	    			contentBuffer.append(newFlag);
	    			contentBuffer.append("]\tUnit=[");
	    			contentBuffer.append(unit);
	    			contentBuffer.append("]\nOld LSL=[");
	    			contentBuffer.append(oldLsl);
	    			contentBuffer.append("]\tOld USL=[");
	    			contentBuffer.append(oldUsl);
	    			contentBuffer.append("]\nNew LSL=[");
	    			contentBuffer.append(newLsl);
	    			contentBuffer.append("]\tNew USL=[");
	    			contentBuffer.append(newUsl);
	    			contentBuffer.append("]\nTest Comment=[");
	    			contentBuffer.append(testComment);
	    			contentBuffer.append("]\t\n");	
    			}	
    		}  // for (int index ..
    		contentBuffer.append("\n\nHW Modifications\n");
    		contentBuffer.append("------------------------------------- \n");
    		contentBuffer.append(hwModification);   	
    		contentBuffer.append("\n\nPerformance Indexes\n");
    		contentBuffer.append("------------------------------------- \n");
    		contentBuffer.append("\n\nExpected Average Yield Variation [%]:  ");
    		contentBuffer.append(expAvgYv);
    		contentBuffer.append("\n\nZero Yield Wafer [%]:  ");
    		contentBuffer.append(zeroYw);
    		contentBuffer.append("\n\nNew Test Time [ms]:  ");
    		contentBuffer.append(newTesttime);
    		contentBuffer.append("\n\nIs Temporary: ");
    		contentBuffer.append(isTemp);
    		contentBuffer.append("\n\nValid Till: ");
    		contentBuffer.append(validTill);
    		contentBuffer.append("\n\n-----------------------------------------------\nMail sent on ");
    		Calendar now = Calendar.getInstance();
    		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    		contentBuffer.append(formatter.format(now.getTime()));
    		contentBuffer.append("\n");
    		content = contentBuffer.toString();
    		MailUtils.sendMail(mailHost, fromAddress, toAddresses, subject, content);
    		debugLog("TpActionServlet :: sendMail : TP Email sent for " + subject);
        } catch (Exception ex) {
            errorLog("TpActionServlet :: sendMail : error while sending mail: " + ex.getMessage(), ex);
            ex.printStackTrace();
            try	{
            	EMailTrack.trackQuery(session.getId(), (String) session.getAttribute("unixUser"), content);
            } catch (Exception e) {
                errorLog("TpActionServlet :: sendMail : error while sending mail: " + ex.getMessage(), ex);
                ex.printStackTrace();
            }
            throw new TpmsException(ex.getMessage());
        }  finally {
			try	{
				if (subjectBuffer != null) {
					subjectBuffer = null;
				}
				if (contentBuffer != null) {
					contentBuffer = null;
				}
			} catch (Exception e){
				errorLog("TPActionServlet :: sendMail : Fail to delocate contentBuffer!");
			}
        }
    }
}
