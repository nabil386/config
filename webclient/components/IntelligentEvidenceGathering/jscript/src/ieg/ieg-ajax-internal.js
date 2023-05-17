/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2017, 2019. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/*
 * Modification History
 * --------------------
 * 11-Sep-2019  JD   [RTC248549] Updated blank entry option and pass params
 * 								 from old widget into newly created one.
 * 11-Aug-2017  CMC  [RTC200064] Initial version.
 */

require(["curam/widget/FilteringSelect", 
  "dojo/data/ObjectStore", 
  "dojo/store/Memory",
  "dojo/json",  
  "dijit/registry"]);

/**
* This script is used in conjunction with codetable-hierarchy.js.
*/
function IEGAJAXCall(dataTarget, inputProviderName) {
  this.target=dataTarget;
  this.inputProvider = inputProviderName || 'null';
}

/**
* Modify the functions prototype.
*/
IEGAJAXCall.prototype.doRequest = function (
  opAlias, params, isPopup, synchronous, placeholderText) {
  
  var theServlet = "../servlet/JSONServlet";
  var filler = this;

  if (isPopup) 
    theServlet= "../" + theServlet;

  var oData = {
      caller: this.target.id,
      operation: opAlias,
      inputProvider: this.inputProvider,
      args: params
  };
  
  function processResult(oResult, opAlias, placeholderText){
    oResult = dojo.fromJson(oResult);
    if (oResult instanceof Array) {
      if (opAlias == "getCodeTableSubset") {
        filler.fillCTWithBlank(oResult, placeholderText); 
      }
    }
  };

  // AJAX post request.
  // Response is JSON in the format:
  // "[{"default":true,"code":"INGR1","descr":"A'ane"},{"default":false...
  var bindRes = dojo.xhrPost({
    url:theServlet,
    handleAs:   "text",
    load: function(data, evt){
      processResult(data, opAlias, placeholderText);
    },
    error: function(){
      alert("error");
    },
    content: {"content": dojo.toJson(oData)},
    preventCache: true,
    sync: synchronous
  });
};

/**
* Creates and populates the HCT dropdown. The results of the 
* AJAX call are passed in. These are converted into a Dojo 
* store which is then used as the data provider for the dropdown.
*/
IEGAJAXCall.prototype.fillCTWithBlank = function (theResult, placeholderText) {
  
  // Create Curam Filtering Select widget here and populate its options with the result.
  var selectOptions = new Array();
  
  // Add blank entry
  selectOptions[0] = { id:"", value:"", name:"", label:placeholderText };

  // Iterate through array and extract the "code" and "descr" items
  // to be used in the dropdown
  for (var i = 0; i < theResult.length; i++) {
   selectOptions[i+1] = 
     { id:theResult[i]["code"], label:theResult[i]["descr"] };
  }
  
  // create a store using the array
  var store = new dojo.store.Memory({ data: selectOptions });  
  var os = new dojo.data.ObjectStore({ objectStore: store });

  var oldFilteringSelect = dijit.byId(this.target.id);
  var oldOnChangeFunction = oldFilteringSelect.onChange;
  var oldFilteringSelectParams = dijit.byId(this.target.id).params;

  oldFilteringSelectParams.store = os;
  oldFilteringSelectParams.searchAttr ='label';
  oldFilteringSelectParams.maxHeight = -1;
  oldFilteringSelectParams.onChange = oldOnChangeFunction;
  oldFilteringSelect.destroy(true);


  var cellNode = dojo.byId("cell_" + this.target.id);
  
  if (dojo.isIE <= 8) {
    var targetDiv = document.createElement("div");
    var targetDivID = targetDiv.setAttribute("id", this.target.id);
    while(cellNode.hasChildNodes()) {
      cellNode.removeChild(cellNode.lastChild);
    }
    cellNode.appendChild(targetDiv);
  } else {
    cellNode.innerHTML = "<div id='" + this.target.id + "'></div>";
  }
  
  // create the widget
  var select = new curam.widget.FilteringSelect(oldFilteringSelectParams,
  dojo.byId(this.target.id));
  select.startup();
};