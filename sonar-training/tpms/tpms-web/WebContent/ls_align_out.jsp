<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
    isErrorPage="false"
    errorPage="uncaughtErr.jsp"
%>
<%
    String time = new Long(System.currentTimeMillis()).toString();
    String reqId=request.getParameterValues("reqId")[0];
    String repFileName=request.getParameterValues("repFileName")[0];
    String webAppDir=getServletContext().getInitParameter("webAppDir");
    String xslFileName=webAppDir.concat("/xsl/"+"ls_align_rep.xsl");
    // ATTENZIONE: temporaneo x tests --> da mandare webAppDir
    //String xslFileName="/users/vobadm/jakarta-tomcat-3.2.3/webapps/tpms" + "/xsl/"+"ls_align_rep.xsl";
    // FINE TEMPORANEO
    Vector xslFiles=new Vector();
    Vector xslProps=new Vector();
    Properties props=new Properties();
    xslFiles.addElement(xslFileName);
    String actionTxt=(String)request.getParameterValues("actionTxt")[0];
    String refreshTime=(String)request.getParameterValues("refreshTime")[0];
    boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
    //String webAppDir=getServletContext().getInitParameter("webAppDir");
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

  function viewLs()
  {
    document.viewLsForm.submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="ALIGN LINESET "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>
<%
   if (startBool)
   {%>
    <%@ include file="waitmsg.jsp" %>
 <%}
   else
   {%>
           
             <!--inizio - sezione GENERALE-->
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->LINESET ALIGN RESULTS</b></td>
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
             
 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
