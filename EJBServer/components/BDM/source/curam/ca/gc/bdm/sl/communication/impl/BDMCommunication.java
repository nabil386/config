package curam.ca.gc.bdm.sl.communication.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.attachment.impl.Attachment;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCCTSUBMITOPT;
import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLE;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLETYPE;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDtls;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMCommStatusHistoryFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMPhoneNumberFactory;
import curam.ca.gc.bdm.entity.fact.BDMUserFactory;
import curam.ca.gc.bdm.entity.fact.BDMWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMConcernRoleCommRMKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetails;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetailsList;
import curam.ca.gc.bdm.entity.intf.BDMEscalationLevel;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtls;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtlsStruct2;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelKey;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMUserDtls;
import curam.ca.gc.bdm.entity.struct.BDMUserKey;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberDetails;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagIDKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCategorySubCategory;
import curam.ca.gc.bdm.facade.communication.struct.BDMCategorySubCategoryList;
import curam.ca.gc.bdm.facade.communication.struct.BDMCategorySubCategoryListData;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleIDEvidenceIDAndNameDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleIDEvidenceIDAndNameDetailsList;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceWizardKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMFileNameAndDataDtls;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchCriteria;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchDetailsList;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMViewCorrespondenceDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.impl.BDMGenerateCorrespondenceMapper;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.communication.fact.BDMManagePrivacyRequestFactory;
import curam.ca.gc.bdm.sl.communication.struct.BDMCommunicationStatusDetails;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.fec.struct.BDMFECTaskCreateDetails;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.BDMCCTOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFResponse;
import curam.ca.gc.bdm.sl.pdc.fact.BDMPDCPhoneNumberFactory;
import curam.ca.gc.bdm.sl.pdc.intf.BDMPDCPhoneNumber;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.codetable.ATTACHMENTSTATUS;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASECATEGORY;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETRANSACTIONEVENTS;
import curam.codetable.COMMUNICATIONDIRECTION;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.COMMUNICATIONTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.CORRESPONDENT;
import curam.codetable.EMAILTYPE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.GENDER;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.PHONETYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TASKCHANGETYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.VERIFICATIONSTATUS;
import curam.codetable.impl.COMMUNICATIONFORMATEntry;
import curam.core.facade.fact.ConcernRoleAlternateIDFactory;
import curam.core.facade.fact.TaskManagementFactory;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.facade.intf.ConcernRoleAlternateID;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.facade.struct.TaskDeadlineDetails;
import curam.core.facade.struct.TaskManagementTaskKey;
import curam.core.facade.struct.WizardStateID;
import curam.core.fact.AddressFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.EmailAddressFactory;
import curam.core.fact.MaintainAttachmentAssistantFactory;
import curam.core.fact.MaintainConcernRoleAddressFactory;
import curam.core.fact.MaintainConcernRoleEmailFactory;
import curam.core.fact.MaintainConcernRolePhoneFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.PhoneNumberFactory;
import curam.core.fact.ProspectPersonFactory;
import curam.core.fact.SystemUserFactory;
import curam.core.hook.impl.CommunicationInvocationStrategyHook;
import curam.core.hook.impl.PreCreateCommunicationHook;
import curam.core.hook.impl.PreModifyCommunicationHook;
import curam.core.impl.CuramConst;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.EnvVars;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.Address;
import curam.core.intf.CaseHeader;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.EmailAddress;
import curam.core.intf.MaintainAttachmentAssistant;
import curam.core.intf.MaintainConcernRoleAddress;
import curam.core.intf.MaintainConcernRoleEmail;
import curam.core.intf.MaintainConcernRolePhone;
import curam.core.intf.Person;
import curam.core.intf.PhoneNumber;
import curam.core.intf.SystemUser;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.intf.TaskAssignment;
import curam.core.sl.entity.struct.CaseIDParticipantRoleKey;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.ParticipantRoleIDAndNameDetails;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.entity.struct.TaskAssignmentDtlsList;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.fact.ClientInteractionFactory;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.impl.CaseTransactionLogIntf;
import curam.core.sl.infrastructure.cmis.impl.CMISAccessInterface;
import curam.core.sl.infrastructure.cmis.impl.CMSMetadataConst;
import curam.core.sl.infrastructure.cmis.impl.CMSMetadataInterface;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDStatusAndEvidenceTypeKey;
import curam.core.sl.infrastructure.impl.ValidationManagerConst;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.intf.ClientMerge;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.sl.struct.CaseParticipantRoleDetails;
import curam.core.sl.struct.ClientInteractionSupplementaryDetails;
import curam.core.sl.struct.CommunicationIDKey;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.sl.struct.RecordedCommKey;
import curam.core.sl.struct.SQLStatement;
import curam.core.sl.struct.ValidatePrimaryCaseParticipantDetails;
import curam.core.struct.AddressDetails;
import curam.core.struct.AddressKey;
import curam.core.struct.AlternateIDReadmultiDtlsList;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseIDConcernRoleID;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseSecurityCheckKey;
import curam.core.struct.CommunicationContactKey;
import curam.core.struct.CommunicationDetails;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleAlternateIDRMKey;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleNameDetails;
import curam.core.struct.CreateAttachmentCommStruct;
import curam.core.struct.CuramInd;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.EmailAddressKey;
import curam.core.struct.MaintainAddressKey;
import curam.core.struct.MaintainCommunicationKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.PhoneNumberDtls;
import curam.core.struct.PhoneNumberKey;
import curam.core.struct.ProspectPersonDtls;
import curam.core.struct.ProspectPersonKey;
import curam.core.struct.ReadCommAttachmentIn;
import curam.core.struct.ReadCommAttachmentOut;
import curam.core.struct.ReadConcernRoleEmailKey;
import curam.core.struct.ReadConcernRolePhoneKey;
import curam.core.struct.ReadParticipantRoleIDDetails;
import curam.core.struct.ReadParticipantRoleNameAndTypeDetails;
import curam.core.struct.SecurityResult;
import curam.core.struct.SystemUserDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.events.TASK;
import curam.message.BPOCASEEVENTS;
import curam.message.BPOCOMMUNICATION;
import curam.message.BPOMAINTAINCONCERNROLECOMMASSISTANT;
import curam.message.BPOTASKMANAGEMENT;
import curam.message.GENERAL;
import curam.message.GENERALCASE;
import curam.message.GENERALCOMMUNICATION;
import curam.message.GENERALCONCERN;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.fact.PDCEmailAddressFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCEmailAddress;
import curam.pdc.struct.ParticipantEmailAddressDetails;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.struct.ConcernRoleIDCaseIDKey;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.GeneralConstants;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Blob;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.BusinessObjectAssociationAdminFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.TaskHistoryAdminFactory;
import curam.util.workflow.fact.WorkflowDeadlineAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.intf.BusinessObjectAssociationAdmin;
import curam.util.workflow.intf.Task;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.intf.TaskHistoryAdmin;
import curam.util.workflow.intf.WorkflowDeadlineAdmin;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.BizObjAssociationKey;
import curam.util.workflow.struct.BizObjectAssociationDetails;
import curam.util.workflow.struct.BizObjectAssociationDetailsList;
import curam.util.workflow.struct.BizObjectID;
import curam.util.workflow.struct.TaskDetailsWithoutSnapshot;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;
import curam.util.workflow.struct.WorkflowDeadlineInfo;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkIDAndDataItemIDDetails;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkIDAndDataItemIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationStatusDetails;
import curam.wizard.util.impl.CodetableUtil;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.bind.JAXBException;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Level;

