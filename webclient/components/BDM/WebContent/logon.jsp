<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">

  <jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/>
  <jsp:directive.page import="curam.omega3.user.UserPreferences"/>
  <jsp:directive.page import="curam.util.client.jsp.JspUtil"/>  
  <jsp:directive.page import="curam.core.struct.CuramInd"/>
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
      <title><jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logon.title"));]]></jsp:scriptlet></title>
              
    </head>
    <body class="${classDirection} curam soria logon" 
          onload="return window_onload()">

        <jsp:scriptlet>
          <![CDATA[out.print("<div class=\"login-content\" role=\"main\""
                + " aria-label=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.banner.landmark")
                + "\" />");]]>
         </jsp:scriptlet>
         
          <div >
                <jsp:scriptlet>
                <![CDATA[out.print("<img alt=\"" 
                + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Application.logo.alt")
                + "\" src=\"" +  pageContext.getAttribute("o3__serverURL") + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Application.logo")
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
            
           <div>
              <jsp:scriptlet>
                <![CDATA[
                out.print("<a href=\"https://www.canada.ca/en/employment-social-development/accessibility.html\" target=\"_blank\" rel=\"noopener\" tabindex=\"1\" style=\"text-decoration-line: underline;\" >");
                out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon","Logon.accessibility.text"));
                 out.print("</a>");]]></jsp:scriptlet> 
            </div>
            
             <!--  output the session expired message-->
  			<jsp:scriptlet>
  				<![CDATA[
					JspUtil.printSessionExpiredMessage(pageContext);
				 ]]>
  			</jsp:scriptlet>
             
             <p>
             <jsp:scriptlet>
                <![CDATA[
                out.print("<a href=\".\" tabindex=\"2\" style=\"margin-top: 40px;\" title=\""  
                + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.title") + "\""
                + " class=\"ac\" onMouseDown=\"this.className='ac selected'\" onMouseOut=\"this.className='ac'\""
                + "onMouseUp=\"this.className='ac';\" >");
                ]]></jsp:scriptlet>
                <span class="login-left-corner" >
                  <span class="login-right-corner">
                    <jsp:scriptlet><![CDATA[out.print("<span class=\"login-middle\">" +
                   curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", 
                              "Logon.Button.value") + "</span>");
                 ]]></jsp:scriptlet>
                  </span>
                </span>
                <jsp:scriptlet><![CDATA[out.print("</a>");]]></jsp:scriptlet> 
                </p>
             <p>
             <jsp:scriptlet>
                   <![CDATA[out.print(
                     curam.omega3.util.CDEJResources.getProperty(
                     "curam.omega3.i18n.Logon",
                     "Logon.note"));]]>
                 </jsp:scriptlet>  
             </p> 
         
         
         
         
         <jsp:scriptlet>
         
         boolean testModeEnabled = curam.omega3.util.DataTestIDUtils.isTestModeEnabled();
               if(testModeEnabled) {
         </jsp:scriptlet>
            
            <h3 class="login-note">
              <jsp:scriptlet><![CDATA[out.print(
              curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logon.instruction"));]]>
              </jsp:scriptlet>
            </h3>
            <h3 class="login-note">
              <jsp:scriptlet><![CDATA[out.print(
              curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logon.instruction.fr"));]]>
              </jsp:scriptlet>
            </h3>
            
            <form id="loginform" name="loginform" action="j_security_check" method="post" >
               <label for="j_username">
                 <jsp:scriptlet>
                   <![CDATA[out.print(
                     curam.omega3.util.CDEJResources.getProperty(
                     "curam.omega3.i18n.Logon",
                     "Logon.username"));]]>
                 </jsp:scriptlet>
               </label>
               <br />
               <jsp:scriptlet>
               
               String usernameTitleAttr = "title=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.username.title") + "\"";
               String usernameDataTestIDAttr = " data-testid=\"link_Logon.username.title\"";
               String usernameTitleAndDataTestIDAttr = testModeEnabled ? usernameTitleAttr + usernameDataTestIDAttr : usernameTitleAttr;
                 <![CDATA[
                   out.print("<input " + usernameTitleAndDataTestIDAttr
                   + " name=\"j_username\" type=\"text\" id=\"j_username\" tabindex=\"3\" />");
                 ]]>
               </jsp:scriptlet>
              <br />
               <label for="j_password">                            
                 <jsp:scriptlet>
                   <![CDATA[out.print(
                     curam.omega3.util.CDEJResources.getProperty(
                     "curam.omega3.i18n.Logon",
                     "Logon.password"));]]>
                 </jsp:scriptlet>
               </label>  
               <br />
               <jsp:scriptlet>
               String passwordTitleAttr = "title=\"" + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.password.title") + "\"";
               String passwordDataTestIDAttr = " data-testid=\"link_Logon.password.title\"";
               String passwordTitleAndDataTestIDAttr = testModeEnabled ? passwordTitleAttr + passwordDataTestIDAttr : passwordTitleAttr;
                 <![CDATA[out.print("<input " + passwordTitleAndDataTestIDAttr
                   + " name=\"j_password\" type=\"password\" id=\"j_password\" tabindex=\"4\" autocomplete=\"off\"/>");
                   ]]>
                </jsp:scriptlet>
             
                <jsp:scriptlet>
                  <![CDATA[out.print("<input tabindex=\"-1\" title=\"" 
                    + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.title") + "\""
                    + "  value=\""
                    + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.value") + "\""
                    + " name=\"action\" type=\"submit\" class=\"submit\" aria-hidden=\"true\" />");
                  ]]>
                </jsp:scriptlet>
                 <br/>
                <jsp:scriptlet>
                String logonBtnClassAttr = "class=\"login-middle\"";
                String logonBtnDataTestIDAttr = " data-testid=\"link_Logon.Button.title\"";
                String logonBtnClassAndDataTestIDAttr = testModeEnabled ? logonBtnClassAttr + logonBtnDataTestIDAttr : logonBtnClassAttr;
                <![CDATA[
                out.print("<a href=\"#\" tabindex=\"5\" title=\"" 
                + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.title") + "\""
                + " class=\"ac\" onMouseDown=\"this.className='ac selected'\" onMouseOut=\"this.className='ac'\""
                + "onMouseUp=\"this.className='ac';\" onClick=\"document.loginform.submit();\" >");
                ]]></jsp:scriptlet>
                <span class="login-left-corner" >
                  <span class="login-right-corner">
                    <jsp:scriptlet><![CDATA[out.print("<span " + logonBtnClassAndDataTestIDAttr + ">" +
                   curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", 
                              "Test.Logon.Button.value") + "</span>");
                 ]]></jsp:scriptlet>
                  </span>
                </span>
                <jsp:scriptlet><![CDATA[out.print("</a>");]]></jsp:scriptlet> 
             </form>   
             <jsp:scriptlet>
               }
            </jsp:scriptlet>
            
            <jsp:scriptlet><![CDATA[out.print("</div>");]]></jsp:scriptlet>
    </body>
  </html>
</jsp:root>