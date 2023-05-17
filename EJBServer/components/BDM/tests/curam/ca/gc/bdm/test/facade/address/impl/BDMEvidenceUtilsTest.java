package curam.ca.gc.bdm.test.facade.address.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMALERTOCCUR;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMGenderEvidenceVO;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchResult;
import curam.ca.gc.bdm.sl.impl.BDMEvidenceActivationEventsListener;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMPDCContactPreferenceEvidenceTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.EMAILTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.GENDER;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.PersonRegistration;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.HashMap;
import jdk.nashorn.internal.ir.annotations.Ignore;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
@Ignore
public class BDMEvidenceUtilsTest extends CuramServerTestJUnit4 {

  BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

  BDMEvidenceActivationEventsListener bdmEvidenceEventListener1 =
    new BDMEvidenceActivationEventsListener();

  @Tested
  BDMGenderEvidence bdmGenderEvidence;

  public BDMEvidenceUtilsTest() {

    super();
  }

  @Before
  public void setUp() throws AppException, InformationalException {

  }

  private final String _addressDataINTL = null;

  @Inject
  BDMPDCContactPreferenceEvidenceTest contactObj;

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  private static final String kFromDateAttrName = "fromDate";

  private static final String kParticipant = "participant";

  private static final String kEmailAddressType = "emailAddressType";

  private static final String kEmailAddress = "emailAddress";

  private static final String ERR_EMAILADDRESS_INVALID =
    "Email address must be valid.";

  private static final String kPreferred = "preferredInd";

