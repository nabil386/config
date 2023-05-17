<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet version="1.0"
                                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                                xmlns:svg="http://www.w3.org/2000/svg"
                                xmlns:xlink="http://www.w3.org/1999/xlink"
                                xmlns="http://www.w3.org/2000/svg" exclude-result-prefixes="svg xsl xlink" >
  <xsl:output method="xml" cdata-section-elements="svg:style svg:script" encoding="UTF-8"/>
  <xsl:param name="rules-config-file" />
  <xsl:variable name="rules-config" select="document(concat('file:///', $rules-config-file))"/>
  <xsl:param name="rules-editor-config-file"/>
  <xsl:variable name="rules-editor-config" select="document(concat('file:///', $rules-editor-config-file))"/>
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
         <xsl:for-each select="$rules-config/RULES-CONFIG/CONFIG[@ID = $rules-config/RULES-CONFIG/@DEFAULT]/TYPE">
           <xsl:variable name="image-ref">
             <xsl:choose>
               <xsl:when test="@NAME='PRODUCT'">p</xsl:when>
               <xsl:when test="@NAME='ASSESSMENT'">a</xsl:when>
               <xsl:when test="@NAME='SUBRULESET'">srs</xsl:when>
               <xsl:when test="@NAME='OBJECTIVE_GROUP'">og</xsl:when>
               <xsl:when test="@NAME='OBJECTIVE_LIST_GROUP'">olg</xsl:when>
               <xsl:when test="@NAME='OBJECTIVE'">o</xsl:when>
               <xsl:when test="@NAME='RULE_GROUP'">rg</xsl:when>
               <xsl:when test="@NAME='RULE_LIST_GROUP'">rlg</xsl:when>
               <xsl:when test="@NAME='RULE'">r</xsl:when>
             </xsl:choose>
           </xsl:variable>
           <image id="{concat('rS', $image-ref)}" xlink:href="{concat($images-folder, @SUCCESS-ICON)}" x="0" y="0" width="16" height="16"/>
           <image id="{concat('rF', $image-ref)}" xlink:href="{concat($images-folder, @FAILURE-ICON)}" x="0" y="0" width="16" height="16"/>
         </xsl:for-each>
         <xsl:for-each select="$rules-editor-config/RULES-CONFIG/CONFIG[@ID = $rules-editor-config/RULES-CONFIG/@DEFAULT]/TYPE">
             <image id="{concat('eS', @NAME)}" xlink:href="{concat($images-folder, @SUCCESS-ICON)}" x="0" y="0" width="16" height="16"/>
             <image id="{concat('eF', @NAME)}" xlink:href="{concat($images-folder, @FAILURE-ICON)}" x="0" y="0" width="16" height="16"/>
         </xsl:for-each>
      </xsl:element>
  </xsl:template>
</xsl:stylesheet>