<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%
  String repType=request.getParameterValues("repType")[0];
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <% if (repType.equals("HTML")) {%><LINK href="default_print.css" type=text/css rel=stylesheet><%}%>
</HEAD>
<BODY bgColor="#FFFFFF">
<%
  if (repType.equals("HTML"))
  {%>
    <TABLE BORDER="0"><TR><TD ALIGN="CENTER">
    <IMG SRC="img/st-rep-logo.gif" BORDER="0">
    <FONT class="titverdibig">
      <b>TPMS HELP</b><BR><BR>
       TPMS/W host=<%= getServletContext().getInitParameter("TpmsInstName") %> &nbsp; User=<%= session.getValue("user") %><BR><BR>
       <%= tol.dateRd.getCurDateTime() %>
    </FONT>
    </TD></TR>
    <TR><TD colspan="2"><IMG SRC="img/pix.gif" height="8"></TD></TR>
    </TABLE>
  <%}

%>

</BODY>
</HTML>