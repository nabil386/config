package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.fact.BDMRepresentativeFactory;
import curam.ca.gc.bdm.entity.intf.BDMRepresentative;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLESTATUS;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.AllocateParticipantID;
import curam.core.impl.CuramConst;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleAlternateID;
import curam.core.intf.UniqueID;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.ValidationManager;
import curam.core.sl.struct.RepresentativeRegistrationDetails;
import curam.core.struct.AlternateIDTypeCodeKey;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleAlternateReadKey;
import curam.core.struct.DupConcernRoleAltIDDetailsList;
import curam.core.struct.SearchCRTypeAltIDType;
import curam.message.BPOMAINTAINCONCERNROLEALTID;
import curam.message.BPOPERSONREGISTRATION;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.MultipleRecordException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.FrequencyPattern;
import java.util.Map;

public class Representative extends curam.ca.gc.bdm.sl.base.Representative {

  @Inject
  private Map<String, AllocateParticipantID> allocateParticipantIDMap;

  /**
   * Add injection.
   */
  public Representative() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void registerRepresentative(
    final RepresentativeRegistrationDetails representativeRegistrationDetails)
    throws AppException, InformationalException {

    // variable used to insert representative
    final curam.core.sl.entity.intf.Representative representativeObj =
      curam.core.sl.entity.fact.RepresentativeFactory.newInstance();

    final ConcernRoleAlternateReadKey concernRoleAlternateReadKey =
      new ConcernRoleAlternateReadKey();

    // ConcernRoleAlternateID maintenance object
    final ConcernRoleAlternateID concernRoleAlternateIDObj =
      ConcernRoleAlternateIDFactory.newInstance();

    ConcernRoleAlternateIDDtls concernRoleAlternateIDDtls =
      new ConcernRoleAlternateIDDtls();

    // Unique id generator class
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

    // current date
    final Date currentDate = Date.getCurrentDate();

    // BEGIN, CR00278802, KRK
    boolean altIDExist = true;

    // END, CR00278802

    if (representativeRegistrationDetails.representativeDtls.alternateID
      .length() == 0) {

      // Concern role maintenance object
      final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();

      // declaration of variables to prevent duplicate alternative ID's
      final AlternateIDTypeCodeKey alternateIDTypeCodeKey =
        new AlternateIDTypeCodeKey();
      boolean recordNotFound;

      // Alternate representative reference number
      // BEGIN, CR00049218, GM
      String alternateRepresentativeRefNo = CuramConst.gkEmpty;

      // END, CR00049218

      // validation to prevent duplicate alternative ID's
      while (altIDExist) {

        // BEGIN, CR00312286, SG
        alternateRepresentativeRefNo =
          allocateParticipantIDMap.get("REPRESENTATIVE").getNextID();
        // END, CR00312286

        // BEGIN, CR00228935, PB
        // BEGIN, CR00249213, NS
        alternateIDTypeCodeKey.typeCode =
          CONCERNROLEALTERNATEID.SERVICE_SUPPLIER_REFERENCE_NUMBER;
        // END, CR00249213
        // END, CR00228935
        alternateIDTypeCodeKey.alternateID = alternateRepresentativeRefNo;
        alternateIDTypeCodeKey.statusCode = RECORDSTATUS.NORMAL;

        recordNotFound = false;

        // BEGIN, CR00394323, PMD
        final ValidationManager validationManagerObj =
          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager();

        if (validationManagerObj.enabled(
          BPOMAINTAINCONCERNROLEALTID.ERR_CONCERNROLEALTID_XRV_ID_TYPE_CONCERNTYPE_OVERLAP,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          11)) {

          final ConcernRoleAlternateID concernRoleAltIDObj =
            ConcernRoleAlternateIDFactory.newInstance();

          final SearchCRTypeAltIDType searchCRTypeAltIDType =
            new SearchCRTypeAltIDType();

          searchCRTypeAltIDType.alternateID = alternateRepresentativeRefNo;
          searchCRTypeAltIDType.alternateIDType =
            CONCERNROLEALTERNATEID.SERVICE_SUPPLIER_REFERENCE_NUMBER;
          searchCRTypeAltIDType.statusCode = RECORDSTATUS.NORMAL;
          searchCRTypeAltIDType.concernRoleType =
            CONCERNROLETYPE.REPRESENTATIVE;

          final DupConcernRoleAltIDDetailsList dupConcernRoleAltIDDetailsList =
            concernRoleAltIDObj
              .searchByCRTypeAltIDAndType(searchCRTypeAltIDType);

          if (dupConcernRoleAltIDDetailsList.dtls.size() > 0) {

            final AppException ae = new AppException(
              BPOMAINTAINCONCERNROLEALTID.ERR_CONCERNROLEALTID_XRV_ID_TYPE_CONCERNTYPE_OVERLAP);

            ae.arg(CodeTable.getOneItem(CONCERNROLETYPE.TABLENAME,
              CONCERNROLETYPE.REPRESENTATIVE));
            curam.core.sl.infrastructure.impl.ValidationManagerFactory
              .getManager().throwWithLookup(ae,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
                11);
          } else {
            recordNotFound = true;
          }
        } else {
          recordNotFound = true;
        }
        // END, CR00394323

        // BEGIN, CR00394029, GD
        if (validationManagerObj.enabled(
          BPOPERSONREGISTRATION.ERR_PERSON_SSN_ALREADY_EXISTS,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          4)) {

          try {

            concernRoleAlternateIDDtls = concernRoleAlternateIDObj
              .readByAltIDTypeCode(alternateIDTypeCodeKey);

            if (representativeRegistrationDetails.representativeDtls.alternateID
              .length() > 0
              && representativeRegistrationDetails.representativeDtls.alternateID
                .equals(concernRoleAlternateIDDtls.alternateID)) {
              final AppException e = new AppException(
                BPOPERSONREGISTRATION.ERR_PERSON_SSN_ALREADY_EXISTS);

              e.arg(
                representativeRegistrationDetails.representativeDtls.alternateID);

              curam.core.sl.infrastructure.impl.ValidationManagerFactory
                .getManager().throwWithLookup(e,
                  curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
                  4);
            }

          } catch (final MultipleRecordException e) {// do nothing
          } catch (final RecordNotFoundException e) {
            recordNotFound = true;
          }
        } else {
          recordNotFound = true;
        }
        // END, CR00394029
        if (recordNotFound) {

          concernRoleAlternateReadKey.primaryAlternateID =
            alternateRepresentativeRefNo;
          concernRoleAlternateReadKey.statusCode =
            CONCERNROLESTATUS.DEFAULTCODE;

          try {
            concernRoleObj.readByAlternateID(concernRoleAlternateReadKey);
          } catch (final RecordNotFoundException e) {
            altIDExist = false;
          }
        }

      }

      // Generate unique id for representative
      representativeRegistrationDetails.representativeDtls.alternateID =
        alternateRepresentativeRefNo;
    }
    // based on domain FREQUENCY_PATTERN
    // calculate the next payment date if payment frequency has been entered
    if (representativeRegistrationDetails.representativeDtls.paymentFrequency
      .length() != 0) {
      // BEGIN, CR00049218, GM
      String frequencyPattern = CuramConst.gkEmpty;

      // END, CR00049218

      frequencyPattern =
        representativeRegistrationDetails.representativeDtls.paymentFrequency;
      representativeRegistrationDetails.representativeDtls.nextPaymentDate =
        new FrequencyPattern(frequencyPattern).getNextOccurrence(currentDate);
    }

    // insert representative
    /*
     * representativeObj.insert(
     * representativeRegistrationDetails.representativeDtls,
     * representativeRegistrationDetails.representativeRegistrationDetails);
     */

    // calling customized insert method to avoid null address and phone number
    // record

    final BDMRepresentative bdmRepresentative =
      BDMRepresentativeFactory.newInstance();
    bdmRepresentative.insert(
      representativeRegistrationDetails.representativeDtls,
      representativeRegistrationDetails.representativeRegistrationDetails);

    // BEGIN, CR00099256, KH
    Date startDate =
      representativeRegistrationDetails.representativeRegistrationDetails.startDate;

    // If the start date has not been specified, set it to today
    if (startDate.equals(Date.kZeroDate)) {
      startDate = currentDate;
    }
    // END, CR00099256

    concernRoleAlternateIDDtls.concernRoleAlternateID =
      uniqueIDObj.getNextID();
    concernRoleAlternateIDDtls.concernRoleID =
      representativeRegistrationDetails.representativeDtls.concernRoleID;
    concernRoleAlternateIDDtls.alternateID =
      representativeRegistrationDetails.representativeDtls.alternateID;
    // BEGIN, CR00228935, PB
    // BEGIN, CR00249213, NS
    // BEGIN, CR00278802, KRK
    if (!altIDExist) {
      concernRoleAlternateIDDtls.typeCode =
        CONCERNROLEALTERNATEID.REFERENCE_NUMBER;
    } else {
      concernRoleAlternateIDDtls.typeCode =
        CONCERNROLEALTERNATEID.DEFAULTCODE;
    }
    // END, CR00278802
    // END, CR00249213
    // END, CR00228935

    // BEGIN, CR00099256, KH
    concernRoleAlternateIDDtls.startDate = startDate;
    concernRoleAlternateIDDtls.endDate = Date.kZeroDate;
    // END, CR00099256
    concernRoleAlternateIDDtls.statusCode =
      curam.codetable.RECORDSTATUS.NORMAL;
    // BEGIN, CR00049218, GM
    concernRoleAlternateIDDtls.comments = CuramConst.gkEmpty;
    // END, CR00049218

    // insert concern role alternate id record
    // BEGIN, CR00059688, POH
    // Call the EvidenceController object and insert evidence

    // Evidence descriptor details
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    evidenceDescriptorInsertDtls.participantID =
      concernRoleAlternateIDDtls.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType =
      CASEEVIDENCE.CONCERNROLEALTERNATEID;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    // Evidence Interface details
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID =
      concernRoleAlternateIDDtls.concernRoleID;
    eiEvidenceInsertDtls.evidenceObject = concernRoleAlternateIDDtls;

    // EvidenceController business object
    final curam.core.sl.infrastructure.impl.EvidenceControllerInterface evidenceControllerObj =
      (curam.core.sl.infrastructure.impl.EvidenceControllerInterface) curam.core.sl.infrastructure.fact.EvidenceControllerFactory
        .newInstance();

    // Insert the evidence
    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
    // END, CR00059688

  }

}
