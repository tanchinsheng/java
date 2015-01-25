<%@ page import="it.txt.afs.servlets.PacketServlet"%>
<%@ page import="tpms.utils.UserUtils"%>
<%@ page import="tpms.utils.Vob"%>
<%@ page import="it.txt.afs.servlets.master.AfsServletUtils"%>
<%@ page import="it.txt.general.utils.GeneralStringUtils"%>
<%@ page import="it.txt.afs.servlets.VobQueryServlet"%>
<%@ page import="it.txt.afs.security.AfsSecurityManager"%>
<%@ page import="it.txt.tpms.servlets.GroupLinesetVobQueryServlet"%>
<%@ page import="it.txt.tpms.servlets.LoadReceivedLinesetPackage"%>
<%@ page import="it.txt.tpms.lineset.filters.servlets.FilterManagement" %>
<%
    String workMode = (session.getAttribute("workMode") == null ? "NONE" : (String) session.getAttribute("workMode"));
    String t = Long.toString(System.currentTimeMillis());
    boolean isSendWorkModeEnabled = false;
    boolean isRecWorkModeEnabled = false;
    boolean isLocRepModeEnabled = false;
    boolean isGlobRepModeEnabled = false;
    boolean isWorkDirBrowseVisible = false;
    boolean isVobAccessVisible = false;
    boolean isAidedFtpServiceEnabled = false;
    String currentUser = (String) session.getAttribute("user");
 
    if (currentUser != null) {
        isSendWorkModeEnabled = ((Boolean) session.getAttribute("isSendWorkModeEnabled")).booleanValue();
        isRecWorkModeEnabled = ((Boolean) session.getAttribute("isRecWorkModeEnabled")).booleanValue();
        isLocRepModeEnabled = ((Boolean) session.getAttribute("isLocRepModeEnabled")).booleanValue();
        isGlobRepModeEnabled = ((Boolean) session.getAttribute("isGlobRepModeEnabled")).booleanValue();
        isAidedFtpServiceEnabled = ((Boolean) session.getAttribute("isAidedFtpServiceEnabled")).booleanValue();
        isWorkDirBrowseVisible = ((Boolean) session.getAttribute("isWorkDirBrowseVisible")).booleanValue();
        isVobAccessVisible = ((Boolean) session.getAttribute("isVobAccessVisible")).booleanValue();

    }

    String vobDescriptionWithLabel = "";
    String vobTypeWithLabel = "";

    if (!GeneralStringUtils.isEmptyString((String) session.getAttribute("vobDesc")) ){
        vobDescriptionWithLabel = "<B>Vob: </B>" + session.getAttribute("vobDesc");
        vobTypeWithLabel = "<B>Type: </B>" + session.getAttribute("vobType");
    } else if (UserUtils.hasAfsRole(currentUser)) {
        Vob afsTVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME);
        Vob afsRVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME);
        if (afsTVob != null) {
            vobDescriptionWithLabel = "<nobr><B>Send plant: </B>" + afsTVob.getPlant();
        }

        if (afsRVob != null){
            vobTypeWithLabel = "<nobr><B>Recieve plant: </B>" + afsRVob.getPlant();
        }
    }

    boolean showReports = false;
    if (!GeneralStringUtils.isEmptyString(currentUser) && UserUtils.hasQueryUserRole(currentUser) ){
        showReports = true;
    }
%>




<script type="text/javascript" language="javascript">
    function openHelpWindow()
    {
        //window.open("<=contextPath%>/help/main_help.jsp", "help_window", "width=900,height=270,resizable=yes,scrollbars=no,status=0,location=no,menubar=no");
        window.open("<%=config.getServletContext().getInitParameter("helpLink")%>", "help_window", "width=900,height=270,resizable=yes,scrollbars=yes,status=0,location=no,menubar=no");
    }
</script>

