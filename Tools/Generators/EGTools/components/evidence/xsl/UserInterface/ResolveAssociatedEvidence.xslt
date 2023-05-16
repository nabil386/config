<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:output method="xml" indent="yes"/>

<xsl:template name="ResolveAssociatedEvidence">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="UIName"/>

  <xsl:param name="capName" />


  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <redirect:write select="concat($filepath, '.uim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE  PAGE_ID="{$UIName}">
  <JSP_SCRIPTLET>

      String context = request.getContextPath() + "/";
      context +=
        curam
        .omega3
        .user
        .UserPreferencesFactory
        .getUserPreferences(pageContext.getSession()).getLocale()
        + "/";

      // Check that all parameters are present.

      // Retrieve the parameters
      String caseID = request.getParameter("caseID");
      String contextDescription = request.getParameter("contextDescription");
      String linkedEvID = request.getParameter("linkedEvID");
      String linkedEvType = request.getParameter("linkedEvType");
      String evidenceID = request.getParameter("evidenceID");
      String evidenceType = request.getParameter("evidenceType");

      // Check that the parameters were supplied
      if (caseID == null) {
        throw new Exception("'caseID' parameter does not exist");
      }
      if (contextDescription == null) {
        throw new Exception("'contextDescription' parameter does not exist");
      }
      if (linkedEvID == null) {
        throw new Exception("'linkedEvID' parameter does not exist");
      }
      if (linkedEvType == null) {
        throw new Exception("'linkedEvType' parameter does not exist");
      }
      if (evidenceID == null) {
        throw new Exception("'evidenceID' parameter does not exist");
      }
      if (evidenceType == null) {
        throw new Exception("'evidenceType' parameter does not exist");
      }

      // Retrieve the full query string for this request
      java.util.Enumeration params = request.getParameterNames();
      StringBuffer queryBuffer = new StringBuffer(25);
      while (params.hasMoreElements()) { 
       
        <!-- BEGIN, CR00389891, ELG -->
        // sanitize parameter name
        String parameterName = curam.omega3.request.RequestUtils.escapeURL((String) params.nextElement());
        <!-- END, CR00389891 -->
        
        String parameterValue = request.getParameter(parameterName);
        
        <!-- BEGIN, CR00383917, ELG -->
        if (null != parameterValue) {
          // sanitize parameter value
          parameterValue = curam.omega3.request.RequestUtils.escapeURL(parameterValue);
        }
        <!-- END, CR00383917 -->
        
        queryBuffer.append("&amp;").append(parameterName).append("=");
        queryBuffer.append(parameterValue);
       
      }
      
      if (queryBuffer.length() &gt; 0) {
        queryBuffer.deleteCharAt(0);
      }

      String url = context;

      // If the evidenceID is 0 then URL is set to view page,
      // otherwise redirect to the list page.
      if (!evidenceID.equals("0")) {
        url+= "<xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>SnapshotPage.do?" + queryBuffer;
      } else {
        url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>ForAssociationPage.do?" + queryBuffer;
      }

      // Send the response
      response.sendRedirect(response.encodeRedirectURL(url));

    
  </JSP_SCRIPTLET>

</PAGE>
  
  </redirect:write>

</xsl:template>

</xsl:stylesheet>