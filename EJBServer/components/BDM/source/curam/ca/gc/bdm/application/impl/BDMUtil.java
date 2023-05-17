package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.codetable.BDMEMAILADDRESSCHANGETYPE;
import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMPHONENUMBERCHANGETYPE;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.entity.fact.BDMFEWorkqueueProvinceLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMPhoneNumberFactory;
import curam.ca.gc.bdm.entity.fact.BDMProspectPersonFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.fact.BDMWorkqueueProvinceLinkFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMConcernRoleCommRMKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetails;
import curam.ca.gc.bdm.entity.fec.struct.BDMRecordCommunicationsTaskDetailsList;
import curam.ca.gc.bdm.entity.intf.BDMEscalationLevel;
import curam.ca.gc.bdm.entity.intf.BDMExternalParty;
import curam.ca.gc.bdm.entity.intf.BDMProspectPerson;
import curam.ca.gc.bdm.entity.intf.BDMTask;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.intf.BDMPerson;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDtls;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNum;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNumList;
import curam.ca.gc.bdm.entity.struct.BDMDynamicEvidenceDataAttributeDetails;
import curam.ca.gc.bdm.entity.struct.BDMDynamicEvidenceDataAttributeDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtls;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtlsStruct2;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelKey;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMFEWorkqueueProvinceLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEWorkqueueProvinceLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonKey;
import curam.ca.gc.bdm.entity.struct.BDMPhoneNumberDtls;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonDtls;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaskKey;
import curam.ca.gc.bdm.entity.struct.BDMWorkqueueProvinceLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMWorkqueueProvinceLinkKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardStp3DataDetails;
import curam.ca.gc.bdm.facade.bdmcommonintake.fact.BDMApplicationCaseCheckEligibilityFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAConcernRoleKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMForeignID;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.facade.integritycheck.fact.BDMIntegrityCheckFactory;
import curam.ca.gc.bdm.facade.integritycheck.struct.BDMIntegrityCheckKey;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonSearchWizardKey;
import curam.ca.gc.bdm.facade.participant.struct.BDMRegisterPersonWizardSearchDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonDemographicPageDetails;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetails;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchKey1;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyEvidenceWizardDetailsResult;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMAddressEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.ca.gc.bdm.message.BDMBPOADDRESS;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.sl.fact.BDMPDCPersonFactory;
import curam.ca.gc.bdm.sl.fact.RepresentativeFactory;
import curam.ca.gc.bdm.sl.pdc.fact.BDMPDCPhoneNumberFactory;
import curam.ca.gc.bdm.sl.pdc.intf.BDMPDCPhoneNumber;
import curam.ca.gc.bdm.sl.struct.BDMPersonIdentificationDetails;
import curam.ca.gc.bdm.sl.struct.BDMThirdPartyParticipantSearchKey;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckUtil;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ImageBuilder;
import curam.cefwidgets.docbuilder.impl.LinkBuilder;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSSTATE;
import curam.codetable.BDMCCTCOMMUNICATIONSTATUS;
import curam.codetable.BDMTASKCATEGORY;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.COMMUNICATIONTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRYCODE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.DUPLICATESTATUS;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.IEGYESNO;
import curam.codetable.PHONETYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SENSITIVITY;
import curam.codetable.impl.COUNTRYEntry;
import curam.codetable.impl.PHONETYPEEntry;
import curam.commonintake.entity.struct.AppCaseEligibilityResultKey;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseCheckEligibilityFactory;
import curam.commonintake.facade.struct.ApplicationCaseCheckEligibilityDetails;
import curam.commonintake.facade.struct.ApplicationCaseCheckEligibilityDisplayXML;
import curam.commonintake.facade.struct.ApplicationCaseCheckEligibiltyResultList;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.fact.OrganizationFactory;
import curam.core.facade.fact.ParticipantFactory;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.intf.Organization;
import curam.core.facade.struct.AlternateIDDetailsList;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.facade.struct.ICProductDeliveryDetails;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.facade.struct.ListICProductDeliveryDetails;
import curam.core.facade.struct.MaintainParticipantAddressDetails;
import curam.core.facade.struct.ParticipantCommunicationKey;
import curam.core.facade.struct.PersonSearchDetailsResult;
import curam.core.facade.struct.ReadLocationDetails;
import curam.core.facade.struct.ReadLocationKey;
import curam.core.facade.struct.RegisterPersonWizardSearchDetails;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.ConcernRolePhoneNumberFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.ProspectPersonFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.intf.AddressElement;
import curam.core.intf.AlternateName;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.ConcernRolePhoneNumber;
import curam.core.intf.Person;
import curam.core.intf.ProspectPerson;
import curam.core.intf.UniqueID;
import curam.core.intf.Users;
import curam.core.sl.entity.fact.ConcernRoleDuplicateFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleDtlsList;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.CaseParticipantRole_eoCaseIDDetails;
import curam.core.sl.entity.struct.CaseParticipantRole_eoFullDetails;
import curam.core.sl.entity.struct.DuplicateForConcernRoleDtls;
import curam.core.sl.entity.struct.DuplicateForConcernRoleDtlsList;
import curam.core.sl.entity.struct.ParticipantRoleIDAndTypeCodeKey;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDParticipantIDStatusCode;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.intf.Representative;
import curam.core.sl.struct.AllParticipantSearchDetails;
import curam.core.sl.struct.AllParticipantSearchResult;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseParticipantRoleDetails;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.ConcernRoleIDKey;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.sl.struct.RepresentativeRegistrationDetails;
import curam.core.sl.struct.ViewCaseParticipantRoleDetailsList;
import curam.core.sl.struct.ViewCaseParticipantRole_boKey;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.AddressReadMultiDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.AlternateIDReadmultiDtls;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameKey;
import curam.core.struct.CaseCount;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleAlternateIDDtlsList;
import curam.core.struct.ConcernRoleAlternateIDDtlsStruct1;
import curam.core.struct.ConcernRoleAlternateIDKeyStruct1;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleIDTypeStatusEndDateKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRolePhoneNumberDtls;
import curam.core.struct.ConcernRolePhoneNumberKey;
import curam.core.struct.ElementDetails;
import curam.core.struct.LayoutKey;
import curam.core.struct.LocaleStruct;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.PhoneConcernRoleIDTypeDtls;
import curam.core.struct.PhoneConcernRoleIDTypeDtlsList;
import curam.core.struct.PhoneConcernRoleIDTypeKey;
import curam.core.struct.PhoneNumberDtls;
import curam.core.struct.ProspectPersonDtls;
import curam.core.struct.ProspectPersonKey;
import curam.core.struct.UserRoleDetails;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.creole.value.CodeTableItem;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.datastore.impl.EntityType;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataIDAndAttrKey;
import curam.dynamicevidence.sl.entity.struct.EvidenceIDDetails;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.events.TASK;
import curam.message.BPOPARTICIPANT;
import curam.pdc.base.PDCPhoneNumber;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.fact.PDCAlternateNameFactory;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.fact.ParticipantDataCaseFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.impl.PDCPhoneNumberEvidencePopulator;
import curam.pdc.intf.PDCAlternateName;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.ParticipantAlternateNameDetails;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetailsList;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.internal.workflow.fact.ActivityInstanceFactory;
import curam.util.internal.workflow.intf.ActivityInstance;
import curam.util.internal.workflow.struct.ActivityInstanceDtlsList;
import curam.util.internal.workflow.struct.ProcessInstanceKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.StringHelper;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssocSearchDetails;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.BizObjectTypeKey;
import curam.util.workflow.struct.TaskKey;
import curam.workspaceservices.applicationprocessing.impl.AddressMappingStrategy;
import curam.workspaceservices.util.impl.DatastoreHelper;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.transform.TransformerException;
import org.apache.xerces.impl.xpath.regex.RegularExpression;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static curam.core.fact.ConcernRoleFactory.newInstance;

/**
 *
 * Util Class to implement generic logic for BDM POroject
 *
 * @author teja.konda
 *
 */
public class BDMUtil {

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private Set<PDCPhoneNumberEvidencePopulator> pdcPhoneNumberEvidencePopulators;

  @Inject
  private AddressMappingStrategy addressMappingStrategy;

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvidence;

  static final String participantAttr = "participant";

  static final String phoneCountryCodeAttr = "phoneCountryCode";

  static final String phoneAreaCodeAttr = "phoneAreaCode";

  static final String phoneNumberAttr = "phoneNumber";

  static final String phoneExtensionAttr = "phoneExtension";

  static final String fromDateAttr = "fromDate";

  static final String toDateAttr = "toDate";

  static final String phoneTypeAttr = "phoneType";

  static final String commentsAttr = "comments";

  static final String preferredIndAttr = "preferredInd";

  static final String stringTrue = "true";

  static final String countryUS = "US";

  static final String countryCA = "CA";

  public static Map<String, String> cctDescriptionMap = null;

  public static HashMap<String, String> cctStatusToBDMStatusMap = null;

  public static HashMap<String, Integer> interimApplicationTemplateMap = null;

  static final String emailAddressTypeAttr = "emailAddressType";

  static final String ADD2 = "ADD2";

  private static String ERR_INVALID_TRACKING_NUM = "Invalid Tracking Number:";

  private static String reqSIN = "reqSIN";

  private static String CODE = " code: ";

  static final String equalSign = "=";

  BDMCommunicationHelper bdmCommunicationHelper =
    new BDMCommunicationHelper();

  public static String GET_LIST_OF_FOREIGNID_SQL =
    " SELECT  DISTINCT c2.ALTERNATEID,  c2.CONCERNROLEALTERNATEID    "
      + "INTO :alternateID, :concernRoleAlternateID "
      + " FROM CONCERNROLE c , EVIDENCEDESCRIPTOR e,  CONCERNROLEALTERNATEID c2 "
      + " WHERE  c.CONCERNROLEID = e.PARTICIPANTID AND c2.CONCERNROLEID = e.PARTICIPANTID  "
      + " AND e.EVIDENCETYPE = 'PDC0000259' AND c2.STATUSCODE = '"
      + RECORDSTATUS.NORMAL + "'" + " AND c2.TYPECODE = '"
      + CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER
      + "' AND c.CONCERNROLEID =";

  private static String countAppealCasesByParticipantID =
    "SELECT   COUNT(*)  into :value  FROM   CaseParticipantRole,  CaseHeader  "
      + " WHERE   CaseParticipantRole.participantRoleID = :concernRoleID "
      + " AND   (  CaseParticipantRole.typeCode = 'APP' OR   CaseParticipantRole.typeCode = 'RSP'   ) "
      + " AND  CaseParticipantRole.recordStatus = 'RST1' AND  CaseHeader.caseID = CaseParticipantRole.caseID "
      + " AND  CaseHeader.caseTypeCode = 'CT8' "
      + " AND CaseHeader.statuscode <> 'CS3' and CaseHeader.statuscode <> 'CS11' ";

  private static String foundOnlyResidentialAddressEnded =
    "Select crd.addressid, crd.typecode, crd.startdate, crd.enddate "
      + "INTO :addressid, :typecode, :startdate, :enddate  "
      + "from    evidencedescriptor ed, concernrole cr,    concernroleaddress crd "
      + " where    cr.concernroleid=ed.participantid "
      + " and cr.concernroleid=crd.concernroleid "
      + " and   ed.evidencedescriptorid=";

  private static String isDependantClaimedOnAnotherCase_SelectSTmt =
    "SELECT  DA_STARTDATE.value,DA_ENDDATE.value, DA_CLAIMDEPENDANT.value INTO :statusCode, :correctionSetID, :evidenceType FROM EVIDENCEDESCRIPTOR ed";

  private static String isDependantClaimedOnAnotherCase_InnerJoin =
    " INNER JOIN caseheader ch ON ed.CASEID = ch.caseid AND ch.CASETYPECODE  IN ('";

  private static String isDependantClaimedOnAnotherCase_conditionCheckRoleType =
    " INNER JOIN CASEPARTICIPANTROLE cpr ON cpr.caseid = ch.caseid and cpr.TYPECODE IN ('PRI','MEM','RPR') AND cpr.recordstatus ='RST1' AND cpr.PARTICIPANTROLEID = ";

  private static String isDependantClaimedOnAnotherCase_condition2 =
    " INNER JOIN DYNAMICEVIDENCEDATAATTRIBUTE da ON ed.RELATEDID = da.EVIDENCEID AND da.name='dependentCaseParticipant' AND da.value = cpr.CASEPARTICIPANTROLEID "
      + " INNER JOIN DYNAMICEVIDENCEDATAATTRIBUTE da_startDate ON da.EVIDENCEID  = DA_STARTDATE.evidenceID AND da_startDate.name='startDate'"
      + " INNER JOIN DYNAMICEVIDENCEDATAATTRIBUTE da_endDate ON da.EVIDENCEID  = DA_endDate.evidenceID AND da_endDate.name='endDate' "
      + "INNER JOIN DYNAMICEVIDENCEDATAATTRIBUTE da_claimDependant ON da.EVIDENCEID  = DA_claimDependant.evidenceID AND da_claimDependant.name='claimDependant'";

  private static String isDependantClaimedOnAnotherCase_condition1 =
    " INNER JOIN CASERELATIONSHIP cr ON ( ed.CASEID = cr.CASEID AND ed.SOURCECASEID = cr.RELATEDCASEID )"
      + "  OR ( ed.CASEID = cr.RELATEDCASEID AND ed.SOURCECASEID = cr.CASEID  )";

  private static String findActiveForeignLiaison =
    "SELECT " + "   FOREIGNLIAISONID " + "INTO " + "   :foreignLiaisonID "
      + "FROM " + "   BDMFOREIGNLIAISON b " + "WHERE  b.RECORDSTATUS ='RST1' "
      + " AND  b.FOREIGNAPPIDS LIKE ";

  public BDMUtil() {

    GuiceWrapper.getInjector().injectMembers(this);

  }

