<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page import="curam.util.client.jsp.JspUtil"/>
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
  <jsp:text>
    <![CDATA[<!DOCTYPE html>]]>
  </jsp:text>
<jsp:scriptlet>pageContext.setAttribute("o3__serverURL", JspUtil.getServerRootURL());</jsp:scriptlet>
<jsp:directive.page import="java.util.Locale" />
<curam:userPreferences/>
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
          document.getElementById('logout-button').focus();
          return true;
        }
      </script>
      <title><jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logout.title"));]]></jsp:scriptlet></title>
              
    </head>
    <body class="${classDirection} curam logon logout" 
          onload="return window_onload()"
          dir="${htmlDirection}"> 
       
        <jsp:scriptlet>
          <![CDATA[out.print("<div class=\"login-content\" role=\"main\""
                + "aria-label=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.main.landmark")
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
            <form id="logout" name="logout" action="servlet/BDMApplicationController" method="post">
            <p class="message2">
              <jsp:scriptlet><![CDATA[out.print(
              curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logout.message2"));]]>
              </jsp:scriptlet>
            </p>
            
              <jsp:scriptlet><![CDATA[
                 out.print("<input tabindex=\"-1\" title=\"" 
                 + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logout.Button.title.submit") + "\""
                 + "  value=\""
                 + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logout.Button.submit") + "\""
                 + " name=\"j_logout\" type=\"submit\" class=\"submit\" aria-hidden=\"true\"/>");
                 ]]></jsp:scriptlet>
              <input type="hidden" name="logoutExitPage" value="" />
              
            <jsp:scriptlet>
            boolean testModeEnabled = curam.omega3.util.DataTestIDUtils.isTestModeEnabled();
            String logoutBtnClassAttr = "class=\"login-middle\"";
            String logoutBtnDataTestIDAttr = " data-testid=\"link_Logout.Button.title.submit\"";
            String logoutBtnClassAndDataTestIDAttr = testModeEnabled ? logoutBtnClassAttr + logoutBtnDataTestIDAttr : logoutBtnClassAttr;
            <![CDATA[out.print("<a href=\"#\" tabIndex=\"1\" id=\"logout-button\" title=\"" 
                    + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logout.Button.title.submit") + "\""
             + " class=\"ac\" onMouseDown=\"this.className='ac selected'\" onMouseOut=\"this.className='ac'\""
             + "onMouseUp=\"this.className='ac';\" onClick=\"document.logout.submit();\" >");
             ]]></jsp:scriptlet>
           <span class="login-left-corner" >
             <span class="login-right-corner">
               <jsp:scriptlet><![CDATA[out.print("<span " + logoutBtnClassAndDataTestIDAttr + ">" +
                   curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", 
                              "Logout.Button.submit") + "</span>");
                 ]]></jsp:scriptlet>
             </span>
           </span>
           <jsp:scriptlet><![CDATA[out.print("</a>");]]></jsp:scriptlet>
           <BR/>
           <!-- span class="filler">&amp;nbsp;&amp;nbsp;&amp;nbsp;</span-->
           <jsp:scriptlet>
           String cancelBtnDataTestIDAttr = " data-testid=\"link_Logout.Button.title.cancel\"";
           String cancelBtnClassAndDataTestIDAttr = testModeEnabled ? logoutBtnClassAttr + cancelBtnDataTestIDAttr : logoutBtnClassAttr;
           <![CDATA[out.print("<a tabindex=\"2\" title=\"" 
               + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logout.Button.title.cancel") + "\""
               + " class=\"ac\" onMouseDown=\"this.className='ac selected'\" onMouseOut=\"this.className='ac'\""
               + "onMouseUp=\"this.className='ac';\" href=\"AppController.do\" >");
               ]]></jsp:scriptlet>
             <span class="login-left-corner">
               <span class="login-right-corner">
                 <jsp:scriptlet><![CDATA[out.print("<span " + cancelBtnClassAndDataTestIDAttr + ">" +
                   curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", 
                              "Logout.Button.cancel") + "</span>");
                 ]]></jsp:scriptlet>
               </span>
             </span>
           <jsp:scriptlet><![CDATA[out.print("</a>");]]></jsp:scriptlet>  
           </form>   
           
           <jsp:scriptlet><![CDATA[out.print("</div>");]]></jsp:scriptlet>
    </body>
  </html>
</jsp:root>