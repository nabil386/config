/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
    "dojo/_base/declare", 
    "dojo/_base/lang", 
    "dojo/query", 
    "dijit/layout/ContentPane", 
    "dijit/_TemplatedMixin", 
    "dijit/_WidgetsInTemplateMixin", 
    "dojo/text!./templates/TitlePane.html", 
    "dojo/fx/easing"
], function(declare, lang, query, ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin, template) {
	return declare("cwtk.widget.TitlePane", [ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin], {

		templateString : template,

		widgetsInTemplate : true,

		baseClass : "cw-title-pane",

		_titleNode : null,

		_showButtonDiv : null,
		_hideButtonDiv : null,

		_helpNode : null,
		_helpPanel : null,
		_helpDialog : null,

		_actionsNode : null,
		_actionsPanel : null,
		_actionsDialog : null,

        /* Properties */
		isPrimary : false,
		
		/* Labels */
		title : 'Title Pane',
		labelActions : 'Actions',
		labelHelp : 'Help',
		labelHide : 'Hide',
		labelShow : 'Show',
		
		/* Content */
		hrefActions : '',
		hrefHelp : '',

		/**
		 * Overridden to mixin the override resources if provided.
		 */
		postMixInProperties : function() {
			this.inherited(arguments);
		},

		/**
		 * Override to force startup/layout on BorderContainer.
		 */
		startup : function() {
			this.inherited(arguments);
			this._init();
		},

		/**
		 *
		 */
		_init : function() {
			console.log('init title pane');
		},

		_swap : function(nodeA, nodeB) {

			if (dojo.style(nodeA, 'display') === 'none') {
				nodeA.style.display = "";
				nodeB.style.display = "none";
			} else {
				nodeA.style.display = "none";
				nodeB.style.display = "";
			}

		},

		_slidePanel : function(panel) {

			var hidden = dojo.style(panel, 'display') === 'none';

			if (hidden) {
				dojo.fx.wipeIn({
					node : panel,
					duration : 300,
					easing : dojo.fx.easing.linear
				}).play();
			} else {
				dojo.fx.wipeOut({
					node : panel,
					duration : 300,
					easing : dojo.fx.easing.linear
				}).play();
			}
		},

		_slide : function() {

			this._swap(this._showButtonDiv, this._hideButtonDiv);

			this._slidePanel(this.containerNode);
		},

		_slideEvent : function(event) {
			this._slide();
		},

		_helpEvent : function(event) {
			dijit.byId(this._helpDialog).show();
		},
		
		refreshActions : function() {
		    if (this._actionPanel) {
		        dijit.byId(this._actionPanel.id).refresh();
		    }
		}
	});
});
