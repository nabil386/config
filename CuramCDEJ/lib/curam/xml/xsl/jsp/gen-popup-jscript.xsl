<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (c) 2002 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Interactive
Technology Design, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*" />
  <xsl:template match="APP_CONFIG">
    <xsl:text>require(["curam/omega3-util"], function(util) {</xsl:text>
    <xsl:apply-templates select="POPUP_PAGES/POPUP_PAGE" />
    <xsl:text>util.setPopupProperties("../CDEJ/popups/date-selector.jsp","SVR_DATE","",280,301,false,null,null,null);&#xa;</xsl:text>
    <xsl:text>util.setPopupProperties("../CDEJ/popups/date-selector.jsp","SVR_DATETIME","",209,193,false,null,null,null);&#xa;</xsl:text>
    <xsl:text>util.setPopupProperties("frequency-editor.jsp","FREQUENCY_PATTERN","",870,undefined,false,null,null,null);});&#xa;</xsl:text>
  </xsl:template>
  <xsl:template match="POPUP_PAGE">
    <xsl:variable name="create-page">
      <xsl:choose>
        <xsl:when test="@CREATE_PAGE_ID">
          <xsl:text>"</xsl:text>
          <xsl:value-of select="@CREATE_PAGE_ID" />
          <xsl:text>Page.do"</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>null</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="insert-mode">
      <xsl:choose>
        <xsl:when test="@CONTROL_INSERT_MODE">
          <xsl:text>"</xsl:text>
          <xsl:value-of select="@CONTROL_INSERT_MODE" />
          <xsl:text>"</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>null</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:text>util.setPopupProperties("</xsl:text>
    <xsl:value-of select="@PAGE_ID" />
    <xsl:text>Page.do","</xsl:text>
    <xsl:value-of select="DOMAIN" />
    <xsl:text>","</xsl:text>
    <xsl:value-of select="CT_CODE" />
    <xsl:text>",</xsl:text>
    <xsl:choose>
      <!-- making the WIDTH element optional. Not specifying it
       results in default height being used. -->
      <xsl:when test="WIDTH">
        <xsl:value-of select="WIDTH" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>undefined</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>,</xsl:text>
    <xsl:choose>
      <!-- making the HEIGHT element optional. Not specifying it
       results in height being calculated automatically. -->
      <xsl:when test="HEIGHT">
        <xsl:value-of select="HEIGHT" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>undefined</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>,</xsl:text>
    <xsl:value-of select="SCROLLBARS" />
    <xsl:text>,</xsl:text>
    <xsl:value-of select="$create-page"/>
    <xsl:text>,</xsl:text>
    <xsl:value-of select="$insert-mode"/>
    <xsl:text>);&#xa;</xsl:text>
  </xsl:template>
</xsl:stylesheet>
