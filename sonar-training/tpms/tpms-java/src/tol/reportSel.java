package tol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class reportSel extends dataSel
{
 public reportSel(String fileDir, dbRdr dbr, LogWriter log) throws Exception
 {
   this._xmlFname="repdesc.xml";
   this.setIgnoreCaseBool(true);
   init(fileDir, dbr, log, this._xmlFname);
 }

 public reportSel(String fileDir, dbRdr dbr, LogWriter log, String xmlFname) throws Exception
 {
   this.setIgnoreCaseBool(true);
   init(fileDir, dbr, log, xmlFname);
 }

 public repSect getReport(String name)
 {
   return (repSect)get(name);
 }

 public static void main(String[] args) throws Exception
 {
  try
  {
   reportSel rep = new reportSel("D:/TempXXX", null, null);
  }
  catch(Exception e)
  {
    ParamException pe=new ParamException("REPDESC.XML");
    //debug("WARNING: "+pe.getMessage());
  }
 }

 public void printReportByName(String name, HttpServletRequest req, JspWriter out) throws Exception
 {
   this.printReportByName(name, req, out, null);
 }

 public void printReportByName(String name, HttpServletRequest req, JspWriter out, String firstCol) throws Exception
 {
   slctLst l=get(name);
   l.printAttrsReport(firstCol, req, out, null, null, null, null, null);
 }
}