<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2011 Curam Software Ltd.  All rights reserved.

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
<!--  <xsl:include href="JavaID2CodeValue.xslt" />-->
  
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="ResolveEvidenceObjectVerifications">
    
    <xsl:variable name="component"><xsl:value-of select="//EvidenceEntities/@product"/></xsl:variable>
    
    <xsl:variable name="prefix"><xsl:value-of select="//EvidenceEntities/@prefix"/></xsl:variable>
    <xsl:variable name="pagePostfix">Verifications</xsl:variable>
    
    <xsl:variable name="pagename"><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$caseType"/>Object<xsl:value-of select="$pagePostfix"/></xsl:variable>
    <xsl:variable name="filename"><xsl:value-of select="$pagename"/>.uim</xsl:variable>
    
      <redirect:open select="$filename" method="xml" append="false" />
      <redirect:write select="$filename">
      
<xsl:call-template name="printXMLCopyright">
 <xsl:with-param name="date" select="$date"/>
</xsl:call-template>
<xsl:comment> Description                                                            </xsl:comment>
<xsl:comment> ===========                                                            </xsl:comment>
<xsl:comment> This script opens the sample evidence view pages.                      </xsl:comment>

<PAGE PAGE_ID="{$pagename}">
  <JSP_SCRIPTLET>
    
    curam.omega3.request.RequestHandler 
      rh = curam.omega3.request.RequestHandlerFactory.getRequestHandler(request);

    String context = request.getContextPath() + "/";
    context += curam.omega3.user.UserPreferencesFactory.getUserPreferences(pageContext.getSession()).getLocale() + "/";

    String evidenceType = request.getParameter("evidenceType");

    <!-- BEGIN, CR00383917, ELG -->
    if (evidenceType == null) {
      throw new Exception("'evidenceType' parameter does not exist");
    } else {
      // sanitize parameter value
      evidenceType = curam.omega3.request.RequestUtils.escapeURL(evidenceType);
    }
    <!-- END, CR00383917 -->

    String url = "";
    String successionID = request.getParameter("successionID");

    <!-- BEGIN, CR00383917, ELG -->
    if (successionID == null) {
      throw new Exception("'successionID' parameter does not exist");
    } else {
      // sanitize parameter value
      successionID = curam.omega3.request.RequestUtils.escapeURL(successionID);
    }
    <!-- END, CR00383917 -->

    //
    // Get the main portion of the url based on the 'evidenceType'
    //

    <xsl:for-each select="//EvidenceEntities/EvidenceEntity">

      <xsl:variable name="evidenceType">
        <xsl:call-template name="JavaID2CodeValue">
          <xsl:with-param name="java_identifier" select="@name"/>
          <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
        <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
        </xsl:call-template>
      </xsl:variable>
      <xsl:if test="position()>1">else </xsl:if>if (evidenceType.equals("<xsl:value-of select="$evidenceType"/>")) {        
      url = context;        

      url += "<xsl:value-of select="$prefix"/>_list<xsl:value-of select="@name"/><xsl:value-of select="$caseType"/>Obj<xsl:value-of select="$pagePostfix"/>" 
             + "Page.do?successionID=" + successionID;
      url += "&amp;";
    }
    </xsl:for-each>    

    else {        
      url = context + "Evidence_overrideResolveWarningPage.do?";        
    }  


  url += rh.getSystemParameters();
  response.sendRedirect(response.encodeRedirectURL(url));
  
    
  </JSP_SCRIPTLET>
</PAGE>

      </redirect:write>
      <redirect:close select="$filename"/>

</xsl:template>

</xsl:stylesheet>