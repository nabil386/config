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
    "dojo/_base/lang", 
    "dijit/_Widget",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dojo/text!./templates/InlineDropDown.html"
], function(dojo, declare, lang, _Widget, _TemplatedMixin, _WidgetsInTemplateMixin, template){

		return declare("cwtk.widget.InlineDropDown", [_Widget,_TemplatedMixin,_WidgetsInTemplateMixin], {
		
		   buttonNode: null,
		   dropdownNode: null,
		   tooltipContainer: null,
		   containerWrapper: null,
		   
		   templateString: template,
		   
		   /* Properties */
		   
		   maxWidth : '50%', 
		   
		   postMixInProperties : function() {
		       this.inherited(arguments);
	       },
		   
		   buildRendering : function() {
               this.inherited(arguments);
		   },
		   
           startup : function() {
               this.inherited(arguments);
               dojo.connect(this, "resize", this._onResize);
               dojo.connect(this.dropdownNode, "openDropDown", dojo.hitch(this, function(){ 
                   this.fixDropDown();
               }));
               dojo.place(dojo.clone(this.containerNode), this.tooltipContainer);
               setTimeout(dojo.hitch(this, function(){this._onResize();}), 300);
           },
           
           _onResize : function() {
               this.fixLayout();
           },
           
           fixDropDown : function() {
		   
			   //reset margins
			   this.dropdownNode.dropDown.domNode.style.marginLeft = 0;
		       this.dropdownNode.connector.style.marginLeft = 0;
			   
			   var posAround = dojo.position(this.dropdownNode.aroundNode, true);
			   var posConnector = dojo.position(this.dropdownNode.connector, true);
			   var posWrapper = dojo.position(this.containerWrapper, true);
			   var posTooltip = dojo.position(this.tooltipContainer, true);
			   
			   //calculate position
    		   var shiftConnector = (posAround.x + posAround.w / 2) - (posConnector.x + posConnector.w / 2);
    		   var shiftContainer = posWrapper.x - posTooltip.x;
			   
			   this.dropdownNode.connector.style.marginLeft = (shiftConnector - shiftContainer) + "px";
			   this.dropdownNode.dropDown.domNode.style.marginLeft = shiftContainer + "px";
			   
		   },
           
           fixLayout : function() {
           
               var containerWidth = 0;
               dojo.query(">", this.containerNode).forEach(function(node){
                   var pos = dojo.position(node);
                   containerWidth += pos.w;
               });
               dojo.style(this.containerNode, 'width', containerWidth + 'px');
               
               
               var wrapperWidth;
               if (this.maxWidth.indexOf('%') > -1) {
                   wrapperWidth = dojo.position(this.domNode.parentNode).w * parseInt(this.maxWidth) / 100;
               } else {
                   wrapperWidth = parseInt(this.maxWidth);
               }
               
               if (containerWidth <= wrapperWidth) {
                   dojo.addClass(this.dropdownNode.domNode, 'dijitHidden');
                   dojo.style(this.containerWrapper, 'width', wrapperWidth + 'px');
               } else {
                   dojo.removeClass(this.dropdownNode.domNode, 'dijitHidden');
                   var posAround = dojo.position(this.dropdownNode.aroundNode, true);
                   dojo.style(this.containerWrapper, 'width', wrapperWidth - posAround.w + 'px');
               }
           },
           
           destroy : function() {
               this.inherited(arguments);
           }
        });

});