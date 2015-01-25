<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>    
<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
  <form:form action="formWithValidation.html" commandName="userWithValidation"  >
   <table align="center" >
		<tr>
			<td>User Name :<font color="red"><form:errors path="userName" /></font></td>
			<td><form:input path="userName" /></td>
		</tr>
		<tr>
			<td>Email :</td>
			<td><form:input path="email" /></td>
		</tr>		
		<tr>
			<td></td>
			<td><input type="submit" value="Submit" /></td>
		</tr>
	</table>
  </form:form> 
  
<hr />
	<core:set var="attributeName" value="${userWithValidation.userName}"/>
	
<core:if test="${pageContext.request.method=='POST' && error == null }">	

	Using Scriptlet - User name is:
	<%
	String myVariable = (String)pageContext.getAttribute("attributeName");
	out.print(myVariable);
	%>
	<hr />		
		
		Using taglib - User name is: <core:out value="${userWithValidation.userName}" escapeXml="false" />
	<br/><br/>	
	Save Successfully.
</core:if>  
  
</body>
</html>