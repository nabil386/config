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
<!--
  Main stylesheet for writing the create pages for an entity.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="../UICommon.xslt"/>

  <xsl:import href="CreateUtilityTemplates.xslt"/>

  <xsl:import href="CreateContentVIM.xslt"/>
  <xsl:import href="StandaloneCreateUIM.xslt"/>
  <xsl:import href="WizardCreateUIM.xslt"/>
  
  <xsl:import href="WizardSelectUIM.xslt"/>
  <xsl:import href="WizardSelectContentVIM.xslt"/>
  
  <xsl:import href="WizardSelectEmploymentUIM.xslt"/>
  <xsl:import href="WizardSelectEmploymentContentVIM.xslt"/>
  
  <!-- BEGIN, 191675, JAY -->
  <xsl:import href="WizardEndDateUIM.xslt"/>
  <xsl:import href="WizardEndDateContentVIM.xslt"/>
  <!-- END, 191675, JAY -->
  <xsl:import href="CreateConstantsProperties.xslt"/>
  
  <xsl:import href="ResolveCreateEvidenceFromRelated.xslt"/>
  
  <!--
    Template to write the create pages for an entity.
    
    @param entityElement XML Element for the entity
    @param caseType Type of the case 
  -->
  <xsl:template name="WriteCreatePagesForEntity">

    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    
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
    
    <!--
      If a top level entity or only one related type then write the standalone create pages.
    -->
    <xsl:if test="($isTopLevel='true' or ($hasParent='true' and $hasPreAssociation='false') or ($hasParent='false' and $hasPreAssociation='true')) and $hasCoreEmployment='false'">
      <xsl:call-template name="INTERNAL-WriteCreatePagesForEntity-StandaloneCreate">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>
    </xsl:if>
    
    <!-- If a top level entitiy is related to employment, then write the appropriate wizard -->
    <xsl:if test="($isTopLevel='true' and $hasCoreEmployment='true')">
      <xsl:call-template name="INTERNAL-WriteCreatePagesForEntity-WizardEmploymentOnly">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>
    </xsl:if>
    
    <!--
      If at least one higher relationship then need to write the resolve script
    -->
    <xsl:if test="$hasParent='true' or $hasPreAssociation='true' or $hasMultipleMandatoryParents='true'">
      
      <!-- Write resolve script for creating from business object tab of related entity -->
      <xsl:variable name="resolveFileName"><xsl:call-template name="Utilities-getResolveScriptName-FromRelatedBusinessObject">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>
      
      <xsl:call-template name="ResolveCreateEvidenceFromRelated">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
        <xsl:with-param name="fileName" select="$resolveFileName"/>
      </xsl:call-template>
    </xsl:if>
    
    <!--
      If at least one higher relationship then need to write the SelectAll Wizard
    -->
    <xsl:if test="$hasParent='true' or $hasPreAssociation='true' or $hasMultipleMandatoryParents='true'">
      <xsl:call-template name="INTERNAL-WriteCreatePagesForEntity-WizardSelectAll">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>
    </xsl:if>
    
    <!-- Write wizard for each mandatory parent -->
    <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
      <xsl:call-template name="INTERNAL-WriteCreatePagesForEntity-WizardWithPreSelection">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="@name"/>
      </xsl:call-template>
    </xsl:for-each>
    
    <!--
      If both parent and pre association exists
    -->
    <xsl:if test="$hasParent='true' and $hasPreAssociation='true'">
      
      <!-- One with a pre selected parent -->
      <xsl:call-template name="INTERNAL-WriteCreatePagesForEntity-WizardWithPreSelection">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/>
      </xsl:call-template>
      
      <!-- One with a pre selected preassociation -->
      <xsl:call-template name="INTERNAL-WriteCreatePagesForEntity-WizardWithPreSelection">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
      </xsl:call-template>
      
    </xsl:if>
    
    <!-- BEGIN, 191675, JAY -->
    <!-- 
      If Business End Date exists
     -->
    <xsl:if test="$isTopLevel='true' and $hasAutoEndDate='true' and $hasCoreEmployment='false'">
     	
     	<xsl:call-template name="INTERNAL-WriteCreatePagesForEntity-WithEndDateWizard">
     	  <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
     	</xsl:call-template>
     </xsl:if>
    <!-- END, 191675, JAY -->
    <!-- Write the constants properties file for use in the create pages -->
    <xsl:call-template name="CreateConstantsProperties">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>
  </xsl:template>
  
  <!--
    Internal template to write the standalone create pages for an entity.
    
    This gets the following pages generated
    - standalone content vim + properties
    - standalone content uim + properties
    
    @param entityElement XML Element for the entity
    @param caseType Type of the case 
  -->
  <xsl:template name="INTERNAL-WriteCreatePagesForEntity-StandaloneCreate">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    
    <xsl:variable name="standaloneCreateContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-StandaloneContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="standaloneCreateUIMName"><xsl:call-template name="Utilities-getCreatePageName-StandaloneContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <!-- Write the create content vim for use in standalone create pages -->
    <xsl:call-template name="CreateContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$standaloneCreateContentVIMName"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
    </xsl:call-template>
    
    <!-- Write the create UIM Page for standalone create pages -->
    <xsl:call-template name="StandaloneCreateUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$standaloneCreateUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$standaloneCreateContentVIMName"/>
    </xsl:call-template>
    
  </xsl:template>
  
  <!--
    Internal template to write the wizard pages for when an entity only has core Employment related to it.
    
    This gets the following pages generated
    - wizard select vim + properties
    - wizard select uim + properties
    - wizard content vim + properties
    - wizard content uim + properties
    
    @param entityElement XML Element for the entity
    @param caseType Type of the case 
  -->
  <xsl:template name="INTERNAL-WriteCreatePagesForEntity-WizardEmploymentOnly">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    
    <xsl:variable name="wizardCreateContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardCreateUIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>EmpOnly</xsl:variable>
    
    <xsl:variable name="wizardSelectEmploymentContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>_only</xsl:variable>
    
    <xsl:variable name="wizardSelectEmploymentUIMName"><xsl:call-template name="Utilities-CreateWizards-SelectEmploymentOnly-getStartingPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <!-- Write the create content vim for use on all wizard pages -->
    <xsl:call-template name="CreateContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardCreateContentVIMName"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
    </xsl:call-template>
    
    <!-- Write the create UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardCreateUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardCreateUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardCreateContentVIMName"/>
      <xsl:with-param name="wizardSelectUIMName" select="$wizardSelectEmploymentUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardSelectEmploymentUIMName"/>
    </xsl:call-template>
    
    <!-- Write the select content vim for use on the Select All wizard page -->
    <xsl:call-template name="WizardSelectEmploymentContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectEmploymentContentVIMName"/>
      <xsl:with-param name="uimPageName" select="$wizardSelectEmploymentUIMName"/>
    </xsl:call-template>
    
    <!-- Write the select UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardSelectEmploymentUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectEmploymentUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardSelectEmploymentContentVIMName"/>
      <xsl:with-param name="wizardSelectUIMName" select="$wizardCreateUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardSelectEmploymentUIMName"/>
    </xsl:call-template>
  </xsl:template>
  
  <!--
    Internal template to write the 'SelectAll' wizard create pages for an entity.
    
    This gets the following pages generated
    - wizard select vim + properties
    - wizard select uim + properties
    - wizard content vim + properties
    - wizard content uim + properties
    
    @param entityElement XML Element for the entity
    @param caseType Type of the case 
  -->
  <xsl:template name="INTERNAL-WriteCreatePagesForEntity-WizardSelectAll">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    
    <xsl:variable name="wizardCreateContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardSelectAllCreateUIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardSelectAllSelectContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardSelectAllSelectUIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-SelectAll-getStartingPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <!-- Write the create content vim for use on all wizard pages -->
    <xsl:call-template name="CreateContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardCreateContentVIMName"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
    </xsl:call-template>
    
    <!-- Write the create UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardCreateUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectAllCreateUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardCreateContentVIMName"/>
      <xsl:with-param name="wizardSelectUIMName" select="$wizardSelectAllSelectUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>
    
    <!-- Write the select content vim for use on the Select All wizard page -->
    <xsl:call-template name="WizardSelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectAllSelectContentVIMName"/>
    </xsl:call-template>
    
    <!-- Write the select UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardSelectUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectAllSelectUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardSelectAllSelectContentVIMName"/>
      <xsl:with-param name="wizardCreateUIMName" select="$wizardSelectAllCreateUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>
    
    <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">
    
      <xsl:variable name="wizardSelectEmploymentContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardSelectEmploymentUIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <!-- Write the select employment  vim for use on the Select All wizard page -->
    <xsl:call-template name="WizardSelectEmploymentContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectEmploymentContentVIMName"/>
      <xsl:with-param name="uimPageName" select="$wizardSelectEmploymentUIMName"/>
    </xsl:call-template>
    
    <!-- Write the select employment UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardSelectEmploymentUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectEmploymentUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardSelectEmploymentContentVIMName"/>
      <xsl:with-param name="wizardSelectUIMName" select="$wizardSelectAllSelectUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
  <!--
    Internal template to write the 'wizard create pages for an entity when the specified type is pre selected
    
    This gets the following pages generated
    - wizard select vim + properties
    - wizard select uim + properties
    - wizard content uim + properties
    
    @param entityElement XML Element for the entity
    @param caseType Type of the case 
  -->
  <xsl:template name="INTERNAL-WriteCreatePagesForEntity-WizardWithPreSelection">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    
    <xsl:variable name="wizardCreateContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardCreateUIMName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardSelectContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardSelectUIMName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>
    
    <!-- Write the create UIM page for use on all this wizard -->
    <xsl:call-template name="WizardCreateUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardCreateUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardCreateContentVIMName"/>
      <xsl:with-param name="wizardSelectUIMName" select="$wizardSelectUIMName"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>
    
    <!-- Write the select content vim for use on thisl wizard page -->
    <xsl:call-template name="WizardSelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectContentVIMName"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template>
    
    <!-- Write the select UIM page for use on all thiswizard -->
    <xsl:call-template name="WizardSelectUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardSelectContentVIMName"/>
      <xsl:with-param name="wizardCreateUIMName" select="$wizardCreateUIMName"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>
    
    <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">
      
      <xsl:variable name="wizardSelectEmploymentVIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectContentVIM">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
      </xsl:call-template></xsl:variable>
      
      <xsl:variable name="wizardSelectEmploymentUIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
      </xsl:call-template></xsl:variable>
      
      <!-- Write the select employment  vim for use on the Select All wizard page -->
      <xsl:call-template name="WizardSelectEmploymentContentVIM">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
        <xsl:with-param name="fileName" select="$wizardSelectEmploymentVIMName"/>
        <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
        <xsl:with-param name="uimPageName" select="$wizardSelectEmploymentUIMName"/>
      </xsl:call-template>
      
      <!-- Write the select employment UIM page for use on all the Select All wizard -->
      <xsl:call-template name="WizardSelectEmploymentUIM">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
        <xsl:with-param name="fileName" select="$wizardSelectEmploymentUIMName"/>
        <xsl:with-param name="includeVIMFileName" select="$wizardSelectEmploymentVIMName"/>
        <xsl:with-param name="wizardSelectUIMName" select="$wizardSelectUIMName"/>
        <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
        <xsl:with-param name="wizardName" select="$wizardName"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
   <!-- BEGIN, 191675, JAY -->
   <!--
    Internal template to write the end date create wizard create pages for an entity.
    
    This gets the following pages generated
    - wizard select vim + properties
    - wizard select uim + properties
    - wizard content vim + properties
    - wizard content uim + properties
    
    @param entityElement XML Element for the entity
    @param caseType Type of the case 
  -->
  <xsl:template name="INTERNAL-WriteCreatePagesForEntity-WithEndDateWizard">
    
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    
    <xsl:variable name="wizardCreateContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardEndDateCreateUIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardEndDateCreate-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardEndDateContentVIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardEndDate-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardEndDateSelectUIMName"><xsl:call-template name="Utilities-getCreatePageName-WizardEndDate-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-WithEndDate-getStartingPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>
    
    <!-- Write the create content vim for use on all wizard pages -->
    <xsl:call-template name="CreateContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardCreateContentVIMName"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
      <xsl:with-param name="endDateWizard" select="'true'"/>
    </xsl:call-template>
    
    <!-- Write the create UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardCreateUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardEndDateCreateUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardCreateContentVIMName"/>
      <xsl:with-param name="wizardEndDateUIMName" select="$wizardEndDateSelectUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
      <xsl:with-param name="endDateWizard" select="'true'"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
    </xsl:call-template>
    
    <!-- Write the select content vim for use on the Select All wizard page -->
    <xsl:call-template name="WizardEndDateContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardEndDateContentVIMName"/>
    </xsl:call-template>
    
    <!-- Write the select UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardEndDateUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardEndDateSelectUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardEndDateContentVIMName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>
   
  </xsl:template>
  <!-- END, 191675, JAY -->
  
</xsl:stylesheet>
