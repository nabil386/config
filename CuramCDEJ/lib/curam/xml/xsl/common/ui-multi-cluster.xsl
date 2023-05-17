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

  <xsl:import href="ui-section.xsl" />
  <xsl:import href="ui-param.xsl" />

  <!--
  NOTE: While these templates do not have a "match" attribute,
  there will still be a context node if the calling template
  had a context node. This context node is automatically passed
  through to templates called from this template.
  -->

  <!--
  Generate a multi-cluster. This is a like a cluster except there can be
  several fields for each label, i.e., a list with headings on the left
  and records displayed in columns. As the styling is identical to a
  cluster, this is a "multi-cluster" rather than a re-oriented "list".

  $title       - The title of the cluster.
  $description - The description of the cluster.
  -->
  <xsl:template name="gen-multi-cluster">

    <xsl:param name="title" select="''"/>
    <xsl:param name="description" select="''"/>

    <xsl:call-template name="gen-section">
      <xsl:with-param name="type" select="'multi-cluster'" />
      <xsl:with-param name="title" select="$title" />
      <xsl:with-param name="description" select="$description" />
    </xsl:call-template>

  </xsl:template>
  
  <!--
  Generate a multi-cluster body. This will call back to
  "gen-multi-cluster-rows". This template should iterate over the elements
  that cause rows to be created and call "gen-multi-cluster-row" for each.

  $title       - The title of the cluster.
  $description - The description of the cluster.
  -->
  <xsl:template name="gen-multi-cluster-body">

    <xsl:param name="title" select="''"/>
    <xsl:param name="description" select="''"/>

    <!-- Cluster body. -->
    <table cellspacing="0" cellpadding="0">

      <!-- Column definitions. Label, Field, Field, Field, .... -->
      <col xsl:use-attribute-sets="cluster-column-label-attributes" />
      <xsl:call-template name="gen-multi-cluster-column-defs" />

      <!-- Body rows. -->
      <tbody>
        <xsl:call-template name="gen-multi-cluster-rows" />
      </tbody>

    </table>

  </xsl:template>

  <!-- ============ M U L T I - C L U S T E R   C O L U M N S ============ -->
  <!--
  Generate the column definitions for the cluster. Override this with a
  template that calculates the total number of column definitions required
  and then calls "gen-multi-cluster-column-def" passing the total number of
  columns.
  -->
  <xsl:template name="gen-multi-cluster-column-defs">

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-multi-cluster-column-defs</xsl:text>
    </xsl:message>

  </xsl:template>

  <!--
  Generate column definitions for a the multi-cluster's value fields. The
  label column is not counted as it is always produced. The label column
  will keep its width; the value columns will share the remaining space
  given by the "value-width" parameter.

  $columns     The total number of value columns required.
  $value-width The percentage width (without the "%") of the cluster
                 allocated to all the value columns.
  -->
  <xsl:template name="gen-multi-cluster-column-def">

    <xsl:param name="columns" />
    <xsl:param name="value-width" select="50" />

    <!-- Internal parameter for looping. -->
    <xsl:param name="this-column" select="1" />

    <xsl:if test="$this-column &lt;= $columns">
      <col width="{concat(round(($value-width) div $columns), '%')}" />
      <!-- Recurse. -->
      <xsl:call-template name="gen-multi-cluster-column-def">
        <xsl:with-param name="columns" select="$columns" />
        <xsl:with-param name="value-width" select="$value-width" />
        <xsl:with-param name="this-column" select="$this-column + 1" />
      </xsl:call-template>
    </xsl:if>

  </xsl:template>

  <!-- =============== M U L T I - C L U S T E R   B O D Y =============== -->
  <!--
  Generate the rows within the body of a cluster. Override this with
  your iterator for the elements that create cluster rows and call
  "gen-multi-cluster-row" from inside the iterator for each element.
  -->
  <xsl:template name="gen-multi-cluster-rows">

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-multi-cluster-rows</xsl:text>
    </xsl:message>

  </xsl:template>

  <!--
  Generate a row within the body of a multi-cluster. The row will start with
  a standard cluster label. Calls back to "gen-multi-cluster-fields".
  -->
  <xsl:template name="gen-multi-cluster-row">

    <tr>
      <xsl:call-template name="gen-multi-cluster-field-label" />
      <xsl:call-template name="gen-multi-cluster-fields" />
    </tr>

  </xsl:template>

  <!--
  Generates the field values for a row within the body of a multi-cluster.
  Override this with your iterator for the elements that create value
  fields and call "gen-multi-cluster-field-value" from inside the iterator
  for each element passing an index value if needed.
  -->
  <xsl:template name="gen-multi-cluster-fields">

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-multi-cluster-fields</xsl:text>
    </xsl:message>

  </xsl:template>

  <!--
  Generate a value field within a row of a multi-cluster. Calls back to
  "gen-multi-cluster-field-value-content" passing back the provided "index"
  parameter representing the current field value column. The indexes do
  not necessarily correlate to column numbers.

  $index - The index of the field value.
  -->
  <xsl:template name="gen-multi-cluster-field-value">

    <xsl:param name="index" select="-1" />

    <td class="{$cluster-field-value-right-class}">
      <xsl:call-template name="gen-multi-cluster-field-value-content">
        <xsl:with-param name="index" select="$index" />
      </xsl:call-template>
    </td>

  </xsl:template>

  <!--
  Generate content for a field value. Override this with a template that
  creates the content of a cluster field value cell. The index value will
  be provided.

  $index - Optional index value.
  -->
  <xsl:template name="gen-multi-cluster-field-value-content">

    <xsl:param name="index" select="-1" />

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-multi-cluster-field-value-content</xsl:text>
    </xsl:message>

  </xsl:template>

  <!--
  Generate the field label cell within the body of a cluster and add a
  colon if the label is on the left-hand side..
  -->
  <xsl:template name="gen-multi-cluster-field-label">

    <td class="{$cluster-field-label-left-class}">
      <xsl:call-template name="gen-multi-cluster-field-label-content" />
      <xsl:text>:</xsl:text>
    </td>

  </xsl:template>

  <!--
  Generate content for a field label. Override this with a template that
  creates the content of a cluster field label cell.
  -->
  <xsl:template name="gen-multi-cluster-field-label-content">

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-multi-cluster-field-label-content</xsl:text>
    </xsl:message>

  </xsl:template>

</xsl:stylesheet>