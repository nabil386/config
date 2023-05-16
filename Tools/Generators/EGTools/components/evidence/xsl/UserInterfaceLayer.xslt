<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006, 2017. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008,2010-2011 Curam Software Ltd.  All rights reserved.

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

  <xsl:output method="xml" indent="yes"/>

  <!-- Include Section -->

  <!-- Global Variables -->
  <xsl:include href="UserInterface/UICommon.xslt" />
  <xsl:include href="EvidenceCommon.xslt" />

  <!-- View User Interface Templates -->
  <xsl:include href="UserInterface/ViewUI.xslt" />
  <xsl:include href="UserInterface/ViewSnapshot.xslt" />
  <xsl:include href="UserInterface/ViewContentUI.xslt" />

  <!-- Modify User Interface Templates -->
  <xsl:include href="UserInterface/ModifyUI.xslt" />
  <xsl:include href="UserInterface/ModifyContentUI.xslt" />

  <!-- Process Related Evidence -->
  <xsl:include href="UserInterface/ProcessRelatedEvidence.xslt" />

  <!-- Associated Evidence User Interface Templates -->
  <xsl:include href="UserInterface/ListEvidenceForAssociationUI.xslt"/>
  <xsl:include href="UserInterface/ListEvidenceForAssociation2UI.xslt"/>
  <xsl:include href="UserInterface/ViewUIFromAssociationList.xslt"/>
  <xsl:include href="UserInterface/ResolveAssociatedEvidence.xslt"/>
  <xsl:include href="UserInterface/ViewAssociatedEvidenceUI.xslt"/>
  <xsl:include href="UserInterface/ConfirmCreateAssociationUI.xslt"/>
  <xsl:include href="UserInterface/ConfirmCancelAssociationUI.xslt"/>

  <!-- Related Case Participant User Interface Templates -->
  <xsl:include href="UserInterface/ResolveRelatedCaseParticipant.xslt" />
