package tol;

import java.util.*;
import javax.servlet.http.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import tol.util.CritBuilder;
import tol.util.RecordsetCritBuilder;

import java.io.*;
import java.sql.*;

public class appRdr
{
 static final String _appFileName="app.xml";
 String code;
 Element docRoot;
 Element layoutRoot;       //can be null
 Element headRoot;         //can be null
 Element inputRoot;
 Element optnsRoot;        //can be null
 Element extractRoot;
 Element methodsRoot;
 Element outlistRoot;      //can be null
 Element outputsRoot=null; //can be null
 Element dbImageRoot=null;//can be null

 int lev;
 String reqID;
 boolean cumulatedBool = false;
 boolean cumulatedOnlyMode = false;
 selCrit slCrt=null;
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg); else System.out.println(msg);}

 public appRdr(String initDir, String model_id, String analisys_id, String app_code) throws Exception
 {

   String fileName=initDir.concat("/"+"analisys"+"/"+model_id+"/"+analisys_id+"/"+app_code);
   fileName=fileName.concat("/"+_appFileName);

   docRoot=xmlRdr.getRoot(fileName,false);

   layoutRoot =xmlRdr.getChild(docRoot,"LAYOUT");
   if (layoutRoot!=null)
   {
     headRoot=xmlRdr.getChild(layoutRoot,"HEADING");
   }
   optnsRoot=xmlRdr.getChild(docRoot,"OPTIONS");
   inputRoot=xmlRdr.getChild(docRoot,"INPUT");
   dbImageRoot=xmlRdr.getChild(docRoot,"DBIMAGE");
   outlistRoot=xmlRdr.getChild(docRoot, "OUTLIST");
   code=app_code;

   //--- EVENTUALLY: BASE APP CONFIGURATION MANAGEMENT ---//
   String baseApp=docRoot.getAttribute("baseApp");
   debug(app_code+":"+baseApp);
   if (inputRoot.getAttribute("type").equals("CRITERION"))
   {
         debug("APP INIT>CRITERION");
         Element baseAppRoot=docRoot;
         if (!baseApp.equals(""))
         {
             debug("baseapp-start>");   //debug
             String baseAppfileName=initDir.concat("/"+"analisys"+"/"+model_id+"/"+"baseapps"+"/"+baseApp+"/"+"baseapp.xml");
             baseAppRoot=xmlRdr.getRoot(baseAppfileName,false);
             extractRoot=xmlRdr.getChild(baseAppRoot,"EXTRACTION");
         }
         else
         {
             debug("APP INIT>EXTROOT"+new Boolean(xmlRdr.getChild(inputRoot,"EXTRACTION")!=null).toString());
             extractRoot=xmlRdr.getChild(inputRoot,"EXTRACTION");
         }
         methodsRoot=xmlRdr.getChild(baseAppRoot,"METHODS");
         outputsRoot=xmlRdr.getChild(baseAppRoot,"OUTPUTS");

         //--- OUTLIST MANAGEMENT ---//
         if (outlistRoot!=null)
         {
           Element baseAppOutlistRoot=xmlRdr.getChild(baseAppRoot,"OUTLISTS");
           String outlistName=outlistRoot.getAttribute("name");
           NodeList nl=baseAppOutlistRoot.getElementsByTagName("OUTLIST");
           outlistRoot=null;
           for (int k=0; k<nl.getLength(); k++)
           {
             if (((Element)nl.item(k)).getAttribute("name").equals(outlistName))
             outlistRoot=(Element)nl.item(k);
           }
         }
   }
 }

 public void setReqID(String s) {reqID=s;}
 public String getReqID() {return reqID;}

 public void setLev(String s) throws Exception {lev=Integer.parseInt(s);}
 public int getLev() {return lev;}

 public void getAppTempFileNames(HttpServletRequest request, Vector v)
 {
   String sID=request.getSession().getId();
   v.addElement(sID.concat("_"+this.getReqID()+"_CRIT.XML"));
   v.addElement(sID.concat("_"+this.getReqID()+"_RUNL.XML"));
 }

 public void getSidTempFileNames(HttpServletRequest request, Vector v)
 {
   String sID=request.getSession().getId();
   if (this.outlistRoot!=null)
   {
     v.addElement(sID.concat("_OUTLIST_"+new Integer(getLev()).toString()+".XML"));
   }
 }

 public boolean hasDbImages()  {return (this.dbImageRoot!=null);}

 public Vector getInpuDbImageNames()
 {
   if (inputRoot==null) return null;
   else
   {
     Vector v=new Vector();
     NodeList images=inputRoot.getElementsByTagName("DBIMAGE");
     for (int i=0; i<images.getLength(); i++)
     {
        v.addElement(((Element)images.item(i)).getAttribute("name"));
     }
     return v;
   }
 }

  public String[] getInputCubeNames()
  {
    if (inputRoot==null) return null;
    else
    {
      NodeList nl=inputRoot.getElementsByTagName("CUBE");
      String[] cubes=new String[nl.getLength()];
      for (int i=0; i<nl.getLength(); i++)
      {
         cubes[i]=new String((((Element)nl.item(i)).getAttribute("name")));
      }
      return cubes;
    }
  }

 public void createInputDbImages(dataSel dsel, Connection outConn) throws Exception
 {
    Vector imageNames=this.getInpuDbImageNames();
    if (imageNames.size()==0) return;
    Connection srcConn = dsel.getDbRdr().getNewConnDbRdr().getDbConnection();
    try
    {
        long allImgsLoadTime=System.currentTimeMillis();
        for (int i=0; i<imageNames.size(); i++)
        {
             String imageName=(String)imageNames.elementAt(i);
             repSect dbimage=(repSect)dsel.get(imageName);
             long imgLoadTime=System.currentTimeMillis();
             dbimage.createImage(srcConn, outConn, this.getReqID());
             imgLoadTime=(System.currentTimeMillis()-imgLoadTime)/1000;
             debug("DB-IMAGE-UPLOAD-TIME-"+imageName+">"+new Long(imgLoadTime).toString());
        }
        allImgsLoadTime=(System.currentTimeMillis()-allImgsLoadTime)/1000;
        debug("DB-IMAGES-UPLOAD-TIME-"+this.getCode()+">"+new Long(allImgsLoadTime).toString());
    }
    catch (Exception e) {throw e;}
    finally { srcConn.close(); }
 }

 public void truncateInputDbImages(dataSel dsel, Connection outConn) throws Exception
 {
   Vector imageNames=this.getInpuDbImageNames();
   if (imageNames.size()==0) return;
   try
   {
       for (int i=0; i<imageNames.size(); i++)
       {
            String imageName=(String)imageNames.elementAt(i);
            repSect dbimage=(repSect)dsel.get(imageName);
            debug("TRUNCATING "+dbimage.getOutputTableName()+">");
            Statement sqlSttmt = outConn.createStatement();
            sqlSttmt.execute("TRUNCATE TABLE "+dbimage.getOutputTableName());
       }
   }
   catch (Exception e) {throw e;}
 }

 public Properties getDbImageFields()
 {
  if (this.dbImageRoot==null) return null;
  else
  {
    NodeList flds=dbImageRoot.getElementsByTagName("FIELD");
    if (flds.getLength()==0) return null;
    else
    {
      Properties fldProps=new Properties();
      for (int i=0; i<flds.getLength(); i++)
      {
        String attr=((Element)flds.item(i)).getAttribute("attr");
        String colname=((Element)flds.item(i)).getAttribute("colname");
        fldProps.setProperty(attr,(colname.equals("") ? attr.toUpperCase() : colname.toUpperCase()));
      }
      return fldProps;
    }
  }
 }

 public boolean hasOutputs()  {return (this.outputsRoot!=null);}


 public String getRunlFileName(HttpServletRequest request, String runlFnamePrefix)
 {
   String sID=request.getSession().getId();
   String fileName=(runlFnamePrefix!=null ?  runlFnamePrefix : sID.concat("_"+reqID));
   fileName=fileName.concat("_RUNL.XML");
   return fileName;
 }

 public String getCritFileName(HttpServletRequest request)
 {return this.getCritFileName(request, null);}

 public String getCritFileName(HttpServletRequest request, String critFnamePrefix)
 {
   String sID=request.getSession().getId();
   String fileName=(critFnamePrefix!=null ?  critFnamePrefix : sID.concat("_"+reqID));
   fileName=fileName.concat("_CRIT.XML");
   return fileName;
 }

 public String getHeaderFileName(HttpServletRequest request, String headerFnamePrefix)
 {
   String sID=request.getSession().getId();
   String fileName=(headerFnamePrefix!=null ?  headerFnamePrefix : sID.concat("_"+reqID));
   fileName=fileName.concat("_HEAD.XML");
   return fileName;
 }

 public String getItemsFileName(HttpServletRequest request, String headerFnamePrefix)
 {
   String sID=request.getSession().getId();
   String fileName=(headerFnamePrefix!=null ?  headerFnamePrefix : sID.concat("_"+reqID));
   fileName=fileName.concat("_HEAD2.XML");
   return fileName;
 }

 public String getOutFileName(HttpServletRequest request, boolean smallBool, boolean debug)
 {
   String sID=request.getSession().getId();
   String fileName;
   if (debug) fileName=new String("VINCI");
   else fileName=sID.concat("_"+reqID);
   Element outEl=(Element)outputsRoot.getElementsByTagName("OUTPUT").item(0);
   String filExt=outEl.getAttribute("filExt");
   if (outEl.getAttribute("type").equals("OUTLIST"))
   {
     fileName=new String((debug ? "VINCI" : sID)+"_OUTLIST_"+new Integer(lev).toString());
     filExt="XML";
   }
   else
    if (outEl.getAttribute("type").equals("LOG"))
    {
      Vector logFnames=this.getLogFileNames(request, debug);
      return (String)logFnames.elementAt(logFnames.size()-1);
    }
    else
     if (outEl.getAttribute("type").equals("REPORT"))
     {
       if (debug) fileName=fileName.concat("_"+this.code);
       else fileName=fileName.concat("_OUT");
     }
     else
      if (outEl.getAttribute("type").equals("CHART"))
      {
        if (debug) fileName=fileName.concat("_"+this.code);
        else fileName=fileName.concat("_OUT");
        fileName=fileName.concat(smallBool ? "_S" : "");
      }
   fileName=fileName.concat("."+filExt);
   return fileName;
 }


 public Vector getLogFileNames(HttpServletRequest request, boolean debug)
 {
   String sID=request.getSession().getId();
   String filExt="XML";
   String fileName;
   if (debug) fileName=new String("VINCI").concat("_"+this.code);
   else fileName=sID.concat("_"+reqID);
   Vector v=new Vector();
   v.addElement(fileName.concat("_LOG_"+"EXTR"+"."+filExt));
   v.addElement(fileName.concat("_LOG_"+"ELAB"+"."+filExt));
   return v;
 }


 public String getOutType()
 {
   Element outEl=(Element)outputsRoot.getElementsByTagName("OUTPUT").item(0);
   return outEl.getAttribute("type");
 }

 public String getCode() {return code;}

 public Element getImagesRoot() throws Exception
 {
   //debug("EXTRACT-ROOT>"+new Boolean(extractRoot!=null).toString());
   //debug("EXTRACT-ROOT>"+extractRoot.getAttribute("appcode"));
   return xmlRdr.getChild(extractRoot,"IMAGES");
 }
 public Element getMethodsRoot() {return this.methodsRoot;}

 public Element getOutlistRoot() {return this.outlistRoot;}

 public String getFileName()
 {
   return docRoot.getAttribute("fileName");
 }

 public boolean getLframeBool()
 {
   return layoutRoot.getAttribute("leftFrame").equals("Y");
 }

 public boolean isDemo()
 {
   return docRoot.getAttribute("demo").equals("Y");
 }

 public static String getServletName(String servletName, String demoBoolStr)
 {
   if (demoBoolStr.equals("Y")) return servletName.concat("_demo");
   else return servletName;
 }


 public int getNoptRows() throws Exception
 {
   return Integer.valueOf(layoutRoot.getAttribute("nOptRows")).intValue();
 }

 public String getTitle() throws Exception
 {
   if (headRoot==null) return null;
   return xmlRdr.getVal(headRoot,"TITLE");
 }

 public String getTitleImg() throws Exception
 {
   return xmlRdr.getChild(headRoot,"TITLE").getAttribute("img");
 }

 public String getExtrAppCode() throws Exception
 {
   return extractRoot.getAttribute("appcode");
 }

 public NodeList getOptions()
 {
   return optnsRoot.getElementsByTagName("OPTION");
 }

