package it.txt.afs.clearcase.utils;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 26-gen-2006
 * Time: 11.33.45
 * This class contains the constants for the clercase interface section
 */
public class ClearcaseInterfaceCostants {
    /**
     * *******CLEARCASE IN FILE FIELDS***************
     */
    //always present fields: the following fields will be always present in clearcase input file
    public static final String RECORD_TYPE_FIELD = "rectype";
    public static final String ACTION_FIELD = "action";
    public static final String SESSION_ID_FIELD = "session_id";
    public static final String DEBUG_FIELD = "debug";
    public static final String XML_OUT_FILE_FIELD = "outfile";
    public static final String USER_HOME_DIR = "unix_home";
    public static final String USER_GROUP_DIR = "unix_group";

    //send in parameters
    public static final String SEND_ACTION_PACKAGE_ID_FIELD = "pkg_id";
    public static final String SEND_ACTION_PACKAGE_NAME_FIELD = "jobname";
    public static final String SEND_ACTION_TP_RELEASE_FIELD = "release_nb";
    public static final String SEND_ACTION_TP_REVISION_FIELD = "revision_nb";
    public static final String SEND_ACTION_DESTINATION_PLANT_FIELD = "to_plant";
    public static final String SEND_ACTION_FROM_PLANT_FIELD = "from_plant";
    public static final String SEND_ACTION_SENDER_LOGIN_FIELD = "user";
    public static final String SEND_ACTION_FROM_EMAIL_FIELD = "from_mail";
    public static final String SEND_ACTION_FIRST_RECIEVE_LOGIN_FIELD = "valid_login";
    public static final String SEND_ACTION_FIRST_RECIEVE_EMAIL_FIELD = "to_mail";
    public static final String SEND_ACTION_SECOND_RECIEVE_LOGIN_FIELD = "prod_login";
    public static final String SEND_ACTION_SECOND_RECIEVE_EMAIL_FIELD = "to_mail1";
    public static final String SEND_ACTION_CC_EMAIL_FIELD = "cc_mail1";
    public static final String SEND_ACTION_T_VOB_FIELD = "to_vob";
    public static final String SEND_ACTION_T_VOB_NAME_FIELD = "vobname";
    public static final String SEND_ACTION_XFER_PATH_FIELD = "xfer_path";

    //extract in parameters
    public static final String EXTRACT_ACTION_PACKAGE_ID_FIELD = SEND_ACTION_PACKAGE_ID_FIELD;
    public static final String EXTRACT_ACTION_PACKAGE_NAME_FIELD = SEND_ACTION_PACKAGE_NAME_FIELD;
    public static final String EXTRACT_ACTION_EXTRACTION_LOGIN_FIELD = SEND_ACTION_SENDER_LOGIN_FIELD;
    public static final String EXTRACT_ACTION_FROM_EMAIL_FIELD = SEND_ACTION_FROM_EMAIL_FIELD;
    public static final String EXTRACT_ACTION_FIRST_RECIEVE_EMAIL_FIELD = SEND_ACTION_FIRST_RECIEVE_EMAIL_FIELD;
    public static final String EXTRACT_ACTION_SECOND_RECIEVE_EMAIL_FIELD = SEND_ACTION_SECOND_RECIEVE_EMAIL_FIELD;
    public static final String EXTRACT_ACTION_ABSOLUTE_EXTRACT_PATH_FIELD = "work_dir";
    public static final String EXTRACT_ACTION_CC_EMAIL_FIELD = SEND_ACTION_CC_EMAIL_FIELD;
    public static final String EXTRACT_ACTION_R_VOB_NAME_FIELD = SEND_ACTION_T_VOB_NAME_FIELD;
    public static final String EXTRACT_ACTION_TP_RELEASE_FIELD = SEND_ACTION_TP_RELEASE_FIELD;
    public static final String EXTRACT_ACTION_TP_REVISION_FIELD = SEND_ACTION_TP_REVISION_FIELD;

    //query in parameters
    /**
     * filter2
     */
    public static final String QUERY_USER_LOGIN_FIELD = "unixUser";
    public static final String QUERY_FILTER_FIELD = "filter1";
    public static final String QUERY_VOBNAME_FIELD = "vobname";

    /**
     * the following couple of constants represents the values for clearcase interface action and record_type:
     * each action needs one couple (action, record_type)
     */
    //action = send
    public static final String REC_TYPE_ACTION_VALUE = "ftp";
    public static final String SEND_ACTION_VALUE = "ftp_send";
    //action=extract
    public static final String EXTRACT_ACTION_VALUE = "ftp_extract";

    //action=query
    public static final String REC_TYPE_QUERY_VALUE = "ftp";
    public static final String QUERY_ACTION_VALUE = "ftp_query";
    /**********END CLEARCASE IN FILE FIELDS****************/

