package it.txt.tpms.tp;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-gen-2007
 * Time: 9.40.31
 */
public class ProductionArea {

    private String id;
    private String installationId;
    private String plant;
    private String description;
    //private String varName;


    public ProductionArea ( String id, String installationId, String plant, String description, String varName ) {
        this.id = id;
        this.installationId = installationId;
        this.plant = plant;
        this.description = description;
        //this.varName = varName;
    }

    public String getId () {
        return id;
    }

    public String getInstallationId () {
        return installationId;
    }

    public String getPlant () {
        return plant;
    }

    public String getDescription () {
        return description;
    }

    /*public String getVarName () {
        return varName;
    }*/

}
