package tol;

import java.sql.*;
import java.util.*;


public class oneConnDbWrtr extends oneConnDbRdr
{
 static int _lockErrCode=54;
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public oneConnDbWrtr(String xmlInitFile) throws SQLException
 {
   super(xmlInitFile);
   conn.setAutoCommit(false);
 }

 public oneConnDbWrtr(String _host, String _lstnrPort, String _sid, String _dbUsrStr, String _dbPswdStr) throws SQLException
 {
   super(_host, _lstnrPort, _sid, _dbUsrStr, _dbPswdStr);
   conn.setAutoCommit(false);
 }

 public oneConnDbWrtr(String _dbConnStr, String _dbUsrStr, String _dbPswdStr) throws SQLException
 {
   super(_dbConnStr, _dbUsrStr, _dbPswdStr);
   conn.setAutoCommit(false);
 }

 public void selForUpdate(String qryStr, boolean waitBool) throws SQLException
 {
  try
  {
    Statement stmt= conn.createStatement();
    if (!waitBool) qryStr=qryStr.concat(" NOWAIT");
    stmt.executeQuery(qryStr);
    stmt.getResultSet().close();
    stmt.close();
  }
  catch(SQLException e)
  {
    if (e.getErrorCode()==_lockErrCode) throw new SQLExceptionLock(e.getMessage());
    else throw e;
  }
 }

 public void submit(String sqlStr) throws SQLException
 {
  try
  {
    Statement stmt= conn.createStatement();
    stmt.executeUpdate(sqlStr);
    stmt.close();
  }
  catch(SQLException e)
  {
    if (e.getErrorCode()==_lockErrCode) throw new SQLExceptionLock(e.getMessage());
    else throw e;
  }
 }

 public void commit() throws SQLException
 {
   conn.commit();
 }

 public void rollback() throws SQLException
 {
   conn.rollback();
 }

 public static void main(String args[]) throws Exception
 {
   oneConnDbWrtr dbwrt=new oneConnDbWrtr("172.16.17.74","1521","ORCL","TPMS_PROD","TPMS");
   //debug(">DBCONN OPENED");
   //dbwrt.selForUpdate("SELECT FILENAME FROM SSDB_WAFER WHERE LOT_ID='A101189' AND WAFER_ID='20' FOR UPDATE", false);
   //debug(">ROWS LOCKED");
   //Thread.currentThread().sleep(15000);
   //dbwrt.submit("UPDATE SSDB_WAFER SET FILENAME='DANIELE' WHERE LOT_ID='A101189' AND WAFER_ID='20'");
   //debug(">ROWS UPDATED");
   Vector v=new Vector();
   dbwrt.getRows(v, "SELECT MAX(DBID) FROM TP_PLANT");
   System.out.println(v);
   //dbwrt.commit();
   //debug(">COMMIT");
   dbwrt.close();
   //debug(">DBCONN CLOSED");
 }
}