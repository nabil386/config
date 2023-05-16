package curam.ca.gc.bdm.creole.impl;

import curam.ca.gc.bdm.entity.fact.BDMAddressFactory;
import curam.ca.gc.bdm.entity.fact.BDMBankAccountFactory;
import curam.ca.gc.bdm.entity.struct.BDMAddressAndBenefitTypeSearchKey;
import curam.ca.gc.bdm.entity.struct.BDMAddressConcernRoleDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMAddressPDCDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMBankAccountConcernRoleDetailsList;
import curam.ca.gc.bdm.util.integrity.impl.BDMAddressIntegrityUtil;
import curam.ca.gc.bdm.util.integrity.impl.BDMAddressPerBenefitTypeCalculatorBase;
import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.impl.EnvVars;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeCaseIDStatusesKey;
import curam.core.sl.struct.EvidenceCaseKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.SortCodeAccountNumStruct;
import curam.creole.execution.session.Session;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.workspaceservices.intake.fact.IntakeAppConcernRoleLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.fact.ProgramTypeFactory;
import curam.workspaceservices.intake.struct.IntakeAppConcernRoleKey;
import curam.workspaceservices.intake.struct.IntakeAppConcernRoleLinkDtls;
import curam.workspaceservices.intake.struct.IntakeAppConcernRoleLinkDtlsList;
import curam.workspaceservices.intake.struct.IntakeApplicationKey;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtlsList;
import curam.workspaceservices.intake.struct.ProgramTypeDtls;
import curam.workspaceservices.intake.struct.ProgramTypeDtlsList;
import curam.workspaceservices.intake.struct.SearchByRecordStatusKey;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Statics class to be called from verification rule sets.
 * This class is to used only from verification rule set and not by eligibility
 * and entitlement ruleset.
 *
 */
public class BDMVerificationStatics {

  /**
   * Return the application properties value set for bank account count
   * as application properties cannot be read directly in rules.
   *
   * @param session
   * @return
   */
  public static Number getBankAccountCountLimit(final Session session) {

    // return the application properties value for bank account
    return Integer.valueOf(Configuration
      .getProperty(EnvVars.BDM_ENV_INDIVIDUALS_PER_BANK_ACCOUNT));

  }