/**
 * called by the analisys manager (in case the analisys is 'static') so to
 * define which options should be set by the user
 */
 public void getOptns_forAnalisys(Element tEl) throws Exception
 {
     Element tEl_optns=xmlRdr.addEl(tEl,"OPTIONS");
     if (optnsRoot!=null)
     {
       NodeList items=optnsRoot.getElementsByTagName("OPTION");
       for (int i=0; i<items.getLength(); i++)
       {
         Element item=(Element)items.item(i);
         if (item.getAttribute("show").equals("Y"))
         {
           item.setAttribute("appID",getCode());
           TransformerFactory.newInstance().newTransformer().transform(new DOMSource(item), new DOMResult(tEl_optns));

         }
       }
     }
 }

 public String getAppType()
 {
   return docRoot.getAttribute("type");
 }

 public boolean getBuildItemsXmlBool()
 {
   return !(docRoot.getAttribute("buildItemsXml").equals("N"));
 }

 public boolean isKeyDriven()
 {
   return (inputRoot.getAttribute("type").equals("KEY"));
 }

 public String getOutFilExt(String defaultStr)
 {
   String s=docRoot.getAttribute("filExt");
   return (s.equals("") ? defaultStr : s);
 }

 public Integer getMaxNofKeys()
 {
     if (inputRoot!=null)
     {
         String n=inputRoot.getAttribute("maxKeys");
         return (n.equals("") ? null : Integer.valueOf(n));
     }
     else return null;
 }

 public boolean isSingleType()
 {
   return (getAppType().equals("SINGLE"));
 }

 public void printHeadItemsTablar(HttpServletRequest request, Writer out, String logoTag) throws Exception
 {this.printHeadItemsTablar(request,out,logoTag,true);}

 public void printHeadItemsTablar(HttpServletRequest request, Writer out, String logoTag, boolean tableTagBool) throws Exception
 {
  HttpSession session=request.getSession();
  NodeList items=null;
  if (headRoot!=null) items=headRoot.getChildNodes();
  else return;
  Vector vals=new Vector();
  int printedItems=0;
  if (tableTagBool)
  {
    out.write("<TABLE BORDER=\"0\" CELLPADDING=\"0\" WIDTH=\"100%\">"+"\n");
    out.write("<TR>"+"\n");
  }
  out.write("<TD ALIGN=\"LEFT\">"+logoTag+"</TD>"+"\n");
  for (int i=0; i<items.getLength(); i++)
  {
   String alignTag="ALIGN=\"CENTER\"";
   if (!(items.item(i) instanceof Element)) continue;
   Element item=(Element)items.item(i);
   if (item.getNodeName().equals("HFIELD"))
   {
    String label=xmlRdr.getVal(item,"LABEL");
    String lovName=xmlRdr.getVal(item,"LOVNAME");
    String val=slCrt.getBindString(lovName, label, true);
    if (val==null) continue;
    else
      if (val.equals("")) continue;
      else
       {
         printedItems++;
         out.write("<TD "+alignTag+"><FONT SIZE=\"-1\">"+xmlRdr.format(val)+"</FONT></TD>"+"\n");
       }
   }
   if (item.getNodeName().equals("APPOPT"))
   {
    String label=xmlRdr.getVal(item,"LABEL");
    String optName=xmlRdr.getVal(item,"OPTNAME");
    Element opt=this.getOptEl(optName);
    String val;
    if ((opt==null)||(opt.getAttribute("value").equals("")))
    {
      //used for:
      //  1) retrieving exec options set up by other applications, whose
      //     values are stored into the Http Session
      //  2) compatibility towards STDF analysis, as in STDF analysis
      //     each execution option is stored into the HTTP Session
      val=(String)session.getAttribute(optName);
    }
    else
    {
      val=opt.getAttribute("value");
    }
    if (val==null) continue;
    else
      if (val.equals("")) continue;
      else
      {
        printedItems++;
        out.write("<TD "+alignTag+"><FONT SIZE=\"-1\">"+((label!=null ? label+"=" : "")+xmlRdr.format(val)+" ")+"</FONT></TD>"+"\n");
      }
   }//endif: APPOPT case
  }
  if (tableTagBool)
  {
    out.write("</TR>"+"\n");
    out.write("</TABLE>"+"\n");
  }
 }

