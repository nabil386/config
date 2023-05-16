<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  Copyright © 2004 Curam Software Ltd.
  All rights reserved.
 
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:variable name="image-config" select="$rules-config/RULES-CONFIG/CONFIG[@ID = $rules-config/RULES-CONFIG/@DEFAULT]"/>
  <xsl:param name="legislation-alt-text"/>
  <xsl:param name="success-alt-text"/>
  <xsl:param name="failure-alt-text"/>
  <xsl:template match="decision">
    <div class="summary">
      <xsl:apply-templates select="rule-node"/>
    </div>
  </xsl:template>
  <xsl:template match="rule-node">
    <xsl:if test="@result='F' and @summary='true'">
      <xsl:apply-templates select="." mode="failed"/>
    </xsl:if>
    <xsl:apply-templates select="rule-node"/>
  </xsl:template>
  <xsl:template match="rule-node" mode="failed">
    <xsl:variable name="node-type" select="@type"/>
    <xsl:variable name="edit-page" select="$rules-config/RULES-CONFIG/CONFIG[@ID = $config-id]/TYPE[@NAME = $node-type]/@EDIT-PAGE"/>
    <xsl:variable name="link-text" select="concat($edit-page, 'Page.do?id=', @id, '&amp;rulesetId=',  $ruleset-id, '&amp;', $o3rpu, '&amp;', $decision-id-param)"/>
    <xsl:variable name="image-location" select="concat($static-content-server-url, '/',  $image-config/TYPE[@NAME = $node-type]/@FAILURE-ICON)"/>
    <xsl:variable name="leg-link">
      <xsl:choose>
        <xsl:when test="@LEGISLATION_BASE or @LEGISLATION_ID">
          <xsl:choose>
            <xsl:when test="@LEGISLATION_BASE and @legislation-link">
              <xsl:value-of select="concat(@LEGISLATION_BASE, '/', @legislation-link)"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="@legislation-link"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="'null'"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <div>
    <xsl:choose>
      <xsl:when test="$edit-page">
        <a href="{$link-text}">
          <img src="{$image-location}" alt="{concat(@ALT_TEXT, ' ', $failure-alt-text)}"/>
        </a>
      </xsl:when>
      <xsl:otherwise>
        <img src="{$image-location}" alt="{concat(@ALT_TEXT, ' ', $failure-alt-text)}"/>
      </xsl:otherwise>
    </xsl:choose>
      
    <xsl:if test="$leg-link != 'null'">
      <a href="{$leg-link}">
        <img src="{concat($static-content-server-url, '/themes/classic/images/rules/legislation-link.gif')}" alt="{$legislation-alt-text}"/>
      </a>
    </xsl:if>
    <xsl:choose>
      <xsl:when test="$edit-page and $hyperlink-text = 'true'">
        <a href="{$link-text}">
          <xsl:value-of select="@text"/>
        </a>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="@text"/>
      </xsl:otherwise>
    </xsl:choose>
    </div>
  </xsl:template>
</xsl:stylesheet>
