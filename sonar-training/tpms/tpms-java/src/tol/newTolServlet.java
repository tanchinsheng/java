package tol;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.*;

public class newTolServlet extends tolServlet
{
 static String _delAppCode="EXTR"; //app in charge of deleting images at the end of the static analysis elaboration
 static String _delLineOption="DELETE";   //deletion line option of the previous app
 boolean          _delTmpFilesBool;
 boolean          _skipExtrBool;

 void submit(HttpServletRequest request, HttpServletResponse response) throws Exception
 {
   submit_analisys(request, response);
 }

 void submit_app(HttpServletRequest request, HttpServletResponse response, analisysMgr an, appRdr app, boolean responseBool, boolean staticBool, Vector anInputFilesVect) throws Exception
 {
   Vector inputFiles;                              //keep the stdf list
   boolean cumulatedBool;                          //cumulated
   int batchExtent;                                //number of splits
   HttpSession session = request.getSession(true); //session

   //-- APPLICATION READER MANAGEMENT --//
   String appCode=app.getCode();
   String extrAppCode=app.getExtrAppCode();

   //***** OUTPUT FILE NAMES DEFINITION *****
   String ID = request.getSession().getId();
   String time = new Long(System.currentTimeMillis()).toString();
   time=time.substring(time.length()-6,time.length());
   //--- TIMESTAMP ASSIGNEMENT TO THE APPDESC ELEMENT ---//
   app.setReqID(time);
   if (staticBool) app.setSelCrit(an.getSelCrit());
   else app.setSelCrit(new selCrit((dataSel)session.getAttribute("dsel")));
   //***** CRITERION & RUNLIST DEFINITION *****//
   app.buildCrit(request, imgDirBasePath.concat(imgDir));
   app.buildRunl(request, imgDirBasePath.concat(imgDir));

   //***** SOCKET LINE MANIPULATION *****
   String sckLine=new String(ID+tolSocket._dlmStr+time);
   String optns;
   optns=new Integer(app.getLev()).toString();
   optns=app.formatOptns(optns, 1, this._execOptionNumber);
   Vector inputFilesVect[]  = new Vector[(!this._skipExtrBool ? 2 : 1)];
          inputFilesVect[0] = new Vector();
          if (!this._skipExtrBool)
          {
            inputFilesVect[0].addElement(sckLine+tolSocket._dlmStr+extrAppCode+tolSocket._dlmStr+optns);
            inputFilesVect[1] = new Vector();
            inputFilesVect[1].addElement(sckLine+tolSocket._dlmStr+appCode+tolSocket._dlmStr+optns);
          }
          else
          {
            inputFilesVect[0].addElement(sckLine+tolSocket._dlmStr+appCode+tolSocket._dlmStr+optns);
          }
   if (anInputFilesVect!=null)
   {
     if (!this._skipExtrBool)
     {
       anInputFilesVect.addElement(sckLine+tolSocket._dlmStr+extrAppCode+tolSocket._dlmStr+optns);
     }
     anInputFilesVect.addElement(sckLine+tolSocket._dlmStr+appCode+tolSocket._dlmStr+optns);
   }
   int nGifs=1;
   if (!responseBool) return;
   else
   {
     socketStatus ss=(socketStatus)session.getAttribute("sckStatus");
     ss.init();
     ss.setStartBool(true);

     //***** SAS INTERACTION *****
     //SOCKET MANAGEMENT
     //when the Socket Monitor has complited its execution, it sets the
     //'startBool' socket status attribute to 'false'.
     socketMonitor sckMntr = new socketMonitor
     (
       ss,
       disp,
       tolsckArr,
       procsN,
       2,              //batchExtent
       //true,           //serialize batch extent
       inputFilesVect,
       _DEBUG,
       this.imgDirBasePath.concat(this.sysLogDir)
     );
     sckMntr.setLogWriter(this.log);
     Thread sckThread = new Thread(sckMntr);
     sckThread.start();
     //END OF SAS INTERACTION

     //***** OUTPUT MANAGEMENT BLOCK *****//
     {
       RequestDispatcher rDsp;
       rDsp = getServletContext().getRequestDispatcher(request.getParameterValues("nextPage")[0]);
       request.setAttribute("reqID",session.getId());
       request.setAttribute("title",app.getTitle());
       request.setAttribute("outPage",request.getParameterValues("outPage")[0]);
       rDsp.forward(request, response);
       debug("FWD>");
     }
   }
 }


