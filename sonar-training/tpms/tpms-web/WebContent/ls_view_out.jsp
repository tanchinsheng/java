<%@ page import="org.w3c.dom.Element"%>
<%@ page import="tpms.CtrlServlet"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Properties"%>
<%@ page import="tol.xmlRdr"%>
<%@ page import="it.txt.tpms.servlets.SendLineset"%>
<%@ page import="it.txt.tpms.servlets.LocalLinesetSharing" %>
<%@ page isErrorPage="false" errorPage="uncaughtErr.jsp"%>



<%
    String time = Long.toString(System.currentTimeMillis());
    String reqId = request.getParameter("reqId");

    String webAppDir = config.getServletContext().getInitParameter("webAppDir");
    Element lsRec = (Element)session.getAttribute("lsRec");

    String xmlFileName = request.getParameter("xmlFileName");

    String repFileName = (request.getParameter("repFileName") != null ? request.getParameter("repFileName") : xmlFileName);

    String showTpList = request.getParameter("showTpList");
    String qryType = request.getParameter("qryType");
    String xslFileName;
    if (qryType.equals("ls_details_for_tp_delete"))
    {
        xslFileName=webAppDir.concat("/xsl/"+"ls_view_vob_tp_delete.xsl");
    }else{
        xslFileName=webAppDir.concat("/xsl/"+"ls_view_vob.xsl");
    }
    String user=(String)session.getAttribute("user");
    String unixUser=(String)session.getAttribute("unixUser");
    Element userData= CtrlServlet.getUserData(user);
    String userRole=XmlUtils.getVal(userData, "ROLE");

    String refreshTime=request.getParameter("refreshTime");
    String repTitle=request.getParameter("repTitle");
    String actionTxt=(String)request.getParameter("actionTxt");

    String localPlant = config.getServletContext().getInitParameter("localPlant");

    String ownerFilter=request.getParameter("ownerFilter");

    //FF 12/09/2005: added move lineset func.
    String homeDir = XmlUtils.getVal(userData, "HOME_DIR");
    String curDirPath = "";
    String baseDirShow = "";
    if(request.getAttribute("curDirPath") != null)
    {
      curDirPath = (String)request.getAttribute("curDirPath");
      baseDirShow = curDirPath.substring(homeDir.length());
    }
    String baseDir = homeDir.concat(baseDirShow);
    //FF 12/09/2005: end add
    String contextPath = request.getContextPath();

    Vector xslFiles=new Vector();
    Vector xslProps=new Vector();
    Properties props=new Properties();
    xslFiles.addElement(xslFileName);

    boolean startBool=(session.getAttribute("startBool"+"_"+reqId) == null ? false : ((Boolean)session.getAttribute("startBool"+"_"+reqId)).booleanValue());
    if ((!startBool)&&(session.getAttribute("exception"+"_"+reqId) != null)) throw (Exception)session.getAttribute("exception"+"_"+reqId);

