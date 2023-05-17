<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2007 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose such
Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<!--
Performs a near identity transformation of the Checkstyle XML-format report,
but truncates the file names after the given project name to reduce the
subsequent length of the path to the individual HTML files generated for each
file. For some files, the path length would otherwise exceed the limit that
Windows is capable of handling.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output encoding="UTF-8" method="xml" />

  <xsl:param name="project.name" select="''"/>

  <!--
  Copy the report elements.
  -->
  <xsl:template match="/">
    <xsl:apply-templates select="*" />
  </xsl:template>

  <xsl:template match="node() | @*">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="file">
    <file>
      <xsl:attribute name="name">
        <xsl:apply-templates select="." mode="short-name"/>
      </xsl:attribute>
      <xsl:apply-templates select="node() | @*[local-name() != 'name']"/>
    </file>
  </xsl:template>

  <!--
  Truncate the file name. Keep only the part after the project name, less one
  extra path separator character.
  -->
  <xsl:template match="file" mode="short-name">
    <xsl:value-of select="substring(substring-after(@name, $project.name), 2)"/>
  </xsl:template>

</xsl:stylesheet>
