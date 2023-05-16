/**
 *
 */
package curam.ca.gc.bdmoas.sl.organization.impl;

import com.google.inject.Inject;
import com.ibm.icu.util.Calendar;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.fact.BDMWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMWorkqueueProvinceLinkFactory;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMWorkqueueProvinceLinkKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMAddressEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.ca.gc.bdm.sl.application.struct.CaseCheckResult;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.entity.caseuserrole.fact.BDMOASCaseUserRoleFactory;
import curam.ca.gc.bdmoas.entity.caseuserrole.intf.BDMOASCaseUserRole;
import curam.ca.gc.bdmoas.entity.caseuserrole.struct.BDMOASCaseUserRoleDtls;
import curam.ca.gc.bdmoas.entity.caseuserrole.struct.BDMOASCaseUserRoleKey;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASResidencePeriodEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASResidencePeriodEvidenceVO;
import curam.ca.gc.bdmoas.sl.organization.fact.BDMOASManageOfficeOfControlFactory;
import curam.ca.gc.bdmoas.sl.organization.impl.BDMOASManageOfficeOfControl.PostCodeToWorkQueue;
import curam.ca.gc.bdmoas.sl.organization.struct.BDMOASPostCodeCountryKey;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.COUNTRYCODE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.ORGOBJECTTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.fact.AddressFactory;
import curam.core.fact.CachedCaseHeaderFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.Address;
import curam.core.intf.CachedCaseHeader;
import curam.core.intf.CaseHeader;
import curam.core.sl.entity.struct.CaseIDStruct;
import curam.core.sl.entity.struct.CaseIDStructList;
import curam.core.sl.entity.struct.CaseUserRoleDtls;
import curam.core.sl.entity.struct.OrgObjectLinkDtls;
import curam.core.sl.entity.struct.OrgObjectLinkKey;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDParticipantIDStatusCode;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseOwnerDetails;
import curam.core.sl.struct.ReadWorkQueueDetails;
import curam.core.sl.struct.ReadWorkQueueKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ReadParticipantRoleIDDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataIDAndAttrKey;
import curam.dynamicevidence.sl.entity.struct.EvidenceIDDetails;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseUserRole;
import curam.piwrapper.casemanager.impl.CaseUserRoleDAO;
import curam.piwrapper.organization.impl.OrgObjectLink;
import curam.piwrapper.organization.impl.OrgObjectLinkDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.util.type.DateRange;
import curam.util.type.NotFoundIndicator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.h2.util.StringUtils;

/**
 * ADO-52647: Maps case to appropriate Office of Control Work Queue.
 * 05/01/2023 TODO Notes for pending items to work on, once changes to
 * existing design are finalized:
 * i) Mapping office of control at new Person evidence level
 * instead of case by case. Work queues to be created.
 * ii) Locking manual change to be redesigned/re-implemented at evidence level
 * instead of case user role.
 * iii) Batch (?) for future dated address change to be finalized and
 * implemented.
 * iv) Task to be created to transfer client's case files from previous office
 * of control to the current.
 * v) Evidence hook points to invoke the office of control WQ assignment on
 * address or language preference change.
 *
 * @author abid.a.khan
 *
 *
 */
