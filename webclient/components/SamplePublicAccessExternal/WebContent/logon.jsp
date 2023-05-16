<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:output omit-xml-declaration="yes" />
  <jsp:text>
    <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
            ]]>
  </jsp:text>
<!-- NetWeaver cannot find the resources required in this file unless the abolute path is  defined. -->
<jsp:scriptlet>pageContext.setAttribute("path1", request.getContextPath());</jsp:scriptlet>
  <html>
    <head>
      <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
      </meta>
      <link href="${pageScope.path1}/themes/v6/css/v6_login.css" media="screen" rel="stylesheet" type="text/css" />
      <link href="${pageScope.path1}/CDEJ/css/custom.css" media="screen" rel="stylesheet" type="text/css" />
      <jsp:text><![CDATA[<!--[if IE 8]>]]></jsp:text>
        <link rel="stylesheet" href="themes/v6/css/v6_cc_IE8.css"/>
      <jsp:text><![CDATA[<![endif]-->]]></jsp:text>
      <!--
        Prevent this page from displaying in a dialog. Close the dialog and sent the 
        parent to the log on screen. 
      -->
      <jsp:include page="no-dialog.jsp"/>
      <script type="text/javascript" src="${pageScope.path1}/CDEJ/jscript/curam/util/Logon.js">//script content</script>
      <script type="text/javascript">
        curam.util.Logon.ensureFullPageLogon();
        function window_onload() {
          document.loginform.j_username.focus();
          return true;
        }
      </script>
      <title><jsp:scriptlet><![CDATA[out.print(
            curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logon.title"));]]></jsp:scriptlet></title>
    </head>
    <body class="logon" onload="return window_onload()">
        <div class="top-background">
          <div class="login-people"><!-- comment --></div>
        </div>     
        <div class="login-content">
          <center>
            <h1 class="login-welcome">
              <jsp:scriptlet>
                <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                  "curam.omega3.i18n.Logon",
                  "Logon.welcome"));]]>
                </jsp:scriptlet>
            </h1>
            <div class="form-wrapper">
            <h2 class="login-note">
              <jsp:scriptlet><![CDATA[out.print(
              curam.omega3.util.CDEJResources.getProperty(
              "curam.omega3.i18n.Logon",
              "Logon.note"));]]>
              </jsp:scriptlet>
            </h2>
            <form id="loginform" name="loginform" action="j_security_check" method="post">  
              <table>
                <tr><th>
	             <label for="j_username">
	               <jsp:scriptlet>
	                 <![CDATA[out.print(
	                   curam.omega3.util.CDEJResources.getProperty(
	                   "curam.omega3.i18n.Logon",
	                   "Logon.username"));]]>
	               </jsp:scriptlet>
	             </label>
	             </th>
	             <td>
	             <jsp:scriptlet>
	               <![CDATA[
	                 out.print("<input title=\"" 
	                 + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.username.title") + "\""
	                 + " name=\"j_username\" type=\"text\" id=\"j_username\" tabindex=\"1\" />");
	               ]]>
	             </jsp:scriptlet>
	             </td></tr><tr class="second-row"><th>
	             <label for="j_password">                            
	               <jsp:scriptlet>
	                 <![CDATA[out.print(
	                   curam.omega3.util.CDEJResources.getProperty(
	                   "curam.omega3.i18n.Logon",
	                   "Logon.password"));]]>
	               </jsp:scriptlet>
	             </label>  
	             </th><td>      
	             <jsp:scriptlet>
	               <![CDATA[out.print("<input title=\"" 
	                 + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.password.title") + "\""
	                 + " name=\"j_password\" type=\"password\" id=\"j_password\" tabindex=\"2\" />");
	                 ]]>
	              </jsp:scriptlet>
	              </td>
	                </tr>
                        <tr>
			  <td>
			    <input type="hidden" name="user_type" value="EXTERNAL" />
			  </td>
                        </tr>
	              </table>
	              <jsp:scriptlet>
	                <![CDATA[out.print("<input tabindex=\"-1\" title=\"" 
	                  + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.title") + "\""
	                  + "  value=\""
	                  + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.value") + "\""
	                  + " name=\"action\" type=\"submit\" class=\"submit\" aria-hidden=\"true\" />");
	                ]]>
	              </jsp:scriptlet>
	           </form>
            </div>         
            <jsp:scriptlet><![CDATA[
            out.print("<a href=\"#\" tabindex=\"3\" title=\"" 
            + curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", "Logon.Button.title") + "\""
            + " class=\"ac\" onMouseDown=\"this.className='ac selected'\" onMouseOut=\"this.className='ac'\""
            + "onMouseUp=\"this.className='ac';\" onClick=\"document.loginform.submit();\" >");
            ]]></jsp:scriptlet>
            <span class="login-left-corner" >
              <span class="login-right-corner">
                <span class="login-middle">
                   <jsp:scriptlet><![CDATA[
                    out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon", 
                               "Logon.Button.value"));
                  ]]></jsp:scriptlet></span>
              </span>
            </span>
            <jsp:scriptlet><![CDATA[out.print("</a>");]]></jsp:scriptlet> 
            <p class="copyright">
              <jsp:scriptlet>
                <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon","Logon.copyright.message"));]]>
              </jsp:scriptlet>
              <jsp:text><![CDATA[&nbsp;]]></jsp:text>
              <jsp:scriptlet>
                <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty("curam.omega3.i18n.Logon","Logon.copyright.message2"));]]>
              </jsp:scriptlet>
            </p>
          </center>  
         </div>
        <div class="bottom-background"><!-- comment --></div>
    </body>
  </html>
</jsp:root>