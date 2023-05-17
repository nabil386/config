<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2012,2021. All Rights Reserved.

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
  <xsl:output method="text"/>

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt" />

  <xsl:param name="productEvidenceHome"/>
  <xsl:param name="date"/>

  <xsl:template match="EvidenceEntity[ServiceLayer]">

    <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="casePartRoleIDattr"><xsl:value-of select="@relateEvidenceParticipantID"/></xsl:variable>
    <xsl:variable name="ucName"><xsl:value-of select="translate($capName, $lcletters, $ucletters)"/></xsl:variable>
    <xsl:variable name="lcName"><xsl:value-of select="translate($capName, $ucletters, $lcletters)"/></xsl:variable>
    <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>

    <xsl:variable name="modifiable" select="@modify"/>

    <xsl:variable name="EvidenceType"><xsl:value-of select="$ucName"/></xsl:variable>

    <xsl:variable name="EvidenceKey"><xsl:value-of select="$capName"/>Key</xsl:variable>
    <xsl:variable name="EvidenceDtls"><xsl:value-of select="$capName"/>Dtls</xsl:variable>

    <!-- BEGIN, CR00100405, CD -->
    <xsl:variable name="ProductEvidenceDtls"><xsl:value-of select="$EntityName"/>Dtls</xsl:variable>
    <!-- END, CR00100405 -->

    <xsl:variable name="EvidenceDetails"><xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
    <xsl:variable name="ReadEvidenceDetails">Read<xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
    <xsl:variable name="readEvidenceDetails">read<xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>

    <xsl:variable name="evidenceDtls">dtls</xsl:variable>

    <xsl:variable name="evidenceDescriptorDetails">descriptor</xsl:variable>
    <xsl:variable name="evidenceDescriptorDtls">evidenceDescriptorDtls</xsl:variable>
    <xsl:variable name="caseIDKey">caseIDKey</xsl:variable>
    <xsl:variable name="evidenceKey">evidenceKey</xsl:variable>
    <xsl:variable name="evidenceCaseKey">evidenceCaseKey</xsl:variable>
    <xsl:variable name="warnings">warnings</xsl:variable>
    <xsl:variable name="generalError">ENT<xsl:value-of select="$ucPrefix"/>GENERALERROR</xsl:variable>

    <xsl:variable name="CreateCaseParticipantDetails">CreateCaseParticipantDetails</xsl:variable>
    <xsl:variable name="caseParticipantDetails"><xsl:value-of select="$lcPrefix"/>CaseParticipantDetails</xsl:variable>

    <xsl:variable name="AddressAttributes" select="Attributes/Attribute[@type=$typeAddressID]"/>
    <xsl:variable name="CaseParticipantRoleIDAttributes" select="Attributes/Attribute[@type=$typeCaseParticipantRoleID]"/>

    <xsl:variable name="ParentName"><xsl:value-of select="Relationships/Parent/@name"/></xsl:variable>

    <xsl:variable name="numberOfMandatoryAttributes" select="count(UserInterfaceLayer/Cluster/Field[@mandatory='Yes'])"/>


    <redirect:write select="concat($EntityName, '.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

package <xsl:value-of select="$javaEvidenceCodePath"/>.service.impl;

<xsl:call-template name="GetSortedImports">
  <xsl:with-param name="entity" select="$EntityName"/>
</xsl:call-template>
/**
 * Service Layer interface for <xsl:value-of select="$EntityName"/>
 */
