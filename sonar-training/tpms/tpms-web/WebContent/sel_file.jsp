<%@ page import="java.util.*, java.io.*,
                 org.w3c.dom.*,
                 tol.xmlRdr"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%
         String repFileName=(String)request.getAttribute("repFileName");
         String jobname=(String)request.getAttribute("jobname");
         String job_rel=(String)request.getAttribute("job_rel");
         String job_rev=(String)request.getAttribute("job_rev");
         String job_ver=(String)request.getAttribute("job_ver");
         String fromEmail=(String)request.getAttribute("fromEmail");
         String ownerEmail=(String)request.getAttribute("ownerEmail");
         String facility=(String)request.getAttribute("facility");
         String testerInfo=(String)request.getAttribute("testerInfo");
         String toPlant=(String)request.getAttribute("toPlant");
         String prod_login=((String)request.getAttribute("prod_login"));
         String valid_login=((String)request.getAttribute("valid_login"));
         String email_to=((String)request.getAttribute("email_to"));
         String email_cc=((String)request.getAttribute("email_cc"));
         String certification=((String)request.getAttribute("certification"));
         String line=(request.getAttribute("line")!= null ? (String)request.getAttribute("line") : "");
         String nextPage=(String)request.getAttribute("nextPage");
         String outPage=(String)request.getAttribute("outPage");
         String area_prod=(String)request.getAttribute("area_prod");
         String singleDeliveryCmt=(String)request.getAttribute("singleDeliveryCmt");
         String deliveryComm=(String)request.getAttribute("deliveryComm");
         String hw_modification=(String)request.getAttribute("hw_modification");
         String exp_avg_yv=(String)request.getAttribute("exp_avg_yv");
         String zero_yw=(String)request.getAttribute("zero_yw");
         String new_testtime=(String)request.getAttribute("new_testtime");
         String is_temp=(String)request.getAttribute("is_temp");
         String valid_till=(String)request.getAttribute("valid_till");
         String dis_email=(String)request.getAttribute("dis_email");
         String testcount=(String)request.getAttribute("testcount");
         
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <script language="javascript" src="<%=contextPath%>/js/navdir_funcs.js"></script>
 <script language="javascript">
   function selFile(fileName)
   {
    document.viewFileForm.selFile.value=fileName;
	document.viewFileForm.submit();
   }

 </script>
