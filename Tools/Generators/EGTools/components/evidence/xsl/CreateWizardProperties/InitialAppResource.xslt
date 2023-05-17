<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2010, 2017. All Rights Reserved.

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

  <xsl:import href="../UserInterface/CreateProperties.xslt"/>

  <xsl:output method="text" indent="yes"/>

  <xsl:include href="../EvidenceCommon.xslt" />
  <xsl:include href="../UserInterface/Create/CreateUtilityTemplates.xslt"/>
  <xsl:include href="../UserInterface/Incoming/IncomingAddToCaseUtilityTemplates.xslt"/>

  <xsl:param name="rootPath"/>
  <xsl:param name="localeList"/>
  <xsl:param name="propertyPath"/>
  <xsl:param name="caseType"/>

  <!--
    Template to write the wizard properties files and DMX entries for all entities
  -->
  <xsl:template match="EvidenceEntity[UserInterfaceLayer]">

    <xsl:variable name="propertiesDir">./EvGen/data/initial/blob/</xsl:variable>
    <xsl:variable name="entityElement" select="."/>
    <xsl:variable name="entityName" select="$entityElement/@name"/>

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
     <xsl:variable name="hasAutoEndDate">
      <xsl:call-template name="Utilities-Entity-HasAutoEndDate">
        <xsl:with-param name="entityElement" select="$entityElement"/>
      </xsl:call-template>
    </xsl:variable>


    <!-- Top level entities need no wizards unless they have core employments or business end date -->
    <xsl:if test="$isTopLevel='false' or $hasCoreEmployment='true' or $hasAutoEndDate='true'">
     <!-- END, 191675, JAY -->
      <xsl:variable name="filename" select="concat($rootPath, 'INITIALAPPRESOURCE.dmx')"/>

      <redirect:open select="$filename" method="text" append="true" />
      <redirect:write select="$filename">

        <!-- If a top level entity is related to employment, then write the appropriate wizard -->
        <xsl:if test="($isTopLevel='true' and $hasCoreEmployment='true')">

          <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-SelectEmploymentOnly-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

           <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-SelectEmploymentOnly-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardNameIncoming"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>
        </xsl:if>

        <!-- BEGIN, 191675, JAY -->
        <!-- If top level entity has business end date then write the appropriate wizard -->
        <xsl:if test="($isTopLevel='true' and $hasAutoEndDate='true' and $hasCoreEmployment='false')">

          <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-WithEndDate-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

          <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-WithEndDate-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
          </xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardNameIncoming"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

        </xsl:if>
        <!-- END, 191675, JAY -->
        <!--
          If at least one higher relationship then need to write the SelectAll Wizard
        -->
        <xsl:if test="$hasParent='true' or $hasPreAssociation='true' or $hasMultipleMandatoryParents='true'">

          <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-SelectAll-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"></xsl:with-param>
          </xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

           <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-SelectAll-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"></xsl:with-param>
          </xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardNameIncoming"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

        </xsl:if>

        <!-- Write wizard for each mandatory parent -->
        <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">

          <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="@name"/></xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

           <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="@name"/></xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$wizardNameIncoming"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

        </xsl:for-each>

        <!--
          If both parent and pre association exists
        -->
        <xsl:if test="$hasParent='true' and $hasPreAssociation='true'">

          <xsl:variable name="parentWizardName"><xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/></xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$parentWizardName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

          <xsl:variable name="preAssocWizardName"><xsl:call-template name="Utilities-CreateWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/></xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$preAssocWizardName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

           <xsl:variable name="parentWizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/></xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$parentWizardNameIncoming"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

          <xsl:variable name="preAssocWizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-WithPreSelection-getStartingPage">
            <xsl:with-param name="entityElement" select="$entityElement"/><xsl:with-param name="caseType" select="$caseType"/>
            <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/></xsl:call-template></xsl:variable>

          <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="$localeList"/>
            <xsl:with-param name="propertiesFileName" select="$preAssocWizardNameIncoming"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

        </xsl:if>


      </redirect:write>
      <redirect:close select="$filename"/>

      <xsl:call-template name="INTERNAL-WriteWizardProperties-AllLocales">
        <xsl:with-param name="locales" select="$localeList"/>
        <xsl:with-param name="filePath" select="$propertyPath"/>
        <xsl:with-param name="entityElement" select="$entityElement"/>
      </xsl:call-template>
    </xsl:if>

  </xsl:template>


  <xsl:template name="INTERNAL-WriteDMX-AllLocales">

    <xsl:param name="locales"/>
    <xsl:param name="propertiesFileName"/>
    <xsl:param name="propertiesDir"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

	      <xsl:call-template name="INTERNAL-WriteDMX">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="propertiesFileName" select="$propertiesFileName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
	      <xsl:call-template name="INTERNAL-WriteDMX-AllLocales">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="propertiesFileName" select="$propertiesFileName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
	      <xsl:call-template name="INTERNAL-WriteDMX">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="propertiesFileName" select="$propertiesFileName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="INTERNAL-WriteDMX">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="propertiesFileName" select="$propertiesFileName"/>
            <xsl:with-param name="propertiesDir" select="$propertiesDir"></xsl:with-param>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>

  </xsl:template>

  <!--
    Template to write a DMX entry for a given wizard properties file

    @param propertiesFileName name of the properties file
    @param propertiesDir The directory its located in.
  -->
  <xsl:template name="INTERNAL-WriteDMX">
    <xsl:param name="locale"/>
    <xsl:param name="propertiesFileName"/>
    <xsl:param name="propertiesDir"/>

	<xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/*)&gt;0">

	<xsl:variable name="localeIdentifier" select="substring($locale, (substring($locale,1,1)='_') +1)"/>

    &lt;row&gt;
    &lt;attribute name="localeIdentifier"&gt;
    &lt;value&gt;<xsl:value-of select="$localeIdentifier"/>&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="name"&gt;
    &lt;value&gt;<xsl:value-of select="$propertiesFileName"/>Wizard.properties&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="contentType"&gt;
    &lt;value&gt;text/plain&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="contentDisposition"&gt;
    &lt;value>inline&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="content"&gt;
    &lt;value><xsl:value-of select="$propertiesDir"/><xsl:value-of select="$propertiesFileName"/>Wizard<xsl:value-of select="$locale"/>.properties&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="internal"&gt;
    &lt;value>0&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="lastWritten"&gt;
    &lt;value>SYSTIME&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="versionNo"&gt;
    &lt;value>1&lt;/value&gt;
    &lt;/attribute&gt;
    &lt;attribute name="category"&gt;
    &lt;value/&gt;
    &lt;/attribute&gt;
    &lt;/row&gt;
	</xsl:if>
  </xsl:template>

  <!--
    Template to write the wizard properties files in all requried locales

    @param locale The locales to write
    @param filePath The file path to write
    @param wizardName Name of the wizard, its select page, and the start of the properties file name
    @param createPageName The create page name (page 2) of the wizard

  -->
  <xsl:template name="INTERNAL-WriteWizardProperties-AllLocales">

    <xsl:param name="locales"/>
    <xsl:param name="filePath"/>
    <xsl:param name="entityElement"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="INTERNAL-WriteWizardProperties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="filePath" select="$filePath"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="INTERNAL-WriteWizardProperties-AllLocales">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="filePath" select="$filePath"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="INTERNAL-WriteWizardProperties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="filePath" select="$filePath"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="INTERNAL-WriteWizardProperties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="filePath" select="$filePath"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

  <!--
    Template to write the wizard properties files

    @param locale The locale to write
    @param filePath The file path to write
    @param wizardName Name of the wizard, its select page, and the start of the properties file name
    @param createPageName The create page name (page 2) of the wizard

  -->
  <xsl:template name="INTERNAL-WriteWizardProperties">
    <xsl:param name="locale"/>
    <xsl:param name="filePath"/>
    <xsl:param name="entityElement"/>

	<xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/*)&gt;0">

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
    <xsl:variable name="hasAutoEndDate">
      <xsl:call-template name="Utilities-Entity-HasAutoEndDate">
        <xsl:with-param name="entityElement" select="$entityElement"/>
      </xsl:call-template>
    </xsl:variable>
    <!-- END, 191675, JAY -->

    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

    <xsl:variable name="text.selectEmployment"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Wizard.Text.SelectEmployment"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
      <xsl:with-param name="altPropertyName">Wizard.Item.Text</xsl:with-param>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="title.selectEmployment"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Wizard.Title.SelectEmployment"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
      <xsl:with-param name="altPropertyName">Title</xsl:with-param>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="text.selectEvidence"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Wizard.Text.SelectEvidence"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
      <xsl:with-param name="altPropertyName">Wizard.Item.Text</xsl:with-param>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="title.selectEvidence"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Wizard.Title.SelectEvidence"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
      <xsl:with-param name="altPropertyName">Title</xsl:with-param>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="title.newEvidence"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.NewEntity"/>
      <xsl:with-param name="evidenceNode" select="$entityElement"/>
      <xsl:with-param name="altPropertyName">Title</xsl:with-param>
    </xsl:call-template></xsl:variable>


    <xsl:variable name="text.newEvidence"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.NewEntity"/>
      <xsl:with-param name="evidenceNode" select="$entityElement"/>
      <xsl:with-param name="altPropertyName">Wizard.Item.Text</xsl:with-param>
    </xsl:call-template></xsl:variable>

    <!-- BEGIN, 191675, JAY -->
    <xsl:variable name="text.selectEndDateEvidence"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Wizard.Text.SelectEndDateEvidence"/>
      <xsl:with-param name="evidenceNode" select="$entityElement"/>
      <xsl:with-param name="altPropertyName">Wizard.Item.Text</xsl:with-param>
    </xsl:call-template></xsl:variable>

    <xsl:variable name="title.selectEndDateEvidence"><xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Wizard.Title.SelectEndDateEvidence"/>
      <xsl:with-param name="evidenceNode" select="$entityElement"/>
      <xsl:with-param name="altPropertyName">Title</xsl:with-param>
    </xsl:call-template></xsl:variable>
    <!-- END, 191675, JAY -->
    <!-- If a top level entity is related to employment, then write the appropriate wizard -->
    <xsl:if test="($isTopLevel='true' and $hasCoreEmployment='true')">

      <xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-SelectEmploymentOnly-getStartingPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="createPageName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>EmpOnly</xsl:variable>

      <redirect:write select="concat($filePath, $wizardName, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardName"/>.<xsl:value-of select="$text.selectEmployment"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.<xsl:value-of select="$title.selectEmployment"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardName"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$createPageName"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$createPageName"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$createPageName"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$createPageName"/>

      </redirect:write>

      <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-SelectEmploymentOnly-getStartingPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="createPageNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template>EmpOnly</xsl:variable>

      <redirect:write select="concat($filePath, $wizardNameIncoming, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardNameIncoming"/>.<xsl:value-of select="$text.selectEmployment"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.selectEmployment"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardNameIncoming"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$createPageNameIncoming"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$createPageNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$createPageNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$createPageNameIncoming"/>

      </redirect:write>
    </xsl:if>

    <!-- BEGIN, 191675, JAY -->
    <!-- If a top level entity has business end date and not related to employment, then write the appropriate wizard -->
    <xsl:if test="($isTopLevel='true' and $hasAutoEndDate='true' and $hasCoreEmployment='false')">

    	<xsl:variable name="wizardName"><xsl:call-template name="Utilities-CreateWizards-WithEndDate-getStartingPage">
    		<xsl:with-param name="entityElement" select="$entityElement"/>
    		<xsl:with-param name="caseType" select="$caseType"/>
    	</xsl:call-template></xsl:variable>

    	<xsl:variable name="endDatePageName"><xsl:call-template name="Utilities-getCreatePageName-WizardEndDate-SelectPage">
    		<xsl:with-param name="entityElement" select="$entityElement"/>
    		<xsl:with-param name="caseType" select="$caseType"/>
    	</xsl:call-template></xsl:variable>

    	<redirect:write select="concat($filePath, $wizardName, 'Wizard', $locale, '.properties')">

    	Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardName"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardName"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$endDatePageName"/>.<xsl:value-of select="$text.selectEndDateEvidence"/>
        <xsl:value-of select="$endDatePageName"/>.Wizard.Page.<xsl:value-of select="$title.selectEndDateEvidence"/>
        <xsl:value-of select="$endDatePageName"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$endDatePageName"/>

        </redirect:write>

        <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-IncomingAddToCaseWizards-WithEndDate-getStartingPage">
    		<xsl:with-param name="entityElement" select="$entityElement"/>
    		<xsl:with-param name="caseType" select="$caseType"/>
    	</xsl:call-template></xsl:variable>

    	<xsl:variable name="endDatePageNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardEndDate-SelectPage">
    		<xsl:with-param name="entityElement" select="$entityElement"/>
    		<xsl:with-param name="caseType" select="$caseType"/>
    	</xsl:call-template></xsl:variable>

    	<redirect:write select="concat($filePath, $wizardNameIncoming, 'Wizard', $locale, '.properties')">

    	Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardNameIncoming"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardNameIncoming"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$endDatePageNameIncoming"/>.<xsl:value-of select="$text.selectEndDateEvidence"/>
        <xsl:value-of select="$endDatePageNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.selectEndDateEvidence"/>
        <xsl:value-of select="$endDatePageNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$endDatePageNameIncoming"/>

        </redirect:write>

    </xsl:if>
    <!-- END, 191675, JAY -->
    <!--
    If at least one higher relationship then need to write the SelectAll Wizard
    -->
    <xsl:if test="$hasParent='true' or $hasPreAssociation='true' or $hasMultipleMandatoryParents='true'">

      <xsl:variable name="createPageName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="wizardName"><xsl:call-template name="Utilities-getCreatePageName-WizardSelectAll-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $wizardName, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardName"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardName"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$createPageName"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$createPageName"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$createPageName"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$createPageName"/>

      </redirect:write>

      <xsl:variable name="createPageNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectAll-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $wizardNameIncoming, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardNameIncoming"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardNameIncoming"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$createPageNameIncoming"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$createPageNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$createPageNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$createPageNameIncoming"/>

      </redirect:write>
    </xsl:if>

    <!-- Write wizard for each mandatory parent -->
    <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">

      <xsl:variable name="createPageName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="@name"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="wizardName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="@name"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $wizardName, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardName"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$wizardName"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardName"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$createPageName"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$createPageName"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$createPageName"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$createPageName"/>

      </redirect:write>

      <xsl:variable name="createPageNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="@name"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="wizardNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="@name"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $wizardNameIncoming, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$wizardNameIncoming"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$wizardNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$wizardNameIncoming"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$createPageNameIncoming"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$createPageNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$createPageNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$createPageNameIncoming"/>

      </redirect:write>


    </xsl:for-each>

    <!--
      If both parent and pre association exists
    -->
    <xsl:if test="$hasParent='true' and $hasPreAssociation='true'">

      <xsl:variable name="parentCreatePage"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="parentWizardName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $parentWizardName, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$parentWizardName"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$parentWizardName"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$parentWizardName"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$parentWizardName"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$parentCreatePage"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$parentCreatePage"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$parentCreatePage"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$parentCreatePage"/>

      </redirect:write>

      <xsl:variable name="parentCreatePageIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="parentWizardNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/Parent[1]/@name"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $parentWizardNameIncoming, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$parentWizardNameIncoming"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$parentWizardNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$parentWizardNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$parentWizardNameIncoming"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$parentCreatePageIncoming"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$parentCreatePageIncoming"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$parentCreatePageIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$parentCreatePageIncoming"/>

      </redirect:write>

      <xsl:variable name="preAssocCreatePage"><xsl:call-template name="Utilities-getCreatePageName-Wizard-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="preAssocWizardName"><xsl:call-template name="Utilities-getCreatePageName-Wizard-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $preAssocWizardName, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$preAssocWizardName"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$preAssocWizardName"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$preAssocWizardName"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$preAssocWizardName"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$preAssocCreatePage"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$preAssocCreatePage"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$preAssocCreatePage"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$preAssocCreatePage"/>

      </redirect:write>

      <xsl:variable name="preAssocCreatePageIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-ContentPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
      </xsl:call-template></xsl:variable>

      <xsl:variable name="preAssocWizardNameIncoming"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-Wizard-SelectPage">
        <xsl:with-param name="entityElement" select="$entityElement"/>
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="relatedEntityName" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
      </xsl:call-template></xsl:variable>

      <redirect:write select="concat($filePath, $preAssocWizardNameIncoming, 'Wizard', $locale, '.properties')">

        Number.Wizard.Pages=2
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$preAssocWizardNameIncoming"/>.<xsl:value-of select="$text.selectEvidence"/>
        <xsl:value-of select="$preAssocWizardNameIncoming"/>.Wizard.Page.<xsl:value-of select="$title.selectEvidence"/>
        <xsl:value-of select="$preAssocWizardNameIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.1=<xsl:value-of select="$preAssocWizardNameIncoming"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:value-of select="$preAssocCreatePageIncoming"/>.<xsl:value-of select="$text.newEvidence"/>
        <xsl:value-of select="$preAssocCreatePageIncoming"/>.Wizard.Page.<xsl:value-of select="$title.newEvidence"/>
        <xsl:value-of select="$preAssocCreatePageIncoming"/>.Wizard.Page.Desc=
        Wizard.PageID.2=<xsl:value-of select="$preAssocCreatePageIncoming"/>

      </redirect:write>

    </xsl:if>
	</xsl:if>
  </xsl:template>
</xsl:stylesheet>