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
    "dojox", 
    "dojox/xml/parser"
], function(dojo, declare, dojox){

		return declare("cwtk.util.AjaxHandler", null, {

			TIMEOUT: 0,

			call : function(link, parameters, callback) {
				console.log("Starting AJAX call for link " + link);
				var instance = this;
				dojo.xhrPost({
					url: link,
					content: parameters,
					timeout: instance.TIMEOUT,
					handle: function(dataOrError, args) {
							var doc = dojox.xml.parser.parse(dataOrError);
							var isError = dojo.query('error', doc).length > 0;
							if (isError){
								instance.handleError(dojo.query('error', doc)[0], args);
							} else {
								instance.handleData(dojo.query('data', doc)[0], args, callback);
							}
						 }
				});
			},
			
			handleError : function(err, args) {
				var message = "Error: " + dojox.xml.parser.textContent(dojo.query('message', err)[0]);
				console.log(message);
				alert(message);
			},
			
			handleData : function(data, args, callback) {
				if (callback) {
					callback(data, args);
				} else {
					console.log("No callback for data " + data);
				}
			},

			updateContent : function(contentPane, callback) {
			
				var contentPaneId = contentPane;
			    if (contentPane.id) {
			        contentPaneId = contentPane.id;
			    }
			    
				console.log("Updating view for " + contentPaneId);
				contentPane = dijit.registry.byId(contentPaneId);
				
				//destroying possible widgets to free up registry ID's
				dojo.forEach(dijit.findWidgets(contentPane), function(w) {
					console.log("destroying registered widget..." + w);
					w.destroyRecursive(true);
				});
				dojo.forEach(contentPane.getChildren(), function(w) {
					console.log("destroying registered widget..." + w);
					w.destroyRecursive(true);
				});            
					
				// GUM-3783
				// Ensure that preventCache=true on updates
				contentPane.preventCache = true;
				
				//replace loading message with current snapshot (plain html)       
				contentPane.loadingMessage =  contentPane.attr('content');
				contentPane.onDownloadEnd = callback;
				contentPane.onLoad = function() {
					dojo.animateProperty({node: dojo.byId(contentPane.id), 
									properties: {opacity: {start: 0.5, end: 1}}, duration: 1000}).play();
				};
				contentPane.refresh();
			}
		});

});