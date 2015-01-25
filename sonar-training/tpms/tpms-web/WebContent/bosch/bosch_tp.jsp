<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="it.txt.tpms.bosch.servlets.TpReportServlet"%>
<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="java.util.Vector"%>
<%@ page import="it.txt.general.utils.HtmlUtils"%>
<%@ page import="it.txt.tpms.servlets.DbTpListForTpCommentsServlet"%>
<%@ page isErrorPage="false" errorPage="/uncaughtErr.jsp"%>
<%
    String contextPath = request.getContextPath();

    String division = request.getParameter(TpReportServlet.DIVISION_FIELD_NAME);
    String lsFilterId = request.getParameter( TpReportServlet.LINESET_FILTER_FIELD_NAME);
    String fromPlant = request.getParameter(TpReportServlet.FROM_PLANT_FIELD_NAME);
    String toPlant = request.getParameter(TpReportServlet.TO_PLANT_FIELD_NAME);
    String productLine = GeneralStringUtils.isEmptyString(request.getParameter(TpReportServlet.PRODUCT_LINE_FIELD_NAME))? "" : request.getParameter(TpReportServlet.PRODUCT_LINE_FIELD_NAME);
    String jobName = GeneralStringUtils.isEmptyString(request.getParameter(TpReportServlet.JOBNAME_FIELD_NAME)) ? "" : request.getParameter(TpReportServlet.JOBNAME_FIELD_NAME);
    String lastActionDateFrom = GeneralStringUtils.isEmptyString(request.getParameter(TpReportServlet.LAST_ACTION_FROM_FIELD_NAME)) ? "" : request.getParameter(TpReportServlet.LAST_ACTION_FROM_FIELD_NAME);
    String lastActionDateTo = GeneralStringUtils.isEmptyString(request.getParameter(TpReportServlet.LAST_ACTION_TO_FIELD_NAME)) ? "" : request.getParameter(TpReportServlet.LAST_ACTION_TO_FIELD_NAME);

    TPList reportTpList = (TPList) session.getAttribute(TpReportServlet.TP_LIST_SESSION_ATTRIBUTE_NAME);
    Vector divisionList = (Vector) session.getAttribute(TpReportServlet.DIVISION_LIST_SESSION_ATTRIBUTE_NAME);
    Vector fromPlantList = (Vector) session.getAttribute(TpReportServlet.FROM_PLANT_LIST_SESSION_ATTRIBUTE_NAME);
    Vector toPlantList = (Vector) session.getAttribute(TpReportServlet.DESTINATION_PLANT_LIST_SESSION_ATTRIBUTE_NAME);
    Vector userLinesetFilter = (Vector) session.getAttribute( TpReportServlet.USER_LINESET_FILTERS_SESSION_ATTRIBUTE_NAME );

%>
<HTML>
<HEAD>
    <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <script type="text/javascript" language="javascript" src="<%=contextPath%>/js/CalendarUtils.js"></script>
    <script type="text/javascript" language="javascript">
        function showTPFullComment(tpId){
            var strUrl = "<%=contextPath%>/dbTpListForTpComments?action=<%=DbTpListForTpCommentsServlet.SHOW_TP_COMMENT_ACTION%>&<%=DbTpListForTpCommentsServlet.TP_ID_FIELD_NAME%>=" + tpId;
            var newWin = window.open(strUrl, "tp_details", "width=700,height=400,resizable=yes,scrollbars=yes,status=0,location=no,menubar=no");
            newWin.focus();
        }

        function submitForm(){
            document.forms["tpSearchForm"].submit();
        }

        function exportReportResult(type) {

        }

        function printView(){
            var objForm = document.forms["tpReportExport"];
            objForm.elements["action"].value = '<%=TpReportServlet.EXPORT_TO_HTML_ACTION%>';
            objForm.submit();
        }

        function getCsv() {
            var objForm = document.forms["tpReportExport"];
            objForm.elements["action"].value = '<%=TpReportServlet.EXPORT_TO_CSV_ACTION%>';
            objForm.submit();

        }
    </script>
    <LINK href="<%=contextPath%>/default.css" type="text/css" rel="stylesheet">
</HEAD>
<BODY bgColor="#FFFFFF">
<form action="<%=contextPath%>/acerboTpReport" name="tpReportExport" method="get" target="_blank">
    <input type="hidden" name="action" value="">
    <input type="hidden" name="<%=TpReportServlet.JOBNAME_FIELD_NAME%>" value="<%=jobName%>">
    <input type="hidden" name="<%=TpReportServlet.PRODUCT_LINE_FIELD_NAME%>" value="<%=productLine%>">
    <input type="hidden" name="<%=TpReportServlet.FROM_PLANT_FIELD_NAME%>" value="<%=fromPlant%>">
    <input type="hidden" name="<%=TpReportServlet.TO_PLANT_FIELD_NAME%>" value="<%=toPlant%>">
    <input type="hidden" name="<%=TpReportServlet.DIVISION_FIELD_NAME%>" value="<%=division%>">
    <input type="hidden" name="<%=TpReportServlet.LAST_ACTION_FROM_FIELD_NAME%>" value="<%=lastActionDateFrom%>">
    <input type="hidden" name="<%=TpReportServlet.LAST_ACTION_TO_FIELD_NAME%>" value="<%=lastActionDateTo%>">
    <input type="hidden" name="status" value="<%=(String)request.getAttribute("status") %>">
    