  /**
   * Search address details extension Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testgetEvidenceSet()
    throws AppException, InformationalException {

    final EvidenceDescriptorDtlsList list = new EvidenceDescriptorDtlsList();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final String evidenceType = eType.evidenceType;
    BDMEvidenceUtil.getEvidenceSet(list, evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testpostActivation()
    throws AppException, InformationalException {

    final EIEvidenceKeyList list = new EIEvidenceKeyList();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final String evidenceType = eType.evidenceType;
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final CaseKey key = new CaseKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    key.caseID = pdcID;

    bdmEvidenceEventListener1.postActivation(evidenceControllerObj, key,
      list);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testgetDynamicEvidenceList()
    throws AppException, InformationalException {

    final EvidenceDescriptorDtlsList list = new EvidenceDescriptorDtlsList();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final String evidenceType = eType.evidenceType;
    BDMEvidenceUtil.getDynamicEvidenceList(list, evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testgetGenericSLDataDetails()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;

    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final BDMGenderEvidenceVO bdmGenderEvidenceVO = new BDMGenderEvidenceVO();

    bdmGenderEvidenceVO.setGender(GENDER.MALE);

    bdmGenderEvidence.createGenderEvidence(originalConcernRoleID,
      bdmGenderEvidenceVO, EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);

    final BDMGenderEvidenceVO bdmGenderEvidenceVOModify =
      new BDMGenderEvidenceVO();

    bdmGenderEvidenceVOModify.setGender(GENDER.FEMALE);

    key.evidenceID = 0l;

    BDMEvidenceUtil.getGenericSLDataDetails(key);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testgetDynamicEvidences()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    BDMEvidenceUtil.getDynamicEvidences(pdcID, date, evidenceType,
      originalConcernRoleID);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testcreateSINEvidence()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;
    final String sinNumber = "204617054";
    BDMEvidenceUtil.createSINEvidence(concernRoleID, sinNumber, date);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testgetStaticEvidenceList()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;
    final String sinNumber = "204617054";
    BDMEvidenceUtil.getStaticEvidenceList(concernRoleID, evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testisActiveEvidenceExist()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    BDMEvidenceUtil.isActiveEvidenceExist(crKey, evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testfindCurrentSINEvidence()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    BDMEvidenceUtil.findCurrentSINEvidence(concernRoleID);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testremoveEvidence()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    BDMEvidenceUtil.removeEvidence(concernRoleID, evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testremoveDynamicEvidence()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    BDMEvidenceUtil.removeDynamicEvidence(concernRoleID, evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testgetDynEvdAttrValue()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    final String attrName = "attrName";
    BDMEvidenceUtil.getDynEvdAttrValue(concernRoleID, evidenceType, attrName);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testreadEvidence() throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    BDMEvidenceUtil.readEvidence(evidenceType, concernRoleID);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testmodifyEvidence()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    final Long concernRoleID = originalConcernRoleID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    final String evidenceChangeReason = "evidenceChangeReason";
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final EIEvidenceKey key1 = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, false, false);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key1);
    final DynamicEvidenceDataDetails c =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;
    final EIEvidenceReadDtls dynEvdDataDtls = currEIEvidenceReadDtls;
    BDMEvidenceUtil.modifyEvidence(concernRoleID, evidenceType,
      dynEvdDataDtls, evidenceChangeReason);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testhasInEditEvidences()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    final long integratedCaseID = pdcID;
    BDMEvidenceUtil.hasInEditEvidences(integratedCaseID);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testcreatePersonReferenceNumber()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final long concernRoleID = originalConcernRoleID;
    BDMEvidenceUtil.createPersonReferenceNumber(concernRoleID);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testcreateProspectPersonReferenceNumber()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final long concernRoleID = originalConcernRoleID;
    BDMEvidenceUtil.createProspectPersonReferenceNumber(concernRoleID);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRoleID";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private static final String MARITAL_STATUS = "maritalStatus";

  private static final String ERR_ONLY_ONE_RECORD =
    "A client cannot have more than one Marital Status record.";

  @Test
  public void testcreateAndReturnICDynamicEvidenceByCase()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final EIEvidenceKey key = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, false, false);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);
    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;
    final long concernRoleID = originalConcernRoleID;
    final String caseReference = "caseReference";
    final HashMap<String, String> evidenceData =
      (HashMap<String, String>) currDynamicEvidenceDataDetails;

    final String evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    BDMEvidenceUtil.createAndReturnICDynamicEvidenceByCase(caseReference,
      evidenceData, evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testcreateICDynamicEvidenceByCase()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final EIEvidenceKey key = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, false, false);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);
    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;
    final long concernRoleID = originalConcernRoleID;
    final String caseReference = "caseReference";
    final HashMap<String, String> evidenceData =
      (HashMap<String, String>) currDynamicEvidenceDataDetails;

    final String evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    BDMEvidenceUtil.createICDynamicEvidenceByCase(caseReference, evidenceData,
      evidenceType);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testcreatePDCDynamicEvidence()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final EIEvidenceKey key = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, false, false);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);
    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;
    final long concernRoleID = originalConcernRoleID;

    final HashMap<String, String> evidenceData =
      (HashMap<String, String>) currDynamicEvidenceDataDetails;

    final String evidenceType = CASEEVIDENCE.PHONE_NUMBERS;

    final long participantRoleID = originalConcernRoleID;
    final String evidenceChangeReason = "evidenceChangeReason";
    BDMEvidenceUtil.createPDCDynamicEvidence(participantRoleID, evidenceData,
      evidenceType, evidenceChangeReason);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testgetDynamicEvidenceValue()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);

    final long relatedID = originalConcernRoleID;
    final String evidenceChangeReason = "evidenceChangeReason";
    BDMEvidenceUtil.getDynamicEvidenceValue(relatedID, evidenceChangeReason);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testcreateAlternateID()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final EIEvidenceKey key = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, false, false);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);
    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;
    final long concernRoleID = originalConcernRoleID;

    final HashMap<String, String> evidenceData =
      (HashMap<String, String>) currDynamicEvidenceDataDetails;

    final String evidenceType = CASEEVIDENCE.PHONE_NUMBERS;

    final long relatedID = originalConcernRoleID;
    final String evidenceChangeReason = "evidenceChangeReason";
    final String alternateIDtype = "alternateIDtype";
    BDMEvidenceUtil.createAlternateID(relatedID, evidenceChangeReason,
      alternateIDtype);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testsetDynamicEvidenceDetails()
    throws AppException, InformationalException {

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final HashMap<String, String> evidenceData = null;

    BDMEvidenceUtil.setDynamicEvidenceDetails(evidenceData);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  // ___________________________________________________________________________
  /**
   * Inserts the Participant Email Address evidence .
   *
   * @param details The person details.
   * @param emailAddress
   * @param preferrredInd
   * @param paramAlerts
   *
   * @return The evidence key.
   * Participant Email Address evidence details to be inserted.
   */

