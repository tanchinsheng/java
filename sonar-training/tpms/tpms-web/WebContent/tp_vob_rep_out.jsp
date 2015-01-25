<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.*,
                 it.txt.afs.AfsCommonStaticClass"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>


<%
  String time = new Long(System.currentTimeMillis()).toString();
  String reqId=request.getParameterValues("reqId")[0];
  String repFileName=request.getParameterValues("repFileName")[0];
  String xslFileName=request.getParameterValues("xslFileName")[0];
  String filterXslFile=(request.getParameterValues("filterXslFile")!=null ? request.getParameterValues("filterXslFile")[0] : null);
  String repTitle=request.getParameterValues("repTitle")[0];
  String qryType=request.getParameterValues("qryType")[0];
  String actionTxt=(String)request.getParameterValues("actionTxt")[0];
  String refreshTime=request.getParameterValues("refreshTime")[0];

  // boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
  boolean startBool = (session.getValue("startBool"+"_"+reqId) != null) ? ((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue() : false;
  String webAppDir=config.getServletContext().getInitParameter("webAppDir");
  if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
%>

<%
  String status1Filter=request.getParameterValues("status1Filter")[0];
  String status2Filter=request.getParameterValues("status2Filter")[0];
  String userFilter=request.getParameterValues("userFilter")[0];
  String ownerFilter=request.getParameterValues("ownerFilter")[0];
  Vector xslFiles=new Vector();
  Vector xslProps=new Vector();
  if (!status1Filter.equals(""))
  {
    xslFiles.addElement(webAppDir.concat("/tp_filter_status_vob.xsl"));
    Properties props=new Properties();
    props.setProperty("status1",status1Filter);
    props.setProperty("status2",status2Filter);
    xslProps.addElement(props);
  }
  if (!userFilter.equals(""))
  {
    xslFiles.addElement(webAppDir.concat("/tp_filter_user_vob.xsl"));
    Properties props=new Properties();
    props.setProperty("status",status1Filter);
    props.setProperty("user",userFilter);
    xslProps.addElement(props);
  }
  if (!ownerFilter.equals(""))
  {
    xslFiles.addElement(webAppDir.concat("/tp_filter_owner_vob.xsl"));
    Properties props=new Properties();
    props.setProperty("user",ownerFilter);
    xslProps.addElement(props);
  }
  if (filterXslFile!=null)
  {
    xslFiles.addElement(filterXslFile);
    xslProps.addElement(null);
  }
  xslFiles.addElement(webAppDir.concat("/xsl/"+xslFileName));
  if (qryType.equals("vob_prod_lib_hist"))
  {
    Properties props=new Properties();
    props.setProperty("checkBoxesBool","Y");
    xslProps.addElement(props);
  }
  else xslProps.addElement(null);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <%@ include file="tp_search_form_checks.jsp" %>
 <SCRIPT language=JavaScript>
  function printView()
  {
    url="tp_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle, "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFileName %>&status1Filter=<%= status1Filter %>&status2Filter=<%= status2Filter %>&userFilter=<%= userFilter %><%= (filterXslFile!=null ? "&filterXslFile="+filterXslFile : "") %>&repType=HTML&t=<%= time %>"
    window.open(url);
  }

  function getCsv()
  {
    url="tp_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle, "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFileName %>&status1Filter=<%= status1Filter %>&status2Filter=<%= status2Filter %>&userFilter=<%= userFilter %><%= (filterXslFile!=null ? "&filterXslFile="+filterXslFile : "") %>&repType=CSV&t=<%= time %>"
    window.open(url);
  }

  function submitSearch()
  {
    document.tpSearchForm.submit();
  }

  function start()
  {
   <%
    if (startBool)
    {%>
       var delay = setTimeout("location.reload()",<%= refreshTime %>*1000);
    <%}
   %>
  }

  function go_to_delete(job,rel,action)
  {
    document.viewTpForm.jobname.value=job;
    document.viewTpForm.job_rel.value=rel;
    document.viewTpForm.submit();
  }

  function go_to(job,rel,rev,ver)
  {
    document.viewTpForm.jobname.value=job;
    document.viewTpForm.job_rel.value=rel;
    document.viewTpForm.job_rev.value=rev;
    document.viewTpForm.job_ver.value=ver;
    document.viewTpForm.submit();
  }



  function submitTpCompare()
  {
    ret=checkNofSelItems();
    if (ret>2) return;
    if (ret==1)
    {
      alert('YOU MUST SELECT AT LEAST TWO TPs');
      return;
    }
    if (ret==0)
    {
      alert('AT LEAST ONE TP MUST BE SELECTED!');
      return;
    }
    else
    {
      index1='';
      index2='';
      for (i=0; i< document.viewTpForm.elements.length; i++)
      {
	if (document.viewTpForm.elements[i].type=='checkbox')
	{
          if (document.viewTpForm.elements[i].checked)
          {
          	if (index1=='') index1=document.viewTpForm.elements[i].name.substring(3);
		else index2=document.viewTpForm.elements[i].name.substring(3);
	  }
	}
      }
      job1=index1.substring(0,index1.indexOf('.'));
      job2=index2.substring(0,index2.indexOf('.'));
      if (job1==job2)
      {
        document.tpActionForm.action.value='tp_compare';
        document.tpActionForm.jobLabel1.value=index1;
        document.tpActionForm.jobLabel2.value=index2;
        document.tpActionForm.submit();
      }
      else
      {
        alert('CANNOT COMPARE TPs WITH DIFFERENT NAMES');
        return;
      }
    }
  }

  function checkNofSelItems()
  {
    cont=0;
    for (i=0; i< document.viewTpForm.elements.length; i++)
    {
	if (document.viewTpForm.elements[i].type=='checkbox')
	{
          if (document.viewTpForm.elements[i].checked) cont++;
          if (cont>2)
          {
          	document.viewTpForm.elements[i].checked=false;
		break;
	  }
	}
    }
    if (cont>2)
    {
      alert('YOU CANNOT SELECT MORE THAN TWO TPs');
    }
    return cont;
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

	    <FORM name="tpActionForm" action="tpActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="jobLabel1" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="jobLabel2" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
	    </FORM>

	    <FORM name="viewTpForm" action="tpDetailViewServlet" method="post">
	     <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
	     <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="source"  VALUE="vob">
         <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="">
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;TP&nbsp;&nbsp;&nbsp;REPORT&nbsp;</b></td>
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
   	       <%
                xmlRdr.transform(repFileName, xslFiles, xslProps, out);
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
                    <%
                     if ((qryType.equals("vob_prod_lib_hist"))||(qryType.equals("vob_tp_search")))
		     {%>
                      <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitTpCompare()">&nbsp;TP COMPARE</A></TD>
                      <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                      <TD><img src="img/pix.gif" width="5" border="0"></TD>
		     <%}
                    %>
                    <!--TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="#">&nbsp;GET CSV</TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD-->
	           </TR>
                  </TABLE>
                </td>
 		</tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>

	    <%
             if (VobQryServlet.isSearchFormEnabled(qryType))
	     {%>
                <TR>
                 <TD ALIGN="LEFT">
		  <BR>
                 </TD>
                </TR>
		<% boolean news=false; %>
		<%@ include file="vob_tp_search_form.jsp" %>
	     <%}
	    %>

 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
