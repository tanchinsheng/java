<%@ page import="java.util.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%@ page import="tol.xmlRdr"%>
<%@ page import="it.txt.general.SessionObjects" %>
<%@ page import="it.txt.tpms.users.TpmsUser" %>
<%@ page import="it.txt.general.utils.GeneralStringUtils"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="it.txt.tpms.tp.utils.TpUtils"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="org.w3c.dom.Document"%>
<%@ page import="it.txt.tpms.tp.managers.TPDbManager"%>
<%@ page import="tpms.utils.QueryUtils"%>
<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="it.txt.tpms.tp.TPTestDetails"%>
<%
  String repTitle = (request.getParameter("repTitle")!=null ? request.getParameter("repTitle") : "");
  String repType = request.getParameter("repType");
  String webAppDir = config.getServletContext().getInitParameter("webAppDir");
  String repFileName = request.getParameter("repFileName");
  Element tpRec = (Element) session.getAttribute("tpRec");
  String xslFileName = request.getParameter("xslFileName");
  String jobName = (request.getParameter("jobname") != null ? request.getParameter("jobname") : null);
  String jobRel = (request.getParameter("job_rel") != null ? request.getParameter("job_rel") : null);
  String jobRev = (request.getParameter("job_rev") != null ? request.getParameter("job_rev") : null);
  String jobVer = (request.getParameter("job_ver") != null ? request.getParameter("job_ver") : null);
  String reportLabel = (request.getParameter("reportLabel") == null? "": request.getParameter("reportLabel"));
  int intJobRelease = -7;
  int intTpmsVersion = -7;
  String globalComment = "";
  String tpActionComment = "";
  boolean isDbConnectionAvailable = QueryUtils.checkCtrlServletDBConnection();
  
  if (isDbConnectionAvailable) {
      try {
          intJobRelease = (new Integer(jobRel)).intValue();
          intTpmsVersion = (new Integer(jobVer)).intValue();
      } catch (Throwable e){ }

      if (intJobRelease != -7 && intTpmsVersion != -7) {
      	globalComment = TP.getHtmlDisplayComments(TPDbManager.getTPDbComment(jobName, intJobRelease, jobRev, intTpmsVersion));
      	if (repType.equals("HTML") && xslFileName.equals("tp_view_db.xsl")){
      		String status =XmlUtils.getVal(tpRec,"STATUS");
      	  	tpActionComment = TP.getHtmlDisplayComments(TPDbManager.getTPActionComment(jobName, intJobRelease, jobRev, intTpmsVersion,status));
      	}
  	  }
  }

%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <% if (repType.equals("HTML")) {%><LINK href="default_print.css" type=text/css rel=stylesheet><%}%>
</HEAD>
<BODY bgColor="#FFFFFF">
<% if (repType.equals("CSV")) {%><PRE><%} else {%><CENTER><%}%>
<%
  Vector xslFiles=new Vector();
  Vector xslProps=new Vector();
  if (jobName!=null)
  {
     xslFiles.addElement(webAppDir.concat("/tp_sel_db.xsl"));
     Properties props=new Properties();
     props.setProperty("jobname",jobName);
     props.setProperty("job_rel",jobRel);
     props.setProperty("job_rev",jobRev);
     props.setProperty("job_ver",jobVer);
     xslProps.addElement(props);
  }
  xslFiles.addElement(webAppDir.concat("/xsl/"+xslFileName));
  if (repType.equals("CSV"))
  {
    xslProps.addElement(null);
  }
  else
  {
    Properties props=new Properties();
    props.setProperty("printView","Y");
    props.setProperty("tborder","1");
    props.setProperty("twidth","70%");
    props.setProperty("reportLabel", reportLabel);
    xslProps.addElement(props);
  }
  if (repType.equals("HTML"))
  { %>
    <TABLE BORDER="0"><TR><TD ALIGN="CENTER">
    <IMG alt="" SRC="img/st-rep-logo.gif" BORDER="0">
    <FONT class="titverdibig">
      <b><%= repTitle %></b><BR><BR>
       TPMS/W host=<%= config.getServletContext().getInitParameter("TpmsInstName") %> &nbsp; User=<%= (( TpmsUser )session.getAttribute( SessionObjects.TPMS_USER )).getName() %><BR><BR>
       <%= tol.dateRd.getCurDateTime() %>
    </FONT>
    </TD></TR>
    <TR><TD colspan="2"><IMG alt="" SRC="img/pix.gif" height="8"></TD></TR>
    </TABLE>
  <%}

  xmlRdr.transform(repFileName, xslFiles, xslProps, out);
  if (repType.equals("HTML") && xslFileName.equals("tp_view_db.xsl"))
  { %>
<tr>
	<td ALIGN="LEFT">
	<table border=1 width="480" cellspacing="" cellpadding="1">
		<tr>
			<td width="40%" valign="top" class="testoH"><b>Production area</b></td>
			<td width="60%" valign="top" class="testoD"><%=TpUtils.tpDisplayProductionArea( tpRec )%>
			</td>
		</tr>
	</table>
	<table border=1 RULES="NONE" FRAME="BOX" BORDERCOLORLIGHT="BLACK"
		BORDERCOLORDARK="BLACK" WIDTH="480">
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
			<td class="titred">Database connection not available. Kindly advice
			your System Administrator.<br>
			You will be able to view comments associated with this tp (if
			present) when the database connection will be available again.</td>
		</tr>
		<%}%>
	</table>
	</td>
