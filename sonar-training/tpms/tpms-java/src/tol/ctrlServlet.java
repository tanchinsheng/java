package tol;

import tol.util.WebLogger;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.*;

public class ctrlServlet extends HttpServlet
{
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}
 
 public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
 {
   String usrName=getServletContext().getInitParameter("inst_usrName");
   String plant  =getServletContext().getInitParameter("inst_plantName");
   String stGroup=getServletContext().getInitParameter("inst_stGroup");
   String srcStr =new String(usrName+plant+stGroup);
   String actKey =request.getParameterValues("actKey")[0];
   try
   {
     isActKeyValid(request, srcStr, actKey, true);
     String nextPage=(request.getParameterValues("nextPage"))[0];
     RequestDispatcher rDsp = getServletContext().getRequestDispatcher(nextPage);
     rDsp.forward(request,response);
   }
   catch(Exception e)
   {
     RequestDispatcher rDsp = getServletContext().getRequestDispatcher(tolServlet._caughtErrPage);
     rDsp.forward(request,response);
   }
 }

 public static boolean isActKeyValid(HttpServletRequest request, String srcStr, String actKey, boolean setValidFlag)
 {
    boolean ret=false;
    HttpSession session=request.getSession();
    if (setValidFlag)
    {
      if (isKeyValid(srcStr, actKey))
      {
        ret=true;
      }
      session.setAttribute("AdminPermissionsBool", new Boolean(ret));
    }
    else
    {
      if (session.getAttribute("AdminPermissionsBool")!=null)
      {
        ret=((Boolean)session.getAttribute("AdminPermissionsBool")).booleanValue();
      }
    }
    return ret;
 }

 private static boolean isKeyValid(String src, String key)
 {
    return (src.length()<6 ? false : getKey(src).equals(key));
 }

 private static String getKey(String src)
 {
    int keyLen=9;
    int groupLen=3;
    StringBuffer sb=new StringBuffer();
    int m=src.length()/keyLen;
    int r=src.length()%keyLen;

    for (int j=0; j<keyLen; j++)
    {
      int n=0;
      for (int i=0; i<m; i++)
      {
        n=n+(int)src.charAt(i*keyLen+j);
      }
      if (j<r) n=n+(int)src.charAt(m*keyLen+j);
      n=n%13+(n+j)%3;
      n=(n<10 ? n+((int)'0') : n-10+((int)'A'));
      sb.append((char)n);
      if (((j+1)%groupLen==0)&&(j<keyLen-1)) sb.append('-');
    }
    String retStr=sb.toString();
    return retStr;
 }
}