%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type="text/css" rel=stylesheet>
 <SCRIPT type="text/JavaScript" language="JavaScript">

  function start()
  {
   <%
    if (startBool)
    {%>
       var delay = setTimeout("location.reload()",<%= refreshTime %>*1000)
    <%}
   %>
  }

  function printView()
  {
    url="tp_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode("LINESET DATA REPORT", "UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=ls_view_vob.xsl&status1Filter=&status2Filter=&userFilter=&repType=HTML&t=<%= time %>"
    window.open(url);
  }

  function alignLsAction(action)
  {
    document.lsActionForm.action.value=action;
    document.lsActionForm.submit();
  }

  function submitTpAction(action)
  {
    document.tpActionForm.action.value=action;
    document.tpActionForm.elements['FIELD.LINESET'].value=document.lsActionForm.elements['FIELD.LS_NAME'].value;
    document.tpActionForm.elements['FIELD.LASTBASELINE'].value=document.lsActionForm.lastbaseline.value;
    document.tpActionForm.elements['FIELD.TESTERFAM'].value=document.lsActionForm.tester_fam.value;
    document.tpActionForm.elements['FIELD.XFER_PATH'].value=document.lsActionForm.home_dir.value;
    document.tpActionForm.submit();
  }

  function submitTpMultiAction(action)
  {
    if(checkNofSelItems() == 0)
    {
        return;
    }
    else
    {
        document.tpMultiActionForm.action.value=action;
        document.tpMultiActionForm.submit();
    }
  }

  function showTpList(qryType)
  {
    document.viewLsForm.qryType.value=qryType;
    document.viewLsForm.elements['FIELD.LS_NAME'].value=document.lsActionForm.elements['FIELD.LS_NAME'].value;
    document.viewLsForm.submit();
  }

  function deleteLineset()
  {
    var linesetName = document.lsActionForm.elements['FIELD.LS_NAME'].value;
    if(confirm("ATTENTION!! This action will remove permanently the Lineset " + linesetName + " ! \nDo you want to continue?")){
        document.deleteLsForm.elements['FIELD.LS_NAME'].value = linesetName;
        document.deleteLsForm.submit();
    }else{
        return;
    }
  }

  function moveLineset() {
    document.moveLineSetAction.submit();
  }

  function sendLineset() {
      document.sendLineset.submit();
  }

  function shareLineset() {
      document.shareLineset.submit();
  }

  function go_to(job,rel,rev,ver)
  {
    document.viewTpForm.jobname.value=job;
    document.viewTpForm.job_rel.value=rel;
    document.viewTpForm.job_rev.value=rev;
    document.viewTpForm.job_ver.value=ver;
    document.viewTpForm.submit();
  }

  function go_to_delete(job,rel,action)
  {
    document.viewTpForm.jobname.value=job;
    document.viewTpForm.job_rel.value=rel;
    document.viewTpForm.submit();
  }


  function checkNofSelItems()
  {
    cont=0;

    for (i=0; i< document.tpMultiActionForm.elements.length; i++)
    {
	   if (document.tpMultiActionForm.elements[i].type=='checkbox')
	   {
          if (document.tpMultiActionForm.elements[i].checked) cont++;
	   }
    }
    if (cont==0)
    {
      alert('AT LEAST ONE TP MUST BE SELECTED!');
    }

    return cont;
  }

 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<%  boolean repBool=!startBool; boolean csvRepBool=!startBool; String pageTitle=repTitle; %>
