package tpms;

import it.txt.afs.security.AfsSecurityManager;
import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.servlets.master.AfsServletUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import tol.LogWriter;
import tpms.utils.TpmsConfiguration;
import tpms.utils.Vob;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 30, 2003
 * Time: 11:34:19 AM
 * To change this template use Options | File Templates.
 * FP rev5 - inserita un nuovo gruppo di query sul DB per Engin + Controller (TP_Search + TP_Search_Advanced)
 * identificate dal 'showMenuTpSearchDb'
 * FP rev5 - inserita un nuovo gruppo di query sul DB per Engin - Actions
 * identificate dal 'showMenuActionsDb'
 * FP- rev5 inserita una nuova query per Delete Tp from Vob 'R' per Admin
 */

public class ChangeModeServlet extends AfsGeneralServlet {
    LogWriter log = null;
    TpmsConfiguration tpmsConfiguration = null;

    public void init() throws ServletException {
        this.log = (LogWriter) this.getServletContext().getAttribute("log");
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = getServletConfig().getInitParameter("nextPage");
        HttpSession session = request.getSession();
        try {
            String user = (String) session.getAttribute("user");
            Element userData = CtrlServlet.getUserData(user);
            String userRole = XmlUtils.getVal(userData, "ROLE");
            String mode = request.getParameter("mode");
            checkPermissions(userData, mode);
            session.setAttribute("workMode", mode);
            setDefaultVobForCurrentMode(session, mode, userData);
            checkVobAccessPermission(session, mode);
            setAvailableQueries(session, mode, userRole);
            setAvailableOptions(session, mode);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
            rDsp.forward(request, response);
        }
        catch (Exception e) {
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }
    }

    public static void checkPermissions(Element userData, String mode) throws TpmsException {
        String msgTxt = "REQUIRE AUTHORIZATION TO SWITCH TO " + mode + " MODE";
        String actionTxt = "FORBIDDEN OPERATIVE MODE";
        String role = (XmlUtils.getVal(userData, "ROLE"));
        if ((role.equals("CONTROLLER")) || (role.equals("ADMIN"))) {
            if ((mode.equals("RECWORK")) || (mode.equals("SENDWORK")) || (mode.equals("AIDED_FTP"))) {
                throw new TpmsException(msgTxt, actionTxt);
            }
        } else if (role.equals("QUERY_USER")) {
            if ((mode.equals("RECWORK")) || (mode.equals("SENDWORK")) || (mode.equals("LOCREP")) || (mode.equals("AIDED_FTP"))) {
                throw new TpmsException(msgTxt, actionTxt);
            }
        } else if (role.equals(AfsSecurityManager.AIDED_FTP_SERVICE_ROLE)) {
            if ((mode.equals("RECWORK")) || (mode.equals("SENDWORK")) || (mode.equals("LOCREP"))) {
                throw new TpmsException(msgTxt, actionTxt);
            }
        }
    }

    public static void setDefaultVobForCurrentMode(HttpSession session, String mode, Element userData) throws TpmsException {

        String dfltDvlVob = XmlUtils.getVal(userData, "DVL_VOB");
        String dfltRecVob = XmlUtils.getVal(userData, "REC_VOB");
        Element vobData = null;

        if (mode.equals("SENDWORK")) {
            if (!GeneralStringUtils.isEmptyString(dfltDvlVob)) {
                if (XmlUtils.getVal(userData, "ROLE").equals("ADMIN")) {
                    //in caso di ADMIN verifico solo il nome del VOB
                    vobData = VobManager.getVobData(dfltDvlVob);
                } else {
                    //controllo se il vobD di default dell'utente sia effettivamente agganciato al gruppo Unix dell'utente

                        vobData = VobManager.getVobData(dfltDvlVob, XmlUtils.getVal(userData, "UNIX_GROUP"));
                }
            }
        } else if (mode.equals("RECWORK")) {
            if (!GeneralStringUtils.isEmptyString(dfltRecVob)) {
                vobData = VobManager.getVobData(dfltRecVob);
            }
        }
        setDefaultVobsForAfsMode(dfltDvlVob, dfltRecVob, session);
        if (vobData != null) {
            String vobName = XmlUtils.getVal(vobData, "NAME");
            session.setAttribute("vob", vobName);
            session.setAttribute("vobDesc", XmlUtils.getVal(vobData, "DESC"));
            session.setAttribute("vobType", XmlUtils.getVal(vobData, "TYPE"));
            Vob currentVob = VobManager.getVobDataByVobName(vobName);
            session.setAttribute("vobObject", currentVob);
        }
    }


