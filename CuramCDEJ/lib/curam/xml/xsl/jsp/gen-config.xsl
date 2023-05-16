<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2002, 2021 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Interactive
Technology Design, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.

$Id$
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:curam="http://www.curamsoftware.com/curam"
                xmlns:jsp="http://java.sun.com/JSP/Page">
  <xsl:output method="xml" indent="no" omit-xml-declaration="no" />

  <xsl:param name="static-content-server-url"/>

  <xsl:template match="/">
    <jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:curam="http://www.curamsoftware.com/curam" version="2.0">
      <table class="page-header" summary="">
        <tbody>
          <tr>
            <td class="page-header-left"></td>
            <td class="page-header-right">
              <div class="menu">
                <xsl:apply-templates select="APP_CONFIG/APPLICATION_MENU/LINK" />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </jsp:root>
  </xsl:template>

  <xsl:template match="LINK">
    <curam:link title="{concat('$$', @LABEL)}" titleProp="{@LABEL_PROP}" xsl:use-attribute-sets="onmouse">
      <xsl:attribute name="dataTestidComponentPrefix">
        <xsl:text>link</xsl:text>
      </xsl:attribute>
      <xsl:if test="@ACCESSKEY">
        <xsl:attribute name="accesskey">
          <xsl:value-of select="concat('$$', @ACCESSKEY)"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="@HOME_PAGE='true'">
          <xsl:attribute name="homePage">true</xsl:attribute>
        </xsl:when>
        <xsl:when test="@LOGOUT_PAGE='true'">
          <xsl:attribute name="uri">../logout.jsp</xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="uri">
            <xsl:value-of select="concat(@PAGE_ID, 'Page.do')"/>
          </xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <img id="{concat('bullet', position())}" alt="{concat('$$', @LABEL)}"
           src="/themes/classic/images/header/bullet.gif"
           xsl:use-attribute-sets="onmouse"/>
    </curam:link>
  </xsl:template>

  <xsl:attribute-set name="onmouse">
    <xsl:attribute name="onmouseover">
      <xsl:text>swapImage('bullet</xsl:text>
      <xsl:value-of select="position()"/>
      <xsl:text>','</xsl:text>
      <xsl:value-of select="$static-content-server-url"/>
      <xsl:text>/themes/classic/images/header/bullet_hover.gif')</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="onmouseout">
      <xsl:text>swapImage('bullet</xsl:text>
      <xsl:value-of select="position()"/>
      <xsl:text>','</xsl:text>
      <xsl:value-of select="$static-content-server-url"/>
      <xsl:text>/themes/classic/images/header/bullet.gif')</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>

</xsl:stylesheet>