<!-- DKenny Deprecated -->
<!--  <xsl:include href="UserInterface/CreateRelatedCaseParticipantUI.xslt" /> -->

  <!-- Multiple Parent Evidence Page Templates -->
  <xsl:include href="UserInterface/ResolveParentEvidence.xslt" />
  <xsl:include href="UserInterface/ResolveListEvidence.xslt" />

  <!-- Multiple Associated Children Evidence Page Template -->
  <xsl:include href="UserInterface/ResolveChildEvidence.xslt" />

  <!-- List Related Evidence User Interface Template -->
  <xsl:include href="UserInterface/ListRelatedEvidenceUI.xslt" />

  <!-- DKenny Deprecated -->
  <!-- List Participant Relationship Evidence User Interface Template -->
  <!--<xsl:include href="UserInterface/ListParticipantRelationshipEvidenceUI.xslt" />-->

  <!-- Evidence Hierarchy User Interface Template -->
  <xsl:include href="UserInterface/HierarchyUI.xslt" />

  <!-- List Core Employment User Interface Template -->
  <xsl:include href="UserInterface/ListCoreEmployments.xslt" />
  <!-- Resolve Employment User Interface Template -->
  <xsl:include href="UserInterface/ResolveEmployment.xslt" />

  <!-- Create Search Popup User Interface Template -->
  <!--
  <xsl:include href="UserInterface/CreateSearchPopupUI.xslt" />
  -->

  <!-- Create Employment User Interface Template -->
  <xsl:include href="UserInterface/CreateEmploymentUI.xslt" />

  <!-- Resolve View Evidence For Product Template -->
  <xsl:include href="UserInterface/ResolveViewEvidenceForProduct.xslt" />

  <!-- Resolve Modify Evidence For Product Template -->
  <xsl:include href="UserInterface/ResolveModifyEvidenceForProduct.xslt" />

  <!-- Resolve Modify Evidence For Product Template -->
  <xsl:include href="UserInterface/ResolveViewWorkSeparationEvidence.xslt" />

  <!-- Resolve Evidence Workspace -->
  <xsl:include href="UserInterface/ResolveEvidenceWorkspace.xslt" />

  <!-- Resolve Evidence Object -->
  <xsl:include href="UserInterface/ResolveEvidenceObject.xslt" />
  <xsl:include href="UserInterface/ResolveEvidenceObjectIssues.xslt" />
  <xsl:include href="UserInterface/ResolveEvidenceObjectVerifications.xslt" />

  <!-- Resolve Create Evidence -->
  <xsl:include href="UserInterface/ResolveEvidenceCreate.xslt" />

  <!-- Resolve History Record Evidence -->
  <xsl:include href="UserInterface/ResolveEvidenceHistoryRecord.xslt" />

  <xsl:include href="UserInterface/ViewAssociatedListFrom.xslt"/>
  <xsl:include href="UserInterface/ViewAssociatedFrom.xslt"/>

  <!-- BEGIN, CR00101875, POB -->
  <xsl:include href="UserInterface/ResolveCreateEvidenceFromParentCreate.xslt"/>
  <!-- END, CR00101875 -->

  <!-- BEGIN, CR00114649, POB -->
  <xsl:include href="UserInterface/PreAssociation/PreAssociations.xslt"/>
  <!-- END, CR00114649 -->

  <!-- BEGIN, CR00118883, POB -->
  <xsl:include href="UserInterface/RelationshipsRelated/ProcessRelatedRelationships.xslt"/>
  <!-- END, CR00118883 -->

  <!-- BEGIN, CR00222728, POB -->
  <xsl:include href="UserInterface/ViewCorrectionHistory.xslt"/>
  <!-- END, CR00222728 -->

  <xsl:include href="UserInterface/ListTypeWorkspaceUI.xslt"/>

  <!-- Business Object page generation -->
  <xsl:include href="UserInterface/BusinessObject/ChangeHistoryUI.xslt"/>
  <xsl:include href="UserInterface/BusinessObject/IssuesUI.xslt"/>
  <xsl:include href="UserInterface/BusinessObject/VerificationsUI.xslt"/>
  <xsl:include href="UserInterface/BusinessObject/ListObjectUI.xslt"/>
  <xsl:include href="UserInterface/BusinessObject/ViewObjectSummaryUI.xslt"/>

  <xsl:include href="UserInterface/TypeWorkspace/WorkspaceVerificationsUI.xslt"/>
  <xsl:include href="UserInterface/TypeWorkspace/WorkspaceIssuesUI.xslt"/>

  <!-- Create pages -->
  <xsl:include href="UserInterface/Create/CreatePages.xslt"/>

  <!-- Incoming Add To Case -->
  <xsl:include href="UserInterface/Incoming/IncomingAddToCasePages.xslt"/>

  <!-- Incoming Modify -->
  <xsl:include href="UserInterface/Incoming/IncomingModifyUI.xslt"/>
  <xsl:include href="UserInterface/Incoming/IncomingModifyContentUI.xslt"/>

  <!-- The type of case evidence i.e Integrated or Product -->
  <xsl:param name="caseType" />
  <xsl:param name="serverBuildDir" />
  <xsl:param name="date"/>
  <xsl:param name="createEmploymentLink"/>
  <xsl:param name="createClientListForEmployment"/>

  <!-- BEGIN, PADDY -->
  <xsl:param name="localeList" />
  <!-- END, PADDY -->

  <!-- Main Evidence User Interface Generation Template -->
  <xsl:template match="EvidenceEntity[UserInterfaceLayer]">

    <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="ucName"><xsl:value-of select="translate($capName, $lcletters, $ucletters)"/></xsl:variable>
    <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>
    <xsl:variable name="capNameCaseType"><xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/></xsl:variable>
    <!-- BEGIN, CR00100405, CD -->
    <xsl:variable name="resolveCreateWizardUIName"><xsl:value-of select="$prefix"/>_resolveCreate<xsl:value-of select="$capNameCaseType"/></xsl:variable>

    <xsl:variable name="changeHistoryUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="issuesUIName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="verificationsUIName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="viewObjectSummaryUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="listTypeWorkspaceName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capNameCaseType"/></xsl:variable>

    <xsl:variable name="wsIssuesUIName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capNameCaseType"/>WSIssues</xsl:variable>
    <xsl:variable name="wsVerificationsUIName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capNameCaseType"/>WSVerifications</xsl:variable>

    <xsl:variable name="createContentUIName"><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capNameCaseType"/>_content</xsl:variable>

    <!-- BEGIN, PADDY, CR00094077 -->
    <xsl:variable name="coCreateChildContentUIName">
      <xsl:value-of select="$prefix"/>_coCreate<xsl:value-of select="$capNameCaseType"/>_ChildContent</xsl:variable>

    <xsl:variable name="coCreateChildAggregationName">dtls$<xsl:value-of select="translate(substring($capName, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring($capName, 2)"/>$</xsl:variable>
    <!-- END, PADDY, CR00094077 -->

    <xsl:variable name="listUIName"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="modifyUIName"><xsl:value-of select="$prefix"/>_modify<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="modifyContentUIName"><xsl:value-of select="$prefix"/>_modify<xsl:value-of select="$capNameCaseType"/>_content</xsl:variable>

    <xsl:variable name="incomingModifyUIName"><xsl:value-of select="$prefix"/>_incomingModify<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="incomingModifyContentUIName"><xsl:value-of select="$prefix"/>_incomingModify<xsl:value-of select="$capNameCaseType"/>_content</xsl:variable>

    <xsl:variable name="validateUIName"><xsl:value-of select="$prefix"/>_validate<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="viewSnapshotUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>Snapshot</xsl:variable>

    <xsl:variable name="viewUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="viewContentUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>_content</xsl:variable>
    <xsl:variable name="viewObjectUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/></xsl:variable>
    <xsl:variable name="viewObjectContentUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>Object_content</xsl:variable>
    <xsl:variable name="viewSnapshotContentUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>Snapshot_content</xsl:variable>
    <!-- BEGIN, CR00222728, POB -->
    <xsl:variable name="viewCorrectionHistoryName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/>CH</xsl:variable>
    <!-- END, CR00222728 -->

    <!-- END, CR00100405 -->
    <xsl:variable name="rootPath"/>
    <xsl:variable name="path" select="concat($rootPath, $capName , '/')"/>


  <!-- Open Log File for Testing -->
  <redirect:open select="$logfilename" method="text" append="true" />
  <redirect:write select="$logfilename">

  <!-- NOTE:                                                                -->
  <!-- Put XSLTs in here that should only be generated once per product     -->
  <!-- E.G. resolve scripts, overriding core pages, etc.                    -->
  <!-- CDuffy - Removing this as these resolve scripts need to be generated for both case types now.
  <xsl:if test="position()=2.0 and $caseType='Evidence'">
  </xsl:if>
  -->

  <!-- Create Resolve Evidence Workspace Page -->
  <xsl:call-template name="ResolveEvidenceWorkspace"/>

  <!-- Create Resolve Evidence Object Page -->
  <xsl:call-template name="ResolveEvidenceObject"/>
  <xsl:call-template name="ResolveEvidenceObjectIssues"/>
  <xsl:call-template name="ResolveEvidenceObjectVerifications"/>

  <xsl:call-template name="ResolveEvidenceHistoryRecord"/>

  <!-- Create Resolve Evidence Create Page -->
  <xsl:call-template name="ResolveEvidenceCreate"/>

  <!-- BEGIN, CR00100405, CD -->
  <!-- Removing override loop -->
  <!-- END, CR00100405 -->

  <!-- Create Resolve List Evidence Page -->
  <xsl:call-template name="ResolveListEvidence">

    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
    <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capNameCaseType, 'List')"/>
    <xsl:with-param name="Relationships" select="Relationships"/>
    <xsl:with-param name="capName" select="$capName" />

  </xsl:call-template>
    <!-- BEGIN, CR00100657, POB -->
    <xsl:for-each select="UserInterfaceLayer/ViewAssociated">
      <xsl:call-template name="ViewAssociatedListFrom">
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="entityFrom" select="$capName"/>
        <xsl:with-param name="associatedEntity" select="@name"/>
      </xsl:call-template>

      <xsl:call-template name="ViewAssociatedFrom">
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="entityFrom" select="$capName"/>
        <xsl:with-param name="associatedEntity" select="@name"/>
      </xsl:call-template>
    </xsl:for-each>
    <!-- END, CR00100657, POB -->

    <!-- Generate list pages for related evidence business object tabs -->
      <xsl:for-each select="Relationships/Parent">

        <xsl:call-template name="ListObjectUI">
          <xsl:with-param name="path" select="$path" />
          <xsl:with-param name="EvidenceEntity" select="../../." />
          <xsl:with-param name="UIName" select="concat($listUIName, 'For', @name, 'Obj')"/>
          <xsl:with-param name="parentName" select="@name"/>
        </xsl:call-template>

      </xsl:for-each>

      <xsl:for-each select="Relationships/MandatoryParents/Parent">

        <xsl:call-template name="ListObjectUI">
          <xsl:with-param name="path" select="$path" />
          <xsl:with-param name="EvidenceEntity" select="../../../." />
          <xsl:with-param name="UIName" select="concat($listUIName, 'For', @name, 'Obj')"/>
          <xsl:with-param name="parentName" select="@name"/>
        </xsl:call-template>

      </xsl:for-each>

      <xsl:for-each select="Relationships/Association[@from!='' and @displayInHierarchy='Yes']">

        <xsl:call-template name="ListObjectUI">
          <xsl:with-param name="path" select="$path" />
          <xsl:with-param name="EvidenceEntity" select="../../." />
          <xsl:with-param name="UIName" select="concat($listUIName, 'For', @from, 'Obj')"/>

        </xsl:call-template>

      </xsl:for-each>



  <xsl:variable name="childUIInd">
    <xsl:choose>
      <xsl:when test="count(Relationships/Parent)>0 or (Relationships/@association='Yes' and count(Relationships/Association[@from!=''])>0)">Yes</xsl:when>
      <xsl:otherwise>No</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="EvidenceEntityElem" select="."/>

      <!-- Change History User Interface -->
      <xsl:call-template name="ChangeHistoryUI">
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="UIName" select="concat($changeHistoryUIName,'ObjCH')" />
        <xsl:with-param name="capName" select="$capName" />
      </xsl:call-template>


      <!-- Issues User Interface -->
      <xsl:call-template name="IssuesUI">
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="UIName" select="concat($issuesUIName,'ObjIssues')" />
        <xsl:with-param name="capName" select="$capName" />
      </xsl:call-template>

      <xsl:call-template name="WorkspaceIssuesUI">
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="caseType" select="$caseType" />
        <xsl:with-param name="UIName" select="$wsIssuesUIName" />
        <xsl:with-param name="capName" select="$capName" />

      </xsl:call-template>

      <!-- Verifications User Interface -->
      <xsl:call-template name="VerificationsUI">
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="UIName" select="concat($verificationsUIName,'ObjVerifications')" />
        <xsl:with-param name="capName" select="$capName" />
      </xsl:call-template>

      <xsl:call-template name="WorkspaceVerificationsUI">
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="caseType" select="$caseType" />
        <xsl:with-param name="UIName" select="$wsVerificationsUIName" />
        <xsl:with-param name="capName" select="$capName" />

      </xsl:call-template>

      <!-- Verifications User Interface -->
      <xsl:call-template name="ViewObjectSummaryUI">
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="UIName" select="concat($viewObjectSummaryUIName,'ObjSummary')" />
        <xsl:with-param name="capName" select="$capName" />
      </xsl:call-template>


      <!-- Create List Type Workspace User Interface -->
      <xsl:call-template name="ListTypeWorkspaceUI">
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="caseType" select="$caseType"/>
        <xsl:with-param name="EvidenceEntity" select="$capName" />
        <xsl:with-param name="UIName" select="$listTypeWorkspaceName" />
      </xsl:call-template>

    <!-- BEGIN, CR00225302, POB -->
    <!-- Write the v6.0 create pages -->
    <xsl:call-template name="WriteCreatePagesForEntity">
      <xsl:with-param name="entityElement" select="$EvidenceEntityElem"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>
    <!-- END, CR00225302 -->

    <!-- Write the v7.2 Incoming evidence add to case pages -->
    <xsl:call-template name="WriteIncomingAddToCasePagesForEntity">
      <xsl:with-param name="entityElement" select="$EvidenceEntityElem"/>
      <xsl:with-param name="caseType" select="$caseType"/>
    </xsl:call-template>

    <!-- Create Incoming Modify User Interface -->
  <xsl:call-template name="IncomingModifyUI">
    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$incomingModifyUIName" />
  </xsl:call-template>

  <!-- Create Incoming Modify Content User Interface -->
  <xsl:call-template name="IncomingModifyContentUI">
    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$incomingModifyContentUIName" />
    <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>
  </xsl:call-template>

    <xsl:for-each select="Relationships/Child">

     <!-- create the parent list page that is used by the tabs for the child evidence business object -->
     <xsl:call-template name="ListObjectUI">

       <xsl:with-param name="path" select="$path" />
       <xsl:with-param name="EvidenceEntity" select="../../." />
       <xsl:with-param name="UIName" select="concat($prefix, '_list',$capNameCaseType, 'For', @name, 'Obj')"/>

     </xsl:call-template>


    </xsl:for-each>

    <!-- BEGIN, CR00266438, CD -->
    <!-- this gets hit when hitting the preassociation "child" metadata
         i.e. where preassociation relationship node lives on this entity -->
    <xsl:for-each select="Relationships/PreAssociation[@to!='']">

     <xsl:variable name="assocEntityName" select="@to" />
     <xsl:variable name="assocEntity" select="/EvidenceEntities/EvidenceEntity[@name=$assocEntityName]" />

     <!-- create the parent list page that is used by the tabs for the child evidence business object -->
     <xsl:call-template name="ListObjectUI">

       <xsl:with-param name="path" select="$path" />
       <xsl:with-param name="EvidenceEntity" select="$EvidenceEntityElem" />
       <xsl:with-param name="UIName" select="concat($prefix, '_list',$capNameCaseType, 'For', @to, 'Obj')"/>

     </xsl:call-template>

    </xsl:for-each>
    <!-- END, CR00266438, CD -->

    <!-- this gets hit when hitting the preassociation "parent" metadata
         i.e. where another entity has a preassociation to this one -->
    <xsl:for-each select="/EvidenceEntities/EvidenceEntity/Relationships/PreAssociation[@to=$capName]">

     <!-- create the parent list page that is used by the tabs for the child evidence business object -->
     <xsl:variable name="assocEntity" select="../../."/>

     <xsl:call-template name="ListObjectUI">

       <xsl:with-param name="path" select="$path" />
       <xsl:with-param name="EvidenceEntity" select="$EvidenceEntityElem" />
       <xsl:with-param name="UIName" select="concat($prefix, '_list',$capNameCaseType, 'For', $assocEntity/@name, 'Obj')"/>

     </xsl:call-template>


    </xsl:for-each>

    <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">

      <xsl:variable name="resolveCreateFromParentName"><xsl:value-of select="$prefix"/>_resolveCreate<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>FromParentCreate</xsl:variable>

      <xsl:call-template name="ResolveCreateEvidenceFromParentCreate">
        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="$path" />
        <xsl:with-param name="UIName" select="$resolveCreateFromParentName"/>
        <xsl:with-param name="capName" select="$capName" />
        <xsl:with-param name="Relationships" select="./Relationships" />
      </xsl:call-template>
    </xsl:if>

  <!-- BEGIN, CR00219910, CD -->
  <!-- Create View User Interface -->
  <xsl:call-template name="ViewUI">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$viewUIName" />
    <xsl:with-param name="viewVersion">default</xsl:with-param>

  </xsl:call-template>

    <xsl:call-template name="ViewCorrectionHistory">

      <xsl:with-param name="path" select="$path" />
      <xsl:with-param name="uimPageName" select="$viewCorrectionHistoryName" />
      <xsl:with-param name="entityViewPageName" select="$viewUIName"/>

    </xsl:call-template>

  <!-- Create View Business Object uim -->
  <xsl:call-template name="ViewUI">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="concat($viewObjectUIName,'Object')" />
    <xsl:with-param name="viewVersion" select="$businessObjectViewVersion" />

  </xsl:call-template>

  <!-- Create View History Record uim -->
    <xsl:call-template name="ViewUI">

      <xsl:with-param name="path" select="$path" />
      <xsl:with-param name="UIName" select="concat($viewUIName,$historyRecordViewVersion)" />
      <xsl:with-param name="viewVersion" select="$historyRecordViewVersion" />

  </xsl:call-template>



  <xsl:call-template name="ViewSnapshot">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$viewSnapshotUIName" />
    <xsl:with-param name="VIMName" select="$viewSnapshotContentUIName" />

  </xsl:call-template>

  <!-- Create View Content User Interface -->
  <xsl:call-template name="ViewContentUI">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$viewContentUIName" />
    <xsl:with-param name="viewVersion">default</xsl:with-param>

  </xsl:call-template>

  <!-- Create View Snapshot vim -->
  <xsl:call-template name="ViewContentUI">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$viewSnapshotContentUIName" />
    <xsl:with-param name="viewVersion" select="$snapshotViewVersion" />

  </xsl:call-template>

  <!-- Create View Business Object vim -->
  <xsl:call-template name="ViewContentUI">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$viewObjectContentUIName" />
    <xsl:with-param name="viewVersion" select="$businessObjectViewVersion" />

  </xsl:call-template>
  <!-- END, CR00219910 -->


  <!-- Create Modify User Interface -->
  <xsl:call-template name="ModifyUI">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$modifyUIName" />

  </xsl:call-template>


  <!-- Create Modify Content User Interface -->
  <xsl:call-template name="ModifyContentUI">

    <xsl:with-param name="path" select="$path" />
    <xsl:with-param name="UIName" select="$modifyContentUIName" />
    <xsl:with-param name="baseAggregation">dtls$</xsl:with-param>

  </xsl:call-template>


  <!-- Process Relationships -->
  <!--<xsl:call-template name="ProcessRelatedEvidence">

    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="rootPath" select="$rootPath" />
    <xsl:with-param name="capName" select="$capName" />
    <xsl:with-param name="parents"/>
    <xsl:with-param name="firstParentName" select="$capName"/>

  </xsl:call-template>-->

  <!-- List Core Employments -->
  <xsl:call-template name="ListCoreEmployments">

    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="$rootPath"/>
    <xsl:with-param name="UIName" select="concat($prefix, '_listCoreEmployment', $caseType,'Details')"/>
    <xsl:with-param name="relatedEvidenceName" select="@name"/>
    <xsl:with-param name="employmentResolveScript" select="concat($prefix,'_resolveEmployment', $caseType)"/>
    <xsl:with-param name="cancelLink">Evidence_resolveWorkspace</xsl:with-param>

  </xsl:call-template>

  <!-- List Core Employments from Active HLW -->
  <xsl:call-template name="ListCoreEmployments">

    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="$rootPath"/>
    <xsl:with-param name="UIName" select="concat($prefix, '_listCoreEmployment', $caseType,'Details_fromActive')"/>
    <xsl:with-param name="relatedEvidenceName" select="@name"/>
    <xsl:with-param name="employmentResolveScript" select="concat($prefix,'_resolveEmployment', $caseType)"/>
    <xsl:with-param name="cancelLink">Evidence_resolveWorkspaceActive</xsl:with-param>

  </xsl:call-template>

  <!-- List Core Employments from In Edit HLW -->
  <xsl:call-template name="ListCoreEmployments">

    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="$rootPath"/>
    <xsl:with-param name="UIName" select="concat($prefix, '_listCoreEmployment', $caseType,'Details_fromInEdit')"/>
    <xsl:with-param name="relatedEvidenceName" select="@name"/>
    <xsl:with-param name="employmentResolveScript" select="concat($prefix,'_resolveEmployment', $caseType)"/>
    <xsl:with-param name="cancelLink">Evidence_resolveWorkspaceInEdit</xsl:with-param>

  </xsl:call-template>

  <xsl:call-template name="ResolveEmployment">

    <xsl:with-param name="prefix" select="$prefix" />
    <xsl:with-param name="path" select="$rootPath" />
    <xsl:with-param name="UIName" select="concat($prefix, '_resolveEmployment', $caseType)"/>

  </xsl:call-template>

  <!-- Process Associations Type Relationships -->
  <xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@from!=''])>0">

    <!-- Create List Evidence For Association UI -->
    <xsl:call-template name="ListEvidenceForAssociationUI">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_list', $capNameCaseType, 'ForAssociation')"/>
      <xsl:with-param name="capName" select="$capName" />

      <xsl:with-param name="parents"/>
      <xsl:with-param name="firstParentName" select="$capName" />

      <xsl:with-param name="fromCreateParentInd" select="'No'"/>

    </xsl:call-template>

    <!-- Create View Evidence UI From Association List -->
    <xsl:call-template name="ViewUIFromAssociationList">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_view', $capNameCaseType, 'FromAssociationList')"/>
      <xsl:with-param name="capName" select="$capName" />
      <xsl:with-param name="fromCreateParentInd" select="'No'"/>

    </xsl:call-template>

    <xsl:variable name="fromEntityName"><xsl:for-each select="Relationships/Association[@from!='']"><xsl:value-of select="@from"/></xsl:for-each></xsl:variable>
    <xsl:variable name="assocStartDateField"><xsl:value-of select="//EvidenceEntities/EvidenceEntity[@name=$fromEntityName]/UserInterfaceLayer/Cluster/Field[@metatype=$metatypeAssocStartDate]/@columnName"/></xsl:variable>
    <xsl:variable name="assocEndDateField"><xsl:value-of select="//EvidenceEntities/EvidenceEntity[@name=$fromEntityName]/UserInterfaceLayer/Cluster/Field[@metatype=$metatypeAssocEndDate]/@columnName"/></xsl:variable>

    <!-- Create Confirm Cancel Associated Evidence User Interface -->
    <xsl:call-template name="ConfirmCancelAssociationUI">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_confirmCancel', $capNameCaseType, 'Association')"/>
      <xsl:with-param name="capName" select="$capName" />

    </xsl:call-template>

  </xsl:if>


  <xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@from!=''])>0">

      <!-- Create Resolve Associated Evidence Page -->
      <xsl:call-template name="ResolveAssociatedEvidence">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capNameCaseType, 'Association')"/>
        <xsl:with-param name="capName" select="$capName" />

      </xsl:call-template>

  </xsl:if>

  <!-- Process Creating Associations Type Relationships from Create Parent Evidence -->
  <!--<xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@from!=''])>0">


    <xsl:call-template name="ViewUIFromAssociationList">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_view', $capNameCaseType, 'FromAssociationList_fromCreateParent')"/>
      <xsl:with-param name="capName" select="$capName"/>
      <xsl:with-param name="fromCreateParentInd" select="'Yes'"/>

    </xsl:call-template>


    <xsl:call-template name="CreateUIFromAssociationList">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_create', $capNameCaseType, 'FromAssociationList_fromCreateParent')"/>
      <xsl:with-param name="capName" select="$capName" />
      <xsl:with-param name="fromCreateParentInd" select="'Yes'"/>

    </xsl:call-template>


    <xsl:call-template name="ConfirmCreateAssociationUI">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_confirmCreate', $capNameCaseType, 'Association_fromCreateParent')"/>
      <xsl:with-param name="capName" select="$capName" />
      <xsl:with-param name="fromCreateParentInd" select="'Yes'"/>

    </xsl:call-template>

  </xsl:if>-->


  <!-- Process Associations Type Relationships -->
  <xsl:for-each select="UserInterfaceLayer/Cluster">

    <xsl:for-each select="./association">

      <!-- Create Resolve Associated Evidence Page -->
      <xsl:call-template name="ResolveAssociatedEvidence">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capNameCaseType, @from)"/>
        <xsl:with-param name="capName" select="$capName" />

      </xsl:call-template>

      <!-- Cater for multiple associations -->
      <xsl:for-each select="../../../Relationships/Association">

        <!-- Create List Evidence For Association UI -->
        <xsl:call-template name="ListEvidenceForAssociation2UI">

          <xsl:with-param name="prefix" select="$prefix" />
          <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
          <xsl:with-param name="UIName" select="concat($prefix, '_list', @from, $caseType, 'For', $capNameCaseType, 'Association')"/>
          <xsl:with-param name="capName" select="@from" />
          <xsl:with-param name="childEvidenceName" select="$capName" />
          <xsl:with-param name="evidenceList"/>
          <xsl:with-param name="firstEvidenceName" select="$capName" />
          <xsl:with-param name="fromCreateParentInd" select="'No'"/>

        </xsl:call-template>

        <xsl:call-template name="ViewAssociatedEvidenceUI">

          <xsl:with-param name="prefix" select="$prefix" />
          <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
          <xsl:with-param name="UIName" select="concat($prefix, '_view', @from, $caseType, '_from', $capNameCaseType, 'View')"/>
          <xsl:with-param name="capName" select="$capName" />
          <xsl:with-param name="childEvidenceName" select="$capName" />

        </xsl:call-template>

        <!-- Create View Evidence UI From Association List -->
        <xsl:call-template name="ViewUIFromAssociationList">

          <xsl:with-param name="prefix" select="$prefix" />
          <xsl:with-param name="path" select="concat($rootPath, @from , '/')" />
          <xsl:with-param name="UIName" select="concat($prefix, '_view', @from, $caseType, 'FromAssociationList')"/>
          <xsl:with-param name="capName" select="@from" />
          <xsl:with-param name="childEvidenceName" select="$capName" />
          <xsl:with-param name="fromCreateParentInd" select="'No'"/>

        </xsl:call-template>


      </xsl:for-each>


      <!-- Create Confirm Cancel Associated Evidence User Interface -->
      <xsl:call-template name="ConfirmCancelAssociationUI">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="UIName" select="concat($prefix, '_confirmCancel', $capNameCaseType, 'Association')"/>
        <xsl:with-param name="capName" select="$capName" />

      </xsl:call-template>

    </xsl:for-each>

    <!-- Process Create Related Case Participant On Modify Type Interfaces -->
    <xsl:for-each select="UserInterfaceLayer/CreateParticipantOnModify">

      <!-- Create Resolve Related Case Participant Page -->
      <xsl:call-template name="ResolveRelatedCaseParticipant">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capName, @name)"/>
        <xsl:with-param name="relatedCaseParticipantName" select="@name" />

      </xsl:call-template>

