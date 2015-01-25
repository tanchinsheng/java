<%@ page import="java.util.*, org.w3c.dom.*, tol.*, tpms.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%
    String linesetName = (String)request.getAttribute("linesetName")!= null ? (String)request.getAttribute("linesetName") : "";
    String testerFam = (String)request.getAttribute("testerFam")!= null ? (String)request.getAttribute("testerFam") : "";
    String localPlant = config.getServletContext().getInitParameter("localPlant");
%>
<%
    Element userData=CtrlServlet.getUserData((String)session.getAttribute("user"));
    String homeDir = xmlRdr.getVal(userData,"HOME_DIR");
    String owner = xmlRdr.getVal(userData,"UNIX_USER");
    String fromEmail = xmlRdr.getVal(userData,"EMAIL");
    String baseDirShow ="";
    String curDirPath ="";

    if(request.getAttribute("curDirPath") != null)
    {
      curDirPath= (String)request.getAttribute("curDirPath");
      baseDirShow = curDirPath.substring(homeDir.length());
    }
    String baseDir = homeDir.concat(baseDirShow);

%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
  <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
  <LINK href="default.css" type=text/css rel=stylesheet>
  <script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
  <SCRIPT type="text/javascript" language=JavaScript>

  function changeDir()
   {
     document.changeDirForm.linesetName.value=document.lsActionForm.elements['FIELD.LS_NAME'].value;
     var ind=document.lsActionForm.elements['FIELD.TESTERFAM'].selectedIndex;

     if (ind>=0)
     {
       document.changeDirForm.testerFam.value=document.lsActionForm.elements['FIELD.TESTERFAM'].options[ind].text;
     }
     else
     {
       document.changeDirForm.testerFam.value='';
     }
     document.changeDirForm.submit();
   }


   function submitAction()
   {
        if (!controlData()) return;
        document.lsActionForm.submit();
   }

   function controlData(){
     lsName = document.lsActionForm.elements['FIELD.LS_NAME'].value;
     var testerFam;
     var ind=document.lsActionForm.elements['FIELD.TESTERFAM'].selectedIndex;
     if (ind>=0)
     {
       testerFam =document.lsActionForm.elements['FIELD.TESTERFAM'].options[ind].text;
     }
     else
     {
       testerFam='';
     }
     var syncroDir = document.lsActionForm.elements['FIELD.SYNCRODIR'].value;

     if(lsName == "")
     {
        alert('Lineset name is mandatory!\nLineset name is the logical name for files/dir to one/more TP.');
        return false;
     }

     if (filterUserInputAphaNumericalChars(document.lsActionForm.elements['FIELD.LS_NAME']) >= 0) {
         alert("Lineset contains invalid characters:\nOnly alphanumeric chars ('A..Z' and '0..9') and underscore ('_') are allowed");
         return false;
     }



     if(testerFam == '')
     {
        alert('TESTER FAMILY IS A MANDATORY FIELD! ');
        return false;
     }
     if(syncroDir == '')
     {
        alert('UNIX BASE DIR IS A MANDATORY FIELD! \nUNIX BASE DIR is the directory tree that will be linked with the TPMS/W LINESET NAME. ');
        return false;
     }
     return true;
   }
  </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="NEW LINESET INSERTION FORM"; %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->NEW LINESET DATA</b></td>
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
              <INPUT TYPE="HIDDEN" name="pageTitle" value="NEW LINESET INSERTION FORM -> BASE DIR SELECTION">
              <INPUT TYPE="HIDDEN" name="nextPage" value="ls_action_seldir.jsp">
              <INPUT TYPE="HIDDEN" name="outPage" value="ls_submit_form.jsp">
              <INPUT TYPE="HIDDEN" name="curDirPath" value="<%= baseDir %>">
	      <!-- INITIAL DIRECTORY SPECIFICATION -->
                <INPUT TYPE="HIDDEN" name="nextDirPath" value="<%= baseDir %>">
	      <!-- /INITIAL DIRECTORY SPECIFICATION -->
              <INPUT TYPE="HIDDEN" name="initDirPath" value="HOME_DIR">
              <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="ownerEmail" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="facility" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="testerInfo" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="linesetName" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="testerFam" VALUE="">
        </FORM>

	    <FORM name="lsActionForm" action="lsActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="ls_submit_preview">
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="<%= fromEmail%>">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.HOME_DIR" VALUE="<%=homeDir%>">

                         <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>

                      <!-- INTER ROWS SPACE -->
                      <!--<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>-->
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>Lineset name *</b><br>
                         <input class="txt" maxlength="255" size="10" name="FIELD.LS_NAME" value="<%=linesetName%>">
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                          <b>Tester Family*</b>
                            <select class="tendina" size="1"  name="FIELD.TESTERFAM">
                            <option>
                               <%
                               Vector testfamList=TesterInfoMgr.getTesterFamilyList(localPlant);
                               for (int i=0; i<testfamList.size(); i++)
                               {%>
                               <option <%=(((String)xmlRdr.getVal((Element)testfamList.elementAt(i))).equals(testerFam) ? "selected" : "") %> ><%=xmlRdr.getVal((Element)testfamList.elementAt(i))%>
                             <%}
                           %>
                          </select>

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
                          <b>Owner</b><br>
                          <%=owner%>

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
                         <b>Unix base dir *</b><br>
                         <%=baseDirShow%>
                         <input type="hidden" name="FIELD.SYNCRODIR" value="<%=baseDirShow%>">
                        </td>
                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <tr>
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
              * = Mandatory field
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
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:changeDir()">SEL DIR</a></TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_right.gif"></TD>
		    <TD><img src="img/pix.gif" width="5" border="0"></TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitAction()">SUBMIT PREVIEW</a></TD>
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