  /**
   * Get the count of unique concernroleids which are using the given account
   * number and banksort code. This is required for the Bank Account evidence
   * verification.
   *
   * @param session
   * @param accountNumber
   * @param bankSortCode
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Number getBankAccountNumberUseCount(final Session session,
    final String accountNumber, final String bankSortCode)
    throws AppException, InformationalException {

    // Search unique count of concernroleids which has this account number and
    // the bank sort code and also the client is getting active benefit or has
    // an open application, check for not closed PDC or not closed application
    // case
    // set the search key struct
    final SortCodeAccountNumStruct searchCountBankAccountKey =
      new SortCodeAccountNumStruct();
    searchCountBankAccountKey.accountNumber = accountNumber;
    searchCountBankAccountKey.bankSortCode = bankSortCode;
    searchCountBankAccountKey.bankAccountStatus = BANKACCOUNTSTATUS.OPEN;
    searchCountBankAccountKey.statusCode = RECORDSTATUS.NORMAL;
    final BDMBankAccountConcernRoleDetailsList concernroleIDList =
      BDMBankAccountFactory.newInstance()
        .searchConcernRoleIDListByAccountNumberAndSortCode(
          searchCountBankAccountKey);
    // list will have the distinct concernrole ids(handled in SQL), return the
    // size of the list
    return concernroleIDList.dtls.size();
  }

  /**
   * Check for Address Type properties to verify if the address type needs an
   * Address Integrity Check
   *
   * @param session
   * @param addresType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Boolean isAddressTypeIntegrityCheckRequired(
    final Session session, final CodeTableItem addressType)
    throws AppException, InformationalException {

    // get properties value for address types
    final String addressTypes =
      Configuration.getProperty(EnvVars.BDM_ENV_ADDRESS_TYPES);
    // Check address type if configured
    if (addressTypes == null || addressTypes.isEmpty()
      || !addressTypes.contains(addressType.code())) {
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  /**
   * Search Individual count in whole system who are using the same address as
   * given reference address, the method is implemented for Address Integrity
   * rules.
   *
   * @param session
   * @param addresData
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Number getIndividualPerAddressUseCount(final Session session,
    final String addresData, final CodeTableItem addressType)
    throws AppException, InformationalException {

    // Search Address count for Individual(ConcernRoleIDs) for the given address
    // data.
    final BDMAddressAndBenefitTypeSearchKey searchKey =
      new BDMAddressAndBenefitTypeSearchKey();
    searchKey.addressData = addresData;
    searchKey.addressType = addressType.code();
    final BDMAddressConcernRoleDetailsList concernroleIDList =
      BDMAddressFactory.newInstance()
        .searchConcernRoleIDListByAddressData(searchKey);
    // list will have the distinct concernrole ids(handled in SQL), return the
    // size of the list
    return concernroleIDList.dtls.size();
  }

  /**
   * Search Active Benefit(PDCs) count in whole system who are using the same
   * address as given reference address, the method is implemented for Address
   * Integrity rules.
   *
   * @param session
   * @param addresData
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Number getBenefitPerAddressUseCount(final Session session,
    final String addresData, final CodeTableItem addressType)
    throws AppException, InformationalException {

    // Search Address count for Distinct Benefit for the given address
    // data.
    final BDMAddressAndBenefitTypeSearchKey searchKey =
      new BDMAddressAndBenefitTypeSearchKey();
    searchKey.addressData = addresData;
    searchKey.addressType = addressType.code();
    final OtherAddressData addressdata = new OtherAddressData();
    addressdata.addressData = addresData;
    final BDMAddressPDCDetailsList pdcIDList =
      BDMAddressFactory.newInstance().searchPDCIDListByAddressData(searchKey);
    // list will have the distinct PDC Case ids(handled in SQL), return the
    // size of the list
    return pdcIDList.dtls.size();
  }

  /**
   * Search and compare Active Benefit(PDCs) count for the benefit type
   * individual is applying in whole system who are using the same address as
   * given reference address, the method is implemented for Address Integrity
   * rules.
   *
   * @param session
   * @param addresData
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Boolean getBenefitPerAddressPerBenefitTypeUseCountCheck(
    final Session session, final String addresData,
    final Number concernRoleID, final CodeTableItem addressType)
    throws AppException, InformationalException {

    // Step 1 : Find the Benefit Type Client is applying for
    final IntakeAppConcernRoleKey intakeAppConcernRoleKey =
      new IntakeAppConcernRoleKey();
    intakeAppConcernRoleKey.concernRoleID = concernRoleID.longValue();
    final IntakeAppConcernRoleLinkDtlsList intakeAppList =
      IntakeAppConcernRoleLinkFactory.newInstance()
        .searchByConcernRole(intakeAppConcernRoleKey);
    // Get all the application and PDC configuration List from the program type
    // table
    final SearchByRecordStatusKey programTypeKey =
      new SearchByRecordStatusKey();
    programTypeKey.recordStatus = RECORDSTATUS.NORMAL;
    final ProgramTypeDtlsList programTypeDetailsList =
      ProgramTypeFactory.newInstance().searchByRecordStatus(programTypeKey);
    for (final IntakeAppConcernRoleLinkDtls intakeAppdtls : intakeAppList.dtls) {
      final IntakeApplicationKey intakeAppKey = new IntakeApplicationKey();
      intakeAppKey.intakeApplicationID = intakeAppdtls.intakeApplicationID;
      final IntakeProgramApplicationDtlsList programAppDtlsList =
        IntakeProgramApplicationFactory.newInstance()
          .searchByIntakeApplication(intakeAppKey);
      for (final IntakeProgramApplicationDtls programAppDtls : programAppDtlsList.dtls) {
        final long productid = getProductIDFromProgramID(
          programTypeDetailsList, programAppDtls.programTypeID);
        if (productid != 0) {
          // get the calculator class for address property calculation by
          // benefit type
          final BDMAddressPerBenefitTypeCalculatorBase addressThresholdCalculator =
            new BDMAddressIntegrityUtil().getCalculatorClassImpl(productid);
          if (addressThresholdCalculator != null) {
            final int benefitTypeThreshold = addressThresholdCalculator
              .getBenefitPerAddressAndBenefitTypeLimit();
            // Search Address count for Distinct Benefit for the given address
            // data and for the benefit type
            final BDMAddressAndBenefitTypeSearchKey searchKey =
              new BDMAddressAndBenefitTypeSearchKey();
            searchKey.addressData = addresData;
            searchKey.addressType = addressType.code();
            searchKey.productID = productid;
            searchKey.programtypeid = programAppDtls.programTypeID;
            final BDMAddressPDCDetailsList pdcIDList =
              BDMAddressFactory.newInstance()
                .searchPDCIDListByAddressDataAndBenefitType(searchKey);
            // list will have the distinct PDC Case ids(handled in SQL), return
            // the size of the list
            return pdcIDList.dtls.size() >= benefitTypeThreshold;
          } // end if if (addressThresholdCalculator != null)
        } // end if (productid != 0)
      } // end loop
    }
    return Boolean.FALSE;
  }

  /**
   * Return the application properties value for Address limit set for
   * Individual Per Address.
   *
   * @param session
   * @return
   */
  public static Number getIndividualPerAddressLimit(final Session session) {

    // return the application properties value for bank account
    return Integer.valueOf(
      Configuration.getProperty(EnvVars.BDM_ENV_INDIVIDUALS_PER_ADDRESS));

  }

  /**
   * Return the application properties value for Address limit set for
   * Benefit Per Address.
   *
   * @param session
   * @return
   */
  public static Number getBenefitPerAddressLimit(final Session session) {

    // return the application properties value for bank account
    return Integer.valueOf(
      Configuration.getProperty(EnvVars.BDM_ENV_BENEFITS_PER_ADDRESS));

  }

