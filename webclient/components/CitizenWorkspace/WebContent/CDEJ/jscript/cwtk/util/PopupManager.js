/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012, 2016. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
    "dojo", 
    "dojo/_base/declare",
    "dijit/focus",
    "dojo/_base/connect",
    "dijit/Dialog"
], function(dojo, declare, focus, connect, localDialog){

		return declare("cwtk.util.PopupManager", null, {

			_dialog : null,
			
			_title : '',
			
			_content : '<br/>',
			
			constructor: function() {
			    connect.subscribe("cw_dismiss", dojo.hitch(this, this.onDismiss));
			},
			
			dialog : function () {
			    if (!this._dialog) {
			        this._dialog = dijit.byId('cw-dialog');
			        if (!this._dialog) {
    			        this._dialog = this.createDialog();
			        }
			    }
			    return this._dialog;
			},
			
			createDialog : function () {
			    var dialog = new localDialog({
			        id: 'cw-dialog',
			        toggle: 'fade'
                });
                dojo.connect(dialog, 'onDownloadEnd', function(){
                	/*BEGIN, 177993, RS */
                    dialog.resize();
                    /*END, 177993 */
                    dialog._getFocusItems(dialog.domNode);
					focus.focus(dialog._firstFocusItem);
                });
                return dialog;  
			},
			
			setArgs : function (args) {
			    if (args) {
    		        args.title = args.title || this._title;                     
                    	dojo.attr(this.dialog(), args);
			    }
			},
			
			show : function (args) {
			    this.setArgs(args);
			    this.dialog().show();  
			},
			
			showNew : function (args) {
			    if (this._dialog) {
			        this._dialog.destroy();
			        delete this._dialog;
			    }
			    this._dialog = this.createDialog();
			    this.show(args);  
			},
			
			hide : function () {
			    this.dialog().hide();
			},
			
			onDismiss : function(event) {
			    var dialogQuery = dojo.query(event.node).parents(".dijitDialog");
                if (dialogQuery.length > 0) {
                    var dialog = dijit.byId(dialogQuery[0].id);
                    dialog.hide();
                }
			}
			
		});

});