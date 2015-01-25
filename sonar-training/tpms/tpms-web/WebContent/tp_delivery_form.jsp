<%--This form to use deliver one or more TPs --%>

<%@ page import="tol.reportSel"%>
<%@ page import="org.w3c.dom.NodeList"%>
<%@ page import="tol.slctLst"%>
<%@ page import="org.w3c.dom.Document"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="it.txt.general.utils.XmlUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="tpms.FacilityMgr"%>
<%@ page import="tpms.TesterInfoMgr"%>
<%@ page import="tpms.utils.QueryUtils"%>
<%@ page import="it.txt.tpms.tp.utils.TpUtils" %>
<%@ page import="it.txt.tpms.tp.managers.TPDbManager"%>
<%@ page import="it.txt.tpms.tp.TP"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="it.txt.tpms.tp.list.TPList"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="tol.dateRd"%>
<%@ page import="tpms.EmailInfoMgr" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="tpms.BeanTest" %>
<%
    String repFileName=(String)request.getAttribute("repFileName");
    Document tpDoc=(Document)request.getAttribute("tpMultiDoc");
    NodeList tpRecs=tpDoc.getDocumentElement().getElementsByTagName("TP");
    String baseline=(String)request.getAttribute("baseline");
    reportSel repsel=(reportSel)session.getAttribute("repsel");
    slctLst dbUsrLst=null;
    slctLst dbPlant;
   
    boolean okDbConnection = QueryUtils.checkCtrlServletDBConnection();
    String multiDeliveryCmt =request.getAttribute("multiDeliveryCmt")!= null ? (String)request.getAttribute("multiDeliveryCmt") : "";
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
	
%>

<%String contextPath = request.getContextPath();%>
<HTML>
<HEAD>
  	<TITLE>TPMS/W <%= config.getServletContext().getInitParameter("SW_VER") %></TITLE>
  	<LINK href="default.css" type=text/css rel=stylesheet>
  	<script type="text/javascript" language="javascript" src="<%=contextPath%>/js/GeneralUtils.js"></script>
  	<SCRIPT type="text/javascript" language=JavaScript>
   
    function submitAction() {
    	if (checkNofSelItems()>0) return;
     	if (controlInputData()) return;
     	if(!NewAttribute()) return;
     	var confirmationMessage = "The current TP will be delivered,\nafter that you can't change following fields:\n";
     	confirmationMessage += "\n\nDo you want proceed?";
     	if (confirm(confirmationMessage)) {
     	document.tpActionForm.submitAction.value = "Y";
   		document.tpActionForm.submit();
     	}
   	}

    function checkNofSelItems() {
    	var cont=0;
    	var elem;
        for (var i=0; i< document.tpActionForm.elements.length; i++) {
	    	elem = document.tpActionForm.elements[i].name;
	        if (elem.search('JOB_REL') > 0) {
	        	if (document.tpActionForm.elements[i].value == "") {
	            	cont++;
	                alert('JOB RELEASE IS A MANDATORY FIELD!');
	            }
		    }
	
	        if (elem.search('JOB_REV') > 0) {
	        	if (document.tpActionForm.elements[i].value == "") {
	            	cont++;
	                alert('JOB REVISION IS A MANDATORY FIELD!');
	            }
		    }
	    
      <%	if (okDbConnection) {
       //if the db connection is valid valid and prod login becomes mandatory fields %>
        		if (elem.search('PROD_LOGIN') > 0) {
            		var el = document.tpActionForm.elements[i];
               		if (el[el.selectedIndex].value == "") {
                		cont++;
                    	alert('PROD LOGIN IS A MANDATORY FIELD!');
                    	el.focus();
                   		return cont;
                	}
            	}

            	if (elem.search('VALID_LOGIN') > 0) {
           			var el = document.tpActionForm.elements[i];
               		if(el[el.selectedIndex].value == "") {
                		cont++;
                    	alert('VALID LOGIN IS A MANDATORY FIELD!');
                    	el.focus();
                    	return cont;
                	}
            	}
           
      <%	}%>
        }
    	return cont;
  	}

  	function controlInputData() {
    	var currentField;
     	var elemName = "";
    //Date parname;
     	for (var i=0; i < document.tpActionForm.elements.length; i++) {
     		currentField = document.tpActionForm.elements[i];
        	elemName = currentField.name;
	      	if (elemName.search('JOB_REV') > 0) {
            	if (currentField.value.length != 2) {
                	alert('JOB REVISION length is not valid.\n\nA valid JOB REVISION requires 2 characters and do not contains special characters.');
                	currentField.focus();
                	return true;
            	} else if (filterUserInputNumbersOnly(currentField) >= 0) {
                	alert('JOB REVISION contains special characters!\n\nA valid JOB REVISION requires 2 characters and do not contains special characters.');
                	currentField.focus();
                	return true;
            	}
		    } else if (elemName.search('AREA_PROD') > 0) {
				if (currentField.selectedIndex <= 0) {
					currentField.focus();
					alert('Production Area is a mandatory field!\nKindly select it from the list');
					return true;
				}
		    }
    	}
     	return false;
  	}

	function openCalendar(field, label) { 
    	var url="single_calendar_widget.jsp";
     	var qrystr="?field="+field+"&form="+"tpActionForm"+"&label="+label;
     	var popup=window.open(url+qrystr,"CALENDAR","width=330,height=350,resizable=yes,scrollbars=no,status=0,toolbar=no,location=no,menubar=no");
     	if (popup != null) {
      		if (popup.opener == null) popup.opener = self;
     	}
    } 
