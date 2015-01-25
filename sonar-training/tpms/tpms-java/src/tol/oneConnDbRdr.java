package tol;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.sql.rowset.CachedRowSet;
import oracle.jdbc.driver.OracleConnection;
import org.w3c.dom.Element;

import tpms.utils.MailUtils;
import com.sun.rowset.CachedRowSetImpl;
import tol.LogWriter;

public class oneConnDbRdr {
	static int _nMaxRows = 1000; /* Maximum N. of Rows fetched */
	static int _nMaxRecordsetRows = 1000000;
	String _drvConnStr;
	String _dbConnStr;
	String _dbUsrStr;
	String _dbPswdStr;
	Connection conn;

	static final String _orclDrvConnStr = "jdbc:oracle:thin:@"; /* PROWAY:1521:PWY2 */

	LogWriter log = null;

	public void setLogWriter(LogWriter log) {
		this.log = log;
	}

	public void debug(Object msg) {
		if (log != null)
			log.p(msg);
	}

	public void debug(String msg) {
		if (log != null)
			log.p(msg);
	}

	public oneConnDbRdr() {
	};

	public oneConnDbRdr(String xmlInitFile) throws SQLException {
		String _dbConnStr, _dbUsrStr, _dbPswdStr;
		String errMsg = "ROOT-ELEMENT NOT FOUND";
		try {
			Element plantEl = xmlRdr.getChild(xmlRdr.getRoot(xmlInitFile, false), "PLANTS");
			errMsg = "USERNAME NOT FOUND";
			_dbUsrStr = xmlRdr.getVal(plantEl, "USERNAME");
			errMsg = "PASSWORD NOT FOUND";
			_dbPswdStr = xmlRdr.getVal(plantEl, "PASSWORD");
			errMsg = "CONN_STR NOT FOUND";
			_dbConnStr = xmlRdr.getVal(plantEl, "CONN_STR");
		} catch (Exception e) {
			throw new SQLException(
					"CAN'T INITIALIZE DB CONNECTION FROM XML DECRIPTOR: "
							+ errMsg + " (" + e.toString() + ")");
		}
		initConn(_dbConnStr, _dbUsrStr, _dbPswdStr);
	}

	public oneConnDbRdr(String _host, String _lstnrPort, String _sid,
			String _dbUsrStr, String _dbPswdStr) throws SQLException {
		String _dbConnStr = new String(_host + ":" + _lstnrPort + ":" + _sid);
		initConn(_dbConnStr, _dbUsrStr, _dbPswdStr);
	}

	public oneConnDbRdr(String _dbConnStr, String _dbUsrStr, String _dbPswdStr)
			throws SQLException {
		initConn(_dbConnStr, _dbUsrStr, _dbPswdStr);
	}

	public void initConn(String _dbConnStr, String _dbUsrStr, String _dbPswdStr)
			throws SQLException {
		this._drvConnStr = _orclDrvConnStr;
		this._dbConnStr = _dbConnStr;
		this._dbUsrStr = _dbUsrStr;
		this._dbPswdStr = _dbPswdStr;
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		conn = DriverManager.getConnection(_drvConnStr.concat(_dbConnStr),
				_dbUsrStr, _dbPswdStr);
		((OracleConnection) conn).setDefaultRowPrefetch(100);
		
		Statement stat = null;
		
		try {
			stat = conn.createStatement();
			stat.execute("alter session set nls_language = 'English'");
			conn.commit();
		} catch(Exception e){
			conn.rollback();
			throw new SQLException(e.getMessage());
		} finally {			
			if (stat!=null) stat.close();
		}
	}

	public Connection getDbConnection() {
		return conn;
	}

	public void close() throws SQLException {
		if (!conn.isClosed()){
			conn.close();
		}
	}

	/**
	 * Fills with values fetched from the DB the L.O.V. (the <i>v</i> vector)
	 * and its primary attributes, in case <i>mode=false</i>, its secondary
	 * attributes instead in case <i>mode=true</i>.
	 */
	public synchronized void getRows(Vector v, boolean mode, attrLst attrs,
			String qrStr) throws SQLException {
		this.getRows(v, null, mode, attrs, qrStr);
	}

	/**
	 * Writes the recordset as an XML document onto the <i>out</i> output
	 * stream without filling the L.O.V. and the attributes vectors.
	 */
	public synchronized void getRows(Writer out, boolean mode, attrLst attrs,
			String qrStr) throws SQLException {
		this.getRows(null, out, mode, attrs, qrStr);
	}

	protected synchronized void getRows(Vector v, Writer out, boolean mode,
			attrLst attrs, String qrStr) throws SQLException {
		
		Statement stmt = null;
		ResultSet rset = null;
		try {
			stmt = conn.createStatement();
			stmt.setMaxRows(this._nMaxRows);
			rset = stmt.executeQuery(qrStr);
		if ((attrs != null) && (out == null))
			attrs.clear(mode);
		while (rset.next()) {
			if (rset.getString(1) == null)
				continue;

			// if the fetch is not detailed, the v vector (which has been
			// previously cleared) is affected
			// I add an element to v only if it is a new item
			if ((!mode) && (out == null)) {
				String val = rset.getString(1).trim();
				boolean found = false;
				for (int i = 0; i < v.size(); i++) {
					if (((String) v.elementAt(i)).equals(val)) {
						found = true;
						break;
					}
				}
				if (!found) {
					v.addElement(val);
				}
			}

			if (attrs != null) {
				try {
					if (out == null)
						attrs.getRow(mode, rset);
					else
						attrs.getRow(mode, rset, out);
				} catch (Exception e) {
					if (e instanceof SQLException)
						throw (SQLException) e;
				}
			} else {
				try {
					out.write("<STDATA_RECORD>");
					out.write("<" + "FIELD" + ">");
					out.write(rset.getString(1) == null ? "null" : xmlRdr
							.format(rset.getString(1)));
					out.write("</" + "FIELD" + ">");
					out.write("</STDATA_RECORD>");
				} catch (Exception e) {
					if (e instanceof SQLException)
						throw (SQLException) e;
				}
			}
		}
		conn.commit();
		} catch(Exception e){
			conn.rollback();
			throw new SQLException(e.getMessage());
		} finally { 
			if (rset != null) rset.close();
			if (stmt != null) stmt.close();
		}
	}

