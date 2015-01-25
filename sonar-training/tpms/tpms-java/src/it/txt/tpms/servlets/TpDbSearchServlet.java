package it.txt.tpms.servlets;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;
import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.tpms.reports.TpReportTypes;
import it.txt.tpms.reports.request.ReportRequestObject;
import it.txt.tpms.reports.request.ReportRequestObjectsList;
import it.txt.tpms.reports.request.utils.ReportRequestUtils;
import it.txt.tpms.reports.request.managers.ReportsRequestManager;
import it.txt.tpms.users.TpmsUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;

import tpms.TpmsException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 6-mar-2006
 * Time: 12.27.47
 */
public class TpDbSearchServlet extends AfsGeneralServlet {

    public static final String REQUEST_REPORT_PRODUCTION = "performSearch";
    public static final String DELETE_REPORT_PRODUCTION = "deleteReportRequest";
    public static final String REPORT_REQUEST_LIST_REQUEST_ATTRIBUTE_NAME = "reportsRequestList";
    public static final String REPORT_REQUEST_ID_FIELD_NAME = "reportRequestId";




    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);
        String nextPage = getServletConfig().getInitParameter("searchPage");
        HttpSession session = request.getSession();
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        debugLog("TpDbSearchServlet :: doPost : action = " + action);
        if (GeneralStringUtils.isEmptyString(action)) {
            //means that the search should be initialized...
            debugLog("TpDbSearchServlet :: doPost : action is null or empty");

        } else if (action.equals(REQUEST_REPORT_PRODUCTION)) {
            String reportType = request.getParameter(TpReportTypes.REPORT_TYPE_FIELD_NAME);
            //the user wants to perform the tp search...
            debugLog("TpDbSearchServlet :: doPost : action = REQUEST_REPORT_PRODUCTION");
            if (reportType.equals(TpReportTypes.TP_LAST_STATUS)){
                String reportCriteria = ReportRequestUtils.getReportRequestCriteria(request);
                ReportRequestObject reportRequest;
                try {
                    reportRequest = ReportsRequestManager.newReportRequest(TpReportTypes.TP_LAST_STATUS, currentUser.getTpmsLogin(), reportCriteria, session.getId());
                } catch (TpmsException e) {
                    manageError(e, request, response);
                    return;
                }
                if (reportRequest != null) {

                }
            }
            nextPage = getServletConfig().getInitParameter("resultPage");
        } else if (action.equals(DELETE_REPORT_PRODUCTION)) {
            String reportRequestId = request.getParameter(REPORT_REQUEST_ID_FIELD_NAME);
            try {
                ReportsRequestManager.deleteReportRequest(reportRequestId, currentUser.getTpmsLogin(),session.getId());
            } catch (TpmsException e) {
                manageError(e, request, response);
                return;
            }

        }

        ReportRequestObjectsList reportRequestsList;
        try {

            reportRequestsList = ReportsRequestManager.getUserReportRequests(currentUser.getTpmsLogin());
        } catch (TpmsException e) {
            manageError(e, request, response);
            return;
        }
        
        request.setAttribute(REPORT_REQUEST_LIST_REQUEST_ATTRIBUTE_NAME, reportRequestsList);
        manageRedirect(nextPage, request, response);
    }



}
