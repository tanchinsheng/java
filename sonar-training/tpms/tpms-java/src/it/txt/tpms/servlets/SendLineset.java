package it.txt.tpms.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.afs.utils.AfsWaitInformations;
import it.txt.general.installations.TpmsInstallationData;
import it.txt.general.installations.list.TpmsInstallationsList;
import it.txt.general.installations.manager.TpmsInstallationsManager;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;
import it.txt.tpms.backend.utils.BackEndCallResult;
import it.txt.tpms.backend.utils.BackEndInterfaceConstants;
import it.txt.tpms.backend.utils.BackEndInterfaceUtils;
import it.txt.tpms.lineset.Lineset;
import it.txt.tpms.lineset.managers.LinesetVobManager;
import it.txt.tpms.users.TpmsUser;
import it.txt.tpms.users.manager.TpmsUsersManager;
import tpms.TpmsException;
import tpms.utils.Vob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-ott-2006
 * Time: 11.28.08
 */
public class SendLineset extends AfsGeneralServlet {
    public static final String LS_NAME_PARAMETER = "LS_NAME";
    public static final String LS_OWNER_PARAMETER = "LS_OWNER";
    public static final String LS_BASE_DIR_PARAMETER = "LS_BASE_DIR";
    public static final String LS_TESTER_FAMILY_PARAMETER = "LS_TESTER_FAMILY";
    public static final String LS_BASELINE_PARAMETER = "LS_BASELINE";
    public static final String CC_MAIL_PARAMETER = "CC_MAIL_PARAMETER";
    public static final String DESTINATION_PLANT_ID_PARAMETER = "destinationPlantId";
    public static final String DESTINATION_USER_LOGIN_PARAMENTER = "destinationUserId";

    public static final String SEND_LINESET_ACTION = "sendLineset";
    public static final String INITIALIZE_SEND_LINESET_ACTION = "init";

    public static final String PLANTS_INFORMATION_DATA_ATTRIBUTE_NAME = "plantsData";

    public static final String DESTINATION_PLANT_DATA_ATTRIBUTE_NAME = "destinationPlantData";
    public static final String DESTINATION_USER_DATA_ATTRIBUTE_NAME = "destinationUserData";
    public static final String LINESET_ATTRIBUTE_NAME = "linesetData";

    public static final String SEND_LINESET_RESULT_ATTRIBUTE_NAME = "sendResultData";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //(String linesetName, Vob vob, String owner, String unixBaseDirectory, String testerFamily, String baseline)
        String action = request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);
        HttpSession session = request.getSession();
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        String commonErrorMessage = "SendLineset :: doPost";
        String nextPage = null;

       if (currentUser == null) {
            TpmsException e = new TpmsException("You are not authorized to access this functionality", "Group Lineset list", "Current user is not authenticated: You are not authorized to access this functionality");
            manageError(e, request, response);
        }

        if (INITIALIZE_SEND_LINESET_ACTION.equals(action)) {
            TpmsInstallationsList tpmsInstallationList = null;
            try {
                tpmsInstallationList = TpmsInstallationsManager.getTpmsInstallationsInfoWithUsersData();
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            session.setAttribute(PLANTS_INFORMATION_DATA_ATTRIBUTE_NAME, tpmsInstallationList);
            nextPage = this.getServletConfig().getInitParameter("retrieveData");
            Vob currentVob = (Vob) session.getAttribute("vobObject");
            String name = request.getParameter(LS_NAME_PARAMETER);
            String owner = request.getParameter(LS_OWNER_PARAMETER);
            String baseDirectory = request.getParameter(LS_BASE_DIR_PARAMETER);
            String testerFamily = request.getParameter(LS_TESTER_FAMILY_PARAMETER);
            String baseLine = request.getParameter(LS_BASELINE_PARAMETER);

            TpmsUser tpmsUserOwner = TpmsUsersManager.getLocalTpmsUserByTpmsLogin(owner);

            Lineset lineset = new Lineset(name, currentVob, tpmsUserOwner, baseDirectory, testerFamily, baseLine, tpmsConfiguration.getLocalPlant() );
            session.setAttribute(LINESET_ATTRIBUTE_NAME, lineset);
        } else if (action.equals(SEND_LINESET_ACTION)) {
            String sessionId = session.getId();
            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
                //in this case we have to start the request to the BE.
                //produce the request ID that will identify this user request
                String ccEmailAddresses = request.getParameter(CC_MAIL_PARAMETER);
                Lineset lineset = (Lineset) session.getAttribute(LINESET_ATTRIBUTE_NAME);


                String destinationPlantId = request.getParameter(DESTINATION_PLANT_ID_PARAMETER);
                String destinationUserLogin = request.getParameter(DESTINATION_USER_LOGIN_PARAMENTER);

                TpmsInstallationsList tpmsInstallationList = (TpmsInstallationsList) session.getAttribute(PLANTS_INFORMATION_DATA_ATTRIBUTE_NAME);
                TpmsInstallationData destinationPlantData = tpmsInstallationList.getElement(destinationPlantId);

                TpmsUser destinationUser = destinationPlantData.getUsersList().getElementByTpmsLogin(destinationUserLogin);
                session.removeAttribute(PLANTS_INFORMATION_DATA_ATTRIBUTE_NAME);

                String requestId = BackEndInterfaceUtils.getRequestId(sessionId);

                debugLog(commonErrorMessage + ": requestId = " + requestId + " sessionId = " + sessionId );
                try {
                    LinesetVobManager.sendLineset(lineset, destinationPlantData, destinationUser, ccEmailAddresses, requestId, sessionId );
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
                //populate needed data in order to display the wait page to the user.
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), SEND_LINESET_ACTION, requestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);

            } else {

                //in this case we have to check if the BE call is terminated
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                //check and track the result...
                int checkResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
                BackEndCallResult beCallResult = new BackEndCallResult();
                Lineset lineset = (Lineset) session.getAttribute(LINESET_ATTRIBUTE_NAME);
                try {
                    beCallResult = LinesetVobManager.checkSendLinesetResult(requestId, sessionId, lineset);
                } catch (TpmsException e) {
                    errorLog(commonErrorMessage + " TpmsException : " + e.getMessage(), e);
                    manageError(e, request, response);
                }
                debugLog(commonErrorMessage + " checkResult = " + checkResult);
                checkResult = beCallResult.getCallResult();
                if (checkResult == BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT) {
                    //the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), SEND_LINESET_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found...let's clean all unneeded session data...
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                    request.removeAttribute(LINESET_ATTRIBUTE_NAME);
                    //debugLog(commonErrorMessage + " OK finito... redirigo verso la pg che presenta la lista dei ls, list size = ");
                }
            }

            nextPage = this.getServletConfig().getInitParameter("outPage");

        }
        manageRedirect(nextPage, request, response);
    }


}
