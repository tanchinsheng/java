
<%@ page import="it.txt.afs.security.AfsSecurityManager"%>
<%@ page import="tpms.VobManager"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>

<%
  //accntRec=(Element)request.getAttribute("accntRec");
  String param;
  String value;
  boolean isNewUser = (request.getAttribute("isNewUser")).equals("Y");
  Vector dVobs = VobManager.getDvobList();
  Vector tVobs = VobManager.getTVobsInfo();

  Vector rVobs = VobManager.getRvobList();
  Vector afsRVobs = VobManager.getRVobsInfo();

  String contextPath = request.getContextPath();
  Vector divisions=VobManager.getDivisionList();
  Element  accntRec = (Element) request.getAttribute("accntRec");
  String currentUserDevelopVobName = "";
  String currentUserReceiveVobName = "";

  String divisionEdit = "Y";
  if (accntRec != null) {
      Element divisionElement = XmlUtils.getChild(accntRec,"DIVISION");
      if (divisionElement != null)
        divisionEdit = divisionElement.getAttribute("edit");

      if (GeneralStringUtils.isEmptyString(divisionEdit))
          divisionEdit = "";
  }
	if (!isNewUser) {
		currentUserDevelopVobName = XmlUtils.getVal(accntRec,"DVL_VOB");
		currentUserReceiveVobName = XmlUtils.getVal(accntRec,"REC_VOB");
        if (currentUserReceiveVobName == null)
            currentUserReceiveVobName = "";
        if (currentUserDevelopVobName == null)
            currentUserDevelopVobName = "";
    }

  String vobDesc;
  String vobName;
  Hashtable oneVobData;
  int vobCount ;
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <LINK href="default.css" type="text/css" rel=stylesheet>
 <script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
 <SCRIPT type="text/javascript" language=JavaScript>
 	var currentUserDevelopVobName = "<%=currentUserDevelopVobName%>";
 	var currentUserReceiveVobName = "<%=currentUserReceiveVobName%>"
   function chngRole(role)
   {
     setWorkModes(role);
     setDfltDvlVobs(role);
     setDfltRecVobs(role);
   }

   function setWorkModes(role){
     var box = this.document.userProfileForm.elements['FIELD.WORK_MODE'];
     while (box.options.length > 0) box.options[0] = null;
     
     if(role=='CONTROLLER'){
     	box.options[0] = new Option('Check mode','LOCREP');
     	box.options[1] = new Option('Global report mode','GLOBREP');
     }else if (role=='QUERY_USER'){
       box.options[0] = new Option('Global report mode','GLOBREP');
       box.selectedIndex=0;
     }else if (role == "<%=AfsSecurityManager.AIDED_FTP_SERVICE_ROLE%>"){
         box.options[0] = new Option('AIDED FTP MODE','AIDED_FTP');
     }else{
       box.options[0] = new Option('Send work mode','SENDWORK');
       box.options[1] = new Option('Receive work mode','RECWORK');
       box.options[2] = new Option('Check mode','LOCREP');
       box.options[3] = new Option('Global report mode','GLOBREP');
       
       /*
       if ((role=='ADMIN')||(role=='CONTROLLER')){
         box.selectedIndex=2;
       }
       else box.selectedIndex=0;
       */
       
       if ((role=='ADMIN')){
       		box.selectedIndex=2;
       }else{ 
       		box.selectedIndex=0;
	   }
     }
   }

   function setDfltDvlVobs(role)
   {
     var box = this.document.userProfileForm.elements['FIELD.DVL_VOB'];
     while (box.options.length > 0) box.options[0] = null;
     if (role=='QUERY_USER')
     {
       box.options[0] = new Option('','');
     }  else if (role == '<%=AfsSecurityManager.AIDED_FTP_SERVICE_ROLE%>') {
       <%
       vobCount = tVobs.size();
       int selectedIndex = -1;
       for (int i = 0; i < vobCount; i++)
       {
               oneVobData = (Hashtable) tVobs.elementAt(i);
               vobName = (String) oneVobData.get(VobManager.VOB_NAME_ATTRIBUTE);
               vobDesc = (String) oneVobData.get(VobManager.VOB_DESC_ATTRIBUTE);

             %>
             box.options[<%= i+1 %>] = new Option('<%= vobDesc %>','<%= vobName %>');
       <%
             	if (currentUserDevelopVobName.equals(vobName)){%>
             	document.userProfileForm.elements['FIELD.DVL_VOB'].selectedIndex = <%= i+1 %>;
             	<%}

       }
       %>


     }
     else
     {
       <%
           for (int i=0; i<dVobs.size(); i++)
           {%>
             <%
               vobName = XmlUtils.getVal((Element)dVobs.elementAt(i),"NAME");
               vobDesc = XmlUtils.getVal((Element)dVobs.elementAt(i),"DESC");
             %>
             box.options[<%= i+1 %>] = new Option('<%= vobDesc %>','<%= vobName %>');
           <%}
       %>
     }
     box.selectedIndex=0;
   }

   function setDfltRecVobs(role)
   {

     var box = this.document.userProfileForm.elements['FIELD.REC_VOB'];
     while (box.options.length > 0) box.options[0] = null;
     if (role=='QUERY_USER')
     {
       box.options[0] = new Option('','');
     }  else if (role == '<%=AfsSecurityManager.AIDED_FTP_SERVICE_ROLE%>') {
       <%
       vobCount = afsRVobs.size();
       for (int i = 0; i < vobCount; i++)
       {
               oneVobData = (Hashtable) afsRVobs.elementAt(i);
               vobName = (String) oneVobData.get(VobManager.VOB_NAME_ATTRIBUTE);
               vobDesc = (String) oneVobData.get(VobManager.VOB_DESC_ATTRIBUTE);
             %>
             box.options[<%= i+1 %>] = new Option('<%= vobDesc %>','<%= vobName %>');
           <%}
       %>

     } else
     {
       <%
           for (int i=0; i<rVobs.size(); i++)
           {%>
             <%
               vobName = XmlUtils.getVal((Element)rVobs.elementAt(i),"NAME");
               vobDesc = XmlUtils.getVal((Element)rVobs.elementAt(i),"DESC");
             %>
             box.options[<%= i+1 %>] = new Option('<%= vobDesc %>','<%= vobName %>');
           <%}
       %>
     }
     box.selectedIndex=0;
   }

   function submitForm()
   {
     if (!controlData()) return;
     document.userProfileForm.submit();
   }

   function controlData() {
    /*
***
Tpms login (Unix login)*
Password
Confirm passwd
First name
Surname
Unix group *
UNIX home directory (*)
Receive Default directory (*)
    */

     var objForm = document.userProfileForm;

     if (objForm.elements['FIELD.<%= UserUtils.LDAP_LOGIN_ELEMENT_TAG %>'] != null && !isEmptyString(objForm.elements['FIELD.<%= UserUtils.LDAP_LOGIN_ELEMENT_TAG %>'].value)){
         if (filterUserInputAphaNumericalCharsAndSpace(objForm.elements['FIELD.<%= UserUtils.LDAP_LOGIN_ELEMENT_TAG %>']) >= 0) {
             alert("LDAP Login contains invalid characters:\nOnly alphanumeric chars ('A..Z' and '0..9'), underscore ('_')and space (' ') are allowed");
             objForm.elements['FIELD.<%= UserUtils.LDAP_LOGIN_ELEMENT_TAG %>'].focus();
             return false;
         }
     }

     if (!isEmptyString(objForm.elements['FIELD.EMAIL'].value)){
         if (validateEmailAddress(objForm.elements['FIELD.EMAIL']) >= 0) {
             alert("The value " + objForm.elements['FIELD.EMAIL'].value + " is not a valid email address!\nCorrect it!");
             objForm.elements['FIELD.EMAIL'].focus();
             return false;
         }
     } else {
         alert("Email is mandatory!");
         objForm.elements['FIELD.EMAIL'].focus();
         return false;
     }

     if (objForm.elements['FIELD.UNIX_GROUP'] != null){
         if (!isEmptyString(objForm.elements['FIELD.UNIX_GROUP'].value)){
             if (filterUserInputAphaNumericalChars(objForm.elements['FIELD.UNIX_GROUP']) >= 0) {
                 alert("Unix group contains invalid chars:\nOnly alphanumeric chars ('A..Z' and '0..9') and underscore ('_') are allowed");
                 objForm.elements['FIELD.UNIX_GROUP'].focus();
                 return false;
             }
         } else {
             alert("Unix group is mandatory!");
             objForm.elements['FIELD.UNIX_GROUP'].focus();
             return false;
         }
     }

      var division = '';
      <%
      if (divisionEdit.equals("S"))
      {%>
            var ind1=document.userProfileForm.elements['FIELD.DIVISION'].selectedIndex;
            if (ind1>=0)
            {
                division =document.userProfileForm.elements['FIELD.DIVISION'].options[ind1].text;
            }
            else
            {
                division='';
            }
      <%}
      else {%>
            if (document.userProfileForm.elements['FIELD.DIVISION'] == null)
            {

                division = 'null';
            }else{

                division = document.userProfileForm.elements['FIELD.DIVISION'].value;
            }
     <%}%>
     var home_dir = '';
     if (document.userProfileForm.elements['FIELD.HOME_DIR'] == null)
     {

        home_dir = 'null';
     }else{
        home_dir = document.userProfileForm.elements['FIELD.HOME_DIR'].value;
     }

     var work_dir = '';
     if (document.userProfileForm.elements['FIELD.WORK_DIR'] == null)
     {

        work_dir = 'null';
     }else{
        work_dir = document.userProfileForm.elements['FIELD.WORK_DIR'].value;
     }

     for (i=0; i< document.userProfileForm.elements.length; i++)
       {

         if (document.userProfileForm.elements[i].type == 'radio')
	     {
            var role;
            if (document.userProfileForm.elements[i].checked)
            {
                role = document.userProfileForm.elements[i].value;
            }
	     }else{
             if (document.userProfileForm.elements['FIELD.ROLE'] == null)
             {

                role = 'null';

             }else{
                role = document.userProfileForm.elements['FIELD.ROLE'].value;
             }
         }
       }

     <%
     if (isNewUser)
     {%>

       var tpms_login = document.userProfileForm.elements['FIELD.UNIX_USER'].value;
       var pwd = document.userProfileForm.elements['FIELD.PWD'].value;
       var pwd_confirm = document.userProfileForm.elements['FIELD.PWD_CONFIRM'].value;

       if( tpms_login == '')
       {
            alert('TPMS/W Login IS A MANDATORY FIELD! ');
            return false;
       }

       if (!isEmptyString(pwd) || !isEmptyString(pwd_confirm))  {
           if (isEmptyString(pwd)) {
               alert('If you want to insert password both (Password and Pwd confirm) must be inserted');
               document.userProfileForm.elements['FIELD.PWD_CONFIRM'].focus();
               return false;
           }
           if (isEmptyString(pwd_confirm)) {
               alert('If you want to insert password both (Password and Pwd confirm) must be inserted');
               document.userProfileForm.elements['FIELD.PWD'].focus();
               return false;
           }

           if( pwd != pwd_confirm)
           {
                alert('Password must be equal to Pwd Confirm!');
                return false;
           }
       }


       if ((role=='ENGINEER') || (role=='CONTROLLER'))
        {
            var home_dir = document.userProfileForm.elements['FIELD.HOME_DIR'].value;
            if( home_dir == '')
            {
                alert('Unix Home Dir IS A MANDATORY FIELD! ');
                return false;
            }
            var work_dir = document.userProfileForm.elements['FIELD.WORK_DIR'].value;
            if( work_dir == '')
            {
                alert('Receive Dir IS A MANDATORY FIELD! ');
                return false;
            }
            if( tpms_login != unix_login)
            {
                alert('The TPMS Login must be equal to the Unix Login!');
                return false;
            }
        }
     <%
     }
     %>
     if( division == '')
     {
        alert('Unix group IS A MANDATORY FIELD! ');
        return false;
     }

     if ((role=='ENGINEER') || (role=='CONTROLLER'))
     {

        if( unix_group == '')
        {
            alert('Unix Group IS A MANDATORY FIELD! ');
            return false;
        }
        if( home_dir == '')
        {
            alert('Unix Home Dir IS A MANDATORY FIELD! ');
            return false;
        }

        if( work_dir == '')
        {
            alert('Receive Dir IS A MANDATORY FIELD! ');
            return false;
        }
     }
     return true;
   }
   //end

 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle=(isNewUser ? "USER PROFILE INSERTION PAGE" : "USER PROFILE DATA MODIFICATION PAGE"); %>
