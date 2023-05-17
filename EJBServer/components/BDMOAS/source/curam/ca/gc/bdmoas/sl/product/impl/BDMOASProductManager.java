package curam.ca.gc.bdmoas.sl.product.impl;

import com.google.inject.Inject;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASERELATIONSHIPREASONCODE;
import curam.codetable.CASESTATUS;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.PRODUCTTYPE;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.ProductDelivery;
import curam.core.facade.struct.CertificationCaseIDKey;
import curam.core.facade.struct.CertificationListDetails;
import curam.core.facade.struct.CreateCertificationDetails;
import curam.core.facade.struct.ListCertificationDetails;
import curam.core.facade.struct.MaintainCertificationDetails;
import curam.core.facade.struct.MaintainCertificationKey;
import curam.core.facade.struct.ReadCertificationDetails;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CreateProductDeliveryFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.ProductDeliveryActivationEligibilityFactory;
import curam.core.impl.EnvVars;
import curam.core.impl.ProductDeliveryLifeCycleEvents;
import curam.core.intf.CaseHeader;
import curam.core.intf.CreateProductDelivery;
import curam.core.intf.Person;
import curam.core.intf.ProductDeliveryActivationEligibility;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeCaseIDStatusesKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseHeaderReadmultiDetails1;
import curam.core.struct.CaseHeaderReadmultiDetails1List;
import curam.core.struct.CaseHeaderReadmultiKey1;
import curam.core.struct.CaseIDDetails;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.core.struct.RegisterProductDeliveryDetails;
import curam.core.struct.RegisterProductDeliveryKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.helper.EventDispatcherFactory;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.Calendar;

/**
 * Creates and maintains OAS Benefit PDCs for the given Integrated Case. This
 * implementation follows the HCR model to accommodate deferred verification
 * items that are created and cleared in separate transactions.
 */
public class BDMOASProductManager {

  private final CaseHeaderDtls integratedCaseHeaderDtls;

  @Inject
  protected EventDispatcherFactory<ProductDeliveryLifeCycleEvents> productDeliveryLifeCycleEventDispatcherFactory;

  public BDMOASProductManager(final CaseHeaderDtls integratedCaseHeaderDtls) {

    GuiceWrapper.getInjector().injectMembers(this);

    this.integratedCaseHeaderDtls = integratedCaseHeaderDtls;

  }

