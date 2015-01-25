package tol;

import it.txt.afs.AfsCommonStaticClass;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;

import javax.sql.rowset.CachedRowSet;

import org.w3c.dom.*;

import tpms.utils.MailUtils;
import tpms.utils.TpmsConfiguration;

import com.sun.rowset.CachedRowSetImpl;

public class dbRdr
{
 public static final String _plantCfgFileName  ="plants.xml";
 public static final String _orclDbmsType      ="ORACLE";
 public static final String _ingresDbmsType    ="INGRES";
 public static final String _fileSysDbModel    ="FILESYS";

 String _dbmsType;      //dbms type
 String _drvConnStr;    //driver identification string
 String _dbConnStr;     //used by the 1st constructor only
 String _dbUsrStr;      //used by the 1st constructor only
 String _dbPswdStr;     //used by the 1st constructor only
 Element plantRootEl;   //It's the root element of the plant
                        //configuration file
 int nc;                //connections number
 int curConn;           //current connection index
 oneConnDbRdr cons[];   //connections array
 Boolean free[];        //connections allocation flags
 dbBinder binder;       //the binds returns join conditions
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public dbRdr(String plantInitDir, String plantName, String modelInitDir, String model, int nc) throws Exception
 {
   Element root=xmlRdr.getRoot(plantInitDir.concat("/"+_plantCfgFileName),false);
   Vector vL=new Vector(); Vector vR=new Vector();
   vL.addElement("PL_NAME"); vL.addElement("MODEL_ID");
   vR.addElement(plantName); vR.addElement(model);
   if ((plantRootEl=xmlRdr.findEl(root.getChildNodes(), vL, vR))==null) throw new IOException("NO MATCHING ENTRY INTO THE PLANTS CONFIG FILE>");
   if (!model.equals(_fileSysDbModel))
   {
     initDbRdr(modelInitDir, nc, xmlRdr.getVal(plantRootEl,"DBS_TYPE"), xmlRdr.getVal(plantRootEl,"CONN_STR"), xmlRdr.getVal(plantRootEl,"USERNAME"), xmlRdr.getVal(plantRootEl,"PASSWORD"));
   }
   //debug(toString());//debug
 }

 public dbRdr(String modelInitDir, int nc, String dbmsType, String dbConnStr, String dbUsrStr, String dbPswdStr) throws Exception
 {
   initDbRdr(modelInitDir, nc, dbmsType, dbConnStr, dbUsrStr, dbPswdStr);
 }

 public void initDbRdr(String modelInitDir, int nc, String dbmsType, String dbConnStr, String dbUsrStr, String dbPswdStr) throws Exception
 {
  this._dbmsType  = dbmsType;
  this._dbConnStr = dbConnStr;
  this._dbUsrStr  = dbUsrStr;
  this._dbPswdStr = dbPswdStr;
  this.nc = nc;
  curConn = 0;
  cons = new oneConnDbRdr[nc];
  free = new Boolean[nc];
  for (int i=0; i<nc; i++)
  {
    cons[i]=getNewConnDbRdr(_dbmsType, _dbConnStr, _dbUsrStr, _dbPswdStr);
    free[i]=Boolean.TRUE;
  }
  binder = new dbBinder(modelInitDir);
 }

 public oneConnDbRdr getNewConnDbRdr() throws SQLException
 {
     return getNewConnDbRdr(this._dbmsType, this._dbConnStr, this._dbUsrStr, this._dbPswdStr);
 }

 public oneConnDbRdr getNewConnDbRdr(String dbmsType, String _dbConnStr, String _dbUsrStr, String _dbPswdStr) throws SQLException
 {
   if (dbmsType.equals(this._orclDbmsType))
       return new oneConnDbRdr(_dbConnStr, _dbUsrStr, _dbPswdStr);
   if (dbmsType.equals(this._ingresDbmsType))
       return new oneConnIngresDbRdr(_dbConnStr, _dbUsrStr, _dbPswdStr);
   return null;
 }

 public void close() throws SQLException
 {
  for (int i=0; i<nc; i++)
  {
    cons[i].close();
    free[i] = Boolean.FALSE;
  }
 }


 protected synchronized int getConn()
 {
  int ret = curConn;
  boolean free = false;
  for (int k=0; (!free)&&(k<nc); k++)
  {
    ret = curConn;
    free = this.free[curConn].booleanValue();
    curConn++;
    if (curConn>=nc) curConn=0;
  }
  return ret;
 }

 public oneConnDbRdr getConnection()
 {
     int conn = getConn();
     return cons[conn];
 }

 public void getRows(Vector v, boolean mode, attrLst attrs, String query) throws SQLException
 {
   this.getRows(v, null, mode, attrs, query);
 }

 public void getRows(Writer out, boolean mode, attrLst attrs, String query) throws SQLException
 {
   this.getRows(null, out, mode, attrs, query);
 }

