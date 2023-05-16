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
    "idx/widget/HoverHelpTooltip", 
    "cwtk/widget/_OrientedDropDown" 
], function(dojo, declare, HoverHelpTooltip, _OrientedDropDown){


		return declare("cwtk.widget.HoverHelpTooltip", [HoverHelpTooltip, _OrientedDropDown], {
		
		   handleReset : null,
		   
		   startup : function() {
		       this.inherited(arguments);
		   },
		   
		   _onHover : function() {
		       this.inherited(arguments);
		   },
		   
		   _onUnHover : function() {
		       this.inherited(arguments);
		   },
		   
		   open : function(target) {
		       this.inherited(arguments);
			   
			   this.aroundNode = this._connectNode;
			   this.dropDown = HoverHelpTooltip._masterTT;
			   this.connector = dojo.query('.idxOneuiHoverHelpTooltipConnector', this.dropDown.domNode)[0];
			   this.resetDropDown();
			   
			   var self = this;
		       this.handleReset = dojo.connect(this.dropDown, "_onHide", function(){
		           dojo.disconnect(self.handleReset);
		           self.resetDropDown();
		       });
			   
			   this.fixDropDown();
			   
			   // Fix horizontal alignment of the tooltip. GLS-1748
			   if(!this.isLeftToRight()){
			       var marginLeft = this.dropDown.domNode.style.marginLeft;
			       this.dropDown.domNode.style.marginLeft = this.dropDown.domNode.style.marginRight;
			       this.dropDown.domNode.style.marginRight = "-" + marginLeft;
			    }
		   } 
		});

});