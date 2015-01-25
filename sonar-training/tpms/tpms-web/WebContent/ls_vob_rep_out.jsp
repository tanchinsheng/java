<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"

%>


<%
  String time = new Long(System.currentTimeMillis()).toString();
  String webAppDir=config.getServletContext().getInitParameter("webAppDir");
  String reqId=request.getParameterValues("reqId")[0];
  String repFileName=request.getParameterValues("repFileName")[0];
  String xslFileName=request.getParameterValues("xslFileName")[0];
  String filterXslFile=(request.getParameterValues("filterXslFile")!=null ? request.getParameterValues("filterXslFile")[0] : null);
  String repTitle=request.getParameterValues("repTitle")[0];
  String qryType=request.getParameterValues("qryType")[0];
  String actionTxt=(String)request.getParameterValues("actionTxt")[0];
  String refreshTime=request.getParameterValues("refreshTime")[0];
  boolean startBool=((Boolean)session.getAttribute("startBool"+"_"+reqId)).booleanValue();

  Element userData=CtrlServlet.getUserData((String)session.getAttribute("user"));
  String workDir=xmlRdr.getVal(userData,"WORK_DIR");

  if ((!startBool)&&(session.getAttribute("exception"+"_"+reqId)!=null)) throw (Exception)session.getAttribute("exception"+"_"+reqId);
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
    xslFiles.addElement(webAppDir.concat("/ls_filter_status_vob.xsl"));
    Properties props=new Properties();
    props.setProperty("status1",status1Filter);
    props.setProperty("status2",status2Filter);
    xslProps.addElement(props);
  }

  if (!ownerFilter.equals(""))
  {
    xslFiles.addElement(webAppDir.concat("/ls_filter_owner_vob.xsl"));
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
  if (qryType.equals("vob_lineset_submits"))
  {
    Properties props=new Properties();
    props.setProperty("statusFilterBool","Y");
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
 <SCRIPT type="text/javascript" language=JavaScript>
  function printView()
  {
    url="ls_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle, "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFileName %>&status1Filter=<%= status1Filter %>&status2Filter=<%= status2Filter %>&ownerFilter=<%= ownerFilter %><%= (filterXslFile!=null ? "&filterXslFile="+filterXslFile : "") %>&repType=HTML&t=<%= time %>"
    window.open(url);
  }

  function getCsv()
  {
    url="ls_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle, "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFileName %>&status1Filter=<%= status1Filter %>&status2Filter=<%= status2Filter %>&ownerFilter=<%= ownerFilter %><%= (filterXslFile!=null ? "&filterXslFile="+filterXslFile : "") %>&repType=CSV&t=<%= time %>"
    window.open(url);
  }

  function submitInsertNewLineset(action)
  {
    document.lsActionForm.action.value=action;
    document.lsActionForm.submit();
  }

  function submitDownloadLineset(action)
  {
    var count = checkNofSelItems();
    if(( count > 1) || (count == 0))
    {
        return;
    }
    else
    {
        var pos = getPosition();
        document.lsActionForm.action.value=action;
        document.lsActionForm.elements['FIELD.LS_NAME'].value=document.viewLsForm.elements['lsName_'+pos].value;
        document.lsActionForm.elements['FIELD.BASELINE'].value=document.viewLsForm.elements['lsBaseline_'+pos].value;
        document.lsActionForm.submit();
    }

  }

  function goLsDownloadFilePreview(action,lsName,baseline)
  {
       document.lsActionForm.action.value=action;
       document.lsActionForm.elements['FIELD.LS_NAME'].value=lsName;
       document.lsActionForm.elements['FIELD.BASELINE'].value=baseline;
       document.lsActionForm.submit();
  }


  function start()
  {
   <%
    if (startBool)
    {%>
       var delay = setTimeout("location.reload()",<%= refreshTime %>*1000)
    <%}
   %>
  }

  function go_to(lineset_name)
  {
    document.viewLsForm.elements['FIELD.LS_NAME'].value=lineset_name;
    document.viewLsForm.submit();
  }

  function checkNofSelItems()
  {
    cont=0;
    for (i=0; i< document.viewLsForm.elements.length; i++)
    {
	if (document.viewLsForm.elements[i].type=='checkbox')
	{
          if (document.viewLsForm.elements[i].checked) cont++;
          if (cont>1)
          {
          	document.viewLsForm.elements[i].checked=false;
		break;
	  }
	}
    }
    if (cont>1)
    {
      alert('YOU CANNOT SELECT MORE THAN ONE LINESET');
    }
    if (cont==0)
    {
      alert('AT LEAST ONE LINESET MUST BE SELECTED!');
    }
    return cont;
  }

  function getPosition()
  {
    pos=0;
    for (i=0; i< document.viewLsForm.elements.length; i++)
    {
	  if (document.viewLsForm.elements[i].type == 'checkbox')
	  {
          if (document.viewLsForm.elements[i].checked)
          {
		    return (document.viewLsForm.elements[i].name).substring(3);
          }
	  }
    }

    return pos;
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
             <FORM name="lsActionForm" action="lsActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
             <%
                if (qryType.equals("vob_lineset_history"))
            {
             %>
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="<%=workDir%>">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.LS_NAME" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.BASELINE" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
            <%
            }
            %>
             </FORM>


            <FORM name="viewLsForm" action="lsDetailViewServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="FIELD.LS_NAME" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="<%=qryType%>">
             <INPUT TYPE="HIDDEN" NAME="xmlFileName" VALUE="<%= repFileName %>">
                 <TR>
                  <TD ALIGN="LEFT">
                  <table cellpadding="0" cellspacing="0" border="0" width="760" >
                    <tr>
                      <td width="4"><IMG ALT="" src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                      <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;MY LINESETS REPORT&nbsp;</b></td>
                      <td width="4"><IMG ALT="" src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                      <td><IMG ALT="" src="img/pix.gif" width="553" height="18" alt="" border="0"></td>
                    </tr>
                    <tr>
                      <td colspan="4"><IMG ALT="" src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
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

            <!-- BUTTONS INSERT NEW LINESET-->
           <%
            if ((qryType.equals("vob_my_linesets"))&&((String)session.getAttribute("workMode")).equals("SENDWORK"))
            {%>

              <table cellpadding="0" cellspacing="0" border="0" width="760">
                <tr>
                  <td><IMG ALT="" src="img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><IMG ALT="" src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		        <tr>
                 <td align="right">
     		     <!-- BUTTON INSERT NEW LINESET-->
                 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		      <TR>
                  <TD valign="center"><IMG ALT="" SRC="img/btn_left.gif"></TD>
                  <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitInsertNewLineset('ls_submit')">&nbsp;INSERT NEW LINESET</A></TD>
                  <TD valign="center" ><IMG ALT="" SRC="img/btn_right.gif"></TD>
                  <TD><IMG ALT="" src="img/pix.gif" width="5" border="0"></TD>
	              </TR>
                  </TABLE>
                  </td>
 		</tr>
              </table>

              </TD>
             </TR>
        <%}
      %>
        <!-- /BUTTONS INSERT NEW LINESET-->

     <!-- BUTTONS COMPARE LINESET-->
           <%
            if (qryType.equals("vob_lineset_history"))
            {%>

              <table cellpadding="0" cellspacing="0" border="0" width="760">
                <tr>
                  <td><IMG ALT="" src="img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><IMG ALT="" src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		        <tr>
                 <td align="right">
     		     <!-- BUTTON COMPARE-->
                 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		      <TR>
                  <TD valign="center"><IMG ALT="" SRC="img/btn_left.gif"></TD>
                  <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitDownloadLineset('ls_download')">&nbsp;DOWNLOAD LINESET</A></TD>
                  <TD valign="center" ><IMG ALT="" SRC="img/btn_right.gif"></TD>
                  <TD><IMG ALT="" src="img/pix.gif" width="5" border="0"></TD>
	              </TR>
                  </TABLE>
                  </td>
 		       </tr>
              </table>


        <%}
      %>
        <!-- /BUTTONS COMPARE LINESET-->
 <%}
 %>

<%@ include file="bottom.jsp" %>

</BODY>
</HTML>
