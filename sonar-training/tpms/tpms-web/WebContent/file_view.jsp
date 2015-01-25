<!-- saved from url=(0022)http://internet.e-mail -->
<%@ page import="java.util.*, java.io.*"%>

<HTML>
<HEAD>
 <TITLE>TPMS</TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
</HEAD>
<BODY bgColor="#FFFFFF">
<%
 String contextPath = request.getContextPath();
 String selDir=(String)request.getAttribute("curDirPath");
 String selFile=(String)request.getAttribute("selFile");
 DataInputStream fin=new DataInputStream(new FileInputStream(selDir+"/"+selFile));
 boolean repBool=false; boolean csvRepBool=false; String pageTitle="FILE VIEWER </i><FONT FACE=\"Courier\">(path="+selDir+")</FONT><i>";
%>

<%@ include file="top_large.jsp" %>

<TABLE cellSpacing=0 cellPadding=0 width=934 border=0>
       <TR><TD colspan="2"><IMG SRC="<%=contextPath%>/img/pix.gif" height="12"></TD></TR>
       <TR>
        <TD><IMG SRC="<%=contextPath%>/img/blank.gif" width="12"></TD>
        <TD ALIGN="LEFT">
	      <BR>
              <table cellpadding="0" cellspacing="0" border="0" width="924" >
                <tr>
                  <td width="4"><img src="<%=contextPath%>/img/tit_sx_file.gif" width="4" height="21" alt="" border="0"></td>
                  <td background="<%=contextPath%>/img/tit_center_file.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->FILE "</b><font color="#19559E" face="Courier"><%= selFile %></FONT><b>"</b></td>
                  <td width="4"><img src="<%=contextPath%>/img/tit_dx_file.gif" width="4" height="21" alt="" border="0"></td>
                  <td><img src="<%=contextPath%>/img/pix.gif" width="<%= 830-((selFile.length()+6)*8) %>" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="<%=contextPath%>/img/pix_grey.gif" width="924" height="1" alt="" border="0"></td>
                </tr>
              </table>
        </TD>
       </TR>
       <TR>
        <TD><IMG SRC="<%=contextPath%>/img/blank.gif" width="12"></TD>
        <TD ALIGN="LEFT">
<BR>
<PRE>
<%
 String s="";
 while ((s=fin.readLine())!=null)
 {
   StringTokenizer toks=new StringTokenizer(s,"<>'&",true);
   while(toks.hasMoreTokens())
   {
     String tok=toks.nextToken();
     out.print(" ");
     if (tok.equals("<")) out.print("&lt;");
     else if (tok.equals(">")) out.print("&gt;");
     else if (tok.equals("'")) out.print("&quot;");
     else if (tok.equals("&")) out.print("&amp;");
     else out.print(tok);
   }
   out.println();
 }
 fin.close();
%>
</PRE>

        </TD>
       </TR>
</TABLE>


<% boolean closeButtBool=false; %>
<%@ include file="bottom_large.jsp" %>

</BODY>
</HTML>
