package it.txt.integration.managers;


import it.txt.general.utils.GeneralStringUtils;
import it.txt.integration.utils.ResultDataFormatter;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import tpms.TpmsException;
import tpms.VobManager;
import tpms.utils.MailUtils;
import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 29-giu-2006
 * Time: 11.32.05
 * This class contains usefull methods to gther the plant informations, all the public methods MUST NOT RAISE
 * exception but in case of errors return a KO result
 */
public class PlantInformationsManager extends QueryUtils {

    /**
     * This method retrieves the list of tpms logins and emails for the given plant.
     *
     * @param plantId
     * @param sb      A StringBuffer containing an xml structure which represents the list of tpms logins associated with theri emails
     *                in the given plant
     * @return true if data was succesfully retrieved, false if some warning (i.e. no data found, unable to cole resultset ...)
     * @throws TpmsException
     */
    private static boolean getPlantUserData(String plantId, StringBuffer sb) throws TpmsException {
        boolean result = false;
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append("<accounts>");

        String query = "select TPMS_LOGIN, EMAIL " +
                "from users " +
                "where INSTALLATION_ID = " + getStringValueForQuery(plantId) + " " +
                "order by TPMS_LOGIN, EMAIL";

//        ResultSet plantsUsersData = executeSelectQuery(query);
        SQLInterface iface = new SQLInterface();
        CachedRowSet plantsUsersData = iface.execQuery(query);
        try {
            if (plantsUsersData != null) {
                String tmpLogin;
                String tmpEmailAddress;
                while (plantsUsersData.next()) {
                    tmpLogin = GeneralStringUtils.isEmptyString(plantsUsersData.getString("TPMS_LOGIN")) ? "" : plantsUsersData.getString("TPMS_LOGIN");
                    tmpEmailAddress = GeneralStringUtils.isEmptyString(plantsUsersData.getString("EMAIL")) ? "" : plantsUsersData.getString("EMAIL");
                    sb.append("<account><login>").append(tmpLogin).append("</login><email>").append(tmpEmailAddress).append("</email></account>");
                    result = true;
                }
            }
        } catch (SQLException e) {
            throw new TpmsException("PlantInformationsManager :: getPlantUserData : error while scanning query sb", "", e);
        } finally {
            if (plantsUsersData != null) {
                try {
                    plantsUsersData.close();
                } catch (SQLException e) {
                    errorLog("PlantInformationsManager :: getPlantUserData : error closing ResultSet", e);
                    result = false;
                }
            }
        }

        sb.append("</accounts>");
        return result;

    }

    /**
     * This method retrieve the list of tester models configured for a specific plant
     *
     * @param sb      StringBuffer containing tester info related to the given plant in xml format.
     * @param plantId
     * @return true if data was succesfully retrieved, false if some warning (i.e. no data found, unable to cole resultset ...)
     */
    private static boolean getPlantTesterInfo(String plantId, StringBuffer sb) throws TpmsException{
        boolean result = false;
        if (sb == null) {
            sb = new StringBuffer();
        }

        sb.append("<tester_data>");
        String query = "select tester_info_show " +
                       "from tester_info " +
                       "where installation_id = " + getStringValueForQuery(plantId) + " " +
                       "order by tester_info_show";

//        ResultSet plantTesterInformation = executeSelectQuery(query);
        SQLInterface iface = new SQLInterface();
        CachedRowSet plantTesterInformation = iface.execQuery(query);

        try {
            if (plantTesterInformation != null) {
                String testerModel;
                while (plantTesterInformation.next()) {
                    testerModel = GeneralStringUtils.isEmptyString(plantTesterInformation.getString("tester_info_show")) ? "" : plantTesterInformation.getString("tester_info_show");

                    sb.append("<testermodel>").append(testerModel).append("</testermodel>");
                    result = true;
                }
            }
        } catch (SQLException e) {
            throw new TpmsException("PlantInformationsManager :: getPlantTesterInfo : error while scanning query sb", "", e);
        } finally {
            if (plantTesterInformation != null) {
                try {
                    plantTesterInformation.close();
                } catch (SQLException e) {
                    errorLog("PlantInformationsManager :: getPlantTesterInfo : error closing ResultSet", e);
                    result = false;
                }
            }
        }
        sb.append("</tester_data>");
        return result;
    }

    /**
     * This method add tthe ginven sb the tag with plant name attribute to the given string buffer,
     * if the StringBUffer is null istantiates it
     *
     * @param plantId
     * @param sb
     */
    private static void addPlantOpenTag(String plantId, StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append("<plant name=\"").append(plantId).append("\">");
    }

