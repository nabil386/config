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
      <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/SPMUIComponents/spm-uicomponents-main.bundle.js">
        <jsp:text><![CDATA[ dummy script content]]></jsp:text>
      </script>
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
    <body class="${classDirection} curam soria logonerror logon spm-component" 
          onload="return window_onload()"
          dir="${htmlDirection}">  
      

        <jsp:scriptlet>
          <![CDATA[out.print("<div class=\"login-content\" role=\"main\""
                + " aria-label=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.banner.landmark")
                + "\" />");]]>
         </jsp:scriptlet>
         <div class="login-banner">
          <div class="logo">        
            <jsp:scriptlet>
            <![CDATA[out.print("<img alt=\"" 
            + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Application.logo.alt")
            + "\" src=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Application.logo")
            + "\" />");]]>
            </jsp:scriptlet>
          </div>
          <span class="login-banner-softfont">
            <jsp:scriptlet>
              <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                "curam.omega3.i18n.Logon",
                "Logon.application.title.ibm")); ]]>
              </jsp:scriptlet>
              <jsp:text><![CDATA[&trade;]]></jsp:text>
              <jsp:text><![CDATA[&nbsp;]]></jsp:text> 
          </span>
          <span class="login-banner-boldfont">
              <jsp:scriptlet>
            <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logon.application.title.spm"));]]>
            </jsp:scriptlet>
          </span>
        </div>

        <div class="login-container">

          <h1 class="login-note">
            <jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
            "curam.omega3.i18n.Logon",
            "Logon.instruction"));]]>
            </jsp:scriptlet>
          </h1>
          <div id="messageContainerID" tabindex="0">
            <div id='error-messages-container' class='wrapper-expired-message'>
              <ul id='error-messages' class='messages'>
                <li class='level-1'>
                  <div>
                    <span id='message'>
                      <jsp:scriptlet><![CDATA[out.print(
                        curam.omega3.util.CDEJResources.getProperty(
                        "curam.omega3.i18n.Logon",
                        "LogonError.message"));]]></jsp:scriptlet>
                    </span>
                  </div>
                </li>
              </ul>
            </div>
          </div>

          <jsp:scriptlet>
             <![CDATA[out.print("<input tabindex=\"1\" title=\""
             + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "LogonError.tryAgain.title") + "\""
             + " id=\"try-button\" class=\"bx--btn bx--btn--primary\" type=\"button\" data-testid=\"link_Logout.Button.title.cancel\" value=\""+curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "LogonError.tryAgain") + "\" onclick=\"location.href='.';\">"
             +"</input>");
            ]]>
          </jsp:scriptlet>
        </div>
        <div class="copyright-container">
          <div class="copyright">       
            <jsp:scriptlet>              
              <![CDATA[out.print("<img class=\"copyright-logo\" alt=\"" 
              + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.application.title.merative")
              + "\" src=\"" +  pageContext.getAttribute("o3__serverURL") + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.copyright.logo")
              + "\" />");]]>
            </jsp:scriptlet>  
            <p>      
              <jsp:scriptlet>
                <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon","Logon.copyright.message"));]]>
              </jsp:scriptlet>
            </p>
            <p>
              <jsp:scriptlet>
                <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon","Logon.copyright.message2"));]]>
              </jsp:scriptlet>
            </p>
          </div>
        </div>
        <jsp:scriptlet><![CDATA[out.print("</div>");]]></jsp:scriptlet>
    </body>
  </html>
</jsp:root>
