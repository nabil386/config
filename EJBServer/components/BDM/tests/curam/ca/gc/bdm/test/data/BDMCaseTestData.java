package curam.ca.gc.bdm.test.data;

import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CURRENCY;
import curam.codetable.NOMINEERELATIONSHIP;
import curam.codetable.RECORDSTATUS;
import curam.commonintake.codetable.METHODOFAPPLICATION;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.intf.ApplicationCase;
import curam.commonintake.facade.struct.ApplicationCaseCreateDetails;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.facade.intf.UniqueID;
import curam.core.facade.struct.UniqueIDKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.fact.ProductFactory;
import curam.core.sl.entity.fact.CaseNomineeFactory;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseNomineeCreationDetails;
import curam.core.sl.entity.struct.CaseNomineeDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDtls;
import curam.core.struct.ProductNameStructRef;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

public class BDMCaseTestData {

  private final UniqueID uniqueIDObj;

  private static final long kProductDeliveryPatternID = 80005;

  public BDMCaseTestData() {

    uniqueIDObj = UniqueIDFactory.newInstance();
  }

  /**
   * Creates a case
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createCase(final long concernRoleID)
    throws AppException, InformationalException {

    // create the case
    final CaseHeaderDtls chDtls = new CaseHeaderDtls();
    chDtls.appealIndicator = false;
    final UniqueIDKey uniqueIDKey = new UniqueIDKey();
    uniqueIDKey.keySetName = "CASE";
    chDtls.caseReference =
      Long.toString(uniqueIDObj.getNextIDFromKeySet(uniqueIDKey).uniqueID);
    chDtls.caseID = uniqueIDObj.getNextID().uniqueID;
    chDtls.versionNo = 1;
    chDtls.concernRoleID = concernRoleID;
    chDtls.startDate = Date.getCurrentDate();
    chDtls.statusCode = CASESTATUS.ACTIVE;

    CaseHeaderFactory.newInstance().insert(chDtls);

    return chDtls.caseID;
  }

  /**
   * Creates a IntegratedCase
   *
   * @param productName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createIntegratedCase(final long concernRoleID)
    throws AppException, InformationalException {

    // create the case
    final CaseHeaderDtls chDtls = new CaseHeaderDtls();
    chDtls.appealIndicator = false;
    final UniqueIDKey uniqueIDKey = new UniqueIDKey();
    uniqueIDKey.keySetName = "CASE";
    chDtls.caseReference =
      Long.toString(uniqueIDObj.getNextIDFromKeySet(uniqueIDKey).uniqueID);
    chDtls.caseID = uniqueIDObj.getNextID().uniqueID;
    chDtls.versionNo = 1;
    chDtls.concernRoleID = concernRoleID;
    chDtls.startDate = Date.getCurrentDate();
    chDtls.statusCode = CASESTATUS.ACTIVE;
    chDtls.caseTypeCode = CASETYPECODE.INTEGRATEDCASE;

    CaseHeaderFactory.newInstance().insert(chDtls);

    return chDtls.caseID;

  }

  /**
   * Creates a case participant role
   */
  public long createCPR(final long caseID, final long concernRoleID)
    throws AppException, InformationalException {

    // create the CPR
    final CaseParticipantRoleDtls caseParticipantRoleDtls =
      new CaseParticipantRoleDtls();
    caseParticipantRoleDtls.caseID = caseID;
    caseParticipantRoleDtls.caseParticipantRoleID =
      uniqueIDObj.getNextID().uniqueID;
    caseParticipantRoleDtls.participantRoleID = concernRoleID;
    caseParticipantRoleDtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDtls.versionNo = 1;
    caseParticipantRoleDtls.recordStatus = RECORDSTATUS.DEFAULTCODE;
    caseParticipantRoleDtls.typeCode = CASEPARTICIPANTROLETYPE.NOMINEE;
    CaseParticipantRoleFactory.newInstance().insert(caseParticipantRoleDtls);

    return caseParticipantRoleDtls.caseParticipantRoleID;
  }

