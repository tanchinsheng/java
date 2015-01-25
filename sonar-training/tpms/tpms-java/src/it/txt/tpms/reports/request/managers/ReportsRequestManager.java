package it.txt.tpms.reports.request.managers;


import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.reports.request.ReportRequestObject;
import it.txt.tpms.reports.request.ReportRequestObjectsList;
import it.txt.tpms.managers.report.tp.TpReportManager;
import tpms.TpmsException;
import tpms.utils.QueryUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 15-mar-2006
 * Time: 13.24.32
 * this class is usefull to manage the TPMS_REPORTS_REQUESTS table content and to manage ReportRequestObject
 */
public class ReportsRequestManager extends QueryUtils {

    /**
     * This method is in charge to produce the id that will uniquely identify a report request object
     *
     * @param owner     is who has issued the request
     * @param startDate is the date on wich the request was reaised
     * @return the id for a new ReportRequestObject
     */
    protected static String generateReportRequestId(String owner, Date startDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return tpmsConfiguration.getTpmsInstName() + "_" + owner + "_" + sdf.format(startDate);
    }

    /**
     * This method raises an Exception if the mandatory attributes are not present in a ReqprtRequestObject in order to insert it into database
     *
     * @param reportRequest the object that should be checked
     * @throws TpmsException if any mandatory attributes is missing
     */
    protected static void checkReportRequestCretionMandatoryAttributes(ReportRequestObject reportRequest) throws TpmsException {
        //mandatory attributes are ID, INSTALLATION_ID, OWNER_LOGIN, START_DATE,
        String emptyFields = "";
        if (reportRequest == null) {

            TpmsException e = new TpmsException("ReportsRequestManager :: checkReportRequestCretionMandatoryAttributes", "You can't create a null ReportRequestObject");
            errorLog("", e);
            throw e;
        }
        if (GeneralStringUtils.isEmptyString(reportRequest.getId())) {
            if (GeneralStringUtils.isEmptyString(emptyFields))
                emptyFields = "ID";
            else
                emptyFields = ", ID";
        }
        if (GeneralStringUtils.isEmptyString(reportRequest.getInstallationId())) {
            if (GeneralStringUtils.isEmptyString(emptyFields))
                emptyFields = "INSTALLATION_ID";
            else
                emptyFields = ", INSTALLATION_ID";
        }
        if (GeneralStringUtils.isEmptyString(reportRequest.getOwnerTpmsLogin())) {
            if (GeneralStringUtils.isEmptyString(emptyFields))
                emptyFields = "OWNER_LOGIN";
            else
                emptyFields = ", OWNER_LOGIN";
        }
        if (reportRequest.getStartDate() == null) {
            if (GeneralStringUtils.isEmptyString(emptyFields))
                emptyFields = "START_DATE";
            else
                emptyFields = ", START_DATE";
        }

        if (GeneralStringUtils.isEmptyString(reportRequest.getReportType())) {
            if (GeneralStringUtils.isEmptyString(emptyFields))
                emptyFields = "REPORT_TYPE";
            else
                emptyFields = ", REPORT_TYPE";
        }
        if (!GeneralStringUtils.isEmptyString(emptyFields)) {
            throw new TpmsException("ReportsRequestManager :: checkReportRequestCretionMandatoryAttributes", "Report Request Manager The following files are mandatory: " + emptyFields);
        }
    }

    /**
     * This method instantiates a new Report Request Object and saves it to the database
     * @param reportType    The type of the report that was requested
     * @param ownerTpmsLogin the TpmsLogin of the user that has issued the request
     * @param criteria       the user selected criteria
     * @param sessionId      the session id of the user
     * @return a new ReportRequestObject
     * @throws TpmsException in case of errors
     */
    public static ReportRequestObject newReportRequest(String reportType, String ownerTpmsLogin, String criteria, String sessionId) throws TpmsException {
        ReportRequestObject reportRequest;
        Date startDate = new Date();
        String reportId = generateReportRequestId(ownerTpmsLogin, startDate);
        reportRequest = new ReportRequestObject(reportId, tpmsConfiguration.getTpmsInstName(), ownerTpmsLogin, criteria, reportType, startDate);
        insertReportRequest(reportRequest, sessionId);
        return reportRequest;
    }

