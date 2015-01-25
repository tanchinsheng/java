package it.txt.afs.packet.manager;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.afs.clearcase.ClearcaseInterface;
import it.txt.afs.clearcase.utils.ClearcaseInterfaceCostants;
import it.txt.afs.clearcase.utils.ClearcaseInterfaceUtils;
import it.txt.afs.packet.Packet;
import it.txt.afs.packet.PacketList;
import it.txt.afs.packet.utils.PacketConstants;
import it.txt.afs.packet.utils.PacketQueryResult;
import it.txt.general.utils.GeneralStringUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tpms.TpmsException;
import tpms.VobManager;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;


/**
 * User: furgiuele
 * Date: 18-gen-2006
 * Time: 13.35.37
 * <p>
 * this class is the manager for packages in order to send, re-send, retrieve list and so on....
 * </p>
 */

public class PacketsVobManager extends AfsCommonStaticClass {
    /**
     * checks if the given packet own all the needed attributes to be sent.
     *
     * @param packet
     * @throws TpmsException
     */
    public static void checkSendPacketMandatoryAttributes(Packet packet) throws TpmsException {
        String missingAttributes = "";


        if (GeneralStringUtils.isEmptyString(packet.getName())) {
            missingAttributes += PacketConstants.NAME_LABEL;
        }

        if (GeneralStringUtils.isEmptyString(packet.getId())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.ID_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.ID_LABEL;
            }
        }

