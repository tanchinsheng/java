<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="it.txt.tpms.bosch.servlets.TpReportServlet"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<HTML>
<HEAD>
 <TITLE></TITLE>
</HEAD>
<BODY bgColor="#FFFFFF">
<PRE>
"Job name","Job release","Job revision","Tpms version","Facility","Tester Info","Last action actor","Delivery date","Last action date","Status","Division","Destination plant","From plant","Production Area","Comment"<%
TPList reportTpList = (TPList) session.getAttribute(TpReportServlet.TP_LIST_SESSION_ATTRIBUTE_NAME);
if (reportTpList != null && !reportTpList.isEmpty()) {
    TP tmpTp;
    reportTpList.rewind();
    while(reportTpList.hasNext()) {
        tmpTp = reportTpList.next();
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
"<%=tmpTp.getJobName()%>","<%=tmpTp.getJobReleaseDisplayFormat()%>","<%=tmpTp.getJobRevision()%>","<%=tmpTp.getTpmsVersion()%>","<%=tmpTp.getFacility()%>","<%= TesterInfo%>","<%=tmpTp.getLastActionActor()%>","<%=tmpTp.getFormattedDistributionDate()%>","<%=tmpTp.getFormattedLastActionDate()%>","<%=tmpTp.getStatus()%>","<%=tmpTp.getDivision()%>","<%=tmpTp.getToPlant()%>","<%=tmpTp.getFromPlant()%>","<%=tmpTp.getProductionAreaDisplayValue()%>","<%=tmpTp.getNoNewlineDisplayComments()%>"<%      }
}%>
</PRE>
</BODY>
</HTML>