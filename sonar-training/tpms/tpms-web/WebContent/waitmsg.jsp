<%@ page import="java.io.File"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.DataInputStream"%>
<%@ page import="java.io.BufferedReader"%>
<%
  String pct=null;
  String logFileName = repFileName.substring(0,repFileName.length() - 4)+".log";
  if (new File(logFileName).exists())
  {
     DataInputStream fin=new DataInputStream(new FileInputStream(logFileName));
    String s="";
    while ((s=fin.readLine())!=null)
    {
      if (s.startsWith("pct=")) pct=s.substring(4);
    }
    fin.close();
  }
  int pctnum=0;
  int pctDiscreteNum=0;
  if (pct!=null)
  {
    try{pctnum = new Integer(pct).intValue();} catch(NumberFormatException e) {};
    pctDiscreteNum=pctnum/10;
    if (pctDiscreteNum>9) pctDiscreteNum=9;
  }
%>
             <TR>
              <TD><img src="img/pix.gif" height="8" border="0"></TD>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td><img src="img/tit_sx2_big.gif" width="5"  alt="" border="0"></td>
                  <td background="img/tit_center_big.gif" align="center" class="titverdibig" width="550" nowrap><b>WAIT PLEASE</b></td>
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
		          <TD class="title" align="center" width="550"><%= (actionTxt.startsWith("PROCESSING") ? actionTxt : actionTxt+" RUNNING") %></td>
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>

		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD align="center" width="550"><img src="img/clock4.gif" border="0"></td>	
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>

    <!-- PROGRESS BAR VISUALIZATION -->
    <% if (pct!=null)
       {%>
		<tr>
          <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD align="center">
             <table cellpadding="0" cellspacing="0" border="0">
	           <tr>
                <td valign="bottom" align="center" width="400" height="22" class="waitbar" background="<%= "img/wait_full_"%><%= pctDiscreteNum %>.gif">
                 <b><%= pct %>%</b>
                </td>
               </tr>
             </table>
          </TD>
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
