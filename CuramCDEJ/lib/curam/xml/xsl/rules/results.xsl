<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />

  <xsl:param name="static-content-server-url"/>
  <xsl:param name="success-alt-text"/>
  <xsl:param name="failure-alt-text"/>
  
  <xsl:template match="decision">
    <div class="rules">
      <xsl:apply-templates select="rule-node"/>
    </div>
  </xsl:template>
  <xsl:template match="rule-node">
    <xsl:variable name="alt-text">
      <xsl:choose>
       <xsl:when test="@result = 'F'">
         <xsl:value-of select="concat(@ALT_TEXT, ' ', $failure-alt-text)" />
       </xsl:when>
       <xsl:otherwise>
         <xsl:value-of select="concat(@ALT_TEXT, ' ', $success-alt-text)" />
       </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <div>
      <xsl:choose>
        <xsl:when test="not(parent::decision)">
          <xsl:choose>
            <xsl:when test="count(ancestor::*)=2">
              <xsl:attribute name="style">margin-left:24px;border:transparent dotted 1px;</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="class">indent</xsl:attribute>
              <xsl:attribute name="style">border:transparent dotted 1px;</xsl:attribute>  
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="style">margin-left:24px;border:transparent dotted 1px;</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <img src="{concat($static-content-server-url, '/themes/classic/images/rules/', @ICON)}" 
           alt="{$alt-text}"/>
      <span>
        <xsl:if test="@result='F' and @highlight='true'">
          <xsl:attribute name="class">
              <xsl:text>highlight</xsl:text>
          </xsl:attribute>
        </xsl:if>
        <xsl:if test="name(..)='decision'">
          <xsl:attribute name="style">
            <xsl:text>font-weight: bold</xsl:text>
          </xsl:attribute>
        </xsl:if>
        <xsl:apply-templates select="." mode="print-rule-text" />
      </span>
      <xsl:apply-templates select="rule-node"/>
    </div>
  </xsl:template>
  <xsl:template match="rule-node" mode="print-rule-text">
    <xsl:value-of select="@text"/>
  </xsl:template>
</xsl:stylesheet>