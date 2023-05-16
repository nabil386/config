<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:curam="http://www.curamsoftware.com/curam"
  version="2.0">
  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  
  <jsp:directive.page import="java.sql.*"/> 
  <jsp:directive.page import="java.util.*"/> 
  <jsp:directive.page import="java.net.*" /> 
  <jsp:directive.page import="javax.servlet.RequestDispatcher" /> 
  <jsp:text>
    <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">]]>
  </jsp:text>
  <jsp:scriptlet>
     
    
    javax.servlet.http.HttpServletRequest hsRequest;
    javax.servlet.http.HttpSession httpSession;
    hsRequest = (javax.servlet.http.HttpServletRequest) request;
    httpSession = hsRequest.getSession();
    
    // The following http response headers ensure that logon.jsp is never cached.
    // Customers are advised to configure their application to set similar
    // cache-control and expiry headers for all page player and IEG pages:
    // for example, /CitizenPortal/cw/PlayerPage.do and /CitizenPortal/ieg/Screening.do
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    
    httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    httpResponse.addDateHeader("Expires", 0);
    // Check if a username, password and usertype has been set
    Object usernameObj = httpSession.getAttribute("curam.userName");
    String username = (usernameObj == null) ? "" : usernameObj.toString();
    Object passwordObj = httpSession.getAttribute("curam.password");
    String password = (passwordObj == null) ? "" : passwordObj.toString();
    //if there is no user name then set the user type to be blank
    //written this way to prevent the user type being set when no user name exists
    //as the user type could be hardcoded on the server side even if the user name is not set
    Object usertypeObj = httpSession.getAttribute("curam.userType");
    String usertype = (usernameObj == null) ? "" : usertypeObj.toString();
    
    
    if (!citizenworkspace.util.StringHelper.isEmpty(username)) {
    try {
    password  = curam.util.security.EncryptionUtil.decryptDBPassword(password);
    } catch(Exception e) {
    e.printStackTrace(System.out);
    // TODO 
    // Create a Citizen Workspace error page
    response.sendRedirect("logonerror.jsp");
    return; 
    }
    }
    
    if (citizenworkspace.util.StringHelper.isEmpty(username)) {
    pageContext.setAttribute("username", citizenworkspace.pageplayer.PagePlayerProperties.getPublicUsername());         
    } else {
    pageContext.setAttribute("username", username);     
    }
    
    if (citizenworkspace.util.StringHelper.isEmpty(password)) {
    pageContext.setAttribute("password", citizenworkspace.pageplayer.PagePlayerProperties.getPublicPassword());
    } else {
    pageContext.setAttribute("password", password);        
    }
    
    if (citizenworkspace.util.StringHelper.isEmpty(usertype)) {
    pageContext.setAttribute("usertype", citizenworkspace.pageplayer.PagePlayerProperties.getPublicUsertype());   
    } else {
    pageContext.setAttribute("usertype", usertype);
    }
    
    String pageContextUsername = pageContext.getAttribute("username").toString();
    
    java.security.Principal userPrincipal = hsRequest.getUserPrincipal();
    
    Object expectedSessionTimeoutDontRedirectObj = httpSession.getAttribute("expectedSessionTimeoutDontRedirect");
    String expectedSessionTimeoutDontRedirectAttr = (expectedSessionTimeoutDontRedirectObj == null) ? "" : expectedSessionTimeoutDontRedirectObj.toString();
    
    if(!expectedSessionTimeoutDontRedirectAttr.equals("true") &amp;&amp; userPrincipal==null &amp;&amp; pageContext.getAttribute("username").equals("publiccitizen")) {
    
    /*
    * If this clause gets executed, it means the session expired. The default behaviour upon session expiry is to redirect
    * to the landing page. The only session expiry where we don't want this behaviour is where we explicitly kill the session,
    * as opposed to an automatic session expiry. An example of explicitly killing the session is in Resolve.jspx before we switch from
    * publiccitizen to the generated user account.
    *
    * We check the session for expectedSessionTimeoutDontRedirect because its presence signifies that we explicitly killed the session
    * and that we don't want the redirect to /CitizenPortal/application.do to happen. This is necessary to avoid WebLogic redirecting
    * to the landing page when clicking Apply For Benefits on the landing page.
    *
    * We check if the user principal is null because the user principal will be null after session expiry. If the user principal is not null,
    * this invokation of logon.jsp was invoked for some reason other than session expiry. In that instance, we allow the logon to happen uninterrupted.
    *
    * We check if the user to be logged in is publiccitizen because if the user is anyone else, this invokation of logon.jsp is not
    * as a result of session expiry. There are instances where we manually invalidate a session and the redirect to logon.jsp having
    * put the user to be logged in in the new session (eg CitizenWorkspace_userLogon). In this instance, the userPrincipal would be null
    * and without the check in pageContext.getAttribute("username"), we'd have to set expectedSessionTimeoutDontRedirectAttr more widely.
    * 
    */
    Object doFullLoginAttrObj = httpSession.getAttribute("doFullLogin");
    String doFullLoginAttr = (doFullLoginAttrObj == null) ? "" : doFullLoginAttrObj.toString();
    
    
    if(!doFullLoginAttr.equals("true")) {
    /*
    * doFullLoginAttr is not set, therefore set it and redirect to application.do (and then to here),
    * instead of doing the login.
    * Without doFullLoginAttr, this flow would be an infinite loop.
    */
    httpSession.setAttribute("doFullLogin", "true");
    
    /*
    * We need to redirect to application.do but we can't use a regular response.sendRedirect() because                          
    * we may be in an iframe. If we are in a iframe and we redirect to application.do, we'll end up
    * with two banners on the landing page. The solution to avoiding that problem is to set a variable
    * which signifies that a redirect is required. The actual redirect will happen in the javascript below,             
    * relative to the context logon.jsp is invoked, ie in a iframe or not in an iframe.
    */
    pageContext.setAttribute("redirectRequired", "true");     
    
    } else {
    /*
    * doFullLoginAttr is set, therefore this invokation of logon.jsp was done from another logon.jsp.
    * Do nothing here and allow the login to progress.
    */
    //No redirect, proceed with login
    pageContext.setAttribute("redirectRequired", null);     
    
    // reset the doFullLogin session attribute
    httpSession.setAttribute("doFullLogin", null);                                       
    }		   
    }
    
    // reset the expectedSessionTimeoutDontRedirect parameter
    httpSession.setAttribute("expectedSessionTimeoutDontRedirect", null);                                       
    
  </jsp:scriptlet>
  <jsp:scriptlet>
    
    
    
    
    boolean pageRedirected = false;
    
    // Get the URL parameters.
    String verify_cookie = hsRequest.getParameter("cookies");
    
    String cookie_str = "COOKIES_ENABLED";
    
    // If a test cookie was just added.
    if (verify_cookie != null) {
    
    // Verify that the test cookie exists.
    Cookie cookies[] = hsRequest.getCookies();
    Cookie myCookie = null;
    if (cookies != null) {
    for (int i = 0; i &lt; cookies.length; i++) {
    if (cookies[i].getName().equals(cookie_str)) {
    myCookie = cookies[i];
    
    break;
    }
    }
    }
    
    // If the test cookie cannot be retrieved.
    if (myCookie == null) {
    // Cookies are disabled.
    
    // Inform the user that cookies have to be enabled.
    
    
    
    RequestDispatcher rd = hsRequest.getRequestDispatcher("nocookiejavascript.jsp");
    
    rd.forward(hsRequest, httpResponse);
    pageRedirected = true;
    }
    }
    else {
    // See if a test cookie already exists.
    Cookie cookies[] = hsRequest.getCookies();
    Cookie myCookie = null;
    if (cookies != null) {
    for (int i = 0; i &lt; cookies.length; i++) {
    if (cookies[i].getName().equals(cookie_str)) {
    myCookie = cookies[i];
    
    break;
    }
    }
    }
    
    // If there's no existing test cookie that can be retrieved.
    if (myCookie == null) {
    
    // Add a test cookie.
    Cookie cookie = new Cookie(cookie_str, "OK");
    cookie.setMaxAge(-1);
    httpResponse.addCookie(cookie);
    
    // Add a URL parameter to identify that a test cookie was just added.
    StringBuffer url = hsRequest.getRequestURL();
    String urlParams = hsRequest.getQueryString();
    
    if (urlParams == null) {
    url.append("?cookies=1");
    }
    else {
    url.append("?" + urlParams + "&amp;cookies=1");
    }
    
    httpSession.invalidate();
    // Redirect the browser to this same page, so that the test cookie
    // can be verified.
    httpResponse.sendRedirect(httpResponse.encodeRedirectURL(url.toString()));
    pageRedirected = true;
    }
    }
    
    
    
    
    
  </jsp:scriptlet>
  
  
  
  
  
  
  
  
  
  
  <noscript>
    <h2>
      <center>
        <jsp:scriptlet>
          <![CDATA[out.print(curam.omega3.util.CDEJResources.getProperty(
                          "curam.omega3.i18n.Logon",
                          "Logon.javascriptdisabled.message"));
                          
                       
                          ]]>
        </jsp:scriptlet>
      </center>
    </h2>
  </noscript>
  <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
  
  <div class="logonBody" style="visibility: hidden;">
    <form id="loginform" name="loginform" action="j_security_check" method="post">
      <input type="hidden" name="j_username" value="${fn:escapeXml(pageScope.username)}" />
      <input type="hidden" name="j_password" value="${fn:escapeXml(pageScope.password)}" />
      <input type="hidden" name="user_type" value="${fn:escapeXml(pageScope.usertype)}" />        
      <input type="hidden" id="redirectRequired" name="redirectRequired" value="${fn:escapeXml(pageScope.redirectRequired)}" />
    </form>      
  </div>
  <script type="text/javascript">
    
    /*
    * This javascript will always get executed. 
    * Either a redirect will be invoked or a the login will be submitted.
    */ 
    var redirectRequired = document.getElementById("redirectRequired").value;
    
 // BEGIN, CR00468322, MR
    
    var citizenPortalContextpath="${pageContext.request.contextPath}/application.do";
    
    
    if(redirectRequired) {
    if(window.top!=window.self) {
    // iframe
    window.top.location.href=citizenPortalContextpath;          
    }
    else {
    // no iframe
    window.location.href=citizenPortalContextpath;
    }
    }
    else {
    document.getElementById("loginform").submit();            
    }
 // END, CR00468322
    
  </script>   
</jsp:root>
