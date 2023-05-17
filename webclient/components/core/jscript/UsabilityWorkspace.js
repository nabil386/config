/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
/*
 * This file defines the drag and drop capabilities for the Usability Workspace.
 * @deprecated, replaced by components\CEFWidgets\jscript\UsabilityWorkspace.js
 * @deprecated-since v6.0
 * 
 */
var uw = {
  columns: [],
  podHeights:{},
  // Initialise the columns on the page, setting up Drag and Drop
  init: function(){
    var table = dojo.byId("pod-container-table");
    if(!table){
      return;
    }
    var row = table.rows[0];
    var col, pod, podList, podNode, handle;
    if(!row){return;}

    var dnd = {};
    // Iterate over the first and only row. Each TD is a column.
    for(var i = 0; i < row.cells.length; i++) {
      col = row.cells[i];
      // Set the TD element as a drop target.
      new uw.dnd.HtmlDropTarget(col, ["pod-col"])

      for(var j = 0; j < col.childNodes.length; j++) {
        podNode = col.childNodes[0];
        if(!dojo.html.hasClass(podNode, "pod-wrapper")){
          continue;
        }
        // Iterate through the DIV elements in the TD, each of which is a pod.
        // For each pod, find the header, which is the drag handle.
        for(var k = 0; k < podNode.childNodes.length; k++) {
          if(dojo.html.hasClass(podNode.childNodes[k], "pod-handle")) {
            handle = new uw.dnd.HtmlDragSource(podNode.childNodes[k], "pod-col");
            handle.setDragTarget(podNode);
            break;
          }
        }
      }
    }
    // When a pod is dropped into a column, record the positions of the pods in that
    // column in a hidden input.
    dojo.event.topic.subscribe("podDrop", function(info){
      var row = dojo.byId(info.parent).parentNode;
      // Update the list of pods for all columns on the page.
      for(var cellIndex = 0; cellIndex < row.cells.length; cellIndex++) {
        var col = row.cells[cellIndex];
        var colInputId = uw.columns[col.cellIndex];
        var colInput = dojo.byId(colInputId);
        if(!colInput) {
         return;
        }
        var podIds = [], id;
        // Build the string which is a comma separated list of pod ids in a single column
        for(var i = 0; i < col.childNodes.length; i++) {
          id = col.childNodes[i].getAttribute ? col.childNodes[i].getAttribute("id") : null;
          if(id && id.indexOf("_marker") > -1) {
            dojo.html.removeNode(col.childNodes[i]);
            i--;
            continue;
          }
          if(id) {
            podIds.push(id);
          }
        }
        colInput.value = podIds.join(",");
      }
    });
  },

  // Called by a renderer to specify the ID the hidden input of a TD element
  registerColumn: function(idx, id) {
    uw.columns[Number(idx)] = id;
  },
  
  toggleOpen: function(node, evt) {
    var divBody = dojo.dom.nextElement(node, "div");
    if(divBody._inAnim){return;}
    divBody._inAnim = true;
    var isOpen = dojo.html.hasClass(node, "accordion-open");
    var callback = function(){
    	divBody._inAnim = false;
    	var o = "accordion-open", c = "accordion-closed"; 
    	dojo.html.replaceClass(node, isOpen ? c : o, isOpen ? o : c);
   	};

    dojo.lfx[isOpen ? "wipeOut" : "wipeIn"](divBody, 300, null, callback).play();
  }
};

require(["dojo/dnd/HtmlDragCopy"]);

