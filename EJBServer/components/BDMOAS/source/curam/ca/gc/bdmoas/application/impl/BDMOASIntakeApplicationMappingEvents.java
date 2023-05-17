package curam.ca.gc.bdmoas.application.impl;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.codetable.COUNTRY;
import curam.codetable.GENDER;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationMappingEvents;
import curam.workspaceservices.util.impl.DatastoreHelper;

/**
 * BDMOAS FEATURE 92921: Class Added
 * Class for OAS Intake application mapping events.
 *
 * @author SMisal
 *
 */
public class BDMOASIntakeApplicationMappingEvents
  extends IntakeApplicationMappingEvents {

  /*
   * Perform any required pre-mapping tasks.
   *
   * @param intakeApplication
   * The intake application in question.
   *
   * @throws AppException
   * Generic Exception Signature.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   */
  @Override
  public void preCreateCasesInCuram(final IntakeApplication intakeApplication)
    throws AppException, InformationalException {

    final String intakeScriptID =
      intakeApplication.getIntakeApplicationType().getIntakeScriptID();

    final Datastore dataStore =
      DatastoreHelper.openDatastore(intakeApplication.getSchemaName());

    final Entity applicationEntity =
      dataStore.readEntity(intakeApplication.getRootEntityID());

    final Entity[] personEntites = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON));

    Entity residentialAddress_Applicant = null;
    Entity personEntity_Partner = null;
    boolean isDiffResiAddressForPartner = true;

    for (final Entity personEntity : personEntites) {
      if (personEntity
        .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)
        && personEntity
          .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)
          .equals(Boolean.TRUE)) {

        final Entity communicationPrefEntity =
          BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
            BDMDatastoreConstants.COMMUNICATION_PREF);

        // BR 02: When OAS-GIS application is submitted then map ‘Preferred
        // language of Communication’ on IEG to ‘Preferred Language of Oral
        // Communication’ and ‘Preferred Language of Written Communication’.
        communicationPrefEntity.setAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE,
          communicationPrefEntity.getAttribute(
            BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE));

        // BR-07 When OAS-GIS application is submitted, then map Method of
        // Correspondence to "Digital and Hard copy by Mail"
        communicationPrefEntity.setAttribute(
          BDMLifeEventDatastoreConstants.RECEIVE_CORRESPONDENCE,
          BDMCORRESDELIVERY.POSTALDIG);

        communicationPrefEntity.update();

        // BR-09 When OAS-GIS application is submitted, Last name at Birth is
        // blank for Primary applicant then default Last name at Birth to Last
        // name of primary applicant.
        final String lastNameAtBirth = personEntity
          .getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH);
        if (StringUtil.isNullOrEmpty(lastNameAtBirth)) {

          final String lastName =
            personEntity.getAttribute(BDMDatastoreConstants.LAST_NAME);

          personEntity.setAttribute(
            BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH, lastName);
        }

        if (intakeScriptID.equals(
          BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
          // BR-10 When OAS -GIS application is submitted, and user selects No
          // for
          // ‘ Did the applicant indicate they were born outside of Canada?’
          // then
          // map Country of Birth as ‘Canada’ for primary applicant.
          final Entity legalStatusEntity =
            BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
              BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY);

          final String isBornOutsideOfCanada = legalStatusEntity.getAttribute(
            BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE);

          if (!StringUtil.isNullOrEmpty(isBornOutsideOfCanada)
            && isBornOutsideOfCanada.equals(IEGYESNO.NO)) {
            final String countryOfBirth = personEntity
              .getAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH);

            if (StringUtil.isNullOrEmpty(countryOfBirth)) {
              personEntity.setAttribute(
                BDMOASDatastoreConstants.COUNTRY_OF_BIRTH, COUNTRY.CA);
            }
          }
        }

        // BR11 or BR17
        final Entity[] residentialAddressEntityList =
          personEntity.getChildEntities(dataStore
            .getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
        if (residentialAddressEntityList != null
          && residentialAddressEntityList.length > 0) {
          residentialAddress_Applicant = residentialAddressEntityList[0];
        }
      } else if (personEntity
        .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)
        && personEntity
          .getTypedAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT)
          .equals(Boolean.FALSE)) {
        if (personEntity.hasAttribute(
          BDMOASDatastoreConstants.IS_DIFF_RES_ADDR_FOR_PARTNER_ATTR)
          && personEntity
            .getTypedAttribute(
              BDMOASDatastoreConstants.IS_DIFF_RES_ADDR_FOR_PARTNER_ATTR)
            .equals(IEGYESNO.NO)) {
          isDiffResiAddressForPartner = false;
          personEntity_Partner = personEntity;
        }
      }

      if (intakeScriptID.equals(
        BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)
        || intakeScriptID
          .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT)) {
        // BR-01 When registering a new prospect or person after the submission
        // of
        // OAS-GIS application, Gender should be mapped as ‘Unknown’.
        personEntity.setAttribute(BDMDatastoreConstants.GENDER,
          GENDER.UNKNOWN);
      } else {
        final String genderCode = personEntity
          .getAttribute(BDMOASDatastoreConstants.GENDER_DROPDOWN_ATTR);
        if (!StringUtil.isNullOrEmpty(genderCode)) {
          personEntity.setAttribute(BDMDatastoreConstants.GENDER, genderCode);
        }
      }

      personEntity.update();
    }

    // BR11(Agent portal application) or BR 17 (Client Portal application).
    // BR11: User/Applicant selects No for ‘Did the applicant provide a
    // different residential address for spouse/common-law partner?’ then map
    // the residential address of primary applicant to the residential address
    // of Spouse/Common-Law Partner.
    // BR17 : user selects No for ‘Does your spouse/common-law partner have a
    // different residential address than yours?
    if (personEntity_Partner != null && !isDiffResiAddressForPartner) {
      this.addResidentialAddressForPartner(dataStore, personEntity_Partner,
        residentialAddress_Applicant);
    }

  }

  @Override
  public void
    preMappingValidationsDisabled(final IntakeApplication intakeApplication)
      throws AppException, InformationalException {

    // Nothing to do

  }

  /**
   * Method to add the residential address.
   *
   * @param applicationEntity
   * @param datastore
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private void addResidentialAddressForPartner(final Datastore datastore,
    final Entity personEntity_Partner,
    final Entity residentialAddress_Applicant)
    throws AppException, InformationalException {

    final Entity[] residentialAddressEntityList =
      personEntity_Partner.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));

    Entity residentialAddress = null;
    if (residentialAddressEntityList != null
      && residentialAddressEntityList.length > 0) {
      residentialAddress = residentialAddressEntityList[0];
    } else {
      residentialAddress = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
      personEntity_Partner.addChildEntity(residentialAddress);
      personEntity_Partner.update();
    }

    if (residentialAddress_Applicant != null) {
      final String suite_Apt = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.SUITE_NUM);

      if (suite_Apt != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.SUITE_NUM,
          suite_Apt);
      }

      final String city =
        residentialAddress_Applicant.getAttribute(BDMDatastoreConstants.CITY);
      if (city != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.CITY, city);
      }

      // province for CA, and state for INTL
      final String state = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.STATE);
      if (state != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.STATE, state);
      }

      final String country = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);
      if (country != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
          country);
        personEntity_Partner
          .setAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY, country);
      }

      final String province = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.PROVINCE);
      if (province != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.PROVINCE,
          province);
      }

      final String addLine1 = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.ADDRESS_LINE1);
      if (addLine1 != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
          addLine1);
      }

      final String addLine2 = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.ADDRESS_LINE2);
      if (addLine2 != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
          addLine2);
      }

      final String postalCode = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.POSTAL_CODE);
      if (postalCode != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.POSTAL_CODE,
          postalCode);
      }

      final String zipCode = residentialAddress_Applicant
        .getAttribute(BDMDatastoreConstants.ZIP_CODE);
      if (zipCode != null) {
        residentialAddress.setAttribute(BDMDatastoreConstants.ZIP_CODE,
          zipCode);
      }
      residentialAddress.update();
      personEntity_Partner.update();
    }
  }
}
