package it.txt.tpms.tp.utils;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import tpms.FacilityMgr;
import tpms.TesterInfoMgr;
import tpms.TpmsException;
import tpms.VobManager;
import tpms.utils.QueryUtils;
import tpms.utils.Vob;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-lug-2006
 * Time: 15.21.54
 * To change this template use File | Settings | File Templates.
 */
public class TpDataValidator extends AfsCommonStaticClass {


    /**
     * @param release
     * @return return true if the given release is a valid one, false otherwise
     */
    public static boolean checkJobReleaseFormat(String release) {
        if (!GeneralStringUtils.isEmptyString(release)) {
            //if the release is not empty check the release format...
            if (release.length() != 2) {
                return false;
            } else {
                try {
                    Short.parseShort(release);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param destinationPlantId
     * @return return the T - VOB configured for destinationPlantId if exists null if no VOB is configured for destinationPlantId
     */
    public static Vob checkDestinationPlant(String destinationPlantId) {
        Vob tVob = null;
        if (!GeneralStringUtils.isEmptyString(destinationPlantId)) {
            try {
                tVob = VobManager.searchTVobByDestinationPlant(destinationPlantId);
            } catch (Exception e) {
                errorLog("TpUtils :: checkDestinationPlant : error while retrieveing T Vob for " + destinationPlantId + " plant!", e);
            }
        }
        return tVob;
    }

    /**
     * @param jobName
     * @return return true if the given jobName is a valid one, false otherwise
     */
    public static boolean checkJobName(String jobName) {
        return !GeneralStringUtils.isEmptyString(jobName);
    }

    /**
     * @param jobRevision
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkJobRevision(String jobRevision) {
        if (!GeneralStringUtils.isEmptyString(jobRevision)) {
            //if the release is not empty check the release format...
            if (jobRevision.length() != 2) {
                return false;
            } else {
                try {
                    Short.parseShort(jobRevision);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param line
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkLine(String line) {
        return !GeneralStringUtils.isEmptyString(line);
    }

    /**
     * @param facility
     * @param plantId
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkFacility(String facility, String plantId) {
        if (!GeneralStringUtils.isEmptyString(facility) && !GeneralStringUtils.isEmptyString(plantId)) {
            Iterator facilityList = FacilityMgr.getFacilityListFast(plantId).listIterator();
            while (facilityList.hasNext()) {
                if (facility.equals(facilityList.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param path
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkPath(String path) {
        if (!GeneralStringUtils.isEmptyTrimmedString(path)) {
            File f = new File(path);
            return f.exists();
        }
        return false;
    }

    /**
     * @param testerInfo
     * @param plantId
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkTesterInfo(String testerInfo, String plantId) {
        if (!GeneralStringUtils.isEmptyString(testerInfo) && !GeneralStringUtils.isEmptyString(plantId)) {
            try {
                Element el = TesterInfoMgr.findTesterInfoData(plantId, testerInfo);
                if (el != null){
                    String testerInfoShow = XmlUtils.getTextValue(el, "TESTER_INFO_SHOW");
                    if (!GeneralStringUtils.isEmptyString(testerInfoShow) && testerInfoShow.equals(testerInfo)) {
                        return true;
                    }
                }
            } catch (TpmsException e) {
                errorLog("TpDataValidator :: checkTesterInfo : TpmsException " + e.getMessage(), e);
            }
        }
        return false;
    }

    /**
     * @param validLogin
     * @param plantId
     * @param plantId
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkValidLogin(String validLogin, String plantId) {
        return checkLogin(validLogin, plantId);
    }

    /**
     * @param prodLogin
     * @param plantId
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkProdLogin(String prodLogin, String plantId) {
        return checkLogin(prodLogin, plantId);
    }


    private static boolean checkLogin(String login, String plantId) {
        boolean result = false;
        if (!GeneralStringUtils.isEmptyString(login) && !GeneralStringUtils.isEmptyString(plantId)) {
            String query = "select count(*) as founded " +
                    "from users " +
                    "where installation_id = " + QueryUtils.getStringValueForQuery(plantId) + " and tpms_login = " + QueryUtils.getStringValueForQuery(login);
            ResultSet usersRs = null;
            try {

                usersRs = executeSelectQuery(query);
                if (usersRs != null && usersRs.next() && usersRs.getLong("founded") > 0) {
                    return true;
                }
            } catch (TpmsException e) {
                errorLog("TpDataValidator :: checkToMail( " + login + " , " + plantId + " ) TpmsException : unable to retrieve data : " + e.getMessage(), e);
                result = false;
            } catch (SQLException e) {
                errorLog("TpDataValidator :: checkToMail( " + login + " , " + plantId + " ) SQLException : unable to retrieve data : " + e.getMessage(), e);
                result = false;
            } finally {
                if (usersRs != null) {
                    try {
                        usersRs.close();
                    } catch (SQLException e) {
                        errorLog("TpDataValidator :: checkToMail( " + login + " , " + plantId + " ) TpmsException : unable to retrieve data : " + e.getMessage(), e);
                    }
                }
            }


        } else {
            //in this case we enable the possibility for the caller to left valid and prod login empty so
            result = true;
        }
        return result;
    }


    /**
     * @param toMail
     * @param plantId
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkToMail(String toMail, String plantId) {
        boolean result = false;
        if (!GeneralStringUtils.isEmptyString(toMail) && !GeneralStringUtils.isEmptyString(plantId)) {
            String query = "select count(*) as founded " +
                    "from users " +
                    "where installation_id = " + QueryUtils.getStringValueForQuery(toMail) + " and email = " + QueryUtils.getStringValueForQuery(plantId);
            ResultSet emailRs = null;
            try {

                emailRs = executeSelectQuery(query);
                if (emailRs != null && emailRs.next() && emailRs.getLong("founded") > 0) {
                    return true;
                }
            } catch (TpmsException e) {
                errorLog("TpDataValidator :: checkToMail( " + toMail + " , " + plantId + " ) TpmsException : unable to retrieve data : " + e.getMessage(), e);
                result = false;
            } catch (SQLException e) {
                errorLog("TpDataValidator :: checkToMail( " + toMail + " , " + plantId + " ) SQLException : unable to retrieve data : " + e.getMessage(), e);
                result = false;
            } finally {
                if (emailRs != null) {
                    try {
                        emailRs.close();
                    } catch (SQLException e) {
                        errorLog("TpDataValidator :: checkToMail( " + toMail + " , " + plantId + " ) TpmsException : unable to retrieve data : " + e.getMessage(), e);
                    }
                }
            }


        }
        return result;
    }

    /**
     * @param owner
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkOwner(String owner) {
        return !GeneralStringUtils.isEmptyString(owner);
    }
    /**
     * @param email
     * @return return true if the parameter is valid, false otherwise
     */
    public static boolean checkFromEmail(String email) {
        return !GeneralStringUtils.isEmptyString(email);
    }

}
