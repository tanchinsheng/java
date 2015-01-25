package tpms.utils;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.afs.security.AfsSecurityManager;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.users.manager.TpmsUsersManager;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tpms.CtrlServlet;
import tpms.TpmsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 24-gen-2006
 * Time: 13.38.56
 */
public class UserUtils extends AfsCommonStaticClass {

    private static Element adminData = null;

    private static Element usersDataList = null;

    public static final String LDAP_LOGIN_ELEMENT_TAG = "LDAP_LOGIN";


    static {
        initAdminData();
        initUsersData();
    }


    protected static void initAdminData() {
        try {
            adminData = CtrlServlet.getAdminData();
        } catch (Exception e) {
            errorLog("UserUtils :: initAdminData " + e.getMessage(), e);
        }
    }

    protected static void initUsersData() {
        try {
            usersDataList = CtrlServlet.getAccntsRoot();
        } catch (Exception e) {
            errorLog("UserUtils :: initUsersData " + e.getMessage(), e);
        }

        //just for debug purposes....
        /*try {
            usersDataList = XmlUtils.getRoot("D:\\jakarta-tomcat-3.2.3\\webapps\\tpms51\\cfg\\local_cfg\\accounts.xml");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
    }

    /**
     * Return a vector containing the list of tp_plant.line values for the given userLogin
     * @param userLogin
     * @return a vector containing the list of tp_plant.line values for the given userLogin
     */
    public static Vector getUserTPPlantLineValues(String userLogin) {
        Vector result = new Vector();
        if (!GeneralStringUtils.isEmptyString(userLogin)){
            String localInstallationId = tpmsConfiguration.getLocalPlant();
            String unixUser = getUserUnixLogin(userLogin);
            String query = "select line as lineValue, line as lineText " +
                    "from tp_plant " +
                    "where owner_login = " + QueryUtils.getStringValueForQuery(unixUser) + " and installation_id = " +  QueryUtils.getStringValueForQuery(localInstallationId) + " " +
                    "group by line";

            debugLog("UserUtils :: getUserTPPlantLineValues query = " + query);

            if (QueryUtils.checkCtrlServletDBConnection()) {
                ResultSet linesList = null;
                try {
                    linesList = executeSelectQuery(query);
                    if (linesList != null) {
                        result.addAll(VectorUtils.dumpResultSetToVectorOfStrArray(linesList));
                    }
                } catch (TpmsException e) {
                    String[] msg = new String [1];
                    msg[0] = "Error while retrienving user (" + userLogin + ") line informations";
                    errorLog("UserUtils :: getUserTPPlantLineValues: " + msg[0], e);
                    result.add(msg);
                } finally {
                    if (linesList != null){
                        try {
                            linesList.close();
                        } catch (SQLException e) {
                            String[] msg = new String [1];
                            msg[0] = "Error while closing user (" + userLogin + ") lines informations";
                            errorLog("UserUtils :: getUserTPPlantLineValues: " + msg[0], e);
                            result.add(msg);
                        }
                    }
                }
            } else {
                String[] msg = new String [1];
                msg[0] = "Unable to connect to DB, contact the adminisrator";
                result.add(msg[0]);
            }
        }
        return result;
    }



    public static String getAdminUnixLogin() {
        String adminUnixLogin = "";
        initAdminData();
        try {
            adminUnixLogin = XmlUtils.getVal(adminData, "UNIX_USER");
        } catch (Exception e) {
            errorLog("UserUtils :: getAdminUnixLogin " + e.getMessage(), e);
        }

        return adminUnixLogin;
    }

    public static boolean isAccountDuplicated(String tpmsLogin) {
        return isAccountDuplicated(tpmsLogin, null);
    }

    public static boolean isAccountDuplicated(String tpmsLogin, String ldapLogin) {
        return existsUserByLdapLogin(ldapLogin) || existsUserByTpmsLogin(tpmsLogin);
    }

    public static boolean existsUserByTpmsLogin(String tpmsLogin) {
        Element localUsersDataList = CtrlServlet.getAccntsRoot();
        NodeList usersList = localUsersDataList.getElementsByTagName("USER");
        Element oneUserData;
        String currentTpmsLogin;
        for (int i = 0; i < usersList.getLength(); i++) {
            oneUserData = (Element) usersList.item(i);
            currentTpmsLogin = XmlUtils.getVal(oneUserData, "NAME");
            if (!GeneralStringUtils.isEmptyString(currentTpmsLogin) && currentTpmsLogin.equals(tpmsLogin))
                return true;
        }
        return false;
    }

    public static boolean existsUserByLdapLogin(String ldapLogin) {
        return !GeneralStringUtils.isEmptyString(getTpmsLoginFromLdapLogin(ldapLogin, true));
    }

    public static boolean hasAfsRole(String userLogin) {
        return getUserRole(userLogin).equals(AfsSecurityManager.AIDED_FTP_SERVICE_ROLE);
    }

    public static boolean hasEngineerRole(String userLogin) {
        return getUserRole(userLogin).equals(AfsSecurityManager.ENGINEER_ROLE);
    }

    public static boolean hasQueryUserRole(String userLogin) {
        return getUserRole(userLogin).equals(AfsSecurityManager.QUERY_USER_ROLE);
    }

    public static String getTpmsLoginFromLdapLogin(String ldapLogin) {
        return getTpmsLoginFromLdapLogin(ldapLogin, false);
    }


    /**
     * given an Ldap login return the tpms login (i.e. unix login), null if an error eccours
     *
     * @param ldapLogin      to look for
     * @param reloadUserData if this parameter is true the user data (accouts.xml) will be reloaded from the file system
     * @return given an Ldap login return the tpms login (i.e. unix login), null if an error eccours
     */
    public static String getTpmsLoginFromLdapLogin(String ldapLogin, boolean reloadUserData) {

        if (usersDataList == null || reloadUserData) {
            initUsersData();
        }

        NodeList usersList = usersDataList.getElementsByTagName("USER");
        Element oneUserData;
        String currentLdapLogin;
        int usersCount = usersList.getLength();
        for (int i = 0; i < usersCount; i++) {
            oneUserData = (Element) usersList.item(i);
            currentLdapLogin = XmlUtils.getVal(oneUserData, LDAP_LOGIN_ELEMENT_TAG);
            if (!GeneralStringUtils.isEmptyString(currentLdapLogin) && currentLdapLogin.equals(ldapLogin))
                return XmlUtils.getVal(oneUserData, "NAME");
        }
        return null;
    }

    public static String getWorkDirectory(String userLogin) {
        String workDirectory = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                workDirectory = XmlUtils.getVal(userData, "WORK_DIR");
            } catch (Exception e) {
                errorLog("UserUtils :: getWorkDirectory " + e.getMessage(), e);
            }
        }
        return workDirectory;
    }

    public static String getHomeDirectory(String userLogin) {
        String homeDirectory = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                homeDirectory = XmlUtils.getVal(userData, "HOME_DIR");
            } catch (Exception e) {
                errorLog("UserUtils :: getHomeDirectory " + e.getMessage(), e);
            }
        }

        return homeDirectory;
    }


    public static String getUserEmailAddress(String userLogin) {
        String email = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                email = XmlUtils.getVal(userData, "EMAIL");
            } catch (Exception e) {
                errorLog("UserUtils :: getUserEmailAddress " + e.getMessage(), e);
            }
        }
        return email;
    }

    public static String getUserRole(String userLogin) {
        String role = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                role = XmlUtils.getVal(userData, "ROLE");
            } catch (Exception e) {
                errorLog("UserUtils :: getUserRole " + e.getMessage(), e);
            }
        }
        return role;
    }

    public static String getUserDVob(String userLogin) {
        String role = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                role = XmlUtils.getVal(userData, "DVL_VOB");
            } catch (Exception e) {
                errorLog("UserUtils :: getUserDVob " + e.getMessage(), e);
            }
        }
        return role;
    }

    public static String getUserUnixHome(String userLogin) {
        String role = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                role = XmlUtils.getVal(userData, "HOME_DIR");
            } catch (Exception e) {
                errorLog("UserUtils :: getUserUnixHome " + e.getMessage(), e);
            }
        }
        return role;
    }

    public static String getUserUnixGroup(String userLogin) {
        String unixGroup = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                unixGroup = XmlUtils.getVal(userData, "UNIX_GROUP");
            } catch (Exception e) {
                errorLog("UserUtils :: getUserUnixGroup " + e.getMessage(), e);
            }
        }
        return unixGroup;
    }

    public static String getUserRVob(String userLogin) {
        String role = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                role = XmlUtils.getVal(userData, "REC_VOB");
            } catch (Exception e) {
                errorLog("UserUtils :: getUserRVob " + e.getMessage(), e);
            }
        }
        return role;
    }

    public static String getUserUnixLogin(String userLogin) {
        String role = "";
        if (!GeneralStringUtils.isEmptyString(userLogin)) {
            try {
                Element userData = CtrlServlet.getUserData(userLogin);
                role = XmlUtils.getVal(userData, "UNIX_USER");
            } catch (Exception e) {
                errorLog("UserUtils :: getUserUnixLogin " + e.getMessage(), e);
            }
        }
        return role;
    }



    public static ArrayList getEngineerUnixLoginsByDivision(String division){
        return getEngineerUnixLoginsByDivision( division, null );
    }

    public static ArrayList getEngineerUnixLoginsByDivision(String division, ArrayList exceptList){

        //se usi hashset al posto e poi instanzi array list ottineni che la lista non presenta duplicati...
        //controindicazione: devi ordinare l'hashset prima di istanziare l'array list perchè il tipo di dati non garantisce l'ordinamento.
        ArrayList result = new ArrayList( );
        if ( !GeneralStringUtils.isEmptyString( division ) ) {
            boolean manageExcept = (exceptList != null && exceptList.size() > 0);
            if (usersDataList == null){
                initUsersData();
            }
            if (usersDataList != null) {
                NodeList users = usersDataList.getElementsByTagName( "USER");
                if (users != null){
                    int usersCount = users.getLength();
                    Element oneUserData;
                    String currentUserUnixLogin;
                    for (int i = 0; i < usersCount; i++) {
                        oneUserData = (Element) users.item( i );
                        if ( TpmsUsersManager.ENGINEER_ROLE.equals( XmlUtils.getTextValue(oneUserData, "ROLE") ) && division.equalsIgnoreCase( XmlUtils.getTextValue(oneUserData, "DIVISION") )) {
                            currentUserUnixLogin = XmlUtils.getTextValue(oneUserData, "UNIX_USER");
                            if (manageExcept){
                                if ( exceptList.indexOf( currentUserUnixLogin ) < 0 ) {
                                    result.add( XmlUtils.getTextValue(oneUserData, "UNIX_USER") );
                                }
                            } else {
                                result.add( currentUserUnixLogin );
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}
