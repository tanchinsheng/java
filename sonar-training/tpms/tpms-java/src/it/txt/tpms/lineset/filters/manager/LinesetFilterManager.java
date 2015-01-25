package it.txt.tpms.lineset.filters.manager;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.tpms.lineset.Lineset;
import it.txt.tpms.lineset.filters.LinesetFilter;
import it.txt.tpms.lineset.filters.list.LinesetFilterList;
import it.txt.tpms.lineset.list.LinesetList;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.utils.QueryUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 4-dic-2006
 * Time: 15.35.27
 */
public class LinesetFilterManager {

    /**
     * This method creates a new lineset filter owned by given user associated to the given linesets
     *
     * @param lsList                    the lis of ls that will be associated with the new filter
     * @param user                      the user that will own  the new filter
     * @param linesetFilterDisplayValue the display value for the new filter
     * @param sessionId                 needed to track db query
     */
    public static void createFilter ( LinesetList lsList, TpmsUser user, String linesetFilterDisplayValue, String sessionId ) {
        String commonErrorMessage = "LinesetFilterManager :: createFilter";
        if ( lsList != null && user != null && !GeneralStringUtils.isEmptyString( linesetFilterDisplayValue ) && !GeneralStringUtils.isEmptyString( sessionId ) ) {
            AfsCommonStaticClass.debugLog( commonErrorMessage + "let's start..." + linesetFilterDisplayValue );
            lsList.rewind();
            Lineset oneLineset;
            String query;
            while ( lsList.hasNext() ) {
                oneLineset = lsList.next();
                query = "insert into TPMS_LINESET_FILTERS (FILTER_ID, TPMS_LOGIN, FILTER_DISPLAY_VALUE, INSTALLATION_ID, LINESET_NAME, VOB_NAME, OWNER_INSTALLATION_ID) values " +
                        "(LINESET_FILTER_ID_GENERATOR.nextval, " +
                        QueryUtils.getStringValueForQuery( user.getName() ) + ", " +
                        QueryUtils.getStringValueForQuery( linesetFilterDisplayValue ) + ", " +
                        QueryUtils.getStringValueForQuery( oneLineset.getPlant() ) + ", " +
                        QueryUtils.getStringValueForQuery( oneLineset.getName() ) + ", " +
                        QueryUtils.getStringValueForQuery( oneLineset.getVobName() ) + ", " +
                        QueryUtils.getStringValueForQuery( user.getInstallationId() ) + ")";
                AfsCommonStaticClass.debugLog( commonErrorMessage + " query = " + query );
                AfsCommonStaticClass.executeInsertQuery( query, sessionId, user.getName() );
            }
        }
    }


    public static void associateFilter ( LinesetList lsList, TpmsUser user, String linesetFilterDisplayValue, String sessionId ) {
        linesetFilterDisplayValue = linesetFilterDisplayValue.trim();
        if ( lsList != null && lsList.size() > 0 && user != null && !GeneralStringUtils.isEmptyString( linesetFilterDisplayValue ) ) {
            //TpmsUser user, LinesetFilter linesetFilter, LinesetList lsList,String sessionId
            removeFilterFromLinesets( user, linesetFilterDisplayValue, lsList, sessionId );
            createFilter( lsList, user, linesetFilterDisplayValue, sessionId );

        }

    }

