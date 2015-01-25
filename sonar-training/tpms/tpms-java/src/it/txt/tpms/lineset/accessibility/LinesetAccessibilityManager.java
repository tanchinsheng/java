package it.txt.tpms.lineset.accessibility;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.lineset.Lineset;
import tpms.utils.Vob;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 25-set-2006
 * Time: 16.53.21
 * this class is the full interface to the verify the possibility for a user to get a lineset (owned by a differen user).
 * in the future when the web interface to edit the configuration file of accessibility should be implemented in this class should be realized all methods to edit that file
 */
public class LinesetAccessibilityManager {
    private static LinesetsAccessibilityData linesetsAccessibilityData = LinesetsAccessibilityData.getInstance();


    /**
     * @param vob
     * @param lineset
     * @param userLogin
     * @return true if the user identified by userLogin can get the given lineset  contained in the
     *         given vob, false otherwise (also in case of invalid input parameters)
     */
    public static boolean userCanViewLineset(Vob vob, Lineset lineset, String userLogin) {
        if (lineset != null) {
            return userCanViewLineset(vob, lineset.getName(), userLogin);
        } else {
            return false;
        }
    }

    /**
     * @param vob
     * @param linesetName
     * @param userLogin
     * @return true if the user identified by userLogin can get the lineset (identified by linesetName) contained in the
     *         given vob, false otherwise (also in case of invalid input parameters)
     */
    public static boolean userCanViewLineset(Vob vob, String linesetName, String userLogin) {
        if (vob != null) {
            return userCanViewLineset(vob.getName(), linesetName, userLogin);
        } else {
            return false;
        }
    }


    /**
     * @param vobName
     * @param linesetName
     * @param userLogin
     * @return true if the user identified by userLogin can get the lineset (identified by linesetName) contained in the
     *         given vob identified by vobName, false otherwise (also in case of invalid input parameters)
     */

    public static boolean userCanViewLineset(String vobName, String linesetName, String userLogin) {
        if (GeneralStringUtils.isEmptyTrimmedString(vobName)) {
            return false;
        }
        if (GeneralStringUtils.isEmptyTrimmedString(linesetName)) {
            return false;
        }
        if (GeneralStringUtils.isEmptyTrimmedString(userLogin)) {
            return false;
        }
        return linesetsAccessibilityData.userCanGetLineset(userLogin, linesetName, vobName);
    }

    /**
     * This method force the system to reload accessibility data from file system
     * use it with care it is really time consuming.
     */
    public static void reloadAccessibilityData() {
        linesetsAccessibilityData = null;
        LinesetsAccessibilityData.reloadLinesetAccessibiltyData();
        linesetsAccessibilityData = LinesetsAccessibilityData.getInstance();
    }


    /**
     * this method save the lineset accessibility data.
     * @param lsAccessData the data that should be added to the list
     */
    public static void setLinesetAccessData ( OneLinesetAccessData lsAccessData ) throws IOException{
        linesetsAccessibilityData.setLinesetAccessData( lsAccessData );
    }

    /**
     * return the lineset accessibility informations
     * @param ls in order to retrieve it's accessibility data
     * @return accessibility informations related to the given lineset.
     */
    public static OneLinesetAccessData getLinesetAccessData ( Lineset ls ) {
        //todo fare anche il metodo che si smazza l'xml del lineset??
        if ( ls != null ) {
            return linesetsAccessibilityData.getLinesetAccessData( ls.getName(), ls.getVobName() );
        }
        return null;
    }

    /**
     *
     * @param linesetName
     * @param vobName
     * @return
     */
    public static OneLinesetAccessData getLinesetAccessData( String linesetName, String vobName ) {
        return linesetsAccessibilityData.getLinesetAccessData( linesetName, vobName );
    }


}
