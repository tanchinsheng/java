<%@ page import="java.util.*, org.w3c.dom.*, tol.*, tpms.*"
%>
<%
    String param=""; String value="";
    Vector tVobs=VobManager.getTvobNameList();
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>

   function submitForm()
   {
        topwin = window.opener;
        /*
        if(checkNofSelItems() == 0)
        {
            return;
        }
        else
        {
            var tvobs='';
            for (i=0; i< document.vobForm.elements.length; i++)
            {
                if (document.vobForm.elements[i].type=='checkbox')
                {
                    if (document.vobForm.elements[i].checked) tvobs = tvobs + document.vobForm.elements[i].value + '\n';
                }
            }
            topwin.document.vobForm.elements['FIELD.TVOB'].value=tvobs;
        }
        */
        var tvobs='';
        for (i=0; i< document.vobForm.elements.length; i++)
        {
            if (document.vobForm.elements[i].type=='checkbox')
            {
                if (document.vobForm.elements[i].checked) tvobs = tvobs + document.vobForm.elements[i].value + '\n';
            }
        }
        topwin.document.vobForm.elements['FIELD.TVOB'].value=tvobs;
        window.close();
   }

   function checkNofSelItems()
   {
     cont=0;
     for (i=0; i< document.vobForm.elements.length; i++)
     {
        if (document.vobForm.elements[i].type=='checkbox')
	    {
            if (document.vobForm.elements[i].checked) cont++;
	    }
     }
     if (cont==0)
     {
       alert('AT LEAST ONE TP MUST BE SELECTED!');
     }

     return cont;
   }

 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="VOB INSERTION PAGE"; %>
<%String contextPath = request.getContextPath();%>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;Transational VOB Defined&nbsp;</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
	            </TD>
	            </TR>

               <!-- FORM -->

	           <FORM name="vobForm" method="post">

                    <%for(int i=0; i<tVobs.size();i++){%>
                        <INPUT TYPE=CHECKBOX NAME="chk<%=i%>" value="<%=tVobs.elementAt(i)%>"><%=tVobs.elementAt(i)%><BR>
                    <%
                    }
                    %>


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      </tbody>
                    </table>
                  </form>
               <!--/FORM -->

              </TD>
             </TR>
             <TR>
    	      <td>
	      <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><img src="img/pix_nero.gif" width="300" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><img src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		<tr>
                 <td align="lefth">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="center" align="lefth"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="lefth" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:top.close()">&nbsp;CANCEL</TD>
                    <TD valign="center" align="lefth"><IMG SRC="img/btn_right.gif"></TD>
		       <TD><img src="img/pix.gif" width="5" border="0"></TD>
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


</BODY>
</HTML>
