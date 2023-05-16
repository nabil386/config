package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMAttestation;
// import curam.ca.gc.bdm.codetable.BDMATTESTATIONTYPE;
// import curam.ca.gc.bdm.lifeevent.impl.BDMAttestation;
// import curam.ca.gc.bdm.lifeevent.impl.BDMAttestationVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncomeEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncomeEvidenceVO;
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
 * Class to implement Custom View Processor logic for LifeEvent Income
 * Updates
 * BDM Project
 *
 *
 */
public class BDMLifeEventIncomeUpdateProcessor
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
  BDMIncomeEvidence bdmIncomeEvidence;

  @Inject
  BDMAttestation bdmAttestation;

  protected BDMLifeEventIncomeUpdateProcessor() {

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
      updateIncomeInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is to take info from datastore and process income update
   *
   *
   * @param rootEntity
   * @param datastoreDifferenceXML
   * @throws AppException
   * @throws InformationalException
   */
  private void updateIncomeInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];
    // Read communicationDetails entity from person entity
    final Entity[] incomeEntities = personEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.INCOME));
    final Long concernRoleID =
      Long.parseLong(personEntity.getAttribute("localID"));
    final String previousYear = getPreviousYear();
    final List<BDMIncomeEvidenceVO> bdmIncomeEvidenceVOList =
      new ArrayList<BDMIncomeEvidenceVO>();
    if (incomeEntities.length != 0) {
      for (final Entity incomeEntity : incomeEntities) {
        // If there is a date of change then process an phone number update
        final BDMIncomeEvidenceVO bdmIncomeEvidenceVO =
          new BDMIncomeEvidenceVO();
        String incomeYear = incomeEntity
          .getAttribute(BDMLifeEventDatastoreConstants.INCOME_YEAR);
        if (incomeYear.isEmpty()) {
          incomeYear = previousYear;
        }
        bdmIncomeEvidenceVO.setIncomeYear(incomeYear);
        String incomeAmount = incomeEntity
          .getAttribute(BDMLifeEventDatastoreConstants.INCOME_AMOUNT);
        incomeAmount = incomeAmount.replace(",", "");
        final String incomeEvidenceID = incomeEntity
          .getAttribute(BDMLifeEventDatastoreConstants.INCOME_EVIDENCE_ID);
        bdmIncomeEvidenceVO.setIncomeAmount(incomeAmount);
        // BEGIN - User Story 21834 - Added Attestation
        bdmIncomeEvidenceVO.setAttestationDate(Date.getCurrentDate());
        bdmIncomeEvidenceVO.setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);
        bdmIncomeEvidenceVO.setComments(BDMConstants.EMPTY_STRING);
        // END - User Story 21834 - Added Attestation
        if (!StringUtil.isNullOrEmpty(incomeEvidenceID)) {
          bdmIncomeEvidenceVO.setEvidenceID(Long.parseLong(incomeEvidenceID));
        }
        bdmIncomeEvidenceVOList.add(bdmIncomeEvidenceVO);
      }
      bdmIncomeEvidence.createIncomeEvidence(concernRoleID,
        bdmIncomeEvidenceVOList,
        EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    }

  }

  /**
   * Util method to get previous year
   */
  private String getPreviousYear() {

    final Date today = curam.util.type.Date.getCurrentDate();
    final String date = today.toString();
    final String[] split = date.split("/");
    String year = new String();
    for (final String piece : split) {
      if (piece.length() == 4) {
        year = piece;
        Integer yearInt = Integer.parseInt(year);
        yearInt = yearInt - 1;
        year = yearInt.toString();
      }
    }
    return year;
  }
}
