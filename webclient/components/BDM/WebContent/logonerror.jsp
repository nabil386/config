<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page import="curam.util.client.jsp.JspUtil"/>
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
  <!-- <jsp:scriptlet>session.invalidate();</jsp:scriptlet> -->
  <jsp:text>
    <![CDATA[<!DOCTYPE html>]]>
  </jsp:text>
<jsp:scriptlet>pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL());</jsp:scriptlet>
<jsp:directive.page import="java.util.Locale" />
<jsp:scriptlet>
  Locale locale = request.getLocale();
  String language = locale.toString();
  pageContext.setAttribute("htmlLanguage", JspUtil.replaceUnderscoreByHyphen(language));
  if(language.startsWith("ar")
    || language.startsWith("he")
    || language.startsWith("iw")) {
    pageContext.setAttribute("htmlDirection", "rtl");
    pageContext.setAttribute("classDirection", "rtl");
  } else {
    pageContext.setAttribute("htmlDirection", "ltr");
    pageContext.setAttribute("classDirection", "");
  }
</jsp:scriptlet>
  <html lang="${htmlLanguage}" dir="${htmlDirection}" class="${classDirection}">
    <head>
      <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
      </meta>
      <link href="${pageScope.o3__serverURL}themes/v6/css/v6_login.css" media="screen" rel="stylesheet" type="text/css" />
      <link href="${pageScope.o3__serverURL}themes/curam/css/curam_login.css" media="screen" rel="stylesheet" type="text/css" />
      <link href="${pageScope.o3__serverURL}themes/curam/fonts/main-ibm-font.css" media="screen" rel="stylesheet" type="text/css" />
      <link rel="shortcut icon" href="${pageScope.o3__serverURL}themes/curam/images/SPM_UI_Mod_Placeholderlogo.ico" />
      
      <jsp:scriptlet>
        if(pageContext.getAttribute("htmlDirection").equals("rtl")) {
          </jsp:scriptlet>
          <link href="${pageScope.o3__serverURL}themes/v6_rtl/css/v6_rtl_login.css" media="screen" rel="stylesheet" type="text/css" />
          <link href="${pageScope.o3__serverURL}themes/curam_rtl/css/curam_rtl_login.css" media="screen" rel="stylesheet" type="text/css" />
          <jsp:scriptlet>
        }
      </jsp:scriptlet>
      <link href="${pageScope.o3__serverURL}CDEJ/css/custom.css" media="screen" rel="stylesheet" type="text/css" />
      <jsp:text><![CDATA[<!--[if IE 8]>]]></jsp:text>
        <link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_IE8.css"/>
      <jsp:text><![CDATA[<![endif]-->]]></jsp:text>
      <script type="text/javascript">
        function window_onload() {
          document.getElementById('try-button').focus();
          return true;
        }
      </script>
      <title><jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "LogonError.title"));]]></jsp:scriptlet></title>

    </head>
    <body class="${classDirection} curam logonerror logon" 
          onload="return window_onload()"
          dir="${htmlDirection}">  
      

        <jsp:scriptlet>
          <![CDATA[out.print("<div class=\"login-content\" role=\"main\""
                + " aria-label=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.banner.landmark")
                + "\" />");]]>
         </jsp:scriptlet>
          <div >        
                <jsp:scriptlet>
                <![CDATA[out.print("<img alt=\"" 
                + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Application.logo.alt")
                + "\" src=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Application.logo")
                + "\" />");]]>
                </jsp:scriptlet>
           </div>
            <h2 class="login-welcome" style="margin-top: 50px;">
                <span class="login-welcome-softfont">
                  <jsp:scriptlet>
                    <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                      "curam.omega3.i18n.Logon",
                      "Logon.welcome"));]]>
                    </jsp:scriptlet>
                </span>
                <br />
                <span class="login-welcome-softfont">
                   <jsp:scriptlet>
                  <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                    "curam.omega3.i18n.Logon",
                    "Logon.application.title.ibm"));]]>
                  </jsp:scriptlet>
                </span>
                <jsp:text><![CDATA[&nbsp;]]></jsp:text>
                <jsp:scriptlet>
                <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                  "curam.omega3.i18n.Logon",
                  "Logon.application.title.curam"));]]>
                </jsp:scriptlet>
            </h2>
            <!-- Start content background Div -->
         <jsp:scriptlet>
          <![CDATA[out.print("<div class=\"content-background\" >");]]>
         </jsp:scriptlet>
         
            <p class="message1">
              <jsp:scriptlet><![CDATA[out.print(
                curam.omega3.util.CDEJResources.getProperty(
                "curam.omega3.i18n.Logon",
                "LogonError.message"));]]></jsp:scriptlet>
            </p>     
          <!-- Try again brings the user back to the logon page -->
          <jsp:scriptlet><![CDATA[out.print("<a href=\"logon.jsp\" class=\"ac\" id=\"try-button\" title=\"" 
            + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "LogonError.tryAgain.title")
            + "\" onMouseDown=\"this.className='ac selected'\" onMouseOut=\"this.className='ac'\" tabindex=\"1\""
            + " onMouseUp=\"this.className='ac';\" >");]]>
          </jsp:scriptlet>
              <span class="login-left-corner" >
                <span class="login-right-corner">
                  <span class="login-middle">
                     <jsp:scriptlet>
                       <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon","LogonError.tryAgain"));]]>
                     </jsp:scriptlet>
                   </span>
                </span>
              </span>
           <jsp:scriptlet><![CDATA[out.print("</a>");]]></jsp:scriptlet>
          
          <jsp:scriptlet><![CDATA[out.print("</div>");]]></jsp:scriptlet>  <!-- End content background Div -->
          
           
        <jsp:scriptlet><![CDATA[out.print("</div>");]]></jsp:scriptlet>
    </body>
  </html>
</jsp:root>
