package curam.ca.gc.bdmoas.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.ca.gc.bdmoas.evidencemapping.util.impl.BDMOASEvidenceMappingConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.intf.AlternateName;
import curam.core.intf.ConcernRoleAlternateID;
import curam.core.struct.AlternateIDReadmultiDtls;
import curam.core.struct.AlternateIDReadmultiDtlsList;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameReadByTypeKey;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.datastore.impl.Entity;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BDMOAS FEATURE 92921: Class Added
 * Util Class to implement generic logic for BDM OAS Project
 *
 * @author SMisal
 *
 */
public class BDMOASApplicationUtil {

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvidence;

  public BDMOASApplicationUtil() {

    GuiceWrapper.getInjector().injectMembers(this);

  }

  /* BEGIN BDMOAS FEATURE 52093: Added */
  /**
   *
   * @param personEntity
   * @param concernRole
   */
  public void processPhoneEvd(final Entity personEntity,
    final curam.participant.impl.ConcernRole concernRole) {

    try {

      final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

      final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
        bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
          concernRole.getID(), PDCConst.PDCPHONENUMBER);

      // filter out evidence which are enddated
      final List<DynamicEvidenceDataDetails> existingPhoneList =
        evidenceDataDetailsList.stream()
          .filter(phoneDtls -> phoneDtls
            .getAttribute(BDMOASEvidenceMappingConstants.PHONE_EV_TODATE_ATTR)
            .getValue().isEmpty())
          .collect(Collectors.toList());

      List<DynamicEvidenceDataDetails> existingPhoneEvdDtls;

      final BDMPhoneEvidence bdmPhoneEvidence = new BDMPhoneEvidence();

      final List<BDMPhoneEvidenceVO> phoneEvidenceList =
        new ArrayList<BDMPhoneEvidenceVO>();

      final Entity[] communicationEntity =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMDatastoreConstants.COMMUNICATION_DETAILS);

