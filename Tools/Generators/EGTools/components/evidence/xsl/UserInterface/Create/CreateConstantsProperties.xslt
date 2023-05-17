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

<!-- Global Variables -->
<xsl:import href="../UICommon.xslt"/>

<!-- Create specific templates -->
<xsl:import href="CreateUtilityTemplates.xslt"/>
  
<xsl:output method="xml" indent="yes"/>
  
  <!--
    Template to write a standalone create page (i.e. outside the wizard) for an entity.
    
    @param entityElement XML Element containing the entity
    @param baseDirectory Base Directory to generate to
  -->
  <xsl:template name="CreateConstantsProperties">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="baseDirectory"/>
    <xsl:param name="caseType"/>
    <xsl:param name="locales"/>
      
    <!-- Full name of the file -->
    <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/>Constants</xsl:variable>
    
    <!-- View Content Vim Properties -->
    <xsl:call-template name="CreateConstantsProperties-AllLocales">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>
    
  </xsl:template>
  
  <xsl:template name="CreateConstantsProperties-AllLocales">
  
    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="CreateConstantsProperties-OneLocale">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="CreateConstantsProperties-AllLocales">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="CreateConstantsProperties-OneLocale">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="CreateConstantsProperties-OneLocale">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>
  
  <xsl:template name="CreateConstantsProperties-OneLocale">
    
    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    
	<xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/*)&gt;0">
    
    <!-- Name of the entity -->
    <xsl:variable name="entityName" select="$entityElement/@name"/>
    
    <!-- Full name of the file -->
    <xsl:variable name="localizedFileName"><xsl:value-of select="$fullFileName"/><xsl:value-of select="$locale"/>.properties</xsl:variable>
    
    <redirect:open select="$localizedFileName" method="text" append="true"/>
    <redirect:write select="$localizedFileName">
      <xsl:variable name="evidenceType">
        <xsl:call-template name="JavaID2CodeValue">
          <xsl:with-param name="java_identifier" select="$entityName"/>
          <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
          <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:value-of select="$entityName"/>.EvidenceType=<xsl:value-of select="$evidenceType"
      /><xsl:text>&#xa;</xsl:text>
      <xsl:text>&#xa;</xsl:text>
      <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
        <xsl:variable name="evidenceType">
          <xsl:call-template name="JavaID2CodeValue">
            <xsl:with-param name="java_identifier" select="@name"/>
            <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
            <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="@name"/>.EvidenceType=<xsl:value-of select="$evidenceType"/><xsl:text>&#xa;</xsl:text>
      </xsl:for-each>
      
      
      <xsl:for-each select="$entityElement/UserInterfaceLayer/Cluster/Field[count(ParticipantSearchType)>0]">
        <xsl:variable name="participantSearchValue">
          <xsl:for-each select="ParticipantSearchType">
            
            <xsl:call-template name="JavaID2CodeValue">
              <xsl:with-param name="java_identifier" select="@type"/>
              <xsl:with-param name="codetableName">CaseParticipantRoleType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
            </xsl:call-template>
            <xsl:text>|</xsl:text>
          </xsl:for-each>
        </xsl:variable>
        <xsl:variable name="participantSearchValue2">
          <xsl:choose>
            <xsl:when
              test="substring($participantSearchValue, string-length($participantSearchValue), 1)='|'">
              <xsl:value-of
                select="substring($participantSearchValue, 1, string-length($participantSearchValue)-1)"
              />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$participantSearchValue"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:text>&#xa;</xsl:text><xsl:value-of select="$entityName"/>.<xsl:value-of
          select="@columnName"/>.CaseParticipantType=<xsl:value-of select="$participantSearchValue2"
          /><xsl:text>|</xsl:text>
      </xsl:for-each>
      
      <xsl:if test="count($entityElement/Relationships/Related/ParticipantType) &gt; 0">
        
        <xsl:variable name="participantTypeCodeValue">
          <xsl:for-each select="$entityElement/Relationships/Related/ParticipantType">
            <xsl:call-template name="JavaID2CodeValue">
              <xsl:with-param name="java_identifier" select="@type"/>
              <xsl:with-param name="codetableName">CaseParticipantRoleType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
            </xsl:call-template>
            <xsl:text>|</xsl:text>
          </xsl:for-each>
        </xsl:variable>
        
        <xsl:variable name="participantTypeCodeValue2">
          <xsl:choose>
            <xsl:when
              test="substring($participantTypeCodeValue, string-length($participantTypeCodeValue), 1)='|'">
              <xsl:value-of
                select="substring($participantTypeCodeValue, 1, string-length($participantTypeCodeValue)-1)"
              />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$participantTypeCodeValue"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:text>&#xa;</xsl:text>CaseParticipantRoleType.<xsl:value-of select="$entityName"/>.ListForRelatedEvidence=<xsl:value-of select="$participantTypeCodeValue2"/><xsl:text>|</xsl:text>
      </xsl:if>
      
      <!-- set properties around existing relationships -->
      <xsl:variable name="isTopLevel">
        <xsl:call-template name="Utilities-Entity-IsTopLevel">
          <xsl:with-param name="entityElement" select="$entityElement"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="hasParent">
        <xsl:call-template name="Utilities-Entity-HasParent">
          <xsl:with-param name="entityElement" select="$entityElement"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="hasPreAssociation">
        <xsl:call-template name="Utilities-Entity-HasPreAssociation">
          <xsl:with-param name="entityElement" select="$entityElement"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="hasMultipleMandatoryParents">
        <xsl:call-template name="Utilities-Entity-HasMultipleMandatoryParents">
          <xsl:with-param name="entityElement" select="$entityElement"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="hasCoreEmployment">
        <xsl:call-template name="Utilities-Entity-HasCoreEmployment">
          <xsl:with-param name="entityElement" select="$entityElement"/>
        </xsl:call-template>
      </xsl:variable>
      <!-- BEGIN, 191675, JAY -->
    <!-- hasAutoEndDate variable to create end date wizard pages -->
    <xsl:variable name="hasAutoEndDate">
    	 <xsl:call-template name="Utilities-Entity-HasAutoEndDate">
        <xsl:with-param name="entityElement" select="$entityElement"/>
      </xsl:call-template>
    </xsl:variable>
    <!-- END, 191675, JAY -->
      
      <!-- If any relationships generate the select all wizard name -->
      <xsl:if test="$isTopLevel='false'">
        <xsl:variable name="selectAllWizardName"><xsl:call-template name="Utilities-CreateWizards-SelectAll-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"></xsl:with-param>
        </xsl:call-template></xsl:variable>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$selectAllWizardName"/>.wizardMenu=<xsl:value-of select="$selectAllWizardName"/>Wizard<xsl:value-of select="$locale"/>.properties
      </xsl:if>
      
       <!-- BEGIN, 191675, JAY -->
       <xsl:if test="$isTopLevel='true' and $hasAutoEndDate='true' and $hasCoreEmployment='false'">
        <xsl:variable name="selectEndDateWizardName"><xsl:call-template name="Utilities-CreateWizards-WithEndDate-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"></xsl:with-param>
        </xsl:call-template></xsl:variable>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$selectEndDateWizardName"/>.wizardMenu=<xsl:value-of select="$selectEndDateWizardName"/>Wizard<xsl:value-of select="$locale"/>.properties
      </xsl:if>
      <!-- END, 191675, JAY -->
      <xsl:if test="$isTopLevel='true' and $hasCoreEmployment='true'">
        <xsl:variable name="selectEmploymentOnlyWizardName"><xsl:call-template name="Utilities-CreateWizards-SelectEmploymentOnly-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"></xsl:with-param>
        </xsl:call-template></xsl:variable>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$selectEmploymentOnlyWizardName"/>.wizardMenu=<xsl:value-of select="$selectEmploymentOnlyWizardName"/>Wizard<xsl:value-of select="$locale"/>.properties
      </xsl:if>
      
      <!-- if any parents generate the type list -->
      <xsl:if test="$hasParent='true'">
        
        <xsl:variable name="parentTypeCodeValue">
          <xsl:for-each select="$entityElement/Relationships/Parent">
            <xsl:call-template name="JavaID2CodeValue">
              <xsl:with-param name="java_identifier" select="@name"/>
              <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
              <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
            </xsl:call-template>
            <xsl:text>|</xsl:text>
          </xsl:for-each>
        </xsl:variable>
        
        <xsl:variable name="parentTypeCodeValue2">
          <xsl:choose>
            <xsl:when
              test="substring($parentTypeCodeValue, string-length($parentTypeCodeValue), 1)='|'">
              <xsl:value-of
                select="substring($parentTypeCodeValue, 1, string-length($parentTypeCodeValue)-1)"
              />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$parentTypeCodeValue"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:text>&#xa;</xsl:text> <xsl:value-of select="$entityName"/>.Wizard.TypeList.PARENT=<xsl:value-of select="$parentTypeCodeValue"/>
        
        <!-- generate wizard properties name -->
        <xsl:variable name="selectWizardName"><xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
          <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/>
        </xsl:call-template></xsl:variable>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$selectWizardName"/>.wizardMenu=<xsl:value-of select="$selectWizardName"/>Wizard<xsl:value-of select="$locale"/>.properties
      </xsl:if>
      
        <xsl:text>&#xa;</xsl:text>
        
      <!-- if any preassociations generate the type list -->
      <xsl:if test="$hasPreAssociation='true'">
          
          <xsl:variable name="preAssocTypeCodeValue">
            <xsl:for-each select="$entityElement/Relationships/PreAssociation">
              <xsl:call-template name="JavaID2CodeValue">
                <xsl:with-param name="java_identifier" select="@to"/>
                <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
              </xsl:call-template>
              <xsl:text>|</xsl:text>
            </xsl:for-each>
          </xsl:variable>
          
          <xsl:variable name="preAssocTypeCodeValue2">
            <xsl:choose>
              <xsl:when
                test="substring($preAssocTypeCodeValue, string-length($preAssocTypeCodeValue), 1)='|'">
                <xsl:value-of
                  select="substring($preAssocTypeCodeValue, 1, string-length($preAssocTypeCodeValue)-1)"
                />
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$preAssocTypeCodeValue"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:text>&#xa;</xsl:text> <xsl:value-of select="$entityName"/>.Wizard.TypeList.PREASSOC=<xsl:value-of select="$preAssocTypeCodeValue"/>
        
        <xsl:variable name="selectWizardName"><xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
          </xsl:call-template></xsl:variable>
          <xsl:text>&#xa;</xsl:text>
          <xsl:value-of select="$selectWizardName"/>.wizardMenu=<xsl:value-of select="$selectWizardName"/>Wizard<xsl:value-of select="$locale"/>.properties
        </xsl:if>
        
      <!-- for each mandatory parent generate the type list -->
        <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
            <xsl:variable name="mandatoryParentCode">
              <xsl:call-template name="JavaID2CodeValue">
                <xsl:with-param name="java_identifier" select="@name"/>
                <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
                <xsl:with-param name="serverBuildDir" select="$serverBuildDir"/>
              </xsl:call-template>
          </xsl:variable>
          
          <xsl:text>&#xa;</xsl:text> <xsl:value-of select="$entityName"/>.Wizard.TypeList.<xsl:value-of select="@name"/>=<xsl:value-of select="$mandatoryParentCode"/>
          
          <xsl:variable name="selectWizardName"><xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="@name"/>
          </xsl:call-template></xsl:variable>
          <xsl:text>&#xa;</xsl:text>
          <xsl:value-of select="$selectWizardName"/>.wizardMenu=<xsl:value-of select="$selectWizardName"/>Wizard<xsl:value-of select="$locale"/>.properties
        </xsl:for-each>
      
      <xsl:text>&#xa;</xsl:text>
      
    </redirect:write>
    <redirect:close select="$localizedFileName"/>
	</xsl:if>
  </xsl:template>
</xsl:stylesheet>