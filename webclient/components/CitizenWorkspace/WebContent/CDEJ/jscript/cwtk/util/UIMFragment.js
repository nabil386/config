/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012-2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
define([
    "dojo", 
    "dojo/_base/declare",
    "dojo/_base/url",
    "curam/util/UIMFragment"
], function(dojo, declare, DojoUrl, curamUIMFragment){

		return declare("cwtk.util.UIMFragment", null, {
		
            /**
             *   Loads UIM content (fragment) onto panel.
             */
            load : function(paneID, pageID, params) {
            	console.debug("Loaded fragment: " + pageID);
                var self = this;
                self.get({
                    targetID : paneID,
                    pageID : pageID,
                    params : params,
                    onLoad : function(data) {
                        console.debug(data);
                    }
                });
            },
            
            /**
             *  CDEJ Implementation of loading content onto panel.
             *   
             *   @param JSON object:
             *       - pageID
             *       - targetID
             *       - url
             *       - params
             *       - onLoad
             *       - onDownloadError
             */
            get : function (obj) {
                return curamUIMFragment.get(obj);
            },
            
            /**
             *  Builds URL based on UIM ID.
             */
            constructURL : function (pageID) {
            	return curamUIMFragment._constructPath(pageID);
            }            
            
        });
});