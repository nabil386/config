package curam.ca.gc.bdm.sl.pdc.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.entity.fact.BDMPhoneNumberFactory;
import curam.ca.gc.bdm.entity.struct.BDMPhoneNumberDtls;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.PHONETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.ConcernRolePhoneNumberFactory;
import curam.core.fact.PhoneNumberFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRolePhoneNumber;
import curam.core.intf.PhoneNumber;
import curam.core.intf.UniqueID;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.ClientMerge;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.sl.struct.ParticipantKeyStruct;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRolePhoneDetails;
import curam.core.struct.ConcernRolePhoneNumberDtls;
import curam.core.struct.ConcernRolePhoneNumberKey;
import curam.core.struct.CuramInd;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.DuplicateNumberSearchKey;
import curam.core.struct.PhoneNumberDtls;
import curam.core.struct.PhoneNumberKey;
import curam.core.struct.PhoneRMDtlsList;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.message.BPOMAINTAINCONCERNROLEPHONE;
import curam.message.GENERAL;
import curam.message.GENERALCASE;
import curam.message.GENERALCONCERN;
import curam.message.PARTICIPANTDATACASE;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.impl.PDCPhoneNumber;
import curam.pdc.impl.PDCPhoneNumberEvidencePopulator;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import java.util.Iterator;
import java.util.Set;

