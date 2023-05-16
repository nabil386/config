<?xml version="1.0"?>

<!-- Copyright 2006 Curam Software Ltd.

  All rights reserved.
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information").  You shall not disclose such
  Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
  
  This XSLT copies the xml representing a Question Group while replacing the ID given
  as a parameter.
  
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
    <xsl:param name="oldID" />
    <xsl:param name="newID" />
    <xsl:template match="QuestionGroup">
        <xsl:element name="{name()}">
            <xsl:for-each select="@*">
                <xsl:choose>
		<!-- 
                    Copies the QuestionGroup element and its attributes except for 'id' 
                    where the value is replaced. 
                  -->
                    <xsl:when test="name() = 'id'">
                        <xsl:attribute name="{name()}">
                            <xsl:call-template name="replace-id">
                                <xsl:with-param name="string-in" select="." />
                                <xsl:with-param name="chars-in" select="$oldID" />
                                <xsl:with-param name="chars-out" select="$newID" />
                            </xsl:call-template>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="{name()}">
                            <xsl:value-of select="." />
                        </xsl:attribute>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    <xsl:template match="Translation">
        <xsl:element name="{name()}">
            <xsl:for-each select="@*">
                <xsl:choose>
                <!-- 
		    Copies the Translation element and its attributes except for 'value' 
		    in which occurences of 'oldID.' are replaced by 'newID.'. 
                  -->
                    <xsl:when test="name() = 'value'">
                        <xsl:attribute name="{name()}">
                            <xsl:call-template name="replace-chars-in-string">
                                <xsl:with-param name="string-in" select="." />
                                <xsl:with-param name="chars-in" select="concat($oldID,'.')" />
                                <xsl:with-param name="chars-out" select="concat($newID,'.')" />
                            </xsl:call-template>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:attribute name="{name()}">
                            <xsl:value-of select="." />
                        </xsl:attribute>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
    <!-- Copies the rest. -->
    <xsl:template match="@*|node( )">
        <xsl:copy>
            <xsl:apply-templates select="@*|node( )" />
        </xsl:copy>
    </xsl:template>
    <!--
        Replaces the characters specified by the chars-in parameter with the 
        chars-out parameter.
        
        $string-in     The string to replace characters for.
        $chars-in      The character sequence to replace.
        $chars-out     The replacement characters.
      -->
    <xsl:template name="replace-chars-in-string">
        <xsl:param name="string-in" />
        <xsl:param name="chars-in" />
        <xsl:param name="chars-out" />
        <xsl:choose>
            <xsl:when test="contains($string-in, $chars-in)">
                <xsl:value-of select="concat(substring-before($string-in, $chars-in), $chars-out)" />
                <xsl:call-template name="replace-chars-in-string">
                    <xsl:with-param name="string-in" select="substring-after($string-in, $chars-in)" />
                    <xsl:with-param name="chars-in" select="$chars-in" />
                    <xsl:with-param name="chars-out" select="$chars-out" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string-in" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
        Replaces the id specified by the chars-in parameter with the 
        chars-out parameter.
        
        $string-in     The string to replace characters for.
        $chars-in      The character sequence to replace.
        $chars-out     The replacement characters.
      -->
    <xsl:template name="replace-id">
        <xsl:param name="string-in" />
        <xsl:param name="chars-in" />
        <xsl:param name="chars-out" />
        <xsl:choose>
            <xsl:when test="$string-in = $chars-in">
                <xsl:value-of select="$chars-out" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string-in" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
