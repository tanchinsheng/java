package it.txt.tpms.lineset.managers;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.VectorUtils;
import it.txt.tpms.backend.results.GetLinesetOwnershipResult;
import it.txt.tpms.lineset.Lineset;
import it.txt.tpms.lineset.filters.user.LinesetUserFilters;
import it.txt.tpms.lineset.list.LinesetList;
import it.txt.tpms.lineset.packages.ReceivedLinesetPackage;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.utils.QueryUtils;
import tpms.utils.TpmsConfiguration;
import tpms.utils.Vob;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 14-nov-2006
 * Time: 14.30.14
 */
public class LinesetDBManager {


    private static TpmsConfiguration tpmsConfiguration = AfsCommonStaticClass.getTpmsConfiguration();


    /**
     * updates db with ls informations when a lineset will be loaded in the remote plant
     *
     * @param receivedLinesetPackage    that represent the package that should be loaded
     * @param destinationVob            where the package is been loaded
     * @param getLinesetOwnershipResult the object that represents the result of the action
     * @param sessionId                 the current user session id
     *
     * @return true if the update succedes false otherwhise
     *
     * @throws TpmsException if an error happens
     */
    public static boolean updateLinesetDataAfterLinesetLoadingProcess ( ReceivedLinesetPackage receivedLinesetPackage, Vob destinationVob, GetLinesetOwnershipResult getLinesetOwnershipResult, String sessionId ) throws TpmsException {
        String commonLogMessage = "LinesetDBManager :: updateLinesetDataAfterLinesetLoadingProcess";
        if ( receivedLinesetPackage == null ) {
            throw new TpmsException( "The received lineset package data are null", commonLogMessage, "The received lineset package data are null, unable to update database" );
        }
        if ( destinationVob == null ) {
            throw new TpmsException( "The destination vob is null", commonLogMessage, "The destination vob is null, unable to update database" );
        }
        if ( getLinesetOwnershipResult == null ) {
            throw new TpmsException( "The given operation result is null", commonLogMessage, "The given operation result is null, unable to update database" );
        }

        //check if the old lineset data are present...
        Lineset originalLineset = getLinesetOwnershipResult.getLineset1();
        Lineset lastLineset = getLinesetOwnershipResult.getLineset2();

        int linesetDbCount = countLs( receivedLinesetPackage.getSourcePlantId(), originalLineset.getName(), originalLineset.getVobName() );
        String query;
        //todo qui c'� il baco delka cmabiamento di ownership remoto
        if ( linesetDbCount > 0 ) {
            //vai in update
            query = "update lineset set installation_id = " + QueryUtils.getStringValueForQuery( tpmsConfiguration.getLocalPlant() ) + ", " +
                    "owner = " + QueryUtils.getStringValueForQuery( receivedLinesetPackage.getDestinationTpmsLogin() ) + ", " +
                    "vob_name = " + QueryUtils.getStringValueForQuery( destinationVob.getName() ) + ", " +
                    "baseline_last = " + lastLineset.getBaseline() + ", " +
                    "date_last= sysdate " +
                    "where installation_id = " + QueryUtils.getStringValueForQuery( receivedLinesetPackage.getSourcePlantId() ) + " and " +
                    "owner = " + QueryUtils.getStringValueForQuery( receivedLinesetPackage.getOriginalOwnerTpmsLogin() ) + " and " +
                    "vob_name = " + QueryUtils.getStringValueForQuery( receivedLinesetPackage.getSourceVobName() );

            AfsCommonStaticClass.debugLog( commonLogMessage + " : query = " + query );
            return AfsCommonStaticClass.executeUpdateQuery( query, sessionId, receivedLinesetPackage.getDestinationTpmsLogin() );

        } else {
            //vai in insert
            query = "insert into lineset values ( " + QueryUtils.getStringValueForQuery( tpmsConfiguration.getLocalPlant() ) + ", " +
                    QueryUtils.getStringValueForQuery( destinationVob.getName() ) + ", " +
                    QueryUtils.getStringValueForQuery( receivedLinesetPackage.getDestinationTpmsLogin() ) + ", " +
                    QueryUtils.getStringValueForQuery( receivedLinesetPackage.getLinesetName() ) + ", " +
                    QueryUtils.getStringValueForQuery( receivedLinesetPackage.getTesterFamily() ) + ", " +
                    QueryUtils.getStringValueForQuery( lastLineset.getSyncroDir() ) + ", " +
                    QueryUtils.getStringValueForQuery( lastLineset.getBaseline() ) + ", " +
                    " sysdate, " +
                    lastLineset.getFilesCount() + ", " +
                    lastLineset.getLinesetSizeKB() + ") ";
            AfsCommonStaticClass.debugLog( commonLogMessage + " : query = " + query );
            return AfsCommonStaticClass.executeInsertQuery( query, sessionId, receivedLinesetPackage.getDestinationTpmsLogin() );
        }
    }

