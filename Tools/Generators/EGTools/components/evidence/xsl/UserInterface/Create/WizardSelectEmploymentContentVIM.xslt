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
<xsl:stylesheet extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="../UICommon.xslt"/>

  <xsl:output method="xml" indent="yes"/>


  <!--
    Template to write the content vim and associated properties files for the entities wizard select employment page

    @param entityElement XML Element for the entity
    @param baseDirectory Base Directory to write the files into
    @param fileName Name of the file to create (with no file extension)
    @param relatedEntityName The name of the entity that was pre selected (if any)
  -->
  <xsl:template name="WizardSelectEmploymentContentVIM">

    <xsl:param name="entityElement"/>
    <xsl:param name="baseDirectory"/>
    <xsl:param name="fileName"/>
    <xsl:param name="relatedEntityName"/>
    <xsl:param name="uimPageName"/>

    <!-- Name of the entity -->
    <xsl:variable name="entityName"><xsl:value-of select="$entityElement/@name"/></xsl:variable>

    <!-- Full name and directory of the file to create, minus the file extension -->
    <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/><xsl:value-of select="$fileName"/></xsl:variable>

    <!-- Write the main VIM file -->
    <redirect:write select="concat($fullFileName, '.vim')">

      <!-- add copyright notice -->
      <xsl:call-template name="printXMLCopyright">
        <xsl:with-param name="date" select="$date"/>
      </xsl:call-template>

      <VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

        <!-- Page Parameters -->
        <PAGE_PARAMETER NAME="caseID"/>
        <PAGE_PARAMETER NAME="evidenceID"/>
        <PAGE_PARAMETER NAME="evidenceType"/>
        <xsl:if test="$relatedEntityName!=''">
          <xsl:if test="count($entityElement/Relationships/Parent[@name=$relatedEntityName]) = 1">
            <!-- Parent is already selected so add in correct parameters -->
            <PAGE_PARAMETER NAME="parEvID"/>
            <PAGE_PARAMETER NAME="parEvType"/>
          </xsl:if>
          <xsl:if
            test="count($entityElement/Relationships/PreAssociation[@to=$relatedEntityName]) = 1">
            <!-- Pre Association is already selected so add in correct parameters -->
            <PAGE_PARAMETER NAME="preAssocID"/>
            <PAGE_PARAMETER NAME="preAssocType"/>
          </xsl:if>
          <xsl:if
            test="count($entityElement/Relationships/MandatoryParents/Parent[@name=$relatedEntityName]) = 1">
            <!-- Mandatory Parent is already selected so add in correct parameters -->
            <PAGE_PARAMETER NAME="{$relatedEntityName}ParEvID"/>
            <PAGE_PARAMETER NAME="{$relatedEntityName}ParEvType"/>
          </xsl:if>
        </xsl:if>

        <CONNECT>
          <SOURCE NAME="CONSTANT"
            PROPERTY="CaseParticipantRoleType.{$entityName}.ListForRelatedEvidence"/>
          <TARGET NAME="DISPLAYEMPLIST" PROPERTY="key$caseParticipantTypeList"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE"
            PROPERTY="caseID"/>
          <TARGET NAME="DISPLAYEMPLIST" PROPERTY="key$caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="ACTION" PROPERTY="key$caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="key$caseParticipantTypeList"/>
          <TARGET NAME="ACTION" PROPERTY="key$caseParticipantRoleTypeList"/>
        </CONNECT>

        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="DISPLAYEMPLIST"
          OPERATION="listEmploymentsForCaseParticipantTypeList"
        />
        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="ACTION"
          OPERATION="readRelatedCoreEmployment" PHASE="ACTION"
        />

        <CLUSTER>
        <CLUSTER
          SHOW_LABELS="false"
          STYLE="outer-cluster-borderless"
        >
          <FIELD>
            <CONNECT>
              <SOURCE
                NAME="TEXT"
                PROPERTY="Wizard.SelectEmployment.Description"
              />
            </CONNECT>
          </FIELD>
        </CLUSTER>
        <LIST>
          <CONTAINER WIDTH="4">
            <WIDGET TYPE="SINGLESELECT">
              <WIDGET_PARAMETER NAME="SELECT_SOURCE">
                <CONNECT>
                  <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="employmentID"/>
                </CONNECT>
              </WIDGET_PARAMETER>
              <WIDGET_PARAMETER NAME="SELECT_TARGET">
                <CONNECT>
                  <TARGET NAME="ACTION" PROPERTY="key$employmentID"/>
                </CONNECT>
              </WIDGET_PARAMETER>
            </WIDGET>
          </CONTAINER>

          <FIELD LABEL="List.Title.Participant" WIDTH="18">
            <CONNECT>
              <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="caseParticipantFullName"/>
            </CONNECT>
          </FIELD>

          <FIELD LABEL="Field.Label.Primary" WIDTH="12">
            <CONNECT>
              <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="primaryCurrentInd"/>
            </CONNECT>
          </FIELD>

          <FIELD LABEL="Field.Label.Employer" WIDTH="18">
            <CONNECT>
              <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="employerName"/>
            </CONNECT>
          </FIELD>

          <FIELD LABEL="Field.Label.Occupation" WIDTH="16">
            <CONNECT>
              <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="occupationType"/>
            </CONNECT>
          </FIELD>

          <FIELD LABEL="Field.Label.From" WIDTH="16">
            <CONNECT>
              <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="fromDate"/>
            </CONNECT>
          </FIELD>

          <FIELD LABEL="Field.Label.To" WIDTH="16">
            <CONNECT>
              <SOURCE NAME="DISPLAYEMPLIST" PROPERTY="toDate"/>
            </CONNECT>
          </FIELD>
        </LIST>
        </CLUSTER>
      </VIEW>

    </redirect:write>

    <xsl:call-template name="write-all-locales-selectemploymentcontent-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
    </xsl:call-template>

  </xsl:template>

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-selectemploymentcontent-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-selectemploymentcontent-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-selectemploymentcontent-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-selectemploymentcontent-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-selectemploymentcontent-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-selectemploymentcontent-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <xsl:variable name="generalProperties"
    select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

    <xsl:variable name="employmentProperties"
      select="//EvidenceEntities/Properties[@locale=$locale]/Employment"/>

    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0 or count(//EvidenceEntities/Properties[@locale=$locale]/Employment)&gt;0">
      <redirect:write select="concat($fullFileName, $locale, '.properties')">

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/Wizard.SelectEmployment.Description"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Search"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>

        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.Employment.CaseParticipant"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
        <xsl:text>&#xa;</xsl:text>
        <xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Participant"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
        <xsl:text>&#xa;</xsl:text>
        <xsl:if test="count($employmentProperties/Field.Label.Primary)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$employmentProperties/Field.Label.Primary"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template>
        </xsl:if>

        <xsl:if test="count($employmentProperties/Field.Label.Employer)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$employmentProperties/Field.Label.Employer"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template>
        </xsl:if>

        <xsl:if test="count($employmentProperties/Field.Label.Occupation)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$employmentProperties/Field.Label.Occupation"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template>
        </xsl:if>

        <xsl:if test="count($employmentProperties/Field.Label.From)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$employmentProperties/Field.Label.From"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template>
        </xsl:if>

        <xsl:if test="count($employmentProperties/Field.Label.To)&gt;0">
          <xsl:call-template name="callGenerateProperties">
            <xsl:with-param name="propertyNode" select="$employmentProperties/Field.Label.To"/>
            <xsl:with-param name="evidenceNode" select="."/>
          </xsl:call-template>
        </xsl:if>
      </redirect:write>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
