package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.codetable.BDMBANKACCOUNTCHOICE;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.entity.fact.BDMProspectPersonFactory;
import curam.ca.gc.bdm.entity.intf.BDMProspectPerson;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.intf.BDMPerson;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDtls;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonKey;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonDtls;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
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
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckUtil;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.BANKACCOUNTTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.ProspectPersonFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.AlternateName;
import curam.core.intf.ProspectPerson;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.intf.ExternalUser;
import curam.core.sl.entity.struct.ExternalUserDtls;
import curam.core.sl.entity.struct.ExternalUserKey;
import curam.core.sl.fact.ExternalUserParticipantLinkFactory;
import curam.core.sl.struct.ExternalUserParticipantLinkDetails;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameDtlsList;
import curam.core.struct.AlternateNameKey;
import curam.core.struct.AlternateNameReadByTypeKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.ProspectPersonDtls;
import curam.core.struct.ProspectPersonKey;
import curam.datastore.impl.Entity;
import curam.participant.impl.ConcernRole;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PersonHomePageDetails;
import curam.pdc.facade.struct.ReadPersonKey;
import curam.pdc.fact.PDCAlternateNameFactory;
import curam.pdc.fact.PDCBankAccountFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCAlternateName;
import curam.pdc.struct.ParticipantAlternateNameDetails;
import curam.pdc.struct.ParticipantBankAccountDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.applicationprocessing.impl.ConcernRoleMappingStrategy;
import java.util.HashMap;
import java.util.List;

/**
 * Custom BDM mapping which occurs during application submission, after the
 * ConcernRole is created.
 */
