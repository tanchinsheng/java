 <SCRIPT language=JavaScript>
  function openCalendar(field, label)
  {
   var url="calendar_widget.jsp";
   var qrystr="?field="+field+"&form="+"lsSearchForm"+"&label="+label;
   var popup=window.open(url+qrystr,"CALENDAR","width=680,height=420,resizable=yes,scrollbars=no,status=0,toolbar=no,location=no,menubar=no");

   if (popup != null)
   {
    if (popup.opener == null) popup.opener = self
   }
  }

  function submitSearch()
  {
    <%
        if (qryType.equals("vob_lineset_history"))
        {%>
           if (document.lsSearchForm.elements['field.LS_NAME'].value=='')
           {
             alert('LINESET NAME IS A MANDATORY FIELD FOR THIS QUERY');
             return;
           }
           else
             if (document.lsSearchForm.elements['field.LS_NAME'].value.indexOf('%')>0)
             {
               alert('THE LINESET NAME MUST BE FULLY SPECIFIED (WILD CHARS ARE NOT ALLOWED)');
               return;
             }
     <%}
    %>
    document.lsSearchForm.submit();
  }
 </SCRIPT>
