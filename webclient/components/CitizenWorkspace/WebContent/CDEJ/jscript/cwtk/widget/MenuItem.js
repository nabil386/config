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
    "dijit/MenuItem", 
    "dojo/text!./templates/MenuItem.html"
], function(dojo, declare, MenuItem, template){

		return declare("cwtk.widget.MenuItem", MenuItem, {
           iconSrc: "", 
           templateString: template,
           startup : function() {
               this.inherited(arguments);
               dojo.connect(this, "onClick", this.closeParent);
           }, 
           destroy : function() {
               this.inherited(arguments);
           },
           closeParent : function() {
        	   dojo.query(this.domNode).parents(".dijitTooltipDialog, .dijitDialog").forEach(function(d) {
        		   dijit.popup.close(dijit.byId(d.id));
        	   });
           }
        });

});