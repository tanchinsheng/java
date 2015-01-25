package tol;

import java.io.IOException;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import tol.ws.*;

/**
 * Manages communication between frontend and backend dispatching the incoming request to
 * a free backend listener and managing eventual exception at a high functional level.<p>
 * Communication with the backend listener is realised through the {@link tolSocket} class.
 * The message to be sent mya consist of many batch of lines. The simplest case is having
 * one batch of lines only and then {@link #batchExtent} = 1 and {@link #inputFilesVect}
 * having one item only (a single vector of lines) providing all the lines making up the
 * whole message.
 */
public class socketMonitor implements Runnable
{
/**
 * Holds the communication status (in progress | fulfilled) and any eventual exception
 * raised.
 */
 socketStatus ss;
/**
 * Array of communication channels with backend listeners.<p>
 * Each channel is dedicated to a single backend listener.
 */
 tolSocket[] tolsckArr;
/**
 * Number of communication channels available.<p>
 * It basically provides the {@link #tolsckArr} cardinality
 */
 int procsN;
/**
 * Number of batch of lines that the message to send consists of
 */
 int batchExtent;
/**
 * @deprecated not managed any more
 */
 boolean _serializeBatchExtent;
/**
 * Message lines to send.<p> Each array item is a batch of message lines;
 * each batch is a Vector providing message lines as String items.
 */
 Vector inputFilesVect[];
/**
 * List of the backend listeners involved in the communication (those allocated by the dispatcher).<p>
 * As only serial approach is currently implemented we have this vector having one item only, as
 * a single backend listener is involved in serving the incoming request.
 *
 */
 Vector procsVect;
/**
 * Backend listeners dispatcher.
 */
 SimpleDispatcher disp;
/**
 * Debug flag. When set make no actual send/rceive operation be performed: only debug wait
 * cycle and debug prints will be done.
 */
 boolean debug;
/**
 * References the directory where eventual "failure" control file should be created.
 */
 String sysLogDir;
/**
 * Is responsible for recording debug information into a proper log file
 */
 LogWriter log = null;
 /**
  * Web Service client in case that after the backend listener succesfull service
  * also a web service needs to be called
  */
 WsClient ws;

 /**
  * Sets the Web Service client in case after the backend listener succesfull service
  * also a web service needs to be called
  * @param ws
  */
 public void setOlapWsClient(WsClient ws) {this.ws=ws;}
 /**
  * Sets the log writer
  * @param log - log writer to be used
  */
 public void setLogWriter(LogWriter log) {this.log=log;}
 /**
  * Writes debug information into the log file through {@see #log}
  * @param msg
  */
 public void debug(Object msg) {if (log!=null) log.p(msg);}
 /**
  * Writes debug information into the log file through {@see #log}
  * @param msg
  */
 public void debug(String msg) {if (log!=null) log.p(msg);}

 static final String _sendErrMsg = "CAN'T CONNECT TO THE BACKEND PROCESS";
 static final String _recvErrMsg = "SAS ACKNOWLEDGEMENT TIMEOUT EXPIRED";
 static final String _busyErrMsg = "BACKEND PROCESS BUSY AT THE MOMENT";
 static final String _failFnamPrefix = "CONNECTION-REFUSED";

 static final boolean _queueEnabled = false;