<!-- DKenny Deprecated -->
      <!-- Create Create Related Case Participant User Interface -->
<!--  <xsl:call-template name="CreateRelatedCaseParticipantUI">

        <xsl:with-param name="lcPackage" select="$lcPackage" />
        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="UIName" select="concat($prefix, '_create', @name, 'CaseParticipant')"/>
        <xsl:with-param name="capName" select="$capName" />
        <xsl:with-param name="CreateRelatedCaseParticipantUI" select="."/>
        <xsl:with-param name="modifyUIName" select="$modifyUIName"/>

      </xsl:call-template> -->

    </xsl:for-each>

  </xsl:for-each>

  <!-- Cater for associations needing multiple views
  <xsl:if test="count(UserInterfaceLayer/Cluster/association)>1">
    <xsl:for-each select="Relationships/Association[@from!='']">

      <xsl:call-template name="ViewAssociatedEvidenceUI">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
        <xsl:with-param name="UIName" select="concat($prefix, '_view', @from, $caseType, '_from', $capNameCaseType, 'View')"/>
        <xsl:with-param name="capName" select="$capName" />
        <xsl:with-param name="childEvidenceName" select="$capName" />


      </xsl:call-template>
    </xsl:for-each>
  </xsl:if>
        -->


  <!-- Process Creating Associations Type Relationships from Create Parent Evidence -->
  <xsl:for-each select="Relationships/Association[@from!='' and @createButton='Yes']">

    <!-- Create View Evidence UI From Association List -->
    <xsl:call-template name="ViewUIFromAssociationList">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, @from , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_view', @from, $caseType, 'FromAssociationList_fromCreate')"/>
      <xsl:with-param name="capName" select="@from" />
      <xsl:with-param name="childEvidenceName" select="$capName" />
      <xsl:with-param name="fromCreateParentInd" select="'Yes'"/>

    </xsl:call-template>

  </xsl:for-each>

  <!-- Check if Child Evidence can have Multiple Parent Type -->
  <xsl:if test="(count(Relationships/Parent)&gt;0 and count(Relationships/Parent)>1) or (Relationships/@association='Yes' and count(Relationships/Association[@from!=''])>1)">

    <!-- Create Resolve Parent Evidence Page -->
    <xsl:call-template name="ResolveParentEvidence">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capName, 'EvidenceParent')"/>
      <xsl:with-param name="Relationships" select="Relationships"/>
      <xsl:with-param name="capName" select="$capName" />

    </xsl:call-template>

  </xsl:if>

  <xsl:if test="(Relationships/@association='Yes' and count(Relationships/Association[@to!=''])>1)">

    <!-- Create Resolve Children Evidence Page -->
    <xsl:call-template name="ResolveChildEvidence">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capNameCaseType, 'ChildAssociationList')"/>
      <xsl:with-param name="Relationships" select="Relationships"/>
      <xsl:with-param name="capName" select="$capName" />

    </xsl:call-template>

    <!-- Create Resolve Children Evidence Page -->
    <xsl:call-template name="ResolveChildEvidence">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capNameCaseType, 'ChildAssociationList_fromCreate')"/>
      <xsl:with-param name="Relationships" select="Relationships"/>
      <xsl:with-param name="capName" select="$capName" />
      <xsl:with-param name="fromCreate" select="'_fromCreate'"/>

    </xsl:call-template>

  </xsl:if>

  <!-- Check if Evidence is Parent -->
  <xsl:if test="Relationships/@parent='Yes' and count(Relationships/Child)>0 or (Relationships/@association='Yes' and count(Relationships/Association[@to!='' and @displayInHierarchy='Yes'])>0)">

    <!-- Create Hierarchy UI -->
    <xsl:call-template name="HierarchyUI">

      <xsl:with-param name="path" select="$hierarchyPath"/>
      <xsl:with-param name="UIName" select="concat($prefix, $capNameCaseType, 'Group')"/>
      <xsl:with-param name="capName" select="$capName"/>
      <xsl:with-param name="Relationships" select="Relationships"/>

    </xsl:call-template>

  </xsl:if>

    <!-- BEGIN, CR00118883, POB -->
    <!--<xsl:call-template name="ProcessRelatedRelationships">
      <xsl:with-param name="path" select="$path"/>
      <xsl:with-param name="entityName" select="@name"/>
    </xsl:call-template>-->
    <!-- END, CR00118883 -->

  <!-- COMMENT OUT OLD IMPL OF RELATIONSHIPS/RELATED
  <xsl:for-each select="Relationships/Related">

    <xsl:if test="@relatedRelationshipType!=''">

      <xsl:variable name="relatedRelationshipType" select="@relatedRelationshipType"/>


      <xsl:if test="count(//EvidenceEntities/EvidenceEntity[@name=$relatedRelationshipType])=0">
        <xsl:call-template name="ListRelatedEvidenceConstants">

          <xsl:with-param name="path" select="concat($rootPath, @relatedRelationshipType , '/')"/>
          <xsl:with-param name="relatedEvidenceName" select="@relatedRelationshipType"/>

        </xsl:call-template>
      </xsl:if>

      <xsl:if test="$createClientListForEmployment!='false'">


      <xsl:call-template name="ListRelatedEvidenceUI">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, @relatedRelationshipType , '/')"/>
        <xsl:with-param name="UIName" select="concat($prefix, '_listRelated', @relatedRelationshipType, $caseType)"/>
        <xsl:with-param name="relatedEvidenceName" select="@relatedRelationshipType"/>
        <xsl:with-param name="UISelectRelatedEvidence" select="concat($prefix, '_listCore', @to, $caseType,'Details')"/>
        <xsl:with-param name="cancelLink">Evidence_resolveWorkspace</xsl:with-param>

      </xsl:call-template>

      <xsl:call-template name="ListRelatedEvidenceUI">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, @relatedRelationshipType , '/')"/>
        <xsl:with-param name="UIName" select="concat($prefix, '_listRelated', @relatedRelationshipType, $caseType, '_fromActive')"/>
        <xsl:with-param name="relatedEvidenceName" select="@relatedRelationshipType"/>
        <xsl:with-param name="UISelectRelatedEvidence" select="concat($prefix, '_listCore', @to, $caseType,'Details_fromActive')"/>
        <xsl:with-param name="cancelLink">Evidence_resolveWorkspaceActive</xsl:with-param>

      </xsl:call-template>

      <xsl:call-template name="ListRelatedEvidenceUI">

        <xsl:with-param name="prefix" select="$prefix" />
        <xsl:with-param name="path" select="concat($rootPath, @relatedRelationshipType , '/')"/>
        <xsl:with-param name="UIName" select="concat($prefix, '_listRelated', @relatedRelationshipType, $caseType, '_fromInEdit')"/>
        <xsl:with-param name="relatedEvidenceName" select="@relatedRelationshipType"/>
        <xsl:with-param name="UISelectRelatedEvidence" select="concat($prefix, '_listCore', @to, $caseType,'Details_fromInEdit')"/>
        <xsl:with-param name="cancelLink">Evidence_resolveWorkspaceInEdit</xsl:with-param>

      </xsl:call-template>

    </xsl:if>

    </xsl:if>

  </xsl:for-each>
    -->

