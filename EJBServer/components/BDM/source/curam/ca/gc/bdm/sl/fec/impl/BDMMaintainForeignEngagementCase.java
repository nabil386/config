package curam.ca.gc.bdm.sl.fec.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMATTACHMENTLINKTYPE;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPINTERIM;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMSTATUSOFAGREEMENT;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.entity.fact.BDMCCodeAplTypMappingFactory;
import curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.entity.fact.BDMFAHistoryFactory;
import curam.ca.gc.bdm.entity.fact.BDMFEAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskConfigCatTypeLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.fact.BDMWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMConcernRoleCommRMKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMFAAttachmentNoticationTaskDetails;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetails;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetailsList;
import curam.ca.gc.bdm.entity.intf.BDMExternalParty;
import curam.ca.gc.bdm.entity.intf.BDMTask;
import curam.ca.gc.bdm.entity.intf.BDMTaskConfigCatTypeLink;
import curam.ca.gc.bdm.entity.struct.BDMCCodeAplTypMappingDtls;
import curam.ca.gc.bdm.entity.struct.BDMCCodeAplTypMappingKey;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyNameAndTypeKey;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyNameDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyTypeAndAddrKey;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyDtls;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyKey;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryDtls;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryKey;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMFAKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKeyStruct4;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskConfigCatTypeLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskConfigCatTypeLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskIDAndDetails;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.facade.fact.BDMCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMAttachmentIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMCreateModifyFA;
import curam.ca.gc.bdm.facade.fec.struct.BDMDeleteFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAApplicationType;
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
import curam.ca.gc.bdm.facade.fec.struct.BDMForeignOffice;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfFAApplicationTypes;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfOffices;
import curam.ca.gc.bdm.facade.fec.struct.BDMModifyFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMReadFECaseDetails;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMListICAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMCreateCaseAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMModifyCaseAttachmentDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadCaseAttachmentOut;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMFEC;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainAttachmentFactory;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainAttachmentTaskFactory;
import curam.ca.gc.bdm.sl.fec.struct.BDMFAInterimCodeDetails;
import curam.ca.gc.bdm.sl.fec.struct.BDMFAInterimCodeDetailsList;
import curam.ca.gc.bdm.sl.fec.struct.BDMFECTaskCreateDetails;
import curam.ca.gc.bdm.sl.organization.fact.BDMFECaseOwnershipStrategyFactory;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.BIZOBJASSOCIATION;
import curam.codetable.CASECATEGORY;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CASETYPECODE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.fact.ExternalPartyFactory;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.intf.ExternalParty;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.struct.CreateIntegratedCaseDetails1;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.facade.struct.ListMemberDetailsKey;
import curam.core.facade.struct.ReadCaseAttachmentForModifyKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.UniqueID;
import curam.core.intf.Users;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.intf.TaskAssignment;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.entity.struct.TaskAssignmentDtlsList;
import curam.core.sl.fact.ExternalPartyOfficeFactory;
import curam.core.sl.impl.Constants;
import curam.core.sl.impl.NotificationWDOStruct;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.impl.TaskDefinitionIDConst;
import curam.core.sl.intf.ExternalPartyOffice;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.ExternalPartyOfficeList;
import curam.core.struct.AddressKey;
import curam.core.struct.AttachmentIDAndAttachmentLinkIDStruct;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleAlternateIDKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ReadCaseAttachmentIn;
import curam.core.struct.UniqueIDKeySet;
import curam.core.struct.UserNameKey;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.events.TASK;
import curam.pdc.impl.PDCConst;
import curam.serviceplans.facade.struct.ServicePlanSecurityKey;
import curam.serviceplans.sl.impl.ServicePlanSecurity;
import curam.serviceplans.sl.impl.ServicePlanSecurityImplementationFactory;
import curam.supervisor.facade.struct.ReserveTaskDetailsForUser;
import curam.supervisor.sl.fact.MaintainSupervisorTasksFactory;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.administration.struct.CodeTableItemDetailsList;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.TaskKey;
import curam.wizard.util.impl.CodetableUtil;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.util.ArrayList;
import java.util.List;

