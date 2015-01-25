package it.txt.tpms.bosch.servlets;

import it.txt.afs.security.AfsSecurityManager;
import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.SessionObjects;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.HtmlUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.tpms.bosch.manager.BoschReportsManager;
import it.txt.tpms.lineset.filters.manager.LinesetFilterManager;
import it.txt.tpms.reports.ReportUtils;
import it.txt.tpms.servlets.DbTpListForTpCommentsServlet;
import it.txt.tpms.tp.list.TPList;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.utils.TpmsConfiguration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 25-mag-2006
 * Time: 9.13.14
 */
public class TpReportServlet extends AfsGeneralServlet {


    public static final String TP_LIST_SESSION_ATTRIBUTE_NAME = DbTpListForTpCommentsServlet.TP_LIST_SESSION_ATTRIBUTE_NAME;

    public static final String DESTINATION_PLANT_LIST_SESSION_ATTRIBUTE_NAME = "destinationPlantsList";
    public static final String FROM_PLANT_LIST_SESSION_ATTRIBUTE_NAME = "fromPlanstList";
    public static final String DIVISION_LIST_SESSION_ATTRIBUTE_NAME = "divisionsList";
    public static final String USER_LINESET_FILTERS_SESSION_ATTRIBUTE_NAME = "userLinesetFilters";


    public static final String PERFORM_SEARCH_ACTION = "performSearch";
    public static final String EXPORT_TO_CSV_ACTION = "exportToCsv";
    public static final String EXPORT_TO_HTML_ACTION = "exportToHtml";

    public static final String FROM_PLANT_FIELD_NAME = "fromPlant";
    public static final String TO_PLANT_FIELD_NAME = "toPlant";
    public static final String DIVISION_FIELD_NAME = "division";
    public static final String PRODUCT_LINE_FIELD_NAME = "productLine";
    public static final String JOBNAME_FIELD_NAME = "jobName";
    public static final String LAST_ACTION_FROM_FIELD_NAME = "lastActionFrom";
    public static final String LAST_ACTION_TO_FIELD_NAME = "lastActionTo";
    public static final String LINESET_FILTER_FIELD_NAME = "selectedLinesetFilter";


    private static String formattedSeachCriteria = "";