<!-- DKenny Deprecated -->
  <!-- Process ConcernRoleRelationship List pages -->
<!--<xsl:if test="@concernRoleRelationship='Yes'">


    <xsl:call-template name="ListParticipantRelationshipEvidenceUI">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, 'ClaimParticipant' , '/')"/>
      <xsl:with-param name="UIName" select="concat($prefix, '_listRelated', 'ClaimParticipantForRelationship', $caseType)"/>
      <xsl:with-param name="capName" select="$capName"/>
      <xsl:with-param name="displayName" select="$displayName"/>

      <xsl:with-param name="UISelectRelatedEvidence" select="concat($prefix, '_create', $capNameCaseType)"/>

    </xsl:call-template>

  </xsl:if>
 Dkenny -->

  <!-- Processsing Evidence with links to multiple related Evidence -->
<!--
  <xsl:if test="Relationships/@related='Yes'">

    <xsl:variable name="numUtilizedBy"><xsl:value-of select="count(Relationships/Related[@utilizedBy!=''])"/></xsl:variable>

    <xsl:if test="$numUtilizedBy&gt;0">

      <xsl:variable name="viewUIName"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capNameCaseType"/></xsl:variable>
-->
      <!-- Create Resolve Page to correct Related Evidence List -->
<!--      <xsl:call-template name="ResolveRelatedEvidence">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_resolve', $capName, 'Related', $caseType)"/>
      <xsl:with-param name="capName" select="$capName"/>
      <xsl:with-param name="Relationships" select="Relationships"/>


      </xsl:call-template>

    </xsl:if>

  </xsl:if>
