/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012,2019. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/*
 * Modification History
 * --------------------
 * 11-Sep-2019  JD   [RTC248549]  Updated resetNext function to cater for
 *								  FilteringSelect widget.
 * 11-Aug-2017  CMC  [RTC 200064] Switching internal IEG to use Curam 
 * 							      Filtering Select. 
 * 26-Apr-2017  AZ   [RTC 193795] Use plain select element on mobile devices.
 * 22-Apr-2013  ROR  [CR00381706] Use OneUI FilteringSelect widget instead of
 *                                 standard Select widget.
 */

/**
* Various functions to support CodeTable Hierarchies within IEG.
*/
var CodeTableHierarchy = {

  /**
  * Initialise the holders for the data. i.e. Each selection will cause new
  * data to be loaded from the server. We need to have holders in place that
  * are populated when we receive data back from the server (via AJAX).
  * This function is called on on page load from IEGCTHierarchyListEditRenderer.
  */
  initLists: function(noOptionCode, noOptionDesc, isExternal, 
    ddInfo, placeholderText) {
    var next = null;
    var node;
    var nodeValue = null;
    
    // Iterate in reverse to create a forward-linked list.
    for (var i = ddInfo.length - 1; i >= 0; i--) {
      node = dojo.byId(ddInfo[i].id);
      if (node) {
        nodeValue = node.value;
      }
      next = new CodeTableHierarchy.DropDown(
         dojo.byId(ddInfo[i].id), ddInfo[i].ctName,
         noOptionCode, noOptionDesc, next, isExternal, ddInfo[i].id,
         placeholderText);
    }
  },

  /**
  * A Dropdown "Object" which is created above using
  *  "new CodeTableHierarchy.DropDown"
  */
  DropDown: function(node, codeTableName, noOptionCode, noOptionDesc, 
    next, isExternal, nodeId, placeholderText) {
    this.node = node;
    this.codeTableName = codeTableName;
    this.noOptionCode = noOptionCode;
    this.noOptionDesc = noOptionDesc;
    this.next = next;
    this.nodeId = nodeId;
    this.isExternal = isExternal;
    this.isMobile = ( dojo.isIos > 5 || dojo.isAndroid > 4 );
    this.placeholderText = placeholderText;
    var _this = this;
    
    /**
    * This function makes AJAX requests to the server to populate the second
    * dropdown based on the selection made in the first dropdown.
    * It is triggered by an onChange event which is attached below. 
    */
    this.populate = function() {
      if (_this.isMobile) {
        if (!_this.node.value) {
          _this.resetNext(_this);
        } else if (_this.next != null) {
          _this.resetNext(_this.next);
          new AJAXCall(_this.next.node).doRequest('getCodeTableSubset',
            [_this.codeTableName, _this.node.value], false, true);
        }
      } else {
        var nodeValue = dijit.byId(_this.nodeId).attr('value');
        if (!nodeValue) {
          _this.resetNext(_this);
        } else if (_this.next != null) {
          _this.resetNext(_this.next);
          new IEGAJAXCall(_this.next.node).doRequest('getCodeTableSubset',
            [_this.codeTableName, nodeValue], false, true, placeholderText);
        }   
      }
    };

    /**
    *
    */
    this.resetNext = function(dropDown) {
      var dd = dropDown;
      while (dd.next != null) {
          dd = dd.next;
          dd.node.innerHTML = "";
          
          if (this.isMobile && isExternal != "true") {
              dd.node.options[0] = new Option(dd.noOptionDesc, dd.noOptionCode);
          } else if (!this.isMobile) {
          
  	        var selectOptions = new Array();
  	
  	        // Add blank entry
  	        selectOptions[0] = { id:dd.noOptionCode, value:dd.noOptionCode, name:dd.noOptionDesc, label:dd.noOptionDesc};
  	
  	        // create a store using the array
  	        var newStore = new dojo.store.Memory({ data: selectOptions });
  	        dijit.byId(dd.nodeId).set("store", newStore);
  	        dijit.byId(dd.nodeId).set("item", dijit.byId(dd.nodeId).store.get(0));
  	        dijit.byId(dd.nodeId).setDisplayedValue(noOptionDesc);
          }

        }
      };

    /**
    * Attach the "populate()" function call to the onChange event 
    */
    if (next != null) {
      if (!_this.isMobile) {
        dojo.connect(dijit.byId(this.nodeId), "onChange", this.populate);
      } else {
        dojo.connect(this.node, "onchange", this.populate);
      }
    }
  }
};