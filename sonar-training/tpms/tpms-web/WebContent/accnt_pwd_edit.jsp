<%@ page import="java.util.*, org.w3c.dom.*, tol.*" 
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>
<%
  Element accntRec=(Element)request.getAttribute("accntRec");
  String param=""; String value="";
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function submitForm()
  {
    document.userProfileForm.submit();
  } 
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="USER PASSWORD MODIFICATION PAGE"; %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->PASSWORD MODIFICATION</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">

               <!-- FORM -->
                  
	          <FORM name="userProfileForm" action="userProfileServlet" method="post">
                   <INPUT TYPE="HIDDEN" NAME="action" VALUE="save_pwd">
  		   <INPUT TYPE="HIDDEN" NAME="accntName" VALUE="<%= xmlRdr.getVal(accntRec,"NAME") %>">
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
		
			<% param="NAME"; %><% value=xmlRdr.getVal(accntRec,param); %>
                        <td class=testo width="40%">
                         <b>TPMS user</b><br>
			 <%= value %>
                        </td>
                        
                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->
                        
                        <td class=testo width="40%">
                          &nbsp;                    
                        </td>
                        
                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr>
                        <td colspan=5><img height=15 alt="" src="img/pix.gif" width=1 border=0></td>
                      </tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>
                      
                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
		
			<% param="PWD"; %><% value=xmlRdr.getVal(accntRec,param); %>
                        <td class=testo width="40%">
                         <b>New Password</b><br>
                         <%
                          if (xmlRdr.getChild(accntRec,param).getAttribute("edit").equals("Y"))
                          {%>   
                            <input type="PASSWORD" class="txt" maxlength="100" size="10" name="FIELD.<%= param %>" value="">
                          <%}
			  else {%><%= value %><%} 
			 %>
                        </td>
                        
                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->
                        
			<% param="PWD"; %><% value=xmlRdr.getVal(accntRec,param); %>
                        <td class=testo width="40%">
                         <b>Confirm password</b><br>
                         <%
                          if (xmlRdr.getChild(accntRec,param).getAttribute("edit").equals("Y"))
                          {%>   
                            <input type="PASSWORD" class="txt" maxlength="100" size="10" name="FIELD.<%= param %>_CONFIRM" value="">
                          <%}
			  else {%><%= value %><%} 
			 %>
                        </td>
                        
                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->
                  </tbody>
                    </table>
                  </form>
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
                    <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;SUBMIT</TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>	
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>


<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
