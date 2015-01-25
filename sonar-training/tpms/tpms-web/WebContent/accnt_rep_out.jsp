<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.CtrlServlet"
    isErrorPage="false"
    errorPage="uncaughtErr.jsp"
%>
<%  
  String time = new Long(System.currentTimeMillis()).toString();
  String webAppDir= config.getServletContext().getInitParameter("webAppDir");
  Element accntsRoot=CtrlServlet.getAccntsRoot(); 
%>
<%String contextPath = request.getContextPath();%>

<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function submitForm()
  {
    document.userProfileForm.action.value='new';	
    document.userProfileForm.submit();
  }

  function go_to(user, action)
  {
    if (action=='delete')
    {
      ret=top.window.confirm("DO YUO ACTUALLY WANT TO DELETE USER "+user+" ?");
      if (!ret) return;
    }
    document.userProfileForm.accntName.value=user;	
    document.userProfileForm.action.value=action;	
    document.userProfileForm.submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="LOCAL USERS REPORT"; %>
<%@ include file="top.jsp" %>

           <FORM name="userProfileForm" action="userProfileServlet" method="post">
            <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
 	    <INPUT TYPE="HIDDEN" NAME="accntName" VALUE="">
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;LOCAL&nbsp; USERS&nbsp;</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="603" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
                xmlRdr.transform(accntsRoot.getOwnerDocument(), webAppDir+"/xsl/accnt_rep.xsl", null, out);
               %>
	       <!-- /REPORT -->
               <BR>
              </TD>
             </TR>

             <TR>
    	      <TD>	
	      <!-- BUTTONS -->	
              <table cellpadding="0" cellspacing="0" border="0" width="560">
                <tr>
                  <td colspan="1"><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
	      </table>
	      <table width="560">
		<tr>
                 <td align="right"> 			
     		     <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		      <TR>
                       <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                       <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;NEW USER</a></TD>
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