    /**
     * @param ls the lineset to look for
     *
     * @return the number of occurencies of he given lineset inside db referred to the local plant
     *
     * @throws TpmsException if an error occours
     */
    public static int countLs ( Lineset ls ) throws TpmsException {
        if ( ls != null ) {
            return countLs( tpmsConfiguration.getLocalPlant(), ls.getName(), ls.getVobName() );
        }
        return -1;
    }


    /**
     * @param installationId used to look for the ls
     * @param linesetName    used to look for the ls
     * @param vobName        used to look for the ls
     *
     * @return the number of occurencies of he given lineset inside db referred to the local plant
     *
     * @throws TpmsException if an error occours
     */
    public static int countLs ( String installationId, String linesetName, String vobName ) throws TpmsException {
        String commonErrorMessage = "LinesetDBManager.countLs( " + installationId + ", " + linesetName + ", " + vobName + ")";
        String query = "select count(*) ls_count " +
                "from lineset " +
                "where installation_id = " + QueryUtils.getStringValueForQuery( installationId ) + " " +
                "and lineset_name = " + QueryUtils.getStringValueForQuery( linesetName ) + " " +
                "and vob_name = " + QueryUtils.getStringValueForQuery( vobName );
        int result = -1;
        AfsCommonStaticClass.debugLog("*************************************************");
        AfsCommonStaticClass.debugLog( commonErrorMessage + ": query = " + query );
        AfsCommonStaticClass.debugLog("*************************************************");
        ResultSet rs = AfsCommonStaticClass.executeSelectQuery( query );
        if ( rs != null ) {
            try {
                rs.next();
                result = rs.getInt( "ls_count" );
            } catch ( SQLException e ) {
                throw new TpmsException( "Unable to retrieve Lineset data", commonErrorMessage, e );
            } finally {
                try {
                    rs.close();
                } catch ( SQLException e ) {
                    AfsCommonStaticClass.errorLog( commonErrorMessage + " unable to close result set! " + e.getMessage(), e );
                }
            }
        }
        return result;
    }