 void submit_analisys(HttpServletRequest request, HttpServletResponse response) throws Exception
 {
   HttpSession session = request.getSession(true);
   analisysMgr a=(analisysMgr)session.getAttribute("analisys");
   a.setSelCrit(new selCrit((dataSel)session.getAttribute("dsel")));

   //--- OLD ANALYSIS TEMPORARY FILES DELETION - IF (!_DEBUG) ---//
   if ((!_DEBUG)&&(_delTmpFilesBool)) dclnr.delFiles(a.getOldTempFileNames());

   //--- SUBMIT APPLICATION CALLS ---//
   Vector v=a.getAppRdrs();
   Vector anInputFilesVect=new Vector();
   Vector appTempFileNames=new Vector();
   Vector SidTempFileNames=new Vector();
   for (int i=0; i<v.size(); i++)
   {
     appRdr app=(appRdr)v.elementAt(i);
     submit_app(request, response, a, app, false, true, anInputFilesVect);
     app.getAppTempFileNames(request, appTempFileNames);
     a.setOldTempFileNames(appTempFileNames);
     app.getSidTempFileNames(request, SidTempFileNames);
   }

   //--- SESSION TEMPORARY FILES DELETION - IF (!DEBUG) ---//
   if ((!_DEBUG)&&(_delTmpFilesBool)) dclnr.delFiles(SidTempFileNames);

   //--- TEMPORARY TABLES DELETION INVOKATION ---//
   if (_delTmpFilesBool)
   {
     anInputFilesVect.addElement(this.getDelSckLine(session.getId()));
   }

   //--- THE LIST OF SOCKET LINES MUST BE ARRANGED INTO AN ARRAY ---//
   Vector[] sckMntrInputVect=new Vector[anInputFilesVect.size()];
   for (int i=0; i<anInputFilesVect.size(); i++)
   {
     sckMntrInputVect[i]=new Vector();
     sckMntrInputVect[i].addElement(anInputFilesVect.elementAt(i));
   }

   //--- BACKEND PROCESS CALL ---//
   socketStatus ss=(socketStatus)session.getAttribute("sckStatus");
   ss.init();
   ss.setStartBool(true);

   //***** SAS INTERACTION *****
   //SOCKET MANAGEMENT
   //when the Socket Monitor has complited its execution, it sets the
   //'startBool' socket status attribute to false
   socketMonitor sckMntr = new socketMonitor
   (
     ss,
     disp,
     tolsckArr,
     1,                                          //processes number (useless)
     anInputFilesVect.size(),                    //batchExtent
     //true,                                       //serialize batch extent
     sckMntrInputVect,                           //socket lines list
     _DEBUG,
     this.imgDirBasePath.concat(this.sysLogDir)
   );
   sckMntr.setLogWriter(this.log);
   Thread sckThread = new Thread(sckMntr);
   sckThread.start();

   //***** OUTPUT MANAGEMENT BLOCK *****//
   {
     RequestDispatcher rDsp;
     rDsp = getServletContext().getRequestDispatcher(request.getParameterValues("nextPage")[0]);
     request.setAttribute("reqID",session.getId());
     request.setAttribute("title",a.getTitle());
     request.setAttribute("outPage",request.getParameterValues("outPage")[0]);
     rDsp.forward(request, response);
     debug("FWD>");
   }
 }

 public String getDelSckLine(String sID)
 {
   String optns=new String("0"+tolSocket._dlmStr+_delLineOption);
   optns=appRdr.normalizeOptnsLine(optns, 2, this._execOptionNumber);
   return new String(sID+tolSocket._dlmStr+"0"+tolSocket._dlmStr+_delAppCode+tolSocket._dlmStr+optns);
 }

}
