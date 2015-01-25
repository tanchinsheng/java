package it.txt.tpms.lineset.accessibility;

import it.txt.afs.AfsCommonClass;
import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.FileUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 4-ott-2006
 * Time: 12.25.59
 * this class is the interface that permit to access the content of lineset accessibility configuration file.
 */
public class LinesetsAccessibilityData extends AfsCommonClass {

    private static LinesetsAccessibilityData singlethon = null;

    private Hashtable linesetsUsersAccessibilityData = new Hashtable();

    private final String linesetAccessibilityConfigurationFilePath = tpmsConfiguration.getCommonConfigurationFilesAbsolutePath() + File.separator + "lineset_access.xml";
    //private final String linesetAccessibilityConfigurationFilePath = "D:\\jakarta-tomcat-3.2.3\\webapps\\tpms51.COOPERATIVE_DEVELOPMENT\\cfg\\local_cfg\\lineset_access.xml";

    private LinesetsAccessibilityData () {
        Element linesetAccessibilityRoot = null;
        try {
            linesetAccessibilityRoot = XmlUtils.getRoot( linesetAccessibilityConfigurationFilePath );
        } catch ( ParserConfigurationException e ) {
            errorLog( "LinesetsAccessibilityData :: constructor : (ParserConfigurationException) Unable to Parse lineset accessibility configuration file (" + linesetAccessibilityConfigurationFilePath + ")" + e.getMessage(), e );
        } catch ( SAXException e ) {
            errorLog( "LinesetsAccessibilityData :: constructor : (SAXException) Unable to Parse lineset accessibility configuration file (" + linesetAccessibilityConfigurationFilePath + ")" + e.getMessage(), e );
        } catch ( IOException e ) {
            errorLog( "LinesetsAccessibilityData :: constructor : (IOException) Unable to access lineset accessibility configuration file (" + linesetAccessibilityConfigurationFilePath + ")" + e.getMessage(), e );
        }

        if ( linesetAccessibilityRoot != null ) {
            NodeList nlLinesetAccessData = linesetAccessibilityRoot.getElementsByTagName( LinesetAccessibilityConstants.LS_TAG );
            if ( nlLinesetAccessData != null ) {
                int lsAccessDataCount = nlLinesetAccessData.getLength();
                Element oneLinesetAccessData;
                OneLinesetAccessData tmpOneLinesetAccessData;
                for ( int i = 0; i < lsAccessDataCount; i++ ) {
                    oneLinesetAccessData = ( Element )nlLinesetAccessData.item( i );
                    tmpOneLinesetAccessData = new OneLinesetAccessData( oneLinesetAccessData );
                    linesetsUsersAccessibilityData.put( generateAccessibilityListKey( tmpOneLinesetAccessData.getLinesetName(), tmpOneLinesetAccessData.getVobName() ), tmpOneLinesetAccessData );
                }
            }
        }
    }

    /**
     * generates the key of accessibility data
     * @param linesetName  used in order to produce the key
     * @param vobName used in order to produce the key
     * @return generates the key of accessibility data
     */
    private static String generateAccessibilityListKey ( String linesetName, String vobName ) {
        return linesetName + "_" + vobName;
    }


    public static LinesetsAccessibilityData getInstance () {
        if ( singlethon == null ) {
            singlethon = new LinesetsAccessibilityData();
        }
        return singlethon;
    }

    /**
     * Force the lineset accessibility data to be reloaded:
     * use with care this action is really time consuming!!!
     */
    public static void reloadLinesetAccessibiltyData () {
        singlethon = null;
        singlethon = new LinesetsAccessibilityData();
    }

    /**
     * @param userLogin
     * @param linesetName
     * @param vobName
     *
     * @return returns true if and only if the user identified by userLogin can get the lineset (linesetName) contained in given VOB (vobName)
     */
    public boolean userCanGetLineset ( String userLogin, String linesetName, String vobName ) {
        if ( GeneralStringUtils.isEmptyTrimmedString( userLogin ) || GeneralStringUtils.isEmptyTrimmedString( linesetName ) || GeneralStringUtils.isEmptyTrimmedString( vobName ) ) {
            return false;
        }
        OneLinesetAccessData lsAccessData = ( OneLinesetAccessData )linesetsUsersAccessibilityData.get( generateAccessibilityListKey( linesetName, vobName ) );
        if ( lsAccessData == null ) {
            return false;
        } else {
            return lsAccessData.isUserAccessAllowed( userLogin );
        }
    }

