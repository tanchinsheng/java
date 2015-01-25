<%@ page import="org.w3c.dom.Element" isErrorPage="false" errorPage="uncaughtErr.jsp"%>
<%@ page import="tpms.VobManager"%>
<%@ page import="tol.reportSel"%>
<%@ page import="tol.slctLst"%>
<%@ page import="java.util.Vector"%>
<%@ page import="tpms.FacilityMgr"%>
<%@ page import="tpms.TesterInfoMgr"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="tol.dateRd"%>
<%@ page import="tpms.utils.UserUtils"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="tpms.utils.QueryUtils"%>
<%@ page import="it.txt.tpms.tp.utils.TpUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.Integer"%>
<%@ page import="tpms.EmailInfoMgr"%>
<%@ page import="tpms.BeanTest"%>

<%
    boolean okDbConnection = QueryUtils.checkCtrlServletDBConnection();
	
    String myCurrentUser = (String) session.getAttribute("user");
    String listRepFileName=(String)request.getAttribute("repFileName");
    String jobname = request.getAttribute("jobname")!= null ? (String)request.getAttribute("jobname") : "";
    String job_rel=request.getAttribute("job_rel")!= null ? (String)request.getAttribute("job_rel") : "";
    String job_rev=request.getAttribute("job_rev")!= null ? (String)request.getAttribute("job_rev") : "";
    String job_ver=(String)request.getAttribute("job_ver")!= null ? (String)request.getAttribute("job_ver") : "";
    String fromEmail=(String)request.getAttribute("fromEmail");
    String facility=request.getAttribute("facility")!= null ? (String)request.getAttribute("facility") : "";
    String line=(request.getAttribute("line")!= null ? (String)request.getAttribute("line") : "");
    String testerInfo=(request.getAttribute("testerInfo")!=null ? (String)request.getAttribute("testerInfo") : "");
    Element tpRec=(request.getAttribute("tpRec")!=null ? (Element)request.getAttribute("tpRec") : (Element)session.getAttribute("tpRec"));
    session.setAttribute("tpRec",tpRec);

    Vector toPlantLst = VobManager.getTvobPlantsListFiltered((String)session.getAttribute("vob"), myCurrentUser);

    String prod_login=(request.getAttribute("prod_login")!= null ? (String)request.getAttribute("prod_login") : "");
    String valid_login=(request.getAttribute("valid_login")!= null ? (String)request.getAttribute("valid_login") : "");
    String email_to=(request.getAttribute("email_to")!= null ? (String)request.getAttribute("email_to") : "");
    String email_cc=(request.getAttribute("email_cc")!= null ? (String)request.getAttribute("email_cc") : "");
    String toPlant=(request.getAttribute("toPlant")!= null ? (String)request.getAttribute("toPlant") : (String)toPlantLst.elementAt(0));
    String owner = XmlUtils.getVal(tpRec,"OWNER_LOGIN");
    String productionArea = (request.getAttribute("area_prod")!= null ? (String)request.getAttribute("area_prod") : "");
    String singleDeliveryCmt =request.getAttribute("singleDeliveryCmt")!= null ? (String)request.getAttribute("singleDeliveryCmt") : "";
    String deliveryComm =request.getAttribute("deliveryComm")!= null ? (String)request.getAttribute("deliveryComm") : "";
    String hw_modification =request.getAttribute("hw_modification")!= null ? (String)request.getAttribute("hw_modification") : "";
    String exp_avg_yv =request.getAttribute("exp_avg_yv")!= null ? (String)request.getAttribute("exp_avg_yv") : "";
    String zero_yw =request.getAttribute("zero_yw")!= null ? (String)request.getAttribute("zero_yw") : "";
    String new_testtime =request.getAttribute("new_testtime")!= null ? (String)request.getAttribute("new_testtime") : "";
    String valid_till =request.getAttribute("valid_till")!= null ? (String)request.getAttribute("valid_till") : "";
    String is_temp =request.getAttribute("is_temp")!= null ? (String)request.getAttribute("is_temp") : ""; 
   	String test_no =request.getAttribute("test_no")!= null ? (String)request.getAttribute("test_no") : ""; 
   	String new_flag =request.getAttribute("new_flag")!= null ? (String)request.getAttribute("new_flag") : "";
    String old_lsl =request.getAttribute("old_lsl")!= null ? (String)request.getAttribute("old_lsl") : "";
    String old_usl =request.getAttribute("old_usl")!= null ? (String)request.getAttribute("old_usl") : "";
    String unit =request.getAttribute("unit")!= null ? (String)request.getAttribute("unit") : "";
    String new_lsl =request.getAttribute("new_lsl")!= null ? (String)request.getAttribute("new_lsl") : "";
    String new_usl =request.getAttribute("new_usl")!= null ? (String)request.getAttribute("new_usl") : "";
    String test_comment =request.getAttribute("test_comment")!= null ? (String)request.getAttribute("test_comment") : "";
    String lineset=(request.getAttribute("lineset")!= null ? (String)request.getAttribute("lineset") : "");
    String testerfam=(request.getAttribute("testerfam")!= null ? (String)request.getAttribute("testerfam") : "");
    String dis_email=(request.getAttribute("dis_email")!= null ? (String)request.getAttribute("dis_email") : "");
    String testcount=(request.getAttribute("testcount")!= null ? (String)request.getAttribute("testcount") : "");
    String addRowValue =request.getAttribute("addRowValue")!= null ? (String)request.getAttribute("addRowValue") : "";
    String testrowcount=(request.getAttribute("testrowcount")!= null ? (String)request.getAttribute("testrowcount") : "");

    //String certification=((String)request.getAttribute("certification")!= null ? (String)request.getAttribute("certification") : "");
	
    reportSel repsel=(reportSel)session.getAttribute("repsel");
    slctLst dbUsrLst = null;
    slctLst dbPlant = null;
    slctLst dbmail = null;
 
    String xferFileDir = "";
    //the path to save in ClearCase
    String xferPathToSend ="";
    int index;
    int index2;
    //home dir unix
    String homeDir= UserUtils.getUserUnixHome(myCurrentUser);
    //home dir +
    String lsBaseDir=homeDir.concat(XmlUtils.getVal(tpRec,"XFER_PATH"));
    
    if (request.getAttribute("curFilePath")!=null){
    	index = homeDir.length();
    	xferFileDir = ((String)request.getAttribute("curFilePath")).substring(index);
    	index2 = lsBaseDir.length();
 		xferPathToSend  = ((String)request.getAttribute("curFilePath")).substring(index2+1);
     }
    else {
  	     String tempxferFileDir = request.getAttribute("tempxferFileDir")!=null ? (String)request.getAttribute("tempxferFileDir") : ""; 
    	 if(!tempxferFileDir.equals("")){
	 	 xferFileDir = tempxferFileDir; 
	 	 }
		 String tempxferFile = request.getAttribute("tempxferFile")!=null ? (String)request.getAttribute("tempxferFile") : ""; 
   		 if(!tempxferFile.equals("")){
	 	 xferPathToSend = tempxferFile; 
	 	 }
     }
