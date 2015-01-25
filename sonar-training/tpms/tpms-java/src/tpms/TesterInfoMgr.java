package tpms;

import it.txt.general.utils.FileUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tpms.utils.QueryUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class TesterInfoMgr extends QueryUtils {
    static Element testerInfosRoot;

    static {
        try {
            TesterInfoMgr.setTesterInfosRoot(XmlUtils.getRoot(CtrlServlet._testerInfosFileName));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public static void setTesterInfosRoot(Element root) {
        testerInfosRoot = root;
    }

    public static Vector getTesterFamilyList(String plant) {
        Element root = getTesterInfosRoot(plant);
        return getUniqueVals(root.getElementsByTagName("TESTER_FAMILY"));
    }

    public static String getTesterInfoValFromDesk(String plant, String testerInfoShow) {
        String testInfo = "";
        Element root = getTesterInfosRoot(plant);
        if (root == null) {
            return testInfo;
        }

        NodeList lst = root.getElementsByTagName("TESTER_INFO");
        Element el = XmlUtils.findEl(lst, "TESTER_INFO_SHOW", testerInfoShow);
        if (el == null) {
            return testInfo;
        }

        return XmlUtils.getVal(el, "TST_INFO");

    }

    public static Element getTesterInfoData(String plant, String testerInfoShow) throws TpmsException {
        Element root = getTesterInfosRoot(plant);
        if (root == null) {
            throw new TpmsException("ROOT UNKNOWN", "ACTION FAILED");
        }

        NodeList lst = root.getElementsByTagName("TESTER_INFO");
        Element el = XmlUtils.findEl(lst, "TESTER_INFO_SHOW", testerInfoShow);
        if (el == null) {
            return root;
        }

        return el;

    }

    public static void removeAllTesterInfo(String plant) throws TpmsException {
        Element root = getTesterInfosRoot(plant);
        if (root == null) {
            throw new TpmsException("ROOT UNKNOWN", "ACTION FAILED");
        }

        NodeList nl = root.getElementsByTagName("TESTER_INFO");
        Vector nodeVector = new Vector();
        //put the nodes in a vector because the nodelist is a collection
        for (int i = 0; i < nl.getLength(); i++) {
            nodeVector.add(nl.item(i));
        }

        for (int k = 0; k < nodeVector.size(); k++) {
            root.removeChild((Node) nodeVector.get(k));
        }
    }

    public static Element getNewTesterInfo(String plant) throws TpmsException {
        Element root = getTesterInfosRoot(plant);
        if (root == null) {
            throw new TpmsException("ROOT UNKNOWN", "ACTION FAILED");
        }
        Element tester_info = XmlUtils.addEl(root, "TESTER_INFO");

        XmlUtils.addEl(tester_info, "TESTER_FAMILY");
        XmlUtils.addEl(tester_info, "TESTER_INFO_SHOW");
        XmlUtils.addEl(tester_info, "TST_INFO");
        XmlUtils.addEl(tester_info, "TESTER_MODEL");
        XmlUtils.addEl(tester_info, "SW_NAME");
        XmlUtils.addEl(tester_info, "SW_VERSION");
        XmlUtils.addEl(tester_info, "UNIX_OS");

        return tester_info;
    }

    public static Element setNewPlantElem(Element rootDocument, String new_plant) throws Exception {
        Element plantElem = XmlUtils.addEl(rootDocument, "PLANT");
        plantElem.setAttribute("name", new_plant);
        return plantElem;
    }

    public static Element getNewPlantTesterInfo(Element plantElem) throws Exception {
        Element tester_info = XmlUtils.addEl(plantElem, "TESTER_INFO");
        XmlUtils.addEl(tester_info, "TESTER_FAMILY");
        XmlUtils.addEl(tester_info, "TESTER_INFO_SHOW");
        XmlUtils.addEl(tester_info, "TST_INFO");
        XmlUtils.addEl(tester_info, "TESTER_MODEL");
        XmlUtils.addEl(tester_info, "SW_NAME");
        XmlUtils.addEl(tester_info, "SW_VERSION");
        XmlUtils.addEl(tester_info, "UNIX_OS");

        return tester_info;
    }

    public static Element findTesterInfoData(String plant, String testerInfoShow) throws TpmsException {
        Element root = getTesterInfosRoot(plant);
        if (root == null) {
            throw new TpmsException("ROOT UNKNOWN", "ACTION FAILED");
        }

        NodeList lst = root.getElementsByTagName("TESTER_INFO");
        return XmlUtils.findEl(lst, "TESTER_INFO_SHOW", testerInfoShow);

    }


    public static Vector getTesterInfoShowList(String plant, String testerFamily) {
        Element root = getTesterInfosRoot(plant);
        if (root == null) {
            return new Vector();
        }
        if (testerFamily == null) {
            return getUniqueVals(root.getElementsByTagName("TESTER_INFO_SHOW"));
        } else {
            NodeList lst = root.getElementsByTagName("TESTER_INFO");
            Vector v = XmlUtils.findEls(lst, "TESTER_FAMILY", testerFamily);
            if (v == null) {
                return new Vector();
            }
            Vector v2 = new Vector();
            for (int i = 0; i < v.size(); i++) {
                v2.addElement(XmlUtils.getChild((Element) v.elementAt(i), "TESTER_INFO_SHOW"));
            }
            return v2;
        }
    }

    public static Vector getPlant(String plant_id) throws Exception {
        Vector attrVector = new Vector();
        Element root = getTesterInfosRootDocument(plant_id);
        if (root == null) {
            throw new TpmsException("ROOT UNKNOWN", "ACTION FAILED");
        }

        NodeList nl = root.getElementsByTagName("PLANT");
        if (nl != null) {
            Element el;
            for (int k = 0; k < nl.getLength(); k++) {
                el = (Element) nl.item(k);
                if (el != null) {
                    attrVector.addElement(el.getAttribute("name"));
                }
            }
        }

        return attrVector;
    }

    public static Element getTesterInfosRoot(String plant) {
        return (plant != null ? XmlUtils.findElwithAttr(testerInfosRoot.getElementsByTagName("PLANT"), "name", plant) : testerInfosRoot);
    }

    public static Element getTesterInfosRootDocument(String plant_id) throws TpmsException {
        Element root = getTesterInfosRoot(plant_id);
        if (root != null) {
            root = root.getOwnerDocument().getDocumentElement();
        }
        if (root == null) {
            throw new TpmsException("TESTER INFO ROOT UNKNOWN:<br> please verify your <br>" + CtrlServlet._testerInfosFileName + "<br>configuration file", "ACTION FAILED");
        }
        return root;
    }

    public static Element findTesterInfosRoot(String plant)  {
        return XmlUtils.findElwithAttr(testerInfosRoot.getElementsByTagName("PLANT"), "name", plant);
    }

    static Vector getUniqueVals(NodeList nl) {
        Vector v = new Vector();
        for (int i = 0; i < nl.getLength(); i++) {
            String val = XmlUtils.getVal((Element) nl.item(i));
            boolean found = false;
            for (int j = 0; j < v.size(); j++) {
                if (XmlUtils.getVal((Element) v.elementAt(j)).equals(val)) {
                    found = true;
                    break;
                }
            }
            if (!found) v.addElement(nl.item(i));
        }
        return v;
    }

    public synchronized static void testersSave(String plant) throws TpmsException {
        try {
            FileWriter fout = new FileWriter(CtrlServlet._testerInfosFileName);
            Element root = getTesterInfosRoot(plant);

            XmlUtils.print(root.getOwnerDocument(), fout);
            fout.flush();
            fout.close();
        }
        catch (Exception e) {
            throw new TpmsException("ACCOUNTS XML FILE SAVE FAILURE", "", e);
        }
    }


    public static void initTesterData(Element testerData) throws TpmsException {
        XmlUtils.setVal(testerData, "TESTER_INFO_SHOW", "");
        XmlUtils.setVal(testerData, "TESTER_FAMILY", "");
        XmlUtils.setVal(testerData, "TST_INFO", "");
        XmlUtils.setVal(testerData, "TESTER_MODEL", "");
        XmlUtils.setVal(testerData, "SW_NAME", "");
        XmlUtils.setVal(testerData, "SW_VERSION", "");
        XmlUtils.setVal(testerData, "UNIX_OS", "");
    }


    public static Element getNewTesterData(HttpServletRequest request, String localPlant) throws TpmsException {
        Element newTesterData = getNewTesterInfo(localPlant);
        initTesterData(newTesterData);
        Element duplicateTesterData = null;
        String testerName = request.getParameter("FIELD.TESTER_INFO_SHOW");
        try {
            duplicateTesterData = TesterInfoMgr.findTesterInfoData(localPlant, testerName);
        }
        catch (TpmsException e) {
            errorLog("TesterInfoServlet :: getNewTesterData : error", e);
        }

        if (duplicateTesterData != null) {
            throw new TpmsException("DUPLICATE TESTER INFO " + testerName, "TESTER INSERTION ABORTED");
        }
        getTesterInfosRoot(localPlant).appendChild(newTesterData);
        return newTesterData;
    }

    public synchronized static void setTesterData(HttpServletRequest req, Element testerData) throws TpmsException {
        if (req.getParameter("FIELD.TESTER_INFO_SHOW") != null) {
            XmlUtils.setVal(testerData, "TESTER_INFO_SHOW", req.getParameter("FIELD.TESTER_INFO_SHOW"));
        }
        if (req.getParameter("FIELD.TESTER_FAMILY") != null) {
            XmlUtils.setVal(testerData, "TESTER_FAMILY", req.getParameter("FIELD.TESTER_FAMILY"));
        }
        if (req.getParameter("FIELD.TST_INFO") != null) {
            XmlUtils.setVal(testerData, "TST_INFO", req.getParameter("FIELD.TST_INFO"));
        }
        if (req.getParameter("FIELD.TESTER_MODEL") != null) {
            XmlUtils.setVal(testerData, "TESTER_MODEL", req.getParameter("FIELD.TESTER_MODEL"));
        }
        if (req.getParameter("FIELD.SW_NAME") != null) {
            XmlUtils.setVal(testerData, "SW_NAME", req.getParameter("FIELD.SW_NAME"));
        }
        if (req.getParameter("FIELD.SW_VERSION") != null) {
            XmlUtils.setVal(testerData, "SW_VERSION", req.getParameter("FIELD.SW_VERSION"));
        }
        if (req.getParameter("FIELD.UNIX_OS") != null) {
            XmlUtils.setVal(testerData, "UNIX_OS", req.getParameter("FIELD.UNIX_OS"));
        }
    }


    public static void rollbackTesterInfoFile(String originalFileName, String backupFileName) throws IOException {
        File originalFile = new File(originalFileName);
        File backupFile = new File(backupFileName);
        FileUtils.copy(backupFile, originalFile);
        backupFile.delete();
    }


    public synchronized static void insertTesterInformation(HttpServletRequest request) throws TpmsException {

        String testerInfoFileName = CtrlServlet._testerInfosFileName;
        String testerInfoBackupFileName = CtrlServlet._testerInfosFileName + "." + System.currentTimeMillis();

        try {
            FileUtils.copy(testerInfoFileName, testerInfoBackupFileName);
        } catch (IOException e) {
            errorLog("TesterInfoMgr :: insertTesterInformation : IOException while backing-up tester info file " + e.getMessage(), e);
            throw new TpmsException("Unable to insert tester informations", "tester info insert", e.getMessage() + " while backing up tester info file<br>" + testerInfoFileName);
        }

        File testerInformationBackupFile = new File(testerInfoBackupFileName);

        /************Recupero dati dalla request********************************/
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("user");
        String testerFamily = request.getParameter("FIELD.TESTER_FAMILY");
        String testerInfoShow = request.getParameter("FIELD.TESTER_INFO_SHOW");
        String testerInfo = request.getParameter("FIELD.TST_INFO");
        String testerModel = request.getParameter("FIELD.TESTER_MODEL");
        String testerSoftwareName = request.getParameter("FIELD.SW_NAME");
        String testerSoftwareVersion = request.getParameter("FIELD.SW_VERSION");
        String testerUnixOs = request.getParameter("FIELD.UNIX_OS");

        /*************Inserisco le info nel db*********************/
        String query = "INSERT INTO TESTER_INFO " +
                "( " +
                " INSTALLATION_ID, " +
                " TPMS_ADMIN_EMAIL, " +
                " TESTER_FAMILY, " +
                " TESTER_INFO_SHOW, " +
                " TST_INFO_CODE," +
                " TESTER_MODEL," +
                " SW_NAME," +
                " SW_VERSION," +
                " UNIX_OS" +
                ") " +
                "VALUES " +
                "( '" +
                tpmsConfiguration.getLocalPlant() + "'," +
                "'email_admin'" + "," +
                getStringValueForQuery(testerFamily) + "," +
                getStringValueForQuery(testerInfoShow) + "," +
                getStringValueForQuery(testerInfo) + "," +
                getStringValueForQuery(testerModel) + "," +
                getStringValueForQuery(testerSoftwareName) + "," +
                getStringValueForQuery(testerSoftwareVersion) + "," +
                getStringValueForQuery(testerUnixOs) +
                ") ";

        debugLog("TesterInfoMgr :: insertTesterInformation : query = " + query);

        boolean queryResult = executeUpdateQuery(query, session.getId(), currentUser, false);
        if (queryResult) {
            try {
                /*************Aggiorno l'xml *****************************/
                Element testerData = getNewTesterData(request, tpmsConfiguration.getLocalPlant());
                setTesterData(request, testerData);

                testersSave(tpmsConfiguration.getLocalPlant());
            } catch (TpmsException e) {

                String detailMessage = "";
                try {
                    rollbackTesterInfoFile(testerInfoFileName, testerInfoBackupFileName);
                } catch (IOException e2) {
                    errorLog("TesterInfoMgr :: insertTesterInformation : 0 unable to rollback Tester info file " + e2.getMessage(), e2);
                    detailMessage = "Unable to rollback Tester information file: your configuration and db data are not alligned<br>Backup filename " + testerInfoBackupFileName;
                }
                try {
                    dbWriter.rollback();
                } catch (Exception e1) {
                    errorLog("TesterInfoMgr :: insertTesterInformation : 0 unable to rollback connection ", e1);
                }
                //rilanciare eccezione con errore nell'aggiornamento dell'xml...
                throw new TpmsException("Tester Information insert action failed to update xml file " + e.getMessage(), "tester info insert", detailMessage);
            }
        } else {
            testerInformationBackupFile.delete();
            throw new TpmsException("Unable to insert Tester information into database", "tester info insert", "");
        }
        try {
            dbWriter.commit();
        } catch (Exception e) {
            String detailMessage = "";
            try {
                rollbackTesterInfoFile(testerInfoFileName, testerInfoBackupFileName);
            } catch (IOException e2) {
                errorLog("TesterInfoMgr :: insertTesterInformation : 1 unable to rollback Tester info file " + e2.getMessage(), e2);
                detailMessage = "Unable to rollback Tester information file: your configuration and db data are not alligned<br>Backup filename " + testerInfoBackupFileName;
            }
            //rilanciare eccezione con errore nell'aggiornamento dell'xml...
            throw new TpmsException("Tester Information insert action failed to commit sql insert statement", "tester info insert", detailMessage);
        }
        //rimuovi backup...
        testerInformationBackupFile.delete();
    }

    private static String generateUpdateTesterInformationQuery(String testerName, String testerFamily, String testerInfoShow, String testerInfo, String testerModel, String testerSoftwareName, String testerSoftwareVersion, String testerUnixOs) {
        String query = null;
        try {
            if (searchTester(testerName) == 0) {
                query = "INSERT INTO TESTER_INFO " +
                        "( " +
                        " INSTALLATION_ID, " +
                        " TPMS_ADMIN_EMAIL, " +
                        " TESTER_FAMILY, " +
                        " TESTER_INFO_SHOW, " +
                        " TST_INFO_CODE," +
                        " TESTER_MODEL," +
                        " SW_NAME," +
                        " SW_VERSION," +
                        " UNIX_OS" +
                        ") " +
                        "VALUES " +
                        "( '" +
                        tpmsConfiguration.getLocalPlant() + "'," +
                        "'email_admin'" + "," +
                        getStringValueForQuery(testerFamily) + "," +
                        getStringValueForQuery(testerInfoShow) + "," +
                        getStringValueForQuery(testerInfo) + "," +
                        getStringValueForQuery(testerModel) + "," +
                        getStringValueForQuery(testerSoftwareName) + "," +
                        getStringValueForQuery(testerSoftwareVersion) + "," +
                        getStringValueForQuery(testerUnixOs) +
                        ") ";
                debugLog("QUERY INSERT TESTER INFO= " + query);
            } else {
                debugLog("UPDATE DB TESTER_INFO TRANSACTION");
                query = "UPDATE TESTER_INFO SET " +
                        " INSTALLATION_ID='" + tpmsConfiguration.getLocalPlant() + "'," +
                        " TPMS_ADMIN_EMAIL = 'email_admin', " +
                        " TESTER_FAMILY=" + getStringValueForQuery(testerFamily) + ", " +
                        " TESTER_INFO_SHOW=" + getStringValueForQuery(testerInfoShow) + "," +
                        " TST_INFO_CODE=" + getStringValueForQuery(testerInfo) + ", " +
                        " TESTER_MODEL=" + getStringValueForQuery(testerModel) + ", " +
                        " SW_NAME=" + getStringValueForQuery(testerModel) + ", " +
                        " SW_VERSION=" + getStringValueForQuery(testerSoftwareVersion) + ", " +
                        " UNIX_OS=" + getStringValueForQuery(testerUnixOs) + " " +
                        " WHERE INSTALLATION_ID = '" + tpmsConfiguration.getLocalPlant() + "' AND " +
                        " TESTER_INFO_SHOW = " + getStringValueForQuery(testerName) + "";
                debugLog("QUERY UPDATE TESTER INFO= " + query);
            }
        } catch (Exception e) {
            errorLog("UpdateDbTesterInfoAction :: generateQuery : unable to generate query!!", e);
        }

        return query;
    }

    private static int searchTester(String testerName) {
        try {
            Vector v = new Vector();
            dbWriter.getRows(v,
                    "SELECT TESTER_INFO_SHOW " +
                            "FROM TESTER_INFO" +
                            " WHERE INSTALLATION_ID = '" + tpmsConfiguration.getLocalPlant() + "' AND " +
                            "TESTER_INFO_SHOW = '" + testerName + "'");

            return v.size();
        } catch (Exception e) {
            return -1;
        }
    }


    static void removeAllFieldsEditPerms(Element testerData) {
        NodeList nl = testerData.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                ((Element) nl.item(i)).removeAttribute("edit");
            }
        }
    }

    public synchronized static void updateTesterInformation(HttpServletRequest request, Element testerData) throws TpmsException {

        String testerInfoFileName = CtrlServlet._testerInfosFileName;
        String testerInfoBackupFileName = CtrlServlet._testerInfosFileName + "." + System.currentTimeMillis();

        try {
            FileUtils.copy(testerInfoFileName, testerInfoBackupFileName);
        } catch (IOException e) {
            errorLog("TesterInfoMgr :: updateTesterInformation : IOException while backing-up tester info file " + e.getMessage(), e);
            throw new TpmsException("Unable to update tester information", "tester info update", e.getMessage() + " while backing up tester info file<br>" + testerInfoFileName);
        }

        File testerInformationBackupFile = new File(testerInfoBackupFileName);

        /************Recupero dati dalla request********************************/
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("user");
        String testerFamily = request.getParameter("FIELD.TESTER_FAMILY");
        String testerInfoShow = request.getParameter("FIELD.TESTER_INFO_SHOW");
        String testerInfo = request.getParameter("FIELD.TST_INFO");
        String testerModel = request.getParameter("FIELD.TESTER_MODEL");
        String testerSoftwareName = request.getParameter("FIELD.SW_NAME");
        String testerSoftwareVersion = request.getParameter("FIELD.SW_VERSION");
        String testerUnixOs = request.getParameter("FIELD.UNIX_OS");

        String oldTesterName = XmlUtils.getVal(testerData, "TESTER_INFO_SHOW");

        String query = generateUpdateTesterInformationQuery(oldTesterName, testerFamily, testerInfoShow, testerInfo, testerModel, testerSoftwareName, testerSoftwareVersion, testerUnixOs);

        boolean queryResult = executeUpdateQuery(query, session.getId(), currentUser, false);

        if (queryResult) {
            try {
                /*************Aggiorno l'xml *****************************/
                removeAllFieldsEditPerms(testerData);
                setTesterData(request, testerData);
                testersSave(tpmsConfiguration.getLocalPlant());
            } catch (TpmsException e) {
                String detailMessage = "";
                try {
                    rollbackTesterInfoFile(testerInfoFileName, testerInfoBackupFileName);
                } catch (IOException e2) {
                    errorLog("TesterInfoMgr :: updateTesterInformation : 0 unable to rollback Tester info file " + e2.getMessage(), e2);
                    detailMessage = "Unable to rollback Tester information file: your configuration and db data are not alligned<br>Backup filename " + testerInfoBackupFileName;
                }
                try {
                    dbWriter.rollback();
                } catch (Exception e1) {
                    errorLog("TesterInfoMgr :: updateTesterInformation : 0 unable to rollback connection ", e1);
                }
                //rilanciare eccezione con errore nell'aggiornamento dell'xml...
                throw new TpmsException("Tester Information update action failed to update xml file " + e.getMessage(), "tester info update", detailMessage);
            }
        } else {
            testerInformationBackupFile.delete();
            throw new TpmsException("Unable to update Tester information into database", "tester info update", "");
        }
        try {
            dbWriter.commit();
        } catch (Exception e) {
            String detailMessage = "";
            try {
                rollbackTesterInfoFile(testerInfoFileName, testerInfoBackupFileName);
            } catch (IOException e2) {
                errorLog("TesterInfoMgr :: updateTesterInformation : 1 unable to rollback Tester info file " + e2.getMessage(), e2);
                detailMessage = "Unable to rollback Tester information file: your configuration and db data are not alligned<br>Backup filename " + testerInfoBackupFileName;
            }
            //rilanciare eccezione con errore nell'aggiornamento dell'xml...
            throw new TpmsException("Tester Information update action failed to commit sql update statement", "tester info update", detailMessage);
        }
        //rimuovi backup...
        testerInformationBackupFile.delete();
    }

    public synchronized static void deleteTesterInformation(Element testerData, String sessionId, String currentUser) throws TpmsException {

        String testerInfoFileName = CtrlServlet._testerInfosFileName;
        String testerInfoBackupFileName = CtrlServlet._testerInfosFileName + "." + System.currentTimeMillis();

        try {
            FileUtils.copy(testerInfoFileName, testerInfoBackupFileName);
        } catch (IOException e) {
            errorLog("TesterInfoMgr :: deleteTesterInformation : IOException while backing-up tester info file " + e.getMessage(), e);
            throw new TpmsException("Unable to delete tester information", "tester info update", e.getMessage() + " while backing up tester info file<br>" + testerInfoFileName);
        }

        File testerInformationBackupFile = new File(testerInfoBackupFileName);

        String query = "DELETE FROM TESTER_INFO " +
                "WHERE INSTALLATION_ID = '" + tpmsConfiguration.getLocalPlant() + "' AND " +
                "TESTER_FAMILY = '" + XmlUtils.getVal(testerData, "TESTER_FAMILY") + "' AND " +
                "TESTER_INFO_SHOW = '" + XmlUtils.getVal(testerData, "TESTER_INFO_SHOW") + "' AND " +
                "TST_INFO_CODE = '" + XmlUtils.getVal(testerData, "TST_INFO") + "'";


        boolean queryResult = executeUpdateQuery(query, sessionId, currentUser, false);


        if (queryResult) {
            try {
                /*************Aggiorno l'xml *****************************/
                delTesterData(testerData, tpmsConfiguration.getLocalPlant());
                testersSave(tpmsConfiguration.getLocalPlant());
            } catch (TpmsException e) {
                String detailMessage = "";
                try {
                    rollbackTesterInfoFile(testerInfoFileName, testerInfoBackupFileName);
                } catch (IOException e2) {
                    errorLog("TesterInfoMgr :: deleteTesterInformation : 0 unable to rollback Tester info file " + e2.getMessage(), e2);
                    detailMessage = "Unable to rollback Tester information file: your configuration and db data are not alligned<br>Backup filename " + testerInfoBackupFileName;
                }
                try {
                    dbWriter.rollback();
                } catch (Exception e1) {
                    errorLog("TesterInfoMgr :: deleteTesterInformation : 0 unable to rollback connection ", e1);
                }
                //rilanciare eccezione con errore nell'aggiornamento dell'xml...
                throw new TpmsException("Tester Information delete action failed to update xml file " + e.getMessage(), "tester info delete", detailMessage);
            }
        } else {
            testerInformationBackupFile.delete();
            throw new TpmsException("Unable to delete Tester information into database", "tester info update", "");
        }
        try {
            dbWriter.commit();
        } catch (Exception e) {
            String detailMessage = "";
            try {
                rollbackTesterInfoFile(testerInfoFileName, testerInfoBackupFileName);
            } catch (IOException e2) {
                errorLog("TesterInfoMgr :: deleteTesterInformation : 1 unable to rollback Tester info file " + e2.getMessage(), e2);
                detailMessage = "Unable to rollback Tester information file: your configuration and db data are not alligned<br>Backup filename " + testerInfoBackupFileName;
            }
            //rilanciare eccezione con errore nell'aggiornamento dell'xml...
            throw new TpmsException("Tester Information delete action failed to commit sql update statement", "tester info delete", detailMessage);
        }
        //rimuovi backup...
        testerInformationBackupFile.delete();
    }


    public synchronized static void delTesterData(Element testerData, String localPlant) {
        //CtrlServlet.getAccntsRoot().removeChild(testerData);
        TesterInfoMgr.getTesterInfosRoot(localPlant).removeChild(testerData);
    }
}
