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