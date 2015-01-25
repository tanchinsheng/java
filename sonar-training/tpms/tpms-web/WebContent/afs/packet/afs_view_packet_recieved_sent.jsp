<%@ page import="it.txt.afs.packet.Packet" %>
<%@ page import="it.txt.afs.packet.utils.PacketConstants" %>
<%@ page import="it.txt.afs.servlets.FileDirectorySelectionServlet"%>
<%
    String contextPath = request.getContextPath();
    Packet packet = (Packet) request.getAttribute(PacketConstants.PACKET_ATTRIBUTE_REQUEST_NAME);
    boolean showWaitMessage = AfsServletUtils.showWaitMessage(request);
    String currentUserLogin = (String) session.getAttribute("user");
    String currentUserWorkDir = UserUtils.getWorkDirectory(currentUserLogin);
    String currentUserHomeDir = UserUtils.getHomeDirectory(currentUserLogin);
    if (GeneralStringUtils.isEmptyString(currentUserWorkDir))
        currentUserWorkDir = currentUserHomeDir;

    String selectedExtractionPath = request.getParameter(FileDirectorySelectionServlet.USER_SELECTION_CONTROL_NAME);
    if (GeneralStringUtils.isEmptyString(selectedExtractionPath)) {
        selectedExtractionPath = "";
    } else {
       currentUserWorkDir = selectedExtractionPath;  
    }

%>
<HTML>
<HEAD>
    <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <LINK href="<%=contextPath%>/default.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
    <script language="javascript">
    function extractPackage(){
        var objForm = document.extractForm;
        if (!isEmptyString(objForm.<%=PacketConstants.EXTRACT_PATH_FIELD_NAME%>.value)) {
            objForm.<%=AfsServletUtils.ACTION_FIELD_NAME%>.value = '<%=PacketServlet.EXTRACT_PACKET_ACTION%>';
            objForm.submit();
        } else {
            alert('<%=PacketConstants.EXTRACTION_PATH_LABEL%> is mandatory!');
        }
    }

    function selectExtractionPath(){
        var objForm = document.forms['extractForm'];
        objForm.elements['<%=AfsServletUtils.ACTION_FIELD_NAME%>'].value = '<%=PacketServlet.EXTRACTION_PATH%>';

        objForm.submit();
    }

    </script>
</HEAD>

<BODY bgColor="#FFFFFF">
<% boolean repBool = false;
    boolean csvRepBool = false;
    String pageTitle = "Sent package view"; %>
