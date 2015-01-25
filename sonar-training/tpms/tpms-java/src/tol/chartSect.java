package tol;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import org.w3c.dom.*;

public class chartSect extends repSect
{
 Element stchart;
 LogWriter log=null;
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public void setLogWriter(LogWriter log) {this.log=log;}

 public chartSect(String name, String label, dbRdr d, String t, String bt, Element stchart)
 {
  dbr = d;
  this.name      = name;
  this.label     = label;
  this.tableName = t;
  selButName = bt;
  index = getIndex(); //called after having set selButName
  v=new Vector();
  this.stchart=stchart;
  initStChart();
 }

 private void initStChart()
 {
   debug("stchart-init");//debug
   this.setDataRefID("STLABELCOLUMN");
   this.setDataRefID("STXCOLUMN");
   this.setDataRefID("STYCOLUMN");
 }

 private void setDataRefID(String tag)
 {
   NodeList nl=stchart.getElementsByTagName(tag);
   for (int i=0; i<nl.getLength(); i++)
   {
     Element el=(Element)nl.item(i);
     if (el.getAttribute("DATAREFID").equals("")) el.setAttribute("DATAREFID","datasel");
   }
 }

 public Element getChartDef() {return stchart;}

 public void fetch(HttpServletRequest req) throws Exception
 {
   this.fetchDependent(true, req);
 }
}
