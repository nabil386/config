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
    "dojo/_base/declare"
], function(dojo, declare){

		return declare("cwtk.util.EventHandler", null, {

			addEventListener : function (event, func) {
				dojo.subscribe(event, func);
			},
			
			dispatchEvent : function (event, args) {
				dojo.publish(event, [args]);
			},
			
			initService: function(service) {
				dojo.ready(service.init);
			},
			
			registerAction: function(action) {
				this.addEventListener(action.eventType, action.execute);
				
			}
		});

});