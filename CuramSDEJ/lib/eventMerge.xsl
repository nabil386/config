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
  <xsl:preserve-space elements="events"/>
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

  <!-- Assign the main source file -->
  <xsl:variable name="mainfileroot" select="/"/>

  <!--Load the merge file -->
  <xsl:variable name="mergefile" select="document($mergeFileName)"/>

  <!-- Assign the list of handler registration from the main file -->
  <xsl:variable name="mainEventsList" select="$mainfileroot/events/event-class"/>

  <!-- Start to combine the files -->
  <xsl:template match="events">
    <xsl:copy>
      <!-- Copy the attributes from the main file events tag including the schema -->
      <xsl:apply-templates select="@*|node()"/>
      <xsl:call-template name="new_event_class">
        <xsl:with-param name="this-events" select="."/>
      </xsl:call-template>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*|*">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template name="new_event_class">
    <xsl:param name="this-events"/>
    <xsl:for-each select="$mergefile//event-class">
      <xsl:if test="count($this-events/event-class[@identifier = current()/@identifier]) = 0">
        <xsl:copy-of select="."/>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template match="event-class">

    <xsl:variable name="this-event-class" select="." />

    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:copy-of select="./annotation"/>

      <!-- Add every event type in an event type from the main file. -->
      <xsl:for-each select="$this-event-class/event-type">
        <xsl:copy-of select="."/>
      </xsl:for-each>

      <!-- Search for event types in the merge file of the same name,
           and add any new event type entries that exist in them, only
           if the event type does not exist already in the main file. -->
      <xsl:for-each select="$mergefile//event-class[@identifier = $this-event-class/@identifier]/event-type">
        <!-- Test to see if the event type is not already in the main file -->
        <xsl:if test="count($this-event-class/event-type[@identifier = current()/@identifier]) = 0">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>

    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>