<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2003-2004 Curam Software Ltd.
All rights reserved.
 
This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <!--
  The attributes applied to column definitions for clusters.
  -->
  <xsl:attribute-set name="cluster-column-field-attributes">
    <xsl:attribute name="width">
      <xsl:value-of select="concat(100 - $label-width, '%')" />
    </xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="cluster-column-label-attributes">
    <xsl:attribute name="width">
      <xsl:value-of select="concat($label-width, '%')" />
    </xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="cluster-column-field-only-attributes">
    <xsl:attribute name="class">
      <xsl:value-of select="$cluster-field-col-no-label-class" />
    </xsl:attribute>
    <xsl:attribute name="width">
      <xsl:value-of select="'100%'" />
    </xsl:attribute>
  </xsl:attribute-set>

  <!--
  The class attribute value applied to the main div containing an action
  set. Action sets will only appear in multi-clusters.
  -->
  <xsl:param name="action-set-class" select="'ac_cluster'" />

</xsl:stylesheet>