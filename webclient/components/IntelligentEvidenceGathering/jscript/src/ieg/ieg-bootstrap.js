/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2014,2020. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

var modules = [
  "ieg/ieg-focus",
  "ieg/ieg-modal",
  "dojo/domReady",
  "dojo/ready",
  "dojo/topic",
  "curam/util/LocalConfig",
  "dojo/fx",
  "dojo/number",
  "dojo/behavior",
  "dojo/dom",
  "dojo/parser",
  "dojo/html",
  "dijit/registry",
  "dijit/Dialog",
  "dijit/focus",
  "dijit/registry",
  "dojox/layout/ContentPane",
  "dojox/widget/Standby",
  "cm/_base/_form",
  "cm/_base/_behavior",
  "cm/_base/_dom",
  "cm/_base/_topics",
  "cm/_base/_pageBehaviors",
  "cm/_base/_validation",
  "curam/util",
  "curam/util/Dialog",
  "curam/IEGTooltip",
  "ieg/help-behaviour"];

/**
 * When the page has finished loading an event is fired.
 * Any items that need to hook into this event can be put here.
 */
require(["dojo/topic", "dojo/query", "dojo/on", "dojo/dom-style", "dojo/dom-class", "dojo/dom-attr",
         "dojo/fx", "ieg/mandatory-behaviour"],
		  function(topic, query, on, domStyle, domClass, domAttr, coreFx){
  topic.subscribe("ieg-page-loaded", function(){
    // discover if this is an internal or external style script
    bootstrap.setScriptStyleFlag();

    // hookup IEG navigation links on page load/transition
    bootstrap.connectNavigationLinks();

    // apply any overrides defined
    bootstrap.applyStyleOverrides();

    bootstrap.setPageHeadings();

    // Expand Collapse Functionality
    bootstrap.connectExpandCollapseToggle();
    
    // (re)apply page behaviors
    dojo.behavior.apply();

    // If required, inform screen reader about the currently selected person tab
    bootstrap.informScreenReaderAboutCurrentPersonTab();
    
    // If present, attach behavior to the print button
    if (dojo.byId("printItem")) {
      cm.addBehavior('PRINT');
    }
  });
});

/**
 * This section executes at the very start of the IEG Player once.
 */
require(modules, function(iegFocus, iegModal, domReady, ready, topic, localConfig) {
    ready(function() {
      console.log("IEG Player Started");

      curam.util.extendXHR();
      // if running in modal then call loadFinished
      if(iegModal.isInModal()) {
        iegModal.loadFinished();
      }

      // initialise the IEG Player focus manager
      iegFocus.initialise();

      // Get pageTitleHeading element
      var pageTitleHeading = dojo.byId("pageTitleHeading");

      // Update window title in modal
      if(iegModal.isInModal()) {
        if(pageTitleHeading && pageTitleHeading.textContent) {
          iegModal.setModalTitle(pageTitleHeading.textContent);
        }
      }

      // Update iframe title
      var setPageTitleAsWindowTitle = localConfig.readOption(
          "iegConfig_PROP_SET_PAGE_TITLE_AS_WINDOW_TITLE_PROP", 'false');
      if(iegModal.isInModal() || setPageTitleAsWindowTitle === "true") {
        if(pageTitleHeading && pageTitleHeading.textContent) {
          window.document.title = pageTitleHeading.textContent;
        }
      }

      // Set browser tab title for initial page
      if(pageTitleHeading && pageTitleHeading.textContent) {
		if (curam.util.getTopmostWindow() === window) {
			//Prevent the tab title from displaying 'undefined' when running standalone
			curam.util._browserTabTitleData.separator = '';
			curam.util._browserTabTitleData.staticTabTitle = '';
		}
        curam.debug.log("ieg.ieg-bootstrap.ready calling curam.util.setBrowserTabTitle");
        curam.util.getTopmostWindow().curam.util.setBrowserTabTitle(pageTitleHeading.textContent);
      } else {
        curam.debug.log("Error getting element with id 'pageTitleHeading' (var pageTitleHeading = " + pageTitleHeading + ")");
      }

      // fire the page loaded event on initial page load
      topic.publish("ieg-page-loaded");

      // publish onto the dojo topic which removes the Progress Widget
      curam.util.getTopmostWindow().dojo.publish('/curam/progress/unload');
    });
});

/**
 * Using a single global object to wrap all functions.
 */
