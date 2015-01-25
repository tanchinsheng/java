package tol;

import tol.util.WebLogger;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.*;
import tol.ws.*;


public class tolServlet extends HttpServlet
{
  public static final  String _sasOkCode    = "0";
  public static final  String _sasAbortCode = "2";

  public static final    String _caughtErrPage       = "/caughtErr.jsp";
  protected static final String _graphicFileExt      = "GIF";
  protected static final String _miniaturizedFileExt = "S";
  protected static final String _sasAbortMsg         = "_ABORT_";
  protected static final int    _execOptionNumber    = 8; //number of options passed to SAS process (options other than the
                                                          //application code)
  boolean          _DEBUG;
  //boolean          _delTmpFilesBool;
  //boolean          _skipExtrBool;
  int              _batchExtent;
  //boolean          _serializeBatchExtent;
  int              procsN;
  int              servletPort;
  int              soTimeout;       //is expressed in seconds
  String           sasIP;
  int              sasPort;
  String           critsDir;
  String           imgDir;
  String           usrLogDir;
  String           sysLogDir;
  String           imgDirBasePath;
  String           initDir;
  int              GIF_delDelay;    //is expressed in minutes
  long             lastRunTstmp=0;  //is expressed in [ms]
  int              _sleep;          //is expressed in seconds
  tolSocket[]      tolsckArr;
  SimpleDispatcher disp;
  dirCleaner       dclnr;
  int              dirCleanCont=0;
  LogWriter        log = null;

  public void setLogWriter(LogWriter log) {this.log=log;}
  public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}


public void init() throws ServletException
{
  String str;
  try
  {
    this.log       = (LogWriter)this.getServletContext().getAttribute("log");
    str            = getServletConfig().getInitParameter("procsN");
    procsN         = (Integer.valueOf(str)).intValue();
    str            = getServletConfig().getInitParameter("servletPort");
    servletPort    = (Integer.valueOf(str)).intValue();
    str            = getServletConfig().getInitParameter("soTimeout");
    soTimeout      = (Integer.valueOf(str)).intValue();
    sasIP          = getServletConfig().getInitParameter("sasIP");
    str            = getServletConfig().getInitParameter("sasPort");
    sasPort        = (Integer.valueOf(str)).intValue();
    critsDir       = getServletContext().getInitParameter("critsDir");
    imgDir         = getServletContext().getInitParameter("imgDir");
    sysLogDir      = getServletContext().getInitParameter("sysLogDir");
    usrLogDir      = getServletContext().getInitParameter("usrLogDir");
    imgDirBasePath = getServletContext().getInitParameter("imgDirBasePath");
    initDir        = getServletContext().getInitParameter("initDir");
    str            = getServletContext().getInitParameter("GIF_delDelay");
    GIF_delDelay   = (Integer.valueOf(str)).intValue();
    _DEBUG         = getServletContext().getInitParameter("DEBUG").toUpperCase().equals("TRUE");
    //_delTmpFilesBool = getServletContext().getInitParameter("delTmpFiles").toUpperCase().equals("TRUE");
    //_skipExtrBool  = getServletContext().getInitParameter("skipDataExtraction").toUpperCase().equals("TRUE");
    //str            = getServletContext().getInitParameter("SLEEP");
    //_sleep         = (Integer.valueOf(str)).intValue();
    str            = getServletConfig().getInitParameter("batchProcessExtent");
    _batchExtent   = (Integer.valueOf(str)).intValue();
    //str            = getServletContext().getInitParameter("serializeBatchExtent");
    //if (str!=null) _serializeBatchExtent = str.toUpperCase().equals("TRUE");
    //else           _serializeBatchExtent = false;

    //debug("delTmpFiles>"+ _delTmpFilesBool);
    //debug("skipDataExtraction>"+ _skipExtrBool);
    debug("procsN>"+ procsN);
    debug("batchExt>"+ _batchExtent);
    //debug("serializeBatchExt>"+ _serializeBatchExtent);
    debug("javaPort>"+ servletPort);
    debug("soTimeout>"+ soTimeout);
    debug("sasIP>"+ sasIP);
    debug("sasPort>"+ sasPort);
    debug("critsDir>"+ critsDir);
    debug("imgDir>"+ imgDir);
    debug("usrLogDir>"+ usrLogDir);
    debug("sysLogDir>"+ sysLogDir);
    debug("initDir>"+ initDir);
    debug("basePath>"+ imgDirBasePath + imgDir);
    debug("gifExpiration>"+ GIF_delDelay);

    tolsckArr = new tolSocket[procsN];
    for (int i=0; i<procsN; i++)
    {
      tolsckArr[i] = new tolSocket(servletPort+i,sasIP,sasPort,i,sasPort+i);
      tolsckArr[i].setLogWriter(this.log);
      tolsckArr[i].initLstSocket(soTimeout);
    }

    disp  = new SimpleDispatcher(procsN);
    disp.setLogWriter(this.log);
    dclnr = new dirCleaner(imgDirBasePath.concat(imgDir),GIF_delDelay);
  }
  catch(Exception e) {debug("ERROR>SERVLET INITIALIZATION: "+e);}
}

