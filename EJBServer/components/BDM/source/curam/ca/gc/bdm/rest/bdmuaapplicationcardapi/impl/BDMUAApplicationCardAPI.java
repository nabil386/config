package curam.ca.gc.bdm.rest.bdmuaapplicationcardapi.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmuaapplicationcardapi.struct.BDMUASubmittedApplication;
import curam.ca.gc.bdm.rest.bdmuaapplicationcardapi.struct.BDMUASubmittedApplicationList;
import curam.citizenworkspace.rest.facade.fact.ApplicationAPIFactory;
import curam.citizenworkspace.rest.facade.intf.ApplicationAPI;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplication;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationList;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationProgram;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.codetable.impl.PRODUCTTYPEEntry;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.infrastructure.assessment.fact.CaseDeterminationFactory;
import curam.core.facade.infrastructure.assessment.intf.CaseDetermination;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionDetailsList;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionListDetails;
import curam.core.facade.infrastructure.assessment.struct.CaseIDDeterminationIDKey;
import curam.core.facade.intf.CaseHeader;
import curam.core.facade.intf.ProductDelivery;
import curam.core.facade.struct.CaseHeaderDetails;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.fact.PersonFactory;
import curam.core.intf.Person;
import curam.core.sl.infrastructure.assessment.codetable.impl.CASEDETERMINATIONINTERVALRESULTEntry;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.codetable.impl.IntakeApplicationStatusEntry;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.fact.IntakeApplicationFactory;
import curam.workspaceservices.intake.intf.IntakeApplication;
import curam.workspaceservices.intake.struct.IntakeApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeApplicationKey;
import java.util.Comparator;

