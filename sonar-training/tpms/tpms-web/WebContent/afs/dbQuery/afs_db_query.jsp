<%@ page import="it.txt.afs.packet.utils.PacketConstants"%>
<%@ page import="it.txt.afs.packet.PacketList"%>
<%@ page import="it.txt.afs.packet.Packet"%>
<%@ page import="it.txt.afs.packet.utils.PacketsDbSearchResult"%>
<%@ page import="it.txt.afs.servlets.DatabaseQueryServlet"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="it.txt.general.utils.HtmlUtils"%>
<%
    String contextPath = request.getContextPath();


    Vector destinationPlantList = (Vector) session.getAttribute(DatabaseQueryServlet.DESTINATION_PLANT_SESSION_ATTRIBUTE_NAME);
    Vector fromPlantList = (Vector) session.getAttribute(DatabaseQueryServlet.FROM_PLANT_SESSION_ATTRIBUTE_NAME);
    Vector firstRecieversList = (Vector) session.getAttribute(DatabaseQueryServlet.FIRST_RECIEVERS_LOGIN_SESSION_ATTRIBUTE_NAME);
    Vector secondRecieversList = (Vector) session.getAttribute(DatabaseQueryServlet.SECOND_RECIEVERS_LOGIN_SESSION_ATTRIBUTE_NAME);
    Vector senderLoginList  = (Vector) session.getAttribute(DatabaseQueryServlet.SENDER_LOGIN_SESSION_ATTRIBUTE_NAME);
    Vector extractionLoginList = (Vector) session.getAttribute(DatabaseQueryServlet.EXTRACTION_LOGIN_SESSION_ATTRIBUTE_NAME);
    String executedAction = (String) request.getParameter(AfsServletUtils.ACTION_FIELD_NAME);


    PacketsDbSearchResult searchResult = (PacketsDbSearchResult) request.getAttribute(DatabaseQueryServlet.SEARCH_RESULT_REQUEST_ATTRIBUTE_NAME);
    PacketList packetList = null;
    String name = "";
    String tpRelease = "";
    String tpRevision = "";
    String destinationPlant = "";
    String fromPlant = "";
    String senderLogin = "";
    String firstRecieveLogin = "";
    String secondRecieveLogin = "";
    String status = "";
    String extractionLogin = "";
    String extractionDateFrom = "";
    String extractionDateTo = "";
    String lastActionDateFrom = "";
    String lastActionDateTo = "";
    String sentDateFrom = "";
    String sentDateTo = "";

    if (searchResult != null){
        packetList = searchResult.getPacketList();
        Hashtable searchParameters = searchResult.getFieldsValues();
        String tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_NAME_FIELDS);
        name = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;

        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_TP_RELEASE_FIELDS);
        tpRelease = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_TP_REVISION_FIELDS);
        tpRevision = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_DESTINATION_PLANT_FIELDS);
        destinationPlant = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_FROM_PLANT_FIELDS);
        fromPlant = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_SENDER_LOGIN_FIELDS);
        senderLogin = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_FIRST_RECIEVE_LOGIN_FIELDS);
        firstRecieveLogin = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_SECOND_RECIEVE_LOGIN_FIELDS);
        secondRecieveLogin = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_STATUS_FIELDS);
        status = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;
        tmpString = (String) searchParameters.get(AfsServletUtils.DB_SEARCH_EXTRACTION_LOGIN_FIELDS);
        extractionLogin = (GeneralStringUtils.isEmptyString(tmpString)) ? "" : tmpString;


        Date tmpDate = (Date) searchParameters.get(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS);
        extractionDateFrom = (tmpDate == null) ? "" : DatabaseQueryServlet.formatDate(tmpDate);
        tmpDate = (Date) searchParameters.get(AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS);
        extractionDateTo = (tmpDate == null) ? "" : DatabaseQueryServlet.formatDate(tmpDate);
        tmpDate = (Date) searchParameters.get(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS);
        lastActionDateFrom = (tmpDate == null) ? "" : DatabaseQueryServlet.formatDate(tmpDate);
        tmpDate = (Date) searchParameters.get(AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS);
        lastActionDateTo = (tmpDate == null) ? "" : DatabaseQueryServlet.formatDate(tmpDate);
        tmpDate = (Date) searchParameters.get(AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS);
        sentDateFrom = (tmpDate == null) ? "" : DatabaseQueryServlet.formatDate(tmpDate);
        tmpDate = (Date) searchParameters.get(AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS);
        sentDateTo = (tmpDate == null) ? "" : DatabaseQueryServlet.formatDate(tmpDate);
   }