    /**********CLEARCASE PARSED OUT FILE FIELDS****************/
    /**
     * the following constants represt the possible keys for the Hashtbale obtained from the parsing of the clearcase output file
     */
    public static final String OUT_RESULT = "result";
    public static final String OUT_SYSTEM_MESSAGE = "sys_message";
    public static final String OUT_USER_MESSAGE = "user_message";
    public static final String OUT_TRACK_MESSAGE = "track_message";
    /**
     * The following cstants represnts the possible values for the OUT_RESULT attribute
     */
    public static final String OUT_RESULT_OK_VALUE = "ok";
    public static final String OUT_RESULT_KO_VALUE = "ko";
    /**********END CLEARCASE PARSED OUT FILE FIELDS****************/

    /**********CLEARCASE COMMANDS RESULTS****************/
    /**
     * All the methods that have to check or invoke a clearcase result MUST return one of the following costants.
     */
    public static final int COMMAND_OK_RESULT = 0; // the clearcase command execution was ok
    public static final int COMMAND_KO_RESULT = -1;// the clearcase command execution was ko
    public static final int COMMAND_UNKNOW_RESULT = -2;// the clearcase command execution was unknow: i.e. batch execution
    /**********END CLEARCASE COMMANDS RESULTS****************/

    //*******************PACKET ACTION XML OUTPUT **************//
    /**
     * FILE ELEMENTS/TAGS
     */
    public static final String SENT_XMLOUT_SEND_ROOT_ELEMENT = "REP";
    public static final String SENT_XMLOUT_PACKET_ELEMENT = "PKG";
    public static final String SENT_XMLOUT_LABEL_ELEMENT = "LABEL";
    public static final String SENT_XMLOUT_ID_ELEMENT = "ID";
    public static final String SENT_XMLOUT_NAME_ELEMENT = "NAME";
    public static final String SENT_XMLOUT_SENTDATE_ELEMENT = "DATE";
    /**
     * Parsed xml out file key values
     */
    public static final String PARSED_SENT_XMLOUT_LABEL_KEY = "LABEL";
    public static final String PARSED_SENT_XMLOUT_ID_KEY = "ID";
    public static final String PARSED_SENT_XMLOUT_NAME_KEY = "NAME";
    public static final String PARSED_SENT_XMLOUT_SENTDATE_KEY = "DATE";

    /**
     * Query xml out file fields
     */
    public static final String QUERY_XML_OUT_FIELD_PACKET_LIST = "PACKET_LIST";
    public static final String QUERY_XML_OUT_FIELD_PACKET = "PACKET";
    public static final String QUERY_XML_OUT_FIELD_PKG_LABEL = "PKG_LABEL";
    public static final String QUERY_XML_OUT_FIELD_NAME = "NAME";
    public static final String QUERY_XML_OUT_FIELD_ID = "ID";
    public static final String QUERY_XML_OUT_FIELD_TP_RELEASE = "TP_RELEASE";
    public static final String QUERY_XML_OUT_FIELD_TP_REVISION = "TP_REVISION";
    public static final String QUERY_XML_OUT_FIELD_DESTINATION_PLANT = "DESTINATION_PLANT";
    public static final String QUERY_XML_OUT_FIELD_FROM_PLANT = "FROM_PLANT";
    public static final String QUERY_XML_OUT_FIELD_SENDER_LOGIN = "SENDER_LOGIN";
    public static final String QUERY_XML_OUT_FIELD_SENDER_EMAIL = "SENDER_EMAIL";
    public static final String QUERY_XML_OUT_FIELD_FIRST_RECIEVE_LOGIN = "FIRST_RECIEVE_LOGIN";
    public static final String QUERY_XML_OUT_FIELD_FIRST_RECIEVE_EMAIL = "FIRST_RECIEVE_EMAIL";
    public static final String QUERY_XML_OUT_FIELD_SECOND_RECIEVE_LOGIN = "SECOND_RECIEVE_LOGIN";
    public static final String QUERY_XML_OUT_FIELD_SECOND_RECIEVE_EMAIL = "SECOND_RECIEVE_EMAIL";
    public static final String QUERY_XML_OUT_FIELD_CC_EMAIL = "CC_EMAIL";
    public static final String QUERY_XML_OUT_FIELD_STATUS = "STATUS";
    public static final String QUERY_XML_OUT_FIELD_LAST_ACTION_DATE = "LAST_ACTION_DATE";
    public static final String QUERY_XML_OUT_FIELD_SENT_DATE = "SENT_DATE";
    public static final String QUERY_XML_OUT_FIELD_SENT_ACTOR = "SENT_ACTOR";
    public static final String QUERY_XML_OUT_FIELD_EXTRACTION_DATE = "EXTRACTION_DATE";
    public static final String QUERY_XML_OUT_FIELD_EXTRACTION_LOGIN = "EXTRACTION_LOGIN";
    public static final String QUERY_XML_OUT_FIELD_XFER_PATH = "XFER_PATH";


    /**
     * *************XML OUTPUT FORMATS******************
     */
    //2006.01.31-15.26.35
    public static final String XMLOUT_DATE_FORMAT = "y.M.d-h.m.s";
}
