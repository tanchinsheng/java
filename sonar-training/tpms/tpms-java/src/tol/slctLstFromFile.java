package tol;

import java.util.Vector;
import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.w3c.dom.*;

public class slctLstFromFile extends slctLst
{
 String baseDir;
 Vector isDirVect;
 Vector dateVect;
 LogWriter log = null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public slctLstFromFile(String name, String label, char type, String p, dbRdr d, String t, String f,
                        String blnk, String bt, int rowsN, boolean isMulti, String baseDir)
 {
  super(name, label, type, p, d, t, f, blnk, bt, rowsN, isMulti);
  this.baseDir=baseDir;
  isDirVect=new Vector();
  dateVect=new Vector();
 }

/**
 * @see #fetchDependent(boolean, HttpServletRequest)
 */
 public boolean fetchDependent(HttpServletRequest req) throws Exception
 {
  return fetchDependent(false, req);
 }

/**
 * Fetches this L.O.V. values according to the filtering conditions
 * defined by the <i>master</i> L.O.V.s.
 *
 * @param mode if false means that no detailed attributes must be retrieved
 *        (the main level attributes, defined by the <i>addAttr</i> method, are
 *         fetched anyway)
 */
 public boolean fetchDependent(boolean mode, HttpServletRequest req) throws Exception
 //mode = false, means that no detailed attributes must be retrieved
 //PS: req is not employed
 {
  String relDir[]=req.getParameterValues(parName);
  String fullDirName=(relDir!=null ? baseDir.concat("/"+relDir[0]) : baseDir);
  debug(fullDirName); //debug
  File dir = new File(fullDirName);
  if (!dir.isDirectory()) throw new IOException("'"+fullDirName+"' DIRECTORY DOESN'T EXIST");
  String[] fileArr = dir.list();
  v.clear(); isDirVect.clear(); dateVect.clear();
  try
  {
    for (int i=0; true; i++)
    {
      v.addElement(fileArr[i]);
      File f=new File(fullDirName, fileArr[i]);
      isDirVect.addElement(new Boolean(f.isDirectory()));
      /*
        Date d=new Date(f.lastModified());
        String day  =new Integer(d.getDay()).toString();
        String month=new Integer(d.getMonth()).toString();
        String year =new Integer(d.getYear()).toString();
        String dateStr=new String(day+"/"+month+"/"+year);
        dateVect.addElement(dateStr);
      */
    }
  }
  catch(ArrayIndexOutOfBoundsException e) {}
  if (v.size()==0) v.addElement(blankField);
  return true;
 }

 public boolean isDir(int i) {return ((Boolean)isDirVect.elementAt(i)).booleanValue();}

 public String getBaseDir() {return baseDir;}

 public void setBaseDir(String s) {this.baseDir=s;}

 public String getFullFileName(String relFname, HttpServletRequest req)
 {
   String relDir[]=req.getParameterValues(parName);
   String fullDirName=(relDir!=null ? baseDir.concat("/"+relDir[0]) : baseDir);
   return new String(fullDirName+"/"+relFname);
 }

 public String getAttrLabel(int attrIndex) {return new String("");}

 public String getAttrLabel(String label)  {return new String("");}
}
