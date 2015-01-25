<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*, tpms.*"
%>
<%
    Element userData=CtrlServlet.getUserData((String)session.getAttribute("user"));
    String repFileName=(String)request.getAttribute("repFileName");
    Document tpDoc=(Document)request.getAttribute("tpMultiDoc");
    NodeList tpRecs=tpDoc.getDocumentElement().getElementsByTagName("TP");
    String baseline=(String)request.getAttribute("baseline");

    reportSel repsel=(reportSel)session.getAttribute("repsel");
    slctLst dbUsrLst=null;
    slctLst dbPlant=null;

%>
  <%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
  <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
  <LINK href="default.css" type=text/css rel=stylesheet>
  <SCRIPT language=JavaScript>
   function submitAction()
   {
     if (checkNofSelItems()>0)return;
     document.tpActionForm.submit();
   }

   function checkNofSelItems()
  {
    cont=0;

    for (i=0; i< document.tpActionForm.elements.length; i++)
    {
	    elem = document.tpActionForm.elements[i].name;

        if (elem.search('JOB_REL') >0)
	   {
            if(document.tpActionForm.elements[i].value == ""){
                cont++;
                alert('JOB RELEASE IS A MANDATORY FIELD! ');
            }

	   }

       if (elem.search('JOB_REV') >0)
	   {

            if(document.tpActionForm.elements[i].value == ""){
                cont++;
                alert('JOB REVISION IS A MANDATORY FIELD! ');
            }

	   }
    }

    return cont;
  }

  </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="document.tpActionForm.elements['FIELD.JOBNAME'].focus()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP DELETE FORM"; %>
<%@ include file="top.jsp" %>
   <FORM name="tpActionForm" action="tpMultiActionServlet" method="post">
     <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
     <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_delete">
     <INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
     <INPUT TYPE="HIDDEN" NAME="baseline" VALUE="<%= baseline %>">

     <%
       for (int i=0; i<tpRecs.getLength(); i++)
       {
         Element tpRec=(Element)tpRecs.item(i);
         String toPlant = xmlRdr.getVal(tpRec,"TO_PLANT");
         String prod_login = (xmlRdr.getVal(tpRec,"PROD_LOGIN")!=null ? xmlRdr.getVal(tpRec,"PROD_LOGIN") : "");
         String valid_login =(xmlRdr.getVal(tpRec,"VALID_LOGIN")!=null ? xmlRdr.getVal(tpRec,"VALID_LOGIN") : "");
         String email_to = (xmlRdr.getVal(tpRec,"EMAIL_TO")!=null ? xmlRdr.getVal(tpRec,"EMAIL_TO") : "");
         //String certification= (xmlRdr.getVal(tpRec,"CERTIFICATION")!=null ? xmlRdr.getVal(tpRec,"CERTIFICATION") : "");
         String certification= "check";
         %>
             <TR>
              <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="560" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->&nbsp;TP DATA&nbsp;</b></td>
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

               <table cellspacing=0 cellpadding=0 width="70%" border=0 >
                <tbody>

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

                      <!-- FIELDS ROW: -->
                      <tr>

                        <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>Job name </b><br>
                         <%= xmlRdr.getVal(tpRec,"JOBNAME") %>
                         <INPUT type="HIDDEN" name="chk<%= xmlRdr.getVal(tpRec,"TP_LABEL") %>" value="Y">
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                         &nbsp;
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

                        <td class=testo width="40%">
                         <b>Job release</b><br>
                         <%= xmlRdr.getVal(tpRec,"JOB_REL") %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="40%">
                         <b>Job revision</b><br>
                         <%= xmlRdr.getVal(tpRec,"JOB_REV") %>
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

                       <td class=testo width="30%">
                          <b>Destination Plant</b><br>
                          <%=toPlant  %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                         <b>Product line</b><br>
                         <%= xmlRdr.getVal(tpRec,"LINE") %>
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
                        <td class=testo width="40%">
                         <b>Process stage</b><br>
                        <%= xmlRdr.getVal(tpRec,"FACILITY") %>
                        </td>

                        <!-- COLUMN SEPARATOR --><td>&nbsp;</td><td>&nbsp;</td><!-- /COLUMN SEPARATOR -->

                        <td class=testo width="30%">
                         <b>Tester info</b><br>
                         <%= xmlRdr.getVal(tpRec,"TESTER_INFO")%>
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
                        <td class=testo width="40%" valign="top" colspan="4">
                         <b>Xfer path </b><br>
                         <%= xmlRdr.getVal(tpRec,"XFER_PATH") %>
                        </td>

                        <!-- RIGHT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /RIGHT COLUMN SEPARATOR -->

                      </tr>
                      <!-- /FIELDS ROW -->

                      <!-- INTER ROWS SPACE -->
                      <tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      <!-- /INTER ROWS SPACE -->

		        </tbody>
               </table>
              </TD>
             </TR>

             <%
               if (i<tpRecs.getLength()-1)
               {%>
                 <!-- BLACK LINE -->
                 <TR>
                  <td>
                    <table cellpadding="0" cellspacing="0" border="0" width="65%">
                      <tr>
                        <td><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
                      </tr>
                      <tr>
                        <td colspan=5><img alt="" src="img/blank.gif"  border=0></td>
                      </tr>
                      <tr>
                        <td colspan=5><img alt="" src="img/blank.gif"  border=0></td>
                      </tr>
                    </table>
                  </td>
                 </TR>
                 <!-- /BLACK LINE -->
               <%}
             %>


       <%}
     %>
     </FORM>
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
                    <TD valign="center" align="right"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:history.back()">&nbsp;BACK</TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_right.gif"></TD>
		    <TD><img src="img/pix.gif" width="5" border="0"></TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_left.gif"></TD>
                    <TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitAction()">&nbsp;DELETE</TD>
                    <TD valign="center" align="right"><IMG SRC="img/btn_right.gif"></TD>
                   </TR>
                  </TABLE>
                 </td>
 		</tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>


<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
