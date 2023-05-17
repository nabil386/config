<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <HELP>
      <xsl:apply-templates select="APP_CONFIG/HELP"/>
    </HELP>
  </xsl:template>
  <xsl:template match="HELP">
    <xsl:choose>
      <xsl:when test="@INCLUDE and not(@INCLUDE='false')">
        <xsl:attribute name="INCLUDE">
          <xsl:value-of select="'true'"/>
        </xsl:attribute>
        <xsl:if test="@ACCESSKEY">
          <xsl:attribute name="ACCESSKEY"><xsl:value-of select="@ACCESSKEY"/></xsl:attribute>
        </xsl:if>
        <xsl:if test="@TYPE">
	  <xsl:attribute name="TYPE"><xsl:value-of select="@TYPE"/></xsl:attribute>
        </xsl:if>
        <xsl:if test="@ICON and not(@ICON='false')">
          <xsl:attribute name="ICON">
            <xsl:choose>
              <xsl:when test="@ICON_KEY">
                <xsl:value-of select="@ICON_KEY"/>
              </xsl:when>
              <xsl:when test="ICON">
                <xsl:value-of select="ICON"/>
              </xsl:when>
            </xsl:choose>
          </xsl:attribute>
        </xsl:if>
        <xsl:variable name="text">
          <xsl:choose>
            <xsl:when test="@TEXT_KEY">
              <xsl:value-of select="@TEXT_KEY"/>
            </xsl:when>
            <xsl:when test="TEXT">
              <xsl:value-of select="TEXT"/>
            </xsl:when>
          </xsl:choose>
        </xsl:variable>
        <xsl:if test="@TEXT and not(@TEXT='false')">
          <xsl:attribute name="TEXT">
            <xsl:value-of select="$text"/>
          </xsl:attribute>
        </xsl:if>
        <xsl:attribute name="ALT">
          <xsl:value-of select="$text"/>
        </xsl:attribute>
        <xsl:attribute name="NEW_WINDOW">
          <xsl:choose>
            <xsl:when test="@NEW_WINDOW and @NEW_WINDOW='false'">
              <xsl:value-of select="'false'"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="'true'"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="INCLUDE">
          <xsl:value-of select="'false'"/>
        </xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>