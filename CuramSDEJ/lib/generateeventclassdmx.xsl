<xsl:stylesheet version = '1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>

<!-- Copyright 2005 Curam Software Ltd.
  All rights reserved.
  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information").  You shall not disclose such
  Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.

  This XSLT merges a handler registration file; the main file, with a delta file;
  the merge file, to produce a new handler registration file. Duplicate handler
  registrations names are copied depending on precedence; the main file has the
  highest precedence.
-->

  <xsl:output method="xml" indent="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:template match="events">
    <xsl:element name="table">
    
      <xsl:attribute name="name">EVENTCLASS</xsl:attribute>
      
      <xsl:element name="column">
        <xsl:attribute name="name">EVENTCLASS</xsl:attribute>
        <xsl:attribute name="type">text</xsl:attribute>
      </xsl:element>
      <xsl:element name="column">
        <xsl:attribute name="name">VERSIONNO</xsl:attribute>
        <xsl:attribute name="type">number</xsl:attribute>
      </xsl:element>
      
      <xsl:for-each select="event-class">
        <xsl:element name="row">

        <xsl:element name="attribute">
          <xsl:attribute name="name">EVENTCLASS</xsl:attribute>
          <xsl:element name="value">
            <xsl:value-of select="./@value" />
          </xsl:element>
        </xsl:element>
        <xsl:element name="attribute">
          <xsl:attribute name="name">VERSIONNO</xsl:attribute>
          <xsl:element name="value">1</xsl:element>
        </xsl:element>
        
        </xsl:element>
      </xsl:for-each>
      
    </xsl:element>

  </xsl:template>

</xsl:stylesheet>