    public void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        this.doPost( request, response );
    }

    public void doPost ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        String action = request.getParameter( "action" );
        String nextPage = getInitParameter( "tpList" );
        HttpSession session = request.getSession();
        String Status = request.getParameter("status");
        TpmsUser currentUser = ( TpmsUser )session.getAttribute( SessionObjects.TPMS_USER );

        if ( currentUser == null || !AfsSecurityManager.QUERY_USER_ROLE.equals( currentUser.getRole() ) ) {
            TpmsException e = new TpmsException( "You are not authorized to access this functionality", "Action not allowed!" );
            manageError( e, request, response );
            return;
        }

        if ( !GeneralStringUtils.isEmptyString( action ) ) {
            if ( action.equals( PERFORM_SEARCH_ACTION ) ) {
                String fromPlant = request.getParameter( FROM_PLANT_FIELD_NAME );
                String toPlant = request.getParameter( TO_PLANT_FIELD_NAME );
                String division = request.getParameter( DIVISION_FIELD_NAME );
                String productLine = request.getParameter( PRODUCT_LINE_FIELD_NAME );
                String jobName = request.getParameter( JOBNAME_FIELD_NAME );
                String selectedFilterId = request.getParameter( LINESET_FILTER_FIELD_NAME );


                Date lastActionDateFrom = null;
                try {
                    if ( !GeneralStringUtils.isEmptyTrimmedString( request.getParameter( LAST_ACTION_FROM_FIELD_NAME ) ) ) {
                        lastActionDateFrom = HtmlUtils.clientSideDateFormatSearchFields.parse( request.getParameter( LAST_ACTION_FROM_FIELD_NAME ) );
                    }
                } catch ( ParseException e ) {
                    errorLog( "TpReportServlet :: doPost : unable to parse LAST_ACTION_FROM_FIELD_NAME, parameter will not be considered" );
                }

                Date lastActionDateTo = null;
                try {
                    if ( !GeneralStringUtils.isEmptyTrimmedString( request.getParameter( LAST_ACTION_TO_FIELD_NAME ) ) ) {
                        lastActionDateTo = HtmlUtils.clientSideDateFormatSearchFields.parse( request.getParameter( LAST_ACTION_TO_FIELD_NAME ) );
                    }
                } catch ( ParseException e ) {
                    errorLog( "TpReportServlet :: doPost : unable to parse LAST_ACTION_TO_FIELD_NAME, parameter will not be considered" );
                }

                String filterDisplayValue = null;
                if ( !GeneralStringUtils.isEmptyString( selectedFilterId ) ) {
                    Vector userLsFilters = ( Vector )session.getAttribute( USER_LINESET_FILTERS_SESSION_ATTRIBUTE_NAME );
                    int filterPos = VectorUtils.findInVectorOfStringArray( userLsFilters, 0, selectedFilterId );
                    if ( filterPos >= 0 ) {
                        debugLog( "TpReportServlet :: doPost : filterDisplayValue founded = " + ( ( String[] )userLsFilters.get( filterPos ) )[1] );
                        filterDisplayValue = ( ( String[] )userLsFilters.get( filterPos ) )[1];
                    } else {
                        debugLog( "TpReportServlet :: doPost : filterDisplayValue NOT founded " );
                    }
                }

                TPList tpList;
                try {
                    tpList = BoschReportsManager.getTPs( fromPlant, toPlant, division, productLine, jobName, lastActionDateFrom, lastActionDateTo, filterDisplayValue, currentUser ,Status);
                } catch ( TpmsException e ) {
                    manageError( e, request, response );
                    return;
                }
                debugLog( "TpReportServlet :: doPost : " + (tpList != null ? tpList.size() : -3));
                session.setAttribute( TP_LIST_SESSION_ATTRIBUTE_NAME, tpList );

            } else if ( action.equals( EXPORT_TO_CSV_ACTION ) ) {
                initializeFormattedReportSearchCriteria( request );
                nextPage = this.getInitParameter( "exportCsvFormat" );
            } else if ( action.equals( EXPORT_TO_HTML_ACTION ) ) {
                initializeFormattedReportSearchCriteria( request );
           		//String tstatus = request.getParameter("status");
        		request.setAttribute("status",Status);
                nextPage = this.getInitParameter( "exportHtmlFormat" );
            }
        } else {
            initializeSearchListValues( session );
        }
   		//String tstatus = request.getParameter("status");
		request.setAttribute("status",Status);
        manageRedirect( nextPage, request, response );
    }


    private static void initializeFormattedReportSearchCriteria ( HttpServletRequest request ) {
        formattedSeachCriteria = "";
        String division = request.getParameter( DIVISION_FIELD_NAME );
        String fromPlant = request.getParameter( FROM_PLANT_FIELD_NAME );
        String toPlant = request.getParameter( TO_PLANT_FIELD_NAME );
        String productLine = GeneralStringUtils.isEmptyString( request.getParameter( PRODUCT_LINE_FIELD_NAME ) ) ? "" : request.getParameter( PRODUCT_LINE_FIELD_NAME );
        String jobName = GeneralStringUtils.isEmptyString( request.getParameter( JOBNAME_FIELD_NAME ) ) ? "" : request.getParameter( JOBNAME_FIELD_NAME );
        String lastActionDateFrom = GeneralStringUtils.isEmptyString( request.getParameter( LAST_ACTION_FROM_FIELD_NAME ) ) ? "" : request.getParameter( LAST_ACTION_FROM_FIELD_NAME );
        String lastActionDateTo = GeneralStringUtils.isEmptyString( request.getParameter( LAST_ACTION_TO_FIELD_NAME ) ) ? "" : request.getParameter( LAST_ACTION_TO_FIELD_NAME );
        String linesetFilter = GeneralStringUtils.isEmptyString( request.getParameter( LINESET_FILTER_FIELD_NAME ) ) ? "" : request.getParameter( LINESET_FILTER_FIELD_NAME );

        String tmpCriteria;

        if ( !GeneralStringUtils.isEmptyString( jobName ) ) {
            formattedSeachCriteria = "Job name = " + jobName;
        }

        if ( !GeneralStringUtils.isEmptyString( productLine ) ) {
            tmpCriteria = "Product line = " + productLine;
            formattedSeachCriteria = GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? tmpCriteria : formattedSeachCriteria + ", " + tmpCriteria;
        }

        if ( !GeneralStringUtils.isEmptyString( fromPlant ) ) {
            tmpCriteria = "From plant = " + fromPlant;
            formattedSeachCriteria = GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? tmpCriteria : formattedSeachCriteria + ", " + tmpCriteria;
        }

        if ( !GeneralStringUtils.isEmptyString( toPlant ) ) {
            tmpCriteria = "To plant = " + toPlant;
            formattedSeachCriteria = GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? tmpCriteria : formattedSeachCriteria + ", " + tmpCriteria;
        }

        if ( !GeneralStringUtils.isEmptyString( division ) ) {
            tmpCriteria = "Division = " + division;
            formattedSeachCriteria = GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? tmpCriteria : formattedSeachCriteria + ", " + tmpCriteria;
        }
        if ( !GeneralStringUtils.isEmptyString( linesetFilter ) ) {
            tmpCriteria = "Lineset filter = " + linesetFilter;
            formattedSeachCriteria = GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? tmpCriteria : formattedSeachCriteria + ", " + tmpCriteria;
        }
        if ( !GeneralStringUtils.isEmptyString( lastActionDateFrom ) ) {
            tmpCriteria = "Last action: From = " + lastActionDateFrom;
            formattedSeachCriteria = GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? tmpCriteria : formattedSeachCriteria + ", " + tmpCriteria;
        }

        if ( !GeneralStringUtils.isEmptyString( lastActionDateTo ) ) {
            if ( GeneralStringUtils.isEmptyString( lastActionDateFrom ) ) {
                tmpCriteria = "Last action until = " + lastActionDateTo;
            } else {
                tmpCriteria = "To = " + lastActionDateTo;
            }
            formattedSeachCriteria = GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? tmpCriteria : formattedSeachCriteria + ", " + tmpCriteria;
        }
        debugLog( "TpReportServlet :: initializeFormattedReportSearchCriteria : formattedSeachCriteria = " + formattedSeachCriteria );
    }

    public static boolean showExportIcons ( HttpSession session ) {
        TPList reportTpList = ( TPList )session.getAttribute( TP_LIST_SESSION_ATTRIBUTE_NAME );
        return ( reportTpList != null && !reportTpList.isEmpty() );
    }

    private void initializeSearchListValues ( HttpSession session ) {
        String commonErrorMessage = "TpReportServlet :: initializeSearchListValues";
        session.setAttribute( DESTINATION_PLANT_LIST_SESSION_ATTRIBUTE_NAME, ReportUtils.getTpToPlantList() );
        session.setAttribute( FROM_PLANT_LIST_SESSION_ATTRIBUTE_NAME, ReportUtils.getTpFromPlantList() );
        session.setAttribute( DIVISION_LIST_SESSION_ATTRIBUTE_NAME, ReportUtils.getTpDivisionList() );
        TpmsUser currentUser = ( TpmsUser )session.getAttribute( SessionObjects.TPMS_USER );
        Vector userLinesetFilters = null;
        try {
            userLinesetFilters = LinesetFilterManager.getUserLinesetFilter( currentUser );
        } catch ( TpmsException e ) {
            errorLog( commonErrorMessage + ": unable to retrieve user lineset filters " + e.getMessage(), e );
        }
        session.setAttribute( USER_LINESET_FILTERS_SESSION_ATTRIBUTE_NAME, userLinesetFilters );

    }

    public static String getInstallationName () {
        return TpmsConfiguration.getInstance().getTpmsInstName();
    }

    public static String getFormattedDate () {
        SimpleDateFormat dateFormat = new SimpleDateFormat( TpmsConfiguration.getInstance().getAfsDatesFormat() );
        return dateFormat.format( new Date() );
    }


    public static String getFormattedSeachCriteria () {
        return GeneralStringUtils.isEmptyString( formattedSeachCriteria ) ? "None" : formattedSeachCriteria;
    }

}
