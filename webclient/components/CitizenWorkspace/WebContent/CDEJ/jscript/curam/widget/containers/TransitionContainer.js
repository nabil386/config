define(["dojo/_base/declare",
        "dojo/parser",
        "dijit/_Widget",
        "dojo/dom-construct",
        "dojo/_base/window",
        "dijit/layout/ContentPane", 
        "dojo/dom-class",
        "dojo/_base/fx",
        "curam/util/cache/CacheLRU",
        "dojox/layout/ContentPane",
        "dojo/_base/array",
        "dojo/query"],
        function(declare,
                        parser,
        		_Widget,
        		domConstruct,
        		window,
        		ContentPane,
        		domClass,
        		fx,
        		CacheLRU,
        		ContentPaneX,
        		array,
        		query) {
	return declare("curam.widget.containers.TransitionContainer",[ContentPane], 
{
		transitionDuration: 200,
		_panelCache : null,
		_currentlyDisplayedPanelKey : -1,
		_panelToLoadKey : -1,
		_beenProcessed : false,
		
		constructor: function(args) {
			
	        var cacheMapArgs = 
	        	{
	        		maxSize : 5
	        	};
			this._panelCache = new CacheLRU(cacheMapArgs);			
	    },
	    
	    /**
	     * Function builds up the section of the the URL
	     * that contains the parameters
	     */
	    _buildPramUrl : function(jsonIn)
	    {
	    	var outPut = "";
	    	
	    	if(jsonIn.param != null)
	    	{	    		
	    		array.forEach(jsonIn.param, function(entry, i){
	    			if(i >0)
	    			{
	    				outPut +="&";
	    			}
	    			outPut += encodeURIComponent(entry.paramKey) + "=" + encodeURIComponent(entry.paramValue);
	    		});	    		
	    	}
	    	
	    	return outPut;
	    	
	    },
		
		_setDisplayPanelAttr : function(jsonIn)
		{
			
				jsonIn = this._doDataTranslation(jsonIn);
				
				
				var pramUrl = this._buildPramUrl(jsonIn);
				
				var compositeKey = jsonIn.key; // + pramUrl; fix from Martin to remove parameters as part of key.
				
				if(this._currentlyDisplayedPanelKey != compositeKey)
				{
					// touch the current displayed panel to keep it alive for 
					// transition out
					this._panelCache.getItem(this._currentlyDisplayedPanelKey);
					var panel = this._panelCache.getItem(compositeKey);
					
					if(panel == null)
					{
						// page does not exist  new one needs to be created
						
						var uri = this._doResourceLookUp(jsonIn, pramUrl, compositeKey);						

						uri = this._applyParamToUri(jsonIn, pramUrl, compositeKey, uri);
						
						var contentPane = new ContentPaneX({
							//content: jsonIn.key + " Hey I'm a panel "+new Date().getTime(),
							href : uri,
							preload : false, // set to false as the transition container decides when to load
							preventCache:true,
							executeScripts:true,  // runs embeded javascript
				                        scriptHasHooks:true,// merits further look but not stable in dojox layout
				                        refreshOnShow:false, // set to false as the transition container decides when to refresh
				                        open : false, // set so it does not fetch remote content when not visable
							style :{
								padding : 0,
								border : 0,
								opacity : 0
							}
						});//}).placeAt(window.body());
						
						contentPane = this._contentPaneCreated(jsonIn, pramUrl, compositeKey, contentPane);
	
						//style.set(contentPane.domNode, "opacity", "0");
						var fadeInArgs = {
						        node: contentPane.domNode,
						        duration: this.transitionDuration,
						        onEnd : dojo.hitch(this,this._panelFadeInComplete)
						      };
						
						var cbFadeOut = dojo.hitch(this,function(key)
						{
							this._panelFadedOut(key);
						});
						var fadeOutArgs = {
						        node: contentPane.domNode,
						        duration: this.transitionDuration,
						        onEnd : function(){		
						        	console.info('Fadding out onEnd Called for : '+ compositeKey);
						        	cbFadeOut(compositeKey);
						        }
						      };
						var fadeIn = fx.fadeIn(fadeInArgs);
						var fadeOut = fx.fadeOut(fadeOutArgs);
						
						
						panel = {
								panel : contentPane,
								fadeIn : fadeIn,
								fadeOut : fadeOut
						};
						
						var options = {
								callback : function(key, item)
								{
									try
									{
										item.panel.destroy();
										delete item;
									}	
							  		catch(err){
										console.error(err);
									}
								}
						};
						
						this._panelCache.addItem(compositeKey,panel,options);
						
						
					}
					else
					{
						//do nothing as panel all ready exists  
						console.info("Doning nothing as panel all ready exists");
                        // NOTE: temp workaround to ensure the content panes are always refreshed
                        // when this is called through the "displayContent" API.
                        // ExternalApplication.js sets "forceRefresh".
		                                  
		                                    // do nothing as all ready displayed
		                                    var panel = this._panelCache.getItem(compositeKey);
		                                    if (panel) {
		                                      var uri = this._doResourceLookUp(jsonIn, pramUrl, compositeKey);                                                
		                                      uri = this._applyParamToUri(jsonIn, pramUrl, compositeKey, uri);
		                                      // trigger refresh, applying new parameters.
		                                      panel.panel.open = false; // don't want a refresh when the href is set on the next line.
		                                      panel.panel.set("href", uri);
		                                    }
		                                  
						
					}
					
					this._doSwapPanel(jsonIn, compositeKey);
					
					
				}
				else
				{
                      // NOTE: Same workaround as above, except this time we do the lookups with "_currentlyDisplayedPanelKey"
			          
        		            // do nothing as all ready displayed
    				    var panel = this._panelCache.getItem(this._currentlyDisplayedPanelKey);
    				    if (panel) {
  				      var uri = this._doResourceLookUp(jsonIn, pramUrl, this._currentlyDisplayedPanelKey);                                                
                                      uri = this._applyParamToUri(jsonIn, pramUrl, compositeKey, uri);
                                      // trigger refresh, applying new parameters.
                                      panel.panel.set("href", uri);
  				    }
				  
				}
		},
		
		/**
		 * Function to be over writen that allows the 
		 * transalation of the data that of different format
		 * for loading resources 
		 */
		_doDataTranslation : function(jsonIn)
		{
			return jsonIn; 
		},
		
		/**
		 * Utility function to be over writen that allows custisation of the 
		 *  the contentPanes as they are created
		 */
		_contentPaneCreated : function(jsonIn, pramUrl, compositeKey, contentPane)
		{
			return contentPane;
		},
	    
	    /**
	     * Function to be over written to allow look up of
	     * reference identifer to URI
	     */
	    _doResourceLookUp : function(jsonIn, pramUrl, compositeKey)
	    {
	    	var uri = jsonIn.key;
	    	
	    	return uri;
	    },
	    
	    /**
	     * Function applys parameters to the requested URI
	     */
	    _applyParamToUri : function (jsonIn, pramUrl, compositeKey, uri)
	    {
	    	if(pramUrl.length >0)
	    	{
	    		if(uri.indexOf("?") != -1)
	    		{
	    			uri += "&";
	    		}
	    		else
	    		{
	    			uri += "?";
	    		}
	    		uri += pramUrl;
	    	}
	    	
	    	return uri;
	    },
	    
	    /**
	     * function called with the display panel has been faded
	     */
	    _panelFadedOut : function(panelFadedOutKey)
            {
                var container = this._panelCache.getItem(panelFadedOutKey);
                container.panel.cancel();

                if(container.panel.domNode != null)
                {
                        domClass.add(container.panel.domNode, "dijitHidden");
                }
                else 
                {
                        //im null on the fade out
                }
                container.panel.open = false;

                // if the ContentPane has an iframe within it, it will be
                // reloaded when the call to domConstruct.place is made below.
                // This is a quick fix to reset the iframe source to prevent
                // unnecessary reloads. In universal access, a reload can cause
                // harm because some of the pages do "auto" logons, clear the
                // user session etc. So, we don't want these pages to be
                // reloaded unintentionally.
                // TODO: this quick fix triggers a harmless "about:blank"
                // request. destroy the iframe "properly". 
                array.forEach(
                  query("iframe", container.panel.domNode), function(iframe) {
                    iframe.src="";
                });
                
                this._fadedOutPanelProcess(container);

                domConstruct.place(container.panel.domNode, window.body());
                this._panelFadeOutComplete();
                this._panelFadeIn();
            },

            /**
             * Utility function to be over written that allows
             * additional processing of a panel after it has been 
             * faded out 
             */
            _fadedOutPanelProcess : function(container)
            {

            },

            /**
             * Utility function to be over written 
             */
            _panelFadeOutComplete : function()
            {

            },
            
            /**
             * function fades in a panel to be displayed
             */
	    _panelFadeIn : function()
	    {
	    	if (this._panelToFadeInKey != -1)
	    	{
		    	var newPanel = this._panelCache.getItem(this._panelToFadeInKey);
		    	this.set('content', newPanel.panel);
		    	
		    	this._currentlyDisplayedPanelKey = this._panelToFadeInKey;
	        	//domConstruct.place(newPanel.panel.domNode, this.domNode);	
				
				
		    	if(newPanel.panel.domNode != null)
		    	{
		    		domClass.remove(newPanel.panel.domNode, "dijitHidden");
		    	}
		    	else 
		    	{
		    		//"im null on the fade IN
		    	}	    	
		    	
		    	newPanel.panel.onLoad = function()
				{	
					newPanel.fadeIn.play();			
				};
			newPanel.panel.open = true;
			newPanel.panel.refresh();
                        newPanel.panel.resize();
	    	}	    	
	    },
	    
	    /**
	     * Utility function to be over written 
	     */
	    _panelFadeInComplete : function()
	    {
	    	
	    },	
		
		/**
		 * Function swaps out the panel that is currently displayed
		 * for the new panel 
		 */
		_doSwapPanel : function(jsonIn, key)
		{

			var currentlyDisplayedPanel = this._panelCache.getItem(this._currentlyDisplayedPanelKey);
			
			if(currentlyDisplayedPanel != null)
			{
				this.fadeOutDisplay(key);			
			}
			else
			{
				this._panelToFadeInKey = key;
				this._panelFadeIn();
			}
			
		},
		
		fadeOutDisplay : function(key)
		{
			console.info("fadeOutDisplay");
			if(key == null)
			{
				key = -1;
			}			
			var currentlyDisplayedPanel = this._panelCache.getItem(this._currentlyDisplayedPanelKey);
			
			if(currentlyDisplayedPanel != null)
			{
				if(currentlyDisplayedPanel.fadeIn.status() == "playing"){
					console.info("fadeOutDisplay  - currentlyDisplayedPanel.fadeIn.status() == playing"  );
					currentlyDisplayedPanel.fadeIn.stop();
					currentlyDisplayedPanel.fadeOut.play();
				}
				else
				{
					if(currentlyDisplayedPanel.fadeOut.status() != "playing"){
						currentlyDisplayedPanel.fadeOut.play();
					}
				}

				this._panelToFadeInKey = key;
			}
			else
			{
				this._panelToFadeInKey = key;	
				this._panelFadeIn();
			}
			
		},
		
		/**
		 * Clean up functions
		 */
		destroy: function() {
			try
			{
				this._panelCache.destroy();			
			}	
	  		catch(err){
				console.error(err);
			}	  		
			this.inherited(arguments);
		}
		
	
	});
});
