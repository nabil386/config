<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2005-2008 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Interactive
Technology Design, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:c="http://java.sun.com/jsp/jstl/core">

  <xsl:strip-space elements="*" />
  
  <xsl:template match="PAGE" mode="frameset-wizard">
    <xsl:variable name="page-id" select="@PAGE_ID" />
    <xsl:variable name="customs" select="INFO" />
    <xsl:variable name="config">
      <xsl:choose>
        <xsl:when test="current()//FIELD[@DOMAIN='AGENDA_XML'][1]/@CONFIG">
           <xsl:value-of select="current()//FIELD[@DOMAIN='AGENDA_XML'][1]/@CONFIG"/>
        </xsl:when>
        <xsl:otherwise><xsl:text>none</xsl:text></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:for-each select="document('xim:wizard', /)">
      <chunk type="jsp-frameset" name="{$page-id}">
        <xsl:apply-templates mode="frameset">
          <xsl:with-param name="page-id" select="$page-id" />
          <xsl:with-param name="customs" select="$customs" />
          <xsl:with-param name="config" select="$config" />
        </xsl:apply-templates>
      </chunk>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template match="PAGE" mode="frameset-treecontrol">
    <xsl:variable name="page-id" select="@PAGE_ID" />
    <xsl:variable name="customs" select="INFO" />
    
    <xsl:for-each select="document('xim:treecontrol', /)">
      <chunk type="jsp-frameset" name="{$page-id}">
        <xsl:apply-templates mode="frameset">
          <xsl:with-param name="page-id" select="$page-id" />
          <xsl:with-param name="customs" select="$customs" />
        </xsl:apply-templates>
      </chunk>
    </xsl:for-each>
  </xsl:template>

  <!-- This is the identity transformation. -->
  <xsl:template match="node()" mode="frameset">
    <xsl:param name="page-id" />
    <xsl:param name="customs" />
    <xsl:param name="config" />
    
    <xsl:choose>
    <xsl:when test="name()='frameset' and @class='outer-frame'">
      <xsl:element name="frameset">
        <xsl:attribute name="id">
          <xsl:value-of select="concat('Curam_', $page-id)"/>
        </xsl:attribute>
        <xsl:apply-templates select="@*" mode="frameset">
	        <xsl:with-param name="page-id" select="$page-id" />
        </xsl:apply-templates>
        <xsl:apply-templates select="node()" mode="frameset">
          <xsl:with-param name="page-id" select="$page-id" />
          <xsl:with-param name="customs" select="$customs" />
          <xsl:with-param name="config" select="$config" />
        </xsl:apply-templates>
      </xsl:element>
    </xsl:when>
    <xsl:otherwise>
      <xsl:copy>
        <xsl:apply-templates select="@*" mode="frameset">
          <xsl:with-param name="page-id" select="$page-id" />
        </xsl:apply-templates>
        <xsl:apply-templates select="node()" mode="frameset">
          <xsl:with-param name="page-id" select="$page-id" />
          <xsl:with-param name="customs" select="$customs" />
          <xsl:with-param name="config" select="$config" />
        </xsl:apply-templates>
      </xsl:copy>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="@*" mode="frameset">
    <xsl:copy />
  </xsl:template>
  
  <xsl:template match="c:url[@var = 'navUrl']/@value" mode="frameset">
    <xsl:param name="page-id" />

    <xsl:attribute name="value">
      <xsl:value-of select="concat($page-id, 'Frame.do')" />
    </xsl:attribute>
  </xsl:template>
  
 <xsl:template match="c:url[@var = 'headerUrl']|c:url[@var = 'buttonsUrl']" mode="frameset">
    <xsl:param name="config"/>
    
    <c:url value="{current()/@value}" var="{current()/@var}">
      <c:param name="configKey">
        <xsl:attribute name="value">
          <xsl:choose>
           <xsl:when test="$config and not($config = 'none')"><xsl:value-of select="$config"/></xsl:when>
           <xsl:otherwise><xsl:text>default</xsl:text></xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
      </c:param>
    </c:url>
  </xsl:template>
  
  <xsl:template match="c:set" mode="frameset">
    <xsl:param name="customs"/>
    <xsl:choose>
      <xsl:when test="$customs/OVERRIDEN[@PARAM = current()/@var]">
        <c:set var="{current()/@var}" value="{$customs/OVERRIDEN[@PARAM = current()/@var]/@VALUE}"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy-of select="."/>
      </xsl:otherwise>
    </xsl:choose>
 </xsl:template>
  
</xsl:stylesheet>
