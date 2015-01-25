<%@ page import="java.util.*, java.io.*, 
		 org.w3c.dom.*, 
		 tol.xmlRdr"
         isErrorPage="false" 
         errorPage="uncaughtErr.jsp" 
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <script language="javascript" src="<%=contextPath%>/js/navdir_funcs.js"></script>
</HEAD>
<LINK href="default.css" type=text/css rel=stylesheet>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="WORK DIRECTORY BROWSE PAGE"; %>
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

	    <FORM name="selDirForm" action="navDirServlet" method="post" target="_new">
	     <INPUT TYPE="HIDDEN" NAME="action" VALUE="select">	
         <INPUT TYPE="HIDDEN" name="nextPage" value="nav_dir_out.jsp">
 	     <INPUT TYPE="HIDDEN" name="outPage" value="file_view.jsp">
 	     <INPUT TYPE="HIDDEN" name="curDirPath" value="<%= request.getAttribute("curDirPath") %>">
 	     <INPUT TYPE="HIDDEN" name="nextDirPath" value="<%= request.getAttribute("curDirPath") %>">
 	     <INPUT TYPE="HIDDEN" name="editFile" value="<%= request.getAttribute("curDirPath") %>">
 	     <INPUT TYPE="HIDDEN" name="selFile" value="">
	     
             <TR>
              <TD ALIGN="LEFT">


<!-- REPORT -->
<%
  Properties props=new Properties();
  props.setProperty("enableSelection","N");
  props.setProperty("dirOnly","N");
  xmlRdr.applyXSL((Document)request.getAttribute("dirContentXml"), getServletContext().getInitParameter("webAppDir")+"/navDir.xsl", props, out); 
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
              <!-- /BUTTONS -->
              </TD>
             </TR>
            </FORM>

<%@ include file="bottom.jsp" %>

       <FORM name="navDirActionForm" action="navDirServlet" method="post">
 	<INPUT TYPE="HIDDEN" name="nextPage" value="nav_dir_out.jsp">
 	<INPUT TYPE="HIDDEN" name="outPage" value="sel_dir_out.jsp">
 	<INPUT TYPE="HIDDEN" name="curDirPath" value="<%= request.getAttribute("curDirPath") %>">
 	<INPUT TYPE="HIDDEN" name="nextDirPath" value="">
       </FORM>

</BODY>
</HTML>
