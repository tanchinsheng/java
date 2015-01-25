package tpms.utils;

import it.txt.afs.AfsCommonStaticClass;

import it.txt.general.utils.FileUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.general.utils.AlphabeticalSort;
import tpms.TpmsException;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * User: furgiuele
 * Date: 23-giu-2006
 * Time: 16.23.19
 * This class contains methods that permits the dump and parsing of remote plants data
 */
public class RemotePlantsDataManager extends AfsCommonStaticClass {
    public static final String PATH_TO_DUMP_FILES = tpmsConfiguration.getWebAppDir() + File.separator + "cfg" + File.separator + "local_cfg" + File.separator;
    public static final String ACCOUNTS_DUMP_FILE_NAME = "accounts_per_plant_dump.xml";

    public static final String INSTALLATIONS_TAG_NAME = "INSTALLATIONS";
    public static final String INSTALLATION_ID_TAG_NAME = "INSTALLATION_ID";
    public static final String INSTALLATION_ID_PLANT_ID_ATTRIBUTE = "plant_id";

    private static HashMap emailsPerPlant = new HashMap();
    private static HashMap loginPerPlant = new HashMap();


    /**
     * this method dumps the user data to an xml file: return true if this operation succedes false otherwise
     *
     * @return return true if this operation succedes false otherwise
     */
    public static boolean dumpDbUserData() {
        boolean result = true;
        String query = "select INSTALLATION_ID, FIRST_NAME, SURNAME, TPMS_ROLE, TPMS_LOGIN, EMAIL, UNIX_LOGIN, UNIX_HOME, WORK_DIR, DIVISION " +
                "from users " +
                "order by INSTALLATION_ID, EMAIL, TPMS_LOGIN, TPMS_ROLE";
        try {
            ResultSet plantsUsersData = executeSelectQuery(query);

            if (plantsUsersData != null) {
                StringBuffer xmlAccountData = new StringBuffer();
                String currentPlantName = "";
                String previousPlantName;
                int columnsCount;
                while (plantsUsersData.next()) {
                    previousPlantName = currentPlantName;
                    currentPlantName = plantsUsersData.getString(INSTALLATION_ID_TAG_NAME);
                    if (!currentPlantName.equals(previousPlantName)) {
                        //data related to new plant should be treated
                        if (plantsUsersData.isFirst()) {
                            //means that we are managing the fist plant.
                            xmlAccountData.append("<");
                            xmlAccountData.append(INSTALLATIONS_TAG_NAME);
                            xmlAccountData.append(">\n\t<").append(INSTALLATION_ID_TAG_NAME).append(" ").append(INSTALLATION_ID_PLANT_ID_ATTRIBUTE).append("=\"");
                            xmlAccountData.append(currentPlantName);
                            xmlAccountData.append("\">\n");
                        } else {
                            //means that this is not the first plant (i.e. close the previous one)
                            xmlAccountData.append("</").append(INSTALLATION_ID_TAG_NAME).append(">\t\n<").append(INSTALLATION_ID_TAG_NAME).append(" ").append(INSTALLATION_ID_PLANT_ID_ATTRIBUTE).append("=\"").append(currentPlantName).append("\">\n");
                        }
                    }
                    columnsCount = plantsUsersData.getMetaData().getColumnCount();
                    String currentColumnName;
                    String currentColumnValue;
                    xmlAccountData.append("\t\t<USER>\n");
                    for (int z = 1; z <= columnsCount; z++) {
                        //add to string buffer the current user data..
                        currentColumnName = plantsUsersData.getMetaData().getColumnName(z);
                        if (currentColumnName.equals(INSTALLATION_ID_TAG_NAME)) {
                            continue;
                        }
                        currentColumnValue = GeneralStringUtils.isEmptyString(plantsUsersData.getString(currentColumnName)) ? "" : plantsUsersData.getString(currentColumnName);
                        xmlAccountData.append("\t\t\t<").append(currentColumnName).append(">").append(currentColumnValue).append("</").append(currentColumnName).append(">\n");
                    }
                    xmlAccountData.append("\t\t</USER>\n");
                }

                try { //add 17th Dec 2007 to close statement
                    if (plantsUsersData != null)
                    	plantsUsersData.close();
                } catch (SQLException e) {
                    errorLog("RemotePlantsDataManager :: dumpDbUserData : error while closing resultset! ", e);
                }

                if (!GeneralStringUtils.isEmptyString(xmlAccountData.toString())) {
                    //if there were founded data....
                    xmlAccountData.append("\t</").append(INSTALLATION_ID_TAG_NAME).append(">\n</").append(INSTALLATIONS_TAG_NAME).append(">");
                    String tmpAccountsFileName = System.currentTimeMillis() + ".xml";

                    try {
                        //dump data to a temporary file.
                        FileUtils.writeToFile(PATH_TO_DUMP_FILES + tmpAccountsFileName, xmlAccountData.toString());
                        //move temporary file to the default one.
                        FileUtils.copy(PATH_TO_DUMP_FILES + tmpAccountsFileName, PATH_TO_DUMP_FILES + ACCOUNTS_DUMP_FILE_NAME);
                        //invalidate cached lists
                        emailsPerPlant = new HashMap();
                        loginPerPlant = new HashMap();
                        //remove the temporary created file.
                        File tmpAccountFile = new File(PATH_TO_DUMP_FILES + tmpAccountsFileName);
                        tmpAccountFile.delete();
                    } catch (IOException e) {
                        errorLog("UserUtils :: dumpDbUserData : Unable to write temporary accounts file or to copy it to the default one " + e.getMessage(), e);
                        result = false;
                    }
                }
            }
        } catch (TpmsException e) {
            errorLog("UserUtils :: dumpDbUserData : TpmsException : Unable to retrieve account data from database " + e.getMessage(), e);
            result = false;
        } catch (SQLException e1) {
            errorLog("UserUtils :: dumpDbUserData : SQLException : Unable to retrieve account data from database " + e1.getMessage(), e1);
            result = false;
        }

        return result;

    }

