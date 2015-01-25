<!-- author: TXT e-solutions s.p.a (Daniele Colecchia) -->
<%@ page isErrorPage="true" import="tol.*, tpms.*" %>
<% Exception exc=(Exception)exception; %>

	    <%
	     String msgType   = "ERROR";
 	     String actionTxt = "";
 	     String msgTxt    = "";
 	     String detailTxt = "";
 	     String sysDetailTxt = "";
 	     boolean backBut  = false;
   	    %>	 	

<%@ include file="err.jsp" %>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TPMS ERROR PAGE"; %>
<%@ include file="top_large.jsp" %>

 <TABLE cellSpacing=0 cellPadding=0 width=934 border=0>
  <TBODY>
       <TR><TD colspan="2"><IMG SRC="img/pix.gif" height="12"></TD></TR>
       <TR>
        <TD><IMG SRC="img/blank.gif" width="12"></TD>
        <TD ALIGN="LEFT">
	 <BR>
         <TABLE cellSpacing=0 cellPadding=0 border=0>
          <TR>
	    <%@ include file="msg.jsp" %>
          </TR>
	 </TABLE> 
        </TD>
       </TR>
  </TBODY>
  </TABLE>

<% boolean closeButtBool=false; %> 
<%@ include file="bottom_large.jsp" %>
</BODY>
</HTML>
