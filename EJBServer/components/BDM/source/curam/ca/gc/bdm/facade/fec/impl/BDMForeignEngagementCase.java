package curam.ca.gc.bdm.facade.fec.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMATTACHMENTLINKTYPE;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPINTERIM;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.fact.BDMFEAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink;
import curam.ca.gc.bdm.entity.intf.BDMForeignApplication;
import curam.ca.gc.bdm.entity.intf.BDMForeignLiaison;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryKey;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMAttachmentIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMCreateModifyFA;
import curam.ca.gc.bdm.facade.fec.struct.BDMDeleteFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetailsForRead;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAConcernRoleKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFACountryCodeKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAList;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAViewAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECReadICDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfFAApplicationTypes;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfOffices;
import curam.ca.gc.bdm.facade.fec.struct.BDMModifyFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMReadFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMUserAccessIndicatorStruct;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMListICAttachmentDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.impl.BDMForeignPeriod;
import curam.ca.gc.bdm.message.BDMFEC;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.fec.intf.BDMMaintainForeignEngagementCase;
import curam.ca.gc.bdm.sl.fec.struct.BDMFAInterimCodeDetailsList;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.ClientMerge;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CuramInd;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.UserRoleDetails;
import curam.core.struct.UsersKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.supervisor.facade.struct.ReserveTaskDetailsForUser;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.GeneralConstants;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BDMForeignEngagementCase
  extends curam.ca.gc.bdm.facade.fec.base.BDMForeignEngagementCase {

  private final BDMUtil bdmUtil = new BDMUtil();

  /**
   * This BPO is used for creating a foreign engagement case.
   */
  @Override
  public CreateIntegratedCaseResultAndMessages
    createFEIntegratedCase(final BDMFECaseDetails details)
      throws AppException, InformationalException {

    CreateIntegratedCaseResultAndMessages caseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMMaintainForeignEngagementCase bdmMaintainForeignEngagementCase =
      BDMMaintainForeignEngagementCaseFactory.newInstance();

    final BDMModifyFECaseDetails bdmModifyFECaseDetails =
      new BDMModifyFECaseDetails();

    validateCreateEditFECase(details, bdmModifyFECaseDetails);

    caseResultAndMessages =
      bdmMaintainForeignEngagementCase.createFEIntegratedCase(details);

    return caseResultAndMessages;
  }

  /**
   * Validation method for Create / Edit - FE Case.
   *
   * @author hamed.mohammed
   * @throws AppException
   * @throws InformationalException
   */
  private void validateCreateEditFECase(final BDMFECaseDetails details,
    final BDMModifyFECaseDetails bdmModifyFECaseDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (details.concernRoleID == CuramConst.gkZero) {
      details.concernRoleID =
        bdmModifyFECaseDetails.modifyICDetails.concernRoleID;
    }

    final RecordCount recordCount =
      BDMFECaseFactory.newInstance().searchFECaseExistsForCountry(details);

    boolean countryModifiedInd = false;

    BDMFECaseDtls bdmfeCaseDtls = null;
    CaseHeaderDtls caseHeaderDtls = null;
    if (bdmModifyFECaseDetails.modifyICDetails.caseID != CuramConst.gkZero) {

      final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
        BDMFECaseFactory.newInstance();

      final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      bdmfeCaseKey.caseID = bdmModifyFECaseDetails.modifyICDetails.caseID;
      caseHeaderKey.caseID = bdmModifyFECaseDetails.modifyICDetails.caseID;

      try {
        bdmfeCaseDtls = bdmfeCase.read(bdmfeCaseKey);
        caseHeaderDtls = CaseHeaderFactory.newInstance().read(caseHeaderKey);
      } catch (final RecordNotFoundException rnfe) {
        // do nothing
        bdmfeCaseDtls = new BDMFECaseDtls();
        caseHeaderDtls = new CaseHeaderDtls();
      }

      if (bdmfeCaseDtls.countryCode.equals(details.countryCode)) {
        countryModifiedInd = true;
      }
      // BEGIN: Bug 73473: User is able to modify the country, which has a
      // Foreign Application in open and active state.
      final BDMCaseKey bdmCaseKey = new BDMCaseKey();
      bdmCaseKey.caseID = bdmModifyFECaseDetails.modifyICDetails.caseID;
      final BDMFAList bdmfaList = listForeignApplications(bdmCaseKey);

      final int bdmFAListSize = bdmfaList.bdmFADetails.size();
      BDMFADetails bdmfaDetails = null;
      if (bdmFAListSize > CuramConst.gkZero) {
        for (int i = 0; i < bdmFAListSize; i++) {
          bdmfaDetails = new BDMFADetails();
          bdmfaDetails = bdmfaList.bdmFADetails.item(i);

          if (bdmfaDetails.recordStatus.equals(RECORDSTATUS.NORMAL)
            && !countryModifiedInd) {

            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(new AppException(
                BDMFEC.ERR_COUNTRY_CANNOT_BE_MODIFIED_SINCE_ACTIVE_LIAISON_OR_ACTIVE_FA_EXIST),
                CuramConst.gkEmpty, InformationalType.kError);
          }
        }
      }
      // END: Bug 73473: User is able to modify the country, which has a Foreign
      // Application in open and active state.

    }

    if (recordCount.count > 0 && !countryModifiedInd) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_AN_OPEN_STATUS_FE_CASE_EXIST),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    informationalManager.failOperation();

  }

  /**
   * This BPO is used for reading the foreign engagement case details.
   */
  @Override
  public BDMReadFECaseDetails readFEIntegratedCase(
    final IntegratedCaseIDKey key) throws curam.util.exception.AppException,
    curam.util.exception.InformationalException {

    BDMReadFECaseDetails bdmReadFECaseDetails = new BDMReadFECaseDetails();

    bdmReadFECaseDetails = BDMMaintainForeignEngagementCaseFactory
      .newInstance().readFEIntegratedCase(key);

    return bdmReadFECaseDetails;

  }

  /**
   * This BPO is used for modifying the foreign engagement case details.
   */
  @Override
  public void modifyFEIntegratedCase(final BDMModifyFECaseDetails details)
    throws curam.util.exception.AppException,
    curam.util.exception.InformationalException {

    final BDMFECaseDetails bdmfeCaseDetails = new BDMFECaseDetails();
    bdmfeCaseDetails.countryCode = details.countryCode;

    validateCreateEditFECase(bdmfeCaseDetails, details);

    BDMMaintainForeignEngagementCaseFactory.newInstance()
      .modifyFEIntegratedCase(details);
  }

  /**
   * This BPO is used for fetching details of the foreign engagement case
   * details for the Home Page.
   */
  @Override
  public BDMFECReadICDetails
    readFECDetailsForHome(final IntegratedCaseIDKey key)
      throws AppException, InformationalException {

    BDMFECReadICDetails bdmfecReadICDetails = new BDMFECReadICDetails();

    bdmfecReadICDetails = BDMMaintainForeignEngagementCaseFactory
      .newInstance().readFECDetailsForHome(key);

    bdmfecReadICDetails.totalForeignResidencePeriod = CuramConst.gkEmpty;

    bdmfecReadICDetails.totalForeignContributionPeriod = getForeignPeriod(
      key.caseID, CASEEVIDENCEEntry.BDM_FOREIGN_CONTRIBUTION_PERIOD);
    bdmfecReadICDetails.totalForeignResidencePeriod = getForeignPeriod(
      key.caseID, CASEEVIDENCEEntry.BDM_FOREIGN_RESIDENCE_PERIOD);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();

    // Set the concern role key
    concernRoleKey.concernRoleID =
      bdmfecReadICDetails.readFEICDetails.details.concernRoleID;

    final CuramInd duplicateInd =
      clientMergeObj.isConcernRoleDuplicate(concernRoleKey);

    // Check if the concern role has been marked as a duplicate
    if (duplicateInd.statusInd) {

      final InformationalManager informationalManager =
        TransactionInfo.getInformationalManager();

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.WAR_DUPLICATE_PERSON_FEC_CASE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(localisableString,
          GeneralConstants.kEmpty,
          InformationalElement.InformationalType.kWarning,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);

      final String warnings[] =
        informationalManager.obtainInformationalAsString();

      for (int loop = 0; loop < warnings.length; loop++) {

        final InformationalMsgDtls informationalMsgDtls =
          new InformationalMsgDtls();

        informationalMsgDtls.informationMsgTxt = warnings[loop];

        bdmfecReadICDetails.informationalMsgDtlsList.dtls
          .addRef(informationalMsgDtls);
      }
    }

    return bdmfecReadICDetails;
  }

  /**
   * This BPO is used for creating the foreign application.
   */
  @Override
  public curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey
    createForeignApplication(final BDMCreateModifyFA details)
      throws AppException, InformationalException {

    // validation of foreign application
    validateForeignApplication(details);

    // Service Layer Call
    return BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createForeignApplication(details);

  }

  /**
   * Private method to validate create/modify foreign application
   * BR-01 - User enters a future date for the ‘Received Date’
   * BR-02 - User enters ‘Start Date’ earlier than ‘Received Date’
   * BR-03 - User selects a ‘Foreign Office’ and also selects ‘BESS’
   * BR-04 - User attempts to Edit a Foreign Application whose Record Status is
   * ‘Canceled’
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  private void validateForeignApplication(final BDMCreateModifyFA details)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (details.receiveDate.after(Date.getCurrentDate())) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_RECEIVED_DATE_CANNOT_BE_IN_FUTURE),
        CuramConst.gkEmpty, InformationalType.kError);
    }
    if (details.externalPartyOfficeID != CuramConst.gkZero
      && details.bessInd) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMFEC.ERR_FOREIGN_OFFICE_ENTERED_BESS_CANNOT_BE_SELECTED),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    if (details.fApplicationID != CuramConst.gkZero) {

      final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
        new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
      bdmfaKey.fApplicationID = details.fApplicationID;

      BDMForeignApplicationDtls bdmfaDtls = new BDMForeignApplicationDtls();

      try {
        bdmfaDtls = BDMForeignApplicationFactory.newInstance().read(bdmfaKey);
      } catch (final RecordNotFoundException rnfe) {
        bdmfaDtls = new BDMForeignApplicationDtls();
      }
      if (bdmfaDtls.recordStatus.equals(RECORDSTATUS.CANCELLED)) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMFEC.ERR_YOU_CANNOT_MODIFY_THIS_RECORD),
          CuramConst.gkEmpty, InformationalType.kError);
      }
    }

    if (details.typeCode
      .equalsIgnoreCase(BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM)
      && details.interimChkStrList.contains(BDMFOREIGNAPPINTERIM.OTHER)
      && StringUtil.isNullOrEmpty(details.interimOther)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMFEC.ERR_INTERIM_CHK_BOX_SELECTED_SPECIFY_NOT_ENTERED),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    if (details.typeCode.equalsIgnoreCase(
      BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM) && details.bessInd) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMFEC.ERR_INTERIM_CHK_BOX_SELECTED_BESS_CANNOT_BE_SELECTED),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    if (details.typeCode
      .equalsIgnoreCase(BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM)
      && details.countryCode.equalsIgnoreCase(BDMSOURCECOUNTRY.US)
      && StringUtil.isNullOrEmpty(details.consent)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_CLIENT_MUST_BE_SELECTED),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    // Fail for business validations.
    informationalManager.failOperation();

  }

  /**
   * This BPO is used for modifying/editing the foreign application.
   */
  @Override
  public void modifyForeignApplication(final BDMCreateModifyFA details)
    throws AppException, InformationalException {

    validateForeignApplication(details);

    // Service Layer Call
    BDMMaintainForeignEngagementCaseFactory.newInstance()
      .modifyForeignApplication(details);

  }

  /**
   * This BPO is used for reading the foreign application details.
   */
  @Override
  public BDMFADetails readForeignApplication(final BDMFAKey key)
    throws AppException, InformationalException {

    BDMFADetails bdmfaDetails = new BDMFADetails();

    // Service Layer Call
    bdmfaDetails = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .readForeignApplication(key);

    return bdmfaDetails;
  }

  /**
   * This BPO is used for getting a list of foreign applications on a foreign
   * engagement case.
   */
  @Override
  public BDMFAList listForeignApplications(final BDMCaseKey key)
    throws AppException, InformationalException {

    BDMFAList bdmfaList = new BDMFAList();

    // Service Layer Call
    bdmfaList = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .listForeignApplications(key);

    return bdmfaList;
  }

  /**
   * This BPO is used to fetch foreign application details for the view screen.
   */
  @Override
  public BDMFADetails viewForeignApplication(final BDMFAKey key)
    throws AppException, InformationalException {

    // Service Layer Call
    final BDMFADetails bdmfaDetails = BDMMaintainForeignEngagementCaseFactory
      .newInstance().viewForeignApplication(key);

    bdmfaDetails.isIOClientContact = BDMUtil.isIOClientContact();

    return bdmfaDetails;
  }

  /**
   * This BPO is used for deleting/canceling a foreign application.
   */
  @Override
  public void deleteForeignApplication(final BDMDeleteFADetails details)
    throws AppException, InformationalException {

    validateDeleteForeignApplication(details);

    // Service Layer Call
    BDMMaintainForeignEngagementCaseFactory.newInstance()
      .deleteForeignApplication(details);

  }

  /**
   * Private method to validate delete foreign application
   * BR-01 - User attempts to Delete a Foreign Application which is already
   * Cancelled (Status=Canceled)
   * BR-02 - User attempts to delete a Foreign Application that is already
   * Completed (Status= Completed)
   * BR-03 - User attempts to delete a Foreign Application record which is
   * linked to Foreign Liaison
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  private void
    validateDeleteForeignApplication(final BDMDeleteFADetails details)
      throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
      new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();

    bdmfaKey.fApplicationID = details.fApplicationID;

    final BDMForeignApplicationDtls bdmfaDtls =
      BDMForeignApplicationFactory.newInstance().read(bdmfaKey);

    if (bdmfaDtls.recordStatus.equals(RECORDSTATUS.CANCELLED)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMFEC.ERR_YOU_CANNOT_DELETE_THIS_RECORD_ALREADY_DELETED),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    if (bdmfaDtls.recordStatus.equals(RECORDSTATUS.NORMAL)
      && bdmfaDtls.status.equals(BDMFOREIGNAPPSTATUS.COMPLETED)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMFEC.ERR_YOU_CANNOT_DELETE_THIS_RECORD_SINCE_COMPLETED),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    // TODO-FL BEGIN: this validation needed to implemented when foreign liaison
    // is implemented.
    // START Bug 107945: E2E Defect - Deleting Foreign Application that is
    // linked to a foreign Liaison and business rule not working
    BDMForeignLiaisonKey bdmForeignLiaisonKey = new BDMForeignLiaisonKey();

    bdmForeignLiaisonKey =
      BDMUtil.findActiveForeignLiaison(bdmfaDtls.fApplicationID);

    if (isActiveForeignApplicationExists(
      bdmForeignLiaisonKey).changeAllIndicator) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMFEC.ERR_RECORD_CANNOT_BE_DELETED_SINCE_AN_ACTIVE_LIAISON),
        CuramConst.gkEmpty, InformationalType.kError);

    }
    // TODO-FL END:
    // END Bug 107945

    // Fail for business validations.
    informationalManager.failOperation();

  }

  /**
   * Bug 107945: E2E Defect - Deleting Foreign Application that is linked to a
   * foreign Liaison and business rule not working
   *
   *
   * @param bdmForeignLiaisonKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public IndicatorStruct isActiveForeignApplicationExists(
    final BDMForeignLiaisonKey bdmForeignLiaisonKey)
    throws AppException, InformationalException {

    final IndicatorStruct indicatorStruct = new IndicatorStruct();

    final BDMForeignLiaison bdmForeignLiaison =
      BDMForeignLiaisonFactory.newInstance();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final BDMForeignLiaisonDtls bdmForeignLiaisonDtls =
      bdmForeignLiaison.read(nfIndicator, bdmForeignLiaisonKey);

    BDMForeignApplicationKey bdmForeignApplicationKey = null;

    BDMForeignApplicationDtls bdmForeignApplicationDtls = null;

    final BDMForeignApplication bdmForeignApplication =
      BDMForeignApplicationFactory.newInstance();

    if (!nfIndicator.isNotFound()
      && !StringUtil.isNullOrEmpty(bdmForeignLiaisonDtls.foreignAppIDs)) {

      // separate tab delimited list of selected cases into an array
      final List<String> foreignApplicationIDs =
        StringUtil.delimitedText2StringList(
          bdmForeignLiaisonDtls.foreignAppIDs, CuramConst.gkTabDelimiterChar);

      for (final String foreignApplicationID : foreignApplicationIDs) {

        bdmForeignApplicationKey = new BDMForeignApplicationKey();
        bdmForeignApplicationKey.fApplicationID =
          Long.parseLong(foreignApplicationID);

        bdmForeignApplicationDtls = new BDMForeignApplicationDtls();

        bdmForeignApplicationDtls =
          bdmForeignApplication.read(bdmForeignApplicationKey);

        if (bdmForeignApplicationDtls.recordStatus
          .equals(RECORDSTATUS.NORMAL)) {

          indicatorStruct.changeAllIndicator = true;

          break;

        }

      }

    }

    return indicatorStruct;

  }

  /**
   * This BPO is used for getting the foreign application history records.
   */
  @Override
  public BDMFAList listFAHistory(final BDMFAKey key)
    throws AppException, InformationalException {

    // Service Layer Call
    final BDMFAList bdmfaList = BDMMaintainForeignEngagementCaseFactory
      .newInstance().listFAHistory(key);

    return bdmfaList;
  }

  /**
   * This BPO is used for creating the foreign engagement case attachment
   * details.
   */
  @Override
  public BDMAttachmentIDs
    createFECaseAttachment(final BDMFAAttachmentDetails createFAAttachment)
      throws AppException, InformationalException {

    return BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(createFAAttachment);

    // return bdmAttachmentIDs;

  }

  /**
   * This BPO is used for modifying the foreign engagement case attachment
   * details.
   */
  @Override
  public BDMAttachmentIDs modifyFECaseAttachment(
    final curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails details)
    throws AppException, InformationalException {

    final BDMAttachmentIDs bdmAttachmentIDs =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .modifyFECaseAttachment(details);

    return bdmAttachmentIDs;
  }

  @Override
  public BDMFAViewAttachmentDetails
    readFAAttachmentDetails(final BDMFAAttachmentKey key)
      throws AppException, InformationalException {

    final BDMFAViewAttachmentDetails bdmfaViewAttachmentDetails =
      new BDMFAViewAttachmentDetails();
    return bdmfaViewAttachmentDetails;
  }

  @Override
  public BDMListICAttachmentDetails listFAAttachments(final BDMCaseKey key)
    throws AppException, InformationalException {

    final BDMListICAttachmentDetails bdmListICAttachmentDetails =
      new BDMListICAttachmentDetails();
    return bdmListICAttachmentDetails;

  }

  /**
   * This BPO is used for fetching the list of foreign identifier for the
   * selected person concern role ID.
   */
  @Override
  public BDMListOfForeignIDs
    getListOfForeignIdentifiers(final BDMFAConcernRoleKey key)
      throws AppException, InformationalException {

    final BDMListOfForeignIDs bdmListOfForeignIDs =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .getListOfForeignIdentifiers(key);

    return bdmListOfForeignIDs;
  }

  /**
   * This BPO is used for fetching the list of application type codes for the
   * selected country.
   */
  @Override
  public BDMListOfFAApplicationTypes
    getListOfFAApplicationTypesByCountryCode(final BDMFACountryCodeKey key)
      throws AppException, InformationalException {

    final BDMListOfFAApplicationTypes bdmListOfFAApplicationTypes =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .getListOfFAApplicationTypesByCountryCode(key);
    bdmListOfFAApplicationTypes.isIOClientContact =
      BDMUtil.isIOClientContact();
    return bdmListOfFAApplicationTypes;

  }

  /**
   * This BPO is used for fetching the list of foreign offices for the selected
   * country.
   */
  @Override
  public BDMListOfOffices
    getListOfForeignOffices(final BDMFACountryCodeKey key)
      throws AppException, InformationalException {

    final BDMListOfOffices bdmListOfOffices =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .getListOfForeignOffices(key);

    return bdmListOfOffices;
  }

  /**
   * This BPO is used for getting country for a foreign engagement case.
   */
  @Override
  public BDMFECaseDtls readCountryCode(final CaseHeaderKey key)
    throws AppException, InformationalException {

    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();

    bdmfeCaseDtls = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .readCountryCode(key);

    return bdmfeCaseDtls;
  }

  /**
   * This BPO is used to fetch list of linked foreign applications with an
   * attachment.
   */
  @Override
  public BDMFAList getFAListLinkedToAttachment(final BDMFAAttachmentKey key)
    throws AppException, InformationalException {

    BDMFAList bdmfaList = new BDMFAList();

    // Service Layer Call
    bdmfaList = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .getFAListLinkedToAttachment(key);

    return bdmfaList;
  }

  /**
   * This BPO is used for canceling/deleting an foreign engagement case
   * attachment.
   */
  @Override
  public void cancelFECaseAttachment(
    final curam.core.facade.struct.CancelCaseAttachmentKey key)
    throws AppException, InformationalException {

    validateCancelFECAttachment(key);

    BDMMaintainForeignEngagementCaseFactory.newInstance()
      .cancelFECaseAttachment(key);

  }

  /**
   * Private method to validate delete attachment.
   * BR-01 - User attempts to Delete an attached which is already
   * is linked to Foreign Application or Foreign Liaison.
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  private void validateCancelFECAttachment(
    final curam.core.facade.struct.CancelCaseAttachmentKey key)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (faFlRecordIsActive(key.cancelCaseAttachmentKey.attachmentID)) {

      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMFEC.ERR_RECORD_CANNOT_BE_DELETED_SINCE_AN_ACTIVE_LIAISON_OR_ACTIVE_FA_EXIST),
          CuramConst.gkEmpty, InformationalType.kError);
    }

    // Fail for business validations.
    informationalManager.failOperation();

  }

  /**
   * This is a utility method, it returns true if there are active foreign
   * application(s) and foreign liaison(s).
   *
   * @param attachmentID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean faFlRecordIsActive(final long attachmentID)
    throws AppException, InformationalException {

    boolean faFlRecordIsActive = false;

    final curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();

    final BDMFEAttachmentLinkKeyStruct3 bdmfeAttachmentLinkKeyStruct3 =
      new BDMFEAttachmentLinkKeyStruct3();
    bdmfeAttachmentLinkKeyStruct3.attachmentID = attachmentID;
    bdmfeAttachmentLinkKeyStruct3.attachmentLinkTypCd =
      BDMATTACHMENTLINKTYPE.FAATTACHMENTLINK;
    bdmfeAttachmentLinkKeyStruct3.recordStatus = RECORDSTATUS.NORMAL;

    final BDMFEAttachmentLinkDtlsList bdmfeAttachmentLinkDtlsList =
      bdmfeAttachmentLink.readByAttachmentIDAttchmtLnkTypCdAndRecordStatus(
        bdmfeAttachmentLinkKeyStruct3);

    final int listSize = bdmfeAttachmentLinkDtlsList.dtls.size();

    BDMFEAttachmentLinkDtls bdmfeAttachmentLinkDtls = null;
    curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey = null;
    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    BDMForeignApplicationDtls bdmfaDtls = null;

    for (int i = 0; i < listSize; i++) {
      bdmfeAttachmentLinkDtls = new BDMFEAttachmentLinkDtls();
      bdmfaKey = new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
      bdmfeAttachmentLinkDtls = bdmfeAttachmentLinkDtlsList.dtls.item(i);
      bdmfaKey.fApplicationID = bdmfeAttachmentLinkDtls.relatedID;
      bdmfaDtls = bdmfa.read(bdmfaKey);
      if (bdmfaDtls.recordStatus.equals(RECORDSTATUS.NORMAL)) {
        faFlRecordIsActive = true;
      }
    }
    return faFlRecordIsActive;
  }

  /**
   * This BPO is used for reading attachment wizard information for create
   * attachment wizard screen.
   */
  @Override
  public BDMFAAttachmentDetails readFECaseAttachmentWizardDetails(
    final BDMAttachmentIDs key) throws AppException, InformationalException {

    final BDMFAAttachmentDetails bdmfaAttachmentDetails =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .readFECaseAttachmentWizardDetails(key);

    return bdmfaAttachmentDetails;
  }

  /**
   * This BPO is used for reading attachment wizard information for edit
   * attachment wizard screen.
   */
  @Override
  public BDMFAAttachmentDetailsForRead readFECAttachmentWizardForModify(
    final BDMAttachmentIDs key) throws AppException, InformationalException {

    final BDMFAAttachmentDetailsForRead bdmfaAttachmentDetails =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .readFECAttachmentWizardForModify(key);

    return bdmfaAttachmentDetails;
  }

  /**
   * This BPO is used for displaying list of foreign application which are not
   * linked to an attachment yet.
   */
  @Override
  public BDMFAList listFANotLinkedWithAttachment(final BDMAttachmentIDs key)
    throws AppException, InformationalException {

    BDMFAList bdmfaList = new BDMFAList();

    // Service Layer Call
    bdmfaList = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .listFANotLinkedWithAttachment(key);

    return bdmfaList;
  }

  /**
   * This BPO is used for doing unlinking of a foreign application with an
   * attachment.
   */
  @Override
  public void unlinkFAFromAttachment(final BDMFAAttachmentKey key)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();

    final BDMFEAttachmentLinkKey bdmfeAttachmentLinkKey =
      new BDMFEAttachmentLinkKey();
    bdmfeAttachmentLinkKey.feAttachmentLinkID = key.feAttachmentLinkID;

    final BDMFEAttachmentLinkDtls bdmfeAttachmentLinkDtls =
      bdmfeAttachmentLink.read(bdmfeAttachmentLinkKey);

    if (bdmfeAttachmentLinkDtls.recordStatus.equals(RECORDSTATUS.CANCELLED)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMFEC.ERR_YOU_CANNOT_DELETE_THIS_RECORD_ALREADY_DELETED),
        CuramConst.gkEmpty, InformationalType.kError);
    }

    // Fail for business validations.
    informationalManager.failOperation();

    BDMMaintainForeignEngagementCaseFactory.newInstance()
      .unlinkFAFromAttachment(key);

  }

  /**
   * This method is used for viewing foreign application history record
   * information on the view detailed screen:
   * BDMFEC_detailsForeignApplicationHistory
   */
  @Override
  public BDMFADetails viewForeignApplicationHistory(final BDMFAHistoryKey key)
    throws AppException, InformationalException {

    // Service Layer Call
    final BDMFADetails bdmfaDetails = BDMMaintainForeignEngagementCaseFactory
      .newInstance().viewForeignApplicationHistory(key);
    bdmfaDetails.isIOClientContact = BDMUtil.isIOClientContact();

    return bdmfaDetails;
  }

  /**
   * Gets the evidence list and loop through them to get start and end date
   * and calculates the foreign contribution period
   *
   * details for the Home Page.
   */
  private String getForeignPeriod(final long caseId,
    final CASEEVIDENCEEntry caseevidenceEntry)
    throws AppException, InformationalException {

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = caseId;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    final StandardEvidenceInterface standardEvidenceInterface =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap()
        .getEvidenceType(caseevidenceEntry.getCode());

    final List<BDMForeignPeriod> bdmForeignPeriodList = new ArrayList<>();
    for (int i = 0; i < activeEvdList.dtls.size(); i++) {
      if (activeEvdList.dtls.get(i).evidenceType
        .equals(caseevidenceEntry.getCode())) {
        final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();
        readEvidenceKey.evidenceID = activeEvdList.dtls.get(i).evidenceID;
        readEvidenceKey.evidenceType = caseevidenceEntry.getCode();

        final DynamicEvidenceDataDetails evidenceData =
          (DynamicEvidenceDataDetails) standardEvidenceInterface
            .readEvidence(readEvidenceKey);
        final String startDate =
          evidenceData.getAttribute("startDate").getValue();

        final String endDate =
          evidenceData.getAttribute("endDate").getValue();

        final BDMForeignPeriod bdmForeignPeriod =
          new BDMForeignPeriod(startDate, endDate);
        bdmForeignPeriod.setPeriod();
        bdmForeignPeriodList.add(bdmForeignPeriod);
      }
    }
    // loop through the foreignContributionPeriodList calculate the sum of
    // years, months, days in all objects

    int days = 0;
    int months = 0;
    int years = 0;
    for (final BDMForeignPeriod fcpElm : bdmForeignPeriodList) {
      days = days + fcpElm.getDayCount();
      months = months + fcpElm.getMonthCount();
      years = years + fcpElm.getYearCount();

    }

    final Period totalPeriod = Period.of(years, months, days).normalized();

    // Total Foreign Residence Period
    int numOfMonths = totalPeriod.getMonths() + totalPeriod.getDays() / 30;
    final int numOfDays = totalPeriod.getDays() % 31;
    final int numOfYears = totalPeriod.getYears() + numOfMonths / 12;
    numOfMonths = numOfMonths % 12;

    String totalForeignPeriod = CuramConst.gkEmpty;

    final String currentLoggedInUserDefaultLocale =
      BDMUtil.getLoggedInUserLocale();

    if (currentLoggedInUserDefaultLocale
      .equals(BDMConstants.gkLocaleLowerEN)) {

      // 0 year 0 month and 0 day
      totalForeignPeriod = String.format("%s %s %s %s and %s %s", numOfYears,
        numOfYears > 1 ? "years" : "year", numOfMonths,
        numOfMonths > 1 ? "months" : "month", numOfDays,
        numOfDays > 1 ? "days" : "day");
    }

    if (currentLoggedInUserDefaultLocale
      .equals(BDMConstants.gkLocaleLowerFR)) {

      // 0 année 0 mois 0 jour 
      totalForeignPeriod = String.format("%s %s %s %s and %s %s", numOfYears,
        numOfYears > 1 ? "années" : "année", numOfMonths,
        numOfMonths > 1 ? "mois" : "mois", numOfDays,
        numOfDays > 1 ? "jours" : "jour");
    }

    return totalForeignPeriod;
  }

  @Override
  public BDMFAInterimCodeDetailsList listFAInterimCodeDetails()
    throws AppException, InformationalException {

    BDMFAInterimCodeDetailsList bdmfaInterimCodeDetailsList =
      new BDMFAInterimCodeDetailsList();

    bdmfaInterimCodeDetailsList = BDMMaintainForeignEngagementCaseFactory
      .newInstance().listFAInterimCodeDetails();

    Collections.sort(bdmfaInterimCodeDetailsList.bdmFAInterimCodeDetails,
      (a, b) -> a.code.compareTo(b.code));

    return bdmfaInterimCodeDetailsList;
  }

  @Override
  public void reserveTask(final ReserveTaskDetailsForUser details)
    throws AppException, InformationalException {

    final BDMMaintainForeignEngagementCase bdmMaintainForeignEngagementCase =
      BDMMaintainForeignEngagementCaseFactory.newInstance();

    bdmMaintainForeignEngagementCase.reserveTask(details);
  }

  @Override
  public BDMAttachmentIDs
    createFECaseAttachmentLinkingFA(final BDMFAAttachmentDetails details)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub

    return BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachmentLinkingFA(details);

  }

  @Override
  public BDMAttachmentIDs
    modifyFECaseAttachmentLinkingFA(final BDMFAAttachmentDetails details)
      throws AppException, InformationalException {

    return BDMMaintainForeignEngagementCaseFactory.newInstance()
      .modifyFECaseAttachmentLinkingFA(details);
  }

  @Override
  public BDMUserAccessIndicatorStruct isUserAllowedToApplyEvidenceChanges()
    throws AppException, InformationalException {

    final BDMUserAccessIndicatorStruct accessIndicatorStruct =
      new BDMUserAccessIndicatorStruct();

    final String user = TransactionInfo.getProgramUser();

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = user;

    final UserRoleDetails userRoleDetails =
      UsersFactory.newInstance().readUserRole(usersKey);

    if (userRoleDetails.roleName
      .equalsIgnoreCase(BDMConstants.kIOBenefitOfficer)
      || userRoleDetails.roleName
        .equalsIgnoreCase(BDMConstants.kIOSupervisor)) {
      accessIndicatorStruct.isUserAllowedToApplyEvidence = true;
    }

    return accessIndicatorStruct;
  }

}
