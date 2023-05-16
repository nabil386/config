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
    "dijit/Dialog"
], function(dojo, declare, Dialog){

		return declare("cwtk.eligibilityresults.CategoryAction", null, {
		
        execute : function(args) {
            
            this.action(args.url, args.title, args.motivationID, args.categoryID);
        
        },
        
        action: function(url, title, motivationID, categoryID) {
        
            cw.popup.show({title: title});
            cw.fragment.load(cw.popup.dialog().id, url, {motivationID:motivationID, categoryID:categoryID});
            cw.form.clearUpdateListeners();
            cw.form.addUpdateListener(function(){
                cw.ajax.updateContent('cw-eligibility-results-context-bar')
            });
            cw.form.addUpdateListener(function(){
                cw.ajax.updateContent('cw-eligibility-results-actions-'+ categoryID)
            });                                         
        }
		          
		});

});



