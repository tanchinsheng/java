package it.txt.tpms.lineset.managers;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.installations.TpmsInstallationData;
import it.txt.general.installations.manager.TpmsInstallationsManager;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.backend.BackEndInterface;
import it.txt.tpms.backend.results.GetLinesetOwnershipResult;
import it.txt.tpms.backend.results.LinesetModifiedFileData;
import it.txt.tpms.backend.utils.BackEndCallResult;
import it.txt.tpms.backend.utils.BackEndInterfaceConstants;
import it.txt.tpms.backend.utils.BackEndInterfaceUtils;
import it.txt.tpms.lineset.Lineset;
import it.txt.tpms.lineset.accessibility.LinesetAccessibilityManager;
import it.txt.tpms.lineset.list.LinesetList;
import it.txt.tpms.lineset.packages.LinesetPackage;
import it.txt.tpms.lineset.packages.ReceivedLinesetPackage;
import it.txt.tpms.lineset.packages.manager.LinesetPackageManager;
import it.txt.tpms.users.TpmsUser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tpms.TpmsException;
import tpms.VobManager;
import tpms.utils.TpmsConfiguration;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 14-set-2006
 * Time: 13.12.16
 */
public class LinesetVobManager {
    public static final String LS_DATA_TAG = "LS";
    public static final String LS_NAME_TAG = "LS_NAME";
    public static final String LS_OWNER_TAG = "LS_OWNER";
    public static final String LS_TESTER_FAMILY_TAG = "TESTERFAM";
    public static final String LS_UNIX_HOME_TAG = "UNIX_HOME";
    public static final String LS_SYNCHRO_DIR_TAG = "SYNCRODIR";
    public static final String LS_LAST_ACTION_DATE_TAG = "DATELAST";
    public static final String LS_BASELINE_TAG = "LASTBASELINE";
    public static final String LS_FILES_COUNT_TAG = "NB_OF_FILES";
    public static final String LS_SIZE_TAG = "TOTAL_MB_SIZE";
    public static final String LS_STATUS_TAG = "SUBMIT_STATUS";
    public static final String LS_ORIGINAL_OWNER_TAG = "ORIGINAL_OWNER";
    public static final String LS_READY_STATUS_VALUE = "READY";

    //GO stands for Get ownership
    public static final String GO_RESULT_TAG = "RESULT";
    public static final String GO_MESSAGE_TAG = "MSG";
    public static final String GO_LINESET1_TAG = "BS1";
    public static final String GO_LINESET2_TAG = "BS2";
    public static final String GO_FILE_TAG = "FILE";
    public static final String GO_SUMMARY_TAG = "SUMM";
    public static final String GO_NEW_FILES_NOT_REFERENCED_TAG = "NB_NEW";
    public static final String GO_NEW_REFERENCED_FILES_TAG = "NB_NEW_REFBYXFER";
    public static final String GO_TOTAL_NOT_REFERENCED_FILES_TAG = "NB_OUTOFXFER";
    public static final String GO_NOT_MODIFIED_FILES_TAG = "NB_UNCHANGED";
    public static final String GO_MODIFIED_FILES_TAG = "NB_MODIFIED";
    public static final String GO_REMOVED_FILES_TAG = "NB_REMOVED";

    private static TpmsConfiguration tpmsConfiguration = AfsCommonStaticClass.getTpmsConfiguration();

    /**
     * Parse the resulting xml file of an ls query BE call returning the list containing those linesets that are owned by ownerLogin.
     *
     * @param ownerLogin
     * @param requestId
     *
     * @return a list of linesets according to the previously described logic
     */
    public static LinesetList parseMyLinesetXmlDataFile ( String ownerLogin, String requestId, Vob queriedVob ) throws ParserConfigurationException, SAXException, IOException, TpmsException {
        return parseLinesetQueryXmlOutFile( ownerLogin, false, requestId, queriedVob );
    }

    /**
     * Parse the resulting xml file of an ls query BE call returning the list containing those linesets that are *NOT* owned by ownerLogin.
     *
     * @param ownerLogin
     * @param requestId
     *
     * @return a list of linesets according to the previously described logic
     */
    public static LinesetList parseOtherLinesetXmlDataFile ( String ownerLogin, String requestId, Vob queriedVob ) throws ParserConfigurationException, SAXException, IOException, TpmsException {
        return parseLinesetQueryXmlOutFile( ownerLogin, true, requestId, queriedVob );
    }

    /**
     * Parse the resulting xml file of an ls query BE call: if otherLinesets is true the list will be populated with those linesets that are not owned by ownerLogin.
     * If otherLinesets is false the the lineset list will contain only those linesets owned by the current user.
     *
     * @param userLoginFilter
     * @param otherLinesets
     * @param requestId
     *
     * @return a list of linesets according to the previously described logic
     */
    protected static LinesetList parseLinesetQueryXmlOutFile ( String userLoginFilter, boolean otherLinesets, String requestId, Vob queriedVob ) throws ParserConfigurationException, SAXException, IOException, TpmsException {
        String actionDescription = "LinesetVobManager :: parseLinesetQueryXmlOutFile :";
        LinesetList lsList = new LinesetList();
        String xmlDataFileName = BackEndInterfaceUtils.buildXmlOutFilePath( requestId );
        //String xmlDataFileName = requestId;
        File xmlDataFile = new File( xmlDataFileName );
        if ( !xmlDataFile.exists() ) {
            String msg = "Unable to parse query result ";
            throw new TpmsException( msg, "lineset query", actionDescription + " xml data file do not exists" );
        }
        Element sentPacketListRoot = XmlUtils.getRoot( xmlDataFile );
        if ( sentPacketListRoot != null && sentPacketListRoot.hasChildNodes() ) {
            NodeList nlLinesets = sentPacketListRoot.getElementsByTagName( LS_DATA_TAG );
            if ( nlLinesets != null ) {

                if ( nlLinesets.getLength() > 0 ) {
                    int linesetCount = nlLinesets.getLength();
                    Element oneLinesetData;
                    String linesetOwner;
                    String linesetStatus;
                    String linesetName;
                    boolean addLinesetToList;
                    for ( int i = 0; i < linesetCount; i++ ) {
                        oneLinesetData = ( Element )nlLinesets.item( i );

                        linesetOwner = XmlUtils.getTextValue( oneLinesetData, LS_OWNER_TAG );
                        linesetStatus = XmlUtils.getTextValue( oneLinesetData, LS_STATUS_TAG );


                        if ( !GeneralStringUtils.isEmptyString( linesetStatus ) ) {
                            if ( linesetStatus.equals( LS_READY_STATUS_VALUE ) ) {
                                if ( !GeneralStringUtils.isEmptyTrimmedString( linesetOwner ) ) {
                                    if ( otherLinesets ) {
                                        //add to the list linesets not owned by ownerLogin and if the current user may get the current lineset.
                                        linesetName = XmlUtils.getTextValue( oneLinesetData, LS_NAME_TAG );
                                        addLinesetToList = !userLoginFilter.equals( linesetOwner ) && LinesetAccessibilityManager.userCanViewLineset( queriedVob, linesetName, userLoginFilter );
                                    } else {
                                        //add to the list linesets owned by ownerLogin
                                        addLinesetToList = userLoginFilter.equals( linesetOwner );
                                    }
                                    if ( addLinesetToList ) {
                                        lsList.addElement( parseOneLinesetData( oneLinesetData, queriedVob ) );
                                    }
                                } else {
                                    //the current lineset does not have the owner associated.
                                    AfsCommonStaticClass.errorLog( actionDescription + " : linset without owner found!! lineset name = " + XmlUtils.getTextValue( oneLinesetData, LS_NAME_TAG ) );
                                }
                            }
                        } else {
                            AfsCommonStaticClass.errorLog( actionDescription + " : linset without STATUS found!! lineset name = " + XmlUtils.getTextValue( oneLinesetData, LS_NAME_TAG ) );
                        }


                    }
                }

            }
        }
        return lsList;
    }



