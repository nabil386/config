/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
dojo.provide("ListController");

/*
 * Modification History
 * --------------------
 * 27-Sep-2012 BD [CR00339777] Dojo 1.7 Upgrade. Migrate code, move ListController
 *                             from curam.* into the global namespace. 
 * 01-Nov-2010 BD [CR00224983] Removed display information feature from this 
 *                             class. This feature is now provided separately 
 *                             and any interaction between the 2 widgets is 
 *                             carried by the events system. 
 * 01-Sep-2010 BD [CR00218959] Added support for form field updates. The form
 *                             field is passed to the list controller which 
 *                             updates the value of the list size. The
 *                             form field can be queried when the form is 
 *                             submitted.
 * 25-Aug-2010 BD [CR00217234] Now publishing global event when list size
 *                             changes. This is to facilitate the PodContainer
 *                             to trigger a save when any list changes it size.
 *                             Added supporting function to find the id of the
 *                             Pod containing the list.
 * 09-Aug-2010 BD [CR00212945] Initial version.
 */

/**
 * Create and manage a list controller widget that controls the size of the 
 * list. Show and Hide items in the list by clicking on the add and remove
 * control buttons. Broadcast a message when an item has changed to let interest 
 * parties know.
 * 
 */
require(["dijit/_Widget","dijit/_Templated","curam/debug"]);

