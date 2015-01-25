package it.txt.afs.servlets.master;


import it.txt.general.utils.GeneralStringUtils;
import it.txt.afs.AfsCommonStaticClass;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 31-gen-2006
 * Time: 13.13.07
 * To change this template use File | Settings | File Templates.
 */
public class AfsServletUtils extends AfsCommonStaticClass {
    //manage error page
    public static final String ERROR_PAGE = "caughtErr.jsp";
    //this is the name of the field that is used by the wait page to maintain the request id (in order to look for out file)... servlets set this field in Request attribues, jsp set this field in parameters
    public static final String CHECK_RESULT_REQUEST_NAME = "checkResult";
    //this is the name of the field that is used by servlets in order to identify the user action
    public static final String ACTION_FIELD_NAME = "action";

    public static final String USER_DATE_FORMAT = tpmsConfiguration.getAfsDatesFormat();

    /**
     * **********SESSION ATTRIBUTES NAME*****************************
     */
    //name of session attribute containing the Current seleced T vob...
    public static final String SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME = "selectedAfsUserTVob";
    //name of session attribute containing the Current seleced R vob...
    public static final String SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME = "selectedAfsUserRVob";

    /**
     * **********REQUEST ATTRIBUTES NAME*****************************
     */
    //name of request attribute containing the packet list that should be presented.
    public static final String QUERY_PACKET_LIST_REQUEST_ATTRIBUTE_NAME = "queryResultingPacketList";
    //name of request attribute/parameter containing the requestId that permits the identification of the xml out file.
    public static final String REQUEST_ID_REQUEST_ATTRIBUTE_NAME = "requestId";
    //This is the name of the wait object stored in request attributes...
    public static final String WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME = "wait_informations";

    /**
     * **********DB PACKETS SEARCH ATTRIBUTES *****************************
     */


    public static final String DB_SEARCH_ID_FIELDS = "ID";
    public static final String DB_SEARCH_NAME_FIELDS = "NAME";
    public static final String DB_SEARCH_TP_RELEASE_FIELDS = "TP_RELEASE";
    public static final String DB_SEARCH_TP_REVISION_FIELDS = "TP_REVISION";
    public static final String DB_SEARCH_DESTINATION_PLANT_FIELDS = "DESTINATION_PLANT";
    public static final String DB_SEARCH_FROM_PLANT_FIELDS = "FROM_PLANT";
    public static final String DB_SEARCH_SENDER_LOGIN_FIELDS = "SENDER_LOGIN";
    public static final String DB_SEARCH_FIRST_RECIEVE_LOGIN_FIELDS = "FIRST_RECIEVE_LOGIN";
    public static final String DB_SEARCH_SECOND_RECIEVE_LOGIN_FIELDS = "SECOND_RECIEVE_LOGIN";
    public static final String DB_SEARCH_STATUS_FIELDS = "STATUS";
    public static final String DB_SEARCH_EXTRACTION_LOGIN_FIELDS = "EXTRACTION_LOGIN";

    public static final String DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS = "EXTRACTION_DATE_FROM";
    public static final String DB_SEARCH_EXTRACTION_DATE_TO_FIELDS = "EXTRACTION_DATE_TO";

    public static final String DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS = "LAST_ACTION_DATE_FROM";
    public static final String DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS = "LAST_ACTION_DATE_TO";

    public static final String DB_SEARCH_SENT_DATE_FROM_FIELDS = "SENT_DATE_FROM";
    public static final String DB_SEARCH_SENT_DATE_TO_FIELDS = "SENT_DATE_TO";


    /**
     * @param request
     * @return true if the wait message should be displayed, false otherwise
     */
    public static boolean showWaitMessage(HttpServletRequest request) {
        return request.getAttribute(WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME) != null;
    }

    public static Object getRequestAttributeParameter(HttpServletRequest request, String attributeName) {
        Object obj = null;
        if (!GeneralStringUtils.isEmptyString(attributeName)) {
            obj = request.getAttribute(attributeName);
            if (obj == null) {
                obj = request.getParameter(attributeName);
            }
        }
        return obj;
    }

    public static String printHiddenField(String fieldName, String fieldValue) {
        return "<input type=\"hidden\" name=\"" + fieldName + "\" value=\"" + fieldValue + "\">";
    }

}
