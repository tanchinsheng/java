package it.txt.integration.managers;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.integration.utils.RequestAttributesAndFieldsNames;
import it.txt.integration.utils.ResultDataFormatter;
import it.txt.tpms.backend.BackEndInterface;
import it.txt.tpms.backend.utils.BackEndInterfaceConstants;
import it.txt.tpms.backend.utils.BackEndInterfaceUtils;
import it.txt.tpms.tp.utils.TpDataValidator;
import org.xml.sax.SAXException;
import tpms.TpmsException;
import tpms.VobManager;
import tpms.utils.UserUtils;
import tpms.utils.Vob;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 3-lug-2006
 * Time: 16.57.54
 * This class contains the whole logic to let external tool to make tp queries, all the public methods MUST NOT RAISE
 * exception but in case of errors return a KO result
 */
public class TpQueriesManager extends AfsCommonStaticClass {

    /**
     * @param destinationPlantId
     * @param jobName
     * @param release
     * @param owner
     * @param status1
     * @param status2
     * @param sessionId
     * @return a StringBuffer containing xml data to be sent to the caller
     */
    public static StringBuffer retrieveTps(String destinationPlantId, String jobName, String release, String owner, String status1, String status2, String sessionId) {
        StringBuffer result;
        String actionDescription = "TP Query: ";
        if (GeneralStringUtils.isEmptyString(destinationPlantId)) {
            //plant ID is mandatory... if is null or empty raise an error...
            //ko no plant id given....return the message.
            return ResultDataFormatter.generateKoResult("Destination Plant ID is null or empty(" + RequestAttributesAndFieldsNames.TO_PLANT_FIELD + "), no operation will be performed ");
        } else {
            TpDataValidator tpDataValidator = new TpDataValidator();
            if (!GeneralStringUtils.isEmptyString(release) && !tpDataValidator.checkJobReleaseFormat(release)) {
                //ko release is not valid....return the message.
                return ResultDataFormatter.generateKoResult("The given release number is not valid (" + RequestAttributesAndFieldsNames.JOB_RELEASE_FIELD + " = " + release + ")");
            } else {
                //gather other needed infos:

                String requestId = BackEndInterfaceUtils.getRequestId(sessionId);
                String xmlDataFile = BackEndInterfaceUtils.buildXmlOutFilePath(requestId);
                String inBackEndFileName = BackEndInterfaceUtils.buildInFilePath(requestId);
                String outBackEndFileName = BackEndInterfaceUtils.buildOutFilePath(requestId);
                Vob tVob;
                try {
                    tVob = VobManager.searchTVobByDestinationPlant(destinationPlantId);
                } catch (Exception e) {
                    String msg = actionDescription + "Error while retrieving " + VobManager.T_VOB_TYPE_FLAG_VALUE + " vob information for " + destinationPlantId + " - " + e.getMessage();
                    return ResultDataFormatter.generateKoResult(msg, e);
                }
                if (tVob == null) {
                    String msg = actionDescription + "Unable to retrieve " + VobManager.T_VOB_TYPE_FLAG_VALUE + " vob information for " + destinationPlantId;
                    return ResultDataFormatter.generateKoResult(msg);
                }

                //write the BE input file
                boolean beInFileWrited;
                try {
                    //               writeBackEndInFile(String queryType, String beInFileName, String xmlDataFile, String vobName, String jobName, String release, String owner, String status1, String status2, String sessionId)
                    beInFileWrited = writeBackEndInFile("tp_query", inBackEndFileName, xmlDataFile, tVob.getName(), jobName, release, owner, status1, status2, sessionId);
                } catch (IOException e) {
                    String msg = actionDescription + "IOException while writing Back End input file ( " + inBackEndFileName + " ) - " + e.getMessage();
                    return ResultDataFormatter.generateKoResult(msg, e);
                }
                if (beInFileWrited) {
                    //the be input file was succesfully writed...lets go on with execution
                    //execute the action waiting for its termination
                    try {
                        BackEndInterface.invokeCommand(UserUtils.getAdminUnixLogin(), inBackEndFileName, outBackEndFileName, sessionId, true);
                    } catch (IOException e) {
                        String msg = actionDescription + "IOException while calling BE executable : " + e.getMessage();
                        return ResultDataFormatter.generateKoResult(msg, e);
                    }
                    //parse action result
                    File backEndOutFile = new File(outBackEndFileName);

                    if (backEndOutFile.exists()) {
                        Hashtable outFileContent;
                        try {
                            outFileContent = BackEndInterface.parseOutFile(backEndOutFile, UserUtils.getAdminUnixLogin(), sessionId);
                        } catch (IOException e) {
                            String msg = actionDescription + "IOException while parsing BE out file ( " + outBackEndFileName + " ) : " + e.getMessage();
                            return ResultDataFormatter.generateKoResult(msg, e);
                        }
                        String beCallResult = (String) outFileContent.get(BackEndInterfaceConstants.OUT_RESULT);
                        if (beCallResult.equals(BackEndInterfaceConstants.OUT_RESULT_OK_VALUE)) {
                            //parse XML data result
                            try {
                                //StringBuffer xmlData = FileUtils.readFile(xmlDataFile);
                                //if some filter are needed they should be applied to xmlData content.
                                //StringBuffer xmlData = null;
                                boolean ownerFilter = !GeneralStringUtils.isEmptyString(owner);
                                result = ResultDataFormatter.generateOkResult(BackEndInterfaceUtils.cleanAndFilterTpData(xmlDataFile, owner, ownerFilter));
                            } catch (FileNotFoundException e) {
                                String msg = actionDescription + "FileNotFoundException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (IOException e) {
                                String msg = actionDescription + "IOException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (ParserConfigurationException e) {
                                String msg = actionDescription + "ParserConfigurationException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (SAXException e) {
                                String msg = actionDescription + "SAXException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (TpmsException e) {
                                String msg = actionDescription + "TpmsException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            }
                        } else {
                            //the BE call generated a KO result... return the error
                            String sysMsg = (String) outFileContent.get(BackEndInterfaceConstants.OUT_SYSTEM_MESSAGE);
                            String msg = actionDescription + "Back End process reports the following error: " + sysMsg;
                            return ResultDataFormatter.generateKoResult(msg);
                        }
                    } else {
                        //error... the command exection is terminated BUT there is no out file
                        String msg = actionDescription + "Back End process is terminated but no output file was found ( " + outBackEndFileName + " )";
                        return ResultDataFormatter.generateKoResult(msg);
                    }
                } else {
                    //the be input file was NOT succesfully writed...stop the execution and return the error message
                    String msg = actionDescription + "General error while writing Back End input file ( " + inBackEndFileName + " )";
                    return ResultDataFormatter.generateKoResult(msg);
                }
            }
        }
        return result;
    }

    private static boolean writeBackEndInFile(String queryType, String beInFileName, String xmlDataFile, String vobName, String jobName, String release, String owner, String status1, String status2, String sessionId) throws IOException {

/*-- action=tp_history
-- rectype=query
-- debug=1
-- outfile=/export/home/vobadm/jakarta-tomcat-3.2.3/webapps/tpms/images/4dkneegk11_1151424221240_rep.xml
-- vobname=TEST_MASTER
-- session_id=4dkneegk11
-- jobname=testina
-- filter2=
-- filter1=
-- ownerFilter=vobadm
*/

        Hashtable inFileParameters = new Hashtable();
        inFileParameters.put("action", queryType);
        inFileParameters.put("rectype", "query");
        inFileParameters.put("debug", tpmsConfiguration.getCCDebugClearcaseInterfaceValue());
        inFileParameters.put("outfile", xmlDataFile);
        inFileParameters.put("vobname", vobName);
        inFileParameters.put("session_id", sessionId);

        if (! GeneralStringUtils.isEmptyString(jobName)) {
            inFileParameters.put("jobname", jobName);
        }
        if (!GeneralStringUtils.isEmptyString(release)) {
            inFileParameters.put("release_nb", release);
        }

        //questo filtro è stato riportano ma viene ignorato dal BE
        if (!GeneralStringUtils.isEmptyString(owner)) {
            inFileParameters.put("ownerFilter", owner);
        }
        if (!GeneralStringUtils.isEmptyString(status1)) {
            inFileParameters.put("filter1", status1);
        }
        if (!GeneralStringUtils.isEmptyString(status2)) {
            inFileParameters.put("filter2", status2);
        }

        return BackEndInterfaceUtils.writeInBackEndFile(inFileParameters, beInFileName);
    }


    public static StringBuffer retrieveTpHistory(String destinationPlantId, String jobName, String release, String ownerLoginFilter, String sessionId) {
        StringBuffer result = new StringBuffer();
        String actionDescription = "TP History : ";
        if (GeneralStringUtils.isEmptyString(destinationPlantId)) {
            //plant ID is mandatory... if is null or empty raise an error...
            //ko no plant id given....return the message.
            String msg = actionDescription + "Destination Plant ID is null or empty, ( " + RequestAttributesAndFieldsNames.TO_PLANT_FIELD + " ) no operation will be performed";
            return ResultDataFormatter.generateKoResult(msg);
        } else if (GeneralStringUtils.isEmptyString(jobName)) {
            //ko jobName is a mandatory... in this case is null or empty
            String msg = actionDescription + "The given job name is null or empty (" + RequestAttributesAndFieldsNames.JOB_NAME_FIELD + " = " + jobName + ")";
            return ResultDataFormatter.generateKoResult(msg);
        } else {
            TpDataValidator tpDataValidator = new TpDataValidator();
            if (!GeneralStringUtils.isEmptyString(release) && !tpDataValidator.checkJobReleaseFormat(release)) {
                //ko release is not valid....return the message.
                String msg = actionDescription + "The given release number is not valid (" + RequestAttributesAndFieldsNames.JOB_RELEASE_FIELD + " = " + release + ")";
                return ResultDataFormatter.generateKoResult(msg);
            } else {
                //ok the whole set of given parameters is good...lets start gathering other needed infos:
                String requestId = BackEndInterfaceUtils.getRequestId(sessionId);
                String xmlDataFile = BackEndInterfaceUtils.buildXmlOutFilePath(requestId);
                String inBackEndFileName = BackEndInterfaceUtils.buildInFilePath(requestId);
                String outBackEndFileName = BackEndInterfaceUtils.buildOutFilePath(requestId);
                Vob tVob = VobManager.searchTVobByDestinationPlant(destinationPlantId);
                if (tVob == null) {
                    String msg = actionDescription + "Unable to retrieve " + VobManager.T_VOB_TYPE_FLAG_VALUE + " vob information for " + destinationPlantId;
                    return ResultDataFormatter.generateKoResult(msg);
                }
                //write the BE input file
                boolean beInFileWrited;
                try {
                    beInFileWrited = writeBackEndInFile("tp_history", inBackEndFileName, xmlDataFile, tVob.getName(), jobName, release, ownerLoginFilter, null, null, sessionId);
                } catch (IOException e) {
                    String msg = actionDescription + "IOException while writing Back End input file ( " + inBackEndFileName + " ) " + e.getMessage();
                    return ResultDataFormatter.generateKoResult(msg, e);
                }
                if (beInFileWrited) {
                    //the be input file was succesfully writed...lets go on with execution
                    //execute the action waiting for its termination
                    try {
                        BackEndInterface.invokeCommand(UserUtils.getAdminUnixLogin(), inBackEndFileName, outBackEndFileName, sessionId, true);
                    } catch (IOException e) {
                        String msg = actionDescription + "IOException while calling BE executable : " + e.getMessage();
                        return ResultDataFormatter.generateKoResult(msg, e);
                    }
                    //parse action result
                    File backEndOutFile = new File(outBackEndFileName);

                    if (backEndOutFile.exists()) {
                        Hashtable outFileContent;
                        try {
                            outFileContent = BackEndInterface.parseOutFile(backEndOutFile, UserUtils.getAdminUnixLogin(), sessionId);
                        } catch (IOException e) {
                            String msg = actionDescription + "IOException while parsing BE out file ( " + outBackEndFileName + " ) : " + e.getMessage();
                            return ResultDataFormatter.generateKoResult(msg, e);
                        }
                        String beCallResult = (String) outFileContent.get(BackEndInterfaceConstants.OUT_RESULT);
                        if (beCallResult.equals(BackEndInterfaceConstants.OUT_RESULT_OK_VALUE)) {
                            //parse XML data result
                            try {
                                //if some filter are needed they should be applied to xmlData content.
                                //StringBuffer xmlData = null;
                                result = ResultDataFormatter.generateOkResult(BackEndInterfaceUtils.cleanAndFilterTpData(xmlDataFile, ownerLoginFilter, false));
                            } catch (FileNotFoundException e) {
                                String msg = actionDescription + "FileNotFoundException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (IOException e) {
                                String msg = actionDescription + "IOException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                errorLog("TpQueryManger :: retrieveTpHistory : " + msg, e);
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (ParserConfigurationException e) {
                                String msg = actionDescription + "ParserConfigurationException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (SAXException e) {
                                String msg = actionDescription + "SAXException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            } catch (TpmsException e) {
                                String msg = actionDescription + "TpmsException while reading BE xml data file ( " + xmlDataFile + " ) : " + e.getMessage();
                                return ResultDataFormatter.generateKoResult(msg, e);
                            }
                        } else {
                            //the BE call generated a KO result... return the error
                            String sysMsg = (String) outFileContent.get(BackEndInterfaceConstants.OUT_SYSTEM_MESSAGE);
                            String msg = actionDescription + "Back End process reports the following error: " + sysMsg;
                            return ResultDataFormatter.generateKoResult(msg);
                        }
                    } else {
                        //error... the command exection is terminated BUT there is no out file
                        String msg = actionDescription + "Back End process is terminated but no output file was found ( " + outBackEndFileName + " )";
                        return ResultDataFormatter.generateKoResult(msg);
                    }


                }

            }

        }
        return result;
    }


}
