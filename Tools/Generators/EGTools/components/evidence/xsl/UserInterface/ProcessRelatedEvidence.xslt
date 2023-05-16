<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

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


  
<xsl:template name="ProcessRelatedEvidence">

  <xsl:param name="prefix" />
  <xsl:param name="rootPath" />

  <xsl:param name="capName" />

  <xsl:param name="parents" />
  <xsl:param name="firstParentName" />

  <xsl:variable name="UIName"><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/>Evidence_fromCreate<xsl:value-of select="$parents"/></xsl:variable>
    
  <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>

  <xsl:if test="count($EvidenceEntity/Relationships/Child)&gt;0 or $EvidenceEntity/Relationships/@association='Yes'">
  
    <xsl:for-each select="$EvidenceEntity/Relationships/Child[@createButton='Yes']">  

      <xsl:variable name="childName"><xsl:value-of select="@name"/></xsl:variable>

              

      <!-- Process Child Evidence -->
      <xsl:call-template name="ProcessRelatedEvidence">
  
        <xsl:with-param name="rootPath" select="$rootPath" />
        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="parents" select="concat($parents, $capName)"/>    
        <xsl:with-param name="capName" select="@name" />
        <xsl:with-param name="firstParentName" select="$firstParentName" />

      </xsl:call-template>

    </xsl:for-each>
        
    <xsl:for-each select="$EvidenceEntity/Relationships/Association[@createButton='Yes']">  

      <xsl:choose>
        <xsl:when test="@from!=''">

        </xsl:when>  
        <xsl:when test="@to!='' and @displayInHierarchy='Yes'">                 

        </xsl:when>  
      </xsl:choose>

      <!-- Process Association Child Evidence -->
      <xsl:call-template name="ProcessRelatedEvidence">
  
        <xsl:with-param name="rootPath" select="$rootPath" />
        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="parents" select="concat($parents, $capName)"/>    
        <xsl:with-param name="capName" select="@name" />
        <xsl:with-param name="firstParentName" select="$firstParentName" />
         

      </xsl:call-template>

    </xsl:for-each>
    
  </xsl:if>        

</xsl:template>

</xsl:stylesheet>