    /**
     * this method adds a closing plant tag to the ginven string buffer, if the string buffer is null istantiates it.
     * @param sb
     */
    private static void addPlantClosingTag(StringBuffer sb) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append("</plant>");
    }


    /**
     * retrieve the whole data for the plant identified by plantId
     *
     * @param plantId
     * @return a StringBuffer representing an xml hat contains the whole data for the plant identified by plantId
     */
    public static StringBuffer getPlantInformations(String plantId) {
        //todo okkio è implementato con due punti di vista diversi.... se gesera xml di errore allora costruisce output con metodi di ResultDataFormatter altrimenti se lo costruisce da solo
        //todo allineare i comportamenti???
        StringBuffer result = new StringBuffer("");
        ResultDataFormatter.addXmlHeader(result);

        ResultDataFormatter.addActionResultOpenTag(result);

        StringBuffer operationResult = new StringBuffer();
        StringBuffer resultData = new StringBuffer();

        if (GeneralStringUtils.isEmptyString(plantId)) {
            addPlantOpenTag(plantId, resultData);
            addPlantClosingTag(resultData);
            return ResultDataFormatter.generateKoResult("Plant ID in input is null or empty, no operation will be performed", resultData.toString());
        } else {
            //let's check db connection...
            /* 
        	if  (!QueryUtils.checkCtrlServletDBConnection()){
                return ResultDataFormatter.generateKoResult("DB not available");
            }
            */
        	SQLInterface iface = new SQLInterface();
    		if (iface.checkDBConnection() == false ) {
    			MailUtils.sendMailImpl("Database is not available");
    			return ResultDataFormatter.generateKoResult("DB not available");
    		}
	
            boolean dataRetrievingError = false;
            //retrieve plant data....
            StringBuffer plantUsersInformations = new StringBuffer();
            boolean plantsUserDataNoWarning = false;
            try {
                plantsUserDataNoWarning = getPlantUserData(plantId, plantUsersInformations);
            } catch (TpmsException e) {
                dataRetrievingError = true;
                errorLog("PlantInformationsManager :: getPlantInformations : Unable to retrieve the list of emails and logins for " + plantId, e);
            }
            //tester informations
            StringBuffer plantTesterInformations = new StringBuffer();
            boolean testerInfoNoWarning = false;
            try {
                testerInfoNoWarning = getPlantTesterInfo(plantId, plantTesterInformations);
            } catch (TpmsException e) {
                dataRetrievingError = true;
                errorLog("PlantInformationsManager :: getPlantTesterInfo : Unable to retrieve the list of tester information for " + plantId, e);
            }

            if (dataRetrievingError || (!testerInfoNoWarning && !plantsUserDataNoWarning)) {
                addPlantOpenTag(plantId, resultData);
                addPlantClosingTag(resultData);
                return ResultDataFormatter.generateKoResult("General error while fetching user data for plant " + plantId, resultData.toString());

            } else {
                //plant data retrieving succesfully completed (NO KO message, yes OK or warning)
                ResultDataFormatter.addResultDataOpenTag(resultData);
                addPlantOpenTag(plantId, resultData);
                if (testerInfoNoWarning && plantsUserDataNoWarning) {
                    //ALL DATA SUCCESSFULLY RETRIEVED
                    ResultDataFormatter.addHeaderData(operationResult);
                    resultData.append(plantUsersInformations).append(plantTesterInformations);
                } else if (!testerInfoNoWarning) {
                    //warning on tester infos (i.e. no data data found)
                    ResultDataFormatter.addHeaderData(operationResult, ResultDataFormatter.WARNING_RESULT, "No tester information found for plant " + plantId);
                    resultData.append(plantUsersInformations).append(plantTesterInformations);
                } else {
                    //warning su user data (no data found or problems during resultset closing)
                    ResultDataFormatter.addHeaderData(operationResult, ResultDataFormatter.WARNING_RESULT, "No user information found for plant " + plantId);
                    resultData.append(plantUsersInformations).append(plantTesterInformations);
                }
                addPlantClosingTag(resultData);
                ResultDataFormatter.addResultDataClosingTag(resultData);
            }
        }

        result.append(operationResult);
        result.append(resultData);
        ResultDataFormatter.addActionResultClosingTag(result);
        return result;
    }

    /**
     * retrieve the list of plants actually available.
     *
     * @return a StringBuffer representing an xml hat contains the whole data for the plant identified by plantId
     */
    public static StringBuffer getPlantsList() {
        StringBuffer result = new StringBuffer();
        Vector tVobList = VobManager.getTVobsInfo();
        int tVobCount = tVobList.size();
        Hashtable oneVobInfo;
        ResultDataFormatter.addXmlHeader(result);
        ResultDataFormatter.addActionResultOpenTag(result);
        ResultDataFormatter.addHeaderData(result);
        ResultDataFormatter.addResultDataOpenTag(result);
        result.append("<PLANTS_LIST>");
        for (int i = 0; i < tVobCount; i++) {
            oneVobInfo = (Hashtable) tVobList.get(i);
            //add plant ID
            result.append("<PLANT>").append(oneVobInfo.get(VobManager.VOB_PLANT_ATTRIBUTE)).append("</PLANT>");
        }
        result.append("</PLANTS_LIST>");
        ResultDataFormatter.addResultDataClosingTag(result);
        ResultDataFormatter.addActionResultClosingTag(result);
        return result;
    }

}
