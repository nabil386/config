package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.integritycheck.fact.BDMIntegrityCheckFactory;
import curam.ca.gc.bdm.facade.integritycheck.struct.BDMIntegrityCheckKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckUtil;
import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.DifferenceCommand;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.PersonDAO;
import curam.pdc.impl.PDCConst;
import curam.piwrapper.participantmanager.impl.ConcernRoleAddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.workspaceservices.codetable.impl.DataHubUpdateProcessorTypeEntry;
import curam.workspaceservices.util.impl.LogHelper;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Class to implement Custom View Processor logic for LifeEvent Personal Updates
 * BDM Project
 *
 *
 */
public class BDMLifeEventPersonalUpdateProcessor
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
  BDMIdentificationEvidence bdmIdentificationEvidence;

  @Inject
  BDMNamesEvidence bdmNamesEvidence;

  @Inject
  BDMDateofBirthEvidence bdmDateofBirthEvidence;

  protected BDMLifeEventPersonalUpdateProcessor() {

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

    final Document datastoreDifferenceXML = diffCommand.convertToXML();

    try {
      updatePersonalInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }
  }

  /**
   * method to update personal information
   *
   * @param rootEntity
   * @param datastoreDifferenceXML
   * @throws AppException
   * @throws InformationalException
   */
  private void updatePersonalInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];

    final Long concernRoleID =
      Long.parseLong(personEntity.getAttribute("localID"));

    // Get last name at birth, parents last name at birth, DOB and evidenceID
    // from datastore
    final String lastNameAtBirth = personEntity
      .getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH);
    final String parentsLastNameAtBirth = personEntity
      .getAttribute(BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH);
    final String dateOfBirth =
      personEntity.getAttribute(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH);
    final String dobEvidenceID = personEntity
      .getAttribute(BDMLifeEventDatastoreConstants.DOB_EVIDENCE_ID);
    // Check if information has changed
    final BDMLifeEventCustomProcessorUtil bdmLifeEventCustomProcessorUtil =
      new BDMLifeEventCustomProcessorUtil();
    Element personDOBAttributes = new Element("Person");
    personDOBAttributes = bdmLifeEventCustomProcessorUtil
      .getDOBValueObjectForConcernRoleID(concernRoleID, personDOBAttributes);
    Boolean changeFlag = false;
    if (!personDOBAttributes
      .getAttributeValue(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH)
      .toString().equals(lastNameAtBirth)) {
      changeFlag = true;
    } else if (!personDOBAttributes
      .getAttributeValue(
        BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH)
      .toString().equals(parentsLastNameAtBirth)) {
      changeFlag = true;
    } else if (!personDOBAttributes
      .getAttributeValue(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH)
      .toString().equals(dateOfBirth)) {
      changeFlag = true;
    }
    // Process a DOB update if changeFlag is true
    if (changeFlag) {
      final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO =
        new BDMDateofBirthEvidenceVO();
      bdmDateofBirthEvidenceVO.setBirthLastName(lastNameAtBirth);
      bdmDateofBirthEvidenceVO
        .setMothersBirthLastName(parentsLastNameAtBirth);
      bdmDateofBirthEvidenceVO.setDateOfBirth(Date.fromISO8601(dateOfBirth));
      // BEGIN - User Story 21834 - Added Attestation
      bdmDateofBirthEvidenceVO.setAttestationDate(Date.getCurrentDate());
      bdmDateofBirthEvidenceVO
        .setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);
      bdmDateofBirthEvidenceVO.setComments(BDMConstants.EMPTY_STRING);
      // END - User Story 21834 - Added Attestation
      if (!StringUtil.isNullOrEmpty(dobEvidenceID)) {
        bdmDateofBirthEvidenceVO.setEvidenceID(Long.parseLong(dobEvidenceID));
        // BEGIN - User Story 21834 - Added Attestation
        final List<BDMDateofBirthEvidenceVO> bdmDateofBirthEvidenceVOList =
          bdmDateofBirthEvidence.getDOBEvidenceValueObject(concernRoleID);
        if (!bdmDateofBirthEvidenceVOList.isEmpty()) {
          for (final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVOIn : bdmDateofBirthEvidenceVOList) {
            if (bdmDateofBirthEvidenceVOIn.getEvidenceID() == Long
              .parseLong(dobEvidenceID)) {
              bdmDateofBirthEvidenceVO.setAttesteeCaseParticipant(
                bdmDateofBirthEvidenceVOIn.getAttesteeCaseParticipant());
              bdmDateofBirthEvidenceVO
                .setPerson(bdmDateofBirthEvidenceVOIn.getPerson());
              break;
            }
          }
        }
        // END - User Story 21834 - Added Attestation
      }
      bdmDateofBirthEvidence.createDateOfBirthEvidence(concernRoleID,
        bdmDateofBirthEvidenceVO,
        EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    }

    // Get SIN, evidenceID, and sinRawDate from datastore
    final String sinRawDate =
      personEntity.getAttribute(BDMLifeEventDatastoreConstants.SIN_RAW_DATE);
    // If there is a date of change then process a SIN update
    if (!StringUtil.isNullOrEmpty(sinRawDate)) {

      // the Identification evidence validation prevents user from updating the
      // SIN evidence. The facade scoped object is set to allow SIN updates
      // this value is checked in the evidence validation
      TransactionInfo.setFacadeScopeObject(
        Configuration.getProperty(EnvVars.ENV_CANEDIT_SSN), false);

      final String sinDatastore = personEntity
        .getAttribute(BDMLifeEventDatastoreConstants.SOCIAL_INSURANCE_NUMBER);
      final String sinEvidenceID = personEntity
        .getAttribute(BDMLifeEventDatastoreConstants.SIN_EVIDENCE_ID);
      final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVOCurrent =
        bdmIdentificationEvidence.getSINEvidenceValueObject(concernRoleID);
      // If current SIN matches Script SIN then do not update
      if (bdmIdentificationEvidenceVOCurrent.getAlternateID() == null
        || !bdmIdentificationEvidenceVOCurrent.getAlternateID()
          .equals(sinDatastore)) {
        final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVOUpdate =
          new BDMIdentificationEvidenceVO();
        bdmIdentificationEvidenceVOUpdate.setAlternateID(sinDatastore);
        bdmIdentificationEvidenceVOUpdate
          .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
        bdmIdentificationEvidenceVOUpdate
          .setFromDate(Date.fromISO8601(sinRawDate));
        bdmIdentificationEvidenceVOUpdate.setPreferredInd(true);
        if (!StringUtil.isNullOrEmpty(sinEvidenceID)) {
          bdmIdentificationEvidenceVOUpdate
            .setEvidenceID(Long.parseLong(sinEvidenceID));
        } else {
          bdmIdentificationEvidenceVOUpdate.setEvidenceID(0);
        }

        // Remove SIN SIR Response evidence if it exists.
        new BDMSINIntegrityCheckUtil()
          .removeExistingSINSIRResponceEvidence(concernRoleID);

        final Date endDate =
          bdmIdentificationEvidenceVOUpdate.getFromDate().addDays(-1);

        bdmIdentificationEvidence.createSINIdentificationEvidence(
          concernRoleID, endDate, bdmIdentificationEvidenceVOUpdate,
          EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT,
          bdmIdentificationEvidenceVOUpdate.getFromDate());
        changeFlag = true;
      }
    }

    // Get firstName, middlename, lastname and evidenceID from datastore
    final String nameDateChange = personEntity
      .getAttribute(BDMLifeEventDatastoreConstants.NAME_DATE_CHANGE);
    // If there is a date of change then process a name update
    if (!StringUtil.isNullOrEmpty(nameDateChange)) {
      final String firstname =
        personEntity.getAttribute(BDMLifeEventDatastoreConstants.FIRST_NAME);
      final String middlename =
        personEntity.getAttribute(BDMLifeEventDatastoreConstants.MIDDLE_NAME);
      final String lastname =
        personEntity.getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME);
      final String nameEvidenceID = personEntity
        .getAttribute(BDMLifeEventDatastoreConstants.NAME_EVIDENCE_ID);
      final BDMNamesEvidenceVO bdmNamesEvidenceVO = new BDMNamesEvidenceVO();
      bdmNamesEvidenceVO.setFirstName(firstname);
      bdmNamesEvidenceVO.setMiddleName(middlename);
      bdmNamesEvidenceVO.setLastName(lastname);
      bdmNamesEvidenceVO.setNameType(ALTERNATENAMETYPE.REGISTERED);
      if (!StringUtil.isNullOrEmpty(nameEvidenceID)) {
        bdmNamesEvidenceVO.setEvidenceID(Long.parseLong(nameEvidenceID));
      }
      bdmNamesEvidence.createNamesEvidence(concernRoleID, bdmNamesEvidenceVO,
        EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
      changeFlag = true;
    }

    // Get prefName and evidenceID from datastore
    // If there is a date of change then process a pref name update
    if (!StringUtil.isNullOrEmpty(nameDateChange)) {

      final String prefFirstname = personEntity
        .getAttribute(BDMLifeEventDatastoreConstants.PREFERRED_NAME);

      final String middlename =
        personEntity.getAttribute(BDMLifeEventDatastoreConstants.MIDDLE_NAME);
      final String lastname =
        personEntity.getAttribute(BDMLifeEventDatastoreConstants.LAST_NAME);
      final String prefNameEvidenceID = personEntity
        .getAttribute(BDMLifeEventDatastoreConstants.PREF_NAME_EVIDENCE_ID);
      final BDMNamesEvidenceVO bdmPrefNamesEvidenceVO =
        new BDMNamesEvidenceVO();
      bdmPrefNamesEvidenceVO.setFirstName(prefFirstname);
      bdmPrefNamesEvidenceVO.setMiddleName(middlename);
      bdmPrefNamesEvidenceVO.setLastName(lastname);
      bdmPrefNamesEvidenceVO.setNameType(ALTERNATENAMETYPE.PREFERRED);

      if (!StringUtil.isNullOrEmpty(prefNameEvidenceID)) {
        // Update existing preferred name evidence
        if (!StringUtil.isNullOrEmpty(prefFirstname)) {
          bdmPrefNamesEvidenceVO
            .setEvidenceID(Long.parseLong(prefNameEvidenceID));
          bdmNamesEvidence.createNamesEvidence(concernRoleID,
            bdmPrefNamesEvidenceVO,
            EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
        } else {
          // Remove existing preferred name evidence
          BDMEvidenceUtil.removeEvidence(Long.parseLong(prefNameEvidenceID),
            PDCConst.PDCNAME);
        }

      } else {
        // Create preferred name evidence if never ever exist.
        if (!StringUtil.isNullOrEmpty(prefFirstname)) {
          bdmPrefNamesEvidenceVO.setEvidenceID(
            UniqueIDFactory.newInstance().getNextID().uniqueID);

          final HashMap<String, String> evidenceData =
            new BDMLifeEventUtil().getEvidenceData(bdmPrefNamesEvidenceVO);

          BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID,
            evidenceData, PDCConst.PDCNAME,
            EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
        }
      }
      changeFlag = true;
    }

    // BEGIN TASK 16215 - Calling SIN Integrity Check process when Name/SIN or
    // DOB evidence changes.
    if (changeFlag) {
      try {
        final BDMIntegrityCheckKey integrityCheckKey =
          new BDMIntegrityCheckKey();
        integrityCheckKey.concernRoleID = concernRoleID;
        BDMIntegrityCheckFactory.newInstance()
          .sinIntegrityCheckOnPerson(integrityCheckKey);
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
    // END TASK 16215

  }

}
