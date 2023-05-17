package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMPersonMatchConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.integritycheck.fact.BDMIntegrityCheckFactory;
import curam.ca.gc.bdm.facade.integritycheck.struct.BDMIntegrityCheckKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdm.sl.struct.BDMPersonIdentificationDetails;
import curam.ca.gc.bdm.sl.struct.BDMPersonKey;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckConstants;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckUtil;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLETYPE;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.struct.ConcernRoleKey;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.PersonDAO;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.struct.PDCEvidenceDetails;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.fact.ParticipantDataCaseFactory;
import curam.pdc.impl.PDCConst;
import curam.piwrapper.participantmanager.impl.ConcernRoleAddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.workspaceservices.util.impl.LogHelper;
import java.util.List;

/**
 * This is service layer class which creates or modifies the Date Of birth,
 * Person Names or the SIN Identification number.
 *
 *
 * @author vaibhav.patil
 *
 */
public class BDMPDCPerson extends curam.ca.gc.bdm.sl.base.BDMPDCPerson {

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleAddressDAO concernRoleAddressDAO;

  @Inject
  LogHelper logHelper;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvidence;

  @Inject
  BDMNamesEvidence bdmNamesEvidence;

  @Inject
  BDMDateofBirthEvidence bdmDateofBirthEvidence;

  private final BDMUtil bdmUtil = new BDMUtil();

  protected BDMPDCPerson() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method updates and/or creates the SIN Identification evidence, Date of
   * Birth and the Names.
   * TASK 26338
   *
   */
  @Override
  public void changeIdentificationInfoDetails(
    final BDMPersonIdentificationDetails details)
    throws AppException, InformationalException {

    boolean sinUpdated = false;
    boolean nameUpdated = false;
    boolean dobUpdated = false;

    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = details.concernRoleID;
    final long concernRoleID = bdmPersonKey.concernRoleID;

    TransactionInfo.setFacadeScopeObject(BDMConstants.INVOKED_FROM_EDIT,
      false);

    final BDMPersonIdentificationDetails identificationDetails =
      readPersonIdentificationDetails(bdmPersonKey);

    // New Incoming Sin Number
    final String newSinNumber = details.sinNumber;
    if (!StringUtil.isNullOrEmpty(newSinNumber)) {

      final long sinEvidenceID =
        identificationDetails.identificationEvidenceID;

      // Removeing below as it is nolonger used
      // final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVOCurrent =
      // bdmIdentificationEvidence.getSINEvidenceValueObject(concernRoleID);

      final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVOUpdate =
        new BDMIdentificationEvidenceVO();
      bdmIdentificationEvidenceVOUpdate.setAlternateID(newSinNumber);
      bdmIdentificationEvidenceVOUpdate
        .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);

      bdmIdentificationEvidenceVOUpdate.setPreferredInd(true);
      if (sinEvidenceID != 0) {
        bdmIdentificationEvidenceVOUpdate.setEvidenceID(sinEvidenceID);
      } else {
        bdmIdentificationEvidenceVOUpdate.setEvidenceID(0);
      }
      bdmIdentificationEvidenceVOUpdate
        .setFromDate(details.sinIdentificationStartDate);
      bdmIdentificationEvidenceVOUpdate
        .setToDate(details.sinIdentificationEndDate);

      // Remove SIN SIR Response evidence if it exists.
      new BDMSINIntegrityCheckUtil()
        .removeExistingSINSIRResponceEvidence(concernRoleID);

      bdmIdentificationEvidence.createSINIdentificationEvidence(concernRoleID,
        details.sinIdentificationExistingEndDate,
        bdmIdentificationEvidenceVOUpdate, details.evidenceChangeReason,
        identificationDetails.sinIdentificationExistingStartDate);

      sinUpdated = true;
    }

    // If the names changes then only update the evidence
    if (!(details.firstForename.equals(identificationDetails.firstForename)
      && details.middleName.equals(identificationDetails.middleName)
      && details.lastName.equals(identificationDetails.lastName))) {

      final String firstname = details.firstForename;
      final String middlename = details.middleName;
      final String lastname = details.lastName;
      final long nameEvidenceID = identificationDetails.nameEvidenceID;
      final BDMNamesEvidenceVO bdmNamesEvidenceVO = new BDMNamesEvidenceVO();
      bdmNamesEvidenceVO.setFirstName(firstname);
      bdmNamesEvidenceVO.setMiddleName(middlename);
      bdmNamesEvidenceVO.setLastName(lastname);
      bdmNamesEvidenceVO.setNameType(ALTERNATENAMETYPE.REGISTERED);
      if (nameEvidenceID != 0) {
        bdmNamesEvidenceVO.setEvidenceID(nameEvidenceID);
      }

      bdmNamesEvidence.createNamesEvidence(concernRoleID, bdmNamesEvidenceVO,
        details.evidenceChangeReason);

      nameUpdated = true;
    }

