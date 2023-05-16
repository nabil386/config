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

  <xsl:import href="ui-action-control.xsl" />

  <!--
  NOTE: While these templates do not have a "match" attribute,
  there will still be a context node if the calling template
  had a context node. This context node is automatically passed
  through to templates called from this template.
  -->

  <!--
  Generate an action set. This template can be overridden to
  prevent any content from being generated, to wrap it in other
  content, or to change the context element.

  By default, the $position and $context parameters are passed
  directly on to the "gen-action-set-content" method.

  $position The requested location of the set: 'top' or 'bottom'.
  $context  The context of the set: 'page', 'cluster', or 'list'.
  -->
  <xsl:template name="gen-action-set">
    <xsl:param name="position" />
    <xsl:param name="context" />

    <xsl:call-template name="gen-action-set-content">
      <xsl:with-param name="position" select="$position" />
      <xsl:with-param name="context" select="$context" />
    </xsl:call-template>

  </xsl:template>

  <!--
  Generate the content of an action set.

  $position The requested location of the set: 'top' or 'bottom'.
  $context  The context of the set: 'page', 'cluster', or 'list'.
  -->
  <xsl:template name="gen-action-set-content">
    <xsl:param name="position" />
    <xsl:param name="context" />

    <div class="{$action-set-class}">
      <div class="{$action-set-align-class}">
        <xsl:call-template name="gen-action-set-action-controls" />
      </div>
    </div>
  </xsl:template>

  <!--
  Generate the action controls within an action set. Override this
  with your iterator for the elements that create action controls
  and call "gen-action-set-action-control" from inside the iterator
  for each element.
  -->
  <xsl:template name="gen-action-set-action-controls">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-action-set-action-controls</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
  Generate an action control within an action set.
  -->
  <xsl:template name="gen-action-set-action-control">
    <span class="{$action-set-action-control-class}">
      <xsl:call-template name="gen-action-control">
        <xsl:with-param name="context" select="'action-set'" />
      </xsl:call-template>
    </span>
  </xsl:template>
 
</xsl:stylesheet>