</HEAD>
<LINK href="default.css" type=text/css rel=stylesheet>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle=(String)request.getAttribute("pageTitle"); %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx_file.gif" width="4" height="21" alt="" border="0"></td>
        	  <td background="img/tit_center_file.gif" align="center" class="titverdifl" nowrap><!-- FORM TITLE--><font color="#19559E" face="Courier"><%= request.getAttribute("curDirPath") %></font></td>
                  <td width="4"><img src="img/tit_dx_file.gif" width="4" height="21" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="<%= 603+80-(((String)request.getAttribute("curDirPath")).length()*8) %>" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>


	    <FORM name="viewFileForm" action="navDirServlet" method="post" target="_new">
	     <INPUT TYPE="HIDDEN" NAME="action" VALUE="select">
             <INPUT TYPE="HIDDEN" name="nextPage" value="sel_file.jsp">
 	     <INPUT TYPE="HIDDEN" name="outPage" value="file_view.jsp">
 	     <INPUT TYPE="HIDDEN" name="curDirPath" value="<%= request.getAttribute("curDirPath") %>">
 	     <INPUT TYPE="HIDDEN" name="nextDirPath" value="<%= request.getAttribute("curDirPath") %>">
             <INPUT TYPE="HIDDEN" name="initDirPath" value="<%= ((request.getParameterValues("initDirPath")==null)||(request.getParameterValues("initDirPath")[0].equals("WORK_DIR")) ? "WORK_DIR" : "HOME_DIR") %>">
             <INPUT TYPE="HIDDEN" name="editFile" value="<%= request.getAttribute("curDirPath") %>">
 	     <INPUT TYPE="HIDDEN" name="selFile" value=""><!-- mean for file content visualization -->
            </FORM>

	    <FORM name="selDirForm" action="navDirServlet" method="post">
	     <INPUT TYPE="HIDDEN" NAME="action" VALUE="select">
       	     <INPUT TYPE="HIDDEN" name="nextPage" value="<%= nextPage %>">
 	     <INPUT TYPE="HIDDEN" name="outPage" value="<%= outPage %>">
 	     <INPUT TYPE="HIDDEN" name="curDirPath" value="<%= request.getAttribute("curDirPath") %>">
 	     <INPUT TYPE="HIDDEN" name="nextDirPath" value="">
             <!-- new param -->
             <INPUT TYPE="HIDDEN" name="initDirPath" value="<%= ((request.getParameterValues("initDirPath")==null)||(request.getParameterValues("initDirPath")[0].equals("WORK_DIR")) ? "WORK_DIR" : "HOME_DIR") %>">
             <INPUT TYPE="HIDDEN" name="fileSelBool" value="Y">
             <INPUT TYPE="HIDDEN" name="editDir" value="<%= request.getAttribute("curDirPath") %>">
             <INPUT TYPE="HIDDEN" name="editFile" value="">
             <!-- /new params -->


             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="<%= jobname %>">
             <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="<%= job_rel %>">
             <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="<%= job_rev %>">
             <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="<%= job_ver %>">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="<%= fromEmail %>">
             <INPUT TYPE="HIDDEN" NAME="ownerEmail" VALUE="<%= ownerEmail %>">
             <INPUT TYPE="HIDDEN" NAME="facility" VALUE="<%= facility %>">
             <INPUT TYPE="HIDDEN" NAME="testerInfo" VALUE="<%= testerInfo %>">
             <INPUT TYPE="HIDDEN" NAME="toPlant" VALUE="<%= toPlant %>">

             <INPUT TYPE="HIDDEN" NAME="line" VALUE="<%=line%>">
             <INPUT TYPE="HIDDEN" NAME="valid_login" VALUE="<%=valid_login%>">
             <INPUT TYPE="HIDDEN" NAME="prod_login" VALUE="<%=prod_login%>">
             <INPUT TYPE="HIDDEN" NAME="email_to" VALUE="<%=email_to%>">
             <INPUT TYPE="HIDDEN" NAME="email_cc" VALUE="<%=email_cc%>">
             <INPUT TYPE="HIDDEN" NAME="certification" VALUE="<%= certification %>">
             <INPUT TYPE="HIDDEN" NAME="area_prod" VALUE="<%= area_prod %>">
             <INPUT TYPE="HIDDEN" NAME="singleDeliveryCmt" VALUE="<%= singleDeliveryCmt %>">
             <INPUT TYPE="HIDDEN" NAME="deliveryComm" VALUE="<%= deliveryComm %>">
             <INPUT TYPE="HIDDEN" NAME="hw_modification" VALUE="<%= hw_modification %>">
             <INPUT TYPE="HIDDEN" NAME="exp_avg_yv" VALUE="<%= exp_avg_yv %>">
             <INPUT TYPE="HIDDEN" NAME="zero_yw" VALUE="<%= zero_yw %>">
             <INPUT TYPE="HIDDEN" NAME="new_testtime" VALUE="<%= new_testtime %>">
             <INPUT TYPE="HIDDEN" NAME="is_temp" VALUE="<%= is_temp %>">
             <INPUT TYPE="HIDDEN" NAME="valid_till" VALUE="<%= valid_till %>">
             <INPUT TYPE="HIDDEN" NAME="dis_email" VALUE="<%= dis_email %>">
             <INPUT TYPE="HIDDEN" NAME="testcount" VALUE="<%= testcount %>">
                       

             <TR>
              <TD ALIGN="LEFT">


<!-- REPORT -->
<%
        Properties props=new Properties();
        props.setProperty("enableSelection","N");
        props.setProperty("dirOnly","N");
        props.setProperty("enableFileSelection","Y");
        //props.setProperty("fileExt","sh");
        xmlRdr.applyXSL((Document)request.getAttribute("dirContentXml"), this.getServletConfig().getServletContext().getInitParameter("webAppDir")+"/navDir.xsl", props, out);
%>

