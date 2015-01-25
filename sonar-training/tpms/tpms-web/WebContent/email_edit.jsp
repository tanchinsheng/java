<%--
   		Program Name  		:	email_edit.jsp
  		Developed by 		:	STMicroelectronics
   		Date				: 	30-Sep-2007 
   		Description			:	 
--%>

<%@ page import="java.util.*, org.w3c.dom.*, tol.*, tpms.*" %>
 
<HTML>
<HEAD>
 <TITLE>TPMS/W Division email edit page <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
    
 <SCRIPT language=JavaScript>
  
   function submitForm()
   { 
   var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
   var address = document.emailInfoForm.emailAddress.value;
   if(reg.test(address) == false || address.length > 100) 
   {
      alert('Invalid Email Address');
   } 
   else 
   { 
     document.emailInfoForm.action.value='save'; 
     document.emailInfoForm.submit();
     return true;
   }
   }

 </SCRIPT>
 
</HEAD>
<BODY bgColor="#FFFFFF">

<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="Email Address Edit Page"; %>

<%String contextPath = request.getContextPath();%>
<%@ include file="top.jsp" %>

              <TR><TD ALIGN="LEFT"></TD></TR>

             <TR>
              <TD ALIGN="LEFT">
	           <FORM name="emailInfoForm" action="emailInfoServlet" method="post">
		            
 	                <!-- <INPUT TYPE="HIDDEN" NAME="emailAddress" VALUE=""> -->
                    <INPUT TYPE="HIDDEN" NAME="action" VALUE="save">
                   
                    <table cellspacing=0 cellpadding=0 width="65%" border=0 >
                      <tbody>
                        <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                        
                        <tr><td class=testo width="15%" valign="top" colspan="1">
                         <b>Email Address *</b>  
                         <input type="text" class="txtlong" maxlength="80" size="40" name="emailAddress" >
                         </td>
                         </tr>
                         
                        <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>  
                      </tbody>
                    </table>
                  </form>
              
              </TD>
             </TR>
             
             <TR><td>
             
             <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr><td><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td></tr>
                <tr><td><img src="img/blank_flat.gif" alt="" border="0"></td></tr>
		        <tr><td align="right">
                 
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm();">&nbsp;SUBMIT</TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		 </tr>
                <tr><td class="txtnob"><br>
                    <i>*  = Mandatory field<br></i>
		            </td>
                </tr>
              </table> 
              </TD>
             </TR>
  
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
