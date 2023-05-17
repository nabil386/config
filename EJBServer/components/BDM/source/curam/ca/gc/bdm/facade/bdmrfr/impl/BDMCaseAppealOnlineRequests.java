package curam.ca.gc.bdm.facade.bdmrfr.impl;

import com.google.inject.Inject;
import curam.appeal.sl.entity.fact.AppealOnlineRequestLinkFactory;
import curam.appeal.sl.entity.intf.AppealOnlineRequestLink;
import curam.appeal.sl.entity.struct.AppealOnlineRequestIDKey;
import curam.appeal.sl.entity.struct.AppealOnlineRequestLinkDtls;
import curam.appeal.sl.entity.struct.AppealOnlineRequestLinkDtlsList;
import curam.appeal.sl.entity.struct.OnlineRequestAppealCaseIDKey;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestFactory;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestKey;
import curam.ca.gc.bdm.facade.appeals.fact.BDMAppealRequestFactory;
import curam.ca.gc.bdm.facade.appeals.intf.BDMAppealRequest;
import curam.ca.gc.bdm.facade.appeals.struct.BDMAppealRequestsForConcernRoleDetailsList;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMOnlineAppealRequestDetails;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMOnlineAppealRequestsList;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.onlineappealrequest.impl.OnlineAppealRequest;
import curam.core.onlineappealrequest.impl.OnlineAppealRequestDAO;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseParticipantRole;
import curam.core.sl.entity.struct.CaseParticipantRoleCaseAndTypeKey;
import curam.core.sl.entity.struct.CaseParticipantRoleIDDetails;
import curam.core.sl.entity.struct.CaseParticipantRoleIDDetailsList;
import curam.core.struct.RelationshipConcernRoleIDKey;
import curam.datastore.impl.NoSuchSchemaException;
import curam.message.APPEALOBJECT;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.NotFoundIndicator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 */
public class BDMCaseAppealOnlineRequests
  extends curam.ca.gc.bdm.facade.bdmrfr.base.BDMCaseAppealOnlineRequests {

  @Inject
  private PersonDAO personDAO;

  @Inject
  private OnlineAppealRequestDAO onlineAppealRequestDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  private final AppealOnlineRequestLink appealOnlineRequestLink =
    AppealOnlineRequestLinkFactory.newInstance();

  CaseParticipantRole caseParticipantRoleObj =
    CaseParticipantRoleFactory.newInstance();

  public BDMCaseAppealOnlineRequests() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public BDMOnlineAppealRequestsList getOnlineAppealRequestsForExistingAppeal(
    final OnlineRequestAppealCaseIDKey appealCaseID)
    throws AppException, InformationalException {

    final BDMOnlineAppealRequestsList returnList =
      new BDMOnlineAppealRequestsList();

    final List<OnlineAppealRequest> allAppealRequestsForAppellants =
      new ArrayList<>();

    final List<OnlineAppealRequest> caseLinkedAppealRequests =
      getAppealRequestsForCase(appealCaseID);

    final CaseParticipantRoleCaseAndTypeKey caseParticipantRoleCaseAndTypeKey =
      new CaseParticipantRoleCaseAndTypeKey();

    caseParticipantRoleCaseAndTypeKey.caseID = appealCaseID.appealCaseID;
    caseParticipantRoleCaseAndTypeKey.typeCode =
      CASEPARTICIPANTROLETYPE.APPELLANT;

    final CaseParticipantRoleIDDetailsList caseParticipantRoleIDDetailsList =
      this.caseParticipantRoleObj
        .searchActiveRoleByCaseAndType(caseParticipantRoleCaseAndTypeKey);

    if (caseParticipantRoleIDDetailsList != null
      && caseParticipantRoleIDDetailsList.dtls != null) {
      for (int i = 0; i < caseParticipantRoleIDDetailsList.dtls.size(); i++) {

        final long appellantID =
          caseParticipantRoleIDDetailsList.dtls.item(i).participantRoleID;

        final Person appellant =
          this.personDAO.get(Long.valueOf(appellantID));

        final List<OnlineAppealRequest> requestsForAppellant =
          this.onlineAppealRequestDAO.searchByPrimaryAppellant(appellant);

        final AppealOnlineRequestLink onlineRequestLink =
          AppealOnlineRequestLinkFactory.newInstance();

        final List<OnlineAppealRequest> filteredRequestList =
          new ArrayList<>();

        final NotFoundIndicator recordNotFound = new NotFoundIndicator();
        final AppealOnlineRequestIDKey appealOnlineRequestIDKey =
          new AppealOnlineRequestIDKey();

        for (final OnlineAppealRequest onlineAppealRequest : requestsForAppellant) {
          appealOnlineRequestIDKey.onlineAppealRequestID =
            onlineAppealRequest.getID().longValue();
          onlineRequestLink.readAppealCaseIDForRequest(recordNotFound,
            appealOnlineRequestIDKey);

          if (recordNotFound.isNotFound()) {
            filteredRequestList.add(onlineAppealRequest);
          }
        }

        allAppealRequestsForAppellants.addAll(filteredRequestList);
      }
    }

    allAppealRequestsForAppellants.removeAll(caseLinkedAppealRequests);

    returnList.dtls
      .addAll(getRequestsDetailsList(allAppealRequestsForAppellants));

    return returnList;
  }

  public List<OnlineAppealRequest>
    getAppealRequestsForCase(final OnlineRequestAppealCaseIDKey caseIDKey)
      throws AppException, InformationalException {

    final List<OnlineAppealRequest> appealRequests = new ArrayList<>();

    final AppealOnlineRequestLinkDtlsList linksList =
      this.appealOnlineRequestLink.searchByAppealCaseID(caseIDKey);

    final Iterator<AppealOnlineRequestLinkDtls> linksIterator =
      linksList.dtls.iterator();

    while (linksIterator.hasNext()) {
      final AppealOnlineRequestLinkDtls link = linksIterator.next();

      final AppealOnlineRequestIDKey requestKey =
        new AppealOnlineRequestIDKey();

      requestKey.onlineAppealRequestID = link.onlineAppealRequestID;

      appealRequests.add(this.onlineAppealRequestDAO
        .get(Long.valueOf(link.onlineAppealRequestID)));
    }
    return appealRequests;
  }

  /**
   * Returns the list of online appeal requests details.
   *
   * @param onlineAppealRequests
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private List<BDMOnlineAppealRequestDetails> getRequestsDetailsList(
    final List<OnlineAppealRequest> onlineAppealRequests)
    throws AppException, InformationalException {

    final List<BDMOnlineAppealRequestDetails> appealRequestDetailsList =
      new ArrayList<>();

    for (final OnlineAppealRequest request : onlineAppealRequests) {

      final BDMOnlineAppealRequestDetails details =
        getRequestDetails(request);

      appealRequestDetailsList.add(details);
    }
    return appealRequestDetailsList;
  }

  /**
   * This method populates and returns the online appeal request details from
   * the object.
   *
   * @param onlineAppealRequest online appeal request
   * @return online appeal request details
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public BDMOnlineAppealRequestDetails
    getRequestDetails(final OnlineAppealRequest onlineAppealRequest)
      throws AppException, InformationalException {

    final BDMOnlineAppealRequestDetails onlineRequestDtls =
      new BDMOnlineAppealRequestDetails();

    onlineRequestDtls.requestID = onlineAppealRequest.getID().longValue();

    onlineRequestDtls.primaryAppellant =
      onlineAppealRequest.getPrimaryAppellant().getName();

    try {
      onlineRequestDtls.requestURI =
        onlineAppealRequest.getDocumentURI("CaseOnlineAppealRequests_list");
    } catch (final NoSuchSchemaException e) {

      onlineRequestDtls.requestURI = "";
    }

    onlineRequestDtls.submittedDateTime =
      onlineAppealRequest.getSubmittedDateTime();

    onlineRequestDtls.title =
      APPEALOBJECT.INF_APPEAL_ONLINEREQUEST_PREFIX.getMessageText()
        + onlineRequestDtls.submittedDateTime;

    // get reference number for the request
    final BDMOnlineAppealRequestKey key = new BDMOnlineAppealRequestKey();
    key.onlineAppealRequestID = onlineAppealRequest.getID();

    final BDMOnlineAppealRequestDtls bdmOnlineAppealRequestDetails =
      BDMOnlineAppealRequestFactory.newInstance().read(key);
    onlineRequestDtls.reference =
      bdmOnlineAppealRequestDetails.onlineAppealRequestReference;

    // get program name
    onlineRequestDtls.program = bdmOnlineAppealRequestDetails.programCode;

    return onlineRequestDtls;
  }

  /**
   * This method returns the list of appeal requests.
   *
   * @param appealCaseIDKey OnlineRequestAppealCaseIDKey
   * @return online appeal request details
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   * @since Task-57493
   */
  @Override
  public BDMAppealRequestsForConcernRoleDetailsList
    getOnlineAppealRequestsDetailsForCase(
      final OnlineRequestAppealCaseIDKey appealCaseIDKey)
      throws AppException, InformationalException {

    final BDMAppealRequestsForConcernRoleDetailsList returnList =
      new BDMAppealRequestsForConcernRoleDetailsList();
    final CaseParticipantRoleCaseAndTypeKey caseParticipantRoleCaseAndTypeKey =
      new CaseParticipantRoleCaseAndTypeKey();

    caseParticipantRoleCaseAndTypeKey.caseID = appealCaseIDKey.appealCaseID;
    caseParticipantRoleCaseAndTypeKey.typeCode =
      CASEPARTICIPANTROLETYPE.APPELLANT;

    final CaseParticipantRoleIDDetailsList caseParticipantRoleIDDetailsList =
      this.caseParticipantRoleObj
        .searchActiveRoleByCaseAndType(caseParticipantRoleCaseAndTypeKey);
    final BDMAppealRequest appealRequest =
      BDMAppealRequestFactory.newInstance();
    for (final CaseParticipantRoleIDDetails caseParticipantRoleDtls : caseParticipantRoleIDDetailsList.dtls) {
      final RelationshipConcernRoleIDKey listKey =
        new RelationshipConcernRoleIDKey();

      listKey.concernRoleID = caseParticipantRoleDtls.participantRoleID;

      returnList.dtls
        .addAll(appealRequest.listAppealRequestsForConcernRole(listKey).dtls);
    }

    return returnList;
  }

}
