<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:svg="http://www.w3.org/2000/svg"
				xmlns:xlink="http://www.w3.org/1999/xlink"
				xmlns="http://www.w3.org/2000/svg" 
				exclude-result-prefixes="svg xsl" >
  <xsl:output method="xml" cdata-section-elements="svg:style svg:script" encoding="UTF-8"/>

  <xsl:template match="node()|@*">
    <svg>
      <xsl:apply-templates mode="copy"/>
    </svg>
  </xsl:template>
  
  <!-- All definitions sections are kept as they might hold font or style 
       information. -->
  <xsl:template match="svg:defs" mode="copy">
    <xsl:copy-of select="."/>
  </xsl:template>

  <!-- Copy all attributes of the text section and leave a space in the
       text node to ensure we can insert text dynamically. -->
  <xsl:template match="svg:text[@id='omega3Text']" mode="copy">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:text> </xsl:text>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>