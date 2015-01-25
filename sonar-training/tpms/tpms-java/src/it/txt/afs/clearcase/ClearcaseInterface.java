package it.txt.afs.clearcase;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.afs.clearcase.utils.ClearcaseInterfaceCostants;
import it.txt.general.utils.GeneralStringUtils;
import tpms.CtrlServlet;
import tpms.utils.DBTrack;
import tpms.utils.QueryUtils;
import java.io.*;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * Classname : ClearcaseInterface
 * 
 * @version :	30-gen-2006
 *  
 * @author :	furgiuele
 */
public class ClearcaseInterface extends AfsCommonStaticClass {
	
    /* Constants represent the clearcase out file fields */
    protected static final String TRACK_LABEL = "track=";
    protected static final String SYSTEM_MESSAGE_LABEL = "usermsg=";
    protected static final String USER_MESSAGE_LABEL = "sysmsg=";
    protected static final String OK_RESULT = "esit=0";
    protected static final String KO_RESULT = "esit=1";

    /**
     * this method build the clearcase command
     *
     * @param remoteUser  the unix user associated with the current user
     * @param inFilePath  clearcase input file path & name
     * @param outFilePath clearcase out file path & name
     *
     * @return the clearcase command
     */
    private static String buildClearcaseCommand(String remoteUser, String inFilePath, String outFilePath) {
        String result;
        
        if (tpmsConfiguration.getDvlBool()) {
            result = "D:\\bat\\ccSimulator\\emptyCCCommand.bat D:\\bat\\ccSimulator\\test.txt";
        } else {
            result = tpmsConfiguration.getUnixScriptExecMode() + " " + 
            		tpmsConfiguration.getClearcaseCommandExecutionHost() + " -n -l " + remoteUser
                    + " " + tpmsConfiguration.getCcScriptsDir() + File.separator + 
                    CtrlServlet._tpmsShellScriptName + " " + inFilePath + " " + outFilePath;
        }
        debugLog("ClearcaseInterface :: buildClearcaseCommand : " + result);
        return result;
    }