    /**
     * This method is useful for those linesets that must be different form the one where it is been created (like remote cooperative development)
     * where the application CAN NOT be able to load VOB object data
     * starting from an Xml Element containing lineset data return a Lineset object
     *
     * @param oneLinesetData
     * @param linesetVobName
     *
     * @return a lineset object
     */
    private static Lineset parseOneLinesetData ( Element oneLinesetData, String linesetVobName ) {
                String actionDescription = "LinesetVobManager :: parseOneLinesetData 0 :";
        Lineset oneLineset = null;

        if ( oneLinesetData != null ) {
            String linesetName;
            String linesetOwner;
            String linesetTesterFamily;
            String linesetUnixHome;
            String linesetSynchroDir;
            String linesetBaseline;
            String linesetStatus;
            int linesetFilesCount;
            float linesetSize;
            Date linesetLastActionDate;
            String originalOwner;


            linesetName = XmlUtils.getTextValue( oneLinesetData, LS_NAME_TAG );
            linesetOwner = XmlUtils.getTextValue( oneLinesetData, LS_OWNER_TAG );
            linesetTesterFamily = XmlUtils.getTextValue( oneLinesetData, LS_TESTER_FAMILY_TAG );
            linesetUnixHome = XmlUtils.getTextValue( oneLinesetData, LS_UNIX_HOME_TAG );
            linesetSynchroDir = XmlUtils.getTextValue( oneLinesetData, LS_SYNCHRO_DIR_TAG );
            linesetBaseline = XmlUtils.getTextValue( oneLinesetData, LS_BASELINE_TAG );
            linesetStatus = XmlUtils.getTextValue( oneLinesetData, LS_STATUS_TAG );
            originalOwner = XmlUtils.getTextValue( oneLinesetData, LS_READY_STATUS_VALUE );


            try {
                linesetLastActionDate = XmlUtils.getDateValue( oneLinesetData, LS_LAST_ACTION_DATE_TAG, BackEndInterfaceConstants.XMLOUT_DATE_FORMAT );
            } catch ( ParseException e ) {
                AfsCommonStaticClass.errorLog( actionDescription + " unable to parse last action date: >linesetName  = " + linesetName + "< ", e );
                linesetLastActionDate = null;
            }
            try {
                linesetSize = XmlUtils.getFloatValue( oneLinesetData, LS_SIZE_TAG );
            } catch ( NumberFormatException e ) {
                AfsCommonStaticClass.errorLog( actionDescription + " unable size :  >linesetName  = " + linesetName + "< ", e );
                linesetSize = -1;
            }

            try {
                linesetFilesCount = XmlUtils.getIntValue( oneLinesetData, LS_FILES_COUNT_TAG );
            } catch ( NumberFormatException e ) {
                AfsCommonStaticClass.errorLog( actionDescription + " unable to number of files : >linesetName = " + linesetName + "< ", e );
                linesetFilesCount = -1;
            }
            //usa questo costruttore
            //public Lineset ( String linesetName, String owner, String originalOwner, String testerFamily, String unixBaseDirectory, String syncroDir,
            // Date lastActionDate, String baseline, int filesCount, float linesetSizeKB, String submitStatus,
            // String vobName, String plant ) {
            oneLineset = new Lineset( linesetName, linesetOwner, originalOwner, linesetTesterFamily, linesetUnixHome, linesetSynchroDir,
                    linesetLastActionDate, linesetBaseline, linesetFilesCount, linesetSize, linesetStatus,
                    linesetVobName, tpmsConfiguration.getLocalPlant() );


        }
        return oneLineset;
    }



