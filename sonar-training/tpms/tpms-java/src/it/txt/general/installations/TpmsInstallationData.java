package it.txt.general.installations;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.users.list.TpmsUsersList;
import it.txt.tpms.users.manager.TpmsUsersManager;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 16-ott-2006
 * Time: 13.04.27
 */
public class TpmsInstallationData {
    private String id;
    private String tpmsInstallationId;
    private String serverAddress;
    private TpmsUsersList usersList;
    private boolean userDataAlreadyLoaded = false;

    private HashMap thisHashMap;

    public static final String INSTALLATION_ID = "ID";
    public static final String INSTALLATION_PLANT_ID = "INSTALLATION_ID";
    public static final String INSTALLATION_SERVER_ADDRESS = "SERVER_ADDRESS";


    public TpmsInstallationData(String id, String installationId, String serverAddress) {
        this.id = id;
        this.tpmsInstallationId = installationId;
        this.serverAddress = serverAddress;
    }

    private void populateHashMapData() {
        thisHashMap = new HashMap();
        thisHashMap.put(INSTALLATION_ID, this.getId());
        thisHashMap.put(INSTALLATION_PLANT_ID, this.getTpmsInstallationId());
        thisHashMap.put(INSTALLATION_SERVER_ADDRESS, this.getServerAddress());
    }

    public HashMap getInstallationDataHashMap() {
        if (thisHashMap == null || thisHashMap.size() == 0) {
            populateHashMapData();
        }
        return thisHashMap;
    }


    /**
     * load the informations related to the urrent installation data
     *
     * @return true if the load succed, false otherwise.
     */
    public boolean loadUserData() {
        if (GeneralStringUtils.isEmptyString(tpmsInstallationId)) {
            return false;
        } else if (!userDataAlreadyLoaded) {
            usersList = TpmsUsersManager.loadPlantUsers(tpmsInstallationId);
            userDataAlreadyLoaded = true;
            return true;
        } else {
            return false;
        }


    }

    public String getId() {
        return id;
    }

    public String getTpmsInstallationId() {
        return tpmsInstallationId;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public TpmsUsersList getUsersList() {
        if (usersList == null && !userDataAlreadyLoaded) {
            loadUserData();
        }
        return usersList;
    }
}
