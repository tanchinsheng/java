<!-- CONTENT -->
             <TR>
              <TD><img src="img/pix.gif" height="8" border="0"></TD>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td><img src="img/tit_sx2_big.gif" width="5"  alt="" border="0"></td>
                  <td background="img/tit_center_big.gif" align="center" class="titverdibig" width="550" nowrap><b><%= msgType %></b></td>
                  <td><img src="img/tit_dx2_big.gif" width="5"  alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="3"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title" align="center" width="550">&nbsp;</td>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title" align="center" width="550"><FONT CLASS="title"><%= actionTxt %></FONT></td>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title_detail" align="center" width="550"><FONT CLASS="title"><%= msgTxt %></FONT></td>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
<%
 if (!detailTxt.equals(""))
 {%>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title_detail" align="center" width="550"><FONT CLASS="title"><%= detailTxt %></FONT></td>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
 <%}
%>
<%
 if (backBut)
 {%>
		<tr>
                 <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
                 <td align="center" valign="bottom">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:history.back()">&nbsp;BACK&nbsp;</a></TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
		    <TD valign="center" ><IMG SRC="img/pix.gif" WIDTH="5"></TD>	
		    <% if (!sysDetailTxt.equals(""))
                     {%>
                       <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                       <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:viewErrDett()">DETAILS</a></TD>
                       <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                     <%}
		    %>	
                   </TR>
                  </TABLE>
                 </td>
                 <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
 		</tr>
 <%}
%>
	
		<tr>
                  <td><img src="img/tbl_sx2_corner_big.gif" width="5" border="0"></td>
                  <td valign="bottom"><img src="img/pix_grey.gif" width="550" height="1" border="0"></td>
                  <td><img src="img/tbl_dx2_corner_big.gif" width="5" border="0"></td>
		</tr>
              </table>	
	      <BR>
	      </TD>
	     </TR>
<!-- /CONTENT --> 