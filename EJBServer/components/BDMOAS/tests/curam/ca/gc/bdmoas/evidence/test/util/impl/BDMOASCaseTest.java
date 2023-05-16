package curam.ca.gc.bdmoas.evidence.test.util.impl;

import curam.codetable.COUNTRY;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.GENDER;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.codetable.METHODOFAPPLICATION;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.intf.ApplicationCase;
import curam.commonintake.facade.struct.ApplicationCaseCreateDetails;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.intf.Person;
import curam.core.facade.struct.CreateIntegratedCaseDetails1;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.AddressDataFactory;
import curam.core.intf.AddressData;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.LayoutKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;

/**
 * A utility for testing apply change level evidence requirements.
 */
public class BDMOASCaseTest extends CuramServerTestJUnit4 {

  /**
   * Creates an OAS Integrated Case for the given concernRoleID.
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CreateIntegratedCaseResult createIntegratedCase(
    final long concernRoleID) throws AppException, InformationalException {

    final CreateIntegratedCaseDetails1 caseDetails =
      new CreateIntegratedCaseDetails1();
    caseDetails.integratedCaseType = PRODUCTCATEGORY.OAS_OLD_AGE_SECURITY;
    caseDetails.primaryClientID = caseDetails.primaryClientID = concernRoleID;

    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();
    final CreateIntegratedCaseResult integratedCaseResult =
      integratedCase.createIntegratedCase1(caseDetails);

    return integratedCaseResult;

  }

  public ApplicationCaseKey createApplicationCase(final long concernRoleID,
    final long applicationCaseAdminID)
    throws AppException, InformationalException {

    final ApplicationCase applicationCaseObj =
      ApplicationCaseFactory.newInstance();
    final ApplicationCaseCreateDetails applicationCaseCreateDetails =
      new ApplicationCaseCreateDetails();
    applicationCaseCreateDetails.concernRoleID = concernRoleID;
    applicationCaseCreateDetails.dtls.applicationCaseAdminID =
      applicationCaseAdminID;
    applicationCaseCreateDetails.dtls.applicationDate = getToday();
    applicationCaseCreateDetails.dtls.methodOfApplication =
      METHODOFAPPLICATION.PAPER;

    return applicationCaseObj
      .createApplicationCaseForConcernRole(applicationCaseCreateDetails);

  }

  /**
   * Registers a person for test purposes.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PersonRegistrationResult registerPerson()
    throws AppException, InformationalException {

    final Person person = PersonFactory.newInstance();
    final PersonRegistrationDetails personDtls =
      new PersonRegistrationDetails();

    personDtls.personRegistrationDetails.firstForename = "Testington";
    personDtls.personRegistrationDetails.surname = "Bear";
    personDtls.personRegistrationDetails.dateOfBirth =
      Date.fromISO8601("19500101");
    personDtls.personRegistrationDetails.sex = GENDER.MALE;
    personDtls.personRegistrationDetails.addressData = this.getAddressData();
    personDtls.personRegistrationDetails.mailingAddressData =
      personDtls.personRegistrationDetails.addressData;
    personDtls.personRegistrationDetails.registrationDate =
      Date.getCurrentDate();
    personDtls.personRegistrationDetails.nationality = "CA";
    personDtls.personRegistrationDetails.birthCountry = "CA";
    personDtls.personRegistrationDetails.currentMaritalStatus = "Married";

    final PersonRegistrationResult result = person.register(personDtls);

    return result;

  }

  /**
   * Creates a dummy address data string.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getAddressData() throws AppException, InformationalException {

    final AddressData addressData = AddressDataFactory.newInstance();
    final LayoutKey layoutKey = addressData.getLayoutForLocale(null);

    final AddressFieldDetails addressDtls = new AddressFieldDetails();
    addressDtls.addressLayoutType = layoutKey.addressLayoutType;
    addressDtls.addressLine1 = "10 10th Street";
    addressDtls.city = "Newton";
    addressDtls.province = PROVINCETYPE.ALBERTA;
    addressDtls.postalCode = "J7A 0A5";
    addressDtls.countryCode = COUNTRY.CA;

    return addressData.parseFieldsToData(addressDtls).addressData;

  }

  /**
   * Creates a dynamic evidence of the given type with the given attributes.
   *
   * @param caseID
   * @param concernRoleID
   * @param evidenceType
   * @param attributes
   * @param effectiveFrom
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public EIEvidenceKey createEvidence(final long caseID,
    final long concernRoleID, final CASEEVIDENCEEntry evidenceType,
    final Map<String, String> attributes, final Date effectiveFrom)
    throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evidenceType.getCode(),
        Date.getCurrentDate());

    for (final String key : attributes.keySet()) {
      dynamicEvidenceDataDetails.getAttribute(key)
        .setValue(attributes.get(key));
    }

    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.caseID = caseID;
    eiEvidenceInsertDtls.descriptor.evidenceType = evidenceType.getCode();
    eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
    eiEvidenceInsertDtls.descriptor.receivedDate = Date.getCurrentDate();
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.INITIAL;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    return evidenceController.insertEvidence(eiEvidenceInsertDtls);

  }

  public EIEvidenceKey createEvidenceWithParent(final long caseID,
    final long concernRoleID, final CASEEVIDENCEEntry evidenceType,
    final Map<String, String> attributes, final Date effectiveFrom,
    final Date receivedDate, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evidenceType.getCode(),
        Date.getCurrentDate());

    for (final String key : attributes.keySet()) {
      dynamicEvidenceDataDetails.getAttribute(key)
        .setValue(attributes.get(key));
    }

    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.caseID = caseID;
    eiEvidenceInsertDtls.descriptor.evidenceType = evidenceType.getCode();
    eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
    eiEvidenceInsertDtls.descriptor.receivedDate = receivedDate;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.INITIAL;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;
    eiEvidenceInsertDtls.parentKey = parentKey;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    return evidenceController.insertEvidence(eiEvidenceInsertDtls);

  }

}