dojo.declare("ListController", [dijit._Widget, dijit._Templated], {

  // formFieldId: String
  //   Id of the input field that will store and return the 
  //   current list size when a page is submitted, such as a 
  //   save action.
  formFieldId:"",
  // showItemImage: String
  //   An URI string to the image for the show control.
  showItemImage:"",
  // hideItemImage: String
  //   An URI string to the image for the hide control.
  hideItemImage:"",
  // minListSize: Integer
  //   The mininum list size. Defaulted to 1.
  minListSize:1,
  // fullListSize: Integer
  //   The full list size of which this list is a subset.
  fullListSize:null,
  // listId: String 
  //   The Id of the associated list.
  listId:"",
  // listContainer: Node
  //   A DOM node that represents the actual list.
  listContainer:null,
  // showControl: Node
  //   A DOM node that represents the show control.
  showControl:null,
  // hideControl: Node
  //   A DOM node that represents the hide control.
  hideControl:null,
  // Unique ID: String
  //   The unique identifier for this instance of a UIMController.
  uid:"",
  // classList: String
  //   A String representing classes to be added to the div wrapper. 
  classList: "",

  templateString:
    '<div id="listcontroller_${uid}" listId=${listId} class="listcontroller ${classList}" dojoAttachPoint="listController">' +
      '<span dojoAttachPoint="showControl" dojoAttachEvent="onclick:showItem" class="show"><img src=${showItemImage}></span>' +
      '<span dojoAttachPoint="hideControl" dojoAttachEvent="onclick:hideItem" class="hide"><img src=${hideItemImage}></span>' +
    '</div>',
  
  constructor: function(args){
    // summary:
    //   Constructor, create a ListController widget.
    //   
    //   parameter: listId, the unique identifier for the list.
    curam.debug.log("ListController.construct");
    
    this.listId = args.listId;
    if (!this.listId) {
      console.error("ERROR: No associated list ID provided.");
      return;
    }
    this.showItemImage = args.showItemImage;
    if (!this.showItemImage){
      console.error(
          "ERROR: No 'showItemImage' parameter provided for show control");
    }
    this.hideItemImage = args.hideItemImage;
    if (!this.hideItemImage){
      console.error(
          "ERROR: No 'hideItemImage' parameter provided for hide control");
    }
    if (args.minListSize) {
      this.minListSize = args.minListSize;
    }
    if (args.fullListSize) {
      this.fullListSize = args.fullListSize;
    }

    curam.debug.log("new ListController()...");
    curam.debug.log("id:" + this.listId);
    curam.debug.log("Minimum List Size:" + this.minListSize);
    curam.debug.log("Full List Size:" + this.fullListSize);       
    
    if (args.formFieldId) {
      this.formFieldId = args.formFieldId;
      curam.debug.log("Form Field Id:" + this.formFieldId);
    }
    
    return this.listController;

  },
  
  // summary:
  //   postCreate, after the widget has been created add a listener for events
  //               coming from the iframe, then load the iframe. It is 
  //               important that the order is maintained here as adding the
  //               listener after loading the iframe will mean the events are
  //               missed.
  postCreate: function(){
    
    curam.debug.log("ListController.postCreate");
    
  },
  
  startup: function(){

    curam.debug.log("ListController.startup");
    // Enable / Disable the list sizers based on the size of the list and the
    // min/max values.

    // Find the list container.
    curam.debug.log("Searching for list container by Id: " + this.listId);
    if(!this.listId || this.listId == '') {
      console.error("No list Id provided, cannot continue creating ListController.");
      return;
    }
    this.listContainer = dojo.query('#'+this.listId)[0];

    //
    // Initialize the controllers
    //
    
    if(!this.listContainer){
      curam.debug.log("No associated list found for controller!");
      dojo.addClass(this.showControl, "disabled");
      dojo.addClass(this.hideControl, "disabled");
      return;
    }

    var visibleItems = this._getVisibleItems().length;
    this._updateListSizeField(visibleItems);
    
    if (visibleItems <= this.minListSize) {
      dojo.addClass(this.hideControl, "disabled");
    }else{
      dojo.addClass(this.hideControl, "enabled");
    }
    
    if (this._getAllItems().length == this._getVisibleItems().length) {
      dojo.addClass(this.showControl, "disabled");
    }else{
      dojo.addClass(this.showControl, "enabled");
    }
    
    this._started=true;
    curam.debug.log("startup finished!");
  },

  showItem: function(){
    
    // summary:
    //   Show the next list item.
    //
    //   Reacts to an mouse click action on the show control.
    //   Shows the next item in the list, unless there are no more items.
    //   If the list size was the minimum and the hide control was disabled then
    //   the hide button will be re-enabled.
    curam.debug.log("ListController.showItem");
    
    if (dojo.hasClass(this.showControl, "disabled")){
      return;
    }

    var visibleItems = this._getVisibleItems();
    var numVisibleItems = visibleItems.length;
    var allItems = this._getAllItems();
    var totalItems = allItems.length;      
          
    // Exit if we have already reached the full list size.
    if(numVisibleItems == totalItems){
      return;
    }
    
    // Show the row.
    var nextItem = allItems[numVisibleItems];
    dojo.removeClass(nextItem, "blocked");
    numVisibleItems++;  
    this._updateListSizeField(numVisibleItems);

    // Disable Show Control when the max size is reached.
    if(numVisibleItems == totalItems){    
      dojo.replaceClass(this.showControl, "disabled", "enabled");
    }

    // Enable Hide Control when greater than min list size.
    if (numVisibleItems > this.minListSize) {
      if (dojo.hasClass(this.hideControl, "disabled")) {    
        dojo.replaceClass(this.hideControl, "enabled", "disabled");
      }
    }
    
    this._publish("show", numVisibleItems);
  },

  hideItem: function(){
  
    // summary:
    //   Hide the last list Item
    //
    //   Reacts to an mouse click action on the show control.
    //   Shows the next item in the list, unless there are no more items.
    //   If the list size was the minimum and the hide control was disabled then
    //   the hide button will be re-enabled.
    curam.debug.log("ListController.hideItem");
    
    if (dojo.hasClass(this.hideControl, "disabled")){
      return;
    }

    var visibleItems = this._getVisibleItems();
    var numVisibleItems = visibleItems.length;
    
    // Exit if we have already reached the minimum list size.
    if(numVisibleItems == this.minListSize){
      return;
    }
    
    // Hide the row.
    var lastItem = visibleItems[numVisibleItems-1];
    dojo.addClass(lastItem, "blocked");
    numVisibleItems--;
    this._updateListSizeField(numVisibleItems);

    // Disable Hide Control when the min size is reached.
    if(numVisibleItems == this.minListSize){    
      dojo.replaceClass(this.hideControl, "disabled", "enabled");
    }
    // Enable show Control when the less than list size.
    var totalItems = this._getAllItems().length;
    if (numVisibleItems < totalItems) {
      if (dojo.hasClass(this.showControl, "disabled")) {    
        dojo.replaceClass(this.showControl, "enabled", "disabled");
      }
    }
    
    // Publish an event for interested parties.
    this._publish("hide", numVisibleItems);
    
  },
  
  _getAllItems: function(){
  
    // summary:
    //    Return the full list of items in the table.
    curam.debug.log("ListController._getAllItems");
  
    // Get the body of the table, this ensures we don't count the column headers
    var table = dojo.query("tbody", this.listContainer)[0];
    return table.rows;
          
  },
  
  _getVisibleItems: function(){

    curam.debug.log("ListController_getVisibleItems");
    // summary:
    //    Return the list of visible items in the table.
  
    // Get the body of the table, this ensures we don't count the column headers
    var table = dojo.query("tbody", this.listContainer)[0];
    var visibleItems = dojo.filter(table.rows, function(item){
    
      if(!dojo.hasClass(item, "blocked")){
        return item;
      }
    
    });
    
    return visibleItems;
          
  },

  _publish: function(event, visibleItems){
    
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
          'visibleItems':visibleItems 
       }]);
    
    var podId = this._findContainingPod(this.listContainer);
    dojo.publish(globalTopic,
        [{ 
           'listId':this.listId,
           'event': event,
           'visibleItems':visibleItems, 
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
  
  _updateListSizeField: function(size){
    
    // summary: 
    //  Update the list size value in the associated form field.
    //  The value can be passed back to the server when the page is submitted.
    
    if (this.formFieldId !== "") {
      var listSizeInputField = dojo.byId(this.formFieldId);
      // Use a prefix on the field to diffentiate from other form fields.
      listSizeInputField.value = "list:" + size;
    }
  }
  
  
});
