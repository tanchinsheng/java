<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="tpms.utils.VobConfiguration"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="it.txt.general.utils.GeneralStringUtils"%>
<%
    Element vobRec=(Element)request.getAttribute("vobRec");
    boolean isNewVob = (request.getAttribute("isNewVob")).equals("Y");
    String type = (String)request.getAttribute("type");
    String mandatoryFieldsMark = "";
    Vector IDList = (Vector) request.getAttribute("IDList");
    NodeList vobNodes = (NodeList)request.getAttribute("vobNodes");
    Vector vobNameList;
    String errorMessages = "";
    if (isNewVob) {
        mandatoryFieldsMark = "*";
        vobNameList = VobConfiguration.getVobNameList(vobNodes, type);
        int vobsNumber = vobNodes.getLength();
        Element vob;
        String tmpType;
        for ( int i = 0; i < vobsNumber; i++ ) {
            vob = (Element)vobNodes.item(i);
            tmpType = XmlUtils.getVal(vob, "TYPE");
            if (!GeneralStringUtils.isEmptyString(tmpType) && !tmpType.equals(type)) {
                if (GeneralStringUtils.isEmptyString(errorMessages)){
                    errorMessages = "There is at least one vob that got errors, or is missconfigured:<br>";
                }
                errorMessages = errorMessages + tmpType + "<br>";
            }
        }
    } else {
        vobNameList = new Vector();
    }

    String param;
    String value;


%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>

    function controlData(){

     var vobName;
     if (document.vobForm.elements['FIELD.NAME'].selectedIndex != null)
     {
         var ind1=document.vobForm.elements['FIELD.NAME'].selectedIndex;
         if (ind1>=0)
         {
           vobName =document.vobForm.elements['FIELD.NAME'].options[ind1].text;
         }
         else
         {
           vobName='';
         }
     }
     var vobDesc = document.vobForm.elements['FIELD.DESC'].value;

     var vobPlantID;
     if (document.vobForm.elements['FIELD.PLANT'].selectedIndex != null)
     {
         var ind2=document.vobForm.elements['FIELD.PLANT'].selectedIndex;
         if (ind2>=0)
         {
           vobPlantID =document.vobForm.elements['FIELD.PLANT'].options[ind2].text;
         }
         else
         {
           vobPlantID='';
         }
     }

     if(vobName == '')
     {
        alert('VOBNAME IS A MANDATORY FIELD! ');
        return false;
     }

     if(vobDesc == '')
     {
        alert('VOB DESCRIPTION IS A MANDATORY FIELD! ');
        return false;
     }

     if(vobPlantID == '')
     {
        alert('PLANT ID IS A MANDATORY FIELD! ');
        return false;
     }

     return true;
   }

   function submitForm()
   {
     if (!controlData()) return;

     document.vobForm.submit();
   }

 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle=(isNewVob ? "VOB INSERTION PAGE" : "VOB DATA MODIFICATION PAGE"); %>
<%String contextPath = request.getContextPath();%>
<%@ include file="top.jsp" %>
<%
if (!GeneralStringUtils.isEmptyString(errorMessages)) {
    %>
    <tr><td class="titred" align="center"><%=errorMessages%></td></tr>
<%
}
%>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img SRC="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><%= (isNewVob ? "&nbsp;NEW&nbsp;VOB&nbsp;" : "VOB DATA") %></b></td>
                  <td width="4"><img SRC="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img SRC="<%=contextPath%>/img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img SRC="<%=contextPath%>/img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
	            </TD>
	            </TR>

             <TR>
              <TD ALIGN="LEFT">

               <!-- FORM -->

	           <FORM name="vobForm" action="<%=contextPath%>/vobManagerServlet" method="post">
		            <INPUT TYPE="HIDDEN" NAME="vobName" VALUE="<%= (isNewVob ? "" : XmlUtils.getVal(vobRec,"NAME")) %>">
                    <INPUT TYPE="HIDDEN" NAME="action" VALUE="save">
                    <INPUT TYPE="HIDDEN" NAME="isNewVob" VALUE="<%= (isNewVob ? "Y" : "N") %>">
                    <INPUT TYPE="HIDDEN" NAME="type" VALUE="<%=type%>">
                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >



                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->
                      <tr>
                      <!-- FIELDS ROW: -->
                      <% param="NAME"; %>
                      <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                      <td class=testo width="30%">
                          <b>VOB NAME <%=mandatoryFieldsMark%></b><br>
                         <%if(isNewVob)
                          {%>
                              <select class="tendina" size="1"  name="FIELD.<%= param %>" >
                               &nbsp;
                               <option></option>
                               <%
                                   for (int i=0; ( i < vobNameList.size()); i++)
                                   {%>
                                   <option <%= (((String)vobNameList.elementAt(i)).equals(value) ? "selected" : "") %> ><%= vobNameList.elementAt(i) %></option>
                                 <%}
                               %>
                              </select>
                           <%}else{%>
                                    <%= value %>
                                    <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=value%>">
                           <%}%>
                         </td>

                    <!-- FIELDS ROW: -->
                      </tr>

                   <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>
			            <% param="DESC"; %>
                        <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                        <td class=testo width="40%">
                         <b>VOB Description *</b><br>

                            <input class="txt" maxlength="15" size="15" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">

                        </td>

                <!-- FIELDS ROW: -->
                    </tr>

                     <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->


                      <!-- FIELDS ROW: -->
                      <tr>

			<% param="PLANT"; %>
                        <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Master Plant ID <%=mandatoryFieldsMark%></b><br>
                            <%
                                if(isNewVob)
                                {%>
                                <%if (IDList.size() > 0) {%>
                                    <select class="tendina" size="1" name="FIELD.<%= param %>">

                                    <%for (int i=0; i<IDList.size(); i++)
                                    {%>
                                        <option <%= (((String)IDList.elementAt(i)).equals(value) ? "selected" : "") %> ><%= IDList.elementAt(i) %>
                                    <%}%>
                                    </select>
                                    <%} else {%>
                                        Please verifiy the Tester Info configuration.
                                    <%}%>
                                <%}else{%>
                                         <%= value %>
                                        <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=value%>">
                                <%}%>


                        </td>

		      </tr>

                    <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                    <!-- FIELDS ROW: -->
                      <tr>
             <% param="TYPE"; %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Vob Type</b><br>
                               <%= type %>
                               <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=type%>">
                        </td>
              </tr>


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" SRC="<%=contextPath%>/img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->


     <!--  quiqui             </table>
                  </form>


              </TD>
             </TR>-->
             <TR>
    	      <td>
	      <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><img SRC="<%=contextPath%>/img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><img SRC="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		<tr>
                 <td align="right">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                    <TD valign="center" align="right"><IMG alt="" SRC="<%=contextPath%>/img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:history.back()">&nbsp;BACK</a></TD>
                    <TD valign="center" align="right"><IMG alt="" SRC="<%=contextPath%>/img/btn_right.gif"></TD>
		       <TD><img alt="" SRC="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                    <TD valign="center"><IMG alt="" SRC="<%=contextPath%>/img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;SUBMIT</a></TD>
                    <TD valign="center" ><IMG alt="" SRC="<%=contextPath%>/img/btn_right.gif"></TD>
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
