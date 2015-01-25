package it.txt.tpms.reports;

import tpms.utils.QueryUtils;
import tpms.TpmsException;

import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.txt.general.utils.VectorUtils;


public class ReportUtils extends QueryUtils {
    private static final String[] emptyRow = new String[2];

    static {
        emptyRow [0] = emptyRow [1] = "";
    }


    public static final String FROM_PLANT_CODE = "from_plant_code";
    public static final String FROM_PLANT_VALUE = "from_plant";

    public static final String TO_PLANT_CODE = "to_plant_code";
    public static final String TO_PLANT_VALUE = "to_plant";

    public static final String DIVISION_CODE = "division_code";
    public static final String DIVISION_VALUE = "division";

    /**
     *
     * @return a vector contianing all the from plant present in tp_plant table
     */
    public static Vector getTpFromPlantList() {
        String query = "select from_plant as from_plant_code, from_plant " +
                        "from tp_plant " +
                        "where from_plant IS NOT NULL "+
                        "group by from_plant " +
                        "order by from_plant";
        Vector result = new Vector();
        result.add(emptyRow);
        ResultSet fromPlantList = null;
        try {
            fromPlantList = executeSelectQuery(query);
        } catch (TpmsException e) {
            errorLog("ReportUtils :: getTpFromPlantList : executing query ( " + query + " )", e);
        }
        if (fromPlantList != null){
            result.addAll(VectorUtils.dumpResultSetToVectorOfStrArray(fromPlantList));

        }
        try {
            if (fromPlantList != null)
                fromPlantList.close();
        } catch (SQLException e) {
            errorLog("ReportUtils :: getTpFromPlantList : error while closing resultset", e);
        }
        return result;
    }

    /**
     *
     * @return a vector contianing all the to plant present in tp_plant table
     */
    public static Vector getTpToPlantList(){
        String query = "select to_plant as to_plant_code, to_plant " +
                        "from tp_plant " +
                        "where to_plant IS NOT NULL "+
                        "group by to_plant, to_plant " +
                        "order by to_plant ";
        Vector result = new Vector();
        result.add(emptyRow);
        ResultSet toPlantList = null;
        try {
            toPlantList = executeSelectQuery(query);
        } catch (TpmsException e) {
            errorLog("ReportUtils :: getTpToPlantList : executing query ( " + query + " )", e);
        }
        if (toPlantList != null){
            result.addAll(VectorUtils.dumpResultSetToVectorOfStrArray(toPlantList));
        }
        try {
            if (toPlantList != null)
                toPlantList.close();
        } catch (SQLException e) {
            errorLog("ReportUtils :: getTpToPlantList : error while closing resultset", e);
        }
        return result;
    }

    /**
     *
     * @return a vector contianing all the divisions present in tp_plant table
     */
    public static Vector getTpDivisionList(){
        String query = "select division as division_code, division " +
                        "from tp_plant " +
                        "where division IS NOT NULL "+
                        "group by division, division " +
                        "order by division";
        Vector result = new Vector();
        result.add(emptyRow);
        ResultSet divisiontList = null;
        try {
            divisiontList = executeSelectQuery(query);
        } catch (TpmsException e) {
            errorLog("ReportUtils :: getTpDivisionList : executing query ( " + query + " )", e);
        }
        if (divisiontList != null){
            result.addAll(VectorUtils.dumpResultSetToVectorOfStrArray(divisiontList));
        }
        try {
            if (divisiontList != null)
                divisiontList.close();
        } catch (SQLException e) {
            errorLog("ReportUtils :: getTpDivisionList : error while closing resultset", e);
        }
        return result;
    }
}