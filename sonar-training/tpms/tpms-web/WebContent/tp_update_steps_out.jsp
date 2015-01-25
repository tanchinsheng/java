<%@ page import="java.util.Properties"%>
<%@ page import="tol.xmlRdr"%>
<%@ page isErrorPage="false" errorPage="uncaughtErr.jsp"%>

<%  
  String time = Long.toString(System.currentTimeMillis());
  String reqId=request.getParameter("reqId");
  String repTitle="TP FILES MODIFICATIONS REPORT";
  String repFileName=request.getParameter("repFileName");
  String actionTxt=request.getParameter("actionTxt");
  String refreshTime=request.getParameter("refreshTime");
  boolean startBool=((Boolean)session.getAttribute("startBool"+"_"+reqId)).booleanValue();
  String webAppDir= config.getServletContext().getInitParameter("webAppDir");
  Properties xslProps=new Properties();
  String xslFname="tp_update_rep.xsl";
  String xslFileName=webAppDir+"/xsl/"+xslFname; 
  if ((!startBool)&&(session.getAttribute("exception"+"_"+reqId)!=null)) throw (Exception)session.getAttribute("exception"+"_"+reqId);
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

  function printView()
  {
    url="tp_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle, "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFname %>&status1Filter=&status2Filter=&userFilter=&repType=HTML&t=<%= time %>"
    window.open(url);
  }

  function submitTpFileDiff(file)
  {
        document.tpActionFormFileDiff.tpFile.value=file;
        document.tpActionFormFileDiff.submit();	
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=!startBool; boolean csvRepBool=false; String pageTitle="TP MARK AS VALIDATED "+(startBool ? "IN PROGRESS" : "SUCCEDED -> TP FILES MODIFICATIONS REPORT"); %>
<%@ include file="top.jsp" %>
<%
 if (startBool){%>
   <%@ include file="waitmsg.jsp" %>
 <%} else {%>
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td><img src="img/tit_sx2_big.gif" width="5"  alt="" border="0"></td>
                  <td background="img/tit_center_big.gif" align="center" class="titverdibig" width="550" nowrap><b>OK</b></td>
                  <td><img src="img/tit_dx2_big.gif" width="5"  alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="3"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title" align="center" width="550">&nbsp;</td>
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title" align="center" width="550"><FONT CLASS="title">TP ACTION COMPLETED</FONT></td>
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>
		<tr>
                  <td><img src="img/tbl_sx2_big.gif" width="5" border="0"></td>
		  <TD class="title_detail" align="center" width="550"><FONT CLASS="title">TP status successfully changed to "Ready for production"</FONT></td>
                  <td><img src="img/tbl_dx2_big.gif" width="5" border="0"></td>
		</tr>



		<tr>
                  <td><img src="img/tbl_sx2_corner_big.gif" width="5" border="0"></td>
                  <td valign="bottom"><img src="img/pix_grey.gif" width="550" height="1" border="0"></td>
                  <td><img src="img/tbl_dx2_corner_big.gif" width="5" border="0"></td>
		</tr>
              </table>
 <%}%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