-->
  <!--
  <xsl:if test="$capName=$WorkSeparation">
    <xsl:call-template name="ViewWorkSeparation">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_viewWorkSeparationEvidenceForSelfEmployment')"/>
      <xsl:with-param name="capName" select="$capName"/>

    </xsl:call-template>
  </xsl:if>-->

  <!-- Create Only For Integrated Evidence -->
  <!-- Dkenny Employment Related xslts should be called regardless of evidence location -->
  <!--<xsl:if test="($caseType='Evidence' and UserInterfaceLayer/Sitemaps/Sitemap/@name='IntegratedCase' ) ">-->

    <!-- Create Search Popup User Interfaces-->
    <!-- CDuffy
    <xsl:call-template name="CreateSearchPopupUI">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $searchPopupPath , '/')" />

    </xsl:call-template>
    -->

    <!-- Create Employment User Interfaces-->
    <xsl:call-template name="CreateEmploymentUI">

      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="path" select="concat($rootPath, $employmentPath , '/')" />

    </xsl:call-template>
  <!-- Dkenny Employment Related xslts should be called regardless of evidence location -->
  <!--</xsl:if>-->

  <!-- Create Only ForProduct Evidence -->
    <xsl:if test="($caseType='ProductEvidence' and UserInterfaceLayer/Sitemaps/Sitemap/@name='ProductDelivery' ) ">

    <!-- Need to create resolve pages for an entity if it exits on both integrated and product level -->
    <xsl:if test="@caseType='Integrated'">

    <xsl:call-template name="ResolveViewEvidenceForProduct">

      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="UIName" select="concat($prefix, '_resolveView', $capName, 'ForProductEvidence')"/>
      <xsl:with-param name="capName" select="$capName"/>
      <xsl:with-param name="prefix" select="$prefix" />

    </xsl:call-template>

    <!-- Create Resolve Page for modify -->
    <xsl:call-template name="ResolveModifyEvidenceForProduct">

      <xsl:with-param name="path" select="concat($rootPath, $capName , '/')" />
      <xsl:with-param name="prefix" select="$prefix" />
      <xsl:with-param name="UIName" select="concat($prefix, '_resolveModify', $capName, 'ForProductEvidence')"/>
      <xsl:with-param name="capName" select="$capName"/>

   </xsl:call-template>

   </xsl:if>

  </xsl:if>

  <!-- BEGIN, CR00100405, CD -->
  <!-- Removing override loop -->
  <!-- END, CR00100405 -->

  <!-- Close Log File -->
  </redirect:write>
  <redirect:close select="$logfilename"/>

</xsl:template>

</xsl:stylesheet>