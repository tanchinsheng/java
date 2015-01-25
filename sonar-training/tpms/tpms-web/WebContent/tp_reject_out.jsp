<%@ page isErrorPage="false" errorPage="uncaughtErr.jsp"%>
<%  
  String reqId=request.getParameter("reqId");
  String actionTxt=request.getParameter("actionTxt");
  String refreshTime=request.getParameter("refreshTime");
  boolean startBool=((Boolean)session.getAttribute("startBool"+"_"+reqId)).booleanValue();
  if ((!startBool)&&(session.getAttribute("exception"+"_"+reqId)!=null)) throw (Exception)session.getAttribute("exception"+"_"+reqId);
  String repFileName=request.getParameter("repFileName");
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
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
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP EXTRACTION TO VALIDATE "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>
<%
 if (startBool)
 {%>
    <%@ include file="waitmsg.jsp" %>
 <%}
 else 
 {%>
   <%
    String msgType   = "OK";
    actionTxt        = "TP ACTION COMPLETED";
    String msgTxt    = "TP SUCCESSFULLY REJECTED";
    String detailTxt = "";
    String sysDetailTxt = "";
    boolean backBut  = false;
   %>
   <%@ include file="msg.jsp" %>
 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
