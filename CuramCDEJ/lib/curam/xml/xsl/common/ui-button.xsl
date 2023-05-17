<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright © 2003 Curam Software Ltd.
  All rights reserved.
 
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <!--
    NOTE: While these templates do not have a "match" attribute,
    there will still be a context node if the calling template
    had a context node. This context node is automatically passed
    through to templates called from this template.
  -->

  <!--
    Generate a submit button.

    $name    The name of the submit button.
    $caption The button caption. This will be the alt-text if an
               image is provided.
    $image   The path to the button image (optional).
  -->
  <xsl:template name="gen-submit-button">
    <xsl:param name="name" />
    <xsl:param name="caption" />
    <xsl:param name="image" select="''" />

    <input name="{$name}" value="{$caption}">
      <xsl:choose>
        <xsl:when test="$image = ''">
          <xsl:attribute name="type">
            <xsl:text>submit</xsl:text>
          </xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="type">
            <xsl:text>image</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="alt">
            <xsl:value-of select="$caption" />
          </xsl:attribute>
          <xsl:attribute name="src">
            <xsl:value-of select="$image" />
          </xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
    </input>
  </xsl:template>

</xsl:stylesheet>