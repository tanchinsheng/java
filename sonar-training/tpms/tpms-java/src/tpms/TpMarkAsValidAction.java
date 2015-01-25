package tpms;

import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.TP;

import org.w3c.dom.Element;
import tol.LogWriter;
import tpms.utils.DBTrack;
import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

public class TpMarkAsValidAction extends TpAction {
    String newVersion;
    String division;

    public TpMarkAsValidAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException {

        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("user=" + this.getUserName());
        out.println("rectype=" + "tp");
        out.println("action=" + this.getCCAction());
        out.println("submit=1");
        out.println("outfile=" + repFileName);
        out.println("diff_outfile=" + repFileName);
        String vob = (XmlUtils.getChild(tpRec, "VOB") != null ? XmlUtils.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + 0);
        out.println("jobname=" + XmlUtils.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + XmlUtils.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + XmlUtils.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        debugLog("TpMarkAsValidAction :: writeBackEndInFile : CC ACTION start Get New TPMS ver...");
        out.println("new_version_nb=" + getNewTpmsVer());
        debugLog("TpMarkAsValidAction :: writeBackEndInFile : CC ACTION stop Get New TPMS ver.");

        /**
         * Actual available properties list:
         * - extract_dir
         * - from_mail=from@txt.it
         * - cc_mail1=cc1@txt.it
         * - cc_mail2=cc2@txt.it
         */
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        out.flush();
        out.close();
        debugLog("TpMarkAsValidAction :: writeBackEndInFile :  CC IN FILE CREATED>" + fName);
    }

    public void updateDbKoResult() throws Exception {
        deleteTpLock(newVersion);
    }

    public void doDbTransaction() throws Exception {
    	
        boolean dbConnectionAvailable = QueryUtils.checkDbConnection(dbwrt);
        if (dbConnectionAvailable) {
            division = getDivision();
        } else {
            division = divisionNotAvailableValue;
        }
    	
        debugLog("TpMarkAsValidAction :: doDbTransaction : TP owner's division = " + division);
        Element tp2El = null;
        try {
            tp2El = getNewTpData();
        }
        catch (Exception e) {
            errorLog("TpMarkAsValidAction :: doDbTransaction : Exception " + e.getMessage(), e);
        }
        String vobVersion = (tp2El != null ? XmlUtils.getVal(tp2El, "JOB_VER") : "44");

        debugLog("TpMarkAsValidAction :: doDbTransaction : TP MARK_AS_VALIDATE - TpRec TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        debugLog("TpMarkAsValidAction :: doDbTransaction : TP MARK_AS_VALIDATE - NEW TPMS_VER=" + vobVersion);
        debugLog("TpMarkAsValidAction :: doDbTransaction : TP MARK_AS_VALIDATE DB TRANSACTION STARTED");
        debugLog("TpMarkAsValidAction :: doDbTransaction : Action Comment = " + this.getMarkAsValCmt());
        if (vobVersion.equals(XmlUtils.getVal(tpRec, "JOB_VER"))) {
            /* no modfication of TP, update tp to tp_plant in status READY_TO_PROD */
            updateToTpPlant("Ready_to_production");  //no exception thrown
            
            /* insert new row in history table */
            insertToHistory(XmlUtils.getVal(tpRec, "JOB_VER"), "Ready_to_production"); // throw exception
            
            /* insert or update Action Comments */
            if (searchTpActionCmt()!=0){
            	debugLog("TpMarkAsValidation :: doDbTransaction : Old record found, perform updating...");
            	updateToActionComment();   // throw exception
            } else {
            	debugLog("TpMarkAsValidation :: doDbTransaction : No existing record found, perform inserting...");
            	insertToActionComment(XmlUtils.getVal(tpRec, "JOB_VER"));  // throw exception          	
            }
    
            /* delete from tp_lock */
            deleteTpLock(newVersion);
        } else {
            /* Modification to TP, insert new tp in TP_PLANT */
            insertToTpPlant(vobVersion);  //no exception thrown
            /* insert new row in history table */ 
            insertToHistory(vobVersion, "Ready_to_production"); // throw exception
            
            if (searchTpInProduction() != 0) { // search in TP_HISTORY ?
                /* update TP to tp_plant in status Obsolete */
                updateToTpPlant("Obsolete");  //no exception thrown
                insertToHistory(XmlUtils.getVal(tpRec, "JOB_VER"), "Obsolete"); // throw exception
            } else {
                /* update TP to tp_plant in status GHOST */ 
                updateToTpPlant("Ghost");  //no exception thrown
            }
            
            /* insert Action Comments */
            insertToActionComment(vobVersion); // throw exception
            
            /* delete from tp_lock */
            deleteTpLock(vobVersion);
        }
 
        debugLog("TpMarkAsValidAction :: doDbTransaction : ENDED");

    }

    private int searchTpActionCmt() throws Exception {
    	
        int result = -1;
        Vector v = new Vector();
        try {
        	dbwrt.getRows(v,
                "SELECT JOBNAME " +
                        "FROM ACTION_COMMENTS" +
                        " WHERE" +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                         " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL")+ " AND " +
                         " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV")+ "' AND " +
                        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " +
    					" STATUS='Ready_to_production'");
        return v.size();
        } catch (Exception e) {
        	errorLog("TpMarkAsValidAction :: searchTpActionCmt : unable to search TP...",e);
        }
        return result;
    }
    
