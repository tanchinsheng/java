<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="it.txt.tpms.servlets.DbTpListForTpCommentsServlet"%>
<%--
Created by IntelliJ IDEA.
User: furgiuele
Date: 19-mag-2006
Time: 13.02.39
To change this template use File | Settings | File Templates.
--%>
<%
    String contextPath = request.getContextPath();
    TP tp = (TP) request.getAttribute(DbTpListForTpCommentsServlet.TP_DATA_SELECTED_REQUEST_ATTRIBUTE_NAME);

%>
<HTML>
    <HEAD>
        <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
        <LINK href="<%=contextPath%>/default.css" type=text/css rel=stylesheet>
    </HEAD>
    <BODY bgColor="#FFFFFF">
        <table>
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0" width="650" >
                        <tr>
                            <td width="4"><img src="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                            <td background="<%=contextPath%>/img/tit_center.gif" align="center" class="titverdi" nowrap><b>&nbsp;TP Details&nbsp;</b></td>
                            <td width="4"><img src="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                            <td><img src="<%=contextPath%>/img/pix.gif" width="453" height="18" alt="" border="0"></td>
                        </tr>
                        <tr>
                            <td colspan="4"><img src="<%=contextPath%>/img/pix_grey.gif" width="650" height="1" alt="" border="0"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                    <table width="100%" cellspacing="1" cellpadding="2" border="0">
                        <tr>
                            <td width="40%" valign="top" class="testoH">
                                <b>Job name</b>
                            </td>
                            <td width="60%" valign="top" class="testoD">
                                <%=tp.getJobName()%>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%" valign="top" class="testoH">
                                <b>Job release</b>
                            </td>
                            <td width="60%" valign="top" class="testoD">
                                <%=tp.getJobReleaseDisplayFormat()%>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%" valign="top" class="testoH">
                                <b>Job revision</b>
                            </td>
                            <td width="60%" valign="top" class="testoD">
                                <%=tp.getJobRevision()%>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%" valign="top" class="testoH">
                                <b>Version</b>
                            </td>
                            <td width="60%" valign="top" class="testoD">
                                <%=tp.getTpmsVersion()%>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%" valign="top" class="testoH">
                                <b>Delivery date</b>
                            </td>
                            <td width="60%" valign="top" class="testoD">
                                <%=tp.getFormattedDistributionDate()%>
                            </td>
                        </tr>
                        <tr>
                            <td width="40%" valign="top" class="testoH">
                                <b>Product line</b>
                            </td>
                            <td width="60%" valign="top" class="testoD">
                                <%=tp.getLine()%>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <br>
                                <table border=1 RULES="NONE" FRAME="BOX" BORDERCOLORLIGHT="BLACK" BORDERCOLORDARK="BLACK" WIDTH="650">
                                    <tr>
                                        <td class="testoH"><b>Comment</b></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table  cellspacing="0" cellpadding="0" border="0">
                                                <tr><td><br><%=tp.getHtmlDisplayComments()%></td></tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                    <!-- BUTTONS -->
                    <table cellpadding="0" cellspacing="0" border="0" width="650">
                        <tr>
                          <td colspan="1"><img src="<%=contextPath%>/img/pix_nero.gif" width="650" height="1" alt="" border="0"></td>
                        </tr>
                    </table>
                    <table width="650">
                        <tr>
                            <td align="right">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td align="right">
                                            <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                                                <TR>
                                                   <TD valign="center"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                                                   <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:self.close()">&nbsp;&nbsp;&nbsp;CLOSE&nbsp;&nbsp;</a></TD>
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
        </table>

    </BODY>
</HTML>




<%--
  Created by IntelliJ IDEA.
  User: furgiuele
  Date: 19-mag-2006
  Time: 13.02.39
  To change this template use File | Settings | File Templates.

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Simple jsp page</title></head>
  <body>tp_show_comment.jsp</body>
</html>--%>