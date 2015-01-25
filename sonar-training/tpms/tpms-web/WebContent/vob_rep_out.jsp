
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="tpms.VobManager"%>
<%@ page import="java.util.Properties"%>
<%@ page import="org.w3c.dom.Element"%>
<%
  String webAppDir=config.getServletContext().getInitParameter("webAppDir");
  String plant=((String)request.getAttribute("plant")!= null ? (String)request.getAttribute("plant") : config.getServletContext().getInitParameter("localPlant"));
  String localPlant=config.getServletContext().getInitParameter("localPlant");
  Element vobRoot= VobManager.getVobsRoot();
  Properties props=new Properties();
  props.setProperty("plant",plant);
  props.setProperty("localPlant",localPlant);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>

 <SCRIPT language=JavaScript>
  function submitForm(type)
  {
    if (confirm("Retrieve VOB data (may take a while)\nDo whish to continue?")){
      document.vobForm.action.value='new';
      document.vobForm.type.value=type;
      document.vobForm.submit();
    }
  }


  function go_to(vob, action, type)
  {
    if (action=='delete')
    {
      ret=top.window.confirm("Do you want to delete " + vob + " VOB?");
      if (!ret) return;
    }
    document.vobForm.vobName.value=vob;
    document.vobForm.action.value=action;
    document.vobForm.type.value=type;
    document.vobForm.submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" >
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="VOBS REPORT"; %>
<%@ include file="top.jsp" %>

           <FORM name="vobForm" action="vobManagerServlet" method="post">
            <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
 	        <INPUT TYPE="HIDDEN" NAME="vobName" VALUE="">
            <INPUT TYPE="HIDDEN" NAME="plant" VALUE="">
            <INPUT TYPE="HIDDEN" NAME="type" VALUE="">


         <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;Configured VOBs&nbsp;</b></td>
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
                XmlUtils.transform(vobRoot.getOwnerDocument(), webAppDir+"/xsl/vob_manager_rep.xsl", props, out);
               %>
	       <!-- /REPORT -->
               <BR>
              </TD>
             </TR>

             <TR>
    	      <TD>

          <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="760">
                <tr>
                  <td colspan="1"><img src="img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
	          </table>
              <table width="500">
                <tr>
                     <td align="lefth">
                     <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                      <TR>
                           <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                           <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm('D')">&nbsp;New VOB D Definition</a></TD>
                           <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                     </TR>
                    </TABLE>
                    </td>


                    <td align="lefth">
                     <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                      <TR>
                           <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                           <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm('T')">&nbsp;New VOB T Definition</a></TD>
                           <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                     </TR>
                    </TABLE>
                    </td>

                    <td align="lefth">
                     <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                      <TR>
                           <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                           <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm('R')">&nbsp;New VOB R Definition</a></TD>
                           <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                     </TR>
                    </TABLE>
                    </td>
                </tr>
             </table>

              <!-- /BUTTONS -->
              </TD>
             </TR>

            </FORM>

<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
