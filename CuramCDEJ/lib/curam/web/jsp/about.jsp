<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:curam="http://www.curamsoftware.com/curam" xmlns:cing="http://www.curamsoftware.com/curam/jde/client/curam-ng" xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
<jsp:directive.page contentType="text/html; charset=UTF-8" errorPage="/en/CuramErrorPage.do" isErrorPage="false" language="java" pageEncoding="UTF-8"/>
<jsp:directive.page import="curam.util.client.jsp.JspUtil"/>
<jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/>
<jsp:directive.page import="curam.omega3.user.UserPreferences"/>
<jsp:output omit-xml-declaration="yes"/>
<jsp:text><![CDATA[<!DOCTYPE html>]]></jsp:text>
<curam:userPreferences/>
<html lang="${htmlLanguage}" dir="${htmlDirection}" role="region" aria-label="${landmarkLabel}"> <head> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> <title><jsp:scriptlet><![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("bannermenu.about.title"));]]></jsp:scriptlet></title> <jsp:scriptlet> <![CDATA[ pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL(1)); ]]> </jsp:scriptlet> <link rel="stylesheet" type="text/css" media="screen, print" href="${pageScope.o3__serverURL}themes/v6/css/v6_main.css"/> <link href="${pageScope.o3__serverURL}themes/curam/css/curam_main.css" media="screen" rel="stylesheet" type="text/css"/> <link href="${pageScope.o3__serverURL}themes/curam/fonts/main-ibm-font.css" media="screen" rel="stylesheet" type="text/css"/> <jsp:scriptlet> if(pageContext.getAttribute("htmlDirection").equals("rtl")) { </jsp:scriptlet> <link href="${pageScope.o3__serverURL}themes/curam_rtl/css/curam_about_rtl.css" media="screen" rel="stylesheet" type="text/css"/> <jsp:scriptlet> } </jsp:scriptlet> <link rel="stylesheet" href="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dijit/themes/soria/soria.css"/> <jsp:text><![CDATA[<!--[if IE 8]>]]></jsp:text> <link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_IE8.css"/> <jsp:text><![CDATA[<![endif]-->]]></jsp:text> <jsp:text><![CDATA[<!--[if IE 9]>]]></jsp:text> <link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_IE9.css"/> <jsp:text><![CDATA[<![endif]-->]]></jsp:text> <jsp:scriptlet> UserPreferences prefs = UserPreferencesFactory.getUserPreferences(session); pageContext.setAttribute("landmarkLabel", curam.omega3.util.CDEJResources.getProperty("modal.panel.frame.title")); </jsp:scriptlet> <script type="text/javascript">
          <jsp:scriptlet>JspUtil.outputDojoLoaderConfiguration(pageContext, prefs.getLocale());</jsp:scriptlet>
         </script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dojo/dojo.js">// dummy script content</script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dijit/dijit.js" type="text/javascript">// script content</script> <script type="text/javascript">
          <jsp:scriptlet>curam.util.client.jsp.JspUtil.outputJSModulePaths(pageContext);</jsp:scriptlet>
          var jsPageID="about";
        </script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/cdej.js">// script content</script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/cdej-cm.js">// script content</script> <script type="text/javascript">
          require(["dojo/dom-class","curam/core-uim", "curam/util", "curam/dialog", "curam/util/onLoad", "curam/util/ScreenContext"], function(domClass) {
          <jsp:text>var jsBaseURL = curam.util.retrieveBaseURL();</jsp:text> 
          <jsp:text>dojo.global.jsScreenContext = new curam.util.ScreenContext();</jsp:text>
          jsScreenContext.setContext('MODAL');
          curam.dialog.initModal('about');

          // register publisher for the page height information
          curam.util.onLoad.addPublisher(function(context) {
            context.height = curam.util.getPageHeight();
            context.title = "About";
          });

          dojo.addOnLoad(function() {
              curam.util.onLoad.execute();
              domClass.remove(dojo.body(), "hidden");
          });
        });
        </script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/SPMUIComponents/spm-uicomponents-main.bundle.js"> dummy script content</script> <script type="text/javascript">require(["curam/util"], function(){
           curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/nonStandardModalFooter", [{"numButtons": "0"}]);});
          </script> </head> <body id="about" class="${htmlDirection} DefaultApp basic modal ${htmlLanguage} soria curam bx--uim-modal spm-component" role="document" aria-label="${landmarkLabel}"> <cing:page pageID="AboutBox"> <cing:component style="curam-util-client::about-box"/> </cing:page>
</body>
</html>
</jsp:root>