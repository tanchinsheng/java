package it.txt.tpms.tplib.actions;

import it.txt.general.utils.Base64;
import it.txt.general.utils.FileUtils;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.tplib.logger.Logger;
import it.txt.tpms.tplib.xml.XmlReader;
import it.txt.tpms.tplib.TPLibProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tol.oneConnDbWrtr;
import tol.xmlRdr;
import tpms.utils.MailUtils;
import tpms.utils.TpmsConfiguration;

public abstract class TPLibAction {
	protected XmlReader reader;
	protected String tpmsDir;
	protected String sid;
	protected String inFileName;
	protected String outFileName;
	protected String packageExtension;
	protected String daemonName;
	protected HashMap tpData;

	protected String vob;
	protected String jobName;
	protected String release;
	protected String revision;
	protected String version;
	protected String host;
	
	public TPLibAction(){
		tpData = new HashMap();
		Logger.debug("TPLibAction :: TPLibAction : Starting action " + getLabel() + "...");
	}

	public void setReader(XmlReader reader) {
		this.reader = reader;

		vob = reader.getValue("VOB");
		jobName = reader.getValue("JOB_NAM");
		release = reader.getValue("JOB_REV");
		revision = reader.getValue("JOB_VERSION");
		version = reader.getValue("TPMS_JOB_VER");
		host = reader.getValue("HOSTNAME");
	}

	public XmlReader getReader() {
		return reader;
	}

	public String getTpmsDir() {
		return tpmsDir;
	}

