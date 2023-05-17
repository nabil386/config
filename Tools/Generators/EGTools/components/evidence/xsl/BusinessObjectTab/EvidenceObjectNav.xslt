<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2010-2011 Curam Software Ltd.  All rights reserved.

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

<xsl:import href="../EvidenceCommon.xslt"/>
<xsl:import href="../UserInterface/CreateProperties.xslt"/>

<xsl:output method="xml" indent="yes"/>


<xsl:template name="EvidenceObjectNav">




<xsl:param name="prefix"/>
<xsl:param name="path"/>
<xsl:param name="entityElement" />
<xsl:param name="Relationships" />
<xsl:param name="localeList" />
<xsl:param name="caseType" />
  
<xsl:variable name="entityName" select="$entityElement/@name"/>

  
<redirect:write select="concat($path, 'Object.nav')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             

<nc:navigation id="{$entityName}Object" xmlns:nc="http://www.curamsoftware.com/curam/util/client/navigation-config">
  
  <xsl:if test="count($Relationships/Parent)&gt;0">
  <nc:loader-registry>
      <nc:loader class="{$javaEvidenceCodePath}.service.tab.impl.{$entityName}BObjNavLoader"/>
  </nc:loader-registry>
  </xsl:if>
  <nc:nodes>
    
    <nc:navigation-page id="Details" page-id="{$prefix}_view{$entityName}{$caseType}Object" title="leaf.title.Home" />
     
    <nc:navigation-page id="ChangeHistory" page-id="{$prefix}_view{$entityName}{$caseType}ObjCH" title="leaf.title.ChangeHistory" />
    <xsl:for-each select="$Relationships/Child">
      <nc:navigation-page id="{@name}" page-id="{$prefix}_list{@name}{$caseType}For{$entityName}Obj" title="leaf.title.{@name}" />
    </xsl:for-each>
    <!-- TODO can a parent also exist in mandatory parent? -->
    <xsl:for-each select="$Relationships/MandatoryParents/Parent">
      <nc:navigation-page id="{@name}" page-id="{$prefix}_list{@name}{$caseType}For{$entityName}Obj" title="leaf.title.{@name}" />
    </xsl:for-each>
    <xsl:for-each select="$Relationships/Parent">
      <nc:navigation-page id="{@name}" dynamic="true" page-id="{$prefix}_list{@name}{$caseType}For{$entityName}Obj" title="leaf.title.{@name}" />
    </xsl:for-each>
    
    <xsl:for-each select="$Relationships/Association">
      <nc:navigation-page id="{@to}" page-id="{$prefix}_list{@to}{$caseType}For{$entityName}Obj" title="leaf.title.{@to}" />
    </xsl:for-each>
    <xsl:for-each select="$Relationships/PreAssociation">
    <xsl:variable name="assocName" select="@to"/>
    <xsl:if test="count($Relationships/Parent[@name=$assocName])=0 and count($Relationships/MandatoryParents/Parent[@name=$assocName])=0" >
      <nc:navigation-page id="{@to}" page-id="{$prefix}_list{@to}{$caseType}For{$entityName}Obj" title="leaf.title.{@to}" />
    </xsl:if> 
    </xsl:for-each>
    <!-- BEGIN, CR00266438, CD -->
    <xsl:for-each select="//EvidenceEntities/EvidenceEntity/Relationships/PreAssociation[@to=$entityName]">
    <xsl:variable name="assocName" select="../../@name"/>
    <xsl:if test="count($Relationships/Child[@name=$assocName])=0" >      
      <nc:navigation-page id="{$assocName}" page-id="{$prefix}_list{$assocName}{$caseType}For{$entityName}Obj" title="leaf.title.{$assocName}" />
    </xsl:if> 
    </xsl:for-each>
    <!-- END, CR00266438, CD -->
    
    <nc:navigation-page id="Issues" page-id="{$prefix}_list{$entityName}{$caseType}ObjIssues" title="leaf.title.Issues" />    
    
    <nc:navigation-page id="Verifications" page-id="{$prefix}_list{$entityName}{$caseType}ObjVerifications" title="leaf.title.Verifications" />
    
  </nc:nodes>
  
</nc:navigation>   
 
