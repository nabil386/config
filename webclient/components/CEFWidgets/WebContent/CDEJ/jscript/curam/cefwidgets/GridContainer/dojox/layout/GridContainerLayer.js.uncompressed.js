/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/*
  Copyright (c) 2004-2009, The Dojo Foundation All Rights Reserved.
  Available via Academic Free License >= 2.1 OR the modified BSD license.
  see: http://dojotoolkit.org/license for details
*/

/**
 * Curam Modifications
 * ====================
 * 03-Oct-2012 BD [CR00345824] Dojo 1.7 Upgrade. Migrate code. Bug fix on _insertChild
 *                             function which was using typeof incorrectly.
 * 04-Sep-2011 BD [CR00285772] Switch subscription to "contentPane.pageLoaded" to 
 *                             "/curam/main-content/page/loaded" to reflect changes 
 *                             made by the CDEJ.
 * 24-Jan-2011 BD [CR00243768] Added functionality to support lazy loading of Pods.
 * 14-Jan-2011 BD [CR00242417] Move the enable drag and drop code to a function that occurs after the 
 *                             page has loaded. Publish an event to acknowledge the event has occured.
 * 04-Jan-2011 BD [CR00238694] Reinstated the calls to inherited in the postCreate function and the call
 *                             to organize children in startup for
 *                             GridContainerLite to remove a bug introduced that was causing the 
 *                             pod positions to be lost.
 * 10-Dec-2010 BD [CR00237694] Removed calls to resize functions to improve performance. 
 *                             These calls when removed did not seem to affect the functionality 
 *                             of the page. Moved the activation of drag and drop to after the page has 
 *                             loaded.
 * 27-Oct-2010 BD [CR00224614] Moved class manipulation (add/remove) from post 
 *                             create functions to buildRendering functions to realize performance gains. 
 *                             The logic here is that manipulating the DOM nodes after they are inserted 
 *                             into the DOM is more expensive than doing it prior to insertion. Therefore
 *                             were possible we do this upfront so that the load time for the widget is 
 *                             shorter.
 * 02-Sep-2010 CH [CR00218087] Added a style attribute of 'closed' to the Portlet and published a 'close' 
 *                             event in the _onClose method.
 * 01-Sep-2010 BD [CR00219082] Fixed bug in _selectFocus method. The code was allowing a null
 *                             pointer exception to occur where no focus was found. Code now just
 *                             returns in that scenario.
 */

