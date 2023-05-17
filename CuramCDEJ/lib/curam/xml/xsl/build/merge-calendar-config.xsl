<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/">

		<xsl:variable name="cellHeight"
		              select="/wrapper/CONFIGURATION[@MONTH_CELL_HEIGHT][1]/@MONTH_CELL_HEIGHT"/>
		<xsl:variable name="showRepeat"
		              select="/wrapper/CONFIGURATION[@SHOW_REPEAT_EVENT_TEXT][1]/@SHOW_REPEAT_EVENT_TEXT"/>

		<xsl:choose>
			<xsl:when test="$cellHeight and $showRepeat">
				<CONFIGURATION MONTH_CELL_HEIGHT="{$cellHeight}"
											 SHOW_REPEAT_EVENT_TEXT="{$showRepeat}">
					<xsl:call-template name="unique_calendar_type"/>
					<EVENT_TYPES>
					  <xsl:call-template name="unique_event_type"/>
          </EVENT_TYPES>
				</CONFIGURATION>
			</xsl:when>
			<xsl:when test="$cellHeight">
				<CONFIGURATION MONTH_CELL_HEIGHT="{$cellHeight}">
					<xsl:call-template name="unique_calendar_type"/>
					<EVENT_TYPES>
					  <xsl:call-template name="unique_event_type"/>
          </EVENT_TYPES>
				</CONFIGURATION>
			</xsl:when>
			<xsl:when test="$showRepeat">
				<CONFIGURATION SHOW_REPEAT_EVENT_TEXT="{$showRepeat}">
					<xsl:call-template name="unique_calendar_type"/>
					<EVENT_TYPES>
					  <xsl:call-template name="unique_event_type"/>
          </EVENT_TYPES>
			  </CONFIGURATION>
			</xsl:when>
			<xsl:otherwise>
				<CONFIGURATION>
					<xsl:call-template name="unique_calendar_type"/>
					<EVENT_TYPES>
					  <xsl:call-template name="unique_event_type"/>
          </EVENT_TYPES>
				</CONFIGURATION>
			</xsl:otherwise>
		</xsl:choose>
  </xsl:template>

  <xsl:template name="unique_calendar_type">
    <xsl:copy-of select="/wrapper/CONFIGURATION/CALENDAR[not(@TYPE = preceding::CALENDAR/@TYPE)]" />
  </xsl:template>

  <xsl:template name="unique_event_type">
    <xsl:copy-of select="/wrapper/CONFIGURATION/EVENT_TYPES/TYPE[not(@NAME = preceding::TYPE/@NAME)]" />
  </xsl:template>

</xsl:stylesheet>