<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2006 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose such
Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<!--
Extracts some basic information from a Checkstyle XML-format report and
creates a properties file that can be included by Ant.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output encoding="ISO-8859-1" method="text" />

  <xsl:template match="/">
    <xsl:apply-templates select="checkstyle" />
  </xsl:template>

  <xsl:template match="checkstyle">
    <!-- Number of files. -->
    <xsl:text>style.files=</xsl:text>
    <xsl:value-of select="count(file)"/>
    <xsl:text>&#xa;</xsl:text>

    <!-- Number of style errors across all files. -->
    <xsl:text>style.errors=</xsl:text>
    <xsl:value-of select="count(file/error)"/>
    <xsl:text>&#xa;</xsl:text>

    <!-- Number of files containing one or more style errors. -->
    <xsl:text>style.files.in.error=</xsl:text>
    <xsl:value-of select="count(file[error])"/>
    <xsl:text>&#xa;</xsl:text>

    <!-- Percentage of good files. -->
    <xsl:text>style.pass.rate=</xsl:text>
    <xsl:value-of
      select="format-number((count(file) - count(file[error])) div count(file),
                            '##.#%')"/>
    <xsl:text>&#xa;</xsl:text>

    <!-- Percentage of bad files. -->
    <xsl:text>style.fail.rate=</xsl:text>
    <xsl:value-of
      select="format-number(count(file[error]) div count(file), '##.#%')"/>
    <xsl:text>&#xa;</xsl:text>
  </xsl:template>

</xsl:stylesheet>
