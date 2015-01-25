<%@ page import="java.util.*"
         isErrorPage="false"
         errorPage="uncaughtErr.jsp"
%>

<%
    String firstFieldName = request.getParameter("field");
 
    String fieldName1;
    fieldName1 = firstFieldName;
 
    String formName = request.getParameter("form");
    String fieldLabel = request.getParameter("label");
%>
<HTML>
<HEAD>
<TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<META content="text/html; charset=us-ascii" http-equiv=Content-Type>
<LINK href="default.css" rel="stylesheet" type="text/css">
<SCRIPT language=JavaScript>
 function submitForm()
 {
   topwin = window.opener;
   
   <% if (fieldName1!=null)
      {%>
        topwin.document.<%= formName %>.elements['<%= fieldName1 %>'].value=document.dateSelForm.elements['<%= fieldName1 %>'].value;
      <%}
   %>
   
   window.close();
 }

 function setDate(y, m, d, field)
 {
 if (m < 10) m = '0' + m;
 if (d < 10) d = '0' + d;  
 document.forms.profileAcctAbbonamentiForm.elements[field].value=""+d+"/"+m+"/"+y;
 }



function setDate(i, str) 
{
 if (str == "   ") 
 {
  return;
 }
 dt = new Date();
  mnth1  = dt.getMonth(); /* 0-11*/
  dayOfMnth = dt.getDate(); /* 1-31*/
  yr = dt.getFullYear(); 
   str=trim(str);
 mnth = document.dateSelForm.month1.value;
 year = document.dateSelForm.year1.value 
 dateStr = (str.length<2 ? "0"+str : str)+"/"+getMonthLab(mnth)+"/"+year;
  dateStr = trim(dateStr);
 if(yr == year && mnth1 == mnth){
	if(parseInt(dayOfMnth) > parseInt(str)){
		alert('Cannot Go Before Current date');
 		return;
 	}
 }
 mnth++;
 
 if (i==1) document.dateSelForm.elements['<%= fieldName1 %>'].value = dateStr;

 //document.dateSelForm.submit();
}


/**
 * The function removes spaces from the selected date.
 */

function trim(str) 
{
 res="";

 for(var i=0; i< str.length; i++) 
 {
   if (str.charAt(i) != " ") 
   {
     res +=str.charAt(i);
   }
 }
 return res;
}
 

/**
 * The method to get the Month name given the Month number of the year.
 */

function getMonthName(mnth) 
{

 if (mnth == 0) {
  name = "JANUARY";
 }else if(mnth==1) {
  name = "FEBRARY";
 }else if(mnth==2) {
  name = "MARCH";
 }else if(mnth==3) {
  name = "APRIL";
 }else if(mnth==4) {
  name = "MAY";
 } else if(mnth==5) {
  name = "JUNE";
 } else if(mnth==6) {
  name = "JULY";
 } else if(mnth==7) {
  name = "AUGUST";
 } else if(mnth==8) {
  name = "SEPTEMBER";
 } else if(mnth==9) {
  name = "OCTOBER";
 } else if(mnth==10) {
  name = "NOVEMBER";
 } else if(mnth==11) {
  name = "DECEMBER";
 }

 return name;
}

function getMonthLab(mnth) 
{

 if (mnth == 0) {
  name = "JAN";
 }else if(mnth==1) {
  name = "FEB";
 }else if(mnth==2) {
  name = "MAR";
 }else if(mnth==3) {
  name = "APR";
 }else if(mnth==4) {
  name = "MAY";
 } else if(mnth==5) {
  name = "JUN";
 } else if(mnth==6) {
  name = "JUL";
 } else if(mnth==7) {
  name = "AUG";
 } else if(mnth==8) {
  name = "SEP";
 } else if(mnth==9) {
  name = "OCT";
 } else if(mnth==10) {
  name = "NOV";
 } else if(mnth==11) {
  name = "DEC";
 }

 return name;
}


/**
 * Get the number of days in the month based on the year.
 */

function getNoOfDaysInMnth(mnth,yr) {
 
 rem = yr % 4;

 if(rem ==0) {
   leap = 1;
 } else {
  leap = 0;
 }

 noDays=0;

 if ( (mnth == 1) || (mnth == 3) || (mnth == 5) ||
      (mnth == 7) || (mnth == 8) || (mnth == 10) ||
      (mnth == 12)) {
  noDays=31;
 } else if (mnth == 2) {
           noDays=28+leap;
        } else {
           noDays=30;
 }

 //alert(noDays);
 return noDays;
 
      
}//getNoOfDaysInMnth()
  

/**
 * The function to reset the date values in the buttons of the 
 * slots.
 */

