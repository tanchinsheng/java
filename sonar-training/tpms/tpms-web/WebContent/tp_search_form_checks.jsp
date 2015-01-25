 <SCRIPT language=JavaScript>
  function openCalendar(field, label)
  {
   var url="calendar_widget.jsp";
   var qrystr="?field="+field+"&form="+"tpSearchForm"+"&label="+label;
   var popup=window.open(url+qrystr,"CALENDAR","width=680,height=420,resizable=yes,scrollbars=no,status=0,toolbar=no,location=no,menubar=no");
 
   if (popup != null)
   {
    if (popup.opener == null) popup.opener = self
   }
  }

  function submitSearch()
  {  
    <%
     if ((qryType.equals("db_tp_history"))||(qryType.equals("vob_tp_history")))
     {%> 
       if (document.tpSearchForm.elements['field.JOBNAME'].value=='')
       {
         alert('JOBNAME IS A MANDATORY FIELD FOR THIS QUERY');
         return;
       } 
       else
         if (document.tpSearchForm.elements['field.JOBNAME'].value.indexOf('%')>0)
         {
           alert('THE JOBNAME MUST BE FULLY SPECIFIED (WILD CHARS ARE NOT ALLOWED)');
           return;
         }   
     <%}
    %>
    if (!controlData()) return;
    document.tpSearchForm.submit();
  }

  function controlJobName(){
     var jobName = document.tpSearchForm.elements['field.JOBNAME'].value;
     var irregExpress = new Array(".",";","!","$","-",":","@","#","[","]","+","£","&"," ","\,","\"","\'","(",")","\\","/","*","=","?","^");
     var length = jobName.length;
     var finalCar = jobName.substring(length-1);

     for (i=0; i < irregExpress.length; i++)
     {
        if (jobName.lastIndexOf(irregExpress[i]) >= 0)
        {
            return true;
        }
     }
     return false;
   }


   //controllo numerico su JobRelease
    function controlJobRel(){
         if (document.tpSearchForm.elements['field.JOB_REL'] != null) {
             var jobRel = document.tpSearchForm.elements['field.JOB_REL'].value;
             var length = jobRel.length;
             var res = false;
             for (j=0; j<=length-1; j++)
             {
                 var car = jobRel.substring(j,j+1);
                 if((car == '0')||(car == '1')||(car == '2')||(car == '3')||(car == '4')
                    ||(car == '5')||(car == '6')||(car == '7')||(car == '8')||(car == '9'))
                 {
                    res = false;
                 }else{

                    return true;
                 }
             }
             return res;
         }
    }

   function controlData(){

     if (controlJobName())
     {
        alert('CHARACTER NOT VALIN IN JobName!(NOT USE SPECIAL CHARACTER)');
        return false;
     }

     <%
     if (!((qryType.equals("db_prod_lib_hist"))||(qryType.equals("vob_prod_lib_hist"))||(qryType.equals("db_tp_search_base"))))
     {%>
       if (controlJobRel()) {
        return alert('JOB RELEASE requires a number.');
        return false;
     }
     <%}
    %>


     return true;
   }
 </SCRIPT>
