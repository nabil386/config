<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils"
    exclude-result-prefixes="request-utils">

	<xsl:output method="xml" indent="no"
		omit-xml-declaration="yes" />

	<!--The supplied NodeSet containing the calendar config data. -->
	<xsl:param name="calendar-config" />
	<!-- The Text of the Date Bar -->
	<xsl:param name="DateBarText" />
	<!-- This parameter contains any __o3xxx parameters that need to be passed 
		on. -->
	<xsl:param name="o3Parameters" />
	<!-- User current locale -->
	<xsl:param name="locale" />
	<!-- Table summary for screen readers. -->
	<xsl:param name="summary" />
	<!-- The url of the web server with static content -->
	<xsl:param name="static-content-server-url" />
	<!-- The username variable is supplied by the Transformer object in the calendar jsp tag. -->
    <xsl:param name="username" />

	<!-- 12 or 24 hour mode -->
	<xsl:param name="app-time-format" />
	<xsl:variable name="mode-override">
		<xsl:choose>
			<xsl:when test="$config/DAY_VIEW_TIME_FORMAT">
				<xsl:value-of select="$config/DAY_VIEW_TIME_FORMAT" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$app-time-format" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>


	<xsl:attribute-set name="dateBar">
		<xsl:attribute name="class">dateBar</xsl:attribute>
	</xsl:attribute-set>


	<!--The details page associated with this calendar. -->
	<xsl:variable name="calendarType"
		select="CURAM_CALENDAR_DATA/@TYPE" />
	<!--The calendar configuration node with all details of calendar for this 
		type. -->
	<xsl:variable name="config"
		select="$calendar-config/CONFIGURATION/CALENDAR[@TYPE =$calendarType]" />

	<!-- Determines the hyperlink location of an event description and generates 
		the appropriate "href" attribute. -->
	<xsl:template name="add-href">
	
	  <xsl:attribute name="href">
	  
	  <xsl:variable name="url-no-hash">
        <xsl:value-of select="$config/DESCRIPTION_LOCATION" />
        <xsl:text>?</xsl:text>
        <xsl:apply-templates select="." mode="determineLink" />
        <xsl:value-of select="$o3Parameters" />
      </xsl:variable>
      
      <xsl:variable name="url-with-hash">
        <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
      </xsl:variable>
      
      <xsl:value-of select="$url-with-hash"/>
      
      </xsl:attribute>
	</xsl:template>

  <!-- Matches a start on an event--> 
  <xsl:template match="STARTTIME|ENDTIME">
    <xsl:choose>
        <xsl:when test="$mode-override = 12">
            <xsl:variable name="first-hour" select="number(substring(.,1,2))"/>
            <xsl:variable name="minutes" select="number(substring(.,4,2))" />
            <xsl:variable name="hour-value" select="$first-hour - 12*number($first-hour &gt; 12)"/> 
			<xsl:if test="$hour-value &lt; 10">0</xsl:if>
			<xsl:value-of select="$hour-value"/>
			<xsl:text>:</xsl:text>
			<xsl:if test="$minutes &lt; 30">0</xsl:if>
			<xsl:value-of select="$minutes"/>
            <!--   <xsl:value-of select="concat($hour-value, ':', $minutes)"/> -->
            <xsl:choose>
                <xsl:when test="$first-hour &lt; 12 and $first-hour &gt;= 00 "><sup> AM</sup></xsl:when>
            	<xsl:otherwise><sup> PM</sup></xsl:otherwise>
            </xsl:choose> 
        </xsl:when> 
        <xsl:otherwise>
            <xsl:value-of select="substring(.,1,5)"/>
        </xsl:otherwise> 
    </xsl:choose>
  </xsl:template>



	<!-- Adds the onclick event to the anchor -->
	<xsl:template name="add-onclick">
		<xsl:attribute name="onclick">
	      <xsl:text>calendarOpenModalDialog(arguments[0], this); return false;</xsl:text>
	  </xsl:attribute>
	</xsl:template>

	<!-- This template generates parameters for an event's description hyperlink. -->
	<xsl:template match="SINGLE_DAY_EVENT"
		mode="determineLink">
		<xsl:value-of
			select="concat('ID=', ID, '&#38;TYPE=', TYPE)" />
	</xsl:template>

	<!-- This template generates parameters for an event's description hyperlink. -->
	<xsl:template match="EVENT" mode="determineLink">
		<xsl:value-of
			select="concat('ID=', ID,'&#38;RE=', RECURRING,
			'&#38;AT=', ATTENDEE, '&#38;RO=', READ_ONLY,
			'&#38;LV=', LEVEL, '&#38;AC=', ACCEPTANCE)" />
	</xsl:template>


</xsl:stylesheet>

  