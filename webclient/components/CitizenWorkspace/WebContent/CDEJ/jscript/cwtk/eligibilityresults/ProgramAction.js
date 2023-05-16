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

       return declare("cwtk.eligibilityresults.ProgramAction", null, {
 
            execute : function(args) {
                var motivationID = args.motivationID;
                var programID = args.programID;
                var url = args.url;
                this.action(motivationID, programID, url);        
            },
        
            action: function(motivationID, programID, url) {
        
                var comboActionId = 'program-action-button-' + programID + '_button';  
                var buttonActionId = 'program-action-button-' + programID;            
                var node = dojo.byId(comboActionId);
                
                if(node == null) {
                    node = dojo.byId(buttonActionId);
                }
            
                cw.eligibilityResult.transitions.openEnrollment(node);        
                cw.fragment.load("background-panel", url, {motivationID:motivationID, programID:programID});
        
                cw.form.clearUpdateListeners();
                cw.form.addUpdateListener(function(){
                    cw.ajax.updateContent('cw-eligibility-results-context-bar')
                });
                cw.form.addUpdateListener(function(){
                    cw.ajax.updateContent('program-right-' + programID)
                });
            }  
          });

});

