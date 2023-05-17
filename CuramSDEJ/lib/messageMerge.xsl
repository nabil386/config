<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!-- Copyright 2002-2011 Curam Software Ltd.
 All rights reserved.
 This software is the confidential and proprietary information of Curam
 Software, Ltd. ("Confidential Information").  You shall not disclose such
 Confidential Information and shall use it only in accordance with the
 terms of the license agreement you entered into with Curam Software. -->
    <!-- This XSLT merges a message file; the main message file, with a delta
     message file; the merge file, to produce a new message file.
     Duplicate message names are copied depending on precedence; the main
     message file has the highest precedence. -->
    <xsl:param name="mergeFileName"/>
    <xsl:param name="reportDeprecation"/>
    <xsl:preserve-space elements="messages message locales"/>
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    <!-- Assign the main source file -->
    <xsl:variable name="mainfileroot" select="/"/>
    <!--Load the merge file (the lower priority component's file). -->
    <xsl:variable name="mergefile" select="document($mergeFileName)"/>
    <!-- Assign the list of message names from the main message file -->
    <xsl:variable name="mainMessageList" select="$mainfileroot/messages/message"/>
    <!-- Start to combine the files -->
    <xsl:template match="messages">
        <xsl:copy>
            <!-- Copy the attributes from the main message file messages tag
           including the schema. -->
            <xsl:apply-templates select="@*|node()"/>
            <xsl:call-template name="new_messages"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="@*|*">
        <xsl:copy-of select="."/>
    </xsl:template>

    <xsl:template match="message">
        <!-- Copy the message node and its attributes in the main file to the
         result file. -->
        <xsl:variable name="this-message" select="."/>
        <xsl:variable name="lower-message"
          select="$mergefile/messages/message[@name = $this-message/@name]"/>
        <!-- Copy the message tag and its attributes in the main file to the
         result file. -->
        <xsl:choose>
          <!-- Does "mergefile" contain a matching element? -->
          <xsl:when test="$lower-message">
            <!-- Copy any deprecation PI from mergefile's element. -->
            <xsl:for-each select="$lower-message/preceding-sibling::node()
                              [self::* or self::processing-instruction('curam-deprecated')][1]
                            /self::processing-instruction()">
              <xsl:copy/>
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
            <!-- Copy any deprecation PI from this element. -->
            <xsl:for-each select="$this-message/preceding-sibling::node()
                              [self::* or self::processing-instruction('curam-deprecated')][1]
                            /self::processing-instruction()">
              <xsl:copy/>
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:copy-of select="locale"/>

            <xsl:for-each select="$lower-message">

              <xsl:if test="$reportDeprecation">
               <xsl:if test="preceding-sibling::node()
                              [self::* or self::processing-instruction('curam-deprecated')][1]
                            /self::processing-instruction()">
                <xsl:if test="not($this-message/preceding-sibling::node()
                              [self::* or self::processing-instruction('curam-deprecated')][1]
                            /self::processing-instruction())">
                  <xsl:message>
<xsl:value-of select="$mergeFileName"/>:
warning: [deprecation][customization] <xsl:value-of select="@name"/> has been deprecated, but is overridden by another component in the server component order.
[deprecation comment] <xsl:value-of select="preceding-sibling::node()
                         [self::* or self::processing-instruction('curam-deprecated')][1]
                       /self::processing-instruction()"/>
                  </xsl:message>
                </xsl:if>
               </xsl:if>
              </xsl:if>

              <xsl:for-each select="./locale">
                <!-- Merge the message if its locale country and language are different
                     to those in this message. -->
                <xsl:if test="not($this-message/locale[((@language = current()/@language)
                    and ((not(@country) and not(current()/@country)) or
                    (not(@country) and current()/@country='') or
                    (@country='' and not(current()/@country)) or
                    (@country = current()/@country)))])">
                    <xsl:copy-of select="."/>
                </xsl:if>
              </xsl:for-each>

            </xsl:for-each>

            <xsl:copy-of select="cause"/>
            <xsl:copy-of select="action"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template name="new_messages">
        <!-- Merge in all new messages from the merge file that do not appear in the
         main message file. -->
        <xsl:for-each select="$mergefile//messages/message[not(@name = $mainMessageList/@name)]">
            <xsl:for-each select="preceding-sibling::node()
                              [self::* or self::processing-instruction('curam-deprecated')][1]
                            /self::processing-instruction()">
              <xsl:copy/>
            </xsl:for-each>
            <xsl:copy-of select="."/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
