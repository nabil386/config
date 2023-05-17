<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="org_tree_config" match="/wrapper">
    <LAZY_TREE>
      <xsl:call-template name="unique_lazy_config"/>
    </LAZY_TREE>
  </xsl:template>

  <xsl:template name="unique_lazy_config">
    <xsl:copy-of select="/wrapper/LAZY_TREE/CONFIG[not(@NAME = preceding::CONFIG/@NAME)]" />
  </xsl:template>

</xsl:stylesheet>