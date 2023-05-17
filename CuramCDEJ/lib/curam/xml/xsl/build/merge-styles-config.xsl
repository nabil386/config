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
    xmlns:sc="http://www.curamsoftware.com/curam/util/client/style-config">

  <xsl:output method="xml" indent="yes"/>

  <!-- 
  The "wrapper" contains a number of "styles" elements, one for each component
  that defined a style configuration. The "styles" elements are ordered with
  the lowest priority configuration first, so styles are ignored if there is
  a following style with the same name.
  -->
  <xsl:template match="/wrapper">
    <sc:styles
        xmlns:sc="http://www.curamsoftware.com/curam/util/client/style-config">
      <xsl:copy-of select="//sc:style[not(following::sc:style/@name = @name)]"/>
    </sc:styles>
  </xsl:template>

</xsl:stylesheet>
