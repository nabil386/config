package curam.workspaceservices.applicationprocessing.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMPersonMatchImpl;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMALERTOCCUR;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EMAILTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.IEGYESNO;
import curam.codetable.LANGUAGE;
import curam.codetable.PHONETYPE;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.codetable.impl.COUNTRYEntry;
import curam.codetable.impl.NATIONALITYEntry;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.struct.ExternalUserDtls;
import curam.core.sl.fact.ExternalUserParticipantLinkFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.intf.ExternalUserParticipantLink;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.sl.struct.ExternalUserKey;
import curam.core.sl.struct.ExternalUserParticipantLinkDetailsList;
import curam.core.sl.struct.ProspectPersonRegistrationDtls;
import curam.core.struct.AlternateIDAndType;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.value.CodeTableItem;
import curam.datastore.impl.Entity;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.fact.PDCPersonFactory;
import curam.pdc.fact.PDCProspectPersonFactory;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.impl.PDCContactPreferencesEvidencePopulator;
import curam.pdc.intf.PDCPerson;
import curam.pdc.intf.PDCProspectPerson;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.pdc.struct.PDCProspectPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.codetable.fact.CodeTableFactory;
import curam.util.internal.codetable.intf.CodeTable;
import curam.util.internal.codetable.struct.CTItem;
import curam.util.internal.codetable.struct.CTItemKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.StringHelper;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLink;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLinkDAO;
import curam.workspaceservices.intake.impl.IntakeApplicationDAO;
import curam.workspaceservices.mappingbeans.impl.ProspectPersonMappingBean;
import java.util.List;
import java.util.Set;

/**
 * Custom Implementation class extending the OOTB IntakeClientManagementImpl
 *
 * Using same OOTB package, Since the parent class is package
 * private we can not move this class to custom
 * package and few dependencies are also package private.
 *
 * There are no hooks provided by Curam for this customization. So we
 * are keeping these class to the same package as the Curam OOTB
 * package.
 *
 */

