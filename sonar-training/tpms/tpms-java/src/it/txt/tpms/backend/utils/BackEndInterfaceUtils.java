package it.txt.tpms.backend.utils;

import it.txt.afs.clearcase.utils.ClearcaseInterfaceUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.IOException;

import tpms.TpmsException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 5-lug-2006
 * Time: 10.39.10
 * To change this template use File | Settings | File Templates.
 */
public class BackEndInterfaceUtils extends ClearcaseInterfaceUtils {


    /**
     * todo commenti
     * @param xmlFileName
     * @param owner
     * @param applyOwnershipFilter
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TpmsException
     */
   public static String cleanAndFilterTpData(String xmlFileName, String owner, boolean applyOwnershipFilter) throws ParserConfigurationException, SAXException, IOException, TpmsException {
        StringBuffer filteredTPs = new StringBuffer();
        if (applyOwnershipFilter && GeneralStringUtils.isEmptyString(owner)) {
            throw new TpmsException("BackEndInterfaceUtils :: cleanAndFilterTpData : unable to apply ownership filters as requested, the input owner login is null or empty");
        }
        Element resultRoot = XmlUtils.getRoot(xmlFileName);
            if (resultRoot != null) {
                NodeList tpList = resultRoot.getElementsByTagName("TP");
                int tpCount = tpList.getLength();
                Node oneTp;
                String currentTpOwner;
                for (int i = 0; i < tpCount; i++) {
                    oneTp = tpList.item(i);
                    currentTpOwner = XmlUtils.getTextValue((Element) oneTp, "OWNER_LOGIN");
                    if (applyOwnershipFilter) {
                        if (!GeneralStringUtils.isEmptyString(currentTpOwner) && currentTpOwner.equals(owner)) {
                            filteredTPs.append(XmlUtils.getNodeStructureAsString(oneTp));
                        }
                    } else {
                        filteredTPs.append(XmlUtils.getNodeStructureAsString(oneTp));
                    }
                }
            }
        return filteredTPs.toString();
    }
}
