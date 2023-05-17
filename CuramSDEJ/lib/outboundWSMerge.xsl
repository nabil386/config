<?xml version="1.0"?>

<!-- Copyright 2010 Curam Software Ltd.
  All rights reserved.
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information").  You shall not disclose such
  Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.

  This XSLT merges outbound web services configuration files the main file, 
  with a delta file; the merge file, to produce a new outbound web services
  configuration file. Duplicate service names are copied depending on
  precedence; the main file has the highest precedence.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:param name="mergeFileName"/>
  <xsl:preserve-space elements="services"/>
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

  <!-- Assign the main source file -->
  <xsl:variable name="mainfileroot" select="/"/>

  <!--Load the merge file -->
  <xsl:variable name="mergefile" select="document($mergeFileName)"/>

  <!-- Start to combine the files -->
  <xsl:template match="services">
    <xsl:copy>
      <!-- Copy the attributes from the main file services tag including the schema -->
      <xsl:apply-templates select="@*|node()"/>
      <xsl:call-template name="new_service">
        <xsl:with-param name="this-services" select="."/>
      </xsl:call-template>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*|*">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template name="new_service">
    <xsl:param name="this-services"/>
    <xsl:for-each select="$mergefile//service">
      <xsl:if test="count($this-services/service[@name = current()/@name]) = 0">
        <xsl:copy-of select="."/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template match="service">

    <xsl:variable name="this-service" select="." />

    <xsl:copy>
      <xsl:copy-of select="@*"/>

      <!-- Search for service in the merge file of the same name,
           and add any new service entries that exist in them, only
           if the service name does not exist already in the main file. -->
      <xsl:for-each select="$mergefile//service[@name = $this-service/@name]">
        <!-- Test to see if the service name is not already in the main file -->
        <xsl:if test="count($this-service[@name = current()//@name]) = 0">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>

    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
