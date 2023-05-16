<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:nc="http://www.curamsoftware.com/curam/jde/client/widget/nc">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="tree_control_config" match="/wrapper">
    <nc:node-config>
      <xsl:call-template name="unique_node_info"/>
    </nc:node-config>
  </xsl:template>

  <xsl:template name="unique_node_info">
    <xsl:copy-of select="/wrapper/nc:node-config/nc:node-info[not(@name = preceding::nc:node-info/@name)]" />
  </xsl:template>

</xsl:stylesheet>