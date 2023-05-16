<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright 2005-2021 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Interactive
Technology Design, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.

$Id$
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:domain="http://xml.apache.org/xalan/java/curam.util.common.util.JavaScriptEscaper"
                xmlns:xalan="http://xml.apache.org/xalan"
                extension-element-prefixes="xalan"
                exclude-result-prefixes="domain">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:param name="static-content-server-url"/>
  <xsl:param name="player-mode"/>
  <xsl:param name="navigator-hidden" select="'false'"/>
  <xsl:param name="confirm-quit"/>
  <xsl:param name="quit-question"/>

  <xsl:template match="agenda">
    <xsl:variable name="numPages" select="count(page-flow/section/page|page-flow/summary)"/>
    <xsl:variable name="numParams" select="count(parameters/parameter)"/>
    <xsl:variable name="isSummary" select="count(page-flow/summary)"/>
    <script type="text/javascript">
      <xsl:text>require(["wizard"], function(wizard) {</xsl:text>
       <xsl:text>dojo.addOnLoad(function(){</xsl:text>
      <xsl:text>var wizardNavigator = new WizardNavigator('</xsl:text>
      <xsl:apply-templates select="exit-page"/>
      <xsl:text>', </xsl:text>
      <xsl:choose>
        <xsl:when test="not(page-flow/section/page[@initial='true'])">
          <xsl:text>0</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="page-flow/section/page[@initial='true'][1]" mode="initial"/>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:text>, '</xsl:text>
      <xsl:value-of select="$player-mode"/>
      <xsl:text>');</xsl:text>
      <xsl:text>&#xa;wizardNavigator.setPageList([</xsl:text>
      <xsl:apply-templates select="page-flow/section/page" mode="page-names"/>
      <xsl:text>]);</xsl:text>
      <xsl:text>&#xa;wizardNavigator.setPageTitleList([</xsl:text>
      <xsl:apply-templates select="page-flow/section/page" mode="page-titles"/>
      <xsl:text>]);</xsl:text>
      <xsl:text>&#xa;wizardNavigator.setSectionTitleList([</xsl:text>
      <xsl:apply-templates select="page-flow/section" mode="page-titles"/>
      <xsl:text>]);</xsl:text>
      <xsl:if test="$isSummary">
        <xsl:text>&#xa;wizardNavigator.setSummary(</xsl:text>
        <xsl:apply-templates select="page-flow/summary" mode="name-and-title"/>
      <xsl:text>);</xsl:text>
      </xsl:if>
      <xsl:if test="$navigator-hidden = 'false'">
        <xsl:text>&#xa;wizardNavigator.setVisible();</xsl:text>
      </xsl:if>
      <xsl:if test="$navigator-hidden = 'true'">
        <xsl:text>&#xa;wizardNavigator.setClaimantScheme();</xsl:text>
      </xsl:if>
      <xsl:if test="$confirm-quit = 'true'">
        <xsl:text>&#xa;wizardNavigator.setQuitConfirm(&apos;</xsl:text>
        <xsl:value-of select="domain:escapeText($quit-question)"/>
        <xsl:text>&apos;);</xsl:text>
      </xsl:if>
      <xsl:apply-templates select="parameters/parameter"/>
      <xsl:apply-templates select="page-flow/section/page[@disableback='true']" mode="disable-back"/>
      <xsl:apply-templates select="page-flow/section/page[@disableback='dynamic']" mode="disable-back"/>
      <xsl:apply-templates select="page-flow/section/page[@disableforward='true']" mode="disable-forward"/>
      <xsl:apply-templates select="page-flow/section/page[@disableforward='dynamic']" mode="disable-forward"/>
      <xsl:apply-templates select="page-flow/section/page[@submitonnext='true']" mode="submit-on-next"/>
      <xsl:text>&#xa;dojo.addOnLoad(function(){wizardNavigator.initContent();});</xsl:text>
      <xsl:text>  });</xsl:text>
      <xsl:text>});</xsl:text>
    </script>
    <div id="wizardItems" class="itemsList" style="overflow-y:scroll;height:80vh;" tabindex="-1">
      <xsl:if test="$navigator-hidden = 'true'">
        <xsl:attribute name="aria-hidden">true</xsl:attribute>
      </xsl:if>
      <ul id="fullList" tabindex="-1">
        <xsl:if test="$navigator-hidden = 'true'">
          <xsl:attribute name="role">presentation</xsl:attribute>
        </xsl:if>
        <xsl:if test="$navigator-hidden = 'false'">
          <xsl:attribute name="tabIndex">0</xsl:attribute>
        </xsl:if>
        <xsl:apply-templates select="page-flow/section|page-flow/section/page" mode="html"/>
      </ul>
    </div>
    <xsl:apply-templates select="page-flow/summary" mode="html"/>
  </xsl:template>

  <xsl:template match="page" mode="initial">
    <xsl:value-of select="count(preceding::page)"/>
  </xsl:template>

  <xsl:template match="page" mode="disable-back">
    <!-- wizardNavigator.addBackDisable('pageID'); -->
    <xsl:text>&#xa;wizardNavigator.addDisableBack(&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@id)"/>
    <xsl:text>&apos;,&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@disableback)"/>
    <xsl:text>&apos;);</xsl:text>
  </xsl:template>

  <xsl:template match="page" mode="disable-forward">
    <!-- wizardNavigator.addForwardDisable('pageID'); -->
    <xsl:text>&#xa;wizardNavigator.addDisableForward(&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@id)"/>
    <xsl:text>&apos;,&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@disableforward)"/>
    <xsl:text>&apos;);</xsl:text>
  </xsl:template>

  <xsl:template match="page" mode="submit-on-next">
    <!-- wizardNavigator.addFormRef('pageID'); -->
    <xsl:text>&#xa;wizardNavigator.addFormRef(&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@id)"/>
    <xsl:text>&apos;);</xsl:text>
  </xsl:template>

  <xsl:template match="page" mode="page-names">
    <xsl:if test="position() &gt; 1">
      <xsl:text>, </xsl:text>
    </xsl:if>
    <xsl:text>&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@id)"/>
    <xsl:text>&apos;</xsl:text>
  </xsl:template>

  <xsl:template match="page|section" mode="page-titles">
    <xsl:if test="position() &gt; 1">
      <xsl:text>, </xsl:text>
    </xsl:if>
    <xsl:text>&apos;</xsl:text>
    <!--description is already escaped in WizardFilter.java-->
    <xsl:value-of select="@description"/>
    <xsl:text>&apos;</xsl:text>
  </xsl:template>

  <xsl:template match="summary" mode="name-and-title">
    <xsl:text>&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@id)"/>
    <xsl:text>&apos;</xsl:text>
    <xsl:text>, </xsl:text>
    <xsl:text>&apos;</xsl:text>
    <xsl:value-of select="@description"/>
    <xsl:text>&apos;</xsl:text>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="count(ancestor::page-flow/descendant::page)"/>
    <xsl:if test="@close-on-submit and @close-on-submit = 'true'">
      <xsl:text>, true</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="parameter">
    <!-- wizardNavigator.addParam('param1', 'value1'); -->
    <xsl:text>&#xa;wizardNavigator.addParam(&apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@name)"/>
    <xsl:text>&apos;, &apos;</xsl:text>
    <xsl:value-of select="domain:escapeText(@value)"/>
    <xsl:text>&apos;);</xsl:text>
  </xsl:template>

  <xsl:template match="page" mode="html">
    <xsl:variable name="sections" select="count(preceding::section)"/>
    <li class="item" id="{concat('nav_',position()-2-$sections)}" role="heading">
      <xsl:if test="$navigator-hidden = 'true'">
        <xsl:attribute name="aria-hidden">true</xsl:attribute>
      </xsl:if>
      <xsl:attribute name="aria-label"><xsl:value-of select="@description"/></xsl:attribute>
      <xsl:if test="string-length(@status) &gt; 0">
        <img src="{concat($static-content-server-url,'/',@status)}"/>
      </xsl:if>
    </li>
  </xsl:template>

  <xsl:template match="summary" mode="html">
    <div class="summaryWrap" id="swrap">
      <xsl:if test="$navigator-hidden = 'true'">
        <xsl:attribute name="aria-hidden">true</xsl:attribute>
      </xsl:if>
      <div class="summaryDiv" id="sum_0" aria-label="{@description}">
        <xsl:if test="string-length(@status) &gt; 0">
          <img src="{concat($static-content-server-url,'/',@status)}"/>
        </xsl:if>
        <a class="summaryLink" href="#"></a>
      </div>
    </div>
  </xsl:template>

  <xsl:template match="section" mode="html">
    <xsl:variable name="pages" select="count(preceding::page)"/>
    <xsl:if test="not(@visible='false')">
    <xsl:variable name="image-present" select="string-length(@status) &gt; 0"/>
      <li class="sectionNormal" id="{concat('s_',position()-1-$pages)}">
        <xsl:if test="$image-present='true'">
          <xsl:attribute name="style">margin-bottom:1px;</xsl:attribute>
        </xsl:if>
        <div class="sectionText">
          <xsl:attribute name="style">
            <xsl:if test="$image-present='true'">
              width:92%;
            </xsl:if>
            overflow:hidden;float:left;text-align:left;padding-left:4px;
        </xsl:attribute>
        <xsl:if test="$image-present='true'">
          <img src="{concat($static-content-server-url,'/',@status)}"/>
        </xsl:if>
        </div>
        <img id="si_{position()-1-$pages}" class="expander" src="../CDEJ/themes/classic/images/wizard/contract_16x16.gif"/>
      </li>
    </xsl:if>
  </xsl:template>

  <xsl:template match="exit-page">
    <xsl:value-of select="domain:escapeText(@id)"/>
    <xsl:text>Page.do</xsl:text>
    <xsl:apply-templates select="parameters/parameter" mode="exit"/>
  </xsl:template>

  <xsl:template match="parameter" mode="exit">
    <xsl:if test="position() = 1">
      <xsl:text>?</xsl:text>
    </xsl:if>
    <xsl:value-of select="domain:escapeText(@name)"/>
    <xsl:text>=</xsl:text>
    <xsl:value-of select="domain:escapeText(@value)"/>
    <xsl:if test="not(position() = last())">
      <xsl:text>&amp;</xsl:text>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>