 public void getRows(Vector v, Writer out, boolean mode, attrLst attrs, String query) throws SQLException
 {  int conn = getConn();
  debug("DBCN#>"+conn);//debug
  try
  {
    free[conn]=Boolean.FALSE;
    cons[conn].getRows(v, out, mode, attrs, query);
    free[conn]=Boolean.TRUE;
    cons[conn].isClosed();
  }
  catch(SQLException e)
  {
     free[conn]=Boolean.TRUE;
     if (cons[conn].isClosed())
     {
       cons[conn]=getNewConnDbRdr(_dbmsType, _dbConnStr, _dbUsrStr, _dbPswdStr);
       debug("REOPEN#>"+conn+":"+e.toString());//debug
     }
     else
     {
       cons[conn].getDbConnection().close();
       debug("CLOSE#>"+conn);
       cons[conn]=getNewConnDbRdr(_dbmsType, _dbConnStr, _dbUsrStr, _dbPswdStr);
       debug("REOPEN#>"+conn+":"+e.toString()+" (noclose)");//debug
     }
     curConn=conn;
     throw e;
  }finally{
	  cons[conn].isClosed();
  }
 }

 public ResultSet getRecordset(String qrStr) throws SQLException
 {return getRecordset(qrStr, false);}
 
 public ResultSet getCacheset(String qrStr) throws SQLException
 {return getCacheset(qrStr, false);}

 public ResultSet getRecordset(String qrStr, boolean limitBool) throws SQLException
 {
    ResultSet rset = null;
    int conn = getConn();
    CachedRowSet cacheRowSet = new CachedRowSetImpl();
    try
    {
      free[conn]=Boolean.FALSE;
      rset=cons[conn].getRecordset(qrStr, limitBool);
      cacheRowSet.populate(rset);
      free[conn]=Boolean.TRUE;
    }
    catch(SQLException e)
    {
       free[conn]=Boolean.TRUE;
       if (cons[conn].isClosed())
       {
         cons[conn]=getNewConnDbRdr(_dbmsType, _dbConnStr, _dbUsrStr, _dbPswdStr);
       }
       curConn=conn;
       
       throw e;
    }finally {
		if (rset != null)
			rset.close();
		 cons[conn].isClosed();
	}
	return cacheRowSet;
   }

public CachedRowSet getCacheset(String qrStr, boolean limitBool) throws SQLException {
	int conn = getConn();
    CachedRowSet crs = new CachedRowSetImpl();
    try {
    	free[conn]=Boolean.FALSE;
    	crs=cons[conn].getCacheset(qrStr, limitBool);
    	free[conn]=Boolean.TRUE;
    }
    catch(SQLException e) {
    	free[conn]=Boolean.TRUE;
    	if (cons[conn].isClosed()) {
    		cons[conn]=getNewConnDbRdr(_dbmsType, _dbConnStr, _dbUsrStr, _dbPswdStr);
    	}
    	curConn=conn;
       
    	throw e;
    } finally {
		 cons[conn].isClosed();
	}
	return crs;
}

 public String applyTO_CHAR(String f, boolean mode, String format)
 {
  //almost one connection should have been defined
  return ((oneConnDbRdr)cons[0]).applyTO_CHAR(f, mode, format);
 }


 public String applyTO_CHAR(String f, boolean mode)
 {
  //almost one connection should have been defined
  return ((oneConnDbRdr)cons[0]).applyTO_CHAR(f, mode);
 }

 public String applyTO_DATE(String f, boolean lowBndBool)
 {
  return ((oneConnDbRdr)cons[0]).applyTO_DATE(f, lowBndBool);
 }

 public String getQuery(String tA, String fNames, String whrStr, String orderCls, Vector tbls, boolean distinctBool) throws Exception
 {return getQuery(tA, null, fNames, whrStr, orderCls, tbls, distinctBool);}

 public String getQuery(String tA, String ftA, String fNames, String whrStr, String orderCls, Vector tbls, boolean distinctBool) throws Exception
 {
   if (binder!=null) return binder.getQuery(tA, ftA, fNames, whrStr, orderCls, tbls, distinctBool);
   else return getSlctStr(tA, fNames, whrStr, orderCls);
 }

 public static String getSlctStr(String table, String fieldLst, String whrClause, String orderCls)
 {
  String qrStr  = "SELECT DISTINCT ";
         qrStr  = qrStr.concat(fieldLst);
         qrStr  = qrStr.concat(" FROM ");
         qrStr  = qrStr.concat(table);
         qrStr  = qrStr.concat(" " + whrClause);
         qrStr  = qrStr.concat(" " + orderCls);
  return(qrStr);
 }

/**
 * Returns a Plant configuration file's property (addressed by tag-name).
 */
 public String getCfgProp(String name)
 {
   String s;
   try{s=xmlRdr.getVal(plantRootEl,name);}
   catch(Exception e){return null;}
   return s;
 }

 public void restoreAllConns() throws IOException
 {

 }

 public String toString()
 {
   String s=new String("------ DATABASE READER PROPS ------\n");
   s=s.concat(getCfgProp("MODEL_ID")+"\n");
   s=s.concat(getCfgProp("PL_NAME") +"\n");
   s=s.concat(getCfgProp("DBS_TYPE")+"\n");
   s=s.concat(getCfgProp("CONN_STR")+"\n");
   s=s.concat(getCfgProp("USERNAME")+"\n");
   s=s.concat(getCfgProp("PASSWORD")+"\n");
   s=s.concat("-----------------------------------\n");
   return s;
 }
}
