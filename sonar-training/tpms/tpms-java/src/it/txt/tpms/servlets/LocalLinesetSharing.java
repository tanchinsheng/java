package it.txt.tpms.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.general.SessionObjects;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.lineset.accessibility.LinesetAccessibilityManager;
import it.txt.tpms.lineset.accessibility.OneLinesetAccessData;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 26-gen-2007
 * Time: 13.23.59
 */
public class LocalLinesetSharing extends AfsGeneralServlet {
    public static final String INITIALIZE_PAGE_DATA_ACTION = "init";
    public static final String SAVE_LINESET_ACCESSIBILITY_ACTION = "save";

    public static final String LINESET_NAME_FIELD = "linesetName";
    public static final String LINESET_CREATOR_FIELD = "originalOwner";
    public static final String VOBNAME_FIELD = "vobName";
    public static final String DIVISION_USERS_FIELD = "divisionUsers";
    public static final String ALLOWED_USERS_FIELD = "allowedUsers";
    public static final String SOURCE_XML_FILE = "xmlFileName";

    public static final String DIVISION_USERS_ATTRIBUTE = "divisionUsers";
    public static final String LINESET_RIGHTS_ATTRIBUTE = "lsAccessData";
    public static final String RESULT_USER_MESSAGE_ATTRIBUTE = "actionResultMessage";

    public void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        doPost( request, response );
    }

    public void doPost ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String action = request.getParameter( AfsServletUtils.ACTION_FIELD_NAME );
        HttpSession session = request.getSession();
        TpmsUser currentUser = ( TpmsUser )session.getAttribute( SessionObjects.TPMS_USER );
        String commonErrorMessage = "LocalLinesetSharing :: doPost";
        String nextPage = this.getServletConfig().getInitParameter( "nextPage" );

        if ( currentUser == null ) {
            TpmsException e = new TpmsException( "You are not authorized to access this functionality", commonErrorMessage, "Current user is not authenticated: You are not authorized to access this functionality" );
            manageError( e, request, response );
        }

        Vob selectedVob = ( Vob )session.getAttribute( "vobObject" );

        if ( selectedVob == null ) {
            manageRedirect( this.getServletConfig().getInitParameter( "selectVob" ), request, response );
        }

        String linesetName = request.getParameter( LINESET_NAME_FIELD );
        String vobName = request.getParameter( VOBNAME_FIELD );
        //the trim on . char is due to the fact that the backend can not have empty strings....no comment on this please!;o)
        String linesetCreatorUnixLogin = GeneralStringUtils.trimChar( request.getParameter( LINESET_CREATOR_FIELD ), '.');
        if (GeneralStringUtils.isEmptyString( linesetCreatorUnixLogin ) ) {
            linesetCreatorUnixLogin = currentUser.getUnixUser();
        }
        OneLinesetAccessData oneLinesetAccessData = LinesetAccessibilityManager.getLinesetAccessData( linesetName, vobName );
        debugLog( commonErrorMessage + " : action = " + action + " linesetName = " + linesetName + " vobName = " + vobName + " originalOwner = " + linesetCreatorUnixLogin );
        String userMessage = "";
        if ( GeneralStringUtils.isEmptyString( action ) || action.equals( INITIALIZE_PAGE_DATA_ACTION ) ) {
            //recupra la lista degli utenti che possono prendere questo ls
            //recupera la lista degli utenti che potrebbero prendere il ls
            ArrayList myTemporaryAllowedUser;
            if ( oneLinesetAccessData == null ) {
                //in this case for this lineset no sharing already exists...
                oneLinesetAccessData = new OneLinesetAccessData();
                oneLinesetAccessData.setLinesetCreatorLogin( linesetCreatorUnixLogin );
                oneLinesetAccessData.setLinesetName( linesetName );
                oneLinesetAccessData.setVobName( vobName );
            }
            myTemporaryAllowedUser = new ArrayList( oneLinesetAccessData.getAllowedUsersLogin() );
            myTemporaryAllowedUser.add( linesetCreatorUnixLogin );
            ArrayList divisionEngineers = UserUtils.getEngineerUnixLoginsByDivision( currentUser.getDivision(), myTemporaryAllowedUser );

            request.setAttribute( DIVISION_USERS_ATTRIBUTE, divisionEngineers );
            session.setAttribute( LINESET_RIGHTS_ATTRIBUTE, oneLinesetAccessData );

        } else if ( action.equals( SAVE_LINESET_ACCESSIBILITY_ACTION ) ) {
            if ( oneLinesetAccessData == null ) {
                oneLinesetAccessData = ( OneLinesetAccessData )session.getAttribute( LocalLinesetSharing.LINESET_RIGHTS_ATTRIBUTE );
            }
            String[] allowedUserLogins = request.getParameterValues( ALLOWED_USERS_FIELD );
            ArrayList myAllowedUserLogins = new ArrayList();
            if ( allowedUserLogins != null && allowedUserLogins.length > 0 ) {
                for ( int i = 0; i < allowedUserLogins.length; i++ ) {
                    myAllowedUserLogins.add( allowedUserLogins[i] );
                }
            }

            userMessage = "Lineset sharing information successfully saved";
            oneLinesetAccessData.setAllowedUsersLogin( myAllowedUserLogins );

            try {
                LinesetAccessibilityManager.setLinesetAccessData( oneLinesetAccessData );
            } catch ( IOException e ) {
                errorLog( commonErrorMessage + " : IOException : " + e.getMessage(), e );
                userMessage = "Error while saving lineset sharing informations. Advice your administartor<!--" + e.getMessage() + "-->";
            }

            String[] notSelectedDivisionUsers = request.getParameterValues( DIVISION_USERS_FIELD );
            ArrayList myNotSelectedDivisionUsers = new ArrayList();

            if ( notSelectedDivisionUsers != null && notSelectedDivisionUsers.length > 0 ) {
                for ( int i = 0; i < notSelectedDivisionUsers.length; i++ ) {
                    myNotSelectedDivisionUsers.add( notSelectedDivisionUsers[i] );
                }
            } else {
                debugLog( commonErrorMessage + " : action = notSelectedDivisionUsers == null ?" + (notSelectedDivisionUsers == null)  );
            }

            request.setAttribute( DIVISION_USERS_ATTRIBUTE, myNotSelectedDivisionUsers );
        }

        request.setAttribute( RESULT_USER_MESSAGE_ATTRIBUTE, userMessage );
        manageRedirect( nextPage, request, response );
    }
}
