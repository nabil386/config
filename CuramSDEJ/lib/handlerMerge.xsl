<?xml version="1.0"?>

<!-- Copyright 2005 Curam Software Ltd.
  All rights reserved.
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information").  You shall not disclose such
  Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.

  This XSLT merges a handler registration file; the main file, with a delta file;
  the merge file, to produce a new handler registration file. Duplicate handler
  registrations names are copied depending on precedence; the main file has the
  highest precedence.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:param name="mergeFileName"/>
  <xsl:preserve-space elements="registrations"/>
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

  <!-- Assign the main source file -->
  <xsl:variable name="mainfileroot" select="/"/>

  <!--Load the merge file -->
  <xsl:variable name="mergefile" select="document($mergeFileName)"/>

  <!-- Assign the list of handler registration from the main file -->
  <xsl:variable name="mainRegistrationsList" select="$mainfileroot/registrations/event-registration"/>

  <!-- Start to combine the files -->
  <xsl:template match="registrations">
    <xsl:copy>
      <!-- Copy the attributes from the main file registrations tag including the schema -->
      <xsl:apply-templates select="@*|node()"/>
      <xsl:call-template name="new_registration">
        <xsl:with-param name="this-registrations" select="."/>
      </xsl:call-template>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*|*">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template name="new_registration">
    <xsl:param name="this-registrations"/>
    <xsl:for-each select="$mergefile//event-registration">
      <xsl:if test="count($this-registrations/event-registration[@handler = current()/@handler]) = 0">
        <xsl:copy-of select="."/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="event-registration" name="event-registration">
    <!-- Copy the event-registration tag and its attributes in the main file. -->
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates select="event-classes">
        <xsl:with-param name="this-event-registration" select="."/>
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="event-classes">
    <xsl:param name="this-event-registration"/>
    <xsl:variable name="this-event-classes" select="." />

    <xsl:copy>
      <xsl:copy-of select="@*"/>

      <!-- Add every event classes in a registration from the main file. -->
      <xsl:for-each select="$this-event-classes/event-class">
        <xsl:copy-of select="."/>
      </xsl:for-each>

      <!-- Search for event classes in the merge file of the same name,
           and add any new event class entries that exist in them, only
           if the event class does not exist already in the main file. -->
      <xsl:for-each select="$mergefile//event-registration[@handler = $this-event-registration/@handler]/event-classes/event-class">
        <!-- Test to see if the event class is not already in the main file -->
        <xsl:if test="count($this-event-classes/event-class[@identifier = current()/@identifier]) = 0">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>

    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>