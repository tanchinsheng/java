package tol;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class dRngTxtLst extends rngTxtLst
{
 static final int _defSize = 10;

 public dRngTxtLst(String name, String label, String p, String p2, dbRdr d, String t, String f, String butName, boolean always)
 {
  super(name, label, 'D', p, p2, d, t, f, "", "", butName, 0, _defSize);
  alwaysActive = always; //ALWAYS ACTIVE FLAG
  if (alwaysActive) blankField=dtrd.getCurDate();
  else blankField=new String();
  blankField2=blankField;
  v.clear();  v.addElement(blankField);
  v2.clear(); v2.addElement(blankField2);
 }


public String getInitVal()
{
  if (initVal==null) return null;
  else return treatToday(initVal);
}

public String getInitVal2()
{
  if (initVal2==null) return null;
  else return treatToday(initVal2);
}

String treatToday(String val)
{
  if (val.equals("TODAY")) return dtrd.getCurDate();
  else if ((val.startsWith("TODAY"))&&(val.charAt(5)=='-')&&(val.length()>6))
       {
         String num=val.substring(6,val.length());
         int offset;
         try{offset=(0-Integer.valueOf(num).intValue());}
         catch(NumberFormatException e) {offset=0;}
         debug(offset+" "+num+" "+dtrd.addDays(dtrd.getCurDate(),0));
         return dtrd.addDays(dtrd.getCurDate(),offset);
       }
       else return null;
}

 //MUSTN'T BE SET MANDATORY IF IT IS ALREADY ALWAYS ACTIVE
 //IF IT IS NOT ALREADY ACTIVE, IT'S NO SENSE TO DEFINE IT AS
 //MANDATORY
 public void setMand() {}

/*
 public void getYears_HTML(int index, JspWriter out, int offset) throws Exception
 {
  String s = (index==1 ? (String)v.elementAt(0) : (String)v2.elementAt(0));
  Integer year = dtrd.getYear(s);
  for (int i=year.intValue()-1; i<=year.intValue()+1; i++)
  {

    if (i==year.intValue())
        out.println("<option selected>"+year.toString());
    else
        out.println("<option>"+new Integer(i).toString());
  }
 }

 public void getMonths_HTML(int index, JspWriter out) throws Exception
 {
  String s = (index==1 ? (String)v.elementAt(0) : (String)v2.elementAt(0));
  Integer month = dtrd.getMonth(s);

    for (int i=1; i<=12; i++)
    {
      if (i==month.intValue())
          out.println("<option selected>"+month.toString());
      else
          out.println("<option>"+new Integer(i).toString());
    }
 }

 public void getDays_HTML(int index, JspWriter out) throws Exception
 {
  String s = (index==1 ? (String)v.elementAt(0) : (String)v2.elementAt(0));
  Integer day = dtrd.getDay(s);
  for (int i=1; i<=31; i++)
  {
    int d = (day.intValue()<=15 ? i : (31-i)+1);
    if (d==day.intValue())
        out.println("<option selected>"+day.toString());
    else
        out.println("<option>"+new Integer(d).toString());
  }
 }
*/

 public void fetchGUI(HttpServletRequest req) throws Exception
 {
   if (req.getParameterValues("index")==null) return;

   String  indexStr = (req.getParameterValues("index"))[0];
   Integer indexPar = Integer.valueOf(indexStr);
   if (indexPar.intValue()!=index) return;

   if ((req.getParameterValues("date1")==null)&&
       (req.getParameterValues("date2")==null))
   {
     //I've clicked on 'date' into the left panel
     v=new Vector(); v2=new Vector();
     v.addElement(blankField);
     v2.addElement(blankField);
   }

   if (req.getParameterValues("date1")!=null)
   {
     String date1 = (req.getParameterValues("date1"))[0];
     date1 = check(date1);
     v  = new Vector(); v.addElement(date1);
   }

   if (req.getParameterValues("date2")!=null)
   {
     String date2   = (req.getParameterValues("date2"))[0];
     date2 = check2(date2);
     v2 = new Vector(); v2.addElement(date2);
   }
 }

 public void toHtmlGUI(HttpServletRequest req, JspWriter out) throws Exception
 {
  if ((alwaysActive)||(!isBlank()))
  {
    out.print("(");
    out.print((String)v.elementAt(0));
    out.print(", ");
    out.print((String)v2.elementAt(0));
    out.print(")");
    out.println("<BR>");
  }
 }

}//class
