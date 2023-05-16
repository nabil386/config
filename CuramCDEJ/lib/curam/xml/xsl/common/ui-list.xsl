<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright © 2010 Curam Software Ltd.
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
    Generate a list.

    $title       - The title of the list.
    $description - The description of the list.
  -->
  <xsl:template name="gen-list">

    <xsl:param name="title" select="''"/>
    <xsl:param name="description" select="''"/>

    <xsl:call-template name="gen-section">
      <xsl:with-param name="type" select="'list'" />
      <xsl:with-param name="title" select="$title" />
      <xsl:with-param name="description" select="$description" />
    </xsl:call-template>

  </xsl:template>
  
  <!--
    Generate a list body.

    $title       - The title of the list.
    $description - The description of the list.
  -->
  <xsl:template name="gen-list-body">

    <xsl:param name="title" select="''"/>
    <xsl:param name="description" select="''"/>

    <!-- List body. -->
    <table>

      <!-- Column definitions. -->
      <xsl:call-template name="gen-list-column-defs" />

      <!-- Column headings in the list header. -->
      <thead class="{$list-header-class}">
        <xsl:call-template name="gen-list-header-content" />
      </thead>

      <!-- Body rows. -->
      <tbody>
        <xsl:call-template name="gen-list-body-content" />
      </tbody>

    </table>

  </xsl:template>

  <!-- ===================== L I S T   C O L U M N S ===================== -->
  <!--
    Generate the column definitions for the list. Override this with a
    template that iterates over the elements that create column definitions
    and then call "gen-list-column-def" for each element.
  -->
  <xsl:template name="gen-list-column-defs">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-list-column-defs</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
    Generate a column definitions for a list.
  -->
  <xsl:template name="gen-list-column-def">
    <col xsl:use-attribute-sets="list-column-attributes" />
  </xsl:template>

  <!-- ====================== L I S T   H E A D E R ====================== -->
  <!--
    A hook to allow the content to be omitted or wrapped by overriding
    this template. The default behaviour creates a header row.
  -->
  <xsl:template name="gen-list-header-content">
    <xsl:call-template name="gen-list-header-row" />
  </xsl:template>

  <!--
    Generate the row of column titles in the list header.
  -->
  <xsl:template name="gen-list-header-row">
    <tr>
      <xsl:call-template name="gen-list-header-fields" />
    </tr>
  </xsl:template>

  <!--
    Generate the fields in the header row containing the titles. Override
    this template with one that iterates over the elements that create
    column titles and call "gen-list-header-field" for each element.
  -->
  <xsl:template name="gen-list-header-fields">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-list-header-fields</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
    Generates a header field (column heading) for a list.
  -->
  <xsl:template name="gen-list-header-field">
    <th>
      <span>
        <xsl:call-template name="gen-list-header-field-content" />
      </span>
    </th>
  </xsl:template>

  <!--
    Generates a the content (the title) to appear in a list header field
    (column heading). Override this template with one that generates the
    content of the field (if any).
  -->
  <xsl:template name="gen-list-header-field-content">
    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-list-header-field-content</xsl:text>
    </xsl:message>
  </xsl:template>

  <!-- ======================== L I S T   B O D Y ======================== -->
  <!--
    A hook to allow the content to be omitted or wrapped by overriding
    this template. The default behaviour is to generate body rows.
  -->
  <xsl:template name="gen-list-body-content">
    <xsl:call-template name="gen-list-body-rows" />
  </xsl:template>

  <!--
    Generate the rows within the body of a list. Override this with
    your iterator for the elements that create list body rows and
    call "gen-list-body-row" for each element passing the index
    if required.

    $index - Optional index number that will be passed on.
  -->
  <xsl:template name="gen-list-body-rows">
    <xsl:param name="index" select="-1" />

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-list-body-rows</xsl:text>
    </xsl:message>
  </xsl:template>

  <!--
    Generate a row of fields in the list body.
  -->
  <xsl:template name="gen-list-body-row">
    <xsl:param name="index" select="-1" />

    <tr>
      <xsl:call-template name="gen-list-body-fields">
        <xsl:with-param name="index" select="$index" />
      </xsl:call-template>
    </tr>
  </xsl:template>
  
  <!--
    Generate the fields in a list body row. Override this with
    your iterator for the elements that create list body fields and
    call "gen-list-body-field" for each element passing the index
    if required.
  -->
  <xsl:template name="gen-list-body-fields">
    <xsl:param name="index" select="-1" />

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-list-body-fields</xsl:text>
    </xsl:message>
  </xsl:template>
  
  <!--
    Generates a header field (column heading) for a list.
  -->
  <xsl:template name="gen-list-body-field">
    <xsl:param name="index" select="-1" />

    <td class="{$list-body-field-class}">
      <xsl:call-template name="gen-list-body-field-content">
        <xsl:with-param name="index" select="$index" />
      </xsl:call-template>
    </td>
  </xsl:template>

  <!--
    Generates a the content to appear in a list body field (cell). Override
    this template with one that generates the content of the field (if any).
  -->
  <xsl:template name="gen-list-body-field-content">
    <xsl:param name="index" select="-1" />

    <xsl:message terminate="yes">
      <xsl:text>ERROR: Not implemented: gen-list-body-field-content</xsl:text>
    </xsl:message>
  </xsl:template>

</xsl:stylesheet>
