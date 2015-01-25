package it.txt.afs.clearcase.utils;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.afs.packet.Packet;
import it.txt.afs.packet.PacketList;
import it.txt.afs.packet.utils.PacketConstants;
import it.txt.general.utils.FileUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tpms.TpmsException;
import tpms.utils.UserUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 26-gen-2006
 * Time: 15.10.50
 * This class contains common utility in order to call the clearcase interface.
 */
public class ClearcaseInterfaceUtils extends AfsCommonStaticClass {


    /**
     * Add the common fields in the hashtable that will represt the in file for the clearcase command
     *
     * @return an hashtable containing common parameters list
     */
    public static Hashtable initInFileParameterHashTable(String action, String recordType, String requestId, String login) {
        Hashtable parameters = new Hashtable();

        parameters.put(ClearcaseInterfaceCostants.RECORD_TYPE_FIELD, recordType);
        parameters.put(ClearcaseInterfaceCostants.ACTION_FIELD, action);
        parameters.put(ClearcaseInterfaceCostants.SESSION_ID_FIELD, requestId);
        parameters.put(ClearcaseInterfaceCostants.DEBUG_FIELD, tpmsConfiguration.getCCDebugClearcaseInterfaceValue());
        parameters.put(ClearcaseInterfaceCostants.XML_OUT_FILE_FIELD, buildXmlOutFilePath(requestId));
        parameters.put(ClearcaseInterfaceCostants.USER_GROUP_DIR, UserUtils.getUserUnixGroup(login));
        parameters.put(ClearcaseInterfaceCostants.USER_HOME_DIR, UserUtils.getUserUnixHome(login));

        return parameters;

    }

    /**
     * create the out file for the cc call
     *
     * @param fileName to be writed
     * @return true if all the operation are well terminated, false otherwise
     * @throws IOException if any problem happens duing file creation
     */
    /*  private static boolean writeOutBackEndFile(String fileName) throws IOException {
        return FileUtils.writeToFile(fileName, "");
    }*/


    /**
     * This method popopulate the in file for cc with param values
     *
     * @param params   <key, value> pairs that ill be written to file
     * @param fileName File name to be created and populated
     * @return true if all the operation are well terminated, false otherwise
     * @throws IOException
     */
    public static boolean writeInBackEndFile(Hashtable params, String fileName) throws IOException {

        Enumeration paramKeys = params.keys();
        StringBuffer sb = null;
        String currentParamKey;

        while (paramKeys.hasMoreElements()) {
            currentParamKey = (String) paramKeys.nextElement();
            if (sb == null || sb.length() == 0) {
                sb = new StringBuffer();
                sb.append(currentParamKey).append("=").append((String) params.get(currentParamKey));
            } else {
                sb.append("\n").append(currentParamKey).append("=").append(params.get(currentParamKey));
            }
        }
        if (sb != null && sb.length() > 0) {
            return FileUtils.writeToFile(fileName, sb.toString());
        } else {
            return false;
        }
    }

    /**
     * Own the logic to produce a unique clearcase request id
     *
     * @param sessionId
     * @return a request Id
     */

    public static String getRequestId(String sessionId) {
        return sessionId + "_" + System.currentTimeMillis();
    }

    /**
     * This method owns the build logic of clearcase input file name.
     *
     * @param requestId
     * @return clearcase input file path name
     */
    public static String buildInFilePath(String requestId) {
        return tpmsConfiguration.getCcInOutDir() + File.separator + "afs_" + requestId + "_in";
    }

    /**
     * This method owns the build logic of clearcase input file name.
     *
     * @return clearcase input file path name
     */
    public static String buildOutFilePath(String requestId) {
        if (tpmsConfiguration.getDvlBool()) {
            debugLog("ClearcaseInterfaceUtils :: buildOutFilePath : debug mode out file = D:\\bat\\ccSimulator\\out_test.txt");
            return "D:\\bat\\ccSimulator\\out_test.txt";
        } else {
            return tpmsConfiguration.getCcInOutDir() + File.separator + "afs_" + requestId + "_out";
        }
    }

