<%
  String workMode="NONE";
  String t = new Long(System.currentTimeMillis()).toString();
  boolean isSendWorkModeEnabled=false;
  boolean isRecWorkModeEnabled=false;
  boolean isLocRepModeEnabled=false;
  boolean isGlobRepModeEnabled=false;
  boolean isWorkDirBrowseVisible=false;
  boolean isVobAccessVisible=false;
  if (session.getAttribute("user")!=null)
  {
    isSendWorkModeEnabled=((Boolean)session.getAttribute("isSendWorkModeEnabled")).booleanValue();
    isRecWorkModeEnabled=((Boolean)session.getAttribute("isRecWorkModeEnabled")).booleanValue();
    isLocRepModeEnabled=((Boolean)session.getAttribute("isLocRepModeEnabled")).booleanValue();
    isGlobRepModeEnabled=((Boolean)session.getAttribute("isGlobRepModeEnabled")).booleanValue();
    isWorkDirBrowseVisible=((Boolean)session.getAttribute("isWorkDirBrowseVisible")).booleanValue();
    isVobAccessVisible=((Boolean)session.getAttribute("isVobAccessVisible")).booleanValue();
  }

%>



<TABLE cellSpacing=0 cellPadding=0 border=0>
  <TR>
   <TD colspan="7"><img src="img/blank_flat.gif" border="0"></TD>
  </TR>
  <TR>
  	<TD bgColor=#19559e ><img src="img/blank_flat.gif" width="60" border="0"></TD>
  	<TD ><IMG alt="" width="18" src="<%= (workMode.equals("WORK") ? "img/corner_blu_grey.gif" : "img/corner_blu_white.gif") %>" border=0></TD>
	<TD background="img/center_white.gif"></TD>
  	<TD ><img src="<%= (workMode.equals("WORK") ? "img/corner_grey_white.gif" : (workMode.equals("LOCREP") ? "img/corner_white_grey.gif" : "img/corner_white_white.gif")) %>" width="18" border="0"></TD>
	<TD background="img/center_white.gif"></TD>
  	<TD ><img src="<%= (workMode.equals("LOCREP") ? "img/corner_grey_white.gif" : (workMode.equals("GLOBREP") ? "img/corner_white_grey.gif" : "img/corner_white_white.gif")) %>" width="18" border="0"></TD>
	<TD background="img/center_white.gif"></TD>
  	<TD ><img src="<%= (workMode.equals("GLOBREP") ? "img/corner_grey_white_end.gif" : "img/corner_white_white_end.gif") %>" width="18" border="0"></TD>
  </TR>
</TABLE>

<!-- horizontal dark blue line -->

<TABLE cellSpacing=0 cellPadding=0 width="984" border=0>

  <TR>
   <TD align="left"  bgColor=#19559e>
    <TABLE cellSpacing=0 cellPadding=0 border=0 width=168>
     <TR>
      <TD ALIGN="LEFT" bgColor=#19559e>
       <IMG alt="" src="img/pix.gif" width="6"><IMG alt="" src="img/st-rep-logo.gif"
          border=0>
      </TD>
      <TD ALIGN="CENTER" VALIGN="CENTER" bgColor=#19559e>
      </TD>
     </TR>
    </TABLE>
   </TD>

   <TD align="left"  bgColor=#19559e>
    <TABLE cellSpacing=0 cellPadding=0 border=0 width=817>
     <TR>

    <!-- ACTIONS -->
    <TD class=testobmenu bgColor=#19559e height="19" align="left">
      <A href="#" class="testopercorso">|&nbsp;&nbsp;</A>
    </TD>
    <%
     if ((session.getValue("user")!=null)&&(isVobAccessVisible)&&(tpms.VobManager.getVobsRoot().getElementsByTagName("VOB").getLength()>1))
     {%>
       <TD class=testobmenu bgColor=#19559e height="19" align="left">
         <A href="#" class="testopercorso">|&nbsp;&nbsp;</A>
       </TD>
     <%}
    %>
    <%
     if (session.getValue("user")!=null)
     {%>
       <TD class=testobmenu bgColor=#19559e height="19" align="left">
         <A href="#" class="testopercorso">|&nbsp;&nbsp; </A>
       </TD>
     <%}
    %>
    <%
     if ((session.getValue("user")!=null)&&(isWorkDirBrowseVisible))
     {%>
       <FORM name="navDirForm" action="navDirServlet" method="post">
 	<INPUT TYPE="HIDDEN" name="nextPage" value="nav_dir_out.jsp">
 	<INPUT TYPE="HIDDEN" name="outPage" value="sel_dir_out.jsp">
 	<INPUT TYPE="HIDDEN" name="curDirPath" value="">
 	<INPUT TYPE="HIDDEN" name="nextDirPath" value="">
       </FORM>
       <TD class=testobmenu bgColor=#19559e height="19" align="left">
         <A href="#" class="testopercorso">|&nbsp;&nbsp;</A>
       </TD>
     <%}
    %>
    <TD align=right bgColor=#19559e height=19>
      <IMG height=16 alt="" src="img/pix.gif"  border=0><A
      href=""><IMG height=19
      alt="Help" src="img/but_help.gif" width=52
      border=0></A>
    </TD>

     </TR>
    </TABLE>
   </TD>

  </TR>
  <TR>
    <TD bgColor=#ffffff colSpan=2><IMG height=1 alt=""
      src="img/pix.gif" width=1 border=0></TD>
  </TR>

</TABLE>

