/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2013,2017. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 19-Jun-2017  JD  [RTC199030] Require CM module for Intern testing purposes.
 * 07-Mar-2017  GB  [RTC188674]  Moving the aria-expanded
 * 								from the img to the td.
 * 27-Feb-2017	GB	[RTC189529] Fixing a defect that makes
 * 								the help text be displayed
 * 								incorrectly when a specific
 * 								cluster is expanded.
 * 21-Feb-2017	GB	[RTC188674] Making the expand collapse
 * 								toggle keyboard accessible.
 * 25-Jan-2017  GB 	[RTC185482] Initial Version.
 */

/**
 * Collections of functions related to the expand collapse toggle.
 */
define([], function() {

    // PUBLIC METHODS...
    return {

    	/**
  	   * Defines the initial state of the toggle and also adds a onClick event to
  	   * change the state by using the verifyStateForExpandCollapseToggle function.
  	   */
  	  connectExpandCollapseToggle: function() {
  		  require(["dojo/query", "dojo/on", "dojo/keys"],
  				  function(query, on, keys){
  			  query(".expand-collapse-td").forEach(function(node){
  				verifyStateForExpandCollapseToggle(node, true);
  				  on(node, "click", function() {
  					verifyStateForExpandCollapseToggle(this, false);
  				  });
  				  on(node, "keydown", function(event){
  					  if (event.keyCode == keys.ENTER) {
  						verifyStateForExpandCollapseToggle(this, false);
  					  }
  				  });
  			  });

  		  });
  	  }
    };

    /**
	 * Changes the state of the toggle according to the parameters passed and also its current state.
	 */
	function verifyStateForExpandCollapseToggle (element, firstTime) {
	  require(["dojo/query", "dojo/on", "dojo/dom-style", "dojo/dom-class", "dojo/dom-attr", "dojo/fx", "cm/_base/_dom"],
			  function(query, on, domStyle, domClass, domAttr, coreFx){
		var expandCollapseImg = query(".expand-collapse-img", element)[0];
		var wrapper = cm.getParentByClass(element, "cluster")
		|| cm.getParentByClass(element, "list");
		if (wrapper == null && cm.getParentByClass(element, "listHeadingTable") != null) {
			wrapper = cm.getParentByClass(element, "listHeadingTable").parentNode;
		} else if (wrapper == null && cm.getParentByClass(element, "expand-relationship-summary-list") != null) {
			wrapper = cm.getParentByClass(element, "expand-relationship-summary-list").parentNode;
		}

		if (expandCollapseImg != null && wrapper != null) {
			if (firstTime) {
				if (domClass.contains(expandCollapseImg, "expanded-element")) {
			        domAttr.set(element, "aria-expanded", "true");

			        if (wrapper.children != null) {
			        	for (var i = 1; i < wrapper.children.length; i++) {
			        		if (!domClass.contains(wrapper.children[i], "helpDiv")) {
			        			domStyle.set(wrapper.children[i], "display", "block");
			        		}
			        	}
			        }
				} else if (domClass.contains(expandCollapseImg, "collapsed-element")) {
			        domAttr.set(element, "aria-expanded", "false");

			        if (wrapper.children != null) {
			        	for (var i = 1; i < wrapper.children.length; i++) {
			        		domStyle.set(wrapper.children[i], "display", "none");
			        	}
			        }
				}
			} else {
				if (domClass.contains(expandCollapseImg, "expanded-element")) {
					domClass.remove(expandCollapseImg, "expanded-element");
			        domClass.add(expandCollapseImg, "collapsed-element");
			        domAttr.set(element, "aria-expanded", "false");

			        if (wrapper.children != null) {
			        	for (var i = 1; i < wrapper.children.length; i++) {
			        		if (!domClass.contains(wrapper.children[i], "helpDiv")) {
			        			domStyle.set(wrapper.children[i], "display", "block");
			        		}
			        		coreFx.wipeOut({
			    		      node: wrapper.children[i]
			    		    }).play();
			        	}
			        }
				} else if (domClass.contains(expandCollapseImg, "collapsed-element")) {
					domClass.remove(expandCollapseImg, "collapsed-element");
			        domClass.add(expandCollapseImg, "expanded-element");
			        domAttr.set(element, "aria-expanded", "true");

			        if (wrapper.children != null) {
			        	for (var i = 1; i < wrapper.children.length; i++) {
			        		if (!domClass.contains(wrapper.children[i], "helpDiv")) {
				        		domStyle.set(wrapper.children[i], "display", "none");
				        		coreFx.wipeIn({
				        		      node: wrapper.children[i]
				        		    }).play();
			        		}
			        	}
			        }
				}
			}
		}
	  });
	}
});
