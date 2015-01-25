<%@ page isErrorPage="false" errorPage="uncaughtErr.jsp"%>
<%
    String time = Long.toString(System.currentTimeMillis());
    String reqId=request.getParameter("reqId");
    String listRepFileName=request.getParameter("listRepFileName");
    String linesetName=request.getParameter("FIELD.LINESET");
    String repFileName=request.getParameter("repFileName");
    String actionTxt=request.getParameter("actionTxt");
    String refreshTime=request.getParameter("refreshTime");
    boolean startBool=((Boolean)session.getAttribute("startBool"+"_"+reqId)).booleanValue();
    String webAppDir=config.getServletContext().getInitParameter("webAppDir");
    if ((!startBool)&&(session.getAttribute("exception"+"_"+reqId)!=null)) throw (Exception)session.getAttribute("exception"+"_"+reqId);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="<%=contextPath%>/default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function start()
  {
   <%
      if (startBool)
      {%>
       var delay = setTimeout("location.reload()",<%= refreshTime %>*1000);
    <%}
   %>
  }

  function viewTps()
  {
    self.document.forms["viewLsForm"].submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="NEW TP DELIVERY "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>
<%
   if (startBool)
   {%>
    <%@ include file="waitmsg.jsp" %>
 <%}
   else
   {%>
            <FORM name="viewLsForm" action="vobQryServlet" method="post">
                <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="vob_tp_to_process">
            </FORM>
   <%
      String msgType   = "OK";
      actionTxt        = "TP ACTION COMPLETED";
      String msgTxt    = "TP SUCCESSFULLY DELIVERED";
      String detailTxt = "";
      String sysDetailTxt = "";
      boolean backBut  = false;
   %>
   <%@ include file="msg_top.jsp" %>

		<tr>
            <td><img alt ="" src="<%=contextPath%>/img/tbl_sx2_big.gif" width="5" border="0"></td>
            <td align="center" valign="bottom">
     		  <!--TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="center"><img alt ="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" href="#" onClick="javascript:viewTps()">&nbsp;GOTO DISTRIBUTED TPs&nbsp;</a></TD>
                    <TD valign="center" ><img alt ="" src="<%=contextPath%>/img/btn_right.gif"></TD>
               </TR>
              </TABLE-->
           </td>
           <td><img alt ="" src="<%=contextPath%>/img/tbl_dx2_big.gif" width="5" border="0"></td>
 		</tr>


   <%@ include file="msg_bottom.jsp" %>
 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
