<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2012,2015. All rights reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/02/xpath-functions">
	<xsl:template match="Include[RelativePath]">
		<!-- look up the document with the relative path and copy its contents -->
		<xsl:variable name="relativePath">
			<xsl:value-of select="translate(./RelativePath/@value, '\', '/')"/>
		</xsl:variable>
		<!--
		<xsl:message terminate="no">Processing relative path: <xsl:value-of select="$relativePath"/></xsl:message>
		-->

		<xsl:if test="not(document($relativePath, .))">
			<xsl:message terminate="yes">Could not find file <xsl:value-of select="$relativePath"/>
				included in rule set.</xsl:message>
		</xsl:if>
		
		<xsl:text>
</xsl:text>
		<xsl:comment>Start inclusion of <xsl:value-of select="$relativePath"/></xsl:comment>
		<xsl:text>
</xsl:text>
		<xsl:choose>
			<xsl:when test="document($relativePath, .)/RuleSet">
				<!-- Root node is a RuleSet - include its contents-->
				<xsl:apply-templates select="document($relativePath, .)/RuleSet/*"/>
			</xsl:when>
			<xsl:otherwise>
				<!-- Root node is not a RuleSet - include it directly -->
				<xsl:apply-templates select="document($relativePath, .)"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>
</xsl:text>
		<xsl:comment>End inclusion of <xsl:value-of select="$relativePath"/></xsl:comment>
		<xsl:text>
</xsl:text>
	</xsl:template>

	<xsl:template match="@*|node()|text()">
		<!--identity for all other nodes-->
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
