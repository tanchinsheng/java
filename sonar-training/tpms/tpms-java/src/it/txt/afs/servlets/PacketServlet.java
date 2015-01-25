package it.txt.afs.servlets;

import it.txt.afs.clearcase.utils.ClearcaseInterfaceCostants;
import it.txt.afs.clearcase.utils.ClearcaseInterfaceUtils;
import it.txt.afs.packet.Packet;
import it.txt.afs.packet.manager.PacketsVobManager;
import it.txt.afs.packet.utils.PacketConstants;
import it.txt.afs.packet.utils.PacketUtils;
import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import tpms.utils.Vob;
import it.txt.afs.utils.AfsWaitInformations;
import it.txt.afs.security.AfsSecurityManager;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;
import it.txt.tpms.users.TpmsUser;

import tpms.TpmsException;
import tpms.utils.UserUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 23-gen-2006
 * Time: 17.50.46
 * To change this template use File | Settings | File Templates.
 */
public class PacketServlet extends AfsGeneralServlet {
    public static final String CREATE_NEW_PACKET_ACTION = "create";
    public static final String SEND_PACKET_ACTION = "send";
    public static final String VIEW_OWNED_PACKET_ACTION = "viewOwned";
    public static final String VIEW_RECIEVED_PACKET_ACTION = "viewReceived";
    public static final String RESEND_PACKET_ACTION = "reSend";
    public static final String EXTRACT_PACKET_ACTION = "extract";
    public static final String SELECT_XFER_PATH = "selectXferPath";
    public static final String EXTRACTION_PATH = "selectExtractionPath";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo mettere controlli sui rights dell'utente corrente
        String action = request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);
        debugLog("PacketServlet :: doPost : action = " + action);
        String nextPage = "";
        HttpSession session = request.getSession();
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);


        if (action.equals(CREATE_NEW_PACKET_ACTION)) {
            debugLog("PacketServlet :: doPost : CREATE_NEW_PACKET_ACTION");
            try {
                AfsSecurityManager.canSendPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            //the user want to create a new packet... the form to crete it will be displayed.
            Packet packet = new Packet();
            //initialize some attributes of the new package....
            packet.setSenderLogin(currentUser.getTpmsLogin());
            packet.setSenderEmail(UserUtils.getUserEmailAddress(currentUser.getTpmsLogin()));
            packet.setFromPlant(tpmsConfiguration.getLocalPlant());
            session.setAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME, packet);

            nextPage = this.getServletConfig().getInitParameter("packetCreate");

        } else if (action.equals(SELECT_XFER_PATH)) {
            debugLog("PacketServlet :: doPost SELECT_XFER_PATH");
            Packet packet = (Packet) session.getAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME);
            String selectedXFerPath = request.getParameter(FileDirectorySelectionServlet.USER_SELECTION_CONTROL_NAME);
            packet = populatePacketDataFromRequest(request, packet);
            if (GeneralStringUtils.isEmptyString(selectedXFerPath)){
                debugLog("PacketServlet :: doPost SELECT_XFER_PATH The user want to select the xfer path");
                session.setAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME, packet);
                nextPage = this.getServletConfig().getInitParameter("navDirServlet");
            } else {
                debugLog("PacketServlet :: doPost SELECT_XFER_PATH The user have selected the xfer path");
                
                packet.setXferPath(selectedXFerPath);
                nextPage = this.getServletConfig().getInitParameter("packetCreate");
            }
        } else if (action.equals(SEND_PACKET_ACTION)) {

            try {
                AfsSecurityManager.canSendPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }

            Packet packet = (Packet) session.getAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME);
            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
                debugLog("PacketServlet :: doPost : SEND_PACKET_ACTION submit clearcase command");
                //this is a submit for packet send....
                //the user submits a new packet ....
                packet = this.populatePacketDataFromRequest(request, packet);
                //calculate Packet ID
                packet.setId(PacketUtils.generatePacketId(packet));

                String requestId = ClearcaseInterfaceUtils.getRequestId(session.getId());
                try {
                    PacketsVobManager.sendPacket(packet, session.getId(), requestId);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }

                debugLog("PacketServlet :: doPost : prima di settare l'attributo che per il wait message");
                //put the attribute in order to tell to the jsp that the wait page should be presented...
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), SEND_PACKET_ACTION, requestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                debugLog("PacketServlet :: doPost : sending ended....");

            } else {
                debugLog("PacketServlet :: doPost : SEND_PACKET_ACTION verify clearcase command result");
                // this is a submit for check packet send result
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                //check and track the result...
                int checkResult = ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT;
                try {
                    //String requestId, String sessionId, String currentUser.getTpmsLogin(), Packet packet
                    checkResult = PacketsVobManager.checkSendCommandResult(requestId, session.getId(), currentUser.getTpmsLogin(), packet);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }

                if (checkResult == ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), SEND_PACKET_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found and it's ok...
                    //...tell to the showing page that has to show the creation confirmation message.
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                }
            }
            //retrieve the next page that should be presented to the user.
            nextPage = this.getServletConfig().getInitParameter("packetCreationResult");

        } else if (action.equals(VIEW_OWNED_PACKET_ACTION)) {
            debugLog("PacketServlet :: doPost : VIEW_OWNED_PACKET_ACTION");
            //the user want to view an owned package details...
            Packet packet;
            try {
                packet = getPacketDataFromXmlFile(request);
                debugLog("PacketServlet :: doPost : getPacketDataFromXmlFile finished");
            } catch (TpmsException e) {
                manageError(e, request, response);
                return;
            }

            if (packet != null) {
                if (packet.getStatus().equals(PacketConstants.SENT_PACKET_STATUS)) {
                    nextPage = this.getServletConfig().getInitParameter("ownedSentPacket");
                } else {
                    nextPage = this.getServletConfig().getInitParameter("ownedExtractedPacket");
                }
            } else {
                manageError("Warning no package found!", request, response);
            }


        } else if (action.equals(VIEW_RECIEVED_PACKET_ACTION)) {
            debugLog("PacketServlet :: doPost : VIEW_RECIEVED_PACKET_ACTION");
            try {
                AfsSecurityManager.canViewRecievedPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            //the user want to view an recieved package details...
            Packet packet;
            try {
                packet = getPacketDataFromXmlFile(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
                return;
            }

            if (packet != null) {
                if (packet.getStatus().equals(PacketConstants.SENT_PACKET_STATUS)) {
                    nextPage = this.getServletConfig().getInitParameter("recievedSentPacket");
                } else {
                    nextPage = this.getServletConfig().getInitParameter("recievedExtractedPacket");
                }
            } else {
                manageError("Warning no package found!", request, response);
            }

        } else if (action.equals(EXTRACTION_PATH)) {
            debugLog("PacketServlet :: doPost EXTRACTION_PATH");
            nextPage = this.getServletConfig().getInitParameter("navDirServlet");
        } else if (action.equals(EXTRACT_PACKET_ACTION)) {
            try {
                AfsSecurityManager.canExtractPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            //the user want to extract a package...
            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
                debugLog("PacketServlet :: doPost : EXTRACT_PACKET_ACTION invoke clearcase command");
                //let's fo the extract action.....
                Packet packet;
                try {
                    packet = getPacketDataFromXmlFile(request);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                    return;
                }
                //retrieve the extraction path...
                String extractionPath = request.getParameter(PacketConstants.EXTRACT_PATH_FIELD_NAME);
                String extractionRequestId = ClearcaseInterfaceUtils.getRequestId(session.getId());
                //(Packet packet, String extractionPath, String sessionId, String requestId, String extractionLogin, String afsRVobName)
                Vob currentRVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME);

                try {
                    PacketsVobManager.extractPacket(packet, extractionPath, session.getId(), extractionRequestId, currentUser.getTpmsLogin(), currentRVob.getName(), UserUtils.getAdminUnixLogin());
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }

                packet.setExtractionPath(extractionPath);
                session.setAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME, packet);
                //put the attribute in order to tell to the jsp that the wait page should be presented...
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), EXTRACT_PACKET_ACTION, extractionRequestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
            } else {
                debugLog("PacketServlet :: doPost : EXTRACT_PACKET_ACTION verify clearcase command result");
                Packet packet = (Packet) session.getAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME);
                // this is a submit for check packet extraction result
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                //check and track the result...
                int checkResult = ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT;
                try {
                    //String requestId, String sessionId, String currentUser.getTpmsLogin(), Packet packet
                    checkResult = PacketsVobManager.checkExtractCommandResult(requestId, session.getId(), currentUser.getTpmsLogin(), packet);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }

                if (checkResult == ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), EXTRACT_PACKET_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found and it's ok...
                    //...tell to the showing page that has to show the creation confirmation message.
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                }
            }
            nextPage = this.getServletConfig().getInitParameter("packetExtractionResult");
        } else if (action.equals(RESEND_PACKET_ACTION)) {
            //the user want to resend an already sended package...


        }
        manageRedirect(nextPage, request, response);
    }


    private Packet getPacketDataFromXmlFile(HttpServletRequest request) throws TpmsException {
        String requestId = request.getParameter(AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME);
        String packetId = request.getParameter(PacketConstants.ID_FIELD_NAME);
        Packet packet = PacketsVobManager.getPacketDetails(requestId, packetId);
        request.setAttribute(AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME, requestId);
        request.setAttribute(PacketConstants.PACKET_ATTRIBUTE_REQUEST_NAME, packet);
        return packet;
    }


    private Packet populatePacketDataFromRequest(HttpServletRequest request, Packet packet) {
        this.retrieveRequestParametersAndAttributesValues(request);
        if (packet == null) {
                packet = new Packet();
        }
        //...in order to populate it with the user given informations.
        String tmpString = (String) parametersValuesList.get(PacketConstants.NAME_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)) {
            packet.setName(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.DESTINATION_PLANT_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setDestinationPlant(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.TP_RELEASE_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setTpRelease(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.TP_REVISION_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setTpRevision(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.FIRST_RECIEVE_LOGIN_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setFirstRecieveLogin(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.FIRST_RECIEVE_EMAIL_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setFirstRecieveEmail(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.SECOND_RECIEVE_LOGIN_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setSecondRecieveLogin(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.SECOND_RECIEVE_EMAIL_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setSecondRecieveEmail(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.CC_EMAIL_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setCcEmail(tmpString);
        }

        tmpString = (String) parametersValuesList.get(PacketConstants.XFERPATH_FIELD_NAME);
        if (!GeneralStringUtils.isEmptyString(tmpString)){
            packet.setXferPath(tmpString);
        }
        return packet;
    }

}
