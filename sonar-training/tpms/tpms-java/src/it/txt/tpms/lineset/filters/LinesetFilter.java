package it.txt.tpms.lineset.filters;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 4-dic-2006
 * Time: 15.27.16
 */
public class LinesetFilter {
    private String ownerTpmsLogin;
    private String id;
    private String displayValue;
    private String installationId;
    private String linesetName;
    private String vobName;
    private String ownerInstallationId;

    private HashMap thisHashMap;

    public static final String ID = "ID";
    public static final String INSTALLATION_PLANT_ID = "INSTALLATION_ID";
    public static final String TPMS_LOGIN = "TPMS_LOGIN";
    public static final String DISPLAY_VALUE = "DISPLAY_VALUE";
    public static final String LINESET_NAME = "LINESET_NAME";
    public static final String VOB_NAME = "VOB_NAME";
    public static final String OWNER_INSTALLATION_ID = "OWNER_INSTALLTION_ID";


    public LinesetFilter ( String tpmsLogin, String filterId, String filterDiplayValue, String installationId, String linesetName, String vobName, String ownerInstallationId ) {
        this.ownerTpmsLogin = tpmsLogin;
        this.id = filterId;
        this.displayValue = filterDiplayValue;
        this.installationId = installationId;
        this.linesetName = linesetName;
        this.vobName = vobName;
        this.ownerInstallationId = ownerInstallationId;

    }


    public LinesetFilter ( String tpmsLogin, String filterDiplayValue, String installationId, String linesetName, String vobName, String ownerInstallationId ) {
        this.ownerTpmsLogin = tpmsLogin;
        this.displayValue = filterDiplayValue;
        this.installationId = installationId;
        this.linesetName = linesetName;
        this.vobName = vobName;
        this.ownerInstallationId = ownerInstallationId;
    }



    private void populateHashMapData() {
        thisHashMap = new HashMap();
        thisHashMap.put(ID, this.getId());
        thisHashMap.put(INSTALLATION_PLANT_ID, this.getInstallationId());
        thisHashMap.put(TPMS_LOGIN, this.getOwnerTpmsLogin());
        thisHashMap.put(DISPLAY_VALUE, this.getDisplayValue());
        thisHashMap.put(LINESET_NAME, this.getLinesetName());
        thisHashMap.put(VOB_NAME, this.getVobName());
        thisHashMap.put(OWNER_INSTALLATION_ID, this.getOwnerInstallationId());

    }



    public HashMap getHashMap() {
        if (thisHashMap == null || thisHashMap.size() == 0) {
            populateHashMapData();
        }
        return thisHashMap;
    }

    public String getOwnerTpmsLogin () {
        return ownerTpmsLogin;
    }

    public String getId () {
        return id;
    }

    public String getDisplayValue () {
        return displayValue;
    }

    public String getInstallationId () {
        return installationId;
    }

    public String getLinesetName () {
        return linesetName;
    }

    public String getVobName () {
        return vobName;
    }


    public String getOwnerInstallationId () {
        return ownerInstallationId;
    }
}