    /**
     * this methods manage the vob information for the afs service: retrieve D and R informations ad set the in session...
     *
     * @param userDevelopVob
     * @param userRecieveVob
     * @param session
     */
    public static void setDefaultVobsForAfsMode(String userDevelopVob, String userRecieveVob, HttpSession session) {

        Vob v = VobManager.getVobDataByVobName(userDevelopVob);
        if (v != null && v.getType().equals(VobManager.T_VOB_TYPE_FLAG_VALUE)) {
            session.setAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME, v);
        } else {
            session.removeAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME);
        }

        v = VobManager.getVobDataByVobName(userRecieveVob);
        if (v != null && v.getType().equals(VobManager.R_VOB_TYPE_FLAG_VALUE)) {
            session.setAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME, v);
        } else {
            session.removeAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME);
        }

    }


    public void checkVobAccessPermission(HttpSession session, String mode) throws TpmsException {
        if (session.getAttribute("vob") == null) return;
        String vobType = (String) session.getAttribute("vobType");
        if (mode.equals("SENDWORK")) {
            if (vobType.equals("R")) {
                session.removeAttribute("vob");
                session.removeAttribute("vobDesc");
                session.removeAttribute("vobType");
            }
        }
        if (mode.equals("RECWORK")) {
            if (vobType.equals("T")) {
                session.removeAttribute("vob");
                session.removeAttribute("vobDesc");
                session.removeAttribute("vobType");
            }
        }
    }

    public static void setAvailableQueries(HttpSession session, String mode, String userRole) {

        if ((mode.equals("SENDWORK")) || (mode.equals("LOCREP"))) {
            session.setAttribute("showMenuLinesetQueriesVob", ((session.getAttribute("vobType") != null) && (session.getAttribute("vobType").equals("D"))) ? Boolean.TRUE : Boolean.FALSE);
            session.setAttribute("showMenuLinesetHistVob", ((session.getAttribute("vobType") != null) && (session.getAttribute("vobType").equals("D"))) ? Boolean.TRUE : Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesVob", Boolean.TRUE);
            session.setAttribute("showMenuTpHistVob", Boolean.TRUE);
            session.setAttribute("showMenuTpQueriesDb", Boolean.FALSE);
            session.setAttribute("showMenuTpSearchDb", Boolean.FALSE);
            session.setAttribute("showMenuTpHistDb", Boolean.FALSE);
            session.setAttribute("showMenuLsQueriesDb", Boolean.FALSE);
            session.setAttribute("showMenuActionsDb", Boolean.FALSE);
            //FP- rev5 inserita una nuova query per Delete Tp from Vob 'R' per Admin
            session.setAttribute("showMenuDeleteTpVob", ((session.getAttribute("vobType") != null) && (session.getAttribute("vobType").equals("R")) && (userRole.equals("ADMIN"))) ? Boolean.TRUE : Boolean.FALSE);
            session.setAttribute("showAidedFtpServiceMenu", Boolean.FALSE);
        }
        if (mode.equals("RECWORK")) {
            session.setAttribute("showMenuLinesetQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuLinesetHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesVob", Boolean.TRUE);
            session.setAttribute("showMenuTpHistVob", Boolean.TRUE);
            session.setAttribute("showMenuTpQueriesDb", Boolean.FALSE);
            session.setAttribute("showMenuTpSearchDb", Boolean.FALSE);
            session.setAttribute("showMenuTpHistDb", Boolean.FALSE);
            session.setAttribute("showMenuLsQueriesDb", Boolean.FALSE);
            session.setAttribute("showMenuActionsDb", Boolean.FALSE);
            //FP- rev5 inserita una nuova query per Delete Tp from Vob 'R' per Admin
            session.setAttribute("showMenuDeleteTpVob", Boolean.FALSE);
            session.setAttribute("showAidedFtpServiceMenu", Boolean.FALSE);
        }
        if (mode.equals("GLOBREP") && ((userRole.equals("QUERY_USER")) || (userRole.equals("ADMIN")))) {
            session.setAttribute("showMenuLinesetQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuLinesetHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuTpHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesDb", Boolean.TRUE);
            //FP- rev5 inserita un nuovo gruppo di query sul DB per Engin + Controller (TP_Search + TP_Search_Advanced)
            session.setAttribute("showMenuTpSearchDb", Boolean.FALSE);
            session.setAttribute("showMenuTpHistDb", Boolean.TRUE);
            session.setAttribute("showMenuLsQueriesDb", Boolean.TRUE);
            session.setAttribute("showMenuActionsDb", Boolean.FALSE);
            //FP- rev5 inserita una nuova query per Delete Tp from Vob 'R' per Admin
            session.setAttribute("showMenuDeleteTpVob", Boolean.FALSE);
            session.setAttribute("showAidedFtpServiceMenu", Boolean.FALSE);
        } else if ((mode.equals("GLOBREP") && (userRole.equals("CONTROLLER")))) {
            session.setAttribute("showMenuLinesetQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuLinesetHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuTpHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesDb", Boolean.FALSE);
            //FP- rev5 inserita un nuovo gruppo di query sul DB per Engin + Controller (TP_Search + TP_Search_Advanced)
            session.setAttribute("showMenuTpSearchDb", Boolean.TRUE);
            session.setAttribute("showMenuTpHistDb", Boolean.TRUE);
            session.setAttribute("showMenuLsQueriesDb", Boolean.TRUE);
            session.setAttribute("showMenuActionsDb", Boolean.FALSE);
            //FP- rev5 inserita una nuova query per Delete Tp from Vob 'R' per Admin
            session.setAttribute("showMenuDeleteTpVob", Boolean.FALSE);
            session.setAttribute("showAidedFtpServiceMenu", Boolean.FALSE);
        } else if ((mode.equals("GLOBREP") && (userRole.equals("ENGINEER")))) {
            session.setAttribute("showMenuLinesetQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuLinesetHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuTpHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesDb", Boolean.FALSE);
            //FP- rev5 inserita un nuovo gruppo di query sul DB per Engin + Controller (TP_Search + TP_Search_Advanced)
            session.setAttribute("showMenuTpSearchDb", Boolean.TRUE);
            session.setAttribute("showMenuTpHistDb", Boolean.TRUE);
            session.setAttribute("showMenuLsQueriesDb", Boolean.TRUE);
            //FP- rev5 inserita un nuovo gruppo di query sul DB per Engin - Actions
            session.setAttribute("showMenuActionsDb", Boolean.TRUE);
            //FP- rev5 inserita una nuova query per Delete Tp from Vob 'R' per Admin
            session.setAttribute("showMenuDeleteTpVob", Boolean.FALSE);
            session.setAttribute("showAidedFtpServiceMenu", Boolean.FALSE);
        } else if ((mode.equals("AIDED_FTP") && (userRole.equals("AIDED_FTP")))) {
            session.setAttribute("showMenuLinesetQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuLinesetHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesVob", Boolean.FALSE);
            session.setAttribute("showMenuTpHistVob", Boolean.FALSE);
            session.setAttribute("showMenuTpQueriesDb", Boolean.FALSE);
            session.setAttribute("showMenuTpSearchDb", Boolean.FALSE);
            session.setAttribute("showMenuTpHistDb", Boolean.FALSE);
            session.setAttribute("showMenuLsQueriesDb", Boolean.FALSE);
            session.setAttribute("showMenuActionsDb", Boolean.FALSE);
            session.setAttribute("showMenuDeleteTpVob", Boolean.FALSE);
            session.setAttribute("showAidedFtpServiceMenu", Boolean.TRUE);
        }
    }

    public static void setAvailableOptions(HttpSession session, String mode) {
        if ((mode.equals("RECWORK")) || (mode.equals("SENDWORK")) || (mode.equals("LOCREP") || (mode.equals("AIDED_FTP")))) {
            session.setAttribute("isWorkDirBrowseVisible", Boolean.TRUE);
            session.setAttribute("isVobAccessVisible", Boolean.TRUE);
        } else if (mode.equals("GLOBREP")) {
            session.setAttribute("isWorkDirBrowseVisible", Boolean.FALSE);
            session.setAttribute("isVobAccessVisible", Boolean.FALSE);
        }
    }

    public static String getDefaultMode(String role, Element userData) throws Exception {
        if (role.equals("ENGINEER")) {
            if ((XmlUtils.getVal(userData, "WORK_MODE") != null) && (!XmlUtils.getVal(userData, "WORK_MODE").equals(""))) {
                return XmlUtils.getVal(userData, "WORK_MODE");
            } else
                return "RECWORK";
        } else if ((role.equals("ADMIN")) || (role.equals("CONTROLLER"))) {
            if ((XmlUtils.getVal(userData, "WORK_MODE") != null) && (!XmlUtils.getVal(userData, "WORK_MODE").equals(""))) {
                return XmlUtils.getVal(userData, "WORK_MODE");
            }
            return "LOCREP";
        } else if (role.equals("QUERY_USER")) {
            return "GLOBREP";
        } else if (role.equals(AfsSecurityManager.AIDED_FTP_SERVICE_ROLE)) {
            return "AIDED_FTP";
        }
        return "";
    }

    public static void setAvailableModes(HttpSession session, String role) throws Exception {
        if (role.equals("ENGINEER")) {
            session.setAttribute("isSendWorkModeEnabled", Boolean.TRUE);
            session.setAttribute("isRecWorkModeEnabled", Boolean.TRUE);
            session.setAttribute("isLocRepModeEnabled", Boolean.FALSE);
            session.setAttribute("isGlobRepModeEnabled", Boolean.TRUE);
            session.setAttribute("isAidedFtpServiceEnabled", Boolean.FALSE);
        } else if ((role.equals("ADMIN")) || (role.equals("CONTROLLER"))) {
            session.setAttribute("isSendWorkModeEnabled", Boolean.FALSE);
            session.setAttribute("isRecWorkModeEnabled", Boolean.FALSE);
            session.setAttribute("isLocRepModeEnabled", Boolean.TRUE);
            session.setAttribute("isGlobRepModeEnabled", Boolean.TRUE);
            session.setAttribute("isAidedFtpServiceEnabled", Boolean.FALSE);
        } else if (role.equals("QUERY_USER")) {
            session.setAttribute("isSendWorkModeEnabled", Boolean.FALSE);
            session.setAttribute("isRecWorkModeEnabled", Boolean.FALSE);
            session.setAttribute("isLocRepModeEnabled", Boolean.FALSE);
            session.setAttribute("isGlobRepModeEnabled", Boolean.TRUE);
            session.setAttribute("isAidedFtpServiceEnabled", Boolean.FALSE);
        } else if (role.equals(AfsSecurityManager.AIDED_FTP_SERVICE_ROLE)) {
            session.setAttribute("isSendWorkModeEnabled", Boolean.FALSE);
            session.setAttribute("isRecWorkModeEnabled", Boolean.FALSE);
            session.setAttribute("isLocRepModeEnabled", Boolean.FALSE);
            session.setAttribute("isGlobRepModeEnabled", Boolean.FALSE);
            session.setAttribute("isAidedFtpServiceEnabled", Boolean.TRUE);
        }
    }


}
