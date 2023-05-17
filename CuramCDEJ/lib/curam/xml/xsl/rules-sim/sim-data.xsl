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

  <xsl:import href="../common/ui.xsl" />
  <xsl:import href="../common/runtime-params.xsl" />
  <xsl:import href="../common/string-utils.xsl" />
  <xsl:import href="ui-param-sim-data.xsl" />

  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <!--
  The prefix for all control names related to the simulation data and
  the command strings for storing values, adding elements, or retrieving
  the meta-data.
  -->
  <xsl:param name="sim-prefix" />
  <xsl:param name="sim-store-command" />
  <xsl:param name="sim-add-command" />
  <xsl:param name="sim-meta-data-command" />

  <!--
  The image and caption of the add button shown in lists. If the image
  is not an empty string, the caption should be used as the alt-text.
  -->
  <xsl:param name="sim-add-button-image" />
  <xsl:param name="sim-add-button-caption" />

  <!--
  When the add button is pressed, an "add" command is created. This
  command is passed to the next invocation of this stylesheet and must
  be acted upon. The commands are contained in this parameter. A
  "commands" element contains "add" elements with an "rdoname" attribute
  set to the value of the list RDO to which a blank line should be added
  during rendering.
  -->
  <xsl:param name="sim-commands" />
  
  <!--
  Values node-set for simulation data fields.
  -->
  <xsl:param name="sim-values" />

  <!--
  Encoded value for the meta-data string to be embedded as a hidden
  field.
  -->
  <xsl:param name="sim-encoded-meta-data" />

  <!--
  The locale-code used when rendering descriptions using encoded
  message strings.
  -->
  <xsl:param name="locale-code" />

  <!--
  The width value (in percent) for the generated clusters' labels.
  -->
  <xsl:param name="label-width" />

  <!--
  The meta-data is the root document to be processed, but when the
  context changes to data in the sim-values node-set, this variable
  is needed to access the meta-data again.
  -->
  <xsl:variable name="sim-meta-data" select="/" />

  <!--
  The key used to select all value elements for dataitems of a list
  RDO given the RDO name and index value. Must operate within the
  context of the "sim-values" document.
  -->
  <xsl:key name="values-by-rdo-name-and-index" match="value[@index]"
           use="concat(@rdoname, '.', @index)"/>

  
  <!-- The rules sim editor should only display dropdowns for code tables
       and text fields for all other domains. A radio button template was 
       introduce into ui-field.xsl that had an impact on the rules sim editor.
       So, we override that radio button template here and get it to re-call the
       text field template.
    -->
  <xsl:template name="gen-boolean-radio-button-field">
  
    <xsl:param name="name" />
    <xsl:param name="title" />
    <xsl:param name="value" />
    <xsl:param name="style" />
    <xsl:param name="onfocus" />

    <xsl:call-template name="gen-text-input-field">
      <xsl:with-param name="name" select="$name" />
      <xsl:with-param name="title" select="$title" />
      <xsl:with-param name="value" select="$value" />
      <xsl:with-param name="style" select="$style" />
    </xsl:call-template>

  </xsl:template>


  <!--
  Meta-data templates.
  -->
  <xsl:template match="rdos">
    <input type="hidden" value="{$sim-encoded-meta-data}"
           name="{concat($sim-prefix, $name-encoding-delim,
                         $sim-meta-data-command)}" />
    <xsl:apply-templates />
  </xsl:template>

  <!--
  Only match rdo elements that contain dataitem elements. There is no
  point displaying something that has no values to be entered.
  -->
  <xsl:template match="rdo[dataitem]">

    <xsl:variable name="localized-descr">
      <xsl:call-template name="get-localized-message">
        <xsl:with-param name="messages" select="@description" />
        <xsl:with-param name="locale-code" select="$locale-code" />
      </xsl:call-template>
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="@islist = 'true'">
        <xsl:call-template name="gen-multi-cluster">
          <xsl:with-param name="title" select="@name" />
          <xsl:with-param name="description" select="$localized-descr" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="gen-cluster">
          <xsl:with-param name="title" select="@name" />
          <xsl:with-param name="description" select="$localized-descr" />
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--
  Generates a label for a dataitem (the context). The name is used if a
  description is not available. The description may be empty apart from
  the language codes, so it is tested for content after translation.
  -->
  <xsl:template name="gen-dataitem-label">
    <xsl:variable name="description">
      <xsl:call-template name="get-localized-message">
        <xsl:with-param name="messages" select="@description" />
        <xsl:with-param name="locale-code" select="$locale-code" />
      </xsl:call-template>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="not(normalize-space($description) = '')">
        <xsl:value-of select="$description" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="@name" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- ========================= C L U S T E R S ========================= -->

  <!-- ================== c l u s t e r   c o l u m n s ================== -->
  <!--
  Call-back. Context is a single "rdo".
  -->
  <xsl:template name="gen-cluster-column-defs">
    <!-- The default layout order is used. -->
    <xsl:call-template name="gen-cluster-column-def" />
  </xsl:template>

  <!-- ===================== c l u s t e r   b o d y ===================== -->
  <!--
  Call-back. Context is a single "rdo".
  -->
  <xsl:template name="gen-cluster-rows">
    <xsl:for-each select="dataitem">
      <xsl:call-template name="gen-cluster-row" />
    </xsl:for-each>
  </xsl:template>

  <!--
  Call-back. Context is a single "dataitem".
  -->
  <xsl:template name="gen-cluster-fields">
    <xsl:call-template name="gen-cluster-field" />
  </xsl:template>
  
  <!--
  Override to get rid of the colon at the end
  -->
  <xsl:template name="gen-cluster-field-label">
    <xsl:param name="layout-order" />
    <xsl:param name="field-class" />

    <td class="{$field-class}">
      <xsl:call-template name="gen-cluster-field-label-content"/>
    </td>
  </xsl:template>

  <!--
  Call-back. Context is a single "dataitem".
  -->
  <xsl:template name="gen-cluster-field-label-content">
    <xsl:call-template name="gen-dataitem-label" />
  </xsl:template>

  <!--
  Call-back. Context is a single "dataitem". The "title" attribute is
  needed for Section 508 compliance.
  -->
  <xsl:template name="gen-cluster-field-value-content">

    <xsl:call-template name="gen-field">
      <xsl:with-param name="name"
                      select="concat($sim-prefix, $name-encoding-delim,
                                     $sim-store-command,
                                     $name-encoding-delim, ../@name,
                                     $name-encoding-delim, @name)" />
      <xsl:with-param name="title">
        <xsl:call-template name="gen-dataitem-label" />
      </xsl:with-param>
      <xsl:with-param name="value"
                      select="$sim-values/values
                              /value[@rdoname = current()/../@name
                                     and @dataitemname = current()/@name]
                              /@value" />
      <xsl:with-param name="domain" select="@type" />
      <xsl:with-param name="mode" select="'in'" />
      <xsl:with-param name="style" select="'width: 100%;'" />
    </xsl:call-template>

  </xsl:template>

  <!-- =================== M U L T I - C L U S T E R S =================== -->

  <!-- =============== m u l t i - c l u s t e r   h e a d =============== -->
  <!--
  Call-back. Context is a single "rdo". Needs to pass the total number of
  value columns required to the "gen-multi-cluster-column-def" template.
  -->
  <xsl:template name="gen-multi-cluster-column-defs">

    <!--
    Select one value for each column that will be created (the index
    attributes will be unique in this node set.) The value data is
    sparse, i.e., the indices are not necessarily consecutive, and for
    a given index, there is not necessarily a value for each dataitem.
    -->
    <xsl:variable name="value-per-index"
        select="$sim-values/values/value
                   [@rdoname = current()/@name
                    and generate-id()
                           = generate-id(
                                key('values-by-rdo-name-and-index',
                                    concat(@rdoname, '.', @index))[1])]" />

    <xsl:variable name="value-columns"
                  select="count($value-per-index)" />

    <xsl:variable name="new-columns"
                  select="count($sim-commands/commands
                                /add[@rdoname = current()/@name])" />

    <xsl:call-template name="gen-multi-cluster-column-def">
      <xsl:with-param name="columns">
        <xsl:choose>
          <xsl:when test="$value-columns + $new-columns &gt; 0">
            <xsl:value-of select="$value-columns + $new-columns" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="1" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:with-param>
      <xsl:with-param name="value-width" select="100 - $label-width" />
    </xsl:call-template>

  </xsl:template>

  <!-- =============== m u l t i - c l u s t e r   b o d y =============== -->
  <!--
  Call-back. Context is a single "rdo". Iterates over the elements that will
  drive row generation in the cluster: the RDO's dataitem elements.
  -->
  <xsl:template name="gen-multi-cluster-rows">

    <xsl:for-each select="dataitem">
      <xsl:call-template name="gen-multi-cluster-row" />
    </xsl:for-each>

  </xsl:template>

  <!--
  Call-back. Context is a single "dataitem". Iterates over all the values for
  the context dataitem, but must allow for the sparseness of the value data.
  -->
  <xsl:template name="gen-multi-cluster-fields">

    <xsl:variable name="this-dataitem" select="." />

    <!--
    See comment in "gen-multi-cluster-column-defs" above. We now have a
    node-list with one value for each column (but not necessarily for this
    dataitem).
    -->
    <xsl:variable name="value-per-index"
        select="$sim-values/values/value
                   [@rdoname = $this-dataitem/../@name
                    and generate-id()
                           = generate-id(
                                key('values-by-rdo-name-and-index',
                                    concat(@rdoname, '.', @index))[1])]" />

    <xsl:choose>
      <xsl:when test="count($value-per-index) &gt; 0">
        <!--
        Use the selected values (sorted by index) to drive the
        generation of each field.
        -->
        <xsl:for-each select="$value-per-index">
          <xsl:sort order="ascending" data-type="number" select="@index" />
 
          <xsl:variable name="this-index" select="@index" />
  
          <xsl:apply-templates select="$this-dataitem"
                               mode="gen-multi-cluster-field-value">
            <xsl:with-param name="index" select="$this-index" />
          </xsl:apply-templates>
  
          <xsl:if test="position() = last()">
            <!--
            Process any "add" commands for this RDO. This is in the loop
            so that the new index can be calculated.
            -->
            <xsl:for-each select="$sim-commands/commands/add
                                  [@rdoname = $this-dataitem/../@name]">
              <xsl:apply-templates select="$this-dataitem"
                                   mode="gen-multi-cluster-field-value">
                <!-- Here, position() is the position of the "add". -->
                <xsl:with-param name="index"
                                select="$this-index + position()" />
              </xsl:apply-templates>
            </xsl:for-each>
          </xsl:if>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <!--
        Display a default empty column. If there can be only one add command
        for an RDO, the commands could be eliminated and an empty column
        always added to the list.
        -->
        <xsl:apply-templates select="$this-dataitem"
                             mode="gen-multi-cluster-field-value" />
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>

  <!--
  Override to get rid of the colon at the end
  -->
  <xsl:template name="gen-multi-cluster-field-label">
    <td class="{$cluster-field-label-left-class}">
      <xsl:call-template name="gen-multi-cluster-field-label-content" />
    </td>
  </xsl:template>

  <!--
  Template used to switch the context to the dataitem element before calling
  the "gen-multi-cluster-field-value" template.
  -->
  <xsl:template match="dataitem" mode="gen-multi-cluster-field-value">

    <xsl:param name="index" select="1" />

    <xsl:call-template name="gen-multi-cluster-field-value">
      <xsl:with-param name="index" select="$index" />
    </xsl:call-template>

  </xsl:template>

  <!--
  Call-back. Context is a single "dataitem".
  -->
  <xsl:template name="gen-multi-cluster-field-label-content">

    <xsl:call-template name="gen-dataitem-label" />

  </xsl:template>

  <!--
  Call-back. Context is a single "dataitem". The "title" attribute is
  needed for Section 508 compliance. The "index" indicates the value
  that should be used (no value is okay).
  -->
  <xsl:template name="gen-multi-cluster-field-value-content">

    <xsl:param name="index" />

    <xsl:variable name="enc-name"
                  select="concat(../@name, $name-encoding-delim, @name,
                                 $name-encoding-delim, $index)" />

    <xsl:call-template name="gen-field">
      <xsl:with-param name="name"
                      select="concat($sim-prefix, $name-encoding-delim,
                                     $sim-store-command,
                                     $name-encoding-delim, $enc-name)" />
      <xsl:with-param name="title">
        <xsl:call-template name="gen-dataitem-label" />
      </xsl:with-param>
      <xsl:with-param name="value"
                      select="$sim-values/values
                              /value[@rdoname = current()/../@name
                                     and @dataitemname = current()/@name
                                     and @index = $index]
                              /@value" />
      <xsl:with-param name="domain" select="@type" />
      <xsl:with-param name="mode" select="'in'" />
      <xsl:with-param name="style" select="'width: 100%;'" />
    </xsl:call-template>

  </xsl:template>

  <!-- ====================== A C T I O N   S E T S ===================== -->

  <!--
  Override to put action sets at the bottom of multi-clusters only.
  Context is an "rdo". 
  -->
  <xsl:template name="gen-action-set">
    <xsl:param name="position" />
    <xsl:param name="context" />

    <xsl:if test="$position = 'bottom' and $context = 'multi-cluster'">
      <xsl:call-template name="gen-action-set-content">
        <xsl:with-param name="position" select="$position" />
        <xsl:with-param name="context" select="$context" />
      </xsl:call-template>
    </xsl:if>

  </xsl:template>

  <!--
  Call-back. Context is an "rdo".
  Single call with no parameters just to trigger an "Add" button.
  -->
  <xsl:template name="gen-action-set-action-controls">
    <xsl:call-template name="gen-action-set-action-control" />
  </xsl:template>

  <!--
  Call-back. Context is an "rdo".
  -->
  <xsl:template name="gen-action-control">

    <xsl:param name="context" />

    <!--
    The image-url will be an empty string if $sim-add-button-image is an
    empty string, or else it will be prefixed with the static URL.
    -->
    <xsl:variable name="image-url">
      <xsl:if test="$sim-add-button-image">
        <xsl:value-of select="concat($static-content-server-url, '/')" />
      </xsl:if>
      <xsl:value-of select="$sim-add-button-image" />
    </xsl:variable>

    <xsl:if test="$context = 'action-set' and @islist = 'true'">
      <xsl:call-template name="gen-submit-button">
        <xsl:with-param name="name"
                        select="concat($sim-prefix, $name-encoding-delim,
                                       $sim-add-command,
                                       $name-encoding-delim, @name)" />
        <xsl:with-param name="caption" select="$sim-add-button-caption" />
        <xsl:with-param name="image" select="$image-url" />
      </xsl:call-template>
    </xsl:if>

  </xsl:template>

</xsl:stylesheet>