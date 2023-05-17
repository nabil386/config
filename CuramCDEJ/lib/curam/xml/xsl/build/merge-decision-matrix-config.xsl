<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="decision_matrix_config" match="/wrapper">
    <DECISION-MATRIX-CONFIG DEFAULT="{(/wrapper/DECISION-MATRIX-CONFIG)[1]/@DEFAULT}">
      <xsl:call-template name="unique_configs"/> 
    </DECISION-MATRIX-CONFIG>
  </xsl:template>

  <xsl:template name="unique_configs">
    <xsl:copy-of select="/wrapper/DECISION-MATRIX-CONFIG/CONFIG[not(@ID = preceding::CONFIG/@ID)]" />
  </xsl:template> 

</xsl:stylesheet>