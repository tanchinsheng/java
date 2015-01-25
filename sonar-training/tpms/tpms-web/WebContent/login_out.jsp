<%@ page import="java.util.*" 
    isErrorPage="false"%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="LOGIN PAGE"; %>
<%@ include file="top.jsp" %>
<%
 String msgType   = "OK";
 String actionTxt = "HI "+((String)session.getAttribute("user")).toUpperCase();
 String msgTxt    = "YOU HAVE SUCCESSFULLY LOGGED IN";
 String detailTxt = "";
 String sysDetailTxt = "";
 boolean backBut  = false;
%>

<%@ include file="msg.jsp" %>


<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
