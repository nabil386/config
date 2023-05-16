<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2010 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>


<xsl:output method="text" indent="yes"/>

<xsl:include href="../EvidenceCommon.xslt" />



<xsl:param name="rootPath"/>
<xsl:param name="date"/>

<xsl:template match="table[row]">
      
      

<xsl:variable name="filename" select="concat($rootPath, 'NOTABLEAPPRESOURCE.dmx')"/>
<xsl:variable name="table" select="table"/>
<!-- using this block for evidence generation until one has been decided -->
<!-- TODO add this to a evidence generator properties file -->
<xsl:variable name="resourceIDStart" select="23000"/>




<redirect:open select="$filename" method="text" />
<redirect:write select="$filename"> 
  
  <xsl:for-each select="row">
  
  
  &lt;row&gt;
    <xsl:for-each select="attribute">
    &lt;attribute name="<xsl:value-of select="@name"/>"&gt;
    &lt;value&gt;<xsl:value-of select="."/>&lt;/value&gt;
    &lt;/attribute&gt;
    </xsl:for-each>
    &lt;attribute name="resourceID"&gt;
      &lt;value&gt;<xsl:value-of select="$resourceIDStart+position()"/>&lt;/value&gt;
    &lt;/attribute&gt;
    
  &lt;/row&gt;    
     
  </xsl:for-each>
 
 </redirect:write>
 <redirect:close select="$filename"/>

  </xsl:template>
  
</xsl:stylesheet> 