<%@ page import = "java.util.*, java.net.*, tpms.*"
         isErrorPage = "false"
         errorPage = "uncaughtErr.jsp"%>
<%  
  String time = new Long(System.currentTimeMillis()).toString();
  String outPage = (String)request.getAttribute("outPage");
  String actionTxt=(String)request.getAttribute("actionTxt");
  String refreshTime=(String)request.getAttribute("refreshTime");
  String contextPath = request.getContextPath();
  String qryStr="";
  StringBuffer formSB = new StringBuffer("");
  for (Enumeration e = request.getAttributeNames(); e.hasMoreElements();)
  {       
    String attrnam = (String)e.nextElement();
    String attrval = (String)request.getAttribute(attrnam);

    if (attrnam.equals("outPage")) {
        continue;
    }
    if (attrnam.equals("nextPage")){
        continue;
    }
	if ((attrnam.startsWith("lsFile"))&&(!attrnam.equals("lsFile"))) continue; /* id=20-OCT-2003 */
    if ((attrnam.startsWith("tpFile"))&&(!attrnam.equals("tpFile"))) continue; /* id=20-OCT-2003 */
  	if ((!attrnam.equals("FIELD.HW_MODIFICATION")) && (!attrnam.equals("FIELD.DELIVERYCOMM")) && (!attrnam.equals("FIELD.SINGLEDELIVERYCMT")) && (!attrnam.equals("addRowValue"))){
  		if ((!attrnam.equals("rejectCmt")) && (!attrnam.equals("markAsValCmt"))){
    		formSB.append("<input type=\"hidden\" name=\"").append(attrnam).append("\" value=\"").append(attrval).append("\">");
		}
  	}
  	
  }

%>

<HTML>
<HEAD>
 <TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
 <link rel="stylesheet" type="text/css" href="default.css"/>
 <SCRIPT language=JavaScript>
  function forwarda() 
  {
    document.forms["tmpForm"].submit();
    <%--location.replace("<%= outPage+"?t="+time+qryStr %>");--%>
  } 
 </SCRIPT>
</HEAD>
<BODY bgcolor="#FFFFFF" onLoad="forwarda()">
<form action="<%=contextPath%>/<%=outPage%>" name="tmpForm" method="get">
    <%=formSB.toString()%>
</form>
</BODY>
</HTML>
