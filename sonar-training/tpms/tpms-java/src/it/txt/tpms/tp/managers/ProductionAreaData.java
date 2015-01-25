package it.txt.tpms.tp.managers;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.ProductionArea;
import it.txt.tpms.tp.list.ProductionAreasList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-gen-2007
 * Time: 10.25.58
 */
public class ProductionAreaData {
    public static final String INSTALLATION = "INSTALLATION";
    public static final String INSTALLATION_ID = "INSTALLATION_ID";
    public static final String PLANT_PRODUCTIONS_AREA = "PLANT_PRODUCTIONS_AREA";
    public static final String PRODUCTION_AREA = "PRODUCTION_AREA";
    public static final String ID = "ID";
    public static final String PLANT = "PLANT";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String VAR_NAME = "VAR_NAME";

    private static ProductionAreaData singlethon = null;

    private static final String productionAreasFilePath = AfsCommonStaticClass.getTpmsConfiguration().getCommonConfigurationFilesAbsolutePath() + File.separator + "production_areas.xml";
    //private static final String productionAreasFilePath = "D:\\jakarta-tomcat-3.2.3\\webapps\\tpms51.COOPERATIVE_DEVELOPMENT\\cfg\\local_cfg\\production_areas.xml";
    private Hashtable productionAreasData = new Hashtable();

    private ProductionAreaData() {
        Element productionAreasRoot = null;
        String commonErrorMessage = "ProductionAreaData :: constructor";
        
        AfsCommonStaticClass.debugLog(commonErrorMessage + " will try to load " + productionAreasFilePath);
        try {
            productionAreasRoot = XmlUtils.getRoot(productionAreasFilePath);
        } catch (ParserConfigurationException e) {
            AfsCommonStaticClass.errorLog( commonErrorMessage + " : (ParserConfigurationException) Unable to Parse lineset accessibility configuration file (" + productionAreasFilePath + ")" + e.getMessage(), e );
        } catch (SAXException e) {
            AfsCommonStaticClass.errorLog( commonErrorMessage + " : (SAXException) Unable to Parse lineset accessibility configuration file (" + productionAreasFilePath + ")" + e.getMessage(), e );
        } catch (IOException e) {
            AfsCommonStaticClass.errorLog( commonErrorMessage + " : (IOException) Unable to access lineset accessibility configuration file (" + productionAreasFilePath + ")" + e.getMessage(), e );
        }

        if (productionAreasRoot != null) {
            NodeList nlProductionAreasList = productionAreasRoot.getElementsByTagName(INSTALLATION);
            int productionAreasCount = nlProductionAreasList.getLength();
            Element oneInstallationProductionAreas;
            String currentInstallationId;
            for (int i = 0; i < productionAreasCount; i++) {
                oneInstallationProductionAreas = (Element) nlProductionAreasList.item(i);
                currentInstallationId = XmlUtils.getTextValue(oneInstallationProductionAreas, INSTALLATION_ID);
                productionAreasData.put(currentInstallationId, getProductionAreasFromElement(oneInstallationProductionAreas));
            }
        }
        AfsCommonStaticClass.debugLog(commonErrorMessage + " loading terminated " + productionAreasFilePath);
    }

    
    /**
     *
     * @param installationProductionAreas the element that will be converted into a ProductionAreasList
     * @return ProductionAreasList representing data contained in the given xml element
     */
    private static ProductionAreasList getProductionAreasFromElement(Element installationProductionAreas) {
        ProductionAreasList productionAreas = new ProductionAreasList();
        String installationId = XmlUtils.getTextValue(installationProductionAreas, INSTALLATION_ID);
        String varName = XmlUtils.getTextValue(installationProductionAreas, VAR_NAME);
        NodeList nlProductionArea = installationProductionAreas.getElementsByTagName(PRODUCTION_AREA);
        if (nlProductionArea != null) {
            int productionAreasCount = nlProductionArea.getLength();
            Element oneProductionAreaElement;
            String id;
            String plant;
            String description;
            for (int i = 0; i < productionAreasCount; i++) {
                oneProductionAreaElement = (Element) nlProductionArea.item(i);
                if (oneProductionAreaElement != null) {
                    id = XmlUtils.getTextValue(oneProductionAreaElement, ID);
                    plant = XmlUtils.getTextValue(oneProductionAreaElement, PLANT);
                    description = XmlUtils.getTextValue(oneProductionAreaElement, DESCRIPTION);
                    //String id, String installationId, String plant, String description, String varName
                    productionAreas.addElement(new ProductionArea( id, installationId, plant, description, varName));
                }
            }
        }
        return productionAreas;
    }


    public static ProductionAreaData getInstance() {
        if (singlethon == null) {
            singlethon = new ProductionAreaData();
        }
        return singlethon;
    }

    
    /**
     * Force the lineset accessibility data to be reloaded:
     * use with care this action is really time consuming!!!
     */
    public static void reloadProductionAreasData() {
        singlethon = null;
        singlethon = new ProductionAreaData();
    }

    
    /**
     * return the ProductionAreasList related to the given installation id
     * @param installationId used to retrive production areas
     * @return the ProductionAreasList related to the given installation id
     */
    public ProductionAreasList getPlantInstallationProductionArea(String installationId) {
        if (singlethon == null) {
            singlethon = new ProductionAreaData();
        }
        ProductionAreasList productionAreas;
        if (!GeneralStringUtils.isEmptyString(installationId)) {
            productionAreas = (ProductionAreasList) productionAreasData.get(installationId);
        } else {
            productionAreas = new ProductionAreasList();
        }
        return productionAreas;
    }

    
    /**
     * @return a ProductionAreasList containing all the configured installation data
     */
    public ProductionAreasList getAllProductionsAreas() {
        ProductionAreasList result = new ProductionAreasList();
        
        if (singlethon == null) {
            singlethon = new ProductionAreaData();
        }
        Enumeration plantsIds = productionAreasData.keys();
        String currentPlantId;
        while (plantsIds.hasMoreElements()) {
            currentPlantId = (String) plantsIds.nextElement();
            ProductionAreasList pas = getPlantInstallationProductionArea(currentPlantId);
            result.addElements(pas);
        }
        return result;
    }

    
    public ProductionArea getProductionArea(String installationId, String prodAreaId) {
        ProductionArea prodArea = null;

        if (!GeneralStringUtils.isEmptyString(installationId) &&  !GeneralStringUtils.isEmptyString(prodAreaId)) {
            if (singlethon == null) {
                singlethon = new ProductionAreaData();
            }
            ProductionAreasList prodAreasList = getPlantInstallationProductionArea (installationId);
            if (prodAreasList != null && prodAreasList.size() > 0) {
                prodArea = prodAreasList.findProductAreaById(prodAreaId);
            }
        }
        return prodArea;
    }
}