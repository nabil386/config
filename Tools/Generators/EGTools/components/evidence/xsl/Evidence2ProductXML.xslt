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
  <xsl:include href="UserInterface/JavaID2CodeValue.xslt" />
    
  <xsl:output method="text" indent="yes"/>
  
  <xsl:param name="buildRoot"/>
  <xsl:param name="clientDir"/>
  <xsl:param name="prefix"/>
  <xsl:param name="serverBuildDir"/>

  <xsl:template match="EvidenceEntities">    
    
    <xsl:variable name="filename"><xsl:value-of select="$buildRoot"/>/Product.xml</xsl:variable>
    
      <xsl:if test="count(EvidenceEntity)>0">
        <redirect:open select="$filename" method="text" append="true" />
        <redirect:write select="$filename"> 
&lt;product clientDir=&quot;<xsl:value-of select="$clientDir"/>&quot; prefix=&quot;<xsl:value-of select="$prefix"/>&quot;&gt;      
        <xsl:for-each select="EvidenceEntity">
          <xsl:variable name="evidenceType">
            <xsl:call-template name="JavaID2CodeValue">
              <xsl:with-param name="java_identifier" select="@name"/>
              <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
            </xsl:call-template>
          </xsl:variable>
  &lt;evidence type=&quot;<xsl:value-of select="$evidenceType"/>&quot;/&gt;
        </xsl:for-each>
&lt;/product&gt;

        </redirect:write>
        <redirect:close select="$filename"/>
      </xsl:if>

</xsl:template>

</xsl:stylesheet>