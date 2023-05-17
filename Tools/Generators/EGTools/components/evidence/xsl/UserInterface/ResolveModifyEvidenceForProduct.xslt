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
    
    <xsl:template name="ResolveModifyEvidenceForProduct">
        
        <xsl:param name="path"/>
        <xsl:param name="prefix"/>
        <xsl:param name="UIName"/>        
        <xsl:param name="capName" />
        
        <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>
        
        
        <redirect:write select="concat($filepath, '.uim')">
            
            <xsl:call-template name="printXMLCopyright">
              <xsl:with-param name="date" select="$date"/>
            </xsl:call-template>
            <PAGE PAGE_ID="{$UIName}">
                <JSP_SCRIPTLET>
                    
                    curam.omega3.request.RequestHandler 
                    rh = curam.omega3.request.RequestHandlerFactory.getRequestHandler(request);
                    
                    String context = request.getContextPath() + "/";
                    context += curam.omega3.user.UserPreferencesFactory.getUserPreferences(pageContext.getSession()).getLocale() + "/";
                    String caseID = request.getParameter("caseID");
                    String evID = request.getParameter("evidenceID");
                    String evType = request.getParameter("evidenceType");
                    String evidenceDescriptorID = request.getParameter("evidenceDescriptorID");
                    String contextDescription = request.getParameter("contextDescription");
                    
                    String parEvID = request.getParameter("parEvID");
                    String parEvType = request.getParameter("parEvType");
                    String grandParEvID = request.getParameter("grandParEvID");
                    String grandParEvidenceDescID = request.getParameter("grandParEvidenceDescID");
                    String grandParEvType = request.getParameter("grandParEvType");
                    
                    String pageName="";
                    String caseType="";
                    String url = "";
                    String integratedCaseID = "0";
                    
                    <!-- BEGIN, CR00383917, ELG -->
                    if (caseID == null) {
                      throw new Exception("'caseID' parameter does not exist");
                    } else {
                      // sanitize parameter value
                      caseID = curam.omega3.request.RequestUtils.escapeURL(caseID);
                    }
                    
                    if (evID == null) {
                      throw new Exception("'evidenceID' parameter does not exist");
                    } else {
                      // sanitize parameter value
                      evID = curam.omega3.request.RequestUtils.escapeURL(evID);
                    }
                    
                    if (evType == null) {
                      throw new Exception("'evidenceType' parameter does not exist");
                    } else {
                      // sanitize parameter value
                      evType = curam.omega3.request.RequestUtils.escapeURL(evType);
                    }
                    
                    if (null != contextDescription) {
                      // sanitize parameter value
                      contextDescription = curam.omega3.request.RequestUtils.escapeURL(contextDescription);
                    }
                    
                    if (null != parEvID) {
                      // sanitize parameter value
                      parEvID = curam.omega3.request.RequestUtils.escapeURL(parEvID);
                    }
                    
                    if (null != parEvType) {
                      // sanitize parameter value
                      parEvType = curam.omega3.request.RequestUtils.escapeURL(parEvType);
                    }
                    
                    if (null != grandParEvID) {
                      // sanitize parameter value
                      grandParEvID = curam.omega3.request.RequestUtils.escapeURL(grandParEvID);
                    }
                    
                    if (null != grandParEvType) {
                      // sanitize parameter value
                      grandParEvType = curam.omega3.request.RequestUtils.escapeURL(grandParEvType);
                    }
                    <!-- END, CR00383917 -->
                    
                    if (((parEvID != null) &amp;&amp; (parEvType == null)) ||
                       ((parEvID == null) &amp;&amp; (parEvType != null))) {
                      throw new Exception("'parEvID' and 'parEvType': cannot specify one without the other");
                    }
                    
                    if (((grandParEvID != null) &amp;&amp; (grandParEvType == null)) ||
                        ((grandParEvID == null) &amp;&amp; (grandParEvType != null))) {
                      throw new Exception("'grandParEvID' and 'grandParEvType': cannot specify one without the other");
                    }
                    
                    if ((parEvID == null) &amp;&amp; (grandParEvID != null)) {
                      throw new Exception("Cannot specify 'grandParEvID' without specifying 'parEvID'");
                    }
                                        
                    curam.interfaces.FinancialPkg.Financial_readIntegratedCaseIDByCaseID_TH th = new curam.interfaces.FinancialPkg.Financial_readIntegratedCaseIDByCaseID_TH();
                    
                    th.setFieldValue(th.resolveIntegratedCaseKey$caseKey$caseID_idx,caseID);
                    
                    th.callServer();
                    
                    integratedCaseID = th.getFieldValue(th.result$integratedCaseID$integratedCaseID_idx);
                    
                    if(integratedCaseID.compareTo("0")!=0){pageName="<xsl:value-of select="$prefix"/>_modify<xsl:value-of select="$capName"/>ProductEvidence";}
                    else {pageName="<xsl:value-of select="$prefix"/>_modify<xsl:value-of select="$capName"/>Evidence";}
                    
                    // BEGIN, CR00001077, DK
                    if(contextDescription == null){
                    
                    curam.interfaces.EvidencePkg.Evidence_getContextDescription_TH 
                    thContextDescription = new curam.interfaces.EvidencePkg.Evidence_getContextDescription_TH();
                    thContextDescription.setFieldValue(thContextDescription.key$caseID_idx, caseID);
                    thContextDescription.setFieldValue(thContextDescription.key$evidenceType_idx, evType);
                    
                    thContextDescription.callServer();
                    contextDescription = thContextDescription.getFieldValue(thContextDescription.result$contextDescription_idx);
                    
                    }
                    // END, CR00001077 
                    
                    url = context + pageName + "Page.do?caseID=" + caseID + "&amp;evidenceID=" + evID + "&amp;evidenceType=" + evType + "&amp;contextDescription=" + contextDescription ;    
                    if (parEvID != null) {
                    url = url + "&amp;parEvID=" + parEvID + "&amp;parEvType=" + parEvType;
                    if (grandParEvID != null) {
                    url = url + "&amp;grandParEvID=" + grandParEvID + "&amp;grandParEvType=" + grandParEvType;
                    }
                    }
                    
                    url += "&amp;" + rh.getSystemParameters();  
                    response.sendRedirect(response.encodeRedirectURL(url));
                    
                    
                </JSP_SCRIPTLET>
            </PAGE>
        </redirect:write>
        
    </xsl:template>
    
</xsl:stylesheet>