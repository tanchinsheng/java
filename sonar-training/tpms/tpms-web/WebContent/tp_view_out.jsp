<%@ page import="it.txt.general.utils.GeneralStringUtils"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="it.txt.tpms.tp.utils.TpUtils"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="org.w3c.dom.Document"%>
<%@ page import="tpms.TpActionServlet"%>
<%@ page import="it.txt.tpms.tp.managers.TPDbManager"%>
<%@ page import="tpms.utils.QueryUtils"%>
<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="it.txt.tpms.tp.TPTestDetails"%>

<%
    String time = Long.toString(System.currentTimeMillis());
    String webAppDir=config.getServletContext().getInitParameter("webAppDir");
    Document tpData = (Document) session.getAttribute("tpData");
    Element tpRec = (Element) session.getAttribute("tpRec");
    String repFileName = request.getParameter("repFileName");
    String source = GeneralStringUtils.isEmptyString(request.getParameter("source"))? "" : request.getParameter("source");
    String qryType = request.getParameter("qryType");
    String jobName = request.getParameter("jobname");
    String jobRel = request.getParameter("job_rel");
    String jobRev = request.getParameter("job_rev");
    String jobVer = request.getParameter("job_ver");
    String myWorkMode = (session.getAttribute("workMode") == null ? "NONE" : (String) session.getAttribute("workMode"));
    boolean isExtractEnabled = TpUtils.allowExtractAction(tpRec);
    boolean showTpRejectButton = (tpRec.getAttribute("isRejectEnabled").equals("Y") || !isExtractEnabled) && source.equals("vob") && myWorkMode.equals("RECWORK");

    String xslFname;
    if (GeneralStringUtils.isEmptyString(jobRev) && GeneralStringUtils.isEmptyString(jobVer)){
        xslFname="tp_delete_view.xsl";
    } else if (source.equals("vob")){
        xslFname="tp_view_vob.xsl";
    } else {
        xslFname="tp_view_db.xsl";
    }

    int intJobRelease = -7;
    int intTpmsVersion = -7;
    String globalComment = "";
    String tpActionComment = "";
    String status =XmlUtils.getVal(tpRec,"STATUS");
    boolean isDbConnectionAvailable = QueryUtils.checkCtrlServletDBConnection();
    if (isDbConnectionAvailable) {
        try {
            intJobRelease = (new Integer(jobRel)).intValue();
            intTpmsVersion = (new Integer(jobVer)).intValue();
        } catch (Throwable e){ }

        if (intJobRelease != -7 && intTpmsVersion != -7) {
        	globalComment = TP.getHtmlDisplayComments(TPDbManager.getTPDbComment(jobName, intJobRelease, jobRev, intTpmsVersion));
            tpActionComment = TP.getHtmlDisplayComments(TPDbManager.getTPActionComment(jobName, intJobRelease, jobRev, intTpmsVersion,status));
        }
    }

