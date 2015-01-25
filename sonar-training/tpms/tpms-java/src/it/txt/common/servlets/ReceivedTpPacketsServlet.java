package it.txt.common.servlets;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.common.managers.database.TpPacketDbManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;

import tpms.TpmsException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 1-mar-2006
 * Time: 9.50.05
 * This servlet is used in order to retrieve packets and tp recieved by current user.
 */
public class ReceivedTpPacketsServlet extends AfsGeneralServlet
{

    public static final String ELEMENT_LIST_ATTRIBUTE_NAME = "tpAndPacketsList";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("user");
        String nextPage = this.getServletConfig().getInitParameter("listPage");
        try {
            request.setAttribute(ELEMENT_LIST_ATTRIBUTE_NAME, TpPacketDbManager.retrieveRecievedTpAndPacketsList(currentUser));
        } catch (TpmsException e) {
            manageError(e, request, response);
        }
        manageRedirect(nextPage, request, response);
    }
}
