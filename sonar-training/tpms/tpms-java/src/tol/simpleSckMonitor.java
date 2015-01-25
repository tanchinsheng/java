package tol;


import java.io.IOException;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Created by IntelliJ IDEA.
 * User: colecchia
 * Date: Sep 22, 2003
 * Time: 12:45:31 PM
 * To change this template use Options | File Templates.
 */

public class simpleSckMonitor extends socketMonitor
{

    public simpleSckMonitor(socketStatus ss, tolSocket tolsckArr[], Vector inputFilesVect[])
    {
        this.ss = ss;
        this.tolsckArr = tolsckArr;
        this.inputFilesVect=inputFilesVect;
        this.debug = false;
    }

    public boolean doSendRecvBlock()
    {
        //***** SEND BLOCK *****
          try
          {
            if (inputFilesVect[0].size()>=1)
            {
              debug("PROC"+"0"+">");
              tolsckArr[0].cSend(inputFilesVect[0],debug);
            }
          }
          catch(IOException e)
          {
            handlErr(new IOException(_sendErrMsg),_sendErrMsg);
            return false;
          }
          //***** RECEIVE BLOCK *****
          try
          {
            if (inputFilesVect[0].size()>=1)
            {
              String retCode = tolsckArr[0].receive(debug);
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
            return true;
          }
      return true;
    }


    public boolean doRecSendvBlock(String cmd) throws Exception
    {
        //***** RECEIVE BLOCK *****
        try
        {
          System.out.println("SCK LISTENING...");
          if (inputFilesVect[0]!=null)
          {
            inputFilesVect[0].clear();
            tolsckArr[0].receive(inputFilesVect[0], debug);
            System.out.println(inputFilesVect[0]);
          }
        }
        catch(Exception e)
        {
          throw e;
          //handlErr(new IOException(_recvErrMsg),_recvErrMsg);
          //return true;
        }

        //***** SENDER IP ADDRESS DETECTION *****//
        StringTokenizer strtok=new StringTokenizer((String)inputFilesVect[0].elementAt(0)," ",false);
        String ipAddr=strtok.nextToken();
        String port=tolSocket.getPortFotmIPAddressString(ipAddr);
        String host=tolSocket.getHostFotmIPAddressString(ipAddr);
        System.out.println("PORT>"+port);
        System.out.println("HOST>"+host);
        tolsckArr[0].setSsckPort(Integer.valueOf(port).intValue());
        tolsckArr[0].setSsckIPAddr(host);

        //***** SYS COMMAND EXECUTION *****//
        try
        {
            Runtime.getRuntime().exec(cmd);
        }
        catch(IOException e)
        {
          handlErr(new IOException("SYS CALL TO '"+cmd+"' FAILED>"),"SYS CALL TO '"+cmd+"'FAILED>");
          return false;
        }

        //***** SEND BLOCK *****
        try
        {
          if (true)
          {
            debug("PROC"+"0"+">");
            tolsckArr[0].cSend("2");
          }
        }
        catch(IOException e)
        {
          handlErr(new IOException(_sendErrMsg),_sendErrMsg);
          return false;
        }
        return true;
    }


   public static void main(String args[]) throws Exception
   {
       String cmd="";
       int nSteps;
       if (args.length >= 1) nSteps=Integer.valueOf(args[0]).intValue();
       else nSteps=1;
       if (args.length >= 2) cmd=args[1];
       else cmd = "D:/Program Files/Microsoft Office/Office/excel.exe";
       tolSocket tolsckArr[] = new tolSocket[1];
       tolsckArr[0] = new tolSocket(9080,"",0,0,0);
       tolsckArr[0].setLogWriter(null);
       socketStatus ss=new socketStatus("");
       ss.init();
       System.out.println("SCK CREATED...");
       Vector v[] = new Vector[1];
       v[0] = new Vector();
       simpleSckMonitor sckm=new simpleSckMonitor(ss, tolsckArr, v);
       tolsckArr[0].initLstSocket(0);
       for (int i=0; i<nSteps; i++)
       {
           sckm.doRecSendvBlock(cmd);
           if (ss.getErrBool()) System.out.println("ERROR>"+ss.getErrObj());
       }
       tolsckArr[0].closeLstSocket();
   }

}
