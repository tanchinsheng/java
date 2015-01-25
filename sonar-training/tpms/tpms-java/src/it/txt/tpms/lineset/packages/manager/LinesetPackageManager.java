package it.txt.tpms.lineset.packages.manager;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.lineset.packages.list.ReceivedLinesetPackagesList;
import it.txt.tpms.lineset.packages.ReceivedLinesetPackage;
import it.txt.tpms.users.TpmsUser;
import tpms.TpmsException;
import tpms.utils.TpmsConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 19-ott-2006
 * Time: 10.17.31
 *
 */
public class LinesetPackageManager extends AfsCommonStaticClass {

    private static class LinesetPackageFileNameFilter implements FilenameFilter {
        /**
         * this class is usefull to filter only thse files that represent linese packages in a given directory
         */
        private TpmsUser tpmsUser = null;

        public LinesetPackageFileNameFilter() {
        }

        public LinesetPackageFileNameFilter(TpmsUser receiverTpmsUser) {
            this.tpmsUser = receiverTpmsUser;
        }

        public boolean accept(File dir, String name) {
            boolean result;
            File myFile = new File(dir.getAbsolutePath() + File.separator + name);
            //debugLog("***************************************LinesetPackageManager.LinesetPackageFileNameFilter.accept***************************************" );
            if ( tpmsUser != null ){
                //if the given user is not null filter by ownership (receiver or owner) also
                /*debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : tpmsUser is NOT null name=" + tpmsUser.getName() );
                debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : myFile.isFile() = " + myFile.isFile());
                debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : name.indexOf(LINESET_PACKAGE_INFO_SEPARATOR) >= 0 = " + (name.indexOf(LINESET_PACKAGE_INFO_SEPARATOR) >= 0));
                debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : name.startsWith(LINESET_PACKAGE_PREFIX >= 0 = " + (name.startsWith(LINESET_PACKAGE_PREFIX)));
                debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : name.indexOf(tpmsUser.getName()) >= 0 = " + (name.indexOf(tpmsUser.getName()) >= 0));
                debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : name.endsWith(LINESET_PACKAGE_FILE_EXTENSION) = " + name.endsWith(LINESET_PACKAGE_FILE_EXTENSION));
                debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : name.indexOf(tpmsConfiguration.getLocalPlant() >= 0 = " + (name.indexOf(tpmsConfiguration.getLocalPlant()) >= 0));*/
                result = ( myFile.isFile()  && name.indexOf(LINESET_PACKAGE_INFO_SEPARATOR) >= 0 && name.startsWith(LINESET_PACKAGE_PREFIX) && name.indexOf(tpmsUser.getName()) >= 0  && name.endsWith(LINESET_PACKAGE_FILE_EXTENSION) && name.indexOf(tpmsConfiguration.getLocalPlant()) >= 0);
            } else {
                //if the user is null do NOT filter by ownership
                //debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : tpmsUser is null" );
                result = ( myFile.isFile()  && name.indexOf(LINESET_PACKAGE_INFO_SEPARATOR) >= 0 && name.startsWith(LINESET_PACKAGE_PREFIX) && name.endsWith(LINESET_PACKAGE_FILE_EXTENSION) && name.indexOf(tpmsConfiguration.getLocalPlant()) >= 0);
            }
            debugLog("LinesetPackageManager.LinesetPackageFileNameFilter.accept : result is " + result + " name = " + name );
            debugLog("***************************************************************************************************************************************" );
            return result;
        }
    }




    protected static final String LINESET_PACKAGE_PREFIX = "lineset_package";
    protected static final String LINESET_PACKAGE_INFO_SEPARATOR = "@#@#";
    protected static final String LINESET_PACKAGE_FILE_EXTENSION = "tar";

    protected static final int LINESET_PACKAGE_FILENAME_FIELDS_COUNT = 8;

    private static TpmsConfiguration tpmsConfiguration = TpmsConfiguration.getInstance();


