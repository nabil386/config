<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2004-2005 Curam Software Ltd.
All rights reserved.
 
This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:tag="http://xml.apache.org/xalan/java/curam.omega3.taglib.widget.DynamicMenuTag"
  xmlns:resources="http://xml.apache.org/xalan/java/curam.omega3.util.CDEJResources"
  exclude-result-prefixes="resources tag" 
  version="1.0">

  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
  <xsl:param name="menu-config" />
  <xsl:variable name="config" select="$menu-config/DYNAMIC_MENU_CONFIG" />
  <xsl:variable name="limit-to" select="$config/MAX_LENGTH"/>
  <xsl:param name="locale"/>
  <xsl:param name="static-content-server-url"/>

  <xsl:template match="DYNAMIC_MENU">
    <xsl:variable name="num-links" select="count(LINK)"/>
    <table class="breadcrumb">
      <tbody>
        <tr>
          <xsl:for-each select="LINK">
            <xsl:apply-templates select="@TYPE"/>
            <td style="padding-right:3px">
              <a>
                <xsl:attribute name="href">         
                  <xsl:value-of select="@PAGE_ID"/>
                  <xsl:text>Page.do</xsl:text>              
                  <xsl:apply-templates select="PARAMETER"/>
                </xsl:attribute>
                <xsl:call-template name="textual-part">
                  <xsl:with-param name="unformatted"
                      select="tag:getLocalizedMessage(@DESC, $locale)"/>
                </xsl:call-template>            
              </a>
            </td>
            <xsl:if test="position() != last()">
              <td>
                <xsl:choose>
                  <xsl:when test="$config/SEPARATOR/@IMAGE">
                    <img src="{$static-content-server-url}/{$config/SEPARATOR/@IMAGE}" alt=""/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of
                      select="resources:getProperty($config/SEPARATOR/@TEXT)"/>
                    </xsl:otherwise>
                </xsl:choose>
              </td>
            </xsl:if>
          </xsl:for-each>
        </tr>
      </tbody>
    </table>
  </xsl:template>

  <xsl:template name="textual-part">
    <xsl:param name="unformatted"/>
    <xsl:choose>
      <xsl:when test="(($limit-to) and (string-length($unformatted) &gt; $limit-to))">
        <xsl:value-of select="concat(substring($unformatted, 0, $limit-to + 1),'...')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$unformatted"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="PARAMETER">
      <xsl:choose>
      <xsl:when test="position()=1">
        <xsl:text>?</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>&amp;</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:value-of select="@NAME"/>=<xsl:value-of select="@VALUE"/>
  </xsl:template>
  
  <xsl:template match="@TYPE">
    <xsl:variable name="currentType" select="$config/LINK[@TYPE = current()]" />
    <xsl:if test="$currentType">
      <td>
        <xsl:variable name="currentText"
                      select="resources:getProperty($currentType/@TEXT)"/>
        <img src="{$static-content-server-url}/{$currentType/@IMAGE}"
             alt="{$currentText}"/>
      </td>
    </xsl:if>
  </xsl:template> 
  
</xsl:stylesheet>
