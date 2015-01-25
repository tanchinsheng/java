package tpms;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 24, 2003
 * Time: 4:46:53 PM
 * To change this template use Options | File Templates.
 */
public class VobManager extends AfsCommonStaticClass {
    static Element vobsRoot = null;

    public static final String T_VOB_TYPE_FLAG_VALUE = "T";
    public static final String R_VOB_TYPE_FLAG_VALUE = "R";
    public static final String D_VOB_TYPE_FLAG_VALUE = "D";

    public static final String VOB_NAME_ATTRIBUTE = "NAME";
    public static final String VOB_DESC_ATTRIBUTE = "DESC";
    public static final String VOB_PLANT_ATTRIBUTE = "PLANT";
    public static final String VOB_TYPE_ATTRIBUTE = "TYPE";
    public static final String VOB_DIVISION_ATTRIBUTE = "DIVISION";
    public static final String VOB_TVOBS_ATTRIBUTE = "TVOB";


    static {
        if (vobsRoot == null) {
            try {
                vobsRoot = XmlUtils.getRoot(CtrlServlet._vobsFileName);
                //vobsRoot = XmlUtils.getRoot("D:\\jakarta-tomcat-3.2.3\\webapps\\tpms51\\cfg\\local_cfg\\vobs.xml");
            } catch (Exception e) {
                errorLog("VobManager :: static block : vobsRoot was null, exception while trying to retrieve root element", e);
            }
        }
    }

    public static void setVobsRoot(Element root) {
        vobsRoot = root;
    }

    /**
     * questo metodo ritorna l'elemento vobName verificando i permessi su VobName --> UnixGroup
     * (usato per la verifica alla login dei permessi sul Default Vob dell'utente)
     * da non controllare in caso di login ADMIN
     *
     * @param vobName
     * @param unixGroup
     */

    public static Element getVobData(String vobName, String unixGroup) {
        Element result = null;
        Element el = null;

        NodeList vobList = vobsRoot.getElementsByTagName("VOB");
        if (vobList != null) {
            el = XmlUtils.findEl(vobList, "NAME", vobName);
        }
        if ((el != null) && (XmlUtils.getVal(el, "DIVISION").equals(unixGroup))) {
            result = el;
        }
        return result;
    }


    public static Element getVobData(String vobName) throws TpmsException {
        Element el;

        el = XmlUtils.findEl(vobsRoot.getElementsByTagName("VOB"), "NAME", vobName);
        if (el != null) {
            return el;
        } else {
            throw new TpmsException("UNKNOWN VOB '" + vobName + "' <br> Please check you default vobs or, in order to proceed, select a valid vob", "ACTION ABORTED");
        }
    }

    public static String getVobDesc(String vobName) throws TpmsException {
        Element vobData = getVobData(vobName);
        return XmlUtils.getVal(vobData, "DESC");
    }


    public static Element getVobsRoot() {
        return vobsRoot;
    }

    public static int getNofVobs() {
        return vobsRoot.getElementsByTagName("VOB").getLength();
    }

    static NodeList getTvobsList(String dvlVobName) throws TpmsException {
        Element dvlVob = getVobData(dvlVobName);
        return dvlVob.getElementsByTagName("TVOB");
    }

    public static String getTvobsListAsString(String dvlVobName) throws TpmsException {
        String lst = "";
        NodeList tvobs = getTvobsList(dvlVobName);
        for (int i = 0; i < tvobs.getLength(); i++) {
            Element tvob = (Element) tvobs.item(i);
            lst = lst + XmlUtils.getVal(tvob) + " ";
        }
        return lst;
    }

    public static Vector getDandTvobsList(String dvlVobName) throws TpmsException {
        Vector lst = new Vector();
        NodeList tvobs = getTvobsList(dvlVobName);
        Element dvob = getVobData(dvlVobName);
        lst.addElement(dvob);
        for (int i = 0; i < tvobs.getLength(); i++) {
            Element tvob = getVobData(XmlUtils.getVal(tvobs.item(i)));
            lst.addElement(tvob);
        }
        return lst;
    }

    public static Vector getTvobPlantsList(String dvlVobName) throws TpmsException {
        Vector plants = new Vector();
        plants.addElement(XmlUtils.getVal(getVobData(dvlVobName), "PLANT"));
        NodeList tvobs = getTvobsList(dvlVobName);
        for (int i = 0; i < tvobs.getLength(); i++) {
            Element tvob = (Element) tvobs.item(i);
            Element vobData = getVobData(XmlUtils.getVal(tvob));
            if (vobData != null) {
                plants.addElement(XmlUtils.getVal(vobData, "PLANT"));
            }
        }
        return plants;
    }

