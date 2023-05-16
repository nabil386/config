<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:text>
    <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">]]>
  </jsp:text>
  <jsp:scriptlet>

 </jsp:scriptlet>
  
  <html>
  <head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
  
  
  <script type="text/javascript">
  var textCookies="<jsp:scriptlet>
  <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                    "curam.omega3.i18n.Logon",
                    "Logon.nocookies.message"));          
      ]]></jsp:scriptlet>";

      document.write("<h2><center>");
      document.write(textCookies);
      document.write("</center></h2>");
 </script>
 <noscript>
 <h2>
 <center>
 <jsp:scriptlet>
   <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                     "curam.omega3.i18n.Logon",
                     "Logon.nocookiejavascript.message"));
                     
                  
                     ]]>
 </jsp:scriptlet>
 </center>
 </h2>
</noscript>
 </head>
<body>
</body>
</html>
</jsp:root>