<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:x="http://www.curamsoftware.com/
     schemas/DifferenceCommand"
    xmlns:fn="http://www.w3.org/2006/xpath-functions"
      version="2.0">
    <xsl:output indent="yes"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="update">
        <xsl:for-each select="./diff[@entityType='Application']">
            <xsl:element name="client-data">
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="diff[@entityType='Person']">
        <xsl:element name="client">
            <xsl:attribute name="localID">
                <xsl:value-of select="./@identifier"/>
            </xsl:attribute>
            <xsl:element name="evidence">
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="diff[@entityType='Resource']">
        <xsl:element name="entity">
            <!-- Evidence type Income -->
            <xsl:attribute name="type">DET0038000</xsl:attribute>
            <xsl:attribute name="action">
                <xsl:value-of select="./@diffType"/>
            </xsl:attribute>
            <xsl:attribute name="localID">
                <xsl:value-of select="./@identifier"/>
            </xsl:attribute>
            <xsl:for-each select="./attribute">
                <xsl:copy-of select="."/>
            </xsl:for-each>


        </xsl:element>
    </xsl:template>


    <xsl:template match="*">
        <!-- do nothing -->
    </xsl:template>
</xsl:stylesheet>