function fillDates(index,dayOfWeek1,noOfDaysInmnth) {

 for(var i=1; i<43; i++) {
   str = (index==1 ? "s" : "ss")+i;
   document.dateSelForm.elements[str].value="   ";
 }


 startSlotIndx = dayOfWeek1;
 slotIndx = startSlotIndx;

 for(var i=1; i<(noOfDaysInmnth+1); i++) {
  slotName = (index==1 ? "s" : "ss")+slotIndx;

  val="";
  if (i<10) 
  {
    val = " "+i+" ";
  } else {
    val = i;
  }

  document.dateSelForm.elements[slotName].value = val;
  slotIndx++;
 }
  
}//fillDates()
 

/**
 * The function that is called at the time of loading the page.
 * This function displays Today's date and also displays the 
 * the calendar of the current month.
 */

function thisMonth() {

  dt = new Date();
  mnth  = dt.getMonth(); /* 0-11*/
  dayOfMnth = dt.getDate(); /* 1-31*/
  dayOfWeek = dt.getDay(); /*0-6*/
  yr = dt.getFullYear(); /*4-digit year*/

  //alert("month:"+(mnth+1)+":dayofMnth:"+dayOfMnth+":dayofweek:"+dayOfWeek+":year:"+yr);

  mnthName = getMonthName(mnth)+ " ";
  document.dateSelForm.month1.value = mnth;
  document.dateSelForm.year1.value = yr;
  document.dateSelForm.currMonth1.value = mnth;
  document.dateSelForm.currYear1.value = yr;
  
  

  document.dateSelForm.monthYear1.value = mnthName+yr;
 

  //document.dateSelForm.<%= fieldName1 %>.value = dayOfMnth+"/"+(mnth+1)+"/"+yr;
  

  startStr = (mnth+1)+"/1/"+yr;
  dt1 = new Date(startStr);
  dayOfWeek1 = dt1.getDay(); /*0-6*/

  noOfDaysInMnth = getNoOfDaysInMnth(mnth+1,yr);
  fillDates(1,dayOfWeek1+1,noOfDaysInMnth);
 
 

}//thisMonth()

/**
 * The function that will be used to display the calendar of the next month.
 */

function nextMonth(i) 
{
 var currMnth = (i==1 ? document.dateSelForm.month1.value : document.dateSelForm.month2.value);
 currYr = (i==1 ? document.dateSelForm.year1.value : document.dateSelForm.year2.value);

 if (currMnth == "11") {
    nextMnth = 0;
    nextYr = currYr;
    nextYr++;
 } else {
   nextMnth=currMnth;
   nextMnth++;
   nextYr = currYr;
 }

 mnthName = getMonthName(nextMnth);
 if (i==1) document.dateSelForm.month1.value=nextMnth; else document.dateSelForm.month2.value=nextMnth;
 if (i==1) document.dateSelForm.year1.value=nextYr; else document.dateSelForm.year2.value=nextYr;
 if (i==1) document.dateSelForm.monthYear1.value= mnthName+" "+nextYr; else document.dateSelForm.monthYear2.value= mnthName+" "+nextYr;

 str = (nextMnth+1)+"/1/"+nextYr;
 dt = new Date(str);
 dayOfWeek = dt.getDay();

 noOfDays = getNoOfDaysInMnth(nextMnth+1,nextYr);
 fillDates(i,dayOfWeek+1,noOfDays);
 

}//nextMonth()

/**
 * The method to display the calendar of the previous month.
 */

function prevMonth(i) 
{

 var currMnth = document.dateSelForm.month1.value;
 currYr = document.dateSelForm.year1.value;

 if (currMnth == "0") {
    prevMnth = 11;
    prevYr = currYr;
    prevYr--;
 } else {
   prevMnth=currMnth;
   prevMnth--;
   prevYr = currYr;
 }

 str = (prevMnth+1)+"/1/"+prevYr;
 dt = new Date(str);
 dayOfWeek = dt.getDay();

 /***********************************************
  * Remove the comment if do not want the user to 
  * go to any previous month than this current month.
  ***********************************************/

 runningMonth = document.dateSelForm.currMonth1.value;
 rMonth=runningMonth;
 rMonth++;
 runningYear = document.dateSelForm.currYear1.value;
 rYear=runningYear;

 str = (rMonth)+"/1/"+rYear;
 dt1 = new Date(str);
 
 if (dt.valueOf() < dt1.valueOf()) {
   alert('Cannot Go Before Current Month');
   return;
 }

 /**************************************************
 * End of comment
 **************************************************/

 mnthName = getMonthName(prevMnth);
 if (i==1) document.dateSelForm.month1.value=prevMnth; else document.dateSelForm.month2.value=prevMnth;
 if (i==1) document.dateSelForm.year1.value=prevYr; else document.dateSelForm.year2.value=prevYr;
 if (i==1) document.dateSelForm.monthYear1.value= mnthName+" "+prevYr; else document.dateSelForm.monthYear2.value= mnthName+" "+prevYr;


 noOfDays = getNoOfDaysInMnth(prevMnth+1,prevYr);
 fillDates(i,dayOfWeek+1,noOfDays);
 
}//prevMonth()

