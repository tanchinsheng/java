package it.txt.tpms.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.afs.utils.AfsWaitInformations;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;
import it.txt.tpms.backend.utils.BackEndCallResult;
import it.txt.tpms.backend.utils.BackEndInterfaceConstants;
import it.txt.tpms.backend.utils.BackEndInterfaceUtils;
import it.txt.tpms.lineset.managers.LinesetVobManager;
import it.txt.tpms.lineset.packages.list.ReceivedLinesetPackagesList;
import it.txt.tpms.lineset.packages.ReceivedLinesetPackage;
import it.txt.tpms.lineset.packages.manager.LinesetPackageManager;
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
 * Date: 19-ott-2006
 * Time: 17.06.12
 */
public class LoadReceivedLinesetPackage extends AfsGeneralServlet {

    public static final String LOAD_LINESET_ACTION = "sendLineset";
    public static final String INITIALIZE_RECEIVED_LINESETS_ACTION = "init";

    public static final String LINESET_PACKAGES_LIST_ATTRIBUTE_NAME = "linesetPackagesList";
    public static final String LINESET_PACKAGE_TO_BE_LOADED = "linesetPacakgeToBeLoaded";
    public static final String LOAD_LINESET_PACKAGE_RESULT_ATTRIBUTE_NAME = "loadLinesetPackage";

    public static final String LINESET_PACKAGE_ID_PARAMETER_NAME = "linsetPackageId";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);
        HttpSession session = request.getSession();
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        String commonErrorMessage = "LoadReceivedLinesetPackage :: doPost";
        String nextPage = null;

        if (currentUser == null) {
            TpmsException e = new TpmsException("You are not authorized to access this functionality", commonErrorMessage, "Current user is not authenticated: You are not authorized to access this functionality");
            manageError(e, request, response);
        }

        Vob selectedVob = (Vob) session.getAttribute("vobObject");
        if (selectedVob == null) {
            manageRedirect(this.getServletConfig().getInitParameter("selectVob"), request, response);
            return;
        }
        TpmsUser currentTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin(currentUser.getTpmsLogin());

        if (action.equals(INITIALIZE_RECEIVED_LINESETS_ACTION)) {
            //let's initialize lists and so on for the current user.

            ReceivedLinesetPackagesList linesetPackagesList = null;
            try {
                linesetPackagesList = LinesetPackageManager.getReceivedLinesetPackagesList(currentTpmsUser);
                debugLog(commonErrorMessage + ": linesetPackagesList size " + (linesetPackagesList==null? -7 : linesetPackagesList.size()));

            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            session.setAttribute(LINESET_PACKAGES_LIST_ATTRIBUTE_NAME, linesetPackagesList);
            nextPage = this.getServletConfig().getInitParameter("receivedLinesetList");
        } else if (action.equals(LOAD_LINESET_ACTION)) {
            String sessionId = session.getId();



            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
                //in this case we have to start the request to the BE.
                //so let's identify the package...
                String linesetPackageId = request.getParameter(LINESET_PACKAGE_ID_PARAMETER_NAME);
                ReceivedLinesetPackagesList linesetPackagesList = (ReceivedLinesetPackagesList) session.getAttribute(LINESET_PACKAGES_LIST_ATTRIBUTE_NAME);
                ReceivedLinesetPackage linesetPackage = linesetPackagesList.getElement(linesetPackageId);
                
                if (linesetPackage == null) {
                    TpmsException e = new TpmsException("Unable to find lineset package " + linesetPackageId + " inside list", commonErrorMessage, "Unable to find package");
                    manageError(e, request, response);
                }
                String requestId = BackEndInterfaceUtils.getRequestId(sessionId);
                //call backend action
                try {
                    LinesetVobManager.loadLineset(selectedVob, linesetPackage, currentTpmsUser, requestId, sessionId);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
                //populate needed data in order to display the wait page to the user.
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), LOAD_LINESET_ACTION, requestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                session.setAttribute(LINESET_PACKAGE_TO_BE_LOADED, linesetPackage);
                session.removeAttribute(LINESET_PACKAGES_LIST_ATTRIBUTE_NAME);

            } else {
                //in this case we have to check if the BE call is terminated
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                //check and track the result...
                int checkResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
                BackEndCallResult beCallResult = new BackEndCallResult();
                ReceivedLinesetPackage linesetPackage = (ReceivedLinesetPackage) session.getAttribute(LINESET_PACKAGE_TO_BE_LOADED);
                try {
                    beCallResult = LinesetVobManager.checkLoadLinesetPackageResult(requestId, sessionId, currentTpmsUser, selectedVob, linesetPackage);
                } catch (TpmsException e) {
                    errorLog(commonErrorMessage + " TpmsException : " + e.getMessage(), e);
                    manageError(e, request, response);
                }
                debugLog(commonErrorMessage + " checkResult = " + checkResult);
                checkResult = beCallResult.getCallResult();
                if (checkResult == BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), LOAD_LINESET_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found...add result data to session ...
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                    session.setAttribute(LOAD_LINESET_PACKAGE_RESULT_ATTRIBUTE_NAME, beCallResult);
                    session.removeAttribute(LINESET_PACKAGE_TO_BE_LOADED);
                }
            }
            nextPage = this.getServletConfig().getInitParameter("loadLinesetResult");
        }


        manageRedirect(nextPage, request, response);
    }
}
