<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%String contextPath = request.getContextPath();%>
<%
    String repFileName=request.getParameterValues("repFileName")[0];
    String time = new Long(System.currentTimeMillis()).toString();
    String reqId=request.getParameterValues("reqId")[0];
    String webAppDir=getServletContext().getInitParameter("webAppDir");
    String xslFileName=webAppDir.concat("/xsl/"+"ls_align_preview_rep.xsl");
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

   function go_to_in_costruction_page()
  {
    alert("TPMS FUNCTIONALITY IN COSTRUCTION");
    return;
  }

  function submitLsFileDiff(file)
  {
        document.lsActionFormFileDiff.lsFile.value=file;
        document.lsActionFormFileDiff.elements['FIELD.FILE_PATH'].value=file;
        document.lsActionFormFileDiff.elements['FIELD.LS_NAME'].value=document.lsActionForm.elements['FIELD.LS_NAME'].value;
        document.lsActionFormFileDiff.elements['FIELD.BASELINE1'].value=document.lsActionForm.elements['FIELD.BASELINE1'].value;
        document.lsActionFormFileDiff.elements['FIELD.BASELINE2'].value=document.lsActionForm.elements['FIELD.BASELINE2'].value;
        document.lsActionFormFileDiff.submit();
  }

  function selectAll()
  {
    for (i=0; i< document.lsActionForm.elements.length; i++)
    {
       if (document.lsActionForm.elements[i].type=='checkbox')
       {
            if(document.lsActionForm.ALL.checked)
            {

                document.lsActionForm.elements[i].checked = true;
            }else{

                document.lsActionForm.elements[i].checked = false;
            }
       }
    }
  }
  </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="LINESET ALIGN PREVIEW"; %>
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
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="ls_align">
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="<%= fromEmail%>">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_EMAIL" VALUE="<%= fromEmail%>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
             <INPUT TYPE="HIDDEN" NAME="FIELD.SUBMIT_STATUS" VALUE="INPROGRESS">
             <!--inizio - sezione GENERALE-->
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
                  props.setProperty("filter","COMPARE");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <!--fine - sezione GENERALE-->
             <!-- inizio - sezione SUMMARY -->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->SUMMARY</b></td>
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
                  props.setProperty("filter","SUMM");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <!--fine - sezione SUMMARY-->
             
             <!--inizio - sezione FILES REFERENCED BY XFER NO FOUND-->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->UNEXISTING FILES REFERENCED INTO XFER</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
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
                  props.setProperty("filter","BS4");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>

             <!--fine - sezione FILES REFERENCED BY XFER NO FOUND-->

             
             <!--inizio - sezione NEW FILES-->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->NEW FILES REFERENCED BY XFER</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
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
       props.setProperty("filter","BS2");
       xslProps.addElement(props);
       xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>


             <!--fine - sezione NEW FILES-->

             <!--inizio - sezione REMOVED-->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->FILES NO MORE FOUND</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
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
                  props.setProperty("filter","BS1");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>

             <!--fine - sezione REMOVED-->
             
             <!--inizio - sezione FILES MODIFIED-->
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->MODIFIED  FILES</b></td>
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
                  props.setProperty("filter","BOTH");
                  xslProps.addElement(props);
                  xmlRdr.transform(repFileName, xslFiles, xslProps, out);

              %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <!--fine - sezione FILES MODIFIED-->

             <!-- inizio - sezione COMMENT -->
            <TR>
              <TD ALIGN="LEFT">
              <BR>
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->COMMENT (max 10 rows)</b></td>
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
            <td class=testo width="40%" valign="top" colspan="4"><textarea name="comment" rows="4" cols="50"></textarea></td>
         </TR>

         <!--fine - sezione COMMENT-->

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
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitAction()">ALIGN</TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>

             <FORM name="lsActionFormFileDiff" action="lsActionServlet" method="post" target="_new">
              <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
              <INPUT TYPE="HIDDEN" NAME="action" VALUE="ls_align_file_diff">
              <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="FIELD.LS_NAME" VALUE="">
 	          <INPUT TYPE="HIDDEN" NAME="FIELD.BASELINE1" VALUE="">
 	          <INPUT TYPE="HIDDEN" NAME="FIELD.BASELINE2" VALUE="">
 	          <INPUT TYPE="HIDDEN" NAME="FIELD.FILE_PATH" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="lsFile" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>">
             </FORM>

<%}
%>

<%@ include file="bottom.jsp" %>

</BODY>
</HTML>
