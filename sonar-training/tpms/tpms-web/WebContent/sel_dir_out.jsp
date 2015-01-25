<%@ page import="java.util.*" 
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>
<%String contextPath = request.getContextPath();%>
<HTML><HEAD><TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type=text/css rel=stylesheet>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="WORK DIRECTORY SELECTION"; %>
<%@ include file="top.jsp" %>
<% 
 String selFile=(String)request.getAttribute("curDirPath");
%>
<%
 String msgType   = "OK";
 String actionTxt = selFile;
 String msgTxt    = "DIRECTORY SUCCESSFULLY SELECTED";
 String detailTxt = "";
 String sysDetailTxt = "";
 boolean backBut  = false;
%>
<%@ include file="msg.jsp" %>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
