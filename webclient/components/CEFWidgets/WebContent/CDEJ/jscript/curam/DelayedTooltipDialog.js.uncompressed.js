/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * PID 5725-H26
 *
 * Copyright IBM Corporation 2014,2021.
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the US 
 * Copyright Office
 */

/* Modification
 * ------------
 * 12-Mar-2021 JD Remove aria-label from popup container and logic inside close
 *                button onclick listener which looks for events with no
 *                coordinates as bug is no longer an issue in Chrome and
 *                it was preventing close button from working on Edge Chromium
 *                when JAWs is enabled.
 * 13-Jul-2020 JD Removed a number of event listeners and moved remaining ones
 *                to be defined within postCreate(). Removed compressed
 *                code and updated with non-compressed version. Updated
 *                height of popup when first opened to be auto as size was
 *                incorrect on first opening. Changed this.connect to
 *                dojo.connect and added comments to code to make it more
 *                readable. Overrided buildRendering() and templateString 
 *                from base dijit version for accessibility purposes and
 *                moved initial focus from closeButton to domNode so tooltip
 *                contents are announced by screen reader. Added functionality
 *                to trap focus in tooltip when tabbing.
 * 24-May-2019 CM Update close button images from background images to actual
 *                images for accessibility purposes.
 * 21-Oct-2014 DM Make tooltip accessible for screen readers.
 * 10-May-2011 BD Upgraded for accessibility. Allow the tooltip to be opened 
 *                and closed from the keyboard. Added alt text to the close
 *                button.
 * 22-Feb-2011 BD Override destory and destroyDescendents functions to do nothing, 
 *                which fixes a bug that is causing the IE8 browser to crash.
 * 08-Feb-2011 BD Add on click action to remove styling from the tooltip node.
 *                This removes positional styling which is initially set to 
 *                top:5000px;left:5000px, to ensure the dialog is not seen while
 *                the page is rendering. When clicked the style is removed and 
 *                the dialog place in the correct location.
 */

dojo.provide("curam.DelayedTooltipDialog");
dojo.require("dijit.TooltipDialog");

  // ___________________________________________________________________________
  /**
   * Defines a new curam javascript widget called DelayedTooltipDialog which
   * is based on the dojo's dijit.TooltipDialog.
   *
   * The tooltip opens by clicking on a target and is closed by clicking the
   * close icon which is added to the tooltip. 
   *
   *
   */  
