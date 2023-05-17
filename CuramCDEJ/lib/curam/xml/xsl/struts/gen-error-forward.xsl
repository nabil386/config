<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright 2004 Curam Software Ltd.
  All rights reserved.

  This software is the confidential and proprietary information of Curam
  Software,  Ltd. ("Confidential Information"). You shall not disclose such
  Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" omit-xml-declaration="yes" indent="no" />

  <xsl:template match="APP_CONFIG">
    <xsl:choose>
      <xsl:when test="count(ERROR_PAGE[not(@TYPE)]) = 0">
        <!-- If there are no non-typed entries, take the first entry with a type. -->
        <xsl:call-template name="process-merged-errors">
          <xsl:with-param name="defaultErrorPage" select="ERROR_PAGE[1]/@PAGE_ID"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <!-- Always take the first default entry (no TYPE). There should only
             ever be one due to merging. -->
        <xsl:call-template name="process-merged-errors">
          <xsl:with-param name="defaultErrorPage" select="ERROR_PAGE[not(@TYPE)][1]/@PAGE_ID"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="process-merged-errors">
    <xsl:param name="defaultErrorPage"/>
    <global-forwards>
      <!-- Handle the entries with TYPE specified -->
      <xsl:for-each select="ERROR_PAGE[@TYPE]">
        <!-- For all the typed entries -->
        <xsl:call-template name="exception-to-error-page">
          <xsl:with-param name="name" select="concat('do', @TYPE ,'Error')"/>
          <xsl:with-param name="path-prefix"><xsl:call-template name="first-four-chars"><xsl:with-param name="string" select="@PAGE_ID"/></xsl:call-template></xsl:with-param>
          <xsl:with-param name="err-page" select="@PAGE_ID"/>
        </xsl:call-template>
      </xsl:for-each>
      <!-- Handle where APPLICATION error page hasn't been defined. -->
      <xsl:if test="count(ERROR_PAGE[@TYPE='APPLICATION']) = 0">
        <xsl:call-template name="handle-default-error-page">
          <xsl:with-param name="type" select="'APPLICATION'"/>
          <xsl:with-param name="defaultErrorPage" select="$defaultErrorPage"/>
        </xsl:call-template>
      </xsl:if>
      <!-- Handle where SYSTEM error page hasn't been defined. -->
      <xsl:if test="count(ERROR_PAGE[@TYPE='SYSTEM']) = 0">
        <xsl:call-template name="handle-default-error-page">
          <xsl:with-param name="type" select="'SYSTEM'"/>
          <xsl:with-param name="defaultErrorPage" select="$defaultErrorPage"/>
        </xsl:call-template>
      </xsl:if>
      <forward name="CTL1NEXT" path="/ag.AgendaServlet"/>
      <forward name="MOD" path="/ag.AgendaSummary"/>
      <forward name="CLOSING" path="/modal-closing.jspx"/>
      <forward name="doXSSError" path="/empty-error-page.jspx"/>      
    </global-forwards>
  </xsl:template>

  <xsl:template name="handle-default-error-page">
    <xsl:param name="defaultErrorPage"/>
    <xsl:param name="type"/>
    <xsl:call-template name="exception-to-error-page">
      <xsl:with-param name="name" select="concat('do', $type ,'Error')"/>
      <xsl:with-param name="path-prefix"><xsl:call-template name="first-four-chars"><xsl:with-param name="string" select="$defaultErrorPage"/></xsl:call-template></xsl:with-param>
      <xsl:with-param name="err-page" select="$defaultErrorPage"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="exception-to-error-page">
    <xsl:param name="name"/>
    <xsl:param name="path-prefix"/>
    <xsl:param name="err-page"/>
    <forward name="{$name}" path="/jsps/{$path-prefix}/{$err-page}.jspx"/>
  </xsl:template>

  <xsl:template name="first-four-chars">
    <xsl:param name="string"/>
    <xsl:value-of select="translate(substring($string, 1, 4),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
                                    'abcdefghijklmnopqrstuvwxyz')"/>
  </xsl:template>

</xsl:stylesheet>