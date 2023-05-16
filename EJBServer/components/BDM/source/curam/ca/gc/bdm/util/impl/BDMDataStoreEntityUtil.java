package curam.ca.gc.bdm.util.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author amod.gole
 *
 * Class to Read DEtails from the Evidence and set to entity
 *
 * Task Date Name Description
 * 9418 05/09/2022 AGOLE New Class for Address Utility function defined for AG
 * script
 *
 */
public class BDMDataStoreEntityUtil {

  @Inject
  BDMPhoneEvidence bdmPhoneEvidence;

  @Inject
  BDMEmailEvidence bdmEmailEvidence;

  @Inject
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  @Inject
  BDMBankEvidence bdmBankEvidence;

  public BDMDataStoreEntityUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Method to get Communication Details from the Evidence and set to entity
   *
   * @param personEntity
   * @param datastore
   * @param concernRoleID
   * @param type
   *
   *
   * @throws AppException
   * @throws InformationalException
   */
  public Entity getCommunicationDetailsEnity(final Entity personEntity,
    final Datastore datastore, final long concernRoleID, final String type)
    throws AppException, InformationalException {

    final Entity[] communicationDetailsEntityList =
      personEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.COMMUNICATION_DETAILS));

    Entity communicationDetailsPrimary = null;
    Entity communicationDetailsAlternate = null;
    boolean existingEntity = false;

    if (communicationDetailsEntityList != null
      && communicationDetailsEntityList.length > 0) {
      if (communicationDetailsEntityList.length == 1) {

        communicationDetailsPrimary = communicationDetailsEntityList[0];
      }
      if (communicationDetailsEntityList.length == 2) {
        for (final Entity communicationDetails : communicationDetailsEntityList) {
          if (communicationDetails
            .getAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM).toString()
            .equals("true")) {
            communicationDetailsPrimary = communicationDetails;
          } else {
            communicationDetailsAlternate = communicationDetails;
          }
        }
        existingEntity = true;
      }
    } else {
      communicationDetailsPrimary = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.COMMUNICATION_DETAILS));
      communicationDetailsPrimary
        .setAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM, "true");
      communicationDetailsAlternate = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.COMMUNICATION_DETAILS));
      communicationDetailsAlternate
        .setAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM, "false");
    }

    final List<BDMPhoneEvidenceVO> phoneEvidenceList =
      bdmPhoneEvidence.getEvidenceValueObject(concernRoleID);
    final ListIterator<BDMPhoneEvidenceVO> listIter =
      phoneEvidenceList.listIterator();
    while (listIter.hasNext()) {
      final BDMPhoneEvidenceVO bdmPhoneEvidenceVO = listIter.next();
      // only retrieve phone number with "from date" is equal to today or in
      // the past; "to date" is blank or in the future
      if ((bdmPhoneEvidenceVO.getFromDate().before(Date.getCurrentDate())
        || bdmPhoneEvidenceVO.getFromDate().equals(Date.getCurrentDate()))
        && (bdmPhoneEvidenceVO.getToDate() == null
          || bdmPhoneEvidenceVO.getToDate().isZero()
          || bdmPhoneEvidenceVO.getToDate().after(Date.getCurrentDate()))) {

        // Get phone and evidenceID from dyn evidence
        final String phoneCountryCode =
          bdmPhoneEvidenceVO.getPhoneCountryCode();
        final String phoneAreaCode = bdmPhoneEvidenceVO.getPhoneAreaCode();
        final String phoneNumber = bdmPhoneEvidenceVO.getPhoneNumber();
        final String fullPhoneNumber = phoneAreaCode + "-" + phoneNumber;
        final String phoneExtension =
          bdmPhoneEvidenceVO.getPhoneExtension() == null ? ""
            : bdmPhoneEvidenceVO.getPhoneExtension();
        bdmPhoneEvidenceVO.getPhoneExtension();
        final String phoneType = bdmPhoneEvidenceVO.getPhoneType();
        final Boolean preferredInd = bdmPhoneEvidenceVO.isPreferredInd();
        final Boolean useForAlertsInd =
          bdmPhoneEvidenceVO.isUseForAlertsInd();
        final Boolean morningInd = bdmPhoneEvidenceVO.isMorningInd();
        final Boolean afternoonInd = bdmPhoneEvidenceVO.isAfternoonInd();
        final Boolean eveningInd = bdmPhoneEvidenceVO.isEveningInd();
        final Long evidenceID = bdmPhoneEvidenceVO.getEvidenceID();

        Entity communicationDetails = null;
        if (phoneEvidenceList.size() == 1
          && (preferredInd == null || !preferredInd) || preferredInd) {
          communicationDetails = communicationDetailsPrimary;
        } else {
          communicationDetails = communicationDetailsAlternate;

        }
        // Set phone and evidenceID into datastore
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.COUNTRY_CODE, phoneCountryCode);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_AREA_CODE, phoneAreaCode);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_NUMBER, phoneNumber);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.FULL_PHONE_NUMBER, fullPhoneNumber);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_EXT, phoneExtension);
        communicationDetails
          .setAttribute(BDMLifeEventDatastoreConstants.PHONE_TYPE, phoneType);
        // communicationDetails.setAttribute(
        // BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
        // preferredInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_ALT_PREF,
          useForAlertsInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_MOR, morningInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_AFTNOON,
          afternoonInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_EVE, eveningInd.toString());
        // communicationDetails.setAttribute(BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID,
        // evidenceID.toString());
      }
    }

    // Email Evidence Mapping
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      bdmEmailEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmEmailEvidenceVOList.isEmpty()) {
      for (final BDMEmailEvidenceVO bdmEmailEvidenceVO : bdmEmailEvidenceVOList) {
        // only retrieve email with "from date" is equal to today or in
        // the past; "to date" is blank or in the future
        if ((bdmEmailEvidenceVO.getFromDate().before(Date.getCurrentDate())
          || bdmEmailEvidenceVO.getFromDate().equals(Date.getCurrentDate()))
          && (bdmEmailEvidenceVO.getToDate() == null
            || bdmEmailEvidenceVO.getToDate().isZero()
            || bdmEmailEvidenceVO.getToDate().after(Date.getCurrentDate()))) {
          // Get email and evidenceID dyn evidence
          Entity communicationDetails = null;
          // communicationDetails = new Element("CommunicationDetails");
          // Get email and evidenceID from dyn evidence
          final String emailAdr = bdmEmailEvidenceVO.getEmailAddress();
          final String emailType = bdmEmailEvidenceVO.getEmailAddressType();
          final Boolean isPreferredCommunication =
            bdmEmailEvidenceVO.isPreferredInd();
          final Boolean alertPrefEmail =
            bdmEmailEvidenceVO.isUseForAlertsInd();
          final Long emailEvidenceID = bdmEmailEvidenceVO.getEvidenceID();
          // Set last name at birth, parents last name at birth, DOB and
          // evidenceID into datastore
          if (bdmEmailEvidenceVOList.size() == 1
            && (isPreferredCommunication == null || !isPreferredCommunication)
            || isPreferredCommunication) // Assuming only one phone evidence for
                                         // partcipant/ if
          {
            communicationDetails = communicationDetailsPrimary;
          } else {
            communicationDetails = communicationDetailsAlternate;

          }
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_ADDRESS, emailAdr);
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_TYPE, emailType);
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.IS_PRIMARY_COMM,
            isPreferredCommunication.toString());
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL,
            alertPrefEmail.toString());
          // communicationDetails.setAttribute(BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID,emailEvidenceID.toString());
          // add email to datastore
        }
      }
    }
    personEntity.addChildEntity(communicationDetailsPrimary);
    personEntity.addChildEntity(communicationDetailsAlternate);
    personEntity.update();
    return personEntity;
  }

  public Entity getCommunicationPrefEntity(final Entity personEntity,
    final Datastore datastore, final long concernRoleID)
    throws AppException, InformationalException {

    final Entity[] communicationPrefEntityList =
      personEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.COMMUNICATION_PREF));

    Entity communicationPref = null;
    // Entity communicationDetailsAlternate = null;
    boolean existingEntity = false;

    if (communicationPrefEntityList != null
      && communicationPrefEntityList.length > 0) {
      communicationPref = communicationPrefEntityList[0];
      existingEntity = true;
    } else {
      communicationPref = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.COMMUNICATION_PREF));
    }

    /**
     * Method to get the Communication Pref from the Evidence and set to entity
     *
     * @param personEntity
     * @param datastore
     * @param concernRoleID
     * @param type
     *
     *
     * @throws AppException
     * @throws InformationalException
     */
    final List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
      bdmContactPreferenceEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmContactPreferenceEvidenceVOList.isEmpty()) {
      for (final BDMContactPreferenceEvidenceVO bdmContactPreferenceEvidenceVO : bdmContactPreferenceEvidenceVOList) {
        // Get CommunicationPref from dyn evidence

        final String recieveCorrespondPref =
          bdmContactPreferenceEvidenceVO.getPreferredCommunication();
        final String preferredLanguage =
          bdmContactPreferenceEvidenceVO.getPreferredLanguage();
        final String languageS =
          bdmContactPreferenceEvidenceVO.getPreferredOralLanguage();
        final String languageW =
          bdmContactPreferenceEvidenceVO.getPreferredWrittenLanguage();
        final Long communicationPrefEvidenceID =
          bdmContactPreferenceEvidenceVO.getEvidenceID();
        // Set CommunicationPref into datastore
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.RECEIVE_CORRESPONDENCE,
          recieveCorrespondPref);
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE,
          languageS);
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE,
          languageW);

        personEntity.addChildEntity(communicationPref);
      }

    }
    personEntity.update();
    return personEntity;
  }

  /**
   * Method to get bank accounts from the Evidence and set to entity
   *
   * Only displays open bank accounts that have an end date in the future or
   * no end date. If two accounts are the same then display the one that has
   * the latest startdate, then latest effective date/time.
   *
   * @param personEntity
   * @param datastore
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */

  final public void getPaymentBanksEntity(final Entity personEntity,
    final Datastore datastore, final long concernRoleID)
    throws AppException, InformationalException {

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
        final Entity paymentBanksEntity = datastore.newEntity(datastore
          .getEntityType(BDMLifeEventDatastoreConstants.PAYMENT_BANKS));
        personEntity.addChildEntity(paymentBanksEntity);
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
          BDMLifeEventDatastoreConstants.BANK_SORT_CODE,
          bdmBankEvidenceVO.getSortCode());
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.BANK_ACCOUNT_NAME_AND_NUMBER,
          bankAccountName + " (" + accountNumber + ")");
        paymentBanksEntity.setAttribute(
          BDMLifeEventDatastoreConstants.PAYMENT_BANK_EVIDENCE_ID,
          bankEvidenceID.toString());
        paymentBanksEntity.update();
        personEntity.update();
      }
    }
  }

}
