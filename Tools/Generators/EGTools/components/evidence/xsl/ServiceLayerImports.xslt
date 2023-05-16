<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006, 2020. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008 Curam Software Ltd.  All rights reserved.

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

  <xsl:template match="EvidenceEntity[ServiceLayer]">

    <xsl:variable name="EntityName"><xsl:value-of select="@name"/></xsl:variable>

    <!-- BEGIN, CR00100657, POB -->
    <xsl:variable name="modifiable" select="@modify"/>
    <!-- END, CR00100657 -->

    <!-- BEGIN, CR00100405, CD -->
    <xsl:variable name="ProductEvidenceDtls"><xsl:value-of select="$EntityName"/>Dtls</xsl:variable>
    <!-- END, CR00100405 -->

    <xsl:variable name="casePartRoleIDattr"><xsl:value-of select="@relateEvidenceParticipantID"/></xsl:variable>

    <xsl:variable name="EvidenceDetails"><xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
    <xsl:variable name="ReadEvidenceDetails">Read<xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>

    <xsl:variable name="generalError">ENT<xsl:value-of select="$ucPrefix"/>GENERALERROR</xsl:variable>

    <xsl:variable name="CaseParticipantRoleIDAttributes" select="Attributes/Attribute[@type=$typeCaseParticipantRoleID]"/>

    <xsl:variable name="ReadParticipanCaseAndUserDetails" select="Relationships/@parent!='Yes' and count($CaseParticipantRoleIDAttributes)=0" />

    <redirect:write select="concat($EntityName, '.imports')">

