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

<!-- Global Variables -->
<xsl:import href="../UICommon.xslt"/>

<xsl:output method="xml" indent="yes"/>

<!--
    Template to write the main page for listing Evidence records and CPR
    types for use in relating the evidence being created to a core entity such
    as Employment
    
    @param path The path to store the files in
    @param pageName The main part of the page name
    @param currentEntityElem The base element of the current entity
    @param workspaceType The file name postfix showing the workspace type (i.e. Active, InEdit or blank)
-->
<xsl:template name="ListEvidenceAndCPRForRelatedEntity">

  <xsl:param name="path"/>
  <xsl:param name="pageName"/>
  <xsl:param name="currentEntityElem"/>
  <xsl:param name="workspaceType"/>

  <xsl:variable name="fullPageName"><xsl:value-of select="$pageName"/><xsl:if test="$workspaceType!=''">_from<xsl:value-of select="$workspaceType"/></xsl:if></xsl:variable>
    
  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$fullPageName"/></xsl:variable>
    <!-- BEGIN, CR00128375, POB -->
  <xsl:variable name="cancelPage"><xsl:choose>
      <xsl:when test="$workspaceType!=''">Evidence_resolveWorkspace<xsl:value-of select="$workspaceType"/></xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$currentEntityElem/@name"/><xsl:value-of select="$caseType"/>List</xsl:otherwise>
  </xsl:choose></xsl:variable>
    <!-- END, CR00128375 -->
  <redirect:write select="concat($filepath, '.uim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE
  PAGE_ID="{$fullPageName}"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
    
    <PAGE_TITLE>
        <CONNECT>
            <SOURCE
                NAME="TEXT"
                PROPERTY="Page.Title"
            />
        </CONNECT>
        <CONNECT>
            <SOURCE
                NAME="PAGE"
                PROPERTY="contextDescription"
            />
        </CONNECT>
    </PAGE_TITLE>
    
    <PAGE_PARAMETER NAME="caseID"/>
    <PAGE_PARAMETER NAME="contextDescription"/>
    
    <ACTION_SET ALIGNMENT="CENTER">
        <ACTION_CONTROL LABEL="ActionControl.Label.Cancel" IMAGE="CancelButton">
            <LINK SAVE_LINK="false" PAGE_ID="{$cancelPage}">
                <CONNECT>
                    <SOURCE PROPERTY="caseID" NAME="PAGE"/>
                    <TARGET PROPERTY="caseID" NAME="PAGE"/>
                </CONNECT>
            </LINK>
        </ACTION_CONTROL>
    </ACTION_SET>
    
    <!-- SERVER INTERFACES -->
    <xsl:if test="$currentEntityElem/Relationships/Related/@relatedRelationshipType!=''">
        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" OPERATION="getEvidenceListByTypeWithCPR" NAME="DISPLAYEVIDENCETYPE" PHASE="DISPLAY"/>
        
        <CONNECT>
            <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
            />
            <TARGET
                NAME="DISPLAYEVIDENCETYPE"
                PROPERTY="key$caseID"
            />
        </CONNECT>
        
        <CONNECT>
            <SOURCE PROPERTY="{$currentEntityElem/Relationships/Related/@relatedRelationshipType}.EvidenceType" NAME="CONSTANT"/>
            <TARGET PROPERTY="key$evidenceType" NAME="DISPLAYEVIDENCETYPE"/>
        </CONNECT>
    </xsl:if>
    
    <xsl:if test="count($currentEntityElem/Relationships/Related/ParticipantType)&gt;0">
        
   <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="DISPLAYCPRLIST"
       OPERATION="listCaseParticipantsExtraDetails" PHASE="DISPLAY"/>
            
            <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="caseID"/>
                <TARGET NAME="DISPLAYCPRLIST" PROPERTY="key$caseID"/>
            </CONNECT>
            <CONNECT>
                <!-- BEGIN, CR00129346, POB -->
                <SOURCE NAME="CONSTANT" PROPERTY="CaseParticipantRoleType.{$currentEntityElem/@name}.ListForRelatedEvidence"/>
                <!-- END, CR00129346 -->
                <TARGET NAME="DISPLAYCPRLIST" PROPERTY="key$caseParticipantTypeList"/>
            </CONNECT>
    </xsl:if>
    
    <CLUSTER DESCRIPTION="Cluster.RelatedCPRecords.Description">
    <!-- Include VIM for listing the evidence records if an evidence type was specified -->
    <xsl:if test="$currentEntityElem/Relationships/Related/@relatedRelationshipType!=''">
        <xsl:variable name="relatedEvidenceListVimPageName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$currentEntityElem/Relationships/Related/@relatedRelationshipType"/>EvidenceFor<xsl:value-of select="$currentEntityElem/@name"/><xsl:value-of select="$caseType"/><xsl:if test="$workspaceType!=''">_from<xsl:value-of select="$workspaceType"/></xsl:if></xsl:variable>
        
        <INCLUDE FILE_NAME="{$relatedEvidenceListVimPageName}.vim"/>
        
    </xsl:if>
    
    <xsl:if test="count($currentEntityElem/Relationships/Related/ParticipantType)&gt;0">
            
            <xsl:variable name="relatedCPRVimPageName"><xsl:value-of select="$prefix"/>_listCPRFor<xsl:value-of select="$currentEntityElem/@name"/><xsl:value-of select="$caseType"/><xsl:if test="$workspaceType!=''">_from<xsl:value-of select="$workspaceType"/></xsl:if></xsl:variable>
            
            <INCLUDE FILE_NAME="{$relatedCPRVimPageName}.vim"/>
            
    </xsl:if>
    </CLUSTER>