    /**
     * starting from a plant name looks in dumped data for the users logins of the given plant
     *
     * @param plantId to filter users accounts.
     * @return a vector containing the list of the users that are present in the given plant from the dumped data
     */
    public static Vector getPlantUsers(String plantId) {
        Vector result = (Vector) loginPerPlant.get(plantId);
        if (result == null || result.size() <= 0){
            result = retrieveRemotePlantSingleAttributeValueList(plantId, "TPMS_LOGIN");
            loginPerPlant.put(plantId, result);
        }
        return result;
    }

    /**
     * starting from a plant name looks in dumped data for the users email addresses of the given plant
     *
     * @param plantId to filter users accounts.
     * @return a vector containing the list of the email addresses that are present in the given plant from the dumped data
     */
    public static Vector getPlantEmails(String plantId) {
        Vector result = (Vector) emailsPerPlant.get(plantId);
        if (result == null || result.size() <= 0){
            result = retrieveRemotePlantSingleAttributeValueList(plantId, "EMAIL");
            emailsPerPlant.put(plantId, result);
        }
        return result;
    }


    private static Vector retrieveRemotePlantList() {
        try {
            Element xmlAccountsPerPlantRoot = XmlUtils.getRoot(PATH_TO_DUMP_FILES + ACCOUNTS_DUMP_FILE_NAME);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }


    /**
     * This method pharses the remote plants xml file and return a vector containing the list of values contained in
     * attributeName
     * @param plantId
     * @param attributeName
     * @return a vector containing all the values of attribute named attributeName
     */
    private static Vector retrieveRemotePlantSingleAttributeValueList(String plantId, String attributeName) {
        //Vector result = new Vector();
        ArrayList result = new ArrayList();


        if (!GeneralStringUtils.isEmptyString(plantId) && !GeneralStringUtils.isEmptyString(attributeName)) {
            try {
                Element xmlAccountsPerPlantRoot = XmlUtils.getRoot(PATH_TO_DUMP_FILES + ACCOUNTS_DUMP_FILE_NAME);
                NodeList xmlAccountsPerPlantNodes = xmlAccountsPerPlantRoot.getElementsByTagName(INSTALLATION_ID_TAG_NAME);
                int nodesCount = xmlAccountsPerPlantNodes.getLength();
                Element currentPlantData;

                for ( int i = 0; i < nodesCount; i++ ) {
                    currentPlantData = (Element) xmlAccountsPerPlantNodes.item(i);
                    if (plantId.equals(currentPlantData.getAttribute(INSTALLATION_ID_PLANT_ID_ATTRIBUTE))) {
                        //plants founded
                        NodeList plantData = currentPlantData.getElementsByTagName("USER");
                        int plantNodesCount = plantData.getLength();
                        for (int z = 0; z < plantNodesCount; z++){
                            result.add(XmlUtils.getTextValue((Element) plantData.item(z), attributeName));
                        }
                        break;
                    }
                }
                Collections.sort(result, new AlphabeticalSort());
            } catch (ParserConfigurationException e) {
                errorLog("UserUtils :: dumpDbUserData : ParserConfigurationException : Unable to parse remote account data xml file (" + PATH_TO_DUMP_FILES + ACCOUNTS_DUMP_FILE_NAME + "): " + e.getMessage(), e);
            } catch (SAXException e) {
                errorLog("UserUtils :: dumpDbUserData : SAXException : Unable to parse remote account data xml file (" + PATH_TO_DUMP_FILES + ACCOUNTS_DUMP_FILE_NAME + "): " + e.getMessage(), e);
            } catch (IOException e) {
                errorLog("UserUtils :: dumpDbUserData : IOException : Unable to parse remote account data xml file (" + PATH_TO_DUMP_FILES + ACCOUNTS_DUMP_FILE_NAME + "): " + e.getMessage(), e);
            }
        }

        return new Vector(result);
    }
}
