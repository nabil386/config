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
    "dojo/NodeList-traverse", 
    "dojo/fx", 
    "cwtk/eligibilityresults/CategoryAction",
    "cwtk/eligibilityresults/ProgramAction",
    "cwtk/eligibilityresults/Transitions"
], function(dojo, declare, traverse, fx, CategoryAction, ProgramAction, Transitions){

		return declare("cwtk.eligibilityresults.EligibilityResult", null, {
    
		    transitions: new Transitions(),
		    
		    // Events
		    programAction : new ProgramAction(),
		    categoryAction: new CategoryAction(),
		    
		    _upfrontLoadingCount : 0,
		
			showHideDetails:function(node) {
				
				var titlePane = dojo.query(node).parents(".cw-program").first()[0];
				var hidden = dojo.hasClass(titlePane, 'cw-collapsed');
    			
    			var focusOn = function() {
                    dojo.query('.cw-program-title-show-hide a', titlePane).forEach(function(n){
                        if (n != node) {
                            n.focus();
                        }
                    });
    			}
				
				dojo.forEach(
				    dojo.query(".cw-program-details", titlePane), 
				    function(panel){
					
    					if(hidden){
    					    dojo.style(panel, 'display', 'none');//workaround for smooth transition
				            dojo.addClass(titlePane, 'cw-expanded');
            				dojo.removeClass(titlePane, 'cw-collapsed');
    						dojo.fx.wipeIn({node:panel, duration:300}).play();
    						focusOn();
    					} else {
    						dojo.fx.wipeOut({node:panel, duration:300, onEnd: function(){
    						    dojo.addClass(titlePane, 'cw-collapsed');
		                        dojo.removeClass(titlePane, 'cw-expanded');
		                        focusOn();
    						}}).play();
    					}
				    });
			},
		
			programMenuAction: function(motivationID, programID, url) {
			    this.programAction.execute({programID:programID, url:url, motivationID:motivationID});
			},
			
			categoryMenuAction: function(motivationID, categoryID, url, title) {
			    this.categoryAction.execute({url:url, title:title, motivationID:motivationID, categoryID:categoryID});
			},
			
			upfrontLoading: function(pane) {
			     
			    var that = this;
			    var layout = dojo.byId('eligibility-main-layout');
			    
			    var handleStart = function(ev) {
    		        that._upfrontLoadingCount++;
    		        that._createOverlay(layout);
			    };
			    var handleStop = function(ev) {
			        that._upfrontLoadingCount--;
			        if (that._upfrontLoadingCount <= 0) {
			            dojo.style(that._overlay(layout), 'display', 'none');
			            setTimeout(function(){
			                dojo.destroy(that._overlay(layout))
		                }, 1000);
			        }
			    };
			    
		        var cp = dijit.byId(pane);
		        dojo.connect(cp, 'onDownloadStart', handleStart);
		        dojo.connect(cp, 'onDownloadEnd', handleStop);
		        dojo.connect(cp, 'onDownloadError', handleStop);
			},
			
			_overlay : function(layout) {
			    return dojo.byId(layout.id + '_overlay');
			},
			
			_createOverlay : function(layout) {
			    return dojo.create('div', {
    			        id : layout.id + '_overlay',
    			        innerHTML : "<span class='dijitContentPaneLoading'><span class='dijitInline dijitIconLoading'></span></span>",
    			        style: {position: 'absolute', width: '100%', height: '100%', background: 'white', zIndex: '999'}
    		        }, layout);
			}
				
		});

});
