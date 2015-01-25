<%@ page import="it.txt.afs.packet.Packet" %>
<%@ page import="it.txt.afs.packet.utils.PacketConstants" %>
<%@ page import="it.txt.afs.servlets.DatabaseQueryServlet"%>
<%
    String contextPath = request.getContextPath();
    Packet packet = (Packet) request.getAttribute(DatabaseQueryServlet.PACKET_DETAILS_REQUEST_ATTRIBUTE_NAME);
%>
<HTML>
<HEAD>
    <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <LINK href="<%=contextPath%>/default.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>

</HEAD>

<BODY bgColor="#FFFFFF">
<% boolean repBool = false;
    boolean csvRepBool = false;
    String pageTitle = "Sent package view"; %>
<%@ include file="/top.jsp" %>

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
<!-- /FIELDS ROW -->
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<tr>
    <!-- LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.FROM_PLANT_LABEL%></b><br>
        <%=packet.getFromPlant()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.DESTINATION_PLANT_LABEL%></b><br>
        <%=packet.getDestinationPlant()%>
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
        <%=packet.getStatus()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">&nbsp;</td>
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
    <td class=testo width="40%">
        <b><%=PacketConstants.SENT_DATE_LABEL%></b><br>
        <%=packet.getFormattedSentDate()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.EXTRACTION_DATE_LABEL%></b><br>
        <%=packet.getFormattedExtractionDate()%>
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
        <b><%=PacketConstants.EXTRACTION_LOGIN_LABEL%></b><br>
        <%=packet.getExtractionLogin()%>
    </td>
    <!-- COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <!-- /COLUMN SEPARATOR -->
    <td class=testo width="40%">
        <b><%=PacketConstants.LAST_ACTION_DATE_LABEL%></b><br>
        <%=packet.getFormattedLastActionDate()%>
    </td>
</tr>
<!-- INTER ROWS SPACE -->
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
<!-- /INTER ROWS SPACE -->
<tr>
    <!-- /LEFT COLUMN SEPARATOR -->
    <td>&nbsp;</td>
    <td class=testo width="100%" colspan="4">
        <b><%=PacketConstants.XFERPATH_LABEL%></b><br>
        <%=packet.getXferPath()%>
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
                                class="testo"><A CLASS="BUTTON" HREF="javascript:history.back()">BACK</a></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                        </TR>
                    </TABLE>
                </td>
            </tr>
        </table>
        <!-- /BUTTONS -->
    </TD>
</TR>


<%@ include file="/bottom.jsp" %>
</BODY>
</HTML>
