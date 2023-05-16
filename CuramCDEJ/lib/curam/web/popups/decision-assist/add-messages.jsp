<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0"> <jsp:directive.page contentType="text/html; charset=UTF-8" errorPage="/en/CuramErrorPage.do" isErrorPage="false" language="java" pageEncoding="UTF-8"/> <jsp:directive.page import="curam.util.common.util.JavaScriptEscaper"/> <jsp:directive.page import="curam.util.common.util.xml.XMLEscaper"/> <jsp:directive.page import="curam.util.client.jsp.JspUtil"/> <jsp:text> <![CDATA[<?xml version="1.0" encoding="UTF-8" ?> <!DOCTYPE html>]]> </jsp:text> <jsp:scriptlet> <![CDATA[ java.util.Locale locale=curam.omega3.user.UserPreferencesFactory .getUserPreferences(pageContext.getSession()) .getLocale(); pageContext.setAttribute("page-locale", locale); java.util.List localeList=curam.omega3.util.CDEJResources.getLocaleList(); pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL(3)); ]]> </jsp:scriptlet> <html> <head> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> <title> <jsp:scriptlet> <![CDATA[out.print( curam.omega3.util.CDEJResources.getProperty( "curam.omega3.i18n.DecisionMatrixAddMessages", "page.title"));]]> </jsp:scriptlet> </title> <link rel="stylesheet" type="text/css" media="screen, print" href="${pageScope.o3__serverURL}themes/classic/css/curam.css"/> <link rel="stylesheet" type="text/css" media="screen, print" href="${pageScope.o3__serverURL}CDEJ/css/curam_common.css"/> <link href="${pageScope.o3__serverURL}themes/new/css/new_main.css" media="all" type="text/css" rel="stylesheet"/> <script src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dojo/dojo.js" type="text/javascript">// script content</script> <script type="text/javascript">
  <jsp:scriptlet>curam.util.client.jsp.JspUtil.outputJSModulePaths(pageContext);</jsp:scriptlet>
	</script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/cdej-cm.js" type="text/javascript">// script content</script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/cdej.js" type="text/javascript">// script content</script> <script src="${pageScope.o3__serverURL}CDEJ/jscript/decision-matrix-popup.js" type="text/javascript">// script content</script> <script type="text/javascript">
	  require(["curam/core-uim", "curam/dialog", "curam/ListMap"]);
		<jsp:scriptlet>
       <![CDATA[
         String safeMessages = 
           JavaScriptEscaper.escapeText(request.getParameter("messages"), true);
         
         out.print("var messageVar='" + safeMessages + "';");
        ]]>
  	</jsp:scriptlet>
    <jsp:text>
      <![CDATA[
				var messageVar = "";
        function temp() {
          var parentWin = curam.dialog.getParentWindow(window);
          var messages = messageVar;
          var locales = dojo.trim(parentWin.localeList).split(',');
          var input;
          	
          for (var m = 0; m < locales.length; m++) {
            input = dojo.byId(locales[m]);
            if (input!=null && input!="undefined" && messages[locales[m]]) {
              alert("messages[" + locales[m] + "] = " + messages[locales[m]] );
              input.value = messages[locales[m]];
            }
          }
        }
        dojo.addOnLoad(temp);
        var jsBaseURL = curam.util.retrieveBaseURL();
        var jsScreenContext = new curam.util.ScreenContext();
        jsScreenContext.setContext('POPUP|MODAL');
        curam.dialog.initModal('add-messages');

        // register publisher for the page height information
        curam.util.onLoad.addPublisher(function(context) {
          context.height = curam.util.getPageHeight();
          context.title = window.document.title;
        });
        dojo.addOnLoad(curam.util.onLoad.execute);
        ]]></jsp:text>
  </script> </head> <body style="padding-right:0px;font-family:arial,helvetica,clean,sans-serif; background: none; background-color:white;"> <jsp:scriptlet> <![CDATA[ String safeCombinationID = XMLEscaper.escapeXML(request.getParameter("combinationID")); out.print("<input type=\"hidden\" id=\"combinationId\" value=\"" + safeCombinationID + "\"/>"); ]]> </jsp:scriptlet> <div style="height:200px; background-color:white;"> <div class="page-title-bar" style="width:100%;margin:0 auto;"> <div class="title"> <h1><span style="font-size:9pt;">Add messages for a contradiction combination</span></h1> </div> </div> <div class=""></div> <div class="list list-with-header label-field collapse" style="border:none;width:100%;margin:0 auto;"> <div class="cluster cluster-with-header label-field" style="margin-bottom:5px;margin-left:6px;"> <h2 style="font-size:9pt;">List Of Locales and Messages</h2> <table class="input-cluster" cellspacing="0" cellpadding="0" style="font-size:8pt;"> <col class="field" width="15%"/> <col class="field" width="85%"/> <thead> <tr> <th class="field"> Locale </th> <th class="field"> Message </th> </tr> </thead> <tbody id="messageTable"> <jsp:scriptlet> <![CDATA[ String language=""; String country=""; String variant=""; String localeCode=""; java.util.Iterator it=localeList.iterator(); while(it.hasNext()) { ]]> </jsp:scriptlet> <tr style="height:35px;"> <td class="column" style="font-weight:bold;"> <jsp:scriptlet> <![CDATA[ java.util.Locale currentLocale=(java.util.Locale) it.next(); language=currentLocale.getLanguage(); country=currentLocale.getCountry(); if (country != "") { country="_" + country; } variant=currentLocale.getVariant(); if (variant != "") { variant="_" + variant; } localeCode=language+country+variant; out.print(localeCode); ]]> </jsp:scriptlet> </td> <td class="column left" style="font-weight:bold;"> <jsp:scriptlet> <![CDATA[ out.print("<input id=\"" + localeCode + "\" type=\"text\" style=\"width:100%;\" value=\"\"/>"); ]]> </jsp:scriptlet> </td> </tr> <jsp:scriptlet> <![CDATA[}]]> </jsp:scriptlet> </tbody> </table> </div> <div align="center" style="font-size:8pt"> <a class="ac" onclick="addMessagesFromPopup()" href="#" title="ok" style="padding-right:2px">Save</a><span style="color:#35B207"> | </span> <a class="ac" onclick="curam.dialog.closeModalDialog()" href="#" title="cancel" style="padding-left:2px">Cancel</a> </div> </div> </div> </body> </html>
</jsp:root>