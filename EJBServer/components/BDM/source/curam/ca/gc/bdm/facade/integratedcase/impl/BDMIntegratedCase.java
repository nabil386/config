package curam.ca.gc.bdm.facade.integratedcase.impl;

import com.google.inject.Inject;
import curam.attachment.impl.Attachment;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONUSERTYPE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.entity.bdmcct.intf.BDMCCTTemplate;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTTemplateDtls;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTTemplateKey;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMCurrentDeductionsForProductDeliveryList;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMDeductionItemDetail;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMICAttachmentDetails;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMListICAttachmentDetails;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMListICTaskDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationAndListRowActionDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationDetailList;
import curam.ca.gc.bdm.facade.struct.BDMAttachmentDetailsList;
import curam.ca.gc.bdm.facade.struct.BDMListICProductDeliveryAttachmentDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainAttachmentFactory;
import curam.ca.gc.bdm.sl.attachment.intf.BDMMaintainAttachment;
import curam.ca.gc.bdm.sl.attachment.struct.BDMCaseAttachmentAndLinkDetails;
import curam.ca.gc.bdm.sl.attachment.struct.BDMCaseAttachmentAndLinkDetailsList;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.sl.struct.BDMTaskSearchDetails;
import curam.codetable.CASETYPECODE;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.PRODUCTTYPE;
import curam.contextviewer.constant.impl.CuramConst;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.struct.CommunicationDetailList;
import curam.core.facade.struct.CommunicationFilterKey;
import curam.core.facade.struct.CurrentDeductionsForProductDeliveryList;
import curam.core.facade.struct.DeductionItemDetail;
import curam.core.facade.struct.DeletePageIdentifier;
import curam.core.facade.struct.ICContextDescriptionDetails;
import curam.core.facade.struct.ICContextDescriptionKey_fo;
import curam.core.facade.struct.ICProductDeliveryContextDescription;
import curam.core.facade.struct.ICProductDeliveryContextDescriptionKey;
import curam.core.facade.struct.ICProductDeliveryMenuDataDetails;
import curam.core.facade.struct.ICProductDeliveryMenuDataKey;
import curam.core.facade.struct.IntegratedCaseMenuDataDetails;
import curam.core.facade.struct.IntegratedCaseMenuDataKey;
import curam.core.facade.struct.ListICAttachmentsKey;
import curam.core.facade.struct.ListICProductDeliveryAttachmentKey;
import curam.core.facade.struct.ListICProductDeliveryDeductionKey;
import curam.core.facade.struct.ListICTaskDetails;
import curam.core.facade.struct.ListICTaskKey_eo;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.impl.EnvVars;
import curam.core.impl.ProductHookManager;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.infrastructure.impl.GeneralConst;
import curam.core.sl.infrastructure.impl.ReflectionConst;
import curam.core.struct.AttachmentCaseID;
import curam.core.struct.CaseConcernRoleName;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseHomePageNameAndType;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ICHomePageNameAndType;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDeliveryKey;
import curam.message.BPOREFLECTION;
import curam.message.SEPARATOR;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.GeneralConstants;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * New Mthod to populate Deductions and Populate Add/Modify actions
 *
 * @author pavan.subbagari
 *
 */