/**
 * returns the OPTION element with the given name.
 * if the research fails, return null.
 */
 Element getOptEl(String optName)
 {
   if (this.optnsRoot==null) return null;
   NodeList optns=optnsRoot.getElementsByTagName("OPTION");
   for (int i=0; i<optns.getLength(); i++)
   {
     Element opt=(Element)optns.item(i);
     if (opt.getAttribute("name").equals(optName)) return opt;
   }
   return null;
 }

 public String formatOptns(String optns, int usedNum, int execOptionNumber)
 {
   return appRdr.normalizeOptnsLine(optns, usedNum, execOptionNumber);
 }

 public static String normalizeOptnsLine(String optns, int usedNum, int execOptionNumber)
 //it adds to the option string (optns) as many missing values as the number of the unspecified options.
 //the number of missing values results from _execOptionNumber - usedNum; the latter parameter is the number
 //of the options explicitly defined by the user.
 {
  for (int i=usedNum; i<execOptionNumber; i++)
  {
    optns = optns.concat(tolSocket._dlmStr);
    optns = optns.concat(".");
  }
  return optns;
 }

/**
 * Returns the key list - must be called for key driven applications only *
 */
 public Vector getInputVector(HttpServletRequest request, String dir, String critFnamePrefix, Vector attrsVect) throws Exception
 {
     HttpSession session=request.getSession();
     dataSel dsel=(dataSel)session.getAttribute("dsel");
     slctLst keyLst=dsel.getKey();
     //keyLstPrefix : plant's STDFHOME field content
     //param keyLstPrefix2Bool: true iff the plant's STDFPATH field is set to 'Y'
     String keyLstPrefix=keyLst.getDbRdr().getCfgProp("STDFHOME");
     boolean keyLstPrefix2Bool=((keyLst.getDbRdr().getCfgProp("STDFPATH").toUpperCase().equals("Y"))||(keyLst.getDbRdr().getCfgProp("STDFPATH").toUpperCase().equals("REL.YES")));
     //debug("stdf-dir-bool>"+keyLstPrefix2Bool); //debug

     //-- KEY LIST MANAGEMENT --//
     if (inputRoot.getAttribute("source").equals("REQUEST"))
     {
       Vector v=new Vector();
       String keyLstItem=(request.getParameterValues(inputRoot.getAttribute("name")))[0];
       if (keyLst.getName().equals("stdf"))
       {
         keyLstItem=(keyLstItem.trim().equals("") ? "." : (keyLstPrefix2Bool ? keyLst.getAttrVal("stdf-dir", keyLstItem).concat("/"+keyLstItem) : keyLstItem));
         keyLstItem = new String((keyLstPrefix!=null ? keyLstPrefix+"/" : "")+keyLstItem);
       }
       v.addElement(keyLstItem);
       return v;
     }
     else
     {
       Vector v, out;
       v=keyLst.getVector();
       if (keyLst.getName().equals("stdf"))
       {
         out=new Vector();
         for (int i=0; i<v.size(); i++)
         {
           String keyLstItem=(String)v.elementAt(i);
	         if (keyLstItem.trim().equals("")) continue;
           String attr="-";
           if (!(keyLst instanceof slctLstFromFile))
           {
	           attr=keyLst.getAttrVal("wafer", keyLstItem);
             if (attr==null) throw new NumberFormatException("UNDEFINED 'WAFER' PRIMARY ATTRIBUTE FOR STDF FIELD");
           }
           keyLstItem=(keyLstPrefix2Bool ? keyLst.getAttrVal("stdf-dir", keyLstItem).concat("/"+keyLstItem) : keyLstItem);
           String keyLstEntry = new String((keyLstPrefix!=null ? keyLstPrefix+"/" : "")+keyLstItem);
           out.addElement(keyLstEntry);
           attrsVect.addElement(attr);
           //debug("stdf"+keyLstEntry+" attr>"+attr);
         }
       }
       else
       {
         out=(Vector)v.clone();
       }
       return out;
     }
 }



 public void buildCrit(HttpServletRequest request, String dir) throws Exception
 {this.buildCrit(request, dir, null, false);}

