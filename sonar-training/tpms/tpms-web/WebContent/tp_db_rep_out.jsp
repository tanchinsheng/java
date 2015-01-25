<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
    isErrorPage="false"
    errorPage="uncaughtErr.jsp"
%>
<%
  String actionTxt=(String)request.getParameter("actionTxt");  
  String time = Long.toString(System.currentTimeMillis()); 
  String reqId=request.getParameter("reqId");
  String repFileName=request.getParameter("repFileName");
  String repTitle=request.getParameter("repTitle");
  String qryType=request.getParameter("qryType");
  String refreshTime=request.getParameter("refreshTime");
  boolean startBool=((Boolean)session.getAttribute("startBool"+"_"+reqId)).booleanValue();
  String webAppDir=config.getServletContext().getInitParameter("webAppDir");
  String xslFileNameTp="tp_query_db.xsl";
  String xslFileNameLs="ls_query_db.xsl";
  if ((!startBool)&&(session.getAttribute("exception"+"_"+reqId)!=null)) throw (Exception)session.getAttribute("exception"+"_"+reqId);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <%@ include file="tp_search_form_checks.jsp" %>
 <%--@ include file="ls_search_form_checks.jsp" --%>
 <SCRIPT language=JavaScript>
  function start()
  {
   <%
    if (startBool)
    {%>
       setTimeout("location.reload()",<%= refreshTime %>*1000)
    <%}
   %>
  }


  function printView()
  {

    var url="tp_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle,"UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFileNameTp %>&repType=HTML&t=<%= time %>"
    var objForms = window.document.forms;
    if (objForms != null){
        var reportLabel = "";
        var elementName = "";
        var elementValue = "";

        for ( var z = 0; z < objForms.length; z++ ) {
            var objForm = objForms[z];
            if ( objForm != null ) {
                var objElements = objForm.elements;
                for ( var i = 0; i < objElements.length; i++ ) {
                    if (objElements[i].type != "hidden") {
                        elementName = objElements[i].name.replace("field.", "");
                        if (elementName.indexOf(".min") >= 0){
                            elementName = elementName.replace(".min", "") + " FROM";
                        } else if (elementName.indexOf(".max") >= 0){
                            elementName = elementName.replace(".max", "") + " TO";
                        }

                        if (elementName.indexOf("DATE TO") >= 0){
                            elementName = "TO";
                        }
                        //replace "_" with spaces (" ")
                        while (elementName.indexOf("_") >= 0) {
                            elementName = elementName.replace("_", " ");
                        }
                        if (objElements[i].type == "text") {
                            elementValue = objElements[i].value;
                        } else if (objElements[i].type == "select-one"){
                            elementValue = objElements[i].options[objElements[i].selectedIndex].value;
                        } else {
                            elementValue = "";
                        }
                        if (elementValue != null && elementValue != "") {
                            if (reportLabel != null && reportLabel != ""){
                                reportLabel = reportLabel + " " + elementName + " = " + elementValue + ";";
                            } else {
                                reportLabel = "Selection Criteria: " + elementName + " = " + elementValue + ";";
                            }
                        }
                    }
                }
            }
        }
        if (reportLabel != null){
            url = url + "&reportLabel=" + escape(reportLabel);
        }
    }
    window.open(url);
  }

  function getCsv()
  {

    url="tp_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle, "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= "db_csv.xsl" %>&repType=CSV&t=<%= time %>"
    window.open(url);
  }

  function go_to(job,rel,rev,ver)
  {
    document.viewTpForm.jobname.value=job;
    document.viewTpForm.job_rel.value=rel;
    document.viewTpForm.job_rev.value=rev;
    document.viewTpForm.job_ver.value=ver;
    document.viewTpForm.submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=!startBool; boolean csvRepBool=!startBool; String pageTitle=repTitle; %>
<%@ include file="top.jsp" %>
<%
 if (startBool)
 {%>
   <%@ include file="waitmsg.jsp" %>
 <%}
 else 
 {%>
	    <FORM name="viewTpForm" action="<%=contextPath%>/tpDetailViewServlet" method="post">
	     <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
	     <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="source"  VALUE="db">
         <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="<%= qryType %>">
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;REPORT&nbsp;</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="603" height="18" alt="" border="0"></td>
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
   	       <% if(!qryType.equals("db_ls_search"))
              {
                xmlRdr.transform(repFileName, webAppDir.concat("/xsl/"+xslFileNameTp), null, out);
              }else{
                xmlRdr.transform(repFileName, webAppDir.concat("/xsl/"+xslFileNameLs), null, out);
              }
              %>
	       <!-- /REPORT -->
              </TD>
             </TR>	     
            </FORM>

             <TR>
    	      <TD class="testo">
              <br>	
	      <!-- BUTTONS -->	
              <table cellpadding="0" cellspacing="0" border="0" width="760">
                <tr>
                  <td><img src="img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><img src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		<tr>
                 <td align="right">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
 		      <!--TD valign="top" ><IMG SRC="img/blank_flat.gif" width="4"></TD-->
                      <!--TD valign="top"><IMG SRC="img/btn_left.gif"></TD>
                      <TD valign="top" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="#">&nbsp;GET CSV</TD>
                      <TD valign="top" ><IMG SRC="img/btn_right.gif"></TD-->
	           </TR>
                  </TABLE>
                </td>
 		</tr>	
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>

	    <%
             if (!qryType.equals("") && (!qryType.equals("db_ls_search") && !qryType.equals("db_tp_offLine")))
	     {%>	
                <TR>
                 <TD ALIGN="LEFT">
		  <BR>
                 </TD>
                </TR>
		<% boolean news=false; %>
		<%@ include file="tp_search_form.jsp" %>
	     <%}
	    %>
        <%
             if (qryType.equals("db_ls_search"))
	     {%>
                <TR>
                 <TD ALIGN="LEFT">
		  <BR>
                 </TD>
                </TR>
		<% boolean news=false; %>
		<%@ include file="ls_search_form.jsp" %>
	     <%}
	    %>
        <%
             if (qryType.equals("db_tp_offLine"))
	     {%>
                <TR>
                 <TD ALIGN="LEFT">
		  <BR>
                 </TD>
                </TR>
		<% boolean news=false; %>

	     <%}
	    %>

 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
