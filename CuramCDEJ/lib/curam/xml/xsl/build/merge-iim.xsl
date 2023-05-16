<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<xsl:template match="/wrapper">
		<IMAGES>
			<xsl:call-template name="image"/>
		</IMAGES>
	</xsl:template>
	<xsl:template name="image">
		<xsl:if test="count(/wrapper/IMAGES/IMAGE) &gt; 0">
		  <xsl:apply-templates select="//IMAGE"/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="IMAGE">
		<xsl:if test="not(following::IMAGE[@FILE_NAME=current()/@FILE_NAME])">
			<xsl:copy-of select="."/>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
