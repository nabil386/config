package curam.ca.gc.bdm.evidence.util.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMPersonMatchConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.codetable.BDMALERTCHOICE;
import curam.ca.gc.bdm.codetable.BDMATTESTATIONTYPE;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMTAXSITUATION;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYPROGRAM;
import curam.ca.gc.bdm.facade.address.struct.BDMAddressEvidenceDtls;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyEvidenceWizardDetailsResult;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMAttestationVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncarcerationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncomeEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
import curam.ca.gc.bdm.sl.fec.struct.BDMFECTaskCreateDetails;
import curam.ca.gc.bdm.sl.maintaincasedeductions.impl.MaintainCaseDeductions;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESEARCHSTATUS;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.IEGYESNO;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TASKPRIORITY;
import curam.codetable.impl.CONCERNROLEALTERNATEIDEntry;
import curam.codetable.impl.PHONETYPEEntry;
import curam.codetable.impl.RECORDSTATUSEntry;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.infrastructure.fact.EvidenceFactory;
import curam.core.facade.infrastructure.intf.Evidence;
import curam.core.facade.infrastructure.struct.EvidenceListKey;
import curam.core.facade.infrastructure.struct.ListAllForInEditWorkspaceDtls;
import curam.core.facade.struct.ConcernRoleIDKey;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.MaintainConcernRoleAltIDFactory;
import curam.core.fact.MaintainPhoneNumberFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.intf.CaseHeader;
import curam.core.intf.MaintainConcernRoleAltID;
import curam.core.intf.MaintainPhoneNumber;
import curam.core.intf.UniqueID;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDEvidenceTypeStatusesKey;
import curam.core.sl.infrastructure.entity.struct.CaseIDStatusAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKeyList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeCaseIDStatusesKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.ModifyParticipantID;
import curam.core.sl.infrastructure.entity.struct.ParticipantIDEvidenceTypeStatusesKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.intf.EvidenceController;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.ECEvidenceForListPageDtls;
import curam.core.sl.infrastructure.struct.ECWIPNewAndUpdateDtls;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.sl.struct.CaseIDAndParticipantRoleIDKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseIDParticipantIDEvidenceTypeKey;
import curam.core.sl.struct.CaseIDTypeAndStatusKey;
import curam.core.sl.struct.CaseIDTypeCodeKey;
import curam.core.sl.struct.CaseParticipantRoleFullDetails1;
import curam.core.sl.struct.CaseParticipantRoleIDAndNameDtlsList;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceCaseKey;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.AlternateIDDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseReference;
import curam.core.struct.ConcernRoleAlternateIDDtlsList;
import curam.core.struct.ConcernRoleEmailAddressKey;
import curam.core.struct.ConcernRoleIDTypeStatusEndDateKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRolePhoneNumberDtls;
import curam.core.struct.ElementDetails;
import curam.core.struct.MaintainConcernRoleAltIDKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonKey;
import curam.core.struct.PhoneNumberDetails;
import curam.core.struct.UniqueIDKeySet;
import curam.creole.value.CodeTableItem;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchAttributeException;
import curam.dynamicevidence.datastore.impl.DynamicEvidenceDatastore;
import curam.dynamicevidence.datastore.impl.DynamicEvidenceDatastoreFactory;
import curam.dynamicevidence.definition.impl.Attribute;
import curam.dynamicevidence.definition.impl.DataAttribute;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.facade.fact.DynamicEvidenceMaintenanceFactory;
import curam.dynamicevidence.facade.intf.DynamicEvidenceMaintenance;
import curam.dynamicevidence.facade.struct.DynEvdModifyDetails;
import curam.dynamicevidence.facade.struct.DynamicEvidenceData;
import curam.dynamicevidence.facade.wrapper.impl.DataWrapperXMLUtils;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeKey;
import curam.dynamicevidence.sl.entity.struct.EvidenceIDDetails;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.sl.struct.impl.ReadEvidenceDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.facade.struct.PDCEvidenceDetails;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.fact.PDCAddressFactory;
import curam.pdc.fact.PDCAlternateIDFactory;
import curam.pdc.fact.PDCEmailAddressFactory;
import curam.pdc.fact.PDCPhoneNumberFactory;
import curam.pdc.fact.PDCRelationshipsFactory;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.fact.ParticipantDataCaseFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCAddress;
import curam.pdc.intf.PDCAlternateID;
import curam.pdc.intf.PDCPhoneNumber;
import curam.pdc.intf.PDCRelationships;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.ParticipantAddressDetails;
import curam.pdc.struct.ParticipantAlternateIDDetails;
import curam.pdc.struct.ParticipantEmailAddressDetails;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.pdc.struct.ParticipantRelationshipDetails;
import curam.piwrapper.evidence.impl.EvidenceDescriptorDAO;
import curam.piwrapper.participantmanager.impl.ConcernRoleRelationship;
import curam.piwrapper.participantmanager.impl.ConcernRoleRelationshipDAO;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.AccessLevel;
import curam.util.type.AccessLevelType;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.StringList;
import curam.util.workflow.impl.EnactmentService;
import curam.workspaceservices.applicationprocessing.impl.DataStoreCEFMappings;
import curam.workspaceservices.codetable.ApplicationChannel;
import curam.workspaceservices.mappingbeans.impl.IntakeApplicationMappingBean;
import curam.workspaceservices.mappingbeans.impl.PhoneNumberMappingBean;
import curam.workspaceservices.mappingbeans.impl.RelationshipMappingBean;
import curam.workspaceservices.util.impl.DatastoreHelper;
import curam.workspaceservices.util.impl.DateTimeTools;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Utility class to provide functionality related to Evidences.
 *
 * @since 7/22/2016
 *
 */
@SuppressWarnings("restriction")
public class BDMEvidenceUtil {

  private final BDMUtil bdmUtil = new BDMUtil();

  @Inject
  private ConcernRoleDAO concernroleDAO;

  @Inject
  private ConcernRoleRelationshipDAO concernRoleRelationshipDAO;

  @Inject
  private EvidenceDescriptorDAO evidenceDescriptorDAO;

  public BDMEvidenceUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // BEGIN TASK 16973 - Bank account evidence
  public static final String kFINANCIAL_INSTITUTION_NUMBER =
    "financialInstitutionNumber";

  public static final String kBRANCH_TRANSIT_NUMBER = "branchTransitNumber";

  public static final String kSORT_CODE = "sortCode";

  public static final String kPROGRAM = "program";
  // END TASK 16973 - Bank account evidence

  private static String CODE = " code: ";

