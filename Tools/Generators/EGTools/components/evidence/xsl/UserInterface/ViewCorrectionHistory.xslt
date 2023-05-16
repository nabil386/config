<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2010,2017. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
  Copyright (c) 2010 Curam Software Ltd.  All rights reserved.

  This software is the confidential and proprietary information of Curam
  Software, Ltd. (&quot;Confidential Information&quot;).  You shall not
  disclose such Confidential Information and shall use it only in accordance
  with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="UICommon.xslt"/>

  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="ViewCorrectionHistory">

    <!-- Path to write the file in -->
    <xsl:param name="path"/>
    <!-- Name of the UIM page to create -->
    <xsl:param name="uimPageName"/>
    <!-- The view page for the entity -->
    <xsl:param name="entityViewPageName"/>

    <!-- The current EvidenceEntity XML Element -->
    <xsl:variable name="CurrentEvidenceEntity" select="."/>

    <xsl:variable name="entityName"><xsl:value-of select="$CurrentEvidenceEntity/@name"/></xsl:variable>
    <xsl:variable name="capNameCaseType"><xsl:value-of select="$entityName"/><xsl:value-of select="$caseType"/></xsl:variable>

    <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$uimPageName"/></xsl:variable>

    <xsl:variable name="correctionHistoryListVIM">Evidence_listCorrectionHistory</xsl:variable>

    <redirect:write select="concat($filepath, '.uim')">

<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

      <PAGE PAGE_ID="{$uimPageName}" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd" WINDOW_OPTIONS="width=900">

        <MENU MODE="IN_PAGE_NAVIGATION">
          <ACTION_CONTROL STYLE="in-page-link" LABEL="ActionControl.Label.Details">
            <LINK PAGE_ID="{$entityViewPageName}" DISMISS_MODAL="FALSE" SAVE_LINK="FALSE">
              <CONNECT>
                <SOURCE PROPERTY="caseID" NAME="PAGE"/>
                <TARGET PROPERTY="caseID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceID" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL STYLE="in-page-current-link" LABEL="ActionControl.Label.History">
            <LINK PAGE_ID="{$uimPageName}" DISMISS_MODAL="FALSE" SAVE_LINK="FALSE">
              <CONNECT>
                <SOURCE PROPERTY="caseID" NAME="PAGE"/>
                <TARGET PROPERTY="caseID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceID" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceID" NAME="PAGE"/>
              </CONNECT>
              <CONNECT>
                <SOURCE PROPERTY="evidenceType" NAME="PAGE"/>
                <TARGET PROPERTY="evidenceType" NAME="PAGE"/>
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </MENU>

        <PAGE_TITLE>
          <CONNECT>
            <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
          </CONNECT>
        </PAGE_TITLE>

        <PAGE_PARAMETER NAME="caseID"/>

        <INCLUDE FILE_NAME="{$correctionHistoryListVIM}.vim"/>

      </PAGE>

    </redirect:write>

      <xsl:call-template name="write-all-locales-view-history-properties">
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filepath" select="$filepath"/>
        <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
      </xsl:call-template>

  </xsl:template>

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-view-history-properties">

    <xsl:param name="locales"/>
    <xsl:param name="filepath"/>
    <xsl:param name="CurrentEvidenceEntity"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">
      <xsl:choose>
        <!--more than one-->
         <xsl:when test="contains($locales,',')">

           <xsl:call-template name="write-view-history-properties">
             <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
             <xsl:with-param name="filepath" select="$filepath"/>
             <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
           </xsl:call-template>

           <!-- Recursively call self to process all locales -->
           <xsl:call-template name="write-all-locales-view-history-properties">
             <xsl:with-param name="locales" select="substring-after($locales,',')"/>
             <xsl:with-param name="filepath" select="$filepath"/>
             <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
           </xsl:call-template>
         </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-view-history-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-view-history-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="filepath" select="$filepath"/>
            <xsl:with-param name="CurrentEvidenceEntity" select="$CurrentEvidenceEntity"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

  <xsl:template name="write-view-history-properties">

    <xsl:param name="locale"/>
    <xsl:param name="filepath"/>
    <xsl:param name="CurrentEvidenceEntity"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

      <redirect:write select="concat($filepath, $locale, '.properties')">
      <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

<xsl:if test="count($generalProperties/Help.PageDescription.ViewEntity)&gt;0"><xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.ViewEntity"/>
          <xsl:with-param name="evidenceNode" select="."/>
	      <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
        </xsl:call-template>
      </xsl:if>

      <xsl:if test="count($generalProperties/Page.Title.ViewEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ViewEntity "/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
</xsl:call-template>
    <xsl:text>&#xa;</xsl:text>
      </xsl:if>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Details "/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.History "/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>

    </redirect:write>
      </xsl:if>
    </xsl:template>

</xsl:stylesheet>
