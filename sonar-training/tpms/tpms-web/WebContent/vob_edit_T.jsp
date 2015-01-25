<%@ page import="org.w3c.dom.Element"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="tpms.utils.VobConfiguration"%>
<%@ page import="it.txt.general.utils.GeneralStringUtils"%>
<%
    boolean isNewVob = (request.getAttribute("isNewVob")).equals("Y");
    String mandatoryFieldsMark = "";
    String errorMessages = "";
    String type = (String)request.getAttribute("type");
    Element vobRec=(Element)request.getAttribute("vobRec");
    Vector IDList = (Vector)request.getAttribute("IDList");
    NodeList vobNodes = (NodeList)request.getAttribute("vobNodes");
    Vector vobNameList = VobConfiguration.getVobNameList(vobNodes, type);
    if (isNewVob) {

        mandatoryFieldsMark = "*";
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
    }


    String param;
    String value;


%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>

   var unixGroupArray = new Array();

   function getUnixGroup() {
        <%
           for (int i=0; i<vobNodes.getLength(); i++)
           {
              Element vob=(Element)vobNodes.item(i);

              String vobName=XmlUtils.getVal(vob,"VOB_NAME");
              NodeList divis=vob.getElementsByTagName("UNIX_GROUP");
              %>
                unixGroupArray[<%=i%>] = new Array();
                unixGroupArray[<%=i%>][0] = '<%= vobName %>';

              <%
              for (int j=0; j<divis.getLength(); j++)
              {
                 Element div=(Element)divis.item(j);
              %>
                     unixGroupArray[<%=i%>][<%=j+1%>] = '<%=XmlUtils.getVal(div)%>';
              <%}
           }
        %>
   }

   getUnixGroup();
   function setGroupForVob(vobName)
   {
     for (i=0; i < unixGroupArray.length; i++)
     {
       if (unixGroupArray[i][0]==vobName) break;
     }

     var box = this.document.vobForm.elements['FIELD.DIVISION'];
     while (box.options.length > 0) box.options[0] = null;
     if (unixGroupArray[i] != null){
         for (j=1; j < unixGroupArray[i].length; j++)
         {
           box.options[j-1] = new Option(unixGroupArray[i][j],unixGroupArray[i][j]);
         }
     }
   }


   function changeVobName()
   {
     var ind = document.vobForm.elements['FIELD.NAME'].selectedIndex;
     var vobName = document.vobForm.elements['FIELD.NAME'].options[ind].text;
     setGroupForVob(vobName);
   }

    function openUnixGroups()
    {
       var url="vob_sel_unix_group.jsp";

       var popup=window.open(url);

       if (popup != null)
       {
        if (popup.opener == null) popup.opener = self
       }
    }

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
<FORM name="vobForm" action="<%=contextPath%>/vobManagerServlet" method="post">
		            <INPUT TYPE="HIDDEN" NAME="vobName" VALUE="<%= (isNewVob ? "" : XmlUtils.getVal(vobRec,"NAME")) %>">
                    <INPUT TYPE="HIDDEN" NAME="action" VALUE="save">
                    <INPUT TYPE="HIDDEN" NAME="isNewVob" VALUE="<%= (isNewVob ? "Y" : "N") %>">
                    <INPUT TYPE="HIDDEN" NAME="type" VALUE="<%=type%>">
             <TR>
              <TD ALIGN="LEFT">

                   
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><%= (isNewVob ? "&nbsp;NEW&nbsp; VOB&nbsp;" : "VOB DATA") %></b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
              </table>
              </td>
              </tr>

                    <TR>
               <!-- FORM -->
                      <!-- FIELDS ROW: -->
                      <% param="NAME"; %>
                      <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                      <td class=testo width="30%">
                          <b>VOB NAME <%=mandatoryFieldsMark%></b><br>
                         <%if(isNewVob)
                          {%>
                              <select class="tendina" size="1"  name="FIELD.<%= param %>" onChange="changeVobName()">
                               &nbsp;
                               <option></option>
                               <%
                                   for (int i=0; i<vobNameList.size(); i++)
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
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
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
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->


             <!-- FIELDS ROW: -->
                      <tr>

			<% param="DIVISION"; %>
                        <td class=testo colspan="5">
                         <b>Unix Group</b><br>
                         Those are all the groups associated with this vob (read-only)<br>
                         <%
                         Vector unixGroups;

                         if(isNewVob)
                         {%>
                              <select class="tendina" size="3" name="FIELD.<%= param %>">
                              </select>
                        <%}else{
                            unixGroups=VobConfiguration.getUnixGroupOfVob(vobNodes, XmlUtils.getVal(vobRec,"NAME"));%>

                           <%for (int i=0; i<unixGroups.size(); i++){%>
                                   <li>&nbsp;<%=unixGroups.get(i)%>
                           <%}%>
                        <%}%>

                          </td>
		                </tr>

                    <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

			<% param="PLANT"; %>
                        <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Destination Plant ID <%=mandatoryFieldsMark%></b><br>
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
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                    <!-- FIELDS ROW: -->
                      <tr>
             <% param="TYPE"; %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Vob Type </b><br>
                               <%= type %>
                               <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=type%>">
                        </td>
              </tr>


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->


                    </table>
                  </form>
               <!--/FORM -->


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
                    <TD valign="center" align="right"><IMG alt="" SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:history.back()">&nbsp;BACK</a></TD>
                    <TD valign="center" align="right"><IMG alt="" SRC="img/btn_right.gif"></TD>
		            <TD><img alt="" src="img/pix.gif" width="5" border="0"></TD>
                    <TD valign="center"><IMG alt="" SRC="img/btn_left.gif"></TD>
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;SUBMIT</a></TD>
                    <TD valign="center" ><IMG alt="" SRC="img/btn_right.gif"></TD>
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