dojo.declare("uw.dnd.HtmlDragSource", dojo.dnd.HtmlDragSource,
{
  _onDragStart: dojo.dnd.HtmlDragSource.prototype.onDragStart,
  onDragStart: function() {
    var obj = this._onDragStart();
    var _this = this;
    // Override the default createDragNode function.
    obj.createDragNode = function() {
      var parent = dojo.byId(this.domNode.parentNode);
      var mb = dojo.html.getMarginBox(parent);
      this.marker = document.createElement("div");
      
      // Create a marker to remember the original position of the DIV being dragged.
      this.marker.setAttribute("id", this.domNode.id + "_marker");
      dojo.dom.insertAfter(this.marker, this.domNode);
      var node = this.domNode;
      var wrapper = document.createElement("div");
      dojo.html.setOpacity(wrapper, 0.5);
      dojo.html.setStyle(wrapper, "width", mb.width + "px");
      dojo.html.setStyle(wrapper, "height", mb.height + "px");

      // Compensate for the margin at the bottom of the pod, usually 20px
      var marginBottom = dojo.html.getStyle(node, "margin-bottom");
      marginBottom = Number(marginBottom.split("px")[0]);

      // Store the correct height of the pod before it is hidden.
      uw.podHeights[this.domNode.id] = dojo.html.getMarginBox(this.domNode).height - marginBottom;
     // dojo.html.hide(this.domNode);
      wrapper.appendChild(node);
      return wrapper;
    };
    //Override the default onDragEnd function
    obj.onDragEnd = function(e) {
      switch(e.dragStatus){
        case "dropSuccess":
          dojo.html.removeNode(this.dragClone);
          this.dragClone = null;
          dojo.html.removeNode(this.marker);
          break;

        case "dropFailure": // slide back to the start.
          var startCoords = dojo.html.getAbsolutePosition(this.dragClone, true);
          var endCoords = { left: this.dragStartPosition.x + 1,
            top: this.dragStartPosition.y + 1};
            
          // Slide the pod back into position.
          var anim = dojo.lfx.slideTo(this.dragClone, endCoords, 300);
          var dragObject = this;
          
          dojo.event.connect(anim, "onEnd", function(e){
            //Place the pod back in its original DOM position
            var wrapper = dragObject.domNode.parentNode;
            dojo.dom.insertBefore(dragObject.domNode, dragObject.marker);
            dragObject.dragClone = null;
            dojo.html.removeNode(this.marker);
            dojo.html.removeNode(wrapper);
          });
          anim.play();
          break;
      }
      dojo.event.topic.publish('dragEnd', { source: this } );
    };
    return obj;
  },
  onDragEnd: function(evt){
    if(evt.dragStatus != "dropSuccess") {
      setTimeout(function(){
        dojo.html.show(evt.dragSource.dragObject);
      }, 320);
    } else {
      dojo.html.show(evt.dragSource.dragObject);
    }
  }
});