%>
<HTML>
<HEAD>
    <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <LINK href="<%=contextPath%>/default.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=contextPath%>/js/PacketView.js"></script>
    <script language="javascript">
    function openCalendar(fieldDateFrom, fieldDateTo)
    {
        var url="<%=contextPath%>/calendar_widget.jsp";
        var qrystr="?field=" + fieldDateFrom + "&field2=" + fieldDateTo + "&form="+"searchPackets";
        var popup = window.open(url+qrystr,"CALENDAR","width=680,height=420,resizable=yes,scrollbars=no,status=0,toolbar=no,location=no,menubar=no");

        if (popup != null)
        {
            if (popup.opener == null) popup.opener = self
            popup.focus();
        }
    }
    </script>
</HEAD>

<BODY bgColor="#FFFFFF">

<% boolean repBool = false; boolean csvRepBool = false; String pageTitle = "Database query"; %>
<%@ include file="/top.jsp" %>
<form name="showPacketDetails" method="post" action="<%=contextPath%>/queryDatabasePackets">
    <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="<%=DatabaseQueryServlet.VIEW_PACKET_DETAILS_ACTION_VALUE%>">
    <input type="hidden" name="<%=AfsServletUtils.DB_SEARCH_ID_FIELDS%>" value ="">
</form>
<form name="searchPackets" method="post" action="<%=contextPath%>/queryDatabasePackets">
    <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="<%=DatabaseQueryServlet.SEARCH_PACKETS_ACTION_VALUE%>">
<TR>
<TD ALIGN="LEFT">
<TABLE cellSpacing=1 cellPadding=2 border=0>

<!--intestazione -->
    <TR bgColor="#c9e1fa">
      <TD class="testoH"><B><%=PacketConstants.NAME_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.TP_RELEASE_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.TP_REVISION_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.DESTINATION_PLANT_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.EXTRACTION_DATE_LABEL%></B></TD>
      <TD class="testoH"><B><%=PacketConstants.STATUS_LABEL%></B></TD>
    </TR>
