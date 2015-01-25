package it.txt.tpms.lineset.utils;

import it.txt.tpms.lineset.Lineset;
import it.txt.tpms.users.TpmsUser;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 25-gen-2007
 * Time: 17.00.59
 */
public class LinesetUtils {

    public static boolean userCanShareLineset( Lineset ls, TpmsUser tpmsUser){
        //todo completare!
        if (tpmsUser != null && ls != null && ls.getOriginalOwnerUnixLogin().equals( tpmsUser.getUnixUser() )){
            return true;
        }
        return false;
    }
}