dojo.declare("curam.DelayedTooltipDialog", dijit.TooltipDialog, {

  closeDelay: 1000,
   
  openDelay: 1000,
  
  orientation: null,

  targetNode: "",
  
  closeButton: "",
  
  closeButtonAltText: "",

  /*
   * Parameter for localized text
   * which gets announced by screenreader
   * to indicate a dialog has opened.
   */
  tooltipDialogAnnouncementText: "",

  /*
   * Parameter for close button
   * default image source path.
   */
  closeButtonDefaultIcon: "",
  
  /*
   * Parameter for close button
   * hover image source path.
   */
  closeButtonHoverIcon: "",

  /*
   * Override the dijit templateString with custom HTML template.
   * This has been done for accessibility purposes in order to add two hidden
   * spans. These will contain the text of the tooltip title and content within.
   * aria-labelledby and aria-describedby attributes have been added to the
   * domNode too and they point at the hidden spans.
   */
  templateString: dojo.cache("curam", "templates/TooltipDialog.html"),

  /*
   * Override the dijit buildRendering function to populate the hidden spans
   * defined in the custom template.
   * These spans are used for accessibility purposes in order to have a 
   * screenreader announce the tooltip title and content.
   * aria-labelledby and aria-describedby attributes have been added
   * to the domNode in the custom HTML template and they point to the hidden
   * span content.
   * The text of the tooltip title and content is scrapped before rendering and
   * added into the spans.
   */
  buildRendering: function(){
    this.inherited(arguments);

    // Get data object containing tooltip title and content.
    var tooltipDataNode = this.content.firstChild;
    // Get tooltip title and content divs.
    var tooltipTitleNode = dojo.query('div[class*="tooltip-title"]', tooltipDataNode)[0];
    var tooltipContentNode = dojo.query('div[class*="tooltip-inner-panel"]', tooltipDataNode)[0];
    if(tooltipTitleNode) {
      // Get hidden span that will be populated with tooltip title text.
      // As the span is added in custom HTML template, no need to check if it exists.
      var tooltipTitleHiddenLabelSpan = this.domNode.children[0];
      var tooltipTitleHiddenLabelSpanID = this.id + "_tooltip_title";
      if (tooltipTitleHiddenLabelSpan && tooltipTitleHiddenLabelSpan.id === tooltipTitleHiddenLabelSpanID) {
          tooltipTitleHiddenLabelSpan.innerHTML = tooltipTitleNode.innerText;
          var ua = navigator.userAgent.toLowerCase();
          // If the browser is IE, add text to title to indicate a dialog is present.
          if ((/msie/.test(ua)||/trident/.test(ua)) && !/opera/.test(ua)) {
            tooltipTitleHiddenLabelSpan.innerHTML = tooltipTitleHiddenLabelSpan.innerHTML + " " + this.tooltipDialogAnnouncementText;
          }
      }
    }

    if(tooltipContentNode) {
      // Get hidden span that will be populated with tooltip content text.
      // As the span is added in custom HTML template, no need to check if it exists.
      var tooltipTitleHiddenDescriptionSpan = this.domNode.children[1];
      var tooltipTitleHiddenDescriptionSpanID = this.id + "_tooltip_content";
      if(tooltipTitleHiddenDescriptionSpan && tooltipTitleHiddenDescriptionSpan.id === tooltipTitleHiddenDescriptionSpanID){
        tooltipTitleHiddenDescriptionSpan.innerHTML = tooltipContentNode.innerText;
      }
    }
  },

  postCreate: function() {
  
    this.inherited(arguments);
    
    dojo.style(this.domNode, "display", "none");
    
    // Create inner html for button
    var link = 
      "<div>" +
         "<img src=\""+this.closeButtonDefaultIcon + "\" alt=\"\" class=\"tooltip-close-default-icon\">" +
         "<img src=\""+this.closeButtonHoverIcon + "\" alt=\"\" class=\"tooltip-close-hover-icon\">" +
         "<span class=\"hiddenControlForScreenReader\">" +
          this.closeButtonAltText +
         "</span>" +
      "</div>"

    // find the tooltip title node and add the close icon to it
    var tooltipTitleContent = dojo.query('div[class*="tooltip-title"]', this.domNode)[0];
    this.closeButton =
      dojo.create("button", { innerHTML: link }, tooltipTitleContent);
    dojo.attr(this.closeButton, "class",
      "tooltip-close-button tooltip-close-button-normal");
    dojo.attr(this.closeButton, "tabIndex", "0");

    // Store a copy of 'this' for inner functions
    var _this = this;

    // Add onkeypress event listener to domNode
    dojo.connect(this.domNode, "onkeypress",
      function(event) {

        // If ESC key is pressed when tooltip is open, close it.
        if(event.keyCode == 27) {
          _this.close();
          dojo.stopEvent(event); 
        } else if(document.activeElement === _this.domNode){
          // If the focus is currently on the domNode and TAB or SHIFT-TAB key has been pressed,
          // determine what the first and last focusable element in the tooltip dialog is and then
          // focus on the appropriate element. This will trap with keyboard focus within the tooltip.
          var focusableElements =
              'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';
          // Use querySelectorAll instead of dojo.query as that guarantees order.
          var focusableContent = _this.domNode.querySelectorAll(focusableElements);
          var firstFocusableElement = focusableContent[0];
          var lastFocusableElement = focusableContent[focusableContent.length - 1];

          // If SHIFT-TAB was pressed, focus on last element.
          // If TAB was pressed, focus on first element.
          if(event.keyCode == 9 && event.shiftKey) {
            lastFocusableElement.focus();
            dojo.stopEvent(event);
          } else if(event.keyCode == 9 && !(event.shiftKey)){
            firstFocusableElement.focus();
            dojo.stopEvent(event);
          }
      }
    });

    // In IE11, when the domNode is focused on, there is no focus outline to
    // indicate that focus is present on the element. The following two event
    // listeners are for simulating the IE11 focus outline being added/removed 
    // on focus/blur. This has been done for accessibility purposes.
    dojo.connect(this.domNode, "onfocus",
      function() {
        if (window.document.documentMode && _this._popupWrapper) {
          dojo.attr(_this._popupWrapper, "style", {"outline": "1px dotted #686868"});
        }
    });

    dojo.connect(this.domNode, "onblur",
      function() {
        if (window.document.documentMode && _this._popupWrapper) {
          dojo.attr(_this._popupWrapper, "style", {"outline": "0"});
        }
    });

    // Define various event listeners for close button.
    dojo.connect(this.closeButton, "onmouseover",
      function() {
        dojo.attr(_this.closeButton, "class",
          "tooltip-close-button tooltip-close-button-mouseover");
        dojo.attr(_this.closeButton, "style", {cursor:"pointer"});
    });
    dojo.connect(this.closeButton, "onfocus",
      function() {
        dojo.attr(_this.closeButton, "class",
          "tooltip-close-button tooltip-close-button-mouseover");
    });
    dojo.connect(this.closeButton, "onmouseout",
      function() {
       dojo.attr(_this.closeButton, "class",
         "tooltip-close-button tooltip-close-button-normal");
    });
    dojo.connect(this.closeButton, "onblur",
      function() {
       dojo.attr(_this.closeButton, "class",
         "tooltip-close-button tooltip-close-button-normal");
    });
    dojo.connect(this.closeButton, 'onclick', function(event) {
     _this.close();
     dojo.stopEvent(event);
    });
    // If SPACEBAR or ENTER is pressed on the close button, close the tooltip.
    dojo.connect(this.closeButton, 'onkeypress', function(event) {
      if (CEFUtils.enterKeyPress(event)) {
        _this.close();
        dojo.stopEvent(event);
      } else {
        _this.closeButton.focus();
      }
    });

    // Add event handlers for the tooltip icon.
    if (this.targetNode) {
    
      var targetNode = dojo.byId(this.targetNode);
      
      dojo.connect(targetNode, "onclick", _this, function(event) {
        _this.openTooltip(event);
        dojo.stopEvent(event);
      });
      dojo.connect(targetNode, "onkeypress", _this, function(event) {
        if (CEFUtils.enterKeyPress(event)) {
          _this.openTooltip(event);
          dojo.stopEvent(event);
        }
      });
      dojo.connect(targetNode, "onmouseover", function() {
        dojo.attr(targetNode, "style", {cursor:"pointer"});
        dojo.attr(targetNode, "class", "rollover");
      });
      dojo.connect(targetNode, "onfocus", function() {
        dojo.attr(targetNode, "style", {cursor:"pointer"});
        dojo.attr(targetNode, "class", "rollover");
      });
      dojo.connect(targetNode, "onblur", function() {
        dojo.attr(targetNode, "class", "normal");
      });
      dojo.connect(targetNode, "onmouseout", function() {
        dojo.attr(targetNode, "class", "normal");
      });
     
    }
  },

  openTooltip: function(event) {
    
    // summary:
    //       open the tooltip associated with the item that was just clicked or
    //       entered.
    // 
    // parameter: Event
    //       the event from which this function was invoked.

    if (!((event.type==="click")||CEFUtils.enterKeyPress(event))) {
      return;
    }
    var _this = this;

    var tooltip = dojo.byId(this.id);
    dojo.removeClass(tooltip,'tooltip-startup-position');

    var node = dijit.popup.open({
        popup: _this,
        around: _this.targetNode,
        orient: _this.orientation
    });

    if (this._popupWrapper){
      dojo.attr(this._popupWrapper, "style", {height:"auto"});
      dojo.removeAttr(this._popupWrapper, "aria-label");
    }

    // Focus on the domNode instead of the close button.
    // This was changed for accessibility purposes as only the close button
    // would be announced when it received focus. No content would be read out.
    // Focussing on the domNode instead causes the screen reader to announce the
    // content contained in the elements referenced by the aria-labelledby and
    // aria-describedby attributes.
    this.domNode.focus();

  },

  close: function() {
    
    // summary:
    //    Closes the tooltip and resets the tooltips CSS class.
    dijit.popup.close(this);
    dojo.attr(this.closeButton,
      "class", "tooltip-close-button tooltip-close-button-normal");
    dojo.byId(this.targetNode).focus();
  }
  
});
