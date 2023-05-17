<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2006,2021. All Rights Reserved.

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
  <xsl:key name="Attribute" match="Attributes/Attribute" use="@name"/>

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt" />

  <xsl:template match="EvidenceEntity[EntityLayer]">

    <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="ucName"><xsl:value-of select="translate($capName, $lcletters, $ucletters)"/></xsl:variable>
    <xsl:variable name="lcName"><xsl:value-of select="translate($capName, $ucletters, $lcletters)"/></xsl:variable>
    <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>

    <!-- BEGIN, CR00100405, CD -->
    <xsl:variable name="returnType"><xsl:value-of select="$EntityName"/>Dtls</xsl:variable>

    <xsl:variable name="returnTypeVar"><xsl:value-of select="$lcName"/>Dtls</xsl:variable>

    <xsl:variable name="argumentType"><xsl:value-of select="$EntityName"/>Key</xsl:variable>
    <!-- END, CR00100405 -->

    <redirect:write select="concat($EntityName, '.imports')">

<xsl:element name="imports">

  <!-- BEGIN, CR00220936, CD -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.Date;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00220936, CD -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EvidenceTransferDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.CaseHeaderKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.CASEEVIDENCE;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.EVIDENCEDESCRIPTORSTATUS;</xsl:attribute>
  </xsl:element>

  <xsl:for-each select="UserInterfaceLayer/Cluster/Field[@display='Yes' and @codetable!='']">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.<xsl:value-of select="@codetable"/>;</xsl:attribute>
  </xsl:element>
  </xsl:for-each>

  <xsl:if test="UserInterfaceLayer/Cluster/Field[@display='Yes'] and Field[@type='ADDRESS_ID'] and @displayAttribute!=''">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.fact.AddressFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.intf.Address;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:if test="@cachingEnabled='true' or @cachingEnabled='TRUE'">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.impl.EnvVars;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.resources.Configuration;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:if test="count(Attributes/Attribute[@type='CASE_PARTICIPANT_ROLE_ID'])>0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.fact.CaseParticipantRoleFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.intf.CaseParticipantRole;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseIDParticipantRoleKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleKey;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;</xsl:attribute>
  </xsl:element>

  <xsl:if test="count(Relationships/PreAssociation) &gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/Parent)&gt;0 or count(Relationships/Child)&gt;0 or count(Relationships/Association)&gt;0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.fact.EvidenceRelationshipFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.intf.EvidenceRelationship;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.AttributedDateDetails;</xsl:attribute>
  </xsl:element>

  <xsl:if test = "count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/Parent)&gt;0 or count(Relationships/Association[@from!=''])>0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.ChildKey;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:if test = "count(Relationships/PreAssociation) &gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/Child)&gt;0 or count(Relationships/Parent)&gt;0 or count(Relationships/Association)&gt;0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.ChildKeyList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.ParentKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.intf.EvidenceController;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.InformationalElement;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:if test = "BusinessDates[@startDate!=''] and BusinessDates[@startDateNotOnEntity='true'] and BusinessDates[@endDate!=''] and BusinessDates[@endDateNotOnEntity='true']">
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.service.fact.<xsl:value-of select="$EntityName"/>Factory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.Read<xsl:value-of select="$EntityName"/>EvidenceDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceCaseKey;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;</xsl:attribute>
  </xsl:element>

  <xsl:if test = "count(Relationships/Child)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/Parent)&gt;0 or count(Relationships/Association[@from!=''])>0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.ParentKeyList;</xsl:attribute>
  </xsl:element>
  </xsl:if>
  <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/PreAssociation[@to!=''])&gt;0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ParentList;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;</xsl:attribute>
  </xsl:element>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIEvidenceKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIFieldsForListDisplayDtls;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.struct.ValidateMode;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CaseIDKey;</xsl:attribute>
  </xsl:element>

  <!--
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.core.struct.EvidenceDescriptorDetails;</xsl:attribute>
  </xsl:element>-->

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.EvidenceDescriptorDetails;</xsl:attribute>
  </xsl:element>

  <xsl:if test="UserInterfaceLayer/Cluster/Field[@display='Yes'] and Field[@type='ADDRESS_ID'] and @displayAttribute!=''">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.AddressElementStruct;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.AddressKey;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.CaseKey;</xsl:attribute>
  </xsl:element>

  <xsl:if test="UserInterfaceLayer/Cluster/Field[@display='Yes'] and Field[@type='ADDRESS_ID'] and @displayAttribute!=''">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.struct.OtherAddressData;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:if test="count(EntityLayer/Event)>0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.events.<xsl:value-of select="$prefix"/>Evidence;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.events.impl.EventService;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.events.struct.Event;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/Child)&gt;0 or count(Relationships/Association)&gt;0 or count(Relationships/Parent)&gt;0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.message.ENT<xsl:value-of select="$ucPrefix"/>GENERALERROR;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.message.<xsl:value-of select="$ucPrefix"/>GENERALERROR;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.AppException;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.InformationalException;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.exception.InformationalManager;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.transaction.TransactionInfo;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;</xsl:attribute>
  </xsl:element>

  <xsl:variable name="CodeTable"
    select="(UserInterfaceLayer/Cluster/Field/@display='Yes' and
             not(UserInterfaceLayer/Cluster/Field/@type='ADDRESS_ID' and
                 UserInterfaceLayer/Cluster/Field/@displayAttribute!='') and
             (UserInterfaceLayer/Cluster/Field/@metaType=$metatypeCodetable and
              UserInterfaceLayer/Cluster/Field/@codetable!=''))"/>

  <xsl:if test="$CodeTable">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.CodeTable;</xsl:attribute>
  </xsl:element>
  </xsl:if>
  <xsl:if test="$CodeTable or
    count(Relationships/MandatoryParents/Parent)&gt;0 or
                count(Relationships/Parent)&gt;0 or
                count(Relationships/Child)&gt;0 or
                Relationships/@association='Yes'">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.util.type.CodeTableItemIdentifier;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <!-- BEGIN, CR00100405, CD -->
  <!-- Removing references to override callout class -->

  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.hook.fact.<xsl:value-of select="$EntityName"/>HookFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$EntityName"/>EvidenceDetails;</xsl:attribute>
  </xsl:element>

  <xsl:if test= "Attributes/Attribute/@displayAttribute!=''">
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.ReadEmploymentAddressEvidenceDetails;</xsl:attribute>
  </xsl:element>
  </xsl:if>

  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$argumentType"/>;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$returnType"/>;</xsl:attribute>
  </xsl:element>
  <!-- END, CR00100405 -->

  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.validation.intf.Validate<xsl:value-of select="$EntityName"/>;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import <xsl:value-of select="$javaEvidenceCodePath"/>.validation.fact.Validate<xsl:value-of select="$EntityName"/>Factory;</xsl:attribute>
  </xsl:element>

  <!-- BEGIN, CR00120824, CD -->
  <xsl:if
    test="count(UserInterfaceLayer/Cluster/Field[
          @metatype=$metatypeParentCaseParticipant or
          @metatype=$metatypeEmployerCaseParticipant or
          @metatype=$metatypeCaseParticipantSearch or
          @metatype=$metatypeDisplayCaseParticipant]) &gt; 0">
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.CASEPARTICIPANTROLETYPE;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.codetable.RECORDSTATUS;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.struct.CaseParticipantRoleDetails;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.fact.CaseParticipantRoleFactory;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.intf.CaseParticipantRole;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseIDParticipantRoleKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleKey;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleDtls;</xsl:attribute>
  </xsl:element>
  </xsl:if>
  <!-- END, CR00120824 -->
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.entity.struct.CaseParticipantRoleDtlsList;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;</xsl:attribute>
  </xsl:element>
  <xsl:element name="import">
    <xsl:attribute name="value">  import curam.core.sl.infrastructure.struct.EvidencePeriod;</xsl:attribute>
  </xsl:element>

</xsl:element>
    </redirect:write>

</xsl:template>

</xsl:stylesheet>
