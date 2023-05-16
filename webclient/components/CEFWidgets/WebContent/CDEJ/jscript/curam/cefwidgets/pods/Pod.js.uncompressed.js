/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012,2017. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Pod Widget. 
 * Builds a Dojo widget that renders a Pod.
 */ 
dojo.provide("curam.cefwidgets.pods.Pod");
require(["curam/cefwidgets/pods/TitlePane","dojo/fx","dijit/form/CheckBox","dijit/layout/ContentPane","dijit/layout/BorderContainer"]);

/**
 * Modification History
 * --------------------
 * 21-Apr-2017 AZ  [RTC 193602] Accessibility updates for VoiceOver.
 * 26-Sep-2011 BD  [CR00345824] Dojo 1.7 Upgrade. Migrate code.
 * 21-May-2011 SD  [CR00287888] Accessibility updates, corrected the order in 
                   which pod title icons are read out by screen reader. Also,
                   included state of pod when expanded or collapsed. Finally, 
                   amended HTML to prevent screen reader reading out all icon
                   text at once.
 * 16-May-2011 BD  [CR00266958] Add sceen reader text to the expand/collapse 
 *                 button and mark it as a button for accessibility. 
 * 04-May-2011 BD  [CR00265795] Added 3 new attributes for holding the 'alt' 
 *                 text on the Toggle, Close and Filter buttons. 
 *                 Added a postMixInProperties function to overwrite the 
 *                 inherited values. Added an 'img' tag to the filter button to 
 *                 allow an 'alt' attribute to be added for screen readers. This
 *                 element is placed off screen, but is still read by the screen
 *                 reader.
 * 26-Apr-2011 BD  [CR00264942] Add keyboard controls to the Pod. Allow the user
 *                 to tab to the 'close' and 'filter' icons and allow them to be
 *                 invoked using the 'enter' key.
 * 
 */

