<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

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
<xsl:import href="UICommon.xslt"/>

<xsl:output method="xml" indent="yes"/>

<xsl:template name="HierarchyUI">

  <xsl:param name="path" />
  <xsl:param name="UIName" />

  <xsl:param name="capName" />

  <xsl:param name="Relationships" />


  <xsl:variable name="filepath" select="concat($path, '/', $UIName)"/>
  <xsl:variable name="childLevelNo">
    <xsl:call-template name="GetChildLevel">
      <xsl:with-param name="capName" select="$capName"/>
    </xsl:call-template>
  </xsl:variable>    
  <redirect:write select="concat($filepath, '.xml')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>
<xsl:comment> Description                                                            </xsl:comment>
<xsl:comment> ===========                                                            </xsl:comment>
<xsl:comment> The page hierarchy for the <xsl:value-of select="$capName"/> Evidence group.         </xsl:comment>

<PAGE_GROUP
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  TITLE="Group.Title.{$UIName}"
  GROUP_ID="{$UIName}"
  APPLICATION="Curam"
>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="evidenceID"/>
  <PAGE_PARAMETER NAME="evidenceType"/>
  <xsl:if test="$childLevelNo=1">
   <PAGE_PARAMETER NAME="parEvID"/>
   <PAGE_PARAMETER NAME="parEvType"/>
  </xsl:if>  
  <xsl:if test="$childLevelNo=2">
    <PAGE_PARAMETER NAME="parEvID"/>
    <PAGE_PARAMETER NAME="parEvType"/>
    <PAGE_PARAMETER NAME="grandParEvID"/>
    <PAGE_PARAMETER NAME="grandParEvType"/>
  </xsl:if> 

  <PAGE_REF
    DEFAULT="true"
    PAGE_ID="{$prefix}_view{$capName}{$caseType}"
    TITLE="Page.Title.{$capName}"
  />

  <xsl:for-each select="$Relationships/Child">  

    <xsl:variable name="childEvidenceName" select="@name"/>
    <xsl:variable name="ChildEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$childEvidenceName]"/>
  
    <xsl:choose>
        <xsl:when test="count($ChildEvidenceEntity/Relationships/Parent)>1">          

  <PAGE_REF
    PAGE_ID="{$prefix}_list{@name}{$caseType}For{$capName}"
    TITLE="Page.Title.{@name}"
  />
      
        </xsl:when>        
        <xsl:otherwise>

  <PAGE_REF
    PAGE_ID="{$prefix}_list{@name}{$caseType}"
    TITLE="Page.Title.{@name}"
  />

        </xsl:otherwise>
      </xsl:choose>            
  </xsl:for-each>
  
  <xsl:for-each select="$Relationships/Association[@to!='' and @displayInHierarchy='Yes']">  

    <xsl:variable name="childEvidenceName" select="@to"/>
    
    <xsl:variable name="ChildEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$childEvidenceName]"/>
    
    <xsl:choose>
      <xsl:when test="count($ChildEvidenceEntity/Relationships/Association[@displayInHierarchy='Yes'])>0">          

  <PAGE_REF
    PAGE_ID="{$prefix}_list{@to}{$caseType}For{$capName}"
    TITLE="Page.Title.{@to}"
  />
      
        </xsl:when>        
        <xsl:otherwise>

  <PAGE_REF
    PAGE_ID="{$prefix}_list{@to}{$caseType}"
    TITLE="Page.Title.{@to}"
  />

        </xsl:otherwise>
      </xsl:choose>          
    
</xsl:for-each>

</PAGE_GROUP>

  </redirect:write>

  <!-- View Content Vim Properties -->
  <xsl:call-template name="write-all-locales-HierarchyUI-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="fullFileName" select="$filepath"/>
    <xsl:with-param name="capName" select="$capName"/>
    <xsl:with-param name="UIName" select="$UIName"/>
    <xsl:with-param name="Relationships" select="$Relationships"/>
  </xsl:call-template>

</xsl:template>


  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-HierarchyUI-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="capName"/>
    <xsl:param name="UIName"/>
    <xsl:param name="Relationships" />

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-HierarchyUI-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="capName" select="$capName"/>
            <xsl:with-param name="UIName" select="$UIName"/>
            <xsl:with-param name="Relationships" select="$Relationships"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-HierarchyUI-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="capName" select="$capName"/>
            <xsl:with-param name="UIName" select="$UIName"/>
            <xsl:with-param name="Relationships" select="$Relationships"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-HierarchyUI-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="capName" select="$capName"/>
            <xsl:with-param name="UIName" select="$UIName"/>
            <xsl:with-param name="Relationships" select="$Relationships"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-HierarchyUI-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="capName" select="$capName"/>
            <xsl:with-param name="UIName" select="$UIName"/>
            <xsl:with-param name="Relationships" select="$Relationships"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-HierarchyUI-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="capName"/>
    <xsl:param name="UIName"/>
    <xsl:param name="Relationships"/>
    
    <xsl:variable name="entityElement" select="."/>
    <xsl:variable name="entityLabel"><xsl:call-template name="getEntityLabelForPropertiesFile"> 
		<xsl:with-param name="locale" select="$locale"/>        
		<xsl:with-param name="evidenceNode" select="$entityElement"/>          
	  </xsl:call-template></xsl:variable>
    
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
    
    <xsl:if test="count($entityElement//*[@locale=$locale])&gt;0">
      <redirect:write select="concat($fullFileName, $locale, '.properties')">
Group.Title.<xsl:value-of select="$UIName"/>=<xsl:value-of select="$capName"/>

Page.Title.<xsl:value-of select="$capName"/>=<xsl:value-of select="$entityLabel"/>

  <xsl:for-each select="$Relationships/Child">  

    <xsl:variable name="childLogicalName">
      <xsl:variable name="childEvidenceName" select="@name"/>
      <xsl:variable name="childEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$childEvidenceName]"/>
      <xsl:choose>
        <xsl:when test="$childEvidenceEntity/@shortName!=''"><xsl:value-of select="$childEvidenceEntity/@shortName"/></xsl:when>  
        <xsl:otherwise><xsl:call-template name="getEntityLabelForPropertiesFile"> 
		  <xsl:with-param name="locale" select="$locale"/>        
		  <xsl:with-param name="evidenceNode" select="$childEvidenceEntity"/>          
	      </xsl:call-template></xsl:otherwise>
      </xsl:choose>    
    </xsl:variable>
    
Page.Title.<xsl:value-of select="@name"/>=<xsl:value-of select="$childLogicalName"/>
  
  </xsl:for-each>

  <xsl:for-each select="$Relationships/Association[@to!='' and @displayInHierarchy='Yes']">  

    <xsl:variable name="childLogicalName">
      <xsl:variable name="childEvidenceName" select="@to"/>
      <xsl:variable name="childEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$childEvidenceName]"/>
      <xsl:choose>
        <xsl:when test="$childEvidenceEntity/@shortName!=''"><xsl:value-of select="$childEvidenceEntity/@shortName"/></xsl:when>  
        <xsl:otherwise><xsl:call-template name="getEntityLabelForPropertiesFile"> 
		  <xsl:with-param name="locale" select="$locale"/>        
		  <xsl:with-param name="evidenceNode" select="$childEvidenceEntity"/>          
	      </xsl:call-template></xsl:otherwise>
      </xsl:choose>    
    </xsl:variable>
    
Page.Title.<xsl:value-of select="@to"/>=<xsl:value-of select="$childLogicalName"/>
  
  </xsl:for-each>

  </redirect:write>
    </xsl:if>
</xsl:template>

</xsl:stylesheet>