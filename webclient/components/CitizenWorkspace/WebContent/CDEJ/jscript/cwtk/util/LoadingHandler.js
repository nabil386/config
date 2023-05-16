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
], function(dojo, declare, localDialog){

		return declare("cwtk.util.LoadingHandler", null, {
		
		    title: 'Loading',
		    
		    description: '',
		    
		    dialog: null,
		    
		    template: '<div class=\"cw-loading-pane-div\"><div class=\"cw-loading-pane-description\">{description}</div><div class=\"cw-loading-pane-image\"/></div>',

            getDialog : function() {
            
                if (this.dialog == null) {
                    this.dialog = new localDialog({
                        toggle: 'fade'
                    });
                    dojo.addClass(this.dialog.domNode, 
                        'cw-loading-pane-dialog');
                }
                return this.dialog;
            },
            
			/* object {title, description}*/
			show : function(obj) {
					
				var d = this.getDialog();
                var title = this.title;                     
                var description = this.description;
                
                if (obj) {
                    title = obj.title || title;
                    description = obj.description || description;
                }
                     
                d.set('title', title);
                d.set('content', this.template.replace("{description}", 
                    description));     
                  
                this.dialog.show();
				
			},
			
			hide : function() {
			    this.getDialog().hide();
			}
		});

});