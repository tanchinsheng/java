<%@ page import="java.util.*, org.w3c.dom.*, tol.*, tpms.*"
%>
<%
  Element testerRec=(Element)request.getAttribute("testerRec");
  String param=""; String value="";
  boolean isNewTester=(request.getAttribute("isNewTester")).equals("Y");
  String localPlant = (String)request.getAttribute("localPlant");

%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
    function controlData(){
     var testerFam;
     //FP 15-10-04 release 4.4
     <%
     if (isNewTester)
     {%>
        testerFam = document.testerInfoForm.elements['FIELD.TESTER_FAMILY'].value;
        if(testerFam == '')
        {
            alert('TESTER INFO IS A MANDATORY FIELD! ');
            return false;
        }
     <%
     }else{
     %>
         var ind = document.testerInfoForm.elements['FIELD.TESTER_FAMILY'].selectedIndex;
         if (ind>=0)
         {
           testerFam =document.testerInfoForm.elements['FIELD.TESTER_FAMILY'].options[ind].text;
           if(testerFam == '')
           {
                alert('TESTER FAMILY IS A MANDATORY FIELD! ');
                return false;
           }
         }
         else
         {
           alert('TESTER FAMILY IS A MANDATORY FIELD! ');
           return false;
         }
     <%
     }
     %>
     var testerInfoShow = document.testerInfoForm.elements['FIELD.TESTER_INFO_SHOW'].value;
     var tstModel = document.testerInfoForm.elements['FIELD.TESTER_MODEL'].value;
     var tstSw = document.testerInfoForm.elements['FIELD.SW_NAME'].value;
     var tstSwVer = document.testerInfoForm.elements['FIELD.SW_VERSION'].value;
     var tstUnixOs = document.testerInfoForm.elements['FIELD.UNIX_OS'].value;

     if(testerInfoShow == '')
     {
        alert('TESTER INFO SHOW IS A MANDATORY FIELD! ');
        return false;
     }

     if(tstModel == '')
     {
        alert('TESTER MODEL IS A MANDATORY FIELD! ');
        return false;
     }

     if(tstSw == '')
     {
        alert('SW NAME IS A MANDATORY FIELD! ');
        return false;
     }

     if(tstSwVer == '')
     {
        alert('SW VERSION IS A MANDATORY FIELD! ');
        return false;
     }

     if(tstUnixOs == '')
     {
        alert('UNIX OS IS A MANDATORY FIELD! ');
        return false;
     }

     return true;
   }

   function submitForm()
   {
     if (!controlData()) return;
     //FP 15-10-04 release 4.4
     var testerFam;
     <%
     if (isNewTester)
     {%>
        testerFam = document.testerInfoForm.elements['FIELD.TESTER_FAMILY'].value;
     <%
     }else{
     %>
        var ind = document.testerInfoForm.elements['FIELD.TESTER_FAMILY'].selectedIndex;
        testerFam =document.testerInfoForm.elements['FIELD.TESTER_FAMILY'].options[ind].text;
     <%
     }
     %>
     var tstModel = document.testerInfoForm.elements['FIELD.TESTER_MODEL'].value;
     var tstSw = document.testerInfoForm.elements['FIELD.SW_NAME'].value;
     var tstSwVer = document.testerInfoForm.elements['FIELD.SW_VERSION'].value;
     var tstUnixOs = document.testerInfoForm.elements['FIELD.UNIX_OS'].value;
     document.testerInfoForm.elements['FIELD.TST_INFO'].value = testerFam + " " + tstModel + " " + tstSw + " " + tstSwVer + " " + tstUnixOs;
     document.testerInfoForm.submit();
   }

 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle=(isNewTester ? "TESTER INFO INSERTION PAGE" : "TESTER INFO DATA MODIFICATION PAGE"); %>
