<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="meeting_view_config" match="/wrapper">
    <SCHEDULE_CONFIG>
      <xsl:call-template name="unique_configs"/>
    </SCHEDULE_CONFIG>
  </xsl:template>

  <xsl:template name="unique_configs">
    <xsl:copy-of select="/wrapper/SCHEDULE_CONFIG/CONFIG[not(@TYPE = preceding::CONFIG/@TYPE)]" />
  </xsl:template>

</xsl:stylesheet>