public class BDMUAApplicationCardAPI extends
  curam.ca.gc.bdm.rest.bdmuaapplicationcardapi.base.BDMUAApplicationCardAPI {

  public BDMUAApplicationCardAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Gets the list of submitted applications
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMUASubmittedApplicationList listSubmittedApplications()
    throws AppException, InformationalException {

    final BDMUASubmittedApplicationList bdmUASubmittedApplicationList =
      new BDMUASubmittedApplicationList();

    final ApplicationAPI applicationAPIObj =
      ApplicationAPIFactory.newInstance();
    final UASubmittedApplicationList uaSubmittedApplicationsList =
      applicationAPIObj.listSubmittedApplications();

    for (final UASubmittedApplication uaSubmittedApplication : uaSubmittedApplicationsList.data) {
      // Get the Application Program So we can get the proper caseID
      final UASubmittedApplicationProgram uaSubmittedApplicationProgram =
        uaSubmittedApplication.applicationPrograms.get(0);
      BDMUASubmittedApplication bdmUASubmittedApplication =
        new BDMUASubmittedApplication();

      if (uaSubmittedApplicationProgram.programStatusDetails.status
        .equals(IntakeProgramApplicationStatusEntry.PENDING.getCode())) {
        // For Pending Application Programs, get application ref # and set to
        // benefit ID
        bdmUASubmittedApplication.dtls.assign(uaSubmittedApplication);
        final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
        final IntakeApplication intakeApplicationObj =
          IntakeApplicationFactory.newInstance();
        final IntakeApplicationKey intakeApplicationkey =
          new IntakeApplicationKey();
        intakeApplicationkey.intakeApplicationID =

          bdmUASubmittedApplication.dtls.application_id;

        final IntakeApplicationDtls intakeApplicationDtls =
          intakeApplicationObj.read(notFoundIndicator, intakeApplicationkey);
        if (!notFoundIndicator.isNotFound()) {
          bdmUASubmittedApplication.benefitID =
            intakeApplicationDtls.reference;
        }
      } else if (uaSubmittedApplicationProgram.programStatusDetails.status
        .equals(IntakeProgramApplicationStatusEntry.APPROVED.getCode())) {
        // For Approved/NonPending Applications Programs
        bdmUASubmittedApplication =
          approvedApplication(uaSubmittedApplication);
      } else {
        // For all other Application Programs statuses
        final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
        final CaseIDDetails caseIDDetails = new CaseIDDetails();
        caseIDDetails.caseID = uaSubmittedApplicationProgram.case_id;
        final CaseHeaderDetails applicationCaseHeaderDetails =
          caseHeaderObj.readCaseHeader(caseIDDetails);
        bdmUASubmittedApplication.dtls.assign(uaSubmittedApplication);
        // Account for Pending Application
        if (applicationCaseHeaderDetails.dtls.caseTypeCode
          .equals(CASETYPECODEEntry.APPLICATION_CASE.getCode())) {
          bdmUASubmittedApplication.benefitID =
            applicationCaseHeaderDetails.dtls.caseReference;
        }
      }
      bdmUASubmittedApplicationList.data.add(bdmUASubmittedApplication);
      // Begin - PN - Create order for submitted benefit cards
      final BDMUAApplicationCardComparator bdmUAApplicationCardComparator =
        new BDMUAApplicationCardComparator();
      bdmUASubmittedApplicationList.data.sort(bdmUAApplicationCardComparator);
      // Begin - PN - Create order for submitted benefit cards
    }
    return bdmUASubmittedApplicationList;
  }

  /**
   * Method for populating BDMUASubmittedApplication with CaseID
   *
   * @param caseIDDetials
   * @param uaSubmittedApplication
   * @throws AppException
   * @throws InformationalException
   */
  public BDMUASubmittedApplication populateBDMUASubmittedApplication(
    final CaseIDDetails caseIDDetials,
    final UASubmittedApplication uaSubmittedApplication)
    throws AppException, InformationalException {

    final BDMUASubmittedApplication bdmUASubmittedApplication =
      new BDMUASubmittedApplication();
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

    final CaseHeaderDetails caseHeaderDetails =
      caseHeaderObj.readCaseHeader(caseIDDetials);

    bdmUASubmittedApplication.dtls.assign(uaSubmittedApplication);

    // Get Product Delivery Information for benefit to fill BDM Name
    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
    productDeliveryKey.caseID = caseHeaderDetails.dtls.caseID;
    final ProductDeliveryTypeDetails productDeliveryTypeDetails =
      productDeliveryObj.readProductType(productDeliveryKey);
    final String productType = productDeliveryTypeDetails.productType;

    bdmUASubmittedApplication.dtls.name = CodeTable.getOneItemForUserLocale(

      PRODUCTTYPEEntry.get(productType).getTableName().toString(),
      PRODUCTTYPEEntry.get(productType).getCode());

    // Get and fill CaseReference and Status for the benefit
    bdmUASubmittedApplication.caseStatus = caseHeaderDetails.dtls.statusCode;
    bdmUASubmittedApplication.benefitID =
      caseHeaderDetails.dtls.caseReference;

    // Get Payment Frequency, Method, Eligibility, Coverage Period
    final CaseDetermination caseDeterminationObj =
      CaseDeterminationFactory.newInstance();
    final CaseIDDeterminationIDKey caseIDDeterminationIDKey =
      new CaseIDDeterminationIDKey();
    caseIDDeterminationIDKey.caseID = caseHeaderDetails.dtls.caseID;
    final CaseDeterminationDecisionDetailsList caseDeterminationDecisionDetailsList =
      caseDeterminationObj
        .listDecisionPeriodsForDetermination(caseIDDeterminationIDKey);

    // Fill Payment Frequency, Eligibility, Coverage Period
    for (final CaseDeterminationDecisionListDetails caseDeterminationDecisonListDetails : caseDeterminationDecisionDetailsList.dtls) {

      // Remove excess information from coverPeriod
      String covPer = new String();
      covPer = caseDeterminationDecisonListDetails.coverPeriod;

      covPer = covPer.substring(covPer.lastIndexOf("("));
      covPer = covPer.replace(')', ' ');
      covPer = covPer.replace('(', ' ');
      covPer = covPer.replace('-', '/');
      covPer = covPer.replace('|', '-');
      covPer = covPer.trim();

      final Date fromDate =
        caseDeterminationDecisonListDetails.decisionFromDate;
      final Date toDate = caseDeterminationDecisonListDetails.decisionToDate;
      if ((Date.getCurrentDate().after(fromDate)
        || Date.getCurrentDate().equals(fromDate))
        && (Date.getCurrentDate().before(toDate)
          || Date.getCurrentDate().equals(toDate))) {
        bdmUASubmittedApplication.coveragePeriod = covPer;
        // ResultCode gives gives an eligibility code
        bdmUASubmittedApplication.eligibility = determineEligibility(
          caseDeterminationDecisonListDetails.resultCode);

        // PN - add Payment type to payment method
        bdmUASubmittedApplication.paymentMethod =
          caseDeterminationDecisonListDetails.summary
            + determinePaymentType(caseHeaderDetails);
      }
    }
    // Account for ineligibility
    if (caseDeterminationDecisionDetailsList.dtls.isEmpty()) {
      bdmUASubmittedApplication.eligibility = BDMConstants.knot_eligible;
    }
    return bdmUASubmittedApplication;
  }

  /**
   * Populates a BDMUASubmittedApplication if the application has been approved
   *
   * @param uaSubmittedApplication
   * @throws AppException
   * @throws InformationalException
   */

  public BDMUASubmittedApplication
    approvedApplication(final UASubmittedApplication uaSubmittedApplication)
      throws AppException, InformationalException {

    BDMUASubmittedApplication bdmUASubmittedApplication =
      new BDMUASubmittedApplication();

    final UASubmittedApplicationProgram uaSubmittedApplicationProgram =
      uaSubmittedApplication.applicationPrograms.get(0);

    final CaseIDDetails caseIDDetials = new CaseIDDetails();
    caseIDDetials.caseID = uaSubmittedApplicationProgram.case_id;

    bdmUASubmittedApplication = populateBDMUASubmittedApplication(
      caseIDDetials, uaSubmittedApplication);

    return bdmUASubmittedApplication;
  }

  /**
   * Find the Payment Type
   *
   * @param benefitCaseHeaderDetails
   * @throws AppException
   * @throws InformationalException
   */
  public static String
    determinePaymentType(final CaseHeaderDetails benefitCaseHeaderDetails)
      throws AppException, InformationalException {

    String paymentMethodCode = "";
    String paymentMethod = "";

    final Person person = PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = benefitCaseHeaderDetails.dtls.concernRoleID;
    final PersonDtls personDtls = person.read(personKey);
    paymentMethodCode = personDtls.methodOfPmtCode;

    if (paymentMethodCode
      .contentEquals(METHODOFDELIVERYEntry.CHEQUE.getCode())) {

      paymentMethod = BDMConstants.BY + CodeTable.getOneItemForUserLocale(
        METHODOFDELIVERYEntry.CHEQUE.getTableName().toString(),
        METHODOFDELIVERYEntry.CHEQUE.getCode());
    } else if (paymentMethodCode
      .contentEquals(METHODOFDELIVERYEntry.CASH.getCode())) {
      paymentMethod = BDMConstants.BY + CodeTable.getOneItemForUserLocale(
        METHODOFDELIVERYEntry.CASH.getTableName().toString(),
        METHODOFDELIVERYEntry.CASH.getCode());
    } else if (paymentMethodCode
      .contentEquals(METHODOFDELIVERYEntry.VOUCHER.getCode())) {
      paymentMethod = BDMConstants.BY + CodeTable.getOneItemForUserLocale(
        METHODOFDELIVERYEntry.VOUCHER.getTableName().toString(),
        METHODOFDELIVERYEntry.VOUCHER.getCode());
    } else if (paymentMethodCode
      .contentEquals(METHODOFDELIVERYEntry.NOT_SPECIFIED.getCode())) {
      paymentMethod = BDMConstants.BY + CodeTable.getOneItemForUserLocale(
        METHODOFDELIVERYEntry.NOT_SPECIFIED.getTableName().toString(),
        METHODOFDELIVERYEntry.NOT_SPECIFIED.getCode());
    } else {
      paymentMethod = BDMConstants.BY + BDMConstants.DIRECTDEPOSIT;
    }
    return paymentMethod;
  }

  /**
   * Find the eligibility
   *
   * @param eligiblityCode
   */
  public static String determineEligibility(final String eligiblityCode) {

    String eligibility = new String();
    final String tableName = CASEDETERMINATIONINTERVALRESULTEntry.TABLENAME;

    if (eligiblityCode
      .equals(CASEDETERMINATIONINTERVALRESULTEntry.ELIGIBLE.getCode())) {
      eligibility = BDMUtil.getCodeTableDescriptionForUserLocale(tableName,
        CASEDETERMINATIONINTERVALRESULTEntry.ELIGIBLE.getCode());
    } else if (eligiblityCode
      .equals(CASEDETERMINATIONINTERVALRESULTEntry.INELIGIBLE.getCode())) {
      eligibility = BDMUtil.getCodeTableDescriptionForUserLocale(tableName,
        CASEDETERMINATIONINTERVALRESULTEntry.INELIGIBLE.getCode());
    } else if (eligiblityCode.equals(
      CASEDETERMINATIONINTERVALRESULTEntry.INDETERMINABLE.getCode())) {
      eligibility = BDMUtil.getCodeTableDescriptionForUserLocale(tableName,
        CASEDETERMINATIONINTERVALRESULTEntry.INDETERMINABLE.getCode());
    }

    return eligibility;
  }

  /**
   * Implement Sorting Logic for benefit cards
   *
   */
  public static class BDMUAApplicationCardComparator
    implements Comparator<BDMUASubmittedApplication> {

    @Override
    public int compare(final BDMUASubmittedApplication o1,
      final BDMUASubmittedApplication o2) {

      // Begin - PN - Maintain Consistency for Equal dates
      if (o1.dtls.submittedOn.compareTo(o2.dtls.submittedOn) == 0) {
        // PN - keep pending to front
        if (o1.dtls.status
          .contentEquals(IntakeApplicationStatusEntry.PENDING.toString())) {
          return -1;
        } else if (o2.dtls.status
          .contentEquals(IntakeApplicationStatusEntry.PENDING.toString())) {
          return 1;
        } else {
          // PN - Sort in order of benefit ID for consistency
          return o1.benefitID.compareTo(o2.benefitID) * -1;
        }
      }
      // End - PN - Maintain Consistency for Equal dates
      return o1.dtls.submittedOn.compareTo(o2.dtls.submittedOn) * -1;

    }
  }
  // End - PN - Implement Sorting Logic for benefit cards
}
