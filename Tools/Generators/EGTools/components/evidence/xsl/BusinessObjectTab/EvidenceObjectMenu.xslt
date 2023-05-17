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

<xsl:import href="../UserInterface/Create/CreateUtilityTemplates.xslt"/>
<xsl:import href="../UserInterface/CreateProperties.xslt"/>
<xsl:import href="../EvidenceCommon.xslt"/>
  
<xsl:output method="xml" indent="yes"/>


<xsl:template name="EvidenceObjectMenu">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="entityElement" />
  <xsl:param name="Relationships" />
  <xsl:param name="caseType" />
  
  <xsl:variable name="entityName" select="$entityElement/@name"/>
  
  <xsl:variable name="filename"><xsl:value-of select="$path"/>ObjectMenu.mnu</xsl:variable>

  <redirect:open select="$filename" method="text" append="false" />
<redirect:write select="$filename">
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>             

  
  
<mc:menu-bar xmlns:mc="http://www.curamsoftware.com/curam/util/client/menubar-config" id="{$entityName}ObjectMenu">
  
  <mc:loader-registry>
    <mc:loader class="{$javaEvidenceCodePath}.service.tab.impl.{$entityName}BObjMenuLoader"/>
  </mc:loader-registry>
  
  <xsl:if test="count(Relationships/Child)>0">
    <xsl:for-each select="$Relationships/Child">
      <xsl:variable name="childName" select="@name"/>
      <xsl:variable name="pageName"><xsl:call-template name="Utilities-getResolveScriptName-FromRelatedBusinessObject">
        <xsl:with-param name="entityElement" select="//EvidenceEntities/EvidenceEntity[@name=$childName]"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>
      <mc:menu-item id="New{@name}"  title="MenuItem.Title.New{@name}" dynamic="true" 
        tooltip="MenuItem.Tooltip.New{@name}" page-id="{$pageName}" open-as="modal"/>  
    </xsl:for-each>  
  </xsl:if>
    
  <!-- BEGIN, CR00266438, CD -->  
  <xsl:if test="count(//EvidenceEntities/EvidenceEntity/Relationships/PreAssociation[@to=$entityName])&gt;0">
    <xsl:for-each select="//EvidenceEntities/EvidenceEntity[count(Relationships/PreAssociation[@to=$entityElement/@name])&gt;0]">
      <xsl:variable name="preAssocName" select="@name"/>
      <xsl:if test="count($Relationships/Child[@name=$preAssocName])=0" >
      <xsl:variable name="pageName"><xsl:call-template name="Utilities-getResolveScriptName-FromRelatedBusinessObject">
        <xsl:with-param name="entityElement" select="."/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>
      <mc:menu-item id="New{@name}"  title="MenuItem.Title.New{@name}" dynamic="true" 
        tooltip="MenuItem.Tooltip.New{@name}" page-id="{$pageName}" open-as="modal"/>  
      </xsl:if>
    </xsl:for-each>
  </xsl:if>
  <!-- END, CR00266438, CD -->
  
</mc:menu-bar>
 
