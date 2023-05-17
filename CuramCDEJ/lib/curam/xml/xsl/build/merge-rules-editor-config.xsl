<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="rules_config" match="/wrapper">
    <RULES-CONFIG DEFAULT="{(/wrapper/RULES-CONFIG)[1]/@DEFAULT}">
      <xsl:call-template name="unique_configs"/> 
    </RULES-CONFIG>
  </xsl:template>

  <xsl:template name="unique_configs">
    <xsl:copy-of select="/wrapper/RULES-CONFIG/CONFIG[not(@ID = preceding::CONFIG/@ID)]" />
  </xsl:template> 

</xsl:stylesheet>