dojo.declare("curam.cefwidgets.pods.Pod", [curam.cefwidgets.pods.TitlePane], {

  // summary: 
  //    This is a copy of the dojox.widget.Portlet class with some 
  //    modifications for Curam use. The class extends the cefwidgets version of
  //    TitlePane which has a modifed template. The changes are listed below.
  //
  // Roundy Corners:
  //    The version of TitlePane used is extended to produce extra HTML 
  //    nodes that are used for adding rounded corners. 
  //
  // Broadcast "Close Portlet" event:
  //    This version also includes a broadcast event ('/dojox/mdnd/close') when a Portlet 
  //    is closed using the close icon. This gives the Container an opportunity to react to 
  //    the the close event. See onClose function.
  //
  // Performance tweaks:
  //    Where possible classes have been added to the template instead of setting them
  //    programmatically. This speeds up the rendering of the page because setting attributes
  //    on nodes has been found to be very expensive.

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
  
  podCloseButtonAltText: "",
  podToggleExpandButtonAltText: "",
  podToggleCollapseButtonAltText: "",
  podFilterExpandButtonAltText: "",
  podFilterCollapseButtonAltText: "",  
  
  buildRendering: function(){
    this.inherited(arguments);

    // Hide the portlet until it is fully constructed.
    //dojo.style(this.domNode, "visibility", "hidden");
  },

  postMixInProperties: function() {
    this.inherited('postMixInProperties', arguments);
    this.closeIconAltText = this.podCloseButtonAltText;
    
    // Add in the state of the pod for accessibility reasons
    if(this.open) {
      this.togglePodAltText = this.podToggleCollapseButtonAltText;
    } else {
      this.togglePodAltText = this.podToggleExpandButtonAltText;
    }
  },
  
  postCreate: function(){
    this.inherited(arguments);

    // Curam Modification
    // All classes moved to the custom template 'cefwidgets.pods.TitlePane.html'
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

    // Curam Modification
    // The close icon is moved to the template because it is not optional on Pods.
    //if(this.closable){
    //  this.closeIcon = this._createIcon("dojoxCloseNode", "dojoxCloseNodeHover", dojo.hitch(this, "onClose"));
    //  dojo.style(this.closeIcon, "display", "");
    //}
    
    // Remove following attributes from title node as they are interfering with 
    // ability of screen reader to read accessibility related text.
    dojo.removeAttr(this.titleBarNode, "tabindex");
    dojo.removeAttr(this.titleBarNode, "role");
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
    // Curam Modification.
    // Not required because Curam already has a mask.
    //dojo.style(this.domNode, "visibility", "visible");
  },

  _placeSettingsWidgets: function(){
    // summary: Checks all the children to see if they are instances
    //    of dojox.widget.PortletSettings.  If they are, 
    //    create an icon for them in the title bar which when clicked,
    //    calls their toggle() method.
    console.log("curam.cefwidgets.pods.Pod ::: _placeSettingsWidgets");

    dojo.forEach(this.getChildren(), dojo.hitch(this, function(child){
      if(child.portletIconClass && child.toggle && !child.attr("portlet")){
        
        this._createIcon(child.portletIconClass, child.portletIconHoverClass, child.portletIconSelectClass, dojo.hitch(child, "toggle"));
        dojo.place(child.domNode, this.containerNode, "before");
        child.attr("portlet", this);
        this._settingsWidget = child;
      }
    }));
  },

  _createIcon: function(clazz, hoverClazz, selectClazz, fn){
    // summary: 
    //    creates an icon in the title bar.

    var icon = dojo.create("button",{
      "class": "dojoxPortletIcon " + clazz,
      "waiRole": "button",
      "tabindex": "0",
      "role": "button",
      "type": "button"
    });
    var screenReaderText = dojo.create("span",{
      "class": "hiddenControlForScreenReader"
    });
    screenReaderText.innerHTML=this.podFilterExpandButtonAltText;
    dojo.place(screenReaderText, icon, "first");
    dojo.place(icon, this.closeNode, "before");
      
    // Toggle on both the onclick and keypress events.
    this.connect(icon, "onclick", fn); 
    this.connect(icon, "onkeypress", fn);
    
    this.connect(icon, "onclick", function(){
     if(this._settingsWidget.visible){
       dojo.addClass(icon, selectClazz);
       screenReaderText.innerHTML=this.podFilterCollapseButtonAltText;
     }else{
      dojo.removeClass(icon, selectClazz); 
      screenReaderText.innerHTML=this.podFilterExpandButtonAltText;
     }
    });
    this.connect(icon, "onkeypress", function(){
      if(this._settingsWidget.visible){
        dojo.addClass(icon, selectClazz);
        screenReaderText.innerHTML=this.podFilterCollapseButtonAltText;
      }else{
       dojo.removeClass(icon, selectClazz);
       screenReaderText.innerHTML=this.podFilterExpandButtonAltText;
      }
     });

    if(hoverClazz){
      this.connect(icon, "onfocus", function(){
        dojo.addClass(icon, hoverClazz);
      });
      this.connect(icon, "onmouseover", function(){
        dojo.addClass(icon, hoverClazz);
      });
      
      this.connect(icon, "onblur", function(){
        dojo.removeClass(icon, hoverClazz);
      });
      this.connect(icon, "onmouseout", function(){
        dojo.removeClass(icon, hoverClazz);
      });

      this.connect(icon, "onkeydown", function(){
        dojo.addClass(icon, selectClazz);
      });
      this.connect(icon, "onmousedown", function(){
        dojo.addClass(icon, selectClazz);
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
    
    // Ignore if the key press wasn't on the 'enter' key
    if (evt.type === "keyup" || evt.type === "keydown") {
      if (CEFUtils.enterKeyPress(evt) !== true) {
        return false;
      }
    }    
    dojo.style(this.domNode, "display", "none");
    dojo.attr(this.domNode, "closed", "true")
    dojo.publish("/dojox/mdnd/close",[this]);    
  },
  
  mouseenterClose:function () {
   dojo.addClass(this.closeNode,"dojoxCloseNodeHover");
  },
 
  mouseoutClose: function(){
   dojo.removeClass(this.closeNode,"dojoxCloseNodeHover");
  },

  mousedownClose: function(){
   dojo.addClass(this.closeNode,"dojoxCloseNodeSelect");
  },
  
  mouseupClose: function(){
   dojo.removeClass(this.closeNode,"dojoxCloseNodeSelect");
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

  destroyDescendants: function(/*Boolean*/ preserveDom){
    // do nothing - bug fix to stop the browser from crashing
  },
  
  destroy: function() {
    // do nothing - bug fix to stop the browser from crashing
  },
  
  _setCss: function(){
    this.inherited(arguments);
    
    dojo.style(this.arrowNode, "display", this.toggleable ? "":"none");

    // Need to inform the screen reader when the state of the pod changes
    if(this.open) {
      dojo.attr(this.arrowNode, "alt", this.podToggleCollapseButtonAltText);
    } else {
      dojo.attr(this.arrowNode, "alt", this.podToggleExpandButtonAltText);
    }
  }
});

dojo.declare("curam.cefwidgets.pods.PodSettings", [dijit._Container, dijit.layout.ContentPane],{
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

  // portletIconSelectClass: String
  //    The CSS class to apply to the icon in the Portlet title bar that is used
  //    to toggle the visibility of this widget when the mouse is down or
  //    when the settings widget is currently in a visible state.
  portletIconSelectClass: "dojoxPortletSettingsIconSelect",
  
  // visible: Boolean
  //    Open or Closed state. True if the settings panel is open, false 
  //    otherwise.
  visible: true,

  postCreate: function(){
    // summary:
    //    Sets the require CSS classes on the widget.

    this.inherited(arguments);
    // Start the PortletSettings widget hidden, always.
    dojo.style(this.domNode, "display", "none");
    dojo.addClass(this.domNode, "dojoxPortletSettingsContainer");

    // Remove the unwanted content pane class.
    dojo.removeClass(this.domNode, "dijitContentPane");
  },

  _setPortletAttr: function(portlet){
    // summary: 
    //    Sets the portlet that encloses this widget.
    this.portlet = portlet;
  },

  toggle: function(event){
    
    // check if the event was a key press on the 'enter' key. If not exit.
    if (event.type === "keypress") {
      if (CEFUtils.enterKeyPress(event) !== true) {
        return false;
      } else {
        //Stop the event from propagating. This was causing the title bar to 
        // collapse/expand the Pod.
        dojo.stopEvent(event);
      }
    }    
    
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
      this.visible=true;
      
    }else{
      dojo.fx.wipeOut({
        node: n,
        onEnd: dojo.hitch(this, function(){
          dojo.style(n,{"display": "none", "height": "", "width":""});
        }
      )}).play();
      this.visible=false;
    }
  }
});

dojo.declare("curam.cefwidgets.pods.PodDialogSettings", 
  curam.cefwidgets.pods.PodSettings,{
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
      dojo["require"]("dijit.Dialog");
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
