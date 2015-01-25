<%@ page import="it.txt.general.utils.FileSystemNavigation"%>
<%@ page import="it.txt.afs.servlets.FileDirectorySelectionServlet"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>
<%@ page isErrorPage="false" errorPage="/uncaughtErr.jsp"%>
<%
    String contextPath = request.getContextPath();
    FileSystemNavigation fileSystemNavigation = (FileSystemNavigation) request.getAttribute(FileDirectorySelectionServlet.NAVIGATION_OBJECT_REQUEST_ATTRIBUTE_NAME);
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
    <script language="javascript">
        function browseTo(clickedDir) {
            var parentLinkValue = '<%=FileDirectorySelectionServlet.PARENT_LINK_VALUE%>';
            var filePathSeparator = '\<%=File.separator%>';
            var objForm = document.selectPath;

            if (clickedDir == parentLinkValue) {
                objForm.<%=FileDirectorySelectionServlet.INITIAL_DIRECTORY_PARAMETER_NAME%>.value = objForm.<%=FileDirectorySelectionServlet.INITIAL_DIRECTORY_PARAMETER_NAME%>.value + filePathSeparator + clickedDir;
            } else {
                document.selectPath.<%=FileDirectorySelectionServlet.INITIAL_DIRECTORY_PARAMETER_NAME%>.value = clickedDir;
            }
            document.selectPath.submit();
        }

        function selectElement(){
            var objForm = document.selectPath;
            var radioButtons = objForm.<%=FileDirectorySelectionServlet.USER_SELECTION_CONTROL_NAME%>;
            var elementSelected = false;
            if (radioButtons != null) {
                if (radioButtons.length == null)  {
                    if (radioButtons.value != null){
                        elementSelected = true;
                    }
                } else {
                    for (var i = 0; i < radioButtons.length; i++) {
                        if (radioButtons[i].checked){
                            elementSelected = true;
                        }
                    }
                }
            }

            if (elementSelected){
                submitSelectPathForm(objForm)
            } else {
                alert("You must select at least one file!");
            }
        }

        function submitSelectPathForm(objForm) {
            objForm.<%=AfsServletUtils.ACTION_FIELD_NAME%>.value = '<%=FileDirectorySelectionServlet.SELECT_ENTRY_ACTION_VALUE%>';
            objForm.submit();
            return;
        }

    </script>
</HEAD>
<LINK href="<%=contextPath%>/default.css" type=text/css rel=stylesheet>

<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle=""; %>
<%@ include file="/top.jsp" %>

<TR>
    <TD ALIGN="LEFT">
        <table cellpadding="0" cellspacing="0" border="0" >
            <tr>
                <td colspan="4">
                    <table cellpadding="0" cellspacing="0" border="0" width="760" >
                        <tr>
                            <td width="4"><img src="img/tit_sx_file.gif" width="4" height="21" alt="" border="0"></td>
                            <td background="img/tit_center_file.gif" align="center" class="titverdifl" nowrap><font color="#19559E" face="Courier"><%=new File(fileSystemNavigation.getCurrentDirectory()).getCanonicalPath()%></font></td>
                            <td width="4"><img src="img/tit_dx_file.gif" width="4" height="21" alt="" border="0"></td>
                            <td><img src="img/pix.gif" width="<%= 603+80-(fileSystemNavigation.getCurrentDirectory().length()*8) %>" height="18" alt="" border="0"></td>
                        </tr>
                        <tr>
                            <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                Please select an element in the following using the associated radio button (<input type="radio">) and clicking on 'OK' button
                <li>In order to navigate inside of a directory click on the directory name itself.</li>
                <%if (fileSystemNavigation.showParentLink()) {%>
                <li>In order to goto to the parent directory please click on the parent link ('<%=FileDirectorySelectionServlet.PARENT_LINK_VALUE%>')</li>
                <%}%>
                <br>
                <br>
            </tr>
        </table>
    </TD>
</TR>
<TR>
    <TD ALIGN="LEFT">
        <!--page content-->
        <table border="0" cellpadding="4" cellspacing="1">
            <!--intestazione-->
            <tr bgcolor="#c9e1fa">
                <td bgcolor="#ffffff"><img alt="" src="<%=contextPath%>/img/blank_flat.gif"></td>
                <td class="testoH"><b>Name</b></td>
                <td class="testoH"><b>Size [B]</b></td>
                <td class="testoH"><b>Last modified</b></td>
            </tr>
            <!--fine intestazione-->
