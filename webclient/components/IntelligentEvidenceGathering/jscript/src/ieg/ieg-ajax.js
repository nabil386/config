/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013, 2019. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/*
 * Modification History
 * --------------------
 * 11-Sep-2019  JD   [RTC248549]  Updated to use IEGFilteringSelect widget and
 * 								  pass params from old widget into newly
 *								  created one.
 * 22-Apr-2013  ROR  [CR00381706] Use OneUI FilteringSelect widget instead of
 *                                 standard Select widget.
 */

require(["ieg/widget/IEGFilteringSelect",
  "dojo/data/ObjectStore",
  "dojo/store/Memory",
  "dojo/json",  
  "dijit/registry"]);
  
// TODO: In this method we take a JSON response from the server, convert it to 
// an array, use the array as a provider for data stores and use the data 
// stores to populate the widget. This is all a bit long winded.
// There are better, newer ways of doing this where we can use JSON data 
// directly to populate the widget and simply specify which parameters/keys in
// the data is to be used in the widget.  

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
    //oResult = dojo.JSON.parse(oResult);
    if (oResult instanceof Array) {
      if (opAlias == "getCodeTableSubset") {
        filler.fillCTWithBlank(oResult, placeholderText); 
      }
    }
  };

  // Do AJAX post
  // Response is JSON in the format...
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
* This function creates and populates the dropdown. The results of the 
* AJAX call are passed in. These are converted into a Dojo store which is then
* used as the data provider for the dropdown.
*/
IEGAJAXCall.prototype.fillCTWithBlank = function (theResult, placeholderText) {
  
  // Create Dijit Select widget here and populate its options with the result.
  var selectOptions = new Array();
  
  // add in a blank entry
  selectOptions[0] = { id:"", value:"", name:"", label:placeholderText };

  // iterate through the array and extract the "code" and "descr" items
  // to be used in the dropdown
  for (var i = 0; i < theResult.length; i++) {
   selectOptions[i+1] = 
     { id:theResult[i]["code"], label:theResult[i]["descr"] };
  }
  
  // create a store using the array
  var store = new dojo.store.Memory({ data: selectOptions });  
  var os = new dojo.data.ObjectStore({ objectStore: store });

  var oldWidget = dijit.byId(this.target.id);
  var oldOnChangeFunction = oldWidget.onChange;
  var oldWidgetParams = dijit.byId(this.target.id).params;

  oldWidgetParams.store = os;
  oldWidgetParams.searchAttr = 'label';
  oldWidgetParams.maxHeight = -1;
  oldWidgetParams.onChange = oldOnChangeFunction;
  oldWidget.destroy(true);

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
  var select = new ieg.widget.IEGFilteringSelect(oldWidgetParams,
    dojo.byId(this.target.id));
  select.startup();
};