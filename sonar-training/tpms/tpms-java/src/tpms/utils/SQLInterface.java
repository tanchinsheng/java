package tpms.utils;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.Base64;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.tpms.tplib.TPLibProcess;
import it.txt.tpms.tplib.logger.Logger;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tol.oneConnDbWrtr;
import tol.xmlRdr;
import tpms.TpmsException;

import com.sun.rowset.CachedRowSetImpl;

public class SQLInterface extends AfsGeneralServlet {

	private static final long serialVersionUID = 1L;
	private CachedRowSetImpl crs;

	public SQLInterface() {}

	public CachedRowSet execQuery(String query) {
		Statement pStat = null;
		oneConnDbWrtr dbwrt = null;
		ResultSet rs = null;

		try {
			dbwrt = getConnection();
			pStat = dbwrt.getDbConnection().createStatement();
			rs = pStat.executeQuery(query);

			// create CachedRowSet and populate
			crs = new CachedRowSetImpl();
			crs.populate(rs);

		} catch (SQLException se) {
			errorLog("SQLInterface :: execQuery : SQL Exception" + se.getMessage(), se );
		} catch (Exception e) {
			errorLog("SQLInterface :: execQuery : Exception" + e.getMessage(), e );
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
				Logger.reportError("SQLInterface :: execQuery : Fail to close resources!");
			}
		}
		return crs;
	}

	protected oneConnDbWrtr getConnection() throws Exception {
		
		oneConnDbWrtr dbwrt = null;
		try {
	        if (tpmsConfiguration == null)
	            tpmsConfiguration = TpmsConfiguration.getInstance();
			String _webAppDir = tpmsConfiguration.getWebAppDir();
			String plantInitDir = _webAppDir + File.separator + "cfg" + File.separator + "local_cfg";
	        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        
	        Document doc = documentBuilder.parse(new File(plantInitDir + File.separator + "plants.xml"));
	        Element dbInfoElement = (Element) doc.getDocumentElement().getElementsByTagName("PLANTS").item(0);
	        String username = xmlRdr.getVal(dbInfoElement, "USERNAME");
	        String pwd = xmlRdr.getVal(dbInfoElement, "PASSWORD");
	        pwd = decriptString(pwd);
	        String connectionString = xmlRdr.getVal(dbInfoElement, "CONN_STR");
	        dbwrt = new oneConnDbWrtr(connectionString, username, pwd);
		} catch (Exception e) {
			//throw e;
			String subject = TPLibProcess.mailSubject(": SQLInterface Fail to get Connection!");
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
        	errorLog("SQLInterface :: closeConnection : error closing DB connection!" + e.getMessage());
        }
    }
   
   public void executeMultiple(Vector sqlVec) throws TpmsException{
	   oneConnDbWrtr dbwrt = null;
	   Statement stmt = null;
	   
	   try {
		   dbwrt = getConnection();
		   stmt = dbwrt.getDbConnection().createStatement();
		   Iterator it = sqlVec.iterator();
		   while(it.hasNext()) {
			   Object obj = it.next();
			   if(obj instanceof String) {
				   String sqlStr = (String) obj;
				   debugLog("SQLInterface :: execMultiple : " + sqlStr);
				   stmt.execute(sqlStr);
			   }			   
		   }		   
		   dbwrt.commit();
	   } catch (Exception e) {
			try {
				dbwrt.rollback();
			} catch (SQLException se) {
				errorLog("SQLInterface :: execMultiple : rollback() : SQLException" + se.getMessage(), se);
			}
			errorLog("SQLInterface :: execMultiple : Exception" + e.getMessage(), e );
			throw new TpmsException(e.getMessage());
	   } finally {
		   try {			
			   if (stmt != null) {
				   stmt.close();
				   stmt = null;
			   }
			   if (dbwrt != null) {
				   closeConnection(dbwrt);
				   dbwrt = null;
			   }
		   } catch (Exception e) {
			   Logger.reportError("SQLInterface :: execMultiple : Fail to close resources!");
			   throw new TpmsException(e.getMessage());
		   }
	   }
   }
   
   public void execute(String sqlStr) throws TpmsException{
	   oneConnDbWrtr dbwrt = null;
	   Statement stmt = null;
	   
	   try {
		   dbwrt = getConnection();
		   stmt = dbwrt.getDbConnection().createStatement();
		   debugLog("SQLInterface :: execute : " + sqlStr);
		   stmt.execute(sqlStr);		   
		   dbwrt.commit();
	   } catch (Exception e) {
			try {
				dbwrt.rollback();
			} catch (SQLException se) {
				errorLog("SQLInterface :: execute : rollback() : SQLException" + se.getMessage(), se);
			}
			errorLog("SQLInterface :: execute : Exception" + e.getMessage(), e );
			throw new TpmsException(e.getMessage());
	   } finally {
		   try {			
			   if (stmt != null) {
				   stmt.close();
				   stmt = null;
			   }
			   if (dbwrt != null) {
				   closeConnection(dbwrt);
				   dbwrt = null;
			   }
		   } catch (Exception e) {
			   Logger.reportError("SQLInterface :: execute : Fail to close resources!");
			   throw new TpmsException(e.getMessage());
		   }
	   }
   }
   
   public boolean checkDBConnection() {
	   
	   oneConnDbWrtr dbwrt = null;
	   Statement stmt = null;
	   ResultSet rs = null;
	   boolean result = false;
	   int dbConnectionCheckInt = 1;
	   String query = "select " + dbConnectionCheckInt + " as TEST_VALUE from dual";
	   try {
		  
		   dbwrt = getConnection();
		   stmt = dbwrt.getDbConnection().createStatement();
		   rs = stmt.executeQuery(query);
		   if (rs != null) {
			   rs.next();
			   if (dbConnectionCheckInt == rs.getInt("TEST_VALUE")) {
				   result = true;
			   } 
		   }
	   } catch (Exception e) {			
			errorLog("SQLInterface :: checkDBConnection : Exception" + e.getMessage(), e );	
			result = false;
	   } finally {
		   try {
			   if (rs != null) {
				   rs.close();
			   }
			   if (stmt != null) {
				   stmt.close();
			   }
			   if (dbwrt != null) {
				   closeConnection(dbwrt);
			   }
		   } catch (Exception e) {
			   Logger.reportError("SQLInterface :: checkDBConnection : Fail to close resources!");			   
		   }
	   }
	   debugLog(this.getClass().getName() + " :: checkDBConnection : Database Alive = " + result);
	   return result;
   }
}