</form>

<% boolean repBool=TpReportServlet.showExportIcons(session); boolean csvRepBool=TpReportServlet.showExportIcons(session); 
String tStatus = (String)request.getAttribute("status");
String pageTitle = "";

if (tStatus.equals("In_Production")) pageTitle = "TP IN PRODUCTION";
if (tStatus.equals("In_Validation")) pageTitle = "TP IN VALIDATION";
if (tStatus.equals("Ready_to_production")) pageTitle = "TP READY TO PRODUCTION";
 %>
<%@ include file="/top.jsp" %>
<tr>
    <td>&nbsp</td>
</tr>
<%if (!GeneralStringUtils.isEmptyString(request.getParameter("action")) && request.getParameter("action").equals(TpReportServlet.PERFORM_SEARCH_ACTION)) {%>
<tr>
    <td>
        <table cellpadding="0" cellspacing="1" border="0" >
            <tr bgcolor="#c9e1fa">
                <td class="testoH"><b>Job name</b></td>
                <td class="testoH"><b>Rel</b></td>
                <td class="testoH"><b>Rev</b></td>
                <td class="testoH"><b>Ver</b></td>
                <td class="testoH"><b>Facility</b></td>
                <td class="testoH"><b>Tester Info</b></td>
                <td class="testoH"><b>Last act Actor</b></td>
                <td class="testoH"><b>Delivery date</b></td>
                <td class="testoH"><b>Last act date</b></td>
                <td class="testoH"><b>Status</b></td>
                <td class="testoH"><b>Division</b></td>
                <td class="testoH"><b>Dest plant</b></td>
                <td class="testoH"><b>From plant</b></td>
                <td class="testoH"><b>Comment</b></td>
            </tr>
            <%if (reportTpList != null && !reportTpList.isEmpty()) {
                TP tmpTp;
                while(reportTpList.hasNext()) {
                    tmpTp = reportTpList.next();
            %>
            <tr>
                <td align="left" class="testoD"><%=tmpTp.getJobName()%></td>
                <td align="left" class="testoD"><%=tmpTp.getJobReleaseDisplayFormat()%></td>
                <td align="left" class="testoD"><%=tmpTp.getJobRevision()%></td>
                <td align="left" class="testoD"><%=tmpTp.getTpmsVersion()%></td>
                <td align="left" class="testoD"><%=tmpTp.getFacility()%></td>
                <%  
                String TesterInfo=tmpTp.getTesterInfo();

                if (tmpTp.getTesterInfo() !=null)

                {

                String value=tmpTp.getTesterInfo();
		
                           	int indexBeginValue = value.indexOf(" ");
		if(indexBeginValue != -1){
		
                String second_part=value.substring(indexBeginValue+1,value.length());

                int indexEndValue = second_part.indexOf(" ");

                if (indexEndValue == -1)                	

                {
                	indexEndValue=second_part.length();
				}

                TesterInfo = value.substring(0, indexBeginValue)+" "+ second_part.substring(0,indexEndValue);

                }else{
		TesterInfo=tmpTp.getTesterInfo();
		}
		}
                %>
                <td align="left" class="testoD"><%=TesterInfo%></td>
                <td align="left" class="testoD"><%=tmpTp.getLastActionActor()%></td>
                <td align="left" class="testoD"><%=tmpTp.getFormattedDistributionDate()%></td>
                <td align="left" class="testoD"><%=tmpTp.getFormattedLastActionDate()%></td>
                <td align="left" class="testoD"><%=tmpTp.getStatus()%></td>
                <td align="left" class="testoD"><%=tmpTp.getDivision()%></td>
                <td align="left" class="testoD"><%=tmpTp.getToPlant()%></td>
                <td align="left" class="testoD"><%=tmpTp.getFromPlant()%></td>
                <td align="left" class="testoDSmall"><a href="javascript: showTPFullComment('<%=tmpTp.getId()%>')"><%=tmpTp.getShortHtmlDisplayComments()%></a>&nbsp;</td>
            </tr>
            <% }
            } else  {%>
            <tr>
                <td colspan="13" align="center">
                    No TPs found with given criteria
                </td>
            </tr>
            <%}%>
        </table>
    </td>
</tr>
<%}%>
<tr>
    <td>&nbsp</td>
