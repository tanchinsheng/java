/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: May 3, 2004
 * Time: 2:32:51 PM
 * To change this template use Options | File Templates.
 */

package tpms;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tol.LogWriter;
import tpms.utils.TpmsConfiguration;
import tpms.utils.VobConfiguration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.XmlUtils;
import it.txt.general.utils.GeneralStringUtils;

public class VobManagerServlet extends AfsGeneralServlet {
    LogWriter log = null;

    private TpmsConfiguration tpmsConfiguration = null;

    public void init() throws ServletException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();


    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String repPage = getServletConfig().getInitParameter("repPage");
        String editPageT = getServletConfig().getInitParameter("editPageT");
        String editPageR = getServletConfig().getInitParameter("editPageR");
        String editPageD = getServletConfig().getInitParameter("editPageD");
        String delPage = getServletConfig().getInitParameter("delPage");


        String action = request.getParameter("action");
        String type = ((request.getParameter("type") == null) ? "" : request.getParameter("type"));
        debugLog("VobManagerServlet :: doPost : action = " + action);
        String actionTxt = "";
        String nextPage = repPage;
        HttpSession session = request.getSession();
        Element vobData;
        boolean isNewVob;
        String vobName;
        Vector IDList;
        String user = (String) session.getAttribute("user");
        try {
            Element userData = CtrlServlet.getUserData(user);
            vobName = (request.getParameter("vobName") == null ? "" : request.getParameter("vobName"));
            isNewVob = ((request.getParameter("isNewVob") != null) && (request.getParameter("isNewVob").equals("Y")));
            vobData = (((action.equals("new")) || ((action.equals("save")) && (isNewVob)) || (action.equals("report"))) ? null : VobManager.getVobData(vobName));
            IDList = TesterInfoMgr.getPlant(tpmsConfiguration.getLocalPlant());
            NodeList vobs;
            if (action.equals("view")) {
                //FF: 05 Oct 2005 PAY ATTENTION: the following lines are the same of the case action.equals("report")
                //and the db connection isn't available!!
                debugLog("VIEW VOB MANAGER ");
                actionTxt = "VOB DATA VIEW";

                nextPage = repPage;

            }
            if ((action.equals("edit"))) {
                actionTxt = "VOB DATA MODIFICATION";
                request.setAttribute("isNewVob", "N");
                request.setAttribute("vobRec", vobData);
                request.setAttribute("localPlant", tpmsConfiguration.getLocalPlant());
                request.setAttribute("type", type);
                //setEditPermissions(vobData, userData);
                if (type.equals("D")) {
                    debugLog("get vobName edit");
                    nextPage = editPageD;
                } else if (type.equals("T")) {
                    String tVobName = request.getParameter("vobName");
                    vobs = VobConfiguration.editTVob(tVobName);
                    request.setAttribute("vobNodes", vobs);
                    nextPage = editPageT;
                } else {
                    nextPage = editPageR;
                }

            }
            if (action.equals("new")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    actionTxt = "VOB INITIALIZATION";
                    request.setAttribute("isNewVob", "Y");
                    request.setAttribute("type", type);
                    request.setAttribute("localPlant", tpmsConfiguration.getLocalPlant());
                    if (type.equals("D")) {
                        debugLog("get vobName");
                        //Chiamata unix per avere xml VOB D
                        vobs = VobConfiguration.getVobConfig("D");
                        request.setAttribute("vobNodes", vobs);
                        nextPage = editPageD;
                    } else if (type.equals("T")) {
                        debugLog("get vobName");
                        //Chiamata unix per avere xml VOB T
                        vobs = tpms.utils.VobConfiguration.getVobConfig("T");
                        request.setAttribute("vobNodes", vobs);
                        request.setAttribute("IDList", IDList);
                        nextPage = editPageT;
                    } else {
                        debugLog("get vobName");
                        //Chiamata unix per avere xml VOB R
                        vobs = tpms.utils.VobConfiguration.getVobConfig("R");
                        request.setAttribute("vobNodes", vobs);
                        request.setAttribute("IDList", IDList);
                        nextPage = editPageR;
                    }

                } else
                    throw new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            }
            if ((action.equals("save"))) {
                if (isNewVob) {
                    debugLog("New VOB saving");
                    actionTxt = "NEW VOB INSERTION";
                    vobData = getNewVobData(request, type);
                    debugLog("New VOB loaded");
                    if (type.equals("T")) {
                        vobs = VobConfiguration.getVobsNode();
                        setVobData(request, vobData, type, vobs);
                    } else {
                        vobs = VobConfiguration.getVobsNode();
                        setVobData(request, vobData, type, vobs);
                    }
                    debugLog("New VOB setted");
                    VobManager.vobsSave();
                    debugLog("New VOB saved");
                    nextPage = repPage;
                } else {
                    actionTxt = "VOB DATA MODIFICATION";
                    request.setAttribute("vobName", vobName);
                    if (type.equals("D")) {
                        vobData = XmlUtils.removeChilds(vobData, "TVOB");
                        if (request.getParameter("FIELD.TVOB") != null) {
                            Vector tVobs = setMultiLineVector(request.getParameter("FIELD.TVOB") != null ? request.getParameter("FIELD.TVOB") : "");
                            for (int j = 0; j < tVobs.size(); j++) {
                                XmlUtils.addEl(vobData, "TVOB", (String) tVobs.elementAt(j));
                            }
                        }
                    }
                    String vobDescription = GeneralStringUtils.isEmptyString(request.getParameter("FIELD.DESC")) ? "" : request.getParameter("FIELD.DESC");
                    XmlUtils.setVal(vobData, "DESC", vobDescription);
                    debugLog("VobManagerServlet :: doPost : Update Vob started...");
                    VobManager.vobsSave();
                    debugLog("VobManagerServlet :: doPost : Update Vob accomplisched");
                    nextPage = delPage;
                }
            }

