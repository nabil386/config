/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012,2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/* Modifications
 * =============
 * Upgrade code for Dojo 1.7
 *
 */
dojo.provide("cef.TitlePane");

require(["dijit/TitlePane"]);

dojo.declare("cef.TitlePane", dijit.TitlePane, {
	
		_currentOpenCloseClass:'',
		newId:'',
		navigationContent:'',

		postCreate: function(){
			this.inherited(arguments);
			

			var myTitleBar = this.titleBarNode;
			dojo.attr(myTitleBar,"class", "evidenceTitlePane");
			this.newId = 'div_'+ this.id;

			arrowImageSpan = dojo.create("span", {id: this.newId});
			dojo.addClass(arrowImageSpan, 'arrowImageSpanClass');
			
			dojo.place(arrowImageSpan, this.titleNode, 'before');
			dojo.place(this.navigationContent, this.titleNode, 'after');
			
			arrowImg = dojo.create('img', {
	            id: 'img_' + this.id
	        }, arrowImageSpan);
			
			var args = [this];
			dojo.connect(arrowImg, "onclick", args, this.startToggle);
			
			if(this._currentOpenCloseClass == 'EvidenceDashboardOpen') {
				dojo.attr(arrowImg, 'src', '../Images/arrow_toggle_expand.png');
			} else {
				dojo.attr(arrowImg, 'src', '../Images/arrow_toggle_mini.png');
			}
			
			var myTextNode = this.titleNode;
			dojo.attr(myTextNode,"class", "evidenceTitlePaneTextNode");
			
			var myHideNode = this.hideNode;
			dojo.attr(myHideNode,"class", "noBorderTitlePaneContentOuter");
			
		},
			
		_updateArrowCSS: function() {
				if (dojo.byId('img_'+this.id) != null) {
					if(this._currentOpenCloseClass == 'EvidenceDashboardOpen') {
						dojo.attr(dojo.byId('img_'+this.id), 'src', '../Images/arrow_toggle_expand.png');
					} else {
						dojo.attr(dojo.byId('img_'+this.id), 'src', '../Images/arrow_toggle_mini.png');
					}
				}
		},
			

		_setCss: function(){
			// summary:
			//		Set the open/close css state for the TitlePane.
			// tags:
			//		private

			var node = this.titleBarNode || this.focusNode;

			if(this._titleBarClass){
				dojo.removeClass(node, this._titleBarClass);
			}
			this._titleBarClass = "EvidenceDashboard" + (this.toggleable ? "" : "Fixed") + (this.open ? "Open" : "Closed");
			this._currentOpenCloseClass = this._titleBarClass;
			this._updateArrowCSS();
			this.arrowNodeInner.innerHTML = this.open ? "-" : "+";
		},
		
		_onTitleEnter: function(/*Boolean*/ e){
			//do nothing, this is the remove the style.
		},
		
		_onTitleClick: function(){
			// summary:
			//		Handler when user clicks the title bar
			// tags:
			//		private
			//do nothing remove toggle for title bar.

		},
		
		startToggle: function(args) {
			if(this[0].toggleable){ 
				this[0].toggle();
			}
		}
});
