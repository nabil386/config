<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2005 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:dc="http://www.curamsoftware.com/curam/util/common/domain-config">

  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/wrapper">
    <dc:domains
        xmlns:dc="http://www.curamsoftware.com/curam/util/common/domain-config">
      <xsl:copy-of
          select="//dc:domain[not(following::dc:domain/@name = @name)]"/>
    </dc:domains>
  </xsl:template>

</xsl:stylesheet>