public class BDMConcernRoleMappingStrategyImpl
  extends ConcernRoleMappingStrategy {

  @Inject
  private PersonDAO personDAO;

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvidence;

  @Inject
  private BDMUtil bdmUtil;

  /**
   * BDM concern role mappings which get called during the application
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

    if (isRegisteredPerson(concernRole)) {
      // Insert into BDMPerson table
      addDemographicInformationForPerson(personEntity, concernRole);
      if (isPrimaryParticipant(personEntity, concernRole)) {
        updatePersonPaymentMethod(personEntity, concernRole);
        linkExternalUserToConcernRole(personEntity, concernRole);
        processPaymentEvd(personEntity, concernRole);
        createPreferredNameEvidence(personEntity, concernRole);
        bdmUtil.updateNamesEvd(personEntity, concernRole);
        bdmUtil.processPhoneEvd(personEntity, concernRole);
        bdmUtil.processEmailEvd(personEntity, concernRole);
        bdmUtil.processAddressEvd(personEntity, concernRole);
        updateDateOfBirthEvidence(personEntity, concernRole);
        updateGenderEvidence(personEntity, concernRole);
        updateSINEvidence(personEntity, concernRole);
        updateContactPrefEvidence(personEntity, concernRole);
      }
    } else {
      // Insert into BDMProspectPersonTable
      addDemographicInformationForProspectPerson(personEntity, concernRole);
      createPreferredNameEvidence(personEntity, concernRole);
      bdmUtil.updateNamesEvd(personEntity, concernRole);
    }
  }

  /**
   * Read MinorityGroup, IndigenousPerson, and EducationLevel attributes
   * and insert them into BDMProspectPerson table
   *
   * @param personEntity
   * Data store entity reference
   * @param concernRole
   * Target concern role
   *
   * @throws InformationalException
   * @throws AppException
   */
  private void addDemographicInformationForProspectPerson(
    final Entity personEntity, final ConcernRole concernRole)
    throws AppException, InformationalException {

    final String minorityGroup =
      personEntity.getAttribute(BDMDatastoreConstants.MINORITY_GROUP);

    final String indigenousPerson =
      personEntity.getAttribute(BDMDatastoreConstants.INDIGENOUS_PERSON);

    final String educationLevel =
      personEntity.getAttribute(BDMDatastoreConstants.EDUCATION_LEVEL);

    if (StringUtil.isNullOrEmpty(minorityGroup)) {

      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format(
          "UA mapping to Person: " + BDMDatastoreConstants.MINORITY_GROUP
            + " value is null or empty for '%s'.",
          concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }
    } else if (StringUtil.isNullOrEmpty(indigenousPerson)) {

      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format(
          "UA mapping to Person: " + BDMDatastoreConstants.INDIGENOUS_PERSON
            + " value is null or empty for '%s'.",
          concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }
    } else if (StringUtil.isNullOrEmpty(educationLevel)) {

      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format(
          "UA mapping to Person: " + BDMDatastoreConstants.EDUCATION_LEVEL
            + " value is null or empty for '%s'.",
          concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }
    }

    // If attributes are not null or empty then add them into BDMProspectPerson
    // table
    if (!StringUtil.isNullOrEmpty(educationLevel)
      || !StringUtil.isNullOrEmpty(minorityGroup)
      || !StringUtil.isNullOrEmpty(indigenousPerson)) {

      final BDMProspectPerson bdmProspectPerson =
        BDMProspectPersonFactory.newInstance();
      final BDMProspectPersonKey bdmProspectPersonKey =
        new BDMProspectPersonKey();
      bdmProspectPersonKey.concernRoleID = concernRole.getID();
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      bdmProspectPerson.read(notFoundIndicator, bdmProspectPersonKey);

      if (notFoundIndicator.isNotFound()) {

        final BDMProspectPersonDtls bdmProspectPersonDtls =
          new BDMProspectPersonDtls();

        bdmProspectPersonDtls.concernRoleID = concernRole.getID();
        bdmProspectPersonDtls.educationLevel = educationLevel;
        bdmProspectPersonDtls.indigenousPersonType = indigenousPerson;
        bdmProspectPersonDtls.memberMinorityGrpType = minorityGroup;

        bdmProspectPerson.insert(bdmProspectPersonDtls);
      }
    }
  }

  /**
   * Read MinorityGroup, IndigenousPerson, and EducationLevel attributes
   * and insert them into BDMPerson table
   *
   * @param personEntity
   * Data store entity reference
   * @param concernRole
   * Target concern role
   *
   * @throws InformationalException
   * @throws AppException
   */
  private void addDemographicInformationForPerson(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    final String minorityGroup =
      personEntity.getAttribute(BDMDatastoreConstants.MINORITY_GROUP);

    final String indigenousPerson =
      personEntity.getAttribute(BDMDatastoreConstants.INDIGENOUS_PERSON);

    final String educationLevel =
      personEntity.getAttribute(BDMDatastoreConstants.EDUCATION_LEVEL);

    if (StringUtil.isNullOrEmpty(minorityGroup)) {

      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format(
          "UA mapping to Person: " + BDMDatastoreConstants.MINORITY_GROUP
            + " value is null or empty for '%s'.",
          concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }
    } else if (StringUtil.isNullOrEmpty(indigenousPerson)) {

      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format(
          "UA mapping to Person: " + BDMDatastoreConstants.INDIGENOUS_PERSON
            + " value is null or empty for '%s'.",
          concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }
    } else if (StringUtil.isNullOrEmpty(educationLevel)) {

      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format(
          "UA mapping to Person: " + BDMDatastoreConstants.EDUCATION_LEVEL
            + " value is null or empty for '%s'.",
          concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }
    }

    // If attributes are not null or empty then add them into BDMPerson table
    if (!StringUtil.isNullOrEmpty(educationLevel)
      || !StringUtil.isNullOrEmpty(minorityGroup)
      || !StringUtil.isNullOrEmpty(indigenousPerson)) {

      final BDMPerson bdmPerson = BDMPersonFactory.newInstance();
      final BDMPersonKey bdmPersonKey = new BDMPersonKey();
      bdmPersonKey.concernRoleID = concernRole.getID();
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      bdmPerson.read(notFoundIndicator, bdmPersonKey);

      if (notFoundIndicator.isNotFound()) {

        final BDMPersonDtls bdmPersonDtls = new BDMPersonDtls();

        bdmPersonDtls.concernRoleID = concernRole.getID();
        bdmPersonDtls.educationLevel = educationLevel;
        bdmPersonDtls.indigenousPersonType = indigenousPerson;
        bdmPersonDtls.memberMinorityGrpType = minorityGroup;

        try {
          bdmPerson.insert(bdmPersonDtls);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Set the Person.methodOfPmtCode database column based on what is entered on
   * Person.paymentMethod datastore attribute which comes from the CE script.
   *
   * This method does not cater for prospect persons since they have no
   * methodOfPayment field.
   *
   * If the Person.paymentMethod datastore attribute is anything other than EFT
   * or Cheque, no value will be set.
   *
   * @param personEntity
   * Data store entity reference
   * @param concernRole
   * Target concern role
   *
   * @throws InformationalException
   * @throws AppException
   */
  private void updatePersonPaymentMethod(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    final String paymentMethod =
      personEntity.getAttribute(BDMDatastoreConstants.PAYMENT_METHOD);

    if (StringUtil.isNullOrEmpty(paymentMethod)) {

      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format(
          "UA mapping to Person: " + BDMDatastoreConstants.PAYMENT_METHOD
            + " value is null or empty for '%s'.",
          concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }

      /*
       * No payment method set. Exit.
       */
      return;
    }

    final PDCPerson pdcPerson = PDCPersonFactory.newInstance();
    final ReadPersonKey readPersonKey = new ReadPersonKey();
    readPersonKey.dtls.concernRoleID = concernRole.getID();
    final PersonHomePageDetails personHomePageDetails =
      pdcPerson.readHomePageDetailsForModify(readPersonKey);

    if (paymentMethod.equals(METHODOFDELIVERY.EFT)) {
      personHomePageDetails.dtls.methodOfPmtCode = METHODOFDELIVERY.EFT;
    } else if (paymentMethod.equals(METHODOFDELIVERY.CHEQUE)) {
      personHomePageDetails.dtls.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
    } else {
      /*
       * Nothing to do.
       */
      return;
    }

    try {
      pdcPerson.modifyPersonHomePageDetails(personHomePageDetails);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if the concern role is a registered person or a prospect.
   *
   * @param concernRole The concern role.
   * @return true if the person is not a prospect, false otherwise
   *
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isRegisteredPerson(final ConcernRole concernRole)
    throws AppException, InformationalException {

    boolean isRegisteredPerson = false;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
    prospectPersonKey.concernRoleID = concernRole.getID();
    ProspectPersonFactory.newInstance().read(notFoundIndicator,
      prospectPersonKey);
    if (notFoundIndicator.isNotFound()) {
      isRegisteredPerson = true;
    }
    return isRegisteredPerson;
  }

  /**
   * Checks if the person is the primary participant.
   *
   * @param personEntity
   * @param concernRole
   * @return true if the person is primary, false otherwise
   */
  private boolean isPrimaryParticipant(final Entity personEntity,
    final ConcernRole concernRole) {

    final String isPrimaryParticipant =
      personEntity.getAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT);

    if (StringUtil.isNullOrEmpty(isPrimaryParticipant)) {
      if (Trace.atLeast(Trace.kTraceOn)) {
        final String warning = String.format("UA mapping to Person: "
          + BDMDatastoreConstants.DEPENDANT_AT_SAME_RESIDENCE
          + " value is null or empty for '%s'.", concernRole.getID());
        Trace.kTopLevelLogger.warn(warning);
      }
      /*
       * No isPrimaryParticipant. Assume false.
       */
      return false;
    }

    if (isPrimaryParticipant.equals("true")) {
      // isPrimaryParticipant is Yes then the person is the Primary
      return true;
    } else {
      // isPrimaryParticipant is No then the person is a dependant
      return false;
    }
  }

  /**
   * Link the ExternalUser account that submitted the application to the created
   * Primary Applicant for Created Registered Persons
   *
   *
   * @param intakeClient
   * @param concernRoleID
   * @param externalUserName
   * @param concernRoleID
   * @param personRelType
   * @throws AppException
   * @throws InformationalException
   */
  private void linkExternalUserToConcernRole(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // Instantiate objects used for linking User to concernRole
    final CitizenWorkspaceAccountManager citizenWorkspaceAccountManager =
      GuiceWrapper.getInjector()
        .getInstance(CitizenWorkspaceAccountManager.class);

    // Only try linking user to concernrole, if the concernrole is not already
    // linked
    if (citizenWorkspaceAccountManager.hasLinkedAccount(concernRole)) {
      return;
    }

    final Entity rootEntity =
      BDMApplicationEventsUtil.getRootEntity(personEntity);

    final String externalUserName =
      rootEntity.getAttribute(BDMDatastoreConstants.EXTERNAL_USER_NAME);
    final String externalUserType =
      rootEntity.getAttribute(BDMDatastoreConstants.EXTERNAL_USER_TYPE);
    final String userType =
      rootEntity.getAttribute(BDMDatastoreConstants.USER_TYPE);

    // Dont attempt to link for Public or Generated Users
    if (externalUserName.equals(BDMConstants.EMPTY_STRING)
      || externalUserName.equals(BDMDatastoreConstants.UNAUTHED_USER)
      || externalUserType.equals(BDMConstants.EMPTY_STRING)
      || userType.equals(BDMConstants.EMPTY_STRING)
      || userType.equals(BDMDatastoreConstants.INTERNAL)) {
      // Dont link if the user wasnt logged in
      return;
    }

    // search for the external user details
    final ExternalUserDtls externalUserDtls =
      BDMApplicationEventsUtil.getExternalUserDetails(externalUserName);

    // If there are no external User details found, do not attempt to link
    if (externalUserDtls == null) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "No external user details were found for: " + externalUserName
        + ". Skipping attempt to link applicant to user.");
      return;
    }

    // Only attempt to link if the User is registered unlinked user
    if (externalUserDtls.roleName
      .equals(BDMDatastoreConstants.REGISTERED_CITIZEN_ROLE)) {

      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "External user details were found for registered user : "
        + externalUserName + ". Attempting to link applicant to user.");

      // Get person details.
      final long concernRoleID = concernRole.getID();
      final Person person = personDAO.get(concernRoleID);

      // try linking user to concernrole
      try {

        // Perform participant link
        final ExternalUserParticipantLinkDetails extUserParticipantDtls =
          new ExternalUserParticipantLinkDetails();
        extUserParticipantDtls.details.externalUserParticipantLinkDtls.extUserPartRelType =
          person.getConcernRoleType().getCode();
        extUserParticipantDtls.details.externalUserParticipantLinkDtls.userName =
          externalUserName;
        extUserParticipantDtls.details.participantRoleID = concernRoleID;
        ExternalUserParticipantLinkFactory.newInstance()
          .insert(extUserParticipantDtls);

        // Update Role of External user from CITIZENROLE to LINKEDCITIZENROLE
        final ExternalUser externalUserObj =
          ExternalUserFactory.newInstance();
        final ExternalUserKey externalUserKey = new ExternalUserKey();
        externalUserKey.userName = externalUserName;

        // Invoke OOTB method to link participant to user
        citizenWorkspaceAccountManager.linkUsertoParticipant(externalUserName,
          personDAO.get(concernRoleID));

        // Get latest user dtls
        final ExternalUserDtls externalUserUpdateDtls =
          BDMApplicationEventsUtil.getExternalUserDetails(externalUserName);

        // check if the external User has an updated linked role
        if (!externalUserUpdateDtls.roleName
          .equals(BDMDatastoreConstants.LINKED_CITIZEN_ROLE)) {
          // Update user Role to Linked
          externalUserUpdateDtls.roleName =
            BDMDatastoreConstants.LINKED_CITIZEN_ROLE;
          externalUserObj.modify(externalUserKey, externalUserUpdateDtls);
        }
        Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
          + "External user linked to applicant concernroleID: "
          + concernRoleID);

      } catch (final Exception e) {
        Trace.kTopLevelLogger.warn(
          BDMConstants.BDM_LOGS_PREFIX + "External user : " + externalUserName
            + " could not be linked to applicant with concernroleID: "
            + concernRoleID);
        e.printStackTrace();
        return;
      }

    }
  }

  /**
   * Process payment evidence
   *
   * @param personEntity
   * @param concernRole
   */
  private void processPaymentEvd(final Entity personEntity,
    final curam.participant.impl.ConcernRole concernRole)
    throws AppException, InformationalException {

    final String preferredPaymentMethod =
      personEntity.getAttribute(BDMDatastoreConstants.PAYMENT_METHOD);
    if (!StringUtil.isNullOrEmpty(preferredPaymentMethod)) {
      // Get payment entity which contains the Bank information
      final Entity paymentEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(personEntity, BDMDatastoreConstants.PAYMENT);

      final Entity[] paymentBanksEntities =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMLifeEventDatastoreConstants.PAYMENT_BANKS);

      if (preferredPaymentMethod.equals(METHODOFDELIVERY.EFT)) {
        // Get new payment entity for new bank from datastore
        final String preferredBankAccount = paymentEntity.getAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_BANK_ACCOUNT);

        if (StringUtil.isNullOrEmpty(preferredBankAccount)
          || preferredBankAccount.equals(BDMBANKACCOUNTCHOICE.ADD_NEW)) {
          createBankAccount(personEntity, concernRole);
        } else if (preferredBankAccount
          .equals(BDMBANKACCOUNTCHOICE.USE_EXISTING)) {

          for (final Entity paymentBanksEntity : paymentBanksEntities) {
            if (!StringUtil.isNullOrEmpty(paymentBanksEntity.getAttribute(
              BDMLifeEventDatastoreConstants.PAYMENT_PREFERRED_BANK_FOR_PROGRAM))) {
              final boolean preferredBankForProgram =
                Boolean.parseBoolean(paymentBanksEntity.getAttribute(
                  BDMLifeEventDatastoreConstants.PAYMENT_PREFERRED_BANK_FOR_PROGRAM));
              if (preferredBankForProgram) {
                final long preferredBankEvidenceID =
                  Long.parseLong(paymentBanksEntity.getAttribute(
                    BDMLifeEventDatastoreConstants.PAYMENT_BANK_EVIDENCE_ID));
                // update this bank account to be preferred bank account
                BDMEvidenceUtil.modifySelectedEvidenceAttribute(
                  preferredBankEvidenceID, "preferredInd", "true",
                  PDCConst.PDCBANKACCOUNT,
                  EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
                break;
              }
            }
          }
        }

      } else if (preferredPaymentMethod.equals(METHODOFDELIVERY.CHEQUE)) {
        // Task 63926 -when submitting application, the preferred payment method
        // is chosen as cheque.
        if (paymentBanksEntities != null) {
          for (final Entity paymentBanksEntity : paymentBanksEntities) {
            // remove the preferred indicator on the evidence, since this
            // indicator is used to add EFT payment destination when
            // authorizing the program
            BDMEvidenceUtil.modifySelectedEvidenceAttribute(
              Long.parseLong(paymentBanksEntity.getAttribute(
                BDMLifeEventDatastoreConstants.PAYMENT_BANK_EVIDENCE_ID)),
              "preferredInd", "false", PDCConst.PDCBANKACCOUNT,
              EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
          }
        }
      }
    }

  }

  /**
   * If bank account details are available on the Person entity, map them to a
   * new bank account for the concern role.
   *
   * @param personEntity The datastore person entity.
   * @param concernRole The person or prospect person.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createBankAccount(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // Get payment entity which contains the Bank information
    final Entity paymentEntity = BDMApplicationEventsUtil
      .retrieveChildEntity(personEntity, BDMDatastoreConstants.PAYMENT);

    final Entity[] paymentBanksEntities =
      BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
        BDMLifeEventDatastoreConstants.PAYMENT_BANKS);

    final String datastoreBankAccountNumber =
      paymentEntity.getAttribute(BDMDatastoreConstants.BANK_ACCT_NUM);

    if (!StringUtil.isNullOrEmpty(datastoreBankAccountNumber)) {

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
            // update this bank account to be preferred bank account
            BDMEvidenceUtil.modifySelectedEvidenceAttribute(
              Long.parseLong(paymentBanksEntity.getAttribute(
                BDMLifeEventDatastoreConstants.PAYMENT_BANK_EVIDENCE_ID)),
              "preferredInd", "true", PDCConst.PDCBANKACCOUNT,
              EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
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
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Util method to create Person Preferred name
   *
   * @since Bug-19917
   * @param personEntity
   * @param concernRole
   */
  public void createPreferredNameEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // check if Preferred name is entered
    if (personEntity.hasAttribute(BDMDatastoreConstants.PREFERRED_NAME)
      && !personEntity.getTypedAttribute(BDMDatastoreConstants.PREFERRED_NAME)
        .toString().isEmpty()) {

      // Read Person Details or read Prospect Person Details
      final PersonKey personKey = new PersonKey();
      personKey.concernRoleID = concernRole.getID();
      final curam.core.intf.Person personObj = PersonFactory.newInstance();
      final ProspectPerson prospectPersonObj =
        ProspectPersonFactory.newInstance();
      final AlternateName alternateNameObj =
        AlternateNameFactory.newInstance();
      final AlternateNameKey alternateNameKey = new AlternateNameKey();

      if (isRegisteredPerson(concernRole)) {
        final PersonDtls personDtls = personObj.read(personKey, true);
        alternateNameKey.alternateNameID = personDtls.primaryAlternateNameID;
      } else {

        final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
        prospectPersonKey.concernRoleID = concernRole.getID();
        final ProspectPersonDtls prospectPersonDtls =
          prospectPersonObj.read(prospectPersonKey);

        alternateNameKey.alternateNameID =
          prospectPersonDtls.primaryAlternateNameID;
      }

      final ParticipantAlternateNameDetails nameDetails =
        new ParticipantAlternateNameDetails();

      // Check if an preferred name already exists
      final AlternateNameReadByTypeKey alternateNameReadByTypeKey =
        new AlternateNameReadByTypeKey();
      alternateNameReadByTypeKey.concernRoleID = concernRole.getID();
      alternateNameReadByTypeKey.nameStatus = RECORDSTATUS.NORMAL;
      alternateNameReadByTypeKey.nameType = ALTERNATENAMETYPE.PREFERRED;
      final AlternateNameDtlsList alternateNameDtlsList =
        alternateNameObj.searchByType(alternateNameReadByTypeKey);

      final PDCAlternateName pdcAlternateNameObj =
        PDCAlternateNameFactory.newInstance();
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

      if (!alternateNameDtlsList.dtls.isEmpty()) {
        final AlternateNameDtls prefAlternateNameDtls =
          alternateNameDtlsList.dtls.get(0);
        nameDetails.assign(prefAlternateNameDtls);
        // Make first name the Preferred Name
        nameDetails.firstForename = personEntity
          .getTypedAttribute(BDMDatastoreConstants.PREFERRED_NAME).toString();

        try {
          pdcAlternateNameObj.modify(nameDetails);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      } else {
        // Gathering Name information about current Name to fill in similar
        // fields
        final AlternateNameDtls alternateNameDtls =
          alternateNameObj.read(notFoundIndicator, alternateNameKey);
        if (!notFoundIndicator.isNotFound()) {
          nameDetails.assign(alternateNameDtls);
          // Make first name the Preferred Name
          nameDetails.firstForename = personEntity
            .getTypedAttribute(BDMDatastoreConstants.PREFERRED_NAME)
            .toString();
          // Make Name Type Preferred Name
          nameDetails.nameType = ALTERNATENAMETYPE.PREFERRED;
          // call OOTB Modify method to insert person name evidence
          try {
            pdcAlternateNameObj.insert(nameDetails);
          } catch (final Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // BEGIN TASK 21129 - map evidence information

  /**
   * The method to update person's date of birth evidence details if its not
   * same as the existing evidence details.
   *
   * @since ADO-21129
   * @param personEntity the datastore person entity
   * @param concernRole concern role of the person
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private void updateDateOfBirthEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // get the date of birth evidence data for concern role
    final List<BDMDateofBirthEvidenceVO> getDOBEvidenceValueObjectList =
      new BDMDateofBirthEvidence()
        .getDOBEvidenceValueObject(concernRole.getID());

    // get last name at birth, parents last name at birth, DOB from datastore
    final BDMDateofBirthEvidenceVO dateOfBirthEvidenceVO =
      new BDMDateofBirthEvidenceVO();

    dateOfBirthEvidenceVO.setBirthLastName(personEntity
      .getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH));
    dateOfBirthEvidenceVO.setMothersBirthLastName(personEntity.getAttribute(
      BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH));
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
        e.printStackTrace();
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
      BDMEvidenceUtil.modifyEvidence(dobEvidenceVO.getEvidenceID(),
        PDCConst.PDCBIRTHANDDEATH, dateOfBirthEvidenceVO,
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT, true);
      // End BUG-26395
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
  private void updateGenderEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // get the gender evidence data for the concern role
    final List<BDMGenderEvidenceVO> genderEvidenceValueObjectList =
      new BDMGenderEvidence()
        .getGenderEvidenceValueObject(concernRole.getID());

    // get gender from the datastore
    final BDMGenderEvidenceVO genderEvidenceVO = new BDMGenderEvidenceVO();

    genderEvidenceVO.setGender(
      personEntity.getAttribute(BDMLifeEventDatastoreConstants.GENDER));

    // create gender evidence for the person
    if (genderEvidenceValueObjectList.isEmpty()) {
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(genderEvidenceVO);

      try {
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
          evidenceData, PDCConst.PDCGENDER,
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
      } catch (final Exception e) {
        e.printStackTrace();
      }

      return;
    }

    // modify the existing gender evidence if the gender entered is different
    // from the one on the existing evidence
    final BDMGenderEvidenceVO genderEvidenceVOObj =
      genderEvidenceValueObjectList.get(0);

    if (!genderEvidenceVO.equals(genderEvidenceVOObj)) {
      // assigning existing comments
      genderEvidenceVO.setEvidenceID(genderEvidenceVOObj.getEvidenceID());
      genderEvidenceVO.setComments(genderEvidenceVOObj.getComments());

      BDMEvidenceUtil.modifyEvidence(genderEvidenceVOObj.getEvidenceID(),
        PDCConst.PDCGENDER, genderEvidenceVO,
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
    }

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
  private void updateSINEvidence(final Entity personEntity,
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

    // create SIN evidence for the person
    if (StringUtil.isNullOrEmpty(sinEvidenceValueObject.getAlternateID())) {
      identificationEvidenceVO.setPreferredInd(true);
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(identificationEvidenceVO);
      try {
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
          evidenceData, PDCConst.PDCIDENTIFICATION,
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
      } catch (final Exception e) {
        e.printStackTrace();
      }
      return;
    }

    // modify the existing SIN if the SIN entered is different from the one on
    // the existing evidence
    if (!sinEvidenceValueObject.equals(identificationEvidenceVO)) {
      try {
        // Remove SIN SIR Response evidence if it exists.
        new BDMSINIntegrityCheckUtil()
          .removeExistingSINSIRResponceEvidence(concernRole.getID());
        final Date endDate =
          identificationEvidenceVO.getFromDate().addDays(-1);
        identificationEvidenceVO
          .setEvidenceID(sinEvidenceValueObject.getEvidenceID());
        identificationEvidenceVO.setPreferredInd(true);
        bdmIdentificationEvidence.createSINIdentificationEvidence(
          concernRole.getID(), endDate, identificationEvidenceVO,
          EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT,
          identificationEvidenceVO.getFromDate());
      } catch (final Exception e) {
        e.printStackTrace();
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
  private void updateContactPrefEvidence(final Entity personEntity,
    final ConcernRole concernRole)
    throws AppException, InformationalException {

    // get the contact preference active evidence list for concern role
    final List<BDMContactPreferenceEvidenceVO> contactPrefEvidenceValueObjectList =
      new BDMContactPreferenceEvidence()
        .getEvidenceValueObject(concernRole.getID());

    // get contact pref details from datastore
    final BDMContactPreferenceEvidenceVO contactPrefEvidenceVO =
      new BDMContactPreferenceEvidenceVO();

    // Get payment entity which contains the Bank information
    final Entity communicationPrefEntity =
      BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
        BDMDatastoreConstants.COMMUNICATION_PREF);

    contactPrefEvidenceVO.setPreferredCommunication(communicationPrefEntity
      .getAttribute(BDMLifeEventDatastoreConstants.RECEIVE_CORRESPONDENCE));
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
      contactPrefEvidenceVO.setPreferredCommunication(BDMCORRESDELIVERY.DIG);
    }

    // create contact pref evidence for a person
    if (contactPrefEvidenceValueObjectList.isEmpty()) {
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(contactPrefEvidenceVO);

      try {
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRole.getID(),
          evidenceData, PDCConst.PDCCONTACTPREFERENCES,
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
      } catch (final Exception e) {
        e.printStackTrace();
      }
      return;
    }

    // modify the existing contact preferences if the values are different from
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
        e.printStackTrace();
      }
    }

  }

  // END TASK 21129 - map evidence information
}
