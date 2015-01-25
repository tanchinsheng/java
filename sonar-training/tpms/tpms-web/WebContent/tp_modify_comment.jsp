<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="it.txt.tpms.servlets.DbTpListForTpCommentsServlet"%>
<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page isErrorPage="false" errorPage="/uncaughtErr.jsp"%>
<%
    String contextPath = request.getContextPath();
    TPList selectedTps = (TPList) request.getAttribute(DbTpListForTpCommentsServlet.TP_LIST_SELECTED_FOR_COMMENTES_REQUEST_ATTRIBUTE_NAME);
    String errorMessage = (String) request.getAttribute(DbTpListForTpCommentsServlet.ERROR_MESSAGE);
    String comment = request.getParameter(DbTpListForTpCommentsServlet.TP_COMMENTS_FIELD_NAME) ;
    if (GeneralStringUtils.isEmptyString(comment))
        comment = "";
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
    <script type="text/javascript" language="javascript">

        function showMessage() {
            <%if (!GeneralStringUtils.isEmptyString(errorMessage)){%>
                alert("<%=GeneralStringUtils.escapedEncode(errorMessage)%>");
            <%}%>
        }

        function isValidComment(){
            var objForm = document.forms["tpsModifyCommentForm"];
            var commentText = objForm.elements["<%=DbTpListForTpCommentsServlet.TP_COMMENTS_FIELD_NAME%>"].value;
          
  			if (commentText != '') { 	
				if (filterSpecialChar(commentText) == false) {
     				alert("TP Comments contains invalid characters ~ , | ,\" and \' \nTP Comments must not be more than 2048 characters!");
         			return false;
      			} 
			}
        	return true; 
        }

        function saveComment(){
            if (isValidComment()){
                document.forms["tpsModifyCommentForm"].submit();
            }
        }

        function showTPFullComment(tpId){
            var strUrl = "<%=contextPath%>/dbTpListForTpComments?action=<%=DbTpListForTpCommentsServlet.SHOW_TP_COMMENT_ACTION%>&<%=DbTpListForTpCommentsServlet.TP_ID_FIELD_NAME%>=" + tpId;
            var newWin = window.open(strUrl, "tp_details", "width=700,height=400,resizable=yes,scrollbars=yes,status=0,location=no,menubar=no");
            newWin.focus();
        }
    </script>
</HEAD>
<LINK href="<%=contextPath%>/default.css" type=text/css rel=stylesheet>

<BODY bgColor="#FFFFFF" onLoad="showMessage()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP COMMENTS"; %>
<%@ include file="/top.jsp" %>

<TR>
    <TD>
        <table cellpadding="0" cellspacing="0" border="0" >
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td width="4"><img src="<%=contextPath%>/img/tit_sx_file.gif" width="4" height="21" alt="" border="0"></td>
                            <td background="<%=contextPath%>/img/tit_center_file.gif" align="center" class="titverdifl" nowrap><font color="#19559E" face="Courier">TPs</font></td>
                            <td width="4"><img src="<%=contextPath%>/img/tit_dx_file.gif" width="4" height="21" alt="" border="0"></td>
                            <td><img src="<%=contextPath%>/img/pix.gif" width="680" height="18" alt="" border="0"></td>
                        </tr>
                        <tr>
                            <td colspan="4"><img src="<%=contextPath%>/img/pix_grey.gif" width="610" height="1" alt="" border="0"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD ALIGN="LEFT">

        <table>
            <form action="<%=contextPath%>/dbTpListForTpComments" name="tpsModifyCommentForm" method="post">
            <input type="hidden" name="action" value="<%=DbTpListForTpCommentsServlet.SAVE_TPS_COMMENT_ACTION%>">
            <tr>
                <td>

                        <table cellpadding="0" cellspacing="1" border="0" >
                            <tr bgcolor="#c9e1fa">
                                <td class="testoH"><b>Job name</b></td>
                                <td class="testoH"><b>Rel</b></td>
                                <td class="testoH"><b>Rev</b></td>
                                <td class="testoH"><b>Ver</b></td>
                                <td class="testoH"><b>Delivery date</b></td>
                                <td class="testoH"><b>Prod line</b></td>
                                <td class="testoH"><b>Comment</b></td>
                            </tr>
                            <%if (selectedTps != null && !selectedTps.isEmpty()) {
                                TP tmpTp;
                                while(selectedTps.hasNext()) {
                                    tmpTp = selectedTps.next();
                            %>
                            <INPUT TYPE="hidden" NAME="<%=DbTpListForTpCommentsServlet.TP_ID_FIELD_NAME%>" value="<%=tmpTp.getId()%>">
                            <tr>
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

                </td>
            </tr>
            <tr>
                <td><img src="<%=contextPath%>/img/pix_nero.gif" width="610" height="1" alt="" border="0"></td>
            </tr>
            <tr>
                <td>
                    <textarea rows="15" cols="72" name="<%=DbTpListForTpCommentsServlet.TP_COMMENTS_FIELD_NAME%>" wrap="off"><%=comment%></textarea>
                </td>
            </tr>
          </form>
        </table>
    </TD>
</TR>
<TR>
    <td>
<!-- BUTTONS -->
        <table cellpadding="0" cellspacing="0" border="0" width="560">
            <tr>
              <td colspan="1"><img src="<%=contextPath%>/img/pix_nero.gif" width="610" height="1" alt="" border="0"></td>
            </tr>
        </table>
        <table width="610">
            <tr>
                <td align="right">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">
                                <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">

                                    <TR>
                                       <TD valign="center"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                                       <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:saveComment()">&nbsp;&nbsp;&nbsp;Save comments&nbsp;&nbsp;</a></TD>
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