var bootstrap = {
  /**
   * Hookup the IEG Player navigation links such as Add & Edit.
   */
  connectNavigationLinks:function() {
    require(['dojo/query','dojo/on', 'ieg/ieg-xhr'],
      function(query, on, iegXHR) {
      query(".iegNavigationLink").forEach(
        function(node) {
          // Skip links with a class of bhv-grouped-head-select. These links
          // will have already had different onclick functions defined by
          // cm.registerBehavior("IEG_GROUPED_HEAD_SELECT", { ... } ) in
          // ieg-behaviors.js.
          if (!dojo.hasClass(node, "bhv-grouped-head-select")) {
            on(node, 'click', function(e) {
              dojo.stopEvent(e);
              return iegXHR.iegLink(e, this.id);
            });
          }
        });
      });
  },

  /**
   * Apply various overrides required for items such as accessibility.
   */
  applyStyleOverrides:function() {
    // The date widget has the incorrect role for JAWS. Set the role
    // to textbox on the appropriate node
    dojo.query('.dijitDateTextBox').
     forEach(function(node, index, arr){
        node.setAttribute("role", "listbox");
    });
  },

  /**
   * Sets a flag to denote if the script is being run in internal or external
   * mode. This relies on the class attribute of the Body node being "oneui".
   */
  _isExternalMode: false,
  setScriptStyleFlag: function() {
    require(["dojo/_base/window", "dojo/dom-class"], function(win, domClass){
        bootstrap._isExternalMode = false;
        if (domClass.contains(win.body(), "oneui")){
          bootstrap._isExternalMode = true;
        }
    });
  },
  isExternalMode: function() {
    return bootstrap._isExternalMode;
  },

  /**
   * When viewed on a low resolution when the sections panel contains
   * a large amount of text the text was overflowing onto the next sections
   * panel. This is a fix that will stretch the all of the section panels to
   * the size of the largest panel
   */
  setSectionPanelsHeight:function() {
    require(["dojo/dom-style"], function(domStyle){
      var maxHeight = 0;
      var sectionPanelHeights = new Array();
      var i = 0;

      // Find the max height of the sections panels
      dojo.query('.sectionLabelSpan').
        forEach(function(node, index, arr){
          var spanHeight = domStyle.get(node, "height");
          sectionPanelHeights[i] = spanHeight;
          i++;
      });

      maxHeight = Math.max.apply(Math, sectionPanelHeights);
      maxHeight = maxHeight.toString();
      maxHeight = maxHeight + 'px';

      // Apply the max height to the parent div of all sections panels
      // to make them all the same height
      dojo.query('.inner-section-div').
        forEach(function(node, index, arr){
          domStyle.set(node,'height',maxHeight);
      });
    });
  },


  setPageHeadings: function() {
    if(window == window.top){
      require(["dojo/dom-construct"], function(domConstruct){
        var pageTitleHeading = dojo.byId("pageTitleHeading");
        var pageSubtitleHeading = dojo.byId("pageSubtitleHeading");
        var newHeading = document.createElement('h1');
		var newSubHeading = document.createElement('h2');

		//replace title h2 heading with h1
        newHeading.innerHTML = pageTitleHeading.innerHTML;
        newHeading.setAttribute("id", "pageTitleHeading");
        domConstruct.place(newHeading, pageTitleHeading, "replace");


		//replace sub-title heading h3 with h2
        newSubHeading.innerHTML = pageSubtitleHeading.innerHTML;
        newSubHeading.setAttribute("id", "pageSubtitleHeading");
        domConstruct.place(newSubHeading, pageSubtitleHeading, "replace");

        // Check page for cluster titles
        dojo.query("h3").forEach(function(node, index, arr){
          var newClusterTitle = document.createElement("h2");
          newClusterTitle.innerHTML = node.firstChild.nodeValue;
          if(node.getAttribute("class") != null) {
            newClusterTitle.setAttribute("class", node.getAttribute("class"));
          }
          domConstruct.place(newClusterTitle, node, "replace");
        });
      });
    }
  },
  
  connectExpandCollapseToggle : function() {
	  require(["ieg/expand-collapse-toggle"], function(expandCollapseToggle){
		  expandCollapseToggle.connectExpandCollapseToggle();
	  });
  },

  informScreenReaderAboutCurrentPersonTab : function() {
	  var hiddenPersonTabNameContainer = dojo.query(".hiddenPersonTabNameContainer")[0];
	  var wasPersonTabs = dojo.query(["name$=wasPersonTabs"]);
	  if (wasPersonTabs.length !== 0 && hiddenPersonTabNameContainer !== undefined) {
		  // if this page has person tabs
		  dojo.empty(hiddenPersonTabNameContainer);
		  var erroMessageContainer = dojo.byId("ieg-error-messages");
		  if (erroMessageContainer == null) {
			  var currentPersonName = dojo.query(".currentPerson")[0].title;
			  var personTabInfoDiv = document.createElement("div");
			  if (currentPersonName !== "") {
				  // when the person tab selected contains info on a current person
				  require(["dojo/ready", "curam/util/LocalConfig"], function(ready, localConfig) {
					  ready(function() {
						  var selectedPersonTabAnnouncement =
							  localConfig.readOption("iegConfig_PROP_SELECTED_PERSONTAB_ACCESSABILITY_ANNOUNCEMENT");
						  personTabInfoDiv.innerHTML = selectedPersonTabAnnouncement + " " + currentPersonName;
						  hiddenPersonTabNameContainer.appendChild(personTabInfoDiv);
					  });
				  });
			  } else {
				  // when the person tab selcted represents a new person
				  require(["dojo/ready", "curam/util/LocalConfig"], function(ready, localConfig) {
					  ready(function() {
						  var unknownPersonTabAnnouncement =
							  localConfig.readOption("iegConfig_PROP_UNKNOWWN_PERSONTAB_ACCESSABILITY_ANNOUNCEMENT");
						  personTabInfoDiv.innerHTML = unknownPersonTabAnnouncement;
						  hiddenPersonTabNameContainer.appendChild(personTabInfoDiv);
					  });
				  });
			  }
		  }
	  }
  }
};

/**
 * dc is a legacy function attached to the submit
 * event of a form by the cing:form tag.
 * It is not needed, but defining it here so an error is not thrown.
 *
 * @deprecated Not used anymore
 */
if (typeof(dc) == "undefined") {
  dc = function(){}
}
