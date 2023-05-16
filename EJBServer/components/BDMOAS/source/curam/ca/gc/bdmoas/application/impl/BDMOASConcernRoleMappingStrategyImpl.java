package curam.ca.gc.bdmoas.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMConcernRoleMappingStrategyImpl;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.codetable.impl.BDMMARITALSTATUSEntry;
import curam.ca.gc.bdm.codetable.impl.BDMRECEIVEDFROMEntry;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAConcernRoleKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONFORM;
import curam.ca.gc.bdmoas.evidencemapping.util.impl.BDMOASEvidenceMappingUtil;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASMaritalStatusEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASMaritalStatusEvidenceVO;
import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.BANKACCOUNTTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.COUNTRY;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CONCERNROLEALTERNATEIDEntry;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.core.impl.EnvVars;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.participant.impl.ConcernRole;
import curam.pdc.fact.PDCBankAccountFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.ParticipantBankAccountDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * BDMOAS FEATURE 92921: Class Added
 * Custom BDMOAS mapping which occurs during application submission, after the
 * ConcernRole is created.
 *
 * @author SMisal
 *
 */
public class BDMOASConcernRoleMappingStrategyImpl
  extends BDMConcernRoleMappingStrategyImpl {

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvd;

  @Inject
  private BDMUtil bdmUtil;

  @Inject
  private BDMOASApplicationUtil bdmoasApplcnUtil;

  @Inject
  private BDMOASEvidenceMappingUtil bdmoasEvidenceUtil;

  /**
   * BDM OAS concern role mappings which get called during the application
   * submission process _after_ the person has been registered.
   *
   * @param personEntity
   * Data store entity reference
   * @param concernRole
   * Target concern role
   *
   * @throws InformationalException
   * @throws AppException
   */
  @Override
  public void updateConcernRoleFromDataStore(final Entity personEntity,
    final ConcernRole concernRole)
    throws InformationalException, AppException {

    // update Names only if submitted from UA
    final Entity rootEntity = personEntity.getParentEntity();

    final Datastore datastore = rootEntity.getDatastore();

    final Entity intakeApplicationEntity =
      rootEntity.getChildEntities(datastore
        .getEntityType(BDMDatastoreConstants.INTAKE_APPLICATION_TYPE))[0];

    final String intakeAppTypeIDStr = intakeApplicationEntity
      .getAttribute(BDMDatastoreConstants.INTAKE_APPLICATION_TYPE_ID);

    final long intakeAppTypeID = Long.parseLong(intakeAppTypeIDStr);

    final String externalUserName =
      rootEntity.getAttribute(BDMDatastoreConstants.EXTERNAL_USER_NAME);

    final boolean isPrimaryParticipant =
      isPrimaryParticipant(personEntity, concernRole);

    // determine condition matching submission from UA
    if (StringUtils.isNotBlank(externalUserName) || !isPrimaryParticipant) {
      bdmUtil.updateNamesEvd(personEntity, concernRole);
    }

    updateDateOfBirthEvidence(personEntity, concernRole);

    updateNamesEvidence(personEntity, concernRole);

    updateSINEvidence(personEntity, concernRole);

    final String applicationForm = rootEntity.getAttribute("applicationForm");
    if (BDMOASAPPLICATIONFORM.ISP5054.equals(applicationForm)) {
      final String ssin =
        personEntity.getAttribute("socialSecurityOrIdentificationNumber");
      if (!ssin.isEmpty()) {
        createIdentificationsEvidence(personEntity, concernRole);
      }
      final String otherFirstName =
        personEntity.getAttribute("otherFirstName");
      if (!otherFirstName.isEmpty()) {
        createNamesEvidence(personEntity, concernRole);
      }
    }

    // BDMOAS OAS Account Number evidence.
    createPDCEvidenceByAlternateIDType(personEntity, concernRole,
      BDMOASDatastoreConstants.OAS_ACCOUNT_NUM_ATTR,
      CONCERNROLEALTERNATEIDEntry.BDM_ACCOUNT_NUMBER);

    if (isPrimaryParticipant) {
      // BDMOAS Foreign Identifier evidence.
      createPDCForeignIDEvidence(personEntity, concernRole);

      // BDMOAS Marital Status evidence.
      createMaritalStatusEvidence(personEntity, concernRole);

      bdmoasApplcnUtil.processPhoneEvd(personEntity, concernRole);

    }

    if (isPrimaryParticipant
      && intakeAppTypeID == BDMOASConstants.OAS_GIS_COMBINED_INTAKE_APP_TYPE_ID) {
      // Bank Account evidence
      createBankAccount(personEntity, concernRole);
    }

    updateGenderEvidence(personEntity, concernRole);

    bdmoasEvidenceUtil.processAddressEvd(personEntity, concernRole);

    updateContactPrefEvidence(personEntity, concernRole);

    if (concernRole.getConcernRoleType()
      .equals(CONCERNROLETYPEEntry.PERSON)) {
      updatePersonPaymentMethod(personEntity, concernRole);

    }

    linkExternalUserToConcernRole(personEntity, concernRole);

  }

  /**
   * If bank account details are available on the Person entity, unmatched with
   * an existing account, map them to a
   * new bank account for the concern role.
   *
   * @param personEntity The datastore person entity.
   * @param concernRole The person or prospect person.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  protected void createBankAccount(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // Get payment entity which contains the Bank information
    final Entity paymentEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(personEntity, BDMDatastoreConstants.PAYMENT);

    final Entity[] paymentBanksEntities =
      BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
        BDMLifeEventDatastoreConstants.PAYMENT_BANKS);

    if (paymentEntity != null) {
      final String datastoreBankAccountNumber =
        paymentEntity.getAttribute(BDMDatastoreConstants.BANK_ACCT_NUM);

      if (StringUtil.isNullOrEmpty(datastoreBankAccountNumber))
        return;

      boolean isExistingBankAccount = false;

      final String bankInstitutionNumber = paymentEntity
        .getAttribute(BDMDatastoreConstants.BANK_INSTITUTION_CODE);
      final String bankBranchNumber =
        paymentEntity.getAttribute(BDMDatastoreConstants.BANK_BRANCH_NUM);

      final String datastoreBankSortCode =
        bankInstitutionNumber + bankBranchNumber;

      if (paymentBanksEntities != null) {
        for (final Entity paymentBanksEntity : paymentBanksEntities) {
          // get the existing bank account info
          final String existingBankAccountNum = paymentBanksEntity
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_ACCT_NUM);
          final String existingBankSortCode = paymentBanksEntity
            .getAttribute(BDMLifeEventDatastoreConstants.BANK_SORT_CODE);
          if (datastoreBankSortCode.equals(existingBankSortCode)
            && datastoreBankAccountNumber.equals(existingBankAccountNum)) {

            isExistingBankAccount = true;
            break;
          }
        }
      }

      if (!isExistingBankAccount) {

        final String datastoreBankAccountName =
          paymentEntity.getAttribute(BDMDatastoreConstants.BANK_ACCOUNT_NAME);

        final ParticipantBankAccountDetails participantBankAccountDetails =
          new ParticipantBankAccountDetails();

        // defaults
        participantBankAccountDetails.concernRoleID = concernRole.getID();
        participantBankAccountDetails.bankAccountStatus =
          BANKACCOUNTSTATUS.OPEN;
        participantBankAccountDetails.jointAccountInd = false;
        participantBankAccountDetails.startDate = Date.getCurrentDate();
        participantBankAccountDetails.typeCode =
          BANKACCOUNTTYPE.PERSONALCURRENT;
        participantBankAccountDetails.primaryBankInd = true;
        participantBankAccountDetails.statusCode = RECORDSTATUS.NORMAL;

        // datastore mappings
        participantBankAccountDetails.accountNumber =
          datastoreBankAccountNumber;
        participantBankAccountDetails.bankSortCode = datastoreBankSortCode;
        participantBankAccountDetails.name = datastoreBankAccountName;

        try {
          PDCBankAccountFactory.newInstance()
            .insert(participantBankAccountDetails);
        } catch (final Exception e) {
          BDMOASEvidenceMappingUtil.logPDCEvidenceException(
            participantBankAccountDetails.concernRoleID, e,
            PDCConst.PDCBANKACCOUNT);
        }
      }
    }
  }

  /**
   * Updates person's date of birth evidence details if its not
   * same as the existing evidence details.
   * Overrides BDM version of the same method to account for country of birth.
   *
   * @param personEntity the datastore person entity
   * @param concernRole concern role of the person
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Override
  protected void updateDateOfBirthEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // get the date of birth evidence data for concern role
    final List<BDMDateofBirthEvidenceVO> getDOBEvidenceValueObjectList =
      new BDMDateofBirthEvidence()
        .getDOBEvidenceValueObject(concernRole.getID());

    final boolean isPrimaryInd = personEntity
      .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)
      .equals(Boolean.TRUE);

    // get last name at birth, parents last name at birth, DOB from datastore
    final BDMDateofBirthEvidenceVO dateOfBirthEvidenceVO =
      new BDMDateofBirthEvidenceVO();

    if (isPrimaryInd) {
      dateOfBirthEvidenceVO.setBirthLastName(personEntity
        .getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH));
    }

    dateOfBirthEvidenceVO.setDateOfBirth(Date.fromISO8601(personEntity
      .getAttribute(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH)));
    // BEGIN - User Story 21834 - Added Attestation
    dateOfBirthEvidenceVO.setComments(BDMConstants.kAttestationComments);
    dateOfBirthEvidenceVO.setAttestationDate(Date.getCurrentDate());
    dateOfBirthEvidenceVO.setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);

    // END - User Story 21834 - Added Attestation
    // create date of birth evidence for the person
    if (getDOBEvidenceValueObjectList.isEmpty()) {
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(dateOfBirthEvidenceVO);

      try {
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
          evidenceData, PDCConst.PDCBIRTHANDDEATH,
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
      } catch (final Exception e) {
        BDMOASEvidenceMappingUtil.logPDCEvidenceException(concernRole.getID(),
          e, PDCConst.PDCBIRTHANDDEATH);
      }
      return;
    }
    // modify the existing date of birth evidence if the dob values entered are
    // different than the one on the existing evidence
    final BDMDateofBirthEvidenceVO dobEvidenceVO =
      getDOBEvidenceValueObjectList.get(0);

    if (!dobEvidenceVO.equals(dateOfBirthEvidenceVO)) {
      // assigning existing comments and date of death values as these are not
      // added from the application
      if (dobEvidenceVO.getComments() != null) {
        dateOfBirthEvidenceVO.setComments(
          dobEvidenceVO.getComments() + dateOfBirthEvidenceVO.getComments());
      }

      dateOfBirthEvidenceVO.setDateOfDeath(dobEvidenceVO.getDateOfDeath());
      // BEGIN - User Story 21834 - Added Attestation
      dateOfBirthEvidenceVO
        .setAttesteeCaseParticipant(dobEvidenceVO.getPerson());
      dateOfBirthEvidenceVO.setPerson(dobEvidenceVO.getPerson());
      // END - User Story 21834 - Added Attestation
      // Begin BUG-26395
      final String countryOfBirth =
        personEntity.getAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH);

      if (isPrimaryInd && !StringUtil.isNullOrEmpty(countryOfBirth)
        && COUNTRY.CA.equals(countryOfBirth)) {
        dateOfBirthEvidenceVO.setCountryOfBirth(countryOfBirth);
      }

      BDMEvidenceUtil.modifyEvidence(dobEvidenceVO.getEvidenceID(),
        PDCConst.PDCBIRTHANDDEATH, dateOfBirthEvidenceVO,
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT, true);
      // End BUG-26395
    }

  }

  /**
   * Updates person's Names evidence details if its not
   * same as the existing evidence details.
   *
   * @param personEntity the datastore person entity
   * @param concernRole concern role of the person
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private void updateNamesEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // get the Names evidence data for concern role
    final List<BDMNamesEvidenceVO> getNamesEvidenceValueObjectList =
      new BDMNamesEvidence().getNamesEvidenceValueObject(concernRole.getID());

    // get Names evidence details from datastore
    final BDMNamesEvidenceVO namesEvidenceFromDatastoreVO =
      new BDMNamesEvidenceVO();

    namesEvidenceFromDatastoreVO.setTitle(
      personEntity.getAttribute(BDMOASDatastoreConstants.kPersonTitle));
    namesEvidenceFromDatastoreVO.setFirstName(
      personEntity.getAttribute(BDMDatastoreConstants.FIRST_NAME));
    namesEvidenceFromDatastoreVO.setMiddleName(
      personEntity.getAttribute(BDMDatastoreConstants.MIDDLE_NAME));
    namesEvidenceFromDatastoreVO.setLastName(
      personEntity.getAttribute(BDMDatastoreConstants.LAST_NAME));

    // modify the existing Names evidence if the Names evidence values entered
    // are different than the one on the existing evidence
    final BDMNamesEvidenceVO namesEvidenceVO =
      getNamesEvidenceValueObjectList.get(0);

    if (!namesEvidenceVO.equals(namesEvidenceFromDatastoreVO)) {
      BDMEvidenceUtil.modifyEvidence(namesEvidenceVO.getEvidenceID(),
        PDCConst.PDCNAME, namesEvidenceFromDatastoreVO,
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT, true);
    }

  }

  private void createMaritalStatusEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    if (personEntity
      .getChildEntities(personEntity.getDatastore().getEntityType(
        BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY)).length > 0) {

      final Entity maritalStatusEntity = personEntity
        .getChildEntities(personEntity.getDatastore().getEntityType(
          BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY))[0];

      final String currentMaritalStatus = maritalStatusEntity.getAttribute(
        BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR);

      final List<BDMOASMaritalStatusEvidenceVO> maritalStatusEvidenceValueObject =
        new BDMOASMaritalStatusEvidence()
          .getMaritalStatusEvidenceValueObject(concernRole.getID());

      if (maritalStatusEvidenceValueObject.isEmpty()
        && currentMaritalStatus.equals(BDMMARITALSTATUS.SINGLE)) {
        // create only if there is no existing evidence and status is Single.

        // START bugfix-110972 Marital Status for Single
        BDMOASEvidenceMappingUtil.mapMaritalStatusEvidence(
          concernRole.getID(), BDMMARITALSTATUSEntry.SINGLE);
        // END bugfix-110972 Marital Status for Single

      }
    }

  }

  /**
   * The method to update person's gender evidence details if its different from
   * the existing gender evidence details.
   *
   * @since ADO-21129
   * @param personEntity the datastore person entity
   * @param concernRole concern role of the person
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Override
  protected void updateGenderEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // get the gender evidence data for the concern role
    final List<BDMGenderEvidenceVO> genderEvidenceValueObjectList =
      new BDMGenderEvidence()
        .getGenderEvidenceValueObject(concernRole.getID());

    // get gender from the datastore
    final BDMGenderEvidenceVO genderEvidenceVO = new BDMGenderEvidenceVO();

    // To be set to 'Unknown' once value is made available
    genderEvidenceVO.setGender(
      personEntity.getAttribute(BDMLifeEventDatastoreConstants.GENDER));

    // create gender evidence for the person
    if (genderEvidenceValueObjectList.isEmpty()) {
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(genderEvidenceVO);

      try {
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
          evidenceData, PDCConst.PDCGENDER, EVIDENCECHANGEREASON.INITIAL);
      } catch (final Exception e) {
        BDMOASEvidenceMappingUtil.logPDCEvidenceException(concernRole.getID(),
          e, PDCConst.PDCGENDER);
      }

    }

  }

  private void createPDCForeignIDEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    final BDMFAConcernRoleKey foreignIDKey = new BDMFAConcernRoleKey();
    foreignIDKey.concernRoleID = concernRole.getID();

    // get stored foreign IDs from DB
    final BDMListOfForeignIDs listOfForeignIdentifiers =
      bdmUtil.getListOfForeignIdentifiers(foreignIDKey);

    final Entity[] residencePeriodEntities =
      personEntity.getChildEntities(personEntity.getDatastore()
        .getEntityType(BDMOASDatastoreConstants.RESIDENCE_HISTORY));

    for (final Entity residencePeriodEntity : residencePeriodEntities) {

      final String foreignCountryIDNumber =
        residencePeriodEntity.getAttribute(
          BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRYID_ATTR);

      final boolean isForeignIDNotBlankInd =
        StringUtils.isNotBlank(foreignCountryIDNumber);
      final boolean isMatched =
        isForeignIDNotBlankInd && listOfForeignIdentifiers.bdmFId.stream()
          .map(foreignID -> foreignID.alternateID).distinct().anyMatch(
            alternateID -> alternateID.equals(foreignCountryIDNumber));

      if (isForeignIDNotBlankInd && !isMatched) {

        createPDCEvidenceByAlternateIDType(residencePeriodEntity, concernRole,
          BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRYID_ATTR,
          CONCERNROLEALTERNATEIDEntry.BDM_FOREIGN_IDENTIFIER);
      }
    }
  }

  /**
   * Creates PDC Identification evidence for given
   * alternate reference IDs (e.g. SSN, foreign ID) by type and
   * mappedAttribute obtained from the entity.
   *
   * @param entity
   * @param concernRole
   * @param mappedAttribute
   * @param alternateIDTypeEntry
   * @throws AppException
   * @throws InformationalException
   */
  protected void createPDCEvidenceByAlternateIDType(final Entity entity,
    final ConcernRole concernRole, final String mappedAttribute,
    final CONCERNROLEALTERNATEIDEntry alternateIDTypeEntry)
    throws AppException, InformationalException {

    // get the Alternate Reference Number for concern role
    final BDMIdentificationEvidenceVO alternateIDEvidenceValueObject =
      getIdentificationEvidenceValueObject(concernRole.getID(),
        alternateIDTypeEntry);

    // get client Alternate Reference Number from datastore
    final BDMIdentificationEvidenceVO identificationEvidenceVO =
      new BDMIdentificationEvidenceVO();

    identificationEvidenceVO
      .setAlternateID(entity.getAttribute(mappedAttribute));
    identificationEvidenceVO.setAltIDType(alternateIDTypeEntry.getCode());
    identificationEvidenceVO.setFromDate(Date.getCurrentDate());

    if (mappedAttribute
      .equals(BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRYID_ATTR)) {
      final String residenceHistCountry = entity.getAttribute(
        BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRY_ATTR);

      if (!StringUtil.isNullOrEmpty(residenceHistCountry)) {
        identificationEvidenceVO.setCountry(residenceHistCountry);
      }

      identificationEvidenceVO
        .setBdmReceivedFrom(BDMRECEIVEDFROMEntry.CLIENT_REPORTED.getCode());
    }

    if (!StringUtil.isNullOrEmpty(identificationEvidenceVO.getAlternateID())
      && (StringUtil
        .isNullOrEmpty(alternateIDEvidenceValueObject.getAlternateID())
        || !alternateIDEvidenceValueObject
          .equals(identificationEvidenceVO))) {

      // create AlternateID evidence for the person

      identificationEvidenceVO.setPreferredInd(false);
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(identificationEvidenceVO);
      try {
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
          evidenceData, PDCConst.PDCIDENTIFICATION,
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
      } catch (final Exception e) {

        BDMOASEvidenceMappingUtil.logPDCEvidenceException(concernRole.getID(),
          e, PDCConst.PDCIDENTIFICATION);

      }
    }

  }

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMIdentificationEvidenceVO getIdentificationEvidenceValueObject(
    final long concernRoleID,
    final CONCERNROLEALTERNATEIDEntry alternateIDTypeEntry)
    throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCIDENTIFICATION);

    final BDMIdentificationEvidenceVO identificationEvidenceVO =
      new BDMIdentificationEvidenceVO();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final String alternateIDType =
        details.getAttribute("altIDType").getValue();

      if (!alternateIDTypeEntry.getCode().equals(alternateIDType)) {
        continue;
      }

      new BDMLifeEventUtil().setEvidenceData(identificationEvidenceVO,
        details);
    }

    return identificationEvidenceVO;
  }

  /**
   * The method to update person's identification evidence details if its not
   * same as the existing evidence details.
   *
   * @since ADO-21129
   * @param personEntity the datastore person entity
   * @param concernRole concern role of the person
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Override
  protected void updateSINEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // the Identification evidence validation prevents user from updating the
    // SIN evidence. The facade scoped object is set to allow SIN updates
    // this value is checked in the evidence validation
    TransactionInfo.setFacadeScopeObject(
      Configuration.getProperty(EnvVars.ENV_CANEDIT_SSN), false);

    // get the SIN for concern role
    final BDMIdentificationEvidenceVO sinEvidenceValueObject =
      new BDMIdentificationEvidence()
        .getSINEvidenceValueObject(concernRole.getID());

    // get client SIN from datastore
    final BDMIdentificationEvidenceVO identificationEvidenceVO =
      new BDMIdentificationEvidenceVO();

    identificationEvidenceVO.setAlternateID(personEntity
      .getAttribute(BDMLifeEventDatastoreConstants.SOCIAL_INSURANCE_NUMBER));
    identificationEvidenceVO
      .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
    identificationEvidenceVO.setFromDate(Date.getCurrentDate());

    // BEGIN: Task 92423 Add SIN evidence only if present
    if (!StringUtil
      .isNullOrEmpty(identificationEvidenceVO.getAlternateID())) {
      // END: Task 92423 Add SIN evidence only if present
      // create SIN evidence for the person
      if (StringUtil.isNullOrEmpty(sinEvidenceValueObject.getAlternateID())) {
        identificationEvidenceVO.setPreferredInd(true);
        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(identificationEvidenceVO);
        try {
          BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
            evidenceData, PDCConst.PDCIDENTIFICATION,
            EVIDENCECHANGEREASON.INITIAL);
        } catch (final Exception e) {
          BDMOASEvidenceMappingUtil.logPDCEvidenceException(
            concernRole.getID(), e, PDCConst.PDCIDENTIFICATION);
        }
        return;
      }

      // modify the existing SIN if the SIN entered is different from the one on
      // the existing evidence
      if (!sinEvidenceValueObject.equals(identificationEvidenceVO)) {
        try {
          final Date endDate =
            identificationEvidenceVO.getFromDate().addDays(-1);
          identificationEvidenceVO
            .setEvidenceID(sinEvidenceValueObject.getEvidenceID());
          identificationEvidenceVO.setPreferredInd(true);
          // added date param to match change in BDM component.
          bdmIdentificationEvd.createSINIdentificationEvidence(
            concernRole.getID(), endDate, identificationEvidenceVO,
            EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT,
            Date.kZeroDate);
        } catch (final Exception e) {
          BDMOASEvidenceMappingUtil.logPDCEvidenceException(
            concernRole.getID(), e, PDCConst.PDCIDENTIFICATION);
        }
      }
    }
  }

  /**
   * The method to update person's contact preferences evidence details if its
   * different from the existing evidence details.
   *
   * @since ADO-21129
   * @param personEntity the datastore person entity
   * @param concernRole concern role of the person
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Override
  protected void updateContactPrefEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // Get payment entity which contains the Bank information
    final Entity communicationPrefEntity =
      BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
        BDMDatastoreConstants.COMMUNICATION_PREF);

    if (communicationPrefEntity != null) {
      // get the contact preference active evidence list for concern role
      final List<BDMContactPreferenceEvidenceVO> contactPrefEvidenceValueObjectList =
        new BDMContactPreferenceEvidence()
          .getEvidenceValueObject(concernRole.getID());

      // get contact pref details from datastore
      final BDMContactPreferenceEvidenceVO contactPrefEvidenceVO =
        new BDMContactPreferenceEvidenceVO();

      contactPrefEvidenceVO
        .setPreferredOralLanguage(communicationPrefEntity.getAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE));
      contactPrefEvidenceVO
        .setPreferredWrittenLanguage(communicationPrefEntity.getAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE));

      if (StringUtil
        .isNullOrEmpty(contactPrefEvidenceVO.getPreferredCommunication())) {
        // preferred method is mandatory, this is not set for the applications
        // submitted by not logged in clients
        contactPrefEvidenceVO
          .setPreferredCommunication(BDMCORRESDELIVERY.POSTALDIG);
      }

      // create contact pref evidence for a person
      if (contactPrefEvidenceValueObjectList.isEmpty()) {
        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(contactPrefEvidenceVO);

        try {
          BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
            evidenceData, PDCConst.PDCCONTACTPREFERENCES,
            EVIDENCECHANGEREASON.INITIAL);
        } catch (final Exception e) {
          BDMOASEvidenceMappingUtil.logPDCEvidenceException(
            concernRole.getID(), e, PDCConst.PDCCONTACTPREFERENCES);
        }
        return;
      }

      // modify the existing contact preferences if the values are different
      // from
      // the one on the existing evidence
      final BDMContactPreferenceEvidenceVO contactPrefEvidenceVOObject =
        contactPrefEvidenceValueObjectList.get(0);

      if (!contactPrefEvidenceVO.equals(contactPrefEvidenceVOObject)) {

        contactPrefEvidenceVO
          .setParticipant(contactPrefEvidenceVOObject.getParticipant());
        contactPrefEvidenceVO
          .setEvidenceID(contactPrefEvidenceVOObject.getEvidenceID());
        contactPrefEvidenceVO
          .setComments(contactPrefEvidenceVOObject.getComments());

        try {
          BDMEvidenceUtil.modifyEvidence(
            contactPrefEvidenceVOObject.getEvidenceID(),
            PDCConst.PDCCONTACTPREFERENCES, contactPrefEvidenceVO,
            EVIDENCECHANGEREASON.REPORTEDBYCLIENT, true); // Task 26150 fix
          // evidence status code
        } catch (final Exception e) {
          BDMOASEvidenceMappingUtil.logPDCEvidenceException(
            concernRole.getID(), e, PDCConst.PDCCONTACTPREFERENCES);
        }
      }
    }
  }

  private void createIdentificationsEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    final String alternateID =
      personEntity.getAttribute("socialSecurityOrIdentificationNumber");

    final String country = personEntity
      .getAttribute("socialSecurityOrIdentificationNumberCountry");

    BDMOASEvidenceMappingUtil.mapIdentificationsEvidence(concernRole.getID(),
      alternateID, country);
  }

  private void createNamesEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    final String otherFirstName = personEntity.getAttribute("otherFirstName");

    final String otherLastName = personEntity.getAttribute("otherLastName");

    BDMOASEvidenceMappingUtil.mapNamesEvidence(concernRole.getID(),
      otherFirstName, otherLastName);
  }
}
