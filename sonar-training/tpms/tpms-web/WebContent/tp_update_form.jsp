<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="tpms.CtrlServlet"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="tpms.TpActionServlet"%>
<%@ page isErrorPage="false" errorPage="uncaughtErr.jsp"%>
<%
    String repFileName = (String)request.getAttribute("repFileName");
    String jobname = (String)request.getAttribute("jobname");
    String job_rel = (String)request.getAttribute("job_rel");
    String job_rev = (String)request.getAttribute("job_rev");
    String job_ver = (String)request.getAttribute("job_ver");
    String workDir = (request.getAttribute("curDirPath")!=null ? (String)request.getAttribute("curDirPath") : (String)request.getAttribute("workDir"));
    String fromEmail = (String)request.getAttribute("fromEmail");
    String ownerEmail = (String)request.getAttribute("ownerEmail");
    String facility = (String)request.getAttribute("facility");
    String testerInfo = (String)request.getAttribute("testerInfo");
    Element tpRec = (Element) session.getAttribute("tpRec");
    boolean isStepsTP = false;
    if (tpRec != null && "STEPS".equals(XmlUtils.getVal(tpRec, "ORIGIN_LBL"))){
        isStepsTP = true;
    }
%>
<%String contextPath = request.getContextPath();%> 
<HTML>
<HEAD>
  <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
  <LINK href="default.css" type=text/css rel=stylesheet>
  <script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
  <SCRIPT language=JavaScript> 
    
   function changeDir()
   {
     document.changeDirForm.submit();
   }
   
   function markActionComm(){
   		var markActionComm = document.tpActionForm.markAsValCmt.value;
     	if (markActionComm != "") {	
	    	if (filterSpecialChar(markActionComm) == false) {
           		alert("Comment contains invalid characters ~ , | ,\" and \' \nComment must not be more than 2048 characters!");
            	return false;
       		} 
       	}
   		return true;
   }
   
   function submitAction()
   {
     <%if (isStepsTP) {%>
        if (confirm("Be careful!\nThe current TP was sent using a tool different from TPMS/W.\n\nAll the changes you have done to TP files will be ignored by TPMS/W system\n\nContinue? ")){
            document.tpActionForm.submit();
        }
     <%} else {%>
     	 if(!markActionComm()) return;
         document.tpActionForm.submit();
     <%}%>
   }
  </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP MARK AS VALIDATED FORM"; %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->TP UPLOAD DATA</b></td>
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
            <FORM name="changeDirForm" action="navDirServlet" method="post">
 	      <INPUT TYPE="HIDDEN" name="pageTitle" value="TP MARK AS VALIDATE FORM -> SOURCE DIR SELECTION">
 	      <INPUT TYPE="HIDDEN" name="nextPage" value="tp_action_seldir.jsp">
 	      <INPUT TYPE="HIDDEN" name="outPage" value="tp_update_form.jsp">
 	      <INPUT TYPE="HIDDEN" name="curDirPath" value="<%= workDir %>">
 	      <INPUT TYPE="HIDDEN" name="nextDirPath" value="<%= workDir %>">

              <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
              <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="<%= jobname %>">
              <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="<%= job_rel %>">
              <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="<%= job_rev %>">
              <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="<%= job_ver %>">
              <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="<%= fromEmail %>">
              <INPUT TYPE="HIDDEN" NAME="ownerEmail" VALUE="<%= fromEmail %>">
              <INPUT TYPE="HIDDEN" NAME="facility" VALUE="<%= facility %>">
              <INPUT TYPE="HIDDEN" NAME="testerInfo" VALUE="<%= testerInfo %>">

            </FORM>

	    <FORM name="tpActionForm" action="tpActionServlet" method="post">

             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <%if (isStepsTP) {%>
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="<%=TpActionServlet.TP_UPDATE_STEPS_ACTION_VALUE%>">
             <%} else {%>
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_update">
             <%}%>
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
                          <b>TPMS version</b><br>
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
                         <%= ownerEmail %><input type="hidden" name="fromEmail" value="<%= fromEmail %>">
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>From email</b><br>
                         <%= fromEmail %><input type="hidden" name="fromEmail" value="<%= fromEmail %>">
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

                        <td class=testo width="30%">
                          <b>To email (local plant)</b><br>
                          <select class="tendina" maxlength="100" size="1" name="toEmail">
			  <option value="" selected>
			  <%
                            NodeList emails=CtrlServlet.getAccntsRoot().getElementsByTagName("EMAIL");
			    for (int i=0; i<emails.getLength(); i++)
			    {%>
			      <option><%= XmlUtils.getVal((Element)emails.item(i)) %>
			    <%}
			  %>
			  </select>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>CC email</b><br>
                         <input class="txt" maxlength="100" size="10" name="ccEmail" value="">
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
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Target dir</b><br>
                         <%= workDir %>
			 <input type="hidden" name="workDir" value="<%= workDir %>">
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
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Action Comment</b><br>
                        <textarea name="markAsValCmt" rows="4" cols="60" WRAP=PHYSICAL value=""></textarea>
                       </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <tr>
                      	<td class=testoDSmall width="40%" valign="top" colspan="10"> 
							Note: single, double quotes, "~" and "|" not allowed and not more than 2048 chars!
						</td>
                      </tr>                      
                      <tr>
                      		<td colspan=5><img alt="" src="img/blank.gif"  border=0></td>
                      </tr>
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
                    <TD valign="center" align="right"><IMG alt="" SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:changeDir()">CHANGE DIR</a></TD>
                    <TD valign="center" align="right"><IMG alt="" SRC="img/btn_right.gif"></TD>
		    <TD><img src="img/pix.gif" width="5" border="0"></TD>
                    <TD valign="center" align="right"><IMG alt="" SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitAction()">SUBMIT</a></TD>
                    <TD valign="center" align="right"><IMG alt="" SRC="img/btn_right.gif"></TD>
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
