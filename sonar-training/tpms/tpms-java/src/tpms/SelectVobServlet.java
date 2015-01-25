package tpms;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.SessionObjects;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.users.TpmsUser;
import org.w3c.dom.Element;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class SelectVobServlet extends AfsGeneralServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inPage = getServletConfig().getInitParameter("inPage");
        String outPage = getServletConfig().getInitParameter("outPage");
        String nextPage;
        HttpSession session = request.getSession();
        debugLog("VOB SELECTION>");
        TpmsUser currentUser = (TpmsUser) session.getAttribute(SessionObjects.TPMS_USER);
        if (request.getParameter("vob") == null) {
            debugLog("SelectVobServlet :: doPost : to vob selection...");
            if (session.getAttribute("workMode").equals("SENDWORK")) {
                request.setAttribute("D_TypeEnabled", "Y");
                request.setAttribute("T_TypeEnabled", "Y");
                request.setAttribute("R_TypeEnabled", "N");
            } else if (session.getAttribute("workMode").equals("RECWORK")) {
                request.setAttribute("D_TypeEnabled", "Y");
                request.setAttribute("T_TypeEnabled", "N");
                request.setAttribute("R_TypeEnabled", "Y");
            } else if (session.getAttribute("workMode").equals("LOCREP")) {
                request.setAttribute("D_TypeEnabled", "Y");
                request.setAttribute("T_TypeEnabled", "Y");
                request.setAttribute("R_TypeEnabled", "Y");
            }

            nextPage = inPage;
        } else {
            debugLog("SelectVobServlet :: doPost : vob selected perform needed activities...");
            if (UserUtils.hasAfsRole(currentUser.getTpmsLogin())) {
                debugLog("SelectVobServlet :: doPost : ... aided ftp user");
                String tVobName = request.getParameter("vob");
                String rVobName = request.getParameter("rVob");
                Vob tVob;
                Vob rVob;
                if (!GeneralStringUtils.isEmptyString(rVobName)) {
                    rVob = VobManager.getVobDataByVobName(rVobName);
                    session.setAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME, rVob);
                } else {
                    session.removeAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME);
                }
                if (!GeneralStringUtils.isEmptyString(tVobName)) {
                    tVob = VobManager.getVobDataByVobName(tVobName);
                    session.setAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME, tVob);
                } else {
                    session.removeAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME);
                }
            } else {
                debugLog("SelectVobServlet :: doPost : ... NOT aided ftp user");
                String vobName = request.getParameter("vob");
                Element vobData = null;
                try {
                    vobData = VobManager.getVobData(vobName);
                } catch (TpmsException e) {
                    manageError(e, request, response);
                }
                session.setAttribute("vob", vobName);
                session.setAttribute("vobDesc", XmlUtils.getVal(vobData, "DESC"));
                session.setAttribute("vobType", XmlUtils.getVal(vobData, "TYPE"));
                Vob currentVob = VobManager.getVobDataByVobName(vobName);
                session.setAttribute("vobObject", currentVob);
                if (XmlUtils.getVal(vobData, "TYPE").equals("R"))
                    session.setAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME, currentVob);
            }

            String userRole = (String) session.getAttribute("role");
            ChangeModeServlet.setAvailableQueries(session, (String) session.getAttribute("workMode"), userRole);
            nextPage = outPage;
        }

        RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
        debugLog("SelectVobServlet :: doPost : redirect...");
        rDsp.forward(request, response);


    }
}