<% if (session.getAttribute("user") != null) {%>
<FORM name="logOutForm" action="logoutServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="xxx">
</FORM>
<FORM name="tpVobQryForm" action="vobQryServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="">
</FORM>
<FORM name="LinesetVobQryForm" action="lsVobQryServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="">
</FORM>
<FORM name="tpDbQryForm" action="dbQryServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="qryType" VALUE="">
</FORM>
<FORM name="topSelVobForm" action="selectVobServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="foo" VALUE="foo">
</FORM>
<FORM name="topUserProfileForm" action="userProfileServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="action"
           VALUE="<%= (tpms.CtrlServlet.isUserAdmin((String)session.getAttribute("user")) ? "report" : "view")  %>">
</FORM>
<FORM name="topTesterInfoForm" action="testerInfoServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="action" VALUE="report"> <!--view-->
</FORM>
<FORM name="topEmailInfoForm" action="emailInfoServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="action" VALUE="report"> <!--view-->
</FORM>
<FORM name="topVobForm" action="vobManagerServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="action" VALUE="report"> <!--view-->
</FORM>

<FORM name="changeModeForm" action="changeModeServlet" method="post">
    <INPUT TYPE="HIDDEN" NAME="mode" VALUE="">
</FORM>
<%}%>

<TABLE cellSpacing=0 cellPadding=0 border=0>
    <TR>
        <TD colspan="10"><img alt="" src="img/blank_flat.gif" border="0"></TD>
    </TR>
    <TR>
        <TD class="mainColor"><img alt="" src="img/blank_flat.gif" width="60" border="0"></TD>
        <TD><img alt="" width="18" src="<%= (workMode.equals("SENDWORK") ? "img/corner_blu_grey.gif" : "img/corner_blu_white.gif") %>" border=0></TD>
        <TD background="<%= (workMode.equals("SENDWORK") ? "img/center_grey.gif" : "img/center_white.gif") %>"><% if ((!workMode.equals("SENDWORK")) && (isSendWorkModeEnabled)) {%><A HREF="javascript:document.changeModeForm.mode.value='SENDWORK';document.changeModeForm.submit()"><FONT class="titverdi" SIZE="-1">SEND WORK MODE</FONT></A><%} else {%><FONT class="titverdi" SIZE="-1"><%= (!isSendWorkModeEnabled ? "SEND WORK MODE" : "<B>SEND WORK MODE</B>") %></FONT><%}%></TD>
        <TD><img alt="" src="<%= (workMode.equals("SENDWORK") ? "img/corner_grey_white.gif" : (workMode.equals("RECWORK") ? "img/corner_white_grey.gif" : "img/corner_white_white.gif")) %>" width="18" border="0"></TD>
        <TD background="<%= (workMode.equals("RECWORK") ? "img/center_grey.gif" : "img/center_white.gif") %>"><% if ((!workMode.equals("RECWORK")) && (isRecWorkModeEnabled)) {%><A HREF="javascript:document.changeModeForm.mode.value='RECWORK';document.changeModeForm.submit()"><FONT class="titverdi" SIZE="-1">RECEIVE WORK MODE</FONT></A><%} else {%><FONT class="titverdi" SIZE="-1"><%= (!isRecWorkModeEnabled ? "RECEIVE WORK MODE" : "<B>RECEIVE WORK MODE</B>") %></FONT><%}%></TD>
        <TD><img alt="" src="<%= (workMode.equals("RECWORK") ? "img/corner_grey_white.gif" : (workMode.equals("AIDED_FTP") ? "img/corner_white_grey.gif" : "img/corner_white_white.gif")) %>" width="18" border="0"></TD>
        <TD background="<%= (workMode.equals("AIDED_FTP") ? "img/center_grey.gif" : "img/center_white.gif") %>"><% if ((!workMode.equals("AIDED_FTP")) && (isAidedFtpServiceEnabled)) {%><A HREF="javascript:document.changeModeForm.mode.value='AIDED_FTP';document.changeModeForm.submit()"><FONT class="titverdi" SIZE="-1">AIDED FTP SERVICE</FONT></A><%} else {%><FONT class="titverdi" SIZE="-1"><%= (!isAidedFtpServiceEnabled ? "AIDED FTP SERVICE" : "<B>AIDED FTP SERVICE</B>") %></FONT><%}%></TD>
        <TD><img alt="" src="<%= (workMode.equals("AIDED_FTP") ? "img/corner_grey_white.gif" : (workMode.equals("LOCREP") ? "img/corner_white_grey.gif" : "img/corner_white_white.gif")) %>" width="18" border="0"></TD>
        <TD background="<%= (workMode.equals("LOCREP") ? "img/center_grey.gif" : "img/center_white.gif") %>"><% if ((!workMode.equals("LOCREP")) && (isLocRepModeEnabled)) {%><A HREF="javascript:document.changeModeForm.mode.value='LOCREP';document.changeModeForm.submit()"><FONT class="titverdi" SIZE="-1">CHECK MODE</FONT></A><%} else {%><FONT class="titverdi" SIZE="-1"><%= (!isLocRepModeEnabled ? "CHECK MODE" : "<B>CHECK MODE</B>") %></FONT><%}%></TD>
        <TD><img alt="" src="<%= (workMode.equals("LOCREP") ? "img/corner_grey_white.gif" : (workMode.equals("GLOBREP") ? "img/corner_white_grey.gif" : "img/corner_white_white.gif")) %>" width="18" border="0"></TD>
        <TD background="<%= (workMode.equals("GLOBREP") ? "img/center_grey.gif" : "img/center_white.gif") %>"><% if ((!workMode.equals("GLOBREP")) && (isGlobRepModeEnabled)) {%><A HREF="javascript:document.changeModeForm.mode.value='GLOBREP';document.changeModeForm.submit()"><FONT class="titverdi" SIZE="-1">GLOBAL REPORT MODE</FONT></A><%} else {%><FONT class="titverdi" SIZE="-1"><%= (!isGlobRepModeEnabled ? "GLOBAL REPORT MODE" : "<B>GLOBAL REPORT MODE</B>") %></FONT><%}%></TD>
        <TD><img alt="" src="<%= (workMode.equals("GLOBREP") ? "img/corner_grey_white_end.gif" : "img/corner_white_white_end.gif") %>" width="18" border="0"></TD>
    </TR>