        if (GeneralStringUtils.isEmptyString(packet.getFromPlant())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.FROM_PLANT_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.FROM_PLANT_LABEL;
            }
        }


        if (GeneralStringUtils.isEmptyString(packet.getDestinationPlant())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.DESTINATION_PLANT_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.DESTINATION_PLANT_LABEL;
            }
        }

        if (GeneralStringUtils.isEmptyString(packet.getFirstRecieveLogin())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.FIRST_RECIEVE_LOGIN_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.FIRST_RECIEVE_LOGIN_LABEL;
            }
        }

        if (GeneralStringUtils.isEmptyString(packet.getFirstRecieveEmail())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.FIRST_RECIEVE_EMAIL_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.FIRST_RECIEVE_EMAIL_LABEL;
            }
        }
        /**
        if (GeneralStringUtils.isEmptyString(packet.getSecondRecieveLogin())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.SECOND_RECIEVE_LOGIN_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.SECOND_RECIEVE_LOGIN_LABEL;
            }
        }

        if (GeneralStringUtils.isEmptyString(packet.getSecondRecieveEmail())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.SECOND_RECIEVE_EMAIL_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.SECOND_RECIEVE_EMAIL_LABEL;
            }
        }
        **/
        if (GeneralStringUtils.isEmptyString(packet.getXferPath())) {
            if (GeneralStringUtils.isEmptyString(missingAttributes)) {
                missingAttributes = PacketConstants.XFERPATH_LABEL;
            } else {
                missingAttributes += ", " + PacketConstants.XFERPATH_LABEL;
            }
        }

        //if there is at least one missing attribute An exception will be raised
        if (!GeneralStringUtils.isEmptyString(missingAttributes)) {
            debugLog("PacketsVobManager :: checkSendPacketMandatoryAttributes NON passato " + missingAttributes);
            throw new TpmsException("Send packet fail: missing mandatory attributes!", "packet_send", "the missing attributes are " + missingAttributes);
        }
    }


    /**
     * will send the given packet
     *
     * @param packet
     * @param sessionId
     * @param requestId the id that identify the request.
     * @return the clearcase output file which will contain the result of clearcase operation
     * @throws TpmsException
     */
    public static String sendPacket(Packet packet, String sessionId, String requestId) throws TpmsException {
        String clearcaseOutputFilePath;
        if (packet != null) {

            debugLog("PacketsVobManager :: sendPacket : check packet attributes ...");
            checkSendPacketMandatoryAttributes(packet);
            debugLog("PacketsVobManager :: sendPacket : packet sending started ...");

            Vob afsTVobData;
            try {
                afsTVobData = VobManager.searchTVobByDestinationPlant(packet.getDestinationPlant());
            } catch (Exception e) {
                String msg = "Unable to retrieve " + VobManager.T_VOB_TYPE_FLAG_VALUE + " information for " + packet.getDestinationPlant();
                errorLog("PacketsVobManager :: sendPacket : " + msg, e);
                throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, e);
            }


            Hashtable inFileParameters = ClearcaseInterfaceUtils.initInFileParameterHashTable(ClearcaseInterfaceCostants.SEND_ACTION_VALUE,
                    ClearcaseInterfaceCostants.REC_TYPE_ACTION_VALUE,
                    requestId, packet.getSenderLogin());

            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_T_VOB_FIELD, afsTVobData.getName());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_T_VOB_NAME_FIELD, afsTVobData.getName());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_PACKAGE_ID_FIELD, packet.getId());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_PACKAGE_NAME_FIELD, packet.getName());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_TP_RELEASE_FIELD, packet.getTpRelease());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_TP_REVISION_FIELD, packet.getTpRevision());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_DESTINATION_PLANT_FIELD, packet.getDestinationPlant());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_FROM_PLANT_FIELD, packet.getFromPlant());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_SENDER_LOGIN_FIELD, packet.getSenderLogin());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_FROM_EMAIL_FIELD, packet.getSenderEmail());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_FIRST_RECIEVE_LOGIN_FIELD, packet.getFirstRecieveLogin());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_FIRST_RECIEVE_EMAIL_FIELD, packet.getFirstRecieveEmail());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_SECOND_RECIEVE_LOGIN_FIELD, packet.getSecondRecieveLogin());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_SECOND_RECIEVE_EMAIL_FIELD, packet.getSecondRecieveEmail());
            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_CC_EMAIL_FIELD, packet.getCcEmail());
            debugLog("PacketsVobManager :: sendPacket : absolute XFerPath = " + packet.getXferPath() + " relative XFerPath = " +GeneralStringUtils.replaceAllIgnoreCase(packet.getXferPath(), UserUtils.getUserUnixHome(packet.getSenderLogin()) + File.separator, "") );

            inFileParameters.put(ClearcaseInterfaceCostants.SEND_ACTION_XFER_PATH_FIELD, GeneralStringUtils.replaceAllIgnoreCase(packet.getXferPath(), UserUtils.getUserUnixHome(packet.getSenderLogin()) + File.separator, ""));

            String clearcaseInputFilePath = ClearcaseInterfaceUtils.buildInFilePath(requestId);

            clearcaseOutputFilePath = ClearcaseInterfaceUtils.buildOutFilePath(requestId);

            try {
                ClearcaseInterfaceUtils.writeInBackEndFile(inFileParameters, clearcaseInputFilePath);
            } catch (IOException e) {
                String msg = "Unable to produce in file (" + clearcaseInputFilePath + ")";
                errorLog("PacketsVobManager :: sendPacket : " + msg, e);
                throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, e);
            }

            try {
                ClearcaseInterface.invokeCommand(UserUtils.getUserUnixLogin(packet.getSenderLogin()), clearcaseInputFilePath, clearcaseOutputFilePath, sessionId);
            } catch (IOException e) {
                String msg = "Unable to call clearcase statement";
                errorLog("PacketsVobManager :: sendPacket : " + msg, e);
                throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, e);
            }


        } else {
            String msg = "Given packet is null!!!";
            errorLog("PacketsVobManager :: sendPacket : " + msg);
            throw new TpmsException(msg, "Send packet");
        }

        return clearcaseOutputFilePath;
    }

    /**
     * check if is present the output file for the operation identified by the requestId:
     * If yes parse it in order to:
     * track the action in the database and identify the operation result
     * insert packet informations in the database
     * that could be:
     * <li>COMMAND_OK_RESULT the clearcase command execution was ok
     * <li>COMMAND_KO_RESULT the clearcase command execution was ko
     * <li>COMMAND_UNKNOW_RESULT the clearcase command execution was unknow: the out file is not still present.
     *
     * @param requestId        the id that idenfy the requested operation
     * @param sessionId        the session id of the current user
     * @param currentUserLogin the current user login
     * @return <li>COMMAND_OK_RESULT the clearcase command execution was ok
     *         <li>COMMAND_UNKNOW_RESULT the clearcase command execution was unknow: the out file is not still present.
     * @throws TpmsException if an error occurs or if the clearcase command ended with error...
     */
    public static int checkSendCommandResult(String requestId, String sessionId, String currentUserLogin, Packet packet) throws TpmsException {
        debugLog("ClearcaseInterface :: checkSendCommandResult : started....");
        int result = ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT;
        String outFilePath = ClearcaseInterfaceUtils.buildOutFilePath(requestId);
        File outFile = new File(outFilePath);
        Hashtable outFileContent;

        if (outFile.exists()) {
            debugLog("ClearcaseInterface :: checkSendCommandResult : outFile found " + outFilePath);
            //se il file esiste avvio la procedura per tracciarlo e per verificare il riusltato dell'operazione
            String unixUser = UserUtils.getUserUnixLogin(currentUserLogin);
            try {
                outFileContent = ClearcaseInterface.parseOutFile(new File(outFilePath), unixUser, sessionId);
            } catch (IOException e) {
                String msg = "There was an error parsing clearcase output file";
                errorLog("ClearcaseInterface :: checkSendCommandResult : requestId = " + requestId + " " + msg + " exception message = " + e.getMessage(), e);
                throw new TpmsException(msg, "outFilePath = " + outFilePath, e);
            }


            String ccCommandResult = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_RESULT);

            if (ccCommandResult.equals(ClearcaseInterfaceCostants.OUT_RESULT_OK_VALUE)) {
                // if the send action has ok result...
                result = ClearcaseInterfaceCostants.COMMAND_OK_RESULT;
                //insert packet data into database complating packet data with cc xml return info
                //parsing cc xml out file
                Hashtable xmlOutContent;
                try {
                    xmlOutContent = ClearcaseInterfaceUtils.parseSentOrExtractedXmlOutFile(requestId);
                } catch (ParserConfigurationException e) {
                    String msg = "PacketsVobManager :: checkSendCommandResult: ParserConfigurationException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, e);
                } catch (SAXException e) {
                    String msg = "PacketsVobManager :: checkSendCommandResult: SAXException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, e);
                } catch (IOException e) {
                    String msg = "PacketsVobManager :: checkSendCommandResult: IOException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, e);
                }
                //Check if the parsed xml file is related to the packet that we are maanging...
                if (tpmsConfiguration.getDvlBool()) {
                    //if is a developing installation bypass the checks...
                    packet.setSentDate((Date) xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_SENTDATE_ELEMENT));
                    packet.setLastActionDate(packet.getSentDate());
                    packet.setStatus(PacketConstants.SENT_PACKET_STATUS);
                } else {
                    //its a real installation....
                    if (packet.getId().equals(xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_ID_ELEMENT))) {
                        packet.setSentDate((Date) xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_SENTDATE_ELEMENT));
                        packet.setLastActionDate(packet.getSentDate());
                        packet.setStatus(PacketConstants.SENT_PACKET_STATUS);
                    } else {
                        String msg = "PacketsVobManager :: checkSendCommandResult : the resulting clearcase xml out file is not related to the current sent package!!!";
                        String detailedMsg = "Current packet id = " + packet.getId() + " xml packet if = " + xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_ID_ELEMENT) + " out xml path = " + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId);
                        throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, detailedMsg);
                    }
                }

                //insert the populated packet into database
                try {
                    PacketsDbManager.dbInsertPacketData(packet, sessionId, packet.getSenderLogin());
                } catch (TpmsException e) {

                    errorLog("PacketsVobManager :: checkSendCommandResult : error inserting data into database, no data inserted", e);
                }

            } else {
                // if the send action to cc has ko result...
                String userMsg = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_USER_MESSAGE);
                String sysMsg = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_SYSTEM_MESSAGE);
                //there was an error in clearcase operation so reaise an exception....
                errorLog("ClearcaseInterface :: checkSendCommandResult : error from cc command user message = " + userMsg + "*** system message = " + sysMsg);
                throw new TpmsException(userMsg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, sysMsg);
            }

        }

        debugLog("ClearcaseInterface :: checkSendCommandResult : ended result = " + result);
        return result;
    }


    /**
     * ************* METHODS TO INVOKE CLEARCASE QUERIES *********************
     */

    /**
     * this method invokes the clearcase query action for packets
     * @param requestId the request id that uniquely identify the query action
     * @param userLogin the user that has requested the query execution
     * @param sessionId the session id of the user that has requested the query action
     * @param packetsStatus the status of packets that should be retrived
     * @param vobName the name of the vob that should queried
     * @throws TpmsException in case of errors...
     */


    protected static void queryPackets(String requestId, String userLogin, String sessionId, String packetsStatus, String vobName) throws TpmsException {
        debugLog("PacketsVobManager :: queryPackets : start param = " + requestId + " " + userLogin + " " + sessionId + " " + packetsStatus + vobName);
        if (GeneralStringUtils.isEmptyString(requestId) || GeneralStringUtils.isEmptyString(userLogin) || GeneralStringUtils.isEmptyString(sessionId) || GeneralStringUtils.isEmptyString(packetsStatus)) {
            throw new TpmsException("PacketsVobManager :: queryPackets : input parameter empty or null ", "( requestId = " + requestId + ", userLogin = " + userLogin + ", sessionId = " + sessionId + ", packetsStatus = " + packetsStatus + ")");
        }
        //gathering in file parameters....
        Hashtable inFileParameters = ClearcaseInterfaceUtils.initInFileParameterHashTable(ClearcaseInterfaceCostants.QUERY_ACTION_VALUE,
                ClearcaseInterfaceCostants.REC_TYPE_QUERY_VALUE,
                requestId,
                userLogin);
        inFileParameters.put(ClearcaseInterfaceCostants.QUERY_FILTER_FIELD, packetsStatus);
        inFileParameters.put(ClearcaseInterfaceCostants.QUERY_VOBNAME_FIELD, vobName);
        inFileParameters.put(ClearcaseInterfaceCostants.QUERY_USER_LOGIN_FIELD, UserUtils.getUserUnixLogin(userLogin));
        //gathering in/out file names with path...
        String clearcaseInputFilePath = ClearcaseInterfaceUtils.buildInFilePath(requestId);
        String clearcaseOutputFilePath = ClearcaseInterfaceUtils.buildOutFilePath(requestId);

        debugLog("PacketsVobManager :: queryPackets : clearcaseInputFilePath = " + clearcaseInputFilePath + " clearcaseOutputFilePath = " + clearcaseOutputFilePath);

        try {
            //write the back-end in file ...
            ClearcaseInterfaceUtils.writeInBackEndFile(inFileParameters, clearcaseInputFilePath);
        } catch (IOException e) {
            String msg = "Unable to produce in file (" + clearcaseInputFilePath + ")";
            errorLog("PacketsVobManager :: queryPackets : " + msg, e);
            throw new TpmsException(msg, ClearcaseInterfaceCostants.QUERY_ACTION_VALUE, e);
        }

        debugLog("PacketsVobManager :: queryPackets : input file writed!!");


        try {
            //call the be command...
            ClearcaseInterface.invokeCommand(UserUtils.getUserUnixLogin(userLogin), clearcaseInputFilePath, clearcaseOutputFilePath, sessionId);
        } catch (Exception e) {
            String msg = "Unable to call clearcase statement";
            errorLog("PacketsVobManager :: queryPackets : " + msg, e);
            throw new TpmsException(msg, ClearcaseInterfaceCostants.SEND_ACTION_VALUE, e);
        }

        debugLog("PacketsVobManager :: queryPackets : clearcase command invocation ended...");

    }

    /**
     * this method invokes q eury in order to retrieve pathe packets sent by ownerLogin that are in sent status
     * @param requestId the request id that uniquely identify the query action the
     * @param ownerLogin the user that has requested the query execution
     * @param sessionId the session id of the user that has requested the query action
     * @param vobName the name of the vob that should queried
     * @throws TpmsException in case of errors...
     */
    public static void queryOwnedSentPackets(String requestId, String ownerLogin, String sessionId, String vobName) throws TpmsException {
        queryPackets(requestId, ownerLogin, sessionId, PacketConstants.SENT_PACKET_STATUS, vobName);
    }

    /**
     * this method invokes q eury in order to retrieve pathe packets sent by ownerLogin that are in extracted status
     * @param requestId the request id that uniquely identify the query action the
     * @param ownerLogin the user that has requested the query execution
     * @param sessionId the session id of the user that has requested the query action
     * @param vobName the name of the vob that should queried
     * @throws TpmsException in case of errors...
     */
    public static void queryOwnedExtractedPackets(String requestId, String ownerLogin, String sessionId, String vobName) throws TpmsException {
        queryPackets(requestId, ownerLogin, sessionId, PacketConstants.EXTRACTED_PACKET_STATUS, vobName);
    }

     /**
     * this method invokes q eury in order to retrieve pathe packets recieved by recieverLogin that are in sent status
     * @param requestId the request id that uniquely identify the query action the
     * @param recieverLogin the user that has requested the query execution
     * @param sessionId the session id of the user that has requested the query action
     * @param vobName the name of the vob that should queried
     * @throws TpmsException in case of errors...
     */
    public static void queryRecievedExtractedPackets(String requestId, String recieverLogin, String sessionId, String vobName) throws TpmsException {
        queryPackets(requestId, recieverLogin, sessionId, PacketConstants.EXTRACTED_PACKET_STATUS, vobName);
    }

     /**
     * this method invokes q eury in order to retrieve pathe packets recieved by recieverLogin that are in extracted status
     * @param requestId the request id that uniquely identify the query action the
     * @param recieverLogin the user that has requested the query execution
     * @param sessionId the session id of the user that has requested the query action
     * @param vobName the name of the vob that should queried
     * @throws TpmsException in case of errors...
     */
    public static void queryRecievedSentPackets(String requestId, String recieverLogin, String sessionId, String vobName) throws TpmsException {
        queryPackets(requestId, recieverLogin, sessionId, PacketConstants.SENT_PACKET_STATUS, vobName);
    }

    /**
     * ************* METHODS TO PARSE QUERY XML RESULT *********************
     */


    /**
     * This mathod check if the out file is present: if yes do all the needed actions:
     * check the out file for the cc action result, if the result id ok track the action & parse the result xml file.
     *
     * @param login     for filtering purposes
     * @param owned     for filtering purposes
     * @param requestId to identiiy the right out file
     * @return true if the out file was found false otherwise.
     * @throws TpmsException
     */
    protected static PacketQueryResult queryPacketsParseResult(String login, boolean owned, String requestId, String sessionId) throws TpmsException {


        boolean isPresentClearcaseResult = false;
        PacketList packetList = null;
        String outFilePath = ClearcaseInterfaceUtils.buildOutFilePath(requestId);

        File outFile = new File(outFilePath);
        if (outFile.exists()) {
            //the isPresentClearcaseResult is present
            isPresentClearcaseResult = true;
            debugLog("PacketsVobManager :: queryPacketsParseResult : out file found, start parsing ...");
            // and parse the outFile...
            Hashtable outFileContent;
            debugLog("PacketsVobManager :: queryPacketsParseResult : outFile found " + outFilePath);
            //se il file esiste avvio la procedura per tracciarlo e per verificare il riusltato dell'operazione
            String unixUser = UserUtils.getUserUnixLogin(login);
            try {
                outFileContent = ClearcaseInterface.parseOutFile(new File(outFilePath), unixUser, sessionId);
            } catch (IOException e) {
                String msg = "There was an error parsing clearcase output file ";
                errorLog("PacketsVobManager :: queryPacketsParseResult : " + outFilePath + " requestId = " + requestId + " " + msg, e);
                throw new TpmsException(msg, "outFilePath = " + outFilePath, e);
            }
            debugLog("PacketsVobManager :: queryPacketsParseResult : outFile parsing finished...");
            //look for command isPresentClearcaseResult...
            String ccCommandResult = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_RESULT);
            if (ccCommandResult.equals(ClearcaseInterfaceCostants.OUT_RESULT_OK_VALUE)) {
                debugLog("PacketsVobManager :: queryPacketsParseResult : ok isPresentClearcaseResult.");
                //if the cc command was ok...
                //initialize the packet List
                try {
                    packetList = ClearcaseInterfaceUtils.parseAndFilterQueryXmlOutFile(requestId, owned, login);
                    debugLog("PacketsVobManager :: queryPacketsParseResult : xml out file parsed " + ((packetList == null) ? " empty isPresentClearcaseResult " : "" + packetList.size() + " packets"));
                } catch (ParserConfigurationException e) {
                    String msg = "PacketsVobManager :: queryPacketsParseResult: ParserConfigurationException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, "query packet", e);
                } catch (SAXException e) {
                    String msg = "PacketsVobManager :: queryPacketsParseResult: SAXException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, "query packet", e);
                } catch (IOException e) {
                    String msg = "PacketsVobManager :: queryPacketsParseResult: IOException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, "query packet", e);
                }
            } else {
                //if the send action to cc has ko isPresentClearcaseResult...
                String userMsg = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_USER_MESSAGE);
                String sysMsg = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_SYSTEM_MESSAGE);
                //there was an error in clearcase operation so reaise an exception....
                errorLog("PacketsVobManager :: queryPacketsParseResult : error from cc command user message = " + userMsg + "*** system message = " + sysMsg);
                throw new TpmsException(userMsg, "", sysMsg);
            }
        }


        return new PacketQueryResult(isPresentClearcaseResult, packetList);
    }

    /**
     * This mathod check if the cc out file is present: if yes do all the needed actions:
     * check the out file for the cc action result, if the result id ok track the action & parse the result xml file.
     * The packet list will be populated with the packet in sent status where the sender is the identified by login
     *
     * @param ownerLogin
     * @param requestId
     * @param sessionId
     * @return the owned sent packages list
     * @throws TpmsException if an error occours
     */
    public static PacketQueryResult ownedSentPacketsParseResult(String ownerLogin, String requestId, String sessionId) throws TpmsException {
        return queryPacketsParseResult(ownerLogin, true, requestId, sessionId);
    }

    /**
     * This mathod check if the cc out file is present: if yes do all the needed actions:
     * check the out file for the cc action result, if the result id ok track the action & parse the result xml file.
     * The packet list will be populated with the packet in extracted status where the sender is the identified by login
     *
     * @param ownerLogin
     * @param requestId
     * @param sessionId
     * @return the owned extracted packages list
     * @throws TpmsException TpmsException if an error occours
     */
    public static PacketQueryResult ownedExtractedPacketsParseResult(String ownerLogin, String requestId, String sessionId) throws TpmsException {
        return queryPacketsParseResult(ownerLogin, true, requestId, sessionId);
    }

    /**
     * This mathod check if the cc out file is present: if yes do all the needed actions:
     * check the out file for the cc action result, if the result is ok track the action & parse the result xml file.
     * The packet list will be populated with the packet in sent status where the recievrLogin is equal to first_recieve_login or to second_recieve_login
     *
     * @param recieverLogin
     * @param requestId
     * @param sessionId
     * @return the recieved packages list
     * @throws TpmsException if an error occours
     */

    public static PacketQueryResult recievedSentPacketsParseResult(String recieverLogin, String requestId, String sessionId) throws TpmsException {
        return queryPacketsParseResult(recieverLogin, false, requestId, sessionId);
    }


    /**
     * This mathod check if the cc out file is present: if yes do all the needed actions:
     * check the out file for the cc action result, if the result is ok track the action & parse the result xml file.
     * The packet list will be populated with the packet in extracted status where the recieverLogin is equal to extraction_login
     *
     * @param recieverLogin
     * @param requestId
     * @param sessionId
     * @return the extracted sent packages list
     * @throws TpmsException if an error occours
     */

    public static PacketQueryResult recievedExtractedPacketsParseResult(String recieverLogin, String requestId, String sessionId) throws TpmsException {
        //here the unix user will be retrieved and passed to the queryPacketsParseResult() because the extract action MUST contain the unix user in order to be albe
        //to perform the action.
        return queryPacketsParseResult(UserUtils.getUserUnixLogin(recieverLogin), false, requestId, sessionId);
    }

    /**
     * look for packet.
     *
     * @param requestId used to identify the xml where look for packet.
     * @param packetId  the id of the packet to look for
     * @return a packet object idenfied by packetId, null if the packet was not found
     * @throws TpmsException if an error occours
     */
    public static Packet getPacketDetails(String requestId, String packetId) throws TpmsException {
        //verify if the input parameters are good... otherwise there is no way to proceed.
        if (GeneralStringUtils.isEmptyString(requestId)) {
            String msg = "Unable to identify the packet data";
            String detailedMsg = "PacketsVobManager :: getPacketDetails : unable to determinate the xml file the requestId is null or empty (packetId = " + packetId + ")";
            throw new TpmsException(msg, "", detailedMsg);
        }

        if (GeneralStringUtils.isEmptyString(packetId)) {
            String msg = "Unable to identify the packet data";
            String detailedMsg = "PacketsVobManager :: getPacketDetails : unable to determinate the packet the packetId is null or empty (requestId = " + requestId + ")";
            throw new TpmsException(msg, "", detailedMsg);
        }
        debugLog("PacketsVobManager :: getPacketDetails : cheking phase passed, start processing");
        //if input parameters are good let's proceed...
        String xmlOutFilePath = ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId);
        File xmlOutFile;
        if (tpmsConfiguration.getDvlBool()) {
            xmlOutFile = new File("D:\\bat\\ccSimulator\\query_sample_xml_out_file.xml");
        } else {
            xmlOutFile = new File(xmlOutFilePath);
        }

        Packet packet = null;

        if (xmlOutFile.exists()) {

            Element xmlPacketData;
            try {
                xmlPacketData = ClearcaseInterfaceUtils.lookUpPacketInXmlOutFileByPacketId(xmlOutFile, packetId);
            } catch (ParserConfigurationException e) {
                String msg = "PacketsVobManager :: getPacketDetails: ParserConfigurationException Unable to parse xmlOutFile (" + xmlOutFilePath + ")";
                errorLog(msg, e);
                throw new TpmsException(msg, "", e);
            } catch (SAXException e) {
                String msg = "PacketsVobManager :: getPacketDetails: SAXException Unable to parse xmlOutFile (" + xmlOutFilePath + ")";
                errorLog(msg, e);
                throw new TpmsException(msg, "", e);
            } catch (IOException e) {
                String msg = "PacketsVobManager :: getPacketDetails: IOException Unable to parse xmlOutFile (" + xmlOutFilePath + ")";
                errorLog(msg, e);
                throw new TpmsException(msg, "", e);
            }
            if (xmlPacketData != null) {
                packet = ClearcaseInterfaceUtils.getPacketObjetFromXmlPacketData(xmlPacketData);
            } else {
                debugLog("PacketsVobManager :: getPacketDetails: xmlPacketData is null");
            }
        } else {
            //if the out xml data file does not exists... the process can't continue
            String msg = "Unable to identify the packet data";
            String detailedMsg = "PacketsVobManager :: getPacketDetails : unable to find xml data file (requestId = " + requestId + ", packetId = " + packetId + ", xmlFile = " + xmlOutFilePath + ")";
            throw new TpmsException(msg, "", detailedMsg);
        }
        return packet;
    }

    /**
     * This method invokes the extract clearcase action...
     * @param packet the packet tobe extracted
     * @param extractionPath where extract the package
     * @param sessionId the surrent session id
     * @param requestId the current request id
     * @param extractionLogin the user that will perform the extraction
     * @param afsRVobName recive vob that contains the package to be extracted
     * @param administratorUnixLogin the administration login used because the operation on R-VOB must be performet as admin
     * @return the clearcase filepath name that is used to understood when the cc action is terminated
     * @throws TpmsException in case of errors...
     */
    public static String extractPacket(Packet packet, String extractionPath, String sessionId, String requestId, String extractionLogin, String afsRVobName, String administratorUnixLogin) throws TpmsException {
        //do needed checks on input parameters....
        if (packet == null) {
            String msg = "Unable to extract packet";
            String detailedMsg = "PacketsVobManager :: extractPacket the input packet is null! ( extractionPath = " + extractionPath + ")";
            throw new TpmsException(msg, "", detailedMsg);
        }

        if (GeneralStringUtils.isEmptyString(extractionPath) || GeneralStringUtils.isEmptyString(sessionId) ||
                GeneralStringUtils.isEmptyString(sessionId) || GeneralStringUtils.isEmptyString(requestId) ||
                GeneralStringUtils.isEmptyString(extractionLogin) || GeneralStringUtils.isEmptyString(afsRVobName)) {
            String msg = "Unable to extract packet";
            String detailedMsg = "PacketsVobManager :: extractPacket at last one of the following parameter is null or empty! ( )";
            throw new TpmsException(msg, "", detailedMsg);
        }

        String clearcaseInputFilePath = ClearcaseInterfaceUtils.buildInFilePath(requestId);
        String clearcaseOutputFilePath = ClearcaseInterfaceUtils.buildOutFilePath(requestId);

        Hashtable inFileParameters = ClearcaseInterfaceUtils.initInFileParameterHashTable(ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE,
                ClearcaseInterfaceCostants.REC_TYPE_ACTION_VALUE,
                requestId, extractionLogin);

        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_PACKAGE_ID_FIELD, packet.getId());
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_PACKAGE_NAME_FIELD, packet.getName());
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_EXTRACTION_LOGIN_FIELD, UserUtils.getUserUnixLogin(extractionLogin));
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_FROM_EMAIL_FIELD, packet.getSenderEmail());
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_FIRST_RECIEVE_EMAIL_FIELD, packet.getFirstRecieveEmail());
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_SECOND_RECIEVE_EMAIL_FIELD, packet.getSecondRecieveEmail());
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_ABSOLUTE_EXTRACT_PATH_FIELD, extractionPath);
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_TP_REVISION_FIELD, packet.getTpRevision());
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_TP_RELEASE_FIELD, packet.getTpRelease());
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_CC_EMAIL_FIELD, packet.getCcEmail());            //
        inFileParameters.put(ClearcaseInterfaceCostants.EXTRACT_ACTION_R_VOB_NAME_FIELD, afsRVobName);

        try {
            ClearcaseInterfaceUtils.writeInBackEndFile(inFileParameters, clearcaseInputFilePath);
        } catch (IOException e) {
            String msg = "Unable to produce in file (" + clearcaseInputFilePath + ")";
            errorLog("PacketsVobManager :: extractPacket : " + msg, e);
            throw new TpmsException(msg, ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE, e);
        }

        try {
            ClearcaseInterface.invokeCommand(administratorUnixLogin, clearcaseInputFilePath, clearcaseOutputFilePath, sessionId);
        } catch (Exception e) {
            String msg = "Unable to call clearcase statement";
            errorLog("PacketsVobManager :: extractPacket : " + msg, e);
            throw new TpmsException(msg, ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE, e);
        }

        return clearcaseOutputFilePath;
    }

    /**
     * this method check if an alredy invoked extract action is terminated or not
     * @param requestId the request id in order to uniquely identify the extract action
     * @param sessionId the current session id
     * @param currentUserLogin the user that has requested the extract action
     * @param packet the packet that should be extracted
     * @return <li>COMMAND_OK_RESULT the clearcase command execution was ok
     *         <li>COMMAND_UNKNOW_RESULT the clearcase command execution was unknow: the out file is not still present.
     * @throws TpmsException if an error occurs or if the clearcase command ended with error...
     */
    public static int checkExtractCommandResult(String requestId, String sessionId, String currentUserLogin, Packet packet) throws TpmsException {
        debugLog("PacketsVobManager :: checkExtractCommandResult : started.... requestId = " + requestId + " sessionId = " + sessionId + " currentUserLogin = " + currentUserLogin + " packet is null?" + (packet == null));
        int result = ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT;
        String outFilePath = ClearcaseInterfaceUtils.buildOutFilePath(requestId);
        File outFile = new File(outFilePath);
        Hashtable outFileContent;

        if (outFile.exists()) {
            debugLog("PacketsVobManager :: checkExtractCommandResult : outFile found " + outFilePath);
            //se il file esiste avvio la procedura per tracciarlo e per verificare il riusltato dell'operazione
            String unixUser = UserUtils.getUserUnixLogin(currentUserLogin);
            try {
                outFileContent = ClearcaseInterface.parseOutFile(new File(outFilePath), unixUser, sessionId);
            } catch (IOException e) {
                String msg = "There was an error parsing clearcase output file";
                errorLog("PacketsVobManager :: checkExtractCommandResult : requestId = " + requestId + " " + msg + " exception message = " + e.getMessage(), e);
                throw new TpmsException(msg, "outFilePath = " + outFilePath, e);
            }


            String ccCommandResult = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_RESULT);

            if (ccCommandResult.equals(ClearcaseInterfaceCostants.OUT_RESULT_OK_VALUE)) {
                // if the send action has ok result...
                result = ClearcaseInterfaceCostants.COMMAND_OK_RESULT;
                //insert packet data into database complating packet data with cc xml return info
                //parsing cc xml out file
                Hashtable xmlOutContent;
                try {
                    xmlOutContent = ClearcaseInterfaceUtils.parseSentOrExtractedXmlOutFile(requestId);
                } catch (ParserConfigurationException e) {
                    String msg = "PacketsVobManager :: checkExtractCommandResult: ParserConfigurationException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE, e);
                } catch (SAXException e) {
                    String msg = "PacketsVobManager :: checkExtractCommandResult: SAXException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE, e);
                } catch (IOException e) {
                    String msg = "PacketsVobManager :: checkExtractCommandResult: IOException Unable to parse xmlOutFile (" + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId) + ")";
                    errorLog(msg, e);
                    throw new TpmsException(msg, ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE, e);
                }
                //Check if the parsed xml file is related to the packet that we are maanging...
                if (tpmsConfiguration.getDvlBool()) {
                    //if is a developing installation bypass the checks...
                    packet.setExtractionDate((Date) xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_SENTDATE_ELEMENT));
                    packet.setLastActionDate(packet.getExtractionDate());
                    packet.setExtractionLogin(currentUserLogin);
                    packet.setStatus(PacketConstants.EXTRACTED_PACKET_STATUS);
                } else {
                    //its a real installation....
                    if (packet.getId().equals(xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_ID_ELEMENT))) {
                        packet.setExtractionDate((Date) xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_SENTDATE_ELEMENT));
                        packet.setLastActionDate(packet.getExtractionDate());
                        packet.setExtractionLogin(currentUserLogin);
                        packet.setStatus(PacketConstants.EXTRACTED_PACKET_STATUS);
                    } else {
                        String msg = "PacketsVobManager :: checkExtractCommandResult : the resulting clearcase xml out file is not related to the current sent package!!!";
                        String detailedMsg = "Current packet id = " + packet.getId() + " xml packet if = " + xmlOutContent.get(ClearcaseInterfaceCostants.SENT_XMLOUT_ID_ELEMENT) + " out xml path = " + ClearcaseInterfaceUtils.buildXmlOutFilePath(requestId);
                        throw new TpmsException(msg, ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE, detailedMsg);
                    }
                }

                //update the packet data into database
                try {
                    PacketsDbManager.dbUpdatePacketData(packet, sessionId, currentUserLogin);
                } catch (TpmsException e) {
                    errorLog("PacketsVobManager :: checkExtractCommandResult : error inserting data into database, no data updated", e);
                }

            } else {
                // if the send action to cc has ko result...
                String userMsg = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_USER_MESSAGE);
                String sysMsg = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_SYSTEM_MESSAGE);
                //there was an error in clearcase operation so reaise an exception....
                errorLog("ClearcaseInterface :: checkExtractCommandResult : error from cc command user message = " + userMsg + "*** system message = " + sysMsg);
                throw new TpmsException(userMsg, ClearcaseInterfaceCostants.EXTRACT_ACTION_VALUE, sysMsg);
            }

        }

        debugLog("ClearcaseInterface :: checkExtractCommandResult : ended result = " + result);
        return result;
    }


}