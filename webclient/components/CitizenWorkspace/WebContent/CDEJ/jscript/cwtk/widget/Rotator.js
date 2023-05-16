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
    "dojo/text!./templates/Rotator.html"
], function(dojo, declare, lang, _Widget, _TemplatedMixin, _WidgetsInTemplateMixin, template){

		return declare("cwtk.widget.Rotator", [_Widget,_TemplatedMixin,_WidgetsInTemplateMixin], {

		   prevNode: null,
		   nextNode: null,
		   containerWrapper: null,

		   templateString: template,

		   shift : 150,

		   duration : 750,

		   postMixInProperties : function() {
		       this.inherited(arguments);
	       },

		   buildRendering : function() {
               this.inherited(arguments);
		   },
           startup : function() {
               this.inherited(arguments);
               dojo.connect(this, "resize", this._onResize);

           },

           _onResize : function() {
               this.fixWidth();
               this.showArrows();

               setTimeout(dojo.hitch(this, function(){
                   this._slide(0);
               }), 1000);
           },

           fixWidth : function() {

               var totalWidth = 0;
               dojo.query(">", this.containerNode).forEach(function(node){
                   totalWidth += dojo.position(node).w;
               });
               dojo.style(this.containerNode, 'width', (totalWidth + 1) + 'px');

           },

           showArrows : function() {
               var posWrap = dojo.position(this.containerWrapper);
               var posCon = dojo.position(this.containerNode);

               if (posCon.w < posWrap.w) {
                   dojo.style(this.prevNode, 'display', 'none');
                   dojo.style(this.nextNode, 'display', 'none');
               } else {
                   dojo.style(this.prevNode, 'display', '');
                   dojo.style(this.nextNode, 'display', '');
               }
           },

           _prev : function(event) {
               var left = dojo.style(this.containerNode, 'left');

               var posWrap = dojo.position(this.containerWrapper);
               var posCon = dojo.position(this.containerNode);

               var diff = posWrap.x - posCon.x;
               if (diff > this.shift) {
                   left += this.shift;
               } else {
                   left += diff;
               }
               this._slide(left);
           },

           _next : function(event) {
               var left = dojo.style(this.containerNode, 'left');

               var posWrap = dojo.position(this.containerWrapper);
               var posCon = dojo.position(this.containerNode);

               var diff = (posWrap.x + posWrap.w) - (posCon.x + posCon.w);
               if ((diff + this.shift) > 0) {
                   left += diff;
               } else {
                   left -= this.shift;
               }
               this._slide(left);
           },

           _slide : function(shift) {
               dojo.animateProperty({node: this.containerNode,
                   properties: {left: (shift)},
                   duration: this.duration}).play();
           },

           destroy : function() {
               this.inherited(arguments);
           }
        });

});