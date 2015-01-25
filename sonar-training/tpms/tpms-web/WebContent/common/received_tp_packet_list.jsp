<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.txt.afs.packet.utils.PacketConstants"%>
<%@ page import="it.txt.common.list.TpPacketElementsList"%>
<%@ page import="it.txt.common.servlets.ReceivedTpPacketsServlet"%>
<%@ page import="it.txt.common.elements.TpPacketElement"%>
<%
    String contextPath = request.getContextPath();
    TpPacketElementsList receivedTpPacketElementsList = (TpPacketElementsList) request.getAttribute(ReceivedTpPacketsServlet.ELEMENT_LIST_ATTRIBUTE_NAME);
%>
<HTML>
<HEAD>
    <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <LINK href="<%=contextPath%>/default.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/js/PacketView.js"></script>
</HEAD>

<BODY bgColor="#FFFFFF">

<% boolean repBool = false; boolean csvRepBool = false; String pageTitle = "Database query"; %>
<%@ include file="/top.jsp" %>
<TR>
<TD ALIGN="LEFT">
<TABLE cellSpacing=1 cellPadding=2 border=0>
    <!--intestazione -->
    <TR bgColor="#c9e1fa">
      <TD class="testoH"><B>Jobname /<nobr><%=PacketConstants.NAME_LABEL%></nobr></B></TD>
      <TD class="testoH"><B><%=PacketConstants.TP_RELEASE_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.TP_REVISION_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.FROM_PLANT_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.SENDER_LOGIN_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.SENT_DATE_LABEL%></B></TD>
      <TD class="testoH"><B>Type</B></TD>
      <TD class="testoH"><B><%=PacketConstants.RVOB_NAME_LABEL%></B></TD>
    </TR>
<%if (receivedTpPacketElementsList != null && !receivedTpPacketElementsList.isEmpty()) {
        TpPacketElement currentElement;
        while (receivedTpPacketElementsList.hasMoreElements()) {
            currentElement = receivedTpPacketElementsList.nextElement();
%>
    <!--riga -->
    <TR>
      <TD class="testoD"><%=currentElement.getJobName()%>&nbsp;</TD>
      <TD class="testoD"><%=currentElement.getJobRelease()%>&nbsp;</TD>
      <TD class="testoD"><%=currentElement.getJobRevision()%>&nbsp;</TD>
      <TD class="testoD"><%=currentElement.getFromPlant()%>&nbsp;</TD>
      <TD class="testoD"><%=currentElement.getOwnerLogin()%></TD>
      <TD class="testoD"><%=currentElement.getFormattedSendDate()%>&nbsp;</TD>
      <TD class="testoD"><%=currentElement.isTp() ? "Tpms/W" : "Aided Ftp"%>&nbsp;</TD>
      <TD class="testoD"><%=currentElement.getFromPlantVobName()%>&nbsp;</TD>
    <TR>
      <%}
   } else {%>
    <tr>
        <TD COLSPAN="8"><center>No received Tp or Packages found.</center></TD>
    </tr>
   <%}%>
    <tr>
        <td COLSPAN="8">&nbsp</td>
    </tr>
    <tr>
        <td COLSPAN="8"><img src="<%=contextPath%>/img/pix_nero.gif" width="100%" height="1" alt="" border="0"></td>
    </tr>
</TABLE>
</TD>
</TR>
<%@ include file="/bottom.jsp" %>
</BODY>
</HTML>