</tr>
<tr>
<td> 
	<table width="480" cellspacing="" cellpadding="1" border="1">
		<%
			int jobRelease = -7;
			String RACEComment="";
            jobRelease = (new Integer(XmlUtils.getVal(tpRec,"JOB_REL"))).intValue();
            TPList newAttribute = (TPList) TPDbManager.getNewAttribute(jobName, jobRelease, jobRev, intTpmsVersion);
		    if (isDbConnectionAvailable){
		       	 newAttribute.rewind();
		         TP tmpTp;
		         while(newAttribute.hasNext()) {
		             tmpTp = newAttribute.next();           
            		 RACEComment=TP.getHtmlTextareaComments(tmpTp.getdeliveryComment());
        %>
		<tr>
			<td width="40%" valign="top" class="testoH"><b>RACE Comment</b></td>
			<%if (RACEComment != "") { %>
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
			<% if (hwModification != "") { %>
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
      		if (isDbConnectionAvailable && testAttributes!=null && testAttributes.size()>0) { 
				for (int i = 0;i < testAttributes.size();i++ ){
    	  			testDetails = (it.txt.tpms.tp.TPTestDetails) testAttributes.get(i); %>
		<tr>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getTestNoID()%></td>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getTestNo()%></td>
			<% String YesNo=""; 
               if ("Y".equals(testDetails.getNewFlag())) { 
            	   YesNo="es"; 
               }
      		   else if  ("N".equals(testDetails.getNewFlag())) { 
      			   YesNo="o";	  	 
      		   }
      		%>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getNewFlag()+YesNo%></td>

			<% if (testDetails.getOldLSL()!= -99999.99) { %>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getOldLSL()%></td>
			<%} else {%>
			<td width="10%" valign="top" class="testoD">&nbsp;</td>
			<% } %>

			<% if (testDetails.getOldUSL()!= -99999.99) { %>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getOldUSL()%></td>
			<%} else {%>
			<td width="10%" valign="top" class="testoD">&nbsp;</td>
			<% } %>

			<td width="10%" valign="top" class="testoD"><%=testDetails.getUnit()%></td>

			<% if (testDetails.getNewLSL()!= -99999.99) { %>
			<td width="10%" valign="top" class="testoD"><%=testDetails.getNewLSL()%></td>
			<%} else {%>
			<td width="10%" valign="top" class="testoD">&nbsp;</td>
			<% } %>

			<% if (testDetails.getNewUSL()!= -99999.99) { %>
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
<%} %>
<% if (repType.equals("CSV"))  {%></PRE><%} else {%></CENTER><%}%>
</BODY>
</HTML>



