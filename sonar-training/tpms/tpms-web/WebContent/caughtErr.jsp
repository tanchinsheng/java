<!-- author: TXT e-solutions s.p.a (Daniele Colecchia) -->
<%@ page isErrorPage="true" import="java.io.*, tol.*, tpms.*" %>
<% Exception exc=(Exception)request.getAttribute("exception"); %>

<%
 String msgType   = "ERROR";
 String actionTxt = "";
 String msgTxt    = "";
 String detailTxt = "";
 String sysDetailTxt = "";
 boolean backBut  = true;
    Boolean myBackButton = (Boolean)request.getAttribute("errorBackButton");
    if (myBackButton != null && !myBackButton.booleanValue()){
        backBut= false;
    }
%>
<%String contextPath = request.getContextPath();%>
<%@ include file="err.jsp" %>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TPMS CONTROL ERROR PAGE"; %>
<%@ include file="top.jsp" %>


<%@ include file="msg.jsp" %>

<%@ include file="bottom.jsp" %>

</BODY>
</HTML>