@SuppressWarnings("all")
public abstract class <xsl:value-of select="$EntityName"/>
  extends <xsl:value-of select="$javaEvidenceCodePath"/>.service.base.<xsl:value-of select="$EntityName"/> implements Evidence2Compare {

  /**
   * Constant for the Holding Evidence type.
   */
  private final String kHoldingEvidence = "ET10000";

  @Inject
  private ObjectComparator objectComparator;

  @Inject
  protected curam.aes.sl.incomingevidence.impl.IncomingEvidenceUtil incomingEvidenceUtil;

  @Inject
  private curam.aes.sl.incomingevidence.impl.IncomingEvidenceMaintenanceHandler evidenceMaintenanceHandler;

  @Inject(optional = true)
  private curam.aes.sl.delivery.processor.evidence.impl.HoldingEvidenceConversion holdingEvidenceConversion;

  /**
   * Utility class.
   */
  @Inject
  DeliveryPlanProcessorUtil util;

  public <xsl:value-of select="$EntityName"/>() {
    GuiceWrapper.getInjector().injectMembers(this);
  }
  <!-- END, CR00435583 -->

  //___________________________________________________________________________
  /**
   * <p>
   * This method checks if informational messages have been added to
   * <code>InformationalManager</code>. It checks for entries of type
   * <code>kError</code> or <code>kFatalError</code>. If these exist, then the
   * failOperation() is called and the transaction will be rolled back.
   * </p>
   * <p>
   * Entries of type <code>kWarning</code> are rendered as informationals on
   * the page if the page is expecting informationals.
   * </p>
   */
  private void checkForInformationals()
    throws AppException, InformationalException {

    java.util.LinkedList list =
      curam.util.transaction.TransactionInfo.getInformationalManager(
        ).getMessagesForField(CuramConst.gkEmpty);

    for (int i = 0; i &lt; list.size(); i++) {

      curam.util.exception.InformationalElement element =
        (curam.util.exception.InformationalElement) list.get(i);

      if (element.getInformationalType().equals(
        curam.util.exception.InformationalElement.InformationalType.kError)
          || element.getInformationalType().equals(
            curam.util.exception.InformationalElement.InformationalType.kFatalError)) {

        curam.util.transaction.TransactionInfo.getInformationalManager().failOperation();
      }

    }

  }


  <!-- BEGIN, CR00127279, CD -->
  //___________________________________________________________________________
  /**
   * Creates a <xsl:value-of select="$capName"/> record.
   *
   * @param dtls contains <xsl:value-of select="$capName"/> evidence
   * record creation details.
   *
   * @return the new evidence ID and warnings.
   */
  public ReturnEvidenceDetails create<xsl:value-of select="$EntityName"/>Evidence(
    <xsl:value-of select="$EvidenceDetails"/> dtls)
    throws AppException,InformationalException {

    return create<xsl:value-of select="$EntityName"/>Evidence(dtls, null, null, false);
  }
  <!-- END, CR00127279 -->


  //___________________________________________________________________________
  /**
   * Creates a <xsl:value-of select="$capName"/> record.
   *
   * @param dtls contains <xsl:value-of select="$capName"/> evidence
   *   record creation details.
   * @param sourceEvidenceDescriptorDtls If this function is called during
   *   evidence sharing, this parameter will be non-null and it represents
   *   the header of the evidence record being shared (i.e. the source evidence
   *   record)
   * @param targetCase If this function is called during evidence sharing,
   *   this parameter will be non-null and it represents the case the evidence
   *   is being shared with.
   * @param sharingInd A flag to determine if the function is called in evidence
   *   sharing mode. If <code>false</code>, the function is being called as
   *   part of a regular create.
   *
   * @return the new evidence ID and warnings.
   */
  public ReturnEvidenceDetails create<xsl:value-of select="$EntityName"/>Evidence(
    <xsl:value-of select="$EvidenceDetails"/> dtls,
    EvidenceDescriptorDtls sourceEvidenceDescriptorDtls,
    CaseHeaderDtls targetCase, boolean sharingInd)
    throws AppException,InformationalException {

    <xsl:if test="count(Relationships/Parent)&gt;0">
    if (dtls.parEvKey.evidenceID == 0 || dtls.parEvKey.evType.length()==0) {
      // get the selected parent details
      StringList evidenceIDAndTypeStringList =
        StringUtil.delimitedText2StringList(dtls.selectedParent.tabbedDetails, '|');
      if (evidenceIDAndTypeStringList.size() == 2) {
        dtls.parEvKey.evidenceID =
          Long.parseLong(evidenceIDAndTypeStringList.item(0));
        dtls.parEvKey.evType =
          evidenceIDAndTypeStringList.item(1);
      }
    }
<!-- BEGIN, 25/02/2008, CD -->
    </xsl:if>

    // validate the mandatory fields
    //validateMandatoryDetails(dtls);

    // customisation point
    <!-- BEGIN, 25/02/2008, CD -->
    Customise<xsl:value-of select="$EntityName"/> customise<xsl:value-of select="$EntityName"/> = Customise<xsl:value-of select="$EntityName"/>Factory.newInstance();
    customise<xsl:value-of select="$EntityName"/>.preCreate(dtls);
    <!-- END, 25/02/2008, CD -->

    <xsl:if test="count(Relationships/Parent)&gt;0">
      <!-- If the participant related to this evidence (to go on its descriptor) is blank,
           it means we need to find the participant by drilling up through the record's ancestors. -->
      <xsl:if test="$casePartRoleIDattr=''">

    EIEvidenceKey parEvKey = new EIEvidenceKey();

    parEvKey.evidenceID = dtls.parEvKey.evidenceID;
    parEvKey.evidenceType = dtls.parEvKey.evType;

    <!-- BEGIN, CR00101870, POB -->
    <!--
      Need to make sure that the getCaseParticipantRoleID method only gets called if the evidenceID is not 0
    -->
    long retrievedCPRID = 0;
    if(parEvKey.evidenceID != 0){
      retrievedCPRID = <xsl:value-of select="$ucPrefix"/>EvidenceMaintenanceFactory.newInstance()
        .getCaseParticipantRoleID(parEvKey).caseParticipantRoleID;
    }

      </xsl:if>
    </xsl:if>

    <!-- BEGIN, CR00098559, POB -->
      <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">
        <!-- If the participant related to this evidence (to go on its descriptor) is blank,
          it means we need to find the participant by drilling up through the record's ancestors. -->
        <xsl:if test="$casePartRoleIDattr=''">

          <xsl:variable name="mParentName" select="Relationships/MandatoryParents/Parent/@name"/>
          <xsl:variable name="lcMParentName"><xsl:value-of select="translate(substring($mParentName, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring($mParentName, 2)"/></xsl:variable>

          EIEvidenceKey parEvKey = new EIEvidenceKey();

          parEvKey.evidenceID = dtls.<xsl:value-of select="$lcMParentName"/>ParEvKey.evidenceID;
          parEvKey.evidenceType = dtls.<xsl:value-of select="$lcMParentName"/>ParEvKey.evType;

          long retrievedCPRID = 0;
          if(parEvKey.evidenceID != 0){
            retrievedCPRID = <xsl:value-of select="$ucPrefix"/>EvidenceMaintenanceFactory.newInstance()
              .getCaseParticipantRoleID(parEvKey).caseParticipantRoleID;
          }

        </xsl:if>
      </xsl:if>
      <!-- END, CR00098559, POB -->
      <!-- END, CR00101870 -->

      <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
        <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@createParticipant='Yes']">
          <xsl:variable name="structType"><xsl:if test="@createParticipantType=$ParticipantTypeEmployer">Employer</xsl:if></xsl:variable>
          <xsl:variable name="relatedParticipantCapName">
            <xsl:choose>
              <xsl:when test="not(@name) or @name=''"/>
              <xsl:otherwise>
                <xsl:call-template name="capitalize">
                  <xsl:with-param name="convertThis" select="@name"/>
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="relatedColumnName" select="@columnName"/>

    // Struct to pass to the validation function
    curam.core.sl.struct.CaseParticipant<xsl:value-of select="$structType"/>Details validate<xsl:value-of select="$relatedParticipantCapName"/>Details =
      new curam.core.sl.struct.CaseParticipant<xsl:value-of select="$structType"/>Details();

    <!-- BEGIN, CR00112535, CD -->
    <xsl:variable name="cpDetails"><xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose></xsl:variable>

    // validate that the create case participant details are correct
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.address = dtls.<xsl:value-of select="$cpDetails"/>.address;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.caseParticipantRoleID = dtls.<xsl:value-of select="$cpDetails"/>.caseParticipantRoleID;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.participantName = dtls.<xsl:value-of select="$cpDetails"/>.participantName;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.participantRoleID = dtls.<xsl:value-of select="$cpDetails"/>.participantRoleID;
    <xsl:if test="@createParticipantType!=$ParticipantTypeEmployer">
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.participantSecondName = dtls.<xsl:value-of select="$cpDetails"/>.participantSecondName;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.ssn = dtls.<xsl:value-of select="$cpDetails"/>.ssn;
    </xsl:if>
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.phoneAreaCode = dtls.<xsl:value-of select="$cpDetails"/>.phoneAreaCode;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.phoneNumber = dtls.<xsl:value-of select="$cpDetails"/>.phoneNumber;

    boolean detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/> =
      validate<xsl:value-of select="$relatedParticipantCapName"/>Details(validate<xsl:value-of select="$relatedParticipantCapName"/>Details, false);
    <!-- END, CR00112535 -->
    if (detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>) {
          <!-- BEGIN, CR00101681, POB -->
          <xsl:choose>
            <xsl:when test="count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relatedColumnName and @notOnEntity='Yes'])&gt;0">dtls.nonEvidenceDetails.<xsl:value-of select="@columnName"/> =</xsl:when><xsl:otherwise>
      dtls.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@columnName"/> =</xsl:otherwise></xsl:choose>
          <!-- END, CR00101681 -->
        get<xsl:value-of select="$relatedParticipantCapName"/>CPRID(validate<xsl:value-of select="$relatedParticipantCapName"/>Details, dtls.<xsl:value-of select="$caseIDKey"/>)
        .caseParticipantRoleID;
    }
      </xsl:for-each>
    </xsl:if>

    EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    // Retrieve the participantRoleID
    CaseParticipantRoleKey caseParticipantRoleKey =
       new CaseParticipantRoleKey();

    ReturnEvidenceDetails createdEvidence = new ReturnEvidenceDetails();

    if (sharingInd) {

      EvidenceDescriptorDtls sharedDescriptorDtls =
        evidenceControllerObj.shareEvidence(
          sourceEvidenceDescriptorDtls,
          targetCase);

      // Return the evidence ID and warnings
      createdEvidence.evidenceKey.evidenceID = sharedDescriptorDtls.relatedID;
      createdEvidence.evidenceKey.evType = sharedDescriptorDtls.evidenceType;

    } else {

      <xsl:choose>
        <xsl:when test="(count(Relationships/Parent)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0)and $casePartRoleIDattr=''">
      if (retrievedCPRID != 0) {
        caseParticipantRoleKey.caseParticipantRoleID = retrievedCPRID;

        CaseParticipantRole caseParticipantRoleObj =
          CaseParticipantRoleFactory.newInstance();

        long participantRoleID = 0;
        if(caseParticipantRoleKey.caseParticipantRoleID != 0) {
          participantRoleID = caseParticipantRoleObj
            .readCaseIDandParticipantID(caseParticipantRoleKey).participantRoleID;
        }
        evidenceDescriptorInsertDtls.participantID = participantRoleID;
      }

        </xsl:when>
        <xsl:when test="$casePartRoleIDattr!=''">
      caseParticipantRoleKey.caseParticipantRoleID =
        dtls.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="$casePartRoleIDattr"/>;

      CaseParticipantRole caseParticipantRoleObj =
        CaseParticipantRoleFactory.newInstance();

      long participantRoleID = 0;
      if(caseParticipantRoleKey.caseParticipantRoleID != 0) {
        participantRoleID = caseParticipantRoleObj
        .readCaseIDandParticipantID(caseParticipantRoleKey).participantRoleID;
      }

      evidenceDescriptorInsertDtls.participantID = participantRoleID;
        </xsl:when>
        <!-- BEGIN, CR00112474, DG -->
        <xsl:otherwise>
      // As there is no participant associated with this evidence we must retrieve
      // the case participant to set the evidence descriptor participant.
      CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = dtls.caseIDKey.caseID;
      evidenceDescriptorInsertDtls.participantID =
        CaseHeaderFactory.newInstance().readCaseParticipantDetails(
          caseHeaderKey).concernRoleID;
        </xsl:otherwise>

      </xsl:choose>
      <!-- END, CR00112474 -->

      <xsl:if test="count($AddressAttributes)>0">
      //
      // Address details
      //

      // If the address is populated
      Address addressObj = AddressFactory.newInstance();
      OtherAddressData otherAddressData;
      EmptyIndStruct addressEmpty;
      AddressDtls addressDtls;
      <xsl:for-each select="$AddressAttributes">
      otherAddressData = new OtherAddressData();
      otherAddressData.addressData = dtls.addressDetails.address;

      addressEmpty = addressObj.isEmpty(otherAddressData);
      addressDtls = new AddressDtls();

      // enter an address if one is provided
      if (!addressEmpty.emptyInd) {

        addressDtls.addressData = dtls.addressDetails.address;
        addressObj.insert(addressDtls);
        dtls.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@name"/> = addressDtls.addressID;

      }
      </xsl:for-each>
    </xsl:if>
      // Evidence descriptor details
      evidenceDescriptorInsertDtls.caseID = dtls.caseIDKey.caseID;
      evidenceDescriptorInsertDtls.evidenceType =
        CASEEVIDENCE.<xsl:value-of select="$EvidenceType"/>;
      evidenceDescriptorInsertDtls.receivedDate =
        dtls.descriptor.receivedDate;
      <!-- BEGIN, CR00207324, GSP -->
       // As the record is created, the change reason should be Initial
       evidenceDescriptorInsertDtls.changeReason = EVIDENCECHANGEREASON.INITIAL;
      <!-- END, CR00207324 -->
        <!-- BEGIN, CR00098559, POB -->
        <xsl:choose>
          <xsl:when test="count(Relationships/MandatoryParents/Parent)&gt;0">
            // Evidence Interface details
            EIMultiParentEvidenceInsertDtls eiMultiParentEvidenceInsertDtls = new EIMultiParentEvidenceInsertDtls();
            eiMultiParentEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
            eiMultiParentEvidenceInsertDtls.evidenceObject =
            dtls.<xsl:value-of select="$evidenceDtls"/>;

            EIEvidenceKey parKey = new EIEvidenceKey();
            <xsl:for-each select="Relationships/MandatoryParents/Parent">

              <xsl:variable name="mParentName" select="@name"/>
              <xsl:variable name="lcMParentName"><xsl:value-of select="translate(substring($mParentName, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring($mParentName, 2)"/></xsl:variable>
              parKey = new EIEvidenceKey();
              parKey.evidenceID = dtls.<xsl:value-of select="$lcMParentName"/>ParEvKey.evidenceID;
              parKey.evidenceType = dtls.<xsl:value-of select="$lcMParentName"/>ParEvKey.evType;

              eiMultiParentEvidenceInsertDtls.parentKeyList.dtls.addRef(parKey);

            </xsl:for-each>
            // Insert the evidence
            EIEvidenceKey eiEvidenceKey =
            evidenceControllerObj.insertEvidenceWithMutipleParents(eiMultiParentEvidenceInsertDtls);
          </xsl:when>
          <xsl:otherwise>
      // Evidence Interface details
      EIEvidenceInsertDtls eiEvidenceInsertDtls = new EIEvidenceInsertDtls();
      eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);<xsl:if test="count(Relationships/Parent)&gt;0">
      eiEvidenceInsertDtls.parentKey.evidenceID =
        dtls.parEvKey.evidenceID;
      eiEvidenceInsertDtls.parentKey.evidenceType =
        dtls.parEvKey.evType;</xsl:if>
      eiEvidenceInsertDtls.evidenceObject =
        dtls.<xsl:value-of select="$evidenceDtls"/>;

      // Insert the evidence
      EIEvidenceKey eiEvidenceKey =
        evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
      <xsl:if test="Relationships/@preAssociation='Yes'">
      // Create the association
      curam.core.sl.struct.AssociationDetails assocDetails = new curam.core.sl.struct.AssociationDetails();

        <xsl:choose >
          <xsl:when test="Relationships/@mulitplePreAssociationID='Yes' and count(Relationships/PreAssociation[@name!='']) &gt; 0">
            <xsl:for-each select="Relationships/PreAssociation[@name!='']">
      assocDetails.evidenceID = eiEvidenceKey.evidenceID;
      assocDetails.evidenceType = eiEvidenceKey.evidenceType;

      assocDetails.parentEvidenceCaseKey.caseIDKey.caseID = dtls.caseIDKey.caseID;

      assocDetails.parentEvidenceCaseKey.evidenceKey.evidenceID = dtls.<xsl:value-of select="@name"/>PreAssocKey.evidenceID;
      assocDetails.parentEvidenceCaseKey.evidenceKey.evType = dtls.<xsl:value-of select="@name"/>PreAssocKey.evType;

      evidenceControllerObj.createAssociation(assocDetails);
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
      assocDetails.evidenceID = eiEvidenceKey.evidenceID;
      assocDetails.evidenceType = eiEvidenceKey.evidenceType;

      assocDetails.parentEvidenceCaseKey.caseIDKey.caseID = dtls.caseIDKey.caseID;

      assocDetails.parentEvidenceCaseKey.evidenceKey.evidenceID = dtls.preAssocKey.evidenceID;
      assocDetails.parentEvidenceCaseKey.evidenceKey.evType = dtls.preAssocKey.evType;

      evidenceControllerObj.createAssociation(assocDetails);
          </xsl:otherwise>
        </xsl:choose>

          </xsl:if>
          </xsl:otherwise>
        </xsl:choose>
        <!-- END, CR00098559, POB -->


      // Return the evidence ID and warnings
      createdEvidence.evidenceKey.evidenceID = eiEvidenceKey.evidenceID;
      createdEvidence.evidenceKey.evType = eiEvidenceKey.evidenceType;
      createdEvidence.warnings = evidenceControllerObj.getWarnings();
    }

    // customisation point
    <!-- BEGIN, 25/02/2008, CD -->
    customise<xsl:value-of select="$EntityName"/>.postCreate(dtls, createdEvidence);


    <!-- END, 25/02/2008, CD -->
    return createdEvidence;
  }


  //___________________________________________________________________________
  /**
   * Modifies a <xsl:value-of select="$capName"/> record.
   *
   * @param details contains <xsl:value-of select="$capName"/> evidence
   * record modification details.
   *
   * @return the modified evidence ID and warnings.
   */
  public ReturnEvidenceDetails modify<xsl:value-of select="$EntityName"/>Evidence
    (<xsl:value-of select="$EvidenceDetails"/> details)
    throws AppException, InformationalException {

    // validate the mandatory fields
    //validateMandatoryDetails(details);

    <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/PreAssociation[@to!=''])&gt;0">
      fillInRelatedDetails(details);
    </xsl:if>

    // customisation point
    <!-- BEGIN, 25/02/2008, CD -->
    Customise<xsl:value-of select="$EntityName"/> customise<xsl:value-of select="$EntityName"/> = Customise<xsl:value-of select="$EntityName"/>Factory.newInstance();
    customise<xsl:value-of select="$EntityName"/>.preModify(details);
    <!-- END, 25/02/2008, CD -->

    // EvidenceController business object
    EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
  <xsl:if test="count(Attributes/Attribute[@type=$typeCaseParticipantRoleID])>1">
    eiEvidenceKey.evidenceID =
      details.<xsl:value-of select="$lcName"/>EvidenceDetails.evidenceID;
    eiEvidenceKey.evidenceType = CASEEVIDENCE.<xsl:value-of select="$EvidenceType"/>;

    // read the record details
    // Retrieve the evidence details
    EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);

    <xsl:value-of select="$ReadEvidenceDetails"/> readEvidenceDetails =
      new <xsl:value-of select="$ReadEvidenceDetails"/>();

    readEvidenceDetails.<xsl:value-of select="$evidenceDtls"/>.assign(
      (<xsl:value-of select="$EntityName"/>Dtls)(eiEvidenceReadDtls.evidenceObject));

    <xsl:for-each select="Attributes/Attribute[@type=$typeCaseParticipantRoleID]">
    // case participant role cannot be changed for this evidence
    if (readEvidenceDetails.<xsl:value-of select="$lcName"/>EvidenceDetails.<xsl:value-of select="@name"/>
      != details.<xsl:value-of select="$lcName"/>EvidenceDetails.<xsl:value-of select="@name"/>) {

      throw new AppException(
        <xsl:value-of select="$generalError"/>.ERR_FV_CASEPARTICIPANT_CHANGE);

    }
    </xsl:for-each>
  </xsl:if>
  <xsl:if test="count($AddressAttributes)>0">
    //
    // Address Details
    //
    Address addressObj =
      AddressFactory.newInstance();
    OtherAddressData otherAddressData;

    EmptyIndStruct addressEmpty;
    AddressDtls addressDtls;
    AddressKey addressKey = new AddressKey();
    <xsl:for-each select="$AddressAttributes">
    otherAddressData = new OtherAddressData();
    otherAddressData.addressData = details.addressDetails.address;

    addressEmpty = addressObj.isEmpty(otherAddressData);

    // if address is provided then record it.
    if (!addressEmpty.emptyInd) {

      addressDtls = new AddressDtls();

      addressDtls.addressData = details.addressDetails.address;

      // If an address record hasn't previously been created
      if (details.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@name"/> == 0) {

        addressObj.insert(addressDtls);

        details.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@name"/> = addressDtls.addressID;

      } else {

        addressKey.addressID = details.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@name"/>;

        // Modify the exist address record
        addressObj.modify(addressKey, addressDtls);

      }
    }
    </xsl:for-each>
  </xsl:if>
    <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
      <!-- BEGIN, CR00101786, CD  -->
      <!-- @modifyParticipant='Yes' covers the optional participant pattern - e.g. CGISS Pregnancy Father -->
      <!-- @modifyParticipant='Many' covers the modifiable participant pattern - e.g. CGISS SpendDown Medical Expense -->
      <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes' or @modifyParticipant='Many']">
        <!-- BEGIN, CR00112535, CD -->
        <xsl:variable name="rpdColName"><xsl:value-of select="@columnName"/></xsl:variable>
        <xsl:variable name="instancesOfField" select="../../UserInterfaceLayer/Cluster/Field[@columnName=$rpdColName]"/>

         <xsl:choose>
           <xsl:when test="count($instancesOfField[@fullCreateParticipant='Yes']) &gt; 0">
    <!-- BEGIN, CR00375230, ELG -->
    boolean alreadyEntered<xsl:value-of select="@columnName"/> = details.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@columnName"/> != 0;

    <xsl:if test="@modifyParticipant='Yes'">
    // if the optional participant was not created on the create screen,
    // creation details may have been entered on the modify screen
    if (!alreadyEntered<xsl:value-of select="@columnName"/>) {
    <!-- END, CR00112535 -->
    <!-- END, CR00375230 -->
    </xsl:if>

      // validate that the create case participant details are correct
      <xsl:variable name="structType"><xsl:if test="@createParticipantType=$ParticipantTypeEmployer">Employer</xsl:if></xsl:variable>
      <xsl:variable name="relatedParticipantCapName">
        <xsl:choose>
          <xsl:when test="not(@name) or @name=''"/>
          <xsl:otherwise>
            <xsl:call-template name="capitalize">
              <xsl:with-param name="convertThis" select="@name"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="validateDetails">validate<xsl:value-of select="$relatedParticipantCapName"/>Details</xsl:variable>
      // Struct to pass to the validation function
      curam.core.sl.struct.CaseParticipant<xsl:value-of select="$structType"/>Details <xsl:value-of select="$validateDetails"/> =
        new curam.core.sl.struct.CaseParticipant<xsl:value-of select="$structType"/>Details();

      <xsl:value-of select="$validateDetails"/>.address = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.address;
      <xsl:value-of select="$validateDetails"/>.caseParticipantRoleID = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.caseParticipantRoleID;
      <xsl:value-of select="$validateDetails"/>.participantName = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.participantName;
      <xsl:value-of select="$validateDetails"/>.participantRoleID = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.participantRoleID;
      <xsl:if test="@createParticipantType!=$ParticipantTypeEmployer">
      <xsl:value-of select="$validateDetails"/>.participantSecondName = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.participantSecondName;
      <xsl:value-of select="$validateDetails"/>.ssn = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.ssn;
      </xsl:if>
      <xsl:value-of select="$validateDetails"/>.phoneAreaCode = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.phoneAreaCode;
      <xsl:value-of select="$validateDetails"/>.phoneNumber = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.phoneNumber;

      <!-- BEGIN, CR00112535, CD -->
      <!-- BEGIN, CR00375230, ELG -->
      boolean detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/> =
        <xsl:value-of select="$validateDetails"/>(<xsl:value-of select="$validateDetails"/>, alreadyEntered<xsl:value-of select="@columnName"/>);
      <!-- END, CR00375230 -->

      if (detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>) {
        details.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@columnName"/> =
          get<xsl:value-of select="$relatedParticipantCapName"/>CPRID(<xsl:value-of select="$validateDetails"/>, details.<xsl:value-of select="$caseIDKey"/>)
          .caseParticipantRoleID;
      }
    <xsl:if test="@modifyParticipant='Yes'">
    }
    </xsl:if>
    <!-- END, CR00101786 -->

          </xsl:when>
          <xsl:otherwise>
    // This is part of the Modifiable Participant Pattern. The <xsl:value-of select="@name"/>
    // participant has one selection mechanism - a CPR drop down. If this is
    // not left empty on the modify screen, update the record.
    if (details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.caseParticipantRoleID != 0) {
      details.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@columnName"/> = details.<xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose>.caseParticipantRoleID;
    }
          </xsl:otherwise>
        </xsl:choose>
    <!-- END, CR00112535 -->
      </xsl:for-each>
    </xsl:if>

    //
    // Call the EvidenceController to modify the evidence
    //

    eiEvidenceKey.evidenceID =
      details.<xsl:value-of select="$evidenceDtls"/>.evidenceID;
    eiEvidenceKey.evidenceType =
      CASEEVIDENCE.<xsl:value-of select="$EvidenceType"/>;

    // Create the evidence Interface modify Dtls and assign the details
    EIEvidenceModifyDtls eiEvidenceModifyDtls = new EIEvidenceModifyDtls();
    eiEvidenceModifyDtls.descriptor.receivedDate =
      details.descriptor.receivedDate;
    eiEvidenceModifyDtls.descriptor.versionNo =
      details.descriptor.versionNo;
    eiEvidenceModifyDtls.descriptor.effectiveFrom =
      details.descriptor.effectiveFrom;
      <!-- BEGIN, CR00113818, POB -->
      <!-- Pass new descriptor field to the evidence controller modify method -->
    eiEvidenceModifyDtls.descriptor.changeReceivedDate =
      details.descriptor.changeReceivedDate;
      <!-- END, CR00113818 -->
      <!-- BEGIN, CR00207324, GSP -->
      <!-- Pass new descriptor field to the evidence controller modify method -->
    eiEvidenceModifyDtls.descriptor.changeReason =
      details.descriptor.changeReason;
      <!-- END, CR00207324 -->
    eiEvidenceModifyDtls.evidenceObject =
      details.<xsl:value-of select="$evidenceDtls"/>;
    evidenceControllerObj.modifyEvidence(
      eiEvidenceKey, eiEvidenceModifyDtls);

    //
    // Return details from the modify operation
    //

    ReturnEvidenceDetails returnEvidenceDetails = new ReturnEvidenceDetails();
    returnEvidenceDetails.evidenceKey.evidenceID = eiEvidenceKey.evidenceID;
    returnEvidenceDetails.evidenceKey.evType = eiEvidenceKey.evidenceType;
    returnEvidenceDetails.warnings = evidenceControllerObj.getWarnings();

    // customisation point
    <!-- BEGIN, 25/02/2008, CD -->
    customise<xsl:value-of select="$EntityName"/>.postModify(details, returnEvidenceDetails);
    <!-- END, 25/02/2008, CD -->
    return returnEvidenceDetails;
  }

    //___________________________________________________________________________
  /**
   * Adds incoming <xsl:value-of select="$capName"/> record to the case.
   *
   * @param details contains <xsl:value-of select="$capName"/> evidence
   * record details.
   *
   * @return the evidence ID and warnings.
   */
  public ReturnEvidenceDetails incomingAddToCase<xsl:value-of select="$EntityName"/>Evidence
    (<xsl:value-of select="$EvidenceDetails"/> details)
    throws AppException, InformationalException {

    <xsl:if test="count(Relationships/Parent)&gt;0">
    if (details.parEvKey.evidenceID == 0 || details.parEvKey.evType.length()==0) {
      // get the selected parent details
      StringList evidenceIDAndTypeStringList =
        StringUtil.delimitedText2StringList(details.selectedParent.tabbedDetails, '|');
      if (evidenceIDAndTypeStringList.size() == 2) {
        details.parEvKey.evidenceID =
          Long.parseLong(evidenceIDAndTypeStringList.item(0));
        details.parEvKey.evType =
          evidenceIDAndTypeStringList.item(1);
      }
    }
    </xsl:if>

    // customisation point
    Customise<xsl:value-of select="$EntityName"/> customise<xsl:value-of select="$EntityName"/> = Customise<xsl:value-of select="$EntityName"/>Factory.newInstance();
    customise<xsl:value-of select="$EntityName"/>.preModify(details);

    curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey = new curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey();
    relatedIDAndEvidenceTypeKey.relatedID = details.<xsl:value-of select="$evidenceDtls"/>.evidenceID;
    relatedIDAndEvidenceTypeKey.evidenceType = CASEEVIDENCE.<xsl:value-of select="$EvidenceType"/>;
    EvidenceDescriptorDtls evidenceDescriptorDtls = EvidenceDescriptorFactory.newInstance().readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
    curam.core.sl.infrastructure.struct.EIEvidenceKeyList parKeyList = new curam.core.sl.infrastructure.struct.EIEvidenceKeyList();
    <xsl:choose>
      <xsl:when test="count(Relationships/MandatoryParents/Parent)&gt;0">
        curam.core.sl.infrastructure.struct.EIEvidenceKey parKey = new curam.core.sl.infrastructure.struct.EIEvidenceKey();
        <xsl:for-each select="Relationships/MandatoryParents/Parent">
          <xsl:variable name="mParentName" select="@name"/>
          <xsl:variable name="lcMParentName"><xsl:value-of select="translate(substring($mParentName, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring($mParentName, 2)"/></xsl:variable>
          parKey = new curam.core.sl.infrastructure.struct.EIEvidenceKey();
          parKey.evidenceID = details.<xsl:value-of select="$lcMParentName"/>ParEvKey.evidenceID;
          parKey.evidenceType = details.<xsl:value-of select="$lcMParentName"/>ParEvKey.evType;
          parKeyList.dtls.add(parKey);
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="count(Relationships/Parent)&gt;0">
          curam.core.sl.infrastructure.struct.EIEvidenceKey parKey = new curam.core.sl.infrastructure.struct.EIEvidenceKey();
          parKey.evidenceID = details.parEvKey.evidenceID;
          parKey.evidenceType = details.parEvKey.evType;
          parKeyList.dtls.add(parKey);
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>

    final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey incoming = new curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey();
    incoming.evidenceDescriptorID = evidenceDescriptorDtls.evidenceDescriptorID;


      <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
        <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@createParticipant='Yes']">
          <xsl:variable name="structType"><xsl:if test="@createParticipantType=$ParticipantTypeEmployer">Employer</xsl:if></xsl:variable>
          <xsl:variable name="relatedParticipantCapName">
            <xsl:choose>
              <xsl:when test="not(@name) or @name=''"/>
              <xsl:otherwise>
                <xsl:call-template name="capitalize">
                  <xsl:with-param name="convertThis" select="@name"/>
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="relatedColumnName" select="@columnName"/>

    // Struct to pass to the validation function
    curam.core.sl.struct.CaseParticipant<xsl:value-of select="$structType"/>Details validate<xsl:value-of select="$relatedParticipantCapName"/>Details =
      new curam.core.sl.struct.CaseParticipant<xsl:value-of select="$structType"/>Details();

    <xsl:variable name="cpDetails"><xsl:choose><xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when><xsl:otherwise>caseParticipantDetails</xsl:otherwise></xsl:choose></xsl:variable>

    // validate that the create case participant details are correct
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.address = details.<xsl:value-of select="$cpDetails"/>.address;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.caseParticipantRoleID = details.<xsl:value-of select="$cpDetails"/>.caseParticipantRoleID;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.participantName = details.<xsl:value-of select="$cpDetails"/>.participantName;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.participantRoleID = details.<xsl:value-of select="$cpDetails"/>.participantRoleID;
    <xsl:if test="@createParticipantType!=$ParticipantTypeEmployer">
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.participantSecondName = details.<xsl:value-of select="$cpDetails"/>.participantSecondName;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.ssn = details.<xsl:value-of select="$cpDetails"/>.ssn;
    </xsl:if>
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.phoneAreaCode = details.<xsl:value-of select="$cpDetails"/>.phoneAreaCode;
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details.phoneNumber = details.<xsl:value-of select="$cpDetails"/>.phoneNumber;

    boolean detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/> =
      validate<xsl:value-of select="$relatedParticipantCapName"/>Details(validate<xsl:value-of select="$relatedParticipantCapName"/>Details, true);
    if (detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>) {
          <xsl:choose>
            <xsl:when test="count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relatedColumnName and @notOnEntity='Yes'])&gt;0">details.nonEvidenceDetails.<xsl:value-of select="@columnName"/> =</xsl:when><xsl:otherwise>
      details.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@columnName"/> =</xsl:otherwise></xsl:choose>
        get<xsl:value-of select="$relatedParticipantCapName"/>CPRID(validate<xsl:value-of select="$relatedParticipantCapName"/>Details, details.<xsl:value-of select="$caseIDKey"/>)
        .caseParticipantRoleID;
    }
      </xsl:for-each>
    </xsl:if>

    for(curam.core.sl.infrastructure.struct.EIEvidenceKey currentParKey : parKeyList.dtls ) {

      relatedIDAndEvidenceTypeKey = new curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey();
      relatedIDAndEvidenceTypeKey.relatedID = currentParKey.evidenceID;
      relatedIDAndEvidenceTypeKey.evidenceType = currentParKey.evidenceType;
      EvidenceDescriptorDtls parentEvidenceDescriptorDtls = EvidenceDescriptorFactory.newInstance().readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
      final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey parent = new curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey();
      parent.evidenceDescriptorID = parentEvidenceDescriptorDtls.evidenceDescriptorID;
      new curam.aes.sl.impl.AESDataIntegrityCheck().validateObjectLevelDataIntegrity(incoming, parent);
    }

    final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList evidenceDescriptorsAdded =
      evidenceMaintenanceHandler.addToCaseInBulk(details.dtls, evidenceDescriptorDtls, parKeyList);

    // customisation point
    ReturnEvidenceDetails returnEvidenceDetails = new ReturnEvidenceDetails();
    returnEvidenceDetails.evidenceKey.evidenceID = evidenceDescriptorsAdded.dtls.get(0).relatedID;
    returnEvidenceDetails.evidenceKey.evType = evidenceDescriptorsAdded.dtls.get(0).evidenceType;
    customise<xsl:value-of select="$EntityName"/>.postModify(details, returnEvidenceDetails);

    incomingEvidenceUtil.checkIncomingBusinessObjectAndRemoveIfNeeded(
        evidenceDescriptorDtls.successionID);

    return returnEvidenceDetails;
  }


  // ___________________________________________________________________________
  /**
   * Performs an Incoming Evidence Modify. Method will perform different actions
   * depends on the data stored in the wizard. i.e Update With Incoming vs
   * Retain Existing, New Version vs Correction.
   *
   * <P>
   * Update with Incoming - New Version will take the incoming evidence and add
   * it into the succession set of the selected existing evidence using an
   * effective date selected by the caseworker.
   * </P>
   *
   * <P>
   * Update with Incoming - Correction will use the values stored on the
   * incoming evidence to correct the existing evidence.
   * </P>
   *
   * @param wizardKey Wizard Persisted State ID
   * @param dtls Evidence dtls object
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ReturnEvidenceDetails incomingModify<xsl:value-of select="$EntityName"/>Evidence(
    curam.guidedchanges.facade.struct.WizardKey wizardKey, <xsl:value-of select="$EntityName"/>EvidenceDetails dtls)
    throws AppException, InformationalException {


    <xsl:if test="count(Relationships/Parent)&gt;0">
    if (dtls.parEvKey.evidenceID == 0 || dtls.parEvKey.evType.length()==0) {
      // get the selected parent details
      StringList evidenceIDAndTypeStringList =
        StringUtil.delimitedText2StringList(dtls.selectedParent.tabbedDetails, '|');
      if (evidenceIDAndTypeStringList.size() == 2) {
        dtls.parEvKey.evidenceID =
          Long.parseLong(evidenceIDAndTypeStringList.item(0));
        dtls.parEvKey.evType =
          evidenceIDAndTypeStringList.item(1);
      }
    }
    </xsl:if>

    Customise<xsl:value-of select="$EntityName"/> customise<xsl:value-of select="$EntityName"/> = Customise<xsl:value-of select="$EntityName"/>Factory.newInstance();
    customise<xsl:value-of select="$EntityName"/>.preModify(dtls);

    final curam.aes.facade.struct.IncomingModifyWizardStoreDetails store = (curam.aes.facade.struct.IncomingModifyWizardStoreDetails) new curam.wizardpersistence.impl.WizardPersistentState()
        .read(wizardKey.wizardStateID);

    final EvidenceDescriptorDtls incomingEvidenceDescriptorDtls = incomingEvidenceUtil
        .readEvidenceDescriptorDtls(store.incomingEvidenceDescriptorID);

     final EvidenceDescriptorDtls existingEvidenceDescriptorDtls = incomingEvidenceUtil
        .readEvidenceDescriptorDtls(store.existingEvidenceDescriptorID);

    final Date effectiveFrom = dtls.descriptor.effectiveFrom;
    final String changeReason = dtls.descriptor.changeReason;
    final Date recievedDate = dtls.descriptor.receivedDate;
    <xsl:if test="BusinessDates/@startDate!=''">

    // get the earliest start date between the incoming evidence, existing
    // evidence and the user entered start date
    final Date earliestDate = getEarliestStartDate(dtls,
      existingEvidenceDescriptorDtls, incomingEvidenceDescriptorDtls);
    </xsl:if>

    // validate descriptor effective from date does not clash if its changing
    if (!existingEvidenceDescriptorDtls.effectiveFrom.equals(effectiveFrom)) {
      incomingEvidenceUtil.validateEffectiveDateChange(
          existingEvidenceDescriptorDtls.evidenceDescriptorID, effectiveFrom);
    }

    final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey incoming = new curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey();
    incoming.evidenceDescriptorID = incomingEvidenceDescriptorDtls.evidenceDescriptorID;
    final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey existing = new curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey();
    existing.evidenceDescriptorID = existingEvidenceDescriptorDtls.evidenceDescriptorID;
    new curam.aes.sl.impl.AESDataIntegrityCheck().validateRecordLevelDataIntegrity(incoming, existing);

    EvidenceDescriptorDtls newEvidenceDescriptor = new EvidenceDescriptorDtls();
    newEvidenceDescriptor.assign(existingEvidenceDescriptorDtls);
    newEvidenceDescriptor.effectiveFrom = effectiveFrom;
    newEvidenceDescriptor.changeReason = changeReason;
    newEvidenceDescriptor.receivedDate = recievedDate;
    newEvidenceDescriptor.sourceCaseID = incomingEvidenceDescriptorDtls.sourceCaseID;

    if (curam.aes.codetable.AESINCOMINGMODIFYTYPE.UPDATEWITHINCOMING.equals(store.type)) {

      if (store.newVersion) {
        // Succesion
        newEvidenceDescriptor.correctionSetID = "";
        incomingEvidenceUtil.updateWithIncoming(existingEvidenceDescriptorDtls,
            newEvidenceDescriptor, dtls.dtls);
      } else if (store.correction) {
        // Correction
        incomingEvidenceUtil.updateWithIncoming(existingEvidenceDescriptorDtls,
            newEvidenceDescriptor, dtls.dtls);
      }

      incomingEvidenceUtil.linkRecords(incomingEvidenceDescriptorDtls, newEvidenceDescriptor);

      incomingEvidenceUtil.updateVDIEDLink(newEvidenceDescriptor, incomingEvidenceDescriptorDtls);

      final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey incomingEvidenceDescriptorKey = new curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey();
      incomingEvidenceDescriptorKey.evidenceDescriptorID = store.incomingEvidenceDescriptorID;
      curam.aes.facade.fact.IncomingEvidenceFactory.newInstance()
          .dismiss(incomingEvidenceDescriptorKey);

    } else if (curam.aes.codetable.AESINCOMINGMODIFYTYPE.RETAINEXISTING.equals(store.type)) {

      curam.core.sl.infrastructure.struct.EIEvidenceKeyList parKeyList = new curam.core.sl.infrastructure.struct.EIEvidenceKeyList();

      existingEvidenceDescriptorDtls.effectiveFrom = effectiveFrom;
      <xsl:if test="BusinessDates/@startDate!='' and BusinessDates/@startDateNotOnEntity=''">
      // If the incoming start date is not the earliest then we need to
      // update the incoming succession with the earliest start date
      if (!earliestDate.isZero()) {
	      updateSuccessionSetWithEarliestStartDate(earliestDate,
	        incomingEvidenceDescriptorDtls, store.correction);

	      // If the existing start date is not the earliest then we need to
	      // update the existing succession with the earliest start date
	      updateSuccessionSetWithEarliestStartDate(earliestDate,
	        existingEvidenceDescriptorDtls, store.correction);

	      // update the start date details if it is not set to the earliest
	      // available one
	      if (!dtls.dtls.<xsl:value-of select="BusinessDates/@startDate"/>.equals(earliestDate)) {
	        dtls.dtls.<xsl:value-of select="BusinessDates/@startDate"/> = earliestDate;
	      }
      }
      </xsl:if>

      <xsl:if test="BusinessDates/@startDate!='' and BusinessDates/@startDateNotOnEntity='true'">
      // If the incoming start date is not the earliest then we need to
      // update the incoming succession with the earliest start date
      if (!earliestDate.isZero()) {
	      updateSuccessionSetWithEarliestStartDate(earliestDate,
	        incomingEvidenceDescriptorDtls, store.correction);

	      // If the existing start date is not the earliest then we need to
	      // update the existing succession with the earliest start date
	      updateSuccessionSetWithEarliestStartDate(earliestDate,
	        existingEvidenceDescriptorDtls, store.correction);

	      // update the start date details if it is not set to the earliest
	      // available one
	      if (!dtls.nonEvidenceDetails.<xsl:value-of select="BusinessDates/@startDate"/>.equals(earliestDate)) {
	        dtls.nonEvidenceDetails.<xsl:value-of select="BusinessDates/@startDate"/> = earliestDate;
	      }
      }
      </xsl:if>

      if (store.newVersion) {

      incomingEvidenceUtil.validateRetainExistingNewVersionTimelineIntegrity(
            store.existingEvidenceDescriptorID,
            store.incomingEvidenceDescriptorID, effectiveFrom);

        evidenceMaintenanceHandler.retainExistingEvidenceNewVersion(
            dtls.dtls, incomingEvidenceDescriptorDtls,
            parKeyList,
            newEvidenceDescriptor);

      } else if (store.correction) {

      incomingEvidenceUtil.validateRetainExistingCorrectionTimelineIntegrity(
            store.existingEvidenceDescriptorID,
            store.incomingEvidenceDescriptorID, effectiveFrom);

        evidenceMaintenanceHandler.retainExistingEvidenceCorrection(
            dtls.dtls, incomingEvidenceDescriptorDtls,
            parKeyList,
            newEvidenceDescriptor);
      }
    }

    // customisation point
    ReturnEvidenceDetails returnEvidenceDetails = new ReturnEvidenceDetails();
    returnEvidenceDetails.evidenceKey.evidenceID = newEvidenceDescriptor.relatedID;
    returnEvidenceDetails.evidenceKey.evType = newEvidenceDescriptor.evidenceType;
    customise<xsl:value-of select="$EntityName"/>.postModify(dtls, returnEvidenceDetails);

    incomingEvidenceUtil.checkIncomingBusinessObjectAndRemoveIfNeeded(
        incomingEvidenceDescriptorDtls.successionID);

    new curam.wizardpersistence.impl.WizardPersistentState().remove(wizardKey.wizardStateID);

    return new ReturnEvidenceDetails();
  }

  <xsl:if test="BusinessDates/@startDate!='' and BusinessDates/@startDateNotOnEntity=''">
   /**
   * Compare the incoming, user entered and existing start dates to get the
   * earliest one.
   *
   * @param dataStruct
   * @param existingEvidenceDescriptorDtls
   * @param incomingEvidenceDescriptorDtls
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Date getEarliestStartDate(<xsl:value-of select="$EntityName"/>EvidenceDetails dtls,
    final EvidenceDescriptorDtls existingEvidenceDescriptorDtls,
    final EvidenceDescriptorDtls incomingEvidenceDescriptorDtls)
    throws AppException, InformationalException {

    // get the earliest incoming start date
    final EvidenceInterface evidenceInterface =
      util.getEvidenceInterface(incomingEvidenceDescriptorDtls.evidenceType);
    final SuccessionID successionID = new SuccessionID();
    successionID.successionID = incomingEvidenceDescriptorDtls.successionID;
    final EvidenceDescriptorDtlsList incomingEvidenceDescriptorDtlsList =
      EvidenceDescriptorFactory.newInstance()
        .searchBySuccessionID(successionID);

    Date earliestIncomingStartDate = new Date();
    for (final EvidenceDescriptorDtls evidenceDescriptor : incomingEvidenceDescriptorDtlsList.dtls
      .items()) {

      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptor.relatedID;
      evidenceKey.evidenceType = evidenceDescriptor.evidenceType;

      final Date startDate = evidenceInterface.getStartDate(evidenceKey);
      if (startDate != null &amp;&amp; !startDate.isZero() &amp;&amp; (earliestIncomingStartDate.isZero()
        || startDate.before(earliestIncomingStartDate))) {
        earliestIncomingStartDate = startDate;
      }
    }

    // get the earliest existing start date
    final SuccessionID successionKey = new SuccessionID();
    successionKey.successionID = existingEvidenceDescriptorDtls.successionID;

    final EvidenceDescriptorDtlsList existingEvidenceDescriptorDtlsList =
      EvidenceDescriptorFactory.newInstance()
        .searchBySuccessionID(successionKey);

    Date earliestExistingStartDate = new Date();

    for (final EvidenceDescriptorDtls evidenceDescriptor : existingEvidenceDescriptorDtlsList.dtls
      .items()) {

      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptor.relatedID;
      evidenceKey.evidenceType = evidenceDescriptor.evidenceType;

      final Date startDate = evidenceInterface.getStartDate(evidenceKey);
      if (startDate != null &amp;&amp; !startDate.isZero() &amp;&amp; (earliestExistingStartDate.isZero()
        || startDate.before(earliestExistingStartDate))) {
        earliestExistingStartDate = startDate;
      }
    }

    // get the entered start date
    final Date enteredStartDate = dtls.dtls.<xsl:value-of select="BusinessDates/@startDate"/>;

    // get the earliest date between the incoming, existing and user entered
    final SortedSet&lt;Date&gt; dates = new TreeSet&lt;Date&gt;();
    dates.add(earliestIncomingStartDate);
    dates.add(earliestExistingStartDate);
    dates.add(enteredStartDate);
    final Date earliestDate = dates.first();
    return earliestDate;
  }
  </xsl:if>

  <xsl:if test="BusinessDates/@startDate!='' and BusinessDates/@startDateNotOnEntity='true'">
   /**
   * Compare the incoming, user entered and existing start dates to get the
   * earliest one.
   *
   * @param dataStruct
   * @param existingEvidenceDescriptorDtls
   * @param incomingEvidenceDescriptorDtls
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Date getEarliestStartDate(<xsl:value-of select="$EntityName"/>EvidenceDetails dtls,
    final EvidenceDescriptorDtls existingEvidenceDescriptorDtls,
    final EvidenceDescriptorDtls incomingEvidenceDescriptorDtls)
    throws AppException, InformationalException {

    // get the earliest incoming start date
    final EvidenceInterface evidenceInterface =
      util.getEvidenceInterface(incomingEvidenceDescriptorDtls.evidenceType);
    final SuccessionID successionID = new SuccessionID();
    successionID.successionID = incomingEvidenceDescriptorDtls.successionID;
    final EvidenceDescriptorDtlsList incomingEvidenceDescriptorDtlsList =
      EvidenceDescriptorFactory.newInstance()
        .searchBySuccessionID(successionID);

    Date earliestIncomingStartDate = new Date();
    for (final EvidenceDescriptorDtls evidenceDescriptor : incomingEvidenceDescriptorDtlsList.dtls
      .items()) {

      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptor.relatedID;
      evidenceKey.evidenceType = evidenceDescriptor.evidenceType;

      final Date startDate = evidenceInterface.getStartDate(evidenceKey);
      if (!startDate.isZero() &amp;&amp; (earliestIncomingStartDate.isZero()
        || startDate.before(earliestIncomingStartDate))) {
        earliestIncomingStartDate = startDate;
      }
    }

    // get the earliest existing start date
    final SuccessionIDAndStatus successionKey = new SuccessionIDAndStatus();
    successionKey.successionID = existingEvidenceDescriptorDtls.successionID;
    successionKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptorDtlsList existingEvidenceDescriptorDtlsList =
      EvidenceDescriptorFactory.newInstance()
        .searchActiveBySuccessionID(successionKey);

    Date earliestExistingStartDate = new Date();

    for (final EvidenceDescriptorDtls evidenceDescriptor : existingEvidenceDescriptorDtlsList.dtls
      .items()) {

      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptor.relatedID;
      evidenceKey.evidenceType = evidenceDescriptor.evidenceType;

      final Date startDate = evidenceInterface.getStartDate(evidenceKey);
      if (!startDate.isZero() &amp;&amp; (earliestExistingStartDate.isZero()
        || startDate.before(earliestExistingStartDate))) {
        earliestExistingStartDate = startDate;
      }
    }

    // get the entered start date
    final Date enteredStartDate = dtls.nonEvidenceDetails.<xsl:value-of select="BusinessDates/@startDate"/>;

    // get the earliest date between the incoming, existing and user entered
    final SortedSet&lt;Date&gt; dates = new TreeSet&lt;Date&gt;();
    dates.add(earliestIncomingStartDate);
    dates.add(earliestExistingStartDate);
    dates.add(enteredStartDate);
    final Date earliestDate = dates.first();
    return earliestDate;
  }
  </xsl:if>

  <xsl:if test="BusinessDates/@startDate!=''">
  /**
   * If the start date for the succession set is not the earliest then we need
   * to update the succession set with the earliest start date.
   *
   * @param earliestStartDate
   * @param evidenceDescriptorDtls
   * @throws AppException
   * @throws InformationalException
   */
  private void updateSuccessionSetWithEarliestStartDate(
    final Date earliestStartDate,
    final EvidenceDescriptorDtls evidenceDescriptorDtls, final boolean correctionInd)
    throws AppException, InformationalException {

    final EvidenceInterface evidenceInterface =
      util.getEvidenceInterface(evidenceDescriptorDtls.evidenceType);
    final SuccessionID successionKey = new SuccessionID();
    successionKey.successionID = evidenceDescriptorDtls.successionID;

    final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
      EvidenceDescriptorFactory.newInstance()
        .searchBySuccessionID(successionKey);

    for (final EvidenceDescriptorDtls evidenceDescriptor : evidenceDescriptorDtlsList.dtls
      .items()) {

	  if (correctionInd
        &amp;&amp; evidenceDescriptor.evidenceDescriptorID == evidenceDescriptorDtls.evidenceDescriptorID) {
        // no need to update the current record if it is being corrected
        continue;
      }
      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptor.relatedID;
      evidenceKey.evidenceType = evidenceDescriptor.evidenceType;

      final Object evObject = util.getEvidenceObject(evidenceKey.evidenceType,
        evidenceKey.evidenceID);

      final Date startDate = evidenceInterface.getStartDate(evidenceKey);

      if (!earliestStartDate.equals(startDate)
        &amp;&amp; earliestStartDate.before(startDate)) {

        evidenceInterface.setStartDate(evObject, earliestStartDate);
        evidenceInterface.modifyEvidence(evidenceKey, evObject);
      }
    }
  }
  </xsl:if>

  //___________________________________________________________________________
  /**
   * Reads a <xsl:value-of select="$capName"/> record.
   *
   * @param key contains ID of record to read.
   *
   * @return <xsl:value-of select="$capName"/> evidence details read.
   */
  public <xsl:value-of select="$ReadEvidenceDetails"/>
    read<xsl:value-of select="$EntityName"/>Evidence(EvidenceCaseKey key)
    throws AppException, InformationalException {

    // customisation point
    <!-- BEGIN, 25/02/2008, CD -->
    Customise<xsl:value-of select="$EntityName"/> customise<xsl:value-of select="$EntityName"/> = Customise<xsl:value-of select="$EntityName"/>Factory.newInstance();
    customise<xsl:value-of select="$EntityName"/>.preRead(key);
    <!-- END, 25/02/2008, CD -->


    // EvidenceController business object
    EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface)EvidenceControllerFactory.newInstance();

    EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = key.evidenceKey.evidenceID;
    eiEvidenceKey.evidenceType =
      CASEEVIDENCE.<xsl:value-of select="$EvidenceType"/>;

    // Retrieve the evidence details
    EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);

    // Retrieve the evidence descriptor details
    EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();

    EvidenceDescriptorKey evidenceDescriptorKey = new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID =
      eiEvidenceReadDtls.descriptor.evidenceDescriptorID;

    EvidenceDescriptorDtls evidenceDescriptorDtls =
      evidenceDescriptorObj.read(evidenceDescriptorKey);

    //
    // Return the evidence
    //

    <xsl:value-of select="$ReadEvidenceDetails"/> readEvidenceDetails =
      new <xsl:value-of select="$ReadEvidenceDetails"/>();
    readEvidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    <!-- BEGIN, CR00101613, POB -->
    readEvidenceDetails.descriptor.approvalRequestStatus =
      eiEvidenceReadDtls.descriptor.approvalRequestStatus;
    <!-- END, CR00101613 -->

    <!-- handle read of parent details for children in Associations -->
    <xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@to!='']) > 0">
    <xsl:variable name="toEntity"><xsl:for-each select="Relationships/Association[@to!='']"><xsl:value-of select="@to"/></xsl:for-each></xsl:variable>
    // EvidenceRelationship Link entity
    curam.core.sl.infrastructure.entity.intf.EvidenceRelationship
      evidenceRelationshipObj =
        curam.core.sl.infrastructure.entity.fact.EvidenceRelationshipFactory.newInstance();

    curam.core.sl.infrastructure.entity.struct.ParentKey parentKey = new
      curam.core.sl.infrastructure.entity.struct.ParentKey();

    curam.core.sl.infrastructure.entity.struct.ChildKeyList childKeyList;

    parentKey.parentID = key.evidenceKey.evidenceID;
    parentKey.parentType = key.evidenceKey.evType;

    childKeyList = evidenceRelationshipObj.searchByParent(parentKey);

    // Check if the record has a association to a specified entity type
    for(int i = 0; i &lt; childKeyList.dtls.size(); i++) {

      if(childKeyList.dtls.item(i).childType.equals(CASEEVIDENCE.<xsl:value-of select="translate($toEntity, $lcletters, $ucletters)"/>)) {

        readEvidenceDetails.associatedEvidenceDetails.evidenceKey.evidenceID = childKeyList.dtls.item(i).childID;
        readEvidenceDetails.associatedEvidenceDetails.evidenceKey.evType = childKeyList.dtls.item(i).childType;
        break;
      }
    }

    if(readEvidenceDetails.associatedEvidenceDetails.evidenceKey.evidenceID == 0) {
      readEvidenceDetails.associatedEvidenceDetails.linkText = "Add";
    } else {
      readEvidenceDetails.associatedEvidenceDetails.linkText = "View";
    }
    </xsl:if>
      <xsl:choose>
        <xsl:when test="count(Relationships/Association[@to!='' and @displayInHierarchy='Yes'])>0">
    if(readEvidenceDetails.associatedEvidenceDetails.evidenceKey.evidenceID != 0) {
      readEvidenceDetails.associatedEvidenceDetails.linkText = "View";
    }
        </xsl:when>
        <xsl:otherwise>

        </xsl:otherwise>
      </xsl:choose>
    readEvidenceDetails.descriptor.updatedBy =
      eiEvidenceReadDtls.descriptor.updatedBy;
    readEvidenceDetails.descriptor.updatedDateTime =
      eiEvidenceReadDtls.descriptor.updatedDateTime;

    // assign the evidence to the return object
    readEvidenceDetails.<xsl:value-of select="$evidenceDtls"/>.assign(
      (<xsl:value-of select="$ProductEvidenceDtls"/>)(eiEvidenceReadDtls.evidenceObject));
    <xsl:if test="count($AddressAttributes)>0">
    //
    // Read an address if it has been recorded
    //
    Address addressObj = AddressFactory.newInstance();
    AddressKey addressKey = new AddressKey();

      <xsl:for-each select="$AddressAttributes">
    if (readEvidenceDetails.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@name"/> != 0) {

      addressKey.addressID =
        readEvidenceDetails.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@name"/>;

      // Read the address
      OtherAddressData otherAddressData =
        addressObj.readAddressData(addressKey);

      // Populate the return object with the address data
      readEvidenceDetails.addressDetails.address =
        otherAddressData.addressData;

    }
      </xsl:for-each>
    </xsl:if>

      <!-- BEGIN, CR00099582, POB -->
      <xsl:if test="count(Relationships/MandatoryParents/Parent) &gt; 0">

    EvidenceRelationship evidenceRelationshipObj =
    EvidenceRelationshipFactory.newInstance();

    ChildKey childKey = new ChildKey();
    childKey.childID = key.evidenceKey.evidenceID;
    childKey.childType = key.evidenceKey.evType;

    ParentKeyList parentKeyList =
      evidenceRelationshipObj.searchByChild(childKey);


    for(int i = 0; i &lt; parentKeyList.dtls.size(); i++){

      eiEvidenceKey.evidenceID = parentKeyList.dtls.item(i).parentID;
      eiEvidenceKey.evidenceType = parentKeyList.dtls.item(i).parentType;

      EIFieldsForListDisplayDtls eiFieldsForListDisplayDtls =
      evidenceControllerObj.getEvidenceSummaryDetails(eiEvidenceKey);

        <xsl:for-each select="Relationships/MandatoryParents/Parent">

          <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>

        if(parentKeyList.dtls.item(i).parentType.equals(CASEEVIDENCE.<xsl:call-template name="toUpper"><xsl:with-param name="convertThis" select="@name"></xsl:with-param></xsl:call-template>)){

          readEvidenceDetails.<xsl:value-of select="$lcParentName"/>ParentEvidenceLink.parentID =
          eiEvidenceKey.evidenceID;
          readEvidenceDetails.<xsl:value-of select="$lcParentName"/>ParentEvidenceLink.parentType =
          eiEvidenceKey.evidenceType;
          readEvidenceDetails.<xsl:value-of select="$lcParentName"/>ParentEvidenceLink.displayText =
          eiFieldsForListDisplayDtls.summary;

          continue;
        }
        </xsl:for-each>
    }
      </xsl:if>
      <!-- END, CR00099582 -->
    <!-- <xsl:if test="count(ServiceLayer/RelatedParticipantDetails)>0">
    CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();

    // Concatenate the Name and alternateID for the participants in the list
    ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
      new ConcatenateNameAndAlternateIDSSNKey();
    ConcatenateNameAndAlternateIDSSNDetails
      concatenateNameAndAlternateIDSSNDetails =
      new ConcatenateNameAndAlternateIDSSNDetails();

    concatenateNameAndAlternateIDSSNKey.type =
      CONCERNROLEALTERNATEID.getDefaultCode();
    concatenateNameAndAlternateIDSSNKey.status =
      RECORDSTATUS.NORMAL;

    concatenateNameAndAlternateIDSSNKey.caseID = key.caseIDKey.caseID;
      <xsl:for-each select="ServiceLayer/RelatedParticipantDetails">
    if (readEvidenceDetails.<xsl:value-of select="$evidenceDtls"/>
        .<xsl:value-of select="@columnName"/> != 0) {

      caseParticipantRoleKey.caseParticipantRoleID = readEvidenceDetails
        .<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@columnName"/>;

      concatenateNameAndAlternateIDSSNKey.name =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readFullName(caseParticipantRoleKey).fullName;
      concatenateNameAndAlternateIDSSNKey.caseParticipantRoleID =
        readEvidenceDetails.<xsl:value-of select="$evidenceDtls"/>
        .<xsl:value-of select="@columnName"/>;
      concatenateNameAndAlternateIDSSNDetails =
        GeneralUtilityFactory.newInstance()
        .concatenateNameAndAlternateIDSSN(concatenateNameAndAlternateIDSSNKey);
      <xsl:variable name="readParticipantNameDetails">
        <xsl:choose>
          <xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when>
          <xsl:otherwise>caseParticipantDetails</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      readEvidenceDetails.<xsl:value-of select="$readParticipantNameDetails"/>
        .caseParticipantName =
        concatenateNameAndAlternateIDSSNDetails.nameAndAlternateID;

    }
      </xsl:for-each>
    </xsl:if>-->
    <!-- CDuffy 15/02/2008 - readRelatedAttributes is no longer valid metadata
    <xsl:if test="Relationships/Parent/@readRelatedAttributes='Yes'">
    //
    // Read Related Parent Evidence Details
    //

    // Create the link entity object
    EvidenceRelationship evidenceRelationshipObj =
      EvidenceRelationshipFactory.newInstance();

    EIEvidenceKey childKey = new EIEvidenceKey();
    childKey.evidenceID = key.evidenceKey.evidenceID;
    childKey.evidenceType = key.evidenceKey.evType;

    ParentList parentList = evidenceRelationshipObj.getParentKeyList(childKey);

    RelatedAttributeKey relatedKey = new RelatedAttributeKey();
    relatedKey.caseID = key.caseIDKey.caseID;
    relatedKey.parentEvidenceID = parentList.list.dtls.item(0).parentID;
    </xsl:if>
    -->
    <xsl:if test="Relationships/@relatedEntityAttributes='Yes'">
    // Get all related entity attributes

    // Instantiate <xsl:value-of select="$EntityName"/>RelatedEntityAttributes object

    <xsl:value-of select="$EntityName"/>RelatedEntityAttributes getRelatedEntityAttributesObj =
      <xsl:value-of select="$EntityName"/>RelatedEntityAttributesFactory.newInstance();

    <xsl:choose>
      <xsl:when test="Relationships/@preAssociation='Yes' and Relationships/@mulitplePreAssociationID='Yes'">
    curam.core.sl.struct.MultiplePreAssocCaseEvKey preAssocKey = new
    curam.core.sl.struct.MultiplePreAssocCaseEvKey();

    preAssocKey.evCaseKey.caseIDKey.caseID = key.caseIDKey.caseID;
    preAssocKey.evCaseKey.evidenceKey.evidenceID = key.evidenceKey.evidenceID;
    preAssocKey.evCaseKey.evidenceKey.evType = key.evidenceKey.evType;

    readEvidenceDetails.relatedEntityAttributes = getRelatedEntityAttributesObj.getRelatedEntityAttributes(preAssocKey);
      </xsl:when>
      <xsl:when test="Relationships/@preAssociation='Yes'">
    curam.core.sl.struct.PreAssocCaseEvKey preAssocKey = new
    curam.core.sl.struct.PreAssocCaseEvKey();

    preAssocKey.evCaseKey.caseIDKey.caseID = key.caseIDKey.caseID;
    preAssocKey.evCaseKey.evidenceKey.evidenceID = key.evidenceKey.evidenceID;
    preAssocKey.evCaseKey.evidenceKey.evType = key.evidenceKey.evType;

    readEvidenceDetails.relatedEntityAttributes = getRelatedEntityAttributesObj.getRelatedEntityAttributes(preAssocKey);
      </xsl:when>
      <xsl:otherwise>
    readEvidenceDetails.relatedEntityAttributes = getRelatedEntityAttributesObj.getRelatedEntityAttributes(key);
      </xsl:otherwise>
    </xsl:choose>
    </xsl:if>
      <!-- BEGIN, CR00100657, POB -->
      <xsl:if test="$modifiable='No'">
      InformationalManager infoManager = TransactionInfo.getInformationalManager();

      String locale = curam.util.transaction.TransactionInfo.getProgramLocale();
      String exceptionArgumentStr;

      AppException appException = new AppException(
      curam.message.ENTITYGEN<xsl:value-of select="$ucName"/>VALIDATION.PAGE_NOT_MODIFIABLE);

      if(locale == null){
      exceptionArgumentStr =
      curam.message.ENTITYGEN<xsl:value-of select="$ucName"/>VALIDATION.ENTITY_DISPLAY_NAME.getMessageText();
      }else{
      exceptionArgumentStr =
      curam.message.ENTITYGEN<xsl:value-of select="$ucName"/>VALIDATION.ENTITY_DISPLAY_NAME.getMessageText(locale);
      }

      appException.arg(exceptionArgumentStr);

      infoManager.addInformationalMsg(appException,
      CuramConst.gkEmpty,  InformationalElement.InformationalType.kWarning);

      //  Retrieve list of informational messages
      String[] messages = infoManager.obtainInformationalAsString();

      // Return the Informational messages
      for (int i = 0; i &lt; messages.length; i++) {

      ECWarningsDtls ecWarningsDtls = new ECWarningsDtls();

      ecWarningsDtls.msg = messages[i];
      readEvidenceDetails.warnings.dtls.addRef(ecWarningsDtls);
      }
      </xsl:if>

    <!-- BEGIN, CR00119218, CD -->
    <xsl:if test="count(ServiceLayer/RelatedParticipantDetails)>0">
    CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();

    MaintainConcernRoleDetails maintainConcernRoleDetailsObj =
      MaintainConcernRoleDetailsFactory.newInstance();
    CaseParticipantRole caseParticipantRoleObj =
      CaseParticipantRoleFactory.newInstance();
    ParticipantRoleIDAndNameDetails participantRoleIDAndName;
    ReadConcernRoleKey readConcernRoleKey;
    ReadConcernRoleDetails readConcernRole;
    ConcernRolesDetails concernRolesDetails;

    <xsl:if test="//EvidenceEntities/@displayAltID='Yes'">
    // Concatenate the Name and alternateID for the participants in the list
    ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
      new ConcatenateNameAndAlternateIDSSNKey();
    ConcatenateNameAndAlternateIDSSNDetails
      concatenateNameAndAlternateIDSSNDetails =
      new ConcatenateNameAndAlternateIDSSNDetails();

    concatenateNameAndAlternateIDSSNKey.type =
      CONCERNROLEALTERNATEID.getDefaultCode();
    concatenateNameAndAlternateIDSSNKey.status =
      RECORDSTATUS.NORMAL;

    concatenateNameAndAlternateIDSSNKey.caseID = key.caseIDKey.caseID;
    </xsl:if>

    <xsl:for-each select="ServiceLayer/RelatedParticipantDetails">

      <xsl:variable name="columnName" select="@columnName"/>
      <xsl:variable name="aggregationName"><xsl:choose><xsl:when test="count(../../UserInterfaceLayer/Cluster/Field[@columnName=$columnName and @notOnEntity='Yes'])&gt;0">nonEvidenceDetails</xsl:when><xsl:otherwise>dtls</xsl:otherwise></xsl:choose></xsl:variable>

    if (readEvidenceDetails.<xsl:value-of select="$aggregationName"/>
        .<xsl:value-of select="$columnName"/> != 0) {

      caseParticipantRoleKey.caseParticipantRoleID = readEvidenceDetails
      .<xsl:value-of select="$aggregationName"/>.<xsl:value-of select="$columnName"/>;

      String fullName =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
          .readFullName(caseParticipantRoleKey).fullName;

      participantRoleIDAndName =
        caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(caseParticipantRoleKey);

      readConcernRoleKey = new ReadConcernRoleKey();
      readConcernRoleKey.concernRoleID = participantRoleIDAndName.participantRoleID;
      readConcernRole =
        maintainConcernRoleDetailsObj.readConcernRole(readConcernRoleKey);

      concernRolesDetails = new ConcernRolesDetails();
      concernRolesDetails.concernRoleID = readConcernRole.concernRoleID;
      concernRolesDetails.concernRoleType = readConcernRole.concernRoleType;
      concernRolesDetails.concernRoleName = readConcernRole.concernRoleName;

      fullName = maintainConcernRoleDetailsObj.appendAgeToName(
        concernRolesDetails).concernRoleName;

      <xsl:if test="//EvidenceEntities/@displayAltID='Yes'">
      concatenateNameAndAlternateIDSSNKey.name = fullName;
      concatenateNameAndAlternateIDSSNKey.caseParticipantRoleID =
      readEvidenceDetails.<xsl:value-of select="$aggregationName"/>.<xsl:value-of select="$columnName"/>;
      concatenateNameAndAlternateIDSSNDetails =
        GeneralUtilityFactory.newInstance()
        .concatenateNameAndAlternateIDSSN(concatenateNameAndAlternateIDSSNKey);

      fullName = concatenateNameAndAlternateIDSSNDetails.nameAndAlternateID;
      </xsl:if>

      <!--
        Updated to use the correct name of the aggregated struct so that
        multiple aggregations are supported
      -->

      <xsl:variable name="readParticipantNameDetails">
        <xsl:choose>
          <!-- BEGIN, CR00101786, CD  -->
          <xsl:when test="@name and @name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when>
          <!-- END, CR00101786  -->
          <xsl:otherwise>caseParticipantDetails</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:choose>
        <xsl:when test="@createParticipantType=$ParticipantTypeEmployer">
      readEvidenceDetails.<xsl:value-of select="$readParticipantNameDetails"/>.employerName = fullName;
        </xsl:when>
        <xsl:otherwise>
      readEvidenceDetails.<xsl:value-of select="$readParticipantNameDetails"/>.caseParticipantName = fullName;
        </xsl:otherwise>
      </xsl:choose>

    }
    </xsl:for-each>
    </xsl:if>
    <!-- END, CR00119218 -->

      <!-- END, CR00100657, POB -->
    // customisation point
    <!-- BEGIN, 25/02/2008, CD -->
    customise<xsl:value-of select="$EntityName"/>.postRead(key, readEvidenceDetails);
    <!-- END, 25/02/2008, CD -->

    return readEvidenceDetails;
  }


  <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
    <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@createParticipant='Yes']">

      <xsl:if test="@createParticipantType=$ParticipantTypeServiceProvider or
                    @createParticipantType=$ParticipantTypeEmployer or
                    @createParticipantType=$ParticipantTypeUnion or
                    @createParticipantType=$ParticipantTypePerson or
                    @createParticipantType=$ParticipantTypeUnknown">

      <xsl:variable name="argumentStruct">
        <xsl:choose>
          <xsl:when test="@createParticipantType=$ParticipantTypeServiceProvider">CaseParticipantDetails</xsl:when>
          <xsl:when test="@createParticipantType=$ParticipantTypeEmployer">CaseParticipantEmployerDetails</xsl:when>
          <xsl:when test="@createParticipantType=$ParticipantTypeUnion">UnionDetails</xsl:when>
          <xsl:when test="@createParticipantType=$ParticipantTypePerson">CaseParticipantDetails</xsl:when>
          <xsl:when test="@createParticipantType=$ParticipantTypeUnknown">CaseParticipantDetails</xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="relatedParticipantCapName">
        <xsl:choose>
          <xsl:when test="not(@name) or @name=''"/>
          <xsl:otherwise>
            <xsl:call-template name="capitalize">
              <xsl:with-param name="convertThis" select="@name"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
  //___________________________________________________________________________
  /**
   * Method that takes in 3 potential entry methods for a case participant and
   * based on what's entered, creates a Representative and a CaseParticipant or
   * just a CaseParticipant, or just links in the selected CaseParticipant.
   *
   * @param dtls The <xsl:value-of select="@name"/> record details.
   */
  private CaseParticipantRoleIDKey get<xsl:value-of select="$relatedParticipantCapName"/>CPRID(curam.core.sl.struct.<xsl:value-of select="$argumentStruct"/> dtls,
    CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    // return struct
    CaseParticipantRoleIDKey caseParticipantRoleIDKey =
      new CaseParticipantRoleIDKey();

    CaseParticipantRole caseParticipantRoleObj =
      CaseParticipantRoleFactory.newInstance();

    CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();

<!-- END, 25/02/2008, CD -->
      <xsl:if test="../@createRelatedParticipant='Yes'">

    long relParticipantRoleID = 0;

    CaseParticipantRoleDetails caseParticipantRoleDetails =
      new CaseParticipantRoleDetails();

    ReadByParticipantRoleTypeAndCaseKey readByParticipantRoleTypeAndCaseKey =
      new ReadByParticipantRoleTypeAndCaseKey();
    ReadByParticipantRoleTypeAndCaseDetails
      readByParticipantRoleTypeAndCaseDetails;
    NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      </xsl:if>

    <xsl:if test="@createParticipantType=$ParticipantTypeServiceProvider or @createParticipantType=$ParticipantTypeEmployer or @createParticipantType=$ParticipantTypeUnknown">
    boolean <xsl:value-of select="@name"/>RegisteredParticipant = false;

    // representative registration objects
    Representative <xsl:value-of select="@name"/>RepresentativeObj =
      RepresentativeFactory.newInstance();
    RepresentativeRegistrationDetails <xsl:value-of select="@name"/>RepresentativeRegistrationDetails =
      new curam.core.sl.struct.RepresentativeRegistrationDetails();

    Address <xsl:value-of select="@name"/>AddressObj = AddressFactory.newInstance();
    OtherAddressData <xsl:value-of select="@name"/>OtherAddressData =
      new OtherAddressData();
    EmptyIndStruct <xsl:value-of select="@name"/>AddressEmpty;

    // Check to see if recipient is registered on the system
    // registeredParticipant is true if the participant is already registered
    // on the system, but not a case participant of this case
      <xsl:value-of select="@name"/>RegisteredParticipant = false;

    if(dtls.caseParticipantRoleID != 0) {

       <xsl:value-of select="@name"/>RegisteredParticipant = true;
      caseParticipantRoleKey.caseParticipantRoleID = dtls.caseParticipantRoleID;
      CaseIDAndParticipantRoleIDDetails caseIDAndParticipantRoleIDDetails =
        caseParticipantRoleObj.readCaseIDandParticipantID(caseParticipantRoleKey);
      relParticipantRoleID = caseIDAndParticipantRoleIDDetails.participantRoleID;
    } else {
      if (dtls.participantRoleID != 0) {
        <xsl:value-of select="@name"/>RegisteredParticipant = true;
        relParticipantRoleID = dtls.participantRoleID;
      }
    }



    // If the participant has not been registered on the system, register it now
    if (!<xsl:value-of select="@name"/>RegisteredParticipant) {

      // populate the registration details
      <xsl:choose>
        <xsl:when test="@createParticipantType=$ParticipantTypeEmployer">
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeDtls.representativeName =
        dtls.participantName;
        </xsl:when>
        <xsl:otherwise>
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeDtls.representativeName =
        dtls.participantName + CuramConst.gkSpace + dtls.participantSecondName;
        </xsl:otherwise>
      </xsl:choose>
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode =
        dtls.phoneAreaCode;
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.phoneNumber =
        dtls.phoneNumber;
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.registrationDate =
        Date.getCurrentDate();

      // Check the address
      <xsl:value-of select="@name"/>OtherAddressData.addressData = dtls.address;
      <xsl:value-of select="@name"/>AddressEmpty = <xsl:value-of select="@name"/>AddressObj.isEmpty(<xsl:value-of select="@name"/>OtherAddressData);

      // if address is not provided for representative registration then we use
      // the default one
      if (<xsl:value-of select="@name"/>AddressEmpty.emptyInd) {
        <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.addressData =
        AddressDataFactory.newInstance().getAddressDataForLocale().addressData;
      } else {
        <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.addressData =
        dtls.address;
      }

      // register the evidence participant as a representative
      <xsl:value-of select="@name"/>RepresentativeObj.registerRepresentative(<xsl:value-of select="@name"/>RepresentativeRegistrationDetails);

      // Record the participant role of the newly registered representative
      relParticipantRoleID = <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeDtls.concernRoleID;
    }

      // create a case participant role

      caseParticipantRoleDetails.dtls.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;
      caseParticipantRoleDetails.dtls.fromDate =
        Date.getCurrentDate();
      caseParticipantRoleDetails.dtls.participantRoleID = relParticipantRoleID;
      caseParticipantRoleDetails.dtls.recordStatus =
        RECORDSTATUS.NORMAL;
      caseParticipantRoleDetails.dtls.toDate =
        Date.kZeroDate;
      caseParticipantRoleDetails.dtls.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;

      // case participant may exist already thus we need
      // to check for an exception
      readByParticipantRoleTypeAndCaseKey.participantRoleID = relParticipantRoleID;
      readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;
      readByParticipantRoleTypeAndCaseKey.recordStatus =
        RECORDSTATUS.NORMAL;
      readByParticipantRoleTypeAndCaseKey.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;

      readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);

      <xsl:if test="@createRoleType='MEMBER'">
      if (notFoundIndicator.isNotFound()) {
        // when creating type MEMBER need to check for existance of type PRIMARY
        readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.PRIMARY;
        readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);
      }
      </xsl:if>

      if (notFoundIndicator.isNotFound()) {

        caseParticipantRoleObj.insertCaseParticipantRole(caseParticipantRoleDetails);

        // set case participant role id for the evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          caseParticipantRoleDetails.dtls.caseParticipantRoleID;

      } else {

        // set case participant role id for the related evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          readByParticipantRoleTypeAndCaseDetails.caseParticipantRoleID;

      }
    </xsl:if>
    <xsl:if test="@createParticipantType=$ParticipantTypePerson">
    boolean registeredParticipant = false;

    // Participant registration objects
    curam.core.intf.PersonRegistration personRegistrationObj =
      curam.core.fact.PersonRegistrationFactory.newInstance();
    curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      new curam.core.struct.PersonRegistrationDetails();

    // Struct to pass to the validation function
    curam.core.sl.struct.CaseParticipantDetails validateDetails =
      new curam.core.sl.struct.CaseParticipantDetails();

    // validate that the create case participant details are correct
    <!-- BEGIN, CR00112535, CD -->
    validate<xsl:value-of select="$relatedParticipantCapName"/>Details(dtls, false);
    <!-- END, CR00112535 -->

    // Check to see if recipient is registered on the system
    // registeredParticipant is true if the participant is already registered
    // on the system, but not a case participant of this case
    registeredParticipant = false;

    if(dtls.caseParticipantRoleID != 0) {
      registeredParticipant = true;
      caseParticipantRoleKey.caseParticipantRoleID = dtls.caseParticipantRoleID;
      CaseIDAndParticipantRoleIDDetails caseIDAndParticipantRoleIDDetails =
      caseParticipantRoleObj.readCaseIDandParticipantID(caseParticipantRoleKey);
      relParticipantRoleID = caseIDAndParticipantRoleIDDetails.participantRoleID;
    } else {
      if (dtls.participantRoleID != 0) {
        registeredParticipant = true;
        relParticipantRoleID = dtls.participantRoleID;
      }
    }

    // If the participant has not been registered on the system, register it now
    if (!registeredParticipant) {

      // register the evidence participant

      // representative registration objects
      curam.core.sl.intf.Representative representativeObj =
        curam.core.sl.fact.RepresentativeFactory.newInstance();

      RepresentativeRegistrationDetails representativeRegistrationDetails =
        new RepresentativeRegistrationDetails();

      // populate the registration details
      representativeRegistrationDetails.representativeDtls.representativeName =
        dtls.participantName + " " + dtls.participantSecondName;

      if (dtls.ssn.length()>0) {
        representativeRegistrationDetails.representativeDtls.alternateID =
          dtls.ssn;
      }

      representativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode =
        dtls.phoneAreaCode;

      representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber =
        dtls.phoneNumber;

      representativeRegistrationDetails.representativeRegistrationDetails.registrationDate =
        Date.getCurrentDate();

      representativeRegistrationDetails.representativeRegistrationDetails.addressData =
        dtls.address;

      // throw any exceptions added to informational manager at this point.
      checkForInformationals();

      // register the separated partner as a representative
      representativeObj.registerRepresentative(
        representativeRegistrationDetails);

      // Keep note of the participant role of newly registered representative
      relParticipantRoleID =
        representativeRegistrationDetails.representativeDtls.concernRoleID;

    }

      // create a case participant role

      caseParticipantRoleDetails.dtls.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;
      caseParticipantRoleDetails.dtls.fromDate =
        Date.getCurrentDate();
      caseParticipantRoleDetails.dtls.participantRoleID = relParticipantRoleID;
      caseParticipantRoleDetails.dtls.recordStatus =
        RECORDSTATUS.NORMAL;
      caseParticipantRoleDetails.dtls.toDate =
        Date.kZeroDate;
      caseParticipantRoleDetails.dtls.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;

      // case participant may exist already thus we need
      // to check for an exception
      readByParticipantRoleTypeAndCaseKey.participantRoleID = relParticipantRoleID;
      readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;
      readByParticipantRoleTypeAndCaseKey.recordStatus =
        RECORDSTATUS.NORMAL;
      readByParticipantRoleTypeAndCaseKey.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;

      readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);

      <xsl:if test="@createRoleType='MEMBER'">
        if (notFoundIndicator.isNotFound()) {
        // when creating type MEMBER need to check for existance of type PRIMARY
        readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.PRIMARY;
        readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);
        }
      </xsl:if>

      if (notFoundIndicator.isNotFound()) {

        caseParticipantRoleObj.insertCaseParticipantRole(caseParticipantRoleDetails);

        // set case participant role id for the evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          caseParticipantRoleDetails.dtls.caseParticipantRoleID;

      } else {

        // set case participant role id for the related evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          readByParticipantRoleTypeAndCaseDetails.caseParticipantRoleID;

      }
    </xsl:if>

    <xsl:if test="@createParticipantType=$ParticipantTypeUnion">
    boolean <xsl:value-of select="@name"/>RegisteredParticipant = false;

    // representative registration objects
    Representative <xsl:value-of select="@name"/>RepresentativeObj =
      RepresentativeFactory.newInstance();
    RepresentativeRegistrationDetails <xsl:value-of select="@name"/>RepresentativeRegistrationDetails =
      new RepresentativeRegistrationDetails();

    Address <xsl:value-of select="@name"/>AddressObj = AddressFactory.newInstance();
    OtherAddressData <xsl:value-of select="@name"/>OtherAddressData = new OtherAddressData();
    EmptyIndStruct <xsl:value-of select="@name"/>AddressEmpty;

    // validate that the create case participant details are correct
    validateInsert<xsl:value-of select="@name"/>Details(dtls,
      "<xsl:value-of select="@label"/>");

    // Check to see if recipient is registered on the system
    // registeredParticipant is true if the participant is already registered
    // on the system, but not a case participant of this case
    <xsl:value-of select="@name"/>RegisteredParticipant = false;

    if(dtls.caseParticipantRoleID != 0) {
      <xsl:value-of select="@name"/>RegisteredParticipant = true;
      caseParticipantRoleKey.caseParticipantRoleID = dtls.caseParticipantRoleID;
      CaseIDAndParticipantRoleIDDetails caseIDAndParticipantRoleIDDetails =
      caseParticipantRoleObj.readCaseIDandParticipantID(caseParticipantRoleKey);
      relParticipantRoleID = caseIDAndParticipantRoleIDDetails.participantRoleID;
    } else {
      if (dtls.participantRoleID != 0) {
        <xsl:value-of select="@name"/>RegisteredParticipant = true;
        relParticipantRoleID = dtls.participantRoleID;
      }
    }

    // If the participant has not been registered on the system, register it now
    if (!<xsl:value-of select="@name"/>RegisteredParticipant) {

      // populate the registration details
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeDtls.representativeName =
        dtls.participantName;
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeDtls.alternateID =
        dtls.localNumber;
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode =
        dtls.phoneAreaCode;
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.phoneNumber =
        dtls.phoneNumber;
      <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.registrationDate =
        Date.getCurrentDate();

      // Check the address
      <xsl:value-of select="@name"/>OtherAddressData.addressData = dtls.address;
      <xsl:value-of select="@name"/>AddressEmpty = <xsl:value-of select="@name"/>AddressObj.isEmpty(<xsl:value-of select="@name"/>OtherAddressData);

      // if address is not provided for representative registration then we use
      // the default one
      if (<xsl:value-of select="@name"/>AddressEmpty.emptyInd) {
        <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.addressData =
        AddressDataFactory.newInstance().getAddressDataForLocale().addressData;
      } else {
        <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeRegistrationDetails.addressData =
          dtls.address;
      }

      // register the evidence participant as a representative
      <xsl:value-of select="@name"/>RepresentativeObj.registerRepresentative(
        <xsl:value-of select="@name"/>RepresentativeRegistrationDetails);

      // Record the participant role of the newly registered representative
      relParticipantRoleID =
        <xsl:value-of select="@name"/>RepresentativeRegistrationDetails.representativeDtls.concernRoleID;
    }

      // create a case participant role

      caseParticipantRoleDetails.dtls.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;
      caseParticipantRoleDetails.dtls.fromDate =
        Date.getCurrentDate();
      caseParticipantRoleDetails.dtls.participantRoleID = relParticipantRoleID;
      caseParticipantRoleDetails.dtls.recordStatus =
        RECORDSTATUS.NORMAL;
      caseParticipantRoleDetails.dtls.toDate =
        Date.kZeroDate;
      caseParticipantRoleDetails.dtls.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;

      // case participant may exist already thus we need
      // to check for an exception
      readByParticipantRoleTypeAndCaseKey.participantRoleID = relParticipantRoleID;
      readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;
      readByParticipantRoleTypeAndCaseKey.recordStatus =
        RECORDSTATUS.NORMAL;
      readByParticipantRoleTypeAndCaseKey.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;

      readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);

      <xsl:if test="@createRoleType='MEMBER'">
        if (notFoundIndicator.isNotFound()) {
        // when creating type MEMBER need to check for existance of type PRIMARY
        readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.PRIMARY;
        readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);
        }
      </xsl:if>

      if (notFoundIndicator.isNotFound()) {

        caseParticipantRoleObj.insertCaseParticipantRole(
          caseParticipantRoleDetails);

        // set case participant role id for the evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          caseParticipantRoleDetails.dtls.caseParticipantRoleID;

      } else {

        // set case participant role id for the related evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          readByParticipantRoleTypeAndCaseDetails.caseParticipantRoleID;

      }
    </xsl:if>
    <xsl:if test="@createParticipantType=$ParticipantTypeCoreEmployer">
    if (dtls.<xsl:value-of select="$evidenceDtls"/>.<xsl:value-of select="@columnName"/> == 0) {

      Employment employmentObj = EmploymentFactory.newInstance();
      EmploymentKey employmentKey = new EmploymentKey();
      employmentKey.employmentID =
        dtls.<xsl:value-of select="$evidenceDtls"/>.employmentID;

      EmploymentDtls employmentDtls = employmentObj.read(employmentKey);

      // Create the case participant role for the Employer
      caseParticipantRoleDetails.dtls.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;
      caseParticipantRoleDetails.dtls.fromDate =
        Date.getCurrentDate();
      caseParticipantRoleDetails.dtls.participantRoleID =
        employmentDtls.employerConcernRoleID;
      caseParticipantRoleDetails.dtls.recordStatus =
        RECORDSTATUS.NORMAL;
      caseParticipantRoleDetails.dtls.toDate = Date.kZeroDate;
      caseParticipantRoleDetails.dtls.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;


      // case participant may exist already thus we need
      // to check for an exception
      readByParticipantRoleTypeAndCaseKey.participantRoleID =
        employmentDtls.employerConcernRoleID;
      readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.<xsl:value-of select="@createRoleType"/>;
      readByParticipantRoleTypeAndCaseKey.recordStatus =
        RECORDSTATUS.NORMAL;
      readByParticipantRoleTypeAndCaseKey.caseID = <xsl:value-of select="$caseIDKey"/>.caseID;

      readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);

      <xsl:if test="@createRoleType='MEMBER'">
        if (notFoundIndicator.isNotFound()) {
        // when creating type MEMBER need to check for existance of type PRIMARY
        readByParticipantRoleTypeAndCaseKey.typeCode =
        CASEPARTICIPANTROLETYPE.PRIMARY;
        readByParticipantRoleTypeAndCaseDetails =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDCaseIDTypeAndRecordStatus(
        notFoundIndicator, readByParticipantRoleTypeAndCaseKey);
        }
      </xsl:if>

      if (notFoundIndicator.isNotFound()) {

        caseParticipantRoleObj.insertCaseParticipantRole(caseParticipantRoleDetails);

        // set case participant role id for the evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          caseParticipantRoleDetails.dtls.caseParticipantRoleID;

      } else {

        // set case participant role id for the related evidence
        caseParticipantRoleIDKey.caseParticipantRoleID =
          readByParticipantRoleTypeAndCaseDetails.caseParticipantRoleID;

      }
    }
    </xsl:if>
    return caseParticipantRoleIDKey;

  }
      </xsl:if>
    </xsl:for-each>
  </xsl:if>



  <xsl:if test="ServiceLayer/@createRelatedParticipant='Yes'">
    <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@createParticipant='Yes']">

      <xsl:variable name="relatedParticipantCapName">
        <xsl:choose>
          <xsl:when test="not(@name) or @name=''"/>
          <xsl:otherwise>
            <xsl:call-template name="capitalize">
              <xsl:with-param name="convertThis" select="@name"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <!-- BEGIN, CR00101878, POB -->
      <xsl:variable name="participantColumnName" select="@columnName"/>
      <xsl:variable name="singleNameField" select="../../UserInterfaceLayer/Cluster/Field[@columnName=$participantColumnName]/@singleNameField"/>
      <!-- END, CR00101878 -->

      <xsl:if test="@createParticipantType=$ParticipantTypeServiceProvider or
                    @createParticipantType=$ParticipantTypeEmployer or
                    @createParticipantType=$ParticipantTypeUnion or
                    @createParticipantType=$ParticipantTypeUnknown">

        <xsl:variable name="EntityName" select="../../@name"/>
  //___________________________________________________________________________
  /**
   * Validates the <xsl:value-of select="@name"/> details that are
   * being inserted.
   *
   * @param dtls The <xsl:value-of select="@name"/> record
   * details to validated.
   */
