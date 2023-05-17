package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMEMAILADDRESSCHANGETYPE;
import curam.ca.gc.bdm.lifeevent.impl.BDMAttestation;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
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

public class BDMLifeEventEmailUpdateProcessor
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
  BDMEmailEvidence bdmEmailEvidence;

  @Inject
  BDMAttestation bdmAttestation;

  protected BDMLifeEventEmailUpdateProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void processUpdate(
    final DataHubUpdateProcessorTypeEntry dataHubUpdateProcessorType,
    final String context, final Entity entity,
    final DifferenceCommand diffCommand) {

    final Document datastoreDifferenceXML = diffCommand.convertToXML();

    try {
      updateEmailInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }

  }

  /**
   *
   * @param citizenData
   * @param citizen
   * @throws AppException
   * @throws InformationalException
   */
  private void updateEmailInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];
    final Entity[] communicationDetailsEntities =
      personEntity.getChildEntities(datastore
        .getEntityType(BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS));

    final Long concernRoleID =
      Long.parseLong(personEntity.getAttribute("localID"));

    // Get email info and evidenceID
    // from datastore
    boolean isReceivingAlert = false;
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      new ArrayList<BDMEmailEvidenceVO>();
    if (communicationDetailsEntities.length != 0) {
      for (final Entity communicationDetailsEntity : communicationDetailsEntities) {
        // Process the email address that user picked
        if (Long.toString(communicationDetailsEntity.getUniqueID())
          .equals(personEntity.getAttribute(
            BDMLifeEventDatastoreConstants.SELECTED_EMAIL_ADDRESS_ID))) {
          // Process an email update
          final BDMEmailEvidenceVO bdmEmailEvidenceVO =
            new BDMEmailEvidenceVO();
          // set emailAddressChangeType
          final String emailAddressChangeType = personEntity.getAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_ADDRESS_CHANGE_TYPE);
          bdmEmailEvidenceVO
            .setEmailAddressChangeType(emailAddressChangeType);
          final String emailEvidenceID = communicationDetailsEntity
            .getAttribute(BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID);
          if (!StringUtil.isNullOrEmpty(emailEvidenceID)) {
            bdmEmailEvidenceVO.setEvidenceID(Long.parseLong(emailEvidenceID));
          }
          // set other evidence attributes
          assignEmailEvidence(communicationDetailsEntity, bdmEmailEvidenceVO);
          bdmEmailEvidenceVOList.add(bdmEmailEvidenceVO);
          isReceivingAlert = communicationDetailsEntity
            .getAttribute(BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL)
            .equals(IEGYESNO.YES) ? true : false;
        }
      }
      if (isReceivingAlert) {
        // remove the alert indicator on other email addresses
        for (final Entity communicationDetailsEntity : communicationDetailsEntities) {
          if (!Long.toString(communicationDetailsEntity.getUniqueID())
            .equals(personEntity.getAttribute(
              BDMLifeEventDatastoreConstants.SELECTED_EMAIL_ADDRESS_ID))
            && communicationDetailsEntity
              .getAttribute(BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL)
              .equals(IEGYESNO.YES)) {
            final BDMEmailEvidenceVO bdmEmailEvidenceVO =
              new BDMEmailEvidenceVO();
            // set emailAddressChangeType
            bdmEmailEvidenceVO
              .setEmailAddressChangeType(BDMEMAILADDRESSCHANGETYPE.UPDATE);
            // set evidenceID
            final String emailEvidenceID = communicationDetailsEntity
              .getAttribute(BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID);
            bdmEmailEvidenceVO.setEvidenceID(Long.parseLong(emailEvidenceID));
            // set other evidence attributes
            // set other evidence attributes
            assignEmailEvidence(communicationDetailsEntity,
              bdmEmailEvidenceVO);
            // clear the alert indicator
            bdmEmailEvidenceVO.setUseForAlertsInd(false);
            bdmEmailEvidenceVO.setAlertFrequency("");
            bdmEmailEvidenceVO.setFromDate(Date.getCurrentDate());
            // add this object to the top of the list, so that the alert
            // indicator is cleared first
            bdmEmailEvidenceVOList.add(0, bdmEmailEvidenceVO);
          }
        }
      }
      bdmEmailEvidence.createEmailEvidence(concernRoleID,
        bdmEmailEvidenceVOList,
        EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    }

  }

  private void assignEmailEvidence(final Entity communicationDetailsEntity,
    final BDMEmailEvidenceVO bdmEmailEvidenceVO) {

    final String emailAdr = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.EMAIL_ADDRESS);
    final String emailType = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.EMAIL_TYPE);
    final String isPreferredCommunication =
      communicationDetailsEntity.getAttribute(
        BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION);
    final String requestedAlerts = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL);

    final String alertFreq = communicationDetailsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.ALERT_FREQUENCY);

    bdmEmailEvidenceVO.setEmailAddress(emailAdr);
    bdmEmailEvidenceVO.setEmailAddressType(emailType);
    bdmEmailEvidenceVO.setFromDate(Date.getCurrentDate());
    if (isPreferredCommunication.equals(IEGYESNO.YES)) {
      bdmEmailEvidenceVO.setPreferredInd(true);
    } else {
      bdmEmailEvidenceVO.setPreferredInd(false);
    }
    if (requestedAlerts.equals(IEGYESNO.YES)) {
      bdmEmailEvidenceVO.setUseForAlertsInd(true);
    } else {
      bdmEmailEvidenceVO.setUseForAlertsInd(false);
    }
    if (!StringUtil.isNullOrEmpty(alertFreq)) {
      bdmEmailEvidenceVO.setAlertFrequency(alertFreq);
    }
  }

}
