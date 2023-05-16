// Used by curam.util.getTopWindow()
window.__extAppTopWin = true;

var modules = [
"dojo/domReady",
"dojo/ready",
"dijit/registry",
"dojo/parser",
"dojo/aspect",
"dojo/dom",
"dojo/dom-class",
"dijit/PopupMenuBarItem",
"dijit/MenuItem",
"idx/widget/MenuBar",
"idx/widget/Menu",
"idx/widget/Header",
"dijit/form/Button",
"dojo/dom-geometry",
"dojo/fx",
"idx/widget/MenuDialog",
"idx/widget/MenuHeading",
"idx/widget/HoverHelpTooltip",
"dijit/layout/ContentPane",
"dojox/layout/ContentPane",
"dijit/TooltipDialog",
"dijit/layout/BorderContainer",
/*CURAMDEP: Curam modules that will be needed for the new layer below this line (apart from uim fragment above) */
"curam/util",
"curam/util/ScreenContext",
"curam/util/onLoad",
"curam/util/UimDialog",
"curam/util/UIMFragment"

];

require(modules, function(domReady, ready, dijitRegistry, dojoParser, dojoAspect, dojoDom, dojoDomClass) {

    dojo.global.jsScreenContext =
      new curam.util.ScreenContext("EXTAPP");

    console.log("before registering dojo/domReady function");
    domReady(function() {
      console.log("DOM Ready");
      //var iframe = dojoDom.byId("content-iframe");
      // for now just change this URL to load up
      //iframe.src = "en_US/ShowAllCasesPage.do?o3ctx=1048576";

    });
    console.log("after registering dojo/domReady function");

    console.log("before registering dojo/ready function");
    ready(function() {
      console.log("dojo/ready !!");
      var loadFromStorage = dojo.global.loadFromStorage || null;

      loadAppContentPanel(loadFromStorage);
      //loadFragmentInMainContent();
      //loadFragmentInMainContentUsingSCriptletRedirect();

      // TODO: this doesn't work in IE8 yet
      require(["dojo/_base/window", "dojo/_base/fx"], function(win, fx){
        fx.fadeIn({
          node: win.body(),
          delay: 500,
          duration: 1000
        }).play();
      });
    });
    console.log("after registering dojo/ready function");
});

function loadLandingPageInMainContent() {
  if (curam.config.landingPage) {
    curam.util.UIMFragment.get({
      pageID: curam.config.landingPage,
      params: {testParam1: "1", testParam2: "2"},
      targetID: "app-content"
    });
  } else {
    throw "ERROR: Landing page not set correctly: "
          + curam.config.landingPage;
  }
}
function loadIFrameFragmentInMainContent() {
  curam.util.UIMFragment.get({
    pageID: "UIMIFrameWrapper",
    params: {uimPageID: "ShowAllCases", testParam2: "2"},
    targetID: "app-content"
  });
}
function loadFragmentInMainContentUsingScriptletRedirect() {
  curam.util.UIMFragment.get({
    pageID: "SampleFragmentFromScriptletRedirect",
    params: {testParam1: "1", testParam2: "2"},
    targetID: "app-content"
  });
}
function loadFragmentInMainContent() {
  var fragmentModules = [
    "dijit/form/TextBox",
    "idx/widget/form/TextBox",
    "dijit/form/DateTextBox",
    "idx/form/DateTextBox",
    "dijit/form/Select",
    "idx/widget/form/Select"
  ];
  require(fragmentModules);
  require(fragmentModules, function() {
    curam.util.UIMFragment.get({
      pageID: "SampleFragmentFromScriptlet",
      params: {testParam1: "1", testParam2: "2"},
      targetID: "app-content"
    });
  });
}

function loadMainContentFromStorage() {
  if (browserHasLocalStorage()) {
    var url = localStorage.getItem("main-content-url");
    if (url && url.length > 0) {
      dijit.byId("app-content").attr("href", url);
      return true;
    }
  }
  return false;
}

function browserHasLocalStorage() {
  try {
    return "localStorage" in window && window['localStorage'] !== null;
  } catch (e) {
    return false;
  }
}

function appContentLoadStart() {

  var url = dijit.byId("app-content").attr("href");

  if (browserHasLocalStorage()) {
    localStorage.setItem("main-content-url", url);
  }
  return "";
}


function loadAppContentPanel(forceLoadFromStorage) {
  var loadFromStorageSuccess = false;
  if (forceLoadFromStorage) {
    loadFromStorageSuccess = loadMainContentFromStorage();
  }
  if (!loadFromStorageSuccess) {
    loadLandingPageInMainContent();
  }
}
