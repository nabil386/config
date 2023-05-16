package curam.ca.gc.bdm.facade.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.entity.bdmcct.intf.BDMCCTTemplate;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTTemplateDtls;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTTemplateKey;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.fact.BDMPhoneNumberFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.intf.BDMPhoneNumber;
import curam.ca.gc.bdm.entity.struct.BDMPhoneNumberDtls;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberDetails;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberKey;
import curam.ca.gc.bdm.facade.address.fact.BDMAddressFactory;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationAndListRowActionDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationDetailList;
import curam.ca.gc.bdm.facade.participant.struct.BDMConcernRolePhoneDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMCreateContactFromUnregParticipant;
import curam.ca.gc.bdm.facade.participant.struct.BDMDate;
import curam.ca.gc.bdm.facade.participant.struct.BDMListConcernContactDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMListParticipantFinancials1;
import curam.ca.gc.bdm.facade.participant.struct.BDMMaintainParticipantPhoneDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMParticipantFinancials;
import curam.ca.gc.bdm.facade.participant.struct.BDMReadParticipantAddressDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMTasksForConcernAndCaseDetails;
import curam.ca.gc.bdm.facade.productdelivery.impl.BDMViewPaymentsUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.sl.fact.BDMMaintainConcernRolePhoneFactory;
import curam.ca.gc.bdm.sl.financial.impl.BDMFinancial;
import curam.ca.gc.bdm.sl.intf.BDMMaintainConcernRolePhone;
import curam.ca.gc.bdm.sl.struct.BDMTaskSearchDetails;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.ParticipantFactory;
import curam.core.facade.intf.Participant;
import curam.core.facade.struct.CommunicationAndListRowActionDetails;
import curam.core.facade.struct.CommunicationDetailList;
import curam.core.facade.struct.CreateParticipantAddressDetails;
import curam.core.facade.struct.CreateParticipantEmailAddressDetails;
import curam.core.facade.struct.CreateParticipantPhoneDetails;
import curam.core.facade.struct.DeletePageIdentifier;
import curam.core.facade.struct.ListConcernContactDetails;
import curam.core.facade.struct.ListContactKey;
import curam.core.facade.struct.ListParticipantFinancials1;
import curam.core.facade.struct.ListParticipantFinancialsKey;
import curam.core.facade.struct.ListParticipantTaskKey_eo;
import curam.core.facade.struct.MaintainParticipantAddressDetails;
import curam.core.facade.struct.MaintainParticipantEmailAddressDetails;
import curam.core.facade.struct.MaintainParticipantPhoneDetails;
import curam.core.facade.struct.ModifiedAddressDetails;
import curam.core.facade.struct.ParticipantCommunicationKey;
import curam.core.facade.struct.ParticipantContextKey;
import curam.core.facade.struct.ParticipantFinancials1;
import curam.core.facade.struct.ReadParticipantAddressDetails;
import curam.core.facade.struct.ReadParticipantAddressKey;
import curam.core.facade.struct.ReadParticipantAddressList;
import curam.core.facade.struct.ReadParticipantAddressListKey;
import curam.core.facade.struct.ReadParticipantPhoneNumberDetails;
import curam.core.facade.struct.ReadParticipantPhoneNumberKey;
import curam.core.facade.struct.ReadParticipantPhoneNumberList;
import curam.core.facade.struct.ReadParticipantPhoneNumberListKey;
import curam.core.facade.struct.TasksForConcernAndCaseDetails;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PersonFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AlternateName;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.PaymentInstruction;
import curam.core.sl.entity.struct.DuplicateForConcernRoleDtls;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.struct.SearchAlternateIDDetailsKey;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressRMDtls;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameDtlsList;
import curam.core.struct.AlternateNameKey;
import curam.core.struct.AlternateNameReadByTypeKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernContactRMultiDtls;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRolePhoneDetails;
import curam.core.struct.CuramInd;
import curam.core.struct.FinInstructionID;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.MaintainPhoneNumberKey;
import curam.core.struct.PaymentInstructionDtls;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.SearchForAlternateNameKey;
import curam.pdc.fact.PDCAlternateNameFactory;
import curam.pdc.intf.PDCAlternateName;
import curam.pdc.struct.ParticipantAlternateNameDetails;
import curam.piwrapper.impl.AddressDAO;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class BDMParticipant
  extends curam.ca.gc.bdm.facade.participant.base.BDMParticipant {

  private final BDMUtil bdmUtil = new BDMUtil();

  public BDMParticipant() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.impl.BDMMaintainSupervisorUsers workspace;

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsers;

  @Inject
  public AddressDAO addressDAO;

  @Inject
  public BDMFinancial bdmFinancialObj;

  protected static final int kMinimumPhoneNumberLength = 1;

  @Override
  public BDMCommunicationDetailList
    listCommunication(final ParticipantCommunicationKey key)
      throws AppException, InformationalException {

    // Original Person Communicatoin list
    final BDMCommunicationDetailList bdmCommList =
      new BDMCommunicationDetailList();
    final CommunicationDetailList _commList =
      ParticipantFactory.newInstance().listCommunication(key);

    curam.core.sl.entity.struct.DuplicateForConcernRoleDtlsList duplicateForConcernRoleDtlsList =
      new curam.core.sl.entity.struct.DuplicateForConcernRoleDtlsList();

    duplicateForConcernRoleDtlsList = BDMUtil
      .getDuplicatePersonOfOriginal(key.participantCommKey.concernRoleID);

    CommunicationDetailList _duplicateConcernCommList =
      new CommunicationDetailList();

    if (duplicateForConcernRoleDtlsList.dtls.size() > 0) {
      for (final DuplicateForConcernRoleDtls duplicateForConcernRoleDtls : duplicateForConcernRoleDtlsList.dtls) {
        final ParticipantCommunicationKey duplicateConcernKey =
          new ParticipantCommunicationKey();
        duplicateConcernKey.participantCommKey.concernRoleID =
          duplicateForConcernRoleDtls.duplicateConcernRoleID;
        _duplicateConcernCommList = ParticipantFactory.newInstance()
          .listCommunication(duplicateConcernKey);

        for (int i = 0; i < _duplicateConcernCommList.communicationDtls
          .size(); i++) {
          _commList.communicationDtls
            .addRef(_duplicateConcernCommList.communicationDtls.get(i));
        }
      }
    }

    // Task 68028 to fix bug 66743- Do not display Appeal Notice and Request for
    // Reconsideration in Communications list

    final Iterator<CommunicationAndListRowActionDetails> itr =
      _commList.communicationDtls.iterator();
    while (itr.hasNext()) {
      final CommunicationAndListRowActionDetails communicationAndListRowActionDetails =
        itr.next();
      if (communicationAndListRowActionDetails.subjectText
        .equalsIgnoreCase("Appeal Notice")
        || communicationAndListRowActionDetails.subjectText
          .equalsIgnoreCase("Request for Reconsideration")) {
        itr.remove();
      }
    }
    bdmCommList.assign(_commList);

    // Populate delete page for all correspondence records.
    final DeletePageIdentifier deletePageIdentifier =
      new DeletePageIdentifier();
    deletePageIdentifier.deletePageName = CuramConst.gkStoreCuramReturnPage
      + BDMConstants.kdeleteCorrespondencePageIdentifier;

    // BUG-103323, Start
    final BDMCCTTemplateKey templateKey = new BDMCCTTemplateKey();
    final BDMCCTTemplate templateObj = BDMCCTTemplateFactory.newInstance();
    BDMCCTTemplateDtls templateDtls = new BDMCCTTemplateDtls();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    ConcernRoleCommunicationDtls concernRoleCommunicationDtls =
      new ConcernRoleCommunicationDtls();
    final ConcernRoleCommunicationKey concernRoleCommunicationKey =
      new ConcernRoleCommunicationKey();
    final ConcernRoleCommunication concernRoleCommunicationObj =
      ConcernRoleCommunicationFactory.newInstance();
    // BUG-103323, End

    for (int i = 0; i < _commList.communicationDtls.size(); i++) {
      bdmCommList.communicationDtls.get(i).commDtls =
        _commList.communicationDtls.get(i);
      final BDMCommunicationAndListRowActionDetails item =
        bdmCommList.communicationDtls.get(i);
      if (item.commDtls.communicationFormat
        .equals(COMMUNICATIONFORMAT.PROFORMA)
        && item.commDtls.communicationStatus
          .equals(COMMUNICATIONSTATUS.SENT)) {
        // add print details when bulk print is ready
      }
      final BDMCommunicationHelper commHelper = new BDMCommunicationHelper();
      commHelper.determineActionMenuIndicator(item);

      // get the correspondence tracking number for correspondence
      // communication
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
            templateKey.templateID = CuramConst.gkZero;
          }
          if (CuramConst.gkZero != templateKey.templateID) {
            templateDtls = templateObj.read(nfIndicator, templateKey);
            if (!nfIndicator.isNotFound()) {
              bdmCommList.communicationDtls.get(i).commDtls.subjectText =
                templateDtls.templateName + CuramConst.gkSpace
                  + templateDtls.templateDesc;
            }
          }
        }
        // BUG-103323, End

        // read the correspondence tracking number from
        // BDMConcernRoleCommunication
        final BDMConcernRoleCommunicationKey commKey =
          new BDMConcernRoleCommunicationKey();
        commKey.communicationID =
          bdmCommList.communicationDtls.get(i).commDtls.communicationID;

        final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
          BDMConcernRoleCommunicationFactory.newInstance().read(nfIndicator,
            commKey);

        if (!nfIndicator.isNotFound())
          // set the tracking number
          bdmCommList.communicationDtls.get(i).trackingNumber =
            bdmConcernRoleCommunicationDtls.trackingNumber + "";
        // Set the workItemID
        bdmCommList.communicationDtls.get(i).workItemID =
          bdmConcernRoleCommunicationDtls.workItemID;

        // BUG-92853, Start
        // Tracking Number should not be enabled for statuses
        // 1. Suppressed
        // 2. Misdirected
        // 3. Cancelled
        if (COMMUNICATIONSTATUS.SUPPRESS.equalsIgnoreCase(
          bdmCommList.communicationDtls.get(i).commDtls.communicationStatus)
          || COMMUNICATIONSTATUS.MISDIRECTED.equalsIgnoreCase(
            bdmCommList.communicationDtls.get(i).commDtls.communicationStatus)
          || COMMUNICATIONSTATUS.CANCELLED
            .equalsIgnoreCase(bdmCommList.communicationDtls
              .get(i).commDtls.communicationStatus)) {
          bdmCommList.communicationDtls.get(i).enableTrackingInd = false;
        } else {
          bdmCommList.communicationDtls.get(i).enableTrackingInd = true;
        }
        // BUG-92853, End

      }

    }

    return bdmCommList;
  }

  @Override
  public BDMReadParticipantAddressDetails
    readMailingAddress(final ReadParticipantAddressListKey key)
      throws AppException, InformationalException {

    final BDMReadParticipantAddressDetails addressDetails =
      new BDMReadParticipantAddressDetails();
    final ReadParticipantAddressList addrList =
      ParticipantFactory.newInstance().listAddress(key);
    AddressRMDtls mailingAddr = null;
    for (final AddressRMDtls addrDetails : addrList.readMultiByConcRoleIDResult.details.dtls
      .items()) {
      if (Date.getCurrentDate().before(addrDetails.startDate)
        || !addrDetails.endDate.isZero()
          && Date.getCurrentDate().after(addrDetails.endDate)) {
        continue;
      }

      if (addrDetails.typeCode.equals(CONCERNROLEADDRESSTYPE.MAILING)) {
        if (mailingAddr != null && !addrDetails.primaryInd)
          continue;
        mailingAddr = addrDetails;
      }
    }

    if (mailingAddr == null) {
      return addressDetails;
    }
    final ReadParticipantAddressKey addrkey = new ReadParticipantAddressKey();
    addrkey.readConcernRoleAddressKey.concernRoleAddressID =
      mailingAddr.concernRoleAddressID;
    final ReadParticipantAddressDetails participantAddrDetails =
      ParticipantFactory.newInstance().readAddress(addrkey);
    addressDetails.dtls = participantAddrDetails;
    addressDetails.addressID = mailingAddr.addressID;
    addressDAO.get(mailingAddr.addressID);
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = mailingAddr.addressID;
    addressDetails.dtls.addressDetails.formattedAddressData =
      BDMAddressFactory.newInstance().getCanadaPostAddressFormat(addressKey,
        null).addressData;

    return addressDetails;
  }

  /**
   * Gets a list of the participant's financials and modifies the status to read
   * from BDMFinancialInstruction. Sorts by processed date descending, modifies
   * payment display indicator according to the BDM Financial Instruction status
   * and removes any cancelled/reversed payments that are linked to a reissued
   * payment
   */
  @Override
  public BDMListParticipantFinancials1
    listParticipantFinancial1(final ListParticipantFinancialsKey key)
      throws AppException, InformationalException {

    // initialize return struct
    final BDMListParticipantFinancials1 bdmListFinancials =
      new BDMListParticipantFinancials1();

    // call ootb method
    final ListParticipantFinancials1 listParticipantFinancials =
      ParticipantFactory.newInstance().listParticipantFinancial1(key);

    // filter out linked cancelled and reversed payments
    filterOutLinkedCancelledAndReversedPayments(listParticipantFinancials);

    // assign details to new return struct
    bdmListFinancials.assign(listParticipantFinancials);

    BDMParticipantFinancials bdmFinancials;
    ParticipantFinancials1 participantFinancials;
    for (int i =
      0; i < listParticipantFinancials.participantFinancialsList.dtls
        .size(); i++) {

      bdmFinancials = bdmListFinancials.participantFinancialsList.dtls.get(i);

      participantFinancials =
        listParticipantFinancials.participantFinancialsList.dtls.get(i);

      bdmFinancials.dtls = participantFinancials;

      // check if it's invalidated
      final boolean isInvalidated = BDMViewPaymentsUtil
        .isInvalidated(participantFinancials.finInstructionID);

      // START - Task 21028 - Added changes for code refactoring after removing
      // BDMFinInstruction entity.
      final PaymentInstruction paymentInstructionObj =
        PaymentInstructionFactory.newInstance();
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      final FinInstructionID finInstructionIDKey = new FinInstructionID();
      finInstructionIDKey.finInstructionID =
        participantFinancials.finInstructionID;
      final PaymentInstructionDtls paymentInstructionDtls =
        paymentInstructionObj.readByFinInstructionID(notFoundIndicator,
          finInstructionIDKey);

      String statusCode = "";
      if (!notFoundIndicator.isNotFound()) {
        statusCode = BDMViewPaymentsUtil.getBDMPaymentInstructionStatusCode(
          paymentInstructionDtls.pmtInstrumentID);
        bdmFinancials.bdmStatusCode =
          BDMViewPaymentsUtil.getPaymentStatus(statusCode, isInvalidated);

        // START update method of delivery
        final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
          new BDMPaymentInstrumentKey();
        bdmPaymentInstrumentKey.pmtInstrumentID =
          paymentInstructionDtls.pmtInstrumentID;
        final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
          BDMPaymentInstrumentFactory.newInstance()
            .read(bdmPaymentInstrumentKey);
        if (!StringUtil
          .isNullOrEmpty(bdmPaymentInstrumentDtls.spsDeliveryMethodType)) {
          participantFinancials.deliveryMethodType =
            bdmPaymentInstrumentDtls.spsDeliveryMethodType;
        }
        // END - update method of delivery - JSHAH
      } else {
        statusCode = BDMViewPaymentsUtil.getFinInstructionStatusCode(
          participantFinancials.finInstructionID);
        bdmFinancials.bdmStatusCode = BDMViewPaymentsUtil
          .getPaymentStatusForFinInstruction(statusCode, isInvalidated);

      }
      // END Task - 21028 - JSHAH

      // modify payment display indicators
      participantFinancials.displayIndicators =
        BDMViewPaymentsUtil.modifyPaymentDisplayIndicators(
          participantFinancials.displayIndicators, statusCode, isInvalidated);

    }

    final Comparator<BDMParticipantFinancials> comparator =
      new Comparator<BDMParticipantFinancials>() {

        @Override
        public int compare(final BDMParticipantFinancials o1,
          final BDMParticipantFinancials o2) {

          // compares the items based off their effective date and multiplied by
          // negative 1 to enforce sorting in descending order
          return -1 * o1.dtls.effectiveDate.compareTo(o2.dtls.effectiveDate);
        }
      };

    // sort by effective date descending
    bdmListFinancials.participantFinancialsList.dtls.sort(comparator);

    return bdmListFinancials;
  }

  /**
   * Filters out any cancelled payments or reversals that are linked to another
   * payment as they do not want to be displayed
   *
   * @param listParticipantFinancials
   * @throws AppException
   * @throws InformationalException
   */
  private void filterOutLinkedCancelledAndReversedPayments(
    final ListParticipantFinancials1 listParticipantFinancials)
    throws AppException, InformationalException {

    final Set<Long> totalLinkedFIs = new HashSet<Long>();

    // iterate through current list of financials
    for (final ParticipantFinancials1 caseFinancialDetails : listParticipantFinancials.participantFinancialsList.dtls) {
      bdmFinancialObj.addLinkedFIsToRemove(totalLinkedFIs,
        caseFinancialDetails.typeCode, caseFinancialDetails.statusCode,
        caseFinancialDetails.finInstructionID);

    }

    final Predicate<ParticipantFinancials1> shouldRemove =
      details -> totalLinkedFIs.contains(new Long(details.finInstructionID));

    // remove linked FIs
    listParticipantFinancials.participantFinancialsList.dtls
      .removeIf(shouldRemove);

  }

  /**
   * Creates a contact record for a participant who was not previously
   * registered.
   *
   * @param details
   * Registration details for the contact
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */

  @Override
  public void createContactFromUnregisteredParticipant(
    final BDMCreateContactFromUnregParticipant details)
    throws AppException, InformationalException {

    // convert the code table code to phone country code
    if (!details.phoneCountryCode.isEmpty()) {
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneCountryCode =
        getPhoneCountryCodeByCodeTableCode(details.phoneCountryCode);
    }

    bdmUtil.validatePhoneNumberDetails(
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneCountryCode,
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode,
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber,
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneExtension);

    // BEGIN TASK-25344 Remove unused Variables
    // final ConcernContactRMultiDtls concernContactRMultiDtls =
    registerContactDetails(details);
  }

  /**
   * Register a contact record for a participant who was not previously
   * registered.
   *
   * @param details
   * Registration details for the contact
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */

  private ConcernContactRMultiDtls
    registerContactDetails(final BDMCreateContactFromUnregParticipant details)
      throws AppException, InformationalException {

    final curam.core.intf.MaintainContacts maintainContactsObj =
      curam.core.fact.MaintainContactsFactory.newInstance();
    final ConcernContactRMultiDtls concernContactDtls =
      new ConcernContactRMultiDtls();
    // Concern Role object and key to get concern name.
    final curam.core.intf.ConcernRole concernRoleObj =
      curam.core.fact.ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    ConcernRoleDtls concernRoleDtls;

    // current date
    final curam.util.type.Date currentDate =
      curam.util.type.Date.getCurrentDate();

    // Representative maintenance object
    final curam.core.sl.intf.Representative representativeObj =
      curam.core.sl.fact.RepresentativeFactory.newInstance();

    // Struct passed to Representative::registerRepresentative
    final curam.core.sl.struct.RepresentativeRegistrationDetails representativeRegistrationDetails =
      new curam.core.sl.struct.RepresentativeRegistrationDetails();

    // Check to ensure the Contact Name has been entered
    if (details.dtls.createContactFromUnregisteredParticipant.name
      .length() == 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(
            curam.message.BPOMAINTAINCONTACTS.ERR_CONTACTS_FV_NAME_EMPTY),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Check that phone number is at least three digits if not empty,
    if (details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber
      .length() < kMinimumPhoneNumberLength
      && details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber
        .length() > 0) {
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(new AppException(
          curam.message.BPOMAINTAINCONTACTPHONENUMBER.ERR_CONTACTPHONE_FV_SHORT),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Check for mandatory provision of phone number with extension
    if (details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneExtension
      .length() > 0
      && details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber
        .length() == 0) {
      final AppException e = new AppException(
        curam.message.BPOMAINTAINCONTACTPHONENUMBER.ERR_CONTACT_XFV_EXTENSION_EMPTY);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(e,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Check for mandatory provision of phone number with country code
    if (details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneCountryCode
      .length() > 0
      && details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber
        .length() == 0) {
      final AppException e = new AppException(
        curam.message.BPOMAINTAINCONTACTPHONENUMBER.ERR_CONTACT_XFV_COUNTRYCODE_EMPTY);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(e,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Check for mandatory provision of phone number with area code
    if (details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode
      .length() > 0
      && details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber
        .length() == 0) {
      final AppException e = new AppException(
        curam.message.BPOMAINTAINCONTACTPHONENUMBER.ERR_CONTACT_XFV_AREACODE_EMPTY);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(e,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Read Concern Role record for the Concern for which
    // the contact is being created
    concernRoleKey.concernRoleID =
      details.dtls.createContactFromUnregisteredParticipant.concernRoleID;
    concernRoleDtls = concernRoleObj.read(concernRoleKey);

    representativeRegistrationDetails.representativeDtls.representativeName =
      details.dtls.createContactFromUnregisteredParticipant.name;

    // START : TASK-118752 : Set the BDMUNPARSE INDICATOR for intl address
    representativeRegistrationDetails.representativeRegistrationDetails.addressData =
      bdmUtil.setBDMUnparsedIndForINTLAddress(
        details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.addressData);
    // END TASK-118752
    representativeRegistrationDetails.representativeRegistrationDetails.phoneCountryCode =
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneCountryCode;
    representativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode =
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode;
    representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber =
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber;
    representativeRegistrationDetails.representativeRegistrationDetails.phoneExtension =
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeRegistrationDetails.phoneExtension;
    representativeRegistrationDetails.representativeRegistrationDetails.registrationDate =
      currentDate;
    representativeRegistrationDetails.representativeRegistrationDetails.sensitivity =
      concernRoleDtls.sensitivity;

    representativeRegistrationDetails.representativeDtls.representativeType =
      curam.codetable.REPRESENTATIVETYPE.CONTACT;

    // Call registerRepresentative
    representativeObj
      .registerRepresentative(representativeRegistrationDetails);

    // Set the key and read the concern role details for the contact
    concernRoleKey.concernRoleID =
      representativeRegistrationDetails.representativeDtls.concernRoleID;
    concernRoleDtls = concernRoleObj.read(concernRoleKey);
    // Task 98009 - DEV: Case Management Code Changes For Preferred Language
    // update preferred language.
    if (!StringUtil.isNullOrEmpty(details.preferredLanguage)) {
      concernRoleDtls.preferredLanguage = details.preferredLanguage;
      ConcernRoleFactory.newInstance().modify(concernRoleKey,
        concernRoleDtls);
    }

    if (concernRoleDtls.primaryPhoneNumberID != 0) {
      final BDMPhoneNumberDtls bdmPhoneNumberDtls = new BDMPhoneNumberDtls();
      bdmPhoneNumberDtls.phoneNumberID = concernRoleDtls.primaryPhoneNumberID;
      bdmPhoneNumberDtls.phoneCountryCode = details.phoneCountryCode;
      bdmPhoneNumberDtls.bdmPhoneNumberID = UniqueID.nextUniqueID();
      BDMPhoneNumberFactory.newInstance().insert(bdmPhoneNumberDtls);
    }

    concernContactDtls.concernRoleID =
      details.dtls.createContactFromUnregisteredParticipant.concernRoleID;
    concernContactDtls.contactConRoleID =
      representativeRegistrationDetails.representativeDtls.concernRoleID;
    concernContactDtls.name = concernRoleDtls.concernRoleName;
    concernContactDtls.contactTypeCode =
      details.dtls.createContactFromUnregisteredParticipant.contactTypeCode;
    concernContactDtls.companyContactTypeCode =
      details.dtls.createContactFromUnregisteredParticipant.companyContactTypeCode;
    if (details.dtls.createContactFromUnregisteredParticipant.companyContactTypeCode != null
      && details.dtls.createContactFromUnregisteredParticipant.companyContactTypeCode
        .length() > 0) {
      concernContactDtls.contactTypeCode =
        details.dtls.createContactFromUnregisteredParticipant.companyContactTypeCode;
    }
    concernContactDtls.startDate =
      details.dtls.createContactFromUnregisteredParticipant.startDate;
    concernContactDtls.endDate =
      details.dtls.createContactFromUnregisteredParticipant.endDate;

    concernContactDtls.comments =
      details.dtls.createContactFromUnregisteredParticipant.comments;

    // Create the Contact.
    maintainContactsObj.createConcernContact(concernContactDtls);

    return concernContactDtls;
  }

  /**
   * method to get phone country code
   * Task 21121 Phone number validations
   *
   * @since
   * @param
   */
  private String getPhoneCountryCodeByCodeTableCode(
    final String codetableCode) throws AppException, InformationalException {

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniqueKey.code = codetableCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

    final CodeTableItemDetails codeTableItemDetails = codeTableAdminObj
      .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey);
    return codeTableItemDetails.annotation.trim();
  }

  /**
   * modify a phone number
   *
   * @param details
   * phone details for the contact
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public void
    modifyPhoneNumber(final BDMMaintainParticipantPhoneDetails details)
      throws AppException, InformationalException {

    // convert the code table code to phone country code
    if (!details.phoneCountryCode.isEmpty()) {
      details.dtls.concernRolePhoneDetails.phoneCountryCode =
        getPhoneCountryCodeByCodeTableCode(details.phoneCountryCode);
    }

    bdmUtil.validatePhoneNumberDetails(
      details.dtls.concernRolePhoneDetails.phoneCountryCode,
      details.dtls.concernRolePhoneDetails.phoneAreaCode,
      details.dtls.concernRolePhoneDetails.phoneNumber,
      details.dtls.concernRolePhoneDetails.phoneExtension);

    final MaintainParticipantPhoneDetails maintainParticipantPhoneDetails =
      new MaintainParticipantPhoneDetails();
    maintainParticipantPhoneDetails.assign(details.dtls);

    // Phone number maintenance key

    final MaintainPhoneNumberKey maintainPhoneNumberKey =
      new MaintainPhoneNumberKey();

    // Get concern role ID from key
    maintainPhoneNumberKey.concernRoleID =
      details.dtls.concernRolePhoneDetails.concernRoleID;

    final BDMMaintainConcernRolePhone bdmMaintainConcernRolePhoneObj =
      BDMMaintainConcernRolePhoneFactory.newInstance();

    bdmMaintainConcernRolePhoneObj.modifyPhoneNumber(maintainPhoneNumberKey,
      details.dtls.concernRolePhoneDetails);

  }

  /**
   * Create a new email address for a participant.
   *
   * @param details
   * The email address details being entered.
   *
   * @return A list of informational messages.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public CreateParticipantEmailAddressDetails
    createEmailAddress(final MaintainParticipantEmailAddressDetails details)
      throws AppException, InformationalException {

    validateEmailDetails(details.concernRoleEmailDetails.emailAddress);
    return ParticipantFactory.newInstance().createEmailAddress(details);
  }

  /**
   * Modify the details of an email address for a participant.
   *
   * @param details
   * The email address details being modified.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public void
    modifyEmailAddress(final MaintainParticipantEmailAddressDetails details)
      throws AppException, InformationalException {

    validateEmailDetails(details.concernRoleEmailDetails.emailAddress);
    ParticipantFactory.newInstance().modifyEmailAddress(details);
  }

  /**
   * Validation method
   * Task 21122 Email formatting validation
   *
   * @since
   * @param
   */
  private void validateEmailDetails(final String emailAddress)
    throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final String emailRegex =
      "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$";

    // validation: Email address must be valid.
    if (!emailAddress.isEmpty()) {
      final String[] emailAddressArray = emailAddress.split("@");
      if (!emailAddress.matches(emailRegex) || emailAddressArray.length == 2
        && (emailAddressArray[0].length() > 64
          || emailAddressArray[1].length() > 253)) {
        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_INVALIE_EMAIL_ADDRESS);
        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }
    }
    informationalManager.failOperation();
  }

  /**
   * Retrieves a list of phone number records for a concern role
   *
   * @param key
   * The concern role id for which a list of phone numbers are
   * returned.
   *
   * @return The list of phone numbers returned from the database.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public ReadParticipantPhoneNumberList
    listPhoneNumber(final ReadParticipantPhoneNumberListKey key)
      throws AppException, InformationalException {

    // // Phone number maintenance object
    final BDMMaintainConcernRolePhone maintainConcernRolePhoneObj =
      BDMMaintainConcernRolePhoneFactory.newInstance();

    // // Details to be returned
    final ReadParticipantPhoneNumberList readParticipantPhoneNumberList =
      new ReadParticipantPhoneNumberList();

    // Read list of phone numbers
    readParticipantPhoneNumberList.readMultiByConcernRoleIDPhoneResult =
      maintainConcernRolePhoneObj
        .readmultiByConcernRole(key.maintainPhoneNumberKey);

    // Return details
    return readParticipantPhoneNumberList;
  }

  /**
   * Reads a phone number record for a concern role
   *
   * @param key The concern role phone number ID.
   *
   * @return The phone number details returned from the database.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */

  @Override
  public ReadParticipantPhoneNumberDetails
    readPhoneNumber(final ReadParticipantPhoneNumberKey key)
      throws AppException, InformationalException {

    // Phone number maintenance object
    final curam.core.intf.MaintainConcernRolePhone maintainConcernRolePhoneObj =
      curam.core.fact.MaintainConcernRolePhoneFactory.newInstance();

    // Details to be returned
    final ReadParticipantPhoneNumberDetails readParticipantPhoneNumberDetails =
      new ReadParticipantPhoneNumberDetails();

    // Read the phone number
    readParticipantPhoneNumberDetails.concernRolePhoneDetails =
      maintainConcernRolePhoneObj
        .readPhoneNumber(key.readConcernRolePhoneKey);

    final ReadBDMPhoneNumberKey readBDMPhoneNumberKey =
      new ReadBDMPhoneNumberKey();
    readBDMPhoneNumberKey.phoneNumberID =
      readParticipantPhoneNumberDetails.concernRolePhoneDetails.phoneNumberID;
    /*
     * BEGIN TASK-25344 Removed Unused Variable keeping it for future reference
     * final curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberDetails dtls =
     * BDMPhoneNumberFactory.newInstance()
     * .readBDMPhoneNumber(readBDMPhoneNumberKey);
     */

    final String phoneCountryCodeDesc =
      getPhoneCountryCodeDesc(readBDMPhoneNumberKey.phoneNumberID);
    readParticipantPhoneNumberDetails.concernRolePhoneDetails.phoneCountryCode =
      phoneCountryCodeDesc;
    // Context key
    final ParticipantContextKey participantContextKey =
      new ParticipantContextKey();

    participantContextKey.participantContextDescriptionKey.concernRoleID =
      readParticipantPhoneNumberDetails.concernRolePhoneDetails.concernRoleID;
    // Get the context description for the concern role
    readParticipantPhoneNumberDetails.participantContextDescriptionDetails =
      ParticipantFactory.newInstance().readContextDescription(
        participantContextKey).participantContextDescriptionDetails;

    // Return details
    return readParticipantPhoneNumberDetails;
  }

  /**
   * Creates a concern role phone number record
   *
   * @param details
   * Concern role identifier & concern role phone number details
   *
   * @return A list of informational messages.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public CreateParticipantPhoneDetails

    createPhoneNumber(final BDMConcernRolePhoneDetails details)
      throws AppException, InformationalException {

    // Phone number maintenance object and key
    final BDMMaintainConcernRolePhone maintainConcernRolePhoneObj =
      BDMMaintainConcernRolePhoneFactory.newInstance();
    final MaintainPhoneNumberKey maintainPhoneNumberKey =
      new MaintainPhoneNumberKey();
    // START - 99317 - Unable to create Representative Contact phone number -JP
    String temp = "";
    // convert the code table code to phone country code
    if (!details.phoneCountryCode.isEmpty()) {
      temp = getPhoneCountryCodeByCodeTableCode(details.phoneCountryCode);
    }

    bdmUtil.validatePhoneNumberDetails(temp, details.phoneAreaCode,
      details.phoneNumber, details.phoneExtension);

    // Details to be returned
    final CreateParticipantPhoneDetails createParticipantPhoneDetails =
      new CreateParticipantPhoneDetails();

    // Get concern role ID from key
    maintainPhoneNumberKey.concernRoleID = details.concernRoleID;

    // Create phone number
    final ConcernRolePhoneDetails concernRolePhDetails =
      new ConcernRolePhoneDetails();

    concernRolePhDetails.assign(details);
    // END - 99317 - Unable to create Representative Contact phone number - JP

    maintainConcernRolePhoneObj.createPhoneNumber(maintainPhoneNumberKey,
      concernRolePhDetails);
    // Return details
    return createParticipantPhoneDetails;
  }

  /**
   * This method returns the phone country code description.
   *
   * @param phoneNumberID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getPhoneCountryCodeDesc(final long phoneNumberID)
    throws AppException, InformationalException {

    final BDMPhoneNumber bdmphoneNumberObj =
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

  // BEGIN Task-25344 Review_Comment_Impl_For_10892_Name_Evidence
  /**
   * Creates a Preferred Name for Person
   *
   * @param dtls
   * Concern role identifier
   *
   * @return SearchForAlternateNameKey Concern role identifier & Preferred Name
   * details
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public SearchForAlternateNameKey
    readPreferredName(final SearchAlternateIDDetailsKey details)
      throws AppException, InformationalException {

    final SearchForAlternateNameKey searchForAlternateNameKey =
      new SearchForAlternateNameKey();
    final AlternateName alternateNameObj = AlternateNameFactory.newInstance();
    final AlternateNameReadByTypeKey alternateNameReadByTypeKey =
      new AlternateNameReadByTypeKey();
    alternateNameReadByTypeKey.concernRoleID = details.concernRoleID;
    alternateNameReadByTypeKey.nameStatus = RECORDSTATUS.NORMAL;
    alternateNameReadByTypeKey.nameType = ALTERNATENAMETYPE.PREFERRED;
    final AlternateNameDtlsList alternateNameDtlsList =
      alternateNameObj.searchByType(alternateNameReadByTypeKey);

    if (!alternateNameDtlsList.dtls.isEmpty()) {
      final AlternateNameDtls prefAlternateNameDtls =
        alternateNameDtlsList.dtls.get(0);
      searchForAlternateNameKey.firstForename =
        prefAlternateNameDtls.firstForename;
      searchForAlternateNameKey.otherForename =
        prefAlternateNameDtls.otherForename;
      searchForAlternateNameKey.surname = prefAlternateNameDtls.surname;

    }
    searchForAlternateNameKey.concernRoleID = details.concernRoleID;
    return searchForAlternateNameKey;
  }

  /**
   * Creates a Preferred Name for Person
   *
   * @param dtls
   * Concern role identifier & Preferred Name details
   *
   * @return
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public void createPreferredName(final SearchForAlternateNameKey dtls)
    throws AppException, InformationalException {

    // Read Person Details
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = dtls.concernRoleID;
    final curam.core.intf.Person personObj = PersonFactory.newInstance();

    final PersonDtls personDtls = personObj.read(personKey, true);

    final AlternateName alternateNameObj = AlternateNameFactory.newInstance();
    final AlternateNameKey alternateNameKey = new AlternateNameKey();
    alternateNameKey.alternateNameID = personDtls.primaryAlternateNameID;

    final ParticipantAlternateNameDetails nameDetails =
      new ParticipantAlternateNameDetails();

    // Check if an preferred name already exists
    final AlternateNameReadByTypeKey alternateNameReadByTypeKey =
      new AlternateNameReadByTypeKey();
    alternateNameReadByTypeKey.concernRoleID = dtls.concernRoleID;
    alternateNameReadByTypeKey.nameStatus = RECORDSTATUS.NORMAL;
    alternateNameReadByTypeKey.nameType = ALTERNATENAMETYPE.PREFERRED;
    final AlternateNameDtlsList alternateNameDtlsList =
      alternateNameObj.searchByType(alternateNameReadByTypeKey);

    final PDCAlternateName pdcAlternateNameObj =
      PDCAlternateNameFactory.newInstance();
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    if (!alternateNameDtlsList.dtls.isEmpty()) {
      final AlternateNameDtls prefAlternateNameDtls =
        alternateNameDtlsList.dtls.get(0);
      nameDetails.assign(prefAlternateNameDtls);
      // Make first name the Preferred Name
      nameDetails.firstForename = dtls.firstForename;
      nameDetails.otherForename = dtls.otherForename;
      nameDetails.surname = dtls.surname;
      try {
        pdcAlternateNameObj.modify(nameDetails);
      } catch (final Exception e) {
        e.printStackTrace();
      }
    } else {
      // Gathering Name information about current Name to fill in similar
      // fields
      final AlternateNameDtls alternateNameDtls =
        alternateNameObj.read(notFoundIndicator, alternateNameKey);
      if (!notFoundIndicator.isNotFound()) {
        nameDetails.assign(alternateNameDtls);
        // Make first name the Preferred Name
        nameDetails.firstForename = dtls.firstForename;
        nameDetails.otherForename = dtls.otherForename;
        nameDetails.surname = dtls.surname;
        // Make Name Type Preferred Name
        nameDetails.nameType = ALTERNATENAMETYPE.PREFERRED;
        // call OOTB Modify method to insert person name evidence
        try {
          pdcAlternateNameObj.insert(nameDetails);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    }

  }
  // END TASK-25344

  @Override
  public BDMTasksForConcernAndCaseDetails
    listParticipantTask(final ListParticipantTaskKey_eo key)
      throws AppException, InformationalException {

    final BDMTasksForConcernAndCaseDetails bdmTasksForConcernAndCaseDetails =
      new BDMTasksForConcernAndCaseDetails();

    TasksForConcernAndCaseDetails tasksForConcernAndCaseDetails =
      new TasksForConcernAndCaseDetails();

    final Participant participant = ParticipantFactory.newInstance();

    tasksForConcernAndCaseDetails = participant.listParticipantTask(key);

    bdmTasksForConcernAndCaseDetails.assign(tasksForConcernAndCaseDetails);
    bdmTasksForConcernAndCaseDetails.dtls
      .assign(tasksForConcernAndCaseDetails.detailsList.dtls);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMTaskSearchDetails bdmTaskSearchDetails = null;
    BDMEscalationLevelString bdmEscalationLevel = null;

    for (int i = 0; i < bdmTasksForConcernAndCaseDetails.dtls.dtls
      .size(); i++) {

      bdmTaskSearchDetails = new BDMTaskSearchDetails();

      bdmTaskSearchDetails =
        bdmTasksForConcernAndCaseDetails.dtls.dtls.get(i);

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

    return bdmTasksForConcernAndCaseDetails;
  }

  @Override
  public BDMListConcernContactDetails
    listCaseConcernContactsForCommunication(final CaseHeaderKey caseHeaderKey)
      throws AppException, InformationalException {

    final CaseHeaderDtls caseHeaderDetails =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);

    final ListContactKey contactKey = new ListContactKey();
    contactKey.contactRMByConcernKey.concernRoleID =
      caseHeaderDetails.concernRoleID;
    final BDMListConcernContactDetails bdmListConcernContactDetails =
      new BDMListConcernContactDetails();
    bdmListConcernContactDetails.concernRoleID =
      contactKey.contactRMByConcernKey.concernRoleID;
    final ListConcernContactDetails concernContactDetails = ParticipantFactory
      .newInstance().listConcernContactsForCommunication(contactKey);
    bdmListConcernContactDetails.dtls = concernContactDetails;
    return bdmListConcernContactDetails;
  }

  @Override
  public CuramInd
    isParticipantDuplicateOrRdRv(final ConcernRoleKey concernRoleKey)
      throws AppException, InformationalException {

    final boolean isRdRv = BDMUtil.isReadReviewUser();
    final CuramInd ind = new CuramInd();

    ind.statusInd = false;

    final Participant participantObj = ParticipantFactory.newInstance();

    final CuramInd isDuplicate =
      participantObj.isParticipantDuplicate(concernRoleKey);

    if (isRdRv || isDuplicate.statusInd) {
      ind.statusInd = true;
    }

    return ind;
  }

  /**
   * Task 98009 - DEV: Case Management Code Changes For Preferred Language
   */
  @Override
  public BDMDate displayDefaultEndDate()
    throws AppException, InformationalException {

    final BDMDate defaultEndDate = new BDMDate();
    defaultEndDate.date = Date.getCurrentDate()
      .addDays(BDMConstants.kUnverifiedContactDefaultEndDate);
    return defaultEndDate;
  }

  @Override
  public ReadParticipantAddressList
    listAddress(final ReadParticipantAddressListKey key)
      throws AppException, InformationalException {

    // Address maintenance object

    /// START Bug 110992: Address record not reflecting in the address tab for
    /// the third party individual contact
    // CAll OOTB method to populate address
    final curam.core.intf.MaintainConcernRoleAddress maintainConcernRoleAddressObj =
      curam.core.fact.MaintainConcernRoleAddressFactory.newInstance();

    // Details to be returned
    final ReadParticipantAddressList readParticipantAddressList =
      new ReadParticipantAddressList();

    // Read list of addresses

    readParticipantAddressList.readMultiByConcRoleIDResult =
      maintainConcernRoleAddressObj
        .readmultiByConcernRoleID(key.maintainAddressKey);

    /*
     * final curam.ca.gc.bdm.facade.address.intf.BDMAddress bdmAddress =
     * BDMAddressFactory.newInstance();
     *
     * readParticipantAddressList.readMultiByConcRoleIDResult =
     * bdmAddress.readmultiByConcernRoleID(key.maintainAddressKey);
     */

    // Context key
    final ParticipantContextKey participantContextKey =
      new ParticipantContextKey();

    participantContextKey.participantContextDescriptionKey.concernRoleID =
      key.maintainAddressKey.concernRoleID;

    final Participant participant = ParticipantFactory.newInstance();

    // Get the context description for the concern role
    readParticipantAddressList.participantContextDescriptionDetails =
      participant.readContextDescription(
        participantContextKey).participantContextDescriptionDetails;

    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warnings[i];
      readParticipantAddressList.informationalMsgDtlsList.dtls
        .addRef(informationalMsgDtls);

    }

    // Return details
    return readParticipantAddressList;
  }

  @Override
  public CreateParticipantAddressDetails
    createAddress(final MaintainParticipantAddressDetails details)
      throws AppException, InformationalException {

    // START TASK 118752 : Set the BDMUNPARSE to 1 for the INTL Address
    details.addressDetails.addressData = bdmUtil
      .setBDMUnparsedIndForINTLAddress(details.addressDetails.addressData);
    return ParticipantFactory.newInstance().createAddress(details);
    // END TASK 118752
  }

  @Override
  public ModifiedAddressDetails
    modifyAddress(final MaintainParticipantAddressDetails details)
      throws AppException, InformationalException {

    // START TASK 118752 : Set the BDMUNPARSE to 1 for the INTL Address
    details.addressDetails.addressData = bdmUtil
      .setBDMUnparsedIndForINTLAddress(details.addressDetails.addressData);
    return ParticipantFactory.newInstance().modifyAddress(details);
    // END TASK 118752
  }

}