</SCRIPT>
</HEAD>
<BODY bgColor=#ffffff onload=thisMonth()>
<TABLE border="0" cellspacing="3" cellpadding="0">
<TR>
<% if (fieldName1!=null)
{%>
 <TD ALIGN="LEFT">
 <TABLE border=0 cellPadding=0 cellSpacing=0 width=290'>
  <TBODY>
  <TR>
    <TD width=4><IMG alt="" border=0 height=18 src="img/tit_sx.gif" width=4></TD>
    <TD align=middle background=img/tit_center.gif class=titverdi noWrap><B><!-- FORM TITLE-->DATE</B></TD>
    <TD width=4><IMG alt="" border=0 height=18 src="img/tit_dx.gif" width=4></TD>
    <TD><IMG alt="" border=0 height=18 src="img/pix.gif" width=253></TD></TR>
  <TR>
    <TD colSpan=4><IMG alt="" border=0 height=1 src="img/pix_grey.gif" width=290></TD>
  </TR>
  <TR>
    <TD colSpan=4><IMG alt="" border=0 height=6 src="img/pix.gif" width=290></TD>
  </TR>
  </TBODY>
 </TABLE>
 </TD>
<%}
%>

</TR>

<TR>
<TD>
<FORM name=dateSelForm><INPUT name=month1 type=hidden> 
<INPUT name=year1 type=hidden> 
<INPUT name=currMonth1 type=hidden> 
<INPUT name=currYear1 type=hidden> 

<TR>