public class BDMCommunication
  extends curam.ca.gc.bdm.sl.communication.base.BDMCommunication {

  @Inject
  protected Attachment attachment;

  @Inject
  protected Provider<CaseTransactionLogIntf> caseTransactionLogProvider;

  // BEGIN, CR00289910, CD
  @Inject
  private CMISAccessInterface cmisAccess;

  // END, CR00289910
  // BEGIN, CR00354960, CD
  @Inject
  private Provider<CMSMetadataInterface> cmsMetadataProvider;

  // END, CR00354960

  // END, CR00090982

  // BEGIN, CR00467223, MV
  @Inject
  protected PreCreateCommunicationHook preCreateCommunicationHook;

  @Inject
  protected PreModifyCommunicationHook preModifyCommunicationHook;

  @Inject
  protected CommunicationInvocationStrategyHook communicationInvocationStrategyHook;

  @Inject
  BDMCommunicationHelper helper;

  private final BDMUtil bdmUtil = new BDMUtil();

  // END, CR00467223
  public BDMCommunication() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Modifies a recorded communication.
   *
   * @curam .util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   * @param recordedCommKey
   * Communication ID.
   *
   * @param recordedCommDetails
   * Communication details.
   *
   * #DOC_END#
   */
  @Override
  public void modifyRecordedComm(final RecordedCommKey recordedCommKey,
    final RecordedCommDetails1 recordedCommDetails)
    throws AppException, InformationalException {

    // BEGIN, CR00467223, MV
    final ConcernRoleCommunicationDtls concernRoleCommunicationDetails =
      new ConcernRoleCommunicationDtls();

    concernRoleCommunicationDetails.assign(recordedCommDetails);

    preModifyCommunication(concernRoleCommunicationDetails,
      COMMUNICATIONFORMATEntry.RECORDED);
    // END, CR00467223

    if (recordedCommDetails.caseID != 0) {

      // BEGIN CR00100436
      final CaseKey caseKey = new CaseKey();

      caseKey.caseID = recordedCommDetails.caseID;

      final SecurityResult isSensitivityException =
        checkSensitivityExceptions(caseKey);

      if (!isSensitivityException.result) {

        // BEGIN, CR00227042, PM
        final DataBasedSecurity dataBasedSecurity =
          SecurityImplementationFactory.get();
        final CaseSecurityCheckKey caseSecurityCheckKey =
          new CaseSecurityCheckKey();

        caseSecurityCheckKey.caseID = recordedCommDetails.caseID;

        // BEGIN, CR00246088, PM
        caseSecurityCheckKey.type = DataBasedSecurity.kMaintainSecurityCheck;
        // END, CR00246088

        final DataBasedSecurityResult dataBasedSecurityResult =
          dataBasedSecurity.checkCaseSecurity1(caseSecurityCheckKey);

        if (!dataBasedSecurityResult.result) {
          if (dataBasedSecurityResult.readOnly) {
            throw new AppException(
              GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
          } else if (dataBasedSecurityResult.restricted) {
            throw new AppException(GENERALCASE.ERR_CASESECURITY_CHECK_RIGHTS);
          } else {
            throw new AppException(
              GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
          }
        }
        // END, CR00227042
      }
    } // END, CR00100436
    else // BEGIN, CR00227859, PM
    if (0 != recordedCommDetails.clientParticipantRoleID) {
      final ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
        new ConcernRoleCommunicationDtls();

      concernRoleCommunicationDtls.concernRoleID =
        recordedCommDetails.clientParticipantRoleID;
      performSecurityChecks(concernRoleCommunicationDtls);
    }
    // END, CR00227859

    // BEGIN, CR00099710, PMD
    // Only apply validation for non case communications.
    if (recordedCommDetails.caseID == 0) {

      final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      concernRoleKey.concernRoleID =
        recordedCommDetails.clientParticipantRoleID;

      // Check if the concern role has been marked as a duplicate.
      final CuramInd curamInd =
        clientMergeObj.isConcernRoleDuplicate(concernRoleKey);

      // If the concern role is a duplicate, throw an exception.
      if (curamInd.statusInd) {

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().throwWithLookup(
            new AppException(
              BPOCOMMUNICATION.ERR_COMM_XRV_DUPLICATE_CLIENT_MODIFY),
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            4);
      }
    }
    // END, CR00099710

    // BEGIN, CR00052141, MC

    // BEGIN, HARP 70636, GYH
    // Set the communication incoming status.
    if (recordedCommDetails.communicationDirection
      .equals(COMMUNICATIONDIRECTION.INCOMING)) {

      recordedCommDetails.incomingInd = true;
    }
    // END, HARP 70636
    // END, CR00052141

    // Validations.
    validateRecordedCommunication1(recordedCommDetails);

    final ConcernRoleCommunication concernRoleCommunicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
      new ConcernRoleCommunicationDtls();
    final ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new ConcernRoleCommunicationKey();

    concernRoleCommunicationDtls.assign(recordedCommDetails);

    // BEGIN, CR00164936, JMA
    // Read Concern Role details.
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleDtlsKey = new ConcernRoleKey();

    concernRoleDtlsKey.concernRoleID =
      recordedCommDetails.correspondentParticipantRoleID;

    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleDtlsKey);

    final Address addressObj = AddressFactory.newInstance();
    final OtherAddressData otherAddressData = new OtherAddressData();

    otherAddressData.addressData = recordedCommDetails.addressData;

    final boolean addressEmpty =
      addressObj.isEmpty(otherAddressData).emptyInd;

    // If new address is entered.
    if (!addressEmpty) {

      final MaintainConcernRoleAddress maintainConcernRoleAddressObj =
        MaintainConcernRoleAddressFactory.newInstance();
      final MaintainAddressKey maintainAddressKey = new MaintainAddressKey();
      final AddressDetails addressDetails = new AddressDetails();

      // Get concern role ID from key.
      maintainAddressKey.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      addressDetails.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      addressDetails.addressData = recordedCommDetails.addressData;
      addressDetails.startDate = Date.getCurrentDate();
      addressDetails.typeCode = CONCERNROLEADDRESSTYPE.PRIVATE;

      if (concernRoleDtls.primaryAddressID == 0) {
        addressDetails.primaryAddressInd = true;
      }

      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();

      concernRoleAddressKey.concernRoleAddressID =
        maintainConcernRoleAddressObj.createConcernRoleAddress(
          maintainAddressKey, addressDetails).key.concernRoleAddressID;

      // Read the concern role address to get address ID.
      final ConcernRoleAddress concernRoleAddressObj =
        ConcernRoleAddressFactory.newInstance();

      concernRoleCommunicationDtls.addressID =
        concernRoleAddressObj.read(concernRoleAddressKey).addressID;

    } else {

      concernRoleCommunicationDtls.addressID = recordedCommDetails.addressID;
    }

    // If new email address is entered.
    if (recordedCommDetails.newEmailAddress.length() != 0) {

      // BEGIN, CR00352563, ZV
      final PDCEmailAddress pdcEmailAddressObj =
        PDCEmailAddressFactory.newInstance();
      final ParticipantEmailAddressDetails participantEmailAddressDetails =
        new ParticipantEmailAddressDetails();

      participantEmailAddressDetails.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      participantEmailAddressDetails.emailAddress =
        recordedCommDetails.newEmailAddress;
      participantEmailAddressDetails.startDate = Date.getCurrentDate();
      participantEmailAddressDetails.typeCode = EMAILTYPE.PERSONAL;

      if (concernRoleDtls.primaryEmailAddressID == 0) {
        participantEmailAddressDetails.primaryEmailInd = true;
      }

      final ReadConcernRoleEmailKey readConcernRoleEmailKey =
        new ReadConcernRoleEmailKey();

      // Create email address.
      readConcernRoleEmailKey.concernRoleEmailAddressID = pdcEmailAddressObj
        .insert(participantEmailAddressDetails).concernRoleEmailAddressID;
      // END, CR00352563

      final MaintainConcernRoleEmail maintainConcernRoleEmailObj =
        MaintainConcernRoleEmailFactory.newInstance();

      concernRoleCommunicationDtls.emailAddressID =
        maintainConcernRoleEmailObj
          .readEmailAddress(readConcernRoleEmailKey).emailAddressID;

    } else {

      concernRoleCommunicationDtls.emailAddressID =
        recordedCommDetails.correspondentEmailID;
    }

    if (recordedCommDetails.phoneNumber.length() != 0) {

      // BEGIN, CR00352563, ZV
      final BDMPDCPhoneNumber bdmPDCPhoneNumberObj =
        BDMPDCPhoneNumberFactory.newInstance();
      final ParticipantPhoneDetails participantPhoneDetails =
        new ParticipantPhoneDetails();

      participantPhoneDetails.phoneCountryCode =
        recordedCommDetails.phoneCountryCode;
      participantPhoneDetails.phoneAreaCode =
        recordedCommDetails.phoneAreaCode;
      participantPhoneDetails.phoneNumber = recordedCommDetails.phoneNumber;
      participantPhoneDetails.phoneExtension =
        recordedCommDetails.phoneExtension;
      participantPhoneDetails.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      participantPhoneDetails.startDate = Date.getCurrentDate();

      if (recordedCommDetails.methodTypeCode
        .equals(COMMUNICATIONMETHOD.FAX)) {
        participantPhoneDetails.typeCode = PHONETYPE.FAX;
      } else {
        participantPhoneDetails.typeCode = PHONETYPE.PERSONAL;
      }

      if (concernRoleDtls.primaryPhoneNumberID == 0) {
        participantPhoneDetails.primaryPhoneInd = true;
      }

      final ReadConcernRolePhoneKey readConcernRolePhoneKey =
        new ReadConcernRolePhoneKey();

      // Task 21121 - use BDMPhoneCountry code table value to store country code
      readConcernRolePhoneKey.concernRolePhoneNumberID = bdmPDCPhoneNumberObj
        .insert(participantPhoneDetails).concernRolePhoneNumberID;
      // END, CR00352563

      final MaintainConcernRolePhone maintainConcernRolePhoneObj =
        MaintainConcernRolePhoneFactory.newInstance();

      concernRoleCommunicationDtls.phoneNumberID = maintainConcernRolePhoneObj
        .readPhoneNumber(readConcernRolePhoneKey).phoneNumberID;

    } else {

      concernRoleCommunicationDtls.phoneNumberID =
        recordedCommDetails.phoneNumberID;

    }
    // END, CR00164936

    concernRoleCommunicationKey.communicationID =
      recordedCommDetails.communicationID;

    // Modify the existing attachment entry.
    if (recordedCommDetails.attachmentInd == true) {

      final AttachmentKey attachmentKey = new AttachmentKey();
      AttachmentDtls attachmentDtls = new AttachmentDtls();

      attachmentKey.attachmentID = recordedCommDetails.attachmentID;
      // BEGIN, CR00146458, VR
      attachmentDtls = attachment.read(attachmentKey);
      // END, CR00146458

      if (recordedCommDetails.newAttachmentContents.length() != 0) {
        attachmentDtls.attachmentContents =
          recordedCommDetails.newAttachmentContents;

        // Parses the file name from path of the file.
        File attachmentName;

        attachmentName = new File(recordedCommDetails.newAttachmentName);
        attachmentDtls.attachmentName = attachmentName.getName();
      }

      attachmentDtls.documentType = recordedCommDetails.communicationTypeCode;
      attachmentDtls.fileLocation = recordedCommDetails.fileLocation;
      attachmentDtls.fileReference = recordedCommDetails.fileReference;
      attachmentDtls.receiptDate = recordedCommDetails.communicationDate;

      // BEGIN, CR00364828, CD
      // create some meta data about the attachment
      final CMSMetadataInterface cmsMetadata = cmsMetadataProvider.get();

      cmsMetadata.add(CMSMetadataConst.kCaseID,
        Long.toString(recordedCommDetails.caseID));
      cmsMetadata.add(CMSMetadataConst.kCommunicationDate,
        recordedCommDetails.communicationDate);
      cmsMetadata.add(CMSMetadataConst.kParticipantID,
        Long.toString(recordedCommDetails.correspondentParticipantRoleID));
      // END, CR00364828

      // Modify attachment record.
      // BEGIN, CR00146458, VR
      attachment.modify(attachmentKey, attachmentDtls);
      // END, CR00146458

      if (recordedCommDetails.caseID != 0) {

        // BEGIN CR00100268, JG
        final CodeTableItemIdentifier codeTableItemIdentifier =
          new CodeTableItemIdentifier(COMMUNICATIONMETHOD.TABLENAME,
            recordedCommDetails.methodTypeCode);
        String fileNameOrReference = CuramConst.gkEmpty;

        if (attachmentDtls.attachmentName.length() != 0) {
          fileNameOrReference = attachmentDtls.attachmentName;
        } else if (attachmentDtls.fileReference.length() != 0) {
          fileNameOrReference = attachmentDtls.fileReference;
        }

        final LocalisableString attachmentDescription = new LocalisableString(
          BPOCASEEVENTS.COMMUNICATION_ATTACHMENT_MODIFIED)
            .arg(fileNameOrReference).arg(codeTableItemIdentifier)
            .arg(recordedCommDetails.correspondentName);

        // BEGIN CR00108818, GBA
        caseTransactionLogProvider.get().recordCaseTransaction(
          CASETRANSACTIONEVENTS.COMMUNICATION_ATTACHMENT_MODIFIED,
          attachmentDescription, recordedCommDetails.caseID,
          concernRoleCommunicationKey.communicationID);
        // END CR00108818
        // END CR00100268
      }

    } else if (recordedCommDetails.attachmentInd == false
      && recordedCommDetails.newAttachmentContents.length() != 0) {

      final CreateAttachmentCommStruct createAttachmentCommStruct =
        new CreateAttachmentCommStruct();

      createAttachmentCommStruct.newAttachmentName =
        recordedCommDetails.newAttachmentName;
      createAttachmentCommStruct.newAttachmentContents =
        recordedCommDetails.newAttachmentContents;
      createAttachmentCommStruct.fileLocation =
        recordedCommDetails.fileLocation;
      createAttachmentCommStruct.fileReference =
        recordedCommDetails.fileReference;
      createAttachmentCommStruct.receiptDate =
        recordedCommDetails.communicationDate;
      createAttachmentCommStruct.documentType =
        recordedCommDetails.communicationTypeCode;
      createAttachmentCommStruct.communicationID =
        recordedCommDetails.communicationID;

      final MaintainAttachmentAssistant maintainAttachmentAssistantObj =
        MaintainAttachmentAssistantFactory.newInstance();

      maintainAttachmentAssistantObj
        .insertAttachmentDetails(createAttachmentCommStruct);

      // Set the attachment indicator to true.
      concernRoleCommunicationDtls.attachmentInd = true;

    }

    // Set the communication status to sent or received.
    if (recordedCommDetails.communicationDirection
      .equals(COMMUNICATIONDIRECTION.OUTGOING)) {

      concernRoleCommunicationDtls.communicationStatus =
        COMMUNICATIONSTATUS.SENT;
    } else {

      concernRoleCommunicationDtls.communicationStatus =
        COMMUNICATIONSTATUS.RECEIVED;
    }

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID =
      recordedCommDetails.correspondentParticipantRoleID;

    concernRoleCommunicationDtls.correspondentName =
      concernRoleObj.readConcernRoleName(concernRoleKey).concernRoleName;

    concernRoleCommunicationDtls.userName = TransactionInfo.getProgramUser();

    // START: TASK 96970: Task 10 and 11
    final CommunicationIDKey communicationIDKey = new CommunicationIDKey();
    communicationIDKey.communicationID =
      concernRoleCommunicationKey.communicationID;
    closeCommunicationTask(communicationIDKey, recordedCommDetails);
    // END: TASK 96970: Task 10 and 11

    // Modify existing communication entry.
    concernRoleCommunicationObj.modify(concernRoleCommunicationKey,
      concernRoleCommunicationDtls);

    // START: Task 89604: DEV: Manage Privacy Request Implementation
    BDMManagePrivacyRequestFactory.newInstance()
      .processTaskForPrivacyRequest(communicationIDKey, recordedCommDetails);
    // END: Task 89604: DEV: Manage Privacy Request Implementation

    // BEGIN, CR00022584, RR
    // BEGIN, CR00022728, RR
    if (recordedCommDetails != null && recordedCommDetails.caseID != 0) {
      // Log Transaction Details

      // BEGIN, CR00090982, JG
      final CodeTableItemIdentifier codeTableItemIdentifier =
        new CodeTableItemIdentifier(COMMUNICATIONMETHOD.TABLENAME,
          recordedCommDetails.methodTypeCode);

      final ConcernRoleNameDetails correspondentConcernRoleName =
        concernRoleObj.readConcernRoleName(concernRoleKey);

      final LocalisableString description =
        new LocalisableString(BPOCASEEVENTS.COMMUNICATION_MODIFIED)
          .arg(codeTableItemIdentifier)
          .arg(correspondentConcernRoleName.concernRoleName);

      caseTransactionLogProvider.get().recordCaseTransaction(
        CASETRANSACTIONEVENTS.COMMUNICATION_MODIFY, description,
        recordedCommDetails.caseID,
        concernRoleCommunicationKey.communicationID);

      // END, CR00090982
    }
    // END, CR00022728
    // END, CR00022584
  }

  /**
   * Close the communication task
   *
   * @param communicationIDKey
   * @param recordedCommDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void closeCommunicationTask(
    final CommunicationIDKey communicationIDKey,
    final RecordedCommDetails1 recordedCommDetails)
    throws AppException, InformationalException {

    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey =
      new BDMConcernRoleCommRMKey();

    // Get the current communication details
    final ConcernRoleCommunicationKey communicationKey =
      new ConcernRoleCommunicationKey();
    communicationKey.communicationID = communicationIDKey.communicationID;
    bdmCncrCmmRmKey.communicationID = communicationIDKey.communicationID;
    final ConcernRoleCommunication communicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationDtls existingCommDtls =
      communicationObj.read(communicationKey);

    if ((existingCommDtls.typeCode
      .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
      || existingCommDtls.typeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))
      && !existingCommDtls.typeCode
        .equalsIgnoreCase(recordedCommDetails.communicationTypeCode)) {

      bdmCncrCmmRmKey.communicationTypeCode = existingCommDtls.typeCode;

      if (recordedCommDetails.clientParticipantRoleID != CuramConst.kLongZero
        && recordedCommDetails.caseID == CuramConst.kLongZero) {
        bdmCncrCmmRmKey.concernRoleID =
          recordedCommDetails.clientParticipantRoleID;

        final Long existingTaskID =
          BDMUtil.getExistingTaskIDForPerson(bdmCncrCmmRmKey);

        if (existingTaskID != CuramConst.gkZero) {
          final Event closeTaskEvent = new Event();
          closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
          closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
          closeTaskEvent.primaryEventData = existingTaskID;
          EventService.raiseEvent(closeTaskEvent);
        }

      } else if (recordedCommDetails.caseID != CuramConst.kLongZero) {
        bdmCncrCmmRmKey.caseID = recordedCommDetails.caseID;
        bdmCncrCmmRmKey.concernRoleID =
          recordedCommDetails.correspondentConcernRoleID;

        final Long existingTaskID =
          BDMUtil.getExistingTaskIDForCase(bdmCncrCmmRmKey);

        if (existingTaskID != CuramConst.gkZero) {
          final Event closeTaskEvent = new Event();
          closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
          closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
          closeTaskEvent.primaryEventData = existingTaskID;
          EventService.raiseEvent(closeTaskEvent);
        }

        // delete the urgent flag

        final CaseIDDetails caseDtlskey = new CaseIDDetails();
        caseDtlskey.caseID = recordedCommDetails.caseID;
        final BDMCaseUrgentFlagDetailsList urgentFlagDtlsList =
          BDMCaseUrgentFlagFactory.newInstance()
            .listCurrentUrgentFlags(caseDtlskey);

        String urgentFlagType = null;
        if (existingCommDtls.typeCode
          .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)) {
          urgentFlagType = BDMURGENTFLAGTYPE.MPENQUIRY;
        } else if (existingCommDtls.typeCode
          .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
          urgentFlagType = BDMURGENTFLAGTYPE.MINISTERIAL_ENQUIRY;
        }

        for (final BDMCaseUrgentFlagDetails urgentFlagDtls : urgentFlagDtlsList.dtls
          .items()) {
          final BDMCaseUrgentFlagIDKey deleteFlagKey =
            new BDMCaseUrgentFlagIDKey();
          deleteFlagKey.bdmCaseUrgentFlagID =
            urgentFlagDtls.bdmCaseUrgentFlagID;
          deleteFlagKey.caseID = urgentFlagDtls.caseID;
          if (urgentFlagDtls.type.equalsIgnoreCase(urgentFlagType)) {
            BDMCaseUrgentFlagFactory.newInstance()
              .deleteCaseUrgentFlag(deleteFlagKey);
          }
        }

      }

    }
  }

  /**
   * Create a recorded communication, correspondent is added as a participant
   * and returns the communicationID..
   *
   * @curam .util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   * @param recordedCommDetails
   * The details of the record communication to be created.
   *
   * @return The communication id of the recorded communication just created.
   *
   * #DOC_END#
   */
  @Override
  public ConcernRoleCommKeyOut createRecordedCommWithReturningID(
    final RecordedCommDetails1 recordedCommDetails)
    throws AppException, InformationalException {

    // BEGIN, CR00467223, MV
    final ConcernRoleCommunicationDtls concernRoleCommunicationDetails =
      new ConcernRoleCommunicationDtls();

    concernRoleCommunicationDetails.assign(recordedCommDetails);

    preCreateCommunication(concernRoleCommunicationDetails,
      COMMUNICATIONFORMATEntry.RECORDED);
    // END, CR00467223

    final CommunicationDetails communicationDetails =
      new CommunicationDetails();
    final CommunicationContactKey communicationContactKey =
      new CommunicationContactKey();

    final MaintainCommunicationKey commKey = new MaintainCommunicationKey();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();

    CuramInd curamInd;

    // Set the communication incoming status.
    if (recordedCommDetails.communicationDirection
      .equals(COMMUNICATIONDIRECTION.INCOMING)) {

      recordedCommDetails.incomingInd = true;
    }

    // BEGIN, CR00099710, PMD
    // Only apply validation for participant communications.
    if (recordedCommDetails.caseID != 0) {

      concernRoleKey.concernRoleID =
        recordedCommDetails.clientParticipantRoleID;

      // Check if the concern role has been marked as a duplicate.
      curamInd = clientMergeObj.isConcernRoleDuplicate(concernRoleKey);

      // If the concern role is a duplicate, throw an exception.
      if (curamInd.statusInd) {

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().throwWithLookup(
            new AppException(
              BPOCOMMUNICATION.ERR_COMM_XRV_DUPLICATE_CLIENT_CREATE),
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            5);
      }
    }
    // END, CR00099710

    // BEGIN, CR00101018, CM
    if (recordedCommDetails.caseID != 0) {

      // BEGIN, CR00100552, CM
      final CaseIDConcernRoleID caseIDConcernRoleID =
        new CaseIDConcernRoleID();

      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

      ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
        new ReadParticipantRoleIDDetails();

      caseHeaderKey.caseID = recordedCommDetails.caseID;

      readParticipantRoleIDDetails =
        caseHeaderObj.readParticipantRoleID(caseHeaderKey);

      caseIDConcernRoleID.caseID = recordedCommDetails.caseID;
      caseIDConcernRoleID.concernRoleID =
        readParticipantRoleIDDetails.concernRoleID;

      concernRoleKey.concernRoleID =
        readParticipantRoleIDDetails.concernRoleID;

      // Check if the concern role has been marked as a duplicate.
      curamInd = clientMergeObj.isConcernRoleDuplicate(concernRoleKey);

      // If the concern role has been marked as a duplicate, a
      // notification will
      // be sent to the case owners.
      if (curamInd.statusInd) {

        sendNotification(caseIDConcernRoleID);
      }
      // END, CR00100552
    }
    // END, CR00101018

    // Validations.
    this.validateRecordedCommunication1(recordedCommDetails);

    // BEGIN, CR00152391, AK
    // Security Checks.
    final ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
      new ConcernRoleCommunicationDtls();

    concernRoleCommunicationDtls.concernRoleID =
      recordedCommDetails.clientParticipantRoleID;

    if (concernRoleCommunicationDtls.concernRoleID == 0) {
      concernRoleCommunicationDtls.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
    }

    // BEGIN, CR00227859, PM
    concernRoleCommunicationDtls.caseID = recordedCommDetails.caseID;
    // END, CR00227859

    performSecurityChecks(concernRoleCommunicationDtls);
    // END, CR00152391

    communicationDetails.assign(recordedCommDetails);

    // Set the communication status to sent or received.
    if (recordedCommDetails.communicationDirection
      .equals(COMMUNICATIONDIRECTION.OUTGOING)) {

      communicationDetails.communicationStatus = COMMUNICATIONSTATUS.SENT;
    } else {
      communicationDetails.communicationStatus = COMMUNICATIONSTATUS.RECEIVED;
    }

    communicationDetails.communicationFormat = COMMUNICATIONFORMAT.RECORDED;
    communicationDetails.userName = TransactionInfo.getProgramUser();

    // BEGIN, CR00143201, JMA
    // Read Concern Role details
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleDtlsKey = new ConcernRoleKey();

    concernRoleDtlsKey.concernRoleID =
      recordedCommDetails.correspondentParticipantRoleID;

    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleDtlsKey);

    final Address addressObj = AddressFactory.newInstance();
    final OtherAddressData otherAddressData = new OtherAddressData();

    otherAddressData.addressData = recordedCommDetails.addressData;

    final boolean addressEmpty =
      addressObj.isEmpty(otherAddressData).emptyInd;

    // If new address is entered.
    if (!addressEmpty) {

      final MaintainConcernRoleAddress maintainConcernRoleAddressObj =
        MaintainConcernRoleAddressFactory.newInstance();
      final MaintainAddressKey maintainAddressKey = new MaintainAddressKey();
      final AddressDetails addressDetails = new AddressDetails();

      maintainAddressKey.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      addressDetails.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      addressDetails.addressData = recordedCommDetails.addressData;
      addressDetails.startDate = Date.getCurrentDate();
      addressDetails.typeCode = CONCERNROLEADDRESSTYPE.PRIVATE;

      if (concernRoleDtls.primaryAddressID == 0) {
        addressDetails.primaryAddressInd = true;
      }

      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();

      concernRoleAddressKey.concernRoleAddressID =
        maintainConcernRoleAddressObj.createConcernRoleAddress(
          maintainAddressKey, addressDetails).key.concernRoleAddressID;

      // Read the concern role address to get address ID.
      final ConcernRoleAddress concernRoleAddressObj =
        ConcernRoleAddressFactory.newInstance();

      communicationContactKey.addressID =
        concernRoleAddressObj.read(concernRoleAddressKey).addressID;
    } else {
      communicationContactKey.addressID = recordedCommDetails.addressID;
    }

    if (recordedCommDetails.newEmailAddress.length() != 0) {

      // BEGIN, CR00352563, ZV
      final PDCEmailAddress pdcEmailAddressObj =
        PDCEmailAddressFactory.newInstance();
      final ParticipantEmailAddressDetails participantEmailAddressDetails =
        new ParticipantEmailAddressDetails();

      participantEmailAddressDetails.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      participantEmailAddressDetails.emailAddress =
        recordedCommDetails.newEmailAddress;
      participantEmailAddressDetails.startDate = Date.getCurrentDate();
      participantEmailAddressDetails.typeCode = EMAILTYPE.PERSONAL;

      if (concernRoleDtls.primaryEmailAddressID == 0) {
        participantEmailAddressDetails.primaryEmailInd = true;
      }

      final ReadConcernRoleEmailKey readConcernRoleEmailKey =
        new ReadConcernRoleEmailKey();

      // Create email address.
      readConcernRoleEmailKey.concernRoleEmailAddressID = pdcEmailAddressObj
        .insert(participantEmailAddressDetails).concernRoleEmailAddressID;
      // END, CR00352563

      final MaintainConcernRoleEmail maintainConcernRoleEmailObj =
        MaintainConcernRoleEmailFactory.newInstance();

      communicationContactKey.emailAddressID = maintainConcernRoleEmailObj
        .readEmailAddress(readConcernRoleEmailKey).emailAddressID;

    } else {

      communicationContactKey.emailAddressID =
        recordedCommDetails.correspondentEmailID;
    }

    if (recordedCommDetails.phoneNumber.length() != 0) {

      // BEGIN, CR00352563, ZV
      final BDMPDCPhoneNumber bdmPDCPhoneNumberObj =
        BDMPDCPhoneNumberFactory.newInstance();
      final ParticipantPhoneDetails participantPhoneDetails =
        new ParticipantPhoneDetails();

      participantPhoneDetails.phoneCountryCode =
        recordedCommDetails.phoneCountryCode;
      participantPhoneDetails.phoneAreaCode =
        recordedCommDetails.phoneAreaCode;
      participantPhoneDetails.phoneNumber = recordedCommDetails.phoneNumber;
      participantPhoneDetails.phoneExtension =
        recordedCommDetails.phoneExtension;
      participantPhoneDetails.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      participantPhoneDetails.startDate = Date.getCurrentDate();

      if (recordedCommDetails.methodTypeCode
        .equals(COMMUNICATIONMETHOD.FAX)) {
        participantPhoneDetails.typeCode = PHONETYPE.FAX;
      } else {
        participantPhoneDetails.typeCode = PHONETYPE.PERSONAL;
      }

      if (concernRoleDtls.primaryPhoneNumberID == 0) {
        participantPhoneDetails.primaryPhoneInd = true;
      }

      final ReadConcernRolePhoneKey readConcernRolePhoneKey =
        new ReadConcernRolePhoneKey();

      // Task 21121 - use BDMPhoneCountry code table value to store country code
      readConcernRolePhoneKey.concernRolePhoneNumberID = bdmPDCPhoneNumberObj
        .insert(participantPhoneDetails).concernRolePhoneNumberID;
      // END, CR00352563

      final MaintainConcernRolePhone maintainConcernRolePhoneObj =
        MaintainConcernRolePhoneFactory.newInstance();

      communicationContactKey.phoneNumberID = maintainConcernRolePhoneObj
        .readPhoneNumber(readConcernRolePhoneKey).phoneNumberID;
    } else {
      communicationContactKey.phoneNumberID =
        recordedCommDetails.phoneNumberID;
    }
    // END, CR00143201

    commKey.concernRoleID = recordedCommDetails.clientParticipantRoleID;
    commKey.caseID = recordedCommDetails.caseID;

    if (recordedCommDetails.newAttachmentName.length() != 0) {
      communicationDetails.attachmentInd = true;
    }

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = insertCommDetails(
      commKey, communicationDetails, communicationContactKey);

    if (recordedCommDetails.newAttachmentName.length() != 0) {

      final CreateAttachmentCommStruct createAttachmentCommStruct =
        new CreateAttachmentCommStruct();

      createAttachmentCommStruct.newAttachmentName =
        recordedCommDetails.newAttachmentName;
      createAttachmentCommStruct.newAttachmentContents =
        recordedCommDetails.newAttachmentContents;
      createAttachmentCommStruct.fileLocation =
        recordedCommDetails.fileLocation;
      createAttachmentCommStruct.fileReference =
        recordedCommDetails.fileReference;
      createAttachmentCommStruct.receiptDate =
        recordedCommDetails.communicationDate;
      createAttachmentCommStruct.documentType =
        recordedCommDetails.communicationTypeCode;
      createAttachmentCommStruct.communicationID =
        concernRoleCommKeyOut.communicationID;
      final MaintainAttachmentAssistant maintainAttachmentAssistantObj =
        MaintainAttachmentAssistantFactory.newInstance();

      // BEGIN, CR00354960, CD
      // create some meta data about the attachment
      final CMSMetadataInterface cmsMetadata = cmsMetadataProvider.get();

      // BEGIN, CR00361162, CD
      cmsMetadata.add(CMSMetadataConst.kCaseID,
        Long.toString(recordedCommDetails.caseID));
      // END, CR00361162
      cmsMetadata.add(CMSMetadataConst.kCommunicationDate,
        recordedCommDetails.communicationDate);
      cmsMetadata.add(CMSMetadataConst.kParticipantID,
        Long.toString(recordedCommDetails.correspondentParticipantRoleID));
      // END, CR00354960

      maintainAttachmentAssistantObj
        .insertAttachmentDetails(createAttachmentCommStruct);

    }
    // BEGIN,HARP 47358, DK
    // Call into sample interaction center
    // and record the client interaction for this communication.
    final ClientInteractionSupplementaryDetails clientInteractionSupplementaryDetails =
      new ClientInteractionSupplementaryDetails();

    clientInteractionSupplementaryDetails.clientDtls.dtls.concernRoleID =
      recordedCommDetails.clientParticipantRoleID;
    clientInteractionSupplementaryDetails.clientDtls.dtls.relatedID =
      concernRoleCommKeyOut.communicationID;

    // BEGIN, CR00222190, ELG
    clientInteractionSupplementaryDetails.clientDtls.dtls.description =
      GENERALCOMMUNICATION.INF_COMMUNICATION
        .getMessageText(TransactionInfo.getProgramLocale());
    // END, CR00222190

    clientInteractionSupplementaryDetails.methodTypeCode =
      recordedCommDetails.methodTypeCode;
    clientInteractionSupplementaryDetails.incomingInd =
      recordedCommDetails.incomingInd;
    clientInteractionSupplementaryDetails.communicationDirectionInd = true;
    clientInteractionSupplementaryDetails.correspondentConcernRoleID =
      recordedCommDetails.correspondentParticipantRoleID;

    ClientInteractionFactory.newInstance()
      .recordClientAndCorrespondentInteraction(
        clientInteractionSupplementaryDetails);

    // BEGIN, CR00022584, RR
    // BEGIN, CR00022728, RR

    // BEGIN, CR00098859, MR
    // Log Transaction Details.
    // When an incoming communication is created.
    if (recordedCommDetails != null && recordedCommDetails.caseID != 0
      && recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.INCOMING)) {

      final CodeTableItemIdentifier codeTableItemIdentifier =
        new CodeTableItemIdentifier(COMMUNICATIONMETHOD.TABLENAME,
          recordedCommDetails.methodTypeCode);

      concernRoleKey.concernRoleID =
        communicationDetails.correspondentConcernRoleID;
      final ConcernRoleNameDetails correspondentConcernRoleName =
        concernRoleObj.readConcernRoleName(concernRoleKey);

      final LocalisableString description =
        new LocalisableString(BPOCASEEVENTS.COMMUNICATION_RECEIVED)
          .arg(codeTableItemIdentifier)
          .arg(correspondentConcernRoleName.concernRoleName);

      caseTransactionLogProvider.get().recordCaseTransaction(
        CASETRANSACTIONEVENTS.COMMUNICATION_RECEIVED, description,
        recordedCommDetails.caseID, concernRoleCommKeyOut.communicationID);

    }

    // When an outgoing communication is created.
    if (recordedCommDetails != null && recordedCommDetails.caseID != 0
      && recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.OUTGOING)) {

      final CodeTableItemIdentifier codeTableItemIdentifier =
        new CodeTableItemIdentifier(COMMUNICATIONMETHOD.TABLENAME,
          recordedCommDetails.methodTypeCode);

      concernRoleKey.concernRoleID =
        communicationDetails.correspondentConcernRoleID;

      final ConcernRoleNameDetails correspondentConcernRoleName =
        concernRoleObj.readConcernRoleName(concernRoleKey);

      final LocalisableString description =
        new LocalisableString(BPOCASEEVENTS.COMMUNICATION_SENT)
          .arg(codeTableItemIdentifier)
          .arg(correspondentConcernRoleName.concernRoleName);

      caseTransactionLogProvider.get().recordCaseTransaction(
        CASETRANSACTIONEVENTS.COMMUNICATION_SENT, description,
        recordedCommDetails.caseID, concernRoleCommKeyOut.communicationID);

    }
    // END, CR00098859
    // END, CR00022728
    // END, CR00022584
    // BEGIN, CR00237551, NS
    if (recordedCommDetails.caseID != 0) {

      final CaseParticipantRoleDetails caseParticipantRoleDetails =
        new CaseParticipantRoleDetails();

      caseParticipantRoleDetails.dtls.caseID = recordedCommDetails.caseID;
      caseParticipantRoleDetails.dtls.fromDate = Date.getCurrentDate();
      caseParticipantRoleDetails.dtls.typeCode =
        CASEPARTICIPANTROLETYPE.CORRESPONDENT;
      caseParticipantRoleDetails.dtls.caseParticipantRoleID =
        recordedCommDetails.caseParticipantRoleID;
      caseParticipantRoleDetails.dtls.participantRoleID =
        recordedCommDetails.correspondentParticipantRoleID;

      addCaseParticipant(caseParticipantRoleDetails);
    }

    // START: Task 87597: DEV: Raising a task for person when created record
    // communication record - TASK-05 Implementation
    /**
     * Enquiry - Foreign Benefits
     * Enquiry - Foreign Benefits - Requires Attention
     * Enquiry - Canadian Benefits
     * Enquiry - Canadian Benefits - Requires Attention
     * Enquiry - Address Change
     * Enquiry - Other
     * Enquiry - Processing Status
     * REU
     */
    final String listOfCommunicationTypCdsStr = Configuration
      .getProperty(EnvVars.BDM_RECORD_COMMUNICATION_LIST_OF_COMMTYPECODES);

    BDMConcernRoleCommRMKey bdmCncrCmmRmKey = null;

    if (recordedCommDetails.clientParticipantRoleID != CuramConst.kLongZero
      && recordedCommDetails.caseID == CuramConst.kLongZero
      && listOfCommunicationTypCdsStr
        .indexOf(recordedCommDetails.communicationTypeCode) != -1
      && recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.INCOMING)) {
      bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
      bdmCncrCmmRmKey.communicationDate =
        recordedCommDetails.communicationDate;
      bdmCncrCmmRmKey.concernRoleID =
        recordedCommDetails.clientParticipantRoleID;
      bdmCncrCmmRmKey.communicationTypeCode =
        recordedCommDetails.communicationTypeCode;
      bdmCncrCmmRmKey.communicationID = concernRoleCommKeyOut.communicationID;
      enactPersonRecCommNotificationWorkFlow(bdmCncrCmmRmKey);
    }
    // END: Task 87597: DEV: Raising a task for person when created record
    // communication record - TASK-05 Implementation

    // START: Task 88742: DEV: Raising a task for case when created record
    // communication record - TASK-05 Implementation
    else if (recordedCommDetails.caseID != CuramConst.kLongZero
      && listOfCommunicationTypCdsStr
        .indexOf(recordedCommDetails.communicationTypeCode) != -1
      && recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.INCOMING)) {

      bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
      bdmCncrCmmRmKey.communicationDate =
        recordedCommDetails.communicationDate;
      bdmCncrCmmRmKey.communicationID = concernRoleCommKeyOut.communicationID;
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      CaseHeaderDtls headerDtls = null;
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = recordedCommDetails.caseID;
      headerDtls =
        CaseHeaderFactory.newInstance().read(nfIndicator, caseHeaderKey);
      bdmCncrCmmRmKey.caseID = recordedCommDetails.caseID;
      bdmCncrCmmRmKey.concernRoleID = headerDtls.concernRoleID;
      bdmCncrCmmRmKey.communicationTypeCode =
        recordedCommDetails.communicationTypeCode;
      enactFecRecCommNotificationWorkFlow(bdmCncrCmmRmKey);
    }
    // END: Task 88742: DEV: Raising a task for case when created record
    // communication record - TASK-05 Implementation

    // START: Task 96970: DEV: Implement Task 10 and 11 where created
    // communication record
    else if (recordedCommDetails.caseID != CuramConst.kLongZero
      && recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.INCOMING)
      && (recordedCommDetails.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
        || recordedCommDetails.communicationTypeCode
          .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))) {

      bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
      bdmCncrCmmRmKey.communicationDate =
        recordedCommDetails.communicationDate;
      bdmCncrCmmRmKey.communicationID = concernRoleCommKeyOut.communicationID;
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      CaseHeaderDtls headerDtls = null;
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = recordedCommDetails.caseID;
      headerDtls =
        CaseHeaderFactory.newInstance().read(nfIndicator, caseHeaderKey);
      bdmCncrCmmRmKey.caseID = recordedCommDetails.caseID;
      bdmCncrCmmRmKey.concernRoleID = headerDtls.concernRoleID;
      bdmCncrCmmRmKey.communicationTypeCode =
        recordedCommDetails.communicationTypeCode;
      enactFecRecCommNotificationWorkFlow(bdmCncrCmmRmKey);
    } else if (recordedCommDetails.clientParticipantRoleID != CuramConst.kLongZero
      && recordedCommDetails.caseID == CuramConst.kLongZero
      && recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.INCOMING)
      && (recordedCommDetails.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
        || recordedCommDetails.communicationTypeCode
          .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))) {
      bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
      bdmCncrCmmRmKey.communicationDate =
        recordedCommDetails.communicationDate;
      bdmCncrCmmRmKey.concernRoleID =
        recordedCommDetails.clientParticipantRoleID;
      bdmCncrCmmRmKey.communicationTypeCode =
        recordedCommDetails.communicationTypeCode;
      bdmCncrCmmRmKey.communicationID = concernRoleCommKeyOut.communicationID;
      enactPersonRecCommNotificationWorkFlow(bdmCncrCmmRmKey);
    }
    // END: Task 96970: DEV: Implement Task 10 and 11 where created
    // communication record

    // START: Task 89604: DEV: Manage Privacy Request - TASK-08 Implementation
    // Task -13
    final CommunicationIDKey communicationIDKey = new CommunicationIDKey();
    communicationIDKey.communicationID =
      concernRoleCommKeyOut.communicationID;
    BDMManagePrivacyRequestFactory.newInstance()
      .processTaskForPrivacyRequest(communicationIDKey, recordedCommDetails);
    // END: Task 89604: DEV: Manage Privacy Request - TASK-08 Implementation

    // START - Bug 97847
    if (recordedCommDetails.caseID != CuramConst.kLongZero
      && recordedCommDetails.communicationDirection
        .equals(COMMUNICATIONDIRECTION.OUTGOING)
      && recordedCommDetails.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.INTERIM_APP_OUTGOING)) {

      final curam.ca.gc.bdm.entity.intf.BDMForeignApplication bdmfa =
        BDMForeignApplicationFactory.newInstance();
      final BDMCaseKey bdmCaseKey = new BDMCaseKey();
      bdmCaseKey.caseID = recordedCommDetails.caseID;
      final BDMFAList bdmfaList = BDMMaintainForeignEngagementCaseFactory
        .newInstance().listForeignApplications(bdmCaseKey);

      for (final BDMFADetails bdmfaDetails : bdmfaList.bdmFADetails) {

        final BDMForeignApplicationDtls bdmfaDtls =
          new BDMForeignApplicationDtls();
        final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
          new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
        bdmfaKey.fApplicationID = bdmfaDetails.fApplicationID;
        bdmfaDtls.assign(bdmfaDetails);
        bdmfaDtls.status = BDMFOREIGNAPPSTATUS.COMPLETED;
        bdmfa.modify(bdmfaKey, bdmfaDtls);
      }

      // Bug Fix for 98081
      final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
      bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
      bdmCncrCmmRmKey.wdoName = BDMConstants.kBdmTaskCreateDetails;
      bdmCncrCmmRmKey.caseID = recordedCommDetails.caseID;
      bdmCncrCmmRmKey.concernRoleID =
        recordedCommDetails.correspondentConcernRoleID;
      bdmCncrCmmRmKey.communicationTypeCode =
        recordedCommDetails.communicationTypeCode;

      final BDMRecordCommunicationsTaskDetailsList bdmRecordCommunicationsTaskDetailsList =
        bdmfeCase.getFECaseListOfTaskForCommunication(bdmCncrCmmRmKey);

      for (final BDMRecordCommunicationsTaskDetails bdmRecordCommunicationsTaskDetails : bdmRecordCommunicationsTaskDetailsList.dtls) {
        if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.gkZero) {
          final Event closeTaskEvent = new Event();
          closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
          closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
          closeTaskEvent.primaryEventData =
            bdmRecordCommunicationsTaskDetails.taskID;
          EventService.raiseEvent(closeTaskEvent);
        }
      }
    }

    // START - Bug 97847

    return concernRoleCommKeyOut;

  }

  /**
   * Task 87598: DEV: Raising a task for case when created record communication
   * record.
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void enactFecRecCommNotificationWorkFlow(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

    bdmCncrCmmRmKey.wdoName = BDMConstants.kBdmTaskCreateDetails;

    final BDMRecordCommunicationsTaskDetailsList bdmRecordCommunicationsTaskDetailsList =
      bdmfeCase.getFECaseListOfTaskForCommunication(bdmCncrCmmRmKey);

    BDMRecordCommunicationsTaskDetails bdmRecordCommunicationsTaskDetails =
      null;

    final int taskListSize =
      bdmRecordCommunicationsTaskDetailsList.dtls.size();

    boolean isSameCommTypInd = false;
    Long existingTaskID = 0L;

    for (int i = 0; i < taskListSize; i++) {

      bdmRecordCommunicationsTaskDetails =
        new BDMRecordCommunicationsTaskDetails();

      bdmRecordCommunicationsTaskDetails =
        bdmRecordCommunicationsTaskDetailsList.dtls.item(i);

      if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.kLongZero
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot
          .indexOf(bdmCncrCmmRmKey.caseID + CuramConst.gkEmpty) != -1
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot.indexOf(
          bdmCncrCmmRmKey.communicationTypeCode + CuramConst.gkEmpty) != -1) {

        isSameCommTypInd = true;
        existingTaskID = bdmRecordCommunicationsTaskDetails.taskID;
        break;
      }
    }

    if (taskListSize == CuramConst.gkZero || !isSameCommTypInd) {
      createFECaseRecCommNotificationTask(bdmCncrCmmRmKey);
    } else if (isSameCommTypInd) {

      final curam.util.workflow.struct.TaskKey taskKey =
        new curam.util.workflow.struct.TaskKey();

      final Task taskObj = TaskFactory.newInstance();

      taskKey.taskID = existingTaskID;

      nfIndicator = new NotFoundIndicator();

      taskObj.read(nfIndicator, taskKey);

      if (!nfIndicator.isNotFound()) {

        // TaskManagement.readDeadlineDetails
        final TaskManagementTaskKey taskManagementTaskKey =
          new TaskManagementTaskKey();

        taskManagementTaskKey.taskKey.taskID = existingTaskID;

        TaskDeadlineDetails taskDeadlineDetails = new TaskDeadlineDetails();

        taskDeadlineDetails = TaskManagementFactory.newInstance()
          .readDeadlineDetails(taskManagementTaskKey);

        final DateTime cmmDtTmInput =
          new DateTime(bdmCncrCmmRmKey.communicationDate.getDateTime());

        final DateTime newDeadLine2Days = cmmDtTmInput.addTime(Integer
          .parseInt(Configuration.getProperty(
            EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_TWO_TASK_DEADLINE))
          * BDMConstants.kBdm24Hours, 0, 0);

        final DateTime newDeadLine1Day = cmmDtTmInput.addTime(Integer
          .parseInt(Configuration.getProperty(
            EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_THREE_TASK_DEADLINE))
          * BDMConstants.kBdm24Hours, 0, 0);

        // BUG 103035 Start

        if (!(bdmCncrCmmRmKey.communicationTypeCode
          .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
          || bdmCncrCmmRmKey.communicationTypeCode
            .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))) {

          // BUG 103035 END

          final BDMEscalationLevelDtls bdmCurrentEscalationLevelDtls =
            bdmUtil.getCurrentEscalationLevel(existingTaskID);

          if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_1
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine2Days;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_2
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_3
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_4
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_5
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_6
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_7
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_8
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          }
        }

        taskDeadlineDetails.deadlineDetails.taskID = existingTaskID;
        taskDeadlineDetails.deadlineDetails.versionNo =
          taskDeadlineDetails.deadlineDetails.versionNo;

        // Modify the existing record: This is an existing record so just
        // update the details.
        modifyDeadline(taskDeadlineDetails.deadlineDetails.taskID,
          taskDeadlineDetails.deadlineDetails.deadline, CuramConst.gkEmpty,
          taskDeadlineDetails.deadlineDetails.versionNo);

        if (!(bdmCncrCmmRmKey.communicationTypeCode
          .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
          || bdmCncrCmmRmKey.communicationTypeCode
            .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))) {
          modifyEscalationLevelAndBizObjAssoc(bdmCncrCmmRmKey,
            taskDeadlineDetails);
        }
      }
    }
  }

  private void modifyEscalationLevelAndBizObjAssoc(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey,
    final TaskDeadlineDetails taskDeadlineDetails)
    throws AppException, InformationalException {

    final BizObjAssociation bizObjAssociation =
      BizObjAssociationFactory.newInstance();
    BizObjAssociationDtls bizObjAssociationDtls = null;
    final TaskKey taskKey2 = new TaskKey();
    taskKey2.taskID = taskDeadlineDetails.deadlineDetails.taskID;
    final BizObjAssociationDtlsList bizObjAssociationDtlsList =
      bizObjAssociation.searchByTaskID(taskKey2);

    final int bzObjAssListSize = bizObjAssociationDtlsList.dtls.size();

    for (int p = 0; p < bzObjAssListSize; p++) {

      bizObjAssociationDtls = new BizObjAssociationDtls();
      bizObjAssociationDtls = bizObjAssociationDtlsList.dtls.item(p);

      if (bizObjAssociationDtls.bizObjectType
        .equals(BUSINESSOBJECTTYPE.BDMCOMMUNICATION)
        && bizObjAssociationDtls.bizObjectID != CuramConst.kLongZero) {

        final BDMEscalationLevel bdmEscalationLevel =
          BDMEscalationLevelFactory.newInstance();

        final BDMEscalationLevelKeyStruct1 bdmEscalationLevelKeyStruct1 =
          new BDMEscalationLevelKeyStruct1();

        bdmEscalationLevelKeyStruct1.communicationID =
          bizObjAssociationDtls.bizObjectID;

        final BDMEscalationLevelDtlsStruct2 bdmEscalationLevelDtlsStruct2 =
          bdmEscalationLevel
            .readByCommunicationID(bdmEscalationLevelKeyStruct1);

        final BDMEscalationLevelKey bdmEscalationLevelKey =
          new BDMEscalationLevelKey();

        bdmEscalationLevelKey.bdmEscalationLevelID =
          bdmEscalationLevelDtlsStruct2.bdmEscalationLevelID;

        final BDMEscalationLevelDtls bdmEscalationLevelDtls =
          bdmEscalationLevel.read(bdmEscalationLevelKey);

        final String nxtEscalationLevel = BDMUtil
          .getNextEscalationLevel(bdmEscalationLevelDtls.escalationLevel);

        BDMUtil.createTaskEscalationLevel(bdmCncrCmmRmKey.communicationID,
          nxtEscalationLevel);

        final BizObjAssociationKey bizObjAssociationKey =
          new BizObjAssociationKey();

        bizObjAssociationKey.bizObjAssocID =
          bizObjAssociationDtls.bizObjAssocID;

        final BizObjectID bizObjectID = new BizObjectID();

        bizObjectID.bizObjectID = bdmCncrCmmRmKey.communicationID;

        bizObjAssociation.modifyBusinessObjectID(bizObjAssociationKey,
          bizObjectID);

        break;
      }
    }
  }

  /**
   * This private method is used for raising a task for a specific type of
   * record communication on the foreign engagement case.
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void createFECaseRecCommNotificationTask(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

    if (!(bdmCncrCmmRmKey.communicationTypeCode
      .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
      || bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))) {
      BDMUtil.createTaskEscalationLevel(bdmCncrCmmRmKey.communicationID,
        BDMESCALATIONLEVELS.ESCALATION_LEVEL_1);
    }

    // TASK 96970
    // When the record communication of type " MP Enquiry" OR "Ministerial
    // Enquiry" is created and the direction is "incoming"
    // then add Urgent flag to the case with the type "MP Enquiry" and set the
    // start date as receive date
    // recorded on the Record communication.

    if (bdmCncrCmmRmKey.communicationTypeCode
      .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
      || bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
      final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
        curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
          .newInstance();

      final BDMCaseUrgentFlagDtls bdmCaseUrgentFlagDtls =
        new BDMCaseUrgentFlagDtls();

      bdmCaseUrgentFlagDtls.bdmCaseUrgentFlagID = UniqueID.nextUniqueID();
      bdmCaseUrgentFlagDtls.caseID = bdmCncrCmmRmKey.caseID;
      bdmCaseUrgentFlagDtls.recordStatus = RECORDSTATUS.NORMAL;
      bdmCaseUrgentFlagDtls.startDate = bdmCncrCmmRmKey.communicationDate;
      if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)) {
        bdmCaseUrgentFlagDtls.type = BDMURGENTFLAGTYPE.MPENQUIRY;
      } else if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
        bdmCaseUrgentFlagDtls.type = BDMURGENTFLAGTYPE.MINISTERIAL_ENQUIRY;
      }
      bdmCaseUrgentFlagDtls.versionNo = 1;

      enBDMCaseUrgentFlag.insert(bdmCaseUrgentFlagDtls);
    }

    final List<Object> enactmentStructs = new ArrayList<>();
    final BDMFECTaskCreateDetails bfmFECTaskCrtDetails =
      new BDMFECTaskCreateDetails();

    bfmFECTaskCrtDetails.concernRoleID = bdmCncrCmmRmKey.concernRoleID;
    bfmFECTaskCrtDetails.caseID = bdmCncrCmmRmKey.caseID;
    bfmFECTaskCrtDetails.communicationID = bdmCncrCmmRmKey.communicationID;
    bfmFECTaskCrtDetails.priority = TASKPRIORITY.NORMAL;
    bfmFECTaskCrtDetails.communicationTypeCode =
      bdmCncrCmmRmKey.communicationTypeCode;

    CaseHeaderDtls headerDtls = null;

    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();

    final curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();

    final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
      new BDMWorkQueueCountryLinkKey();

    final String currentLoggedInUser = TransactionInfo.getProgramUser();

    boolean fecCaseFoundInd = false;

    headerDtls = new CaseHeaderDtls();

    if (bdmCncrCmmRmKey.caseID != CuramConst.kLongZero) {

      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = bdmCncrCmmRmKey.caseID;
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
            orallanuage = BDMConstants.gkLocaleFR;
          }

        } else if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredWrittenLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleFR;
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

    // START: Bug 114376 - Task 10 and 11
    if (TransactionInfo.getProgramLocale()
      .equalsIgnoreCase(BDMConstants.gkLocaleUpperEN)) {
      if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkMPEnquiry_EN;
      } else if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkMPEnquiry_EN;
      } else {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkEnquiry;
      }
    } else if (TransactionInfo.getProgramLocale()
      .equalsIgnoreCase(BDMConstants.gkLocaleUpperFR)) {
      if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkMPEnquiry_FR;
      } else if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc =
          BDMConstants.gkMinistrialEnquiry_FR;
      } else {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkEnquiry_FR;
      }
    }
    // END: Bug 114376 - Task 10 and 11

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = bfmFECTaskCrtDetails.concernRoleID;

    final String strConcernRoleFullName =
      ConcernRoleFactory.newInstance().read(concernRoleKey).concernRoleName;

    final String fecTypeDesc = CodetableUtil.getCodetableDescription(
      PRODUCTCATEGORY.TABLENAME, headerDtls.integratedCaseType);

    String strIdentificationRefernce = CuramConst.gkEmpty;

    // Get person's refernce number
    strIdentificationRefernce =
      bdmUtil.getPersonReferenceNumber(bdmCncrCmmRmKey.concernRoleID);

    // Get original person SIN Number
    final String personSINNumber =
      bdmUtil.getSINNumberForPerson(bfmFECTaskCrtDetails.concernRoleID);

    if (!StringUtil.isNullOrEmpty(personSINNumber)) {
      strIdentificationRefernce = CuramConst.gkEmpty;
      strIdentificationRefernce = personSINNumber;
    }

    bfmFECTaskCrtDetails.communicationDate =
      bdmCncrCmmRmKey.communicationDate;
    bfmFECTaskCrtDetails.oralWrittenLanguage = oralWrittenLanguage.toString();
    // Start - Fix for Bug 114911
    bfmFECTaskCrtDetails.commTypCdDesc =
      CodetableUtil.getNonCachedCodetableItem(COMMUNICATIONTYPE.TABLENAME,
        bdmCncrCmmRmKey.communicationTypeCode,
        BDMConstants.gkLocaleLowerFR).description
        + BDMConstants.kForwardSlash
        + CodetableUtil.getNonCachedCodetableItem(COMMUNICATIONTYPE.TABLENAME,
          bdmCncrCmmRmKey.communicationTypeCode,
          BDMConstants.gkLocaleLowerEN).description;
    // End - Fix for Bug 114911

    bfmFECTaskCrtDetails.strConcernRoleFullName = strConcernRoleFullName;
    bfmFECTaskCrtDetails.strIdentificationRefernce =
      strIdentificationRefernce;
    bfmFECTaskCrtDetails.fecTypeDesc = fecTypeDesc;
    bfmFECTaskCrtDetails.feCaseReference = headerDtls.caseReference;

    enactmentStructs.add(bfmFECTaskCrtDetails);

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();

    Date commDtForDeadLineCalc = new Date();
    commDtForDeadLineCalc = bdmCncrCmmRmKey.communicationDate;

    int taskDeadline = 0;
    if (bdmCncrCmmRmKey.communicationTypeCode
      .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
      || bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
      taskDeadline = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_TWO_TASK_DEADLINE));
    } else {
      taskDeadline = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_ONE_TASK_DEADLINE));
    }

    final long inputDateTimeInMills =
      commDtForDeadLineCalc.addDays(taskDeadline).asLong();

    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    final long kProcessInstanceID =
      EnactmentService.startProcessInV3CompatibilityMode(
        BDMConstants.kBDMFECRecordCommunicationNotificationTask,
        enactmentStructs);

    bdmUtil.addBDMTaskForRecCommCommType(kProcessInstanceID,
      bdmCncrCmmRmKey.communicationTypeCode);

  }

  /**
   * This private method is used for raising a task for a specific type of
   * record communication on the person profile.
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  private void createPersonRecCommNotificationTask(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

    if (!(bdmCncrCmmRmKey.communicationTypeCode
      .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
      || bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))) {
      BDMUtil.createTaskEscalationLevel(bdmCncrCmmRmKey.communicationID,
        BDMESCALATIONLEVELS.ESCALATION_LEVEL_1);
    }

    final List<Object> enactmentStructs = new ArrayList<>();
    final BDMFECTaskCreateDetails bfmFECTaskCrtDetails =
      new BDMFECTaskCreateDetails();

    bfmFECTaskCrtDetails.concernRoleID = bdmCncrCmmRmKey.concernRoleID;
    bfmFECTaskCrtDetails.communicationID = bdmCncrCmmRmKey.communicationID;
    bfmFECTaskCrtDetails.priority = TASKPRIORITY.NORMAL;
    bfmFECTaskCrtDetails.communicationTypeCode =
      bdmCncrCmmRmKey.communicationTypeCode;

    final curam.core.facade.intf.CaseHeader caseHeaderFacade =
      curam.core.facade.fact.CaseHeaderFactory.newInstance();
    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.dtls.concernRoleID =
      bdmCncrCmmRmKey.concernRoleID;
    concernRoleIDStatusCodeKey.dtls.statusCode = CASESTATUS.OPEN;

    final CaseHeaderDtlsList caseHeaderDtlsList =
      caseHeaderFacade.searchByConcernRoleID(concernRoleIDStatusCodeKey);

    final int cseHdrListSize = caseHeaderDtlsList.dtlsList.dtls.size();

    CaseHeaderDtls headerDtls = null;

    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();

    final curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();

    final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
      new BDMWorkQueueCountryLinkKey();

    final String currentLoggedInUser = TransactionInfo.getProgramUser();

    boolean fecCaseFoundInd = false;

    for (int i = 0; i < cseHdrListSize; i++) {

      headerDtls = new CaseHeaderDtls();

      headerDtls = caseHeaderDtlsList.dtlsList.dtls.item(i);

      if (headerDtls.caseTypeCode.equals(CASECATEGORY.INTEGRATEDCASE)) {

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
        break;
      }
    }

    if (bfmFECTaskCrtDetails.workQueueID == CuramConst.kLongZero) {

      bfmFECTaskCrtDetails.workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);

    }

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

    String strIdentificationRefernce = CuramConst.gkEmpty;

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
            orallanuage = BDMConstants.gkLocaleFR;
          }

        } else if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredWrittenLanguage)) {

          if (BDMLANGUAGE.ENGLISHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleUpperEN;
          } else if (BDMLANGUAGE.FRENCHL
            .equals(dynamicEvidenceDataAttributeDtls.value)) {
            writtenlanuage = BDMConstants.gkLocaleFR;
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

    // START: Bug 114376 - Task 10 and 11
    if (TransactionInfo.getProgramLocale()
      .equalsIgnoreCase(BDMConstants.gkLocaleUpperEN)) {
      if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkMPEnquiry_EN;
      } else if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc =
          BDMConstants.gkMinistrialEnquiry_EN;
      } else {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkEnquiry;
      }
    } else if (TransactionInfo.getProgramLocale()
      .equalsIgnoreCase(BDMConstants.gkLocaleUpperFR)) {
      if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkMPEnquiry_FR;
      } else if (bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
        bfmFECTaskCrtDetails.staticTextDesc =
          BDMConstants.gkMinistrialEnquiry_FR;
      } else {
        bfmFECTaskCrtDetails.staticTextDesc = BDMConstants.gkEnquiry_FR;
      }
    }
    // END: Bug 114376 - Task 10 and 11

    // Get person's reference number
    strIdentificationRefernce =
      bdmUtil.getPersonReferenceNumber(bdmCncrCmmRmKey.concernRoleID);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = bfmFECTaskCrtDetails.concernRoleID;

    final String strConcernRoleFullName =
      ConcernRoleFactory.newInstance().read(concernRoleKey).concernRoleName;

    // Get original person SIN Number
    final String personSINNumber =
      bdmUtil.getSINNumberForPerson(bfmFECTaskCrtDetails.concernRoleID);

    if (!StringUtil.isNullOrEmpty(personSINNumber)) {
      strIdentificationRefernce = CuramConst.gkEmpty;
      strIdentificationRefernce = personSINNumber;
    }

    bfmFECTaskCrtDetails.communicationDate =
      bdmCncrCmmRmKey.communicationDate;
    bfmFECTaskCrtDetails.oralWrittenLanguage = oralWrittenLanguage.toString();
    // Start - Fix for Bug 114911
    bfmFECTaskCrtDetails.commTypCdDesc =
      CodetableUtil.getNonCachedCodetableItem(COMMUNICATIONTYPE.TABLENAME,
        bdmCncrCmmRmKey.communicationTypeCode,
        BDMConstants.gkLocaleLowerFR).description
        + " / "
        + CodetableUtil.getNonCachedCodetableItem(COMMUNICATIONTYPE.TABLENAME,
          bdmCncrCmmRmKey.communicationTypeCode,
          BDMConstants.gkLocaleLowerEN).description;
    // End - Fix for Bug 114911
    bfmFECTaskCrtDetails.strConcernRoleFullName = strConcernRoleFullName;
    bfmFECTaskCrtDetails.strIdentificationRefernce =
      strIdentificationRefernce;

    enactmentStructs.add(bfmFECTaskCrtDetails);

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();

    Date commDtForDeadLineCalc = new Date();
    commDtForDeadLineCalc = bdmCncrCmmRmKey.communicationDate;

    int taskDeadline = 0;
    if (bdmCncrCmmRmKey.communicationTypeCode
      .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
      || bdmCncrCmmRmKey.communicationTypeCode
        .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
      taskDeadline = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_TWO_TASK_DEADLINE));
    } else {
      taskDeadline = Integer.parseInt(Configuration.getProperty(
        EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_ONE_TASK_DEADLINE));
    }

    final long inputDateTimeInMills =
      commDtForDeadLineCalc.addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    final long kProcessInstanceID =
      EnactmentService.startProcessInV3CompatibilityMode(
        BDMConstants.kBDMPersonRecordCommunicationNotificationTask,
        enactmentStructs);

    bdmUtil.addBDMTaskForRecCommCommType(kProcessInstanceID,
      bdmCncrCmmRmKey.communicationTypeCode);

  }

  /**
   * Task 87597: DEV: Raising a task for person when created record
   * communication record
   *
   * @param bdmCncrCmmRmKey
   * @throws AppException
   * @throws InformationalException
   */
  @SuppressWarnings("unused")
  private void enactPersonRecCommNotificationWorkFlow(
    final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.fec.intf.BDMFECase bdmfeCase =
      BDMFECaseFactory.newInstance();

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

    bdmCncrCmmRmKey.wdoName = BDMConstants.kBdmTaskCreateDetails;

    final BDMRecordCommunicationsTaskDetailsList bdmRecordCommunicationsTaskDetailsList =
      bdmfeCase.getListOfTaskForCommunication(bdmCncrCmmRmKey);

    BDMRecordCommunicationsTaskDetails bdmRecordCommunicationsTaskDetails =
      null;

    final int taskListSize =
      bdmRecordCommunicationsTaskDetailsList.dtls.size();

    boolean isSameCommTypInd = false;
    Long existingTaskID = 0L;

    for (int i = 0; i < taskListSize; i++) {

      bdmRecordCommunicationsTaskDetails =
        new BDMRecordCommunicationsTaskDetails();

      bdmRecordCommunicationsTaskDetails =
        bdmRecordCommunicationsTaskDetailsList.dtls.item(i);

      if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.kLongZero
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot
          .indexOf(bdmCncrCmmRmKey.concernRoleID + CuramConst.gkEmpty) != -1
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot.indexOf(
          bdmCncrCmmRmKey.communicationTypeCode + CuramConst.gkEmpty) != -1) {

        isSameCommTypInd = true;
        existingTaskID = bdmRecordCommunicationsTaskDetails.taskID;
        break;
      }
    }

    if (taskListSize == CuramConst.gkZero || !isSameCommTypInd) {
      createPersonRecCommNotificationTask(bdmCncrCmmRmKey);
    } else if (isSameCommTypInd) {
      final TaskKey taskKey = new TaskKey();

      final Task taskObj = TaskFactory.newInstance();

      final boolean foundCommunicationTypeCdInd = false;

      taskKey.taskID = existingTaskID;

      nfIndicator = new NotFoundIndicator();

      final TaskDtls taskDtls = taskObj.read(nfIndicator, taskKey);

      if (!nfIndicator.isNotFound()) {

        // TaskManagement.readDeadlineDetails
        final TaskManagementTaskKey taskManagementTaskKey =
          new TaskManagementTaskKey();

        taskManagementTaskKey.taskKey.taskID = existingTaskID;

        TaskDeadlineDetails taskDeadlineDetails = new TaskDeadlineDetails();

        taskDeadlineDetails = TaskManagementFactory.newInstance()
          .readDeadlineDetails(taskManagementTaskKey);

        final DateTime cmmDtTmInput =
          new DateTime(bdmCncrCmmRmKey.communicationDate.getDateTime());

        final DateTime newDeadLine2Days = cmmDtTmInput.addTime(Integer
          .parseInt(Configuration.getProperty(
            EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_TWO_TASK_DEADLINE))
          * BDMConstants.kBdm24Hours, 0, 0);

        final DateTime newDeadLine1Day = cmmDtTmInput.addTime(Integer
          .parseInt(Configuration.getProperty(
            EnvVars.BDM_RECORD_COMMUNICATION_ESCALATION_THREE_TASK_DEADLINE))
          * BDMConstants.kBdm24Hours, 0, 0);
        // START : BUG 113123 : Escalation level not created for MP Inquiry and
        // Ministerial inquiry.
        if (!(bdmCncrCmmRmKey.communicationTypeCode
          .equalsIgnoreCase(COMMUNICATIONTYPE.MP_ENQUIRY)
          || bdmCncrCmmRmKey.communicationTypeCode
            .equalsIgnoreCase(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY))) {
          final BDMEscalationLevelDtls bdmCurrentEscalationLevelDtls =
            bdmUtil.getCurrentEscalationLevel(existingTaskID);
          // check for escalation level and set the dead line
          if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_1
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine2Days;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_2
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_3
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_4
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_5
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_6
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_7
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_8
            .equals(bdmCurrentEscalationLevelDtls.escalationLevel)) {

            taskDeadlineDetails.deadlineDetails.deadline = newDeadLine1Day;

          }
          taskDeadlineDetails.deadlineDetails.taskID = existingTaskID;
          taskDeadlineDetails.deadlineDetails.versionNo =
            taskDeadlineDetails.deadlineDetails.versionNo;

          // Modify the existing record: This is an existing record so just
          // update the details.
          modifyDeadline(taskDeadlineDetails.deadlineDetails.taskID,
            taskDeadlineDetails.deadlineDetails.deadline, CuramConst.gkEmpty,
            taskDeadlineDetails.deadlineDetails.versionNo);
          modifyEscalationLevelAndBizObjAssoc(bdmCncrCmmRmKey,
            taskDeadlineDetails);
        } // END IF END : BUG 113123
      }
    }
  }

  private void modifyDeadline(final long taskID, final DateTime deadline,
    final String comments, final int versionNo)
    throws AppException, InformationalException {

    final String userName = TransactionInfo.getProgramUser();

    // Validate the deadline details.
    validateModifyDeadline(taskID, userName, deadline);

    final WorkflowDeadlineAdmin workflowDeadlineAdminObj =
      WorkflowDeadlineAdminFactory.newInstance();
    final TaskHistoryAdmin taskHistoryAdminObj =
      TaskHistoryAdminFactory.newInstance();
    WorkflowDeadlineInfo workflowDeadlineDtls = new WorkflowDeadlineInfo();

    // Store the original value of the deadline as this will be used in the
    // task history record that is created as a result of this action.
    DateTime originalDeadline = DateTime.kZeroDateTime;

    try {
      workflowDeadlineDtls =
        workflowDeadlineAdminObj.readDeadlineDetailsByTaskID(taskID);

      // Modify the existing record.
      originalDeadline = workflowDeadlineDtls.deadlineTime;
      // This is an existing record so just update the details.
      workflowDeadlineAdminObj.modifyDeadline(taskID, deadline,
        workflowDeadlineDtls.versionNo);
    } catch (final AppException e) {
      // If there is no deadline specified for this task, then throw the
      // appropriate exception as this function cannot be used to create a new
      // deadline.
      throw new AppException(
        BPOTASKMANAGEMENT.ERR_TASK_NO_DEADLINE_CANNOT_CREATE_NEW_DEADLINE);
    }

    // Add the Task History record to record this action.
    taskHistoryAdminObj.create(taskID, DateTime.getCurrentDateTime(),
      TASKCHANGETYPE.DEADLINECHANGED, comments,
      Long.toString(deadline.asLong()),
      originalDeadline.equals(DateTime.kZeroDateTime) ? CuramConst.gkEmpty
        : Long.toString(originalDeadline.asLong()),
      userName);
  }

  private void validateModifyDeadline(final long taskID,
    final String userName, final DateTime deadline)
    throws AppException, InformationalException {

    final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();

    checkMaintainSecurity(taskID);

    // The deadline must be specified.
    if (deadline == null || deadline.equals(DateTime.kZeroDateTime)) {
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOTASKMANAGEMENT.ERR_TASK_FV_DEADLINE_EMPTY),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    final TaskDetailsWithoutSnapshot taskDtls =
      taskAdminObj.readDetails(taskID);

    // The deadline of a task cannot be modified if the task is closed.
    if (TASKSTATUS.CLOSED.equals(taskDtls.status)) {
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(
            BPOTASKMANAGEMENT.ERR_TASK_CANNOT_MODIFY_DEADLINE_CLOSED_TASK),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // The deadline date time must be a date time in the future.
    /*
     * if (deadline.before(DateTime.getCurrentDateTime())
     * || deadline.equals(DateTime.getCurrentDateTime())) {
     * curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
     * .throwWithLookup(
     * new AppException(
     * BPOTASKMANAGEMENT.ERR_TASK_FV_DEADLINE_NOT_FUTURE_DATE),
     * curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
     * 1);
     * }
     */

  }

  private void checkMaintainSecurity(final long taskId)
    throws AppException, InformationalException {

    final CaseSecurityCheckKey caseSecurityCheckKey =
      new CaseSecurityCheckKey();
    final ParticipantSecurityCheckKey participantKey =
      new ParticipantSecurityCheckKey();
    // getting Case ID
    final BusinessObjectAssociationAdmin bizObjAssociationAdminObj =
      BusinessObjectAssociationAdminFactory.newInstance();

    final BizObjectAssociationDetailsList bizObjAssociationDtlsList =
      bizObjAssociationAdminObj.searchByTaskID(taskId);

    for (final BizObjectAssociationDetails bizObjAssociationDtls : bizObjAssociationDtlsList.dtls
      .items()) {
      if (bizObjAssociationDtls.bizObjectType
        .equals(BUSINESSOBJECTTYPE.CASE)) {

        // Check security on this case (linkedID)
        caseSecurityCheckKey.caseID = bizObjAssociationDtls.bizObjectID;

        // BEGIN, CR00291090, SG
      } else if (BUSINESSOBJECTTYPE.PERSON
        .equals(bizObjAssociationDtls.bizObjectType)) {
        // END, CR00291090
        participantKey.participantID = bizObjAssociationDtls.bizObjectID;
      }
    }

    // Security variables
    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();

    DataBasedSecurityResult dataBasedSecurityResult;

    // perform case security check
    caseSecurityCheckKey.type = DataBasedSecurity.kMaintainSecurityCheck;

    final SystemUser systemUserObj = SystemUserFactory.newInstance();
    final SystemUserDtls systemUserDtls = systemUserObj.getUserDetails();

    final TaskAssignment taskAssignmentObj =
      TaskAssignmentFactory.newInstance();
    final curam.core.sl.entity.struct.TaskKey taskIDKey =
      new curam.core.sl.entity.struct.TaskKey();

    taskIDKey.taskID = taskId;
    boolean isTaskUser = false;
    final TaskAssignmentDtlsList taskAssignmentDtlsList =
      taskAssignmentObj.searchAssignmentsByTaskID(taskIDKey);

    for (final TaskAssignmentDtls taskAssignmentDtls : taskAssignmentDtlsList.dtls
      .items()) {

      if (systemUserDtls.userName.equals(taskAssignmentDtls.relatedName)) {
        isTaskUser = true;
        break;
      }
    }

    if (!isTaskUser) {

      if (CuramConst.gkZero != caseSecurityCheckKey.caseID) {
        dataBasedSecurityResult =
          dataBasedSecurity.checkCaseSecurity1(caseSecurityCheckKey);

        if (!dataBasedSecurityResult.result) {
          if (dataBasedSecurityResult.readOnly) {
            throw new AppException(
              GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
          } else if (dataBasedSecurityResult.restricted) {
            throw new AppException(
              curam.message.GENERALCASE.ERR_CASESECURITY_CHECK_RIGHTS);
          } else {
            throw new AppException(
              GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
          }
        }
      } else {
        if (CuramConst.gkZero != participantKey.participantID) {
          participantKey.type = LOCATIONACCESSTYPE.MAINTAIN;
          dataBasedSecurityResult =
            dataBasedSecurity.checkParticipantSecurity(participantKey);

          if (!dataBasedSecurityResult.result) {
            throw new AppException(
              GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
          }
        }
      }
    }
  }

  /**
   * Reads a recorded communication.
   *
   * @curam .util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   * @param recordedCommKey
   * Communication ID.
   * @return The record communication details.
   *
   * #DOC_END#
   */
  @Override
  public RecordedCommDetails1
    readRecordedComm(final RecordedCommKey recordedCommKey)
      throws AppException, InformationalException {

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();
    final ConcernRoleCommunication concernRoleCommunicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    ConcernRoleCommunicationDtls concernRoleCommunicationDtls;
    final ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new ConcernRoleCommunicationKey();

    concernRoleCommunicationKey.communicationID =
      recordedCommKey.communicationID;

    concernRoleCommunicationDtls =
      concernRoleCommunicationObj.read(concernRoleCommunicationKey);

    // BEGIN, CR00227859, PM
    performSecurityChecksForRead(concernRoleCommunicationDtls);
    // END, CR00227859

    recordedCommDetails.assign(concernRoleCommunicationDtls);

    // Set the communication direction.
    // Set the communication status to sent or received.
    if (concernRoleCommunicationDtls.communicationStatus
      .equals(COMMUNICATIONSTATUS.SENT)) {

      recordedCommDetails.communicationDirection =
        COMMUNICATIONDIRECTION.OUTGOING;
    } else {

      recordedCommDetails.communicationDirection =
        COMMUNICATIONDIRECTION.INCOMING;
    }

    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID =
      concernRoleCommunicationDtls.correspondentConcernRoleID;

    recordedCommDetails.correspondentConcernRoleType =
      concernRoleObj.readConcernRoleType(concernRoleKey).concernRoleType;

    if (concernRoleCommunicationDtls.caseID != 0) {

      final CaseIDParticipantRoleKey caseIDParticipantRoleKey =
        new CaseIDParticipantRoleKey();

      final curam.core.sl.entity.intf.CaseParticipantRole caseParticipantRoleObj =
        curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance();

      caseIDParticipantRoleKey.caseID = concernRoleCommunicationDtls.caseID;
      caseIDParticipantRoleKey.participantRoleID =
        concernRoleCommunicationDtls.correspondentConcernRoleID;

      recordedCommDetails.caseParticipantRoleID =
        caseParticipantRoleObj.readCaseParticipantRoleID(
          caseIDParticipantRoleKey).caseParticipantRoleID;
    }

    // If email record for concern role exists, read its details.
    if (concernRoleCommunicationDtls.emailAddressID != 0) {

      final EmailAddress emailAddressObj = EmailAddressFactory.newInstance();
      final EmailAddressKey emailAddressKey = new EmailAddressKey();

      emailAddressKey.emailAddressID =
        concernRoleCommunicationDtls.emailAddressID;
      recordedCommDetails.emailAddress =
        emailAddressObj.read(emailAddressKey).emailAddress;

    }

    // If an address record for concern role exists, read its details.
    if (concernRoleCommunicationDtls.addressID != 0) {

      final Address addressObj = AddressFactory.newInstance();
      final AddressKey addressKey = new AddressKey();
      curam.core.struct.AddressDtls addressDtls;
      final OtherAddressData otherAddressData = new OtherAddressData();

      addressKey.addressID = concernRoleCommunicationDtls.addressID;
      addressDtls = addressObj.read(addressKey);

      final OtherAddressData addressDataStr = new OtherAddressData();

      addressDataStr.addressData = addressDtls.addressData;
      addressObj.getLongFormat(addressDataStr);
      recordedCommDetails.formattedAddressData = addressDataStr.addressData;
      otherAddressData.addressData = addressDtls.addressData;

      // Get address line 1 to display on page.
      // BEGIN, CR00296699, ZV
      recordedCommDetails.addressLine1 =
        addressObj.getShortFormat(otherAddressData).addressData;
      // END CR00296699
    }

    // If a phone number record for concern role exists, read its details.
    if (concernRoleCommunicationDtls.phoneNumberID != 0) {

      final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
      final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();
      PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();

      phoneNumberKey.phoneNumberID =
        concernRoleCommunicationDtls.phoneNumberID;

      phoneNumberDtls = phoneNumberObj.read(phoneNumberKey);
      // Task 21121 - BEGIN - Phone number with phone country code (code table
      // value)
      final String phoneCountryCodeDesc =
        getPhoneCountryCodeDesc(phoneNumberKey.phoneNumberID);
      recordedCommDetails.phoneCountryCode = phoneCountryCodeDesc;
      recordedCommDetails.phoneAreaCode = phoneNumberDtls.phoneAreaCode;
      recordedCommDetails.phoneNumber = phoneNumberDtls.phoneNumber;
      recordedCommDetails.phoneExtension = phoneNumberDtls.phoneExtension;
      recordedCommDetails.phoneNumberString =
        recordedCommDetails.phoneCountryCode + kSpace
          + phoneNumberDtls.phoneAreaCode + kSpace
          + phoneNumberDtls.phoneNumber;
      // Task 21121 - END - Phone number with phone country code (code table
      // value)
    }

    // Retrieve attachment details.
    if (concernRoleCommunicationDtls.attachmentInd) {

      final curam.core.intf.Attachment attachmentObj =
        curam.core.fact.AttachmentFactory.newInstance();
      final ReadCommAttachmentIn readCommAttachmentIn =
        new ReadCommAttachmentIn();
      ReadCommAttachmentOut readCommAttachmentOut;

      readCommAttachmentIn.communicationID =
        recordedCommDetails.communicationID;

      // We only want to read an attachment if it is active.
      readCommAttachmentIn.activeAttachmentStatusCode =
        ATTACHMENTSTATUS.ACTIVE;

      // Passing the statusCode to check only for active records.
      readCommAttachmentIn.statusCode = RECORDSTATUS.NORMAL;

      try {
        readCommAttachmentOut =
          attachmentObj.readCommAttachment(readCommAttachmentIn);
      } catch (final RecordNotFoundException e) {
        readCommAttachmentOut = null;
      }

      if (readCommAttachmentOut != null) {

        // If attachment record found, retrieve attachment details.
        recordedCommDetails.attachmentID = readCommAttachmentOut.attachmentID;
        recordedCommDetails.newAttachmentName =
          readCommAttachmentOut.attachmentName;
        recordedCommDetails.attachmentInd = true;

      } else {
        recordedCommDetails.attachmentInd = false;
      }
    }
    return recordedCommDetails;
  }

  private String getPhoneCountryCodeDesc(final long phoneNumberID)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMPhoneNumber bdmphoneNumberObj =
      BDMPhoneNumberFactory.newInstance();
    final ReadBDMPhoneNumberKey readBDMPhoneNumberKey =
      new ReadBDMPhoneNumberKey();
    readBDMPhoneNumberKey.phoneNumberID = phoneNumberID;
    final ReadBDMPhoneNumberDetails readBDMPhoneNumberDetails =
      bdmphoneNumberObj.readBDMPhoneNumber(readBDMPhoneNumberKey);

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniqueKey.code = readBDMPhoneNumberDetails.phoneCountryCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;
    final String phoneCountryCodeDesc = codeTableAdminObj
      .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey).description;
    return phoneCountryCodeDesc;
  }

  void validatePersonNotDeceased(
    final ReadParticipantRoleNameAndTypeDetails readParticipantRoleNameAndTypeDetails,
    final ConcernRoleKey concernRoleKey)
    throws AppException, InformationalException {

    // Check if the client being communicated with is a Person
    if (readParticipantRoleNameAndTypeDetails.concernRoleType
      .equals(CONCERNROLETYPE.PERSON)) {

      // Check the Person's date of death
      final Person personObj = PersonFactory.newInstance();
      final PersonKey personKey = new PersonKey();

      personKey.concernRoleID = concernRoleKey.concernRoleID;
      final PersonDtls personDtls = personObj.read(personKey);

      if (!personDtls.dateOfDeath.isZero()) {

        ValidationManagerFactory.getManager().throwWithLookup(
          new AppException(
            BPOCOMMUNICATION.ERR_COMM_CORRESPONDENT__IS_DECEASED),
          ValidationManagerConst.kSetOne, 1);
      }
    }
  }

  /**
   * This method used to create wizard and
   * save all the correspondence details in it.
   *
   * @param corresDetails
   * @return WizardStateID
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public WizardStateID
    saveCorrespondenceWizard(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    // If third party is selected set the toContactThirdPartyConcernRoleID
    if (!corresDetails.toTPContactConcernRoleIDAndEvidenceIDCommaDelimited
      .isEmpty()) {
      final String[] concernRoleIDAndEvidenceID =
        corresDetails.toTPContactConcernRoleIDAndEvidenceIDCommaDelimited
          .split(CuramConst.gkCommaDelimiter);
      if (concernRoleIDAndEvidenceID.length == 2) {
        corresDetails.toContactThirdPartyConcernRoleID =
          Long.parseLong(concernRoleIDAndEvidenceID[0]);
      }
    }

    // If third party is selected set the ccContactThirdPartyConcernRoleID
    if (!corresDetails.ccTPContactConcernRoleIDAndEvidenceIDCommaDelimited
      .isEmpty()) {
      final String[] concernRoleIDAndEvidenceID =
        corresDetails.ccTPContactConcernRoleIDAndEvidenceIDCommaDelimited
          .split(CuramConst.gkCommaDelimiter);
      if (concernRoleIDAndEvidenceID.length == 2) {
        corresDetails.ccThirdPartyContactConcernRoleID =
          Long.parseLong(concernRoleIDAndEvidenceID[0]);
      }
    }
    // validate the inputs
    validateCorespondenceDetails(corresDetails);

    // validate and populate the mandatory fields for TO client
    setTOClientMandatoryFields(corresDetails);

    // validate and populate the mandatory fields for CC client
    setCCClientMandatoryFields(corresDetails);

    // set the names of the selected TO and CC correspondents
    populateNameDetails(corresDetails);

    final WizardPersistentState wizardObj = new WizardPersistentState();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = wizardObj.create(corresDetails);

    return wizardStateID;
  }

  /**
   * This method is to create a correspondence record in Curam and
   * send the request to CCT. Once received the CCT response data, it
   * updates the created correspondence record with the CCT response data.
   *
   * @param wizardKey
   * @return BDMTemplateDetails
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMTemplateDetails
    createCorrespondence(final BDMCorrespondenceWizardKey wizardKey)
      throws AppException, InformationalException {

    // get the correspondence details from the wizard state
    final WizardPersistentState wizardObj = new WizardPersistentState();

    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizardKey.wizardStateID);

    // validate if submit option is selected
    if (wizardKey.submitOpt.isEmpty()) {
      throw new AppException(BDMBPOCCT.ERR_SUBMIT_OPTION_MUST_BE_ENTERED);
    } else {
      corresDetails.submitOpt = wizardKey.submitOpt;

      final BDMCCTCreateCorrespondenceRequest request =
        new BDMCCTCreateCorrespondenceRequest();

      request.setAutoCloseEditor(false);
      request.setCommunity(
        Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

      request.setDataMapName(BDMConstants.kCCTDataMapName);
      request.setTemplateID(corresDetails.templateID);
      request.setTemplateFullPath(corresDetails.templatePath);

      // insert correspondence record in Curam database
      final long communicationID = insertCorrespondenceDetails(corresDetails);
      corresDetails.communicationID = communicationID;

      final BDMConcernRoleCommunicationKey bdmCCKey =
        new BDMConcernRoleCommunicationKey();
      bdmCCKey.communicationID = corresDetails.communicationID;
      final BDMConcernRoleCommunicationDtls bdmCCDtls =
        BDMConcernRoleCommunicationFactory.newInstance().read(bdmCCKey);
      corresDetails.trackingNumber = bdmCCDtls.trackingNumber;

      try {
        request.setDataXML(getDataXML(corresDetails));
      } catch (final Exception e) {
        throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
      }

      // BUG-111905, Start
      // If SSO is enabled, use the current logged in user as the initial
      // assignee.
      // Else use the default configured user id as the initial assignee.
      final String loggedInUser = TransactionInfo.getProgramUser();
      if (Configuration
        .getBooleanProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED)
        && Configuration
          .getBooleanProperty(EnvVars.ENV_ALTERNATE_LOGIN_ID_ENABLED)) {
        // TASK-119554, Start
        // For the initial assignee name, the samAccountName needs
        // to be set instead of the Curam Users.username
        final BDMUserKey bdmUserKey = new BDMUserKey();
        bdmUserKey.userName = loggedInUser;
        final NotFoundIndicator nfIndicator = new NotFoundIndicator();
        final BDMUserDtls bdmUserDtls =
          BDMUserFactory.newInstance().read(nfIndicator, bdmUserKey);
        // If the user record in not found or if the samAccountName is
        // empty, throw error
        if (nfIndicator.isNotFound()
          || StringUtil.isNullOrEmpty(bdmUserDtls.samAccountName)) {
          Trace.kTopLevelLogger.log(Level.ERROR,
            "samAccountName not found for : " + loggedInUser);
          throw new AppException(
            BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
        }
        request.setInitialAssigneeName(bdmUserDtls.samAccountName);
        // TASK-119554, End
      } else {
        String initialAssignee = Configuration
          .getProperty(EnvVars.BDM_CCT_API_INITIAL_ASSIGNEE_VALUE);
        if (StringUtil.isNullOrEmpty(initialAssignee)) {
          initialAssignee = Configuration
            .getProperty(EnvVars.BDM_CCT_API_INITIAL_ASSIGNEE_VALUE_DEFAULT);
        }
        request.setInitialAssigneeName(initialAssignee);
      }
      // BUG-111905, End
      request.setRedirectToAwaitingDelivery(true);

      request.setSubmitOnCreate(false);
      request.setDeliveryOptionName(BDMConstants.kDeliveryOptionLocalPrint);
      request.setOnlyArchive(corresDetails.isDigitalInd);
      request.setEditorRedirectURL(BDMConstants.EMPTY_STRING);
      if (BDMCCTSUBMITOPT.MODIFY.equalsIgnoreCase(corresDetails.submitOpt)) {

        request.setCorrespondenceMode(BDMConstants.kEdit);

      } else {

        request.setCorrespondenceMode(BDMConstants.kReview);
      }

      // set the UserName - should be in the format of CURAM-<loggedInUserName>
      request.setUserID(
        BDMConstants.kEventSource + BDMConstants.gkHypen + loggedInUser);

      // call the interface to send the request
      final BDMCCTCreateCorrespondenceResponse response =
        new BDMCCTOutboundInterfaceImpl().createCorrespondence(request);

      if (response != null) {
        saveCCTCorrespondenceResponce(response, corresDetails);
        final BDMTemplateDetails bdmTemplateDetails =
          new BDMTemplateDetails();
        bdmTemplateDetails.cctUrl = response.getWorkItemURL();
        bdmTemplateDetails.workItemID = response.getWorkItemID();
        // BUG-91984, Start
        final String cctStatus = response.getStatus();
        if (!StringUtil.isNullOrEmpty(cctStatus)) {
          bdmTemplateDetails.cctStatus = cctStatus;
        }
        // BUG-91984, End
        return bdmTemplateDetails;
      } else {
        throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
      }
    }
  }

  /**
   * This method is to search templates based on the search criteria.
   *
   * @param searchKey
   * @param BDMTemplateSearchDetailsList
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMTemplateSearchDetailsList
    searchTemplates(final BDMTemplateSearchKey searchKey)
      throws AppException, InformationalException {

    // validate search template inputs
    validateSearchTemplateInputs(searchKey);

    final CuramValueList<BDMTemplateSearchDetails> curamValueList =
      DynamicDataAccess.executeNsMulti(BDMTemplateSearchDetails.class,
        searchKey, false, true,
        searchTemplateCatSubCatSQL(searchKey).sqlStatement);

    final BDMTemplateSearchDetailsList detailsList =
      new BDMTemplateSearchDetailsList();
    detailsList.dtls.addAll(curamValueList);

    // search result returns no records
    if (detailsList.dtls.size() == 0) {
      throw new AppException(BDMBPOCCT.ERR_NO_RESULTS_RETURNED);
    }

    // search result has more than 50 results
    if (detailsList.dtls.size() > 50) {
      throw new AppException(BDMBPOCCT.ERR_SEARCH_RESULT_MORE_THAN_50);
    }

    // compose the category-subcategory name
    for (int i = 0; i < detailsList.dtls.size(); i++) {

      // Concatenate the template name and template description
      detailsList.dtls.get(i).templateName =
        detailsList.dtls.get(i).templateName + CuramConst.gkSpace
          + detailsList.dtls.get(i).templateDesc;

      detailsList.dtls.get(i).category =
        detailsList.dtls.get(i).category.replace(
          BDMConstants.kMarkUpSingleQuote, GeneralConstants.kApostrophe);
      detailsList.dtls.get(i).subCategory =
        detailsList.dtls.get(i).subCategory.replace(
          BDMConstants.kMarkUpSingleQuote, GeneralConstants.kApostrophe);

      if (!detailsList.dtls.get(i).subCategory.isEmpty()) {
        detailsList.dtls.get(i).categoryAndSubCategory =
          detailsList.dtls.get(i).category + CuramConst.gkDash
            + detailsList.dtls.get(i).subCategory;

      } else {
        detailsList.dtls.get(i).categoryAndSubCategory =
          detailsList.dtls.get(i).category;
      }
    }
    return detailsList;
  }

  private SQLStatement
    searchTemplateCatSubCatSQL(final BDMTemplateSearchKey key) {

    final SQLStatement sqlStatement = new SQLStatement();
    String sql = new String();
    String selectStr = new String();
    String intoStr = new String();
    String fromStr = new String();
    String whereStr = new String();

    // select query
    selectStr =
      "SELECT DISTINCT R.templateID, R.templateName, R.category, R.subcategory, R.templateDesc ";

    // into query
    intoStr =
      "INTO :templateID, :templateName, :category, :subCategory, :templateDesc ";

    // from query
    fromStr =
      " from (SELECT DISTINCT t.templateID, t.templateName, t.templatePath, t.templatedesc,"
        + " CASE WHEN f.parentFolderID IS NULL THEN f.folderName ELSE s.folderName END AS category, "
        + " CASE WHEN f.parentFolderID IS NOT NULL THEN f.folderName END AS subCategory, "
        + " CASE WHEN f.parentFolderID IS NULL THEN f.folderID ELSE s.folderID END AS categoryID, "
        + " CASE WHEN f.parentFolderID IS NOT NULL THEN f.folderID END AS subCategoryID "
        + " FROM BDMCCTTemplate t JOIN BDMCCTFolder f ON f.folderID = t.folderID LEFT JOIN BDMCCTFolder s ON f.parentFolderID = s.folderID) R ";

    // where query
    whereStr = "WHERE ";

    // if category has been selected
    if (CuramConst.gkZero != key.categoryID) {
      if (CuramConst.gkZero == key.subCategoryID) {
        whereStr += " R.categoryID = '" + key.categoryID + "' ";
      } else {
        // if sub category has been selected
        whereStr += " R.categoryID = '" + key.categoryID
          + "'AND R.subCategoryID = '" + key.subCategoryID + "' ";
      }
    }

    if (!key.templateIDName.isEmpty()) {
      if (CuramConst.gkZero != key.categoryID) {
        whereStr += " AND ";
      }

      try {
        // check if the tempalteName is numeric
        Long.parseLong(key.templateIDName);
        // if numeric, match it with templateID
        whereStr += " R.templateID=:templateIDName ";
      } catch (final NumberFormatException ex) {
        // if not numeric, match it with the template name
        // Escape apostrophe character if it is present
        key.templateIDName = key.templateIDName.replace(
          GeneralConstants.kApostrophe, GeneralConstants.kTwoApostrophes);

        whereStr += "( UPPER(R.templateName) like '%"
          + key.templateIDName.toUpperCase() + "%' "

          + "OR UPPER(R.templateDesc) like '%"
          + key.templateIDName.toUpperCase() + "%' )";
      }
    }

    sql = selectStr + intoStr + fromStr + whereStr;

    sqlStatement.sqlStatement = sql;

    return sqlStatement;
  }

  /**
   * This method is to validate the search criteria inputs.
   *
   * @param searchKey
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void
    validateSearchTemplateInputs(final BDMTemplateSearchKey searchKey)
      throws AppException, InformationalException {

    // sub category cannot be selected if category is not selected
    if (CuramConst.gkZero == searchKey.categoryID
      && CuramConst.gkZero != searchKey.subCategoryID) {
      throw new AppException(
        BDMBPOCCT.ERR_TEMPLATE_CATEGORY_MUST_BE_SELECTED);
    }

    // validate if a search criteria is entered
    if (searchKey.templateIDName.isEmpty()
      && CuramConst.gkZero == searchKey.categoryID) {
      throw new AppException(
        BDMBPOCCT.ERR_TEMPLATE_NAME_OR_CATEGORY_MUST_BE_ENTERED);
    }

    // BUG-82031, Start
    // Configure the minimum chars in a property.
    // validate if the templateIDName length is not less than 3
    if (!StringUtil.isNullOrEmpty(searchKey.templateIDName)) {
      final int minimumLength = Configuration
        .getIntProperty(EnvVars.BDM_CORRESPONDENCE_TEMPLATE_MIN_SEARCH_LEN);
      if (minimumLength > searchKey.templateIDName.length()) {
        final AppException ae =
          new AppException(BDMBPOCCT.ERR_MINIMUM_LENGTH);
        ae.arg(minimumLength);
        throw ae;
      }
    }
    // BUG-82031, End
  }

  /**
   * This method is to search templates categories and sub-categories
   *
   * @return BDMCategorySubCategoryList
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMCategorySubCategoryList getCategoryAndSubCategoryList()
    throws AppException, InformationalException {

    final BDMCategorySubCategoryList categoryList =
      BDMCCTTemplateFactory.newInstance().searchCategorySubCategory();

    return categoryList;
  }

  private SQLStatement searchCategoryAndSubCategorySQL() {

    final SQLStatement sqlStatement = new SQLStatement();
    String selectStr = new String();

    selectStr = "SELECT DISTINCT  "
      + "CASE  WHEN f.parentFolderID IS NULL THEN  f.folderName   ELSE   s.folderName   END AS category,  "
      + "CASE   WHEN f.parentFolderID IS NOT NULL THEN  f.folderName   END AS subcategory,  "
      + "CASE  WHEN f.parentFolderID IS NULL THEN  f.folderid   ELSE   s.folderid   END AS categoryid,  "
      + "CASE   WHEN f.parentFolderID IS NOT NULL THEN  f.folderid   END AS subcategoryid "
      + "INTO  :categoryName,  :subCategoryName, :categoryID, :subCategoryID  "
      + "FROM      BDMCCTTemplate   t      JOIN BDMCCTFolder   f  ON f.folderID = t.folderID      "
      + "LEFT JOIN BDMCCTFolder  s  ON f.parentFolderID = s.folderID "
      + "ORDER BY subcategory";

    sqlStatement.sqlStatement = selectStr;
    return sqlStatement;
  }

  @Override
  public BDMTemplateSearchCriteria getSubCategorySearchCriteria()
    throws AppException, InformationalException {

    final BDMTemplateSearchCriteria criteria =
      new BDMTemplateSearchCriteria();

    BDMCategorySubCategoryListData subCategoryListData;
    BDMCategorySubCategoryListData categoryListData;
    final HashMap<String, ArrayList<String>> subCategoryLookUp =
      new HashMap<String, ArrayList<String>>();

    final CuramValueList<BDMCategorySubCategory> curamValueList =
      DynamicDataAccess.executeNsMulti(BDMCategorySubCategory.class, false,
        true, searchCategoryAndSubCategorySQL().sqlStatement);

    for (final BDMCategorySubCategory dtls : curamValueList) {
      dtls.categoryName = dtls.categoryName.replace(
        BDMConstants.kMarkUpSingleQuote, GeneralConstants.kApostrophe);
      dtls.subCategoryName = dtls.subCategoryName.replace(
        BDMConstants.kMarkUpSingleQuote, GeneralConstants.kApostrophe);
      subCategoryListData = new BDMCategorySubCategoryListData();
      subCategoryListData.listCode = Long.toString(dtls.subCategoryID);
      subCategoryListData.listValue = dtls.subCategoryName;
      criteria.subCategoryDtls.dtls.add(subCategoryListData);
      if (subCategoryLookUp.containsKey(dtls.categoryName)) {
        subCategoryLookUp.get(dtls.categoryName).add(dtls.subCategoryName);
      } else {
        categoryListData = new BDMCategorySubCategoryListData();
        categoryListData.listCode = Long.toString(dtls.categoryID);
        categoryListData.listValue = dtls.categoryName;
        criteria.categoryDtls.dtls.add(categoryListData);
        subCategoryLookUp.put(dtls.categoryName, new ArrayList<String>());
        subCategoryLookUp.get(dtls.categoryName).add(dtls.subCategoryName);
      }
    }
    criteria.subCategoryArray = constructJavaScriptObject(subCategoryLookUp);
    return criteria;
  }

  /**
   * Constructs a String (JSON string) in a format that is used by the DOJO
   * code that dynamically display the Template subCategory based on the
   * category.
   *
   * @return String - formatted string in JSON format
   *
   * @throws DatabaseException
   * @throws AppRuntimeException
   * @throws AppException
   * @throws InformationalException
   */
  private String constructJavaScriptObject(
    final HashMap<String, ArrayList<String>> subCategoryLookUp)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    final StringBuilder builder = new StringBuilder();
    short counter = CuramConst.gkZero;

    builder.append("{");
    builder.append("label : ").append("\"name\", ");
    builder.append("identifier : ").append("\"value\", ");
    builder.append("items : ");
    builder.append("[");
    builder.append("{");
    builder.append("value: ").append("\"").append("").append("\"");
    builder.append(", ");
    builder.append("name: ").append("\"").append("").append("\"");
    builder.append(", ");
    builder.append("category: ").append("\"").append("").append("\"");
    builder.append("},");
    for (final Entry<String, ArrayList<String>> entry : subCategoryLookUp
      .entrySet()) {
      final ArrayList<String> subCategoryList = entry.getValue();
      for (final String subCategory : subCategoryList) {
        if (counter == 0) {
          builder.append("{");
          final long randomNumber = (long) (Math.random() * 55555);
          builder.append("value: ").append("\"").append(randomNumber)
            .append("#").append(subCategory).append("\"");
          builder.append(", ");
          builder.append("name: ").append("\"").append(subCategory)
            .append("\"");
          builder.append(", ");
          builder.append("category: ").append("\"").append(entry.getKey())
            .append("\"");
          builder.append("}");
        } else {
          final long randomNumber = (long) (Math.random() * 55555);
          builder.append(",{");
          builder.append("value: ").append("\"").append(randomNumber)
            .append("#").append(subCategory).append("\"");
          builder.append(", ");
          builder.append("name: ").append("\"").append(subCategory)
            .append("\"");
          builder.append(", ");
          builder.append("category: ").append("\"").append(entry.getKey())
            .append("\"");
          builder.append("}");
        }
        counter++;
      }
    }
    builder.append("]};");
    return builder.toString();
  }

  /**
   * This method is to validate correspondence details entered.
   *
   * @param corresDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void
    validateCorespondenceDetails(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    if (0 == corresDetails.concernRoleID) {
      // TODO to add a new error message for checking client info
      throw new AppException(BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    } else {
      setClientMandatoryFields(corresDetails);

    }

    // 'Recipient - To' section is mandatory
    if (!corresDetails.toClientIsCorrespondent
      && 0 == corresDetails.toContactConcernRoleID
      && 0 == corresDetails.toParticipantRoleID
      && 0 == corresDetails.toContactThirdPartyConcernRoleID) {

      throw new AppException(BDMBPOCCT.ERR_TO_RECIPIENT_MANDATORY);
    }

    // only one recipient for 'To' section is allowed
    if (corresDetails.toClientIsCorrespondent
      && 0 != corresDetails.toParticipantRoleID
      || corresDetails.toClientIsCorrespondent
        && 0 != corresDetails.toContactConcernRoleID
      || 0 != corresDetails.toParticipantRoleID
        && 0 != corresDetails.toContactConcernRoleID
      || corresDetails.toClientIsCorrespondent
        && 0 != corresDetails.toContactThirdPartyConcernRoleID
      || 0 != corresDetails.toParticipantRoleID
        && 0 != corresDetails.toContactThirdPartyConcernRoleID
      || 0 != corresDetails.toContactConcernRoleID
        && 0 != corresDetails.toContactThirdPartyConcernRoleID) {
      throw new AppException(BDMBPOCCT.ERR_ONLY_ONE_TO_RECIPIENT_ALLOWED);
    }

    // only one recipient for 'CC' section is allowed
    if (corresDetails.ccClientIsCorrespondent
      && 0 != corresDetails.ccParticipantRoleID
      || corresDetails.ccClientIsCorrespondent
        && 0 != corresDetails.ccContactConcernRoleID
      || 0 != corresDetails.ccParticipantRoleID
        && 0 != corresDetails.ccContactConcernRoleID
      || corresDetails.ccClientIsCorrespondent
        && 0 != corresDetails.ccThirdPartyContactConcernRoleID
      || 0 != corresDetails.ccParticipantRoleID
        && 0 != corresDetails.ccThirdPartyContactConcernRoleID
      || 0 != corresDetails.ccContactConcernRoleID
        && 0 != corresDetails.ccThirdPartyContactConcernRoleID) {
      throw new AppException(BDMBPOCCT.ERR_ONLY_ONE_TO_RECIPIENT_ALLOWED);
    }

    // both 'To' and 'CC' cannot be the same individual
    if (corresDetails.toClientIsCorrespondent
      && corresDetails.ccClientIsCorrespondent
      || corresDetails.toContactConcernRoleID == corresDetails.ccContactConcernRoleID
        && corresDetails.toContactConcernRoleID != 0
      || corresDetails.toParticipantRoleID == corresDetails.ccParticipantRoleID
        && corresDetails.toParticipantRoleID != 0
      || corresDetails.toContactThirdPartyConcernRoleID == corresDetails.ccThirdPartyContactConcernRoleID
        && corresDetails.toContactThirdPartyConcernRoleID != 0) {
      throw new AppException(BDMBPOCCT.ERR_CANNOT_SAME_INDIVIDUAL);
    }

    // 'CC' section cannot have the same individual
    if (corresDetails.concernRoleID == corresDetails.ccContactConcernRoleID
      && corresDetails.concernRoleID != 0
      || corresDetails.concernRoleID == corresDetails.ccParticipantRoleID
      || corresDetails.ccParticipantRoleID == corresDetails.ccContactConcernRoleID
        && corresDetails.ccParticipantRoleID != 0
      || corresDetails.concernRoleID == corresDetails.ccThirdPartyContactConcernRoleID) {
      throw new AppException(BDMBPOCCT.ERR_NO_SAME_CC_ALLOWED);
    }
  }

  /**
   * This method is to get and set the names of the selected correspondents.
   *
   * @param corresDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void
    populateNameDetails(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    corresDetails.clientName =
      helper.getConcernRoleName(corresDetails.concernRoleID);
    // TO Section
    if (corresDetails.toClientIsCorrespondent) {
      // set the name

      corresDetails.toCorrespondentName =
        helper.getConcernRoleName(corresDetails.concernRoleID);

    } else if (corresDetails.toContactConcernRoleID != 0) {
      // set the name
      corresDetails.toCorrespondentName =
        helper.getConcernRoleName(corresDetails.toContactConcernRoleID);

    } else if (corresDetails.toParticipantRoleID != 0) {
      // set the name
      corresDetails.toCorrespondentName =
        helper.getConcernRoleName(corresDetails.toParticipantRoleID);
    } else if (corresDetails.toContactThirdPartyConcernRoleID != 0) {
      // set the name
      corresDetails.toCorrespondentName = helper
        .getConcernRoleName(corresDetails.toContactThirdPartyConcernRoleID);
    }

    // CC Section
    if (corresDetails.ccClientIsCorrespondent) {
      // set the cc correspondent name
      corresDetails.ccCorrespondentName =
        helper.getConcernRoleName(corresDetails.concernRoleID);
    } else if (corresDetails.ccContactConcernRoleID != 0) {
      // set the cc contact name
      corresDetails.ccCorrespondentName =
        helper.getConcernRoleName(corresDetails.ccContactConcernRoleID);
    } else if (corresDetails.ccParticipantRoleID != 0) {
      // set the cc participant name
      corresDetails.ccCorrespondentName =
        helper.getConcernRoleName(corresDetails.ccParticipantRoleID);
    } else if (corresDetails.ccThirdPartyContactConcernRoleID != 0) {
      // set the cc participant name
      corresDetails.ccCorrespondentName = helper
        .getConcernRoleName(corresDetails.ccThirdPartyContactConcernRoleID);
    }
  }

  /**
   * This method is to create concernRoleCorrespondence record in Curam.
   *
   * @param commDtls
   * @return long - communicationID
   *
   * @throws AppException
   * @throws InformationalException
   */
  private long
    insertCorrespondenceDetails(final BDMCorrespondenceDetails commDtls)
      throws AppException, InformationalException {

    final ConcernRoleCommunicationDtls concernRoleCommDtls =
      new ConcernRoleCommunicationDtls();

    // set the details
    // implement human-readable Unique ID
    concernRoleCommDtls.communicationID = UniqueID.nextUniqueID();
    concernRoleCommDtls.concernRoleID = commDtls.concernRoleID;
    concernRoleCommDtls.statusCode = RECORDSTATUS.NORMAL;
    concernRoleCommDtls.correspondentName = commDtls.toCorrespondentName;
    if (0 != commDtls.caseID) {
      concernRoleCommDtls.caseID = commDtls.caseID;
    }
    // TODO: need to check where it is populated
    concernRoleCommDtls.subjectText = commDtls.subject;
    concernRoleCommDtls.documentTemplateID =
      String.valueOf(commDtls.templateID);
    concernRoleCommDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    concernRoleCommDtls.communicationDate = Date.getCurrentDate();
    concernRoleCommDtls.userName = TransactionInfo.getProgramUser();

    // set the method type
    if (commDtls.isDigitalInd) {
      concernRoleCommDtls.methodTypeCode = COMMUNICATIONMETHOD.DIGITAL;
    } else {
      concernRoleCommDtls.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    }

    // set the correspondentConcernRoleID and correspondenceTypeCode
    if (commDtls.toClientIsCorrespondent) {
      concernRoleCommDtls.correspondentConcernRoleID = commDtls.concernRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.CLIENT;
    } else if (commDtls.toContactConcernRoleID != 0) {
      concernRoleCommDtls.correspondentConcernRoleID =
        commDtls.toContactConcernRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.CONTACT;
    } else if (commDtls.toContactThirdPartyConcernRoleID != 0) {
      concernRoleCommDtls.correspondentConcernRoleID =
        commDtls.toContactThirdPartyConcernRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.THIRDPARTY;
    } else {
      concernRoleCommDtls.correspondentConcernRoleID =
        commDtls.toParticipantRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.PARTICIPANT;
    }
    concernRoleCommDtls.addressID = commDtls.toAddressID;
    // insert into ConcernRoleCommunication
    ConcernRoleCommunicationFactory.newInstance().insert(concernRoleCommDtls);

    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommDtls =
      new BDMConcernRoleCommunicationDtls();

    // BUG-92596, Start
    // Set the correct field for concernRoleID
    // set the ccClientConcernRoleID
    if (commDtls.ccClientIsCorrespondent) {
      bdmConcernRoleCommDtls.ccClientConcernRoleID =
        concernRoleCommDtls.correspondentConcernRoleID;
    }
    // BUG-92596, End

    // set the BDM Correspondence details
    bdmConcernRoleCommDtls.communicationID =
      concernRoleCommDtls.communicationID;
    if (0 != commDtls.ccContactConcernRoleID) {
      bdmConcernRoleCommDtls.ccContactConcernRoleID =
        commDtls.ccContactConcernRoleID;
    }
    if (0 != commDtls.ccParticipantRoleID) {
      bdmConcernRoleCommDtls.ccParticipantConcernRoleID =
        commDtls.ccParticipantRoleID;
    }
    if (0 != commDtls.ccThirdPartyContactConcernRoleID) {
      bdmConcernRoleCommDtls.ccThirdPartyContactConcernRoleID =
        commDtls.ccThirdPartyContactConcernRoleID;
    }

    bdmConcernRoleCommDtls.digitalInd = commDtls.isDigitalInd;
    bdmConcernRoleCommDtls.ccAddressID = commDtls.ccDetails.ccAddressID;

    // TASK-99477, Start
    // Insert the template name used as well
    bdmConcernRoleCommDtls.templateName = commDtls.templateName;
    // TASK-99477, End

    // insert into BDMConcernRoleCommunication
    BDMConcernRoleCommunicationFactory.newInstance()
      .insert(bdmConcernRoleCommDtls);

    return concernRoleCommDtls.communicationID;
  }

  /**
   * This method is to get the mandatory fields for DataXML
   * to send over to CCT. If any mandatory value is missing,
   * it throws an error on the screen.
   *
   * @param corresDetails
   * @return BDMCorrespondenceDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  private BDMCorrespondenceDetails
    setTOClientMandatoryFields(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    // get the 'TO' client details
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    if (corresDetails.toClientIsCorrespondent) {
      concernRoleKey.concernRoleID = corresDetails.concernRoleID;
      corresDetails.toRecipientContactCode =
        BDMConstants.kGenerateCorrespondenceXMLClient;
    } else if (corresDetails.toContactConcernRoleID != 0) {
      concernRoleKey.concernRoleID = corresDetails.toContactConcernRoleID;
      corresDetails.toRecipientContactCode =
        BDMConstants.kGenerateCorrespondenceXMLIndividual;
    } else if (!corresDetails.toTPContactConcernRoleIDAndEvidenceIDCommaDelimited
      .isEmpty()) {
      final String[] concernRoleIDAndEvidenceID =
        corresDetails.toTPContactConcernRoleIDAndEvidenceIDCommaDelimited
          .split(CuramConst.gkCommaDelimiter);
      concernRoleKey.concernRoleID =
        Long.parseLong(concernRoleIDAndEvidenceID[0]);
      corresDetails.toContactThirdPartyConcernRoleID =
        Long.parseLong(concernRoleIDAndEvidenceID[0]);

      final long evidenceID = Long.parseLong(concernRoleIDAndEvidenceID[1]);
      final String role = BDMEvidenceUtil.getDynEvdAttrValue(evidenceID,
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
        BDMConstants.kBDMDynEvdAttrNameRole);

      final String roleType = BDMEvidenceUtil.getDynEvdAttrValue(evidenceID,
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
        BDMConstants.kBDMDynEvdAttrNameRoleType);

      if (BDMTHIRDPARTYROLE.PATRONATO.equalsIgnoreCase(role)) {
        corresDetails.toRecipientContactCode =
          BDMConstants.kGenerateCorrespondenceXMLPatronato;
      } else if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL
        .equalsIgnoreCase(roleType)) {
        corresDetails.toRecipientContactCode =
          BDMConstants.kGenerateCorrespondenceXMLIndividual;
      } else if (BDMTHIRDPARTYROLETYPE.ORGANIZATION
        .equalsIgnoreCase(roleType)) {
        corresDetails.toRecipientContactCode =
          BDMConstants.kGenerateCorrespondenceXMLOrganization;
      }

    } else {
      concernRoleKey.concernRoleID = corresDetails.toParticipantRoleID;
    }

    // To client address
    final boolean isThirdPartyContactInd =
      corresDetails.toContactThirdPartyConcernRoleID != 0
        || corresDetails.toContactConcernRoleID != 0 ? true : false;
    corresDetails.toAddressID = helper.getAddressIDforConcern(
      concernRoleKey.concernRoleID, isThirdPartyContactInd);
    final String[] splittedAddress =
      helper.getAddressMapper(corresDetails.toAddressID);

    corresDetails.toDetails.addressLineOne = splittedAddress[0];
    corresDetails.toDetails.addressLineTwo = splittedAddress[1];
    corresDetails.toDetails.addressLineThree = splittedAddress[2];
    corresDetails.toDetails.addressLineFour = splittedAddress[3];
    if (!StringUtil.isNullOrEmpty(splittedAddress[4])
      && BDMConstants.kBDMUNPARSE.equalsIgnoreCase(splittedAddress[4])) {
      corresDetails.toDetails.unparsedAddressInd = true;
    }

    // set preferred language
    corresDetails.toParticipantPreferredLanguage =
      helper.getPreferredLanguage(concernRoleKey.concernRoleID);

    // If third party contact is the primary correspondent and the
    // third-party contact doesn't have preferred language captured,
    // letter will be sent in client's preferred language.
    if (StringUtil.isNullOrEmpty(corresDetails.toParticipantPreferredLanguage)
      && corresDetails.toContactThirdPartyConcernRoleID != 0) {
      corresDetails.toParticipantPreferredLanguage =
        helper.getPreferredLanguage(corresDetails.concernRoleID);
    }

    if (StringUtil
      .isNullOrEmpty(corresDetails.toParticipantPreferredLanguage)) {
      throw new AppException(BDMBPOCCT.ERR_PREFERRED_LANGUAGE_MISSING)
        .arg(helper.getConcernRoleName(concernRoleKey.concernRoleID));
    }

    return corresDetails;
  }

  /**
   * This method is to get the mandatory fields for DataXML
   * to send over to CCT. If any mandatory value is missing,
   * it throws an error on the screen.
   *
   * @param corresDetails
   * @return BDMCorrespondenceDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  private BDMCorrespondenceDetails

    setCCClientMandatoryFields(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    // Setting client CC address
    if (corresDetails.ccClientIsCorrespondent) {
      concernRoleKey.concernRoleID = corresDetails.concernRoleID;
      corresDetails.ccRecipientContactCode =
        BDMConstants.kGenerateCorrespondenceXMLClient;
    } else if (corresDetails.ccContactConcernRoleID != 0) {
      concernRoleKey.concernRoleID = corresDetails.ccContactConcernRoleID;
      corresDetails.ccRecipientContactCode =
        BDMConstants.kGenerateCorrespondenceXMLIndividual;
    } else if (!corresDetails.ccTPContactConcernRoleIDAndEvidenceIDCommaDelimited
      .isEmpty()) {
      final String[] concernRoleIDAndEvidenceID =
        corresDetails.ccTPContactConcernRoleIDAndEvidenceIDCommaDelimited
          .split(CuramConst.gkCommaDelimiter);
      concernRoleKey.concernRoleID =
        Long.parseLong(concernRoleIDAndEvidenceID[0]);
      corresDetails.ccThirdPartyContactConcernRoleID =
        Long.parseLong(concernRoleIDAndEvidenceID[0]);

      final long evidenceID = Long.parseLong(concernRoleIDAndEvidenceID[1]);
      final String role = BDMEvidenceUtil.getDynEvdAttrValue(evidenceID,
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
        BDMConstants.kBDMDynEvdAttrNameRole);

      final String roleType = BDMEvidenceUtil.getDynEvdAttrValue(evidenceID,
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
        BDMConstants.kBDMDynEvdAttrNameRoleType);

      if (BDMTHIRDPARTYROLE.PATRONATO.equalsIgnoreCase(role)) {
        corresDetails.ccRecipientContactCode =
          BDMConstants.kGenerateCorrespondenceXMLPatronato;
      } else if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL
        .equalsIgnoreCase(roleType)) {
        corresDetails.ccRecipientContactCode =
          BDMConstants.kGenerateCorrespondenceXMLIndividual;
      } else if (BDMTHIRDPARTYROLETYPE.ORGANIZATION
        .equalsIgnoreCase(roleType)) {
        corresDetails.ccRecipientContactCode =
          BDMConstants.kGenerateCorrespondenceXMLOrganization;
      }

    } else if (corresDetails.ccParticipantRoleID != 0) {
      concernRoleKey.concernRoleID = corresDetails.ccParticipantRoleID;
    }

    // CC client address
    final boolean isThirdPartyContactInd =
      corresDetails.ccThirdPartyContactConcernRoleID != 0 ? true : false;
    if (concernRoleKey.concernRoleID != 0) {
      corresDetails.ccDetails.ccAddressID = helper.getAddressIDforConcern(
        concernRoleKey.concernRoleID, isThirdPartyContactInd);
      final String[] splittedAddressCCClient =
        helper.getAddressMapper(corresDetails.ccDetails.ccAddressID);

      corresDetails.ccDetails.ccAddressLineOne = splittedAddressCCClient[0];
      corresDetails.ccDetails.ccAddressLineTwo = splittedAddressCCClient[1];
      corresDetails.ccDetails.ccAddressLineThree = splittedAddressCCClient[2];
      corresDetails.ccDetails.ccAddressLineFour = splittedAddressCCClient[3];
      if (!StringUtil.isNullOrEmpty(splittedAddressCCClient[4])
        && BDMConstants.kBDMUNPARSE
          .equalsIgnoreCase(splittedAddressCCClient[4])) {
        corresDetails.ccDetails.ccUnparsedAddressInd = true;
      }
    }

    if (CuramConst.gkZero != concernRoleKey.concernRoleID) {
      // set preferred language
      corresDetails.ccContactPreferredLanguage =
        helper.getPreferredLanguage(concernRoleKey.concernRoleID);

      // If third party contact is the primary correspondent and the
      // third-party contact doesn't have preferred language captured,
      // letter will be sent in client's preferred language.
      // BUG-103516, Start
      if (StringUtil.isNullOrEmpty(corresDetails.ccContactPreferredLanguage)
        && corresDetails.ccThirdPartyContactConcernRoleID != 0) {
        corresDetails.ccContactPreferredLanguage =
          helper.getPreferredLanguage(corresDetails.concernRoleID);
      }
      // BUG-103516, End

      if (StringUtil
        .isNullOrEmpty(corresDetails.ccContactPreferredLanguage)) {
        throw new AppException(BDMBPOCCT.ERR_PREFERRED_LANGUAGE_MISSING)
          .arg(helper.getConcernRoleName(concernRoleKey.concernRoleID));
      }
    }

    return corresDetails;
  }

  private BDMCorrespondenceDetails

    setClientMandatoryFields(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    // get client details
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = corresDetails.concernRoleID;

    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(concernRoleKey);

    final NotFoundIndicator nfInd = new NotFoundIndicator();

    // get the person or prospect person details based concernRoleType
    if (CONCERNROLETYPE.PERSON.equals(concernRoleDtls.concernRoleType)) {

      final PersonKey personKey = new PersonKey();
      personKey.concernRoleID = concernRoleKey.concernRoleID;
      final PersonDtls personDtls =
        PersonFactory.newInstance().read(nfInd, personKey);

      if (!nfInd.isNotFound()) {
        // client date of birth
        if (personDtls.dateOfBirth.isZero()) {
          throw new AppException(BDMBPOCCT.ERR_PERSON_DOB_IS_MISSING)
            .arg(concernRoleDtls.concernRoleName);
        } else {
          corresDetails.clientDtls.dateOfBirth = personDtls.dateOfBirth;
        }

        // client date of death
        if (!personDtls.dateOfDeath.isZero()) {
          corresDetails.clientDtls.dateOfDeath = personDtls.dateOfDeath;
        }

        // client gender
        if (GENDER.MALE.equalsIgnoreCase(personDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderMale;

        } else if (GENDER.FEMALE.equalsIgnoreCase(personDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderFemale;

        } else if (GENDER.GENDER_X.equalsIgnoreCase(personDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderX;

        } else if (GENDER.UNKNOWN.equalsIgnoreCase(personDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderUnknown;

        } else if (GENDER.INVALID.equalsIgnoreCase(personDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderInvalid;

        } else {
          throw new AppException(BDMBPOCCT.ERR_PERSON_GENDER_IS_MISSING)
            .arg(concernRoleDtls.concernRoleName);
        }
      }

    } else if (CONCERNROLETYPE.PROSPECTPERSON
      .equals(concernRoleDtls.concernRoleType)) {
      final ProspectPersonKey prosPersonKey = new ProspectPersonKey();
      prosPersonKey.concernRoleID = concernRoleKey.concernRoleID;
      final ProspectPersonDtls prosPersonDtls =
        ProspectPersonFactory.newInstance().read(nfInd, prosPersonKey);

      if (!nfInd.isNotFound()) {
        // client date of birth
        if (prosPersonDtls.dateOfBirth.isZero()) {
          throw new AppException(BDMBPOCCT.ERR_PERSON_DOB_IS_MISSING)
            .arg(concernRoleDtls.concernRoleName);
        } else {
          corresDetails.clientDtls.dateOfBirth = prosPersonDtls.dateOfBirth;
        }

        // client date of death
        if (!prosPersonDtls.dateOfDeath.isZero()) {

          corresDetails.clientDtls.dateOfDeath = prosPersonDtls.dateOfDeath;
        }

        // client gender
        if (GENDER.MALE.equalsIgnoreCase(prosPersonDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderMale;

        } else if (GENDER.FEMALE.equalsIgnoreCase(prosPersonDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderFemale;

        } else if (GENDER.GENDER_X.equalsIgnoreCase(prosPersonDtls.gender)) {
          corresDetails.clientDtls.gender = BDMConstants.kClientGenderX;

        } else {
          throw new AppException(BDMBPOCCT.ERR_PERSON_GENDER_IS_MISSING)
            .arg(concernRoleDtls.concernRoleName);
        }

      }
    }

    // SIN
    final ConcernRoleAlternateID concernRoleAlternateID =
      ConcernRoleAlternateIDFactory.newInstance();
    final ConcernRoleAlternateIDRMKey alternateIDRMKey =
      new ConcernRoleAlternateIDRMKey();
    alternateIDRMKey.concernRoleID = corresDetails.concernRoleID;
    final AlternateIDReadmultiDtlsList alternateIDReadmultiDtlsList =
      concernRoleAlternateID.searchByConcernRole(alternateIDRMKey);
    for (int j = 0; j < alternateIDReadmultiDtlsList.dtls.size(); j++) {
      if (RECORDSTATUS.DEFAULTCODE
        .equalsIgnoreCase(alternateIDReadmultiDtlsList.dtls.get(j).statusCode)
        && CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER.equalsIgnoreCase(
          alternateIDReadmultiDtlsList.dtls.get(j).typeCode)) {
        corresDetails.clientDtls.sin = Long
          .parseLong(alternateIDReadmultiDtlsList.dtls.get(j).alternateID);
      }
      // BUG-103327, Start
      // Explicitly query the reference number instead of assigning
      // primaryAlternateID from concernRoleDtls
      if (RECORDSTATUS.DEFAULTCODE
        .equalsIgnoreCase(alternateIDReadmultiDtlsList.dtls.get(j).statusCode)
        && CONCERNROLEALTERNATEID.REFERENCE_NUMBER.equalsIgnoreCase(
          alternateIDReadmultiDtlsList.dtls.get(j).typeCode)) {
        corresDetails.clientDtls.clientID =
          alternateIDReadmultiDtlsList.dtls.get(j).alternateID;
      }
      // BUG-103327, End
    }
    final boolean isThirdPartyContactInd =
      corresDetails.toContactThirdPartyConcernRoleID != 0 ? true : false;
    corresDetails.toAddressID = helper.getAddressIDforConcern(
      corresDetails.concernRoleID, isThirdPartyContactInd);
    final String[] splittedAddress =
      helper.getAddressMapper(corresDetails.toAddressID);

    corresDetails.clientDtls.addressLineOne = splittedAddress[0];
    corresDetails.clientDtls.addressLineTwo = splittedAddress[1];
    corresDetails.clientDtls.addressLineThree = splittedAddress[2];
    corresDetails.clientDtls.addressLineFour = splittedAddress[3];
    if (!StringUtil.isNullOrEmpty(splittedAddress[4])
      && BDMConstants.kBDMUNPARSE.equalsIgnoreCase(splittedAddress[4])) {
      corresDetails.clientDtls.unparsedAddressInd = true;
    }

    return corresDetails;
  }

  /**
   * This method used to get the correspondence details
   * for the Inline Details page.
   *
   * @param key
   * @return BDMViewCorrespondenceDetails
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMViewCorrespondenceDetails

    getViewCorrespondence(final BDMCommunicationKey key)
      throws AppException, InformationalException {

    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();

    // get the ConcernRoleCommunication details
    final ConcernRoleCommunicationKey commKey =
      new ConcernRoleCommunicationKey();
    commKey.communicationID = key.communicationID;

    final ConcernRoleCommunicationDtls commDtls =
      ConcernRoleCommunicationFactory.newInstance().read(commKey);

    // set the details
    viewDetails.communicationID = commDtls.communicationID;
    viewDetails.clientName =
      helper.getConcernRoleName(commDtls.concernRoleID);
    viewDetails.comments = commDtls.comments;
    viewDetails.correspondentName = commDtls.correspondentName;
    viewDetails.correspondentType = commDtls.correspondentTypeCode;
    viewDetails.methodTypeCode = commDtls.methodTypeCode;
    viewDetails.fileLocation = commDtls.fileLocation;
    viewDetails.fileReference = commDtls.fileReferenceNumber;
    viewDetails.documentLocation = commDtls.documentLocation;
    viewDetails.documentReference = commDtls.documentRefNumber;
    // BEGIN BUG-94134 - Client's address appears instead of contact's address
    // in
    // Communications Details
    viewDetails.toAddressData = helper.getMailingAddressDataByConcernRole(
      commDtls.correspondentConcernRoleID).addressData;
    // END BUG-94134

    // BUG-96885, Start

    // BUG-99102, Start
    // The address mapper returns 5 strings.
    // The last string contains "1" for unparsed addresses.
    // Don't include that while displaying the address.
    StringBuffer sb = new StringBuffer("");
    if (CuramConst.gkZero != commDtls.addressID) {
      final String[] addrString = helper.getAddressMapper(commDtls.addressID);
      for (int i = 0; i < addrString.length - 1; i++) {
        if (!StringUtil.isNullOrEmpty(addrString[i])) {
          sb.append(addrString[i]);
          sb.append("\n");
        }
      }
    }
    // BUG-99102, End
    viewDetails.toAddressDataStr = sb.toString();
    // BUG-96885, End

    // get the BDMConcernRoleCommunication details
    final BDMConcernRoleCommunicationKey bdmCommKey =
      new BDMConcernRoleCommunicationKey();
    bdmCommKey.communicationID = commDtls.communicationID;

    final BDMConcernRoleCommunicationDtls bdmCommDtls =
      BDMConcernRoleCommunicationFactory.newInstance().read(bdmCommKey);

    if (bdmCommDtls.ccClientConcernRoleID != 0) {
      viewDetails.isCCClientInd = true;
      viewDetails.ccClientAddressData = viewDetails.toAddressData;
      viewDetails.ccClientCorrespondentType = CORRESPONDENT.CLIENT;
    }

    if (bdmCommDtls.ccContactConcernRoleID != 0) {
      viewDetails.isCCContactInd = true;
      viewDetails.ccContactName =
        helper.getConcernRoleName(bdmCommDtls.ccContactConcernRoleID);
      viewDetails.ccContactCorrespondentType = CORRESPONDENT.CONTACT;
      viewDetails.ccContactAddressData =
        helper.getMailingAddressDataByConcernRole(
          bdmCommDtls.ccContactConcernRoleID).addressData;
    }

    if (bdmCommDtls.ccParticipantConcernRoleID != 0) {
      viewDetails.isCCParticipantInd = true;
      viewDetails.ccParticipantName =
        helper.getConcernRoleName(bdmCommDtls.ccParticipantConcernRoleID);
      viewDetails.ccParticipantCorrespondentType = CORRESPONDENT.PARTICIPANT;
      viewDetails.ccParticipantAddressData =
        helper.getMailingAddressDataByConcernRole(
          bdmCommDtls.ccParticipantConcernRoleID).addressData;
    }

    if (bdmCommDtls.ccThirdPartyContactConcernRoleID != 0) {
      viewDetails.isCCThirdPartyContactInd = true;
      viewDetails.ccThirdPartyContactName = helper
        .getConcernRoleName(bdmCommDtls.ccThirdPartyContactConcernRoleID);
      viewDetails.ccThirdPartyContactCorrespondentType =
        CORRESPONDENT.THIRDPARTY;
      viewDetails.ccThirdPartyContactAddressData =
        helper.getMailingAddressDataByConcernRole(
          bdmCommDtls.ccThirdPartyContactConcernRoleID).addressData;
    }
    // BUG-96885, Start
    if (CuramConst.gkZero != bdmCommDtls.ccAddressID) {
      sb = new StringBuffer("");
      final String[] addrString =
        helper.getAddressMapper(bdmCommDtls.ccAddressID);
      for (int i = 0; i < addrString.length - 1; i++) {
        if (!StringUtil.isNullOrEmpty(addrString[i])) {
          sb.append(addrString[i]);
          sb.append("\n");
        }
      }
      viewDetails.ccAddressDataStr = sb.toString();
    }
    // BUG-96885, End

    return viewDetails;
  }

  /**
   * This method used to get the generated correspondence
   * template to view and download.
   *
   * @param key
   * @return FileNameAndDataDtls
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFileNameAndDataDtls
    downloadCorrespondence(final CommunicationIDKey key)
      throws AppException, InformationalException {

    // get the BDMConcernRoleCommunication details
    final BDMConcernRoleCommunicationKey bdmKey =
      new BDMConcernRoleCommunicationKey();
    bdmKey.communicationID = key.communicationID;

    final BDMConcernRoleCommunicationDtls dtls =
      BDMConcernRoleCommunicationFactory.newInstance().read(bdmKey);

    final BDMFileNameAndDataDtls fileNameAndDataDtls =
      new BDMFileNameAndDataDtls();
    fileNameAndDataDtls.fileName = dtls.workItemID + ".pdf";
    fileNameAndDataDtls.contentType = "application/pdf";
    fileNameAndDataDtls.contentDisposition = "inline";

    // set the request details
    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    try {
      request.setUserID(
        Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
      request.setCommunity(
        Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
      request.setVaultLetterID(dtls.trackingNumber);
      request.setWorkItemID(dtls.workItemID);

      // call the interface to send the request
      final BDMCCTGetCompletedPDFResponse response =
        new BDMCCTOutboundInterfaceImpl().getCompletedPDF(request);

      fileNameAndDataDtls.fileContent =
        new Blob(Base64.decodeBase64(response.getDocumentBytes().getBytes()));

    } catch (final Exception ioe) {
      throw new AppException(BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    }

    return fileNameAndDataDtls;
  }

  /**
   * This method is to fetch and create dataXML payload.
   *
   * @param corresDetails
   * @param communicationID
   * @return String - dataXML
   *
   * @throws AppException
   * @throws InformationalException
   */
  private String getDataXML(final BDMCorrespondenceDetails corresDetails)
    throws AppException, InformationalException, JAXBException {

    final BDMGenerateCorrespondenceMapper mapper =
      new BDMGenerateCorrespondenceMapper();

    final String dataXml = mapper.correspondenceMapper(corresDetails);
    return dataXml;
  }

  private void saveCCTCorrespondenceResponce(
    final BDMCCTCreateCorrespondenceResponse response,
    final BDMCorrespondenceDetails corresDetails)
    throws AppException, InformationalException {

    BDMConcernRoleCommunicationDtls bdmConcernRoleCommDtls =
      new BDMConcernRoleCommunicationDtls();

    final BDMConcernRoleCommunication bdmCommunicationObj =
      BDMConcernRoleCommunicationFactory.newInstance();
    final BDMConcernRoleCommunicationKey bdmCCKey =
      new BDMConcernRoleCommunicationKey();
    bdmCCKey.communicationID = corresDetails.communicationID;
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    // BUG-91264, Start
    // Read the current details before modifying.
    bdmConcernRoleCommDtls = bdmCommunicationObj.read(nfIndicator, bdmCCKey);
    if (!nfIndicator.isNotFound()) {

      // set the response data
      bdmConcernRoleCommDtls.submittedInd = response.getSubmittedInd();
      bdmConcernRoleCommDtls.workItemID = response.getWorkItemID();
      bdmConcernRoleCommDtls.trackingNumber = corresDetails.trackingNumber;

      // BUG-91984, Start
      final String cctStatusString = response.getStatus();
      if (!StringUtil.isNullOrEmpty(cctStatusString)) {
        final String cctStatus =
          BDMUtil.getCCTDescriptionToCodeMap().get(cctStatusString);
        if (!StringUtil.isNullOrEmpty(cctStatus)) {
          bdmConcernRoleCommDtls.cctStatus = cctStatus;
        }
      }
      // BUG-91984, End

      bdmCommunicationObj.modify(bdmCCKey, bdmConcernRoleCommDtls);
      // BUG-91264, End

      // get the ConcernRoleCommunication details
      final ConcernRoleCommunicationKey key =
        new ConcernRoleCommunicationKey();
      key.communicationID = corresDetails.communicationID;

      final ConcernRoleCommunication concernCommObj =
        ConcernRoleCommunicationFactory.newInstance();
      final ConcernRoleCommunicationDtls concernRoleCommDtls =
        concernCommObj.read(key);

      // set the correspondence status
      if (response.getSubmittedInd()) {
        concernRoleCommDtls.communicationStatus =
          COMMUNICATIONSTATUS.SUBMITTED;
      } else {
        concernRoleCommDtls.communicationStatus = COMMUNICATIONSTATUS.DRAFT;
      }

      // update the correspondence status
      concernCommObj.modify(key, concernRoleCommDtls);
    }
  }

  @Override
  /**
   *
   * This method modifies the status of a communication record.
   * The current status is saved in the status history entity before
   * modification.
   *
   * @param communicationID and communication status
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void
    modifyCommunicationStatus(final BDMCommunicationStatusDetails details)
      throws AppException, InformationalException {

    // BUG-94100, Start
    // Ensure comments are entered.
    if (StringUtil.isNullOrEmpty(details.statusComments)) {
      throw new AppException(BDMBPOCCT.ERR_STATUS_COMMENTS_MANDATORY);
    }

    // Get the current communication details
    final ConcernRoleCommunicationKey communicationKey =
      new ConcernRoleCommunicationKey();
    communicationKey.communicationID = details.communicationID;
    final ConcernRoleCommunication communicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final ConcernRoleCommunicationDtls commDtls =
      communicationObj.read(nfIndicator, communicationKey);
    if (!nfIndicator.isNotFound()) {

      // insert the current correspondence status in status history
      final BDMCommStatusHistoryDtls historyDtls =
        new BDMCommStatusHistoryDtls();
      historyDtls.BDMCommStatusHistoryID = UniqueID.nextUniqueID();
      historyDtls.communicationID = commDtls.communicationID;
      historyDtls.recordStatus = RECORDSTATUS.CANCELLED;
      historyDtls.statusCode = commDtls.communicationStatus;
      historyDtls.statusDateTime = DateTime.getCurrentDateTime();
      BDMCommStatusHistoryFactory.newInstance().insert(historyDtls);
      boolean taskToBeCreated = false;
      final BDMCommunicationHelper helper = new BDMCommunicationHelper();

      // A task needs to be sent out only if
      // 1. The previous status is SENT
      // 2. The new status is either VOID, RETURNED or MISDIRECTED
      if (COMMUNICATIONSTATUS.SENT
        .equalsIgnoreCase(commDtls.communicationStatus)
        && (COMMUNICATIONSTATUS.VOID.equalsIgnoreCase(details.statusCode)
          || COMMUNICATIONSTATUS.RETURNED.equalsIgnoreCase(details.statusCode)
          || COMMUNICATIONSTATUS.MISDIRECTED
            .equalsIgnoreCase(details.statusCode))) {
        taskToBeCreated = true;
      } else if (!COMMUNICATIONSTATUS.SENT
        .equalsIgnoreCase(commDtls.communicationStatus)
        && !commDtls.communicationStatus
          .equalsIgnoreCase(details.statusCode)) {
        // If the previous status is not SENT
        // and the current status is different from the previous status,
        // close the task if exists.
        helper.closeCommunicationStatusTask(details.communicationID,
          commDtls.concernRoleID);
      }

      // Update the communication record with the new status
      commDtls.communicationStatus = details.statusCode;
      communicationObj.modify(communicationKey, commDtls);

      // Update BDMConcernRoleCommunication entity with the comments
      final BDMConcernRoleCommunication bdmCommunicationObj =
        BDMConcernRoleCommunicationFactory.newInstance();
      final BDMConcernRoleCommunicationKey crKey =
        new BDMConcernRoleCommunicationKey();
      crKey.communicationID = details.communicationID;

      final BDMConcernRoleCommunicationDtls crDtls =
        bdmCommunicationObj.read(nfIndicator, crKey);
      if (!nfIndicator.isNotFound()) {
        crDtls.statusComments = details.statusComments;
        bdmCommunicationObj.modify(crKey, crDtls);

        if (taskToBeCreated) {
          helper.createStatusChangeTask(commDtls, crDtls);
        }
      }

    }
    // BUG-94100, End

  }

  /**
   * This method returns the record from BDMConcernRoleCommunication
   * entity for the given communicationID.
   *
   * @param communicationID
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMConcernRoleCommunicationDtls
    readBDMConcernRoleCommunicationDetails(
      final BDMConcernRoleCommunicationKey key)
      throws AppException, InformationalException {

    BDMConcernRoleCommunicationDtls dtls =
      new BDMConcernRoleCommunicationDtls();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    dtls =
      BDMConcernRoleCommunicationFactory.newInstance().read(nfIndicator, key);
    if (!nfIndicator.isNotFound()) {
      return dtls;
    }
    return dtls;
  }

  @Override
  public BDMConcernRoleIDEvidenceIDAndNameDetailsList
    listThirdPartyContactsForCommunication(final ConcernRoleIDCaseIDKey key)
      throws AppException, InformationalException {

    long concernRoleID = 0;
    if (CuramConst.gkZero != key.caseID) {

      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
        new ReadParticipantRoleIDDetails();
      caseHeaderKey.caseID = key.caseID;
      readParticipantRoleIDDetails =
        caseHeaderObj.readParticipantRoleID(caseHeaderKey);

      concernRoleID = readParticipantRoleIDDetails.concernRoleID;

    } else if (CuramConst.gkZero != key.concernRoleID) {
      concernRoleID = key.concernRoleID;
    }
    final BDMConcernRoleIDEvidenceIDAndNameDetailsList concernRoleIDAndNameDetailsList =
      new BDMConcernRoleIDEvidenceIDAndNameDetailsList();

    PDCEvidenceDetailsList pdcEvidenceList = new PDCEvidenceDetailsList();

    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();

    final PersonAndEvidenceTypeList personKey =
      new PersonAndEvidenceTypeList();
    personKey.concernRoleID = concernRoleID;
    personKey.evidenceTypeList = CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT;
    pdcEvidenceList =
      pdcPersonObj.listCurrentParticipantEvidenceByTypes(personKey);

    for (int i = 0; i < pdcEvidenceList.list.size(); i++) {

      final BDMConcernRoleIDEvidenceIDAndNameDetails concernRoleIDAndNameDetails =
        new BDMConcernRoleIDEvidenceIDAndNameDetails();

      // Check if the evidence is verified
      // Get the evidence descriptor id from evidence id
      final RelatedIDStatusAndEvidenceTypeKey relatedIDStatusAndEvidenceTypeKey =
        new RelatedIDStatusAndEvidenceTypeKey();
      relatedIDStatusAndEvidenceTypeKey.relatedID =
        pdcEvidenceList.list.item(i).evidenceID;
      relatedIDStatusAndEvidenceTypeKey.evidenceType =
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT;
      relatedIDStatusAndEvidenceTypeKey.statusCode = RECORDSTATUS.NORMAL;

      final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
        new RelatedIDAndEvidenceTypeKey();

      relatedIDAndEvidenceTypeKey.relatedID =
        pdcEvidenceList.list.item(i).evidenceID;
      relatedIDAndEvidenceTypeKey.evidenceType =
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT;
      final EvidenceDescriptorDtlsList descriptorDtlsList =
        EvidenceDescriptorFactory.newInstance()
          .searchByRelatedIDAndEvidenceType(relatedIDAndEvidenceTypeKey);

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : descriptorDtlsList.dtls) {

        // Get the VDIEDLinkID from evidence descriptor id
        final EvidenceDescriptorKey evidenceDescriptorKey =
          new EvidenceDescriptorKey();
        evidenceDescriptorKey.evidenceDescriptorID =
          evidenceDescriptorDtls.evidenceDescriptorID;
        final VDIEDLinkIDAndDataItemIDDetailsList vdiedLinkIDAndDataItemIDDetailsList =
          VDIEDLinkFactory.newInstance()
            .readByEvidenceDescriptorID(evidenceDescriptorKey);

        // Check if the evidence is verified based on VDIEDLinkID
        for (final VDIEDLinkIDAndDataItemIDDetails vdiedLinkIDAndDataItemIDDetails : vdiedLinkIDAndDataItemIDDetailsList.dtls) {
          final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();
          vdiedLinkKey.VDIEDLinkID =
            vdiedLinkIDAndDataItemIDDetails.vDIEDLinkID;
          final VerificationStatusDetails verificationStatusDetails =
            curam.verification.sl.infrastructure.fact.VerificationFactory
              .newInstance().getVerificationStatus(vdiedLinkKey);

          if (VERIFICATIONSTATUS.VERIFIED
            .equalsIgnoreCase(verificationStatusDetails.verificationStatus)) {

            final String fromDateStr = BDMEvidenceUtil.getDynEvdAttrValue(
              pdcEvidenceList.list.item(i).evidenceID,
              CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
              BDMConstants.kBDMDynEvdAttrNameFrom);
            final String toDateStr = BDMEvidenceUtil.getDynEvdAttrValue(
              pdcEvidenceList.list.item(i).evidenceID,
              CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
              BDMConstants.kBDMDynEvdAttrNameTo);
            final Date fromDate = fromDateStr.isEmpty() ? Date.kZeroDate
              : Date.getDate(fromDateStr);
            final Date toDate =
              toDateStr.isEmpty() ? Date.kZeroDate : Date.getDate(toDateStr);

            // Check if the record is within the date range
            if (BDMDateUtil.isDateBetween(Date.getCurrentDate(), fromDate,
              toDate)) {

              // Get the case participant role id
              final String thirdPartyCaseParticipantRoleID = BDMEvidenceUtil
                .getDynEvdAttrValue(pdcEvidenceList.list.item(i).evidenceID,
                  CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
                  BDMConstants.kBDMDynEvdAttrNameThirdPartyCaseParticipantRole);

              final CaseParticipantRole caseParticipantRoleObj =
                CaseParticipantRoleFactory.newInstance();

              final CaseParticipantRoleKey caseParticipantRoleKey =
                new CaseParticipantRoleKey();
              caseParticipantRoleKey.caseParticipantRoleID =
                Long.parseLong(thirdPartyCaseParticipantRoleID);

              // Get the concernrole ID and concernrole name
              final ParticipantRoleIDAndNameDetails participantRoleIDAndNameDetails =
                caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(
                  caseParticipantRoleKey);

              concernRoleIDAndNameDetails.concernRoleName =
                participantRoleIDAndNameDetails.name;
              concernRoleIDAndNameDetails.concernRoleID =
                participantRoleIDAndNameDetails.participantRoleID;

              // Send the details as concernRoleID,evidenceID because the action
              // phase
              // will need both these IDs to get further details
              concernRoleIDAndNameDetails.concernRoleIDAndEvidenceIDCommaDelimited =
                concernRoleIDAndNameDetails.concernRoleID
                  + CuramConst.gkCommaDelimiter
                  + pdcEvidenceList.list.item(i).evidenceID;
              concernRoleIDAndNameDetailsList.dtls
                .add(concernRoleIDAndNameDetails);
            }
          }
        }
      }
    }
    return concernRoleIDAndNameDetailsList;

  }

  @Override
  public void validateRecordedCommunication1(
    final RecordedCommDetails1 recordedCommDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final Date currentDate = Date.getCurrentDate();
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    // Ensure that the communication is not already canceled.
    if (recordedCommDetails.statusCode.equals(RECORDSTATUS.CANCELLED)) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(GENERAL.ERR_GENERAL_FV_NO_MODIFY_RECORD_CANCELLED),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          13);
    }

    // Subject field is mandatory.
    if (recordedCommDetails.subject.length() == 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(BPOCOMMUNICATION.ERR_COMM_SUBJECT_NOT_SUPPLIED),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Correspondent is mandatory.
    if (recordedCommDetails.correspondentParticipantRoleID == 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(BPOCOMMUNICATION.ERR_COMM_NAME_NOT_SUPPLIED),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          4);
    }

    // Date must not be in the future.
    if (recordedCommDetails.communicationDate.after(currentDate)) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(
            BPOMAINTAINCONCERNROLECOMMASSISTANT.ERR_COMM_DATE_IN_FUTURE),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }

    // Email is mandatory if type is Outgoing Email
    if (recordedCommDetails.methodTypeCode.equals(COMMUNICATIONMETHOD.EMAIL)
      && !recordedCommDetails.incomingInd) {

      final boolean doesEmailEvidenceExists =
        BDMUtil.doesEvidenceExistsForPerson(
          recordedCommDetails.correspondentParticipantRoleID,
          PDCConst.PDCEMAILADDRESS);

      if (!doesEmailEvidenceExists) {

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(
            new AppException(
              BPOCOMMUNICATION.ERR_RECORD_COMM_EMAIL_NOT_SUPPLIED),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            1);
      }
    }

    // Phone Number is mandatory if type is Phone
    if (recordedCommDetails.methodTypeCode.equals(COMMUNICATIONMETHOD.PHONE)
      && !recordedCommDetails.incomingInd) {

      final boolean doesPhoneEvidenceExists =
        BDMUtil.doesEvidenceExistsForPerson(
          recordedCommDetails.correspondentParticipantRoleID,
          PDCConst.PDCPHONENUMBER);

      if (!doesPhoneEvidenceExists) {

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(
            new AppException(BPOCOMMUNICATION.ERR_COMM_PHONE_NOT_SUPPLIED),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            1);
      }
    }

    // START - BUG 120625 - Commented out as FAX has been disabled.
    // Fax is mandatory if type is Outgoing Fax
    /*
     * if (recordedCommDetails.methodTypeCode.equals(COMMUNICATIONMETHOD.FAX)
     * && recordedCommDetails.phoneNumberID == 0 // BEGIN,
     * // CR00143201, JMA
     * && recordedCommDetails.phoneNumber.length() == 0 // END,
     * // CR00143201
     * && !recordedCommDetails.incomingInd) {
     *
     * curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
     * .addInfoMgrExceptionWithLookup(
     * new AppException(BPOCOMMUNICATION.ERR_COMM_FAX_NOT_SUPPLIED),
     * CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
     * curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
     * 1);
     * }
     */
    // END - BUG 120625 - Commented out as FAX has been disabled.

    // BEGIN, CR00143201, JMA
    final Address addressObj = AddressFactory.newInstance();
    final OtherAddressData otherAddressData = new OtherAddressData();

    otherAddressData.addressData = recordedCommDetails.addressData;

    final boolean addressEmpty =
      addressObj.isEmpty(otherAddressData).emptyInd;

    if (!addressEmpty && recordedCommDetails.addressID != 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(BPOCOMMUNICATION.ERR_COMM_XFV_MULTIPLE_ADDRESS),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          2);
    }

    /*
     * if (recordedCommDetails.phoneNumberID != 0
     * && recordedCommDetails.phoneNumber.length() != 0) {
     *
     * curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
     * .addInfoMgrExceptionWithLookup(
     * new AppException(
     * BPOCOMMUNICATION.ERR_COMM_XFV_MULTIPLE_PHONENUMBER),
     * CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
     * curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
     * 0);
     * }
     */

    if (recordedCommDetails.correspondentEmailID != 0
      && recordedCommDetails.newEmailAddress.length() != 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(
            BPOCOMMUNICATION.ERR_COMM_XFV_MULTIPLE_EMAILADDRESS),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }
    // END, CR00143201

    if (recordedCommDetails.caseID == 0) {

      // Only want to do this validation for a person.
      concernRoleKey.concernRoleID =
        recordedCommDetails.correspondentParticipantRoleID;

      if (concernRoleObj.readConcernRoleType(concernRoleKey).concernRoleType
        .equals(CONCERNROLETYPE.PERSON)) {

        // If correspondent is the client the correspondent type must be
        // client.
        if (recordedCommDetails.correspondentParticipantRoleID == recordedCommDetails.clientParticipantRoleID
          && !recordedCommDetails.correspondentType
            .equals(CORRESPONDENT.CLIENT)) {

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(
              new AppException(
                BPOCOMMUNICATION.ERR_COMM_CORRESPONDENT__IS_CLIENT),
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kError,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              1);
        }
      } else {

        // If correspondent is not a client the correspondent type must
        // not be
        // client.
        if (recordedCommDetails.correspondentParticipantRoleID != recordedCommDetails.clientParticipantRoleID
          && recordedCommDetails.correspondentType
            .equals(CORRESPONDENT.CLIENT)) {

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(
              new AppException(
                BPOCOMMUNICATION.ERR_COMM_CORRESPONDENT__NOT_CLIENT),
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kError,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              1);

        }
      }
    } else {

      final ValidatePrimaryCaseParticipantDetails validatePrimaryCaseParticipantDetails =
        new ValidatePrimaryCaseParticipantDetails();

      validatePrimaryCaseParticipantDetails.caseIDParticipantIDAndTypeDetails.caseID =
        recordedCommDetails.caseID;
      validatePrimaryCaseParticipantDetails.caseIDParticipantIDAndTypeDetails.participantRoleID =
        recordedCommDetails.correspondentParticipantRoleID;
      validatePrimaryCaseParticipantDetails.caseIDParticipantIDAndTypeDetails.typeCode =
        curam.codetable.CASEPARTICIPANTROLETYPE.PRIMARY;
      validatePrimaryCaseParticipantDetails.caseIDParticipantIDAndTypeDetails.recordStatus =
        curam.codetable.RECORDSTATUS.NORMAL;
      validatePrimaryCaseParticipantDetails.correspondentType =
        recordedCommDetails.correspondentType;

      validatePrimaryParticipantRole(validatePrimaryCaseParticipantDetails);
    }

    informationalManager.failOperation();
  }

}
