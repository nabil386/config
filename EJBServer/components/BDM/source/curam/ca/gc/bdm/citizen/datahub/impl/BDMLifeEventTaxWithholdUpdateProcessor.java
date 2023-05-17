package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDMETHOD;
import curam.ca.gc.bdm.codetable.BDMTAXCHANGETYPE;
import curam.ca.gc.bdm.codetable.impl.BDMVTWTYPEEntry;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidenceVO;
import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.DifferenceCommand;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.common.util.xml.dom.Document;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.facade.struct.ReadPersonKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.workspaceservices.codetable.impl.DataHubUpdateProcessorTypeEntry;

public class BDMLifeEventTaxWithholdUpdateProcessor
  implements CustomUpdateProcessor {

  @Inject
  BDMVoluntaryTaxWithholdEvidence bdmVoluntaryTaxWithholdEvidence;

  protected BDMLifeEventTaxWithholdUpdateProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void processUpdate(
    final DataHubUpdateProcessorTypeEntry dataHubUpdateProcessorType,
    final String context, final Entity entity,
    final DifferenceCommand diffCommand) {

    final Document datastoreDifferenceXML = diffCommand.convertToXML();

    try {
      updateTaxInfoInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }
  }

  /**
   * processes the entity and applies the appropriate update to the Tax Withhold
   * evidence
   *
   * @param rootEntity
   * @param datastoreDifferenceXML
   * @throws AppException
   * @throws InformationalException
   * @throws NoSuchSchemaException
   */
  private void updateTaxInfoInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];
    final Long concernRoleID = Long.parseLong(
      personEntity.getAttribute(BDMLifeEventDatastoreConstants.LOCAL_ID));
    final ReadPersonKey readPersonKey = new ReadPersonKey();
    readPersonKey.dtls.concernRoleID = concernRoleID;

    // get taxInfos
    final Entity[] taxInfoEntities = personEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.TAX_INFO));
    if (taxInfoEntities != null) {
      for (final Entity taxInfoEntityIn : taxInfoEntities) {
        // taxInfo has no evidenceID, then add it as new evidence as the only
        // case where this is true is when a new Tax Info is being created and
        // no others exist
        if (StringUtil.isNullOrEmpty(taxInfoEntityIn.getAttribute(
          BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_EVIDENCE_ID))) {
          // Process taxInfo update
          final BDMVoluntaryTaxWithholdEvidenceVO bdmVoluntaryTaxWithholdEvidenceVO =
            setEvidenceVO(taxInfoEntityIn);
          bdmVoluntaryTaxWithholdEvidence
            .createOrUpdateVoluntaryTaxWithholdEvidence(concernRoleID,
              bdmVoluntaryTaxWithholdEvidenceVO,
              EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
          return;
        }
        // check all current Tax Infos to see which was selected, then process
        // the appropriate update whether its remove or modify
        if (taxInfoEntityIn
          .getAttribute(
            BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_SELECTED)
          .equals("true")) {
          // Get the evidence VO to be processed for update or removal
          BDMVoluntaryTaxWithholdEvidenceVO bdmVoluntaryTaxWithholdEvidenceVO =
            setEvidenceVO(taxInfoEntityIn);
          final long evidenceID = Long.parseLong(taxInfoEntityIn.getAttribute(
            BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_EVIDENCE_ID));
          // If remove End date current evidence beginning today
          if (personEntity
            .getAttribute(
              BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_CHANGE_TYPE)
            .equals(BDMTAXCHANGETYPE.REMOVE)) {
            // If the startDate is in future, remove the evidence
            // if the start date is the current date or in the past, end date
            // evidence with current date.
            final Date evdStartDate =
              Date.fromISO8601(taxInfoEntityIn.getAttribute(
                BDMLifeEventDatastoreConstants.TAX_ORIGINAL_START_DATE));

            // If the startDate is in future
            if (Date.getCurrentDate().before(evdStartDate)) {

              BDMEvidenceUtil.removeDynamicEvidence(evidenceID,
                CASEEVIDENCE.BDMVTW);
            } else {
              final BDMVoluntaryTaxWithholdEvidenceVO bdmVTWEvidenceVOupdate =
                new BDMVoluntaryTaxWithholdEvidenceVO();
              bdmVTWEvidenceVOupdate
                .setVoluntaryTaxWithholdType(bdmVoluntaryTaxWithholdEvidenceVO
                  .getVoluntaryTaxWithholdType());
              bdmVTWEvidenceVOupdate.setEvidenceID(evidenceID);
              bdmVTWEvidenceVOupdate.setEndDate(Date.getCurrentDate());
              bdmVoluntaryTaxWithholdEvidence
                .createOrUpdateVoluntaryTaxWithholdEvidence(concernRoleID,
                  bdmVTWEvidenceVOupdate,
                  EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
            }
          }
          // Update evidence using change effective date
          else if (personEntity
            .getAttribute(
              BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_CHANGE_TYPE)
            .equals(BDMTAXCHANGETYPE.MODIFY)) {
            final BDMVoluntaryTaxWithholdEvidenceVO bdmVoluntaryTaxWithholdEvidenceVOupdate =
              new BDMVoluntaryTaxWithholdEvidenceVO();
            bdmVoluntaryTaxWithholdEvidenceVO =
              setEvidenceVO(taxInfoEntityIn);
            final Date originalStartDate =
              Date.fromISO8601(taxInfoEntityIn.getAttribute(
                BDMLifeEventDatastoreConstants.TAX_ORIGINAL_START_DATE));
            final Date effectiveDate =
              Date.fromISO8601(taxInfoEntityIn.getAttribute(
                BDMLifeEventDatastoreConstants.TAX_CHANGE_EFFECTIVE_DATE));
            final String endDateStr = taxInfoEntityIn
              .getAttribute(BDMLifeEventDatastoreConstants.TAX_END_DATE);
            final Date endDate = endDateStr.isEmpty() ? Date.kZeroDate
              : Date.fromISO8601(endDateStr);
            // If the startDate is identical, then only update the current
            // evidence
            if (originalStartDate.equals(effectiveDate)) {
              bdmVoluntaryTaxWithholdEvidenceVOupdate
                .setVoluntaryTaxWithholdType(bdmVoluntaryTaxWithholdEvidenceVO
                  .getVoluntaryTaxWithholdType());
              bdmVoluntaryTaxWithholdEvidenceVOupdate
                .setAmount(bdmVoluntaryTaxWithholdEvidenceVO.getAmount());
              bdmVoluntaryTaxWithholdEvidenceVOupdate.setPercentageValue(
                bdmVoluntaryTaxWithholdEvidenceVO.getPercentageValue());
              bdmVoluntaryTaxWithholdEvidenceVOupdate
                .setEvidenceID(evidenceID);
              if (!endDate.equals(Date.kZeroDate)) {
                bdmVoluntaryTaxWithholdEvidenceVOupdate.setEndDate(endDate);
              }
              bdmVoluntaryTaxWithholdEvidence
                .createOrUpdateVoluntaryTaxWithholdEvidence(concernRoleID,
                  bdmVoluntaryTaxWithholdEvidenceVOupdate,
                  EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
            } else {
              // end date current vtw starting the day before change effective
              // date
              bdmVoluntaryTaxWithholdEvidenceVOupdate
                .setVoluntaryTaxWithholdType(bdmVoluntaryTaxWithholdEvidenceVO
                  .getVoluntaryTaxWithholdType());
              bdmVoluntaryTaxWithholdEvidenceVOupdate
                .setEvidenceID(evidenceID);
              bdmVoluntaryTaxWithholdEvidenceVOupdate
                .setEndDate(effectiveDate.addDays(-1));
              bdmVoluntaryTaxWithholdEvidenceVOupdate
                .setEvidenceID(evidenceID);
              bdmVoluntaryTaxWithholdEvidence
                .createOrUpdateVoluntaryTaxWithholdEvidence(concernRoleID,
                  bdmVoluntaryTaxWithholdEvidenceVOupdate,
                  EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
              // create new tax withhold beginning the new effective date
              bdmVoluntaryTaxWithholdEvidenceVO =
                setEvidenceVO(taxInfoEntityIn);
              bdmVoluntaryTaxWithholdEvidenceVO.setEvidenceID(0L);
              bdmVoluntaryTaxWithholdEvidenceVO.setStartDate(effectiveDate);
              if (!endDate.equals(Date.kZeroDate)) {
                bdmVoluntaryTaxWithholdEvidenceVO.setEndDate(endDate);
              }
              bdmVoluntaryTaxWithholdEvidence
                .createOrUpdateVoluntaryTaxWithholdEvidence(concernRoleID,
                  bdmVoluntaryTaxWithholdEvidenceVO,
                  EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
            }
          }
        }
      }
    }
  }

  /**
   * Sets the passed entity and effectiveDate information into a
   * BDMVoluntaryTaxWithholdEvidenceVO
   *
   * @param taxInfoEntityIn
   * @param effectiveDate
   * @return BDMVoluntaryTaxWithholdEvidenceVO
   * @throws AppException
   * @throws InformationalException
   * @throws NoSuchSchemaException
   */
  private BDMVoluntaryTaxWithholdEvidenceVO
    setEvidenceVO(final Entity taxInfoEntityIn) {

    final BDMVoluntaryTaxWithholdEvidenceVO bdmVoluntaryTaxWithholdEvidenceVO =
      new BDMVoluntaryTaxWithholdEvidenceVO();
    final String startDate = taxInfoEntityIn
      .getAttribute(BDMLifeEventDatastoreConstants.TAX_START_DATE);
    if (!startDate.isEmpty()) {
      bdmVoluntaryTaxWithholdEvidenceVO
        .setStartDate(Date.fromISO8601(startDate));
    }
    final String effectiveDate = taxInfoEntityIn
      .getAttribute(BDMLifeEventDatastoreConstants.TAX_CHANGE_EFFECTIVE_DATE);
    if (!effectiveDate.isEmpty()) {
      bdmVoluntaryTaxWithholdEvidenceVO
        .setStartDate(Date.fromISO8601(effectiveDate));
    }
    final String endDate = taxInfoEntityIn
      .getAttribute(BDMLifeEventDatastoreConstants.TAX_END_DATE);
    if (!endDate.isEmpty()) {
      bdmVoluntaryTaxWithholdEvidenceVO.setEndDate(Date.fromISO8601(endDate));
    }
    bdmVoluntaryTaxWithholdEvidenceVO
      .setVoluntaryTaxWithholdType(BDMVTWTYPEEntry.FEDERAL.getCode());
    final String taxDedMethod = taxInfoEntityIn
      .getAttribute(BDMLifeEventDatastoreConstants.TAX_DED_METHOD);
    if (taxDedMethod.equals(BDMDEDMETHOD.DOLLAR)) {
      bdmVoluntaryTaxWithholdEvidenceVO.setAmount(taxInfoEntityIn
        .getAttribute(BDMLifeEventDatastoreConstants.TAX_DOLLAR_AMOUNT));
    } else if (taxDedMethod.equals(BDMDEDMETHOD.PERCT)) {
      final Integer percentInt = Integer.parseInt(taxInfoEntityIn
        .getAttribute(BDMLifeEventDatastoreConstants.TAX_DOLLAR_PERCENT));
      bdmVoluntaryTaxWithholdEvidenceVO
        .setPercentageValue(percentInt.toString());
    }
    final String evidenceID = taxInfoEntityIn.getAttribute(
      BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_EVIDENCE_ID);
    if (!evidenceID.isEmpty()) {
      bdmVoluntaryTaxWithholdEvidenceVO
        .setEvidenceID(Long.parseLong(evidenceID));
    } else {
      bdmVoluntaryTaxWithholdEvidenceVO.setEvidenceID(0L);
    }
    return bdmVoluntaryTaxWithholdEvidenceVO;
  }

}