  /**
   * Creates a PDC if one does not exist and the primary participant is applying
   * for benefits. Maintains the certification from date as the month following
   * the 60th birthday. Activates the PDC if not already active.
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void manageProduct() throws AppException, InformationalException {

    if (!BDMOASProductUtility
      .isOpenOASIntegratedCase(this.integratedCaseHeaderDtls)) {
      return;
    }

    final CaseHeaderDtlsList caseHeaderDtlsList = this.listOASBenefitPDCs();

    if (caseHeaderDtlsList.dtls.size() < 1
      && this.applicationDetailsExist()) {
      caseHeaderDtlsList.dtls.addRef(this.createProductDeliveryCase());
    }

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderDtlsList.dtls) {
      this.maintainCertificationPeriod(caseHeaderDtls);
    }

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderDtlsList.dtls) {
      this.maintainPDCStatus(caseHeaderDtls);
    }

  }

  /**
   * Returns all non-closed OAS Benefit PDCs for the Integrated Case.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private CaseHeaderDtlsList listOASBenefitPDCs()
    throws AppException, InformationalException {

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderReadmultiKey1 caseHeaderKey =
      new CaseHeaderReadmultiKey1();
    caseHeaderKey.integratedCaseID = this.integratedCaseHeaderDtls.caseID;

    final CaseHeaderReadmultiDetails1List pdcDetailsList =
      caseHeaderObj.searchByIntegratedCaseID(caseHeaderKey);

    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final ProductDeliveryKey pdcKey = new ProductDeliveryKey();

    final CaseHeaderDtlsList caseHeaderDtlsList = new CaseHeaderDtlsList();

    for (final CaseHeaderReadmultiDetails1 pdcDetails : pdcDetailsList.dtls) {

      if (!CASESTATUS.CLOSED.equals(pdcDetails.statusCode)) {

        pdcKey.caseID = pdcDetails.caseID;

        final ProductDeliveryTypeDetails productDeliveryTypeDetails =
          productDeliveryObj.readProductType(pdcKey);

        if (PRODUCTTYPE.OAS_BENEFITS
          .equals(productDeliveryTypeDetails.productType)) {

          final CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
          caseHeaderDtls.assign(pdcDetails);
          caseHeaderDtlsList.dtls.addRef(caseHeaderDtls);

        }

      }

    }

    return caseHeaderDtlsList;

  }

  /**
   * Returns true if active Application Details evidence exists for the primary
   * participant.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean applicationDetailsExist()
    throws AppException, InformationalException {

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceTypeCaseIDStatusesKey evidenceDescriptorKey =
      new EvidenceTypeCaseIDStatusesKey();
    evidenceDescriptorKey.caseID = this.integratedCaseHeaderDtls.caseID;
    evidenceDescriptorKey.evidenceType = CASEEVIDENCE.OAS_APPLICATION_DETAILS;
    evidenceDescriptorKey.statusCode1 = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
      evidenceDescriptorObj
        .searchByCaseIDEvidenceTypeAndStatus(evidenceDescriptorKey);

    for (final EvidenceDescriptorDtls evidenceDescriptorDtls : evidenceDescriptorDtlsList.dtls) {

      if (this.integratedCaseHeaderDtls.concernRoleID == evidenceDescriptorDtls.participantID) {
        return true;
      }

    }

    return false;

  }

  /**
   * Creates an OAS Benefits PDC.
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private CaseHeaderDtls createProductDeliveryCase()
    throws AppException, InformationalException {

    final CreateProductDelivery createProductDelivery =
      CreateProductDeliveryFactory.newInstance();
    final RegisterProductDeliveryKey productDeliveryKey =
      new RegisterProductDeliveryKey();
    productDeliveryKey.integratedCaseID =
      this.integratedCaseHeaderDtls.caseID;
    productDeliveryKey.caseStartDate = Date.getCurrentDate();
    productDeliveryKey.clientID = this.integratedCaseHeaderDtls.concernRoleID;
    productDeliveryKey.currencyType =
      Configuration.getProperty(EnvVars.ENV_BASECURRENCY);
    productDeliveryKey.productID = 920000;
    productDeliveryKey.productProviderID = 80000;
    productDeliveryKey.productDeliveryPatternID = 920000;
    productDeliveryKey.providerLocationID = 80000;
    productDeliveryKey.receivedDate = TransactionInfo.getSystemDate();
    productDeliveryKey.relatedCaseID = this.integratedCaseHeaderDtls.caseID;
    productDeliveryKey.caseRelationshipReasonCode =
      CASERELATIONSHIPREASONCODE.LINKEDCASES;

    final RegisterProductDeliveryDetails productDeliveryDetails =
      new RegisterProductDeliveryDetails();
    productDeliveryDetails.integratedCaseID =
      this.integratedCaseHeaderDtls.caseID;
    productDeliveryDetails.integratedCaseInd = true;

    final RegisterProductDeliveryDetails registerProductDeliveryDetails =
      createProductDelivery.registerProductDelivery(productDeliveryKey,
        productDeliveryDetails);

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = registerProductDeliveryDetails.caseID;

    return caseHeaderObj.read(caseHeaderKey);

  }

  /**
   * Creates a certification is one does not exist, otherwise maintains the
   * certification from date as the earliest of the existing from date or the
   * month following the client's 60th birthday.
   *
   * @param caseHeaderDtls
   * @throws AppException
   * @throws InformationalException
   */
  private void
    maintainCertificationPeriod(final CaseHeaderDtls caseHeaderDtls)
      throws AppException, InformationalException {

    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final CertificationCaseIDKey certificationCaseIDKey =
      new CertificationCaseIDKey();
    certificationCaseIDKey.caseID = caseHeaderDtls.caseID;

    final ListCertificationDetails listCertificationDetails =
      productDeliveryObj.listCertification(certificationCaseIDKey);

    final Date periodFromDate = this.calculateCertificationFromDate(
      this.integratedCaseHeaderDtls.concernRoleID);

    if (listCertificationDetails.dtls.size() < 1) {
      this.createCertification(caseHeaderDtls, periodFromDate);
    } else {
      for (final CertificationListDetails certificationDetails : listCertificationDetails.dtls) {
        final MaintainCertificationKey certificationKey =
          new MaintainCertificationKey();
        certificationKey.certificationDiaryID =
          certificationDetails.certificationDiaryID;
        final ReadCertificationDetails readCertificationDetails =
          productDeliveryObj.readCertification(certificationKey);
        if (periodFromDate
          .compareTo(certificationDetails.periodFromDate) < 0) {
          final MaintainCertificationDetails maintainCertificationDetails =
            new MaintainCertificationDetails();
          maintainCertificationDetails
            .assign(readCertificationDetails.maintainCertificationDetails);
          maintainCertificationDetails.caseID = caseHeaderDtls.caseID;
          maintainCertificationDetails.periodFromDate = periodFromDate;
          productDeliveryObj
            .modifyCertification(maintainCertificationDetails);
        }
      }
    }

  }

