<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.CtrlServlet,
                 tpms.TesterInfoMgr"

%>
<%  
  String time = Long.toString(System.currentTimeMillis());
  String webAppDir=config.getServletContext().getInitParameter("webAppDir");
  String plant=((String)request.getAttribute("plant")!= null ? (String)request.getAttribute("plant") : config.getServletContext().getInitParameter("localPlant"));
  String localPlant=config.getServletContext().getInitParameter("localPlant");
  Element testerRoot= TesterInfoMgr.getTesterInfosRoot(plant);
  Properties props=new Properties();
  props.setProperty("plant",plant);
  props.setProperty("localPlant",localPlant);
  Vector plantList = TesterInfoMgr.getPlant(plant);
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>

 <SCRIPT language=JavaScript>
  function submitForm()
  {
    document.testerInfoForm.action.value='new';
    document.testerInfoForm.submit();
  }

  function changePlant()
   {
     var ind = document.testerInfoForm.elements['PLANT'].selectedIndex;
     var plant = document.testerInfoForm.elements['PLANT'].options[ind].text;
     document.testerInfoForm.action.value='view';
     document.testerInfoForm.plant.value=plant;
     document.testerInfoForm.submit();
   }

  function go_to(tester, action)
  {
    if (action=='delete')
    {
      ret=top.window.confirm("DO YUO ACTUALLY WANT TO DELETE TESTER "+tester+" ?");
      if (!ret) return;
    }
    document.testerInfoForm.testerName.value=tester;
    document.testerInfoForm.action.value=action;
    document.testerInfoForm.submit();
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TESTER INFO REPORT"; %>
<%@ include file="top.jsp" %>

           <FORM name="testerInfoForm" action="testerInfoServlet" method="post">
            <INPUT TYPE="HIDDEN" NAME="action" VALUE="">
 	        <INPUT TYPE="HIDDEN" NAME="testerName" VALUE="">
            <INPUT TYPE="HIDDEN" NAME="plant" VALUE="">
         <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
              <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="left" class="titverdi" nowrap><b><!-- FORM TITLE-->PLANT</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="603" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix.gif" width="760" height="25" alt="" border="0"></td>
                </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td class=testo width="30%">
                          <select class="tendina" maxlength="100" size="1"  name="PLANT" onChange="changePlant()">
                          &nbsp;
                           <%
                               for (int i=0; i<plantList.size(); i++)
                               {%>
                               <option <%= (((String)plantList.elementAt(i)).equals(plant) ? "selected" : "") %> ><%= plantList.elementAt(i) %>
                             <%}
                           %>
                          </select>
                    </td>
                      <!-- LEFT COLUMN SEPARATOR -->
                    <td colspan="2">&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
                </tr>
                <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
              </table>
	      </TD>
	     </TR>

         <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;<%=plant%>&nbsp; TESTER INFO&nbsp;</b></td>
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
                xmlRdr.transform(testerRoot.getOwnerDocument(), webAppDir+"/xsl/tester_rep.xsl", props, out);
               %>
	       <!-- /REPORT -->
               <BR>
              </TD>
             </TR>

             <TR>
    	      <TD>
          <% if (plant.equals(localPlant)){%>
          <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="760">
                <tr>
                  <td colspan="1"><img src="img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
	          </table>
              <table width="760">
                <tr>
                     <td align="right">
                     <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                      <TR>
                           <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                           <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;NEW TESTER INFO</a></TD>
                           <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                     </TR>
                    </TABLE>
                    </td>
                </tr>
             </table>
          <%}%>
              <!-- /BUTTONS -->
              </TD>
             </TR>

            </FORM>

<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