    // If the date or birth or last name at birth changes, then update the
    // evidence
    if (!(details.dateOfBirth.equals(identificationDetails.dateOfBirth)
      && details.parentLastNameAtBirth
        .equals(identificationDetails.parentLastNameAtBirth)
      && details.lastNameAtBirth
        .equals(identificationDetails.lastNameAtBirth))) {

      final long dobEvidenceID = identificationDetails.dobEvidenceID;

      final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO =
        new BDMDateofBirthEvidenceVO();
      bdmDateofBirthEvidenceVO.setBirthLastName(details.lastNameAtBirth);
      bdmDateofBirthEvidenceVO
        .setMothersBirthLastName(details.parentLastNameAtBirth);
      bdmDateofBirthEvidenceVO.setDateOfBirth(details.dateOfBirth);
      // Added Attestation
      bdmDateofBirthEvidenceVO.setAttestationDate(Date.getCurrentDate());
      bdmDateofBirthEvidenceVO
        .setBdmAgreeAttestation(details.attestationAgree);// BDMAGREEATTESTATION.YES);

      bdmDateofBirthEvidenceVO.setComments(BDMConstants.EMPTY_STRING);
      if (dobEvidenceID != 0) {
        bdmDateofBirthEvidenceVO.setEvidenceID(dobEvidenceID);

        // Added Attestation
        final List<BDMDateofBirthEvidenceVO> bdmDateofBirthEvidenceVOList =
          bdmDateofBirthEvidence.getDOBEvidenceValueObject(concernRoleID);
        if (!bdmDateofBirthEvidenceVOList.isEmpty()) {
          for (final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVOIn : bdmDateofBirthEvidenceVOList) {
            if (bdmDateofBirthEvidenceVOIn.getEvidenceID() == dobEvidenceID) {
              if (bdmDateofBirthEvidenceVOIn
                .getAttesteeCaseParticipant() != 0l) {
                bdmDateofBirthEvidenceVO.setAttesteeCaseParticipant(
                  bdmDateofBirthEvidenceVOIn.getAttesteeCaseParticipant());
              } else {
                final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
                concernRoleKey.concernRoleID = concernRoleID;

                final long participantDataCaseID =
                  ParticipantDataCaseFactory.newInstance()
                    .getParticipantDataCase(concernRoleKey).caseID;
                final long caseParticipantRoleID =
                  bdmUtil.getCaseParticipantRoleID(participantDataCaseID,
                    concernRoleID).caseParticipantRoleID;
                bdmDateofBirthEvidenceVO
                  .setAttesteeCaseParticipant(caseParticipantRoleID);
              }
              bdmDateofBirthEvidenceVO
                .setPerson(bdmDateofBirthEvidenceVOIn.getPerson());
              break;
            }
          }
        } else {
          final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
          concernRoleKey.concernRoleID = concernRoleID;

          final long participantDataCaseID = ParticipantDataCaseFactory
            .newInstance().getParticipantDataCase(concernRoleKey).caseID;
          final long caseParticipantRoleID =
            bdmUtil.getCaseParticipantRoleID(participantDataCaseID,
              concernRoleID).caseParticipantRoleID;
          bdmDateofBirthEvidenceVO
            .setAttesteeCaseParticipant(caseParticipantRoleID);
        }

      }
      bdmDateofBirthEvidence.createDateOfBirthEvidence(concernRoleID,
        bdmDateofBirthEvidenceVO, details.evidenceChangeReason);

    }

    // BUG 97736 - START
    if (!details.dateOfBirth.equals(identificationDetails.dateOfBirth)) {
      dobUpdated = true;
    }
    // BUG 97736 - END

    // Calling SIN Integrity Check process when Name/SIN or
    // DOB evidence changes. This will not be called for the Prospect Person.
    if (sinUpdated || nameUpdated || dobUpdated) {
      final BDMIntegrityCheckKey integrityCheckKey =
        new BDMIntegrityCheckKey();
      integrityCheckKey.concernRoleID = concernRoleID;
      BDMIntegrityCheckFactory.newInstance()
        .sinIntegrityCheckOnPerson(integrityCheckKey);
    }

