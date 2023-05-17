package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.entity.fact.BDMUsernameGuidLinkFactory;
import curam.ca.gc.bdm.entity.intf.BDMUsernameGuidLink;
import curam.ca.gc.bdm.entity.struct.BDMUserNameGuidKey;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDetails;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.citizenworkspace.impl.StartIntakeEvents;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.PersonFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AlternateName;
import curam.core.intf.Person;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.fact.ExternalUserParticipantLinkFactory;
import curam.core.sl.entity.intf.ExternalUser;
import curam.core.sl.entity.intf.ExternalUserParticipantLink;
import curam.core.sl.entity.struct.ExternalUserDtls;
import curam.core.sl.entity.struct.ExternalUserKey;
import curam.core.sl.entity.struct.ExternalUserParticipantLinkDetails;
import curam.core.sl.entity.struct.ExternalUserParticipantLinkDetailsList;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;

/**
 * Start Intake Events Listener created to populate some attributes in the
 * datastore before IEG script is launched.
 * *
 */
public class BDMStartIntakeEventsListener implements StartIntakeEvents {

  protected static final char kEquals = '=';

  protected static final char kNewLine = '\n';

  @Inject
  BDMBankEvidence bdmBankEvidence;

  /**
   * Custom implementation of creating a new datastore for the IEG
   *
   * @param rootEntity
   * Datastore entity root of IEG script
   */
  @Override
  public void startIntake(final Entity rootEntity)
    throws AppException, InformationalException {

    // pre-populate ieg fields based on details provided on sign up
    try {
      this.prePopulateDetails(rootEntity);
    } catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * This method creates and pre-populates fields in the IEG
   *
   * @param rootEntity
   * Datastore entity root of IEG script
   * @throws AppException
   * OOTB exception
   * @throws InformationalException
   * OOTB exception
   */
  protected void prePopulateDetails(final Entity rootEntity)
    throws AppException, InformationalException, NoSuchSchemaException {

    // Get user name of the current user
    // Read details from ExternalUser table based on current user
    final String userName = TransactionInfo.getProgramUser();
    final ExternalUser externalUserObj = ExternalUserFactory.newInstance();
    final ExternalUserKey externalUserKey = new ExternalUserKey();
    externalUserKey.userName = userName;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // try find the external user dtls
    final ExternalUserDtls externalUserDtls =
      externalUserObj.read(notFoundIndicator, externalUserKey);

    // set the application channel to UA, this is used to generate the overview
    // section in application PDF
    // rootEntity.setTypedAttribute("applicationChannel", "UA");

    // dont proceed if the ExternalUser is not found
    if (notFoundIndicator.isNotFound()) {
      rootEntity.update();
      return;
    }

    // Get BDM datastore
    final Datastore datastore = rootEntity.getDatastore();

    // Get and update Person Datastore from root
    Entity personEntity = null;
    final Entity[] personEntities = rootEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON));

    // replace the CitizenPortal Schema Person with the BDM_STIMULUS_SCHEMA
    // person
    if (personEntities.length > 0) {
      // get existing Person Datastore, Can assume only 1 exists
      personEntity = Arrays.stream(personEntities).iterator().next();
      personEntity.delete();
    }
    personEntity = datastore
      .newEntity(datastore.getEntityType(BDMDatastoreConstants.PERSON));
    rootEntity.addChildEntity(personEntity);

    final Long concernRoleID =
      getConcernRoleDtlsForExternalUser(externalUserDtls);

    // populate information based on linked External User
    if (concernRoleID != 0l && externalUserDtls.roleName
      .equals(BDMDatastoreConstants.LINKED_CITIZEN_ROLE)) {
      setPersonEntityAttributes(externalUserDtls, personEntity);
      setBankEntityAttributes(externalUserDtls, concernRoleID, personEntity);
    }
    // populate information based on DECD information
    populateDECDDetails(userName, personEntity);