	public synchronized ResultSet getRecordset(String qrStr)
			throws SQLException {
		return getRecordset(qrStr, false);
	}
	
	public synchronized CachedRowSet getCacheset(String qrStr)
			throws SQLException {
		return getCacheset(qrStr, false);
	}

	public synchronized ResultSet getRecordset(String qrStr, boolean limitBool)
			throws SQLException {

		Statement stmt = null;
		ResultSet rset = null;
		CachedRowSet cacheRowSet = new CachedRowSetImpl();
		try {
			stmt = conn.createStatement();
			if (limitBool)
				stmt.setMaxRows(this._nMaxRecordsetRows);
			rset = stmt.executeQuery(qrStr);
			cacheRowSet.populate(rset);
			conn.commit();		
		} catch (Exception e) {
			conn.rollback();
			String strQuery="oneConnDbRdr :: getRecordset ERROR";
			MailUtils.sendMailImpl(strQuery);
			throw new SQLException(e.getMessage());
		} finally {
			 if (rset != null){
			    try {
			    	rset.close();
			    	rset = null;
			    }
			    catch( SQLException e ){}
			}
			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
			    }
			    catch( Exception e ){}
			}
		}
		return cacheRowSet;
	}

	public synchronized CachedRowSet getCacheset(String qrStr, boolean limitBool)
	throws SQLException {

		Statement stmt = null;
		ResultSet rset = null;
		CachedRowSet cacheRowSet = new CachedRowSetImpl();		
		try {
			stmt = conn.createStatement();
			if (limitBool)
				stmt.setMaxRows(this._nMaxRecordsetRows);
			rset = stmt.executeQuery(qrStr);
			cacheRowSet.populate(rset);
			conn.commit();		
		} catch (Exception e) {
			conn.rollback();
			String strQuery="oneConnDbRdr :: getCacheset ERROR";
			MailUtils.sendMailImpl(strQuery);
			throw new SQLException(e.getMessage());
		} finally {
			if (rset != null){
				try {
					rset.close();
					rset = null;
				}
				catch( Exception e ){}
			}
			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
				}
				catch( Exception e ){}
			}
		}
		return cacheRowSet;
	}
	
	/**
	 * Debug method
	 */
	public synchronized void submitFile(String flName, Vector v)
			throws SQLException {
		String qrStr;
		StringBuffer sb = new StringBuffer();
		Vector vt = new Vector();
		try {
			flRdr fl = new flRdr(flName);
			fl.getRows(vt);
		} catch (IOException e) {
			debug("CAN'T READ THE STATEMENT>" + e);
		}
		for (int i = 0; i < vt.size(); i++) {
			sb.append(vt.elementAt(i));
			sb.append("\n");
		}
		qrStr = sb.toString();
		debug(qrStr);
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(qrStr);
		while (rset.next()) {
			v.addElement(rset.getString(1));
			debug(rset.getString(1));
		}
	}

	// ------------------------//
	// --- FORMATTING FUNCT ---//
	// ------------------------//
	public String applyTO_CHAR(String f, boolean mode) {
		return applyTO_CHAR(f, mode, null);
	}

	public String applyTO_CHAR(String f, boolean mode, String format) {
		String timeFormat = ((format != null) && (format.equals("TIME")) ? " HH24:MI:SS"
				: "");
		String s = new String("TO_CHAR(");
		s = s.concat(f);
		if (!mode)
			s = s.concat(",'DD/MON/YYYY" + timeFormat + "')");
		else
			s = s.concat(",'YYYY/MM/DD')");
		return (s);
	}

	public String applyTO_DATE(String f, boolean lowBndBool)
	// lowBndBool=true means that I request a lower bound
	// (otherwise i've requested an upper bound
	{
		String lowBndStr = "0:00:00";
		String upBndStr = "23:59:59";
		String timeStr = (lowBndBool ? lowBndStr : upBndStr);
		String s = new String("TO_DATE('");
		s = s.concat(f).concat(" ");
		s = s.concat(timeStr).concat("'");
		if (f.length() == 11)
			s = s.concat(",'DD/MON/YYYY HH24:MI:SS')");
		else
			s = s.concat(",'DD/MM/YYYY HH24:MI:SS')");
		return (s);
	}

	/**
	 * @date=07/07/2003
	 */
	public void checkConn() throws SQLException {
		if (this.conn.isClosed()) {
			this.initConn(_dbConnStr, _dbUsrStr, _dbPswdStr);
		}
	}

	/**
	 * @date=07/07/2003
	 */
	public boolean isClosed() throws SQLException {
		return this.conn.isClosed();
	}

	/**
	 * @date=07/07/2003
	 */
	public synchronized void getRows(Vector v, String qrStr)
			throws SQLException {
		getRows(v, null, false, null, qrStr);
	}

};
