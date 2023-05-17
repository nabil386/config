<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2008,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2008 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="XML" indent="yes"/>

  <xsl:param name="clientDir"/>
  <xsl:param name="localeList"/>
  <xsl:include href="UICommon.xslt"/>
  <xsl:template match="EvidenceEntities">

    <!-- Create a generatedImages.imm file -->
    <xsl:variable name="filename">
      <xsl:value-of select="$clientDir"/>/Evidence/GeneratedImages.iim</xsl:variable>

    <xsl:if
      test="count(EvidenceEntity/Relationships/Child[@createButton='Yes'])>0 or 
    count(EvidenceEntity/Relationships/Association[@createButton='Yes'])>0 or
    count(EvidenceEntity/UserInterfaceLayer/ViewAssociated)>0 or
    count(EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink)>0">
      <redirect:write select="$filename">

        <IMAGES>
          <!--- Create entry for Child buttons -->
          <xsl:for-each select="EvidenceEntity/Relationships/Child[@createButton='Yes']">
            <xsl:variable name="childLogicalName" select="@name"/>
            <xsl:variable name="propertyAssignmentString">SaveAndAdd<xsl:value-of select="$childLogicalName"/>Button</xsl:variable>
            <IMAGE TEXT="{$propertyAssignmentString}.text" FILE_NAME="{$propertyAssignmentString}"/>
          </xsl:for-each>

          <!--- Create entry for Associate buttons -->
          <xsl:for-each select="EvidenceEntity/Relationships/Association[@createButton='Yes']">
            <xsl:variable name="associateLogicalName">
              <xsl:if test="@to!=''">
                <xsl:value-of select="@to"/>
              </xsl:if>
              <xsl:if test="@from!=''">
                <xsl:value-of select="@from"/>
              </xsl:if>
            </xsl:variable>
            <xsl:variable name="propertyAssignmentString">SaveAndAssociate<xsl:value-of select="$associateLogicalName"/>Button</xsl:variable>
            <IMAGE TEXT="{$propertyAssignmentString}.text" FILE_NAME="{$propertyAssignmentString}"/>
          </xsl:for-each>
          <!-- BEGIN, CR00100657, POB -->
          <xsl:for-each select="EvidenceEntity/UserInterfaceLayer/ViewAssociated">
            <xsl:variable name="propertyAssignmentString">ViewAssociated<xsl:value-of select="@name"/>Button</xsl:variable>
            <IMAGE TEXT="{$propertyAssignmentString}.text" FILE_NAME="{$propertyAssignmentString}"/>
          </xsl:for-each>
          <!-- END, CR00100657, POB -->

          <!-- BEGIN, CR00113102, DG -->
          <xsl:for-each select="EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink">
            <xsl:variable name="linkName">
              <xsl:call-template name="replaceAll">
                  <xsl:with-param name="stringToTransform" select="@displayName"/>
                  <xsl:with-param name="target" select="' '"/>
                  <xsl:with-param name="replacement" select="''"/>
              </xsl:call-template>
            </xsl:variable>

            <xsl:variable name="propertyAssignmentString">SaveAndAdd<xsl:value-of select="$linkName"/>Button</xsl:variable>
            <IMAGE TEXT="{$propertyAssignmentString}.text" FILE_NAME="{$propertyAssignmentString}"/>
          </xsl:for-each>
          <!-- END, CR00113102 -->


          </IMAGES>
      </redirect:write>
      <redirect:close select="$filename"/>
      
    </xsl:if>

    <xsl:variable name="fullFileName"><xsl:value-of select="$clientDir"/>/Evidence/GeneratedImagesText</xsl:variable>
      
    <!-- Create Content Vim Properties -->
    <xsl:call-template name="write-all-locales-generateimages-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
    </xsl:call-template>
    
  </xsl:template>
    
  <xsl:template name="write-all-locales-generateimages-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-generateimages-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-generateimages-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-generateimages-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-generateimages-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>
    
  <xsl:template name="write-generateimages-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    
	<xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/*)&gt;0">
    
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
  
    <!-- Create a generatedImagesText.properties file -->
    <xsl:variable name="propertiesFilename"><xsl:value-of select="$fullFileName"/><xsl:value-of select="$locale"/>.properties</xsl:variable>
    <xsl:variable name="carriageReturn" select="'&#10;'"/>

    <xsl:if
      test="count(EvidenceEntity/Relationships/Child[@createButton='Yes'])>0 or 
      count(EvidenceEntity/Relationships/Association[@createButton='Yes'])>0 or
      count(EvidenceEntity/UserInterfaceLayer/ViewAssociated)>0 or
    count(EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink)>0">
      <redirect:write select="$propertiesFilename">

          <!--- Create entry for Child buttons -->
        <xsl:for-each select="EvidenceEntity/Relationships/Child[@createButton='Yes']">
          <xsl:variable name="childLogicalName" select="@name"/>
          <xsl:variable name="propertyAssignmentString">SaveAndAdd<xsl:value-of select="$childLogicalName"/>Button</xsl:variable>
          <xsl:variable name="propertyAndHelp"><xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAddButton"/>
            <xsl:with-param name="altPropertyName"><xsl:value-of select="$propertyAssignmentString"/>.text</xsl:with-param>
            <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@name=$childLogicalName]"/>
          </xsl:call-template></xsl:variable>
          <xsl:value-of select="substring-before($propertyAndHelp, $carriageReturn)" disable-output-escaping="yes"/>
          <xsl:text>&#10;</xsl:text>
        </xsl:for-each>

          <!--- Create entry for Associate buttons -->
        <xsl:for-each select="EvidenceEntity/Relationships/Association[@createButton='Yes']">
          <xsl:variable name="associateLogicalName">
            <xsl:if test="@to!=''">
              <xsl:value-of select="@to"/>
            </xsl:if>
            <xsl:if test="@from!=''">
              <xsl:value-of select="@from"/>
            </xsl:if>
          </xsl:variable>
          <xsl:variable name="propertyAssignmentString">SaveAndAssociate<xsl:value-of select="$associateLogicalName"/>Button</xsl:variable>
          <xsl:variable name="propertyAndHelp"><xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndAssociateButton"/>
            <xsl:with-param name="altPropertyName"><xsl:value-of select="$propertyAssignmentString"/>.text</xsl:with-param>
            <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@name=$associateLogicalName]"/>
          </xsl:call-template></xsl:variable>
          <xsl:value-of select="substring-before($propertyAndHelp, $carriageReturn)" disable-output-escaping="yes"/>
          <xsl:text>&#10;</xsl:text>
        </xsl:for-each>
        
        <!-- BEGIN, CR00100657, POB -->
        <xsl:for-each select="EvidenceEntity/UserInterfaceLayer/ViewAssociated">
          <xsl:variable name="assocName" select="@name"/>
          <xsl:variable name="currentAssocEl" select="/EvidenceEntities/EvidenceEntity[@name=$assocName][1]"/>
          <xsl:variable name="propertyAssignmentString">ViewAssociated<xsl:value-of select="$assocName"/>Button</xsl:variable>
          <xsl:value-of select="$propertyAssignmentString"/>.text=View Associated <xsl:call-template name="getEntityLabelForPropertiesFile"> 
		    <xsl:with-param name="locale" select="$locale"/>        
		    <xsl:with-param name="evidenceNode" select="$currentAssocEl"/>          
		  </xsl:call-template>
        </xsl:for-each>
        <!-- END, CR00100657, POB -->
        
        <!-- BEGIN, CR00113102, DG -->
        <xsl:for-each select="EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink">
		  <xsl:variable name="linkDisplayName" select="@displayName"/>
          <xsl:variable name="linkName">
              <xsl:call-template name="replaceAll">
                  <xsl:with-param name="stringToTransform" select="@displayName"/>
                  <xsl:with-param name="target" select="' '"/>
                  <xsl:with-param name="replacement" select="''"/>
              </xsl:call-template>
          </xsl:variable>
          <xsl:variable name="propertyAssignmentString">SaveAndAdd<xsl:value-of select="$linkName"/>Button</xsl:variable>
          <xsl:value-of select="$propertyAssignmentString"/>.text=Save <xsl:text disable-output-escaping="yes">&amp;</xsl:text> Add <xsl:call-template name="getEntityLabelForPropertiesFile"> 
		    <xsl:with-param name="locale" select="$locale"/>        
		    <xsl:with-param name="evidenceNode" select="//EvidenceEntities/EvidenceEntity[@displayName=$linkDisplayName]"/>          
		  </xsl:call-template>
        </xsl:for-each>
        <!-- END, CR00113102, POB -->
        
      </redirect:write>
      <redirect:close select="$propertiesFilename"/>
    </xsl:if>

    </xsl:if>

  </xsl:template>

</xsl:stylesheet>
