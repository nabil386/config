<?xml version="1.0"?>

<!-- Copyright 2004-2008 Curam Software Ltd.

  All rights reserved.
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information").  You shall not disclose such
  Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.

  This XSLT merges a codetable file; the main codetable file, with a delta codetable file;
  the merge file, to produce a new codetable file. Duplicate codetable names are copied
  depending on precedence; the main codetable file has the highest precedence.
  All locales are merged, any duplicates in the merge file are ignored.

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:param name="mergeFileName"/>
  <xsl:preserve-space elements="root property locales"/>
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

  <!-- Assign the custom source file -->
  <xsl:variable name="mainfileroot" select="/"/>

  <!--Load the core file -->
  <xsl:variable name="mergefile" select="document($mergeFileName)"/>

  <!-- Assign the list of prx names from the main prx file -->
  <xsl:variable name="mainPrxList" select="$mainfileroot/root/property"/>

  <!-- Start to combine the files -->
  <xsl:template match="root">
    <xsl:copy>
      <!-- Copy the attributes from the main prx file root tag including the schema. -->
      <xsl:apply-templates select="@*|node()"/>
      <xsl:call-template name="new_custom_properties"/>
      <xsl:call-template name="new_core_properties"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*|*">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template match="property" name="property">
    <xsl:variable name="this-property" select="." />

    <xsl:if test="$this-property[@name = $mergefile/root/property/@name]">
    <xsl:copy>
      <xsl:copy-of select="$mergefile/root/property[@name = $this-property/@name]/@dynamic"/>
      <xsl:copy-of select="$mergefile/root/property[@name = $this-property/@name]/@constant"/>
      <xsl:copy-of select="$this-property/@name"/>
      <xsl:copy-of select="$mergefile/root/property[@name = $this-property/@name]/type"/>

      <xsl:if test="$this-property/value">
        <xsl:copy-of select="./value"/>
      </xsl:if>
      <xsl:if test="$this-property/default-value">
        <xsl:copy-of select="./default-value"/>
      </xsl:if>
      <xsl:copy-of select="$mergefile/root/property[@name = $this-property/@name]/category"/>
      <locales>
      <xsl:for-each select="$this-property/locales/locale">
        <xsl:copy-of select="."/>
      </xsl:for-each>

      <xsl:for-each select="$mergefile/root/property[@name = $this-property/@name]/locales/locale">
        <xsl:if test="not($this-property/locales/locale[((@language = current()/@language)
                        and ((not(@country) and not(current()/@country))
                          or (@country = current()/@country)))])">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>
      </locales>
    </xsl:copy>
    </xsl:if>
  </xsl:template>

  <xsl:template name="new_custom_properties">
    <xsl:for-each select="$mainPrxList[not(@name = $mergefile/root/property/@name)]">
      <xsl:copy-of select="."/>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="new_core_properties">
    <xsl:for-each select="$mergefile/root/property[not(@name = $mainPrxList/@name)]">
      <xsl:copy-of select="."/>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
