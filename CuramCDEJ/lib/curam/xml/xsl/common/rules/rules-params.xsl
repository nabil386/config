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
  <xsl:include href="../runtime-params.xsl"/>
  <xsl:param name="rules-config"/>
  <xsl:param name="config-id"/>
  <xsl:param name="decision-id-param"/>
  <xsl:param name="o3rpu"/>
  <xsl:param name="legislation-base"/>
  <xsl:variable name="hyperlink-text" select="$rules-config/RULES-CONFIG/CONFIG[@ID = $config-id]/@HYPERLINK-TEXT"/>
  <xsl:variable name="ruleset-id" select="/decision/*[1]/@id"/>
</xsl:stylesheet>
