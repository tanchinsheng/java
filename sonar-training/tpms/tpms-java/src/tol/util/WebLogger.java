package tol.util;

import javax.servlet.http.*;
import java.util.*;
import tol.*;
import java.io.*;

public class WebLogger
{

  public WebLogger()
  {
  }

  public void printRequest(HttpServletRequest req, String fileName) throws Exception
  {
  String dsep="/";
  String sep=":";
  String tsep=":";
  String fieldsep=" ";
  Calendar cal=new GregorianCalendar();
  String DAY_OF_MONTH = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
  String MONTH = dateRd.getMonthLabel(cal.get(Calendar.MONTH)+1);
  String YEAR = new Integer(cal.get(Calendar.YEAR)).toString();
  String HOUR_OF_DAY = new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString();
  String MINUTE = new Integer(cal.get(Calendar.MINUTE)).toString();
  String SECOND = new Integer(cal.get(Calendar.SECOND)).toString();

  DAY_OF_MONTH = formatString(DAY_OF_MONTH, 2, "0");
  MONTH        = formatString(MONTH, 2, "0");
  HOUR_OF_DAY  = formatString(HOUR_OF_DAY, 2, "0");
  MINUTE       = formatString(MINUTE, 2, "0");
  SECOND       = formatString(SECOND, 2, "0");

  String line = "[";
  line=line.concat(DAY_OF_MONTH).concat(dsep).concat
                  (MONTH).concat(dsep).concat
                  (YEAR).concat(sep).concat
                  (HOUR_OF_DAY).concat(tsep).concat
                  (MINUTE).concat(tsep).concat
                  (SECOND).concat
                  (" +0000]");

  String RequestURI  = req.getRequestURI();
  String appBut[]    = req.getParameterValues("appBut");
  String QueryString = req.getQueryString();
  String RemoteHost  = req.getRemoteHost();
  String RemoteAddr  = req.getRemoteAddr();

  if (RequestURI==null)  RequestURI="-";
  if (QueryString==null) QueryString="-";
  else
  {
  String str="?";
  QueryString = str.concat(QueryString);
  }
  if (RemoteHost==null)  RemoteHost="-";
  if (RemoteAddr==null)  RemoteAddr="-";


  line=line.concat(fieldsep).concat(RequestURI).concat(fieldsep).concat
                                   (appBut==null ? "-" : appBut[0]).concat(fieldsep).concat
                                   ("-").concat(fieldsep).concat
                                   (RemoteHost).concat(fieldsep).concat
                                   (RemoteAddr);

  //STAMPO I NOMI E I VALORI DEI PARAMETRI HTTP
  String parameters = getHttpParameters(req);
  String replaceString = "%20";
  parameters=WebLogger.replaceString(parameters, " ", replaceString);
  line=line.concat(fieldsep).concat(parameters);
  printLine(line, fileName);
  }

  private void printLine(String content, String fileName) throws Exception
  {
    //Costruisco il DataOutputStream in append mode
    DataOutputStream output = new DataOutputStream(new FileOutputStream(fileName, true));
    output.writeBytes(content.concat("\n"));
    output.close();
  }

  //Metodo che serve a formattare la stringa "numerica" str  aggiungendo 'n-str.lenght()'
  //caratteri 'c' davanti.....
  private static String formatString(String str, int n, String c) throws Exception
  {
    int len=str.length();
    if ( (len<n) & (c.length()==1) )
    {
      for (int i=0; i<(n-len); i++)
      {
        str=c.concat(str);
      }
    }
    return str;
  }

  public static String replaceString(String str, String oldString, String newString) throws Exception
  {
    String before="", after="";
    int fromIndex=0;
    int i=0;
    for (i=0; i<str.length(); i++)
    {
      i=str.indexOf(oldString, fromIndex);
      if (i>=0)
      {
        before=str.substring(0, i);
        after=str.substring(i+oldString.length());
        str=before.concat(newString).concat(after);
        fromIndex=i+newString.length();
      }
      else i=str.length();
    }
    return str;
  }

  private static String getHttpParameters(HttpServletRequest req) throws Exception
  {
    String parametro="";
    String valore="";
    String parameters="?";
    Enumeration e = req.getParameterNames();
    for ( ; e.hasMoreElements() ;)
    {
      parametro=(String)e.nextElement();
      valore=req.getParameterValues(parametro)[0];
      parameters=parameters.concat(parametro).concat("=").concat(valore);
      if (e.hasMoreElements())
        parameters=parameters.concat("&");
    }
    return parameters;
  }
}
