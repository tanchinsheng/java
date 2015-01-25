<!-- author: TXT e-solutions s.p.a (Daniele Colecchia) -->
<%@ page isErrorPage="true" import="tol.*, tpms.*" %>
<% Exception exc=(Exception)exception; %>

<%
 String msgType   = "ERROR";
 String actionTxt = "";
 String msgTxt    = "";
 String detailTxt = "";
 String sysDetailTxt = "";
 boolean backBut  = true;
%>
<%String contextPath = request.getContextPath();%>

<%@ include file="err.jsp" %>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TPMS ERROR PAGE"; %>
<%@ include file="top.jsp" %>

<%@ include file="msg.jsp" %>

<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
