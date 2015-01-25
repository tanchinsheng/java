<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="it.txt.tpms.servlets.DbTpListForTpCommentsServlet"%>
<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="it.txt.general.utils.HtmlUtils"%>
<%@ page import="tpms.utils.QueryUtils"%>
<%@ page isErrorPage="false" errorPage="/uncaughtErr.jsp"%>
<%
    String contextPath = request.getContextPath();
    TPList myTPList = (TPList) session.getAttribute(DbTpListForTpCommentsServlet.TP_LIST_SESSION_ATTRIBUTE_NAME);
    String selectedLine = request.getParameter(DbTpListForTpCommentsServlet.LINE_SEARCH_FIELD);
    if(GeneralStringUtils.isEmptyString(selectedLine)){
        selectedLine = "";
    }
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
    <script type="text/javascript" language="javascript">
        function modifyTpsComment(){
            var objForm = document.forms["tpsCommentForm"];
            var tpCheckboxes = objForm.elements["<%=DbTpListForTpCommentsServlet.TP_ID_FIELD_NAME%>"];
            var doSubmit = false;
            if (tpCheckboxes != null) {
                if (!isNaN(tpCheckboxes.length)) {
                    for (var i = 0; i < tpCheckboxes.length; i++) {
                        if (tpCheckboxes[i].checked){
                            //fai il submit della form...
                                doSubmit = true;
                            break;
                        }
                    }
                } else {
                    if (tpCheckboxes.checked) {
                        doSubmit = true;
                    }
                }
            }
            if (tpCheckboxes != null) {
                if (doSubmit) {
                    objForm.submit();
                } else {
                    alert ("At least one tp should be selected in order to add comment.");
                }
            } else {
           	    alert ("Perform a search in order to view TPs\nand select one or more of them in order to modify comments");
            }
        }

        function showTPFullComment(tpId){
            var strUrl = "<%=contextPath%>/dbTpListForTpComments?action=<%=DbTpListForTpCommentsServlet.SHOW_TP_COMMENT_ACTION%>&<%=DbTpListForTpCommentsServlet.TP_ID_FIELD_NAME%>=" + tpId;
            var newWin = window.open(strUrl, "tp_details", "width=700,height=400,resizable=yes,scrollbars=yes,status=0,location=no,menubar=no");
            newWin.focus();
        }

        function getCsv() {
            var strUrl = "<%=contextPath%>/dbTpListForTpComments?action=<%=DbTpListForTpCommentsServlet.EXPORT_TO_CSV_ACTION%>";
            var newWin = window.open(strUrl, "tp_csv_export", "width=700,height=400,resizable=yes,scrollbars=yes,status=0,location=no,menubar=yes");
            newWin.focus();
        }

        function searchMyTPs(){
            var objForm =  document.forms["searchMyTPForm"];
            var selIndex = objForm.elements["<%=DbTpListForTpCommentsServlet.LINE_SEARCH_FIELD%>"].selectedIndex;
            var selValue =objForm.elements["<%=DbTpListForTpCommentsServlet.LINE_SEARCH_FIELD%>"].options[selIndex].value;
            if (isEmptyString(selValue)) {
                var message = "No value selected for line field:\nthis search may take a while\n\nDo you want to continue?"
                if (confirm(message)){
                    objForm.submit();
                }
            } else {
                objForm.submit();
            }
        }
    </script>
</HEAD>
<LINK href="<%=contextPath%>/default.css" type=text/css rel=stylesheet>

<BODY bgColor="#FFFFFF">
<form action="<%=contextPath%>/dbTpListForTpComments" name="exportTpCommentForm" method="post" target="_blank">
    <input type="hidden" name="action" value="<%=DbTpListForTpCommentsServlet.EXPORT_TO_CSV_ACTION%>">
</form>
<% boolean repBool = false; boolean csvRepBool = DbTpListForTpCommentsServlet.showExportButtons(session); String pageTitle="TP COMMENTS"; %>
<%@ include file="/top.jsp" %>

<TR>
    <TD>
        <table cellpadding="0" cellspacing="0" border="0" >
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td width="4"><img src="<%=contextPath%>/img/tit_sx_file.gif" width="4" height="21" alt="" border="0"></td>
                            <td background="img/tit_center_file.gif" align="center" class="titverdifl" nowrap><font color="#19559E" face="Courier"><%="TP List"%></font></td>
                            <td width="4"><img src="<%=contextPath%>/img/tit_dx_file.gif" width="4" height="21" alt="" border="0"></td>
                            <td><img src="<%=contextPath%>/img/pix.gif" width="680" height="18" alt="" border="0"></td>
                        </tr>
                        <tr>
                            <td colspan="4"><img src="<%=contextPath%>/img/pix_grey.gif" width="680" height="1" alt="" border="0"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </TD>