    /**
     * this method perform searches on lineset table and return the list of lineset with associated lineset filters defined by the current user.
     *
     * @param user               the user needed to identify filter
     * @param linesetName        filter on lineset name
     * @param actionDateFrom     last ation date from
     * @param actionDateTo       last ation date to
     * @param plant              plant id where ls is been creted
     * @param filterDisplayValue the already defined filter
     *
     * @return a list of lineset
     *
     * @throws TpmsException if an error happens
     */
    public static LinesetList getLinesetListWithSpecificUserFilters ( TpmsUser user, String linesetName, Date actionDateFrom, Date actionDateTo, String plant, String filterDisplayValue ) throws TpmsException {
        LinesetList resultingLinesetList = new LinesetList();
        String commonErrorMessage = "LinesetDBManager :: getLinesetListWithSpecificUserFilters ";
        if ( user != null ) {
            String query = "select ls.installation_id, ls.vob_name, ls.owner, ls.lineset_name, ls.tester_family, ls.file_system_dir, ls.baseline_last, TO_CHAR(ls.date_last, '" + QueryUtils.ORACLE_DATE_FORMAT + "') as date_last, decode(ls.nb_of_files,'.', 0,null,0,  to_number(ls.nb_of_files)) as nb_of_files, ls.mbytes " +
                    "from lineset ls , tpms_lineset_filters lsf " +
                    "where lsf.lineset_name (+)= ls.lineset_name and lsf.installation_id (+)= ls.installation_id and lsf.vob_name (+)= ls.vob_name";

            String additionalWhereCondition = "";

            if ( !GeneralStringUtils.isEmptyString( filterDisplayValue ) ) {
                additionalWhereCondition = additionalWhereCondition + " and lsf.FILTER_DISPLAY_VALUE = " + QueryUtils.getStringValueForQuery( filterDisplayValue );
            }

            if ( !GeneralStringUtils.isEmptyString( linesetName ) ) {
                additionalWhereCondition = additionalWhereCondition + " and upper(ls.lineset_name) like upper(" + QueryUtils.getStringValueForQuery( linesetName ) + ")";
            }

            if ( !GeneralStringUtils.isEmptyString( plant ) ) {
                additionalWhereCondition = additionalWhereCondition + " and ls.installation_id = " + QueryUtils.getStringValueForQuery( plant );
            }

            String dateCondition = QueryUtils.getDateInInterval( "ls.date_last", actionDateFrom, actionDateTo, true, true );

            if ( !GeneralStringUtils.isEmptyString( dateCondition ) ) {
                additionalWhereCondition = additionalWhereCondition + " and " + dateCondition;
            }

            if ( !GeneralStringUtils.isEmptyTrimmedString( additionalWhereCondition ) ) {
                query = query + additionalWhereCondition;
            }
            query = query + " order by ls.lineset_name";
            AfsCommonStaticClass.debugLog( commonErrorMessage + "+++++++++++++++++++++++++++++++++++" );
            AfsCommonStaticClass.debugLog( commonErrorMessage + " : query = " + query );
            AfsCommonStaticClass.debugLog( commonErrorMessage + "+++++++++++++++++++++++++++++++++++" );
            ResultSet rsFoundedLinesets = AfsCommonStaticClass.executeSelectQuery( query );

            if ( rsFoundedLinesets != null ) {
                try {
                    Lineset oneLs;
                    SimpleDateFormat dbDateFormat = QueryUtils.getDBSimpleDateFormat();
                    LinesetUserFilters linesetUserFilters = null;
                    try {
                        linesetUserFilters = new LinesetUserFilters( user );
                    } catch ( TpmsException e ) {
                        AfsCommonStaticClass.errorLog( commonErrorMessage + ": error while LinesetUserFilters lookup " + e.getMessage(), e );
                    }


                    int i = -1;
//                    String previousVobName = "";
//                    String previousInstallationId = "";
//                    String previousLinesetName = "";
                    while ( rsFoundedLinesets.next() ) {

                        try {
                            i++;                            
//                            if (!rsFoundedLinesets.isFirst() && !( previousLinesetName.equals( rsFoundedLinesets.getString("lineset_name") ) && previousVobName.equals( rsFoundedLinesets.getString("vob_name") )  && previousInstallationId.equals( rsFoundedLinesets.getString("installation_id") ) )) {
                            	AfsCommonStaticClass.debugLog( commonErrorMessage + " :§§ iniziato " + i + " ls name = " + rsFoundedLinesets.getString( "lineset_name" ) );
                                oneLs = new Lineset( rsFoundedLinesets.getString( "lineset_name" ), rsFoundedLinesets.getString( "vob_name" ), rsFoundedLinesets.getString( "owner" ), rsFoundedLinesets.getString( "file_system_dir" ), rsFoundedLinesets.getString( "tester_family" ), rsFoundedLinesets.getString( "baseline_last" ), rsFoundedLinesets.getString( "installation_id" ) );   //TO_CHAR(SENT_DATE, '" + ORACLE_DATE_FORMAT + "') as SENT_DATE
                                oneLs.setFilesCount( rsFoundedLinesets.getInt( "nb_of_files" ) );
                                //oneLs.setLinesetSizeKB( rsFoundedLinesets.getString( "mbytes" ) );
                                oneLs.setLinesetSizeKB( -2 );
                                oneLs.setLastActionDate( dbDateFormat.parse( rsFoundedLinesets.getString( "date_last" ) ) );
	                            // ATTENZIONE:
	                            // commentando le successive 3 righe e sommnetando
								// la 4a viene eseguita una query per ogni ls per
								// recuperare i filtri.
	                            // e si incorren nel too many open cursors
	                            // se si lasciano scommentate le tre righe
								// sottostanti il sistema risponde che
	                            if ( linesetUserFilters != null ) {
	                                oneLs.setFiltersList( linesetUserFilters.getLinesetFiltersList( oneLs ) );
	                            }
	                            // oneLs.setFiltersList(
								// LinesetFilterManager.getLinesetFilters( oneLs,
								// user ) );
	                            resultingLinesetList.addElement( oneLs );
//                            }
//                            previousVobName = rsFoundedLinesets.getString("vob_name");
//                            previousLinesetName = rsFoundedLinesets.getString("lineset_name");
//                            previousInstallationId = rsFoundedLinesets.getString("installation_id");
                            
                            AfsCommonStaticClass.debugLog( commonErrorMessage + " :�� finito " + i + " ls name = " + rsFoundedLinesets.getString( "lineset_name" ) );
                        } catch ( SQLException e ) {
                            AfsCommonStaticClass.errorLog( commonErrorMessage + ": error while getting lineset db data " + e.getMessage(), e );
                        } catch ( ParseException e ) {
                            AfsCommonStaticClass.errorLog( commonErrorMessage + ": error while getting lineset db date (ParseException) " + e.getMessage(), e );
                        }
                    }
                } catch ( SQLException e ) {
                    throw new TpmsException( commonErrorMessage + ": error while getting lineset db data external SQLException " + e.getMessage(), commonErrorMessage, e );
                } finally {
                    try {
                        rsFoundedLinesets.close();
                    } catch ( SQLException e ) {
                        AfsCommonStaticClass.errorLog( commonErrorMessage + ": error while closing resultset " + e.getMessage(), e );
                    }
                }
            }
        }
        AfsCommonStaticClass.debugLog( commonErrorMessage + ": resultingLinesetList.size() = " + resultingLinesetList.size() );
        return resultingLinesetList;
    }


