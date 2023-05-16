<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  Copyright (c) 2004-2005 Curam Software Ltd.
  All rights reserved.

  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:code-table="http://xml.apache.org/xalan/java/curam.omega3.codetable.CodeTableRepository"
  xmlns:domain-info="http://xml.apache.org/xalan/java/curam.util.client.domain.util.DomainUtils"
  xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils" 
  exclude-result-prefixes="code-table domain-info bidi-utils" version="1.0"
>

  <xsl:import href="ui-param.xsl" />

  <!--
    NOTE: While these templates do not have a "match" attribute,
    there will still be a context node if the calling template
    had a context node. This context node is automatically passed
    through to templates called from this template.
  -->

  <!--
    Generate a field. This can be an input or an output and the actual control
    depends on the domain definition.

    $name   The encoded name of the field.
    $title  The title (label) for the field.
    $value  The initial value for the field (optional).
    $domain The domain definition name for the field.
    $mode   The field mode: "in", "out", or "inout".
    $style  The CSS style to apply to the field (optional).
  -->
  <xsl:template name="gen-field">

    <xsl:param name="name" />
    <xsl:param name="title" />
    <xsl:param name="value" />
    <xsl:param name="domain" />
    <xsl:param name="mode" />
    <xsl:param name="style" />

    <xsl:choose>
      <xsl:when test="$mode = 'in'">
        <xsl:call-template name="gen-input-field">
          <xsl:with-param name="name" select="$name" />
          <xsl:with-param name="title" select="$title" />
          <xsl:with-param name="value" select="$value" />
          <xsl:with-param name="domain" select="$domain" />
          <xsl:with-param name="style" select="$style" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:message terminate="yes">
          <xsl:text>
            ERROR: Unsupported mode in "gen-field".
          </xsl:text>
        </xsl:message>
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>

  <!--
    Generate an input field. The actual control depends on the domain
    definition.

    $name   The encoded name of the field.
    $title  The title (label) for the field.
    $value  The initial value for the field (optional).
    $domain The domain definition name for the field.
    $style  The CSS style to apply to the field (optional).
  -->
  <xsl:template name="gen-input-field">

    <xsl:param name="name" />
    <xsl:param name="title" />
    <xsl:param name="value" />
    <xsl:param name="domain" />
    <xsl:param name="style" />

    <xsl:param name="id" />
    <xsl:param name="onfocus" />

    <!-- Get the code-table name for the domain. -->
    <xsl:variable name="ct-name"
      select="domain-info:getDomainNodeSet($domain)/@code-table-name" />

    <!-- Get the code-table data from the code-table repository. -->
    <xsl:variable name="code-table"
      select="code-table:getCodeTableNodeSet($ct-name, $locale)" />

    <!-- Get the domain node set -->
    <xsl:variable name="domain-nodeset"
      select="domain-info:getDomainNodeSet($domain)" />

    <!-- Get the root domain name -->
    <xsl:variable name="root-domain-name"
      select="$domain-nodeset/@root-domain" />

    <xsl:choose>
      <xsl:when test="$code-table">
        <xsl:call-template name="gen-code-table-list-field">
          <xsl:with-param name="name" select="$name" />
          <xsl:with-param name="title" select="$title" />
          <xsl:with-param name="value" select="$value" />
          <xsl:with-param name="code-table"
            select="$code-table" />
          <xsl:with-param name="style" select="$style" />
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$root-domain-name = 'SVR_BOOLEAN'">
        <xsl:call-template
          name="gen-boolean-radio-button-field">
          <xsl:with-param name="name" select="$name" />
          <xsl:with-param name="title" select="$title" />
          <xsl:with-param name="value" select="$value" />
          <xsl:with-param name="style" select="$style" />
          <!--<xsl:with-param name="id" select="$element-id" />-->
          <xsl:with-param name="onfocus" select="$onfocus" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="gen-text-input-field">
          <xsl:with-param name="name" select="$name" />
          <xsl:with-param name="title" select="$title" />
          <xsl:with-param name="value" select="$value" />
          <xsl:with-param name="style" select="$style" />
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>

  <!--
    Generate a text input field.

    $name  The encoded name of the field.
    $title The title (label) for the field.
    $value The initial value for the field (optional).
    $style The CSS style to apply to the field (optional).
  -->
  <xsl:template name="gen-text-input-field">

    <xsl:param name="name" />
    <xsl:param name="title" />
    <xsl:param name="value" select="''" />
    <xsl:param name="style" />

    <input type="text" id="{$name}" name="{$name}" value="{$value}"
      title="{$title}">
      <xsl:attribute name="dir">
			<xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(title)"/>
      </xsl:attribute>            
      
      <xsl:if test="$style">
        <xsl:attribute name="style">
          <xsl:value-of select="$style" />
        </xsl:attribute>
      </xsl:if>
    </input>

  </xsl:template>

  <!--
    Generate a code-table drop-down list field. A blank entry is always added
    to the list and is used as the default value.

    $name       The encoded name of the field.
    $title      The title (label) for the field.
    $value      The initial value for the field (optional).
    $code-table The code-table node set.
    $style      The CSS style to apply to the field (optional).
  -->
  <xsl:template name="gen-code-table-list-field">

    <xsl:param name="name" />
    <xsl:param name="title" />
    <xsl:param name="value" />
    <xsl:param name="code-table" />
    <xsl:param name="style" />
    <xsl:param name="no-blank" />

    <xsl:variable name="class">
      <xsl:choose>
        <xsl:when test="$style ='answer-input-eval' ">
          <xsl:value-of select="$style" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$code-table-list-class" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <select>
      <xsl:if test="$name">
        <xsl:attribute name="id">
          <xsl:value-of select="$name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$name">
        <xsl:attribute name="name">
          <xsl:value-of select="$name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$title">
        <xsl:attribute name="title">
          <xsl:value-of select="$title"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$class">
        <xsl:attribute name="class">
          <xsl:value-of select="$class"/>
        </xsl:attribute>
      </xsl:if>

      <!-- Conditionally insert a blank entry. Select it as the default. -->
      <xsl:if test="not($no-blank = 'true')">
        <option value="">
          <xsl:if test="not($value)">
            <xsl:attribute name="selected">
              <xsl:text>selected</xsl:text>
            </xsl:attribute>
          </xsl:if>
        </option>
      </xsl:if>

      <xsl:for-each select="$code-table/item">
        <xsl:if test="@enabled = 'true'">
          <option value="{@code}">
            <xsl:if test="@code = $value">
              <xsl:attribute name="selected">
                <xsl:text>selected</xsl:text>
              </xsl:attribute>
            </xsl:if>
            <xsl:value-of select="description/text()" />
          </option>
        </xsl:if>
      </xsl:for-each>
    </select>

  </xsl:template>

  <!--
    Generate a radiobox field.

    $name    The encoded name of the field.
    $title   The title (label) for the field.
    $value   The initial value for the field (optional).
    $style   The CSS style to apply to the field (optional).
    $id      The id of the field. If not specified the value of $name is used
    $onfocus The onfocus attribute for the field. (optional)
  -->
  <xsl:template name="gen-boolean-radio-button-field">

    <xsl:param name="name" />
    <xsl:param name="title" />
    <xsl:param name="value" />
    <xsl:param name="style" />
    <xsl:param name="onfocus" />

    <input type="radio" name="{$name}" value="true"
      title="{$title}">
      <xsl:if test="$value = 'true'">
        <xsl:attribute name="checked">
          <xsl:text>checked</xsl:text>
        </xsl:attribute>
      </xsl:if>
      <!--<xsl:if test="$onfocus">
        <xsl:attribute name="onfocus">
          <xsl:value-of select="$onfocus" />
        </xsl:attribute>
      </xsl:if>-->
    </input>
    <input type="radio" name="{$name}" value="false"
           title="{$title}">
      <xsl:if test="$value = '' or $value = 'false'">
        <xsl:attribute name="checked">
          <xsl:text>checked</xsl:text>
        </xsl:attribute>
      </xsl:if>
      <!--<xsl:if test="$onfocus">
        <xsl:attribute name="onfocus">
          <xsl:value-of select="$onfocus" />
        </xsl:attribute>
      </xsl:if>-->
    </input>
  </xsl:template>

  <!--
  Generate a checkbox field.

  $name    The encoded name of the field.
  $title   The title (label) for the field.
  $value   The initial value for the field (optional).
  $style   The CSS style to apply to the field (optional).
  $id      The id of the field. If not specified the value of $name is used
  $onfocus The onfocus attribute for the field. (optional)
  -->
  <xsl:template name="gen-checkbox-field">

    <xsl:param name="name" />
    <xsl:param name="title" />
    <xsl:param name="value" />
    <xsl:param name="style" />
    <xsl:param name="id" />
    <xsl:param name="onfocus" />
    <!--<input type="checkbox" id="{$id}" name="{$name}.true" value="true"
           title="{$title}">-->
    <input type="checkbox" name="{$name}.true" value="true" title="{$title}">
      <xsl:if test="$value = 'true'">
        <xsl:attribute name="checked">
          <xsl:text>checked</xsl:text>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$style">
        <xsl:attribute name="style">
          <xsl:value-of select="$style" />
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$onfocus">
        <xsl:attribute name="onfocus">
          <xsl:value-of select="$onfocus" />
        </xsl:attribute>
      </xsl:if>
    </input>
    <!-- need a second hidden fields to submit "false" -->
    <!--<input type="hidden" name="{$name}" value="false" />-->

  </xsl:template>

  <!--
  Generate a true or false drop-down list field.

  $id       The encoded name of the field.
  $name    The encoded name of the field.
  $value      The initial value for the field (optional).
  -->
  <xsl:template name="gen-true-false-list-field">

    <xsl:param name="id" />
    <xsl:param name="name" />
    <xsl:param name="value" />

    <select id="{$id}" name="{$name}">
      <option value="false">
        <xsl:if test="$value = 'false'">
          <xsl:attribute name="selected">
            <xsl:text>selected</xsl:text>
          </xsl:attribute>
        </xsl:if>
       <xsl:text>No</xsl:text>
      </option>
      <option value="true">
        <xsl:if test="$value = 'true'">
          <xsl:attribute name="selected">
            <xsl:text>selected</xsl:text>
          </xsl:attribute>
        </xsl:if>
        <xsl:text>Yes</xsl:text>
      </option>
    </select>

  </xsl:template>



</xsl:stylesheet>