            if (action.equals("delete")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    actionTxt = "VOB DELETION";
                    delVobData(vobData);
                    VobManager.vobsSave();
                    nextPage = delPage;
                } else
                    throw new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            }


            if (action.equals("report")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    //FF: 05 Oct 2005 PAY ATTENTION: the following lines are the same of the case action.equals("view") case!!
                    debugLog("REPORT VOB from xml");
                    actionTxt = "TESTER INFO DATA VIEW";

                    nextPage = repPage;
                } else
                    throw new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            }
        }
        catch (Exception e) {
            if (e instanceof TpmsException) ((TpmsException) e).setAction(actionTxt + " FAILURE");
            request.setAttribute("exception", e);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher(CtrlServlet._caughtErrPage);
            rDsp.forward(request, response);
        }

        RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + nextPage);
        rDsp.forward(request, response);
    }

    public static void setEditPermissions(Element testerData, Element userData) throws Exception {
        setAllFieldsEditPerms(testerData, false);
        if (CtrlServlet.isUserAdmin(userData)) {
            setAllFieldsEditPerms(testerData, true);
        }

    }


    public synchronized static void setVobData(HttpServletRequest req, Element vobData, String type, NodeList vobNodes) throws Exception {
        if (req.getParameter("FIELD.NAME") != null) {
            XmlUtils.setVal(vobData, "NAME", req.getParameter("FIELD.NAME"));
        }
        if (req.getParameter("FIELD.DESC") != null) {
            XmlUtils.setVal(vobData, "DESC", req.getParameter("FIELD.DESC"));
        }
        if (type.equals("D")) {
            if (req.getParameter("FIELD.DIVISION") != null) {
                XmlUtils.setVal(vobData, "DIVISION", req.getParameter("FIELD.DIVISION"));
            }
        }

        if (type.equals("T")) {
            Vector unixGroups;
            unixGroups = tpms.utils.VobConfiguration.getUnixGroupOfVob(vobNodes, req.getParameter("FIELD.NAME"));

            for (int i = 0; i < unixGroups.size(); i++) {
                XmlUtils.addEl(vobData, "DIVISION", (String) unixGroups.elementAt(i));
            }
        }

        if (req.getParameter("FIELD.PLANT") != null) {
            XmlUtils.setVal(vobData, "PLANT", req.getParameter("FIELD.PLANT"));
        }
        if (req.getParameter("FIELD.TYPE") != null) {
            XmlUtils.setVal(vobData, "TYPE", req.getParameter("FIELD.TYPE"));
        }

        if (type.equals("D")) {
            vobData = XmlUtils.removeChilds(vobData, "TVOB");
            if (req.getParameter("FIELD.TVOB") != null) {
                Vector tVobs = setMultiLineVector(req.getParameter("FIELD.TVOB") != null ? req.getParameter("FIELD.TVOB") : "");
                for (int j = 0; j < tVobs.size(); j++) {
                    XmlUtils.addEl(vobData, "TVOB", (String) tVobs.elementAt(j));
                }
            }
        }
    }

    public static Element getNewVobData(HttpServletRequest req, String type) throws TpmsException {
        Element newVobData = VobManager.getNewVob(type);
        initVobData(newVobData, type);
        Element duplicateVobData = null;
        String vobName = req.getParameter("FIELD.NAME");
        try {
            duplicateVobData = VobManager.getVobData(vobName);
        }
        catch (TpmsException e) {
            errorLog("VobManagerServlet :: getNewVobData error", e);
        }

        if (duplicateVobData != null) {
            throw new TpmsException("DUPLICATE VOB " + vobName, "VOB INSERTION ABORTED");
        }
        VobManager.vobsRoot.appendChild(newVobData);
        return newVobData;
    }

    public static void initVobData(Element vobData, String type) throws TpmsException {
        XmlUtils.setVal(vobData, "NAME", "");
        XmlUtils.setVal(vobData, "DESC", "");
        if (type.equals("D")) {
            XmlUtils.setVal(vobData, "DIVISION", "");
        }
        XmlUtils.setVal(vobData, "PLANT", "");
        XmlUtils.setVal(vobData, "TYPE", "");
    }


    public synchronized static void delVobData(Element vobData) throws Exception {
        VobManager.vobsRoot.removeChild(vobData);
    }

    static void setAllFieldsEditPerms(Element testerData, boolean bool) throws Exception {
        NodeList nl = testerData.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                ((Element) nl.item(i)).setAttribute("edit", (bool ? "Y" : "N"));
            }
        }
    }

    static void removeAllFieldsEditPerms(Element testerData) throws Exception {
        NodeList nl = testerData.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                ((Element) nl.item(i)).removeAttribute("edit");
            }
        }
    }

    public static Vector setMultiLineVector(String val) {
        Vector tvobs = new Vector();
        StringTokenizer tknzr = new StringTokenizer(val, "\n", false);
        for (int i = 0; tknzr.hasMoreTokens(); i++) {
            tvobs.add(i, tknzr.nextToken().trim());

        }
        return tvobs;
    }


}

