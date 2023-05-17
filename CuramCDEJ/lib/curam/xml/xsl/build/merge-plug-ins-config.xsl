<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2008 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:pc="http://www.curamsoftware.com/curam/util/common/plug-in-config">

  <xsl:output method="xml" indent="yes"/>

  <!--
  The configurations appear as separate "pc:plug-ins" collections within a
  single "wrapper" element. Each "pc:plug-ins" collection contains "pc:plug-in"
  elements to configure the plug-ins for a unique "purpose". The "pc:plug-ins"
  collections are presented in ascending order of component priority.
  THE CONFIGURATION FOR THE HIGHEST PRIORITY COMPONENT APPEARS LAST IN THE LIST.
  The merging ensure that the highest priority "purpose" is preserved and are
  unique.
  -->
  <xsl:template match="/wrapper">
    <pc:plug-ins
      xmlns:pc="http://www.curamsoftware.com/curam/util/common/plug-in-config">
      <xsl:copy-of
        select="//pc:plug-in[not(following::pc:plug-in/@purpose = @purpose)]"/>
    </pc:plug-ins>
  </xsl:template>

</xsl:stylesheet>