public void destroy()
{
   for (int i=0; i<procsN; i++)
   {
      try {tolsckArr[i].closeLstSocket();}
      catch (Exception e)
      {debug("ERROR>SERVLET SCK DEALLOCATION: " + e);}
   }
}


 public void doPost (HttpServletRequest request,
 HttpServletResponse response) throws ServletException, IOException
 {
   HttpSession session=request.getSession();
   socketStatus ss=((socketStatus)session.getAttribute("sckStatus"));
   if (ss==null)
   {
     ss=new socketStatus(session.getId());
     session.setAttribute("sckStatus",ss);
   }
   debug("POST>"); //debug
   boolean startBool=ss.getStartBool();
   //-- CHECK IF THERE'S JUST ONE APPLICATION RUNNING FOR THIS SESSION --//

   if (startBool)
   {
     String msg="YOU CAN'T RUN THIS APPLICATION UNTIL YOUR PREVIOUS APPLICATION HAS BEEN COMPLETED";
     request.setAttribute("errType","CONTROL SERVLET ERROR");
     request.setAttribute("errObj",new NumberFormatException(msg));
     RequestDispatcher rDsp = getServletContext().getRequestDispatcher(_caughtErrPage);
     rDsp.forward(request,response);
     if ((!_DEBUG)&&(System.currentTimeMillis()-lastRunTstmp>GIF_delDelay*60000))
     {
       try
       {
         dclnr.delFilesIfOld();
         this.lastRunTstmp=System.currentTimeMillis();
       }
       catch(Exception e) {}
     }
     return;
   }

   try
   {
     //-- SPECIFIC WEB LOG PRINT --//
     WebLogger webLogger = new WebLogger();
     webLogger.printRequest(request, imgDirBasePath.concat(sysLogDir+"weblog.txt"));
     submit(request,response);
     return;
   }
   catch(Exception e)
   {
     ss.setStartBool(false);
     ss.setErr(e, null, "CONTROL SERVLET ERROR");
     request.setAttribute("errType","CONTROL SERVLET ERROR");
     request.setAttribute("errObj",e);
     debug("CONTROL SERVLET MSG>");
     RequestDispatcher rDsp = getServletContext().getRequestDispatcher(_caughtErrPage);
     rDsp.forward(request,response);
   }
 }


void submit(HttpServletRequest request, HttpServletResponse response) throws Exception
{submit_app(request, response);}