%>
<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
<TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
<LINK href="default.css" type=text/css rel=stylesheet>
<script language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
<SCRIPT language=JavaScript>
	var tstInfoArray = new Array();
   	var facilityArray = new Array();
   	var usersArray = new Array();
   	var usrEmailsArray = new Array();
   	var newAttrEmailArr = new Array();
   	var tmpText;
   	getUsersData();
   	getTesterInfos();
   	getFacilities();
   
 	<%=TpUtils.buildProductionAreasCsArrays("productionAreas")%> 
   	function openCalendar(field, label) {
  		var url="single_calendar_widget.jsp";
   		var qrystr="?field="+field+"&form="+"tpActionForm"+"&label="+label;
   		var popup=window.open(url+qrystr,"CALENDAR","width=330,height=350,resizable=yes,scrollbars=no,status=0,toolbar=no,location=no,menubar=no");
   		if (popup != null){
    		if (popup.opener == null) popup.opener = self;
    	}
  	}
  
 
	function chkXfer() {
		var xferFile = document.tpActionForm.elements['FIELD.XFER_PATH'].value;
	 	if(xferFile == "") {
	 		alert('Must select xferfile.'); 
			document.tpActionForm.elements['FIELD.EMAIL_TO'].focus();
    	}
	}

	function removeRowFromTable(rowno) {
		tmpText ="";
		var rCount="";
		document.addtpActionForm.deletedRow.value=rowno;
		document.addtpActionForm.action.value = 'tp_submit';
 		document.addtpActionForm.tempJobname.value=document.tpActionForm.elements['FIELD.JOBNAME'].value;
 		document.addtpActionForm.tempjob_rel.value=document.tpActionForm.elements['FIELD.JOB_REL'].value;
   		document.addtpActionForm.tempjob_rev.value=document.tpActionForm.elements['FIELD.JOB_REV'].value;
     
    	var ind = document.tpActionForm.elements['FIELD.FACILITY'].selectedIndex;
    	if (ind>=0) {
    		document.addtpActionForm.tempfacility.value=document.tpActionForm.elements['FIELD.FACILITY'].options[ind].text;
    	} else {
       		document.addtpActionForm.tempfacility.value='';
    	}
    
    	ind = document.tpActionForm.elements['FIELD.TESTER_INFO'].selectedIndex;
    	if (ind>=0) {
    		document.addtpActionForm.temptesterInfo.value=document.tpActionForm.elements['FIELD.TESTER_INFO'].options[ind].text;
    	} else {
       		document.addtpActionForm.temptesterInfo.value='';
    	}

    	ind = document.tpActionForm.elements['FIELD.TO_PLANT'].selectedIndex;
    	if (ind>=0) {
    		document.addtpActionForm.temptoPlant.value=document.tpActionForm.elements['FIELD.TO_PLANT'].options[ind].text;
    	} else {
       		document.addtpActionForm.temptoPlant.value='';
    	}

		ind = document.tpActionForm.elements['FIELD.PROD_LOGIN'].selectedIndex;
    	if (ind>=0) {
        	document.addtpActionForm.tempprod_login.value=document.tpActionForm.elements['FIELD.PROD_LOGIN'].options[ind].text;
    	} else {
       		document.addtpActionForm.tempprod_login.value='';
    	}
     
    	ind = document.tpActionForm.elements['FIELD.VALID_LOGIN'].selectedIndex;
    	if (ind>=0) {
        	document.addtpActionForm.tempvalid_login.value=document.tpActionForm.elements['FIELD.VALID_LOGIN'].options[ind].text;
    	} else {
       		document.addtpActionForm.tempvalid_login.value='';
    	}
    
    	ind = document.tpActionForm.elements['FIELD.EMAIL_TO'].selectedIndex;
    	if (ind>=0) {
        	document.addtpActionForm.tempemail_to.value=document.tpActionForm.elements['FIELD.EMAIL_TO'].options[ind].text;
    	} else {
       		document.addtpActionForm.tempemail_to.value='';
    	}
   
    	document.addtpActionForm.tempemail_cc.value=document.tpActionForm.elements['FIELD.EMAIL_CC'].value;
    	document.addtpActionForm.templine.value=document.tpActionForm.elements['FIELD.LINE'].value;

    	var prodAreaSelIndex=document.tpActionForm.elements['FIELD.AREA_PROD'].selectedIndex;
    	if (prodAreaSelIndex >= 0) {
    		document.addtpActionForm.elements['FIELD.AREA_PROD'].value=document.tpActionForm.elements['FIELD.AREA_PROD'].options[prodAreaSelIndex].value; 
    	} else {
        	document.addtpActionForm.elements['FIELD.AREA_PROD'].value = "";
    	}
		document.addtpActionForm.tempsingleDeliveryCmt.value=document.tpActionForm.elements['FIELD.SINGLEDELIVERYCMT'].value;
		document.addtpActionForm.tempdeliveryComm.value=document.tpActionForm.elements['FIELD.DELIVERYCOMM'].value;
		document.addtpActionForm.temphw_modification.value=document.tpActionForm.elements['FIELD.HW_MODIFICATION'].value;
		document.addtpActionForm.tempexp_avg_yv.value=document.tpActionForm.elements['FIELD.EXP_AVG_YV'].value;
		document.addtpActionForm.tempzero_yw.value=document.tpActionForm.elements['FIELD.ZERO_YW'].value;
		document.addtpActionForm.tempnew_testtime.value=document.tpActionForm.elements['FIELD.NEW_TESTTIME'].value;
		document.addtpActionForm.tempvalid_till.value=document.tpActionForm.elements['FIELD.VALID_TILL'].value;
		document.addtpActionForm.tempis_temp.value=document.tpActionForm.elements['FIELD.IS_TEMP'].value; 
		var count=parseInt(document.tpActionForm.elements['FIELD.TEST_NO'].length)-1;
		for(i=1;i<document.tpActionForm.elements['FIELD.TEST_NO'].length;i++) {
			var tempTestNo = document.tpActionForm.elements['FIELD.TEST_NO'][i].value;
        	if (tempTestNo != "") {
	    		var tempTestNo = document.tpActionForm.elements['FIELD.TEST_NO'][i].value;   
	     		var tempNewFlag;
	     		var ind77=document.tpActionForm.elements['FIELD.NEW_FLAG'][i].selectedIndex;
	     		if (ind77>=0) {
	       			tempNewFlag =document.tpActionForm.elements['FIELD.NEW_FLAG'][i].options[ind77].value;
	     		} else {
	       			tempNewFlag='';
	        	}
	    		var tempOldLSL = document.tpActionForm.elements['FIELD.OLD_LSL'][i].value;
	    		var tempOldUSL = document.tpActionForm.elements['FIELD.OLD_USL'][i].value;
	    		var tempUnit = document.tpActionForm.elements['FIELD.UNIT'][i].value;
	    		var tempNewLSL = document.tpActionForm.elements['FIELD.NEW_LSL'][i].value;
	    		var tempNewUSL = document.tpActionForm.elements['FIELD.NEW_USL'][i].value;
	    		var tempTestComment = document.tpActionForm.elements['FIELD.TESTS_COMMENTS'][i].value;
	    		if(tempOldLSL == "") {
	    			tempOldLSL = "EMPTY";
	    		}	
				if(tempOldUSL == "") {
	    			tempOldUSL = "EMPTY";
	        	}	
	    		if(tempUnit == "") {
	    			tempUnit = "EMPTY";
	    		}	
	    		if(tempNewLSL == "") {
	    			tempNewLSL = "EMPTY";
	    		}	
	    		if(tempNewUSL == "") {
	    			tempNewUSL = "EMPTY";
	    		}	
	    		if(tempTestComment == "") {
	    			tempTestComment = "EMPTY";
	    		}
	    		if(i != parseInt(rowno)) {	
	   				if (tmpText == "") {
	   					rCount =1;
	    	 			tmpText = tempTestNo + "|" + tempNewFlag + "|" + tempOldLSL + "|" + tempOldUSL + "|" + tempUnit + "|" + tempNewLSL + "|" + tempNewUSL + "|" + tempTestComment + "~";
	    			} else {
	    				rCount++;
	    				tmpText = tmpText + "|" + tempTestNo + "|" + tempNewFlag + "|" + tempOldLSL + "|" + tempOldUSL + "|" + tempUnit + "|" + tempNewLSL + "|" + tempNewUSL + "|" + tempTestComment + "~";
	    			}
	    			if(i==count) {
	    				rCount++;
	    			}
	    		}
		    } else {
		    	rCount++;
	    	}
		}
		if (rCount == "") {
			rCount=1;
		}    
	
		document.addtpActionForm.tempAddRow.value = tmpText;
  		document.addtpActionForm.testcount.value = rCount;
  		document.addtpActionForm.tempxferFileDir.value='<%=xferFileDir%>';
  		document.addtpActionForm.tempxferFile.value= document.tpActionForm.elements['FIELD.XFER_PATH'].value;
 		document.addtpActionForm.LINE_SET.value= document.tpActionForm.LINE_SET.value;
  		document.addtpActionForm.TESTER_FAMILY.value= document.tpActionForm.TESTER_FAMILY.value;
   		document.addtpActionForm.submit();
	}

	function generateRow() {
		tmpText ="";
		var count = 2;	
  		document.addtpActionForm.Addrowcount.value = "Add";
  		document.addtpActionForm.action.value = 'tp_submit';
  		var rCount = '<%= request.getAttribute("testcount") != null ? request.getAttribute("testcount") : "" %>';
		document.addtpActionForm.testcount.value = parseInt(rCount)+1;
  		var testlen = document.tpActionForm.elements['FIELD.TEST_NO'].length;

		for (var index=1;index<document.tpActionForm.elements['FIELD.TEST_NO'].length;index++) {
			if (document.tpActionForm.elements['FIELD.TEST_NO'][index].value=="") {
				count++;
			}
		}
		if (parseInt(count)>2) {
			alert('Test# is mandatory!'); 
		} else {
			for(i=1;i<document.tpActionForm.elements['FIELD.TEST_NO'].length;i++) {
        		var tempTestNo = document.tpActionForm.elements['FIELD.TEST_NO'][i].value;
            	if (tempTestNo != "") {
	    			var tempTestNo = document.tpActionForm.elements['FIELD.TEST_NO'][i].value;   
     				var tempNewFlag;
     				var ind77=document.tpActionForm.elements['FIELD.NEW_FLAG'][i].selectedIndex;
     				if (ind77>=0) {
       					tempNewFlag =document.tpActionForm.elements['FIELD.NEW_FLAG'][i].options[ind77].value;
     				} else {
       					tempNewFlag='';
        			}
	    			var tempOldLSL = document.tpActionForm.elements['FIELD.OLD_LSL'][i].value;
	    			var tempOldUSL = document.tpActionForm.elements['FIELD.OLD_USL'][i].value;
	    			var tempUnit = document.tpActionForm.elements['FIELD.UNIT'][i].value;
	    			var tempNewLSL = document.tpActionForm.elements['FIELD.NEW_LSL'][i].value;
	    			var tempNewUSL = document.tpActionForm.elements['FIELD.NEW_USL'][i].value;
	    			var tempTestComment = document.tpActionForm.elements['FIELD.TESTS_COMMENTS'][i].value;
	    
	   				if (tempTestNo != "") {
	   					if (filterSpecialCharOnly(tempTestNo) == false) {
 			    		    alert("Test # contains invalid characters ~ , | , \" and \' ");
            				return false;
			       		} else {
   			    			if (tempTestNo.length > 32) {
       							alert("Test # must not be more than 32 characters!");
       							return false;
      		 				} 
      					}
     				}	    
	    			if(tempOldLSL != '') { 
       					var result=filterDoubleNumberonly(tempOldLSL);
     					if(result == false) {
       						alert("Old LSL must be data type : double!");
       						return false;
       					}
     				} else {
     					tempOldLSL = "EMPTY";
     				}
     				
					if (tempOldUSL != '') { 
       					var result=filterDoubleNumberonly(tempOldUSL);
     					if (result == false) {
       						alert("Old USL must be data type : double!");
       						return false;
       					}
     				} else { 
     					tempOldUSL = "EMPTY";
     				}
     				
     				if (tempUnit != "") {
     					if (!SpecialCharValid(tempUnit)) {
   			    			alert("Unit contains invalid characters ~ , | ,\" and \' ");
           					return false; 
           				} else {
           					if (tempUnit.length > 6) {
       							alert("Unit must not be more than six digits!");
       							return false;
      		 				} 
      					}
     				} else {	
     					tempUnit = "EMPTY";
     				} 
     
					if (tempNewLSL != '') { 
       					var result=filterDoubleNumberonly(tempNewLSL);
     					if (result == false) {
       						alert("New LSL must be data type : double!");
       						return false;
       					}
     				} else { 
     					tempNewLSL = "EMPTY";
     				}
	    
					if (tempNewUSL != '') { 
       					var result=filterDoubleNumberonly(tempNewUSL);
 						if (result == false) {
       						alert("New USL must be data type : double!");
       						return false;
						}
     				} else { 	
     					tempNewUSL = "EMPTY";
     				}
     
					if (tempTestComment != "") {	
						if (filterSpecialCharOnly(tempTestComment) == false) {
           					alert("Comment contains invalid characters ~ , | , \" and \' ");
            				return false;
       					} else {
       						if (tempTestComment.length > 255) {
       							alert("Comment must not be more than 255 characters!");
       							return false;
       						}
       					}
     				} else {
     					tempTestComment = "EMPTY";
     				}     

					if (tmpText == "") {
	    				tmpText = tempTestNo + "|" + tempNewFlag + "|" + tempOldLSL + "|" + tempOldUSL + "|" + tempUnit + "|" + tempNewLSL + "|" + tempNewUSL + "|" + tempTestComment + "~";
	    			} else {	    
	    				tmpText = tmpText + "|" + tempTestNo + "|" + tempNewFlag + "|" + tempOldLSL + "|" + tempOldUSL + "|" + tempUnit + "|" + tempNewLSL + "|" + tempNewUSL + "|" + tempTestComment + "~";
	    			}
				}
			}
	
  			document.addtpActionForm.tempAddRow.value = tmpText;
  			document.addtpActionForm.tempxferFileDir.value='<%=xferFileDir%>';
  			document.addtpActionForm.tempxferFile.value= document.tpActionForm.elements['FIELD.XFER_PATH'].value;
  			document.addtpActionForm.LINE_SET.value= document.tpActionForm.LINE_SET.value;
  			document.addtpActionForm.TESTER_FAMILY.value= document.tpActionForm.TESTER_FAMILY.value;
			ind = document.tpActionForm.elements['FIELD.DIS_EMAIL'].selectedIndex;
     		if (ind>=0) {
       			document.addtpActionForm.tempdisEmail.value=document.tpActionForm.elements['FIELD.DIS_EMAIL'].options[ind].text;
     		} else {
       			document.addtpActionForm.tempdisEmail.value='';
     		} 
 			document.addtpActionForm.tempJobname.value=document.tpActionForm.elements['FIELD.JOBNAME'].value;
 			document.addtpActionForm.tempjob_rel.value=document.tpActionForm.elements['FIELD.JOB_REL'].value;
   			document.addtpActionForm.tempjob_rev.value=document.tpActionForm.elements['FIELD.JOB_REV'].value;
     
        	var ind = document.tpActionForm.elements['FIELD.FACILITY'].selectedIndex;
    		if (ind>=0) {
       			document.addtpActionForm.tempfacility.value=document.tpActionForm.elements['FIELD.FACILITY'].options[ind].text;
    		} else {
       			document.addtpActionForm.tempfacility.value='';
     		}

     		ind = document.tpActionForm.elements['FIELD.TESTER_INFO'].selectedIndex;
     		if (ind>=0) {
       			document.addtpActionForm.temptesterInfo.value=document.tpActionForm.elements['FIELD.TESTER_INFO'].options[ind].text;
     		} else {
       			document.addtpActionForm.temptesterInfo.value='';
     		}

     		ind = document.tpActionForm.elements['FIELD.TO_PLANT'].selectedIndex;
     		if (ind>=0) {
       			document.addtpActionForm.temptoPlant.value=document.tpActionForm.elements['FIELD.TO_PLANT'].options[ind].text;
     		} else {
       			document.addtpActionForm.temptoPlant.value='';
     		}

     		ind = document.tpActionForm.elements['FIELD.PROD_LOGIN'].selectedIndex;
     		if (ind>=0) {
       			document.addtpActionForm.tempprod_login.value=document.tpActionForm.elements['FIELD.PROD_LOGIN'].options[ind].text;
     		} else {
       			document.addtpActionForm.tempprod_login.value='';
     		}
     
     		ind = document.tpActionForm.elements['FIELD.VALID_LOGIN'].selectedIndex;
     		if (ind>=0) {
       			document.addtpActionForm.tempvalid_login.value=document.tpActionForm.elements['FIELD.VALID_LOGIN'].options[ind].text;
     		} else {
      			document.addtpActionForm.tempvalid_login.value='';
     		}
     
     		ind = document.tpActionForm.elements['FIELD.EMAIL_TO'].selectedIndex;
     		if (ind>=0) {
       			document.addtpActionForm.tempemail_to.value=document.tpActionForm.elements['FIELD.EMAIL_TO'].options[ind].text;
     		} else {
       			document.addtpActionForm.tempemail_to.value='';
     		}
   
     		document.addtpActionForm.tempemail_cc.value=document.tpActionForm.elements['FIELD.EMAIL_CC'].value;
     		document.addtpActionForm.templine.value=document.tpActionForm.elements['FIELD.LINE'].value;

     		var prodAreaSelIndex=document.tpActionForm.elements['FIELD.AREA_PROD'].selectedIndex;
     		if (prodAreaSelIndex >= 0) {
    			document.addtpActionForm.elements['FIELD.AREA_PROD'].value=document.tpActionForm.elements['FIELD.AREA_PROD'].options[prodAreaSelIndex].value; 
    		} else {
         		document.addtpActionForm.elements['FIELD.AREA_PROD'].value = "";
     		}
			document.addtpActionForm.tempsingleDeliveryCmt.value=document.tpActionForm.elements['FIELD.SINGLEDELIVERYCMT'].value;
			document.addtpActionForm.tempdeliveryComm.value=document.tpActionForm.elements['FIELD.DELIVERYCOMM'].value;
			document.addtpActionForm.temphw_modification.value=document.tpActionForm.elements['FIELD.HW_MODIFICATION'].value;
			document.addtpActionForm.tempexp_avg_yv.value=document.tpActionForm.elements['FIELD.EXP_AVG_YV'].value;
			document.addtpActionForm.tempzero_yw.value=document.tpActionForm.elements['FIELD.ZERO_YW'].value;
			document.addtpActionForm.tempnew_testtime.value=document.tpActionForm.elements['FIELD.NEW_TESTTIME'].value;
			document.addtpActionForm.tempvalid_till.value=document.tpActionForm.elements['FIELD.VALID_TILL'].value;			
	 		var is_temp;
     		var ind99=document.tpActionForm.elements['FIELD.IS_TEMP'].selectedIndex;
     		if (ind99>=0) {
       			document.addtpActionForm.tempis_temp.value =document.tpActionForm.elements['FIELD.IS_TEMP'].options[ind99].value;
     		} else {
       			document.addtpActionForm.tempis_temp.value='';     
     		}  
 			document.addtpActionForm.submit();
		}
	}

	function NewAttribute() { 
		tmpText ="";
		var rowcount= 0;
  		for (i=0;i<document.tpActionForm.elements['FIELD.TEST_NO'].length;i++) {         
        	var tempTestNo = document.tpActionForm.elements['FIELD.TEST_NO'][i].value; 
	    	if (tempTestNo != "") {	
	    		rowcount = rowcount +1; 
	    		var tempTestNo = document.tpActionForm.elements['FIELD.TEST_NO'][i].value;   
	    		var tempNewFlag;
	     		var ind77=document.tpActionForm.elements['FIELD.NEW_FLAG'][i].selectedIndex;
	     		if (ind77>=0) {
	       			tempNewFlag =document.tpActionForm.elements['FIELD.NEW_FLAG'][i].options[ind77].value;
	     		} else {
	       			tempNewFlag='';
	      		}
	    		var tempOldLSL = document.tpActionForm.elements['FIELD.OLD_LSL'][i].value;
	    		var tempOldUSL = document.tpActionForm.elements['FIELD.OLD_USL'][i].value;
	    		var tempUnit = document.tpActionForm.elements['FIELD.UNIT'][i].value;
	    		var tempNewLSL = document.tpActionForm.elements['FIELD.NEW_LSL'][i].value;
	   			var tempNewUSL = document.tpActionForm.elements['FIELD.NEW_USL'][i].value;
	    		var tempTestComment = document.tpActionForm.elements['FIELD.TESTS_COMMENTS'][i].value;
	   	 		if (tempTestNo != "") {
	   	 			if (filterSpecialCharOnly(tempTestNo) == false) {
     			    	alert("Test # contains invalid characters ~ , | ,\" and \' ");
            			return false;
       				} else { 
       					if (tempTestNo.length > 32) {
       						alert("Test # must not be more than 32 characters!");
       						return false;
      		 			} 
      		 		}
     			}	    
	     
     			if (tempOldLSL != '') { 
       				var result=filterDoubleNumberonly(tempOldLSL);
     				if (result == false) {
     				    alert("Old LSL must be data type : double!");
       					return false;
       				}
     			} else { 
     				tempOldLSL = "EMPTY";
     			}

	 			if (tempOldUSL != '') { 
       				var result=filterDoubleNumberonly(tempOldUSL);
     				if (result == false) {
       					alert("Old USL must be data type : double!");
       					return false;
       				}
     			} else {
     				tempOldUSL = "EMPTY";
     			}
     
	   	 		if (tempUnit != "") {
	   	 			if (!SpecialCharValid(tempUnit)) {
     			    	alert("Unit contains invalid characters ~ , | , \" and \' ");
            			return false;
 	      			} else { 
 	      				if (tempUnit.length > 6) {
       						alert("Unit must not be more than 6 characters!");
       						return false;
      		 			} 
      				}
     			} else {
     				tempUnit = "EMPTY";
     			} 
     
				if (tempNewLSL != '') { 
       				var result=filterDoubleNumberonly(tempNewLSL);
     				if (result == false) {
       					alert("New LSL must be data type : double!");
       					return false;
       				}
     			} else {
     				tempNewLSL = "EMPTY";
     			}
	    
				if (tempNewUSL != '') { 
       				var result=filterDoubleNumberonly(tempNewUSL);
     				if (result == false) {
       					alert("New USL must be data type : double!");
       					return false;
    				}
     			} else {	
     				tempNewUSL = "EMPTY";
     			}
     
		     	if (tempTestComment != "") {	
	    	 		if (filterSpecialCharOnly(tempTestComment) == false) {
           				alert("Comment contains invalid characters ~ , | ,\" and \' ");
            			return false;
       				} else {
       					if (tempTestComment.length > 255) {
       						alert("Comment must not be more than 255 characters!");
       						return false;
       					}
       				}
     			} else {
     				tempTestComment = "EMPTY";
     			}
     
	    		if (tmpText == "") {
	    			tmpText = tempTestNo + "|" + tempNewFlag + "|" + tempOldLSL + "|" + tempOldUSL + "|" + tempUnit + "|" + tempNewLSL + "|" + tempNewUSL + "|" + tempTestComment + "~";
	    		} else {
	    			tmpText = tmpText + tempTestNo + "|" + tempNewFlag + "|" + tempOldLSL + "|" + tempOldUSL + "|" + tempUnit + "|" + tempNewLSL + "|" + tempNewUSL + "|" + tempTestComment + "~";
	    		}
	    	}
		}
  		document.tpActionForm.addRowValue.value = tmpText;
  		document.tpActionForm.testrowcount.value = rowcount;
     	return true;
	} 
   
    function getUsersData() {
    <%
   		int k=0;
      	for (int i=0; i<toPlantLst.size(); i++) {
          	String plant=(String)toPlantLst.elementAt(i);
          	try {
              	dbUsrLst=repsel.get("USER");
              	dbPlant=repsel.get("PLANT");
              	dbPlant.setVal(plant);
              	dbUsrLst.fetch();
          	} catch (Exception e) {}

            {%>
             	usersArray[ <%=i%> ] = new Array();
              	usersArray[ <%=i%> ][0] = '<%= plant %>';
              	usersArray[ <%=i%> ][1] = '';
              	usrEmailsArray[ <%=k%> ] = new Array();
              	usrEmailsArray[ <%=k%> ][0] = '<%= plant %>';
              	usrEmailsArray[ <%=k%> ][1] = '';

             <%
                if ((dbUsrLst!=null)&&(!dbUsrLst.isBlank())) {
                	Vector dbUsers=dbUsrLst.getVector();
                    for (int j=0; j<dbUsers.size(); j++) {%>
                    	usersArray[ <%=i%> ][ <%=j+2%> ] = '<%= dbUsers.elementAt(j) %>';
                        usrEmailsArray[ <%=k%> ][ <%=j+2%> ] = '<%= dbUsrLst.getAttrVal("email",j) %>';
                  <%}
                } %>
         <% }
          	k++;
       	} %>
    }
 	
    function getFacilities() {
       <%
    	for (int i=0; i<toPlantLst.size(); i++) {
        	String plant=(String)toPlantLst.elementAt(i);
            Vector facilityLst=FacilityMgr.getFacilityList(plant); %>
            facilityArray[<%=i%>] = new Array();
            facilityArray[<%=i%>][0] = '<%= plant %>';
            facilityArray[<%=i%>][1] = '';
       <% 	for (int j=0; j<facilityLst.size(); j++) {%>
            	facilityArray[<%=i%>][<%=j+2%>] = '<%=XmlUtils.getVal((Element)facilityLst.elementAt(j))%>';
       <%	}
        }	%>
    }

   	function getTesterInfos() {
    <%
    	for (int i=0; i<toPlantLst.size(); i++) {
        	String plant=(String)toPlantLst.elementAt(i);
            if (request.getAttribute("TESTER_FAMILY")==null) {
            	testerfam = XmlUtils.getVal(tpRec,"TESTERFAM");
            } else {
            	testerfam = (String)request.getAttribute("TESTER_FAMILY");
            }
              
            Vector tstInfoLst=TesterInfoMgr.getTesterInfoShowList(plant, testerfam); %>
            tstInfoArray[<%=i%>] = new Array();
            tstInfoArray[<%=i%>][0] = '<%= plant %>';
            tstInfoArray[<%=i%>][1] = '';

         <% for (int j=0; j<tstInfoLst.size(); j++) {%>
            	tstInfoArray[<%=i%>][<%=j+2%>] = '<%=XmlUtils.getVal((Element)tstInfoLst.elementAt(j))%>';
         <% }
        } %>
   	}

	function setProductionAreaForPlant( plant ) {
    	var selProdArea = "<%=productionArea%>";
        var prodarea = '<%= request.getAttribute("FIELD.AREA_PROD")!= null ? request.getAttribute("FIELD.AREA_PROD") : ""%>';
        if (prodarea != "") {
        	selProdArea = prodarea;
        }
        var plantProdAreas = productionAreas[plant];
        populateCombo ( document.tpActionForm.elements['FIELD.AREA_PROD'], plantProdAreas, selProdArea, 0, 1, true ) ;
	}
	
   	function setUsersForPlant(plant) {
    	for (var i=0; i < usersArray.length; i++) {
       		if (usersArray[i][0]==plant) break;
     	}
     	var box1 = this.document.tpActionForm.elements['FIELD.VALID_LOGIN'];
     	var box2 = this.document.tpActionForm.elements['FIELD.PROD_LOGIN'];
     	while (box1.options.length > 0) box1.options[0] = null;
     	while (box2.options.length > 0) box2.options[0] = null;
     	for (j=1; j < usersArray[i].length; j++) {
      		box1.options[j-1] = new Option(usersArray[i][j],usersArray[i][j]);
       		box2.options[j-1] = new Option(usersArray[i][j],usersArray[i][j]);
        }
   	}
 
   	function setUsersEmailForPlant(plant) {
    	for (i=0; i < usrEmailsArray.length; i++) {
       		if (usrEmailsArray[i][0]==plant) break;
     	}

     	var box = this.document.tpActionForm.elements['FIELD.EMAIL_TO'];
     	while (box.options.length > 0) box.options[0] = null;

     	for (j=1; j < usrEmailsArray[i].length; j++) {
       		box.options[j-1] = new Option(usrEmailsArray[i][j],usrEmailsArray[i][j]);
     	}
   	}

   	function setTstInfoForPlant(plant) {
    	for (i=0; i < tstInfoArray.length; i++) {
       		if (tstInfoArray[i][0]==plant) break;
     	}
    	var box = this.document.tpActionForm.elements['FIELD.TESTER_INFO'];

     	while (box.options.length > 0) box.options[0] = null;
     	for (j=1; j < tstInfoArray[i].length; j++) {
       		box.options[j-1] = new Option(tstInfoArray[i][j],tstInfoArray[i][j]);
     	}
   	}

    function setFacilityForPlant(plant) {
    	for (i=0; i < facilityArray.length; i++) {
       		if (facilityArray[i][0]==plant) break;
     	}
     	var box = this.document.tpActionForm.elements['FIELD.FACILITY'];
     	while (box.options.length > 0) box.options[0] = null;
     	for (j=1; j < facilityArray[i].length; j++) {
       		box.options[j-1] = new Option(facilityArray[i][j],facilityArray[i][j]);
     	}
   	}
	
    function changeDestPlant() {
    	var ind = document.tpActionForm.elements['FIELD.TO_PLANT'].selectedIndex;
     	var plant = document.tpActionForm.elements['FIELD.TO_PLANT'].options[ind].text;
     	setTstInfoForPlant(plant);
     	setFacilityForPlant(plant);
     	setUsersForPlant(plant);
     	setUsersEmailForPlant(plant);
     	setProductionAreaForPlant(plant);
   	}
	
    function showTesterInfos() {
    	for (i=0; i < tstInfoArray.length; i++) {
      		for (j=0; j < tstInfoArray[i].length; j++) {
         		alert(tstInfoArray[i][j]);
      		}
    	}
   	}

    function showFacility() {
   		for (i=0; i < facilityArray.length; i++) {
      		for (j=0; j < facilityArray[i].length; j++) {
         		alert(facilityArray[i][j]);
      		}
    	}
   	}

    function changeDir() {
    	document.changeDirForm.jobname.value=document.tpActionForm.elements['FIELD.JOBNAME'].value;
     	document.changeDirForm.job_rel.value=document.tpActionForm.elements['FIELD.JOB_REL'].value;
     	document.changeDirForm.job_rev.value=document.tpActionForm.elements['FIELD.JOB_REV'].value;
     
    	var ind = document.tpActionForm.elements['FIELD.FACILITY'].selectedIndex;
     	if (ind>=0) {
       		document.changeDirForm.facility.value=document.tpActionForm.elements['FIELD.FACILITY'].options[ind].text;
     	} else {
       		document.changeDirForm.facility.value='';
     	}

     	ind = document.tpActionForm.elements['FIELD.TESTER_INFO'].selectedIndex;
   		if (ind>=0) {
       		document.changeDirForm.testerInfo.value=document.tpActionForm.elements['FIELD.TESTER_INFO'].options[ind].text;
   		} else {
      		document.changeDirForm.testerInfo.value='';
    	}

     	ind = document.tpActionForm.elements['FIELD.TO_PLANT'].selectedIndex;
     	if (ind>=0) {
       		document.changeDirForm.toPlant.value=document.tpActionForm.elements['FIELD.TO_PLANT'].options[ind].text;
     	} else {
       		document.changeDirForm.toPlant.value='';
     	}

     	//document.changeDirForm.prod_login.value=document.tpActionForm.elements['FIELD.PROD_LOGIN'].value;
     	//document.changeDirForm.valid_login.value=document.tpActionForm.elements['FIELD.VALID_LOGIN'].value;
     	//document.changeDirForm.email_to.value=document.tpActionForm.elements['FIELD.EMAIL_TO'].value;
     	//start FP 15-10-04 release 4.4
     	
     	ind = document.tpActionForm.elements['FIELD.PROD_LOGIN'].selectedIndex;
     	if (ind>=0) {
       		document.changeDirForm.prod_login.value=document.tpActionForm.elements['FIELD.PROD_LOGIN'].options[ind].text;
     	} else {
       		document.changeDirForm.prod_login.value='';
     	}
     	
     	ind = document.tpActionForm.elements['FIELD.VALID_LOGIN'].selectedIndex;
     	if (ind>=0) {
       		document.changeDirForm.valid_login.value=document.tpActionForm.elements['FIELD.VALID_LOGIN'].options[ind].text;
     	} else {
       		document.changeDirForm.valid_login.value='';
     	}
     	
     	ind = document.tpActionForm.elements['FIELD.EMAIL_TO'].selectedIndex;
     	if (ind>=0) {
      		document.changeDirForm.email_to.value=document.tpActionForm.elements['FIELD.EMAIL_TO'].options[ind].text;
     	} else {
       		document.changeDirForm.email_to.value='';
     	}
     	document.changeDirForm.email_cc.value=document.tpActionForm.elements['FIELD.EMAIL_CC'].value;
     	document.changeDirForm.line.value=document.tpActionForm.elements['FIELD.LINE'].value;

     	var prodAreaSelIndex=document.tpActionForm.elements['FIELD.AREA_PROD'].selectedIndex;
     	if (prodAreaSelIndex >= 0) {
    		document.changeDirForm.area_prod.value=document.tpActionForm.elements['FIELD.AREA_PROD'].options[prodAreaSelIndex].value; 
    	} else {
         	document.changeDirForm.area_prod.value = "";
     	}
		document.changeDirForm.singleDeliveryCmt.value=document.tpActionForm.elements['FIELD.SINGLEDELIVERYCMT'].value;
		document.changeDirForm.deliveryComm.value=document.tpActionForm.elements['FIELD.DELIVERYCOMM'].value;
		document.changeDirForm.hw_modification.value=document.tpActionForm.elements['FIELD.HW_MODIFICATION'].value;
		document.changeDirForm.exp_avg_yv.value=document.tpActionForm.elements['FIELD.EXP_AVG_YV'].value;
		document.changeDirForm.zero_yw.value=document.tpActionForm.elements['FIELD.ZERO_YW'].value;
		document.changeDirForm.new_testtime.value=document.tpActionForm.elements['FIELD.NEW_TESTTIME'].value;
		document.changeDirForm.valid_till.value=document.tpActionForm.elements['FIELD.VALID_TILL'].value;
		document.changeDirForm.is_temp.value=document.tpActionForm.elements['FIELD.IS_TEMP'].value;
		var testlen =document.tpActionForm.elements['FIELD.TEST_NO'].length;
		document.changeDirForm.testcount.value=parseInt(testlen) -1;
     	ind = document.tpActionForm.elements['FIELD.DIS_EMAIL'].selectedIndex;
     	if (ind>=0) {
       		document.changeDirForm.dis_email.value=document.tpActionForm.elements['FIELD.DIS_EMAIL'].options[ind].text;
     	} else {
       		document.changeDirForm.dis_email.value='';
     	}        	
  		/*
     	if (document.tpActionForm.elements['FIELD.CERTIFICATION'].checked)
     	{
        	document.changeDirForm.certification.value="check";
     	} else {
        	document.changeDirForm.certification.value="no-check";
     	}
     	*/
     	document.changeDirForm.submit();
   	}

	function submitAction() {
   		if (!controlData()) return;
      	if(!NewAttribute()) return;
      	
     /*if (document.tpActionForm.elements['FIELD.CERTIFICATION'].checked)
     {
        document.tpActionForm.elements['FIELD.CERTIFICATION'].value="check";
     }else{
        document.tpActionForm.elements['FIELD.CERTIFICATION'].value="no-check";
     }*/
   
     	var objForm = document.tpActionForm;
     	var confirmationMessage = "The current TP will be delivered,\nafter that you can't change following fields:\n";
     	confirmationMessage += "\n- Job name = " + objForm.elements['FIELD.JOBNAME'].value;
     	confirmationMessage += "\n- Job release = " + objForm.elements['FIELD.JOB_REL'].value;
     	var destinationPlantIndex = objForm.elements['FIELD.TO_PLANT'].selectedIndex;
     	var destPlant = "";
     	if (destinationPlantIndex >= 0) {
       		destPlant = objForm.elements['FIELD.TO_PLANT'].options[destinationPlantIndex].text;
     	}
     	confirmationMessage += "\n- Destination plant = " + destPlant;
     	confirmationMessage += "\n- Prod line = " + objForm.elements['FIELD.LINE'].value;
     	confirmationMessage += "\n- Xfer path = " + objForm.elements['FIELD.XFER_PATH'].value;
     	confirmationMessage += "\n\nDo you want proceed?";
     	if (confirm(confirmationMessage)) {
 			document.tpActionForm.submit();
     	}
   	}

    function controlJobName() {
    	var notValidCharPos = filterUserInputAphaNumericalChars(document.tpActionForm.elements['FIELD.JOBNAME']);
        if (notValidCharPos != -1) {
            alert("The field Job Name invalid characters\nOnly alphanumeric chars ('A..Z' and '0..9') and underscore ('_') are allowed");
            return true;
        }
        return false;
    }

    function controlFinalJobName() {
    	var jobName = document.tpActionForm.elements['FIELD.JOBNAME'].value;
     	var length = jobName.length;
     	var finalCar = jobName.substring(length-1);

     	for (var i=0; i<=9; i++) {
        	if (finalCar == i) {
            	return true;
        	}
     	}
     	return false;
   	}

    function controlJobRev() {
    	var objJobRevisionField = document.tpActionForm.elements['FIELD.JOB_REV'];
     	var jobRevision = objJobRevisionField.value;
     	var notAllowedCharPos = filterUserInputNumbersOnly(objJobRevisionField);
     	if (notAllowedCharPos >= 0) {
        	return true;
     	}
     	return false;
    }
   //controllo numerico su JobRelease
	function controlJobRel() {
    	if (document.tpActionForm.elements['FIELD.JOB_REL'] != null) {
        	var notAllowedCharPos = filterUserInputNumbersOnly(document.tpActionForm.elements['FIELD.JOB_REL']);
           	if (notAllowedCharPos >= 0) {
               	return true;
           	}
        }
       	return false;
    }

    function controlLine() {
    	if (document.tpActionForm.elements['FIELD.LINE'] != null) {
        	var notAllowedCharPos = filterUserInputAphaNumericalChars(document.tpActionForm.elements['FIELD.LINE']);
            if (notAllowedCharPos >= 0) {
                return true;
            }
        }
        return false;
    }

    function controlCCEmail() {
    	if (document.tpActionForm.elements['FIELD.EMAIL_CC'] != null) {
            var notAllowedCharPos = filterUserInputRelaxedAphaNumericalChars(document.tpActionForm.elements['FIELD.EMAIL_CC']);
            if (notAllowedCharPos >= 0) {
                return true;
            }
        }
        return false;
    }

    function controlData() { 
    	var jobName = document.tpActionForm.elements['FIELD.JOBNAME'].value;
     	var jobRelease = document.tpActionForm.elements['FIELD.JOB_REL'].value;
     	var jobRevision = document.tpActionForm.elements['FIELD.JOB_REV'].value;
      	var singleDeliveryCmt = document.tpActionForm.elements['FIELD.SINGLEDELIVERYCMT'].value;
     	var deliveryComm = document.tpActionForm.elements['FIELD.DELIVERYCOMM'].value;
     	var hw_modification = document.tpActionForm.elements['FIELD.HW_MODIFICATION'].value;
     	var exp_avg_yv = document.tpActionForm.elements['FIELD.EXP_AVG_YV'].value;
     	var zero_yw = document.tpActionForm.elements['FIELD.ZERO_YW'].value;
     	var new_testtime = document.tpActionForm.elements['FIELD.NEW_TESTTIME'].value;
     	var valid_till = document.tpActionForm.elements['FIELD.VALID_TILL'].value;
     	var arrTestNo = document.tpActionForm.elements['FIELD.TEST_NO'].value;
   	 	var is_temp;
     	var ind77=document.tpActionForm.elements['FIELD.IS_TEMP'].selectedIndex;
     	if (ind77>=0) {
       		is_temp =document.tpActionForm.elements['FIELD.IS_TEMP'].options[ind77].value;
     	} else {
       		is_temp='';
       	}
     	var line = document.tpActionForm.elements['FIELD.LINE'].value;
     	var xferFile = document.tpActionForm.elements['FIELD.XFER_PATH'].value;
     	var xferFileDir = '<%=xferFileDir%>';
     	var x ='<%=listRepFileName%>';
     	var facility;
     	var ind=document.tpActionForm.elements['FIELD.FACILITY'].selectedIndex;
     	if (ind>=0) {
       		facility =document.tpActionForm.elements['FIELD.FACILITY'].options[ind].text;
     	} else {
       		facility='';
     	}

     	var destPlant;
     	var ind1=document.tpActionForm.elements['FIELD.TO_PLANT'].selectedIndex;
     	if (ind1>=0) {
       		destPlant =document.tpActionForm.elements['FIELD.TO_PLANT'].options[ind1].text;
     	} else {
      		destPlant='';
     	}

     	var testInfo;
     	var ind2=document.tpActionForm.elements['FIELD.TESTER_INFO'].selectedIndex;
     	if (ind2>=0) {
       		testInfo =document.tpActionForm.elements['FIELD.TESTER_INFO'].options[ind2].text;
     	} else {
       		testInfo='';
     	}

     	var validLogin;
     	var ind3=document.tpActionForm.elements['FIELD.VALID_LOGIN'].selectedIndex;
     	if (ind3>=0) {
       		validLogin =document.tpActionForm.elements['FIELD.VALID_LOGIN'].options[ind3].text;
     	} else {
       		validLogin='';
     	}

     	var prodLogin;
     	var ind4=document.tpActionForm.elements['FIELD.PROD_LOGIN'].selectedIndex;
     	if (ind4>=0) {
       		prodLogin =document.tpActionForm.elements['FIELD.PROD_LOGIN'].options[ind4].text;
     	} else {
       		prodLogin='';
     	}
     	
     	var productionArea = '';
     	var prodAreaSelIndex=document.tpActionForm.elements['FIELD.AREA_PROD'].selectedIndex;
     	
     	if (prodAreaSelIndex > 0) {
       		productionArea = document.tpActionForm.elements['FIELD.AREA_PROD'].options[prodAreaSelIndex].value;
     	} else {
       		productionArea = '';
     	}
     	if (jobName == '') {
        	alert('JOBNAME is mandatory!');
        	return false;
     	}
     	if (jobRelease == '') {
        	alert('JOB RELEASE is mandatory!');
        	return false;
     	}
     	if (jobRevision == '') {
        	alert('JOB REVISION is mandatory!');
        	return false;
     	}
     	if (line == '') {
        	alert('LINE is mandatory!');
        	return false;
     	}
     	if (destPlant == '') {
        	alert('DESTINATION PLANT is mandatory!');
        	return false;
     	}
     	if (facility == '') {
        	alert('PROCESS STAGE is mandatory!');
        	return false;
    	}
     	if(testInfo == '') {
        	alert('TESTER INFO is mandatory!');
        	return false;
     	}
     
    <%  if (okDbConnection) {
     //if the db connection is not valid skip the check on mandatory valid and prod login %>
     		if (validLogin == '') {
        		alert('VALID LOGIN is mandatory!');
        		return false;
     		}
     		if(prodLogin == '') {
        		alert('PROD LOGIN is mandatory!');
        		return false;
     		}
     <% } %>
        if (xferFileDir == '') {
        	alert('XFER FILE is mandatory!');
        	return false;
  		}
  		
  		if (singleDeliveryCmt != '') { 	
			if (filterSpecialChar(singleDeliveryCmt) == false) {
     			alert("Delivery Comments contains invalid characters ~ , | ,\" and \' \nDelivery Comments must not be more than 2048 characters!");
         		return false;
      		} 
		}
  		
		if (deliveryComm != '') { 	
			if (filterSpecialChar(deliveryComm) == false) {
     			alert("Race Comments contains invalid characters ~ , | ,\" and \' \nRace Comments must not be more than 2048 characters!");
         		return false;
      		} 
		}

     	if (hw_modification != '') {
     		if (filterSpecialChar(hw_modification) == false) {
           		alert("HW Modifications contain invalid characters ~ , | ,\" and \' \nHW Modifications must not be more than 2048 characters!");
            	return false;
       		} 
     	}

    	if (exp_avg_yv != '') { 
       		//var result=filterDoubleNumberLen(exp_avg_yv);
       		//if (result == false) {
       		if ((parseFloat(exp_avg_yv) > 99.99 || parseFloat(exp_avg_yv) < -99.99 || isNaN(exp_avg_yv))||((String(exp_avg_yv).indexOf(".")) > 0)&& (String(exp_avg_yv).indexOf(".") < String(exp_avg_yv).length - 3)){
        		alert("Expected Average Yield Variation should be between -99.99 to 99.99 inclusively!");
       			return false;
       		}
     	}
    
      	if (zero_yw != '') { 
      		var result=filterDoubleNumberLen(zero_yw);
       		if (result == false) {
       			alert("Zero Yield Wafer should be between 0.0 to 99.99 inclusively!");
       			return false;
       		}
     	}
   
 		if(new_testtime != '') { 
 			var spChar = "."; 
 			if (new_testtime.indexOf(spChar) != -1) {
	  			alert("New Test Time contains invalid character..dot!");
     			return false;
     		}
       		var result=filterIntegerNumberLen(new_testtime);
       		if (result == false) {
       			alert("New Test Time contains invalid characters:\nOnly number are allowed ('0..9')");
       			return false;
       		}
     	}

     	if (controlJobName()) {
        	return false;
     	}
     	if (controlFinalJobName()) {
        	if (!confirm('Last part of typed Job name is a number.\nAre you sure that RELEASE NB is into JOB RELEASE field?')) {
            	return false;
        	}
     	}
     	if (controlJobRel()) {
        	alert("Job release contains invalid characters:\nOnly number are allowed ('0..9')");
        	return false;
     	}

     	if (controlJobRev()) {
        	alert("Job revision contains invalid characters:\nOnly number are allowed ('0..9')");
        	return false;
     	}
     	if (controlLine()) {
         	alert("Line contains invalid characters:\nOnly alphanumeric chars ('A..Z' and '0..9') and underscore ('_') are allowed");
         	return false;
     	}
     	if (controlCCEmail()){
         	alert("CC email contains invalid characters:\nOnly alphanumeric chars ('A..Z' and '0..9'), underscore ('_'), at ('@'), dot ('.'), space (' ') and comma (',') are allowed");
         	return false;
     	}
     	if (productionArea == '') {
         	alert("Production Area is mandatory!");
         	return false;
     	}
     	return true;
    }

