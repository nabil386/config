<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
--> 
<!--
Copyright (c) 2006-2008 Curam Software Ltd.  All rights reserved.

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

<xsl:output method="xml" indent="yes"/>


<xsl:template name="DefaultAppSection">

  
  <xsl:param name="path"/>
  <xsl:param name="prefix"/>
  
  
  
  <xsl:variable name="filename"><xsl:value-of select="$path"/><xsl:value-of select="$prefix"/>GeneratedAppSection.sec</xsl:variable>

  <redirect:open select="$filename" method="text" append="false" />
<redirect:write select="$filename">
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>              


<sc:section 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:sc="http://www.curamsoftware.com/curam/util/client/section-config"
  id="{$prefix}GeneratedAppSection"
>
  <xsl:for-each select="/EvidenceEntities/EvidenceEntity">
    <sc:tab id="{@name}Object"/>
  </xsl:for-each>
</sc:section>
 
   
 
</redirect:write>
<redirect:close select="$filename"/>
  
  </xsl:template>
  
</xsl:stylesheet> 