package tol;

import java.io.*;
import java.util.*;
import java.sql.*;
import org.w3c.dom.*;

public class oneConnIngresDbRdr extends oneConnDbRdr
{
 static final String _ingresDrvConnStr="jdbc:caribou:jsqlingres//";
 String driverClassName="COM.cariboulake.sql.JSQLDriver";
                        //--- COM.cariboulake.sql.JSQLDriver  (CCAM driver version) DEFAULT ---//
                        //--- com.cariboulake.jsql.JSQLDriver (4.0.70) ---//

 public oneConnIngresDbRdr(String xmlInitFile) throws SQLException
 {
   super(xmlInitFile);
   initConn(_dbConnStr, _dbUsrStr, _dbPswdStr);
 }

 public oneConnIngresDbRdr(String _dbConnStr, String _dbUsrStr, String _dbPswdStr) throws SQLException
 {
   initConn(_dbConnStr, _dbUsrStr, _dbPswdStr);
 }

 public void initConn(String _dbConnStr, String _dbUsrStr, String _dbPswdStr) throws SQLException
 {
  this._drvConnStr= _ingresDrvConnStr;
  this._dbConnStr = _dbConnStr; //(ie: "jsvr_host:jsvr_port/dbname")
  this._dbUsrStr  = _dbUsrStr;
  this._dbPswdStr = _dbPswdStr;
  int splitChar=_dbConnStr.indexOf("//");
  if (splitChar>-1)
  {
    driverClassName=_dbConnStr.substring(0,splitChar);
    _dbConnStr=_dbConnStr.substring(splitChar+2);
  }
  String connStr=new String(_drvConnStr+_dbConnStr);
  debug("driver-class>"+driverClassName+"\n"+"conn-str>"+connStr); //debug
  Properties props = new Properties();
  props.put("user",     _dbUsrStr);
  props.put("password", _dbPswdStr);
  try {
        Class.forName(driverClassName).newInstance();
        conn = DriverManager.getConnection(connStr, props);
      }
  catch(Exception e) {throw new SQLException(e.getMessage());}
 }

  //------------------------//
 //--- FORMATTING FUNCT ---//
 //------------------------//
 public String applyTO_CHAR(String f, boolean mode)
 {
   return applyTO_CHAR(f, mode, null);
 }

 public String applyTO_CHAR(String f, boolean mode, String format)
 {
   return(f);
 }


 public String applyTO_DATE(String f, boolean lowBndBool)
 //lowBndBool=true means that I request a lower bound
 //(otherwise i've requested an upper bound
 {
  String lowBndStr="0:00:00";
  String upBndStr ="23:59:59";
  String timeStr  =(lowBndBool ? lowBndStr : upBndStr);
  String s = new String("'");
  StringTokenizer st=new StringTokenizer(f,dateRd._separators,false);
  s = s.concat(st.nextToken()+"-");
  s = s.concat(dateRd.getMonthLabel(st.nextToken())+"-");
  s = s.concat(st.nextToken()+" ");
  s = s.concat(timeStr+"'");
  return(s);
 }

 //Invoke it this way in Agrate TPA:
 //java oneConnIngresDbRdr "164.130.21.108:9100/idewstpa::idea_ews" "dpgag000" "" "qry.txt"
 //Shortcut: invoke 'javaTest.bat'
 static public void main(String args[]) throws Exception
 {
   oneConnIngresDbRdr dbr = new oneConnIngresDbRdr("com.cariboulake.jsql.JSQLDriver//164.130.21.108:9100/idewstpa::idea_ews","ingres","xxx");
   //dbr.submitFile(args[3],new Vector());
   //debug(oneConnIngresDbRdr.sApplyTO_DATE("01/10/1987",false));
 }
};
