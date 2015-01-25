<%@ page import="java.util.Properties"%>
<%@ page import="tol.xmlRdr"%>
<%@ page import="tpms.VobManager"%>
<%
 String currentUserLogin = (String) session.getAttribute("user");
 Properties props = new Properties();

 %>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<SCRIPT LANGUAGE="JavaScript">
  function submitForm()
  {
    document.selVobForm.submit();
  }
 </SCRIPT>

</HEAD>
<LINK href="default.css" type=text/css rel=stylesheet>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="SELECT WORK DIRECTORY"; %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560">
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;&nbsp;LOCAL&nbsp; VOBS&nbsp;&nbsp;</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="403" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

	    <FORM name="selVobForm" action="selectVobServlet" method="post">
             <TR>
              <TD ALIGN="LEFT" >


<!-- REPORT -->
    <%
       if (UserUtils.hasAfsRole(currentUserLogin)){
         props.setProperty("userRole",(String)session.getAttribute("role"));
         props.setProperty("userDiv",(String)session.getAttribute("division"));
         props.setProperty("D_TypeEnabled","N");
         props.setProperty("R_TypeEnabled","N");
         props.setProperty("T_TypeEnabled","Y");
    %>
         <font class="testohpnero"><b>Transmission Vobs:</b></font>
        <%xmlRdr.applyXSL(VobManager.getVobsRoot().getOwnerDocument(), config.getServletContext().getInitParameter("webAppDir")+"/xsl/vobs_rep.xsl", props, out);%>
         <br>
         <font class="testohpnero"><b>Receiver Vobs:</b></font>
         <%
         props.setProperty("D_TypeEnabled","N");
         props.setProperty("R_TypeEnabled","Y");
         props.setProperty("T_TypeEnabled","N");
         props.setProperty("controlName", "rVob");
        xmlRdr.applyXSL(VobManager.getVobsRoot().getOwnerDocument(), config.getServletContext().getInitParameter("webAppDir")+"/xsl/vobs_rep.xsl", props, out);
    %>


    <%}else{
         if (session.getAttribute("vob")!= null){
            props.setProperty("vob_dflt_name",(String)session.getAttribute("vob"));
         }
         props.setProperty("userRole",(String)session.getAttribute("role"));
         props.setProperty("userDiv",(String)session.getAttribute("division"));
         props.setProperty("D_TypeEnabled",(String)request.getAttribute("D_TypeEnabled"));
         props.setProperty("R_TypeEnabled",(String)request.getAttribute("R_TypeEnabled"));
         props.setProperty("T_TypeEnabled",(String)request.getAttribute("T_TypeEnabled"));
         xmlRdr.applyXSL(VobManager.getVobsRoot().getOwnerDocument(), config.getServletContext().getInitParameter("webAppDir")+"/xsl/vobs_rep.xsl", props, out);
    }%>

<!-- /REPORT -->
 
               <BR>
              </TD>
             </TR>
             <TR>
    	      <td>	
	      <!-- BUTTONS -->	
              <table cellpadding="0" cellspacing="0" border="0" width="560">
                <tr>
                  <td colspan="1"><img src="img/pix_nero.gif" width="100%" height="1" alt="" border="0"></td>
                </tr>
	      </table>
	      <table width="560">
		<tr>
                 <td align="right"> 			
     		     <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		      <TR>
                       <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                       <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;SUBMIT</a></TD>
                       <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
		      </TR>
		     </TABLE>			
		 </td>
		</tr>
	      </table>	
              <!-- /BUTTONS -->
              </TD>
             </TR>
            </FORM>

<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
