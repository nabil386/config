<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
  This stylesheet contains utility templates related to the incoming add to case pages
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
  <xsl:import href="../Create/CreateUtilityTemplates.xslt"/>

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
  <xsl:template name="Utilities-IncomingAddToCaseWizards-SelectAll-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:when><xsl:otherwise><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectPage"><xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/></xsl:call-template></xsl:otherwise>
    </xsl:choose></xsl:template>

  <!--
    Template to return the name of the generated starting page for use in the Select Employment Only All wizard.

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-IncomingAddToCaseWizards-SelectEmploymentOnly-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>_only</xsl:template>

  <!--
    Template to return the name of the generated starting page for use in the wizards where a record has been pre selected..

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
    @param relatedEntityName The name of the related entity that was pre selected.
  -->
  <xsl:template name="Utilities-IncomingAddToCaseWizards-WithPreSelection-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:choose>
      <xsl:when test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:when><xsl:otherwise><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectPage"><xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/><xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:otherwise>
    </xsl:choose></xsl:template>
  <!-- BEGIN, 191675, JAY -->
  <!--
    Template to return the name of the generated starting page for use in the wizards where a evidence type has business end date.

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
    @param relatedEntityName The name of the related entity that was pre selected.
  -->
  <xsl:template name="Utilities-IncomingAddToCaseWizards-WithEndDate-getStartingPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
  	<xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardEndDateIncomingAddToCase-ContentPage">
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
    Template to return the name of the generated create page for use in the Evidence_resolveIncomingAddToCase script

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-Evidence_resolveIncomingAddToCase">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="isTopLevel"><xsl:call-template name="Utilities-Entity-IsTopLevel"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:variable name="hasCoreEmployment"><xsl:call-template name="Utilities-Entity-HasCoreEmployment"><xsl:with-param name="entityElement" select="$entityElement"/></xsl:call-template></xsl:variable>
    <xsl:choose>
      <xsl:when test="$isTopLevel='true' and $hasCoreEmployment='false'">
        <xsl:call-template name="Utilities-getIncomingAddToCasePageName-StandaloneContentPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$isTopLevel='true' and $hasCoreEmployment='true'">
        <xsl:call-template name="Utilities-IncomingAddToCaseWizards-SelectEmploymentOnly-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="Utilities-IncomingAddToCaseWizards-SelectAll-getStartingPage">
          <xsl:with-param name="entityElement" select="$entityElement"/>
          <xsl:with-param name="caseType" select="$caseType"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

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
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:variable name="relatedNameExtension"><xsl:call-template name="Utilities-getFileExtension-RelatedEntitiyName"><xsl:with-param name="entityElement" select="$entityElement"/>
    <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:variable>
    <xsl:value-of select="$prefix"/>_addToCaseSelectEmp<xsl:value-of select="$entityNameCaseType"/><xsl:value-of select="$relatedNameExtension"/></xsl:template>

  <!--
    Template to return the name of the generated content vim page for selecting an employment to relate to an entity. This page
    will always be the first page of a wizard.

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
    @param relatedEntityName Name of the related Entity
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage"><xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/><xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template>Content</xsl:template>

  <!--
    Template to return the name of the generated create page for use standalone (i.e. outside of a wizard).
    This is for entities that do not need a related object selected, or if its already implicitly selected

    (e.g. creating a child from its parents business object tab)

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-StandaloneContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_sa</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_sa</xsl:otherwise>
    </xsl:choose></xsl:template>
 <!-- BEGIN, 191675, JAY -->
 <!--
    Template to return the name of the generated wizard create page for use in the End Date wizard.
    This is for entities that need a related object selected,.

    (e.g. ending active evidence when creating new evidence from Evidence workspace)

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardEndDateIncomingAddToCase-ContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
      <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0">
        <xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzEndDateCre</xsl:when>
		<xsl:otherwise><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzEndDateCre</xsl:otherwise>
    </xsl:choose></xsl:template>
    <!-- END, 191675, JAY -->

  <!--
    Template to return the name of the generated select page for use in the Select All wizard.
    This is for entities that need a related object selected,.

    (e.g. creating a child from the In Edit Evidence workspace)

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardEndDate-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0">
     	<xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzEndDateSelect</xsl:when>
     	<xsl:otherwise><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzEndDateSelect</xsl:otherwise>
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
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardEndDate-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose><xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0">
 		<xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzEndDateContent</xsl:when>
     	<xsl:otherwise><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzEndDateSelect</xsl:otherwise>
    </xsl:choose></xsl:template>

  <!-- END, 191675, JAY -->
  <!--
    Template to return the name of the generated create page for use in the Select All wizard.
    This is for entities that need a related object selected,.

    (e.g. creating a child from the In Edit Evidence workspace)

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-ContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose>
      <xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzSelAllCre</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzSelAllCre</xsl:otherwise>
    </xsl:choose></xsl:template>

  <!--
    Template to return the name of the generated select page for use in the Select All wizard.
    This is for entities that need a related object selected,.

    (e.g. creating a child from the In Edit Evidence workspace)

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose>
      <xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzSelAll</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzSelAll</xsl:otherwise>
    </xsl:choose></xsl:template>

  <!--
    Template to return the name of the generated content VIM for use in the select
    page of the Select All wizard.
    This is for entities that need a related object selected,.

    (e.g. creating a child from the In Edit Evidence workspace)

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:choose>
      <xsl:when
        test="count($entityElement/Relationships/Parent) = 0 and count($entityElement/Relationships/MandatoryParents) = 0 and count($entityElement/Relationships/MandatoryPreAssociation) = 0"
        ><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzSelAllContent</xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzSelAllContent</xsl:otherwise>
    </xsl:choose></xsl:template>

  <!--
    Template to return the name of the generated create page for use in the Select All wizard.

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
    @relatedEntityName Name of the related entity thats been pre selected
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-ContentPage">
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
  <xsl:template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectPage">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectPage">
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
  <xsl:template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectContentVIM">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectContentVIM">
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
  <xsl:template name="Utilities-getIncomingAddToCasePageName-StandaloneContentVim">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_saContent</xsl:template>

  <!--
    Template to return the name of the generated create content vim for use in wizard applications.
    This is for entities that do need at least one related object selected, and its not implicitly selected

    (e.g. creating a child from the In Edit Evidence workspace)

    @param entityElement XML Element for the current entity
    @param caseType Type of the case
  -->
  <xsl:template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentVim">
    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:variable name="entityNameCaseType"><xsl:value-of select="$entityElement/@name"/><xsl:value-of select="$caseType"/></xsl:variable>
    <xsl:value-of select="$prefix"/>_addToCase<xsl:value-of select="$entityNameCaseType"/>_wzContent</xsl:template>

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
  <xsl:template name="Utilities-getIncomingAddToCasePage-EvidenceIDParamName">
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
  <xsl:template name="Utilities-getIncomingAddToCasePage-EvidenceTypeParamName">
    <xsl:param name="entityElement"/>
    <xsl:param name="fromRelated"/>
    <xsl:choose>
      <xsl:when test="$entityElement/Relationships/Parent/@name=$fromRelated">parEvType</xsl:when>
      <xsl:when test="$entityElement/Relationships/MandatoryParents/Parent/@name=$fromRelated"><xsl:value-of select="$fromRelated"/>ParEvType</xsl:when>
      <xsl:when test="$entityElement/Relationships/PreAssociation/@to=$fromRelated">preAssocType</xsl:when>
    </xsl:choose>

  </xsl:template>


</xsl:stylesheet>