public class BDMIntegratedCase
  extends curam.ca.gc.bdm.facade.integratedcase.base.BDMIntegratedCase {

  public BDMIntegratedCase() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.impl.BDMMaintainSupervisorUsers workspace;

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsers;

  protected static final int kBufSize = 256;

  @Inject
  protected Attachment attachment;

  @Override
  public BDMCurrentDeductionsForProductDeliveryList
    listCurrentProductDeliveryDeductions(
      final ListICProductDeliveryDeductionKey key)
      throws AppException, InformationalException {

    // final CurrentDeductionsForProductDeliveryList delList =
    // super.listCurrentProductDeliveryDeduction(key);

    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();
    final CurrentDeductionsForProductDeliveryList delList =
      integratedCase.listCurrentProductDeliveryDeduction(key);

    final BDMCurrentDeductionsForProductDeliveryList bdmDeductionList =
      new BDMCurrentDeductionsForProductDeliveryList();

    for (final DeductionItemDetail currentProdDelivery : delList.dtlsList) {

      final CaseDeductionItemKey caseDeductionItemIdKey =
        new CaseDeductionItemKey();
      caseDeductionItemIdKey.caseDeductionItemID =
        currentProdDelivery.caseDeductionItemID;
      final CaseDeductionItemDtls caseDeductionItemDtls =
        CaseDeductionItemFactory.newInstance().read(caseDeductionItemIdKey);

      final BDMDeductionKey deductionKey = new BDMDeductionKey();
      deductionKey.deductionID = caseDeductionItemDtls.deductionID;
      final BDMDeductionDetails bdmDeductionDtls =
        BDMDeductionFactory.newInstance().readByDeductionID(deductionKey);
      BDMDeductionItemDetail bdmDeductionDtl = new BDMDeductionItemDetail();

      if (bdmDeductionDtls.managedBy
        .equalsIgnoreCase(BDMDEDUCTIONUSERTYPE.AGENT)) {
        bdmDeductionDtl = new BDMDeductionItemDetail();
        bdmDeductionDtl.modifyInd = true;
        bdmDeductionDtl.deleteInd = true;
        if (currentProdDelivery.activeInd) {
          bdmDeductionDtl.deActivateInd = true;
          currentProdDelivery.activeInd = false;
        } else {
          currentProdDelivery.activeInd = true;
        }
      } else {
        currentProdDelivery.activeInd = false;
      }

      bdmDeductionDtl.assign(currentProdDelivery);
      bdmDeductionList.dtls.add(bdmDeductionDtl);
    }

    return bdmDeductionList;
  }

  ///// START: TASK 12776 : Add file source JP
  // ___________________________________________________________________________
  /**
   * List Attachments for Product Delivery Case
   * {@inheritDoc}
   */
  @Override
  public BDMListICProductDeliveryAttachmentDetails
    listProductDeliveryAttachment(
      final ListICProductDeliveryAttachmentKey key)
      throws AppException, InformationalException {

    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();

    // Create return object
    final BDMListICProductDeliveryAttachmentDetails listICProductDeliveryAttachmentDetails =
      new BDMListICProductDeliveryAttachmentDetails();

    // MaintainAttachment manipulation variable
    final BDMMaintainAttachment maintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    BDMAttachmentDetailsList attachmentDetailsList;

    // ICProductDeliveryContextDescriptionKey object
    final ICProductDeliveryContextDescriptionKey icProductDeliveryContextDescriptionKey =
      new ICProductDeliveryContextDescriptionKey();

    // ICProductDeliveryMenuDataKey object
    final ICProductDeliveryMenuDataKey icProductDeliveryMenuDataKey =
      new ICProductDeliveryMenuDataKey();

    // Set key to read the list of attachment
    final AttachmentCaseID attachmentCaseID = new AttachmentCaseID();
    attachmentCaseID.assign(key);

    // Call MaintainAttachment BPO to retrieve the list of attachments
    attachmentDetailsList =
      maintainAttachmentObj.readCaseAttachments(attachmentCaseID);

    // Check to see if the list is populated
    if (!attachmentDetailsList.dtls.isEmpty()) {

      // Reserve space in the return object
      listICProductDeliveryAttachmentDetails.attachmentDetailsList.dtls
        .ensureCapacity(attachmentDetailsList.dtls.size());

      // Assign details to return object
      listICProductDeliveryAttachmentDetails.attachmentDetailsList
        .assign(attachmentDetailsList);
    }

    // Set key to read context description
    icProductDeliveryContextDescriptionKey.caseID = key.caseID;

    // Read context description
    listICProductDeliveryAttachmentDetails.contextDescription =
      getICProductDeliveryContextDescription(
        icProductDeliveryContextDescriptionKey);

    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    caseHeaderKey.caseID = key.caseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

    if (caseHeaderDtls.integratedCaseID != 0) {
      // Set key to read menu data
      icProductDeliveryMenuDataKey.caseID = key.caseID;
      // Read menu data
      listICProductDeliveryAttachmentDetails.icProductDeliveryMenuData =
        getICProductDeliveryMenuData(icProductDeliveryMenuDataKey);
    }

    return listICProductDeliveryAttachmentDetails;
    // return null;
  }

  // @Override
  protected ICProductDeliveryMenuDataDetails
    getICProductDeliveryMenuData(final ICProductDeliveryMenuDataKey key)
      throws AppException, InformationalException {

    // BEGIN, 100358, VM
    final CaseKey caseKey = new CaseKey();

    caseKey.caseID = key.caseID;

    final CaseTypeCode caseTypeCode =
      CaseHeaderFactory.newInstance().readCaseTypeCode(caseKey);

    final ProductHookManager productHookManager = new ProductHookManager();
    final curam.core.facade.intf.MenuData menuDataObj =
      productHookManager.getMenuDataHook(caseTypeCode.caseTypeCode);

    return menuDataObj.getICProductDeliveryMenuData(key);
    // END, 100358
  }

  // ___________________________________________________________________________
  /**
   * List Attachments for Integrated Case
   * {@inheritDoc}
   */
  // @Override
  @Override
  public BDMListICAttachmentDetails
    listAttachment(final ListICAttachmentsKey key)
      throws AppException, InformationalException {

    // Create return object
    final BDMListICAttachmentDetails listICAttachmentDetails =
      new BDMListICAttachmentDetails();

    // MaintainAttachment manipulation variables

    // MaintainAttachment manipulation variable
    final BDMMaintainAttachment bdmmaintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    final AttachmentCaseID attachmentCaseID = new AttachmentCaseID();

    BDMCaseAttachmentAndLinkDetailsList bdmCaseAttachmentAndLinkDetailsList;

    // ICContextDescriptionKey facade object
    final ICContextDescriptionKey_fo icContextDescriptionKey =
      new ICContextDescriptionKey_fo();

    // IntegratedCaseMenuDataKey object
    final IntegratedCaseMenuDataKey integratedCaseMenuDataKey =
      new IntegratedCaseMenuDataKey();

    // Assign key to retrieve list of attachments
    attachmentCaseID.assign(key);

    // Call MaintainAttachments BPO to retrieve the list of attachments on
    // the Integrated Case

    bdmCaseAttachmentAndLinkDetailsList =
      bdmmaintainAttachmentObj.searchIntegCaseAttachments(attachmentCaseID);
    BDMCaseAttachmentAndLinkDetails details;

    BDMICAttachmentDetails bdmDetails;

    // Check to see if the list is populated
    if (!bdmCaseAttachmentAndLinkDetailsList.dtls.isEmpty()) {

      // Reserve space in the return object

      listICAttachmentDetails.attachmentDetailsList
        .ensureCapacity(bdmCaseAttachmentAndLinkDetailsList.dtls.size());

      // Assign details to return object

      for (int i = 0; i < bdmCaseAttachmentAndLinkDetailsList.dtls
        .size(); i++) {

        bdmDetails = new BDMICAttachmentDetails();

        details = bdmCaseAttachmentAndLinkDetailsList.dtls.item(i);

        bdmDetails.assign(details);

        listICAttachmentDetails.attachmentDetailsList.addRef(bdmDetails);

      }

    }

    // Set key to read context description
    icContextDescriptionKey.caseID = key.caseID;

    // Read context description and assign to return object
    listICAttachmentDetails.contextDescription =
      getIntegratedCaseContextDescription(icContextDescriptionKey);

    // Set key to read menu data
    integratedCaseMenuDataKey.caseID = key.caseID;

    // Read menu data and assign to return object
    listICAttachmentDetails.icMenuData =
      getIntegratedCaseMenuData(integratedCaseMenuDataKey);

    return listICAttachmentDetails;
  }

  // ___________________________________________________________________________
  /**
   * {@inheritDoc}
   */
  // @Override
  protected ICContextDescriptionDetails
    getIntegratedCaseContextDescription(final ICContextDescriptionKey_fo key)
      throws AppException, InformationalException {

    // Create return object
    final ICContextDescriptionDetails icContextDescriptionDetails =
      new ICContextDescriptionDetails();

    // CaseHeader manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();

    ICHomePageNameAndType icHomePageNameAndType;

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();

    // Read caseHeader
    caseKey.caseID = key.caseID;
    icHomePageNameAndType = caseHeaderObj.readICHomePageNameAndType(caseKey);

    // Create the context description
    final StringBuffer buffer = new StringBuffer(kBufSize);

    buffer.append(CodeTable.getOneItem(PRODUCTCATEGORY.TABLENAME,
      icHomePageNameAndType.integratedCaseType,
      TransactionInfo.getProgramLocale()));

    buffer.append(CuramConst.kSpace);

    buffer.append(SEPARATOR.INF_SEPARATOR_CONTEXT_DESCRIPTION
      .getMessageText(TransactionInfo.getProgramLocale()));

    buffer.append(icHomePageNameAndType.caseReference);

    // Assign it to the return object
    icContextDescriptionDetails.description = buffer.toString();

    return icContextDescriptionDetails;
  }

  // ___________________________________________________________________________
  /**
   * {@inheritDoc}
   */
  // @Override
  protected IntegratedCaseMenuDataDetails
    getIntegratedCaseMenuData(final IntegratedCaseMenuDataKey key)
      throws AppException, InformationalException {

    // BEGIN, 100358, VM
    final CaseKey caseKey = new CaseKey();

    caseKey.caseID = key.caseID;

    final CaseTypeCode caseTypeCode =
      CaseHeaderFactory.newInstance().readCaseTypeCode(caseKey);

    final ProductHookManager productHookManager = new ProductHookManager();
    final curam.core.facade.intf.MenuData menuDataObj =
      productHookManager.getMenuDataHook(caseTypeCode.caseTypeCode);

    return menuDataObj.getIntegratedCaseMenuData(key);
    // END, 100358
  }

  // @Override
  protected ICProductDeliveryContextDescription
    getICProductDeliveryContextDescription(
      final ICProductDeliveryContextDescriptionKey key)
      throws AppException, InformationalException {

    // Create the return object
    final ICProductDeliveryContextDescription icProductDeliveryContextDescription =
      new ICProductDeliveryContextDescription();

    // Check the Environmental variable
    // Retrieve the Environment variable for the Appeals component
    // installation
    // setting. If the environment variable is present and set to 'true',
    // run
    // the Reflection code.
    if (Configuration.getBooleanProperty(EnvVars.ENV_APPEALS_ISINSTALLED)) {

      final String className =
        ReflectionConst.coreToAppealDelegateWrapperClassName;

      final String methodName =
        ReflectionConst.icProductDeliveryContextMethodName;

      // Set the arguments to pass through
      final Object[] arguments = new Object[]{key };

      // Set the passed class types to the method
      final Class[] parameterTypes =
        new Class[]{ICProductDeliveryContextDescriptionKey.class };

      return (ICProductDeliveryContextDescription) performReflection(
        className, methodName, arguments, parameterTypes);
    }

    // CaseHeader manipulation variables
    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    CaseConcernRoleName caseConcernRoleName;
    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();

    // ProductDelivery manipulation variables
    final curam.core.intf.ProductDelivery productDeliveryObj =
      curam.core.fact.ProductDeliveryFactory.newInstance();
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
    ProductDeliveryDtls productDeliveryDtls;

    // ConcernRole manipulation variables
    final curam.core.intf.ConcernRole concernRoleObj =
      curam.core.fact.ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    // Set key to read ProductDelivery
    productDeliveryKey.caseID = key.caseID;

    // Read ProductDelivery
    productDeliveryDtls = productDeliveryObj.read(productDeliveryKey);

    // Set key to read concernRoleName
    concernRoleKey.concernRoleID = productDeliveryDtls.recipConcernRoleID;

    // Read ConcernRole
    // BEGIN CR00103984 SAI
    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleKey);
    // END CR00103984
    CaseHomePageNameAndType caseHomePageNameAndType;

    // Set key to read caseHeader
    caseKey.caseID = key.caseID;

    // Read caseHeader
    caseConcernRoleName = caseHeaderObj.readCaseConcernRoleName(caseKey);

    // Read productDelivery
    caseHomePageNameAndType =
      productDeliveryObj.readCaseHomePageNameAndType(caseKey);

    // Assign it to the return object
    icProductDeliveryContextDescription.description =
      CodeTable.getOneItem(PRODUCTTYPE.TABLENAME,
        caseHomePageNameAndType.typeCode, TransactionInfo.getProgramLocale())

        + GeneralConstants.kSpace + caseConcernRoleName.caseReference
        + GeneralConstants.kSpace + GeneralConstants.kMinus
        + GeneralConstants.kSpace

        + concernRoleDtls.concernRoleName + GeneralConstants.kSpace
        + concernRoleDtls.primaryAlternateID;

    return icProductDeliveryContextDescription;
  }

  // ___________________________________________________________________________
  /**
   * Execute reflection on the Argument Class and Method, using argument
   * parameters.
   *
   * @param className
   * Class name to instantiate
   * @param methodName
   * Method to be run
   * @param arguments
   * Arguments to be passed to Method
   * @param parameterTypes
   * Class Types of Parameters used
   *
   * @return Invoked method will return object which can be case by cast
   * method.
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  protected Object performReflection(final String className,
    final String methodName, final Object[] arguments,
    final Class[] parameterTypes)
    throws AppException, InformationalException {

    Class cls = null;

    try {
      // Load the class
      cls = Class.forName(className);

      // Create a method for the static newInstance constructor

      final Method constructorMethod =
        cls.getMethod(ReflectionConst.kNewInstance);

      final Object classObj = constructorMethod.invoke(cls);

      // Define the method

      final Method method =
        classObj.getClass().getMethod(methodName, parameterTypes);

      // Now invoke the method
      return method.invoke(classObj, arguments);

    } catch (final IllegalAccessException e) {// ignore - biz methods MUST
      // be public
    } catch (final ClassNotFoundException e) {

      final AppException ae =
        new AppException(BPOREFLECTION.ERR_REFLECTION_CLASS_NOT_FOUND);

      ae.arg(className);
      ae.arg(getClass().toString());

      ae.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME, CASETYPECODE.APPEAL,
        TransactionInfo.getProgramLocale()));

      throw new RuntimeException(ae);

    } catch (final NoSuchMethodException e) {

      final AppException ae =
        new AppException(BPOREFLECTION.ERR_REFLECTION_NO_SUCH_METHOD);

      ae.arg(className);

      ae.arg(getClass().toString() + GeneralConst.gDot + methodName);

      ae.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME, CASETYPECODE.APPEAL,
        TransactionInfo.getProgramLocale()));

      throw new RuntimeException(ae);

    } catch (final IllegalArgumentException e) {

      final AppException ae =
        new AppException(BPOREFLECTION.ERR_ILLEGAL_FUNCTION_ARGUMENTS);

      ae.arg(getClass().toString() + GeneralConst.gDot + methodName);

      ae.arg(className);
      ae.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME, CASETYPECODE.APPEAL,
        TransactionInfo.getProgramLocale()));

      throw new RuntimeException(ae);

    } catch (final InvocationTargetException e) {

      final AppException exc =
        new AppException(BPOREFLECTION.ERR_BPO_MESSAGE_RETURNED);

      exc.arg(e.getTargetException().getMessage());
      throw exc;
    }

    return null;
  }

  @Override
  public BDMCommunicationDetailList
    listFilteredCommunication(final CommunicationFilterKey key)
      throws AppException, InformationalException {

    final BDMCommunicationDetailList bdmCommList =
      new BDMCommunicationDetailList();
    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();
    final CommunicationDetailList commList =
      integratedCase.listFilteredCommunication(key);
    bdmCommList.assign(commList);
    // Populate delete page for all correspondence records.
    final DeletePageIdentifier deletePageIdentifier =
      new DeletePageIdentifier();
    deletePageIdentifier.deletePageName =
      curam.core.impl.CuramConst.gkStoreCuramReturnPage
        + BDMConstants.kdeleteCorrespondencePageIdentifier;
    // BUG-91516, Start
    // Populate workItemID and Tracking Number.
    final BDMCommunicationHelper commHelper = new BDMCommunicationHelper();
    final BDMConcernRoleCommunication bdmCommObj =
      BDMConcernRoleCommunicationFactory.newInstance();
    final BDMConcernRoleCommunicationKey bdmCommKey =
      new BDMConcernRoleCommunicationKey();
    BDMConcernRoleCommunicationDtls bdmCommDtls =
      new BDMConcernRoleCommunicationDtls();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    // BUG-103323, Start
    final BDMCCTTemplateKey templateKey = new BDMCCTTemplateKey();
    final BDMCCTTemplate templateObj = BDMCCTTemplateFactory.newInstance();
    BDMCCTTemplateDtls templateDtls = new BDMCCTTemplateDtls();
    ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
      new ConcernRoleCommunicationDtls();
    final ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new ConcernRoleCommunicationKey();
    final ConcernRoleCommunication concernRoleCommunicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    // BUG-103323, End
    for (int i = 0; i < commList.communicationDtls.size(); i++) {
      bdmCommList.communicationDtls.get(i).commDtls =
        commList.communicationDtls.get(i);
      final BDMCommunicationAndListRowActionDetails item =
        bdmCommList.communicationDtls.get(i);
      commHelper.determineActionMenuIndicator(item);

      if (COMMUNICATIONFORMAT.CORRESPONDENCE.equalsIgnoreCase(
        bdmCommList.communicationDtls.get(i).commDtls.communicationFormat)) {
        bdmCommList.communicationDtls.get(i).commDtls.deletePage =
          deletePageIdentifier;
        // BUG-103323, Start
        // Populate subject in the format of
        // ISS-1234 + Space + Description
        // Need to fetch templateID from communication
        concernRoleCommunicationKey.communicationID =
          bdmCommList.communicationDtls.get(i).commDtls.communicationID;
        concernRoleCommunicationDtls = concernRoleCommunicationObj
          .read(nfIndicator, concernRoleCommunicationKey);
        if (!nfIndicator.isNotFound()) {
          try {
            templateKey.templateID =
              Long.parseLong(concernRoleCommunicationDtls.documentTemplateID);
          } catch (final NumberFormatException nfe) {
            templateKey.templateID = curam.core.impl.CuramConst.gkZero;
          }
          if (curam.core.impl.CuramConst.gkZero != templateKey.templateID) {
            templateDtls = templateObj.read(nfIndicator, templateKey);
            if (!nfIndicator.isNotFound()) {
              bdmCommList.communicationDtls.get(i).commDtls.subjectText =
                templateDtls.templateName + curam.core.impl.CuramConst.gkSpace
                  + templateDtls.templateDesc;
            }
          }
        }
        // BUG-103323, End

        // Populate the workitemID and tracking number
        bdmCommKey.communicationID = item.commDtls.communicationID;
        bdmCommDtls = bdmCommObj.read(nfIndicator, bdmCommKey);
        if (!nfIndicator.isNotFound()) {
          item.workItemID = bdmCommDtls.workItemID;
          item.trackingNumber = bdmCommDtls.trackingNumber + "";
          // BUG-91598, Start
          // Tracking Number should not be enabled for statuses
          // 1. Suppressed
          // 2. Misdirected
          // 3. Cancelled
          if (COMMUNICATIONSTATUS.SUPPRESS
            .equalsIgnoreCase(item.commDtls.communicationStatus)
            || COMMUNICATIONSTATUS.MISDIRECTED
              .equalsIgnoreCase(item.commDtls.communicationStatus)
            || COMMUNICATIONSTATUS.CANCELLED
              .equalsIgnoreCase(item.commDtls.communicationStatus)) {
            item.enableTrackingInd = false;
          } else {
            item.enableTrackingInd = true;
          }
          // BUG-91598, End
        }
      } else if (BDMConstants.kConversionIdentifier
        .equalsIgnoreCase(item.documentLocation)
        && COMMUNICATIONFORMAT.RECORDED
          .equalsIgnoreCase(bdmCommList.communicationDtls
            .get(i).commDtls.communicationFormat)) {
        // BUG-120974, Start
        // populate tracking number for MIGRATED_DATA// read the correspondence tracking
        // number from
        // BDMConcernRoleCommunication
        final BDMConcernRoleCommunicationKey commKey =
          new BDMConcernRoleCommunicationKey();
        commKey.communicationID =
          bdmCommList.communicationDtls.get(i).commDtls.communicationID;

        final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
          BDMConcernRoleCommunicationFactory.newInstance().read(nfIndicator,
            commKey);

        if (!nfIndicator.isNotFound()) {
          // set the tracking number
          bdmCommList.communicationDtls.get(i).trackingNumber =
            bdmConcernRoleCommunicationDtls.trackingNumber + "";
        }
        // BUG-102974, End
      }
    }

    // BUG-91516, End
    return bdmCommList;
  }

  @Override
  public BDMListICTaskDetails listTask(final ListICTaskKey_eo key)
    throws AppException, InformationalException {

    final BDMListICTaskDetails bdmListICTaskDetails =
      new BDMListICTaskDetails();

    ListICTaskDetails listICTaskDetails = new ListICTaskDetails();

    final IntegratedCase integratedCase = IntegratedCaseFactory.newInstance();

    listICTaskDetails = integratedCase.listTask(key);
    bdmListICTaskDetails.assign(listICTaskDetails);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMTaskSearchDetails bdmTaskSearchDetails = null;
    BDMEscalationLevelString bdmEscalationLevel = null;

    for (int i = 0; i < bdmListICTaskDetails.detailsList.dtls.size(); i++) {

      bdmTaskSearchDetails = new BDMTaskSearchDetails();

      bdmTaskSearchDetails = bdmListICTaskDetails.detailsList.dtls.get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmTaskSearchDetails.taskID;

      caseUrgentFlagStringDetails =
        workspace.getCaseUrgentFlagsByTaskID(taskKey);

      bdmTaskSearchDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      bdmEscalationLevel = new BDMEscalationLevelString();
      bdmEscalationLevel =
        bdmMaintainSupervisorUsers.readEscalationLevelByTaskID(taskKey);

      bdmTaskSearchDetails.escalationLevelDesc =
        bdmEscalationLevel.escalationLevelDesc;

    }

    return bdmListICTaskDetails;
  }

}
