package it.txt.tpms.users.manager;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.users.TpmsUser;
import it.txt.tpms.users.list.TpmsUsersList;
import org.w3c.dom.Element;
import tpms.CtrlServlet;
import tpms.TpmsException;
import tpms.utils.QueryUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 10-ott-2006
 * Time: 13.24.11
 */
public class TpmsUsersManager {

    public static TpmsUser getLocalTpmsUserByTpmsLogin ( String tpmsLogin ) {
        TpmsUser tpmsUser = null;
        if ( !GeneralStringUtils.isEmptyString( tpmsLogin ) ) {
            Element userData = null;
            try {
                userData = CtrlServlet.getUserData( tpmsLogin );
            } catch ( Exception e ) {
                AfsCommonStaticClass.errorLog( "UserUtils :: getTpmsUserByLogin " + e.getMessage(), e );
            }

            if ( userData != null ) {
                tpmsUser = new TpmsUser();
                tpmsUser.setName( tpmsLogin );
                tpmsUser.setLdapLogin( XmlUtils.getVal( userData, "LDAP_LOGIN" ) );
                tpmsUser.setUnixUser( XmlUtils.getVal( userData, "UNIX_USER" ) );
                tpmsUser.setUnixGroup( XmlUtils.getVal( userData, "UNIX_GROUP" ) );
                tpmsUser.setFirstName( XmlUtils.getVal( userData, "FIRST_NAME" ) );
                tpmsUser.setSurname( XmlUtils.getVal( userData, "SURNAME" ) );
                tpmsUser.setEmail( XmlUtils.getVal( userData, "EMAIL" ) );
                tpmsUser.setDivision( XmlUtils.getVal( userData, "DIVISION" ) );
                tpmsUser.setCriptedPassword( XmlUtils.getVal( userData, "PWD" ) );
                tpmsUser.setHomeDirectory( XmlUtils.getVal( userData, "HOME_DIR" ) );
                tpmsUser.setWorkDirectory( XmlUtils.getVal( userData, "WORK_DIR" ) );
                tpmsUser.setRole( XmlUtils.getVal( userData, "ROLE" ) );
                tpmsUser.setWorkMode( XmlUtils.getVal( userData, "WORK_MODE" ) );
                tpmsUser.setDefaultDevelopmentVobName( XmlUtils.getVal( userData, "DVL_VOB" ) );
                tpmsUser.setDefaultReceivingVobName( XmlUtils.getVal( userData, "REC_VOB" ) );
            }
        }
        return tpmsUser;
    }

    public static TpmsUsersList loadPlantUsers ( String plantId ) {
        TpmsUsersList tpmsUsersList = new TpmsUsersList();
        if ( !GeneralStringUtils.isEmptyString( plantId ) ) {
            String query = "select INSTALLATION_ID, FIRST_NAME, SURNAME, TPMS_ROLE, TPMS_LOGIN, EMAIL, UNIX_LOGIN, UNIX_HOME, WORK_DIR, DIVISION " +
                    "from users " +
                    "where installation_id = " + QueryUtils.getStringValueForQuery( plantId ) + " " +
                    "order by TPMS_LOGIN";

            CachedRowSet rsUsers = null;
            try {

                rsUsers = AfsCommonStaticClass.executeSelectCacheQuery( query );
                TpmsUser tmpTpmsUser;
                while ( rsUsers.next() ) {

                    tmpTpmsUser = new TpmsUser();
                    tmpTpmsUser.setName( rsUsers.getString( "TPMS_LOGIN" ) );
                    tmpTpmsUser.setDivision( rsUsers.getString( "DIVISION" ) );
                    tmpTpmsUser.setEmail( rsUsers.getString( "EMAIL" ) );
                    tmpTpmsUser.setFirstName( rsUsers.getString( "FIRST_NAME" ) );
                    tmpTpmsUser.setHomeDirectory( rsUsers.getString( "UNIX_HOME" ) );
                    tmpTpmsUser.setName( rsUsers.getString( "TPMS_LOGIN" ) );
                    tmpTpmsUser.setRole( rsUsers.getString( "TPMS_ROLE" ) );
                    tmpTpmsUser.setSurname( rsUsers.getString( "SURNAME" ) );
                    tmpTpmsUser.setUnixGroup( rsUsers.getString( "DIVISION" ) );
                    tmpTpmsUser.setUnixUser( rsUsers.getString( "UNIX_LOGIN" ) );
                    tmpTpmsUser.setWorkDirectory( rsUsers.getString( "WORK_DIR" ) );
                    tmpTpmsUser.setInstallationId( "INSTALLATION_ID" );

                    tpmsUsersList.addElement( tmpTpmsUser );
                }
            } catch ( TpmsException e ) {
                AfsCommonStaticClass.errorLog( "TpmsUsersManager :: loadPlantUsers TpmsException : " + e.getMessage(), e );
                return tpmsUsersList;
            } catch ( SQLException e ) {
                AfsCommonStaticClass.errorLog( "TpmsUsersManager :: loadPlantUsers SQLException : " + e.getMessage(), e );
                return tpmsUsersList;
            } finally {
                if ( rsUsers != null ) {
                    try {
                        rsUsers.close();
                    } catch ( SQLException e ) {
                        AfsCommonStaticClass.errorLog( "TpmsUsersManager :: loadPlantUsers SQLException closing users resultset: " + e.getMessage(), e );
                    }
                }
            }
        }
        return tpmsUsersList;

    }