public class BDMOASCaseOwnershipStrategy extends
  curam.ca.gc.bdmoas.sl.organization.base.BDMOASCaseOwnershipStrategy {

  private static final String kErrorMsgNullAddress =
    "CA Residence Address Evidence object cannot be null.";

  private static final int RESIDENCY_LIMIT_TEN = 10;

  private static final String ADDRESS = "address";

  private static final String TO_DATE = "toDate";

  private static final String FROM_DATE = "fromDate";

  private static final int RESIDENCY_LIMIT_TWENTY = 20;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  CaseUserRoleDAO caseUserRoleDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  @Inject
  OrgObjectLinkDAO orgObjectLinkDAO;

  public BDMOASCaseOwnershipStrategy() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void assignCase(final CaseIDKey chKey)
    throws AppException, InformationalException {

    if (!isUserRoleManualChangeActivatedForCase(chKey)) {

      final OrgObjectLinkDtls orgObjectLinkDtls = new OrgObjectLinkDtls();
      final CaseUserRoleDtls caseUserRoleDtls = new CaseUserRoleDtls();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = chKey.caseID;

      final ReadWorkQueueKey readWorkQueueKey =
        getOfficeOfControlWorkQueueForCase(caseHeaderKey);

      final curam.core.sl.intf.WorkQueue workqueue =
        curam.core.sl.fact.WorkQueueFactory.newInstance();

      final ReadWorkQueueDetails readWorkQueueDetails =
        workqueue.read(readWorkQueueKey);

      final curam.core.sl.intf.CaseUserRole caseUserRoleObj =
        curam.core.sl.fact.CaseUserRoleFactory.newInstance();

      orgObjectLinkDtls.userName = readWorkQueueDetails.dtls.name;
      orgObjectLinkDtls.orgObjectType = ORGOBJECTTYPE.WORKQUEUE;
      orgObjectLinkDtls.orgObjectReference = readWorkQueueKey.key.workQueueID;
      final CaseOwnerDetails caseOwnerDetails = new CaseOwnerDetails();
      caseOwnerDetails.orgObjectReference = readWorkQueueKey.key.workQueueID;
      caseOwnerDetails.orgObjectType = ORGOBJECTTYPE.WORKQUEUE;

      // Record case owner changes if not already present.
      // TODO The case ownership/assignment may change depending on design
      // revision in favor of person level evidence.
      //
      if (!isActiveOwnerOrgObjectLinkPresentForCase(chKey,
        readWorkQueueKey)) {

        final OrgObjectLinkKey orgObjectLinkKey =
          caseUserRoleObj.createOrgObjectLink(orgObjectLinkDtls);

        caseUserRoleDtls.caseID = chKey.caseID;
        caseUserRoleDtls.orgObjectLinkID = orgObjectLinkKey.orgObjectLinkID;

        caseUserRoleObj.createOwnerCaseUserRole(caseUserRoleDtls);

        final CachedCaseHeader cachedCaseHeader =
          CachedCaseHeaderFactory.newInstance();

        final CaseHeaderDtls caseHeaderDtls =
          cachedCaseHeader.read(caseHeaderKey);

        // Update the CaseHeader entity with the initial owner of the case.
        orgObjectLinkDtls.orgObjectLinkID = orgObjectLinkKey.orgObjectLinkID;
        caseHeaderDtls.ownerOrgObjectLinkID =
          orgObjectLinkDtls.orgObjectLinkID;

        cachedCaseHeader.modify(caseHeaderKey, caseHeaderDtls);

      }
    }

  }

  private ReadWorkQueueKey
    getOfficeOfControlWorkQueueForCase(final CaseHeaderKey caseHeaderKey)
      throws AppException, InformationalException {

    ReadWorkQueueKey readWorkQueueKey = new ReadWorkQueueKey();

    final BDMOASPostCodeCountryKey postCodeCountryKey =
      new BDMOASPostCodeCountryKey();

    final curam.core.intf.CaseHeader caseHeaderObj =
      CaseHeaderFactory.newInstance();

    final ReadParticipantRoleIDDetails participantRoleIDDetails =
      caseHeaderObj.readParticipantRoleID(caseHeaderKey);

    final Map<DateRange, BDMAddressEvidenceVO> dateRangeAddressMap =
      getResidentialAddressEvidenceMapByConcernRoleID(
        participantRoleIDDetails.concernRoleID);

    final Map<DateRange, String> dateRangeCountryMap = BDMOASEvidenceUtil
      .getResidentialAddressDateRangeCountryCodeMapByConcernRoleID(
        participantRoleIDDetails.concernRoleID);

    final List<BDMOASResidencePeriodEvidenceVO> bdmoasResPeriod =
      new BDMOASResidencePeriodEvidence()
        .getResidencePeriodEvidenceValueObject(
          participantRoleIDDetails.concernRoleID);

    // TODO: Residence Period aggregation calculations to be derived
    // from Rules objects for consistency.
    final int canadianResidencePeriod = BDMOASOfficeOfControlHelper
      .getResidencePeriodByCountry(bdmoasResPeriod, COUNTRY.CA);

    final BDMContactPreferenceEvidenceVO clientPreferredLanguage =
      getClientPreferredLanguage(participantRoleIDDetails.concernRoleID);

    // 5-a if the Residential Address is Outside Canada
    // and no previous Canadian Residential Address has been
    // indicated
    if (!dateRangeCountryMap.isEmpty() && !BDMOASOfficeOfControlHelper
      .containsValue(dateRangeCountryMap, COUNTRY.CA)) {
      // Assign the Office of Control to the Scarborough Regional
      // Office
      readWorkQueueKey.key.workQueueID = PostCodeToWorkQueue.L.getValue();

      return readWorkQueueKey;
    }

    final DateRange mostRecentDateRangeAddressKey =
      getMostRecentActiveDateRangeAddressKeyByAddressMap(dateRangeAddressMap);

    /**
     * 5) If current Address not Canada and a previous
     * CA address exists
     */
    if (!dateRangeCountryMap.isEmpty()
      && Objects.nonNull(mostRecentDateRangeAddressKey)
      && !StringUtils
        .isNullOrEmpty(dateRangeCountryMap.get(mostRecentDateRangeAddressKey))
      && !dateRangeCountryMap.get(mostRecentDateRangeAddressKey)
        .equals(COUNTRY.CA)) {

      // Find latest CA address (given the most recent residence is not CA).
      final Map.Entry<DateRange, BDMAddressEvidenceVO> mostRecentCanadianAddressEntry =
        getLatestAddressMapEntryByCountry(dateRangeAddressMap, COUNTRY.CA);

      if (Objects.nonNull(mostRecentCanadianAddressEntry)
        && !mostRecentDateRangeAddressKey
          .equals(mostRecentCanadianAddressEntry.getKey())) {

        /**
         * Previous CA address exists:
         * Assign Office of Control to the Regional Office Code that
         * corresponds to the Postal Code of Client’s latest Canadian Address
         */
        postCodeCountryKey.countryCode =
          mostRecentCanadianAddressEntry.getValue().getCountryCode();
        postCodeCountryKey.postCode =
          mostRecentCanadianAddressEntry.getValue().getPostalCode();

        if (canadianResidencePeriod < RESIDENCY_LIMIT_TWENTY) {
          // Get list of residency periods with SSA countries
          // TODO SSA Country check - to be confirmed if BDMExternalParty serves
          // as a source for SSA country?
          final Map<String, Integer> ssaCountryToResidencyPeriodMap =
            BDMOASOfficeOfControlHelper
              .getSSACountryPeriodMapFromResidencyList(bdmoasResPeriod);

          if (!ssaCountryToResidencyPeriodMap.isEmpty()) {

            readWorkQueueKey =
              getWorkQueueBySSAResidence(ssaCountryToResidencyPeriodMap);

            if (readWorkQueueKey.key.workQueueID != 0)

              return readWorkQueueKey;

          } // No SSA country residency Period.
        }

        // Client had a previous CA address on file?
        return getWorkQueueForValidCAResidenceAddress(
          mostRecentCanadianAddressEntry.getValue(), clientPreferredLanguage);
      }

      // Foreign Address, Prev CA address not on file:
      // Online/Paper application? -- feature enhancement pending.
      // TODO Enhancement feature shall determine how Online or Paper
      // application needs to be implemented here.

    } // 5)

    if (Objects.nonNull(mostRecentDateRangeAddressKey)
      && mostRecentDateRangeAddressKey.isStarted()
      && !mostRecentDateRangeAddressKey.isEnded()) {

      final BDMAddressEvidenceVO latestAddressEvidence =
        dateRangeAddressMap.get(mostRecentDateRangeAddressKey);

      postCodeCountryKey.countryCode = latestAddressEvidence.getCountryCode();

      postCodeCountryKey.postCode = latestAddressEvidence.getPostalCode();

      // CA Address Present on account
      if (postCodeCountryKey.countryCode.equals(COUNTRYCODE.CACODE)) {// CA
                                                                      // address

        if (canadianResidencePeriod < RESIDENCY_LIMIT_TEN) {

          // Get list of residency periods with SSA countries
          final Map<String, Integer> ssaCountryToResidencyPeriodMap =
            BDMOASOfficeOfControlHelper
              .getSSACountryPeriodMapFromResidencyList(bdmoasResPeriod);

          if (!ssaCountryToResidencyPeriodMap.isEmpty()) {

            readWorkQueueKey =
              getWorkQueueBySSAResidence(ssaCountryToResidencyPeriodMap);

            if (readWorkQueueKey.key.workQueueID != 0)
              return readWorkQueueKey;

          } // No SSA country residency Period.

        }

        // CA Residence NOT < 10 || No SSA country residency Period available

        if (!BDMOASOfficeOfControlHelper
          .isValidPostalCode(postCodeCountryKey.postCode)) {// Invalid
                                                            // Postal Code
          return getWorkQueueForInvalidCAResidenceAddress(
            latestAddressEvidence, clientPreferredLanguage);
        }

        /**
         * a. Alternatively, If the Residential Address is in Ontario,
         * Canada and Client’s Preferred Language is French,
         * then by default, the system assigns the Office of Control to
         * the Timmins Regional Office
         */

        return getWorkQueueForValidCAResidenceAddress(latestAddressEvidence,
          clientPreferredLanguage);

      } // CA residence
    } // isStarted

    return readWorkQueueKey;
  }

  private ReadWorkQueueKey getWorkQueueForValidCAResidenceAddress(
    final BDMAddressEvidenceVO addressEvidence,
    final BDMContactPreferenceEvidenceVO clientPreferredLanguage)
    throws AppException, InformationalException {

    final ReadWorkQueueKey readWorkQueueKey = new ReadWorkQueueKey();

    if (Objects.isNull(addressEvidence))
      throw new IllegalStateException(kErrorMsgNullAddress);

    final BDMOASPostCodeCountryKey postCodeCountryKey =
      new BDMOASPostCodeCountryKey();
    postCodeCountryKey.countryCode = addressEvidence.getCountryCode();
    postCodeCountryKey.postCode = addressEvidence.getProvince();

    if (postCodeCountryKey.postCode.equals(PROVINCETYPE.ONTARIO)
      && clientPreferredLanguage.getPreferredOralLanguage()
        .equals(BDMLANGUAGE.FRENCHL)
      || clientPreferredLanguage.getPreferredWrittenLanguage()
        .equals(BDMLANGUAGE.FRENCHL)) {

      readWorkQueueKey.key.workQueueID = PostCodeToWorkQueue.P.getValue();

      return readWorkQueueKey;

    }

    readWorkQueueKey.key.workQueueID =
      BDMOASManageOfficeOfControlFactory.newInstance()
        .getWorkQueueByPostCodeOrCountry(postCodeCountryKey).key.workQueueID;

    return readWorkQueueKey;
  }

  private ReadWorkQueueKey getWorkQueueBySSAResidence(

    final Map<String, Integer> ssaCountryToResidencyPeriodMap)
    throws AppException, InformationalException {

    final ReadWorkQueueKey readWorkQueueKey = new ReadWorkQueueKey();
    final Entry<String, Integer> entryWithLongestResidencyPeriodOrFirstIfPeriodsMatch =
      BDMOASOfficeOfControlHelper
        .getEntryHavingMaxValue(ssaCountryToResidencyPeriodMap);

    if (Objects.nonNull(entryWithLongestResidencyPeriodOrFirstIfPeriodsMatch)
      && entryWithLongestResidencyPeriodOrFirstIfPeriodsMatch
        .getValue() > 0) {
      final BDMWorkQueueCountryLinkKey bdmWorkQCountryKey =
        new BDMWorkQueueCountryLinkKey();
      bdmWorkQCountryKey.countryCode =
        entryWithLongestResidencyPeriodOrFirstIfPeriodsMatch.getKey();

      /**
       * 4b) covers all 3 scenarios for SSA residency
       */
      readWorkQueueKey.key.workQueueID = BDMWorkQueueCountryLinkFactory
        .newInstance().read(bdmWorkQCountryKey).workQueueID;

      return readWorkQueueKey;

    }

    return readWorkQueueKey;
  }

  private ReadWorkQueueKey getWorkQueueForInvalidCAResidenceAddress(
    final BDMAddressEvidenceVO latestAddressEvidence,
    final BDMContactPreferenceEvidenceVO clientPreferredLanguage)
    throws AppException, InformationalException {

    final ReadWorkQueueKey readWorkQueueKey = new ReadWorkQueueKey();
    /**
     * a. If the Province is not Ontario,
     * map Office of Control in accordance with R1-Work Queue
     * Configuration
     */
    if (!latestAddressEvidence.getProvince().equals(PROVINCETYPE.ONTARIO)) {// Not
                                                                            // in
                                                                            // ON
      final BDMWorkqueueProvinceLinkKey bdmWorkqueueProvinceLinkKey =
        new BDMWorkqueueProvinceLinkKey();
      bdmWorkqueueProvinceLinkKey.provinceTypeCd =
        latestAddressEvidence.getProvince();

      readWorkQueueKey.key.workQueueID = BDMWorkqueueProvinceLinkFactory
        .newInstance().read(bdmWorkqueueProvinceLinkKey).workQueueID;

      return readWorkQueueKey;

    } else { // In ON and Invalid Postal Code

      // b. If Province is Ontario :

      if (clientPreferredLanguage.getPreferredOralLanguage()
        .equals(BDMLANGUAGE.FRENCHL)
        || clientPreferredLanguage.getPreferredWrittenLanguage()
          .equals(BDMLANGUAGE.FRENCHL)) {
        /**
         * If written and/or oral language preference is French, then
         * Office
         * of Control will be set to Timmins Regional Office by default
         */
        readWorkQueueKey.key.workQueueID = PostCodeToWorkQueue.P.getValue();

        return readWorkQueueKey;

      } else if (clientPreferredLanguage.getPreferredOralLanguage()
        .equals(BDMLANGUAGE.ENGLISHL)
        && clientPreferredLanguage.getPreferredWrittenLanguage()
          .equals(BDMLANGUAGE.ENGLISHL)) {

        /**
         * If written and oral language preference is English, then
         * Office
         * of Control will be set to Scarborough Regional Office by
         * default
         */
        readWorkQueueKey.key.workQueueID = PostCodeToWorkQueue.L.getValue();

        return readWorkQueueKey;
      } else {
        // TODO: Verify catch all scenario to fall back on default WQ.
        readWorkQueueKey.key.workQueueID = PostCodeToWorkQueue.DEF.getValue();
        return readWorkQueueKey;
      }
    }
  }

  private Map.Entry<DateRange, BDMAddressEvidenceVO>
    getLatestAddressMapEntryByCountry(
      final Map<DateRange, BDMAddressEvidenceVO> dateRangeAddressMap,
      final String countryCode) {

    final TreeMap<DateRange, BDMAddressEvidenceVO> sortedDateRangeAddressMap =
      new TreeMap<>(dateRangeAddressMap);

    Map.Entry<DateRange, BDMAddressEvidenceVO> mostRecentCanadianAddressEntry =
      null;

    for (final Map.Entry<DateRange, BDMAddressEvidenceVO> entry : sortedDateRangeAddressMap
      .descendingMap().entrySet()) {
      if (!entry.getValue().getCountryCode().equals(countryCode)
        || entry.getKey().startsInFuture()) {
        continue;
      }

      if (mostRecentCanadianAddressEntry == null || entry.getKey().start()
        .compareTo(mostRecentCanadianAddressEntry.getKey().start()) > 0) {
        mostRecentCanadianAddressEntry = entry;
      }
    }
    return mostRecentCanadianAddressEntry;
  }

  private boolean isUserRoleManualChangeActivatedForCase(
    final CaseIDKey chKey) throws AppException, InformationalException {

    final BDMOASCaseUserRole bdmOASCaseUserObj =
      BDMOASCaseUserRoleFactory.newInstance();

    final Optional<CaseUserRole> activeCaseUserRole =
      getActiveCaseUserRole(chKey);

    if (activeCaseUserRole.isPresent()) {

      final BDMOASCaseUserRoleKey caseUserRoleKey =
        new BDMOASCaseUserRoleKey();
      caseUserRoleKey.caseUserRoleID = activeCaseUserRole.get().getID();

      // read dtls
      final NotFoundIndicator caseUserRoleIndicator = new NotFoundIndicator();

      final BDMOASCaseUserRoleDtls bdmOASCaseUserDtls =
        bdmOASCaseUserObj.read(caseUserRoleIndicator, caseUserRoleKey);

      if (!caseUserRoleIndicator.isNotFound()) {

        return bdmOASCaseUserDtls.isManuallyChanged;
      }
    }

    return false;
  }

  private boolean isActiveOwnerOrgObjectLinkPresentForCase(
    final CaseIDKey chKey, final ReadWorkQueueKey readWorkQueueKey) {

    final Optional<CaseUserRole> activeCaseUserRole =
      getActiveCaseUserRole(chKey);

    if (activeCaseUserRole.isPresent()) {

      final OrgObjectLink orgObjectLink =
        activeCaseUserRole.get().getOrgObjectLink();
      return (Long) orgObjectLink
        .getOrgObjectIdentifier() == readWorkQueueKey.key.workQueueID;
    }

    return false;
  }

  private Optional<CaseUserRole>
    getActiveCaseUserRole(final CaseIDKey chKey) {

    final curam.piwrapper.caseheader.impl.CaseHeader caseHeader =
      caseHeaderDAO.get(chKey.caseID);

    final List<CaseUserRole> listActiveCaseUserRoleByCase =
      caseUserRoleDAO.listActiveByCase(caseHeader);

    return listActiveCaseUserRoleByCase.stream()
      .filter(
        cur -> cur.getRecordStatus().getCode().equals(RECORDSTATUS.NORMAL))
      .findFirst();

  }

  /**
   * Returns true if client has open
   * application/integrated/pdc case, otherwise false.
   *
   * @param caseIDKey application case identifier
   * @return true if client has open case otherwise false
   *
   * @throws InformationalException Generic Exception Signature
   * @throws AppException Generic Exception Signature
   */
  private CaseCheckResult hasAnyOpenCase(final CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    final CaseCheckResult caseCheckResult = new CaseCheckResult();
    caseCheckResult.hasOpenCase = false;

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseIDKey.caseID;

    final CaseHeaderDtls appCaseHeader = caseHeaderObj.read(caseHeaderKey);

    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(Long.valueOf(caseIDKey.caseID));

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = appCaseHeader.concernRoleID;

    // get all client cases
    final CaseIDStructList caseIDList =
      caseHeaderObj.searchCaseIDByConcernRoleID(concernRoleKey);

    for (final CaseIDStruct caseID : caseIDList.dtls) {

      caseHeaderKey.caseID = caseID.caseID;
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

      // return true if client has open application case
      if (CASETYPECODE.APPLICATION_CASE.equals(caseHeaderDtls.caseTypeCode)
        && CASESTATUS.OPEN.equals(this.applicationCaseDAO
          .get(Long.valueOf(caseID.caseID)).getStatus().getCode())
        && caseID.caseID != caseIDKey.caseID) {

        caseCheckResult.hasOpenCase = true;
        break;
        // END TASK 23597
      } else if (CASETYPECODE.PRODUCTDELIVERY
        .equals(caseHeaderDtls.caseTypeCode)) {

        // check the start date of PDC, if PDC was created in current year
        // the PDC was created this year so return true
        caseCheckResult.hasOpenCase = applicationCase.getSubmittedDateTime()
          .getCalendar().get(Calendar.YEAR) == caseHeaderDtls.startDate
            .getCalendar().get(Calendar.YEAR) ? true : false;
      }
    }

    return caseCheckResult;
  }

  private DateRange getMostRecentActiveDateRangeAddressKeyByAddressMap(
    final Map<DateRange, BDMAddressEvidenceVO> dateRangedAddressMap) {

    DateRange mostRecent = null;
    for (final Map.Entry<DateRange, BDMAddressEvidenceVO> entry : dateRangedAddressMap
      .entrySet()) {
      final DateRange key = entry.getKey();
      if (mostRecent == null || !key.startsInFuture()
        && key.startsAfter(mostRecent.start()) && !key.isEnded()) {
        mostRecent = key;
      }
    }

    return mostRecent;
  }

  /**
   * Gets residential address evidence map by concern role
   * ID.
   *
   * @param concernRoleID the concern role ID
   * @return residential address evidence map by concern role
   * ID
   * @throws AppException app exception
   * @throws InformationalException informational exception
   */
  private Map<DateRange, BDMAddressEvidenceVO>
    getResidentialAddressEvidenceMapByConcernRoleID(final long concernRoleID)
      throws AppException, InformationalException {

    final Map<DateRange, BDMAddressEvidenceVO> dateRangedAddressMap =
      new HashMap<>();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final Long caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    // Search Active Residential Address records
    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
      new CaseIDParticipantIDStatusCode();
    paramCaseIDParticipantIDStatusCode.caseID = caseID;
    paramCaseIDParticipantIDStatusCode.participantID = concernRoleID;
    paramCaseIDParticipantIDStatusCode.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final EvidenceDescriptorDtlsList allActiveEvdList = evidenceDescriptorObj
      .searchActiveByCaseIDParticipantID(paramCaseIDParticipantIDStatusCode);

    final List<EvidenceDescriptorDtls> activeAddressListForParticipant =
      allActiveEvdList.dtls.stream()
        .filter(ed -> ed.evidenceType.equals(CASEEVIDENCE.BDMADDRESS))
        .collect(Collectors.toList());

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    BDMAddressEvidenceVO addressEvidence;
    boolean isResidentialAddress = false;
    for (final EvidenceDescriptorDtls evidenceDescriptorDtls : activeAddressListForParticipant) {

      // Read address evidence data
      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
      evidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceControllerInterface.readEvidence(evidenceKey);

      final DynamicEvidenceDataDetails evidenceData =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      isResidentialAddress =
        evidenceData.getAttribute(BDMOASDatastoreConstants.kAddressType)
          .getValue().equals(CONCERNROLEADDRESSTYPE.PRIVATE);

      if (isResidentialAddress) {
        // Read residential address attributes
        addressEvidence = new BDMAddressEvidenceVO();
        final Date startDate =
          BDMOASEvidenceUtil.getDateAttrValue(evidenceData, FROM_DATE);
        addressEvidence.setFromDate(startDate);
        final Date endDate =
          BDMOASEvidenceUtil.getDateAttrValue(evidenceData, TO_DATE);
        addressEvidence.setToDate(endDate);
        final DateRange dr = new DateRange(startDate, endDate);

        // Read Address Country
        final Address addressObj = AddressFactory.newInstance();
        final AddressKey addressKey = new AddressKey();
        addressKey.addressID =
          Long.valueOf(evidenceData.getAttribute(ADDRESS).getValue());

        final AddressDtls addressDtls = addressObj.read(addressKey);
        addressEvidence.setCountryCode(addressDtls.countryCode);

        final Map<String, String> addressDataMap =
          BDMUtil.getAddressDataMap(addressDtls.addressData);
        addressEvidence
          .setPostalCode(addressDataMap.get(ADDRESSELEMENTTYPE.POSTCODE));

        dateRangedAddressMap.put(dr, addressEvidence);
      }
    }

    return dateRangedAddressMap;
  }

  private BDMContactPreferenceEvidenceVO getClientPreferredLanguage(
    final long concernRoleID) throws AppException, InformationalException {

    final BDMContactPreferenceEvidenceVO contactPreference =
      new BDMContactPreferenceEvidenceVO();

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCCONTACTPREFERENCES;

    final DynamicEvidenceDataAttribute dynamicEvidenceDataAttribute =
      DynamicEvidenceDataAttributeFactory.newInstance();
    final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();

    PDCEvidenceDetailsList pdcEvidenceList = new PDCEvidenceDetailsList();
    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();
    final PersonAndEvidenceTypeList personAndEvidenceType =
      new PersonAndEvidenceTypeList();
    personAndEvidenceType.concernRoleID = concernRoleID;
    personAndEvidenceType.evidenceTypeList = PDCConst.PDCCONTACTPREFERENCES;
    pdcEvidenceList =
      pdcPersonObj.listParticipantEvidenceByTypes(personAndEvidenceType);

    for (int i = 0; i < pdcEvidenceList.list.size(); i++) {
      if (eType.evidenceType
        .equalsIgnoreCase(pdcEvidenceList.list.get(i).evidenceType)) {
        evidenceIDDetails.evidenceID = pdcEvidenceList.list.get(i).evidenceID;
        break;
      }
    }

    final DynamicEvidenceDataIDAndAttrKey idAndAttrKey =
      new DynamicEvidenceDataIDAndAttrKey();
    idAndAttrKey.evidenceID = evidenceIDDetails.evidenceID;
    idAndAttrKey.name = BDMConstants.kpreferredWrittenLanguage;

    final DynamicEvidenceDataAttributeDtlsList dataAttributeWrittenDtlsList =
      dynamicEvidenceDataAttribute
        .searchByEvidenceIDAndAttribute(idAndAttrKey);
    idAndAttrKey.name = BDMConstants.kpreferredOralLanguage;

    final DynamicEvidenceDataAttributeDtlsList dataAttributeOralDtlsList =
      dynamicEvidenceDataAttribute
        .searchByEvidenceIDAndAttribute(idAndAttrKey);

    contactPreference.setEvidenceID(evidenceIDDetails.evidenceID);

    if (!dataAttributeWrittenDtlsList.dtls.isEmpty()) {
      contactPreference.setPreferredWrittenLanguage(
        dataAttributeWrittenDtlsList.dtls.get(0).value);
    }

    if (!dataAttributeOralDtlsList.dtls.isEmpty()) {
      contactPreference.setPreferredOralLanguage(
        dataAttributeOralDtlsList.dtls.get(0).value);
    }

    return contactPreference;
  }

}