</redirect:write>
<!--<redirect:close select="$filename"/>-->
  
  
  <!-- write the nav properties -->
  
  
    <xsl:call-template name="write-all-locales-nav-properties">
   
    
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="filepath" select="$path"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="Relationships" select="$Relationships"/>
    </xsl:call-template>
    
  
  </xsl:template>
  
  
  
  
    <!--iterate through each token, generating each element-->
      <xsl:template name="write-all-locales-nav-properties">
  
         <xsl:param name="locales"/>
         <xsl:param name="filepath"/>
         <xsl:param name="entityElement"/>
         <xsl:param name="Relationships"/>
         <!--tokens still exist-->
         <xsl:if test="$locales">
  
           <xsl:choose>
  
             <!--more than one-->
             <xsl:when test="contains($locales,',')">
  
               <xsl:call-template name="write-nav-properties">
                 <xsl:with-param name="locale"
                                select="concat('_', substring-before($locales,','))"/>
                 <xsl:with-param name="filepath"
                                select="$filepath"/>
                 <xsl:with-param name="entityElement"
                                select="$entityElement"/>
                 <xsl:with-param name="Relationships" select="$Relationships"/>
               </xsl:call-template>
  
               <!-- Recursively call self to process all locales -->
               <xsl:call-template name="write-all-locales-nav-properties">
                 <xsl:with-param name="locales"
                                 select="substring-after($locales,',')"/>
                 <xsl:with-param name="filepath"
                                select="$filepath"/>
                 <xsl:with-param name="entityElement"
                                select="$entityElement"/>
                 <xsl:with-param name="Relationships" select="$Relationships"/>
               </xsl:call-template>
  
             </xsl:when>
  
             <!--only one token left-->
             <xsl:otherwise>
  
               <!-- Call for the final locale -->
               <xsl:call-template name="write-nav-properties">
                 <xsl:with-param name="locale" select="concat('_', $locales)"/>
                 <xsl:with-param name="filepath"
                                select="$filepath"/>
                 <xsl:with-param name="entityElement"
                                select="$entityElement"/>
                 <xsl:with-param name="Relationships" select="$Relationships"/>
               </xsl:call-template>
  
               <!-- Finally call for the default locale -->
               <xsl:call-template name="write-nav-properties">
                <xsl:with-param name="locale"/>
                <xsl:with-param name="filepath" select="$filepath"/>
                <xsl:with-param name="entityElement"
                                select="$entityElement"/>
                <xsl:with-param name="Relationships" select="$Relationships"/>
               </xsl:call-template>
  
             </xsl:otherwise>
  
           </xsl:choose>
  
         </xsl:if>
  
      </xsl:template>
  
  
      <xsl:template name="write-nav-properties">
  
  
        
        <xsl:param name="locale"/>
        <xsl:param name="filepath"/>
        <xsl:param name="entityElement"/>
        <xsl:param name="Relationships"/>
        
        <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
        <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
        <redirect:write select="concat($filepath, 'Object', $locale, '.properties')">

		<xsl:if test="count($generalProperties/Tab.Name)&gt;0">
<xsl:call-template name="generateProperty">
            <xsl:with-param name="propertyNode" select="$generalProperties/Tab.Name"/>
            <xsl:with-param name="evidenceNode" select="$entityElement"/>
            <xsl:with-param name="propertyMappings" select="$propertyMappings"/>
          </xsl:call-template>        
        </xsl:if>

		<xsl:if test="count($generalProperties/Tab.Title)&gt;0">
<xsl:call-template name="generateProperty">
            <xsl:with-param name="propertyNode" select="$generalProperties/Tab.Title"/>
            <xsl:with-param name="evidenceNode" select="$entityElement"/>
            <xsl:with-param name="propertyMappings" select="$propertyMappings"/>
          </xsl:call-template>        
        </xsl:if>    
        
        <xsl:if test="count($generalProperties/leaf.title.Home)&gt;0">
    <xsl:call-template name="generateProperty">
                     
      <xsl:with-param name="propertyNode" select="$generalProperties/leaf.title.Home"/>
      <xsl:with-param name="altPropertyName" select="leaf.title.Details"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
            
                     
        </xsl:call-template>
        
  </xsl:if>
  
  <xsl:if test="count($generalProperties/leaf.title.ChangeHistory)&gt;0">
          <xsl:call-template name="generateProperty">
                   
            <xsl:with-param name="propertyNode" select="$generalProperties/leaf.title.ChangeHistory"/>
            <xsl:with-param name="altPropertyName" select="leaf.title.ChangeHistory"/>
            <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
          
                   
         </xsl:call-template>
  
        </xsl:if>
   
   
        <xsl:if test="count($generalProperties/leaf.title.Verifications)&gt;0">
          <xsl:call-template name="generateProperty">
                      
            <xsl:with-param name="propertyNode" select="$generalProperties/leaf.title.Verifications"/>
            <xsl:with-param name="altPropertyName" select="leaf.title.Verifications"/>
            <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
             
                      
          </xsl:call-template>
     
        </xsl:if>
       
        <xsl:if test="count($generalProperties/leaf.title.Issues)&gt;0">
    <xsl:call-template name="generateProperty">
                        
      <xsl:with-param name="propertyNode" select="$generalProperties/leaf.title.Issues"/>
      <xsl:with-param name="altPropertyName" select="leaf.title.Issues"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
               
                        
    </xsl:call-template>
       
        </xsl:if>
   
   
   
        <xsl:for-each select="Relationships/Child">
  <xsl:variable name="childName" select="@name"/>
		<xsl:variable name="childLabel"><xsl:call-template name="getEntityLabelForXMLFile"> 
		  <xsl:with-param name="locale" select="$locale"/>        
		  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$childName]"/>          
		</xsl:call-template></xsl:variable>	
