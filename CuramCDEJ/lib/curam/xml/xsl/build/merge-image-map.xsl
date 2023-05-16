<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <xsl:output method="xml" indent="yes"/>
  <xsl:key name = "myKey" match = "locale" use = "parent::domain/@name" />
  <xsl:key name = "mappingKey" match = "mapping" use = "concat(../../@name,../@name)" />

  <xsl:template match="/wrapper">
    <xsl:variable name="unique-domains" select="map/domain[not(@name = preceding::domain/@name)]" />
    <map>
      <xsl:for-each select="$unique-domains">
        <xsl:variable name="domain" select="@name"/>
        <domain name="{$domain}">
          <xsl:apply-templates select = "key('myKey',@name)[not(@name = preceding::domain[@name=$domain]/locale/@name)]" /> 
        </domain>
      </xsl:for-each>
    </map>
  </xsl:template>

  <xsl:template match="locale">
    <xsl:variable name="locale" select="@name"/>
    <xsl:variable name="domain" select="../@name"/>
    <locale name="{@name}">
      <xsl:apply-templates select="key('mappingKey',concat($domain,$locale))[not(@value = preceding::domain[@name=$domain]/locale[@name=$locale]/mapping/@value)]"/>
    </locale>
  </xsl:template>

  <xsl:template match="mapping">
    <xsl:copy-of select="."/>
  </xsl:template>

</xsl:stylesheet>