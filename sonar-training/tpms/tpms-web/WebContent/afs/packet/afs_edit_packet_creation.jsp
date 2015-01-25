<%@ page import="it.txt.afs.packet.Packet" %>
<%@ page import="it.txt.afs.packet.utils.PacketConstants" %>
<%@ page import="it.txt.general.utils.HtmlUtils" %>
<%@ page import="tol.reportSel" %>
<%@ page import="tol.slctLst" %>
<%@ page import="tpms.VobManager" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.txt.afs.servlets.master.AfsServletUtils"%>
<%@ page import="it.txt.afs.servlets.FileDirectorySelectionServlet"%>
<%
    String contextPath = request.getContextPath();
    String currentUserLogin = (String) session.getAttribute("user");
    Packet packet = (Packet) session.getAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME);
    Vector destinationPlants = VobManager.getTVobsInfo();
    reportSel repSel = (reportSel) session.getAttribute("repsel");
    slctLst dbUsrLst = null;
    slctLst dbPlant;
    Vob tVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME);
    String currentSelectedPlant = "";
    if (!GeneralStringUtils.isEmptyString(packet.getDestinationPlant())){
        currentSelectedPlant = packet.getDestinationPlant();
    } else if (tVob != null) {
        currentSelectedPlant = tVob.getPlant();
    }

    String currentUserWorkDir = UserUtils.getWorkDirectory(currentUserLogin);
    String currentUserHomeDir = UserUtils.getHomeDirectory(currentUserLogin);
    if (GeneralStringUtils.isEmptyString(currentUserWorkDir))
        currentUserWorkDir = currentUserHomeDir;
%>
<HTML>
<HEAD>
<TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="<%=contextPath%>/default.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
<script language="javascript">

function selectXferPath(){
    var objForm = window.document.forms['pckActionForm'];
    objForm.elements['<%=AfsServletUtils.ACTION_FIELD_NAME%>'].value = '<%=PacketServlet.SELECT_XFER_PATH%>';
    var xferPath = objForm.<%=PacketConstants.XFERPATH_FIELD_NAME%>.value;
    if (!isEmptyString(xferPath)) {
        objForm.<%=FileDirectorySelectionServlet.INITIAL_DIRECTORY_PARAMETER_NAME%>.value = xferPath;
    }
    objForm.submit();
}

var selectedFirstRecieveLogin = "<%=GeneralStringUtils.isEmptyString(packet.getFirstRecieveLogin()) ? "" : packet.getFirstRecieveLogin()%>";
var selectedSecondRecieveLogin = "<%=GeneralStringUtils.isEmptyString(packet.getSecondRecieveLogin()) ? "" : packet.getSecondRecieveLogin()%>";

var usersArray = new Array();
var usersEmailArray = new Array();


<%
String plant;
Vector dbUsers;
for (int i = 0; i < destinationPlants.size(); i++)
{
    plant = (String) ((Hashtable) destinationPlants.elementAt(i)).get(VobManager.VOB_PLANT_ATTRIBUTE);
    try {
        dbUsrLst = repSel.get("USER");
        dbPlant = repSel.get("PLANT");
        dbPlant.setVal(plant);
        dbUsrLst.fetch();
    } catch(Exception e) {}
%>
    usersArray['<%=plant%>'] = new Array();
    usersArray['<%=plant%>'][0] = '';//'no.db.connection';
    usersEmailArray['<%=plant%>'] = new Array();
    usersEmailArray['<%=plant%>'][0] = '';// 'no.db.connection@txt.it';
    <%
    if ((dbUsrLst!=null) && (!dbUsrLst.isBlank()))
    {
        dbUsers = dbUsrLst.getVector();
        for (int j = 0; j < dbUsers.size(); j++)
        {%>
        usersArray['<%=plant%>'][<%=j + 1%>] = '<%= dbUsers.elementAt(j) %>';
        usersEmailArray['<%=plant%>'][<%=j + 1%>] = '<%= dbUsrLst.getAttrVal("email",j) %>';

        <%}
    }
}
%>