    public static String generatePackageFileName(String linesetName, String baseline, String testerFamily, String destinationPlantTpmsInstallationId,
                                                 String destinationTpmsLogin, String originalOwnerTpmsLogin, String sourcePlantDataTpmsInstallationId,
                                                 String sourceVobName) {

        //<PREFIX>@#@#<LINESET_NAME>@#@#<baseline>@#@#<tester_family>@#@#<SOURCE_VOB>@#@#<SOURCE_PLANT>@#@#<ORIGINAL_OWNER>@#@#<DESTINATION_PLANT>@#@#<destination_user>.<extension>

        return LINESET_PACKAGE_PREFIX + LINESET_PACKAGE_INFO_SEPARATOR + linesetName + LINESET_PACKAGE_INFO_SEPARATOR
                + baseline + LINESET_PACKAGE_INFO_SEPARATOR
                + testerFamily + LINESET_PACKAGE_INFO_SEPARATOR
                + sourceVobName + LINESET_PACKAGE_INFO_SEPARATOR
                + sourcePlantDataTpmsInstallationId + LINESET_PACKAGE_INFO_SEPARATOR
                + originalOwnerTpmsLogin + LINESET_PACKAGE_INFO_SEPARATOR
                + destinationPlantTpmsInstallationId + LINESET_PACKAGE_INFO_SEPARATOR
                + destinationTpmsLogin + "." + LINESET_PACKAGE_FILE_EXTENSION;
    }


    public static String generateFullyQualifiedIncomingPackageFilePath( ReceivedLinesetPackage linesetPackage ) {
        return tpmsConfiguration.getLinesetPackageIncomingDirectory() + File.separator + linesetPackage.getPackageFileName();
    }


    /**
     *
     * @param packageFileName package file name to be loaded
     * @return starting from a packageFileName return the representing LinesetPackage object
     * @throws TpmsException if an error happens during LinesetPackage object istantiation
     */
    public static ReceivedLinesetPackage getReceivedLinesetPackageFormPackageFileName( String packageFileName ) throws TpmsException {
        ReceivedLinesetPackage linesetPackage = null;
        if (!GeneralStringUtils.isEmptyTrimmedString(packageFileName)) {
            //let's remove any possible path info...
            String pathSeparator = File.separator;
            String packageFullFileName = packageFileName;
            debugLog("LinesetPackageManager.getReceivedLinesetPackageFormPackageFileName 0 : packageFileName = " + packageFileName);

            int lastPathSeparatorPos = packageFileName.lastIndexOf(pathSeparator);
            if ( lastPathSeparatorPos >= 0 ) {
                lastPathSeparatorPos++;
                packageFileName = packageFileName.substring(lastPathSeparatorPos);
            }

            debugLog("LinesetPackageManager.getReceivedLinesetPackageFormPackageFileName 1 : packageFileName = " + packageFileName);
            //let's remove any possible file extension
            int lastDotPos = packageFileName.lastIndexOf(".");
            if (lastDotPos >= 0) {
                packageFileName = packageFileName.substring(0, lastDotPos);
            }
            debugLog("LinesetPackageManager.getReceivedLinesetPackageFormPackageFileName 2 : packageFileName = " + packageFileName);
            //let's remove the package file prefix
            packageFileName = packageFileName.replaceAll(LINESET_PACKAGE_PREFIX + LINESET_PACKAGE_INFO_SEPARATOR, "");
            debugLog("LinesetPackageManager.getReceivedLinesetPackageFormPackageFileName 3 : packageFileName = " + packageFileName);
            //let's parse the remeining string and instantiate the LinesetPackage that will be returned

            StringTokenizer st = new StringTokenizer(packageFileName, LINESET_PACKAGE_INFO_SEPARATOR);
            debugLog("LinesetPackageManager.getReceivedLinesetPackageFormPackageFileName 4 : packageFileName = " + packageFileName);

            if (st.countTokens() != LINESET_PACKAGE_FILENAME_FIELDS_COUNT){
                throw new TpmsException("The given package file seems not to be a package file! (" + packageFileName + ")", "LinesetPackageManager :: getReceivedLinesetPackageFormPackageFileName", "The given package file seems not a Linset package file");
            }

            //<PREFIX>@#@#<LINESET_NAME>@#@#<baseline>@#@#<tester_family>@#@#<SOURCE_VOB>@#@#<SOURCE_PLANT>@#@#<ORIGINAL_OWNER>@#@#<DESTINATION_PLANT>@#@#<destination_user>.<extension>
            String linesetName = st.nextToken();
            String baseline = st.nextToken();
            String testerFamily = st.nextToken();
            String sourceVobName = st.nextToken();
            String sourcePlantTpmsId = st.nextToken();
            String ownerTpmsLogin = st.nextToken();
            String destinationPlantTpmsId = st.nextToken();
            String destinationUserTpmsLogin = st.nextToken();
            //String linesetName, String baseline, String destinationPlantId, String destinationTpmsLogin, String originalOwnerTpmsLogin, String sourcePlantId, String sourceVobName, String testerFamily, String packageFileName
            debugLog("LinesetPackageManager.getReceivedLinesetPackageFormPackageFileName 0 : linesetName = " + linesetName + " baseline = " + baseline + " destinationPlantTpmsId = " + destinationPlantTpmsId +
                    " destinationUserTpmsLogin = " + destinationUserTpmsLogin + " ownerTpmsLogin = " + ownerTpmsLogin + " sourcePlantTpmsId = " + sourcePlantTpmsId + " vobName = " + sourceVobName);
            linesetPackage = new ReceivedLinesetPackage(linesetName, baseline, destinationPlantTpmsId, destinationUserTpmsLogin, ownerTpmsLogin, sourcePlantTpmsId, sourceVobName, testerFamily, packageFullFileName);
        }
        return linesetPackage;
    }


