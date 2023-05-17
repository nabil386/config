<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    Copyright (c) 2010 Curam Software Ltd.  All rights reserved.
    
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
    
    <xsl:import href="../UICommon.xslt"/>
    <xsl:import href="CreateUtilityTemplates.xslt"/>
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:template name="ResolveCreateEvidenceFromRelated">
        
        <xsl:param name="entityElement"/>
        <xsl:param name="caseType"/>
        <xsl:param name="baseDirectory"/>
        <xsl:param name="fileName"/>
        
        <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/><xsl:value-of select="$fileName"/></xsl:variable>
        
        <redirect:write select="concat($fullFileName, '.uim')">
            
            <!-- add copyright notice -->
            <xsl:call-template name="printXMLCopyright">
              <xsl:with-param name="date" select="$date"/>
            </xsl:call-template>
            
            <PAGE  PAGE_ID="{$fileName}">
                <JSP_SCRIPTLET>
                    
                    curam.omega3.request.RequestHandler 
                    rh = curam.omega3.request.RequestHandlerFactory.getRequestHandler(request);
                    
                    String context = request.getContextPath() + "/";
                    context += curam.omega3.user.UserPreferencesFactory.getUserPreferences(pageContext.getSession()).getLocale() + "/";
                    
                    String successionID = request.getParameter("successionID");
                    
                    String pageName="";
                    String url = context;
                    
                    if (successionID == null) {
                    core.ScriptletMissingParamException e = 
                    new core.ScriptletMissingParamException(-20002, "successionID");
                    System.out.println(e);
                    throw e;
                    }
                    
                    //
                    // get the caseID and evidence id and type of the related record
                    //    
                    
                    curam.interfaces.EvidencePkg.Evidence_getEvidenceAndCaseFromSuccession_TH
                    resolveEvidenceAndCase = new curam.interfaces.EvidencePkg.Evidence_getEvidenceAndCaseFromSuccession_TH();
                    
                    resolveEvidenceAndCase.setFieldValue(resolveEvidenceAndCase.key$successionID_idx, successionID);
                    
                    resolveEvidenceAndCase.callServer();
                    
                    String caseID = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$caseIDKey$caseID_idx);
                    String inputID = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$evidenceKey$evidenceID_idx);
                    String inputType = resolveEvidenceAndCase.getFieldValue(resolveEvidenceAndCase.result$evidenceKey$evType_idx);
                    
                    <xsl:for-each select="$entityElement/Relationships/Parent">
                        
                        <xsl:variable name="currentType">
                            <xsl:call-template name="JavaID2CodeValue"> 
                                <xsl:with-param name="java_identifier" select="@name"/>  
                                <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                                <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
                            </xsl:call-template>
                        </xsl:variable>
                        
                        <xsl:variable name="evidenceIDParam"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="fromRelated" select="@name"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:variable name="evidenceTypeParam"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="fromRelated" select="@name"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:variable name="createPageName"><xsl:call-template name="Utilities-getCreatePageName-ResolveCreateEvidenceFromRelated">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="caseType" select="$caseType"/>
                            <xsl:with-param name="fromRelated" select="@name"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@name"/>
                        url = context 
                        + "<xsl:value-of select="$createPageName"/>Page.do?<xsl:value-of select="$evidenceIDParam"/>="+ curam.omega3.request.RequestUtils.escapeURL(inputID)
                        + "&amp;<xsl:value-of select="$evidenceTypeParam"/>="+ curam.omega3.request.RequestUtils.escapeURL(inputType);
                        }
                    </xsl:for-each>
                    
                    <xsl:for-each select="$entityElement/Relationships/PreAssociation">
                        
                        <xsl:variable name="currentType">
                            <xsl:call-template name="JavaID2CodeValue"> 
                                <xsl:with-param name="java_identifier" select="@to"/>  
                                <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                                <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
                            </xsl:call-template>
                        </xsl:variable>
                        
                        <xsl:variable name="evidenceIDParam"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="fromRelated" select="@to"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:variable name="evidenceTypeParam"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="fromRelated" select="@to"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:variable name="createPageName"><xsl:call-template name="Utilities-getCreatePageName-ResolveCreateEvidenceFromRelated">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="caseType" select="$caseType"/>
                            <xsl:with-param name="fromRelated" select="@to"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@to"/>
                        url = context 
                        + "<xsl:value-of select="$createPageName"/>Page.do?<xsl:value-of select="$evidenceIDParam"/>="+ curam.omega3.request.RequestUtils.escapeURL(inputID)
                        + "&amp;<xsl:value-of select="$evidenceTypeParam"/>="+ curam.omega3.request.RequestUtils.escapeURL( inputType);
                        }
                    </xsl:for-each>
                    
                    <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
                        
                        <xsl:variable name="currentType">
                            <xsl:call-template name="JavaID2CodeValue"> 
                                <xsl:with-param name="java_identifier" select="@name"/>  
                                <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                                <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
                            </xsl:call-template>
                        </xsl:variable>
                        
                        <xsl:variable name="evidenceIDParam"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="fromRelated" select="@name"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:variable name="evidenceTypeParam"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="fromRelated" select="@name"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:variable name="createPageName"><xsl:call-template name="Utilities-getCreatePageName-ResolveCreateEvidenceFromRelated">
                            <xsl:with-param name="entityElement" select="$entityElement"/>
                            <xsl:with-param name="caseType" select="$caseType"/>
                            <xsl:with-param name="fromRelated" select="@name"/>
                        </xsl:call-template></xsl:variable>
                        
                        <xsl:if test="position()=1">if ( </xsl:if><xsl:if test="position()>1">else if ( </xsl:if>inputType.equals("<xsl:value-of select="$currentType"/>") ) { // <xsl:value-of select="@name"/>
                        url = context 
                        + "<xsl:value-of select="$createPageName"/>Page.do?<xsl:value-of select="$evidenceIDParam"/>="+ curam.omega3.request.RequestUtils.escapeURL(inputID)
                        + "&amp;<xsl:value-of select="$evidenceTypeParam"/>="+ curam.omega3.request.RequestUtils.escapeURL( inputType);
                        }
                    </xsl:for-each>
                    
                    url += "&amp;caseID=" + curam.omega3.request.RequestUtils.escapeURL(caseID)
                    + "&amp;" + rh.getSystemParameters(); 
                    
                    // Send the response
                    response.sendRedirect(response.encodeRedirectURL(url));
                </JSP_SCRIPTLET>
            </PAGE>
        </redirect:write>
    </xsl:template>
    
</xsl:stylesheet>