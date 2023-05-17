<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template name="workflow_config" match="/wrapper">
    <WORKFLOW_CONFIG>
      <xsl:call-template name="unique_icon_code"/>
      <xsl:call-template name="icon_notification"/>
      <xsl:call-template name="unique_configs"/>
    </WORKFLOW_CONFIG>
  </xsl:template>

  <xsl:template name="unique_icon_code">
    <xsl:copy-of select="/wrapper/WORKFLOW_CONFIG/ICON[not(@CODE = preceding::ICON/@CODE)
                         and not(@NOTIFICATION)]" />
  </xsl:template>

  <xsl:template name="icon_notification">
    <xsl:copy-of select="(/wrapper/WORKFLOW_CONFIG/ICON[@NOTIFICATION='true'])[1]" />
  </xsl:template>

  <xsl:template name="unique_configs">
    <xsl:copy-of select="/wrapper/WORKFLOW_CONFIG/CONFIG[not(@ID = preceding::CONFIG/@ID)]" />
  </xsl:template>

</xsl:stylesheet>