    /**
     * Insert a new ReportRequest inside of database
     *
     * @param reportRequest that should be inserted
     * @param sessionId     the session id of the curent user
     * @throws TpmsException if en arror occours
     */
    public static void insertReportRequest(ReportRequestObject reportRequest, String sessionId) throws TpmsException {
        checkReportRequestCretionMandatoryAttributes(reportRequest);

        String reportRequestFiledNames = "ID, INSTALLATION_ID, OWNER_LOGIN, START_DATE, END_DATE, CRITERIA, REPORT_TYPE";
        StringBuffer reportRequestFieldValues = new StringBuffer();
        String valuesSeparator = ", ";
        reportRequestFieldValues.append(getStringValueForQuery(reportRequest.getId()));
        reportRequestFieldValues.append(valuesSeparator).append(getStringValueForQuery(reportRequest.getInstallationId()));
        reportRequestFieldValues.append(valuesSeparator).append(getStringValueForQuery(reportRequest.getOwnerTpmsLogin()));
        reportRequestFieldValues.append(valuesSeparator).append(getDateForQuery(reportRequest.getStartDate()));
        reportRequestFieldValues.append(valuesSeparator).append(getDateForQuery(reportRequest.getEndDate()));
        reportRequestFieldValues.append(valuesSeparator).append(getStringValueForQuery(reportRequest.getCriteria()));
        reportRequestFieldValues.append(valuesSeparator).append(getStringValueForQuery(reportRequest.getReportType()));
        String query = "insert into TPMS_REPORTS_REQUESTS (" + reportRequestFiledNames + ") values (" + reportRequestFieldValues.toString() + ")";

        boolean queryDone = executeInsertQuery(query, sessionId, reportRequest.getOwnerTpmsLogin());

        if ( queryDone ) {
            TpReportManager tpReportManager = new TpReportManager(reportRequest);
            tpReportManager.run();
        }
    }


    /**
     * update the database data of reportRequest with the information contained in the given object
     *
     * @param reportRequest the object taht should be update in database.
     * @param sessionId     is used in order to manage the lost query
     * @throws TpmsException in case of error
     */
    public static void updateReportRequest(ReportRequestObject reportRequest, String sessionId) throws TpmsException {
        checkReportRequestCretionMandatoryAttributes(reportRequest);
        String valuesSeparator = ", ";
        StringBuffer reportRequestFieldsValues = new StringBuffer();

        reportRequestFieldsValues.append("INSTALLATION_ID = ").append(getStringValueForQuery(reportRequest.getInstallationId())).append(valuesSeparator);
        reportRequestFieldsValues.append("OWNER_LOGIN = ").append(getStringValueForQuery(reportRequest.getOwnerTpmsLogin())).append(valuesSeparator);
        reportRequestFieldsValues.append("START_DATE = ").append(getDateForQuery(reportRequest.getStartDate())).append(valuesSeparator);
        reportRequestFieldsValues.append("END_DATE = ").append(getDateForQuery(reportRequest.getEndDate())).append(valuesSeparator);
        reportRequestFieldsValues.append("CRITERIA = ").append(getStringValueForQuery(reportRequest.getCriteria())).append(valuesSeparator);
        reportRequestFieldsValues.append("REPORT_TYPE = ").append(getStringValueForQuery(reportRequest.getReportType())).append(valuesSeparator);
        //REPORT_TYPE
        String query = "update TPMS_REPORTS_REQUESTS set " + reportRequestFieldsValues.toString() + " where id = " + getStringValueForQuery(reportRequest.getId());
        executeUpdateQuery(query, sessionId, reportRequest.getOwnerTpmsLogin());
    }

    /**
     * If the given report request object is not null delete it from the database
     *
     * @param reportRequest    report request that should be deleted
     * @param currentUserLogin the login of the current user
     * @param sessionId        the session id of the curtrent user (used for lost query action)
     */
    public static void deleteReportRequest(ReportRequestObject reportRequest, String sessionId, String currentUserLogin) throws TpmsException {
        if (reportRequest != null)
            deleteReportRequest(reportRequest.getId(), currentUserLogin, sessionId);
    }


    /**
     * Delete from db the ReportRequestObject identified by reportRequestId, an ownership check should be done
     *
     * @param reportRequestId  the id of the ReportRequestObjectthat should be deleted
     * @param currentUserLogin the current user
     * @param sessionId
     */
    public static void deleteReportRequest(String reportRequestId, String currentUserLogin, String sessionId) throws TpmsException {
        String query = "delete TPMS_REPORTS_REQUESTS where id = " + getStringValueForQuery(reportRequestId);
        String selectQuery = "select ID from TPMS_REPORTS_REQUESTS " +
                "where ID = " + getStringValueForQuery(reportRequestId) + " and INSTALLATION_ID = " + getStringValueForQuery(tpmsConfiguration.getTpmsInstName()) +
                "and OWNER_LOGIN = " + getStringValueForQuery(currentUserLogin);
        ResultSet rs = executeSelectQuery(selectQuery);
        boolean executeDelete = false;
        try {
            if (rs != null && rs.next()) {
                executeDelete = true;
                //rs.close();
            }
        } catch (SQLException e) {
            errorLog("ReportsRequestManager :: deleteReportRequestId : unable to fetch data reportRequestId = " + reportRequestId + " currentUserLogin = " + currentUserLogin +
                    " sessionId = " + sessionId);
        } finally {  
        	if (rs!= null){
        		try {
        			rs.close();
        		} catch (SQLException se) {
        			errorLog("ReportsRequestManager :: deleteReportRequest : error while closing resultset! ",se);
        		}
        	}
        }
        
        if (executeDelete)
            executeDeleteQuery(query, sessionId, currentUserLogin);
    }

