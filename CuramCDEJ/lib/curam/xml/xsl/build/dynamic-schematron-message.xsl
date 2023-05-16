<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright 2004-2009 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose
such Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias"
                xmlns:curam="http://www.curamsoftware.com/curamtron"
                exclude-result-prefixes="curam">

  <xsl:import href="skeleton1-5.xsl"/>

  <xsl:template name="process-prolog">
    <axsl:include href="cp:curam/util/common/uim/validator/string-utils.xsl" />
    <axsl:output method="text" />
    <axsl:variable name="chars-to-escape" select="'\:()|'" />
  </xsl:template>

  <!--
  This template generates an xsl message that includes the file name and line
  number inserted in every element by the validating parser. It also includes
  the name of the element under test and the error code (which is a reference
  to an error message). The format of the message is:

    severity:system-id:line-no:col-no:element:attribute:message-name(arguments)

  The arguments are separated by "|" characters. Some of the fields are not
  used at present.
  -->
  <xsl:template name="process-message">
    <xsl:param name="pattern" />
    <xsl:param name="role" />
    <axsl:message>
      <!-- No severity. -->
      <axsl:text>:</axsl:text>
      <axsl:call-template name="escape-string">
        <axsl:with-param name="string">
          <axsl:value-of select="@__file"/>
        </axsl:with-param>
        <axsl:with-param name="chars-to-escape" select="$chars-to-escape" />
      </axsl:call-template>
      <axsl:text>:</axsl:text>
      <axsl:call-template name="escape-string">
        <axsl:with-param name="string">
          <axsl:value-of select="@__line"/>
        </axsl:with-param>
        <axsl:with-param name="chars-to-escape" select="$chars-to-escape" />
      </axsl:call-template>
      <axsl:text>:</axsl:text>
      <!-- No column number. -->
      <axsl:text>:</axsl:text>
      <axsl:call-template name="escape-string">
        <axsl:with-param name="string">
          <axsl:value-of select="name()"/>
        </axsl:with-param>
        <axsl:with-param name="chars-to-escape" select="$chars-to-escape" />
      </axsl:call-template>
      <axsl:text>:</axsl:text>
      <!-- No attribute name. -->
      <axsl:text>:</axsl:text>
      <axsl:call-template name="escape-string">
        <axsl:with-param name="string">
          <axsl:text><xsl:value-of select="curam:errorcode"/></axsl:text>
        </axsl:with-param>
        <axsl:with-param name="chars-to-escape" select="$chars-to-escape" />
      </axsl:call-template>
      <axsl:text>:</axsl:text>
      <xsl:call-template name="process-args"/>
    </axsl:message>
  </xsl:template>

  <xsl:template name="process-args">
    <axsl:text>(</axsl:text>
      <xsl:apply-templates select="curam:append" />
    <axsl:text>)</axsl:text>
  </xsl:template>

  <!--
  Inserts a "value-of" element to evaluate an XPath expression that will be
  appended to the list of error message arguments. As an XPath expression is
  expected, literal strings must be surrounded by single quotation marks.
  -->
  <xsl:template match="curam:append">
    <axsl:call-template name="escape-string">
      <axsl:with-param name="string">
        <axsl:value-of select="{text()}"/>
      </axsl:with-param>
      <axsl:with-param name="chars-to-escape" select="$chars-to-escape" />
    </axsl:call-template>
    <xsl:if test="position() != last()">
      <axsl:text>|</axsl:text>
    </xsl:if>
  </xsl:template>

  <!--
  Inserts a variable declaration that can be used in the rules.
  -->
  <xsl:template match="curam:variable">
    <axsl:variable name="{@name}" select="{@select}" />
  </xsl:template>

</xsl:stylesheet>
