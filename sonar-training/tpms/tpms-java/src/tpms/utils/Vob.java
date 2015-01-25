package tpms.utils;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 4-feb-2006
 * Time: 15.21.15
 * To change this template use File | Settings | File Templates.
 */
public class Vob {


    private String name = "";
    private String description = "";
    private String plant = "";
    private String type = "";

    private Vector divisionsList = new Vector();

    private Vector tVobsList = new Vector();



    public Vob(String name, String description, String plant, String type) {
        this.name = name;
        this.description = description;
        this.plant = plant;
        this.type = type;
    }

    public Vob(String name, String description, String plant, String type, Vector divisionsList) {
        this.name = name;
        this.description = description;
        this.plant = plant;
        this.type = type;
        this.divisionsList = divisionsList;
    }




    public Vob(String name, String description, String plant, String type, Vector divisionslist, Vector tVobsList){
        this.name = name;
        this.description = description;
        this.plant = plant;
        this.type = type;
        this.divisionsList = divisionslist;
        this.tVobsList = tVobsList;
    }


    public Vector getDivisionsList() {
        return divisionsList;
    }

    public Vector gettVobsList() {
        return tVobsList;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPlant() {
        return plant;
    }

    public String getType() {
        return type;
    }


}