<%@ include file="top.jsp" %>
<%
if (startBool)
 {%>
   <%@ include file="waitmsg.jsp" %>
 <%}
 else
 {%>

	    <FORM name="viewTpForm" action="tpDetailViewServlet" method="post">
	     <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%=repFileName%>">
	     <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="">
	     <INPUT TYPE="HIDDEN" NAME="source"  VALUE="vob">
         <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="">
        </FORM>



<FORM name="deleteLsForm" action="lsActionServlet" method="post">
	        <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
            <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
            <INPUT TYPE="HIDDEN" NAME="FIELD.LS_NAME" VALUE="">
            <INPUT TYPE="HIDDEN" NAME="action" VALUE="ls_delete">
	        <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
        </FORM>

        <FORM name="lsActionForm" action="lsActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><IMG ALT="" src="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="<%=contextPath%>/img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp&nbsp;LINESET&nbsp;DATA&nbsp;&nbsp</b></td>
                  <td width="4"><IMG ALT="" src="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="603" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><IMG ALT="" src="<%=contextPath%>/img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
             if (qryType.equals("vob_lineset_submits"))
             {
                props.setProperty("statusFilterBool","Y");
             }
             props.setProperty("filter","LS");
             xslProps.addElement(props);
             if (xmlFileName!=null)
             {
               XmlUtils.applyXSL(lsRec.getOwnerDocument(), xslFileName, props, out);
             }
             else
             {
               xmlRdr.transform(repFileName, xslFiles, xslProps, out);
             }
            %>
	       <!-- /REPORT -->
              </TD>
             </TR>
            </FORM>

<FORM name="moveLineSetAction" action="<%=contextPath%>/lsActionServlet" method="post">
     <INPUT TYPE="HIDDEN" NAME="action" VALUE="ls_move">
     <INPUT TYPE="HIDDEN" NAME="xslFileName" VALUE="<%=xslFileName%>">
 </FORM>

 <FORM name="sendLineset" action="<%=contextPath%>/sendLineset" method="post">
     <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="<%=SendLineset.INITIALIZE_SEND_LINESET_ACTION%>">
     <INPUT TYPE="hidden" NAME="<%=SendLineset.LS_NAME_PARAMETER%>" VALUE="<%=XmlUtils.getTextValue(lsRec, "LS_NAME")%>">
     <INPUT TYPE="hidden" NAME="<%=SendLineset.LS_BASE_DIR_PARAMETER%>" VALUE="<%=XmlUtils.getTextValue(lsRec, "UNIX_HOME")%>">
     <INPUT TYPE="hidden" NAME="<%=SendLineset.LS_BASELINE_PARAMETER%>" VALUE="<%=XmlUtils.getTextValue(lsRec, "LASTBASELINE")%>">
     <INPUT TYPE="hidden" NAME="<%=SendLineset.LS_OWNER_PARAMETER%>" VALUE="<%=XmlUtils.getTextValue(lsRec, "LS_OWNER")%>">
     <INPUT TYPE="hidden" NAME="<%=SendLineset.LS_TESTER_FAMILY_PARAMETER%>" VALUE="<%=XmlUtils.getTextValue(lsRec, "TESTERFAM")%>">
</FORM>

<FORM name="shareLineset" action="<%=contextPath%>/localLinesetSharing" method="post">
    <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="<%=LocalLinesetSharing.INITIALIZE_PAGE_DATA_ACTION%>">
    <input type="hidden" name="<%=LocalLinesetSharing.LINESET_NAME_FIELD%>" value="<%=XmlUtils.getTextValue(lsRec, "LS_NAME")%>">
    <input type="hidden" name="<%=LocalLinesetSharing.VOBNAME_FIELD%>" value="<%= session.getAttribute("vob") %>">
    <input type="hidden" name="<%=LocalLinesetSharing.LINESET_CREATOR_FIELD%>" value="<%= XmlUtils.getTextValue(lsRec, "ORIGINAL_OWNER") %>">
    <input type="hidden" name="<%=LocalLinesetSharing.SOURCE_XML_FILE%>" value="<%=xmlFileName%>">
</FORM>
            <!-- BUTTONS -->
           <%  if ((qryType.equals("vob_lineset_submits")) && (((String)session.getAttribute("workMode")).equals("SENDWORK"))&& (((String)XmlUtils.getVal(lsRec,"SUBMIT_STATUS")).equals("ABORTED")))
            {%>
             <TR>
    	      <TD class="testo">
              <br>

              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		        <tr>
                 <td align="right">
                 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		      <TR>
                      <!--DELETE LINESET-->
		              <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:deleteLineset()">DELETE LINESET</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

	              </TR>
                 </TABLE>
                 </td>
 		        </tr>
              </table>
              </TD>
             </TR>
             <%}%>
             <!-- /BUTTONS -->
             <!-- BUTTONS -->
           <%  if ((!qryType.equals("vob_lineset_submits"))&&((String)session.getAttribute("workMode")).equals("SENDWORK"))
            {%>
             <TR>
    	      <TD class="testo">
              <br>

              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		        <tr>
                 <td align="right">
                 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		      <TR>

                       <!-- insert new TP -->
		              <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitTpAction('tp_submit')">&nbsp;DELIVER NEW TP</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

                      <!-- SHOW TP LIST -->
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:showTpList('ls_query_details')">&nbsp;SHOW TP LIST</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

		              <!-- ALIGN LINESET -->
		              <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:alignLsAction('ls_align_preview')">ALIGN LINESET PREVIEW</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

                      <!--DELETE LINESET-->
		              <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:deleteLineset()">DELETE LINESET</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

                      <%--MOVE LINESET--%>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:moveLineset()">MOVE LINESET</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

                      <%--SEND LINESET--%>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:sendLineset()">SEND LINESET</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                      <%--SHARE LINESET--%>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:shareLineset()">SHARE LINESET</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                  </TR>
                 </TABLE>
                 </td>
 		        </tr>
              </table>
              </TD>
             </TR>
             <%}%>
             <!-- /BUTTONS -->

             <!-- BUTTONS -->
           <%  if ((!qryType.equals("vob_lineset_submits"))&&(userRole.equals("ADMIN")))
            {%>
             <TR>
    	      <TD class="testo">
              <br>

              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		        <tr>
                 <td align="right">
                 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		      <TR>
                      <!-- SHOW TP LIST -->
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:showTpList('ls_details_for_tp_delete')">&nbsp;SHOW TP LIST</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

                      <!--DELETE LINESET-->
		              <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:deleteLineset()">DELETE LINESET</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>

	              </TR>
                 </TABLE>
                 </td>
 		        </tr>
              </table>
              </TD>
             </TR>
             <%}%>
             <!-- /BUTTONS -->

             <FORM name="tpActionForm" action="tpActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%=repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.JOBNAME" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.JOB_REL" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.JOB_REV" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.JOB_VER" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.FACILITY" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.TESTERFAM" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.XFER_PATH" VALUE="">

             <INPUT TYPE="HIDDEN" NAME="FIELD.FROM_PLANT" VALUE="<%= localPlant %>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.LINESET" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.LASTBASELINE" VALUE="">
	         <INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_LOGIN" VALUE="<%= unixUser %>">
	         <INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_EMAIL" VALUE="<%= XmlUtils.getVal(userData,"EMAIL") %>">
             </FORM>

             <FORM name="tpMultiActionForm" action="tpMultiActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%=repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="testerFam" VALUE="<%=(String)XmlUtils.getVal(lsRec,"TESTERFAM") %>">
             <INPUT TYPE="HIDDEN" NAME="baseline" VALUE="<%=(String)XmlUtils.getVal(lsRec,"LASTBASELINE") %>">
   <%
    if (!showTpList.equals("Y")&&(!userRole.equals("ADMIN")))
    {%>
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><IMG ALT="" src="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="<%=contextPath%>/img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;LINESET&nbsp;TPs&nbsp;&nbsp</b></td>
                  <td width="4"><IMG ALT="" src="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="603" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><IMG ALT="" src="<%=contextPath%>/img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

         <TR>
          <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
                  props.setProperty("filter","TP");
                  props.setProperty("localPlant","N");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);
           %>
	      <!-- /REPORT -->
          </TD>
         </TR>
      <%}
    %>
    <%
    if (!showTpList.equals("Y")&&(userRole.equals("ADMIN")))
    {%>
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><IMG ALT="" src="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="<%=contextPath%>/img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;LINESET&nbsp;TPs&nbsp;&nbsp</b></td>
                  <td width="4"><IMG ALT="" src="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="603" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><IMG ALT="" src="<%=contextPath%>/img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

         <TR>
          <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
                  props.setProperty("filter","TPS_SUM");
                  props.setProperty("localPlant","N");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);
           %>
	      <!-- /REPORT -->
          </TD>
         </TR>
      <%}
    %>

         </FORM>
   <%
    if (!showTpList.equals("Y")&&(!userRole.equals("ADMIN")))
    {%>
         <TR>
    	      <TD class="testo">
              <br>
             <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><IMG ALT="" src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		        <tr>
                 <td align="right">
                 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		     <TR>
		          <!-- insert new TP -->
		              <%--<TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitTpAction('tp_submit')">&nbsp;DELIVER NEW TP</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>--%>
		          <!-- DELIVER TPs -->
		              <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="<%=contextPath%>/img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitTpMultiAction('tp_delivery')">&nbsp;DELIVER TPs</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                 <%--<!-- DELETE TPs -->
		              <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG ALT="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitTpMultiAction('tp_delete')">&nbsp;DELETE TPs</a></TD>
                      <TD valign="center" ><IMG ALT="" src="<%=contextPath%>/img/btn_right.gif"></TD>--%>
	             </TR>
                 </TABLE>
                 </td>
 		        </tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>

          <%} else if (!showTpList.equals("Y")&&(userRole.equals("ADMIN")))
         { %>

          <%} %>
            <FORM name="viewLsForm" action="lsDetailViewServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="FIELD.LS_NAME" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="">
            </FORM>


 <%}
%>
<%@ include file="bottom.jsp" %>

</BODY>
</HTML>
