<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2010,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2010-2011 Curam Software Ltd.  All rights reserved.

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

  <!-- Include Section -->
  <xsl:include href="EvidenceCommon.xslt" />
  <xsl:include href="BusinessObjectTab/DefaultAppSection.xslt"/>
  <xsl:include href="BusinessObjectTab/EvidenceObjectTab.xslt"/>
  <xsl:include href="BusinessObjectTab/EvidenceObjectNav.xslt"/>
  <xsl:include href="BusinessObjectTab/EvidenceObjectMenu.xslt"/>
  <!-- Global Variables -->
  
  
  <xsl:param name="caseType"/>
  <xsl:param name="date"/>
  <xsl:param name="localeList"/>
  
  
  <!-- Main Evidence Business Object Tab Generation Template -->
  <xsl:template match="EvidenceEntity[UserInterfaceLayer]">
      
      
      
  <xsl:variable name="entityName" select="@name"/>
  <xsl:variable name="entityElement" select="."/>
  <xsl:variable name="rootPath"/>
  <xsl:variable name="path" select="concat($rootPath, $entityName , '')"/>
  
  
  <!-- Generate DefaultAddSection entries -->
  <xsl:call-template name="DefaultAppSection">
  
    <xsl:with-param name="path" select="$rootPath" />
    <xsl:with-param name="prefix" select="$prefix" />
    
  </xsl:call-template>
  
  <!-- Generate tab entries -->
  <xsl:call-template name="EvidenceObjectTab">
    
    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="concat($rootPath, $entityName , '')" />
    <xsl:with-param name="entityElement" select="$entityElement" />
    <xsl:with-param name="caseType" select="$caseType" />
    
  
  </xsl:call-template>
  
  <!-- Generate nav entries -->
  <xsl:call-template name="EvidenceObjectNav">
      
    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="concat($rootPath, $entityName , '')" />
    <xsl:with-param name="entityElement" select="$entityElement" />
    <xsl:with-param name="Relationships" select="Relationships"/>
    <xsl:with-param name="localeList" select="$localeList"/>
    <xsl:with-param name="caseType" select="$caseType" />
      
  </xsl:call-template>
  
  <!-- BEGIN, CR00266438, CD -->
  <xsl:if test="count($entityElement/Relationships/Child)&gt;0 or 
                count(//EvidenceEntities/EvidenceEntity/Relationships/PreAssociation[@to=$entityName])&gt;0">
  <!-- END, CR00266438, CD -->
  <!-- Generate menu entries -->
  <xsl:call-template name="EvidenceObjectMenu">
      
    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="concat($rootPath, $entityName , '')" />
    <xsl:with-param name="entityElement" select="$entityElement" />
    <xsl:with-param name="Relationships" select="Relationships"/>
    <xsl:with-param name="caseType" select="$caseType" />
  </xsl:call-template>
     
  </xsl:if>
</xsl:template>

</xsl:stylesheet>