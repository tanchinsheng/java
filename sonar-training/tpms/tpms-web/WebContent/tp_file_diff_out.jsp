<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
    isErrorPage="false"
    errorPage="uncaughtErr_large.jsp" 
%>
<%  
  String reqId=request.getParameterValues("reqId")[0];
  String repFileName=request.getParameterValues("repFileName")[0];
  String compareRepFileName=request.getParameterValues("compareRepFileName")[0];
  String actionTxt=(String)request.getParameterValues("actionTxt")[0];
  String refreshTime=(String)request.getParameterValues("refreshTime")[0];
  boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
  String webAppDir=getServletContext().getInitParameter("webAppDir");
  String xslFileName=webAppDir+"/xsl/"+"tp_compare_rep.xsl";
  if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
%>

<%
 String selFile=(String)request.getParameterValues("tpFile")[0];
 boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP FILE DIFFERENCE VIEWER";
%>
<HTML>
<HEAD>
 <TITLE>TPMS</TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function start()
  {
   <% 
    if (startBool)
    {%>
       var delay = setTimeout("location.reload()",<%= refreshTime %>*1000);
    <%}
   %>
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">

<%@ include file="top_large.jsp" %>

<%
 if (startBool)
 {%>
  <TABLE cellSpacing=0 cellPadding=0 width=934 border=0>
  <TBODY>
       <TR><TD colspan="2"><IMG SRC="img/pix.gif" height="12"></TD></TR>
       <TR>
        <TD><IMG SRC="img/blank.gif" width="12"></TD>
        <TD ALIGN="LEFT">
	 <BR>
         <TABLE cellSpacing=0 cellPadding=0 border=0>
          <TR>	 	
           <%@ include file="waitmsg.jsp" %>
          </TR>
	 </TABLE> 
        </TD>
       </TR>
  </TBODY>
  </TABLE>
 <%}
 else 
 {%>
  <TABLE cellSpacing=0 cellPadding=0 width=934 border=0>
  <TBODY>
       <TR><TD colspan="2"><IMG SRC="img/pix.gif" height="12"></TD></TR>
       <TR>
        <TD><IMG SRC="img/blank.gif" width="12"></TD>
        <TD ALIGN="LEFT">
          <BR>
          <TABLE cellpadding="0" cellspacing="0" border="0"> 	
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="924" >
                <tr>
                  <td width="4"><img src="img/tit_sx_file.gif" width="4" height="21" alt="" border="0"></td>
                  <td background="img/tit_center_file.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->COMPARED TESTING PROGRAMS</b></td>
                  <td width="4"><img src="img/tit_dx_file.gif" width="4" height="21" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="640" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="924" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix.gif" height="6" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%                
                xmlRdr.transform(compareRepFileName, xslFileName, null, out);
               %>
	       <!-- /REPORT -->
              </TD>	
             </TR>
          </TABLE>
        </TD>
       </TR>
  </TBODY>
  </TABLE>

  <TABLE cellSpacing=0 cellPadding=0 width=934 border=0>
  <TBODY>
       <TR>
        <TR><TD colspan="2"><IMG SRC="img/pix.gif" height="9"></TD></TR>
        <TD><IMG SRC="img/blank.gif" width="12"></TD>
        <TD ALIGN="LEFT">
         <BR>	
              <table cellpadding="0" cellspacing="0" border="0" width="924" >
                <tr>
                  <td width="4"><img src="img/tit_sx_file.gif" width="4" height="21" alt="" border="0"></td>
                  <td background="img/tit_center_file.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->FILE "</b><font color="#19559E" face="Courier"><%= selFile %></b><b>" DIFFERENCES</b></FONT></td>
                  <td width="4"><img src="img/tit_dx_file.gif" width="4" height="21" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="<%= 830-((selFile.length()+16)*8) %>" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="924" height="1" alt="" border="0"></td>
                </tr>
              </table>
        </TD>
       </TR>
       <TR>
        <TD><IMG SRC="img/blank.gif" width="12"></TD>
        <TD ALIGN="LEFT">
<BR>
<PRE>
<% 
 String s="";
 DataInputStream fi=new DataInputStream(new FileInputStream(repFileName));
 while ((s=fi.readLine())!=null)
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
 fi.close();
%>
</PRE>

        </TD>
       </TR>	     
  </TBODY>
  </TABLE>
 <%}
%>

<% boolean closeButtBool=(startBool ? false : false); %> 
<%@ include file="bottom_large.jsp" %>

</BODY>
</HTML>