<TD ALIGN="LEFT">
<TABLE bgColor=#d4d4d4 border=1 cellPadding=0 cellSpacing=0 width=280>
  <TBODY>
  <TR>
    <TD align=center class="testonoline" bgColor=#c9e1fa  colSpan=7 vAlign=top> 
    <IMG border=0 height=12 src="img/pix.gif"> 
    <INPUT type="button" name="prev" onclick="prevMonth(1)" value="<<"> 
    <INPUT class="txtnob" maxlength="100" size="15"  name="monthYear1"> 
    <INPUT type="button" name="next" onclick="nextMonth(1)"  value=">>"> 
    <IMG border=0 height=12 src="img/pix.gif"> 
    </TD>
  </TR>
  <TR bgColor=#000080>
    <TD width="14%"><FONT color=#ffffff size=2><STRONG>SUN</STRONG></FONT></TD>
    <TD width="14%"><FONT color=#ffffff size=2><STRONG>MON</STRONG></FONT></TD>
    <TD width="14%"><FONT color=#ffffff size=2><STRONG>TUE</STRONG></FONT></TD>
    <TD width="14%"><FONT color=#ffffff size=2><STRONG>WED</STRONG></FONT></TD>
    <TD width="14%"><FONT color=#ffffff size=2><STRONG>THU</STRONG></FONT></TD>
    <TD width="15%"><FONT color=#ffffff size=2><STRONG>FRI</STRONG></FONT></TD>
    <TD width="15%"><FONT color=#ffffff size=2><STRONG>SAT</STRONG></FONT></TD></TR>
  <TR>
    <TD align=middle width="14%"><INPUT name=s1 onclick="setDate(1, this.value);" type=button value=" 1 "></TD>
    <TD align=middle width="14%"><INPUT name=s2 onclick="setDate(1, this.value);" type=button value=" 2 "></TD>
    <TD align=middle width="14%"><INPUT name=s3 onclick="setDate(1, this.value);" type=button value=" 3 "></TD>
    <TD align=middle width="14%"><INPUT name=s4 onclick="setDate(1, this.value);" type=button value=" 4 "></TD>
    <TD align=middle width="14%"><INPUT name=s5 onclick="setDate(1, this.value);" type=button value=" 5 "></TD>
    <TD align=middle width="15%"><INPUT name=s6 onclick="setDate(1, this.value);" type=button value=" 6 "></TD>
    <TD align=middle width="15%"><INPUT name=s7 onclick="setDate(1, this.value);" type=button value=" 7 "></TD></TR>
  <TR>
    <TD align=middle width="14%"><INPUT name=s8 onclick="setDate(1, this.value);" type=button value=" 8 "></TD>
    <TD align=middle width="14%"><INPUT name=s9 onclick="setDate(1, this.value);" type=button value=" 9 "></TD>
    <TD align=middle width="14%"><INPUT name=s10 onclick="setDate(1, this.value);" type=button value=10></TD>
    <TD align=middle width="14%"><INPUT name=s11 onclick="setDate(1, this.value);" type=button value=11></TD>
    <TD align=middle width="14%"><INPUT name=s12 onclick="setDate(1, this.value);" type=button value=12></TD>
    <TD align=middle width="15%"><INPUT name=s13 onclick="setDate(1, this.value);" type=button value=13></TD>
    <TD align=middle width="15%"><INPUT name=s14 onclick="setDate(1, this.value);" type=button value=14></TD></TR>
  <TR>
    <TD align=middle width="14%"><INPUT name=s15 onclick="setDate(1, this.value);" type=button value=15></TD>
    <TD align=middle width="14%"><INPUT name=s16 onclick="setDate(1, this.value);" type=button value=16></TD>
    <TD align=middle width="14%"><INPUT name=s17 onclick="setDate(1, this.value);" type=button value=17></TD>
    <TD align=middle width="14%"><INPUT name=s18 onclick="setDate(1, this.value);" type=button value=18></TD>
    <TD align=middle width="14%"><INPUT name=s19 onclick="setDate(1, this.value);" type=button value=19></TD>
    <TD align=middle width="15%"><INPUT name=s20 onclick="setDate(1, this.value);" type=button value=20></TD>
    <TD align=middle width="15%"><INPUT name=s21 onclick="setDate(1, this.value);" type=button value=21></TD></TR>
  <TR>
    <TD align=middle width="14%"><INPUT name=s22 onclick="setDate(1, this.value);" type=button value=22></TD>
    <TD align=middle width="14%"><INPUT name=s23 onclick="setDate(1, this.value);" type=button value=23></TD>
    <TD align=middle width="14%"><INPUT name=s24 onclick="setDate(1, this.value);" type=button value=24></TD>
    <TD align=middle width="14%"><INPUT name=s25 onclick="setDate(1, this.value);" type=button value=25></TD>
    <TD align=middle width="14%"><INPUT name=s26 onclick="setDate(1, this.value);" type=button value=26></TD>
    <TD align=middle width="15%"><INPUT name=s27 onclick="setDate(1, this.value);" type=button value=27></TD>
    <TD align=middle width="15%"><INPUT name=s28 onclick="setDate(1, this.value);" type=button value=28></TD></TR>
  <TR>
    <TD align=middle><INPUT name=s29 onclick="setDate(1, this.value);" type=button value=29></TD>
    <TD align=middle><INPUT name=s30 onclick="setDate(1, this.value);" type=button value=30></TD>
    <TD align=middle><INPUT name=s31 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s32 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s33 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s34 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s35 onclick="setDate(1, this.value);" type=button value="   "></TD></TR>
  <TR>
    <TD align=middle><INPUT name=s36 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s37 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s38 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s39 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s40 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s41 onclick="setDate(1, this.value);" type=button value="   "></TD>
    <TD align=middle><INPUT name=s42 onclick="setDate(1, this.value);" type=button value="   "></TD> 
    </TR></TBODY></TABLE>
<TABLE border=0 cellPadding=0 cellSpacing=0 width=280>
  <TBODY>
  <tr>
    <td><img src="img/pix.gif" height="8" alt="" border="0"></td>
  </tr>
  <TR>
    <TD align=right class=testonoline><B>Date</B> 
      <INPUT readonly class="txt" maxlength="100" size="11" name="<%= fieldName1 %>" onblur="if (this.value != '') { this.value = ''; }"> 
    </TD>
  </TR>
  </TBODY>
</TABLE>
</TD>
 



                
                
		<tr>
                 <td  colspan="<%= ((fieldName1==null) ? "1" : "2") %>" align="right">
     		  <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   <TR>
                      <TD valign="top"><IMG SRC="img/btn_left.gif"></TD>
                      <TD valign="top" background="img/btn_center.gif" >
                      <A CLASS="BUTTON" HREF="javascript:submitForm()">SUBMIT</A></TD>			
                      <TD valign="top" ><IMG SRC="img/btn_right.gif"></TD>
		      <TD valign="top" ><IMG SRC="img/pix.gif" width="32" height="1"></TD>	
	           </TR>
                  </TABLE>
                 </td>
 		</tr>	
  
</FORM>
</TD>
</TR>
</TABLE>
</BODY>
</HTML>
