package curam.ca.gc.bdm.util.integrity.impl;

import curam.ca.gc.bdm.application.impl.BDMPersonMatchConstants;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.BDMWSSINValidateInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSearchTable;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.intf.BDMWSSINValidateInterfaceIntf;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.impl.VerificationInterface;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.struct.PDCEvidenceDetails;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.impl.PDCConst;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.intf.VDIEDLink;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkAndDataItemIDDetails;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkAndDataItemIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.fact.VerificationControllerFactory;
import curam.verification.sl.infrastructure.intf.VerificationController;
import java.io.IOException;
import java.text.Normalizer;

public class BDMSINIntegrityCheckUtil {

  public BDMSINIntegrityCheckResult sinIntegrityCheck(
    final long concernRoleID) throws AppException, InformationalException {

    final BDMSINSIRSearchTable bdmSINSIRSearchTable =
      new BDMSINSIRSearchTable();

    final BDMSINIntegrityCheckDetails details =
      new BDMSINIntegrityCheckDetails(concernRoleID);

    setRequest(bdmSINSIRSearchTable, details);

    final String errMsg = validateRequest(bdmSINSIRSearchTable);
    if (null != errMsg) {
      // evidence not found
      return null;
      // throw new AppRuntimeException(new Exception(errMsg));
    }

    convertRequest(bdmSINSIRSearchTable);

    try {

      final BDMWSSINValidateInterfaceIntf wsSinService =
        new BDMWSSINValidateInterfaceImpl();

      final BDMSINSIRRestResponse restResponse =
        wsSinService.validatePersonBySIN(bdmSINSIRSearchTable);

      return new BDMSINIntegrityCheckResult(bdmSINSIRSearchTable,
        restResponse, details);

    } catch (final IOException e) {
      e.printStackTrace();
      return new BDMSINIntegrityCheckResult(bdmSINSIRSearchTable, null,
        details);
    }

  }

  public String
    validateRequest(final BDMSINSIRSearchTable bdmSINSIRSearchTable)
      throws AppException, InformationalException {

    if (StringUtil
      .isNullOrEmpty(bdmSINSIRSearchTable.getPerPersonSINIdentification())) {
      return "Identification not found";
    }
    if (StringUtil
      .isNullOrEmpty(bdmSINSIRSearchTable.getNcPersonGivenName())) {
      return "Given Name not found";
    }
    if (StringUtil.isNullOrEmpty(bdmSINSIRSearchTable.getNcPersonSurName())) {
      return "Surname not found";
    }
    if (StringUtil
      .isNullOrEmpty(bdmSINSIRSearchTable.getNcPersonBirthDate())) {
      return "Date of Birth not found";
    }

    /*
     * Commented out as part of TASK 93103
     * if (StringUtil
     * .isNullOrEmpty(bdmSINSIRSearchTable.getSsParentMaidenName())) {
     * return "Parent maiden name no found";
     * }
     */

    return null;
  }

  /**
   * Convert to upper case and remove the accented characters
   *
   * @param bdmSINSIRSearchTable
   * @throws AppException
   * @throws InformationalException
   */
  private void convertRequest(final BDMSINSIRSearchTable bdmSINSIRSearchTable)
    throws AppException, InformationalException {

    bdmSINSIRSearchTable.setNcPersonGivenName(
      unaccent(bdmSINSIRSearchTable.getNcPersonGivenName().toUpperCase()));

    if (!StringUtil
      .isNullOrEmpty(bdmSINSIRSearchTable.getNcPersonMiddleName())) {
      bdmSINSIRSearchTable.setNcPersonMiddleName(
        unaccent(bdmSINSIRSearchTable.getNcPersonMiddleName().toUpperCase()));
    }

    bdmSINSIRSearchTable.setNcPersonSurName(
      unaccent(bdmSINSIRSearchTable.getNcPersonSurName().toUpperCase()));

    bdmSINSIRSearchTable.setSsParentMaidenName(
      unaccent(bdmSINSIRSearchTable.getSsParentMaidenName().toUpperCase()));
  }

