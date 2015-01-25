package tpms;

import it.txt.afs.AfsCommonStaticClass;
import it.txt.tpms.users.TpmsUser;
import it.txt.tpms.users.manager.TpmsUsersManager;
import it.txt.general.utils.GeneralStringUtils;
import org.w3c.dom.Element;
import tol.LogWriter;
import tol.dateRd;
import tol.oneConnDbWrtr;
import tol.xmlRdr;
import tpms.utils.QueryUtils;
import tpms.utils.TpmsConfiguration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class TpAction extends AfsCommonStaticClass {

    protected final String divisionNotAvailableValue = "XXXX";
    protected static final int MAX_COMMENT_LENGTH = 2048;

    protected String REQID;
    protected String repFileName;
    protected String action;
    protected String ccInFile;
    protected String ccOutFile;
    protected String cmd;
    protected String actionTxt;
    protected String userName;
    protected Properties params;
    protected Element tpRec;
    protected boolean hasDbActionBool;
    protected oneConnDbWrtr dbwrt;
    protected String webAppDir;
    protected String userRole;
    protected String sessionId;
    private int testrowCount;
    private String addRow;
    private String addRace;
    private String extToValidateCmt;
    private String rejectCmt;
    private String markAsValCmt;
    private String singleDeliveryCmt;
    private String multiDeliveryCmt;
    private String putInProdCmt;
    
    public int getTestRowCount() {
 		return this.testrowCount;
	}
	public void setTestRowCount(int testrowCount) {
		this.testrowCount = testrowCount;
	} 
    public String getAddRow() {
 		return this.addRow;
	}
	public void setAddRow(String addRow) {
		this.addRow = addRow;
	}
	public String getAddRace() {
		return addRace;
	}
	public void setAddRace(String addRace) {
		this.addRace = addRace;
	}
    
    public void setExtToValidateCmt(String extToValidateCmt) 
    { 
	this.extToValidateCmt = extToValidateCmt;
    }
    
    public String getExtToValidateCmt() 
    { 
	return this.extToValidateCmt;
    }
    
    public void setRejectCmt(String rejectCmt) 
    { 
	this.rejectCmt = rejectCmt;
    }
    
    public String getRejectCmt() 
    { 
	return this.rejectCmt;
    }
    
    public String getMarkAsValCmt() 
    { 
	return this.markAsValCmt;
    }
    
    public void setMarkAsValCmt(String markAsValCmt) 
    { 
	this.markAsValCmt = markAsValCmt;
    }
    
    public String getPutInProdCmt() 
    { 
	return this.putInProdCmt;
    }
    
    public void setPutInProdCmt(String putInProdCmt) 
    { 
	this.putInProdCmt = putInProdCmt;
    }
    
    public String getSingleDeliveryCmt() {
		return singleDeliveryCmt;
	}
	public void setSingleDeliveryCmt(String singleDeliveryCmt) {
		this.singleDeliveryCmt = singleDeliveryCmt;
	}
	public String getMultiDeliveryCmt() {
		return multiDeliveryCmt;
	}
	public void setMultiDeliveryCmt(String multiDeliveryCmt) {
		this.multiDeliveryCmt = multiDeliveryCmt;
	}
    
    protected TpmsConfiguration tpmsConfiguration = null;
    LogWriter log = null;

    private TpmsUser tpmsUser =  null;
     
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        
    }
    public String getSessionId() { 
    	return sessionId;
	}

    public TpmsUser getTpmsUser(){
        if (tpmsUser == null && !GeneralStringUtils.isEmptyString(userName)) {
            tpmsUser = TpmsUsersManager.getLocalTpmsUserByTpmsLogin(userName);
        }
        return tpmsUser;
    }

    public void debug(Object msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public void debug(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public TpAction() {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        if (dbwrt == null) {
            try {
                errorLog("TpAction :: constructor : the connection is null, trying to retrieve it");
                this.dbwrt = QueryUtils.getDbConnection();
                if (dbwrt == null) {
                    debugLog("TpAction :: constructor : no error while retrieving connection trial BUT the connection is still null");
                } else {
                    debugLog("TpAction :: constructor : the connection successfully retrieved");
                }
            } catch (Exception e) {
                errorLog("TpAction :: constructor : no way to get the connection, it is null!!! " + e.getMessage(), e);
            }
        } else {
            debugLog("TpAction :: constructor : the connection is not null");
        }
    }

    public String getCcActionUnixLogin(String userLogin, String adminLogin) {
        return userLogin;
    }

    public TpAction(String action, LogWriter log) {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.action = action;
        this.log = log;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAction() {
        return this.action;
    }

    public String getCCAction() {
        return this.action;
    }

    public String getCCAction2() {
        return null;
    }

    public void setCcInFile(String s) {
        ccInFile = s;
    }

    public String getCcInFile() {
        return ccInFile;
    }

    public String getUserName() {
        return userName;
    }

    public void setCcOutFile(String s) {
        ccOutFile = s;
    }

    public String getCcOutFile() {
        return ccOutFile;
    }

    public void setCcCommand(String s) {
        cmd = s;
    }

    public String getCcCommand() {
        return cmd;
    }

    public void setActionTxt(String s) {
        actionTxt = s;
    }

    public String getActionTxt() {
        return actionTxt;
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "tp");
        out.println("action=" + this.getCCAction());
        out.println("outfile=" + repFileName);
        out.println("user=" + this.getUserName());
        out.println("submit=1");
        out.println("diff_outfile=" + repFileName);
        String vob = (xmlRdr.getChild(tpRec, "VOB") != null ? xmlRdr.getVal(tpRec, "VOB") : tpRec.getAttribute("VOB"));
        out.println("vobname=" + vob);
        out.println("session_id=" + SID);
        out.println("debug=" + 0);
        out.println("jobname=" + xmlRdr.getVal(tpRec, "JOBNAME"));
        out.println("release_nb=" + xmlRdr.getVal(tpRec, "JOB_REL"));
        out.println("revision_nb=" + xmlRdr.getVal(tpRec, "JOB_REV"));
        out.println("version_nb=" + xmlRdr.getVal(tpRec, "JOB_VER"));
        for (Enumeration e = params.keys(); e.hasMoreElements();) {
            String param = (String) e.nextElement();
            out.println(param + "=" + params.getProperty(param));
        }
        out.flush();
        out.close();
        debugLog("CC IN FILE CREATED>" + fName);
    }

    public static String bindQry(String qryStr, Properties params) throws TpmsParamException {
        String qryStr2 = "";
        StringTokenizer tok = new StringTokenizer(qryStr, "$#<>=!^*+-/()' \n\t\r\f", true);
        while (tok.hasMoreTokens()) {
            String s = tok.nextToken();
            if (s.equals("$")) {
                String parName = tok.nextToken();
                s = params.getProperty(parName);
                if (s == null) throw new TpmsParamException("ACTION QUERY BINDER CAN'T BIND THE '" + parName + "' PARAMETER", parName, TpmsParamException._MISSING);
            }
            qryStr2 = qryStr2.concat(s);
        }
        return qryStr2;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public void setTpRec(Element tpRec) {
        this.tpRec = tpRec;
    }

    public void setParams(Properties params) {
        this.params = params;
    }

    public void setDbWrtr(oneConnDbWrtr dbwrt) {
        this.dbwrt = dbwrt;
    }

    public void doDbTransaction() throws Exception {
        debugLog("TpAction :: doDbTransaction : empty method!");
    }

    public String getDbTransLogMsg(Exception e) {
        String s = "";
        try {
            s = s + "ACTION:" + this.getAction().toUpperCase() + " ";
            s = s + "USER:" + this.userName + " ";
            s = s + "JOB:" + xmlRdr.getVal(tpRec, "TP_LABEL") + " ";
            s = s + "DATE:" + dateRd.getCurDateTime() + " ";
            if (e != null) {
                s = s + "ERRTYP:" + e.getClass().getName() + " ";
                s = s + "ERRMSG:" + (e.getMessage() != null ? e.getMessage() : "");
            }
        } catch (Exception exc) {
            return null;
        }
        return s;
    }

    public void updateDb() throws Exception {
    	
        Exception DbException = null;
        boolean commitBool = false;
        for (int i = 0; ((!commitBool) && (i < 3)); i++) {
            commitBool = true;
            try {
                debugLog("TpAction :: updateDb : executing doDbTransaction attempt = " + i );
                this.doDbTransaction();

                if (this instanceof TpMarkAsValidAction){
                    debugLog("TpAction :: updateDb : current action is instanceof TpMarkAsValidAction!");
                } else {
                    debugLog("TpAction :: updateDb : current action is NOT instanceof TpMarkAsValidAction! " + this.getClass().getName());
                }

                debugLog("TpAction :: updateDb : executing doDbTransaction attempt = " + i + " successfully ended");
            } catch (Exception e) {
                errorLog("TpAction :: updateDb : exception executing doDbTransaction attempt = " + i + " message = " + e.getMessage(), e);
                DbException = e;
                commitBool = false;
                dbwrt.checkConn();
                this.dbwrt.rollback();
                Thread.sleep(2000);
            }
        }
        if (commitBool)
            this.dbwrt.commit();
        else {
            errorLog("DB-TRANS-FAILED>" + getDbTransLogMsg(DbException), DbException);
            if (DbException != null) throw DbException;
        }
    }

    public void updateDbKoResult() throws Exception{

    }

    public boolean hasDbAction() {
        return hasDbActionBool;
    }

    public void setRepFileName(String s) {
        this.repFileName = s;
    }

    public String getRepFileName() {
        return repFileName;
    }

    public void setReqID(String reqID) {
        this.REQID = reqID;
    }

    public String getReqID() {
        return this.REQID;
    }

    public Element getTpRec() {
        return tpRec;
    }

    public void setWebAppDir(String w) {
        this.webAppDir = w;
    }

    public String getWebAppDir() {
        return this.webAppDir;
    }

    public String toString() {
        try {
            String s = "\n";
            s = s + "ACTION: " + action + "\n";
            s = s + "TP: " + xmlRdr.getVal(tpRec, "TP_LABEL") + "\n";
            return s;
        } catch (Exception e) {
            return "";
        }
    }

}