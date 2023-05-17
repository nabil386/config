/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
    "dojo/_base/declare", 
    "dojo/_base/lang", 
    "dojo/query",
    "dojo/dom-class",
    "dojo/on",
    "dojo/text!./templates/ContrastModeChooser.html",     
    "dijit/_WidgetBase", 
    "dijit/_TemplatedMixin"     
], function(declare, lang, query, domClass, on, template, _WidgetBase, _TemplatedMixin) {
	return declare("cwtk.widget.ContrastModeChooser", [_WidgetBase, _TemplatedMixin], {
		templateString : template,
		widgetsInTemplate : false,
		baseClass : "cw-contrast-mode-selector",		
		standardViewLinkText: null,
		highContrastViewLinkText: null,
		highContrastEnabled: false,
		standardViewLinkAttachPoint: null,
		highContrastViewLinkAttachPoint: null,
        
        postCreate: function(){
            if(this.highContrastEnabled) {                                
                domClass.add(this.highContrastViewLinkAttachPoint, "selectedContrastMode");   
            } else {
                domClass.add(this.standardViewLinkAttachPoint, "selectedContrastMode");
            }
            
            on(this.highContrastViewLinkAttachPoint, "click", 
                dojo.hitch(this,                 
                    function()
                    {
                        this.setHighContrastEnabled(true);
                    })
            );  

            on(this.standardViewLinkAttachPoint, "click", 
                dojo.hitch(this,                 
                    function()
                    {
                        this.setHighContrastEnabled(false);
                    })
            );  
                                    
        },
        
        setHighContrastEnabled: function(enabled) {
                dojo.xhrGet(
                    {
                        url: "servlet/ContrastChange?hcEnabled=" + enabled,
                        load: function(res) {
                            location.reload();
                        }
                    }
                );            
        }
                        		
	});
});