    // START 113265 Change Identification Information - R2
    // modify evidence for receivedFrom, Received From Country and Mode Of
    // receipt
    bdmUtil.updateEvidenceAttributes(details.bdmReceivedFrom,
      details.countryCode, details.concernRoleID);
    // END 113265 Change Identification Information - R2

  }

  /**
   * This method reads the Person Identification related data.
   * TASK 26338
   */
  @Override
  public BDMPersonIdentificationDetails readPersonIdentificationDetails(
    final BDMPersonKey key) throws AppException, InformationalException {

    final BDMPersonIdentificationDetails identificationDetails =
      new BDMPersonIdentificationDetails();

    final PersonAndEvidenceTypeList pdcEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    pdcEvidenceTypeList.concernRoleID = key.concernRoleID;
    pdcEvidenceTypeList.evidenceTypeList = PDCConst.PDCIDENTIFICATION + "|"
      + PDCConst.PDCNAME + "|" + PDCConst.PDCBIRTHANDDEATH;

    identificationDetails.concernRoleID = key.concernRoleID;

    // START 113265
    identificationDetails.receivedDate = Date.getCurrentDate();
    // END 113265

    final PDCEvidenceDetailsList pdcEvidenceDetailsList =
      PDCPersonFactory.newInstance()
        .listCurrentParticipantEvidenceByTypes(pdcEvidenceTypeList);

    for (final PDCEvidenceDetails pdcEvidence : pdcEvidenceDetailsList.list) {
      if (pdcEvidence.evidenceType.equals(PDCConst.PDCIDENTIFICATION)) {

        final EIEvidenceReadDtls readDtls = BDMEvidenceUtil
          .readEvidence(pdcEvidence.evidenceType, pdcEvidence.evidenceID);

        if (CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER
          .equals(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMPersonMatchConstants.ALT_ID_TYPE))) {
          final String endDate = BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMPersonMatchConstants.TO_DATE);

          if (!endDate.isEmpty() && !Date.fromISO8601(endDate).isZero()
            && Date.fromISO8601(endDate).before(Date.getCurrentDate())) {
            continue;
          }

          identificationDetails.sinIdentificationExistingEndDate =
            endDate.isEmpty() ? Date.kZeroDate
              : Date.fromISO8601(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
                BDMPersonMatchConstants.TO_DATE));

          // Task 93103 - Start
          final String startDate = BDMEvidenceUtil
            .getDynEvdAttrValue(readDtls, BDMPersonMatchConstants.FROM_DATE);

          identificationDetails.sinIdentificationExistingStartDate =
            startDate.isEmpty() ? Date.kZeroDate
              : Date.fromISO8601(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
                BDMPersonMatchConstants.FROM_DATE));
          //// Task 93103 - End

          identificationDetails.existingSINNumber =
            BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
              BDMPersonMatchConstants.ALTERNATE_ID);
          identificationDetails.identificationEvidenceID =
            pdcEvidence.evidenceID;

          // Task 99491 - Start
          identificationDetails.doesSINNumberExist =
            StringUtil.isNullOrEmpty(identificationDetails.existingSINNumber)
              ? Boolean.FALSE : Boolean.TRUE;
          // Task 99491 - End

          continue;
        }

      }

      if (pdcEvidence.evidenceType.equals(PDCConst.PDCNAME)) {

        final EIEvidenceReadDtls readDtls = BDMEvidenceUtil
          .readEvidence(pdcEvidence.evidenceType, pdcEvidence.evidenceID);

        if (ALTERNATENAMETYPE.REGISTERED
          .equals(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_NAMETYPE))) {

          identificationDetails.firstForename =
            BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
              BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_FIRSTNAME);

          identificationDetails.middleName =
            BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
              BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_MIDDLENAME);

          identificationDetails.lastName =
            BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
              BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_LASTNAME);
          identificationDetails.nameEvidenceID = pdcEvidence.evidenceID;
          identificationDetails.concernRoleName =
            identificationDetails.firstForename + " "
              + identificationDetails.lastName;
          continue;

        }

      }

      if (pdcEvidence.evidenceType.equals(PDCConst.PDCBIRTHANDDEATH)) {

        final EIEvidenceReadDtls readDtls = BDMEvidenceUtil
          .readEvidence(pdcEvidence.evidenceType, pdcEvidence.evidenceID);

        identificationDetails.dateOfBirth =
          Date.fromISO8601(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_DATEOFBIRTH));

        identificationDetails.lastNameAtBirth =
          BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_BIRTHLASTNAME);

        identificationDetails.parentLastNameAtBirth =
          BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_MOTHERBIRTHLASTNAME);

        identificationDetails.dobEvidenceID = pdcEvidence.evidenceID;

        continue;

      }

    }

    return identificationDetails;
  }

  /**
   * Method checks for the Prospect Person
   *
   * @param concernRoleID
   * @return
   */
  private boolean isProspectPerson(final long concernRoleID) {

    return concernRoleDAO.get(concernRoleID).getConcernRoleType().getCode()
      .equals(CONCERNROLETYPE.PROSPECTPERSON) ? true : false;
  }

}