</tr>
<tr>
    <td><img src="<%=contextPath%>/img/pix_nero.gif" width="680" height="1" alt="" border="0"></td>
</tr>
<tr>
    <td>&nbsp</td>
</tr>
<tr>
    <td>
        <table cellpadding="0" cellspacing="0" border="0">
            <form action="<%=contextPath%>/acerboTpReport" name="tpSearchForm" method="post">
                <input type="hidden" name="action" value="<%=TpReportServlet.PERFORM_SEARCH_ACTION%>">
                <!--search fields table-->
                <tr>
                    <td>&nbsp;</td>
                      <input type="hidden" name="status" value="<%=(String)request.getAttribute("status") %>">
                     <td class=testo width="40%">
                        <b>Job name (PRIS) %</b><br>
                        <input type="text" name="<%=TpReportServlet.JOBNAME_FIELD_NAME%>" value="<%=jobName%>">
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td class=testo width="40%">
                        <b>Product Line %</b><br>
                        <input type="text" name="<%=TpReportServlet.PRODUCT_LINE_FIELD_NAME%>" value="<%=productLine%>">
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class=testo width="40%">
                        <b>From plant</b><br>
                        <select name="<%=TpReportServlet.FROM_PLANT_FIELD_NAME%>">
                            <%=HtmlUtils.buildComboOptionsList(fromPlantList, fromPlant)%>
                        </select>
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td class=testo width="40%">
                        <b>Destination plant</b><br>
                        <select name="<%=TpReportServlet.TO_PLANT_FIELD_NAME%>">
                            <%=HtmlUtils.buildComboOptionsList(toPlantList, toPlant)%>
                        </select>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class=testo width="40%">
                        <b>Division</b><br>
                        <select name="<%=TpReportServlet.DIVISION_FIELD_NAME%>">
                            <%=HtmlUtils.buildComboOptionsList(divisionList, division)%>
                        </select>
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td class=testo width="40%">
                        <b>Lineset filter</b><br>
                        <select name="<%=TpReportServlet.LINESET_FILTER_FIELD_NAME%>">
                            <%=HtmlUtils.buildComboOptionsList(userLinesetFilter, lsFilterId)%>
                        </select>
                        &nbsp;
                    </td>
                </tr>
                <tr><td colspan="6" height="5"></td></tr>
                <tr><td class=testo colspan="6" align="center"><b>Last action date</b></td></tr>
                <!-- FIELDS ROW -->
                <tr>
                    <!-- LEFT COLUMN SEPARATOR -->
                    <td>&nbsp;</td>
                    <!-- /LEFT COLUMN SEPARATOR -->
                    <td class=testo width="40%">
                        From (dd/mon/yyyy)<br>
                        <input type="text" name="<%=TpReportServlet.LAST_ACTION_FROM_FIELD_NAME%>" value="<%=lastActionDateFrom%>">&nbsp;<a href="javascript: openCalendar('<%=TpReportServlet.LAST_ACTION_FROM_FIELD_NAME%>','<%=TpReportServlet.LAST_ACTION_TO_FIELD_NAME%>', '<%=contextPath%>', 'tpSearchForm')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                    </td>
                    <!-- COLUMN SEPARATOR -->
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <!-- /COLUMN SEPARATOR -->
                    <td class=testo width="40%">
                        To (dd/mon/yyyy)<br>
                         <input type="text" name="<%=TpReportServlet.LAST_ACTION_TO_FIELD_NAME%>" value="<%=lastActionDateTo%>">&nbsp;<a href="javascript: openCalendar('<%=TpReportServlet.LAST_ACTION_FROM_FIELD_NAME%>','<%=TpReportServlet.LAST_ACTION_TO_FIELD_NAME%>', '<%=contextPath%>', 'tpSearchForm')"><img alt="" src="<%=contextPath%>/img/ico_calendar.gif" border="0"></a>
                    </td>
                </tr>
                      <tr><td colspan="5">&nbsp;</td></tr>
                <tr>
                  <td>&nbsp;</td>
                  <td colspan="4">&nbsp% = Wild character</td>
                </tr>
            </form>
         </table>
    </td>
</tr>
<tr>
    <td>&nbsp</td>
</tr>
<TR>
    <td>
<!-- BUTTONS -->
        <table cellpadding="0" cellspacing="0" border="0" width="680">
            <tr>
              <td colspan="1"><img src="<%=contextPath%>/img/pix_nero.gif" width="680" height="1" alt="" border="0"></td>
            </tr>
        </table>
        <table width="680">
            <tr>
                <td align="right">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">
                                <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                                    <TR>
                                       <TD valign="center"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                                       <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;</a></TD>
                                       <TD valign="center" ><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                                    </TR>
                                </TABLE>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
<!-- /BUTTONS -->
    </TD>
</TR>
<%@ include file="/bottom.jsp" %>
</BODY>
</HTML>