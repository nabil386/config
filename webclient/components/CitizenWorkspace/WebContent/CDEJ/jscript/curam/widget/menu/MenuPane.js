define([ "dojo/_base/declare", 
         "dojo/_base/lang", 
         "dojo/on", 
         "dojo/dom-class",
         "curam/widget/componentWrappers/ListWraper",
         "curam/widget/form/ToggleButtonGroup",
         "dojo/_base/window",
         "dojo/dom-construct",
         "dijit/TooltipDialog",
         "dijit/popup",
         "dojo/_base/fx",
         "dojo/dom-style",
         "dojox/layout/ExpandoPane",
         "dojo/dom-geometry",
         "dojo/aspect",
         "dojo/keys",
         "dijit/Tooltip",
         "idx/widget/HoverCard",
         "dojo/query",
         "dojo/dom-style",
         "dojo/has",
         "dojo/dom-attr"], 
         function(declare,
                         lang, 
                         on, 
                         domClass,
                         listWraper, 
                         toggleButtonGroup,
                         window,
                         domConstruct,
                         tooltipDialog,
                         popup,
                         fx,
                         style,
                         expandoPane,
                         domGeom,
                         aspect,
                         keys,
                         Tooltip,
                         HoverCard,
                         query,
                         domStyle,
                         has,
                         domAttr) {

        var _MenuPaneButtonIndexer = declare("curam.widget.menu._MenuPaneButtonIndexer", null, {

                selectedButtonKey : -1,  // the id of the selected button
                selectedButtonDisplayIndex : -1,
                expandButtonDisplayIndex : -1, // index that the expand button is set at
                
                
                _buttonDisplayOrderArrayOrginale : null,  // keeps track of the order of the buttons been displayed
                _buttonMap : null, // map containing the buttons and there releated info index by the button ID
                _subPagenMap : null, // map containing the mapping between sub pages and there button key = sub page id value = button id                                       
                
                _buttonPrimaryContainerArray : null,
                _buttonSecondaryContainerArray : null,
                
                
                constructor: function(args) {
                        this._buttonMap = [];
                        this._subPagenMap = [];
                        this._buttonDisplayOrderArrayOrginale = new Array();
                        this._buttonPrimaryContainerArray = new Array();
                        this._buttonSecondaryContainerArray = new Array();
                        
            },
                
                addNewButton : function(button, key)
                {
                        var holder = {
                                        key : key,
                                        id : button.id,
                                        button : button,
                                        contextBox : null,
                                        displayOrderIndex : null,
                                        displayOrderOrginaleIndex : this._buttonDisplayOrderArrayOrginale.length
                        };                      
                        this._buttonMap[key] = holder;  
                        this._buttonDisplayOrderArrayOrginale.push(key);
                },
                
                addButtonReferenceToPrimaryContainer : function (key, primaryContainer)
                {
                        if(primaryContainer)
                        {
                                this._buttonPrimaryContainerArray.push(key);
                        }
                        else
                        {
                                this._buttonSecondaryContainerArray.push(key);
                        }
                },
                
                getButton : function(key)
                {
                        var buttonItem = this._buttonMap[key];
                        
                        return buttonItem;
                },
                
                /**
                 * Function adds index's to the sub page / primary page 
                 * mapping
                 * 
                 * @ subPageID the id of the sub page
                 * @ primaryPageID id of the primary page for this sub page
                 */
                setNewSubPage : function(subPageID, primaryPageID)
                {               
                        this._subPagenMap[subPageID] = primaryPageID;   
                },
                
                /**
                 * Function gets primary page of a sub page
                 * @param subPageID The id of the sub page been processed
                 * @returns
                 */
                getSubPagePrimaryPage : function(subPageID)
                {
                        var primaryPageID = this._subPagenMap[subPageID];
                        
                        return primaryPageID;
                },              
                
                getButtonPrimary : function(index)
                {
                        var key = this._buttonPrimaryContainerArray[index];
                        
                        var buttonItem = this.getButton(key);
                        
                        return buttonItem;
                },
                
                getButtonSecondary : function(index)
                {
                        var key = this._buttonSecondaryContainerArray[index];
                        
                        var buttonItem = this.getButton(key);
                        
                        return buttonItem;
                },
                
                swapButtonFomPrimaryContainerToSecondaryContainer : function(fromPrimaryToSecondary)
                {
                        if(fromPrimaryToSecondary)
                        {
                                var item = this._buttonPrimaryContainerArray.pop();
                                this._buttonSecondaryContainerArray.unshift(item);
                        }
                        else
                        {
                                var item = this._buttonSecondaryContainerArray.shift();
                                this._buttonPrimaryContainerArray.push(item);
                        }
                        
                },
                
                swapButtonContainerToContainer : function(fromPrimary, sourceIndex, targetIndex)
                {
                        if(fromPrimary)
                        {
                                var item = this._buttonPrimaryContainerArray.splice(sourceIndex,1);
                                this._buttonSecondaryContainerArray.splice(targetIndex,0,item[0]);
                        }
                        else
                        {
                                var item = this._buttonSecondaryContainerArray.splice(sourceIndex,1);
                                this._buttonPrimaryContainerArray.splice(targetIndex,0,item[0]);
                        }
                        
                },
                
                swapButtonContainerItemIndex : function(primary, sourceIndex, targetIndex)
                {
                        if(primary)
                        {
                                var item = this._buttonPrimaryContainerArray.splice(sourceIndex,1);
                                this._buttonPrimaryContainerArray.splice(targetIndex,0,item[0]);
                        }
                        else
                        {
                                var item = this._buttonSecondaryContainerArray.splice(sourceIndex,1);
                                this._buttonSecondaryContainerArray.splice(targetIndex,0,item[0]);
                        }
                        
                },
                
                /**
                 * function will return what container the button is
                 * currently stored in from it's orginale index
                 *  
                 * @param index is a number which represents the originale placement of a button 
                 * @returns {Number}  0 for primary container 1 for secondary container
                 */
                getWhichContinerFromIndex : function(index)
                {                       
                        var outPut = 0;                 
                        if( index >= this._buttonPrimaryContainerArray.length   )
                        {
                                // contained in the secondary container
                                outPut = 1;
                        }
                        
                        return outPut;
                }

        });     
        
        
        return declare("curam.widget.menu.MenuPane", [ expandoPane ], 
{
                _listWrapper : null,
                _expandButton : null, // button that toggles the pop up menu
                _expandButtonContentBox : null, // contains the size of the expand button
                _toolTipDialogExpand : null,
                _toolTipDialogExpandContents : null,
                _fadeIn : null,
                _fadeOut : null,
                _menuPaneButtonIndexer : null,
                duration : 300,  // time taken for fade in and fade out
                _buttonSizerDiv : null,
                _buttonSizerList : null,
                _resizeResizeHandler : null,
                _showEndresizeResizeHandler : null,
                _hideEndResizeHandler : null,
                resizeDelay : 250,  // time for the resize delay if set to 0 or less there is no delay
                _resizeDelayHandler : null,
                _previouseHeight : -1, // records the previouse hight, to pervent false resizes
                _resizeStatusQue : 1,  // resize is qued up
                _resizeStatusResizeing : 0,  // ressize is taking place
                _resizeStatusNotInUse : -1, // no resize under way
                _resizeCurentStatus : -1, // holds state bewteen resize delays
                
                // defines styles for the position on buttons in there OL
                _classNavMenu : "navMenu",
                _classNavMenuOverFlow : "navMenuOverFlow",
                
                // defines the class for the main menu pain
                _classCurramSideMenuButton : "curramSideMenuButton",
                _classCurramSideMenuButtonIcon : "curramSideMenuButtonIcon",
                
                // defines the styles for the over flow menu
                _classCurramSideMenuOverFlowButton : "curramSideMenuOverFlowButton",
                _classCurramSideMenuOverFlowButtonIcon : "curramSideMenuOverFlowButtonIcon",
                
                // defines the styles for the expand button that pops the over flow menu
                _classCurramSideMenuOverFlowButtonExpand : "curramSideMenuOverFlowButtonExpand",
                _classCurramSideMenuOverFlowButtonExpandIcon : "curramSideMenuOverFlowButtonExpandIcon",

                constructor:function (args) 
                {
                        this.inherited( arguments);
                        this._menuPaneButtonIndexer = new _MenuPaneButtonIndexer();
                },
                
                postCreate : function() {
                        this.inherited(arguments);
                        
                        // hide the expando pain's visule controles
                        domClass.add(this.titleWrapper, "dijitHidden");
                        
                        // setup the button that hides / displays the over flow menu
                        this._expandButton = new dijit.form.Button({
                                id: "navOverflowButton",
                                baseClass: this._classCurramSideMenuOverFlowButtonExpand, 
                                iconClass: this._classCurramSideMenuOverFlowButtonExpandIcon,
                                orgID : "exapnadButton",
                                showLabel : false
                        });

                        // used to contain the contents of the dialog
                        this._toolTipDialogExpandContentsListWrapper = new curam.widget.componentWrappers.ListWraper(
                                {
                                        listType   :       'ol',
                                        role       :       "menu",
                                        baseClass  :       this._classNavMenuOverFlow,
                                        _doBeforeItemSet : lang.hitch(this,function(item, listItem)
                                        {
                                                if (item != null)
                                                {                                                       
                                                        if(this.isLeftToRight())
                                                        {
                                                                // alight the button to one side of the over flow menu
                                                                domStyle.set(item.focusNode, "textAlign", "left");
                                                                // set padding at the traling side of the button
                                                                domStyle.set(item.containerNode, "marginRight", "10px"); 
                                                        }
                                                        else
                                                        {
                                                                // alight the button to one side of the over flow menu
                                                                domStyle.set(item.focusNode, "textAlign", "right");
                                                                // set padding at the traling side of the button
                                                                domStyle.set(item.containerNode, "marginLeft", "10px"); 
                                                        } 
                                                        
                                                        // over come an issue with the over lfow arrows witht the containg dialog
                                                        domStyle.set(item.containerNode, "padding", "0px");
                                                        
                                                        // set the over flow menu style
                                                        item.set("baseClass",this._classCurramSideMenuOverFlowButton);
                                                        // set the over flow menu style
                                                        domClass.replace(item.domNode, this._classCurramSideMenuOverFlowButton, this._classCurramSideMenuButton);                                                       
                                                        // set the icon style for the over flow menu
                                                        domClass.add(item.iconNode, this._classCurramSideMenuOverFlowButtonIcon);
                                                        
                                                } 
                                        })
                                }
                        );//.placeAt(this._toolTipDialogExpand.containerNode);
                        
                        var overFlowContainer = null;
                        
                        if(has("ie") != null && has("ie") < 9){
                          // ie 8 and less
                          overFlowContainer = domConstruct.create("div");
                        }
                        else
                        {
                            // every other browser
                            overFlowContainer = domConstruct.create("nav");                    
                        }

                        domAttr.set(overFlowContainer, "role", "navigation");
						domAttr.set(overFlowContainer, "aria-label", "navigation_menu_overflow");

                        this.overFlowContainer = overFlowContainer;
                        this._toolTipDialogExpandContentsListWrapper.placeAt(overFlowContainer);
                                                
                        this._listWrapper = new curam.widget.componentWrappers.ListWraper(
                                        {
                                                listType       :       'ol',
                                                role           :       "menu",
                                                baseClass      :       this._classNavMenu,
                                                _doBeforeItemSet : lang.hitch(this,function(item, listItem)
                                                {               
                                                        if (item != null && item.orgID != "exapnadButton" )
                                                        {
                                                                // remove the over flow menu icon
                                                                domClass.remove(item.iconNode, this._classCurramSideMenuOverFlowButtonIcon);
                                                                if(has("ie"))
                                                                {
                                                                  // fix for ie to remove redendent style
                                                                  domClass.remove(item.domNode, "curramSideMenuOverFlowButtonHover");
                                                                }
                                                                
                                                                // center the text
                                                                domStyle.set(item.focusNode, "textAlign", "center");
                                                                
                                                                // set padding at the traling side of the button
                                                                if(this.isLeftToRight())
                                                                {
                                                                        domStyle.set(item.containerNode, "marginRight", "0px"); 
                                                                }
                                                                else
                                                                {
                                                                        domStyle.set(item.containerNode, "marginLeft", "0px"); 
                                                                }
                                                                // BEGIN, 168309, MR
                                                                if((null!=item.containerNode.id) && (null!=item.containerNode.innerHTML)){
                                                              
                                                               dojo.setAttr(item.containerNode.id,"title",item.containerNode.innerHTML);}
                                                                
                                                                // END, 168309
                                                                // the side menu style
                                                                item.set("baseClass", this._classCurramSideMenuButton);
                                                                // the side menu style
                                                                domClass.replace(item.domNode, this._classCurramSideMenuButton, this._classCurramSideMenuOverFlowButton);
                                                        }       
                                                })
                                                
                                        }
                        );//.placeAt(this.containerNode);
                        
                        if(has("ie") != null && has("ie") < 9){
                            // ie 8 and less
                            var div1 = domConstruct.create("div", null, this.containerNode);
                            domAttr.set(div1, "role", "navigation");
							domAttr.set(div1, "aria-label", "navigation_menu");
                            this._listWrapper.placeAt(div1);
                        }
                        else
                        {
                            // every other browser
                            var navEl = domConstruct.create("nav", null, this.containerNode);
                            domAttr.set(navEl, "role", "navigation");
							domAttr.set(navEl, "aria-label", "navigation_menu");
                            this._listWrapper.placeAt(navEl);                       
                        }
                        
                        this._fadeIn = fx.fadeIn({ node: this._listWrapper.domNode, duration: this.duration, onEnd : lang.hitch(this, "_showContainer") });
                        this._fadeOut = fx.fadeOut({ node: this._listWrapper.domNode, duration: this.duration, onEnd: lang.hitch(this, "_onHideEnd") });
                
                        this._resizeResizeHandler = aspect.after(this, "resize", this._doResize, true);
                        
                        this._showEndresizeResizeHandler = aspect.after(this, "_showEnd", lang.hitch(this,"_onShowComplete"), false);
                        this._hideEndResizeHandler = aspect.after(this, "_hideEnd", lang.hitch(this,"_onHideComplete"), false);

                        
                },              
                
                /**
                 * Override to force startup/layout on BorderContainer.
                 */
                startup : function() {
                        this.inherited(arguments);

                             
                },
                
                fadeIn : function ()
                {
                        this._fadeIcons(true);
                },
                
                fadeOut : function()
                {
                        this._fadeIcons(false);
                },
                
                _fadeIcons : function(visable)
                {

                        // close popup
                        this._toolTipDialogExpand.hide(this._expandButton.domNode);

                        if(visable == true)
                        {
                                // show panel
                                
                                if(this._fadeOut.status() == "playing"){
                                        this._fadeOut.stop();
                                        this._fadeIn.play();
                                }
                                else
                                {
                                        if(this._fadeIn.status() != "playing"){
                                                this._fadeIn.play();
                                        }
                                }
                        }
                        else
                        {
                                // hide panel
                                if(this._fadeIn.status() == "playing"){
                                        this._fadeIn.stop();
                                        this._fadeOut.play();
                                }
                                else
                                {
                                        if(this._fadeOut.status() != "playing"){
                                                this._fadeOut.play();
                                        }
                                }
                        }
                        
                        
                },
                
                _showContainer : function()
                {
                        if(!this._showing){
                                this.toggle();
                        }
                },
                
                /**
                 * utility function to be over writen
                 */
                _onShowComplete : function()
                {
                        
                },
                
                _onHideEnd : function()
                {
                        if(this._showing){
                                this.toggle();
                        }
                },

                
                /**
                 * utility function to be over writen
                 */
                _onHideComplete : function()
                {
                        
                },
                
                /**
                 * add a new array of menu items
                 */
                addMenuItems : function (items)
                {
                        
                        this._cleanDownExistingMenuItems();
                        
                        dojo.forEach(items, function(item, i){
                            this._addMenuItem(item, i);
                          },this);
                        
                        this._initaleProcessMenuItems();
                        this._initalePlaceMenuItems();
                        
                },
                
                /**
                 * Function destroys all buttons and puts it back 
                 * to a clean unset state
                 */
                _cleanDownExistingMenuItems : function()
                {
                        // care full here the order is important
                        this._removeButtonCacheContent();
                        this._toolTipDialogExpandContentsListWrapper.deleteAllChildern();
                        
                        this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length = 0;
                        
                        this._removeExpandButton();     
                        this._listWrapper.deleteAllChildern();                  

                        this._menuPaneButtonIndexer._buttonDisplayOrderArrayOrginale.length = 0;
                        
                        this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length = 0;
                        
                        this._menuPaneButtonIndexer.selectedButtonKey = -1;
                        this._menuPaneButtonIndexer.selectedButtonDisplayIndex = -1;
                        this._menuPaneButtonIndexer.expandButtonDisplayIndex = -1;
                        this._menuPaneButtonIndexer.selectedButtonKey = -1;
                        
                        for (var key in this._menuPaneButtonIndexer._subPagenMap) 
                        {                               
                                delete this._menuPaneButtonIndexer._subPagenMap[key];
                        }
                },
                
                /**
                 * Set's the selected button 
                 * 
                 * @param inputJson 
                 * @param exceptionButtonFound if true an exception will be thrown if button is not found
                 */
                setSelectedButton : function(inputJson)
                {
                        if(inputJson.exceptionButtonFound == null)
                        {
                                inputJson.exceptionButtonFound = true;
                        }
                        
                        if(this._menuPaneButtonIndexer.getButton(inputJson.key) == null && 
                                        this._menuPaneButtonIndexer.getSubPagePrimaryPage(inputJson.key) == null)
                        {
                                // display an abertory page that does not exist in the page maps
                                if(inputJson.exceptionButtonFound == false)
                                {
                                        // display the abertory page
                                        this._onSelectAfter(inputJson);
                                }
                                else
                                {
                                        // display an error if the page is not found
                                        throw new Error("No button exists with the requested id : "+inputJson.key);
                                }
                        }
                        else
                        {
                                this._buttonSelected(inputJson, true);
                        }
                        
                },
                
                /**
                 * Function unselects any selected menus. 
                 */
                deselect : function()
                {
                        if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex !=-1 )
                        {
                                var button = this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
                                
                                button.button.set('checked', false);
                        }
                },
                
                _onSelectBefore : function(inputJson)
                {
                        
                },
                
                _onSelectAfter : function(inputJson)
                {
                        
                },
                                
                /**
                 * Add new menu item
                 */
                _addMenuItem : function (item, index)
                {
                        item = this._filterItem(item);
                        
                        this._generateSubPageIndex(item.id, item.subPageIds);
                        
                        // defines call back for the selected button
                        // get get reference to the selected button
                        var cb = lang.hitch(this,function(selectedButton)
                                        {
                                                var pram = {
                                                                key : selectedButton.orgID,
                                                                param : []
                                                };
                                                
                                                this._buttonSelected(pram, false);
                                        });
                        
                        var but = new curam.widget.form.ToggleButtonGroup({
                                label : item.label,
                                orgID : item.id,
                                groupName : "menuPaneCuramWidget",
                                onClick :  function(e){         
                                        cb(this);
                                },
                                baseClass: this._classCurramSideMenuButton,
                                iconClass: this._classCurramSideMenuButtonIcon
                        });
                        
                        if(item.iconPath != null && lang.trim(item.iconPath).length >0 )
                        {
                                style.set(but.iconNode, {
                                    backgroundImage : "url("+item.iconPath+")" 
                                  });   
                        }                       
                        
                        if(item.selected != null && item.selected == true )
                        {
                                this._menuPaneButtonIndexer.selectedButtonKey = item.id;
                        }
                        
                        this._menuPaneButtonIndexer.addNewButton(but, item.id);
                        
                },
                
                /**
                 * Function builds up the index or primary to sub page
                 * relations
                 * 
                 * @primaryId id of primary page
                 * @subPageIds array of sub page id related to the primary id
                 */
                _generateSubPageIndex : function(primaryId, subPageIds)
                {
                        if(subPageIds != null && subPageIds.length >0)
                        {
                                dojo.forEach(subPageIds, function(subpageID){
                                        
                                        if(this._menuPaneButtonIndexer.getSubPagePrimaryPage(subpageID) == null)
                                        {
                                                this._menuPaneButtonIndexer.setNewSubPage(subpageID, primaryId);
                                        }
                                        else
                                        {
                                                throw new Error("There has been a clash, sub page has all ready been registered.  Primary ID : "+primaryId+" Subpage ID : "+subpageID);
                                        }

                                },this);
                        }
                },
                
                /**
                 * Utility function that can transform data in to 
                 * required format
                 */
                _filterItem : function(item)
                {
                        return item;
                },
                
                _initaleProcessMenuItems : function()
                {
                                
                        var boxSize = dojo.contentBox(this.domNode);    
                        
                        if(this._showing == false){
                                boxSize.w = this._showSize;
                        }       
                        
                        this._buttonSizerDiv = domConstruct.create("div", { style: { height : boxSize.h+"px", width : boxSize.w+"px" } }     );
                                                
                        domClass.add(this._buttonSizerDiv, "dijitOffScreen");
                        dojo.place(this._buttonSizerDiv, window.body());
                        
                        this._buttonSizerList = new curam.widget.componentWrappers.ListWraper(
                                        {
                                                listType : 'ol',
                                                baseClass : this._classNavMenu
                                                
                                        }
                        ).placeAt(this._buttonSizerDiv);

                        for (var key in this._menuPaneButtonIndexer._buttonMap)
                        {
                                var butItem = this._menuPaneButtonIndexer.getButton(key);
                                
                                if(butItem.button)
                                {
                                        this._buttonSizerList.set("item", butItem.button.domNode);
                                        var butonContextBox = dojo.contentBox(butItem.button.domNode);          
                                        
                                        this._menuPaneButtonIndexer.getButton(key).contextBox = butonContextBox;
                                }
                        }
                        
                        this._buttonSizerList.set("item", this._expandButton.domNode);

                        this._expandButtonContentBox = dojo.contentBox(this._expandButton.domNode);

                        domClass.add(this._expandButton.domNode, "dijitHidden");
                        
                        domConstruct.place(this._expandButton.domNode,window.body());
                        
                        // after the expand button has been created and added to the existing dom
                        // we can create the hover card
                         
                        this._toolTipDialogExpand = new idx.widget.HoverCard({
                          draggable : false,
                          hideDelay : 450,
                          showDelay : 0,
                          target: this._expandButton.id,
                          content : this.overFlowContainer,
                          forceFocus : true,
                          focus : lang.hitch(  this, function(){
                                  //get the list of buttons from the over flow panel
                                  var buttonToSwapBack = this._menuPaneButtonIndexer.getButtonSecondary(0);
                                  // set the focus to be the first button
                                  buttonToSwapBack.button.focus();

                                  }),
                          defaultPosition : ['after-centered', 'before-centered'],
                          moreActions:[ ],
                          actions: [ ]
                        });

                        // Hide the hover card initially.
                        domClass.add(this._toolTipDialogExpand.domNode, "dijitHidden");
                        // remove items from ibm one ui hovercard that are not needed
                        domClass.add(query(".idxOneuiHoverCardFooter",this._toolTipDialogExpand.bodyNode)[0], "dijitHidden");
                        domClass.add(this._toolTipDialogExpand.gripNode, "dijitHidden");
                        domClass.add(this._toolTipDialogExpand.actionIcons, "dijitHidden");
                        domClass.add(this._toolTipDialogExpand.moreActionsNode, "dijitHidden");
                        
                },
                
                /**
                 * Function places the buttons in the nav bar for the first 
                 * time
                 */
                _initalePlaceMenuItems : function()
                {
                        var index =0;
                        for (var key in this._menuPaneButtonIndexer._buttonMap) 
                        {
                                var item = this._menuPaneButtonIndexer.getButton(key);
                                
                                if(item.button.get('checked'))
                                {
                                        this._menuPaneButtonIndexer.selectedButtonDisplayIndex = index;
                                        this._menuPaneButtonIndexer.selectedButtonKey = key;
                                }
                                item.displayOrderOrginaleIndex = index;
                                
                                if( this._menuPaneButtonIndexer.expandButtonDisplayIndex == -1 &&  
                                                (this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight") ) > (this._expandButtonContentBox.h + item.contextBox.h)   )
                                {
                                        // add the item to the main container 
                                        this._listWrapper.set("item", item.button);
                                        this._menuPaneButtonIndexer.addButtonReferenceToPrimaryContainer(key, true);
                                }
                                else
                                {                                       
                                        this._addExpandButton(index);
                                        
                                        // add button to the pop up dialog
                                        this._toolTipDialogExpandContentsListWrapper.set("item", item.button);
                                        this._menuPaneButtonIndexer.addButtonReferenceToPrimaryContainer(key, false);
                                        
                                        if(index == this._menuPaneButtonIndexer.selectedButtonDisplayIndex )
                                        {
                                                selectedIndexPositionTemp =this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length-1;
                                        }                                       
                                }       
                                
                                if(index == 0)
                                {
                                        idcar = item.button.id;
                                }
                                index++;
                        }
                        
                        //remove move sizer containers
                        this._buttonSizerList.destroy();
                        domConstruct.destroy(this._buttonSizerDiv);
                        
                        if(this._menuPaneButtonIndexer.selectedButtonKey != -1 )
                        {
                                var selectedButton = this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
                                selectedButton.button._onClick();
                        }
                        
                },
                
                _addExpandButton : function(index)
                {
                        if( this._menuPaneButtonIndexer.expandButtonDisplayIndex == -1)
                        {
                                console.info("add expando");
                                // add the expand button
                                this._menuPaneButtonIndexer.expandButtonDisplayIndex = index;
                                
                                domClass.remove(this._expandButton.domNode, "dijitHidden");
                                this._listWrapper.set("item", this._expandButton);
                        }
                },
                
                _removeExpandButton : function()
                {
                        if( this._menuPaneButtonIndexer.expandButtonDisplayIndex != -1 &&
                                        this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length == 0)
                        {
                                this._menuPaneButtonIndexer.expandButtonDisplayIndex = -1;

                                console.info("Remove expando : "+this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length);
                                domClass.add(this._expandButton.domNode, "dijitHidden");
                                
                                domConstruct.place(this._expandButton.domNode, window.body() );
                                
                                this._listWrapper.deleteChild(this._listWrapper.get("ItemCount"));
                                
                        }
                },
                
                _doResize : function(args)
                {
                        if(args != null && args.h != null && args.h >10)
                        {                         
                            if(this._previouseHeight != args.h && this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length > 0)
                            {
                                if(this.resizeDelay >0)
                                {
                                  // have a reisze delay helps performance                                
                                  if(this._resizeDelayHandler != null)
                                  {
                                      this._resizeDelayHandler.remove();
                                  }
                                  this._previouseHeight = args.h;
                                  if (this._toolTipDialogExpand) {
                                    this._toolTipDialogExpand.hide(this._expandButton.domNode);
                                  }
                              
                                  var cb = lang.hitch(this,function()
                                     {
                                        this._callRepositionButtons();
                                     });
                                
                                  this._resizeDelayHandler = this.defer(cb,this.resizeDelay);
                                }
                                else
                                {
                                  // no resize delay
                                  this._callRepositionButtons();
                                }
                            }
                        }
                },
                
                _callRepositionButtons : function()
                {
                    if(this._resizeCurentStatus == this._resizeStatusNotInUse)
                    {
                      this._positionButtonDuringResize();
                    }
                    else
                    {
                      this._resizeCurentStatus == this._resizeStatusQue;
                    }
                },
                
                _positionButtonDuringResize : function()
                {
                        this._resizeCurentStatus = this._resizeStatusResizeing;
                        if( this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length > 0 && 
                                        this.get("ContainerHeight") < this._listWrapper.get("ContainerHeight"))
                        {
                                // main menu panel has got smaller need to move 
                                // buttons to pop up
                                this._addExpandButton(this._listWrapper.get("ItemCount"));

                                var offset = 1;
                                while (  (this.get("ContainerHeight") < this._listWrapper.get("ContainerHeight"))
                                                && this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length > 0 )
                                {

                                        if(offset ==2 && this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length ==1)
                                        {
                                                offset =1;
                                        }
                                        
                                        var arrayIndex = this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-offset;
                                        

                                        var menuPaneItem = this._menuPaneButtonIndexer.getButtonPrimary(arrayIndex);

                                        //if (arrayIndex == this._menuPaneButtonIndexer.selectedButtonDisplayIndex )//this._menuPaneButtonIndexer.selectedButtonKey == key)
                                        if(menuPaneItem.button.get('checked') && this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length > 1)
                                        {
                                                // increase off set to leave selected button in place.
                                                offset = 2;
                                                console.info(arrayIndex+" : I am checked *************************  = "+menuPaneItem.button.get('checked'));
                                        }
                                        else
                                        {
                                                console.info("selected = "+menuPaneItem.button.get('checked'));
                                                
                                                // from primary to secondary, source : target 
                                                this._menuPaneButtonIndexer.swapButtonContainerToContainer(true, arrayIndex, 0);

                                                this._toolTipDialogExpandContentsListWrapper.set("item", menuPaneItem.button,"first");
                                                this._listWrapper.deleteChild(arrayIndex);

                                                this._menuPaneButtonIndexer.expandButtonDisplayIndex--;
                                                if(offset == 2)
                                                {
                                                        if (this._menuPaneButtonIndexer.selectedButtonDisplayIndex != 0)
                                                        {
                                                                this._menuPaneButtonIndexer.selectedButtonDisplayIndex--;
                                                        }
                                                        else
                                                        {
                                                                // not needed
                                                        }
                                                }
                                        }
                                }

                                console.info("Move from main to popup-----------------");

                        }
                        else if(this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length > 0 && 
                                        this.get("ContainerHeight") > this._listWrapper.get("ContainerHeight"))
                        {
                                // you have items in the pop up that could be moved to the main menu panel
                          
                          console.info(" secondary container size = "+this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length);

                                console.info("Move from popup to main****************");

                                var keepGoing = true;
                                while (keepGoing &&  this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length > 0 ) 
                                {
                                        var index =0;
                                        var item = this._menuPaneButtonIndexer.getButtonSecondary(index);
                                        
                                        if( item != null &&  
                                                        (this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight") ) > item.contextBox.h   )
                                        {
                                                var insertPosition = this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length;// "last";
                                                
                                                
                                                if(this._menuPaneButtonIndexer.expandButtonDisplayIndex != -1)
                                                {
                                                        //insertPosition = this._menuPaneButtonIndexer.expandButtonDisplayIndex;
                                                        this._menuPaneButtonIndexer.expandButtonDisplayIndex++;
                                                        
                                                        if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length  > 0 )
                                                        {
                                                                var lastButton = this._menuPaneButtonIndexer.getButtonPrimary(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
                                                                if(lastButton.button.get('checked') &&
                                                                                 lastButton.displayOrderOrginaleIndex >= insertPosition         )
                                                                {
                                                                        if(insertPosition != 0)
                                                                        {
                                                                                insertPosition--;
                                                                        }
                                                                        this._menuPaneButtonIndexer.selectedButtonDisplayIndex ++;
                                                                }
                                                        }
                                                }
                                                
                                                // secondary to primary  : source : target
                                                this._menuPaneButtonIndexer.swapButtonContainerToContainer(false, 0, insertPosition);
                                                this._listWrapper.set("item", item.button,insertPosition);
                                                this._toolTipDialogExpandContentsListWrapper.deleteChild(index);
                                                

                                                if(this._menuPaneButtonIndexer.expandButtonDisplayIndex != -1 &&
                                                                this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length <=0)
                                                {       
                                                        this._removeExpandButton();
                                                }                                                       
                                        }
                                        else
                                        {
                                                keepGoing = false;
                                        }
                                }
                        }
                        else
                        {
                                // do nothing here as there is enough room to dispaly all
                                // buttons
                                //console.info("do nothing!"+this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length);
                        }
                        
                        // a resize request has been queued up; do it now
                        if(this._resizeCurentStatus <= this._resizeStatusResizeing)
                        {
                             this._resizeCurentStatus= this._resizeStatusNotInUse;
                        }
                        else
                        {
                            this._positionButtonDuringResize.apply(this);
                        }
                },
                
                /**
                 * Function handles a nav bar button been pressed
                 * in order to process the new page to be displayed
                 * 
                 * @param selectedButton If a @param secondaryPage is false or 
                 * null @param selectedButton will be a wrapper containing the 
                 * selected button, if it is true it will be the ID of the sub page to display
                 * 
                 * @param subPage is a boolean if true a sub page is requested if false the 
                 * primary page is requested
                 */
                _buttonSelected : function(inputJson, subPage)
                {       
                        
                        this._toolTipDialogExpand.hide(this._expandButton.domNode);
                        
                        var buttonWraped;
                        
                        if(this._menuPaneButtonIndexer.getButton(inputJson.key) != null )
                        {
                                buttonWraped = this._menuPaneButtonIndexer.getButton(inputJson.key);    
                        }
                        else if(this._menuPaneButtonIndexer.getSubPagePrimaryPage(inputJson.key) != null)
                        {
                                var prmaryButtonID = this._menuPaneButtonIndexer.getSubPagePrimaryPage(inputJson.key);
                                buttonWraped = this._menuPaneButtonIndexer.getButton(prmaryButtonID);
                        }
                        else
                        {
                                throw new Error("state unknow for requested selected button : "+inputJson.key);
                        }
                        
                        buttonWraped.button.set("checked",true);
                        this._onSelectBefore(inputJson);
                        this._positionSelectedButton(buttonWraped);
                        if(this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length >0)
                        {
                              this._previouseHeight++;
                              this._callRepositionButtons();
                        }
                        this._onSelectAfter(inputJson);
                },
                
                _positionSelectedButton : function(selectedButton)
                {
                        
                        // if a button is all ready selected move it back to it's orginale place
                        if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex !=-1 )
                        {
                                // this._menuPaneButtonIndexer.selectedButtonDisplayIndex
                                // this._menuPaneButtonIndexer.selectedButtonKey = item.id;
                                
                                var previousSelectedButton = this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
                                var orginaleIndex = previousSelectedButton.displayOrderOrginaleIndex;
                                
                                // need to reposition the old selected button
                                if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex != orginaleIndex)
                                {
                                        // need to reposition button to orginale place holder.
                                        
                                        
                                        if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length >0)
                                        {
                                                // button spread accross both primary and secondary containers
                                        
                                                // get the index of the postion in the secondary
                                                var orginaleIndexPosition = orginaleIndex - (this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
                                                
                                                // get the button to sawap back that the selected button took it's place
                                                var buttonToSwapBack = this._menuPaneButtonIndexer.getButtonSecondary(0);
        
                                                // from primary to secondary, source : target 
                                                this._menuPaneButtonIndexer.swapButtonContainerToContainer(true, this._menuPaneButtonIndexer.selectedButtonDisplayIndex, orginaleIndexPosition);
                                                this._toolTipDialogExpandContentsListWrapper.set("item", previousSelectedButton.button, orginaleIndexPosition);
                                                this._listWrapper.deleteChild(this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
                                                
        
                                                // from secondary to primary, source : target 
                                                this._menuPaneButtonIndexer.swapButtonContainerToContainer(false, 0, this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
                                                this._listWrapper.set("item", buttonToSwapBack.button, this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
                                                this._toolTipDialogExpandContentsListWrapper.deleteChild(0);
                                                
                                                this._menuPaneButtonIndexer.selectedButtonDisplayIndex =-1;
                                                this._menuPaneButtonIndexer.selectedButtonKey = -1;
                                        }
                                        else
                                        {
                                                // all buttons in the secondary container
                                                

                                                // get the index of the postion in the secondary
                                                var orginaleIndexPosition = previousSelectedButton.displayOrderOrginaleIndex;
                                                
                                                // reorder the buttons in the indexer | secondary  : source : target
                                                this._menuPaneButtonIndexer.swapButtonContainerItemIndex(false, 0, orginaleIndexPosition);
                                                
                                                // place it at the start of the list
                                                this._toolTipDialogExpandContentsListWrapper.set("item", previousSelectedButton.button,orginaleIndexPosition+1);                                        
                                                // remove old place holder
                                                this._toolTipDialogExpandContentsListWrapper.deleteChild(0);
                                                
                                                this._menuPaneButtonIndexer.selectedButtonDisplayIndex =-1;
                                                this._menuPaneButtonIndexer.selectedButtonKey = -1;
                                                
                                        }
                                        
                                }
                                else
                                {
                                        console.info('no need to repostion old selected button');
                                        // no need to reposition the button                                     
                                        this._menuPaneButtonIndexer.selectedButtonDisplayIndex =-1;
                                }                               
                        }                       
                        
                        // now to reposition the newly slected button
                        
                        var orginaleIndex = selectedButton.displayOrderOrginaleIndex;
                        
                        if(this._menuPaneButtonIndexer.getWhichContinerFromIndex(orginaleIndex) == 1)
                        {
                                // selected button in secondary container need to move to primary container
                                
                                if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length >0)
                                {
                                        // button spread accross both primary and secondary containers
                                        
                                        // get the index of the postion in the secondary
                                        var selectedButtonIndex = orginaleIndex - (this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);                                    
                                        
                                        // the button that needs to be moved to creat space for selected button
                                        var buttonToMove = this._menuPaneButtonIndexer.getButtonPrimary(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
                                        
                                        // from primary to secondary, source : target 
                                        this._menuPaneButtonIndexer.swapButtonContainerToContainer(true, this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1, 0);
                                        this._toolTipDialogExpandContentsListWrapper.set("item", buttonToMove.button, 0);
                                        this._listWrapper.deleteChild(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
                                        

                                        // from secondary to primary, source : target 
                                        this._menuPaneButtonIndexer.swapButtonContainerToContainer(false, selectedButtonIndex+1, this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
                                        this._listWrapper.set("item", selectedButton.button, this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
                                        this._toolTipDialogExpandContentsListWrapper.deleteChild(selectedButtonIndex+1);

                                        this._menuPaneButtonIndexer.selectedButtonDisplayIndex = this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1;
                                        this._menuPaneButtonIndexer.selectedButtonKey = selectedButton.key;                                     
                                        
                                }
                                else
                                {
                                        // all buttons in the secondary container                                       
                                        
                                        // reorder the buttons in the indexer | secondary  : source : target
                                        this._menuPaneButtonIndexer.swapButtonContainerItemIndex(false, orginaleIndex, 0);
                                        
                                        // place it at the start of the list
                                        this._toolTipDialogExpandContentsListWrapper.set("item", selectedButton.button,0);                                      
                                        // remove old place holder
                                        this._toolTipDialogExpandContentsListWrapper.deleteChild(orginaleIndex+1);
                                        
                                        this._menuPaneButtonIndexer.selectedButtonDisplayIndex = 0;
                                        this._menuPaneButtonIndexer.selectedButtonKey = selectedButton.key;
                                }                               
                        }
                        else
                        {
                                // selected button in primary container no need to reposition buttons
                                this._menuPaneButtonIndexer.selectedButtonDisplayIndex = orginaleIndex;
                                this._menuPaneButtonIndexer.selectedButtonKey = selectedButton.key;

                                console.info('no need to repostion New selected button :' +orginaleIndex +" key = "+selectedButton.key);
                        }                       
                },
                
                _placeMenuItems : function(item, index)
                {       
                        
                        if( this._menuPaneButtonIndexer.expandButtonDisplayIndex == -1 &&  
                                        (this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight") ) > (this._expandButtonContentBox.h + item.contextBox.h)   )
                        {
                                // add the item to the main container 
                                this._listWrapper.set("item", item.button);
                        }
                        else
                        {                                       
                                if( this._menuPaneButtonIndexer.expandButtonDisplayIndex == -1)
                                {
                                        // add the expand button
                                        this._menuPaneButtonIndexer.expandButtonDisplayIndex = index;
                                        
                                        domClass.remove(this._expandButton.domNode, "dijitHidden");
                                        this._listWrapper.set("item", this._expandButton);
                                }
                                
                                // add button to the pop up dialog
                                this._toolTipDialogExpandContentsListWrapper.set("item", item.button);
                                
                        }
                },
                
                _getContainerHeightAttr : function()
                {
                        var contentBoxOuterContainer = domGeom.getContentBox(this.containerNode);
                        return contentBoxOuterContainer.h;
                },
                
                _setWidthAttr : function(width)
                {                                         
                        if(this._showing){
                                // resize container when visable
                                // not implamented
                        }
                        else
                        {
                                // resize container when not visable 
                                this._showAnim.properties.width = width;
                                this._showSize = width;
                                this._currentSize.w = width;

                        }
                },
                
                /**
                 * function removes the content of the button 
                 * cache;
                 */
                _removeButtonCacheContent: function()
                {
                        for (var key in this._menuPaneButtonIndexer._buttonMap) 
                        {
                                var entry = this._menuPaneButtonIndexer.getButton(key);
                                if(entry.button)
                                {
                                        entry.button.destroy();
                                }
                                delete entry.button;
                                delete entry.contextBox;
                                delete entry.displayOrderIndex;
                                delete entry.displayOrderOrginaleIndex;
                                delete entry.id;
                                delete entry.key;
                                delete entry;
                                
                                delete this._menuPaneButtonIndexer._buttonMap[key];
                        }
                },
                
                /**
                 * Clean up functions
                 */
                destroy: function() {
                        try
                        {       
                                this._resizeCurentStatus= this._resizeStatusNotInUse;
                                this._resizeDelayHandler != null ? this._resizeDelayHandler.remove() : null;                            
                                this._resizeResizeHandler.remove();
                                this._showEndresizeResizeHandler.remove();
                                this._hideEndResizeHandler.remove();
                                
                                this._expandButton.destroy();
                                this._removeButtonCacheContent();
                                this._toolTipDialogExpandContentsListWrapper.destroy();
                                this._toolTipDialogExpand.destroy();                            
                                this._listWrapper.destroy();    
                                delete this._menuPaneButtonIndexer;
                        }       
                        catch(err){
                                console.error(err);
                        }                       
                        this.inherited(arguments);
                }
        });
});