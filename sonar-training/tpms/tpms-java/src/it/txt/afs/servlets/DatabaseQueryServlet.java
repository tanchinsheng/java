package it.txt.afs.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.afs.packet.manager.PacketsDbManager;
import it.txt.afs.packet.utils.PacketsDbSearchResult;

import it.txt.afs.packet.Packet;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.HtmlUtils;
import it.txt.general.SessionObjects;

import it.txt.tpms.users.TpmsUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Date;

import java.text.ParseException;


import tpms.TpmsException;
import tpms.utils.UserUtils;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-feb-2006
 * Time: 13.16.32
 * Manage the database queries
 */
public class DatabaseQueryServlet extends AfsGeneralServlet {

    //private static SimpleDateFormat clientSideDateFormatter = new SimpleDateFormat( HtmlUtils.DATE_FORMAT_USER_INPUT, Locale.UK );



    public static final String DESTINATION_PLANT_SESSION_ATTRIBUTE_NAME = "destinationPlantList";
    public static final String FROM_PLANT_SESSION_ATTRIBUTE_NAME = "fromPlantList";
    public static final String FIRST_RECIEVERS_LOGIN_SESSION_ATTRIBUTE_NAME = "firstReceiverLoginList";
    public static final String SECOND_RECIEVERS_LOGIN_SESSION_ATTRIBUTE_NAME = "secondReceiverLoginList";
    public static final String SENDER_LOGIN_SESSION_ATTRIBUTE_NAME = "senderLoginList";
    public static final String EXTRACTION_LOGIN_SESSION_ATTRIBUTE_NAME = "extractionLoginList";
    //value of the action filed that indicates that the user want to
    public static final String SEARCH_PACKETS_ACTION_VALUE = "searchPackets";
    public static final String VIEW_PACKET_DETAILS_ACTION_VALUE = "viewPacketDetails";

