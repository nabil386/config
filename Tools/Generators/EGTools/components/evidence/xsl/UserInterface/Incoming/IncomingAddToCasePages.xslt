<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
  Main stylesheet for writing the Incoming Add To Case pages for an entity.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="../UICommon.xslt"/>

  <!-- Contains Utililities for setting variables -->
  <xsl:import href="../Create/CreateUtilityTemplates.xslt"/>

  <xsl:import href="IncomingAddToCaseUtilityTemplates.xslt"/>

  <xsl:import href="IncomingAddToCaseContentVIM.xslt"/>
  <xsl:import href="StandaloneIncomingAddToCaseUIM.xslt"/>
  <xsl:import href="WizardIncomingAddToCaseUIM.xslt"/>

  <xsl:import href="WizardIncomingAddToCaseSelectUIM.xslt"/>
  <xsl:import href="WizardIncomingAddToCaseSelectContentVIM.xslt"/>

  <xsl:import href="IncomingAddToCaseConstantsProperties.xslt"/>

  <!--
    Template to write the addToCase pages for an entity.

    @param entityElement XML Element for the entity
    @param caseType Type of the case
  -->
  <xsl:template name="WriteIncomingAddToCasePagesForEntity">

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
 <!--
      If a top level entity or only one related type then write the standalone addToCase pages.
    -->
    <xsl:if test="($isTopLevel='true' or ($hasParent='true' and $hasPreAssociation='false') or ($hasParent='false' and $hasPreAssociation='true')) and $hasCoreEmployment='false'">
      <xsl:call-template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-StandaloneIncomingAddToCase">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>
    </xsl:if>

    <!-- If a top level entitiy is related to employment, then write the appropriate wizard -->
    <xsl:if test="($isTopLevel='true' and $hasCoreEmployment='true')">
      <xsl:call-template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardEmploymentOnly">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>
    </xsl:if>

    <!--
      If at least one higher relationship then need to write the SelectAll Wizard
    -->
    <xsl:if test="$hasParent='true' or $hasPreAssociation='true' or $hasMultipleMandatoryParents='true'">
      <xsl:call-template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardSelectAll">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>
    </xsl:if>

    <!-- Write wizard for each mandatory parent -->
    <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
      <xsl:call-template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardWithPreSelection">
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
      <xsl:call-template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardWithPreSelection">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/>
      </xsl:call-template>

      <!-- One with a pre selected preassociation -->
      <xsl:call-template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardWithPreSelection">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
      </xsl:call-template>

    </xsl:if>

    <!-- Write the constants properties file for use in the addToCase pages -->
    <xsl:call-template name="IncomingAddToCaseConstantsProperties">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>
  </xsl:template>

  <!--
    Internal template to write the standalone addToCase pages for an entity.

    This gets the following pages generated
    - standalone content vim + properties
    - standalone content uim + properties

    @param entityElement XML Element for the entity
    @param caseType Type of the case
  -->
  <xsl:template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-StandaloneIncomingAddToCase">

    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>

    <xsl:variable name="StandaloneIncomingAddToCaseContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-StandaloneContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="StandaloneIncomingAddToCaseUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-StandaloneContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <!-- Write the addToCase content vim for use in standalone addToCase pages -->
    <xsl:call-template name="IncomingAddToCaseContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$StandaloneIncomingAddToCaseContentVIMName"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
    </xsl:call-template>

    <!-- Write the addToCase UIM Page for standalone addToCase pages -->
    <xsl:call-template name="StandaloneIncomingAddToCaseUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$StandaloneIncomingAddToCaseUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$StandaloneIncomingAddToCaseContentVIMName"/>
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
  <xsl:template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardEmploymentOnly">

    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>

    <xsl:variable name="wizardIncomingAddToCaseContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardIncomingAddToCaseUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>EmpOnly</xsl:variable>

    <xsl:variable name="wizardSelectEmploymentContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>_only</xsl:variable>

    <xsl:variable name="wizardSelectEmploymentUIMName"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-SelectEmploymentOnly-getStartingPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <!-- Write the addToCase content vim for use on all wizard pages -->
    <xsl:call-template name="IncomingAddToCaseContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardIncomingAddToCaseContentVIMName"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
    </xsl:call-template>

    <!-- Write the addToCase UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardIncomingAddToCaseUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardIncomingAddToCaseUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardIncomingAddToCaseContentVIMName"/>
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
      <xsl:with-param name="wizardSelectUIMName" select="$wizardIncomingAddToCaseUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardSelectEmploymentUIMName"/>
    </xsl:call-template>
  </xsl:template>

  <!--
    Internal template to write the 'SelectAll' wizard addToCase pages for an entity.

    This gets the following pages generated
    - wizard select vim + properties
    - wizard select uim + properties
    - wizard content vim + properties
    - wizard content uim + properties

    @param entityElement XML Element for the entity
    @param caseType Type of the case
  -->
  <xsl:template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardSelectAll">

    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>

    <xsl:variable name="wizardIncomingAddToCaseContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardSelectAllIncomingAddToCaseUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardSelectAllSelectContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardSelectAllSelectUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardName"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-SelectAll-getStartingPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <!-- Write the addToCase content vim for use on all wizard pages -->
    <xsl:call-template name="IncomingAddToCaseContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardIncomingAddToCaseContentVIMName"/>
      <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
    </xsl:call-template>

    <!-- Write the addToCase UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardIncomingAddToCaseUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectAllIncomingAddToCaseUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardIncomingAddToCaseContentVIMName"/>
      <xsl:with-param name="wizardSelectUIMName" select="$wizardSelectAllSelectUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>

    <!-- Write the select content vim for use on the Select All wizard page -->
    <xsl:call-template name="WizardIncomingAddToCaseSelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectAllSelectContentVIMName"/>
    </xsl:call-template>

    <!-- Write the select UIM page for use on all the Select All wizard -->
    <xsl:call-template name="WizardIncomingAddToCaseSelectUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectAllSelectUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardSelectAllSelectContentVIMName"/>
      <xsl:with-param name="wizardIncomingAddToCaseUIMName" select="$wizardSelectAllIncomingAddToCaseUIMName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>

    <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">

      <xsl:variable name="wizardSelectEmploymentContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardSelectEmploymentUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage">
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
    Internal template to write the 'wizard addToCase pages for an entity when the specified type is pre selected

    This gets the following pages generated
    - wizard select vim + properties
    - wizard select uim + properties
    - wizard content uim + properties

    @param entityElement XML Element for the entity
    @param caseType Type of the case
  -->
  <xsl:template name="INTERNAL-WriteIncomingAddToCasePagesForEntity-WizardWithPreSelection">

    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="relatedEntityName"/>

    <xsl:variable name="wizardIncomingAddToCaseContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentVim">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardIncomingAddToCaseUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardSelectContentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardSelectUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="wizardName"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-WithPreSelection-getStartingPage">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template></xsl:variable>

    <!-- Write the addToCase UIM page for use on all this wizard -->
    <xsl:call-template name="WizardIncomingAddToCaseUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardIncomingAddToCaseUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardIncomingAddToCaseContentVIMName"/>
      <xsl:with-param name="wizardSelectUIMName" select="$wizardSelectUIMName"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>

    <!-- Write the select content vim for use on thisl wizard page -->
    <xsl:call-template name="WizardIncomingAddToCaseSelectContentVIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectContentVIMName"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
    </xsl:call-template>

    <!-- Write the select UIM page for use on all thiswizard -->
    <xsl:call-template name="WizardIncomingAddToCaseSelectUIM">
      <xsl:with-param name="entityElement" select="$entityElement"/>
      <xsl:with-param name="caseType" select="$caseType"/>
      <xsl:with-param name="baseDirectory" select="concat($entityElement/@name, '/')"/>
      <xsl:with-param name="fileName" select="$wizardSelectUIMName"/>
      <xsl:with-param name="includeVIMFileName" select="$wizardSelectContentVIMName"/>
      <xsl:with-param name="wizardIncomingAddToCaseUIMName" select="$wizardIncomingAddToCaseUIMName"/>
      <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
      <xsl:with-param name="wizardName" select="$wizardName"/>
    </xsl:call-template>

    <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">

      <xsl:variable name="wizardSelectEmploymentVIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectContentVIM">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$relatedEntityName"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="wizardSelectEmploymentUIMName"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage">
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

</xsl:stylesheet>
