<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  <jsp:directive.page import="curam.omega3.user.InfrastructureUserPreferences"/>
  <jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/>
  <jsp:directive.page import="curam.omega3.user.UserPreferences"/>
  <jsp:text>
    <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">]]>
  </jsp:text>
  <jsp:scriptlet>
      
    javax.servlet.http.HttpServletRequest hsRequest;
    javax.servlet.http.HttpSession httpSession;
    hsRequest = (javax.servlet.http.HttpServletRequest) request;
    httpSession = hsRequest.getSession();

    //High contrast mode selection persists across logout operation
    UserPreferences prefs = UserPreferencesFactory.getUserPreferences(session);    
    Cookie hcCookie = new Cookie("citizenportal.highContrastMode", (String)     prefs.getUserPreference(InfrastructureUserPreferences.HIGH_CONTRAST_MODE));
    hcCookie.setMaxAge(-1);	//delete on browser exit
    response.addCookie(hcCookie);
        
    // Reset any session values        
    httpSession.setAttribute("curam.userName", null);    
    httpSession.setAttribute("curam.password", null);
    httpSession.setAttribute("curam.userType", null);
    httpSession.setAttribute("curam.tempUser", null);
    httpSession.invalidate();
                
  </jsp:scriptlet>
  <html>
    <head>
      <script type="text/javascript">
        function autoSubmit() {
          document.getElementById("logout").submit();
        }
      </script>
      <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
    </head>
    <body class="logoutBody" style="visibility: hidden;" onload="autoSubmit()">
      <form id="logout" name="logout" action="servlet/ApplicationController" method="post">
        <input type="submit" name="j_logout" value="Log Out" />
        <jsp:scriptlet>
        </jsp:scriptlet>
        <input type="hidden" name="logoutExitPage" value="redirect.jsp" />
      </form>
    </body>
  </html>
</jsp:root>
