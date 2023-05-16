package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.DifferenceCommand;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.common.util.xml.dom.Document;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.workspaceservices.codetable.impl.DataHubUpdateProcessorTypeEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vipresh.sharma
 * @since 9010 Life Events Update communication preference
 */
public class BDMLifeEventCommunicationAlertUpdateProcessor
  implements CustomUpdateProcessor {

  @Inject
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  protected BDMLifeEventCommunicationAlertUpdateProcessor() {

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

    final Entity communicationPrefsEntity =
      personEntity.getChildEntities(datastore
        .getEntityType(BDMLifeEventDatastoreConstants.PREFERRED_LANGUAGE))[0];

    final Long concernRoleID =
      Long.parseLong(personEntity.getAttribute("localID"));

    // Get communication and alert and evidenceID from datastore
    final List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
      new ArrayList<BDMContactPreferenceEvidenceVO>();
    // Set CommunicationPref from datastore
    final String recieveCorrespondPref = communicationPrefsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.RECEIVE_CORRESPONDENCE);
    final String languageS = communicationPrefsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE);
    final String languageW = communicationPrefsEntity.getAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE);
    // Set from datastore
    final String communicationEvidenceID = communicationPrefsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.COMM_PREF_EVIDENCE_ID);
    // Process communication and alerts update
    final BDMContactPreferenceEvidenceVO bdmContactPreferenceEvidenceVO =
      new BDMContactPreferenceEvidenceVO();
    bdmContactPreferenceEvidenceVO
      .setPreferredCommunication(recieveCorrespondPref);
    bdmContactPreferenceEvidenceVO.setPreferredOralLanguage(languageS);
    bdmContactPreferenceEvidenceVO.setPreferredWrittenLanguage(languageW);
    final long caseParticipantID = Long.parseLong(communicationPrefsEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PARTICIPANT));
    bdmContactPreferenceEvidenceVO.setParticipant(caseParticipantID);
    if (!StringUtil.isNullOrEmpty(communicationEvidenceID)) {
      bdmContactPreferenceEvidenceVO
        .setEvidenceID(Long.parseLong(communicationEvidenceID));
    }
    bdmContactPreferenceEvidenceVOList.add(bdmContactPreferenceEvidenceVO);
    bdmContactPreferenceEvidence.createContactPreference(concernRoleID,
      bdmContactPreferenceEvidenceVO,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
  }

}