    public static ReportRequestObject getReportRequest(String reportRequestId, String currentUserLogin) throws TpmsException{
        ReportRequestObject result = null;
        String query = "select ID, INSTALLATION_ID, OWNER_LOGIN, START_DATE, END_DATE, CRITERIA, REPORT_TYPE " +
                       "from TPMS_REPORTS_REQUESTS " +
                       "where INSTALLATION_ID = " + getStringValueForQuery(tpmsConfiguration.getTpmsInstName()) +
                        " and OWNER_LOGIN = " + getStringValueForQuery(currentUserLogin) + " and id = " + getStringValueForQuery(reportRequestId);

        try {
            ResultSet oneReportRequestRs = executeSelectQuery(query);
            if (oneReportRequestRs != null) {
                oneReportRequestRs.next();
                result = new ReportRequestObject( oneReportRequestRs.getString("ID"), oneReportRequestRs.getString("INSTALLATION_ID"),
                oneReportRequestRs.getString("OWNER_LOGIN"), oneReportRequestRs.getDate("START_DATE"),
                oneReportRequestRs.getDate("END_DATE"), oneReportRequestRs.getString("CRITERIA"),
                oneReportRequestRs.getString("REPORT_TYPE") );
                try {
                    if (oneReportRequestRs != null)
                    	oneReportRequestRs.close();
                } catch (SQLException e) {
                    errorLog("ReportsRequestManager :: getReportRequest : error while closing resultset", e);
                }           
            }

        } catch (TpmsException e) {
            //todo gestici l'errore  : predi eccezione, log rilancia con messaggio capibile dall'utente.
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        } catch (SQLException e) {
            //todo gestici l'errore  : predi eccezione, log, rilancia con messaggio capibile dall'utente.
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new TpmsException("", "", "");
        }

        return result;
    }

    /**
     * retrieve the current user report request object list
     * @param currentUserLogin
     * @return the current user report request object list
     */
    public static ReportRequestObjectsList getUserReportRequests(String currentUserLogin) throws TpmsException {
        ReportRequestObjectsList reportRequestObjectsList = new ReportRequestObjectsList();
        String query = "select ID, INSTALLATION_ID, OWNER_LOGIN, START_DATE, END_DATE, CRITERIA, REPORT_TYPE " +
                       "from TPMS_REPORTS_REQUESTS " +
                       "where INSTALLATION_ID = " + getStringValueForQuery(tpmsConfiguration.getTpmsInstName()) +
                        " and OWNER_LOGIN = " + getStringValueForQuery(currentUserLogin);

        ResultSet reportRequestsRs = executeSelectQuery(query);
        if (reportRequestsRs != null) {
            ReportRequestObject tmpReportRequest;
            try {
                while(reportRequestsRs.next()){
                    //(String id, String installationId, String ownerTpmsLogin, Date startDate, Date endDate, String criteria)
                    tmpReportRequest = new ReportRequestObject( reportRequestsRs.getString("ID"), reportRequestsRs.getString("INSTALLATION_ID"),
                                                                reportRequestsRs.getString("OWNER_LOGIN"), reportRequestsRs.getDate("START_DATE"),
                                                                reportRequestsRs.getDate("END_DATE"), reportRequestsRs.getString("CRITERIA"),
                                                                reportRequestsRs.getString("REPORT_TYPE") );
                    reportRequestObjectsList.addReportRequest( tmpReportRequest );
                }
            } catch (SQLException e) {
                errorLog("ReportsRequestManager :: getUserReportRequests : unable to fetch results", e);
                throw new TpmsException("Error while trying to get Reports requets", "", e);
            } finally {
                try {
                    reportRequestsRs.close();
                } catch (SQLException e) {
                    errorLog("ReportsRequestManager :: getUserReportRequests : unable to to close ResultSet", e);
                }
            }
        }
        return reportRequestObjectsList;
    }

}
