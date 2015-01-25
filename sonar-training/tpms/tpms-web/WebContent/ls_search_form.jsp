<%@ page import="tol.slctLst,
                 java.util.Vector,
                 tol.reportSel"%>
<%

 //Vector fromPlantLst=(Vector)session.getAttribute("plantLst");
 //Vector toPlantLst=(Vector)session.getAttribute("plantLst");
   reportSel repsel=(reportSel)session.getAttribute("repsel");
   String userRole =(String)session.getAttribute("role");
   slctLst plant=repsel.get("LS_PLANT");

   plant.fetch();

   Vector plantLst=plant.getVector();
%>

	     <%
 		String parname="";
 		String parval="";
	     %>
	     <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><%= (news ? "LS SEARCH FORM" : "NEW SEARCH") %></b></td>
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

		<FORM name="lsSearchForm" action="dbQryServlet" method="post">
 		 <INPUT type="HIDDEN" name="qryType" value="<%= qryType %>">
                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>
         <% if (userRole.equals("QUERY_USER"))
		   {%>
                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
			<% parname = "field.LS_PLANT"; %>
                        <% parval = ((news)||(request.getParameterValues(parname)==null) ? "" : request.getParameterValues(parname)[0]); %>
                        <td class=testo width="40%" valign="top">
                         <b>Plant</b><br>
                         <select class="tendina" maxlength="100" size="1"  name="<%= parname %>">
			   <option value="">&nbsp;
			   <%
 			     for (int i=0; i<plantLst.size(); i++)
 			     {%>
   			       <option <%= (parval.equals((String)plantLst.elementAt(i)) ? "selected" : "") %> ><%=plantLst.elementAt(i) %>
 			     <%}
			   %>
			 </select>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="40%" valign="top">
			  &nbsp;
                        </td>
                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->
            <%
            }%>
                     <% if (qryType.equals("db_ls_search"))
                      {%>
                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
			<% parname = "field.LS_NAME"; %>
                        <td class=testo width="40%" valign="top">
                         <b>Line Set name <%= (qryType.equals("db_tp_history") ? "*" : "%") %></b><br>
                         <input class="txt" maxlength="100" size="10" name="<%= parname %>" value="<%= ((request.getParameterValues(parname)==null) ? "" : request.getParameterValues(parname)[0]) %>">
                        </td>

                        <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

 			<% parname = "field.LS_ACTION_DATE.min"; %>
                        <td class=testo width="40%">
                         <b>Action date<br>(from dd/mon/yyyy)</b><br>
                         <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameterValues(parname)==null) ? "" : request.getParameterValues(parname)[0]) %>">
                         <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Action date')">
			 <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

 			<% parname = "field.LS_ACTION_DATE.max"; %>
                        <td class=testo width="30%">
                          <b>Action date<br>(to  dd/mon/yyyy)</b><br>
                          <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameterValues(parname)==null) ? "" : request.getParameterValues(parname)[0]) %>">
                          <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Action date')">
			  <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->
                      <%}
                     %>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      </tbody>
                    </table>
                  </FORM>
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
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitSearch()">&nbsp;SUBMIT</TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>
                <tr>
                  <td class="txtnob">
                    <br>
                    <i>
                      % = Wild character (admitted as terminal character only)<br>
                    </i>
		  </td>
                </tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>
