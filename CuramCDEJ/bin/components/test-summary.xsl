<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Copyright © 2006 Curam Software Ltd.
All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information"). You shall not disclose such
Confidential Information and shall use it only in accordance with the
terms of the license agreement you entered into with Curam Software.
-->
<!--
Extracts some basic information from a JUnit XML-format test report and
creates a properties file that can be included by Ant.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output encoding="ISO-8859-1" method="text" />

  <xsl:template match="/">
    <xsl:apply-templates select="testsuites" />
  </xsl:template>

  <!--
  Usually there is only one test suite, but suites may be merged together, so
  we need to add up all of the results.
  -->
  <xsl:template match="testsuites">
    <xsl:variable name="tests" select="sum(testsuite/@tests)"/>
    <xsl:variable name="errors" select="sum(testsuite/@errors)"/>
    <xsl:variable name="failures" select="sum(testsuite/@failures)"/>

    <xsl:text>test.tests=</xsl:text>
    <xsl:value-of select="$tests"/>
    <xsl:text>&#xa;</xsl:text>

    <xsl:text>test.errors=</xsl:text>
    <xsl:value-of select="$errors"/>
    <xsl:text>&#xa;</xsl:text>

    <xsl:text>test.failures=</xsl:text>
    <xsl:value-of select="$failures"/>
    <xsl:text>&#xa;</xsl:text>

    <xsl:text>test.pass.rate=</xsl:text>
    <xsl:value-of
      select="format-number(($tests - $errors - $failures) div $tests,
                            '##.#%')"/>
    <xsl:text>&#xa;</xsl:text>

    <xsl:text>test.fail.rate=</xsl:text>
    <xsl:value-of
      select="format-number(($errors + $failures) div $tests, '##.#%')"/>
    <xsl:text>&#xa;</xsl:text>
  </xsl:template>

</xsl:stylesheet>