</redirect:write>
<redirect:close select="$filename"/>
  
  
  <!-- write the nav properties -->
    
    
      <xsl:call-template name="write-all-locales-menu-properties">
     
      
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filepath" select="$path"/>
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="Relationships" select="$Relationships"/>
      </xsl:call-template>
      
    
    </xsl:template>
    
    
    
    
      <!--iterate through each token, generating each element-->
        <xsl:template name="write-all-locales-menu-properties">
    
           <xsl:param name="locales"/>
           <xsl:param name="filepath"/>
           <xsl:param name="entityElement"/>
           <xsl:param name="Relationships"/>
           <!--tokens still exist-->
           <xsl:if test="$locales">
    
             <xsl:choose>
    
               <!--more than one-->
               <xsl:when test="contains($locales,',')">
    
                 <xsl:call-template name="write-menu-properties">
                   <xsl:with-param name="locale"
                                  select="concat('_', substring-before($locales,','))"/>
                   <xsl:with-param name="filepath"
                                  select="$filepath"/>
                   <xsl:with-param name="entityElement"
                                  select="$entityElement"/>
                   <xsl:with-param name="Relationships" select="$Relationships"/>
                 </xsl:call-template>
    
                 <!-- Recursively call self to process all locales -->
                 <xsl:call-template name="write-all-locales-menu-properties">
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
                 <xsl:call-template name="write-menu-properties">
                   <xsl:with-param name="locale" select="concat('_', $locales)"/>
                   <xsl:with-param name="filepath"
                                  select="$filepath"/>
                   <xsl:with-param name="entityElement"
                                  select="$entityElement"/>
                   <xsl:with-param name="Relationships" select="$Relationships"/>
                 </xsl:call-template>
    
                 <!-- Finally call for the default locale -->
                 <xsl:call-template name="write-menu-properties">
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
    
    
        <xsl:template name="write-menu-properties">
    
    
          
          <xsl:param name="locale"/>
          <xsl:param name="filepath"/>
          <xsl:param name="entityElement"/>
          <xsl:param name="Relationships"/>
          
          <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
          <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
          <redirect:write select="concat($filepath, 'ObjectMenu', $locale, '.properties')">
     
          
          <!-- BEGIN, CR00266438, CD -->
          <xsl:for-each select="Relationships/Child">
            <xsl:call-template name="getNewLinkProperties">
              <xsl:with-param name="locale" select="$locale"/>
              <xsl:with-param name="linkToEntityName" select="@name"/>
              <xsl:with-param name="generalProperties" select="$generalProperties"/>
            </xsl:call-template>            
          </xsl:for-each>


          <xsl:for-each select="//EvidenceEntities/EvidenceEntity[count(Relationships/PreAssociation[@to=$entityElement/@name])&gt;0]">
            <xsl:variable name="preAssocName" select="@name"/>
            <xsl:if test="count($Relationships/Child[@name=$preAssocName])=0" >
            <xsl:call-template name="getNewLinkProperties">
              <xsl:with-param name="locale" select="$locale"/>
              <xsl:with-param name="linkToEntityName" select="@name"/>
              <xsl:with-param name="generalProperties" select="$generalProperties"/>
            </xsl:call-template>                        
            </xsl:if>
          </xsl:for-each>
          <!-- END, CR00266438, CD -->
          
      </redirect:write>
      
      </xsl:if>
    
  </xsl:template>
  
  
  <!-- BEGIN, CR00266438, CD -->
  <xsl:template name="getNewLinkProperties">
  
    <xsl:param name="locale"/>
    <xsl:param name="linkToEntityName"/>
    <xsl:param name="generalProperties"/>
    
    <xsl:variable name="propName">MenuItem.Title.New<xsl:value-of select="$linkToEntityName"/></xsl:variable>
    <xsl:variable name="propTooltip">MenuItem.Tooltip.New<xsl:value-of select="$linkToEntityName"/></xsl:variable>
	<xsl:variable name="linkToEntityLabel"><xsl:call-template name="getEntityLabelForPropertiesFile"> 
      <xsl:with-param name="locale" select="$locale"/>        
      <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$linkToEntityName]"/>          
    </xsl:call-template></xsl:variable>
	        <!-- Submenu.Title.New has been deprecated as the property should control the dynamic 
			     part of the output for localizability. Submenu.Title.New.2 supersedes this. -->
            <xsl:variable name="linkText"><xsl:choose>
			  <xsl:when test="$generalProperties/Submenu.Title.New.2"><xsl:call-template name="callGenerateProperties">	  	    	                   
				  <xsl:with-param name="propertyNode" select="$generalProperties/Submenu.Title.New.2"/>
				  <xsl:with-param name="altPropertyName" select="$propName"/>
				  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$linkToEntityName]"/>	  	    	          
                  </xsl:call-template></xsl:when>
			  <xsl:otherwise><xsl:call-template name="generateProperty">	  	    	                   
	      <xsl:with-param name="propertyNode" select="$generalProperties/Submenu.Title.New"/>
	      <xsl:with-param name="altPropertyName" select="$propName"/>
              <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
                  </xsl:call-template><xsl:value-of select="$linkToEntityLabel"/></xsl:otherwise>
			  </xsl:choose></xsl:variable>
	  	    	          
	        <!-- Submenu.Tooltip.New has been deprecated as the property should control the dynamic 
			     part of the output for localizability. Submenu.Tooltip.New.2 supersedes this. -->
	        <xsl:variable name="tooltipText"><xsl:choose>
			  <xsl:when test="$generalProperties/Submenu.Tooltip.New.2"><xsl:call-template name="callGenerateProperties">	  	  	    	                   
				  <xsl:with-param name="propertyNode" select="$generalProperties/Submenu.Tooltip.New.2"/>
				  <xsl:with-param name="altPropertyName" select="$propTooltip"/>
				  <xsl:with-param name="evidenceNode" select="/EvidenceEntities/EvidenceEntity[@name=$linkToEntityName]"/>    	          	     	                   
			    </xsl:call-template></xsl:when>
			  <xsl:otherwise><xsl:call-template name="generateProperty">	  	  	    	                   
	      <xsl:with-param name="propertyNode" select="$generalProperties/Submenu.Tooltip.New"/>
	      <xsl:with-param name="altPropertyName" select="$propTooltip"/>
	      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>    	          
			    </xsl:call-template><xsl:value-of select="$linkToEntityLabel"/></xsl:otherwise>
			  </xsl:choose></xsl:variable>
  
<xsl:value-of select="normalize-space($linkText)"/><xsl:text>&#xa;</xsl:text>
<xsl:value-of select="normalize-space($tooltipText)"/><xsl:text>&#xa;</xsl:text>
  </xsl:template>
  <!-- END, CR00266438, CD -->
  
</xsl:stylesheet> 