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

  <xsl:output method="text"/>
  <xsl:strip-space elements="*"/>
  <xsl:param name="package"/>

  <xsl:template match="/">
  
<xsl:text>package </xsl:text><xsl:value-of select="$package"/>
<xsl:text>;
</xsl:text>
    <xsl:apply-templates select="event-class"/>
    
  </xsl:template>

  <xsl:template match="event-class">

<xsl:text>/**
</xsl:text>
<xsl:text> * Generated </xsl:text><xsl:value-of select="./@identifier"/><xsl:text> events file.
</xsl:text>
<xsl:text> * </xsl:text><xsl:apply-templates select="annotation"/>
<xsl:text> 
</xsl:text>
<xsl:text> * 
</xsl:text>
<xsl:text> * @author Events Generator
</xsl:text>
<xsl:text> */
</xsl:text>
<xsl:text>public final class </xsl:text><xsl:value-of select="./@identifier"/><xsl:text> {

</xsl:text>
    <xsl:apply-templates select="event-type"/>
<xsl:text>}
</xsl:text>

  </xsl:template>
  
  <xsl:template match="event-type">
  
<xsl:text>  /** </xsl:text><xsl:apply-templates select="annotation"/>
<xsl:text> */
</xsl:text>
  
<xsl:text>  public static final curam.util.events.struct.EventKey </xsl:text>
<xsl:value-of select="./@identifier"/>
<xsl:text>
      = new curam.util.events.struct.EventKey();
</xsl:text>
<xsl:text>
  static {
</xsl:text>
<xsl:text>    </xsl:text><xsl:value-of select="./@identifier"/>
<xsl:text>.eventClass = "</xsl:text><xsl:value-of select="../@value"/><xsl:text>";
</xsl:text>
<xsl:text>    </xsl:text><xsl:value-of select="./@identifier"/>
<xsl:text>.eventType = "</xsl:text><xsl:value-of select="./@value"/><xsl:text>";
  }

</xsl:text>

  </xsl:template>
  
  <xsl:template match="annotation">
  
<xsl:value-of select="."/>

  </xsl:template>

</xsl:stylesheet>