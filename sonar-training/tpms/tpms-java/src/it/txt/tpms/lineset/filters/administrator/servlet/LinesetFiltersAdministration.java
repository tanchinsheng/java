package it.txt.tpms.lineset.filters.administrator.servlet;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.SessionObjects;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 19-dic-2006
 * Time: 10.29.56
 */
public class LinesetFiltersAdministration  extends AfsGeneralServlet {
    public static final String ACTION_INITIALIZE = "init";
    public static final String ACTION_SELECT_USERS = "userSelection";
    public static final String ACTION_COPY_FILTER = "copyFilters";

    public void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        doPost( request, response );
    }

    public void doPost ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String action = request.getParameter( "action" );
        String nextPage = null;
        HttpSession session = request.getSession();

        TpmsUser currentUser = ( TpmsUser )session.getAttribute( SessionObjects.TPMS_USER );

        if ( currentUser == null ) {
            TpmsException e = new TpmsException( "You are not authorized to access this functionality", "Action not allowed!" );
            manageError( e, request, response );
            return;
        }

        if (ACTION_INITIALIZE.equals( action )) {

        } else if (ACTION_SELECT_USERS.equals( action )) {

        } else if (ACTION_COPY_FILTER.equals( action )) {

        }

        manageRedirect( nextPage, request, response );
    }
}
