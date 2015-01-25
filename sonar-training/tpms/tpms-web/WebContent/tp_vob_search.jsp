<%@ page import="java.util.*, tpms.*" 
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>
<%
   String qryType=request.getParameterValues("qryType")[0];
   String repTitle=VobQryServlet.getReportTitle(qryType);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <%@ include file="tp_search_form_checks.jsp" %> 
</HEAD>
<BODY bgColor="#FFFFFF">
<% 
  boolean repBool=false; boolean csvRepBool=false; String pageTitle=repTitle; 
%>
<%@ include file="top.jsp" %>

<% boolean news=true; %>
<% String repFileName=null; %>
<%@ include file="vob_tp_search_form.jsp" %>

<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
