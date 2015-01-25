package it.txt.afs.servlets;

import it.txt.afs.clearcase.utils.ClearcaseInterfaceUtils;
import it.txt.afs.packet.manager.PacketsVobManager;
import it.txt.afs.packet.utils.PacketQueryResult;
import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import tpms.utils.Vob;
import it.txt.afs.utils.AfsWaitInformations;
import it.txt.afs.security.AfsSecurityManager;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;
import tpms.TpmsException;
import it.txt.tpms.users.TpmsUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 3-feb-2006
 * Time: 13.52.56
 * To change this template use File | Settings | File Templates.
 */
public class VobQueryServlet extends AfsGeneralServlet {
    /**
     * Action filed values:
     * <li>OWNED_SENT_PACKAGES_ACTION will show the packages in status sent where the sender is the current user
     * <li>OWNED_EXTRACTED_PACKAGES_ACTION will show the packages in status extracted where the sender is the current user
     * <li>RECIEVED_SENT_PACKAGES_ACTION will show the packages in status sent where the current user is in the first or second recieve login
     * <li>RECIEVED_EXTRACTED_PACKAGES_ACTION will show the packages in status extracted where the current user the extraction login
     */
    public static final String OWNED_SENT_PACKAGES_ACTION = "ownedSentPackages";
    public static final String OWNED_EXTRACTED_PACKAGES_ACTION = "ownedExtractedPackages";
    public static final String RECIEVED_SENT_PACKAGES_ACTION = "recievedSentPacakges";
    public static final String RECIEVED_EXTRACTED_PACKAGES_ACTION = "recievedExtractedPackages";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String action = request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);
        HttpSession session = request.getSession();
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        debugLog("VobQueryServlet :: doPost : action = " + action + " user = " + currentUser.getTpmsLogin());
        String nextPage = "";


        if (action.equals(OWNED_SENT_PACKAGES_ACTION)) {
            try {
                AfsSecurityManager.canViewOwnedPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
debugLog("VobQueryServlet :: doPost : OWNED_SENT_PACKAGES_ACTION ");
            //show the packages in status sent where the sender is the current user
            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
debugLog("VobQueryServlet :: doPost : cc command invokation... ");
                //invoke clearcase command....
                String requestId = ClearcaseInterfaceUtils.getRequestId(session.getId());
                //gather current selected T vob data:
                Vob tVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME);

                if (tVob == null) {
                    manageRedirect(this.getServletConfig().getInitParameter("selectVob"), request, response);
                    return;
                }
                String tVobName = tVob.getName();
                try {
                    PacketsVobManager.queryOwnedSentPackets(requestId, currentUser.getTpmsLogin(), session.getId(), tVobName);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
                //put the attribute in order to tell to the jsp that the wait page should be presented...
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), OWNED_SENT_PACKAGES_ACTION, requestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
            } else {
debugLog("VobQueryServlet :: doPost : look for cc command result... ");
                //check for clearcase command result
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);

                PacketQueryResult packetQueryResult;

                try {
                    packetQueryResult = PacketsVobManager.ownedSentPacketsParseResult(currentUser.getTpmsLogin(), requestId, session.getId());
                } catch (TpmsException e) {
                    manageError(e, request, response);
                    return;
                }
                boolean resultNotFound = packetQueryResult.isResultPresent();
                if (!resultNotFound) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), OWNED_SENT_PACKAGES_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found and it's ok...
                    //...tell to the showing page that has to show the creation confirmation message.
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                    //set the founded packet list into request attribute.
                    request.setAttribute(AfsServletUtils.QUERY_PACKET_LIST_REQUEST_ATTRIBUTE_NAME, packetQueryResult.getPacketlist());
                    request.setAttribute(AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME, requestId);

                }
            }
            nextPage = this.getServletConfig().getInitParameter("owned_sent_list");


        } else if (action.equals(OWNED_EXTRACTED_PACKAGES_ACTION)) {
            try {
                AfsSecurityManager.canViewOwnedPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            //show the packages in status extracted where the sender is the current user
            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
                //invoke clearcase command....
                String requestId = ClearcaseInterfaceUtils.getRequestId(session.getId());
                //gather current selected T vob data:
                Vob tVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME);

                if (tVob == null) {
                    manageRedirect(this.getServletConfig().getInitParameter("selectVob"), request, response);
                    return;
                }
                String tVobName = tVob.getName();
                try {
                    PacketsVobManager.queryOwnedExtractedPackets(requestId, currentUser.getTpmsLogin(), session.getId(), tVobName);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
                //put the attribute in order to tell to the jsp that the wait page should be presented...
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), OWNED_EXTRACTED_PACKAGES_ACTION, requestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
            } else {
                //check for clearcase command result
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);

                PacketQueryResult packetQueryResult;

                try {
                    packetQueryResult = PacketsVobManager.ownedExtractedPacketsParseResult(currentUser.getTpmsLogin(), requestId, session.getId());
                } catch (TpmsException e) {
                    manageError(e, request, response);
                    return;
                }
                boolean resultNotFound = packetQueryResult.isResultPresent();
                if (!resultNotFound) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), OWNED_EXTRACTED_PACKAGES_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found and it's ok...
                    //...tell to the showing page that has to show the creation confirmation message.
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                    //set the founded packet list into request attribute.
                    request.setAttribute(AfsServletUtils.QUERY_PACKET_LIST_REQUEST_ATTRIBUTE_NAME, packetQueryResult.getPacketlist());
                    request.setAttribute(AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME, requestId);

                }
            }
            nextPage = this.getServletConfig().getInitParameter("owned_extracted_list");
        } else if (action.equals(RECIEVED_SENT_PACKAGES_ACTION)) {
            try {
                AfsSecurityManager.canViewRecievedPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            //show the packages in status sent where the current user is in the first or second recieve login
            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
                //invoke clearcase command....
                String requestId = ClearcaseInterfaceUtils.getRequestId(session.getId());
                //gather current selected T vob data:
                Vob RVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME);

                if (RVob == null) {
                    manageRedirect(this.getServletConfig().getInitParameter("selectVob"), request, response);
                    return;
                }
                String tVobName = RVob.getName();
                try {
                    PacketsVobManager.queryRecievedSentPackets(requestId, currentUser.getTpmsLogin(), session.getId(), tVobName);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }

                //put the attribute in order to tell to the jsp that the wait page should be presented...
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), RECIEVED_SENT_PACKAGES_ACTION, requestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);

            } else {
                //check for clearcase command result
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);

                PacketQueryResult packetQueryResult;

                try {
                    packetQueryResult = PacketsVobManager.recievedSentPacketsParseResult(currentUser.getTpmsLogin(), requestId, session.getId());
                } catch (TpmsException e) {
                    manageError(e, request, response);
                    return;
                }
                boolean resultNotFound = packetQueryResult.isResultPresent();
                if (!resultNotFound) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), RECIEVED_SENT_PACKAGES_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found and it's ok...
                    //...tell to the showing page that has to show the creation confirmation message.
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                    //set the founded packet list into request attribute.
                    request.setAttribute(AfsServletUtils.QUERY_PACKET_LIST_REQUEST_ATTRIBUTE_NAME, packetQueryResult.getPacketlist());
                    request.setAttribute(AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME, requestId);

                }
            }
            nextPage = this.getServletConfig().getInitParameter("recieved_sent_list");
        } else if (action.equals(RECIEVED_EXTRACTED_PACKAGES_ACTION)) {
            try {
                AfsSecurityManager.canViewRecievedPacket(request);
            } catch (TpmsException e) {
                manageError(e, request, response);
            }
            //show the packages in status extracted where the current user the extraction login
            if (GeneralStringUtils.isEmptyString(request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME))) {
                //invoke clearcase command....
                String requestId = ClearcaseInterfaceUtils.getRequestId(session.getId());
                //gather current selected T vob data:
                Vob rVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME);

                if (rVob == null) {
                    manageRedirect(this.getServletConfig().getInitParameter("selectVob"), request, response);
                    return;
                }
                String tVobName = rVob.getName();
                try {
                    PacketsVobManager.queryRecievedExtractedPackets(requestId, currentUser.getTpmsLogin(), session.getId(), tVobName);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
                //put the attribute in order to tell to the jsp that the wait page should be presented...
                AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), RECIEVED_EXTRACTED_PACKAGES_ACTION, requestId);
                request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);

            } else {
                //check for clearcase command result
                String requestId = request.getParameter(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                PacketQueryResult packetQueryResult;
                try {
                    packetQueryResult = PacketsVobManager.recievedExtractedPacketsParseResult(currentUser.getTpmsLogin(), requestId, session.getId());
                } catch (TpmsException e) {
                    manageError(e, request, response);
                    return;
                }
                boolean resultNotFound = packetQueryResult.isResultPresent();
                if (!resultNotFound) {
                    // the result is not still present....
                    //put the attribute in order to tell to the jsp that the wait page should be presented...
                    AfsWaitInformations afsWaitInformations = new AfsWaitInformations(this.getServletName(), RECIEVED_EXTRACTED_PACKAGES_ACTION, requestId);
                    request.setAttribute(AfsServletUtils.WAIT_OBJECT_REQUEST_ATTRIBUTE_NAME, afsWaitInformations);
                } else {
                    //The result was found and it's ok...
                    //...tell to the showing page that has to show the creation confirmation message.
                    request.removeAttribute(AfsServletUtils.CHECK_RESULT_REQUEST_NAME);
                    //set the founded packet list into request attribute.
                    request.setAttribute(AfsServletUtils.QUERY_PACKET_LIST_REQUEST_ATTRIBUTE_NAME, packetQueryResult.getPacketlist());
                    request.setAttribute(AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME, requestId);

                }
            }
            nextPage = this.getServletConfig().getInitParameter("recieved_extracted_list");
        }
        debugLog("VobQueryServlet :: doPost : action = " + action + " user = " + currentUser.getTpmsLogin() + " nextPage = " + nextPage);
        manageRedirect(nextPage, request, response);
    }


}

