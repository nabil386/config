<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
  <!-- <jsp:scriptlet>session.invalidate();</jsp:scriptlet> -->
  <jsp:text>
    <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
            ]]>
  </jsp:text>
<!-- NetWeaver cannot find the resources required in this file unless the abolute path is  defined. -->
  <jsp:scriptlet>
    pageContext.setAttribute("path1", request.getContextPath());

    javax.servlet.http.HttpServletRequest hsRequest;
    javax.servlet.http.HttpSession httpSession;
    hsRequest = (javax.servlet.http.HttpServletRequest) request;
    httpSession = hsRequest.getSession();
    
    httpSession.invalidate();
    
  </jsp:scriptlet>
  <html>
    <head>
      <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
      </meta>
      <link href="${pageScope.path1}/themes/v6/css/v6_login.css" media="screen" rel="stylesheet" type="text/css" />
      <link href="${pageScope.path1}/CDEJ/css/custom.css" media="screen" rel="stylesheet" type="text/css" />
      <jsp:text><![CDATA[<!--[if IE 8]>]]></jsp:text>
        <link rel="stylesheet" href="${pageScope.path1}/themes/v6/css/v6_cc_IE8.css"/>
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
    <body class="logon logout" onload="return window_onload()">
        <div class="top-background">
          <div class="login-people"><!-- comment --></div>
        </div>     
        <div class="login-content">
      <center>

            <h1 class="login-welcome">

                   <jsp:scriptlet><![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                      "curam.omega3.i18n.Logon",
                      "Logout.thank"));]]>
                    </jsp:scriptlet>
            </h1>
            <div class="form-wrapper">
            <h2 class="message1">
<jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "LogonError.error"));]]></jsp:scriptlet>
            </h2>
            <p class="message2">
                        
			  <jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "LogonError.message"));]]></jsp:scriptlet>
	      
          </p>
          </div>       
                              <!-- Try again logs the user out, which brings them back to the home page -->
                              <jsp:scriptlet><![CDATA[out.print("<a href=\"logout.jsp\" class=\"ac\" id=\"try-button\" title=\"" 
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
                            
           <p class="copyright">
             <jsp:scriptlet>
               <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon","Logon.copyright.message"));]]>
             </jsp:scriptlet>
          </p>
      </center>
        </div>
        <div class="bottom-background"><!-- comment --></div>
    </body>
  </html>
</jsp:root>