<%@ include file="top.jsp" %>

             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><%= (isNewUser ? "&nbsp;NEW&nbsp; USER&nbsp;" : "USER PROFILE DATA") %></b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="453" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4">Password field is needed if you want to create a user that will not use the Ldap authentication.<br>In this case leave LDAP Login field blank and kindly fill Password and Confirm passwd fields.</td>
                </tr>
              </table>
	      </TD>
	     </TR>

             <TR>
              <TD ALIGN="LEFT">

               <!-- FORM -->

	           <FORM name="userProfileForm" action="<%=contextPath%>/userProfileServlet" method="post">
		           <INPUT TYPE="HIDDEN" NAME="accntName" VALUE="<%= (isNewUser ? "" : XmlUtils.getVal(accntRec,"NAME")) %>">
                   <INPUT TYPE="HIDDEN" NAME="action" VALUE="save">
                   <INPUT TYPE="HIDDEN" NAME="isNewUser" VALUE="<%= (isNewUser ? "Y" : "N") %>">
                    <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                      <tbody>


                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
                        <%
                            if (isNewUser){
                               value = "";
                            } else {
                                value = XmlUtils.getVal(accntRec, UserUtils.LDAP_LOGIN_ELEMENT_TAG);
                                if (value == null) value = "";
                            }
                        %>
                        <td class=testo width="40%">
                         <b>LDAP Login</b><br>
                         <%if (isNewUser) {%>
                            <input class="txt" maxlength="100" size="10" name="FIELD.<%= UserUtils.LDAP_LOGIN_ELEMENT_TAG %>" value="<%= value %>">
                         <%} else {%>
                            <%=value%>
                         <%}%>
                       </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

			            <% param="UNIX_USER"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%">
                         <b><nobr>Tpms login (Unix login)*</nobr></b><br>
                         <%
                          if (isNewUser)
                          {%>
                            <input class="txt" maxlength="20" size="10" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			  else {%><%= value %><%}
			 %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

		   <% if (isNewUser)
                      {%>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

			<% param="PWD"; %>
                        <td class=testo width="40%">
                         <b>Password</b><br>
                         <input type="PASSWORD" class="txt" maxlength="100" size="10" name="FIELD.<%= param %>" value="">
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

			<% param="PWD"; %>
                        <td class=testo width="40%">
                         <b>Confirm passwd</b><br>
			  <input type="PASSWORD" class="txt" maxlength="100" size="10" name="FIELD.<%= param %>_CONFIRM" value="">
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->
		      <%}
		   %>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

			<% param="FIRST_NAME"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%">
                         <b>First name</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            <input class="txt" maxlength="20" size="10" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			  else {%><%= value %><%}
			 %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

			<% param="SURNAME"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%">
                         <b>Surname</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            <input class="txt" maxlength="20" size="10" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			  else {%><%= value %><%}
			 %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

			<% param="EMAIL"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%">
                         <b>Email (*)</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            <input class="txt" maxlength="128" size="15" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			  else {%><%= value %><%}
			 %>
                        </td>
                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->
                        <% param="DIVISION"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Unix group *</b><br>
                         <%
                          if ((isNewUser) || (divisionEdit.equals("Y")))
                          {%>
                                    <input class="txt" maxlength="40" size="10" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			               else if(divisionEdit.equals("S"))
                           {%>
                                    <select class="tendina" maxlength="100" size="1" name="FIELD.<%= param %>">
                                        <%
                                            for (int i=0; i<divisions.size(); i++)
                                        {%>
                                            <option <%= (((String)divisions.elementAt(i)).equals(value) ? "selected" : "") %> ><%= divisions.elementAt(i) %>
                                        <%}%>
                                    </select>
                          <%}
                           else{%><%= value %><%}
			              %>
                        </td>
                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

			<% param="HOME_DIR"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>UNIX home directory (*)</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			  else {%><%= value %><%}
			 %>
                        </td>

                       <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="40%">
			  &nbsp;
			</td>

		      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

			<% param="WORK_DIR"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Receive Default directory (*)</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            &nbsp;<input class="txtlong" maxlength="100" size="40" name="FIELD.<%= param %>" value="<%= (value!=null ? value : "") %>">
                          <%}
			  else {%><%= value %><%}
			 %>
                        </td>

                       <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="40%">
			  &nbsp;
			</td>

		      </tr>

            <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
  <%--                    <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                      <td class=testo width="40%"

                       <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="40%">
			  &nbsp;
			</td>--%>

		      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                      <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
			            <% param="ROLE"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%" valign="top">
                         <b>Role</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                            <input type="RADIO" name="FIELD.<%= param %>" value="ADMIN" <%= (value.equals("ADMIN") ? "CHECKED" : "") %> onClick="chngRole('ADMIN')">ADMIN<br>
                            <input type="RADIO" name="FIELD.<%= param %>" value="ENGINEER" <%= (value.equals("ENGINEER")||value.equals("") ? "CHECKED" : "") %> onClick="chngRole('ENGINEER')">ENGINEER<br>
                            <input type="RADIO" name="FIELD.<%= param %>" value="CONTROLLER" <%= (value.equals("CONTROLLER") ? "CHECKED" : "") %> onClick="chngRole('CONTROLLER')">CONTROLLER<br>
                            <input type="RADIO" name="FIELD.<%= param %>" value="QUERY_USER" <%= (value.equals("QUERY_USER") ? "CHECKED" : "") %> onClick="chngRole('QUERY_USER')">QUERY USER<br>
                            <input type="RADIO" name="FIELD.<%= param %>" value="<%=AfsSecurityManager.AIDED_FTP_SERVICE_ROLE%>" <%= (value.equals(AfsSecurityManager.AIDED_FTP_SERVICE_ROLE) ? "CHECKED" : "") %> onClick="chngRole('AIDED_FTP')">AIDED FTP<br>
                          <%}
                          else {%><%= value %><%}
			             %>
                        </td>


                     <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

			            <% param="WORK_MODE"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <td class=testo width="40%" valign="top">
                         <b>Default mode *</b><br>
                         <% if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                            { %>
                          <select class="tendina" maxlength="100" size="1" name="FIELD.<%= param %>">
							<% if ((isNewUser)||(!XmlUtils.getVal(accntRec,"ROLE").equals("QUERY_USER"))){ %>
                               <option value="SENDWORK" <%= (value.equals("SENDWORK") ? "selected" : "") %>>Send work mode
                               <option value="RECWORK" <%= (value.equals("RECWORK") ? "selected" : "") %>>Receive work mode
                               <option value="LOCREP" <%= (value.equals("LOCREP") ? "selected" : "") %>>Check mode
                            <%}
                           %>
                           <option value="GLOBREP" <%=  (value.equals("GLOBREP") ? "selected" : "") %>>Global report mode
                          </select>
                          <%}
                            else {%><%= value %><%}
			             %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
			            <% param="DVL_VOB"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <% value=(value!=null ? value : ""); %>
                        <td class=testo width="40%" valign="top">
                         <b>Default develop vob</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                          <select class="tendina"  size="1" name="FIELD.<%= param %>">
                          <option value=""></option>
                          <%
                             if ((isNewUser)||(!XmlUtils.getVal(accntRec,"ROLE").equals("QUERY_USER")))
                             {
                                  for (int i=0; i < dVobs.size(); i++)
                                  {%>
                                   <%
                                            vobName=XmlUtils.getVal((Element)dVobs.elementAt(i),"NAME");
                                            vobDesc=XmlUtils.getVal((Element)dVobs.elementAt(i),"DESC");
                                   %>
                                   <option value="<%= vobName %>" <%= (currentUserDevelopVobName.equals(vobName) ? "selected" : "") %>><%= vobDesc %></option>
                                  <%}
                             }
                          %>
                          </select>
                         <%}
                         else {%><%= value %><%}
			             %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

			            <% param="REC_VOB"; %>
                        <% value=(isNewUser ? "" : XmlUtils.getVal(accntRec,param)); %>
                        <% value=(value!=null ? value : ""); %>
                        <td class=testo width="40%" valign="top">
                         <b>Default reception vob</b><br>
                         <%
                          if ((isNewUser)||(XmlUtils.getChild(accntRec,param).getAttribute("edit").equals("Y")))
                          {%>
                          <select class="tendina" size="1" name="FIELD.<%= param %>">
                          <option value=""></option>
                          <%
                              if ((isNewUser)||(!XmlUtils.getVal(accntRec,"ROLE").equals("QUERY_USER")))
                              {
                                   for (int i = 0; i < rVobs.size(); i++)
                                   {%>
                                   <%
                                             vobName = XmlUtils.getVal((Element)rVobs.elementAt(i),"NAME");
                                             vobDesc = XmlUtils.getVal((Element)rVobs.elementAt(i),"DESC");
                                   %>
                                   <option value="<%= vobName %>" <%= (currentUserReceiveVobName.equals(vobName) ? "selected" : "") %>><%= vobDesc %></option>
                                  <%}
                              }
                          %>
                          </select>
                         <%}
                         else {%><%= value %><%}
			             %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->
                      </tr>
                      <!-- /FIELDS ROW -->


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
                    <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitForm()">&nbsp;SUBMIT</a></TD>
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
		            (*) = Mandatory field for some user roles only<br>
                    </i>
		  </td>
                </tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>


<%@ include file="bottom.jsp" %>
<%if (!isNewUser) {%>
<script language="javascript">
    chngRole('<%=XmlUtils.getVal(accntRec,"ROLE")%>')
</script>
<%}%>
</BODY>
</HTML>
