<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2010, 2017. All Rights Reserved.

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

<!-- Global Variables -->
<xsl:import href="../UICommon.xslt"/>

<!-- Create specific templates -->
<xsl:import href="CreateUtilityTemplates.xslt"/>

<xsl:output method="xml" indent="yes"/>

<!--
  Template to write a standalone create page (i.e. outside the wizard) for an entity.

  @param entityElement XML Element containing the entity
  @param caseType The case type being generated
  @param baseDirectory Base Directory to generate to
  @param fileName The name of the file to generate (minus the file extension)
  @param includeVIMFileName The name of the VIM file to include
-->
<xsl:template name="StandaloneCreateUIM">

  <xsl:param name="entityElement"/>
  <xsl:param name="caseType"/>
  <xsl:param name="baseDirectory"/>
  <xsl:param name="fileName"/>
  <xsl:param name="includeVIMFileName"/>

  <!-- Name of the entity -->
  <xsl:variable name="entityName" select="$entityElement/@name"/>

  <!-- Full name of the file -->
  <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/><xsl:value-of select="$fileName"/></xsl:variable>

  <redirect:write select="concat($fullFileName, '.uim')">

    <!-- add copyright notice -->
    <xsl:call-template name="printXMLCopyright">
      <xsl:with-param name="date" select="$date"/>
    </xsl:call-template>

<PAGE
  PAGE_ID="{$fileName}"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
  WINDOW_OPTIONS="width=900"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>

  <INCLUDE FILE_NAME="{$includeVIMFileName}.vim"/>

    <ACTION_SET
      BOTTOM="false"
    >
      <ACTION_CONTROL
        IMAGE="SaveButton"
        LABEL="ActionControl.Label.Save"
        TYPE="SUBMIT"
      />

      <xsl:if test="$entityElement/UserInterfaceLayer/@saveAndNewButton='Yes'">
        <xsl:call-template name="Utilities-getCreatePageName-getSaveAndNewLink">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="pageID" select="$fileName"/>
        </xsl:call-template>
      </xsl:if>

      <ACTION_CONTROL
        IMAGE="CancelButton"
        LABEL="ActionControl.Label.Cancel"
      />
    </ACTION_SET>

</PAGE>

  </redirect:write>

  <xsl:call-template name="write-all-locales-standalonecreateuim-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="fullFileName" select="$fullFileName"/>
    <xsl:with-param name="entityElement" select="$entityElement"/>
  </xsl:call-template>

</xsl:template>

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-standalonecreateuim-properties">

       <xsl:param name="locales"/>
       <xsl:param name="fullFileName"/>
       <xsl:param name="entityElement"/>

       <!--tokens still exist-->
       <xsl:if test="$locales">

         <xsl:choose>

           <!--more than one-->
           <xsl:when test="contains($locales,',')">

             <xsl:call-template name="write-standalonecreateuim-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="fullFileName"
                              select="$fullFileName"/>
               <xsl:with-param name="entityElement"
                              select="$entityElement"/>
             </xsl:call-template>

             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-standalonecreateuim-properties">
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="fullFileName"
                              select="$fullFileName"/>
               <xsl:with-param name="entityElement"
                              select="$entityElement"/>
             </xsl:call-template>

           </xsl:when>

           <!--only one token left-->
           <xsl:otherwise>

             <!-- Call for the final locale -->
             <xsl:call-template name="write-standalonecreateuim-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="fullFileName"
                              select="$fullFileName"/>
               <xsl:with-param name="entityElement"
                              select="$entityElement"/>
             </xsl:call-template>

             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-standalonecreateuim-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="fullFileName" select="$fullFileName"/>
         <xsl:with-param name="entityElement"
                              select="$entityElement"/>
             </xsl:call-template>

           </xsl:otherwise>

         </xsl:choose>

       </xsl:if>

    </xsl:template>


  <xsl:template name="write-standalonecreateuim-properties">

      <xsl:param name="locale"/>
      <xsl:param name="fullFileName"/>
      <xsl:param name="entityElement"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

      <redirect:write select="concat($fullFileName, $locale, '.properties')">
      <xsl:if test="count($generalProperties/Help.PageDescription.CreateEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.CreateEntity"/>
      <xsl:with-param name="evidenceNode" select="$entityElement"/>
	  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
    </xsl:call-template>
      </xsl:if>
      <xsl:if test="count($generalProperties/Page.Title.NewEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.NewEntity"/>
          <xsl:with-param name="evidenceNode" select="$entityElement"/>
	  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
    </xsl:call-template>
    <xsl:text>&#xa;</xsl:text>
    </xsl:if>

    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Save"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>

    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Cancel"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>

<xsl:if test="$entityElement/UserInterfaceLayer/@saveAndNewButton='Yes'">
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.SaveAndNew"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
</xsl:if>
  </redirect:write>
  </xsl:if>
    </xsl:template>

</xsl:stylesheet>
