<?xml version="1.0" encoding="UTF-8"?> <jsp:root xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:curam="http://www.curamsoftware.com/curam" xmlns:cing="http://www.curamsoftware.com/curam/jde/client/curam-ng" xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"> <curam:userPreferences/> <jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/> <jsp:directive.page import="curam.omega3.user.UserPreferences"/> <jsp:directive.page import="curam.util.client.jsp.JspUtil"/> <jsp:directive.page import="curam.util.client.jsp.FileEditUtil"/> <jsp:directive.page import="curam.omega3.util.CDEJResources"/> <jsp:directive.page import="curam.util.common.util.JavaScriptEscaper"/> <jsp:directive.page buffer="32kb" isErrorPage="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" isELIgnored="false" language="java" errorPage="/__o3ErrorPage.do"/> <jsp:text> <![CDATA[<?xml version="1.0" encoding="UTF-8" ?><!DOCTYPE html>]]> </jsp:text> <jsp:scriptlet> <![CDATA[ UserPreferences prefs=UserPreferencesFactory.getUserPreferences(session); String locale=prefs.getLocale().toString(); pageContext.setAttribute("pageLocale", locale); pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL(1)); pageContext.setAttribute("feModalTitle", JavaScriptEscaper.escapeText(CDEJResources.getProperty("curam.omega3.i18n.FileEdit","Window.WFE.Dialog.Title"))); pageContext.setAttribute("feSearchBtnCaption", JavaScriptEscaper.escapeText(CDEJResources.getProperty("curam.omega3.i18n.FileEdit", "Btn.Search.Text"))); pageContext.setAttribute("feCommitBtnCaption", JavaScriptEscaper.escapeText(CDEJResources.getProperty("curam.omega3.i18n.FileEdit", "Btn.Commit.Text"))); pageContext.setAttribute("testModeEnabled", curam.omega3.util.DataTestIDUtils.isTestModeEnabled()); ]]> </jsp:scriptlet> <curam:userPreferences localeCode="${pageScope.pageLocale}"/> <c:set scope="request" value="file-edit-dialog" var="pageId"/> <html role="region" aria-label="Modal Frame"> <head> <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/> <link href="${pageScope.o3__serverURL}themes/curam/css/curam_main.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}themes/${applicationScope.theme}/css/${applicationScope.theme}_main.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}CDEJ/css/custom.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}themes/v6/css/v6_high_contrast.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dijit/themes/soria/soria.css" media="all" type="text/css" rel="stylesheet"/> <link href="${pageScope.o3__serverURL}themes/soria/css/soria_main.css" media="all" type="text/css" rel="stylesheet"/> <script src="${pageScope.o3__serverURL}CDEJ/jscript/deployJava.js">//dummy</script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dojo/dojo.js" data-dojo-config="async:0,parseOnLoad:true,isDebug:false">//script content</script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/dojo.layer.js">// script content</script> <script type="text/javascript">
   <jsp:scriptlet>pageContext.setAttribute("appID", curam.omega3.util.UserUtils.getApplicationID((javax.servlet.http.HttpServletRequest) pageContext.getRequest()));</jsp:scriptlet>
   <!--
     if(pageContext.getAttribute("htmlDirection").equals("rtl")) {
       pageContext.setAttribute("classDirection", "rtl");
     } else {
       pageContext.setAttribute("classDirection", "");
     }-->
     <jsp:scriptlet>
     JspUtil.outputJSModulePaths(pageContext);
     </jsp:scriptlet>
   </script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/cdej-cm.js" type="text/javascript">//Comment</script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/cdej.js" type="text/javascript">//Comment</script> <curam:jsUserPreferences/> <script type="text/javascript">
     var jsPageID="file-edit-dialog";
     require(["curam/core-uim", "curam/dialog", "curam/util", "curam/util/onLoad"]);
     var jsBaseURL = curam.util.retrieveBaseURL();
     dojo.global.jsScreenContext = new curam.util.ScreenContext();
     dojo.global.jsScreenContext.setContext('MODAL');
     dojo.global.jsScreenContext.clear('ACTION|ERROR|RESOLVE');
     curam.dialog.initModal('file-edit-dialog');
     var startTrigger = curam.util.getTopmostWindow()
          .dojo.subscribe("/curam/dialog/displayed", this, canStartWordSession);
     var endTrigger = curam.util.getTopmostWindow()
                  .dojo.subscribe("/curam/dialog/closed", this, endWordSession);

     var fedRefs = {};
     var searchPageOpen = false;
     var myself = null;
     
     var fnSavePrompt = function() {
       return confirm(getMessage("Msg.Save.Prompt"));
     }

     function canStartWordSession(openerId) {
       fedRefs = fedRefs || {};
       if (getParent() != null) {
         var base = fedRefs.parentRef;
         var dialogRef = base &amp;&amp; base.getDialogRef ? base.getDialogRef() : null;
         var dialogId = dialogRef ? dialogRef.id : curam.dialog._id;
         if (openerId == dialogId) {
           if (base.addEventListener) {
             base.addEventListener("beforeunload", base.warnInterrupt, false);
           } else {
             base.attachEvent("onbeforeunload", base.warnInterrupt);
             base.dropInterruptListener = base.dropListenerObsolete;
           }
           base.addCloser();
           base.addFedRef(window);
           //window.postMessage({ type: "PAGE_2_EXTN", info: "canStart"}, "*");           
         } //else report error - cannot proceed!
       }
     }

     function endWordSession(closerId) {
       if (closerId == curam.dialog._id) {
         // duplicate, should be part of modal close or landing
         // window.postMessage({ type: "PAGE_2_EXTN", cmd: "QUIT"}, "*");
         curam.util.getTopmostWindow().dojo.unsubscribe(startTrigger);
         curam.util.getTopmostWindow().dojo.unsubscribe(endTrigger);
       }
     }

     function autoCloseSearch() {
       if (searchPageOpen &amp;&amp; fedRefs.searchPageRef) {
         fedRefs.searchPageRef.closeButtonNode.click();
       }
     }

     function getParent() {
       // search hierarchy for a window with a token
       if (!fedRefs.parentRef || typeof fedRefs.parentRef == "undefined" || fedRefs.parentRef.getToken == "undefined") {
         var hierarchy = curam.dialog._getDialogHierarchy();
         if (hierarchy) {
           for (var i = 0; i &lt; hierarchy.length; i++) {
             if (hierarchy[i].getToken) {
               fedRefs.parentRef = hierarchy[i];
               break;
             }
           }
         }
         if (!fedRefs.parentRef) {
           console.error("Parent reference has been lost!");
         }
       }
       return fedRefs.parentRef;
     }

     function getMessage(msgKey) {
       var holder = document.getElementById(msgKey);
       return (holder ? holder.innerHTML : msgKey);
     }

     function getConfig(configKey) {
       var holder = document.getElementById(configKey + ".L") 
                      || document.getElementById(configKey);
       return (holder ? holder.innerHTML : "#");
     }

     function openSearchPage() {
       if (!searchPageID || searchPageID == "") {
         return false;
       }
       fedRefs.searchPageRef = null; //release if not yet
       searchPageOpen = true;
       var searchUrl = searchPageID + "Page.do?"+ linkParams;
       fedRefs.searchPageRef = curam.util.showModalDialogWithRef(searchUrl);
       require(["dojo/aspect"], function(asp){
         fedRefs.searchAspect = 
           asp.before(fedRefs.searchPageRef, "onCancel",
              function() {
                fedRefs.searchPageRef = null;
                searchPageOpen = false;
                fedRefs.searchAspect.remove();
                fedRefs.searchAspect = null;
           });
       });
       return false;
     }

     function enableSearch() {
       var btn = curam.util.getTopmostWindow().document.getElementById("searchpageBtn");
       if (btn.addEventListener) {
         btn.addEventListener("click", openSearchPage, false);
       } else {
         btn.attachEvent("onclick", openSearchPage);
       }
       dojo.removeClass(btn, "disabled-button");
     }

     curam.util.onLoad.addPublisher(function(context){
       context.pageID = "FileEditControlPanel";
       context.title = '${pageScope.feModalTitle}';
       context.height = "300";
     });
     dojo.addOnLoad(function() {
       fedRefs.returnSubscription =
         dojo.subscribe("curam/fileedit/returntoapp", function() {
           var rs = dojo.unsubscribe(fedRefs.returnSubscription);
           var ss = dojo.unsubscribe(fedRefs.stopSubscription);
           var cs = dojo.unsubscribe(fedRefs.closeSubscription);
           rs = cs = ss = null;
           myself.closeButtonNode.click();
         });
       fedRefs.stopSubscription = 
         dojo.subscribe("curam/fileedit/stopclose", function() {
           if (getParent() != null) {
             fedRefs.parentRef.dojo.publish("curam/fileedit/stopcloseable");
           }
           dojo.publish("curam/fileedit/enablesearch");
         });
       fedRefs.closeSubscription =
         dojo.subscribe("curam/fileedit/makecloseable", function() {
           if (getParent() != null) {
             fedRefs.parentRef.dojo.publish("curam/fileedit/makecloseable");
           } else {
             if (myself) {
               myself._setClosableAttr(true);
             }
           }
         });
  
       curam.util.onLoad.execute();
       dojo.publish('/curam/page/loaded');
   });
  </script> <style>
   #statusDisplay {
     border:1px navy solid;
     margin:20px 5px 40px 10px;
     padding-left:10px;
     background: whitesmoke;
   }
   .record {
     padding:2px 5px 4px 18px;
     vertical-align:middle;
     background-repeat: no-repeat;
   }
   .ECL {
     background-image: url("${pageScope.o3__serverURL}themes/v6/images/icon_error.png");
   }
   .ICL {
     background-image: url("${pageScope.o3__serverURL}themes/v6/images/icon_information.png");
   }
   .WCL {
     background-image: url("${pageScope.o3__serverURL}themes/v6/images/warning_icon.png");
     background-size: 16px 16px;
   }
   #commitDocChangesBtnIE.disabled-button {
     border: 1px solid #777677;
     color: #777677 !important;
     background-color: inherit !important;
   }
   #commitDocChangesBtnIE.disabled-button span.middle {
     color: #777677;
   }
   </style> <title>${pageScope.feModalTitle}</title> </head> <body id="Curam_file-edit-dialog" class="${classDirection} ${appID} basic modal ${htmlLanguage} curam en soria bx--uim-modal spm-component" role="document" aria-label="Modal Frame"> <div class="page-header"> <div class="page-title-bar"> <div class="title"> <h2 title="${pageScope.feModalTitle}"> <span>${pageScope.feModalTitle}</span> </h2> </div> </div> </div> <jsp:scriptlet> String agent = request.getParameter("agent"); String chromeMessaging = CDEJResources.getConfigProperty("curam.omega3.ApplicationConfiguration", "fileedit.chrome.messaging.enabled"); boolean chromeWithApplet = false; if (agent.equals("chrome") &amp;&amp; chromeMessaging != null &amp;&amp; chromeMessaging.equals("false")) { chromeWithApplet = true; } if (agent.equals("chrome") &amp;&amp; !chromeWithApplet) { </jsp:scriptlet> <jsp:include page="file-edit-chrome.jsp"/> <jsp:scriptlet> } else { </jsp:scriptlet> <jsp:include page="file-edit-ie.jsp"/> <jsp:scriptlet> if(chromeWithApplet) { </jsp:scriptlet> <jsp:include page="file-edit-chrome-applet.jsp"/> <jsp:scriptlet> } } FileEditUtil.outputMessages(pageContext); FileEditUtil.outputMessagingConfig(pageContext); </jsp:scriptlet> <div region="bottom" id="actions-panel"> <div class="action-set buttons"> <div id="searchButtonDiv" style="display:none"> <curam:button name="search" cssClass="submit replace" type="submit" dataTestidComponentPrefix="link" labelProp="Btn.Search.Text">${pageScope.feSearchBtnCaption}</curam:button> </div> <script type="text/javascript">
          <jsp:text><![CDATA[dojo.addOnLoad(function(){curam.util.replaceSubmitButton('search', undefined, undefined, "searchpageBtn")});]]></jsp:text>
        </script> <curam:button name="commit" cssClass="submit replace curam-default-action" type="submit" dataTestidComponentPrefix="link" labelProp="Btn.Commit.Text">${pageScope.feCommitBtnCaption}</curam:button> <script type="text/javascript">
          <jsp:text><![CDATA[dojo.addOnLoad(function(){curam.util.replaceSubmitButton('commit', undefined, undefined, "commitDocChangesBtnIE")});]]></jsp:text>
        </script> </div> </div> </body> </html> </jsp:root>