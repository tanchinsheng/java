<%@ page import = "java.util.*, java.net.*, tpms.*"
         isErrorPage = "false"
         errorPage = "uncaughtErr.jsp"%>
<%  
  String time = new Long(System.currentTimeMillis()).toString();
  String outPage=(String)request.getAttribute("outPage"); 
  String actionTxt=(String)request.getAttribute("actionTxt");
  String refreshTime=(String)request.getAttribute("refreshTime");
%>

<%
  String qryStr="";
  for (Enumeration e=request.getAttributeNames(); e.hasMoreElements();)
  {       
    String attrnam=(String)e.nextElement();
    String attrval=(String)request.getAttribute(attrnam);
    if (attrnam.equals("outPage")) continue;
    if (attrnam.equals("nextPage")) continue;
    if ((attrnam.startsWith("lsFile"))&&(!attrnam.equals("lsFile"))) continue; /* id=20-OCT-2003 */
    if ((attrnam.startsWith("tpFile"))&&(!attrnam.equals("tpFile"))) continue; /* id=20-OCT-2003 */
    qryStr=qryStr+"&"+attrnam+"="+URLEncoder.encode(attrval, "UTF-8");
  }
%>

<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <link rel="stylesheet" type="text/css" href="default.css"/>
 <SCRIPT language=JavaScript>
  function forwarda() 
  {
    location.replace("<%= outPage+"?t="+time+qryStr %>");
  } 
 </SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" onLoad="forwarda()">
</BODY>
</HTML>


