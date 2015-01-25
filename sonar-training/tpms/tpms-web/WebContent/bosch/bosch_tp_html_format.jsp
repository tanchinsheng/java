<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="it.txt.tpms.bosch.servlets.TpReportServlet"%>
<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    TPList reportTpList = (TPList) session.getAttribute(TpReportServlet.TP_LIST_SESSION_ATTRIBUTE_NAME);
%>
<html>
    <head>
        <title>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %>: TPs <%=((String)request.getAttribute("status")).replace('_', ' ') %>: html view </title>
        <LINK href="<%=contextPath%>/default_print.css" type=text/css rel=stylesheet>
    </head>
  <body>
  <center>
    <TABLE BORDER="0">
        <tr><td align="center"><IMG alt="" SRC="<%=contextPath%>/img/st-rep-logo.gif" BORDER="0"></td></tr>
        <TR>
            <TD ALIGN="CENTER">
                <FONT class="titverdibig">
                  <b>Custom TP <%=((String)request.getAttribute("status")).replace('_', ' ') %> Report</b><BR><BR>
                   TPMS/W host=<%=TpReportServlet.getInstallationName()%> &nbsp; User=<%=session.getAttribute("user")%><BR><BR>
                   <%=TpReportServlet.getFormattedDate()%>
                </FONT>
             </TD>
        </TR>
        <TR>
            <TD colspan="2"><IMG alt="" SRC="<%=contextPath%>/img/pix.gif" height="8"></TD>
        </TR>
    </TABLE>
    <TABLE BORDER="0">
        <TR>
            <TD ALIGN="CENTER">
                <FONT class="titverdibig">Selection Criteria:<%=TpReportServlet.getFormattedSeachCriteria()%></FONT>
            </td>
        </tr>
    </TABLE>
    <table cellspacing="1" cellpadding="2" border="1" width="100%">
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
                <td class="testoH"><b>Production Area</b></td>
                <td class="testoH"><b>Comment</b></td>
            </tr>
            <%if (reportTpList != null && !reportTpList.isEmpty()) {
                TP tmpTp;
                reportTpList.rewind();
                while(reportTpList.hasNext()) {
                    tmpTp = reportTpList.next();
            %>
            <tr>
                <td align="left" class="testoD"><%=tmpTp.getJobName()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getJobReleaseDisplayFormat()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getJobRevision()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getTpmsVersion()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getFacility()%>&nbsp;</td>
                <%  String TesterInfo=tmpTp.getTesterInfo();

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
                <td align="left" class="testoD"><%=tmpTp.getLastActionActor()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getFormattedDistributionDate()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getFormattedLastActionDate()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getStatus()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getDivision()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getToPlant()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getFromPlant()%>&nbsp;</td>
                <td align="left" class="testoD"><%=tmpTp.getProductionAreaDisplayValue()%>&nbsp;</td>
                <td align="left" class="testoDSmall"><%=tmpTp.getHtmlDisplayComments()%>&nbsp;</td>
            </tr>
            <% }
            }%>

    </table>
      </center>
</body>
</html>