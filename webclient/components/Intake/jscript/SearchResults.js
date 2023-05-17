/* 
 * Licensed Materials - Property of IBM
 * 
 * PID 5725-H26
 * 
 * Copyright IBM Corporation 2010,2021. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Modification History
 * --------------------
 * 04-Aug-2021 DR [RTC 207558] Added function clickSearchButton(evt).
 */

var findClient = {

  searchValidation : function(obj) {
	
	var personDetails = "";
	var details = "";
	var url_search = "";
  var returnValue = true;
	
	
	var tableId = document.getElementById('PT19001_body').getElementsByTagName('table')[0];
		
	for ( var i = 0; i < tableId.rows.length; i++) {
		var newrow = tableId.getElementsByTagName('tr')[i];

		for ( var k = 0; k < newrow.cells.length; k++) {
			var newtd = newrow.getElementsByTagName('td')[k];
			var newInput = newtd.getElementsByTagName('input')[0];
          
			if (newInput + '' != 'undefined') {			    
				if (newInput.type == 'text') {				    
					personDetails += newInput.value + ',';					
				}
			}
		}
	}
	
	details = personDetails.split(",");
	
	url_search = "&forename=" + details[1] + "&middleName="
	+details[2]+"&surname="+details[3]+"&dateOfBirth="
	+details[4]+"&referenceNumber="+ details[0];
	  
  obj.href = 'Person_resolveFindClientPage.do?o3ctx=4096' + url_search;
 
	return returnValue;
},




reset : function() {
	var personDetails ='';var details = '';
	var tableId = document.getElementById('PT19001_body').getElementsByTagName('table')[0];
	for(var i=0; i<tableId.rows.length; i++) {
		var newrow = tableId.getElementsByTagName('tr')[i];
		for(var k=0; k<newrow.cells.length; k++) {
			var newtd = newrow.getElementsByTagName('td')[k];
			var newInput = newtd.getElementsByTagName('input')[0];
				if(newInput+'' != 'undefined'){
					if(newInput.type == 'text'){
						newInput.value='';
					}
					}
				}
		}
}
}




function textBoxValidation() {
	
	var personDetails ='';var details = '';
	var tableId = document.getElementById('PT19001_body').getElementsByTagName('table')[0];
	tableId.rows[0].cells[0].id = "validateID";
	for(var i=0; i<tableId.rows.length; i++) {
		var newrow = tableId.getElementsByTagName('tr')[i];
		for(var k=0; k<newrow.cells.length; k++) {
			var newtd = newrow.getElementsByTagName('td')[k];
			var newInput = newtd.getElementsByTagName('input')[0];
				if(newInput+'' != 'undefined'){
					if(newInput.type == 'text'){
						personDetails += newInput.id + ',';
					}
					}
				}
		}
	details = personDetails.split(",");
	document.getElementById(details[1]).maxLength=25;
	document.getElementById(details[2]).maxLength=25;
	document.getElementById(details[3]).maxLength=25;
	document.getElementById(details[4]).maxLength=10;
	document.getElementById(details[0]).maxLength=15;
    	  } 

function fieldValidation(middleName)
{	
	if(document.getElementById('informationMesgText').value=='true' && document.getElementById('typeAction').value=="")
		{	      
			document.getElementById("error-messages-container").innerHTML="<div id='myDiv'>" +
					"<h2 class='message-list' id='error-messages-header'>" +
					MESSAGE_LIST_TEXT + "</h2><ul id='error-messages' class='messages'>" +
					"<li class='level-1'> <div><span>" + ERROR_TEXT + "</span>" 
					+ SEARCH_RESULT_EMPTY_TEXT +"<div></div>" +
					" </li></ul>";	
		}
}

function deleteMyDivTag()
{
	document.getElementById('typeAction').value="true";
	var d = document.getElementById('error-messages-container');
	  var olddiv = document.getElementById('myDiv');
	  d.removeChild(olddiv);
}

function clickSearchButton(element) {

	var keyboardEvent = element.onkeypress.arguments[0];

	
	if(keyboardEvent && keyboardEvent.type === "keypress" && keyboardEvent.keyCode === 13) {
	  var searchButton = document.getElementsByClassName("findClientActions")[0].getElementsByClassName("action-bar")[0].getElementsByClassName("ac")[0];
	  searchButton.click();

	  keyboardEvent.preventDefault();

   }
 }
