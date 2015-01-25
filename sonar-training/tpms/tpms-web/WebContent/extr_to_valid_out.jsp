<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
    isErrorPage="false"
    errorPage="uncaughtErr.jsp" 
%>
<%  
  String time = new Long(System.currentTimeMillis()).toString();
  String reqId=request.getParameterValues("reqId")[0];
  String repFileName=request.getParameterValues("repFileName")[0];
  String actionTxt=(String)request.getParameterValues("actionTxt")[0];
  String refreshTime=(String)request.getParameterValues("refreshTime")[0];
  boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
  String webAppDir=getServletContext().getInitParameter("webAppDir");
  if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function start()
  {
   <% 
    if (startBool)
    {%>
       var delay = setTimeout("location.reload()",<%= refreshTime %>*1000);
    <%}
   %>
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP EXTRACTION TO VALIDATE "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>
<%
 if (startBool)
 {%>
    <%@ include file="waitmsg.jsp" %>
 <%}
 else 
 {%>
   <%
    String msgType   = "OK";
    actionTxt        = "TP ACTION COMPLETED";
    String msgTxt    = "TP FILES SUCCESSFULLY EXTRACTED";
    String detailTxt = "";
    String sysDetailTxt = "";
    boolean backBut  = false;
   %>
   <%@ include file="msg.jsp" %>
 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