//In submit this function will read the test array value
		function NewAttribute() { 
		tmpText ="";
		for (i=0;i<document.tpActionForm.elements['FIELD.TEST_NO'].length;i++) { 
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
  		document.tpActionForm.tempAddRow.value = tmpText;
  		var dismail;
  		ind = document.tpActionForm.elements['FIELD.DIS_EMAIL'].selectedIndex;
     		if (ind>=0) {
       			dismail=document.tpActionForm.elements['FIELD.DIS_EMAIL'].options[ind].text;
     		} else {
       			dismail="EMPTY";
     		}
     		if (dismail == ''){
     		
     			dismail="EMPTY";
     		}
			
 			var deliveryComm = document.tpActionForm.elements['FIELD.DELIVERYCOMM'].value;
 			if (deliveryComm != '') {
 				if (filterSpecialChar(deliveryComm) == false) {	
					alert("Race Comments contains invalid characters ~ , | ,\" and \' \nRace Comments must not be more than 2048 characters!");
					return false;
			    } 
			}else{
				deliveryComm="EMPTY";
			}
			var hw_modification = document.tpActionForm.elements['FIELD.HW_MODIFICATION'].value;
			if (hw_modification != '') {	
				if (filterSpecialChar(hw_modification) == false) {
		            alert("HW Modifications contain invalid characters ~ , | ,\" and \' \nHW Modifications must not be more than 2048 characters!");
		            return false;
		       	} 
		    }else{
				hw_modification="EMPTY";
			}
			var exp_avg_yv = document.tpActionForm.elements['FIELD.EXP_AVG_YV'].value;
			if(exp_avg_yv != '') { 
	        	//var result=filterDoubleNumberLen(exp_avg_yv);
	       		//if (result == false) { 
      		if ((parseFloat(exp_avg_yv) > 99.99 || parseFloat(exp_avg_yv) < -99.99 || isNaN(exp_avg_yv))||((String(exp_avg_yv).indexOf(".")) > 0)&& (String(exp_avg_yv).indexOf(".") < String(exp_avg_yv).length - 3)){
	       			alert("Expected Average Yield Variation should be between -99.99 to 99.99 inclusively!");
	       			return false;
	      	 	}
	     	}else{
				exp_avg_yv="EMPTY";
			}
			var zero_yw = document.tpActionForm.elements['FIELD.ZERO_YW'].value;
			if (zero_yw != '') { 
		       	var result=filterDoubleNumberLen(zero_yw);
		       	if (result == false) {
		       		alert("Zero Yield Wafer should be between 0.0 to 99.99 inclusively!");
		       		return false;
		       	}
		     }else{
				zero_yw="EMPTY";
			}
			var new_testtime = document.tpActionForm.elements['FIELD.NEW_TESTTIME'].value;
			if (new_testtime != '') { 
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
	     	}else{
				new_testtime="EMPTY";
			}
			var valid_till = document.tpActionForm.elements['FIELD.VALID_TILL'].value;
			if (valid_till != ''){
				valid_till = document.tpActionForm.elements['FIELD.VALID_TILL'].value;
			}else{
				valid_till="EMPTY";
			}		
	 		var is_temp;
     		var ind99=document.tpActionForm.elements['FIELD.IS_TEMP'].selectedIndex;
     		if (ind99>=0) {
       			is_temp =document.tpActionForm.elements['FIELD.IS_TEMP'].options[ind99].value;
     		} else {
       			is_temp='EMPTY';     
     		}  
     		tmpRace = deliveryComm + "|" + hw_modification + "|" + exp_avg_yv + "|" + zero_yw + "|" + new_testtime + "|" + is_temp + "|" + valid_till + "|" + dismail;
     		document.tpActionForm.tempRace.value = tmpRace;
     		var DeliveryCmt = document.tpActionForm.elements['multiDeliveryCmt'].value;
     		if (DeliveryCmt != '') { 
      			if (filterSpecialChar(DeliveryCmt) == false) {
       			alert("Delivery Comments contains invalid characters ~ , | ,\" and \' \nDelivery Comment must not be more than 2048 characters!");
       			return false;
       			}
       		}
    	return true;
	} 

//click add button this function run and increase one row      
	function generateRow() {
		tmpText ="";
		var count = 2;	
  		document.tpActionForm.Addrowcount.value = "Add";
  		document.tpActionForm.action.value = 'tp_delivery';
  		document.tpActionForm.submitAction.value = "null";
  		var rCount = '<%= request.getAttribute("testcount") != null ? request.getAttribute("testcount") : "" %>';
		document.tpActionForm.testcount.value = parseInt(rCount)+1;
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
			document.tpActionForm.tempAddRow.value = tmpText;
  			
			ind = document.tpActionForm.elements['FIELD.DIS_EMAIL'].selectedIndex;
     		if (ind>=0) {
       			document.tpActionForm.tempdisEmail.value=document.tpActionForm.elements['FIELD.DIS_EMAIL'].options[ind].text;
     		} else {
       			document.tpActionForm.tempdisEmail.value='';
     		}
     		document.tpActionForm.tempmultiDeliveryCmt.value=document.tpActionForm.elements['multiDeliveryCmt'].value;
  			document.tpActionForm.tempdeliveryComm.value=document.tpActionForm.elements['FIELD.DELIVERYCOMM'].value;
			document.tpActionForm.temphw_modification.value=document.tpActionForm.elements['FIELD.HW_MODIFICATION'].value;
			document.tpActionForm.tempexp_avg_yv.value=document.tpActionForm.elements['FIELD.EXP_AVG_YV'].value;
			document.tpActionForm.tempzero_yw.value=document.tpActionForm.elements['FIELD.ZERO_YW'].value;
			document.tpActionForm.tempnew_testtime.value=document.tpActionForm.elements['FIELD.NEW_TESTTIME'].value;
			document.tpActionForm.tempvalid_till.value=document.tpActionForm.elements['FIELD.VALID_TILL'].value;			
	 		var is_temp;
     		var ind99=document.tpActionForm.elements['FIELD.IS_TEMP'].selectedIndex;
     		if (ind99>=0) {
       			document.tpActionForm.tempis_temp.value =document.tpActionForm.elements['FIELD.IS_TEMP'].options[ind99].value;
     		} else {
       			document.tpActionForm.tempis_temp.value='';     
     		} 
 			document.tpActionForm.submit();
		}
	}