/**
 * Dir is the ouput criterion file directory
 */
 public void buildCrit(HttpServletRequest request, String dir, String critFnamePrefix, boolean detailedCritBool) throws Exception
 {
     String critFname=this.getCritFileName(request, critFnamePrefix);
     HttpSession session=request.getSession();
     dataSel dsel=(dataSel)session.getAttribute("dsel");
     CritBuilder crtbldr=(CritBuilder)session.getAttribute("crtbldr");
     if (this.isKeyDriven()) crtbldr=new RecordsetCritBuilder(crtbldr);
     crtbldr.getCrit(code, dsel, dir, critFname, detailedCritBool, this.getDbImageFields());
 }

 public void buildHead(HttpServletRequest request, String dir, String headFnamePrefix) throws Exception
 {
     String headFname=this.getHeaderFileName(request, headFnamePrefix);
     HttpSession session=request.getSession();
     FileWriter fout=new FileWriter(dir.concat(headFname));
     fout.write("<?xml version=\"1.0\"?>"+"\n");
     fout.write("<HEADER>"+"\n");
     fout.write("<TITLE>"+this.getTitle()+"</TITLE>"+"\n");
     this.printHeadItemsTablar(request, fout, "$LOGO", true);
     fout.write("</HEADER>"+"\n");
     fout.close();
 }

 public void buildItems(HttpServletRequest request, String dir, String headFnamePrefix, Vector inputKeys, Vector inputAttrs, String baseGifName, String filExt) throws Exception
 {
     String itemsFname=this.getItemsFileName(request, headFnamePrefix);
     HttpSession session=request.getSession();
     dataSel dsel = (dataSel)session.getAttribute("dsel");
     String attrName = dsel.getKey().getAttrLabel("wafer");
     String keyName  = dsel.getKey().getLabel();
     FileWriter fout=new FileWriter(dir.concat(itemsFname));
     fout.write("<?xml version=\"1.0\"?>"+"\n");
     fout.write("<ITEMS>"+"\n");
     if ((getAppType().equals("SINGLE"))||(this.cumulatedOnlyMode))
     {
       fout.write("<ITEM "+(this.cumulatedOnlyMode ? "caption=\"CUMULATED\"" : "")+">"+"\n");
         fout.write("<GIFNAME>"+tolServlet.getOutName(baseGifName, filExt, 0)+"</GIFNAME>"+"\n");
       fout.write("</ITEM>"+"\n");
     }
     else
     {
       for (int i=0; i<inputKeys.size(); i++)
       {
         if (((String)inputKeys.elementAt(i)).equals("[cumulated]"))
         {
           fout.write("<ITEM caption=\"CUMULATED\">"+"\n");
             fout.write("<GIFNAME>"+tolServlet.getOutName(baseGifName, filExt, i)+"</GIFNAME>"+"\n");
           fout.write("</ITEM>"+"\n");
         }
         else
         {
           fout.write("<ITEM>"+"\n");
             fout.write("<"+attrName+">"+xmlRdr.format((String)inputAttrs.elementAt(i))+"</"+attrName+">"+"\n");
             fout.write("<"+keyName+" caption=\"N\">"+xmlRdr.format((String)inputKeys.elementAt(i))+"</"+keyName+">"+"\n");
             fout.write("<GIFNAME>"+tolServlet.getOutName(baseGifName, filExt, i)+"</GIFNAME>"+"\n");
           fout.write("</ITEM>"+"\n");
         }
       }
     }
     fout.write("</ITEMS>"+"\n");
     fout.close();
 }


 public void buildRunl(HttpServletRequest request, String dir) throws Exception
 {this.buildRunl(request, dir, null);}

