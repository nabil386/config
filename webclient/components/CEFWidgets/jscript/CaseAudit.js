var CaseAudit = {};

CaseAudit.addFocusAreaToList = function(){

  list=document.getElementById("focusAreaList");
  text=document.getElementById("newFocusArea").value;
  hiddenField=hiddenField+text+"\t";
  
  if (text.length!=0) {
    newCheckedFocusArea='<input type="checkbox" checked="true" onClick="CaseAudit.selectOrDeselect(this)" name="" id="" value="'+text+'">'+text+'<br />';
    list.innerHTML = list.innerHTML + newCheckedFocusArea;
    document.getElementById("newFocusArea").value="";
    document.getElementById("addButton").disabled="true";
  }
};

CaseAudit.mapVarToForm = function(event, inputID){

  var fixFn = dojo.fixEvent || dojo.event.browser.fixEvent;
  var stopFn = dojo.stopEvent || dojo.event.browser.stopEvent;
  event = fixFn(event);
  stopFn(event);
  
  var hidden_input = dojo.byId(inputID);
  hidden_input.value = dojo.string.trim(hiddenField);  
  
  dojo.byId("__o3btn.CTL1").click();
};

CaseAudit.goBack = function(event){
  event = dojo.fixEvent ? dojo.fixEvent(event) : dojo.event.browser.fixEvent(event);
  dojo.stopEvent ? dojo.stopEvent(event) : dojo.event.browser.stopEvent(event);
  
  var rpu = curam.util.getUrlParamValue(window.location.href, "__o3rpu");
 
  if (rpu) {
    curam.util.redirectWindow(rpu, true);
  }
};

CaseAudit.selectOrDeselect = function(checkbox){

  if(checkbox.checked){

    hiddenField=hiddenField+checkbox.value+"\t";
  } else {

		hiddenField=hiddenField.replace(checkbox.value+"\t","");
  }
};

CaseAudit.openPage = function(pageID) {

if(curam.util.isModalWindow()){
window.close();
window.opener.location.replace(pageID);
} else {
window.location.replace(pageID);
}
};