  /**
   *
   * Method to filter address by Unit Number
   * OOTB has hard coded Address search to Street Name and Street Number
   *
   * @param registerPersonWizardSearchDetails
   * @param unitNumber
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public RegisterPersonWizardSearchDetails filterAddress(
    final RegisterPersonWizardSearchDetails registerPersonWizardSearchDetails,
    final String unitNumber) throws AppException, InformationalException {

    RegisterPersonWizardSearchDetails registerPersonWizardSearchDtls =
      new RegisterPersonWizardSearchDetails();

    try {
      // initilization of variables
      final ConcernRoleAddress concernroleAddressObj =
        ConcernRoleAddressFactory.newInstance();
      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();
      final ConcernRoleKey concernroleKey = new ConcernRoleKey();

      final AddressElement addressElement =
        AddressElementFactory.newInstance();
      final AddressKey addressKey = new AddressKey();
      AddressElementDtlsList addressElementDtlsList =
        new AddressElementDtlsList();
      Boolean isMatched = false;
      String streetName = "";

      // for each list of matched records, further match with Unit Number
      for (int i =
        0; i < registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
          .size(); i++) {

        // get AddressID of current ConcernroleID
        concernroleKey.concernRoleID =
          registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
            .get(i).concernRoleID;
        concernRoleAddressKey.concernRoleAddressID =
          concernroleAddressObj.readConcernRoleAddressDetailsByConcernRoleID(
            concernroleKey).concernRoleAddressID;
        addressKey.addressID =
          concernroleAddressObj.read(concernRoleAddressKey).addressID;

        // Read Address Elements for a given Address ID
        addressElementDtlsList =
          addressElement.readAddressElementDetails(addressKey);

        // matching unit Number entered on the screen to unit number of OOTB
        // Search Results
        for (int j = 0; j < addressElementDtlsList.dtls.size(); j++) {

          if (addressElementDtlsList.dtls.get(j).elementType
            .equals(ADDRESSELEMENTTYPE.APT)
            && addressElementDtlsList.dtls.get(j).upperElementValue
              .equals(unitNumber)) {

            isMatched = true;

          } else if (addressElementDtlsList.dtls.get(j).elementType
            .equals(ADD2)) { // reading street name to replace street Name
                             // with Unit Number on display

            streetName = addressElementDtlsList.dtls.get(j).elementValue;
          }

        }

        if (isMatched) { // if the record is matched add it to search results

          if (!streetName.isEmpty()) { // replace street Name with unit number
                                       // on display results

            registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
              .get(i).formattedAddress =
                registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
                  .get(i).formattedAddress.replace(streetName,
                    unitNumber.toLowerCase());
          }

          // add matched record to return list
          registerPersonWizardSearchDtls.searchResult.personSearchResult.dtlsList
            .add(
              registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
                .get(i));

        }

      }
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_ADDRESS_FILTER + e);
      // if there is any exception then return the original list
      registerPersonWizardSearchDtls = registerPersonWizardSearchDetails;

    }

    return registerPersonWizardSearchDtls;
  }

  /**
   * util method to create Phone Number Evidence
   *
   * @param details
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ConcernRolePhoneNumberKey createPhoneNumberEvidence(
    final ParticipantPhoneDetails details, final String countryCode)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = details.concernRoleID;
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCPHONENUMBER;
    final EvidenceTypeDef evidenceType =
      this.etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);
    final EvidenceTypeVersionDef evTypeVersion =
      this.etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);
    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute(participantAttr);
    DynamicEvidenceTypeConverter.setAttribute(participant,
      Long.valueOf(pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID));
    assignEvidenceDetails(pdcCaseIDCaseParticipantRoleID, details,
      dynamicEvidenceDataDetails, countryCode);
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();
    evidenceDescriptorInsertDtls.participantID = details.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = details.concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;
    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
    final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
      new ConcernRolePhoneNumberKey();
    final Object facadeScopeObject =
      TransactionInfo.getFacadeScopeObject(PDCPhoneNumber.class.getName());
    if (facadeScopeObject != null)
      concernRolePhoneNumberKey.concernRolePhoneNumberID =
        ((Long) facadeScopeObject).longValue();
    return concernRolePhoneNumberKey;
  }

  /**
   * Assigning phone Number evidence details from registration screen
   *
   * @param pdcCaseIDCaseParticipantRoleID
   * @param details
   * @param dynamicEvidenceDataDetails
   * @param countryCode
   * @throws AppException
   * @throws InformationalException
   */
  private void assignEvidenceDetails(
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID,
    final ParticipantPhoneDetails details,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String countryCode) throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails phoneCountryCode =
      dynamicEvidenceDataDetails.getAttribute(phoneCountryCodeAttr);
    DynamicEvidenceTypeConverter.setAttribute(phoneCountryCode,
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, countryCode));
    final DynamicEvidenceDataAttributeDetails phoneAreaCode =
      dynamicEvidenceDataDetails.getAttribute(phoneAreaCodeAttr);
    DynamicEvidenceTypeConverter.setAttribute(phoneAreaCode,
      details.phoneAreaCode);
    final DynamicEvidenceDataAttributeDetails phoneNumber =
      dynamicEvidenceDataDetails.getAttribute(phoneNumberAttr);
    DynamicEvidenceTypeConverter.setAttribute(phoneNumber,
      details.phoneNumber);
    final DynamicEvidenceDataAttributeDetails phoneExtension =
      dynamicEvidenceDataDetails.getAttribute(phoneExtensionAttr);
    DynamicEvidenceTypeConverter.setAttribute(phoneExtension,
      details.phoneExtension);
    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(fromDateAttr);
    if (!details.startDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(fromDate, details.startDate);
    } else {
      fromDate.setValue("");
    }
    final DynamicEvidenceDataAttributeDetails endDate =
      dynamicEvidenceDataDetails.getAttribute(toDateAttr);
    if (!details.endDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(endDate, details.endDate);
    } else {
      endDate.setValue("");
    }
    final DynamicEvidenceDataAttributeDetails phoneType =
      dynamicEvidenceDataDetails.getAttribute(phoneTypeAttr);
    DynamicEvidenceTypeConverter.setAttribute(phoneType,
      new CodeTableItem(PHONETYPE.TABLENAME, details.typeCode));
    final DynamicEvidenceDataAttributeDetails comments =
      dynamicEvidenceDataDetails.getAttribute(commentsAttr);
    DynamicEvidenceTypeConverter.setAttribute(comments, details.comments);
    final DynamicEvidenceDataAttributeDetails preferredAddressInd =
      dynamicEvidenceDataDetails.getAttribute(preferredIndAttr);
    if (details.primaryPhoneInd) {
      DynamicEvidenceTypeConverter.setAttribute(preferredAddressInd,
        Boolean.valueOf(true));
    } else if (details.concernRolePhoneNumberID != 0L) {
      final ConcernRolePhoneNumberKey concernRolePhoneNumberKey =
        new ConcernRolePhoneNumberKey();
      concernRolePhoneNumberKey.concernRolePhoneNumberID =
        details.concernRolePhoneNumberID;
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      final ConcernRolePhoneNumberDtls storedConcernRolePhoneNumberDtls =
        ConcernRolePhoneNumberFactory.newInstance().read(notFoundIndicator,
          concernRolePhoneNumberKey);
      if (!notFoundIndicator.isNotFound()) {
        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
        concernRoleKey.concernRoleID = details.concernRoleID;
        final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
        final ConcernRoleDtls concernRoleDtls =
          concernRoleObj.read(concernRoleKey);
        if (concernRoleDtls.primaryPhoneNumberID == storedConcernRolePhoneNumberDtls.phoneNumberID)
          DynamicEvidenceTypeConverter.setAttribute(preferredAddressInd,
            Boolean.valueOf(true));
      }
    }
    if (!this.pdcPhoneNumberEvidencePopulators.isEmpty()) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = details.concernRoleID;
      final CaseIDKey caseIDKey = new CaseIDKey();
      caseIDKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
      final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
        new ConcernRolePhoneNumberDtls();
      concernRolePhoneNumberDtls.assign(details);
      final PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();
      phoneNumberDtls.assign(details);
      for (final PDCPhoneNumberEvidencePopulator pdcPhoneNumberEvidencePopulator : this.pdcPhoneNumberEvidencePopulators)
        pdcPhoneNumberEvidencePopulator.populate(concernRoleKey, caseIDKey,
          concernRolePhoneNumberDtls, phoneNumberDtls,
          dynamicEvidenceDataDetails);
    }
  }

  /**
   *
   * @param personSearchDtls
   * @param unitApt
   * @return
   */
  public PersonSearchDetailsResult filterAddressForPerson(
    final PersonSearchDetailsResult personSearchDetailsResultDtls,
    final String unitApt) throws AppException, InformationalException {

    PersonSearchDetailsResult personSearchDetailsResult =
      new PersonSearchDetailsResult();

    try {
      // initilization of variables
      final ConcernRoleAddress concernroleAddressObj =
        ConcernRoleAddressFactory.newInstance();
      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();
      final ConcernRoleKey concernroleKey = new ConcernRoleKey();

      final AddressElement addressElement =
        AddressElementFactory.newInstance();
      final AddressKey addressKey = new AddressKey();
      AddressElementDtlsList addressElementDtlsList =
        new AddressElementDtlsList();
      Boolean isMatched = false;
      String streetName = "";

      // for each list of matched records, further match with Unit Number
      for (int i =
        0; i < personSearchDetailsResultDtls.personSearchResult.dtlsList
          .size(); i++) {

        // get AddressID of current ConcernroleID
        concernroleKey.concernRoleID =
          personSearchDetailsResultDtls.personSearchResult.dtlsList
            .get(i).concernRoleID;
        concernRoleAddressKey.concernRoleAddressID =
          concernroleAddressObj.readConcernRoleAddressDetailsByConcernRoleID(
            concernroleKey).concernRoleAddressID;
        addressKey.addressID =
          concernroleAddressObj.read(concernRoleAddressKey).addressID;

        // Read Address Elements for a given Address ID
        addressElementDtlsList =
          addressElement.readAddressElementDetails(addressKey);

        // matching unit Number entered on the screen to unit number of OOTB
        // Search Results
        for (int j = 0; j < addressElementDtlsList.dtls.size(); j++) {

          if (addressElementDtlsList.dtls.get(j).elementType
            .equals(ADDRESSELEMENTTYPE.APT)
            && addressElementDtlsList.dtls.get(j).upperElementValue
              .equals(unitApt)) {

            isMatched = true;

          } else if (addressElementDtlsList.dtls.get(j).elementType
            .equals(ADD2)) { // reading street name to replace street Name
                             // with Unit Number on display

            streetName = addressElementDtlsList.dtls.get(j).elementValue;
          }

        }

        if (isMatched) { // if the record is matched add it to search results

          if (!streetName.isEmpty()) { // replace street Name with unit number
                                       // on display results

            personSearchDetailsResultDtls.personSearchResult.dtlsList
              .get(i).formattedAddress =
                personSearchDetailsResultDtls.personSearchResult.dtlsList
                  .get(i).formattedAddress.replace(streetName,
                    unitApt.toLowerCase());
          }

          // add matched record to return list
          personSearchDetailsResult.personSearchResult.dtlsList.add(
            personSearchDetailsResultDtls.personSearchResult.dtlsList.get(i));

        }

      }
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_ADDRESS_FILTER + e);
      // if there is any exception then return the original list
      personSearchDetailsResult = personSearchDetailsResultDtls;

    }

    return personSearchDetailsResult;

  }

  /**
   * customized OOTB Class to add new attributes to CP evidence
   *
   * @since TASK-9375
   * @param concernRoleID
   * @param personEntity
   * @param personDetails
   * @throws AppException
   * @throws InformationalException
   */
  public void createContactPreferenceEvidence(final long concernRoleID,
    final String preferredOralLang, final String preferredWrittenLang,
    String commMethod) throws AppException, InformationalException {

    if (commMethod.isEmpty()) {

      commMethod = BDMCORRESDELIVERY.POSTALDIG;

    }

    // get latest evidnce type version
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    final EvidenceTypeDef evidenceType =
      this.etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);
    final EvidenceTypeVersionDef evTypeVersion =
      this.etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

    // set dynamic values for evidence
    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute(participantAttr);
    DynamicEvidenceTypeConverter.setAttribute(participant,
      Long.valueOf(pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID));

    // prefered oral lang
    final DynamicEvidenceDataAttributeDetails preferredOralLanguage =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kpreferredOralLanguage);
    DynamicEvidenceTypeConverter.setAttribute(preferredOralLanguage,
      new CodeTableItem(BDMLANGUAGE.TABLENAME, preferredOralLang));

    // prefered written lang
    final DynamicEvidenceDataAttributeDetails preferredWrittenLanguage =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kpreferredWrittenLanguage);
    DynamicEvidenceTypeConverter.setAttribute(preferredWrittenLanguage,
      new CodeTableItem(BDMLANGUAGE.TABLENAME, preferredWrittenLang));
    // prefered comm methods
    final DynamicEvidenceDataAttributeDetails preferredcommMethod =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kpreferredCommunicationMethod);
    DynamicEvidenceTypeConverter.setAttribute(preferredcommMethod,
      new CodeTableItem(BDMCORRESDELIVERY.TABLENAME, commMethod));

    // calling OOTB Evidence creation API
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();
    evidenceDescriptorInsertDtls.participantID = concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;
    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

  }

  /**
   * util method to fetch caseparticipantroleID by caseiD and ConcernroleID
   *
   * @param caseID
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CaseParticipantRoleIDStruct
    getCaseParticipantRoleID(final long caseID, final long concernRoleID)
      throws AppException, InformationalException {

    final CaseParticipantRole caseParticipantRole =
      CaseParticipantRoleFactory.newInstance();

    final CaseParticipantRoleIDStruct caseParticipantRoleIDStruct =
      new CaseParticipantRoleIDStruct();

    final ViewCaseParticipantRole_boKey viewCaseParticipantRole_boKey =
      new ViewCaseParticipantRole_boKey();
    viewCaseParticipantRole_boKey.showOnlyActive = true;
    viewCaseParticipantRole_boKey.dtls.caseID = caseID;

    final ViewCaseParticipantRoleDetailsList caseMemberList =
      caseParticipantRole.viewCaseMemberList(viewCaseParticipantRole_boKey);

    for (final CaseParticipantRole_eoFullDetails caseMember : caseMemberList.dtls) {
      if (caseMember.typeCode.equals(CASEPARTICIPANTROLETYPE.MEMBER)
        || caseMember.typeCode.equals(CASEPARTICIPANTROLETYPE.PRIMARY)
        || caseMember.typeCode
          .equals(CASEPARTICIPANTROLETYPE.RELATEDPERSON)) {
        if (caseMember.participantRoleID == concernRoleID) {
          caseParticipantRoleIDStruct.caseParticipantRoleID =
            caseMember.caseParticipantRoleID;
        }
      }
    }
    return caseParticipantRoleIDStruct;
  }

  /**
   * util method to fetch caseiD by caseParticipantRoleID
   *
   * @param caseID
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static long
    getCaseIDfromCaseParticipantRoleID(final long caseParticipantRoleID)
      throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final curam.core.sl.entity.intf.CaseParticipantRole caseParticipantRoleObj =
      curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance();
    final CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();
    caseParticipantRoleKey.caseParticipantRoleID = caseParticipantRoleID;
    final CaseParticipantRole_eoCaseIDDetails caseParticipantRoleDetails =
      caseParticipantRoleObj.readCaseID(notFoundIndicator,
        caseParticipantRoleKey);
    if (notFoundIndicator.isNotFound()) {
      return 0l;
    }
    return caseParticipantRoleDetails.caseID;
  }

  /*
   * Helper method to get the SIN Number
   *
   */

  public String getSINNumberForPerson(final long concernRoleID)
    throws AppException, InformationalException {

    String sinNumber = "";

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMSINIDENTIFICATIONSTATUS;

    final DynamicEvidenceDataAttribute dynamicEvidenceDataAttribute =
      DynamicEvidenceDataAttributeFactory.newInstance();
    final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();

    PDCEvidenceDetailsList pdcEvidenceList = new PDCEvidenceDetailsList();
    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = concernRoleID;
    pdcEvidenceList = pdcPersonObj.listEvidenceForParticipant(personKey);
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
    idAndAttrKey.name = reqSIN;
    final DynamicEvidenceDataAttributeDtlsList dataAttributeDtlsList =
      dynamicEvidenceDataAttribute
        .searchByEvidenceIDAndAttribute(idAndAttrKey);

    if (!dataAttributeDtlsList.dtls.isEmpty()) {

      DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls =
        new DynamicEvidenceDataAttributeDtls();
      dynamicEvidenceDataAttributeDtls = dataAttributeDtlsList.dtls.get(0);
      sinNumber = dynamicEvidenceDataAttributeDtls.value;
    }

    return sinNumber;
  }

  /**
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getPersonReferenceNumber(final long concernRoleID)
    throws AppException, InformationalException {

    String strIdentificationRefernce = CuramConst.gkEmpty;

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey2 =
      new ConcernRoleIDStatusCodeKey();
    concernRoleIDStatusCodeKey2.dtls.concernRoleID = concernRoleID;
    concernRoleIDStatusCodeKey2.dtls.statusCode = RECORDSTATUS.NORMAL;
    final AlternateIDDetailsList alternateIDDetailsList =
      curam.core.facade.fact.ConcernRoleAlternateIDFactory.newInstance()
        .searchByConcernRoleIDAndStatus(concernRoleIDStatusCodeKey2);

    final int crnRlAltIDLstSize = alternateIDDetailsList.dtls.dtls.size();
    AlternateIDReadmultiDtls alternateIDReadmultiDtls = null;
    for (int q = 0; q < crnRlAltIDLstSize; q++) {
      alternateIDReadmultiDtls = new AlternateIDReadmultiDtls();
      alternateIDReadmultiDtls = alternateIDDetailsList.dtls.dtls.item(q);
      if (alternateIDReadmultiDtls.typeCode
        .equals(CONCERNROLEALTERNATEID.REFERENCE_NUMBER)) {
        strIdentificationRefernce =
          alternateIDReadmultiDtls.alternateID.toString();
      }
    }

    return strIdentificationRefernce;
  }

  /*
   * Helper method to get the SIN Number
   *
   */

  public DynamicEvidenceDataAttributeDtlsList
    getMaritalStatusEvidenceForPerson(final long concernRoleID)
      throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final DynamicEvidenceDataAttribute dynamicEvidenceDataAttribute =
      DynamicEvidenceDataAttributeFactory.newInstance();
    final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();

    PDCEvidenceDetailsList pdcEvidenceList = new PDCEvidenceDetailsList();
    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = concernRoleID;
    pdcEvidenceList = pdcPersonObj.listEvidenceForParticipant(personKey);
    for (int i = 0; i < pdcEvidenceList.list.size(); i++) {
      if (eType.evidenceType
        .equalsIgnoreCase(pdcEvidenceList.list.get(i).evidenceType)) {
        evidenceIDDetails.evidenceID = pdcEvidenceList.list.get(i).evidenceID;
        break;
      }
    }
    final EvidenceIDDetails details = new EvidenceIDDetails();
    details.evidenceID = evidenceIDDetails.evidenceID;
    final DynamicEvidenceDataAttributeDtlsList dataAttributeDtlsList =
      dynamicEvidenceDataAttribute.searchByEvidenceID(details);

    return dataAttributeDtlsList;
  }

  /**
   * Helper method to filter search results by province, postal and country
   * 8914 person search extension
   *
   * @param personSearchDetailsResultDtls
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMPersonSearchDetailsResult filterAddressForPerson(
    final BDMPersonSearchDetailsResult personSearchDetailsResultDtls,
    final BDMPersonSearchKey1 key)
    throws AppException, InformationalException {

    final String province = key.stateProvince.trim().toUpperCase();
    final String country = key.countryCode.toUpperCase();
    final String postalCode = key.postalCode.trim().toUpperCase();

    // START: Task 93506: DEV: Address Format Search updates
    final String unitNumber = key.unitNumber.toUpperCase();
    Boolean isUnitNumberMatched = false;
    // END: Task 93506: DEV: Address Format Search updates
    Boolean isProvinceMatched = false;
    Boolean isCountryMatched = false;
    Boolean isPostalCodeMatched = false;

    final Set<Long> uniqueConcernRoleIDs = new HashSet<Long>();
    BDMPersonSearchDetailsResult personSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    try {
      // initilization of variables
      final ConcernRoleAddress concernroleAddressObj =
        ConcernRoleAddressFactory.newInstance();
      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();
      final ConcernRoleKey concernroleKey = new ConcernRoleKey();

      final AddressElement addressElement =
        AddressElementFactory.newInstance();
      final AddressKey addressKey = new AddressKey();
      AddressElementDtlsList addressElementDtlsList =
        new AddressElementDtlsList();
      final CodeTableAdmin codeTableAdminObj =
        CodeTableAdminFactory.newInstance();

      final Person personObj = PersonFactory.newInstance();
      PersonKey personKey = new PersonKey();
      PersonDtls personDtls = new PersonDtls();
      AlternateNameDtls nameDtls = new AlternateNameDtls();
      AlternateNameKey nameKey = new AlternateNameKey();
      final AlternateName alternateNameObj =
        AlternateNameFactory.newInstance();
      final ProspectPerson prospectPersonObj =
        ProspectPersonFactory.newInstance();
      ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
      ProspectPersonDtls prospectPersonDtls = new ProspectPersonDtls();

      // for each list of matched records, further match with Unit Number
      for (final BDMPersonSearchDetails personSearchResult : personSearchDetailsResultDtls.personSearchResult.dtlsList) {

        // isProvinceMatched = false;
        // isCountryMatched = false;
        // isPostalCodeMatched = false;
        // // START: Task 93506: DEV: Address Format Search updates
        // isUnitNumberMatched = false;
        // // END: Task 93506: DEV: Address Format Search updates

        // tmpPostCodeZip = "";
        // get AddressID of current ConcernroleID
        concernroleKey.concernRoleID = personSearchResult.concernRoleID;
        concernRoleAddressKey.concernRoleAddressID =
          concernroleAddressObj.readConcernRoleAddressDetailsByConcernRoleID(
            concernroleKey).concernRoleAddressID;
        addressKey.addressID =
          concernroleAddressObj.read(concernRoleAddressKey).addressID;

        // Read person name details
        final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
        concernRoleKey.concernRoleID = concernroleKey.concernRoleID;

        final ConcernRoleDtls concernRoleDtls =
          concernRoleObj.read(concernRoleKey);

        nameKey = new AlternateNameKey();
        personSearchResult.concernRoleType = concernRoleDtls.concernRoleType;

        // Populate Reference number
        personSearchResult.referenceNum = concernRoleDtls.primaryAlternateID;

        if (CONCERNROLETYPE.PERSON.equals(concernRoleDtls.concernRoleType)) {
          personKey = new PersonKey();
          personKey.concernRoleID = concernroleKey.concernRoleID;
          personDtls = new PersonDtls();
          personDtls = personObj.read(personKey);
          nameKey.alternateNameID = personDtls.primaryAlternateNameID;
        } else if (CONCERNROLETYPE.PROSPECTPERSON
          .equals(concernRoleDtls.concernRoleType)) {
          prospectPersonKey = new ProspectPersonKey();
          prospectPersonKey.concernRoleID = concernroleKey.concernRoleID;
          prospectPersonDtls = new ProspectPersonDtls();
          prospectPersonDtls = prospectPersonObj.read(prospectPersonKey);
          nameKey.alternateNameID = prospectPersonDtls.primaryAlternateNameID;
        }
        nameDtls = new AlternateNameDtls();
        nameDtls = alternateNameObj.read(nameKey);

        // Set return firstForeName
        personSearchResult.firstForename = nameDtls.firstForename;

        // Set return surName
        personSearchResult.lastName = nameDtls.surname;

        // Read Address Elements for a given Address ID
        addressElementDtlsList =
          addressElement.readAddressElementDetails(addressKey);

        // Search Results
        for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {

          isPostalCodeMatched = checkLine1AndLine2Andcity(addressElementDtls,
            personSearchResult, postalCode, isPostalCodeMatched);

          isCountryMatched = checkCountry(addressElementDtls,
            personSearchResult, postalCode, country, isCountryMatched);

          isPostalCodeMatched = checkPostCodeAndZip(addressElementDtls,
            postalCode, personSearchResult, isPostalCodeMatched);

          isProvinceMatched = checkProvince(addressElementDtls, province,
            codeTableAdminObj, personSearchResult, isProvinceMatched);

          isProvinceMatched = checkState(addressElementDtls, province,
            codeTableAdminObj, personSearchResult, isProvinceMatched);

          isProvinceMatched = checkBdmStProv(addressElementDtls,
            personSearchResult, province, isProvinceMatched);

          isUnitNumberMatched = checkAPT(addressElementDtls,
            personSearchResult, unitNumber, isUnitNumberMatched);

        }

        if (!province.isEmpty() && !isProvinceMatched) {
          continue;
        }
        if (!country.isEmpty() && !isCountryMatched) {
          continue;
        }
        if (!postalCode.isEmpty() && !isPostalCodeMatched) {
          continue;
        }
        // START: Task 93506: DEV: Address Format Search updates
        if (!unitNumber.isEmpty() && !isUnitNumberMatched) {
          continue;
        }
        // END: Task 93506: DEV: Address Format Search updates

        // Populate SIN number
        personSearchResult.sinNumber =
          getAlternateIDByConcernRoleIDType(personSearchResult.concernRoleID,
            CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);

        // Set province
        // personSearchResult.stateOrProvince = tmpProvinceState;

        // Set return postal code
        // personSearchResult.postalOrZipCode = tmpPostCodeZip;

        if (uniqueConcernRoleIDs.contains(personSearchResult.concernRoleID)) {
          continue;
        } else {
          uniqueConcernRoleIDs.add(personSearchResult.concernRoleID);
        }

        // add matched record to return list
        personSearchDetailsResult.personSearchResult.dtlsList
          .add(personSearchResult);

      }

    } catch (

    final Exception e) {

      Trace.kTopLevelLogger.info(
        BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_ADDRESS_FILTER + e);
      // if there is any exception then return the original list
      personSearchDetailsResult = personSearchDetailsResultDtls;

    }

    return personSearchDetailsResult;

  }

  /**
   * Helper method to filter search results by province, postal and country
   * 8914 person search extension
   *
   * @param registerPersonWizardSearchDetails
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  private boolean checkAPT(final AddressElementDtls addressElementDtls,
    final BDMPersonSearchDetails personSearchResult, final String unitNumber,
    Boolean isUnitNumberMatched) {

    if (addressElementDtls.elementType.equals(ADDRESSELEMENTTYPE.APT)) {

      personSearchResult.unitNumber = addressElementDtls.upperElementValue;
      if (!unitNumber.isEmpty()
        && addressElementDtls.upperElementValue.equals(unitNumber)) {
        isUnitNumberMatched = true;
      }

    }
    return isUnitNumberMatched;
  }

  private Boolean checkBdmStProv(final AddressElementDtls addressElementDtls,
    final BDMPersonSearchDetails personSearchResult, final String province,
    Boolean isProvinceMatched) {

    if (addressElementDtls.elementType
      .equals(ADDRESSELEMENTTYPE.BDMSTPROV_X)) {
      // Set return provinceState
      if (personSearchResult.stateOrProvince.length() == 0) {
        personSearchResult.stateOrProvince =
          addressElementDtls.upperElementValue;
      }

      if (!province.isEmpty()) {
        // Added for international addresses
        if (addressElementDtls.upperElementValue.contains(province)) {
          isProvinceMatched = true;

        }
      }

    }
    return isProvinceMatched;
  }

  private Boolean checkLine1AndLine2Andcity(
    final AddressElementDtls addressElementDtls,
    final BDMPersonSearchDetails personSearchResult, final String postalCode,
    Boolean isPostalCodeMatched) {

    if (addressElementDtls.elementType.equals(ADDRESSELEMENTTYPE.LINE1)) {
      // Set return street number
      personSearchResult.streetNumber = addressElementDtls.upperElementValue;

      // START: Task 93506: DEV: Address Format Search updates
      if (!postalCode.isEmpty()
        && addressElementDtls.upperElementValue.contains(postalCode)) {
        isPostalCodeMatched = true;
      }
      // END: Task 93506: DEV: Address Format Search updates

    } else if (addressElementDtls.elementType
      .equals(ADDRESSELEMENTTYPE.LINE2)) {
      // Set return street name
      personSearchResult.streetName = addressElementDtls.upperElementValue;

      // START: Task 93506: DEV: Address Format Search updates
      if (!postalCode.isEmpty()
        && addressElementDtls.upperElementValue.contains(postalCode)) {
        isPostalCodeMatched = true;
      }
      // END: Task 93506: DEV: Address Format Search updates

    } else if (addressElementDtls.elementType
      .equals(ADDRESSELEMENTTYPE.CITY)) {
      // Set return city
      personSearchResult.addressCity = addressElementDtls.upperElementValue;

      // START: Task 93506: DEV: Address Format Search updates
      if (!postalCode.isEmpty()
        && addressElementDtls.upperElementValue.contains(postalCode)) {
        isPostalCodeMatched = true;
      }
      // END: Task 93506: DEV: Address Format Search updates

    }
    return isPostalCodeMatched;
  }

  private Boolean checkCountry(final AddressElementDtls addressElementDtls,
    final BDMPersonSearchDetails personSearchResult, final String postalCode,
    final String country, Boolean isCountryMatched) {

    if (addressElementDtls.elementType.equals(ADDRESSELEMENTTYPE.COUNTRY)) {
      // Set return country
      personSearchResult.countryCodeDesc =
        addressElementDtls.upperElementValue;
      if (!country.isEmpty()
        && addressElementDtls.upperElementValue.equals(country)) {
        isCountryMatched = true;
      }
    }
    return isCountryMatched;
  }

  private Boolean checkPostCodeAndZip(
    final AddressElementDtls addressElementDtls, final String postalCode,

    final BDMPersonSearchDetails personSearchResult,
    Boolean isPostalCodeMatched) {

    if (addressElementDtls.elementType.equals(ADDRESSELEMENTTYPE.POSTCODE)) {

      // Start - Fix for Bug 68757/99746
      String caPostalCode = "";
      if (!StringUtil.isNullOrEmpty(postalCode)) {
        final String postalCode1 = postalCode.substring(0, 3).trim();
        final String postalCode2 = postalCode.substring(3).trim();
        caPostalCode = postalCode1 + BDMConstants.kStringSpace + postalCode2;
      }
      // End - Fix for Bug 68757/99746

      if (personSearchResult.postalOrZipCode.length() == 0)
        personSearchResult.postalOrZipCode =
          addressElementDtls.upperElementValue;

      if (!postalCode.isEmpty()
        && addressElementDtls.upperElementValue.contains(caPostalCode)) {
        isPostalCodeMatched = true;
      }

    } else if (addressElementDtls.elementType
      .equals(ADDRESSELEMENTTYPE.ZIP)) {
      if (personSearchResult.postalOrZipCode.length() == 0)
        personSearchResult.postalOrZipCode =
          addressElementDtls.upperElementValue;

      if (!postalCode.isEmpty()
        && addressElementDtls.upperElementValue.contains(postalCode)) {
        isPostalCodeMatched = true;
      }
    }
    return isPostalCodeMatched;
  }

  private Boolean checkProvince(final AddressElementDtls addressElementDtls,
    final String province, final CodeTableAdmin codeTableAdminObj,
    final BDMPersonSearchDetails personSearchResult,
    Boolean isProvinceMatched) throws AppException, InformationalException {

    if (addressElementDtls.elementType.equals(ADDRESSELEMENTTYPE.PROVINCE)) {
      // Set return province
      if (personSearchResult.stateOrProvince.length() == 0) {
        personSearchResult.stateOrProvince = CodeTable.getOneItem(
          PROVINCETYPE.TABLENAME, addressElementDtls.upperElementValue,
          TransactionInfo.getProgramLocale());
      }

      if (!province.isEmpty()) {
        // Search province name in code table description
        final CodeTableItemDetailsList list =
          codeTableAdminObj.searchByCodeTableItemDescription(
            PROVINCETYPE.TABLENAME, CuramConst.gkPercentage + province);

        for (int count = 0; count < list.dtls.size(); count++) {
          if (addressElementDtls.upperElementValue
            .equals(list.dtls.get(count).code)) {
            isProvinceMatched = true;
            break;
          }
        } // end for
      }
    }
    return isProvinceMatched;
  }

  private Boolean checkState(final AddressElementDtls addressElementDtls,
    final String province, final CodeTableAdmin codeTableAdminObj,
    final BDMPersonSearchDetails personSearchResult,
    Boolean isProvinceMatched) throws AppException, InformationalException {

    if (addressElementDtls.elementType.equals(ADDRESSELEMENTTYPE.STATEPROV)) {

      // Set return provinceState
      if (personSearchResult.stateOrProvince.length() == 0) {
        personSearchResult.stateOrProvince = CodeTable.getOneItem(
          ADDRESSSTATE.TABLENAME, addressElementDtls.upperElementValue,
          TransactionInfo.getProgramLocale());
      }

      if (!province.isEmpty()) {
        final CodeTableItemDetailsList list =
          codeTableAdminObj.searchByCodeTableItemDescription(
            ADDRESSSTATE.TABLENAME, CuramConst.gkPercentage + province);

        for (int count = 0; count < list.dtls.size(); count++) {
          if (addressElementDtls.upperElementValue
            .equals(list.dtls.get(count).code)) {
            isProvinceMatched = true;
            break;
          }
        } // end for
      }
    }
    return isProvinceMatched;
  }

  public BDMRegisterPersonWizardSearchDetails filterAddressPersonWizardSearch(
    final BDMRegisterPersonWizardSearchDetails registerPersonWizardSearchDetails,
    final BDMPersonSearchWizardKey key)
    throws AppException, InformationalException {

    final String province = key.stateProvince.trim().toUpperCase();
    final String country = key.countryCode.toUpperCase();
    final String postalCode = key.postalCode.trim().toUpperCase();

    final String unitNumber = key.unitNumber.trim().toUpperCase();
    Boolean isProvinceMatched = false;
    Boolean isCountryMatched = false;
    Boolean isPostalCodeMatched = false;
    // START: Task 93506: DEV: Address Format Search updates
    Boolean isUnitNumberMatched = false;
    // END: Task 93506: DEV: Address Format Search updates
    final Set<Long> uniqueConcernRoleIDs = new HashSet<Long>();

    BDMRegisterPersonWizardSearchDetails registerPersonWizardSearchDtls =
      new BDMRegisterPersonWizardSearchDetails();

    try {
      // initilization of variables
      final ConcernRoleAddress concernroleAddressObj =
        ConcernRoleAddressFactory.newInstance();
      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();
      final ConcernRoleKey concernroleKey = new ConcernRoleKey();

      final AddressElement addressElement =
        AddressElementFactory.newInstance();
      final AddressKey addressKey = new AddressKey();
      AddressElementDtlsList addressElementDtlsList =
        new AddressElementDtlsList();
      final CodeTableAdmin codeTableAdminObj =
        CodeTableAdminFactory.newInstance();

      // for each list of matched records, further match with Unit Number
      for (int i =
        0; i < registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
          .size(); i++) {

        isProvinceMatched = false;
        isCountryMatched = false;
        isPostalCodeMatched = false;
        // START: Task 93506: DEV: Address Format Search updates
        isUnitNumberMatched = false;
        // END: Task 93506: DEV: Address Format Search updates
        // get AddressID of current ConcernroleID
        concernroleKey.concernRoleID =
          registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
            .get(i).concernRoleID;
        concernRoleAddressKey.concernRoleAddressID =
          concernroleAddressObj.readConcernRoleAddressDetailsByConcernRoleID(
            concernroleKey).concernRoleAddressID;
        addressKey.addressID =
          concernroleAddressObj.read(concernRoleAddressKey).addressID;

        // Read Address Elements for a given Address ID
        addressElementDtlsList =
          addressElement.readAddressElementDetails(addressKey);

        // Search Results
        for (int j = 0; j < addressElementDtlsList.dtls.size(); j++) {

          if (!country.isEmpty()
            && addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.COUNTRY)
            && addressElementDtlsList.dtls.get(j).upperElementValue
              .equals(country)) {
            isCountryMatched = true;
          }

          if (!postalCode.isEmpty()) {
            if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.POSTCODE)) {

              // Start - Fix for Bug 68757/99746
              String caPostalCode = "";
              if (!StringUtil.isNullOrEmpty(postalCode)) {
                final String postalCode1 = postalCode.substring(0, 3).trim();
                final String postalCode2 = postalCode.substring(3).trim();
                caPostalCode =
                  postalCode1 + BDMConstants.kStringSpace + postalCode2;
              }
              // End - Fix for Bug 68757/99746

              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(caPostalCode)) {
                isPostalCodeMatched = true;
              }

            } else if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.ZIP)) {
              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(postalCode)) {
                isPostalCodeMatched = true;
              }
            }
            // START: Task 93506: DEV: Address Format Search updates
            else if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.APT)) {

              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(postalCode)) {
                isPostalCodeMatched = true;
              }
            } else if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.LINE1)) {
              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(postalCode)) {
                isPostalCodeMatched = true;
              }
            } else if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.LINE2)) {
              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(postalCode)) {
                isPostalCodeMatched = true;
              }
            } else if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.CITY)) {
              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(postalCode)) {
                isPostalCodeMatched = true;
              }
            }
            // END: Task 93506: DEV: Address Format Search updates
          }
          // Province search
          if (!province.isEmpty()) {
            // search by code table description for province and
            // stateprov
            if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.PROVINCE)) {

              // Search province name in code table description
              final CodeTableItemDetailsList list =
                codeTableAdminObj.searchByCodeTableItemDescription(
                  PROVINCETYPE.TABLENAME, CuramConst.gkPercentage + province);

              for (int count = 0; count < list.dtls.size(); count++) {
                if (addressElementDtlsList.dtls.get(j).upperElementValue
                  .equals(list.dtls.get(count).code)) {
                  isProvinceMatched = true;
                  break;
                }
              } // end for
            } else if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.STATEPROV)) {

              final CodeTableItemDetailsList list =
                codeTableAdminObj.searchByCodeTableItemDescription(
                  ADDRESSSTATE.TABLENAME, CuramConst.gkPercentage + province);

              for (int count = 0; count < list.dtls.size(); count++) {
                if (addressElementDtlsList.dtls.get(j).upperElementValue
                  .equals(list.dtls.get(count).code)) {
                  isProvinceMatched = true;
                  break;
                }
              } // end for
            } else if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.BDMSTPROV_X)) {
              // Added for international addresses
              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(province)) {
                isProvinceMatched = true;
                break;
              }
            }
          }

          if (!unitNumber.isEmpty()) {
            if (addressElementDtlsList.dtls.get(j).elementType
              .equals(ADDRESSELEMENTTYPE.APT)) {

              if (addressElementDtlsList.dtls.get(j).upperElementValue
                .contains(unitNumber)) {
                isUnitNumberMatched = true;
              }

            }
          }

        }

        if (!province.isEmpty() && !isProvinceMatched) {
          continue;
        }
        if (!country.isEmpty() && !isCountryMatched) {
          continue;
        }
        if (!postalCode.isEmpty() && !isPostalCodeMatched) {
          continue;
        }
        // START: Task 93506: DEV: Address Format Search updates
        if (!unitNumber.isEmpty() && !isUnitNumberMatched) {
          continue;
        }
        // END: Task 93506: DEV: Address Format Search updates
        if (uniqueConcernRoleIDs.contains(
          registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
            .get(i).concernRoleID)) {
          continue;
        } else {
          uniqueConcernRoleIDs.add(
            registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
              .get(i).concernRoleID);
        }

        // Record is okay based on the search criteria
        // Lets populate search result with SIN Number
        registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
          .get(i).sinNumber = getAlternateIDByConcernRoleIDType(
            registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
              .get(i).concernRoleID,
            CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);

        // add matched record to return list
        registerPersonWizardSearchDtls.searchResult.personSearchResult.dtlsList
          .add(
            registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
              .get(i));

      }
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_ADDRESS_FILTER + e);
      // if there is any exception then return the original list
      registerPersonWizardSearchDtls = registerPersonWizardSearchDetails;

    }

    return registerPersonWizardSearchDtls;
  }

  /**
   * Helper method to return SIN number based on concernRoleID
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getAlternateIDByConcernRoleIDType(
    final long concernRoleID, final String typeCode)
    throws AppException, InformationalException {

    String sinNumber = "";

    final curam.core.intf.ConcernRoleAlternateID concernrolealternateID =
      ConcernRoleAlternateIDFactory.newInstance();
    final ConcernRoleIDTypeStatusEndDateKey concernRoleIDTypeStatusEndDateKey =
      new ConcernRoleIDTypeStatusEndDateKey();

    // Code to access end date

    final curam.core.facade.intf.ConcernRole concernRole =
      curam.core.facade.fact.ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      concernRole.readConcernRole(concernRoleKey);

    final ConcernRoleAlternateIDKeyStruct1 concernRoleAlternateIDKeyStruct1 =
      new ConcernRoleAlternateIDKeyStruct1();
    concernRoleAlternateIDKeyStruct1.concernRoleID = concernRoleID;
    concernRoleAlternateIDKeyStruct1.typeCode = typeCode;
    concernRoleAlternateIDKeyStruct1.statusCode = RECORDSTATUS.NORMAL;
    concernRoleAlternateIDKeyStruct1.alternateID =
      concernRoleDtls.primaryAlternateID;

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final ConcernRoleAlternateIDDtlsStruct1 ConcernRoleAlternateIDDtlsStruct1 =
      concernrolealternateID.readByConcernRoleIDAndType(notFoundIndicator,
        concernRoleAlternateIDKeyStruct1);
    if (!notFoundIndicator.isNotFound()) {
      concernRoleIDTypeStatusEndDateKey.endDate =
        ConcernRoleAlternateIDDtlsStruct1.endDate;
    }

    // End

    concernRoleIDTypeStatusEndDateKey.concernRoleID = concernRoleID;
    concernRoleIDTypeStatusEndDateKey.statusCode = RECORDSTATUS.NORMAL;
    concernRoleIDTypeStatusEndDateKey.typeCode = typeCode;

    final ConcernRoleAlternateIDDtlsList concernRoleAlternateIDDtlsList =
      concernrolealternateID.searchByConcernRoleIDTypeStatusAndEndDate(
        concernRoleIDTypeStatusEndDateKey);

    if (concernRoleAlternateIDDtlsList.dtls.size() > 0) {
      sinNumber = concernRoleAlternateIDDtlsList.dtls.get(0).alternateID;
    }

    return sinNumber;
  }

  /**
   *
   * Util Method to convert Address Data
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
   *
   * Util method to check if the dependant on this case is claimed by any one
   * else on other cases
   *
   * @since ADO-16437
   * @param caseID
   * @param concernRoleID
   * @param dependantStartDate
   * @param dependantEndDate
   * @return
   */

  public Boolean isDependantClaimedOnAnotherCase(final long caseID,
    final long concernRoleID, final Date dependantStartDate,
    final Date dependantEndDate) throws AppException, InformationalException {

    Boolean isClaimedAsDependant = false;

    final StringBuffer dynamicSQL = new StringBuffer();

    dynamicSQL.append(isDependantClaimedOnAnotherCase_SelectSTmt);
    dynamicSQL.append(isDependantClaimedOnAnotherCase_InnerJoin);
    dynamicSQL.append("" + CASETYPECODE.INTEGRATEDCASE + "','"
      + CASETYPECODE.APPLICATION_CASE + "') AND ch.STATUSCODE ='"
      + CASESTATUS.OPEN + "' AND ed.EVIDENCETYPE ='"
      + CASEEVIDENCE.BDMDEPENDANT + "' AND ed.STATUSCODE IN ('"
      + EVIDENCEDESCRIPTORSTATUS.ACTIVE + "','"
      + EVIDENCEDESCRIPTORSTATUS.INEDIT + "') AND ed.caseid <> " + caseID);
    // Ignore evidences on related cases or have the case as the evidence source
    dynamicSQL.append(isDependantClaimedOnAnotherCase_condition1);
    dynamicSQL.append(
      "  AND cr.CASEID <> " + caseID + "  AND cr.RELATEDCASEID <> " + caseID);
    // Check if the role type is primary, member, or related person
    dynamicSQL.append(
      isDependantClaimedOnAnotherCase_conditionCheckRoleType + concernRoleID);

    dynamicSQL.append(isDependantClaimedOnAnotherCase_condition2);

    final CuramValueList<EvidenceDescriptorDtls> evidenceDescriptorDtls =
      DynamicDataAccess.executeNsMulti(EvidenceDescriptorDtls.class, null,
        false, dynamicSQL.toString());

    // checking if the dependant is claimed by any one during the evidence
    // period
    for (int i = 0; i < evidenceDescriptorDtls.size(); i++) {

      if (evidenceDescriptorDtls.get(i).evidenceType.toString()
        .equals(stringTrue)
        && (Date.getDate(evidenceDescriptorDtls.get(i).statusCode)
          .equals(dependantStartDate)
          || Date.getDate(evidenceDescriptorDtls.get(i).statusCode)
            .before(dependantStartDate))
        && (null == dependantEndDate || dependantEndDate.isZero()
          || evidenceDescriptorDtls.get(i).correctionSetID.isEmpty()
          || dependantEndDate.before(
            Date.getDate(evidenceDescriptorDtls.get(i).correctionSetID)))) {

        isClaimedAsDependant = true;

      }

    }
    return isClaimedAsDependant;

  }

  /**
   *
   * @param caseID
   * @param caseParticipantRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long getParticipantRoleID(final Number caseID,
    final long caseParticipantRoleID)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();
    caseParticipantRoleKey.caseParticipantRoleID = caseParticipantRoleID;

    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance()
        .read(notFoundIndicator, caseParticipantRoleKey);

    if (notFoundIndicator.isNotFound()) {
      return 0l;
    }
    return caseParticipantRoleDtls.participantRoleID;
  }

  /**
   * SL method to read person demographics details
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMPersonDtls readPersonDemographicsDetails(final PersonKey key)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = key.concernRoleID;
    BDMPersonDtls bdmPersonDtls =
      bdmPersonObj.read(notFoundIndicator, bdmPersonKey);

    if (notFoundIndicator.isNotFound()) {
      bdmPersonDtls = new BDMPersonDtls();
    }

    return bdmPersonDtls;
  }

  /**
   * SL method to modify person demographics data
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  public void modifyPersonDemographicsDetails(
    final BDMPersonDemographicPageDetails details)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = details.concernRoleID;
    BDMPersonDtls bdmPersonDtls =
      bdmPersonObj.read(notFoundIndicator, bdmPersonKey);

    if (notFoundIndicator.isNotFound()) {
      bdmPersonDtls = new BDMPersonDtls();
      bdmPersonDtls.concernRoleID = bdmPersonKey.concernRoleID;
      bdmPersonDtls.educationLevel = details.educationLevel;
      bdmPersonDtls.indigenousPersonType = details.indigenousPersonType;
      bdmPersonDtls.memberMinorityGrpType = details.memberMinorityGrpType;

      bdmPersonObj.insert(bdmPersonDtls);
    } else {
      bdmPersonDtls.educationLevel = details.educationLevel;
      bdmPersonDtls.indigenousPersonType = details.indigenousPersonType;
      bdmPersonDtls.memberMinorityGrpType = details.memberMinorityGrpType;

      bdmPersonObj.modify(bdmPersonKey, bdmPersonDtls);
    }
  }

  /**
   * SL method to read prospect person demographics details
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMProspectPersonDtls readProspectPersonDemographicsDetails(
    final ProspectPersonKey key) throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final BDMProspectPerson bdmProspectPersonObj =
      BDMProspectPersonFactory.newInstance();
    final BDMProspectPersonKey bdmProspectPersonKey =
      new BDMProspectPersonKey();
    bdmProspectPersonKey.concernRoleID = key.concernRoleID;
    BDMProspectPersonDtls bdmProspectPersonDtls =
      bdmProspectPersonObj.read(notFoundIndicator, bdmProspectPersonKey);

    if (notFoundIndicator.isNotFound()) {
      bdmProspectPersonDtls = new BDMProspectPersonDtls();
    }
    return bdmProspectPersonDtls;
  }

  /**
   * SL method to modify prospect person demographics details
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  public void modifyProspectPersonDemographicsDetails(
    final BDMPersonDemographicPageDetails details)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final BDMProspectPerson enBDMProspectPersonObj =
      BDMProspectPersonFactory.newInstance();
    final BDMProspectPersonKey bdmProspectPersonKey =
      new BDMProspectPersonKey();
    bdmProspectPersonKey.concernRoleID = details.concernRoleID;
    BDMProspectPersonDtls bdmProspectPersonDtls =
      enBDMProspectPersonObj.read(notFoundIndicator, bdmProspectPersonKey);

    if (notFoundIndicator.isNotFound()) {
      bdmProspectPersonDtls = new BDMProspectPersonDtls();
      bdmProspectPersonDtls.concernRoleID =
        bdmProspectPersonKey.concernRoleID;
      bdmProspectPersonDtls.educationLevel = details.educationLevel;
      bdmProspectPersonDtls.indigenousPersonType =
        details.indigenousPersonType;
      bdmProspectPersonDtls.memberMinorityGrpType =
        details.memberMinorityGrpType;

      enBDMProspectPersonObj.insert(bdmProspectPersonDtls);
    } else {
      bdmProspectPersonDtls.educationLevel = details.educationLevel;
      bdmProspectPersonDtls.indigenousPersonType =
        details.indigenousPersonType;
      bdmProspectPersonDtls.memberMinorityGrpType =
        details.memberMinorityGrpType;

      enBDMProspectPersonObj.modify(bdmProspectPersonKey,
        bdmProspectPersonDtls);
    }
  }

  /**
   * Util method to validate address fields
   *
   *
   * @param addressData
   * @param informationalManager
   * @throws InformationalException
   */
  public InformationalManager validateAddress(
    final AddressEvidenceWizardStp3DataDetails step3DataDtls,
    final String countryCode, final String addressType,
    final InformationalManager informationalManager)
    throws InformationalException, AppException {

    // get Country
    final String country = countryCode;

    // Postal Code
    final String postalCode = step3DataDtls.clientSelectedPostalCode;

    // state
    String state = "";

    if (country.equals(countryUS)) {

      state = step3DataDtls.clientSelectedState;

    } else if (!country.equals(countryCA)) {

      state = step3DataDtls.clientSelectedRegion;
    }

    final String poBox = step3DataDtls.clientSelectedPOBox;

    if (addressType.equals(CONCERNROLEADDRESSTYPE.DEFAULTCODE)
      && !poBox.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PO_RESIDENTIAL);// ERR_PO_RESIDENTIAL

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    if (addressType.equals(CONCERNROLEADDRESSTYPE.DEFAULTCODE)) {

      if (StringUtil.isNullOrEmpty(step3DataDtls.clientSelectedAddressLine1)
        || StringUtil
          .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine2)) {
        if (country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {
          final LocalisableString localisableString = new LocalisableString(
            BDMBPOADDRESS.ERR_STRNUM_STRNAME_MUST_BE_COMPLETED_FOR_CANADIANADDRESS);
          informationalManager.addInformationalMsg(localisableString, "",
            InformationalElement.InformationalType.kError);
          // BUG 82399 - Remove street number, street name validation for non
          // US/CA countries
        } else if (country.equalsIgnoreCase(COUNTRYCODE.USCODE)) {
          // Task 68157 Show correct message for non-Canadian address
          final LocalisableString localisableString = new LocalisableString(
            BDMBPOADDRESS.ERR_STRNUM_STRNAME_MUST_BE_ENTERED);
          informationalManager.addInformationalMsg(localisableString, "",
            InformationalElement.InformationalType.kError);
        }

      }
    }

    if (!addressType.equals(CONCERNROLEADDRESSTYPE.DEFAULTCODE)) {

      if (country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {
        // START : BUG-106455 : if Condition modified for CA address.
        if (StringUtil.isNullOrEmpty(step3DataDtls.clientSelectedPOBox)
          && (StringUtil
            .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine1)
            || StringUtil
              .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine2))) {
          // END : BUG-106455
          final LocalisableString localisableString = new LocalisableString(
            BDMBPOADDRESS.ERR_STRNUM_STRNAME_OR_POBOX_MUST_BE_COMPLETED_FOR_CANADIANADDRESS);
          informationalManager.addInformationalMsg(localisableString, "",
            InformationalElement.InformationalType.kError);
        }
      } else if (country.equalsIgnoreCase(COUNTRYCODE.USCODE)) {
        // Task 68157 Show correct message for non-Canadian address

        // According to Foundations FDD, street name is mandatory for United
        // states addresses.
        // But, for international addresses, address line 2 is not mandatory.
        if (StringUtil.isNullOrEmpty(step3DataDtls.clientSelectedPOBox)
          && (StringUtil
            .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine1)
            || StringUtil
              .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine2))) {

          final LocalisableString localisableString = new LocalisableString(
            BDMBPOADDRESS.ERR_STRNUM_STRNAME_OR_POBOX_MUST_BE_ENTERED);
          informationalManager.addInformationalMsg(localisableString, "",
            InformationalElement.InformationalType.kError);
        }
      } else if (!country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {
        // Task 68157 Show correct message for non-Canadian address

        // According to Foundations FDD, street name is mandatory for United
        // states addresses.
        // But, for international addresses, address line 2 is not mandatory.
        if (StringUtil.isNullOrEmpty(step3DataDtls.clientSelectedPOBox)
          && StringUtil
            .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine1)) {

          final LocalisableString localisableString = new LocalisableString(
            BDMBPOADDRESS.ERR_STRNUM_STRNAME_OR_POBOX_MUST_BE_ENTERED);
          informationalManager.addInformationalMsg(localisableString, "",
            InformationalElement.InformationalType.kError);
        }
      }
    }

    if (!addressType.equals(CONCERNROLEADDRESSTYPE.DEFAULTCODE)) {

      if (!StringUtil.isNullOrEmpty(step3DataDtls.clientSelectedPOBox)
        && (!StringUtil
          .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine1)
          || !StringUtil
            .isNullOrEmpty(step3DataDtls.clientSelectedAddressLine2))) {
        if (country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {
          final LocalisableString localisableString = new LocalisableString(
            BDMBPOADDRESS.ERR_STRNUM_STRNAME_OR_POBOX_MUST_BE_COMPLETED_FOR_CANADIANADDRESS);
          informationalManager.addInformationalMsg(localisableString, "",
            InformationalElement.InformationalType.kError);
        } else {
          final LocalisableString localisableString = new LocalisableString(
            BDMBPOADDRESS.ERR_STRNUM_STRNAME_OR_POBOX_MUST_BE_ENTERED);
          informationalManager.addInformationalMsg(localisableString, "",
            InformationalElement.InformationalType.kError);
        }

      }

    }

    // city
    final String city = step3DataDtls.clientSelectedCity;

    // province
    final String province = step3DataDtls.clientSelectedProvince;

    final String zipCode = step3DataDtls.clientSelectedZipCode;

    // validations for CA Address
    if (country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {

      // City is Mandatory
      if (city.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_CITY_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
      // Province is Mandatory
      if (province.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_PROV_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }
      // Postal Code is Mandatory
      if (postalCode.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_POSTALCODE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      } else if (!isValidPostalCode(postalCode)) {// Postal Code should be in
                                                  // valid Canadian Format

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }

    } else if (country.equalsIgnoreCase(COUNTRYCODE.USCODE)) {// validations for
                                                              // US Address

      // City is Mandatory
      // START - BUG 107500
      if (city.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_CITY_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
      // END - BUG 107500
      if (zipCode.isEmpty()) {// Zip code is mandatory

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_US_ZIPCODE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
      // BEGIN, BUG 97516, US Zip code Validation
      else if (!isValidUSZipCode(zipCode)) {

        final LocalisableString localisableString = new LocalisableString(
          BDMBPOADDRESS.ERR_US_ZIP_CODE_MUST_BE_VALID_FORMAT);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }
      // END, BUG 97516, US Zip code Validation
    }
    if (!country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {// Validations for all
                                                        // other countries

      // City is Mandatory
      // START - BUG 107500
      if (city.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_CITY_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
      // END - BUG 107500
      if (state.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_STATE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
    }

    return informationalManager;

  }

  private Boolean isValidUSZipCode(final String usZipCode) {

    Boolean isValidUSZipCode = false;
    if (usZipCode.length() == 5
      && org.apache.commons.lang3.math.NumberUtils.isNumber(usZipCode)) {
      isValidUSZipCode = true;
    } else if (usZipCode.contains(CuramConst.gkDash)) {
      final String[] ZipCodeStrings = usZipCode.split(CuramConst.gkDash);
      if (ZipCodeStrings.length == 2 && ZipCodeStrings[0].length() == 5
        && org.apache.commons.lang3.math.NumberUtils
          .isNumber(ZipCodeStrings[0])
        && ZipCodeStrings[1].length() == 4
        && org.apache.commons.lang3.math.NumberUtils
          .isNumber(ZipCodeStrings[1])) {
        isValidUSZipCode = true;
      }
    }
    return isValidUSZipCode;
  }

  /**
   * util ensures the post code is in the format ANA NAN or ANANAN example: B9T
   * 9A9
   * or B9T9A9
   *
   * @param postCode
   * Postal Code
   *
   * @return true if postal code matches pattern
   */
  public boolean isValidPostalCode(final String postalCode) {

    // CA postal code regular expression.
    final RegularExpression regExpWithSpace =
      new RegularExpression(CuramConst.gkCanadianREWithSpace);
    final RegularExpression regExpWithoutSpace =
      new RegularExpression(CuramConst.gkCanadianRE);

    final Boolean validCAPostalCode = regExpWithSpace.matches(postalCode)
      || regExpWithoutSpace.matches(postalCode);

    return validCAPostalCode;
  }

  /**
   *
   * @param mailingAddressData
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean isAddressEmpty(final String mailingAddressData)
    throws AppException, InformationalException {

    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = mailingAddressData;

    Boolean isEmptyAddress = false;

    // get Country
    final String country =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

    // city
    final String city =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

    if (city.isEmpty() || country.isEmpty()) {
      isEmptyAddress = true;
    }

    return isEmptyAddress;
  }

  /**
   * Method to get the Document from XML String.
   *
   * @param applicationCaseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  public Document getDocumentFromCheckEligiblityResultsXML(
    final long applicationCaseID, final String programName)

    throws AppException, InformationalException {

    ApplicationCaseCheckEligibilityDisplayXML displayXML =
      getXMLFromCheckEligibiiltyResultsByProgram(applicationCaseID,
        programName);
    if (displayXML == null) {
      // To get correct results, running the check ELigibility again as there
      // could evidence changes happened which may cause member be to eligible
      // or
      // ineligible.
      // Run the Check Eligibility for the App Case.
      final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
      applicationCaseKey.applicationCaseID = applicationCaseID;
      BDMApplicationCaseCheckEligibilityFactory.newInstance()
        .checkEligibility(applicationCaseKey);

      displayXML = getXMLFromCheckEligibiiltyResultsByProgram(
        applicationCaseID, programName);
    }
    final String xml = displayXML == null ? "" : displayXML.displayXML;

    final DOMParser parser = new DOMParser();
    try {
      parser.parse(new InputSource(new StringReader(xml)));

    } catch (final SAXException e) {
      Trace.kTopLevelLogger.error(e.getMessage(), e);
    } catch (final IOException e) {
      Trace.kTopLevelLogger.error(e.getMessage(), e);
    }
    final Document document = parser.getDocument();
    return document;
  }

  /**
   * Returns the attribute value from the XML string returned from the
   * Application check eligibility results.
   *
   * @param applicationCaseID
   * @param xpathAttributeName
   * @param programName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public NodeList getAttributeValue(final long applicationCaseID,
    final String xpathAttributeName, final String programName)
    throws AppException, InformationalException {

    final Document document = getDocumentFromCheckEligiblityResultsXML(
      applicationCaseID, programName);

    NodeList node = null;
    try {
      node = XPathAPI.selectNodeList(document, xpathAttributeName);
    } catch (final TransformerException e) {
      e.printStackTrace();
    }

    return node == null ? null : node;
  }

  /**
   * Returns the Node List for the attributes passed.
   *
   * @param document
   * @param xpathAttributeName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public NodeList getNodeListValue(final Document document,
    final String xpathAttributeName)
    throws AppException, InformationalException {

    try {
      return XPathAPI.selectNodeList(document, xpathAttributeName);
    } catch (final TransformerException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns the node for the attribute passed.
   *
   * @param document
   * @param xpathAttributeName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public Node getNodeValue(final Document document,
    final String xpathAttributeName)
    throws AppException, InformationalException {

    try {
      return XPathAPI.selectSingleNode(document, xpathAttributeName);
    } catch (final TransformerException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * This method returns the object which contains the XML of the Check
   * Eligibility Results.
   *
   * @param applicationCaseID
   * @param programName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ApplicationCaseCheckEligibilityDisplayXML
    getXMLFromCheckEligibiiltyResultsByProgram(final long applicationCaseID,
      final String programName) throws AppException, InformationalException {

    final curam.commonintake.facade.intf.ApplicationCaseCheckEligibility applicationCaseCheckEligibility =
      ApplicationCaseCheckEligibilityFactory.newInstance();

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();

    applicationCaseKey.applicationCaseID = applicationCaseID;
    final ApplicationCaseCheckEligibiltyResultList checkEligibiltyResultList =
      applicationCaseCheckEligibility
        .listEligibilityChecks(applicationCaseKey);

    final AppCaseEligibilityResultKey appCaseEligibilityResultKey =
      new AppCaseEligibilityResultKey();

    if (checkEligibiltyResultList.eligibilityDtls.isEmpty()) {
      return null;
    }

    for (final ApplicationCaseCheckEligibilityDetails details : checkEligibiltyResultList.eligibilityDtls) {

      if (details.programName.equals(programName)) {

        appCaseEligibilityResultKey.appCaseEligibilityResultID =
          details.dtls.appCaseEligibilityResultID;

        return applicationCaseCheckEligibility
          .viewCheckEligibilityDisplayRules(appCaseEligibilityResultKey);
      }
    }
    return null;
  }

  /**
   * Checks if the concern role is a registered person or a prospect.
   *
   * @param concernRole The concern role.
   * @return true if the person is not a prospect, false otherwise
   *
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isRegisteredPerson(final Long concernRole)
    throws AppException, InformationalException {

    boolean isRegisteredPerson = false;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
    prospectPersonKey.concernRoleID = concernRole;
    ProspectPersonFactory.newInstance().read(notFoundIndicator,
      prospectPersonKey);
    if (notFoundIndicator.isNotFound()) {
      isRegisteredPerson = true;
    }
    return isRegisteredPerson;
  }

  /**
   * Util method to update Person Middle name if not updated
   *
   * @since ADO-16944
   * @param personEntity
   * @param concernRole
   */
  public void updateNamesEvd(final Entity personEntity,
    final curam.participant.impl.ConcernRole concernRole)
    throws AppException, InformationalException {

    // Read Person Details
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = concernRole.getID();
    final Person personObj = PersonFactory.newInstance();

    final AlternateName alternateNameObj = AlternateNameFactory.newInstance();
    final AlternateNameKey alternateNameKey = new AlternateNameKey();

    final ProspectPerson prospectPersonObj =
      ProspectPersonFactory.newInstance();

    if (isRegisteredPerson(concernRole.getID())) {
      final PersonDtls personDtls = personObj.read(personKey, true);
      alternateNameKey.alternateNameID = personDtls.primaryAlternateNameID;
    } else {

      final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
      prospectPersonKey.concernRoleID = concernRole.getID();
      final ProspectPersonDtls prospectPersonDtls =
        prospectPersonObj.read(prospectPersonKey);

      alternateNameKey.alternateNameID =
        prospectPersonDtls.primaryAlternateNameID;
    }
    final ParticipantAlternateNameDetails nameDetails =
      new ParticipantAlternateNameDetails();
    final AlternateNameDtls alternateNameDtls =
      alternateNameObj.read(alternateNameKey);

    final String middleName =
      personEntity.hasAttribute(BDMDatastoreConstants.MIDDLE_NAME)
        ? personEntity.getTypedAttribute(BDMDatastoreConstants.MIDDLE_NAME)
          .toString()
        : "";

    final String firstName = personEntity
      .getTypedAttribute(BDMDatastoreConstants.FIRST_NAME).toString();

    final String lastName = personEntity
      .getTypedAttribute(BDMDatastoreConstants.LAST_NAME).toString();

    final BDMNamesEvidenceVO evidenceName = new BDMNamesEvidenceVO();
    evidenceName.setFirstName(alternateNameDtls.firstForename);
    evidenceName.setLastName(alternateNameDtls.surname);
    evidenceName.setMiddleName(alternateNameDtls.otherForename);

    final BDMNamesEvidenceVO dateStoreName = new BDMNamesEvidenceVO();
    dateStoreName.setFirstName(firstName);
    dateStoreName.setLastName(lastName);
    dateStoreName.setMiddleName(middleName);

    if (0 != dateStoreName.compareTo(evidenceName)) {

      final PDCAlternateName pdcAlternateName =
        PDCAlternateNameFactory.newInstance();

      nameDetails.firstForename = firstName;
      nameDetails.otherForename = middleName;
      nameDetails.surname = lastName;

      nameDetails.alternateNameID = alternateNameKey.alternateNameID;
      nameDetails.title = alternateNameDtls.title;
      nameDetails.nameSuffix = alternateNameDtls.nameSuffix;
      nameDetails.nameType = alternateNameDtls.nameType;
      nameDetails.nameStatus = alternateNameDtls.nameStatus;
      nameDetails.initials = alternateNameDtls.initials;
      nameDetails.concernRoleID = alternateNameDtls.concernRoleID;

      // call OOTB Modify method to update person name
      try {
        pdcAlternateName.modify(nameDetails);
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }

  }

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
        evidenceDataDetailsList.stream().filter(phoneDtls -> phoneDtls
          .getAttribute(toDateAttr).getValue().isEmpty())
          .collect(Collectors.toList());

      List<DynamicEvidenceDataDetails> existingPhoneEvdDtls;

      final BDMPhoneEvidence bdmPhoneEvidence = new BDMPhoneEvidence();

      final List<BDMPhoneEvidenceVO> phoneEvidenceList =
        new ArrayList<BDMPhoneEvidenceVO>();

      final Entity[] communicationEntity =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMDatastoreConstants.COMMUNICATION_DETAILS);

      final Entity alertPreference =
        BDMApplicationEventsUtil.hasChildEntities(personEntity,
          BDMDatastoreConstants.ALERT_PREFERENCES)
            ? BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
              BDMDatastoreConstants.ALERT_PREFERENCES)[0]
            : null;

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
            .equals(stringTrue) ? Boolean.TRUE : Boolean.FALSE;
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
            .equals(stringTrue) ? Boolean.TRUE : Boolean.FALSE);

        }

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON)) {

          dataStorePhone.setAfternoonInd(communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.PHONE_IS_AFTNOON).toString()
            .equals(stringTrue) ? Boolean.TRUE : Boolean.FALSE);

        }

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.PHONE_IS_EVE)) {

          dataStorePhone.setEveningInd(communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.PHONE_IS_EVE).toString()
            .equals(stringTrue) ? Boolean.TRUE : Boolean.FALSE);

        }

        dataStorePhone.setPreferredInd(isPreferredInd);

        // Task 62736 - alert list question use phoneNumberSelected for phone
        // number
        final Boolean isAlertPrfInd = communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.PHONE_NUMBER_SELECTED)
          .toString().equals("true") ? Boolean.TRUE : Boolean.FALSE;

        dataStorePhone.setUseForAlertsInd(isAlertPrfInd);

        if (isAlertPrfInd && null != alertPreference && alertPreference
          .hasAttribute(BDMDatastoreConstants.ALERT_OCCUR)) {
          // Task 62736 - phone number validation ruleset uses alert frequency
          dataStorePhone.setAlertFrequency(alertPreference
            .getAttribute(BDMDatastoreConstants.ALERT_OCCUR).toString());

        }

        existingPhoneEvdDtls = existingPhoneList.size() > 0
          ? existingPhoneList.stream()
            .filter(phoneEvdDtls -> phoneEvdDtls.getAttribute(phoneTypeAttr)
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

      // Task 63058 - if a different active phone is receiving alert, it will be
      // updated so that it will no longer receive alert and set alert frequency
      // to blank
      final List<BDMPhoneEvidenceVO> finalPhoneEvidenceList =
        new ArrayList<BDMPhoneEvidenceVO>();
      for (final BDMPhoneEvidenceVO evidencePhoneVO : phoneEvidenceList) {
        if (evidencePhoneVO.isUseForAlertsInd()
          && existingPhoneList.size() > 0) {
          for (final DynamicEvidenceDataDetails phoneEvdItemDtals : existingPhoneList) {
            final BDMPhoneEvidenceVO evidencePhone = new BDMPhoneEvidenceVO();
            new BDMLifeEventUtil().setEvidenceData(evidencePhone,
              phoneEvdItemDtals);
            if (evidencePhone.isUseForAlertsInd() && evidencePhone
              .getEvidenceID() != evidencePhoneVO.getEvidenceID()) {
              evidencePhone.setAlertFrequency(CuramConst.gkEmpty);
              evidencePhone.setUseForAlertsInd(false);
              evidencePhone
                .setPhoneNumberChangeType(BDMPHONENUMBERCHANGETYPE.UPDATE);
              finalPhoneEvidenceList.add(evidencePhone);
              break;
            }
          }
        }
      }
      finalPhoneEvidenceList.addAll(phoneEvidenceList);

      bdmPhoneEvidence.createPhoneEvidence(concernRole.getID(),
        finalPhoneEvidenceList, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.ERR_PHONE);
      e.printStackTrace();
    }

  }

  /**
   *
   * @param personEntity
   * @param concernRole
   * @throws AppException
   * @throws InformationalException
   */
  public void processEmailEvd(final Entity personEntity,
    final curam.participant.impl.ConcernRole concernRole)
    throws AppException, InformationalException {

    try {

      final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

      final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
        bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
          concernRole.getID(), PDCConst.PDCEMAILADDRESS);

      // filter out evidence which are enddated
      final List<DynamicEvidenceDataDetails> existingEmailList =
        evidenceDataDetailsList.stream().filter(emailDtls -> emailDtls
          .getAttribute(toDateAttr).getValue().isEmpty())
          .collect(Collectors.toList());

      List<DynamicEvidenceDataDetails> existingEmailEvdDtls;

      final BDMEmailEvidence bdmEmailEvidence = new BDMEmailEvidence();

      final List<BDMEmailEvidenceVO> emailEvidenceList =
        new ArrayList<BDMEmailEvidenceVO>();

      final Entity[] communicationEntity =
        BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
          BDMDatastoreConstants.COMMUNICATION_DETAILS);

      final Entity alertPreference =
        BDMApplicationEventsUtil.hasChildEntities(personEntity,
          BDMDatastoreConstants.ALERT_PREFERENCES)
            ? BDMApplicationEventsUtil.retrieveChildEntities(personEntity,
              BDMDatastoreConstants.ALERT_PREFERENCES)[0]
            : null;
      BDMEmailEvidenceVO dataStoreEmail = null;

      for (int i = 0; i < communicationEntity.length; i++) {

        dataStoreEmail = new BDMEmailEvidenceVO();

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.EMAIL_ADDRESS)
          && communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.EMAIL_ADDRESS).isEmpty()) {

          continue;

        }

        Boolean isPreferredInd = false;

        if (communicationEntity[i]
          .hasAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM)) {

          isPreferredInd = communicationEntity[i]
            .getAttribute(BDMDatastoreConstants.IS_PRIMARY_COMM).toString()
            .equals(stringTrue) ? Boolean.TRUE : Boolean.FALSE;
        }

        dataStoreEmail.setEmailAddress(communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.EMAIL_ADDRESS).toString());

        final String emailType = communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.EMAIL_TYPE).toString();
        dataStoreEmail.setEmailAddressType(emailType);

        dataStoreEmail.setPreferredInd(isPreferredInd);
        dataStoreEmail.setFromDate(Date.getCurrentDate());

        // Task 62736 - alert list question use phoneNumberSelected for phone
        // number
        final Boolean isAlertPrfInd = communicationEntity[i]
          .getAttribute(BDMDatastoreConstants.EMAIL_ADR_SELECTED).toString()
          .equals(stringTrue) ? Boolean.TRUE : Boolean.FALSE;
        dataStoreEmail.setUseForAlertsInd(isAlertPrfInd);

        if (isAlertPrfInd && null != alertPreference && alertPreference
          .hasAttribute(BDMDatastoreConstants.ALERT_OCCUR)) {
          // email validation ruleset uses alert frequency
          dataStoreEmail.setAlertFrequency(alertPreference
            .getAttribute(BDMDatastoreConstants.ALERT_OCCUR).toString());

        }

        existingEmailEvdDtls = existingEmailList.size() > 0
          ? existingEmailList.stream().filter(emailEvdDtls -> emailEvdDtls
            .getAttribute(emailAddressTypeAttr).getValue().equals(emailType))
            .collect(Collectors.toList())
          : new ArrayList<DynamicEvidenceDataDetails>();

        if (existingEmailEvdDtls.size() > 0) {
          // there might be multiple email addresses
          boolean matched = false;
          boolean partiallyMatched = false;
          for (final DynamicEvidenceDataDetails emailEvdItemDtls : existingEmailEvdDtls) {
            final BDMEmailEvidenceVO evidenceEmail = new BDMEmailEvidenceVO();
            new BDMLifeEventUtil().setEvidenceData(evidenceEmail,
              emailEvdItemDtls);
            if (!evidenceEmail.equals(dataStoreEmail)) {
              if (evidenceEmail.getEmailAddress()
                .equals(dataStoreEmail.getEmailAddress())) {
                dataStoreEmail.setEvidenceID(evidenceEmail.getEvidenceID());
                // Task 25720- Email address life event rework
                dataStoreEmail.setEmailAddressChangeType(
                  BDMEMAILADDRESSCHANGETYPE.UPDATE);
                emailEvidenceList.add(dataStoreEmail);
                partiallyMatched = true;
                break;
              }
            } else {
              // evidence already exists, skip this email from datastore
              matched = true;
              break;
            }
          }

          if (!matched && !partiallyMatched) {
            emailEvidenceList.add(dataStoreEmail);
          }
        } else {
          // no existing email, create new evd
          emailEvidenceList.add(dataStoreEmail);
        }
      }

      // Task 63058 - if a different active email is receiving alert, it will be
      // updated so that it will no longer receive alert and set alert frequency
      // to blank
      final List<BDMEmailEvidenceVO> finalEmailEvidenceList =
        new ArrayList<BDMEmailEvidenceVO>();
      for (final BDMEmailEvidenceVO evidenceEmailVO : emailEvidenceList) {
        if (evidenceEmailVO.isUseForAlertsInd()
          && emailEvidenceList.size() > 0) {
          for (final DynamicEvidenceDataDetails emailEvdItemDtals : existingEmailList) {
            final BDMEmailEvidenceVO evidenceEmail = new BDMEmailEvidenceVO();
            new BDMLifeEventUtil().setEvidenceData(evidenceEmail,
              emailEvdItemDtals);
            if (evidenceEmail.isUseForAlertsInd() && evidenceEmail
              .getEvidenceID() != evidenceEmailVO.getEvidenceID()) {
              evidenceEmail.setAlertFrequency(CuramConst.gkEmpty);
              evidenceEmail.setUseForAlertsInd(false);
              evidenceEmail
                .setEmailAddressChangeType(BDMEMAILADDRESSCHANGETYPE.UPDATE);
              finalEmailEvidenceList.add(evidenceEmail);
              break;
            }
          }
        }
      }
      finalEmailEvidenceList.addAll(emailEvidenceList);
      bdmEmailEvidence.createEmailEvidence(concernRole.getID(),
        finalEmailEvidenceList, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.ERR_EMAIL);
      e.printStackTrace();
    }

  }

  /**
   * Process Address Evidence updates from entity
   *
   * @param personEntity
   * @param concernRole
   */
  public void processAddressEvd(final Entity personEntity,
    final curam.participant.impl.ConcernRole concernRole) {

    try {

      final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

      final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
        bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
          concernRole.getID(), PDCConst.PDCADDRESS);

      // filter out evidence which are enddated
      final List<DynamicEvidenceDataDetails> existingAddressList =
        evidenceDataDetailsList.stream().filter(addressDtls -> addressDtls
          .getAttribute(toDateAttr).getValue().isEmpty())
          .collect(Collectors.toList());

      final List<DynamicEvidenceDataDetails> existingAddressEvdDtls;
      final List<DynamicEvidenceDataDetails> existingMailingAddressEvdDtls;

      new ArrayList<BDMAddressEvidenceVO>();

      final BDMAddressEvidenceVO residentialDSAddress =
        new BDMAddressEvidenceVO();
      final BDMAddressEvidenceVO residentialEvdAddress =
        new BDMAddressEvidenceVO();
      final BDMAddressEvidenceVO mailingDSAddress =
        new BDMAddressEvidenceVO();
      final BDMAddressEvidenceVO mailingEvdAddress =
        new BDMAddressEvidenceVO();

      // private address
      final String residentialAddressData = getAddress(personEntity);
      String mailingAddressData;
      // if same mailing address is not the same as residential
      if (personEntity
        .getTypedAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT)
        .equals(IEGYESNO.YES)) {
        // set to application input mailing address
        mailingAddressData = getMailingAddress(personEntity);
      } else {
        // set to residential
        mailingAddressData = residentialAddressData;
      }

      residentialDSAddress.setAddressData(residentialAddressData);
      mailingDSAddress.setAddressData(mailingAddressData);
      existingAddressEvdDtls = existingAddressList.size() > 0
        ? existingAddressList.stream()
          .filter(addressEvdDtls -> addressEvdDtls
            .getAttribute(BDMConstants.gkAddressType).getValue()
            .equals(CONCERNROLEADDRESSTYPE.PRIVATE))
          .collect(Collectors.toList())
        : new ArrayList<DynamicEvidenceDataDetails>();

      if (existingAddressEvdDtls.size() > 0) {
        new BDMLifeEventUtil().setEvidenceData(residentialEvdAddress,
          existingAddressEvdDtls.get(0));
        if (0 != residentialDSAddress.compareTo(residentialEvdAddress)) {
          // if there is an existing address modify it
          // BEGIN TASK-28473 Change Reason Field Is missing on Edit Evidence
          bdmEvidenceUtil.modifyAddressEvidence(concernRole.getID(),
            residentialAddressData, Date.getCurrentDate(),
            CONCERNROLEADDRESSTYPE.PRIVATE, Date.kZeroDate,
            residentialEvdAddress.isPreferredInd(), "",
            residentialEvdAddress.getEvidenceID(), null, CuramConst.gkEmpty,
            CuramConst.gkEmpty, CuramConst.gkEmpty, CuramConst.gkEmpty);
          // if required change null to valid change reason default is set to
          // reported by client
          // END TASK-28473
        }
      }

      existingMailingAddressEvdDtls = existingAddressList.size() > 0
        ? existingAddressList.stream()
          .filter(addressEvdDtls -> addressEvdDtls
            .getAttribute(BDMConstants.gkAddressType).getValue()
            .equals(CONCERNROLEADDRESSTYPE.MAILING))
          .collect(Collectors.toList())
        : new ArrayList<DynamicEvidenceDataDetails>();
      // Begin Task-61496 bug fix 61322
      // is new mailing address is entered
      final Boolean isMailingAddressUpdated = personEntity
        .getTypedAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS_UPDATE)
        .equals(IEGYESNO.YES);

      if (existingMailingAddressEvdDtls.size() > 0) {
        new BDMLifeEventUtil().setEvidenceData(mailingEvdAddress,
          existingMailingAddressEvdDtls.get(0));

        if (isMailingAddressUpdated) {

          final Date endDate = Date.getCurrentDate().addDays(-1);
          final long evidenceID =
            existingMailingAddressEvdDtls.get(0).getID();

          // end date the current existing identification evidence
          BDMEvidenceUtil.endDateEvidence(evidenceID, endDate,
            PDCConst.PDCADDRESS, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

          // create a new address evidence
          new BDMEvidenceUtil().createAddressEvidence(concernRole.getID(),
            mailingAddressData, Date.getCurrentDate(), "AT4", Date.kZeroDate,
            false, "");

        } else {
          // End Task-61496
          if (0 != mailingDSAddress.compareTo(mailingEvdAddress)) {
            // if there is an existing address modify it
            // BEGIN TASK-28473 Change Reason Field Is missing on Edit Evidence
            bdmEvidenceUtil.modifyAddressEvidence(concernRole.getID(),
              mailingAddressData, Date.getCurrentDate(),
              CONCERNROLEADDRESSTYPE.MAILING, Date.kZeroDate,
              mailingEvdAddress.isPreferredInd(), "",
              mailingEvdAddress.getEvidenceID(), null, CuramConst.gkEmpty,
              CuramConst.gkEmpty, CuramConst.gkEmpty, CuramConst.gkEmpty);
            // if required change null to valid change reason default is set to
            // reported by client;
            // END TASK-28473
          }
        }
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info("Error Occured in processAddressEvd");
      e.printStackTrace();
    }

  }

  /**
   * Get Address Entity
   *
   * @param personEntity
   * @param entityType
   */
  Entity getAddress(final Entity personEntity, final String entityType) {

    final Datastore dataStore = personEntity.getDatastore();
    final EntityType addressType = dataStore.getEntityType(entityType);
    if (addressType == null)
      return null;
    final Entity[] addresses = personEntity.getChildEntities(addressType);
    if (addresses.length == 0)
      return null;
    return addresses[0];
  }

  /**
   * Get Residential Address String
   *
   * @param personEntity
   * @param entityType
   */
  String getAddress(final Entity personEntity)
    throws AppException, InformationalException {

    String address = "";
    final Entity addressEntity =
      getAddress(personEntity, BDMConstants.gkResidentialAddress);
    if (null != addressEntity) {
      final Map<String, Object> fieldMap =
        DatastoreHelper.convertEntityToFieldMap(addressEntity);
      address = this.addressMappingStrategy.getAddressData(fieldMap);
    }
    if (!StringHelper.isEmpty(address))
      return address;
    final AddressData addressData = AddressDataFactory.newInstance();
    final LocaleStruct localeStruct = new LocaleStruct();
    final LayoutKey layoutKey = addressData.getLayoutForLocale(localeStruct);
    final OtherAddressData otherAddressData =
      addressData.getDefaultAddressDataForLayout(layoutKey);
    return otherAddressData.addressData;
  }

  /**
   * Get Mailing Address String
   *
   * @param personEntity
   * @param entityType
   */
  String getMailingAddress(final Entity personEntity)
    throws AppException, InformationalException {

    String address = "";
    final Entity addressEntity =
      getAddress(personEntity, BDMConstants.gkMailingAddress);
    if (null != addressEntity) {
      final Map<String, Object> fieldMap =
        DatastoreHelper.convertEntityToFieldMap(addressEntity);
      address = this.addressMappingStrategy.getAddressData(fieldMap);
    }
    return address;
  }

  /**
   * Check if International residential or mailing address is added, then map
   * the Intl datastore back to the respective address datastore
   *
   * @param entity {@link Entity}
   * @param key String
   * @return String or null if not found.
   */
  public void mapIntlAddressesToAddressDatastore(final Entity rootEntity)
    throws InformationalException, AppException {

    // Get entities for each address datastore, Assuming that all datastores
    // will exist
    // Assuming only 1 datastore of each type will ever exist
    final Entity[] personEntities = BDMApplicationEventsUtil.getEntities(
      rootEntity.getUniqueID(), BDMDatastoreConstants.PERSON, rootEntity);
    final Entity personEntity = personEntities[0];
    final Entity resAddrEntity = BDMApplicationEventsUtil.retrieveChildEntity(
      personEntity, BDMDatastoreConstants.RESIDENTIAL_ADDRESS);
    Entity mailAddrEntity = BDMApplicationEventsUtil.retrieveChildEntity(
      personEntity, BDMDatastoreConstants.MAILING_ADDRESS);

    // Check if Residential Address is non canadian address
    if (!resAddrEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
      .equals(COUNTRYEntry.CA.getCode())) {
      // Map Intl into Residential Address
      final Entity intlResAddrEntity =
        BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
          BDMDatastoreConstants.INTL_RESIDENTIAL_ADDRESS);
      // TASK - 26500 Moved the common logic to single function
      mapAndUpdateAddressEntity(intlResAddrEntity, resAddrEntity);
      resAddrEntity.update();

    }

    // final Check if non mailing canadian final address
    // check res final yes no then final check county
    if (personEntity
      .getAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT)
      .equals(IEGYESNO.NO)) {
      mailAddrEntity = resAddrEntity;
      mailAddrEntity.update();
    } else {
      // Check if Mailing Address is non canadian address
      if (!mailAddrEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
        .equals(COUNTRYEntry.CA.getCode())) {
        // Map Intl into Mailing Address
        final Entity intlMailAddrEntity =
          BDMApplicationEventsUtil.retrieveChildEntity(personEntity,
            BDMDatastoreConstants.MAILING_ADDRESS);
        // TASK - 26500 Moved the common logic to single function
        mapAndUpdateAddressEntity(intlMailAddrEntity, mailAddrEntity);

        mailAddrEntity.update();

      }
    }
  }

  // TASK - 26500 Moved the common logic to single function
  /**
   *
   * May 27, 2022
   * Map data from INTL Address Entity to Res/Mail Address Entity and removed
   * the old attribute from the entity
   *
   * @param source
   * @param target
   * void
   */
  public void mapAndUpdateAddressEntity(final Entity source,
    final Entity target) {

    if (source.hasAttribute(BDMDatastoreConstants.SUITE_NUM)) {
      target.setTypedAttribute(BDMDatastoreConstants.SUITE_NUM,
        source.getTypedAttribute(BDMDatastoreConstants.SUITE_NUM));
    } else {
      target.removeAttribute(BDMDatastoreConstants.SUITE_NUM);
    }
    if (source.hasAttribute(BDMDatastoreConstants.ADDRESS_LINE1)) {
      target.setTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
        source.getTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE1));
    } else {
      target.removeAttribute(BDMDatastoreConstants.ADDRESS_LINE1);
    }
    if (source.hasAttribute(BDMDatastoreConstants.ADDRESS_LINE2)) {
      target.setTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
        source.getTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE2));
    } else {
      target.removeAttribute(BDMDatastoreConstants.ADDRESS_LINE2);
    }
    if (source.hasAttribute(BDMDatastoreConstants.ADDRESS_LINE3)) {
      target.setTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE3,
        source.getTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE3));
    } else {
      target.removeAttribute(BDMDatastoreConstants.ADDRESS_LINE3);
    }
    if (source.hasAttribute(BDMDatastoreConstants.ADDRESS_LINE4)) {
      target.setTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE4,
        source.getTypedAttribute(BDMDatastoreConstants.ADDRESS_LINE4));
    } else {
      target.removeAttribute(BDMDatastoreConstants.ADDRESS_LINE4);
    }
    if (source.hasAttribute(BDMDatastoreConstants.CITY)) {
      target.setTypedAttribute(BDMDatastoreConstants.CITY,
        source.getTypedAttribute(BDMDatastoreConstants.CITY));
    } else {
      target.removeAttribute(BDMDatastoreConstants.CITY);
    }
    if (source.hasAttribute(BDMDatastoreConstants.PROVINCE)) {
      target.setTypedAttribute(BDMDatastoreConstants.PROVINCE,
        source.getTypedAttribute(BDMDatastoreConstants.PROVINCE));
    } else {
      target.removeAttribute(BDMDatastoreConstants.PROVINCE);
    }
    if (source.hasAttribute(BDMDatastoreConstants.STATE)) {
      target.setTypedAttribute(BDMDatastoreConstants.STATE,
        source.getTypedAttribute(BDMDatastoreConstants.STATE));
    } else {
      target.removeAttribute(BDMDatastoreConstants.STATE);
    }
    if (source.hasAttribute(BDMDatastoreConstants.ZIP_CODE)) {
      String zipCode =
        source.getTypedAttribute(BDMDatastoreConstants.ZIP_CODE).toString();
      if (zipCode.endsWith("-")) {
        zipCode = zipCode.substring(0, zipCode.length() - 1);
      }
      target.setTypedAttribute(BDMDatastoreConstants.ZIP_CODE, zipCode);
    } else {
      target.removeAttribute(BDMDatastoreConstants.ZIP_CODE);
    }
    if (source.hasAttribute(BDMDatastoreConstants.POSTAL_CODE)) {
      target.setTypedAttribute(BDMDatastoreConstants.POSTAL_CODE,
        source.getTypedAttribute(BDMDatastoreConstants.POSTAL_CODE));
    } else {
      target.removeAttribute(BDMDatastoreConstants.POSTAL_CODE);
    }
    target.update();
  }

  /**
   * Try get the Userlocale description for a codetableitem, if not try get the
   * default value. if neither return an empty string
   *
   * @param tableName expects CASEDETERMINATIONINTERVALRESULTEntry.TABLENAME
   * @param code expects CASEDETERMINATIONINTERVALRESULTEntry.ELIGIBLE.getCode()
   *
   */
  public static String getCodeTableDescriptionForUserLocale(
    final String tableName, final String code) {

    String codeTableItemDescription = new String();

    // try get user locale
    try {
      codeTableItemDescription =
        CodeTable.getOneItemForUserLocale(tableName, code);
    } catch (AppRuntimeException | AppException | InformationalException e) {

      Trace.kTopLevelLogger.warn(
        BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_PREFIX_LOCALE_DESC
          + tableName + CODE + code + BDMConstants.defaulDesc);

      // try get the default version instead
      try {
        codeTableItemDescription = CodeTable.getOneItem(tableName, code);
      } catch (AppRuntimeException | AppException
        | InformationalException e1) {

        Trace.kTopLevelLogger.warn(
          BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_PREFIX_LOCALE_DESC
            + tableName + CODE + code + BDMConstants.provideEmptyStringDesc);
      }
    }
    return codeTableItemDescription;
  }

  /**
   * Check if the integrated case has active PDCs, and return true or false
   *
   * @param integratedCaseID
   *
   */
  public static boolean
    integratedCasehasActivePDC(final long integratedCaseID) {

    try {
      final IntegratedCase integratedCaseObj =
        IntegratedCaseFactory.newInstance();
      final IntegratedCaseIDKey integratedCaseIDKey =
        new IntegratedCaseIDKey();
      integratedCaseIDKey.caseID = integratedCaseID;
      final ListICProductDeliveryDetails listICProductDeliveryDetails =
        integratedCaseObj.listProduct(integratedCaseIDKey);
      for (final ICProductDeliveryDetails icProductDeliveryDetails : listICProductDeliveryDetails.dtls) {
        if (icProductDeliveryDetails.statusCode.equals(CASESTATUS.ACTIVE)) {
          return true;
        }
      }
    } catch (final Exception e) {
      return false;
    }
    return false;
  }

  /**
   * Count active appeal case for a client
   *
   * @param participantRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static int
    countAppealCasesByParticipantID(final long participantRoleID)
      throws AppException, InformationalException {

    final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
    concernRoleIDKey.concernRoleID = participantRoleID;

    final CaseCount ret =
      (CaseCount) DynamicDataAccess.executeNs(CaseCount.class,
        concernRoleIDKey, false, countAppealCasesByParticipantID);

    return ret.value;

  }

  /**
   * Task 60485: DEV: Implement- Informational message
   * This utility method is used: To found out the only residential address is
   * ended by returning boolean value "true" or "false"
   *
   * @param evidenceDescriptorID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static boolean
    foundOnlyResidentialAddressEnded(final long evidenceDescriptorID)
      throws AppException, InformationalException {

    boolean foundOnlyResidentialEndedInd = false;
    boolean foundResidentialAddressNotEndedInd = false;

    boolean returnBoolean = false;

    final StringBuffer dynamicSQL = new StringBuffer();

    dynamicSQL.append(foundOnlyResidentialAddressEnded);
    dynamicSQL.append(" " + evidenceDescriptorID + ";");

    final CuramValueList<ConcernRoleAddressDtls> concernroleAddressDtlsLst =
      DynamicDataAccess.executeNsMulti(ConcernRoleAddressDtls.class, null,
        false, dynamicSQL.toString());

    for (int i = 0; i < concernroleAddressDtlsLst.size(); i++) {

      if (!concernroleAddressDtlsLst.item(i).endDate.isZero()
        && concernroleAddressDtlsLst.item(i).typeCode
          .equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {
        foundOnlyResidentialEndedInd = true;
      }

      if (concernroleAddressDtlsLst.item(i).endDate.isZero()
        && concernroleAddressDtlsLst.item(i).typeCode
          .equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {
        foundResidentialAddressNotEndedInd = true;
      }

    }
    if (foundOnlyResidentialEndedInd && !foundResidentialAddressNotEndedInd) {
      returnBoolean = true;
    } else {
      returnBoolean = false;
    }
    return returnBoolean;
  }

  /**
   * Method to check logged-in users role is BDMAdmin.
   *
   * @return boolean
   */
  public static boolean isBDMAdminUser()
    throws AppException, InformationalException {

    boolean isBDMAdmin = false;

    final String user = TransactionInfo.getProgramUser();

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = user;

    final UserRoleDetails userRoleDetails =
      UsersFactory.newInstance().readUserRole(usersKey);

    if (userRoleDetails.roleName.equalsIgnoreCase(BDMConstants.kBDMAdmin)
      || userRoleDetails.roleName.contains(BDMConstants.kBDMAdmin)) {
      isBDMAdmin = true;
    }

    return isBDMAdmin;
  }

  /**
   * This utility method to fetch the list of foreign identifier alternate IDs.
   * CONCERNROLE.CONCERNROLEID ---> key.concernRoleID
   * EVIDENCETYPE --> Identifications
   * CONCERNROLEALTERNATEID.TYPECODE
   * --> CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER
   * CONCERNROLEALTERNATEID.STATUSCODE
   * --> RECORDSTATUS.NORMAL
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMListOfForeignIDs
    getListOfForeignIdentifiers(final BDMFAConcernRoleKey key)
      throws AppException, InformationalException {

    final BDMListOfForeignIDs bdmListOfForeignIDs = new BDMListOfForeignIDs();

    final StringBuffer dynamicSQL = new StringBuffer();

    dynamicSQL.append(GET_LIST_OF_FOREIGNID_SQL);

    dynamicSQL.append(key.concernRoleID + " ");

    final CuramValueList<BDMForeignID> bdmListOfFIds = DynamicDataAccess
      .executeNsMulti(BDMForeignID.class, null, false, dynamicSQL.toString());

    BDMForeignID bdmForeignID = new BDMForeignID();

    if (bdmListOfFIds.size() > CuramConst.gkZero) {

      for (int i = 0; i < bdmListOfFIds.size(); i++) {

        bdmForeignID = new BDMForeignID();

        bdmForeignID = bdmListOfFIds.item(i);

        bdmListOfForeignIDs.bdmFId.addRef(bdmForeignID);

      }
    }

    return bdmListOfForeignIDs;
  }

  /**
   * Formats a text for displaying address
   *
   * @param addLine1
   * @param addLine2
   * @param city
   * @param prov
   * @param countryCode
   * @param postalCode
   * @return
   */
  public String formatAddressForDisplay(final String addLine1,
    final String addLine2, final String city, final String prov,
    final String countryCode, final String postalCode) {

    String addrText = "";

    if (!StringUtil.isNullOrEmpty(addLine1)) {
      addrText = addLine1;
    }
    if (!StringUtil.isNullOrEmpty(addLine2)) {
      addrText = addrText + BDMConstants.gkCommaSpace + addLine2;
    }
    if (!StringUtil.isNullOrEmpty(city)) {
      addrText = addrText + BDMConstants.gkCommaSpace + city;
    }
    if (!StringUtil.isNullOrEmpty(prov)) {
      addrText = addrText + BDMConstants.gkCommaSpace + prov;
    }
    if (!StringUtil.isNullOrEmpty(countryCode)) {
      addrText = addrText + BDMConstants.gkCommaSpace + countryCode;
    }
    if (!StringUtil.isNullOrEmpty(postalCode)) {
      addrText = addrText + BDMConstants.gkCommaSpace + postalCode;
    }

    if (addrText.startsWith(BDMConstants.gkCommaSpace)) {
      addrText = addrText.replaceFirst(BDMConstants.gkCommaSpace, "");
    }

    return addrText;
  }

  public static BDMPersonSearchDetailsResult getPersonPrimaryAlternateID(
    final BDMPersonSearchDetailsResult personSearchDetailsResultDtls)
    throws AppException, InformationalException {

    final Person personObj = PersonFactory.newInstance();
    final ProspectPerson prospectPersonObj =
      ProspectPersonFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    PersonKey personKey;
    final ConcernRole concernRoleObj = newInstance();
    for (int i =
      0; i < personSearchDetailsResultDtls.personSearchResult.dtlsList
        .size(); i++) {

      concernRoleKey.concernRoleID =
        personSearchDetailsResultDtls.personSearchResult.dtlsList
          .get(i).concernRoleID;
      final ConcernRoleDtls concernRoleDtls =
        concernRoleObj.read(concernRoleKey);
      if (CONCERNROLETYPE.PERSON.equals(concernRoleDtls.concernRoleType)) {
        personKey = new PersonKey();
        personKey.concernRoleID = concernRoleKey.concernRoleID;
        final PersonDtls personDtls = personObj.read(personKey);
        personSearchDetailsResultDtls.personSearchResult.dtlsList
          .get(i).primaryAlternateID = personDtls.primaryAlternateID;
        personSearchDetailsResultDtls.personSearchResult.dtlsList
          .get(i).xmlPersonData =
            BDMUtil.getPersonNameDetails(
              personSearchDetailsResultDtls.personSearchResult.dtlsList
                .get(i))
              .toString();
      } else if (CONCERNROLETYPE.PROSPECTPERSON
        .equals(concernRoleDtls.concernRoleType)) {
        final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();
        prospectPersonKey.concernRoleID = concernRoleKey.concernRoleID;
        final ProspectPersonDtls prospectPersonDtls =
          prospectPersonObj.read(prospectPersonKey);
        personSearchDetailsResultDtls.personSearchResult.dtlsList
          .get(i).primaryAlternateID = prospectPersonDtls.primaryAlternateID;
        personSearchDetailsResultDtls.personSearchResult.dtlsList
          .get(i).xmlPersonData =
            BDMUtil.getPersonNameDetails(
              personSearchDetailsResultDtls.personSearchResult.dtlsList
                .get(i))
              .toString();

      }
    }
    return personSearchDetailsResultDtls;
  }

  public static ContentPanelBuilder
    getPersonNameDetails(final BDMPersonSearchDetails details)
      throws AppException, InformationalException {

    final ContentPanelBuilder personNameDetails =
      ContentPanelBuilder.createPanel(CuramConst.gkPersonSearchName);

    final LocalisableString participantName =
      new LocalisableString(BPOPARTICIPANT.INF_PROSPECT_PERSON);

    participantName.arg(details.primaryAlternateID);

    LinkBuilder linkBuilder;

    if (details.concernRoleType.equals(CONCERNROLETYPE.PROSPECTPERSON)) {
      linkBuilder = LinkBuilder.createLink(details.primaryAlternateID,
        CuramConst.gkProspectPersonResolveHomePage);
    } else {
      linkBuilder = LinkBuilder.createLink(details.primaryAlternateID,
        CuramConst.gkPersonResolveHomePage);
    }
    linkBuilder.addParameter(CuramConst.gkPageParameterConcernRoleID,
      String.valueOf(details.concernRoleID));

    personNameDetails.addLinkItem(linkBuilder);

    if (details.concernRoleType.equals(CONCERNROLETYPE.PROSPECTPERSON)) {

      final ImageBuilder imageBuilder = ImageBuilder
        .createImage(CuramConst.gkIconProspectPerson, CuramConst.gkEmpty);

      imageBuilder.setImageResource(CuramConst.gkRendererImages);

      // Add prospect person alternative text
      final String duplicateText =
        new LocalisableString(BPOPARTICIPANT.INF_PROSPECT_PERSON)
          .toClientFormattedText();

      imageBuilder.setImageAltText(duplicateText);

      personNameDetails.addImageItem(imageBuilder);
    }

    return personNameDetails;
  }

  /**
   * Convert the address data into a map
   *
   * @param addressData
   * @return
   */
  public static Map<String, String>
    getAddressDataMap(final String addressData) {

    return Arrays.stream(addressData.split(CuramConst.gkNewLine))
      .filter(s -> s.contains(equalSign) && !s.endsWith(equalSign))
      .map(s -> s.split(equalSign))
      .collect(Collectors.toMap(s -> s[0], s -> s[1]));
  }

  /**
   * This is an utility method: which will give you the formatted address data.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getFormattedAddress(final AddressKey addressKey)
    throws AppException, InformationalException {

    String returnStrFormattedAddressData = CuramConst.gkEmpty;

    final Address addressObj = AddressFactory.newInstance();
    AddressDtls addressDtls = null;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    addressDtls = addressObj.read(notFoundIndicator, addressKey);

    if (!notFoundIndicator.isNotFound()) {
      if (addressDtls != null) {
        final OtherAddressData formattedAddressData = new OtherAddressData();
        formattedAddressData.addressData = addressDtls.addressData;
        addressObj.getLongFormat(formattedAddressData);
        returnStrFormattedAddressData =
          formattedAddressData.addressData.toString();
      }
    }
    return returnStrFormattedAddressData;
  }

  public static boolean isIOAdminUser()
    throws AppException, InformationalException {

    boolean isBDMIOAdmin = false;

    final String user = TransactionInfo.getProgramUser();

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = user;

    final UserRoleDetails userRoleDetails =
      UsersFactory.newInstance().readUserRole(usersKey);

    if (userRoleDetails.roleName.equalsIgnoreCase(BDMConstants.kIOAdmin)
      || userRoleDetails.roleName.contains(BDMConstants.kIOAdmin)
      || userRoleDetails.roleName.equalsIgnoreCase(BDMConstants.kIOProcessing)
      || userRoleDetails.roleName
        .equalsIgnoreCase(BDMConstants.kIOBenefitOfficer)
      || userRoleDetails.roleName
        .equalsIgnoreCase(BDMConstants.kIOSupervisor)) {
      isBDMIOAdmin = true;
    }

    return isBDMIOAdmin;
  }

  public static boolean isIOClientContact()
    throws AppException, InformationalException {

    boolean isBDMIOClientContact = false;

    final String user = TransactionInfo.getProgramUser();

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = user;

    final UserRoleDetails userRoleDetails =
      UsersFactory.newInstance().readUserRole(usersKey);

    if (userRoleDetails.roleName
      .equalsIgnoreCase(BDMConstants.kIOClientContact)
      || userRoleDetails.roleName.contains(BDMConstants.kIOClientContact)) {
      isBDMIOClientContact = true;
    }

    return isBDMIOClientContact;
  }

  /**
   * This utility method will allow to create a record in BDMEscalationLevel
   * Table with the given communicationID
   *
   * @param commID
   * @throws AppException
   * @throws InformationalException
   */
  public static void createTaskEscalationLevel(final long communicationID,
    final String escalationLevelTypCd)
    throws AppException, InformationalException {

    final BDMEscalationLevelDtls escalationLevelDtls =
      new BDMEscalationLevelDtls();
    escalationLevelDtls.bdmEscalationLevelID =
      UniqueIDFactory.newInstance().getNextID();
    escalationLevelDtls.communicationID = communicationID;
    escalationLevelDtls.escalationLevel = escalationLevelTypCd;

    BDMEscalationLevelFactory.newInstance().insert(escalationLevelDtls);

  }

  /**
   * This utility method is used for getting next escalation level.
   *
   * @param currentEscalationLevel
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String
    getNextEscalationLevel(final String currentEscalationLevel)
      throws AppException, InformationalException {

    String nextEscalationLevel = currentEscalationLevel;

    if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_1
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_2;
    } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_2
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_3;
    } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_3
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_4;
    } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_4
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_5;
    } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_5
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_6;
    } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_6
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_7;
    } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_7
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_8;
    } else if (BDMESCALATIONLEVELS.ESCALATION_LEVEL_8
      .equals(currentEscalationLevel)) {
      nextEscalationLevel = currentEscalationLevel;
    }

    return nextEscalationLevel;
  }

  /**
   * Get workQueueID by province of the currently logged in user in to the
   * system.
   *
   * @param currentLoggedInUserName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long
    getWorkQueueIDByProvOfLoggedInUser(final String currentLoggedInUserName)
      throws AppException, InformationalException {

    long workQueueID = CuramConst.kLongZero;

    final AddressElement addressElement = AddressElementFactory.newInstance();

    AddressKey addressKey = null;

    AddressElementDtls addressElementDtls = null;

    BDMWorkqueueProvinceLinkDtls bdmWorkqueueProvinceLinkDtls = null;

    BDMWorkqueueProvinceLinkKey bdmWorkqueueProvinceLinkKey = null;

    final Organization organization = OrganizationFactory.newInstance();

    final ReadLocationKey readLocationKey = new ReadLocationKey();

    final Users usersObj = UsersFactory.newInstance();

    final UsersKey usersKey = new UsersKey();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    usersKey.userName = currentLoggedInUserName;

    final UsersDtls usersDtls = usersObj.read(nfIndicator, usersKey);

    if (!nfIndicator.isNotFound()) {

      readLocationKey.locationKeyRef.locationID = usersDtls.locationID;

      final ReadLocationDetails readLocationDetails =
        organization.readLocation(readLocationKey);

      addressKey = new AddressKey();

      addressKey.addressID = readLocationDetails.locationDetails.addressID;

      final AddressElementDtlsList addressElementDtlsList =
        addressElement.readAddressElementDetails(addressKey);

      final int addressElementListSize = addressElementDtlsList.dtls.size();

      for (int k = 0; k < addressElementListSize; k++) {

        addressElementDtls = new AddressElementDtls();

        addressElementDtls = addressElementDtlsList.dtls.item(k);

        if (addressElementDtls.elementType
          .equals(BDMConstants.kADDRESSELEMENT_PROVINCE)
          || addressElementDtls.elementType
            .equals(BDMConstants.kADDRESSELEMENT_STATEPROV)) {

          bdmWorkqueueProvinceLinkDtls = new BDMWorkqueueProvinceLinkDtls();

          bdmWorkqueueProvinceLinkKey = new BDMWorkqueueProvinceLinkKey();
          bdmWorkqueueProvinceLinkKey.provinceTypeCd =
            addressElementDtls.upperElementValue;

          bdmWorkqueueProvinceLinkDtls = BDMWorkqueueProvinceLinkFactory
            .newInstance().read(bdmWorkqueueProvinceLinkKey);

          workQueueID = bdmWorkqueueProvinceLinkDtls.workQueueID;

          break;

        }

      }

    }

    return workQueueID;

  }

  /**
   * Utility method to raise TASK.CLOSE event and close the task.
   *
   * @param concernRoleID
   * @param taskID
   * @throws AppException
   * @throws InformationalException
   */
  public void raiseAndCloseTask(final long taskID)
    throws AppException, InformationalException {

    // Create a new Event
    final Event event = new Event();
    event.eventKey = TASK.CLOSED;

    event.primaryEventData = taskID;
    // Raise event
    EventService.raiseEvent(event);
  }

  /**
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Map<String, String> getCCTDescriptionToCodeMap()
    throws AppException, InformationalException {

    if (null == cctDescriptionMap) {
      cctDescriptionMap = new HashMap<>();
      // Populate the description from CCT as key and corresponding curam code
      // as
      // pair.

      // Here we need to put description as key since that's the text value
      // Interop layer/CCT will be sending back to Curam as a status update.
      final LinkedHashMap<String, String> codetableHashMap =
        CodeTable.getAllItems(BDMCCTCOMMUNICATIONSTATUS.TABLENAME,
          TransactionInfo.getProgramLocale());

      cctDescriptionMap = codetableHashMap.entrySet().stream()
        .collect(Collectors.<Entry<String, String>, String, String> toMap(
          Entry::getValue, Entry::getKey));

    }
    return cctDescriptionMap;
  }

  /**
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static HashMap<String, String> getCCTStatusToBDMStatusMap()
    throws AppException, InformationalException {

    if (null == cctStatusToBDMStatusMap) {
      cctStatusToBDMStatusMap = new HashMap<String, String>();
      // The following mappings are specified in Manage status FDD
      // CCT to Curam Mapping Status
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.ACTIVE_INCOMPLETE,
        COMMUNICATIONSTATUS.DRAFT);
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.ACTIVE_COMPLETE,
        COMMUNICATIONSTATUS.DRAFT);
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.PENDING_DELIVERY,
        COMMUNICATIONSTATUS.SUBMITTED);
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.FINISHED,
        COMMUNICATIONSTATUS.SENT);
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.FAILED_DELIVERY,
        COMMUNICATIONSTATUS.FAILED_DELIVERY);
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.PENDING_APPROVAL,
        COMMUNICATIONSTATUS.UNDER_REVIEW);
      cctStatusToBDMStatusMap.put(
        BDMCCTCOMMUNICATIONSTATUS.SUBMITTED_REJECTED,
        COMMUNICATIONSTATUS.QA_REJECTED);
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.CANCELED,
        COMMUNICATIONSTATUS.CANCELLED);
      cctStatusToBDMStatusMap.put(BDMCCTCOMMUNICATIONSTATUS.FAILED_OVERSIZED,
        COMMUNICATIONSTATUS.SUBMITTED_OVERSIZE);

    }
    return cctStatusToBDMStatusMap;
  }

  /**
   *
   * @param cctStatus
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getBDMStatusForCCTStatus(final String cctStatus)
    throws AppException, InformationalException {

    String bdmCommunicationStatus = "";
    if (getCCTStatusToBDMStatusMap().containsKey(cctStatus)) {
      bdmCommunicationStatus = getCCTStatusToBDMStatusMap().get(cctStatus);
    }
    return bdmCommunicationStatus;
  }

  /**
   * TASK 54344, 89066 Manage Allocation - R1
   * Get workQueueID by province of the currently logged in user in to the
   * system.
   *
   * @param currentLoggedInUserName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long
    getFEWorkQueueIDByProvOfLoggedInUser(final String currentLoggedInUserName)
      throws AppException, InformationalException {

    long workQueueID = CuramConst.kLongZero;

    AddressElementDtls addressElementDtls = null;

    BDMFEWorkqueueProvinceLinkDtls bdmFEWorkqueueProvinceLinkDtls = null;

    BDMFEWorkqueueProvinceLinkKey bdmFEWorkqueueProvinceLinkKey = null;

    final AddressElementDtlsList addressElementDtlsList =
      getLoggedInUserAddressDtlsList(currentLoggedInUserName);

    if (null != addressElementDtlsList) {
      final int addressElementListSize = addressElementDtlsList.dtls.size();

      for (int k = 0; k < addressElementListSize; k++) {

        addressElementDtls = new AddressElementDtls();

        addressElementDtls = addressElementDtlsList.dtls.item(k);

        if (addressElementDtls.elementType.equals("PROV")
          || addressElementDtls.elementType.equals("STATEPROV")) {

          bdmFEWorkqueueProvinceLinkDtls =
            new BDMFEWorkqueueProvinceLinkDtls();

          bdmFEWorkqueueProvinceLinkKey = new BDMFEWorkqueueProvinceLinkKey();
          bdmFEWorkqueueProvinceLinkKey.provinceTypeCd =
            addressElementDtls.upperElementValue;

          bdmFEWorkqueueProvinceLinkDtls = BDMFEWorkqueueProvinceLinkFactory
            .newInstance().read(bdmFEWorkqueueProvinceLinkKey);

          workQueueID = bdmFEWorkqueueProvinceLinkDtls.workQueueID;

          break;

        }
      }
    }

    return workQueueID;

  }

  /**
   * TASK 54344, 89066 Manage Allocation - R1
   * This method will return address details list based on logged in user
   * location.
   *
   * @param currentLoggedInUserName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private AddressElementDtlsList
    getLoggedInUserAddressDtlsList(final String currentLoggedInUserName)
      throws AppException, InformationalException {

    AddressElementDtlsList addressElementDtlsList = null;
    final AddressElement addressElement = AddressElementFactory.newInstance();
    final UsersKey usersKey = new UsersKey();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    usersKey.userName = currentLoggedInUserName;
    final Users usersObj = UsersFactory.newInstance();
    final UsersDtls usersDtls = usersObj.read(nfIndicator, usersKey);
    AddressKey addressKey = null;
    if (!nfIndicator.isNotFound()) {
      final ReadLocationKey readLocationKey = new ReadLocationKey();
      final Organization organization = OrganizationFactory.newInstance();
      readLocationKey.locationKeyRef.locationID = usersDtls.locationID;

      final ReadLocationDetails readLocationDetails =
        organization.readLocation(readLocationKey);
      addressKey = new AddressKey();
      addressKey.addressID = readLocationDetails.locationDetails.addressID;

      addressElementDtlsList =
        addressElement.readAddressElementDetails(addressKey);

    }
    return addressElementDtlsList;
  }

  /**
   * Retrive alternate id of a person when correspondence tracking number is
   * provided
   *
   * @param key
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  public static String
    getAlternateIDByTrackingNumber(final BDMPersonSearchKey1 key)
      throws AppException, InformationalException {

    List<String> alternalteIDList = new ArrayList<String>();
    String altId = "";
    long trackingNum = 0;
    try {
      trackingNum = Long.parseLong(key.corrTrackingNumber);

      final BDMSearchByTrackingNumberKey trackingKey =
        new BDMSearchByTrackingNumberKey();

      trackingKey.trackingNumber = trackingNum;

      final BDMPersonSearchResultByTrackingNumList bdmPersonSearchResultByTrackingNumList =
        BDMPersonFactory.newInstance().searchByTrackingNumber(trackingKey);

      if (!bdmPersonSearchResultByTrackingNumList.dtls.isEmpty()) {
        alternalteIDList =
          getPersonSearchResult(bdmPersonSearchResultByTrackingNumList);
      } else
        altId = String.valueOf(0);

      if (!alternalteIDList.isEmpty() && alternalteIDList.size() == 1) {
        altId = alternalteIDList.get(0);
      }

    } catch (final NumberFormatException exception) {
      Trace.kTopLevelLogger
        .error(ERR_INVALID_TRACKING_NUM + exception.getMessage());

      altId = String.valueOf(0);

    }

    return altId;
  }

  private static List<String>
    getPersonSearchResult(final BDMPersonSearchResultByTrackingNumList str) {

    final List<String> alternateIDList = new ArrayList<>();
    for (int i = 0; i < str.dtls.size(); i++) {

      final BDMPersonSearchResultByTrackingNum obj = str.dtls.get(i);

      alternateIDList.add(obj.primaryAlternateID);

    }
    return alternateIDList;

  }

  /**
   * Method to call SIN-SIR Webservice call
   *
   * @param sinNumber
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  public void callSINSIR(final String sinNumber, final long concernRoleID)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.sl.struct.BDMPersonKey bdmPersonKey =
      new curam.ca.gc.bdm.sl.struct.BDMPersonKey();
    bdmPersonKey.concernRoleID = concernRoleID;
    // final long concernRoleID = bdmPersonKey.concernRoleID;

    final BDMPersonIdentificationDetails identificationDetails =
      BDMPDCPersonFactory.newInstance()
        .readPersonIdentificationDetails(bdmPersonKey);

    if (!StringUtil.isNullOrEmpty(sinNumber)
      && !sinNumber.equals(identificationDetails.existingSINNumber)) {

      final long sinEvidenceID =
        identificationDetails.identificationEvidenceID;

      final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVOCurrent =
        bdmIdentificationEvidence.getSINEvidenceValueObject(concernRoleID);

      // If current SIN matches SIN then do not update
      if (bdmIdentificationEvidenceVOCurrent.getAlternateID() == null
        || !bdmIdentificationEvidenceVOCurrent.getAlternateID()
          .equals(sinNumber)) {

        final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVOUpdate =
          new BDMIdentificationEvidenceVO();
        bdmIdentificationEvidenceVOUpdate.setAlternateID(sinNumber);
        bdmIdentificationEvidenceVOUpdate
          .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
        bdmIdentificationEvidenceVOUpdate.setPreferredInd(true);

        if (sinEvidenceID != 0) {
          bdmIdentificationEvidenceVOUpdate.setEvidenceID(sinEvidenceID);
        } else {
          bdmIdentificationEvidenceVOUpdate.setEvidenceID(0);
        }

        bdmIdentificationEvidenceVOUpdate.setFromDate(Date.getCurrentDate());
        // Remove SIN SIR Response evidence if it exists.
        new BDMSINIntegrityCheckUtil()
          .removeExistingSINSIRResponceEvidence(concernRoleID);

        bdmIdentificationEvidence.createSINIdentificationEvidence(
          concernRoleID, null, bdmIdentificationEvidenceVOUpdate, "", null);
      }

      // Calling SIN Integrity Check process
      final BDMIntegrityCheckKey integrityCheckKey =
        new BDMIntegrityCheckKey();
      integrityCheckKey.concernRoleID = concernRoleID;
      BDMIntegrityCheckFactory.newInstance()
        .sinIntegrityCheckOnPerson(integrityCheckKey);
    }
  }

  /**
   *
   * This method is to update Evidence while Registering a person
   * OR Registering a Prospect Person as Person
   * Task - 93103
   *
   * @param bdmReceivedFrom
   * @param countryCode
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  public void updateEvidenceAttributes(final String bdmReceivedFrom,
    final String countryCode, final long concernRoleID)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;

    final long participantDataCaseID = ParticipantDataCaseFactory
      .newInstance().getParticipantDataCase(concernRoleKey).caseID;

    // Get EvidenceIDs from the case
    final CaseIDKey caseKey = new CaseIDKey();
    caseKey.caseID = participantDataCaseID;

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();

    final CaseIDParticipantIDStatusCode caseIDParticipantIDStatusCode =
      new CaseIDParticipantIDStatusCode();

    caseIDParticipantIDStatusCode.caseID = participantDataCaseID;
    caseIDParticipantIDStatusCode.participantID =
      concernRoleKey.concernRoleID;
    caseIDParticipantIDStatusCode.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
      evidenceDescriptorObj
        .searchActiveByCaseIDParticipantID(caseIDParticipantIDStatusCode);

    final String[] evidenceList =
      BDMConstants.kBDMvidencesToUpdate.split(CuramConst.gkComma);

    for (final EvidenceDescriptorDtls evidenceDescriptorDtls : evidenceDescriptorDtlsList.dtls) {
      final EvidenceServiceInterface evidenceServiceInterface =
        EvidenceGenericSLFactory.instance(evidenceDescriptorDtls.relatedID);

      if (Arrays.asList(evidenceList)
        .contains(evidenceDescriptorDtls.evidenceType)) {

        final EvidenceMap map =
          curam.core.sl.infrastructure.impl.EvidenceController
            .getEvidenceMap();
        final StandardEvidenceInterface standardEvidenceInterface =
          map.getEvidenceType(evidenceDescriptorDtls.evidenceType);

        final EIEvidenceKey evidenceKey = new EIEvidenceKey();
        evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        evidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;

        final DynamicEvidenceDataDetails evidenceData =
          (DynamicEvidenceDataDetails) standardEvidenceInterface
            .readEvidence(evidenceKey);

        // update the bdmReceivedFrom
        final DynamicEvidenceDataAttributeDetails bdmReceivedFromObj =
          evidenceData.getAttribute(BDMConstants.kBDMRECEIVEDFROM_ATTR);

        final CodeTableItem receivedFromItem =
          new CodeTableItem(BDMRECEIVEDFROM.TABLENAME, bdmReceivedFrom);
        final CodeTableItem countryItem =
          new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, countryCode);

        DynamicEvidenceTypeConverter.setAttribute(bdmReceivedFromObj,
          receivedFromItem);

        // update the bdmReceivedFromCountry
        final DynamicEvidenceDataAttributeDetails bdmReceivedCountryObj =
          evidenceData
            .getAttribute(BDMConstants.kBDMRECEIVEDFROMCOUNTRY_ATTR);

        DynamicEvidenceTypeConverter.setAttribute(bdmReceivedCountryObj,
          countryItem);

        // update the BDMMODEOFRECEIPT
        final DynamicEvidenceDataAttributeDetails attributeObj =
          evidenceData.getAttribute(BDMConstants.kBDMMODEOFRECEIPT_ATTR);

        if (bdmReceivedFrom
          .equalsIgnoreCase(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT)) {

          final CodeTableItem modeOfReceiptItem = new CodeTableItem(
            BDMMODEOFRECEIPT.TABLENAME, BDMMODEOFRECEIPT.LIAISON);

          DynamicEvidenceTypeConverter.setAttribute(attributeObj,
            modeOfReceiptItem);
        } else {
          BDMEvidenceUtil.setDataAttributeNoCheck(
            BDMConstants.kBDMMODEOFRECEIPT_ATTR, null, evidenceData);
        }
        TransactionInfo.setFacadeScopeObject("prospectAsPersonIndicator",
          false);

        // evidence descriptor details for this evidence
        final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
          new RelatedIDAndEvidenceTypeKey();
        relatedIDAndTypeKey.relatedID = evidenceDescriptorDtls.relatedID;
        relatedIDAndTypeKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;

        // set descriptor attributes
        final EvidenceDescriptorDetails descriptor =
          new EvidenceDescriptorDetails();
        descriptor.evidenceType = evidenceDescriptorDtls.evidenceType;
        descriptor.caseID = evidenceDescriptorDtls.caseID;
        descriptor.receivedDate = Date.getCurrentDate();
        descriptor.participantID = evidenceDescriptorDtls.participantID;
        descriptor.evidenceDescriptorID =
          evidenceDescriptorDtls.evidenceDescriptorID;
        descriptor.versionNo = evidenceDescriptorDtls.versionNo;
        descriptor.changeReason = EVIDENCECHANGEREASON.SHAREDFROMOTHERCASE;

        final EvidenceTypeKey eType = new EvidenceTypeKey();
        eType.evidenceType = evidenceDescriptorDtls.evidenceType;

        // get Latest Version of Evidence
        final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
        final EvidenceServiceInterface evidenceServiceInterfaceLatest =
          EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

        genericDtls.setData(evidenceData);
        genericDtls.setDescriptor(descriptor);
        genericDtls.setCaseIdKey(descriptor.caseID);

        evidenceServiceInterfaceLatest.setBypassSLValidations(true);
        evidenceServiceInterfaceLatest.modifyEvidence(genericDtls);

      }
    }
  }

  /**
   * Helper method to filter search results by unit number, province, postal and
   * country for customized all participant search (i.e., third party (contact)
   * search)
   *
   * @param participantSearchResult
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static AllParticipantSearchResult filterAddressForAllParticipant(
    final AllParticipantSearchResult participantSearchResult,
    final BDMThirdPartyParticipantSearchKey key)
    throws AppException, InformationalException {

    final String provinceKey = key.stateProvince.trim().toUpperCase();
    final String countryKey = key.countryCode.toUpperCase();
    final String postalCodeKey = key.postalCode.trim().toUpperCase();
    final String unitNumberKey = key.unitNumber.toUpperCase();

    final boolean isCheckProvince = !provinceKey.isEmpty();
    final boolean isCheckCountry = !countryKey.isEmpty();
    final boolean isCheckPostalCode = !postalCodeKey.isEmpty();
    final boolean isCheckUnitNumber = !unitNumberKey.isEmpty();

    if (!isCheckProvince && !isCheckCountry && !isCheckPostalCode
      && !isCheckUnitNumber)
      return participantSearchResult;

    AllParticipantSearchResult filteredParticipantSearchResult =
      new AllParticipantSearchResult();

    final Set<Long> uniqueCrIDs = new HashSet<>();

    try {
      // initilization of variables
      final ConcernRoleAddress concernroleAddressObj =
        ConcernRoleAddressFactory.newInstance();

      final AddressElement addressElement =
        AddressElementFactory.newInstance();

      // for each list of matched records, further match with Unit Number
      for (final AllParticipantSearchDetails details : participantSearchResult.dtlsList) {

        // get AddressID of current ConcernroleID
        final AddressKey addressKey = new AddressKey();

        try {
          final ConcernRoleKey concernroleKey = new ConcernRoleKey();
          concernroleKey.concernRoleID = details.concernRoleID;
          final ConcernRoleAddressKey concernRoleAddressKey =
            new ConcernRoleAddressKey();
          concernRoleAddressKey.concernRoleAddressID = concernroleAddressObj
            .readConcernRoleAddressDetailsByConcernRoleID(
              concernroleKey).concernRoleAddressID;
          addressKey.addressID =
            concernroleAddressObj.read(concernRoleAddressKey).addressID;
        } catch (final RecordNotFoundException rnfe) {
          // address does not exist, skip this record
          continue;
        }

        // Read Address Elements for a given Address ID
        final AddressElementDtlsList addressElementDtlsList =
          addressElement.readAddressElementDetails(addressKey);

        boolean isProvinceMatched = false;
        boolean isCountryMatched = false;
        boolean isPostalCodeMatched = false;
        boolean isUnitNumberMatched = false;

        final CodeTableAdmin codeTableAdminObj =
          CodeTableAdminFactory.newInstance();

        // Search Results
        for (final AddressElementDtls addrElemDtls : addressElementDtlsList.dtls) {

          if (addrElemDtls.elementType.equals(ADDRESSELEMENTTYPE.COUNTRY)) {
            if (isCheckCountry) {
              isCountryMatched =
                addrElemDtls.upperElementValue.equals(countryKey);
            } else {
              isCountryMatched = true;
            }
          } else if (addrElemDtls.elementType
            .equals(ADDRESSELEMENTTYPE.POSTCODE)) {

            if (isCheckPostalCode) {
              isPostalCodeMatched =
                addrElemDtls.upperElementValue.contains(postalCodeKey);
            } else {
              isPostalCodeMatched = true;
            }

          } else if (addrElemDtls.elementType
            .equals(ADDRESSELEMENTTYPE.ZIP)) {

            if (isCheckPostalCode) {
              isPostalCodeMatched =
                addrElemDtls.upperElementValue.contains(postalCodeKey);
            } else {
              isPostalCodeMatched = true;
            }
          } else if (addrElemDtls.elementType
            .equals(ADDRESSELEMENTTYPE.PROVINCE)) {

            if (isCheckProvince) {
              // Search province name in code table description
              final CodeTableItemDetailsList list = codeTableAdminObj
                .searchByCodeTableItemDescription(PROVINCETYPE.TABLENAME,
                  CuramConst.gkPercentage + provinceKey);

              for (int count = 0; count < list.dtls.size(); count++) {
                if (addrElemDtls.upperElementValue
                  .equals(list.dtls.get(count).code)) {
                  isProvinceMatched = true;
                  break;
                }
              } // end for
            } else {
              isProvinceMatched = true;
            }
          } else if (addrElemDtls.elementType
            .equals(ADDRESSELEMENTTYPE.STATEPROV)) {

            if (isCheckProvince) {
              final CodeTableItemDetailsList list = codeTableAdminObj
                .searchByCodeTableItemDescription(ADDRESSSTATE.TABLENAME,
                  CuramConst.gkPercentage + provinceKey);

              for (int count = 0; count < list.dtls.size(); count++) {
                if (addrElemDtls.upperElementValue
                  .equals(list.dtls.get(count).code)) {
                  isProvinceMatched = true;
                  break;
                }
              } // end for
            } else {
              isProvinceMatched = true;
            }
          } else if (addrElemDtls.elementType
            .equals(ADDRESSELEMENTTYPE.BDMSTPROV_X)) {

            if (isCheckProvince) {
              // Added for international addresses
              isProvinceMatched =
                addrElemDtls.upperElementValue.contains(provinceKey);
            } else {
              isProvinceMatched = true;
            }
          }
          // START: Task 93506: DEV: Address Format Search updates
          else if (addrElemDtls.elementType.equals(ADDRESSELEMENTTYPE.APT)) {

            if (isCheckUnitNumber) {
              isUnitNumberMatched =
                addrElemDtls.upperElementValue.equals(unitNumberKey);
            } else {
              isUnitNumberMatched = true;
            }
          }
        }

        if (!(isProvinceMatched && isCountryMatched && isPostalCodeMatched
          && isUnitNumberMatched)) {
          continue;
        }

        // add matched record to return list
        if (!uniqueCrIDs.contains(details.concernRoleID)) {
          filteredParticipantSearchResult.dtlsList.addRef(details);
          uniqueCrIDs.add(details.concernRoleID);
        }
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(
        BDMConstants.BDM_LOGS_PREFIX + BDMConstants.ERR_ADDRESS_FILTER + e);
      // if there is any exception then return the original list
      filteredParticipantSearchResult = participantSearchResult;

    }

    return filteredParticipantSearchResult;
  }

  // START : Bug 110516: Unable to add an existing Person/Prospect Person as
  // Third Party Contact
  /**
   * This method is to get the client's specified type of address.
   *
   * @param concernRoleID
   * @param addressType
   * @return OtherAddressData
   *
   * @throws AppException
   * @throws InformationalException
   */
  public OtherAddressData getAddressDataByConcernRole(
    final long concernRoleID, final String addressType)
    throws AppException, InformationalException {

    final OtherAddressData addressData = new OtherAddressData();

    // get all the addresses for the concernRole

    final curam.core.struct.ConcernRoleIDStatusCodeKey key =
      new curam.core.struct.ConcernRoleIDStatusCodeKey();
    key.concernRoleID = concernRoleID;
    key.statusCode = RECORDSTATUS.NORMAL;

    final AddressReadMultiDtlsList addressList = ConcernRoleAddressFactory
      .newInstance().searchAddressesByConcernRoleIDAndStatus(key);

    // get the specified address
    for (final AddressReadMultiDtls details : addressList.dtls) {
      if (addressType.equalsIgnoreCase(details.typeCode)) {
        final AddressKey addressKey = new AddressKey();
        addressKey.addressID = details.addressID;
        addressData.addressData = details.addressData;
        break;
      }
    }

    return addressData;
  }

  // END Bug 110516: Unable to add an existing Person/Prospect Person as Third
  // Party Contact

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method is used to register representative.
   *
   * @param thirdPartyEvd
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public RepresentativeRegistrationDetails registerRepresentative(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd)
    throws AppException, InformationalException {

    final Representative representativeObj =
      curam.core.sl.fact.RepresentativeFactory.newInstance();

    final RepresentativeRegistrationDetails representativeRegistrationDetails =
      new RepresentativeRegistrationDetails();

    representativeRegistrationDetails.representativeDtls.representativeName =
      thirdPartyEvd.thirdPartyDetails.firstName + " "
        + thirdPartyEvd.thirdPartyDetails.lastName;

    // START - BUG 100608
    // setting the mailing address as default for representative
    if (!thirdPartyEvd.thirdPartyAdditionalDetails.isMailingAddSame) {
      representativeRegistrationDetails.representativeRegistrationDetails.addressData =
        thirdPartyEvd.thirdPartyAdditionalDetails.mailingAddress;
    } else {
      representativeRegistrationDetails.representativeRegistrationDetails.addressData =
        thirdPartyEvd.thirdPartyAdditionalDetails.residentialAddress;
    }
    // END - BUG 100608

    representativeRegistrationDetails.representativeRegistrationDetails.registrationDate =
      Date.getCurrentDate();
    representativeRegistrationDetails.representativeRegistrationDetails.sensitivity =
      SENSITIVITY.DEFAULTCODE;
    // create phone number
    if (thirdPartyEvd.thirdPartyAdditionalDetails.phoneType.length() > 0
      && thirdPartyEvd.thirdPartyAdditionalDetails.phoneNumber.length() > 0) {
      representativeRegistrationDetails.representativeRegistrationDetails.phoneAreaCode =
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneAreaCode;
      final String phoneCountryCode = getPhoneCountryCode(
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneCountryCode);

      validatePhoneNumberDetails(phoneCountryCode,
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneAreaCode,
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneNumber,
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneExtension);

      if (!StringUtil.isNullOrEmpty(phoneCountryCode)) {
        representativeRegistrationDetails.representativeRegistrationDetails.phoneCountryCode =
          phoneCountryCode;
      }

      representativeRegistrationDetails.representativeRegistrationDetails.phoneExtension =
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneExtension;
      representativeRegistrationDetails.representativeRegistrationDetails.phoneNumber =
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneNumber;
      representativeRegistrationDetails.representativeRegistrationDetails.phoneType =
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneType;
      representativeRegistrationDetails.representativeRegistrationDetails.startDate =
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneFromDate;
      representativeRegistrationDetails.representativeRegistrationDetails.endDate =
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneToDate;

    }

    final curam.ca.gc.bdm.sl.intf.Representative representative =
      RepresentativeFactory.newInstance();

    representative.registerRepresentative(representativeRegistrationDetails);
    // START BUG: 100116

    if (thirdPartyEvd.thirdPartyAdditionalDetails.phoneType.length() > 0
      && thirdPartyEvd.thirdPartyAdditionalDetails.phoneNumber.length() > 0) {

      final ConcernRolePhoneNumber concernRolePhoneNumberObj =
        ConcernRolePhoneNumberFactory.newInstance();

      final PhoneConcernRoleIDTypeKey phoneConcernRoleIDTypeKey =
        new PhoneConcernRoleIDTypeKey();

      phoneConcernRoleIDTypeKey.concernRoleID =
        representativeRegistrationDetails.representativeDtls.concernRoleID;
      phoneConcernRoleIDTypeKey.typeCode =
        thirdPartyEvd.thirdPartyAdditionalDetails.phoneType;

      final PhoneConcernRoleIDTypeDtlsList phoneConcernRoleIDTypeDtlsList =
        concernRolePhoneNumberObj
          .searchPhonesByConcernRoleType(phoneConcernRoleIDTypeKey);

      for (final PhoneConcernRoleIDTypeDtls phoneConcernRoleIDTypeDtls : phoneConcernRoleIDTypeDtlsList.dtls) {
        // Insert the recored in BDMPhoneNumber Table now for primary phone
        // number
        final BDMPhoneNumberDtls bdmPhoneNumberDtls =
          new BDMPhoneNumberDtls();
        final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
        bdmPhoneNumberDtls.bdmPhoneNumberID = uniqueIDObj.getNextID();
        bdmPhoneNumberDtls.phoneNumberID =
          phoneConcernRoleIDTypeDtls.phoneNumberID;
        bdmPhoneNumberDtls.phoneCountryCode =
          thirdPartyEvd.thirdPartyAdditionalDetails.phoneCountryCode;
        BDMPhoneNumberFactory.newInstance().insert(bdmPhoneNumberDtls);
      }
    }
    //// END BUG: 100116

    if (thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneType.length() > 0
      && thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneNumber
        .length() > 0) {
      final String altPhoneCountryCode = getPhoneCountryCode(
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneCountryCode);
      validatePhoneNumberDetails(altPhoneCountryCode,
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneAreaCode,
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneNumber,
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneExtension);

      final ParticipantPhoneDetails participantPhoneDetails =
        new ParticipantPhoneDetails();

      participantPhoneDetails.concernRoleID =
        representativeRegistrationDetails.representativeDtls.concernRoleID;
      participantPhoneDetails.startDate =
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneFromDate;
      participantPhoneDetails.endDate =
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneToDate;
      participantPhoneDetails.typeCode = PHONETYPEEntry
        .get(thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneType)
        .getCode();
      participantPhoneDetails.phoneAreaCode =
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneAreaCode;
      participantPhoneDetails.phoneCountryCode =
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneCountryCode;
      participantPhoneDetails.phoneNumber =
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneNumber;
      participantPhoneDetails.phoneExtension =
        thirdPartyEvd.thirdPartyAdditionalDetails.altPhoneExtension;

      final BDMPDCPhoneNumber bdmpdcPhoneNumber =
        BDMPDCPhoneNumberFactory.newInstance();

      bdmpdcPhoneNumber.insert(participantPhoneDetails);
    }

    if (!StringUtil.isNullOrEmpty(
      thirdPartyEvd.thirdPartyAdditionalDetails.contactEmailAddress)) {
      BDMEvidenceUtil.createEmailAddressEvidence(
        representativeRegistrationDetails.representativeDtls.concernRoleID,
        thirdPartyEvd.thirdPartyAdditionalDetails.contactEmailAddress,
        thirdPartyEvd.thirdPartyAdditionalDetails.contactEmailType);
    }

    // START - BUG 100608 - inserting non-mandatory residential address
    registerResidentialAddress(
      representativeRegistrationDetails.representativeDtls.concernRoleID,
      thirdPartyEvd);

    // modify preferred Language
    if (!StringUtil
      .isNullOrEmpty(thirdPartyEvd.thirdPartyDetails.languagePref)) {
      final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID =
        representativeRegistrationDetails.representativeDtls.concernRoleID;
      ConcernRoleDtls concernRoleDtls;
      concernRoleDtls = concernRoleObj.read(concernRoleKey);
      // update preferred language.
      concernRoleDtls.preferredLanguage =
        thirdPartyEvd.thirdPartyDetails.languagePref;
      ConcernRoleFactory.newInstance().modify(concernRoleKey,
        concernRoleDtls);
    }
    return representativeRegistrationDetails;

  }

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method is used to insert caseparticipant role
   *
   * @param caseID
   * @param concernRoleID
   * @param caseParticipantType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long insertCaseParticipantRole(final long caseID,
    final long participantRoleID, final String caseParticipantType)
    throws AppException, InformationalException {

    final CaseParticipantRole caseParticipantRoleObj =
      CaseParticipantRoleFactory.newInstance();

    final CaseParticipantRoleDetails caseParticipantRole_boDetails =
      new CaseParticipantRoleDetails();

    caseParticipantRole_boDetails.dtls.caseID = caseID;

    caseParticipantRole_boDetails.dtls.participantRoleID = participantRoleID;

    caseParticipantRole_boDetails.dtls.fromDate = Date.getCurrentDate();
    caseParticipantRole_boDetails.dtls.typeCode = caseParticipantType;

    caseParticipantRoleObj
      .insertCaseParticipantRole(caseParticipantRole_boDetails);
    return caseParticipantRole_boDetails.dtls.caseParticipantRoleID;
  }

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method is used to create mailing address for representative.
   *
   * @param representativeConcernRoleID
   * @param thirdPartyEvd
   * @throws AppException
   * @throws InformationalException
   */
  public void registerResidentialAddress(
    final long representativeConcernRoleID,
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd)
    throws AppException, InformationalException {

    final MaintainParticipantAddressDetails residentialAddress =
      new MaintainParticipantAddressDetails();

    final boolean isResidentialAddressEmpty = this.isAddressEmpty(
      thirdPartyEvd.thirdPartyAdditionalDetails.residentialAddress);

    if (!isResidentialAddressEmpty) {
      residentialAddress.addressDetails.addressData =
        thirdPartyEvd.thirdPartyAdditionalDetails.residentialAddress;
      residentialAddress.addressDetails.caseID =
        thirdPartyEvd.thirdPartyDetails.caseID;
      residentialAddress.addressDetails.concernRoleID =
        representativeConcernRoleID;
      residentialAddress.addressDetails.startDate = Date.getCurrentDate();
      residentialAddress.addressDetails.typeCode =
        CONCERNROLEADDRESSTYPE.DEFAULTCODE;

      ParticipantFactory.newInstance().createAddress(residentialAddress);

    }
  }

  public static DuplicateForConcernRoleDtlsList
    getDuplicatePersonOfOriginal(final long originalConcernRoleID)
      throws AppException, InformationalException {

    // Start - Get duplication person of the original person, if any...
    final curam.core.sl.entity.intf.ConcernRoleDuplicate concernRoleDuplicateObj =
      ConcernRoleDuplicateFactory.newInstance();
    DuplicateForConcernRoleDtlsList duplicateForConcernRoleDtlsList =
      new DuplicateForConcernRoleDtlsList();

    // Set an indicator if this concern has duplicates
    final curam.core.struct.ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new curam.core.struct.ConcernRoleIDStatusCodeKey();

    // Set the concern role id and status
    concernRoleIDStatusCodeKey.concernRoleID = originalConcernRoleID;
    concernRoleIDStatusCodeKey.statusCode = DUPLICATESTATUS.UNMARKED;

    // Check if this concern role has any duplicates
    duplicateForConcernRoleDtlsList = concernRoleDuplicateObj
      .searchByOriginalConcernRoleIDNotStatus(concernRoleIDStatusCodeKey);

    if (duplicateForConcernRoleDtlsList.dtls.size() > 0) {
      for (final DuplicateForConcernRoleDtls duplicateForConcernRoleDtls : duplicateForConcernRoleDtlsList.dtls) {
        final ParticipantCommunicationKey duplicateConcernKey =
          new ParticipantCommunicationKey();
        duplicateConcernKey.participantCommKey.concernRoleID =
          duplicateForConcernRoleDtls.duplicateConcernRoleID;
      }
    }
    return duplicateForConcernRoleDtlsList;
  }

  /**
   * Method to get the existing privacy request tasks for the person
   *
   * @param bdmCncrCmmRmKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Long
    getExistingTaskIDForPerson(final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
      throws AppException, InformationalException {

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    bdmCncrCmmRmKey.wdoName = BDMConstants.kBdmTaskCreateDetails;

    final BDMRecordCommunicationsTaskDetailsList bdmRecordCommunicationsTaskDetailsList =
      bdmfeCase.getListOfTaskForCommunication(bdmCncrCmmRmKey);
    final int taskListSize =
      bdmRecordCommunicationsTaskDetailsList.dtls.size();

    Long existingTaskID = 0L;

    for (int i = 0; i < taskListSize; i++) {

      final BDMRecordCommunicationsTaskDetails bdmRecordCommunicationsTaskDetails =
        bdmRecordCommunicationsTaskDetailsList.dtls.item(i);

      if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.kLongZero
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot
          .indexOf(bdmCncrCmmRmKey.caseID + CuramConst.gkEmpty) != -1
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot.indexOf(
          bdmCncrCmmRmKey.communicationTypeCode + CuramConst.gkEmpty) != -1) {

        existingTaskID = bdmRecordCommunicationsTaskDetails.taskID;
        break;
      }
    }
    return existingTaskID;
  }

  /**
   * Method to get the existing privacy request tasks for integrated case
   *
   * @param bdmCncrCmmRmKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Long
    getExistingTaskIDForCase(final BDMConcernRoleCommRMKey bdmCncrCmmRmKey)
      throws AppException, InformationalException {

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    bdmCncrCmmRmKey.wdoName = BDMConstants.kBdmTaskCreateDetails;

    final BDMRecordCommunicationsTaskDetailsList bdmRecordCommunicationsTaskDetailsList =
      bdmfeCase.getFECaseListOfTaskForCommunication(bdmCncrCmmRmKey);
    final int taskListSize =
      bdmRecordCommunicationsTaskDetailsList.dtls.size();

    Long existingTaskID = 0L;

    for (int i = 0; i < taskListSize; i++) {

      final BDMRecordCommunicationsTaskDetails bdmRecordCommunicationsTaskDetails =
        bdmRecordCommunicationsTaskDetailsList.dtls.item(i);

      if (bdmRecordCommunicationsTaskDetails.taskID != CuramConst.kLongZero
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot
          .indexOf(bdmCncrCmmRmKey.caseID + CuramConst.gkEmpty) != -1
        && bdmRecordCommunicationsTaskDetails.wdoSnapshot.indexOf(
          bdmCncrCmmRmKey.communicationTypeCode + CuramConst.gkEmpty) != -1) {

        existingTaskID = bdmRecordCommunicationsTaskDetails.taskID;
        break;
      }
    }
    return existingTaskID;
  }

  public static boolean isReadReviewUser()
    throws AppException, InformationalException {

    boolean isReadReview = false;

    final String user = TransactionInfo.getProgramUser();

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = user;

    final UserRoleDetails userRoleDetails =
      UsersFactory.newInstance().readUserRole(usersKey);

    if (userRoleDetails.roleName.equalsIgnoreCase(BDMConstants.kIOReadReview)
      || userRoleDetails.roleName.contains(BDMConstants.kIOReadReview)) {
      isReadReview = true;
    }

    return isReadReview;
  }

  /**
   * Validation method
   * Task 21121 Phone number validations
   *
   * @since
   * @param
   */
  public void validatePhoneNumberDetails(final String phoneCountryCode,
    final String phoneAreaCode, final String phoneNumber,
    final String phoneExtension) throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final Boolean isPlusOneCountry = phoneCountryCode.isEmpty() ? false
      : phoneCountryCode.equals(BDMConstants.kphonePrefix);

    // validation: if country code is +1, then area code is mandatory
    if (isPlusOneCountry && phoneAreaCode.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE);

      informationalManager.addInformationalMsg(localisableString,
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    } else if (isPlusOneCountry && (!isNumeric(phoneAreaCode)
      || phoneAreaCode.length() != BDMConstants.gkPhoneAreaLength3)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE_3DIGIT);

      informationalManager.addInformationalMsg(localisableString,
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

    }

    // validation: if country code is +1 then phone Number must be 7 digits and
    // numeric
    if (isPlusOneCountry && phoneNumber.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_MISSING);

      informationalManager.addInformationalMsg(localisableString,
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

    } else if (isPlusOneCountry && (!isNumeric(phoneNumber)
      || phoneNumber.length() != BDMConstants.gkPhoneAreaLength7)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_7DIGIT);

      informationalManager.addInformationalMsg(localisableString,
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

    }
    // validation: if country code is not +1 then phone Number and Area code
    // must be numeric
    if (!isPlusOneCountry
      && (!isNumeric(phoneAreaCode) || !isNumeric(phoneNumber))) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_OTHER_COUNTRY);

      informationalManager.addInformationalMsg(localisableString,
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

    }

    // validation: Extension can only have numeric values
    if (!isNumeric(phoneExtension)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_EXTENSION_NUMERIC);

      informationalManager.addInformationalMsg(localisableString,
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

    }

    informationalManager.failOperation();

  }

  /**
   * Util Method to validate if given string is a number
   *
   * @since
   * @param phoneNumber
   * @return
   */
  private boolean isNumeric(final String phoneNumber) {

    if (phoneNumber.isEmpty()) {

      return true;

    }
    // regex to check for Numeric Values
    final Pattern pattern = Pattern.compile("^[0-9]*$");
    final Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  /**
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getPhoneCountryCode(final String phoneCountry)
    throws AppException, InformationalException {

    String phoneCountryCodeStr = "";
    if (!StringUtil.isNullOrEmpty(phoneCountry)) {
      final CodeTableAdmin codeTableAdminObj =
        CodeTableAdminFactory.newInstance();
      final CodeTableItemUniqueKey codeTableItemUniqueKey =
        new CodeTableItemUniqueKey();

      codeTableItemUniqueKey.code = phoneCountry;
      codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
      codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

      phoneCountryCodeStr = codeTableAdminObj
        .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey).annotation
          .trim();
    }
    return phoneCountryCodeStr;
  }

  /**
   * Added method to check address is empty or not
   *
   * @param additionalDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean isAddressEmpty(final OtherAddressData mailingAddressData,
    final OtherAddressData residentialAddressData,
    final boolean isMailingAddSame)
    throws AppException, InformationalException {

    boolean isAddressEmpty = true;

    final AddressData addressDataObj = AddressDataFactory.newInstance();

    final AddressMapList mailingAddressMapList =
      addressDataObj.parseDataToMap(mailingAddressData);

    final AddressMap mailingAddressMap = new AddressMap();

    mailingAddressMap.name = ADDRESSELEMENTTYPE.COUNTRY;
    final ElementDetails mailingCountryCode =
      addressDataObj.findElement(mailingAddressMapList, mailingAddressMap);

    String countryCd = mailingCountryCode.elementValue;

    // if residential address is provided and checked the same for mailing
    // address
    if (isMailingAddSame) {
      final AddressMapList residentialAddressMapList =
        addressDataObj.parseDataToMap(residentialAddressData);

      final AddressMap residentialAddressMap = new AddressMap();

      residentialAddressMap.name = ADDRESSELEMENTTYPE.COUNTRY;
      final ElementDetails residentialCountryCode = addressDataObj
        .findElement(residentialAddressMapList, residentialAddressMap);

      countryCd = residentialCountryCode.elementValue;
    }

    if (!StringUtil.isNullOrEmpty(countryCd)) {
      isAddressEmpty = false;
    }

    return isAddressEmpty;

  }

  /**
   * This is a utility method to insert record in the BDMTask table.
   *
   * Bug 98073: INT Defect - Task Category and Type is not displayed when a
   * system generated task is created (TASK-01 - Reference from Task Sheet)
   *
   * @param kProcessInstanceID
   * @param codeTableTypeCode
   * @param faTypeCode
   * @throws AppException
   * @throws InformationalException
   */
  public void addBDMTask(final long kProcessInstanceID,
    final String codeTableTypeCode, final String faTypeCode)
    throws AppException, InformationalException {

    if (kProcessInstanceID != 0) {

      final long taskID = getTaskIDForProcessInstanceID(kProcessInstanceID);

      if (taskID != 0L) {
        final BDMTask bdmTask = BDMTaskFactory.newInstance();
        final BDMTaskKey key = new BDMTaskKey();
        key.taskID = taskID;

        final BDMTaskDtls newDtls = new BDMTaskDtls();
        newDtls.taskID = taskID;

        if (codeTableTypeCode.equals(DOCUMENTTYPE.FOREIGN_APPLICATION)
          && faTypeCode.equals(BDMFOREIGNAPPTYPE.DISABILITY)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBAPPLICATIONDISABILITY;
        } else if (codeTableTypeCode
          .equals(DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS)
          && faTypeCode.equals(BDMFOREIGNAPPTYPE.DISABILITY)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBAPPLICATIONDISABILITY;
        } else if (codeTableTypeCode.equals(DOCUMENTTYPE.FOREIGN_APPLICATION)
          && !faTypeCode.equals(BDMFOREIGNAPPTYPE.DISABILITY)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBAPPLICATIONS;
        } else if (codeTableTypeCode
          .equals(DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS)
          && !faTypeCode.equals(BDMFOREIGNAPPTYPE.DISABILITY)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOAPPLICATIONFB;
          newDtls.type = BDMTASKTYPE.BDMFBAPPLICATIONS;
        }
        // START: Bug 99510 FIX, added category and type for the Task
        else if (codeTableTypeCode
          .equals(CASEEVIDENCE.BDM_FOREIGN_RESIDENCE_PERIOD)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFOREIGNRESIDENCEEVIDENCE;
        } else if (codeTableTypeCode
          .equals(CASEEVIDENCE.BDM_FOREIGN_CONTRIBUTION_PERIOD)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFOREIGNCONTRIBUTIONEEVIDENCE;
        }
        // END: Bug 99510 FIX, added category and type for the Task

        bdmTask.insert(newDtls);
      }
    }
  }

  /**
   * This is a utility method to retrieve the TaskID.
   *
   * Bug 98073: INT Defect - Task Category and Type is not displayed when a
   * system generated task is created (TASK-01 - Reference from Task Sheet)
   *
   * @param processInstanceID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @SuppressWarnings("restriction")
  protected long getTaskIDForProcessInstanceID(final long processInstanceID)
    throws AppException, InformationalException {

    final ActivityInstance activityInstanceObj =
      ActivityInstanceFactory.newInstance();

    final ProcessInstanceKey instanceKey = new ProcessInstanceKey();
    long taskID = 0;

    instanceKey.processInstanceID = processInstanceID;

    final ActivityInstanceDtlsList instanceDtlsList =
      activityInstanceObj.searchByProcessInstanceID(instanceKey);

    for (int i = 0; i < instanceDtlsList.dtls.size(); i++) {
      if (instanceDtlsList.dtls.item(i).taskID != 0) {
        taskID = instanceDtlsList.dtls.item(i).taskID;
        if (taskID != 0) {
          break;
        }
      }
    }

    return taskID;
  }

  /**
   * Get the current escalation level for a task.
   *
   * @param taskID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMEscalationLevelDtls getCurrentEscalationLevel(final long taskID)
    throws AppException, InformationalException {

    BDMEscalationLevelDtls bdmEscalationLevelDtls =
      new BDMEscalationLevelDtls();

    final BizObjAssociation bizObjAssociation =
      BizObjAssociationFactory.newInstance();

    BizObjAssociationDtls bizObjAssociationDtls = null;

    final TaskKey taskKey2 = new TaskKey();
    taskKey2.taskID = taskID;

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

        // Implement not found indicator
        final NotFoundIndicator nfi = new NotFoundIndicator();
        final BDMEscalationLevelDtlsStruct2 bdmEscalationLevelDtlsStruct2 =
          bdmEscalationLevel.readByCommunicationID(nfi,
            bdmEscalationLevelKeyStruct1);
        if (nfi.isNotFound()) {
          return bdmEscalationLevelDtls;
        }

        final BDMEscalationLevelKey bdmEscalationLevelKey =
          new BDMEscalationLevelKey();

        bdmEscalationLevelKey.bdmEscalationLevelID =
          bdmEscalationLevelDtlsStruct2.bdmEscalationLevelID;

        bdmEscalationLevelDtls =
          bdmEscalationLevel.read(bdmEscalationLevelKey);

      }
    }
    return bdmEscalationLevelDtls;

  }

  /**
   * This is a utility method, which will do an insert in to BDMTask table for a
   * particular type of communication.
   *
   * @param kProcessInstanceID
   * @param communicationTypeCode
   * @throws AppException
   * @throws InformationalException
   */
  public void addBDMTaskForRecCommCommType(final long kProcessInstanceID,
    final String communicationTypeCode)
    throws AppException, InformationalException {

    if (kProcessInstanceID != 0) {

      final long taskID = getTaskIDForProcessInstanceID(kProcessInstanceID);

      if (taskID != 0) {
        final BDMTask bdmTask = BDMTaskFactory.newInstance();
        final BDMTaskKey key = new BDMTaskKey();
        key.taskID = taskID;
        final BDMTaskDtls newDtls = new BDMTaskDtls();
        newDtls.taskID = taskID;

        if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.ENQUIRY_FOREIGN_BENEFITS)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFBINQUIRYFOREIGNBENEFITS;
        } else if (communicationTypeCode.equals(
          COMMUNICATIONTYPE.ENQUIRY_FOREIGN_BENEFITS_REQUIRES_ATTENTION)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFBINQUIRYFOREIGNBENEFITS;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.ENQUIRY_CANADIAN_BENEFITS)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFBINQUIRYCANADIANBENEFITS;
        } else if (communicationTypeCode.equals(
          COMMUNICATIONTYPE.ENQUIRY_CANADIAN_BENEFITS_REQUIRES_ATTENTION)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFBINQUIRYCANADIANBENEFITS;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.ENQUIRY_CHANGE_ADDRESS)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMENQUIRYADDRESSCHANGE;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.ENQUIRY_OTHER)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFBENQUIRYOTHER;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.ENQUIRY_PROCESSING_STATUS)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFBENQUIRYPROCESSINGSTATUS;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.ENQUIRY_REU)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMFBENQUIRYREU;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.MP_ENQUIRY)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMMPINQUIRY;
        } else if (communicationTypeCode
          .equals(COMMUNICATIONTYPE.MINISTERIAL_ENQUIRY)) {
          newDtls.category = BDMTASKCATEGORY.BDMIOMAINTENANCEFB;
          newDtls.type = BDMTASKTYPE.BDMMINISTRIALINQUIRY;
        }

        bdmTask.insert(newDtls);
      }
    }
  }

  /**
   * Method to check if the TASK-02 is required
   *
   * @param documentType
   * @return
   */
  public Boolean isAttachmentTaskIIRequired(final String documentType) {

    if (documentType.equals(DOCUMENTTYPE.BESS)
      || documentType.equals(DOCUMENTTYPE.BESS_ADDITIONAL_DOCUMENTS)
      || documentType.equals(DOCUMENTTYPE.FB_INCOMING_LIAISON_MEDICAL)
      || documentType.equals(DOCUMENTTYPE.FB_INCOMING_LIAISON_NONMEDICAL)
      || documentType.equals(DOCUMENTTYPE.INTERIM_APPLICATION)) {

      return true;
    }
    return false;
  }

  /**
   * Method to check if the TASK-06 is required
   *
   * @param documentType
   * @return
   */
  public Boolean isAttachmentTaskVIRequired(final String documentType) {

    if (documentType.equals(DOCUMENTTYPE.ADDRESS)
      // START, BUG 98671, Fox for Close Task 06
      || documentType.equals(DOCUMENTTYPE.FOR_TASKTYPE_DIRECT_DEPOSIT)
      // END, BUG 98671, Fox for Close Task 06
      || documentType.equals(DOCUMENTTYPE.CONSENT_TO_COMMUNICATE_1603)
      || documentType.equals(DOCUMENTTYPE.AUTHORIZATION_TO_COMMUNICATE_3015)
      || documentType.equals(DOCUMENTTYPE.CORRESPONDENCE)
      || documentType.equals(DOCUMENTTYPE.POA)
      || documentType.equals(DOCUMENTTYPE.TRUSTEE)) {

      return true;
    }
    return false;
  }

  /**
   * This method checks if a given template belongs to interim
   * application processing flow.
   *
   * @param templateCode
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean isInterimApplicationTemplate(final String templateCode)
    throws AppException, InformationalException {

    if (getInterimApplicationTemplatesMap()
      .containsKey(templateCode.toUpperCase())) {
      return true;
    }
    return false;
  }

  /**
   * This method gets the list of templates that are part of interim
   * application processing flow.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static HashMap<String, Integer> getInterimApplicationTemplatesMap()
    throws AppException, InformationalException {

    if (null == interimApplicationTemplateMap) {
      interimApplicationTemplateMap = new HashMap<String, Integer>();
      final String codeString = Configuration
        .getProperty(EnvVars.BDM_INTERIM_APPLICATION_TEMPLATE_LIST);
      final String[] codes = codeString.split(CuramConst.gkComma);
      for (final String code : codes) {
        if (!interimApplicationTemplateMap.containsKey(code.toUpperCase())) {
          interimApplicationTemplateMap.put(code.toUpperCase(), 1);
        }
      }
    }
    return interimApplicationTemplateMap;
  }

  /**
   * This is a utility method to figure out that, editing deadline for this task
   * is allowed or not.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean isAllowedToEditTaskDeadline(final long taskID)
    throws AppException, InformationalException {

    boolean returnIndicator = true;

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final BDMTaskKey bdmTaskKey = new BDMTaskKey();
    bdmTaskKey.taskID = taskID;
    BDMTaskDtls bdmTaskDtls = new BDMTaskDtls();

    final BDMTask bdmTask = BDMTaskFactory.newInstance();
    bdmTaskDtls = bdmTask.read(nfIndicator, bdmTaskKey);

    if (!nfIndicator.isNotFound()) {

      if (bdmTaskDtls.type.equals(BDMTASKTYPE.FB_INTERIM_APP)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMMINISTRIALINQUIRY)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMMPINQUIRY)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBADDRESS)
        || bdmTaskDtls.type
          .equals(BDMTASKTYPE.ADDRESSCHANGE_DIRECTDEPOSIT_PWS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBCONSENTTOCUMMUNICATE1603)
        || bdmTaskDtls.type
          .equals(BDMTASKTYPE.BDMFBAUTHORIZATIONTOCOMMUNICATE3015)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBCORRESPONDENCE)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBPOA)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBTRUSTEE)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBINQUIRYFOREIGNBENEFITS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBINQUIRYCANADIANBENEFITS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMENQUIRYADDRESSCHANGE)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBENQUIRYOTHER)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBENQUIRYPROCESSINGSTATUS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBENQUIRYREU)
        || bdmTaskDtls.type
          .equals(BDMTASKTYPE.BDMFOREIGNCONTRIBUTIONEEVIDENCE)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFOREIGNRESIDENCEEVIDENCE)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBBESS)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBLIASONMEDICAL)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBLIASONNONMEDICAL)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBAPPLICATIONDISABILITY)
        || bdmTaskDtls.type.equals(BDMTASKTYPE.BDMFBAPPLICATIONS)) {

        returnIndicator = false;

      }

    }

    return returnIndicator;
  }

  /**
   * Bug 99516: User should be prevented from deleting an Office member that was
   * specified as Third Party Contact
   *
   * @param officeMemFullName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean
    isOfficeMemberThirdPartyContact(final String officeMemFullName)
      throws AppException, InformationalException {

    boolean isOfficeMem3rdPartyContactInd = false;

    final BDMExternalParty bdmExternalParty =
      BDMExternalPartyFactory.newInstance();

    final ConcernRoleDtls concernRoleDtls = new ConcernRoleDtls();

    concernRoleDtls.concernRoleName = officeMemFullName;

    BDMDynamicEvidenceDataAttributeDetails dynamicEvidenceDataAttributeDetails =
      new BDMDynamicEvidenceDataAttributeDetails();

    BDMDynamicEvidenceDataAttributeDetailsList dynamicEvidenceDataAttributeDetailsList =
      new BDMDynamicEvidenceDataAttributeDetailsList();

    try {

      dynamicEvidenceDataAttributeDetailsList =
        bdmExternalParty.getDynamicAttributeDetaillsByValue(concernRoleDtls);

      int listSize = CuramConst.gkZero;

      listSize = dynamicEvidenceDataAttributeDetailsList.dtls.size();

      for (int i = 0; i < listSize; i++) {

        dynamicEvidenceDataAttributeDetails =
          new BDMDynamicEvidenceDataAttributeDetails();

        dynamicEvidenceDataAttributeDetails =
          dynamicEvidenceDataAttributeDetailsList.dtls.item(i);

        if (!StringUtil
          .isNullOrEmpty(dynamicEvidenceDataAttributeDetails.attributeName)
          && BDMConstants.kIndividualWithinOrg
            .equals(dynamicEvidenceDataAttributeDetails.attributeName)) {

          isOfficeMem3rdPartyContactInd = true;

          break;

        }

      }

    } catch (final RecordNotFoundException rnfe) {
      isOfficeMem3rdPartyContactInd = false;
    }

    return isOfficeMem3rdPartyContactInd;
  }

  /**
   * Base on the supplied primary and correspondent concern role ID, retrieve
   * all
   * active case participant role records with a CPR type of Correspondent on
   * the primary's integrated cases.
   *
   * @param primaryCrID
   * @param corCrID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public List<CaseParticipantRoleDtls>
    getAllActiveCorrespondentCprsOnICCasesByCrID(final long primaryCrID,
      final long corCrID) throws AppException, InformationalException {

    final List<CaseParticipantRoleDtls> list = new ArrayList<>();

    final curam.core.sl.entity.intf.CaseParticipantRole cprEntity =
      curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance();

    final ActiveCasesConcernRoleIDAndTypeKey caseSearchKey =
      new ActiveCasesConcernRoleIDAndTypeKey();

    caseSearchKey.concernRoleID = primaryCrID;
    caseSearchKey.caseTypeCode = CASETYPECODE.INTEGRATEDCASE;
    caseSearchKey.statusCode = RECORDSTATUS.NORMAL;

    final CaseHeaderDtlsList caseList = CaseHeaderFactory.newInstance()
      .searchActiveCasesByTypeConcernRoleID(caseSearchKey);

    final Set<Long> caseIDSet = new HashSet<>();
    for (final CaseHeaderDtls chDtls : caseList.dtls) {
      caseIDSet.add(chDtls.caseID);
    }

    final ParticipantRoleIDAndTypeCodeKey crIdTypeKey =
      new ParticipantRoleIDAndTypeCodeKey();
    crIdTypeKey.participantRoleID = corCrID;
    crIdTypeKey.typeCode = CASEPARTICIPANTROLETYPE.CORRESPONDENT;

    final CaseParticipantRoleDtlsList corCprList =
      cprEntity.searchByParticipantRoleIDTypeCode(crIdTypeKey);
    for (final CaseParticipantRoleDtls corCprDtls : corCprList.dtls) {
      // end date if this is a IC cpr and it is active
      if (caseIDSet.contains(corCprDtls.caseID)
        && !RECORDSTATUS.CANCELLED.equals(corCprDtls.recordStatus)) {
        list.add(corCprDtls);
      }
    }

    return list;
  }

  /**
   * Helper method to get case participant role dtls by the case participant
   * role ID.
   *
   * @param caseParticipantRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CaseParticipantRoleDtls
    getCaseParticipantRoleDtls(final long caseParticipantRoleID)
      throws AppException, InformationalException {

    final curam.core.sl.entity.intf.CaseParticipantRole cprEntity =
      curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance();

    final CaseParticipantRoleKey cprKey = new CaseParticipantRoleKey();

    cprKey.caseParticipantRoleID = caseParticipantRoleID;

    return cprEntity.read(new NotFoundIndicator(), cprKey);
  }

  /**
   * Method to get the Name of the person for Task Subject
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getConcernRoleName(final long concernRoleID)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;

    final String strConcernRoleFullName =
      ConcernRoleFactory.newInstance().read(concernRoleKey).concernRoleName;
    return strConcernRoleFullName;
  }

  /**
   * Method to get the Person SIN/Identification Details
   *
   * @param bdmCncrCmmRmKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getPersonIdentificationDetail(final Long concernRoleID)
    throws AppException, InformationalException {

    String strIdentificationRefernce = CuramConst.gkEmpty;

    final String personSINNumber = getSINNumberForPerson(concernRoleID);

    if (!StringUtil.isNullOrEmpty(personSINNumber)) {
      strIdentificationRefernce = personSINNumber;
    } else {
      strIdentificationRefernce = getPersonReferenceNumber(concernRoleID);

    }
    return strIdentificationRefernce;
  }

  /**
   * This is a Utility method, to close existing open task generated for the
   * communication of type "Interim Application - Request" - when a foreign
   * application of type Interim, for country "United States", and concent
   * selected as "No"
   *
   * Bug 100716: TASK-13: Not closing after US Interim Application with client
   * consent 'No' is created
   *
   * @author hamed.mohammed
   * @param caseID
   * @throws AppException
   * @throws InformationalException
   */
  public void findandCloseTasksForCommunicationOfTypeInterimRequest(
    final long caseID) throws AppException, InformationalException {

    final BizObjAssociation bizObjAssociation =
      BizObjAssociationFactory.newInstance();

    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectID = caseID;
    bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    final BizObjAssocSearchDetailsList bizObjAssocSearchDetailsList =
      bizObjAssociation.searchByBizObjectTypeAndID(bizObjectTypeKey);

    final TaskKey taskKey = new TaskKey();

    final curam.core.struct.ConcernRoleCommunicationKey communicationKey =
      new curam.core.struct.ConcernRoleCommunicationKey();

    for (final BizObjAssocSearchDetails assocSearchDetails : bizObjAssocSearchDetailsList.dtls) {

      if (assocSearchDetails.taskID != CuramConst.gkZero) {

        taskKey.taskID = assocSearchDetails.taskID;

        final BizObjAssociationDtlsList bizObjAssociationDtlsList =
          bizObjAssociation.searchByTaskID(taskKey);

        for (final BizObjAssociationDtls bizObjAssociationDtls : bizObjAssociationDtlsList.dtls) {

          if (bizObjAssociationDtls.bizObjectType
            .equals(BUSINESSOBJECTTYPE.BDMCOMMUNICATION)) {

            communicationKey.communicationID =
              bizObjAssociationDtls.bizObjectID;

            final ConcernRoleCommunicationDtls communicationDtls =
              ConcernRoleCommunicationFactory.newInstance()
                .read(communicationKey);

            if (communicationDtls.typeCode
              .equals(COMMUNICATIONTYPE.INTERIM_APP_REQUEST)) {

              final Event closeTaskEvent = new Event();
              closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
              closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
              closeTaskEvent.primaryEventData = bizObjAssociationDtls.taskID;
              EventService.raiseEvent(closeTaskEvent);

            }

          }
        }

      }
    }

  }

  /**
   * Bug 107945: E2E Defect - Deleting Foreign Application that is linked to a
   * foreign Liaison and business rule not working
   *
   * @param foreignApplicationID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMForeignLiaisonKey
    findActiveForeignLiaison(final long foreignApplicationID)
      throws AppException, InformationalException {

    final BDMForeignLiaisonKey bdmForeignLiaisonKey =
      new BDMForeignLiaisonKey();

    final StringBuffer dynamicSQL = new StringBuffer();

    dynamicSQL.append(findActiveForeignLiaison + BDMConstants.kSingleQuote
      + CuramConst.gkPercentage);
    dynamicSQL.append(foreignApplicationID + CuramConst.gkPercentage
      + BDMConstants.kSingleQuote + CuramConst.gkSemiColon);

    final CuramValueList<BDMForeignLiaisonDtls> bdmForeignLiaisonDtlsList =
      DynamicDataAccess.executeNsMulti(BDMForeignLiaisonDtls.class, null,
        false, dynamicSQL.toString());

    for (int i = 0; i < bdmForeignLiaisonDtlsList.size(); i++) {

      if (bdmForeignLiaisonDtlsList
        .item(i).foreignLiaisonID != CuramConst.kLongZero) {

        bdmForeignLiaisonKey.foreignLiaisonID =
          bdmForeignLiaisonDtlsList.item(i).foreignLiaisonID;

        return bdmForeignLiaisonKey;

      }

    }

    return bdmForeignLiaisonKey;

  }

  /**
   *
   * Utility method to compare person's residential and mailing address
   *
   * @param evidenceDescriptorID
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  public boolean
    isResidentialAndMailingAddressMatching(final long evidenceDescriptorID)
      throws AppException, InformationalException {

    boolean returnBoolean = false;

    final StringBuffer dynamicSQL = new StringBuffer();

    dynamicSQL.append(foundOnlyResidentialAddressEnded);
    dynamicSQL.append(" " + evidenceDescriptorID + ";");

    final CuramValueList<ConcernRoleAddressDtls> concernroleAddressDtlsLst =
      DynamicDataAccess.executeNsMulti(ConcernRoleAddressDtls.class, null,
        false, dynamicSQL.toString());

    String residentialAddress = "";
    String mailingAddress = "";

    final AddressKey resAddressKey = new AddressKey();
    final AddressKey mailingAddressKey = new AddressKey();

    for (int i = 0; i < concernroleAddressDtlsLst.size(); i++) {

      if (concernroleAddressDtlsLst.item(i).endDate.isZero()
        && concernroleAddressDtlsLst.item(i).typeCode
          .equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {

        resAddressKey.addressID = concernroleAddressDtlsLst.item(i).addressID;
        residentialAddress = this.getFormattedAddress(resAddressKey);
      }

      if (concernroleAddressDtlsLst.item(i).endDate.isZero()
        && concernroleAddressDtlsLst.item(i).typeCode
          .equals(CONCERNROLEADDRESSTYPE.MAILING)) {

        mailingAddressKey.addressID =
          concernroleAddressDtlsLst.item(i).addressID;
        mailingAddress = this.getFormattedAddress(mailingAddressKey);
      }
    }

    if (residentialAddress.equalsIgnoreCase(mailingAddress)) {
      returnBoolean = true;
    } else {
      returnBoolean = false;
    }

    return returnBoolean;
  }

  public static boolean isCountryRestricted(final String countryCode) {

    final String[] restrictedCountries =
      Configuration.getProperty(EnvVars.BDM_ENV_RESTRICTED_COUNTRY_LIST)
        .split(CuramConst.gkComma);

    boolean isCountryRestricted = false;
    /*
     * for (final String country : restrictedCountries) {
     * if (country.trim().equals(countryCode)) {
     * isCountryRestricted = true;
     * break;
     * }
     * }
     */
    if (Arrays.asList(restrictedCountries).contains(countryCode)) {
      isCountryRestricted = true;
    }

    return isCountryRestricted;

  }

  /**
   * This Utlity method will get the current logged in user's default locale.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getLoggedInUserLocale()
    throws AppException, InformationalException {

    final String currentLoggedInUser = TransactionInfo.getProgramUser();

    final Users usersObj = UsersFactory.newInstance();
    final UsersKey usersKey = new UsersKey();
    usersKey.userName = currentLoggedInUser;
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final UsersDtls usersDtls = usersObj.read(notFoundIndicator, usersKey);

    return usersDtls.defaultLocale;
  }

}