function populateUsersListCombo() {
    var objForm = window.document.forms['pckActionForm'];
    var objPlant = objForm.<%=PacketConstants.DESTINATION_PLANT_FIELD_NAME%>;
    var strSelectedPlant = objPlant[objPlant.selectedIndex].value;
    var objCombo = objForm.<%=PacketConstants.FIRST_RECIEVE_LOGIN_FIELD_NAME%>;
    populateCombo2(objCombo, usersArray[strSelectedPlant], selectedFirstRecieveLogin);
    //objForm.<%=PacketConstants.FIRST_RECIEVE_EMAIL_FIELD_NAME%>.value = "";
    populateFirstEmailAddress();
    objCombo = objForm.<%=PacketConstants.SECOND_RECIEVE_LOGIN_FIELD_NAME%>;
    populateCombo2(objCombo, usersArray[strSelectedPlant], selectedSecondRecieveLogin);
    //objForm.<%=PacketConstants.SECOND_RECIEVE_EMAIL_FIELD_NAME%>.value = "";
    populateSecondEmailAddress();
}

function populateEmailsAddress(objCombo, objHidden, objComboPlant) {
    if (objComboPlant != null && objCombo != null && objHidden != null) {
        var strSelectedPlant = objComboPlant.options[objComboPlant.selectedIndex].value;
        var index = objCombo.selectedIndex
        if (isEmptyString(strSelectedPlant) || index == 0) {
            objHidden.value = "";
        } else {
            //popola con email utente
            objHidden.value = usersEmailArray[strSelectedPlant][index];
        }
    }
}

function populateFirstEmailAddress() {
    var objForm = window.document.forms['pckActionForm'];
    populateEmailsAddress(objForm.<%=PacketConstants.FIRST_RECIEVE_LOGIN_FIELD_NAME%>,
            objForm.<%=PacketConstants.FIRST_RECIEVE_EMAIL_FIELD_NAME%>,
            objForm.<%=PacketConstants.DESTINATION_PLANT_FIELD_NAME%>)
}

function populateSecondEmailAddress() {
    var objForm = window.document.forms['pckActionForm'];
    populateEmailsAddress(objForm.<%=PacketConstants.SECOND_RECIEVE_LOGIN_FIELD_NAME%>,
            objForm.<%=PacketConstants.SECOND_RECIEVE_EMAIL_FIELD_NAME%>,
            objForm.<%=PacketConstants.DESTINATION_PLANT_FIELD_NAME%>)
}


function controlJobRev(){
    var objJobRevisionField = document.tpActionForm.elements['FIELD.JOB_REV'];
    var jobRevision = objJobRevisionField.value;
    var notAllowedCharPos = filterUserInputNumbersOnly(objJobRevisionField);
    if (notAllowedCharPos >= 0) {
        return true;
    }
    return false;
}
   //controllo numerico su JobRelease
function controlJobRel(){
    if (document.tpActionForm.elements['FIELD.JOB_REL'] != null) {
        var notAllowedCharPos = filterUserInputNumbersOnly(document.tpActionForm.elements['FIELD.JOB_REL']);
        if (notAllowedCharPos >= 0){
            return true;
        }
    }
    return false;
}
function sendPackage() {
    var objForm = window.document.forms['pckActionForm'];

    if (isEmptyString(objForm.<%=PacketConstants.NAME_FIELD_NAME%>.value)) {
        alert("<%=PacketConstants.NAME_LABEL%> is mandatory\npopulate it in order to proceed");
        return;
    }
    if (filterUserInputAphaNumericalChars(objForm.<%=PacketConstants.NAME_FIELD_NAME%>) >= 0) {
        alert("<%=PacketConstants.NAME_LABEL%> contains invalid characters:\nOnly alphanumeric chars ('A..Z' and '0..9') and underscore ('_') are allowed");
        return;
    }
    var objCombo = objForm.<%=PacketConstants.DESTINATION_PLANT_FIELD_NAME%>
    if (objCombo.selectedIndex != -1 && isEmptyString(objCombo.options[objCombo.selectedIndex].value)) {
        alert("<%=PacketConstants.DESTINATION_PLANT_LABEL%> is mandatory\npopulate it in order to proceed");
        return;
    }
    objCombo = objForm.<%=PacketConstants.FIRST_RECIEVE_LOGIN_FIELD_NAME%>
    if (objCombo.selectedIndex != -1 && isEmptyString(objCombo.options[objCombo.selectedIndex].value)) {
        alert("<%=PacketConstants.FIRST_RECIEVE_LOGIN_LABEL%> is mandatory\npopulate it in order to proceed");
        return;
    }
    <%--objCombo = objForm.<%=PacketConstants.SECOND_RECIEVE_LOGIN_FIELD_NAME%>
    if (objCombo.selectedIndex != -1 && isEmptyString(objCombo.options[objCombo.selectedIndex].value)) {
        alert("<%=PacketConstants.SECOND_RECIEVE_LOGIN_LABEL%> is mandatory\npopulate it in order to proceed");
        return;
    }--%>
    if (objCombo.selectedIndex != -1 && isEmptyString(objForm.<%=PacketConstants.XFERPATH_FIELD_NAME%>.value)) {
        alert("<%=PacketConstants.XFERPATH_LABEL%> is mandatory\npopulate it in order to proceed");
        return;
    }

    if (objForm.<%=PacketConstants.TP_RELEASE_FIELD_NAME%> != null && !isEmptyString(objForm.<%=PacketConstants.TP_RELEASE_FIELD_NAME%>.value)){
        var notAllowedCharPos = filterUserInputNumbersOnly(objForm.<%=PacketConstants.TP_RELEASE_FIELD_NAME%>);
        if (notAllowedCharPos >= 0){
            alert("<%=PacketConstants.TP_RELEASE_LABEL%> contains invalid characters:\nOnly number are allowed ('0..9')");
            return false;
        }
    }
    if (objForm.<%=PacketConstants.TP_REVISION_FIELD_NAME%> != null && !isEmptyString(objForm.<%=PacketConstants.TP_REVISION_FIELD_NAME%>.value)){
        var notAllowedCharPos = filterUserInputNumbersOnly(objForm.<%=PacketConstants.TP_REVISION_FIELD_NAME%>);
        if (notAllowedCharPos >= 0){
            alert("<%=PacketConstants.TP_REVISION_LABEL%> contains invalid characters:\nOnly number are allowed ('0..9')");
            return false;
        }
    }
    objForm.elements['<%=AfsServletUtils.ACTION_FIELD_NAME%>'].value = "<%=PacketServlet.SEND_PACKET_ACTION%>";
    objForm.submit();
}
</script>
</HEAD>

