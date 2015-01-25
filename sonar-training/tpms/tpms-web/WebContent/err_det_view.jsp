<%
 String actionTxt=request.getParameterValues("actionTxt")[0];
 String msgTxt=request.getParameterValues("msgTxt")[0];
 String detailTxt=request.getParameterValues("detailTxt")[0];
 String sysDetailTxt=request.getParameterValues("sysDetailTxt")[0];
%>
<html>
<head>
 <title>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %> ERROR DETAILS</title>
 <link rel="stylesheet" type="text/css" href="default.css"/>
</head>
<BODY bgcolor="#FFFFFF">
<CENTER>
<TABLE CELLSPACING="0" CELLPADDING="0">

<!-- CONTENT -->
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td><img src="img/tit_sx2_big.gif" width="5"  alt="" border="0"></td>
                  <td background="img/tit_center_big.gif" align="center" class="titverdibig" width="550" nowrap><b>ERROR</b></td>
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
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title_detail" align="center" width="550"><FONT CLASS="title"><%= detailTxt %></FONT></td>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title" align="center" width="550"><HR></td>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title_detail" align="center" width="550">
		    <FONT CLASS="title"><%= sysDetailTxt %></FONT>
		    <BR>	
		  </TD>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
		<tr>
                 <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
                 <td align="center" valign="bottom">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="top"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="top" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:window.close()">CLOSE</TD>
                    <TD valign="top" ><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
                 <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
 		</tr>
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

</TABLE>
</CENTER>
</BODY>
</HTML>
