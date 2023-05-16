<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:svg="http://www.w3.org/2000/svg"
				xmlns:xlink="http://www.w3.org/1999/xlink"
				xmlns="http://www.w3.org/2000/svg" exclude-result-prefixes="svg xsl xlink" >
  <xsl:output method="xml" cdata-section-elements="svg:style svg:script" encoding="UTF-8"/>
  <xsl:param name="workflow-config-file" />
  <xsl:variable name="workflow-config" select="document(concat('file:///', $workflow-config-file))"/>
  <xsl:variable name="images-folder" select="'../../'"/>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  <!-- Template to add group nodes to the definitions section so images are reused within the svg document.
        This also allows us to easily associate an icon with its pop-up text.
    -->
  <xsl:template match="svg:defs">
      <xsl:element name="defs">
         <xsl:apply-templates/>
         <xsl:for-each select="$workflow-config/WORKFLOW_CONFIG/ICON">
           <xsl:choose>
             <xsl:when test="@CODE">
               <image id="{concat('_i', @CODE)}" xlink:href="{concat($images-folder,
                   @PATH)}" x="0" y="0" width="32" height="32"/>
             </xsl:when>
             <xsl:when test="@NOTIFICATION">
               <image id="notify-icon" xlink:href="{concat($images-folder,
                   @PATH)}" x="0" y="0" width="12" height="9"/>
             </xsl:when>
           </xsl:choose>
         </xsl:for-each>
      </xsl:element>
  </xsl:template>
</xsl:stylesheet>