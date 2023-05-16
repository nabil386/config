package curam.ca.gc.bdm.pdc.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.fact.BDMPhoneNumberFactory;
import curam.ca.gc.bdm.entity.intf.BDMPhoneNumber;
import curam.ca.gc.bdm.entity.struct.BDMPhoneNumberDtls;
import curam.ca.gc.bdm.entity.struct.BDMPhoneNumberKey;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberDetails;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberKey;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRolePhoneNumberFactory;
import curam.core.fact.PhoneNumberFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.ConcernRolePhoneNumber;
import curam.core.intf.PhoneNumber;
import curam.core.intf.UniqueID;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.ClientMerge;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRolePhoneDetails;
import curam.core.struct.ConcernRolePhoneNumberDtls;
import curam.core.struct.ConcernRolePhoneNumberDtlsList;
import curam.core.struct.ConcernRolePhoneNumberKey;
import curam.core.struct.PhoneNumberDtls;
import curam.core.struct.PhoneNumberKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.message.PDCEVIDENCEREMOVALVALIDATIONS;
import curam.pdc.impl.PDCPhoneNumber;
import curam.pdc.impl.PDCPhoneNumberReplicator;
import curam.pdc.impl.PDCPhoneNumberReplicatorExtender;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BDMPDCPhoneNumberReplicatorImpl
  implements PDCPhoneNumberReplicator {

  @Inject
  private Set<PDCPhoneNumberReplicatorExtender> pdcPhoneNumberReplicatorExtenders;

  protected BDMPDCPhoneNumberReplicatorImpl() {

    GuiceWrapper.getInjector().injectMembers(this);

  }

  @Override
  public List<ConcernRolePhoneNumberKey> findLegacyRecordsToUpdate(
    final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    final List<ConcernRolePhoneNumberKey> concernRolePhoneNumberKeyList =
      new ArrayList();
    final ParticipantPhoneDetails participantPhoneDetails =
      new ParticipantPhoneDetails();
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
    eiEvidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;
    this.assignDynamicEvidenceToDetails(dynamicEvidenceDataDetails,
      participantPhoneDetails);
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = evidenceDescriptorDtls.participantID;
    final ConcernRolePhoneNumberDtlsList concernRolePhoneNumberDtlsList =
      concernRolePhoneNumberObj.searchByConcernRole(concernRoleKey);
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();
    final Iterator var14 = concernRolePhoneNumberDtlsList.dtls.iterator();

    while (var14.hasNext()) {
      final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
        (ConcernRolePhoneNumberDtls) var14.next();
      phoneNumberKey.phoneNumberID = concernRolePhoneNumberDtls.phoneNumberID;
      final PhoneNumberDtls phoneNumberDtls =
        phoneNumberObj.read(phoneNumberKey);
      if (!phoneNumberDtls.statusCode.equals(RECORDSTATUS.CANCELLED)
        && concernRolePhoneNumberDtls.typeCode
          .equals(participantPhoneDetails.typeCode)
        && phoneNumberDtls.phoneNumber
          .equals(participantPhoneDetails.phoneNumber)
        && phoneNumberDtls.phoneAreaCode
          .equals(participantPhoneDetails.phoneAreaCode)
        && phoneNumberDtls.phoneExtension
          .equals(participantPhoneDetails.phoneExtension)
        && phoneNumberDtls.phoneCountryCode
          .equals(participantPhoneDetails.phoneCountryCode)) {
        final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
          new ConcernRolePhoneNumberKey();
        concernRolePhoneNumberKey.concernRolePhoneNumberID =
          concernRolePhoneNumberDtls.concernRolePhoneNumberID;
        concernRolePhoneNumberKeyList.add(concernRolePhoneNumberKey);
      }
    }

    return concernRolePhoneNumberKeyList;
  }

  @Override
  public void replicateInsertEvidence(
    final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    final ParticipantPhoneDetails participantPhoneDetails =
      new ParticipantPhoneDetails();
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
    eiEvidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;
    this.assignDynamicEvidenceToDetails(dynamicEvidenceDataDetails,
      participantPhoneDetails);
    participantPhoneDetails.concernRoleID =
      evidenceDescriptorDtls.participantID;
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      new ConcernRolePhoneNumberDtls();
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();
    final ConcernRolePhoneDetails concernRolePhoneDetails =
      new ConcernRolePhoneDetails();
    concernRolePhoneDetails.assign(participantPhoneDetails);
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    phoneNumberDtls.assign(concernRolePhoneDetails);
    phoneNumberObj.insert(phoneNumberDtls);

    // Task 21121 -BEGIN - use BDMPhoneCountry code table value to store
    // country code
    final BDMPhoneNumberDtls bdmPhoneNumberDtls = new BDMPhoneNumberDtls();
    bdmPhoneNumberDtls.bdmPhoneNumberID = uniqueIDObj.getNextID();
    bdmPhoneNumberDtls.phoneNumberID = phoneNumberDtls.phoneNumberID;
    bdmPhoneNumberDtls.phoneCountryCode =
      dynamicEvidenceDataDetails.getAttribute("phoneCountryCode").getValue();
    BDMPhoneNumberFactory.newInstance().insert(bdmPhoneNumberDtls);
    // Task 21121 -END- use BDMPhoneCountry code table value to store country
    // code

    concernRolePhoneNumberDtls.assign(concernRolePhoneDetails);
    concernRolePhoneNumberDtls.phoneNumberID = phoneNumberDtls.phoneNumberID;
    concernRolePhoneNumberDtls.concernRolePhoneNumberID =
      uniqueIDObj.getNextID();
    concernRolePhoneNumberObj.pdcInsert(concernRolePhoneNumberDtls);
    TransactionInfo.setFacadeScopeObject(PDCPhoneNumber.class.getName(),
      concernRolePhoneNumberDtls.concernRolePhoneNumberID);

  }

  @Override
  public void replicateModifyEvidence(
    final EvidenceDescriptorDtls evidenceDescriptorDtls,
    final EvidenceDescriptorDtls previousActiveEvidDescriptorDtls)
    throws AppException, InformationalException {

    final List<ConcernRolePhoneNumberKey> concernRolePhoneNumberKeyList =
      this.findLegacyRecordsToUpdate(previousActiveEvidDescriptorDtls);
    final Iterator var4 = concernRolePhoneNumberKeyList.iterator();

    while (var4.hasNext()) {
      final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
        (ConcernRolePhoneNumberKey) var4.next();
      this.modifyPhoneNumber(concernRolePhoneNumberKey,
        evidenceDescriptorDtls);
    }

  }

  @Override
  public void replicateRemoveEvidence(
    final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    this.validateEvidenceRemoval(evidenceDescriptorDtls);
    final List<ConcernRolePhoneNumberKey> concernRolePhoneNumberKeyList =
      this.findLegacyRecordsToUpdate(evidenceDescriptorDtls);
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final Iterator var7 = concernRolePhoneNumberKeyList.iterator();

    while (var7.hasNext()) {
      final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
        (ConcernRolePhoneNumberKey) var7.next();
      final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
        concernRolePhoneNumberObj.read(concernRolePhoneNumberKey);
      phoneNumberKey.phoneNumberID = concernRolePhoneNumberDtls.phoneNumberID;
      final PhoneNumberDtls phoneNumberDtls =
        phoneNumberObj.read(phoneNumberKey);
      phoneNumberDtls.statusCode = RECORDSTATUS.CANCELLED;
      phoneNumberObj.pdcModify(phoneNumberKey, phoneNumberDtls);
    }

  }

  private void assignDynamicEvidenceToDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final ParticipantPhoneDetails details)
    throws AppException, InformationalException {

    details.phoneCountryCode =
      dynamicEvidenceDataDetails.getAttribute("phoneCountryCode").getValue();
    details.phoneAreaCode =
      dynamicEvidenceDataDetails.getAttribute("phoneAreaCode").getValue();
    details.phoneNumber =
      dynamicEvidenceDataDetails.getAttribute("phoneNumber").getValue();
    details.phoneExtension =
      dynamicEvidenceDataDetails.getAttribute("phoneExtension").getValue();
    details.startDate = (Date) DynamicEvidenceTypeConverter
      .convert(dynamicEvidenceDataDetails.getAttribute("fromDate"));
    details.endDate = (Date) DynamicEvidenceTypeConverter
      .convert(dynamicEvidenceDataDetails.getAttribute("toDate"));
    details.typeCode =
      dynamicEvidenceDataDetails.getAttribute("phoneType").getValue();
    details.comments =
      dynamicEvidenceDataDetails.getAttribute("comments").getValue();
    final Iterator var3 = this.pdcPhoneNumberReplicatorExtenders.iterator();

    while (var3.hasNext()) {
      final PDCPhoneNumberReplicatorExtender pdcPhoneNumberReplicatorExtender =
        (PDCPhoneNumberReplicatorExtender) var3.next();
      pdcPhoneNumberReplicatorExtender.assignDynamicEvidenceToExtendedDetails(
        dynamicEvidenceDataDetails, details);
    }

  }

  private void modifyPhoneNumber(
    final ConcernRolePhoneNumberKey concernRolePhoneNumberKey,
    final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    final ParticipantPhoneDetails participantPhoneDetails =
      new ParticipantPhoneDetails();
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
    eiEvidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;
    this.assignDynamicEvidenceToDetails(dynamicEvidenceDataDetails,
      participantPhoneDetails);
    participantPhoneDetails.concernRoleID =
      evidenceDescriptorDtls.participantID;
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      new ConcernRolePhoneNumberDtls();
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();
    final PhoneNumberDtls modifyPhoneNumberDtls = new PhoneNumberDtls();
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberReadDtls =
      concernRolePhoneNumberObj.read(concernRolePhoneNumberKey);
    concernRolePhoneNumberDtls.assign(participantPhoneDetails);
    concernRolePhoneNumberDtls.phoneNumberID =
      concernRolePhoneNumberReadDtls.phoneNumberID;
    concernRolePhoneNumberDtls.versionNo =
      concernRolePhoneNumberReadDtls.versionNo;
    concernRolePhoneNumberDtls.concernRolePhoneNumberID =
      concernRolePhoneNumberKey.concernRolePhoneNumberID;
    concernRolePhoneNumberObj.pdcModify(concernRolePhoneNumberKey,
      concernRolePhoneNumberDtls);
    phoneNumberKey.phoneNumberID = concernRolePhoneNumberDtls.phoneNumberID;
    final PhoneNumberDtls phoneNumberDtls =
      phoneNumberObj.read(phoneNumberKey);
    modifyPhoneNumberDtls.assign(phoneNumberDtls);
    modifyPhoneNumberDtls.assign(participantPhoneDetails);
    modifyPhoneNumberDtls.phoneNumberID = phoneNumberKey.phoneNumberID;
    modifyPhoneNumberDtls.statusCode = phoneNumberDtls.statusCode;
    modifyPhoneNumberDtls.versionNo = phoneNumberDtls.versionNo;
    phoneNumberObj.pdcModify(phoneNumberKey, modifyPhoneNumberDtls);

    // Task 21121 -BEGIN - use BDMPhoneCountry code table value to store
    // country code
    final BDMPhoneNumber bdmPhoneNumberObj =
      BDMPhoneNumberFactory.newInstance();
    final ReadBDMPhoneNumberKey readBDMPhoneNumberKey =
      new ReadBDMPhoneNumberKey();
    readBDMPhoneNumberKey.phoneNumberID = phoneNumberKey.phoneNumberID;
    final ReadBDMPhoneNumberDetails readBDMPhoneNumberDetails =
      bdmPhoneNumberObj.readBDMPhoneNumber(readBDMPhoneNumberKey);
    final BDMPhoneNumberKey bdmPhoneNumberKey = new BDMPhoneNumberKey();
    bdmPhoneNumberKey.bdmPhoneNumberID =
      readBDMPhoneNumberDetails.bdmPhoneNumberID;
    final BDMPhoneNumberDtls modifyBDMPhoneNumberDtls =
      new BDMPhoneNumberDtls();
    modifyBDMPhoneNumberDtls.assign(readBDMPhoneNumberDetails);
    modifyBDMPhoneNumberDtls.phoneCountryCode =
      dynamicEvidenceDataDetails.getAttribute("phoneCountryCode").getValue();
    bdmPhoneNumberObj.modify(bdmPhoneNumberKey, modifyBDMPhoneNumberDtls);
    // Task 21121 -END- use BDMPhoneCountry code table value to store country
    // code
  }

  private void validateEvidenceRemoval(
    final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = evidenceDescriptorDtls.participantID;
    if (clientMergeObj.isConcernRoleDuplicate(concernRoleKey).statusInd) {
      ValidationManagerFactory.getManager().throwWithLookup(new AppException(
        PDCEVIDENCEREMOVALVALIDATIONS.ERR_PHONE_NUMBER_XRV_DUPLICATE_CLIENT_DELETE),
        "a", 0);
    }

  }
}