    public static final String ADMIN_ROLE = "ADMIN";
    public static final String AIDED_FTP_SERVICE_ROLE = "AIDED_FTP";
    public static final String CONTROLLER_ROLE = "CONTROLLER";
    public static final String ENGINEER_ROLE = "ENGINEER";
    public static final String QUERY_USER_ROLE = "QUERY_USER";





    public static TpmsUsersList usersListByUsersByRole ( String plantId, String userRole ) {
        TpmsUsersList tpmsUsersList = new TpmsUsersList();
        if ( !GeneralStringUtils.isEmptyString( plantId ) && !GeneralStringUtils.isEmptyString( userRole )) {
            String query = "select INSTALLATION_ID, FIRST_NAME, SURNAME, TPMS_ROLE, TPMS_LOGIN, EMAIL, UNIX_LOGIN, UNIX_HOME, WORK_DIR, DIVISION " +
                    "from users " +
                    "where installation_id = " + QueryUtils.getStringValueForQuery( plantId ) + " and tpms_role = " + QueryUtils.getStringValueForQuery( userRole ) + " " +
                    "order by TPMS_LOGIN";

            CachedRowSet rsUsers = null;
            try {

                rsUsers = AfsCommonStaticClass.executeSelectCacheQuery( query );
                TpmsUser tmpTpmsUser;
                while ( rsUsers.next() ) {

                    tmpTpmsUser = new TpmsUser();
                    tmpTpmsUser.setName( rsUsers.getString( "TPMS_LOGIN" ) );
                    tmpTpmsUser.setDivision( rsUsers.getString( "DIVISION" ) );
                    tmpTpmsUser.setEmail( rsUsers.getString( "EMAIL" ) );
                    tmpTpmsUser.setFirstName( rsUsers.getString( "FIRST_NAME" ) );
                    tmpTpmsUser.setHomeDirectory( rsUsers.getString( "UNIX_HOME" ) );
                    tmpTpmsUser.setName( rsUsers.getString( "TPMS_LOGIN" ) );
                    tmpTpmsUser.setRole( rsUsers.getString( "TPMS_ROLE" ) );
                    tmpTpmsUser.setSurname( rsUsers.getString( "SURNAME" ) );
                    tmpTpmsUser.setUnixGroup( rsUsers.getString( "DIVISION" ) );
                    tmpTpmsUser.setUnixUser( rsUsers.getString( "UNIX_LOGIN" ) );
                    tmpTpmsUser.setWorkDirectory( rsUsers.getString( "WORK_DIR" ) );
                    tmpTpmsUser.setInstallationId( "INSTALLATION_ID" );

                    tpmsUsersList.addElement( tmpTpmsUser );
                }
            } catch ( TpmsException e ) {
                AfsCommonStaticClass.errorLog( "TpmsUsersManager :: loadPlantUsers TpmsException : " + e.getMessage(), e );
                return tpmsUsersList;
            } catch ( SQLException e ) {
                AfsCommonStaticClass.errorLog( "TpmsUsersManager :: loadPlantUsers SQLException : " + e.getMessage(), e );
                return tpmsUsersList;
            } finally {
                if ( rsUsers != null ) {
                    try {
                        rsUsers.close();
                    } catch ( SQLException e ) {
                        AfsCommonStaticClass.errorLog( "TpmsUsersManager :: loadPlantUsers SQLException closing users resultset: " + e.getMessage(), e );
                    }
                }
            }
        }
        return tpmsUsersList;

    }





    public static Vector getUsersPlants () {
        String query = "select installation_id as id, installation_id as plant " +
                "from users " +
                "group by installation_id";

        CachedRowSet rs = null;
        Vector result = new Vector();
        try {
            rs = AfsCommonStaticClass.executeSelectCacheQuery( query );
            result = VectorUtils.dumpResultSetToVectorOfStrArray( rs, true );
        } catch ( TpmsException e ) {
            AfsCommonStaticClass.errorLog( "TpmsUsersManager :: getUsersPlants TpmsException : " + e.getMessage(), e );
        } finally {
            if ( rs != null ) {
                try {
                    rs.close();
                } catch ( SQLException e ) {
                    AfsCommonStaticClass.errorLog( "TpmsUsersManager :: getUsersPlants SQLException closing users resultset: " + e.getMessage(), e );
                }
            }
        }

        return result;


    }


}
