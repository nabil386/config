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
    "dojo/_base/lang", 
    "dojox/layout/ContentPane",
    "cwtk/util/UIMFragment"
], function(dojo, declare, lang, ContentPane, UIMFragment){

		return declare("cwtk.widget.FragmentPane", ContentPane, {
           uim: "", 
           parameters: null,
           isUimLoaded: false,
           
           startup : function() {
               this.inherited(arguments);
               this._onStart();
           },
           
           _onStart : function() {
               this.onStart();
               this.loadUim();
           },
           
           onStart : function() {
               //called after creation
           },
           
           loadUim: function() {
               if (this.uim && this.uim.length > 0 && !this.isUimLoaded) {
                   this.isUimLoaded = true;
                   var frag = new UIMFragment();
                   frag.load(this.id, this.uim, this.parameters);
               }
               
           },
           
           refreshUim: function() {
               this.isUimLoaded = false;
               this.loadUim();
           },
           
            
           destroy : function() {
               this.inherited(arguments);
           }
        });

});