<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2003-2004, 2007 Curam Software Ltd.
All rights reserved.
 
This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"                
                version="1.0"
                xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils"
                exclude-result-prefixes="bidi-utils">

  <xsl:import href="ui-param.xsl" />

  <!--
  NOTE: While these templates do not have a "match" attribute,
  there will still be a context node if the calling template
  had a context node. This context node is automatically passed
  through to templates called from this template.
  -->

  <!--
  Generate a heading for a cluster or list, etc. If the title and description
  are both empty, no heading will be generated.

  $context     The what the heading is for: 'cluster', 'list', or
                 'multi-cluster'.
  $title       The title text.
  $description The description text.
  -->
  <xsl:template name="gen-heading">
    <xsl:param name="title" select="''" />
    <xsl:param name="description" select="''" />
    <xsl:param name="context" select="''" />

    <xsl:variable name="has-title" select="not(normalize-space($title) = '')"/>
    <xsl:variable name="has-desc" select="not(normalize-space($description) = '')"/>

    <xsl:if test="$has-title or $has-desc">
        <xsl:if test="$has-title">
         <div class="header-wrapper">
          <h2 class="collapse">
            <span class="collapse-title">
              <xsl:attribute name="dir">
                <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(title)"/>
              </xsl:attribute>		                                                                                                       	                            
              <xsl:value-of select="$title"/>
            </span>
            <span><xsl:text> </xsl:text></span>
          </h2>
         </div>
        </xsl:if>
        <xsl:if test="$has-desc">
          <p>
              <xsl:attribute name="dir">
                <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(description)"/>
              </xsl:attribute>                                                                                                 	                
            <xsl:value-of select="$description"/>
          </p>
        </xsl:if>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>