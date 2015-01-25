package it.txt.tpms.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.afs.utils.AfsWaitInformations;
import it.txt.general.SessionObjects;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.backend.utils.BackEndCallResult;
import it.txt.tpms.backend.utils.BackEndInterfaceConstants;
import it.txt.tpms.backend.utils.BackEndInterfaceUtils;
import it.txt.tpms.lineset.Lineset;
import it.txt.tpms.lineset.list.LinesetList;
import it.txt.tpms.lineset.managers.LinesetVobManager;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.VobManager;
import tpms.utils.Vob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-ott-2006
 * Time: 9.16.07
 */
public class GetLinesetOwnership extends AfsGeneralServlet {
    public static final String GET_LINESET_OWNERSHIP_ACTION = "getLinesetOwnerShip";
    public static final String LINESET_ID_PARAMETER = "linesetId";
    public static final String GET_LINESET_OWNERSHIP_RESULT_ATTRIBUTE_NAME = "getLinesetOwnershipResultData";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String commonErrorMessage = "GetLinesetOwnership :: doPost : ";
        debugLog(commonErrorMessage + " started");
        HttpSession session = request.getSession();

        String nextPage = this.getServletConfig().getInitParameter("action_result");
        //minimal security checks....
        TpmsUser currentTpmsUser = (TpmsUser) session.getAttribute( SessionObjects.TPMS_USER );
        if ( currentTpmsUser == null ) {
            TpmsException e = new TpmsException("You are not authorized to access this functionality", "Group Lineset list", "Current user is not authenticated: You are not authorized to access this functionality");
            manageError(e, request, response);
        }

        Vob selectedVob = (Vob) session.getAttribute("vobObject");
        if ( selectedVob == null ){
            manageRedirect(this.getServletConfig().getInitParameter("selectVob"), request, response);
            return;
        }

        if ( !VobManager.D_VOB_TYPE_FLAG_VALUE.equals(selectedVob.getType()) ) {
            manageRedirect(this.getServletConfig().getInitParameter("selectVob"), request, response);
            return;
        }


        String sessionId = session.getId();
        String action = request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);
        //TpmsUser currentTpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin(currentUserLogin);


        if ( action.equals(GET_LINESET_OWNERSHIP_ACTION) ) {
            if ( GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME)) ) {
                //in this case we have to start the request to the BE.
                //produce the request ID that will identify this user request
                String linesetId = request.getParameter(LINESET_ID_PARAMETER);
                LinesetList displayedLsList = (LinesetList) session.getAttribute(GroupLinesetVobQueryServlet.LINESET_LIST_ATTRIBUTE_NAME);
                Lineset selectedLineset = displayedLsList.getElement(linesetId);

                if ( selectedLineset != null ) {
                    String requestId = BackEndInterfaceUtils.getRequestId(sessionId);
                    try {
                        LinesetVobManager.getLinesetOwnership(currentTpmsUser, selectedLineset, requestId, sessionId);
                    } catch (TpmsException e) {
                        manageError(e, request, response);
                    }
                    //populate needed data in order to display the wait page to the user.
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), GET_LINESET_OWNERSHIP_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //error: unable to find selected lineset in displayed lineset list
                    manageError("Unable to identify selected lineset", request, response);

                }
            } else {

                //in this case we have to check if the BE call is terminated
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                //check and track the result...
                int checkResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
                BackEndCallResult beCallResult = new BackEndCallResult();
                try {
                    beCallResult = LinesetVobManager.checkGetLinesetOwnershipResult(requestId, sessionId, currentTpmsUser, selectedVob, currentTpmsUser);
                } catch (TpmsException e) {
                    errorLog(commonErrorMessage + " TpmsException : " + e.getMessage(), e);
                    manageError(e, request, response);
                }
                debugLog(commonErrorMessage + " checkResult = " + checkResult);
                checkResult = beCallResult.getCallResult();
                if ( checkResult == BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT ) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), GET_LINESET_OWNERSHIP_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found...
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                    session.setAttribute(GET_LINESET_OWNERSHIP_RESULT_ATTRIBUTE_NAME, beCallResult);
                    debugLog(commonErrorMessage + " OK finito... redirigo verso la pg che presenta la lista dei ls, list size = ");
                }
            }
        }
        manageRedirect(nextPage, request, response);
    }
}
