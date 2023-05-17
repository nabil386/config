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
<!--
  This stylesheet contains utility templates related to the create pages
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
  
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Templates for names of wizard starting pages
  -->
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Template to return the name of the generated starting page for use in the Select All wizard.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-CreateWizards-SelectAll-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:when><xsl:otherwise><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-SelectPage"><xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/></xsl:call-template></xsl:otherwise>
    </xsl:choose></xsl:template>
  
  <!--
    Template to return the name of the generated starting page for use in the Select Employment Only All wizard.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-CreateWizards-SelectEmploymentOnly-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>_only</xsl:template>
  
  <!--
    Template to return the name of the generated starting page for use in the wizards where a record has been pre selected..
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
    @param relatedEntityName The name of the related entity that was pre selected.
  -->
  <xsl:template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:when><xsl:otherwise><xsl:call-template name="Utilities-getCreatePageName-Wizard-SelectPage"><xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/><xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:otherwise>
    </xsl:choose></xsl:template>
  <!-- BEGIN, 191675, JAY -->  
  <!--
    Template to return the name of the generated starting page for use in the wizards where a evidence type has business end date.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
    @param relatedEntityName The name of the related entity that was pre selected.
  -->
  <xsl:template name="Utilities-CreateWizards-WithEndDate-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
  	<xsl:call-template name="Utilities-getCreatePageName-WizardEndDateCreate-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>  
  </xsl:template>
  <!-- JAY, 191675, JAY -->

  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Templates for names of resolve script pages and for create links
  -->
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Template to return the name of the generated create page for use in the Evidence_resolveCreate script
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-Evidence_resolveCreate">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="isTopLevel"><xsl:call-template name="Utilities-Entity-IsTopLevel"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:variable name="hasCoreEmployment"><xsl:call-template name="Utilities-Entity-HasCoreEmployment"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:choose>
      <xsl:when test="$isTopLevel='true' and $hasCoreEmployment='false'">
        <xsl:call-template name="Utilities-getCreatePageName-StandaloneContentPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$isTopLevel='true' and $hasCoreEmployment='true'">
        <xsl:call-template name="Utilities-CreateWizards-SelectEmploymentOnly-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="Utilities-CreateWizards-SelectAll-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!--
    Template to return the name of the generated create page when coming from the business object tab of a related type
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
    @param 
  -->
  <xsl:template name="Utilities-getCreatePageName-ResolveCreateEvidenceFromRelated">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="fromRelated"/>
    <xsl:variable name="hasParent"><xsl:call-template name="Utilities-Entity-HasParent"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:variable name="hasPreAssociation"><xsl:call-template name="Utilities-Entity-HasPreAssociation"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:variable name="hasMandatoryParents"><xsl:call-template name="Utilities-Entity-HasMultipleMandatoryParents"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:variable name="hasCoreEmployment"><xsl:call-template name="Utilities-Entity-HasCoreEmployment"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:choose>
      <xsl:when test="($hasParent='true' and $hasPreAssociation='true') or $hasMandatoryParents='true'">
        <xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
          <xsl:with-param name="relatedEntityName" select="$fromRelated"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$hasCoreEmployment='true'">
        <xsl:call-template name="Utilities-CreateWizards-SelectEmploymentOnly-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="Utilities-getCreatePageName-StandaloneContentPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <!--
    Template to return the name of the generated resolve script for deciding the
    correct create page to use when creating from the business object tab of a related entity
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getResolveScriptName-FromRelatedBusinessObject">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$entityNameCaseType"/>_fromRelated</xsl:template>
  
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Templates for names of generated pages
  -->
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Template to return the name of the generated page for selecting an employment to relate to an entity. This page
    will always be the first page of a wizard.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
    @param relatedEntityName Name of the related Entity
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:variable name="relatedNameExtension"><xsl:call-template name="Utilities-getFileExtension-RelatedEntitiyName"><xsl:with-param name="entityElement" select="$entityElement"/>
    <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:variable>
    <xsl:value-of select="$prefix"/>_selectEmp<xsl:value-of select="$entityNameCaseType"/><xsl:value-of select="$relatedNameExtension"/></xsl:template>
  
  <!--
    Template to return the name of the generated content vim page for selecting an employment to relate to an entity. This page
    will always be the first page of a wizard.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
    @param relatedEntityName Name of the related Entity
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:call-template name="Utilities-getCreatePageName-WizardSelectEmployment-SelectPage"><xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/><xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template>Content</xsl:template>
  
  <!--
    Template to return the name of the generated create page for use standalone (i.e. outside of a wizard).
    This is for entities that do not need a related object selected, or if its already implicitly selected
    
    (e.g. creating a child from its parents business object tab)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-StandaloneContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_sa</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_sa</xsl:otherwise>
    </xsl:choose></xsl:template>
 <!-- BEGIN, 191675, JAY -->   
 <!--
    Template to return the name of the generated wizard create page for use in the End Date wizard.
    This is for entities that need a related object selected,.
    
    (e.g. ending active evidence when creating new evidence from Evidence workspace)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardEndDateCreate-ContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
      <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0">
        <xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzEndDateCre</xsl:when>
		<xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzEndDateCre</xsl:otherwise>
    </xsl:choose></xsl:template>
    <!-- END, 191675, JAY -->
  
  <!--
    Template to return the name of the generated select page for use in the Select All wizard.
    This is for entities that need a related object selected,.
    
    (e.g. creating a child from the In Edit Evidence workspace)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardEndDate-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0">
     	<xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzEndDateSelect</xsl:when>
     	<xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzEndDateSelect</xsl:otherwise>
	</xsl:choose></xsl:template>
  <!-- BEGIN, 191675, JAY -->
  <!--
    Template to return the name of the generated content VIM for use in the select
    page of the  End Date wizard.
    This is for entities that need a related object selected,.
    
    (e.g. ending active evidence when creating new evidence from Evidence workspace)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardEndDate-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0">
 		<xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzEndDateContent</xsl:when>
     	<xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzEndDateSelect</xsl:otherwise>
    </xsl:choose></xsl:template>
  
  <!-- END, 191675, JAY -->
  <!--
    Template to return the name of the generated create page for use in the Select All wizard.
    This is for entities that need a related object selected,.
    
    (e.g. creating a child from the In Edit Evidence workspace)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardSelectAll-ContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose>
      <xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzSelAllCre</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzSelAllCre</xsl:otherwise>
    </xsl:choose></xsl:template>
  
  <!--
    Template to return the name of the generated select page for use in the Select All wizard.
    This is for entities that need a related object selected,.
    
    (e.g. creating a child from the In Edit Evidence workspace)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardSelectAll-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose>
      <xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzSelAll</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzSelAll</xsl:otherwise>
    </xsl:choose></xsl:template>
  
  <!--
    Template to return the name of the generated content VIM for use in the select
    page of the Select All wizard.
    This is for entities that need a related object selected,.
    
    (e.g. creating a child from the In Edit Evidence workspace)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-WizardSelectAll-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose>
      <xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzSelAllContent</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzSelAllContent</xsl:otherwise>
    </xsl:choose></xsl:template>
  
  <!--
    Template to return the name of the generated create page for use in the Select All wizard.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
    @relatedEntityName Name of the related entity thats been pre selected
  -->
  <xsl:template name="Utilities-getCreatePageName-Wizard-ContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template><xsl:call-template name="Utilities-getFileExtension-RelatedEntitiyName"><xsl:with-param name="entityElement" select="$entityElement"/>
    <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:template>
  
  <!--
    Template to return the name of the generated select page for use in the Select All wizard.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
    @relatedEntityName Name of the related entity thats been pre selected
  -->
  <xsl:template name="Utilities-getCreatePageName-Wizard-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template><xsl:call-template name="Utilities-getFileExtension-RelatedEntitiyName"><xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:template>
  
  <!--
    Template to return the name of the generated content VIM for use in the select
    page of awizard.
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case
    @relatedEntityName Name of the related entity thats been pre selected
  -->
  <xsl:template name="Utilities-getCreatePageName-Wizard-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template><xsl:call-template name="Utilities-getFileExtension-RelatedEntitiyName"><xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:template>
  
  <!--
    Template to return the name of the generated create content vim for use standalone (i.e. outside of a wizard).
    This is for entities that do not need a related object selected, or if its already implicitly selected
    
    (e.g. creating a child from its parents business object tab)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-StandaloneContentVim">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_saContent</xsl:template>
  
  <!--
    Template to return the name of the generated create content vim for use in wizard applications.
    This is for entities that do need at least one related object selected, and its not implicitly selected
    
    (e.g. creating a child from the In Edit Evidence workspace)
    
    @param entityElement XML Element for the current entity
    @param caseType Type of the case 
  -->
  <xsl:template name="Utilities-getCreatePageName-Wizard-ContentVim">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:value-of select="$prefix"/>_create<xsl:value-of select="$entityNameCaseType"/>_wzContent</xsl:template>
  
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Templates for names of  pre selected evidence id and type parameters
  -->
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Template to return the name of the evidenceID param generated for the related entity type. This is to be used by stylesheets
    generating resovle scripts that need to know where to pass their parameter, and also in the generated create page stylesheets.
    
    @param entityElement XML Element for the current entity
    @param fromRelated Name fo the related type 
  -->
  <xsl:template name="Utilities-getCreatePage-EvidenceIDParamName">
    <xsl:param name="entityElement"/>
    <xsl:param name="fromRelated"/>
    <xsl:choose>
      <xsl:when test="$entityElement/Relationships/Parent/@name=$fromRelated">parEvID</xsl:when>
      <xsl:when test="$entityElement/Relationships/MandatoryParents/Parent/@name=$fromRelated"><xsl:value-of select="$fromRelated"/>ParEvID</xsl:when>
      <xsl:when test="$entityElement/Relationships/PreAssociation/@to=$fromRelated">preAssocID</xsl:when>
    </xsl:choose>
    
  </xsl:template>
  
  <!--
    Template to return the name of the evidenceID param generated for the related entity type. This is to be used by stylesheets
    generating resovle scripts that need to know where to pass their parameter, and also in the generated create page stylesheets.
    
    @param entityElement XML Element for the current entity
    @param fromRelated Name fo the related type 
  -->
  <xsl:template name="Utilities-getCreatePage-EvidenceTypeParamName">
    <xsl:param name="entityElement"/>
    <xsl:param name="fromRelated"/>
    <xsl:choose>
      <xsl:when test="$entityElement/Relationships/Parent/@name=$fromRelated">parEvType</xsl:when>
      <xsl:when test="$entityElement/Relationships/MandatoryParents/Parent/@name=$fromRelated"><xsl:value-of select="$fromRelated"/>ParEvType</xsl:when>
      <xsl:when test="$entityElement/Relationships/PreAssociation/@to=$fromRelated">preAssocType</xsl:when>
    </xsl:choose>
    
  </xsl:template>
  
  <xsl:template name="Utilities-getFileExtension-RelatedEntitiyName">
    <xsl:param name="entityElement"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:choose><xsl:when test="count($entityElement/Relationships/Parent[@name=$relatedEntityName]) &gt; 0">PAR</xsl:when><xsl:when test="count($entityElement/Relationships/PreAssociation[@to=$relatedEntityName]) &gt; 0">PRE</xsl:when>
      <xsl:otherwise><xsl:value-of select="$relatedEntityName"/></xsl:otherwise></xsl:choose></xsl:template>
  
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Templates for returnign info on an entities relationships
  -->
  <!-- *************************************************************************************************************************************** -->
  <!-- *************************************************************************************************************************************** -->
  <!--
    Template to return whether or not the entity is top level (i.e. never requires another record pre selected).
    
    @param entityElement XML Element wiht the entity in it
    
    @return true or false
  -->
  <xsl:template name="Utilities-Entity-IsTopLevel">
    
    <xsl:param name="entityElement"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/Parent)=0 and count($entityElement/Relationships/MandatoryParents)=0 and count($entityElement/Relationships/PreAssociation)=0">true</xsl:when>
      <xsl:otherwise>false</xsl:otherwise>
    </xsl:choose>
    
  </xsl:template>
  
  <!--
    Template to return whether or not the entity has multiple mandatory parents
    
    @param entityElement XML Element wiht the entity in it
    
    @return true or false
  -->
  <xsl:template name="Utilities-Entity-HasMultipleMandatoryParents">
    
    <xsl:param name="entityElement"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/MandatoryParents) = 0">false</xsl:when>
      <xsl:otherwise>true</xsl:otherwise>
    </xsl:choose>
    
  </xsl:template>
  
  <!--
    Template to return whether or not the entity has a Parent
    
    @param entityElement XML Element wiht the entity in it
    
    @return true or false
  -->
  <xsl:template name="Utilities-Entity-HasParent">
    
    <xsl:param name="entityElement"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/Parent) = 0">false</xsl:when>
      <xsl:otherwise>true</xsl:otherwise>
    </xsl:choose>
    
  </xsl:template>
  
  <!--
    Template to return whether or not the entity has a pre association
    
    @param entityElement XML Element wiht the entity in it
    
    @return true or false
  -->
  <xsl:template name="Utilities-Entity-HasPreAssociation">
    
    <xsl:param name="entityElement"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/PreAssociation) = 0">false</xsl:when>
      <xsl:otherwise>true</xsl:otherwise>
    </xsl:choose>
    
  </xsl:template>
  
  <!--
    Template to return whether or not the entity is related to core Employment
    
    @param entityElement XML Element wiht the entity in it
    
    @return true or false
  -->
  <xsl:template name="Utilities-Entity-HasCoreEmployment">
    
    <xsl:param name="entityElement"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/Related[@to='Employment']) = 0">false</xsl:when>
      <xsl:otherwise>true</xsl:otherwise>
    </xsl:choose>
    
  </xsl:template>
  
  <!-- BEGIN, 191675, JAY -->
  <!--
    Template to return whether or not the entity is related to End Date Wizard
    
    @param entityElement XML Element with the entity in it
    
    @return true or false
  -->
  <xsl:template name="Utilities-Entity-HasAutoEndDate">
    
    <xsl:param name="entityElement"/>
    <xsl:variable name="hasBusinessEndDate">
      <xsl:choose>
        <xsl:when test="$entityElement/BusinessDates/@endDate=''">false</xsl:when>
        <xsl:otherwise>true</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="hasAutoEndDateActive">
      <xsl:choose>
        <xsl:when test="$entityElement/AutoEndDate/@active='Yes'">true</xsl:when>
        <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:choose>
   	  <xsl:when test="$hasBusinessEndDate='true' and $hasAutoEndDateActive='true'">true</xsl:when>
   	  <xsl:otherwise>false</xsl:otherwise>
    </xsl:choose>  	
  </xsl:template>
  
  <!-- 
  Template to return whether end date wizard applicable to the Entity
  
  @param entityElement XML Element with the entity in it
  
  @return true or false
   -->
   <xsl:template name="Utilities-Entity-EndDateWizardApplicable">
   
   	<xsl:param name="entityElement"/>
   
   	<xsl:variable name="isTopLevel"><xsl:call-template name="Utilities-Entity-IsTopLevel">
   		<xsl:with-param name="entityElement" select="$entityElement"/>
   	</xsl:call-template></xsl:variable>
   	<xsl:variable name="hasCoreEmployment"><xsl:call-template name="Utilities-Entity-HasCoreEmployment">
   		<xsl:with-param name="entityElement" select="$entityElement"/>
   	</xsl:call-template></xsl:variable>
   	<xsl:variable name="hasAutoEndDate"><xsl:call-template name="Utilities-Entity-HasAutoEndDate">
   		<xsl:with-param name="entityElement" select="$entityElement"/>
   	</xsl:call-template></xsl:variable>
   	
   	<xsl:choose>
   		<xsl:when test="$isTopLevel='true' and $hasAutoEndDate='true' and $hasCoreEmployment='false'">true</xsl:when>
   		<xsl:otherwise>false</xsl:otherwise>
   	</xsl:choose>
   </xsl:template>
   <!-- END, 191675, JAY -->
  <!--
    Template to return the 'Save and New' link

    @param entityElement XML Element with the entity in it

    @return true or false
  -->
  <xsl:template name="Utilities-getCreatePageName-getSaveAndNewLink">

    <xsl:param name="entityElement"/>
    <xsl:param name="pageID"/>

    
    <ACTION_CONTROL
      IMAGE="SaveAndNewButton"
      LABEL="ActionControl.Label.SaveAndNew"
      TYPE="SUBMIT"
    >          
        <!-- BEGIN, CR00266438, CD -->      
      <LINK PAGE_ID="{$pageID}" DISMISS_MODAL="false" SAVE_LINK="false">
        <!-- END, CR00266438, CD -->
        <!-- Page Parameters and their standard connections -->

        <!-- CaseID -->
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>

        <!-- Parent -->
        <xsl:if test="count($entityElement/Relationships/Parent) &gt; 0">
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/Parent[1]/@name"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/Parent[1]/@name"/>
          </xsl:call-template></xsl:variable>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
          <TARGET NAME="PAGE" PROPERTY="{$idParamName}"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
          <TARGET NAME="PAGE" PROPERTY="{$typeParamName}"/>
        </CONNECT>
        </xsl:if>

        <!-- Multiple Mandatory Parents -->
        <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
          <xsl:variable name="aggregationName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="@name"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="@name"/>
          </xsl:call-template></xsl:variable>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
          <TARGET NAME="PAGE" PROPERTY="{$idParamName}"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
          <TARGET NAME="PAGE" PROPERTY="{$typeParamName}"/>
        </CONNECT>
        </xsl:for-each>  

        <!-- PreAssociations -->
        <xsl:if test="$entityElement/Relationships/@preAssociation='Yes'">
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getCreatePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
          </xsl:call-template></xsl:variable>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
          <TARGET NAME="PAGE" PROPERTY="{$idParamName}"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
          <TARGET NAME="PAGE" PROPERTY="{$typeParamName}"/>
        </CONNECT>
        </xsl:if>

        <!-- Related Employment -->
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="employmentID"/>
          <TARGET NAME="PAGE" PROPERTY="employmentID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="employerConcernRoleID"/>
          <TARGET NAME="PAGE" PROPERTY="employerConcernRoleID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
          <TARGET NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
        </CONNECT>
        </xsl:if>
        
      </LINK>
    </ACTION_CONTROL>    
  </xsl:template>
        
        
        
</xsl:stylesheet>
