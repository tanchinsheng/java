<%@ page import="java.util.*" 
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>
<%
 String mode=(String)session.getAttribute("workMode");
 String modeDesc="";
 if (mode.equals("SENDWORK")) modeDesc="SEND WORK MODE";
 if (mode.equals("RECWORK")) modeDesc="RECEIVE WORK MODE";
 if (mode.equals("LOCREP")) modeDesc="CHECK MODE";
 if (mode.equals("GLOBREP")) modeDesc="GLOBAL REPORT MODE";
%>
<%String contextPath = request.getContextPath();%>
<HTML><HEAD><TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type=text/css rel=stylesheet>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TPMS OPERATIVE MODE SWITCH PAGE"; %>
<%@ include file="top.jsp" %>
<% 
%>
<%
 String msgType   = "OK";
 String actionTxt = "TPMS OPERATIVE MODE SUCCESSFULLY SWITCHED";
 String msgTxt    = "CURRENT MODE IS '"+modeDesc+"'";
 String detailTxt = "";
 String sysDetailTxt = "";
 boolean backBut  = false;
%>
<%@ include file="msg.jsp" %>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