  public void setRequest(final BDMSINSIRSearchTable bdmSINSIRSearchTable,
    final BDMSINIntegrityCheckDetails details)
    throws AppException, InformationalException {

    final PersonAndEvidenceTypeList pdcEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    pdcEvidenceTypeList.concernRoleID = details.getConcernRoleID();
    pdcEvidenceTypeList.evidenceTypeList =
      PDCConst.PDCIDENTIFICATION + "|" + PDCConst.PDCNAME + "|"
        + PDCConst.PDCBIRTHANDDEATH + "|" + PDCConst.PDCADDRESS;

    final PDCEvidenceDetailsList pdcEvidenceDetailsList =
      PDCPersonFactory.newInstance()
        .listCurrentParticipantEvidenceByTypes(pdcEvidenceTypeList);

    for (final PDCEvidenceDetails pdcEvidence : pdcEvidenceDetailsList.list) {
      if (pdcEvidence.evidenceType.equals(PDCConst.PDCIDENTIFICATION)) {

        final EIEvidenceReadDtls readDtls = BDMEvidenceUtil
          .readEvidence(pdcEvidence.evidenceType, pdcEvidence.evidenceID);

        if (!CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER
          .equals(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMPersonMatchConstants.ALT_ID_TYPE))) {

          continue;
        }

        bdmSINSIRSearchTable.setPerPersonSINIdentification(
          BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMPersonMatchConstants.ALTERNATE_ID));

        details.setSinEvidenceDescriptorID(pdcEvidence.evidenceDescriptorID);
        continue;
      }

      if (pdcEvidence.evidenceType.equals(PDCConst.PDCNAME)) {

        final EIEvidenceReadDtls readDtls = BDMEvidenceUtil
          .readEvidence(pdcEvidence.evidenceType, pdcEvidence.evidenceID);

        if (!ALTERNATENAMETYPE.REGISTERED
          .equals(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_NAMETYPE))) {

          continue;
        }