</PAGE>
      
  </redirect:write>
  
    <xsl:call-template name="write-all-locales-ListEvidenceAndCPRForRelatedEntity-properties">   
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filepath" select="$filepath"/>
    </xsl:call-template>
    
</xsl:template>
    
    <!--
        Recursive method to write a corresponding properties file for each defined locale
        
        @param locales The remaining list of locales to process
        @param filepath The properties file to write
    -->
    <xsl:template name="write-all-locales-ListEvidenceAndCPRForRelatedEntity-properties">
        
        <xsl:param name="locales"/>
        <xsl:param name="filepath"/>
        
        <!--tokens still exist-->
        <xsl:if test="$locales">               
            
            <xsl:choose>
                
                <!--more than one-->
                <xsl:when test="contains($locales,',')">
                    
                    <xsl:call-template name="write-ListEvidenceAndCPRForRelatedEntity-properties">
                        <xsl:with-param name="locale"
                            select="concat('_', substring-before($locales,','))"/>
                        <xsl:with-param name="filepath"
                            select="$filepath"/>
                    </xsl:call-template>
                    
                    <!-- Recursively call self to process all locales -->
                    <xsl:call-template name="write-all-locales-ListEvidenceAndCPRForRelatedEntity-properties">   
                        <xsl:with-param name="locales"
                            select="substring-after($locales,',')"/>
                        <xsl:with-param name="filepath"
                            select="$filepath"/>
                    </xsl:call-template>
                    
                </xsl:when>
                
                <!--only one token left-->
                <xsl:otherwise>
                    
                    <!-- Call for the final locale -->
                    <xsl:call-template name="write-ListEvidenceAndCPRForRelatedEntity-properties">
                        <xsl:with-param name="locale" select="concat('_', $locales)"/>
                        <xsl:with-param name="filepath"
                            select="$filepath"/>
                    </xsl:call-template>
                    
                    <!-- Finally call for the default locale -->
                    <xsl:call-template name="write-ListEvidenceAndCPRForRelatedEntity-properties">
                        <xsl:with-param name="locale"/>
                        <xsl:with-param name="filepath" select="$filepath"/>
                    </xsl:call-template>
                    
                </xsl:otherwise>
                
            </xsl:choose>
            
        </xsl:if>
        
    </xsl:template>
    
    <!--
        Template to write the properties file in the given locale
        
        @param locale The locale to write
        @param filepath The properties file to write
    -->
    <xsl:template name="write-ListEvidenceAndCPRForRelatedEntity-properties">
        
        <xsl:param name="locale"/>
        <xsl:param name="filepath"/>
        
        <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
            <redirect:write select="concat($filepath, $locale, '.properties')">    
                <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/> 
                
                <xsl:if test="count($generalProperties/Help.List.RelatedEvidence.PageDescription)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/Help.List.RelatedEvidence.PageDescription"/>
                        <xsl:with-param name="evidenceNode" select="."/>
	                    <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
                    </xsl:call-template>  
                </xsl:if>
                
                <xsl:if test="count($generalProperties/ActionControl.Label.Cancel)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Cancel"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template>  
                </xsl:if>
                
                <xsl:if test="count($generalProperties/Page.Title.ListRelatedEntity)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ListRelatedEntity"/>
                        <xsl:with-param name="evidenceNode" select="."/>
	                    <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
                    </xsl:call-template>  
                    <xsl:text>&#xa;</xsl:text> 
                </xsl:if>
                
                <xsl:if test="count($generalProperties/List.Title.Action)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Action"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template> 
                </xsl:if>  
                
                <xsl:if test="count($generalProperties/ActionControl.Label.Select.For.RelatedEntity.Association)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Select.For.RelatedEntity.Association"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template> 
                </xsl:if>  
                
                <xsl:if test="count($generalProperties/List.Title.Name)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Name"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template> 
                </xsl:if>
                
                <xsl:if test="count($generalProperties/List.Title.Type)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Type"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template> 
                </xsl:if>
                
                <xsl:if test="count($generalProperties/List.Title.DateOfBirth)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.DateOfBirth"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template> 
                </xsl:if>
                
                <xsl:choose>
                    <xsl:when test="count($generalProperties/List.Title.RelatedCPR)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.RelatedCPR"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template> 
                    </xsl:when>
                    <xsl:otherwise>List.Title.RelatedCPR=List.Title.RelatedCPR must be specified in the general.properties file</xsl:otherwise>
                    </xsl:choose>
                <xsl:text>&#xa;</xsl:text>
                <xsl:choose>
                    <xsl:when test="count($generalProperties/Cluster.RelatedCPRecords.Description)&gt;0">
                    <xsl:call-template name="callGenerateProperties">
                        <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.RelatedCPRecords.Description"/>
                        <xsl:with-param name="evidenceNode" select="."/>
                    </xsl:call-template> 
                    </xsl:when>
                    <xsl:otherwise>Cluster.RelatedCPRecords.Description=Cluster.RelatedCPRecords.Description must be specified in the general.properties file</xsl:otherwise>
                    </xsl:choose>
                <xsl:text>&#xa;</xsl:text>
            </redirect:write>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>