    /**
     * Owns the logic to build the path of the xml file that will contain the clearcase result after a call
     *
     * @param requestId
     * @return the xml file path that will contain the clearcase call result.
     */
    public static String buildXmlOutFilePath(String requestId) {
        //repFileName = _webAppDir + File.separator + "images" + File.separator + REQID + "_rep.xml";
        if (tpmsConfiguration.getDvlBool()) {
            return "D:\\bat\\ccSimulator\\out_test.xml";
        } else {
            return tpmsConfiguration.getWebAppDir() + File.separator + "images" + File.separator + "afs_" + requestId + "_rep.xml";
        }
    }

    /**
     * This methods parse the xml out file for send action
     *
     * @param requestId the request id
     * @return blabal
     */
    public static Hashtable parseSentOrExtractedXmlOutFile(String requestId) throws ParserConfigurationException, SAXException, IOException, TpmsException {
        Hashtable result = new Hashtable();
        String xmlOutFilePath;
        if (tpmsConfiguration.getDvlBool()) {
            xmlOutFilePath = "D:\\bat\\ccSimulator\\out_test.xml";
        } else {
            xmlOutFilePath = buildXmlOutFilePath(requestId);
        }
        Element sentPacketListRoot = XmlUtils.getRoot(xmlOutFilePath);
        Element el = (Element) sentPacketListRoot.getElementsByTagName(ClearcaseInterfaceCostants.SENT_XMLOUT_PACKET_ELEMENT).item(0);
        result.put(ClearcaseInterfaceCostants.PARSED_SENT_XMLOUT_ID_KEY, XmlUtils.getTextValue(el, ClearcaseInterfaceCostants.SENT_XMLOUT_ID_ELEMENT).trim());
        result.put(ClearcaseInterfaceCostants.PARSED_SENT_XMLOUT_LABEL_KEY, XmlUtils.getTextValue(el, ClearcaseInterfaceCostants.SENT_XMLOUT_LABEL_ELEMENT).trim());


        Date sentDate;
        if (tpmsConfiguration.getDvlBool()) {
            sentDate = new Date();
        } else {
            try {
                sentDate = XmlUtils.getDateValue(el, ClearcaseInterfaceCostants.SENT_XMLOUT_SENTDATE_ELEMENT, ClearcaseInterfaceCostants.XMLOUT_DATE_FORMAT);
            } catch (ParseException e) {
                String msg = "ClearcaseInterfaceUtils :: parseSentOrExtractedXmlOutFile: Unable to parse sent date!! date = " + XmlUtils.getTextValue(el, ClearcaseInterfaceCostants.SENT_XMLOUT_SENTDATE_ELEMENT);
                errorLog(msg, e);
                throw new TpmsException(msg, "", e);
            }
        }
        result.put(ClearcaseInterfaceCostants.PARSED_SENT_XMLOUT_SENTDATE_KEY, sentDate);
        result.put(ClearcaseInterfaceCostants.PARSED_SENT_XMLOUT_NAME_KEY, XmlUtils.getTextValue(el, ClearcaseInterfaceCostants.SENT_XMLOUT_NAME_ELEMENT).trim());


        return result;
    }

    /**
     * Keeps an xml element rapresenting the a packet, parse it and add it to the given packet list. If the given packet list is null
     * a new one will be istantiated and returned. This process starts if and only if the xmlPacketData is not null
     *
     * @param packetList    the packet list where the packet should be added
     * @param xmlPacketData xml data representing the packet
     * @return the given packet list (or a new one if the given is null) with packet the packet added
     * @throws TpmsException in case of parsing date error...
     */
    private static PacketList addXmlPacketToPacketList(PacketList packetList, Element xmlPacketData) throws TpmsException {
        if (xmlPacketData != null) {
            if (packetList == null) {
                packetList = new PacketList();
            }
            debugLog("ClearcaseInterfaceUtils :: addXmlPacketToPacketList : retieve packet data from xml ...");
            Packet packetToBeAdded = getPacketObjetFromXmlPacketData(xmlPacketData);
            packetList.addPacket(packetToBeAdded);
        }
        return packetList;
    }

