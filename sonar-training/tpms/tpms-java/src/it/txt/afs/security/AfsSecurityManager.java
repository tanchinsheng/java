package it.txt.afs.security;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.users.manager.TpmsUsersManager;
import tpms.TpmsException;
import tpms.utils.UserUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 24-gen-2006
 * Time: 12.22.53
 */
public class AfsSecurityManager {

    //creazione package..
    public static final String VIEW_CREATE_PACKET = "createPacket";
    //user roles...
    public static final String AIDED_FTP_SERVICE_ROLE = TpmsUsersManager.AIDED_FTP_SERVICE_ROLE;
    public static final String ENGINEER_ROLE = TpmsUsersManager.ENGINEER_ROLE;
    public static final String QUERY_USER_ROLE = TpmsUsersManager.QUERY_USER_ROLE;

    private static final String SESSION_CURRENT_USER_LOGIN = "user";

    /**
     * retrieve the current user login.
     * @param request
     * @return  the current user login.
     */
    private static String getCurrentUserLogin(HttpServletRequest request) {
        return (String) request.getSession().getAttribute(SESSION_CURRENT_USER_LOGIN);
    }


    /**
     *
     * @param request
     * @return true if the current user is authenticathed false otherwise
     */
    public static boolean isAuthenticated(HttpServletRequest request){
        return !GeneralStringUtils.isEmptyString(getCurrentUserLogin(request));
    }

    /**
     *
     * @param request
     * @return true if the current logged in user can extract packets
     * @throws TpmsException if the current user can't extract packet ..
     */
    public static boolean canExtractPacket(HttpServletRequest request) throws TpmsException {
        if (!isAuthenticated(request)){
            throw new TpmsException("You are not authorized to create packets");
        }
        String userLogin = getCurrentUserLogin(request);
        String userRole = UserUtils.getUserRole(userLogin);
        if (userRole.equals(AIDED_FTP_SERVICE_ROLE) || userRole.equals(ENGINEER_ROLE)) {
            return true;
        }
        throw new TpmsException("You are not authorized to access this functionality!");

    }

    /**
     *
     * @param request
     * @return true if the current logged in user can send packets
     * @throws TpmsException if the current user can't send packet ..
     */
    public static boolean canSendPacket(HttpServletRequest request) throws TpmsException {
        if (!isAuthenticated(request)){
            throw new TpmsException("You are not authorized to create packets");
        }
        String userLogin = getCurrentUserLogin(request);
        String userRole = UserUtils.getUserRole(userLogin);
        if (userRole.equals(AIDED_FTP_SERVICE_ROLE)) {
            return true;
        }
        throw new TpmsException("You are not authorized to access this functionality!");
    }

    /**
     *
     * @param request
     * @return true if the current logged in user can view owned packets
     * @throws TpmsException if the current user can't view owned packet ..
     */
    public static boolean canViewOwnedPacket(HttpServletRequest request) throws TpmsException {
        if (!isAuthenticated(request)){
            throw new TpmsException("You are not authorized to view packets");
        }
        String userLogin = getCurrentUserLogin(request);
        String userRole = UserUtils.getUserRole(userLogin);
        if (userRole.equals(AIDED_FTP_SERVICE_ROLE)) {
            return true;
        }
        throw new TpmsException("You are not authorized to access this functionality!");
    }

    /**
     *
     * @param request
     * @return true if the current logged in user can view recieved packets
     * @throws TpmsException if the current user can't view recieved packet ..
     */
    public static boolean canViewRecievedPacket(HttpServletRequest request) throws TpmsException {
        if (!isAuthenticated(request)){
            throw new TpmsException("You are not logged in, can't access this functionality");
        }
        String userLogin = getCurrentUserLogin(request);
        String userRole = UserUtils.getUserRole(userLogin);
        if (userRole.equals(AIDED_FTP_SERVICE_ROLE) || userRole.equals(ENGINEER_ROLE)) {
            return true;
        }
        throw new TpmsException("You are not authorized to access this functionality!");
    }

}

