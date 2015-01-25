<%@ page import="java.util.*" 
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>
<%String contextPath = request.getContextPath();%>
<HTML><HEAD><TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type=text/css rel=stylesheet>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="MAIN FORM PAGE"; %>
<%@ include file="top.jsp" %>
<tr></td>
<FORM action="navDirServlet" method="post">
 <INPUT TYPE="HIDDEN" name="mextPage" value="nav_dir_out.jsp">
 <INPUT TYPE="HIDDEN" name="curDirPath">
 <INPUT TYPE="HIDDEN" name="nextDirPath" value="<%= session.getValue("homeDirPath") %>">
 <input type="submit" value="Submit">
</FORM>
</td></tr>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
