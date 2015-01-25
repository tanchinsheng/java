<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
    isErrorPage="false"
    errorPage="uncaughtErr.jsp"
%>
<%
    String time = new Long(System.currentTimeMillis()).toString();
    String reqId=request.getParameterValues("reqId")[0];
    String repFileName=request.getParameterValues("repFileName")[0];
    String actionTxt=(String)request.getParameterValues("actionTxt")[0];
    String refreshTime=(String)request.getParameterValues("refreshTime")[0];
    String webAppDir=getServletContext().getInitParameter("webAppDir");

    boolean startBool=(session.getValue("startBool"+"_"+reqId)==null ? false : ((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue());
    if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
%>

<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function viewLs()
  {
    document.viewLsForm.submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" >
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="NEW LINESET INSERTION "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>

           <FORM name="viewLsForm" action="lsVobQryServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="vob_lineset_submits">
           </FORM>
   <%
      String msgType   = "OK";
      actionTxt        = "LINESET ACTION STARTED";
      String msgTxt    = "WHEN THE ACTION WILL BE COMPLETED YOU WILL BE INFORMED BY AN EMAIL!";
      String detailTxt = "YOU CAN VERIFY ACTION PROGRESS BY MEANS OF 'Lineset In Progress' QUERY ";
      String sysDetailTxt = "";
      boolean backBut  = false;
   %>
   <%@ include file="msg_top.jsp" %>

		<tr>
            <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
            <td align="center" valign="bottom">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:viewLs()">&nbsp;LINESET IN PROGRESS</TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
               </TR>
              </TABLE>
           </td>
           <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
 		</tr>


   <%@ include file="msg_bottom.jsp" %>

<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