void submit_app(HttpServletRequest request, HttpServletResponse response) throws Exception
{
   String retFlname;                               //the output files base name
   Vector inputFiles, inputFiles_plain;            //keeps the stdf list
   Vector attrsVect=new Vector();		           //keeps the stdf attributes (ie: wafer-no)
   boolean cumulatedBool;                          //cumulated
   int batchExtent;                                //number of splits
   HttpSession session = request.getSession(true); //session

   //-- APPLICATION READER MANAGEMENT --//
   String appCode=(request.getParameterValues("appBut"))[0];
   appRdr app=new appRdr(initDir, (String)session.getAttribute("model_id"), (String)session.getAttribute("analisys_id"), appCode);
   app.setLogWriter(log);
   dataSel dsel=(dataSel)session.getAttribute("dsel");
   slctLst keyLst=dsel.getKey();

   /*** REFRESHE KEY L.O.V. DATA (IF NECESSARY) ***/
   boolean detailedCritBool=false;
   if (app.hasDbImages())
   {
     detailedCritBool=true;
     keyLst.refreshSelData(true, request);
   }

   /*** CHECK THE L.O.V. AND MAKE A COPY FOR USE OF THE CURRENT APP ***/
   dsel.check(request);
   app.setSelCrit(new selCrit(dsel));
   session.setAttribute("app",app);

   //***** OUTPUT FILE NAMES DEFINITION *****
   String ID = request.getSession().getId();
   String time = new Long(System.currentTimeMillis()).toString();
   time=time.substring(time.length()-6,time.length());
   if (!_DEBUG) retFlname=appCode.concat(ID).concat(time);
   else retFlname = new String("VINCI");
   if (_DEBUG) retFlname=retFlname.concat("_"+appCode);  //trivial
   String errorLogName = imgDirBasePath.concat(usrLogDir).concat(retFlname);
   String outFilExt = app.getOutFilExt(this._graphicFileExt);

   //***** EXECUTION OPTIONS MANAGEMENT *****

   String optns="";
   if (app.isKeyDriven())
   {
       inputFiles_plain = app.getInputVector(request, this.imgDirBasePath.concat(this.imgDir), retFlname, attrsVect);
       StringBuffer appOptns=new StringBuffer();
       cumulatedBool=app.getOptnStr(appOptns, request, _execOptionNumber);
       if (cumulatedBool)
       {
         inputFiles_plain.addElement(new String("[cumulated]"));
         attrsVect.addElement(new String("[cumulated]"));
       }
       optns=new String(tolSocket._dlmStr+appCode+appOptns.toString());
   }
   else
   {
       inputFiles_plain = new Vector();
       inputFiles_plain.addElement("SELCRIT");
       app.getOptnStr(new StringBuffer(), request, _execOptionNumber); //VALIDITY CHECK
       cumulatedBool=true;
       optns=tolSocket._dlmStr+"EXTRACT"+app.formatOptns("",0,_execOptionNumber);
   }

   //***** STDF LIST MANIPULATION *****
   inputFiles = bindOutputNames(null, inputFiles_plain, retFlname, outFilExt, optns);
   batchExtent = ((inputFiles.size()<=_batchExtent) ? 1 : _batchExtent);
   if (cumulatedBool)  batchExtent=1;
   if (app.isSingleType()) batchExtent=1;
   Vector inputFilesVect[] = splitStdfList(inputFiles, batchExtent);
   int nGifs=(app.isSingleType() ? 1 : inputFiles.size());

   //***** HEADER XML FILE CREATION *****//
   app.buildHead(request, imgDirBasePath.concat(imgDir), retFlname);

   //***** ITEMS XML FILE CREATION *****//
    if (app.getBuildItemsXmlBool())
    {
     app.buildItems(request, imgDirBasePath.concat(imgDir), retFlname, inputFiles_plain, attrsVect, retFlname, outFilExt);
    }

   //***** CRYTERION XML FILE DEFINITION *****//
   String critFlname;
   for (int k=0; k<batchExtent; k++)
   {
     if (inputFilesVect[k].size()>=1)
     {
       String line=(String)inputFilesVect[k].elementAt(0);
       StringTokenizer st=new StringTokenizer(line," \n",false);
       st.nextToken();
       critFlname=st.nextToken();
       critFlname=critFlname.substring(0,critFlname.lastIndexOf("."));
       app.buildCrit(request, imgDirBasePath.concat(imgDir), critFlname, detailedCritBool);
       if (!app.isKeyDriven())
       {
            app.buildRunl(request, imgDirBasePath.concat(imgDir), critFlname);
       }
     }
   }

   //***** SESSION STATE MANAGEMENT *****//
   socketStatus ss=(socketStatus)session.getAttribute("sckStatus");
   ss.init();
   ss.setStartBool(true);

   //***** SAS INTERACTION *****
   //SOCKET MANAGEMENT
   //when the Socket Monitor has complited its execution, it sets the
   //'startBool' session attribute to 'false'.
   socketMonitor sckMntr = new socketMonitor
   (
       ss,
       disp,
       tolsckArr,
       procsN,
       batchExtent,
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

     request.setAttribute("reqID",retFlname);
     request.setAttribute("analisys_id",session.getAttribute("analisys_id"));
     request.setAttribute("model_id",session.getAttribute("model_id"));
     request.setAttribute("title",app.getTitle());
     for (Enumeration e=request.getParameterNames(); e.hasMoreElements();)
     {
       String parnam=(String)e.nextElement();
       String parval=request.getParameterValues(parnam)[0];
       request.setAttribute(parnam, parval);
     }
     rDsp.forward(request, response);
     debug("FWD>");
   }
}

  /*****************
   *** UTILITIES ***
   *****************/

  protected Vector[] splitStdfList(Vector inputFiles, int batchExtent)
  //it performs the partitioning of one input vector (inputFiles) into 'batchExtent' sub-vectors - theese
  //subvectors are arranged into the array returned by this method
  {
     int nFiles = inputFiles.size();
     int m = (batchExtent > 1 ? nFiles / batchExtent : nFiles);
     int r = (batchExtent > 1 ? nFiles % batchExtent : 0);
     debug(">m"+m+" r"+r+">");
     //inputFilesVect is an array of vector; the k-th vector contains STDF names which must
     //be processed by the process whose ID is the k-th element of procsVect
     Vector inputFilesVect[] = new Vector[batchExtent];
     for (int i=0; i<batchExtent; i++)
     {
         inputFilesVect[i] = new Vector();
         for (int j=i*m; j<(i+1)*m; j++)
         {
           inputFilesVect[i].addElement(inputFiles.elementAt(j));
         }
     }
     //REST MANAGEMENT
     for (int i=0; i<r; i++)
     {
         inputFilesVect[i].addElement(inputFiles.elementAt((m*batchExtent)+i));
     }
     return inputFilesVect;
  }


  public static String getJspUrlPath(String jspPath)
  {
   int i=jspPath.lastIndexOf("/");
   return jspPath.substring(0,i);
  }

  public static Vector getOutNames(String baseName, String filExt, int nOuts)
  {
    String indexSeparator = "N";
    Vector v=new Vector();
    for (int i=0; i<nOuts; i++)
    {
      v.addElement(baseName.concat(indexSeparator).concat(new Integer(i).toString()).concat("."+filExt));
    }
    return v;
  }

  protected Vector bindOutputNames(slctLst keyLst, Vector v, String baseName, String filExt, String optns)
  //RETURNS VECTOR v APPENDING TO ITS ENTRIES THE RESPECTIVE OUTPUT NAME.
  //k-th ELEMENT OF THE RESULTING VECTOR IS: (v[k], outputName(v[k]))
  //OUTPUT NAME IS OBTAINED BY MEANS OF THE getOutName STATIC METHOD.
  {
    Vector vO = new Vector();
    for (int i=0; i<v.size(); i++)
    {
      String outName = getOutName(baseName, filExt, i);
      String keyLstEntry=(String)v.elementAt(i);
      vO.addElement(keyLstEntry.concat(tolSocket._dlmStr+outName+optns));
    }
    return vO;
  }

  public static String getOutName(String baseName, String filExt, int index)
  //given (baseName='XXX', filExt='JPEG', index=0) as input, it returns such a string: 'XXXN0.JPEG'
  {return getOutName(baseName, false, filExt, index);}

  public static String getHeaderName(String baseName)
  {
    return baseName.concat("_HEAD.XML");
  }

  public static String getItemsName(String baseName)
  {
    return baseName.concat("_HEAD2.XML");
  }

  public static String getErrLogName(String baseName, int index)
  {
    String indexSeparator = "N";
    String outName = baseName.concat(indexSeparator).concat(new Integer(index).toString());
    outName = outName.concat("_LOG_ELAB");
    outName = outName.concat(".").concat("XML");
    return outName;
  }

  public static String getMinOutName(String baseName, String filExt, int index)
  //given (baseName='XXX', filExt='JPEG', index=0) as input, it returns such a string: 'XXXN0.JPEG'
  {return getOutName(baseName, true, filExt, index);}

  public static String getMinOutName(String bigGifName)
  {
    int i = bigGifName.lastIndexOf('.');
    String baseName = bigGifName.substring(0,i);
    String ext      = bigGifName.substring(i,bigGifName.length());
    return baseName+"_S"+ext;
  }

  protected static String getOutName(String baseName, boolean miniaturized, String filExt, int index)
  //given (baseName='XXX', filExt='JPEG', index=0) as input, it returns such a string: 'XXXN0_S.JPEG'
  {
    String indexSeparator = "N";
    String outName = baseName.concat(indexSeparator).concat(new Integer(index).toString());
    if (miniaturized) outName=outName.concat("_").concat(_miniaturizedFileExt);
    outName = outName.concat(".").concat(filExt);
    return outName;
  }

  public static void main (String argv [])
  {
    //debug(tolServlet.getMinOutName("XXX.GIF"));
  }
}//class


