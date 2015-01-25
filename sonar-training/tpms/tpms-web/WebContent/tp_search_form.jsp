<%@ page import="tol.slctLst,
                 java.util.Vector,
                 tol.reportSel"%>
<%
    reportSel repsel=(reportSel)session.getAttribute("repsel");
    String userRole =(String)session.getAttribute("role");
    slctLst fromPlant = repsel.get("FROM_PLANT");
    slctLst toPlant = repsel.get("TO_PLANT");

    slctLst divisions = repsel.get("DIVISION");
    fromPlant.fetch();
    toPlant.fetch();

    divisions.fetch();
    Vector fromPlantLst = fromPlant.getVector();
    Vector toPlantLst = toPlant.getVector();
    Vector divisionList = divisions.getVector();
    //ocio alla porcata.....
    boolean statusAlreadyDisplayed = false;
%>

	     <%
 		String parname;
 		String parval;
	     %>
	     <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><%= (news ? "TP SEARCH FORM" : "NEW SEARCH") %></b></td>
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

		<FORM name="tpSearchForm" action="dbQryServlet" method="post">
 		 <INPUT type="HIDDEN" name="qryType" value="<%= qryType %>">
                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>
                     <% if (qryType.equals("db_tp_history"))
                      {%>
                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
			<% parname = "field.JOBNAME"; %>
                        <td class=testo width="40%" valign="top">
                         <b>Job name (PRIS)<%= (qryType.equals("db_tp_history")? "*" : "%") %></b><br>
                         <input class="txt" maxlength="100" size="10" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

			<% parname = "field.JOB_REL"; %>
                        <td class=testo width="40%" valign="top">
                         <b>Job release (PRIS)</b><br>
                         <input class="txt" maxlength="100" size="10" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                        </td>
                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->
                      <%}
                     %>
 <% if (!qryType.equals("db_tp_history"))
		      {%>
                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
            <% parname = "field.JOBNAME"; %>
                        <td class=testo width="40%" valign="top">
                         <b>Job name (PRIS) %</b><br>
                         <input class="txt" maxlength="100" size="10" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                        </td>
            <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->
            <% parname = "field.LINE"; %>
                        <td class=testo width="40%" valign="top">
                         <b>Prod line %</b><br>
                         <input class="txt" maxlength="100" size="10" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                        </td>
                       <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->
                      </tr>
                      <!-- /FIELDS ROW -->
	              <%}
		     %>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                       <% if (userRole.equals("ADMIN") || userRole.equals("QUERY_USER"))
                       {%>
                      <tr>
                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <% parname = "field.FROM_PLANT"; %>
                        <% parval = ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)); %>
                        <td class=testo width="40%">
                          <b>From plant</b><br>
                          <select class="tendina" maxlength="100" size="1"  name="<%= parname %>">
                            <option value="">&nbsp;</option>
                            <%
                              for (int i=0; i<fromPlantLst.size(); i++)
                              {%>
                                <option <%= (parval.equals((String)fromPlantLst.elementAt(i)) ? "selected" : "") %> value="<%= fromPlantLst.elementAt(i) %>"><%= fromPlantLst.elementAt(i) %></option>
                              <%}
                            %>
                          </select>
                                    </td>
                                  <!-- /FIELDS ROW -->

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->
                        <% parname = "field.TO_PLANT"; %>
                        <% parval = ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)); %>
                        <td class=testo width="40%" valign="top">
                         <b>Destination plant</b><br>
                         <select class="tendina" maxlength="100" size="1"  name="<%= parname %>">
                           <option value="">&nbsp;</option>
                           <%
                             for (int i=0; i < toPlantLst.size(); i++)
                             {%>
                               <option <%= (parval.equals((String)toPlantLst.elementAt(i)) ? "selected" : "") %> value="<%= toPlantLst.elementAt(i) %>"><%= toPlantLst.elementAt(i) %></option>
                             <%}
                           %>
                         </select>
                        </td>
                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                                            <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
			<% parname = "field.DIVISION"; %>
                        <% parval = ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)); %>
                        <td class=testo width="40%" valign="top">
                            <b>Division</b><br>
                            <select class="tendina"  name="<%= parname %>">
                                <option value="">&nbsp;</option>
                           <%
                             for (int i = 0; i < divisionList.size(); i++)
                             {%>
                               <option <%= (parval.equals((String)divisionList.elementAt(i)) ? "selected" : "") %> value="<%= divisionList.elementAt(i) %>"><%= divisionList.elementAt(i) %></option>
                             <%}
                           %>
                            </select>
                        </td>
                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->


			<% parname = "field.STATUS"; %>
                        <% parval = ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)); statusAlreadyDisplayed = true;%>
                        <td class=testo width="30%" valign="top">
                          <b>Status</b><br>
                          <select class="tendina" maxlength="100" size="1"  name="<%= parname %>">
                          <%--if (qryType.equals("db_prod_lib_hist")){%>
                            <option value="">&nbsp;</option>
                            <option <%= (parval.equals("In_Production") ? "selected" : "") %> value="In_Production">In_Production</option>
                            <option <%= (parval.equals("Obsolete") ? "selected" : "") %> value="Obsolete">Obsolete</option>
                          <%--} else {--%>
                            <option value="">&nbsp;</option>
                            <option <%= (parval.equals("Distributed") ? "selected" : "") %> value="Distributed">Distributed</option>
                            <option <%= (parval.equals("In_Validation") ? "selected" : "") %> value="In_Validation">In_Validation</option>
                            <option <%= (parval.equals("Ready_to_production") ? "selected" : "") %> value="Ready_to_production">Ready_to_production</option>
                            <option <%= (parval.equals("In_Production") ? "selected" : "") %> value="In_Production">In_Production</option>
                            <option <%= (parval.equals("Obsolete") ? "selected" : "") %> value="Obsolete">Obsolete</option>
                            <option <%= (parval.equals("Ghost") ? "selected" : "") %> value="Ghost">Ghost</option>
                            <option <%= (parval.equals("Rejected") ? "selected" : "") %> value="Rejected">Rejected</option>
                            <option <%= (parval.equals("Sent2TPLib") ? "selected" : "") %> value="Sent2TPLib">Sent2TPLib</option>
                          <%--}--%>
                          </select>
                        </td>
                                                  <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->
                      </tr>

                      <%

                          }%>
			<!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

 			            <% parname = "field.LAST_ACTION_DATE.min"; %>
                        <td class=testo width="40%">
                         <b>Last Action date<br>(from dd/mon/yyyy)</b><br>
                         <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                         <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Last Action date')">
			                <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

 			<% parname = "field.LAST_ACTION_DATE.max"; %>
                        <td class=testo width="40%">
                          <b>Last Action date<br>(to  dd/mon/yyyy)</b><br>
                          <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                          <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Last Action date')">
			  <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>

                      <!-- /FIELDS ROW -->

		     <% if ((qryType.equals("db_tp_search")) || (qryType.equals("db_prod_lib_hist")))
                      {
                        %>

		      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

 			<% parname = "field.DISTRIB_DATE.min"; %>
                        <td class=testo width="40%">
                         <b>Delivery date<br>(from dd/mon/yyyy)</b><br>
                         <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                         <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Creation date')">
			 <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

 			<% parname = "field.DISTRIB_DATE.max"; %>
                        <td class=testo width="30%">
                          <b>Delivery date<br>(to  dd/mon/yyyy)</b><br>
                          <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                          <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Creation date')">
			  <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->
	              <%}
		     %>

             <%-- if ((qryType.equals("db_prod_lib_hist")))
                      {
                         %>
                       <!-- /FIELDS ROW -->

		      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <!-- tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

 			<% parname = "field.PROD_DATE.min"; %>
                        <td class=testo width="40%">
                         <b>Production date<br>(from dd/mon/yyyy)</b><br>
                         <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                         <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Creation date')">
			 <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

 			<% parname = "field.PROD_DATE.max"; %>
                        <td class=testo width="30%">
                          <b>Production date<br>(to  dd/mon/yyyy)</b><br>
                          <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                          <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Creation date')">
			  <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr-->
                      <!-- /FIELDS ROW -->
	              <%}
		     %-->

		     <%-- if (qryType.equals("db_tp_history")) {%>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

 			<% parname = "field.ACTION_DATE.min"; %>
                        <td class=testo width="40%">
                         <b>Action date<br>(from dd/mon/yyyy)</b><br>
                         <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                         <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Action date')">
			 <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

 			<% parname = "field.ACTION_DATE.max"; %>
                        <td class=testo width="30%">
                          <b>Action date<br>(to  dd/mon/yyyy)</b><br>
                          <input class="txtnob" maxlength="100" size="11" name="<%= parname %>" value="<%= ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)) %>">
                          <A href="javascript:openCalendar('<%= parname.substring(0,parname.length()-4) %>','Action date')">
			  <img src="img/pix.gif" width="2" border="0"><IMG src="img/ico_calendar.gif" border="0" alt="Calendar"></A>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->
	              <%}--%>


	             <% if (qryType.equals("db_tp_search") || qryType.equals("db_tp_search_base"))
		      {%>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=4><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=4><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW -->


                      <!-- FIELDS ROW -->



<%if (!statusAlreadyDisplayed) {%>
                     <tr>
                         <td>&nbsp;</td>
                        <% parname = "field.TO_PLANT"; %>
                        <% parval = ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)); %>
                        <td class=testo width="40%" valign="top">
                         <b>Destination plant</b><br>
                         <select class="tendina" maxlength="100" size="1"  name="<%= parname %>">
                           <option value="">&nbsp;</option>
                           <%
                             for (int i=0; i < toPlantLst.size(); i++)
                             {%>
                               <option <%= (parval.equals((String)toPlantLst.elementAt(i)) ? "selected" : "") %> value="<%= toPlantLst.elementAt(i) %>"><%= toPlantLst.elementAt(i) %></option>
                             <%}
                           %>
                         </select>
                        </td>
                         <td>&nbsp;</td><td>&nbsp;</td>
            <% parname = "field.STATUS"; %>
                        <% parval = ((news)||(request.getParameter(parname)==null) ? "" : request.getParameter(parname)); %>
                        <td class=testo width="30%" valign="top">
                          <b>Status</b><br>
                          <select class="tendina" maxlength="100" size="1"  name="<%= parname %>">
                          <%--if (qryType.equals("db_prod_lib_hist")){%>
                            <option value="">&nbsp;</option>
                            <option <%= (parval.equals("In_Production") ? "selected" : "") %> value="In_Production">In_Production</option>
                            <option <%= (parval.equals("Obsolete") ? "selected" : "") %> value="Obsolete">Obsolete</option>
                          <%--} else {--%>
                            <option value="">&nbsp;</option>
                            <option <%= (parval.equals("Distributed") ? "selected" : "") %> value="Distributed">Distributed</option>
                            <option <%= (parval.equals("In_Validation") ? "selected" : "") %> value="In_Validation">In_Validation</option>
                            <option <%= (parval.equals("Ready_to_production") ? "selected" : "") %> value="Ready_to_production">Ready_to_production</option>
                            <option <%= (parval.equals("In_Production") ? "selected" : "") %> value="In_Production">In_Production</option>
                            <option <%= (parval.equals("Obsolete") ? "selected" : "") %> value="Obsolete">Obsolete</option>
                            <option <%= (parval.equals("Ghost") ? "selected" : "") %> value="Ghost">Ghost</option>
                            <option <%= (parval.equals("Rejected") ? "selected" : "") %> value="Rejected">Rejected</option>
                            <option <%= (parval.equals("Sent2TPLib") ? "selected" : "") %> value="Sent2TPLib">Sent2TPLib</option>
                          <%--}--%>
                          </select>
                        </td>
             </tr>
		      <%}
              }%>
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
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitSearch()">&nbsp;SUBMIT</a></TD>
                    <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>
                <tr>
                  <td class="txtnob">
                    <br>
                    <i>
                     <%
                      if (qryType.equals("db_tp_history"))
                      {%> * = Mandatory field <br> <%}
                      else
                      {%> % = Wild character<br> <%}
                     %>
                    </i>
		  </td>
                </tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>