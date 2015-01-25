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
    boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
    String webAppDir=getServletContext().getInitParameter("webAppDir");
    if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
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

  function viewLs()
  {
    document.viewLsForm.submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="LINESET DELETE "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>
<%
   if (startBool)
   {%>
    <%@ include file="waitmsg.jsp" %>
 <%}
   else
   {%>
           <FORM name="viewLsForm" action="lsVobQryServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="vob_lineset_submits">
           </FORM>
   <%
      String msgType   = "OK";
      actionTxt        = "LINESET ACTION COMPLETED";
      String msgTxt    = "LINESET FILES SUCCESSFULLY DELETED";
      String detailTxt = "";
      String sysDetailTxt = "";
      boolean backBut  = false;
   %>
   <%@ include file="msg_top.jsp" %>

   <%@ include file="msg_bottom.jsp" %>
 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
