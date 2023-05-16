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

<xsl:template name="ResolveChildEvidence">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="UIName"/>

  <xsl:param name="capName" />

  <xsl:param name="Relationships"/>
  <xsl:param name="fromCreate" />


  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <redirect:write select="concat($filepath, '.uim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE  PAGE_ID="{$UIName}">
  <JSP_SCRIPTLET>

      // Get the context details
      String context = request.getContextPath() + "/";
      curam.omega3.request.RequestHandler rh =
        curam.omega3.request.RequestHandlerFactory.getRequestHandler(
          request);
      context
        += curam
          .omega3
          .user
          .UserPreferencesFactory
          .getUserPreferences(pageContext.getSession())
          .getLocale()
        + "/";

      // Retrieve the child evidence type
      String linkedEvType =
        request.getParameter("linkedEvType");
      if (linkedEvType == null) {
        throw new Exception("'linkedEvType' parameter does not exist");
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
      
      <xsl:for-each select="$Relationships/Association[@to!='' and @displayInHierarchy='No']">  

        <xsl:variable name="ChildType">
          <xsl:call-template name="JavaID2CodeValue"> 
            <xsl:with-param name="java_identifier" select="@type"/>  
            <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
            <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
          </xsl:call-template>
        </xsl:variable>
        
      if (linkedEvType.equals("<xsl:value-of select="$ChildType"/>")) {
        url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>For<xsl:value-of select="@to"/><xsl:value-of select="$caseType"/>Association<xsl:value-of select="$fromCreate"/>Page.do?" + queryBuffer;
      }
  
      </xsl:for-each>

      response.sendRedirect(response.encodeRedirectURL(url));


  </JSP_SCRIPTLET>

</PAGE>
  
  </redirect:write>

</xsl:template>

</xsl:stylesheet>