  /**
   * Creates a certification period from the client's 60th birthday.
   *
   * @param caseHeaderDtls
   * @param periodFromDate
   * @throws AppException
   * @throws InformationalException
   */
  private void createCertification(final CaseHeaderDtls caseHeaderDtls,
    final Date periodFromDate) throws AppException, InformationalException {

    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();

    final CreateCertificationDetails certificationDetails =
      new CreateCertificationDetails();
    certificationDetails.caseID = caseHeaderDtls.caseID;
    certificationDetails.periodFromDate = periodFromDate;
    certificationDetails.periodToDate = Date.fromISO8601("99991231");
    certificationDetails.certificationReceivedDate = Date.getCurrentDate();

    productDeliveryObj.createCertification(certificationDetails);

  }

  /**
   * Activates the PDC if not already active.
   *
   * @param caseHeaderDtls
   * @throws AppException
   * @throws InformationalException
   */
  private void maintainPDCStatus(final CaseHeaderDtls caseHeaderDtls)
    throws AppException, InformationalException {

    if (!CASESTATUS.ACTIVE.equals(caseHeaderDtls.statusCode)
      && !CASESTATUS.DELAYEDPROC.equals(caseHeaderDtls.statusCode)) {
      final ProductDeliveryActivationEligibility activationEligibility =
        ProductDeliveryActivationEligibilityFactory.newInstance();
      final CaseIDDetails caseIDDetails = new CaseIDDetails();
      caseIDDetails.caseID = caseHeaderDtls.caseID;
      activationEligibility.assessEligibilityForCase(caseIDDetails);

      this.productDeliveryLifeCycleEventDispatcherFactory
        .get(ProductDeliveryLifeCycleEvents.class)
        .postProductDeliveryActivation(caseIDDetails.caseID);

    }

  }

  /**
   * Returns the start of the month following the client's 60th birthday.
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Date calculateCertificationFromDate(final long concernRoleID)
    throws AppException, InformationalException {

    final int pdcStartDateAge = 60;

    final Person personObj = PersonFactory.newInstance();
    final PersonKey key = new PersonKey();
    key.concernRoleID = concernRoleID;
    final PersonDtls personDtls = personObj.read(key);

    final Calendar calendar = personDtls.dateOfBirth.getCalendar();
    calendar.add(Calendar.YEAR, pdcStartDateAge);
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));

    return new Date(calendar);

  }

}
