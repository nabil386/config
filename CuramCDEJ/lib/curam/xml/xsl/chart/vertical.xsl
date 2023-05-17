<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:import href="../common/runtime-params.xsl"/>
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />
  
  <xsl:param name="locale-code"/>
  <xsl:param name="config-id"/>
  <xsl:param name="config-file"/>
  <xsl:param name="x-axis-label"/>
  <xsl:param name="y-axis-label"/>
  <xsl:param name="map-id"/>
  <xsl:param name="height"/>
  <xsl:param name="width"/>
  <xsl:param name="pane-width"/>
  
  <xsl:variable name="curr-config" 
       select="$config-file/CHART-CONFIG/CONFIG[@ID = $config-id]"/>

  <xsl:template match="CHART">

    <div style="{concat('width: ', $pane-width, 'px;height:', $height + 20, 'px;position: relative;overflow: auto;')}">
      <img alt="" usemap="{concat('#', $map-id)}">
        <xsl:attribute name="src">
          <xsl:text>../servlet/ChartImageGenerator?tp=v</xsl:text>
          <xsl:value-of select="concat('&amp;ht=', $height, '&amp;wt=', $width)"/>
          <xsl:if test="$x-axis-label">
            <xsl:value-of select="concat('&amp;xl=', $x-axis-label)"/>
          </xsl:if>
          <xsl:if test="$y-axis-label">
            <xsl:value-of select="concat('&amp;yl=', $y-axis-label)"/>
          </xsl:if>            
          <xsl:if test="$curr-config/@FOREGROUND_COLOR">
            <xsl:value-of select="concat('&amp;fg=', $curr-config/@FOREGROUND_COLOR)"/>
          </xsl:if>
          <xsl:if test="$curr-config/@BACKGROUND_COLOR">
            <xsl:value-of select="concat('&amp;bg=', $curr-config/@BACKGROUND_COLOR)"/>
          </xsl:if>
          <xsl:if test="$curr-config/@BORDER_COLOR">
            <xsl:value-of select="concat('&amp;bo=', $curr-config/@BORDER_COLOR)"/>
          </xsl:if>
          <xsl:if test="$curr-config/@MAX_VALUE">
            <xsl:value-of select="concat('&amp;mx=', $curr-config/@MAX_VALUE)"/>
          </xsl:if>
          <xsl:if test="$curr-config/@MIN_INCREMENT">
            <xsl:value-of select="concat('&amp;ni=', $curr-config/@MIN_INCREMENT)"/>
          </xsl:if>
          <xsl:if test="$curr-config/@MAX_INCREMENT">
            <xsl:value-of select="concat('&amp;ji=', $curr-config/@MAX_INCREMENT)"/>
          </xsl:if>          
          <xsl:if test="$curr-config/LEGEND">
            <xsl:for-each select="$curr-config/LEGEND/ITEM">
              <xsl:value-of select="concat('&amp;lg=', position() - 1, '|', @COLOR, '|', @CODE)"/>
            </xsl:for-each>
          </xsl:if>
          <xsl:apply-templates mode="source" select="UNIT"/>
        </xsl:attribute>
      </img>
    </div>
  </xsl:template>

  <xsl:template mode="source" match="UNIT">
    <xsl:variable name="index" select="position() - 1"/>
    <xsl:value-of select="concat('&amp;cp=', $index, '|', CAPTION/@parsed-text)"/>
    <xsl:for-each select="BLOCK">
      <xsl:value-of select="concat('&amp;ln=', $index, '|', @LENGTH, '|', @TYPE)"/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>