<%if (packetList != null && !packetList.isEmpty()) {
        Packet currentPacket;
        while (packetList.hasMorePackets()) {
            currentPacket = packetList.nextPacket();
%>
    <!--riga -->
            <TR>
              <TD class="testoD"><a href="javascript: showPacketsDetails('<%=currentPacket.getId()%>', document.showPacketDetails.<%=AfsServletUtils.DB_SEARCH_ID_FIELDS%>)"><%=currentPacket.getName()%></a>&nbsp;</TD>
              <TD class="testoD"><%=currentPacket.getTpRelease()%>&nbsp;</TD>
              <TD class="testoD"><%=currentPacket.getTpRevision()%>&nbsp;</TD>
              <TD class="testoD"><%=currentPacket.getDestinationPlant()%>&nbsp;</TD>
              <TD class="testoD"><%=currentPacket.getFormattedExtractionDate()%>&nbsp;</TD>
              <TD class="testoD"><%=currentPacket.getStatus()%>&nbsp;</TD>
            <TR>
      <%}
   } else if (!GeneralStringUtils.isEmptyString(executedAction) && executedAction.equals(DatabaseQueryServlet.SEARCH_PACKETS_ACTION_VALUE)){%>
    <TR>
      <TD class="testo" colspan="6"><center><B>No packages found</B><br>The given search criterina given an empty result</center></TD>
    </TR>
 <%}%>

            <tr>
                <td colspan="6">&nbsp</td>
            </tr>
            <tr>
                <td colspan="6"><img src="<%=contextPath%>/img/pix_nero.gif" width="100%" height="1" alt="" border="0"></td>
            </tr>

            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.NAME_LABEL%></b> <nobr>(use % as jolly char)</nobr><br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_NAME_FIELDS%>" value="<%=name%>" maxlength="128">
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.SENDER_LOGIN_LABEL%></b><br>
                    <select name="<%=AfsServletUtils.DB_SEARCH_SENDER_LOGIN_FIELDS%>">
                        <%=HtmlUtils.buildComboOptionsListFromVectorOfHashtable(senderLoginList, senderLogin, AfsServletUtils.DB_SEARCH_SENDER_LOGIN_FIELDS, AfsServletUtils.DB_SEARCH_SENDER_LOGIN_FIELDS, true, "", "")%>
                    </select>
                </td>
            </tr>
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.TP_RELEASE_LABEL%></b><br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_TP_RELEASE_FIELDS%>" value="<%=tpRelease%>" maxlength="128">
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.TP_REVISION_LABEL%></b><br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_TP_REVISION_FIELDS%>" value="<%=tpRevision%>">
                </td>
            </tr>
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.FROM_PLANT_LABEL%></b><br>
                    <select name="<%=AfsServletUtils.DB_SEARCH_FROM_PLANT_FIELDS%>">
                        <%=HtmlUtils.buildComboOptionsListFromVectorOfHashtable(fromPlantList, fromPlant, AfsServletUtils.DB_SEARCH_FROM_PLANT_FIELDS, AfsServletUtils.DB_SEARCH_FROM_PLANT_FIELDS, true, "", "")%>
                    </select>
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.DESTINATION_PLANT_LABEL%></b><br>
                    <select name="<%=AfsServletUtils.DB_SEARCH_DESTINATION_PLANT_FIELDS%>">
                        <%=HtmlUtils.buildComboOptionsListFromVectorOfHashtable(destinationPlantList, destinationPlant, AfsServletUtils.DB_SEARCH_DESTINATION_PLANT_FIELDS, AfsServletUtils.DB_SEARCH_DESTINATION_PLANT_FIELDS, true, "", "")%>
                    </select>
                </td>
            </tr>
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.FIRST_RECIEVE_LOGIN_LABEL%></b><br>
                    <select name="<%=AfsServletUtils.DB_SEARCH_FIRST_RECIEVE_LOGIN_FIELDS%>">
                        <%=HtmlUtils.buildComboOptionsListFromVectorOfHashtable(firstRecieversList, firstRecieveLogin, AfsServletUtils.DB_SEARCH_FIRST_RECIEVE_LOGIN_FIELDS, AfsServletUtils.DB_SEARCH_FIRST_RECIEVE_LOGIN_FIELDS, true, "", "")%>
                    </select>
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.SECOND_RECIEVE_LOGIN_LABEL%></b><br>
                    <select name="<%=AfsServletUtils.DB_SEARCH_FIRST_RECIEVE_LOGIN_FIELDS%>">
                        <%=HtmlUtils.buildComboOptionsListFromVectorOfHashtable(secondRecieversList, secondRecieveLogin, AfsServletUtils.DB_SEARCH_SECOND_RECIEVE_LOGIN_FIELDS, AfsServletUtils.DB_SEARCH_SECOND_RECIEVE_LOGIN_FIELDS, true, "", "")%>
                    </select>
                </td>
            </tr>
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.STATUS_LABEL%></b><br>
                    <select name="">
                        <option value="" <%=(GeneralStringUtils.isEmptyString(status)? "selected" : "")%>></option>
                        <option value="<%=PacketConstants.SENT_PACKET_STATUS%>" <%=(status.equals(PacketConstants.SENT_PACKET_STATUS)? "selected" : "")%>><%=PacketConstants.SENT_PACKET_STATUS%></option>
                        <option value="<%=PacketConstants.EXTRACTED_PACKET_STATUS%>" <%=(status.equals(PacketConstants.EXTRACTED_PACKET_STATUS)? "selected" : "")%>><%=PacketConstants.EXTRACTED_PACKET_STATUS%></option>
                    </select>
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.EXTRACTION_LOGIN_LABEL%></b><br>
                    <select name="<%=AfsServletUtils.DB_SEARCH_EXTRACTION_LOGIN_FIELDS%>">
                        <%=HtmlUtils.buildComboOptionsListFromVectorOfHashtable(extractionLoginList, extractionLogin, AfsServletUtils.DB_SEARCH_EXTRACTION_LOGIN_FIELDS, AfsServletUtils.DB_SEARCH_EXTRACTION_LOGIN_FIELDS, true, "", "")%>
                    </select>
                </td>
            </tr>
            <tr><td class=testo colspan="6" align="center"><b><%=PacketConstants.SENT_DATE_LABEL%></b></td></tr>
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    From (dd/mon/yyyy)<br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS%>" value="<%=sentDateFrom%>">&nbsp;<a href="javascript: openCalendar('<%=AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS%>','<%=AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS%>')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    To (dd/mon/yyyy)<br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS%>" value="<%=sentDateTo%>">&nbsp;<a href="javascript: openCalendar('<%=AfsServletUtils.DB_SEARCH_SENT_DATE_FROM_FIELDS%>','<%=AfsServletUtils.DB_SEARCH_SENT_DATE_TO_FIELDS%>')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                </td>
            </tr>
            <tr><td class=testo colspan="6" align="center"><b><%=PacketConstants.EXTRACTION_DATE_LABEL%></b></td></tr>
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    From (dd/mon/yyyy)<br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS%>" value="<%=extractionDateFrom%>">&nbsp;<a href="javascript: openCalendar('<%=AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS%>','<%=AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS%>')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    To (dd/mon/yyyy)<br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS%>" value="<%=extractionDateTo%>">&nbsp;<a href="javascript: openCalendar('<%=AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_FROM_FIELDS%>','<%=AfsServletUtils.DB_SEARCH_EXTRACTION_DATE_TO_FIELDS%>')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                </td>
            </tr>
            <tr><td class=testo colspan="6" align="center"><b><%=PacketConstants.LAST_ACTION_DATE_LABEL%></b></td></tr>
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    From (dd/mon/yyyy)<br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS%>" value="<%=lastActionDateFrom%>">&nbsp;<a href="javascript: openCalendar('<%=AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS%>','<%=AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS%>')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    To (dd/mon/yyyy)<br>
                    <input type="text" name="<%=AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS%>" value="<%=lastActionDateTo%>">&nbsp;<a href="javascript: openCalendar('<%=AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_FROM_FIELDS%>','<%=AfsServletUtils.DB_SEARCH_LAST_ACTION_DATE_TO_FIELDS%>')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                </td>
            </tr>
            <tr>
                <td colspan="6"><img src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
            </tr>
            <tr >
                <td colspan="6"><img src="<%=contextPath%>/img/pix_nero.gif" width="100%" height="1" alt="" border="0"></td>
            </tr>
            <tr>
                <td align="right" colspan="6">
                    <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                        <TR>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                            <TD valign="center" align="right" background="<%=contextPath%>/img/btn_center.gif"
                                class="testo"><A CLASS="BUTTON" HREF="javascript:document.searchPackets.submit();">SUBMIT</a></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                        </TR>
                    </TABLE>

                </td>
            </tr>
            <tr>
                <td colspan="6"><img src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
            </tr>
</TABLE>


</TD>
</TR>
</form>


<%@ include file="/bottom.jsp" %>
</BODY>
</HTML>
