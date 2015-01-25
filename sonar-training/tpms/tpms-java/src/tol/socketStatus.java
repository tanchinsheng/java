package tol;

/**
 * Holds the current status of a communication process with a backend listener.
 */
public class socketStatus
{
  private String   sessID;
  public boolean   startBool;
  public boolean   errBool;
  public String    errStr;
  public String    errCode;
  public Exception errObj;
  public String    outputName;
  LogWriter log = null;

  public void setLogWriter(LogWriter log) {this.log=log;}
  public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

  public socketStatus(String sessID)
  {
    this.sessID=sessID;
    init();
  }

  public synchronized void init()
  {
    startBool=false;
    errBool=false;
    errCode=tolServlet._sasOkCode;
    errStr=new String();
    errObj=new Exception();
  }

  public String getID() {return sessID;}

  public boolean getStartBool() {return startBool;}

  public boolean getErrBool() {return errBool;}

  public void resetErrBool() {errBool=false;}

  public Exception getErrObj() {return errObj;}

  public String getErrCode() {return errCode;}

  public String getErrStr() {return errStr;}

  public void setOutputName(String s) {outputName=s;}

  public String getOutptName() {return outputName;}

  public synchronized void setStartBool(boolean b) {startBool=b;}

  public synchronized void setErr(Exception e, String code, String str)
  {
    if (e!=null) {errBool=true; errObj=e;}
    if (code!=null) errCode=code;
    if (str!=null) errStr=str;
  }
}