    try {
      rootEntity.update();
    } catch (final Exception e) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Could not update root entity for application with pre-population information: "
        + externalUserDtls.userName);
      e.printStackTrace();
    }
  }

  /**
   *
   * @param externalUserDtls
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  protected void setPersonEntityAttributes(
    final ExternalUserDtls externalUserDtls, final Entity personEntity)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // Get PersonDtls
    final Person personObj = PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    PersonDtls personDtls = new PersonDtls();
    final Long concernRoleID =
      getConcernRoleDtlsForExternalUser(externalUserDtls);

    personKey.concernRoleID = concernRoleID;

    personDtls = personObj.read(notFoundIndicator, personKey);

    if (notFoundIndicator.isNotFound()) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Person could not be found for Linked User: "
        + externalUserDtls.userName);
    } else {

      // set last name at birth
      personEntity.setTypedAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH,
        personDtls.personBirthName);

      // set parents last name at birth
      personEntity.setTypedAttribute(
        BDMDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH,
        personDtls.motherBirthSurname);

      // set marital status
      personEntity.setTypedAttribute(BDMDatastoreConstants.MARITAL_STATUS,
        personDtls.maritalStatusCode);

      // set gender
      personEntity.setTypedAttribute(BDMDatastoreConstants.GENDER,
        personDtls.gender);

      // set DOB
      personEntity.setTypedAttribute(BDMDatastoreConstants.DATE_OF_BIRTH,
        personDtls.dateOfBirth);

      // set preferred name
      personEntity.setTypedAttribute(BDMDatastoreConstants.PREFERRED_NAME,
        getPreferredName(concernRoleID));

      // Set SIN
      personEntity.setTypedAttribute(BDMDatastoreConstants.SIN,
        BDMUtil.getAlternateIDByConcernRoleIDType(concernRoleID,
          CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER));

      // Set residential country
      // TODO grab relevant Country Code when information is available
      personEntity
        .setTypedAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY, "CA");

      // Set mailing country
      // TODO grab relevant Country Code when information is available
      personEntity.setTypedAttribute(BDMDatastoreConstants.MAILING_COUNTRY,
        "CA");

      // get AlternateNameDtls
      final AlternateName alternateNameObj =
        AlternateNameFactory.newInstance();
      final AlternateNameKey alternateNameKey = new AlternateNameKey();
      AlternateNameDtls alternateNameDtls = new AlternateNameDtls();

      alternateNameKey.alternateNameID = personDtls.primaryAlternateNameID;

      alternateNameDtls =
        alternateNameObj.read(notFoundIndicator, alternateNameKey);

      if (notFoundIndicator.isNotFound()) {
        Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
          + "Alternate Name could not be found for Linked User: "
          + externalUserDtls.userName);
      } else {

        // set first name
        personEntity.setTypedAttribute(BDMDatastoreConstants.FIRST_NAME,
          alternateNameDtls.firstForename);

        // set other given name
        personEntity.setTypedAttribute(BDMDatastoreConstants.MIDDLE_NAME,
          alternateNameDtls.otherForename);

        // set last name
        personEntity.setTypedAttribute(BDMDatastoreConstants.LAST_NAME,
          alternateNameDtls.surname);
      }
    }

    try {
      personEntity.update();
    } catch (final Exception e) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Could not update person entity for application with pre-population information for: "
        + externalUserDtls.userName);
      e.printStackTrace();
    }
  }

  /**
   * This method creates and pre-populates fields in the IEG based on whether
   * there is DECD information available
   *
   * @param rootEntity
   * Datastore entity root of IEG script
   * @throws AppException
   * OOTB exception
   * @throws InformationalException
   * OOTB exception
   */
  protected void populateDECDDetails(final String userName,
    final Entity personEntity)
    throws AppException, InformationalException, NoSuchSchemaException {

    final BDMUsernameGuidLink bdmUserNameGuidLinkObj =
      BDMUsernameGuidLinkFactory.newInstance();

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final BDMUsernameGuidLinkKey bdmUsernameGuidLinkKey =
      new BDMUsernameGuidLinkKey();
    final BDMUserNameGuidKey bdmUserNameGuidKey = new BDMUserNameGuidKey();
    bdmUserNameGuidKey.username = userName;

    final BDMUsernameGuidLinkDetails bdmUsernameGuidLinkDetials =
      bdmUserNameGuidLinkObj.readGuidByUserName(notFoundIndicator,
        bdmUserNameGuidKey);
    // dont proceed if the BDMUsernameGuidLinkDetails is not found
    if (notFoundIndicator.isNotFound()) {
      return;
    }
    bdmUsernameGuidLinkKey.usernameGuidLinkID =
      bdmUsernameGuidLinkDetials.usernameGuidLinkID;
    final BDMUsernameGuidLinkDtls bdmUsernameGuidLinkDtls =
      bdmUserNameGuidLinkObj.read(notFoundIndicator, bdmUsernameGuidLinkKey);
    // dont proceed if the BDMUsernameGuidLinkDtls is not found
    if (notFoundIndicator.isNotFound()
      || bdmUsernameGuidLinkDtls.guidSavedInformation
        .equals(CuramConst.gkEmpty)) {
      return;
    }

    // try parse the guid information
    String country_of_residence = "";
    String current_marital_status = "";
    String net_income = "";
    try {
      final JSONObject decdJSON =
        new JSONObject(bdmUsernameGuidLinkDtls.guidSavedInformation);
      country_of_residence =
        decdJSON.getString(BDMConstants.kcountry_of_residence);
      current_marital_status =
        decdJSON.getString(BDMConstants.kcurrent_marital_status);
      net_income = decdJSON.getString(BDMConstants.knet_income);

    } catch (final JSONException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      return;
    }

    // Get BDM datastore
    final Datastore datastore = personEntity.getParentEntity().getDatastore();

    // Update income on Income entity
    if (!StringUtil.isNullOrEmpty(net_income)) {
      // assume no income entity already exists
      Entity incomeEntity = null;
      incomeEntity = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.INCOME_ENTITY));
      personEntity.addChildEntity(incomeEntity);
      // Set income
      incomeEntity.setTypedAttribute(BDMDatastoreConstants.INCOME_AMOUNT,
        net_income);

      try {
        incomeEntity.update();
      } catch (final Exception e) {
        Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
          + "Could not update income entity for application with decd pre-population information");
        e.printStackTrace();
      }
    }

    if (!StringUtil.isNullOrEmpty(country_of_residence)) {
      // Set residential country
      personEntity.setTypedAttribute(
        BDMDatastoreConstants.RESIDENTIAL_COUNTRY, country_of_residence);
      // Set mailing country
      personEntity.setTypedAttribute(BDMDatastoreConstants.MAILING_COUNTRY,
        country_of_residence);
    }
    if (!StringUtil.isNullOrEmpty(current_marital_status)) {
      // set marital status
      personEntity.setTypedAttribute(BDMDatastoreConstants.MARITAL_STATUS,
        current_marital_status);
    }

    try {
      personEntity.update();
    } catch (final Exception e) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Could not update person entity for application with decd pre-population information");
      e.printStackTrace();
    }
  }

  /**
   * Pre-populate information for each active Bank account evidence that is
   * available for the Person
   *
   * @param externalUserDtls
   * @param concernRoleID
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  protected void setBankEntityAttributes(
    final ExternalUserDtls externalUserDtls, final Long concernRoleID,
    final Entity personEntity) throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // Get PersonDtls
    final Person personObj = PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    PersonDtls personDtls = new PersonDtls();

    personKey.concernRoleID = concernRoleID;
    personDtls = personObj.read(notFoundIndicator, personKey);

    if (notFoundIndicator.isNotFound()) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Person could not be found for Linked User: "
        + externalUserDtls.userName);
    } else {
      // Get bank accounts from dyn evidence
      // Only displays open bank accounts that have an end date in the future or
      // no end date. If two accounts are the same then display the one that has
      // the latest startdate, then latest effective date/time.
      final HashMap<String, Integer> bankAccountMap =
        new HashMap<String, Integer>();

      final List<BDMBankEvidenceVO> bdmBankEvidenceVOList =
        bdmBankEvidence.getBankEvidenceValueObjectList(concernRoleID);
      if (!bdmBankEvidenceVOList.isEmpty()) {
        // first sweep: set up the hashmap
        for (int i = 0; i < bdmBankEvidenceVOList.size(); i++) {
          final BDMBankEvidenceVO bdmBankEvidenceVO =
            bdmBankEvidenceVOList.get(i);
          // Get payment from dyn evidence
          // paymentBanksEntity Entity
          if (bdmBankEvidenceVO.getToDate() == null
            || bdmBankEvidenceVO.getToDate().isZero()
            || bdmBankEvidenceVO.getToDate().after(Date.getCurrentDate())) {
            final String key = bdmBankEvidenceVO.getSortCode()
              + bdmBankEvidenceVO.getAccountNumber();
            if (bankAccountMap.containsKey(key)) {
              final Integer loc = bankAccountMap.get(key);
              if (!bdmBankEvidenceVOList.get(loc.intValue()).getFromDate()
                .after(bdmBankEvidenceVO.getFromDate())) {
                // replace with the bank account with latest startdate
                bankAccountMap.replace(key, i);
              }
            } else {
              bankAccountMap.put(key, i);
            }
          }
        }
        // second sweep: Set bank account info into datastore
        if (!bankAccountMap.isEmpty()) {
          personEntity.setAttribute(
            BDMLifeEventDatastoreConstants.HAS_ACTIVE_BANK_ACCOUNTS, "true");
        }
        final Iterator hmIterator = bankAccountMap.entrySet().iterator();
        while (hmIterator.hasNext()) {
          final Map.Entry mapElement = (Map.Entry) hmIterator.next();
          final int i = (int) mapElement.getValue();
          final BDMBankEvidenceVO bdmBankEvidenceVO =
            bdmBankEvidenceVOList.get(i);
          final Entity paymentBanksEntity =
            personEntity.getDatastore().newEntity(personEntity.getDatastore()
              .getEntityType(BDMLifeEventDatastoreConstants.PAYMENT_BANKS));
          personEntity.addChildEntity(paymentBanksEntity);

          // final Element paymentBanksEntity =
          // new Element(BDMLifeEventDatastoreConstants.PAYMENT_BANKS);
          final String bankAccountName = bdmBankEvidenceVO.getAccountName();
          final String accountNumber = bdmBankEvidenceVO.getAccountNumber();
          final Long bankEvidenceID = bdmBankEvidenceVO.getEvidenceID();
          // Set payment into datastore
          paymentBanksEntity.setAttribute(
            BDMLifeEventDatastoreConstants.BANK_ACCT_NUM, accountNumber);
          paymentBanksEntity.setAttribute(
            BDMLifeEventDatastoreConstants.BANK_BRANCH_NUM,
            bdmBankEvidenceVO.getBranchTransitNumber());
          paymentBanksEntity.setAttribute(
            BDMLifeEventDatastoreConstants.BANK_INSTITUTION_CODE,
            bdmBankEvidenceVO.getFinancialInstitutionNumber());
          paymentBanksEntity.setAttribute(
            BDMLifeEventDatastoreConstants.BANK_ACCOUNT_NAME_AND_NUMBER,
            bankAccountName + " (xxx"
              + accountNumber.substring(accountNumber.length() - 4) + ")");
          paymentBanksEntity.setAttribute(
            BDMLifeEventDatastoreConstants.PAYMENT_BANK_EVIDENCE_ID,
            bankEvidenceID.toString());
          paymentBanksEntity.update();
          // personEntity.addContent(paymentBanksEntity);
        }
      }
    }
    try {
      personEntity.update();
    } catch (final Exception e) {
      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Could not update bank entity for  application with pre-population information for: "
        + externalUserDtls.userName);
      e.printStackTrace();
    }
  }

  /**
   * Get the concernroleID for the passed External User
   *
   * @param externalUserDtls
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected Long
    getConcernRoleDtlsForExternalUser(final ExternalUserDtls externalUserDtls)
      throws AppException, InformationalException {

    final ExternalUserParticipantLink externalUserParticipantLink =
      ExternalUserParticipantLinkFactory.newInstance();
    final ExternalUserKey externalUserKey = new ExternalUserKey();
    externalUserKey.userName = externalUserDtls.userName;

    ExternalUserParticipantLinkDetailsList externalUserParticipantLinkDetailsList =
      new ExternalUserParticipantLinkDetailsList();
    externalUserParticipantLinkDetailsList =
      externalUserParticipantLink.searchExtendedByUsername(externalUserKey);

    if (!externalUserParticipantLinkDetailsList.dtls.isEmpty()) {
      final ExternalUserParticipantLinkDetails externalUserParticipantLinkDetails =
        externalUserParticipantLinkDetailsList.dtls.get(0);
      final Long externalUserConcernRole =
        externalUserParticipantLinkDetails.participantRoleID;
      return externalUserConcernRole;
    }

    return 0l;
  }

  /**
   * Gets the preferred name info for the person
   *
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  protected String getPreferredName(final long concernRoleID)
    throws AppException, InformationalException {

    final BDMNamesEvidence bdmNamesEvidence = new BDMNamesEvidence();
    final List<BDMNamesEvidenceVO> bdmNamesEvidenceVOList =
      bdmNamesEvidence.getNamesEvidenceValueObject(concernRoleID);
    if (!bdmNamesEvidenceVOList.isEmpty()) {
      for (final BDMNamesEvidenceVO bdmNamesEvidenceVO : bdmNamesEvidenceVOList) {
        if (bdmNamesEvidenceVO.getNameType()
          .equals(ALTERNATENAMETYPE.PREFERRED)) {
          return bdmNamesEvidenceVO.getFirstName();
        }
      }
    }
    return "";
  }

}