</TABLE>

<!-- horizontal dark blue line -->

<TABLE cellSpacing=0 cellPadding=0 width=984 border=0>
    <TBODY>
        <TR>
            <TD align="left" class="mainColor">
                <TABLE cellSpacing=0 cellPadding=0 border=0 width=168>
                    <TR>
                        <TD ALIGN="LEFT" class="mainColor">
                            <img alt="" src="img/pix.gif" width="6"><img alt="" src="img/st-rep-logo.gif"
                                                                         border=0>
                        </TD>
                        <%
                            if (!request.getRequestURI().endsWith("login.jsp")) {%>
                        <TD ALIGN="CENTER" VALIGN="CENTER" class="mainColor">
                            <A href="<%= (session.getAttribute("user")!=null ? "javascript:document.logOutForm.submit()" : "login.jsp") %>"><img alt=""
                                    alt=""
                                    src="<%= (session.getAttribute("user")!=null ? "img/but_logout_top.gif" : "img/but_login_top.gif") %>"
                                    border=1></A>
                        </TD>
                        <%}
                        %>
                    </TR>
                </TABLE>
            </TD>

            <TD align="left" class="mainColor">
                <TABLE cellSpacing=0 cellPadding=0 border=0 width=817>
                    <TR>

                        <!-- ACTIONS -->
                        <%
                            if (!request.getRequestURI().endsWith("main.jsp")) {%>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
                            <A href="main.jsp" class="testopercorso">|&nbsp;&nbsp; HOME</A>
                        </TD>
                        <%} else {%>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
                            <A href="#" class="testopercorso">|&nbsp;&nbsp;</A>
                        </TD>
                        <%}
                        %>
                        <%
                            if ((session.getAttribute("user") != null) && (isVobAccessVisible) && (tpms.VobManager.getVobsRoot().getElementsByTagName("VOB").getLength() >= 1)) {%>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
                            <A href="javascript:document.topSelVobForm.submit()" class="testopercorso">|&nbsp;&nbsp;
                                SELECT VOB</A>
                        </TD>
                        <%}
                        %>
                        <%
                            if (session.getAttribute("user") != null) {%>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
                            <A href="javascript:document.topUserProfileForm.submit()" class="testopercorso">|&nbsp;&nbsp; <%= (tpms.CtrlServlet.isUserAdmin((String) session.getAttribute("user")) ? "USER PROFILES" : "MY PROFILE")  %></A>
                        </TD>
                        <%}
                        %>
                        <%
                            if ((session.getAttribute("user") != null) && (tpms.CtrlServlet.isUserAdmin((String) session.getAttribute("user")))) {%>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
                            <A href="javascript:document.topTesterInfoForm.submit()" class="testopercorso">|&nbsp;&nbsp;TESTER INFO</A>
                        </TD>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
			                <A href="javascript:document.topEmailInfoForm.submit()" class="testopercorso">|&nbsp;&nbsp;EMAIL INFO</A>
                        </TD>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
                            <A href="javascript:document.topVobForm.submit()" class="testopercorso">|&nbsp;&nbsp; VOBS MNG</A>
                        </TD>
                        <%}
                        %>
                        <%
                            if ((session.getAttribute("user") != null) && (isWorkDirBrowseVisible)) {%>
                        <FORM name="navDirForm" action="navDirServlet" method="post">
                            <INPUT TYPE="HIDDEN" name="nextPage" value="nav_dir_out.jsp">
                            <INPUT TYPE="HIDDEN" name="outPage" value="sel_dir_out.jsp">
                            <INPUT TYPE="HIDDEN" name="curDirPath" value="">
                            <INPUT TYPE="HIDDEN" name="nextDirPath" value="">
                        </FORM>
                        <TD class=testobmenu class="mainColor" height="19" align="left">
                            <A href="javascript:document.navDirForm.submit()" class="testopercorso">|&nbsp;&nbsp; WORK
                                DIR</A>
                        </TD>
                        <%}
                        %>
                        <TD align=right class="mainColor" height=19>
                            <img alt="" height=16 alt="" src="img/pix.gif" border=0>
                            <A href="#" onClick="openHelpWindow()"><img height=19
                                                                        alt="Help" src="img/but_help.gif" width=52
                                                                        border=0></A>
                        </TD>

                    </TR>
                </TABLE>
            </TD>

        </TR>
        <TR>
            <TD bgColor=#ffffff colSpan=2><img alt="" height=1 alt=""
                                               src="img/pix.gif" width=1 border=0></TD>
        </TR>
    </TBODY>
