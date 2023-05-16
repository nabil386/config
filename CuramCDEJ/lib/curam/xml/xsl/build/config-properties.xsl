<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright (c) 2005 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Interactive
Technology Design, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.

$Id$
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text" indent="no"/>

  <xsl:template match="APP_CONFIG">
    <xsl:text>static.content.url=</xsl:text>
    <xsl:choose>
      <xsl:when test="/APP_CONFIG/STATIC_CONTENT_SERVER/URL">
        <xsl:variable name="url" select="normalize-space(/APP_CONFIG/STATIC_CONTENT_SERVER/URL)"/>
        <xsl:choose>
          <xsl:when test="substring($url, string-length($url), 1) = '/'">
            <xsl:value-of select="substring($url, 1, string-length($url)-1)"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$url"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>..</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>