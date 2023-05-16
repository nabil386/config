package curam.workspaceservices.applicationprocessing.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatchImpl;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.IEGYESNO;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.codetable.impl.COUNTRYEntry;
import curam.codetable.impl.NATIONALITYEntry;
import curam.core.facade.fact.ProspectPersonFactory;
import curam.core.facade.struct.ProspectPersonRegistrationDetails;
import curam.core.facade.struct.ProspectPersonRegistrationResult;
import curam.core.impl.EnvVars;
import curam.core.struct.AlternateIDAndType;
import curam.datastore.impl.Entity;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.struct.PDCProspectPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLink;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLinkDAO;
import curam.workspaceservices.intake.impl.IntakeApplicationDAO;
import curam.workspaceservices.mappingbeans.impl.ProspectPersonMappingBean;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * OAS Implementation class extending custom BDMIntakeClientManagementImpl
 *
 *
 */

@SuppressWarnings("restriction")
public class BDMOASIntakeClientManagementImpl
  extends BDMIntakeClientManagementImpl {

  private static final String EXISTING_LINKS_FOR_CRID_STR =
    " Num of existing links on intake application for concernroleid: ";

  private static final String EXISTS_STR =
    " already exists on the application.";

  private static final String EXACT_MATCH_STR =
    " Exact match with concernRoleId: ";

  private static final String PARTIAL_MATCH_FOR =
    " Partial match found for person: ";

  @Inject
  private Provider<MappedProspectPersonInternal> mappedPersonProvider;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private IntakeApplicationConcernRoleLinkDAO intakeApplicationConcernRoleLinkDAO;

  @Inject
  private IntakeApplicationDAO intakeApplicationDAO;

  /**
   * Attempts to
   * find an existing person in the system
   * and map the person from the submitted application to the existing person.
   * If a matching person cannot
   * be found then either register the person in the
   * submitted application as a Prospect Person or a
   * Person.
   *
   * <p>
   * If the following conditions are met then the person in the
   * {@code intakeClient} parameter will be
   * registered as a <br>
   * <br>
   * <b> Prospect Person:</b>
   * <UL>
   * <LI>If the client information supplied in the application is partially
   * matched to an
   * existing client in the system,
   * and the the
   * {@code registerAsProspectParam}
   * parameter is set to true (this is set OOTB using a system property).</LI>
   * </UL>
   * <br>
   * <b> Person: </b>
   * <UL>
   * <LI>If neither exact match nor a partial match criteria succeeds, register
   * the client as a Person.
   * </LI>
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
   * </p>
   */
  @Override
  public MappedProspectPersonInternal getMappedPerson(
    final ProspectPersonMappingBean intakeClient,
    final boolean registerAsProspectParam)
    throws AppException, InformationalException {

    TransactionInfo.setFacadeScopeObject(
      Configuration.getProperty(EnvVars.ENV_CANEDIT_SSN), false);

    if (intakeClient.isExistingConcernRole().booleanValue()) {
      final curam.participant.impl.ConcernRole concernRole =
        this.concernRoleDAO.get(intakeClient.getConcernRoleID());

      if (concernRole.getConcernRoleType().equals(CONCERNROLETYPEEntry.PERSON)
        || concernRole.getConcernRoleType()
          .equals(CONCERNROLETYPEEntry.PROSPECTPERSON)) {
        // Check for exact match criteria for OAS
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
   * BDM OAS custom person match
   *
   * @param intakeClient
   * @return existing person if an exact match was found, or a newly
   * registered prospect for a partial match
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public MappedProspectPersonInternal
    findPersonMatch(final ProspectPersonMappingBean intakeClient)
      throws AppException, InformationalException {

    final Entity personEntity = intakeClient.getPersonEntity();
    final Entity rootEntity = personEntity.getParentEntity();

    final String externalUserName =
      rootEntity.getAttribute(BDMDatastoreConstants.EXTERNAL_USER_NAME);

    // For external users, invoke BDM implementation.
    if (StringUtils.isNotEmpty(externalUserName)) {
      return super.findPersonMatch(intakeClient);
    }

    final boolean isPrimaryParticipant = ((Boolean) personEntity
      .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT))
        .booleanValue();

    MappedProspectPersonInternal mappedProspectPerson = null;

    final BDMOASPersonMatchImpl bdmOASPersonMatchImpl =
      new BDMOASPersonMatchImpl();

    final BDMPersonSearchResultDetailsList exactMatchResults =
      bdmOASPersonMatchImpl.performExactMatch(intakeClient);
    if (exactMatchResults.dtls.size() == 1) {
      // if the exact match is already on the intake application, set it as a
      // partial match
      if (isExactMatchAlreadyPresentOnApplication(rootEntity,
        exactMatchResults.dtls.get(0).concernRoleID)) {
        if (Trace.atLeast(Trace.kTraceOn)) {
          Trace.kTopLevelLogger.info(BDMOASConstants.BDM_OAS_LOG_FORMATTER_4,
            BDMOASConstants.BDM_OAS_LOGS_PREFIX, EXACT_MATCH_STR,
            exactMatchResults.dtls.get(0).concernRoleID, EXISTS_STR);
        }
        return registerProspect(intakeClient);
      }

      if (Trace.atLeast(Trace.kTraceOn)) {
        Trace.kTopLevelLogger.info(BDMOASConstants.BDM_OAS_LOG_FORMATTER_3,
          BDMOASConstants.BDM_OAS_LOGS_PREFIX, EXACT_MATCH_STR,
          exactMatchResults.dtls.get(0).concernRoleID);
      }
      mappedProspectPerson = this.mappedPersonProvider.get();
      mappedProspectPerson
        .setConcernRoleID(exactMatchResults.dtls.get(0).concernRoleID);
      mappedProspectPerson.setDataStoreID(personEntity.getUniqueID());
      mappedProspectPerson.setPrimaryClientInd(isPrimaryParticipant);
    } else if (bdmOASPersonMatchImpl.isPartialMatchFound(intakeClient)) {
      if (Trace.atLeast(Trace.kTraceOn)) {
        Trace.kTopLevelLogger.info(BDMOASConstants.BDM_OAS_LOG_FORMATTER_3,
          BDMOASConstants.BDM_OAS_LOGS_PREFIX, PARTIAL_MATCH_FOR,
          intakeClient.getProspectPersonDetails().firstForename);
      }
      mappedProspectPerson = registerProspect(intakeClient);
    } else {
      // if no exact, nor partial match, then register person
      mappedProspectPerson = registerPerson(intakeClient);
    }

    return mappedProspectPerson;
  }

  /**
   * Registers a prospect person.
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

    final ProspectPersonRegistrationDetails details =
      new ProspectPersonRegistrationDetails();

    details.prospectPersonRegistrationDtls.assign(prospectDetails);

    final ProspectPersonRegistrationResult registrationResult =
      ProspectPersonFactory.newInstance().registerProspectPerson(details);

    mappedProspectPerson.setConcernRoleID(
      registrationResult.registrationIDDetails.concernRoleID);
    mappedProspectPerson.setDataStoreID(prospectPersonBean.getUniqueID());
    mappedProspectPerson
      .setReference(registrationResult.registrationIDDetails.alternateID);
    if (prospectPersonBean.primaryClientInd) {
      mappedProspectPerson.setPrimaryClientInd(true);
    }

    return mappedProspectPerson;
  }

  /**
   * Determines whether or not given concern role already exists on the
   * intake application
   *
   * @param intakeClient
   * @param concernRoleID
   * @return
   */
  private boolean isExactMatchAlreadyPresentOnApplication(
    final Entity rootEntity, final long concernRoleID) {

    // Same last name, DOB, and SIN for two different people on
    // the same application, causing a stuck app
    final curam.participant.impl.ConcernRole concernRole =
      this.concernRoleDAO.get(concernRoleID);

    final IntakeApplication intakeApplication =
      intakeApplicationDAO.readByRootEntityID(rootEntity.getUniqueID());
    final List<IntakeApplicationConcernRoleLink> existingLinks =
      intakeApplicationConcernRoleLinkDAO
        .listByConcernRoleIntakeApplication(concernRole, intakeApplication);

    if (Trace.atLeast(Trace.kTraceOn)) {
      Trace.kTopLevelLogger.info(BDMOASConstants.BDM_OAS_LOG_FORMATTER_5,
        BDMOASConstants.BDM_OAS_LOGS_PREFIX, EXISTING_LINKS_FOR_CRID_STR,
        concernRoleID, BDMOASConstants.BDM_OAS_EQUALS, existingLinks.size());
    }

    return !existingLinks.isEmpty();

  }

}
