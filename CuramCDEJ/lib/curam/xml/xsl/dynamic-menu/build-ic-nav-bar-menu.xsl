<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2004-2005, 2007 Curam Software Ltd.
All rights reserved.
 
This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:tag="http://xml.apache.org/xalan/java/curam.omega3.taglib.widget.DynamicMenuTag"
  xmlns:resources="http://xml.apache.org/xalan/java/curam.omega3.util.CDEJResources"
  exclude-result-prefixes="resources tag"   
  version="1.0">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>

  <xsl:param name="menu-config" />
  <xsl:param name="locale"/>
  <xsl:param name="static-content-server-url"/>

  <xsl:variable name="config" select="$menu-config/INTEGRATED_CASE_MENU_CONFIG" />
  <xsl:variable name="limit-to" select="$config/MAX_LENGTH"/>

  <xsl:template match="DYNAMIC_MENU">
    <div class="menu ic-tabs">
      <ul>
        <xsl:apply-templates select="LINK"/>
      </ul>
    </div>
  </xsl:template>

  <xsl:template match="LINK">
    <xsl:variable name="type" select="@TYPE"/>
    <xsl:variable name="image" select="$config/LINK[@TYPE=$type]/@IMAGE" />
    <xsl:variable name="image-desc"
        select="resources:getProperty($config/LINK[@TYPE=$type]/@TEXT)"/>
    <li>
      <a>
         <xsl:if test="position() = last()">
           <xsl:attribute name="class">selected</xsl:attribute>
         </xsl:if>
        <xsl:attribute name="href">
          <xsl:value-of select="@PAGE_ID"/>
          <xsl:text>Page.do</xsl:text>
          <xsl:apply-templates select="PARAMETER"/>
        </xsl:attribute>
        <!-- <img src="{$static-content-server-url}/{$image}" alt="{$image-desc}"/>-->
        <xsl:call-template name="textual-part">
          <xsl:with-param name="unformatted" select="tag:getLocalizedMessage(@DESC, $locale)"/>
        </xsl:call-template>
      </a>
    </li>
  </xsl:template>

  <xsl:template match="PARAMETER">
    <xsl:choose>
      <xsl:when test="position()=1">
        <xsl:text>?</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>&amp;</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:value-of select="@NAME"/>
    <xsl:text>=</xsl:text>
    <xsl:value-of select="@VALUE"/>
  </xsl:template>

  <xsl:template name="textual-part">
    <xsl:param name="unformatted"/>
    <xsl:choose>
      <xsl:when test="(($limit-to) and (string-length($unformatted) &gt; $limit-to))">
        <xsl:value-of select="concat(substring($unformatted, 0, $limit-to + 1),'...')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$unformatted"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>