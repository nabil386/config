/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
    "dojo", 
    "dojo/_base/declare", 
    "dijit/form/DropDownButton", 
    "cwtk/widget/_OrientedDropDown", 
    "dojo/text!./templates/DropDown.html"
], function(dojo, declare, DropDownButton, _OrientedDropDown, template){

		return declare("cwtk.widget.DropDown", [DropDownButton, _OrientedDropDown], {
		   
		   isButton : false,
		   _isFocusable : false,
		   
		   _timerHover : null,
		   
		   doEmployOrientationIE8Hack_ : true,
		   
		   postMixInProperties : function() {
			   this.inherited(arguments);
		       if(!this.isButton) {
		           this.templateString = template;
	           }
		   }, 
		   startup : function() {
			   this.inherited(arguments);
			   this.connector = dojo.query('.dijitTooltipConnector', this.dropDown.domNode)[0];
			   this.aroundNode = this._aroundNode || this.domNode;
			   if (this.isButton) {
			       this.aroundNode = dojo.query('.dijitButtonNode', this.domNode)[0];
		       }
			   dojo.connect(this.dropDown, "onMouseLeave", dojo.hitch(this, function() {
			       this.closeDropDown(true);
		       }));
			   dojo.connect(this.dropDown, "onMouseOver", dojo.hitch(this, function() {
			       clearTimeout(this._timerHover);
			       delete this._timerHover;
		       }));
			   dojo.connect(this, "onMouseLeave", dojo.hitch(this, function() {
			       if (this._opened) {
    			       this._timerHover = setTimeout(dojo.hitch(this, function(){
        			       this.closeDropDown(true);
        			   }), 2000);
			       }
		       }));
			   this.focusNode.tabIndex = (this._isFocusable ? "0" : "-1");
		       
		   }, 
		   destroy : function() {
			   this.inherited(arguments);
		   }, 
		   openDropDown : function() {
			   this.inherited(arguments);
			   this.fixDropDown();
		   }  
		});

});