</SCRIPT>
</HEAD>

<BODY bgColor="#FFFFFF" onLoad="document.tpActionForm.elements['FIELD.JOBNAME'].focus()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="NEW TP DELIVERY FORM"; %>
<%@ include file="top.jsp"%>
<TR>
	<TD ALIGN="LEFT">
	<table cellpadding="0" cellspacing="0" border="0" width="560">
		<tr>
			<td width="4"><img src="<%=contextPath%>/img/tit_sx.gif" width="4" height="18" alt="" border="0"></td>
			<td background="<%=contextPath%>/img/tit_center.gif" align="center" class="titverdi" nowrap><b><!-- FORM TITLE-->NEW TP DATA</b></td>
			<td width="4"><img src="<%=contextPath%>/img/tit_dx.gif" width="4" height="18" alt="" border="0"></td>
			<td><img src="<%=contextPath%>/img/pix.gif" width="453" height="18" alt="" border="0"></td>
		</tr>
		<tr>
			<td colspan="4"><img src="<%=contextPath%>/img/pix_grey.gif" width="560" height="1" alt="" border="0"></td>
		</tr>
	</table>
	</TD>
</TR>

<TR>
	<TD ALIGN="LEFT"><!-- FORM -->
	<FORM name="changeDirForm" action="<%=contextPath%>/navDirServlet"
		method="post"><INPUT TYPE="HIDDEN" name="pageTitle"
		value="NEW TP DELIVERY FORM -> XFER FILE SELECTION"> 
		<INPUT TYPE="HIDDEN" name="nextPage" value="sel_file.jsp"> 
		<INPUT TYPE="HIDDEN" name="outPage" value="tp_submit_form.jsp"> 
		<INPUT TYPE="HIDDEN" name="curDirPath" value="<%= lsBaseDir %>"> 
		<!-- INITIAL DIRECTORY SPECIFICATION -->
		<INPUT TYPE="HIDDEN" name="nextDirPath" value="<%= (request.getParameterValues("curDirPath")!=null ? request.getParameterValues("curDirPath")[0] : lsBaseDir) %>">
		<!-- /INITIAL DIRECTORY SPECIFICATION --> 
		<INPUT TYPE="HIDDEN" name="initDirPath" value="HOME_DIR"> 
		<INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%=listRepFileName%>">
	    <INPUT TYPE="HIDDEN" NAME="jobname" VALUE=""> 
	    <INPUT TYPE="HIDDEN" NAME="job_rel" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="job_rev" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="job_ver" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="ownerEmail" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="facility" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="testerInfo" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="toPlant" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="line" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="valid_login" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="prod_login" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="email_to" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="email_cc" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="certification" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="area_prod" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="singleDeliveryCmt" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="deliveryComm" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="hw_modification" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="exp_avg_yv" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="zero_yw" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="new_testtime" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="is_temp" VALUE="">  
		<INPUT TYPE="HIDDEN" NAME="valid_till" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="testrowcount">  
		<INPUT TYPE="HIDDEN" NAME="Addrowcount"> 
		<INPUT TYPE="HIDDEN" NAME="testcount" value=""> 
		<INPUT TYPE="HIDDEN" NAME="dis_email" VALUE=""> 
		<!-- INPUT TYPE="HIDDEN" NAME="addRowValue" VALUE=""--> 
		<INPUT TYPE="HIDDEN" NAME="testno" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="new_flag" VALUE="">
	</FORM>
	
	<FORM name="addtpActionForm" action="tpActionServlet" method="post">
		<INPUT TYPE="HIDDEN" NAME="action" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="deletedRow" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%=listRepFileName %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= session.getAttribute("vob") %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.JOBNAME" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.JOB_REL" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.JOB_REV" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.JOB_VER" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.FACILITY" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.TESTERFAM" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.XFER_PATH" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.FROM_PLANT" VALUE="<%= XmlUtils.getVal(tpRec,"FROM_PLANT") %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.LINESET" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.LASTBASELINE" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_LOGIN" VALUE="<%= owner %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_EMAIL" VALUE="<%= XmlUtils.getVal(tpRec,"OWNER_EMAIL") %>"> 
		<INPUT TYPE="HIDDEN" NAME="testrowcount"> 
		<INPUT TYPE="HIDDEN" NAME="Addrowcount"> 
		<INPUT TYPE="HIDDEN" NAME="testcount"> 
		<INPUT TYPE="HIDDEN" NAME="tempJobname" VALUE=""> 
	 	<INPUT TYPE="HIDDEN" NAME="tempjob_rel" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempjob_rev" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempjob_ver" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempfacility" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="temptesterInfo" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="temptoPlant" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="templine" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempvalid_login" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempprod_login" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempemail_to" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempemail_cc" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.AREA_PROD" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempsingleDeliveryCmt" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempdeliveryComm" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="temphw_modification" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempexp_avg_yv" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempzero_yw" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempnew_testtime" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempis_temp" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempvalid_till" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempxferFileDir" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempxferFile" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempAddRow" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="templineset" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="temptesterfam" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempdisEmail" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="LINE_SET" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="TESTER_FAMILY" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="ROWNO" VALUE="">
	</FORM>

	<FORM name="tpActionForm" action="<%=contextPath%>/tpActionServlet" method="POST">
		<INPUT TYPE="HIDDEN" NAME="listRepFileName" VALUE="<%=listRepFileName%>"> 
		<INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="Y"> 
		<INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_submit"> 
		<INPUT TYPE="HIDDEN" NAME="workDir" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="fromEmail" VALUE="<%= XmlUtils.getVal(tpRec,"OWNER_EMAIL") %>">
		<INPUT TYPE="HIDDEN" NAME="toEmail" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="ccEmail" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="singleDeliveryCmt" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="deliveryComm" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="hw_modification" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="exp_avg_yv" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="zero_yw" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="new_testtime" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="is_temp" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="valid_till" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.TEST_NO" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_FLAG" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.OLD_LSL" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.OLD_USL" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.UNIT" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_LSL" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_USL" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.TESTS_COMMENTS" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="testrowcount" value=""> 	 	
		<INPUT TYPE="HIDDEN" NAME="Addrowcount" value=""> 
		<INPUT TYPE="HIDDEN" NAME="testcount" Value=""> 
		<INPUT TYPE="HIDDEN" NAME="disEmail" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="addRowValue" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.LASTBASELINE" VALUE="<%= XmlUtils.getVal(tpRec,"LASTBASELINE") %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.LINESET" VALUE="<%= XmlUtils.getVal(tpRec,"LINESET") %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.JOB_VER" VALUE="<%="0" %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.FROM_PLANT" VALUE="<%= XmlUtils.getVal(tpRec,"FROM_PLANT") %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.VOB" VALUE="<%= XmlUtils.getVal(tpRec,"VOB") %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.EMAIL_FREE" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_EMAIL" VALUE="<%= XmlUtils.getVal(tpRec,"OWNER_EMAIL") %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.OWNER_LOGIN" value="<%= owner %>"> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.STATUS" VALUE="Submitted"> <% GregorianCalendar cal = new GregorianCalendar(); %>
		<INPUT TYPE="HIDDEN" NAME="FIELD.DATE" VALUE="<%= cal.get(Calendar.YEAR) %>.<%= (cal.get(Calendar.MONTH)<9 ? "0" : "") %><%= cal.get(Calendar.MONTH)+1 %>.<%= (cal.get(Calendar.DATE)<10 ? "0" : "") %><%= cal.get(Calendar.DATE) %>-<%= dateRd.getCurDateTime().substring(9).replace(':','.').trim() %>">

		<table cellspacing=0 cellpadding=0 width="70%" border=0>
			<tbody>
			<!-- INTER ROWS SPACE -->
			<!--<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif"  border=0></td></tr>-->
			<!-- /INTER ROWS SPACE -->

			<!-- FIELDS ROW: -->
			<tr>

				<!-- LEFT COLUMN SEPARATOR -->
				<!-- td>&nbsp;</td-->
				<!-- /LEFT COLUMN SEPARATOR -->

				<td class=testo width="40%"><b>Lineset name </b><br>
				<%
                	if (request.getAttribute("LINE_SET")==null) {
                    	lineset = XmlUtils.getVal(tpRec,"LINESET");
                    } else {
                       	lineset = (String)request.getAttribute("LINE_SET");
                    } %><%= lineset%><input type="hidden" name="LINE_SET" value="<%= lineset %>">
                </td>

				<!-- COLUMN SEPARATOR -->
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<!-- /COLUMN SEPARATOR -->
				<td class=testo width="40%"><b>Tester family</b><br>
				<%
                    if(request.getAttribute("TESTER_FAMILY")==null){
                       	testerfam = XmlUtils.getVal(tpRec,"TESTERFAM");
                    } else {
                       testerfam = (String)request.getAttribute("TESTER_FAMILY");
                    } %> <%= testerfam%>
                    <input type="hidden" name="TESTER_FAMILY" value="<%= testerfam %>">
                </td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
			<% 	String tempJobname = request.getAttribute("tempJobname")!=null ? (String)request.getAttribute("tempJobname") : ""; 
               	if (!tempJobname.equals("")) {
                	jobname = tempJobname; 
                }%>
				<td class=testo width="40%"><b>Job name (PRIS)*</b><br>
					<input class="txt" maxlength="255" size="10" name="FIELD.JOBNAME" value="<%=jobname%>">
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			 <% String tempjob_rel = request.getAttribute("tempjob_rel")!=null ? (String)request.getAttribute("tempjob_rel") : ""; 
                if (!tempjob_rel.equals("")) {
                	job_rel = tempjob_rel; 
                }%>
				<td class=testo width="30%"><b>Job release (PRIS)*</b><br>
					<input class="txt" maxlength="100" size="10" name="FIELD.JOB_REL" value="<%=job_rel%>">
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
		<%  	String tempjob_rev = request.getAttribute("tempjob_rev")!=null ? (String)request.getAttribute("tempjob_rev") : ""; 
                if(!tempjob_rev.equals("")) {
                	job_rev = tempjob_rev; 
                } %>
				<td class=testo width="40%"><b>Job revision *</b><br>
				<input class="txt" maxlength="2" size="10" name="FIELD.JOB_REV" value="<%=job_rev%>"></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			 <% String templine = request.getAttribute("templine")!=null ? (String)request.getAttribute("templine") : ""; 
                if (!templine.equals("")) {
                	line = templine; 
                }%>
				<td class=testo width="40%"><b>Product line *</b><br>
					<input class="txt" maxlength="100" size="10" name="FIELD.LINE" value="<%=line%>">
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
			<%  String temptoPlant = request.getAttribute("temptoPlant")!=null ? (String)request.getAttribute("temptoPlant") : ""; 
                if (!temptoPlant.equals("")) {
                	toPlant = temptoPlant; 
                }%>
				<td class=testo width="30%"><b>Destination Plant*</b><br>
					<select class="tendina" size="1" name="FIELD.TO_PLANT" onChange="changeDestPlant()"> &nbsp;
					<%  for (int i=0; i<toPlantLst.size(); i++) {%>
							<option <%= (((String)toPlantLst.elementAt(i)).equals(toPlant) ? "selected" : "") %>>
							<%= toPlantLst.elementAt(i)%>
    				<%  }  %>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			<%  String temparea_prod = request.getAttribute("FIELD.AREA_PROD")!=null ? (String)request.getAttribute("FIELD.AREA_PROD") : ""; 
                if(!temparea_prod.equals("")) {
                	productionArea = temparea_prod;
                }%>
				<td class=testo width="30%"><b><nobr>Production Area *</nobr></b><br>
					<select class="tendina" size="1" name="FIELD.AREA_PROD" value="<%=productionArea%>"></select>
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
			 <% String tempfacility = request.getAttribute("tempfacility")!=null ? (String)request.getAttribute("tempfacility") : ""; 
        	    if (!tempfacility.equals("")) {
                    facility = tempfacility; 
                }%>
				<td class=testo width="40%"><b><nobr>Process stage *</nobr></b><br>
					<select class="tendina" size="1" name="FIELD.FACILITY">
				<%  Vector facilitylst=FacilityMgr.getFacilityList(toPlant);
                    for (int i=0; i<facilitylst.size(); i++) {
                    	boolean selBool=(!facility.equals("") ? facility.equals( XmlUtils.getVal((Element)facilitylst.elementAt(i))) : (i==0)); %>
						<option <%= (selBool ? "selected" : "") %>><%=XmlUtils.getVal((Element)facilitylst.elementAt(i))%>
				  <%} %>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			<%  String temptesterInfo = request.getAttribute("temptesterInfo")!=null ? (String)request.getAttribute("temptesterInfo") : ""; 
 	            if(!temptesterInfo.equals("")) {
                	testerInfo = temptesterInfo;  
                } %>
				<td class=testo width="30%"><b>Tester info *</b><br>
					<select class="tendina" name="FIELD.TESTER_INFO">
				<%	if(request.getAttribute("TESTER_FAMILY")==null) {
                		testerfam = XmlUtils.getVal(tpRec,"TESTERFAM");
                	} else {
                    	testerfam = (String)request.getAttribute("TESTER_FAMILY");
               	    }
                	Vector tstInfos=TesterInfoMgr.getTesterInfoShowList(toPlant, testerfam);
                	for (int i=0; i<tstInfos.size(); i++) {
                		boolean selBool=(!testerInfo.equals("") ? testerInfo.equals( XmlUtils.getVal((Element)tstInfos.elementAt(i))) : (i==0)); %>
						<option <%= (selBool ? "selected" : "") %>><%=XmlUtils.getVal((Element)tstInfos.elementAt(i))%></option>
				<%  }  %>
					</select>
				</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
			<%	String tempprod_login = request.getAttribute("tempprod_login")!=null ? (String)request.getAttribute("tempprod_login") : ""; 
                if (!tempprod_login.equals("")) {
                	prod_login = tempprod_login; 
                } %>
				<td class=testo width="40%"><b>Prod Login <%=(okDbConnection) ? "*" : ""%></b><br>
					<select class="tendina" size="1" name="FIELD.PROD_LOGIN">
						<option value=""></option>
				<%  	try {
                			dbPlant.setVal(toPlant);
                    		dbUsrLst.fetch();
                		} catch(Exception e) { }
                		if ((dbUsrLst!=null)&&(!dbUsrLst.isBlank())) {
               	 			Vector dbUsers=dbUsrLst.getVector();
                    		for (int j=0; j<dbUsers.size(); j++) {
                    			boolean selBool=(!prod_login.equals("") && prod_login.equals(dbUsers.elementAt(j))); %>
								<option <%= (selBool ? "selected" : "") %>><%= dbUsers.elementAt(j) %></option>
						<%  }
                		} %>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
		 	<%  String tempvalid_login = request.getAttribute("tempvalid_login")!=null ? (String)request.getAttribute("tempvalid_login") : ""; 
                if (!tempvalid_login.equals("")) {
                	valid_login = tempvalid_login; 
                }%>
				<td class=testo width="40%"><b>Valid Login <%=(okDbConnection) ? "*" : ""%></b><br>
					<select class="tendina" size="1" name="FIELD.VALID_LOGIN">
						<option value=""></option>
					<%  try {
                     		dbPlant.setVal(toPlant);
                            dbUsrLst.fetch();
                        } catch(Exception e) { }
                        if ((dbUsrLst!=null)&&(!dbUsrLst.isBlank())) {
                        	Vector dbUsers=dbUsrLst.getVector();
                            for (int j=0; j<dbUsers.size(); j++) {
                            	boolean selBool=(!valid_login.equals("") && valid_login.equals(dbUsers.elementAt(j))); %>
						<option <%= (selBool ? "selected" : "") %>><%= dbUsers.elementAt(j) %></option>
						<%  }
                        } %>
					</select>
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
		<%  	String tempemail_to = request.getAttribute("tempemail_to")!=null ? (String)request.getAttribute("tempemail_to") : ""; 
                if (!tempemail_to.equals("")) {
                	email_to = tempemail_to; 
                }%>
				<td class=testo width="40%"><b>To Email </b><br>
					<select class="tendina" size="1" name="FIELD.EMAIL_TO">
						<option value=""></option>
				<%  	try {
                    		dbPlant.setVal(toPlant);
                        	dbUsrLst.fetch();
                    	} catch(Exception e) { }
                    	if ((dbUsrLst!=null)&&(!dbUsrLst.isBlank())) {
                    		Vector dbUsers = dbUsrLst.getVector();
                        	//VectorUtils.sortAlphabeticallyVectorOfStrings(dbUsers);
                        	for (int j=0; j<dbUsers.size(); j++) {
                        		boolean selBool=(!email_to.equals("") && email_to.equals(dbUsrLst.getAttrVal("email", j))); %>
								<option <%= (selBool ? "selected" : "") %>><%= dbUsrLst.getAttrVal("email",j) %></option>
					  	<%	}
                    	} %>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			<%  String tempemail_cc = request.getAttribute("tempemail_cc")!=null ? (String)request.getAttribute("tempemail_cc") : ""; 
                if (!tempemail_cc.equals("")) {
                	email_cc = tempemail_cc;
                }%>
				<td class=testo width="40%"><b>CC Email </b><br>
					<input class="txt" size="10" name="FIELD.EMAIL_CC" value="<%=email_cc%>">
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
		<% 		String tempxferFileDir = request.getAttribute("tempxferFileDir")!=null ? (String)request.getAttribute("tempxferFileDir") : ""; 
                if (!tempxferFileDir.equals("")) {
                	xferFileDir = tempxferFileDir; 
                }
                String tempxferFile = request.getAttribute("tempxferFile")!=null ? (String)request.getAttribute("tempxferFile") : ""; 
                if (!tempxferFile.equals("")) {
                	xferPathToSend = tempxferFile; 
                }%>
				<td class=testo width="40%" valign="top" colspan="4"><b>Xfer path *</b><br> <%= xferFileDir %> 
					<input type="hidden" name="FIELD.XFER_PATH" value="<%= xferPathToSend %>">
				</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr>
				<td align="right">
					<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
					<TR>
						<TD valign="center" align="right"><IMG alt="" SRC="<%=contextPath%>/img/btn_left.gif"></TD>
						<TD valign="center" align="right" background="<%=contextPath%>/img/btn_center.gif" class="testo">
							<A CLASS="BUTTON" HREF="javascript:changeDir()">SELECT XFER FILE</A></TD>
						<TD valign="center" align="right"><IMG alt="" SRC="<%=contextPath%>/img/btn_right.gif"></TD>
					</TR>
					</TABLE>
				</td>
			</tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			</tbody>
		</table>
		<table>
		<tr>
		<% 	String tempsingleDeliveryCmt = request.getAttribute("tempsingleDeliveryCmt")!=null ? (String)request.getAttribute("tempsingleDeliveryCmt") : ""; 
                if (!tempsingleDeliveryCmt.equals("")) {
                	singleDeliveryCmt = tempsingleDeliveryCmt.trim(); 
                }%> 
			<td class=testo width="40%" valign="top" >
		    <b>Action Comment</b><br>
		    <textarea name="FIELD.SINGLEDELIVERYCMT" rows="6" cols="70" WRAP=PHYSICAL value="<%=singleDeliveryCmt %>"><%=singleDeliveryCmt %></textarea>                        
		    </td>
		</tr>
		<!-- INTER ROWS SPACE -->
		<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
		<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
		<!-- /INTER ROWS SPACE -->
	<%	ArrayList emailAttributes =  (ArrayList)EmailInfoMgr.getEmailDetails();
		tpms.EmailDetails  emailDetails ;
		if (emailAttributes.size() != 0) { %>
		
			<tr>
			<% 	String tempdeliveryComm = request.getAttribute("tempdeliveryComm")!=null ? (String)request.getAttribute("tempdeliveryComm") : ""; 
                if (!tempdeliveryComm.equals("")) {
                	deliveryComm = tempdeliveryComm.trim(); 
                }%> 
                <td class=testo width="40%"><b>Race Comments</b></td>
            </tr>
            <tr><td><textarea rows="6" cols="70" maxlength="2048" wrap="off" name="FIELD.DELIVERYCOMM" onclick="chkXfer()" value="<%=deliveryComm%>"><%=deliveryComm%></textarea></td></tr>
		<!-- INTER ROWS SPACE -->
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
		<!-- /INTER ROWS SPACE -->
		<tr>
			<TD class=testo width="40%"><b>TESTS </b>
				<table border="1">
					<tr>
						<th></th>
						<th>Test # *</th>
						<th>New</th>
						<th>Old LSL</th>
						<th>Old USL</th>
						<th>Unit</th>
						<th>New LSL</th>
						<th>New USL</th>
						<th>Comment</th>
						<Th><input type="button" name="Addrow" onClick="generateRow()" value="Add"></Th>
					</tr>
				<% 	String ttestno="";
					String tunit="";
					String tnew_lsl="";
					String tnewflag="N";
					String tnew_usl="";
					String told_lsl="";
					String ttest_comment="";
					String told_usl="";
					int trowno=0;
					int rno=0;
							
					String str = request.getAttribute("testcount")==null?"":request.getAttribute("testcount").toString();
                	if (!str.equals("")) { 
            			int rcount= Integer.parseInt(str);
            			for (int i=1;i<rcount; i++) {
							rno = rno + 1;
							BeanTest beanTest = (BeanTest)request.getAttribute(i + "");
            				if (beanTest == null)
            				{
            					beanTest = (BeanTest)(request.getSession().getValue(i + ""));
            				}
            				if (beanTest.getTestNo()!= null && i!=0) {
            					ttestno=beanTest.getTestNo();
            					told_usl=beanTest.getOldUSL();
            					tnewflag=beanTest.getNewFlag();
            					tunit=beanTest.getUnit();
            					told_lsl=beanTest.getOldLSL();
            					tnew_lsl=beanTest.getNewLSL();
            					tnew_usl=beanTest.getNewUSL();
            					ttest_comment=beanTest.getTestComment();	
            					trowno=beanTest.getRowNo();
            				} %>
					<tr>
						<td><A href="javascript:removeRowFromTable('<%= rno%>')"><img src="img/ico_canc.gif" alt="Delete" border="0"></img></A></td>
						<TD><input type="text" size="8" maxlength='32' name="FIELD.TEST_NO" value=<%=ttestno%>></TD>
						<TD>
							<select class="tendina" size="1" name="FIELD.NEW_FLAG" value=<%=tnewflag%>>
						<% 	if(tnewflag.equals("Y")){ %>
								<option value="Y" selected>Yes</option>
								<option value="N">No</option>
						<%	} else { %>
								<option value="Y">Yes</option>
								<option value="N" selected>No</option>
						<%	}%>
							</select>
						</TD>
						<TD><input type="text" size="8" name="FIELD.OLD_LSL" value=<%=told_lsl%>></TD>
						<TD><input type="text" size="8" name="FIELD.OLD_USL" value=<%=told_usl%>></TD>
						<TD><input type="text" size="8" maxlength='6' name="FIELD.UNIT" value=<%=tunit%>></TD>
						<TD><input type="text" size="10" name="FIELD.NEW_LSL" value=<%=tnew_lsl%>></TD>
						<TD><input type="text" size="10" name="FIELD.NEW_USL" value=<%=tnew_usl%>></TD>
						<TD><input type="text" size="20" maxlength='255' name="FIELD.TESTS_COMMENTS" value="<%=ttest_comment%>"></TD>
					</tr>
				<%  }%>

				<%	rno = rno + 1;
					tnewflag="N"; %>
					<tr>
						<td>
							<A href="javascript:removeRowFromTable('<%= rno%>')"> 
								<img src="img/ico_canc.gif" alt="Delete" border="0"></img>
							</A>
						</td>
						<TD>
							<input type="text" size="8" maxlength='32' name="FIELD.TEST_NO" value="">
						</TD>
						<TD>
							<select class="tendina" size="1" name="FIELD.NEW_FLAG" value="">
						<% 		if(tnewflag.equals("Y")) { %>
								<option value="Y" selected>Yes</option>
								<option value="N">No</option>
						<%		} else { %>
								<option value="Y">Yes</option>
								<option value="N" selected>No</option>
						<%		}%>
							</select>
						</TD>

						<TD><input type="text" size="8" name="FIELD.OLD_LSL" value=""></TD>
						<TD><input type="text" size="8" name="FIELD.OLD_USL" value=""></TD>
						<TD><input type="text" size="8" maxlength='6' name="FIELD.UNIT" value=""></TD>
						<TD><input type="text" size="10" name="FIELD.NEW_LSL" value=""></TD>
						<TD><input type="text" size="10" name="FIELD.NEW_USL" value=""></TD>
						<TD><input type="text" size="20" maxlength='255' name="FIELD.TESTS_COMMENTS" value=""></TD>
					</tr>
					<TD><input type="hidden" size="10" name="FIELD.TESTCOUNT" value="<%=testcount%>"></TD>
				<%  }%>
				</table>
			</TD>
		</tr>
		<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
		<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
		<tr>
			<td>
				<tr>
			<% 		String temphw_modification = request.getAttribute("temphw_modification")!=null ? (String)request.getAttribute("temphw_modification") : ""; 
                    if (!temphw_modification.equals("")) {
                    	hw_modification = temphw_modification; 
                    }%>
					<td class=testo width="40%"><b>HW Modifications</b></td>
				</tr>
				<tr><td><textarea rows="6" cols="70" maxlength='2048' wrap="off" name="FIELD.HW_MODIFICATION" value="<%=hw_modification%>"><%=hw_modification%></textarea></td></tr>
				<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
				<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
				<tr><td class=testo width="40%"><b>Performance Indexes: </b></td></tr>
				<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
				<tr>
			<%  	String tempexp_avg_yv = request.getAttribute("tempexp_avg_yv")!=null ? (String)request.getAttribute("tempexp_avg_yv") : ""; 
                	if (!tempexp_avg_yv.equals("")) {
                		exp_avg_yv = tempexp_avg_yv; 
                	}%>
					<td class=testo width="40%">
						<b>Expected Average Yield Variation [%]:</b>
			 &nbsp; &nbsp; 
						<input type="text" maxlength='6' name="FIELD.EXP_AVG_YV" value="<%= exp_avg_yv %>">
					</td>
				</tr>
				<tr>
					<td class=testo width="40%"><% String tempzero_yw = request.getAttribute("tempzero_yw")!=null ? (String)request.getAttribute("tempzero_yw") : ""; 
                    	if (!tempzero_yw.equals("")) {
                       		zero_yw = tempzero_yw; 
                       	}%> 
                       	<b>Zero Yield Wafer [%]:</b>
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
						<input type="text" maxlength='5' name="FIELD.ZERO_YW" value="<%= zero_yw %>">
					</td>
				</tr>
				<tr>
					<td class=testo width="40%">
				<%  	String tempnew_testtime = request.getAttribute("tempnew_testtime")!=null ? (String)request.getAttribute("tempnew_testtime") : ""; 
                    	if (!tempnew_testtime.equals("")) {
                        	 new_testtime = tempnew_testtime; 
                       	}%> 
                       	<b>New Test Time [ms]:</b> 
                       	&nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; 		<input type="text" name="FIELD.NEW_TESTTIME" maxlength='6' value="<%= new_testtime %>">
					</td>
				</tr>
				<tr>
			<% 		String tempis_temp = request.getAttribute("tempis_temp")!=null ? (String)request.getAttribute("tempis_temp") : ""; 
                    if	(!tempis_temp.equals(""))	{
                    	is_temp = tempis_temp; 
                    }%>
					<td class=testo width="40%">
						<b>Is Temporary [y/n]:</b> &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp;&nbsp;
						<select class="tendina" size="1" name="FIELD.IS_TEMP" value="<%= is_temp %>">
				<% 		if (is_temp.equals("Y")) { %>
							<option value="Y" selected>Yes</option>
							<option value="N">No</option>
				<%		} else { %>
							<option value="Y">Yes</option>
							<option value="N" selected>No</option>
				<%		}%>
						</select>
					</td>
				</tr>
				<tr>
					<td class=testo width="40%"><% String tempvalid_till = request.getAttribute("tempvalid_till")!=null ? (String)request.getAttribute("tempvalid_till") : ""; 
                         if (!tempvalid_till.equals("")) {
                        	 valid_till = tempvalid_till; 
                         }%> 
                         <b>Valid Till:</b>
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			<font face="arial" size="-2">(dd/mon/yyyy)</font><br>
			&nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						<input readonly class="txtnob" maxlength="20" size="13" name="FIELD.VALID_TILL" value="<%=valid_till%>" onblur="if (this.value != '') { this.value = ''; }"> 
						<A href="javascript:openCalendar('<%= "FIELD.VALID_TILL"%>','Creation date')">
							<img src="img/pix.gif" width="2" border="0">
							<IMG src="img/ico_calendar.gif" border="0" alt="Calendar"> 
						</A>
					</td>
				</tr>
				<tr>
			<% 		String tempdisemail = request.getAttribute("tempdisEmail")!=null ? (String)request.getAttribute("tempdisEmail") : ""; 
					if (!tempdisemail.equals("")) {
                    	dis_email = tempdisemail; 
                    }%>
					<td class=testo width="40%">
						<b>Mailing List: </b> &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; 
						<select class="tendina" size="1" name="FIELD.DIS_EMAIL">
						<option value=""></option>
				<% 		if(emailAttributes.size() != 0) {
			      			for (int i = 0; i < emailAttributes.size();i++ ) {
			      				emailDetails = (tpms.EmailDetails) emailAttributes.get(i); 
			      				boolean selBool=(!dis_email.equals("") && dis_email.equals(emailDetails.getEmailaddress())); %>
							<option <%= (selBool ? "selected" : "") %>>
								<%= emailDetails.getEmailaddress() %>
							</option>
				<%			}
                        }
                         %>
						</select>
					</td>
				</tr>
			</table>
	<%	} else {
			deliveryComm ="";
            hw_modification="";
            exp_avg_yv="";
            zero_yw="";
            valid_till="";
            new_testtime="";
            is_temp="N";%>
            <INPUT TYPE="HIDDEN" NAME="FIELD.SINGLEDELIVERYCMT" VALUE=""> 
            <INPUT TYPE="HIDDEN" NAME="FIELD.DELIVERYCOMM" VALUE=""> 
            <INPUT TYPE="HIDDEN" NAME="FIELD.HW_MODIFICATION" VALUE="">
			<INPUT TYPE="HIDDEN" NAME="FIELD.EXP_AVG_YV" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.ZERO_YW" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_TESTTIME" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.IS_TEMP" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.VALID_TILL" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.TEST_NO" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_FLAG" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.OLD_LSL" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.OLD_USL" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.UNIT" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_LSL" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_USL" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.TESTS_COMMENTS" VALUE=""> 
			<INPUT TYPE="HIDDEN" NAME="FIELD.DIS_EMAIL" VALUE=""> 
	<%  }%>
	</FORM>
	</TD>
