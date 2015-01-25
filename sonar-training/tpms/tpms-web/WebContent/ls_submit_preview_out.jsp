<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%
    String repFileName=request.getParameterValues("repFileName")[0];
    String time = new Long(System.currentTimeMillis()).toString();
    String reqId=request.getParameterValues("reqId")[0];
    String webAppDir=getServletContext().getInitParameter("webAppDir");
    String xslFileName=webAppDir.concat("/xsl/"+"ls_submit_preview_rep.xsl");
    String fromEmail = request.getParameterValues("fromEmail")[0];
    Vector xslFiles=new Vector();
    Vector xslProps=new Vector();
    Properties props=new Properties();
    xslFiles.addElement(xslFileName);
    String actionTxt=(String)request.getParameterValues("actionTxt")[0];
    String refreshTime=(String)request.getParameterValues("refreshTime")[0];
    boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();

    Element userData=CtrlServlet.getUserData((String)session.getAttribute("user"));
    String homeDir=xmlRdr.getVal(userData,"HOME_DIR");

    if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
  <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
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


   function submitAction()
   {
        document.lsActionForm.submit();
   }
  </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="NEW LINESET SUBMIT PREVIEW"; %>
<%@ include file="top.jsp" %>
<%
   if (startBool)
   {%>
    <%@ include file="waitmsg.jsp" %>
 <%}
   else
   {%>



	    <FORM name="lsActionForm" action="lsActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="ls_submit">
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="<%= fromEmail%>">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_EMAIL" VALUE="<%= fromEmail%>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.SUBMIT_STATUS" VALUE="INPROGRESS">
             <!--inizio - sezione LS_INFO-->
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->LINESET INFO</b></td>
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
                  props.setProperty("filter","LS_INFO");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <!--fine - sezione LS_INFO-->

             <!--inizio - sezione XFER_LIST-->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->XFER LIST</b></td>
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
                  props.setProperty("filter","XFER_LIST");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <!--fine - sezione XFER_LIST-->

             <!--inizio - sezione NOT_FOUND_FILES-->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->NOT FOUND FILES</b></td>
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
                  props.setProperty("filter","NOT_FOUND_FILES");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <!--fine - sezione NOT_FOUND_FILES-->

             <!--inizio - sezione NOT_REFERENCED_FILES-->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->NOT REFERENCED FILES</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="523" height="18" alt="" border="0"></td>
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
                  props.setProperty("filter","NOT_REFERENCED_FILES");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <!--fine - sezione NOT_REFERENCED_FILES-->

            </FORM>

             <TR>
    	      <td>
	      <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
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
                    <TD valign="center" align="right"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitAction()">SUBMIT</TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>

<%}
%>
<%@ include file="bottom.jsp" %>

</BODY>
</HTML>
