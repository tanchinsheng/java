package it.txt.integration.managers;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.integration.utils.RequestAttributesAndFieldsNames;
import it.txt.integration.utils.ResultDataFormatter;
import it.txt.tpms.backend.BackEndInterface;
import it.txt.tpms.backend.utils.BackEndInterfaceUtils;
import it.txt.tpms.tp.TP;
import it.txt.tpms.tp.utils.TPConstants;
import it.txt.tpms.tp.utils.TpDataValidator;
import it.txt.tpms.users.TpmsUser;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 7-lug-2006
 * Time: 16.41.29
 * This class contains usefull methods in order to make actions on TPs
 */
public class TpActionsManager {
    //the following value is the value used for the tester information in the case of the emergency tp deploy
    private static final String TESTER_INFO_EMPTY_VALUE = "XXXEMPTYTSTINFOXXX";

    public static StringBuffer deliverTps ( String toPlant, String jobName, String releaseNb, String revisionNb, String line, String facility, String packagePath,
                                            String testerInfo, String fromPlant, String fromMail, String validLogin, String prodLogin, String toMail,
                                            String ccMail, String owner, String sessionId, boolean emergencyMode ) {
        String actionDescription = "TP delivery : ";
        Vob tVob;
        //input data checks
        //debugLog("TpActionsManager :: deliverTps : start parameter validation");
        tVob = TpDataValidator.checkDestinationPlant( toPlant );
        if ( tVob == null ) {
            //no t vob configured found....
            return ResultDataFormatter.generateKoResult( "Unable to find T VOB for " + toPlant + " plant" );
        }

        if ( !TpDataValidator.checkJobName( jobName ) ) {
            //jobname is not valid...
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.JOB_NAME_FIELD + " parameter (" + jobName + ")" );
        }

