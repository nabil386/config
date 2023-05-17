<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  Copyright 2004 Curam Software Ltd.
  All rights reserved.
 
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="../common/rules/rules-params.xsl"/>
  <xsl:import href="../common/rules/static-rules.xsl"/>
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />

  <xsl:template match="rule-node">
    <xsl:if test="@result='F' and @summary='true' and @type='RULE'">
      <xsl:apply-templates select="." mode="failed"/>
    </xsl:if>
    <xsl:apply-templates select="rule-node"/>
  </xsl:template>
</xsl:stylesheet>
