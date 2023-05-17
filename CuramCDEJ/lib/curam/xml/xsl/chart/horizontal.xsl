<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="../common/runtime-params.xsl"/>
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />

  <xsl:param name="locale-code"/>
  <xsl:param name="config-id"/>
  <xsl:param name="config-file"/>
  <xsl:param name="caption"/>
  <xsl:param name="map-id"/>
  <xsl:param name="height"/>
  <xsl:param name="width"/>

  <!-- -->

  <xsl:variable name="curr-config" 
       select="$config-file/CHART-CONFIG/CONFIG[@ID = $config-id]"/>
  <xsl:variable name="border">
    <xsl:choose>
      <xsl:when test="$curr-config/@BORDER_COLOR">
        <xsl:value-of select="$curr-config/@BORDER_COLOR"/>
      </xsl:when>
      <xsl:otherwise>cfdae6</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="foreground">
    <xsl:choose>
      <xsl:when test="$curr-config/@FOREGROUND_COLOR">
        <xsl:value-of select="$curr-config/@FOREGROUND_COLOR"/>
      </xsl:when>
      <xsl:otherwise>336699</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="background">
    <xsl:choose>
      <xsl:when test="$curr-config/@BACKGROUND_COLOR">
        <xsl:value-of select="$curr-config/@BACKGROUND_COLOR"/>
      </xsl:when>
      <xsl:otherwise>f0f2f5</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="caption-background">
    <xsl:choose>
      <xsl:when test="$curr-config/@CAPTION_BACKGROUND">
        <xsl:value-of select="$curr-config/@CAPTION_BACKGROUND"/>
      </xsl:when>
      <xsl:otherwise>81a0bf</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="caption-foreground">
    <xsl:choose>
      <xsl:when test="$curr-config/@CAPTION_FOREGROUND">
        <xsl:value-of select="$curr-config/@CAPTION_BACKGROUND"/>
      </xsl:when>
      <xsl:otherwise>ffffff</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <xsl:variable name="pane-height">
    <xsl:choose>
      <xsl:when test="$height > 156">
        <xsl:value-of select="number(156)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="number($height + 20)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:template match="CHART">
    <xsl:if test="$caption">
     <div>
       <xsl:attribute name="style">
         <xsl:value-of select="concat('background-color: #', $caption-background,
          '; color: #', $caption-foreground, ';width: ', 
          $width, 'px; height: 26px;padding-top: 6px;padding-left: 6px;',
          'font-weight: bold')"/>
       </xsl:attribute>
       <xsl:value-of select="$caption"/>
     </div>
   </xsl:if>
   <div style="{concat('width: ', $width, 'px;height:',
            $pane-height, 'px;position: relative;overflow: auto;',
            'background-color: #', $border, ';')}">
  			<img alt="" usemap="{concat('#', $map-id)}">  
         <xsl:attribute name="src">
           <xsl:text>../servlet/ChartImageGenerator?tp=h</xsl:text>
           <xsl:value-of select="concat('&amp;ht=', $height, '&amp;wt=', $width - 16, 
                                '&amp;bg=', $background, '&amp;fg=', $foreground,
                                '&amp;bo=', $border)"/>
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