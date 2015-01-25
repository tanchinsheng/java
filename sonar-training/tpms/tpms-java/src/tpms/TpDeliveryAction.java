package tpms;

import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import it.txt.tpms.tp.TP;
import it.txt.tpms.users.TpmsUser;
import org.w3c.dom.Element;
import tol.LogWriter;
import tpms.utils.DBTrack;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 30, 2003
 * Time: 11:34:19 AM
 * To change this template use Options | File Templates.
 * FP rev5 - aggiunto l'insert dei campi DIVISION, VOB_STATUS, INSTALLATION_ID
 */
public class TpDeliveryAction extends TpAction {
    public TpDeliveryAction(String action, LogWriter log) {
        super(action, log);
        hasDbActionBool = true;
    }

    String division;

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws Exception{

        TpmsUser tpmsUser = getTpmsUser();

        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=tp");
        out.println("action=" + this.getCCAction());
        out.println("linesetname=" + XmlUtils.getVal(tpRec, "LINESET"));
        out.println("baseline=" + XmlUtils.getVal(tpRec, "LASTBASELINE"));
        out.println("jobname=" + XmlUtils.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + XmlUtils.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + XmlUtils.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=" + XmlUtils.getVal(tpRec, "JOB_VER"));
        out.println("facility=" + XmlUtils.getVal(tpRec, "FACILITY"));
        out.println("tester_info=" + XmlUtils.getVal(tpRec, "TESTER_INFO"));
        out.println("line=" + XmlUtils.getVal(tpRec, "LINE"));
        out.println("xfer_path=" + XmlUtils.getVal(tpRec, "XFER_PATH"));
        out.println("to_plant=" + XmlUtils.getVal(tpRec, "TO_PLANT"));
        out.println("valid_login=" + XmlUtils.getVal(tpRec, "VALID_LOGIN"));
        out.println("prod_login=" + XmlUtils.getVal(tpRec, "PROD_LOGIN"));
        out.println("from_mail=" + XmlUtils.getVal(tpRec, "OWNER_EMAIL"));
        out.println("to_mail=" + XmlUtils.getVal(tpRec, "EMAIL_TO"));
        out.println("cc_mail1=" + XmlUtils.getVal(tpRec, "EMAIL_CC"));
        String ccEmail2 = XmlUtils.getVal(tpRec, "EMAIL_FREE");

        if (GeneralStringUtils.isEmptyString(ccEmail2))
            ccEmail2 = "";

        out.println("cc_mail2=" + ccEmail2);
        out.println("from_plant=" + XmlUtils.getVal(tpRec, "FROM_PLANT"));
        //out.println("certification=" + XmlUtils.getVal(tpRec, "CERTIFICATION"));
        out.println("area_prod=" + XmlUtils.getVal(tpRec, "AREA_PROD"));
        out.println("outfile=" + XmlUtils.getVal(tpRec, "repFileName"));
        String vob = (XmlUtils.getChild(tpRec, "VOB") != null ? XmlUtils.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));
        out.println("trans_vob_list=" + VobManager.getTvobsListAsString(vob));
        String toVob = VobManager.getTvobsListAsString(vob);
        if (GeneralStringUtils.isEmptyString(toVob)){
            throw new TpmsException("Unable to find transitional vob!", "tp delivery action", "The system is unable to find a transitional vob associated with plant " + XmlUtils.getVal(tpRec, "TO_PLANT") + "<br>Please contact your Administrator asking him to verify the vob configuration for the given plant.");
        }
        out.println("to_vob=" + VobManager.getTvobFromPlant(vob, XmlUtils.getVal(tpRec, "TO_PLANT")));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + tpmsConfiguration.getCCDebugClearcaseInterfaceValue());
        //Element userData = CtrlServlet.getUserData(XmlUtils.getVal(tpRec, "OWNER_LOGIN"));
        String unixGroup = tpmsUser.getUnixGroup();
        out.println("unix_group=" + unixGroup);
        //start FP REV 5
        division = tpmsUser.getDivision();
        out.println("division=" + division);
        //stop
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        out.flush();
        out.close();
        debugLog("TpDeliveryAction :: writeBackEndInFile : CC IN FILE CREATED>" + fName);
    }


    private String generateQueryInsertIntoTpPlant(String newVersion) {
        String query = "";
        String delivery_Comment = "";
        String hw_modification ="";
        String tempEXP_AVG_YV="";
        String tempZERO_YW="";
        String tempNEW_TESTTIME="";
        String tempVALID_TILL="";
        String tempIS_TEMP="";
        String tempDIS_EMAIL="";
        TpmsUser tpmsUser = getTpmsUser();
        try {
        	String tRace = this.getAddRace();
        	StringTokenizer tempRace = new StringTokenizer(tRace, "|");
			while(tempRace.hasMoreTokens()) {
				 delivery_Comment = tempRace.nextToken();
				if (delivery_Comment.equals("EMPTY")) {
					delivery_Comment="";
				}
				 hw_modification = tempRace.nextToken();
				if (hw_modification.equals("EMPTY")) {
					hw_modification="";
				}
				 tempEXP_AVG_YV = tempRace.nextToken();
				if (tempEXP_AVG_YV.equals("EMPTY")) {
					tempEXP_AVG_YV="";
				}
				 tempZERO_YW = tempRace.nextToken();
				if (tempZERO_YW.equals("EMPTY")) {
					tempZERO_YW="-1";
				}
				tempNEW_TESTTIME = tempRace.nextToken();
				if (tempNEW_TESTTIME.equals("EMPTY")) {
					tempNEW_TESTTIME="-1";
				}
				 tempIS_TEMP = tempRace.nextToken();
					if (tempIS_TEMP.equals("EMPTY")) {
						tempIS_TEMP="";
					}
				tempVALID_TILL = tempRace.nextToken();
				if (tempVALID_TILL.equals("EMPTY")) {
					tempVALID_TILL="";
				}
				tempDIS_EMAIL = tempRace.nextToken();
				if (tempDIS_EMAIL.equals("EMPTY")) {
					tempDIS_EMAIL="";
				}
			}
            debugLog("TpDeliveryAction :: generateQueryInsertIntoTpPlant : Race Comments : "+delivery_Comment);
            debugLog("TpDeliveryAction :: generateQueryInsertIntoTpPlant : HW Modifications: "+hw_modification);
            debugLog("TpDeliveryAction :: generateQueryInsertIntoTpPlant : EXP_AVG_YV : "+tempEXP_AVG_YV +","+ " ZERO_YW : "+tempZERO_YW+","+" NEW_TESTTIME : "+tempNEW_TESTTIME+","+" IS_TEMP : "+ tempIS_TEMP+","+" VALID_TILL : "+ tempVALID_TILL);
        	delivery_Comment = TP.getDbFormatCommentsFromFieldFormat(delivery_Comment);
        	if(delivery_Comment.length()>TpAction.MAX_COMMENT_LENGTH){
        		delivery_Comment = delivery_Comment.substring(0,TpAction.MAX_COMMENT_LENGTH);
         	}
         	hw_modification = TP.getDbFormatCommentsFromFieldFormat(hw_modification);
         	if(hw_modification.length()>TpAction.MAX_COMMENT_LENGTH){
         		hw_modification = hw_modification.substring(0,TpAction.MAX_COMMENT_LENGTH);
        	}
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
                    XmlUtils.getVal(tpRec, "FROM_PLANT")  + "','" +
                    delivery_Comment + "','" +
                    hw_modification + "','" +
                    tempEXP_AVG_YV + "','" +
                    tempZERO_YW + "','" +
                    tempNEW_TESTTIME + "','" +
                    tempIS_TEMP + "','" +
                    tempVALID_TILL + "','" +
                    tempDIS_EMAIL +
                    "')";
            debugLog("TpDeliveryAction :: generateQueryInsertIntoTpPlant : query : " + query);
        } catch (Exception e) {
            errorLog("TpDeliveryAction :: generateQueryInsertIntoTpPlant : unable to generate query!!", e);
        }
        return query;
    }


    public String generateQueryInsertIntoTpHistory(String newVersion) {
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
                    " PRODUCTION_AREA_ID " +
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
            debugLog("TpDeliveryAction :: generateQueryInsertIntoTpHistory : query : " + query);
        } catch (Exception e) {
            errorLog("TpDeliveryAction:: generateQueryInsertIntoTpHistory : unable to generate query!!", e);
        }
        return query;
    }


    public void doDbTransaction(String queryInsertIntoTpPlant, String queryInsertIntoTpHistory, String newVersion) throws Exception {
        debugLog("TpDeliveryAction :: doDbTransaction : TRANSACTION STARTED");
        try {
            dbwrt.submit(queryInsertIntoTpPlant);
            dbwrt.submit(queryInsertIntoTpHistory);
            dbwrt.commit();
        } catch (Exception e) {
            if (dbwrt != null) {
                try {
                    dbwrt.rollback();
                } catch (SQLException e1) {
                    errorLog("TpDeliveryAction :: doDbTransaction : unable to rollback!", e1);
                }
            }
            String action = "DB UPDATE";
            String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
        }
        debugLog("TpDeliveryAction :: doDbTransaction : TRANSACTION ENDED");
    }

    private String getNewTpJobVersion() {
        String newJobVersion = "99";
        try {
            Element root = XmlUtils.getRoot(repFileName);
            Element tp2El = XmlUtils.getDirChild(root, "TP");
            newJobVersion = XmlUtils.getVal(tp2El, "JOB_VER");
        } catch (Exception e) {
            errorLog("TpDeliveryAction :: getNewTpJobVersion : error retrieving new TP version : " + e.getMessage(), e);
        }
        return newJobVersion;
    }

    public void updateDb() throws Exception {
        Exception DbException = null;
        String newTpJobVersion = getNewTpJobVersion();
        XmlUtils.setVal(tpRec, "JOB_VER", newTpJobVersion);
        String addRowArr = this.getAddRow();
	    if(!GeneralStringUtils.isEmptyString(addRowArr)){
	    	testTpDeliveryInsert(newTpJobVersion);
	    }
	    insertMultiDeliveryComment(newTpJobVersion);
        String queryInsertIntoTpPlant = generateQueryInsertIntoTpPlant(newTpJobVersion);
        String queryInsertIntoTpHistory = generateQueryInsertIntoTpHistory(newTpJobVersion);
        debugLog("TpDeliveryAction :: updateDb : queryInsertIntoTpPlant = " + queryInsertIntoTpPlant + "\nqueryInsertIntoTpHistory = " + queryInsertIntoTpHistory);
        boolean commitBool = false;
        if (!GeneralStringUtils.isEmptyString(queryInsertIntoTpPlant) && !GeneralStringUtils.isEmptyString(queryInsertIntoTpHistory)) {
            for (int i = 0; ((!commitBool) && (i < 3)); i++) {
                commitBool = true;
                try {
                    this.doDbTransaction(queryInsertIntoTpPlant, queryInsertIntoTpHistory, newTpJobVersion);
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
                String [] queries = new String [2];
                queries[0] = queryInsertIntoTpPlant;
                queries[1] = queryInsertIntoTpHistory;
                DBTrack.trackQueries(this.sessionId, this.userName, queries);
                errorLog("TpDeliveryAction :: updateDb : DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
                if (DbException != null) throw DbException;
            }
        } else {
            errorLog("TpDeliveryAction :: updateDb : unable to get queries ( queryInsertIntoTpPlant = " + queryInsertIntoTpPlant + " queryInsertIntoTpHistory = " + queryInsertIntoTpHistory + " newTpJobVersion = " + newTpJobVersion) ;
        }
    }
    /* insert delivery Comment into Action_Comments table*/
    public void  insertMultiDeliveryComment(String newVersion) throws Exception {
        String query = "";
        Exception DbException = null;
        boolean commitBool = false;
        String multiDeliveryCmt = TP.getDbFormatCommentsFromFieldFormat(this.getMultiDeliveryCmt());
        if (multiDeliveryCmt.length()>TpAction.MAX_COMMENT_LENGTH){
        	multiDeliveryCmt = multiDeliveryCmt.substring(0,TpAction.MAX_COMMENT_LENGTH);
     	} 
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
            multiDeliveryCmt + "'," +
            "SYSDATE" + ",'" +   
            "Distributed" + "','" +
            XmlUtils.getVal(tpRec, "LINESET") + "','" +
            XmlUtils.getVal(tpRec, "FROM_PLANT") + "')";
        debugLog("TpDeliveryAction :: InsertMultiDeliveryComment : Query : "+query);
		 commitBool = false;
    	if (!GeneralStringUtils.isEmptyString(query)) {
    		for (int i = 0; ((!commitBool) && (i < 3)); i++) {
    			commitBool = true;
    	        try { 
    	        	this.doDbTransaction(query);
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
    	    	String [] queries = new String [1];
    	        queries[0] = query;
    	        DBTrack.trackQueries(this.sessionId, this.userName, queries);
    	        errorLog("TpDeliveryAction :: insertMultiDeliveryComment : DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
    	        if (DbException != null) throw DbException;
    	    }
    	} else {
    		errorLog("TpDeliveryAction :: insertMultiDeliveryComment : unable to get queries ( queryInsertIntoActionComments = " + query) ;
    	}
           
        }
 
    /* insert Test array value*/
	public void testTpDeliveryInsert(String newVersion)throws Exception{
	    int testNoId = 0;
	    String query ="";
	    Exception DbException = null;
		    StringTokenizer insertAddRow = new StringTokenizer(this.getAddRow(), "~");
			while(insertAddRow.hasMoreTokens()) {
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
					debugLog("TpDeliveryAction :: testTpDeliveryInsert : TestNo : "+testNo+","+" newFlag : "+newFlag +","+" oldLSL : "+oldLSL+","+" oldUSL : "+oldUSL+","+" unit : "+unit+","+" newLSL : "+newLSL +","+" newUSL : "+newUSL+","+" testComment : "+testComment);
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
					debugLog("TpDeliveryAction :: testTpDeliveryInsert : multi query :"+query);
					boolean commitBool = false;
			    	if (!GeneralStringUtils.isEmptyString(query)) {
			    		for (int i = 0; ((!commitBool) && (i < 3)); i++) {
			    			commitBool = true;
			    	        try { 
			    	        	this.doDbTransaction(query);
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
			    	    	String [] queries = new String [1];
			    	        queries[0] = query;
			    	        DBTrack.trackQueries(this.sessionId, this.userName, queries);
			    	        errorLog("TpDeliveryAction :: testTpDeliveryInsert : DB-TRANS-FAILED>" + getDbTransLogMsg(DbException));
			    	        if (DbException != null) throw DbException;
			    	    }
			    	} else {
			    		errorLog("TpDeliveryAction :: testTpDeliveryInsert : unable to get queries ( queryInsertIntoTpPlant = " + query) ;
			    	}
				}
			}
	}	

	public void doDbTransaction(String query) throws Exception {
		debugLog("TpDeliveryAction :: doDbTransaction : TRANSACTION STARTED");
    	try {
    		dbwrt.submit(query);
    	    dbwrt.commit();
    	} catch (Exception e) {
    		if (dbwrt != null) {
    			try {
    				dbwrt.rollback();
    	        } catch (SQLException e1) {
    	        	errorLog("TpDeliveryAction :: doDbTransaction : unable to rollback!", e1);
    	        }
    	    }
    	    String action = "DB UPDATE";
    	    String msg = "DB UPDATE FAILURE - DB NOT ACCESSIBLE";
            throw new TpmsException(msg, action, e);
    	}
    	debugLog("TpDeliveryAction :: doDbTransaction : TRANSACTION ENDED");
	}


}