</TR>
<tr>
    <td>
        <%if (QueryUtils.checkCtrlServletDBConnection()) {%>
        <form action="<%=contextPath%>/dbTpListForTpComments" name="tpsCommentForm" method="post">
            <input type="hidden" name="action" value="<%=DbTpListForTpCommentsServlet.MODIFY_TPS_COMMENT_ACTION%>">
            <input type="hidden" name="<%=DbTpListForTpCommentsServlet.LINE_SEARCH_FIELD%>" value="<%=selectedLine%>">
        <table cellpadding="0" cellspacing="1" border="0" >
            <tr bgcolor="#c9e1fa">
                <td class="testoH">&nbsp;</td>
                <td class="testoH"><b>Job name</b></td>
                <td class="testoH"><b>Rel</b></td>
                <td class="testoH"><b>Rev</b></td>
                <td class="testoH"><b>Ver</b></td>
                <td class="testoH"><b>Delivery date</b></td>
                <td class="testoH"><b>Prod Line</b></td>
                <td class="testoH"><b>Comment</b></td>
            </tr>
            <%

             if (myTPList != null && !myTPList.isEmpty()) {
                myTPList.rewind();
                TP tmpTp;
                while(myTPList.hasNext()) {
                    tmpTp = myTPList.next();
            %>
            <tr>
                <td align="left" class="testoD"><INPUT TYPE="CHECKBOX" NAME="<%=DbTpListForTpCommentsServlet.TP_ID_FIELD_NAME%>" value="<%=tmpTp.getId()%>"></td>
                <td align="left" class="testoD"><%=tmpTp.getJobName()%></td>
                <td align="left" class="testoD"><%=tmpTp.getJobReleaseDisplayFormat()%></td>
                <td align="left" class="testoD"><%=tmpTp.getJobRevision()%></td>
                <td align="left" class="testoD"><%=tmpTp.getTpmsVersion()%></td>
                <td align="left" class="testoD"><%=tmpTp.getFormattedDistributionDate()%></td>
                <td align="left" class="testoD"><%=tmpTp.getLine()%></td>
                <td align="left" class="testoDSmall"><a href="javascript: showTPFullComment('<%=tmpTp.getId()%>')"><%=tmpTp.getShortHtmlDisplayComments()%></a>&nbsp;</td>
            </tr>
            <% }
            } else  {
                //se la lista fosse vuota....
            }%>
        </table>
       </form>
        <table cellpadding="0" cellspacing="0" border="0" width="560">
            <tr>
              <td colspan="1"><img src="<%=contextPath%>/img/pix_nero.gif" width="680" height="1" alt="" border="0"></td>
            </tr>
        </table>

       <form action="<%=contextPath%>/dbTpListForTpComments" name="searchMyTPForm" method="post">
            <table cellpadding="0" cellspacing="1" border="0" >
                <tr>
                    <td>
                       <input type="hidden" name="action" value="<%=DbTpListForTpCommentsServlet.SEARCH_MY_TP_ACTION%>">
                       <b>Line:</b>&nbsp;&nbsp;
                       <select name="<%=DbTpListForTpCommentsServlet.LINE_SEARCH_FIELD%>">
                           <option value=""></option>
                           <%=HtmlUtils.buildComboOptionsList(UserUtils.getUserTPPlantLineValues((String) session.getAttribute("user")), selectedLine)%>
                       </select>
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                            <TR>
                               <TD valign="center"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                               <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:searchMyTPs()">&nbsp;SEARCH&nbsp;</a></TD>
                               <TD valign="center" ><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                            </TR>
                        </TABLE>
                    </td>
                </tr>
            </table>
       </form>
        <%} else {%>
        <center>Database connection not available, kindly advice your system administrator.</center>
        <%}%>
    </td>
</tr>

<TR>
    <td>
<!-- BUTTONS -->
        <table cellpadding="0" cellspacing="0" border="0" width="560">
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
                                       <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:modifyTpsComment()">&nbsp;MODIFY COMMENTS&nbsp;</a></TD>
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