<BODY bgColor="#FFFFFF" onLoad="populateUsersListCombo()">
<% boolean repBool = false; boolean csvRepBool = false; String pageTitle = "NEW PACKAGE SEND FORM"; %>
<%@ include file="/top.jsp" %>


<TR>
    <TD ALIGN="LEFT">
        <table cellpadding="0" cellspacing="0" border="0" width="560">
            <tr>
                <td width="4"><img src="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                <td background="<%=contextPath%>/img/tit_center.gif" align="center" class="titverdi" nowrap><b>NEW PACKAGE</b></td>
                <td width="4"><img src="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                <td><img src="<%=contextPath%>/img/pix.gif" width="453" height="18" alt="" border="0"></td>
            </tr>
            <tr>
                <td colspan="4"><img src="<%=contextPath%>/img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
            </tr>
        </table>
    </TD>
</TR>
<TR>
<TD ALIGN="LEFT">
    <FORM name="pckActionForm" action="<%=contextPath%>/packetServlet" method="post">
    <%--xferpath parameters--%>
    <input type="hidden" name="<%=FileDirectorySelectionServlet.RESULT_PAGE_PARAMETER_NAME%>" value="packetServlet?<%=AfsServletUtils.ACTION_FIELD_NAME%>=<%=PacketServlet.SELECT_XFER_PATH%>">
    <input type="hidden" name="<%=FileDirectorySelectionServlet.INITIAL_DIRECTORY_PARAMETER_NAME%>" value="<%=currentUserWorkDir%>">
    <input type="hidden" name="<%=FileDirectorySelectionServlet.RELATIVE_ROOT_DIR_PARAMETER_NAME%>" value="<%=currentUserHomeDir%>">
    <input type="hidden" name="<%=FileDirectorySelectionServlet.LIMIT_NAVIGATION_UNDER_RELATIVE_ROOT_PARAMETER_NAME%>" value="<%=FileDirectorySelectionServlet.LIMIT_NAVIATION_TO_ROOT_PARAMETER_VALUE%>">
    <%--xferpath parameters--%>
        <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="">
        <input type="hidden" name="<%=PacketConstants.FIRST_RECIEVE_EMAIL_FIELD_NAME%>" value="<%=packet.getFirstRecieveEmail()%>">
        <input type="hidden" name="<%=PacketConstants.SECOND_RECIEVE_EMAIL_FIELD_NAME%>" value="<%=packet.getSecondRecieveEmail()%>">
        <table cellspacing=0 cellpadding=0 width="70%" border=0>
            <!-- INTER ROWS SPACE -->
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <!-- /INTER ROWS SPACE -->
            <!-- FIELDS ROW -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.NAME_LABEL%>*</b><br>
                    <input type="text" name="<%=PacketConstants.NAME_FIELD_NAME%>" value="<%=packet.getName()%>" maxlength="128">
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.DESTINATION_PLANT_LABEL%>*</b><br>
                    <select name="<%=PacketConstants.DESTINATION_PLANT_FIELD_NAME%>" onChange="populateUsersListCombo()">
                        <%=HtmlUtils.buildComboOptionsListFromVectorOfHashtable(destinationPlants, currentSelectedPlant, VobManager.VOB_PLANT_ATTRIBUTE, VobManager.VOB_PLANT_ATTRIBUTE, true, "", "")%>
                    </select>
                </td>
            </tr>
            <!-- /FIELDS ROW -->
            <!-- INTER ROWS SPACE -->
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <!-- /INTER ROWS SPACE -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.TP_RELEASE_LABEL%></b><br>
                    <input type="text" name="<%=PacketConstants.TP_RELEASE_FIELD_NAME%>" value="<%=packet.getTpRelease()%>"
                           maxlength="5">
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.TP_REVISION_LABEL%></b><br>
                    <input type="text" name="<%=PacketConstants.TP_REVISION_FIELD_NAME%>" value="<%=packet.getTpRevision()%>"
                           maxlength="5">
                </td>
            </tr>
            <!-- INTER ROWS SPACE -->
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <!-- /INTER ROWS SPACE -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.FIRST_RECIEVE_LOGIN_LABEL%>*</b><br>
                    <select name="<%=PacketConstants.FIRST_RECIEVE_LOGIN_FIELD_NAME%>" onChange="populateFirstEmailAddress()">

                    </select>
                </td>
                <!-- COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <!-- /COLUMN SEPARATOR -->
                <td class=testo width="40%">
                    <b><%=PacketConstants.SECOND_RECIEVE_LOGIN_LABEL%></b><br>
                    <select name="<%=PacketConstants.SECOND_RECIEVE_LOGIN_FIELD_NAME%>" onChange="populateSecondEmailAddress()">

                    </select>
                </td>
            </tr>
            <!--/fields-->
            <!-- INTER ROWS SPACE -->
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <!-- /INTER ROWS SPACE -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="80%" colspan="4">
                    <b><%=PacketConstants.CC_EMAIL_LABEL%></b><nobr>(only STM internal addresses, more addresses comma separated)</nobr><br>
                    <input type="text" name="<%=PacketConstants.CC_EMAIL_FIELD_NAME%>" value="<%=packet.getCcEmail()%>"
                           SIZE="80" maxlength="256">
                </td>
            </tr>
            <!-- INTER ROWS SPACE -->
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
            <!-- /INTER ROWS SPACE -->
            <tr>
                <!-- LEFT COLUMN SEPARATOR -->
                <td>&nbsp;</td>
                <!-- /LEFT COLUMN SEPARATOR -->
                <td class=testo width="80%" colspan="4">
                    <b><%=PacketConstants.XFERPATH_LABEL%>*</b><br>
                    <%=GeneralStringUtils.replace(packet.getXferPath(), "\\\\", "\\")%>
                    <input type="HIDDEN" name="<%=PacketConstants.XFERPATH_FIELD_NAME%>" value="<%=packet.getXferPath()%>">
                </td>
            </tr>
        </table>
    </FORM>
</TD>
</TR>
<TR>
    <td>
        <!-- BUTTONS -->
        <table cellpadding="0" cellspacing="0" border="0" width="65%">
            <tr>
                <td>* Mandatory fields</td>
            </tr>
            <tr>
                <td><img src="<%=contextPath%>/img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
            </tr>
            <tr>
                <td><img src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td>
            </tr>
            <tr>
                <td align="right">
                    <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                        <TR>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                            <TD valign="center" align="right" background="<%=contextPath%>/img/btn_center.gif"
                                class="testo"><A CLASS="BUTTON" HREF="javascript:selectXferPath()">SELECT XFER FILE OR DIR</a></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                            <TD><img alt="" src="<%=contextPath%>/img/pix.gif" width="5" border="0"></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                            <TD valign="center" align="right" background="<%=contextPath%>/img/btn_center.gif"
                                class="testo"><A CLASS="BUTTON" HREF="javascript:sendPackage()">SEND</a></TD>
                            <TD valign="center" align="right"><IMG alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
                        </TR>
                    </TABLE>
                </td>
            </tr>
        </table>
        <!-- /BUTTONS -->
    </TD>
</TR>


<%@ include file="/bottom.jsp" %>
</BODY>
</HTML>
