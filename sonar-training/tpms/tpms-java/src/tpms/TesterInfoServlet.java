/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: May 3, 2004
 * Time: 2:32:51 PM
 * To change this template use Options | File Templates.
 */

package tpms;


import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tol.LogWriter;
import tol.oneConnDbWrtr;
import tpms.utils.TpmsConfiguration;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TesterInfoServlet extends AfsGeneralServlet {
    LogWriter log = null;
    String _localPlant;
    String _TpmsInstID;
    String _webAppDir;
    private TpmsConfiguration tpmsConfiguration = null;


    public void init() throws ServletException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        _localPlant = tpmsConfiguration.getLocalPlant();
        _webAppDir = tpmsConfiguration.getWebAppDir();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String repPage = getServletConfig().getInitParameter("repPage");
        String viewPage = getServletConfig().getInitParameter("viewPage");
        String editPage = getServletConfig().getInitParameter("editPage");
        String delPage = getServletConfig().getInitParameter("delPage");

        String action = request.getParameter("action");
        String plant = (((request.getParameter("plant") == null) || ((request.getParameter("plant")).equals(""))) ? _localPlant : request.getParameter("plant"));
        String actionTxt = "";
        String nextPage = viewPage;
        HttpSession session = request.getSession();
        Element testerData;
        boolean isNewTester;
        String testerName;
        String currentUser = (String) session.getAttribute("user");
        try {

            Element userData = CtrlServlet.getUserData(currentUser);
            testerName = (request.getParameter("testerName") == null ? "" : request.getParameter("testerName"));
            isNewTester = ((request.getParameter("isNewTester") != null) && (request.getParameter("isNewTester").equals("Y")));
            testerData = (((action.equals("new")) || ((action.equals("save")) && (isNewTester))) ? null : TesterInfoMgr.getTesterInfoData(_localPlant, testerName));

            if (action.equals("view")) {
                //FF: 05 Oct 2005 PAY ATTENTION: the following lines are the same of the case action.equals("report")
                //and the db connection isn't available!!
                debugLog("VIEW TESTER_INFO ");
                actionTxt = "TESTER INFO DATA VIEW";
                request.setAttribute("plant", plant);
                nextPage = repPage;
            }
            if ((action.equals("edit"))) {
                actionTxt = "TESER INFO DATA MODIFICATION";
                request.setAttribute("isNewTester", "N");
                request.setAttribute("testerRec", testerData);
                request.setAttribute("localPlant", _localPlant);
                setEditPermissions(testerData, userData);
                nextPage = editPage;
            }
            if (action.equals("new")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    actionTxt = "TESTER INFO INITIALIZAZTION";
                    request.setAttribute("isNewTester", "Y");
                    request.setAttribute("localPlant", _localPlant);
                    nextPage = editPage;
                } else
                    throw new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            }
            if ((action.equals("save"))) {
                if (isNewTester) {
                    actionTxt = "NEW TESTER INFO INSERTION";


                    TesterInfoMgr.insertTesterInformation(request);
                    nextPage = repPage;
                } else {
                    actionTxt = "TESTER INFO DATA MODIFICATION";
                    request.setAttribute("testerName", testerName);
                    TesterInfoMgr.updateTesterInformation(request, testerData);
                    nextPage = delPage;
                }
            }

            if (action.equals("delete")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    actionTxt = "TESTER INFO DELETION";
                    TesterInfoMgr.deleteTesterInformation(testerData, session.getId(), currentUser);
                    nextPage = delPage;
                } else {
                    TpmsException e = new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
                    manageError(e, request, response);
                }
            }


            if (action.equals("report")) {
                if (CtrlServlet.isUserAdmin(userData)) {
                    actionTxt = "TESTER INFO DATA FETCH";
                    GetDbTesterInfosAction getDbTesterInfosAction = new GetDbTesterInfosAction(log, testerData, _localPlant, (oneConnDbWrtr) this.getServletContext().getAttribute("dbWriter"));
                    Vector plantList = null;
                    try {
                        plantList = getDbTesterInfosAction.getPlantList(_localPlant);
                    } catch (Exception e) {
                        //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        //FF 05 Oct 2005: no action is taken if this exception is raised because it will be managed checking that plantlist != null
                    }

                    if (plantList != null) {
                        for (int j = 0; j < plantList.size(); j++) {

                            // mi faccio restituire il result set del plant
                            ResultSet testerInfos = getDbTesterInfosAction.getTesterInfos((String) plantList.get(j));
                            Element newTesterData;
                            // controllo che il nodo del plant esista gia
                            if (TesterInfoMgr.findTesterInfosRoot((String) plantList.get(j)) != null) {
                                // se esiste cancello tutti i suoi figli e gli creo nuovi figli con i dati del result set
                                TesterInfoMgr.removeAllTesterInfo((String) plantList.get(j));
                                while (testerInfos.next()) {
                                    newTesterData = TesterInfoMgr.getNewTesterInfo((String) plantList.get(j));
                                    TesterInfoMgr.initTesterData(newTesterData);
                                    setTesterDataFromDB(testerInfos, newTesterData);
                                }
                            } else {
                                // se non esiste creo un nuovo elemento <PLANT name = palnt > e a questo aggingo i nuovi dati
                                Element rootDocument = TesterInfoMgr.getTesterInfosRootDocument(_localPlant);
                                Element plantElem = TesterInfoMgr.setNewPlantElem(rootDocument, (String) plantList.get(j));
                                while (testerInfos.next()) {
                                    newTesterData = TesterInfoMgr.getNewPlantTesterInfo(plantElem);
                                    TesterInfoMgr.initTesterData(newTesterData);
                                    setTesterDataFromDB(testerInfos, newTesterData);
                                }
                            }
                            TesterInfoMgr.testersSave((String) plantList.get(j));
                            try { //add 17th Dec 2007 to close statement
                                if (testerInfos != null)
                                	testerInfos.close();
                            } catch (SQLException e) {
                                errorLog("TesterInfoServlet :: doPost : error while closing resultset", e);
                            }                           
                        }


                    } else {
                        //throw new TpmsException("TESTER_INFO DB CONNECTION FAILURE");
                        //FF: 05 Oct 2005 PAY ATTENTION: the following lines are the same of the case action.equals("view") case!!
                        debugLog("REPORT TESTER_INFO from xml");
                        actionTxt = "TESTER INFO DATA VIEW";
                        request.setAttribute("plant", plant);

                    }
                    nextPage = repPage;
                } else {
                    TpmsException e = new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
                    manageError(e, request, response);
                }
            }
        }
        catch (Exception e) {
            if (e instanceof TpmsException) ((TpmsException) e).setAction(actionTxt + " FAILURE");
            manageError(e, request, response);
        }

        manageRedirect(nextPage, request, response);
    }

    public static void setEditPermissions(Element testerData, Element userData) throws Exception {
        setAllFieldsEditPerms(testerData, false);
        if (CtrlServlet.isUserAdmin(userData)) {
            setAllFieldsEditPerms(testerData, true);
        }

    }




    public synchronized static void setTesterDataFromDB(ResultSet result, Element testerData) throws Exception {
        if (result.getObject("TESTER_INFO_SHOW") != null) {
            XmlUtils.setVal(testerData, "TESTER_INFO_SHOW", (String) result.getObject("TESTER_INFO_SHOW"));
        }

        if (result.getObject("TESTER_FAMILY") != null) {
            XmlUtils.setVal(testerData, "TESTER_FAMILY", (String) result.getObject("TESTER_FAMILY"));
        }

        if (result.getObject("TST_INFO_CODE") != null) {
            XmlUtils.setVal(testerData, "TST_INFO", (String) result.getObject("TST_INFO_CODE"));
        }

        if (result.getObject("TESTER_MODEL") != null) {
            XmlUtils.setVal(testerData, "TESTER_MODEL", (String) result.getObject("TESTER_MODEL"));
        }

        if (result.getObject("SW_NAME") != null) {
            XmlUtils.setVal(testerData, "SW_NAME", (String) result.getObject("SW_NAME"));
        }

        if (result.getObject("SW_VERSION") != null) {
            XmlUtils.setVal(testerData, "SW_VERSION", (String) result.getObject("SW_VERSION"));
        }

        if (result.getObject("UNIX_OS") != null) {
            XmlUtils.setVal(testerData, "UNIX_OS", (String) result.getObject("UNIX_OS"));
        }
    }





    public static void checkMandatoryData(Element userData) throws Exception {
        String role;
        if ((role = XmlUtils.getVal(userData, "ROLE")).equals("")) throw new TpmsException("FIELD 'Role' IS MANDATORY");
        if ((!role.equals("QUERY_USER")) && (XmlUtils.getVal(userData, "UNIX_USER").equals(""))) throw new TpmsException("FIELD 'Unix login' IS MANDATORY FOR THIS ROLE");
        if ((!role.equals("QUERY_USER")) && (XmlUtils.getVal(userData, "HOME_DIR").equals(""))) throw new TpmsException("FIELD 'Unix home dir' IS MANDATORY FOR THIS ROLE");
        if ((!role.equals("QUERY_USER")) && (XmlUtils.getVal(userData, "WORK_DIR").equals(""))) throw new TpmsException("FIELD 'Unix work dir' IS MANDATORY FOR THIS ROLE");
        if ((!role.equals("QUERY_USER")) && (XmlUtils.getVal(userData, "EMAIL").equals(""))) throw new TpmsException("FIELD 'Email' IS MANDATORY FOR THIS ROLE");
        if (XmlUtils.getVal(userData, "WORK_MODE").equals("")) throw new TpmsException("FIELD 'Default mode' IS MANDATORY");
        if (XmlUtils.getVal(userData, "PWD").equals("")) throw new TpmsException("FIELD 'Password' IS MANDATORY");
    }



    static void setAllFieldsEditPerms(Element testerData, boolean bool) throws Exception {
        NodeList nl = testerData.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                ((Element) nl.item(i)).setAttribute("edit", (bool ? "Y" : "N"));
            }
        }
    }




}


