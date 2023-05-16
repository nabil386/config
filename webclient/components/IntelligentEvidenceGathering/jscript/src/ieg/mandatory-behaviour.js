/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2014,2017. All Rights Reserved. 
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
/*
 * Modification History
 * --------------------
 * 19-Oct-2017  CMC [RTC211368]  Fixing issue with dropdowns in closed 
 *                               conditional clusters throwing validation errors. 
 * 28-Sep-2017  CMC [RTC206900]  Switched to disabling rather than destroying 
 * 								 hidden mandatory elements. 
 * 22-Sep-2014  ROR [CR00445363] Refactored to AMD module.
 */

/**
 * Functions related to controlling mandatory field behaviour on an IEG Page.
 */
define(["dojo/dom", "dojo/NodeList-traverse"], function(dom) {
    // PUBLIC METHODS...
    return {
	  
	  /**
	   * Helper function used for checking mandatory fields.
	   * 
	   * Mandatory fields in hidden clusters do not become mandatory until the 
	   * cluster is revealed. This function handles that scenario.
	   * For example, when the page is loaded and submitted this function runs 
	   * through all the fields in hidden clusters and checks if they were 
	   * mandatory. If they were it sets the mandatory flag in the o3fmeta field to
	   * false before the page is submitted.
	   */
	  conditionalMandatoryHandling: function() {
	  
	    // Get all IDs of questions in hidden clusters
	    // get all nodes of type input in hidden clusters - text, radio, checkbox
	    // NB: doesn't work correctly for all types in external mode (i.e. radio)
	    var hiddenIDs = dojo.query(
	      "div.cluster.conditional.hidden input").map("return item.id;");
	    // get all dropdowns in hidden clusters
	    hiddenIDs = hiddenIDs.concat(
	      dojo.query("div.cluster.conditional.hidden select")
	      .map("return item.id;"));
	    // get all text areas in hidden clusters
	    hiddenIDs = hiddenIDs.concat(
	      dojo.query("div.cluster.conditional.hidden textarea")
	      .map("return item.id;"));
	    // all combo boxes
	    hiddenIDs = hiddenIDs.concat(
	      dojo.query("div.cluster.conditional.hidden table[role='combobox']")
	      .map("return item.id;"));
	    
	    // we need to specifically lookup radio buttons.
	    // When dojo replaces the radio buttons with widgets it modifies the id's
	    // We need to lookup the radio buttons and return the name instead.
	    hiddenIDs = hiddenIDs.concat(
	      dojo.query("div.cluster.conditional.hidden input[type='radio']")
	      .map("return item.name;"));
		  hiddenIDs = hiddenIDs.concat(
	        dojo.query("div.cluster.conditional.hidden table[role='listbox']")
	        .map("return item.id;"));			

		var disabeledElements = [];
	    // loop through the fields and check if the mandatory flag 
	    // needs to be flipped
	    for (var i = 0; i < hiddenIDs.length; i++) {
	    	
	      var curamFilteringSelect=false;
	      if (hiddenIDs[i].indexOf('curam_widget_FilteringSelect') != -1) {
	        curamFilteringSelect=true;
	      }
		  var isMandatory = getMandatoryIndicator(hiddenIDs[i]);
		  if (isMandatory === true) {
		    var input = dojo.byId(hiddenIDs[i]);
			if (input && (input.value === undefined || input.value.length == 0) 
			  || curamFilteringSelect) {
		      var widget = dijit.byId(hiddenIDs[i]);
			  if (widget) {
			    widget.set('disabled', true);
				disabeledElements.push(hiddenIDs[i]);
		      } else {
			    input.disabled = 'disabled';
				disabeledElements.push(hiddenIDs[i]);
			  }
		    }
		  }
	    }
        // Remove all hidden inputs inside a conditional cluster which is
        // hidden, so that the inputs are not submitted so the server
        var hiddenInputs = dojo.query("div.cluster.conditional.hidden input[type='hidden']");
        for(var i = 0; i < hiddenInputs.length; i++) {
          dojo.destroy(hiddenInputs[i].id);
        }
        return disabeledElements;
	  }

    };
	    /**
	 * Get the mandatory indicator for the specified Id.
	 * 
	 * @param hiddenID The Id of the field to get the mandatory indicator for.
	 * 
	 * @return The Mandatory indicator for the specified field.
	 */
	function getMandatoryIndicator(hiddenID) {
	   var mandatoryFields = getMandatoryFields();
	   
	   if (hiddenID.indexOf('curam_widget_FilteringSelect') != -1) { 
	     // If this is a Curam Filtering Select update hiddenID
		 // to the hidden ID of the widget.
		 var curamFilteringSelect = dojo.query(
		   "input[id='" + hiddenID + "'").siblings("input[type=hidden]");
		 hiddenID = dojo.attr(curamFilteringSelect[0], "name");
	   }
	   
	   for (var i = 0; i < mandatoryFields.length; i++) {
	     if (mandatoryFields[i] == hiddenID) {
	       return true;
	     }
	   }
	   return false;
	}
	  
	/**
	 * Gets all the mandatory fields on a page.
	 * 
	 * @return List of mandatory fields.
	 */
	function getMandatoryFields() {
	   var metaInput = dojo.query("input[name='__o3fmeta_mandatory_data']")[0];
	   var mandatoryFields;
	   
	   if (metaInput.value.indexOf(",") > 0) {
	     mandatoryFields = metaInput.value.split(",");
	   } else {
	     mandatoryFields = new Array(metaInput.value);
	   }
	        
	   return mandatoryFields;
	}
    
});