<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Copyright 2004, 2007-2008 Curam Software Ltd. All rights reserved. This 
  software is the confidential and proprietary information of Curam Software, 
  Ltd. ("Confidential Information"). You shall not disclose such Confidential 
  Information and shall use it only in accordance with the terms of the license 
  agreement you entered into with Curam Software. -->
<xsl:stylesheet exclude-result-prefixes="tag bidi-utils"
  xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:tag="http://xml.apache.org/xalan/java/curam.omega3.taglib.widget.MeetingViewTag">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />
  <xsl:param name="locale" />
  <xsl:param name="static-content-server-url" />
  <xsl:param name="tag" />
  <xsl:param name="open-new-window" />
  <xsl:param name="interval" />
  <xsl:param name="amPmMode" />
  <xsl:param name="SingleSelect.Column.One" />
  <xsl:param name="SingleSelect.Column.Two" />
  <xsl:param name="SingleSelect.Column.Three" />
  <xsl:param name="Table.Summary" />
  <xsl:param name="User.Home.Alt" />

  <!-- Table summary for screen readers. -->
  <xsl:variable name="summary" select="$Table.Summary" />
  <xsl:variable name="intervalAmount">
    <xsl:value-of select="tag:getIntervalAmount($tag)" />
  </xsl:variable>
  <xsl:variable name="numberQuarterHours">
    <xsl:value-of select="tag:getNumQuarterHours($tag)" />
  </xsl:variable>
  <xsl:variable name="timeLineLength">
    <xsl:value-of select="$numberQuarterHours * 15 * ($interval div 15)" />
  </xsl:variable>
  <xsl:variable name="user-home-alt" select="$User.Home.Alt" />
  <xsl:template match="SCHEDULE">
    <xsl:variable name="single-quote">'</xsl:variable>
    <div id="holder" class="meeting-view single-select" style="width:100%;overflow:auto;">
      <table id="MeetingView" summary="{$summary}" style="width:100%;">
        <colgroup span="3">
          <col width="{120 + number($timeLineLength &lt;= 700)*50}" />
          <col width="{50 + number($timeLineLength &lt;= 700)*50}" />
          <col width="{$timeLineLength}" />
        </colgroup>
        <thead>
          <tr>
            <th class="user">
              <xsl:if test="bidi-utils:getResolvedBaseTextDirection($SingleSelect.Column.One) != ''">
                <xsl:attribute name="dir">
                  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection($SingleSelect.Column.One)"/>
                </xsl:attribute> 
              </xsl:if>
              <xsl:value-of select="$SingleSelect.Column.One" />
            </th>
            <th class="duration">
		          <xsl:if test="bidi-utils:getResolvedBaseTextDirection($SingleSelect.Column.Two) != ''">
                <xsl:attribute name="dir">
                  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection($SingleSelect.Column.Two)"/>
                </xsl:attribute> 
              </xsl:if>                 
              <xsl:value-of select="$SingleSelect.Column.Two" />
            </th>
            <th class="schedule">
		          <xsl:if test="bidi-utils:getResolvedBaseTextDirection($SingleSelect.Column.Three) != ''">
                <xsl:attribute name="dir">
                  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection($SingleSelect.Column.Three)"/>
                </xsl:attribute> 
              </xsl:if>                 
              <xsl:value-of select="$SingleSelect.Column.Three" />
            </th>
          </tr>
        </thead>
        <tbody>
          <xsl:comment>force closing tag</xsl:comment>
          <xsl:for-each select="./USER">
            <xsl:call-template name="select-row" />
          </xsl:for-each>
        </tbody>
      </table>
    </div>
  </xsl:template>
  <xsl:template name="select-row">
    <tr>
      <xsl:attribute name="class">
        <xsl:choose>
          <xsl:when test="position() mod 2 = 0">
            <xsl:text>darkRow</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>lightRow</xsl:text>
          </xsl:otherwise>
         </xsl:choose>
        </xsl:attribute>
      <xsl:apply-templates select="." mode="row-contents" />
    </tr>
  </xsl:template>
  <xsl:template match="USER" mode="row-contents">
    <xsl:choose>
      <xsl:when test="$amPmMode = 'true'">
        <td class="user12">
          <span>
            <xsl:if test="bidi-utils:getResolvedBaseTextDirection(@NAME) != ''">
              <xsl:attribute name="dir">
                <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@NAME)"/>
              </xsl:attribute>
            </xsl:if>
            <xsl:value-of select="@NAME" />
          </span>
        </td>
        <td class="duration12">
          <xsl:value-of disable-output-escaping="yes" select="tag:getUserDuration($tag, string(@ID))" />
        </td>
      </xsl:when>
      <xsl:otherwise>
        <td class="user">
          <span>          
		        <xsl:if test="bidi-utils:getResolvedBaseTextDirection(@NAME) != ''">
              <xsl:attribute name="dir">
                <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@NAME)"/>
              </xsl:attribute>
            </xsl:if>                          
            <xsl:value-of select="@NAME" />
          </span>
        </td>
        <td class="duration">
          <xsl:value-of disable-output-escaping="yes" select="tag:getUserDuration($tag, string(@ID))" />
        </td>
      </xsl:otherwise>
    </xsl:choose>
    <td class="schedule">
      <xsl:if test="$amPmMode = 'true'">
        <span class="format12"></span>
      </xsl:if>
      <span class="timeBar">
        <xsl:value-of disable-output-escaping="yes"
          select="tag:getTimeSlot($tag, string(@ID))" />
        <span class="timeLine">
          <br />
          <img src="{concat($static-content-server-url, '/themes/v6/images/meeting-view/blank.png')}"
               alt="" />
          <xsl:value-of disable-output-escaping="yes" select="tag:getTimeGrid($tag)" />
          <img src="{concat($static-content-server-url, '/themes/v6/images/meeting-view/blank.png')}"
               alt="" />
        </span>
        <br />
        <xsl:if test="$interval = 60">
          <xsl:text>&#160;&#160;&#160;&#160;</xsl:text>
        </xsl:if>
        <xsl:if test="$interval = 30">
          <xsl:text>&#160;</xsl:text>
        </xsl:if>
        <xsl:call-template name="time-text">
          <xsl:with-param name="counter" select="0" />
        </xsl:call-template>
      </span>
    </td>
  </xsl:template>
  <xsl:template name="time-text">
    <xsl:param name="counter" />
    <xsl:if test="number($counter) &lt; number($intervalAmount)">
      <xsl:choose>
        <xsl:when test="$amPmMode = 'true'">
          <span class="timeSlot12"
                style="{concat('margin-right:', 18 + number($counter = 0)*3, 'px;',
                  			'margin-left:', number($counter = 0)*number($interval = 30)*5 + number($counter = 0)*number($interval = 60)*10, 'px;')}">
            <xsl:value-of select="tag:getTime($tag, $counter)" />
          </span>
        </xsl:when>
        <xsl:when test="starts-with($locale, 'ar') or starts-with($locale, 'iw') or starts-with($locale, 'he')">
        <!-- Adding RLM marker before each time to be treated as RTL character so that the numbers are ordered correctly from right 
             to left.--> 
          <span class="timeSlot"
          style="{concat('margin-left:', 36 + number($counter = 0)*3, 'px;',
                  	'margin-right:', number($counter = 0)*number($interval = 30)*5 + number($counter = 0)*number($interval = 60)*10, 'px;')}">
            <xsl:value-of select="concat('&#x200F;', tag:getTime($tag, $counter))" />
          </span>
        </xsl:when>
        <xsl:otherwise>
          <span class="timeSlot"
            style="{concat('margin-right:', 36 + number($counter = 0)*3, 'px;',
                  	'margin-left:', number($counter = 0)*number($interval = 30)*5 + number($counter = 0)*number($interval = 60)*10, 'px;')}">
            <xsl:value-of select="tag:getTime($tag, $counter)" />
          </span>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:call-template name="time-text">
        <xsl:with-param name="counter" select="number($counter) + 1" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>