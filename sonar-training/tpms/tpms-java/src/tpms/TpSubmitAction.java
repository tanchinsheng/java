/**
 * Created by IntelliJ IDEA.
 * User: puglisi
 * Date: Sep 5, 2003
 * Time: 4:01:46 PM
 * To change this template use Options | File Templates.
 */
package tpms;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.TP;
import it.txt.tpms.users.TpmsUser;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tol.LogWriter;
import tpms.utils.DBTrack;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class TpSubmitAction extends TpAction {
	
    private String division;
	
    public TpSubmitAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }
          
    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "tp");
        //out.println("action=" + this.getCCAction());
        out.println("action=tp_delivery");
        //Element userData = CtrlServlet.getUserData(XmlUtils.getVal(tpRec, "OWNER_LOGIN"));
        TpmsUser tpmsUser = getTpmsUser();
        String unixGroup = tpmsUser.getUnixGroup();
        out.println("unix_group=" + unixGroup);      
        division = tpmsUser.getDivision();
        out.println("division=" + division);
        out.println("linesetname=" + XmlUtils.getVal(tpRec, "LINESET"));
        out.println("baseline=" + XmlUtils.getVal(tpRec, "LASTBASELINE"));
        out.println("jobname=" + XmlUtils.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + XmlUtils.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + XmlUtils.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=0");
        out.println("facility=" + XmlUtils.getVal(tpRec, "FACILITY"));
        out.println("tester_info=" + XmlUtils.getVal(tpRec, "TESTER_INFO"));
        out.println("line=" + XmlUtils.getVal(tpRec, "LINE"));
        out.println("xfer_path=" + XmlUtils.getVal(tpRec, "XFER_PATH"));
        out.println("to_plant=" + XmlUtils.getVal(tpRec, "TO_PLANT"));
        out.println("valid_login=" + XmlUtils.getVal(tpRec, "VALID_LOGIN"));
        out.println("prod_login=" + XmlUtils.getVal(tpRec, "PROD_LOGIN"));
        out.println("from_plant=" + XmlUtils.getVal(tpRec, "FROM_PLANT"));
        //out.println("certification=" + XmlUtils.getVal(tpRec, "CERTIFICATION"));
        out.println("area_prod=" + XmlUtils.getVal(tpRec, "AREA_PROD"));
        out.println("outfile=" + repFileName);
        String vob = (XmlUtils.getChild(tpRec, "VOB") != null ? XmlUtils.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));
        out.println("to_vob=" + VobManager.getTvobFromPlant(vob, XmlUtils.getVal(tpRec, "TO_PLANT")));
        out.println("trans_vob_list=" + VobManager.getTvobsListAsString(vob));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + TpActionServlet._cc_debug);
        out.println("to_mail=" + XmlUtils.getVal(tpRec, "EMAIL_TO"));
        out.println("cc_mail1=" + XmlUtils.getVal(tpRec, "EMAIL_CC"));
        out.println("cc_mail2=" + XmlUtils.getVal(tpRec, "EMAIL_FREE"));
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        out.flush();
        out.close();

        //Element userData = CtrlServlet.getUserData(XmlUtils.getVal(tpRec, "OWNER_LOGIN"));
        debugLog("TpSubmitAction :: writeBackEndInFile : CC IN FILE CREATED>" + fName);
    }

    private String generateQueryInsertIntoTpPlant(String newVersion, String division) {
        String query = "";
        TpmsUser tpmsUser = getTpmsUser();

        try {
        	String delivery_Comment="";
        	delivery_Comment = TP.getDbFormatCommentsFromFieldFormat(XmlUtils.getVal(tpRec, "DELIVERYCOMM"));
        	String hw_modification="";
        	hw_modification = TP.getDbFormatCommentsFromFieldFormat(XmlUtils.getVal(tpRec, "HW_MODIFICATION"));
        	if (delivery_Comment.length()>TpAction.MAX_COMMENT_LENGTH) {
        		delivery_Comment = delivery_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
        	}
         	if (hw_modification.length()>TpAction.MAX_COMMENT_LENGTH){
         		hw_modification = hw_modification.substring(0,TpAction.MAX_COMMENT_LENGTH);
         	}
         
            String tempZERO_YW = XmlUtils.getVal(tpRec, "ZERO_YW");
            if (tempZERO_YW.equals("")){
            	tempZERO_YW = "-1";
            }
            else {
            	tempZERO_YW=XmlUtils.getVal(tpRec, "ZERO_YW");
            }
            
            String tempNEW_TESTTIME = XmlUtils.getVal(tpRec, "NEW_TESTTIME");
            if (tempNEW_TESTTIME.equals("")){
            	tempNEW_TESTTIME = "-1";
            }
            else {
            	tempNEW_TESTTIME=XmlUtils.getVal(tpRec, "NEW_TESTTIME");
            }
            debugLog("TpSubmitAction :: generateQueryInsertIntoTpPlant : Race Comments : "+XmlUtils.getVal(tpRec, "DELIVERYCOMM"));
            debugLog("TpSubmitAction :: generateQueryInsertIntoTpPlant : HW Modifications : "+XmlUtils.getVal(tpRec, "HW_MODIFICATION"));
            debugLog("TpSubmitAction :: generateQueryInsertIntoTpPlant : EXP_AVG_YV : "+XmlUtils.getVal(tpRec, "EXP_AVG_YV") +","+ " ZERO_YW : "+tempZERO_YW+","+" NEW_TESTTIME : "+tempNEW_TESTTIME+","+" IS_TEMP : "+ XmlUtils.getVal(tpRec, "IS_TEMP")+","+" VALID_TILL : "+ XmlUtils.getVal(tpRec, "VALID_TILL"));
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
                    " PRODUCTION_AREA_ID," +
                    " INSTALLATION_ID," +
                    " DELIVERY_COMMENTS," +
                    " HW_MODIFICATIONS," +
                    " EXP_AVG_YV," +
                    " ZERO_YW," +
                    " NEW_TT," +
                    " IS_TEMP," +
                    " VALID_TILL," +
                    " EMAIL" +
                    ") " +
                    "VALUES " +
                    "( '" +
                    XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                    XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                    XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                    newVersion + ",'" +
                    XmlUtils.getVal(tpRec, "LINE") + "','" +
                    XmlUtils.getVal(tpRec, "FACILITY") + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    tpmsUser.getName() + "','" +
                    tpmsUser.getEmail() + "','" +
                    "ls" + "','" +
                    "LINESET_B" + XmlUtils.getVal(tpRec, "LASTBASELINE") + "_" + XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    XmlUtils.getVal(tpRec, "TO_PLANT") + "','" +
                    XmlUtils.getVal(tpRec, "TESTER_INFO") + "','" +
                    XmlUtils.getVal(tpRec, "VALID_LOGIN") + "','" +
                    XmlUtils.getVal(tpRec, "PROD_LOGIN") + "','" +
                    tpmsUser.getName() + "'," +
                    "SYSDATE" + ",'" +
                    "Distributed'," +
                    "SYSDATE,'" +
                    "','" +
                    division + "','" +
                    "OnLine','" +
                    XmlUtils.getVal(tpRec, "AREA_PROD") + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    delivery_Comment + "','" +
                    hw_modification + "','" +
                    XmlUtils.getVal(tpRec, "EXP_AVG_YV") + "','" +
                    tempZERO_YW + "','" +
                    tempNEW_TESTTIME + "','" +
                    XmlUtils.getVal(tpRec, "IS_TEMP") + "','" +
                    XmlUtils.getVal(tpRec, "VALID_TILL") + "','" +
                    XmlUtils.getVal(tpRec, "DIS_EMAIL") +
                    "')";
            debugLog("TpSubmitAction :: generateQueryInsertIntoTpPlant : query : " + query);
        } catch (Exception e) {
            errorLog("TpSubmitAction :: generateQueryInsertIntoTpPlant : unable to generate query!!", e);
        }
        return query;
    }

    public String generateQueryInsertIntoTpHistory(String newVersion, String division) {
        String query = "";
        TpmsUser tpmsUser = getTpmsUser();
        try {
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
                    " LINESET_NAME," +
                    " PRODUCTION_AREA_ID "+
                    ") " +
                    "VALUES " +
                    "( '" +
                    XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
                    XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
                    XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
                    newVersion + ",'" +
                    tpmsUser.getName() + "'," +
                    "SYSDATE,'" +
                    "Distributed','" +
                    division + "','" +
                    "OnLine','" +
                    tpmsUser.getName() + "','" +
                    XmlUtils.getVal(tpRec, "FROM_PLANT") + "','" +
                    XmlUtils.getVal(tpRec, "LINESET") + "','" +
                    XmlUtils.getVal(tpRec, "AREA_PROD") +
                    "')";
        } catch (Exception e) {
            errorLog("TpSubmitAction :: generateQueryInsertIntoTpPlant : unable to generate query!!", e);
        }
        return query;
    }
   
    public void doDbTransaction(String[] submitqueries,String newVersion) throws Exception {
        debugLog("TpSubmitAction :: doDbTransaction : TRANSACTION STARTED");
        try {
               for (int i = 0; i < submitqueries.length; i++) {
            	   dbwrt.submit(submitqueries[i]);
            	   dbwrt.commit();
               }
             
        } catch (Exception e) {
            if (dbwrt != null) {
                try {
                    dbwrt.rollback();
                } catch (SQLException e1) {
                    errorLog("TpSubmitAction :: doDbTransaction : unable to rollback!", e1);
                }
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
        debugLog("TpSubmitAction :: doDbTransaction : TP DELIVERY DB TRANSACTION ENDED");
    }

    private String getNewTpJobVersion() {
        String newJobVersion = "99";

        try {
            Element root = XmlUtils.getRoot(repFileName);
            Element tp2El = XmlUtils.getDirChild(root, "TP");
            newJobVersion = XmlUtils.getVal(tp2El, "JOB_VER");
        } catch ( ParserConfigurationException e ) {
            errorLog("TpSubmitAction :: getNewTpJobVersion : ParserConfigurationException " + e.getMessage(), e );
        } catch ( SAXException e ) {
            errorLog("TpSubmitAction :: getNewTpJobVersion : SAXException " + e.getMessage(), e );
        } catch ( IOException e ) {
            errorLog("TpSubmitAction :: getNewTpJobVersion : IOException " + e.getMessage(), e );
        }

        return newJobVersion;
    }


    public void updateDb() throws Exception {

       	String [] arrayqueries = new String [this.getTestRowCount()];
        Exception DbException = null;
        String newTpJobVersion = getNewTpJobVersion();
        XmlUtils.setVal(tpRec, "JOB_VER", newTpJobVersion);
        String addRowArray = this.getAddRow();
        if (!GeneralStringUtils.isEmptyString(addRowArray)){
        	arrayqueries = testTpDeliveryInsert(newTpJobVersion);
	    }
        String queryInsertIntoActionCmt = insertSingleDeliveryComment(newTpJobVersion);
        String queryInsertIntoTpPlant = generateQueryInsertIntoTpPlant(newTpJobVersion, division);
        String queryInsertIntoTpHistory = generateQueryInsertIntoTpHistory(newTpJobVersion, division);
       
        boolean commitBool = false;
       
	    if (!GeneralStringUtils.isEmptyString(queryInsertIntoTpPlant) && !GeneralStringUtils.isEmptyString(queryInsertIntoTpHistory)) {
	    	int queriesSize = 3; //queryInsertIntoTpPlant+queryInsertIntoTpHistory+queryInsertIntoActionCmt
        	if(!GeneralStringUtils.isEmptyString(addRowArray)){
        		queriesSize += arrayqueries.length;
        	}
        	String [] queries = new String [queriesSize];
            queries[0] = queryInsertIntoTpPlant;
            queries[1] = queryInsertIntoTpHistory;
            queries[2] = queryInsertIntoActionCmt;
            if (!GeneralStringUtils.isEmptyString(addRowArray)){
            	int j = 3;
            	for (int i = 0; i < arrayqueries.length; i++) {
            		queries[j] = arrayqueries[i];
            		j++;
                }
            }
	    	
	    	for (int i = 0; ((!commitBool) && (i < 3)); i++) {
	    		commitBool = true;
	            try { 
	            	this.doDbTransaction(queries,newTpJobVersion);
	            } catch (Exception e) {
	            	DbException = e;
	                commitBool = false;
	                if (dbwrt != null) {
	                	try {
	                		dbwrt.checkConn();
	                    } catch (SQLException e1) {}
	                }
	                Thread.sleep(2000);
	            }
	        }
	        if (!commitBool) {
	            DBTrack.trackQueries(this.sessionId, this.userName, queries);
	            errorLog("TpSubmitAction :: doDbTransaction : DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
	            if (DbException != null) throw DbException;
	        }
	    } else {
	    	debugLog("TpSubmitAction :: updateDb : unable to get queries ( queryInsertIntoTpPlant = " + queryInsertIntoTpPlant + " queryInsertIntoTpHistory = " + queryInsertIntoTpHistory + " newTpJobVersion = " + newTpJobVersion);
	    }
    }

    /* Generate Test Queries*/
    public String[] testTpDeliveryInsert(String newVersion){
	    int testNoId = 0;
	    int queriesSize = 0;
	    String query ="";
		StringTokenizer insertAddRow = new StringTokenizer(this.getAddRow(), "~"); // get all rows
		String [] generateArrayQueries = new String [this.getTestRowCount()];
		while (insertAddRow.hasMoreTokens()) {
			StringTokenizer insertAddRow1 = new StringTokenizer(insertAddRow.nextToken(), "|");
			testNoId++;
			while(insertAddRow1.hasMoreTokens()) {
				String testNo = insertAddRow1.nextToken();
				if (testNo.equals("EMPTY")) {
					testNo="";
				}
				String newFlag = insertAddRow1.nextToken();
				String oldLSL = insertAddRow1.nextToken();
				if (oldLSL.equals("EMPTY")){
					oldLSL="";
				}
				String oldUSL = insertAddRow1.nextToken();
				if (oldUSL.equals("EMPTY")) {
					oldUSL="";
				}
				String unit = insertAddRow1.nextToken();
				if (unit.equals("EMPTY")) {
					unit="";
				}
				String newLSL = insertAddRow1.nextToken();
				if (newLSL.equals("EMPTY")){
						newLSL="";
				}
				String newUSL = insertAddRow1.nextToken();
				if (newUSL.equals("EMPTY")){
					newUSL="";
				}
				String testComment = insertAddRow1.nextToken();
				testComment = TP.getDbFormatCommentsFromFieldFormat(testComment);
				if (testComment.equals("EMPTY")) {
					 testComment="";
				}
				debugLog("TpSubmitAction :: testTpDeliveryInsert : TestNo : "+testNo+","+" newFlag : "+newFlag +","+" oldLSL : "+oldLSL+","+" oldUSL : "+oldUSL+","+" unit : "+unit+","+" newLSL : "+newLSL +","+" newUSL : "+newUSL+","+" testComment : "+testComment);
				query = "INSERT INTO TP_TEST_DELIVERY " +
		             "( " +
		             " JOBNAME, " +
		             " JOB_RELEASE, " +
		             " JOB_REVISION, " +
		             " TPMS_VER, " + 
		             " TEST_NO_ID, " +
		             " TEST_NO, " +
		             " NEW_FLAG, " +
		             " OLD_LSL, " +
		             " OLD_USL, " +
		             " UNIT, " +
		             " NEW_LSL, " +
		             " NEW_USL, " +
		             " TESTS_COMMENTS, " +
		             " LINESET_NAME," +
		             " FROM_PLANT" +
		             ") " +
		             "VALUES " +
		             "( '" +
		        XmlUtils.getVal(tpRec, "JOBNAME") + "'," +
		        XmlUtils.getVal(tpRec, "JOB_REL") + ",'" +
		        XmlUtils.getVal(tpRec, "JOB_REV") + "'," +
		        newVersion+"," + 
		        testNoId + ",'" +
		        testNo + "','" +
		        newFlag + "','" +
		        oldLSL + "','" +
		        oldUSL + "','" +
		        unit + "','" +
		        newLSL + "','" +
		        newUSL + "','" +
		        testComment + "','" +
		        XmlUtils.getVal(tpRec, "LINESET") + "','" +
		        XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
				generateArrayQueries[queriesSize] = query;
				queriesSize++;
				debugLog("TpSubmitAction :: testTpDeliveryInsert : query : "+query);
				 
			}
		}	return generateArrayQueries;
	}	

	public String insertSingleDeliveryComment(String newVersion) {
        String query = "";
        String singleDeliveryCmt = TP.getDbFormatCommentsFromFieldFormat(XmlUtils.getVal(tpRec, "SINGLEDELIVERYCMT"));
        if (singleDeliveryCmt.length()>TpAction.MAX_COMMENT_LENGTH){
        	singleDeliveryCmt = singleDeliveryCmt.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	} try {
	        query = "INSERT INTO ACTION_COMMENTS " +
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
	            XmlUtils.getVal(tpRec, "JOB_VER") + ",'" +
	            singleDeliveryCmt + "'," +
	            "SYSDATE" + ",'" +   
	            "Distributed" + "','" +
	            XmlUtils.getVal(tpRec, "LINESET") + "','" +
	            XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
	        debugLog("TpSubmitAction :: insertSingleDeliveryComment: query : "+query);
        } catch (Exception e) {
            errorLog("TpSubmitAction :: insertSingleDeliveryComment: unable to generate query!!", e);
        }
        return query;
    }
}