<%@ include file="/top.jsp" %>
<%if (showWaitMessage) {%>
<%@ include file="/afs/afs_wait_message.jsp" %>
<%} else {%>
<form name="extractForm" method="post" action="<%=contextPath%>/packetServlet">
    <%--xferpath parameters--%>
    <input type="hidden" name="<%=FileDirectorySelectionServlet.RESULT_PAGE_PARAMETER_NAME%>" value="packetServlet?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=PacketServlet.VIEW_RECIEVED_PACKET_ACTION%>">
    <input type="hidden" name="<%=FileDirectorySelectionServlet.INITIAL_DIRECTORY_PARAMETER_NAME%>" value="<%=currentUserWorkDir%>">
    <input type="hidden" name="<%=FileDirectorySelectionServlet.RELATIVE_ROOT_DIR_PARAMETER_NAME%>" value="<%=currentUserHomeDir%>">
    <input type="hidden" name="<%=FileDirectorySelectionServlet.LIMIT_NAVIGATION_UNDER_RELATIVE_ROOT_PARAMETER_NAME%>" value="<%=FileDirectorySelectionServlet.LIMIT_NAVIATION_TO_ROOT_PARAMETER_VALUE%>">
    <input type="hidden" name="<%=FileDirectorySelectionServlet.FILE_SELECTION_PARAMETER_NAME%>" value="<%=FileDirectorySelectionServlet.DIRECTORY_SELECTION_PARAMETER_VALUE%>">
    <%--xferpath parameters--%>
    <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="">
    <input type="hidden" name="<%=PacketConstants.ID_FIELD_NAME%>" value="<%=packet.getId()%>">
    <input type="hidden" name="<%=AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME%>" value="<%=AfsServletUtils.getRequestAttributeParameter(request, AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME)%>">
    <input type="hidden" name="<%=PacketConstants.EXTRACT_PATH_FIELD_NAME%>" value="<%=selectedExtractionPath%>">

<TR>
    <TD ALIGN="LEFT">
        <table cellpadding="0" cellspacing="0" border="0" width="560">
            <tr>
                <td width="4"><img src="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                <td background="<%=contextPath%>/img/tit_center.gif" align="center" class="titverdi" nowrap><b>PACKAGE DATA</b></td>
                <td width="4"><img src="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                <td><img src="<%=contextPath%>/img/pix.gif" width="453" height="18" alt="" border="0"></td>
            </tr>
            <tr>
                <td colspan="4"><img src="<%=contextPath%>/img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
<TD ALIGN="LEFT">

<table cellspacing=0 cellpadding=0 width="70%" border=0>
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<!-- FIELDS ROW -->
<tr>
    <!-- LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.NAME_LABEL%></b><br>
        <%=packet.getName()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.FROM_PLANT_LABEL%></b><br>
        <%=packet.getFromPlant()%>
    </td>
</tr>
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<tr>
    <!-- LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.STATUS_LABEL%></b><br>
        <%=packet.getFormattedStatus()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.SENT_DATE_LABEL%></b><br>
        <%=packet.getFormattedSentDate()%>
    </td>
</tr>
<!-- /FIELDS ROW -->
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<tr>
    <!-- LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td class=testo width="80%" colspan="4">
        <b><%=PacketConstants.SENDER_LOGIN_LABEL%></b><br>
        <%=packet.getSenderLogin()%>
    </td>
</tr>
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<tr>
    <!-- LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.TP_RELEASE_LABEL%></b><br>
        <%=packet.getTpRelease()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.TP_REVISION_LABEL%></b><br>
        <%=packet.getTpRevision()%>
    </td>
</tr>
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<tr>
    <!-- LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.FIRST_RECIEVE_LOGIN_LABEL%></b><br>
        <%=packet.getFirstRecieveLogin()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.SECOND_RECIEVE_LOGIN_LABEL%></b><br>
        <%=packet.getSecondRecieveLogin()%>
    </td>
</tr>
<!--/fields-->
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<tr>
    <!-- LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td class=testo width="80%" colspan="4">
        <b><%=PacketConstants.EXTRACTION_PATH_LABEL%>*</b>(the path where the package will be extracted)<br>
        <%=selectedExtractionPath%>
    </td>
</tr>


</table>

</TD>
</TR>
<TR>
    <td>
        <!-- BUTTONS -->
        <table cellpadding="0" cellspacing="0" border="0" width="65%">
            <tr>
                <td>&nbsp</td>
            </tr>
            <tr>
                <td><img src="<%=contextPath%>/img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
            </tr>
            <tr>
                <td><img src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
            </tr>
            <tr>
                <td align="right">
                    <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                        <TR>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                            <TD valign="center" align="right" background="<%=contextPath%>/img/btn_center.gif"
                                class="testo"><A CLASS="BUTTON" HREF="javascript:selectExtractionPath()">SELECT EXTRACTION PATH</a></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                            <TD><img alt="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                            <TD valign="center" align="right" background="<%=contextPath%>/img/btn_center.gif"
                                class="testo"><A CLASS="BUTTON" HREF="javascript:extractPackage()">EXTRACT</a></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                        </TR>
                    </TABLE>
                </td>
            </tr>
        </table>
        <!-- /BUTTONS -->
    </TD>
</TR>
</form>
<%}%>

<%@ include file="/bottom.jsp" %>
</BODY>
</HTML>
