package tol;

import java.util.*;
import java.text.*;


public class dateRd
{
 public static final String _separators   = " -/\\.:";
 public static final String _dflt_dateSep = "/";
 String _dateSep;
 StringTokenizer st; 
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public dateRd()
 {
  this._dateSep = _dflt_dateSep;
 }


 public dateRd(String _dateSep)
 {
  this._dateSep = _dateSep;
 }


 public String getCurDate()
 {
  GregorianCalendar cal = new GregorianCalendar();
  return getString(cal);
 }

 public String getCurDate(boolean extendedFormat)
 {
  GregorianCalendar cal = new GregorianCalendar();
  if (!extendedFormat) return getString(cal, false);
  else return getString(cal, true);
 }

 public String getDate(String s) throws NumberFormatException
 { return addDays(s, 0); }



 public String addDays(String s, int n) throws NumberFormatException
 {
  GregorianCalendar cal = getCal(s);
  cal.add(Calendar.DATE, n);
  return getString(cal);
 }


 protected String getString(GregorianCalendar cal)
 {
  Integer date  = new Integer(cal.get(Calendar.DATE));
  Integer month = new Integer(cal.get(Calendar.MONTH)+1);
  Integer year  = new Integer(cal.get(Calendar.YEAR));
  String dStr="0", mStr="0", yStr, str;

  if (date.toString().length() == 2) dStr = date.toString();
  else dStr = dStr.concat(date.toString());
  /*
  if (month.toString().length() == 2) mStr = month.toString();
  else mStr = mStr.concat(month.toString());
  */
  mStr = getMonthLabel(month.intValue()).toUpperCase();
  yStr = year.toString();
  str  = dStr; str = str.concat(_dateSep);
  str  = str.concat(mStr); str = str.concat(_dateSep);
  str  = str.concat(yStr);
  return str;
 }

 protected String getString(GregorianCalendar cal, boolean extendedFormat)
 {
  if (!extendedFormat) return getString(cal);
  else
  {
   Integer date  = new Integer(cal.get(Calendar.DATE));
   Integer month = new Integer(cal.get(Calendar.MONTH)+1);
   Integer year  = new Integer(cal.get(Calendar.YEAR));
   return new String(date.toString()+"-"+this.getMonthLabel(month.toString())+"-"+year.toString());
  }
 }

 public String getRev(String s)
 {
  String str[] = new String[3];
  String sOut;
  st= new StringTokenizer(s,_dateSep,false);
  int i=0;
  while (st.hasMoreTokens()) 
        {
         str[i] = new String( st.nextToken() );
         i++; 
        }
  sOut = str[2]; sOut = sOut.concat(_dateSep);
  sOut = sOut.concat(str[1]); sOut = sOut.concat(_dateSep);
  sOut = sOut.concat(str[0]);
  return sOut;
  }

 public GregorianCalendar getCal(String s) throws NumberFormatException
 {
  String str; Integer n; 
  int fields[] = new int[3];
  int calFields[] = new int[3];
  st= new StringTokenizer(s,_dateSep,false);
  GregorianCalendar cal;
  int i=0;
  try {
   while (st.hasMoreTokens())
          {
                str = new String( st.nextToken() );
                debug(str+">");
                if ((i==1)&&(getMonthNum(str)>=1)) //month label case
                {
                  fields[i] = getMonthNum(str);
                }
                else
                {
                  n = Integer.valueOf(str.trim());
                  fields[i] = n.intValue();
                }
                i++;
          }
         }
  catch (NumberFormatException e)
   { throw new NumberFormatException(""); }
  //NB: Calendar counts Months starting from 0
  fields[1] = fields[1] - 1;
  cal = new GregorianCalendar(fields[2], fields[1], fields[0]);
  calFields[0]=cal.get(Calendar.DATE);
  calFields[1]=cal.get(Calendar.MONTH);
  calFields[2]=cal.get(Calendar.YEAR);
  for (i=0;i<3;i++)
  {
   if (fields[i]!=calFields[i])
        throw new NumberFormatException("");
  }
  return cal;
 }

 public Integer getYear(String date) throws NumberFormatException
 //date is of type: dd/mm/yyyy
 {
  st= new StringTokenizer(date,_dateSep,false);
  Integer.valueOf(st.nextToken());
  Integer.valueOf(st.nextToken());
  Integer ret = Integer.valueOf(st.nextToken());
  return ret;
 }

 public Integer getMonth(String date) throws NumberFormatException
//date is of type: dd/mm/yyyy
 {
  st= new StringTokenizer(date,_dateSep,false);
  Integer.valueOf(st.nextToken());
  Integer ret = Integer.valueOf(st.nextToken());
  return ret;
 }

 public Integer getDay(String date) throws NumberFormatException
//date is of type: dd/mm/yyyy
 {
  st= new StringTokenizer(date,_dateSep,false);
  Integer ret = Integer.valueOf(st.nextToken());
  return ret;
 }

 public String getDate(String d, String m, String y)
 {
   return new String(d+_dateSep+m+_dateSep+y);
 }

 public static String getMonthLabel(String index)
 {
  if (getMonthNum(index)>=1) return index.toLowerCase();
  Number month;
  try{month=Integer.valueOf(index);}
  catch(NumberFormatException e) {return null;}
  return getMonthLabel(month.intValue());
 }

 public static String getMonthLabel(int month)
 {
   switch(month)
   {
     case 1  : return "jan";
     case 2  : return "feb";
     case 3  : return "mar";
     case 4  : return "apr";
     case 5  : return "may";
     case 6  : return "jun";
     case 7  : return "jul";
     case 8  : return "aug";
     case 9  : return "sep";
     case 10 : return "oct";
     case 11 : return "nov";
     case 12 : return "dec";
     default: return null;
   }
 }

 public static int getMonthNum(String month)
 {
     if (month.toLowerCase().equals("jan")) return 1;
     if (month.toLowerCase().equals("feb")) return 2;
     if (month.toLowerCase().equals("mar")) return 3;
     if (month.toLowerCase().equals("apr")) return 4;
     if (month.toLowerCase().equals("may")) return 5;
     if (month.toLowerCase().equals("jun")) return 6;
     if (month.toLowerCase().equals("jul")) return 7;
     if (month.toLowerCase().equals("aug")) return 8;
     if (month.toLowerCase().equals("sep")) return 9;
     if (month.toLowerCase().equals("oct")) return 10;
     if (month.toLowerCase().equals("nov")) return 11;
     if (month.toLowerCase().equals("dec")) return 12;
     return -1;
 }

 public static String getCurDateTime()
 {
   DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
   return df.format(new Date());
 }
 public static void main(String args[])
 {
  dateRd d=new dateRd(); System.out.println(dateRd.getCurDateTime());
  //debug(d.getCurDate(false));
  //debug(d.addDays("27/09/2002",2));
 }
}
