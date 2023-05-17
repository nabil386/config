/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013,2019. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 27-May-2019  JD  [RTC242600]  Increased millisecond value for timeout in 
 * setFocus().
 * 22-Sep-2014  ROR [CR00445363] Refactored to AMD module.
 */
 
/**
 * Collections of functions related to setting focus on an IEG script page.
 */
define(["dojo/dom", "dojo/topic","ieg/update-manager","ieg/ieg-modal"], 
    function(dom, topic, updateManager, iegModal) {
    // PUBLIC METHODS...
    return {
      /**
  	   * Initialise. Setup listener for page transition event and handle focus
  	   * accordingly.
  	   */
      initialise:function() {
        var handle = this;
        topic.subscribe("ieg-page-loaded", function(){
    	  // on page load/transition scroll the page to the 
          // top & check default focus
    	  handle.scrollToTop();
    	  handle.checkDefaultFocus();
        });
      },
      
      /**
	   * Sets the initial page focus to the first input field.
	   *
	   * @param element
       *    The HTML element to set focus on
       */
	  setFocus: function(element) {
	    
	    // Find all inputs
	    var inputs;
	    if(element == undefined) {
	      inputs = dojo.query("input, select, textarea");
	    } else {
	      // search within the specified element
	      inputs = dojo.query("input, select, textarea", element);
	    }

	    var positions = {};
	    inputs = inputs
	      .filter(function(n){
	        // Filter out hidden nodes & nodes that can't be tabbed to
	        return n.offsetWidth > 0 && n.tabIndex >= 0;
	      })
	      .forEach(function(input){
	        // Store the positions of each node just once
	        var id = dojo.attr(input, "id") || dojo.attr(input, "name");
	        var value = dojo.attr(input, "value") || "";
	        positions[id + value] = dojo.position(input);
	      })
	      .some(function(input){
	        try {
	          // set the focus on the first element.
	          if (dojo.isIE) {
	            window.focus();
	          } 
	          setTimeout(function() { input.focus(); }, 250);
	          return true;
	        } catch(e){
	          return false;
	        }
	      });
	  },
	  
	  /**
	   * Sets the page focus to the validation panel. 
	   */
	  setMessagesFocus: function() {
	    var messages = dojo.query("#ieg-error-messages")[0];  
	    if(messages) {
	      setTimeout(function() { messages.focus(); }, 10);
	    }
	  },
	  
	  /**
	   * Set the default focus to the print link if there are no input 
	   * elements present on the current page.
	   */
	  setDefaultFocus:function() {
	    // set the focus to the print link (if it exists)
	    setTimeout(function() { dijit.focus(dojo.byId('printItem')); }, 10);
	  },
	  
	  /**
	   * Checks for input elements on the page before setting the default focus.
	   */
	  checkDefaultFocus:function() {
        // look for possible elements to focus on
	    var inputs1 = dojo.query("input[type=text]");
	    var inputs2 = dojo.query("input[type=radio]");
	    var inputs3 = dojo.query("input[type=checkbox]");
	    var selects = dojo.query("select");
	    var textareas = dojo.query("textarea");
	    var fields = inputs1 + selects + inputs2 + inputs3 + textareas;
	    
	    // get the page focus configuration
	    var pageFocus = updateManager.getPageConfiguration("setFocus");
	    
	    // check for presence of validations panel
	    if(dojo.query("#ieg-error-messages")[0]) {
	      this.setMessagesFocus();
	    }
	    // is setFocus page configuration is set to false then set default focus
	    else if(pageFocus != undefined
	      && pageFocus == false) {
		  this.setDefaultFocus();
	    }
	    // if no input fields then set default focus
	    else if (fields.length == 0) {
	      this.setDefaultFocus();
	    }
	    // if we have input fields then focus on one of these
	    else {
	      this.setFocus();
	    }
	  },
	  
	  /**
	   * Scroll to the top of the page.
	   */
	  scrollToTop:function() {
	    if(iegModal.isInModal()) {
	      dojo.query('.modalForm')[0].scrollTop = 0;      
	    } else {
	      dojo.query('.tabForm')[0].scrollTop = 0;
	    } 
	  }
    };
});