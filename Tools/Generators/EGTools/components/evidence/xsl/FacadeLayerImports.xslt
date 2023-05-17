<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2018. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008,2010 Curam Software Ltd.  All rights reserved.

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
  <xsl:output method="xml"/>

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt" />

  <xsl:template match="EvidenceEntities">

    <xsl:variable name="filename">Facade.imports</xsl:variable>

    <xsl:variable name="generalError">ENT<xsl:value-of select="$ucPrefix"/>GENERALERROR</xsl:variable>

    <xsl:variable name="modelEvidenceCodePath">
      <xsl:choose>
        <xsl:when test="//EvidenceEntities/@package='evidence' ">evidence</xsl:when>
        <xsl:otherwise><xsl:value-of select="//EvidenceEntities/@package"/>.evidence</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <redirect:open select="$filename" method="text" append="false" />
    <redirect:write select="$filename">

<xsl:element name="imports">
	<!-- BEGIN, 191675, JAY -->
  <xsl:element name="import">
  	<xsl:attribute name="value">import com.google.inject.Inject;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
  <!-- END, 191675, JAY -->
    <xsl:attribute name="value">import curam.core.intf.MaintainConcernRoleDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.fact.MaintainConcernRoleDetailsFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.ParticipantRoleIDAndNameDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.ReadConcernRoleKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.ReadConcernRoleDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.ConcernRolesDetails;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.message.<xsl:value-of select="$generalError"/>;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.<xsl:value-of select="$modelEvidenceCodePath"/>.facade.struct.<xsl:value-of select="$prefix"/>RelatedEmploymentDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.<xsl:value-of select="$modelEvidenceCodePath"/>.facade.struct.<xsl:value-of select="$prefix"/>RelatedEmploymentKey;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.fact.EvidenceRelationshipFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.ChildKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.ParentKeyList;</xsl:attribute>
  </xsl:element>

  <!-- BEGIN, CR00219910, CD -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.SuccessionID;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00219910 -->
  <!-- BEGIN, CR00118883, POB -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CaseParticipantIDNameAndDOBDetailsList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CaseParticipantIDNameAndDOBDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.fact.CaseParticipantRoleFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.intf.CaseParticipantRole;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.NotFoundIndicator;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.fact.PersonFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.intf.Person;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.PersonDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.PersonKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.Date;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00118883 -->
  <!-- BEGIN, CR00114649, POB -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceTypeAndDescription;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceTypeAndDescriptionList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceTypeKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.CodeTable;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00114649 -->
  <!-- BEGIN, CR00101875, POB -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.ParentEvidenceLink;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIFieldsForListDisplayDtls;</xsl:attribute>
  </xsl:element>

  <!-- END, CR00101875 -->

  <!-- BEGIN, CR00100662, POB -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.facade.infrastructure.fact.EvidenceFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.facade.infrastructure.intf.Evidence;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.facade.infrastructure.struct.EvidenceListKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.facade.infrastructure.struct.EvidenceListDetails;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00100662 -->
  <!-- BEGIN, 191675, JAY, Auto end date evidence -->
  <xsl:element name="import">
  	<xsl:attribute name="value">import curam.dynamicevidence.validation.impl.MultiFailOperation;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
  	<xsl:attribute name="value">import curam.core.sl.infrastructure.impl.AutoEndDateEvidenceOperations;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
  	<xsl:attribute name="value">import curam.core.sl.infrastructure.impl.AutoEndDateEvidenceHook;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
  	<xsl:attribute name="value">import curam.core.facade.infrastructure.struct.PageNameDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
  	<xsl:attribute name="value">import curam.core.facade.infrastructure.struct.AutoEndDateEvidenceDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
  	<xsl:attribute name="value">import curam.core.facade.infrastructure.struct.AutoEndDateReturnEvidenceDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;</xsl:attribute>
  </xsl:element>
    <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.resources.StringUtil;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.StringList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.impl.CuramConst;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
  	<xsl:attribute name="value">import curam.util.resources.Locale;</xsl:attribute>
  </xsl:element>
  <!-- BEGIN, 191675, JAY, Auto End Date -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceCaseKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.CaseIDAndEvidenceTypeKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ECParentDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ECParentEvidenceDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ECEvidenceForListPageDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIListCaseEvidenceKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EvidenceTypeDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EvidenceTypeDtlsList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.CaseKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.CASEEVIDENCE;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.fact.CaseHeaderFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.intf.CaseHeader;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CaseIDKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.ReturnEvidenceDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.ReturnAutoEndDateEvidenceDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.CaseHeaderKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.ConcernRoleDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.ReadParticipantRoleIDDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.SearchCaseParticipantDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.SearchCaseParticipantDetailsKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.SearchCaseParticipantDetailsList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.fact.GeneralUtilityFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.intf.GeneralUtility;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EmploymentDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CaseParticipantEmployerConcernRoleKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIEvidenceKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.facade.struct.ReadEmploymentDetailsList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CreateWizardEmploymentDetailsList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CreateWizardEmploymentDetails;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.CONCERNROLEALTERNATEID;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.RECORDSTATUS;</xsl:attribute>
  </xsl:element>
    <xsl:element name="import">
  <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ConcatenateNameAndAlternateIDSSNDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ConcatenateNameAndAlternateIDSSNKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.events.impl.EventService;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.events.struct.Event;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.aes.sl.incomingevidence.impl.IncomingEvidenceUtil;</xsl:attribute>
  </xsl:element>

  <xsl:for-each select="EvidenceEntity">
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.Read<xsl:value-of select="@name"/>EvidenceDetails;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="@name"/>EvidenceDetails;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.service.fact.<xsl:value-of select="@name"/>Factory;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.service.intf.<xsl:value-of select="@name"/>;</xsl:attribute>
    </xsl:element>
  </xsl:for-each>

   <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.persistence.GuiceWrapper;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.AppException;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.InformationalException;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.transaction.TransactionInfo;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.fact.CachedCaseHeaderFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.intf.CachedCaseHeader;</xsl:attribute>
  </xsl:element>

  <xsl:variable name="numberOfOptionalParticipants">
      <xsl:call-template name="getNumberOfOptionalParticipants">
        <xsl:with-param name="relatedParticipantDetails" select="EvidenceEntity/ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes']"/>
        <xsl:with-param name="numberOfOptionalParticipants" select="number(0)"/>
      </xsl:call-template>
  </xsl:variable>

  <xsl:if test="$numberOfOptionalParticipants &gt; 0">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.struct.OptionalParticipantDetails;</xsl:attribute>
    </xsl:element>
  </xsl:if>

</xsl:element>

      </redirect:write>
      <redirect:close select="$filename"/>

  </xsl:template>

</xsl:stylesheet>
