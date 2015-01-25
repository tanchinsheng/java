package tpms;

/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 18, 2003
 * Time: 3:28:12 PM
 * To change this template use Options | File Templates.
 */

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.util.Vector;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

import it.txt.general.utils.XmlUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.afs.AfsCommonStaticClass;

import javax.xml.parsers.ParserConfigurationException;

public class FacilityMgr {
    static Element facilityRoot;


    public static boolean setFacilityRoot(String facilityFilePath) {
        if (!GeneralStringUtils.isEmptyString(facilityFilePath)) {
            File facilityFile = new File(facilityFilePath);
            if (facilityFile.exists()) {
                try {
                    facilityRoot = XmlUtils.getRoot(facilityFile);
                    return true;
                } catch (ParserConfigurationException e) {
                    AfsCommonStaticClass.errorLog("FacilityMgr :: setFacilityRoot (" + facilityFilePath + ") : ParserConfigurationException  " + e.getMessage(), e);
                } catch (SAXException e) {
                    AfsCommonStaticClass.errorLog("FacilityMgr :: setFacilityRoot (" + facilityFilePath + ") : SAXException " + e.getMessage(), e);
                } catch (IOException e) {
                    AfsCommonStaticClass.errorLog("FacilityMgr :: setFacilityRoot (" + facilityFilePath + ") : IOException : " + e.getMessage(), e);
                }
            }
        }
        return false;
    }

    public static void setFacilityRoot(Element root) {
        facilityRoot = root;
    }

    static Element getFacilityRoot(String plant) {
        return (plant != null ? XmlUtils.findElwithAttr(facilityRoot.getElementsByTagName("PLANT"), "name", plant) : facilityRoot);
    }

    public static Vector getFacilityList(String plant) throws Exception {
        Element root = getFacilityRoot(plant);
        if (root == null) {
            return new Vector();
        }

        return getUniqueVals(root.getElementsByTagName("FACILITY"));
    }

    private static Vector getUniqueVals(NodeList nl) {
        Vector v = new Vector();
        String val;
        boolean found;
        for (int i = 0; (nl != null && i < nl.getLength()); i++) {
            val = XmlUtils.getVal((Element) nl.item(i));
            found = false;
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


    public static Vector getFacilityListFast(String plantId) {
        Element root = getFacilityRoot(plantId);
        if (root == null) {
            return new Vector();
        }
        NodeList nl = root.getElementsByTagName("FACILITY");
        HashMap tmpHashMap = new HashMap(nl.getLength());
        String val;
        for (int i = 0; i < nl.getLength(); i++) {
            val = XmlUtils.getVal((Element) nl.item(i));
            tmpHashMap.put(val, val);
        }
        return new Vector(tmpHashMap.values());
    }

}

