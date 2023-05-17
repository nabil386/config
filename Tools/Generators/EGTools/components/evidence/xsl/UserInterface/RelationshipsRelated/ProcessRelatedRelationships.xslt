<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2014. All Rights Reserved.
 
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
    
    <!-- Global Variables -->
    <xsl:import href="../UICommon.xslt"/>

    
    <!-- Templates for listing Case Participants based on CPR Type  for searching on core entities -->
    <xsl:import href="ListCPRForRelatedEntity.xslt"/>
    
    <!-- Templates for writing the main page for listing all Case Participants for related entities -->
    <xsl:import href="ListEvidenceAndCPRForRelatedEntity.xslt"/>
    
    <!-- Templates for Listing Case Participants for a given evidence type -->
    <xsl:import href="ListEvidenceTypeForRelatedEntity.xslt"/>
    
    <!-- 
        This template handles the Related pattern for the entity. It is the generic entry point and
        makes all decisions about which specific templates to call based on the patterns
        
        @param path Path where the file is to be written
        @param entityName The name of the Entity being generated
    -->
    <xsl:template name="ProcessRelatedRelationships">
        
        <xsl:param name="path"/>
        <xsl:param name="entityName"/>
        
        <!-- Main page for showing list of related Entity and CPR entries -->
        <xsl:variable name="mainRelatedPageName"><xsl:value-of select="$prefix"/>_listRelatedEntriesFor<xsl:value-of select="$entityName"/><xsl:value-of select="$caseType"/></xsl:variable>
        
        <!-- The base element for the entity being generated -->
        <xsl:variable name="currentEntityElem" select="//EvidenceEntities/EvidenceEntity[@name=$entityName]"/>
        
        <!-- Only generate if required -->
        <xsl:if test="count($currentEntityElem/Relationships/Related) &gt; 0">
            
            <xsl:choose>
                <xsl:when test="$currentEntityElem/Relationships/Related/@utilizedBy!=''">
                    <xsl:call-template name="RelatedViaUtilizedBy">
                        <xsl:with-param name="path" select="$path"/>
                        <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    
                    <xsl:call-template name="RelatedViaEntityAndCPRList">
                        <xsl:with-param name="path" select="$path"/>
                        <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
                        <xsl:with-param name="mainRelatedPageName" select="$mainRelatedPageName"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
            
        </xsl:if>
        
    </xsl:template>
    
    <!-- 
        This template handles the Related pattern for the entity where an Evidence Entity 
        and/or list of Case Participant Role types has been specified for linking via 
        
        @param path Path where the file is to be written
        @param currentEntityElem The base Element for the current Entity
        @param mainRelatedPageName The name of the main page to create
    -->
    <xsl:template name="RelatedViaEntityAndCPRList">
        
        <xsl:param name="path"/>
        <xsl:param name="currentEntityElem"/>
        <xsl:param name="mainRelatedPageName"/>
        
        <!-- Standard starting page -->
        <xsl:call-template name="ListEvidenceAndCPRForRelatedEntity">
            <xsl:with-param name="path" select="$path"/>
            <xsl:with-param name="pageName" select="$mainRelatedPageName"/>
            <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
        </xsl:call-template>
        
        <!-- From Active Workpace -->
        <xsl:call-template name="ListEvidenceAndCPRForRelatedEntity">
            <xsl:with-param name="path" select="$path"/>
            <xsl:with-param name="pageName" select="$mainRelatedPageName"/>
            <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
            <xsl:with-param name="workspaceType" select="$fromActiveWorkspacePostfix"/>
        </xsl:call-template>
        
        <!-- From InEdit Workspace -->
        <xsl:call-template name="ListEvidenceAndCPRForRelatedEntity">
            <xsl:with-param name="path" select="$path"/>
            <xsl:with-param name="pageName" select="$mainRelatedPageName"/>
            <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
            <xsl:with-param name="workspaceType" select="$fromInEditWorkspacePostfix"/>
        </xsl:call-template>
        
        <xsl:if test="$currentEntityElem/Relationships/Related/@relatedRelationshipType!=''">
            
            <xsl:variable name="relatedEvidenceListVimPageName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$currentEntityElem/Relationships/Related/@relatedRelationshipType"/>EvidenceFor<xsl:value-of select="$currentEntityElem/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
            
            <xsl:call-template name="ListEvidenceTypeForRelatedEntity">
                <xsl:with-param name="path" select="$path"/>
                <xsl:with-param name="vimPageName" select="$relatedEvidenceListVimPageName"/>
                <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
            </xsl:call-template>
            
            <xsl:call-template name="ListEvidenceTypeForRelatedEntity">
                <xsl:with-param name="path" select="$path"/>
                <xsl:with-param name="vimPageName" select="$relatedEvidenceListVimPageName"/>
                <xsl:with-param name="workspaceType" select="$fromActiveWorkspacePostfix"/>
                <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
            </xsl:call-template>
            
            <xsl:call-template name="ListEvidenceTypeForRelatedEntity">
                <xsl:with-param name="path" select="$path"/>
                <xsl:with-param name="vimPageName" select="$relatedEvidenceListVimPageName"/>
                <xsl:with-param name="workspaceType" select="$fromInEditWorkspacePostfix"/>
                <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
            </xsl:call-template>
        </xsl:if>
        
        <xsl:if test="count($currentEntityElem/Relationships/Related/ParticipantType) &gt; 0">
                
                <xsl:variable name="relatedCPRVimPageName"><xsl:value-of select="$prefix"/>_listCPRFor<xsl:value-of select="$currentEntityElem/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
                
                <!-- Standard VIM Page -->
                <xsl:call-template name="ListCPRForRelatedEntity">
                    <xsl:with-param name="path" select="$path"/>
                    <xsl:with-param name="vimPageName" select="$relatedCPRVimPageName"/>
                    <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
                </xsl:call-template>
                
                <!-- From Active Workspace -->
                <xsl:call-template name="ListCPRForRelatedEntity">
                    <xsl:with-param name="path" select="$path"/>
                    <xsl:with-param name="vimPageName" select="$relatedCPRVimPageName"/>
                    <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
                    <xsl:with-param name="workspaceType" select="$fromActiveWorkspacePostfix"/>
                </xsl:call-template>
                
                <!-- From InEdit Workspace -->
                <xsl:call-template name="ListCPRForRelatedEntity">
                    <xsl:with-param name="path" select="$path"/>
                    <xsl:with-param name="vimPageName" select="$relatedCPRVimPageName"/>
                    <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
                    <xsl:with-param name="workspaceType" select="$fromInEditWorkspacePostfix"/>
                </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    <!-- 
        This template handles the Related pattern for the entity where the utilizedBy
        option has been specified
        
        @param path Path where the file is to be written
        @param pageName The name of the page to write
        @param currentEntityElem The base Element for the current Entity
    -->
    <xsl:template name="RelatedViaUtilizedBy">
        
        <xsl:param name="path"/>
        <xsl:param name="pageName"/>
        <xsl:param name="currentEntityElem"/>
        
        <!-- Do nothing for the moment. Possibly supersede this meta-data?? -->
    </xsl:template>
    
</xsl:stylesheet>