/**
 * dir is the runlist criterion file directory
 */
 public void buildRunl(HttpServletRequest request, String dir, String runlPrefix) throws Exception
 {
     HttpSession session=request.getSession();
     CritBuilder crtbldr=(CritBuilder)session.getAttribute("crtbldr");
     String fname=this.getRunlFileName(request, runlPrefix);
     Document doc=xmlRdr.getNewDoc("RUNLIST");
     Element root=doc.getDocumentElement();
     Element runlImgsRoot=xmlRdr.addEl(root,"IMAGES");
     Element images=crtbldr.getImagesDefRoot();
     xmlRdr.print(docRoot.getOwnerDocument(), log.getOut(), true);
     debug("RUN LIST FILE>"+dir+"/"+fname);
     NodeList immagini=getImagesRoot().getElementsByTagName("IMAGE");
     for (int k=0; k<immagini.getLength(); k++)
     {
       if (!(immagini.item(k) instanceof Element)) continue;
       Element immagine=(Element)immagini.item(k);
       String image=xmlRdr.getVal(immagine);
       debug("IMAGE>"+image); //debug
       Element el_image = xmlRdr.addEl(runlImgsRoot, "IMAGE");
       el_image.setAttribute("mand", (immagine.getAttribute("mand").equals("Y") ? "Y" : "N"));
       el_image.setAttribute("name",image);
       //----- leggo i campi dell'immagine corrente -----//
       NodeList rows=images.getChildNodes();
       int j=0;
       for (int i=0; i<rows.getLength(); i++)
       {
         if (!(rows.item(i) instanceof Element)) continue;
         Element n=(Element)rows.item(i);
         String cur_img=n.getAttribute("name");
         if (cur_img.equals(image))
         {
           NodeList fields=n.getChildNodes();
           int j2=0;
           for (j=0; j<fields.getLength(); j++)
           {
             if (!(fields.item(j) instanceof Element)) continue;
             Element f=(Element)fields.item(j);
             j2++;
             String index=new Integer(j2).toString();
             String field=xmlRdr.getVal(f);
             debug("IMAGE-FIELD>"+field);
             Element el_fld=xmlRdr.addEl(el_image,"FIELD", field);
           }
         }
         j++;
       }
    }

    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.transform(new DOMSource(getMethodsRoot()), new DOMResult(root));
    if (getOutlistRoot()!=null) transformer.transform(new DOMSource(getOutlistRoot()), new DOMResult(root));
    this.getOptnStr(null, root, request, 0);
    
    //--- PRINT RUN LIST TO XML FILE ---//
    FileOutputStream output = new FileOutputStream(dir.concat("/"+fname), false);
    xmlRdr.echo(doc, new PrintStream(output));
    output.close();
 }


 public boolean getOptnStr(StringBuffer retStr, HttpServletRequest request, int execOptionNumber) throws Exception
 {
   return getOptnStr(retStr, null, request, execOptionNumber);
 }


 public boolean getOptnStr(Element tEl, HttpServletRequest request, int execOptionNumber) throws Exception
 {
   return getOptnStr(null, tEl, request, execOptionNumber);
 }