    public void insertToHistory(String newVersion, String newStatus) throws Exception {
    	
        debugLog("TpMarkAsValidAction :: insertToHistory : STARTED");
    	
        String query = "INSERT INTO TP_HISTORY " +
                "( " +
                " JOBNAME, " +
                " JOB_RELEASE, " +
                " JOB_REVISION, " +
                " TPMS_VER, " +
                " ACTOR," +
                " ACTION_DATE," +
                " STATUS," +
                " DIVISION," +
                " VOB_STATUS," +
                " OWNER_LOGIN," +
                " INSTALLATION_ID," +
                " LINESET_NAME," +
                " PRODUCTION_AREA_ID " +
                ") " +
                "VALUES " +
                "( '" +
                XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                newVersion + "," +
                "'" + userName + "'," +
                "SYSDATE,'" + newStatus +
                "','" +
                division + "','" +
                "OnLine" + "','" +
                XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                XmlUtils.getVal(tpRec, "LINESET") + "','" +
                XmlUtils.getVal(tpRec, "AREA_PROD")  +
                "')";

        debugLog("TpMarkAsValidAction :: insertToHistory : QUERY = " + query);
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            try {
                DBTrack.trackQuery(sessionId, userName, query);
            } catch (IOException ex) {
                errorLog("TpMarkAsValidAction :: insertToHistory : unable to track lost query : " + ex.getMessage(), ex);
            }
            throw new TpmsException(msg, action, e);
        }
        debugLog("TpMarkAsValidAction :: insertToHistory : ENDED");
    }

    public void insertToActionComment(String Version) throws Exception {
	    
        debugLog("TpMarkAsValidAction :: insertToActionComment : STARTED");

        String temp_mav_action_Comment = TP.getDbFormatCommentsFromFieldFormat(this.getMarkAsValCmt());
        if (temp_mav_action_Comment.length() > TpAction.MAX_COMMENT_LENGTH){
        	temp_mav_action_Comment = temp_mav_action_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	} 
        String query = "INSERT INTO ACTION_COMMENTS " +
        "( " +
        " JOBNAME, " +
        " JOB_RELEASE, " +
        " JOB_REVISION, " +
        " TPMS_VER, " +
        " COMMENT_BODY, " +
        " CREATION_DATE, " + 
        " STATUS," +
        " LINESET_NAME," +
        " FROM_PLANT" +
        ") " +
        "VALUES " +
        "( '" +
        XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
        XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
        XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
        Version + ",'" +
        temp_mav_action_Comment + "'," +
        "SYSDATE" + ",'" +   
        "Ready_to_production" + "','" +
        XmlUtils.getVal(tpRec, "LINESET") + "','" +
        XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
        debugLog("TpMarkAsValidAction :: insertToActionComment : QUERY = " + query); 
 
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            try {
                DBTrack.trackQuery(sessionId, userName, query);
            } catch (IOException ex) {
                errorLog("TpMarkAsValidAction :: insertToActionComment : unable to track lost query : " + ex.getMessage(), ex);
            }
            throw new TpmsException(msg, action, e);
        }
        debugLog("TpMarkAsValidAction :: insertToActionComment : ENDED");
    }
 
    public void updateToActionComment() throws Exception {
    	
        debugLog("TpMarkAsValidAction :: updateToActionComment : STARTED");
         
        String temp_mav_action_Comment = TP.getDbFormatCommentsFromFieldFormat(this.getMarkAsValCmt());
        if (temp_mav_action_Comment.length() > TpAction.MAX_COMMENT_LENGTH){
        	temp_mav_action_Comment = temp_mav_action_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	}
        String query = "UPDATE ACTION_COMMENTS SET COMMENT_BODY = '" + temp_mav_action_Comment +
        "', CREATION_DATE = SYSDATE" +
        " WHERE " +
        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER") + " AND " + 
        " STATUS='Ready_to_production'";
        debugLog("TPMarkAsValidAction :: updateToActionComment : QUERY = " + query);
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            try {
                DBTrack.trackQuery(sessionId, userName, query);
            } catch (IOException ex) {
                errorLog("TpMarkAsValidAction :: updateToActionComment : unable to track lost query : " + ex.getMessage(), ex);
            }
            throw new TpmsException(msg, action, e);
        }
        debugLog("TPMarkAsValidAction :: updateToActionComment :  ENDED");
    }

    
    private int searchTp()  throws Exception {
    	int result = -1;
    	try {
        	Vector v = new Vector();
        	dbwrt.getRows(v,
                "SELECT JOBNAME " +
                        "FROM TP_PLANT" +
                        " WHERE" +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        	result = v.size();
        } catch (Exception e) {
            errorLog("TpMarkAsValidAction :: searchTp : unable to get tp number", e);
        }
        return result;
    }

    private boolean tpIsLock() throws Exception {
        Vector v = new Vector();
        dbwrt.getRows(v,
                "SELECT JOBNAME " +
                        "FROM TP_LOCK" +
                        " WHERE" +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "'");
        return (v.size() != 0);
    }

    private int getNewTpmsVer() {
        /* questo valore verrà utilizzato all'interno del metodo per capire se si sono 
    	   verficati errori nel recupero della versione di tp
    	   English : Default value if no existing TPs found. 
    	*/
        final int DEFAULT_INT_EMPTY_VALUE = -10;
        /* questo valore verrà passato alla chiamata cliaer case per informarla che
           non sono riuscito a calcolare nuova versione del tp
           English : Default value if not able to generate new version 
        */
        final int DEFAULT_INT_UNABLE_TO_GENERATE_TP_NEW_VERSION = -1;
        int indexTpLock = DEFAULT_INT_EMPTY_VALUE;
        int indexTpPlant = DEFAULT_INT_EMPTY_VALUE;

        int result = DEFAULT_INT_UNABLE_TO_GENERATE_TP_NEW_VERSION;
        int tpNumber = 0;
        try {
            /* verifico se esiste il tp in quel plant
        	English : I verify if it exists the tp in that plant 
        	*/
            tpNumber = searchTp(); //throws Exception
        } catch (Exception e) {
            errorLog("TpMarkAsValidAction  :: getNewTpmsVer : unable to count tps in tp_plant", e);
        }
        if (tpNumber == 0) { // Not found old TPs
            try {
                /* se non esiste lo inserirsco 
            	(...è la prima spedizione di questo tp in quel plant...) 
            	English : Insert a new record to TP_PLANT 
            	*/
                insertToTpPlant(XmlUtils.getVal(tpRec, "JOB_VER")); //throws Exception 
            } catch (Exception e) {
                errorLog("TpMarkAsValidAction  :: getNewTpmsVer : re-align database unable to insert tp", e);
            }
        }
        /* Cerco di recuperare la nuova versione da assegnare al tp
         * English : I want to I recover the new version to assign the tp 
         */
        boolean tpIsLoked = false;
        try {
            /* verifico se c'è un lock su questo tp
             * English : Verify if JOBNAME.XX.YY is locked in TP_LOCK
             */
            tpIsLoked = tpIsLock();
        } catch (Exception e) {
            errorLog("TpMarkAsValidAction  :: getNewTpmsVer : unable to verify if tp is locked", e);
        }
        if (tpIsLoked) {
            /* se il tp fosse già lokkato....
             * English : If TP is already locked....
             */
            try {
                /* ... cerco di recuperare la versione del tp lokkato....
                 * English : I want to I recover the version of the locked TP
                 * based on JOBNAME.XX in TP_LOCK
                 */
                indexTpLock = getMaxTpmsVerFromLock();  
            } catch (Exception e) {
                errorLog("TpMarkAsValidAction  :: getNewTpmsVer : unable to get max TP version from tp_lock", e);
            }
            try {
                /*... cerco di recuperare la versione del tp ....
                 * English : I want to I recover the version of the TP
                 * based on JOBNAME.XX in TP_PLANT
                 */
                indexTpPlant = getMaxTpmsVer();
            } catch (Exception e) {
                errorLog("TpMarkAsValidAction  :: getNewTpmsVer : unable to get max TP version from tp_plant", e);
            }
            /*... calcolo il massimo tra la massima versione del tp e la massima versione del tp lokkato ....
             * English :I calculate the utmost between the greatest version of the tp and the greatest version of the tp lokkato 
             */
            if (indexTpPlant != DEFAULT_INT_EMPTY_VALUE && indexTpLock != DEFAULT_INT_EMPTY_VALUE) {
                /*... se sono riuscito a reciuperare entrambe i valori calcolo il max tra i due, 
                 * lo incremento e lo ritorno...
                 * English: if I succeeded at reciuperare both the values I calculate the max between 
                 * the two, the development and the return
                 */
                if (indexTpLock < indexTpPlant) {
                    result = indexTpPlant + 1;
                } else {
                    result = indexTpLock + 1;
                }
            }
            try {
                /* lokko la versione che ho generato...
                 *  English : Lock the version that I generated
                 *  based on JOBNAME.XX.YY.ZZ into TP_LOCK
                 */
                insertTpLock(result);
            } catch (Exception e) {
                errorLog("TpMarkAsValidAction  :: getNewTpmsVer : unable to insert lock for tp", e);
            }
        } else {
            /* se il tp NON fosse già lokkato....
             * English : If TP not found in TP_LOCK
             */
            try {
                /* prendo la massima versione del tp e la incremento....
                 * English :I take the greatest version of the tp and the development
                 * based on JOBNAME.XX in TP_PLANT
                 */
                result = getMaxTpmsVer() + 1;
            } catch (Exception e) {
                errorLog("TpMarkAsValidAction  :: getNewTpmsVer : unable to get max TPMS version from tp_plant B", e);
            }
            try {
                /*... metto un lock sulla versione del tp che ho generato ...
                 * English : I put a lock on the version of the tp that I bred
                 * based on JOBNAME.XX.YY.ZZ into TP_LOCK
                 */
                insertTpLock(result);
            } catch (Exception e) {
                errorLog("TpMarkAsValidAction  :: getNewTpmsVer : unable to insert lock for tp B", e);
            }
        }
        newVersion = Integer.toString(result);
        return result;
    }

    private int getMaxTpmsVer() throws Exception {
        Vector v = new Vector();
        dbwrt.getRows(v,
                "SELECT MAX(TPMS_VER) " +
                        "FROM TP_PLANT" +
                        " WHERE" +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + "");
        return Integer.valueOf((String) v.elementAt(0)).intValue();
    }

    private void insertTpLock(int newTpmsVer) throws Exception {
        String query = "INSERT INTO TP_LOCK " +
                "(" +
                " JOBNAME, " +
                " JOB_RELEASE, " +
                " JOB_REVISION, " +
                " TPMS_VER " +
                ") " +
                "VALUES " +
                "( '" +
                XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                newTpmsVer +
                ")";

        debugLog("TpMarkAsValidAction  :: insertTpLock : QUERY = " + query);
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            debugLog("TpMarkAsValidAction  :: insertTpLock : Open DbTrack FILE>");
            PrintWriter out = new PrintWriter(new FileWriter(webAppDir + "/logs/DbTrack.log", true));
            out.println("query=" + query);
            out.flush();
            out.close();
            debugLog("TpMarkAsValidAction  :: insertTpLock : DbTrack FILE Updated>");
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
    }

    private int getMaxTpmsVerFromLock() throws Exception {
        Vector v = new Vector();
        dbwrt.getRows(v,
                "SELECT MAX(TPMS_VER) " +
                        "FROM TP_LOCK" +
                        " WHERE" +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + "");
        return Integer.valueOf((String) v.elementAt(0)).intValue();
    }

    private void deleteTpLock(String version) throws Exception {
        String query = "DELETE " +
                "FROM TP_LOCK" +
                " WHERE" +
                " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                " TPMS_VER=" + version;
        debugLog("TpMarkAsValidAction :: deleteTpLock : QUERY = " + query);
   
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            errorLog("TpMarkAsValidAction :: deleteTpLock : Open DbTrack FILE>");
            PrintWriter out = new PrintWriter(new FileWriter(webAppDir + "/logs/DbTrack.log", true));
            out.println("query=" + query);
            out.flush();
            out.close();
            errorLog("TpMarkAsValidAction :: deleteTpLock : DbTrack FILE Updated>");
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            errorLog("TpMarkAsValidAction :: deleteTpLock : Exception " + e.getMessage(), e);
            throw new TpmsException(msg, action, e);
        }
    }

    private Element getNewTpData() throws Exception {
        Element root = XmlUtils.getRoot(repFileName);
        return XmlUtils.getDirChild(root, "TP2");
    }

    /* Do not throw exception here, need to INSERT TO TP_HISTORY */
    public void insertToTpPlant(String version) {
    	
        debugLog("TpMarkAsValidAction :: insertToTpPlant : STARTED");

        String query = "INSERT INTO TP_PLANT " +
                "( " +
                " JOBNAME, " +
                " JOB_RELEASE, " +
                " JOB_REVISION, " +
                " TPMS_VER, " +
                " LINE, " +
                " FACILITY, " +
                " FROM_PLANT, " +
                " OWNER_LOGIN," +
                " OWNER_EMAIL," +
                " ORIGIN," +
                " ORIGIN_LBL," +
                " LINESET_NAME," +
                " TO_PLANT," +
                " TESTER_INFO," +
                " VALID_LOGIN," +
                " PROD_LOGIN," +
                " LAST_ACTION_ACTOR," +
                " LAST_ACTION_DATE," +
                " STATUS," +
                " DISTRIB_DATE," +
                " PROD_DATE," +
                " DIVISION," +
                " VOB_STATUS," +
                " INSTALLATION_ID" +
                ") " +
                "VALUES " +
                "( '" +
                XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                version + ",'" +
                XmlUtils.getVal(tpRec, "LINE") + "','" +
                XmlUtils.getVal(tpRec, "FACILITY") + "','" +
                XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "','" +
                XmlUtils.getVal(tpRec, "OWNER_EMAIL") + "','" +
                "tp','TP_" +
                XmlUtils.getVal(tpRec, "JOBNAME") + "." + XmlUtils.getVal(tpRec, "JOB_REL") + "." +
                XmlUtils.getVal(tpRec, "JOB_REV") + "." + XmlUtils.getVal(tpRec, "JOB_VER") +
                "','" +
                XmlUtils.getVal(tpRec, "LINESET") + "','" +
                XmlUtils.getVal(tpRec, "TO_PLANT") + "','" +
                XmlUtils.getVal(tpRec, "TESTER_INFO") + "','" +
                XmlUtils.getVal(tpRec, "VALID_LOGIN") + "','" +
                XmlUtils.getVal(tpRec, "PROD_LOGIN") + "','" +
                userName + "'," +
                "SYSDATE" + ",'" +
                "Ready_to_production'," +
                "SYSDATE,'" +
                "','" +
                division + "','" +
                "OnLine" + "','" +
                XmlUtils.getVal(tpRec, "FROM_PLANT") +
                "')";
 
        debugLog("TpMarkAsValidAction :: insertToTpPlant : QUERY = " + query);
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            //String action = "DB UPDATE";
            //String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            try {
                DBTrack.trackQuery(sessionId, userName, query);
            } catch (IOException ex) {
                errorLog("TpMarkAsValidAction :: insertToTpPlant : unable to track lost query : " + ex.getMessage(), ex);
            }
            //throw new TpmsException(msg, action, e);
        }
        debugLog("TpMarkAsValidAction :: insertToTpPlant : ENDED");
    } 

    /* Do not throw exception here, must do INSERT INTO TP_HISTORY  */
    public void updateToTpPlant(String newStatus){
        debugLog("TpMarkAsValidAction :: updateToTpPlant : STARTED");

        String query = "UPDATE TP_PLANT SET" +
                " LAST_ACTION_ACTOR ='" + userName +
                "', LAST_ACTION_DATE = SYSDATE" +
                ", STATUS ='" + newStatus + "'" +
                " WHERE " +
                " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER");

        debugLog("TpMarkAsValidAction :: updateToTpPlant : QUERY = " + query);
        try {
            dbwrt.submit(query);
            dbwrt.commit();
        } catch (Exception e) {
            try {
                DBTrack.trackQuery(sessionId, userName, query);
            } catch (IOException ex) {
                errorLog("TpMarkAsValidAction :: updateToTpPlant : unable to track lost query : " + ex.getMessage(), ex);
            }
        }
        debugLog("TpMarkAsValidAction :: updateToTpPlant :  ENDED");
    }
    
    private int searchTpInProduction() throws Exception {
        Vector v = new Vector();
        dbwrt.getRows(v,
                "SELECT JOBNAME " +
                        "FROM TP_HISTORY" +
                        " WHERE" +
                        " JOBNAME='" + XmlUtils.getVal(tpRec, "JOBNAME") + "' AND " +
                        " JOB_RELEASE=" + XmlUtils.getVal(tpRec, "JOB_REL") + " AND " +
                        " JOB_REVISION='" + XmlUtils.getVal(tpRec, "JOB_REV") + "' AND " +
                        " STATUS=" + "'In_Production'" + " AND " +
                        " TPMS_VER=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        return v.size();
    }

    private String getDivision() throws Exception {

        String query = "SELECT DIVISION FROM USERS where TPMS_LOGIN='" +
                        XmlUtils.getVal(tpRec, "OWNER_LOGIN") + "' AND INSTALLATION_ID='" +
                        XmlUtils.getVal(tpRec, "FROM_PLANT") + "'";

        debugLog("TpMarkAsValidAction :: getDivision : QUERY = " + query);

        SQLInterface iface = new SQLInterface();
        CachedRowSet rs = iface.execQuery(query);
        String  result = "";
        if (rs != null){
        	while (rs.next()) { 
        		result = rs.getString(1);
        	}
        }

        return result;
    }

}