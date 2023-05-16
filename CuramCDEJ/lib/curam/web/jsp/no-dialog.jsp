<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"> <jsp:directive.page import="curam.omega3.util.CDEJResources"/> <jsp:directive.page import="curam.util.client.jsp.JspUtil"/> <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"/> <jsp:output omit-xml-declaration="yes"/> <script type="text/javascript">
    <jsp:scriptlet>
    	curam.util.client.jsp.JspUtil.outputDojoLoaderConfiguration(pageContext, CDEJResources.getDefaultLocale());
    </jsp:scriptlet>
  </script> <jsp:scriptlet>pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL());</jsp:scriptlet> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/dojotk/dojo/dojo.js">// dummy script content</script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/cdej-cm.js">// script content</script> <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/cdej.js">// script content</script> <script type="text/javascript">
    <![CDATA[
     require(["curam/core-uim"]);
     require(["curam/util"], function(){
      var parentWindow = window.opener || (window.dialogArguments ? window.dialogArguments[0] : null);
       if (parentWindow) {
         var href = window.location.href.replace(/o3frame=modal/g,"").replace(/\?&/g,"?").replace(/&&/g, "&");
         href = curam.util.updateCtx(href);
         if(href.indexOf("?") == href.length - 1) {
           href = href.substring(0, href.length -1);
         }
         parentWindow.curam.util.redirectWindow(href);
         window.close();
       }
     });
     
     ]]>
   </script>
</jsp:root>