%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
<TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type=text/css rel=stylesheet>
<SCRIPT language=JavaScript>
	function printView()
  	{
    <% 	 if (source.equals("vob")) {%>
         	url="tp_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode("TP DATA REPORT", "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFname %>&status1Filter=&status2Filter=&userFilter=&repType=HTML&t=<%= time %>"
         	job="&jobname=<%= java.net.URLEncoder.encode(XmlUtils.getVal(tpRec,"JOBNAME"), "UTF-8") %>&job_rel=<%= XmlUtils.getVal(tpRec,"JOB_REL") %>&job_rev=<%= XmlUtils.getVal(tpRec,"JOB_REV") %>&job_ver=<%= XmlUtils.getVal(tpRec,"JOB_VER") %>";
         <%} else {%>
         	url="tp_csv.jsp?repTitle=<%= java.net.URLEncoder.encode("TP DATA REPORT", "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFname %>&repType=HTML&t=<%= time %>"
         	job="&jobname=<%= java.net.URLEncoder.encode(XmlUtils.getVal(tpRec,"JOBNAME"), "UTF-8") %>&job_rel=<%= XmlUtils.getVal(tpRec,"JOB_REL") %>&job_rev=<%= XmlUtils.getVal(tpRec,"JOB_REV") %>&job_ver=<%= XmlUtils.getVal(tpRec,"JOB_VER") %>";
       <%}
    %>
    window.open(url+job);
    }

  	function go_to(job,rel,rev,ver)
  	{
    	document.viewTpForm.jobname.value=job;
    	document.viewTpForm.job_rel.value=rel;
    	document.viewTpForm.job_rev.value=rev;
    	document.viewTpForm.job_ver.value=ver;
    	document.viewTpForm.submit();
  	}

  	function submitAction(action)
  	{
    	document.tpActionForm.action.value=action;
    	document.tpActionForm.submit();
  	}

   	function submitMultiAction()
  	{
    	if(checkNofSelItems() == 0) {
        	return;
    	} else {
        	document.tpMultiActionForm.submit();
    	}
  	}
  	
  	function checkNofSelItems()
  	{
    	cont=0;
		for (i=0; i< document.tpMultiActionForm.elements.length; i++) {
	   		if (document.tpMultiActionForm.elements[i].type=='checkbox'){
          		if (document.tpMultiActionForm.elements[i].checked) cont++;
	   		}
    	}
    	if (cont==0) {
      		alert('AT LEAST ONE TP MUST BE SELECTED!');
    	}
    	return cont;
  	}

  	function selectAll()
 	{
    	for (i=0; i< document.tpMultiActionForm.elements.length; i++) {
       		if (document.tpMultiActionForm.elements[i].type=='checkbox') {
            	if (document.tpMultiActionForm.ALL.checked){
					document.tpMultiActionForm.elements[i].checked = true;
            	}else{
					document.tpMultiActionForm.elements[i].checked = false;
            	}
       		}
    	}
  	}

</SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=true; boolean csvRepBool=false; String pageTitle="TP DATA"; %>
<%@ include file="top.jsp"%>
<FORM name="viewTpForm" action="tpDetailViewServlet" method="post">
	<INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%=repFileName%>"> 
	<INPUT TYPE="HIDDEN" NAME="jobname" VALUE=""> 
	<INPUT TYPE="HIDDEN" NAME="job_rel" VALUE=""> 
	<INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="">
	<INPUT TYPE="HIDDEN" NAME="job_ver" VALUE=""> 
	<INPUT TYPE="HIDDEN" NAME="source" VALUE="vob"> 
	<INPUT TYPE="HIDDEN" NAME="qryType" VALUE="">
