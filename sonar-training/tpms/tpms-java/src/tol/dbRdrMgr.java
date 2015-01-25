package tol;

import java.io.*;
import java.util.Vector;

public class dbRdrMgr
{
 static String _localCfgDir="local_cfg";
 static String _dbInitDir="db_int";
 Vector dbRdrs;
 Vector dbModelDirs; //used to check if a db conn already exists
 Vector dbPlantNames;    //used to check if a db conn already exists
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public dbRdrMgr()
 {
   dbRdrs=new Vector();
   dbModelDirs=new Vector();
   dbPlantNames=new Vector();
 }

 public dbRdr getDbRdr(String initDir, String plantName, String modelID, String nConnStr) throws Exception
 {
   return getDbRdr(initDir.concat("/"+_localCfgDir), plantName, initDir.concat("/"+_dbInitDir), modelID, nConnStr);
 }

 public dbRdr getDbRdr(String plantInitDir, String plantName, String dbModelDir, String modelID, String nConnStr) throws Exception
 {
          dbRdr dbr=getDbRdr(dbModelDir, plantName);
          if (dbr!=null) return dbr;

          //--- DATABASE READER INITIALIZATION ---//
          try {
                  if (!(new File(dbModelDir).isDirectory()))
                  {
                    String msg=new String("DBCONN INIT ERROR FOR DIR '"+dbModelDir+"': ACCESS FAILED.");
                    throw new IOException(msg);
                  }
                  debug("DBCONN INIT FOR DIR '"+dbModelDir+"'");
                  dbr=new dbRdr(plantInitDir, plantName, dbModelDir, modelID, Integer.valueOf(nConnStr).intValue());
                  dbr.setLogWriter(log);
              }
          catch(Exception e)
              {
                  debug("DBCONN INIT ERROR FOR DIR '"+dbModelDir+"'"+e);
                  throw e;
              }
          dbRdrs.addElement(dbr);
          dbModelDirs.addElement(dbModelDir);
          dbPlantNames.addElement(plantName);
          return dbr;
}

 private dbRdr getDbRdr(String dbModelDir, String plantName)
 {
  for (int i=0; i<dbModelDirs.size(); i++)
  {
    if ((dbModelDir.equals((String)dbModelDirs.elementAt(i)))&&(plantName.equals((String)dbPlantNames.elementAt(i))))
    return (dbRdr)dbRdrs.elementAt(i);
  }
  return null;
 }



 public void close() throws Exception
 {
  for (int i=0; i<dbRdrs.size(); i++)
  {
    String dbModelDir=(String)dbModelDirs.elementAt(i);
    try
    {
      ((dbRdr)dbRdrs.elementAt(i)).close();
      debug("DBCONN CLOSE FOR DIR '"+dbModelDir+"'.");
    }
    catch(Exception e)
    {
       debug("DBCONN CLOSE ERROR FOR DIR '"+dbModelDir+"'");
       throw e;
    }
  }
 }

/*
 public static void main(String[] args) throws IOException
 {
    dbRdrMgr dbmgr = new dbRdrMgr();
    debug(dbmgr.getDbRdr("tol-txt"));
    debug(dbmgr.getDbRdr("tol-txt"));
    debug(dbmgr.getDbRdr("tol-txt"));
    //debug(dbmgr.getDbRdr("ews"));
    dbmgr.close();
 }
*/
}