	public void setTpmsDir(String dir) {
		tpmsDir = dir;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getInFileName() {
		return inFileName;
	}

	public String getDaemonName(){
		return daemonName;
	}
	
	public void setInFileName(String inFileName) {
		this.inFileName = inFileName;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public void setPackageExtension(String ext){
		packageExtension = ext;
	}

	public void setDaemonName(String daemon){
		daemonName = daemon;
	}
	
	public boolean writeBackEndInFile() {
		PrintWriter out = null;
		boolean result = true;
		boolean dataResult = getDataFromTpPlant() && getDataFromTpHistory();

		Logger.debug("TPLibProcess :: writeBackEndInFile : starting...");
		if (!dataResult) {
			return false;
		}

		String xmlFileName = reader.getSourceFileName();
		String packageFile = GeneralStringUtils.replace(xmlFileName, ".xml", "." + packageExtension);

		try {
			out = new PrintWriter(new FileWriter(inFileName));
			out.println("user=" + (String)tpData.get("user"));
			out.println("rectype=tp");
			out.println("action=" + getActionName());
			out.println("submit=1");
			out.println("outfile=" + outFileName);
			out.println("vobname=" + vob);
			out.println("session_id=" + sid);
			out.println("debug=0");
			out.println("jobname=" + jobName);
	        out.println("release_nb=" + release);
	        out.println("revision_nb=" + revision);
	        out.println("version_nb=" + version);
	        out.println("tester_info=" + (String)tpData.get("tester_info"));
			out.println("file_path=" + packageFile);
		} catch (Exception e) {
			Logger.error(getLabel() + " :: writeBackEndInFile : " + e.getMessage());
			result = false;
		} finally {
			if (out != null){
				out.flush();
		        out.close();
		        out = null;
			}
		}
		return result;
	}

	public int execute(){
		String cmd = "rsh 127.0.0.1 -n -l " + getDaemonName() + " /tpms/scripts/ExecFiles/webmain_tpmsw.sh ";
		int result = -1;

		try {
			Logger.debug("TPLibAction :: execute : Calling " + cmd + inFileName + " " + outFileName);
			Process p = Runtime.getRuntime().exec(cmd + inFileName + " " + outFileName);
			result = p.waitFor();
		} catch (Exception e) {
			Logger.error(getLabel() + "::execute: ACTION ABORTED - CC COMMAND EXECUTION FAILURE");
			e.printStackTrace();
			result = -1;
		}
		return result;
	}

	public boolean readBackEndOut(){
		File f = new File(outFileName);
		if (!f.exists()) {
			Logger.error(getLabel() + " :: readBackEndOut : The output file does not exists");
			return false;
		}

		BufferedReader reader = null;
		String line = "";
        String userMsg = "";
        String sysMsg = "";
        String trackMsg = "";
        boolean result = false;

		try {
			reader = new BufferedReader(new FileReader(outFileName));
	        while ((line = reader.readLine()) != null){
	        	if (line.startsWith("track=")){
	        		trackMsg = line.substring(6);
	        	} else if (line.equals("esit=0")){
	        		result = true;
	        	} else if (line.equals("esit=1")){
	        		result = false;
	        	} else if (line.startsWith("usermsg=")){
	        		userMsg = line.substring(8);
	        	} else if (line.startsWith("sysmsg=")){
	        		sysMsg = line.substring(7);
	        	}
	        }
		}	catch (Exception e)	{
			Logger.reportError(getLabel() + " :: readBackEndOut : " + e.getMessage());
		} finally {
			try {
				reader.close();
				reader = null;
			} catch(Exception e) {
				Logger.reportError(getLabel() + " :: readBackEndOut : " + "Fail to close reader!");
			}
		}

        if (!result){
        	Logger.error(getLabel() + " :: readBackEndOut : Errors during execution of back-end action:");
        	Logger.error("track=" + trackMsg);
        	Logger.error("userMsg=" + userMsg);
        	Logger.error("sysMsg=" + sysMsg);
        }
		return result;
	}

	public boolean updateDb(){
		if (updateTpPlant()) {
			return updateTpHistory();
		}
		return false;
	}

	protected boolean getDataFromTpPlant(){
		ResultSet rs = null;
		Statement pStat = null;
		oneConnDbWrtr dbwrt = null;
		boolean result = true;

		try {
			dbwrt = getConnection();
			String query = "SELECT TESTER_INFO, LAST_ACTION_ACTOR FROM TP_PLANT WHERE " +
					"JOBNAME = '" + jobName + "' AND " +
					"JOB_RELEASE = '" + release + "' AND " +
					"JOB_REVISION = '" + revision + "' AND " +
					"TPMS_VER = '" + version + "' AND " +
					"STATUS = '" + getInitialStatus() + "'";

			Logger.debug("debug :" + query);
			pStat = dbwrt.getDbConnection().createStatement();
			rs = pStat.executeQuery(query);
			Logger.debug(getLabel() + " :: getDataFromTpPlant : executing query:");
			Logger.debug(query);

			if ((rs != null) && rs.next()) {
				tpData.put("tester_info", rs.getString("TESTER_INFO"));
				tpData.put("user", rs.getString("LAST_ACTION_ACTOR"));
			} else {
				Logger.error(getLabel() + " :: getDataFromTpPlant :TP data not found in TP_PLANT!");
				result = false;
			}
		} catch (Exception e) {
			Logger.error(getLabel() + " :: getDataFromTpPlant : " + e.getMessage());
			result = false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pStat != null) {
					pStat.close();
					pStat = null;
				}
				if (dbwrt != null) {
					closeConnection(dbwrt);
					dbwrt = null;
				}
			} catch (Exception e) {
				Logger.reportError("TPLibAction :: getDataFromTpPlant : Fail to close resources!");
			}
		}
		return result;
	}

