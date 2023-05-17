package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMAttestation;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncarcerationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncarcerationEvidenceVO;
import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.DifferenceCommand;
import curam.codetable.EVIDENCECHANGEREASON;
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
public class BDMLifeEventIncarcerationUpdateProcessor
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
  BDMAttestation bdmAttestation;

  @Inject
  BDMIncarcerationEvidence bdmIncarcerationEvidence;

  protected BDMLifeEventIncarcerationUpdateProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   *
   * @param dataHubUpdateProcessorType
   * @param context
   * @param entity
   * @throws AppException
   * @throws InformationalException
   */
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
      updateIncarcerationInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }

  }

  /**
   * This method is to read info from datastore and process incarecration update
   *
   *
   * @param rootEntity
   * @param datastoreDifferenceXML
   * @throws AppException
   * @throws InformationalException
   */
  private void updateIncarcerationInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];
    // Read incarceration entity from person entity
    final Entity[] incarcerationEntities = personEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.INCARCERATION));
    final Long concernRoleID =
      Long.parseLong(personEntity.getAttribute("localID"));

    /*
     * Get incarceration info from DB and populate into datastore
     * "incarQuestion" type="IEG_YES_NO"
     * "instName" type="IEG_STRING"
     * "startDateInca" type="IEG_DATE"
     * "endDateInca" type="IEG_DATE"
     *
     * "localID" type="IEG_STRING" default=""
     * "curamDataStoreUniqueID" type="IEG_STRING" default=""
     * "incarcerationEvidenceID" type="IEG_STRING" default=""
     * "incarcerationPeriodDesc" type="IEG_STRING" default=""
     * "incarcerationSelected" type="IEG_BOOLEAN" default="false"
     */
    final List<BDMIncarcerationEvidenceVO> bdmIncarcerationEvidenceVOList =
      new ArrayList<BDMIncarcerationEvidenceVO>();
    if (incarcerationEntities.length != 0) {
      for (final Entity incarcerationEntity : incarcerationEntities) {

        if (Long.toString(incarcerationEntity.getUniqueID())
          .equals(personEntity.getAttribute(
            BDMLifeEventDatastoreConstants.SELECTED_INCARCERATION_ID))) {
          final BDMIncarcerationEvidenceVO bdmIncarcerationEvidenceVO =
            new BDMIncarcerationEvidenceVO();
          final String incarcerationChangeType = personEntity.getAttribute(
            BDMLifeEventDatastoreConstants.INCARCERATION_CHANGE_TYPE);
          bdmIncarcerationEvidenceVO
            .setIncarcerationChangeType(incarcerationChangeType);
          bdmIncarcerationEvidenceVO.setInstitutionName(incarcerationEntity
            .getAttribute(BDMLifeEventDatastoreConstants.INCARCERATION_Name));
          bdmIncarcerationEvidenceVO
            .setStartDate(Date.fromISO8601(incarcerationEntity.getAttribute(
              BDMLifeEventDatastoreConstants.INCARCERATION_START_DATE)));
          final String endDate = incarcerationEntity.getAttribute(
            BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE);
          if (!endDate.isEmpty()) {
            bdmIncarcerationEvidenceVO
              .setEndDate(Date.fromISO8601(incarcerationEntity.getAttribute(
                BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE)));
          } else {
            bdmIncarcerationEvidenceVO.setEndDate(Date.kZeroDate);
          }
          // BEGIN - User Story 21834 - Added Attestation
          bdmIncarcerationEvidenceVO
            .setAttestationDate(Date.getCurrentDate());
          bdmIncarcerationEvidenceVO
            .setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);
          bdmIncarcerationEvidenceVO.setComments(BDMConstants.EMPTY_STRING);
          // END - User Story 21834 - Added Attestation
          final String incarcerationEvidenceID =
            incarcerationEntity.getAttribute(
              BDMLifeEventDatastoreConstants.INCARCERATION_EVIDENCE_ID);

          if (!StringUtil.isNullOrEmpty(incarcerationEvidenceID)) {
            bdmIncarcerationEvidenceVO
              .setEvidenceID(Long.parseLong(incarcerationEvidenceID));
          }
          bdmIncarcerationEvidenceVOList.add(bdmIncarcerationEvidenceVO);
        }

      }
      bdmIncarcerationEvidence.createIncarcerationEvidence(concernRoleID,
        bdmIncarcerationEvidenceVOList,
        EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    }

  }

}