<!-- /REPORT -->

               <BR>
              </TD>
             </TR>
             <TR>
    	      <td>
	      <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="560">
                <tr>
                  <td colspan="1"><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
	      </table>
	      <table width="560">
		   <tr>
                 <td align="right">
                  <table cellpadding="0" cellspacing="0" border="0">
                   <tr>
                         <td align="right">
                         <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                          <TR>
                               <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                               <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;</TD>
                               <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                           </TR>
                          </TABLE>
                         </td>
 		           </tr>
                  </table>
		        </td>
		   </tr>
	      </table>
          <!-- /BUTTONS -->
              </TD>
             </TR>
            </FORM>

<%@ include file="bottom.jsp" %>

       <FORM name="navDirActionForm" action="navDirServlet" method="post">
        <INPUT TYPE="HIDDEN" name="nextPage" value="<%= nextPage %>">
        <INPUT TYPE="HIDDEN" name="outPage" value="<%= outPage %>">
        <INPUT TYPE="HIDDEN" name="curDirPath" value="<%= request.getAttribute("curDirPath") %>">
        <INPUT TYPE="HIDDEN" name="nextDirPath" value="">
        <!-- new param -->
        <INPUT TYPE="HIDDEN" name="initDirPath" value="<%= ((request.getParameterValues("initDirPath")==null)||(request.getParameterValues("initDirPath")[0].equals("WORK_DIR")) ? "WORK_DIR" : "HOME_DIR") %>">
        <INPUT TYPE="HIDDEN" name="fileSelBool" value="Y">
        <!-- /new params -->

             <INPUT TYPE="HIDDEN" name="pageTitle" value="<%= pageTitle %>">
             <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
             <INPUT TYPE="HIDDEN" NAME="jobname" VALUE="<%= jobname %>">
             <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="<%= job_rel %>">
             <INPUT TYPE="HIDDEN" NAME="job_rev" VALUE="<%= job_rev %>">
             <INPUT TYPE="HIDDEN" NAME="job_ver" VALUE="<%= job_ver %>">
             <INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="<%= fromEmail %>">
             <INPUT TYPE="HIDDEN" NAME="ownerEmail" VALUE="<%= ownerEmail %>">
             <INPUT TYPE="HIDDEN" NAME="facility" VALUE="<%= facility %>">
             <INPUT TYPE="HIDDEN" NAME="testerInfo" VALUE="<%= testerInfo %>">
             <INPUT TYPE="HIDDEN" NAME="toPlant" VALUE="<%= toPlant %>">
             <INPUT TYPE="HIDDEN" NAME="line" VALUE="<%=line%>">
             <INPUT TYPE="HIDDEN" NAME="valid_login" VALUE="<%=valid_login%>">
             <INPUT TYPE="HIDDEN" NAME="prod_login" VALUE="<%=prod_login%>">
             <INPUT TYPE="HIDDEN" NAME="email_to" VALUE="<%=email_to%>">
             <INPUT TYPE="HIDDEN" NAME="email_cc" VALUE="<%=email_cc%>">
             <INPUT TYPE="HIDDEN" NAME="certification" VALUE="<%= certification %>">
           	 <INPUT TYPE="HIDDEN" NAME="area_prod" VALUE="<%= area_prod %>">
           	 <INPUT TYPE="HIDDEN" NAME="singleDeliveryCmt" VALUE="<%= singleDeliveryCmt %>">
 			 <INPUT TYPE="HIDDEN" NAME="deliveryComm" VALUE="<%= deliveryComm %>">
 			 <INPUT TYPE="HIDDEN" NAME="hw_modification" VALUE="<%= hw_modification %>">
             <INPUT TYPE="HIDDEN" NAME="exp_avg_yv" VALUE="<%= exp_avg_yv %>">
             <INPUT TYPE="HIDDEN" NAME="zero_yw" VALUE="<%= zero_yw %>">
             <INPUT TYPE="HIDDEN" NAME="new_testtime" VALUE="<%= new_testtime %>">
             <INPUT TYPE="HIDDEN" NAME="is_temp" VALUE="<%= is_temp %>">
             <INPUT TYPE="HIDDEN" NAME="valid_till" VALUE="<%= valid_till %>">
             <INPUT TYPE="HIDDEN" NAME="dis_email" VALUE="<%= dis_email %>">
             <INPUT TYPE="HIDDEN" NAME="testcount" VALUE="<%= testcount %>">
            

       </FORM>

</BODY>
</HTML>