</TABLE>

<!-- tabella contenuto -->
<TABLE cellSpacing=0 cellPadding=0 width=984 bgColor=#ffffff border=0>
<TBODY>
<TR>
<!-- MENU -->
<TD class=testohpnero vAlign=top align=middle width=169 >

<TABLE cellSpacing=0 cellPadding=0 width=169
       border=0>
<TBODY>
<TR>
<!-- MENU ITEMS LIST -->
<TD vAlign=top>
<% if (session.getAttribute("user") == null) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img alt="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM" align="left">
            &nbsp;
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img alt="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img alt="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM" align="left">
            &nbsp;
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img alt="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img alt="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM" align="left">
            &nbsp;
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img alt="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%} else {
    /**
     * menu logged user...
     */
    if ((session.getAttribute("showMenuLinesetQueriesVob") == null) || (((Boolean) session.getAttribute("showMenuLinesetQueriesVob")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img alt="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM" align="left">
            <img alt="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Lineset Queries
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img alt="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img alt="" src="img/blank_thin.gif" border=0><img alt="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img alt="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM"
                   HREF="javascript:document.LinesetVobQryForm.qryType.value='vob_my_linesets';document.LinesetVobQryForm.submit()">
                    <% if (workMode.equals("SENDWORK")) {%>
                    My Linesets
                    <%} else {%>
                    Lineset
                    <%}%>

                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img alt="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<% if (workMode.equals("SENDWORK") && UserUtils.hasEngineerRole(currentUser)) {%>
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img alt="" src="img/blank_thin.gif" border=0><img alt="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoSM">
            <img alt="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/groupLinesetVobQueryServlet?action=<%=GroupLinesetVobQueryServlet.GET_GROUP_LINESETS_ACTION%>">
                    Shared Linesets
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img alt="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1"><A class="testoSM" HREF="<%=contextPath%>/loadReceivedLineset?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=LoadReceivedLinesetPackage.INITIALIZE_RECEIVED_LINESETS_ACTION%>">Lineset distributed</A></FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}%>
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img alt="" src="img/blank_thin.gif" border=0><img alt="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM"
                   HREF="javascript:document.LinesetVobQryForm.qryType.value='vob_lineset_submits';document.LinesetVobQryForm.submit()">
                    Lineset in-progress
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}
%>

<%
    if ((session.getAttribute("showMenuLinesetHistVob") == null) || (((Boolean) session.getAttribute("showMenuLinesetHistVob")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM" align="left">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Lineset History
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="ls_vob_search.jsp?qryType=vob_lineset_history&t=<%= t %>">
                    <% if (workMode.equals("SENDWORK")) {%>
                    My Linesets History
                    <%} else {%>
                    Lineset History
                    <%}%>
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<%}
%>

<%
    if ((session.getAttribute("showMenuTpQueriesVob") == null) || (((Boolean) session.getAttribute("showMenuTpQueriesVob")).booleanValue())) {
        if (workMode.equals("RECWORK")) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                View TP/Aided Ftp (db)
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/receivedTpPacketsServlet">
                    <nobr>To Be Processed</nobr>
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<%}%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM" align="left">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;TP Queries
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1"><A class="testoSM"
                               HREF="javascript:document.tpVobQryForm.qryType.value='vob_tp_to_process';document.tpVobQryForm.submit()">TP distributed</A></FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM"
                   HREF="javascript:document.tpVobQryForm.qryType.value='vob_tp_in_progress';document.tpVobQryForm.submit()">
                    TP in-progress
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM"
                   HREF="javascript:document.tpVobQryForm.qryType.value='vob_tp_in_production';document.tpVobQryForm.submit()">
                    TP In Production
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_vob_search.jsp?qryType=vob_tp_search&t=<%= t %>">
                    TP Search
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}

    if ((session.getAttribute("showMenuTpHistVob") == null) || (((Boolean) session.getAttribute("showMenuTpHistVob")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;TP History
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_vob_search.jsp?qryType=vob_tp_history&t=<%= t %>">
                    Single TP History
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%if (workMode.equals("RECWORK")) {%>
<!-- MENU ITEM (class=testoM - primary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Aided Ftp Queries
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/queryVobPackets?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=VobQueryServlet.RECIEVED_SENT_PACKAGES_ACTION%>">
                    Received list
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/queryVobPackets?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=VobQueryServlet.RECIEVED_EXTRACTED_PACKAGES_ACTION%>">
                    Extracted list
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}%>
<%}
%>

<%if (showReports) {%>
  <!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="<%=contextPath%>/img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="<%=contextPath%>/img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Reports
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="<%=contextPath%>/img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="<%=contextPath%>/img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="<%=contextPath%>/img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/filterManagement?action=<%=FilterManagement.ACTION_INITIALIZE_SEARCHING_VALUES%>">
                    My Lineset Filters
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="<%=contextPath%>/img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="<%=contextPath%>/img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="<%=contextPath%>/img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/acerboTpReport?status=In_Production">
                    TP In Production
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="<%=contextPath%>/img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="<%=contextPath%>/img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="<%=contextPath%>/img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                 <A class="testoSM" HREF="<%=contextPath%>/acerboTpReport?status=In_Validation">
                    TP In Validation 
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="<%=contextPath%>/img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="<%=contextPath%>/img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="<%=contextPath%>/img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                 <A class="testoSM" HREF="<%=contextPath%>/acerboTpReport?status=Ready_to_production">
                    Ready To Production 
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="<%=contextPath%>/img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="<%=contextPath%>/img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="<%=contextPath%>/img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/acerboTpNotInProductionReport">
                    TP Nearly Ready
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="<%=contextPath%>/img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<%
    if ((session.getAttribute("showMenuTpHistDb") == null) || (((Boolean) session.getAttribute("showMenuTpHistDb")).booleanValue())) {%>
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_prod_lib_hist&t=<%= t %>">
                    TP History
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}%>
<!-- /MENU ITEM -->
<%
    }
%>
<%if ((session.getAttribute("showMenuTpQueriesDb") == null) || (((Boolean) session.getAttribute("showMenuTpQueriesDb")).booleanValue()) ||
      (session.getAttribute("showMenuLsQueriesDb") == null) || (((Boolean) session.getAttribute("showMenuLsQueriesDb")).booleanValue()) ||
      (session.getAttribute("showMenuTpHistDb") == null) || (((Boolean) session.getAttribute("showMenuTpHistDb")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Queries
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<%}%>
<!-- /MENU ITEM -->
<%
    if ((session.getAttribute("showMenuLsQueriesDb") == null) || (((Boolean) session.getAttribute("showMenuLsQueriesDb")).booleanValue())) {%>

<!-- MENU ITEM (class=testoM - Main Item)-->
<%--TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;LS Queries (db)
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE--%>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="ls_search.jsp?qryType=db_ls_search&t=<%= t %>">
                    Line Search
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<%}%>
<% if ((session.getAttribute("showMenuTpQueriesDb") == null) || (((Boolean) session.getAttribute("showMenuTpQueriesDb")).booleanValue())) {%>


<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<%--TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_tp_to_process&t=<%= t %>">
                    TP distributed
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE--%>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM)-->
<%--TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_tp_in_progress&t=<%= t %>">
                    TP in-progress
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE--%>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM)-->
<%--TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_tp_in_production&t=<%= t %>">
                    TP in production
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE--%>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_tp_search&t=<%= t %>">
                    TP Search
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}%>
<%
    if ((session.getAttribute("showMenuTpSearchDb") == null) || (((Boolean) session.getAttribute("showMenuTpSearchDb")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;TP Search
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<%--<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_tp_search_base&t=<%= t %>">
                    TP search
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>--%>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_tp_search&t=<%= t %>">
                    TP Search
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->




<!-- /MENU ITEM -->
<%
    }
%>

<%
    if ((session.getAttribute("showMenuTpHistDb") == null) || (((Boolean) session.getAttribute("showMenuTpHistDb")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<%--TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;TP History
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE--%>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_search.jsp?qryType=db_tp_history&t=<%= t %>">
                    Single TP History
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}%>
<%if ((session.getAttribute("showMenuTpQueriesDb") == null) || (((Boolean) session.getAttribute("showMenuTpQueriesDb")).booleanValue()) ||
      (session.getAttribute("showMenuLsQueriesDb") == null) || (((Boolean) session.getAttribute("showMenuLsQueriesDb")).booleanValue()) ||
      (session.getAttribute("showMenuTpHistDb") == null) || (((Boolean) session.getAttribute("showMenuTpHistDb")).booleanValue())) {%>
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Aided FTP
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/queryDatabasePackets">
                    Search Packets
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}%>



<%

    if ((session.getAttribute("showMenuActionsDb") == null) || (((Boolean) session.getAttribute("showMenuActionsDb")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Actions
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM"
                   HREF="javascript:document.tpDbQryForm.qryType.value='db_tp_offLine';document.tpDbQryForm.submit()">
                    TP OffLine
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- MENU ITEM (class=testoSM)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width="4" border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/dbTpListForTpComments">
                    TP Comments
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- /MENU ITEM -->
<%}
   if ((session.getAttribute("showMenuDeleteTpVob") == null) || (((Boolean) session.getAttribute("showMenuDeleteTpVob")).booleanValue())) {%>
<!-- MENU ITEM (class=testoM - Main Item)-->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Actions
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->

<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                <A class="testoSM" HREF="tp_vob_search.jsp?qryType=vob_delete_tp_search&t=<%= t %>">
                    Tp Delete
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<%}
  if ((session.getAttribute("showAidedFtpServiceMenu") == null) || (((Boolean) session.getAttribute("showAidedFtpServiceMenu")).booleanValue())) {
                           %>



<!-- MENU ITEM (class=testoM - primary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Send
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                &nbsp;&nbsp;<A class="testoSM" HREF="<%=contextPath%>/packetServlet?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=PacketServlet.CREATE_NEW_PACKET_ACTION%>">
                    New package
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                &nbsp;&nbsp;<A class="testoSM" HREF="<%=contextPath%>/queryVobPackets?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=VobQueryServlet.OWNED_SENT_PACKAGES_ACTION%>">
                    Sent list
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            &nbsp;&nbsp;<FONT SIZE="-1">
                <A class="testoSM" HREF="<%=contextPath%>/queryVobPackets?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=VobQueryServlet.OWNED_EXTRACTED_PACKAGES_ACTION%>">
                    Extracted list
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoM - primary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Receive
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                &nbsp;&nbsp;<A class="testoSM" HREF="<%=contextPath%>/queryVobPackets?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=VobQueryServlet.RECIEVED_SENT_PACKAGES_ACTION%>">
                    Received list
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                &nbsp;&nbsp;<A class="testoSM" HREF="<%=contextPath%>/queryVobPackets?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=VobQueryServlet.RECIEVED_EXTRACTED_PACKAGES_ACTION%>">
                    Extracted list
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- MENU ITEM (class=testoM - primary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="4" class="testoM"><img ALT="" src="img/blank_thin.gif" border=0></TD>
        <TD width="165" class="testoM">
            <img ALT="" src="img/freccia_down.gif" border=0>
            <FONT class="testoM" SIZE="-1">
                &nbsp;Database queries
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- MENU ITEM (class=testoSM - Secondary Item) -->
<TABLE cellSpacing=0 cellPadding=0 class="mainColor" border=0>
    <TR>
        <TD width="8" class="testoSM"><img ALT="" src="img/blank_thin.gif" border=0><img ALT="" src="img/blank_thin.gif"
                                                                                         border=0></TD>
        <TD width="165" class="testoSM">
            <img ALT="" src="img/blank.gif" width=4 border=0>&nbsp;
            <FONT SIZE="-1">
                &nbsp;&nbsp;<A class="testoSM" HREF="<%=contextPath%>/queryDatabasePackets">
                    Search packets
                </A>
            </FONT>
        </TD>
    </TR>
    <TR bgcolor="#ffffff"><TD colSpan=2 width="169"><img ALT="" src="img/pix_grey.gif" border=0></TD></TR>
</TABLE>
<!-- /MENU ITEM -->
<!-- /MENU ITEM -->
<%
  }
}    //end logged user menu

%>
</TD>
<!-- MENU ITEMS LIST -->
</TR>

<!-- MENU TERMINATOR -->
<TR>
    <TD width=169 colSpan=3><img alt=""
                                 src="img/menu_close.gif" width=168
                                 border=0></TD>
</TR>
<TR>
    <TD width=169 colSpan=3 align="center">
        <font face="times" color="#4444EE" class="txtlogo"><br>TXT<br>e-solutions</font>
    </TD>
</TR>
</TBODY>
</TABLE>

</TD>
<!-- /MENU -->

<TD class=testonmenu vAlign=top align=left width=834>

<TABLE cellSpacing=0 cellPadding=0 width=816 border=0>
    <TBODY>
        <TR>
            <TD align="left" vAlign="top"><img ALT="" src="img/blank_flat.gif" width="14"><img height=29 alt=""
                                                                                        src="img/tit_tpms.gif" border=0><img alt=""
                    src="img/blank_flat.gif" width="14">
            </TD>
            <TD align="left" valign="<%= (repBool ? "center" : "center") %>" class="title" width="80%">/ <B><I>
                <!-- PAGE TITLE --><%= pageTitle %><!-- /PAGE TITLE --></I></B>
                &nbsp;&nbsp;
                <% if (repBool) {%>
                &nbsp;<A href="javascript:printView()"><img ALT="" src="img/text_icon_small.gif" alt="Print view"
                                                                       border="0"></A>
                <%}
                %>
                <% if (csvRepBool) {%>
                &nbsp;<A href="javascript:getCsv()"><img ALT="" src="img/text_csv_small.gif" alt="CSV Data"
                                                                    border="0"></A>
                <%}
                %>
                <% if (config.getServletContext().getInitParameter("supportMail") != null) {%>
                &nbsp;<A href="mailto:<%= config.getServletContext().getInitParameter("supportMail") %>"><img alt=""
                    src="img/mailbig.gif" border="0" alt="Contact us"></A>
                <%}
                %>
            </TD>
            <TD align=right><img alt="" height=29 src="img/ico_mechp.gif"
                                 border=0></TD>
        </TR>
    </TBODY>
</TABLE>


<!-- MAIN TABLE -->
<TABLE cellSpacing=0 cellPadding=0 width=750 border=0>
    <TBODY>
        <TR>
            <TD vAlign=top align=center>
            <TABLE cellSpacing=0 cellPadding=0 width=750 bgColor=#000000
                   background=img/pix.gif border=0>
                <TBODY>
                    <TR>
                        <TD vAlign=top bgColor=#f2f2f2>
                            <TABLE cellSpacing=0 cellPadding=0 width=734
                                   border=0>
                                <TBODY>
                                    <TR>
                                        <TD width=15><img height=24 alt=""
                                                          src="img/pix.gif" width=15 vspace=3 border=0></TD>
                                        <TD class=testo width=700><!--nome utente-->
                                            <TABLE cellSpacing=0 cellPadding=0 width=700 border=0>
                                                <TBODY>
                                                    <TR>
                                                        <TD class="testlocationbar" vAlign="top">
                                                            <!-- PLANT -->
                                                            <B>Web site:</B> <%= config.getServletContext().getInitParameter("TpmsInstName") %>
                                                            <!-- PLANT -->
                                                            <img ALT="" src="img/blank_flat_small.gif" border="0">
                                                            <!-- USER -->
                                                            <B>Tpms user:</B> <%= (session.getAttribute("user") == null ? "Guest" : session.getAttribute("user")) %>
                                                            <!-- USER -->
                                                            <img ALT="" src="img/blank_flat_small.gif" border="0">
                                                            <%
                                                                if (session.getAttribute("user") != null) {%>
                                                            <!-- ROLE -->
                                                            <B>Role:</B> <%= tpms.CtrlServlet.getRoleDesc((String) session.getAttribute("role")) %>
                                                            <!-- ROLE -->
                                                            <img ALT="" src="img/blank_flat_small.gif" border="0">
                                                            <!-- DIVISION -->
                                                            <B>Ux grp:</B> <%= (String) session.getAttribute("division") %>
                                                            <!-- DIVISION -->
                                                            <img ALT="" src="img/blank_flat_small.gif" border="0">
                                                            <%}
                                                            %>
                                                            <% if (isVobAccessVisible) {%>
                                                            <!-- VOB -->
                                                            <%=vobDescriptionWithLabel%>
                                                            <!-- VOB -->
                                                            <img ALT="" src="img/blank_flat_small.gif" border="0">
                                                            <!-- VOB type -->
                                                            <%=vobTypeWithLabel%>
                                                            <!-- VOB type-->
                                                            <img ALT="" src="img/blank_flat_small.gif" border="0">
                                                            <%}
                                                            %>
                                                        </TD>
                                                    </TR>
                                                </TBODY>
                                            </TABLE>
                                        </TD>
                                        <TD vAlign=top align=right>&nbsp;</TD>
                                        <TD vAlign=top width=1><img height=22 alt=""
                                                                    src="img/pix_grigio.gif" width=1
                                                                    border=0></TD>
                                    </TR>
                                </TBODY>
                            </TABLE>
                        </TD>
                    </TR>
                    <TR>
                        <TD><img height=1 src="img/pix_grey.gif"
                                 width=780 border=0></TD>
                    </TR>
                </TBODY>
            </TABLE>

            <BR>

            <TABLE WIDTH="95%">

                <!-- CONTENT START -->
