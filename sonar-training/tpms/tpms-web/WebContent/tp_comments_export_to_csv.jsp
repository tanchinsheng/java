<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="it.txt.tpms.servlets.DbTpListForTpCommentsServlet"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
</HEAD>
<BODY bgColor="#FFFFFF">
    <PRE>
"Job name","Rel","Rev","Version","Delivery date","Prod Line","Comment"<%
        TPList inProductionTpList = (TPList) session.getAttribute(DbTpListForTpCommentsServlet.TP_LIST_SESSION_ATTRIBUTE_NAME);
        if (inProductionTpList != null && !inProductionTpList.isEmpty()) {
            TP tmpTp;
            inProductionTpList.rewind();
            while(inProductionTpList.hasNext()) {
                tmpTp = inProductionTpList.next();
    %>
"<%=tmpTp.getJobName()%>","<%=tmpTp.getJobReleaseDisplayFormat()%>","<%=tmpTp.getJobRevision()%>","<%=tmpTp.getTpmsVersion()%>","<%=tmpTp.getLine()%>","<%=tmpTp.getNoNewlineDisplayComments()%>"<%      }
        }%>
    </PRE>
</BODY>
</HTML>