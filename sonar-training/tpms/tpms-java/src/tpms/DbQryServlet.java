package tpms;

import org.w3c.dom.Element;
import tol.*;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.XmlUtils;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 30, 2003
 * Time: 11:34:19 AM
 * FP rev5 - aggiunto il filtro sulle DB query per USER 'Engin' or 'Controller'
 * FP rev5 - aggiunta la gestione della query 'db_tp_search_base'
 */


public class DbQryServlet extends AfsGeneralServlet {


    private String _localPlant;


    private LogWriter log = null;


    public void init() throws ServletException {
        super.init();
        _localPlant = getServletContext().getInitParameter("localPlant");
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String qryType = request.getParameter("qryType");
        HttpSession session = request.getSession();
        
        String REQID = session.getId() + "_" + Long.toString(System.currentTimeMillis());
        String repFileName = tpmsConfiguration.getWebAppDir() + "/" + "images" + "/" + REQID + "_rep.xml";

        String nextPage = getServletConfig().getInitParameter("nextPage");
        String outPage = getServletConfig().getInitParameter("outPage");
        String actionTxt = getServletConfig().getInitParameter("actionTxt");

        if (session.getAttribute("exception") != null) session.removeAttribute("exception");

        session.setAttribute("startBool" + "_" + REQID, Boolean.TRUE);
        //start-  FP rev5
        String user = (String) session.getAttribute("user");
        Element userData = null;
        try {
            userData = CtrlServlet.getUserData(user);
        } catch (Exception e) {
            errorLog("DbQryServlet :: doPost : unable to get user data user = " + user, e);
            manageError(e, request, response);
        }
        String userRole = XmlUtils.getVal(userData, "ROLE");
        String userDiv = XmlUtils.getVal(userData, "DIVISION");
        //stop
        reportSel repsel = (reportSel) session.getAttribute("repsel");
        repSect repObj = getReportObject(repsel, qryType);
        try {
            repObj.setFilter(request);
        } catch (Exception e) {
            errorLog("DbQryServlet :: doPost : repObj.setFilter", e);
            manageError(e, request, response);
        }
        String workMode = (String) session.getAttribute("workMode");
         this.setFilter(repsel, repObj, workMode, qryType);

        String addWhrStr = this.getAddWhereClause(session, repsel, workMode, qryType, userRole, userDiv);

        doQry(session, REQID, repFileName, repObj, 0, actionTxt, addWhrStr);

        request.setAttribute("outPage", outPage);
        request.setAttribute("actionTxt", actionTxt);
        request.setAttribute("refreshTime", getServletConfig().getInitParameter("refreshTime"));
        request.setAttribute("reqId", REQID);
        request.setAttribute("repFileName", repFileName);
        request.setAttribute("repTitle", getReportTitle(qryType));
        request.setAttribute("qryType", qryType);

        for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
            String attrnam = (String) e.nextElement();
            if (attrnam.startsWith("field.")) {
                request.setAttribute(attrnam, request.getParameter(attrnam));
            }
        }
        debugLog("DbQryServlet :: doPost : nextPage = " + nextPage + " query type = " + qryType);
        manageRedirect(nextPage, request, response);


    }

    public void doQry(HttpSession session, String REQID, String repFileName, repSect repObj, long timeOut, String actionTxt, String addWhrStr) {
    	DbActionDaemon daemon = new DbActionDaemon(session, REQID, this.log, repObj, actionTxt, addWhrStr, tpmsConfiguration.getDebug(), repFileName, 0);
        Thread daemonThread = new Thread(daemon);
        daemonThread.start();
    }

    public repSect getReportObject(reportSel repsel, String qryType) {
        if (qryType.equals("db_tp_to_process")) return repsel.getReport("tp_report");
        if (qryType.equals("db_tp_in_progress")) return repsel.getReport("tp_report");
        if (qryType.equals("db_tp_in_production")) return repsel.getReport("tp_in_production_report");
        if (qryType.equals("db_tp_search")) return repsel.getReport("tp_report");
        if (qryType.equals("db_tp_search_base")) return repsel.getReport("tp_report");
        if (qryType.equals("db_prod_lib_hist")) return repsel.getReport("tp_report");
        if (qryType.equals("db_tp_history")) return repsel.getReport("tp_hist_report");
        if (qryType.equals("db_ls_search")) return repsel.getReport("ls_report");
        if (qryType.equals("db_tp_offLine")) return repsel.getReport("tp_report");
        return null;
    }

    static public String getReportTitle(String qryType) {
        if (qryType.equals("db_tp_to_process")) return "TP DISTRIBUTED REPORT (DB)";
        if (qryType.equals("db_tp_in_progress")) return "TP IN PROGRESS REPORT (DB)";
        if (qryType.equals("db_tp_in_production")) return "TP IN PRODUCTION REPORT (DB)";
        if (qryType.equals("db_tp_search")) return "TP SEARCH RESULT (DB)";
        if (qryType.equals("db_tp_search_base")) return "TP SEARCH Base RESULT (DB)";
        if (qryType.equals("db_tp_history")) return "SINGLE TP HISTORY REPORT (DB)";
        if (qryType.equals("db_prod_lib_hist")) return "TP HISTORY REPORT (DB)";
        if (qryType.equals("db_ls_search")) return "LINESET SEARCH RESULT (DB)";
        if (qryType.equals("db_tp_offLine")) return "TPs OFF-LINE RESULT (DB)";
        return "";
    }

    void setFilter(reportSel repsel, repSect repObj, String workMode, String qryType) {

    	 slctLst statusLst = repsel.get("STATUS");

        Vector statusVals = new Vector();
        if (qryType.equals("db_tp_to_process")) {
            statusLst.setVal("Distributed");
        }
        if (qryType.equals("db_tp_in_progress")) {
            statusVals.addElement("In_Validation");
            statusVals.addElement("Ready_to_production");
            statusLst.setVal(statusVals);
        }
        if (qryType.equals("db_tp_in_production")) {
            statusLst.setVal("In_Production");
        }
        slctLst jobRevLst = repsel.get("JOB_REV");
        if (!jobRevLst.isBlank()) jobRevLst.setVal(jobRevLst.getVal().length() == 1 ? "0" + jobRevLst.getVal() : jobRevLst.getVal());
    }

    private String getAddWhereClause(HttpSession session, reportSel repsel, String workMode, String qryType, String userRole, String userDiv) {
        String whereString;
        String user = (String) session.getAttribute("user");
        String vuserFld = repsel.get("VALID_LOGIN").getTableName() + "." + repsel.get("VALID_LOGIN").getFieldName();
        String puserFld = repsel.get("PROD_LOGIN").getTableName() + "." + repsel.get("PROD_LOGIN").getFieldName();
        String statusFld = repsel.get("STATUS").getTableName() + "." + repsel.get("STATUS").getFieldName();
        String ownerFld = repsel.get("OWNER").getTableName() + "." + repsel.get("OWNER").getFieldName();
        String divisFld = repsel.get("DIVISION").getTableName() + "." + repsel.get("DIVISION").getFieldName();

        if (((workMode == null) || (workMode.equals("SENDWORK")) || (workMode.equals("RECWORK"))) && (!qryType.equals("db_tp_search"))&& (!qryType.equals("db_prod_lib_hist"))) {
            whereString = "((" +
                    " (" + statusFld + " IN('Distributed','In_Validation','Ready_to_production') AND " +
                    "  (" + vuserFld + " = '" + user + "' OR " + vuserFld + " IS NULL)" +
                    " )" +
                    " OR " +
                    " (" + statusFld + " IN('Ready_to_production','In_Production') AND " +
                    "  (" + puserFld + " = '" + user + "')" +
                    " )" +
                    "))";
            debugLog("WHR>" + whereString);
            return whereString;
        }


        /*START - FP-rev5 */
        if (userRole.equals("ENGINEER") && ((!qryType.equals("db_tp_offLine")) && (!qryType.equals("db_ls_search")))) {
            whereString = "((" + ownerFld + " = '" + user +
                    "' AND " + divisFld + " = '" + userDiv +
                    "' AND TP_PLANT.INSTALLATION_ID" + "= '" + _localPlant + "')" +
                    " OR " +
                    " ( TP_PLANT.TO_PLANT" + "='" + _localPlant +
                    "' AND (" + vuserFld + "='" + user +
                    "' OR " + puserFld + "='" + user +
                    "')" +
                    "))";
            debugLog("WHR FILTER ENGIN USER>" + whereString);
            return whereString;
        }

        if (userRole.equals("ENGINEER") && qryType.equals("db_tp_offLine")) {
            whereString = "((" + puserFld + "= '" + user +
                    "' OR " + vuserFld + "='" + user +
                    "') AND TP_PLANT.TO_PLANT" + "='" + _localPlant +
                    "' AND TP_PLANT.STATUS IN('Obsolete', 'In_Production') AND TP_PLANT.VOB_STATUS='OffLine')";
            debugLog("WHR FILTER Dearchive ENGIN USER>" + whereString);
            return whereString;
        }

        if (userRole.equals("CONTROLLER") && (!qryType.equals("db_ls_search"))) {
            whereString = "(" + divisFld + " = '" + userDiv +
                    "' AND TP_PLANT.INSTALLATION_ID" + "= '" + _localPlant + "')";
            debugLog("WHR FILTER CONTROLLER USER>" + whereString);
            return whereString;
        }


        if (userRole.equals("ENGINEER") && qryType.equals("db_ls_search")) {
            whereString = "((" + "LINESET.INSTALLATION_ID" + " = '" + _localPlant +
                    "' AND " + "LINESET.OWNER" + " = '" + user + "'))";
            debugLog("WHR FILTER ENGIN USER FOR LINESET>" + whereString);
            return whereString;
        }

        if (userRole.equals("CONTROLLER") && qryType.equals("db_ls_search")) {
            whereString = "((" + "LINESET.INSTALLATION_ID" + " = '" + _localPlant + "') AND (LINESET.OWNER IN (select UNIX_LOGIN from USERS where DIVISION='" + userDiv + "')))";
            debugLog("WHR FILTER CONTROLLER USER FOR LINESET>" + whereString);
            return whereString;
        }
        /*STOP - FP-rev5 */

        return null;
    }

}

