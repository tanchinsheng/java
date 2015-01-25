<%@ page import="it.txt.afs.packet.utils.PacketConstants"%>
<%@ page import="it.txt.afs.packet.PacketList"%>
<%@ page import="it.txt.afs.packet.Packet"%>
<%
    String contextPath = request.getContextPath();
    boolean showWaitMessage = AfsServletUtils.showWaitMessage(request);
    PacketList packetList = null;
    if (!showWaitMessage) {
        packetList = (PacketList) request.getAttribute(AfsServletUtils.QUERY_PACKET_LIST_REQUEST_ATTRIBUTE_NAME);
    }
%>
<HTML>
<HEAD>
    <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <LINK href="<%=contextPath%>/default.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/js/PacketView.js"></script>
</HEAD>

<BODY bgColor="#FFFFFF">
<form name="showPacketDetails" action="<%=contextPath%>/packetServlet" method="post">
    <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="<%=PacketServlet.VIEW_OWNED_PACKET_ACTION%>">
    <input type="hidden" name="<%=AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME%>" value="<%=request.getAttribute(AfsServletUtils.REQUEST_ID_REQUEST_ATTRIBUTE_NAME)%>">
    <input type="hidden" name="<%=PacketConstants.ID_FIELD_NAME%>" value="">
</form>



<% boolean repBool = false; boolean csvRepBool = false; String pageTitle = "My Sent Packages"; %>
<%@ include file="/top.jsp" %>
<%if (showWaitMessage) {%>
<%@ include file="/afs/afs_wait_message.jsp" %>
<%} else {%>

<TR>
<TD ALIGN="LEFT">
<TABLE cellSpacing=1 cellPadding=2 border=0>
<!--intestazione -->
    <TR bgColor="#c9e1fa">
      <TD class="testoH"><B><%=PacketConstants.NAME_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.TP_RELEASE_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.TP_REVISION_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.DESTINATION_PLANT_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.SENT_DATE_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.STATUS_LABEL%></B></TD>
    </TR>
<%
    if (packetList != null && !packetList.isEmpty()) {
        Packet currentPacket;
        while (packetList.hasMorePackets()) {
            currentPacket = packetList.nextPacket();

%>
    <!--riga -->
            <TR>
              <TD class="testoD"><a href="javascript: showPacketsDetails('<%=currentPacket.getId()%>', document.showPacketDetails.<%=PacketConstants.ID_FIELD_NAME%>)"><%=currentPacket.getName()%></a></TD>
              <TD class="testoD"><%=currentPacket.getTpRelease()%></TD>
              <TD class="testoD"><%=currentPacket.getTpRevision()%></TD>
              <TD class="testoD"><%=currentPacket.getDestinationPlant()%></TD>
              <TD class="testoD"><%=currentPacket.getFormattedSentDate()%></TD>
              <TD class="testoD"><%=currentPacket.getStatus()%></TD>
            <TR>
      <%}
   } else {%>
    <tr>
        <td colspan="6" align="center"> No packages found </td>
    </tr>
    <%}%>
            <tr>
                <td colspan="6">&nbsp</td>
            </tr>
            <tr>
                <td colspan="6"><img src="<%=contextPath%>/img/pix_nero.gif" width="100%" height="1" alt="" border="0"></td>
            </tr>
            <tr>
                <td colspan="6"><img src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
            </tr>
            <tr>
                <td align="right" colspan="6">&nbsp;</td>
            </tr>
</TABLE>


    </TD>
</TR>

<%}%>

<%@ include file="/bottom.jsp" %>
</BODY>
</HTML>
