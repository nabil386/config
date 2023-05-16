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
    "dojo/NodeList-traverse", 
    "dojo/fx"
], function(dojo, declare){

		return declare("cwtk.util.SlidingHandler", null, {

			swap:function(nodeA, nodeB) {
					
					var showNode = nodeA;
					var hideNode = nodeA;
					
					if(dojo.style(nodeA, 'display') === 'none') {
					    hideNode = nodeB;
					} else {
					    showNode = nodeB;
					}
					hideNode.style.display = "none";
					showNode.style.display = "";
					showNode.focus();
					dojo.query("a", showNode).forEach(function(n){
					    n.focus();
					});
					
				},
				
				slidePanel:function(panel) {
					
					var hidden = dojo.style(panel, 'display') === 'none';
					
					if(hidden){
						dojo.fx.wipeIn({node:panel, duration:300}).play();
					} else {
						dojo.fx.wipeOut({node:panel, duration:300}).play();
					}
				},
				slide:function(node) {
					
					var titlePane = dojo.query(node).parents(".cw-title-pane-query").first()[0];
					var showDiv = dojo.query(".cw-title-pane-show-div", titlePane)[0];
					var hideDiv = dojo.query(".cw-title-pane-hide-div", titlePane)[0];
					
					this.swap(showDiv, hideDiv);
					
					dojo.forEach(
						dojo.query(".cw-title-pane-display-panel", titlePane), 
						this.slidePanel);
				}
		});

});