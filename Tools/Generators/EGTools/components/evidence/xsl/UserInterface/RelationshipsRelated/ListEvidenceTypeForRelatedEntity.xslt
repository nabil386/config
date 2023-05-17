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

<xsl:output method="xml" indent="yes"/>

<!--
  Template to write a VIM file and its associated properties for listing
  Evidence records of a given type for relating a evidence type to a core entity
  
  @param path@param path The path to store the files in
  @param pageName The main part of the page name
  @param workspaceType The file name postfix showing the workspace type (i.e. Active, InEdit or blank)
  @param currentEntityElem The base element of the current entity
-->
<xsl:template name="ListEvidenceTypeForRelatedEntity">

  <xsl:param name="path"/>
  <xsl:param name="vimPageName"/>
  <xsl:param name="workspaceType"/>
  <xsl:param name="currentEntityElem"/>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$vimPageName"/><xsl:if test="$workspaceType!=''">_from<xsl:value-of select="$workspaceType"/></xsl:if></xsl:variable>
  
  <xsl:variable name="listEmploymentsForEvidencePageName"><xsl:value-of select="$prefix"/>_listCoreEmploymentEvidenceDetails<xsl:if test="$workspaceType!=''">_from<xsl:value-of select="$workspaceType"/></xsl:if></xsl:variable>

  <redirect:write select="concat($filepath, '.vim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

  <LIST TITLE="List.Title.RelatedEvidenceList">

      <CONTAINER
        LABEL="List.Title.Action"
        WIDTH="10"
      >

        <ACTION_CONTROL LABEL="ActionControl.Label.Select.For.RelatedEntity.Association">
          <LINK PAGE_ID="{$listEmploymentsForEvidencePageName}" DISMISS_MODAL="FALSE">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYEVIDENCETYPE"
                PROPERTY="result$parentList$dtls$participantID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseParticipantRoleID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="contextDescription"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="contextDescription"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="CONSTANT"
                PROPERTY="{$currentEntityElem/@name}.EvidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>

      </CONTAINER>
      
      <FIELD
        LABEL="List.Title.Name"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYEVIDENCETYPE"
            PROPERTY="result$parentList$dtls$concernRoleName"
          />
        </CONNECT>
        <LINK OPEN_NEW="true" PAGE_ID="{$resolveParticipantHome}">
          <CONNECT>
            <SOURCE NAME="DISPLAYEVIDENCETYPE" PROPERTY="result$parentList$dtls$participantID"/>
            <TARGET NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
          </CONNECT>
        </LINK>
      </FIELD>
      
      <FIELD
        LABEL="List.Title.Details"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYEVIDENCETYPE"
            PROPERTY="result$parentList$dtls$summary"
          />
        </CONNECT>
      </FIELD>
      
      <FIELD
        LABEL="Field.Label.StartDate"
        WIDTH="10"
        >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYEVIDENCETYPE"
            PROPERTY="result$parentList$dtls$startDate"
          />
        </CONNECT>
      </FIELD>
      
      <FIELD
        LABEL="Field.Label.EndDate"
        WIDTH="10"
        >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYEVIDENCETYPE"
            PROPERTY="result$parentList$dtls$endDate"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  
</VIEW>
  
  </redirect:write>
  
  <xsl:call-template name="write-all-locales-ListEvidenceTypeForRelatedEntity-properties">   
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
    <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
  </xsl:call-template>

</xsl:template>

<!--
  Recursive template for writing the properties file in all locales
  
  @param locales List of locales left to process
  @param filepath The file to write (minus locale info)
  @param currentEntityElem The base XML Element for the current Entity
-->
  <xsl:template name="write-all-locales-ListEvidenceTypeForRelatedEntity-properties">
    
    <xsl:param name="locales"/>
    <xsl:param name="filepath"/>
    <xsl:param name="currentEntityElem"/>
    
    <xsl:if test="$locales">
      
      <xsl:choose>
        
        <xsl:when test="contains($locales,',')">
          
          <xsl:call-template name="write-ListEvidenceTypeForRelatedEntity-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
          </xsl:call-template>
          
          <xsl:call-template name="write-all-locales-ListEvidenceTypeForRelatedEntity-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
          </xsl:call-template>
        
        </xsl:when>
        <xsl:otherwise>
          
          <xsl:call-template name="write-ListEvidenceTypeForRelatedEntity-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
          </xsl:call-template>
          
          <xsl:call-template name="write-ListEvidenceTypeForRelatedEntity-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
          </xsl:call-template>
        
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  
  </xsl:template>
  
  <!--
    Template for writing the properties file in a specific locale
    
    @param locales List of locales left to process
    @param filepath The file to write (minus locale info)
    @param currentEntityElem The base XML Element for the current Entity
  -->
  <xsl:template name="write-ListEvidenceTypeForRelatedEntity-properties">
    
    <xsl:param name="locale"/>
    <xsl:param name="filepath"/>
    <xsl:param name="currentEntityElem"/>
        
    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
<redirect:write select="concat($filepath, $locale, '.properties')">
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.Action"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>   

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Select.For.RelatedEntity.Association"/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.Name"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ Field.Label.StartDate"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ Field.Label.EndDate"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>
  
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.Details"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>
  
  <xsl:choose>
    <xsl:when test="count($generalProperties/List.Title.RelatedEvidenceList)&gt;0">
  <xsl:call-template name="callGenerateProperties">
    <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.RelatedEvidenceList"/>
    <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$currentEntityElem/Relationships/Related/@relatedRelationshipType]"/>
  </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>List.Title.RelatedEvidenceList=List.Title.RelatedEvidenceList must be specified in the general.properties file</xsl:otherwise>
  </xsl:choose>
  <xsl:text>&#xa;</xsl:text>
    
  </redirect:write>
    </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>