    /**
     * this method return the plant list represented by the T vobs associated with the given D vob (dvlVobName)
     * filtered for the user userLogin.
     *
     * @param dvlVobName
     * @param userLogin
     * @return plant list
     * @throws TpmsException
     */
    public static Vector getTvobPlantsListFiltered(String dvlVobName, String userLogin) throws TpmsException {
        Vector plants = new Vector();
        if (!GeneralStringUtils.isEmptyString(dvlVobName) && ! GeneralStringUtils.isEmptyString(userLogin)) {
            String userDivision = UserUtils.getUserUnixGroup(userLogin);
            String dVobPlant = XmlUtils.getVal(getVobData(dvlVobName), "PLANT");
            plants.addElement(dVobPlant);
            NodeList tVobs = getTvobsList(dvlVobName);
            Element tVob;
            Element vobData;
            NodeList currentVobDivisions;
            String remotePlant;
            for (int i = 0; i < tVobs.getLength(); i++) {
                tVob = (Element) tVobs.item(i);
                vobData = getVobData(XmlUtils.getVal(tVob));
                if (vobData != null) {
                    currentVobDivisions = vobData.getElementsByTagName("DIVISION");
                    if (currentVobDivisions != null) {
                        int divisionsCount = currentVobDivisions.getLength();
                        for (int z = 0; z < divisionsCount; z++) {
                            if (userDivision.equalsIgnoreCase(XmlUtils.getVal(currentVobDivisions.item(z)))) {
                                remotePlant = XmlUtils.getVal(vobData, "PLANT");
                                plants.addElement(remotePlant);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return plants;
    }


    public static Vector getDivisionList() {
        Vector divisions = new Vector();
        NodeList vobs = vobsRoot.getElementsByTagName("VOB");
        String currentDivision;
        for (int i = 0; i < getNofVobs(); i++) {
            Element vob = (Element) vobs.item(i);
            if (vob != null) {
                NodeList divis = vob.getElementsByTagName("DIVISION");
                int divisNumber = divis.getLength();
                for (int z = 0; z < divisNumber; z++) {
                    Element div = (Element) divis.item(z);
                    currentDivision = XmlUtils.getVal(div);
                    if (VectorUtils.findInVectorOfString(divisions, currentDivision) == -1) {
                        divisions.addElement(XmlUtils.getVal(div));
                    }
                }
            }
        }
        return divisions;
    }

    public static Vector getDvobList() {
        Vector v = new Vector();
        NodeList vobs = vobsRoot.getElementsByTagName("VOB");
        for (int i = 0; i < vobs.getLength(); i++) {
            Element vob = (Element) vobs.item(i);
            if (XmlUtils.getVal(vob, "TYPE").equals("D")) {
                v.addElement(vob);
            }
        }
        return v;
    }


    /**
     * This method build a vector of hashmaps which contains T_VOB_TYPE_FLAG_VALUE vobs informations
     *
     * @return a vector of hashmaps which contains T_VOB_TYPE_FLAG_VALUE vobs informations
     */
    public static Vector getTVobsInfo() {
        return getVobsInfo(T_VOB_TYPE_FLAG_VALUE);
    }

    /**
     * This method build a vecotor of hashmaps which contains R_VOB_TYPE_FLAG_VALUE vobs informations
     *
     * @return a vector of hashmaps which contains R_VOB_TYPE_FLAG_VALUE vobs informations
     */
    public static Vector getRVobsInfo() {
        return getVobsInfo(R_VOB_TYPE_FLAG_VALUE);
    }


    /**
     * search for a t vob data for the given plant
     *
     * @param plant
     * @return t vob data for the given plant
     */

    public static Vob searchTVobByDestinationPlant(String plant) {
        return searchVobByPlantAttribute(T_VOB_TYPE_FLAG_VALUE, plant);
    }

    /**
     * @param plant
     * @return the R vob where the given plant is equal to the content of VOB_PLANT_ATTRIBUTE
     */
    public static Vob searchRVobByDestinationPlant(String plant) {
        return searchVobByPlantAttribute(R_VOB_TYPE_FLAG_VALUE, plant);
    }

    private static Vob searchVobByPlantAttribute(String vobType, String plant) {
        Vob result = null;
        NodeList vobs = vobsRoot.getElementsByTagName("VOB");
        String currentVobType;
        String currentDestinationPlant;
        for (int i = 0; i < vobs.getLength(); i++) {
            //for all configured vob....
            Element vob = (Element) vobs.item(i);
            currentDestinationPlant = XmlUtils.getVal(vob, VOB_PLANT_ATTRIBUTE);
            currentVobType = XmlUtils.getVal(vob, VOB_TYPE_ATTRIBUTE);

            if (!GeneralStringUtils.isEmptyString(currentVobType) && !GeneralStringUtils.isEmptyString(currentDestinationPlant) &&
                    currentVobType.equals(vobType) && currentDestinationPlant.equals(plant)) {
                //if the current vob has have thegiven type and the given plant...
                //retrve the associated divisions list.
                Vector divisionList = new Vector();
                NodeList divis = vob.getElementsByTagName(VOB_DIVISION_ATTRIBUTE);
                int divisionCount = divis.getLength();
                Element div;
                for (int z = 0; z < divisionCount; z++) {
                    div = (Element) divis.item(z);
                    divisionList.add(XmlUtils.getVal(div));
                }
                if (vobType.equals(T_VOB_TYPE_FLAG_VALUE) || vobType.equals(R_VOB_TYPE_FLAG_VALUE)) {
                    //if the vob is T or R do not set the assscociated r vobs list
                    result = new Vob(XmlUtils.getVal(vob, VOB_NAME_ATTRIBUTE), XmlUtils.getVal(vob, VOB_DESC_ATTRIBUTE), XmlUtils.getVal(vob, VOB_PLANT_ATTRIBUTE), vobType, divisionList);
                } else {
                    //if the vob is D retrieve the the assscociated r vobs list and set this list too.
                    Vector associatedTVobsVector = new Vector();
                    NodeList associatedTVobs = vob.getElementsByTagName(VOB_TVOBS_ATTRIBUTE);
                    int associatedTVobsCount = associatedTVobs.getLength();
                    Element tVob;
                    for (int z = 0; z < associatedTVobsCount; z++) {
                        tVob = (Element) associatedTVobs.item(z);
                        associatedTVobsVector.add(XmlUtils.getVal(tVob));
                    }
                    //setta divisioni + tvobname
                    result = new Vob(XmlUtils.getVal(vob, VOB_NAME_ATTRIBUTE), XmlUtils.getVal(vob, VOB_DESC_ATTRIBUTE), XmlUtils.getVal(vob, VOB_PLANT_ATTRIBUTE), vobType, divisionList, associatedTVobsVector);
                }
            }
        }
        return result;
    }

    /**
     * Give an afs vob name return an Vob object populated with that vob informations. if the vob is not preset return null;
     *
     * @param vobName the name of the vob that shoul be searched.
     * @return Vob object
     *         todo: attenzione il vob che viene ritornato sicuramente NON ha associate informazioni didivisione e TVob
     */

    public static Vob getVobDataByVobName(String vobName) {
        Vob result = null;
        if (!GeneralStringUtils.isEmptyString(vobName)) {
            NodeList vobs = vobsRoot.getElementsByTagName("VOB");
            String currentValue;
            for (int i = 0; i < vobs.getLength(); i++) {
                Element vob = (Element) vobs.item(i);
                currentValue = XmlUtils.getVal(vob, VOB_NAME_ATTRIBUTE);
                if (!GeneralStringUtils.isEmptyString(currentValue) && currentValue.equals(vobName)) {
                    //String name, String description, String plant, String type
                    result = new Vob(XmlUtils.getVal(vob, VOB_NAME_ATTRIBUTE), XmlUtils.getVal(vob, VOB_DESC_ATTRIBUTE),
                            XmlUtils.getVal(vob, VOB_PLANT_ATTRIBUTE), XmlUtils.getVal(vob, VOB_TYPE_ATTRIBUTE));
                    return result;
                }
            }
        }
        return result;
    }


    /**
     * this is a general methods used to gather vob infos: return the list of vobs in a vector of hashtable
     * where each hashtablel represet one vob. At the moment this methos return the information for aided ftp service
     *
     * @param afsVobType the afs vob type to seach
     * @return a vector of hashmaps which contains afsVobType vobs type informations
     */
    private static Vector getVobsInfo(String afsVobType) {
        HashSet result = new HashSet();
        //Element vobsRoot = XmlUtils.getRoot("C:\\Documents and Settings\\furgiuele\\Desktop\\buttami\\vobs_TPY.xml");//vobsRoot.getElementsByTagName("VOB");
        NodeList vobs = vobsRoot.getElementsByTagName("VOB");
        String currentVobType;
        Hashtable oneVobInfo;
        for (int i = 0; i < vobs.getLength(); i++) {
            Element vob = (Element) vobs.item(i);
            currentVobType = XmlUtils.getVal(vob, VobManager.VOB_TYPE_ATTRIBUTE);
            if (!GeneralStringUtils.isEmptyString(currentVobType) && currentVobType.equals(afsVobType)) {
                oneVobInfo = new Hashtable();
                oneVobInfo.put(VobManager.VOB_NAME_ATTRIBUTE, XmlUtils.getVal(vob, VobManager.VOB_NAME_ATTRIBUTE));
                oneVobInfo.put(VobManager.VOB_DESC_ATTRIBUTE, XmlUtils.getVal(vob, VobManager.VOB_DESC_ATTRIBUTE));
                oneVobInfo.put(VobManager.VOB_PLANT_ATTRIBUTE, XmlUtils.getVal(vob, VobManager.VOB_PLANT_ATTRIBUTE));
                oneVobInfo.put(VobManager.VOB_TYPE_ATTRIBUTE, XmlUtils.getVal(vob, VobManager.VOB_TYPE_ATTRIBUTE));
                result.add(oneVobInfo);
            }
        }
        return new Vector(result);
    }


    public static Vector getRvobList() {
        Vector v = new Vector();
        NodeList vobs = vobsRoot.getElementsByTagName("VOB");
        String currentValue;
        for (int i = 0; i < vobs.getLength(); i++) {
            Element vob = (Element) vobs.item(i);
            currentValue = XmlUtils.getVal(vob, "TYPE");
            if (!GeneralStringUtils.isEmptyString(currentValue) && currentValue.equals("R")) {
                v.addElement(vob);
            }
        }
        return v;
    }

    public static Vector getTvobList() {
        Vector v = new Vector();
        NodeList vobs = vobsRoot.getElementsByTagName("VOB");
        for (int i = 0; i < vobs.getLength(); i++) {
            Element vob = (Element) vobs.item(i);
            if (XmlUtils.getVal(vob, "TYPE").equals("T")) {
                v.addElement(vob);
            }
        }
        return v;
    }

    public static Vector getTvobNameList() throws Exception {
        Vector v = new Vector();
        NodeList vobs = vobsRoot.getElementsByTagName("VOB");
        for (int i = 0; i < vobs.getLength(); i++) {
            Element vob = (Element) vobs.item(i);
            if (XmlUtils.getVal(vob, "TYPE").equals("T")) {
                String vobName = XmlUtils.getVal(vob, "NAME");
                v.addElement(vobName);
            }
        }
        return v;
    }

    public static String getTvobFromPlant(String vobD, String plantName) throws TpmsException {
        Vector listDandTvobs = VobManager.getDandTvobsList(vobD);
        String toVobName = "";
        String currentPlant;
        for (int i = 0; i < listDandTvobs.size(); i++) {
            Element vobElem = (Element) listDandTvobs.elementAt(i);
            currentPlant = XmlUtils.getVal(vobElem, "PLANT");
            if (!GeneralStringUtils.isEmptyString(currentPlant) && currentPlant.equals(plantName)) {
                toVobName = XmlUtils.getVal(vobElem, "NAME");
            }
        }
        return toVobName;
    }

    public synchronized static void vobsSave() throws TpmsException {
        try {
            FileWriter fout = new FileWriter(CtrlServlet._vobsFileName);

            XmlUtils.print(vobsRoot.getOwnerDocument(), fout);
            fout.flush();
            fout.close();
        }
        catch (Exception e) {
            throw new TpmsException("ACCOUNTS XML FILE SAVE FAILURE", "", e);
        }
    }

    public static Element getNewVob(String type) throws TpmsException {

        if (vobsRoot == null) {
            throw new TpmsException("ROOT UNKNOWN", "ACTION FAILED");
        }
        Element vob = XmlUtils.addEl(vobsRoot, "VOB");

        XmlUtils.addEl(vob, "NAME");
        XmlUtils.addEl(vob, "DESC");
        if (type.equals("D")) {
            XmlUtils.addEl(vob, "DIVISION");
        }
        XmlUtils.addEl(vob, "PLANT");
        XmlUtils.addEl(vob, "TYPE");

        return vob;
    }


}