    /**
     * This method is useful for those linesets that must be loaded locally where the application is able to load VOB object data
     * starting from an Xml Element containing lineset data return a Lineset object
     *
     * @param oneLinesetData
     * @param linesetVob
     *
     * @return a lineset object
     */
    private static Lineset parseOneLinesetData ( Element oneLinesetData, Vob linesetVob ) {
        String actionDescription = "LinesetVobManager :: parseOneLinesetData 0 :";
        Lineset oneLineset = null;

        if ( oneLinesetData != null ) {
            String linesetName;
            String linesetOwner;
            String linesetTesterFamily;
            String linesetUnixHome;
            String linesetSynchroDir;
            String linesetBaseline;
            String linesetStatus;
            int linesetFilesCount;
            float linesetSize;
            Date linesetLastActionDate;
            String originalOwner;


            linesetName = XmlUtils.getTextValue( oneLinesetData, LS_NAME_TAG );
            linesetOwner = XmlUtils.getTextValue( oneLinesetData, LS_OWNER_TAG );
            linesetTesterFamily = XmlUtils.getTextValue( oneLinesetData, LS_TESTER_FAMILY_TAG );
            linesetUnixHome = XmlUtils.getTextValue( oneLinesetData, LS_UNIX_HOME_TAG );
            linesetSynchroDir = XmlUtils.getTextValue( oneLinesetData, LS_SYNCHRO_DIR_TAG );
            linesetBaseline = XmlUtils.getTextValue( oneLinesetData, LS_BASELINE_TAG );
            linesetStatus = XmlUtils.getTextValue( oneLinesetData, LS_STATUS_TAG );
            originalOwner = XmlUtils.getTextValue( oneLinesetData, LS_READY_STATUS_VALUE );


            try {
                linesetLastActionDate = XmlUtils.getDateValue( oneLinesetData, LS_LAST_ACTION_DATE_TAG, BackEndInterfaceConstants.XMLOUT_DATE_FORMAT );
            } catch ( ParseException e ) {
                AfsCommonStaticClass.errorLog( actionDescription + " unable to parse last action date: >linesetName  = " + linesetName + "< ", e );
                linesetLastActionDate = null;
            }
            try {
                linesetSize = XmlUtils.getFloatValue( oneLinesetData, LS_SIZE_TAG );
            } catch ( NumberFormatException e ) {
                AfsCommonStaticClass.errorLog( actionDescription + " unable size :  >linesetName  = " + linesetName + "< ", e );
                linesetSize = -1;
            }

            try {
                linesetFilesCount = XmlUtils.getIntValue( oneLinesetData, LS_FILES_COUNT_TAG );
            } catch ( NumberFormatException e ) {
                AfsCommonStaticClass.errorLog( actionDescription + " unable to number of files : >linesetName = " + linesetName + "< ", e );
                linesetFilesCount = -1;
            }
            //usa questo costruttore
            //public Lineset ( String linesetName, String owner, String testerFamily, String unixBaseDirectory, String syncroDir, Date lastActionDate, String baseline, int filesCount, float linesetSizeKB, String submitStatus, String vobName, String plant ) {
            oneLineset = new Lineset( linesetName, linesetOwner, linesetTesterFamily, linesetUnixHome, linesetSynchroDir,
                    linesetLastActionDate, linesetBaseline, linesetFilesCount, linesetSize, linesetStatus,
                    linesetVob, originalOwner, tpmsConfiguration.getLocalPlant() );


        }
        return oneLineset;
    }


    /**
     * This method makes the call in order to load a Lineset Package into local development VOB (cooperative development)
     *
     * @param dVob
     * @param linesetPackage
     * @param actionTpmsUser
     * @param requestId
     * @param sessionId
     *
     * @throws TpmsException
     */
    public static void loadLineset ( Vob dVob, ReceivedLinesetPackage linesetPackage, TpmsUser actionTpmsUser, String requestId, String sessionId ) throws TpmsException {

        String actionDescription = "load lineset package";
        if ( linesetPackage == null ) {
            //lineset package is null, raise an error...
            String msg = actionDescription + " : input lineset package is null : action not started! ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( dVob == null ) {
            //The vob where the lineset should be loaded is null, raise an error...
            String msg = actionDescription + " : The vob where the lineset should be loaded is null : action not started! ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( actionTpmsUser == null ) {
            //The user that has to execute the action is NULL, raise an error...
            String msg = actionDescription + " : The user that has to execute the action is null : action not started! ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( !dVob.getType().equals( VobManager.D_VOB_TYPE_FLAG_VALUE ) ) {
            //The vob where the lineset should be loaded is null, raise an error...
            String msg = actionDescription + " : The vob where the lineset should be loaded is not a developing one : action not started! ";
            throw new TpmsException( msg, actionDescription, "Select a Developing vob" );
        }

        String xmlDataFile = BackEndInterfaceUtils.buildXmlOutFilePath( requestId );
        String inBackEndFileName = BackEndInterfaceUtils.buildInFilePath( requestId );
        String outBackEndFileName = BackEndInterfaceUtils.buildOutFilePath( requestId );

        boolean beInFileWrited;

        try {
            beInFileWrited = writeLoadLinesetPackageBackEndInFile( linesetPackage, dVob, actionTpmsUser, sessionId, inBackEndFileName, xmlDataFile );
        } catch ( IOException e ) {
            String msg = actionDescription + " : IOException while writing Back End input file ( " + inBackEndFileName + " ) - " + e.getMessage();
            throw new TpmsException( msg, actionDescription, e );
        }

        if ( beInFileWrited ) {
            //if the BE file was succesfully writed call action
            try {
                BackEndInterface.invokeCommand( actionTpmsUser.getUnixUser(), inBackEndFileName, outBackEndFileName, sessionId );
            } catch ( IOException e ) {
                String msg = actionDescription + " : IOException while calling BE executable : " + e.getMessage();
                throw new TpmsException( msg, actionDescription, e );
            }
        }
    }

    /**
     * This method makes the call in order to send a Lineset to a remote tpms installation (cooperative development)
     *
     * @param lineset
     * @param destinationTpmsInstallationData
     *
     * @param destinationUser
     * @param requestId
     * @param sessionId
     *
     * @throws TpmsException
     */
    public static void sendLineset ( Lineset lineset, TpmsInstallationData destinationTpmsInstallationData, TpmsUser destinationUser,
                                     String ccEmailAddresses, String requestId, String sessionId )
            throws TpmsException {
        String actionDescription = "send lineset ownership";

        if ( lineset == null ) {
            //lineset is null, raise an error...
            String msg = actionDescription + " : input lineset is null : action not started! ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( destinationTpmsInstallationData == null ) {
            //destination installation is null, raise an error
            String msg = actionDescription + " : destination information are null : action not started! ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( destinationUser == null ) {
            //destination user is null, raise an error
            String msg = actionDescription + " : destination user is null : action not started! ";
            throw new TpmsException( msg, actionDescription, "" );
        }


        TpmsInstallationData sourceInstallationData = TpmsInstallationsManager.getLocalTpmsInstallationsInfoWithUsersData();


        String xmlDataFile = BackEndInterfaceUtils.buildXmlOutFilePath( requestId );
        String inBackEndFileName = BackEndInterfaceUtils.buildInFilePath( requestId );
        String outBackEndFileName = BackEndInterfaceUtils.buildOutFilePath( requestId );

        boolean beInFileWrited;
        /*
        String linesetPackageFileName = LinesetPackageManager.generatePackageFileName(lineset.getName(), lineset.getBaseline(), lineset.getTesterFamily(), destinationTpmsInstallationData.getTpmsInstallationId(),
                destinationUser.getName(), lineset.getOwner(), sourceInstallationData.getTpmsInstallationId(), lineset.getVobName());
        */
        try {

            LinesetPackage linesetPackage = new LinesetPackage( lineset.getName(), lineset.getBaseline(), lineset.getTesterFamily(),
                    destinationTpmsInstallationData, destinationUser,
                    lineset.getOwnerTpmsUser(), sourceInstallationData, lineset.getVob() );
            beInFileWrited = writeSendLinesetBackEndInFile( lineset, xmlDataFile, sessionId, inBackEndFileName, linesetPackage, ccEmailAddresses );
        } catch ( IOException e ) {
            String msg = actionDescription + " : IOException while writing Back End input file ( " + inBackEndFileName + " ) - " + e.getMessage();
            throw new TpmsException( msg, actionDescription, e );
        }

        if ( beInFileWrited ) {
            //if the BE file was succesfully writed call action
            try {
                BackEndInterface.invokeCommand( UserUtils.getAdminUnixLogin(), inBackEndFileName, outBackEndFileName, sessionId );
            } catch ( IOException e ) {
                String msg = actionDescription + " : IOException while calling BE executable : " + e.getMessage();
                throw new TpmsException( msg, actionDescription, e );
            }
        }
    }


