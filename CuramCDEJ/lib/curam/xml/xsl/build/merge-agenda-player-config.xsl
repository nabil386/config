<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="agenda_config" match="/wrapper">
    <AGENDA>
      <xsl:call-template name="unique_agenda_configs"/> 
    </AGENDA>
  </xsl:template>

  <xsl:template name="unique_agenda_configs">
    <xsl:copy-of select="/wrapper/AGENDA/PLAYER[not(@ID = preceding::PLAYER/@ID)]" />
  </xsl:template> 

</xsl:stylesheet>