    /**
     * this method is called in order to wait the end of cc command
     *
     * @param outFilePath the path to the out file: (if present the action is finished)
     *
     * @return a file representing the out file of cc command.
     */
    private static File waitOutFile(String outFilePath) {
        long timeOut = tpmsConfiguration.getAfsTimeOut();
        File outFile = new File(outFilePath);
        
        /* se il file non esiste o devo aspettare il risultato....*/
        if (!outFile.exists()) {
            for (long i = 0; i < timeOut * 2; i++) {
                if (outFile.exists()) {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return outFile;
    }


    /**
     * parse the cc command out file and build and hashtable with the content, and insert trak data into database
     *
     * @param outFile   it's the out file of the cc command that should be parsed
     * @param unixUser  the unix login of the user that is performing the action
     * @param sessionId of the user that iscurrently connected to the web application
     *
     * @return an hashtable containing:
     *         <li>OUT_RESULT it's the result of overall calls: this can assume OUT_RESULT_OK_VALUE if the result is ok
     *         OUT_RESULT_KO_VALUE otherwise
     *         <li>OUT_SYSTEM_MESSAGE it's the system message from cc
     *         <li>OUT_USER_MESSAGE it's the user message from cc
     *         <li>OUT_TRACK_MESSAGE it's the track string for the result
     *
     * @throws IOException - If an I/O error occurs
     */
    public static Hashtable parseOutFile(File outFile, String unixUser, String sessionId) throws IOException {
    	Hashtable result = new Hashtable();
    	
        debugLog("ClearcaseInterface :: parseOutFile : start ..." + outFile.getAbsolutePath());
        if (outFile.exists()) {
            result.put(ClearcaseInterfaceCostants.OUT_RESULT, "");
            result.put(ClearcaseInterfaceCostants.OUT_SYSTEM_MESSAGE, "");
            result.put(ClearcaseInterfaceCostants.OUT_USER_MESSAGE, "");
            result.put(ClearcaseInterfaceCostants.OUT_TRACK_MESSAGE, "");

            /* parsa il file */
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(outFile));
            } catch (FileNotFoundException e) {
            /* in questa parte del codice non dovrei mai passare visti i controlli fatti in precedenza. */
            }
            String oneFileRow;

            while (in != null && (oneFileRow = in.readLine()) != null) {
                if (oneFileRow.startsWith(TRACK_LABEL)) {
                    result.put(ClearcaseInterfaceCostants.OUT_TRACK_MESSAGE, oneFileRow.substring(TRACK_LABEL.length()));
                }
                for (int i = 0; i < 3; i++) {
                    if (oneFileRow.equals(OK_RESULT)) {
                        result.put(ClearcaseInterfaceCostants.OUT_RESULT, ClearcaseInterfaceCostants.OUT_RESULT_OK_VALUE);
                        break;// stop for execution beause the result is ok and the other lines will not be present.
                    } else if (oneFileRow.equals(KO_RESULT)) {
                        result.put(ClearcaseInterfaceCostants.OUT_RESULT, ClearcaseInterfaceCostants.OUT_RESULT_KO_VALUE);
                    }
                    if (oneFileRow.startsWith(SYSTEM_MESSAGE_LABEL))
                        result.put(ClearcaseInterfaceCostants.OUT_SYSTEM_MESSAGE, oneFileRow.substring(SYSTEM_MESSAGE_LABEL.length()));
                    if (oneFileRow.startsWith( USER_MESSAGE_LABEL))
                        result.put(ClearcaseInterfaceCostants.OUT_USER_MESSAGE, oneFileRow.substring(USER_MESSAGE_LABEL.length()));
                }
            }
            if (in != null) in.close();
        } else {
            errorLog("ClearcaseInterface :: parseOutFile : the given outFile does not exists...probably somesone deleted it!!!");
        }       
    return result; 
   }


    /**
     * This method starting from the track message from cc will track the action in database
     *
     * @param trackMsg  the message from cc
     * @param sessionId the session in
     * @param userId    user id
     */
    protected static void trackAction(String trackMsg, String sessionId, String userId) throws Exception {

        if (!GeneralStringUtils.isEmptyString(trackMsg)) {
            boolean commitBool = false;
            final String trackMsgSeparator = "##";

            debugLog( "TRACK ACTION DB STARTED" );
            int index1 = 0;
            int index2 = trackMsg.indexOf( trackMsgSeparator, index1 );

            String esit = trackMsg.substring( index1, index2 );

            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String err_msg = trackMsg.substring( index1, index2 );

            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String intallation_id = trackMsg.substring( index1, index2 );

            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String start_date = trackMsg.substring( index1, index2 );

            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String vob_name = trackMsg.substring( index1, index2 );

            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String actor = trackMsg.substring( index1, index2 );
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String session_id = trackMsg.substring( index1, index2 );
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String rec_type = trackMsg.substring( index1, index2 );
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String action = trackMsg.substring( index1, index2 );
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String entry_name = trackMsg.substring( index1, index2 );
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String att_1 = trackMsg.substring( index1, index2 );
            index1 = index2 + trackMsgSeparator.length();
            index2 = trackMsg.indexOf( trackMsgSeparator, index1 );
            String att_2 = trackMsg.substring( index1, index2 );
            index1 = index2 + trackMsgSeparator.length();
            String end_date = trackMsg.substring( index1 );

            String query = "INSERT INTO TRACK_DETAILS " +
                    "( " +
                    " ESIT, " +
                    " ERR_MSG, " +
                    " INSTALLATION_ID, " +
                    " START_DATE, " +
                    " VOB_NAME, " +
                    " ACTOR, " +
                    " SESSION_ID, " +
                    " REC_TYPE," +
                    " ACTION," +
                    " ENTRY_NAME," +
                    " ATT_1," +
                    " ATT_2," +
                    " END_DATE" +
                    ") " +
                    "VALUES " +
                    "( '" +
                    QueryUtils.duplicateQuotes( esit ) + "','" +
                    QueryUtils.duplicateQuotes( err_msg ) + "','" +
                    QueryUtils.duplicateQuotes( intallation_id ) + "'," +
                    "TO_DATE('" + start_date + "','DDMONYYYY HH24:MI:SS'),'" +
                    QueryUtils.duplicateQuotes( vob_name ) + "','" +
                    QueryUtils.duplicateQuotes( actor ) + "','" +
                    QueryUtils.duplicateQuotes( session_id ) + "','" +
                    QueryUtils.duplicateQuotes( rec_type ) + "','" +
                    QueryUtils.duplicateQuotes( action ) + "','" +
                    QueryUtils.duplicateQuotes( entry_name ) + "','" +
                    QueryUtils.duplicateQuotes( att_1 ) + "','" +
                    QueryUtils.duplicateQuotes( att_2 ) + "'," +
                    "TO_DATE('" + end_date + "','DDMONYYYY HH24:MI:SS'))";

            debugLog("ClearcaseInterface :: trackAction : query : " + query);

            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                	dbWriter.submit(query);
                    dbWriter.commit();
                } catch ( Exception e ) {
                	commitBool = false;
                    if (dbWriter != null) {
                    	try {
                    		dbWriter.checkConn();
                    		dbWriter.rollback();
                        } catch (SQLException e1) {
                            errorLog("ClearcaseInterface :: trackAction : error in commit...", e1);
                        }
                    }
                    Thread.sleep(2000);
                }
            }
            if (!commitBool) {
            	try {
                    DBTrack.trackQuery(sessionId, userId, query);
                } catch (IOException ex) {
                    errorLog("ClearcaseInterface :: trackAction  : unable to track lost query : " + ex.getMessage(), ex);
                }
            }
        } else {
            errorLog("ClearcaseInterface :: trackAction : the track message is null or empty!!!");
        }
    }


    /**
     * Invokes the CC action NOT wainting for result
     *
     * @param unixUser    the unix user used to execute the cc command
     * @param inFilePath  input file for clearcase
     * @param outFilePath outputfile per cleiacase
     * @param sessionId   the session id of the current user (used for track reasons)
     *
     * @return And integer representing the result:
     *         <li>COMMAND_OK_RESULT the clearcase command execution was ok
     *         <li>COMMAND_KO_RESULT the clearcase command execution was ko
     *         <li>COMMAND_UNKNOW_RESULT the clearcase command execution was unknow: i.e. batch execution
     *
     * @throws IOException
     */
    public static int invokeCommand(String unixUser, String inFilePath, String outFilePath, String sessionId) throws IOException {
        return invokeCommand(unixUser, inFilePath, outFilePath, sessionId, false);
    }


    /**
     * Invokes the CC action.
     *
     * @param unixUser    the unix user used to execute the cc command
     * @param inFilePath  input file for clearcase
     * @param outFilePath outputfile per clearcase
     * @param sessionId   the session id of the current user (used for track reasons)
     * @param waitResult  if true the code will wait that the clearcase process ends
     *
     * @return And integer representing the result:
     *         <li>COMMAND_OK_RESULT the clearcase command execution was ok
     *         <li>COMMAND_KO_RESULT the clearcase command execution was ko
     *         <li>COMMAND_UNKNOW_RESULT the clearcase command execution was unknow: i.e. batch execution
     *
     * @throws IOException - If an I/O error occurs
     */

    public static int invokeCommand(String unixUser, String inFilePath, String outFilePath, String sessionId, boolean waitResult) throws IOException {
    	int result;
    	
    	debugLog("ClearcaseInterface :: invokeCommand start...");
        /* costruisco il comando */
        String clearcaseCommand = buildClearcaseCommand(unixUser, inFilePath, outFilePath);
        /* esegui il comando */
        ClearcaseCommand ccCommand = new ClearcaseCommand(clearcaseCommand, waitResult);
        debugLog("ClearcaseInterface :: invokeCommand : executing " + clearcaseCommand);
        ccCommand.run();
        /* aspetto il risultato */
        File outFile;
        if (waitResult) {
            outFile = waitOutFile(outFilePath);
            /* traccia il risultato nel db */
            Hashtable outFileContent = parseOutFile(outFile, unixUser, sessionId);
            String ccCommandResult = (String) outFileContent.get(ClearcaseInterfaceCostants.OUT_RESULT);
            if (ccCommandResult.equals(ClearcaseInterfaceCostants.OUT_RESULT_OK_VALUE)) {
                result = ClearcaseInterfaceCostants.COMMAND_OK_RESULT;
            } else {
                result = ClearcaseInterfaceCostants.COMMAND_KO_RESULT;
            }
            try {
                trackAction((String) outFileContent.get(ClearcaseInterfaceCostants.OUT_TRACK_MESSAGE), sessionId, unixUser);
            } catch (Exception e) {
                /* this exception will not be raised because if we are not able to connect 
                 * to the database the user must be able to continue its work */
                String msg = "There was an error tracking the user action in database";
                errorLog("ClearcaseInterface :: invokeCommand : " + msg + 
                		" *** unixUser " + unixUser + " inFilePath = " + 
                		inFilePath + " outFilePath = " + outFilePath +
                        " sessionId = " + sessionId + " waitResult = " + 
                        waitResult + " exception message = " + e.getMessage(), e);
            }
        } else {
            result = ClearcaseInterfaceCostants.COMMAND_UNKNOW_RESULT;
        }
        debugLog("ClearcaseInterface :: invokeCommand finished result = " + result);
        return result;
    }


}