leaf.title.<xsl:value-of select="$childName"/>=<xsl:value-of select="$childLabel"/><xsl:text>&#xa;</xsl:text>
  </xsl:for-each>
  
        <xsl:for-each select="$Relationships/Parent">
        <xsl:variable name="parentName" select="@name"/>
		<xsl:variable name="parentLabel"><xsl:call-template name="getEntityLabelForXMLFile"> 
		  <xsl:with-param name="locale" select="$locale"/>        
		  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$parentName]"/>          
		</xsl:call-template></xsl:variable>	
leaf.title.<xsl:value-of select="$parentName"/>=<xsl:value-of select="$parentLabel"/><xsl:text>&#xa;</xsl:text>
  </xsl:for-each>
  <xsl:for-each select="$Relationships/MandatoryParents/Parent">
  <xsl:variable name="parentName" select="@name"/>
  <xsl:variable name="parentLabel"><xsl:call-template name="getEntityLabelForXMLFile"> 
    <xsl:with-param name="locale" select="$locale"/>        
    <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$parentName]"/>          
  </xsl:call-template></xsl:variable>	
leaf.title.<xsl:value-of select="$parentName"/>=<xsl:value-of select="$parentLabel"/><xsl:text>&#xa;</xsl:text>
  </xsl:for-each>
        <xsl:for-each select="$Relationships/Association">
        <xsl:variable name="assocName" select="@to"/>
		<xsl:variable name="assocLabel"><xsl:call-template name="getEntityLabelForXMLFile"> 
		  <xsl:with-param name="locale" select="$locale"/>        
		  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$assocName]"/>          
		</xsl:call-template></xsl:variable>	
leaf.title.<xsl:value-of select="$assocName"/>=<xsl:value-of select="$assocLabel"/><xsl:text>&#xa;</xsl:text>
  </xsl:for-each>

  <!-- BEGIN, CR00266438, CD -->
  <!-- this gets hit when hitting the preassociation "child" metadata
       i.e. where preassociation relationship node lives on this entity -->
        <xsl:for-each select="$Relationships/PreAssociation">
        <xsl:variable name="assocName" select="@to"/>
		<xsl:variable name="assocLabel"><xsl:call-template name="getEntityLabelForXMLFile"> 
		  <xsl:with-param name="locale" select="$locale"/>        
		  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$assocName]"/>          
		</xsl:call-template></xsl:variable>	
        <xsl:if test="count($Relationships/Parent[@name=$assocName])=0 and count($Relationships/MandatoryParents/Parent[@name=$assocName])=0" >
leaf.title.<xsl:value-of select="$assocName"/>=<xsl:value-of select="$assocLabel"/><xsl:text>&#xa;</xsl:text>
    </xsl:if>
  </xsl:for-each>        
 
  <!-- this gets hit when hitting the preassociation "parent" metadata
       i.e. where another entity has a preassociation to this one -->  
  <xsl:for-each select="//EvidenceEntities/EvidenceEntity/Relationships/PreAssociation[@to=$entityElement/@name]">
    <xsl:variable name="assocName" select="../../@name"/>
	<xsl:variable name="assocLabel"><xsl:call-template name="getEntityLabelForXMLFile"> 
	  <xsl:with-param name="locale" select="$locale"/>        
	  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$assocName]"/>          
	</xsl:call-template></xsl:variable>	
    <xsl:if test="count($Relationships/Child[@name=$assocName])=0" >      
leaf.title.<xsl:value-of select="$assocName"/>=<xsl:value-of select="$assocLabel"/><xsl:text>&#xa;</xsl:text>
        </xsl:if>
        </xsl:for-each>        
  <!-- END, CR00266438, CD -->
  
    </redirect:write>
    
    </xsl:if>
  
  
  
  </xsl:template>
  
</xsl:stylesheet> 