  /**
   * This method compares the incarceration period in current evidence with the
   * active incarceration evidence period. The verification is created if the
   * incarceration period is less than the active evidence period.
   *
   * @param session the CREOLE session variable
   * @param caseID case identifier
   * @param evidenceDescriptorID evidence descriptor identifier of the evidence
   * for which conditional verification has been executed
   *
   * @return true if the incarceration period is reduced otherwise false.
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static Boolean isIncarcerationDateChanged(final Session session,
    final Number caseID, final Number evidenceDescriptorID)
    throws AppException, InformationalException {

    boolean isDateChanged = false;

    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID =
      evidenceDescriptorID.longValue();

    final EvidenceDescriptor evidenceDescriptor =
      EvidenceDescriptorFactory.newInstance();

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      evidenceDescriptor.read(evidenceDescriptorKey);

    // get start and end date for the current evidence
    final DynamicEvidenceDataDetails evidenceDataDetails =
      getEvidenceData(caseID, evidenceDescriptorDtls);

    final Date currentStartDate = (Date) DynamicEvidenceTypeConverter
      .convert(evidenceDataDetails.getAttribute("startDate"));

    final Date currentEndDate = (Date) DynamicEvidenceTypeConverter
      .convert(evidenceDataDetails.getAttribute("endDate"));

    // get all active incarceration evidences for case
    final EvidenceTypeCaseIDStatusesKey evidenceTypeCaseIDStatusesKey =
      new EvidenceTypeCaseIDStatusesKey();

    evidenceTypeCaseIDStatusesKey.caseID = caseID.longValue();
    evidenceTypeCaseIDStatusesKey.evidenceType =
      evidenceDescriptorDtls.evidenceType;
    evidenceTypeCaseIDStatusesKey.statusCode1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    final EvidenceDescriptorDtlsList evidencedDescriptorDtlsList =
      evidenceDescriptor
        .searchByCaseIDEvidenceTypeAndStatus(evidenceTypeCaseIDStatusesKey);

    // filter the active evidence list for the participant
    final List<EvidenceDescriptorDtls> participantEvidencedDescriptorDtlsList =
      evidencedDescriptorDtlsList.dtls.stream().filter(
        evdDescriptorDtls -> evdDescriptorDtls.participantID == evidenceDescriptorDtls.participantID)
        .collect(Collectors.toList());

    // compare start and end dates on an active evidence to check if
    // incarceration period has been reduced
    for (final EvidenceDescriptorDtls evdDescriptor : participantEvidencedDescriptorDtlsList) {

      // get evidence data for this evidence descriptor
      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        getEvidenceData(caseID, evdDescriptor);

      final Date startDate = (Date) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute("startDate"));

      final Date endDate = (Date) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute("endDate"));

      // check if start date is changed or if its new non overlapping evidence
      if (currentStartDate.after(startDate) && !endDate.isZero()
        && currentStartDate.before(endDate)) {
        isDateChanged = true;
      }

      // check if the end date not null and updated to end before the existing
      // end date
      if (!endDate.isZero() && !currentEndDate.isZero()
        && currentEndDate.before(endDate)) {
        isDateChanged = true;
      }
    }

    return isDateChanged;
  }

  /**
   * This method returns the dynamic evidence data for the evidence.
   *
   * @param caseID case identifier
   * @param evidenceDescriptorDtls evidence descriptor identifier for which the
   * evidence data
   * @return the dynamic evidence data
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public static DynamicEvidenceDataDetails getEvidenceData(
    final Number caseID, final EvidenceDescriptorDtls evidenceDescriptorDtls)
    throws AppException, InformationalException {

    final EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
    evidenceCaseKey.caseIDKey.caseID = caseID.longValue();
    evidenceCaseKey.evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
    evidenceCaseKey.evidenceKey.evType = evidenceDescriptorDtls.evidenceType;

    // get dynamic evidence data
    final DynamicEvidenceDataDetails evidenceDataDetails =
      EvidenceGenericSLFactory.instance(evidenceDescriptorDtls.relatedID)
        .readEvidence(evidenceCaseKey).dtls;

    return evidenceDataDetails;
  }

  /**
   * Check if SIN Evidence has an end date in past,
   * called from SIN Verification Rules
   *
   * @param session
   * @param sinEndDate
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Boolean isSINEnDatedInPast(final Session session,
    final Date sinEndDate) throws AppException, InformationalException {

    // Check end date on SIN evidence
    if (sinEndDate != null && !sinEndDate.isZero()
      && sinEndDate.before(Date.getCurrentDate())) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  /**
   *
   * @param programTypeDetailsList
   * @param programTypeID
   * @return
   */
  private static long getProductIDFromProgramID(
    final ProgramTypeDtlsList programTypeDetailsList,
    final long programTypeID) {

    for (final ProgramTypeDtls pDtls : programTypeDetailsList.dtls) {
      if (pDtls.programTypeID == programTypeID) {
        return pDtls.productID;
      }
    }
    return 0l;
  }
}
