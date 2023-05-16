<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2006-2008 Curam Software Ltd.  All rights reserved.

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

<!-- Global Variables -->
<xsl:include href="EvidenceCommon.xslt" />  
  
<xsl:output method="text"/>
      
<xsl:template match="EvidenceEntities[EvidenceEntity]">

<redirect:write select="concat($ucPrefix, 'Evidence.evx')"> 
&lt;events package="curam.events"&gt;
&lt;event-class identifier="<xsl:value-of select="$prefix"/>Evidence" value="<xsl:value-of select="$prefix"/>Evidence"&gt;
&lt;annotation&gt;<xsl:value-of select="$prefix"/>Evidence class&lt;/annotation&gt;
            
<xsl:for-each select="EvidenceEntity/EntityLayer/Event[@type='Modify']">     
<xsl:variable name="capName"><xsl:value-of select="../../@name"/></xsl:variable>
  &lt;event-type identifier="Modify<xsl:value-of select="$capName"/>Evidence" value="Modify<xsl:value-of select="$capName"/>Evidence"/&gt;
</xsl:for-each>
<xsl:for-each select="EvidenceEntity/EntityLayer/Event[@type='Activate']">     
<xsl:variable name="capName"><xsl:value-of select="../../@name"/></xsl:variable>
  &lt;event-type identifier="<xsl:value-of select="$capName"/>Activated" value="<xsl:value-of select="$capName"/>Activated"/&gt;
</xsl:for-each>
&lt;/event-class&gt;
&lt;/events&gt;
</redirect:write>
</xsl:template>
</xsl:stylesheet>