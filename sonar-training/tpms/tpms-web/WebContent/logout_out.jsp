<%@ page import="java.util.*" 
    isErrorPage="false"
    errorPage="uncaughtErr.jsp" 
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="LOGIN PAGE"; %>
<%@ include file="top.jsp" %>
<%
 String msgType   = "OK";
 String actionTxt = "BYE";
 String msgTxt    = "YOU HAVE SUCCESSFULLY LOGGED OUT";
 String detailTxt = "";
 String sysDetailTxt = "";
 boolean backBut  = false;
%>
<%@ include file="msg.jsp" %>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
