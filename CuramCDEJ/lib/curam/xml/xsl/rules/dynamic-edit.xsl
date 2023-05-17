<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  Copyright 2004-2006 Curam Software Ltd.
  All rights reserved.
 
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="../common/rules/rules-params.xsl"/>
  <xsl:import href="../common/rules/dynamic-rules-params.xsl"/>
  <xsl:import href="../common/string-utils.xsl" />
  <xsl:strip-space elements="*"/>
  <xsl:output indent="no" omit-xml-declaration="yes" encoding="UTF-8"/>
  
  <xsl:variable name="legislation-base">
    <xsl:if test="RuleSet/*[1]/@legislationbase">
      <xsl:value-of select="string(/RuleSet/*[1]/@legislationbase)"/>
    </xsl:if>
  </xsl:variable>
  <xsl:variable name="ruleset-id" select="/RuleSet/*[1]/@id"/>

  <xsl:template match="RuleSet">
    <script type="text/javascript">
      <xsl:text disable-output-escaping="yes">//&lt;![CDATA[&#xa;</xsl:text>
      <xsl:text>checkAndGetSVGViewer();&#xa;</xsl:text>
      <xsl:text>var currentLocale = </xsl:text>
      <xsl:value-of select="concat($dquot, $locale, $dquot)"/>
      <xsl:text>;&#xa;</xsl:text>
      <xsl:call-template name="create-edit-page-lookup"/>
      <xsl:text>function setTree(windowWidth, svgDoc) {&#xa;</xsl:text>
      <xsl:text>hyperLinkText = </xsl:text>
      <xsl:choose>
        <xsl:when test="$hyperlink-text = 'true'">true</xsl:when>
        <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
      <xsl:text>;&#xa;</xsl:text>
      <xsl:text>var r = new RootTreeNode("__o3__SVGTreeNode"</xsl:text>
      <xsl:text>, windowWidth, svgDoc, new Array(), false, "</xsl:text>
      <xsl:value-of select="$o3rpu"/>
      <xsl:text>", "</xsl:text>
      <xsl:value-of select="$ruleset-id"/>
      <xsl:text>", "</xsl:text>
      <xsl:value-of select="$decision-id-param"/>
      <xsl:text>", null);&#xa;</xsl:text>
      <xsl:text>rootNodesHash[r.id] = r;&#xa;</xsl:text>
      <xsl:apply-templates/>
      <xsl:if test="$open-tree-node">
        <xsl:value-of select="concat('r.initialOpenNode(', $dquot, $open-tree-node, $dquot, ');')"/>
      </xsl:if>
      <xsl:text>return r;&#xa;</xsl:text>
      <xsl:text>}&#xa;</xsl:text>
      <xsl:value-of
             select="concat('emitSVG(', $squot,
                            'src=', $dquot, $static-content-server-url,
                            '/CDEJ/svg/tree.svg', $dquot,
                            ' name=', $dquot, '__o3__SVGTreeNode', $dquot,
                            ' id=', $dquot, '__o3__SVGTreeNode', $dquot,
                            ' height=', $dquot, $height, $dquot,
                            ' width=', $dquot, '100%', $dquot,
                            ' wmode=', $dquot, 'opaque', $dquot,
                            ' type=', $dquot, 'image/svg+xml', $dquot, $squot,
                            ');')" />
      <xsl:text disable-output-escaping="yes">//]]&gt;</xsl:text>
    </script>
  </xsl:template>
  <xsl:template match="Product | Assessment | SubRuleSet | ObjectiveGroup
                       | ObjectiveListGroup | Objective | RuleGroup
                       | RuleListGroup | Rule">
    <xsl:variable name="text-value">
      <xsl:choose>
        <xsl:when test="Label/RuleName/Text[@locale = string($locale)]/@value">
          <xsl:call-template name="replace-chars-in-string">
            <xsl:with-param name="string-in" 
              select="Label/RuleName/Text[@locale = string($locale)]/@value"/>
            <xsl:with-param name="chars-in" select="'&quot;'"/>
            <xsl:with-param name="chars-out" select="'\&quot;'"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="replace-chars-in-string">
            <xsl:with-param name="string-in" 
              select="Label/RuleName/Text[position()=1]/@value"/>
            <xsl:with-param name="chars-in" select="'&quot;'"/>
            <xsl:with-param name="chars-out" select="'\&quot;'"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:call-template name="output-rule">
      <xsl:with-param name="rule-type" select="name()"/>
      <xsl:with-param name="text" select="$text-value"/>
      <xsl:with-param name="id" select="@id"/>
    </xsl:call-template>
  </xsl:template>
  <xsl:template match="SubRuleSetLink">
    <xsl:call-template name="output-rule">
      <xsl:with-param name="rule-type" select="name()"/>
      <xsl:with-param name="text" select="@name"/>
      <xsl:with-param name="id" select="@id"/>
    </xsl:call-template>
  </xsl:template>
  <xsl:template match="DataItemAssignment">
    <xsl:call-template name="output-rule">
      <xsl:with-param name="rule-type" select="name()"/>
      <xsl:with-param name="text" select="@dataitem"/>
      <xsl:with-param name="id" select="@id"/>
    </xsl:call-template>
  </xsl:template>
  <xsl:template name="output-rule">
    <xsl:param name="rule-type"/>
    <xsl:param name="text"/>
    <xsl:param name="id"/>
    <xsl:variable name="parent-id" select="../@id"/>
    <xsl:variable name="image-name">
      <xsl:choose>
        <xsl:when test="@result = 'F'">
          <xsl:text>eF</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>eS</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="leg-link">
    <xsl:choose>
      <xsl:when test="not(./Label/@legislationlink)
                      or ./Label/@legislationlink = ''">
        <xsl:value-of select="'null'"/>
      </xsl:when>
      <xsl:when test="starts-with(./Label/@legislationlink, 'http')
                      or starts-with(./Label/@legislationlink, 'ftp')">
        <xsl:value-of
          select="concat($dquot, ./Label/@legislationlink, $dquot)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of
          select="concat($dquot, $legislation-base, ./Label/@legislationlink,
                         $dquot)"/>
      </xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
      <xsl:text>r.a("</xsl:text>
      <xsl:value-of select="$id"/>
      <xsl:text>","</xsl:text>
      <xsl:choose>
        <xsl:when test="$parent-id">
          <xsl:value-of select="$parent-id"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>__o3__SVGTreeNode</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:text>","</xsl:text>
      <xsl:value-of select="$text"/>
      <xsl:text>","</xsl:text>
      <xsl:value-of select="$image-name"/>
      <xsl:text>","","</xsl:text>
      <xsl:value-of select="$rule-type"/>
      <xsl:text>",</xsl:text>
      <xsl:value-of select="$leg-link"/>
      <xsl:text>);</xsl:text>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template name="create-edit-page-lookup">
    <xsl:text>var editPages=new Array();</xsl:text>
    <xsl:for-each select="$rules-config/RULES-CONFIG/CONFIG[@ID = $config-id]">
      <xsl:for-each select="TYPE">
        <xsl:text>editPages["</xsl:text>
        <xsl:value-of select="@NAME"/>
        <xsl:text>"]="</xsl:text>
        <xsl:value-of select="@EDIT-PAGE"/>
        <xsl:text>";</xsl:text>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>