public class BDMPDCPhoneNumber
  extends curam.ca.gc.bdm.sl.pdc.base.BDMPDCPhoneNumber {

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private Set<PDCPhoneNumberEvidencePopulator> pdcPhoneNumberEvidencePopulators;

  public BDMPDCPhoneNumber() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public ConcernRolePhoneNumberKey
    insert(final ParticipantPhoneDetails details)
      throws AppException, InformationalException {

    new ConcernRolePhoneNumberKey();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = details.concernRoleID;
    ConcernRolePhoneNumberKey concernRolePhoneNumberKey;
    if (PDCUtilFactory.newInstance().isPDCEnabled(concernRoleKey).enabled) {
      concernRolePhoneNumberKey = this.createPhoneNumberEvidence(details);
    } else {
      concernRolePhoneNumberKey = this.createPhoneNumber(details);
    }
    return concernRolePhoneNumberKey;
  }

  private ConcernRolePhoneNumberKey
    createPhoneNumberEvidence(final ParticipantPhoneDetails details)
      throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = details.concernRoleID;
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCPHONENUMBER;
    final EvidenceTypeDef evidenceType =
      this.etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);
    final EvidenceTypeVersionDef evTypeVersion =
      this.etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);
    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute("participant");
    DynamicEvidenceTypeConverter.setAttribute(participant,
      pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);
    this.assignEvidenceDetails(pdcCaseIDCaseParticipantRoleID, details,
      dynamicEvidenceDataDetails);
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();
    evidenceDescriptorInsertDtls.participantID = details.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = details.concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;
    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
    final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
      new ConcernRolePhoneNumberKey();
    final String tmp = BDMPDCPhoneNumber.class.getName();
    final Object facadeScopeObject =
      TransactionInfo.getFacadeScopeObject(PDCPhoneNumber.class.getName());
    if (facadeScopeObject != null) {
      concernRolePhoneNumberKey.concernRolePhoneNumberID =
        (Long) facadeScopeObject;
    }

    return concernRolePhoneNumberKey;
  }

  @Override
  public void modify(final ParticipantPhoneDetails details)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = details.concernRoleID;
    if (PDCUtilFactory.newInstance().isPDCEnabled(concernRoleKey).enabled) {
      this.modifyPhoneNumberEvidence(details);
    } else {
      this.modifyPhoneNumber(details);
    }

  }

  private void
    modifyPhoneNumberEvidence(final ParticipantPhoneDetails details)
      throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = details.concernRoleID;
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
      new ConcernRolePhoneNumberKey();
    concernRolePhoneNumberKey.concernRolePhoneNumberID =
      details.concernRolePhoneNumberID;
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      concernRolePhoneNumberObj.read(concernRolePhoneNumberKey);
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    new PhoneNumberDtls();
    final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();
    phoneNumberKey.phoneNumberID = concernRolePhoneNumberDtls.phoneNumberID;
    final PhoneNumberDtls phoneNumberDtls =
      phoneNumberObj.read(phoneNumberKey);
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
    final ECActiveEvidenceDtlsList ecActiveEvidenceDtlsList =
      evidenceControllerObj.listActive(caseKey);
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    final EIEvidenceKey eiModifyEvidenceKey = new EIEvidenceKey();
    final EIEvidenceModifyDtls eiEvidenceModifyDtls =
      new EIEvidenceModifyDtls();
    new EvidenceDescriptorModifyDtls();
    final Iterator var18 = ecActiveEvidenceDtlsList.dtls.iterator();

    while (var18.hasNext()) {
      final ECActiveEvidenceDtls ecActiveEvidenceDtls =
        (ECActiveEvidenceDtls) var18.next();
      if (ecActiveEvidenceDtls.evidenceType.equals(PDCConst.PDCPHONENUMBER)) {
        eiEvidenceKey.evidenceID = ecActiveEvidenceDtls.evidenceID;
        eiEvidenceKey.evidenceType = PDCConst.PDCPHONENUMBER;
        final EIEvidenceReadDtls evidenceReadDtls =
          evidenceControllerObj.readEvidence(eiEvidenceKey);
        final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
          (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;
        if (this.matchEvidenceDetails(concernRolePhoneNumberDtls,
          phoneNumberDtls, dynamicEvidenceDataDetails)) {
          eiModifyEvidenceKey.evidenceID = ecActiveEvidenceDtls.evidenceID;
          eiModifyEvidenceKey.evidenceType =
            ecActiveEvidenceDtls.evidenceType;
          final EvidenceDescriptorModifyDtls evidenceDescriptorModifyDtls =
            new EvidenceDescriptorModifyDtls();
          evidenceDescriptorModifyDtls.receivedDate = Date.getCurrentDate();
          evidenceDescriptorModifyDtls.changeReason =
            EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
          this.assignEvidenceDetails(pdcCaseIDCaseParticipantRoleID, details,
            dynamicEvidenceDataDetails);
          eiEvidenceModifyDtls.descriptor
            .assign(evidenceDescriptorModifyDtls);
          eiEvidenceModifyDtls.evidenceObject = dynamicEvidenceDataDetails;
          break;
        }
      }
    }

    if (eiModifyEvidenceKey.evidenceID != 0L) {
      evidenceControllerObj.modifyEvidence(eiModifyEvidenceKey,
        eiEvidenceModifyDtls);
    } else {
      throw new AppException(PARTICIPANTDATACASE.ERR_PDC_EVIDENCE_NOT_FOUND);
    }
  }

  private void assignEvidenceDetails(
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID,
    final ParticipantPhoneDetails details,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails phoneCountryCode =
      dynamicEvidenceDataDetails.getAttribute("phoneCountryCode");
    DynamicEvidenceTypeConverter.setAttribute(phoneCountryCode,
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, details.phoneCountryCode));
    final DynamicEvidenceDataAttributeDetails phoneAreaCode =
      dynamicEvidenceDataDetails.getAttribute("phoneAreaCode");
    DynamicEvidenceTypeConverter.setAttribute(phoneAreaCode,
      details.phoneAreaCode);
    final DynamicEvidenceDataAttributeDetails phoneNumber =
      dynamicEvidenceDataDetails.getAttribute("phoneNumber");
    DynamicEvidenceTypeConverter.setAttribute(phoneNumber,
      details.phoneNumber);
    final DynamicEvidenceDataAttributeDetails phoneExtension =
      dynamicEvidenceDataDetails.getAttribute("phoneExtension");
    DynamicEvidenceTypeConverter.setAttribute(phoneExtension,
      details.phoneExtension);
    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute("fromDate");
    if (!details.startDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(fromDate, details.startDate);
    } else {
      fromDate.setValue("");
    }

    final DynamicEvidenceDataAttributeDetails endDate =
      dynamicEvidenceDataDetails.getAttribute("toDate");
    if (!details.endDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(endDate, details.endDate);
    } else {
      endDate.setValue("");
    }

    final DynamicEvidenceDataAttributeDetails phoneType =
      dynamicEvidenceDataDetails.getAttribute("phoneType");
    DynamicEvidenceTypeConverter.setAttribute(phoneType,
      new CodeTableItem(PHONETYPE.TABLENAME, details.typeCode));
    final DynamicEvidenceDataAttributeDetails comments =
      dynamicEvidenceDataDetails.getAttribute("comments");
    DynamicEvidenceTypeConverter.setAttribute(comments, details.comments);
    final DynamicEvidenceDataAttributeDetails preferredAddressInd =
      dynamicEvidenceDataDetails.getAttribute("preferredInd");
    ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls;
    if (details.primaryPhoneInd) {
      DynamicEvidenceTypeConverter.setAttribute(preferredAddressInd, true);
    } else if (details.concernRolePhoneNumberID != 0L) {
      final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
        new ConcernRolePhoneNumberKey();
      concernRolePhoneNumberKey.concernRolePhoneNumberID =
        details.concernRolePhoneNumberID;
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      concernRolePhoneNumberDtls = ConcernRolePhoneNumberFactory.newInstance()
        .read(notFoundIndicator, concernRolePhoneNumberKey);
      if (!notFoundIndicator.isNotFound()) {
        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
        concernRoleKey.concernRoleID = details.concernRoleID;
        final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
        final ConcernRoleDtls concernRoleDtls =
          concernRoleObj.read(concernRoleKey);
        if (concernRoleDtls.primaryPhoneNumberID == concernRolePhoneNumberDtls.phoneNumberID) {
          DynamicEvidenceTypeConverter.setAttribute(preferredAddressInd,
            true);
        }
      }
    }

    if (!this.pdcPhoneNumberEvidencePopulators.isEmpty()) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = details.concernRoleID;
      final CaseIDKey caseIDKey = new CaseIDKey();
      caseIDKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
      concernRolePhoneNumberDtls = new ConcernRolePhoneNumberDtls();
      concernRolePhoneNumberDtls.assign(details);
      final PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();
      phoneNumberDtls.assign(details);
      final Iterator var22 = this.pdcPhoneNumberEvidencePopulators.iterator();

      while (var22.hasNext()) {
        final PDCPhoneNumberEvidencePopulator pdcPhoneNumberEvidencePopulator =
          (PDCPhoneNumberEvidencePopulator) var22.next();
        pdcPhoneNumberEvidencePopulator.populate(concernRoleKey, caseIDKey,
          concernRolePhoneNumberDtls, phoneNumberDtls,
          dynamicEvidenceDataDetails);
      }
    }

  }

  private boolean matchEvidenceDetails(
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls,
    final PhoneNumberDtls phoneNumberDtls,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails) {

    return dynamicEvidenceDataDetails.getAttribute("phoneNumber").getValue()
      .equals(phoneNumberDtls.phoneNumber)
      && dynamicEvidenceDataDetails.getAttribute("phoneType").getValue()
        .equals(concernRolePhoneNumberDtls.typeCode);
  }

  private ConcernRolePhoneNumberKey
    createPhoneNumber(final ParticipantPhoneDetails details)
      throws AppException, InformationalException {

    final ParticipantKeyStruct participantKeyStruct =
      new ParticipantKeyStruct();
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    new EIEvidenceKey();
    new EvidenceDescriptorModifyDtls();
    new EIEvidenceModifyDtls();
    long concernRolePhoneNumberID = 0L;
    long phoneNumberID = 0L;
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      new ConcernRolePhoneNumberDtls();
    final DuplicateNumberSearchKey dupNumSearchKey =
      new DuplicateNumberSearchKey();
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    boolean primaryInd = false;
    concernRoleKey.concernRoleID = details.concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleKey);
    this.checkClientSecurity(concernRoleKey);
    participantKeyStruct.participantID = details.concernRoleID;
    this.checkParticipantSensitivity(participantKeyStruct);
    final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();
    CuramInd curamInd = clientMergeObj.isConcernRoleDuplicate(concernRoleKey);
    if (curamInd.statusInd) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEEPHONE_XRV_DUPLICATE_CLIENT_CREATE),
        "a", 0);
    }

    final ConcernRolePhoneDetails concernRolePhoneDetails =
      new ConcernRolePhoneDetails();
    concernRolePhoneDetails.assign(details);
    if (!CONCERNROLETYPE.REPRESENTATIVE
      .equals(concernRoleDtls.concernRoleType)) {
      this.validateConcernRolePhone(details);
    }
    new CuramInd();
    curamInd = this.validate(details);
    primaryInd = curamInd.statusInd;
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    concernRolePhoneNumberID = uniqueIDObj.getNextID();
    phoneNumberDtls.assign(concernRolePhoneDetails);
    dupNumSearchKey.concernRoleID = details.concernRoleID;
    dupNumSearchKey.phoneNumber = concernRolePhoneDetails.phoneNumber;
    dupNumSearchKey.typeCode = concernRolePhoneDetails.typeCode;
    dupNumSearchKey.phoneExtension = concernRolePhoneDetails.phoneExtension;
    dupNumSearchKey.statusCode = RECORDSTATUS.NORMAL;
    final PhoneRMDtlsList phoneRMDtlsList =
      concernRolePhoneNumberObj.searchPhoneForDuplicate(dupNumSearchKey);
    if (!phoneRMDtlsList.dtls.isEmpty()) {
      final AppException e = new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_PHONENUMBER_XFV_DUPLICATE);
      e.arg(concernRolePhoneDetails.phoneNumber);
      e.arg(concernRoleDtls.concernRoleName);
      ValidationManagerFactory.getManager().throwWithLookup(e, "a", 0);
    }
    // Task 21121 -BEGIN- use BDMPhoneCountry code table value to store country
    // code
    // phoneNumberDtls.phoneCountryCode = "";

    final BDMUtil bdmUtil = new BDMUtil();

    // BUG 100116
    phoneNumberDtls.phoneCountryCode =
      bdmUtil.getPhoneCountryCode(phoneNumberDtls.phoneCountryCode);
    // Task 21121 -END- use BDMPhoneCountry code table value to store country
    // code
    phoneNumberObj.insert(phoneNumberDtls);
    phoneNumberID = phoneNumberDtls.phoneNumberID;
    // Task 21121 -BEGIN- use BDMPhoneCountry code table value to store country
    // code
    final BDMPhoneNumberDtls bdmPhoneNumberDtls = new BDMPhoneNumberDtls();
    bdmPhoneNumberDtls.bdmPhoneNumberID = uniqueIDObj.getNextID();
    bdmPhoneNumberDtls.phoneNumberID = phoneNumberID;
    bdmPhoneNumberDtls.phoneCountryCode = details.phoneCountryCode;
    BDMPhoneNumberFactory.newInstance().insert(bdmPhoneNumberDtls);
    // Task 21121 -END- use BDMPhoneCountry code table value to store country
    // code
    concernRolePhoneNumberDtls.assign(concernRolePhoneDetails);
    concernRolePhoneNumberDtls.phoneNumberID = phoneNumberID;
    concernRolePhoneNumberDtls.concernRolePhoneNumberID =
      concernRolePhoneNumberID;
    concernRolePhoneNumberObj.insert(concernRolePhoneNumberDtls);
    if (primaryInd) {
      concernRoleDtls.primaryPhoneNumberID = phoneNumberID;
      final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
      eiEvidenceKey.evidenceID = concernRoleKey.concernRoleID;
      eiEvidenceKey.evidenceType = CASEEVIDENCE.CONCERNROLE;
      final EvidenceDescriptorModifyDtls evidenceDescriptorModifyDtls =
        new EvidenceDescriptorModifyDtls();
      evidenceDescriptorModifyDtls.receivedDate = Date.getCurrentDate();
      final EIEvidenceModifyDtls eiEvidenceModifyDtls =
        new EIEvidenceModifyDtls();
      eiEvidenceModifyDtls.descriptor.assign(evidenceDescriptorModifyDtls);
      eiEvidenceModifyDtls.parentKey.evidenceID = 0L;
      eiEvidenceModifyDtls.evidenceObject = concernRoleDtls;
      evidenceControllerObj.modifyEvidence(eiEvidenceKey,
        eiEvidenceModifyDtls);
    }

    final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
      new ConcernRolePhoneNumberKey();
    concernRolePhoneNumberKey.concernRolePhoneNumberID =
      concernRolePhoneNumberDtls.concernRolePhoneNumberID;
    return concernRolePhoneNumberKey;
  }

  private void modifyPhoneNumber(final ParticipantPhoneDetails details)
    throws AppException, InformationalException {

    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
      new ConcernRolePhoneNumberKey();
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      new ConcernRolePhoneNumberDtls();
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    new EIEvidenceKey();
    new EvidenceDescriptorModifyDtls();
    new EIEvidenceModifyDtls();
    final Date todaysDate = Date.getCurrentDate();
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();
    final PhoneNumberDtls modifyPhoneNumberDtls = new PhoneNumberDtls();
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final DuplicateNumberSearchKey dupNumSearchKey =
      new DuplicateNumberSearchKey();
    final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();
    concernRoleKey.concernRoleID = details.concernRoleID;
    final CuramInd curamInd =
      clientMergeObj.isConcernRoleDuplicate(concernRoleKey);
    if (curamInd.statusInd) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEEPHONE_XRV_DUPLICATE_CLIENT_MODIFY),
        "a", 0);
    }

    if (details.statusCode.equals(RECORDSTATUS.CANCELLED)) {
      ValidationManagerFactory.getManager().throwWithLookup(
        new AppException(GENERAL.ERR_GENERAL_FV_NO_MODIFY_RECORD_CANCELLED),
        "a", 18);
    }

    this.validateConcernRolePhone(details);
    if (details.primaryPhoneInd && !details.endDate.isZero()
      && details.endDate.before(todaysDate)) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEEPHONE_FV_NON_PRIMARY_DATE_PASSED),
        "a", 0);
    }

    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleKey);
    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();
    participantSecurityCheckKey.participantID = details.concernRoleID;
    participantSecurityCheckKey.type = LOCATIONACCESSTYPE.MAINTAIN;
    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkParticipantSecurity(participantSecurityCheckKey);
    if (!dataBasedSecurityResult.result) {
      if (dataBasedSecurityResult.readOnly) {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
      } else if (dataBasedSecurityResult.restricted) {
        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      } else {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
      }
    } else {
      AppException e;
      if (!CONCERNROLETYPE.PERSON.equals(concernRoleDtls.concernRoleType)
        && !CONCERNROLETYPE.PROSPECTPERSON
          .equals(concernRoleDtls.concernRoleType)
        && !CONCERNROLETYPE.REPRESENTATIVE
          .equals(concernRoleDtls.concernRoleType)) {
        if (!concernRoleDtls.registrationDate.isZero()
          && concernRoleDtls.registrationDate.after(details.startDate)) {
          e = new AppException(
            GENERALCONCERN.ERR_CONCERNROLE_XRV_NONPERSON_START_DATE);
          e.arg(concernRoleDtls.concernRoleName);
          e.arg(concernRoleDtls.registrationDate);
          ValidationManagerFactory.getManager().throwWithLookup(e, "a", 2);
        }
      } else if (!concernRoleDtls.startDate.isZero()
        && concernRoleDtls.startDate.after(details.startDate)
        && !CONCERNROLETYPE.REPRESENTATIVE
          .equals(concernRoleDtls.concernRoleType)) {
        e = new AppException(
          GENERALCONCERN.ERR_CONCERNROLE_XRV_PERSON_START_DATE);
        e.arg(concernRoleDtls.concernRoleName);
        e.arg(concernRoleDtls.startDate);
        ValidationManagerFactory.getManager().throwWithLookup(e, "a", 2);
      }

      if (!concernRoleDtls.endDate.isZero()) {
        if (details.startDate.after(concernRoleDtls.endDate)) {
          e = new AppException(
            GENERALCONCERN.ERR_CONCERNROLE_XRV_START_DATE_END_DATE);
          e.arg(concernRoleDtls.concernRoleName);
          e.arg(concernRoleDtls.endDate);
          ValidationManagerFactory.getManager().throwWithLookup(e, "a", 2);
        }

        if (!details.endDate.isZero()
          && details.endDate.after(concernRoleDtls.endDate)) {
          e = new AppException(GENERALCONCERN.ERR_CONCERNROLE_XRV_END_DATE);
          e.arg(concernRoleDtls.concernRoleName);
          e.arg(concernRoleDtls.endDate);
          ValidationManagerFactory.getManager().throwWithLookup(e, "a", 1);
        }
      }

      dupNumSearchKey.concernRoleID = details.concernRoleID;
      dupNumSearchKey.phoneNumber = details.phoneNumber;
      dupNumSearchKey.typeCode = details.typeCode;
      dupNumSearchKey.phoneExtension = details.phoneExtension;
      dupNumSearchKey.statusCode = RECORDSTATUS.NORMAL;
      final PhoneRMDtlsList phoneRMDtlsList =
        concernRolePhoneNumberObj.searchPhoneForDuplicate(dupNumSearchKey);

      for (int i = 0; i < phoneRMDtlsList.dtls.size(); ++i) {
        if (phoneRMDtlsList.dtls.item(
          i).concernRolePhoneNumberID != details.concernRolePhoneNumberID) {
          final AppException e1 = new AppException(
            BPOMAINTAINCONCERNROLEPHONE.ERR_PHONENUMBER_XFV_DUPLICATE);
          e1.arg(details.phoneNumber);
          e1.arg(concernRoleDtls.concernRoleName);
          ValidationManagerFactory.getManager().throwWithLookup(e1, "a", 1);
        }
      }

      concernRolePhoneNumberKey.concernRolePhoneNumberID =
        details.concernRolePhoneNumberID;
      final ConcernRolePhoneNumberDtls concernRolePhoneNumberReadDtls =
        concernRolePhoneNumberObj.read(concernRolePhoneNumberKey);
      if (!details.primaryPhoneInd
        && concernRolePhoneNumberReadDtls.phoneNumberID == concernRoleDtls.primaryPhoneNumberID) {
        ValidationManagerFactory.getManager()
          .throwWithLookup(new AppException(
            GENERALCONCERN.ERR_PRIMARYPHONENUMBER_MODIFY_FAILURE), "a", 1);
      }

      concernRolePhoneNumberDtls.assign(details);
      concernRolePhoneNumberDtls.phoneNumberID =
        concernRolePhoneNumberReadDtls.phoneNumberID;
      concernRolePhoneNumberObj.modify(concernRolePhoneNumberKey,
        concernRolePhoneNumberDtls);
      phoneNumberKey.phoneNumberID = concernRolePhoneNumberDtls.phoneNumberID;
      final PhoneNumberDtls phoneNumberDtls =
        phoneNumberObj.read(phoneNumberKey);
      modifyPhoneNumberDtls.assign(phoneNumberDtls);
      modifyPhoneNumberDtls.assign(details);
      modifyPhoneNumberDtls.phoneNumberID = phoneNumberKey.phoneNumberID;
      modifyPhoneNumberDtls.statusCode = phoneNumberDtls.statusCode;
      phoneNumberObj.modify(phoneNumberKey, modifyPhoneNumberDtls);
      if (details.primaryPhoneInd) {
        concernRoleDtls.primaryPhoneNumberID =
          concernRolePhoneNumberDtls.phoneNumberID;
        final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
        eiEvidenceKey.evidenceID = concernRoleKey.concernRoleID;
        eiEvidenceKey.evidenceType = CASEEVIDENCE.CONCERNROLE;
        final EvidenceDescriptorModifyDtls evidenceDescriptorModifyDtls =
          new EvidenceDescriptorModifyDtls();
        evidenceDescriptorModifyDtls.receivedDate = Date.getCurrentDate();
        final EIEvidenceModifyDtls eiEvidenceModifyDtls =
          new EIEvidenceModifyDtls();
        eiEvidenceModifyDtls.descriptor.assign(evidenceDescriptorModifyDtls);
        eiEvidenceModifyDtls.parentKey.evidenceID = 0L;
        eiEvidenceModifyDtls.evidenceObject = concernRoleDtls;
        evidenceControllerObj.modifyEvidence(eiEvidenceKey,
          eiEvidenceModifyDtls);
      }

    }
  }

  private void checkClientSecurity(final ConcernRoleKey key)
    throws AppException, InformationalException {

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();
    participantSecurityCheckKey.participantID = key.concernRoleID;
    participantSecurityCheckKey.type = LOCATIONACCESSTYPE.MAINTAIN;
    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkParticipantSecurity(participantSecurityCheckKey);
    if (!dataBasedSecurityResult.result) {
      if (dataBasedSecurityResult.readOnly) {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
      } else if (dataBasedSecurityResult.restricted) {
        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      } else {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
      }
    }
  }

  private void checkParticipantSensitivity(final ParticipantKeyStruct key)
    throws AppException, InformationalException {

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();
    participantSecurityCheckKey.participantID = key.participantID;
    participantSecurityCheckKey.type = LOCATIONACCESSTYPE.MAINTAIN;
    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkParticipantSecurity(participantSecurityCheckKey);
    if (!dataBasedSecurityResult.result) {
      if (dataBasedSecurityResult.readOnly) {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
      } else if (dataBasedSecurityResult.restricted) {
        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      } else {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
      }
    }
  }

  private void validateConcernRolePhone(final ParticipantPhoneDetails details)
    throws AppException, InformationalException {

    if (details.phoneNumber.length() == 0) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERN_FV_PHONE_EMPTY), "a", 0);
    }

    if (details.startDate.isZero()) {
      ValidationManagerFactory.getManager().throwWithLookup(
        new AppException(GENERAL.ERR_GENERAL_FV_FROM_DATE_EMPTY), "a", 1);
    }

    if (!details.endDate.isZero()
      && details.startDate.after(details.endDate)) {
      ValidationManagerFactory.getManager().throwWithLookup(
        new AppException(GENERAL.ERR_GENERAL_XFV_FROM_DATE_TO_DATE), "a", 1);
    }

    if (details.typeCode.length() == 0) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERN_FV_TYPE_EMPTY), "a", 0);
    }

  }

  private CuramInd validate(final ParticipantPhoneDetails details)
    throws AppException, InformationalException {

    final CuramInd curamInd = new CuramInd();
    boolean primaryInd = false;
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final Date todaysDate = Date.getCurrentDate();
    concernRoleKey.concernRoleID = details.concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleKey);
    primaryInd = details.primaryPhoneInd;
    if (concernRoleDtls.primaryPhoneNumberID == 0L) {
      primaryInd = true;
    } else if (primaryInd && !details.endDate.isZero()
      && details.endDate.before(todaysDate)) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEEPHONE_FV_NON_PRIMARY_DATE_PASSED),
        "a", 1);
    }

    AppException e;
    if (!CONCERNROLETYPE.PERSON.equals(concernRoleDtls.concernRoleType)
      && !CONCERNROLETYPE.PROSPECTPERSON
        .equals(concernRoleDtls.concernRoleType)
      && !CONCERNROLETYPE.REPRESENTATIVE
        .equals(concernRoleDtls.concernRoleType)) {
      if (!concernRoleDtls.registrationDate.isZero()
        && concernRoleDtls.registrationDate.after(details.startDate)) {
        e = new AppException(
          GENERALCONCERN.ERR_CONCERNROLE_XRV_NONPERSON_START_DATE);
        e.arg(concernRoleDtls.concernRoleName);
        e.arg(concernRoleDtls.registrationDate);
        ValidationManagerFactory.getManager().throwWithLookup(e, "a", 1);
      }
    } else if (!concernRoleDtls.startDate.isZero()
      && concernRoleDtls.startDate.after(details.startDate)
      && !CONCERNROLETYPE.REPRESENTATIVE
        .equals(concernRoleDtls.concernRoleType)) {
      e = new AppException(
        GENERALCONCERN.ERR_CONCERNROLE_XRV_PERSON_START_DATE);
      e.arg(concernRoleDtls.concernRoleName);
      e.arg(concernRoleDtls.startDate);
      ValidationManagerFactory.getManager().throwWithLookup(e, "a", 1);
    }

    if (!concernRoleDtls.endDate.isZero()) {
      if (details.startDate.after(concernRoleDtls.endDate)) {
        e = new AppException(
          GENERALCONCERN.ERR_CONCERNROLE_XRV_START_DATE_END_DATE);
        e.arg(concernRoleDtls.concernRoleName);
        e.arg(concernRoleDtls.endDate);
        ValidationManagerFactory.getManager().throwWithLookup(e, "a", 1);
      }

      if (!details.endDate.isZero()
        && details.endDate.after(concernRoleDtls.endDate)) {
        e = new AppException(GENERALCONCERN.ERR_CONCERNROLE_XRV_END_DATE);
        e.arg(concernRoleDtls.concernRoleName);
        e.arg(concernRoleDtls.endDate);
        ValidationManagerFactory.getManager().throwWithLookup(e, "a", 2);
      }
    }

    curamInd.statusInd = primaryInd;
    return curamInd;
  }
}
