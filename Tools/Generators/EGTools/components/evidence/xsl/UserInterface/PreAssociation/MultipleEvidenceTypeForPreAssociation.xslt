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

  <!-- Global Variables -->
<xsl:import href="PreAssociationsCommons.xslt"/>
  
<xsl:output method="xml" indent="yes"/>

<!-- 
  This template writes the UIM File for choosing the evidence record 
  for pre association, where one of multiple types needs to be selected
  
  @param path Path where the file is to be written
  @param entityName The name of the Entity being generated
  @param pageName The name of the page to generate
  @param vimPageName The name of the content VIM to include
-->
<xsl:template name="MultipleEvidenceTypeForPreAssociation">

  <xsl:param name="path"/>
  <xsl:param name="entityName"/>
  <xsl:param name="pageName"/>
  <xsl:param name="vimPageName"/>
  
  <!-- The base element for the entity being generated -->
  <xsl:variable name="currentEntityElem" select="//EvidenceEntities/EvidenceEntity[@name=$entityName]"/>
  
  <redirect:write select="concat($path, $pageName, '.uim')">
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE
  PAGE_ID="{$pageName}"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="contextDescription"
      />
    </CONNECT>
  </PAGE_TITLE>
  
  <SERVER_INTERFACE
    CLASS="{$prefix}EvidenceMaintenance"
    NAME="DISPLAY"
    OPERATION="getEvidenceTypeListForPreAssociation"
    PHASE="DISPLAY"
  />
  
  <SERVER_INTERFACE
    CLASS="{$prefix}EvidenceMaintenance"
    NAME="PREASSOCLIST"
    OPERATION="getEvidenceListByType"
    PHASE="ACTION"
  />
  
  <CONNECT>
    <SOURCE NAME="CONSTANT" PROPERTY="{$entityName}.EvidenceType"/>
    <TARGET NAME="DISPLAY" PROPERTY="evidenceTypeKey$evidenceType"/>
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="PREASSOCLIST" PROPERTY="key$caseID"/>
  </CONNECT>
  
  <CLUSTER NUM_COLS="2" TITLE="Cluster.PreAssocEvidenceType.Title" DESCRIPTION="Cluster.PreAssocEvidenceType.Description">
    
    
    <FIELD LABEL="Field.Label.EvidenceType">
      <CONNECT>
        <INITIAL
          PROPERTY="result$dtls$description"
          NAME="DISPLAY"
          HIDDEN_PROPERTY="result$dtls$evidenceType"/>
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="PREASSOCLIST"
          PROPERTY="key$evidenceType"/>
      </CONNECT>
    </FIELD>
    <ACTION_SET
      ALIGNMENT="CENTER"
      TOP="false"
      >
      <ACTION_CONTROL
        DEFAULT="true"
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.Search"
        TYPE="SUBMIT"
        >
        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00128533, POB -->
      <xsl:call-template name="PreAssociations_selectPreAssociationsPage_cancelButton">
        <xsl:with-param name="entityName" select="$entityName"/>
      </xsl:call-template>
      <!-- END, CR00128533 -->
    </ACTION_SET>
    
  </CLUSTER>
  
  <INCLUDE FILE_NAME="{$vimPageName}.vim"/>
  
</PAGE>

  </redirect:write>

  <!-- BEGIN, PADDY -->
  <xsl:call-template name="write-all-locales-multipleevtype-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="concat($path, $pageName)"/>
    <xsl:with-param name="currentEntityElem" select="$currentEntityElem"/>
  </xsl:call-template>
  <!-- END, PADDY -->

</xsl:template>


<!-- BEGIN, PADDY -->

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-multipleevtype-properties">

       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
      <xsl:param name="currentEntityElem"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-multipleevtype-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-multipleevtype-properties">
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

           </xsl:when>

           <!--only one token left-->
           <xsl:otherwise>

             <!-- Call for the final locale -->
             <xsl:call-template name="write-multipleevtype-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-multipleevtype-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
               <xsl:with-param name="currentEntityElem"
                 select="$currentEntityElem"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>


  <xsl:template name="write-multipleevtype-properties">

      <xsl:param name="locale"/>
      <xsl:param name="filepath"/>
      <xsl:param name="currentEntityElem"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

      <redirect:write select="concat($filepath, $locale, '.properties')">
        <xsl:if test="count($generalProperties/Help.PageDescription.SelectPreAssociation)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.SelectPreAssociation"/>
  <xsl:with-param name="evidenceNode" select="$currentEntityElem"/>
  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
    </xsl:call-template>
      </xsl:if>
      <xsl:if test="count($generalProperties/Page.Title.SelectPreAssociation)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.SelectPreAssociation"/>
  <xsl:with-param name="evidenceNode" select="$currentEntityElem"/>
  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
    </xsl:call-template>
    <xsl:text>&#xa;</xsl:text> 
      </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/ActionControl.Label.Cancel)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Cancel"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/ActionControl.Label.Search)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Search"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/Field.Label.EvidenceType)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.EvidenceType"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/Cluster.PreAssocEvidenceType.Title)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.PreAssocEvidenceType.Title"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template> 
        </xsl:if>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($generalProperties/Cluster.PreAssocEvidenceType.Description)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.PreAssocEvidenceType.Description"/>
            <xsl:with-param name="evidenceNode" select="$currentEntityElem"/>
          </xsl:call-template> 
        </xsl:if>

  </redirect:write>
  </xsl:if>
    </xsl:template>

</xsl:stylesheet>