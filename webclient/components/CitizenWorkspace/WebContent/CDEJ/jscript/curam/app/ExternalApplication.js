//BEGIN, 160825,MR
var counter=0;
//END, 160825
define(
  [ "dojo/ready",
	"dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/window",
    "dojo/aspect",
    "dojo/dom-attr",
    "dojo/query",
    "dojo/dom-construct",
    "dijit/_Widget",
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
    "dijit/layout/BorderContainer",
    "dijit/layout/ContentPane",
    "dijit/form/Button",
    "dojo/text!./templates/ExternalApplication.html",
    /* Callback parameters Start */
    "curam/util/UIMFragment",
    "dojo/dom-class",
    "dojo/dom-style",
    "curam/ui/ClientDataAccessor",    
    "curam/widget/containers/TransitionContainer",
    "dojo/on",
    "curam/widget/menu/MenuPane",
    "dijit/CheckedMenuItem",
    "dojo/fx",
    "dijit/focus",
    "dojo/dom-geometry",
    /* Callback parameters End*/
    /* ONE UI banner requirements */
    "idx/widget/MenuBar",
    "idx/widget/Menu",
    "curam/app/Header",
    "idx/widget/MenuDialog",
    "idx/widget/MenuHeading",
    "idx/widget/HoverHelpTooltip",
    "dijit/PopupMenuBarItem",
    "dijit/MenuItem",
    "dijit/form/ComboButton",
    "curam/widget/menu/BannerMenuItem",
    "curam/widget/menu/MegaMenu",
    /* END ONE UI banner requirements */
    "curam/util/SessionTimeout",
    "curam/util/ui/AppExitConfirmation",
    "curam/util"
    ],
  function(
	ready,	  
    declare,
    lang,
    win,
    dojoAspect,
    domAttr,
    query,
    domConstruct,
    _Widget,
    _TemplatedMixin,
    _WidgetsInTemplateMixin,
    BorderContainer,
    ContentPane,
    Button,
    template,
    UIMFragment,
    domClass,
    domStyle,
    ClientDataAccessor,
    TransitionContainer,
    onConnect,
    MenuPane,
    CheckedMenuItem,
    fx,
    focusUtil,
    domGeom) {
  return declare("curam.app.ExternalApplication",[_Widget,_TemplatedMixin,_WidgetsInTemplateMixin], 
  {
    started : false,

  /**
   * The template text.
   *
   * @private
   * @type String
   * @field
   */
  templateString: template,

  widgetsInTemplate: true,
  
  baseClass : "curamApp",
  
  _appConfig: null,
  
  _initializedNavBarID: null,
  
  guardAgainstLeaving: null,

  directLinkData: null,

  /**
   * Overridden to mixin the override resources if provided.
   * @function
   * @public
   */
  postMixInProperties: function() {
    this.inherited(arguments);
  },
  
  /**
   * Override to force startup/layout on BorderContainer.
   */
  startup: function() {
    this.inherited(arguments);
    this._init();
    this._setupUserLeavingGuard();
  },
  
  
  _isNavBarItem: function(uimPageID) {
    // the "map" variable is created in TabLayoutResolver.java
    return (this._appConfig.map[uimPageID] != null); 
  },
  
  /**
   * 
   */
  _init: function() {
    curam.util.extendXHR();
    var da = new ClientDataAccessor();
    da.getRaw("/config/tablayout/extapp[" + curam.config.appID + "]",
        lang.hitch(this, function(data) {
          console.log("External App config data:" + data);
          this._appConfig = data;
          this._postDataLoadInit();
        }),
        function(error, args) {
          console.log("External App config data load error:" + error);
        },
        null);

    // Want to meuns in the banner to animate open and closed, so need to hook
    // into the dojo popupmanager to begin animations at the most appropriate
    // moment.
    dojoAspect.before(dijit.popup, "open", this._evenOutMenuRows, true);
    dojoAspect.after(dijit.popup, "open", lang.hitch(this, "_animateMenuOpen"), true);
    dojoAspect.after(dijit.popup, "close", lang.hitch(this, "_animateMenuClose"), true);


    // The banner should dynamically adapt the length of its text as the
    // viewport is resized or as the user zooms in or out.
    // We use a timer here so the event is not constantly processed as the user
    // resizes the window, or zooms.
    this._bannerResizeTimer = null;
    onConnect(window, "resize", dojo.hitch(this, function(){
      if(this._bannerResizeTimer) clearTimeout(this._bannerResizeTimer);
      this._bannerResizeTimer = setTimeout(this._handleBannerResize, 100);
    }));

  },
  
  /**
   * Sets up a handler that will pop up a confirmation dialog before the user
   * leaves the external application. It will only do so if the appropriate
   * configuration value is enabled. 
   */
  _setupUserLeavingGuard: function() {
    if (this.guardAgainstLeaving) {
      ready( function(){
    	  curam.util.ui.AppExitConfirmation.install();
      });
      
    }
  },

  /**
   * Loads the application "landing page" into the main content panel
   * (_mainContent attach point).
   */
  _loadLandingPage: function(contentPaneID) {
    //TODO: curam.config is a global object on curam pages. Update this widget
//BEGIN, 152478, PC
    if (null!=curam.config.landingPage) {
      // Load the landing page
      this._displayOnlyBodyContent({
      	// BEGIN, CR00459514, MV
        pageID: "CitizenWorkspace_landingPageSessionInvalidate"
      	// END, CR00459514
      });
    } else {
      throw "ERROR: Landing page not set correctly: "
            + curam.config.landingPage;
    }
  //END, 152478   
  },

  /**
   * Loads the application banner.
   */
  _loadBanner: function() {
    // Complex hitching going on here to handle scope. The onLoad callback below
    // is setup to run the _initializeBannerLandingPageLink method in the
    // scope of this object (an instance of ExternalApplication). We have to use
    // hitch to ensure this. TODO: get rid of this hitching and instead ensure
    // ExternalApplication is a singleton with a "getInstance()" method we can
    // call from anywhere. Avoids all this hitching?
    UIMFragment.get({
      url: "CDEJ/extapp/application-banner-fragment.jspx",
      targetID: this._appBanner.id,
      onLoad: lang.hitch(this, this._initializeBannerLandingPageLink)
    });
  },

  /**
   * Loads the application banner.
   */
  _reloadBanner: function() {

    //Calling _loadBanner to refresh the banner after it had been initially
    //displayed was throwing an error. We found that one ui was not correctly
    //handling its desendant widgets, and as such widgets used within the banner
    //were not getting destroyed. We have to manually destory the widgets
    //(with ids) used in the banner, before we can reload it. This prevents the
    //"Tried to register widget with id==xxx but that id is already registered"
    //error.
    if (dijit.byId("MMMenuID")) {
      dijit.byId("MMMenuID").destroy();
    }
    if (dijit.byId("appMegaMenu")) {
      dijit.byId("appMegaMenu").destroy();
    }
    if (dijit.byId("appHelpMenu")) {
      dijit.byId("appHelpMenu").destroy();
    }
    if (dijit.byId("appBannerPrintMenu")) {
      dijit.byId("appBannerPrintMenu").destroy();
    }
    //BEGIN, CR00467393, PC
    if (dijit.byId("appBannerLogOutMenu")) {
      dijit.byId("appBannerLogOutMenu").destroy();
    }
   //END, CR00467393
    this._loadBanner();
  },
  
  /** 
   * Initializes the landing page link in the banner. Must only be called AFTER
   * the banner is loaded. Also, this method assumes that "this" points to an
   * instance of this class, an instance of ExternalApplication. The
   * _loadBanner() method guarantees this.
   * 
   * TODO: Re-implement the banner as a dijit with its own life cycle.
   */
  _initializeBannerLandingPageLink: function(contentPane) {
    // narrow down the query to the dom of the banner content pane
    var headerPrimaryTitle = query(".idxHeaderPrimaryTitle",
                                   contentPane.domNode)[0];
    if (!headerPrimaryTitle) {
      throw "Landing Page link not initialized, title node cannot be found";
    }
    var callBack = lang.hitch(this, "_loadLandingPage");
    dojo.connect(headerPrimaryTitle, "onclick", function() {
      callBack();
    });
  },

  /**
   * Animate the menu open. The menu will first slide open and then the its
   * content will fade in. The function is ran after dijit.popup.open and is
   * passed the same arguments as that function.
   */
  _animateMenuOpen: function(args) {

    var aniNode = dojo.byId(args.popup._popupWrapper.id);

    // only animate if the menu is a mega menu, or help menu from the banner
    if (domAttr.get(aniNode, "dijitpopupparent") !== "appMegaMenu" &
      !domClass.contains(aniNode, "oneuiHeaderGlobalActionsMenu_help")) return

    // Find the table holding the content, only once.
    if (!aniNode.aniTable) aniNode.aniTable = dojo.query("table", aniNode)[0];

    // Attributes to track whether the menu animation states
    if (!aniNode.inAnimation) aniNode.inAnimation = false;
    if (!aniNode.isShown) aniNode.isShown = false;

    // if in animation, means user has kicked off this animation before the
    // previous one has finished. So need to stop and handle it.
    if (aniNode.inAnimation) {

      // The Help menu, by default, opens each time the user clicks it. We dont
      // want this behaviour. We want the help menu to act the same as the mega
      // menu, i.e. alternate between open and close when the user clicks on it.
      if (dijit.byId("appHelpMenu") === args.popup) {
        if (args.popup.shouldNotClose) { // variable set a wiget initialisation.
          args.popup.shouldNotClose = false;
          args.popup.cancelClose=true;
          return;
        }
      }
      aniNode.fx && aniNode.fx.stop();
      aniNode.inAnimation = false;
      aniNode.isShown = false;
      domStyle.set(aniNode, "display", "none");
    }

    if(!aniNode.isShown && !aniNode.inAnimation) {
      dojo.style(aniNode, "display", "none");
      dojo.style(aniNode.aniTable, "opacity", "0");
    }

    // setup animation callback functions
    // set the animation status attributes before and after the animation.
    var onBeginAni = function() {
      aniNode.inAnimation = true;
      aniNode.isShown = false;
      if (dijit.byId("appHelpMenu") === args.popup) {
        // flip the open state variable.
        args.popup.shouldNotClose = args.popup.shouldNotClose ? false : true;
        // menu is opening so it wasnt just closed.
        args.popup.justClosed = false;
      }
    };
    var onEndAni = function() {
      dojo.style(aniNode.aniTable, "opacity", "1");
      aniNode.inAnimation = false;
      aniNode.isShown = true;
      var aniNodeHeight = dojo.marginBox(aniNode).h;
      var viewportHeight = dojo.window.getBox().h - 65 - 20;
      if (viewportHeight < aniNodeHeight) {
        domStyle.set(aniNode, "height", viewportHeight + "px");
        domStyle.set(aniNode, "border-bottom", "1px solid black");
      }
    };

    this._animateMenu(aniNode, aniNode.aniTable, "open", onBeginAni, onEndAni);

  },

  /**
   * Animate the menu closed. The menu will first fade out its content and then
   * slide up. The function is ran after dijit.popup.close and is passed the
   * same arguments as that function.
   */
  _animateMenuClose: function(popup) {

    var aniNode = dojo.byId(popup._popupWrapper.id);

    // only animate if the menu is a mega menu, or help menu from the banner
    if (domAttr.get(aniNode, "dijitpopupparent") !== "appMegaMenu" &
        !domClass.contains(aniNode, "oneuiHeaderGlobalActionsMenu_help")) return

    // The Help menu, by default, opens each time the user clicks it. We dont
    // want this behaviour. We want the help menu to act the same as the mega
    // menu, i.e. alternate between open and close when the user clicks on it.
    // So if the menu has either already just closed, or been closed by some
    // other means (i.e. losing focus) then dont animate closed again.
    if (dijit.byId("appHelpMenu") === popup) {
      if (popup.cancelClose) {
        popup.cancelClose=false;
        return;
      } else if (popup.justClosed) {
        popup.justClosed = false;
        return;
      }
    }

    // Find the table holding the content, only once.
    if (!aniNode.aniTable) aniNode.aniTable = dojo.query("table", aniNode)[0];

    // if in animation, means user has kicked off this animation before the
    // previous one has finished. So need to stop and handle it.
    if (aniNode.inAnimation) {
      aniNode.fx && aniNode.fx.stop();
      aniNode.inAnimation = false;
      aniNode.isShown = false;
      dojo.style(aniNode.aniTable, "opacity", "1");
    }


    // Ensure the menu is fully visible before beginning to animate it closed.
    domStyle.set(aniNode, "display", "block");

    // setup animation callback functions
    // set the animation status attributes before and after the animation.
    var onBeginAni = function() {
      aniNode.inAnimation = true;
      aniNode.isShown = true;
    };
    var onEndAni = function() {
      aniNode.inAnimation = false;
      aniNode.isShown = false;
      domStyle.set(aniNode, "display", "none");
      domStyle.set(aniNode, "border-bottom", "");
      // Set Help Menu open state variables.
      if (dijit.byId("appHelpMenu") === popup) {
        popup.shouldNotClose = false;
        popup.justClosed = true;
      }
    };

    this._animateMenu(aniNode, aniNode.aniTable, "close", onBeginAni, onEndAni);

  },

  /**
   * Base animation function to actually set dojo animation object and start the
   * animation.
   */
  _animateMenu: function(mainNode, contentNode, action, onBeginAni, onEndAni) {

    var duration = 300;
    var animations = [];

    var showMenu = fx.wipeIn({ node: mainNode, duration: duration });
    var showContent = dojo.fadeIn({ node: contentNode, duration: duration });

    var hideMenu = fx.wipeOut({ node: mainNode, duration: duration });
    var hideContent = dojo.fadeOut({ node: contentNode, duration: duration });

    if (action === "open") {
      animations.push(showMenu);animations.push(showContent);
    } else {
      animations.push(hideContent);animations.push(hideMenu);
    }

    // animations are chained, as we want the menu to first open and then fade
    // in its content, and vice versa.
    mainNode.fx = fx.chain(animations);
    dojo.connect(mainNode.fx, "onBegin", onBeginAni);
    dojo.connect(mainNode.fx, "onEnd", onEndAni);
    mainNode.fx.play();

  },

  /** 
   * Ensure that MegaMenu items on any given row, all have a uniform height.
   * That is, a height equal to the largest in the row. This function is needed
   * as each column of items in the menu are repersented by a seperate table, so
   * there is no real link between items along rows.
   * 
   */
  _evenOutMenuRows: function(widget) {
    
    var widgetNode = dojo.byId(widget.popup.id);
    var widgetID = widget.parent ? widget.parent.id : widget.popup.id;
    // only animate if the menu is a mega menu, or help menu from the banner
    if (widgetID !== "appMegaMenu" & widgetID !== "appHelpMenu") return

    var rowClass, containerClass;
    if (widgetID === "appMegaMenu") {
      rowClass = "MMItemContainerRow";
      containerClass = "MMItemContainer";
    } else {
      rowClass = "HMItemContainerRow";
      containerClass = "HMItemContainer";
    }

    var items = dojo.query("div." + containerClass, widgetNode);

    var holdingNode = widget.popup._popupWrapper ? widget.popup._popupWrapper : widgetNode;
    domStyle.set(holdingNode, "display", "block");

    var numItems = items.length;
    var numRows = numItems < 6 ? 1 : Math.ceil(numItems/3);
    var highest = [];
    for (var i = 0; i < numRows; i++) highest[i] = 0;

    // Find highest height in each row.
    for (var curRow = 0; curRow < numRows; curRow++) {
      dojo.forEach(items, function(item, index) {
        domStyle.set(item, "height", "auto");
        // The width of the menus item icons can not be set on the server side.
        // We set the width just before the menu is open for the first time.
        // Items in the menus are held in tables with fixed layouts.
        // As such, we only need to set the width of an item in the first row of
        // the table and all other cells will be set to that value.
        if (curRow === 0 && !domClass.contains(item, "iconSetOUI")) {
          var imgCell = item;
          // Get the imgCell Parent Node
          while (!domClass.contains(imgCell, "menuItemClassOUI")) {
            imgCell = imgCell.parentNode;
          }
          // then find the actual td that holds the icon
          imgCell = dojo.query("td.dijitMenuItemIconCell", imgCell)[0];
          if (numRows === 1) {
            domStyle.set(imgCell, "width", "50px");
          } else {
            domStyle.set(imgCell, "width", "34px");
          }
          domClass.add(item, "iconSetOUI");
        }
        var itemHeight = domStyle.get(item, "height")
        if (domClass.contains(item, rowClass + curRow)) {
          if (itemHeight > highest[curRow] ) {
            highest[curRow] = itemHeight;
          }
        }
      });
    }

    // Heights found, so can once again hide the menu
    domStyle.set(holdingNode, "display", "none");

    // Apply the relavent height to each item.
    for (var i = 0; i < numRows; i++) {
      dojo.forEach(items, function(item, index) {
        if (domClass.contains(item, rowClass + i)) {
          domStyle.set(item, "height", highest[i] + "px");
        }
      });
    }

  },

  /**
   * We calculate the avilable space in the banner for the mega menu title text
   * and the user's welcome message. If there is not enough room for the full
   * text to appear we truncate them. We try to ensure that as much text is
   * shown as possible. We also position the MegaMenu so that it just beside the
   * user menu.
   */
  _handleBannerResize: function(oneuiBanner) {

    // Save the banner object if its passed in, otherwise use existing one.
    CuramExternalApp._oneuiBanner = oneuiBanner || CuramExternalApp._oneuiBanner;
    var currentBanner = CuramExternalApp._oneuiBanner;

    var helpExists = currentBanner._helpNode ? true : false;
    var userExists = currentBanner.userNode ? true : false;
    var megaExists = currentBanner.navigation ? true : false;
    var logoExists = currentBanner.logoExists;
    var printExists = currentBanner._settingsNode ? true : false;
    //BEGIN, CR00467393, PC
    var logoutExists = currentBanner.logoutNode ? true : false;
    //END, CR00467393
    // Had to put code to set the print menu text in this resize method as it
    // was the only place a banner reference ("currentBanner") was available.
    // The print menu is implemented using the OneUI "settings" menu.
    // We reset the alt text below to that of the print menu. The print menu
    // text is looked up in the "TabLayoutResolver" java class.
    if (printExists) {
      // Either title or alt is used based on the browser.
      if (domAttr.get(currentBanner._settingsNode, "title")) {
        domAttr.set(currentBanner._settingsNode, "title",
            CuramExternalApp._appConfig.printMenuLabel);
      }
      if (domAttr.get(currentBanner._settingsNode, "alt")) {
        domAttr.set(currentBanner._settingsNode, "alt",
            CuramExternalApp._appConfig.printMenuLabel);
      }
    }
     


    var isLTR = currentBanner.isLeftToRight();

    if (!userExists && !megaExists) return; //no menus, nothing to do!

    var bannerTitleNode = dojo.query(".idxHeaderPrimaryTitleContainer", currentBanner._leadingGlobalActionsNode)[0];

    var IBMLogoWidth = 0;
    if (logoExists) {
      var IBMLogoNode = dojo.query(".idxHeaderLogoBox", currentBanner._trailingGlobalActionsNode)[0];
      IBMLogoWidth = domGeom.getMarginBox(IBMLogoNode).w;
    }

    var totalBannerWidth = domGeom.position(win.body()).w;
    //BEGIN, CR00467393, PC
    var bannerInnerSpace = totalBannerWidth
                            - domGeom.getMarginBox(bannerTitleNode).w
                            - (helpExists? domGeom.getMarginBox(currentBanner._helpNode).w : 0)
                            - (printExists? domGeom.getMarginBox(currentBanner._settingsNode).w : 0)
                            - (logoutExists? domGeom.getMarginBox(currentBanner.logoutNode).w : 0)
                            - IBMLogoWidth;
    //END, CR00467393                      

    // BEGIN, 163961, MJ
    // Commenting below line of code as it seems to be not used anywhere and
	// giving javascript exception as part of 163961
	/*
	   var lastNodeXPosition = helpExists ? isLTR ?
	   domGeom.position(currentBanner._helpNode).x :
	   box(currentBanner._mainContainerNode).w + box(currentBanner._helpNode).w -
	   domGeom.position(currentBanner._helpNode).x : 885;
	 */
    // END, 163961  
    
    if(userExists){
      var userNode = currentBanner.userNode;
      var userTextNode = currentBanner.userTextNode;
      // reset width set by previous resize event.
      domStyle.set(userTextNode, "width", "");
      domStyle.set(userTextNode , "display", "");
      domStyle.set(userTextNode , "bottom", "");
      var UserWidth = domGeom.getMarginBox(userNode).w;
      var UserTextWidth = domGeom.getMarginBox(userTextNode).w;
    }

    if (megaExists) {
      var MegaMenuNode = currentBanner.navigation.domNode;
      var MegaMenuText = dojo.query("span[id*=text]", MegaMenuNode)[0];
      // reset width set by previous resize event.
      domStyle.set(MegaMenuText, "width", "");
      domStyle.set(MegaMenuText , "display", "");
      domStyle.set(MegaMenuNode, "margin-left", "");
      domStyle.set(MegaMenuNode, "vertical-align", "");
      var MegaMenuWidth = domGeom.getMarginBox(MegaMenuNode).w;
      var MegaMenuTextWidth = domGeom.getMarginBox(MegaMenuText).w;
    }

    var availSpaceForText = bannerInnerSpace
                            - ( megaExists ? (MegaMenuWidth - MegaMenuTextWidth) : 0 )
                            - ( userExists ? (UserWidth - UserTextWidth) : 0 );
    var textWidth = ( megaExists? MegaMenuTextWidth : 0 )
                    + ( userExists? UserTextWidth : 0 );


    // only need to modify width if there is not enough space for the text
    if(availSpaceForText - textWidth < 0) {
      var halfAvailSpace = availSpaceForText/2;
      var space;
      var nodes = [];
      if(megaExists & userExists){
        if(UserTextWidth < halfAvailSpace){
          space = availSpaceForText - UserTextWidth;
          nodes.push(MegaMenuText);
        }else if (MegaMenuTextWidth < halfAvailSpace){
          space = availSpaceForText - MegaMenuTextWidth;
          nodes.push(userTextNode);
        } else {
          space = halfAvailSpace;
          nodes.push(MegaMenuText);
          nodes.push(userTextNode);
        }
      } else if (megaExists) { // Only the MegaMenu exists
        space = availSpaceForText;
        nodes.push(MegaMenuText);
      } else { // Only the user menu exits
        space = availSpaceForText;
        nodes.push(userTextNode);
      }

      for (var i=0; i<nodes.length; i++) {

          domStyle.set(nodes[i], "width", space + "px");
          domStyle.set(nodes[i], "display", "inline-block");
          if (currentBanner.userTextNode === nodes[i]) {
            domStyle.set(nodes[i], "bottom", "3px"); //user only
          } else {
            domStyle.set(nodes[i], "vertical-align", "top"); //mega only
          }

      }
    }

    // Once the text lengths are set, we have to position the MegaMenu again.
    // BEGIN, 163961, MJ
    if (megaExists && isLTR) {
    // END, 163961
    	
      // We align the MegaMenu be using margin-left to push it to the rhs.
      // margin-left value is equal to the space in the banner not occupied by
      // other menus or text.
      var rightVal = totalBannerWidth - domGeom.position(currentBanner._trailingGlobalActionsNode).x;
      domStyle.set(currentBanner._leadingGlobalActionsNode, "right", rightVal + "px");
    }
  },

  _postDataLoadInit: function() {
    
    // assign helper function to main body that does
    // look ups for URI
    this._appContentBody._doResourceLookUp =
      lang.hitch(this, this._doResourceLookUpForMainBody);
    
    // Handles button selection on the nav bar
    this._appNav._onSelectAfter = lang.hitch(this,
        function(inputJson) {
      this._appContentBody.set(
          "displayPanel", inputJson);
    });
    
    
    this._makeNavBarAccessible();
    this._loadBanner();


    // Dont load the landing page if a direct page is requested.
    if (this.directLinkData) {
      if (this._isNavBarItem(this.directLinkData.pageID)) {
        this._initNavBar(this.directLinkData.pageID, lang.hitch(this, function() {
          this.directLinkData.isDirectLink = true;
          this._displayNavMenuAndBodyContent(this.directLinkData);
        }));
      } else {
        this._displayOnlyBodyContent(this.directLinkData);
      }
    } else {
      this._loadLandingPage();
    }

  },

  _initNavBar: function(uimPageID, callBack) {
    var navID = this._appConfig.map[uimPageID];
    if (typeof(navID) == "undefined" // page isn't associated with a navbar
        || navID == this._initializedNavBarID) { // the requested page is associated with the current nabar
      callBack();
      return;
    }
    // otherwise, we initialize the nav bar using "navID"
    var da = new ClientDataAccessor();
    da.getRaw("/config/tablayout/extnav[" + curam.config.appID + "][" + navID + "]",
        lang.hitch(this, function(data) {
          console.log("External App config data:" + data);
          this._loadMenuItems(data.navItems, data.navBarPixelWidth);
          callBack();
          this._initializedNavBarID = navID;
        }),
        function(error, args) {
          console.log("External App navigation config data load error:" + error);
        },
        null);
  },
  
  _makeNavBarAccessible: function() {
    
    var hoverCardCloseIcon = dojo.query(".idxOneuiHoverCardCloseIcon")[0];
    if (hoverCardCloseIcon) { 
      // Without setting this, the tabbing will become trapped once the last
      // focusable element is focused. The user will be unable to loop round to
      // the first element, or reverse tab to the previous one.
      domAttr.set(hoverCardCloseIcon, "tabindex", -1);
      
      // Setting the aria label for the close icon on the hover card to resolve
      // the accessibility issue existing in the current One UI toolkit.
      // TODO: This piece of code can be removed once the toolkit is upgraded
      // where this issue is resolved offically.
      domAttr.set(hoverCardCloseIcon, "aria-label", 
          this._appConfig.hoverCardCloseButtonLabel);
    }
    
    // Setting the label used for the overflow button in
    // the side navigation bar.
    var navOverflowButton = dijit.byId("navOverflowButton");
    navOverflowButton._setLabelAttr(this._appConfig.navOverflowButtonLabel);
    
  },
//BEGIN 118764, PC
  hideAppNavigation : function() {
    //hide left navigation panel
	  this._appNav.fadeOut();
  },  
	  
  showAppNavigation : function() {
    //show left navigation panel
	  this._appNav.fadeIn();
  }, 
//END 118764
  _loadMenuItems: function(navItems, navBarPixelWidth) {
    var menuItems = [];
    this._appNav.set("width", navBarPixelWidth);
    for (var i = 0; i < navItems.length; i++) {
      var navItem = navItems[i];
      var menuItem = {
        id: navItem.pageID,
        label: navItem.title,
        selected: false,
        iconPath: navItem.iconPath,
        subPageIds: navItem.subPageIds,
        iconClass: "whoKnow"     
      }
      menuItems.push(menuItem);
    }
    this._appNav.addMenuItems(menuItems);
  },
  
  megaMenuClick: function(args) {
    if (typeof(args.displayNavBar) == "undefined") {
      // if the displayNavBar hasn't been explicitly set, then it is assumed
      // it is hidden for all mega menu items.
      args.displayNavBar = false;
    }
    this.displayContent(args);
  },
  
  displayContent : function(inputJson) {
   if(inputJson != null) {
      inputJson.forceRefresh = true;
      // The rules for showing the nav bar or not are described
      // with each condition below. TODO: move conditions into
      // a separate function which just sets a boolean flag.
      // Take care with precedence when doing so.
      
      // Where "inputJson.displayNavBar" is explicitly set,
      // this takes precedence.
      
      // "displayNavBar" is explicitly set to false
      if (inputJson.displayNavBar == false) {
        this._displayOnlyBodyContent(inputJson);
        return;
      }
      // "displayNavBar" is explicitly set to false
      if (inputJson.displayNavBar == true) {
        this._displayNavMenuAndBodyContent(inputJson);
        return;
      }
      // inputJson.displayNavBar hasn't been specified
      
      // check if it is the landing page, if so hide the nav bar
      // this takes precedence over the conditions below.
  //BEGIN, 152478, PC
      if (inputJson.pageID == curam.config.landingPage) {
    	  if(!inputJson.pageID==""){
    		  this._displayOnlyBodyContent(inputJson);
    	  }
        return;
      }
 //END, 152478    
      // Check if it is a configured nav bar item and if so
      // display the nav bar
      if (this._isNavBarItem(inputJson.pageID)) {
        this._displayNavMenuAndBodyContent(inputJson);
        return;
      } else {
        // it is not a configured nav bar item, but the nav bar
        // is alread displayed, leave it so.
        if (this._appNav._showing) {
          this._displayNavMenuAndBodyContent(inputJson);
          return;          
        } else {
          // it is not a configured nav bar item, the nav bar is
          // already hidden, leave it so.
          this._displayOnlyBodyContent(inputJson);
          return;
        }
      }
    }
  },
  
  _displayOnlyBodyContent : function(inputJson) {       
    if(this._appNav._showing) {
      // side menu is displayed
      var handlerBodyFadeOut = this.connect(this._appContentBody, '_panelFadeOutComplete', lang.hitch(this,function(){
        // remove listener after first event
        handlerBodyFadeOut.remove();
        var handlerMenuFadeOut = this.connect(this._appNav, '_onHideComplete', lang.hitch(this,function(){
                this._appNav.deselect();

                handlerMenuFadeOut.remove();
                
                // PK change to allow inputJson to pass through....
                //this._appContentBody.set("displayPanel", {
                //        key : inputJson.pageID,
                //        param : inputJson.param
                //});
                inputJson.key = inputJson.pageID; 
                this._appContentBody.set("displayPanel", inputJson);
        }));

        this._appNav.fadeOut();
      }));

      this._appContentBody.fadeOutDisplay();  

    } else {
      // side menu is not displayed
      // PK change to allow inputJson to pass through....
      //this._appContentBody.set("displayPanel", {
      //        key : inputJson.pageID,
      //        param : inputJson.param
      //});
      inputJson.key = inputJson.pageID;
      this._appContentBody.set("displayPanel", inputJson);

    }
  },

  
  /**
   * Function will bring the nav menu and main body in to display
   * and display the page of the requested ID in the main body
   */
  _displayNavMenuAndBodyContent : function(inputJson)
  {               
    inputJson.key = inputJson.pageID;
    if (inputJson.param == null) {
      inputJson.param = [];
    }
    
    inputJson.exceptionButtonFound = false;

    if(this._appNav._showing) {
      this._appNav.setSelectedButton(inputJson);
    } else {
      if (inputJson.isDirectLink) {
        var handlerMenuFadeIn = this.connect(this._appNav, '_onShowComplete', lang.hitch(this,function(){
          handlerMenuFadeIn.remove();
          this._appNav.setSelectedButton(inputJson);
        }));
        this._appNav.fadeIn();
      } else {
        var handlerBodyFadeOut = this.connect(this._appContentBody, '_panelFadeOutComplete', lang.hitch(this,function(){
          // remove listener after first event
          handlerBodyFadeOut.remove();
          var handlerMenuFadeIn = this.connect(this._appNav, '_onShowComplete', lang.hitch(this,function(){

            handlerMenuFadeIn.remove();
            this._appNav.setSelectedButton(inputJson);
          }));

          this._appNav.fadeIn();
        }));
        this._appContentBody.fadeOutDisplay();
      }
    }
  },

  /**
   * Function looks up and returns a URI from a referenceIdentifer
   */
  _doResourceLookUpForMainBody : function(jsonIn, pramUrl, compositeKey) {
    // no translation required...for now....just return the identifier
    // directly
    
    var uri;
    // when the TransitionContainer invokes this call back, jsonIn.key is the
    // UIM Page ID
    if (jsonIn.key) {  
      // assume its a UIM so build a "curam style" url
      if (this._isUIMFragment(jsonIn.key)) {
        uri = jsL + "/" + jsonIn.key + "Page.do?"
              + this._addCDEJParameters();
      } else {
        // we assume it has to load in an iframe.
        uri = jsL + "/UIMIFrameWrapperPage.do?uimPageID="
              + jsonIn.key + "&" + this._addCDEJParameters();
      }
    } else if (jsonIn.url) {
      // just use the specified url without any changes
      uri = jsonIn.url
    }
    // NB: the uri built here is not complete. The TransitionContainer will 
    // take care of adding any parameters specified in jsonIn.param
    return uri;
  },
  
  /**
   * Adds CDEJ parameters to the request string. This function assumes it is
   * called directly after _constructPath() so it always starts with a "?".
   * The screen context parameter is the only one required for "fragment"
   * requests.
   */
  _addCDEJParameters: function() {
    return jsScreenContext.toRequestString();
  },
  /**
   * Called from util.js when a modal is opened from a UIM in the external
   * application. 
   */
  updateMainContentIframe: function(href) {
    var iframe = dojo.query("iframe", this.domNode)[0];
    if (iframe) {
      iframe.contentWindow.location.href = href;
    }
  },
  
  _isUIMFragment: function(pageID) {
    return (this._appConfig && this._appConfig.uimFragRegistry[pageID] != null);
  },


  /**
   * Handle when the user clicks or otherwise activates Person Menu title text.
   */
  _setupUserMenuLinking: function(userNode, linkArgs) {

	dojo.connect(userNode, 'onclick', lang.partial(function(linkArgs, evt){
      // only if the click occurs to the left of the user dropdown arrow, do we
      // allow the application to navigate to the user home page.
      var arrowNode = dojo.byId("curam-extapp_userMenuArrow");
      
      // Workaround when the Jaws is opened.
      if (evt.target != arrowNode) {
          displayContent(linkArgs);
      }
      
    }, linkArgs));
    
    dojo.connect(userNode, 'onkeypress', function(evt){
      if (evt.charOrCode === dojo.keys.ENTER) {
        dojo.stopEvent(evt);
        displayContent(linkArgs);
      }
    });

  },

  /**
   * Out of the box ONEUI header is not accessible. Need to apply certain
   * attributes to aid keyboard navigation.
   */
   _makeUserMenuAccessible: function (banner) {
    var userNode = banner.userNode;
    var textNode = banner.userTextNode;
    var hoverNode = banner.hoverNode;
    
    domAttr.set(userNode, "tabindex", "3");
    domAttr.set(textNode, "title", textNode.innerHTML);

    var userMenuDropDown = hoverNode;
    domAttr.set(userMenuDropDown, "tabindex", "4");
    domAttr.set(userMenuDropDown, "role", "button");
    
    var personMenuLinkHolderNode = dojo.query(".curam-extapp_userHoverMenu_class")[0];
    if(personMenuLinkHolderNode)
    {
    	domAttr.set(userMenuDropDown, "title", personMenuLinkHolderNode.title);
    }
    
    /* To handle the change of image in user node when we are in high contrast mode */
    this._handleUserImageHighContrast(userNode);
  },

  /** 
   * When we are in high contrast mode we should have a different image for user node 
   * and it should be changed in roll as well as click state.
   */
  _handleUserImageHighContrast: function(userNode) {
    var userImageNode = dojo.query(".idxHeaderUserIcon", userNode)[1];
    if (userImageNode) {
      var body = win.body();
      if (body && domClass.contains(body, "high-contrast")) {
        userImageNode.src = "servlet/PathResolver?r=i&p=/config/tablayout/image[banner_hom_normal.png]";
        onConnect(userImageNode, "mouseover", function() {
          userImageNode.src = "servlet/PathResolver?r=i&p=/config/tablayout/image[banner_home_roll.png]";
        });
        onConnect(userImageNode, "click", function() {
          userImageNode.src = "servlet/PathResolver?r=i&p=/config/tablayout/image[banner_home_click.png]";
        });
        onConnect(userImageNode, "mouseout", function() {
          userImageNode.src = "servlet/PathResolver?r=i&p=/config/tablayout/image[banner_hom_normal.png]";
        });
      }
    }
  },
  
  /**
   * Out of the box ONEUI header is not accessable. Need to apply certain
   * attributes to aid keyboard navigation.
   */
  _makeMegaMenuAccessible: function (navNode) {

    var textNode = dojo.query("span[id*=text]", navNode.domNode)[0];
    domAttr.set(textNode, "title", textNode.innerHTML);
  },

  /**
   * Out of the box ONEUI header is not accessable. Need to apply certain
   * attributes to aid keyboard navigation.
   */
  _makeHelpMenuAccessible: function (helpNode) {

    domAttr.set(helpNode, "tabindex", "6");
    domAttr.set(helpNode, "role", "button");
    
    dojo.connect(helpNode, "onkeydown", function(evt){
      if (evt.keyCode === dojo.keys.ENTER) {
        dojo.stopEvent(evt);
        dijit.byId("appHelpMenu")._scheduleOpen(evt.target);
      }
    });

  },

  /**
   * Out of the box ONEUI header is not accessable. Need to apply certain
   * attributes to aid keyboard navigation.
   */
  _makePrintMenuAccessible: function (banner) {
    var printNode = banner._settingsNode;
    domAttr.set(printNode, "tabindex", "5");
    domAttr.set(printNode, "role", "button");
    
    dojo.connect(printNode, "onkeydown", function(evt){
      if (evt.keyCode === dojo.keys.ENTER) {
        dojo.stopEvent(evt);
        dijit.byId("appBannerPrintMenu")._scheduleOpen(evt.target);
      }
    });

  },
 //BEGIN, CR00467393, PC
 /**
   * Out of the box ONEUI header is not accessable. Need to apply certain
   * attributes to aid keyboard navigation.
   */
  _makeLogoutMenuAccessible: function (banner) {
    var logoutNode = banner.logoutNode;
    var textNode = banner.logoutTextNode;
    domAttr.set(logoutNode, "tabindex", "7");
    domAttr.set(textNode, "title", textNode.innerHTML);
     domAttr.set(logoutNode, "role", "button");
     
     
   //BEGIN, CR00467721,YP
     dojo.connect(logoutNode, "onkeypress", function(evt){
         if (evt.keyCode === dojo.keys.ENTER) {
           dojo.stopEvent(evt);
           CuramExternalApp.displayContent({"param":[],"pageID":"LogoutWrapper"});
         }
       });
   //END, CR00467721

  }, 
 //END, CR00467393
  /**
   * Our Person Menu is accessed when the user hovers (or clicks) the arrow next
   * to their username. This causes a hovercard to appear, containing the Person
   * menu items. This unusual approach means we need to surpress the default sub
   * menu behaviour and attach hovercard to the arrow.
   */
  _setupUserMenuHoverCard: function(hoverContentNode) {

      var aspectHandle = dojoAspect.after(curam.app.Header.prototype, "_renderUser",function() {
        domClass.add(this.hoverNode, "idxHeaderDropDown");
        var userNode_arrow = this.hoverNode;
        userNode_arrow.id = "curam-extapp_userMenuArrow";
        dojo.connect(userNode_arrow, 'onclick',idx.widget.HoverHelpTooltip.defaultPosition=['below']);
        aspectHandle.remove(); //only want this to run once
        //if (dojo.isIE !== 7) {
          //domAttr.set(userNode_arrow, "onmouseover", "idx.widget.HoverHelpTooltip.defaultPosition=['below']");
          //domAttr.set(userNode_arrow, "onkeypress", "idx.widget.HoverHelpTooltip.defaultPosition=['below']");
        //} else {
          //dojo.connect(userNode_arrow, 'onclick',idx.widget.HoverHelpTooltip.defaultPosition=['below']);
        //}
 
      });

      // The hovercard by default will always display to the right, and only if
      // there is not enough room there, will it then display to the left. This
      // is expected dojo behaviour. We want the card to always display to the
      // left. To achieve this without subclassing, we move the anchor (which
      // the hovercard displays below) to the extreme right edge of the screen
      // and move it and the hovercard back to their orginal position once
      // displayed. This ensures that the hovercard will always render below and
      // right of its anchor as we want.
      if (dojo.isIE !== 7) {
        // move the arrowNode to the right.
        dojoAspect.before(idx.widget.HoverHelpTooltip, "show", function() {
          var arrowNode = dojo.byId("curam-extapp_userMenuArrow");
          var isLTR = CuramExternalApp._oneuiBanner.isLeftToRight();
          domStyle.set(arrowNode, isLTR ?
            {"position":"fixed", "top" : "2px", "right":"21px"} :
            {"position":"fixed", "top" : "2px", "left":"21px"});
        });
        // after rendered, move the arrowNode back.
        dojoAspect.after(idx.widget.HoverHelpTooltip, "show", function() {
          domStyle.set(dojo.byId("curam-extapp_userMenuArrow"), "position", "static");
        });

        // Using the id passed in, when the widget was initialised, locate the
        // hover card holding node, and move it to its correct positon.
        dojoAspect.after(hoverContentNode, "onShow", lang.partial(function(hoverContentID) {
          var hoverHolderID = "idx_widget__MasterHoverHelpTooltip_0";
          if (hoverContentID.lastIndexOf("_") != -1) {
            hoverHolderID = "idx_widget__MasterHoverHelpTooltip_"
                    + hoverContentID.slice(hoverContentID.lastIndexOf("_")+1);
          }
          var arrowNode = dojo.byId("curam-extapp_userMenuArrow");
          var hoverCardWidget = dijit.byId(hoverHolderID);
          
          // Setting focus to the hover card when it is opened.
          hoverCardWidget.focus();
          
          var hoverCardNode = hoverCardWidget.domNode;
          var connectorNode = hoverCardWidget.connectorNode;

          var notchOffSet = (domGeom.position(hoverCardNode).x + domGeom.position(hoverCardNode).w)
                            - (domGeom.position(connectorNode).x + domGeom.position(connectorNode).w/2);
          var cardRightPosition = domGeom.position(dojo.body()).w
                              - (domGeom.position(arrowNode).x + domGeom.position(arrowNode).w/2)
                              - notchOffSet;
          domStyle.set(hoverCardNode, {"left":"auto", "right":cardRightPosition+"px"});
          // One UI "settings" menu is used for printing
          var printMenuExists = CuramExternalApp._oneuiBanner._settingsNode ? true : false;
          // rightVal += printMenuExists ? domGeom.getMarginBox(CuramExternalApp._oneuiBanner._settingsNode).w : 0;

          // There is a difference of 14px between the right and left values.
          // This differece results from the margin-left property
          // selector: .oneui .idxOneuiHoverHelpTooltipBelow
          // in \ibmidxtk\idx\themes\oneui\idx\oneui\HoverHelpTooltip.css
          var isLTR = CuramExternalApp._oneuiBanner.isLeftToRight();
          domStyle.set(dojo.byId(hoverHolderID), isLTR ?
            {"left":"auto", "right": cardRightPosition + "px"} :
            {"right":"auto", "left": (cardRightPosition+14) + "px"});
        }, hoverContentNode.id));
      }

  },

  /**
   * We need a custom class name on the menu to safely alter its look without
   * affecting other widgets.
   *
   * The dom for the help menu is rebuilt by ONEUI each time the user opens the
   * menu. We therefore need to add our custom class name each time the menu is
   * open.
   */
  _addHelpMenuCustomClass: function(){
    var node = dijit.byId("appHelpMenu")._popupWrapper;
    if (!domClass.contains(node, "oneuiHeaderGlobalActionsMenu_help")){
      domClass.add(node, "oneuiHeaderGlobalActionsMenu_help");
    }
  },
  
  /**
   * Displays the specified page in a modal.
   */
  displayMegaMenuItemInModal: function(requestObj) {
    console.log(requestObj);
  },

  /**
   * When using JAWS12 with IE8, it causes the browser to crash whenever JAWS is
   * trying to read help or mega menu items who's total innerText length is
   * greater than 255 characters. As a fix for this, we need to temporarly
   * shorten the innertext of these menu items as JAWS is reading them. Once
   * JAWS starts to read the shorten version, we can restore the innerTexts
   * orginal value. Therefore, visually, the menu item text does not appear to
   * change. This change only occurs when menu items gains and loses focus or
   * when it is clicked and only happens for menu items that exceed the
   * character limit.
   */
  _preventJAWSCrashClick: function(widget) {

    var textCell = dojo.query("#" + widget.id + "_text", widget)[0];
    if(!textCell.isModified) {
      //As JAWS crashes when the innerText of the focused node contains more
      //than 255 characters, we need to make sure that the sum of each
      //individual text holding element, is less than this limit.
      dojo.query(".wtfoneui", textCell).forEach(function(textDiv) {
        textDiv.oldInnerText = textDiv.innerText;
        if (domClass.contains(textDiv, "MMtitle")) {
          textDiv.innerText = textDiv.innerText.substring(0, 229).concat("...");
        } else {
          var titleTextLen = textDiv.previousSibling;
          while (!domClass.contains(titleTextLen, "MMtitle")) {
            titleTextLen = titleTextLen.previousSibling;
          }
          titleTextLen = titleTextLen.innerText.length;
          //Need to account for the title texts length.
          var truncate = Math.min(250 - titleTextLen, Math.max(10, textDiv.innerText.length - titleTextLen));
          textDiv.innerText = textDiv.innerText.substring(0, truncate).concat("...");
        }
      });
      textCell.isModified = true;

      //Restore orginal value.
      textCell.innerModdedTimer && clearTimeout(textCell.innerModdedTimer);
      textCell.innerModdedTimer = setTimeout(dojo.partial(function(widget){
        if(textCell.isModified) {
          dojo.query(".wtfoneui", textCell).forEach(function(textDiv) {
            textDiv.innerText = textDiv.oldInnerText;
          });
          textCell.isModified = false;
          textCell["innerModdedTimer"] = undefined;
          try {
            delete textCell.innerModdedTimer;
          } catch (e) {/*IE7 doesnt like me delete the property*/}
        }
      }, widget), 2);
    }

  },

  /**
   * When using JAWS12 with IE8, it causes the browser to crash whenever JAWS is
   * trying to read help or mega menu items who's total innerText length is
   * greater than 255 characters. As a fix for this, we need to temporarly
   * shorten the innertext of these menu items as JAWS is reading them. Once
   * JAWS starts to read the shorten version, we can restore the innerTexts
   * orginal value. Therefore, visually, the menu item text does not appear to
   * change. This change only occurs when menu items gains and loses focus or
   * when it is clicked and only happens for menu items that exceed the
   * character limit.
   */
  _preventJAWSCrashFocus: function(widget) {

    //Node holding the menu items text elements
    var textCell = dojo.query("#" + widget.id + "_text", widget)[0];
    if(!textCell.isModified) {
      //As JAWS crashes when the innerText of the focused node contains more
      //than 255 characters, we need to make sure that the sum of each
      //individual text holding element, is less than this limit.
      dojo.query(".wtfoneui", textCell).forEach(function(textDiv) {
        textDiv.oldInnerText = textDiv.innerText;
        if (domClass.contains(textDiv, "MMtitle")) {
          textDiv.innerText = textDiv.innerText.substring(0, 229).concat("...");
        } else {
          var titleTextLen = textDiv.previousSibling;
          while (!domClass.contains(titleTextLen, "MMtitle")) {
            titleTextLen = titleTextLen.previousSibling;
          }
          titleTextLen = titleTextLen.innerText.length;
          //Need to account for the title texts length.
          var truncate = Math.min(250 - titleTextLen, Math.max(10, textDiv.innerText.length - titleTextLen));
          textDiv.innerText = textDiv.innerText.substring(0, truncate).concat("...");
        }
      });
      textCell.isModified = true;

      //Either the orginal value will be restored by this timeout function,
      //or it will be restored when the menu item loses focus and our
      //handler deals with it.
      textCell.innerModdedTimer && clearTimeout(textCell.innerModdedTimer);
      textCell.innerModdedTimer = setTimeout(dojo.partial(function(widget){
        if(textCell.isModified) {
          dojo.query(".wtfoneui", textCell).forEach(function(textDiv) {
            textDiv.innerText = textDiv.oldInnerText;
          });
          textCell.isModified = false;
          textCell["innerModdedTimer"] = undefined;
          try {
            delete textCell.innerModdedTimer;
          } catch (e) {/*IE7 doesnt like me deleting the property*/}
        }
      }, widget), 2);
    }
  },

  /**
   * When using JAWS12 with IE8, it causes the browser to crash whenever JAWS is
   * trying to read help or mega menu items who's total innerText length is
   * greater than 255 characters. As a fix for this, we need to temporarly
   * shorten the innertext of these menu items as JAWS is reading them. Once
   * JAWS starts to read the shorten version, we can restore the innerTexts
   * orginal value. Therefore, visually, the menu item text does not appear to
   * change. This change only occurs when menu items gains and loses focus or
   * when it is clicked and only happens for menu items that exceed the
   * character limit.
   */
  _preventJAWSCrashBlur: function(widget) {

    //Node holding the menu items text elements
    var textCell = dojo.query("#" + widget.id + "_text", widget)[0];
    textCell.innerModdedTimer && clearTimeout(textCell.innerModdedTimer);
    //Restore orginal value, if it has been modified.
    if(textCell.isModified) {
      dojo.query(".wtfoneui", textCell).forEach(function(textDiv) {
        textDiv.innerText = textDiv.oldInnerText;
      });
      textCell.isModified = false;
    }
  },

  /**
   * Added as part of the skip link fix TEC-15122
   * Allows screen readers to 'jump' straight to main content of the page,
   * avoiding having to read through all the navigational elements of the page.
   *
   * The code below is replicated from curam.util.skipLinkFocus()
   * Due to layering issues we replicated the function rather than pull in
   * util.js file. Should remove this once implement proper application wide
   * layering.
   */
  _skipLinkFocus: function(destId) {
    destId = destId || 'app-content';
    // BEGIN, 149093, MR
    dojo.setAttr(destId, "tabIndex", -1);
    // END, 149093
    var dest = dojo.byId(destId);
    if (dest) {
      dest.focus();
    }
  },
  
  /**
   * TEC-16453. Skiplink should become visible when focused (i.e. a user tabs on it)
   * and it should be visible only when it has focus, so it should hide again when 
   * the user tabs off it.
   */
  _showHideSkipLink: function(e) {
    var skipLink = dojo.byId("skipLink");
    if (skipLink) {
      var skipLinkDiv = skipLink.parentNode;
      if (e.type == "focus" && domClass.contains(skipLinkDiv, "hidden")) {
        domClass.remove(skipLinkDiv, "hidden");
      } else if (e.type == "blur" && !domClass.contains(skipLinkDiv, "hidden")) {
        domClass.add(skipLinkDiv, "hidden");
      }
    }
  },
  
/*
  print: function() {
    // if the "curam-iframe" exists
    var iframe = query("iframe.curam-iframe", "app-content")[0];
    console.log("PRINTING IFRAME:" + iframe);
    if (iframe) {
      if (dojo.isIE < 9) {
        iframe.contentWindow.document.execCommand("print", false, null);
      } else {
        iframe.contentWindow.print();
      }
    } else {
      window.print();
    }
  }
*/

  print: function() {
    // if the "curam-iframe" exists
    var iframe = query("#app-content iframe.curam-iframe")[0];
    console.log("PRINTING IFRAME:" + iframe);
    if (iframe) {
      if (dojo.isIE < 11) {
        console.log("# IE: " + dojo.isIE + ", calling iframe.contentWindow.document.execCommand");
        iframe.contentWindow.document.execCommand("print", false, null);
      } else {
        console.log("# Calling iframe.contentWindow.print()");
        iframe.contentWindow.print();
      }
    } else {
      console.log("# Calling window.print()");
      window.print();
    }
  },
  
  /**
   * Create the div container with the script to start the session timeout warning validation.
   */
  //BEGIN, 18955, BD
  createSessionTimeoutWarningContainer: function(sessionTimeoutWarningScript){
	var timeoutContainer = window.document.getElementById('timeout-warning-container');
	if (timeoutContainer == null){
		var containerDOM = query('.curam-extapp');
		var wrapperTimeoutWarningDIV = domConstruct.create('div',{
			                          'id': 'timeout-warning-container'},containerDOM[0]);
	    var warningSessionScript = domConstruct.create('script',{'type':'text/javascript'}, wrapperTimeoutWarningDIV );
	    warningSessionScript.innerHTML =  sessionTimeoutWarningScript;
	}
  }
 //END, 189558, BD

  });
});  
