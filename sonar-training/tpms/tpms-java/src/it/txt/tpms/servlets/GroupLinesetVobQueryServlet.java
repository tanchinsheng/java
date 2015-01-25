package it.txt.tpms.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.afs.utils.AfsWaitInformations;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;
import it.txt.tpms.backend.utils.BackEndCallResult;
import it.txt.tpms.backend.utils.BackEndInterfaceConstants;
import it.txt.tpms.backend.utils.BackEndInterfaceUtils;
import it.txt.tpms.lineset.list.LinesetList;
import it.txt.tpms.lineset.managers.LinesetVobManager;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.VobManager;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class GroupLinesetVobQueryServlet extends AfsGeneralServlet {

    public static final String GET_GROUP_LINESETS_ACTION = "getGroupLinesets";
    public static final String LINESET_LIST_ATTRIBUTE_NAME = "linesetList";

    public void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        doPost( request, response );
    }

    public void doPost ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String commonErrorMessage = "GroupLinesetVobQueryServlet :: doPost : ";
        debugLog( commonErrorMessage + " started" );
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        String nextPage = this.getServletConfig().getInitParameter( "listPage" );
        if (currentUser == null) {
            TpmsException e = new TpmsException( "You are not authorized to access this page", "Group Lineset list", "You are not authorized to access this page" );
            manageError( e, request, response );
        }
        //look for selected D vod info...
        Vob selectedVob = ( Vob )session.getAttribute( "vobObject" );

        if ( selectedVob == null ) {
            manageRedirect( this.getServletConfig().getInitParameter( "selectVob" ), request, response );
            return;
        }

        if ( !VobManager.D_VOB_TYPE_FLAG_VALUE.equals( selectedVob.getType() ) ) {
            manageRedirect( this.getServletConfig().getInitParameter( "selectVob" ), request, response );
            return;
        }

        debugLog( commonErrorMessage + " checks terminated" );
        //ok all verification done with positive result.... let's proceed with action
        String action = request.getParameter( AfsServletUtils.ACTION_FIELD_NAME );
        String sessionId = session.getId();
        String currentUserUnixLogin = UserUtils.getUserUnixLogin( currentUser.getTpmsLogin() );

        if ( GeneralStringUtils.isEmptyString( action ) || action.equals( GET_GROUP_LINESETS_ACTION ) ) {
            if ( GeneralStringUtils.isEmptyString( request.getParameter( AfsServletUtils.CHECK_RESULT_REQUEST_NAME ) ) ) {

                //in this case we have to start the request to the BE.
                //produce the request ID that will identify this user request
                //first of all remove any possible previous result...
                session.removeAttribute( AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME );
                String requestId = BackEndInterfaceUtils.getRequestId( sessionId );
                 try {
                    LinesetVobManager.otherLinesetQuery( selectedVob, currentUser.getTpmsLogin(), requestId, sessionId );
                } catch ( TpmsException e ) {
                    manageError( e, request, response );
                }
                //populate needed data in order to display the wait page to the user.
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations( this.getServletName(), GET_GROUP_LINESETS_ACTION, requestId );
                request.setAttribute( AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations );
            } else {
                //in this case we have to check if the BE call is terminated
                String requestId = request.getParameter( AfsServletUtils.CHECK_RESULT_REQUEST_NAME );
                //check and track the result...
                int checkResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
                LinesetList lsList;
                BackEndCallResult beCallResult = new BackEndCallResult();
                try {
                    //beCallResult = LinesetVobManager.checkOtherLinesetQueryResult(requestId, sessionId, currentUserLogin, selectedVobName);
                    beCallResult = LinesetVobManager.checkOtherLinesetQueryResult( requestId, sessionId, currentUserUnixLogin, selectedVob );
                } catch ( TpmsException e ) {
                    errorLog( commonErrorMessage + " TpmsException : " + e.getMessage(), e );
                    manageError( e, request, response );
                }
                debugLog( commonErrorMessage + " checkResult = " + checkResult );
                checkResult = beCallResult.getCallResult();
                if ( checkResult == BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT ) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations( this.getServletName(), GET_GROUP_LINESETS_ACTION, requestId );
                    request.setAttribute( AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations );
                } else {

                    lsList = ( LinesetList )beCallResult.getResultData();
                    //The result was found...
                    request.removeAttribute( AfsServletUtils.CHECK_RESULT_REQUEST_NAME );
                    session.setAttribute( LINESET_LIST_ATTRIBUTE_NAME, lsList );
                    debugLog( commonErrorMessage + " OK finito... redirigo verso la pg che presenta la lista dei ls, list size = " + lsList.size() );
                }
            }
        }
        manageRedirect( nextPage, request, response );
    }
}
