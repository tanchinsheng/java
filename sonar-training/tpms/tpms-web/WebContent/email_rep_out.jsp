<%--
   Program Name  		:	email_rep_out.jsp
   Developed by 		:	STMicroelectronics
   Date					: 	30-Sep-2007 
   Description			:	   
--%>
 
<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.CtrlServlet,tpms.EmailInfoMgr" %>

<%String contextPath = request.getContextPath();%>

<HTML>
<HEAD>
<TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type=text/css rel=stylesheet>
  
 <SCRIPT language=JavaScript> 
    function submitForm()
    {
       document.emailInfoForm.action.value='new'; 
       document.emailInfoForm.submit();
    }
    
    function deleteAction(email)
    {
    document.emailInfoForm.emailAddress.value=email;
    document.emailInfoForm.action.value='delete';
    var action = document.emailInfoForm.action.value;
    if (action=='delete')
    {
      ret=top.window.confirm("Do you want Confirm delete Email Address ? ");
      if (!ret) return;
    }
    document.emailInfoForm.submit();
    }
  
 </SCRIPT> 

</HEAD>
<BODY bgColor="#FFFFFF">

<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="EMAIL REPORT"; %>
<%@ include file="top.jsp" %> 

     <FORM name="emailInfoForm" action="emailInfoServlet" method="post">
            <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
 	        <INPUT TYPE="HIDDEN" NAME="emailAddress" VALUE=""> 

            <TR><TD ALIGN="LEFT"></TD></TR> 
            <TR><TD ALIGN="LEFT"><BR></TD></TR>
            <TR><TD>
            
            <table cellpadding="0" cellspacing="1" border="0" >
            <tr bgcolor="#c9e1fa"> 
                <td class="testoH" width="100" height="18"><center><b>S.No.</b></center></td>
                <td class="testoH" width="403" height="18"><center><b>Email Address</b></center></td> 
            </tr> 
            <% 
            ArrayList emailAttributes =  (ArrayList)EmailInfoMgr.getEmailDetails();
      		tpms.EmailDetails  emailDetails ; 
      		for (int i = 0; i < emailAttributes.size();i++ ){
      			emailDetails = (tpms.EmailDetails) emailAttributes.get(i); 
      		%>
      			
            <tr> 
                <td align="left" class="testoD" width="100" height="18"><center><%=i+1%></center> </td>
                <td align="left" class="testoD" width="403" height="18"><%=emailDetails.getEmailaddress()%></td>
                <td>
                   <a href="javascript:deleteAction('<%=emailDetails.getEmailaddress()%>')">
                	<img src="img/ico_canc.gif" alt="Delete" border="0" ></img>
                   </a> 
                </td>
            </tr> 
            <%
            } 
      		%>            
           </table>
 
           <table width="760">
                <tr>
                     <td align="right">
                     <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                      <TR>
                           <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                           <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;NEW EMAIL ADDRESS</a></TD>
                           <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                     </TR>
                    </TABLE>
                    </td>
                </tr>
           </table>
             
        </TD></TR>

      </FORM>

 <%@ include file="bottom.jsp" %>
</BODY>
</HTML>
