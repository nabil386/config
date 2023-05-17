/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
dojo.provide("RemoveItemsActionControl");

/* Modification History 
 * ====================
 * 27-Sep-2012  BD [CR00339777] Dojo 1.7 Upgrade. Migrate code, move ListController
 *                              from curam.* into the global namespace. 
 * 27-Jun-2012  SD [CR00330135] Fix localization bug.
 * 20-Jun-2012  DG [CR00329440] Added localization support.
 * 26-Jan-2010  BD [CR00245035] Add mouse events that add CSS classes. This is 
 *                              to circumvent an issue with the hover pseudo 
 *                              class that causes performance issues in IE7
 * 01-Nov-2010  BD [CR00224983] Add uimPageId as member property that is set 
 *                              when creating the widget rather than reading 
 *                              from each check box in the list. Added 
 *                              functions for updating the list info display as
 *                              items are removed from the list by this widget.
 * 12-Sep-2010  GP [CR00215988] Initial Version
 */

require(["dijit/_Widget","dijit/_Templated"]);
dojo.requireLocalization("curam.application", "RemoveItemsActionControl");

dojo.declare("RemoveItemsActionControl", [dijit._Widget, dijit._Templated], {
    removeItemsActionControlText:"",
    listId:"",
    uid:"001",
    uimPageId:"",
    button:null,
    templateString:"<span class='remove-button button-disabled' data-dojo-attach-point=\"button\" id=\"button\" listId=${listId} uimPageId=${uimPageId} data-dojo-attach-event='onclick:_removeItems, onmouseenter:_mouseenter,  onmouseout:_mouseout'><div class='podcontrol-button-left'><div class='podcontrol-button-right'><div class='podcontrol-button-center'>${removeItemsActionControlText}</div></div></div></span>",
    constructor: function(args){
     this.listId=args.listId;
     var bundle = new curam.util.ResourceBundle("RemoveItemsActionControl");
     this.removeItemsActionControlText = bundle.getProperty("RemoveItemsActionControl.templateString");
	   this.removeItemsActionControlText = "Remove";

    },
    postCreate: function(){
     if(!this.listId){
      console.error("no list id");
      return;
     }
    },
    
    startup: function(){
     var topic = "item/checked/" + this.listId;
     var _this = this;
     dojo.subscribe(topic, _this, function(item){
        if(this._findCheckedItems(_this).length > 0){
         dojo.attr(this.button, "class", "remove-button button-enabled");
        } else {
         dojo.attr(this.button,"class", "remove-button button-disabled");
        }
     });
    },
    
    _findCheckedItems: function (_this){
     var table = dojo.byId(_this.listId);
     
     var rows = dojo.query("tr", table);
     
     return dojo.filter(rows, function(row){
      
        return _this._isRowChecked(row);
     });
    },
      
    _isRowChecked: function (row){
      
      var inputControlList = dojo.query('input', row);
      
      for (i = 0; i < inputControlList.length; i++) {
        
        inputelem = inputControlList[i];
        
        if (inputelem.nodeType === 1 &&  
         inputelem.type === 'checkbox') {        
         
          if (inputelem.checked === true ) {
      return 1;
    }
      }
     }
      
      return 0;
    },
    
    _removeItems: function(){
      
      if(this._findCheckedItems(this).length === 0){
        return 0;
      }
      
      var table = dojo.byId(this.listId);
      var rows = dojo.query("tr", table);
      var selectedRows;
      var j = 0;
      var uimPageID;
      var removedItems = 0;
      
      for (var k = 0; k < rows.length; k++) {
              
        var inputControlList = dojo.query('input', rows[k]);
             
        for (var i = 0; i < inputControlList.length; i++) {
    
          inputelem = inputControlList[i];
        
          if (inputelem.nodeType === 1 &&  
              inputelem.type === 'checkbox') {        

            if (inputelem.checked === true ) {
      
              if (j === 0) {
                selectedRows = inputelem.value;
              } else {
                selectedRows = selectedRows + ',' + inputelem.value;
              }
      
              rows[k].parentNode.removeChild(rows[k]);  
              removedItems++;
              j++;
            }
          }
        }         
      }
       
      var context = "4096";
      dojo.xhrPost({
        url: this.uimPageID+"Page.do",
        load: function(type, data, evt){ 
          console.log("successful post");
        },
        error: function(type, error){ 
          console.log("got error");
        },
        content: {o3ctx: context, rowsToBeDeleted: selectedRows},
        mimetype: "text/json"
      });      

      if(this._findCheckedItems(this).length > 0){
        dojo.attr(this.button, "class", "remove-button button-enabled");
      } else {
        dojo.attr(this.button, "class", "remove-button button-disabled");
      }
      this._publish("remove",this._getVisibleItems().length, removedItems);
    },
    
    _publish: function(event, visibleItems, removedItems){
      
      // summary: 
      //  Publish an event for interested parties.
      //  The event is published on 2 channels
      //   1. A target communication to the listcontroller
      //   2. A global broadcast which just indicates that a list sizing event 
      //      occured. This passes the Pod id that contains the list.
      curam.debug.log("ListController._publish");
      
      var topic = "/curam/list/changed/" + this.listId;
      var globalTopic = "/curam/list/changed";
      dojo.publish(topic,
         [{ 
            'listId':this.listId,
            'event': event,
            'visibleItems':visibleItems, 
            'removedItems':removedItems
         }]);
      
      var podId = this._findContainingPod(dojo.byId(this.listId));
      dojo.publish(globalTopic,
          [{ 
             'listId':this.listId,
             'event': event,
             'visibleItems':visibleItems,
             'removedItems':removedItems,
             'podId':podId
          }]);
    },
    
    _findContainingPod: function(list) {
      
      // summary: 
      //  Find the Pod Node that containds this list.    
      //  Uses the known class 'pod-styling' to identify the Pod Node. 
      
      if (!list) {
        return null;
      }
      
      var parent = list.parentNode;
      
      if(parent==null){
        console.log("Error: The target node " + targetNodeName + "was not found on this branch of the DOM");
        return null;
      }

      if (dojo.hasClass(parent, "pod-styling")) {
        return parent.id;
      } else {
        var parent = this._findContainingPod(parent);
        return parent;
      }
    },
    
    _getVisibleItems: function(){

      curam.debug.log("ListController_getVisibleItems");
      // summary:
      //    Return the list of visible items in the table.
    
      // Get the body of the table, this ensures we don't count the column 
      // headers
      var list = dojo.byId(this.listId);
      var table = dojo.query("tbody", list)[0];
      var visibleItems = dojo.filter(table.rows, function(item){
      
        if(!dojo.hasClass(item, "blocked")){
          return item;
        }
      
      });
      
      return visibleItems;
            
    },
    
    _mouseenter: function() {
    	dojo.addClass(this.button, "hover")
    },
    
    _mouseout: function() {
    	dojo.removeClass(this.button, "hover")
    }
    
    

});
                