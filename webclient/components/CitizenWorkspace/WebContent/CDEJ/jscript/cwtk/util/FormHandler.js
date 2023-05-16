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
    "dojo/_base/connect"
], function(dojo, declare, connect){

		return declare("cwtk.util.FormHandler", null, {
        
            _updateStack: new Array(),
            
            addUpdateListener: function(listener) {
                this._updateStack.push(listener);
            },
            
            clearUpdateListeners: function() {
                this._updateStack = new Array(); //clear stack for next execution
            },
            
            notifyUpdateListeners: function() {
                var listener = null;
                while (listener = this._updateStack.pop()) {
                    try {
                        listener();
                    } catch(e) {
                        console.log(e);
                    }
                }
                
            },
            
            /*
             * Load fragment and register next page form.
             * 
             */
            load : function(pane, pageID, params) {
                var cp = this._getContentPane(pane);
                dojo.addOnLoad(function(){
                    cw.fragment.get({
                        targetID : cp.id,
                        pageID : pageID,
                        params : params,
                        onLoad : function(data) {
                            console.log(data);
                        }
                    });
                });
            },
            
            /*
             * Dismiss function.
             * In case of background panel, return to page through transitions.
             * In case of modal dialog, close the window.
             * Otherwise, do nothing.
             */
            dismiss : function(node) {
                this._dismissNode(node);
                this.clearUpdateListeners();
            },
             
            dismissUpdate : function(node) {
                this._dismissNode(node);
                this.notifyUpdateListeners();
            }, 
                       
            _dismissNode : function(node) {
                if(node.domNode) {
                    node = node.domNode;
                }
                connect.publish("cw_dismiss", {node:node});
            },
            
            _getForm : function(node) {
                if (node.domNode) {
                    node = node.domNode;
                }
                return dojo.query(node).parents("form")[0];
            },
            
            _getContentPane : function(node) {
                var form = this._getForm(node);
                return dojo.query(form).parents(".dijitContentPane")[0];
            }
            
        });

});