    public static boolean removeFilterFromLinesets ( TpmsUser user, String linesetFilterDisplayValue, LinesetList lsList, String sessionId ) {
        if ( user != null && !GeneralStringUtils.isEmptyString( linesetFilterDisplayValue ) && lsList != null && lsList.size() > 0 ) {
            String commonErrorMessage = "LinesetFilterManager :: removeFilterFromLinesets";
            String whereCondition = "";
            lsList.rewind();
            Lineset oneLs;
            while ( lsList.hasNext() ) {
                oneLs = lsList.next();
                if ( GeneralStringUtils.isEmptyString( whereCondition ) ) {
                    whereCondition = "(tpms_login = " + QueryUtils.getStringValueForQuery( user.getTpmsLogin() ) + " and owner_installation_id = " + QueryUtils.getStringValueForQuery( user.getInstallationId() ) +
                            " and INSTALLATION_ID = " + QueryUtils.getStringValueForQuery( oneLs.getPlant() ) + " and LINESET_NAME = " + QueryUtils.getStringValueForQuery( oneLs.getName() ) +
                            " and VOB_NAME = " + QueryUtils.getStringValueForQuery( oneLs.getVobName() ) + " and filter_display_value = " + QueryUtils.getStringValueForQuery( linesetFilterDisplayValue ) + ")";
                } else {
                    whereCondition = whereCondition + " or " +
                            "(tpms_login = " + QueryUtils.getStringValueForQuery( user.getTpmsLogin() ) + " and owner_installation_id = " + QueryUtils.getStringValueForQuery( user.getInstallationId() ) +
                            " and INSTALLATION_ID = " + QueryUtils.getStringValueForQuery( oneLs.getPlant() ) + " and LINESET_NAME = " + QueryUtils.getStringValueForQuery( oneLs.getName() ) +
                            " and VOB_NAME = " + QueryUtils.getStringValueForQuery( oneLs.getVobName() ) + " and filter_display_value = " + QueryUtils.getStringValueForQuery( linesetFilterDisplayValue ) + ")";
                }
            }
            String query = "delete tpms_lineset_filters where " + whereCondition;
            AfsCommonStaticClass.debugLog( commonErrorMessage + " : query = " + query );
            return AfsCommonStaticClass.executeDeleteQuery( query, sessionId, user.getTpmsLogin() );
        }
        return false;
    }


    /**
     * this method all remove filters contained in linesetFilterList associated to the given user
     *
     * @param user              that owns the filter
     * @param linesetFilterList the list of linesets where the filter will be removed
     * @param sessionId         used for lost query db tracking
     *
     * @return true if the operation succedes, false otherwise
     *
     * @deprecated se non gestisci owner_installation_id
     */
    public static boolean removeFilters ( TpmsUser user, LinesetFilterList linesetFilterList, String sessionId ) {
        if ( user != null && linesetFilterList != null && linesetFilterList.size() > 0 ) {
            String inClauseValues = null;
            linesetFilterList.rewind();
            String oneFilterId;
            while ( linesetFilterList.hasNext() ) {
                oneFilterId = QueryUtils.getStringValueForQuery( linesetFilterList.next().getId() );
                if ( GeneralStringUtils.isEmptyString( inClauseValues ) ) {
                    inClauseValues = QueryUtils.getStringValueForQuery( oneFilterId );
                } else {
                    inClauseValues = ", " + QueryUtils.getStringValueForQuery( oneFilterId );
                }
            }

            String query = "delete from tpms_lineset_filters where filter_id in (" + inClauseValues + ") and tpms_login = " + QueryUtils.getStringValueForQuery( user.getTpmsLogin() );

            return AfsCommonStaticClass.executeDeleteQuery( query, sessionId, user.getTpmsLogin() );
        }
        return false;
    }


    /**
     * removes from DB the given linesetFilter, verifing the ownership of the filter.
     *
     * @param user          for lost queries management
     * @param linesetFilter the fiulter to be deleted
     * @param sessionId     for lost queries management
     *
     * @return true if the execution of the query succeded, false otherwise
     *
     * @deprecated se non gestisci owner_installation_id
     */
    public static boolean removeFilter ( TpmsUser user, LinesetFilter linesetFilter, String sessionId ) {
        if ( linesetFilter != null && user != null && GeneralStringUtils.isEmptyString( sessionId ) ) {
            String query = "delete TPMS_LINESET_FILTERS " +
                    "where filter_id = " + QueryUtils.getStringValueForQuery( linesetFilter.getId() ) + " and tpms_login = " + QueryUtils.getStringValueForQuery( user.getName() );
            return AfsCommonStaticClass.executeDeleteQuery( query, sessionId, user.getName() );
        }
        return false;
    }


