package it.txt.tpms.lineset.filters.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.general.SessionObjects;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.HtmlUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.tpms.lineset.filters.manager.LinesetFilterManager;
import it.txt.tpms.lineset.list.LinesetList;
import it.txt.tpms.lineset.managers.LinesetDBManager;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 13-dic-2006
 * Time: 10.27.02
 * this servlet is used for management of user linset filters.
 */

public class FilterManagement extends AfsGeneralServlet {
    public static final String ACTION_SEARCH_LINESET = "searchLineset";
    public static final String ACTION_INITIALIZE_SEARCHING_VALUES = "init";
    public static final String ACTION_ASSOCIATE_EXISTING_FILTER = "associateFilter";
    public static final String ACTION_CREATE_NEW_FILTER = "createFilter";
    public static final String ACTION_REMOVE_FILTER = "removeFilter";
    public static final String ACTION_SEARCH_LINESET_FOR_FILTER_REMOVAL = "searchLinesetForFilterRemoval";

    public static final String ATTRIBUTE_USER_FILTERS = "userFilters";
    public static final String ATTRIBUTE_PLANTS_NAME = "plantsName";
    public static final String ATTRIBUTE_FOUNDED_LINESETS = "linesetList";
    public static final String ATTRIBUTE_LINESET_FILTER_DISPLAY_VALUE = "selectedFilterDisplay";


    public static final String SEARCH_PARAM_LINESET_NAME = "linesetName";
    public static final String SEARCH_PARAM_LAST_ACTION_DATE_FROM = "actionDateFrom";
    public static final String SEARCH_PARAM_LAST_ACTION_DATE_TO = "actionDateTo";
    public static final String SEARCH_PARAM_PLANT = "plantId";
    public static final String SEARCH_PARAM_LINESET_FILTER = "filterId";

    public static final String FIELD_SELECTED_LINESET = "selectedLineset";
    public static final String FIELD_NEW_FILTER_DISPLAY_VALUE = "newFilterDisplayValue";
    public static final String FIELD_SELECTED_FILTER = "selectedFilter";
    public static final String FIELD_SELECTED_LINESETS = "selectedLinesets";