</TR>
<TR>
	<td>
	<table cellpadding="0" cellspacing="0" border="0" width="65%">
		<font face="arial" size="-2">
			* = Mandatory field <br> Note that single, double quotes, "~" and "|" not allowed in any entry.
		</font>
		<tr><td><img src="<%=contextPath%>/img/pix_nero.gif" width="560" height="1" alt="" border="0"></td></tr>
		<tr><td><img src="<%=contextPath%>/img/blank_flat.gif" alt="" border="0"></td></tr>
		<tr>
			<td align="right">
			<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
				<TR>
					<!-- TD valign="center" align="right"><IMG alt=""
						SRC="<=contextPath%>/img/btn_left.gif"></TD>
					<TD valign="center" align="right"
						background="<=contextPath%>/img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:changeDir()">SELECT XFER FILE</A></TD>
					<TD valign="center" align="right"><IMG alt=""
						SRC="<=contextPath%>/img/btn_right.gif"></TD>
					<TD><img alt="" src="<=contextPath%>/img/pix.gif" width="5"
						border="0"></TD-->
					<TD valign="center" align="right"><IMG alt=""
						SRC="<%=contextPath%>/img/btn_left.gif"></TD>
					<TD valign="center" align="right"
						background="<%=contextPath%>/img/btn_center.gif" class="testo"><A
						CLASS="BUTTON" HREF="javascript:submitAction()">SUBMIT</A></TD>
					<TD valign="center" align="right"><IMG alt=""
						SRC="<%=contextPath%>/img/btn_right.gif"></TD>
				</TR>
			</TABLE>
			</td>
		</tr>
	</table>
	</td>
</TR>
<%@ include file="bottom.jsp"%>
</BODY>

<script language="javascript">
		var objToPlantCombo = document.tpActionForm.elements['FIELD.TO_PLANT'];
        var plant = objToPlantCombo.options[objToPlantCombo.selectedIndex].text;
	    setProductionAreaForPlant(plant);
</script>
</HTML>

