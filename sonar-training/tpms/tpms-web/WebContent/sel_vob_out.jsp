<%@ page import="java.util.*" 
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>
<%String contextPath = request.getContextPath();%>
<HTML><HEAD><TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type=text/css rel=stylesheet>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="VOB SELECTION PAGE"; %>
<%@ include file="top.jsp" %>
<%
 String vobDesc=(String)session.getAttribute("vobDesc");   
%>
<%
 String msgType   = "OK";
 String actionTxt = (vobDesc == null? "" : vobDesc);
 String msgTxt    = "VOB SUCCESSFULLY SELECTED";
 String detailTxt = "";
 String sysDetailTxt = "";
 boolean backBut  = false;
%>
<%@ include file="msg.jsp" %>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