@SuppressWarnings("restriction")
public class BDMIntakeClientManagementImpl
  extends IntakeClientManagementImpl {

  @Inject
  private Provider<MappedProspectPersonInternal> mappedPersonProvider;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private IntakeApplicationConcernRoleLinkDAO intakeApplicationConcernRoleLinkDAO;

  @Inject
  private IntakeApplicationDAO intakeApplicationDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  @Inject
  private curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO etDefDAO;

  @Inject
  private Set<PDCContactPreferencesEvidencePopulator> pdcContactPreferencesEvidencePopulators;

  private static String globalAlternateIDType = new String();

  /**
   * This method is called when an application is submitted from the Citizen
   * Engagement portal, but not when an
   * application is submitted from the Caseworker Portal. It will attempt to
   * find an existing person in the system
   * and map the person from the submitted application to the existing person in
   * the system. If a matching person can
   * not be found then the method will either register the person in the
   * submitted application as a Prospect Person or a
   * Person. If the following conditions are met then the person in the
   * {@code intakeClient} parameter will be
   * registered as a Prospect Person:
   * <UL>
   * <LI>If the external user that submitted this application did not pass RIDP
   * then register everyone in the
   * application as a Prospect Person</LI>
   * <LI>IF the Person Match logic returned a partial match then register the
   * person as a Prospect Person</LI>
   * <LI>If an exact match wasn't found when the Person Match logic is called,
   * and the the
   * {@code registerAsProspectParam}
   * parameter is set to true (this is set OOTB using a system property), then
   * the system will register the person from
   * the application as a Prospect Person.</LI>
   * </UL>
   *
   * @param intakeClient
   * A mapping bean representing a person going through the application intake
   * process.
   * @param registerAsProspectParam
   * A boolean to say if the person should be registered as a Prospect Person
   * instead of as a Person. Based on an OOTB system property
   * @return A populated {@code MappedProspectPersonInternal} instance
   * representing a newly registered Person/Prospect
   * Person
   */
  @Override
  public MappedProspectPersonInternal getMappedPerson(
    final ProspectPersonMappingBean intakeClient,
    final boolean registerAsProspectParam)
    throws AppException, InformationalException {

    TransactionInfo.setFacadeScopeObject(
      Configuration.getProperty(EnvVars.ENV_CANEDIT_SSN), false);

    final boolean registerAsProspect = registerAsProspectParam;

    if (intakeClient.isExistingConcernRole().booleanValue()) {
      final curam.participant.impl.ConcernRole concernRole =
        this.concernRoleDAO.get(intakeClient.getConcernRoleID());

      if (concernRole.getConcernRoleType()
        .equals(CONCERNROLETYPEEntry.PERSON)) {
        return getMappedPerson(intakeClient);
      }
    }

    // Custom person match
    MappedProspectPersonInternal mappedProspectPerson = null;
    mappedProspectPerson = findPersonMatch(intakeClient);

    if (mappedProspectPerson != null) {
      // if not null, then an exact or partial match was found
      return mappedProspectPerson;
    }
    // No exact or partial match found, register Person
    return registerPerson(intakeClient);
  }

  /**
   * BDM custom person match
   *
   * @param intakeClient
   * @return existing person if an exact match was found, or a newly
   * registered prospect for a partial match
   * @throws AppException
   * @throws InformationalException
   */
  public MappedProspectPersonInternal
    findPersonMatch(final ProspectPersonMappingBean intakeClient)
      throws AppException, InformationalException {

    final Entity personEntity = intakeClient.getPersonEntity();
    final Entity rootEntity = personEntity.getParentEntity();

    final String externalUserName =
      rootEntity.getAttribute(BDMDatastoreConstants.EXTERNAL_USER_NAME);

    final boolean isPrimaryParticipant = ((Boolean) personEntity
      .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT))
        .booleanValue();

    MappedProspectPersonInternal mappedProspectPerson = null;
    boolean isExactMatch = false;
    boolean isPartialMatch = false;
    boolean isRelaxedPartialMatch = false;
    boolean isLinkedUser = false;
    boolean isUnlinkedUser = false;

    // only set isLinkedUser for Primary Participants
    if (isPrimaryParticipant) {
      final ExternalUserDtls externalUserDtls =
        BDMApplicationEventsUtil.getExternalUserDetails(externalUserName);
      if (externalUserDtls != null) {
        // check if userName is already linked to a concernRole
        if (externalUserDtls.roleName.toString()
          .equals(BDMDatastoreConstants.LINKED_CITIZEN_ROLE)) {
          isLinkedUser = true;
          isExactMatch = true;
        } else if (externalUserDtls.roleName.toString()
          .equals(BDMDatastoreConstants.REGISTERED_CITIZEN_ROLE)) {
          isUnlinkedUser = true;
        }
      }
    }

    // Handle if the user is already linked, just use the linked concernRole
    if (isLinkedUser && isPrimaryParticipant) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "User is already a linked Account: " + externalUserName);
      final ExternalUserParticipantLink externalUserParticipantLink =
        ExternalUserParticipantLinkFactory.newInstance();
      final ExternalUserKey externalUserKey = new ExternalUserKey();
      externalUserKey.userKey.userName = externalUserName;
      final ExternalUserParticipantLinkDetailsList externalUserParticipantLinkDetailsList =
        externalUserParticipantLink.searchLinkByUsername(externalUserKey);
      mappedProspectPerson = this.mappedPersonProvider.get();
      final long concernRoleID =
        externalUserParticipantLinkDetailsList.details.dtls
          .get(0).participantRoleID;
      mappedProspectPerson.setConcernRoleID(concernRoleID);
      mappedProspectPerson.setDataStoreID(personEntity.getUniqueID());
      mappedProspectPerson.setPrimaryClientInd(isPrimaryParticipant);
      // DEV fix for 21768 and DEV fix for 21971
      readUpdatePersonEvidences(concernRoleID, intakeClient);
      return mappedProspectPerson;
    }

    final BDMPersonMatchImpl bdmPersonMatchImpl = new BDMPersonMatchImpl();
    // if an unlinked user search using a relaxed exact match to include some
    // partial match types
    if (isUnlinkedUser) {
      final BDMPersonSearchResultDetailsList unlinkedExactMatchResults =
        bdmPersonMatchImpl.performUnlinkedExactMatch(intakeClient);
      if (unlinkedExactMatchResults.dtls.size() == 1) {
        isExactMatch = true;
        // if the concernrole is a match to someone already on the application
        // or if the concernRole found is already linked, then default to
        // prospect creation
        if (isExactMatchAlreadyOnApplication(rootEntity,
          unlinkedExactMatchResults.dtls.get(0).concernRoleID)
          || isConcernRoleAlreadyLinked(
            unlinkedExactMatchResults.dtls.get(0).concernRoleID)) {
          Trace.kTopLevelLogger.info(
            BDMConstants.BDM_LOGS_PREFIX + "Exact match with concernroleid "
              + unlinkedExactMatchResults.dtls.get(0).concernRoleID
              + " already exists");
          isExactMatch = false;
          isPartialMatch = true;
          // register prospect
          mappedProspectPerson = registerProspect(intakeClient);
          return mappedProspectPerson;
        }
        // else continue using the exact match found
        Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
          + "Exact match found for unlinked user");
        mappedProspectPerson = this.mappedPersonProvider.get();
        mappedProspectPerson.setConcernRoleID(
          unlinkedExactMatchResults.dtls.get(0).concernRoleID);
        mappedProspectPerson.setDataStoreID(personEntity.getUniqueID());
        mappedProspectPerson.setPrimaryClientInd(isPrimaryParticipant);
      } else {
        // if unlinked user doesnt have an exact match check for a partial match
        isRelaxedPartialMatch =
          bdmPersonMatchImpl.isRelaxedPartialMatchFound(intakeClient);
        if (isRelaxedPartialMatch == true) {
          // if partial match, then register prospect
          mappedProspectPerson = registerProspect(intakeClient);
        } else {
          // if no exact or partial match, then register person
          mappedProspectPerson = registerPerson(intakeClient);
        }
      }
      return mappedProspectPerson;
    } else {
      // Finally for unauthenticated users determine match type
      final BDMPersonSearchResultDetailsList exactMatchResults =
        bdmPersonMatchImpl.performExactMatch(intakeClient);
      if (exactMatchResults.dtls.size() == 1) {
        isExactMatch = true;
      } else {
        isPartialMatch = bdmPersonMatchImpl.isPartialMatchFound(intakeClient);
      }
      if (isExactMatch) {
        // if the exact match is already on the intake application, set it as a
        // partial match
        if (isExactMatchAlreadyOnApplication(rootEntity,
          exactMatchResults.dtls.get(0).concernRoleID)) {
          Trace.kTopLevelLogger.info(
            BDMConstants.BDM_LOGS_PREFIX + "Exact match with concernroleid "
              + exactMatchResults.dtls.get(0).concernRoleID
              + " already exists on the application");
          isExactMatch = false;
          isPartialMatch = true;
          mappedProspectPerson = registerProspect(intakeClient);
        }
        Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
          + "Exact match found for unauthenticated user person");
        mappedProspectPerson = this.mappedPersonProvider.get();
        mappedProspectPerson
          .setConcernRoleID(exactMatchResults.dtls.get(0).concernRoleID);
        mappedProspectPerson.setDataStoreID(personEntity.getUniqueID());
        mappedProspectPerson.setPrimaryClientInd(isPrimaryParticipant);
      } else if (isPartialMatch) {
        Trace.kTopLevelLogger.info(
          BDMConstants.BDM_LOGS_PREFIX + "Partial match found for person");
        mappedProspectPerson = registerProspect(intakeClient);
      } else {
        // if no exact, nor partial match, then register person
        mappedProspectPerson = registerPerson(intakeClient);
      }
      return mappedProspectPerson;
    }
  }

  /**
   * This method will update SIN & Last name at Birth/Parent’s Last Name at
   * Birth evidence.
   *
   * @param concernRoleID
   * @param intakeClient
   * @throws AppException
   * @throws InformationalException
   */
  private void readUpdatePersonEvidences(final long concernRoleID,
    final ProspectPersonMappingBean intakeClient)
    throws AppException, InformationalException {

    // DEV fix for 21768
    if (concernRoleID != 0) {

      final ProspectPersonRegistrationDtls client =
        intakeClient.getProspectPersonDetails();

      final BDMIdentificationEvidence bdmIdentificationEvidence =
        new BDMIdentificationEvidence();

      final BDMDateofBirthEvidence bdmDateofBirthEvidence =
        new BDMDateofBirthEvidence();

      try {
        final BDMIdentificationEvidenceVO evidenceVO =
          bdmIdentificationEvidence.getSINEvidenceValueObject(concernRoleID);
        if (!evidenceVO.getAlternateID()
          .equals(client.socialSecurityNumber)) {
          evidenceVO.setAlternateID(client.socialSecurityNumber);
          evidenceVO.setFromDate(Date.getCurrentDate());

          bdmIdentificationEvidence.createIdentificationEvidence(
            concernRoleID, evidenceVO, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

        }
      } catch (final Exception exception) {
        Trace.kTopLevelLogger.error(exception.getMessage());
      }

      // DEV fix for 21768
      final List<BDMDateofBirthEvidenceVO> list =
        bdmDateofBirthEvidence.getDOBEvidenceValueObject(concernRoleID);

      final Entity personEntity = intakeClient.getPersonEntity();

      final String lastNameAtBirth = personEntity
        .getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH);

      final String parentsLastNameAtBirth = personEntity.getAttribute(
        BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH);

      for (final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO : list) {
        if (!bdmDateofBirthEvidenceVO.getBirthLastName()
          .equals(lastNameAtBirth)
          || !bdmDateofBirthEvidenceVO.getMothersBirthLastName()
            .equals(parentsLastNameAtBirth)) {
          bdmDateofBirthEvidenceVO.setBirthLastName(lastNameAtBirth);
          bdmDateofBirthEvidenceVO
            .setMothersBirthLastName(parentsLastNameAtBirth);
          try {
            bdmDateofBirthEvidence.createDateOfBirthEvidence(concernRoleID,
              bdmDateofBirthEvidenceVO,
              EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
          } catch (final Exception exception) {
            Trace.kTopLevelLogger.error(exception.getMessage());
          }
        }
      }
    }
  }

  /**
   * There was no method header for this method so adding empty method header
   * that should be updated when this method is
   * touched
   *
   * @param intakeClient
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  MappedProspectPersonInternal
    registerPerson(final ProspectPersonMappingBean intakeClient)
      throws AppException, InformationalException {

    final Entity personEntity = intakeClient.getPersonEntity();

    final MappedProspectPersonInternal mappedProspectPerson =
      this.mappedPersonProvider.get();

    final PDCPerson person = PDCPersonFactory.newInstance();
    final PDCPersonDetails personDetails = new PDCPersonDetails();

    final String socialInsuranceNumber = personEntity
      .getAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER);

    String alternateIDType = getConfigurationAlternateIDType();
    // ADO-16370 Start Added condition to not treat SIN As Primary Alternate ID
    final boolean ssnIsPrimary = Configuration
      .getBooleanProperty(EnvVars.ENV_TREAT_SSN_AS_PRIMARY_ALTERNATEID);
    // Task 97423 START
    final Entity rootEntity =
      BDMApplicationEventsUtil.getRootEntity(personEntity);
    // Task 97423 END
    // ADO-16370 End Added condition to not treat SIN As Primary Alternate ID
    final long rootEntityID =
      BDMApplicationEventsUtil.getRootEntity(personEntity).getUniqueID();

    if (!StringHelper.isEmpty(socialInsuranceNumber)) {
      if (alternateIDType == null) {
        alternateIDType = BDMConstants.EMPTY_STRING;
      }

      // ADO-16370:Start Added condition to not treat SIN As Primary Alternate
      // ID

      if (ssnIsPrimary) {

        personDetails.alternateIDTypeCodeOpt = alternateIDType;
        personDetails.socialSecurityNumber = socialInsuranceNumber;
      } else {

        personDetails.alternateIDTypeCodeOpt = "";
        personDetails.socialSecurityNumber = "";

        final AlternateIDAndType altIdType = new AlternateIDAndType();
        altIdType.alternateID = socialInsuranceNumber;
        altIdType.alternateIDType = getConfigurationAlternateIDType();
        personDetails.personAltIDTypeList.dtls.add(altIdType);
      }
      // ADO-16370:End Added condition to not treat SIN As Primary Alternate ID
    }

    // BDM fields captured be the Application
    if (personEntity.hasAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH)) {
      personDetails.birthName = getStringAttribute(personEntity,
        BDMDatastoreConstants.LAST_NAME_AT_BIRTH);
    }
    if (personEntity.hasAttribute(BDMDatastoreConstants.PARENT_LAST_NAME)) {
      personDetails.motherBirthSurname = getStringAttribute(personEntity,
        BDMDatastoreConstants.PARENT_LAST_NAME);
    }
    // ADA-16944 Mapping Middle Name
    if (personEntity.hasAttribute(BDMDatastoreConstants.MIDDLE_NAME)) {
      personDetails.otherForename =
        getStringAttribute(personEntity, BDMDatastoreConstants.MIDDLE_NAME);
    }

    // personDetails.prefCommMethod = "";

    if (personEntity.hasAttribute(BDMDatastoreConstants.PREFERRED_NAME)) {
      personDetails.preferredName = getStringAttribute(personEntity,
        BDMDatastoreConstants.PREFERRED_NAME);
    }

    // OOTB registration details
    personDetails.countryOfBirth = COUNTRYEntry.DEFAULT().getCode();
    personDetails.maritalStatusCode = getMaritalStatus(personEntity);
    personDetails.dateOfBirth = getDateOfBirth(personEntity);
    if (personEntity.hasAttribute(BDMDatastoreConstants.FIRST_NAME)) {
      personDetails.firstForename =
        getStringAttribute(personEntity, BDMDatastoreConstants.FIRST_NAME);
    }
    personDetails.nationalityCode = NATIONALITYEntry.DEFAULT().getCode();
    personDetails.registrationDate = Date.getCurrentDate();
    if (personEntity.hasAttribute(BDMDatastoreConstants.GENDER)) {
      personDetails.gender =
        getStringAttribute(personEntity, BDMDatastoreConstants.GENDER);
    }
    if (personEntity.hasAttribute(BDMDatastoreConstants.LAST_NAME)) {
      personDetails.surname =
        getStringAttribute(personEntity, BDMDatastoreConstants.LAST_NAME);
    }

    // Handle addresses for Primarys and dependants
    boolean isPrimaryParticipant = false;
    if (personEntity
      .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)) {
      isPrimaryParticipant = ((Boolean) personEntity
        .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT))
          .booleanValue();
    }

    // check if same mailing address is the same as residential
    // handle preferred language for primary only
    // Task 63081 Copy primary applicant mailing address to dependent mailing
    // address

    if (isPrimaryParticipant) {

      // get residential address
      personDetails.addressData = getAddress(personEntity);

      // if same mailing address is not the same as residential
      if (personEntity
        .getTypedAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT)
        .equals(IEGYESNO.YES)) {
        // set to application input mailing address
        personDetails.mailingAddressData = getMailingAddress(personEntity);

      } else {
        // set to residential
        personDetails.mailingAddressData = personDetails.addressData;

      }

      // handle non primary residential address
    } else {
      // Task 97423 START
      // if the dependant is at the same address at the primary, then set
      // their address to the primarys
      final Entity[] personEntities = BDMApplicationEventsUtil
        .getEntities(rootEntityID, BDMDatastoreConstants.PERSON, rootEntity);
      // Task 97423 END
      boolean isPrimaryParentParticipant = false;
      Entity personParentEntity = null;
      for (final Entity personEnt : personEntities) {
        if (personEnt
          .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)) {
          isPrimaryParentParticipant = ((Boolean) personEnt
            .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT))
              .booleanValue();
        }

        if (isPrimaryParentParticipant) {
          personParentEntity = personEnt;
          break;
        }

      }
      // final Entity personParentEntity = personEntities[0];

      if (isPrimaryParentParticipant) {
        // if same mailing address is not the same as residential
        if (personParentEntity
          .getTypedAttribute(
            BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT)
          .equals(IEGYESNO.YES)) {
          // set to application input mailing address
          personDetails.mailingAddressData =
            getMailingAddress(personParentEntity);

        } else {
          // set to residential
          personDetails.mailingAddressData = getAddress(personParentEntity);

        }
      }

      personDetails.addressData = getAddress(personParentEntity);

    }

    final RegistrationIDDetails personRegistrationResult =
      person.insert(personDetails);

    // TASK-10287 End rearranging phone and email evidence
    if (isPrimaryParticipant) {
      createEmailAddressEvidence(personEntity,
        personRegistrationResult.concernRoleID);
      createPhoneNumberEvidence(personEntity,
        personRegistrationResult.concernRoleID);
      createContactPreferenceEvidence(personRegistrationResult.concernRoleID,
        personEntity, personDetails);
    }
    mappedProspectPerson
      .setConcernRoleID(personRegistrationResult.concernRoleID);
    mappedProspectPerson.setDataStoreID(personEntity.getUniqueID());
    mappedProspectPerson.setPrimaryClientInd(isPrimaryParticipant);
    mappedProspectPerson.setReference(personRegistrationResult.alternateID);

    return mappedProspectPerson;
  }

  /**
   * creates the phone number evidence
   *
   * @param personEntity
   * @param concernRoleID
   */
  private void createPhoneNumberEvidence(final Entity personEntity,
    final long concernRoleID) {

    try {
      final Entity[] communicationEntity =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMDatastoreConstants.COMMUNICATION_DETAILS);
      final boolean hasAlertPreference =
        BDMApplicationEventsUtil.hasChildEntities(personEntity,
          BDMDatastoreConstants.ALERT_PREFERENCES);

      for (int i = 0; i < communicationEntity.length; i++) {

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.PHONE_NUMBER)
          && communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.PHONE_NUMBER).isEmpty()) {

          return;

        }

        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

        concernRoleKey.concernRoleID = concernRoleID;

        // Get the PDC case id and primary case participant role for that case.
        final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
        final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
          pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

        final EvidenceTypeKey eType = new EvidenceTypeKey();
        eType.evidenceType = PDCConst.PDCPHONENUMBER;

        final EvidenceTypeDef evidenceType =
          etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);

        final EvidenceTypeVersionDef evTypeVersion =
          evidenceTypeVersionDefDAO.getActiveEvidenceTypeVersionAtDate(
            evidenceType, Date.getCurrentDate());

        final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
          DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

        final DynamicEvidenceDataAttributeDetails participant =
          dynamicEvidenceDataDetails.getAttribute("participant");

        DynamicEvidenceTypeConverter.setAttribute(participant,
          pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);

        Boolean isPreferredInd = false;

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM)) {

          isPreferredInd = communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM).toString()
            .equals("true") ? Boolean.TRUE : Boolean.FALSE;
        }

        final DynamicEvidenceDataAttributeDetails preferred =
          dynamicEvidenceDataDetails.getAttribute("preferredInd");
        DynamicEvidenceTypeConverter.setAttribute(preferred, isPreferredInd);

        assignPhoneNumberEvidenceDetails(dynamicEvidenceDataDetails,
          communicationEntity[i], hasAlertPreference, personEntity);

        final EvidenceControllerInterface evidenceControllerObj =
          (EvidenceControllerInterface) EvidenceControllerFactory
            .newInstance();

        // Call the EvidenceController object and insert evidence
        final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
          new EvidenceDescriptorInsertDtls();

        evidenceDescriptorInsertDtls.participantID = concernRoleID;
        evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
        evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();

        evidenceDescriptorInsertDtls.caseID =
          pdcCaseIDCaseParticipantRoleID.caseID;

        // Evidence Interface details
        final EIEvidenceInsertDtls eiEvidenceInsertDtls =
          new EIEvidenceInsertDtls();

        eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
        eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
        eiEvidenceInsertDtls.descriptor.changeReason =
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
        eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

        evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
      }
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "@@@ exception in createPhoneNumber" + e);

    }
  }

  /**
   * Assigns participant's phone number to the dynamic evidence data
   * struct.
   *
   * @param dynamicEvidenceDataDetails
   * @param entity
   * @throws AppException
   */
  private void assignPhoneNumberEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final Entity entity, final boolean hasAlertPreference,
    final Entity personEntity) throws AppException {

    final DynamicEvidenceDataAttributeDetails countryCode =
      dynamicEvidenceDataDetails.getAttribute("phoneCountryCode");
    DynamicEvidenceTypeConverter.setAttribute(countryCode,
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME,
        entity.getAttribute(BDMDatastoreConstants.COUNTRY_CODE).toString()));

    final DynamicEvidenceDataAttributeDetails phoneAreaCode =
      dynamicEvidenceDataDetails.getAttribute("phoneAreaCode");
    DynamicEvidenceTypeConverter.setAttribute(phoneAreaCode,
      entity.getAttribute(BDMDatastoreConstants.PHONE_AREA_CODE).toString());

    final DynamicEvidenceDataAttributeDetails phoneNumber =
      dynamicEvidenceDataDetails.getAttribute("phoneNumber");
    DynamicEvidenceTypeConverter.setAttribute(phoneNumber,
      entity.getAttribute(BDMDatastoreConstants.PHONE_NUMBER).toString());

    // PhoneType --
    final DynamicEvidenceDataAttributeDetails phoneType =
      dynamicEvidenceDataDetails.getAttribute("phoneType");
    DynamicEvidenceTypeConverter.setAttribute(phoneType,
      new CodeTableItem(PHONETYPE.TABLENAME,
        entity.getAttribute(BDMDatastoreConstants.PHONE_TYPE).toString()));

    final DynamicEvidenceDataAttributeDetails phoneExt =
      dynamicEvidenceDataDetails.getAttribute("phoneExtension");
    DynamicEvidenceTypeConverter.setAttribute(phoneExt,
      entity.getAttribute(BDMDatastoreConstants.PHONE_EXT).toString());

    final Boolean isPrfInd =
      entity.getAttribute(BDMDatastoreConstants.PHONE_ALT_PREF).toString()
        .equals("true") ? Boolean.TRUE : Boolean.FALSE;

    final DynamicEvidenceDataAttributeDetails useForAlertsInd =
      dynamicEvidenceDataDetails.getAttribute("useForAlertsInd");
    DynamicEvidenceTypeConverter.setAttribute(useForAlertsInd, isPrfInd);

    if (isPrfInd && hasAlertPreference) {

      final Entity alertPreference =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMDatastoreConstants.ALERT_PREFERENCES)[0];

      if (alertPreference.hasAttribute(BDMDatastoreConstants.ALERT_OCCUR)) {

        final DynamicEvidenceDataAttributeDetails alertFrequency =
          dynamicEvidenceDataDetails.getAttribute("alertFrequency");

        DynamicEvidenceTypeConverter.setAttribute(alertFrequency,
          new CodeTableItem(BDMALERTOCCUR.TABLENAME, alertPreference
            .getAttribute(BDMDatastoreConstants.ALERT_OCCUR).toString()));
      }
    }

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute("fromDate");
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());

    Boolean isMrngInd = false;
    Boolean isaftInd = false;
    Boolean isevenInd = false;
    if (entity.hasAttribute(BDMDatastoreConstants.PHONE_IS_MOR)) {

      isMrngInd = entity.getAttribute(BDMDatastoreConstants.PHONE_IS_MOR)
        .toString().equals("true") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (entity.hasAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON)) {

      isaftInd = entity.getAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON)
        .toString().equals("true") ? Boolean.TRUE : Boolean.FALSE;
    }
    if (entity.hasAttribute(BDMDatastoreConstants.PHONE_IS_EVE)) {

      isevenInd = entity.getAttribute(BDMDatastoreConstants.PHONE_IS_EVE)
        .toString().equals("true") ? Boolean.TRUE : Boolean.FALSE;
    }

    // morning
    final DynamicEvidenceDataAttributeDetails morningInd =
      dynamicEvidenceDataDetails.getAttribute("morningInd");
    DynamicEvidenceTypeConverter.setAttribute(morningInd, isMrngInd);

    // aft noon
    final DynamicEvidenceDataAttributeDetails afternoonInd =
      dynamicEvidenceDataDetails.getAttribute("afternoonInd");
    DynamicEvidenceTypeConverter.setAttribute(afternoonInd, isaftInd);

    // evening
    final DynamicEvidenceDataAttributeDetails eveningInd =
      dynamicEvidenceDataDetails.getAttribute("eveningInd");
    DynamicEvidenceTypeConverter.setAttribute(eveningInd, isevenInd);

  }

  /**
   * creates the email address evidence
   *
   * @param personEntity
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  private void createEmailAddressEvidence(final Entity personEntity,
    final long concernRoleID) throws AppException, InformationalException {

    try {
      final Entity[] communicationEntity =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMDatastoreConstants.COMMUNICATION_DETAILS);

      final Boolean hasAlertPreferenceEntity =
        BDMApplicationEventsUtil.hasChildEntities(personEntity,
          BDMDatastoreConstants.ALERT_PREFERENCES);

      for (int i = 0; i < communicationEntity.length; i++) {

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.EMAIL_ADDRESS)
          && communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.EMAIL_ADDRESS).isEmpty()) {

          return;

        }

        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

        concernRoleKey.concernRoleID = concernRoleID;

        // Get the PDC case id and primary case participant role for that case.
        final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
        final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
          pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

        final EvidenceTypeKey eType = new EvidenceTypeKey();
        eType.evidenceType = PDCConst.PDCEMAILADDRESS;

        final EvidenceTypeDef evidenceType =
          etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);

        final EvidenceTypeVersionDef evTypeVersion =
          evidenceTypeVersionDefDAO.getActiveEvidenceTypeVersionAtDate(
            evidenceType, Date.getCurrentDate());

        final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
          DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

        final DynamicEvidenceDataAttributeDetails participant =
          dynamicEvidenceDataDetails.getAttribute("participant");

        DynamicEvidenceTypeConverter.setAttribute(participant,
          pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);

        Boolean isPreferredInd = false;

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM)) {

          isPreferredInd = communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM).toString()
            .equals("true") ? Boolean.TRUE : Boolean.FALSE;
        }

        final DynamicEvidenceDataAttributeDetails preferred =
          dynamicEvidenceDataDetails.getAttribute("preferredInd");
        DynamicEvidenceTypeConverter.setAttribute(preferred, isPreferredInd);

        final DynamicEvidenceDataAttributeDetails useForAlerts =
          dynamicEvidenceDataDetails.getAttribute("useForAlertsInd");
        DynamicEvidenceTypeConverter.setAttribute(useForAlerts, false);

        assignEmailAddressEvidenceDetails(dynamicEvidenceDataDetails,
          communicationEntity[i], hasAlertPreferenceEntity, personEntity);

        final EvidenceControllerInterface evidenceControllerObj =
          (EvidenceControllerInterface) EvidenceControllerFactory
            .newInstance();

        // Call the EvidenceController object and insert evidence
        final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
          new EvidenceDescriptorInsertDtls();

        evidenceDescriptorInsertDtls.participantID = concernRoleID;
        evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
        evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();

        evidenceDescriptorInsertDtls.caseID =
          pdcCaseIDCaseParticipantRoleID.caseID;

        // Evidence Interface details
        final EIEvidenceInsertDtls eiEvidenceInsertDtls =
          new EIEvidenceInsertDtls();

        eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
        eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
        eiEvidenceInsertDtls.descriptor.changeReason =
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
        eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

        evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
      }
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "@@@ exception in create EmailAddress " + e);

    }
  }

  /**
   * Assigns participant's email to the dynamic evidence data
   * struct.
   *
   * @param details
   * Participant email
   * @PARAM paramEmailType
   * @param dynamicEvidenceDataDetails
   * Dynamic evidence details.
   */
  private void assignEmailAddressEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final Entity communicationEnity, final Boolean hasAlertPreference,
    final Entity personEntity) throws AppException {

    final DynamicEvidenceDataAttributeDetails emailType =
      dynamicEvidenceDataDetails.getAttribute("emailAddressType");
    DynamicEvidenceTypeConverter.setAttribute(emailType,
      new CodeTableItem(EMAILTYPE.TABLENAME, communicationEnity
        .getAttribute(BDMDatastoreConstants.EMAIL_TYPE).toString()));

    final DynamicEvidenceDataAttributeDetails email =
      dynamicEvidenceDataDetails.getAttribute("emailAddress");
    DynamicEvidenceTypeConverter.setAttribute(email, communicationEnity
      .getAttribute(BDMDatastoreConstants.EMAIL_ADDRESS).toString());

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute("fromDate");
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());

    final Boolean isPrfInd =
      communicationEnity.getAttribute(BDMDatastoreConstants.ALT_PREF_EMAIL)
        .toString().equals("true") ? Boolean.TRUE : Boolean.FALSE;

    final DynamicEvidenceDataAttributeDetails useForAlertsInd =
      dynamicEvidenceDataDetails.getAttribute("useForAlertsInd");
    DynamicEvidenceTypeConverter.setAttribute(useForAlertsInd, isPrfInd);

    if (isPrfInd && hasAlertPreference) {

      final Entity alertPreference =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMDatastoreConstants.ALERT_PREFERENCES)[0];
      if (alertPreference.hasAttribute(BDMDatastoreConstants.ALERT_OCCUR)) {

        final DynamicEvidenceDataAttributeDetails alertFrequency =
          dynamicEvidenceDataDetails.getAttribute("alertFrequency");
        DynamicEvidenceTypeConverter.setAttribute(alertFrequency,
          new CodeTableItem(BDMALERTOCCUR.TABLENAME, alertPreference
            .getAttribute(BDMDatastoreConstants.ALERT_OCCUR).toString()));
      }

    }

  }

  /**
   * customized OOTB Class to add new attributes to CP evidence
   *
   * @since TASK-9375
   * @param concernRoleID
   * @param personEntity
   * @param personDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void createContactPreferenceEvidence(final long concernRoleID,
    final Entity personEntity, final PDCPersonDetails personDetails)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    final EvidenceTypeDef evidenceType =
      this.etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);
    final EvidenceTypeVersionDef evTypeVersion =
      this.evidenceTypeVersionDefDAO.getActiveEvidenceTypeVersionAtDate(
        evidenceType, Date.getCurrentDate());
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);
    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute("participant");
    DynamicEvidenceTypeConverter.setAttribute(participant,
      Long.valueOf(pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID));
    assignContactPreferencesEvidenceDetails(pdcCaseIDCaseParticipantRoleID,
      personDetails, dynamicEvidenceDataDetails, personEntity);
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();
    evidenceDescriptorInsertDtls.participantID = concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;
    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

  }

  /**
   *
   * @param pdcCaseIDCaseParticipantRoleID
   * @param details
   * @param dynamicEvidenceDataDetails
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private void assignContactPreferencesEvidenceDetails(
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID,
    final PDCPersonDetails details,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final Entity personEntity) throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails preferedLanguage =
      dynamicEvidenceDataDetails.getAttribute("preferredLanguage");
    DynamicEvidenceTypeConverter.setAttribute(preferedLanguage,
      new CodeTableItem(LANGUAGE.TABLENAME, details.preferredLanguage));
    final DynamicEvidenceDataAttributeDetails preferedCommunication =
      dynamicEvidenceDataDetails.getAttribute("preferredCommunicationMethod");

    // ADA-9375 Start

    final Entity communicationPref =
      BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
        BDMDatastoreConstants.PREFERRED_LANGUAGE);

    final String preferredWrittenLang = getStringAttribute(communicationPref,
      BDMDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE);

    final String preferredOralLang = getStringAttribute(communicationPref,
      BDMDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE);

    final String commMethod = communicationPref
      .hasAttribute(BDMDatastoreConstants.RECEIVE_CORRESPONDENCE)
        ? getStringAttribute(communicationPref,
          BDMDatastoreConstants.RECEIVE_CORRESPONDENCE)
        : BDMCORRESDELIVERY.POSTALDIG;

    // prefered oral lang
    final DynamicEvidenceDataAttributeDetails preferredOralLanguage =
      dynamicEvidenceDataDetails.getAttribute("preferredOralLanguage");
    DynamicEvidenceTypeConverter.setAttribute(preferredOralLanguage,
      new CodeTableItem(BDMLANGUAGE.TABLENAME, preferredOralLang));

    // prefered written lang
    final DynamicEvidenceDataAttributeDetails preferredWrittenLanguage =
      dynamicEvidenceDataDetails.getAttribute("preferredWrittenLanguage");
    DynamicEvidenceTypeConverter.setAttribute(preferredWrittenLanguage,
      new CodeTableItem(BDMLANGUAGE.TABLENAME, preferredWrittenLang));

    // ADA-16944 Start Mapping correspondence
    if (!commMethod.isEmpty()) {
      DynamicEvidenceTypeConverter.setAttribute(preferedCommunication,
        new CodeTableItem(BDMCORRESDELIVERY.TABLENAME, commMethod));
    }
    // ADA-16944 Start Mapping correspondence
    // ADA-9375End

    final DynamicEvidenceDataAttributeDetails comments =
      dynamicEvidenceDataDetails.getAttribute("comments");
    DynamicEvidenceTypeConverter.setAttribute(comments, details.comments);
    if (!this.pdcContactPreferencesEvidencePopulators.isEmpty()) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = details.concernRoleID;
      final CaseIDKey caseIDKey = new CaseIDKey();
      caseIDKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
      final ConcernRoleDtls concernRoleDtls = new ConcernRoleDtls();
      concernRoleDtls.assign(details);
      for (final PDCContactPreferencesEvidencePopulator pdcContactPreferencesEvidencePopulator : this.pdcContactPreferencesEvidencePopulators)
        pdcContactPreferencesEvidencePopulator.populate(concernRoleKey,
          caseIDKey, concernRoleDtls, dynamicEvidenceDataDetails);
    }

  }

  /**
   * There was no method header for this method so adding empty method header
   * that should be updated when this method is
   * touched
   *
   * @param prospectPersonBean
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  MappedProspectPersonInternal
    registerProspect(final ProspectPersonMappingBean prospectPersonBean)
      throws AppException, InformationalException {

    final boolean ssnIsPrimary = Configuration
      .getBooleanProperty(EnvVars.ENV_TREAT_SSN_AS_PRIMARY_ALTERNATEID);

    final Entity personEntity = prospectPersonBean.getPersonEntity();
    final MappedProspectPersonInternal mappedProspectPerson =
      this.mappedPersonProvider.get();

    final PDCProspectPerson prospectPerson =
      PDCProspectPersonFactory.newInstance();

    final PDCProspectPersonDetails prospectDetails =
      new PDCProspectPersonDetails();

    final String socialInsuranceNumber = personEntity
      .getAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER);

    String alternateIDType = getConfigurationAlternateIDType();

    if (alternateIDType == null) {
      alternateIDType = BDMConstants.EMPTY_STRING;
    }

    if (!ssnIsPrimary) {
      mappedProspectPerson.setSSN(socialInsuranceNumber);
      prospectDetails.socialSecurityNumber = BDMConstants.EMPTY_STRING;
      prospectDetails.alternateIDTypeCodeOpt = BDMConstants.EMPTY_STRING;
      final AlternateIDAndType altIdType = new AlternateIDAndType();
      altIdType.alternateID = socialInsuranceNumber;
      altIdType.alternateIDType = getConfigurationAlternateIDType();
      prospectDetails.prospectAltIDTypeList.dtls.add(altIdType);
    } else {
      prospectDetails.alternateIDTypeCodeOpt = alternateIDType;
      prospectDetails.socialSecurityNumber = socialInsuranceNumber;
    }
    // Task 97423 START
    final Entity rootEntity =
      BDMApplicationEventsUtil.getRootEntity(personEntity);
    // Task 97423 END
    final long rootEntityID =
      BDMApplicationEventsUtil.getRootEntity(personEntity).getUniqueID();

    // map more fields captured be the Application
    if (personEntity.hasAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH)) {
      prospectDetails.personBirthName = getStringAttribute(personEntity,
        BDMDatastoreConstants.LAST_NAME_AT_BIRTH);
    }
    if (personEntity.hasAttribute(BDMDatastoreConstants.PARENT_LAST_NAME)) {
      prospectDetails.motherBirthSurname = getStringAttribute(personEntity,
        BDMDatastoreConstants.PARENT_LAST_NAME);
    }

    if (personEntity.hasAttribute(BDMDatastoreConstants.PREFERRED_NAME)) {
      prospectDetails.preferredName = getStringAttribute(personEntity,
        BDMDatastoreConstants.PREFERRED_NAME);
    }

    prospectDetails.countryOfBirth = COUNTRYEntry.DEFAULT().getCode();
    prospectDetails.maritalStatusCode = getMaritalStatus(personEntity);

    prospectDetails.dateOfBirth = getDateOfBirth(personEntity);
    if (personEntity.hasAttribute(BDMDatastoreConstants.FIRST_NAME)) {
      prospectDetails.firstForename =
        getStringAttribute(personEntity, BDMDatastoreConstants.FIRST_NAME);
    }
    prospectDetails.nationalityCode = NATIONALITYEntry.DEFAULT().getCode();
    prospectDetails.registrationDate = Date.getCurrentDate();

    if (personEntity.hasAttribute(BDMDatastoreConstants.GENDER)) {
      prospectDetails.gender =
        getStringAttribute(personEntity, BDMDatastoreConstants.GENDER);
    }
    if (personEntity.hasAttribute(BDMDatastoreConstants.LAST_NAME)) {
      prospectDetails.surname =
        getStringAttribute(personEntity, BDMDatastoreConstants.LAST_NAME);
    }

    boolean isPrimaryParticipant = false;
    // Handle addresses for Primarys and dependants
    if (personEntity
      .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)) {
      isPrimaryParticipant = ((Boolean) personEntity
        .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT))
          .booleanValue();
    }

    // if same mailing address is the same as residential, set mailing =
    // residential
    // handle preferred language for primary only
    if (isPrimaryParticipant) {

      // get residential address
      prospectDetails.addressData = getAddress(personEntity);
      prospectDetails.mailingAddressData = prospectDetails.addressData;

      // if same mailing address is the same as residential
      if (personEntity
        .getTypedAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT)
        .equals(IEGYESNO.YES)) {
        // set to application input mailing address
        prospectDetails.mailingAddressData = getMailingAddress(personEntity);
      }

      // handle non primary residential address
    } else {
      // if the dependant is at the same address at the primary, then set
      // their address to the primarys
      // Task 97423 START	  
      final Entity[] personEntities = BDMApplicationEventsUtil
        .getEntities(rootEntityID, BDMDatastoreConstants.PERSON, rootEntity);
	  // Task 97423 END
      prospectDetails.addressData = getAddress(personEntities[0]);
      prospectDetails.mailingAddressData = prospectDetails.addressData;

    }

    final RegistrationIDDetails registrationIDDetails =
      prospectPerson.insert(prospectDetails);

    final PDCPersonDetails personDetails = new PDCPersonDetails();
    personDetails.assign(prospectDetails);

    // TASK-10287 End rearranging phone and email evidence
    if (isPrimaryParticipant) {
      createEmailAddressEvidence(personEntity,
        registrationIDDetails.concernRoleID);
      createPhoneNumberEvidence(personEntity,
        registrationIDDetails.concernRoleID);
      createContactPreferenceEvidence(registrationIDDetails.concernRoleID,
        personEntity, personDetails);
    }

    mappedProspectPerson
      .setConcernRoleID(registrationIDDetails.concernRoleID);
    mappedProspectPerson.setDataStoreID(prospectPersonBean.getUniqueID());
    mappedProspectPerson.setReference(registrationIDDetails.alternateID);
    if (prospectPersonBean.primaryClientInd) {
      mappedProspectPerson.setPrimaryClientInd(true);
    }

    return mappedProspectPerson;
  }

  /**
   * Determines whether or not the provided concern role already exists on the
   * intake application
   *
   * @param intakeClient
   * @param concernRoleID
   * @return
   */
  private boolean isExactMatchAlreadyOnApplication(final Entity rootEntity,
    final long concernRoleID) {

    // Same last name, DOB, and SIN for two different people on
    // the same application, causing a stuck app
    final curam.participant.impl.ConcernRole concernRole =
      this.concernRoleDAO.get(concernRoleID);

    final IntakeApplication intakeApplication =
      intakeApplicationDAO.readByRootEntityID(rootEntity.getUniqueID());
    final List<IntakeApplicationConcernRoleLink> existingLinks =
      intakeApplicationConcernRoleLinkDAO
        .listByConcernRoleIntakeApplication(concernRole, intakeApplication);

    // TODO: logging
    Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
      + "# of existing links on intake application for concernroleid: "
      + concernRoleID);
    return !existingLinks.isEmpty();

  }

  /**
   * Determines whether or not the provided concern role is already linked to
   * another user
   *
   * @param concernRole
   * @return
   */
  private boolean isConcernRoleAlreadyLinked(final long concernRoleID) {

    // Instantiate objects used for linking User to concernRole
    final CitizenWorkspaceAccountManager citizenWorkspaceAccountManager =
      GuiceWrapper.getInjector()
        .getInstance(CitizenWorkspaceAccountManager.class);

    // Only try linking user to concernrole, if the concernrole is not already
    // linked
    try {
      final ConcernRole concernRole = concernRoleDAO.get(concernRoleID);
      if (citizenWorkspaceAccountManager.hasLinkedAccount(concernRole)) {
        return true;
      }
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Exposed for testing purposes.
   *
   * @param entity
   * @param attributeName
   * @return
   */
  @Override
  public String getStringAttribute(final Entity entity,
    final String attributeName) {

    // TODO Auto-generated method stub
    return super.getStringAttribute(entity, attributeName);
  }

  static synchronized String getConfigurationAlternateIDType()
    throws AppException, InformationalException {

    final String alternateIDType = Configuration
      .getProperty(EnvVars.ENV_WORKSPACESERVICES_MAPPING_ALTERNATEID_TYPE);

    if (alternateIDType == null) {
      return null;
    }

    if (globalAlternateIDType.equals(alternateIDType)) {
      return alternateIDType;
    }

    final CodeTable ct = CodeTableFactory.newInstance();
    final CTItemKey ck = new CTItemKey();

    ck.tableName = CONCERNROLEALTERNATEID.TABLENAME;
    ck.locale = TransactionInfo.getProgramLocale();
    ck.code = alternateIDType;
    final CTItem result = ct.getOneItem(ck);

    globalAlternateIDType = alternateIDType;
    return alternateIDType;
  }

}
