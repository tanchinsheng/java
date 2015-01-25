package tol;

import java.util.*;
import javax.servlet.jsp.JspWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.w3c.dom.*;

public class analisysMgr
{
 public static final String _initFileName="analisys.xml";
 Element root;
 Element appsRoot;
 Element branchesRoot;
 NodeList branchesList;
 NodeList appsList;
 Element curApp;
 Vector appStack;
 Vector appRdrs; //appRdr instances list
 Vector oldTempFileNames=new Vector();
 int curLev;
 selCrit slCrt=null;
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}


 public analisysMgr(String initDir, String model_id, String analisys_id) throws Exception
 {
   try
   {
     String fileName=initDir.concat("/analisys/"+model_id+"/"+analisys_id+"/"+_initFileName);
     FileInputStream f=new FileInputStream(fileName);
     f.close();
     root = xmlRdr.getRoot(fileName, false);
     appsRoot = xmlRdr.getChild(root,"APPS");
     branchesRoot = xmlRdr.getChild(root,"BRANCHES");
     branchesList = (branchesRoot!=null ? branchesRoot.getElementsByTagName("BRANCH") : null);
     curApp=appsRoot;
     appsList = appsRoot.getElementsByTagName("APP");
   }
   catch(FileNotFoundException e) {debug("NO ANALISYS FILE FOUND>"); throw e;}
   catch(Exception e)             {debug("ANALISYS FILE PARSING ERROR>"); throw e;}
   setLev();
   setCurApp();
   appStack=new Vector();
   buildAppStack(appStack);
   appRdrs=new Vector();
   setCurBranch();
   for (int i=0; i<appStack.size(); i++)
   {
     try
     {
       Element appEl=(Element)appStack.elementAt(i);
       String appCode=appEl.getAttribute("id");
       appRdr app=new appRdr(initDir, model_id, analisys_id, appCode);
       app.setLev(appEl.getAttribute("lev"));
       app.getOptns_forAnalisys(appEl); //new
       appRdrs.addElement(app);
     }
     catch(Exception e){debug("APPLICATION INSTANTIATION FAILURE>"); throw e;}
   }
 }

 public Element getBranchesRoot() {return branchesRoot;}

 public void setCurBranch()
 {
     if (branchesList!=null)
     {
            for (int i=0; i<branchesList.getLength(); i++)
            {
                Element branch=(Element)branchesList.item(i);
                if (branch.getAttribute("default").equals("Y"))
                {
                     branchesRoot.setAttribute("curBranch",branch.getAttribute("id"));
                }
            }
     }
 }

 public void setCurBranch(String branchID)
 {
     if (branchesRoot!=null)
     {
         branchesRoot.setAttribute("curBranch",branchID);
     }
 }

 public String getCurBranchID() {return (branchesRoot!=null ? branchesRoot.getAttribute("curBranch") : "");}

 public NodeList getBranchesList() {return branchesList;}

 public String getType() {return root.getAttribute("type");}

 public String getTitle() {return root.getAttribute("title");}

 public boolean isDemo() {return (root.getAttribute("isDemo").toUpperCase().equals("Y"));}

 public String getServletName(String servletName)
 {
   if (isDemo()) return servletName.concat("_demo");
   else return servletName;
 }

 public Vector getAppRdrs() {return appRdrs;}

 public appRdr getApp(String appCode)
 {
   for (int i=0; i<appRdrs.size(); i++)
   {
       appRdr app=(appRdr)appRdrs.elementAt(i);
       if (app.getCode().equals(appCode)) return app;
   }
   return null;
 }

 public Element getRoot() {return root;}

 public void setLev() {curLev=-1;}

 public void incLev() {curLev++;}

 public void decLev() {curLev--;}

 public int getLev() {return curLev;}

 public void setCurApp(Element el) {curApp=el;}

 public void setCurApp() {curApp=appsRoot;}

 public void setOldTempFileNames(Vector v) {this.oldTempFileNames=v;}

 public Vector getOldTempFileNames() {return this.oldTempFileNames;}

 public Element getCurApp() {return curApp;}

 public String getLogo()
 {
   return root.getAttribute("logo");
 }

 public void printButtons(JspWriter out, String jsFun) throws IOException
 {
  for (int i=0; i<appsList.getLength(); i++)
  {
    Element el=(Element)appsList.item(i);
    if (el.getParentNode()==appsRoot)
    {
      out.println("<INPUT TYPE=\"BUTTON\" VALUE=\""+el.getAttribute("butLabel") +"\" onClick=\""+jsFun+"('"+el.getAttribute("id")+"')\">");
    }
  }
 }

 public Vector getAppStack()
 {
   return this.appStack;
 }

 public NodeList getAppsList()
 {return appsList;}

 void buildAppStack(Vector v)
 {
   if (getLev()>-1)
   {
     getCurApp().setAttribute("lev",new Integer(getLev()).toString());
     v.addElement(getCurApp());
   }
   NodeList sonApps=getCurApp().getChildNodes();
   incLev();
   for (int i=0; i<sonApps.getLength(); i++)
   {
     if ((sonApps.item(i) instanceof Element)&&(((Element)sonApps.item(i)).getNodeName().equals("APP")))
     {
       setCurApp((Element)sonApps.item(i));
       buildAppStack(v);
     }
   }
   decLev();
 }

 public selCrit getSelCrit()
 {return slCrt;}

 public void setSelCrit(selCrit s)
 {this.slCrt=s;}

 public static void main(String args[]) throws Exception
 {
   analisysMgr a=new analisysMgr("/tomcat/webapps/cfg","IE40","YIELD_ANALISYS");
   //debug(a.getAppStack());
 }
}