<xsl:element name="imports">

  <!-- BEGIN, CR00435583, JAF -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.util.impl.ObjectComparator;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import com.google.inject.Inject;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.persistence.GuiceWrapper;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00435583 -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.intf.MaintainConcernRoleDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.fact.MaintainConcernRoleDetailsFactory;</xsl:attribute>
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

  <xsl:if test="count(Relationships/Parent)&gt;0">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.util.resources.StringUtil;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.util.type.StringList;</xsl:attribute>
    </xsl:element>
  </xsl:if>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.fact.AddressDataFactory;</xsl:attribute>
  </xsl:element>
  <!-- BEGIN, 25/02/2008, CD -->
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.customise.fact.Customise<xsl:value-of select="$EntityName"/>Factory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.customise.intf.Customise<xsl:value-of select="$EntityName"/>;</xsl:attribute>
  </xsl:element>
  <!-- END, 25/02/2008, CD -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.CASEEVIDENCE;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.EVIDENCEDESCRIPTORSTATUS;</xsl:attribute>
  </xsl:element>
  <!-- BEGIN, CR00207324, GSP -->
  <xsl:element name="import">
      <xsl:attribute name="value">import curam.codetable.EVIDENCECHANGEREASON;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00207324 -->
   <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.impl.CuramConst;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.InformationalElement;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.SearchCaseParticipantDetails;</xsl:attribute>
  </xsl:element>

  <xsl:if test="(count(ServiceLayer/RelatedParticipantDetails[@createParticipantType=$ParticipantTypeServiceProvider or
               @createParticipantType=$ParticipantTypeEmployer or
               @createParticipantType=$ParticipantTypeUnknown or
               @createParticipantType=$ParticipantTypePerson or
               @createParticipantType=$ParticipantTypeUnion or
               @createParticipantType=$ParticipantTypeCoreEmployer]) > 0) or
    (ServiceLayer/@createRelatedParticipant='Yes' and count(ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes']) > 0)">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.codetable.CASEPARTICIPANTROLETYPE;</xsl:attribute>
    </xsl:element>
  </xsl:if>

  <xsl:if test="(count(ServiceLayer/RelatedParticipantDetails[@createParticipantType=$ParticipantTypeServiceProvider or
                  @createParticipantType=$ParticipantTypeEmployer or
                  @createParticipantType=$ParticipantTypeUnknown or
                  @createParticipantType=$ParticipantTypePerson or
                  @createParticipantType=$ParticipantTypeUnion or
                  @createParticipantType=$ParticipantTypeCoreEmployer]) > 0) and
                 ServiceLayer/@createRelatedParticipant='Yes'">

    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.struct.CaseParticipantRoleDetails;</xsl:attribute>
    </xsl:element>

    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseIDAndParticipantRoleIDDetails;</xsl:attribute>
    </xsl:element>

    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.entity.struct.ReadByParticipantRoleTypeAndCaseDetails;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.entity.struct.ReadByParticipantRoleTypeAndCaseKey;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.util.type.NotFoundIndicator;</xsl:attribute>
    </xsl:element>
  </xsl:if>


  <xsl:if test="count(ServiceLayer/RelatedParticipantDetails[@createParticipantType=$ParticipantTypeCoreEmployer])>0">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.fact.EmploymentFactory;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.intf.Employment;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.struct.EmploymentDtls;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.struct.EmploymentKey;</xsl:attribute>
    </xsl:element>
  </xsl:if>

  <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.struct.EmptyIndStruct;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.struct.OtherAddressData;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.fact.AddressFactory;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.intf.Address;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.util.exception.InformationalManager;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.util.transaction.TransactionInfo;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.struct.RepresentativeRegistrationDetails;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.fact.RepresentativeFactory;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.intf.Representative;</xsl:attribute>
    </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.Date;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleKey;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.fact.CaseParticipantRoleFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIEvidenceKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.intf.CaseParticipantRole;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceCaseKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.ReturnEvidenceDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$ReadEvidenceDetails"/>;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$EvidenceDetails"/>;</xsl:attribute>
  </xsl:element>
  <!-- BEGIN, CR00100405, CD -->
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$ProductEvidenceDtls"/>;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00100405 -->

  <xsl:if test="Relationships/@relatedEntityAttributes='Yes'">
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.relatedattribute.intf.<xsl:value-of select="$EntityName"/>RelatedEntityAttributes;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.relatedattribute.fact.<xsl:value-of select="$EntityName"/>RelatedEntityAttributesFactory;</xsl:attribute>
    </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.AppException;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.InformationalException;</xsl:attribute>
  </xsl:element>

  <xsl:if test="(Relationships/@association='Yes' and count(Relationships/Association[@from!=''])>0) or
                 $casePartRoleIDattr='' or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/Parent)&gt;0">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.intf.EvidenceRelationship;</xsl:attribute>
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
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.ParentKey;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ParentList;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceRelationshipParentChildKey;</xsl:attribute>
    </xsl:element>
  </xsl:if>
  <!-- BEGIN, CR00112474, DG -->
  <xsl:if test="$casePartRoleIDattr=''">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.fact.CaseHeaderFactory;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.struct.CaseHeaderKey;</xsl:attribute>
    </xsl:element>
  </xsl:if>
  <!-- END, CR00112474 -->
  <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIFieldsForListDisplayDtls;</xsl:attribute>
    </xsl:element>
  </xsl:if>

  <!-- BEGIN, 25/02/2008, CD -->
  <xsl:if test="count(UserInterfaceLayer/Cluster/Field[@mandatory='Yes' and @notOnEntity='Yes']) &gt; 0">
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$EntityName"/>NonEvidenceDetails;</xsl:attribute>
    </xsl:element>
  </xsl:if>
  <!-- END, 25/02/2008, CD -->

  <!-- BEGIN, CR000, PADDY -->
    <xsl:element name="import">
      <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.facade.fact.<xsl:value-of select="$ucPrefix"/>EvidenceMaintenanceFactory;</xsl:attribute>
    </xsl:element>
  <!-- END, CR000 -->

  <!-- BEGIN, CR00098559, PADDY -->
  <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EIMultiParentEvidenceInsertDtls;</xsl:attribute>
    </xsl:element>
  </xsl:if>
  <!-- END, CR00098559 -->

  <xsl:variable name="numberOfOptionalParticipants">
      <xsl:call-template name="getNumberOfOptionalParticipants">
        <xsl:with-param name="relatedParticipantDetails" select="ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes']"/>
        <xsl:with-param name="numberOfOptionalParticipants" select="number(0)"/>
      </xsl:call-template>
  </xsl:variable>

  <xsl:if test="$numberOfOptionalParticipants &gt; 0">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.struct.OptionalParticipantDetails;</xsl:attribute>
    </xsl:element>
  </xsl:if>

  <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.struct.CaseIDKey;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.facade.struct.CaseParticipantRoleIDKey;</xsl:attribute>
    </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.message.<xsl:value-of select="$generalError"/>;</xsl:attribute>
  </xsl:element>
   <!-- BEGIN, 123218, JAY -->
   <xsl:element name="import">
    <xsl:attribute name="value">import curam.message.GENERAL;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.LocalisableString;</xsl:attribute>
  </xsl:element>
  <!-- END, 123218, JAY -->

  <!-- BEGIN, CR00100657, POB -->
  <xsl:if test="$modifiable='No'">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.util.transaction.TransactionInfo;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.util.exception.InformationalManager;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ECWarningsDtls;</xsl:attribute>
    </xsl:element>
  </xsl:if>
  <!-- END, CR00100657 -->

  <!-- BEGIN, CR00102248, POB -->
   <xsl:if test="//EvidenceEntities/@displayAltID='Yes'">
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ConcatenateNameAndAlternateIDSSNKey;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ConcatenateNameAndAlternateIDSSNDetails;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.codetable.CONCERNROLEALTERNATEID;</xsl:attribute>
    </xsl:element>
    <xsl:element name="import">
      <xsl:attribute name="value">import curam.core.sl.infrastructure.fact.GeneralUtilityFactory;</xsl:attribute>
    </xsl:element>
  </xsl:if>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.RECORDSTATUS;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.ParticipantRoleIDAndNameDetails;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00102248 -->

  <!-- BEGIN, CR00119218, CD -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EvidenceComparisonDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EvidenceAttributeDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import java.util.Locale;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import java.util.ResourceBundle;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import java.util.MissingResourceException;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EvidenceComparisonHelper;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.Evidence2Compare;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00119218 -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.CaseHeaderDtls;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EvidenceInterface;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.aes.sl.delivery.processor.impl.DeliveryPlanProcessorUtil;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.SuccessionID;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.SuccessionIDAndStatus;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import java.util.SortedSet;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import java.util.TreeSet;</xsl:attribute>
  </xsl:element>

  <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/PreAssociation[@to!=''])&gt;0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ParentList;</xsl:attribute>
  </xsl:element>
</xsl:if>
</xsl:element>


    </redirect:write>

  </xsl:template>

</xsl:stylesheet>