<xsl:choose>
 <xsl:when test="@createParticipantType=$ParticipantTypeServiceProvider or @createParticipantType=$ParticipantTypeUnknown">
 <!--public void validateInsert<xsl:value-of select="@name"/>Details(<xsl:value-of select="$javaEvidenceCodePath"/>.core.struct.ServiceProviderDetails details,-->
  <!-- BEGIN, CR00112535, CD -->
  private boolean validate<xsl:value-of select="$relatedParticipantCapName"/>Details(curam.core.sl.struct.CaseParticipantDetails details, boolean alreadyEntered)
  </xsl:when>
  <xsl:when test="@createParticipantType=$ParticipantTypeEmployer">
     <!--public void validateInsert<xsl:value-of select="@name"/>Details(<xsl:value-of select="$javaEvidenceCodePath"/>.core.struct.CaseParticipantEmployerDetails details,-->
  private boolean validate<xsl:value-of select="$relatedParticipantCapName"/>Details(curam.core.sl.struct.CaseParticipantEmployerDetails details, boolean alreadyEntered)
  </xsl:when>
  <xsl:when test="@createParticipantType=$ParticipantTypeUnion">
        <!--public void validateInsert<xsl:value-of select="@name"/>Details(<xsl:value-of select="$javaEvidenceCodePath"/>.core.struct.UnionDetails details,-->
  private boolean validate<xsl:value-of select="$relatedParticipantCapName"/>Details(curam.core.sl.struct.UnionDetails details, boolean alreadyEntered)
  <!-- END, CR00112535 -->
  </xsl:when>
</xsl:choose>
    throws AppException, InformationalException {

     InformationalManager informationalManager =
       TransactionInfo.getInformationalManager();

     String locale = curam.util.transaction.TransactionInfo.getProgramLocale();
     String <xsl:value-of select="@name"/>ParticipantArg;
     if(locale == null){
       <xsl:value-of select="@name"/>ParticipantArg =
       curam.message.<xsl:call-template name="entity-validation-message-class">
         <xsl:with-param name="entityName" select="$EntityName"/>
         </xsl:call-template>.INF_<xsl:value-of select="translate(@columnName, $lcletters, $ucletters)"/>.getMessageText();
     }else{
       <xsl:value-of select="@name"/>ParticipantArg =
       curam.message.<xsl:call-template name="entity-validation-message-class">
         <xsl:with-param name="entityName" select="$EntityName"/>
         </xsl:call-template>.INF_<xsl:value-of select="translate(@columnName, $lcletters, $ucletters)"/>.getMessageText(locale);
     }

     // registeredParticipant is true if the participant is already registered
     // on the system, but not a case participant of this case
     boolean registeredParticipant = false;
     // caseParticipant is true if the participant is already a case participant
     // If it is a case participant it is implicitly registered on the system.
     boolean caseParticipant = false;
     // Check to see if participant is a case participant or registered on system
     if (details.caseParticipantRoleID != 0) {

       caseParticipant = true;
       registeredParticipant = true;

     } else {

       if (details.participantRoleID != 0) {
         registeredParticipant = true;
       }

     }

    <!-- BEGIN, CR00103142, POB -->
    boolean nameProvided = (details.participantName.length() != 0);

     // Check if address has not been entered
     Address addressObj = AddressFactory.newInstance();

     OtherAddressData otherAddressData = new OtherAddressData();

     otherAddressData.addressData = details.address;

     EmptyIndStruct addressEmpty = addressObj.isEmpty(otherAddressData);

     // Check  area code AND phone number are entered
     boolean areaCodeEmpty = details.phoneAreaCode.length() == 0;
     boolean phoneNumberEmpty = details.phoneNumber.length() == 0;

     boolean detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/> =
       details.caseParticipantRoleID != 0 ||
       details.participantRoleID != 0 || nameProvided ||
       !addressEmpty.emptyInd || !areaCodeEmpty || !phoneNumberEmpty;

     <!-- BEGIN, CR00112535, CD -->
     if (alreadyEntered &amp;&amp; !detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>) {
       return false;
     }
     <!-- END, CR00112535 -->

     <xsl:variable name="relColumnName" select="@columnName"/>

        <xsl:if test="@modifyParticipant='Yes' or (count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='No'])&gt;0 and count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='Yes'])=0)">
     if (detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>) {
     </xsl:if>
     // Ensure that only one participant has been entered.
     if ( (details.caseParticipantRoleID != 0 &amp;&amp; details.participantRoleID != 0)
     || (details.caseParticipantRoleID != 0 &amp;&amp; nameProvided)
     || (details.participantRoleID != 0 &amp;&amp; nameProvided) ) {
       AppException appException =
        new AppException( <xsl:value-of select="$generalError"/>.ERR_XFV_MORE_THAN_ONE_PART);
       appException.arg(<xsl:value-of select="@name"/>ParticipantArg);
       informationalManager.addInformationalMsg(appException, "",
         InformationalElement.InformationalType.kError);
     }

     // If participant has been specified we don't need to validate
     if (!caseParticipant &amp;&amp; !registeredParticipant) {

       //
       // Begin validations
       //
       if(!nameProvided) {
         // If any of an unregistered participant details are entered,
         // ensure that their name is also entered
         if(!addressEmpty.emptyInd || !areaCodeEmpty || !phoneNumberEmpty) {
           AppException appException = new AppException
             ( <xsl:value-of select="$generalError"/>.ERR_FV_CREATE_PROVIDER_DETAILS_SET_NO_NAME);
           appException.arg(<xsl:value-of select="@name"/>ParticipantArg);
           informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
         } else {
           AppException appException = new AppException( <xsl:value-of select="$generalError"/>.ERR_FV_PARTICIPANT_EMPTY);
           appException.arg(<xsl:value-of select="@name"/>ParticipantArg);
           informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
         }
       }
       //If an unregistered participant name is entered, ensure that their address is also entered
       if (addressEmpty.emptyInd &amp;&amp; nameProvided) {
         AppException appException = new AppException(
          <xsl:value-of select="$generalError"/>.ERR_FV_CREATE_PROVIDER_NAME_SET_NO_ADDRESS);
         appException.arg(<xsl:value-of select="@name"/>ParticipantArg);
         informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
       }
        <!-- END, CR00103142 -->
       if(areaCodeEmpty &amp;&amp; !phoneNumberEmpty) {
         AppException appException = new
           AppException( <xsl:value-of select="$generalError"/>.ERR_XFV_PHONE_AREA_CODE);
         informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
       }
       if(!areaCodeEmpty &amp;&amp; phoneNumberEmpty) {
         AppException appException = new
           AppException(<xsl:value-of select="$generalError"/>.ERR_XFV_PHONE_NUMBER);
         informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
       }
     <xsl:if test="@createParticipantType=$ParticipantTypeUnion">
       //    Ensure local number is entered
       if(details.localNumber.length()==0){
         AppException appException =
           new AppException(
           <xsl:value-of select="$generalError"/>.ERR_FV_FIELD_MUST_BE_ENTERED_WHEN_ANOTHER_FIELD_ENTERED);

         appException.arg("Local Number");
         appException.arg("Union Name");
         informationalManager.addInformationalMsg(appException, "",
           InformationalElement.InformationalType.kError);
       }

       </xsl:if>

     }

     checkForInformationals();

        <xsl:if test="@modifyParticipant='Yes' or (count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='No'])&gt;0 and count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='Yes'])=0)">
     }
     </xsl:if>

     return detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>;

   }

  </xsl:if>

       <xsl:if test="@createParticipantType=$ParticipantTypePerson">

  //___________________________________________________________________________
  /**
   * Validates the <xsl:value-of select="@name"/> details that
   * are being inserted.
   *
   * @param dtls The <xsl:value-of select="@name"/> record
   * details to validated.
   */
 <!--public void validateInsert<xsl:value-of select="@name"/>Details(<xsl:value-of select="$javaEvidenceCodePath"/>.core.struct.CaseParticipantDetails details,-->
  <!-- BEGIN, CR00112535, CD -->
  private boolean validate<xsl:value-of select="$relatedParticipantCapName"/>Details(curam.core.sl.struct.CaseParticipantDetails details, boolean alreadyEntered)
  <!-- END, CR00112535 -->
    throws AppException, InformationalException {

    InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

     String locale = curam.util.transaction.TransactionInfo.getProgramLocale();
     String <xsl:value-of select="@name"/>ParticipantArg;
     if(locale == null){
       <xsl:value-of select="@name"/>ParticipantArg =
       curam.message.<xsl:call-template name="entity-validation-message-class">
         <xsl:with-param name="entityName" select="$EntityName"/>
         </xsl:call-template>.INF_<xsl:value-of select="translate(@columnName, $lcletters, $ucletters)"/>.getMessageText();
     }else{
       <xsl:value-of select="@name"/>ParticipantArg =
       curam.message.<xsl:call-template name="entity-validation-message-class">
         <xsl:with-param name="entityName" select="$EntityName"/>
         </xsl:call-template>.INF_<xsl:value-of select="translate(@columnName, $lcletters, $ucletters)"/>.getMessageText(locale);
     }

    // registeredParticipant is true if the participant is already registered
    // on the system, but not a case participant of this case
    boolean registeredParticipant = false;
    // caseParticipant is true if the participant is already a case participant
    // If it is a case participant it is implicitly registered on the system.
    boolean caseParticipant = false;
    // Check to see if participant is a case participant or registered on system
    if (details.caseParticipantRoleID != 0) {

      caseParticipant = true;
      registeredParticipant = true;

    } else {

      if (details.participantRoleID != 0) {
        registeredParticipant = true;
      }

    }

         <!-- BEGIN, CR00103142, POB -->
    boolean firstNameProvided = (details.participantName.length() != 0);
    <!-- BEGIN, CR00101878, POB -->
    <xsl:if test="$singleNameField='No'">
    boolean lastNameProvided = (details.participantSecondName.length() != 0);
    </xsl:if>
    <!-- END, CR00101878 -->

    // Check if address has not been entered
    Address addressObj = AddressFactory.newInstance();

    OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = details.address;

    boolean addressEmpty = addressObj.isEmpty(otherAddressData).emptyInd;

    boolean areaCodeEmpty = details.phoneAreaCode.length() == 0;
    boolean phoneNumberEmpty = details.phoneNumber.length() == 0;

         <!-- BEGIN, CR00101878, POB -->
         <xsl:choose>
         <xsl:when test="$singleNameField='Yes'">
    boolean detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/> =
       details.caseParticipantRoleID != 0 ||
       details.participantRoleID != 0 || firstNameProvided ||
       !addressEmpty || !areaCodeEmpty || !phoneNumberEmpty;
         </xsl:when>
           <xsl:otherwise>
    boolean detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/> =
       details.caseParticipantRoleID != 0 ||
       details.participantRoleID != 0 || firstNameProvided || lastNameProvided ||
       !addressEmpty || !areaCodeEmpty || !phoneNumberEmpty;
           </xsl:otherwise>
           </xsl:choose>

    <!-- BEGIN, CR00112535, CD -->
    if (alreadyEntered &amp;&amp; !detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>){
      return false;
    }
    <!-- END, CR00112535 -->

       <xsl:variable name="relColumnName" select="@columnName"/>
         <xsl:if test="@modifyParticipant='Yes' or (count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='No'])&gt;0 and count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='Yes'])=0)">
    if (detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>) {
    </xsl:if>
    // Ensure that only one participant has been entered.
    if ( (details.caseParticipantRoleID != 0 &amp;&amp; details.participantRoleID != 0)
    || (details.caseParticipantRoleID != 0 &amp;&amp; <xsl:choose>
      <xsl:when test="$singleNameField='Yes'">firstNameProvided)</xsl:when>
      <xsl:otherwise>(firstNameProvided || lastNameProvided))</xsl:otherwise></xsl:choose>
         || (details.participantRoleID != 0 &amp;&amp; <xsl:choose>
           <xsl:when test="$singleNameField='Yes'">firstNameProvided)</xsl:when>
           <xsl:otherwise>(firstNameProvided || lastNameProvided))</xsl:otherwise></xsl:choose> ) {
      AppException appException =
        new AppException( <xsl:value-of select="$generalError"/>.ERR_XFV_MORE_THAN_ONE_PART);
      appException.arg(<xsl:value-of select="@name"/>ParticipantArg);

      informationalManager.addInformationalMsg(appException, "",
        InformationalElement.InformationalType.kError);
    }

    // If participant has been specified we don't need to validate
    if (!caseParticipant &amp;&amp; !registeredParticipant) {

      //
      // Begin validations
      //
      if((!firstNameProvided) <xsl:if test="$singleNameField='No'">&amp;&amp; (!lastNameProvided)</xsl:if>) {
        // If any of an unregistered participant details are entered,
        // ensure that their name is also entered
        if(!addressEmpty || !areaCodeEmpty || !phoneNumberEmpty) {
          AppException appException = new AppException
            ( <xsl:value-of select="$generalError"/>.ERR_FV_CREATE_PROVIDER_DETAILS_SET_NO_NAME);
            appException.arg(<xsl:value-of select="@name"/>ParticipantArg);
            informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
        } else {
          AppException appException = new AppException( <xsl:value-of select="$generalError"/>.ERR_FV_PARTICIPANT_EMPTY);
          appException.arg(<xsl:value-of select="@name"/>ParticipantArg);
          informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
        }
      }
      //If an unregistered participant name is entered, ensure that their address is also entered
      if (addressEmpty &amp;&amp; (firstNameProvided <xsl:if test="$singleNameField='No'"> &amp;&amp; lastNameProvided</xsl:if>)) {
        AppException appException = new AppException(
          <xsl:value-of select="$generalError"/>.ERR_FV_CREATE_PROVIDER_NAME_SET_NO_ADDRESS);
        appException.arg(<xsl:value-of select="@name"/>ParticipantArg);
        informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
      }
       <!-- END, CR00103142 -->
      if(areaCodeEmpty &amp;&amp; !phoneNumberEmpty) {
        AppException appException = new
          AppException( <xsl:value-of select="$generalError"/>.ERR_XFV_PHONE_AREA_CODE);
        informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
      }
      if(!areaCodeEmpty &amp;&amp; phoneNumberEmpty) {
        AppException appException = new
          AppException(<xsl:value-of select="$generalError"/>.ERR_XFV_PHONE_NUMBER);
        informationalManager.addInformationalMsg(appException, "", InformationalElement.InformationalType.kError);
      }
    }

    checkForInformationals();
         <xsl:if test="@modifyParticipant='Yes' or (count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='No'])&gt;0 and count(../../UserInterfaceLayer/Cluster/Field[@columnName=$relColumnName and @mandatory='Yes'])=0)">
    }
    </xsl:if>

    return detailsArePresentFor<xsl:value-of select="$relatedParticipantCapName"/>;

  }
    </xsl:if>
  </xsl:for-each>
</xsl:if>



  <!-- <xsl:if test="($numberOfMandatoryAttributes > 0)"> -->
  //  ___________________________________________________________________________
  /**
  * Validates all mandatory fields for <xsl:value-of select="$EntityName"/>
  *
  * @param dtls contains the attribute details for mandatory validation.
  *
  */
  public void validateMandatoryDetails(<xsl:value-of select="$EvidenceDetails"/> dtls)
    throws AppException, InformationalException {

    curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    String locale = curam.util.transaction.TransactionInfo.getProgramLocale();
    String exceptionArgumentStr;
<!-- BEGIN, 25/02/2008, CD -->
  <xsl:for-each select="UserInterfaceLayer/Cluster/Field[@mandatory='Yes']">

    <xsl:variable name="aggregation">
      <xsl:choose>
        <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails</xsl:when>
        <xsl:otherwise>dtls</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="struct">
      <xsl:choose>
        <xsl:when test="@notOnEntity='Yes'"><xsl:value-of select="$EntityName"/>NonEvidenceDetails</xsl:when>
        <xsl:otherwise><xsl:value-of select="$EntityName"/>Dtls</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    //
    // Is mandatory field <xsl:value-of select="@columnName"/> set
    //
    <!-- BEGIN, CR00435583, JAF -->
    if(objectComparator.equals(dtls.<xsl:value-of select="$aggregation"/>.<xsl:value-of select="@columnName"/>, new <xsl:value-of select="$struct"/>().<xsl:value-of select="@columnName"/>)) {
    <!-- END, CR00435583 -->
<!-- END, 25/02/2008, CD -->
      AppException appException = new AppException(
        curam.message.GENERAL.ERR_GENERAL_FV_MANDATORY_FIELD_MUST_BE_ENTERED);

      <!-- BEGIN, PADDY -->
      <xsl:variable name="messageName">
        curam.message.<xsl:call-template name="entity-validation-message-class">
          <xsl:with-param name="entityName" select="$EntityName"/>
        </xsl:call-template>.<xsl:call-template name="entity-field-validation-message">
          <xsl:with-param name="fieldLabel" select="@label"/>
        </xsl:call-template>
      </xsl:variable>

      if(locale == null){
        exceptionArgumentStr = <xsl:value-of select="$messageName"/>.getMessageText();
      }else{
        exceptionArgumentStr = <xsl:value-of select="$messageName"/>.getMessageText(locale);
      }

      appException.arg(exceptionArgumentStr);
      <!-- END, PADDY -->
      informationalManager.addInformationalMsg(appException,
        CuramConst.gkEmpty,  InformationalElement.InformationalType.kError);
    }
  </xsl:for-each>

    <xsl:if test="count(Relationships/Parent)&gt;0">
    if(dtls.parEvKey.evidenceID == 0 ||
       dtls.parEvKey.evType == null ||
       dtls.parEvKey.evType.length() == 0) {

      AppException appException = new AppException(
        curam.message.GENERAL.ERR_GENERAL_XFV_PARENT_MUST_BE_ENTERED);

      informationalManager.addInformationalMsg(appException,
        CuramConst.gkEmpty,  InformationalElement.InformationalType.kError);
    }
    </xsl:if>

    checkForInformationals();
  }

  <xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0">
  //___________________________________________________________________________
  /**
   *  Reads a list of not associated <xsl:value-of select="$capName"/> records.
   *
   * @param key contains case ID for which <xsl:value-of select="$capName"/> records
   * must be read.
   *
   * @return List of not associated <xsl:value-of select="$capName"/> evidence records read.
   */
  public ListEvidenceDetails list<xsl:value-of select="$EntityName"/>EvidenceWithoutAssociations
    (EvidenceListKey key) throws AppException, InformationalException {

    //
    // Call the EvidenceController object and retrieve evidence list
    //
    EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    EIListCaseEvidenceKey eiListCaseEvidenceKey
      = new EIListCaseEvidenceKey();
    eiListCaseEvidenceKey.caseID = key.caseIDKey.caseID;
    eiListCaseEvidenceKey.evidenceType = CASEEVIDENCE.<xsl:value-of select="$EvidenceType"/>;

    // Retrieve the list of all evidence
    ECEvidenceForListPageDtls ecEvidenceForListPageDtls
      = evidenceControllerObj.listAllForEvidenceListPage(
      eiListCaseEvidenceKey);

    // Return list of not associated evidence
    ECEvidenceForListPageDtls ecNotAssociatedEvidenceForListPageDtls =
      new ECEvidenceForListPageDtls();

    for(int i = 0; i &lt; ecEvidenceForListPageDtls.activeList.dtls.size(); i++) {

      EvidenceRelationship evidenceRelationshipObj =
        EvidenceRelationshipFactory.newInstance();

      ChildKey childKey = new ChildKey();
      childKey.childID =
        ecEvidenceForListPageDtls.activeList.dtls.item(i).evidenceID;
      childKey.childType =
        ecEvidenceForListPageDtls.activeList.dtls.item(i).evidenceType;

      ParentKeyList parentKeyList =
        evidenceRelationshipObj.searchByChild(childKey);

      if (parentKeyList.dtls.size() == 0) {
        ecNotAssociatedEvidenceForListPageDtls.activeList.dtls.addRef(
          ecEvidenceForListPageDtls.activeList.dtls.item(i));
      }

    }

    for(int i = 0; i &lt; ecEvidenceForListPageDtls.newAndUpdateList.dtls.size(); i++) {

      EvidenceRelationship evidenceRelationshipObj =
        EvidenceRelationshipFactory.newInstance();

      ChildKey childKey = new ChildKey();
      childKey.childID =
        ecEvidenceForListPageDtls.newAndUpdateList.dtls.item(i).evidenceID;
      childKey.childType =
        ecEvidenceForListPageDtls.newAndUpdateList.dtls.item(i).evidenceType;

      ParentKeyList parentKeyList =
        evidenceRelationshipObj.searchByChild(childKey);

      if (parentKeyList.dtls.size() == 0) {
        ecNotAssociatedEvidenceForListPageDtls.newAndUpdateList.dtls.addRef(
          ecEvidenceForListPageDtls.newAndUpdateList.dtls.item(i));
      }

    }

    //
    // Return the evidence list
    //
    // Instantiate the return struct
    ListEvidenceDetails listEvidenceDetails =
      new ListEvidenceDetails();

    // assign the list of evidence to the return object
    listEvidenceDetails.evListDtls.assign(
      ecNotAssociatedEvidenceForListPageDtls);
    listEvidenceDetails.evidenceType = eiListCaseEvidenceKey.evidenceType;

    return listEvidenceDetails;
  }
  </xsl:if>
  //  ___________________________________________________________________________
  /**
   * Accessor function to return the caseParticipantRoleID. This function is
   * implemented by all evidence types. If the entity does not store the
   * caseParticipantRoleID it simply requests it from it's parent.
   *
   * @param key key containing evidence ID and type
   * @return CaseParticipantRoleID struct containing only the
   *                                          caseParticipantRoleID
   */
  public SearchCaseParticipantDetails getCaseParticipantRoleID(EIEvidenceKey key)
    throws AppException, InformationalException {

    <xsl:choose>

      <!-- If the metadata 'relateEvidenceParticipantID' is blank that means that
           we need to drill up through the ancestors to find an entity with it set. -->
      <xsl:when test="$casePartRoleIDattr=''">
    ChildKey childKey = new ChildKey();

    EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();

    // populate the search key
    childKey.childID = key.evidenceID;
    childKey.childType = key.evidenceType;

    ParentKeyList parentList = new ParentKeyList();

    // read the list of parents
    parentList = EvidenceRelationshipFactory.newInstance()
      .searchByChild(childKey);

    if (parentList.dtls.size() > 0) {

      // assign the first parent to be passed up the call tree
      eiEvidenceKey.evidenceID = parentList.dtls.item(0).parentID;
      eiEvidenceKey.evidenceType = parentList.dtls.item(0).parentType;

      // As there could be 2 possible parents for a piece of evidence, it is
      // safer to call the facade operation
      final SearchCaseParticipantDetails value =
        <xsl:value-of select="$ucPrefix"/>EvidenceMaintenanceFactory.newInstance()
          .getCaseParticipantRoleID(eiEvidenceKey);;

      if (value.caseParticipantRoleID != 0) {
        return value;
      }

    } else {

      // If we've reached the top of the ancestry tree (i.e. childKey has no
      // parents), we need a fallback mechanism for retrieving the cpr
      // associated with the evidence. The fallback mechanism below finds
      // a member cpr associated with the descriptor's participantID
      final SearchCaseParticipantDetails cprDetails =
        new SearchCaseParticipantDetails();

      final curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey readKey =
        new curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey();

      readKey.evidenceType = key.evidenceType;
      readKey.relatedID = key.evidenceID;

      final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls descriptor =
        curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory
          .newInstance().readByRelatedIDAndType(readKey);

      // Find the associated CPR
      if (descriptor.participantID != 0L) {

        final curam.core.sl.entity.struct.ListCaseParticipantRoleByParticipantKey listByParticipantKey =
          new curam.core.sl.entity.struct.ListCaseParticipantRoleByParticipantKey();

        listByParticipantKey.participantRoleID = descriptor.participantID;

        final curam.core.sl.entity.struct.CaseParticipantRoleDtlsList cprs =
          curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
            .listCaseParticipantRoleByParticipantID(listByParticipantKey);

        for (final curam.core.sl.entity.struct.CaseParticipantRoleDtls cpr : cprs.dtls) {

          if (cpr.typeCode.equals(curam.codetable.CASEPARTICIPANTROLETYPE.PRIMARY)
            || (cpr.typeCode.equals(curam.codetable.CASEPARTICIPANTROLETYPE.MEMBER))) {

            cprDetails.caseParticipantRoleID =
              cpr.caseParticipantRoleID;

            return cprDetails;
          }

        }

      }

    }

    // Should never reach here.
    // If no ancestor had a related evidence participant return an empty value.
    return new SearchCaseParticipantDetails();
      </xsl:when>

      <!-- If the metadata 'relateEvidenceParticipantID' is set to X that means that
           this entity has a CaseParticipantRoleID field, X, that relates to the
           participant which needs to be set on the Evidence Descriptor. -->
      <xsl:otherwise>
    // EvidenceController business object
    EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // return struct
    SearchCaseParticipantDetails searchCaseParticipantDetails = new SearchCaseParticipantDetails();

    // entity dtls struct
    <xsl:value-of select="@name"/>Dtls entityDtls = new <xsl:value-of select="@name"/>Dtls();

    // perform read
    <!-- BEGIN, CR00102248, POB -->
    EIEvidenceReadDtls eiEvidenceReadDtls =
    evidenceControllerObj.readEvidence(key);

    entityDtls =
    (<xsl:value-of select="@name"/>Dtls) eiEvidenceReadDtls.evidenceObject;

    // assign return value
    searchCaseParticipantDetails.caseParticipantRoleID =
      entityDtls.<xsl:value-of select="@relateEvidenceParticipantID"/>;

    CaseParticipantRole caseParticipantRoleObj =
      CaseParticipantRoleFactory.newInstance();

    CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();

    caseParticipantRoleKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;

    ParticipantRoleIDAndNameDetails participantRoleIDAndNameDetails =
      caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(caseParticipantRoleKey);

    // append age to the name
    MaintainConcernRoleDetails maintainConcernRoleDetailsObj =
      MaintainConcernRoleDetailsFactory.newInstance();

    String fullName = participantRoleIDAndNameDetails.name;

    ReadConcernRoleKey readConcernRoleKey = new ReadConcernRoleKey();
    readConcernRoleKey.concernRoleID = participantRoleIDAndNameDetails.participantRoleID;
    ReadConcernRoleDetails readConcernRole =
      maintainConcernRoleDetailsObj.readConcernRole(readConcernRoleKey);

    ConcernRolesDetails concernRolesDetails = new ConcernRolesDetails();
    concernRolesDetails.concernRoleID = readConcernRole.concernRoleID;
    concernRolesDetails.concernRoleType = readConcernRole.concernRoleType;
    concernRolesDetails.concernRoleName = readConcernRole.concernRoleName;

    fullName = maintainConcernRoleDetailsObj.appendAgeToName(
      concernRolesDetails).concernRoleName;
    <xsl:if test="//EvidenceEntities/@displayAltID='Yes'">
    // append the alternate id to the name
    ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
      new ConcatenateNameAndAlternateIDSSNKey();

    concatenateNameAndAlternateIDSSNKey.type =
      CONCERNROLEALTERNATEID.getDefaultCode();
    concatenateNameAndAlternateIDSSNKey.status = RECORDSTATUS.NORMAL;
    concatenateNameAndAlternateIDSSNKey.caseID = eiEvidenceReadDtls.descriptor.caseID;
    concatenateNameAndAlternateIDSSNKey.name = fullName;
    concatenateNameAndAlternateIDSSNKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;

    // Use Utility to work out Participants Full Name
    ConcatenateNameAndAlternateIDSSNDetails concatenateNameAndAlternateIDSSNDetails =
      GeneralUtilityFactory.newInstance().concatenateNameAndAlternateIDSSN(
        concatenateNameAndAlternateIDSSNKey);

    fullName = concatenateNameAndAlternateIDSSNDetails.nameAndAlternateID;
    </xsl:if>
    searchCaseParticipantDetails.name = fullName;
      <!-- END, CR00102248 -->
    <!-- BEGIN, 123218, JAY -->
    LocalisableString localizedNameAndAge = new LocalisableString(GENERAL.INF_SERVER_FORMATTED_MESSAGE);
	localizedNameAndAge.arg(fullName);
	searchCaseParticipantDetails.nameAndAgeOpt = localizedNameAndAge.toClientFormattedText();
    <!-- END, 123218, JAY -->
    // return
    return searchCaseParticipantDetails;
      </xsl:otherwise>

    </xsl:choose>
  }

  <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes']">

      <xsl:variable name="rPcolumnName" select="@columnName"/>

      <xsl:variable name="shortName">
        <xsl:call-template name="capitalize">
          <xsl:with-param name="convertThis" select="@name"/>
        </xsl:call-template>
      </xsl:variable>

      <xsl:for-each select="../../UserInterfaceLayer/Cluster/Field[@columnName=$rPcolumnName and (not(@mandatory) or @mandatory!='Yes')]">

  //  ___________________________________________________________________________
  /**
   * This function provides a means for a client modify screen to determine
   * whether an optional participant on the record has already been created.
   * It returns two booleans which decide which of two conditional clusters
   * should be displayed - one contains a link to a participant, the other
   * contains the required data entry fields to create the participant.
   *
   * @param key key containing evidence ID and caseID
   * @return OptionalParticipantDetails struct two booleans used for conditional
   * clusters on the client.
   */
  public OptionalParticipantDetails get<xsl:value-of select="$EntityName"/><xsl:value-of select="$shortName"/>OptionalParticipantDetails(
    EvidenceCaseKey key) throws AppException, InformationalException {

    OptionalParticipantDetails optionalParticipantDetails =
      new OptionalParticipantDetails();

    if(read<xsl:value-of select="$EntityName"/>Evidence(key).dtls.<xsl:value-of select="$rPcolumnName"/> != 0) {
      optionalParticipantDetails.displayParticipantLink = true;
      optionalParticipantDetails.displayAddParticipantCluster = false;
    } else {
      optionalParticipantDetails.displayParticipantLink = false;
      optionalParticipantDetails.displayAddParticipantCluster = true;
    }
    return optionalParticipantDetails;
  }

    </xsl:for-each>
  </xsl:for-each>

  //___________________________________________________________________________
  /**
   * Return details that will comprise the XML blob used to populate the
   * evidence comparison screen inside the Evidence Broker.
   *
   * @param key Identifies an evidence entity
   * @return Evidence entity details
   */
  public EvidenceComparisonDtls getComparisonData(EvidenceCaseKey key)
    throws AppException, InformationalException {

    <xsl:value-of select="$ProductEvidenceDtls"/> entityDtls = null;

    EvidenceComparisonDtls evidenceComparisonDtls =
      new EvidenceComparisonDtls();

    // read the <xsl:value-of select="$EntityName"/> record
    <xsl:value-of select="$ReadEvidenceDetails"/><xsl:text> </xsl:text><xsl:value-of select="$readEvidenceDetails"/> =
      null;

    if (!key.evidenceKey.evType.equals(kHoldingEvidence)) {
      <xsl:value-of select="$readEvidenceDetails"/> = read<xsl:value-of select="$EntityName"/>Evidence(key);
      evidenceComparisonDtls.descriptor =
        <xsl:value-of select="$readEvidenceDetails"/>.descriptor;
    } else {

      final curam.core.sl.infrastructure.impl.EvidenceMap map = curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();

      final curam.core.sl.infrastructure.impl.EvidenceInterface evidenceInterface =
        (curam.core.sl.infrastructure.impl.EvidenceInterface) map.getEvidenceType(key.evidenceKey.evType);

      final curam.core.sl.infrastructure.struct.EIEvidenceKey sourceEIEvidenceKey = new curam.core.sl.infrastructure.struct.EIEvidenceKey();

      sourceEIEvidenceKey.evidenceID = key.evidenceKey.evidenceID;
      sourceEIEvidenceKey.evidenceType = key.evidenceKey.evType;

      final Object evidenceObject =
        evidenceInterface.readEvidence(sourceEIEvidenceKey);

      final Object targetEvidenceObject =
        holdingEvidenceConversion.convertHoldingEvidence2TargetEvidenceObject(key, evidenceObject);

      entityDtls = (<xsl:value-of select="$ProductEvidenceDtls"/>) targetEvidenceObject;
    }

    ResourceBundle domainTypes =
      ResourceBundle.getBundle(
        "<xsl:value-of select="$javaEvidenceCodePath"/>.service.impl.<xsl:value-of select="$EntityName"/>Domains",
        new Locale(curam.util.transaction.TransactionInfo.getProgramLocale()));

    ResourceBundle labels =
      ResourceBundle.getBundle(
        "<xsl:value-of select="$javaEvidenceCodePath"/>.service.impl.<xsl:value-of select="$EntityName"/>Labels",
        new Locale(curam.util.transaction.TransactionInfo.getProgramLocale()));

    <xsl:variable name="viewClusters" select="UserInterfaceLayer/Cluster[@view='Yes']"/>

    <xsl:for-each select="$viewClusters/Field">
      <xsl:if test="@metatype=$metatypeParentCaseParticipant">
    // retrieve a case participant name from an ancestor record
    EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
      eiEvidenceKey.evidenceID = key.evidenceKey.evidenceID;
      eiEvidenceKey.evidenceType = key.evidenceKey.evType;
    String nameOf_<xsl:value-of select="@name"/> =
        <xsl:value-of select="$prefix"/>EvidenceMaintenanceFactory.newInstance()
      .getCaseParticipantRoleID(eiEvidenceKey).name;
      </xsl:if>
    </xsl:for-each>

    String[] names = {
      <xsl:for-each select="$viewClusters/Field">
        <xsl:choose>


          <xsl:when
            test="@metatype=$metatypeParentCaseParticipant or
                  @metatype=$metatypeEmployerCaseParticipant or
                  @metatype=$metatypeCaseParticipantSearch or
                  @metatype=$metatypeDisplayCaseParticipant">

            <xsl:variable name="columnName"><xsl:choose>
              <xsl:when test="@name and @name!=''"><xsl:value-of select="concat(@name, 'C')"/></xsl:when>
              <xsl:otherwise>c</xsl:otherwise>
              </xsl:choose>aseParticipantDetails.<xsl:choose>
              <xsl:when test="@metatype=$metatypeEmployerCaseParticipant">employerName</xsl:when>
              <xsl:otherwise>caseParticipantName</xsl:otherwise>
            </xsl:choose></xsl:variable>

            <xsl:variable name="qualifiedName"><xsl:choose>
                <xsl:when test="@metatype=$metatypeParentCaseParticipant">nameOf_<xsl:value-of select="@name"/></xsl:when>
                <xsl:otherwise>result.<xsl:choose>
                  <xsl:when test="@notOnEntity='Yes' and
                    @metatype!=$metatypeEmployerCaseParticipant and
                    @metatype!=$metatypeCaseParticipantSearch and
                    @metatype!=$metatypeDisplayCaseParticipant">nonEvidenceDetails.</xsl:when>
                  <xsl:otherwise></xsl:otherwise>
                </xsl:choose><xsl:value-of select="$columnName"/></xsl:otherwise>
            </xsl:choose></xsl:variable>

            <xsl:if test="position()!=1">, </xsl:if>"<xsl:value-of select="$qualifiedName"/>"
          </xsl:when>


          <xsl:otherwise>

            <xsl:variable name="semiQualified">
              <xsl:choose>
                <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">relatedEntityAttributes</xsl:when>
                <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails</xsl:when>
                <xsl:otherwise>dtls</xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            <xsl:if test="position()!=1">, </xsl:if>"result.<xsl:value-of select="$semiQualified"/>.<xsl:value-of select="@columnName"/>"
          </xsl:otherwise>

        </xsl:choose>
      </xsl:for-each>};

    if (entityDtls != null) {
      <xsl:value-of select="$readEvidenceDetails"/> = new <xsl:value-of select="$ReadEvidenceDetails"/>();
      <xsl:value-of select="$readEvidenceDetails"/>.dtls.assign(entityDtls);
    }

    Object[] valueObjects = {
      <xsl:for-each select="$viewClusters/Field">
        <xsl:choose>


          <xsl:when
            test="@metatype=$metatypeParentCaseParticipant or
                  @metatype=$metatypeEmployerCaseParticipant or
                  @metatype=$metatypeCaseParticipantSearch or
                  @metatype=$metatypeDisplayCaseParticipant">

            <xsl:variable name="columnName"><xsl:choose>
              <xsl:when test="@name and @name!=''"><xsl:value-of select="concat(@name, 'C')"/></xsl:when>
              <xsl:otherwise>c</xsl:otherwise>
              </xsl:choose>aseParticipantDetails.<xsl:choose>
              <xsl:when test="@metatype=$metatypeEmployerCaseParticipant">employerName</xsl:when>
              <xsl:otherwise>caseParticipantName</xsl:otherwise>
            </xsl:choose></xsl:variable>

            <xsl:variable name="qualifiedName"><xsl:choose>
                <xsl:when test="@metatype=$metatypeParentCaseParticipant">nameOf_<xsl:value-of select="@name"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="$readEvidenceDetails"/>.<xsl:choose>
                  <xsl:when
                    test="@metatype!=$metatypeEmployerCaseParticipant or
                          @metatype!=$metatypeCaseParticipantSearch or
                          @metatype!=$metatypeDisplayCaseParticipant"></xsl:when>
                  <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails.</xsl:when>
                  <xsl:otherwise></xsl:otherwise>
                </xsl:choose><xsl:value-of select="$columnName"/></xsl:otherwise>
            </xsl:choose></xsl:variable>

            <xsl:if test="position()!=1">, </xsl:if><xsl:value-of select="$qualifiedName"/><xsl:text>
            </xsl:text>
          </xsl:when>


          <xsl:otherwise>
            <xsl:variable name="semiQualified">
              <xsl:choose>
                <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">relatedEntityAttributes</xsl:when>
                <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails</xsl:when>
                <xsl:otherwise>dtls</xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            <xsl:if test="position()!=1">, </xsl:if><xsl:value-of select="$readEvidenceDetails"/>.<xsl:value-of select="$semiQualified"/>.<xsl:value-of select="@columnName"/><xsl:text>
            </xsl:text>
          </xsl:otherwise>

        </xsl:choose>
      </xsl:for-each>};

    String[] attributeNames = {
      <xsl:for-each select="$viewClusters/Field">
        <xsl:if test="position()!=1">, </xsl:if>"<xsl:value-of select="@columnName"/>"
      </xsl:for-each>};

    boolean[] mandatory = {
      <xsl:for-each select="$viewClusters/Field">
        <xsl:if test="position()!=1">, </xsl:if><xsl:choose>
      <xsl:when test="@mandatory='Yes'">true</xsl:when>
      <xsl:otherwise>false</xsl:otherwise>
      </xsl:choose>
      </xsl:for-each>};

    EvidenceComparisonHelper helper = new EvidenceComparisonHelper();

    // populate the return struct one attribute at a time
    for (int i=0; i&lt;names.length &amp;&amp; i&lt;valueObjects.length &amp;&amp; i&lt;attributeNames.length; i++) {

      EvidenceAttributeDtls attribute = new EvidenceAttributeDtls();

      try {
        attribute.domain = domainTypes.getString(names[i]);
      } catch (MissingResourceException mre) {
        // missing domain causes widget to fail
        // insert SVR_STRING by default
        attribute.domain = CuramConst.kDomainSVR_STRING;
      }
      try {
        attribute.label = labels.getString(names[i]);
      } catch (MissingResourceException mre) {
        // labels are blank by default
        attribute.label = CuramConst.gkEmpty;
      }
      attribute.value =
        helper.objectToString(valueObjects[i]);
      attribute.name= attributeNames[i];
      attribute.mandatory = mandatory[i];

      evidenceComparisonDtls.details.addRef(attribute);
    }

    return evidenceComparisonDtls;
  }
  <!-- END, CR00119218 -->

  <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/PreAssociation[@to!=''])&gt;0">
  private void fillInRelatedDetails(<xsl:value-of select="$EntityName"/>EvidenceDetails details)
    throws AppException, InformationalException {

    <xsl:variable name="evidenceTypeCode"><xsl:value-of select="translate($capName, $lcletters, $ucletters)"/></xsl:variable>

    EIEvidenceKey childKey = new EIEvidenceKey();
    childKey.evidenceID = details.dtls.evidenceID;
    childKey.evidenceType = CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>;

    ParentList parentList =
      curam.core.sl.infrastructure.fact.EvidenceRelationshipFactory.newInstance().getParentKeyList(childKey);

    // if the parent is empty, check for an IDENTICALINEDIT parent
    if (parentList.list.dtls.isEmpty()) {
      parentList = curam.core.sl.infrastructure.fact.EvidenceRelationshipFactory.newInstance().getSharedParentKeyList(childKey);
    }

    EIEvidenceKeyList parentEvidenceKeyList = new EIEvidenceKeyList();

    for (int i = 0; i &lt; parentList.list.dtls.size(); i++) {

      EIEvidenceKey eiParentEvidenceKey = new EIEvidenceKey();

      eiParentEvidenceKey.evidenceID =
        parentList.list.dtls.item(i).parentID;
      eiParentEvidenceKey.evidenceType =
        parentList.list.dtls.item(i).parentType;

      parentEvidenceKeyList.dtls.addRef(eiParentEvidenceKey);
    }

    // Filter the list
    EIEvidenceKeyList filteredParentEvidenceKeyList =
      EvidenceControllerFactory.newInstance().filterActivePendingAndIdenticalChanges(parentEvidenceKeyList);

    if (filteredParentEvidenceKeyList.dtls.size() > 0) {

      for (int i = 0; i &lt; filteredParentEvidenceKeyList.dtls.size(); i++) {

        EIEvidenceKey parentKey = filteredParentEvidenceKeyList.dtls.item(i);
        <xsl:if test="count(Relationships/Parent)&gt;0">
        <xsl:for-each select="Relationships/Parent">
        <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@name, $lcletters, $ucletters)"/></xsl:variable>
        if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
          details.parEvKey.evidenceID = parentKey.evidenceID;
          details.parEvKey.evType = parentKey.evidenceType;
        }
        </xsl:for-each>
        </xsl:if>

        <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">
        <xsl:for-each select="Relationships/MandatoryParents/Parent">

        <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@name, $lcletters, $ucletters)"/></xsl:variable>
        <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>

        if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
          details.<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID = parentKey.evidenceID;
          details.<xsl:value-of select="$lcParentName"/>ParEvKey.evType = parentKey.evidenceType;
        }
        </xsl:for-each>
        </xsl:if>

        <xsl:if test="count(Relationships/PreAssociation[@to!=''])&gt;0">
        <xsl:for-each select="Relationships/PreAssociation[@to!='']">
        <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@to, $lcletters, $ucletters)"/></xsl:variable>
        if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
          details.preAssocKey.evidenceID = parentKey.evidenceID;
          details.preAssocKey.evType = parentKey.evidenceType;
        }
        </xsl:for-each>
        </xsl:if>

      }
    }
  }
  </xsl:if>
}
    </redirect:write>

    <xsl:value-of select="@name"/>
    <xsl:text>
    </xsl:text>
  </xsl:template>


  <!-- GetSortedImports and GetUnsortedImports makes -->
  <!-- organising the imports a lot easier.          -->
  <xsl:template name="GetSortedImports">
    <xsl:param name="entity"/>
    <xsl:variable name="file"><xsl:value-of select="$productEvidenceHome"/>/gen.tmp/imports/service/<xsl:value-of select="$entity"/>.imports</xsl:variable>
    <xsl:variable name="unsorted" select="document($file)/imports"/>
    <xsl:for-each select="$unsorted/import"><xsl:sort select="@value"/>
<xsl:value-of select="@value"/><xsl:text>&#10;</xsl:text>
    </xsl:for-each>
  </xsl:template>


  <xsl:template match="text()"/>
</xsl:stylesheet>
