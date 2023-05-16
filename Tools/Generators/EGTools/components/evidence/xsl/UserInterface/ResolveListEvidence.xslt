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

<xsl:template name="ResolveListEvidence">

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

    <xsl:if test="count($Relationships/MandatoryParents/Parent)&gt;0 or count($Relationships/Parent)&gt;0 or count($Relationships/Association)&gt;0">

      // Retrieve the parent evidence type
      String evidenceType =
        request.getParameter("evidenceType");

      <xsl:if test="count($Relationships/MandatoryParents/Parent)&gt;0 or count($Relationships/Parent)&gt;0">
      if (evidenceType == null) {
        throw new Exception("'evidenceType' parameter does not exist");
      }
        </xsl:if>  

      </xsl:if>  
      
      <!-- BEGIN, CR00190139, POB -->
      String systemParams = rh.getSystemParameters();
      
      <!-- BEGIN, CR00221984, CD -->
      String hlWorkspaceURLInclSysParams = "";
      
      // Retrieve the full query string for this request
      java.util.Enumeration params = request.getParameterNames();
      StringBuffer parameterList = new StringBuffer(25);
      StringBuffer currentParameter = new StringBuffer(25);
      
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
        
        <xsl:text disable-output-escaping="yes">currentParameter.append("&lt;![CDATA[&amp;]]&gt;").append(parameterName).append("=");</xsl:text>
          
          <!-- BEGIN, CR00383917, ELG -->
          currentParameter.append(parameterValue);
          <!-- END, CR00383917 -->
          
          if (parameterName.equals(curam.omega3.request.ParameterTypeRegistry.PREFIX_RETURN_PAGE)) {
            
            // if this came from a high level evidence workspace
            // resolve back to it
            if (parameterValue.contains("Evidence_workspace")) {
              hlWorkspaceURLInclSysParams = parameterValue;
            }
            
          } else {
          
          if (!systemParams.contains(currentParameter)) {
            parameterList.append(currentParameter);
            currentParameter = new StringBuffer(25);
          }
        }
      }
      <!-- END, CR00190139 -->
      
      
      if (parameterList.length() &gt; 0) {
        parameterList.deleteCharAt(0);
      }
      
      if (hlWorkspaceURLInclSysParams.length() > 0) {
        
        String[] urlAndListOfParams = hlWorkspaceURLInclSysParams.split("\\?");
        String hlWorkspaceURLAndUserParamsOnly = urlAndListOfParams[0] + "?";
        
        <xsl:text disable-output-escaping="yes">String[] listOfParams = urlAndListOfParams[1].split("\\&lt;![CDATA[&amp;]]&gt;");</xsl:text>
        
        for (int i=0; i &lt; listOfParams.length; i++) {
          
          if (listOfParams[i].startsWith("participantID=") ||
              listOfParams[i].startsWith("caseID=") ||
              listOfParams[i].startsWith("evidenceType=")) {
            
            <xsl:text disable-output-escaping="yes">hlWorkspaceURLAndUserParamsOnly += listOfParams[i] + "&lt;![CDATA[&amp;]]&gt;";</xsl:text>
          }
        }
        
        response.sendRedirect(response.encodeRedirectURL(url + hlWorkspaceURLAndUserParamsOnly + rh.getSystemParameters()));
      
      } else {
      
        <xsl:variable name="associationsOfInterest" select="$Relationships/Association[@from!='' and @displayInHierarchy='Yes']"/>

        <xsl:if test="$Relationships/@association='Yes'">

        <xsl:for-each select="$associationsOfInterest">  

          <xsl:variable name="ParentType">
            <xsl:call-template name="JavaID2CodeValue"> 
              <xsl:with-param name="java_identifier" select="@type"/>  
              <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
            </xsl:call-template>
          </xsl:variable>   

          <xsl:if test="position()>1">else </xsl:if>if (evidenceType.equals("<xsl:value-of select="$ParentType"/>")) {
            url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>For<xsl:value-of select="@from"/>Page.do?" + parameterList;
          }

        </xsl:for-each>

        </xsl:if>  

        <xsl:for-each select="$Relationships/Parent">  

          <xsl:variable name="ParentType">
            <xsl:call-template name="JavaID2CodeValue">
              <xsl:with-param name="java_identifier" select="@type"/>  
              <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
            </xsl:call-template>
          </xsl:variable>        

          <xsl:if test="(position()>1) or (position()=1 and count($associationsOfInterest)>0)">else </xsl:if>if (evidenceType.equals("<xsl:value-of select="$ParentType"/>")) {
            url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>For<xsl:value-of select="@name"/>Page.do?" + parameterList;
          }

        </xsl:for-each>

        <xsl:for-each select="$Relationships/MandatoryParents/Parent">  

        <xsl:variable name="ParentType">
          <xsl:call-template name="JavaID2CodeValue">
            <xsl:with-param name="java_identifier" select="@type"/>  
            <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
            <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
          </xsl:call-template>
        </xsl:variable>        

        <xsl:if test="(position()>1) or (position()=1 and count($associationsOfInterest)>0)">else </xsl:if>if (evidenceType.equals("<xsl:value-of select="$ParentType"/>")) {
          url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>For<xsl:value-of select="@name"/>Page.do?" + parameterList;
        }

      </xsl:for-each>

        <xsl:if test="count($associationsOfInterest)>0 or count($Relationships/Parent)>0 or count($Relationships/MandatoryParents/Parent)>0">else {</xsl:if>
          // if evidenceType is null or blank or doesn't match one of the parent types above     
          url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>Page.do?" + parameterList;

        <xsl:if test="count($associationsOfInterest)>0 or count($Relationships/Parent)>0 or count($Relationships/MandatoryParents/Parent)>0">}</xsl:if>

        <!-- END, CR00221984, CD -->
        response.sendRedirect(response.encodeRedirectURL(url));
      
      }


  </JSP_SCRIPTLET>

</PAGE>
  
  </redirect:write>

</xsl:template>

</xsl:stylesheet>