  /**
   * Creates a case nominee
   *
   * @param caseID
   * @param cprID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createCaseNomineeDetails(final long caseID, final long cprID,
    final long concernRoleID) throws AppException, InformationalException {

    // create the case nominee
    final CaseNomineeDtls caseNomineeDtls = new CaseNomineeDtls();
    final CaseNomineeCreationDetails caseNomineeCreationDetails =
      new CaseNomineeCreationDetails();
    caseNomineeDtls.caseNomineeID = uniqueIDObj.getNextID().uniqueID;
    caseNomineeDtls.caseParticipantRoleID = cprID;
    caseNomineeDtls.defaultNomInd = false;
    caseNomineeDtls.versionNo = 1;
    caseNomineeDtls.defaultNomInd = true;

    caseNomineeCreationDetails.caseID = caseID;
    caseNomineeCreationDetails.concernRoleID = concernRoleID;
    caseNomineeCreationDetails.fromDate = Date.getCurrentDate();
    caseNomineeDtls.currencyType = CURRENCY.DEFAULTCODE;
    caseNomineeDtls.nomineeRelationship = NOMINEERELATIONSHIP.DEFAULTCODE;
    caseNomineeCreationDetails.productDeliveryPatternID =
      kProductDeliveryPatternID;

    CaseNomineeFactory.newInstance().insert(caseNomineeDtls,
      caseNomineeCreationDetails);

    return caseNomineeDtls.caseNomineeID;
  }

  /**
   * Creates a product delivery case
   *
   * @param productName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createProductDeliveryCase(final String productName,
    final long concernRoleID) throws AppException, InformationalException {

    final long pdcCaseID = createCase(concernRoleID);

    this.createProductDelivery(concernRoleID, pdcCaseID, productName);

    return pdcCaseID;
  }

  /**
   * Creates a product delivery case
   *
   * @param productName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public void createProductDelivery(final long concernRoleID,
    final long pdccaseID, final String productName)
    throws AppException, InformationalException {

    final ProductNameStructRef productNameKey = new ProductNameStructRef();
    productNameKey.name = productName;
    productNameKey.statusCode = RECORDSTATUS.NORMAL;
    final ProductDtls productDtls =
      ProductFactory.newInstance().readProductsByName(productNameKey);

    final ProductDeliveryDtls productDeliveryDtls = new ProductDeliveryDtls();
    productDeliveryDtls.caseID = pdccaseID;
    productDeliveryDtls.productID = productDtls.productID;
    productDeliveryDtls.productType = productDtls.typeCode;
    productDeliveryDtls.recipConcernRoleID = concernRoleID;

    ProductDeliveryFactory.newInstance().insert(productDeliveryDtls);
  }

  /**
   * Creates a product delivery case
   *
   * @param productName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMCaseTestDataDetails createProductDeliveryCaseAndRelatedDetails(
    final String productName, final long concernRoleID)
    throws AppException, InformationalException {

    final BDMCaseTestDataDetails bdmCaseTestDataDetails =
      new BDMCaseTestDataDetails();
    final long pdcCaseID = createCase(concernRoleID);
    bdmCaseTestDataDetails.pdcCaseID = pdcCaseID;

    this.createProductDelivery(concernRoleID, pdcCaseID, productName);

    final long casePtcptRoleID_Nominee =
      this.createCPR(pdcCaseID, concernRoleID);
    bdmCaseTestDataDetails.casePtcptRoleID_Nominee = casePtcptRoleID_Nominee;

    final long caseNominneID = this.createCaseNomineeDetails(pdcCaseID,
      casePtcptRoleID_Nominee, concernRoleID);
    bdmCaseTestDataDetails.caseNomineeID = caseNominneID;

    return bdmCaseTestDataDetails;
  }

  public ApplicationCaseKey createApplicationCase(final long concernRoleID)
    throws AppException, InformationalException {

    // create Application case
    final ApplicationCase appCase = ApplicationCaseFactory.newInstance();

    final ApplicationCaseCreateDetails appCaseDetails =
      new ApplicationCaseCreateDetails();
    appCaseDetails.concernRoleID = concernRoleID;
    appCaseDetails.dtls.applicationCaseAdminID = 80001l;
    appCaseDetails.dtls.applicationDate = TransactionInfo.getSystemDate();
    appCaseDetails.dtls.methodOfApplication = METHODOFAPPLICATION.INPERSON;

    final curam.commonintake.entity.struct.ApplicationCaseKey appCaseKey =
      appCase.createApplicationCaseForConcernRole(appCaseDetails);

    return appCaseKey;
  }
}
