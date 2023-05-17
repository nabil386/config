<jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/>
<jsp:directive.page import="curam.omega3.user.UserPreferences"/>
<jsp:directive.page import="curam.util.client.jsp.JspUtil"/>
<jsp:directive.page import="curam.util.client.jsp.FileEditUtil"/>
<jsp:directive.page import="curam.omega3.util.CDEJResources"/>
<jsp:directive.page import="curam.util.common.util.JavaScriptEscaper"/>
<jsp:directive.page isErrorPage="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" errorPage="/__o3ErrorPage.do" buffer="32kb"/>
<jsp:scriptlet> UserPreferences prefs=UserPreferencesFactory.getUserPreferences(session); String locale=prefs.getLocale().toString(); pageContext.setAttribute("pageLocale", locale); String ua=request.getHeader( "User-Agent" ); pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL(1)); pageContext.setAttribute("windowTitle", JavaScriptEscaper.escapeText( CDEJResources.getProperty("curam.omega3.i18n.FileEdit", "Window.WFE.Base.Title"),true)); pageContext.setAttribute("feInterruptMsg", JavaScriptEscaper.escapeText( CDEJResources.getProperty("curam.omega3.i18n.FileEdit", "Msg.Browser.Interrupt"),true)); pageContext.setAttribute("failedLoadMsg", JavaScriptEscaper.escapeText( CDEJResources.getProperty("curam.omega3.i18n.FileEdit", "Window.WFE.Failed.Load.Message"),true)); final String token=curam.util.client.security.CSRFToken.getToken(session); pageContext.setAttribute("tkn", token);
</jsp:scriptlet>
<head> <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/> <meta http-equiv="Pragma" content="no-cache"/> <meta http-equiv="Cache-Control" content="no-cache"/> <meta http-equiv="Expires" content="0"/> <link href="${pageScope.o3__serverURL}themes/${applicationScope.theme}/css/${applicationScope.theme}_main.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}themes/curam/css/curam_main.css" media="all" type="text/css" rel="stylesheet"/> <!--[if IE]><link href="${pageScope.o3__serverURL}themes/${applicationScope.theme}/css/${applicationScope.theme}_cc_IE.css" media="all" type="text/css" rel="stylesheet"/><![endif]--> <!--[if IE 9]><link href="${pageScope.o3__serverURL}themes/${applicationScope.theme}/css/${applicationScope.theme}_cc_IE9.css" media="all" type="text/css" rel="stylesheet"/><![endif]--> <!--[if !IE]>--> <link href="${pageScope.o3__serverURL}themes/${applicationScope.theme}/css/${applicationScope.theme}_cc_notIE.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}themes/${applicationScope.theme}/css/${applicationScope.theme}_cc_IE10.css" media="all" type="text/css" rel="stylesheet"/> <!--<![endif]--> <link href="${pageScope.o3__serverURL}CDEJ/css/custom.css" media="all" type="text/css" rel="stylesheet"/> <!--[if IE]><link href="${pageScope.o3__serverURL}CDEJ/css/custom_cc_ie.css" media="all" type="text/css" rel="stylesheet"/><![endif]--> <!--[if IE 8]><link href="${pageScope.o3__serverURL}CDEJ/css/custom_cc_ie8.css" media="all" type="text/css" rel="stylesheet"/><![endif]--> <!--[if IE 9]><link href="${pageScope.o3__serverURL}CDEJ/css/custom_cc_ie9.css" media="all" type="text/css" rel="stylesheet"/><![endif]--> <!--[if !IE]>--> <link href="${pageScope.o3__serverURL}CDEJ/css/custom_cc_notie.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}CDEJ/css/custom_cc_ie10.css" media="all" type="text/css" rel="stylesheet"/> <!--<![endif]--> <link href="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dijit/themes/soria/soria.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}themes/soria/css/soria_main.css" media="all" type="text/css" rel="stylesheet"/> <link rel="shortcut icon" href="${pageScope.o3__serverURL}themes/classic/images/icons/curam.ico"/> <link rel="icon" href="${pageScope.o3__serverURL}themes/classic/images/icons/curam.ico"/> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dojo/dojo.js" data-dojo-config="async:0,parseOnLoad:true,isDebug:false">//script content</script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/dojo.layer.js">// script content</script> <script type="text/javascript">
    <jsp:scriptlet>JspUtil.outputJSModulePaths(pageContext);</jsp:scriptlet>
  </script> <jsp:scriptlet>JspUtil.outputJSLocalisedValues(pageContext);</jsp:scriptlet> <script src="${pageScope.o3__serverURL}CDEJ/jscript/cdej-cm.js" type="text/javascript">//Comment</script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/cdej.js" type="text/javascript">//Comment</script> <curam:jsUserPreferences/> <script>
    require(["curam/core-uim", "curam/util/onLoad"]);
    var jsPageID ="${pageScope.pID}";
    <jsp:scriptlet>
    final curam.omega3.taglib.ScreenContext sc = new curam.omega3.taglib.ScreenContext();
    sc.set(request.getParameter(curam.omega3.taglib.ScreenContext.CTX_PARAM));
    if (sc.hasContextBits(curam.omega3.taglib.ScreenContext.MODAL)) {
      pageContext.setAttribute("myContext", "MODAL");
      out.print("require(['curam/dialog', 'dojo/aspect'],");
      out.print("function(dial, asp) {");
      out.print("asp.after(curam.dialog, 'checkClose',");
      out.print("function(result) {");
      out.print("if (result != false) { unInitBase(); }");
      out.print("return result; }, false); });");
    } else {
      pageContext.setAttribute("myContext", "TAB");
    }
    </jsp:scriptlet>

    var baseRefs = {};
    var wfeBasePage = window;
    var finalLanding = "";
    var msgTimer = null;
    var msgTimeout = null;
    var jsBaseURL = curam.util.retrieveBaseURL();
    sessionStorage.setItem("fileEditWigetDialog", "true");

    dojo.global.jsScreenContext = new curam.util.ScreenContext();
    dojo.global.jsScreenContext.setContext('${pageScope.myContext}');
    dojo.global.jsScreenContext.clear("ACTION|ERROR|RESOLVE");
    if (dojo.global.jsScreenContext.hasContextBits('MODAL')) {
      curam.dialog.initModal(jsPageID);
      curam.util.onLoad.addPublisher(function(context){
        context.height = "300";
      });
    }
    curam.util.onLoad.addPublisher(
      function(context){
        context.pageID = jsPageID;
        context.title = '${pageScope.windowTitle}';
    });

    var dropListenerObsolete = function() {
      return wfeBasePage.detachEvent("onbeforeunload", wfeBasePage.warnInterrupt);
    }
    var dropListener = function() {
      return wfeBasePage.removeEventListener("beforeunload", wfeBasePage.warnInterrupt, false);
    }
    var dropInterruptListener = dropListener;

    function openWFEWidget(url) {

      if (wfeBasePage != null) {
        /*
    var dt = new Date();
        if (timeMark && (dt.getTime() - timeMark) > 9000) {
          window.location.reload(true);
        }*/
        require(['curam/util', 'curam/tab'], function(u) {

          wfeBasePage.baseRefs = wfeBasePage.baseRefs || {};
          wfeBasePage.baseRefs.wfeDialogRef = u.showModalDialogWithRef(url);
          var uiController = curam.tab.getTabController();
          uiController._prepareWordLanding();
        });
      } //otherwise something went wrong
      if (msgTimeout) {
        clearInterval(msgTimeout);
      }
      if (msgTimer) {
        clearInterval(msgTimer);
      }
     return false;
    };
    function prepareDialogCancel() {
      prepareDialogClose(true);
    }
    function prepareDialogClose(canceling) {
      var bPage = wfeBasePage || window;
      var bRefs = bPage.baseRefs;
      bPage.dropInterruptListener();
      bRefs.closeAspect.remove();
      bRefs.closeAspect = null;
      bRefs.dialogWindow.autoCloseSearch();
      if (canceling) {
        bRefs.wfeDialogRef._onHideHandler();
      }
      bRefs.dialogWindow.fedRefs = {}; //release parentRef
      bPage.dojo.publish("curam/fileedit/endsession");
    }

    function addCloser() {
      require(['dojo/aspect'], function(asp) {
        var bRefs = wfeBasePage.baseRefs;
        bRefs.closeAspect = 
          asp.before(bRefs.wfeDialogRef, "onCancel", wfeBasePage.prepareDialogCancel);
      }); 
    }
    function getDialogRef() {
      return (wfeBasePage && wfeBasePage.baseRefs) ? wfeBasePage.baseRefs.wfeDialogRef : null;
    }
    function addFedRef(fedRef) {
      wfeBasePage.baseRefs.dialogWindow = fedRef;
      wfeBasePage.baseRefs.dialogWindow.myself = wfeBasePage.baseRefs.wfeDialogRef;
    }

    function warnInterrupt(event) {
      var message = '${pageScope.feInterruptMsg}';
      if (typeof event == 'undefined') {
        event = window.event;
      }
      if (event) {
        event.returnValue = message;
        var bRefs = (wfeBasePage || window).baseRefs;
        if (bRefs.wfeDialogRef != null && bRefs.dialogWindow) {
          bRefs.dialogWindow.postMessage({ type:'PAGE_2_EXTN', cmd:"WARNUNLOAD"}, '*');
        }
      }
      return message;
    };

    function land() {
      if (dojo.global.jsScreenContext.hasContextBits('MODAL')) {
        if (finalLanding != "") {
          require(['curam/util'], function(util){
        var twn = util.getTopmostWindow();
      twn.dojo.publish("curam/fileedit/aspect/release", []);
            var parentWindow = curam.dialog.getParentWindow(window);
            if (parentWindow && parentWindow.location
                                    !== twn.location) {
              curam.dialog.doRedirect(parentWindow, finalLanding, true);
            }
          });
        }
        // close modal dialog in any case
        curam.dialog.closeModalDialog();
      } else {
        if (finalLanding == "") {
          require(['curam/util'], function(util){
            twn.dojo.publish("curam/fileedit/aspect/release", []);
            var twn = util.getTopmostWindow();
          });
      window.history.go(-1);
        } else {
          curam.tab.getTabController().processURL(finalLanding);
        }
      }  
    };

    function forget() {
      require(["curam/tab", "curam/util/Request"],
        function(tab, req) {
          var sId = tab.getCurrentSectionId();
          var descr = tab.getSelectedTab(sId).tabDescriptor;
          return req.post({
               url: "../servlet/PathResolver?r=x&p=/data/tab/close&v="
                                       + encodeURIComponent(descr.toJson()),
               headers: { "Content-Encoding": "UTF-8" },
               handleAs: "text",
               preventCache: true
            });
        });
    }

    function clearSession(callback) {
      var fnThen = callback || function(data) { return true; };
      require(["dojo/request/xhr"], function(xhr) {
        return xhr.get(
      jsBaseURL +"/servlet/FileEdit",
          {
            query: "c=clear&tp=true" + paramString,
            headers: { "X-token": '${pageScope.tkn}' },
            handleAs: "text",
            preventCache:true
          }).then(fnThen);
      });
    }

    function getDefaultPage() {
      require(["curam/tab", "curam/util/Request"],
        function(tab, req) {
          var sId = tab.getCurrentSectionId();
          var tID = tab.getSelectedTab(sId).tabDescriptor.tabID;
          var path = "/config/tablayout/defaultpage[" + tID  + "][" + jsPageID + "]";
          return req.get({
            url: "../servlet/PathResolver?r=&p=" + path,
            headers: { "Content-Encoding": "UTF-8" },
            handleAs: "text",
            load: function(result) {
              if (result != "") {
                var finalHRef = result + "Page.do?nc=" + Date.now();
                if (wfeBasePage.paramString && wfeBasePage.paramString != "") {
                  finalHRef += wfeBasePage.paramString;
                }
                wfeBasePage.finalLanding = finalHRef;
              } else {
                wfeBasePage.finalLanding = "";
              }
              return;
            },
            preventCache: true
          });
        });
    };

    function unInitBase() {
      var bRefs = (window || wfeBasePage).baseRefs;
      if (bRefs && bRefs.listenStopClose) {
        dojo.unsubscribe(bRefs.sessionEnder);
        dojo.unsubscribe(bRefs.listenStopClose);
        dojo.unsubscribe(bRefs.listenMakeCloseable);
      }
      bRefs = {};
      wfeBasePage = null;
      dojo.addOnLoad = function(fnOnLoad) {};
    };
    
    function getToken() {
      return '${pageScope.tkn}';
    }

    dojo.addOnLoad(function() {
      if (landingPage != "") {
        finalLanding = landingPage + "Page.do?nc=" + Date.now();
        if (paramString && paramString != "") {
		//BEGIN, RTC 194820,267326 COF
		finalLanding += paramString;
		//END, RTC 194820,267326 COF
        } 
      } else if (rpuPage != "") {
        finalLanding = rpuPage;
      }
      if (finalLanding == "") {
        getDefaultPage();
      }  
    });
    dojo.addOnLoad(function(){
      curam.ui.UIMPageAdaptor.initialize();
      curam.util.onLoad.execute();
      dojo.publish('/curam/page/loaded');
      if (dojo.global.jsScreenContext.hasContextBits('TAB')) {
        require(["curam/tab"], function(tab) {
          tab.initContent(window,jsPageID);
          tab.initTabLinks(window);
        });
        curam.util.addContentWidthListener("content");
      }
    });
    dojo.addOnLoad(function() {      
      var bRefs = (wfeBasePage || window).baseRefs;
      bRefs.listenStopClose = 
        dojo.subscribe("curam/fileedit/stopcloseable",
            function(){
              bRefs.wfeDialogRef._setClosableAttr(false);
            });
      bRefs.listenMakeCloseable = 
        dojo.subscribe("curam/fileedit/makecloseable",
            function() {
              bRefs.wfeDialogRef._setClosableAttr(true);
              (wfeBasePage || window).forget();
            });
      bRefs.sessionEnder = 
        dojo.subscribe("curam/fileedit/endsession",
            function() {
              var sc = dojo.unsubscribe(bRefs.listenStopClose);
              var mc = dojo.unsubscribe(bRefs.listenMakeCloseable);
              var se = dojo.unsubscribe(bRefs.sessionEnder);
              sc = mc = se = null;
              clearSession(land());
            });
      });
    dojo.addOnLoad(function(){
      var msgBlock = document.getElementById('initialMsg');
      if (msgBlock) {
        msgTimeout = setInterval(function() {
          clearInterval(msgTimeout);
            if (msgTimer) {
              clearInterval(msgTimer);
            }
            msgBlock.innerHTML = "${pageScope.failedLoadMsg}"; 
          }, 12000);
        msgTimer = setInterval(function() {
          msgBlock.innerHTML = msgBlock.innerHTML + " ... "; 
        }, 1000);        
      }
    });
   <jsp:scriptlet>FileEditUtil.outputFileEditBaseJS(pageContext, token);</jsp:scriptlet>
  </script> <title>${pageScope.windowTitle}</title> </head>