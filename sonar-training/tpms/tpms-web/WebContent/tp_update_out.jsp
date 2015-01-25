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
 if (startBool)
 {%>
   <%@ include file="waitmsg.jsp" %>
 <%}
 else 
 {%>
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->INVOLVED TP REVISIONS</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="520" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%                
                xmlRdr.transform(repFileName, xslFileName, null, out);
           %>
	       <!-- /REPORT -->
              </TD>	
             </TR>
	     <TR><TD><img alt="" src="img/blank.gif" border="0"></TD></TR>	

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->FILES FEATURED BY THE ORIGINAL TP ONLY</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="410" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
                
                xslProps.setProperty("filter","TP1");
                xmlRdr.transform(repFileName, xslFileName, xslProps, out);
               %>
	       <!-- /REPORT -->
              </TD>
             </TR>
	     <TR><TD><img alt="" src="img/blank.gif" border="0"></TD></TR>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->FILES FEATURED BY THE NEW TP ONLY</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="440" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
                
                xslProps.setProperty("filter","TP2");
                xmlRdr.transform(repFileName, xslFileName, xslProps, out);
               %>
	       <!-- /REPORT -->
              </TD>
             </TR>
	     <TR><TD><img alt="" src="img/blank.gif" border="0"></TD></TR>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->COMMON FILES WITH SOME DIFFERENCES</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="410" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
                
                xslProps.setProperty("filter","BOTH");
                xmlRdr.transform(repFileName, xslFileName, xslProps, out);
               %>
	       <!-- /REPORT -->
              </TD>
             </TR>
	     <TR><TD><img alt="" src="img/blank.gif" border="0"></TD></TR>

	


 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
