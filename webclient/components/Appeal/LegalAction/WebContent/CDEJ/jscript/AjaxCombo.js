/*
 * Licensed Materials - Property of IBM
 *
 * PID 5725-H26
 *
 * Copyright IBM Corporation 2012, 2021. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
 require(["curam/util/Dropdown"]);
 var dropdownInputID = '__o3id1';
 var legalActionDropdown = new curam.util.Dropdown();
 
 function getCodetablesFromAJAXRequest(activityUrl, nonBlankSelectedCode) {   
   if (nonBlankSelectedCode) {
     dojo.xhrGet({url: activityUrl,
                  handleAs: "text",
                  timeout: 5000,
                  load: updateDependantDropdown});
   } else {
     // if blank value selected in 1 dropdown then reset the other dropdown to blank values
     legalActionDropdown.resetDropdownToEmpty(dropdownInputID);
   }            
 }

 function updateDependantDropdown(response) {
   dojo.require("dojox.xml.parser");

   // Parse the xml text returned from the server
   var xmlDoc = dojox.xml.parser.parse(response);
   var codetables = xmlDoc.getElementsByTagName("codetables")[0];
    
   var idList = [];
   var descriptionList = [];
    
   for (loop = 0; loop < codetables.childNodes.length; loop++) {
     item = {};
     var codetable = codetables.childNodes[loop];
     var name = codetable.getElementsByTagName("name")[0];
     var id = codetable.getElementsByTagName("id")[0];
        
     // populates id list and codetable description list
     if(name!=null && name.childNodes[0]!=null){
       var nameValue=name.childNodes[0].nodeValue;
       var idValue=id.childNodes[0].nodeValue;
       idList.push(idValue);
       descriptionList.push(nameValue);
     }
   }
        
   // update the dropdown     
   legalActionDropdown.updateDropdownItems(dropdownInputID, idList, descriptionList);
}

function doCompletion(arg0, arg1, arg2) {
  // this value comes from an value being slected in ComboBox via OnChnage event
  var selectedCode = window.__spmComboboxSelectedCode;
  var url;
  var nonBlankSelectedCode = selectedCode && selectedCode.length > 0;

  if (nonBlankSelectedCode) {
    url = arg1 + "Page.do?Param0=" + selectedCode + "&Param1=" + escape(document.getElementById(arg2).value);
  }

  getCodetablesFromAJAXRequest(url, nonBlankSelectedCode);
}