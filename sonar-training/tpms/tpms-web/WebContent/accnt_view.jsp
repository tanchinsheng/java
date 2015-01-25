<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="tpms.VobManager"%>
<%@ page import="java.util.Properties"%>
<%@ page
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>
<%String contextPath = request.getContextPath();%>
<%  

  String webAppDir = config.getServletContext().getInitParameter("webAppDir");
  Element accntRec = (Element)request.getAttribute("accntRec");
  Properties props=new Properties();
  props.setProperty("name",XmlUtils.getVal(accntRec, "NAME"));
  if ((XmlUtils.getVal(accntRec,"DVL_VOB")!=null)&&(!XmlUtils.getVal(accntRec,"DVL_VOB").equals("")))
  {
      props.setProperty("dvlVobDesc",VobManager.getVobDesc(XmlUtils.getVal(accntRec,"DVL_VOB")));
  }
  if ((XmlUtils.getVal(accntRec,"REC_VOB")!=null)&&(!XmlUtils.getVal(accntRec,"REC_VOB").equals("")))
  {
      props.setProperty("recVobDesc", VobManager.getVobDesc(XmlUtils.getVal(accntRec,"REC_VOB")));
  }
  boolean isPwdModificationEnabled = XmlUtils.getChild(accntRec, "PWD").getAttribute("edit").equals("Y");
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function submitForm(action)
  {
    document.userProfileForm.action.value=action;	
    document.userProfileForm.submit();
  } 
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="USER PROFILE PAGE"; %>
<%@ include file="top.jsp" %>

	    <FORM name="userProfileForm" action="<%=contextPath%>/userProfileServlet" method="post">
             <INPUT TYPE="HIDDEN" NAME="action" VALUE="edit">
             <INPUT TYPE="HIDDEN" NAME="accntName" VALUE="<%= XmlUtils.getVal(accntRec,"NAME") %>">
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;USER PROFILE DATA&nbsp;</b></td>
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
                 XmlUtils.transform(accntRec.getOwnerDocument(), webAppDir+"/xsl/accnt_view.xsl", props, out);
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
		      <%
			if (isPwdModificationEnabled)
			{%>
                          <TD valign="center"><IMG alt="" SRC="img/btn_left.gif"></TD>
                          <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm('edit_pwd')">&nbsp;MODIFY PASSWORD</a></TD>
                          <TD valign="center" ><IMG alt="" SRC="img/btn_right.gif"></TD>
			<%}
		      %>
		      <TD valign="center" ><IMG alt="" SRC="img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG alt="" SRC="img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm('edit')">&nbsp;MODIFY DATA</a></TD>
                      <TD valign="center" ><IMG alt="" SRC="img/btn_right.gif"></TD>
	           </TR>
                  </TABLE>
                </td>
 		</tr>	
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>


<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
