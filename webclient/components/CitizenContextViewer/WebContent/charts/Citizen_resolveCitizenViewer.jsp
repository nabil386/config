<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0">

  <jsp:directive.page buffer="32kb" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" />
  <jsp:directive.page import="curam.util.client.BidiUtils"/>
  <jsp:output omit-xml-declaration="yes" />
  <jsp:text>
    <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">]]>
  </jsp:text>
  <jsp:scriptlet>
    <![CDATA[
      String contextType = request.getParameter("contextType");
      if (contextType == null) {
        contextType = "";
      }
      String concernRoleID = request.getParameter("concernRoleID");

      curam.omega3.user.UserPreferences prefs = curam.omega3.user.UserPreferencesFactory
          .getUserPreferences(session);
      String locale = prefs.getLocale().toString();
      locale = locale.replace("_", "-");
      pageContext.setAttribute("locale" , locale);

      String encodedConcernRoleID = curam.omega3.request.RequestUtils
          .escapeURL(concernRoleID);
      String encodedContextType = curam.omega3.request.RequestUtils
          .escapeURL(contextType);
      if(encodedContextType == null){
    	  encodedContextType ="";
      }

      String htmlBody =
      "<body  style=\"overflow: scroll;  min-height: 100%\" class='spm-component' onload='showContextViewer(\"CitizenContext\",\""+encodedConcernRoleID+"\",\""+encodedContextType+"\",\""+locale+"\")'>"+
     	"<main><div id='CitizenContext'></div> </main> "+
      "</body>";
      

    ]]>
 </jsp:scriptlet>
 <curam:userPreferences/>

<html lang="${locale}" dir="${htmlDirection}">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Citizen Context Viewer</title>
      <script type="text/javascript" src="../CDEJ/jscript/SPMUIComponents/spm-uicomponents-main.bundle.js">
     // Some code
    </script>
 
 	<script type="text/javascript">
    
      function setTitle(title) {
        document.title = title;
      }
    </script>
   	<script type="text/javascript" src="../CDEJ/jscript/ContextViewerPopup.js">
	 // Some code
	</script>	
    <style type="text/css">
      body {
        margin: 0px;
        overflow: hidden;
      }
    </style>
  </head>
  <jsp:scriptlet>
    <![CDATA[
       out.print(htmlBody);
    ]]>
  </jsp:scriptlet>

</html>

</jsp:root>