//to delete test rows 	
function removeRowFromTable(rowno) {
		tmpText ="";
		var rCount="";
		document.tpActionForm.deletedRow.value=rowno;
		document.tpActionForm.action.value = 'tp_delivery';
		document.tpActionForm.submitAction.value = "null";
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
	
		document.tpActionForm.tempAddRow.value = tmpText;
  		document.tpActionForm.testcount.value = rCount;
   		document.tpActionForm.submit();
	}
	
  
	</SCRIPT>
	
</HEAD>
<BODY bgColor="#FFFFFF" onLoad="if (document.tpActionForm.elements['FIELD.JOBNAME']!= null) document.tpActionForm.elements['FIELD.JOBNAME'].focus()">
<% boolean repBool=false; boolean csvRepBool=false; String pageTitle="TP DELIVERY FORM"; %>
<%@ include file="top.jsp" %>
<TR>
	<TD ALIGN="LEFT"><!-- FORM -->
    <FORM name="tpActionForm" action="tpMultiActionServlet" method="post">
    	<INPUT TYPE="HIDDEN" NAME="submitAction" VALUE="">
     	<INPUT TYPE="HIDDEN" NAME="action" VALUE="tp_delivery">
     	<INPUT TYPE="HIDDEN" NAME="repFileName" VALUE="<%= repFileName %>">
     	<INPUT TYPE="HIDDEN" NAME="baseline" VALUE="<%= baseline %>">
     	<INPUT TYPE="HIDDEN" NAME="Addrowcount"> 
		<INPUT TYPE="HIDDEN" NAME="testcount">
		<INPUT TYPE="HIDDEN" NAME="tempmultiDeliveryCmt" VALUE="">  
		<INPUT TYPE="HIDDEN" NAME="tempdeliveryComm" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="temphw_modification" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempexp_avg_yv" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempzero_yw" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempnew_testtime" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempis_temp" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempvalid_till" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempAddRow" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="tempdisEmail" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="ROWNO" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.TEST_NO" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_FLAG" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.OLD_LSL" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.OLD_USL" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.UNIT" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_LSL" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="FIELD.NEW_USL" VALUE=""> 
		<INPUT TYPE="HIDDEN" NAME="FIELD.TESTS_COMMENTS" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="tempRace" VALUE="">
		<INPUT TYPE="HIDDEN" NAME="deletedRow" VALUE=""> 
		
    <%  GregorianCalendar cal = new GregorianCalendar(); %>
        <INPUT TYPE="HIDDEN" NAME="FIELD.DATE" VALUE="<%= cal.get(Calendar.YEAR) %>.<%= (cal.get(Calendar.MONTH)<9 ? "0" : "") %><%= cal.get(Calendar.MONTH)+1 %>.<%= (cal.get(Calendar.DATE)<10 ? "0" : "") %><%= cal.get(Calendar.DATE) %>-<%= dateRd.getCurDateTime().substring(9).replace(':','.').trim() %>">
	<%  int labelsize =0;
    	labelsize = tpRecs.getLength();%>
      	<INPUT TYPE="HIDDEN" NAME="labelLen" VALUE="<%=labelsize %>">
    <%
       //String commonPlantLogin = TpmsConfiguration.getInstance().getCommonEngineerLogin();
     
        for (int i=0; i<tpRecs.getLength(); i++) {
        	Element tpRec=(Element)tpRecs.item(i);
         	String toPlant = XmlUtils.getVal(tpRec,"TO_PLANT");
         	if(request.getAttribute("TESTER_FAMILY")==null){
               	testerfam = XmlUtils.getVal(tpRec,"TESTERFAM");
            } else {
               testerfam = (String)request.getAttribute("TESTER_FAMILY");
            } 
            
         	//String prod_login = (XmlUtils.getVal(tpRec,"PROD_LOGIN")!=null ? XmlUtils.getVal(tpRec,"PROD_LOGIN") : "");
         	String valid_login =(XmlUtils.getVal(tpRec,"VALID_LOGIN")!=null ? XmlUtils.getVal(tpRec,"VALID_LOGIN") : "");
         	String email_to = (XmlUtils.getVal(tpRec,"EMAIL_TO")!=null ? XmlUtils.getVal(tpRec,"EMAIL_TO") : "");
         	//String certification= (XmlUtils.getVal(tpRec,"CERTIFICATION")!=null ? XmlUtils.getVal(tpRec,"CERTIFICATION") : "");
         	//String certification= "check"; %>
         	<input type="hidden" name="TESTER_FAMILY" value="<%= testerfam %>">
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
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                       		<INPUT type="HIDDEN" name="tpLabel" value="<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>">
                      		<INPUT TYPE="HIDDEN" NAME="addRowValue.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>" VALUE=""> 
                      		<tr>
                      			<td>&nbsp;</td>
								<td class=testo width="40%">
                         			<b>Job name </b><br>
                        	 		<%= XmlUtils.getVal(tpRec,"JOBNAME") %>
                         			<INPUT type="HIDDEN" name="chk<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>" value="Y">
                        		</td>
								<td>&nbsp;</td><td>&nbsp;</td>
                        		<td class=testo width="40%">
                         			<b>Job release </b><br>
                         			<%= XmlUtils.getVal(tpRec,"JOB_REL") %>
                         			<INPUT type="HIDDEN" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.JOB_REL" value="<%= XmlUtils.getVal(tpRec,"JOB_REL") %>">
                        		</td>
								<td>&nbsp;</td>
                      		</tr>
                       		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr>
                      			<td>&nbsp;</td>
                        		<td class=testo width="40%">
                         			<b>Job revision *</b><br>
                         			<INPUT type="TEXT" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.JOB_REV" value="<%= XmlUtils.getVal(tpRec,"JOB_REV") %>">
                        		</td>
								<td>&nbsp;</td><td>&nbsp;</td>
                        		<td class=testo width="40%">
                         			<b>Production Area *</b><br>
                         			<SELECT name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.AREA_PROD">
                             			<%=TpUtils.buildPlantProductionAreaOptions( GeneralStringUtils.isEmptyString( XmlUtils.getVal( tpRec, "AREA_PROD" ) ) ? "" : XmlUtils.getVal( tpRec, "AREA_PROD" ), toPlant )%>
                         			</SELECT>
                        		</td>
								<td>&nbsp;</td>
                      		</tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
 			                <tr><td>&nbsp;</td>
                       			<td class=testo width="30%">
                          			<b>Destination Plant</b><br>
                          			<%=toPlant  %>
                        		</td>
                        		<td>&nbsp;</td><td>&nbsp;</td>
                        		<td class=testo width="30%">
                         			<b>Product line </b><br>
                         			<%= XmlUtils.getVal(tpRec,"LINE") %></td>
								<td>&nbsp;</td>
                      		</tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr>
								<td>&nbsp;</td>
                        		<td class=testo width="40%">
                         			<b>Process stage *</b><br>
                         			<select class="tendina" size="1" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.FACILITY">
                          <%			Vector facilitylst= FacilityMgr.getFacilityList(toPlant);
                            			for (int k=0; k<facilitylst.size(); k++) {
                              				boolean selBool=(!XmlUtils.getVal(tpRec,"FACILITY").equals("") ? XmlUtils.getVal(tpRec,"FACILITY").equals( XmlUtils.getVal((Element)facilitylst.elementAt(k))) : (k==0)); %>
                              				<option <%= (selBool ? "selected" : "") %>><%=XmlUtils.getVal((Element)facilitylst.elementAt(k))%>
                            <%			} %>
                          			</select>
                        		</td>
								<td>&nbsp;</td><td>&nbsp;</td> 
                        		<td class=testo width="30%">
                         			<b>Tester info *</b><br>
                         			<select class="tendina" size="1" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.TESTER_INFO">
                          <%        	if(request.getAttribute("TESTER_FAMILY")==null) {
				                      		testerfam = XmlUtils.getVal(tpRec,"TESTERFAM");
				                      	} else {
				                          	testerfam = (String)request.getAttribute("TESTER_FAMILY");
				                     	    }
				                      	Vector tstInfos=TesterInfoMgr.getTesterInfoShowList(toPlant, testerfam);
                          				//Vector tstInfos= TesterInfoMgr.getTesterInfoShowList(toPlant, XmlUtils.getVal(tpRec,"TESTERFAM"));
                            			out.println("testerFamily : "+XmlUtils.getVal(tpRec,"TESTERFAM"));
                          				for (int j=0; j<tstInfos.size(); j++) {
                              				boolean selBool=(!XmlUtils.getVal(tpRec,"TESTER_INFO").equals("") ? XmlUtils.getVal(tpRec,"TESTER_INFO").equals( XmlUtils.getVal((Element)tstInfos.elementAt(j))) : (j==0)); %>
                              				<option <%= (selBool ? "selected" : "") %>><%=XmlUtils.getVal((Element)tstInfos.elementAt(j))%>
                            <%			} %>
                          			</select>
                        		</td>
								<td>&nbsp;</td>
                      		</tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr>
                      			<td>&nbsp;</td>
                        		<td class=testo width="40%">
                         			<b>Prod Login <%=(okDbConnection) ? "*" : ""%></b><br>
                        			<select class="tendina" size="1" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.PROD_LOGIN">
                         				<option value="">
                         <%     		try {
                                			dbUsrLst=repsel.get("USER");
                                			dbPlant=repsel.get("PLANT");
                                			dbPlant.setVal(toPlant);
                                			dbUsrLst.fetch();
                             			} catch(Exception e) { }
                             			if ((dbUsrLst!=null)&&(!dbUsrLst.isBlank())) {
                                			Vector dbUsers=dbUsrLst.getVector();
                                			boolean selBool;
                                			String val;
                                			for (int j=0; j<dbUsers.size(); j++) {
                                    			selBool=(!valid_login.equals("") && valid_login.equals(dbUsers.elementAt(j)));
                                    			val = (String) dbUsers.elementAt(j); %>
                                    			<option value="<%=val%>" <%= (selBool ? "selected" : "") %>><%=val%></option>
                                <%			}
                             			} else { %>
                         <%--<option value="<%= commonPlantLogin %>" <%=(!prod_login.equals("") && prod_login.equals(commonPlantLogin)) ? "selected" : ""%>><%= commonPlantLogin %>--%>
                            <%			}%>
                         			</select>
                        		</td>
								<td>&nbsp;</td><td>&nbsp;</td>
                        		<td class=testo width="40%">
                         			<b>Valid Login <%=(okDbConnection) ? "*" : ""%></b><br>
                         			<select class="tendina" size="1" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.VALID_LOGIN">
                         				<option value="">
                         <%				try {
                                			dbUsrLst=repsel.get("USER");
                                			dbPlant=repsel.get("PLANT");
                                			dbPlant.setVal(toPlant);
                                			dbUsrLst.fetch();
                             			}
                             			catch(Exception e) { }
                             			if ((dbUsrLst!=null)&&(!dbUsrLst.isBlank())) {
                                			Vector dbUsers=dbUsrLst.getVector();
                               		 		boolean selBool;
                               				String val;
                                			for (int j=0; j<dbUsers.size(); j++) {
                                    			selBool=(!valid_login.equals("") && valid_login.equals(dbUsers.elementAt(j)));
                                    			val = (String) dbUsers.elementAt(j); %>
                                    			<option value="<%=val%>" <%= (selBool ? "selected" : "") %>><%=val%></option>
   										 <%}
                            			} else { %>
                             <%--<option value="<%= commonPlantLogin %>" <%=(!valid_login.equals("") && valid_login.equals(commonPlantLogin)) ? "selected" : ""%>><%= commonPlantLogin %>--%>
                             <%			}%>
                         			</select>
                        		</td>
								<td>&nbsp;</td>
                      		</tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr>
								<td>&nbsp;</td>
                        		<td class=testo width="40%" >
                         			<b>To Email </b><br>
                         			<select class="tendina" size="1" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.EMAIL_TO">
                         				<option value="">
                         <%				try	{
                                			dbUsrLst=repsel.get("USER");
                                			dbPlant=repsel.get("PLANT");
                                			dbPlant.setVal(toPlant);
                                			dbUsrLst.fetch();
                             			} catch(Exception e) {}
                             			if ((dbUsrLst!=null)&&(!dbUsrLst.isBlank())) {
                                			Vector dbUsers=dbUsrLst.getVector();
                                			for (int j=0; j<dbUsers.size(); j++) {
                                    			boolean selBool=(!email_to.equals("") ? email_to.equals(dbUsrLst.getAttrVal("email",j)) : false); %>
                                    			<option <%= (selBool ? "selected" : "") %>><%= dbUsrLst.getAttrVal("email",j) %>
                                <%			}
                             			} %>
                         			</select>
                         		</td>
								<td>&nbsp;</td><td>&nbsp;</td>
                        		<td class=testo width="40%">
                         			<b>CC Email </b><br>
                         			<INPUT type="TEXT" name="FIELD.<%= XmlUtils.getVal(tpRec,"TP_LABEL") %>.EMAIL_CC" value="<%=(XmlUtils.getVal(tpRec,"EMAIL_CC")!=null ? XmlUtils.getVal(tpRec,"EMAIL_CC") : "") %>">
                        		</td>
								<td>&nbsp;</td>
                      		</tr>
                       <%--<tr>

                      <!-- LEFT COLUMN SEPARATOR --><td>&nbsp;</td><!-- /LEFT COLUMN SEPARATOR -->
                      <td class=testo width="40%" valign="top" colspan="4">
                      <b>Wait for Certification</b><br>
			          <input type="CHECKBOX" name="FIELD.CERTIFICATION" value="<%=certification%>"
                      <%if(certification.equals("check")){%>
                      checked
                      <%}else{

                      }%>
                      >
                      </td>

                      </tr>--%>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                    		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr>
								<td>&nbsp;</td>
                        		<td class=testo width="40%" valign="top" colspan="4">
                         			<b>Xfer path </b><br>
                         			<%= XmlUtils.getVal(tpRec,"XFER_PATH") %>
                        		</td>
								<td>&nbsp;</td>
                      		</tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      		<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
		        		</tbody>
               		</table>
              	</TD>
            </TR>
			
             <%		if (i<tpRecs.getLength()) {%>
                 <!-- BLACK LINE -->
                 		<TR>
                  			<td>
                    			<table cellpadding="0" cellspacing="0" border="0" width="65%">
                      				<tr><td><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td></tr>
                      				<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                      				<tr><td colspan=5><img alt="" src="img/blank.gif"  border=0></td></tr>
                    			</table>
                  			</td>
                 		</TR>
                 <!-- /BLACK LINE -->
               <%	} %>
      <%		
    	} %>  

	<tr>
        <td class=testo width="40%" valign="top" colspan="4">
        <b>Action Comment</b><br>
        <textarea name="multiDeliveryCmt" rows="6" cols="70" WRAP=PHYSICAL value="<%=multiDeliveryCmt%>"><%=multiDeliveryCmt%></textarea>                        
        </td>
		<!-- INTER ROWS SPACE -->
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
			<tr><td colspan=5><img alt="" src="<%=contextPath%>/img/blank.gif" border=0></td></tr>
		<!-- /INTER ROWS SPACE -->
     </tr>
	 
   	<%	ArrayList emailAttributes =  (ArrayList)EmailInfoMgr.getEmailDetails();
		tpms.EmailDetails  emailDetails ;
		if (emailAttributes.size() != 0) { %>
		<tr>
		<td>
		<table>
			<tr>
			<% 	String tempdeliveryComm = request.getAttribute("tempdeliveryComm")!=null ? (String)request.getAttribute("tempdeliveryComm") : ""; 
			if (!tempdeliveryComm.equals("")) {
                	deliveryComm = tempdeliveryComm.trim(); 
                }%> 
                <td class=testo width="40%"><b>Race Comments</b></td>
            </tr>
            <tr><td><textarea rows="6" cols="70" maxlength="2048" wrap="off" name="FIELD.DELIVERYCOMM" value="<%=deliveryComm%>"><%=deliveryComm%></textarea></td></tr>
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
			<font face="arial" size="-2">(dd/mon/yyyy)</font><br> &nbsp; 
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
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
						<b>Mailing List: </b> &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp;&nbsp; 
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; 
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
			</td>
			</tr>
	<%	} else {
			deliveryComm ="";
            hw_modification="";
            exp_avg_yv="";
            zero_yw="";
            valid_till="";
            new_testtime="";
            is_temp="N";%> 
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
	   <!-- BUTTONS -->
    	<table cellpadding="0" cellspacing="0" border="0" width="65%">
            <font face="arial" size="-2">
				* = Mandatory field <br> Note that single, double quotes, "~" and "|" not allowed in any entry.
			</font>
        	<tr>
            	<td><img src="img/pix_nero.gif" width="560" height="1" alt="" border="0"></td>
            </tr>
            <tr><td><img src="img/blank_flat.gif" alt="" border="0"></td></tr>
			<tr>
				<td align="right">
     		  		<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0">
       		   			<TR>
                    		<TD valign="center" align="right"><IMG alt="" SRC="img/btn_left.gif"></TD>
                    		<TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:history.back()">&nbsp;BACK</a></TD>
                    		<TD valign="center" align="right"><IMG alt="" SRC="img/btn_right.gif"></TD>
		    				<TD><img src="img/pix.gif" width="5" border="0"></TD>
                    		<TD valign="center" align="right"><IMG alt="" SRC="img/btn_left.gif"></TD>
                   			<TD valign="center" align="right" background="img/btn_center.gif" class="testo"><A CLASS="BUTTON" HREF="javascript:submitAction()">&nbsp;SUBMIT</a></TD>
                    		<TD valign="center" align="right"><IMG  alt="" SRC="img/btn_right.gif"></TD>
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