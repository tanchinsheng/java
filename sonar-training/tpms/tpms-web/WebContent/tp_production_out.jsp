<%@ page import="java.util.*, org.w3c.dom.*, java.io.*, java.net.*, tol.*"

%>
<%String contextPath = request.getContextPath();%>
<%
    String time = new Long(System.currentTimeMillis()).toString();
    String reqId=request.getParameterValues("reqId")[0];
    String refreshTime=(String)request.getParameterValues("refreshTime")[0];
    boolean startBool=((Boolean)session.getValue("startBool"+"_"+reqId)).booleanValue();
    String webAppDir=getServletContext().getInitParameter("webAppDir");
    Document tpDoc=(Document)session.getAttribute("tpMultiDoc2");
    NodeList tpRecs=tpDoc.getDocumentElement().getElementsByTagName("TP");
    //if ((!startBool)&&(session.getValue("exception"+"_"+reqId)!=null)) throw (Exception)session.getValue("exception"+"_"+reqId);
    String xslFileName=webAppDir+"/"+"xsl"+"/"+"tp_production_out.xsl";
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


    String repFileName=xmlRdr.getVal(curTpRec,"repFileName");
    String actionTxt="PROCESSING TP #<B>"+new Integer(i+1).toString()+"</B> OF "+new Integer(tpRecs.getLength()).toString();
%>
<HTML>
<HEAD>
 <TITLE>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %><%= tpDoc.getDocumentElement().getNodeName() %></TITLE>
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
          alert('<%= failedTps.size() %> TP PRODUCTION HAVE FAILED');
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



 </SCRIPT>
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="start()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP PRODUCTION "+(startBool ? "IN PROGRESS" : "RESULT"); %>
<%@ include file="top.jsp" %>
<%
   if (startBool)
   {%>
     <%@ include file="waitmsg.jsp" %>
   <%}
   else
   {%>
         <FORM name="tpActionForm" action="tpMultiActionServlet" method="post">
         <TR>
          <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->TP PRODUCTIONS FULFILLED</b></td>
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
       if (failedTps!=null)
       {%>
         <TR>
          <TD ALIGN="LEFT">
              <table cellpadding="0" cellspacing="0" border="0" width="760" >
                <tr>
                  <td width="4"><img src="img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
                  <td background="img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE--><FONT COLOR="#87028E">TP PRODUCTIONS FAILED</FONT></b></td>
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
