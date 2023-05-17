/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013,2021. All Rights Reserved. 
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
var modules = [
  "dojo/domReady",
  "dojo/dom-style",
  "dojo/ready",
  "dijit/a11y",
  "dojo/store/Memory",
  "dojo/_base/lang",
  "dijit/form/TextBox",   
  "dijit/form/RadioButton",
  "dijit/form/Select",
  "dijit/form/Button",
  "dijit/DropDownMenu",
  "dijit/MenuItem",  
  "idx/form/Select",
  "idx/form/Textarea",      
  "idx/form/CheckBox",
  "idx/form/DateTextBox",
  "curam/widget/IDXFilteringSelect",
  "idx/widget/HoverHelpTooltip",
  "idx/widget/Dialog",
  "dijit/form/SimpleTextarea",      
  "dijit/form/DateTextBox"
];
  
require(modules, function(domReady, domStyle, ready, a11y, Memory, lang, 
  dijitTextBox, dijitRadioButton, dijitButton, dijitSelect, 
  dijitFilteringSelect, dijitDialog,
  oneuiTextArea, oneuiCheckBox, oneuiDateTextBox, oneuiHoverHelpTooltip) {
	
    /**
     * Search for nodes that contains the role="listbox"
     * and get the aria-label from the inner input node to
     * be included on node with listbox.
     */
	function _insertAriaLabelNodeContainingRoleListBox() {
		dojo.query('[role=\"listbox\"]').
	      forEach(function(node, index, arr) {
	        // get aria-label from inner input node
	        var inputs = dojo.query('input', node);
	        inputs = inputs.filter(function (input) {
	            return  input.attributes["aria-label"] != null 
	        })[0];	     
	        if(inputs) {
	          node.setAttribute("aria-label", inputs.attributes["aria-label"].value);
	        }
	      });
	  
	}

    
  // ** OneUI Widget Fixes **
    
	/**
	 * When the page has finished loading an event is fired.
	 * Any items that need to hook into this event can be put here.
	 */
	require(["dojo/topic", "ieg/help-behaviour"], function(topic, helpBehaviour){
	  topic.subscribe("ieg-page-loaded", function(){
		  // check the heights of the sections panel
	    bootstrap.setSectionPanelsHeight();
	    bootstrap.setPageHeadings();
	  
	    // Set the aria-label attribute on the filtering selects for JAWS.
	    // NB: this must come after the code above which changes the role of
	    // date fields to textbox.
	    _insertAriaLabelNodeContainingRoleListBox();
	    
	    // set the correct "aria-valuenow" value
	    dojo.query('.dijitReset.dijitInputInner').
	      forEach(function(node, index, arr){
	        node.removeAttribute("aria-valuenow");
	    });
	    
	    // remove empty and disabled labels on dojo checkboxes
	    dojo.query('.idxLabel.dijitInline.dijitHidden').
	      forEach(function(node, index, arr){
          if(node.firstChild.tagName.toLowerCase() == "label") {
		      if(node.firstChild.childNodes.length == 0) {
		    	  // There is no label text present therefore
		    	  // this is an empty label.
		    	  dojo.destroy(node);
		      }
	        }
		});
	  
	    // There is a known issue with the Dijit dropdown widget when used in
	    // certain circumstances. The widget detaches from its parent input field.
	    // This method adds some javascript to a page which listens for the 
	    // onscroll event and closes any open popups/dropdowns.
	    dojo.connect(dojo.query('.tabForm')[0], 'onscroll', function(e){
	      // widget dropdowns
	      dojo.query('.dijitPopup').
	        forEach(function(node, index, arr){
	          helpBehaviour.closeHelpDialog(node);
	        });
	    
	      // Hover Help tooltip
	      dojo.query('.idxOneuiHoverHelpTooltip').
	        forEach(function(node, index, arr){              
	           var dialog = dijit.byId(node);
	           dijit.popup.close(dialog);
	           dialog.removeAttribute('style');
	        });
	    });
	  });
	});
	
	// Patch the _getFocusItems method of idx.widget.Dialog
	// There appears to be a bug in this function. It looks for elements that are
	// tabbable but only looks in the content node. Any buttons added to the 
	// dialog do not live in this node and will therefore never be picked up.
	// Modified the function to also look for a button that is focusable.
	(function() {
	  idx.widget.Dialog.prototype._getFocusItems = 
  	  function(message, force){
  	    if(this._firstFocusItem){
          this._firstFocusItem = this._getFirstItem();
          if(!this._firstFocusItem){
            var elems = a11y._getTabNavigable(this.containerNode);
            this._firstFocusItem = this.buttons[0].focusNode || elems.lowest 
              || elems.first || this.closeButton.focusNode || this.domNode;
          }
          return;
        }
        var elems = a11y._getTabNavigable(this.containerNode);
        this._firstFocusItem = this.buttons[0].focusNode || elems.lowest 
          || elems.first || this.closeButton.focusNode;
        this._lastFocusItem = this.closeButton.focusNode;
  	  };
	})();
	
	// Patch the displayMessage() method of the FilteringSelect.
	// This method displays a tooltip on the select if the entered text is 
	// invalid. This code does not seem to be complete on the widget as it was
	// displaying the message in the wrong position. 
	(function() {      
	  idx.form.FilteringSelect.prototype.displayMessage = 
	    function(message, force){
	      if(this.messageTooltip){
	        this.messageTooltip.set("label", message);
	        if(message && this.focused || force ){
	          this.messageTooltip.open(this.oneuiBaseNode);
	        }else{
	          this.messageTooltip.close();
	        }
	      }
	    };
	})();     
});