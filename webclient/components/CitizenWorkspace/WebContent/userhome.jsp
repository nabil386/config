<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns="http://www.w3.org/1999/xhtml" version="2.0">
  <jsp:directive.page isErrorPage="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" isELIgnored="false" language="java" buffer="32kb" errorPage="/en/ErrorPage.do"/>
  <jsp:directive.page import="curam.omega3.util.UserUtils"/>
  <jsp:directive.page import="curam.omega3.user.InfrastructureUserPreferences"/>
  <jsp:directive.page import="curam.omega3.user.UserPreferencesFactory"/>
  <jsp:directive.page import="curam.omega3.user.UserPreferences"/>
  <jsp:directive.page import="java.util.logging.Logger"/>
  <jsp:directive.page import="curam.interfaces.CitizenWorkspacePkg.CitizenWorkspace_processContrastMode_TH"/><jsp:output omit-xml-declaration="yes"/>
  <jsp:scriptlet>
    <![CDATA[
      // set logger
      final Logger logger = Logger.getLogger(this.getClass().getName());      		
      out.print("<script>top.CuramExternalApp._reloadBanner();</script>");
      logger.log(java.util.logging.Level.INFO,"<script>top.CuramExternalApp._reloadBanner();</script>");
			  
      UserPreferences prefs = UserPreferencesFactory.getUserPreferences(session);      		
      pageContext.setAttribute("highContrastMode",
      prefs.getUserPreference(InfrastructureUserPreferences.HIGH_CONTRAST_MODE));		  
		
      // Scriptlet to redirect to homepage if user is logged in.
      javax.servlet.http.HttpServletRequest hsRequest;
      javax.servlet.http.HttpSession httpSession;
      java.security.Principal userPrincipal;
      hsRequest = (javax.servlet.http.HttpServletRequest) request;
      httpSession = hsRequest.getSession();
      userPrincipal = hsRequest.getUserPrincipal();
      
      UserUtils.setUserPreferences(hsRequest.getSession());

      String url = request.getContextPath() + "/cw/";       
      
      String playerID = request.getParameter("id");
      
      Object pageNameObj = httpSession.getAttribute("curam.page.name");
      String pageName = "";
      if(pageNameObj != null) {
        pageName = pageNameObj.toString();
      }
      
      Object pageTypeObj = httpSession.getAttribute("curam.page.type");
      String pageType = "";
      if(pageTypeObj != null) {
        pageType = pageTypeObj.toString();
      }

      Object paramsObj = httpSession.getAttribute("curam.page.params");
      String params = "";
      if(paramsObj != null) {
        params = paramsObj.toString();
      }

      Object playerExecutionIDObj = httpSession.getAttribute("curam.page.playerExecutionID");
      String playerExecutionID = "";
      if(playerExecutionIDObj != null) {
        playerExecutionID = playerExecutionIDObj.toString();
      }

      Object intakeIEGExecutionIDObj = httpSession.getAttribute("curam.page.intakeIEGExecutionID");
      String intakeIEGExecutionID = "";
      if(intakeIEGExecutionIDObj != null) {
        intakeIEGExecutionID = intakeIEGExecutionIDObj.toString();
      }

      Object screeningIDObj = httpSession.getAttribute("curam.page.screeningID");
      String screeningID = "";
      if(screeningIDObj != null) {
        screeningID = screeningIDObj.toString();
      }

      Object programIDsObj = httpSession.getAttribute("curam.page.programIDs");
      String programIDs = "";
      if(programIDsObj != null) {
        programIDs = programIDsObj.toString();
      }

      Object applicationIDObj = httpSession.getAttribute("curam.page.applicationID");
      String applicationID = "";
      if(applicationIDObj != null) {
        applicationID = applicationIDObj.toString();
      }
      
      Object applicationTypeIDObj = httpSession.getAttribute("curam.page.applicationTypeID");
      String applicationTypeID = "";
      if(applicationTypeIDObj != null) {
        applicationTypeID = applicationTypeIDObj.toString();
      }      

      Object citzAcctPageNameObj = httpSession.getAttribute("curam.page.acct");
      String acctPageName = "";
      if(citzAcctPageNameObj != null) {
        acctPageName = citzAcctPageNameObj.toString();
      }

      Object citzAcctPageTypeObj = httpSession.getAttribute("curam.page.acctPageType");
      String acctPageType = "";
      if(citzAcctPageTypeObj != null) {
        acctPageType = citzAcctPageTypeObj.toString();
      }

      // Clear the page session value
      httpSession.setAttribute("curam.page.name", null);
      httpSession.setAttribute("curam.page.type", null);
      httpSession.setAttribute("curam.page.params", null);
      
      if (pageType.equalsIgnoreCase("UIM")) {
      	       	 
         out.print("<script>top.displayContent({pageID:'" + curam.omega3.request.RequestUtils.escapeURL(pageName) + "'})</script>");
         logger.log(java.util.logging.Level.INFO, "<script>top.displayContent({pageID:'" + curam.omega3.request.RequestUtils.escapeURL(pageName) + "'})</script>");
         
      } else if (!pageType.equalsIgnoreCase("resolve") && citizenworkspace.util.StringHelper.isEmpty(pageName)) {
      
        // In this case the user is being redirected to the application landing page.
        // This should not be a redirect because an iframe may be in use at this point.
        // Instead a valid HTML document is created and javascript is used to set the URL of the top level window.
      
		url = request.getContextPath() + "/application.do";
	    
	    out.print("<html><head><script>window.top.location.href = '" + url + "';</script></head><body></body><br/></html>");
	    logger.log(java.util.logging.Level.INFO, "<html><head><script>window.top.location.href = '" + url + "';</script></head><body></body><br/></html>" );
	
      
      } else {
      
	      if (pageType.equalsIgnoreCase("resolve")) {
	       	
	       	url += "ResolvePage.do?reloadBanner=true";
	       	
	      } else {
		
		url += "PlayerPage.do?reloadBanner=true";    	

	      }
	    
	      if (!params.equals("")) {
	        url +=  citizenworkspace.util.ParameterEncodingHelper.getEncodedPageParms(params) + "&";
	      }

        // If params does not already contain an ID, add playerExecutionID or playerID to the URL
        if(!params.contains("&id=") && !params.contains("?id=")) {
          if (!playerExecutionID.equals("")) {
            url += "&id=" + curam.omega3.request.RequestUtils.escapeURL(playerExecutionID);
          } else if (playerID != null) {
            url += "&id=" + curam.omega3.request.RequestUtils.escapeURL(playerID);
          }
        }
	
	      if (!pageName.equals("") && !pageType.equalsIgnoreCase("UIM")) {
	        url += "&page=" + curam.omega3.request.RequestUtils.escapeURL(pageName);
	      }
	
	      if (!screeningID.equals("")) {
	        url += "&screeningID=" + curam.omega3.request.RequestUtils.escapeURL(screeningID);
	      }
	      
	      if (!programIDs.equals("")) {
	        url += "&programIDs=" + curam.omega3.request.RequestUtils.escapeURL(programIDs);
	      }
	      
	      if (!applicationID.equals("")) {
	        url += "&applicationID=" + curam.omega3.request.RequestUtils.escapeURL(applicationID);
	      }
	      
	      if (!applicationTypeID.equals("")) {
	        url += "&applicationTypeID=" + curam.omega3.request.RequestUtils.escapeURL(applicationTypeID);
	      }	      
	      
	      Object usernameObj = httpSession.getAttribute("curam.userName");    
	      String username = (usernameObj == null) ? "" : usernameObj.toString();
	      Object passwordObj = httpSession.getAttribute("curam.password");
	      String password = (passwordObj == null) ? "" : passwordObj.toString();
	      Object usertypeObj = httpSession.getAttribute("curam.userType");    
	      String usertype = (usertypeObj == null) ? "" : usertypeObj.toString();
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
	
	      // Forward to the page player
	      response.sendRedirect(response.encodeRedirectURL(url));
	   }
	   
  
    ]]>
  </jsp:scriptlet>
</jsp:root>