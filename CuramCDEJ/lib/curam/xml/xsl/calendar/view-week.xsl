<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0" exclude-result-prefixes="tag request-utils"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tag="http://xml.apache.org/xalan/java/curam.omega3.taglib.widget.CalendarTag"
    xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils">
<!-- xsl:import must occur before any other elements -->
<xsl:import href="view-common.xsl"/>
  
    <!-- Calendar Tag calling this object -->
  <xsl:param name="CalendarTag" />
  <!-- The date of the selected day -->
  <xsl:param name="selectedDay" />
  <!-- The ordinal reference to the start month of the current week-->
  <xsl:param name="startMonth" />
  <!-- The date that the start month begins with -->
  <xsl:param name="startDate" />
  <!-- The date that the start month ends with -->
  <xsl:param name="monthEnd" />
  <!-- The ordinal reference to the end month of the current week -->
  <xsl:param name="endMonth" />
  <!-- The year of the start month -->
  <xsl:param name="startYear" />
  <!-- The year of the end month -->
  <xsl:param name="endYear" />
  <!-- The Page Name variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="page-name" />
  <!-- The Hash Token Param variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="secure-urls-hash-token-param" />
  <!-- The is secure URLs solution enabled variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="is-secure-urls-solution-enabled" />
  <!-- The username variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="username" />



  <!-- Locale-formatted days of the week -->
  <xsl:param name="day1format"/>
  <xsl:param name="day2format"/>
  <xsl:param name="day3format"/>
  <xsl:param name="day4format"/>
  <xsl:param name="day5format"/>
  <xsl:param name="day6format"/>
  <xsl:param name="day7format"/>
  
  <!-- Current day prompt for screen reader users -->
  <xsl:param name="todayPrompt"/>

  <!-- Main template -->
  <xsl:template match="/CURAM_CALENDAR_DATA">
    <div xsl:use-attribute-sets="dateBar">
      <xsl:value-of select="$DateBarText"/>
    </div>
    <table width="100%" class="week-view" id="weekView" summary="{$summary}" role="presentation">
      <tbody>
        <tr>
          <td>
            <xsl:attribute name="class">
	      <xsl:text>first-calendar-field</xsl:text>
            </xsl:attribute>
            <xsl:call-template name="print-date">
              <xsl:with-param name="formatted-date" select="$day1format"/>
              <xsl:with-param name="date" select="$startDate" />
            </xsl:call-template>
          </td>
          <td>
            <xsl:attribute name="class">
	      <xsl:text>last-calendar-field</xsl:text>
            </xsl:attribute>
            <xsl:call-template name="print-date">
              <xsl:with-param name="formatted-date" select="$day4format"/>
              <xsl:with-param name="date" select="$startDate + 3" />
            </xsl:call-template>
          </td>
        </tr>
        <tr>
          <td>
            <xsl:attribute name="class">
	      <xsl:text>first-calendar-field</xsl:text>
            </xsl:attribute>
            <xsl:call-template name="print-date">
              <xsl:with-param name="formatted-date" select="$day2format"/>
              <xsl:with-param name="date" select="$startDate + 1" />
            </xsl:call-template>
          </td>
          <td>
            <xsl:attribute name="class">
	      <xsl:text>last-calendar-field</xsl:text>
            </xsl:attribute>
            <xsl:call-template name="print-date">
              <xsl:with-param name="formatted-date" select="$day5format"/>
              <xsl:with-param name="date" select="$startDate + 4" />
            </xsl:call-template>
          </td>
        </tr>
        <tr>
          <td>
            <xsl:attribute name="class">
	      <xsl:text>first-calendar-field</xsl:text>
            </xsl:attribute>
            <xsl:call-template name="print-date">
              <xsl:with-param name="formatted-date" select="$day3format"/>
              <xsl:with-param name="date" select="$startDate + 2" />
            </xsl:call-template>
          </td>
          <td class="weekend-td">
            <xsl:attribute name="class">
	      <xsl:text>last-calendar-field</xsl:text>
            </xsl:attribute>
            <xsl:call-template name="print-date">
              <xsl:with-param name="formatted-date" select="$day6format"/>
              <xsl:with-param name="date" select="$startDate + 5" />
            </xsl:call-template>
          </td>
        </tr>
        <tr>
          <td class="week-empty-cell"></td>
          <td class="weekend-td">
            <xsl:attribute name="class">
	      <xsl:text>last-calendar-field</xsl:text>
            </xsl:attribute>
            <xsl:call-template name="print-date">
              <xsl:with-param name="formatted-date" select="$day7format"/>
              <xsl:with-param name="date" select="$startDate + 6" />
            </xsl:call-template>
          </td>
        </tr>
      </tbody>
    </table>
  </xsl:template>

  <!-- Draws cell caption hyperlink with date for current month. -->
  <xsl:template name="print-date">
    <xsl:param name="formatted-date"/>
    <xsl:param name="day" />
    <xsl:param name="date" />

    <xsl:variable name="adjusted-date">
      <xsl:choose>
        <xsl:when test="$date &gt; $monthEnd">
          <xsl:value-of select="$date - $monthEnd"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$date"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="adjusted-month">
      <xsl:choose>
        <xsl:when test="$date &gt; $monthEnd">
          <xsl:value-of select="$endMonth"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$startMonth"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <div class="cell-caption">
      <span>
        <xsl:if test="$selectedDay = $adjusted-date">
          <xsl:attribute name="class">
            <xsl:text>current-day-link</xsl:text>
          </xsl:attribute>
        </xsl:if>
        
        <xsl:variable name="href-value">
          <xsl:choose>
            <xsl:when test="$is-secure-urls-solution-enabled = 'true'">
		      <xsl:variable name="url-no-pagename">
		        <xsl:value-of select="concat('?startDate=',
		                       tag:getDate($CalendarTag, $adjusted-date,
		                                   $adjusted-month, $startYear),
		                       $o3Parameters)"/>
		      </xsl:variable>
		      <xsl:variable name="url-no-hash">
                <xsl:value-of select="$page-name"/>
		        <xsl:value-of select="$url-no-pagename"/>
		      </xsl:variable>
		      
		      <xsl:variable name="hash-token-value">
		        <xsl:value-of select="request-utils:getHashOfURL($url-no-hash, $username)"/>
		      </xsl:variable>
		        
		      <xsl:variable name="url-with-hash">
		        <xsl:value-of select="$url-no-pagename"/>
		        <xsl:value-of select="$secure-urls-hash-token-param"/>
		        <xsl:value-of select="$hash-token-value"/>
		      </xsl:variable>
		      
		      <xsl:value-of select="$url-with-hash"/>
		    </xsl:when>
		    <xsl:otherwise>
              <xsl:value-of select="concat('?startDate=',
		                       tag:getDate($CalendarTag, $adjusted-date,
		                                   $adjusted-month, $startYear),
		                       $o3Parameters)"/>
             </xsl:otherwise>
           </xsl:choose>  
        </xsl:variable>
        <a href="{$href-value}">
          <xsl:value-of select="$formatted-date"/>
          <xsl:if test="$selectedDay = $adjusted-date">
            <span class="hidden">
              <xsl:text> - </xsl:text>
              <xsl:value-of select="$todayPrompt"/>
            </span>
          </xsl:if>
        </a>
      </span>
    </div>

    <xsl:call-template name="do-cell-body" >
      <xsl:with-param name="eventDate" select="$adjusted-date" />
      <xsl:with-param name="eventMonth" select="$adjusted-month" />
      <xsl:with-param name="eventYear" select="$startYear" />
    </xsl:call-template>
  </xsl:template>

  <!-- Generates the table containing text of each event in a cell body. -->
  <xsl:template name="do-cell-body">
    <xsl:param name="eventDate"/>
    <xsl:param name="eventMonth"/>
    <xsl:param name="eventYear"/>

    <xsl:variable name="single-day-events"
        select="SINGLE_DAY_EVENT
                  [number(substring(DATE, 9, 2)) = number($eventDate)]
                  [number(substring(DATE, 6, 2)) = number($eventMonth)]
                  [number(substring(DATE, 1, 4)) = number($eventYear)]"/>
    <xsl:variable name="other-events"
        select="EVENT
                  [number(substring(DATE, 9, 2)) = number($eventDate)]
                  [number(substring(DATE, 6, 2)) = number($eventMonth)]
                  [number(substring(DATE, 1, 4)) = number($eventYear)]"/>

    <xsl:if test="$single-day-events or $other-events">
      <table class="week-view-events">
        <tbody>
          <!-- Display single day events first -->
          <xsl:apply-templates select="$single-day-events"/>
          <xsl:apply-templates select="$other-events">
            <xsl:sort select="STARTTIME"/>
          </xsl:apply-templates>
        </tbody>
      </table>
    </xsl:if>
  </xsl:template>

  <!-- Regular events. -->
  <xsl:template match="EVENT">
    <tr>
      <td>
        <a>
          <xsl:call-template name="add-href"/>
          <xsl:call-template name="add-onclick"/>
          <span class="event-text">
            <xsl:if test="ALL_DAY = 'false'">
              <xsl:apply-templates select="STARTTIME"/>
              <xsl:text> - </xsl:text>
            </xsl:if>
            <xsl:value-of select="DESCRIPTION"/>
          </span>
        </a>
      </td>
    </tr>
  </xsl:template>

  <!-- Single-day events. -->
  <xsl:template match="SINGLE_DAY_EVENT">
    <tr>
      <td>
        <xsl:call-template name="event-type-image"/>
        <a>
          <xsl:call-template name="add-href"/>
          <xsl:call-template name="add-onclick"/>
          <span class="event-text">
            <xsl:value-of select="DESCRIPTION"/>
          </span>
        </a>
      </td>
    </tr>
  </xsl:template>

  <!-- Template for matching event type to image -->
  <xsl:template name="event-type-image">
    <xsl:variable name="type" select="TYPE"/>

    <xsl:for-each select="$config/../EVENT_TYPES/TYPE[@NAME = $type]">
      <img src="{concat($static-content-server-url, '/', @ICON)}" alt=""/>
      <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
    </xsl:for-each>
  </xsl:template>
  

</xsl:stylesheet>
