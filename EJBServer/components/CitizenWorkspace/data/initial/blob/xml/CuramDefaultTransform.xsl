<?xml version="1.0" encoding="UTF-8"?>
<!-- This script plucks out and copies all resource-related entities from output built by the XMLApplicationBuilder -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:x="http://www.curamsoftware.com/schemas/ClientEvidence" version="2.0">
  <xsl:output indent="yes"/>
  <xsl:template match="/x:client-data">
    <xsl:element name="Application">
      
      <xsl:for-each select="./x:client">
        <xsl:element name="Person">
          
        <xsl:attribute name="localID">
          <xsl:value-of select="./@localID"/>   
        </xsl:attribute>
          
          <xsl:attribute name="isPrimaryParticipant">true</xsl:attribute>
          
        </xsl:element>
      </xsl:for-each>
      
    </xsl:element>
  </xsl:template>
  
  <!-- Override any default match all behaviour -->
  <xsl:template match="*">
    <!-- do nothing -->
  </xsl:template>
</xsl:stylesheet>