<FORM name="selectPath" action="<%=contextPath%>/fileDirectorySelectionServlet" method="post">
    <input type="hidden" name="<%=AfsServletUtils.ACTION_FIELD_NAME%>" value="">
    <%=FileDirectorySelectionServlet.printAllRequestsAttributesAndParameters(request)%>
            <!--dettaglio -->
            <%if (fileSystemNavigation.showParentLink()) {%>
                <tr>
                    <td>
                        <img alt="" src="<%=contextPath%>/img/blank_flat.gif">
                    </td>
                    <td class="testo" bgcolor="#f2f2f2">
                        <img alt="" src="<%=contextPath%>/img/folder_icon.gif" border="0">
                        <img alt="" src="<%=contextPath%>/img/blank_flat_small.gif">
                        <a href="javascript:browseTo('<%=FileDirectorySelectionServlet.PARENT_LINK_VALUE%>')"><%=FileDirectorySelectionServlet.PARENT_LINK_VALUE%></a>
                    </td>
                    <td class="testo" align="right" bgcolor="#f2f2f2">
                        <img alt="" src="<%=contextPath%>/img/blank_flat.gif">
                    </td>
                    <td class="testo" bgcolor="#f2f2f2">

                    </td>
                </tr>
            <%}%>
            <%
                Vector subDirectoriesList = fileSystemNavigation.getSubDirectoriesList();
                if (subDirectoriesList != null) {
                    //Collections.sort(subDirectoriesList);
                    int countSubDirectories = subDirectoriesList.size();
                    boolean canSelectDirs = fileSystemNavigation.showDirectorySelection();
                    File currentDir;
                    for (int i = 0; i < countSubDirectories; i++) {
                        currentDir = (File) subDirectoriesList.get(i);

            %>
                <tr>
                    <td>
                        <%if (canSelectDirs) {%>
                            <input type="radio" name="<%=FileDirectorySelectionServlet.USER_SELECTION_CONTROL_NAME%>" value="<%=GeneralStringUtils.replaceAllIgnoreCase(currentDir.getCanonicalPath(), "\\", "\\\\")%>">
                        <%} else {%>
                            <img alt="" src="<%=contextPath%>/img/blank_flat.gif">
                        <%}%>
                    </td>
                    <td class="testo" bgcolor="#f2f2f2">
                        <img alt="" src="<%=contextPath%>/img/folder_icon.gif" border="0">
                        <img alt="" src="<%=contextPath%>/img/blank_flat_small.gif">
                        <%--=GeneralStringUtils.replaceAllIgnoreCase(currentDir.getAbsolutePath(), "\\", "\\\\")--%>
                        <a href="javascript:browseTo('<%=GeneralStringUtils.replaceAllIgnoreCase(currentDir.getCanonicalPath(), "\\", "\\\\")%>')"><%=currentDir.getName()%></a>
                    </td>
                    <td class="testo" align="right" bgcolor="#f2f2f2">
                        <img alt="" src="<%=contextPath%>/img/blank_flat.gif">&nbsp;
                    </td>
                    <td class="testo" bgcolor="#f2f2f2" align="left">
                        <nobr><%=FileDirectorySelectionServlet.formatDate(new Date(currentDir.lastModified()))%></nobr>
                    </td>
                </tr>
            <%        }
                }
            %>
            <%
                Vector subFileList = fileSystemNavigation.getSubFilesList();
                if (subFileList != null) {
                    //Collections.sort(subFileList);
                    int countSubFiles = subFileList.size();
                    boolean canSelectFiles = fileSystemNavigation.showFileSelection();
                    File currentFile;
                    for (int i = 0; i < countSubFiles; i++) {
                        currentFile = (File) subFileList.get(i);
            %>
                <tr>
                    <td>
                        <%if (canSelectFiles) {%>
                            <%--=GeneralStringUtils.replaceAllIgnoreCase(currentFile.getAbsolutePath(), "\\", "\\\\")--%>
                            <input type="radio" name="<%=FileDirectorySelectionServlet.USER_SELECTION_CONTROL_NAME%>" value="<%=GeneralStringUtils.replaceAllIgnoreCase(currentFile.getCanonicalPath(), "\\", "\\\\")%>">
                        <%} else {%>
                            <img alt="" src="<%=contextPath%>/img/blank_flat.gif">
                        <%}%>
                    </td>
                    <td class="testo" bgcolor="#f2f2f2">
                        <img alt="" src="<%=contextPath%>/img/folder_icon_blank.gif" border="0">
                        <img alt="" src="<%=contextPath%>/img/blank_flat_small.gif"><%=currentFile.getName()%>
                    </td>
                    <td class="testo" align="right" bgcolor="#f2f2f2">
                        <img alt="" src="<%=contextPath%>/img/blank_flat.gif"><%=currentFile.length()%>
                    </td>
                    <td class="testo" bgcolor="#f2f2f2" align="left">
                        <%=FileDirectorySelectionServlet.formatDate(new Date(currentFile.lastModified()))%>
                    </td>
                </tr>
            <%        }
                }
            %>
    </form>
            <!--fine dettaglio -->
        </table>

    </TD>
</TR>
<tr>
  <td colspan="4"><img src="<%=contextPath%>/img/pix_nero.gif" width="100%" height="1" alt="" border="0"></td>
</tr>
<TR>
    <td colspan=4>
<!-- BUTTONS -->
        <table width="100%">
            <tr>
                <td align="right" >
                    <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
                        <TR>
                           <TD valign="center"><img alt="" src="<%=contextPath%>/img/btn_left.gif"></TD>
                           <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:selectElement()">&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;</a></TD>
                           <TD valign="center" ><img alt="" src="<%=contextPath%>/img/btn_right.gif"></TD>
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
