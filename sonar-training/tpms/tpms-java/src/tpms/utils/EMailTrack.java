package tpms.utils;


import it.txt.general.utils.FileUtils;
import it.txt.general.utils.GeneralStringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Date: 04-Feb-2008
  * The main purpose of this class is  to generate email track log.
 */
public class EMailTrack {

    private EMailTrack() {
    }
    public static boolean trackQuery(String sessionId, String userId, String strQuery) throws IOException {
        if (! GeneralStringUtils.isEmptyString(sessionId) && ! GeneralStringUtils.isEmptyString(userId)) {
            //to_date('04/02/2008 12.22.36', 'DD/MM/YYYY HH24:MI:SS')
            Date now = new Date();
           // strQuery = GeneralStringUtils.replaceAllIgnoreCase(strQuery, "SYSDATE", "TO_DATE('" + sdf.format(now) + "', '" + ORACLE_DATE_FORMAT + "')");
            return writeLog(buildEmailTrackFileName(sessionId, userId), strQuery);
        } else {
            return false;
        }
    }
    
    /**
     * this method encapsulte the logic to build the file name related to log file.
     * @param userId    login of the current user
     * @return the log file name
     */
    protected static String buildEmailTrackFileName(String sessionId, String userId) {
        String fileName = "";
        TpmsConfiguration tpmsCfg = TpmsConfiguration.getInstance();
        //build file name
        fileName = tpmsCfg.getLocalPlant()  + "_" + sessionId + "_" + userId + "_" +  System.currentTimeMillis() + ".log";
        //attach the file path to file name.
        fileName = tpmsCfg.getEmailTrackLogDir() + File.separator + fileName;
        it.txt.afs.AfsCommonStaticClass.debugLog("fileName : "+fileName);
        return fileName;
    }

    /**
     * this method write a string in a file. If the file doesn't exist will be created, if the file aready exists the
     * message will be appended to the file
     *
     * @param fileName the name of the file
     * @param message  the message that will inserted (appended or created) to the file
     * @return true if all the operation are well terminated, false otherwise
     * @throws IOException if a not manageable error occours (i.e. disk space)
     */
    protected static boolean writeLog(String fileName, String message) throws IOException {
        return FileUtils.writeToFile(fileName, message);
    }
}
