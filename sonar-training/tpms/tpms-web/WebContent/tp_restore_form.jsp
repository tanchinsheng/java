<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%
 String repFileName=(String)request.getAttribute("repFileName");
 String jobname=(String)request.getAttribute("jobname");
 String job_rel=(String)request.getAttribute("job_rel");
 String job_rev=(String)request.getAttribute("job_rev");
 String job_ver=(String)request.getAttribute("job_ver");
 //String workDir=(request.getAttribute("curDirPath")!=null ? (String)request.getAttribute("curDirPath") : (String)request.getAttribute("workDir"));
 String fromEmail=(String)request.getAttribute("fromEmail");
 String ownerEmail=(String)request.getAttribute("ownerEmail");
 String facility=(String)request.getAttribute("facility");
 String testerInfo=(String)request.getAttribute("testerInfo");
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
  <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
  <LINK href="default.css" type=text/css rel=stylesheet>
  <SCRIPT language=JavaScript>
   function changeDir()
   {
     document.changeDirForm.submit();
   }
   function submitAction()
   {
     document.tpActionForm.submit();
   }
  </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP RESTORE FORM"; %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->TP ACTION DATA</b></td>
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


	    <FORM name="tpActionForm" action="tpActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_restore">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="<%= jobname %>">
             <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="<%= job_rel %>">
             <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="<%= job_rev %>">
             <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="<%= job_ver %>">
                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>Job name (PRIS)</b><br>
                         <%= jobname %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                          <b>Job release (PRIS)</b><br>
                          <%= job_rel %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>Job revision</b><br>
                         <%= job_rev %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                          <b>Process stage</b><br>
                          <%= facility %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>Tester info</b><br>
                         <%= testerInfo %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                          <b>TPMS ver</b><br>
                          <%= job_ver %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>TP owner email</b><br>
                         <%= ownerEmail %>
			 <input type="hidden" name="fromEmail" value="<%= fromEmail %>">
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->



                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->


		     </tbody>
                    </table>
             </FORM>
             <!--/FORM -->

              </TD>
             </TR>
             <TR>
    	      <td>
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
                    <TD valign="center" align="right"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitAction()">SUBMIT</TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_right.gif"></TD>
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
