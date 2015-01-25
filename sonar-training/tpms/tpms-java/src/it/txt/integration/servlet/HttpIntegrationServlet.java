package it.txt.integration.servlet;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.integration.managers.PlantInformationsManager;
import it.txt.integration.managers.TpQueriesManager;
import it.txt.integration.managers.TpActionsManager;
import it.txt.integration.utils.RequestAttributesAndFieldsNames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 29-giu-2006
 * Time: 11.20.07
 * this servrvlet works as request dispatcher for TPMS/W external tool integration
 */
public class HttpIntegrationServlet extends AfsGeneralServlet {


    public static final String PLANT_INFORMATION_ACTION = "get_info";
    public static final String TP_QUERY_ACTION = "tp_query";
    public static final String TP_HISTORY_QUERY_ACTION = "tp_history";
    public static final String TP_DEPLOY_ACTION = "tp_delivery";


    /**
     * This method tracks to tpms log each TPMS HTTP call that arrives to this servlet
     *
     * @param request
     * @param method
     */
    private void logHttpCalls(HttpServletRequest request, String method) {
        StringBuffer parameterNameValuesList = new StringBuffer();
        Enumeration parametersNames = request.getParameterNames();
        String parameterName;
        while (parametersNames.hasMoreElements()) {
            parameterName = (String) parametersNames.nextElement();
            if (!GeneralStringUtils.isEmptyString(parameterName)) {
                parameterNameValuesList.append(parameterName).append("=").append(request.getParameter(parameterName)).append("; ");
            }
        }

        Logger log = Logger.getLogger(this.getClass());
        log.fatal("HttpIntegrationServlet : " + request.getRemoteAddr() + " - " + request.getRemoteHost() + " : " +  request.getSession().getId() + " : mehod " + method + " parameters = " + parameterNameValuesList.toString());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logHttpCalls(request, "POST");
        debugLog("HttpIntegrationServlet : tpmsConfiguration.isRemoteInterfaceEnabled() = " + tpmsConfiguration.isRemoteInterfaceEnabled());
        if (tpmsConfiguration.isRemoteInterfaceEnabled()) {
            String action = request.getParameter(RequestAttributesAndFieldsNames.ACTION_FIELD_NAME);
            debugLog("HttpIntegrationServlet : action = " + action);

            String nextPage = this.getServletConfig().getInitParameter("outputPage");
            HttpSession session = request.getSession();
            if (GeneralStringUtils.isEmptyTrimmedString(action)) {
                nextPage = this.getServletConfig().getInitParameter("noActionErrorPage");
                manageRedirect(nextPage, request, response);
                return;
            }
            if (action.equals(PLANT_INFORMATION_ACTION)) {
                String plantId = request.getParameter(RequestAttributesAndFieldsNames.TO_PLANT_FIELD);
                request.setAttribute(RequestAttributesAndFieldsNames.ACTION_RESULT_ATTRIBUTE, PlantInformationsManager.getPlantInformations(plantId));
            } else if (action.equals(TP_QUERY_ACTION)) {
                String plantId = request.getParameter(RequestAttributesAndFieldsNames.TO_PLANT_FIELD);
                String jobName = request.getParameter(RequestAttributesAndFieldsNames.JOB_NAME_FIELD);
                String jobRelease = request.getParameter(RequestAttributesAndFieldsNames.JOB_RELEASE_FIELD);
                String ownerLoginFilter =  request.getParameter(RequestAttributesAndFieldsNames.OWNER_FILTER_FIELD);
                String statusFilter = request.getParameter(RequestAttributesAndFieldsNames.STATUS_FIELD);
                String status2ndFilter = request.getParameter(RequestAttributesAndFieldsNames.STATUS_2ND_FIELD);
                StringBuffer queryResult = TpQueriesManager.retrieveTps(plantId, jobName, jobRelease, ownerLoginFilter, statusFilter, status2ndFilter, session.getId());
                request.setAttribute(RequestAttributesAndFieldsNames.ACTION_RESULT_ATTRIBUTE, queryResult);
            } else if (action.equals(TP_HISTORY_QUERY_ACTION)) {

                String plantId = request.getParameter(RequestAttributesAndFieldsNames.TO_PLANT_FIELD);
                String jobName = request.getParameter(RequestAttributesAndFieldsNames.JOB_NAME_FIELD);
                String jobRelease = request.getParameter(RequestAttributesAndFieldsNames.JOB_RELEASE_FIELD);
                String ownerLoginFilter =  request.getParameter(RequestAttributesAndFieldsNames.OWNER_FILTER_FIELD);
                StringBuffer queryResult = TpQueriesManager.retrieveTpHistory(plantId, jobName, jobRelease, ownerLoginFilter, session.getId());
                request.setAttribute(RequestAttributesAndFieldsNames.ACTION_RESULT_ATTRIBUTE, queryResult);
            } else if (action.equals(TP_DEPLOY_ACTION)) {
                //debugLog("HttpIntegrationServlet : retrieve request parameters...");
                String plantId = request.getParameter(RequestAttributesAndFieldsNames.TO_PLANT_FIELD);
                String jobName = request.getParameter(RequestAttributesAndFieldsNames.JOB_NAME_FIELD);
                String jobRelease = request.getParameter(RequestAttributesAndFieldsNames.JOB_RELEASE_FIELD);
                String jobRevision = request.getParameter(RequestAttributesAndFieldsNames.JOB_REVISION_FIELD);
                String owner = request.getParameter(RequestAttributesAndFieldsNames.OWNER_FIELD);
                String facility = request.getParameter(RequestAttributesAndFieldsNames.FACILITY_FIELD);
                String packagePath = request.getParameter(RequestAttributesAndFieldsNames.PACKAGE_PATH_FIELD);
                String testerInfo = request.getParameter(RequestAttributesAndFieldsNames.TESTER_INFO_FIELD);
                String fromEMail = request.getParameter(RequestAttributesAndFieldsNames.FROM_MAIL_FIELD);
                String validLogin = request.getParameter(RequestAttributesAndFieldsNames.VALID_LOGIN_FIELD);
                String prodLogin = request.getParameter(RequestAttributesAndFieldsNames.PROD_LOGIN_FIELD);
                String toMail = request.getParameter(RequestAttributesAndFieldsNames.TO_MAIL_FIELD);
                String ccMail = request.getParameter(RequestAttributesAndFieldsNames.CC_MAIL_FIELD);
                String line = request.getParameter(RequestAttributesAndFieldsNames.LINE_FIELD);

                String tmpEmergency = request.getParameter(RequestAttributesAndFieldsNames.EMERGENCY_FIELD);
                boolean emergency = (!GeneralStringUtils.isEmptyString(tmpEmergency) && tmpEmergency.equalsIgnoreCase("true"));
                //debugLog("HttpIntegrationServlet : parameters retrivial terminated, start action call");

                StringBuffer tpDeployResult = TpActionsManager.deliverTps(plantId, jobName, jobRelease, jobRevision, line, facility, packagePath, testerInfo, tpmsConfiguration.getLocalPlant(), fromEMail, validLogin, prodLogin, toMail, ccMail, owner, session.getId(), emergency);
                //debugLog("HttpIntegrationServlet : Action call teminated, prepare result content");
                request.setAttribute(RequestAttributesAndFieldsNames.ACTION_RESULT_ATTRIBUTE, tpDeployResult);
                //debugLog("HttpIntegrationServlet : Result content ready");
            }
            debugLog("HttpIntegrationServlet : Send result to client");
            manageRedirect(nextPage, request, response);
        } else {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "TPMS/W remote call not allowed on this installation");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logHttpCalls(request, "GET");
        doPost(request, response);
    }

}
