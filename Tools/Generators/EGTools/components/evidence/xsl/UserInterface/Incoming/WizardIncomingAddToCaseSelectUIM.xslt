<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
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

<!-- IncomingAddToCase specific templates -->
<xsl:import href="IncomingAddToCaseUtilityTemplates.xslt"/>

<xsl:output method="xml" indent="yes"/>

  <!--
    Template to write a standalone addToCase page (i.e. outside the wizard) for an entity.

    @param entityElement XML Element containing the entity
    @param caseType The case type being generated
    @param baseDirectory Base Directory to generate to
    @param fileName The name of the file to generate (minus the file extension)
    @param includeVIMFileName The name of the VIM file to include
    @param wizardIncomingAddToCaseUIMName The name of the page for the Next button to link to
  -->
  <xsl:template name="WizardIncomingAddToCaseSelectUIM">

    <xsl:param name="entityElement"/>
    <xsl:param name="caseType"/>
    <xsl:param name="baseDirectory"/>
    <xsl:param name="fileName"/>
    <xsl:param name="includeVIMFileName"/>
    <xsl:param name="wizardIncomingAddToCaseUIMName"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:param name="wizardName"/>

    <!-- Name of the entity -->
    <xsl:variable name="entityName" select="$entityElement/@name"/>

    <!-- Full name of the file -->
    <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/><xsl:value-of select="$fileName"/></xsl:variable>

    <redirect:write select="concat($fullFileName, '.uim')">

      <!-- add copyright notice -->
      <xsl:call-template name="printXMLCopyright">
        <xsl:with-param name="date" select="$date"/>
      </xsl:call-template>

      <PAGE
        PAGE_ID="{$fileName}"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
        WINDOW_OPTIONS="width=900"
        >
        <PAGE_TITLE>
          <CONNECT>
            <SOURCE
              NAME="TEXT"
              PROPERTY="Page.Title"
            />
          </CONNECT>
        </PAGE_TITLE>

        <MENU MODE="WIZARD_PROGRESS_BAR">
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="{$wizardName}.wizardMenu"
            />
          </CONNECT>
        </MENU>

        <INCLUDE FILE_NAME="{$includeVIMFileName}.vim"/>

        <ACTION_SET
          ALIGNMENT="RIGHT"
          >

          <ACTION_CONTROL
            IMAGE="CancelButton"
            LABEL="ActionControl.Label.Cancel"
            ALIGNMENT="LEFT"
          />

          <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">

            <xsl:variable name="wizardSelectEmploymentPage"><xsl:call-template name="Utilities-getIncomingAddToCasePageName-WizardSelectEmployment-SelectPage"><xsl:with-param name="entityElement" select="$entityElement"/>
              <xsl:with-param name="caseType" select="$caseType"/><xsl:with-param name="relatedEntityName" select="$relatedEntityName"/></xsl:call-template></xsl:variable>

            <ACTION_CONTROL
              IMAGE="BackButton"
              LABEL="ActionControl.Label.Back"
              >
              <LINK PAGE_ID="{$wizardSelectEmploymentPage}" DISMISS_MODAL="FALSE" SAVE_LINK="FALSE">
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="caseID"/>
                  <TARGET NAME="PAGE" PROPERTY="caseID"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
                  <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
                  <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
                </CONNECT>
                <xsl:if test="$relatedEntityName!=''">
                  <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceIDParamName">
                    <xsl:with-param name="entityElement" select="$entityElement"/>
                    <xsl:with-param name="fromRelated" select="$relatedEntityName"/>
                  </xsl:call-template></xsl:variable>
                  <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceTypeParamName">
                    <xsl:with-param name="entityElement" select="$entityElement"/>
                    <xsl:with-param name="fromRelated" select="$relatedEntityName"/>
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
              </LINK>
            </ACTION_CONTROL>
          </xsl:if>

          <ACTION_CONTROL
            IMAGE="NextButton"
            LABEL="ActionControl.Label.Next"
            TYPE="SUBMIT"
          >
            <LINK PAGE_ID="{$wizardIncomingAddToCaseUIMName}" DISMISS_MODAL="FALSE" SAVE_LINK="FALSE">
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="caseID"/>
                <TARGET NAME="PAGE" PROPERTY="caseID"/>
              </CONNECT>
              <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
                  <TARGET NAME="PAGE" PROPERTY="evidenceType"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
                  <TARGET NAME="PAGE" PROPERTY="evidenceID"/>
                </CONNECT>
              <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">
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
              <!-- If there was a pre seleted related id passed in, then connect this to addToCase page -->
              <xsl:if test="$relatedEntityName!=''">
                <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceIDParamName">
                  <xsl:with-param name="entityElement" select="$entityElement"/>
                  <xsl:with-param name="fromRelated" select="$relatedEntityName"/>
                </xsl:call-template></xsl:variable>
                <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceTypeParamName">
                  <xsl:with-param name="entityElement" select="$entityElement"/>
                  <xsl:with-param name="fromRelated" select="$relatedEntityName"/>
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
              <!-- If there are parents and the relatedEntityName is not a parent then connect from radio button select interface  -->
              <xsl:if test="count($entityElement/Relationships/Parent) &gt; 0 and count($entityElement/Relationships/Parent[@name=$relatedEntityName])=0">
              <CONNECT>
                <SOURCE NAME="PARENTPARAMS" PROPERTY="evidenceKey$evidenceID"/>
                <TARGET NAME="PAGE" PROPERTY="parEvID"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="PARENTPARAMS" PROPERTY="evidenceKey$evType"/>
                <TARGET NAME="PAGE" PROPERTY="parEvType"/>
              </CONNECT>
              </xsl:if>
              <!-- If there are pre associations and the relatedEntityName is not a pre association then connect from radio button select interface  -->
              <xsl:if test="$entityElement/Relationships/@preAssociation='Yes' and count($entityElement/Relationships/PreAssociation[@to=$relatedEntityName])=0">
                <CONNECT>
                  <SOURCE NAME="PREASSOCPARAMS" PROPERTY="evidenceKey$evidenceID"/>
                  <TARGET NAME="PAGE" PROPERTY="preAssocID"/>
                </CONNECT>
                <CONNECT>
                  <SOURCE NAME="PREASSOCPARAMS" PROPERTY="evidenceKey$evType"/>
                  <TARGET NAME="PAGE" PROPERTY="preAssocType"/>
                </CONNECT>
              </xsl:if>
              <!-- For each mandatory parent thats not the related id passed in, connect the radio buttons interface -->
              <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
                <xsl:if test="@name!=$relatedEntityName">
                  <CONNECT>
                    <SOURCE NAME="{@name}PARAMS" PROPERTY="evidenceKey$evidenceID"/>
                    <TARGET NAME="PAGE" PROPERTY="{@name}ParEvID"/>
                  </CONNECT>
                  <CONNECT>
                    <SOURCE NAME="{@name}PARAMS" PROPERTY="evidenceKey$evType"/>
                    <TARGET NAME="PAGE" PROPERTY="{@name}ParEvType"/>
                  </CONNECT>
                </xsl:if>
              </xsl:for-each>
            </LINK>
          </ACTION_CONTROL>

        </ACTION_SET>

      </PAGE>

    </redirect:write>

    <xsl:call-template name="write-all-locales-wizardselectalluim-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
    </xsl:call-template>

  </xsl:template>

  <xsl:template name="write-all-locales-wizardselectalluim-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-wizardselectalluim-properties">
            <xsl:with-param name="locale"
              select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName"
              select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-wizardselectalluim-properties">
            <xsl:with-param name="locales"
              select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName"
              select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-wizardselectalluim-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName"
              select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-wizardselectalluim-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement"
              select="$entityElement"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-wizardselectalluim-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

      <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

      <redirect:write select="concat($fullFileName, $locale, '.properties')">
        <xsl:if test="count($generalProperties/Help.PageDescription.IncomingAddToCaseEntity)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.IncomingAddToCaseEntity"/>
            <xsl:with-param name="evidenceNode" select="$entityElement"/>
	        <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
          </xsl:call-template>
        </xsl:if>
        <xsl:if test="count($generalProperties/Page.Title.NewEvidenceWizard)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.NewEvidenceWizard"/>
            <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
	        <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
          </xsl:call-template>
          <xsl:text>&#xa;</xsl:text>
        </xsl:if>

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Next"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>

        <xsl:if test="count($entityElement/Relationships/Related[@to='Employment']) &gt; 0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Back"/>
            <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
          </xsl:call-template>
        </xsl:if>

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Cancel"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>


      </redirect:write>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
