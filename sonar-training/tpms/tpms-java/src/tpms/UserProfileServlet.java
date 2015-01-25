package tpms;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.afs.security.AfsSecurityManager;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tpms.utils.UserUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserProfileServlet extends AfsGeneralServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String repPage = getServletConfig().getInitParameter("repPage");
        String viewPage = getServletConfig().getInitParameter("viewPage");
        String editPage = getServletConfig().getInitParameter("editPage");
        String delPage = getServletConfig().getInitParameter("delPage");
        String edit_pwdPage = getServletConfig().getInitParameter("edit_pwdPage");
        String save_pwd_msgPage = getServletConfig().getInitParameter("save_pwd_msgPage");
        String action = (request.getParameter("action") == null ? "view" : request.getParameter("action"));
        String actionTxt = "";
        String nextPage = viewPage;
        HttpSession session = request.getSession();
        Element accountData = null;
        boolean isNewUser = false;
        String user = (String) session.getAttribute("user");

        debugLog("UserProfileServlet :: doPost : start action = " + action);


        try {
        	
            Element userData = CtrlServlet.getUserData(user);
            String accntName = (request.getParameter("accntName") == null ? (String) session.getAttribute("user") : request.getParameter("accntName"));
            isNewUser = ((request.getParameter("isNewUser") != null) && (request.getParameter("isNewUser").equals("Y")));
            accountData = (((action.equals("new")) || ((action.equals("save")) && (isNewUser))) ? null : CtrlServlet.getUserData(accntName));

            if (action.equals("view")) {
                actionTxt = "USER PROFILE DATA FETCH";
                request.setAttribute("accntRec", accountData);
                setEditPermissions(accountData, userData);
                nextPage = viewPage;
            } else if ((action.equals("edit")) || (action.equals("edit_pwd"))) {
                actionTxt = (action.indexOf("_pwd") > 0 ? "USER PASSWORD MODIFICATION" : "USER PROFILE DATA MODIFICATION");
                request.setAttribute("isNewUser", "N");
                request.setAttribute("accntRec", accountData);
                setEditPermissions(accountData, userData);
                nextPage = (action.equals("edit") ? editPage : edit_pwdPage);
            } else if (action.equals("new")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    actionTxt = "NEW USER PROFILE INITIALIZAZTION";
                    request.setAttribute("isNewUser", "Y");
                    nextPage = editPage;
                } else
                    throw new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            } else if (action.equals("save") || action.equals("save_pwd")) {
                debugLog("UserProfileServlet :: doPost : action.equals(\"save\") ");
                if (isNewUser) {
                    actionTxt = "NEW USER PROFILE INSERTION";
                    accountData = getNewAccntData(request, userData);
                    request.setAttribute("user", XmlUtils.getVal(accountData, "NAME"));
                    request.setAttribute("accntRec", accountData);
                    CtrlServlet.accntsSave();
                    nextPage = viewPage;
                } else {
                    actionTxt = (action.indexOf("_pwd") > 0 ? "USER PASSWORD MODIFICATION" : "USER PROFILE DATA MODIFICATION");
                    request.setAttribute("user", accntName);

                    Element checkData = (Element) accountData.cloneNode(true);
                    setAccntData(request, checkData);
                    checkMandatoryData(checkData);

                    setAccntData(request, accountData);
                    request.setAttribute("accntRec", accountData);
                    //update the division field in order to view the modification on the top.jsp
                    String division = XmlUtils.getVal(accountData, "DIVISION");
                    debugLog("User DIvision>" + division);
                    session.setAttribute("division", division);
                    CtrlServlet.accntsSave();
                    nextPage = (action.equals("save") ? viewPage : save_pwd_msgPage);
                }
            } else if (action.equals("delete")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    if (userData == accountData) {
                        throw new TpmsException("CANNOT DELETE YOUR ACCOUNT", actionTxt + " NOT ALLOWED");
                    }
                    actionTxt = "USER PROFILE DELETION";
                    delAccntData(accountData);
                    CtrlServlet.accntsSave();
                    nextPage = delPage;
                } else
                    throw new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            } else if (action.equals("report")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    actionTxt = "USER PROFILES DATA FETCH";
                    nextPage = repPage;
                } else
                    throw new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            }
            debugLog("UserProfileServlet :: doPost :: actionTxt = " + actionTxt);
        }
        catch (Exception e) {
            if (e instanceof TpmsException) ((TpmsException) e).setAction(actionTxt + " FAILURE");
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }

        /**
         * DB MANAGEMENT - NO EXCEPTION IS RAISED
         */
        try {
            if (action.equals("save")) {
                if (isNewUser) {
                    //insert
                    InsertDbUserAction insertDbUserAction = new InsertDbUserAction(accountData, session.getId(), user);
                    insertDbUserAction.updateDb();
                } else {
                    //update
                    debugLog("UPDATE ACTION START");
                    UpdateDbUserAction updateDbUserAction = new UpdateDbUserAction(accountData, session.getId(), user);
                    updateDbUserAction.updateDb();
                }
            } else if (action.equals("delete")) {
                //delete
                debugLog("DELETE ACTION START");
                DeleteDbUserAction deleteDbUserAction = new DeleteDbUserAction(accountData, session.getId(), user);
                deleteDbUserAction.updateDb();
            }
        }
        catch (Exception e) {
            //debug("UserProfileServlet :: doPost exception e.getMessege " + e.getMessage() );
        }

        RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
        rDsp.forward(request, response);
    }
    /*  definito un nuovo stato edit=S (select) utile per la gestione della divisione
        il controller e il queryUsers devono poter cambiare divisione di lavoro
        l'editing della DIVISION sarà possibile solo come admin(EDIT=Y) o newUser
    */
    public static void setEditPermissions(Element accntData, Element userData) throws Exception {
        setAllFieldsEditPerms(accntData, false);
        if (CtrlServlet.isUserAdmin(userData)) {
            setAllFieldsEditPerms(accntData, true);
        } else if (accntData == userData) {
            XmlUtils.getChild(accntData, "FIRST_NAME").setAttribute("edit", "Y");
            XmlUtils.getChild(accntData, "SURNAME").setAttribute("edit", "Y");
            XmlUtils.getChild(accntData, "PWD").setAttribute("edit", "Y");
            XmlUtils.getChild(accntData, "EMAIL").setAttribute("edit", "Y");
            XmlUtils.getChild(accntData, "WORK_DIR").setAttribute("edit", "Y");
            if (!XmlUtils.getVal(userData, "ROLE").equals(AfsSecurityManager.ENGINEER_ROLE) &&
                !XmlUtils.getVal(userData, "ROLE").equals(AfsSecurityManager.AIDED_FTP_SERVICE_ROLE)) {
                XmlUtils.getChild(accntData, "DIVISION").setAttribute("edit", "S");
            } else {
                XmlUtils.getChild(accntData, "DIVISION").setAttribute("edit", "N");
            }
            XmlUtils.getChild(accntData, "WORK_MODE").setAttribute("edit", "Y");
            XmlUtils.getChild(accntData, "DVL_VOB").setAttribute("edit", "Y");
            XmlUtils.getChild(accntData, "REC_VOB").setAttribute("edit", "Y");
        }
    }


    public synchronized static void setAccntData(HttpServletRequest request, Element accntData) throws TpmsException {

        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD." + UserUtils.LDAP_LOGIN_ELEMENT_TAG))) {
            XmlUtils.setVal(accntData, UserUtils.LDAP_LOGIN_ELEMENT_TAG, request.getParameter("FIELD." + UserUtils.LDAP_LOGIN_ELEMENT_TAG));
        }

        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.UNIX_USER"))) {
            String tpmsLogin = request.getParameter("FIELD.UNIX_USER");
            XmlUtils.setVal(accntData, "UNIX_USER", tpmsLogin);
            XmlUtils.setVal(accntData, "NAME", tpmsLogin);
        }

        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.PWD"))) {
            if (request.getParameter("FIELD.PWD").equals(request.getParameter("FIELD.PWD_CONFIRM"))) {
                String pwdHash = CtrlServlet.getPwdHash(request.getParameter("FIELD.PWD"));
                XmlUtils.setVal(accntData, "PWD", pwdHash);
            } else
                throw new TpmsException("WRONG PASSWORD CONFIRMATION", "ACCOUNT DATA UPDATE ABORTED");
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.EMAIL"))) {
            XmlUtils.setVal(accntData, "EMAIL", request.getParameter("FIELD.EMAIL"));
        }

        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.FIRST_NAME"))) {
            XmlUtils.setVal(accntData, "FIRST_NAME", request.getParameter("FIELD.FIRST_NAME"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.SURNAME"))) {
            XmlUtils.setVal(accntData, "SURNAME", request.getParameter("FIELD.SURNAME"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.ROLE"))) {
            XmlUtils.setVal(accntData, "ROLE", request.getParameter("FIELD.ROLE"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.DIVISION"))) {
            XmlUtils.setVal(accntData, "UNIX_GROUP", request.getParameter("FIELD.DIVISION"));
            XmlUtils.setVal(accntData, "DIVISION", request.getParameter("FIELD.DIVISION"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.HOME_DIR"))) {
            XmlUtils.setVal(accntData, "HOME_DIR", request.getParameter("FIELD.HOME_DIR"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.WORK_DIR"))) {
            XmlUtils.setVal(accntData, "WORK_DIR", request.getParameter("FIELD.WORK_DIR"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.WORK_MODE"))) {
            XmlUtils.setVal(accntData, "WORK_MODE", request.getParameter("FIELD.WORK_MODE"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.DVL_VOB"))) {
            XmlUtils.setVal(accntData, "DVL_VOB", request.getParameter("FIELD.DVL_VOB"));
        }
        if (!GeneralStringUtils.isEmptyString(request.getParameter("FIELD.REC_VOB"))) {
            XmlUtils.setVal(accntData, "REC_VOB", request.getParameter("FIELD.REC_VOB"));
        }
    }

    /**
     * recupero dati del nuovo utente e verifica
     *
     * @param request
     * @param userData
     */
    public static Element getNewAccntData(HttpServletRequest request, Element userData) throws TpmsException {

        Element newAccntData = (Element) userData.cloneNode(true);
        initUserData(newAccntData);
        String tpmsLogin = request.getParameter("FIELD.NAME");
        String ldapLogin = request.getParameter("FIELD." + UserUtils.LDAP_LOGIN_ELEMENT_TAG);
        if (UserUtils.isAccountDuplicated(tpmsLogin, ldapLogin)) {
            throw new TpmsException("DUPLICATE USER Tpms Login " + tpmsLogin + " or " + " LDAP login " + ldapLogin + " already exists", "USER INSERTION ABORTED");
        }
        setAccntData(request, newAccntData);
        checkMandatoryData(newAccntData);
        CtrlServlet.getAccntsRoot().appendChild(newAccntData);
        return newAccntData;
    }

    public static void initUserData(Element userData) throws TpmsException {
        XmlUtils.setVal(userData, "UNIX_USER", "");
        XmlUtils.setVal(userData, "PWD", "");
        XmlUtils.setVal(userData, "FIRST_NAME", "");
        XmlUtils.setVal(userData, "SURNAME", "");
        XmlUtils.setVal(userData, "DIVISION", "");
        XmlUtils.setVal(userData, "EMAIL", "");
        XmlUtils.setVal(userData, "ROLE", "");
        XmlUtils.setVal(userData, "HOME_DIR", "");
        XmlUtils.setVal(userData, "WORK_DIR", "");
        XmlUtils.setVal(userData, "WORK_MODE", "");
        XmlUtils.setVal(userData, "DVL_VOB", "");
        XmlUtils.setVal(userData, "REC_VOB", "");
        XmlUtils.setVal(userData, UserUtils.LDAP_LOGIN_ELEMENT_TAG, "");
    }

    public static void checkMandatoryData(Element userData) throws TpmsException {
        String role;
        String tpmsLogin = XmlUtils.getVal(userData, "NAME");
        if (GeneralStringUtils.isEmptyString(tpmsLogin)) throw new TpmsException("FIELD 'Tpms login' IS MANDATORY");
        if ((role = XmlUtils.getVal(userData, "ROLE")).equals("")) throw new TpmsException("FIELD 'Role' IS MANDATORY");
        if ((!role.equals("QUERY_USER")) && (XmlUtils.getVal(userData, "HOME_DIR").equals(""))) throw new TpmsException("FIELD 'Unix home dir' IS MANDATORY FOR THIS ROLE");
        if ((!role.equals("QUERY_USER")) && (XmlUtils.getVal(userData, "WORK_DIR").equals(""))) throw new TpmsException("FIELD 'Unix work dir' IS MANDATORY FOR THIS ROLE");
        if ((!role.equals("QUERY_USER")) && (XmlUtils.getVal(userData, "EMAIL").equals(""))) throw new TpmsException("FIELD 'Email' IS MANDATORY FOR THIS ROLE");
        if (XmlUtils.getVal(userData, "WORK_MODE").equals("")) throw new TpmsException("FIELD 'Default mode' IS MANDATORY");
    }

    public synchronized static void delAccntData(Element accntData) throws Exception {
        CtrlServlet.getAccntsRoot().removeChild(accntData);
    }

    static void setAllFieldsEditPerms(Element accntData, boolean bool) throws Exception {
        NodeList nl = accntData.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                ((Element) nl.item(i)).setAttribute("edit", (bool ? "Y" : "N"));
            }
        }
    }

}