if(!dojo._hasResource["dojox.layout.GridContainerLite"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.layout.GridContainerLite"] = true;
dojo.provide("dojox.layout.GridContainerLite");
 
require(["curam/cefwidgets/pods/Pod","dojox/mdnd/Moveable","dojox/mdnd/AreaManager","dojox/mdnd/DropIndicator", "dojox/mdnd/dropMode/OverDropMode", "dojox/mdnd/AutoScroll"]);
dojo.declare(
  "dojox.layout.GridContainerLite",
  [dijit.layout._LayoutWidget, dijit._Templated],
{
  // summary:
  //    The GridContainerLite is a container of child elements that are placed in a kind of grid.
  //
  // description:
  //    GridContainerLite displays the child elements by column
  //    (ie: the children widths are fixed by the column width of the grid but
  //              the children heights are free).
  //    Each child is movable by drag and drop inside the GridContainer.
  //    The position of other children is automatically calculated when a child is moved.
  //
  // example:
  //  | <div dojoType="dojox.layout.GridContainerLite" nbZones="3" isAutoOrganized="true">
  //  |   <div dojoType="dijit.layout.ContentPane">Content Pane 1 : Drag Me !</div>
  //  |   <div dojoType="dijit.layout.ContentPane">Content Pane 2 : Drag Me !</div>
  //  |   <div dojoType="dijit.layout.ContentPane">Content Pane 3 : Drag Me !</div>
  //  | </div>
  //
  // example:
  //  | dojo.ready(function(){
  //  |   var cpane1 = new dijit.layout.ContentPane({
  //  |     title:"cpane1", content: "Content Pane 1 : Drag Me !"
  //  |   }),
  //  |   cpane2 = new dijit.layout.ContentPane({
  //  |     title:"cpane2",
  //  |     content: "Content Pane 2 : Drag Me !"
  //  |   }),
  //  |   cpane3 = new dijit.layout.ContentPane({
  //  |     title:"cpane3",
  //  |     content: "Content Pane 3 : Drag Me !"
  //  |   });
  //  |
  //  |   var widget = new dojox.layout.GridContainerLite({
  //  |     nbZones: 3,
  //  |     isAutoOrganized: true
  //  |   }, dojo.byId("idNode"));
  //  |   widget.addChild(cpane1, 0, 0);
  //  |   widget.addChild(cpane2, 1, 0);
  //  |   widget.addChild(cpane3, 2, 1);
  //  |   widget.startup();
  //  | });
  
  //  autoRefresh: Boolean
  //    Enable the refresh of registered areas on drag start. 
  autoRefresh: true,


  // templateString: String
  //    template of gridContainer.
  templateString: dojo.cache("dojox.layout", "resources/GridContainer.html", "<div id=\"${id}\" class=\"gridContainer\" dojoAttachPoint=\"containerNode\" tabIndex=\"0\" dojoAttachEvent=\"onkeypress:_selectFocus\">\r\n\t<div dojoAttachPoint=\"gridContainerDiv\">\r\n\t\t<table class=\"gridContainerTable\" dojoAttachPoint=\"gridContainerTable\" cellspacing=\"0\" cellpadding=\"0\">\r\n\t\t\t<tbody>\r\n\t\t\t\t<tr dojoAttachPoint=\"gridNode\" >\r\n\t\t\t\t\t\r\n\t\t\t\t</tr>\r\n\t\t\t</tbody>\r\n\t\t</table>\r\n\t</div>\r\n</div>\r\n"),

  // dragHandleClass: Array :
  //    CSS class enabling a drag handle on a child.
  dragHandleClass: "dojoxDragHandle",

  // nbZones: Integer
  //    The number of dropped zones, by default 1.
  nbZones: 1,

  // doLayout: Boolean
  //    If true, change the size of my currently displayed child to match my size.
  doLayout: true,

  // isAutoOrganized: Boolean
  //    If true, widgets are organized automatically,
  //    else the attribute colum of child will define the right column.
  isAutoOrganized: true,

  // acceptTypes: Array
  //    The GridContainer will only accept the children that fit to the types.
  acceptTypes: [],
  
  // colWidths: String
  //    A comma separated list of column widths. If the column widths do not add up
  //    to 100, the remaining columns split the rest of the width evenly
  //    between them.
  colWidths: "",
  
  // lazyLoad: Boolean
  //    Lazy load Pods into the Grid. If true the container will wait until
  //    all Pods are loaded before enabling the drag and drop feature.
  lazyLoad: false,
  
  // podDropDelay: Integer
  //    Number of milliseconds between insert of new Pods into the container.
  //    This value is used when lazy loading Pods. Pods are added 1 at a time 
  //    with a short delay for effect.
  podDropDelay: 500,
  
  // podFadeInDuration: Integer
  //    Number of milliseconds it takes for each lazy loaded Pod to appear.
  //    This value will be applied to the dojo.fadeIn feature that occurs when
  //    Pods are added to an existing container.
  podFadeInDuration: 500,
  
  constructor: function(/*Object*/props, /*DOMNode*/node){
    this.acceptTypes = (props || {}).acceptTypes || ["text"];
    this._disabled = true;
  },

  postCreate: function(){
    //console.log("dojox.layout.GridContainerLite ::: postCreate");
    
    // Curam Modification: Removed calls to inherited arguments. These were 
    // proving to be quite expensive at page load time. Removing them has not
    // appeared to harm functionality.
    //this.inherited(arguments);
    this._grid = [];

    this._createCells();

    // need to resize dragged child when it's dropped.
    // Curam Modification: Removed the resize actions. The logic being that the
    // columns in the PodContainer Grid are always of equal width so they
    // should not require a resizing. However, after removing these calls the 
    // resizing appears to work anyway.
    //this.subscribe("/dojox/mdnd/drop", "resizeChildAfterDrop");
    //this.subscribe("/dojox/mdnd/drag/start", "resizeChildAfterDragStart");
    
    // Curam Modification: Move the enabling of drag functionality to a separate
    // function which gets called after the page has loaded.
    var topWin = curam.util.getTopmostWindow();

    // Subscribe to the page load event. When the page is loaded we fire an
    // event to lazy load the hidden Pods. After that we enable the drag and 
    // drop functionality.
    this.handler = topWin.dojo.subscribe("/curam/main-content/page/loaded", this, function(){
      _gridContainer = this;
      // Remove the call to inact the dragFunctionality. This is now handled in the PodContainer.js file.
      //_gridContainer.dragFunctionality();
      dojo.publish("pods.readyformore", [_gridContainer]);
      topWin.dojo.unsubscribe(_gridContainer.handler);
      
    });
    
    this._dragManager = dojox.mdnd.areaManager();
    // console.info("autorefresh ::: ", this.autoRefresh);
    this._dragManager.autoRefresh = this.autoRefresh;

    //  Add specific dragHandleClass to the manager.
    this._dragManager.dragHandleClass = this.dragHandleClass;

    if(this.doLayout){
      this._border = {
        'h':(dojo.isIE) ? dojo._getBorderExtents(this.gridContainerTable).h : 0,
        'w': (dojo.isIE == 6) ? 1 : 0
      }
    }
    else{
      dojo.style(this.domNode, "overflowY", "hidden");
      dojo.style(this.gridContainerTable, "height", "auto");
    }
    // Call postCreate of dijit.layout._LayoutWidget.
    //this.inherited(arguments);

  },

  startup: function(){
    //console.log("dojox.layout.GridContainerLite ::: startup");
    // Curam Modification: The commented out code has been moved to a separate
    // function that will get called after the page has loaded. This speeds up
    // the time spent loading.
    //if(this._started){ return; }

    if(this.isAutoOrganized){
      this._organizeChildren();
    }
    else{
      this._organizeChildrenManually();
    }

    // Need to call getChildren because getChildren return null
    // The children are not direct children because of _organizeChildren method
    dojo.forEach(this.getChildren(), function(child){ child.startup(); });

    //// Need to enable the Drag And Drop only if the GridContainer is visible.
    //if(this._isShown()){
    //  this.enableDnd();
    //}
    //this.inherited(arguments);
  },
  
  dragFunctionality: function(){
    // summary:
    //    [Curam] Add drag functionality. This was moved our of the startup function to 
    //    here where it can be called after the page has loaded. This was done to reduce the 
    //    pressure on the page at startup which was causing performance issues.
    //console.log("dojox.layout.GridContainerLite ::: dragFunctionality");
    if (this._started) {
           return;
    }
    if (this.isAutoOrganized) {
      this._organizeChildren();
      
    } else {
      
      this._organizeChildrenManually();
    }
    if (!this.lazyLoad && this._isShown()) {
      this.enableDnd();
    }
  },

  resizeChildAfterDrop: function(/*Node*/node, /*Object*/targetArea, /*Integer*/indexChild){
    // summary:
    //    Resize the GridContainerLite inner table and the dropped widget.
    // description:
    //    These components are resized only if the targetArea.node is a
    //    child of this instance of gridContainerLite.
    //    To be resized, the dropped node must have also a method resize.
    // node:
    //    domNode of dropped widget.
    // targetArea:
    //    AreaManager Object containing information of targetArea
    // indexChild:
    //    Index where the dropped widget has been placed
    // returns:
    //    True if resized.

    //console.log("dojox.layout.GridContainerLite ::: resizeChildAfterDrop");
    if(this._disabled){
      return false;
    }
    if(dijit.getEnclosingWidget(targetArea.node) == this){
      var widget = dijit.byNode(node);
      if(widget.resize && dojo.isFunction(widget.resize)){
        widget.resize();
      }
      if(this.doLayout){
        var domNodeHeight = this._contentBox.h,
          divHeight = dojo.contentBox(this.gridContainerDiv).h;
        if(divHeight >= domNodeHeight){
          dojo.style(this.gridContainerTable, "height",
              (domNodeHeight - this._border.h) + "px");
        }
      }
      return true;
    }
    return false;
  },

  resizeChildAfterDragStart: function(/*Node*/node, /*Object*/sourceArea, /*Integer*/indexChild){
    // summary:
    //    Resize the GridContainerLite inner table only if the drag source
    //    is a child of this gridContainer.
    // node:
    //    domNode of dragged widget.
    // sourceArea:
    //    AreaManager Object containing information of sourceArea
    // indexChild:
    //    Index where the dragged widget has been placed

    //console.log("dojox.layout.GridContainerLite ::: resizeChildAfterDragStart");
    if(this._disabled){
      return false;
    }
    if(dijit.getEnclosingWidget(sourceArea.node) == this){
      this._draggedNode = node;
      if(this.doLayout){
        dojo.marginBox(this.gridContainerTable, {
          'h': dojo.contentBox(this.gridContainerDiv).h - this._border.h
        });
      }
      return true;
    }
    return false;
  },

  getChildren: function(){
    // summary:
    //    A specific method which returns children after they were placed in zones.
    // returns:
    //    An array containing all children (widgets).
    // tags:
    //    protected

    //console.log("dojox.layout.GridContainerLite ::: _getChildren");
    var children = [];
    dojo.forEach(this._grid, function(dropZone){
      var widgetsList = dojo.query("> [class]", dropZone.node);
      children = children.concat(dojo.query("> [widgetId]", dropZone.node).map(dijit.byNode));
    });
    return children;  // Array
  },

  _isShown: function(){
    // summary:
    //    Check if the domNode is visible or not.
    // returns:
    //    true if the content is currently shown
    // tags:
    //    protected

    //console.log("dojox.layout.GridContainerLite ::: _isShown");
    if("open" in this){   // for TitlePane, etc.
      return this.open;   // Boolean
    }
    else{
      var node = this.domNode;
      return (node.style.display != 'none') && (node.style.visibility != 'hidden') && !dojo.hasClass(node, "dijitHidden"); // Boolean
    }
  },

  layout: function(){
    // summary:
    //    Resize of each child

    //console.log("dojox.layout.GridContainerLite ::: layout");
    if(this.doLayout){
      var contentBox = this._contentBox;
      dojo.marginBox(this.gridContainerTable, {
        'h': contentBox.h - this._border.h
      });
      dojo.contentBox(this.domNode, {
        'w': contentBox.w - this._border.w
      });
    }
    dojo.forEach(this.getChildren(), function(widget){
      if(widget.resize && dojo.isFunction(widget.resize)){
        widget.resize();
      }
    });
  },

  onShow: function(){
    // summary:
    //    Enabled the Drag And Drop if it's necessary.

    //console.log("dojox.layout.GridContainerLite ::: onShow");
    if(this._disabled){
      //console.log("enableDnd");
      this.enableDnd();
    }
  },

  onHide: function(){
    // summary:
    //    Disabled the Drag And Drop if it's necessary.

    //console.log("dojox.layout.GridContainerLite ::: onHide");
    if(!this._disabled){
      this.disableDnd();
    }
  },

  _createCells: function(){
    // summary:
    //    Create the columns of the GridContainer.
    // tags:
    //    protected

    //console.log("dojox.layout.GridContainerLite ::: _createCells");
    if(this.nbZones === 0){ this.nbZones = 1; }
    var accept = this.acceptTypes.join(","),
      i = 0;
      
    var origWidths = this.colWidths || [];
    var widths = [];
    var colWidth;
    var widthSum = 0;
    
    // Calculate the widths of each column.
    for(i = 0; i < this.nbZones; i++){
      if(widths.length < origWidths.length){
        widthSum += origWidths[i];
        widths.push(origWidths[i]);
      }else{
        if(!colWidth){
          colWidth = (100 - widthSum)/(this.nbZones - i);
        }
        widths.push(colWidth);
      }
    }

    i = 0;
    while(i < this.nbZones){
      // Add the parameter accept in each zone used by AreaManager
      // (see method dojox.mdnd.AreaManager:registerByNode)     
      this._grid.push({
        'node': dojo.create("td", {
          'class': "gridContainerZone",
          'accept': accept,
          'id': this.id + "_dz" + i,
          'style': {
            'width': widths[i] + "%"
          }
        }, this.gridNode)
      });
      i++;
    }
  },

  enableDnd: function(){
    // summary:
    //    Enable the Drag And Drop for children of GridContainer.

    //console.log("dojox.layout.GridContainerLite ::: enableDnd");
    var m = this._dragManager;
    dojo.forEach(this._grid, function(dropZone){
      m.registerByNode(dropZone.node);
    });
    m._dropMode.updateAreas(m._areaList);
    this._disabled = false;
  },

  disableDnd: function(){
    // summary:
    //    Disable the Drag And Drop for children of GridContainer.

    //console.log("dojox.layout.GridContainerLite ::: disableDnd");
    var m = this._dragManager;
    dojo.forEach(this._grid, function(dropZone){
      m.unregister(dropZone.node);
    });
    m._dropMode.updateAreas(m._areaList);
    this._disabled = true;
  },

  _organizeChildren: function(){
    // summary:
    //    List all zones and insert child into columns.

    //console.log("dojox.layout.GridContainerLite ::: _organizeChildren");
    var children = dojox.layout.GridContainerLite.superclass.getChildren.call(this);
    var numZones = this.nbZones,
      numPerZone = Math.floor(children.length / numZones),
      mod = children.length % numZones,
      i = 0;
    //console.log('numPerZone', numPerZone, ':: mod', mod);
    for(var z = 0; z < numZones; z++){
      for(var r = 0; r < numPerZone; r++){
        this._insertChild(children[i], z);
        i++;
      }
      if(mod > 0){
        try{
          this._insertChild(children[i], z);
          i++;
        }
        catch(e){
          console.error("Unable to insert child in GridContainer", e);
        }
        mod--;
      }
      else if(numPerZone === 0){
        break;  // Optimization
      }
    }
  },

  _organizeChildrenManually: function (){
    // summary:
    //    Organize children by column property of widget.

    //console.log("dojox.layout.GridContainerLite ::: _organizeChildrenManually");
    var children = dojox.layout.GridContainerLite.superclass.getChildren.call(this),
      length = children.length,
      child;
    for(var i = 0; i < length; i++){
      child = children[i];
      try{
        this._insertChild(child, child.column - 1);
      }
      catch(e){
        console.error("Unable to insert child in GridContainer", e);
      }
    }
  },

  _insertChild: function(/*Widget*/child, /*Integer*/column, /*Integer?*/p){
    // summary:
    //    Insert a child in a specific column of the GridContainer widget.
    // column:
    //    Column number
    // p:
    //    Place in the zone (0 - first)
    // fadeInDuration:
    //    if the Pod is lazy loaded then it will fade in for this number of milliseconds.
    // returns:
    //    The widget inserted

if ((child.declaredClass) === "curam.cefwidgets.pods.Pod"){
    //console.log("dojox.layout.GridContainerLite ::: _insertChild", child, column, p);
    var zone = this._grid[column].node,
      length = zone.childNodes.length;
    if(typeof(p) == "undefined" || p > length){
      p = length;
    }
    if(this._disabled){
      dojo.place(child.domNode, zone, p);
      dojo.attr(child.domNode, "tabIndex", "0");
    }
    else{
      if (!child.dragRestriction || child.dragRestriction==="false") {
        this._dragManager.addDragItem(zone, child.domNode, p, true, this.podFadeInDuration);
      }
      else{
        dojo.place(child.domNode, zone, p);
        dojo.attr(child.domNode, "tabIndex", "0");
      }
    }
    return child; // Widget
}
  },

  removeChild: function(/*Widget*/ widget){
    //console.log("dojox.layout.GridContainerLite ::: removeChild");
    if(this._disabled){
      this.inherited(arguments);
    }
    else{
      this._dragManager.removeDragItem(widget.domNode.parentNode, widget.domNode);
    }
  },

  addService: function(/*Object*/child, /*Integer?*/column, /*Integer?*/p){
    //console.log("dojox.layout.GridContainerLite ::: addService");
    dojo.deprecated("addService is deprecated.", "Please use  instead.", "Future");
    this.addChild(child, column, p);
  },

  addChild: function(/*Object*/child, /*Integer?*/column, /*Integer?*/p){
    // summary:
    //    Add a child in a specific column of the GridContainer widget.
    // child:
    //    widget to insert
    // column:
    //    column number
    // p:
    //    place in the zone (first = 0)
    // returns:
    //    The widget inserted

    //console.log("dojox.layout.GridContainerLite ::: addChild");
    child.domNode.id = child.id;
    dojox.layout.GridContainerLite.superclass.addChild.call(this, child, 0);
    if(column < 0 || column == undefined){ column = 0; }
    if(p <= 0){ p = 0; }
    try{
      return this._insertChild(child, column, p);
    }
    catch(e){
      console.error("Unable to insert child in GridContainer", e);
    }
    return null;  // Widget
  },
  
  _setColWidthsAttr: function(value){
    this.colWidths = dojo.isString(value) ? value.split(",") : (dojo.isArray(value) ? value : [value]);
    
    if(this._started){ 
      this._updateColumnsWidth();
    }
  },
  
  _updateColumnsWidth: function(/*Object*/ manager){
    // summary:
    //    Update the columns width.
    // manager:
    //    dojox.mdnd.AreaManager singleton
    // tags:
    //    private

    //console.log("dojox.layout.GridContainer ::: _updateColumnsWidth");
    var length = this._grid.length;

    var origWidths = this.colWidths || [];
    var widths = [];
    var colWidth;
    var widthSum = 0;
    var i;

    // Calculate the widths of each column.
    for(i = 0; i < length; i++){
      if(widths.length < origWidths.length){
        widthSum += origWidths[i] * 1;
        widths.push(origWidths[i]);
      }else{
        if(!colWidth){
          colWidth = (100 - widthSum)/(this.nbZones - i);
          
          // If the numbers don't work out, make the remaining columns
          // an even width and let the code below average
          // out the differences.
          if(colWidth < 0){
            colWidth = 100 / this.nbZones;
          }
        }
        widths.push(colWidth);
        widthSum += colWidth * 1;
      }
    }

    // If the numbers are wrong, divide them all so they add up to 100
    if(widthSum > 100){
      var divisor = 100 / widthSum;
      for(i = 0; i < widths.length; i++){
        widths[i] *= divisor;
      }
    }

    // Set the widths of each node
    for(i = 0; i < length; i++){
      this._grid[i].node.style.width = widths[i] + "%";
    }
  },

  _selectFocus: function(/*Event*/event){
    // summary:
    //    Enable keyboard accessibility into the GridContainer.
    // description:
    //    Possibility to move focus into the GridContainer (TAB, LEFT ARROW, RIGHT ARROW, UP ARROW, DOWN ARROW).
    //    Possibility to move GridContainer's children (Drag and Drop) with keyboard. (SHIFT +  ARROW).
    //    If the type of widget is not draggable, a popup is displayed.

    //console.log("dojox.layout.GridContainerLite ::: _selectFocus");
    if(this._disabled){ return; }
    var key = event.keyCode,
      k = dojo.keys,
      zone = null,
      focus = dijit.getFocus(),
      focusNode = focus.node,
      m = this._dragManager,
      found,
      i,
      j,
      r,
      children,
      area,
      widget;
    if(focusNode == null){
      return;
    }else if(focusNode == this.containerNode){
      area = this.gridNode.childNodes;
      switch(key){
        case k.DOWN_ARROW:
        case k.RIGHT_ARROW:
          found = false;
          for(i = 0; i < area.length; i++){
            children = area[i].childNodes;
            for(j = 0; j < children.length; j++){
              zone = children[j];
              if(zone != null && zone.style.display != "none"){
                dijit.focus(zone);
                dojo.stopEvent(event);
                found = true;
                break;
              }
            }
            if(found){ break };
          }
        break;
        case k.UP_ARROW:
        case k.LEFT_ARROW:
          area = this.gridNode.childNodes;
          found = false;
          for(i = area.length-1; i >= 0 ; i--){
            children = area[i].childNodes;
            for(j = children.length; j >= 0; j--){
              zone = children[j];
              if(zone != null && zone.style.display != "none"){
                dijit.focus(zone);
                dojo.stopEvent(event);
                found = true;
                break;
              }
            }
            if(found){ break };
          }
        break;
      }
    }
    else{ 
      if(focusNode.parentNode.parentNode == this.gridNode){
        var child = (key == k.UP_ARROW || key == k.LEFT_ARROW) ? "lastChild" : "firstChild";
        var pos = (key == k.UP_ARROW || key == k.LEFT_ARROW) ? "previousSibling" : "nextSibling";
        switch(key){
          case k.UP_ARROW:
          case k.DOWN_ARROW:
            dojo.stopEvent(event);
            found = false;
            var focusTemp = focusNode;
            while(!found){
              children = focusTemp.parentNode.childNodes;
              var num = 0;
              for(i = 0; i < children.length; i++){
                if(children[i].style.display != "none"){ num++ };
                if(num > 1){ break; }
              }
              if(num == 1){ return; }
              if(focusTemp[pos] == null){
                zone = focusTemp.parentNode[child];
              }
              else{
                zone = focusTemp[pos];
              }
              if(zone.style.display === "none"){
                focusTemp = zone;
              }
              else{
                found = true;
              }
            }
            if(event.shiftKey){
              var parent = focusNode.parentNode;
              for(i = 0; i < this.gridNode.childNodes.length; i++){
                if(parent == this.gridNode.childNodes[i]){
                  break;
                }
              }
              children = this.gridNode.childNodes[i].childNodes;
              for(j = 0; j < children.length; j++){
                if(zone == children[j]){
                  break;
                }
              }
              if(dojo.isMoz || dojo.isWebKit){ i-- };

              widget = dijit.byNode(focusNode);
              if(!widget.dragRestriction){
                r = m.removeDragItem(parent, focusNode);
                this.addChild(widget, i, j);
                dojo.attr(focusNode, "tabIndex", "0");
                dijit.focus(focusNode);
              }
              else{
                dojo.publish("/dojox/layout/gridContainer/moveRestriction", [this]);
              }
            }
            else{
              dijit.focus(zone);
            }
          break;
          case k.RIGHT_ARROW:
          case k.LEFT_ARROW:
            dojo.stopEvent(event);
            if(event.shiftKey){
              var z = 0;
              if(focusNode.parentNode[pos] == null){
                if(dojo.isIE && key == k.LEFT_ARROW){
                  z = this.gridNode.childNodes.length-1;
                }
              }
              else if(focusNode.parentNode[pos].nodeType == 3){
                z = this.gridNode.childNodes.length - 2;
              }
              else{
                for(i = 0; i < this.gridNode.childNodes.length; i++){
                  if(focusNode.parentNode[pos] == this.gridNode.childNodes[i]){
                    break;
                  }
                  z++;
                }
                if(dojo.isMoz || dojo.isWebKit){ z-- };
              }
              widget = dijit.byNode(focusNode);
              var _dndType = focusNode.getAttribute("dndtype");
              if(_dndType == null){
                //check if it's a dijit object
                if(widget && widget.dndType){
                  _dndType = widget.dndType.split(/\s*,\s*/);
                }
                else{
                  _dndType = ["text"];
                }
              }
              else{
                _dndType = _dndType.split(/\s*,\s*/);
              }
              var accept = false;
              for(i = 0; i < this.acceptTypes.length; i++){
                for(j = 0; j < _dndType.length; j++){
                  if(_dndType[j] == this.acceptTypes[i]){
                    accept = true;
                    break;
                  }
                }
              }
              if(accept && !widget.dragRestriction){
                var parentSource = focusNode.parentNode,
                  place = 0;
                if(k.LEFT_ARROW == key){
                  var t = z;
                  if(dojo.isMoz || dojo.isWebKit){ t = z + 1 };
                  place = this.gridNode.childNodes[t].childNodes.length;
                }
                // delete of manager :
                r = m.removeDragItem(parentSource, focusNode);
                this.addChild(widget, z, place);
                dojo.attr(r, "tabIndex", "0");
                dijit.focus(r);
              }
              else{
                dojo.publish("/dojox/layout/gridContainer/moveRestriction", [this]);
              }
            }
            else{
              var node = focusNode.parentNode;
              while(zone === null){
                if(node[pos] !== null && node[pos].nodeType !== 3){
                  node = node[pos];
                }
                else{
                  if(pos === "previousSibling"){
                    node = node.parentNode.childNodes[node.parentNode.childNodes.length-1];
                  }
                  else{
                    node = (dojo.isIE)? node.parentNode.childNodes[0]: node.parentNode.childNodes[1];
                  }
                }
                zone = node[child];
                if(zone && zone.style.display == "none"){
                  // check that all elements are not hidden
                  children = zone.parentNode.childNodes;
                  var childToSelect = null;
                  if(pos == "previousSibling"){
                    for(i = children.length-1; i >= 0; i--){
                      if(children[i].style.display != "none"){
                        childToSelect = children[i];
                        break;
                      }
                    }
                  }
                  else{
                    for(i = 0; i < children.length; i++){
                      if(children[i].style.display != "none"){
                        childToSelect = children[i];
                        break;
                      }
                    }
                  }
                  if(!childToSelect){
                    focusNode = zone;
                    node = focusNode.parentNode;
                    zone = null;
                  }
                  else{
                    zone = childToSelect;
                  }
                }
              }
              dijit.focus(zone);
            }
          break;
        }
      }
    }
  },

  destroy: function(){
    //console.log("dojox.layout.GridContainerLite ::: destroy");
    var m = this._dragManager;
    dojo.forEach(this._grid, function(dropZone){
      m.unregister(dropZone.node);
    });
    this.inherited(arguments);
  }
});

dojo.extend(dijit._Widget, {

  // column: String
  //    Column of the grid to place the widget.
  //    Defined only if  is done.
  column : "1",

  // dragRestriction: Boolean
  //    If true, the widget can not be draggable.
  //    Defined only if  is done.
  dragRestriction : false
});

}

if(!dojo._hasResource["dojox.layout.GridContainer"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.layout.GridContainer"] = true;
dojo.provide("dojox.layout.GridContainer");



dojo.declare(
  "dojox.layout.GridContainer",
  dojox.layout.GridContainerLite,
{
  // summary:
  //    A grid containing any kind of objects and acting like web portals.
  //
  // description:
  //    This component inherits of all features of gridContainerLite plus :
  //      - Resize colums
  //      - Add / remove columns
  //      - Fix columns at left or at right.
  // example:
  //  | <div dojoType="dojox.layout.GridContainer" nbZones="3" isAutoOrganized="true">
  //  |   <div dojoType="dijit.layout.ContentPane">Content Pane 1 : Drag Me !</div>
  //  |   <div dojoType="dijit.layout.ContentPane">Content Pane 2 : Drag Me !</div>
  //  |   <div dojoType="dijit.layout.ContentPane">Content Pane 3 : Drag Me !</div>
  //  | </div>
  //
  // example:
  //  | dojo.ready(function(){
  //  |   var cpane1 = new dijit.layout.ContentPane({ title:"cpane1", content: "Content Pane 1 : Drag Me !" }),
  //  |     cpane2 = new dijit.layout.ContentPane({ title:"cpane2", content: "Content Pane 2 : Drag Me !" }),
  //  |     cpane3 = new dijit.layout.ContentPane({ title:"cpane3", content: "Content Pane 3 : Drag Me !" });
  //  |
  //  |   var widget = new dojox.layout.GridContainer({
  //  |     nbZones: 3,
  //  |     isAutoOrganized: true
  //  |   }, dojo.byId("idNode"));
  //  |   widget.addChild(cpane1, 0, 0);
  //  |   widget.addChild(cpane2, 1, 0);
  //  |   widget.addChild(cpane3, 2, 1);
  //  |   widget.startup();
  //  | });

  // hasResizableColumns: Boolean
  //    Allow or not resizing of columns by a grip handle.
  hasResizableColumns: true,

  // liveResizeColumns: Boolean
  //    Specifies whether columns resize as you drag (true) or only upon mouseup (false)
  liveResizeColumns : false,

  // minColWidth: Integer
  //    Minimum column width in percentage.
  minColWidth: 20,

  // minChildWidth: Integer
  //    Minimum children width in pixel (only used for IE6 which doesn't handle min-width css property)
  minChildWidth: 150,

  // mode: String
  //    Location to add/remove columns, must be set to 'left' or 'right' (default).
  mode: "right",

  // isRightFixed: Boolean
  //    Define if the last right column is fixed.
  //    Used when you add or remove columns by calling setColumns method.
  isRightFixed: false,

  // isLeftFixed: Boolean
  //    Define if the last left column is fixed.
  //    Used when you add or remove columns by calling setColumns method.
  isLeftFixed: false,

  startup: function(){
    // summary:
    //    Call the startup of GridContainerLite and place grips
    //    if user has chosen the hasResizableColumns attribute to true.

    //console.log("dojox.layout.GridContainer ::: startup");
    this.inherited(arguments);
    if(this.hasResizableColumns){
      for(var i = 0; i < this._grid.length - 1; i++){
        this._createGrip(i);
      }
      // If widget has a container parent, grips will be placed
      // by method onShow.
      if(!this.getParent()){
        // Fix IE7 :
        //    The CSS property height:100% for the grip
        //    doesn't work anytime. It's necessary to wait
        //    the end of loading before to place grips.
        dojo.ready(dojo.hitch(this, "_placeGrips"));
      }
    }
  },
  
  resizeChildAfterDrop : function(/*Node*/node, /*Object*/targetArea, /*Integer*/indexChild){
    // summary:
    //    Call when a child is dropped.
    // description:
    //    Allow to resize and put grips
    // node:
    //    domNode of dropped widget.
    // targetArea:
    //    AreaManager Object containing information of targetArea
    // indexChild:
    //    Index where the dropped widget has been placed

    if(this.inherited(arguments)){
      this._placeGrips();
    }
  },

  onShow: function(){
    // summary:
    //    Place grips in the right place when the GridContainer becomes visible.

    //console.log("dojox.layout.GridContainer ::: onShow");
    this.inherited(arguments);
    this._placeGrips();
  },

  resize: function(){
    // summary:
    //    Resize the GridContainer widget and columns.
    //    Replace grips if it's necessary.
    // tags:
    //    callback

    //console.log("dojox.layout.GridContainer ::: resize");
    this.inherited(arguments);
    // Fix IE6 :
    //    IE6 calls method resize itself.
    //    If the GridContainer is not visible at this time,
    //    the method _placeGrips can return a negative value with
    //    contentBox method. (see method _placeGrip() with Fix Ie6 for the height)
    if(this._isShown() && this.hasResizableColumns){
      this._placeGrips();
    }
  },

  _createGrip: function(/*Integer*/ index){
    // summary:
    //    Create a grip for a specific zone.
    // index:
    //    index where the grip has to be created.
    // tags:
    //    protected

    //console.log("dojox.layout.GridContainer ::: _createGrip");
    var dropZone = this._grid[index],
      grip = dojo.create("div", { 'class': "gridContainerGrip" }, this.domNode);
    dropZone.grip = grip;
    dropZone.gripHandler = [
      this.connect(grip, "onmouseover", function(e){
        var gridContainerGripShow = false;
        for(var i = 0; i < this._grid.length - 1; i++){
          if(dojo.hasClass(this._grid[i].grip, "gridContainerGripShow")){
            gridContainerGripShow = true;
            break;
          }
        }
        if(!gridContainerGripShow){
          dojo.removeClass(e.target, "gridContainerGrip");
          dojo.addClass(e.target, "gridContainerGripShow");
        }
      })[0],
      this.connect(grip, "onmouseout", function(e){
        if(!this._isResized){
          dojo.removeClass(e.target, "gridContainerGripShow");
          dojo.addClass(e.target, "gridContainerGrip");
        }
      })[0],
      this.connect(grip, "onmousedown", "_resizeColumnOn")[0],
      this.connect(grip, "ondblclick", "_onGripDbClick")[0]
    ];
  },

  _placeGrips: function(){
    // summary:
    //    Define the position of a grip and place it on page.
    // tags:
    //    protected

    //console.log("dojox.layout.GridContainer ::: _placeGrips");
    var gripWidth, height, left = 0, grip;
    var scroll = this.domNode.style.overflowY;

    dojo.forEach(this._grid, function(dropZone){
      if(dropZone.grip){
        grip = dropZone.grip;
        if(!gripWidth){
          gripWidth = grip.offsetWidth / 2;
        }

        left += dojo.marginBox(dropZone.node).w;

        dojo.style(grip, "left", (left - gripWidth) + "px");
        //if(dojo.isIE == 6){ do it fot all navigators
        if(!height){
          height = dojo.contentBox(this.gridNode).h;
        }
        if(height > 0){
          dojo.style(grip, "height", height + "px");
        }
        //}
      }
    }, this);
  },

  _onGripDbClick: function(){
    // summary:
    //    Called when a double click is catch. Resize all columns with the same width.
    //    The method resize of children have to be called.
    // tags:
    //    callback protected

    //console.log("dojox.layout.GridContainer ::: _onGripDbClick");
    this._updateColumnsWidth(this._dragManager);
    this.resize();
  },

  _resizeColumnOn: function(/*Event*/e){
    // summary:
    //    Connect events to listen the resize action.
    //    Change the type of width columns (% to px).
    //    Calculate the minwidth according to the children.
    // tags:
    //    callback

    //console.log("dojox.layout.GridContainer ::: _resizeColumnOn", e);
    this._activeGrip = e.target;
    this._initX = e.pageX;
    e.preventDefault();

    dojo.body().style.cursor = "ew-resize";

    this._isResized = true;

    var tabSize = [];
    var grid;
    var i;

    for(i = 0; i < this._grid.length; i++){
      tabSize[i] = dojo.contentBox(this._grid[i].node).w;
    }

    this._oldTabSize = tabSize;

    for(i = 0; i < this._grid.length; i++){
      grid = this._grid[i];
      if(this._activeGrip == grid.grip){
        this._currentColumn = grid.node;
        this._currentColumnWidth = tabSize[i];
        this._nextColumn = this._grid[i + 1].node;
        this._nextColumnWidth = tabSize[i + 1];
      }
      grid.node.style.width = tabSize[i] + "px";
    }

    // calculate the minWidh of all children for current and next column
    var calculateChildMinWidth = function(childNodes, minChild){
      var width = 0;
      var childMinWidth = 0;

      dojo.forEach(childNodes, function(child){
        if(child.nodeType == 1){
          var objectStyle = dojo.getComputedStyle(child);
          var minWidth = (dojo.isIE) ? minChild : parseInt(objectStyle.minWidth);

          childMinWidth = minWidth +
                parseInt(objectStyle.marginLeft) +
                parseInt(objectStyle.marginRight);

          if(width < childMinWidth){
            width = childMinWidth;
          }
        }
      });
      return width;
    }
    var currentColumnMinWidth = calculateChildMinWidth(this._currentColumn.childNodes, this.minChildWidth);

    var nextColumnMinWidth = calculateChildMinWidth(this._nextColumn.childNodes, this.minChildWidth);

    var minPix = Math.round((dojo.marginBox(this.gridContainerTable).w * this.minColWidth) / 100);

    this._currentMinCol = currentColumnMinWidth;
    this._nextMinCol = nextColumnMinWidth;

    if(minPix > this._currentMinCol){
      this._currentMinCol = minPix;
    }
    if(minPix > this._nextMinCol){
      this._nextMinCol = minPix;
    }
    this._connectResizeColumnMove = dojo.connect(dojo.doc, "onmousemove", this, "_resizeColumnMove");
    this._connectOnGripMouseUp = dojo.connect(dojo.doc, "onmouseup", this, "_onGripMouseUp");
  },

  _onGripMouseUp: function(){
    // summary:
    //    Call on the onMouseUp only if the reiszeColumnMove was not called.
    // tags:
    //    callback

    //console.log("dojox.layout.GridContainer ::: _onGripMouseUp");
    dojo.body().style.cursor = "default";

    dojo.disconnect(this._connectResizeColumnMove);
    dojo.disconnect(this._connectOnGripMouseUp);

    this._connectOnGripMouseUp = this._connectResizeColumnMove = null;

    if(this._activeGrip){
      dojo.removeClass(this._activeGrip, "gridContainerGripShow");
      dojo.addClass(this._activeGrip, "gridContainerGrip");
    }

    this._isResized = false;
  },

  _resizeColumnMove: function(/*Event*/e){
    // summary:
    //    Change columns size.
    // tags:
    //    callback

    //console.log("dojox.layout.GridContainer ::: _resizeColumnMove");
    e.preventDefault();
    if(!this._connectResizeColumnOff){
      dojo.disconnect(this._connectOnGripMouseUp);
      this._connectOnGripMouseUp = null;
      this._connectResizeColumnOff = dojo.connect(dojo.doc, "onmouseup", this, "_resizeColumnOff");
    }

    var d = e.pageX - this._initX;
    if(d == 0){ return; }

    if(!(this._currentColumnWidth + d < this._currentMinCol || 
        this._nextColumnWidth - d < this._nextMinCol)){

      this._currentColumnWidth += d;
      this._nextColumnWidth -= d;
      this._initX = e.pageX;
      this._activeGrip.style.left = parseInt(this._activeGrip.style.left) + d + "px";

      if(this.liveResizeColumns){
        this._currentColumn.style["width"] = this._currentColumnWidth + "px";
        this._nextColumn.style["width"] = this._nextColumnWidth + "px";
        this.resize();
      }
    }
  },

  _resizeColumnOff: function(/*Event*/e){
    // summary:
    //    Disconnect resize events.
    //    Change the type of width columns (px to %).
    // tags:
    //    callback

    //console.log("dojox.layout.GridContainer ::: _resizeColumnOff");
    dojo.body().style.cursor = "default";

    dojo.disconnect(this._connectResizeColumnMove);
    dojo.disconnect(this._connectResizeColumnOff);

    this._connectResizeColumnOff = this._connectResizeColumnMove = null;

    if(!this.liveResizeColumns){
      this._currentColumn.style["width"] = this._currentColumnWidth + "px";
      this._nextColumn.style["width"] = this._nextColumnWidth + "px";
      //this.resize();
    }

    var tabSize = [],
      testSize = [],
      tabWidth = this.gridContainerTable.clientWidth,
      node,
      update = false,
      i;

    for(i = 0; i < this._grid.length; i++){
      node = this._grid[i].node;
      if(dojo.isIE){
        tabSize[i] = dojo.marginBox(node).w;
        testSize[i] = dojo.contentBox(node).w;
      }
      else{
        tabSize[i] = dojo.contentBox(node).w;
        testSize = tabSize;
      }
    }

    for(i = 0; i < testSize.length; i++){
      if(testSize[i] != this._oldTabSize[i]){
        update = true;
        break;
      }
    }

    if(update){
      var mul = dojo.isIE ? 100 : 10000;
      for(i = 0; i < this._grid.length; i++){
        this._grid[i].node.style.width = Math.round((100 * mul * tabSize[i]) / tabWidth) / mul + "%";
      }
      this.resize();
    }

    if(this._activeGrip){
      dojo.removeClass(this._activeGrip, "gridContainerGripShow");
      dojo.addClass(this._activeGrip, "gridContainerGrip");
    }

    this._isResized = false;
  },

  setColumns: function(/*Integer*/nbColumns){
    // summary:
    //    Set the number of columns.
    // nbColumns:
    //    Number of columns

    //console.log("dojox.layout.GridContainer ::: setColumns");
    var z, j;
    if(nbColumns > 0){
      var length = this._grid.length,
        delta = length - nbColumns;
      if(delta > 0){
        var count = [], zone, start, end, nbChildren;
        // Check if right or left columns are fixed
        // Columns are not taken in account and can't be deleted
        if(this.mode == "right"){
          end = (this.isLeftFixed && length > 0) ? 1 : 0;
          start = (this.isRightFixed) ? length - 2 : length - 1
          for(z = start; z >= end; z--){
            nbChildren = 0;
            zone = this._grid[z].node;
            for(j = 0; j < zone.childNodes.length; j++){
              if(zone.childNodes[j].nodeType == 1 && !(zone.childNodes[j].id == "")){
                nbChildren++;
                break;
              }
            }
            if(nbChildren == 0){ count[count.length] = z; }
            if(count.length >= delta){
              this._deleteColumn(count);
              break;
            }
          }
          if(count.length < delta){
            dojo.publish("/dojox/layout/gridContainer/noEmptyColumn", [this]);
          }
        }
        else{ // mode = "left"
          start = (this.isLeftFixed && length > 0) ? 1 : 0;
          end = (this.isRightFixed) ? length - 1 : length;
          for(z = start; z < end; z++){
            nbChildren = 0;
            zone = this._grid[z].node;
            for(j = 0; j < zone.childNodes.length; j++){
              if(zone.childNodes[j].nodeType == 1 && !(zone.childNodes[j].id == "")){
                nbChildren++;
                break;
              }
            }
            if(nbChildren == 0){ count[count.length] = z; }
            if(count.length >= delta){
              this._deleteColumn(count);
              break;
            }
          }
          if(count.length < delta){
            //Not enough empty columns
            dojo.publish("/dojox/layout/gridContainer/noEmptyColumn", [this]);
          }
        }
      }
      else{
        if(delta < 0){ this._addColumn(Math.abs(delta)); }
      }
      if(this.hasResizableColumns){ this._placeGrips(); }
    }
  },

  _addColumn: function(/*Integer*/nbColumns){
    // summary:
    //    Add some columns.
    // nbColumns:
    //    Number of column to added
    // tags:
    //    private

    //console.log("dojox.layout.GridContainer ::: _addColumn");
    var grid = this._grid,
      dropZone,
      node,
      index,
      length,
      isRightMode = (this.mode == "right"),
      accept = this.acceptTypes.join(","),
      m = this._dragManager;

    //Add a grip to the last column
    if(this.hasResizableColumns && ((!this.isRightFixed && isRightMode)
      || (this.isLeftFixed && !isRightMode && this.nbZones == 1) )){
      this._createGrip(grid.length - 1);
    }

    for(var i = 0; i < nbColumns; i++){
      // Fix CODEX defect #53025 :
      //    Apply acceptType attribute on each new column.
      node = dojo.create("td", {
        'class': "gridContainerZone dojoxDndArea" ,
        'accept': accept,
        'id': this.id + "_dz" + this.nbZones
      });

      length = grid.length;

      if(isRightMode){
        if(this.isRightFixed){
          index = length - 1;
          grid.splice(index, 0, {
            'node': grid[index].node.parentNode.insertBefore(node, grid[index].node) 
          });
        }
        else{
          index = length;
          grid.push({ 'node': this.gridNode.appendChild(node) });
        }
      }
      else{
        if(this.isLeftFixed){
          index = (length == 1) ? 0 : 1;
          this._grid.splice(1, 0, {
            'node': this._grid[index].node.parentNode.appendChild(node, this._grid[index].node)
          });
          index = 1;
        }
        else{
          index = length - this.nbZones;
          this._grid.splice(index, 0, {
            'node': grid[index].node.parentNode.insertBefore(node, grid[index].node)
          });
        }
      }
      if(this.hasResizableColumns){
        //Add a grip to resize columns
        if((!isRightMode && this.nbZones != 1) ||
            (!isRightMode && this.nbZones == 1 && !this.isLeftFixed) ||
              (isRightMode && i < nbColumns-1) ||
                (isRightMode && i == nbColumns-1 && this.isRightFixed)){
          this._createGrip(index);
        }
      }
      // register tnbZoneshe new area into the areaManager
      m.registerByNode(grid[index].node);
      this.nbZones++;
    }
    this._updateColumnsWidth(m);
  },

  _deleteColumn: function(/*Array*/indices){
    // summary:
    //    Remove some columns with indices passed as an array.
    // indices:
    //    Column index array
    // tags:
    //    private

    //console.log("dojox.layout.GridContainer ::: _deleteColumn");
    var child, grid, index,
      nbDelZones = 0,
      length = indices.length,
      m = this._dragManager;
    for(var i = 0; i < length; i++){
      index = (this.mode == "right") ? indices[i] : indices[i] - nbDelZones;
      grid = this._grid[index];

      if(this.hasResizableColumns && grid.grip){
        dojo.forEach(grid.gripHandler, function(handler){
          dojo.disconnect(handler);
        });
        dojo.destroy(this.domNode.removeChild(grid.grip));
        grid.grip = null;
      }

      m.unregister(grid.node);
      dojo.destroy(this.gridNode.removeChild(grid.node));
      this._grid.splice(index, 1);
      this.nbZones--;
      nbDelZones++;
    }

    // last grip
    var lastGrid = this._grid[this.nbZones-1];
    if(lastGrid.grip){
      dojo.forEach(lastGrid.gripHandler, dojo.disconnect);
      dojo.destroy(this.domNode.removeChild(lastGrid.grip));
      lastGrid.grip = null;
    }

    this._updateColumnsWidth(m);
  },

  _updateColumnsWidth: function(/*Object*/ manager){
    // summary:
    //    Update the columns width.
    // manager:
    //    dojox.mdnd.AreaManager singleton
    // tags:
    //    private

    //console.log("dojox.layout.GridContainer ::: _updateColumnsWidth");
    this.inherited(arguments);
    manager._dropMode.updateAreas(manager._areaList);
  },

  destroy: function(){
    dojo.unsubscribe(this._dropHandler);
    this.inherited(arguments);
  }
});

}

if(!dojo._hasResource["dojox.widget.Portlet"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.widget.Portlet"] = true;
dojo.experimental("dojox.widget.Portlet");
// [BD] dojo.require("dijit.TitlePane");
require(["dijit/TitlePane"]);
dojo.provide("dojox.widget.Portlet");



dojo.declare("dojox.widget.Portlet", [dijit.TitlePane, dijit._Container],{
  // summary: A container widget that is designed to be contained
  //    in a dojox.layout.GridContainer. Child widgets can insert
  //    an icon into the title bar of the Portlet, which when
  //    clicked, executes the "toggle" method of the child widget.
  //    A child widget must specify the attribute 
  //    "portletIconClass", and the optional class
  //    "portletIconHoverClass", as well as the 
  //    "toggle" function.

  // resizeChildren: Boolean
  //    If true, when the Portlet is resized, any child widgets
  //    with a 'resize' method have that method called.
  resizeChildren: true,

  // closable: Boolean
  //    If true, a close button is placed in the title bar,
  //    and the Portlet can be hidden. If false, the Portlet
  //    cannot be closed.
  closable: true,

  // _parents: Array
  //     An array of all the StackContainer widgets that this Portlet
  //    is contained in.  These are used to determine if the portlet
  //    is visible or not.
  _parents: null,

  // _size: Object
  //    Cache of the previous size of the portlet, used to determine
  //    if the size has changed and if the child widgets should be
  //    resized.
  _size: null,

  // dragRestriction: Boolean
  //    To remove the drag capability.
  dragRestriction : false,
  
  dndType: "Portlet",

  // CR00224614, added this function for performance gains. Manipulating the 
  // classes before inserting the Node is faster.
  buildRendering: function(){
    this.inherited(arguments);

    // Hide the portlet until it is fully constructed.
    dojo.style(this.domNode, "visibility", "hidden");
    // Add the portlet classes
   //Bdojo.addClass(this.domNode, "dojoxPortlet");
    dojo.removeClass(this.arrowNode, "dijitArrowNode");
   //Bdojo.addClass(this.arrowNode, "dojoxPortletIcon dojoxArrowDown");
   //Bdojo.addClass(this.titleBarNode, "dojoxPortletTitle");
   //Bdojo.addClass(this.hideNode, "dojoxPortletContentOuter");    
    // Choose the class to add depending on if the portlet is draggable or not.
   //Bdojo.addClass(this.domNode, "dojoxPortlet-" + (!this.dragRestriction ? "movable" : "nonmovable"));
    
  },

  postCreate: function(){
    this.inherited(arguments);

    // CR00224614, moved this code into buildRendering for performance gains.
    // Add the portlet classes
    //dojo.addClass(this.domNode, "dojoxPortlet");
    //dojo.removeClass(this.arrowNode, "dijitArrowNode");
    //dojo.addClass(this.arrowNode, "dojoxPortletIcon dojoxArrowDown");
    //dojo.addClass(this.titleBarNode, "dojoxPortletTitle");
    //dojo.addClass(this.hideNode, "dojoxPortletContentOuter");

    // Choose the class to add depending on if the portlet is draggable or not.
    //dojo.addClass(this.domNode, "dojoxPortlet-" + (!this.dragRestriction ? "movable" : "nonmovable"));

    var _this = this;
    if(this.resizeChildren){
      // If children should be resized  when the portlet size changes,
      // listen for items being dropped, when the window is resized,
      // or when another portlet's size changes.

      this.subscribe("/dnd/drop", function(){_this._updateSize();});

      this.subscribe("/Portlet/sizechange", function(widget){_this.onSizeChange(widget);});
      this.connect(window, "onresize", function(){_this._updateSize();});

      // Subscribe to all possible child-selection events that could affect this
      // portlet
      var doSelectSubscribe = dojo.hitch(this, function(id, lastId){
        var widget = dijit.byId(id);
        if(widget.selectChild){
          var s = this.subscribe(id + "-selectChild", function(child){
            var n = _this.domNode.parentNode;

            while(n){
              if(n == child.domNode){
                
                // Only fire this once, as the widget is now visible
                // at least once, so child measurements should be accurate.
                _this.unsubscribe(s);
                _this._updateSize();
                break;
              }
              n = n.parentNode;
            }
          });

          // Record the StackContainer and child widget that this portlet
          // is in, so it can figure out whether or not it is visible.
          // If it is not visible, it will not update it's size dynamically.
          var child = dijit.byId(lastId);
          if(widget && child){
            _this._parents.push({parent: widget, child: child});
          }
        }
      });
      var lastId;
      this._parents = [];

      // Find all parent widgets, and if they are StackContainers,
      // subscribe to their selectChild method calls.
      for(var p = this.domNode.parentNode; p != null; p = p.parentNode){
        var id = p.getAttribute ? p.getAttribute("widgetId") : null;
        if(id){
          doSelectSubscribe(id, lastId);
          lastId = id;
        }
      }
    }
    
    // Prevent clicks on icons from causing a drag to start.
    this.connect(this.titleBarNode, "onmousedown", function(evt){
      if (dojo.hasClass(evt.target, "dojoxPortletIcon")) {
        dojo.stopEvent(evt);
        return false;
      }
      return true;
    });

    // Inform all portlets that the size of this one has changed,
    // and therefore perhaps they have too
    this.connect(this._wipeOut, "onEnd", function(){_this._publish();});
    this.connect(this._wipeIn, "onEnd", function(){_this._publish();});

    if(this.closable){
      this.closeIcon = this._createIcon("dojoxCloseNode", "dojoxCloseNodeHover", dojo.hitch(this, "onClose"));
      dojo.style(this.closeIcon, "display", "");
    }
  },

  startup: function(){
    if(this._started){return;}

    var children = this.getChildren();
    this._placeSettingsWidgets();

    // Start up the children
    dojo.forEach(children, function(child){
      try{
        if(!child.started && !child._started){
          child.startup()
        }
      } 
      catch(e){
        console.log(this.id + ":" + this.declaredClass, e);
      }
    });

    this.inherited(arguments);

    //this._updateSize();
    dojo.style(this.domNode, "visibility", "visible");
  },

  _placeSettingsWidgets: function(){
    // summary: Checks all the children to see if they are instances
    //    of dojox.widget.PortletSettings.  If they are, 
    //    create an icon for them in the title bar which when clicked,
    //    calls their toggle() method.

    dojo.forEach(this.getChildren(), dojo.hitch(this, function(child){
      if(child.portletIconClass && child.toggle && !child.attr("portlet")){
        this._createIcon(child.portletIconClass, child.portletIconHoverClass, dojo.hitch(child, "toggle"));
        dojo.place(child.domNode, this.containerNode, "before");
        child.attr("portlet", this);
      }
    }));
  },

  _createIcon: function(clazz, hoverClazz, fn){
    // summary: 
    //    creates an icon in the title bar.

    var icon = dojo.create("div",{
      "class": "dojoxPortletIcon " + clazz,
      "waiRole": "presentation"
    });
    dojo.place(icon, this.arrowNode, "before");

    this.connect(icon, "onclick", fn);

    if(hoverClazz){
      this.connect(icon, "onmouseover", function(){
       //Bdojo.addClass(icon, hoverClazz);
      });
      this.connect(icon, "onmouseout", function(){
        dojo.removeClass(icon, hoverClazz);
      });
    }
    return icon;
  },

  onClose: function(evt){
    // summary: 
    //    Hides the portlet. Note that it does not
    //    persist this, so it is up to the client to
    //    listen to this method and persist the closed state
    //    in their own way.
    dojo.style(this.domNode, "display", "none");
    dojo.attr(this.domNode, "closed", "true")
    dojo.publish("/dojox/mdnd/close",[this]);
  },

  onSizeChange: function(widget){
    // summary:
    //    Updates the Portlet size if any other Portlet
    //    changes its size.
    if(widget == this){
      return;
    }
    this._updateSize();
  },

  _updateSize: function(){
    // summary: 
    //    Updates the size of all child widgets.
    if(!this.open || !this._started || !this.resizeChildren){
      return;
    }
    
    if(this._timer){
      clearTimeout(this._timer);
    }
    // Delay applying the size change in case the size 
    // changes very frequently, for performance reasons.
    this._timer = setTimeout(dojo.hitch(this, function(){
      var size ={
        w: dojo.style(this.domNode, "width"),
        h: dojo.style(this.domNode, "height")
      };
  
      // If the Portlet is in a StackWidget, and it is not
      // visible, do not update the size, as it could
      // make child widgets miscalculate.
      for(var i = 0; i < this._parents.length; i++){
        var p = this._parents[i];
        var sel = p.parent.selectedChildWidget
        if(sel && sel != p.child){
          return;
        }
      }
  
      if(this._size){
        // If the size of the portlet hasn't changed, don't
        // resize the children, as this can be expensive
        if(this._size.w == size.w && this._size.h == size.h){
          return;
        }
      }
      this._size = size;
  
      

      var fns = ["resize", "layout"];
      this._timer = null;
      var kids = this.getChildren();

      dojo.forEach(kids, function(child){
        for(var i = 0; i < fns.length; i++){
          if(dojo.isFunction(child[fns[i]])){
            try{
              child[fns[i]]();
            } catch(e){
              console.log(e);
            }
            break;
          } 
        }
      }); 
      this.onUpdateSize();
    }), 100);
  },

  onUpdateSize: function(){
    // summary:
    //    Stub function called when the size is changed.
  },

  _publish: function(){
    // summary: Publishes an event that all other portlets listen to.
    //    This causes them to update their child widgets if their
    //    size has changed.
    dojo.publish("/Portlet/sizechange",[this]);
  },

  _onTitleClick: function(evt){
    if(evt.target == this.arrowNode){
      this.inherited(arguments);
    }
  },

  addChild: function(child){
    // summary: 
    //    Adds a child widget to the portlet.
    this._size = null;
    this.inherited(arguments);
    
    if(this._started){
      this._placeSettingsWidgets();
      this._updateSize();
    }
    if(this._started && !child.started && !child._started){
      child.startup();
    }
  },
  
  _setCss: function(){
    this.inherited(arguments);
    dojo.style(this.arrowNode, "display", this.toggleable ? "":"none");
  }
});

dojo.declare("dojox.widget.PortletSettings", [dijit._Container, dijit.layout.ContentPane],{
  // summary: 
  //    A settings widget to be used with a dojox.widget.Portlet.
  // description: 
  //    This widget should be placed inside a dojox.widget.Portlet widget.
  //    It is used to set some preferences for that Portlet.  It is essentially
  //    a ContentPane, and should contain other widgets and DOM nodes that
  //    do the real work of setting preferences for the portlet.

  // portletIconClass: String
  //    The CSS class to apply to the icon in the Portlet title bar that is used
  //    to toggle the visibility of this widget.
  portletIconClass: "dojoxPortletSettingsIcon",

  // portletIconHoverClass: String
  //    The CSS class to apply to the icon in the Portlet title bar that is used
  //    to toggle the visibility of this widget when the mouse hovers over it.
  portletIconHoverClass: "dojoxPortletSettingsIconHover",
  
  // CR00224614, added this function for performance gains. Manipulating the 
  // classes before inserting the Node is faster.
  buildRendering:function(){
    this.inherited(arguments);

    // Start the PortletSettings widget hidden, always.
    dojo.style(this.domNode, "display", "none");
   //Bdojo.addClass(this.domNode, "dojoxPortletSettingsContainer");

    // Remove the unwanted content pane class.
    dojo.removeClass(this.domNode, "dijitContentPane");
  },

  postCreate: function(){
    // summary:
    //    Sets the require CSS classes on the widget.

    // CR00224614, moved this code into buildRendering for performance gains.
    // Start the PortletSettings widget hidden, always.
    //dojo.style(this.domNode, "display", "none");
    //dojo.addClass(this.domNode, "dojoxPortletSettingsContainer");

    // Remove the unwanted content pane class.
    //dojo.removeClass(this.domNode, "dijitContentPane");
  },

  _setPortletAttr: function(portlet){
    // summary: 
    //    Sets the portlet that encloses this widget.
    this.portlet = portlet;
  },

  toggle: function(){
    // summary: 
    //    Toggles the visibility of this widget.
    var n = this.domNode;
    if(dojo.style(n, "display") == "none"){
      dojo.style(n,{
        "display": "block",
        "height": "1px",
        "width": "auto"
      });
      dojo.fx.wipeIn({
        node: n
      }).play();
    }else{
      dojo.fx.wipeOut({
        node: n,
        onEnd: dojo.hitch(this, function(){
          dojo.style(n,{"display": "none", "height": "", "width":""});
        }
      )}).play();
    }
  }
});

dojo.declare("dojox.widget.PortletDialogSettings", 
  dojox.widget.PortletSettings,{
  // summary: 
  //    A settings widget to be used with a dojox.widget.Portlet, which displays
  //    the contents of this widget in a dijit.Dialog box.

  // dimensions: Array
  //    The size of the dialog to display.  This defaults to [300, 300]
  dimensions: null,

  constructor: function(props, node){
    this.dimensions = props.dimensions || [300, 100];
  },

  toggle: function(){
    // summary: 
    //    Shows and hides the Dialog box.
    if(!this.dialog){
      // [BD] dojo["require"]("dijit.Dialog");
      require(["dijit/Dialog"]);
      this.dialog = new dijit.Dialog({title: this.title});
      
      dojo.body().appendChild(this.dialog.domNode);

      // Move this widget inside the dialog
      this.dialog.containerNode.appendChild(this.domNode);

      dojo.style(this.dialog.domNode,{
        "width" : this.dimensions[0] + "px",
        "height" : this.dimensions[1] + "px"
      });
      dojo.style(this.domNode, "display", "");
    }
    if(this.dialog.open){
      this.dialog.hide();
    }else{
      this.dialog.show(this.domNode);
    }
  }
});
}

//dojo.i18n._preloadLocalizations("dojox.nls.GridContainerLayer", ["ROOT","ar","ca","cs","da","de","de-de","el","en","en-gb","en-us","es","es-es","fi","fi-fi","fr","fr-fr","he","he-il","hu","it","it-it","ja","ja-jp","ko","ko-kr","nb","nl","nl-nl","pl","pt","pt-br","pt-pt","ru","sk","sl","sv","th","tr","xx","zh","zh-cn","zh-tw"]);
