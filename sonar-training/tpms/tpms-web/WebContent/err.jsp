<%
 if (session.getAttribute("Authenticated")==null)
 {
   actionTxt="TPMS SESSION EXPIRED";
   msgTxt="RETURN TO THE LOG IN PAGE";
   backBut = false;
 }
 else 
 if (exc instanceof IOException)
 {
   actionTxt="I/O ERROR";
   msgTxt=(exc.getMessage()!=null ? exc.getMessage() : "");
 } 
 else
 if (exc instanceof TpmsException) 
 {
   actionTxt=(((TpmsException)exc).getAction()!=null ? ((TpmsException)exc).getAction() : "ACTION ABORTED");
   msgTxt=(((TpmsException)exc).getMessage()!=null ? ((TpmsException)exc).getMessage() : "");
   detailTxt=(((TpmsException)exc).getDetail()!=null ? ((TpmsException)exc).getDetail() : "");
   sysDetailTxt=(((TpmsException)exc).getSysDetail()!=null ? ((TpmsException)exc).getSysDetail() : "");
 }
%>	

<html>
<head>
 <title>TPMS/W <%= getServletContext().getInitParameter("SW_VER") %> ERROR PAGE</title>
 <link rel="stylesheet" type="text/css" href="default.css"/>
 <script language="Javascript">
  function viewErrDett()
  { 
    window.open("err_det_view.jsp?actionTxt=<%= java.net.URLEncoder.encode(actionTxt,"UTF-8") %>&msgTxt=<%= java.net.URLEncoder.encode(msgTxt,"UTF-8") %>&detailTxt=<%= java.net.URLEncoder.encode(detailTxt,"UTF-8") %>&sysDetailTxt=<%= java.net.URLEncoder.encode(sysDetailTxt,"UTF-8") %>","ERROR_DETAILS","width=680,height=420,resizable=yes,scrollbars=no,status=0,location=no,menubar=no");
  }
 </script>		 	
</head>
