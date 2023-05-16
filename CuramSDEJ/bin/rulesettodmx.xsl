<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
  <xsl:param name="definition"/>
  <xsl:param name="language"/>
  <xsl:template match="/">
    <xsl:element name="table">
      <xsl:attribute name="name">RULESETINFORMATION</xsl:attribute>
      <xsl:element name="column">
        <xsl:attribute name="name">RULESETID</xsl:attribute>
        <xsl:attribute name="type">text</xsl:attribute>
      </xsl:element>
      <xsl:element name="column">
        <xsl:attribute name="name">DEFINITION</xsl:attribute>
        <xsl:attribute name="type">clob</xsl:attribute>
      </xsl:element>
      <xsl:element name="column">
        <xsl:attribute name="name">NAME</xsl:attribute>
        <xsl:attribute name="type">text</xsl:attribute>
      </xsl:element>
      <xsl:element name="column">
        <xsl:attribute name="name">TYPE</xsl:attribute>
        <xsl:attribute name="type">text</xsl:attribute>
      </xsl:element>
      <xsl:element name="column">
        <xsl:attribute name="name">VERSIONNO</xsl:attribute>
        <xsl:attribute name="type">number</xsl:attribute>
      </xsl:element>
      <xsl:variable name="path" select="RuleSet/*[1]"/>
      <xsl:element name="row">
        <xsl:element name="attribute">
          <xsl:attribute name="name">RULESETID</xsl:attribute>
          <xsl:element name="value">
            <xsl:text>'</xsl:text><xsl:value-of select="$path/@id"/><xsl:text>'</xsl:text>
          </xsl:element>
        </xsl:element>
        <xsl:element name="attribute">
          <xsl:attribute name="name">DEFINITION</xsl:attribute>
          <xsl:element name="value">
            <xsl:value-of select="'./clob/'"/>
            <xsl:value-of select="$definition"/>
          </xsl:element>
        </xsl:element>
        <xsl:element name="attribute">
          <xsl:attribute name="name">NAME</xsl:attribute>
          <xsl:element name="value">
            <xsl:variable name="langParam" select="$path/Label/RuleName/Text[@locale=$language]/@value"/>
            <xsl:choose>
              <xsl:when test="string-length($langParam)!=0 ">
                <xsl:value-of select="$langParam"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$path/Label/RuleName/Text[1]/@value"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:element>
        <xsl:element name="attribute">
          <xsl:attribute name="name">TYPE</xsl:attribute>
          <xsl:element name="value">
            <xsl:choose>
              <xsl:when test="name($path)='Product' ">RS1</xsl:when>
              <xsl:when test="name($path)='SubRuleSet' ">RS2</xsl:when>
              <xsl:when test="name($path)='GenericRuleSet' ">RS3</xsl:when>
              <xsl:otherwise>RS1</xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:element>
        <xsl:element name="attribute">
          <xsl:attribute name="name">VERSIONNO</xsl:attribute>
          <xsl:element name="value">1</xsl:element>
        </xsl:element>
      </xsl:element>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