/**
 * Eventually (if tEl!=null) puts into tEl element the options bindings,
 * set up at run-time.
 * Eventually (if retStr!=null) puts the options bindings into retStr.
 */
 public boolean getOptnStr(StringBuffer retStr, Element tEl, HttpServletRequest request, int execOptionNumber) throws Exception
 {
   HttpSession session=request.getSession();
   boolean cumulatedBool=false;
   dataSel dsel=(dataSel)session.getAttribute("dsel");
   String optns="";
   Element tEl_optns=null;
   //-- RUN LIST OPTIONS ELEMENT INITIALIZATION --//
   if (tEl!=null)
   {
     tEl_optns=xmlRdr.addEl(tEl,"OPTIONS");
   }
   if (optnsRoot==null)
   {
     optns=this.formatOptns("", 0, execOptionNumber);
     if (retStr!=null) retStr.append(optns);
     return false;
   }
   NodeList items=optnsRoot.getChildNodes();
   int nOptns=0;
   for (int i=0; i<items.getLength(); i++)
   {
    if (!(items.item(i) instanceof Element)) continue;
    Element item=(Element)items.item(i);
    String optName="";
    String label="";
    String optVal;
    boolean isList=false;
    if (item.getNodeName().equals("OPTION"))
    {
      if (item.getAttribute("show").equals("Y"))
      {
        optName=item.getAttribute("name");
        label=item.getAttribute("label");
        if (request.getParameterValues(optName)==null)
        {
          throw new NumberFormatException(label.toUpperCase()+" NOT SPECIFIED");
        }
        else optVal=(request.getParameterValues(optName))[0];
        if (optVal.equals(""))
        {
          if (!item.getAttribute("mand").equals("N"))
          {
            if (item.getAttribute("send").equals("Y"))
            {
              throw new NumberFormatException(label.toUpperCase()+" NOT SPECIFIED");
            }
          }
          else
          {
            optVal=".";
            if (item.getAttribute("send").equals("Y"))
            {
              optns=optns.concat(tolSocket._dlmStr+optVal);
              continue;
            }
          }
        }
        NodeList optVals=item.getElementsByTagName("OPTVAL");
        //-- LIST OPTIONS SCAN: EVENTUALLY UPDATES optVal --//
        for (int j=0; j<optVals.getLength(); j++)
        {
          isList=true;
          Element optVitem=(Element)optVals.item(j);
          if (optVal.equals(optVitem.getAttribute("show")))
          {
            optVal=optVitem.getAttribute("send");
            if ((optVitem.getAttribute("cumulated").equals("Y"))||(optVitem.getAttribute("cumulated").equals("O"))) cumulatedBool=true;
            if (optVitem.getAttribute("cumulated").equals("O")) cumulatedOnlyMode = true;
            break;
          }
        }
        //-- CHECK FORMAT --//
        if (item.getAttribute("dataType").equals("N"))
        {
          Integer intVal;
          //-- NOT NULL CHECK --//
          if (optVal.trim().equals(""))
          {
              throw new NumberFormatException(label.toUpperCase()+" NOT SPECIFIED");
          }
          //-- NUMERIC FORMAT CHECK --//
          try{intVal=Integer.valueOf(optVal);}
          catch(NumberFormatException e)
          {
            throw new NumberFormatException(label.toUpperCase()+" WRONG FORMAT");
          }
          //-- VALIDITY RANGE CHECK --//
          {
            String minVal=item.getAttribute("minval");
            String maxVal=item.getAttribute("maxval");
            if (!minVal.trim().equals(""))
            {
              if (intVal.intValue()<Integer.valueOf(minVal).intValue())
                throw new NumberFormatException(label.toUpperCase()+" MUST BE >= "+minVal);
            }
            if (!maxVal.trim().equals(""))
            {
              if (intVal.intValue()>Integer.valueOf(maxVal).intValue())
                throw new NumberFormatException(label.toUpperCase()+" MUST BE <= "+maxVal);
            }
          }
        } // NUMBER CASE
        if ((item.getAttribute("dataType").equals("CL"))||(item.getAttribute("dataType").equals("NL")))
        {
          Integer intVal;
          //-- NOT NULL CHECK --//
          if (optVal.trim().equals(""))
          {
            throw new NumberFormatException(label.toUpperCase()+" NOT SPECIFIED");
          }
          StringTokenizer st = new StringTokenizer(optVal, ",-", true);
          boolean sepBoolExpected=false;
          int itemCont = 0;
          String sep   = "";
          int oldIntVal  = 0; /* used only for list of numbers */
          while(st.hasMoreTokens())
          {
            String s = st.nextToken();
            if (sepBoolExpected)
            {
              if ((!s.equals(","))&&(!s.equals("-"))) throw new NumberFormatException(label.toUpperCase()+" WRONG FORMAT - COMMA OR DASH EXPECTED");
              sep=s;
            }
            else
            {
              if ((s.equals(","))||(s.equals("-"))) throw new NumberFormatException(label.toUpperCase()+" WRONG FORMAT - VALUE EXPECTED AFTER A SEPARATOR OR AT THE FIRST POSITION");
              if(item.getAttribute("dataType").equals("NL"))
              {
                try{intVal=Integer.valueOf(s.trim());}
                catch(NumberFormatException e)
                {
                  throw new NumberFormatException(label.toUpperCase()+" WRONG FORMAT - LIST OF NUMBERS EXPECTED");
                }
                if (sep.equals("-"))
                {
                  itemCont+=(intVal.intValue()-oldIntVal);
                }
                else itemCont++;
                oldIntVal=intVal.intValue();
              }
              else
              {
                itemCont++;
              }
            }
            sepBoolExpected=!sepBoolExpected;
          }
          if (!sepBoolExpected) throw new NumberFormatException(label.toUpperCase()+" WRONG FORMAT - A LIST CANNOT END WITH A SEPARATOR");
          if (!item.getAttribute("maxItems").equals(""))
          {
            int maxItems = Integer.valueOf(item.getAttribute("maxItems").trim()).intValue();
            if (itemCont>maxItems)
            {
              throw new NumberFormatException(label.toUpperCase()+" - LESS THAN "+maxItems+" ITEMS MUST BE SELECTED");
            }
          }
          //-- REMOVE INNER BLANKS --//
          optVal = removeInnerBlanks(optVal);
        }
        session.setAttribute(optName,optVal);
      }
      else
      {
        optVal=item.getAttribute("default");
      }

      if ((item.getAttribute("cumulated").equals("Y"))||(item.getAttribute("cumulated").equals("O"))) cumulatedBool=true;
      if (item.getAttribute("cumulated").equals("O")) cumulatedOnlyMode = true;

      if (item.getAttribute("send").equals("Y"))
      {
       optns=optns.concat(tolSocket._dlmStr+optVal);
       //-- RUN LIST OPTIONS ELEMENT INITIALIZATION --//
       if (tEl!=null)
       {
         Element newEl=xmlRdr.addEl(tEl_optns,"OPTION");
         xmlRdr.addEl(newEl,"NAME",item.getAttribute("outname"));
         xmlRdr.addEl(newEl,"VALUE",optVal);
         item.setAttribute("value",optVal);
       }
       nOptns++;
      }
    }
   }
   optns=this.formatOptns(optns, nOptns, execOptionNumber);
   if (retStr!=null) retStr.append(optns);
   this.cumulatedBool = cumulatedBool;
   return cumulatedBool;
 }

 String removeInnerBlanks(String s)
 {
   StringTokenizer st = new StringTokenizer(s," \t\n\r\f",false);
   String ret="";
   while (st.hasMoreTokens()) ret=ret.concat(st.nextToken());
   return ret;
 }

 public selCrit getSelCrit()
 {return slCrt;}

 public void setSelCrit(selCrit s)
 {this.slCrt=s;}

 public static void main(String args[]) throws Exception
 {
  //appRdr a=new appRdr("/tomcat/webapps/cfg","SS00","STDF_ANALISYS","MBS");
  //debug(a.getTitle());
  //debug(a.getOptions().getLength());
  //debug(Integer.valueOf("10"));
 }
}
