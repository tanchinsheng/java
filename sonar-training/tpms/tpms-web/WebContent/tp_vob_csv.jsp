<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>
<%
    /**
     *
repTitle=TP+IN+PROGRESS+REPORT+%28VOB%29
repFileName=/tpms/webtpms/images/sijg9l5si1_1127130750042_rep.xml
xslFileName=tp_query_vob.xsl
status1Filter=In_Validation
status2Filter=Ready_to_production
userFilter=tpmsval
repType=CSV
t=1127130778825
     */

  String webAppDir=config.getServletContext().getInitParameter("webAppDir");

  String repTitle = (request.getParameter("repTitle")!=null ? request.getParameter("repTitle") : "");
  String repFileName=request.getParameter("repFileName");
  String xslFileName=request.getParameter("xslFileName");
  String status1Filter=request.getParameter("status1Filter");
  String status2Filter=request.getParameter("status2Filter");
  String userFilter=request.getParameter("userFilter");
  String repType = request.getParameter("repType");

  




  String filterXslFile=(request.getParameter("filterXslFile")!=null ? request.getParameter("filterXslFile") : null);
  String jobName=(request.getParameter("jobname")!=null ? request.getParameter("jobname") : null);
  String jobRel=(request.getParameter("job_rel")!=null ? request.getParameter("job_rel") : null);
  String jobRev=(request.getParameter("job_rev")!=null ? request.getParameter("job_rev") : null);
  String jobVer=(request.getParameter("job_ver")!=null ? request.getParameter("job_ver") : null);
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
  if (!status1Filter.equals(""))
  {
    xslFiles.addElement(webAppDir.concat("/tp_filter_status_vob.xsl"));
    Properties props=new Properties();
    props.setProperty("status1",status1Filter);
    if (!status2Filter.equals("")) props.setProperty("status2",status2Filter);
    xslProps.addElement(props);		
  }
  if (!userFilter.equals(""))
  {
    xslFiles.addElement(webAppDir.concat("/tp_filter_user_vob.xsl"));
    Properties props=new Properties();
    props.setProperty("user",userFilter);
    xslProps.addElement(props);		
  } 
  if (filterXslFile!=null)
  {
    xslFiles.addElement(filterXslFile);
    xslProps.addElement(null);
  }
  if (jobName!=null)
  {
     xslFiles.addElement(webAppDir.concat("/tp_sel_vob.xsl"));
     Properties props=new Properties();
     props.setProperty("jobname",jobName);
     props.setProperty("job_rel",jobRel);     
     props.setProperty("job_rev",jobRev);     
     props.setProperty("job_ver",jobVer);
     xslProps.addElement(props);
  }
  xslFiles.addElement(webAppDir.concat("/xsl/"+(repType.equals("CSV") ? "tp_vob_csv.xsl" : xslFileName)));
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
    xslProps.addElement(props);
  }

  if (repType.equals("HTML"))
  {%>
    <TABLE BORDER="0"><TR><TD ALIGN="CENTER">	
    <IMG alt="" SRC="img/st-rep-logo.gif" BORDER="0">
    <FONT class="titverdibig">
      <b><%= repTitle %></b><BR><BR>
       TPMS/W host=<%= config.getServletContext().getInitParameter("TpmsInstName") %> &nbsp; Vob=<%= session.getAttribute("vobDesc") %> &nbsp; User=<%= session.getValue("user") %><BR><BR>
       <%= tol.dateRd.getCurDateTime() %>
    </FONT>
    </TD></TR>
    <TR><TD colspan="2"><IMG alt="" SRC="img/pix.gif" height="8"></TD></TR>
    </TABLE> 
  <%}
  xmlRdr.transform(repFileName, xslFiles, xslProps, out);
%>

<% if (repType.equals("CSV"))  {%></PRE><%} else {%></CENTER><%}%>
</BODY>
</HTML>
