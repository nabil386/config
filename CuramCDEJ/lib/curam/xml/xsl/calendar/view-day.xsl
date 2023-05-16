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
<!-- xsl:import must occur before any other elements -->
<xsl:import href="view-common.xsl"/>

  <!-- variables provided by transformer object in CalendarTag -->
  <!-- The current date to match events against -->
  <xsl:param name="EventDate"/>
  <!-- The Current Date variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="CurrentDate" />
  <!-- The Page Name variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="page-name" />
  <!-- The Hash Token Param variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="secure-urls-hash-token-param" />
  <!-- The is secure URLs solution enabled variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="is-secure-urls-solution-enabled" />
  <!-- The username variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="username" />

  <!-- This template match functions as the main loop for placing all events in the correct positions on a page. -->
  <xsl:template match="/CURAM_CALENDAR_DATA">
    <div xsl:use-attribute-sets="dateBar">
      <xsl:value-of select="$DateBarText"/>
    </div>
    <table>
      <xsl:attribute name="summary"><xsl:value-of select="$summary" /></xsl:attribute>
      <xsl:attribute name="id">dayView</xsl:attribute>
      <xsl:attribute name="width">100%</xsl:attribute>
      <xsl:attribute name="cellpadding">5</xsl:attribute>
      <xsl:attribute name="class">dayView</xsl:attribute>
      <tbody>
        <xsl:call-template name="all_day"></xsl:call-template>
        <xsl:call-template name="print-hour">
          <xsl:with-param name="first-hour" select="7"/>
          <xsl:with-param name="last-hour" select="23"/>
          <xsl:with-param name="mode" select="$mode-override"/>
          <xsl:with-param name="counter" select="2"/>
        </xsl:call-template>
      </tbody>
    </table>
  </xsl:template>
  
    
  <!--This template functions as the main template for the day view. It prints out each hour in half hour increments recursively. Any non all-day events are inserted here according to their start times.-->
  <xsl:template name="print-hour">
    <!-- first-hour  - The first hour to include in the range -->
    <xsl:param name="first-hour"/>
    <!-- last-hour   - The last hour to include in the range -->
    <xsl:param name="last-hour"/>
    <!-- mode         - '12' for am/pm display or '24' for 24hr clock display-->
    <xsl:param name="mode"/>
    <!--counter         - row counter, used for calculating location of events in right-hand column.-->
    <xsl:param name="counter"/>
      <xsl:if test="$first-hour &lt;= $last-hour">
        <tr>
          <xsl:if test="$counter mod 2 = 0">
            <td align="right" rowspan="2" class="dayViewRow">
              <a>
                <xsl:attribute name="href">
                  <xsl:choose>
                    <xsl:when test="$is-secure-urls-solution-enabled = 'true'">
	                  <xsl:variable name="url-no-pagename">
	                    <xsl:call-template name="createLink">
	                      <xsl:with-param name="hour" select="$first-hour"/>
	                    </xsl:call-template>
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
                        <xsl:call-template name="createLink">
	                      <xsl:with-param name="hour" select="$first-hour"/>
	                    </xsl:call-template>
                    </xsl:otherwise> 
                  </xsl:choose> 
                </xsl:attribute>
                <xsl:choose>
                  <xsl:when test="$mode = 12">
                    <xsl:variable name="hour-value" select="$first-hour - 12*number($first-hour &gt; 12)"/>
                    <xsl:if test="$hour-value &lt; 10">0</xsl:if>
                    <xsl:value-of select="$hour-value"/>
                    <xsl:choose>
                    <xsl:when test="$first-hour = 12"><sup> PM</sup></xsl:when>
                    <xsl:when test="$first-hour = 7"><sup> AM</sup></xsl:when>                    
                    <xsl:otherwise>:00</xsl:otherwise>
                    </xsl:choose> 
                  </xsl:when> 
                  <xsl:otherwise>
                    <xsl:if test="$first-hour &lt; 10">0</xsl:if>
                    <xsl:value-of select="$first-hour"/>:00
                  </xsl:otherwise> 
                </xsl:choose>
              </a>
            </td>
          </xsl:if>
          <td>
            <xsl:if test="$counter = 2">
              <!-- Any events before the start time should go here -->
              <xsl:for-each select="EVENT[number(substring(STARTTIME,1,2)) &lt; number($first-hour)]
                                                               [ALL_DAY = 'false'][./DATE = $EventDate]">
                <xsl:sort select="STARTTIME"/>
                <xsl:apply-templates select="."/>
              </xsl:for-each>
            </xsl:if>
            <xsl:for-each select="EVENT[number(substring(STARTTIME,1,2)) = number($first-hour)]
                                            [($counter mod 2)=((substring(STARTTIME,4,2)) &gt;= 30)]
                                                                [ALL_DAY = 'false'][./DATE = $EventDate]">
              <xsl:sort select="STARTTIME"/>
              <xsl:apply-templates select="."/>
            </xsl:for-each>
          </td>
        </tr>
        <xsl:call-template name="print-hour">
          <xsl:with-param name="counter" select="$counter + 1"/>
          <xsl:with-param name="first-hour" select="$first-hour + ($counter mod 2)"/>
          <xsl:with-param name="last-hour" select="$last-hour"/>
          <xsl:with-param name="mode" select="$mode"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:template>

    <!-- Matches an event and applies the appropriate template-->
    <xsl:template match="EVENT">
      <div class="event"> 
        <xsl:apply-templates select="STARTTIME"/>
        <xsl:text> - </xsl:text>
        <xsl:apply-templates select="ENDTIME"/>
        <xsl:text> </xsl:text>
        <a>
          <xsl:call-template name="add-href"/>
          <xsl:call-template name="add-onclick"/>
          <xsl:value-of select="DESCRIPTION"/>
        </a>
      </div>
    </xsl:template>

    <!-- This template matches all day events and single day events for the top of the day view-->
    <xsl:template name="all_day">
      <xsl:choose>
        <xsl:when test="count(EVENT[ALL_DAY ='true']) > 0 or count(SINGLE_DAY_x)">
          <tr>
            <th scope="col" align="right" style="width: 25px;">
              <xsl:attribute name="class">
  	      <xsl:text>first-calendar-field</xsl:text>
              </xsl:attribute>
            </th>
            <th scope="col" class="all-day-events-cell">
              <xsl:for-each select="SINGLE_DAY_EVENT">
                <xsl:if test="./DATE = $EventDate">
                  <div class="event"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                    <xsl:call-template name="event-type-image">
                      <xsl:with-param name="type" select="./TYPE" />
                    </xsl:call-template>
                    <a>
                      <xsl:call-template name="add-href"/>
                      <xsl:call-template name="add-onclick"/>
                      <xsl:value-of select="DESCRIPTION"/>
                    </a>
                  </div>
                </xsl:if>
              </xsl:for-each>
              <xsl:for-each select="EVENT[ALL_DAY = 'true']">
                <xsl:if test="./DATE = $EventDate">
                  <div class="event"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                    <a>
                      <xsl:call-template name="add-href"/>
                      <xsl:call-template name="add-onclick"/>
                      <xsl:value-of select="DESCRIPTION"/>
                    </a>
                  </div>
                </xsl:if>
              </xsl:for-each>
            </th>
          </tr>
        </xsl:when>
        <xsl:otherwise>
          <xsl:if test="count(EVENT[ALL_DAY ='true']) = 0">
            <tr>
              <th scope="col" align="right" style="width: 25px;">
                <xsl:attribute name="class">
                  <xsl:text>first-calendar-field</xsl:text>
                </xsl:attribute>
              </th>
              <th scope="col" class="no-all-day-events-cell">
              </th>
            </tr>
          </xsl:if>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

    <!-- Template for matching event type to image -->
    <xsl:template name="event-type-image">
      <xsl:param name="type" />
      <xsl:if test="$config/../EVENT_TYPES">
        <xsl:for-each select="$config/../EVENT_TYPES/TYPE">
          <xsl:if test="@NAME = $type" >
            <img src="{$static-content-server-url}/{@ICON}" alt=""></img>
            <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
          </xsl:if>
        </xsl:for-each>
      </xsl:if>
    </xsl:template>
    
        <!-- This template generates a hyperlink for creating a new event.-->
    <xsl:template name="createLink">
      <!-- start time of this event -->
      <xsl:param name="hour"/>
      <xsl:text>?startTime=</xsl:text>
      <xsl:number value="$hour" format="01"/>
      <xsl:text>:00:00&#38;startDate=</xsl:text>
      <xsl:value-of select="$CurrentDate"/>
      <xsl:value-of select="$o3Parameters"/>
    </xsl:template>
    

</xsl:stylesheet>