    /**
     * This method parse and filter the xml out file produced by a clearcase query action.
     * if the list of owned packeges (i.e. the given login is referred to the sender) (owned == true) only those packages where the given
     * login is the sender.
     * Otherwise
     * if the package status is extracted (PacketConstants.EXTRACTED_PACKET_STATUS) only those packages where the given login is equal
     * to the extraction one will be shown,
     * else only those packages where the given login is equal to the first recieve login or to the second one.
     *
     * @param requestId identifies the cc request in order to retrieve the right xml out file
     * @param owned     this param is involvend in filtering logic... se above for details
     * @param login     this param is involvend in filtering logic... se above for details
     * @return the list of packages that should be shouwn to the user
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TpmsException
     */
    public static PacketList parseAndFilterQueryXmlOutFile(String requestId, boolean owned, String login) throws ParserConfigurationException, SAXException, IOException, TpmsException {
        String xmlOutFilePath;
        if (tpmsConfiguration.getDvlBool()) {
            xmlOutFilePath = "D:\\bat\\ccSimulator\\query_sample_xml_out_file.xml";
        } else {
            xmlOutFilePath = buildXmlOutFilePath(requestId);
        }
        Element xmlPacketListRoot = XmlUtils.getRoot(xmlOutFilePath);
        NodeList xmlPacketListNodes = xmlPacketListRoot.getElementsByTagName(ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_PACKET);
        int nodesCount = xmlPacketListNodes.getLength();

        PacketList result = null;
        Element currentPacketData;
        String currentPkgSender;
        String currentPkgStatus;
        for (int i = 0; i < nodesCount; i++) {
            currentPacketData = (Element) xmlPacketListNodes.item(i);
            if (owned) {
                //the given login is realted to the owner (means to who has sent the package)
                //We have to retrive only those packages where the sender is equal to login....
                currentPkgSender = XmlUtils.getTextValue(currentPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_SENDER_LOGIN);
                if (currentPkgSender.equals(login)) {
                    //add the packet to the list...
                    result = addXmlPacketToPacketList(result, currentPacketData);
                }
            } else {
                // the given login is related to the user that had recieved the packet....
                //and we have to retrieve only those packages where:
                currentPkgStatus = XmlUtils.getTextValue(currentPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_STATUS);

                if (currentPkgStatus.equals(PacketConstants.EXTRACTED_PACKET_STATUS)) {
                    // if the status is extracted the login must be equal to the extraction login
                    if (login.equals(XmlUtils.getTextValue(currentPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_EXTRACTION_LOGIN))) {
                        result = addXmlPacketToPacketList(result, currentPacketData);
                    }
                } else {
                    //the given login should be equal to the first recieve login or to the second recieve login...
                    if (login.equals(XmlUtils.getTextValue(currentPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_FIRST_RECIEVE_LOGIN)) ||
                        login.equals(XmlUtils.getTextValue(currentPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_SECOND_RECIEVE_LOGIN))
                        ) {
                        result = addXmlPacketToPacketList(result, currentPacketData);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Given an xml file that represent the result of a clearcase query search for the packet with the given packetId inside of it
     *
     * @param xmlOutFile the xmlFile containing the query result data.
     * @param packetId   the packetId of the packet to look for.
     * @return the xml element that represent the found packet, null if he packet is not found
     * @throws ParserConfigurationException in case of error
     * @throws SAXException                 in case of error
     * @throws IOException                  in case of error
     */
    public static Element lookUpPacketInXmlOutFileByPacketId(File xmlOutFile, String packetId) throws ParserConfigurationException, SAXException, IOException {
        debugLog("ClearcaseInterfaceUtils :: lookUpPacketInXmlOutFileByPacketId : xmlFile = " + xmlOutFile.getAbsolutePath());
        Element xmlPacketListRoot = XmlUtils.getRoot(xmlOutFile);
        NodeList xmlPacketListNodes = xmlPacketListRoot.getElementsByTagName(ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_PACKET);
        int nodesCount = xmlPacketListNodes.getLength();
        Element currentPacketData;
        for (int i = 0; i < nodesCount; i++) {
            currentPacketData = (Element) xmlPacketListNodes.item(i);
            if (XmlUtils.getTextValue(currentPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_ID).equals(packetId)) {
                return currentPacketData;
            }
        }
        return null;
    }

    /**
     * Starting from a data of a packet represented in a xml Element return the corrispondig packet object
     *
     * @param xmlPacketData xml element thta represents a packet
     * @return a packet represented in a xml Element return the corrispondig packet object, if xmlPacketData a null packet will be returned
     * @throws TpmsException if an error occours during date attributes parsing.
     */
    public static Packet getPacketObjetFromXmlPacketData(Element xmlPacketData) throws TpmsException {
        Packet packet = null;
        if (xmlPacketData != null) {


            packet = new Packet(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_NAME),
                    XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_DESTINATION_PLANT),
                    XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_FROM_PLANT),
                    XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_SENDER_LOGIN),
                    XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_SENDER_EMAIL));
            packet.setId(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_ID));
            packet.setTpRelease(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_TP_RELEASE));
            packet.setTpRevision(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_TP_REVISION));
            packet.setFirstRecieveLogin(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_FIRST_RECIEVE_LOGIN));
            packet.setFirstRecieveEmail(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_FIRST_RECIEVE_EMAIL));
            packet.setSecondRecieveLogin(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_SECOND_RECIEVE_LOGIN));
            packet.setSecondRecieveEmail(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_SECOND_RECIEVE_EMAIL));
            packet.setCcEmail(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_CC_EMAIL));
            packet.setStatus(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_STATUS));
            packet.setExtractionLogin(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_EXTRACTION_LOGIN));
            packet.setXferPath(XmlUtils.getTextValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_XFER_PATH));

            try {
                packet.setSentDate(XmlUtils.getDateValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_SENT_DATE, ClearcaseInterfaceCostants.XMLOUT_DATE_FORMAT));
            } catch (ParseException e) {
                String msg = "ClearcaseInterfaceUtils :: getPacketObjetFromXmlPacketData : unable to parse Sent Date!";
                String msgDetails = "package id = " + packet.getId() + " format = " + ClearcaseInterfaceCostants.XMLOUT_DATE_FORMAT;
                errorLog(msg + " " + msgDetails, e);
                throw new TpmsException(msg, msgDetails, e);
            }

            try {
                packet.setLastActionDate(XmlUtils.getDateValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_LAST_ACTION_DATE, ClearcaseInterfaceCostants.XMLOUT_DATE_FORMAT));
            } catch (ParseException e) {
                String msg = "ClearcaseInterfaceUtils :: getPacketObjetFromXmlPacketData : unable to parse Last Action Date!";
                String msgDetails = "package id = " + packet.getId() + " format = " + ClearcaseInterfaceCostants.XMLOUT_DATE_FORMAT;
                errorLog(msg + " " + msgDetails, e);
                throw new TpmsException(msg, msgDetails, e);
            }


            try {
                packet.setExtractionDate(XmlUtils.getDateValue(xmlPacketData, ClearcaseInterfaceCostants.QUERY_XML_OUT_FIELD_EXTRACTION_DATE, ClearcaseInterfaceCostants.XMLOUT_DATE_FORMAT));
            } catch (ParseException e) {
                String msg = "ClearcaseInterfaceUtils :: getPacketObjetFromXmlPacketData : unable to parse Extraction Date!";
                String msgDetails = "package id = " + packet.getId() + " format = " + ClearcaseInterfaceCostants.XMLOUT_DATE_FORMAT;
                errorLog(msg + " " + msgDetails, e);
                throw new TpmsException(msg, msgDetails, e);
            }
        }
        return packet;

    }


}
