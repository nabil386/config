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

<xsl:template name="ResolveCreateEvidenceFromParentCreate">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="UIName"/>

  <xsl:param name="capName" />

  <xsl:param name="Relationships"/>


  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <redirect:write select="concat($filepath, '.uim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE  PAGE_ID="{$UIName}">
  <JSP_SCRIPTLET>

      // Get the context details
      String url = request.getContextPath() + "/";
      curam.omega3.request.RequestHandler rh =
        curam.omega3.request.RequestHandlerFactory.getRequestHandler(
          request);
      url
        += curam
          .omega3
          .user
          .UserPreferencesFactory
          .getUserPreferences(pageContext.getSession())
          .getLocale()
        + "/";

    <xsl:if test="count($Relationships/MandatoryParents/Parent)&gt;0">

      // Retrieve the parent evidence type
      String parEvType =
        request.getParameter("parEvType");

      if (parEvType == null) {
        throw new Exception("'parEvType' parameter does not exist");
      }  

      </xsl:if>  
      
      String hlWorkspace = "";

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
        <xsl:text disable-output-escaping="yes">queryBuffer.append("&lt;![CDATA[&amp;]]&gt;").append(parameterName).append("=");</xsl:text>
        
        queryBuffer.append(parameterValue);
        
        if (parameterName.equals(curam.omega3.request. 
                                   ParameterTypeRegistry.PREFIX_RETURN_PAGE)) {
          // if this came from a high level evidence workspace
          // resolve back to it
          if (parameterValue.contains("Evidence_workspace")) {
            hlWorkspace = parameterValue;
          }
          
        }
      
      }
      
      if (queryBuffer.length() &gt; 0) {
        queryBuffer.deleteCharAt(0);
      }
      
      if (hlWorkspace.length() > 0) {
        response.sendRedirect(response.encodeRedirectURL(url + hlWorkspace));
      } else {

        <xsl:for-each select="$Relationships/MandatoryParents/Parent">  

        <xsl:variable name="ParentType">
          <xsl:call-template name="JavaID2CodeValue">
            <xsl:with-param name="java_identifier" select="@type"/>  
            <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
            <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
          </xsl:call-template>
        </xsl:variable>        

          <xsl:if test="position()>1">else </xsl:if>if (parEvType.equals("<xsl:value-of select="$ParentType"/>")) {
          url += "<xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>From<xsl:value-of select="@name"/>Page.do?" + queryBuffer;
        }

      </xsl:for-each>

        <xsl:if test="count($Relationships/MandatoryParents/Parent)>0">else {</xsl:if>
          // if parEvType is null or blank or doesn't match one of the parent types above     
          url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>Page.do?" + queryBuffer;

        <xsl:if test="count($Relationships/MandatoryParents/Parent)>0">}</xsl:if>


        response.sendRedirect(response.encodeRedirectURL(url));
      
      }


  </JSP_SCRIPTLET>

</PAGE>
  
  </redirect:write>

</xsl:template>

</xsl:stylesheet>