    /**
     * this method returns the list of recieved lineset packages by receiverTpmsUSer
     * @param receiverTpmsUser  used to filter lineset packages
     * @return the list of recieved lineset packages by receiverTpmsUSer
     * @throws TpmsException in case of errors
     */
    public static ReceivedLinesetPackagesList getReceivedLinesetPackagesList ( TpmsUser receiverTpmsUser ) throws TpmsException{
        File incomingBay = new File(tpmsConfiguration.getLinesetPackageIncomingDirectory());
        String commonErrorMessage = "LinesetPackageManager :: getLinesetPackagesList";

        if (!incomingBay.exists()) {
            throw new TpmsException("Linset packages incoming directory does not exists", commonErrorMessage, "The configured directory does not exists: check with your administrator TPMS configuration (in web.xml look for lineset.packages.incoming.bay)");
        }
        if (!incomingBay.isDirectory()) {
            throw new TpmsException("Linset packages incoming bay is not a directory", commonErrorMessage, "The configured lineset packages incoming bay is not a directory: check with your administrator TPMS configuration (in web.xml look for lineset.packages.incoming.bay)");
        }
        if (receiverTpmsUser == null) {
            throw new TpmsException("The given user is null: unable to retrieve lineset packages list", commonErrorMessage, "The given user is null: unable to retrieve lineset packages list");
        }

        LinesetPackageFileNameFilter fileFilter = new LinesetPackageFileNameFilter(receiverTpmsUser);
        return getLinesetPackagesList(fileFilter, incomingBay);
    }


    /**
     * this is a general method that return the list of linest packages that are present in a given directoy applying the given filter
     * @param fileFilter FilenameFilter istance to be used in order to get only intresting lineset packages file for the current user
     * @param linesetPackagesDirectory  the directory to look for lineset packages
     * @return the list of lineset packages that are present in given directoy filtering content using the given file filter
     * @throws TpmsException in case of invalid input parameters
     */
    private static ReceivedLinesetPackagesList getLinesetPackagesList(FilenameFilter fileFilter, File linesetPackagesDirectory) throws TpmsException {
        ReceivedLinesetPackagesList linesetPackagesList = new ReceivedLinesetPackagesList();
        String commonErrorMessage = "LinesetPackageManager :: getLinesetPackagesList";

        if (linesetPackagesDirectory == null) {
            throw new TpmsException("Linset packages directory is null", commonErrorMessage, "The given directory is null: check with your administrator TPMS configuration");
        }

        if (!linesetPackagesDirectory.exists()) {
            throw new TpmsException("Linset packages directory does not exists", commonErrorMessage, "The given directory does not exists: check with your administrator TPMS configuration");
        }

        if (!linesetPackagesDirectory.isDirectory()) {
            throw new TpmsException("Configured Linset packages path is not a directory", commonErrorMessage, "Configured Linset packages path is not a directory: check with your administrator TPMS configuration");
        }
        File[] packagesFilesList = linesetPackagesDirectory.listFiles(fileFilter);
        if (packagesFilesList != null) {
            debugLog(commonErrorMessage + ": packagesFilesList.length = " + packagesFilesList.length) ;
        }

        if (packagesFilesList != null && packagesFilesList.length > 0) {
            for ( int i = 0; i < packagesFilesList.length; i++ ) {
                debugLog(commonErrorMessage + ": adding package "+ packagesFilesList[i].getName() + " to list") ;
                //add the current file to the list
                linesetPackagesList.addElement(getReceivedLinesetPackageFormPackageFileName(packagesFilesList[i].getName()));
            }
        }
        return linesetPackagesList;
    }

}
