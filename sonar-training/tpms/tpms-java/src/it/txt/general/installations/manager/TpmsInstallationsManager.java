package it.txt.general.installations.manager;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.installations.TpmsInstallationData;
import it.txt.general.installations.list.TpmsInstallationsList;
import it.txt.general.utils.GeneralStringUtils;
import tpms.TpmsException;
import tpms.utils.QueryUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-ott-2006
 * Time: 12.55.50
 * To change this template use File | Settings | File Templates.
 */
public class TpmsInstallationsManager extends AfsCommonStaticClass {

    public static TpmsInstallationsList getTpmsInstallationsInfo() throws TpmsException {
        return getTpmsInstallationsInfo(true, false);
    }

    public static TpmsInstallationsList getTpmsInstallationsInfoWithUsersData() throws TpmsException {
        return getTpmsInstallationsInfo(true, true);
    }

    public static TpmsInstallationsList getAllTpmsInstallationsInfoWithUsersData() throws TpmsException {
        return getTpmsInstallationsInfo(false, true);
    }

    public static TpmsInstallationsList getAllTpmsInstallationsInfo() throws TpmsException {
        return getTpmsInstallationsInfo(false, false);
    }

    /**
     *
     * @param loadUsersData
     * @return the list of tpms installations, if loadUsersData is true perform also the user data loadaing
     * @throws TpmsException
     */
    protected static TpmsInstallationsList getTpmsInstallationsInfo(boolean withoutLocalPlant, boolean loadUsersData) throws TpmsException{
        String actionDescription = "TpmsInstallationsManager :: getTpmsInstallationsInfo";
        TpmsInstallationsList tpmsInstallationsList = new TpmsInstallationsList();
        String query = "select id, installation_id, server_name " +
                       "from TPMS_INSTALLATIONS";


        if (withoutLocalPlant){
            query += " where installation_id != " + QueryUtils.getStringValueForQuery(tpmsConfiguration.getLocalPlant());
        }

        ResultSet rsInstallation = executeSelectQuery(query);

        if (rsInstallation != null){
            try {
                TpmsInstallationData tpmsInstallationData;
                while (rsInstallation.next()){
                    tpmsInstallationData  = new TpmsInstallationData(rsInstallation.getString("id"),
                                                                     rsInstallation.getString("installation_id"),
                                                                     rsInstallation.getString("server_name"));
                    if (loadUsersData) tpmsInstallationData.loadUserData();
                    tpmsInstallationsList.addElement(tpmsInstallationData);
                }
            } catch (SQLException e) {
                errorLog(actionDescription + " : SQLException while getting tpms installations list " + e.getMessage(), e);
            } finally {
                try {
                    rsInstallation.close();
                } catch (SQLException e) {
                    errorLog(actionDescription + " : SQLException while closing tpms installations list result set" + e.getMessage(), e);
                }
            }

        }
        return tpmsInstallationsList;
    }




    public static TpmsInstallationData getLocalTpmsInstallationsInfoWithUsersData() throws TpmsException{
        return getTpmsInstallationInfo(tpmsConfiguration.getLocalPlant(), true);
    }

    public static TpmsInstallationData getLocalTpmsInstallationsInfo() throws TpmsException{
        return getTpmsInstallationInfo(tpmsConfiguration.getLocalPlant(),  false);
    }

   /**
     *
     * @param loadUsersData
     * @return the list of tpms installations, if loadUsersData is true perform also the user data loadaing
     * @throws TpmsException
     */
    public static TpmsInstallationData getTpmsInstallationInfo(String tpmsPlantId, boolean loadUsersData) throws TpmsException{
        String actionDescription = "TpmsInstallationsManager :: getTpmsInstallationsInfo";

        if (GeneralStringUtils.isEmptyString(tpmsPlantId)) {
           throw  new TpmsException("Given plant Id is null or empty", actionDescription, "Given plant Id is null or empty");
        }


        String query = "select id, installation_id, server_name " +
                       "from TPMS_INSTALLATIONS " +
                       "where installation_id = " + QueryUtils.getStringValueForQuery(tpmsPlantId);

        TpmsInstallationData tpmsInstallationData = null;
        ResultSet rsInstallation = executeSelectQuery(query);

        if (rsInstallation != null ){
            try {

               if (rsInstallation.next()) {
                    tpmsInstallationData  = new TpmsInstallationData(rsInstallation.getString("id"),
                                                                     rsInstallation.getString("installation_id"),
                                                                     rsInstallation.getString("server_name"));
                    if (loadUsersData) tpmsInstallationData.loadUserData();
               } else {
                   throw  new TpmsException("The current plant configurations are not present in tpms installation list (plant id = " + tpmsPlantId + "),<br>kindly advice your local system administrator", actionDescription, "The current plant configurations are not present in tpms installation");
               }
            } catch (SQLException e) {
                errorLog(actionDescription + " : SQLException while getting tpms installations list " + e.getMessage(), e);
            } finally {
                try {
                    rsInstallation.close();
                } catch (SQLException e) {
                    errorLog(actionDescription + " : SQLException while closing tpms installations list result set" + e.getMessage(), e);
                }
            }

        }
        return tpmsInstallationData;
    }


}
