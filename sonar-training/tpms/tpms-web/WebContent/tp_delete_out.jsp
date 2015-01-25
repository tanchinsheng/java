<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"
    isErrorPage="false"
    errorPage="uncaughtErr.jsp"
%>
<%
    String time = new Long(System.currentTimeMillis()).toString();
    String reqId=request.getParameterValues("reqId")[0];
    String xmlFileName=request.getParameterValues("xmlFileName")[0];
    String refreshTime=(String)request.getParameterValues("refreshTime")[0];
    boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
    String webAppDir=getServletContext().getInitParameter("webAppDir");
    Document tpDoc=(Document)session.getAttribute("tpMultiDoc");
    NodeList tpRecs=tpDoc.getDocumentElement().getElementsByTagName("TP");
    //if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
    String xslFileName=webAppDir+"/"+"xsl"+"/"+"tp_delete_out.xsl";
    Vector failedTps=null;
    Element curTpRec=(Element)tpRecs.item(0);
    int i;
    //get i-element not error for define the i-repFileName with percentual
    for (i=0; i<tpRecs.getLength(); i++)
    {
      Element el=(Element)tpRecs.item(i);
      if (xmlRdr.getChild(el,"ERROR")==null)
      {
          curTpRec=el;
          break;
      }
    }
    //get i-element not error and count the Tp with From_plant=To_plant
    //if count >0 --> view put in production botton

    int k;
    int countCheck=0;
    String toplant ="";
    String fromplant="" ;
    /*for (k=0; k<tpRecs.getLength(); k++)
    {
      Element elem=(Element)tpRecs.item(k);
      toplant = xmlRdr.getVal(elem,"TO_PLANT");
      fromplant = xmlRdr.getVal(elem,"FROM_PLANT");
      if (fromplant.equals(toplant)){
         countCheck++;
      }

    }
    */
    String repFileName=xmlRdr.getVal(curTpRec,"repFileName");
    String actionTxt="PROCESSING TP #<B>"+new Integer(i+1).toString()+"</B> OF "+new Integer(tpRecs.getLength()).toString();
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER")+xmlFileName%></TITLE>
 <LINK href="default.css" type=text/css rel=stylesheet>
 <SCRIPT language=JavaScript>
  function start()
  {
   <%
      if (startBool)
      {%>
        var delay = setTimeout("location.reload()",<%= refreshTime %>*1000);
      <%}
      else
      {
        if ((failedTps=xmlRdr.findEls(tpRecs,"ERROR","Y"))!=null)
        {%>
          alert('<%= failedTps.size() %> TP DELETATION HAVE FAILED');
        <%}
      }
   %>
  }

  function viewErrDett(index)
  {
    var actionTxt=document.tpActionForm.elements['err_action_txt_'+index].value;
    var msgTxt=document.tpActionForm.elements['err_msg_txt_'+index].value;
    var detailTxt=document.tpActionForm.elements['err_detail_txt_'+index].value;
    var sysDetTxt=document.tpActionForm.elements['err_sys_det_txt_'+index].value;
    window.open("err_det_view.jsp?actionTxt="+actionTxt+"&msgTxt="+msgTxt+"&detailTxt="+detailTxt+"&sysDetailTxt="+sysDetTxt,"ERROR_DETAILS","width=680,height=420,resizable=yes,scrollbars=no,status=0,location=no,menubar=no");
  }

  function submitTpMultiAction()
  {
    if(checkNofSelItems() == 0)
    {
        return;
    }
    else
    {
        document.tpActionForm.submit();
    }
  }
  function checkNofSelItems()
  {
    cont=0;

    for (i=0; i< document.tpActionForm.elements.length; i++)
    {
	   if (document.tpActionForm.elements[i].type=='checkbox')
	   {
          if (document.tpActionForm.elements[i].checked) cont++;
	   }
    }
    if (cont==0)
    {
      alert('AT LEAST ONE TP MUST BE SELECTED!');
    }

    return cont;
  }
 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP DELETE "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>
<%
   if (startBool)
   {%>
     <%@ include file="waitmsg.jsp" %>
   <%}
   else
   {%>
       <FORM name="tpActionForm" action="tpMultiActionServlet" method="post">
         <INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y">
         <INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_toprod">
         <INPUT TYPE="HIDDEN" NAME="baseline" VALUE="">
         <TR>
          <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->TP DELETE FULFILLED</b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="530" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

         <TR>
          <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
             Properties xslProps = new Properties();
             xslProps.setProperty("filter","OK");
             xmlRdr.applyXSL(tpDoc,xslFileName, xslProps, out);
           %>
	       <!-- /REPORT -->
           </BR>
          </TD>
         </TR>
         <%
    if (countCheck > 0)
    {%>
         <TR>
    	      <TD class="testo">
              <br>
             <!-- BUTTONS -->
              <table cellpadding="0" cellspacing="0" border="0" width="65%">
                <tr>
                  <td><img src="img/pix_nero.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
                <tr>
                  <td><img src="img/blank_flat.gif" alt="" border="0"></td>
                </tr>
		        <tr>
                 <td align="right">
                 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		     <TR>
		          <!-- PUT IN Production -->
		              <TD valign="center" ><img src="img/pix.gif" width="5" border="0"></TD>
                      <TD valign="center"><IMG SRC="img/btn_left.gif"></TD>
                      <TD valign="center" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitTpMultiAction()">&nbsp;PUT IN PRODUCTION</TD>
                      <TD valign="center" ><IMG SRC="img/btn_right.gif"></TD>
	             </TR>
                 </TABLE>
                 </td>
 		        </tr>
              </table>
              <!-- /BUTTONS -->
              </TD>
             </TR>

          <%}
          else
          {%>

          <%}
    %>
   <%
       if (failedTps!=null)
       {%>
         <TR>
          <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><FONT COLOR="#87028E">TP DELETE FAILED</FONT></b></td>
                  <td width="4"><img src="img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
                  <td><img src="img/pix.gif" width="570" height="18" alt="" border="0"></td>
                </tr>
                <tr>
                  <td colspan="4"><img src="img/pix_grey.gif" width="760" height="1" alt="" border="0"></td>
                </tr>
              </table>
	      </TD>
	     </TR>

         <TR>
          <TD ALIGN="LEFT">
	       <!-- REPORT -->
   	       <%
            xslProps = new Properties();
            xslProps.setProperty("filter","FAILED");
            xmlRdr.applyXSL(tpDoc,xslFileName, xslProps, out);
           %>
	       <!-- /REPORT -->
          </TD>
         </TR>
       </FORM>
    <%}
   }
%>
<%@ include file="bottom.jsp" %>
</BODY>
</HTML>
