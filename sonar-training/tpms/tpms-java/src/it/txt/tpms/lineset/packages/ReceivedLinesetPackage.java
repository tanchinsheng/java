package it.txt.tpms.lineset.packages;

import it.txt.tpms.lineset.packages.manager.LinesetPackageManager;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 13-nov-2006
 * Time: 16.27.49
 * This class maps a package received that contains lineset data.
 */
public class ReceivedLinesetPackage {

    private String linesetName = null;
    private String baseline = null;
    private String destinationPlantId = null;
    private String destinationTpmsLogin = null;
    private String originalOwnerTpmsLogin = null;
    private String sourcePlantId = null;
    private String linesetTempPackageId = null;
    private String sourceVobName = null;
    private String testerFamily = null;
    private String packageFileName = null;


    public ReceivedLinesetPackage(String linesetName, String baseline, String destinationPlantId, String destinationTpmsLogin, String originalOwnerTpmsLogin, String sourcePlantId, String sourceVobName, String testerFamily, String packageFileName) {
        this.linesetName = linesetName;
        this.baseline = baseline;
        this.destinationPlantId = destinationPlantId;
        this.destinationTpmsLogin = destinationTpmsLogin;
        this.originalOwnerTpmsLogin = originalOwnerTpmsLogin;
        this.sourcePlantId = sourcePlantId;
        this.sourceVobName = sourceVobName;
        this.testerFamily = testerFamily;
        this.packageFileName = packageFileName;

        this.linesetTempPackageId = generateId();
    }

    private String generateId(){
       return "" + System.currentTimeMillis();
    }

    public String getLinesetName() {
        return linesetName;
    }

    public String getBaseline() {
        return baseline;
    }

    public String getDestinationPlantId() {
        return destinationPlantId;
    }

    public String getDestinationTpmsLogin() {
        return destinationTpmsLogin;
    }

    public String getOriginalOwnerTpmsLogin() {
        return originalOwnerTpmsLogin;
    }

    public String getSourcePlantId() {
        return sourcePlantId;
    }

    public String getLinesetPackageId() {
        return linesetTempPackageId;
    }

    public String getSourceVobName() {
        return sourceVobName;
    }

    public String getTesterFamily() {
        return testerFamily;
    }

    public String getPackageFileName() {
        return packageFileName;
    }

    public String getFullyQualifiedIncomingFilePath(){
        return LinesetPackageManager.generateFullyQualifiedIncomingPackageFilePath(this);
    }
}
