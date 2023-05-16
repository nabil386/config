package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.DifferenceCommand;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.IEGYESNO;
import curam.common.util.xml.dom.Document;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.participantmanager.impl.ConcernRoleAddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.workspaceservices.codetable.impl.DataHubUpdateProcessorTypeEntry;
import curam.workspaceservices.util.impl.LogHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Class to implement Custom View Processor logic for LifeEvent Phone number
 * Updates
 * BDM Project
 *
 *
 */
public class BDMLifeEventPhoneNumberUpdateProcessor
  implements CustomUpdateProcessor {

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleAddressDAO concernRoleAddressDAO;

  @Inject
  LogHelper logHelper;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  BDMPhoneEvidence bdmPhoneEvidence;

  public BDMLifeEventPhoneNumberUpdateProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void processUpdate(
    final DataHubUpdateProcessorTypeEntry dataHubUpdateProcessorType,
    final String context, final Entity entity,
    final DifferenceCommand diffCommand) {

    // get updates
    // compare to see whats changed
    // Update if changes exist,
    // Skip otherwise
    final Document datastoreDifferenceXML = diffCommand.convertToXML();

    try {
      updatePhoneNumberInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }

  }

  /**
   * This method is to get phone number info from datastore and process phone
   * update
   *
   * @param citizenData
   * @param citizen
   * @throws AppException
   * @throws InformationalException
   */
  private void updatePhoneNumberInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];
    // Read communicationDetails entity from person entity
    final Entity[] communicationDetailsEntities =
      personEntity.getChildEntities(datastore
        .getEntityType(BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS));
    final Long concernRoleID =
      Long.parseLong(personEntity.getAttribute("localID"));

    /*
     * Get phone number info from datastore and process phone update
     * "phoneType" type="CT_PHONETYPE"
     * "countryCode" type="CT_COUNTRYCODE" default="BDMPC80035"
     * "phoneAreaCode" type="BDM_PHONE_AREA_CODE"
     * "phoneNumber" type="BDM_PHONE_NUMBER" default=""
     * "phoneExt" type="BDM_PHONE_EXT_CODE"
     * "isMor" type="IEG_BOOLEAN"
     * "isAftr" type="IEG_BOOLEAN"
     * "isEve" type="IEG_BOOLEAN"
     * "isPreferredCommunication" type="IEG_YES_NO
     * "receiveAlerts" type="IEG_YES_NO"
     * "communicationDateChange" type="IEG_DATE
     */
    boolean isReceivingAlert = false;
    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceVOList =
      new ArrayList<BDMPhoneEvidenceVO>();
    if (communicationDetailsEntities.length != 0) {
      for (final Entity communicationDetailsEntity : communicationDetailsEntities) {
        // process the phone number that user picked
        if (Long.toString(communicationDetailsEntity.getUniqueID())
          .equals(personEntity.getAttribute(
            BDMLifeEventDatastoreConstants.SELECTED_PHONE_NUMBER_ID))) {
          final BDMPhoneEvidenceVO bdmPhoneEvidenceVO =
            new BDMPhoneEvidenceVO();
          // set phoneNumberChangeType
          final String phoneNumberChangeType = personEntity.getAttribute(
            BDMLifeEventDatastoreConstants.PHONE_NUMBER_CHANGE_TYPE);
          bdmPhoneEvidenceVO.setPhoneNumberChangeType(phoneNumberChangeType);
          // set evidenceID
          final String phoneEvidenceID = communicationDetailsEntity
            .getAttribute(BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID);
          if (!StringUtil.isNullOrEmpty(phoneEvidenceID)) {
            bdmPhoneEvidenceVO.setEvidenceID(Long.parseLong(phoneEvidenceID));
          }
          // set other evidence attributes
          assignPhoneEvidence(communicationDetailsEntity, bdmPhoneEvidenceVO);
          bdmPhoneEvidenceVOList.add(bdmPhoneEvidenceVO);
          isReceivingAlert = communicationDetailsEntity
            .getAttribute(BDMLifeEventDatastoreConstants.RECEIVE_ALERT)
            .equals(IEGYESNO.YES) ? true : false;
        }
      }
      if (isReceivingAlert) {
        // remove the alert indicator on other phone numbers
        for (final Entity communicationDetailsEntity : communicationDetailsEntities) {
          if (!Long.toString(communicationDetailsEntity.getUniqueID())
            .equals(personEntity.getAttribute(
              BDMLifeEventDatastoreConstants.SELECTED_PHONE_NUMBER_ID))
            && communicationDetailsEntity
              .getAttribute(BDMLifeEventDatastoreConstants.RECEIVE_ALERT)
              .equals(IEGYESNO.YES)) {
            final BDMPhoneEvidenceVO bdmPhoneEvidenceVO =
              new BDMPhoneEvidenceVO();
            // set phoneNumberChangeType
            bdmPhoneEvidenceVO
              .setPhoneNumberChangeType(BDMPHONENUMBERCHANGETYPE.UPDATE);
            // set evidenceID
            final String phoneEvidenceID = communicationDetailsEntity
              .getAttribute(BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID);
            bdmPhoneEvidenceVO.setEvidenceID(Long.parseLong(phoneEvidenceID));
            // set other evidence attributes
            assignPhoneEvidence(communicationDetailsEntity,
              bdmPhoneEvidenceVO);
            // clear the alert indicator
            bdmPhoneEvidenceVO.setUseForAlertsInd(false);
            bdmPhoneEvidenceVO.setAlertFrequency("");
            bdmPhoneEvidenceVO.setFromDate(Date.getCurrentDate());
            // add this object to the top of the list, so that the alert
            // indicator is cleared first
            bdmPhoneEvidenceVOList.add(0, bdmPhoneEvidenceVO);
          }
        }
      }
    }
    bdmPhoneEvidence.createPhoneEvidence(concernRoleID,
      bdmPhoneEvidenceVOList,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);

  }

  private void assignPhoneEvidence(final Entity communicationDetailsEntity,
    final BDMPhoneEvidenceVO bdmPhoneEvidenceVO) {

    bdmPhoneEvidenceVO.setPhoneCountryCode(communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.COUNTRY_CODE));
    bdmPhoneEvidenceVO.setPhoneType(communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PHONE_TYPE));
    bdmPhoneEvidenceVO.setPhoneAreaCode(communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PHONE_AREA_CODE));
    bdmPhoneEvidenceVO.setPhoneNumber(communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PHONE_NUMBER));
    bdmPhoneEvidenceVO.setPhoneExtension(communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PHONE_EXT));
    final String isMor = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PHONE_IS_MOR);
    bdmPhoneEvidenceVO.setMorningInd(Boolean.parseBoolean(isMor));
    final String isAftnoon = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PHONE_IS_AFTNOON);
    bdmPhoneEvidenceVO.setAfternoonInd(Boolean.parseBoolean(isAftnoon));
    final String isEve = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PHONE_IS_EVE);
    bdmPhoneEvidenceVO.setEveningInd(Boolean.parseBoolean(isEve));
    final String isPreferredCommunication =
      communicationDetailsEntity.getAttribute(
        BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION);
    if (isPreferredCommunication.equals(IEGYESNO.YES)) {
      bdmPhoneEvidenceVO.setPreferredInd(true);
    }
    final String receiveAlertInd = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.RECEIVE_ALERT);
    if (receiveAlertInd.equals(IEGYESNO.YES)) {
      bdmPhoneEvidenceVO.setUseForAlertsInd(true);
    }
    bdmPhoneEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmPhoneEvidenceVO.setAlertFrequency(communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.ALERT_FREQUENCY));

  }

}
