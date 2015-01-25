<%@ page import="java.util.*" 
    isErrorPage="false"
    errorPage="uncaughtErr.jsp" 
%>
<%String contextPath = request.getContextPath();%>
<HTML><HEAD><TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type="text/css" rel=stylesheet>
</HEAD>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="LOGIN PAGE"; %>
<%@ include file="top.jsp" %>


             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->LOGIN FORM</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>
             <form name="loginForm" action="loginServlet" method="post">
             <TR>
              <TD ALIGN="LEFT">


               <!-- FORM -->


                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>

                      <!-- INTER ROWS SPACE -->
                      <tr>
                        <td colspan=5><img height=15 alt="" src="img/pix.gif" width=1 border=0></td>
                      </tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>LDAP Login</b><br>
                         <input type="text" class="txt" maxlength="100" size="10" name="user" value="">
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                          <b>LDAP Password</b><br>
                          <input type="PASSWORD" class="txt" maxlength="100" size="10" name="pwd" value="">
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      </tbody>
                    </table>

               <!--/FORM -->

              </TD>
             </TR>
             <TR>
    	      <td>
	      <br>
	      <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><img src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		<tr>
                 <td align="right">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="middle"><INPUT TYPE=IMAGE SRC="img/pix.gif" height=0 width=0 ALIGN=MIDDLE><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="middle" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:document.loginForm.submit()">&nbsp;SUBMIT</a></TD>
                    <TD valign="middle" ><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>
              </table>
              </form>
              <!-- /BUTTONS -->
              </TD>
             </TR>




<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
