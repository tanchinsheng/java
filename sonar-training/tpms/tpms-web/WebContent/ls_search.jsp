<%@ page import="java.util.*, tpms.*, tol.reportSel, tol.slctLst" 
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%
   String qryType=request.getParameterValues("qryType")[0];
   String repTitle=DbQryServlet.getReportTitle(qryType);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <%@ include file="ls_search_form_checks.jsp" %>
</HEAD>
<BODY bgColor="#FFFFFF">
<% 
  boolean repBool=false; boolean csvRepBool=false; String pageTitle=repTitle; 
%>
<%@ include file="top.jsp" %>

<% boolean news=true; %>
<%@ include file="ls_search_form.jsp" %>


<%@ include file="bottom.jsp" %>
</BODY>
</HTML>