package it.txt.tpms.bosch.manager;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.tp.TP;
import it.txt.tpms.tp.list.TPList;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.utils.QueryUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 25-mag-2006
 * Time: 9.20.06
 */
public class BoschReportsManager extends QueryUtils {
   /*
select tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tp.FACILITY, tp.FROM_PLANT, tp.OWNER_LOGIN, tp.OWNER_EMAIL, tp.ORIGIN, tp.ORIGIN_LBL, tp.LINESET_NAME, tp.TO_PLANT, tp.TESTER_INFO, tp.VALID_LOGIN, tp.PROD_LOGIN, tp.LAST_ACTION_ACTOR, tp.LAST_ACTION_DATE, tp.STATUS, tp.DISTRIB_DATE, tp.PROD_DATE, tp.DIVISION, tp.VOB_STATUS, tp.INSTALLATION_ID, tp.PRIS_STATUS, tp.PRIS_FOUND_DATE, tc.COMMENT_BODY, lsf.LINESET_NAME
from tp_plant tp, tp_comments tc, tpms_lineset_filters lsf
where tp.status = 'In_Production'  and tc.JOBNAME (+)= tp.JOBNAME and tc.JOB_RELEASE (+)= tp.JOB_RELEASE and tc.JOB_REVISION (+)= tp.JOB_REVISION and
	  lsf.LINESET_NAME = tp.LINESET_NAME and lsf.INSTALLATION_ID = tp.INSTALLATION_ID and
	  lsf.FILTER_DISPLAY_VALUE = 'final testing' and lsf.TPMS_LOGIN = 'queryuser' and lsf.OWNER_INSTALLATION_ID = 'TXT'
order by tp.JOBNAME, tp.JOB_RELEASE, tp.job_revision, tp.TPMS_VERSION
    */

    public static TPList getTPs(String fromPlant, String toPlant, String division, String productLine, String jobName, Date lastActionFrom, Date lastActionTo, String linesetFilterDisplayValue, TpmsUser tpmsUser,String Status) throws TpmsException {

        String querySelect = "select tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tp.FACILITY, tp.FROM_PLANT, tp.OWNER_LOGIN, tp.OWNER_EMAIL, tp.ORIGIN, tp.ORIGIN_LBL, tp.LINESET_NAME, tp.TO_PLANT, tp.TESTER_INFO, tp.VALID_LOGIN, tp.PROD_LOGIN, tp.LAST_ACTION_ACTOR, TO_CHAR(tp.LAST_ACTION_DATE,'" + ORACLE_DATE_FORMAT + "') as LAST_ACTION_DATE, tp.STATUS, TO_CHAR(tp.DISTRIB_DATE,'" + ORACLE_DATE_FORMAT + "') as DISTRIB_DATE, TO_CHAR(tp.PROD_DATE,'" + ORACLE_DATE_FORMAT + "') as PROD_DATE, tp.DIVISION, tp.VOB_STATUS, tp.INSTALLATION_ID, tp.PRIS_STATUS, TO_CHAR(tp.PRIS_FOUND_DATE,'" + ORACLE_DATE_FORMAT + "') as PRIS_FOUND_DATE, tp.PRODUCTION_AREA_ID, tc.COMMENT_BODY ";
        String queryFrom =   "from tp_plant tp, tp_comments tc ";
        String queryBaseWhere =  "where tc.JOBNAME (+)= tp.JOBNAME and tc.JOB_RELEASE (+)= tp.JOB_RELEASE and tc.JOB_REVISION (+)= tp.JOB_REVISION and tc.TPMS_VER (+)= tp.TPMS_VER and tp.status = '" + Status +"'";
        String userConditions = null;

        if (!GeneralStringUtils.isEmptyString(fromPlant)) {
            userConditions = "and tp.FROM_PLANT = " + getStringValueForQuery(fromPlant);
        }

        if (!GeneralStringUtils.isEmptyString(toPlant)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.TO_PLANT = " + getStringValueForQuery(toPlant);
            } else {
                userConditions += " and tp.TO_PLANT = " + getStringValueForQuery(toPlant);
            }
        }