	protected boolean getDataFromTpHistory(){
		ResultSet rs = null;
		Statement pStat = null;
		oneConnDbWrtr dbwrt = null;
		boolean result = true;

		try {
			dbwrt = getConnection();
			String query = "SELECT DIVISION, OWNER_LOGIN, INSTALLATION_ID, LINESET_NAME, " +
				"PRODUCTION_AREA_ID FROM TP_HISTORY WHERE " +
				"JOBNAME = '" + jobName + "' AND " +
				"JOB_RELEASE = '" + release + "' AND " +
				"JOB_REVISION = '" + revision + "' AND " +
				"TPMS_VER = '" + version + "' AND " +
				"ACTOR = '" + (String)tpData.get("user") + "' AND " +
				"STATUS = '" + getInitialStatus() + "'";

			pStat = dbwrt.getDbConnection().createStatement();
			rs = pStat.executeQuery(query);
			Logger.debug(getLabel() + " :: getDataFromTpHistory : executing query:");
			Logger.debug("debug :" + query);

			if((rs != null) && rs.next()){
				tpData.put("division", rs.getString("DIVISION"));
				tpData.put("login", rs.getString("OWNER_LOGIN"));
				tpData.put("from_plant", rs.getString("INSTALLATION_ID"));
				tpData.put("lineset", rs.getString("LINESET_NAME"));
				tpData.put("area_prod", rs.getString("PRODUCTION_AREA_ID"));
			} else {
				Logger.error(getLabel() + " :: getDataFromTpHistory : TP data not found in TP_HISTORY!");
				result = false;
			}
		} catch (Exception e) {
			Logger.error(getLabel() + " :: getDataFromTpHistory : " + e.getMessage());
			result = false;
		} finally {
			try	{
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pStat != null) {
					pStat.close();
					pStat = null;
				}
				if (dbwrt != null) {
					closeConnection(dbwrt);
					dbwrt = null;
				}
			} catch (Exception e){
				Logger.reportError("TPLibAction :: getDataFromTpHistory : Fail to close resources!");
			}
		}
		return result;
	}

	protected oneConnDbWrtr getConnection() throws Exception{
		oneConnDbWrtr dbwrt = null;

		try {
	        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        
	        Document doc = documentBuilder.parse(new File(tpmsDir + "cfg" + File.separator + "local_cfg" + File.separator + "plants.xml"));
	        Element dbInfoElement = (Element) doc.getDocumentElement().getElementsByTagName("PLANTS").item(0);
	        String username = xmlRdr.getVal(dbInfoElement, "USERNAME");
	        String pwd = xmlRdr.getVal(dbInfoElement, "PASSWORD");
	        pwd = decriptString(pwd);
	        String connectionString = xmlRdr.getVal(dbInfoElement, "CONN_STR");
	        dbwrt = new oneConnDbWrtr(connectionString, username, pwd);
		} catch (Exception e) {
			//throw e;
			String subject = TPLibProcess.mailSubject(": TPLIB Fail to get Connection!");
			TPLibProcess.sendMail(subject,"Fail to get Connection!");
		}
		return dbwrt;
	}

	protected String decriptString(String criptedString) {
		final int seed = 5; // Do not change this parameter.

        String result = criptedString;
        if (!GeneralStringUtils.isEmptyString(result)) {
            for (int i = 0; i < seed; i++) {
                byte[] clear = Base64.decode(result);
                if (clear != null && clear.length > 0)
                    result = new String(clear);
            }
        }
        return result;
    }

    protected void closeConnection(oneConnDbWrtr dbwrt){
        try {
            if (dbwrt != null) dbwrt.close();
        } catch (Exception e){
        	Logger.error("TPLibAction :: closeConnection : error closing DB connection!" + e.getMessage());
        }
    }

    protected boolean updateTpPlant(){
    	boolean result = true;
    	
        String query = "UPDATE TP_PLANT SET" +
        	" LAST_ACTION_ACTOR ='" + (String)tpData.get("user") +
        	"', LAST_ACTION_DATE = SYSDATE" +
        	", STATUS ='" + getNewStatus() +"'" +
        	", PROD_DATE = SYSDATE" +
        	", VOB_STATUS = 'OnLine'" +
        	" WHERE " +
        	" JOBNAME='" + jobName + "' AND " +
        	" JOB_RELEASE=" + release + " AND " +
        	" JOB_REVISION='" + revision + "' AND " +
        	" TPMS_VER=" + version;

        oneConnDbWrtr dbwrt = null;

       	try {
       		dbwrt = getConnection();
       	} catch (Exception e) {
       		Logger.reportError("TPLibAction :: updateTpPlant : unable to connect to the oracle server!");  		
       	}

        boolean commitBool = false;
        for (int i = 0; ((!commitBool) && (i < 3)); i++) {
        	commitBool = true;
        	try {
        		dbwrt.submit(query);
        		dbwrt.commit();
        	} catch (Exception e) {
        		commitBool = false;
        		if (dbwrt != null) {
        			try {
        				dbwrt.rollback();
        			} catch (SQLException e1) {
        				Logger.error(getLabel() + " :: updateTpPlant : unable to rollback!");
        			}
        		}
            result = false;
        	} finally {
        		if (dbwrt != null) { 
        			closeConnection(dbwrt);
        			dbwrt = null;
        		}
        	}
        }
        //commitBool = false; //For UTC, set false by removing "//".
        if (!commitBool) {
        	manageLostQuery(query);
        	String subject = TPLibProcess.mailSubject(": TPLIB database updating error!");
        	TPLibProcess.sendMail(subject,query);
        }
        return result;
    }

    protected boolean updateTpHistory(){
    	boolean result = true;

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
        	jobName + "'," +
        	release + ",'" +
        	revision + "'," +
        	version + "," +
        	"'" + (String)tpData.get("user") + "'," +
        	"SYSDATE,'" +
        	getNewStatus() + "','" +
			(String)tpData.get("division") + "','" +
			"OnLine','" +
			(String)tpData.get("login") + "','" +
			(String)tpData.get("from_plant") + "','" +
			(String)tpData.get("lineset") + "','" +
			(String)tpData.get("area_prod") +
			"')";

        oneConnDbWrtr dbwrt = null;

        try {
        	dbwrt = getConnection();
        } catch(Exception e) {
        	Logger.reportError("TPLibAction :: updateTpHistory : unable to connect to the oracle server!");
        }

        boolean commitBool = false;
        for (int i = 0; ((!commitBool) && (i < 3)); i++) {
        	commitBool = true;
        	try {
        		dbwrt.submit(query);
        		dbwrt.commit();
        	} catch (Exception e) {
        		commitBool = false;
        		if (dbwrt != null) {
        			try {
        				dbwrt.rollback();
        			} catch (SQLException e1) {
        				Logger.error(getLabel() + " :: updateTpHistory : unable to rollback!");
        			}
        		}
        		result = false;
        	} finally {
        		if (dbwrt != null) {
        			closeConnection(dbwrt);
        			dbwrt = null;
        		}
        	}
        }
        commitBool = false; //For testing purpose, set false
        if (!commitBool) {
        	manageLostQuery(query);   		
    		String subject = TPLibProcess.mailSubject(": TPLIB database updating error!");
    		TPLibProcess.sendMail(subject,query);
        }
        return result;
    }

    private void manageLostQuery(String theQuery){
    	final String trackDir = TpmsConfiguration.getInstance().getDbTrackLogDir();
    	final String trackFileName =  host + "_" + (String)tpData.get("user") + "_" + System.currentTimeMillis() + ".log";
    	
    	try {
    		FileUtils.writeToFile(trackDir + "/" + trackFileName, theQuery);
    	} catch (Exception e) {
    		Logger.error("Unable to manage the lost query into the file " + trackDir + trackFileName);
    		Logger.error("The lost query is:");
    		Logger.error(theQuery);
			String subject = TPLibProcess.mailSubject(": Manage Lost Query Error!");
			TPLibProcess.sendMail(subject,theQuery);
    	}
    }

    public void deleteInOut() {
    	File inFile = new File(inFileName);
    	File outFile = new File(outFileName);

    	if (!inFile.delete()) {
    		Logger.error("TPLibAction :: deleteInOut : unable to delete the inFile " + inFileName);
    	} else {
    		Logger.debug(inFileName + " successfully deleted");
    	}

    	if (!outFile.delete()) {
    		Logger.error("TPLibAction :: deleteInOut : unable to delete the outFile " + outFileName);
    	} else {
    		Logger.debug(outFileName + " successfully deleted");
    	}
    }
	
    public abstract String getActionName();
    public abstract String getLabel();
    public abstract String getInitialStatus();
    public abstract String getNewStatus();
}