<!-- tabella contenuto -->
<TABLE cellSpacing=0 cellPadding=0 width=984 bgColor=#ffffff border=0>

  <TR>
    <!-- MENU -->
    <TD class=testohpnero vAlign=top align=middle width=10>

      <TABLE cellSpacing=0 cellPadding=0 width=10
       border=0>
        <TR>
         <!-- MENU ITEMS LIST -->
         <TD vAlign=top>
             <!-- MENU ITEM (class=testoM - Main Item)-->
             <TABLE cellSpacing=0 cellPadding=0 bgColor=#19559e border=0>
              <TR>
                <TD width="4" class="testoM"><IMG alt="" src="img/blank_thin.gif" border=0></TD>
                <TD width="35" class="testoM" align="left">
                   &nbsp;
                </TD>
              </TR>
              <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><IMG src="img/pix_grey.gif" border=0></TD></TR>
             </TABLE>
             <!-- /MENU ITEM -->
        <!-- MENU ITEMS LIST -->
        </TR>
  
        <!-- MENU TERMINATOR -->
        <TR>
          <TD width=169 colSpan=3><IMG alt="" 
            src="img/menu_close.gif" width=10 
            border=0></TD>
        </TR>

     </TABLE>

    </TD>
    <!-- /MENU -->

    <TD class=testonmenu vAlign=top align=left width="984">

      <TABLE cellSpacing=0 cellPadding=0 width=972 border=0>
        <TR>
          <TD align=left vAlign=top><IMG src="img/blank_flat.gif" width="14"><IMG height=29 alt="" 
            src="img/tit_tpms.gif" border=0><IMG src="img/blank_flat.gif" width="14">
          </TD>
	  <TD align=left class="title" width="80%">/ <B><I><!-- PAGE TITLE --><%= pageTitle %><!-- /PAGE TITLE --></I></B>
           &nbsp;&nbsp;
           <% if (repBool)
              {%>
                &nbsp;<A href="javascript:printView()" border="0"><img src="img/text_icon_small.gif" alt="Print view" border="0"></A>
              <%}
	   %>
           <% if (csvRepBool)
              {%>
                &nbsp;<A href="javascript:getCsv()" border="0"><img src="img/text_csv_small.gif" alt="CSV Data" border="0"></A>
              <%}
	   %>
	   <% if (getServletContext().getInitParameter("supportMail")!=null)
              {%>
                &nbsp;<A href="mailto:<%= getServletContext().getInitParameter("supportMail") %>"><img src="img/mailbig.gif" border="0" alt="Contact us"></A>	 	     
              <%}
	   %> 	     
	  </TD>
          <TD align=right><IMG height=29 alt="" src="img/ico_mechp.gif" 
            border=0></TD>
        </TR>
      </TABLE>


<!-- MAIN TABLE -->
  <!-- MAIN TABLE -->
      <TABLE cellSpacing=0 cellPadding=0 width=750 border=0>
        <TR>
          <TD vAlign=top align=center>
            <TABLE cellSpacing=0 cellPadding=0 width=750 bgColor=#000000 border=0>
              <TR>
                <TD vAlign=top bgColor=#f2f2f2>
                  <TABLE cellSpacing=0 cellPadding=0 width=734 
                  border=0>
                    <TR>
                      <TD width=15><IMG height=24 alt="" 
                        src="img/pix.gif" width=15 vspace=3 border=0></TD>
                      <TD class=testo width=700><!--nome utente-->
                        <TABLE cellSpacing=0 cellPadding=0 width=700 border=0>
                          <TR>
                            <TD class="testlocationbar" vAlign="top">
                            <!-- PLANT -->
                            <B>TPMS host:</B> <%= getServletContext().getInitParameter("TpmsInstName") %>
                            <!-- PLANT -->                           
			    <img src="img/blank_flat_small.gif" border="0">
                            <!-- USER -->
                            <B>User:</B> <%= (session.getValue("user")==null ? "Guest" : session.getValue("user")) %>
                            <!-- USER -->
			    <img src="img/blank_flat_small.gif" border="0">
 			    <%
                             if (session.getValue("user")!=null)
			     {%>
                                <!-- ROLE -->
                                <B>Role:</B> <%= tpms.CtrlServlet.getRoleDesc((String)session.getAttribute("role")) %>
                                <!-- ROLE -->
                    <img src="img/blank_flat_small.gif" border="0">
                                <!-- DIVISION -->
                                <B>Ux grp:</B> <%= (String)session.getAttribute("division") %>
                                <!-- DIVISION -->
   			        <img src="img/blank_flat_small.gif" border="0">
			     <%}
			    %>	
			    <%
                             if (session.getAttribute("vobDesc")!=null)
			     {%>
                            	<!-- VOB -->
                            	<B>Vob:</B> <%= session.getAttribute("vobDesc") %>
                            	<!-- VOB -->
  			        <img src="img/blank_flat_small.gif" border="0">
			     <%}
			    %>
			  </TD>			
                          </TR>
                        </TABLE>                      
		      </TD>
                      <TD vAlign=top align=right >&nbsp;</TD>
                      <TD vAlign=top width=1><IMG height=22 alt="" 
                        src="img/pix_grigio.gif" width=1 
                        border=0></TD>
                    </TR>
                  </TABLE>
                </TD>
              </TR>
              <TR>
                <TD><IMG height=1 alt="" src="img/pix_grey.gif" 
                  width=934 border=0></TD>
              </TR>
            </TABLE>
          </TD>
          <TD vAlign=top width=63><IMG height=25 alt="" 
            src="img/sez_close_01.gif" width=63 border=0>&nbsp; 
          </TD>
       </TR>
    </TABLE>
  </TD>
 </TR>
</TABLE>

<!-- /MAIN TABLE -->
