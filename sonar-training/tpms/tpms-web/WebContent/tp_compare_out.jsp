<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.*"
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp"
%>


<%  
  String time = new Long(System.currentTimeMillis()).toString();
  String reqId=request.getParameterValues("reqId")[0];
  String repFileName=request.getParameterValues("repFileName")[0];
  String repTitle="TP COMPARE REPORT (VOB)";
  String jobLabel1=request.getParameterValues("jobLabel1")[0];	
  String jobLabel2=request.getParameterValues("jobLabel2")[0];	
  String xmlFileName=request.getParameterValues("xmlFileName")[0];
  String actionTxt=(String)request.getParameterValues("actionTxt")[0];
  String refreshTime=request.getParameterValues("refreshTime")[0];
  boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
  String webAppDir=getServletContext().getInitParameter("webAppDir");
  String xslFname="tp_compare_rep.xsl";
  String xslFileName=webAppDir+"/xsl/"+xslFname;
  Properties xslProps=new Properties();
  if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
%>
 <%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function printView()
  {
    url="tp_vob_csv.jsp?repTitle=<%= java.net.URLEncoder.encode(repTitle,"UTF-8") %>&repFileName=<%= repFileName %>&xslFileName=<%= xslFname %>&status1Filter=&status2Filter=&userFilter=&repType=HTML&t=<%= time %>"
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


  function submitTpFileDiff(file)
  {
        document.tpActionFormFileDiff.tpFile.value=file;
        document.tpActionFormFileDiff.submit();
  }

  function submitDownloadTp1()
  {
    if(checkNofSelItemsTp1() == 0)
    {
        return;
    }
    else
    {
        document.tpActionForm1.submit();
    }
  }

  function submitDownloadTp2()
  {
    if(checkNofSelItemsTp2() == 0)
    {
        return;
    }
    else
    {
        document.tpActionForm2.submit();
    }
  }

  function submitDownloadTpBoth()
  {
    if(checkNofSelItemsTpBoth() == 0)
    {
        return;
    }
    else
    {
        document.tpActionFormBoth.submit();
    }
  }

  function checkNofSelItemsTp1()
  {
    cont=0;

    for (i=0; i< document.tpActionForm1.elements.length; i++)
    {
	   if (document.tpActionForm1.elements[i].type=='checkbox')
	   {
          if (document.tpActionForm1.elements[i].checked) cont++;
	   }
    }

    if (cont==0)
    {
      alert('AT LEAST ONE FILE MUST BE SELECTED!');
    }

    return cont;
  }
  function checkNofSelItemsTp2()
  {
    cont=0;

    for (i=0; i< document.tpActionForm2.elements.length; i++)
    {
	   if (document.tpActionForm2.elements[i].type=='checkbox')
	   {
          if (document.tpActionForm2.elements[i].checked) cont++;
	   }
    }

    if (cont==0)
    {
      alert('AT LEAST ONE FILE MUST BE SELECTED!');
    }

    return cont;
  }
  function checkNofSelItemsTpBoth()
  {
    cont=0;

    for (i=0; i< document.tpActionFormBoth.elements.length; i++)
    {
	   if (document.tpActionFormBoth.elements[i].type=='checkbox')
	   {
          if (document.tpActionFormBoth.elements[i].checked) cont++;
	   }
    }

    if (cont==0)
    {
      alert('AT LEAST ONE FILE MUST BE SELECTED!');
    }

    return cont;
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=!startBool; boolean csvRepBool=false; String pageTitle=repTitle; %>
<%@ include file="top.jsp" %>
<%
 if (startBool)
 {%>
   <%@ include file="waitmsg.jsp" %>
 <%}
 else 
 {%>
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->COMPARED TESTING PROGRAMS</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="480" height="18" alt="" border="0"></td>
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
                xmlRdr.transform(repFileName, xslFileName, null, out);
               %>
	       <!-- /REPORT -->
              </TD>	
             </TR>
             <TR>
    	      <TD class="testo">
  	      <!-- BUTTONS -->	
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><img src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>
	    <TR><TD><img src="img/blank.gif" border="0"></TD></TR>	


	    <FORM name="tpActionForm1" action="tpActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_download">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="jobLabel1" VALUE="<%= jobLabel1 %>">
	     <INPUT TYPE="HIDDEN" NAME="xmlFileName" VALUE="<%= xmlFileName %>">		
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">

            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->FILES FEATURED BY TP#1 ONLY</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="493" height="18" alt="" border="0"></td>
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
                
                xslProps.setProperty("filter","TP1");
                xmlRdr.transform(repFileName, xslFileName, xslProps, out);
               %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <TR>
    	      <TD class="testo">
              <br>	
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
                      <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitDownloadTp1()">&nbsp;DOWNLOAD</A></TD>			
                      <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                      <!--TD><img src="img/blank_flat_small.gif" alt="" border="0"></TD-->
	           </TR>
                  </TABLE>
                </td>
 		</tr>	
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>
            </FORM>
	
	    <TR><TD><img src="img/blank_flat.gif" border="0"></TD></TR>	
			

	    <FORM name="tpActionForm2" action="tpActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_download">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="jobLabel1" VALUE="<%= jobLabel2 %>">
	     <INPUT TYPE="HIDDEN" NAME="xmlFileName" VALUE="<%= xmlFileName %>">		
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->FILES FEATURED BY TP#2 ONLY</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="493" height="18" alt="" border="0"></td>
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
                xslProps.setProperty("filter","TP2");
                xmlRdr.transform(repFileName, xslFileName, xslProps, out);
               %>
	       <!-- /REPORT -->
              </TD>
             </TR>
             <TR>
    	      <TD class="testo">
              <br>	
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
                      <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitDownloadTp2()">&nbsp;DOWNLOAD</A></TD>			
                      <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                      <!--TD><img src="img/blank_flat_small.gif" alt="" border="0"></TD-->
	           </TR>
                  </TABLE>
                </td>
 		</tr>	
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>
            </FORM>
	
	    <TR><TD><img src="img/blank_flat.gif" border="0"></TD></TR>	

	    <FORM name="tpActionFormBoth" action="tpActionServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_download">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
             <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
 	    <INPUT TYPE="HIDDEN" NAME="jobLabel1" VALUE="<%= jobLabel1 %>">
 	    <INPUT TYPE="HIDDEN" NAME="jobLabel2" VALUE="<%= jobLabel2 %>">
 	    <INPUT TYPE="HIDDEN" NAME="xmlFileName" VALUE="<%= xmlFileName %>">		
            <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->COMMON FILES WITH SOME DIFFERENCES</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="413" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>
             <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
                
                xslProps.setProperty("filter","BOTH");
                xmlRdr.transform(repFileName, xslFileName, xslProps, out);
               %>
	       <!-- /REPORT -->
              </TD>
             </TR>
            </FORM>
             <TR>
    	      <TD class="testo">
              <br>	
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
                      <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitDownloadTpBoth()">&nbsp;DOWNLOAD</A></TD>			
                      <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                      <!--TD><img src="img/blank_flat_small.gif" alt="" border="0"></TD-->
	           </TR>
                  </TABLE>
                </td>
 		</tr>	
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>
	     <FORM name="tpActionFormFileDiff" action="tpActionServlet" method="post" target="_new">
              <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
              <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_file_diff">
              <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
              <INPUT TYPE="HIDDEN" NAME="workDir" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="toEmail" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE="">
 	          <INPUT TYPE="HIDDEN" NAME="jobLabel1" VALUE="<%= jobLabel1 %>">
              <INPUT TYPE="HIDDEN" NAME="jobLabel2" VALUE="<%= jobLabel2 %>">
              <INPUT TYPE="HIDDEN" NAME="tpFile" VALUE="">
              <INPUT TYPE="HIDDEN" NAME="compareRepFileName" VALUE="<%= repFileName %>">
              <INPUT TYPE="HIDDEN" NAME="xmlFileName" VALUE="<%= xmlFileName %>">
             </FORM>

 <%}
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
