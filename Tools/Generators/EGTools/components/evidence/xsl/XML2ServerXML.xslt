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
Software, Ltd. (&quot;Confidential Information&quot;).  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt"/>
  <xsl:include href="UserInterface/UICommon.xslt"/>

  <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>
  
  <xsl:template match="EvidenceEntity">
    
    <xsl:variable name="name"><xsl:value-of select="@Name"/></xsl:variable>
    <xsl:variable name="filename"><xsl:value-of select="$name"/>.xml</xsl:variable>
  
    <redirect:open select="$filename" method="xml" append="false"/>          
    <redirect:write select="$filename">
    
    <EvidenceEntity>
    
    <Entity 
      logicalName="{$name}"
      storageLevel="{@caseType}"
    >
      
      <RelatedEntityAttributes>
        <xsl:attribute name="relatedEntityAttributes">
          <xsl:choose>
            <xsl:when test="count(RelatedEntityAttributes/Entity)&gt;0">Yes</xsl:when>
            <xsl:otherwise>No</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        
        <xsl:attribute name="exposeOperation">
          <xsl:choose>
            <xsl:when test="count(RelatedEntityAttributes/Entity/Attributes/Attribute/CreatePageParameter)&gt;0">Yes</xsl:when>
            <xsl:otherwise>No</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
      </RelatedEntityAttributes>
      
      <Relationships>
      
        <xsl:for-each select="Relationships/Parent">
          <Parent name="{@name}"/>
        </xsl:for-each>

        <xsl:for-each select="Relationships/Child">
          <Child name="{@name}"/>
        </xsl:for-each>
        
        <xsl:for-each select="Relationships/Association">
        <Association>
           <xsl:if test="@to!=&apos;&apos;">
             <xsl:attribute name="from"><xsl:value-of select="@to"/></xsl:attribute> 
             <xsl:attribute name="type"><xsl:value-of select="translate(@to, $lcletters, $ucletters)"/></xsl:attribute> 
           </xsl:if>    
 
           <xsl:if test="@from!=&apos;&apos;">
             <xsl:attribute name="to"><xsl:value-of select="@from"/></xsl:attribute> 
             <xsl:attribute name="type"><xsl:value-of select="translate(@from, $lcletters, $ucletters)"/></xsl:attribute> 
          </xsl:if>    
          
          <xsl:attribute name="singleAssociation">
            <xsl:choose>
              <xsl:when test="@displayInHierarchy=&apos;Yes&apos;">No</xsl:when>
              <xsl:otherwise>Yes</xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>              
        </Association>
        </xsl:for-each>
        
        <xsl:for-each select="Relationships/Related">
    
          <xsl:choose>
            <xsl:when test="@UICreate=&apos;Yes&apos;">
              <Related to="Employment" relatedRelationshipType="{@name}"/>          
            </xsl:when>
            <xsl:otherwise>
              <Related utilizedBy="{@name}"/>
            </xsl:otherwise>
          </xsl:choose>
    
        </xsl:for-each>

      </Relationships>
      
    </Entity>
    </EvidenceEntity>
    
    </redirect:write>      
    <redirect:close select="$filename"/>
    
  </xsl:template>
  
</xsl:stylesheet>