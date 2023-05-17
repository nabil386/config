/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/*
 * Copyright 2007-2008, 2010, 2012 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */
 
var meetingTime = {
  addListeners: function(){
    var combo1 = dijit.byId("widget___o3id3");
    
    dojo.connect(combo1, "onChange", updateMeetingTime);
  }
};

function updateMeetingTime() {


  var startTimeFieldDiv = dojo.byId("__o3id3");
  var endTimeFieldDiv = dojo.byId("__o3id6");
  var updateEndTimeDateField = "false";
  
  var comboBoxOfStartTimeField = dijit.byId("__o3id3");
  var storeOfCombo1 = comboBoxOfStartTimeField.store;
  var optionIndexOfStartTimeField = comboBoxOfStartTimeField.item.index;
  var optionIndexForEndTimeField;
  
  // set the index for the end date field value
  if(optionIndexOfStartTimeField == 47) {
    optionIndexForEndTimeField = 1;
    updateEndTimeDateField = "true";
  } else if(optionIndexOfStartTimeField == 48) {
    optionIndexForEndTimeField = 2;
    updateEndTimeDateField = "true";
  } else {
    optionIndexForEndTimeField = optionIndexOfStartTimeField +2;
  }
  
  var timeValueForEndTimeField = storeOfCombo1.root.options[optionIndexForEndTimeField].value;
  var comboBoxOfEndTimeField = dijit.byId("__o3id6");
  comboBoxOfEndTimeField.set("value", timeValueForEndTimeField);


  // check if the date has to be moved onto tomorrow on the time field
  if(updateEndTimeDateField == "true") {
    // update the date on the time field
  }

}

function hideTimeFields() {

  var startTimeFieldDiv = dojo.byId("widget___o3id3");
  var endTimeFieldDiv = dojo.byId("widget___o3id6");
  

  if(startTimeFieldDiv.style.display == ""){
    startTimeFieldDiv.style.display = "none";
  } else {
    startTimeFieldDiv.style.display = "";
  }
  
  if(endTimeFieldDiv.style.display == ""){
    endTimeFieldDiv.style.display = "none";
  } else {
    endTimeFieldDiv.style.display = "";
  }

}

function hideTimeFieldsOnLoad() {

  var input = dojo.byId("__o3id4");

  if(input.checked) {
    hideTimeFields();
  }
  
  meetingTime.addListeners();
}


//Adding an onchange event to a popup field doesn't work for curam popup fields.
//executeMapping is always called when a value is returned from a popup. Overriding this method  
//as an alternative to having an onchange javascript method on the Meeting startDate field.

//keep a reference to existing executeMapping function, so we can call it in the overridden method.
var oldExecuteMapping = executeMapping;

executeMapping = function(resultName, value) {

var startDate = getStartDate();

 oldExecuteMapping(resultName, value);
 
 var postPopupActionStartDate = getStartDate();
 
 //if the startDate value has changed - this method was called on the startDate popup field 
 //changing its value, so update the endDate
 if(postPopupActionStartDate != startDate) {
 updateEndDate(postPopupActionStartDate);
 }
}

//sets the endDate to the value passed in(startDate)
function updateEndDate(value) {
  var endDate = dojo.byId("__o3id5");
  endDate.value = value;
  return endDate;
}

//gets the value of the Meeting startDate
function getStartDate()
{
  var input = dojo.byId("__o3id2");
  var startDate = input.value;
  return startDate;
}