        if (!GeneralStringUtils.isEmptyString(division)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.DIVISION = " + getStringValueForQuery(division);
            } else {
                userConditions += " and tp.DIVISION = " + getStringValueForQuery(division);
            }
        }

        String lastActionCondition = getDateInInterval("tp.LAST_ACTION_DATE", lastActionFrom, lastActionTo);

        if (!GeneralStringUtils.isEmptyString(lastActionCondition)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and " + lastActionCondition;
            } else {
                userConditions += " and " + lastActionCondition;
            }
        }


        if (!GeneralStringUtils.isEmptyString(jobName)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.JOBNAME like " + getStringValueForQuery(jobName);
            } else {
                userConditions += " and tp.JOBNAME like " + getStringValueForQuery(jobName);
            }
        }

        //String productLine

        if (!GeneralStringUtils.isEmptyString(productLine)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.LINE like " + getStringValueForQuery(productLine);
            } else {
                userConditions += " and tp.LINE like " + getStringValueForQuery(productLine);
            }
        }
        String groupBy = "";
        if (!GeneralStringUtils.isEmptyString( linesetFilterDisplayValue) ) {
            if ( tpmsUser != null ) {
                queryFrom = queryFrom + ", tpms_lineset_filters lsf";
                queryBaseWhere = queryBaseWhere + " and lsf.LINESET_NAME = tp.LINESET_NAME and lsf.INSTALLATION_ID = tp.INSTALLATION_ID ";
                queryBaseWhere = queryBaseWhere + " and lsf.FILTER_DISPLAY_VALUE = " + getStringValueForQuery( linesetFilterDisplayValue );
                queryBaseWhere = queryBaseWhere + " and lsf.TPMS_LOGIN = " + getStringValueForQuery( tpmsUser.getTpmsLogin() );
                queryBaseWhere = queryBaseWhere + " and lsf.OWNER_INSTALLATION_ID = " + getStringValueForQuery( tpmsUser.getInstallationId() );
                groupBy = "group by tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tp.FACILITY, tp.FROM_PLANT, tp.OWNER_LOGIN, tp.OWNER_EMAIL, tp.ORIGIN, tp.ORIGIN_LBL, tp.LINESET_NAME, tp.TO_PLANT, tp.TESTER_INFO, tp.VALID_LOGIN, tp.PROD_LOGIN, tp.LAST_ACTION_ACTOR, TO_CHAR(tp.LAST_ACTION_DATE,'" + ORACLE_DATE_FORMAT + "'), tp.STATUS, TO_CHAR(tp.DISTRIB_DATE,'" + ORACLE_DATE_FORMAT + "'), TO_CHAR(tp.PROD_DATE,'" + ORACLE_DATE_FORMAT + "'), tp.DIVISION, tp.VOB_STATUS, tp.INSTALLATION_ID, tp.PRIS_STATUS, TO_CHAR(tp.PRIS_FOUND_DATE,'" + ORACLE_DATE_FORMAT + "'), tc.COMMENT_BODY, tp.PRODUCTION_AREA_ID ";

                // group by tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tp.FACILITY, tp.FROM_PLANT, tp.OWNER_LOGIN, tp.OWNER_EMAIL, tp.ORIGIN, tp.ORIGIN_LBL, tp.LINESET_NAME, tp.TO_PLANT, tp.TESTER_INFO, tp.VALID_LOGIN, tp.PROD_LOGIN, tp.LAST_ACTION_ACTOR, TO_CHAR(tp.LAST_ACTION_DATE,'DDMMYYYYHH24MISS') , tp.STATUS, TO_CHAR(tp.DISTRIB_DATE,'DDMMYYYYHH24MISS') , TO_CHAR(tp.PROD_DATE,'DDMMYYYYHH24MISS') , tp.DIVISION, tp.VOB_STATUS, tp.INSTALLATION_ID, tp.PRIS_STATUS, TO_CHAR(tp.PRIS_FOUND_DATE,'DDMMYYYYHH24MISS'), tc.COMMENT_BODY
            } else {
                errorLog( "BoschReportsManager :: getTPs : I got a ls user filter (" + linesetFilterDisplayValue + ") but current user is null");
            }
        }


        String query = querySelect + " " + queryFrom + " " + queryBaseWhere;
        if (!GeneralStringUtils.isEmptyString(userConditions)) {
            query = query + " " + userConditions;
        }
        query = query + " " + groupBy;
        debugLog("BoschReportsManager :: getTPs : query = " + query);
        ResultSet tpDataRs = executeSelectQuery(query);
        TPList result = new TPList();
        long tpCount;
        if (tpDataRs != null) {

            try {
                TP tmpTP;
                tpCount = 0;
                while (tpDataRs.next()) {

                    tmpTP = new TP(tpDataRs.getString("JOBNAME"), tpDataRs.getInt("JOB_RELEASE"), tpDataRs.getString("JOB_REVISION"), tpDataRs.getInt("TPMS_VER"));

                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("LAST_ACTION_DATE"))) {
                        try {
                            tmpTP.setLastActionDate(simpleDateFormat.parse(tpDataRs.getString("LAST_ACTION_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("LAST_ACTION_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getTPs", e);
                        }
                    }
                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("DISTRIB_DATE"))) {
                        try {
                            tmpTP.setDistributionDate(simpleDateFormat.parse(tpDataRs.getString("DISTRIB_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("DISTRIB_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getTPs", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("PROD_DATE"))) {
                        try {
                            tmpTP.setProductionDate(simpleDateFormat.parse(tpDataRs.getString("PROD_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("PROD_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getTPs", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("PRIS_FOUND_DATE"))) {
                        try {
                            tmpTP.setPrisFoundDate(simpleDateFormat.parse(tpDataRs.getString("PRIS_FOUND_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("PRIS_FOUND_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getTPs", e);
                        }
                    }
                    tmpTP.setInstallationId(tpDataRs.getString("INSTALLATION_ID"));
                    tmpTP.setOwnerLogin(tpDataRs.getString("OWNER_LOGIN"));
                    tmpTP.setValidLogin(tpDataRs.getString("VALID_LOGIN"));
                    tmpTP.setLine(tpDataRs.getString("LINE"));
                    tmpTP.setFacility(tpDataRs.getString("FACILITY"));
                    tmpTP.setFromPlant(tpDataRs.getString("FROM_PLANT"));
                    tmpTP.setOwnerEmail(tpDataRs.getString("OWNER_EMAIL"));
                    tmpTP.setOrigin(tpDataRs.getString("ORIGIN"));
                    tmpTP.setOriginLabel(tpDataRs.getString("ORIGIN_LBL"));
                    tmpTP.setLinesetName(tpDataRs.getString("LINESET_NAME"));
                    tmpTP.setToPlant(tpDataRs.getString("TO_PLANT"));
                    tmpTP.setProductionAreaId( tpDataRs.getString( "PRODUCTION_AREA_ID" ));
                    tmpTP.setTesterInfo(tpDataRs.getString("TESTER_INFO"));
                    tmpTP.setProdLogin(tpDataRs.getString("PROD_LOGIN"));
                    tmpTP.setLastActionActor(tpDataRs.getString("LAST_ACTION_ACTOR"));
                    tmpTP.setStatus(tpDataRs.getString("STATUS"));
                    tmpTP.setPrisStatus(tpDataRs.getString("PRIS_STATUS"));
                    tmpTP.setDivision(tpDataRs.getString("DIVISION"));
                    tmpTP.setVobStatus(tpDataRs.getString("VOB_STATUS"));
                    tmpTP.setDbComments(tpDataRs.getString("COMMENT_BODY"));
                    result.addElement(tmpTP);
                    tpCount ++;
                }
                debugLog( "BoschReportsManager :: getTPs: tpCount = " + tpCount );
            } catch (SQLException e) {
                throw new TpmsException("General error while fetchig tp data " + e.getMessage(), "BoschReportsManager :: getTPs", e);
            } finally{
                try {
                    tpDataRs.close();
                } catch (SQLException e) {
                    errorLog("BoschReportsManager :: getTPs: error while closing ResultSet", e);
                }
            }

        }


        return result;
    }

    public static TPList getNotInProductionTPs(String fromPlant, String toPlant, String division, String productLine, String jobName, Date lastActionFrom, Date lastActionTo, String linesetFilterDisplayValue, TpmsUser tpmsUser ) throws TpmsException {

        String querySelect = "select tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tp.FACILITY, tp.FROM_PLANT, tp.OWNER_LOGIN, tp.OWNER_EMAIL, tp.ORIGIN, tp.ORIGIN_LBL, tp.LINESET_NAME, tp.TO_PLANT, tp.TESTER_INFO, tp.VALID_LOGIN, tp.PROD_LOGIN, tp.LAST_ACTION_ACTOR, TO_CHAR(tp.LAST_ACTION_DATE,'" + ORACLE_DATE_FORMAT + "') as LAST_ACTION_DATE, tp.STATUS, TO_CHAR(tp.DISTRIB_DATE,'" + ORACLE_DATE_FORMAT + "') as DISTRIB_DATE, TO_CHAR(tp.PROD_DATE,'" + ORACLE_DATE_FORMAT + "') as PROD_DATE, tp.DIVISION, tp.VOB_STATUS, tp.INSTALLATION_ID, tp.PRIS_STATUS, TO_CHAR(tp.PRIS_FOUND_DATE,'" + ORACLE_DATE_FORMAT + "') as PRIS_FOUND_DATE, tp.PRODUCTION_AREA_ID, tc.COMMENT_BODY ";
        String queryFrom ="from tp_plant tp, tp_comments tc ";
        String queryBaseWhere = "where tc.JOBNAME (+)= tp.JOBNAME and tc.JOB_RELEASE (+)= tp.JOB_RELEASE and tc.JOB_REVISION (+)= tp.JOB_REVISION and tc.TPMS_VER (+)= tp.TPMS_VER and tp.status in ('Distributed', 'In_Validation', 'Ready_to_production')";

        String userConditions = null;

        if (!GeneralStringUtils.isEmptyString(fromPlant)) {
            userConditions = "and tp.FROM_PLANT = " + getStringValueForQuery(fromPlant);
        }

        if (!GeneralStringUtils.isEmptyString(toPlant)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.TO_PLANT = " + getStringValueForQuery(toPlant);
            } else {
                userConditions += " and tp.TO_PLANT = " + getStringValueForQuery(toPlant);
            }
        }

        if (!GeneralStringUtils.isEmptyString(division)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.DIVISION = " + getStringValueForQuery(division);
            } else {
                userConditions += " and tp.DIVISION = " + getStringValueForQuery(division);
            }
        }

        String lastActionCondition = getDateInInterval("tp.LAST_ACTION_DATE", lastActionFrom, lastActionTo);

        if (!GeneralStringUtils.isEmptyString(lastActionCondition)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and " + lastActionCondition;
            } else {
                userConditions += " and " + lastActionCondition;
            }
        }


        if (!GeneralStringUtils.isEmptyString(jobName)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.JOBNAME like " + getStringValueForQuery(jobName);
            } else {
                userConditions += " and tp.JOBNAME like " + getStringValueForQuery(jobName);
            }
        }

        //String productLine

        if (!GeneralStringUtils.isEmptyString(productLine)) {
            if (GeneralStringUtils.isEmptyString(userConditions)) {
                userConditions = "and tp.LINE like " + getStringValueForQuery(productLine);
            } else {
                userConditions += " and tp.LINE like " + getStringValueForQuery(productLine);
            }
        }

        String groupBy = "";
        if (!GeneralStringUtils.isEmptyString( linesetFilterDisplayValue) ) {
            if ( tpmsUser != null ) {
                queryFrom = queryFrom + ", tpms_lineset_filters lsf";
                queryBaseWhere = queryBaseWhere + " and lsf.LINESET_NAME = tp.LINESET_NAME and lsf.INSTALLATION_ID = tp.INSTALLATION_ID ";
                queryBaseWhere = queryBaseWhere + " and lsf.FILTER_DISPLAY_VALUE = " + getStringValueForQuery( linesetFilterDisplayValue );
                queryBaseWhere = queryBaseWhere + " and lsf.TPMS_LOGIN = " + getStringValueForQuery( tpmsUser.getTpmsLogin() );
                queryBaseWhere = queryBaseWhere + " and lsf.OWNER_INSTALLATION_ID = " + getStringValueForQuery( tpmsUser.getInstallationId() );
                groupBy = "group by tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tp.FACILITY, tp.FROM_PLANT, tp.OWNER_LOGIN, tp.OWNER_EMAIL, tp.ORIGIN, tp.ORIGIN_LBL, tp.LINESET_NAME, tp.TO_PLANT, tp.TESTER_INFO, tp.VALID_LOGIN, tp.PROD_LOGIN, tp.LAST_ACTION_ACTOR, TO_CHAR(tp.LAST_ACTION_DATE,'" + ORACLE_DATE_FORMAT + "'), tp.STATUS, TO_CHAR(tp.DISTRIB_DATE,'" + ORACLE_DATE_FORMAT + "'), TO_CHAR(tp.PROD_DATE,'" + ORACLE_DATE_FORMAT + "'), tp.DIVISION, tp.VOB_STATUS, tp.INSTALLATION_ID, tp.PRIS_STATUS, TO_CHAR(tp.PRIS_FOUND_DATE,'" + ORACLE_DATE_FORMAT + "'), tc.COMMENT_BODY, tp.PRODUCTION_AREA_ID ";

                // group by tp.JOBNAME, tp.JOB_RELEASE, tp.JOB_REVISION, tp.TPMS_VER, tp.LINE, tp.FACILITY, tp.FROM_PLANT, tp.OWNER_LOGIN, tp.OWNER_EMAIL, tp.ORIGIN, tp.ORIGIN_LBL, tp.LINESET_NAME, tp.TO_PLANT, tp.TESTER_INFO, tp.VALID_LOGIN, tp.PROD_LOGIN, tp.LAST_ACTION_ACTOR, TO_CHAR(tp.LAST_ACTION_DATE,'DDMMYYYYHH24MISS') , tp.STATUS, TO_CHAR(tp.DISTRIB_DATE,'DDMMYYYYHH24MISS') , TO_CHAR(tp.PROD_DATE,'DDMMYYYYHH24MISS') , tp.DIVISION, tp.VOB_STATUS, tp.INSTALLATION_ID, tp.PRIS_STATUS, TO_CHAR(tp.PRIS_FOUND_DATE,'DDMMYYYYHH24MISS'), tc.COMMENT_BODY
            } else {
                errorLog( "BoschReportsManager :: getNotInProductionTPs : I got a ls user filter (" + linesetFilterDisplayValue + ") but current user is null");
            }
        }


       String query = querySelect + " " + queryFrom + " " + queryBaseWhere;
        if (!GeneralStringUtils.isEmptyString(userConditions)) {
            query = query + userConditions + groupBy;
        }

        debugLog("BoschReportsManager :: getNotInProductionTPs : query = " + query);
        ResultSet tpDataRs = executeSelectQuery(query);
        TPList result = new TPList();

        if (tpDataRs != null) {
            try {
                TP tmpTP;
                while (tpDataRs.next()) {

                    tmpTP = new TP(tpDataRs.getString("JOBNAME"), tpDataRs.getInt("JOB_RELEASE"), tpDataRs.getString("JOB_REVISION"), tpDataRs.getInt("TPMS_VER"));

                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("LAST_ACTION_DATE"))) {
                        try {
                            tmpTP.setLastActionDate(simpleDateFormat.parse(tpDataRs.getString("LAST_ACTION_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("LAST_ACTION_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getInProductionTPs", e);
                        }
                    }
                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("DISTRIB_DATE"))) {
                        try {
                            tmpTP.setDistributionDate(simpleDateFormat.parse(tpDataRs.getString("DISTRIB_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("DISTRIB_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getInProductionTPs", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("PROD_DATE"))) {
                        try {
                            tmpTP.setProductionDate(simpleDateFormat.parse(tpDataRs.getString("PROD_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("PROD_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getInProductionTPs", e);
                        }
                    }

                    if (!GeneralStringUtils.isEmptyString(tpDataRs.getString("PRIS_FOUND_DATE"))) {
                        try {
                            tmpTP.setPrisFoundDate(simpleDateFormat.parse(tpDataRs.getString("PRIS_FOUND_DATE")));
                        } catch (ParseException e) {
                            String msg = "Error during managing query result: unable to parse sent date (" + tpDataRs.getString("PRIS_FOUND_DATE") + ")";
                            throw new TpmsException(msg, "BoschReportsManager :: getInProductionTPs", e);
                        }
                    }

                    tmpTP.setInstallationId(tpDataRs.getString("INSTALLATION_ID"));
                    tmpTP.setOwnerLogin(tpDataRs.getString("OWNER_LOGIN"));
                    tmpTP.setValidLogin(tpDataRs.getString("VALID_LOGIN"));
                    tmpTP.setLine(tpDataRs.getString("LINE"));
                    tmpTP.setFacility(tpDataRs.getString("FACILITY"));
                    tmpTP.setFromPlant(tpDataRs.getString("FROM_PLANT"));
                    tmpTP.setOwnerEmail(tpDataRs.getString("OWNER_EMAIL"));
                    tmpTP.setOrigin(tpDataRs.getString("ORIGIN"));
                    tmpTP.setOriginLabel(tpDataRs.getString("ORIGIN_LBL"));
                    tmpTP.setLinesetName(tpDataRs.getString("LINESET_NAME"));
                    tmpTP.setToPlant(tpDataRs.getString("TO_PLANT"));
                    tmpTP.setProductionAreaId( tpDataRs.getString( "PRODUCTION_AREA_ID" ));
                    tmpTP.setTesterInfo(tpDataRs.getString("TESTER_INFO"));
                    tmpTP.setProdLogin(tpDataRs.getString("PROD_LOGIN"));
                    tmpTP.setLastActionActor(tpDataRs.getString("LAST_ACTION_ACTOR"));
                    tmpTP.setStatus(tpDataRs.getString("STATUS"));
                    tmpTP.setPrisStatus(tpDataRs.getString("PRIS_STATUS"));
                    tmpTP.setDivision(tpDataRs.getString("DIVISION"));
                    tmpTP.setVobStatus(tpDataRs.getString("VOB_STATUS"));
                    tmpTP.setDbComments(tpDataRs.getString("COMMENT_BODY"));
                    result.addElement(tmpTP);
                }

            } catch (SQLException e) {
                throw new TpmsException("General error while fetchig tp data " + e.getMessage(), "BoschReportsManager :: getInProductionTPs", e);
            } finally{
                try {
                    tpDataRs.close();
                } catch (SQLException e) {
                    errorLog("BoschReportsManager :: getInProductionTPs: error while closing ResultSet", e);
                }
            }

        }


        return result;
    }

}
