package it.txt.integration.managers;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.TP;
import it.txt.tpms.users.TpmsUser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Element;

import tpms.TpmsException;
import tpms.utils.DBTrack;
import tpms.utils.SQLInterface;

/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 17-nov-2006
 * Time: 16.43.36
 */
public class TpDBManager {

    private TP tp;
    private File xmlActionResultDataFile;
    private String sessionId;
    private TpmsUser tpmsUser;


    private String generateQueryInsertIntoTpPlant () {
        String query = null;
        try {
            query = "INSERT INTO TP_PLANT " +
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
                    tp.getJobName() + "'," +
                    tp.getJobRelease() + ",'" +
                    tp.getJobRevision() + "'," +
                    tp.getTpmsVersion() + ",'" +
                    tp.getLine() + "','" +
                    tp.getFacility() + "','" +
                    tp.getFromPlant() + "','" +
                    tp.getOwnerLogin() + "','" +
                    tp.getOwnerEmail() + "','" +
                    tp.getOrigin() + "','" +
                    tp.getOriginLabel() + "','" + //"LINESET_B" + XmlUtils.getVal(tpRec, "LASTBASELINE") + "_" + XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    tp.getLinesetName() + "','" +
                    tp.getToPlant() + "','" +
                    tp.getTesterInfo() + "','" +
                    tp.getValidLogin() + "','" +
                    tp.getProdLogin() + "','" +
                    tp.getOwnerLogin() + "'," +
                    "SYSDATE" + ",'" +
                    tp.getStatus() + "'," +
                    "SYSDATE,'" +
                    "','" +
                    tp.getDivision() + "','" +
                    "OnLine','" +
                    tp.getFromPlant() +
                    "')";
        } catch ( Exception e ) {
            AfsCommonStaticClass.errorLog( "TpDBManager :: writeBackEndInFile : TpDeleteAction :: generateQueryInsertIntoTpPlant : unable to generate query!!", e );
        }
        return query;
    }


    private String generateQueryInsertIntoTpHistory () {
        String query;

            query = "INSERT INTO TP_HISTORY " +
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
                    " LINESET_NAME" +
                    ") " +
                    "VALUES " +
                    "( '" +
                    tp.getJobName() + "'," +
                    tp.getJobRelease() + ",'" +
                    tp.getJobRevision() + "'," +
                    tp.getTpmsVersion() + ",'" +
                    tp.getOwnerLogin() + "'," +
                    "SYSDATE,'" +
                    tp.getStatus() + "','" +
                    tp.getDivision() + "','" +
                    "OnLine','" +
                    tp.getOwnerLogin() + "','" +
                    tp.getFromPlant() + "','" +
                    tp.getLinesetName() +
                    "')";

        return query;
    }


    public TpDBManager ( TP tp, File xmlActionResultDataFile, String sessionId, TpmsUser tpmsUser ) {
        this.tp = tp;
        this.xmlActionResultDataFile = xmlActionResultDataFile;
        this.sessionId = sessionId;
        this.tpmsUser = tpmsUser;
    }


    public void doStepsTPDelivery () {
        String commonLogMessage = "TpDBManager :: doStepsTPDelivery";
        if ( tp != null && xmlActionResultDataFile != null && xmlActionResultDataFile.exists() ) {

            try {
                Element root = XmlUtils.getRoot( xmlActionResultDataFile );
                Element tp2El = XmlUtils.getDirChild( root, "TP" );
                tp.setTpmsVersion( Integer.parseInt( XmlUtils.getVal( tp2El, "JOB_VER" ) ) );
                String insertIntoTpPlant = generateQueryInsertIntoTpPlant();
                String insertIntoTpHistory = generateQueryInsertIntoTpHistory();
//                AfsCommonStaticClass.debugLog(commonLogMessage + " : performing " + insertIntoTpPlant);
//                AfsCommonStaticClass.executeInsertQuery( insertIntoTpPlant, sessionId, tpmsUser.getName() );
//                AfsCommonStaticClass.debugLog(commonLogMessage + " : performing " + insertIntoTpHistory);
//                AfsCommonStaticClass.executeInsertQuery( insertIntoTpHistory, sessionId, tpmsUser.getName() );
                AfsCommonStaticClass.debugLog(commonLogMessage);
                Vector sqlVec = new Vector();
                sqlVec.addElement(insertIntoTpPlant);
                sqlVec.addElement(insertIntoTpHistory);
                SQLInterface sqlInterface = new SQLInterface();
                try {
                	sqlInterface.executeMultiple(sqlVec);                	             	
                } catch (TpmsException te) {
                	Iterator it = sqlVec.iterator();
                	String sql = new String();
                	while(it.hasNext()) {
                		Object obj = it.next();
                		if(obj instanceof String) {
                			sql = sql + (String) obj + System.getProperty("line.separator") + System.getProperty("line.separator");                			           			
                		}
                	}
                	try {
        				DBTrack.trackQuery(sessionId, tpmsUser.getName(), sql);
        			} catch (IOException ioe) {
        				AfsCommonStaticClass.errorLog("TpDBManager :: doStepsTPDelivery : IOException during query tracking: unable to track query!!! " + sql , ioe);
        			}     
                }                
            } catch ( Exception e ) {            	
                AfsCommonStaticClass.errorLog( "TpDBManager :: doStepsTPDelivery error retrieving new TP version ("+ xmlActionResultDataFile.getName() +") : " + e.getMessage(), e );
            }
        } else {

        }
    }


}
