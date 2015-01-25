package tpms.utils;


import it.txt.general.utils.FileUtils;
import it.txt.general.utils.GeneralStringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import it.txt.afs.AfsCommonStaticClass;
/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 28-set-2005
 * Time: 11.21.56
 * The main purpose of this class is  to centralize the threat of the db track log.
 */
public class DBTrack extends AfsCommonStaticClass {

    private static final String ORACLE_DATE_FORMAT = "DD/MM/YYYY HH24:MI:SS";
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);


    private DBTrack() {
        //the creator is private to force all the caller to do not instantite the class
    }

    /**
     * This method is intended to produce a log file that contains a query.
     *
     * @param sessionId session id of the current user
     * @param userId    login of the current user
     * @param strQuery  query that will be logged in to log file
     * @return true if all the process it's ok, false otherwise:
     *         <b>PAY ATTENTION: this method return false also if sessionId or userId are null or empty</b>
     * @throws IOException if a not manageable error occours (i.e. disk space)
     */
    public static boolean trackQuery(String sessionId, String userId, String strQuery) throws IOException {
    	if (! GeneralStringUtils.isEmptyString(sessionId) && ! GeneralStringUtils.isEmptyString(userId)) {
            //to_date('10/01/2006 12.22.36', 'DD/MM/YYYY HH24:MI:SS')
            //replace of the oracle variable sysdate with the actual value in order to simplify the administrator work to re-allign the database.
            //here we do not matter about the kind the sql statement (SELECT, UPDATE, INSERT, DELETE)
        	Date now = new Date();
        	sendMail(sessionId,userId,strQuery);
        	strQuery = GeneralStringUtils.replaceAllIgnoreCase(strQuery, "SYSDATE", "TO_DATE('" + sdf.format(now) + "', '" + ORACLE_DATE_FORMAT + "')");
            return writeLog(buildDBTrackFileName(sessionId, userId), strQuery);
        } else {
            return false;
        }
        
    }
    //to_date('28/02/2008', 'DD/MM/YYYY')
    //To send email to administrator about the sql statement not (insert,update,delete)
    public static boolean sendMail(String sessionId, String userId, String strQuery){  
    	StringBuffer contentBuffer = new StringBuffer();
    	try {
	        TpmsConfiguration tpmsCfg = TpmsConfiguration.getInstance();
	        String toAddress = tpmsCfg.getSupportMail();
	        String fromAddress = tpmsCfg.getMailFromAddress();
			String mailHost = tpmsCfg.getMailServerName(); 
	    	String subject = tpmsCfg.getLocalPlant() + ": Database connection error";
	    	Vector toAddresses = new Vector();
			toAddresses.add(toAddress);
//			debugLog("DBTrack :: sendMail: supportMail: " + toAddress);
			toAddress = tpmsCfg.getSupportMail2();
			if(toAddress != null && "".equals(toAddress) == false) {
				toAddresses.add(toAddress);
//				debugLog("DBTrack :: sendMail: supportMail2: " + toAddress);
			}
			toAddress = tpmsCfg.getSupportMail3();
			if(toAddress != null && "".equals(toAddress) == false) {
				toAddresses.add(toAddress);
//				debugLog("DBTrack :: sendMail: supportMail3: " + toAddress);
			}
			contentBuffer.append("\n User Name :");
	     	contentBuffer.append(userId);
	     	contentBuffer.append("\n Local Plant :");
	     	contentBuffer.append(tpmsCfg.getLocalPlant());
	     	contentBuffer.append("\n Session_Id :");
	     	contentBuffer.append(sessionId);
	       	contentBuffer.append("\n File Name :");
	     	contentBuffer.append(tpmsCfg.getLocalPlant() + "_" + sessionId + "_" + userId + "_" + System.currentTimeMillis() + ".log");
	 		contentBuffer.append("\n\n There was an error tracking in database \n\n");
	 		contentBuffer.append(strQuery);
	 		contentBuffer.append("\n\n Time Date : ");
			contentBuffer.append(new Date());
			String content = contentBuffer.toString();
			if (!fromAddress.equals("") && (!toAddresses.equals(""))){
	        	MailUtils.sendMail(mailHost, fromAddress, toAddresses, subject, content);
	        	debugLog("DBTrack :: sendMail : Email sent with subject: " + subject);
			}
			return true;
	    } catch (Exception e) {
	    	   errorLog("MailUtils :: sendMail : strQuery = " + strQuery);
	           errorLog("MailUtils :: sendMail : error while sending mail: " + e.getMessage(),e);	    	   
	           e.printStackTrace();
	           return false;
	    } finally {
			try	{
				if (contentBuffer != null) {
					contentBuffer = null;
				}
			} catch (Exception e){
				errorLog("MailUtils :: sendMail : Fail to delocate contentBuffer!");
			}
	    }
    } 
	/**
     * The same as trackQuery but keeps in input an array of queries: a log file will be created for each query
     *
     * @param sessionId  session id of the current user
     * @param userId     login of the current user
     * @param strQueries the array of queries that will be logged in to log files
     * @return true if all the process it's ok, false otherwise:
     *         <b>PAY ATTENTION: this method return false also if sessionId or userId are null or empty</b>
     * @throws IOException if a not manageable error occours (i.e. disk space)
     */
    public static boolean trackQueries(String sessionId, String userId, String[] strQueries) throws IOException {
        boolean result = true;

        if (strQueries != null && strQueries.length > 0 && ! GeneralStringUtils.isEmptyString(sessionId) &&
                ! GeneralStringUtils.isEmptyString(userId)) {
            for (int i = 0; i < strQueries.length; i++) {
                result = result && trackQuery(sessionId, userId, strQueries[i]);
            }
        } else {
            return false;
        }
        return result;
    }

    /**
     * this method encapsulte the logic to build the file name related to log file.
     *
     * @param sessionId session id of the current user
     * @param userId    login of the current user
     * @return the log file name
     */
    protected static String buildDBTrackFileName(String sessionId, String userId) {
        String fileName = "";
        TpmsConfiguration tpmsCfg = TpmsConfiguration.getInstance();
        //build file name
        fileName = tpmsCfg.getLocalPlant() + "_" + sessionId + "_" + userId + "_" + System.currentTimeMillis() + ".log";
        //attach the file path to file name.
        fileName = tpmsCfg.getDbTrackLogDir() + File.separator + fileName;
        debugLog("DBTrack :: buildDBTrackFileName : fileName = "+fileName);
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
