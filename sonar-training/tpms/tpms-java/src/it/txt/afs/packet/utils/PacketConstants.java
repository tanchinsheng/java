package it.txt.afs.packet.utils;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 25-gen-2006
 * Time: 16.46.27
 * To change this template use File | Settings | File Templates.
 */
public class PacketConstants {


    //*************PACKET STAUS VALUES****************//
    public static final String SENT_PACKET_STATUS = "Sent";
    public static final String EXTRACTED_PACKET_STATUS = "Extracted";


    //*************PACKET ATTRIBUTES FOR CS****************//
    public static final String ID_FIELD_NAME = "pck_id";
    public static final String NAME_FIELD_NAME = "pck_name";
    public static final String TP_RELEASE_FIELD_NAME = "tp_release";
    public static final String TP_REVISION_FIELD_NAME = "tp_revision";
    public static final String DESTINATION_PLANT_FIELD_NAME = "destination_plant";
    public static final String FROM_PLANT_FIELD_NAME = "from_plant";
    public static final String SENDER_LOGIN_FIELD_NAME = "sender_login";
    public static final String SENDER_EMAIL_FIELD_NAME = "sender_email";
    public static final String FIRST_RECIEVE_LOGIN_FIELD_NAME = "first_recieve_login";
    public static final String FIRST_RECIEVE_EMAIL_FIELD_NAME = "first_recieve_email";
    public static final String SECOND_RECIEVE_LOGIN_FIELD_NAME = "second_recieve_login";
    public static final String SECOND_RECIEVE_EMAIL_FIELD_NAME = "second_recieve_email";
    public static final String CC_EMAIL_FIELD_NAME = "cc_email";
    public static final String STATUS_FIELD_NAME = "status";
    public static final String VOB_STATUS_FIELD_NAME = "vob_status";
    public static final String SENT_DATE_FIELD_NAME = "sent_date";
    public static final String EXTRACTION_DATE_FIELD_NAME = "extraction_date";
    public static final String EXTRACTION_LOGIN_FIELD_NAME = "extraction_login";
    public static final String LAST_ACTION_DATE_FIELD_NAME = "last_action_date";
    public static final String RECIEVER_COMMENTS_FIELD_NAME = "reciever_comments";
    public static final String SENDER_COMMENTS_FIELD_NAME = "sender_comments";
    public static final String XFERPATH_FIELD_NAME = "xferpath";
    public static final String EXTRACT_PATH_FIELD_NAME = "extract_path";

    //*******************PACKET SESSION OBJECT**************//
    public static final String PACKET_ATTRIBUTE_SESSION_NAME = "sessionPacket";
    //*******************PACKET SESSION OBJECT**************//
    public static final String PACKET_ATTRIBUTE_REQUEST_NAME = "requestPacket";

    //*******************PACKET FORM LABELS **************//
    //TODO mettere le seguenti label in un file di properties...e magari fare una classe dedicata
    public static final String ID_LABEL = "Packet id";
    public static final String NAME_LABEL = "Packet name";
    public static final String TP_RELEASE_LABEL = "Tp release";
    public static final String TP_REVISION_LABEL = "Tp revision";
    public static final String DESTINATION_PLANT_LABEL = "Destination plant";
    public static final String FROM_PLANT_LABEL = "From Plant";
    public static final String RVOB_NAME_LABEL = "R Vob Name";
    public static final String SENDER_LOGIN_LABEL = "Sender login";
    public static final String SENDER_EMAIL_LABEL = "Sender email";
    public static final String FIRST_RECIEVE_LOGIN_LABEL = "First recieve login";
    public static final String FIRST_RECIEVE_EMAIL_LABEL = "First recieve email";
    public static final String SECOND_RECIEVE_LOGIN_LABEL = "Second recieve login";
    public static final String SECOND_RECIEVE_EMAIL_LABEL = "Second recieve email";
    public static final String CC_EMAIL_LABEL = "CC emails";
    public static final String STATUS_LABEL = "Status";
    public static final String VOB_STATUS_LABEL = "Vob status";
    public static final String SENT_DATE_LABEL = "Sent date";
    public static final String EXTRACTION_DATE_LABEL = "Extraction date";
    public static final String EXTRACTION_LOGIN_LABEL = "Extraction login";
    public static final String LAST_ACTION_DATE_LABEL = "Last action date";
    public static final String RECIEVER_COMMENTS_LABEL = "Reciever comments";
    public static final String SENDER_COMMENTS_LABEL = "Sender comments";
    public static final String XFERPATH_LABEL = "Xfer File or Directory";
    public static final String EXTRACTION_PATH_LABEL = "Extraction path";

}
