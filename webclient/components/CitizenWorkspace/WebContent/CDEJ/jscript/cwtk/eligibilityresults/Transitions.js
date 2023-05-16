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
	"dojo/_base/connect",
    "dojo/NodeList-traverse",
    "dijit/a11y", 
    "dijit/focus", 
    "dojo/fx", 
	"dojo/fx/easing"
], function(dojo, declare, connect, traverse, a11y, focus, coreFx, easing){

		return declare("cwtk.eligibilityresults.Transitions", null, {
		
		    isOpen : false,
		    
		    _tempEventHandle : null,
		
		    constructor: function() {
			    connect.subscribe("cw_dismiss", dojo.hitch(this, this.onDismiss));
			},
		    
		    invokeEnrollment: function(node) {
				
				if (dojo.hasClass('eligibility-main-layout', 'cw-open-enrollment')) {
					this.closeEnrollment(node);
				} else {
					this.openEnrollment(node);
				}
				
				
			},
			
			openEnrollment: function(node) {
				var step1Duration = 1000;
				var step2Duration = 1000;
				var step3Duration = 1000;
				
				var layout = dojo.byId('eligibility-main-layout');
				var triage = dojo.byId('triage');		
				var content = dojo.byId('eligibility-content');
				var centralPanel = dojo.byId('background-panel');
				var category = dojo.query(node).parents(".cw-category")[0];
				var program = dojo.query(node).parents(".cw-program")[0];
		        var description = dojo.query('.cw-program-description', program)[0];		

				dojo.addClass(layout, 'cw-open-enrollment');
		        dojo.addClass(program, 'cw-open');
		        dojo.addClass(category, 'cw-open');	
		        dojo.addClass(centralPanel, "cw-open");
		        dojo.style(centralPanel, {'top': '0', 'bottom' : '0'});
				
				var contentShift = this.getShiftWidth(content);  
		        
				/*
				 * Step 1: move panes
				 */
				
				try {
    				//stretch content pane
    				dojo.animateProperty({node: content, 
    		                	    	properties: {right: 0}, 
		                    	    	duration: step1Duration}).play();
										
					// fade infomationals out
					var informationals = dojo.byId('informational-content');
					dojo.style(informationals, {'display': 'none'});
                } catch(e){
                    console.log(e);
                }	       
                
                try {         	    	
    		        //move program pane to right (to keep same position)        	    	
    				dojo.animateProperty({node: program, 
		                	    	    properties: {left: contentShift}, 
		                	    	    duration: step1Duration}).play();
                } catch(e) {
                    console.log(e);
                }
		             
                try {   	    	
    		        //move category panes to left        	    	
    		        dojo.query('.cw-category').forEach(function(cat){
    		    		dojo.animateProperty({node: cat, 
    		                	    	properties: {left: (-1 * contentShift)}, 
    		                	    	duration: step1Duration}).play();
    		        });
		        } catch(e) {
		            console.log(e);
	            }
	            
	            try {
    		        //move triage to right 
    		        var triageShift = this.getShiftWidth(triage);
    		        if (triage) {       	    	
    		    		dojo.animateProperty({node: triage, 
    		                	    	properties: {right: (-1 * triageShift)}, 
    		                	    	duration: step1Duration}).play();
    		        }
		        } catch(e) {
		            console.log(e);
	            }
	                    	    	
		        //fade others out
		        dojo.query(
		            '.cw-program-title-show-hide, ' + 
		            '.cw-title-pane-title, ' + 
		            '.cw-program:not(.cw-open), ' + 
		            '.cw-triage, ' + 
		            '.cw-program.cw-open .cw-action-button, ' + 
		            '.cw-program.cw-open .cw-program-description').forEach(function(n){
		        		dojo.animateProperty({node: n, 
		                    properties: {opacity: 0}, 
		                    duration: step1Duration / 2, onEnd: function(){
		                        dojo.style(n, 'visibility', 'hidden');
		                    }}).play();
		        });        	   
		        
				
				/*
				 * Step 2: move program pane up
				 */
				 
				var posCon = dojo.position(content, true);
			
			    //wait panes to move
				dojo.animateProperty({node: program, delay: step1Duration, 
		    		onEnd: function() {
		    			var posPro = dojo.position(program, true);
		    			var posBack = dojo.position(centralPanel, true);
		    			
		    			var offsetProgram = (posPro.y - posCon.y) * -1;
		    			
                        console.log("[Transitions] Offset program panel: " + offsetProgram);
		    			//move program pane up
		    			var animProgram = dojo.animateProperty({node: program, 
		                	    	properties: {top: offsetProgram}, 
		                	    	duration: step2Duration});
		                	    	
		                //contract program description pane    
		    	        var animDesc = dojo.animateProperty({node: description, 
		        	    	properties: {height: 0}, 
		        	    	duration: step2Duration});
		        	    	
		        	    //wait both animations to complete
		        	    var combAnim = coreFx.combine([animProgram, animDesc]);
		        	    combAnim.onEnd = function(){
                    	    var posPro = dojo.position(program, true);
                    	    var posLayout = dojo.position(layout, true);
                    	    var offsetBackground = (posPro.y - posLayout.y) + posPro.h;
                            console.log("[Transitions] Offset background panel: " + offsetBackground);
                    	    dojo.style(centralPanel, {'top': offsetBackground + 'px'});
                    	};
                    	combAnim.play();
		    		}}).play();
				
			    	
			    /*
				 * Step 3: fade central pane in
				 */	
			    	
		        //move program pane up
		    	dojo.animateProperty({node: centralPanel, 
		            delay: step1Duration + step2Duration, 
		            onEnd: function() {
 
		                //fade background pane in
		                dojo.style(centralPanel, {'opacity' : '0'});
		                dojo.style(centralPanel, {'display': 'block'});
		                dojo.style(centralPanel, {'width': '97%'});//TODO
		                dojo.animateProperty({node: centralPanel, 
		                    properties: {opacity: 1}, 
		                    duration: step3Duration}).play();
		                //assure layout size (BorderContainer.resize() gets called sometimes)    
		                dojo.addClass(layout, 'cw-enrollment-expanded');
		                var focusables = a11y._getTabNavigable(centralPanel);
		                focus.focus(focusables.lowest || focusables.first || centralPanel.domNode);    
		            }}).play();    	
					
				this.isOpen = true;
				
				this._tempEventHandle = dojo.connect(
				    dijit.byId('eligibility-main-layout'), 
				    "resize", 
				    dojo.hitch(this, function(ev){ 
                       this.onResize(ev);
                    }));

			},
			
			closeEnrollment: function(node) {
		
				var step1Duration = 1000;
				var step2Duration = 1000;
				var step3Duration = 1000;
				
				var layout = dojo.byId('eligibility-main-layout');		
				var triage = dojo.byId('triage');		
				var content = dojo.byId('eligibility-content');
				var centralPanel = dojo.byId('background-panel');
				var category = dojo.query(node).parents(".cw-category")[0];
				var program = dojo.query(node).parents("#programs-div")[0];
				var description = dojo.query('.cw-program-description', program)[0];
				
				//close
		        //EligibilityResultTransitions.briefClass(layout, 'cw-close-enrollment-event', overallDuration);	
				
				//re-determine layout size (if BorderContainer.resize() has been called)
				dojo.style(content, 'right', '0');
				dojo.removeClass(layout, 'cw-enrollment-expanded');
				if (triage) {
				    var triageShift = this.getShiftWidth(triage);
				    dojo.style(triage, 'right', (-1 * triageShift) +'px');
				}
				
				/*
				 * Step 1: fade central pane out
				 */		
				dojo.animateProperty({node: centralPanel, properties: {opacity: 0}, duration: step1Duration, onEnd: function() {
				    dojo.style(centralPanel, {'display': 'none'});
				    
				    //dojo.style(content, 'right', '0');
		            //dojo.removeClass(content, 'cw-open');
				}}).play();
				
				/*
				 * Step 2: move program pane down
				 */	
		    	dojo.fx.wipeIn({node: description, 
		    	    delay: step1Duration, 
			        duration: step2Duration}).play();
		    	
				dojo.animateProperty({node: program, 
			        properties: {top: 0}, 
			        delay: step1Duration, 
			        duration: step2Duration}).play();
		        
		        /*
				 * Step 3: move others back on
				 */
		        dojo.animateProperty({node: program, 
		    	    delay: step1Duration + step2Duration, 
			        onEnd: function(){
			        
		            		//move program pane to right (to keep same position)        	    	
		            		dojo.animateProperty({node: program, 
		                            	    	properties: {left: 0}, 
		                            	    	duration: step3Duration}).play();
		                            	    	
		                    //move category panes to left        	    	
		                    dojo.query('.cw-category').forEach(function(cat){
		                		dojo.animateProperty({node: cat, 
		                            	    	properties: {left: 0}, 
		                            	    	duration: step3Duration}).play();
		                    });
		                    
		                    //stretch content pane
		                    if (triage) {
		                        var posTriage = dojo.position(triage, true);
		                    
		                    	dojo.animateProperty({node: content, 
		                                        	properties: {right: posTriage.w}, 
		                                        	duration: step3Duration}).play();
		                        
		                        //move triage to right        	    	
		                    	dojo.animateProperty({node: triage, 
		                                        	properties: {right: 0}, 
		                                        	duration: step3Duration}).play();
		                    }

                        // fade informationals in. Note that informationals are optional.
                        var informationals = dojo.byId('informational-content');
                        if (informationals) {
                        dojo.style(informationals, {'display': 'block'});
                        }							

		                    //fade others in
		                    dojo.query(
		                        '.cw-program-title-show-hide, ' + 
		                        '.cw-program:not(.cw-open), ' + 
		                        '.cw-program.cw-open, ' +
		                        '.cw-program.cw-open .cw-program-description, ' + 
		                        '.cw-title-pane-title, ' + 
		                        '.cw-triage').forEach(function(n){
		                            dojo.style(n, 'visibility', 'visible');
		                            dojo.animateProperty({node: n, 
		                                properties: {opacity: 1},
		                                duration: step3Duration}).play();
		                    }); 
			        
			        }}).play();        	
				
		            dojo.animateProperty({node:layout, 
		                delay: step1Duration + step2Duration + step3Duration, 
		                onEnd: function(){
		                    dojo.removeClass(layout, 'cw-open-enrollment');
		                    dojo.removeClass(program, 'cw-open');
		                    dojo.removeClass(centralPanel, "cw-open");
		                    dojo.removeClass(category, 'cw-open');
		            }}).play();
		            
		            // add back the Enrollment that was cancelled
		            dojo.query('.cw-program.cw-expanded.cw-open').forEach(function(n){
		                            dojo.style(n, 'visibility', 'visible');
		                            dojo.style(n, 'top', '');
		                            dojo.style(n, 'left', '');		                            
		                            dojo.removeClass(n, "cw-open");
		                            dojo.animateProperty({node: n, 
		                                properties: {opacity: 1},
		                                duration: step3Duration}).play();
		                    });		            
		            
		            this.isOpen = false;
		            
		            dojo.disconnect(this._tempEventHandle);
		            this._tempEventHandle = null;
					
					
			},
			
			briefClass : function(node, styleClass, duration) {
			    
			    dojo.addClass(node, styleClass);
			    
			    dojo.animateProperty({node: node, duration: duration, onEnd: function() {
		    		dojo.removeClass(node, styleClass);
				}}).play();
			},
			
			invokeExpandedMap: function() {
			    var layout = dojo.byId('eligibility-main-layout');
			    
		        this.briefClass(layout, 'cw-map-event', 1000);
			    if (dojo.hasClass(layout, 'cw-extended-map')) {
		    	    //EligibilityResultTransitions.briefClass(layout, 'cw-standard-map-event', 1000);
			    } else {
		    	    //EligibilityResultTransitions.briefClass(layout, 'cw-extended-map-event', 1000);
			    }
			    
				dojo.removeClass(layout, 'cw-collapsed-map');
		        dojo.toggleClass(layout, 'cw-extended-map');
			},
			
			invokeCollapsedMap: function() {
			    var layout = dojo.byId('eligibility-main-layout');
			    
		        this.briefClass(layout, 'cw-map-event', 1000);
			    if (dojo.hasClass(layout, 'cw-collapsed-map')) {
		    	    //EligibilityResultTransitions.briefClass(layout, 'cw-standard-map-event', 1000);
			    } else {
		    	    //EligibilityResultTransitions.briefClass(layout, 'cw-collapsed-map-event', 1000);
			    }
			    
			    dojo.removeClass(layout, 'cw-extended-map');
		        dojo.toggleClass(layout, 'cw-collapsed-map');
			},
			
			getShiftWidth: function(node) {
			    var shift = dojo.position(node, true).w;
    			if (screen) {
			        shift = Math.max(screen.width, shift);
    			} 
    			return shift + 100;
			},
			
			onDismiss: function(event) {
			    var bgQuery = dojo.query(event.node).parents(".cw-background-panel");
                if (bgQuery.length > 0 || dojo.hasClass(event.node, "cw-background-panel")) {
                    var bgpanel = bgQuery[0];
                   // var actionButton = dojo.query(".cw-program.cw-open .cw-action-button")[0];
                   var actionButton = dojo.query(".cw-program.cw-open")[0];
                    this.closeEnrollment(actionButton);
                }
			},
			
			onResize: function(event) {
			    if (this.isOpen) {
    			    var layout = dojo.byId('eligibility-main-layout');
    			    var program = dojo.query('.cw-program.cw-open')[0];
    			    var posPro = dojo.position(program, true);
                    var posLayout = dojo.position(layout, true);
                    var offsetBackground = (posPro.y - posLayout.y) + posPro.h;
                    console.log("[Transitions] Resized screen, offset background panel: " + offsetBackground);
                    var centralPanel = dojo.byId('background-panel');
                    dojo.style(centralPanel, {'top': offsetBackground + 'px', 'bottom' : '0'});
			    }
			}
		          
		});

});