public class BDMMaintainForeignEngagementCase
  extends curam.ca.gc.bdm.sl.fec.base.BDMMaintainForeignEngagementCase {

  private final String kOnStr = "on";

  private final String kPDF = "pdf";

  private final BDMUtil bdmUtil = new BDMUtil();

  /**
   * This BPO is used for reading the foreign engagement case details.
   */
  @Override
  public CreateIntegratedCaseResultAndMessages
    createFEIntegratedCase(final BDMFECaseDetails details)
      throws AppException, InformationalException {

    final CreateIntegratedCaseResultAndMessages caseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();

    final CreateIntegratedCaseDetails1 createIntegratedCaseDetails1 =
      new CreateIntegratedCaseDetails1();

    createIntegratedCaseDetails1.primaryClientID = details.concernRoleID;

    createIntegratedCaseDetails1.integratedCaseType =
      PRODUCTCATEGORY.BDM_IC_FEC_PROGRAM;

    // Task 71698: Hide "Priority" field from New Foreign Engagement Case
    // screen and pass value in the background
    createIntegratedCaseDetails1.priorityCode = CASEPRIORITY.HIGH;

    CreateIntegratedCaseResult createIntegratedCaseResult =
      new CreateIntegratedCaseResult();

    createIntegratedCaseResult =
      integratedCase.createIntegratedCase1(createIntegratedCaseDetails1);

    caseResultAndMessages.createCaseResult.assign(createIntegratedCaseResult);

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();
    final BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();
    bdmfeCaseDtls.caseID =
      caseResultAndMessages.createCaseResult.integratedCaseID;
    bdmfeCaseDtls.countryCode = details.countryCode;

    // Task 73663: DEV: Automatically link SSA when Country is Modified on
    // Foreign Engagement Case
    final long ssaExternalPartyConcernRoleID =
      checkIfSSACountryAvaialable(details.countryCode);
    bdmfeCaseDtls.ssaCountryID = ssaExternalPartyConcernRoleID;

    bdmfeCase.insert(bdmfeCaseDtls);

    // TASK 54344, 89066 Manage Allocation - R1 - Start
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = createIntegratedCaseResult.integratedCaseID;
    BDMFECaseOwnershipStrategyFactory.newInstance().assignCase(caseIDKey);
    // TASK 54344, 89066 Manage Allocation - R1 - End

    return caseResultAndMessages;
  }

  /**
   * This utility method will check, if there is a SSACountry available, then
   * return its concern role ID.
   * Task 73663: DEV: Automatically link SSA when Country is Modified on Foreign
   * Engagement Case
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private long checkIfSSACountryAvaialable(final String countryCode)
    throws AppException, InformationalException {

    long ssaExternalPartyConcernRoleID = 0;

    final BDMExternalParty externalPartyObj =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    if (!StringUtil.isNullOrEmpty(countryCode)) {

      final BDMExtPartyTypeAndAddrKey typeAndStatusKey =
        new BDMExtPartyTypeAndAddrKey();
      typeAndStatusKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;
      typeAndStatusKey.addrEleType = ADDRESSELEMENTTYPE.COUNTRY;
      typeAndStatusKey.addrEleValue = countryCode;

      final BDMExtPartyNameDetailsList extPartyDetailsList =
        externalPartyObj.searchExtPartyByTypeAndAddrEle(typeAndStatusKey);

      if (extPartyDetailsList.dtls.size() > 0) {
        ssaExternalPartyConcernRoleID =
          extPartyDetailsList.dtls.item(0).concernRoleID;
      } else if (ssaExternalPartyConcernRoleID == CuramConst.gkZero) {

        final String countryName = CodeTable
          .getOneItemForUserLocale(BDMSOURCECOUNTRY.TABLENAME, countryCode);

        final BDMExtPartyNameAndTypeKey bdmExtPartyNameAndTypeKey =
          new BDMExtPartyNameAndTypeKey();
        bdmExtPartyNameAndTypeKey.name = countryName.toUpperCase();
        bdmExtPartyNameAndTypeKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;
        final BDMExternalPartyDetailsList bdmExternalPartyDetailsList =
          externalPartyObj
            .searchExternalPartiesByNameAndType(bdmExtPartyNameAndTypeKey);

        if (bdmExternalPartyDetailsList.dtls.size() > CuramConst.gkZero) {
          ssaExternalPartyConcernRoleID =
            bdmExternalPartyDetailsList.dtls.item(0).concernRoleID;

        }
      }
    }
    return ssaExternalPartyConcernRoleID;
  }

  /**
   * This BPO is used for reading the foreign engagement case details.
   */
  @Override
  public BDMReadFECaseDetails readFEIntegratedCase(
    final IntegratedCaseIDKey key) throws curam.util.exception.AppException,
    curam.util.exception.InformationalException {

    final BDMReadFECaseDetails bdmReadFECaseDetails =
      new BDMReadFECaseDetails();

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = key.caseID;
    try {
      bdmReadFECaseDetails.countryCode =
        bdmfeCase.read(bdmfeCaseKey).countryCode;
    } catch (final RecordNotFoundException rnfe) {
      bdmReadFECaseDetails.countryCode = CuramConst.gkEmpty;
    }

    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();
    bdmReadFECaseDetails.readICDetails = integratedCase.readCaseDetails1(key);

    return bdmReadFECaseDetails;

  }

  /**
   * This BPO is used for modifying the foreign engagement case details.
   */
  @Override
  public void modifyFEIntegratedCase(final BDMModifyFECaseDetails details)
    throws curam.util.exception.AppException,
    curam.util.exception.InformationalException {

    boolean recodNotFoundInd = false;
    long ssaExternalPartyConcernRoleID = 0;
    if (details.modifyICDetails.caseID != CuramConst.gkZero) {

      final IntegratedCase integratedCase =
        IntegratedCaseFactory.newInstance();
      integratedCase.modifyCaseDetails(details.modifyICDetails);

      final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
        BDMFECaseFactory.newInstance();
      final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
      BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();
      final BDMFECaseDtls bdmfeCaseDtls1 = new BDMFECaseDtls();

      bdmfeCaseKey.caseID = details.modifyICDetails.caseID;
      try {
        bdmfeCaseDtls = bdmfeCase.read(bdmfeCaseKey);
      } catch (final RecordNotFoundException rnfe) {
        recodNotFoundInd = true;
        bdmfeCaseDtls.countryCode = details.countryCode;
        bdmfeCaseDtls.caseID = details.modifyICDetails.caseID;
      }
      // Task 73663: DEV: Automatically link SSA when Country is Modified on
      // Foreign Engagement Case
      if (!StringUtil.isNullOrEmpty(details.countryCode)) {
        ssaExternalPartyConcernRoleID =
          checkIfSSACountryAvaialable(details.countryCode);
      }

      if (!bdmfeCaseDtls.countryCode.equals(details.countryCode)) {
        bdmfeCaseDtls1.assign(bdmfeCaseDtls);
        bdmfeCaseDtls1.countryCode = details.countryCode;
        // Task 73663: DEV: Automatically link SSA when Country is Modified on
        // Foreign Engagement Case
        bdmfeCaseDtls1.ssaCountryID = ssaExternalPartyConcernRoleID;
        bdmfeCase.modify(bdmfeCaseKey, bdmfeCaseDtls1);
      } else if (recodNotFoundInd
        && bdmfeCaseDtls.countryCode.equals(details.countryCode)) {
        bdmfeCaseDtls1.assign(bdmfeCaseDtls);
        bdmfeCaseDtls1.countryCode = details.countryCode;
        // Task 73663: DEV: Automatically link SSA when Country is Modified on
        // Foreign Engagement Case
        bdmfeCaseDtls1.ssaCountryID = ssaExternalPartyConcernRoleID;
        bdmfeCase.insert(bdmfeCaseDtls1);
      }
    }

    // START Bug 111157
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = details.modifyICDetails.caseID;
    BDMFECaseOwnershipStrategyFactory.newInstance().assignCase(caseIDKey);
    // END Bug 111157

  }

  /**
   * This BPO is used for fetching details of the foreign engagement case
   * details for the Home Page.
   */
  @Override
  public BDMFECReadICDetails
    readFECDetailsForHome(final IntegratedCaseIDKey key)
      throws AppException, InformationalException {

    final BDMFECReadICDetails bdmfecReadICDetails = new BDMFECReadICDetails();

    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();

    bdmfecReadICDetails.readFEICDetails =
      integratedCase.readCaseDetails2(key);

    // BEGIN: Bug 70085: Country not displayed in type field of Foreign
    // Engagement Case
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = key.caseID;

    BDMFECaseDtls feCaseDtls = null;
    try {
      feCaseDtls = BDMFECaseFactory.newInstance().read(bdmfeCaseKey);
      bdmfecReadICDetails.countryCode = feCaseDtls.countryCode;
    } catch (final RecordNotFoundException rnfe) {
      bdmfecReadICDetails.countryCode = CuramConst.gkEmpty;
    }

    // END: Bug 70085: Country not displayed in type field of Foreign Engagement
    // Case

    // BEGIN FEATURE 52455 Manage SSA Countries - R1
    if (feCaseDtls != null
      && !StringUtil.isNullOrEmpty(feCaseDtls.countryCode)) {
      final String countryName = CodeTable.getOneItemForUserLocale(
        BDMSOURCECOUNTRY.TABLENAME, bdmfecReadICDetails.countryCode);

      bdmfecReadICDetails.countryCodeDesc = countryName;

      // If SSA Country exists then build country code description for SSA
      // Country.
      if (feCaseDtls.ssaCountryID != 0) {
        bdmfecReadICDetails.displayCountryLinkInd = true;
        bdmfecReadICDetails.extPartyCRoleID = feCaseDtls.ssaCountryID;
        final String countryCodeDesc =
          this.buildConuntrCodeDescription(countryName);
        bdmfecReadICDetails.countryCodeDesc = countryCodeDesc;
      } else {
        // It means FECase created after the SSACountry External party created.
        // Current FECase design doesen't automatically links external party
        // with FECase.
        final BDMExternalParty externalPartyObj =
          curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

        final BDMExtPartyTypeAndAddrKey typeAndStatusKey =
          new BDMExtPartyTypeAndAddrKey();
        typeAndStatusKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;
        typeAndStatusKey.addrEleType = ADDRESSELEMENTTYPE.COUNTRY;
        typeAndStatusKey.addrEleValue = bdmfecReadICDetails.countryCode;

        final BDMExtPartyNameDetailsList extPartyDetailsList =
          externalPartyObj.searchExtPartyByTypeAndAddrEle(typeAndStatusKey);

        if (extPartyDetailsList.dtls.size() > 0) {
          // It means it is a SSA Country
          bdmfecReadICDetails.displayCountryLinkInd = true;
          bdmfecReadICDetails.extPartyCRoleID =
            extPartyDetailsList.dtls.item(0).concernRoleID;
          final String countryCodeDesc =
            this.buildConuntrCodeDescription(countryName);
          bdmfecReadICDetails.countryCodeDesc = countryCodeDesc;
        }
      }
    }
    // END FEATURE 52455 Manage SSA Countries - R1

    return bdmfecReadICDetails;
  }

  /**
   * Method to build country code description.
   *
   * @param External party SSA country details.
   *
   * @return void
   */
  private String buildConuntrCodeDescription(final String countryName) {

    final StringBuilder countryDesc = new StringBuilder();
    countryDesc.append(BDMConstants.kView);
    countryDesc.append(Constants.kSpace);
    countryDesc.append(countryName);
    countryDesc.append(Constants.kSpace);
    countryDesc.append(BDMConstants.kDetails);
    return countryDesc.toString();
  }

  /**
   * This BPO is used for creating the foreign application.
   */
  @Override
  public curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey
    createForeignApplication(final BDMCreateModifyFA details)
      throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
      new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();

    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    final BDMForeignApplicationDtls bdmfaDtls =
      new BDMForeignApplicationDtls();

    details.status = BDMFOREIGNAPPSTATUS.OPEN;
    bdmfaDtls.assign(details);

    // Unique id generator class
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    bdmfaDtls.fApplicationID = uniqueIDObj.getNextID();
    bdmfaDtls.recordStatus = RECORDSTATUS.NORMAL;

    if (details.consent.equalsIgnoreCase(BDMYESNO.NO)
      && details.countryCode.equalsIgnoreCase(BDMSOURCECOUNTRY.US)
      && details.typeCode
        .equalsIgnoreCase(BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM)) {

      bdmfaDtls.status = BDMFOREIGNAPPSTATUS.COMPLETED;

      /*
       * Close existing open task generated for the communication of type
       * "Interim Application - Request" - when a foreign application of type
       * Interim, for country "United States", and consent selected as "No"
       *
       * Bug 100716: TASK-13: Not closing after US Interim Application with
       * client
       * consent 'No' is created
       */
      bdmUtil.findandCloseTasksForCommunicationOfTypeInterimRequest(
        bdmfaDtls.caseID);

    }

    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMFA;
    bdmfaDtls.faReferenceNumber = String.valueOf(
      UniqueIDFactory.newInstance().getNextIDFromKeySet(uniqueIDKeySet));

    if (details.externalPartyOfficeID != CuramConst.gkZero) {
      bdmfaDtls.fOfficeID = details.externalPartyOfficeID;
    }
    bdmfa.insert(bdmfaDtls);

    bdmfaKey.fApplicationID = bdmfaDtls.fApplicationID;

    return bdmfaKey;
  }

  /**
   * This BPO is used for modifying/editing the foreign application.
   */
  @Override
  public void modifyForeignApplication(final BDMCreateModifyFA details)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    BDMForeignApplicationDtls bdmfaDtls = new BDMForeignApplicationDtls();
    final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
      new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
    bdmfaKey.fApplicationID = details.fApplicationID;

    bdmfaDtls = bdmfa.read(bdmfaKey);
    bdmfaDtls.assign(bdmfaDtls);
    bdmfaDtls.comments = details.comments;
    bdmfaDtls.typeCode = details.typeCode;
    bdmfaDtls.bessInd = details.bessInd;
    bdmfaDtls.fIdentifier = details.fIdentifier;
    bdmfaDtls.receiveDate = details.receiveDate;
    bdmfaDtls.fOfficeID = details.externalPartyOfficeID;
    if (details.typeCode
      .equalsIgnoreCase(BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM)) {
      bdmfaDtls.interimChkStrList = details.interimChkStrList;
      bdmfaDtls.interimOther = details.interimOther;
      bdmfaDtls.consent = details.consent;
    } else {
      bdmfaDtls.interimChkStrList = BDMConstants.EMPTY_STRING;
      bdmfaDtls.interimOther = BDMConstants.EMPTY_STRING;
      bdmfaDtls.consent = BDMConstants.EMPTY_STRING;
    }

    bdmfa.modify(bdmfaKey, bdmfaDtls);

  }

  /**
   * This BPO is used for reading the foreign application details.
   */
  @Override
  public BDMFADetails readForeignApplication(final BDMFAKey key)
    throws AppException, InformationalException {

    final BDMFADetails bdmfaDetails = new BDMFADetails();

    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
      new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
    bdmfaKey.fApplicationID = key.fApplicationID;

    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);

    bdmfaDetails.assign(bdmfaDtls);
    bdmfaDetails.externalPartyOfficeID = bdmfaDtls.fOfficeID;

    return bdmfaDetails;
  }

  /**
   * This BPO is used for getting a list of foreign applications on a foreign
   * engagement case.
   */
  @Override
  public BDMFAList listForeignApplications(final BDMCaseKey key)
    throws AppException, InformationalException {

    final BDMFAList bdmfaList = new BDMFAList();

    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    BDMForeignApplicationDtlsList bdmfaDtlsList =
      new BDMForeignApplicationDtlsList();

    final BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();

    bdmfaKeyStruct3.caseID = key.caseID;

    bdmfaDtlsList = bdmfa.readByCaseID(bdmfaKeyStruct3);

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
    caseHeaderKey.caseID = key.caseID;
    caseHeaderDtls =
      curam.core.fact.CaseHeaderFactory.newInstance().read(caseHeaderKey);

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();
    bdmfeCaseKey.caseID = key.caseID;

    try {
      bdmfeCaseDtls = bdmfeCase.read(bdmfeCaseKey);
    } catch (final RecordNotFoundException rnfe) {

      bdmfeCaseDtls = new BDMFECaseDtls();
    }

    final int listSize = bdmfaDtlsList.dtls.size();
    BDMForeignApplicationDtls bdmfaDtls = new BDMForeignApplicationDtls();
    BDMFADetails bdmfaDetails = new BDMFADetails();
    AddressKey addressKey = new AddressKey();

    for (int i = 0; i < listSize; i++) {

      bdmfaDtls = new BDMForeignApplicationDtls();
      bdmfaDetails = new BDMFADetails();
      addressKey = new AddressKey();

      bdmfaDtls = bdmfaDtlsList.dtls.get(i);
      bdmfaDetails.assign(bdmfaDtls);

      final ExternalPartyOffice externalPartyOffice =
        ExternalPartyOfficeFactory.newInstance();
      final ExternalPartyOfficeKey externalPartyOfficeKey =
        new ExternalPartyOfficeKey();
      ExternalPartyOfficeDtls externalPartyOfficeDtls =
        new ExternalPartyOfficeDtls();

      if (bdmfaDetails.fOfficeID != CuramConst.gkZero) {
        externalPartyOfficeKey.externalPartyOfficeID = bdmfaDetails.fOfficeID;
        externalPartyOfficeDtls =
          externalPartyOffice.readExternalPartyOffice(externalPartyOfficeKey);

        addressKey.addressID = externalPartyOfficeDtls.primaryAddressID;

        if (addressKey.addressID != CuramConst.kLongZero) {
          bdmfaDetails.foreignOfficeAddressData =
            bdmUtil.getFormattedAddress(addressKey);
        }

      }

      bdmfaDetails.lastUpdatedOnAndByStr = constructLastUpdatedByAndOn(
        bdmfaDetails.lastUpdatedBy, bdmfaDetails.lastUpdatedOn.toString());

      bdmfaDetails.faApplTypeAndRefNumStr =
        CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
          bdmfaDetails.typeCode) + BDMConstants.kHiphen
          + bdmfaDetails.faReferenceNumber;

      bdmfaList.bdmFADetails.addRef(bdmfaDetails);

    }
    bdmfaList.concernroleID = caseHeaderDtls.concernRoleID;
    bdmfaList.countryCode = bdmfeCaseDtls.countryCode;

    return bdmfaList;
  }

  /**
   * This BPO is used for deleting/canceling a foreign application.
   */
  @Override
  public BDMFADetails viewForeignApplication(final BDMFAKey key)
    throws AppException, InformationalException {

    BDMFADetails bdmfaDetails = new BDMFADetails();
    final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
      new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();

    BDMForeignApplicationDtls bdmfaDtls = new BDMForeignApplicationDtls();
    bdmfaKey.fApplicationID = key.fApplicationID;
    bdmfaDtls = BDMForeignApplicationFactory.newInstance().read(bdmfaKey);
    bdmfaDetails.assign(bdmfaDtls);

    final ConcernRoleAlternateIDKey concernRoleAlternateIDKey =
      new ConcernRoleAlternateIDKey();
    ConcernRoleAlternateIDDtls concernRoleAlternateIDDtls =
      new ConcernRoleAlternateIDDtls();

    if (bdmfaDetails.fIdentifier != CuramConst.gkZero) {
      concernRoleAlternateIDKey.concernRoleAlternateID =
        bdmfaDetails.fIdentifier;

      concernRoleAlternateIDDtls = ConcernRoleAlternateIDFactory.newInstance()
        .read(concernRoleAlternateIDKey);
      bdmfaDetails.fIdentifierAlternateID =
        concernRoleAlternateIDDtls.alternateID;

    }

    final AddressKey addressKey = new AddressKey();

    final ExternalPartyOffice externalPartyOffice =
      ExternalPartyOfficeFactory.newInstance();
    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    ExternalPartyOfficeDtls externalPartyOfficeDtls =
      new ExternalPartyOfficeDtls();

    if (bdmfaDetails.fOfficeID != CuramConst.gkZero) {
      externalPartyOfficeKey.externalPartyOfficeID = bdmfaDetails.fOfficeID;

      externalPartyOfficeDtls =
        externalPartyOffice.readExternalPartyOffice(externalPartyOfficeKey);

      addressKey.addressID = externalPartyOfficeDtls.primaryAddressID;

      if (addressKey.addressID != CuramConst.kLongZero) {
        bdmfaDetails.foreignOfficeAddressData =
          bdmUtil.getFormattedAddress(addressKey);
      }
    }

    if (bdmfaDetails.bessInd) {
      bdmfaDetails.bessIndStr = BDMConstants.gkBESSIndYes;
    } else {
      bdmfaDetails.bessIndStr = BDMConstants.gkBESSIndNo;
    }

    if (bdmfaDetails.consent.equalsIgnoreCase(BDMYESNO.YES)) {
      bdmfaDetails.consentStr = BDMConstants.gkBESSIndYes;
    } else if (bdmfaDetails.consent.equalsIgnoreCase(BDMYESNO.NO)) {
      bdmfaDetails.consentStr = BDMConstants.gkBESSIndNo;
    } else {
      bdmfaDetails.consentStr = BDMConstants.EMPTY_STRING;
    }

    if (!StringUtil.isNullOrEmpty(bdmfaDetails.interimChkStrList)) {

      bdmfaDetails = buildCheckListStrings(bdmfaDetails.interimChkStrList,
        bdmfaDetails.interimOther, bdmfaDetails);

    }

    return bdmfaDetails;
  }

  /**
   * This is an utility method, which will build the check list responses as:
   * Yes/No.
   *
   * @param interimChkStrList
   * @param lisnChkLstOthrDesc
   * @param bdmfaDetails
   * @return
   */
  private BDMFADetails buildCheckListStrings(final String interimChkStrList,
    final String lisnChkLstOthrDesc, final BDMFADetails bdmfaDetails) {

    bdmfaDetails.chkRetirementStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    bdmfaDetails.chkDisabilityStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    bdmfaDetails.chkSurvivorStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    bdmfaDetails.chkOtherStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);

    final String strCheckListCodesArr[] =
      interimChkStrList.trim().split(CuramConst.gkTabDelimiter);

    String checkListCodeTableCode = CuramConst.gkEmpty;

    for (int i = 0; i < strCheckListCodesArr.length; i++) {
      checkListCodeTableCode = strCheckListCodesArr[i].toString();

      if (checkListCodeTableCode
        .equals(BDMFOREIGNAPPINTERIM.RETIREMENT_OR_OLDAGE)) {
        bdmfaDetails.chkRetirementStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }
      if (checkListCodeTableCode.equals(BDMFOREIGNAPPINTERIM.DISABILITY)) {
        bdmfaDetails.chkDisabilityStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }
      if (checkListCodeTableCode.equals(BDMFOREIGNAPPINTERIM.SURVIVORS)) {
        bdmfaDetails.chkSurvivorStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }
      if (checkListCodeTableCode.equals(BDMFOREIGNAPPINTERIM.OTHER)) {
        bdmfaDetails.chkOtherStr = lisnChkLstOthrDesc;
      }
    }

    return bdmfaDetails;

  }

  /**
   * This BPO is used for getting the foreign application history records.
   */
  @Override
  public BDMFAList listFAHistory(final BDMFAKey key)
    throws AppException, InformationalException {

    final BDMFAList bdmfaList = new BDMFAList();

    final curam.ca.gc.bdm.entity.intf.BDMFAHistory bdmfaHistory =
      BDMFAHistoryFactory.newInstance();

    BDMFAHistoryDtlsList bdmfaHistoryDtlsList = new BDMFAHistoryDtlsList();

    final BDMFAHistoryKeyStruct1 bdmfaHistoryKeyStruct1 =
      new BDMFAHistoryKeyStruct1();
    bdmfaHistoryKeyStruct1.fApplicationID = key.fApplicationID;

    bdmfaHistoryDtlsList =
      bdmfaHistory.readByForeignApplicationID(bdmfaHistoryKeyStruct1);

    final int listSize = bdmfaHistoryDtlsList.dtls.size();
    BDMFAHistoryDtls bdmfaHistoryDtls = new BDMFAHistoryDtls();
    BDMFADetails bdmfaDetails = new BDMFADetails();

    if (listSize > CuramConst.gkZero) {

      for (int i = 0; i < listSize; i++) {
        bdmfaHistoryDtls = new BDMFAHistoryDtls();
        bdmfaDetails = new BDMFADetails();
        bdmfaHistoryDtls = bdmfaHistoryDtlsList.dtls.get(i);
        bdmfaDetails.assign(bdmfaHistoryDtls);

        bdmfaDetails.lastUpdatedOnAndByStr =
          constructLastUpdatedByAndOn(bdmfaHistoryDtls.lastUpdatedBy,
            bdmfaDetails.lastUpdatedOn.toString());

        bdmfaDetails.faApplTypeAndRefNumStr =
          CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
            bdmfaDetails.typeCode) + BDMConstants.kHiphen
            + bdmfaDetails.faReferenceNumber;

        bdmfaList.bdmFADetails.addRef(bdmfaDetails);
      }
    }

    return bdmfaList;
  }

  /**
   * This is a utility method to construct a string combined of last updated by
   * and
   * last updated on fields.
   *
   * @param lastUpdatedBy
   * @param lastUpdatedOn
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String constructLastUpdatedByAndOn(final String lastUpdatedBy,
    final String lastUpdatedOn) throws AppException, InformationalException {

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = lastUpdatedBy;
    final UsersDtls usersDtls = UsersFactory.newInstance().read(usersKey);

    return usersDtls.fullName + CuramConst.gkSpace + kOnStr
      + CuramConst.gkSpace + lastUpdatedOn;
  }

  /**
   * This BPO is used for deleting/canceling a foreign application.
   */
  @Override
  public void deleteForeignApplication(final BDMDeleteFADetails details)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
      new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
    bdmfaKey.fApplicationID = details.fApplicationID;

    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);
    bdmfaDtls.recordStatus = RECORDSTATUS.CANCELLED;
    bdmfaDtls.faDeleteReason = details.bdmDeleteFAReason;
    bdmfa.modify(bdmfaKey, bdmfaDtls);

    final curam.ca.gc.bdm.entity.intf.BDMFAHistory bdmfaHistory =
      BDMFAHistoryFactory.newInstance();

    final BDMFAHistoryDtls bdmfaHistoryDtls = new BDMFAHistoryDtls();
    bdmfaHistoryDtls.assign(bdmfaDtls);

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    bdmfaHistoryDtls.fAppHistoryID = uniqueIDObj.getNextID();

    bdmfaHistory.insert(bdmfaHistoryDtls);

  }

  /**
   * This BPO is used for creating the foreign engagement case attachment
   * details.
   */
  @Override
  public BDMAttachmentIDs
    createFECaseAttachment(final BDMFAAttachmentDetails details)
      throws AppException, InformationalException {

    BDMAttachmentIDs bdmAttachmentIDs = new BDMAttachmentIDs();

    BDMCreateCaseAttachmentDetails bdmCreateCaseAttachmentDetails = null;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    BDMFAAttachmentDetails bdmfaAttachmentDetails2 = null;

    if (details.actionIDProperty.equals(BDMConstants.kSaveAndClose)) {
      bdmCreateCaseAttachmentDetails = new BDMCreateCaseAttachmentDetails();
      bdmCreateCaseAttachmentDetails
        .assign(details.bdmCreateAttachmentDetails);

      validateAttachmentIsForeignApplication(details);
      bdmAttachmentIDs = createCaseAttachment(bdmCreateCaseAttachmentDetails,
        details.selectedFAList, details.actionIDProperty);
      bdmAttachmentIDs.wizardStateID = details.wizardStateID;

      createAttachmentTaskForFBApplication(bdmCreateCaseAttachmentDetails,
        bdmAttachmentIDs);

    }

    if (details.actionIDProperty.equals(BDMConstants.kNext)) {

      bdmfaAttachmentDetails2 = (BDMFAAttachmentDetails) wizardPersistentState
        .read(details.wizardStateID);

      bdmfaAttachmentDetails2.bdmCreateAttachmentDetails.dtls
        .assign(details.bdmCreateAttachmentDetails.dtls);
      bdmfaAttachmentDetails2.bdmCreateAttachmentDetails.fileSource =
        details.bdmCreateAttachmentDetails.fileSource;

      wizardPersistentState.modify(details.wizardStateID,
        bdmfaAttachmentDetails2);

      bdmAttachmentIDs.wizardStateID = details.wizardStateID;

    }

    return bdmAttachmentIDs;

  }

  private void createAttachmentTaskForFBApplication(
    final BDMCreateCaseAttachmentDetails bdmCreateCaseAttachmentDetails,
    final BDMAttachmentIDs bdmAttachmentIDs)
    throws AppException, InformationalException {

    BDMFAAttachmentNoticationTaskDetails bdmfaAttmntNotificationTakDetails =
      null;

    BDMForeignApplicationKey bdmForeignApplicationKey = null;
    BDMForeignApplicationDtls bdmForeignApplicationDtls = null;

    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();

    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();

    bdmfeCaseKey.caseID =
      bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

    bdmfeCaseDtls = BDMFECaseFactory.newInstance().read(bdmfeCaseKey);

    long fApplicationID = CuramConst.kLongZero;

    CaseHeaderDtls headerDtls = new CaseHeaderDtls();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final BDMCaseKey bdmCaseKey = new BDMCaseKey();
    bdmCaseKey.caseID =
      bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;
    final BDMFAList bdmFAList = listForeignApplications(bdmCaseKey);

    if (bdmAttachmentIDs.attachmentID != CuramConst.kLongZero
      && !bdmFAList.bdmFADetails.isEmpty()
      && (bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
        .equals(DOCUMENTTYPE.FOREIGN_APPLICATION)
        || bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
          .equals(DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS))) {

      for (final BDMFADetails bdmFADetails : bdmFAList.bdmFADetails) {

        fApplicationID = bdmFADetails.fApplicationID;

        bdmForeignApplicationKey = new BDMForeignApplicationKey();
        bdmForeignApplicationKey.fApplicationID = fApplicationID;

        bdmForeignApplicationDtls = new BDMForeignApplicationDtls();

        bdmForeignApplicationDtls = BDMForeignApplicationFactory.newInstance()
          .read(bdmForeignApplicationKey);

        bdmfaAttmntNotificationTakDetails =
          new BDMFAAttachmentNoticationTaskDetails();

        bdmfaAttmntNotificationTakDetails.countryCode =
          bdmfeCaseDtls.countryCode;

        bdmfaAttmntNotificationTakDetails.faReferenceNumber =
          bdmForeignApplicationDtls.faReferenceNumber;

        bdmfaAttmntNotificationTakDetails.fApplicationID = fApplicationID;

        bdmfaAttmntNotificationTakDetails.faTypeCode =
          bdmForeignApplicationDtls.typeCode;

        bdmfaAttmntNotificationTakDetails.caseID =
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

        bdmfaAttmntNotificationTakDetails.attachmentReceiptDate =
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate;

        bdmfaAttmntNotificationTakDetails.wdoName =
          BDMConstants.kBdmTaskCreateDetails;

        bdmfaAttmntNotificationTakDetails.documentType =
          bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType;

        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
        caseHeaderKey.caseID = bdmfaAttmntNotificationTakDetails.caseID;
        headerDtls =
          CaseHeaderFactory.newInstance().read(nfIndicator, caseHeaderKey);

        if (!nfIndicator.isNotFound()) {

          bdmfaAttmntNotificationTakDetails.concernRoleID =
            headerDtls.concernRoleID;

        }
        createFAAttachmentNotificationTask(bdmfaAttmntNotificationTakDetails);
      }

    }
  }

  /**
   * This private method is used for raising a task for a specific type of
   * attachment type linked to a foreign application on the foreign engagement
   * case.
   *
   * When the following Document type is attached to FEC and the attachement is
   * linked to the Foreign Application records
   * 1. Foreign Application - Additional Documents
   * 2. Foreign Application
   *
   * Deadline for the Task: 365 days from the Receipt date entered in the
   * attachment screen
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void createFAAttachmentNotificationTask(
    final BDMFAAttachmentNoticationTaskDetails bdmFaAttmntNotificationTaskDetails)
    throws AppException, InformationalException {

    Date attachmentReceiptDtForDeadLine = new Date();
    attachmentReceiptDtForDeadLine =
      bdmFaAttmntNotificationTaskDetails.attachmentReceiptDate;

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final List<Object> enactmentStructs = new ArrayList<>();
    final BDMFECTaskCreateDetails bfmFECTaskCrtDetails =
      new BDMFECTaskCreateDetails();

    bfmFECTaskCrtDetails.fApplicationID =
      bdmFaAttmntNotificationTaskDetails.fApplicationID;
    bfmFECTaskCrtDetails.faTypeCode =
      bdmFaAttmntNotificationTaskDetails.faTypeCode;
    bfmFECTaskCrtDetails.caseID = bdmFaAttmntNotificationTaskDetails.caseID;
    bfmFECTaskCrtDetails.concernRoleID =
      bdmFaAttmntNotificationTaskDetails.concernRoleID;
    bfmFECTaskCrtDetails.documentType =
      bdmFaAttmntNotificationTaskDetails.documentType;

    bfmFECTaskCrtDetails.priority = TASKPRIORITY.NORMAL;

    CaseHeaderDtls headerDtls = null;

    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();

    final curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();

    final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
      new BDMWorkQueueCountryLinkKey();

    final String currentLoggedInUser = TransactionInfo.getProgramUser();

    boolean fecCaseFoundInd = false;

    headerDtls = new CaseHeaderDtls();

    if (bdmFaAttmntNotificationTaskDetails.caseID != CuramConst.kLongZero) {

      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = bdmFaAttmntNotificationTaskDetails.caseID;
      headerDtls =
        CaseHeaderFactory.newInstance().read(nfIndicator, caseHeaderKey);

      if (!nfIndicator.isNotFound()
        && headerDtls.caseTypeCode.equals(CASECATEGORY.INTEGRATEDCASE)) {

        bdmfeCaseKey.caseID = headerDtls.caseID;

        bfmFECTaskCrtDetails.caseID = headerDtls.caseID;

        nfIndicator = new NotFoundIndicator();

        final BDMFECaseDtls feCaseDtls =
          bdmfeCase.read(nfIndicator, bdmfeCaseKey);

        if (!nfIndicator.isNotFound()) {

          bdmWorkQueueCountryLinkKey.countryCode = feCaseDtls.countryCode;

          nfIndicator = new NotFoundIndicator();

          final BDMWorkQueueCountryLinkDtls bdmWorkQueueCountryLinkDtls =
            bdmWorkQueueCountryLink.read(nfIndicator,
              bdmWorkQueueCountryLinkKey);

          if (!nfIndicator.isNotFound()) {

            bfmFECTaskCrtDetails.workQueueID =
              bdmWorkQueueCountryLinkDtls.workQueueID;

            fecCaseFoundInd = true;

          }

        }

      }
    }

    if (bfmFECTaskCrtDetails.workQueueID == CuramConst.kLongZero
      && !fecCaseFoundInd) {

      bfmFECTaskCrtDetails.workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);

    }

    // Received <receipt date><Client Oral Communication> <Client Written
    // Communication> Foreign Application <Applicaton type and reference number>
    // created Foreign Engagement Case <Case Reference> - <Country> for <First
    // name> <Last Name> <SIN or Person Reference ID> (Curam generated number)

    String orallanuage = CuramConst.gkEmpty;
    String writtenlanuage = CuramConst.gkEmpty;
    StringBuffer oralWrittenLanguage =
      new StringBuffer(BDMConstants.gkLocaleUpperEN);

    DynamicEvidenceDataAttributeDtlsList dynamicEvidenceDataAttributeDtlsList =
      new DynamicEvidenceDataAttributeDtlsList();

    final EvidenceDescriptorDtls evidenceDescriptorDtlsKey =
      new EvidenceDescriptorDtls();
    evidenceDescriptorDtlsKey.participantID =
      bfmFECTaskCrtDetails.concernRoleID;
    evidenceDescriptorDtlsKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    evidenceDescriptorDtlsKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    dynamicEvidenceDataAttributeDtlsList =
      bdmfeCase.getContactPreferencesEvidenceList(evidenceDescriptorDtlsKey);

    DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls = null;

    final int listSize = dynamicEvidenceDataAttributeDtlsList.dtls.size();

    if (listSize > 0) {

      for (int i = 0; i < listSize; i++) {
        dynamicEvidenceDataAttributeDtls =
          new DynamicEvidenceDataAttributeDtls();

        dynamicEvidenceDataAttributeDtls =
          dynamicEvidenceDataAttributeDtlsList.dtls.item(i);

        if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredOralLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            orallanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            orallanuage = BDMConstants.gkLocaleUpperFR;
          }

        } else if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredWrittenLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleUpperFR;
          }

        }

        if (!StringUtil.isNullOrEmpty(orallanuage)
          && !StringUtil.isNullOrEmpty(writtenlanuage)
          && orallanuage.equals(writtenlanuage)) {
          oralWrittenLanguage = new StringBuffer();
          oralWrittenLanguage.append(orallanuage);
        } else if (!StringUtil.isNullOrEmpty(orallanuage)
          && !StringUtil.isNullOrEmpty(writtenlanuage)
          && !orallanuage.equals(writtenlanuage)) {
          oralWrittenLanguage = new StringBuffer();
          oralWrittenLanguage.append(BDMConstants.gkBIL);

        } else {
          oralWrittenLanguage.append(BDMConstants.gkLocaleUpperEN);
        }

      }

    }

    final String foreignApplicationTypCdDesc =
      CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
        bdmFaAttmntNotificationTaskDetails.faTypeCode);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = bfmFECTaskCrtDetails.concernRoleID;

    final String strConcernRoleFullName =
      ConcernRoleFactory.newInstance().read(concernRoleKey).concernRoleName;

    final String fecTypeDesc = CodetableUtil.getCodetableDescription(
      PRODUCTCATEGORY.TABLENAME, headerDtls.integratedCaseType);

    final String coutryCodeDesc =
      CodetableUtil.getCodetableDescription(BDMSOURCECOUNTRY.TABLENAME,
        bdmFaAttmntNotificationTaskDetails.countryCode);

    String strIdentificationRefernce = CuramConst.gkEmpty;

    // Get person's reference number
    strIdentificationRefernce = bdmUtil.getPersonReferenceNumber(
      bdmFaAttmntNotificationTaskDetails.concernRoleID);

    // Get original person SIN Number
    final String personSINNumber =
      bdmUtil.getSINNumberForPerson(bfmFECTaskCrtDetails.concernRoleID);

    if (!StringUtil.isNullOrEmpty(personSINNumber)) {
      strIdentificationRefernce = CuramConst.gkEmpty;
      strIdentificationRefernce = personSINNumber;
    }

    bfmFECTaskCrtDetails.attachmentReceipt = attachmentReceiptDtForDeadLine;
    bfmFECTaskCrtDetails.oralWrittenLanguage = oralWrittenLanguage.toString();
    bfmFECTaskCrtDetails.foreignApplicationTypCdDesc =
      foreignApplicationTypCdDesc;
    bfmFECTaskCrtDetails.faReferenceNumber =
      bdmFaAttmntNotificationTaskDetails.faReferenceNumber;
    // Removed below line to fix: Bug 114287
    // bfmFECTaskCrtDetails.fecTypeDesc = fecTypeDesc;
    bfmFECTaskCrtDetails.feCaseReference = headerDtls.caseReference;
    bfmFECTaskCrtDetails.coutryCodeDesc = coutryCodeDesc;
    bfmFECTaskCrtDetails.strConcernRoleFullName = strConcernRoleFullName;
    bfmFECTaskCrtDetails.strIdentificationRefernce =
      strIdentificationRefernce;

    enactmentStructs.add(bfmFECTaskCrtDetails);

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();

    final int taskDeadline = Integer.parseInt(Configuration.getProperty(
      EnvVars.BDM_RECORD_FOREIGN_APPLICATION_ATTACHMENT_DEADLINE_NOOFDAYS));

    final long inputDateTimeInMills =
      attachmentReceiptDtForDeadLine.addDays(taskDeadline).asLong();

    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    // START: Bug 98073: INT Defect - Task Category and Type is not displayed
    // when
    // a system generated task is created (TASK-01 - Reference from Task
    // Sheet)

    // START: BUG 118183
    final BDMTask bdmTask = BDMTaskFactory.newInstance();
    final curam.core.sl.entity.struct.TaskLinkDtlsList taskLinkDtlsList =
      bdmTask.listOfTasksWhenAttachmentLinkedFId(
        bdmFaAttmntNotificationTaskDetails);

    if (taskLinkDtlsList.dtls.size() == 0) {
      // END: BUG 118183
      final long kProcessInstanceID =
        EnactmentService.startProcessInV3CompatibilityMode(
          BDMConstants.kBDMFBApplicationAttachmentNotificationTask,
          enactmentStructs);

      bdmUtil.addBDMTask(kProcessInstanceID,
        bfmFECTaskCrtDetails.documentType, bfmFECTaskCrtDetails.faTypeCode);
    } else {
      // START: BUG 118183 - Create Notification
      final List<NotificationWDOStruct> notificationEnactmentStructs =
        new ArrayList<NotificationWDOStruct>();
      final NotificationWDOStruct notificationWDOStruct =
        new NotificationWDOStruct();

      notificationWDOStruct.body = BDMConstants.EMPTY_STRING;
      notificationWDOStruct.subject = BDMConstants.kNotificationSubject_fr
        + BDMConstants.kForwardSlash + BDMConstants.kNotificationSubject_en
        + CodetableUtil.getNonCachedCodetableItem(BDMFOREIGNAPPTYPE.TABLENAME,
          bdmFaAttmntNotificationTaskDetails.faTypeCode,
          BDMConstants.gkLocaleLowerFR).description
        + BDMConstants.kForwardSlash
        + CodetableUtil.getNonCachedCodetableItem(BDMFOREIGNAPPTYPE.TABLENAME,
          bdmFaAttmntNotificationTaskDetails.faTypeCode,
          BDMConstants.gkLocaleLowerEN).description
        + BDMConstants.kHyphen + bfmFECTaskCrtDetails.faReferenceNumber;
      notificationWDOStruct.caseID =
        bdmFaAttmntNotificationTaskDetails.caseID;

      // Retrieve the users associated with this task.
      final curam.core.sl.entity.struct.TaskKey userTaskKey =
        new curam.core.sl.entity.struct.TaskKey();
      // Task Assignment entity.
      final TaskAssignment taskAssignmentObj =
        TaskAssignmentFactory.newInstance();

      userTaskKey.taskID = taskLinkDtlsList.dtls.get(0).taskID;
      final TaskAssignmentDtlsList userTaskDetailsList =
        taskAssignmentObj.searchAssignmentsByTaskID(userTaskKey);

      final UserNameKey userKey = new UserNameKey();
      userKey.userName = userTaskDetailsList.dtls.get(0).relatedName;

      if (userKey.userName != null && !userKey.userName.isEmpty()) {
        notificationWDOStruct.userName = userKey.userName;
        notificationEnactmentStructs.add(notificationWDOStruct);
        EnactmentService.startProcessInV3CompatibilityMode(
          TaskDefinitionIDConst.defaultCaseNotificationTaskDefinitionID,
          notificationEnactmentStructs);
      }
      // END: BUG 118183 - Create Notification
    }
    // END: Bug 98073: INT Defect - Task Category and Type is not displayed when
    // a system generated task is created (TASK-01 - Reference from Task Sheet)

  }

  /**
   * Create a record in BDMTask table.
   *
   * @param fApplicationID
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void createBDMTaskRecord(final BDMTaskIDAndDetails key)
    throws AppException, InformationalException {

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final curam.ca.gc.bdm.entity.intf.BDMTask bdmTaskObj =
      BDMTaskFactory.newInstance();

    final BDMTaskDtls bdmTaskDtls = new BDMTaskDtls();

    bdmTaskDtls.taskID = key.taskID;

    final BDMTaskConfigCatTypeLink bdmTaskConfigCatTypeLink =
      BDMTaskConfigCatTypeLinkFactory.newInstance();

    final BDMTaskConfigCatTypeLinkKey bdmTaskConfigCatTypeLinkKey =
      new BDMTaskConfigCatTypeLinkKey();

    bdmTaskConfigCatTypeLinkKey.configCdType = key.documentType;

    final BDMTaskConfigCatTypeLinkDtls bdmTaskConfigCatTypeLinkDtls =
      bdmTaskConfigCatTypeLink.read(nfIndicator, bdmTaskConfigCatTypeLinkKey);

    if (!nfIndicator.isNotFound()) {

      bdmTaskDtls.category = bdmTaskConfigCatTypeLinkDtls.taskCategoryType;

      bdmTaskDtls.type = bdmTaskConfigCatTypeLinkDtls.taskType;

      bdmTaskObj.insert(bdmTaskDtls);
    }

  }

  /**
   * Create an attachment for a case.
   *
   * @param details
   * Case attachment details.
   */
  private BDMAttachmentIDs createCaseAttachment(
    final curam.ca.gc.bdm.facade.struct.BDMCreateCaseAttachmentDetails details,
    final String selectedFAList, final String actionIDProperty)
    throws AppException, InformationalException {

    final BDMAttachmentIDs bdmAttachmentIDs = new BDMAttachmentIDs();
    AttachmentIDAndAttachmentLinkIDStruct attachmentIDAndAttachmentLinkIDStruct =
      new AttachmentIDAndAttachmentLinkIDStruct();

    // MaintainAttachment manipulation variable
    final curam.ca.gc.bdm.sl.attachment.intf.BDMMaintainAttachment bdmmaintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    // Case Header manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();

    // set case key
    caseKey.caseID = details.dtls.createCaseAttachmentDetails.caseID;

    // read case type code
    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);

    // if case type is service plan, check service plan security
    if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.SERVICEPLAN)) {

      // ServicePlanDelivery facade
      final curam.serviceplans.facade.intf.ServicePlanDelivery servicePlanDeliveryObj =
        curam.serviceplans.facade.fact.ServicePlanDeliveryFactory
          .newInstance();
      final ServicePlanSecurityKey servicePlanSecurityKey =
        new ServicePlanSecurityKey();

      // register the service plan security implementation
      ServicePlanSecurityImplementationFactory.register();

      // set the key
      servicePlanSecurityKey.caseID = caseKey.caseID;
      servicePlanSecurityKey.securityCheckType =
        ServicePlanSecurity.kCreateSecurityCheck;

      // check security
      servicePlanDeliveryObj.checkSecurity(servicePlanSecurityKey);
    }

    // Create the case attachment
    attachmentIDAndAttachmentLinkIDStruct =
      bdmmaintainAttachmentObj.insertCaseAttachmentDetails(details);

    // Begin: link attachment with foreign application(s)
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    BDMFEAttachmentLinkDtls bdmfeAttachmentLinkDtls =
      new BDMFEAttachmentLinkDtls();

    final curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();

    // Unique id generator class
    long fApplicationID = 0;

    if (selectedFAList.length() > CuramConst.gkZero
      && actionIDProperty.equals(BDMConstants.kSave)) {

      final String strFAIdsArr[] =
        selectedFAList.trim().split(CuramConst.gkTabDelimiter);

      BDMForeignApplicationDtls bdmfaDtls = null;
      for (int i = 0; i < strFAIdsArr.length; i++) {
        bdmfeAttachmentLinkDtls = new BDMFEAttachmentLinkDtls();
        fApplicationID = Long.parseLong(strFAIdsArr[i].toString());
        bdmfeAttachmentLinkDtls.feAttachmentLinkID = uniqueIDObj.getNextID();
        bdmfeAttachmentLinkDtls.relatedID = fApplicationID;
        bdmfeAttachmentLinkDtls.attachmentDate = Date.getCurrentDate();
        bdmfeAttachmentLinkDtls.attachmentID =
          attachmentIDAndAttachmentLinkIDStruct.attachmentID;
        bdmfeAttachmentLinkDtls.description =
          details.dtls.createCaseAttachmentDetails.description;
        bdmfeAttachmentLinkDtls.recordStatus = RECORDSTATUS.NORMAL;
        bdmfeAttachmentLinkDtls.attachmentLinkTypCd =
          BDMATTACHMENTLINKTYPE.FAATTACHMENTLINK;

        bdmfeAttachmentLink.insert(bdmfeAttachmentLinkDtls);

        bdmAttachmentIDs.feAttachmentLinkID =
          bdmfeAttachmentLinkDtls.feAttachmentLinkID;

        final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
          new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();

        final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
          BDMForeignApplicationFactory.newInstance();
        bdmfaKey.fApplicationID = bdmfeAttachmentLinkDtls.relatedID;
        bdmfaDtls = bdmfa.read(bdmfaKey);

        bdmfa.modify(bdmfaKey, bdmfaDtls);

      }
      // End: link attachment with foreign application(s)

    }

    // START: Task 94070: DEV: Person Attachment Task Implementation
    BDMMaintainAttachmentTaskFactory.newInstance().createFECAttachmentTask(
      details, attachmentIDAndAttachmentLinkIDStruct);
    // END: Task 94070: DEV: Person Attachment Task Implementation

    bdmAttachmentIDs.caseAttachmentLinkID =
      attachmentIDAndAttachmentLinkIDStruct.caseAttachmentLinkID;

    bdmAttachmentIDs.attachmentID =
      attachmentIDAndAttachmentLinkIDStruct.attachmentID;

    // START: Bug 98268: INT Defect - The Foreign Application Status is not
    // changed to "In Progress" after the system generated task (TASK-06)
    // created
    if (!StringUtil.isNullOrEmpty(selectedFAList)
      && (bdmUtil.isAttachmentTaskVIRequired(
        details.dtls.createCaseAttachmentDetails.documentType)
        || bdmUtil.isAttachmentTaskIIRequired(
          details.dtls.createCaseAttachmentDetails.documentType))) {

      fApplicationID = CuramConst.kLongZero;

      BDMForeignApplicationKey bdmForeignApplicationKey = null;

      BDMForeignApplicationDtls bdmForeignApplicationDtls = null;

      if (selectedFAList.length() > CuramConst.gkZero) {

        final String strFAIdsArr[] =
          selectedFAList.trim().split(CuramConst.gkTabDelimiter);

        for (int i = 0; i < strFAIdsArr.length; i++) {

          fApplicationID = Long.parseLong(strFAIdsArr[i].toString());

          bdmForeignApplicationKey = new BDMForeignApplicationKey();
          bdmForeignApplicationKey.fApplicationID = fApplicationID;

          bdmForeignApplicationDtls = new BDMForeignApplicationDtls();

          bdmForeignApplicationDtls = BDMForeignApplicationFactory
            .newInstance().read(bdmForeignApplicationKey);

          bdmForeignApplicationDtls.status = BDMFOREIGNAPPSTATUS.IN_PROGRESS;

          BDMForeignApplicationFactory.newInstance()
            .modify(bdmForeignApplicationKey, bdmForeignApplicationDtls);

        }
      }

    }
    // END: Bug 98268: INT Defect - The Foreign Application Status is not
    // changed to "In Progress" after the system generated task (TASK-06)
    // created
    return bdmAttachmentIDs;
  }

  /**
   * @param details
   */
  private void validateAttachmentIsForeignApplication(
    final BDMFAAttachmentDetails details) throws InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (details.actionIDProperty.equals(BDMConstants.kSaveAndClose)) {
      if (!details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
        .isEmpty()
        && (details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
          .equalsIgnoreCase(DOCUMENTTYPE.FOREIGN_APPLICATION)
          || details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
            .equalsIgnoreCase(
              DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS))) {

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMFEC.ERR_CLICK_NEXT_TO_LINK_FA),
            CuramConst.gkEmpty,
            InformationalElement.InformationalType.kError);

      }
    }
    if (details.actionIDProperty.equals(BDMConstants.kSave)) {
      if (!details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
        .isEmpty()
        && (details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
          .equalsIgnoreCase(DOCUMENTTYPE.FOREIGN_APPLICATION)
          || details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
            .equalsIgnoreCase(
              DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS))
        && StringUtil.isNullOrEmpty(details.selectedFAList)) {

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMFEC.ERR_SELECT_FA_TO_LINK),
            CuramConst.gkEmpty,
            InformationalElement.InformationalType.kError);

      }

    }
    informationalManager.failOperation();
  }

  /**
   * This BPO is used for modifying the foreign engagement case attachment
   * details.
   */
  @Override
  public BDMAttachmentIDs modifyFECaseAttachment(
    final curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails details)
    throws AppException, InformationalException {

    BDMAttachmentIDs bdmAttachmentIDs = new BDMAttachmentIDs();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    BDMModifyCaseAttachmentDetails bdmModifyCaseAttachmentDetails = null;

    if (details.actionIDProperty.equals(BDMConstants.kSaveAndClose)) {

      bdmModifyCaseAttachmentDetails = new BDMModifyCaseAttachmentDetails();

      bdmModifyCaseAttachmentDetails
        .assign(details.bdmModifyAttachmentDetails);

      bdmAttachmentIDs = modifyCaseAttachment(details, details.selectedFAList,
        details.actionIDProperty);
    }

    if (details.actionIDProperty.equals(BDMConstants.kNext)) {
      bdmAttachmentIDs.wizardStateID = wizardPersistentState.create(details);

      bdmAttachmentIDs.wizardStateID = wizardPersistentState.create(details);

    }
    return bdmAttachmentIDs;

  }

  /**
   * Modify an attachment for a case.
   *
   * @param details
   * Case attachment details.
   */
  private BDMAttachmentIDs modifyCaseAttachment(
    final curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails details,
    final String selectedFAList, final String actionIDProperty)
    throws AppException, InformationalException {

    // MaintainAttachment manipulation variable
    final curam.ca.gc.bdm.sl.attachment.intf.BDMMaintainAttachment maintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    // Case Header manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();

    // set case key
    caseKey.caseID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseID;

    // read case type code
    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);

    // if case type is service plan, check service plan security
    if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.SERVICEPLAN)) {

      // ServicePlanDelivery facade
      final curam.serviceplans.facade.intf.ServicePlanDelivery servicePlanDeliveryObj =
        curam.serviceplans.facade.fact.ServicePlanDeliveryFactory
          .newInstance();
      final ServicePlanSecurityKey servicePlanSecurityKey =
        new ServicePlanSecurityKey();

      // register the service plan security implementation
      ServicePlanSecurityImplementationFactory.register();

      // set the key
      servicePlanSecurityKey.caseID = caseKey.caseID;
      servicePlanSecurityKey.securityCheckType =
        ServicePlanSecurity.kMaintainSecurityCheck;

      // check security
      servicePlanDeliveryObj.checkSecurity(servicePlanSecurityKey);
    }

    // START: first read the existing attachment details to compare the document
    // type for bug 118324 before saving the record
    final ReadCaseAttachmentIn attachmentIn = new ReadCaseAttachmentIn();

    attachmentIn.attachmentID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.attachmentID;
    attachmentIn.caseAttachmentLinkID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseAttachmentLinkID;

    final BDMReadCaseAttachmentOut attachmentOut =
      maintainAttachmentObj.readCaseAttachment(attachmentIn);
    // END: first read the existing attachment details to compare the document
    // type for bug 118324

    // Modify the Case Attachment
    maintainAttachmentObj
      .modifyCaseAttachment(details.bdmModifyAttachmentDetails);

    // START: BUG 118324
    // close the existing task if documentType is changed
    if (!attachmentOut.dtls.documentType.equalsIgnoreCase(
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.documentType)) {

      final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
      BDMConcernRoleCommRMKey bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
      bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
      bdmCncrCmmRmKey.wdoName = BDMConstants.kBdmTaskCreateDetails;
      bdmCncrCmmRmKey.caseID =
        details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseID;
      bdmCncrCmmRmKey.concernRoleID =
        details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseParticipantRoleID;
      bdmCncrCmmRmKey.communicationTypeCode =
        details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.documentType;

      final BDMRecordCommunicationsTaskDetailsList bdmRecordCommunicationsTaskDetailsList =
        bdmfeCase.getFECaseListOfTaskForCommunication(bdmCncrCmmRmKey);

      final int taskListSize =
        bdmRecordCommunicationsTaskDetailsList.dtls.size();
      Long existingTaskID = 0L;
      for (int i = 0; i < taskListSize; i++) {

        final BDMRecordCommunicationsTaskDetails bdmRecordCommunicationsTaskDetails =
          bdmRecordCommunicationsTaskDetailsList.dtls.item(i);

        // this is for an attachment only
        if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.kLongZero
          && bdmRecordCommunicationsTaskDetails.wdoSnapshot
            .indexOf(bdmCncrCmmRmKey.caseID + CuramConst.gkEmpty) != -1
          && bdmRecordCommunicationsTaskDetails.wdoSnapshot.indexOf(
            details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.attachmentID
              + CuramConst.gkEmpty) != -1) {
          existingTaskID = bdmRecordCommunicationsTaskDetails.taskID;
          if (existingTaskID != CuramConst.gkZero) {
            final Event closeTaskEvent = new Event();
            closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
            closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
            closeTaskEvent.primaryEventData = existingTaskID;
            EventService.raiseEvent(closeTaskEvent);
          }
        } else if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.kLongZero
          && bdmRecordCommunicationsTaskDetails.wdoSnapshot
            .indexOf(bdmCncrCmmRmKey.caseID + CuramConst.gkEmpty) != -1
          && bdmRecordCommunicationsTaskDetails.wdoSnapshot.indexOf(
            attachmentOut.dtls.documentType + CuramConst.gkEmpty) != -1) {
          existingTaskID = bdmRecordCommunicationsTaskDetails.taskID;
          if (existingTaskID != CuramConst.gkZero) {
            final Event closeTaskEvent = new Event();
            closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
            closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
            closeTaskEvent.primaryEventData = existingTaskID;
            EventService.raiseEvent(closeTaskEvent);
          }
        }
      }
    }

    final AttachmentIDAndAttachmentLinkIDStruct attachmentIDAndAttachmentLinkIDStruct =
      new AttachmentIDAndAttachmentLinkIDStruct();
    attachmentIDAndAttachmentLinkIDStruct.attachmentID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.attachmentID;

    final BDMCreateCaseAttachmentDetails bdmCreateCaseAttachmentDetails =
      new BDMCreateCaseAttachmentDetails();

    bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.documentType;

    bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseID;

    bdmCreateCaseAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.receiptDate;

    BDMMaintainAttachmentTaskFactory.newInstance().createFECAttachmentTask(
      bdmCreateCaseAttachmentDetails, attachmentIDAndAttachmentLinkIDStruct);

    // END: BUG 118324

    final BDMAttachmentIDs bdmAttachmentIDs = new BDMAttachmentIDs();
    bdmAttachmentIDs.caseID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseID;

    bdmAttachmentIDs.caseAttachmentLinkID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseAttachmentLinkID;

    bdmAttachmentIDs.attachmentID =
      details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.attachmentID;

    // Begin: link attachment with foreign application(s)
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

    BDMFEAttachmentLinkDtls bdmfeAttachmentLinkDtls =
      new BDMFEAttachmentLinkDtls();

    final curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();

    // Unique id generator class
    long fApplicationID = 0;

    if (selectedFAList.length() > CuramConst.gkZero
      && actionIDProperty.equals(BDMConstants.kSave)) {

      final String strFAIdsArr[] =
        selectedFAList.trim().split(CuramConst.gkTabDelimiter);

      for (int i = 0; i < strFAIdsArr.length; i++) {
        bdmfeAttachmentLinkDtls = new BDMFEAttachmentLinkDtls();
        fApplicationID = Long.parseLong(strFAIdsArr[i].toString());
        bdmfeAttachmentLinkDtls.feAttachmentLinkID = uniqueIDObj.getNextID();
        bdmfeAttachmentLinkDtls.relatedID = fApplicationID;
        bdmfeAttachmentLinkDtls.attachmentDate = Date.getCurrentDate();
        bdmfeAttachmentLinkDtls.attachmentID =
          details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.attachmentID;
        bdmfeAttachmentLinkDtls.description =
          details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.description;
        bdmfeAttachmentLinkDtls.recordStatus = RECORDSTATUS.NORMAL;
        bdmfeAttachmentLinkDtls.attachmentLinkTypCd =
          BDMATTACHMENTLINKTYPE.FAATTACHMENTLINK;

        bdmfeAttachmentLink.insert(bdmfeAttachmentLinkDtls);

        bdmAttachmentIDs.feAttachmentLinkID =
          bdmfeAttachmentLinkDtls.feAttachmentLinkID;
      }
      // End: link attachment with foreign application(s)

    }

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

    final BDMUtil bdmUtil = new BDMUtil();

    final BDMListOfForeignIDs bdmListOfForeignIDs =
      bdmUtil.getListOfForeignIdentifiers(key);

    return bdmListOfForeignIDs;
  }

  /**
   * This BPO is used for fetching the list of application type codes for the
   * selected
   * country.
   */
  @Override
  public BDMListOfFAApplicationTypes
    getListOfFAApplicationTypesByCountryCode(final BDMFACountryCodeKey key)
      throws AppException, InformationalException {

    final BDMListOfFAApplicationTypes bdmListOfFAApplicationTypes =
      new BDMListOfFAApplicationTypes();

    final curam.ca.gc.bdm.entity.intf.BDMCCodeAplTypMapping bdmcCodeAplTypMapping =
      BDMCCodeAplTypMappingFactory.newInstance();

    final BDMCCodeAplTypMappingKey bdmcCodeAplTypMappingKey =
      new BDMCCodeAplTypMappingKey();
    bdmcCodeAplTypMappingKey.countryCode = key.countryCode;

    BDMCCodeAplTypMappingDtls bdmcCodeAplTypMappingDtls =
      new BDMCCodeAplTypMappingDtls();

    try {
      bdmcCodeAplTypMappingDtls =
        bdmcCodeAplTypMapping.read(bdmcCodeAplTypMappingKey);
    } catch (final RecordNotFoundException rnfe) {
      bdmcCodeAplTypMappingDtls = new BDMCCodeAplTypMappingDtls();
    }

    final String faTypeCodeCommaSeperatedList =
      bdmcCodeAplTypMappingDtls.faListOfApplicationType;

    if (!StringUtil.isNullOrEmpty(faTypeCodeCommaSeperatedList)) {

      final String[] fApplicationTypCdsArr =
        faTypeCodeCommaSeperatedList.split(CuramConst.gkCommaDelimiter);

      BDMFAApplicationType bdmfaApplicationType = new BDMFAApplicationType();

      for (int i = 0; i < fApplicationTypCdsArr.length; i++) {

        bdmfaApplicationType = new BDMFAApplicationType();

        bdmfaApplicationType.applicationTypeCode = fApplicationTypCdsArr[i];

        bdmfaApplicationType.applicationTypeDescription =
          CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
            fApplicationTypCdsArr[i]);

        bdmListOfFAApplicationTypes.bdmFaApplType
          .addRef(bdmfaApplicationType);
      }

    }
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

    final BDMListOfOffices bdmListOfOffices = new BDMListOfOffices();

    BDMForeignOffice bdmForeignOffice = new BDMForeignOffice();

    final ExternalParty externalParty = ExternalPartyFactory.newInstance();

    ExternalPartyOfficeList externalPartyOfficeList =
      new ExternalPartyOfficeList();

    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();

    String countryDesc = CodetableUtil
      .getCodetableDescription(BDMSOURCECOUNTRY.TABLENAME, key.countryCode);

    // START Bug 114374: Taxonomy - The list of foreign offices in not displayed
    // in New and Edit Foreign Application when the user's language is set to
    // French
    final String currentLoggedInUser = TransactionInfo.getProgramUser();

    final Users usersObj = UsersFactory.newInstance();
    final UsersKey usersKey = new UsersKey();
    usersKey.userName = currentLoggedInUser;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final UsersDtls usersDtls = usersObj.read(notFoundIndicator, usersKey);

    if (!notFoundIndicator.isNotFound()) {
      if (usersDtls.defaultLocale
        .equalsIgnoreCase(BDMConstants.gkLocaleLowerFR)) {
        final CodeTableItemDetails codeTableItemDetails =
          CodetableUtil.getNonCachedCodetableItem(BDMSOURCECOUNTRY.TABLENAME,
            key.countryCode, BDMConstants.gkLocaleLowerEN);
        countryDesc = codeTableItemDetails.description;
      }
    }
    // END Bug 114374: Taxonomy - The list of foreign offices in not displayed
    // in New and Edit Foreign Application when the user's language is set to
    // French

    BDMExternalPartyDetailsList bdmExternalPartyDetailsList =
      new BDMExternalPartyDetailsList();

    final BDMExtPartyNameAndTypeKey bdmExtPartyNameAndTypeKey =
      new BDMExtPartyNameAndTypeKey();
    bdmExtPartyNameAndTypeKey.name = countryDesc.toUpperCase();

    bdmExtPartyNameAndTypeKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;

    final BDMExternalParty bdmExternalParty =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    bdmExternalPartyDetailsList = bdmExternalParty
      .searchExternalPartiesByNameAndType(bdmExtPartyNameAndTypeKey);

    if (bdmExternalPartyDetailsList.dtls.size() > CuramConst.gkZero) {

      externalPartyKey.concernRoleID =
        bdmExternalPartyDetailsList.dtls.item(0).concernRoleID;

      final BDMExternalPartyDtls ssaCountryDetails =
        this.readSSACountryDetails(externalPartyKey.concernRoleID);

      if (ssaCountryDetails.statusOfAgreement
        .equals(BDMSTATUSOFAGREEMENT.INFORCE)) {
        externalPartyOfficeList =
          externalParty.listExternalPartyOffice(externalPartyKey);

        final int extPartyOficeLstSize = externalPartyOfficeList.list.size();

        if (extPartyOficeLstSize > CuramConst.gkZero) {

          for (int i = 0; i < extPartyOficeLstSize; i++) {

            if (externalPartyOfficeList.list
              .item(i).externalPartyOfficeDtls.endDate.equals(Date.kZeroDate)
              || !externalPartyOfficeList.list
                .item(i).externalPartyOfficeDtls.endDate
                  .before(Date.getCurrentDate())) {
              bdmForeignOffice = new BDMForeignOffice();

              bdmForeignOffice.officeName = externalPartyOfficeList.list
                .item(i).externalPartyOfficeDtls.name;

              bdmForeignOffice.externalPartyOfficeID =
                externalPartyOfficeList.list
                  .item(i).externalPartyOfficeDtls.externalPartyOfficeID;

              bdmListOfOffices.bdmFO.addRef(bdmForeignOffice);
            }
          }
        }
      }
    }
    return bdmListOfOffices;
  }

  /**
   * This BPO is used for getting country for a foreign engagement case.
   */
  @Override
  public BDMFECaseDtls readCountryCode(final CaseHeaderKey key)
    throws AppException, InformationalException {

    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();

    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();

    bdmfeCaseKey.caseID = key.caseID;

    bdmfeCaseDtls = BDMFECaseFactory.newInstance().read(bdmfeCaseKey);

    return bdmfeCaseDtls;
  }

  /**
   * This BPO is used to fetch list of linked foreign applications with an
   * attachment.
   */
  @Override
  public BDMFAList getFAListLinkedToAttachment(final BDMFAAttachmentKey key)
    throws AppException, InformationalException {

    final BDMFAList bdmfaList = new BDMFAList();
    BDMFADetails bdmfaDetails = null;

    final curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();

    final BDMFEAttachmentLinkKeyStruct4 bdmfeAttachmentLinkKeyStruct4 =
      new BDMFEAttachmentLinkKeyStruct4();
    bdmfeAttachmentLinkKeyStruct4.attachmentID = key.attachmentID;
    bdmfeAttachmentLinkKeyStruct4.attachmentLinkTypCd =
      BDMATTACHMENTLINKTYPE.FAATTACHMENTLINK;

    final BDMFEAttachmentLinkDtlsList bdmfeAttachmentLinkDtlsList =
      bdmfeAttachmentLink
        .readByAttachmentIDAndAttchmtLnkTypCd(bdmfeAttachmentLinkKeyStruct4);

    final int listSize = bdmfeAttachmentLinkDtlsList.dtls.size();

    curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey = null;
    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    BDMForeignApplicationDtls bdmfaDtls = null;

    if (listSize > CuramConst.gkZero) {

      for (int i = 0; i < listSize; i++) {
        bdmfaDetails = new BDMFADetails();
        bdmfaKey =
          new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
        bdmfaKey.fApplicationID =
          bdmfeAttachmentLinkDtlsList.dtls.item(i).relatedID;

        bdmfaDtls = bdmfa.read(bdmfaKey);

        bdmfaDetails.assign(bdmfaDtls);

        // get the record status from BDMFEAttachmentLink
        bdmfaDetails.recordStatus =
          bdmfeAttachmentLinkDtlsList.dtls.item(i).recordStatus;

        // get the feAttachmentLinkID from BDMFEAttachmentLink
        bdmfaDetails.feAttachmentLinkID =
          bdmfeAttachmentLinkDtlsList.dtls.item(i).feAttachmentLinkID;

        bdmfaDetails.lastUpdatedOnAndByStr = constructLastUpdatedByAndOn(
          bdmfaDetails.lastUpdatedBy, bdmfaDetails.lastUpdatedOn.toString());

        bdmfaDetails.faApplTypeAndRefNumStr =
          CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
            bdmfaDetails.typeCode) + BDMConstants.kHiphen
            + bdmfaDetails.faReferenceNumber;

        bdmfaList.bdmFADetails.addRef(bdmfaDetails);

      }

    }

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

    final curam.ca.gc.bdm.facade.intf.BDMCase bdmCase =
      BDMCaseFactory.newInstance();
    bdmCase.cancelCaseAttachment(key);
  }

  /**
   * This BPO is used for reading attachment wizard information for create
   * attachment wizard screen.
   */
  @Override
  public BDMFAAttachmentDetails readFECaseAttachmentWizardDetails(
    final BDMAttachmentIDs key) throws AppException, InformationalException {

    BDMFAAttachmentDetails bdmfaAttachmentDetails =
      new BDMFAAttachmentDetails();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if (key.wizardStateID != 0) {

      bdmfaAttachmentDetails = (BDMFAAttachmentDetails) wizardPersistentState
        .read(key.wizardStateID);
      bdmfaAttachmentDetails.wizardStateID = key.wizardStateID;

    } else {

      bdmfaAttachmentDetails.wizardStateID =
        wizardPersistentState.create(new BDMFAAttachmentDetails());

    }

    return bdmfaAttachmentDetails;
  }

  /**
   * This BPO is used for reading attachment wizard information for edit
   * attachment wizard screen.
   */
  @Override
  public BDMFAAttachmentDetailsForRead readFECAttachmentWizardForModify(
    final BDMAttachmentIDs key) throws AppException, InformationalException {

    BDMFAAttachmentDetailsForRead bdmfaAttachmentDetailsForRead = null;

    BDMFAAttachmentDetails bdmfaAttachmentDetails =
      new BDMFAAttachmentDetails();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if (key.wizardStateID != 0) {

      bdmfaAttachmentDetailsForRead = new BDMFAAttachmentDetailsForRead();

      bdmfaAttachmentDetails = (BDMFAAttachmentDetails) wizardPersistentState
        .read(key.wizardStateID);

      bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails.dtls.attachmentDetails.readCaseAttachmentOut
        .assign(
          bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails);
      bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails.fileSource =
        bdmfaAttachmentDetails.bdmModifyAttachmentDetails.fileSource;

      key.attachmentID =
        bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.attachmentID;
      key.caseAttachmentLinkID =
        bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseAttachmentLinkID;
      key.caseID =
        bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails.dtls.attachmentDetails.readCaseAttachmentOut.caseID;

      bdmfaAttachmentDetails.wizardStateID = key.wizardStateID;

      wizardPersistentState.modify(key.wizardStateID, bdmfaAttachmentDetails);
      bdmfaAttachmentDetailsForRead.wizardStateID = key.wizardStateID;

      // Instantiate facade object for integrated case
      final IntegratedCase integratedCaseObj =
        IntegratedCaseFactory.newInstance();

      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = key.caseID;

      bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails.dtls.listMemberDetails =
        integratedCaseObj.listMemberDetails(listMemberDetailsKey);

      getFileLocationURL(bdmfaAttachmentDetailsForRead);

    } else {

      bdmfaAttachmentDetailsForRead = new BDMFAAttachmentDetailsForRead();
      bdmfaAttachmentDetails.wizardStateID =
        wizardPersistentState.create(new BDMFAAttachmentDetails());

      final curam.ca.gc.bdm.facade.intf.BDMCase bdmCase =
        BDMCaseFactory.newInstance();
      final ReadCaseAttachmentForModifyKey readCaseAttachmentForModifyKey =
        new ReadCaseAttachmentForModifyKey();
      readCaseAttachmentForModifyKey.attachmentKey.readCaseAttachmentIn.attachmentID =
        key.attachmentID;
      readCaseAttachmentForModifyKey.attachmentKey.readCaseAttachmentIn.caseAttachmentLinkID =
        key.caseAttachmentLinkID;
      readCaseAttachmentForModifyKey.listMemberKey.caseID = key.caseID;

      bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails = bdmCase
        .readCaseAttachmentDetailsForModiy(readCaseAttachmentForModifyKey);
      bdmfaAttachmentDetailsForRead.wizardStateID =
        bdmfaAttachmentDetails.wizardStateID;

      // bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails.dtls.attachmentDetails.readCaseAttachmentOut.description;

      // final
      // bdmfaAttachmentDetails//.assign(bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails);
      // wizardPersistentState.modify(bdmfaAttachmentDetails.wizardStateID,
      // bdmfaAttachmentDetails);

      getFileLocationURL(bdmfaAttachmentDetailsForRead);

    }

    return bdmfaAttachmentDetailsForRead;
  }


  /**
   * This BPO is used for displaying list of foreign application which are not
   * linked to an attachment yet.
   */
  @Override
  public BDMFAList listFANotLinkedWithAttachment(final BDMAttachmentIDs key)
    throws AppException, InformationalException {

    final BDMFAList bdmfaList = new BDMFAList();

    final curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();
    final BDMFEAttachmentLinkKeyStruct3 bdmfeAttachmentLinkKeyStruct3 =
      new BDMFEAttachmentLinkKeyStruct3();

    bdmfeAttachmentLinkKeyStruct3.attachmentLinkTypCd =
      BDMATTACHMENTLINKTYPE.FAATTACHMENTLINK;
    bdmfeAttachmentLinkKeyStruct3.recordStatus = RECORDSTATUS.NORMAL;

    bdmfeAttachmentLinkKeyStruct3.attachmentID = key.attachmentID;

    final BDMFEAttachmentLinkDtlsList bdmfeAttachmentLinkDtlsList =
      bdmfeAttachmentLink.readByAttachmentIDAttchmtLnkTypCdAndRecordStatus(
        bdmfeAttachmentLinkKeyStruct3);

    final int faLinkedAttcmntlistSize =
      bdmfeAttachmentLinkDtlsList.dtls.size();

    final StringBuffer strBuffFALinkedAttchmnt = new StringBuffer();

    for (int i = 0; i < faLinkedAttcmntlistSize; i++) {
      strBuffFALinkedAttchmnt
        .append(bdmfeAttachmentLinkDtlsList.dtls.item(i).relatedID
          + CuramConst.gkTabDelimiter);
    }

    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();
    bdmfaKeyStruct3.caseID = key.caseID;
    final BDMForeignApplicationDtlsList bdmfaDtlsList =
      bdmfa.readByCaseID(bdmfaKeyStruct3);

    final int faListSize = bdmfaDtlsList.dtls.size();
    BDMFADetails bdmfaDetails = null;
    BDMForeignApplicationDtls bdmfaDtls = null;
    final String strFaLinkedToAttachments =
      strBuffFALinkedAttchmnt.toString().trim();

    for (int j = 0; j < faListSize; j++) {
      bdmfaDetails = new BDMFADetails();
      bdmfaDtls = new BDMForeignApplicationDtls();
      bdmfaDtls = bdmfaDtlsList.dtls.item(j);
      bdmfaDetails.assign(bdmfaDtls);

      if (strFaLinkedToAttachments
        .indexOf(bdmfaDetails.fApplicationID + CuramConst.gkEmpty) == -1
        && bdmfaDetails.recordStatus.equals(RECORDSTATUS.NORMAL)) {

        bdmfaDetails.lastUpdatedOnAndByStr = constructLastUpdatedByAndOn(
          bdmfaDetails.lastUpdatedBy, bdmfaDetails.lastUpdatedOn.toString());

        bdmfaDetails.faApplTypeAndRefNumStr =
          CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
            bdmfaDetails.typeCode) + BDMConstants.kHiphen
            + bdmfaDetails.faReferenceNumber;

        bdmfaList.bdmFADetails.addRef(bdmfaDetails);
      }
    }

    return bdmfaList;
  }

  /**
   * This BPO is used for doing unlinking of a foreign application with an
   * attachment.
   */
  @Override
  public void unlinkFAFromAttachment(final BDMFAAttachmentKey key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();

    final BDMFEAttachmentLinkKey bdmfeAttachmentLinkKey =
      new BDMFEAttachmentLinkKey();
    bdmfeAttachmentLinkKey.feAttachmentLinkID = key.feAttachmentLinkID;

    final BDMFEAttachmentLinkDtls bdmfeAttachmentLinkDtls =
      bdmfeAttachmentLink.read(bdmfeAttachmentLinkKey);

    bdmfeAttachmentLinkDtls.recordStatus = RECORDSTATUS.CANCELLED;

    bdmfeAttachmentLink.modify(bdmfeAttachmentLinkKey,
      bdmfeAttachmentLinkDtls);

  }

  /**
   * This method is used for viewing foreign application history recordR
   * information on the view detailed screen:
   * BDMFEC_detailsForeignApplicationHistory
   */
  @Override
  public BDMFADetails viewForeignApplicationHistory(final BDMFAHistoryKey key)
    throws AppException, InformationalException {

    final BDMFADetails bdmfaDetails = new BDMFADetails();
    final curam.ca.gc.bdm.entity.struct.BDMFAHistoryKey bdmfaHistoryKey =
      new curam.ca.gc.bdm.entity.struct.BDMFAHistoryKey();

    BDMFAHistoryDtls bdmfaHistoryDtls = new BDMFAHistoryDtls();
    bdmfaHistoryKey.fAppHistoryID = key.fAppHistoryID;
    bdmfaHistoryDtls =
      BDMFAHistoryFactory.newInstance().read(bdmfaHistoryKey);
    bdmfaDetails.assign(bdmfaHistoryDtls);

    final ConcernRoleAlternateIDKey concernRoleAlternateIDKey =
      new ConcernRoleAlternateIDKey();
    ConcernRoleAlternateIDDtls concernRoleAlternateIDDtls =
      new ConcernRoleAlternateIDDtls();

    if (bdmfaDetails.fIdentifier != CuramConst.gkZero) {
      concernRoleAlternateIDKey.concernRoleAlternateID =
        bdmfaDetails.fIdentifier;

      concernRoleAlternateIDDtls = ConcernRoleAlternateIDFactory.newInstance()
        .read(concernRoleAlternateIDKey);
      bdmfaDetails.fIdentifierAlternateID =
        concernRoleAlternateIDDtls.alternateID;

    }

    final AddressKey addressKey = new AddressKey();

    final ExternalPartyOffice externalPartyOffice =
      ExternalPartyOfficeFactory.newInstance();
    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    ExternalPartyOfficeDtls externalPartyOfficeDtls =
      new ExternalPartyOfficeDtls();

    if (bdmfaDetails.fOfficeID != CuramConst.gkZero) {
      externalPartyOfficeKey.externalPartyOfficeID = bdmfaDetails.fOfficeID;

      externalPartyOfficeDtls =
        externalPartyOffice.readExternalPartyOffice(externalPartyOfficeKey);

      addressKey.addressID = externalPartyOfficeDtls.primaryAddressID;

      if (addressKey.addressID != CuramConst.kLongZero) {
        bdmfaDetails.foreignOfficeAddressData =
          bdmUtil.getFormattedAddress(addressKey);
      }

    }

    if (bdmfaDetails.bessInd) {
      bdmfaDetails.bessIndStr = BDMConstants.gkBESSIndYes;
    } else {
      bdmfaDetails.bessIndStr = BDMConstants.gkBESSIndNo;
    }

    return bdmfaDetails;
  }

  /**
   * Method to read external party details.
   *
   * @param concernRoleID
   *
   * @return BDMExternalPartyDtls
   */
  private BDMExternalPartyDtls readSSACountryDetails(final long concernRoleID)
    throws AppException, InformationalException {

    final BDMExternalParty externalPartyObj =
      BDMExternalPartyFactory.newInstance();

    final BDMExternalPartyKey extPartyKey = new BDMExternalPartyKey();
    extPartyKey.concernRoleID = concernRoleID;

    final BDMExternalPartyDtls extPartyDtls =
      externalPartyObj.read(extPartyKey);

    return extPartyDtls;
  }

  /**
   *
   * Get file location information for display
   *
   * @param bdmfaAttachmentDetailsForRead
   */
  private void getFileLocationURL(
    final BDMFAAttachmentDetailsForRead bdmfaAttachmentDetailsForRead) {

    final String fileLocationURL =
      bdmfaAttachmentDetailsForRead.bdmReadAttachmentDetails.dtls.attachmentDetails.readCaseAttachmentOut.fileLocation
        .trim();
    String fileName = CuramConst.gkEmpty;
    String extension = CuramConst.gkEmpty;

    try {
      fileName =
        fileLocationURL.substring(fileLocationURL.lastIndexOf("\\") + 1);

      final int i = fileName.lastIndexOf('.');
      if (i > 0) {
        extension = fileName.substring(i + 1);
      }

      Trace.kTopLevelLogger.info("FileName : " + fileName);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        fileLocationURL + " is not valid file." + e.getLocalizedMessage());
    }

    fileLocationURL.replace("\\", "/");

    bdmfaAttachmentDetailsForRead.attachmentURL.fileName = fileName;
    bdmfaAttachmentDetailsForRead.attachmentURL.fileLocationURL =
      fileLocationURL;

  }

  @Override
  public BDMFAInterimCodeDetailsList listFAInterimCodeDetails()
    throws AppException, InformationalException {

    final BDMFAInterimCodeDetailsList bdmfaInterimCodeDetailsList =
      new BDMFAInterimCodeDetailsList();

    // Codetable maintenance business process object
    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();

    // CodeTableItemDetails struct
    BDMFAInterimCodeDetails codeTableItemDetails = null;

    final String codetableName = BDMFOREIGNAPPINTERIM.TABLENAME;

    final String locale = TransactionInfo.getProgramLocale();
    final CodeTableItemDetailsList codeTableItemList = codeTableAdminObj
      .listAllItemsForLocaleAndLanguage(codetableName, locale);

    for (final CodeTableItemDetails ctDetails : codeTableItemList.dtls
      .items()) {
      codeTableItemDetails = new BDMFAInterimCodeDetails();
      codeTableItemDetails.assign(ctDetails);

      bdmfaInterimCodeDetailsList.bdmFAInterimCodeDetails
        .addRef(codeTableItemDetails);
    }

    return bdmfaInterimCodeDetailsList;
  }

  @Override
  public void reserveTask(final ReserveTaskDetailsForUser details)
    throws AppException, InformationalException {

    final BizObjAssociation bizObjAssociation =
      BizObjAssociationFactory.newInstance();
    final TaskKey taskKey2 = new TaskKey();
    taskKey2.taskID = details.reserveDetails.taskID;
    final BizObjAssociationDtlsList bizObjAssociationDtlsList =
      bizObjAssociation.searchByTaskID(taskKey2);

    final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    for (final BizObjAssociationDtls bizObjAssociationDtls : bizObjAssociationDtlsList.dtls) {

      if (bizObjAssociationDtls.bizObjectType
        .equalsIgnoreCase(BIZOBJASSOCIATION.CASE)) {
        final BDMCaseKey bdmCaseKey = new BDMCaseKey();
        bdmCaseKey.caseID = bizObjAssociationDtls.bizObjectID;
        final BDMFAList bdmfaList = listForeignApplications(bdmCaseKey);

        for (final BDMFADetails bdmfaDetails : bdmfaList.bdmFADetails) {

          final BDMForeignApplicationDtls bdmfaDtls =
            new BDMForeignApplicationDtls();
          final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
            new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
          bdmfaKey.fApplicationID = bdmfaDetails.fApplicationID;
          bdmfaDtls.assign(bdmfaDetails);
          bdmfaDtls.status = BDMFOREIGNAPPSTATUS.IN_PROGRESS;
          bdmfa.modify(bdmfaKey, bdmfaDtls);
        }
      }
    }
    // Create MaintainSupervisorTasks object
    final curam.supervisor.sl.intf.MaintainSupervisorTasks maintainSupervisorTasks =
      MaintainSupervisorTasksFactory.newInstance();

    maintainSupervisorTasks.reserveTask(details.reserveDetails);
  }

  @Override
  public BDMAttachmentIDs
    createFECaseAttachmentLinkingFA(final BDMFAAttachmentDetails details)
      throws AppException, InformationalException {

    BDMAttachmentIDs bdmAttachmentIDs = new BDMAttachmentIDs();

    final BDMCreateCaseAttachmentDetails bdmCreateCaseAttachmentDetails =
      null;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    BDMFAAttachmentDetails bdmfaAttachmentDetails2 = null;

    if (details.actionIDProperty.equals(BDMConstants.kSave)) {

      bdmfaAttachmentDetails2 = new BDMFAAttachmentDetails();

      bdmfaAttachmentDetails2 = (BDMFAAttachmentDetails) wizardPersistentState
        .read(details.wizardStateID);

      details.bdmCreateAttachmentDetails
        .assign(bdmfaAttachmentDetails2.bdmCreateAttachmentDetails);

      wizardPersistentState.modify(details.wizardStateID, details);
      bdmAttachmentIDs.wizardStateID = details.wizardStateID;
      bdmAttachmentIDs.caseID =
        bdmfaAttachmentDetails2.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

      validateAttachmentIsForeignApplication(details);
      bdmAttachmentIDs =
        createCaseAttachment(details.bdmCreateAttachmentDetails,
          details.selectedFAList, details.actionIDProperty);

      // START: Task 118400
      // Generate the task
      createTaskForFECAttachmentLinkingFA(bdmAttachmentIDs, details, false);
      // END: Task 118400
    }
    if (details.actionIDProperty.equals(BDMConstants.kBack)) {

      bdmfaAttachmentDetails2 = new BDMFAAttachmentDetails();

      bdmfaAttachmentDetails2 = (BDMFAAttachmentDetails) wizardPersistentState
        .read(details.wizardStateID);

      details.bdmCreateAttachmentDetails
        .assign(bdmfaAttachmentDetails2.bdmCreateAttachmentDetails);

      wizardPersistentState.modify(details.wizardStateID, details);
      bdmAttachmentIDs.wizardStateID = details.wizardStateID;
    }

    return bdmAttachmentIDs;
  }

  @Override
  public BDMAttachmentIDs
    modifyFECaseAttachmentLinkingFA(final BDMFAAttachmentDetails details)
      throws AppException, InformationalException {

    BDMAttachmentIDs bdmAttachmentIDs = new BDMAttachmentIDs();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails bdmfaAttachmentDetails2 =
      null;

    BDMModifyCaseAttachmentDetails bdmModifyCaseAttachmentDetails = null;

    if (details.actionIDProperty.equals(BDMConstants.kSave)) {

      bdmfaAttachmentDetails2 =
        new curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails();

      bdmModifyCaseAttachmentDetails = new BDMModifyCaseAttachmentDetails();

      bdmfaAttachmentDetails2 = (BDMFAAttachmentDetails) wizardPersistentState
        .read(details.wizardStateID);

      details.bdmModifyAttachmentDetails
        .assign(bdmfaAttachmentDetails2.bdmModifyAttachmentDetails);

      wizardPersistentState.modify(details.wizardStateID, details);

      bdmAttachmentIDs = modifyCaseAttachment(details, details.selectedFAList,
        details.actionIDProperty);

      // START: Task 118400
      // Generate the task
      createTaskForFECAttachmentLinkingFA(bdmAttachmentIDs, details, true);
      // END: Task 118400

    }

    if (details.actionIDProperty.equals(BDMConstants.kBack)) {

      bdmfaAttachmentDetails2 =
        new curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails();

      bdmfaAttachmentDetails2 = (BDMFAAttachmentDetails) wizardPersistentState
        .read(details.wizardStateID);
      bdmfaAttachmentDetails2.selectedFAList = details.selectedFAList;

      details.bdmModifyAttachmentDetails
        .assign(bdmfaAttachmentDetails2.bdmModifyAttachmentDetails);

      wizardPersistentState.modify(details.wizardStateID,
        bdmfaAttachmentDetails2);

    }

    return bdmAttachmentIDs;
  }

  private void createTaskForFECAttachmentLinkingFA(
    final BDMAttachmentIDs bdmAttachmentIDs,
    final BDMFAAttachmentDetails details, final boolean isModify)
    throws AppException, InformationalException {

    // assign here so no need to modify existing code
    if (isModify) {
      details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails
        .assign(
          details.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails);
    }

    // START: Task 85747: DEV: TASK-01 - Foreign Application Task
    BDMFAAttachmentNoticationTaskDetails bdmfaAttmntNotificationTakDetails =
      null;

    BDMForeignApplicationKey bdmForeignApplicationKey = null;
    BDMForeignApplicationDtls bdmForeignApplicationDtls = null;

    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();

    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();

    bdmfeCaseKey.caseID =
      details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

    bdmfeCaseDtls = BDMFECaseFactory.newInstance().read(bdmfeCaseKey);

    long fApplicationID = CuramConst.kLongZero;

    CaseHeaderDtls headerDtls = new CaseHeaderDtls();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    if (bdmAttachmentIDs.attachmentID != CuramConst.kLongZero
      && !StringUtil.isNullOrEmpty(details.selectedFAList)
      && (details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
        .equals(DOCUMENTTYPE.FOREIGN_APPLICATION)
        || details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType
          .equals(DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS))) {

      if (details.selectedFAList.length() > CuramConst.gkZero) {

        final String strFAIdsArr[] =
          details.selectedFAList.trim().split(CuramConst.gkTabDelimiter);

        for (int i = 0; i < strFAIdsArr.length; i++) {

          fApplicationID = Long.parseLong(strFAIdsArr[i].toString());

          bdmForeignApplicationKey = new BDMForeignApplicationKey();
          bdmForeignApplicationKey.fApplicationID = fApplicationID;

          bdmForeignApplicationDtls = new BDMForeignApplicationDtls();

          bdmForeignApplicationDtls = BDMForeignApplicationFactory
            .newInstance().read(bdmForeignApplicationKey);

          bdmfaAttmntNotificationTakDetails =
            new BDMFAAttachmentNoticationTaskDetails();

          bdmfaAttmntNotificationTakDetails.countryCode =
            bdmfeCaseDtls.countryCode;

          bdmfaAttmntNotificationTakDetails.faReferenceNumber =
            bdmForeignApplicationDtls.faReferenceNumber;

          bdmfaAttmntNotificationTakDetails.fApplicationID = fApplicationID;

          bdmfaAttmntNotificationTakDetails.faTypeCode =
            bdmForeignApplicationDtls.typeCode;

          bdmfaAttmntNotificationTakDetails.caseID =
            details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

          bdmfaAttmntNotificationTakDetails.attachmentReceiptDate =
            details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate;

          bdmfaAttmntNotificationTakDetails.wdoName =
            BDMConstants.kBdmTaskCreateDetails;

          bdmfaAttmntNotificationTakDetails.documentType =
            details.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType;

          final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
          caseHeaderKey.caseID = bdmfaAttmntNotificationTakDetails.caseID;
          headerDtls =
            CaseHeaderFactory.newInstance().read(nfIndicator, caseHeaderKey);

          if (!nfIndicator.isNotFound()) {
            bdmfaAttmntNotificationTakDetails.concernRoleID =
              headerDtls.concernRoleID;
          }

          createFAAttachmentNotificationTask(
            bdmfaAttmntNotificationTakDetails);

          // commented for : Bug 118209: E2E Defect - Foreign Benefit
          // Application moving to 'In Progress' status upon linking
          // attachment
          // bdmForeignApplicationDtls.status =
          // BDMFOREIGNAPPSTATUS.IN_PROGRESS;

          BDMForeignApplicationFactory.newInstance()
            .modify(bdmForeignApplicationKey, bdmForeignApplicationDtls);

        }
      }
    }
    // END: Task 85747: DEV: TASK-01 - Foreign Application Task
  }

}