      for (int i = 0; i < communicationEntity.length; i++) {

        final BDMPhoneEvidenceVO dataStorePhone = new BDMPhoneEvidenceVO();

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.PHONE_NUMBER)
          && communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.PHONE_NUMBER).isEmpty()) {

          continue;

        }

        Boolean isPreferredInd = false;

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM)) {

          isPreferredInd = communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM).toString()
            .equals("true") ? Boolean.TRUE : Boolean.FALSE;
        }
        dataStorePhone.setPhoneCountryCode(communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.COUNTRY_CODE).toString());
        dataStorePhone.setPhoneAreaCode(communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.PHONE_AREA_CODE).toString());
        dataStorePhone.setPhoneNumber(communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.PHONE_NUMBER).toString());

        final String phoneType = communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.PHONE_TYPE).toString();
        dataStorePhone.setPhoneType(phoneType);
        dataStorePhone.setPhoneExtension(communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.PHONE_EXT).toString());
        dataStorePhone.setFromDate(Date.getCurrentDate());

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.PHONE_IS_MOR)) {

          dataStorePhone.setMorningInd(communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.PHONE_IS_MOR).toString()
            .equals("true") ? Boolean.TRUE : Boolean.FALSE);

        }

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON)) {

          dataStorePhone.setAfternoonInd(communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON).toString()
            .equals("true") ? Boolean.TRUE : Boolean.FALSE);

        }

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.PHONE_IS_EVE)) {

          dataStorePhone.setEveningInd(communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.PHONE_IS_EVE).toString()
            .equals("true") ? Boolean.TRUE : Boolean.FALSE);

        }

        dataStorePhone.setPreferredInd(isPreferredInd);

        existingPhoneEvdDtls = existingPhoneList.size() > 0
          ? existingPhoneList.stream()
            .filter(phoneEvdDtls -> phoneEvdDtls
              .getAttribute(
                BDMOASEvidenceMappingConstants.PHONE_EV_PHONETYPE_ATTR)
              .getValue().equals(phoneType))
            .collect(Collectors.toList())
          : new ArrayList<DynamicEvidenceDataDetails>();

        if (existingPhoneEvdDtls.size() > 0) {
          // there might be multiple phone numbers
          boolean matched = false;
          boolean partiallyMatched = false;
          for (final DynamicEvidenceDataDetails phoneEvdItemDtals : existingPhoneEvdDtls) {
            final BDMPhoneEvidenceVO evidencePhone = new BDMPhoneEvidenceVO();
            new BDMLifeEventUtil().setEvidenceData(evidencePhone,
              phoneEvdItemDtals);
            if (!evidencePhone.equals(dataStorePhone)) {
              if (evidencePhone.getPhoneNumber()
                .equals(dataStorePhone.getPhoneNumber())
                && evidencePhone.getPhoneAreaCode()
                  .equals(dataStorePhone.getPhoneAreaCode())) {
                // update if partially matched
                dataStorePhone.setEvidenceID(evidencePhone.getEvidenceID());
                // Task 21654 - Phone number life event rework
                dataStorePhone
                  .setPhoneNumberChangeType(BDMPHONENUMBERCHANGETYPE.UPDATE);
                phoneEvidenceList.add(dataStorePhone);
                partiallyMatched = true;
                break;
              }
            } else {
              // evidence already exists, skip this phone from datastore
              matched = true;
              break;
            }
          }
          if (!matched && !partiallyMatched) {
            phoneEvidenceList.add(dataStorePhone);
          }

        } else {
          // no existing phone, create new evd
          phoneEvidenceList.add(dataStorePhone);
        }
      }

      bdmPhoneEvidence.createPhoneEvidence(concernRole.getID(),
        phoneEvidenceList, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(
        BDMOASConstants.ERROR_PHONE_EVIDENCE_MAPPING + concernRole.getID(),
        e.getMessage());
    }

  }
  /* END BDMOAS FEATURE 52093: Added */

  /**
   * Helper method to return active active alternate id.
   *
   * @param concernRoleID, typeCode
   * @return alternateID
   * @throws AppException
   * @throws InformationalException
   */
  public static String getActiveAlternateIDByConcernRoleIDAndType(
    final long concernRoleID, final String typeCode)
    throws AppException, InformationalException {

    String alternateID = "";

    final ConcernRoleAlternateID concernRoleAlternateIDObj =
      ConcernRoleAlternateIDFactory.newInstance();
    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();

    concernRoleIDStatusCodeKey.concernRoleID = concernRoleID;
    concernRoleIDStatusCodeKey.statusCode = RECORDSTATUS.NORMAL;

    final AlternateIDReadmultiDtlsList alternateIDList =
      concernRoleAlternateIDObj
        .searchByConcernRoleIDAndStatus(concernRoleIDStatusCodeKey);

    for (final AlternateIDReadmultiDtls alternateIDReadmultiDtls : alternateIDList.dtls) {
      if (typeCode.equals(alternateIDReadmultiDtls.typeCode)
        && (alternateIDReadmultiDtls.endDate.isZero()
          || !alternateIDReadmultiDtls.endDate
            .before(Date.getCurrentDate()))) {
        alternateID = alternateIDReadmultiDtls.alternateID;
      }
    }

    return alternateID;
  }

  /**
   * Method to get the person title.
   *
   * @param concernRoleID
   * @return title
   * @throws AppException
   * @throws InformationalException
   */

  public static String getPersonTitle(final long concernRoleID)
    throws AppException, InformationalException {

    String title = "";

    final AlternateName alternateNameObj = AlternateNameFactory.newInstance();
    final AlternateNameReadByTypeKey alternateNameReadByTypeKey =
      new AlternateNameReadByTypeKey();
    alternateNameReadByTypeKey.concernRoleID = concernRoleID;
    alternateNameReadByTypeKey.nameStatus = RECORDSTATUS.NORMAL;
    alternateNameReadByTypeKey.nameType = ALTERNATENAMETYPE.REGISTERED;
    AlternateNameDtls alternateNameDtls = null;

    try {
      alternateNameDtls =
        alternateNameObj.readByType(alternateNameReadByTypeKey);
      title = alternateNameDtls.title;
    } catch (final Exception e) {
      // Do nothing
    }

    return title;
  }

}
