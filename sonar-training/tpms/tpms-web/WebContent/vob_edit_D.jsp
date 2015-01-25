<%@ page import="tpms.utils.VobConfiguration"%>
<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="java.util.Vector"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="it.txt.general.utils.GeneralStringUtils"%>
<%
    Element vobRec = (Element)request.getAttribute("vobRec");
    NodeList vobNodes = (NodeList)request.getAttribute("vobNodes");
    String type = (String) request.getAttribute("type");
    boolean isNewVob = (request.getAttribute("isNewVob")).equals("Y");

    String errorMessages = "";
    String mandatoryFieldsMark = "";
    Vector vobNameList;

    if ( isNewVob ) {
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

    String localPlant = (String) request.getAttribute("localPlant");
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language="JavaScript">
   var unixGroupArray = new Array();




   function getUnixGroup()
   {
        <%
           Element vob;
           String tmpType;
           String vobName;
           NodeList divis;
           for (int i = 0; (isNewVob &&i < vobNodes.getLength() ); i++)
           {
              vob = (Element)vobNodes.item(i);
              tmpType = XmlUtils.getVal(vob, "TYPE");
              if (!GeneralStringUtils.isEmptyString(tmpType) && tmpType.equals(type)) {
                  vobName = XmlUtils.getVal(vob,"VOB_NAME");
                  divis = vob.getElementsByTagName("UNIX_GROUP");
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
           }
        %>
   }


   function setGroupForVob(vobName)
   {
     var pos = -1;

     for (i=0; i < unixGroupArray.length; i++)
     {
       if (unixGroupArray[i] != null && unixGroupArray[i][0] == vobName) {
           pos = i;
           break;
       }
     }

     var box = this.document.vobForm.elements['FIELD.DIVISION'];
     while (box.options.length > 0) box.options[0] = null;

     for (j=1; (pos >= 0 && j < unixGroupArray[pos].length); j++)
     {
       box.options[j-1] = new Option(unixGroupArray[pos][j],unixGroupArray[pos][j]);
     }
   }


   function changeVobName()
   {
     var ind = document.vobForm.elements['FIELD.NAME'].selectedIndex;
     var vobName = document.vobForm.elements['FIELD.NAME'].options[ind].text;
     setGroupForVob(vobName);
   }


   function showTesterInfos()
   {
    for (i=0; i < unixGroupArray.length; i++)
    {
      for (j=0; j < unixGroupArray[i].length; j++)
      {
         alert(unixGroupArray[i][j]);
      }
    }
   }

    function setTvob()
    {
       var url="vob_sel_Tvob.jsp";

       var popup=window.open(url,"Selection","width=680,height=420,resizable=yes,scrollbars=no,status=0,toolbar=no,location=no,menubar=no");

       if (popup != null)
       {
        if (popup.opener == null) popup.opener = self
       }
    }

    function controlData()
    {
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

         var vobDivision;
         if (document.vobForm.elements['FIELD.DIVISION'].selectedIndex != null)
         {
             var ind1=document.vobForm.elements['FIELD.DIVISION'].selectedIndex;
             if (ind1>=0)
             {
               vobDivision =document.vobForm.elements['FIELD.DIVISION'].options[ind1].text;
             }
             else
             {
               vobDivision='';
             }
         }
         var vobPlant = document.vobForm.elements['FIELD.PLANT'].value;


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

         if(vobDivision == '')
         {
            alert('UNIX GROUP IS A MANDATORY FIELD! ');
            return false;
         }

         if(vobPlant == '')
         {
            alert('PLANT IS A MANDATORY FIELD! ');
            return false;
         }

         return true;
   }

   function submitForm()
   {
     if (!controlData()) return;

     document.vobForm.submit();
   }

   getUnixGroup();


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
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><%= (isNewVob ? "&nbsp;NEW&nbsp; VOB&nbsp;" : "VOB DATA") %></b></td>
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

	           <FORM name="vobForm" action="<%=contextPath%>/vobManagerServlet" method="post">
		            <INPUT TYPE="HIDDEN" NAME="vobName" VALUE="<%= (isNewVob ? "" : XmlUtils.getVal(vobRec,"NAME")) %>">
                    <INPUT TYPE="HIDDEN" NAME="action" VALUE="save">
                    <INPUT TYPE="HIDDEN" NAME="isNewVob" VALUE="<%= (isNewVob ? "Y" : "N") %>">
                    <INPUT TYPE="HIDDEN" NAME="type" VALUE="<%= type%>">
                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

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
                                   for (int i=0; i < vobNameList.size(); i++)
                                   {%>
                                   <option <%= (((String)vobNameList.elementAt(i)).equals(value) ? "selected" : "") %> ><%= vobNameList.elementAt(i) %>
                                 <%}
                               %>
                              </select>
                           <%} else {%>
                                    <%= value %>
                                    <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=value%>">
                           <%}%>
                        </td>

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
                        <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                        <td class=testo width="40%">
                         <b>Unix Group <%=mandatoryFieldsMark%></b><br>
                         <%if(isNewVob)
                         {%>
                              <select class="tendina" size="1" name="FIELD.<%= param %>">
                              <%
                                Vector unixGroups;
                                if (false)
                                {
                                    unixGroups = VobConfiguration.getUnixGroupOfVob(vobNodes, XmlUtils.getVal(vobRec,"NAME"));

                                    for (int i=0; i<unixGroups.size(); i++)
                                    {
                                      boolean selBool=(!value.equals("") ? value.equals( unixGroups.elementAt(i)) : (i==0));
                                      %>
                                        <option <%= (selBool ? "selected" : "") %>><%=((String)unixGroups.elementAt(i))%>
                                    <%}
                                }
                              %>
                              </select>
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

			<% param="PLANT"; %>
                        <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Vob Master Plant</b><br>
                         <%
                               if ((isNewVob))
                               {%>
                               <%= localPlant %>
                               <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=localPlant%>">
                          <%}
                               else {%>
                                        <%= value %>
                                        <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=value%>">
                        <%}
			            %>
                        </td>

		      </tr>

                    <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                    <!-- FIELDS ROW: -->
                      <tr>
                        <% param="TYPE"; %>
                        <% value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Vob Type</b><br>
                         <%
                               if ((isNewVob))
                               {%>
                               <%= type %>
                               <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=type%>">
                          <%}
                               else {%>
                                        <%= value %>
                                        <INPUT TYPE="HIDDEN" NAME="FIELD.<%= param %>" VALUE="<%=value%>">
                        <%}
			            %>
                        </td>
                       </tr>


              <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                    <!-- FIELDS ROW: -->
                      <tr>
              <% param="TVOB"; %>
                        <% //value=(isNewVob ? "" : XmlUtils.getVal(vobRec,param)); %>
                        <% //Vector values =(isNewVob ? new Vector() : XmlUtils.getVectFromNodeList(vobRec.getElementsByTagName("TVOB"))); %>
                        <%
                        Vector values = new Vector();
                        if (!isNewVob){
                            NodeList tvobs=vobRec.getElementsByTagName("TVOB");
                            for (int i=0; i<tvobs.getLength(); i++)
                            {
                                  vob = (Element)tvobs.item(i);
                                  values.addElement(XmlUtils.getVal(vob));
                            }
                        }


                        %>
                        </tr>
                        <tr>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Transational Vob </b><br>
                        </td>
		                </tr>
                        <tr>
                          <td class=testo width="40%" valign="top" colspan="4">
                            <textarea name="FIELD.<%= param %>" rows="4" cols="50" readonly="readonly"><%for (int i=0; i<values.size(); i++){%>
<%=values.get(i)%><%}%></textarea>
                          </td>
		                </tr>

                      <tr>
                      <td align="right">
     		          <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		            <TR>
                        <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                        <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:setTvob()">&nbsp;SET T VOB</a></TD>
                        <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                      </TR>
                      </TABLE>
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