 public socketMonitor() {};

/**
 * @deprecated _serializeBatchExtent is no more managed
 * @param ss socket status meant to hold the send/receive operation status
 * @param disp backend listeners dispatcher
 * @param tolsckArr array of communication channels with backend listeners
 * @param procsN number of communication channels
 * @param batchExtent number of batches of lines that the message consists of
 * @param _serializeBatchExtent
 * @param inputFilesVect message lines
 * @param debug debug flag
 * @param logDir directory where eventual "failure" control should be created
 */
 public socketMonitor( socketStatus ss,
                       SimpleDispatcher disp,
                       tolSocket[] tolsckArr,
                       int procsN,
                       int batchExtent,
                       boolean _serializeBatchExtent,
                       Vector inputFilesVect[],
                       boolean debug,
                       String logDir
                     )
  {
   this.ss = ss;
   this.disp = disp;
   this.tolsckArr = tolsckArr;
   this.procsN = procsN;
   this.batchExtent = batchExtent;
   this._serializeBatchExtent = _serializeBatchExtent;
   this.inputFilesVect = inputFilesVect;
   this.debug = debug;
   this.sysLogDir = logDir;
   this.ws=null;
  }

/**
 * @param ss socket status meant to hold the send/receive operation status
 * @param disp backend listeners dispatcher
 * @param tolsckArr array of communication channels with backend listeners
 * @param procsN number of communication channels
 * @param batchExtent number of batches of lines that the message consists of
 * @param inputFilesVect message lines
 * @param debug debug flag
 * @param logDir directory where eventual "failure" control should be created
 */
 public socketMonitor( socketStatus ss,
                       SimpleDispatcher disp,
                       tolSocket[] tolsckArr,
                       int procsN,
                       int batchExtent,
                       Vector inputFilesVect[],
                       boolean debug,
                       String logDir
                     )
  {
   this.ss = ss;
   this.disp = disp;
   this.tolsckArr = tolsckArr;
   this.procsN = procsN;
   this.batchExtent = batchExtent;
   this._serializeBatchExtent = true;
   this.inputFilesVect = inputFilesVect;
   this.debug = debug;
   this.sysLogDir = logDir;
   this.ws=null;
  }

/**
 * Picks up a free backend listener, and specifically its dedicated communication channel,
 * if any is available and then performs a send/receive communication cycle.<p>
 * If this communication cycle fails for a I/O problem, such as backend
 * listener temporarily unavailability, the operation is retried again but with a
 * different backend listener. If even this further attempt fails for I/O reasons this
 * method returns recording a proper exception into the socket status {@see #ss}.
 * If no available backend listener can serve the incoming request this method returns
 * setting a proper exception into the socket status.
 */
 public void run()
 {
   int tryCnt=0;
   boolean ok_or_criticErrBool;
   do
   {
     if (_queueEnabled)
     {
       try
       {
        //***** PUSH BLOCK *****
         while ((procsVect=disp.push(1))==null)
         {
           Thread.currentThread().sleep(1000);
         }
       }
       catch(InterruptedException e)
       {
         handlErr(e,"");
         ss.setStartBool(false);
         return;
       }
     }
     else
     {
       //***** PUSH BLOCK *****
       if ((procsVect=disp.push(1))==null)
       {
         handlErr(new IOException(_busyErrMsg),_busyErrMsg);
         ss.setStartBool(false);
         return;
       }
     }
     int proc = ((Integer)procsVect.elementAt(0)).intValue();
     //***** SEND/RECV BLOCK *****
     ok_or_criticErrBool=doSendRecvBlock(proc);
     //***** POP BLOCK *****
     disp.pop(procsVect);
     tryCnt++;
   }
   while((tryCnt<=1)&&(!ok_or_criticErrBool));

   /*** OLAP DEMO ***
   if (this.olapSckArr!=null)
   {
       try
       {
           doOlapSendRecvBlock();
       }
       catch(Exception e) {debug("OLAP SCK COMMUNICATION FAILURE>"+e);}
   }
   */
   if (this.ws!=null)
   {
        ws.call();
   }
   ss.setStartBool(false);
 }

/**
 * Performs a high level send/receive cycle against that backend listener picked up
 * to serve the incoming request.<p>
 * @param proc identified the backend listener to serve the incoming request.
 * It is specifically an index referencing a single communication channel within the
 * {@link #tolsckArr} array.
 * @return true iff the send/eceive cycle either fulfills or end with an
 * application related error.<p>
 * In case of a I/O error, such as a backend listener acknowlegment failure, "false"
 * is returned to notify the calling function that the send/receive cycle needs to be
 * done again possibly through another communication channel currently available.
 *
 * Note: if a web service client is set this is employed to call its target web service
 * after a succesfull backend service invokation
 */
 public boolean doSendRecvBlock(int proc)
 {
   String sasErrCode;
   ss.resetErrBool();
   //***** SEND BLOCK (serialize batches on one SAS proc only) *****
   for (int i=0; i<batchExtent; i++)
   {
       try
       {
         if (inputFilesVect[i].size()>=1)
         {
           debug("PROC"+proc+">");
           tolsckArr[proc].cSend(inputFilesVect[i],debug);
         }
       }
       catch(IOException e)
       {
         handlErr(new IOException(_sendErrMsg),_sendErrMsg);
         disp.markFirstFailedProc(proc);
         createFailureFile(tolsckArr[proc]);
         return false;
       }
       //***** RECEIVE BLOCK *****
       try
       {
         if (inputFilesVect[i].size()>=1)
         {
           String retCode = tolsckArr[proc].receive(debug);
           if (!retCode.equals(tolServlet._sasOkCode))
           {
             ss.setErr(null, retCode, null);
             if (retCode.equals(tolServlet._sasAbortCode))
             {
               handleAbort();
               return true;
             }
           }
         }
       }
       catch(Exception e)
       {
         handlErr(new IOException(_recvErrMsg),_recvErrMsg);
         disp.markFirstFailedProc(proc);
         createFailureFile(tolsckArr[proc]);
         return true;
       }
   }//for
   return true;
 }

/*** OLAP DEMO ***
public String doOlapSendRecvBlock() throws Exception
{
    //***** SEND BLOCK *****
      try
      {
        if (inputFilesVect[0].size()>=1)
        {
          debug("OLAP PROC"+"0"+">");
          this.olapSckArr[0].cSend(inputFilesVect[0],false);
        }
      }
      catch(IOException e)
      {
         throw new IOException("OLAP SCK SEND ERROR>"+e.getMessage());
      }
      //***** RECEIVE BLOCK *****
      try
      {
        if (inputFilesVect[0].size()>=1)
        {
          String olapRepURL = this.olapSckArr[0].receive(false);
          return olapRepURL;
        }
      }
      catch(Exception e)
      {
         throw new IOException("OLAP SCK RECEIVE ERROR>"+e.getMessage());
      }
      return "";
}
*/

/**
 * Writes a backend listener "failure" control file.<P>
 * When communication with a backend listener fails because of I/O reasons that listener is
 * marked as "unavailable" within the Dispatcher and a proper file needs to be created in
 * the {@see #sysLogDir} directory so to instruct the backend daemon to make a recovery.
 *
 * @param sock backend listener communication channel - the failure control file will be
 * named after the listener base port and offset.
 */
void createFailureFile(tolSocket sock)
{
  try
  {
    String fName = _failFnamPrefix+"-"+sock.getPortAndOffsetStr();
    fName = this.sysLogDir+fName;
    debug("SCK-FAILURE-FILE>"+fName);
    File f = new File(fName);
    f.createNewFile();
  }
  catch(Exception e) {debug("ERROR>"+e.toString());}
}


/**
 * Handles I/O exceptions occurred during the send/receive cycle
 */
 protected void handlErr(Exception e, String msg)
 {
   ss.setErr(e, null, msg);
   handleAbort();
 }

/**
 * Handles high level exceptions such as a failure code returned by the backend application
 */
 protected void handleAbort() {}
 }