    /**
     * return the lineset accessibility informations
     * @param linesetName used in order to identify the lineset
     * @param vobName used in order to identify the lineset
     * @return accessibility informations related to the given lineset.
     */
    public OneLinesetAccessData getLinesetAccessData ( String linesetName, String vobName ) {
        OneLinesetAccessData thisLsAccessData = null;
        if ( !GeneralStringUtils.isEmptyTrimmedString( linesetName ) && !GeneralStringUtils.isEmptyTrimmedString( vobName ) ) {
            thisLsAccessData = ( OneLinesetAccessData )linesetsUsersAccessibilityData.get( generateAccessibilityListKey( linesetName, vobName ) );
        }
        return thisLsAccessData;
    }



    /**
     * add changes to lineset accessibility data and dump it to the cfg file
     * @param lsAccessData to be updated
     * @throws IOException if an error happens during files management
     */
    public void setLinesetAccessData ( OneLinesetAccessData lsAccessData ) throws IOException {

        String commonErrorMessage = "LinesetAccessibilityData :: setLinesetAccessData";
        if ( lsAccessData != null ) {
            Hashtable tmpAccessData = new Hashtable( linesetsUsersAccessibilityData );
            tmpAccessData.put( generateAccessibilityListKey( lsAccessData.getLinesetName(), lsAccessData.getVobName() ), lsAccessData );
            //dumpo tutto su file tmp
            StringBuffer xmlAccessibilityData = new StringBuffer( XmlUtils.getOpenTagString( LinesetAccessibilityConstants.LS_ACCESSIBILITY_TAG ) );
            Enumeration accessibilityKeys = tmpAccessData.keys();
            Object currentKey;
            OneLinesetAccessData tmpOneLsAccessData;
            ArrayList tmpAllowedUsersLogins;
            int allowedLoginsSize;
            while ( accessibilityKeys.hasMoreElements() ) {
                currentKey = accessibilityKeys.nextElement();
                tmpOneLsAccessData = ( OneLinesetAccessData )tmpAccessData.get( currentKey );
                if ( tmpOneLsAccessData != null ) {
                    xmlAccessibilityData.append( XmlUtils.getOpenTagString( LinesetAccessibilityConstants.LS_TAG ) );
                    xmlAccessibilityData.append( XmlUtils.getLeafElementAsString( LinesetAccessibilityConstants.LS_NAME_TAG, null, tmpOneLsAccessData.getLinesetName() ) );
                    xmlAccessibilityData.append( XmlUtils.getLeafElementAsString( LinesetAccessibilityConstants.LS_VOB_NAME_TAG, null, tmpOneLsAccessData.getVobName() ) );
                    xmlAccessibilityData.append( XmlUtils.getLeafElementAsString( LinesetAccessibilityConstants.LS_CREATOR_TAG, null, tmpOneLsAccessData.getLinesetCreatorLogin() ) );
                    xmlAccessibilityData.append( XmlUtils.getOpenTagString( LinesetAccessibilityConstants.ALLOWED_USERS_LOGINS_TAG ) );
                    tmpAllowedUsersLogins = tmpOneLsAccessData.getAllowedUsersLogin();
                    allowedLoginsSize = tmpAllowedUsersLogins.size();
                    for ( int i = 0; i < allowedLoginsSize; i++ ) {
                        xmlAccessibilityData.append( XmlUtils.getLeafElementAsString( LinesetAccessibilityConstants.USER_LOGIN_TAG, null, ( String )tmpAllowedUsersLogins.get( i ) ) );
                    }
                    xmlAccessibilityData.append( XmlUtils.getCloseTagString( LinesetAccessibilityConstants.ALLOWED_USERS_LOGINS_TAG ) );
                    xmlAccessibilityData.append( XmlUtils.getCloseTagString( LinesetAccessibilityConstants.LS_TAG ) );
                }
            }
            xmlAccessibilityData.append( XmlUtils.getCloseTagString( LinesetAccessibilityConstants.LS_ACCESSIBILITY_TAG ) );
            String tmpLsAccessDataFile = linesetAccessibilityConfigurationFilePath + System.currentTimeMillis();
            try {
                FileUtils.writeToFile( tmpLsAccessDataFile, xmlAccessibilityData.toString() );
            } catch ( IOException e ) {
                AfsCommonStaticClass.errorLog( commonErrorMessage + " : IOException while writing temporary file " + e.getMessage(), e );
                throw e;
            }
            try {
                FileUtils.copy( tmpLsAccessDataFile, linesetAccessibilityConfigurationFilePath );
                linesetsUsersAccessibilityData = null;
                linesetsUsersAccessibilityData = new Hashtable( tmpAccessData );
                ( new File( tmpLsAccessDataFile ) ).delete();
            } catch ( IOException e ) {
                AfsCommonStaticClass.errorLog( commonErrorMessage + " : IOException coping tmp file to official one " + e.getMessage(), e );
                throw e;
            }
        }
    }
}