    /**
     * @param user used to identify lineset filters
     *
     * @return for the given user its own lineset filters list
     *
     * @throws TpmsException if an error occours
     */
    public static Vector getUserLinesetFilter ( TpmsUser user ) throws TpmsException {
        String commonErrorMessage = "LinesetFilterManager :: getUserLinesetFilter";


        String query = "select MIN(ROWNUM) as FILTER_ID, FILTER_DISPLAY_VALUE " +
                "from TPMS_LINESET_FILTERS " +
                "where TPMS_LOGIN = " + QueryUtils.getStringValueForQuery( user.getName() ) + " AND " +
                "OWNER_INSTALLATION_ID = " + QueryUtils.getStringValueForQuery( user.getInstallationId() ) + " " +
                "group by FILTER_DISPLAY_VALUE";
        AfsCommonStaticClass.debugLog( commonErrorMessage + " : query = " + query );
        ResultSet rs = AfsCommonStaticClass.executeSelectQuery( query );
        Vector result = VectorUtils.dumpResultSetToVectorOfStrArray( rs, true );
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException e ) {
                AfsCommonStaticClass.errorLog( commonErrorMessage + ": error while closing resultset " + e.getMessage(), e );
            }
        }
        return result;
    }




    /**
     * retrieves the filters associated to the given lineset owned by the given user
     *
     * @param ls   the lineset used to identify filters to be returned
     * @param user that owns filters.
     *
     * @return the filters associated to the given lineset owned by the given user
     *
     * @throws TpmsException if an error occours
     * @deprecated perchè da troppo pesante se utilizzato in una lista di lineset, rimpiazzato con la classe LinesetUserFilters
     */
    public static LinesetFilterList getLinesetFilters ( Lineset ls, TpmsUser user ) throws TpmsException {
        String commonErrorMessage = "LinesetFilterManager :: getLinesetFilters";

        LinesetFilterList lsFilterList = null;
        String query = "select FILTER_ID, TPMS_LOGIN, FILTER_DISPLAY_VALUE, INSTALLATION_ID, LINESET_NAME, VOB_NAME, OWNER_INSTALLATION_ID " +
                "from TPMS_LINESET_FILTERS " +
                "where TPMS_LOGIN = " + QueryUtils.getStringValueForQuery( user.getTpmsLogin() ) + " AND OWNER_INSTALLATION_ID = " + QueryUtils.getStringValueForQuery( user.getInstallationId() ) + " AND " +
                "INSTALLATION_ID = " + QueryUtils.getStringValueForQuery( ls.getPlant() ) + " and lineset_name = " + QueryUtils.getStringValueForQuery( ls.getName() ) + " and " +
                "vob_name = " + QueryUtils.getStringValueForQuery( ls.getVobName() );

        AfsCommonStaticClass.debugLog( commonErrorMessage + " : query = " + query );
        ResultSet rs = AfsCommonStaticClass.executeSelectQuery( query );
        if ( rs != null ) {
            lsFilterList = new LinesetFilterList();
            LinesetFilter lsFilter;
            try {
                while ( rs.next() ) {
                    lsFilter = new LinesetFilter( rs.getString( "TPMS_LOGIN" ), rs.getString( "FILTER_ID" ), rs.getString( "FILTER_DISPLAY_VALUE" ), rs.getString( "INSTALLATION_ID" ), rs.getString( "LINESET_NAME" ), rs.getString( "VOB_NAME" ), rs.getString( "OWNER_INSTALLATION_ID" ) );
                    lsFilterList.addElement( lsFilter );
                }
            } catch ( SQLException e ) {
                throw new TpmsException( "Error while retrieving user lineset filters", commonErrorMessage, e );
            } finally {
                try {
                    rs.close();
                } catch ( SQLException e ) {
                    AfsCommonStaticClass.errorLog( commonErrorMessage + ": error while tring to close resultset: " + e.getMessage(), e );
                }
            }
        }
        return lsFilterList;
    }


}
