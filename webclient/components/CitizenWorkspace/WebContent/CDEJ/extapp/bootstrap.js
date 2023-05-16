// NB: This is a bootstrap JS file for the external application. It does not
// contain classes\modules. Simply including it will execute "global" bootstrap
// code.

// Used by curam.util.getTopWindow()       
window.__extAppTopWin = true;

require([
  /* START: modules used during bootstrap */
  "dojo/domReady",
  "dojo/ready",
  "dojo/_base/window",
  "dojo/_base/fx", 
  /* END: modules used during bootstrap */
  /* START: pre-load modules */
  "dojo/dom-geometry",
  "dojo/fx",
  "dojo/aspect", /*used by banner*/
  "dijit/form/Button",
  "dijit/form/ToggleButton",
  "dijit/layout/ContentPane",
  "dijit/TooltipDialog",
  "dijit/layout/BorderContainer",
  "curam/util",
  "curam/util/ScreenContext",
  /* END: modules used during bootstrap */
  /* START: pre-load modules */
  /* required by some code in cdej.js, but not delcared there! So, including
     here for now. */
  "curam/util/onLoad",
  "curam/util/UIMFragment",
  "curam/layout/TabContainer",
  /* The optimal browser message widget*/
  "curam/widget/OptimalBrowserMessage",
  /* The main ExternalApplication widget. */
  "curam/app/ExternalApplication"
  /* END: pre-load modules */
], function(domReady, ready, dojoWindow, fx) {

  // TODO: this is also defined in application.jspx...fix this!
  dojo.global.jsScreenContext = 
    new curam.util.ScreenContext("EXTAPP");
  domReady(function() {
    console.log("DOM Ready");
  });
  ready(function() {
    console.log("dojo/ready !!");
    // TODO: this doesn't work in IE8 yet
    // TODO: move fade in to ExternalApplication.js\ExternalApplication.css
    // rather than global startup behaviour
    fx.fadeIn({
      node: dojoWindow.body(),
      delay: 500,
      duration: 1000
    }).play();  
  });
});

function loadDijitFragmentInMainContent() {
  var fragmentModules = [
    "dijit/form/TextBox",
    "idx/form/TextBox",
    "dijit/form/DateTextBox",
    "idx/form/DateTextBox",
    "dijit/form/Select",
    "idx/form/Select"
  ];
  require(fragmentModules, function() {
    displayContent({
      pageID: "SampleFragmentFromScriptlet"
    });
  });
}

function displayContent(args) {
  dijit.byId("curam-app")._initNavBar(args.pageID, function() {
    dijit.byId("curam-app").displayContent(args);
  });
}

function getAppConfig() {
  var mainAppWidget =  dijit.byId("curam-app");
  if (mainAppWidget) {
    return mainAppWidget._appConfig;
  }
}

function loadMegaMenuItem(megaMenuItem) {
  megaMenuItem.displayNavBar = false;
  displayContent(megaMenuItem);
}

function loadMegaMenuItemInModal(megaMenuItem) {
  dijit.byId("curam-app").displayMegaMenuItemInModal(args);
}

function openModal(url, widthAndHeight) {
  require(['curam/util/UimDialog'], function(uimDialog) {
    var href = url,
        extAppContext = new curam.util.ScreenContext('EXTAPP');

    // set a proper screen context value
    href = curam.util.replaceUrlParam(href, "o3ctx", extAppContext.getValue());

    if (widthAndHeight) {
      uimDialog.openUrl(href, widthAndHeight);
    } else {
      uimDialog.openUrl(href);
    }
  });
}

