<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:curam="http://www.curamsoftware.com/curam" xmlns:cing="http://www.curamsoftware.com/curam/jde/client/curam-ng" xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
<jsp:directive.page contentType="text/html; charset=UTF-8" errorPage="/en/CuramErrorPage.do" isErrorPage="false" language="java" pageEncoding="UTF-8"/>
<jsp:directive.page import="curam.util.client.jsp.JspUtil"/>
<jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/>
<jsp:directive.page import="curam.omega3.user.UserPreferences"/>
<jsp:output omit-xml-declaration="yes"/>
<jsp:text><![CDATA[<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">]]></jsp:text>
<curam:userPreferences/>
<jsp:scriptlet> UserPreferences prefs = UserPreferencesFactory.getUserPreferences(session); if(pageContext.getAttribute("htmlDirection").equals("rtl")) { pageContext.setAttribute("classDirection", "rtl"); } else { pageContext.setAttribute("classDirection", ""); } </jsp:scriptlet>
<html lang="${htmlLanguage}" dir="${htmlDirection}" class="${classDirection}"> <head> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> <title><jsp:scriptlet><![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("help.page.title"));]]></jsp:scriptlet></title> <jsp:scriptlet> <![CDATA[ pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL()); ]]> </jsp:scriptlet> <link rel="stylesheet" type="text/css" media="screen, print" href="${pageScope.o3__serverURL}themes/v6/css/help-style.css"/> <link rel="stylesheet" href="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dijit/themes/soria/soria.css"/> <jsp:text><![CDATA[<!--[if IE 8]>]]></jsp:text> <link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_IE8.css"/> <jsp:text><![CDATA[<![endif]-->]]></jsp:text> <jsp:text><![CDATA[<!--[if IE 9]>]]></jsp:text> <link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_IE9.css"/> <jsp:text><![CDATA[<![endif]-->]]></jsp:text> <jsp:scriptlet> if(pageContext.getAttribute("htmlDirection").equals("rtl")) { </jsp:scriptlet><link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6_rtl/css/help-style_rtl.css"/><jsp:scriptlet> } </jsp:scriptlet> <script type="text/javascript">
        <jsp:scriptlet>JspUtil.outputDojoLoaderConfiguration(pageContext, prefs.getLocale());</jsp:scriptlet>
      </script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dojo/dojo.js">// dummy script content</script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dijit/dijit.js" type="text/javascript">// script content</script> <script type="text/javascript">
	        <jsp:scriptlet>curam.util.client.jsp.JspUtil.outputJSModulePaths(pageContext);</jsp:scriptlet>
          var jsPageID="help";
        </script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/cdej.js">// script content</script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/cdej-cm.js">// script content</script> <script type="text/javascript">
			  	require(["dojo/dom-class", "curam/core-uim", "curam/dialog", "curam/util/ScreenContext", "curam/util/onLoad"], function(domClass) {
            <jsp:text>dojo.global.jsBaseURL = curam.util.retrieveBaseURL();</jsp:text> 
            <jsp:text>dojo.global.jsScreenContext = new curam.util.ScreenContext();</jsp:text>
            jsScreenContext.setContext('MODAL');
            curam.dialog.initModal('help');

            // register publisher for the page height information
            curam.util.onLoad.addPublisher(function(context) {
              context.height = curam.util.getPageHeight();
              context.title = "Help";
            });

            dojo.addOnLoad(function() {
              curam.util.onLoad.execute();
              dojoClass.remove(dojo.body(), "hidden");
            });
			  	});         
        </script> </head>
<body id="help" class="${classDirection} DefaultApp basic modal en soria" lang="${htmlLanguage}"> <cing:page pageID="Help"> <cing:component style="curam-util-client::online-help"/> </cing:page>
</body>
</html>
</jsp:root>