dojo.declare("uw.dnd.HtmlDropTarget", dojo.dnd.HtmlDropTarget,
{
  createDropIndicator: function(dragObject){
    // This value is set in the onDragStart function of the HtmlDragSource
    var height = uw.podHeights[dragObject.domNode.id];

    this.dropIndicator = document.createElement("div");

    dojo.html.setStyle(this.dropIndicator, "height", height + "px");
    dojo.html.setStyle(this.dropIndicator, "width", "100%");

    dojo.html.addClass(this.dropIndicator, "dnd-indicator ");
    var n = dragObject.dragSource.domNode;

    // Ensure that the entire pod is hidden, not just the H2 element
    while(n && !dojo.html.hasClass(n, "pod-wrapper")) {
      n = n.parentNode
    }
    dojo.html.show(this.dropIndicator);

    if(!this._endSubscribed) {
      var _this = this;
      var handle = dojo.event.topic.subscribe('dragEnd', function(e){
        setTimeout(function(){
          _this._clean();
        }, 320);
      });
      this._endSubscribed = true;
    }
  },

  _clean: function(){
    if(this.dropIndicator) {
      dojo.html.removeNode(this.dropIndicator);
      delete this.dropIndicator;
    }
  },
  onDragOut: function(e){
    this._clean();
  },
  onDrop: function(e){
    var _this = this;

    var ret;
    var i = this._getNodeUnderMouse(e);

    var gravity = this.vertical ? dojo.html.gravity.WEST : dojo.html.gravity.NORTH;

    if(this.dropIndicator) {
      var clone = e.dragObject.dragClone;
      
      // Clone just the wrapper, not a deep clone, so no children cloned.
      clone = clone.cloneNode(false);
      dojo.body().appendChild(clone);

      var n = e.dragObject.domNode;
      // Wrap the pod in a div with the right dimensions, for the animation of it
      // dropping into place.
      clone.appendChild(n);
      dojo.html.show(n);
      var abs = dojo.html.getAbsolutePosition(this.dropIndicator);

      var anim = dojo.lfx.slideTo(clone, { left: abs.x, top: abs.y}, 300, null, function(){
        // Take the pod out of the clone wrapping it
        dojo.dom.replaceNode(_this.dropIndicator, e.dragObject.domNode);
        _this._clean();

        // Delete the wrapping node.
        dojo.html.removeNode(clone);

        var col = e.dragObject.domNode.parentNode;
        var count = 0;
        while(col.childNodes[count] != e.dragObject.domNode) {
          count++;
        }
        // Publish the event that the pod has been dropped
        dojo.event.topic.publish("podDrop", {
          id: e.dragObject.domNode.id,
          parent: col.id,
          position: count
        });
      });
      // Fade out the place holder
      var fadeAnim = dojo.lfx.fadeOut(this.dropIndicator, 300);

      anim.play();
      fadeAnim.play();
      ret = true;
    } else {
      ret = this.insert(e, this.domNode, "append");
      this._clean();
    }

    return ret;
  },
  _onDragMove: dojo.dnd.HtmlDropTarget.prototype.onDragMove,
  onDragMove: function(e, dragObjects){
    if(!this.dropIndicator){
      this.createDropIndicator(dragObjects[0]);
    }

    var i = e.type == "mousemove" ? this._getNodeUnderMouse(e) : -1;
    var before;
    var gravity = this.vertical ? dojo.html.gravity.WEST : dojo.html.gravity.NORTH;
    if(i < 0){
      if(this.childBoxes.length){
        before = (dojo.html.gravity(this.childBoxes[0].node, e) & gravity);
      }else{
        before = true;
      }
    }else{
      var child = this.childBoxes[i];
      var gravVal = dojo.html.gravity(child.node, e);
      before = (gravVal & gravity);
    }

    this.placeIndicator(e, dragObjects, i, before);

    if(!dojo.html.hasParent(this.dropIndicator)) {
      dojo.body().appendChild(this.dropIndicator);
    }
  },

  placeIndicator: function(e, dragObjects, boxIndex, before) {
    this.dropIndicator.style.display="";
    var targetProperty = this.vertical ? "left" : "top";
    var child;
    if(boxIndex < 0){
      if(this.childBoxes.length){
        child = before ? this.childBoxes[0] : this.childBoxes[this.childBoxes.length - 1];
      } else{
        this.dropIndicator.style[targetProperty] = dojo.html.getAbsolutePosition(this.domNode, true)[this.vertical?"x":"y"] + "px";
      }
    } else if(boxIndex < this.childBoxes.length){
      child = this.childBoxes[boxIndex];
    }
    if(child){
      dojo.dom[before ? "insertBefore" : "insertAfter"](this.dropIndicator, child.node);
      if(child.width){this.dropIndicator.style.width = child.width + "px";}
    } else {
      this.domNode.appendChild(this.dropIndicator);
    }
  }
}, function(node, types){
  if(arguments.length == 0){ return; }
  var _this = this;
  dojo.event.topic.subscribe('dragStart', function(e){
    if(e.source.domNode.parentNode == _this.domNode) {
      _this.createDropIndicator({dragSource: e.source, domNode: e.source.domNode});
      _this.insert({dragObject: {domNode: _this.dropIndicator}}, e.source.domNode, "after");
    }
  });

  dojo.event.connect(this, "onDragOver", function(){
    dojo.event.topic.publish("dragOver", _this);
  });

  dojo.event.topic.subscribe("dragOver", function(tgt){
    if(tgt != _this && _this.dropIndicator) {
      _this._clean();
    }
  });
});