<%String contextPath = request.getContextPath();%>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><%= (isNewTester ? "&nbsp;NEW&nbsp; TESTER INFO&nbsp;" : "TESTER INFO DATA") %></b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
	            </TD>
	            </TR>

             <TR>
              <TD ALIGN="LEFT">

               <!-- FORM -->

	           <FORM name="testerInfoForm" action="testerInfoServlet" method="post">
		            <INPUT TYPE="HIDDEN" NAME="testerName" VALUE="<%= (isNewTester ? "" : xmlRdr.getVal(testerRec,"TESTER_INFO_SHOW")) %>">
                    <INPUT TYPE="HIDDEN" NAME="action" VALUE="save">
                    <INPUT TYPE="HIDDEN" NAME="isNewTester" VALUE="<%= (isNewTester ? "Y" : "N") %>">
                    <INPUT TYPE="HIDDEN" NAME="FIELD.TST_INFO" VALUE="">
                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>
			            <% param="TESTER_FAMILY"; %>
                        <% value=(isNewTester ? "" : xmlRdr.getVal(testerRec,param)); %>
                        <td class=testo width="40%">
                         <b>Tester Family *</b><br>
                        <%//todo -start FP 15-10-04 release 4.4
                        if(isNewTester)
                        {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                        <%}else{%>
                            <select class="tendina" maxlength="100" size="1"  name="FIELD.<%= param %>">
                                <option>
                                   <%
                                   Vector testfamList=TesterInfoMgr.getTesterFamilyList(localPlant);
                                   for (int i=0; i<testfamList.size(); i++)
                                   {%>
                                   <option <%=(((String)xmlRdr.getVal((Element)testfamList.elementAt(i))).equals(value) ? "selected" : "") %> ><%=xmlRdr.getVal((Element)testfamList.elementAt(i))%>
                                 <%}
                               %>
                             </select>
                        <%}%>
                         </td>

                    <!-- FIELDS ROW: -->
                      </tr>

                   <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>
			            <% param="TESTER_INFO_SHOW"; %>
                        <% value=(isNewTester ? "" : xmlRdr.getVal(testerRec,param)); %>
                        <td class=testo width="40%">
                         <b>Tester Info Show *</b><br>
                         <%
                          if ((isNewTester)||(xmlRdr.getChild(testerRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			              else {%><%= value %><%}
			              %>
                        </td>

                <!-- FIELDS ROW: -->
                    </tr>

                     <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->


             <!-- FIELDS ROW: -->
                      <tr>

			<% param="TESTER_MODEL"; %>
                        <% value=(isNewTester ? "" : xmlRdr.getVal(testerRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Tester Model *</b><br>
                         <%
                          if ((isNewTester)||(xmlRdr.getChild(testerRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			  else {%><%= value %><%}
			 %>
                        </td>

		      </tr>

                    <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

			<% param="SW_NAME"; %>
                        <% value=(isNewTester ? "" : xmlRdr.getVal(testerRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>SW Name *</b><br>
                         <%
                             if ((isNewTester)||(xmlRdr.getChild(testerRec,param).getAttribute("edit").equals("Y")))
                             {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
                 else {%><%= value %><%}
			 %>
                        </td>

		      </tr>

                    <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                    <!-- FIELDS ROW: -->
                      <tr>

			<% param="SW_VERSION"; %>
                        <% value=(isNewTester ? "" : xmlRdr.getVal(testerRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>SW Version *</b><br>
                         <%
                             if ((isNewTester)||(xmlRdr.getChild(testerRec,param).getAttribute("edit").equals("Y")))
                             {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
                 else {%><%= value %><%}
			 %>
                        </td>

		      </tr>

<!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->


             <!-- FIELDS ROW: -->
                      <tr>

			<% param="UNIX_OS"; %>
                        <% value=(isNewTester ? "" : xmlRdr.getVal(testerRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Unix OS *</b><br>
                         <%
                             if ((isNewTester)||(xmlRdr.getChild(testerRec,param).getAttribute("edit").equals("Y")))
                             {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
                 else {%><%= value %><%}
			 %>
                        </td>

		      </tr>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      </tbody>
                    </table>
                  </form>
               <!--/FORM -->

              </TD>
             </TR>
             <TR>
    	      <td>
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
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;SUBMIT</TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>
                <tr>
                  <td class="txtnob">
                    <br>
                    <i>
                     *  = Mandatory field<br>
                    </i>
		  </td>
                </tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>


<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
