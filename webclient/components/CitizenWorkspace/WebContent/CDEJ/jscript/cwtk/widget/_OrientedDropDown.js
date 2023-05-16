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
    "dojo/has"
], function(dojo, declare, has){

		return declare("cwtk.widget._OrientedDropDown", null, {
		
		   aroundNode : null,
		   connector : null,
		   dropDown: null,
		   
		   /* Orientation: M (middle), L (left), R (right) */
		   orientation : '',
		   
		   /* Offset on dropdown position */
		   offset : 0,
		   
		   fixDropDownFirst_ : true,
		   doEmployOrientationIE8Hack_ : false,
		   
		   fixDropDown : function() {
			   if (!this.orientation || this.orientation === '') return;
			   
			   //reset margins
			   this.dropDown.domNode.style.marginLeft = 0;
    		   this.dropDown.domNode.style.marginRight = 0;
		       this.connector.style.marginLeft = 0;
    		   this.connector.style.marginRight = 0;
			   
			   //if overflows to the right, remove classes and orient to right
			   this.removeRightClasses(this.dropDown.domNode);
			   
			   var posAround = dojo.position(this.aroundNode, true);
			   var posDropDown = dojo.position(this.dropDown.domNode, true);
			   var posConnector = dojo.position(this.connector, true);
			   
			   //calculate position
			   
			   if (this.orientation === 'M') {
    			   var shiftConnector = (posAround.x + posAround.w / 2) - (posConnector.x + posConnector.w / 2);
    			   var shiftContainer = (posAround.x + posAround.w / 2) - (posDropDown.x + posDropDown.w / 2);
			   }
			   else if (this.orientation === 'L') {
    			   var shiftConnector = posAround.x - posConnector.x + 10;
    			   var shiftContainer = posAround.x - posDropDown.x;
			   }
			   else if (this.orientation === 'R') {
    			   var shiftConnector = (posAround.x + posAround.w) - (posConnector.x + posConnector.w) - 10;
    			   var shiftContainer = (posAround.x + posAround.w) - (posDropDown.x + posDropDown.w);
    			   
    			   // Ugly hack for GUM-3295
    			   // - in IE only, the nodes are not visible leading to bad posXXX.x|y coordinates
    			   // - this only affects the first time the drop down is shown, thus requiring a slight "shift" to 
    			   //    position things correctly
    			   if (this.doEmployOrientationIE8Hack_ && has("ie") <= 8 && this.fixDropDownFirst_ == true) {
				   	 this.fixDropDownFirst_ = false;
				   	 shiftContainer -= 30;
				   }
			   }
			   
			   if (!shiftConnector && !shiftContainer) {
			       console.log('Orientation not found.');
			       return;
			   }
    		   			   
			   //if overflows body on the left
			   if (posDropDown.x + shiftContainer < 0) {
				   //move to left corner plus 10 pixels of relief
				   shiftContainer = posDropDown.x * -1 + 10;
			   }
			   
			   this.connector.style.marginLeft = (shiftConnector - shiftContainer) + "px";
			   this.dropDown.domNode.style.marginLeft = (this.offset + shiftContainer) + "px";
			   
		   },
		   
		   resetDropDown : function() {
		       if (this.dropDown) {
    		       this.dropDown.domNode.style.marginLeft = '';
        		   this.dropDown.domNode.style.marginRight = '';
		       }
		       if (this.connector) {
    		       this.connector.style.marginLeft = '';
        		   this.connector.style.marginRight = '';
		       }
		   },
		   
		   removeRightClasses : function(node) {
		       var hasRight = false;
		       var classNames = dojo.attr(node, "class").split(" ");
			   for (var i = 0; i < classNames.length; i++) {
			       if (classNames[i].indexOf("Right") > -1) {
			           dojo.removeClass(node, classNames[i]);
			           hasRight = true;
			       }
		       }
		       return hasRight;
		   }  
		});

});