        if ( !TpDataValidator.checkJobReleaseFormat( releaseNb ) ) {
            //releaseNb is not valid...
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.JOB_RELEASE_FIELD + " parameter (" + releaseNb + ")" );
        }

        if ( !TpDataValidator.checkJobRevision( revisionNb ) ) {
            //revision nb non valid
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.JOB_REVISION_FIELD + " parameter (" + revisionNb + ")" );
        }

        if ( !TpDataValidator.checkLine( line ) ) {
            //line is not valid
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.LINE_FIELD + " parameter (" + line + ")" );
        }

        if ( !TpDataValidator.checkFacility( facility, toPlant ) ) {
            //facility is not valid...
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.FACILITY_FIELD + " parameter (" + facility + ") for the given  plant (" + toPlant + ")" );
        }

        if ( !TpDataValidator.checkPath( packagePath ) ) {
            //the package Path is not valid...
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.PACKAGE_PATH_FIELD + " parameter (" + packagePath + ")" );
        }
        if ( !TpDataValidator.checkOwner( owner ) ) {
            //the owner is not valid....
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.OWNER_FIELD + " parameter (" + owner + ")" );
        }
        if ( !TpDataValidator.checkFromEmail( fromMail ) ) {
            return ResultDataFormatter.generateKoResult( "Invalid " + RequestAttributesAndFieldsNames.FROM_MAIL_FIELD + " parameter (" + fromMail + ")" );
        }


        if ( !emergencyMode ) {
            //if not in emergency mode extends the  checks on input parameter....
            if ( !TpDataValidator.checkTesterInfo( testerInfo, toPlant ) ) {
                //testerInfo are not valid for the destination plant
                return ResultDataFormatter.generateKoResult( "Unable to  " + RequestAttributesAndFieldsNames.TESTER_INFO_FIELD + " parameter (" + testerInfo + ") for plant " + toPlant );
            }

            if ( !TpDataValidator.checkValidLogin( validLogin, toPlant ) ) {
                //validLogin are not valid for the destination plant
                return ResultDataFormatter.generateKoResult( "Unable to  " + RequestAttributesAndFieldsNames.VALID_LOGIN_FIELD + " parameter (" + validLogin + ") in plant " + toPlant );
            }

            if ( !TpDataValidator.checkProdLogin( prodLogin, toPlant ) ) {
                //prodLogin are not valid for the destination plant
                return ResultDataFormatter.generateKoResult( "Unable to  " + RequestAttributesAndFieldsNames.PROD_LOGIN_FIELD + " parameter (" + prodLogin + ") in plant " + toPlant );
            }
        } else {
            AfsCommonStaticClass.errorLog( "TpActionsManager :: deliverTps :: the call is made in emergency mode,(" + RequestAttributesAndFieldsNames.PROD_LOGIN_FIELD + ", " + RequestAttributesAndFieldsNames.VALID_LOGIN_FIELD + ", " + RequestAttributesAndFieldsNames.TESTER_INFO_FIELD + " ) fields will be empty" );
            //empty valid login prod login and tester info to defaut empty value
            validLogin = "";
            prodLogin = "";
            testerInfo = TESTER_INFO_EMPTY_VALUE;
        }
        //debugLog("TpActionsManager :: deliverTps : parameter validation terminated, preparing information for BE call");
        if ( GeneralStringUtils.isEmptyTrimmedString( toMail ) ) toMail = "";
        if ( GeneralStringUtils.isEmptyTrimmedString( ccMail ) ) ccMail = "";
        String requestId = BackEndInterfaceUtils.getRequestId( sessionId );
        String xmlDataFile = BackEndInterfaceUtils.buildXmlOutFilePath( requestId );
        String inBackEndFileName = BackEndInterfaceUtils.buildInFilePath( requestId );
        String outBackEndFileName = BackEndInterfaceUtils.buildOutFilePath( requestId );
        //debugLog("TpActionsManager :: deliverTps : Terminated information preparation for BE call, writing input file (" + inBackEndFileName + ")");
        //write in file....
        boolean beInFileWrited;
        try {
            beInFileWrited = writeBackEndInFile( toPlant, jobName, releaseNb, revisionNb, line, facility, packagePath,
                    testerInfo, fromPlant, fromMail, validLogin, prodLogin, toMail,
                    ccMail, owner, tVob, xmlDataFile, sessionId, inBackEndFileName );
        } catch ( IOException e ) {
            String msg = actionDescription + "IOException while writing Back End input file ( " + inBackEndFileName + " ) - " + e.getMessage();
            return ResultDataFormatter.generateKoResult( msg, e );
        }
        //debugLog("TpActionsManager :: deliverTps : Terminated writing input file inFileWrited ? " + beInFileWrited);
        if ( beInFileWrited ) {
            //call action
            try {

                Date now = new Date();
                //TP( String jobName, int jobRelease, String jobRevision, int tpmsVersion, String line, String facility, String fromPlant, String ownerLogin, String ownerEmail, 
                // String origin, String originLabel, String linesetName, String toPlant, String testerInfo, String validLogin, String prodLogin, String lastActionActor,
                // Date lastActionDate, String status, Date distributionDate, Date productionDate, String division, String vobStatus, String installationId,
                // String prisStatus, Date prisFoundDate, String productionAreaId, String dbComments ) {
                //this.id = generateId(jobName, jobRelease, jobRevision, tpmsVersion)
                TP tp = new TP( jobName, Integer.parseInt( releaseNb ), revisionNb, TPConstants.TPMS_VERSION_DEFAULT_VALUE, line, facility, fromPlant, owner, fromMail,
                        "STEPS", "", line, toPlant, testerInfo, validLogin, prodLogin, owner,
                        now, TPConstants.DISTRIBUTED_STATUS, now, null, "STEPS", TPConstants.ON_LINE_VOB_STATUS, fromPlant,
                        TPConstants.UNKNOW_PRIS_STATUS, null, null, null );


                TpmsUser tpmsUser = new TpmsUser();
                tpmsUser.setName( owner );
                tpmsUser.setUnixUser( owner );
                tpmsUser.setInstallationId( fromPlant );
                tpmsUser.setEmail( fromMail );


                TpDBManager tpDBManager = new TpDBManager( tp, new File( xmlDataFile ), sessionId, tpmsUser );
                Method updateDbMethod = null;
                try {
                    updateDbMethod = tpDBManager.getClass().getMethod( "doStepsTPDelivery", null );
                } catch ( NoSuchMethodException e ) {
                    //pay attention this situation should not happen...
                    AfsCommonStaticClass.errorLog( actionDescription + " : DB will not be updated : NoSuchMethodException " + e.getMessage(), e );
                }


                BackEndInterface.invokeCommand( UserUtils.getAdminUnixLogin(), inBackEndFileName, outBackEndFileName, sessionId, tpDBManager, updateDbMethod, null );

            } catch ( IOException e ) {
                String msg = actionDescription + "IOException while calling BE executable : " + e.getMessage();
                return ResultDataFormatter.generateKoResult( msg, e );
            }
        }

        //reply with the file path
        return ResultDataFormatter.generateOkResult( "<output_path>" + outBackEndFileName + "</output_path>" );
    }

    /**
     * Starting from input parameters writes back end in file
     *
     * @param toPlant
     * @param jobName
     * @param releaseNb
     * @param revisionNb
     * @param line
     * @param facility
     * @param packagePath
     * @param testerInfo
     * @param fromPlant
     * @param fromMail
     * @param validLogin
     * @param prodLogin
     * @param toMail
     * @param ccMail
     * @param owner
     * @param tVob
     * @param xmlOutFile
     * @param sessionId
     * @param inBackEndFileName
     *
     * @return true if the write of the in file succeded
     *
     * @throws IOException if an error occours while writing the in file
     */
    private static boolean writeBackEndInFile ( String toPlant, String jobName, String releaseNb, String revisionNb, String line, String facility, String packagePath,
                                                String testerInfo, String fromPlant, String fromMail, String validLogin, String prodLogin, String toMail,
                                                String ccMail, String owner, Vob tVob, String xmlOutFile, String sessionId, String inBackEndFileName ) throws IOException {

        //debugLog("TpActionsManager :: writeBackEndInFile : start input file content preparation..");
        Hashtable inFileParameters = new Hashtable();
        inFileParameters.put( "rectype", "tp" );
        inFileParameters.put( "action", "tp_steps" );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 0");
        inFileParameters.put( "jobname", jobName );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 1");
        inFileParameters.put( "release_nb", releaseNb );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 2");
        inFileParameters.put( "revision_nb", revisionNb );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 3");
        inFileParameters.put( "version_nb", "00" );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 4");
        inFileParameters.put( "facility", facility );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 5");
        inFileParameters.put( "tester_info", testerInfo );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 6");
        inFileParameters.put( "line", line );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 7");
        inFileParameters.put( "linesetname", jobName );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 8");
        inFileParameters.put( "xfer_path", packagePath );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 9");
        inFileParameters.put( "to_plant", toPlant );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 10");
        inFileParameters.put( "valid_login", validLogin );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 11");
        inFileParameters.put( "prod_login", prodLogin );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 12");
        inFileParameters.put( "from_plant", fromPlant );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 13");
        inFileParameters.put( "outfile", xmlOutFile );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 14");
        inFileParameters.put( "session_id", sessionId );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 15");
        inFileParameters.put( "to_mail", toMail );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 16");
        inFileParameters.put( "cc_mail1", ccMail );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 17");
        inFileParameters.put( "cc_mail2", "" );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 18");
        inFileParameters.put( "from_mail", fromMail );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 19");
        inFileParameters.put( "user", owner );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 20");
        inFileParameters.put( "unix_home", "" );//this parameter should be empty.
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 21");
        //debugLog("TpActionsManager :: writeBackEndInFile : Input file content: tVob == null ? " + (tVob == null));
        //debugLog("TpActionsManager :: writeBackEndInFile : Input file content: tVob.getName() = " + tVob.getName());
        inFileParameters.put( "to_vob", tVob.getName() );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 22");
        inFileParameters.put( "vobname", tVob.getName() );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 23");
        inFileParameters.put( "trans_vob_list", tVob.getName() );
        //debugLog("TpActionsManager :: writeBackEndInFile : Input file content: tpmsConfiguration.getCCDebugClearcaseInterfaceValue() = " + tpmsConfiguration.getCCDebugClearcaseInterfaceValue());
        inFileParameters.put( "debug", AfsCommonStaticClass.getTpmsConfiguration().getCCDebugClearcaseInterfaceValue() );
        //debugLog("TpActionsManager :: writeBackEndInFile : input file content 24");

        //debugLog("TpActionsManager :: writeBackEndInFile : Input file content terminated");
        /*File unixHomeFile = new File(packagePath);
        if (unixHomeFile.isFile()){
            inFileParameters.put("unix_home", unixHomeFile.getParentFile());
        } else {
            inFileParameters.put("unix_home", packagePath);
        }*/
        return BackEndInterfaceUtils.writeInBackEndFile( inFileParameters, inBackEndFileName );

    }
}