    public void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        doPost( request, response );
    }

    public void doPost ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String commonErrorMessage = "FilterManagement :: doPost";
        String action = request.getParameter( AfsServletUtils.ACTION_FIELD_NAME );
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        TpmsUser currentUser = ( TpmsUser )session.getAttribute( SessionObjects.TPMS_USER );

        String nextPage = null;
        debugLog( commonErrorMessage + ": action = " + action );
        if ( currentUser == null ) {

            TpmsException e = new TpmsException( "You are not authorized to access this functionality", "Lineset filter management", "Current user is not authenticated: You are not authorized to access this functionality" );
            manageError( e, request, response );
            return;
        }

        if ( GeneralStringUtils.isEmptyString( action ) || action.equals( ACTION_INITIALIZE_SEARCHING_VALUES ) ) {

            //initialize combo search values
            try {
                getUserFiltersList( session, currentUser );
            } catch ( TpmsException e ) {
                manageError( e, request, response );
                return;
            }
            Vector lsPlantList;
            try {
                lsPlantList = LinesetDBManager.getLinesetPlantList();
            } catch ( TpmsException e ) {
                manageError( e, request, response );
                return;
            }
            session.setAttribute( ATTRIBUTE_PLANTS_NAME, lsPlantList );
            nextPage = this.getServletConfig().getInitParameter( "linesetSearchPage" );
        } else if ( action.equals( ACTION_SEARCH_LINESET ) ) {
            //search linesets...
            LinesetList foundedLinesetList = null;
            try {
                foundedLinesetList = searchLinesets( request, currentUser );
            } catch ( TpmsException e ) {
                manageError( e, request, response );
            }
            session.setAttribute( ATTRIBUTE_FOUNDED_LINESETS, foundedLinesetList );
            nextPage = this.getServletConfig().getInitParameter( "linesetSearchPage" );
        } else if ( action.equals( ACTION_ASSOCIATE_EXISTING_FILTER ) ) {
            //asscociate an existing filter to seleceted linesets...
            LinesetList selectedLinesets = getUserSelectedLinesets( request );
            String selectedFilterDisplayValue = getSelectedFilterDisplayValue( request );

            if ( selectedLinesets.size() > 0 && !GeneralStringUtils.isEmptyTrimmedString( selectedFilterDisplayValue ) ) {
                LinesetFilterManager.associateFilter( selectedLinesets, currentUser, selectedFilterDisplayValue, sessionId );
            }

            nextPage = this.getServletConfig().getInitParameter( "associateFilterResult" );
        } else if ( action.equals( ACTION_CREATE_NEW_FILTER ) ) {
            //create a new filter and associate it to selected linesets...
            String newFilterDisplayValue = request.getParameter( FIELD_NEW_FILTER_DISPLAY_VALUE );
            LinesetList selectedLinesets = getUserSelectedLinesets( request );

            if ( !GeneralStringUtils.isEmptyTrimmedString( newFilterDisplayValue ) && selectedLinesets.size() > 0 ) {
                //let's create the new filter and associte it to the selected linesets..
                LinesetFilterManager.createFilter( selectedLinesets, currentUser, newFilterDisplayValue, sessionId );
                try {
                    //let's update user filters loaded in session...
                    getUserFiltersList( session, currentUser );
                } catch ( TpmsException e ) {
                    manageError( e, request, response );
                }
            }

            nextPage = this.getServletConfig().getInitParameter( "associateFilterResult" );

        } else if ( action.equals( ACTION_REMOVE_FILTER ) ) {
            //remove an exisiting filter from selected linesets
            String filterDisplayValue = getSelectedFilterDisplayValue( request );
            LinesetList foundedLinesetList = null;
            LinesetList selectedLinesets = getUserSelectedLinesets( request );
            if ( !GeneralStringUtils.isEmptyTrimmedString( filterDisplayValue ) && selectedLinesets.size() > 0 ) {
                LinesetFilterManager.removeFilterFromLinesets( currentUser, filterDisplayValue, selectedLinesets, sessionId );
                try {
                    foundedLinesetList = searchLinesets( request, currentUser );
                } catch ( TpmsException e ) {
                    manageError( e, request, response );
                }
                
                try {
                    //let's update user filters loaded in session...
                    getUserFiltersList( session, currentUser );
                } catch ( TpmsException e ) {
                    manageError( e, request, response );
                }
            }


            request.setAttribute( ATTRIBUTE_LINESET_FILTER_DISPLAY_VALUE, getSelectedFilterDisplayValue( request ) );
            session.setAttribute( ATTRIBUTE_FOUNDED_LINESETS, foundedLinesetList );
            nextPage = this.getServletConfig().getInitParameter( "linesetListForFilterRemoval" );

        } else if ( action.equals( ACTION_SEARCH_LINESET_FOR_FILTER_REMOVAL ) ) {
            //search linesets for remove filter...
            LinesetList foundedLinesetList = null;
            try {
                foundedLinesetList = searchLinesets( request, currentUser );
            } catch ( TpmsException e ) {
                manageError( e, request, response );
            }
            request.setAttribute( ATTRIBUTE_LINESET_FILTER_DISPLAY_VALUE, getSelectedFilterDisplayValue( request ) );
            session.setAttribute( ATTRIBUTE_FOUNDED_LINESETS, foundedLinesetList );
            nextPage = this.getServletConfig().getInitParameter( "linesetListForFilterRemoval" );

        }

        manageRedirect( nextPage, request, response );
    }

    /**
     * @param request needed to look up values
     *
     * @return the filter display value of an already exisiting filter selected by the user.
     */
    private String getSelectedFilterDisplayValue ( HttpServletRequest request ) {

        String commonErrorMessage = "FilterManagement :: getSelectedFilterDisplayValue";
        HttpSession session = request.getSession();
        Vector userLsFilters = ( Vector )session.getAttribute( ATTRIBUTE_USER_FILTERS );
        String selectedFilterId = request.getParameter( SEARCH_PARAM_LINESET_FILTER );
        debugLog( commonErrorMessage + " : userFilters.size() = " + userLsFilters.size() + " userFilterId = " + selectedFilterId );
        int filterPos = VectorUtils.findInVectorOfStringArray( userLsFilters, 0, selectedFilterId );
        if ( filterPos >= 0 ) {
            debugLog( commonErrorMessage + " : filterDisplayValue founded = " + ( ( String[] )userLsFilters.get( filterPos ) )[1] );
            return ( ( String[] )userLsFilters.get( filterPos ) )[1];
        } else {
            debugLog( commonErrorMessage + " : filterDisplayValue NOT founded " );
            return "";
        }
    }


    /**
     * @param request needed to look up values
     *
     * @return the list of lineset selected by the user
     */
    private LinesetList getUserSelectedLinesets ( HttpServletRequest request ) {
        String commonErrorMessage = "FilterManagement :: getSelectedFilterDisplayValue";
        LinesetList selectedLinesets = new LinesetList();
        String[] selectedLinesetsId = request.getParameterValues( FIELD_SELECTED_LINESET );
        debugLog( commonErrorMessage + " : selectedLinesetsId != null ? " + ( selectedLinesetsId != null ) );
        if ( selectedLinesetsId != null && selectedLinesetsId.length > 0 ) {

            HttpSession session = request.getSession();
            LinesetList fullLinesetList = ( LinesetList )session.getAttribute( FilterManagement.ATTRIBUTE_FOUNDED_LINESETS );
            for ( int i = 0; i < selectedLinesetsId.length; i++ ) {
                selectedLinesets.addElement( fullLinesetList.getElement( selectedLinesetsId[i] ) );
            }
        }
        return selectedLinesets;
    }

    /**
     * load the list of user defined users filter
     *
     * @param session     in order to add info into session
     * @param currentUser used to identify filters
     *
     * @return load the list of user defined users filter
     *
     * @throws TpmsException if an error happen
     */
    private Vector getUserFiltersList ( HttpSession session, TpmsUser currentUser ) throws TpmsException {
        Vector userLsFiltersList = LinesetFilterManager.getUserLinesetFilter( currentUser );
        session.setAttribute( ATTRIBUTE_USER_FILTERS, userLsFiltersList );
        return userLsFiltersList;
    }

    /**
     * retrieves the list of lineset according to search criteria
     *
     * @param request     in order to retrieve search parameter
     * @param currentUser in order to search linesets
     *
     * @return the list of lineset according to search criteria
     *
     * @throws TpmsException if an error happens
     */
    private LinesetList searchLinesets ( HttpServletRequest request, TpmsUser currentUser ) throws TpmsException {
        String commonErrorMessage = "FilterManagement :: searchLinesets";
        HttpSession session = request.getSession();
        String linesetName = request.getParameter( SEARCH_PARAM_LINESET_NAME );
        String plantId = request.getParameter( SEARCH_PARAM_PLANT );
        String strLastActionDateFrom = request.getParameter( SEARCH_PARAM_LAST_ACTION_DATE_FROM );
        Date lastActionDateFrom = null;

        if ( !GeneralStringUtils.isEmptyString( strLastActionDateFrom ) ) {
            try {
                lastActionDateFrom = HtmlUtils.clientSideDateFormatSearchFields.parse( strLastActionDateFrom );
            } catch ( ParseException e ) {
                errorLog( commonErrorMessage + " : unable to parse SEARCH_PARAM_LAST_ACTION_DATE_FROM (*" + strLastActionDateFrom + "*), parameter will ignored " );
            }
        }

        String strLastActionDateTo = request.getParameter( SEARCH_PARAM_LAST_ACTION_DATE_TO );
        Date lastActionDateTo = null;
        if ( !GeneralStringUtils.isEmptyString( strLastActionDateTo ) ) {
            try {
                lastActionDateTo = HtmlUtils.clientSideDateFormatSearchFields.parse( strLastActionDateTo );
            } catch ( ParseException e ) {
                errorLog( commonErrorMessage + " : unable to parse SEARCH_PARAM_LAST_ACTION_DATE_TO (*" + strLastActionDateTo + "*), parameter will ignored " );
            }
        }

        String lsFilterId = request.getParameter( SEARCH_PARAM_LINESET_FILTER );
        String selectedFilterDisplayValue = null;
        if ( !GeneralStringUtils.isEmptyString( lsFilterId ) ) {
            Vector userFilterList = ( Vector )session.getAttribute( ATTRIBUTE_USER_FILTERS );
            int filterPos = VectorUtils.findInVectorOfStringArray( userFilterList, 0, lsFilterId );
            if ( filterPos > 0 ) {
                selectedFilterDisplayValue = ( ( String[] )userFilterList.get( filterPos ) )[1];
            }
        }

        debugLog( commonErrorMessage + " : ****************************************************" );
        debugLog( commonErrorMessage + "linesetName = " + linesetName + " strLastActionDateFrom = " + strLastActionDateFrom + " strLastActionDateTo = " + strLastActionDateTo + " plantId = " + plantId + " filterId = " + lsFilterId );
        debugLog( commonErrorMessage + " : ****************************************************" );

        return LinesetDBManager.getLinesetListWithSpecificUserFilters( currentUser, linesetName, lastActionDateFrom, lastActionDateTo, plantId, selectedFilterDisplayValue );
    }

}
