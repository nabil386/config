<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2003-2004, 2007 Curam Software Ltd.
All rights reserved.
 
This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:import href="ui-heading.xsl" />
  <xsl:import href="ui-action-set.xsl" />

  <!--
  NOTE: While these templates do not have a "match" attribute,
  there will still be a context node if the calling template
  had a context node. This context node is automatically passed
  through to templates called from this template.
  -->

  <!--
  Generate a section. A section is a major page division such as a
  cluster or list that has a 'top' and/or 'bottom' action set, a
  heading, and a main body formatted using tables.

  $title       - The title of the section.
  $description - The description of the section.
  $type        - The type of section: 'cluster', 'list', or 'multi-cluster'.
  -->
  <xsl:template name="gen-section">

    <xsl:param name="title" select="''"/>
    <xsl:param name="description" select="''"/>
    <xsl:param name="type" />

    <div>
      <xsl:variable name="has-title" select="not(normalize-space($title) = '')"/>
      <xsl:variable name="has-desc" select="not(normalize-space($description) = '')"/>

      <xsl:attribute name="class">

        <xsl:choose>
          <xsl:when test="$type='multi-cluster' or $type='cluster'">
            <xsl:text>cluster</xsl:text>
            <xsl:if test="$has-title or $has-desc">
              <xsl:text> cluster-with-header</xsl:text>
            </xsl:if>
            <xsl:text> label-field</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$type"/>
            <xsl:if test="$has-title or $has-desc">
              <xsl:text> list-with-header</xsl:text>
            </xsl:if>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>

      <!-- Top action set. -->
      <xsl:call-template name="gen-action-set">
        <xsl:with-param name="position" select="'top'" />
        <xsl:with-param name="context" select="$type" />
      </xsl:call-template>

      <!-- Heading. -->
      <xsl:call-template name="gen-heading">
        <xsl:with-param name="context" select="$type" />
        <xsl:with-param name="title" select="$title" />
        <xsl:with-param name="description" select="$description" />
      </xsl:call-template>

      <!-- Body. -->
      <xsl:choose>
        <xsl:when test="$type = 'cluster'">
          <xsl:call-template name="gen-cluster-body">
            <xsl:with-param name="title" select="$title" />
            <xsl:with-param name="description" select="$description" />
          </xsl:call-template>
        </xsl:when>
        <xsl:when test="$type = 'multi-cluster'">
          <xsl:call-template name="gen-multi-cluster-body">
            <xsl:with-param name="title" select="$title" />
            <xsl:with-param name="description" select="$description" />
          </xsl:call-template>
        </xsl:when>
        <xsl:when test="$type = 'list'">
          <xsl:call-template name="gen-list-body">
            <xsl:with-param name="title" select="$title" />
            <xsl:with-param name="description" select="$description" />
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:message terminate="yes">
            <xsl:text>ERROR: Unknown section type.</xsl:text>
          </xsl:message>
        </xsl:otherwise>
      </xsl:choose>

      <!-- Bottom action set. -->
      <xsl:call-template name="gen-action-set">
        <xsl:with-param name="position" select="'bottom'" />
        <xsl:with-param name="context" select="$type" />
      </xsl:call-template>
    </div>

  </xsl:template>

</xsl:stylesheet>