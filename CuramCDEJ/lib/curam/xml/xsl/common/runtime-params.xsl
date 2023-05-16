<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2004 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <!--
  The path prefix for static data URLs (e.g., images, CSS files, etc.).
  -->
  <xsl:param name="static-content-server-url" />

  <!--
  The locale (a java.util.Locale object) for this transformation.
  -->
  <xsl:param name="locale" />

  <!--
  Double quote constant.
  -->
  <xsl:variable name="dquot">"</xsl:variable>

  <!--
  Single quote constant.
  -->
  <xsl:variable name="squot">'</xsl:variable>
</xsl:stylesheet>