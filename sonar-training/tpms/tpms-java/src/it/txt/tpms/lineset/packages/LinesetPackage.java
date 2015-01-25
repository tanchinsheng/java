package it.txt.tpms.lineset.packages;

import it.txt.afs.AfsCommonClass;
import it.txt.general.installations.TpmsInstallationData;
import it.txt.tpms.lineset.packages.manager.LinesetPackageManager;
import it.txt.tpms.users.TpmsUser;
import tpms.utils.Vob;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 19-ott-2006
 * Time: 9.08.39
 * This class is usefull to reports the whole set of data needed for sending a linese to a remote plant.
 */
public class LinesetPackage extends AfsCommonClass {

    private String linesetName = null;
    private String baseline = null;
    private TpmsInstallationData destinationPlantData = null;
    private TpmsUser destinationTpmsUser = null;
    private TpmsUser originalOwner = null;
    private TpmsInstallationData sourcePlantData = null;
    private String linesetPackageId = null;
    private Vob sourceVob = null;
    private String testerFamily = null;
    private String packageFileName = null;

    /* public LinesetPackage (String linesetName, String baseline, String testerFamily, String destinationPlantId,
                          String destinationUser, String originalOwner, String sourcePlantId, String sourceVobName)
                          throws TpmsException {
        this.linesetName = linesetName;
        this.baseline = baseline;
        this.sourceVob = VobManager.getVobDataByVobName(sourceVobName);
        this.testerFamily = testerFamily;

        TpmsInstallationData tpmsInstallationData = TpmsInstallationsManager.getTpmsInstallationInfo(destinationPlantId, true);
        //destination plant data & users management
        this.destinationPlantData = tpmsInstallationData;
        TpmsUsersList tpmsUsersList = tpmsInstallationData.getUsersList();
        this.destinationTpmsUser = tpmsUsersList.getElementByTpmsLogin(destinationUser);
        //source plant data & users management
        tpmsInstallationData = TpmsInstallationsManager.getTpmsInstallationInfo(destinationPlantId, true);
        this.sourcePlantData = tpmsInstallationData;
        tpmsUsersList = tpmsInstallationData.getUsersList();
        this.originalOwner = tpmsUsersList.getElementByTpmsLogin(originalOwner);
        this.packageFileName = LinesetPackageManager.generatePackageFileName(linesetName, baseline, testerFamily, destinationPlantId,
                destinationUser, originalOwner, sourcePlantId, sourceVobName);
        debugLog("LinesetPackage :: contructor : packageFileName = " + packageFileName);
        this.linesetPackageId = generateId();
    }*/


    public LinesetPackage ( String name, String baseline, String testerFamily, TpmsInstallationData destinationPlantData,
                            TpmsUser destinationTpmsUser, TpmsUser originalOwner, TpmsInstallationData sourcePlantData,
                            Vob sourceVob ) {
        this.linesetName = name;
        this.baseline = baseline;
        this.destinationPlantData = destinationPlantData;
        this.destinationTpmsUser = destinationTpmsUser;
        this.originalOwner = originalOwner;
        this.sourcePlantData = sourcePlantData;
        this.sourceVob = sourceVob;
        this.testerFamily = testerFamily;
/*        debugLog("LinesetPackage :: contructor : linesetName = " + linesetName);
        debugLog("LinesetPackage :: contructor : baseline = " + baseline);
        debugLog("LinesetPackage :: contructor : destinationPlantData == null ? " + (destinationPlantData == null));
        debugLog("LinesetPackage :: contructor : originalOwner = " + originalOwner.getName());
        debugLog("LinesetPackage :: contructor : sourcePlantData == null ? " + (sourcePlantData == null));
        debugLog("LinesetPackage :: contructor : sourceVob = " + (sourceVob.getName()));
        debugLog("LinesetPackage :: contructor : testerFamily = " + testerFamily);*/
        this.packageFileName = LinesetPackageManager.generatePackageFileName( name, baseline, testerFamily, destinationPlantData.getTpmsInstallationId(),
                destinationTpmsUser.getName(), originalOwner.getName(), sourcePlantData.getTpmsInstallationId(), sourceVob.getName() );
        debugLog( "LinesetPackage :: contructor : packageFileName = " + packageFileName );
        this.linesetPackageId = generateId();
    }


    private String generateId () {
        return "" + System.currentTimeMillis();
    }

    public Vob getSourceVob () {
        return sourceVob;
    }

    public String getLinesetPackageId () {
        return linesetPackageId;
    }

    public TpmsInstallationData getSourcePlantData () {
        return sourcePlantData;
    }

    public String getLinesetName () {
        return linesetName;
    }

    public String getBaseline () {
        return baseline;
    }

    public TpmsInstallationData getDestinationPlantData () {
        return destinationPlantData;
    }

    public TpmsUser getDestinationTpmsUser () {
        return destinationTpmsUser;
    }

    public TpmsUser getOriginalOwner () {
        return originalOwner;
    }

    public String getTesterFamily () {
        return testerFamily;
    }


    public String getPackageFileName () {
        return packageFileName;
    }

}
