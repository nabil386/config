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
  NOTE: While these templates do not have a "match" attribute,
  there will still be a context node if the calling template
  had a context node. This context node is automatically passed
  through to templates called from this template.
  -->

  <!--
  Generate a cluster.

  $title       - The title of the cluster.
  $description - The description of the cluster.
  -->
  <xsl:template name="gen-cluster">

    <xsl:param name="title" select="''"/>
    <xsl:param name="description" select="''"/>

    <xsl:call-template name="gen-section">
      <xsl:with-param name="type" select="'cluster'" />
      <xsl:with-param name="title" select="$title" />
      <xsl:with-param name="description" select="$description" />
    </xsl:call-template>

  </xsl:template>
  
  <!--
  Generate a cluster body.

  $title       - The title of the cluster.
  $description - The description of the cluster.
  -->
  <xsl:template name="gen-cluster-body">

    <xsl:param name="title" select="''"/>
    <xsl:param name="description" select="''"/>

    <!-- Cluster body. -->
    <table cellspacing="0" cellpadding="0">

      <!-- Column definitions. -->
      <xsl:call-template name="gen-cluster-column-defs" />

      <!-- Body rows. -->
      <tbody>
        <xsl:call-template name="gen-cluster-rows" />
      </tbody>

    </table>

  </xsl:template>
  
  <!-- ================== C L U S T E R   C O L U M N S ================== -->
  <!--
  Generate the column definitions for the cluster. Override this with a
  template that iterates over the elements that create column definitions
  and then call "gen-cluster-column-def" passing a layout-order parameter
  set to 'label-field', 'field-label', or 'field-only'.

  These will define the label and field columns for the body table.
  -->
  <xsl:template name="gen-cluster-column-defs">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-cluster-column-defs</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
  Generate column definitions for a cluster field (this will generate
  one or two column definitions depending on whether or not labels are
  to be displayed).

  $layout-order - 'label-field' (default), 'field-label', or 'field-only'.
  -->
  <xsl:template name="gen-cluster-column-def">
    <xsl:param name="layout-order" select="'label-field'"/>

    <xsl:choose>
      <xsl:when test="$layout-order = 'field-label'">
        <col xsl:use-attribute-sets="cluster-column-field-attributes" />
        <col xsl:use-attribute-sets="cluster-column-label-attributes" />
      </xsl:when>
      <xsl:when test="$layout-order = 'field-only'">
        <col xsl:use-attribute-sets="cluster-column-field-only-attributes" />
      </xsl:when>
      <xsl:otherwise>
        <col xsl:use-attribute-sets="cluster-column-label-attributes" />
        <col xsl:use-attribute-sets="cluster-column-field-attributes" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- ===================== C L U S T E R   B O D Y ===================== -->
  <!--
  Generate the rows within the body of a cluster. Override this with
  your iterator for the elements that create cluster rows and call
  "gen-cluster-row" from inside the iterator for each element.
  -->
  <xsl:template name="gen-cluster-rows">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-cluster-rows</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
  Generate a row within the body of a cluster.
  -->
  <xsl:template name="gen-cluster-row">
    <tr>
      <xsl:call-template name="gen-cluster-fields"/>
    </tr>
  </xsl:template>

  <!--
  Generate the fields within a body row of a cluster. Override this with
  your iterator for the elements that create fields in a cluster row and
  call "gen-cluster-field" from inside the iterator for each element.
  -->
  <xsl:template name="gen-cluster-fields">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-cluster-fields</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
  Generate a field (label-value pair) within a cluster row.

  $layout-order - 'label-field' (default) or 'field-label'.
  -->
  <xsl:template name="gen-cluster-field">
    <xsl:param name="layout-order" select="'label-field'"/>

    <xsl:choose>
      <xsl:when test="$layout-order = 'field-label'">
        <xsl:call-template name="gen-cluster-field-value">
          <xsl:with-param name="field-class"
                          select="$cluster-field-value-left-class"/>
        </xsl:call-template>
        <xsl:call-template name="gen-cluster-field-label">
          <xsl:with-param name="layout-order" select="$layout-order"/>
          <xsl:with-param name="field-class"
                          select="$cluster-field-label-right-class"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$layout-order = 'field-only'">
        <xsl:call-template name="gen-cluster-field-value">
          <xsl:with-param name="layout-order" select="$layout-order"/>
          <xsl:with-param name="field-class"
                          select="$cluster-field-value-no-label-class"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="gen-cluster-field-label">
          <xsl:with-param name="layout-order" select="$layout-order"/>
          <xsl:with-param name="field-class"
                          select="$cluster-field-label-left-class"/>
        </xsl:call-template>
        <xsl:call-template name="gen-cluster-field-value">
          <xsl:with-param name="field-class"
                          select="$cluster-field-value-right-class"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--
  Generate the field value cell within the body of a cluster.
  -->
  <xsl:template name="gen-cluster-field-value">
    <xsl:param name="field-class" />

    <td class="{$field-class}">
      <xsl:call-template name="gen-cluster-field-value-content"/>
    </td>
  </xsl:template>

  <!--
  Generate content for a field value. Override this with a template that
  creates the content of a cluster field value cell.
  -->
  <xsl:template name="gen-cluster-field-value-content">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-cluster-field-value-content</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
  Generate the field label cell within the body of a cluster and add a
  colon if the label is on the left-hand side..
  -->
  <xsl:template name="gen-cluster-field-label">
    <xsl:param name="layout-order" />
    <xsl:param name="field-class" />

    <td class="{$field-class}">
      <xsl:call-template name="gen-cluster-field-label-content"/>
      <xsl:if test="$layout-order = 'label-field'">
        <xsl:text>:</xsl:text>
      </xsl:if>
    </td>
  </xsl:template>

  <!--
  Generate content for a field label. Override this with a template that
  creates the content of a cluster field label cell.
  -->
  <xsl:template name="gen-cluster-field-label-content">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-cluster-field-label-content</xsl:text>
    </xsl:message>
  </xsl:template>

</xsl:stylesheet>