    /**
     * this method call the BE action in order to perform the get lineset ownership
     *
     * @param newOwner
     * @param lineset
     * @param requestId
     * @param sessionId
     *
     * @throws TpmsException
     */
    public static void getLinesetOwnership ( TpmsUser newOwner, Lineset lineset, String requestId, String sessionId ) throws TpmsException {
        String actionDescription = "Get lineset ownership";


        if ( lineset == null ) {
            //lineset is null, raise an error...
            String msg = actionDescription + " : input lineset is null : action not started! ) ";
            throw new TpmsException( msg, actionDescription, "" );
        }


        Vob linesetVob = lineset.getVob();

        if ( linesetVob == null ) {
            //VOB is null, raise an error...
            String msg = actionDescription + " : given vob is null : action not started! ) ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( !linesetVob.getType().equals( VobManager.D_VOB_TYPE_FLAG_VALUE ) ) {
            //VOB isn't a D vob... raise an error...
            String msg = actionDescription + " : given vob is not a D one : action not started! (vob name = " + linesetVob.getName() + " ) ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( newOwner == null ) {
            //current user login is null or empty...
            String msg = actionDescription + " : input new owner is null : action not started! ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        String xmlDataFile = BackEndInterfaceUtils.buildXmlOutFilePath( requestId );
        String inBackEndFileName = BackEndInterfaceUtils.buildInFilePath( requestId );
        String outBackEndFileName = BackEndInterfaceUtils.buildOutFilePath( requestId );
        boolean beInFileWrited;
        try {
            //Lineset lineset, Vob dVob, String sessionId
            beInFileWrited = writeChangeOwnershipBackEndInFile( inBackEndFileName, xmlDataFile, newOwner, lineset, sessionId );
        } catch ( IOException e ) {
            String msg = actionDescription + " : IOException while writing Back End input file ( " + inBackEndFileName + " ) - " + e.getMessage();
            throw new TpmsException( msg, actionDescription, e );
        }

        if ( beInFileWrited ) {
            //if the BE file was succesfully writed call action
            try {
                BackEndInterface.invokeCommand( newOwner.getUnixUser(), inBackEndFileName, outBackEndFileName, sessionId );
            } catch ( IOException e ) {
                String msg = actionDescription + " : IOException while calling BE executable : " + e.getMessage();
                throw new TpmsException( msg, actionDescription, e );
            }
        }
    }


    /**
     * call BE in order to retrieve LS not owned by current user
     *
     * @param dVob
     * @param ownerLogin
     * @param requestId
     * @param sessionId
     *
     * @throws TpmsException
     */
    public static void otherLinesetQuery ( Vob dVob, String ownerLogin, String requestId, String sessionId ) throws TpmsException {
        String actionDescription = "Other LS Query";
        //input parameter checks....
        if ( dVob == null ) {
            //VOB is null, raise an error...
            String msg = actionDescription + " : given vob is null : action not started! ) ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        if ( !dVob.getType().equals( VobManager.D_VOB_TYPE_FLAG_VALUE ) ) {
            //VOB isn't a D vob... raise an error...
            String msg = actionDescription + " : given vob is not a D one : action not started! (vob name = " + dVob.getName() + " ) ";
            throw new TpmsException( msg, actionDescription, "" );
        }
        if ( GeneralStringUtils.isEmptyString( ownerLogin ) ) {
            //current user login is null or empty...
            String msg = actionDescription + " : given user login is null or empty : action not started! (currentUserLogin = " + ownerLogin + " ) ";
            throw new TpmsException( msg, actionDescription, "" );
        }

        //BE call preparation...
        String unixUser = UserUtils.getUserUnixLogin( ownerLogin );
        String xmlDataFile = BackEndInterfaceUtils.buildXmlOutFilePath( requestId );
        String inBackEndFileName = BackEndInterfaceUtils.buildInFilePath( requestId );
        String outBackEndFileName = BackEndInterfaceUtils.buildOutFilePath( requestId );

        boolean beInFileWrited;
        try {
            beInFileWrited = writeOtherLsQueryBackEndInFile( dVob, xmlDataFile, sessionId, inBackEndFileName, unixUser );
        } catch ( IOException e ) {
            String msg = actionDescription + " : IOException while writing Back End input file ( " + inBackEndFileName + " ) - " + e.getMessage();
            throw new TpmsException( msg, actionDescription, e );
        }

        if ( beInFileWrited ) {
            //if the BE file was succesfully writed call action
            try {
                BackEndInterface.invokeCommand( unixUser, inBackEndFileName, outBackEndFileName, sessionId );
            } catch ( IOException e ) {
                String msg = actionDescription + " : IOException while calling BE executable : " + e.getMessage();
                throw new TpmsException( msg, actionDescription, e );
            }
        }
    }

    /**
     * @param requestId
     * @param dVob
     *
     * @return this methods parse resulting data file of get lineset ownership action and load lineset package
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TpmsException
     */

    public static GetLinesetOwnershipResult parseGetLinesetOwnershipXmlDataFile ( String requestId, Vob dVob, String sourceVobName ) throws ParserConfigurationException, SAXException, IOException, TpmsException {
        GetLinesetOwnershipResult getLinesetOwnershipResult = null;
        String actionDescription = "LinesetVobManager :: parseGetLinesetOwnershipXmlDataFile :";

        String xmlDataFileName = BackEndInterfaceUtils.buildXmlOutFilePath( requestId );

        File xmlDataFile = new File( xmlDataFileName );
        if ( !xmlDataFile.exists() ) {
            String msg = "Unable to find xml data file (" + xmlDataFileName + ")";
            throw new TpmsException( msg, "Get Lineset Ownership", actionDescription + " xml data file do not exists" );
        }

        Element xmlDataFileRoot = XmlUtils.getRoot( xmlDataFile );
        if ( xmlDataFileRoot != null && xmlDataFileRoot.hasChildNodes() ) {
            //let's check for result and message...
            String message = XmlUtils.getVal( ( Element )xmlDataFileRoot.getElementsByTagName( GO_MESSAGE_TAG ).item( 0 ) );
            String result = XmlUtils.getVal( ( Element )xmlDataFileRoot.getElementsByTagName( GO_RESULT_TAG ).item( 0 ) );
            //todo... qui l'errore.... come vob questo metodo dovrebbe settare quello recuperato dal nome file del pacchetto
            Lineset lineset1 = parseOneLinesetData( ( Element )xmlDataFileRoot.getElementsByTagName( GO_LINESET1_TAG ).item( 0 ), sourceVobName );

            Lineset lineset2 = parseOneLinesetData( ( Element )xmlDataFileRoot.getElementsByTagName( GO_LINESET2_TAG ).item( 0 ), dVob );

            //let's look for summary informations
            Element elSummaryData = ( Element )xmlDataFileRoot.getElementsByTagName( GO_SUMMARY_TAG ).item( 0 );
            int callResult = GetLinesetOwnershipResult.DEFAULT_RESULT;
            if ( elSummaryData != null ) {

                long newFilesNotReferencedCount = XmlUtils.getLongValue( elSummaryData, GO_NEW_FILES_NOT_REFERENCED_TAG );
                long newFilesRefedencedCount = XmlUtils.getLongValue( elSummaryData, GO_NEW_REFERENCED_FILES_TAG );
                long totalFilesNotReferencedCount = XmlUtils.getLongValue( elSummaryData, GO_TOTAL_NOT_REFERENCED_FILES_TAG );
                long filesNotModifiedCount = XmlUtils.getLongValue( elSummaryData, GO_NOT_MODIFIED_FILES_TAG );
                long filesModifiedCount = XmlUtils.getLongValue( elSummaryData, GO_MODIFIED_FILES_TAG );
                long filesRemovedCount = XmlUtils.getLongValue( elSummaryData, GO_REMOVED_FILES_TAG );

                if ( "OK".equals( result ) ) {
                    callResult = GetLinesetOwnershipResult.OK_RESULT;
                } else if ( "WARNING".equals( result ) ) {
                    callResult = GetLinesetOwnershipResult.WARNING_RESULT;
                }
                getLinesetOwnershipResult = new GetLinesetOwnershipResult( message, callResult, lineset1, lineset2, newFilesNotReferencedCount, newFilesRefedencedCount, totalFilesNotReferencedCount,
                        filesNotModifiedCount, filesModifiedCount, filesRemovedCount );
            } else {
                if ( "OK".equals( result ) ) {
                    callResult = GetLinesetOwnershipResult.OK_RESULT;
                } else if ( "WARNING".equals( result ) ) {
                    callResult = GetLinesetOwnershipResult.WARNING_RESULT;
                }

                getLinesetOwnershipResult = new GetLinesetOwnershipResult( message, callResult, lineset1, lineset2 );
            }

            //check for changed files...
            NodeList nlChangedFileList = xmlDataFileRoot.getElementsByTagName( GO_FILE_TAG );
            if ( nlChangedFileList != null ) {
                int changedFilesCount = nlChangedFileList.getLength();
                LinesetModifiedFileData tmpChangedFile;
                for ( int i = 0; i < changedFilesCount; i++ ) {
                    tmpChangedFile = new LinesetModifiedFileData( ( Element )nlChangedFileList.item( i ) );
                    getLinesetOwnershipResult.addFile( tmpChangedFile );
                }
            }

        }
        return getLinesetOwnershipResult;
    }


    /**
     * this method checks for if the BE call done to Load a lineset package and identified by requestId is terminated and if is terminated parse the xml file
     * containing result file
     *
     * @param requestId
     * @param sessionId
     * @param selectedVob
     *
     * @return the result of the backend calls
     *
     * @throws TpmsException
     */
    public static BackEndCallResult checkLoadLinesetPackageResult ( String requestId, String sessionId, TpmsUser actionTpmsUser, Vob selectedVob, ReceivedLinesetPackage linesetPackage ) throws TpmsException {
        String actionDescription = "LinesetVobManager :: checkLoadLinesetPackageResult";

        int actionCallResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
        String outFilePath = BackEndInterfaceUtils.buildOutFilePath( requestId );
        File outFile = new File( outFilePath );
        GetLinesetOwnershipResult getLinesetOwnershipResult = null;
        if ( outFile.exists() ) {
            //out file found ... BE call terminated
            Hashtable outFileContent;
            try {
                outFileContent = BackEndInterface.parseOutFile( new File( outFilePath ), actionTpmsUser.getUnixUser(), sessionId );
            } catch ( IOException e ) {
                String msg = "There was an error parsing clearcase output file (" + outFilePath + ")";
                AfsCommonStaticClass.errorLog( actionDescription + " : requestId = " + requestId + " " + msg + " exception message = " + e.getMessage(), e );
                throw new TpmsException( msg, "outFilePath = " + outFilePath, e );
            }

            String ccCommandResult = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_RESULT );
            AfsCommonStaticClass.debugLog( actionDescription + " ccCommandResult = " + ccCommandResult );
            if ( ccCommandResult.equals( BackEndInterfaceConstants.OUT_RESULT_OK_VALUE ) ) {
                //the BE call is terminated with positive result.... let's check inside of xml file if there is any warning
                actionCallResult = BackEndInterfaceConstants.COMMAND_OK_RESULT;
                try {

                    getLinesetOwnershipResult = parseGetLinesetOwnershipXmlDataFile( requestId, selectedVob, linesetPackage.getSourceVobName() );
                    LinesetDBManager.updateLinesetDataAfterLinesetLoadingProcess( linesetPackage, selectedVob, getLinesetOwnershipResult, sessionId );
                } catch ( ParserConfigurationException e ) {
                    String msg = actionDescription + " : Unable to parse xml data file";
                    AfsCommonStaticClass.errorLog( actionDescription + msg + " ParserConfigurationException = " + e.getMessage(), e );
                    throw new TpmsException( msg, "Load Lineset Package", e );
                } catch ( SAXException e ) {
                    String msg = actionDescription + " : Unable to parse xml data file";
                    AfsCommonStaticClass.errorLog( actionDescription + msg + " SAXException = " + e.getMessage(), e );
                    throw new TpmsException( msg, "Load Lineset Package", e );
                } catch ( IOException e ) {
                    String msg = actionDescription + " : Unable to parse xml data file";
                    AfsCommonStaticClass.errorLog( actionDescription + msg + " IOException = " + e.getMessage(), e );
                    throw new TpmsException( msg, "Load Lineset Package", e );
                } finally {
                    //remove the lineset package file...
                    File linesetPackageFile = new File( linesetPackage.getFullyQualifiedIncomingFilePath() );
                    if ( getLinesetOwnershipResult != null && getLinesetOwnershipResult.packageFileCanBeDeleted() ) {
                        //if the loading package prosses has positive result delete the lineset package file...
                        try {
                            linesetPackageFile.delete();
                        } catch ( Exception e ) {
                            AfsCommonStaticClass.errorLog( actionDescription + " : Unable to delete lineset package file: " + e.getMessage(), e );
                        }
                    }
                }
            } else {
                String systemMessage = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_SYSTEM_MESSAGE );
                String userMessage = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_USER_MESSAGE );
                TpmsException e = new TpmsException( userMessage, "Load Lineset Package", systemMessage );
                AfsCommonStaticClass.errorLog( actionDescription + " : systemMessage = " + systemMessage + " - userMessage = " + userMessage, e );
                throw e;
            }
        }

        return new BackEndCallResult( actionCallResult, getLinesetOwnershipResult );
    }

    /**
     * Checks if the backend call made in order to send a lineset to a remote plant is terminated
     *
     * @param requestId
     * @param sessionId
     * @param lineset
     *
     * @return the result of the backend calls
     *
     * @throws TpmsException
     */
    public static BackEndCallResult checkSendLinesetResult ( String requestId, String sessionId, Lineset lineset ) throws TpmsException {
        String actionDescription = "LinesetVobManager :: checkSendLinesetResult";
        int actionCallResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
        String outFilePath = BackEndInterfaceUtils.buildOutFilePath( requestId );
        File outFile = new File( outFilePath );
        if ( outFile.exists() ) {
            //out file found ... BE call terminated
            Hashtable outFileContent;
            try {
                outFileContent = BackEndInterface.parseOutFile( new File( outFilePath ), lineset.getOwnerTpmsUser().getUnixUser(), sessionId );
            } catch ( IOException e ) {
                String msg = "There was an error parsing clearcase output file (" + outFilePath + ")";
                AfsCommonStaticClass.errorLog( actionDescription + " : requestId = " + requestId + " " + msg + " exception message = " + e.getMessage(), e );
                throw new TpmsException( msg, "outFilePath = " + outFilePath, e );
            }

            String ccCommandResult = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_RESULT );
            AfsCommonStaticClass.debugLog( actionDescription + " ccCommandResult = " + ccCommandResult );
            if ( ccCommandResult.equals( BackEndInterfaceConstants.OUT_RESULT_OK_VALUE ) ) {
                //the BE call is terminated with positive result.... let's check inside of xml file if there is any warning
                actionCallResult = BackEndInterfaceConstants.COMMAND_OK_RESULT;
            } else {
                String systemMessage = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_SYSTEM_MESSAGE );
                String userMessage = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_USER_MESSAGE );
                TpmsException e = new TpmsException( userMessage, "Get Lineset Ownership", systemMessage );
                AfsCommonStaticClass.errorLog( actionDescription + " : systemMessage = " + systemMessage + " - userMessage = " + userMessage, e );
                throw e;
            }
        }

        return new BackEndCallResult( actionCallResult, null );

    }

    /**
     * Checks if the action to get the lineset ownership is terminated and if the action is termianted parse the xml file that contains the data.
     *
     * @param requestId
     * @param sessionId
     * @param newOwner
     * @param dVob
     *
     * @return the result of the backend calls
     *
     * @throws TpmsException
     */
    public static BackEndCallResult checkGetLinesetOwnershipResult ( String requestId, String sessionId, TpmsUser newOwner, Vob dVob, TpmsUser currenTpmsUser ) throws TpmsException {
        String actionDescription = "LinesetVobManager :: checkGetLinesetOwnershipResult";
        int actionCallResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
        String outFilePath = BackEndInterfaceUtils.buildOutFilePath( requestId );
        File outFile = new File( outFilePath );
        GetLinesetOwnershipResult getLinesetOwnershipResult = null;
        if ( outFile.exists() ) {
            //out file found ... BE call terminated
            Hashtable outFileContent;
            try {
                outFileContent = BackEndInterface.parseOutFile( new File( outFilePath ), newOwner.getUnixUser(), sessionId );
            } catch ( IOException e ) {
                String msg = "There was an error parsing clearcase output file (" + outFilePath + ")";
                AfsCommonStaticClass.errorLog( actionDescription + " : requestId = " + requestId + " " + msg + " exception message = " + e.getMessage(), e );
                throw new TpmsException( msg, "outFilePath = " + outFilePath, e );
            }

            String ccCommandResult = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_RESULT );
            AfsCommonStaticClass.debugLog( actionDescription + " ccCommandResult = " + ccCommandResult );
            if ( ccCommandResult.equals( BackEndInterfaceConstants.OUT_RESULT_OK_VALUE ) ) {
                //the BE call is terminated with positive result.... let's check inside of xml file if there is any warning
                actionCallResult = BackEndInterfaceConstants.COMMAND_OK_RESULT;
                try {
                    getLinesetOwnershipResult = parseGetLinesetOwnershipXmlDataFile( requestId, dVob, dVob.getName() );

                    if ( getLinesetOwnershipResult.isOkResult() ) {
                        LinesetDBManager.updateLinesetOwner( getLinesetOwnershipResult, dVob, sessionId, currenTpmsUser );
                    }

                } catch ( ParserConfigurationException e ) {
                    String msg = actionDescription + " : Unable to parse xml data file";
                    AfsCommonStaticClass.errorLog( actionDescription + msg + " ParserConfigurationException = " + e.getMessage(), e );
                    throw new TpmsException( msg, "Get Lineset Ownership", e );
                } catch ( SAXException e ) {
                    String msg = actionDescription + " : Unable to parse xml data file";
                    AfsCommonStaticClass.errorLog( actionDescription + msg + " SAXException = " + e.getMessage(), e );
                    throw new TpmsException( msg, "Get Lineset Ownership", e );
                } catch ( IOException e ) {
                    String msg = actionDescription + " : Unable to parse xml data file";
                    AfsCommonStaticClass.errorLog( actionDescription + msg + " IOException = " + e.getMessage(), e );
                    throw new TpmsException( msg, "Get Lineset Ownership", e );
                }

            } else {
                String systemMessage = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_SYSTEM_MESSAGE );
                String userMessage = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_USER_MESSAGE );
                TpmsException e = new TpmsException( userMessage, "Get Lineset Ownership", systemMessage );
                AfsCommonStaticClass.errorLog( actionDescription + " : systemMessage = " + systemMessage + " - userMessage = " + userMessage, e );
                throw e;
            }
        }

        return new BackEndCallResult( actionCallResult, getLinesetOwnershipResult );
    }


    /*
    * check if is present the output file for the operation identified by the requestId:
    * If yes parse it in order to:
    * <li>track the action in the database and identify the operation result
    *   <li>valorize the list of Linesets.
    *   the result could be:
    * <li>COMMAND_OK_RESULT the clearcase command execution was ok
    * <li>COMMAND_KO_RESULT the clearcase command execution was ko
    * <li>COMMAND_UNKNOW_RESULT the clearcase command execution was unknow: the out file is not still present.
    * @param requestId
    * @param sessionId
    * @param currentUserLogin
    * @param selectedVobName
    * @param lsList
    * @return  <li>COMMAND_OK_RESULT the clearcase command execution was ok
    *          <li>COMMAND_KO_RESULT the clearcase command execution was ko
    *          <li>COMMAND_UNKNOW_RESULT the clearcase command execution was unknow: the out file is not still present.
    * @throws TpmsException in case of error
    */
    public static BackEndCallResult checkOtherLinesetQueryResult ( String requestId, String sessionId, String currentUserLogin, Vob selectedVob ) throws TpmsException {
        String actionDescription = "LinesetVobManager :: checkOtherLinesetQueryResult";
        int actionCallResult = BackEndInterfaceConstants.COMMAND_UNKNOW_RESULT;
        String outFilePath = BackEndInterfaceUtils.buildOutFilePath( requestId );
        File outFile = new File( outFilePath );
        LinesetList lsList = null;
        String unixUser = UserUtils.getUserUnixLogin( currentUserLogin );
        if ( outFile.exists() ) {
            //out file found ... BE call terminated
            Hashtable outFileContent;
            try {
                outFileContent = BackEndInterface.parseOutFile( new File( outFilePath ), unixUser, sessionId );
            } catch ( IOException e ) {
                String msg = "There was an error parsing clearcase output file";
                AfsCommonStaticClass.errorLog( actionDescription + " : requestId = " + requestId + " " + msg + " exception message = " + e.getMessage(), e );
                throw new TpmsException( msg, "outFilePath = " + outFilePath, e );
            }

            String ccCommandResult = ( String )outFileContent.get( BackEndInterfaceConstants.OUT_RESULT );
            AfsCommonStaticClass.debugLog( actionDescription + " ccCommandResult = " + ccCommandResult );
            if ( ccCommandResult.equals( BackEndInterfaceConstants.OUT_RESULT_OK_VALUE ) ) {
                actionCallResult = BackEndInterfaceConstants.COMMAND_OK_RESULT;
                //parse the xml result data file
                try {
                    AfsCommonStaticClass.debugLog( actionDescription + " populating ls List data" );

                    lsList = LinesetVobManager.parseOtherLinesetXmlDataFile( currentUserLogin, requestId, selectedVob );
                    AfsCommonStaticClass.debugLog( actionDescription + " population ls List data terminated " + lsList.size() );
                } catch ( ParserConfigurationException e ) {
                    String msg = actionDescription + "ParserConfigurationException Unable to parse xmlDataFile (" + BackEndInterfaceUtils.buildXmlOutFilePath( requestId ) + ")";
                    AfsCommonStaticClass.errorLog( msg, e );
                    throw new TpmsException( msg, actionDescription, e );
                } catch ( SAXException e ) {
                    String msg = actionDescription + "SAXException Unable to parse xmlDataFile (" + BackEndInterfaceUtils.buildXmlOutFilePath( requestId ) + ")";
                    AfsCommonStaticClass.errorLog( msg, e );
                    throw new TpmsException( msg, actionDescription, e );
                } catch ( IOException e ) {
                    String msg = actionDescription + "IOException Unable to parse xmlDataFile (" + BackEndInterfaceUtils.buildXmlOutFilePath( requestId ) + ")";
                    AfsCommonStaticClass.errorLog( msg, e );
                    throw new TpmsException( msg, actionDescription, e );
                }
            } else {
                actionCallResult = BackEndInterfaceConstants.COMMAND_KO_RESULT;
            }
        }
        return new BackEndCallResult( actionCallResult, lsList );
    }


    /**
     * writes the BE input file to retrieve the list of complete Ls contained in the current DVob
     *
     * @param dVob
     * @param xmlOutFile
     * @param sessionId
     * @param inBackEndFileName
     * @param unixUser
     *
     * @return true if the writing of input file succeded, false otherwise
     *
     * @throws IOException if there is an error while writing file
     */
    private static boolean writeOtherLsQueryBackEndInFile ( Vob dVob, String xmlOutFile, String sessionId, String inBackEndFileName, String unixUser ) throws IOException {
        Hashtable inFileParameters = new Hashtable();
        inFileParameters.put( "rectype", "query" );
        inFileParameters.put( "action", "ls_query" );
        inFileParameters.put( "debug", tpmsConfiguration.getCCDebugClearcaseInterfaceValue() );
        inFileParameters.put( "outfile", xmlOutFile );
        inFileParameters.put( "unixUser", unixUser );
        inFileParameters.put( "vobname", dVob.getName() );
        inFileParameters.put( "session_id", sessionId );
        inFileParameters.put( "filter2", "" );
        inFileParameters.put( "filter1", "READY" );
        inFileParameters.put( "ownerFilter", "" );
        return BackEndInterfaceUtils.writeInBackEndFile( inFileParameters, inBackEndFileName );
    }


    /**
     * writes the BE input file to call the lineset load action
     *
     * @param linesetPackage
     * @param destinationVob
     * @param actionTpmsUser
     * @param sessionId
     * @param inBackEndFileName
     * @param xmlDataFile
     *
     * @return
     *
     * @throws IOException
     */
    private static boolean writeLoadLinesetPackageBackEndInFile ( ReceivedLinesetPackage linesetPackage, Vob destinationVob, TpmsUser actionTpmsUser, String sessionId, String inBackEndFileName, String xmlDataFile ) throws IOException {
        Hashtable inFileParameters = new Hashtable();
        inFileParameters.put( "rectype", "lineset" );
        inFileParameters.put( "action", "ls_coop_download" );
        inFileParameters.put( "debug", tpmsConfiguration.getCCDebugClearcaseInterfaceValue() );
        inFileParameters.put( "session_id", sessionId );
        inFileParameters.put( "outfile", xmlDataFile );
        inFileParameters.put( "file_path", linesetPackage.getFullyQualifiedIncomingFilePath() );
        inFileParameters.put( "linesetname", linesetPackage.getLinesetName() );
        inFileParameters.put( "baseline", linesetPackage.getBaseline() );
        inFileParameters.put( "tester_info", linesetPackage.getTesterFamily() );
        inFileParameters.put( "vobname", destinationVob.getName() );
        inFileParameters.put( "to_plant", linesetPackage.getDestinationPlantId() );
        inFileParameters.put( "from_plant", linesetPackage.getSourcePlantId() );
        inFileParameters.put( "valid_login", linesetPackage.getDestinationTpmsLogin() );
        inFileParameters.put( "user", actionTpmsUser.getUnixUser() );
        inFileParameters.put( "work_dir", actionTpmsUser.getWorkDirectory() );
        inFileParameters.put( "unix_home", actionTpmsUser.getHomeDirectory() );

        return BackEndInterfaceUtils.writeInBackEndFile( inFileParameters, inBackEndFileName );
    }


    /**
     * writes the BE input file to call the action in order to get the ownership of lineset
     *
     * @param inBackEndFileName
     * @param xmlDataFile
     * @param currentTpmsUser
     * @param lineset
     * @param sessionId
     *
     * @return true if the writing of input file succeded, false otherwise
     *
     * @throws IOException
     */
    private static boolean writeChangeOwnershipBackEndInFile ( String inBackEndFileName, String xmlDataFile, TpmsUser currentTpmsUser, Lineset lineset, String sessionId ) throws IOException {
        Hashtable inFileParameters = new Hashtable();
        inFileParameters.put( "rectype", "lineset" );
        inFileParameters.put( "action", "ls_ch_owner" );
        inFileParameters.put( "debug", tpmsConfiguration.getCCDebugClearcaseInterfaceValue() );
        inFileParameters.put( "outfile", xmlDataFile );
        inFileParameters.put( "user", currentTpmsUser.getUnixUser() );
        inFileParameters.put( "linesetname", lineset.getName() );
        inFileParameters.put( "vobname", lineset.getVobName() );
        inFileParameters.put( "session_id", sessionId );
        inFileParameters.put( "unix_home", currentTpmsUser.getHomeDirectory() );
        inFileParameters.put( "to_mail", lineset.getOwnerTpmsUser().getEmail() );
        return BackEndInterfaceUtils.writeInBackEndFile( inFileParameters, inBackEndFileName );
    }

    /**
     * writes the BE input file to send lineset to a remote plant (cooperative development)
     *
     * @return true if the writing of input file succeded, false otherwise
     *
     * @throws IOException if there is an error while writing file
     */
    private static boolean writeSendLinesetBackEndInFile ( Lineset lineset, String xmlOutFile, String sessionId,
                                                           String inBackEndFileName, LinesetPackage linesetPackage,
                                                           String ccEmail ) throws IOException {
        Hashtable inFileParameters = new Hashtable();
        inFileParameters.put( "rectype", "lineset" );
        inFileParameters.put( "action", "ls_transfer_remote" );
        inFileParameters.put( "debug", tpmsConfiguration.getCCDebugClearcaseInterfaceValue() );
        inFileParameters.put( "outfile", xmlOutFile );
        inFileParameters.put( "linesetname", lineset.getName() );
        inFileParameters.put( "baseline", lineset.getBaseline() );
        inFileParameters.put( "vobname", lineset.getVobName() );
        inFileParameters.put( "session_id", sessionId );
        inFileParameters.put( "from_plant", tpmsConfiguration.getLocalPlant() );
        inFileParameters.put( "to_plant", linesetPackage.getDestinationPlantData().getTpmsInstallationId() );
        inFileParameters.put( "valid_login", linesetPackage.getDestinationTpmsUser().getName() );
        inFileParameters.put( "ip_address", linesetPackage.getDestinationPlantData().getServerAddress() );
        inFileParameters.put( "user", lineset.getOwner() );
        inFileParameters.put( "to_mail", linesetPackage.getDestinationTpmsUser().getName() );
        inFileParameters.put( "from_mail", lineset.getOwnerTpmsUser().getEmail() );
        inFileParameters.put( "cc_mail1", GeneralStringUtils.isEmptyString( ccEmail ) ? "" : ccEmail );
        inFileParameters.put( "cc_mail2", "" );

        /*
         String linesetName, String baseline, String testerFamily, String destinationPlantTpmsInstallationId,
         String destinationTpmsLogin, String originalOwnerTpmsLogin, String sourcePlantDataTpmsInstallationId,
         String sourceVobName

        */


        inFileParameters.put( "file_path", LinesetPackageManager.generatePackageFileName( lineset.getName(), lineset.getBaseline(), lineset.getTesterFamily(), linesetPackage.getDestinationPlantData().getTpmsInstallationId(),
                linesetPackage.getDestinationTpmsUser().getName(), lineset.getOwner(), linesetPackage.getSourcePlantData().getTpmsInstallationId(), lineset.getVobName() ) );
        return BackEndInterfaceUtils.writeInBackEndFile( inFileParameters, inBackEndFileName );
    }

}
