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
  The separator for encoded names of input controls.
  -->
  <xsl:param name="name-encoding-delim" select="'.'"/>

  <!--
  The classes applied to a section (list or cluster) heading.
  TODO: Change the CSS so that these class names make more sense!
   -->
  <xsl:param name="section-heading-class" select="'title'" />
  <xsl:param name="section-heading-title-class" select="'header'" />
  <xsl:param name="section-heading-description-class" select="'desc'" />

  <!--
  The attributes applied to column definitions for clusters and lists.
  -->
  <xsl:attribute-set name="cluster-column-field-attributes"/>
  <xsl:attribute-set name="cluster-column-label-attributes"/>
  <xsl:attribute-set name="cluster-column-field-only-attributes" />
  <xsl:attribute-set name="list-column-attributes"/>
  <xsl:attribute-set name="multi-cluster-column-field-attributes"/>

  <!--
  The style applied to the caption element in the body table of
  a section (cluster or list).
  -->
  <xsl:param name="section-body-caption-style" select="'display:none;'" />

  <!--
  The class attribute values applied to the main div and alignment
  div containing an action set.
  -->
  <xsl:param name="action-set-class" select="''" />
  <xsl:param name="action-set-align-class" select="'ac_center'" />

  <!--
  The class attribute value for the span containing an action control
  in an action set.
  -->
  <xsl:param name="action-set-action-control-class" select="'ac_control'" />

  <!--
  The class applied to the "thead" element in a list header.
  -->
  <xsl:param name="list-header-class" select="'field'" />

  <!--
  The class applied to the "td" field elements in a list body.
  -->
  <xsl:param name="list-body-field-class" select="'field'" />

  <!--
  The classes for the "td" elements containing field labels and
  field values when in left or right orientations, or when only
  fields are shown.
  -->
  <xsl:param name="cluster-field-label-left-class"
             select="'label'"/>
  <xsl:param name="cluster-field-label-right-class"
             select="'label'"/>
  <xsl:param name="cluster-field-value-left-class"     select="'field'"/>
  <xsl:param name="cluster-field-value-right-class"    select="'field'"/>
  <xsl:param name="cluster-field-value-no-label-class" select="'field'"/>

  <!--
  The class for the "col" element used when a cluster is not displaying
  any labels.
  -->
  <xsl:param name="cluster-field-col-no-label-class" select="'fieldnolabels'"/>

  <!--
  Classes for field controls.
  -->

  <!--
  The class applied to the "select" for a code-table drop-down list.
  -->
  <xsl:param name="code-table-list-class" select="'left'" />
  
</xsl:stylesheet>