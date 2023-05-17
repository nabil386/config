<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/>
  <jsp:directive.page import="curam.omega3.user.UserPreferences"/>
  <jsp:directive.page import="curam.util.client.jsp.JspUtil"/>
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
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
      <jsp:text><![CDATA[<!--[if IE]>]]></jsp:text>
        <link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_IE.css"/>
      <jsp:text><![CDATA[<![endif]-->]]></jsp:text>
      <jsp:text><![CDATA[<!--[if IE 8]>]]></jsp:text>
        <link rel="stylesheet" href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_IE8.css"/>
      <jsp:text><![CDATA[<![endif]-->]]></jsp:text>
      <jsp:text><![CDATA[<!--[if !IE]>]]></jsp:text>
        <link rel="stylesheet" type="text/css" media="all"
              href="${pageScope.o3__serverURL}themes/v6/css/v6_cc_notIE.css"/>
      <jsp:text>~<![CDATA[<![endif]-->]]></jsp:text>
      <link href="${pageScope.o3__serverURL}CDEJ/css/custom.css" media="screen" rel="stylesheet" type="text/css" />
      <!--
        Prevent this page from displaying in a dialog. Close the dialog and sent the 
        parent to the log on screen. 
      -->
      <jsp:include page="no-dialog.jsp"/>
      
      <script type="text/javascript">
        <jsp:text><![CDATA[
      	require(["dojo/ready","curam/util/Logon"], 
      	function(ready){
      	  ready(curam.util.Logon.ensureFullPageLogon);
      	});
      	/**
      	* Sets the focus in the expired message or in the username. 
      	*/
      	function window_onload() {
      		//verify if the page has the expired message container for setting the focus
      		var errorMessageContainerDOM = document.getElementById('error-messages-container');
       	    if(errorMessageContainerDOM && errorMessageContainerDOM.parentNode){
       	    	errorMessageContainerDOM.parentNode.focus();
       	  
       	    //set the focus on the username input	
       	    }else{
       	    	document.loginform.j_username.focus();
       	    }
            return true;
        }
       ]]></jsp:text>
      </script>

      <script type="text/javascript" src="${pageScope.o3__serverURL}CDEJ/jscript/SPMUIComponents/spm-uicomponents-main.bundle.js">
        <jsp:text><![CDATA[ dummy script content]]></jsp:text>
      </script>
      <script src="${pageScope.o3__serverURL}CDEJ/jscript/SPMUIComponents/spm-custom-carbon-addons-main.bundle.js" type="text/javascript"> dummy script content</script>

      <title><jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logon.title"));]]></jsp:scriptlet></title>
              
    </head>
    <body class="${classDirection} curam soria logon spm-component" 
          onload="return window_onload()">

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
            + "\" src=\"" + pageContext.getAttribute("o3__serverURL") + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Application.logo")
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
             <!-- output the session expired message-->
            <jsp:scriptlet>
              <![CDATA[
              JspUtil.printSessionExpiredMessage(pageContext);
            ]]>
            </jsp:scriptlet>
            
            <form id="loginform" name="loginform" action="j_security_check" method="post">
               <label for="j_username">
                 <jsp:scriptlet>
                   <![CDATA[out.print(
                     curam.omega3.util.CDEJResources.getProperty(
                     "curam.omega3.i18n.Logon",
                     "Logon.username"));]]>
                 </jsp:scriptlet>
               </label>
               <jsp:scriptlet>
               boolean testModeEnabled = curam.omega3.util.DataTestIDUtils.isTestModeEnabled();
               String usernameTitleAttr = "title=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.username.title") + "\"";
               String usernameDataTestIDAttr = " data-testid=\"link_Logon.username.title\"";
               String usernameTitleAndDataTestID = testModeEnabled ? usernameTitleAttr + usernameDataTestIDAttr : usernameTitleAttr;
                 <![CDATA[
                   out.print("<input " + usernameTitleAndDataTestID
                   + " autocomplete=\"username\" name=\"j_username\" type=\"text\" id=\"j_username\" />");
                 ]]>
               </jsp:scriptlet>
               <label for="j_password">                            
                 <jsp:scriptlet>
                   <![CDATA[out.print(
                     curam.omega3.util.CDEJResources.getProperty(
                     "curam.omega3.i18n.Logon",
                     "Logon.password"));]]>
                 </jsp:scriptlet>
               </label>  
               <jsp:scriptlet>
                 String passwordTitleAttr = "title=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.password.title") + "\"";
                 String passwordDataTestIDAttr = " data-testid=\"link_Logon.password.title\"";
                 String passwordTitleAndDataTestID = testModeEnabled ? passwordTitleAttr + passwordDataTestIDAttr : passwordTitleAttr;
                 <![CDATA[out.print("<input " + passwordTitleAndDataTestID
                   + " autocomplete=\"current-password\" name=\"j_password\" type=\"password\" id=\"j_password\" />");
                   ]]>
                </jsp:scriptlet>
                <jsp:scriptlet>
                  String logonBtnDataTestIDAttr = testModeEnabled ? " data-testid=\"link_Logon.Button.title\"" : "";
                  <![CDATA[out.print("<button onClick=\"document.loginform.submit();\" title=\"" 
                    + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.title") + "\""
                    + logonBtnDataTestIDAttr
                    + " name=\"action\" type=\"submit\" class=\"submit bx--btn bx--btn--primary\">"+curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.value")
                    +"<svg focusable=\"false\" class=\"bx--btn__icon\" preserveAspectRatio=\"xMidYMid meet\" xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" width=\"16\" height=\"16\" viewBox=\"0 0 32 32\" aria-hidden=\"true\"><path d=\"M18 6L16.57 7.393 24.15 15 4 15 4 17 24.15 17 16.57 24.573 18 26 28 16 18 6z\"></path></svg></button>");
                  ]]>
                </jsp:scriptlet> 
 
                 
             </form>
            </div>   
             <p>
             <jsp:scriptlet>
                   <![CDATA[out.print(
                     curam.omega3.util.CDEJResources.getProperty(
                     "curam.omega3.i18n.Logon",
                     "Logon.note"));]]>
                 </jsp:scriptlet>  
             </p> 
            
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