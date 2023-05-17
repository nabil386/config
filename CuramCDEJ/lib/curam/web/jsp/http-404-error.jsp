<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0"> <jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/> <jsp:directive.page import="curam.omega3.user.UserPreferences"/> <jsp:directive.page import="curam.util.client.jsp.JspUtil"/> <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"/> <jsp:output omit-xml-declaration="yes"/> <curam:userPreferences/> <jsp:scriptlet> <![CDATA[out.println("<!DOCTYPE html>"); ]]> </jsp:scriptlet> <jsp:scriptlet>pageContext.setAttribute("path1", request.getContextPath());</jsp:scriptlet> <html lang="${htmlLanguage}"> <head> <style type="text/css">
        span {font-size:70%;font-family:Tahoma;line-height:1.22em}
      
        .cell-style {
          padding-left: 10px;
          padding-top: 10px;
        }
      </style> <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/> <title> <jsp:scriptlet> <![CDATA[out.print( curam.omega3.util.CDEJResources.getProperty( "curam.omega3.i18n.HTTP404Error", "error.title"));]]> </jsp:scriptlet> </title> <jsp:scriptlet> UserPreferences prefs = UserPreferencesFactory.getUserPreferences(session); final String initialURI = pageContext.getErrorData().getRequestURI(); final String contextURI = pageContext.getServletContext().getContextPath(); final int cutPoint = initialURI.indexOf(contextURI) + contextURI.length(); final String reducedStr = initialURI.substring(cutPoint); java.util.StringTokenizer pathElements = new java.util.StringTokenizer(reducedStr, "/"); String prefix = ""; for (int i = 0; i &lt; pathElements.countTokens() - 1; i++) { prefix += "../"; } pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL(1)); </jsp:scriptlet> <script type="text/javascript">
        <jsp:scriptlet>JspUtil.outputDojoLoaderConfiguration(pageContext, prefs.getLocale());</jsp:scriptlet>
      </script> <script type="text/javascript" src="${o3__serverURL}CDEJ/jscript/dojotk/dojo/dojo.js">// dummy script content</script> <jsp:scriptlet> <![CDATA[out.println("<script type=\"text/javascript\" src=\"" + prefix + "CDEJ/jscript/cdej-cm.js\">// script content</script>");]]> <![CDATA[out.println("<script type=\"text/javascript\" src=\"" + prefix + "CDEJ/jscript/cdej.js\">// script content</script>");]]> curam.omega3.taglib.ScreenContext screenContext = (curam.omega3.taglib.ScreenContext) pageContext.getAttribute( curam.omega3.taglib.ScreenContext.CTX_ATTR); if (screenContext == null) { screenContext = new curam.omega3.taglib.ScreenContext(); screenContext.clear(curam.omega3.taglib.ScreenContext.HEADER | curam.omega3.taglib.ScreenContext.FOOTER | curam.omega3.taglib.ScreenContext.NAVIGATOR); screenContext.addContextBits(curam.omega3.taglib.ScreenContext.TAB); } </jsp:scriptlet> <script type="text/javascript">
        require(["curam/core-uim", "curam/dialog"]);

        <jsp:scriptlet>
           curam.util.client.jsp.JspUtil.outputJSScreenContextInitialization(pageContext, screenContext);
        </jsp:scriptlet>
      </script> <script type="text/javascript">
        require(['curam/util', 'dojo/ready'], function(util, ready) {
          ready(function(){
            var errorTitle = '<jsp:scriptlet><![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.HTTP404Error", "error.title"));]]></jsp:scriptlet>';
            util.getTopmostWindow().curam.util.setBrowserTabTitle(errorTitle);
            util.getTopmostWindow().dojo.publish('/curam/progress/unload');
          });
        });
      </script> </head> <body id="Curam_http404Error"> <jsp:scriptlet> <![CDATA[out.println("<div role=\"region\" aria-label=\"" + curam.omega3.util.CDEJResources.getProperty( "curam.omega3.i18n.HTTP404Error", "error.title") + "\">");]]> </jsp:scriptlet> <table> <tr> <td class="cell-style"> <jsp:scriptlet> <![CDATA[out.println("<img alt=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.HTTP404Error", "informational.message") + "\" src=\"" + prefix + "themes/v6/images/image_page_not_available.png\"/>");]]> </jsp:scriptlet> </td> <td class="cell-style"> <span tabindex="0"> <jsp:scriptlet> <![CDATA[out.print( curam.omega3.util.CDEJResources.getProperty( "curam.omega3.i18n.HTTP404Error", "error.message"));]]> </jsp:scriptlet> </span> </td> </tr> </table> <jsp:scriptlet> <![CDATA[out.print( "</div>");]]> </jsp:scriptlet> </body> </html>
</jsp:root>