  /**
   * Gets the right active evidence set by Evidence Type.
   *
   * @param list
   * @param evidenceType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static EvidenceDescriptorDtlsList getEvidenceSet(
    final EvidenceDescriptorDtlsList list, final String evidenceType)
    throws AppException, InformationalException {

    if (list.dtls.isEmpty()) {
      return new EvidenceDescriptorDtlsList();
    }
    final EvidenceDescriptorDtlsList filteredByTypeList =
      new EvidenceDescriptorDtlsList();
    for (final EvidenceDescriptorDtls evidenceRecord : list.dtls.items()) {
      if (evidenceRecord.evidenceType.equals(evidenceType)
        && isActiveOrInEdit(evidenceRecord)) {
        filteredByTypeList.dtls.addRef(evidenceRecord);
      }
    }

    long successionID = 0L;

    final Map<Long, EvidenceDescriptorDtlsList> successionMap =
      new HashMap<Long, EvidenceDescriptorDtlsList>();

    EvidenceDescriptorDtlsList successionDescriptorList = null;
    for (final EvidenceDescriptorDtls record : filteredByTypeList.dtls
      .items()) {
      if (successionID == 0L || successionID != record.successionID) {
        successionDescriptorList = new EvidenceDescriptorDtlsList();
        successionID = record.successionID;
        successionDescriptorList.dtls.addRef(record);
        successionMap.put(Long.valueOf(successionID),
          successionDescriptorList);
      } else {
        final EvidenceDescriptorDtlsList evidencelist =
          successionMap.get(Long.valueOf(record.successionID));
        evidencelist.dtls.addRef(record);
        successionMap.put(Long.valueOf(record.successionID), evidencelist);
      }
    }
    final EvidenceDescriptorDtlsList finalList =
      new EvidenceDescriptorDtlsList();
    for (final EvidenceDescriptorDtlsList mapList : successionMap.values()) {
      if (!mapList.dtls.isEmpty()) {
        final EvidenceDescriptorDtls dtls = new EvidenceDescriptorDtls();
        dtls.assign(mapList.dtls.item(0));
        finalList.dtls.addRef(dtls);
      }
    }
    return finalList;
  }

  /**
   * Method to get Dynamic Evidence By Evidence Type And EvidenceID AND CaseID
   * Returns DynamicEvidenceDataDetails
   *
   * @param activeDescriptorDtlsList
   * - Evidence Descriptor List for Which the evidence data need to be fetched
   * List containing evidence
   * descriptor of a specific type of evidence only
   * @return List<DynamicEvidenceDataDetails> - List of Data from the
   * corresponding DynamicEvidencDataAttribute Table
   * @throws AppException
   * @throws InformationalException
   */
  public static List<DynamicEvidenceDataDetails> getDynamicEvidenceList(
    final EvidenceDescriptorDtlsList activeDescriptorDtlsList,
    final String evidenceType) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    for (final EvidenceDescriptorDtls descriptorDtls : getEvidenceSet(
      activeDescriptorDtlsList, evidenceType).dtls) {

      final EvidenceServiceInterface evidenceServiceInterface =
        EvidenceGenericSLFactory
          .instance(Long.valueOf(descriptorDtls.relatedID));

      final EvidenceCaseKey key = new EvidenceCaseKey();
      key.caseIDKey.caseID = descriptorDtls.caseID;
      key.evidenceKey.evidenceID = descriptorDtls.relatedID;
      key.evidenceKey.evType = descriptorDtls.evidenceType;

      final ReadEvidenceDetails readEvidenceDetails =
        evidenceServiceInterface.readEvidence(key);

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        readEvidenceDetails.dtls;

      evidenceDataDetailsList.add(dynamicEvidenceDataDetails);

    }
    return evidenceDataDetailsList;

  }

  /**
   * Gets a GenericSLDataDetails object based on evidence ID. The
   * GenericSLDataDetails instance is a wrapper object
   * for a DynamicEvidenceDataDetails object. Code calling this helper method
   * can access the
   * DynamicEvidenceDataDetails object by calling the getEvidenceData() method
   * which can, in turn, be used to access
   * DynamicEvidenceDataAttributeDetails information.<br>
   * <br>
   *
   * Note that if an evidence key related to Static evidence is passed into this
   * method that a
   * java.lang.ClassCastException will be thrown at runtime. <b>You must guard
   * against this.</b>
   *
   * @param key
   * a key containing the evidence ID and Type
   * @return A GenericSLDataDetials object that contains details about a
   * specific piece of dynamic evidence
   * @throws AppException
   * @throws InformationalException
   */
  public static GenericSLDataDetails getGenericSLDataDetails(
    final EIEvidenceKey key) throws AppException, InformationalException {

    // initialize the EvidenceControllerInterface object
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerObj.readEvidence(key);

    // get the DynamicEvidenceDataDetails and wrap it within the return object
    final GenericSLDataDetails details = new GenericSLDataDetails();
    details
      .setData((DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject);

    return details;
  }

  // ____________________________________________________________________________________________

  /**
   * Checks if and Evidence is either active or in edit mode.
   *
   * @param dtls
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private static boolean isActiveOrInEdit(final EvidenceDescriptorDtls dtls)
    throws AppException, InformationalException {

    final String active = EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final String inedit = EVIDENCEDESCRIPTORSTATUS.INEDIT;
    if (dtls.statusCode.equals(inedit)
      || dtls.statusCode.equals(active) && !dtls.pendingRemovalInd) {
      return true;
    }
    return false;
  }

  /**
   * Gets a list of DynamicEvidenceDataDetails matching the given search data.
   *
   * @param caseID
   * The case that contains the desired evidence
   * @param date
   * The point in time to consider
   * @param evidenceType
   * The type of evidence to search for, eg. a code from
   * curam.codetable.CASEEVIDENCE
   * @param concernRoleID
   * The concern role to search for
   * @return A list of DynamicEvidenceDataDetails, from which attributes can be
   * read
   * @throws AppException
   * @throws InformationalException
   */
  public static List<DynamicEvidenceDataDetails> getDynamicEvidences(
    final long caseID, final Date date, final String evidenceType,
    final long concernRoleID) throws AppException, InformationalException {

    return getDynamicEvidences(
      retrieveEvidence(caseID, date, evidenceType, concernRoleID));
  }

  /**
   * Retrieves a list of relatedIDs for the given search data.
   *
   * @param caseID
   * The case (integrated or application) with the desired evidence
   * @param date
   * The point in time desired
   * @param evidenceType
   * The type of evidence, eg. from curam.codetable.CASEEVIDENCE
   * @param concernRoleID
   * The concern role to get evidence from; if 0, then it gets all such evidence
   * on the case
   * @return A list of relatedIDs
   * @throws AppException
   * @throws InformationalException
   */
  public static List<Long> retrieveEvidence(final long caseID,
    final Date date, final String evidenceType, final long concernRoleID)
    throws AppException, InformationalException {

    /*
     * EvidenceController evidenceControllerObj =
     * EvidenceControllerFactory.newInstance(); ECRetrieveEvidenceKey
     * evidenceKey = new ECRetrieveEvidenceKey(); evidenceKey.caseID = caseID;
     * evidenceKey.dateOfCalculation =
     * denialDate; evidenceKey.evidenceType = evidenceType; ECRelatedIDList
     * evidence =
     * evidenceControllerObj.retrieveEvidence(evidenceKey); return evidence;
     */
    // Return data
    final List<Long> idList = new ArrayList<Long>();

    // OOTB classes
    final curam.core.facade.infrastructure.intf.Evidence evidenceObj =
      EvidenceFactory.newInstance();

    // Get the case evidence
    final EvidenceListKey key = new EvidenceListKey();
    key.key.caseID = caseID;
    key.key.evidenceType = evidenceType;
    final ECEvidenceForListPageDtls insuranceEvidenceLists =
      evidenceObj.listEvidence(key).dtls;
    // gather active evidence
    for (final ECActiveEvidenceDtls evidence : insuranceEvidenceLists.activeList.dtls) {
      // only add evidence attached to the participantRoleID
      if ((evidence.participantID == concernRoleID || concernRoleID == 0)
        && (evidence.effectiveFrom.isZero()
          || !date.before(evidence.effectiveFrom))
        && (evidence.endDate.isZero() || !date.after(evidence.endDate))
        && EVIDENCEDESCRIPTORSTATUS.ACTIVE.equals(evidence.statusCode)) {
        idList.add(evidence.evidenceID);
      }
    }
    // if there wasn't any, gather in-edit evidence
    if (idList.isEmpty()) {
      for (final ECWIPNewAndUpdateDtls evidence : insuranceEvidenceLists.newAndUpdateList.dtls) {
        // only add evidence attached to the participantRoleID
        if ((evidence.participantID == concernRoleID || concernRoleID == 0)
          && (evidence.effectiveFrom.isZero()
            || !date.before(evidence.effectiveFrom))
          && (evidence.endDate.isZero() || !date.after(evidence.endDate))) {
          idList.add(evidence.evidenceID);
        }
      }
    }
    return idList;
  }

  // ____________________________________________________________________________________________

  /**
   * Gets a list of DynamicEvidenceDataDetails from the given list of
   * evidenceIDs
   *
   * @param evidenceIDs
   * A list of evidenceIDs (sometimes aka relatedIDs), not to be confused with
   * evidenceDescriptorIDs
   * @return A list of DynamicEvidenceDataDetails, from which attributes can be
   * read
   */
  public static List<DynamicEvidenceDataDetails>
    getDynamicEvidences(final List<Long> evidenceIDs) {

    if (evidenceIDs.size() == 0) {
      return java.util.Collections.emptyList();
    }
    final List<DynamicEvidenceDataDetails> dynamicEvidences =
      new LinkedList<DynamicEvidenceDataDetails>();
    final DynamicEvidenceDatastore dynamicEvidenceDatastoreObj =
      DynamicEvidenceDatastoreFactory.newDatastoreInstance();
    for (final Long evidenceID : evidenceIDs) {
      dynamicEvidences
        .add(dynamicEvidenceDatastoreObj.readEvidence(evidenceID));
    }
    return dynamicEvidences;
  }

  // ____________________________________________________________________________________________

  /**
   * Checks whether an evidence is in date range
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static boolean isEvidenceInDateRange(final Date startDate,
    final Date endDate) {

    final Date currentDate = Date.getCurrentDate();

    return isEvidenceInDateRange(startDate, endDate, currentDate);
  }

  /**
   * PR-5968 - LNel - ASES - issues with non-PR addresses
   * Checks whether an evidence is in date range
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static boolean isEvidenceInDateRange(final Date startDate,
    final Date endDate, final Date effDate) {

    if (startDate != Date.kZeroDate
      && (effDate.after(startDate) || effDate.equals(startDate))
      && (effDate.before(endDate) || endDate.equals(Date.kZeroDate)
        || effDate.equals(endDate))) {
      return true;
    }

    return false;
  }
  // ____________________________________________________________________________________________

  /**
   * Checks if an IC has In-Edit evidences.
   *
   * @param caseID
   * @return
   * @throws InformationalException
   * @throws AppException
   */
  public static boolean hasInEditEvidences(final long integratedCaseID)
    throws AppException, InformationalException {

    final Evidence evidence = EvidenceFactory.newInstance();
    final CaseIDParticipantIDEvidenceTypeKey idEvidenceTypeKey =
      new CaseIDParticipantIDEvidenceTypeKey();
    idEvidenceTypeKey.caseIDKey.caseID = integratedCaseID;
    final ListAllForInEditWorkspaceDtls editWorkspaceDtls =
      evidence.listAllForInEditWorkspace(idEvidenceTypeKey);

    return editWorkspaceDtls.wipDtls.newAndUpdateList.dtls.size() > 0;
  }

  /**
   * USVI-327 SA Creates a Person Reference Number if there isn't one.
   *
   * @param concernRoleID
   */
  public static void createPersonReferenceNumber(final long concernRoleID) {

    final curam.core.intf.ConcernRoleAlternateID concernrolealternateID =
      ConcernRoleAlternateIDFactory.newInstance();
    final ConcernRoleIDTypeStatusEndDateKey concernRoleIDTypeStatusEndDateKey =
      new ConcernRoleIDTypeStatusEndDateKey();

    concernRoleIDTypeStatusEndDateKey.concernRoleID = concernRoleID;
    concernRoleIDTypeStatusEndDateKey.statusCode = RECORDSTATUS.NORMAL;
    concernRoleIDTypeStatusEndDateKey.typeCode =
      CONCERNROLEALTERNATEID.PERSON_REFERENCE_NUMBER;

    try {
      final ConcernRoleAlternateIDDtlsList concernRoleAlternateIDDtlsList =
        concernrolealternateID.searchByConcernRoleIDTypeStatusAndEndDate(
          concernRoleIDTypeStatusEndDateKey);

      // if there isn't a person reference number create one
      if (concernRoleAlternateIDDtlsList.dtls.size() == 0
        && concernRoleID != 0L) {

        final PDCAlternateID pdcAlternateID =
          PDCAlternateIDFactory.newInstance();
        final ParticipantAlternateIDDetails paramParticipantAlternateIDDetails =
          new ParticipantAlternateIDDetails();
        final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
        uniqueIDKeySet.keySetName = BDMConstants.PERSON;

        paramParticipantAlternateIDDetails.concernRoleID = concernRoleID;
        paramParticipantAlternateIDDetails.alternateID = String.valueOf(
          UniqueIDFactory.newInstance().getNextIDFromKeySet(uniqueIDKeySet));
        paramParticipantAlternateIDDetails.startDate = Date.getCurrentDate();
        paramParticipantAlternateIDDetails.endDate = Date.kZeroDate;
        paramParticipantAlternateIDDetails.statusCode = RECORDSTATUS.NORMAL;
        paramParticipantAlternateIDDetails.typeCode =
          CONCERNROLEALTERNATEID.PERSON_REFERENCE_NUMBER;

        // BEGIN - JH - set primaryAlternateInd to true for person reference
        // number
        paramParticipantAlternateIDDetails.primaryAlternateInd = true;
        // END - JH - set primaryAlternateInd to true for person reference
        // number

        pdcAlternateID.insert(paramParticipantAlternateIDDetails);
      }
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }
  }

  /**
   * USVI-931 PS Creates a Prospect Person Reference Number if there isn't
   * one.
   *
   * @param concernRoleID
   */
  public static void
    createProspectPersonReferenceNumber(final long concernRoleID) {

    final curam.core.intf.ConcernRoleAlternateID concernrolealternateID =
      ConcernRoleAlternateIDFactory.newInstance();
    final ConcernRoleIDTypeStatusEndDateKey concernRoleIDTypeStatusEndDateKey =
      new ConcernRoleIDTypeStatusEndDateKey();

    concernRoleIDTypeStatusEndDateKey.concernRoleID = concernRoleID;
    concernRoleIDTypeStatusEndDateKey.statusCode = RECORDSTATUS.NORMAL;
    concernRoleIDTypeStatusEndDateKey.typeCode =
      CONCERNROLEALTERNATEID.PROSPECT_PERSON_REFERENCE_NUMBER;

    try {
      final ConcernRoleAlternateIDDtlsList concernRoleAlternateIDDtlsList =
        concernrolealternateID.searchByConcernRoleIDTypeStatusAndEndDate(
          concernRoleIDTypeStatusEndDateKey);

      // if there isn't a prospect reference number create one
      if (concernRoleAlternateIDDtlsList.dtls.size() == 0
        && concernRoleID != 0L) {

        final PDCAlternateID pdcAlternateID =
          PDCAlternateIDFactory.newInstance();
        final ParticipantAlternateIDDetails paramParticipantAlternateIDDetails =
          new ParticipantAlternateIDDetails();
        final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
        uniqueIDKeySet.keySetName = BDMConstants.PERSON;

        paramParticipantAlternateIDDetails.concernRoleID = concernRoleID;
        paramParticipantAlternateIDDetails.alternateID = String.valueOf(
          UniqueIDFactory.newInstance().getNextIDFromKeySet(uniqueIDKeySet));
        paramParticipantAlternateIDDetails.startDate = Date.getCurrentDate();
        paramParticipantAlternateIDDetails.endDate = Date.kZeroDate;
        paramParticipantAlternateIDDetails.statusCode = RECORDSTATUS.NORMAL;
        paramParticipantAlternateIDDetails.typeCode =
          CONCERNROLEALTERNATEID.PROSPECT_PERSON_REFERENCE_NUMBER;
        paramParticipantAlternateIDDetails.primaryAlternateInd = false;

        pdcAlternateID.insert(paramParticipantAlternateIDDetails);
      }
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }
  }

  /**
   * Created Dynamic Evidence on IC using Case Reference that will return
   * details of
   * evidence after creating it
   *
   * @param caseReference
   * @param evidenceData
   * @param evidenceType
   * @return
   */
  public static ReturnEvidenceDetails createAndReturnICDynamicEvidenceByCase(
    final String caseReference, final HashMap<String, String> evidenceData,
    final String evidenceType) throws AppException, InformationalException {

    final CaseHeader caseHeader =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseReference caseRef = new CaseReference();
    caseRef.caseReference = caseReference;
    final CaseHeaderDtls caseHeaderDtls =
      caseHeader.readByCaseReference(caseRef);
    Long participantDataCaseID = 0L;

    if (caseHeaderDtls.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
      participantDataCaseID = caseHeaderDtls.caseID;
    }

    if (participantDataCaseID != 0L) {
      final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
        new CaseIDTypeAndStatusKey();
      caseIDTypeAndStatusKey.key.caseID = participantDataCaseID;
      caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
      caseIDTypeAndStatusKey.key.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
      final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
        CaseParticipantRoleFactory.newInstance()
          .listCaseParticipantRolesByTypeCaseIDAndStatus(
            caseIDTypeAndStatusKey);

      evidenceData.put("participant",
        String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
          .get(0).caseParticipantRoleID));

      final DynamicEvidenceMaintenance dynObj =
        DynamicEvidenceMaintenanceFactory.newInstance();
      final DynamicEvidenceData dynamicEvidenceData =
        new DynamicEvidenceData();
      final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

      dynamicEvidenceData.data =
        BDMEvidenceUtil.setDynamicEvidenceDetails(evidenceData);
      dynamicEvidenceData.caseIDKey.caseID = participantDataCaseID;
      dynamicEvidenceData.descriptor.evidenceType = evidenceType;
      dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
      dynamicEvidenceData.descriptor.changeReason =
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
      dynMod.effectiveDateUsed =
        BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

      // create the evidence
      return dynObj.createEvidence(dynamicEvidenceData, dynMod);
    }

    return null;
  }

  /**
   * Created Dynamic Evidence on IC using Case Reference
   *
   * @param caseReference
   * @param evidenceData
   * @param evidenceType
   * @return
   */
  public static boolean createICDynamicEvidenceByCase(
    final String caseReference, final HashMap<String, String> evidenceData,
    final String evidenceType) {

    try {

      final CaseHeader caseHeader =
        curam.core.fact.CaseHeaderFactory.newInstance();
      final CaseReference caseRef = new CaseReference();
      caseRef.caseReference = caseReference;
      final CaseHeaderDtls caseHeaderDtls =
        caseHeader.readByCaseReference(caseRef);
      Long participantDataCaseID = 0L;

      if (caseHeaderDtls.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
        participantDataCaseID = caseHeaderDtls.caseID;
      }

      if (participantDataCaseID != 0L) {
        final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
          new CaseIDTypeAndStatusKey();
        caseIDTypeAndStatusKey.key.caseID = participantDataCaseID;
        caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
        caseIDTypeAndStatusKey.key.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
        final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
          CaseParticipantRoleFactory.newInstance()
            .listCaseParticipantRolesByTypeCaseIDAndStatus(
              caseIDTypeAndStatusKey);

        evidenceData.put("participant",
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));

        final DynamicEvidenceMaintenance dynObj =
          DynamicEvidenceMaintenanceFactory.newInstance();
        final DynamicEvidenceData dynamicEvidenceData =
          new DynamicEvidenceData();
        final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

        dynamicEvidenceData.data =
          BDMEvidenceUtil.setDynamicEvidenceDetails(evidenceData);
        dynamicEvidenceData.caseIDKey.caseID = participantDataCaseID;
        dynamicEvidenceData.descriptor.evidenceType = evidenceType;
        dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
        dynamicEvidenceData.descriptor.changeReason =
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
        dynMod.effectiveDateUsed =
          BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

        // create the evidence
        dynObj.createEvidence(dynamicEvidenceData, dynMod);

      }

      return true;

    } catch (final InformationalException e) {

      Trace.kTopLevelLogger.error(BDMConstants.BDM_LOGS_PREFIX
        + "Failed to create Evidence" + evidenceType);
      return false;
    } catch (final AppException e) {
      Trace.kTopLevelLogger.error(BDMConstants.BDM_LOGS_PREFIX
        + "Failed to create Evidence" + evidenceType);
      return false;
    }
  }

  /**
   * USVI-482 SJ Creates dynamic evidence on IC
   *
   * @param participantRoleID
   * @param evidenceData
   */
  public static boolean createICDynamicEvidence(final long participantRoleID,
    final HashMap<String, String> evidenceData, final String evidenceType) {

    try {

      final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
        new ConcernRoleIDStatusCodeKey();
      concernRoleIDStatusCodeKey.dtls.concernRoleID = participantRoleID;
      concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
      final CaseHeaderDtlsList caseheaderDtlsList = CaseHeaderFactory
        .newInstance().searchByConcernRoleID(concernRoleIDStatusCodeKey);
      Long participantDataCaseID = 0L;

      if (!caseheaderDtlsList.dtlsList.dtls.isEmpty()) {
        for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
          if (dtl.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
            participantDataCaseID = dtl.caseID;
          }
        }
        if (participantDataCaseID != 0L) {

          final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
            new CaseIDTypeAndStatusKey();
          caseIDTypeAndStatusKey.key.caseID = participantDataCaseID;
          caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
          caseIDTypeAndStatusKey.key.typeCode =
            CASEPARTICIPANTROLETYPE.PRIMARY;
          final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
            CaseParticipantRoleFactory.newInstance()
              .listCaseParticipantRolesByTypeCaseIDAndStatus(
                caseIDTypeAndStatusKey);

          evidenceData.put("participant",
            String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
              .get(0).caseParticipantRoleID));

          final DynamicEvidenceMaintenance dynObj =
            DynamicEvidenceMaintenanceFactory.newInstance();
          final DynamicEvidenceData dynamicEvidenceData =
            new DynamicEvidenceData();
          final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

          dynamicEvidenceData.data =
            BDMEvidenceUtil.setDynamicEvidenceDetails(evidenceData);
          dynamicEvidenceData.caseIDKey.caseID = participantDataCaseID;
          dynamicEvidenceData.descriptor.evidenceType = evidenceType;
          dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
          dynamicEvidenceData.descriptor.changeReason =
            EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
          dynMod.effectiveDateUsed =
            BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

          // create the evidence
          dynObj.createEvidence(dynamicEvidenceData, dynMod);

        }
      }

      return true;

    } catch (final InformationalException e) {

      Trace.kTopLevelLogger.error(BDMConstants.BDM_LOGS_PREFIX
        + "Failed to create Evidence" + evidenceType);
      return false;
    } catch (final AppException e) {
      Trace.kTopLevelLogger.error(BDMConstants.BDM_LOGS_PREFIX
        + "Failed to create Evidence" + evidenceType);
      return false;
    }
  }

  /**
   * USVI-367 SA Creates dynamic evidence on PDC
   *
   * @param participantRoleID
   * @param evidenceData
   */
  public static ReturnEvidenceDetails createPDCDynamicEvidence(
    final long participantRoleID, final HashMap<String, String> evidenceData,
    final String evidenceType, final String evidenceChangeReason)
    throws AppException, InformationalException {

    // BEGIN -PR-3658 -IG- Removed Try catch block because that block type
    // should be used by the method calling this
    // method; otherwise, this method catches the exception but the application
    // proceeds without recognizing the
    // exception
    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.dtls.concernRoleID = participantRoleID;
    concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
    final CaseHeaderDtlsList caseheaderDtlsList = CaseHeaderFactory
      .newInstance().searchByConcernRoleID(concernRoleIDStatusCodeKey);
    Long participantDataCaseID = 0L;

    if (!caseheaderDtlsList.dtlsList.dtls.isEmpty()) {
      for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
        if (dtl.caseTypeCode.equals(CASETYPECODE.PARTICIPANTDATACASE)) {
          participantDataCaseID = dtl.caseID;
        }
      }
      if (participantDataCaseID != 0L) {

        final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
          new CaseIDTypeAndStatusKey();
        caseIDTypeAndStatusKey.key.caseID = participantDataCaseID;
        caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
        caseIDTypeAndStatusKey.key.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
        final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
          CaseParticipantRoleFactory.newInstance()
            .listCaseParticipantRolesByTypeCaseIDAndStatus(
              caseIDTypeAndStatusKey);

        if (evidenceType.equals(PDCConst.PDCPHONENUMBER)) {
          evidenceData.put("caseParticipantRoleID",
            String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
              .get(0).caseParticipantRoleID));
        } else {
          evidenceData.put("participant",
            String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
              .get(0).caseParticipantRoleID));
        }

        final DynamicEvidenceMaintenance dynObj =
          DynamicEvidenceMaintenanceFactory.newInstance();
        final DynamicEvidenceData dynamicEvidenceData =
          new DynamicEvidenceData();
        final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

        dynamicEvidenceData.data =
          BDMEvidenceUtil.setDynamicEvidenceDetails(evidenceData);
        dynamicEvidenceData.caseIDKey.caseID = participantDataCaseID;
        dynamicEvidenceData.descriptor.evidenceType = evidenceType;
        dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
        dynamicEvidenceData.descriptor.changeReason = evidenceChangeReason;
        dynMod.effectiveDateUsed =
          BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

        // create the evidence
        final ReturnEvidenceDetails returnEvidenceDetails =
          dynObj.createEvidence(dynamicEvidenceData, dynMod);

        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          updateChangeReason(evidenceType, evidenceChangeReason,
            returnEvidenceDetails);
        return returnEvidenceDetails;
      }
    }
    // END -PR-3658 -IG- Removed Try catch block because that block type should
    // be used by the method calling this
    // method; otherwise, this method catches the exception but the application
    // proceeds without recognizing the
    // exception
    return null;
  }

  /**
   * USVI-367 SA Sets Dynamic Evidence Details
   *
   * @param evidenceData
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static String
    setDynamicEvidenceDetails(final HashMap<String, String> evidenceData) {

    Document document = null;
    String result = "";

    try {
      document = DataWrapperXMLUtils.getNewDocument();

      final Element dataElement =
        document.createElement("dynamicEvidenceData");
      Element valueElement = null;
      Text valueText = null;

      final Iterator it = evidenceData.entrySet().iterator();
      while (it.hasNext()) {
        final Map.Entry pair = (Map.Entry) it.next();

        valueElement = document.createElement(pair.getKey().toString());
        if (pair.getValue() != null) {
          valueText = document.createTextNode(pair.getValue().toString());
          valueElement.appendChild(valueText);
        }

        if (pair.getKey().toString()
          .equalsIgnoreCase("caseParticipantRoleID")) {
          final Element dataElement1 =
            document.createElement("caseParticipants");
          final Element dataElement2 = document.createElement("participant");
          dataElement2.appendChild(valueElement);
          dataElement1.appendChild(dataElement2);
          dataElement.appendChild(dataElement1);
        } else {
          dataElement.appendChild(valueElement);
        }
      }
      document.appendChild(dataElement);

    } catch (final ParserConfigurationException e) {
      e.printStackTrace();
    }

    try {
      result = DataWrapperXMLUtils.printDocumentToString(document);
    } catch (final TransformerException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * getDynamicEvidenceValue uses the relatedid for any evidence and the
   * attribute name to find the value of the given attribute
   *
   * @param relatedID
   * @param dynamicEvidenceDataAttributeName
   * @return value
   */
  public static String getDynamicEvidenceValue(final long relatedID,
    final String dynamicEvidenceDataAttributeName) {

    final DynamicEvidenceDataAttribute dynamicEvidenceDataAttribute =
      curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory
        .newInstance();

    final EvidenceIDDetails evidenceIDDtls = new EvidenceIDDetails();
    evidenceIDDtls.evidenceID = relatedID;
    DynamicEvidenceDataAttributeDtlsList dynamicEvidenceDataAttributeDtlsList;
    try {
      dynamicEvidenceDataAttributeDtlsList =
        dynamicEvidenceDataAttribute.searchByEvidenceID(evidenceIDDtls);

      for (final DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls : dynamicEvidenceDataAttributeDtlsList.dtls) {
        if (dynamicEvidenceDataAttributeDtls.name
          .equals(dynamicEvidenceDataAttributeName)) {
          return dynamicEvidenceDataAttributeDtls.value;
        }
      }
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * PR-4507 & PR-3506
   * Generic method to create alternate id of specified type
   *
   * @param concernRoleID
   * @param alternateID
   * @param alternateIDtype
   * @throws InformationalException
   * @throws AppException
   */
  public static void createAlternateID(final long concernRoleID,
    final String alternateID, final String alternateIDtype)
    throws AppException, InformationalException {

    final PDCAlternateID pdcAlternateID = PDCAlternateIDFactory.newInstance();
    final ParticipantAlternateIDDetails paramParticipantAlternateIDDetails =
      new ParticipantAlternateIDDetails();

    paramParticipantAlternateIDDetails.concernRoleID = concernRoleID;
    paramParticipantAlternateIDDetails.alternateID = alternateID;
    paramParticipantAlternateIDDetails.startDate = Date.getCurrentDate();
    paramParticipantAlternateIDDetails.endDate = Date.kZeroDate;
    paramParticipantAlternateIDDetails.statusCode = RECORDSTATUS.NORMAL;
    paramParticipantAlternateIDDetails.typeCode = alternateIDtype;
    paramParticipantAlternateIDDetails.primaryAlternateInd = false;

    pdcAlternateID.insert(paramParticipantAlternateIDDetails);
  }

  /**
   * Method to create sin evidence for a concernRoleID
   *
   * @param concernRoleID
   * @param ssn
   * @throws AppException
   * @throws InformationalException
   */
  public static void createSINEvidence(final Long concernRoleID,
    final String sin, final Date registrationDate)
    throws AppException, InformationalException {

    // create sin evidence
    final MaintainConcernRoleAltID maintainConcernRoleAltIDObj =
      MaintainConcernRoleAltIDFactory.newInstance();

    final MaintainConcernRoleAltIDKey maintainConcernRoleAltIDKey =
      new MaintainConcernRoleAltIDKey();
    maintainConcernRoleAltIDKey.concernRoleID = concernRoleID;

    final AlternateIDDetails alternateIDDetails = new AlternateIDDetails();

    alternateIDDetails.alternateID = sin;
    alternateIDDetails.concernRoleID = concernRoleID;
    alternateIDDetails.startDate = registrationDate;
    alternateIDDetails.typeCode =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final boolean sinIsPrimary = Configuration
      .getBooleanProperty("curam.workspaceservices.sin.treatasprimary");

    if (sinIsPrimary) {
      alternateIDDetails.primaryAlternateInd = true;
    }

    maintainConcernRoleAltIDObj.createAlternateID(maintainConcernRoleAltIDKey,
      alternateIDDetails);

  }

  /**
   * Utility method to return list of evidences of a given type for IC
   *
   * @param integratedCaseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static EvidenceDescriptorDtlsList getStaticEvidenceList(
    final long integratedCaseID, final String evidenceType)
    throws AppException, InformationalException {

    final EvidenceTypeCaseIDStatusesKey evidenceTypeCaseIDStatusesKey =
      new EvidenceTypeCaseIDStatusesKey();
    evidenceTypeCaseIDStatusesKey.caseID = integratedCaseID;
    evidenceTypeCaseIDStatusesKey.evidenceType = evidenceType;
    evidenceTypeCaseIDStatusesKey.statusCode1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    return EvidenceDescriptorFactory.newInstance()
      .searchByCaseIDEvidenceTypeAndStatus(evidenceTypeCaseIDStatusesKey);

  }

  /**
   * This utility method checks the active evidences and returns true, if the
   * active evidence exist already for the
   * member
   *
   * VR
   *
   * @param concernRoleKey
   * @param evidenceType
   * @return Boolean
   *
   * @throws AppException
   * @throws InformationalException
   *
   */
  public static boolean isActiveEvidenceExist(
    final ConcernRoleKey concernRoleKey, final String evidenceType)
    throws AppException, InformationalException {

    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final CaseKey caseKey = new CaseKey();

    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final ECActiveEvidenceDtlsList ecActiveEvidenceDtlsList =
      evidenceControllerObj.listActive(caseKey);

    for (final ECActiveEvidenceDtls ecActiveEvidenceDtls : ecActiveEvidenceDtlsList.dtls) {
      if (ecActiveEvidenceDtls.evidenceType.equals(evidenceType)) {
        return true;
      }
    }
    return false;
  }

  /**
   * THis method finds the current SIN Evidence
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Long findCurrentSINEvidence(final long concernRoleID)
    throws AppException, InformationalException {

    // retrieve prospect person PDC evidence
    final curam.pdc.facade.intf.PDCPerson pdcPerson =
      curam.pdc.facade.fact.PDCPersonFactory.newInstance();
    final PersonKey paramPersonKey = new PersonKey();
    paramPersonKey.concernRoleID = concernRoleID;

    final PDCEvidenceDetailsList evidenceList =
      pdcPerson.listEvidenceForParticipant(paramPersonKey);

    // merge each prospect evidence type
    for (final PDCEvidenceDetails evidenceDtls : evidenceList.list) {
      if (!PDCConst.PDCIDENTIFICATION.equals(evidenceDtls.evidenceType)) {
        continue;
      }
      final EIEvidenceReadDtls idReadDtls =
        readEvidence(PDCConst.PDCIDENTIFICATION, evidenceDtls.evidenceID);

      // ensure it's the sin
      if (!CONCERNROLEALTERNATEIDEntry.SOCIAL_INSURANCE_NUMBER.getCode()
        .equals(getDynEvdAttrValue(idReadDtls,
          BDMPersonMatchConstants.ALT_ID_TYPE))) {
        continue;
      }

      final Date startDate = Date.getDate(
        getDynEvdAttrValue(idReadDtls, BDMPersonMatchConstants.FROM_DATE));
      final String endDateStr =
        getDynEvdAttrValue(idReadDtls, BDMPersonMatchConstants.TO_DATE);
      final Date endDate = StringUtil.isNullOrEmpty(endDateStr)
        ? Date.kZeroDate : Date.getDate(endDateStr);
      final Date today = Date.getCurrentDate();

      // ensure it's current
      if ((startDate.before(today) || startDate.equals(today))
        && (endDate.after(today) || endDate.equals(Date.kZeroDate))) {
        return evidenceDtls.evidenceID;
      }

    }
    return null;
  }

  /**
   * This method read evidence by type and ID
   *
   * @param evidenceType
   * @param evidenceID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static EIEvidenceReadDtls readEvidence(final String evidenceType,
    final long evidenceID) throws AppException, InformationalException {

    final EvidenceControllerInterface ec =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey idEvdKey = new EIEvidenceKey();
    idEvdKey.evidenceType = evidenceType;
    idEvdKey.evidenceID = evidenceID;
    return ec.readEvidence(idEvdKey);
  }

  /**
   * This method modifies dynamic evidence
   *
   * @param evidenceID
   * @param evidenceType
   * @param dynEvdDataDtls
   * @throws AppException
   * @throws InformationalException
   */
  public static void modifyEvidence(final long evidenceID,
    final String evidenceType,
    final DynamicEvidenceDataDetails dynEvdDataDtls,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EvidenceDescriptorModifyDtls descriptorModifyDtls =
      new EvidenceDescriptorModifyDtls();
    descriptorModifyDtls.changeReason = evidenceChangeReason;
    descriptorModifyDtls.receivedDate = Date.getCurrentDate();

    final EIEvidenceModifyDtls modifyDtls = new EIEvidenceModifyDtls();
    modifyDtls.descriptor = descriptorModifyDtls;
    modifyDtls.evidenceObject = dynEvdDataDtls;

    final EIEvidenceKey modifyEvdKey = new EIEvidenceKey();
    modifyEvdKey.evidenceID = evidenceID;
    modifyEvdKey.evidenceType = evidenceType;

    evidenceControllerObj.modifyEvidence(modifyEvdKey, modifyDtls);
  }

  /**
   * This method remove dynamic evidence
   *
   * @param evidenceID
   * @param evidenceType
   * @param dynEvdDataDtls
   * @throws AppException
   * @throws InformationalException
   */
  public static void removeEvidence(final long evidenceID,
    final String evidenceType) throws AppException, InformationalException {

    final EvidenceController evidenceControllerObj =
      EvidenceControllerFactory.newInstance();

    final EvidenceControllerInterface evidenceControllerInterfaceObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceID;
    eiEvidenceKey.evidenceType = evidenceType;
    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerInterfaceObj.readEvidence(eiEvidenceKey);

    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID =
      eiEvidenceReadDtls.descriptor.evidenceDescriptorID;
    // Call controller operation to remove evidence
    evidenceControllerObj.removeEvidence(evidenceDescriptorKey);

    // Check for case type before calling apply change
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = eiEvidenceReadDtls.descriptor.caseID;
    // check for case type
    final CaseHeaderDtls caseHeaderDtls =
      curam.core.fact.CaseHeaderFactory.newInstance().read(caseHeaderKey);
    if (caseHeaderDtls.caseTypeCode
      .equals(CASETYPECODE.PARTICIPANTDATACASE)) {
      Trace.kTopLevelLogger
        .info("Apply change for the person level evidences not needed.");
    } else {
      // Task 26656 - "apply change" to the pending deletion evidence
      final curam.core.struct.CaseKey caseKey =
        new curam.core.struct.CaseKey();
      caseKey.caseID = eiEvidenceReadDtls.descriptor.caseID;
      final curam.core.sl.infrastructure.struct.ApplyChangesDetails tabbedList =
        new curam.core.sl.infrastructure.struct.ApplyChangesDetails();
      final StringBuffer removeListStr = new StringBuffer();
      removeListStr.append(eiEvidenceReadDtls.descriptor.evidenceDescriptorID)
        .append(CuramConst.gkPipeDelimiterChar).append(evidenceID)
        .append(CuramConst.gkPipeDelimiterChar)
        .append(eiEvidenceReadDtls.descriptor.evidenceType)
        .append(CuramConst.gkPipeDelimiterChar)
        .append(eiEvidenceReadDtls.descriptor.correctionSetID)
        .append(CuramConst.gkPipeDelimiterChar)
        .append(eiEvidenceReadDtls.descriptor.successionID);
      tabbedList.removeList = removeListStr.toString();
      evidenceControllerObj.applyChanges(caseKey, tabbedList);
    }
  }

  /**
   * This method will remove a dynamic evidence
   *
   * @param evidenceID
   * @param evidenceType
   * @throws AppException
   * @throws InformationalException
   */
  public static void removeDynamicEvidence(final long evidenceID,
    final String evidenceType) throws AppException, InformationalException {

    final EvidenceController evidenceControllerObj =
      EvidenceControllerFactory.newInstance();
    final EvidenceControllerInterface evidenceControllerInterfaceObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceID;
    eiEvidenceKey.evidenceType = evidenceType;
    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerInterfaceObj.readEvidence(eiEvidenceKey);
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID =
      eiEvidenceReadDtls.descriptor.evidenceDescriptorID;
    // Call controller operation to remove evidence
    evidenceControllerObj.removeEvidence(evidenceDescriptorKey);
    final EvidenceKey evidenceKey = new EvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = evidenceType;
    evidenceKey.evidenceDescriptorID =
      evidenceDescriptorKey.evidenceDescriptorID;
    evidenceKey.successionID = eiEvidenceReadDtls.descriptor.successionID;
    evidenceKey.correctionSetID =
      eiEvidenceReadDtls.descriptor.correctionSetID;
    // Call controller operation to apply pending evidence removal
    evidenceControllerObj.applyRemoval(evidenceKey);
  }

  /**
   * This method returns the value of a the given dynamic evidence attribute
   *
   * @param evidenceID
   * @param evidenceType
   * @param attrName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getDynEvdAttrValue(final Long evidenceID,
    final String evidenceType, final String attrName)
    throws AppException, InformationalException {

    final EIEvidenceReadDtls idReadDtls =
      readEvidence(evidenceType, evidenceID);
    return getDynEvdAttrValue(idReadDtls, attrName);
  }

  /**
   * This method returns the value of a the given dynamic evidence attribute
   *
   * @param evdDtls
   * @param attrName
   * @return
   */
  public static String getDynEvdAttrValue(final EIEvidenceReadDtls evdDtls,
    final String attrName) {

    final DynamicEvidenceDataDetails dynEvdDataDtls =
      (DynamicEvidenceDataDetails) evdDtls.evidenceObject;
    final DynamicEvidenceDataAttributeDetails attr =
      dynEvdDataDtls.getAttribute(attrName);
    if (attr == null) {
      Trace.kTopLevelLogger.warn("attribute " + attrName
        + " could not be found for evidenceDescriptorID="
        + evdDtls.descriptor.evidenceDescriptorID);
      return CuramConst.gkEmpty;
    }
    return attr.getValue();
  }

  /**
   * Returns the details for a given evidence item including its attribute
   * values.
   *
   * @param evidenceDescriptorDtls
   * The details necessary to uniquely identify an
   * evidence item.
   * @return ReadEvidenceDetails The details for a given evidence item including
   * its attribute values.
   * @throws AppException
   * Generic exception signature.
   * @throws InformationalException
   * Generic exception signature.
   */
  public static ReadEvidenceDetails readDynamicEvidenceDetails(
    final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    ReadEvidenceDetails dynamicEvidenceDetails = null;

    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(evidenceDescriptorDtls.relatedID);

    if (null != evidenceServiceInterface) {
      final EvidenceCaseKey key = new EvidenceCaseKey();
      key.caseIDKey = new curam.core.sl.struct.CaseIDKey();
      key.caseIDKey.caseID = evidenceDescriptorDtls.caseID;
      key.evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
      key.evidenceKey.evType = evidenceDescriptorDtls.evidenceType;

      dynamicEvidenceDetails = evidenceServiceInterface.readEvidence(key);
    }

    return dynamicEvidenceDetails;
  }

  /**
   * Get latest active evidence details list for a given caseID and
   * evidenceType.
   *
   * @param concernRoleID
   * @param evidenceType
   * @return GenericSLDataDetails List
   * @throws AppException
   * @throws InformationalException
   */
  public static List<GenericSLDataDetails>
    getEvidenceDetailsListForCaseIDAndType(final long caseID,
      final String evidenceType) throws AppException, InformationalException {

    // init return
    final List<GenericSLDataDetails> list =
      new ArrayList<GenericSLDataDetails>();

    // set search key
    final CaseKey key = new CaseKey();
    key.caseID = caseID;

    // get evidence list
    final EvidenceController evidenceControllerObj =
      EvidenceControllerFactory.newInstance();
    final ECActiveEvidenceDtlsList dtlsList =
      evidenceControllerObj.listActive(key);

    // traverse results looking for evidence type matches and add them to return
    // list
    for (final ECActiveEvidenceDtls dtls : dtlsList.dtls) {
      if (dtls.evidenceType.equals(evidenceType)) {
        final EIEvidenceKey evKey = new EIEvidenceKey();
        evKey.evidenceID = dtls.evidenceID;
        evKey.evidenceType = evidenceType;
        list.add(getGenericSLDataDetails(evKey));
      }
    }

    return list;
  }

  /**
   *
   * PR-3503 - JM - This method get evidence descriptor key of the return
   * evidence details
   *
   * @param returnEvdDtls
   * The return evidence details to be obtained the key
   *
   * @throws AppException
   * - Generic Curam Exception
   * @throws InformationalException
   * - Generic Curam Exception
   */
  public static EvidenceDescriptorKey
    getEvidenceDescriptorKeyForReturnEvidenceDetails(
      final ReturnEvidenceDetails returnEvdDtls)
      throws AppException, InformationalException {

    final EvidenceDescriptorKey evidenceDesciptorKey =
      new EvidenceDescriptorKey();

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = returnEvdDtls.evidenceKey.evidenceID;
    eiEvidenceKey.evidenceType = returnEvdDtls.evidenceKey.evType;
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    evidenceDesciptorKey.evidenceDescriptorID =
      eiEvidenceReadDtls.descriptor.evidenceDescriptorID;

    return evidenceDesciptorKey;
  }

  /**
   * This method gets the start and end date of the evidence
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Date
    getEndDateForEvidenceRecord(final EIEvidenceKey eiEvidenceKey)
      throws AppException, InformationalException {

    Date endDate = new Date();
    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
    evidenceTypeKey.evidenceType = eiEvidenceKey.evidenceType;
    final EvidenceMap map = getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(evidenceTypeKey.evidenceType);
    endDate = standardEvidenceInterface.getEndDate(eiEvidenceKey);
    return endDate;
  }

  public static EvidenceMap getEvidenceMap() {

    return GuiceWrapper.getInjector().getInstance(EvidenceMap.class);
  }

  /**
   * Utility method to return list of all Active and InEdit evidences of a given
   * type for an IC.
   *
   * @param integratedCaseID
   * The case ID for the IC.
   * @param evidenceType
   * The type of static evidence to look up.
   * @return A list containing the evidenceDescriptor record for each evidence
   * instance found.
   * @throws AppException
   * OOTB Cúram exception
   * @throws InformationalException
   * OOTB Cúram exception
   */
  public static EvidenceDescriptorDtlsList
    getActiveInEditEvidenceListByICIDAndType(final long integratedCaseID,
      final String evidenceType) throws AppException, InformationalException {

    final EvidenceTypeCaseIDStatusesKey evidenceTypeCaseIDStatusesKey =
      new EvidenceTypeCaseIDStatusesKey();
    evidenceTypeCaseIDStatusesKey.caseID = integratedCaseID;
    evidenceTypeCaseIDStatusesKey.evidenceType = evidenceType;
    evidenceTypeCaseIDStatusesKey.statusCode1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    evidenceTypeCaseIDStatusesKey.statusCode1 =
      EVIDENCEDESCRIPTORSTATUS.INEDIT;

    return EvidenceDescriptorFactory.newInstance()
      .searchByCaseIDEvidenceTypeAndStatus(evidenceTypeCaseIDStatusesKey);

  }

  /**
   * PR-11196 - RV - This method is added part of Online CoC implementation.
   * Add Phone number by phone type (Mobile)
   *
   * @param participantRoleID
   * @param phoneCode
   * @param phoneNumber
   * @param phoneType
   * @throws AppException
   * @throws InformationalException
   */
  public static void createPhoneNumberEvidence(final long participantRoleID,
    final String phoneCode, final String phoneNumber,
    final PHONETYPEEntry phoneType)
    throws AppException, InformationalException {

    final PhoneNumberMappingBean phoneNumberVO =
      new PhoneNumberMappingBean(phoneNumber, phoneCode, phoneType);

    final MaintainPhoneNumber maintainPhoneNumberObj =
      MaintainPhoneNumberFactory.newInstance();

    final PhoneNumberDetails phoneNumberDetails = new PhoneNumberDetails();

    phoneNumberDetails.phoneAreaCode = phoneNumberVO.getAreaCode();
    phoneNumberDetails.phoneNumber = phoneNumberVO.getPhoneNumber();
    phoneNumberDetails.comments = "";

    maintainPhoneNumberObj.createPhoneNumber(phoneNumberDetails);

    final long phoneNumberID = phoneNumberDetails.phoneNumberID;

    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      new ConcernRolePhoneNumberDtls();

    concernRolePhoneNumberDtls.concernRoleID = participantRoleID;
    concernRolePhoneNumberDtls.phoneNumberID = phoneNumberID;
    concernRolePhoneNumberDtls.startDate = Date.getCurrentDate();
    concernRolePhoneNumberDtls.endDate = Date.kZeroDate;
    concernRolePhoneNumberDtls.typeCode = phoneType.getCode();

    final ParticipantPhoneDetails participantPhoneDetails =
      new ParticipantPhoneDetails();

    participantPhoneDetails.assign(concernRolePhoneNumberDtls);
    participantPhoneDetails.phoneAreaCode = phoneNumberVO.getAreaCode();
    participantPhoneDetails.phoneNumber = phoneNumberVO.getPhoneNumber();

    final PDCPhoneNumber pdcPhoneNumber = PDCPhoneNumberFactory.newInstance();

    pdcPhoneNumber.insert(participantPhoneDetails);

  }

  /**
   * PR-11196 - RV - This method is added part of Online CoC implementation.
   * This method makes an entry for new email address for given concern role id.
   *
   * @param emailAddressID - holds the email Address ID value.
   * @param concernRoleID - holds concern role id information.
   * @throws AppException, InformationalException
   */
  public static ConcernRoleEmailAddressKey createEmailAddressEvidence(
    final long concernRoleID, final String emailAddress,
    final String emailType) throws AppException, InformationalException {

    // Instantiate objects.
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final ParticipantEmailAddressDetails addressDetails =
      new ParticipantEmailAddressDetails();

    // Set values for email address dtls values.
    addressDetails.concernRoleEmailAddressID = uniqueIDObj.getNextID();
    addressDetails.concernRoleID = concernRoleID;
    addressDetails.emailAddress = emailAddress;
    addressDetails.startDate = Date.getCurrentDate();
    addressDetails.statusCode = RECORDSTATUSEntry.DEFAULT().getCode();
    addressDetails.typeCode = emailType;
    addressDetails.versionNo = 1;

    // Make an entry into email address table.
    final ConcernRoleEmailAddressKey addressKey =
      PDCEmailAddressFactory.newInstance().insert(addressDetails);

    // Return Concern Role Email Address Key value.
    return addressKey;
  }

  /**
   * PR-11196 - RV - This method is added part of Online CoC implementation.
   * This method fetches Dynamic Evidence Data details.
   *
   * @param caseID
   * @param relatedID
   * @param evidenceType
   * @return DynamicEvidenceDataDetails - Dynamic Evidence Data details.
   * @throws AppException
   * @throws InformationalException
   */
  public static DynamicEvidenceDataDetails getDynamicEvidenceDataInfo(
    final long caseID, final long relatedID, final String evidenceType)
    throws AppException, InformationalException {

    // Get Evidence interface object.
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(Long.valueOf(relatedID));

    // Instantiate object and assign values to key struct.
    final EvidenceCaseKey key = new EvidenceCaseKey();
    key.caseIDKey.caseID = caseID;
    key.evidenceKey.evidenceID = relatedID;
    key.evidenceKey.evType = evidenceType;

    // Read Evidence
    final ReadEvidenceDetails readEvidenceDetails =
      evidenceServiceInterface.readEvidence(key);

    // Get DynamicEvidence data details.
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      readEvidenceDetails.dtls;

    // Return DynamicEvidence data details.
    return dynamicEvidenceDataDetails;
  }

  /**
   * map and create dependant evidence for all the dependants
   *
   * @since ADO-16437
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapDependantEvidence(final Entity[] personEntites,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings)
    throws AppException, InformationalException {

    for (int i = 0; i < personEntites.length; i++) {

      // if its primary skip as we dont need to create dependant evidence for
      // primary
      if (personEntites[i].hasAttribute("isPrimaryParticipant")
        && personEntites[i].getTypedAttribute("isPrimaryParticipant")
          .equals(Boolean.TRUE)) {

        continue;

      }

      // call helper method to create dependant evidecne
      createDependantEvidence(personEntites[i], caseID, dataStoreCEFMappings);
    }
  }

  /**
   * Util class to create Dependant evidence
   *
   * @since ADO-16437
   * @param personEntity
   * @param caseID
   * @param dataStoreCEFMappings
   */
  private void createDependantEvidence(final Entity personEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings)
    throws AppException, InformationalException {

    final curam.core.sl.struct.EvidenceTypeKey eType =
      new curam.core.sl.struct.EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMDEPENDANT;

    final long dependantConcernroleID = dataStoreCEFMappings
      .getMappedProspectPerson(personEntity.getUniqueID()).getConcernRoleID();
    final long primaryConcernroleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();

    final Boolean isDependantPerson =
      concernroleDAO.get(dependantConcernroleID).getConcernRoleType()
        .toString().equals(CONCERNROLETYPE.PERSON);

    // get caseParticipantRoleID of the person
    final long caseParticipantRoleid =
      bdmUtil.getCaseParticipantRoleID(caseID,
        primaryConcernroleID).caseParticipantRoleID;

    // get caseParticipantROleID of the person
    final long dependantCaseParticipantRoleid =
      bdmUtil.getCaseParticipantRoleID(caseID,
        dependantConcernroleID).caseParticipantRoleID;

    // get Latest Version of Dependant Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("caseParticipant"),
      caseParticipantRoleid);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("startDate"),
      Date.fromISO8601(personEntity
        .getAttribute(BDMDatastoreConstants.START_LIVING_SAME_RES_DATE)));

    final Boolean isClaimed =
      isDependantPerson && bdmUtil.isDependantClaimedOnAnotherCase(caseID,
        dependantConcernroleID, Date.getCurrentDate(), null) ? false : true;

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("claimDependant"), isClaimed);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("dependentCaseParticipant"),
      dependantCaseParticipantRoleid);

    // BEGIN - User Story 21834 - Added Attestation
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("comments"),
      BDMConstants.kAttestationComments);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("attestationDate"),
      Date.getCurrentDate());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("bdmAgreeAttestation"),
      new CodeTableItem(BDMAGREEATTESTATION.TABLENAME,
        BDMAGREEATTESTATION.YES));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("attesteeCaseParticipant"),
      caseParticipantRoleid);
    // END - User Story 21834 - Added Attestation

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = primaryConcernroleID;
    descriptor.changeReason = EVIDENCECHANGEREASON.REPORTEDBYCLIENT;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(caseParticipantRoleid);
    genericDtls.addRelCp("caseParticipant", cpAdaptor);
    evidenceServiceInterface.createEvidence(genericDtls);
  }

  /**
   * Util for Relationship evidence mapping
   *
   * @since ADO-21128
   * @param intakeApplication
   */
  public void mapRelationshipEvidence(final Entity applicationEntity,
    final DataStoreCEFMappings dataStoreCEFMappings, final String schemaName)
    throws InformationalException, AppException {

    // Create the new relationship evidence
    try {
      Datastore dataStore;
      dataStore = DatastoreHelper.openDatastore(schemaName);
      final IntakeApplicationMappingBean applicationData =
        IntakeApplicationMappingBean.getIntakeApplicationMappingBean(
          dataStore, applicationEntity.getUniqueID());
      // get all new relationship details
      final Iterator<RelationshipMappingBean> relationshipIterator =
        applicationData.getRelationships().iterator();
      while (relationshipIterator.hasNext()) {
        final RelationshipMappingBean relationshipVO =
          relationshipIterator.next();
        final PDCRelationships pdcRelationShips =
          PDCRelationshipsFactory.newInstance();
        final ParticipantRelationshipDetails newParticipantRelationshipDetails =
          new ParticipantRelationshipDetails();
        final ParticipantRelationshipDetails modifiedParticipantRelationshipDetails =
          new ParticipantRelationshipDetails();
        // Get concernroles for the persons to create the relationship for
        final ConcernRole mainConcernRole =
          concernroleDAO.get(dataStoreCEFMappings
            .getMappedProspectPerson(relationshipVO.getRelatedPersonID())
            .getConcernRoleID());
        final ConcernRole relConcernRole =
          concernroleDAO.get(dataStoreCEFMappings
            .getMappedProspectPerson(relationshipVO.getPersonID())
            .getConcernRoleID());

        final List<ConcernRoleRelationship> concernRoleRelationshipList =
          concernRoleRelationshipDAO.listActiveByConcernRole(mainConcernRole);
        concernRoleRelationshipList
          .removeIf(concernRoleRelationship -> concernRoleRelationship
            .getDateRange().isEnded());
        final Iterator<ConcernRoleRelationship> concernRoleRelationshipIterator =
          concernRoleRelationshipList.iterator();
        if (!concernRoleRelationshipIterator.hasNext()) {
          newParticipantRelationshipDetails.relationshipType =
            relationshipVO.getRelationshipType().getCode();
          newParticipantRelationshipDetails.concernRoleID =
            mainConcernRole.getID();
          newParticipantRelationshipDetails.relConcernRoleID =
            relConcernRole.getID();

          // BEGIN Task-61494 fix relationship start date
          // get person relationship start date from DS
          final Entity[] personEntites = applicationEntity
            .getChildEntities(dataStore.getEntityType("Person"));
          Date startDate = Date.getCurrentDate();
          for (int i = 0; i < personEntites.length; i++) {
            // if its primary then skip
            if (personEntites[i].hasAttribute("isPrimaryParticipant")
              && personEntites[i].getTypedAttribute("isPrimaryParticipant")
                .equals(Boolean.TRUE)) {
              continue;
            }
            final long dependantConcernroleID = dataStoreCEFMappings
              .getMappedProspectPerson(personEntites[i].getUniqueID())
              .getConcernRoleID();
            // if same related participant
            if (newParticipantRelationshipDetails.concernRoleID == dependantConcernroleID) {
              startDate = Date.fromISO8601(personEntites[i].getAttribute(
                BDMDatastoreConstants.START_LIVING_SAME_RES_DATE));
              break;
            }
          }
          // assign date from DS
          newParticipantRelationshipDetails.startDate = startDate;
          // END Task-61494
          pdcRelationShips.insert(newParticipantRelationshipDetails);
        }
        while (concernRoleRelationshipIterator.hasNext()) {
          final ConcernRoleRelationship concernRoleRelationship =
            concernRoleRelationshipIterator.next();
          // BEGIN Task-61494 fix relationship start date
          // get person relationship start date from DS
          final Entity[] personEntites = applicationEntity
            .getChildEntities(dataStore.getEntityType("Person"));
          Date startDate = Date.getCurrentDate();
          for (int i = 0; i < personEntites.length; i++) {
            // if its primary then skip
            if (personEntites[i].hasAttribute("isPrimaryParticipant")
              && personEntites[i].getTypedAttribute("isPrimaryParticipant")
                .equals(Boolean.TRUE)) {
              continue;
            }
            final long dependantConcernroleID = dataStoreCEFMappings
              .getMappedProspectPerson(personEntites[i].getUniqueID())
              .getConcernRoleID();
            // if same related participant
            if (newParticipantRelationshipDetails.concernRoleID == dependantConcernroleID) {
              startDate = Date.fromISO8601(personEntites[i].getAttribute(
                BDMDatastoreConstants.START_LIVING_SAME_RES_DATE));
              break;
            }
          }

          if (concernRoleRelationship.getRelatedConcernRole()
            .equals(relConcernRole)) {
            final Date currentRelationshipStartDate =
              concernRoleRelationship.getDateRange().start();
            // if there is already an active relationship then
            if (currentRelationshipStartDate.equals(startDate)) {
              // If evidence began today, then only modify existing
              modifiedParticipantRelationshipDetails.concernRoleRelationshipID =
                concernRoleRelationship.getID();
              modifiedParticipantRelationshipDetails.concernRoleID =
                mainConcernRole.getID();
              modifiedParticipantRelationshipDetails.relConcernRoleID =
                relConcernRole.getID();
              modifiedParticipantRelationshipDetails.relationshipType =
                relationshipVO.getRelationshipType().getCode();
              modifiedParticipantRelationshipDetails.startDate =
                currentRelationshipStartDate;
              pdcRelationShips.modify(modifiedParticipantRelationshipDetails);
              break;
            } else if (currentRelationshipStartDate.before(startDate)) {
              // Evidence before today, end date previous evidence day before
              // new starts
              modifiedParticipantRelationshipDetails.concernRoleRelationshipID =
                concernRoleRelationship.getID();
              modifiedParticipantRelationshipDetails.relationshipType =
                concernRoleRelationship.getRelationshipType().getCode();
              modifiedParticipantRelationshipDetails.concernRoleID =
                mainConcernRole.getID();
              modifiedParticipantRelationshipDetails.relConcernRoleID =
                relConcernRole.getID();
              modifiedParticipantRelationshipDetails.startDate =
                currentRelationshipStartDate;
              modifiedParticipantRelationshipDetails.endDate =
                Date.getCurrentDate().addDays(-1);
              modifiedParticipantRelationshipDetails.statusCode = "";
              pdcRelationShips.modify(modifiedParticipantRelationshipDetails);
              // then add pdcRelationShips.insert for the new
              newParticipantRelationshipDetails.relationshipType =
                relationshipVO.getRelationshipType().getCode();
              newParticipantRelationshipDetails.concernRoleID =
                mainConcernRole.getID();
              newParticipantRelationshipDetails.relConcernRoleID =
                relConcernRole.getID();
              newParticipantRelationshipDetails.startDate = startDate;
              pdcRelationShips.insert(newParticipantRelationshipDetails);
              break;
            } else if (currentRelationshipStartDate.after(startDate)) {
              // Evidence after today, end date new evidence day before current
              // starts then add pdcRelationShips.insert for the new
              newParticipantRelationshipDetails.relationshipType =
                relationshipVO.getRelationshipType().getCode();
              newParticipantRelationshipDetails.concernRoleID =
                mainConcernRole.getID();
              newParticipantRelationshipDetails.relConcernRoleID =
                relConcernRole.getID();
              newParticipantRelationshipDetails.startDate = startDate;
              newParticipantRelationshipDetails.endDate =
                currentRelationshipStartDate.addDays(-1);
              pdcRelationShips.insert(newParticipantRelationshipDetails);
              break;
            }
          }
        }
      }
    } catch (final NoSuchAttributeException e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "Error while mapping pdc relationships");
    }
  }

  // BEGIN TASK 16973 - Bank account evidence
  /**
   * Returns true if the specified list of attributes contains an attribute
   * with the specified attributeName
   *
   * @param attributeName, {@link String}.
   * @param attributes, a {@link List}
   * @return a boolean.
   */
  public static <T extends Attribute> boolean
    hasAttribute(final String attributeName, final List<T> attributes) {

    if (attributes != null) {
      return attributes.stream()
        .filter((final Attribute attribute) -> attribute != null)
        .anyMatch((final Attribute attribute) -> attributeName
          .equals(attribute.getName()));
    }
    return false;
  }

  /**
   * Returns true if these conditions hold.
   * <ul>
   * <li>The {@link DynamicEvidenceDataDetails} object is not null.</li>
   * <li>Both the associated Evidence Type and Evidence Type Version can be
   * retrieved from the {@link DynamicEvidenceDataDetails} and have non-null
   * values.</li>
   * <li>The associated Evidence Type has a type code of
   * {@link PDCConst.PDCBANKACCOUNT}</li>
   * <li>The ETV declares "financial institution number" and "bank transit
   * number" data attributes.</li>
   * </li>
   *
   * @param dynamicEvidenceDataDetails evidence data details
   * @return true if the above conditions are all true, false otherwise.
   */
  public static boolean hasBDMBankAccountCustomizations(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails) {

    if (dynamicEvidenceDataDetails != null) {
      final EvidenceTypeVersionDef evidenceTypeVersionDef =
        dynamicEvidenceDataDetails.getEvidenceTypeVersionDefinition();

      if (evidenceTypeVersionDef != null) {
        final List<DataAttribute> dataAttributes =
          evidenceTypeVersionDef.getDataAttributes();
        final boolean isFinDataAttributeDefined =
          hasAttribute(kFINANCIAL_INSTITUTION_NUMBER, dataAttributes);
        final boolean isTransitDataAttributeDefined =
          hasAttribute(kBRANCH_TRANSIT_NUMBER, dataAttributes);
        final boolean isSortCodeDataAttributeDefined =
          hasAttribute(kSORT_CODE, dataAttributes);

        final EvidenceTypeDef evidenceTypeDef =
          evidenceTypeVersionDef.getEvidenceTypeDef();
        return evidenceTypeDef != null
          && PDCConst.PDCBANKACCOUNT
            .equals(evidenceTypeDef.getEvidenceTypeCode())
          && isFinDataAttributeDefined && isTransitDataAttributeDefined
          && isSortCodeDataAttributeDefined;
      }
    }
    return false;
  }

  /**
   * Sets the attribute with the specified name with the specified value.
   * This methods should only be called if it is known that associated ETV
   * is known to declare the specified attribute.
   *
   * @param attributeName data attribute name
   * @param attributeValue data attribute value
   * @param dynamicEvidenceDataDetails evidence data details
   */
  public static void setDataAttributeNoCheck(final String attributeName,
    final String attributeValue,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails) {

    dynamicEvidenceDataDetails.getAttribute(attributeName)
      .setValue(attributeValue);
  }

  /**
   * Sets the attribute with the specified name with the specified value.
   * This methods will only set the specified attribute if it is declared in the
   * associated ETV.
   *
   * @param attributeName
   * @param attributeValue
   * @param dynamicEvidenceDataDetails
   */
  public static void setDataAttribute(final String attributeName,
    final String attributeValue,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails) {

    if (dynamicEvidenceDataDetails != null) {
      final EvidenceTypeVersionDef evidenceTypeVersionDef =
        dynamicEvidenceDataDetails.getEvidenceTypeVersionDefinition();
      if (evidenceTypeVersionDef != null && hasAttribute(attributeName,
        evidenceTypeVersionDef.getDataAttributes())) {
        setDataAttributeNoCheck(attributeName, attributeValue,
          dynamicEvidenceDataDetails);
      }
    }
  }

  /**
   * Returns true if these conditions hold.
   * <ul>
   * <li>The {@link DynamicEvidenceDataDetails} object is not null.</li>
   * <li>Both the associated Evidence Type and Evidence Type Version can be
   * retrieved from the {@link DynamicEvidenceDataDetails} and have non-null
   * values.</li>
   * <li>The associated Evidence Type has a type code of
   * {@link PDCConst.PDCBANKACCOUNT}</li>
   * <li>The ETV declares "programType" data attributes.</li>
   * </li>
   *
   * @param dynamicEvidenceDataDetails evidence data details
   * @return true if the above conditions are all true, false otherwise.
   */
  public static boolean hasBDMBankAccountProgramCustomizations(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails) {

    if (dynamicEvidenceDataDetails != null) {
      final EvidenceTypeVersionDef evidenceTypeVersionDef =
        dynamicEvidenceDataDetails.getEvidenceTypeVersionDefinition();

      if (evidenceTypeVersionDef != null) {
        final List<DataAttribute> dataAttributes =
          evidenceTypeVersionDef.getDataAttributes();

        final boolean isProgramTypeAttributeDefined =
          hasAttribute(kPROGRAM, dataAttributes);

        final EvidenceTypeDef evidenceTypeDef =
          evidenceTypeVersionDef.getEvidenceTypeDef();

        return evidenceTypeDef != null
          && PDCConst.PDCBANKACCOUNT
            .equals(evidenceTypeDef.getEvidenceTypeCode())
          && isProgramTypeAttributeDefined;
      }
    }
    return false;
  }
  // END TASK 16973 - Bank account evidence

  // BEGIN TASK 12976
  /**
   * Sets the bank account evidence sort code attribute value concatenating the
   * financial institution number and bank transit number.
   *
   * @param dynamicEvidenceDataDetails evidence data attributes
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static void updateDynamicEvidenceDataDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException, InformationalException {

    if (hasBDMBankAccountCustomizations(dynamicEvidenceDataDetails)) {

      final DynamicEvidenceDataAttributeDetails financialInstitutionNumber =
        dynamicEvidenceDataDetails
          .getAttribute(kFINANCIAL_INSTITUTION_NUMBER);
      final DynamicEvidenceDataAttributeDetails bankTransitNumber =
        dynamicEvidenceDataDetails.getAttribute(kBRANCH_TRANSIT_NUMBER);

      String finValue = null;
      String transitValue = null;

      if (financialInstitutionNumber != null
        && (finValue = financialInstitutionNumber.getValue()) != null
        && !finValue.trim().isEmpty() && bankTransitNumber != null
        && (transitValue = bankTransitNumber.getValue()) != null
        && !transitValue.trim().isEmpty()) {

        dynamicEvidenceDataDetails.getAttribute(kSORT_CODE)
          .setValue(finValue + transitValue);
      }
    }
  }

  // END TASK 12976
  /**
   * call OOTB PDC Address evidence creation logic by setting all teh values
   * properly
   *
   * @param concernRoleID
   * @param addressData
   * @param startDate
   * @param typeCode
   * @param endDate
   * @throws AppException
   * @throws InformationalException
   */
  public void createAddressEvidence(final long concernRoleID,
    final String addressData, final Date startDate, final String typeCode,
    final Date endDate, final Boolean isPreferredAddress,
    final String comments) throws AppException, InformationalException {

    final PDCAddress pdcAddress = PDCAddressFactory.newInstance();
    final ParticipantAddressDetails participantAddressDetails =
      new ParticipantAddressDetails();
    // 96244 294-01b Ability to store the addresses in Uppercase
    participantAddressDetails.addressData = addressData.toUpperCase();
    participantAddressDetails.concernRoleID = concernRoleID;
    participantAddressDetails.startDate = startDate;
    participantAddressDetails.typeCode = typeCode;
    participantAddressDetails.endDate = endDate;
    participantAddressDetails.statusCode = RECORDSTATUS.NORMAL;
    participantAddressDetails.comments = comments;
    participantAddressDetails.primaryAddressInd = isPreferredAddress;

    pdcAddress.insert(participantAddressDetails);
  }

  /**
   *
   *
   * @param concernRoleID
   * @param addressData
   * @param startDate
   * @param typeCode
   * @param endDate
   * @throws AppException
   * @throws InformationalException
   */
  public void modifyAddressEvidence(final long concernRoleID,
    final String addressData, final Date startDate, final String typeCode,
    final Date endDate, final Boolean isPreferredAddress,
    final String comments, final long evidenceID, final String changeReason,
    final String bdmReceivedFrom, final String bdmReceivedFromCountry,
    final String bdmModeOfReceipt, final String country)
    throws AppException, InformationalException {

    final curam.core.sl.struct.EvidenceTypeKey eType =
      new curam.core.sl.struct.EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCADDRESS;

    final Address addressObj = AddressFactory.newInstance();

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceID;
    eiEvidenceKey.evidenceType = PDCConst.PDCADDRESS;

    final EIEvidenceKey eiModifyEvidenceKey = new EIEvidenceKey();
    final EIEvidenceModifyDtls eiEvidenceModifyDtls =
      new EIEvidenceModifyDtls();
    EvidenceDescriptorModifyDtls evidenceDescriptorModifyDtls =
      new EvidenceDescriptorModifyDtls();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final AddressKey addressKey = new AddressKey();

    addressKey.addressID = Long.valueOf(dynamicEvidenceDataDetails
      .getAttribute(BDMConstants.kEvidenceAttrAddressID).getValue());

    final OtherAddressData otherAddressDataObj = new OtherAddressData();
    otherAddressDataObj.addressData = addressObj.read(addressKey).addressData;
    final String existingCountry = bdmUtil.getAddressDetails(
      otherAddressDataObj, BDMConstants.kADDRESSELEMENT_COUNTRY);

    final Long caseID = evidenceReadDtls.descriptor.caseID;

    eiModifyEvidenceKey.evidenceID = evidenceID;
    eiModifyEvidenceKey.evidenceType = PDCConst.PDCADDRESS;
    evidenceDescriptorModifyDtls = new EvidenceDescriptorModifyDtls();
    evidenceDescriptorModifyDtls.receivedDate = Date.getCurrentDate();
    if (StringUtil.isNullOrEmpty(changeReason)) {
      evidenceDescriptorModifyDtls.changeReason =
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    } else {
      evidenceDescriptorModifyDtls.changeReason = changeReason;
    }

    // eiEvidenceModifyDtls.descriptor.effectiveFrom = Date.getCurrentDate();
    // Concert Address Data to Upper Case

    final AddressDtls newAddressDtls = new AddressDtls();
    // 96244 294-01b Ability to store the addresses in Uppercase
    newAddressDtls.addressData = addressData.toUpperCase();
    addressObj.insert(newAddressDtls);
    final AddressKey modifyAddressKey = new AddressKey();
    modifyAddressKey.addressID = newAddressDtls.addressID;
    addressObj.modify(modifyAddressKey, newAddressDtls);
    final DynamicEvidenceDataAttributeDetails address =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kEvidenceAttrAddressID);
    DynamicEvidenceTypeConverter.setAttribute(address,
      Long.valueOf(newAddressDtls.addressID));
    assignEvidenceDetails(concernRoleID, caseID, startDate, typeCode, endDate,
      comments, isPreferredAddress, dynamicEvidenceDataDetails,
      bdmReceivedFrom, bdmReceivedFromCountry, bdmModeOfReceipt);
    eiEvidenceModifyDtls.descriptor.assign(evidenceDescriptorModifyDtls);
    eiEvidenceModifyDtls.evidenceObject = dynamicEvidenceDataDetails;

    if (evidenceID != 0L) {
      evidenceControllerObj.modifyEvidence(eiModifyEvidenceKey,
        eiEvidenceModifyDtls);
    }

    // 101950 Updated: 247.3-202 Trigger task to update residency period
    if (!country.equals(CuramConst.gkEmpty)
      && !country.equalsIgnoreCase(existingCountry)
      && typeCode.equals(CONCERNROLEADDRESSTYPE.PRIVATE)
      && (existingCountry.equals(BDMConstants.kCANADA_ADDRESSCCOUNTRY_Code)
        || country.equals(BDMConstants.kCANADA_ADDRESSCCOUNTRY_Code))) {
      createResidencePeriodUpdateTask(concernRoleID);
    }
  }

  /**
   *
   * @param evidenceID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long getCaseParticipantRoleIDFromEvdID(final long evidenceID)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceID;
    eiEvidenceKey.evidenceType = PDCConst.PDCADDRESS;

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    return Long.valueOf(
      dynamicEvidenceDataDetails.getAttribute("participant").getValue());

  }

  /**
   * BEGIN TASK-28446 Invalid Address Type On Evidence Edit Screen
   *
   * @param evidenceID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getExistingAddress(final long evidenceID,
    final BDMAddressEvidenceDtls bdmAddressEvidenceDtls)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceID;
    eiEvidenceKey.evidenceType = PDCConst.PDCADDRESS;

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Address addressObj = AddressFactory.newInstance();
    final AddressKey addressKey = new AddressKey();

    addressKey.addressID = Long
      .valueOf(dynamicEvidenceDataDetails.getAttribute("address").getValue());
    // BEGIN TASK-28446 Invalid Address Type On Evidence Edit Screen
    final String addressType =
      dynamicEvidenceDataDetails.getAttribute("addressType").getValue();
    bdmAddressEvidenceDtls.addressType = addressType;
    // END TASK-28446
    final OtherAddressData otherAddressDataObj = new OtherAddressData();
    otherAddressDataObj.addressData = addressObj.read(addressKey).addressData;

    return addressObj.getShortFormat(otherAddressDataObj).addressData;

  }

  /**
   *
   * @param evidenceID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public OtherAddressData getExistingOtherAddressData(final long evidenceID)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceID;
    eiEvidenceKey.evidenceType = PDCConst.PDCADDRESS;

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Address addressObj = AddressFactory.newInstance();
    final AddressKey addressKey = new AddressKey();

    addressKey.addressID = Long
      .valueOf(dynamicEvidenceDataDetails.getAttribute("address").getValue());

    final OtherAddressData otherAddressDataObj = new OtherAddressData();
    otherAddressDataObj.addressData = addressObj.read(addressKey).addressData;

    return otherAddressDataObj;
  }

  /**
   *
   * @param pdcCaseIDCaseParticipantRoleID
   * @param details
   * @param dynamicEvidenceDataDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void assignEvidenceDetails(final long concernRoleID,
    final long caseID, final Date startDate, final String typeCode,
    final Date endDate, final String comments,
    final Boolean isPreferredAddress,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String bdmReceivedFrom, final String bdmReceivedFromCountry,
    final String bdmModeOfReceipt)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute("fromDate");
    if (!startDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(fromDate, startDate);
    } else {
      fromDate.setValue("");
    }
    final DynamicEvidenceDataAttributeDetails toDate =
      dynamicEvidenceDataDetails.getAttribute("toDate");
    if (!endDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(toDate, endDate);
    } else {
      toDate.setValue("");
    }

    final DynamicEvidenceDataAttributeDetails addressType =
      dynamicEvidenceDataDetails.getAttribute("addressType");
    DynamicEvidenceTypeConverter.setAttribute(addressType,
      new CodeTableItem(CONCERNROLEADDRESSTYPE.TABLENAME, typeCode));
    final DynamicEvidenceDataAttributeDetails commentsObj =
      dynamicEvidenceDataDetails.getAttribute("comments");
    DynamicEvidenceTypeConverter.setAttribute(commentsObj, comments);
    final DynamicEvidenceDataAttributeDetails preferredInd =
      dynamicEvidenceDataDetails.getAttribute("preferredInd");
    DynamicEvidenceTypeConverter.setAttribute(preferredInd,
      Boolean.valueOf(false));
    if (isPreferredAddress) {
      DynamicEvidenceTypeConverter.setAttribute(preferredInd,
        Boolean.valueOf(true));
    }

    // START: Bug 111293: Address evidence details are not saved after the edit
    final DynamicEvidenceDataAttributeDetails bdmReceivedFromAttrName =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kBDMRECEIVEDFROM_ATTR);

    DynamicEvidenceTypeConverter.setAttribute(bdmReceivedFromAttrName,
      new CodeTableItem(BDMRECEIVEDFROM.TABLENAME, bdmReceivedFrom));

    final DynamicEvidenceDataAttributeDetails bdmReceivedFromCountryAttrName =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kBDMRECEIVEDFROMCOUNTRY_ATTR);
    DynamicEvidenceTypeConverter.setAttribute(bdmReceivedFromCountryAttrName,
      new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, bdmReceivedFromCountry));

    final DynamicEvidenceDataAttributeDetails bdmModeOfReceiptAttrName =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kBDMMODEOFRECEIPT_ATTR);
    DynamicEvidenceTypeConverter.setAttribute(bdmModeOfReceiptAttrName,
      new CodeTableItem(BDMMODEOFRECEIPT.TABLENAME, bdmModeOfReceipt));

    // END: Bug 111293: Address evidence details are not saved after the edit
  }

  /**
   * util method to retrieve evidenceDetails on concernroleID, EvidenceType
   *
   * @param concernRoleID
   * @param evidenceType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<DynamicEvidenceDataDetails>
    getEvdDtlsByConcernroleIDandEvidenceType(final long concernRoleID,
      final String evidenceType) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> dynamicEvidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final ParticipantIDEvidenceTypeStatusesKey participantIDEvidenceTypeStatusesKey =
      new ParticipantIDEvidenceTypeStatusesKey();

    participantIDEvidenceTypeStatusesKey.evidenceType = evidenceType;
    participantIDEvidenceTypeStatusesKey.participantID = concernRoleID;
    participantIDEvidenceTypeStatusesKey.statusCode1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();

    final EvidenceDescriptorKeyList evidenceDescriptorKeyList =
      EvidenceDescriptorFactory.newInstance()
        .searchActiveInEditByParticipantIDAndType(
          participantIDEvidenceTypeStatusesKey);

    for (final EvidenceDescriptorKey evidenceDescriptorKey : evidenceDescriptorKeyList.dtls) {

      evidenceKey.evidenceID = evidenceDescriptorDAO
        .get(evidenceDescriptorKey.evidenceDescriptorID).getEvidenceID();
      evidenceKey.evidenceType = evidenceType;
      final EIEvidenceReadDtls eiEvidenceReadDtls =
        evidenceControllerObj.readEvidence(evidenceKey);
      dynamicEvidenceDataDetailsList
        .add((DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject);
    }

    return dynamicEvidenceDataDetailsList;

  }

  /**
   * Method to end date the dynamic evidence based on the business end date.
   *
   * @param evidenceID dynamic evidence identifier
   * @param endDate evidence end date
   * @param evidenceType evidence type
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static void endDateEvidence(final long evidenceID,
    final Date endDate, final String evidenceType,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();
    evidenceIDDetails.evidenceID = evidenceID;

    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();

    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(evidenceType);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = evidenceType;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    if (evidenceType.equals(CASEEVIDENCE.BDMTAX)) {
      BDMEvidenceUtil.setDataAttribute("toDate",
        DateTimeTools.formatDateISO(endDate), evidenceData);
    } else {
      final DynamicEvidenceDataAttributeDetails endDateAttribute =
        evidenceData
          .getAttribute(evidenceData.getEvidenceTypeVersionDefinition()
            .getBusinessEndDateAttribute().getName());

      DynamicEvidenceTypeConverter.setAttribute(endDateAttribute, endDate);
    }

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = evidenceID;
    relatedIDAndTypeKey.evidenceType = evidenceType;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // modify evidence details
    final EIEvidenceModifyDtls evidenceDetails = new EIEvidenceModifyDtls();

    evidenceDetails.evidenceObject = evidenceData;
    evidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    evidenceDetails.descriptor.versionNo = evidenceDescriptorDtls.versionNo;
    evidenceDetails.descriptor.changeReason = evidenceChangeReason;
    evidenceDetails.descriptor.effectiveFrom = Date.kZeroDate;

    evidenceControllerObj.modifyEvidence(evidenceKey, evidenceDetails);
  }

  /**
   * Method to modify selected attribute of the dynamic evidence.
   *
   * @param evidenceID dynamic evidence identifier
   * @param attributeName evidence attribute name
   * @param evidenceType evidence type
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static void modifySelectedEvidenceAttribute(final long evidenceID,
    final String attributeName, final String attributeValue,
    final String evidenceType, final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();
    evidenceIDDetails.evidenceID = evidenceID;

    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();

    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(evidenceType);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = evidenceType;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    BDMEvidenceUtil.setDataAttribute(attributeName, attributeValue,
      evidenceData);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = evidenceID;
    relatedIDAndTypeKey.evidenceType = evidenceType;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // modify evidence details
    final EIEvidenceModifyDtls evidenceDetails = new EIEvidenceModifyDtls();

    evidenceDetails.evidenceObject = evidenceData;
    evidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    evidenceDetails.descriptor.versionNo = evidenceDescriptorDtls.versionNo;
    evidenceDetails.descriptor.changeReason = evidenceChangeReason;
    evidenceDetails.descriptor.effectiveFrom = Date.kZeroDate;

    evidenceControllerObj.modifyEvidence(evidenceKey, evidenceDetails);
  }

  /**
   * Method modify the start and end date of the evidence.
   *
   * @param evidenceID dynamic evidence identifier
   * @param endDate evidence end date
   * @param evidenceType evidence type
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static void modifyDateEvidenceDates(final long evidenceID,
    final Date startDate, final Date endDate, final String evidenceType,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();
    evidenceIDDetails.evidenceID = evidenceID;

    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();

    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(evidenceType);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = evidenceType;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    final DynamicEvidenceDataAttributeDetails startDateAttribute =
      evidenceData
        .getAttribute(evidenceData.getEvidenceTypeVersionDefinition()
          .getBusinessStartDateAttribute().getName());

    final DynamicEvidenceDataAttributeDetails endDateAttribute = evidenceData
      .getAttribute(evidenceData.getEvidenceTypeVersionDefinition()
        .getBusinessEndDateAttribute().getName());

    DynamicEvidenceTypeConverter.setAttribute(startDateAttribute, startDate);
    DynamicEvidenceTypeConverter.setAttribute(endDateAttribute, endDate);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = evidenceID;
    relatedIDAndTypeKey.evidenceType = evidenceType;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // modify evidence details
    final EIEvidenceModifyDtls evidenceDetails = new EIEvidenceModifyDtls();

    evidenceDetails.evidenceObject = evidenceData;
    evidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    evidenceDetails.descriptor.versionNo = evidenceDescriptorDtls.versionNo;
    evidenceDetails.descriptor.changeReason = evidenceChangeReason;
    evidenceDetails.descriptor.effectiveFrom = Date.kZeroDate;

    evidenceControllerObj.modifyEvidence(evidenceKey, evidenceDetails);
  }

  /**
   * Method to modify the dynamic evidence with the data passed in the evidence
   * POJO.
   *
   * @param evidenceID dynamic evidence identifier
   * @param endDate evidence end date
   * @param evidenceType evidence type
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static void modifyEvidence(final long evidenceID,
    final String evidenceType, final Object evidenceVO,
    final String evidenceChangeReason, final boolean... correctionInd)
    throws AppException, InformationalException {

    final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();
    evidenceIDDetails.evidenceID = evidenceID;

    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();

    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(evidenceType);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = evidenceType;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    // set updated attributes for the evidence
    DynamicEvidenceDataAttributeDetails evidenceAttribute;

    try {

      for (final Field field : evidenceVO.getClass().getDeclaredFields()) {
        field.setAccessible(true);

        // skip if field name is evidenceID, or field value is null, or the
        // field does not exist on evidence data
        if (field.getName().equals("evidenceID")
          || field.get(evidenceVO) == null
          || evidenceData.getAttribute(field.getName()) == null)
          continue;

        evidenceAttribute = evidenceData.getAttribute(field.getName());

        if (field.getType().isAssignableFrom(Date.class)
          && null != field.get(evidenceVO)) {

          DynamicEvidenceTypeConverter.setAttribute(evidenceAttribute,
            field.get(evidenceVO));
        } else if (field.getType().isAssignableFrom(DateTime.class)
          && null != field.get(evidenceVO)) {

          DynamicEvidenceTypeConverter.setAttribute(evidenceAttribute,
            field.get(evidenceVO));
        } else {
          evidenceAttribute.setValue(field.get(evidenceVO).toString());
        }

      }
    } catch (final IllegalArgumentException e) {

      Trace.kTopLevelLogger.error(
        BDMConstants.BDM_LOGS_PREFIX + "Exception modifying the evidence "
          + evidenceType + " \n " + e.getMessage());
      throw new AppRuntimeException(e);
    } catch (final IllegalAccessException e) {

      Trace.kTopLevelLogger.error(
        BDMConstants.BDM_LOGS_PREFIX + "Exception modifying the evidence "
          + evidenceType + " \n " + e.getMessage());
      throw new AppRuntimeException(e);
    }

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = evidenceID;
    relatedIDAndTypeKey.evidenceType = evidenceType;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // modify evidence details
    final EIEvidenceModifyDtls evidenceDetails = new EIEvidenceModifyDtls();

    evidenceDetails.evidenceObject = evidenceData;
    evidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    evidenceDetails.descriptor.versionNo = evidenceDescriptorDtls.versionNo;

    // BEGIN TASK 26338 - Do not Set the EffectiveFrom when the CorrectionInd is
    // true;
    if (correctionInd.length == 0
      || correctionInd.length != 0 && !correctionInd[0]) {
      evidenceDetails.descriptor.effectiveFrom = Date.getCurrentDate();
    }
    // END TASK 26338
    evidenceDetails.descriptor.changeReason = evidenceChangeReason;
    // evidenceDetails.descriptor.effectiveFrom = Date.getCurrentDate();

    evidenceControllerObj.modifyEvidence(evidenceKey, evidenceDetails);
  }

  /**
   * util method to retreive address lements from address data
   *
   * @param otherAddressData
   * @param addressElementType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getAddressDetails(final OtherAddressData otherAddressData,
    final String addressElementType)
    throws AppException, InformationalException {

    final AddressData addressDataObj = AddressDataFactory.newInstance();

    final AddressMap addressMap = new AddressMap();

    addressMap.name = addressElementType;

    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final ElementDetails elementDetails =
      addressDataObj.findElement(addressMapList, addressMap);

    if (elementDetails.elementFound) {

      return elementDetails.elementValue;
    }

    return "";

  }

  /**
   * @since 9388
   * @param applicationEntity
   * @param dataStoreToCEFMappings
   * @param schemaName
   * @throws AppException
   * @throws InformationalException
   */
  public void mapTaxCreditEvidence(final Entity applicationEntity,
    final DataStoreCEFMappings dataStoreToCEFMappings,
    final String schemaName) throws AppException, InformationalException {

    final long concernroleID =
      dataStoreToCEFMappings.getPrimaryClient().getConcernRoleID();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernroleID;

    final long participantDataCaseID = ParticipantDataCaseFactory
      .newInstance().getParticipantDataCase(concernRoleKey).caseID;
    final long caseParticipantRoleID =
      bdmUtil.getCaseParticipantRoleID(participantDataCaseID,
        concernroleID).caseParticipantRoleID;

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernroleID, CASEEVIDENCE.BDMTAX);
    // Task 61493 When a new tax credit info was entered in the application and
    // then submitted, the existing tax credit info should have been end-dated
    for (final DynamicEvidenceDataDetails dynamicEvidenceDataDetails : evidenceDataDetailsList) {
      BDMEvidenceUtil.endDateEvidence(dynamicEvidenceDataDetails.getID(),
        Date.getCurrentDate().addDays(-1), CASEEVIDENCE.BDMTAX,
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT);
    }

    try {
      createTaxCreditEvidence(caseParticipantRoleID, participantDataCaseID,
        applicationEntity, schemaName, concernroleID);
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * util method to create TaxCredit Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createTaxCreditEvidence(final long caseParticipantRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final long concernroleID)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity =
      applicationEntity.getChildEntities(dataStore.getEntityType("Person"),
        "isPrimaryParticipant==true")[0];
    Boolean isSaskatchewanResident = false;

    try {

      final Entity residentialEntity = personEntity
        .getChildEntities(dataStore.getEntityType("ResidentialAddress"))[0];

      isSaskatchewanResident = residentialEntity.hasAttribute("province")
        ? residentialEntity.getAttribute("province")
          .equals(PROVINCETYPE.SASKATCHEWAN) ? true : false
        : false;

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " No residential Address could be found");
    }

    final Entity taxEntity = applicationEntity
      .getChildEntities(dataStore.getEntityType("TaxInfo"))[0];

    final int noOfDependants = taxEntity.hasAttribute("childAge")
      ? Integer.parseInt(taxEntity.getAttribute("childAge")) : 0;

    final curam.core.sl.struct.EvidenceTypeKey eType =
      new curam.core.sl.struct.EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMTAX;

    // get caseParticipantRoleID of the person
    final long caseParticipantRoleid = caseParticipantRoleID;

    // get Latest Version of income Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("caseParticipant"),
      caseParticipantRoleid);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("fromDate"),
      Date.getCurrentDate());

    // native
    final String isNativeStatus =
      taxEntity.getAttribute("indianStatus").equals(IEGYESNO.YES)
        ? BDMALERTCHOICE.YES : BDMALERTCHOICE.NO;

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("nativeStatus"),
      new CodeTableItem(BDMALERTCHOICE.TABLENAME, isNativeStatus));

    // TO-DO update noOfDependants
    final int noofDependant =
      isSaskatchewanResident ? noOfDependants > 0 ? noOfDependants : 1 : 0;

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("numberOfdependant"),
      noofDependant);

    if (isNativeStatus.equals(BDMALERTCHOICE.YES)) { // set this only if Native
      // Status
      // is Yes

      final String incomeTaxFromPayIEG =
        taxEntity.getAttribute("dedIncomeTax").equals(IEGYESNO.YES)
          ? BDMALERTCHOICE.YES : BDMALERTCHOICE.NO;

      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidencedataDetails.getAttribute("incomeTaxFromPay"),
        new CodeTableItem(BDMALERTCHOICE.TABLENAME, incomeTaxFromPayIEG));

      if (incomeTaxFromPayIEG.equals(BDMALERTCHOICE.YES)) { // set this only if
        // Deduct infrom
        // from tax is yes

        final String dedIncomeTaxPayIEG =
          taxEntity.getAttribute("dedIncomeTaxPay").equals(IEGYESNO.YES)
            ? BDMALERTCHOICE.YES : BDMALERTCHOICE.NO;
        DynamicEvidenceTypeConverter.setAttribute(
          dynamicEvidencedataDetails.getAttribute("incomeTaxOnlyPart"),
          new CodeTableItem(BDMALERTCHOICE.TABLENAME, dedIncomeTaxPayIEG));
      }
    }
    // this is a mandatory field
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("taxCredit"), new CodeTableItem(
        BDMTAXSITUATION.TABLENAME, taxEntity.getAttribute("taxSituation")));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;
    descriptor.changeReason = EVIDENCECHANGEREASON.REPORTEDBYCLIENT;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(caseParticipantRoleid);
    genericDtls.addRelCp("caseParticipant", cpAdaptor);
    evidenceServiceInterface.createEvidence(genericDtls);

  }

  /**
   * map and create incarceration evidence
   *
   * @since ADO-16437
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapIncarcerationEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName) throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createIncarcerationEvidence(concernRoleID, caseID, applicationEntity,
        schemaName);
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * util method to create Incarceration Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createIncarcerationEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName) throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity =
      applicationEntity.getChildEntities(dataStore.getEntityType("Person"),
        "isPrimaryParticipant==true")[0];

    final BDMIncarcerationEvidenceVO incarcerationEvidenceVO =
      new BDMIncarcerationEvidenceVO();
    try {

      final Entity incarcerationEntity = personEntity
        .getChildEntities(dataStore.getEntityType("Incarceration"))[0];
      incarcerationEvidenceVO
        .setInstitutionName(incarcerationEntity.getAttribute("instName"));
      incarcerationEvidenceVO.setStartDate(
        Date.fromISO8601(incarcerationEntity.getAttribute("startDateInca")));
      if (!incarcerationEntity.getAttribute("endDateInca").isEmpty()) {
        incarcerationEvidenceVO.setEndDate(
          Date.fromISO8601(incarcerationEntity.getAttribute("endDateInca")));
      } else {
        incarcerationEvidenceVO.setEndDate(Date.kZeroDate);
      }
      incarcerationEvidenceVO.setAttestationDate(Date.getCurrentDate());
      incarcerationEvidenceVO.setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);
      incarcerationEvidenceVO.setComments(BDMConstants.kAttestationComments);
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(incarcerationEvidenceVO);
      createACOrICDynamicEvidence(concernRoleID, evidenceData,
        CASEEVIDENCE.BDMINCARCERATION, EVIDENCECHANGEREASON.REPORTEDBYCLIENT,
        caseID);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " No incarceration info could be found");
    }

  }

  /**
   * map and create income evidence
   *
   * @since ADO-16437
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapIncomeEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName) throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createIncomeEvidence(concernRoleID, caseID, applicationEntity,
        schemaName);
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * util method to create Income Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createIncomeEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName) throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity =
      applicationEntity.getChildEntities(dataStore.getEntityType("Person"),
        "isPrimaryParticipant==true")[0];

    final BDMIncomeEvidenceVO incomeEvidenceVO = new BDMIncomeEvidenceVO();
    try {

      final Entity incomeEntity =
        personEntity.getChildEntities(dataStore.getEntityType("Income"))[0];
      incomeEvidenceVO.setIncomeYear(getPreviousYear());
      String incomeAmount = incomeEntity.getAttribute("incomeAmount");
      incomeAmount = incomeAmount.replace(",", "");
      incomeEvidenceVO.setIncomeAmount(incomeAmount);
      incomeEvidenceVO.setAttestationDate(Date.getCurrentDate());
      incomeEvidenceVO.setBdmAgreeAttestation(BDMAGREEATTESTATION.YES);
      incomeEvidenceVO.setComments(BDMConstants.kAttestationComments);
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(incomeEvidenceVO);
      createACOrICDynamicEvidence(concernRoleID, evidenceData,
        CASEEVIDENCE.BDMINCOME, EVIDENCECHANGEREASON.REPORTEDBYCLIENT,
        caseID);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        BDMConstants.BDM_LOGS_PREFIX + " No income info could be found");
    }

  }

  /**
   * Util method to get previous year
   */
  private String getPreviousYear() {

    final Date today = curam.util.type.Date.getCurrentDate();
    final String date = today.toString();
    final String[] split = date.split("/");
    String year = new String();
    for (final String piece : split) {
      if (piece.length() == 4) {
        year = piece;
        Integer yearInt = Integer.parseInt(year);
        yearInt = yearInt - 1;
        year = yearInt.toString();
      }
    }
    return year;
  }

  /**
   * map and create incarceration evidence
   *
   * @since ADO-16437
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapAttestationEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName, final String intakeApplicationType)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createAttestationEvidence(concernRoleID, caseID, applicationEntity,
        schemaName, intakeApplicationType);
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * util method to create Attestation Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createAttestationEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final String intakeApplicationType)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity submissionEntity = applicationEntity
      .getChildEntities(dataStore.getEntityType("Submission"))[0];

    final BDMAttestationVO attestationEvidenceVO = new BDMAttestationVO();
    try {

      if (submissionEntity.getAttribute("acceptAttestation")
        .equals(IEGYESNO.YES)) {
        attestationEvidenceVO
          .setAttestationPeriodStartDate(Date.getCurrentDate());
        attestationEvidenceVO.setEligible(true);
        if (intakeApplicationType.equals(ApplicationChannel.ONLINE)) {
          attestationEvidenceVO
            .setAttestationType(BDMATTESTATIONTYPE.CLIENT_PORTAL_APPLICATION);
        } else if (intakeApplicationType
          .equals(ApplicationChannel.INTERNAL)) {
          attestationEvidenceVO
            .setAttestationType(BDMATTESTATIONTYPE.AGENT_PORTAL_APPLICATION);
        }
      }
      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(attestationEvidenceVO);
      createACOrICDynamicEvidence(concernRoleID, evidenceData,
        CASEEVIDENCE.BDMATTS, EVIDENCECHANGEREASON.REPORTEDBYCLIENT, caseID);
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " No incarceration info could be found");
    }

  }

  /*
   * USVI-367 SA Creates dynamic evidence on AC or IC
   *
   * @param participantRoleID
   *
   * @param evidenceData
   */
  public static void createACOrICDynamicEvidence(final long participantRoleID,
    final HashMap<String, String> evidenceData, final String evidenceType,
    final String evidenceChangeReason, final long caseID)
    throws AppException, InformationalException {

    boolean activateEvidence = false;
    Long acOrICcaseID = 0L;
    if (caseID != 0) {
      acOrICcaseID = caseID;
    } else {
      // find active AC case, or IC case
      final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
        new ConcernRoleIDStatusCodeKey();
      concernRoleIDStatusCodeKey.dtls.concernRoleID = participantRoleID;
      concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
      final CaseHeaderDtlsList caseheaderDtlsList = CaseHeaderFactory
        .newInstance().searchByConcernRoleID(concernRoleIDStatusCodeKey);

      if (!caseheaderDtlsList.dtlsList.dtls.isEmpty()) {
        for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
          if (dtl.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
            && dtl.statusCode.equals(CASESTATUS.OPEN)) {
            acOrICcaseID = dtl.caseID;
            break;
          } else if (dtl.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
            acOrICcaseID = dtl.caseID;
            activateEvidence = true;
          }
        }
      }
    }
    if (acOrICcaseID != 0L) {

      final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
        new CaseIDTypeAndStatusKey();
      caseIDTypeAndStatusKey.key.caseID = acOrICcaseID;
      caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
      caseIDTypeAndStatusKey.key.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
      final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
        CaseParticipantRoleFactory.newInstance()
          .listCaseParticipantRolesByTypeCaseIDAndStatus(
            caseIDTypeAndStatusKey);

      if (evidenceType.equals(CASEEVIDENCE.BDMATTS)) {
        evidenceData.put("attestee",
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      } else if (evidenceType.equals(CASEEVIDENCE.BDMINCOME)
        || evidenceType.equals(CASEEVIDENCE.BDMINCARCERATION)) {
        evidenceData.put("caseParticipantRoleID",
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      } else if (evidenceType.equals(CASEEVIDENCE.BDMVTW)) {
        evidenceData.put("caseParticipant",
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      } else {
        evidenceData.put("participant",
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      }

      final DynamicEvidenceMaintenance dynObj =
        DynamicEvidenceMaintenanceFactory.newInstance();
      final DynamicEvidenceData dynamicEvidenceData =
        new DynamicEvidenceData();
      final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

      dynamicEvidenceData.data =
        setDynamicEvidenceDetailsByEvidenceType(evidenceData, evidenceType);
      dynamicEvidenceData.caseIDKey.caseID = acOrICcaseID;
      dynamicEvidenceData.descriptor.evidenceType = evidenceType;
      dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
      dynamicEvidenceData.descriptor.changeReason = evidenceChangeReason;
      dynMod.effectiveDateUsed =
        BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

      // create the evidence
      final ReturnEvidenceDetails returnEvidenceDetails =
        dynObj.createEvidence(dynamicEvidenceData, dynMod);

      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        updateChangeReason(evidenceType, evidenceChangeReason,
          returnEvidenceDetails);

      // apply activation code to the newly created IC dynamic Evidence
      if (activateEvidence) {
        applyInEditEvidenceToICDynamicEvidence(evidenceDescriptorDtls);
      }
    }

  }

  // OOTB code does not update the Change reason even if we pass the
  // change reason to OOTB Code.
  // Therefore this code is written to update the change reason.
  private static EvidenceDescriptorDtls updateChangeReason(
    final String evidenceType, final String evidenceChangeReason,
    final ReturnEvidenceDetails returnEvidenceDetails)
    throws AppException, InformationalException {

    final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndEvidenceTypeKey.relatedID =
      returnEvidenceDetails.evidenceKey.evidenceID;
    relatedIDAndEvidenceTypeKey.evidenceType = evidenceType;
    final curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor descriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      descriptorObj.readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
    final EvidenceDescriptorKey descriptorKey = new EvidenceDescriptorKey();
    descriptorKey.evidenceDescriptorID =
      evidenceDescriptorDtls.evidenceDescriptorID;
    evidenceDescriptorDtls.changeReason = evidenceChangeReason;
    descriptorObj.modify(descriptorKey, evidenceDescriptorDtls);
    return evidenceDescriptorDtls;
  }

  /**
   * This is an helper method to apply in-edit evidence to all
   * the newly created/modified IC Dynamic evidences.
   *
   * @param EIEvidenceKey
   * @throws AppException, InformationalException
   */

  public static void applyInEditEvidenceToICDynamicEvidence(
    final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    caseKey.caseID = evidenceDescriptorDtls.caseID;
    final curam.core.sl.infrastructure.struct.ApplyChangesDetails tabbedList =
      new curam.core.sl.infrastructure.struct.ApplyChangesDetails();
    final StringBuffer newAndUpdateListStr = new StringBuffer();
    newAndUpdateListStr.append(evidenceDescriptorDtls.evidenceDescriptorID)
      .append(CuramConst.gkPipeDelimiterChar)
      .append(evidenceDescriptorDtls.relatedID)
      .append(CuramConst.gkPipeDelimiterChar)
      .append(evidenceDescriptorDtls.evidenceType)
      .append(CuramConst.gkPipeDelimiterChar)
      .append(evidenceDescriptorDtls.correctionSetID)
      .append(CuramConst.gkPipeDelimiterChar)
      .append(evidenceDescriptorDtls.successionID);
    tabbedList.newAndUpdateList = newAndUpdateListStr.toString();
    EvidenceControllerFactory.newInstance().applyChanges(caseKey, tabbedList);
  }

  /**
   * util method to retrieve active evidenceDetails on concernroleID,
   * EvidenceType and
   * case (AC or IC)
   *
   * @param concernRoleID
   * @param evidenceType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<DynamicEvidenceDataDetails>
    getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(final long concernRoleID,
      final String evidenceType) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> dynamicEvidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.dtls.concernRoleID = concernRoleID;
    concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
    final CaseHeaderDtlsList caseheaderDtlsList = CaseHeaderFactory
      .newInstance().searchByConcernRoleID(concernRoleIDStatusCodeKey);
    Long acOrICcaseID = 0L;

    if (!caseheaderDtlsList.dtlsList.dtls.isEmpty()) {
      for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
        if (dtl.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
          && dtl.statusCode.equals(CASESTATUS.OPEN)) {
          acOrICcaseID = dtl.caseID;
        } else if (dtl.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
          acOrICcaseID = dtl.caseID;
        } else {
          acOrICcaseID = 0L;
        }
        if (acOrICcaseID != 0L) {
          final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
            new CaseIDTypeAndStatusKey();
          caseIDTypeAndStatusKey.key.caseID = acOrICcaseID;
          caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
          caseIDTypeAndStatusKey.key.typeCode =
            CASEPARTICIPANTROLETYPE.PRIMARY;

          final CaseIDEvidenceTypeStatusesKey caseIDEvidenceTypeStatusesKey =
            new CaseIDEvidenceTypeStatusesKey();
          caseIDEvidenceTypeStatusesKey.caseID = acOrICcaseID;
          caseIDEvidenceTypeStatusesKey.evidenceType = evidenceType;
          caseIDEvidenceTypeStatusesKey.statusCode1 =
            EVIDENCEDESCRIPTORSTATUS.ACTIVE;

          final EIEvidenceKey evidenceKey = new EIEvidenceKey();

          final EvidenceDescriptorKeyList evidenceDescriptorKeyList =
            EvidenceDescriptorFactory.newInstance()
              .searchActiveInEditByCaseIDAndType(
                caseIDEvidenceTypeStatusesKey);

          for (final EvidenceDescriptorKey evidenceDescriptorKey : evidenceDescriptorKeyList.dtls) {

            evidenceKey.evidenceID = evidenceDescriptorDAO
              .get(evidenceDescriptorKey.evidenceDescriptorID)
              .getEvidenceID();
            evidenceKey.evidenceType = evidenceType;
            final EIEvidenceReadDtls eiEvidenceReadDtls =
              evidenceControllerObj.readEvidence(evidenceKey);
            dynamicEvidenceDataDetailsList.add(
              (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject);
          }
        }
      }
    }
    return dynamicEvidenceDataDetailsList;
  }

  /**
   * Method to modify the dynamic evidence in AC/IC with the data passed in the
   * evidence
   * POJO.
   *
   * @param evidenceID dynamic evidence identifier
   * @param endDate evidence end date
   * @param evidenceType evidence type
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static void modifyEvidenceForCase(final long concernRoleID,
    final long evidenceID, final String evidenceType,
    final HashMap<String, String> evidenceData,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    boolean activateEvidence = false;
    long acOrICaseID = 0;

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.dtls.concernRoleID = concernRoleID;
    concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
    final CaseHeaderDtlsList caseheaderDtlsList = CaseHeaderFactory
      .newInstance().searchByConcernRoleID(concernRoleIDStatusCodeKey);

    final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndEvidenceTypeKey.evidenceType = evidenceType;
    relatedIDAndEvidenceTypeKey.relatedID = evidenceID;

    final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
      EvidenceDescriptorFactory.newInstance()
        .searchByRelatedIDAndEvidenceType(relatedIDAndEvidenceTypeKey);
    if (evidenceDescriptorDtlsList.dtls.size() > 0) {
      acOrICaseID = evidenceDescriptorDtlsList.dtls.get(0).caseID;
      for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
        if (dtl.caseID == acOrICaseID
          && dtl.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
          activateEvidence = true;
        }
      }
    }
    if (acOrICaseID != 0L) {

      final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
        new CaseIDTypeAndStatusKey();
      caseIDTypeAndStatusKey.key.caseID = acOrICaseID;
      caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
      caseIDTypeAndStatusKey.key.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
      final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
        CaseParticipantRoleFactory.newInstance()
          .listCaseParticipantRolesByTypeCaseIDAndStatus(
            caseIDTypeAndStatusKey);

      if (evidenceType.equals(CASEEVIDENCE.BDMINCOME)
        || evidenceType.equals(CASEEVIDENCE.BDMINCARCERATION)) {
        evidenceData.put("caseParticipantRoleID",
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      } else {
        evidenceData.put("participant",
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      }

      final DynamicEvidenceMaintenance dynObj =
        DynamicEvidenceMaintenanceFactory.newInstance();
      final DynamicEvidenceData dynamicEvidenceData =
        new DynamicEvidenceData();
      final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

      dynamicEvidenceData.data = BDMEvidenceUtil
        .setDynamicEvidenceDetailsByEvidenceType(evidenceData, evidenceType);
      dynamicEvidenceData.caseIDKey.caseID = acOrICaseID;
      dynamicEvidenceData.evidenceId = evidenceID;
      dynamicEvidenceData.descriptor.evidenceType = evidenceType;
      dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
      dynamicEvidenceData.descriptor.changeReason = evidenceChangeReason;
      dynMod.effectiveDateUsed =
        BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

      // modify the evidence
      final ReturnEvidenceDetails returnEvidenceDetails =
        dynObj.modifyEvidence(dynamicEvidenceData, dynMod);

      if (activateEvidence) {
        final curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor descriptorObj =
          EvidenceDescriptorFactory.newInstance();
        relatedIDAndEvidenceTypeKey.relatedID =
          returnEvidenceDetails.evidenceKey.evidenceID;
        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          descriptorObj.readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
        applyInEditEvidenceToICDynamicEvidence(evidenceDescriptorDtls);
      }
    }
  }

  /**
   * Method to set Dynamic Evidence Details by evidence type
   * POJO.
   *
   * @param evidenceData
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @SuppressWarnings("rawtypes")
  public static String setDynamicEvidenceDetailsByEvidenceType(
    final HashMap<String, String> evidenceData, final String evidenceType) {

    Document document = null;
    String result = "";

    try {
      document = DataWrapperXMLUtils.getNewDocument();

      final Element dataElement =
        document.createElement("dynamicEvidenceData");
      Element valueElement = null;
      Text valueText = null;

      final Iterator it = evidenceData.entrySet().iterator();
      while (it.hasNext()) {
        final Map.Entry pair = (Map.Entry) it.next();

        valueElement = document.createElement(pair.getKey().toString());
        if (pair.getValue() != null) {
          valueText = document.createTextNode(pair.getValue().toString());
          valueElement.appendChild(valueText);
        }

        if (pair.getKey().toString().equalsIgnoreCase("caseParticipantRoleID")
          && (evidenceType.equals(CASEEVIDENCE.BDMINCOME)
            || evidenceType.equals(CASEEVIDENCE.BDMINCARCERATION))) {
          final Element dataElement1 =
            document.createElement("caseParticipants");
          final Element dataElement2 =
            document.createElement("caseParticipant");
          dataElement2.appendChild(valueElement);
          dataElement1.appendChild(dataElement2);
          // BEGIN - User Story 21834 - Added Attestation
          final Node cloneElement = valueElement.cloneNode(true);
          final Element attesteeDataElement =
            document.createElement("attesteeCaseParticipant");
          attesteeDataElement.appendChild(cloneElement);
          dataElement1.appendChild(attesteeDataElement);
          // END - User Story 21834 - Added Attestation
          dataElement.appendChild(dataElement1);
        } else {
          dataElement.appendChild(valueElement);
        }
      }
      document.appendChild(dataElement);

    } catch (final ParserConfigurationException e) {
      e.printStackTrace();
    }

    try {
      result = DataWrapperXMLUtils.printDocumentToString(document);
    } catch (final TransformerException e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Task 21654 modify PDC evidence and return the evidence details
   *
   * @param participantRoleID
   * @param evidenceID
   * @param evidenceType
   * @param evidenceData
   */
  public static ReturnEvidenceDetails modifyPDCDynamicEvidence(
    final long participantRoleID, final long evidenceID,
    final String evidenceType, final HashMap<String, String> evidenceData,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey.dtls.concernRoleID = participantRoleID;
    concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
    final CaseHeaderDtlsList caseheaderDtlsList = CaseHeaderFactory
      .newInstance().searchByConcernRoleID(concernRoleIDStatusCodeKey);
    Long participantDataCaseID = 0L;

    if (!caseheaderDtlsList.dtlsList.dtls.isEmpty()) {
      for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
        if (dtl.caseTypeCode.equals(CASETYPECODE.PARTICIPANTDATACASE)) {
          participantDataCaseID = dtl.caseID;
        }
      }
      if (participantDataCaseID != 0L) {

        final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
          new CaseIDTypeAndStatusKey();
        caseIDTypeAndStatusKey.key.caseID = participantDataCaseID;
        caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
        caseIDTypeAndStatusKey.key.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
        final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
          CaseParticipantRoleFactory.newInstance()
            .listCaseParticipantRolesByTypeCaseIDAndStatus(
              caseIDTypeAndStatusKey);

        if (evidenceType.equals(PDCConst.PDCPHONENUMBER)) {
          evidenceData.put("caseParticipantRoleID",
            String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
              .get(0).caseParticipantRoleID));
        } else {
          evidenceData.put("participant",
            String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
              .get(0).caseParticipantRoleID));
        }

        final DynamicEvidenceMaintenance dynObj =
          DynamicEvidenceMaintenanceFactory.newInstance();
        final DynamicEvidenceData dynamicEvidenceData =
          new DynamicEvidenceData();
        final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

        dynamicEvidenceData.data =
          BDMEvidenceUtil.setDynamicEvidenceDetails(evidenceData);
        dynamicEvidenceData.caseIDKey.caseID = participantDataCaseID;
        dynamicEvidenceData.evidenceId = evidenceID;
        dynamicEvidenceData.descriptor.evidenceType = evidenceType;
        dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
        dynamicEvidenceData.descriptor.changeReason = evidenceChangeReason;
        dynMod.effectiveDateUsed =
          BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());
        // modify the evidence
        final ReturnEvidenceDetails returnEvidenceDetails =
          dynObj.modifyEvidence(dynamicEvidenceData, dynMod);
        return returnEvidenceDetails;
      }
    }
    return null;
  }

  /**
   * Task 25425 Get ActiveEvidences by evidence Type and CaseID
   *
   * @param participantRoleID
   * @param evidenceID
   * @param evidenceType
   * @param evidenceData
   */
  @AccessLevel(AccessLevelType.INTERNAL)
  public RelatedIDAndEvidenceTypeKeyList
    getActiveEvidenceIDByEvidenceTypeAndCase(final String evidenceType,
      final CaseKey caseKey) throws AppException, InformationalException {

    final CaseIDStatusAndEvidenceTypeKey caseIDStatusAndEvidenceTypeKey =
      new CaseIDStatusAndEvidenceTypeKey();

    caseIDStatusAndEvidenceTypeKey.caseID = caseKey.caseID;
    caseIDStatusAndEvidenceTypeKey.evidenceType = evidenceType;
    caseIDStatusAndEvidenceTypeKey.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();

    return evidenceDescriptorObj
      .searchByCaseIDTypeAndStatus(caseIDStatusAndEvidenceTypeKey);
  }

  /**
   * BEGIN TASK - 25425 Update Attestation Evidence to correct ParticipantID and
   * CasePArticipant ID
   *
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void updatePrimaryCaseParticipantRoleIDForAttestation(
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings)
    throws AppException, InformationalException {

    // get Primary caseParticipantRoleID
    final long concernroleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernroleID;

    final long participantDataCaseID = ParticipantDataCaseFactory
      .newInstance().getParticipantDataCase(concernRoleKey).caseID;
    final long primarycaseParticipantRoleID =
      bdmUtil.getCaseParticipantRoleID(participantDataCaseID,
        concernroleID).caseParticipantRoleID;

    // GEt EvidenceID from the case for EvidenceType = Attestation
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = caseID;
    final RelatedIDAndEvidenceTypeKeyList attestationRelatedId =
      getActiveEvidenceIDByEvidenceTypeAndCase(CASEEVIDENCE.BDMATTS, caseKey);

    for (final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey : attestationRelatedId.dtls) {
      final EvidenceServiceInterface evidenceServiceInterface =
        EvidenceGenericSLFactory
          .instance(relatedIDAndEvidenceTypeKey.relatedID);
      final EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
      evidenceCaseKey.caseIDKey = new curam.core.sl.struct.CaseIDKey();
      evidenceCaseKey.caseIDKey.caseID = caseKey.caseID;
      evidenceCaseKey.evidenceKey.evidenceID =
        relatedIDAndEvidenceTypeKey.relatedID;
      evidenceCaseKey.evidenceKey.evType = CASEEVIDENCE.BDMATTS;

      final ReadEvidenceDetails evdDtls =
        evidenceServiceInterface.readEvidence(evidenceCaseKey);
      final DynamicEvidenceDataAttributeDetails dedaDtls =
        evdDtls.dtls.getAttribute("attestee");
      // if not a Primary CAseParticipant on the case update it to Primary
      if (dedaDtls == null || dedaDtls.getValue() == null
        || dedaDtls.getValue().isEmpty() || !dedaDtls.getValue()
          .equalsIgnoreCase("" + primarycaseParticipantRoleID)) {
        final DynamicEvidenceDataAttribute dynamicEvidenceDataAttribute =
          DynamicEvidenceDataAttributeFactory.newInstance();
        final DynamicEvidenceDataAttributeKey dynEDKey =
          new DynamicEvidenceDataAttributeKey();
        final DynamicEvidenceDataAttributeDtls dynEDDtls =
          new DynamicEvidenceDataAttributeDtls();
        dynEDDtls.attributeID = dedaDtls.getID();
        dynEDDtls.evidenceID = relatedIDAndEvidenceTypeKey.relatedID;
        dynEDDtls.name = "attestee";
        dynEDDtls.value = "" + primarycaseParticipantRoleID;
        dynEDKey.attributeID = dedaDtls.getID();
        dynamicEvidenceDataAttribute.modify(dynEDKey, dynEDDtls);

        // Update Evidence Descriptor

        final EvidenceDescriptor evidenceDescriptorObj =
          EvidenceDescriptorFactory.newInstance();
        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          evidenceDescriptorObj
            .readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
        if (evidenceDescriptorDtls != null
          && concernroleID != evidenceDescriptorDtls.participantID) { // ParticipantId
          // is not
          // correct
          // update EvidenceDescriptor for participantID
          final EvidenceDescriptorKey key = new EvidenceDescriptorKey();
          final ModifyParticipantID modifyParticipantID =
            new ModifyParticipantID();
          modifyParticipantID.participantID = concernroleID;
          key.evidenceDescriptorID =
            evidenceDescriptorDtls.evidenceDescriptorID;
          evidenceDescriptorObj.modifyParticipantID(key, modifyParticipantID);
        }

      }
    }

  }

  /**
   * This method will create the case deduction item for the VTW, Federal or
   * Prov Tax deduction items.
   *
   * @param caseID
   * @throws AppException
   * @throws InformationalException
   */
  public static void createDeductionsForCase(final long caseID)
    throws AppException, InformationalException {

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    final MaintainCaseDeductions maintainCaseDeductions =
      new MaintainCaseDeductions();
    caseHeaderKey.caseID = caseID;
    maintainCaseDeductions.syncBenefitCaseVTWDeductions(caseHeaderKey);

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = caseID;
    // Create Federal and Provincial Tax deductions
    maintainCaseDeductions.createTaxDeductions(caseIDKey);
  }

  /**
   * util method to create third party contact Evidence
   *
   * @param details
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ReturnEvidenceDetails createThirdPartyContactEvidence(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd)
    throws AppException, InformationalException {

    final CaseIDAndParticipantRoleIDKey key =
      new CaseIDAndParticipantRoleIDKey();
    key.participantRoleID = thirdPartyEvd.thirdPartyDetails.participantRoleID;
    key.caseID = thirdPartyEvd.thirdPartyDetails.caseID;
    final CaseParticipantRoleIDStruct caseparticipantRoleIDStruct =
      CaseParticipantRoleFactory.newInstance()
        .readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(key);
    // START - Bug 100631
    final CaseIDTypeCodeKey key1 = new CaseIDTypeCodeKey();
    key1.caseID = thirdPartyEvd.thirdPartyDetails.caseID;
    key1.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;

    final CaseParticipantRoleFullDetails1 caseParticipantRoleFullDetails1 =
      CaseParticipantRoleFactory.newInstance().readByCaseIDAndTypeCode(key1);
    // END - Bug 100631

    final DynamicEvidenceMaintenance dynObj =
      DynamicEvidenceMaintenanceFactory.newInstance();
    final DynamicEvidenceData dynamicEvidenceData = new DynamicEvidenceData();
    final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();
    final HashMap<String, String> evidenceData =
      new HashMap<String, String>();
    createThirdPartyEvidenceData(evidenceData, thirdPartyEvd,
      caseParticipantRoleFullDetails1.dtls.caseParticipantRoleID);

    dynamicEvidenceData.data =
      BDMEvidenceUtil.setDynamicEvidenceDetails(evidenceData);
    dynamicEvidenceData.caseIDKey.caseID =
      thirdPartyEvd.thirdPartyDetails.caseID;
    dynamicEvidenceData.descriptor.evidenceType =
      CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT;

    dynamicEvidenceData.descriptor.receivedDate =
      thirdPartyEvd.thirdPartyDetails.receivedDate;
    dynamicEvidenceData.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    dynMod.effectiveDateUsed =
      BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

    // create the evidence
    final ReturnEvidenceDetails returnEvidenceDetails =
      dynObj.createEvidence(dynamicEvidenceData, dynMod);
    thirdPartyEvd.evidenceID = returnEvidenceDetails.evidenceKey.evidenceID;
    return returnEvidenceDetails;

  }

  /**
   * This method is used to create Third party evidence data.
   *
   * @param evidenceData
   * @param thirdPartyDetailsResult
   * @param caseparticipantRoleIDStruct
   */
  public void createThirdPartyEvidenceData(
    final HashMap<String, String> evidenceData,
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyDetailsResult,
    final long caseparticipantRoleID) {

    evidenceData.put(BDMConstants.caseParticipantRoleAttr,
      String.valueOf(caseparticipantRoleID));

    evidenceData.put(BDMConstants.participantRoleIDAttr, String
      .valueOf(thirdPartyDetailsResult.thirdPartyDetails.participantRoleID));

    evidenceData.put(BDMConstants.participantIDAttr, String
      .valueOf(thirdPartyDetailsResult.thirdPartyDetails.participantRoleID));

    evidenceData.put(BDMConstants.thirdPartyCaseParticipantRoleAttr,
      String.valueOf(
        thirdPartyDetailsResult.thirdPartyDetails.thirdPartyCaseParticipantRoleID));
    // Fix BUG-98040 Incorrect error message and N/A option on screen 2 and 3
    // when adding an Organization as Third Party
    if (!StringUtil.isNullOrEmpty(
      thirdPartyDetailsResult.selectedIndividual.concernRoleName)
      && !BDMConstants.kNotApplicable
        .equals(thirdPartyDetailsResult.selectedIndividual.concernRoleName)) {
      evidenceData.put(BDMConstants.individualWithinOrgAttr,
        thirdPartyDetailsResult.selectedIndividual.concernRoleName);
    }

    final StringList programList = StringUtil.tabText2StringListWithTrim(
      thirdPartyDetailsResult.thirdPartyDetails.program);
    if (!programList.isEmpty()) {
      final StringBuilder programBuilderEN = new StringBuilder();
      final StringBuilder programBuilderFR = new StringBuilder();
      for (final String program : programList) {
        String programDescriptionEN = BDMConstants.EMPTY_STRING;
        String programDescriptionFR = BDMConstants.EMPTY_STRING;
        try {
          programDescriptionFR = curam.util.type.CodeTable
            .getOneItem(BDMTHIRDPARTYPROGRAM.TABLENAME, program, "fr");

          programDescriptionEN = curam.util.type.CodeTable
            .getOneItem(BDMTHIRDPARTYPROGRAM.TABLENAME, program, "en");
        } catch (AppRuntimeException | AppException
          | InformationalException e1) {

          Trace.kTopLevelLogger.warn(
            BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_PREFIX_LOCALE_DESC
              + BDMTHIRDPARTYPROGRAM.TABLENAME + CODE + program
              + BDMConstants.provideEmptyStringDesc);
        }
        if (programBuilderEN.length() > 0) {
          programBuilderEN.append(BDMConstants.gkCommaSpace);
        }
        if (programBuilderFR.length() > 0) {
          programBuilderFR.append(BDMConstants.gkCommaSpace);
        }
        programBuilderFR.append(programDescriptionFR);
        programBuilderEN.append(programDescriptionEN);
      }
      evidenceData.put(BDMConstants.programAttr,
        programBuilderFR.toString() + BDMConstants.kStringSpace
          + BDMConstants.kForwardSlash + BDMConstants.kStringSpace
          + programBuilderEN.toString());
    }

    evidenceData.put(BDMConstants.kBDMDynEvdAttrNameRole,
      thirdPartyDetailsResult.thirdPartyDetails.role);

    evidenceData.put(BDMConstants.kBDMDynEvdAttrNameRoleType,
      thirdPartyDetailsResult.thirdPartyDetails.roleType);

    evidenceData.put(BDMConstants.poaClassificationAttr,
      thirdPartyDetailsResult.thirdPartyDetails.poaClass);

    evidenceData.put(BDMConstants.poaTypeAttr,
      thirdPartyDetailsResult.thirdPartyDetails.poaType);

    evidenceData.put(BDMConstants.relationshipToClientAttr,
      thirdPartyDetailsResult.thirdPartyDetails.relToClient);

    evidenceData.put(BDMConstants.languagePreferenceAttr,
      thirdPartyDetailsResult.thirdPartyDetails.languagePref);

    evidenceData.put(BDMConstants.kBDMDynEvdAttrNameFrom,
      thirdPartyDetailsResult.thirdPartyDetails.fromDate.toString());

    evidenceData.put(BDMConstants.kBDMDynEvdAttrNameTo,
      thirdPartyDetailsResult.thirdPartyDetails.toDate.toString());

    // START: BUG 98031: Third Paty Contact mapping fix
    evidenceData.put(BDMConstants.kBDMRECEIVEDFROM_ATTR,
      thirdPartyDetailsResult.thirdPartyAdditionalDetails.receivedFrom);

    evidenceData.put(BDMConstants.kBDMRECEIVEDFROMCOUNTRY_ATTR,
      thirdPartyDetailsResult.thirdPartyAdditionalDetails.receivedFromCountry);

    evidenceData.put(BDMConstants.kBDMMODEOFRECEIPT_ATTR,
      thirdPartyDetailsResult.thirdPartyAdditionalDetails.modeOfReceipt);
    // END: BUG 98031: Third Paty Contact mapping fix

    evidenceData.put(BDMConstants.commentsAttr,
      thirdPartyDetailsResult.thirdPartyAdditionalDetails.comments);

    evidenceData.put(BDMConstants.titleAttr,
      thirdPartyDetailsResult.thirdPartyDetails.title);

  }

  /**
   * This Method will End date the active address and Create a new address for
   * the given Address Type
   * 103325 294-204 Add client home address and apply to mailing address
   *
   * @param concernRoleID
   * @param addressData
   * @param startDate
   * @param typeCode
   * @param endDate
   * @param isPreferredAddress
   * @param comments
   */
  public void endDateAnAddressAndCreateNewAddress(final long concernRoleID,
    final String addressData, final Date startDate, final String typeCode,
    final Date endDate, final Boolean isPreferredAddress,
    final String comments, final Boolean isUseResAsMailAddress,
    final String country) throws AppException, InformationalException {

    String existAddCountry = CuramConst.gkEmpty;
    // read ActiveAddress For the given ConcernroleID
    final ListIterator<DynamicEvidenceDataDetails> dynEvdDataItr =
      getEvdDtlsByConcernroleIDandEvidenceType(concernRoleID,
        PDCConst.PDCADDRESS).listIterator();
    Boolean isResAddressCreated = false;
    Boolean isMailAddressCreated = false;
    while (dynEvdDataItr.hasNext()) {
      final DynamicEvidenceDataDetails evdDataDtls = dynEvdDataItr.next();

      final Date evDStartDate = Date.getDate(evdDataDtls
        .getAttribute(BDMConstants.kBDMEvidencefromDate).getValue());
      final Date toDate =
        evdDataDtls.getAttribute(BDMConstants.kBDMEvidencetoDate).getValue()
          .equals(CuramConst.gkEmpty) ? Date.kZeroDate
            : Date.getDate(evdDataDtls
              .getAttribute(BDMConstants.kBDMEvidencetoDate).getValue());
      final boolean preferredInd =
        evdDataDtls.getAttribute(BDMConstants.kEvidenceAttrPreferredInd)
          .getValue().equals("true");

      final String evdcomments = evdDataDtls
        .getAttribute(BDMConstants.kBDMEvidenceComments).getValue();
      if (evdDataDtls.getAttribute(BDMConstants.kEvidenceAttrAddressType)
        .getValue().equals(CONCERNROLEADDRESSTYPE.PRIVATE)
        && toDate.equals(Date.kZeroDate)
        && startDate.addDays(-1).after(evDStartDate)) {
        // EndDate the Active Residential Address
        final OtherAddressData otherAddressData =
          getExistingOtherAddressData(evdDataDtls.getID());
        existAddCountry = bdmUtil.getAddressDetails(otherAddressData,
          BDMConstants.kADDRESSELEMENT_COUNTRY);
        modifyAddressEvidence(concernRoleID, otherAddressData.addressData,
          evDStartDate, CONCERNROLEADDRESSTYPE.PRIVATE, startDate.addDays(-1),
          preferredInd, evdcomments, evdDataDtls.getID(), CuramConst.gkEmpty,
          CuramConst.gkEmpty, CuramConst.gkEmpty, CuramConst.gkEmpty,
          existAddCountry);

        createAddressEvidence(concernRoleID, addressData, startDate,
          CONCERNROLEADDRESSTYPE.PRIVATE, endDate, isPreferredAddress,
          comments);
        isResAddressCreated = true;
      } else if (evdDataDtls
        .getAttribute(BDMConstants.kEvidenceAttrAddressType).getValue()
        .equals(CONCERNROLEADDRESSTYPE.MAILING) && isUseResAsMailAddress
        && toDate.equals(Date.kZeroDate)
        && startDate.addDays(-1).after(evDStartDate)) {
        // EndDate the Active Residential Address
        modifyAddressEvidence(concernRoleID,
          getExistingOtherAddressData(evdDataDtls.getID()).addressData,
          evDStartDate, CONCERNROLEADDRESSTYPE.MAILING, startDate.addDays(-1),
          false, evdcomments, evdDataDtls.getID(), CuramConst.gkEmpty,
          CuramConst.gkEmpty, CuramConst.gkEmpty, CuramConst.gkEmpty,
          CuramConst.gkEmpty);

        createAddressEvidence(concernRoleID, addressData, startDate,
          CONCERNROLEADDRESSTYPE.MAILING, endDate, isPreferredAddress,
          comments);
        isMailAddressCreated = true;

      }

    }

    if (!isResAddressCreated) {
      createAddressEvidence(concernRoleID, addressData, startDate,
        CONCERNROLEADDRESSTYPE.PRIVATE, endDate, isPreferredAddress,
        comments);
    }

    if (!isMailAddressCreated && isUseResAsMailAddress) {
      createAddressEvidence(concernRoleID, addressData, startDate,
        CONCERNROLEADDRESSTYPE.PRIVATE, endDate, isPreferredAddress,
        comments);
    }

    // 101950 Updated: 247.3-202 Trigger task to update residency period
    // Create a Residence Period Update Task if the Country Changes.
    if (isResAddressCreated && !country.equals(existAddCountry)
      && (existAddCountry.equals(BDMConstants.kCANADA_ADDRESSCCOUNTRY_Code)
        || country.equals(BDMConstants.kCANADA_ADDRESSCCOUNTRY_Code))) {
      createResidencePeriodUpdateTask(concernRoleID);
    }
  }

  /**
   * 101950 Updated: 247.3-202 Trigger task to update residency period
   * create the task if there is a change in the residential Country
   *
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  public void createResidencePeriodUpdateTask(final long concernRoleID)
    throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<Object>();

    final BDMFECTaskCreateDetails enactmentDetails =
      new BDMFECTaskCreateDetails();

    final ConcernRoleIDKey concernROleIDkey = new ConcernRoleIDKey();
    concernROleIDkey.concernRoleID = concernRoleID;
    final curam.core.facade.struct.ConcernRoleNameDetails concernNameDtls =
      ConcernRoleFactory.newInstance().readConcernRoleName(concernROleIDkey);

    enactmentDetails.participantRoleID = concernRoleID;// -1991716935204601856l;
    enactmentDetails.priority = TASKPRIORITY.NORMAL;
    enactmentDetails.subject =
      "Residence period needs to be updated as residential address country was updated for "
        + concernNameDtls.concernRoleName;
    enactmentDetails.workQueueID = Long.parseLong(Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_DEFAULT_CASEOWNER_WORKQUEUE));

    enactmentStructs.add(enactmentDetails);

    EnactmentService.startProcess(
      BDMConstants.BDMRESIDENCEPERIODUPDATEWORKFLOW, enactmentStructs);

  }

  /**
   * Method modify the start and end date of the evidence.
   *
   * @param evidenceID dynamic evidence identifier
   * @param endDate evidence end date
   * @param evidenceType evidence type
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public void modifyAddressEvidenceStep1(final long concernRoleID,
    final Date startDate, final String typeCode, final Date endDate,
    final Boolean isPreferredAddress, final String comments,
    final long evidenceID, final String changeReason,
    final String bdmReceivedFrom, final String bdmReceivedFromCountry,
    final String bdmModeOfReceipt)
    throws AppException, InformationalException {

    final curam.core.sl.struct.EvidenceTypeKey eType =
      new curam.core.sl.struct.EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCADDRESS;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceID;
    eiEvidenceKey.evidenceType = PDCConst.PDCADDRESS;

    final EIEvidenceKey eiModifyEvidenceKey = new EIEvidenceKey();
    final EIEvidenceModifyDtls eiEvidenceModifyDtls =
      new EIEvidenceModifyDtls();
    EvidenceDescriptorModifyDtls evidenceDescriptorModifyDtls =
      new EvidenceDescriptorModifyDtls();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Long caseID = evidenceReadDtls.descriptor.caseID;

    eiModifyEvidenceKey.evidenceID = evidenceID;
    eiModifyEvidenceKey.evidenceType = PDCConst.PDCADDRESS;
    evidenceDescriptorModifyDtls = new EvidenceDescriptorModifyDtls();
    evidenceDescriptorModifyDtls.receivedDate = Date.getCurrentDate();
    if (StringUtil.isNullOrEmpty(changeReason)) {
      evidenceDescriptorModifyDtls.changeReason =
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    } else {
      evidenceDescriptorModifyDtls.changeReason = changeReason;
    }

    assignEvidenceDetails(concernRoleID, caseID, startDate, typeCode, endDate,
      comments, isPreferredAddress, dynamicEvidenceDataDetails,
      bdmReceivedFrom, bdmReceivedFromCountry, bdmModeOfReceipt);
    eiEvidenceModifyDtls.descriptor.assign(evidenceDescriptorModifyDtls);
    eiEvidenceModifyDtls.evidenceObject = dynamicEvidenceDataDetails;

    if (evidenceID != 0L) {
      evidenceControllerObj.modifyEvidence(eiModifyEvidenceKey,
        eiEvidenceModifyDtls);
    }

  }

}