</FORM>
<%if (!isExtractEnabled && !source.equals("db")) {%>
<tr>
	<td align="center" class="titred">Tester information associated with this TP are not present in your local plant.<br>
	Please contact your administrator asking him to check the Testers information related to<br>
	"<%=XmlUtils.getVal(tpRec, "TESTER_INFO")%>".</td>
</tr>
<%}
 	if (jobRev.equals("")&& jobVer.equals("")) { %>
<FORM name="tpMultiActionForm" action="tpMultiActionServlet" method="post">
	<INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y"> 
	<INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_delete"> 
	<INPUT TYPE="HIDDEN" NAME="baseline" VALUE=""> 
	<INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%=repFileName %>">
<TR>
	<TD ALIGN="LEFT">
	<table cellpadding="0" cellspacing="0" border="0" width="760">
		<tr>
			<td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
			<td background="img/tit_center.gif" align="center" class="titverdi"
				nowrap><b><!-- FORM TITLE-->&nbsp&nbsp;TPS&nbsp;&nbsp;&nbsp;DATA&nbsp;&nbsp</b></td>
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
	<TD ALIGN="LEFT"><!-- REPORT --> 
	<% XmlUtils.transform(tpData, webAppDir+"/xsl/"+xslFname, null, out); %> <!-- /REPORT --></TD>
</TR>

</FORM>

<TR>
	<TD class="testo"><br>
	<!-- BUTTONS -->
	<table cellpadding="0" cellspacing="0" border="0" width="65%">
		<tr>
			<td><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
		</tr>
		<tr>
			<td><img src="img/blank_flat.gif" alt="" border="0"></td>
		</tr>
		<tr>
			<td align="right">
			<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
				<TR>
					<!-- MULTI TPS DELETE-->
					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:submitMultiAction()">&nbsp;DELETE TPs</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
				</TR>
			</TABLE>
			</td>
		</tr>
	</table>
	<!-- /BUTTONS --></TD>
</TR>
<%
      }else{
      %>
<FORM name="tpActionForm" action="tpActionServlet" method="post">
	<INPUT TYPE="HIDDEN" NAME="action" VALUE=""> 
	<INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
<TR>
	<TD ALIGN="LEFT">
	<table cellpadding="0" cellspacing="0" border="0" width="760">
		<tr>
			<td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
			<td background="img/tit_center.gif" align="center" class="titverdi"
				nowrap><b><!-- FORM TITLE-->&nbsp&nbsp;TP&nbsp;&nbsp;&nbsp;DATA&nbsp;&nbsp</b></td>
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
	<TD ALIGN="LEFT"><!-- REPORT --> <%
                 XmlUtils.transform(tpData, webAppDir+"/xsl/"+xslFname, null, out);
               %> <!-- /REPORT --></TD>
</TR>

<tr>
	<td ALIGN="LEFT">
		<table width="493" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td width="40%" valign="top" class="testoH"><b>Production area</b></td>
				<td width="60%" valign="top" class="testoD"><%=TpUtils.tpDisplayProductionArea( tpRec )%></td>
			</tr>
		</table>

		<table border=1 RULES="NONE" FRAME="BOX" BORDERCOLORLIGHT="BLACK" BORDERCOLORDARK="BLACK" WIDTH="100%">
			<%if (isDbConnectionAvailable) {%>
			<tr>
				<td class="testoH"><b>Global Comment</b></td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td><br><%=globalComment%></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="testoH"><b>Status Comment</b></td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td><br><%=tpActionComment%></td>
						</tr>
					</table>
				</td>
			</tr>
			<%} else {%>
			<tr>
				<td class="titred">Database connection not available. Kindly advice your System Administrator.<br>
				You will be able to view comments associated with this tp (if present) when the database connection will be available again.</td>
			</tr>
			<%}%>
		</table>
	</td>
</tr>
<tr>
<td>
	<table width="498" cellspacing="1" cellpadding="0" border="0">
		<%
			int jobRelease = -7;
            jobRelease = (new Integer(XmlUtils.getVal(tpRec,"JOB_REL"))).intValue();
            TPList newAttribute = (TPList) TPDbManager.getNewAttribute(jobName, jobRelease, jobRev, intTpmsVersion);
		    if (isDbConnectionAvailable && newAttribute != null && newAttribute.size()>0) {
		       	 newAttribute.rewind();
		         TP tmpTp;
		         while(newAttribute.hasNext()) {
		             tmpTp = newAttribute.next();           
            		 String RACEComment="";
            		 RACEComment=TP.getHtmlTextareaComments(tmpTp.getdeliveryComment());
        %>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>RACE Comment</b></td>
			<%if (RACEComment != null) { %>
				<td width="60%" valign="top" class="testoD"><%=RACEComment%></td>
			<%} else {%>
				<td width="60%" valign="top" class="testoD">&nbsp;</td>
			<%} %>
		</tr>
			<% String hwModification="";
              hwModification=TP.getHtmlTextareaComments(tmpTp.gethwModification());        
            %>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>HW Modifications</b></td>
			<% if (hwModification != null) { %>
				<td width="60%" valign="top" class="testoD"><%=hwModification%></td>
			<%} else {%>
				<td width="60%" valign="top" class="testoD">&nbsp;</td>
			<%}%>
		</tr>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>Expected Average Yield Variation [%]:</b></td>
			<%if (!String.valueOf(tmpTp.getexpAvgYV()).equals("")) { %>
				<td width="60%" valign="top" class="testoD"><%=tmpTp.getexpAvgYV()%></td>
			<%}	else {%>
				<td width="60%" valign="top" class="testoD">&nbsp;</td>
			<%}%>
		</tr>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>Zero Yield Wafer [%]:</b></td>
			<%if (tmpTp.getzeroYW()>=0) {%>
				<td width="60%" valign="top" class="testoD"><%=tmpTp.getzeroYW()%></td>
			<%} else {%>
				<td width="60%" valign="top" class="testoD">&nbsp;</td>
			<%}%>
		</tr>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>New Test Time [ms]:</b></td>
			<%if (tmpTp.getnewTestTime()>=0) {%>
				<td width="60%" valign="top" class="testoD"><%=tmpTp.getnewTestTime()%></td>
			<%} else {%>
				<td width="60%" valign="top" class="testoD">&nbsp;</td>
			<%}%>
		</tr>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>Is Temporary?</b></td>
		<% 	String tempTail =""; 
           	if ("Y".equals(tmpTp.getisTemp())) { 
           		tempTail="es";
           	} else if  ("N".equals(tmpTp.getisTemp())) { 
           		tempTail="o"; 
           	} else  tempTail="";	 
      	   	if (tempTail != "") {%>
				<td width="60%" valign="top" class="testoD"><%=tmpTp.getisTemp()+tempTail%></td>
		   	<%} else {%>
				<td width="60%" valign="top" class="testoD">&nbsp;</td>
		   	<%}%>
		</tr>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>Valid Till</b></td>
		<%if (tmpTp.getFormattedvalidTill() != null) { %>
			<td width="60%" valign="top" class="testoD"><%=tmpTp.getFormattedvalidTill()%>&nbsp;</td>
		<%} else {%>
			<td width="60%" valign="top" class="testoD">&nbsp;</td>
		<%} %>
		</tr>
			   <%}
      		}%>
	</table>
	<table border="1">
		<tr>
			<td width="10%" valign="top" class="testoH">ID</td>
			<td width="10%" valign="top" class="testoH">Test #</td>
			<td width="10%" valign="top" class="testoH">New</td>
			<td width="10%" valign="top" class="testoH">Old LSL</td>
			<td width="10%" valign="top" class="testoH">Old USL</td>
			<td width="10%" valign="top" class="testoH">Unit</td>
			<td width="10%" valign="top" class="testoH">New LSL</td>
			<td width="10%" valign="top" class="testoH">New USL</td>
			<td width="30%" valign="top" class="testoH">Comment</td>
		</tr>
		<% ArrayList testAttributes =  (ArrayList)TPDbManager.getTPTestDetails(jobName, intJobRelease, jobRev, intTpmsVersion);
      		TPTestDetails  testDetails;
      		if (isDbConnectionAvailable && testAttributes!=null && testAttributes.size()>0) { %>
		
		<%	for (int i = 0;i < testAttributes.size();i++ ){
    	  		testDetails = (TPTestDetails) testAttributes.get(i); %>
		<tr>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getTestNoID()%></td>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getTestNo()%></td>
		  <%String NewFlagTail=""; 
            if ("Y".equals(testDetails.getNewFlag())) { 
            	NewFlagTail="es";
            } else if  ("N".equals(testDetails.getNewFlag())) { 
            	NewFlagTail="o"; 
            }%>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getNewFlag()+NewFlagTail%></td>

			<% if (testDetails.getOldLSL()!= -99999.99) { %>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getOldLSL()%></td>
			<%} else {%>
			<td width="10%" valign="top" class="testoD">&nbsp;</td>
			<% } 
			if (testDetails.getOldUSL()!= -99999.99) { %>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getOldUSL()%></td>
			<%} else {%>
			<td width="10%" valign="top" class="testoD">&nbsp;</td>
			<% } %>

			<td width="10%" valign="top" class="testoD"><%=testDetails.getUnit()%></td>

			<% if (testDetails.getNewLSL()!= -99999.99) { %>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getNewLSL()%></td>
			<%} else {%>
			<td width="10%" valign="top" class="testoD">&nbsp;</td>
			<% } 
			if (testDetails.getNewUSL()!= -99999.99) { %>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getNewUSL()%></td>
			<%} else {%>
			<td width="10%" valign="top" class="testoD">&nbsp;</td>
			<% } %>
			<td width="30%" valign="top" class="testoD"><%=testDetails.getTestsComments()%></td>
		</tr>

		<% 		}
      		}
            %>
	</table>
</td>
</tr>
</FORM>

<TR>
	<TD class="testo"><br>
	<!-- BUTTONS -->
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td><img src="img/pix_nero.gif" width="100%" height="1" alt="" border="0"></td>
		</tr>
		<tr>
			<td><img src="img/blank_flat.gif" alt="" border="0"></td>
		</tr>
		<tr>
			<td align="right">
			<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
				<TR>
					<%if (isExtractEnabled) {%>
					<!-- EXTRACT TO VALIDATE -->
					<%
                      if ((workMode.endsWith("WORK"))&&(tpRec.getAttribute("isExtrToValidateEnabled").equals("Y")))
                      {%>
					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:submitAction('extr_to_valid')">&nbsp;EXTRACT TO VALIDATE</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
					<%}
		     %>
					<!-- MARK AS VALIDATED -->
					<%
	              if ((workMode.endsWith("WORK"))&&(tpRec.getAttribute("isMarkAsValidatedEnabled").equals("Y")))
	              {%>
					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:submitAction('tp_update')">&nbsp;MARK
					AS VALIDATED</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
					<%}
		     %>
					<!-- PUT IN PRODUCTION -->
					<%
                      if ((workMode.endsWith("WORK"))&&(tpRec.getAttribute("isPutInProductionEnabled").equals("Y")))
	              {%>
					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:submitAction('tp_toprod')">&nbsp;PUT
					IN PRODUCTION</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
					<%}
		     %>
					<!-- DOWNLOAD TP FROM SEARCH Action-->
					<%
                      if ((workMode.endsWith("WORK"))&&(tpRec.getAttribute("isDownloadTpEnabled").equals("Y")))
                      {%>
					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON"
						HREF="javascript:submitAction('tp_download_complete')">&nbsp;DOWNLOAD TP</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
					<%}
		     %>
					<%}
                 //if tester infos are not alligned between source and destination plant the reject button should be shown also.
             %>
					<%if (showTpRejectButton){%>
					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON"
						HREF="javascript:submitAction('<%=TpActionServlet.TP_REJECT_ACTION_VALUE%>')">&nbsp;REJECT TP</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
					<%}%>

					<!--TP Restore Button for restore the TP -->
					<%
                      if ((source.equals("db"))&&(qryType.equals("db_tp_offLine")))
                      {%>
					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:submitAction('tp_restore')">&nbsp;Restore TP</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
					<%}
		     %>
					<!-- BACK Button-->

					<TD valign="center"><img src="img/pix.gif" width="5" border="0"></TD>
					<TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
					<TD valign="center" background="img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:history.back()">&nbsp;BACK</A></TD>
					<TD valign="center"><IMG SRC="img/btn_right.gif"></TD>
				</TR>
			</TABLE>
			</td>
		</tr>
	</table>
	<!-- /BUTTONS --></TD>
</TR>
<%
      }
      %>

<%@ include file="bottom.jsp"%>
</BODY>
</HTML>

