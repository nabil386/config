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
    
    <!-- Templates for entities with multiple possible PreAssociation types -->
    <xsl:import href="MultipleEvidenceTypeForPreAssociation.xslt"/>
    
    <!-- Templates for entities with only one possible PreAssociation type -->
    <xsl:import href="SingleEvidenceTypeForPreAssociation.xslt"/>
    
    <!-- Templates for entities with only one possible PreAssociation type -->
    <xsl:import href="EvidenceListViewForPreAssociation.xslt"/>
    
    <!-- 
        This template handles PreAssociations for the entity. It is the generic entry point and
        makes all decisions about which specific templates to call based on the patterns
        
        @param path Path where the file is to be written
        @param entityName The name of the Entity being generated
    -->
    <xsl:template name="PreAssociations">
        
        <xsl:param name="path"/>
        <xsl:param name="entityName"/>
        
        <!-- The base element for the entity being generated -->
        <xsl:variable name="currentEntityElem" select="//EvidenceEntities/EvidenceEntity[@name=$entityName]"/>
        
        <!-- Only generate if specified -->
        <xsl:if test="$currentEntityElem/UserInterfaceLayer/@generatePreAssociationPages='Yes'">
            
        <!-- The name for the PreAssociation page -->
        <xsl:variable name="pageName"><xsl:value-of select="$prefix"/>_select<xsl:value-of select="$entityName"/><xsl:value-of select="$caseType"/>PreAssociation</xsl:variable>
        
        <xsl:choose>
            <!-- Call the correct template if its just a single pre association -->
            <xsl:when test="$currentEntityElem/Relationships/@multiplePreAssociationID='No' or not($currentEntityElem/Relationships/@multiplePreAssociationID)">
            <xsl:call-template name="SinglePreAssociation">
                <xsl:with-param name="path" select="$path"/>
                <xsl:with-param name="entityName" select="$entityName"/>
                <xsl:with-param name="pageName" select="$pageName"/>
                <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
            </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="MultiplePreAssociation">
                    <xsl:with-param name="path" select="$path"/>
                    <xsl:with-param name="entityName" select="$entityName"/>
                    <xsl:with-param name="pageName" select="$pageName"/>
                    <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
            
        </xsl:if>
        
    </xsl:template>
    
    
    <!--
        Template for handling Multiple PreAssociations. Currently this generates nothing
        
        @param path Path where the file is to be written
        @param entityName The name of the Entity being generated
        @param pageName The name of the page to generate
        @param currentEntityElem Base Element ref for the current entity. All xpath should run off this
    -->
    <xsl:template name="MultiplePreAssociation">
        
        <xsl:param name="path"/>
        <xsl:param name="entityName"/>
        <xsl:param name="pageName"/>
        <xsl:param name="currentEntityElem"/>
        
    </xsl:template>
    
    <!--
        Template for handling Single PreAssociations. This includes patterns for more than one
        possible type and a pattern for there being one single type
        
        @param path Path where the file is to be written
        @param entityName The name of the Entity being generated
        @param pageName The name of the page to generate
        @param currentEntityElem Base Element ref for the current entity. All xpath should run off this
    -->
    <xsl:template name="SinglePreAssociation">
        
        <xsl:param name="path"/>
        <xsl:param name="entityName"/>
        <xsl:param name="pageName"/>
        <xsl:param name="currentEntityElem"/>
        
        <!-- The name for the PreAssociation page -->
        <xsl:variable name="vimPageName"><xsl:value-of select="$prefix"/>_select<xsl:value-of select="$entityName"/><xsl:value-of select="$caseType"/>PreAssociation_content</xsl:variable>
        
        
        
        <xsl:if test="count($currentEntityElem/Relationships/PreAssociation) &gt; 0">
        <xsl:call-template name="EvidenceListViewForPreAssociation">
            <xsl:with-param name="path" select="$path"/>
            <xsl:with-param name="entityName" select="$entityName"/>
            <xsl:with-param name="pageName" select="$vimPageName"/>
        </xsl:call-template>
        </xsl:if>
        
        <xsl:choose>
            <xsl:when test="count($currentEntityElem/Relationships/PreAssociation) &gt; 1">
                <xsl:call-template name="MultipleEvidenceTypeForPreAssociation">
                    <xsl:with-param name="path" select="$path"/>
                    <xsl:with-param name="entityName" select="$entityName"/>
                    <xsl:with-param name="pageName" select="$pageName"/>
                    <xsl:with-param name="vimPageName" select="$vimPageName"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="count(Relationships/PreAssociation)=1">
                <xsl:call-template name="SingleEvidenceTypeForPreAssociation">
                    <xsl:with-param name="path" select="$path"/>
                    <xsl:with-param name="entityName" select="$entityName"/>
                    <xsl:with-param name="pageName" select="$pageName"/>
                    <xsl:with-param name="vimPageName" select="$vimPageName"/>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
        
    </xsl:template>
</xsl:stylesheet>
