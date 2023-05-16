<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="agenda_config" match="/wrapper">
    <HEATMAP_CONFIG>
      <xsl:call-template name="unique_heatmap_configs"/> 
    </HEATMAP_CONFIG>
  </xsl:template>

  <xsl:template name="unique_heatmap_configs">
    <xsl:copy-of select="/wrapper/HEATMAP_CONFIG/CONFIG[not(@ID = preceding::CONFIG/@ID)]" />
  </xsl:template> 

</xsl:stylesheet>