    /**
     * return the list of plants returning it from lineset table usefull when perfoming searches on Lineset table
     *
     * @return the list of plants returning it from lineset table
     *
     * @throws TpmsException if an error occours.
     */
    public static Vector getLinesetPlantList () throws TpmsException {
        String commonErrorMessage = "LinesetDBManager :: getLinesetPlantList ";
        String query = "select installation_id as id , installation_id as plant_name " +
                "from lineset " +
                "group by installation_id " +
                "order by installation_id asc";

        ResultSet rs = AfsCommonStaticClass.executeSelectQuery( query );
        Vector result = VectorUtils.dumpResultSetToVectorOfStrArray( rs, true );
        if ( rs != null ) {
            try {
                rs.close();
            } catch ( SQLException e ) {
                AfsCommonStaticClass.errorLog( commonErrorMessage + ": error while resultset " + e.getMessage(), e );
            }
        }
        return result;
    }


    public static boolean updateLinesetOwner ( GetLinesetOwnershipResult getLinesetOwnershipResult, Vob vob, String sessionId, TpmsUser currentTpmsUser ) {
        boolean result = false;
        if ( getLinesetOwnershipResult != null && vob != null ) {
            String commonErrorMessage = "LinesetDBManager :: updateLinesetOwner";
            Lineset oldLineset = getLinesetOwnershipResult.getLineset1();
            Lineset newLineset = getLinesetOwnershipResult.getLineset2();

            String query = "update lineset " +
                    "set owner = " + QueryUtils.getStringValueForQuery( newLineset.getOwner() ) +
                    " where INSTALLATION_ID = " + QueryUtils.getStringValueForQuery( tpmsConfiguration.getLocalPlant() ) +
                    "  and VOB_NAME = " + QueryUtils.getStringValueForQuery( vob.getName() ) +
                    "  and LINESET_NAME = " +QueryUtils.getStringValueForQuery( oldLineset.getName() );
            AfsCommonStaticClass.debugLog( commonErrorMessage + " query = " + query );
            result = AfsCommonStaticClass.executeUpdateQuery(query, sessionId, currentTpmsUser.getTpmsLogin() );


        }


        return result;
    }


}
