<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008, 2010 Curam Software Ltd.  All rights reserved.

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
  
  <xsl:include href="Create/CreateUtilityTemplates.xslt"/>
  
  <xsl:template name="ResolveEvidenceCreate">
  
    <xsl:variable name="component"><xsl:value-of select="//EvidenceEntities/@product"/></xsl:variable>
    <!-- BEGIN, CR00214375, PB -->
    <xsl:variable name="prefix"><xsl:value-of select="//EvidenceEntities/@prefix"/></xsl:variable>
    <!-- END, CR00214375 -->
    
    <xsl:variable name="pagename"><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$caseType"/>Create</xsl:variable>
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
       
    String url = "";
    
    String caseID = request.getParameter("caseID");
    String evidenceType = request.getParameter("evidenceType");
    String contextDescription = request.getParameter("contextDescription");

    if (evidenceType == null) {
      throw new Exception("'evidenceType' parameter does not exist");
    }
    
    <!-- BEGIN, CR00383917, ELG -->
    if (caseID == null) {
      throw new Exception("'caseID' parameter does not exist");
    } else {
      // sanitize parameter value
      caseID = curam.omega3.request.RequestUtils.escapeURL(caseID);
    }
    <!-- END, CR00383917 -->
         
    if (contextDescription == null) {
      throw new Exception("'contextDescription' parameter does not exist");
    } 
    
    //
    // get the pagename that linked to here
    //
    
    String from = "";
    
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
      
      if (parameterName.equals(curam.omega3.request. 
                                 ParameterTypeRegistry.PREFIX_RETURN_PAGE)) {

        if (parameterValue.contains("Evidence_workspaceActive")) {
          from = "_fromActive";          
        } else if (parameterValue.contains("Evidence_workspaceInEdit")) {
          from = "_fromInEdit";          
        }          

      }
    }
    
   	<!-- BEGIN, 191675, JAY -->
   	// call server to find evidence eligible for auto end date wizard
	curam.interfaces.EvidencePkg.Evidence_isEvdEligibleForAutoEndDate_TH
	  th = new curam.interfaces.EvidencePkg.Evidence_isEvdEligibleForAutoEndDate_TH();
	
	th.setFieldValue(th.key$evidenceType_idx,evidenceType);
	th.setFieldValue(th.key$caseID_idx,caseID);
	
	th.callServer();
     
  	boolean autoEndDateEligibleInd = Boolean.valueOf(th.getFieldValue(th.result$autoEndDateEligibleInd_idx)).booleanValue();
    <!-- END, 191675, JAY -->
    
   <xsl:for-each select="//EvidenceEntities/EvidenceEntity">
      
      <xsl:variable name="entityElement" select="."/>
      
      <xsl:variable name="evidenceType">
        <xsl:call-template name="JavaID2CodeValue">
          <xsl:with-param name="java_identifier" select="@name"/>
          <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
          <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
        </xsl:call-template>
      </xsl:variable>
      
      <!-- get create page name -->
      <xsl:variable name="createStartPageName"><xsl:call-template name="Utilities-getCreatePageName-Evidence_resolveCreate">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>
      
      <!-- BEGIN, 191675, JAY -->
      <!-- get create wizard start page name for evidence with business end date -->
      <xsl:variable name="endDateWizardApplicable"><xsl:call-template name="Utilities-Entity-EndDateWizardApplicable">
      	<xsl:with-param name="entityElement" select="$entityElement"/>
      </xsl:call-template></xsl:variable>
      <!-- END, 191675, JAY -->
    <xsl:if test="position()>1">else </xsl:if>if (evidenceType.equals("<xsl:value-of select="$evidenceType"/>")) {        
      	url = context + "<xsl:value-of select="$createStartPageName"/>" + "Page.do?caseID=" + caseID;
      	 <!-- BEGIN, 191675, JAY -->
      	 <xsl:if test="$endDateWizardApplicable='true'">
      		<xsl:variable name="createEndDateWizardStartPageName"><xsl:call-template name="Utilities-CreateWizards-WithEndDate-getStartingPage">
        		<xsl:with-param name="entityElement" select="$entityElement"/>
        		<xsl:with-param name="caseType" select="$caseType"/>
      		</xsl:call-template></xsl:variable>
      	if(autoEndDateEligibleInd) {
      		
      		url = context + "<xsl:value-of select="$createEndDateWizardStartPageName"/>" + "Page.do?caseID=" + caseID + "&amp;" + "evidenceType=" + evidenceType;
      	}
      	</xsl:if>
      	<!-- END, 191675, JAY -->
      }
    </xsl:for-each>
    else {
      url = context + "Evidence_overrideResolveWarningPage.do?";
    }
    
    // BEGIN, CR00098930, DG
    // Append the system parameters to the URL string. This is required to ensure the modal
    // dialog boxes work correctly.
    url += "&amp;" + rh.getSystemParameters();
    // END, CR00098930
            
   
    response.sendRedirect(response.encodeRedirectURL(url));
  
    
  </JSP_SCRIPTLET>
</PAGE>

      </redirect:write>
      <redirect:close select="$filename"/>

</xsl:template>

</xsl:stylesheet>