    public static final String SEARCH_RESULT_REQUEST_ATTRIBUTE_NAME = "searchResult";
    public static final String PACKET_DETAILS_REQUEST_ATTRIBUTE_NAME = "packetDetails";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = this.getServletConfig().getInitParameter("queryPage");
        String action = request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);
        HttpSession session = request.getSession();
        //todo check db connection...
        if (GeneralStringUtils.isEmptyString(action)) {
            try {
                initializeLists(session);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
        } else if (action.equals(SEARCH_PACKETS_ACTION_VALUE)) {
            PacketsDbSearchResult packetsDbSearchResult = null;
            TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
            if (UserUtils.hasEngineerRole(currentUser.getTpmsLogin()) || UserUtils.hasAfsRole(currentUser.getTpmsLogin())) {
                try {
                    packetsDbSearchResult = PacketsDbManager.searchForPackets(retrieveDbPacketsSearchParameters(request), currentUser.getTpmsLogin());
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
            } else {
                try {
                    packetsDbSearchResult = PacketsDbManager.searchForPackets(retrieveDbPacketsSearchParameters(request));
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
            }
            request.setAttribute(SEARCH_RESULT_REQUEST_ATTRIBUTE_NAME, packetsDbSearchResult);

        } else if (action.equals(VIEW_PACKET_DETAILS_ACTION_VALUE)) {

            try {
                Packet packetDetails = PacketsDbManager.getPacketDataFromDB(request.getParameter(AfsServletUtils.DB_SEARCH_ID_FIELDS));
                request.setAttribute(PACKET_DETAILS_REQUEST_ATTRIBUTE_NAME, packetDetails);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            nextPage = this.getServletConfig().getInitParameter("viewPacketDetails");
        }
        manageRedirect(nextPage, request, response);
    }


    public static String formatDate(Date d) {
        return HtmlUtils.clientSideDateFormatSearchFields.format(d).toUpperCase();
    }


    protected static void initializeLists(HttpSession session) throws TpmsException {
        session.setAttribute(DESTINATION_PLANT_SESSION_ATTRIBUTE_NAME, PacketsDbManager.getDestinationPlantsList());
        session.setAttribute(FROM_PLANT_SESSION_ATTRIBUTE_NAME, PacketsDbManager.getFromPlantsList());
        session.setAttribute(FIRST_RECIEVERS_LOGIN_SESSION_ATTRIBUTE_NAME, PacketsDbManager.getFirstRecieveLoginsList());
        session.setAttribute(SECOND_RECIEVERS_LOGIN_SESSION_ATTRIBUTE_NAME, PacketsDbManager.getSecondRecieveLoginsList());
        session.setAttribute(SENDER_LOGIN_SESSION_ATTRIBUTE_NAME, PacketsDbManager.getSenderLoginsList());
        session.setAttribute(EXTRACTION_LOGIN_SESSION_ATTRIBUTE_NAME, PacketsDbManager.getExtractorsLoginsList());
    }

    protected static Hashtable retrieveDbPacketsSearchParameters(HttpServletRequest request) {

        Hashtable searchParameters = new Hashtable();

        String tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_NAME_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_NAME_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_TP_RELEASE_FIELDS);
        if (!GeneralStringUtils.isEmptyString(request.getParameter(tmpValue))) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_TP_RELEASE_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_TP_REVISION_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_TP_REVISION_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_DESTINATION_PLANT_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_DESTINATION_PLANT_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_FROM_PLANT_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_FROM_PLANT_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_SENDER_LOGIN_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_SENDER_LOGIN_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_SECOND_RECIEVE_LOGIN_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_SECOND_RECIEVE_LOGIN_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_STATUS_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_STATUS_FIELDS, tmpValue);
        }

        tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_EXTRACTION_LOGIN_FIELDS);
        if (!GeneralStringUtils.isEmptyString(tmpValue)) {
            searchParameters.put(AfsServletUtils.DB_SEARCH_EXTRACTION_LOGIN_FIELDS, tmpValue);
        }

        try {
            tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS);
            if (!GeneralStringUtils.isEmptyString(tmpValue)) {
                searchParameters.put(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS, HtmlUtils.clientSideDateFormatSearchFields.parse(tmpValue));
            }
        } catch (ParseException e) {
            errorLog("DatabaseQueryServlet :: retrieveDbPacketsSearchParameters : unable to parse DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS, parameter will not be considered");
        }

        try {
            tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS);
            if (!GeneralStringUtils.isEmptyString(tmpValue)) {
                searchParameters.put(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS, HtmlUtils.clientSideDateFormatSearchFields.parse(tmpValue));
            }
        } catch (ParseException e) {
            errorLog("DatabaseQueryServlet :: retrieveDbPacketsSearchParameters : unable to parse DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS, parameter will not be considered");
        }

        try {
            tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS);
            if (!GeneralStringUtils.isEmptyString(tmpValue)) {
                searchParameters.put(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS, HtmlUtils.clientSideDateFormatSearchFields.parse(tmpValue));
            }
        } catch (ParseException e) {
            errorLog("DatabaseQueryServlet :: retrieveDbPacketsSearchParameters : unable to parse DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS, parameter will not be considered");
        }

        try {
            tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS);
            if (!GeneralStringUtils.isEmptyString(tmpValue)) {
                searchParameters.put(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS, HtmlUtils.clientSideDateFormatSearchFields.parse(tmpValue));
            }
        } catch (ParseException e) {
            errorLog("DatabaseQueryServlet :: retrieveDbPacketsSearchParameters : unable to parse DB_SEARCH_EXTRACTION_DATE_TO_FIELDS, parameter will not be considered");
        }

        try {
            tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS);
            if (!GeneralStringUtils.isEmptyString(tmpValue)) {
                searchParameters.put(AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS, HtmlUtils.clientSideDateFormatSearchFields.parse(tmpValue));
            }
        } catch (ParseException e) {
            errorLog("DatabaseQueryServlet :: retrieveDbPacketsSearchParameters : unable to parse DB_SEARCH_SENT_DATE_FROM_FIELDS, parameter will not be considered");
        }

        try {
            tmpValue = request.getParameter(AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS);
            if (!GeneralStringUtils.isEmptyString(tmpValue)) {
                searchParameters.put(AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS, HtmlUtils.clientSideDateFormatSearchFields.parse(tmpValue));
            }
        } catch (ParseException e) {
            errorLog("DatabaseQueryServlet :: retrieveDbPacketsSearchParameters : unable to parse DB_SEARCH_SENT_DATE_TO_FIELDS, parameter will not be considered");
        }

        debugLog("DatabaseQueryServlet :: retrieveDbPacketsSearchParameters : " + searchParameters.size());


        return searchParameters;
    }
}
