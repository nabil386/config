/*
 * Licensed Materials - Property of IBM
 *
 * PID 5725-H26
 *
 * Copyright IBM Corporation 2008, 2021. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

function PopulateOfficeMembers(theEvent){

	/* BEGIN, 173623, RA*/
  var pageContext = theEvent || arguments[0];
	/* END, 173623*/
  var probableMembersList;
  var object;
  var officeID;
  var officeName;
  var temofficeName;

  pageContext = ""+pageContext;

  probableMembersList= pageContext.substring(pageContext.indexOf("=")+1,pageContext.indexOf("&"));


  var temOfficeID = pageContext.substring(pageContext.indexOf("=")+1,pageContext.length);

  officeID = temOfficeID.substring(temOfficeID.indexOf("=")+1,temOfficeID.indexOf("description")-1 );

  temofficeName = temOfficeID.substring(temOfficeID.indexOf("description"),pageContext.length);

  officeName = temofficeName.substring(temofficeName.indexOf("=")+1,temofficeName.indexOf("&"));

  var val = "";
  val = replaceAll(val,officeName);
  officeName = val;
  var itemName;
  var locationID_desc;
  var locationID_deschf;
  var locationIDValue;
  var id;

  // BEGIN, CR00236827, SS
  curam.dialog.getParentWindow(window).document.forms[0].hiddenJudge_0.value=probableMembersList;

  for(i=0; i < curam.dialog.getParentWindow(window).document.forms[0].elements.length; i++)	{

	  id = curam.dialog.getParentWindow(window).document.forms[0].elements[i].id;
	   if (id.indexOf("locationID_value") != -1) {
	   		locationIDValue =  curam.dialog.getParentWindow(window).document.all[id];
			  curam.dialog.getParentWindow(window).document.forms[0].elements[i].value = officeID;
		  }

	  if(id.indexOf("locationID_desc")!=-1) {
			locationID_desc = curam.dialog.getParentWindow(window).document.all[id];
			curam.dialog.getParentWindow(window).document.forms[0].elements[i].value =
				officeName;
			var temidDesc = ""+id;
			temidDesc = temidDesc.substring(0,temidDesc.length-2);
                        //BEGIN, CR00349333, PB
			curam.dialog.getParentWindow(window).document.getElementById(temidDesc).innerHTML=decodeURIComponent(officeName);
			//END, CR00349333
			curam.dialog.getParentWindow(window).document.getElementById(temidDesc).value=officeName;

		}
 }


 curam.dialog.getParentWindow(window).populateJudge('Hearing');
 // END, CR00236827
 // Close the current dialog and return to the parent page.
 curam.dialog.closeModalDialog();
}

// BEGIN, CR00236827, SS
function populateJudge(category){
    // TODO: Seems like there is a bug here in that is always hardcoded to be 
    // category of "Hearing" -- BOS
    var probableMembersList= document.forms[0].hiddenJudge_0.value;
    var curcode,index;
	var MemberID = new Array();
	var MemberName = new Array();
	var i=1;
	var j=1;
	MemberID[0]="";
	MemberName[0]="";

	while (probableMembersList.length > 0 ) {
		index=probableMembersList.indexOf('%09');

		if ( index == -1 ) {
		  curcode=probableMembersList;
		  probableMembersList = "";
		}else{
		  curcode=probableMembersList.substr(0, index);
		  probableMembersList = probableMembersList.substr(index+3);
		}

		index = curcode.indexOf('%7C');
		var code = curcode.substr(0,index);
		var value = curcode.substr(index+3,curcode.length);
		var temVal = "";
		temVal = replaceAll(temVal,value);
		MemberID[i]=code;
		//BEGIN, CR00349333, PB
		MemberName[i]=decodeURIComponent(temVal);
		//END, CR00349333
		i=i+1;
    }
    //var detailBox;
    var comboboxID;
    
    if(category=='Order') {
    	 comboboxID = '__o3id2';
     } else{
       comboboxID = '__o3id4';
    }
   	populateCombo(comboboxID,MemberID,MemberName);
}
// END, CR00236827

function populateCombo(dropdownInputID,MemberID,MemberName) {
   var idList = [];
   var descriptionList = []; 

  if(MemberID.length>0) {
     for (var loop=0; loop < MemberID.length; loop++){
  	   var memID =MemberID[loop];
  	   var memName =MemberName[loop];
  	   if(memID!=""){
  	  	 //detailComboStore.newItem({id : memID, name : memName});
  	  	 idList.push(memID);
  	  	 descriptionList.push(memName);
  	   }
  	 }
   }
   
   var updatedMembersDropdown = new curam.util.Dropdown();
   updatedMembersDropdown.updateDropdownItems(dropdownInputID, idList, descriptionList);
 }

function replaceAll(val,arg) {

 var temp =  arg.split("+");

	for ( i = 0; i < temp.length; i++ ) {
	 val  = val+temp[i]+" ";
	}

  return val;
}