        bdmSINSIRSearchTable
          .setNcPersonGivenName(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_FIRSTNAME));

        bdmSINSIRSearchTable
          .setNcPersonMiddleName(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_MIDDLENAME));

        bdmSINSIRSearchTable
          .setNcPersonSurName(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_LASTNAME));

        details.setNameEvidenceDescriptorID(pdcEvidence.evidenceDescriptorID);
        continue;
      }

      if (pdcEvidence.evidenceType.equals(PDCConst.PDCBIRTHANDDEATH)) {

        final EIEvidenceReadDtls readDtls = BDMEvidenceUtil
          .readEvidence(pdcEvidence.evidenceType, pdcEvidence.evidenceID);

        bdmSINSIRSearchTable
          .setSsParentMaidenName(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_MOTHERBIRTHLASTNAME));

        bdmSINSIRSearchTable
          .setNcPersonBirthDate(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_DATEOFBIRTH));

        details.setBirthLastName(BDMEvidenceUtil
          .getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_BIRTHLASTNAME)
          .toUpperCase());

        details.setDobEvidenceDescriptorID(pdcEvidence.evidenceDescriptorID);
        continue;
      }

      if (pdcEvidence.evidenceType.equals(PDCConst.PDCADDRESS)) {

        final EIEvidenceReadDtls readDtls = BDMEvidenceUtil
          .readEvidence(pdcEvidence.evidenceType, pdcEvidence.evidenceID);

        final DynamicEvidenceDataDetails dynEvdDataDtls =
          (DynamicEvidenceDataDetails) readDtls.evidenceObject;

        if (!CONCERNROLEADDRESSTYPE.PRIVATE
          .equals(BDMEvidenceUtil.getDynEvdAttrValue(readDtls,
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_ADDRESSTYPE))) {

          continue;
        }

        final Date toDate = (Date) DynamicEvidenceTypeConverter
          .convert(dynEvdDataDtls.getAttribute(
            BDMSINIntegrityCheckConstants.EVIDENCE_ATTNAME_TODATE));

        if (!toDate.isZero() && !Date.getCurrentDate().after(toDate)) {
          continue;
        }

        details
          .setAddressEvidenceDesciptorID(pdcEvidence.evidenceDescriptorID);

        continue;
      }
    }

  }

  public void
    triggerVerifiction(final EvidenceDescriptorKey evidenceDescriptorKey)
      throws AppException, InformationalException {

    if (countOutstandingVerifiction(
      evidenceDescriptorKey.evidenceDescriptorID) == 0) {

      final VerificationController verificationControllerObj =
        VerificationControllerFactory.newInstance();

      verificationControllerObj.insertVerification(evidenceDescriptorKey);
      Trace.kTopLevelLogger
        .info("Outstanding verification created on evidence - "
          + evidenceDescriptorKey.evidenceDescriptorID);

    } else {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "Outstanding verification already exists on evidence - "
        + evidenceDescriptorKey.evidenceDescriptorID);
    }
  }

  /**
   * Removes the existing verification on SIN during SIN Integrity check
   *
   * @param evidenceDescriptorID
   * @throws AppException
   * @throws InformationalException
   */
  public void removeExistingSINVerifiction(final long evidenceDescriptorID)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
      + "Delete Previous Outstanding verification if exists on SIN evidence - "
      + evidenceDescriptorID);
    try {
      final VerificationInterface verificationControllerObj =
        new curam.verification.sl.infrastructure.impl.VerificationController();
      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        new EvidenceDescriptorDtls();
      evidenceDescriptorDtls.evidenceDescriptorID = evidenceDescriptorID;
      verificationControllerObj
        .removeVerificationForSuperseededEvidence(evidenceDescriptorDtls);
      // delete vdiedlink table entries
      final EvidenceDescriptorKey areevidenceDescriptorKey =
        new EvidenceDescriptorKey();
      areevidenceDescriptorKey.evidenceDescriptorID = evidenceDescriptorID;
      // Delete VdiedLink entry
      final VDIEDLink vdiedLinkObj = VDIEDLinkFactory.newInstance();
      final VDIEDLinkAndDataItemIDDetailsList vdiedLinkDtlsList =
        vdiedLinkObj.readByEvidenceDescriptor(areevidenceDescriptorKey);
      for (final VDIEDLinkAndDataItemIDDetails linkdetails : vdiedLinkDtlsList.dtls) {
        final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();
        vdiedLinkKey.VDIEDLinkID = linkdetails.vdIEDLinkID;
        vdiedLinkObj.remove(vdiedLinkKey);
        Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
          + "VdiedLink entry on SIN evidence deleted - VdiedLinkID - "
          + linkdetails.vdIEDLinkID);
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "Previous Outstanding verification does not exists on evidence - "
        + evidenceDescriptorID);
    }
  }

  private int countOutstandingVerifiction(final long evidenceDescriptorID)
    throws AppException, InformationalException {

    final EvidenceDescriptorKey key = new EvidenceDescriptorKey();
    key.evidenceDescriptorID = evidenceDescriptorID;

    final String sql = "SELECT count(1) into :versionNo FROM "
      + "  VERIFICATION v JOIN VDIEDLINK v2 ON "
      + "  v.VDIEDLINKID = v2.VDIEDLINKID "
      + "WHERE v2.EVIDENCEDESCRIPTORID = :evidenceDescriptorID "
      + "AND v.VERIFICATIONSTATUS = 'VST2'";

    final EvidenceDescriptorDtls ret =
      (EvidenceDescriptorDtls) DynamicDataAccess
        .executeNs(EvidenceDescriptorDtls.class, key, false, sql);

    return ret.versionNo;

  }

  public CaseParticipantRoleDtls getCaseParticipantRolePC(
    final long concernRoleID) throws AppException, InformationalException {

    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = concernRoleID;

    final String sql = "SELECT "
      + "  c.CASEPARTICIPANTROLEID ,c.CASEID into :CASEPARTICIPANTROLEID, :CASEID "
      + "FROM " + "  CASEPARTICIPANTROLE c " + "JOIN CASEHEADER c2 ON "
      + "  c.CASEID = c2.CASEID " + "  AND c2.CASETYPECODE = 'CT2001' "
      + "  WHERE c.PARTICIPANTROLEID  = :concernRoleID "
      + "  AND c.TYPECODE = 'PRI'";

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      (CaseParticipantRoleDtls) DynamicDataAccess
        .executeNs(CaseParticipantRoleDtls.class, key, false, sql);

    caseParticipantRoleDtls.participantRoleID = concernRoleID;

    return caseParticipantRoleDtls;

  }

  public CaseParticipantRoleDtls removeExistingSINSIRResponceEvidence(
    final long concernRoleID) throws AppException, InformationalException {

    final CaseParticipantRoleDtls cprPC =
      getCaseParticipantRolePC(concernRoleID);

    final PersonAndEvidenceTypeList pdcEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    pdcEvidenceTypeList.concernRoleID = cprPC.participantRoleID;
    pdcEvidenceTypeList.evidenceTypeList =
      CASEEVIDENCE.BDMSINIDENTIFICATIONSTATUS;

    final PDCEvidenceDetailsList pdcEvidenceDetailsList =
      PDCPersonFactory.newInstance()
        .listCurrentParticipantEvidenceByTypes(pdcEvidenceTypeList);

    if (!pdcEvidenceDetailsList.list.isEmpty()) {

      BDMEvidenceUtil.removeEvidence(
        pdcEvidenceDetailsList.list.item(0).evidenceID,
        CASEEVIDENCE.BDMSINIDENTIFICATIONSTATUS);

    }
    return cprPC;
  }

  /**
   * Remove accented character
   *
   * @param input
   * @return
   */
  private String unaccent(final String input) {

    return Normalizer.normalize(input, Normalizer.Form.NFD)
      .replaceAll("[^\\p{ASCII}]", "");
  }
}