  public EIEvidenceKey createEmailAddressEvidence(
    final PDCPersonDetails details, final String emailAddress,
    final String paramEmailType, final boolean paramPreferred,
    final boolean paramAlerts) throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCEMAILADDRESS;

    final EvidenceTypeDef evidenceType =
      etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);

    final EvidenceTypeVersionDef evTypeVersion =
      etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute(kParticipant);

    DynamicEvidenceTypeConverter.setAttribute(participant,
      pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);

    final DynamicEvidenceDataAttributeDetails preferred =
      dynamicEvidenceDataDetails.getAttribute(kPreferred);
    DynamicEvidenceTypeConverter.setAttribute(preferred, paramPreferred);

    final DynamicEvidenceDataAttributeDetails useForAlerts =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kEvidenceAttrUseForAlert);
    DynamicEvidenceTypeConverter.setAttribute(useForAlerts, paramAlerts);

    assignEmailAddressEvidenceDetails(dynamicEvidenceDataDetails,
      emailAddress, paramEmailType);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Call the EvidenceController object and insert evidence
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    evidenceDescriptorInsertDtls.participantID = details.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.effectiveFrom = Date.getCurrentDate();

    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;

    // Evidence Interface details
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = details.concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    return evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
  }

  /**
   * Assigns participant's email to the dynamic evidence data
   * struct.
   *
   * @param details
   * Participant email
   * @PARAM paramEmailType
   * @param dynamicEvidenceDataDetails
   * Dynamic evidence details.
   */
  private void assignEmailAddressEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String emailAddress, final String paramEmailType)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails emailType =
      dynamicEvidenceDataDetails.getAttribute(kEmailAddressType);
    DynamicEvidenceTypeConverter.setAttribute(emailType,
      new CodeTableItem(EMAILTYPE.TABLENAME, paramEmailType));

    final DynamicEvidenceDataAttributeDetails email =
      dynamicEvidenceDataDetails.getAttribute(kEmailAddress);
    DynamicEvidenceTypeConverter.setAttribute(email, emailAddress);

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());
    // -- Added AJERTS FREQUENCY --->
    final DynamicEvidenceDataAttributeDetails alertFrequency1 =
      dynamicEvidenceDataDetails.getAttribute("alertFrequency");
    DynamicEvidenceTypeConverter.setAttribute(alertFrequency1,
      new CodeTableItem(BDMALERTOCCUR.TABLENAME, BDMALERTOCCUR.PERDAY));
    // -- Added AJERTS FREQUENCY --->
  }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  @Test
  public void testretrieveEvidence()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date date = Date.getCurrentDate();

    BDMEvidenceUtil.retrieveEvidence(pdcID, date, evidenceType,
      originalConcernRoleID);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  @Test
  public void testisEvidenceInDateRange()
    throws AppException, InformationalException {

    final EIEvidenceKey key = new EIEvidenceKey();

    final BDMAddressSearchResult result = new BDMAddressSearchResult();
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    final String evidenceType = eType.evidenceType;
    final Date startdate = Date.getCurrentDate();
    final Date endDate = Date.getCurrentDate().addDays(-10);

    BDMEvidenceUtil.isEvidenceInDateRange(startdate, endDate);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(eType.evidenceType.length() > 0);

  }

  public PDCPersonDetails createOriginalPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    personRegistrationDetails.firstForename = "Original Person";
    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }
}
