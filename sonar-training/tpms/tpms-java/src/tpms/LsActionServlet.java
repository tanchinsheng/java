/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 4, 2003
 * Time: 1:59:37 PM
 * To change this template use Options | File Templates.
 */
package tpms;

import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import tol.oneConnDbWrtr;
import tpms.action.ls.LsMoveAction;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class LsActionServlet extends TpActionServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String commonMessage = "LsActionServlet :: doPost";
        String REQID = (!_DEBUG ? session.getId() + "_" + Long.toString(System.currentTimeMillis()) : action);
        String owner = (String) session.getAttribute("unixUser");
        String xmlFileName = (request.getParameterValues("xmlFileName") != null ? request.getParameter("xmlFileName") : (request.getParameterValues("repFileName") != null ? request.getParameter("repFileName") : null));
        // RepFileName to create from CC Command
        String repFileNameCc;
        // RepFileName for Out Page
        String repFileNameOut;
        repFileNameOut = (!_DEBUG ? _webAppDir + "/" + "images" + "/" + REQID + "_rep.xml" : _webAppDir + "/" + "images" + "/" + action + "_rep.xml");
        /* DVL Execution
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
        String nextPage = getServletConfig().getInitParameter("nextPage"); //not used actually
        String outPage = action.toLowerCase() + "_out.jsp";
        String actionTxt = getServletConfig().getInitParameter("actionTxt");
        LsAction lsAction = null;
        /**
         * Manage user action...
         */
        if (action.equals("ls_delete")) lsAction = new LsDeleteAction(action, log);
        if (action.equals("ls_submit_preview")) lsAction = new LsSubmitPreviewAction(action, log);
        if (action.equals("ls_align_preview")) lsAction = new LsAlignPreviewAction(action, log);
        if (action.equals("ls_align")) lsAction = new LsAlignAction(action, log);
        if (action.equals("ls_download")) lsAction = new LsDownloadAction(action, log);
        if (action.equals("ls_download_file")) lsAction = new LsDownloadFileAction(action, log);
        if (action.equals("ls_download_file_preview")) lsAction = new LsDownloadFilePreviewAction(action, log);
        if (action.equals("ls_align_file_diff")) lsAction = new LsAlignFileDiffAction(action, log);

        if (action.equals("ls_move")) {
            lsAction = new LsMoveAction(action, log);
        }

        if (lsAction == null) {
            lsAction = new LsAction(action, log);
        }
        lsAction.setSessionId(session.getId());
        try {
            String user = (String) session.getAttribute("user");
            String userRole = (String) session.getAttribute("role");
            String unixUser = (String) session.getAttribute("unixUser");
            Element userData = CtrlServlet.getUserData(user);
            Element adminData = CtrlServlet.getAdminData();
            String defltWorkDir = NavDirServlet.getDefaultWorkDirPath(userData);
            String fromEmail = XmlUtils.getVal(userData, "EMAIL");
            String adminLogin = XmlUtils.getVal(adminData, "UNIX_USER");

            /**
             * TP DATA FETCH FROM THE META DATA RECORDSET (XML)
             */
            Properties params = new Properties();
            Element lsRec;
            lsRec = LsDetailViewServlet.getLsData(request);

            if (request.getParameterValues("submitAction") == null) {
                nextPage = action.toLowerCase() + "_form.jsp";
                request.setAttribute("workDir", defltWorkDir);
                request.setAttribute("fromEmail", fromEmail);
                request.setAttribute("unixUser", unixUser);
            } else {
                lsAction.setReqID(REQID);
                lsAction.setRepFileName(repFileNameCc);
                lsAction.setUserName(unixUser);
                lsAction.setUserRole(userRole);
                lsAction.setLsRec(lsRec);
                lsAction.setParams(params);
                lsAction.setLocalPlant(_localPlant);
                lsAction.setWebAppDir(_webAppDir);


                if (lsAction.hasDbAction()) {
                    lsAction.setDbWrtr((oneConnDbWrtr) this.getServletContext().getAttribute("dbWriter"));
                }
                debugLog("LsActionServlet :: doPost :LS-ACTION-START-" + REQID + ">");
                debugLog("LsActionServlet :: doPost :ACTION-TYPE-" + REQID + ">" + action);
                debugLog("LsActionServlet :: doPost :USER-" + REQID + ">" + unixUser);

                debugLog("LsActionServlet :: doPost :LS-" + REQID + ">" + XmlUtils.getVal(lsRec, "LS_LABEL"));
                String workDir = request.getParameter("workDir");
                debugLog("LsActionServlet :: doPost :WORKDIR-" + REQID + ">" + workDir);
                debugLog("LsActionServlet :: doPost :fromemail>" + fromEmail);

                setMultiLineProperty(params, "comment", (request.getParameterValues("comment") != null ? request.getParameterValues("comment")[0] : ""));

                for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                    String parnam = (String) e.nextElement();

                    if (parnam.startsWith("lsFile")) {
                        if (parnam.equals("lsFile")) {
                            params.setProperty("ls_file", request.getParameterValues(parnam)[0]);
                            request.setAttribute("ls_file", request.getParameterValues(parnam)[0]);
                        } else {
                            String index = parnam.substring(6);
                            if (request.getParameterValues("chk" + index) != null) {
                                params.setProperty("ls_file_" + index, request.getParameterValues(parnam)[0]);
                            }
                        }
                    }
                }

                if (session.getAttribute("exception" + "_" + REQID) != null) session.removeAttribute("exception" + "_" + REQID);
                session.setAttribute("startBool" + "_" + REQID, Boolean.TRUE);
                debugLog("LsActionServlet :: doPost :set_repFileName>" + repFileNameCc);
                //avvio la chiamata a cc e attendo l'esito(i.e. andata bene/male)
                VobActionDaemon.doAction(this.log, _DEBUG, _dvlBool, _dvlUnixHost, _dvlCcInOutDir, _dvlUnixAppDir, _webAppDir, _execMode, backgrndBool, lsAction, actionTxt, session, (!_DEBUG ? REQID : action), repFileNameCc, _ccScriptsDir, _ccInOutDir, lsAction.getCcActionUnixLogin(owner, adminLogin), _timeOut);

                //recupero delle info frelative ad eventuali dati di ritorno da cc
                request.setAttribute("xmlFileName", xmlFileName);
                request.setAttribute("outPage", outPage);
                request.setAttribute("actionTxt", actionTxt);
                request.setAttribute("refreshTime", getServletConfig().getInitParameter("refreshTime"));
                request.setAttribute("reqId", REQID);
                request.setAttribute("repFileName", repFileNameOut);
                request.setAttribute("fromEmail", fromEmail);
            }
            CtrlServlet.setAttributes(request);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);
        } catch (Exception e) {
            session.setAttribute("startBool" + "_" + REQID, Boolean.FALSE);
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }
    }

}
