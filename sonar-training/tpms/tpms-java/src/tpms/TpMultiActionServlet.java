package tpms;

import it.txt.general.utils.XmlUtils;
import it.txt.general.SessionObjects;
import it.txt.tpms.users.TpmsUser;
import it.txt.tpms.tp.utils.TpUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tol.oneConnDbWrtr;
import tpms.utils.EMailTrack;
import tpms.utils.MailUtils;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 26, 2003
 * Time: 3:36:08 PM
 * To change this template use Options | File Templates.
 * FP rev5 - ridirezione sulla classe TpDeleteAction
 */
public class TpMultiActionServlet extends TpActionServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        debugLog("TPMultiActionServlet :: doPost : action : " + action);
        //the GLOBAL_REQID timestamp referring for all serial action
        String GLOBAL_REQID = (!_DEBUG ? session.getId() + "_" + Long.toString(System.currentTimeMillis()) : action);
        String testerFam = (request.getParameter("testerFam") != null ? request.getParameter("testerFam") : "");
        String baseline = (request.getParameter("baseline") != null ? request.getParameter("baseline") : "");
        String xmlFileName = (request.getParameter("xmlFileName") != null ? request.getParameter("xmlFileName") : (request.getParameter("repFileName") != null ? request.getParameter("repFileName") : null));
        String repFileName = (!_DEBUG ? _webAppDir + "/" + "images" + "/" + GLOBAL_REQID + "_rep.xml" : _webAppDir + "/" + "images" + "/" + action + "_rep.xml");
        String nextPage = getServletConfig().getInitParameter("nextPage");
        if (action != null && action.equals("tp_delivery")) {
            nextPage = getServletConfig().getInitParameter("nextPageNew");
        }

        String outPage = action.toLowerCase() + "_out.jsp";
        //define a new out page for the multi put in production.
        //in the single put in production (TpActionServlet) the out page is tp_prod_out.jsp
        if (action.equals("tp_toprod")) outPage = "tp_production_out.jsp";
        String actionTxt = getServletConfig().getInitParameter("actionTxt");

        Vector tpActions = new Vector();
        try {
            String user = (String) session.getAttribute("user");
            String unixUser = (String) session.getAttribute("unixUser");
            Element userData = CtrlServlet.getUserData(user);
            String defltWorkDir = NavDirServlet.getDefaultWorkDirPath(userData);

            /**
             * TP DATA FETCH FROM THE META DATA RECORDSET (XML)
             */
            //creation new object Dom with all
            //element TpRec checked
            Document tpMultiDoc;
            //return  root document
            Element tpRecs;
            if (xmlFileName != null) {
                //TP data MUST be gathered from xml file (the user has selected TPs from a RO list)
                //than add tp data to sesion and  present it to the user
                int tpNumber = 0;
                //for n request started chkTP_---->for n tp checked
                //creation n istance TpAction
                //creation n element tpRec
                tpMultiDoc = XmlUtils.getNewDoc("TPS");
                tpRecs = tpMultiDoc.getDocumentElement();
                for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                    String parnam = (String) e.nextElement();
                    String index = Integer.toString(tpNumber + 1);
                    //control to the parameter start with chk
                    if (parnam.startsWith("chkTP_")) {
                        String jobLabel = parnam.substring(3);
                        debugLog("TPMultiActionServlet :: doPost: JOB-LABEL-" + index + ">" + jobLabel);
                        //from XML get l'element tpRec with equal jobLabel
                        Element tpRec = TpDetailViewServlet.getTpData(xmlFileName, "vob", jobLabel);

                        if (tpRec != null) {
                            //add a  index to tpRec
                            XmlUtils.addEl(tpRec, "INDEX", index);
                            //add testerfam
                            XmlUtils.addEl(tpRec, "TESTERFAM", testerFam);
                            debugLog("baseline=" + baseline);
                            //add baseline
                            XmlUtils.addEl(tpRec, "LASTBASELINE", baseline);
                            debugLog("TP-FETCHED>");
                            //update tpRec with new data delivery form
                            updateTpDataFromRequest(tpRec, request);
                            debugLog("TP-UPDATED-FROM-REQ-PARAMS>");
                            //copy element tpRec in the  root Dom file
                            XmlUtils.copy(tpRec, tpRecs);
                            //find TP_LABEL from multidoc in session to set in TPAction
                            tpRec = XmlUtils.findEl(tpRecs.getElementsByTagName("TP"), "TP_LABEL", jobLabel);
                            debugLog("TP-APPENDED>");
                            Properties params = new Properties();
                            debugLog("PROPERTIES-SET-UP>");
                            /*** TP ACTION INSTANTIATION ***/
                            TpAction tpAction = null;
                            if (action.equals("tp_delivery")) tpAction = new TpDeliveryAction(action, log);
                            if (action.equals("tp_toprod")) tpAction = new TpMoveToProdAction(action, log);
                            if (action.equals("tp_delete")) tpAction = new TpDeleteAction(action, log);
                            if (tpAction == null) tpAction = new TpAction(action, log);
                            //FF 13/10/2005 added for 1log4eachFailed query
                            tpAction.setSessionId(session.getId());
                            String addRowArray = (request.getParameter("tempAddRow") != null ? request.getParameter("tempAddRow") : "");
                            if(!addRowArray.equals("")) {
                        		tpAction.setAddRow(addRowArray);
                        	}
                            String Race = (request.getParameter("tempRace") != null ? request.getParameter("tempRace") : "");
                            tpAction.setAddRace(Race);
                            tpAction.setMultiDeliveryCmt(request.getParameter("multiDeliveryCmt") != null ? request.getParameter("multiDeliveryCmt") : "");
                            //FF 13/10/2005 added for 1log4eachFailed query
                            tpAction.setUserName((String) session.getAttribute("user"));
                            tpActions.addElement(tpAction);
                            tpAction.setReqID(GLOBAL_REQID + "_" + index);
                            //add a element reqid to the tpRec
                            //use reqid for verify the single action completed
                            XmlUtils.addEl(tpRec, "REQID", tpAction.getReqID());
                            //set repfileName to the TpAction
                            tpAction.setRepFileName(_webAppDir + "/" + "images" + "/" + GLOBAL_REQID + "_" + index + "_rep.xml");
                            ////add a element repFileName to the tpRec
                            XmlUtils.addEl(tpRec, "repFileName", tpAction.getRepFileName());
                            tpAction.setUserName(unixUser);
                            //FF 15/09/2005 aggiunta gestione cancellazione tpper utente: necessario passare il ruolo
                            debugLog("TPMultiActionServlet :: doPost : session user role> " + session.getAttribute("role"));
                            tpAction.setUserRole((String) session.getAttribute("role"));
                            //set TpRec inthe tpAction with element tpRec
                            tpAction.setTpRec(tpRec);
                            //
                            if (action.equals("tp_toprod")) {
                                params.setProperty("from_mail", (XmlUtils.getVal(tpRec, "OWNER_EMAIL") != null ? XmlUtils.getVal(tpRec, "OWNER_EMAIL") : ""));
                                params.setProperty("to_mail", (XmlUtils.getVal(tpRec, "EMAIL_TO") != null ? XmlUtils.getVal(tpRec, "EMAIL_TO") : ""));
                                params.setProperty("cc_mail1", (XmlUtils.getVal(tpRec, "EMAIL_CC") != null ? XmlUtils.getVal(tpRec, "EMAIL_CC") : ""));
                                params.setProperty("cc_mail2", (XmlUtils.getVal(tpRec, "EMAIL_FREE") != null ? XmlUtils.getVal(tpRec, "EMAIL_FREE") : ""));
                            }
                              	setMultiLineProperty(params, "comment", (request.getParameterValues("multiDeliveryCmt") != null ? request.getParameterValues("multiDeliveryCmt")[0] : ""));
                            tpAction.setParams(params);
                            //control if there are action to DB
                            if (tpAction.hasDbAction()) {
                                tpAction.setDbWrtr((oneConnDbWrtr) this.getServletContext().getAttribute("dbWriter"));
                            }
                            String newtpLabel = "TP_" + XmlUtils.getVal(tpRec, "JOBNAME")+"."+XmlUtils.getVal(tpRec, "JOB_REL")+"."+XmlUtils.getVal(tpRec, "JOB_REV")+"."+XmlUtils.getVal(tpRec, "JOB_VER");
                            String tpLabel = XmlUtils.getVal(tpRec, "TP_LABEL");
                            String toAddress = request.getParameter("FIELD.DIS_EMAIL");
                            if (toAddress != null && !tpLabel.equals(newtpLabel)){
                             	sendMail(request, tpRec);//Multi Mail sending
                            }
                            tpNumber++;
                        }
                    }
                }
            } else {
                int i = 0;
                tpMultiDoc = (Document) session.getAttribute("tpMultiDoc");
                tpRecs = tpMultiDoc.getDocumentElement();
                Document tpMultiDoc2 = XmlUtils.getNewDoc("TPS");
                Element tpRecs2 = tpMultiDoc2.getDocumentElement();
                for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                    String parnam = (String) e.nextElement();
                    String index = Integer.toString(i + 1);
                    //control to the parameter start with chk
                    if (parnam.startsWith("chkTP_")) {
                        String jobLabel = parnam.substring(3);
                        debugLog("JOB-LABEL-" + index + ">" + jobLabel);
                        Element tpRec = XmlUtils.findEl(tpRecs.getElementsByTagName("TP"), "TP_LABEL", jobLabel);
                        XmlUtils.copy(tpRec, tpRecs2);
                        //find TP_LABEL from multidoc in session to set in TPAction
                        tpRec = XmlUtils.findEl(tpRecs2.getElementsByTagName("TP"), "TP_LABEL", jobLabel);
                        //RESET TP CHILD ELEMENTS (error_flag, error_msg, etc.)
                        Element tpError;
                        if ((tpError = XmlUtils.getChild(tpRec, "ERROR")) != null) tpRec.removeChild(tpError);
                        if ((tpError = XmlUtils.getChild(tpRec, "ERR_ACTION_TXT")) != null) tpRec.removeChild(tpError);
                        if ((tpError = XmlUtils.getChild(tpRec, "ERR_MSG_TXT")) != null) tpRec.removeChild(tpError);
                        if ((tpError = XmlUtils.getChild(tpRec, "ERR_DETAIL_TXT")) != null) tpRec.removeChild(tpError);
                        if ((tpError = XmlUtils.getChild(tpRec, "ERR_SYS_DETAIL_TXT")) != null) tpRec.removeChild(tpError);
                        debugLog("TP-RESET>");
                        Properties params = new Properties();
                        debugLog("PROPERTIES-SET-UP>");
                        /*** TP ACTION INSTANTIATION ***/
                        TpAction tpAction = null;
                        if (action.equals("tp_delivery")) tpAction = new TpDeliveryAction(action, log);
                        if (action.equals("tp_toprod")) tpAction = new TpMoveToProdAction(action, log);
                        if (tpAction == null) tpAction = new TpAction(action, log);
                        //FF 13/10/2005 added for 1log4eachFailed query
                        tpAction.setSessionId(session.getId());
                        //FF 13/10/2005 added for 1log4eachFailed query
                        tpAction.setUserName((String) session.getAttribute("user"));
                        tpActions.addElement(tpAction);
                        tpAction.setReqID(GLOBAL_REQID + "_" + index);
                        //add a element reqid to the tpRec
                        //use reqid for verify the single action completed
                        XmlUtils.setVal(tpRec, "REQID", tpAction.getReqID());
                        debugLog("TP REQI ID>" + XmlUtils.getVal(tpRec, "REQID"));
                        //set repfileName to the TpAction
                        tpAction.setRepFileName(_webAppDir + "/" + "images" + "/" + GLOBAL_REQID + "_" + index + "_rep.xml");
                        //add an element repFileName to the tpRec
                        XmlUtils.setVal(tpRec, "repFileName", tpAction.getRepFileName());
                        debugLog("TP REP FILENAME>" + XmlUtils.getVal(tpRec, "repFileName"));
                        tpAction.setUserName(unixUser);
                        //set TpRec in the tpAction to the element tpRec
                        tpAction.setTpRec(tpRec);
                        if (action.equals("tp_toprod")) {
                            params.setProperty("from_mail", (XmlUtils.getVal(tpRec, "OWNER_EMAIL") != null ? XmlUtils.getVal(tpRec, "OWNER_EMAIL") : ""));
                            params.setProperty("to_mail", (XmlUtils.getVal(tpRec, "EMAIL_TO") != null ? XmlUtils.getVal(tpRec, "EMAIL_TO") : ""));
                            params.setProperty("cc_mail1", (XmlUtils.getVal(tpRec, "EMAIL_CC") != null ? XmlUtils.getVal(tpRec, "EMAIL_CC") : ""));
                            params.setProperty("cc_mail2", (XmlUtils.getVal(tpRec, "EMAIL_FREE") != null ? XmlUtils.getVal(tpRec, "EMAIL_FREE") : ""));
                        }
                        tpAction.setParams(params);
                        //control if there are action to DB
                        if (tpAction.hasDbAction()) {
                            tpAction.setDbWrtr((oneConnDbWrtr) this.getServletContext().getAttribute("dbWriter"));
                        }
                        i++;
                    }
                }
                session.setAttribute("tpMultiDoc2", tpMultiDoc2);
            }
            if ((request.getParameter("submitAction") == null) ||(request.getParameter("submitAction").equals("null")) ) {
            	nextPage = action.toLowerCase() + "_form.jsp";
                  String testrowcount = request.getParameter("testcount");
            	if (testrowcount!=null){
            		request.setAttribute("testcount", testrowcount);
            	  	String ttesterfam = request.getParameter("TESTER_FAMILY");
           		 	request.setAttribute("TESTER_FAMILY",ttesterfam);
           		 	String tdeliveryComm = request.getParameter("tempdeliveryComm");
           		 	request.setAttribute("tempdeliveryComm",tdeliveryComm);
            		 String tmultiDeliveryCmt = request.getParameter("tempmultiDeliveryCmt");
            		 request.setAttribute("tempmultiDeliveryCmt",tmultiDeliveryCmt);
            		 debugLog("tdeliveryComm : "+ tdeliveryComm);
            		 String thw_modification = request.getParameter("temphw_modification");
            		 request.setAttribute("temphw_modification",thw_modification);
            		 debugLog("thw_modification : "+ thw_modification);
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
             		 String tdisemail = request.getParameter("tempdisemail");
            		 request.setAttribute("tempdisemail",tdisemail);
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
                 			beanTest.setNewFlag(newFlag); 
                 			beanTest.setUnit(unit);
                 			beanTest.setOldLSL(oldLSL); 
                 			beanTest.setOldUSL(oldUSL);
                 			beanTest.setNewLSL(newLSL);
                 			beanTest.setNewUSL(newUSL); 
                 			beanTest.setTestComment(testComment);
                 			beanTest.setRowNo(rowNo);
                 			request.setAttribute(rowNo + "",beanTest);
                 			session.setAttribute(rowNo + "",beanTest);
             			}
             			rowNo++;            			 
             		} 
            	} else {
            		/* default one row appear in tp_submit_form.jsp
            		 * so start from 1                              */
            		testrowcount = "1";
            		request.setAttribute("testcount", testrowcount);
            	}
                request.setAttribute("workDir", defltWorkDir);
                request.setAttribute("tpMultiDoc", tpMultiDoc);
            } else //go action clear case
            {
                //for each TpAction istance in the vector tpActions
                for (int i = 0; i < tpActions.size(); i++) {
                    TpAction tpAction = (TpAction) tpActions.elementAt(i);
                    //get tpRec in the tpAction
                    Element tpRec = tpAction.getTpRec();
                    //TpFlowMgr.checkPermForAction(unixUser, tpRec, action);
                    String REQID = tpAction.getReqID();
                    debugLog("TP-ACTION-START-" + REQID + ">");
                    debugLog("ACTION-TYPE-" + REQID + ">" + action);
                    debugLog("USER-" + REQID + ">" + unixUser);
                    debugLog("TP-" + REQID + ">" + XmlUtils.getVal(tpRec, "TP_LABEL"));
                }
                if (session.getAttribute("exception" + "_" + GLOBAL_REQID) != null) session.removeAttribute("exception" + "_" + GLOBAL_REQID);
                //set startBool_GLOBAL_REQID true
                TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
                String vobType = (String) session.getAttribute("vobType");

                session.setAttribute("startBool" + "_" + GLOBAL_REQID, Boolean.TRUE);
                VobMultiActionDaemon.doAction(log, _DEBUG, _dvlBool, _dvlUnixHost, _dvlCcInOutDir, _dvlUnixAppDir, _webAppDir, _execMode, backgrndBool, tpActions, actionTxt, session, GLOBAL_REQID, repFileName, _ccScriptsDir, _ccInOutDir, TpUtils.getUserForActionExecution(currentUser.getTpmsLogin(), vobType, action), _timeOut);

                request.setAttribute("outPage", outPage);
                request.setAttribute("actionTxt", actionTxt);
                request.setAttribute("refreshTime", getServletConfig().getInitParameter("refreshTime"));
                request.setAttribute("reqId", GLOBAL_REQID);
                request.setAttribute("repFileName", repFileName);
                request.setAttribute("xmlFileName", xmlFileName);
                session.setAttribute("tpMultiDoc", tpMultiDoc);
            }
            CtrlServlet.setAttributes(request);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);
        }
        catch (Exception e) {
            session.setAttribute("startBool" + "_" + GLOBAL_REQID, Boolean.FALSE);
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }
    }

    void updateTpDataFromRequest(Element tpRec, HttpServletRequest request) throws Exception {
        String tpLabel = XmlUtils.getVal(tpRec, "TP_LABEL");
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String parnam = (String) e.nextElement();
            if (parnam.startsWith("FIELD." + tpLabel + ".")) {
                String fieldName = parnam.substring(6 + tpLabel.length() + 1);
                //System.out.println("fieldnam>"+fieldName);
                String fieldVal = request.getParameter(parnam);
                String oldVal;
                Element fieldEl = XmlUtils.getChild(tpRec, fieldName);
                if (fieldEl == null) {
                    fieldEl = XmlUtils.addEl(tpRec, fieldName);
                    //System.out.println("fieldnam>"+"NOT EXISTS");
                    oldVal = null;
                } else {
                    //System.out.println("fieldnam>"+"EXISTS");
                    oldVal = XmlUtils.getVal(tpRec, fieldName);
                }
                XmlUtils.setVal(fieldEl, fieldVal);
                //System.out.println("fielval>"+"SET");
                fieldEl.setAttribute("oldVal", (oldVal != null ? oldVal : ""));
            }
        }
    }
    private void sendMail(HttpServletRequest request, Element tpRec) throws TpmsException {
    	int checkCount = 0;
    	String content= null;
    	HttpSession session = request.getSession();
    	StringBuffer subjectBuffer = new StringBuffer(); 
    	StringBuffer contentBuffer = new StringBuffer();
    	try {
    		String mailHost = getServletContext().getInitParameter("mailServerName");
    		String fromAddress = getServletContext().getInitParameter("mailFromAddress");
    		String toAddress = request.getParameter("FIELD.DIS_EMAIL");
    		String fromPlant = getServletContext().getInitParameter("TpmsInstName");
    		String tpmsUser = (String) session.getAttribute("user");
    		
    		//For Mail subject design
    		String jobname = XmlUtils.getVal(tpRec, "JOBNAME");
    		String releaseNb = XmlUtils.getVal(tpRec, "JOB_REL");
    		String revisionNb = XmlUtils.getVal(tpRec, "JOB_REV");
    		String versionNb = XmlUtils.getVal(tpRec, "JOB_VER");
    		String destinationPlant = XmlUtils.getVal(tpRec, "TO_PLANT");
    		// For Mail body content design
    		String deliveryComm = request.getParameter("FIELD.DELIVERYCOMM");
    		String hwModification = request.getParameter("FIELD.HW_MODIFICATION");
    		String expAvgYv = request.getParameter("FIELD.EXP_AVG_YV");
    		String newTesttime = request.getParameter("FIELD.NEW_TESTTIME");
    		String zeroYw = request.getParameter("FIELD.ZERO_YW");
    		String isTemp = request.getParameter("FIELD.IS_TEMP");
     		String validTill = request.getParameter("FIELD.VALID_TILL");
    		
    		if ((deliveryComm == null) || deliveryComm.equals("")) {
    			// do nothing
    		} else {
    			checkCount++;
    		}  // if ((deliveryComm ..))
    		
    		if ((hwModification == null) || hwModification.equals("")) {
    			// do nothing
    		} else {
    			checkCount++;
    		}  // if ((hwModification ..))
    		
    		if ((expAvgYv == null) || expAvgYv.equals("")) {
    			// do nothing
    		} else {
    			checkCount++;
    		}  // if ((expAvgYv ..))
    		
    		if ((zeroYw == null) || zeroYw.equals("")) {
    			// do nothing
    		} else {
    			checkCount++;
    		}  // if ((zeroYw ..
    		
    		if ((isTemp == null) || isTemp.equals("")) {
    			// do nothing
    		} else {
    			checkCount++;
    		}  // if ((isTemp ..))
    		
    		if ((validTill == null) || validTill.equals("")) {
    			// do nothing
    		} else {
    			checkCount++;
    		}  // if ((validTill ..))
    		
    		
    		if ((checkCount == 0) || (toAddress == null) || toAddress.equals("")) {
    			return;
    		}  // if ((checkCount ..
    		
    		Vector toAddresses = new Vector();
    		toAddresses.add(toAddress);
    		subjectBuffer.append("TPMSw RACE Notification TP  ");
    		subjectBuffer.append(jobname);
    		subjectBuffer.append(".");
    		subjectBuffer.append(releaseNb);
    		subjectBuffer.append(".");
    		subjectBuffer.append(revisionNb);
    		subjectBuffer.append(".");
    		subjectBuffer.append(versionNb);
    		String subject = subjectBuffer.toString();
    		contentBuffer.append("TPMSw RACE Notification \n");
    		contentBuffer.append("===================================\n");
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
    		
    		if (testNos == null) {
    			contentBuffer.append("\n NO TESTS Information \n");
    		} else {
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
	    				contentBuffer.append((index) + " TEST#:[");
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
	    				contentBuffer.append("]\nTest Commnent=[");
	    				contentBuffer.append(testComment);
	    				contentBuffer.append("]\t\n");
    				}
    			}  // for (int index ..
    		}  // if (testNos .. 
    		
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
    		debugLog("TPMultiActionServlet :: sendMail : subject : " + subject);
    		MailUtils.sendMail(mailHost, fromAddress, toAddresses, subject, content);
    	} catch (Exception ex) {
    		errorLog("TPMultiActionServlet :: sendMail : error while sending mail: " + ex.getMessage(), ex);
    		ex.printStackTrace();
    		try{
                EMailTrack.trackQuery(session.getId(), (String) session.getAttribute("unixUser"), content);
                }catch (Exception e) {
                    errorLog("TPMultiActionServlet :: sendMail : error while sending mail: " + ex.getMessage(), ex);
                    ex.printStackTrace();
                }
    		throw new TpmsException(ex.getMessage());
    	} finally {
			try	{
				if (subjectBuffer != null) {
					subjectBuffer = null;
				}
				if (contentBuffer != null) {
					contentBuffer = null;
				}
			} catch (Exception e){
				errorLog("TpMultiActionServlet :: sendMail : Fail to delocate buffer!");
			}
    	}
    }
}

