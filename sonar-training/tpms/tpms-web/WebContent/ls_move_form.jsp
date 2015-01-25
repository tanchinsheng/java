<%@ page import="java.util.*,
                org.w3c.dom.*,
                tol.*,
                tpms.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%@ page import="it.txt.general.utils.GeneralStringUtils"%>
<%

    String contextPath = request.getContextPath();
    Element userData = CtrlServlet.getUserData((String)session.getAttribute("user"));
    String homeDir = xmlRdr.getVal(userData,"HOME_DIR");
    String fromEmail = xmlRdr.getVal(userData,"EMAIL");
    String xslFileName = request.getParameter("xslFileName");

    if (GeneralStringUtils.isEmptyString(xslFileName))
        xslFileName = (String) request.getAttribute("xslFileName");

    String baseDirShow ="";
    String curDirPath ="";

    if( request.getAttribute("curDirPath") != null )
    {
      curDirPath = (String)request.getAttribute("curDirPath");
      baseDirShow = curDirPath.substring(homeDir.length());
    }
    String baseDir = homeDir.concat(baseDirShow);
    //ls attributes that will be used to produce the _in file
    // getting line set info.
    Element lineSetXmlRecord = (Element)session.getAttribute("lsRec");
    String vobName = (String) session.getAttribute("vob");
    String lsName = xmlRdr.getVal(lineSetXmlRecord, "LS_NAME");
    String lsNewPath = curDirPath;




%>

<HTML>
<HEAD>
  <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
  <LINK href="default.css" type="text/css" rel="stylesheet">
  <SCRIPT type="text/javascript" language=JavaScript>

  function changeDir()
   {
     document.changeDirForm.submit();
   }


    function moveLineset ()
    {
        if(document.lsActionForm.elements["FIELD.LS_NEW_PATH"].value == null || document.lsActionForm.elements["FIELD.LS_NEW_PATH"].value == '')
        {
            alert('NEW UNIX BASE DIR IS A MANDATORY FIELD!\n\nNew UNIX BASE DIR is the directory tree that will be linked with the TPMS/W LINESET NAME.');
        } else {
            document.lsActionForm.submit();
        }
    }


  </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="MOVE LINE SET ACTION FORM"; %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><IMG ALT="" src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->MOVE LINESET BASE DIR</b></td>
                  <td width="4"><IMG ALT="" src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><IMG ALT="" src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><IMG ALT="" src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">

        <!-- FORM -->
        <FORM name="changeDirForm" action="<%=contextPath%>/navDirServlet" method="post">
              <INPUT TYPE="HIDDEN" name="pageTitle" value="MOVE LINESET FORM -> BASE DIR SELECTION">
              <INPUT TYPE="HIDDEN" name="nextPage" value="ls_action_seldir.jsp">
              <INPUT TYPE="HIDDEN" name="outPage" value="ls_move_form.jsp">
              <INPUT TYPE="HIDDEN" name="curDirPath" value="<%= baseDir %>">
              <INPUT TYPE="HIDDEN" name="nextDirPath" value="<%= baseDir %>">
              <INPUT TYPE="HIDDEN" name="initDirPath" value="HOME_DIR">
        </FORM>

	    <FORM name="lsActionForm" action="<%=contextPath%>/lsActionServlet" method="post">
            <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
            <INPUT TYPE="HIDDEN" NAME="action" VALUE="ls_move">
            <INPUT TYPE="HIDDEN" NAME="FIELD.LS_NAME" value="<%=lsName%>">
            <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" value="<%=vobName%>">
            <INPUT TYPE="HIDDEN" NAME="FIELD.LS_NEW_PATH" VALUE="<%=lsNewPath%>">
        </FORM>
            <table border="0">
                <tr>
                    <td align="left" class="testo">
                        <b>Lineset name:</b>
                    </td>
                    <td align="left" class="testo">
                        &nbsp;
                    </td>
                    <td align="left" class="testo">
                        <%=lsName%>
                    </td>
                </tr><tr>
                    <td align="left" class="testo">
                        <b>Tester family:</b>
                    </td>
                    <td align="left" class="testo">
                        &nbsp;
                    </td>
                    <td align="left" class="testo">
                        <%=xmlRdr.getVal(lineSetXmlRecord, "TESTERFAM")%>
                    </td>
                </tr><tr>
                    <td align="left" class="testo">
                        <b>Unix base dir:</b>
                    </td>
                    <td align="left" class="testo">
                        &nbsp;
                    </td>
                    <td align="left" class="testo">
                        <%=xmlRdr.getVal(lineSetXmlRecord, "SYNCRODIR")%>
                    </td>
                </tr>
            </table>

             <!--/FORM -->

              </TD>
             </TR>

    <tr>
        <td class="testo" width="40%">
        &nbsp;
        </td>
    </tr>
    <tr>
        <td class="testo" width="40%">
            <b>New unix base dir *</b><br>
            <%=baseDirShow%>
        </td>
    </tr>
    <tr>
        <td class="testo" width="40%">
        &nbsp;
        </td>
    </tr>
             <TR>
    	      <td>
	      <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
              * = Mandatory field
                <tr>
                  <td><IMG ALT="" src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><IMG ALT="" src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		<tr>
                 <td align="right">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="center" align="right"><IMG ALT="" SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:changeDir()">SEL DIR</a></TD>
                    <TD valign="center" align="right"><IMG ALT="" SRC="img/btn_right.gif"></TD>
		    <TD><IMG ALT="" src="img/pix.gif" width="5" border="0"></TD>
                    <TD valign="center" align="right"><IMG ALT="" SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:moveLineset()">SUBMIT</a></TD>
                    <TD valign="center" align="right"><IMG ALT="" SRC="img/btn_right.gif"></TD>
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
