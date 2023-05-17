package curam.ca.gc.bdm.test.evidence;

import com.google.inject.Inject;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.BankAccountDtls;
import curam.core.struct.ConcernRoleBankAccountDtls;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.pdc.impl.PDCBankAccountEvidencePopulator;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.StringHelper;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BDMPDCBankAccountEvidencePopulatorImplTest
  extends curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4 {

  @Inject
  Set<PDCBankAccountEvidencePopulator> pdcBankAccountEvidencePopulators;

  private CaseIDKey casedIDKey;

  private BankAccountDtls bankAccountDetails;

  private ConcernRoleKey concernRoleKey;

  private ConcernRoleBankAccountDtls concernRoleBankAccDtls;

  private DynamicEvidenceDataDetails dynEvidenceDataDetails;

  private PDCBankAccountEvidencePopulator populator;

  public BDMPDCBankAccountEvidencePopulatorImplTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Before
  public void setUp() {

    concernRoleKey = new ConcernRoleKey();
    casedIDKey = new CaseIDKey();
    concernRoleBankAccDtls = new ConcernRoleBankAccountDtls();
    // Expect one populator
    assertNotNull(pdcBankAccountEvidencePopulators);
    assertEquals(1, pdcBankAccountEvidencePopulators.size());
    populator = pdcBankAccountEvidencePopulators.iterator().next();
    assertNotNull(populator);
  }

  /**
   * Test that after calling the
   * {@link PDCBankAccountEvidencePopulator#populate(ConcernRoleKey, CaseIDKey, ConcernRoleBankAccountDtls, BankAccountDtls, DynamicEvidenceDataDetails)},
   * when
   * {@link BankAccountDtls.sortCode} is an eight character string.
   * and the selected ETV has declared both financial institution number and
   * bank transit number data attributes
   * AND that the financial institution number and bank transit number
   * attributes of the {@link DynamicEvidenceDataDetails} are populated.
   * The financial institution number has the first three characters of the
   * eight character
   * bankSortCode and bank transit number the last five characters.
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Test
  public void testPopulateLatestPDCBankAccountETV_sortCode_OK()
    throws AppException, InformationalException {

    final String bankSortCode = "ABCDEFGH";
    bankAccountDetails = createBankAccountDetails(bankSortCode);
    dynEvidenceDataDetails = DynamicEvidenceDataDetailsFactory.newInstance(
      PDCConst.PDCBANKACCOUNT, curam.util.type.Date.fromISO8601("20010101"));
    BDMEvidenceUtil.setDataAttributeNoCheck(BDMEvidenceUtil.kSORT_CODE,
      bankSortCode, dynEvidenceDataDetails);
    populator.populate(concernRoleKey, casedIDKey, concernRoleBankAccDtls,
      bankAccountDetails, dynEvidenceDataDetails);

    assertEquals("ABC",
      dynEvidenceDataDetails
        .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER)
        .getValue());
    assertEquals("DEFGH", dynEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER).getValue());
    assertEquals(StringHelper.EMPTY_STRING, dynEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kSORT_CODE).getValue());
  }

  /**
   * Test that after calling the
   * {@link PDCBankAccountEvidencePopulator#populate(ConcernRoleKey, CaseIDKey, ConcernRoleBankAccountDtls, BankAccountDtls, DynamicEvidenceDataDetails)},
   * when
   * {@link BankAccountDtls.sortCode} is NOT an eight character string.
   * and the selected ETV has declared both financial institution number and
   * bank transit number data attributes
   * AND, that the financial institution number and bank transit number
   * attributes of the {@link DynamicEvidenceDataDetails} are populated.
   *
   * The financial institution number and bank transit number attributes will
   * have empty string values.
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Test
  public void testPopulateLatestPDCBankAccountETV_sortCode_Not_OK()
    throws AppException, InformationalException {

    final String bankSortCode = "ABCDEFGHIJKL";
    bankAccountDetails = createBankAccountDetails(bankSortCode);
    dynEvidenceDataDetails = DynamicEvidenceDataDetailsFactory.newInstance(
      PDCConst.PDCBANKACCOUNT, curam.util.type.Date.fromISO8601("20000101"));
    BDMEvidenceUtil.setDataAttributeNoCheck(BDMEvidenceUtil.kSORT_CODE,
      bankSortCode, dynEvidenceDataDetails);
    populator.populate(concernRoleKey, casedIDKey, concernRoleBankAccDtls,
      bankAccountDetails, dynEvidenceDataDetails);
    assertEquals(StringHelper.EMPTY_STRING,
      dynEvidenceDataDetails
        .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER)
        .getValue());
    assertEquals(StringHelper.EMPTY_STRING, dynEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER).getValue());
    assertEquals(StringHelper.EMPTY_STRING, dynEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kSORT_CODE).getValue());

  }

  /**
   * Test that after calling the
   * {@link PDCBankAccountEvidencePopulator#populate(ConcernRoleKey, CaseIDKey, ConcernRoleBankAccountDtls, BankAccountDtls, DynamicEvidenceDataDetails)}
   * when {@link BankAccountDtls.sortCode} is an eight character string and the
   * selected ETV is missing either a declaration for either financial
   * institution number or bank transit number data attributes that the
   * attributes are populated, that the financial institution number and bank
   * transit number
   * attributes of the {@link DynamicEvidenceDataDetails} are NOT populated.
   *
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Test
  public void testPopulateLastPDCBankAccountETV()
    throws AppException, InformationalException {

    final String bankSortCode = "ABCDEFGH";
    bankAccountDetails = createBankAccountDetails(bankSortCode);
    dynEvidenceDataDetails = DynamicEvidenceDataDetailsFactory.newInstance(
      PDCConst.PDCBANKACCOUNT, curam.util.type.Date.fromISO8601("19000102"));
    BDMEvidenceUtil.setDataAttributeNoCheck(BDMEvidenceUtil.kSORT_CODE,
      bankSortCode, dynEvidenceDataDetails);
    populator.populate(concernRoleKey, casedIDKey, concernRoleBankAccDtls,
      bankAccountDetails, dynEvidenceDataDetails);
    assertNull(dynEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER));
    assertNull(dynEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER));
    assertEquals(bankSortCode, dynEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kSORT_CODE).getValue());

  }

  protected static BankAccountDtls
    createBankAccountDetails(final String bankSortCode) {

    final BankAccountDtls bankAccountDetails = new BankAccountDtls();
    bankAccountDetails.accountNumber = "0123456789";
    bankAccountDetails.bankAccountID = 1l;
    bankAccountDetails.bankAccountStatus = null;
    bankAccountDetails.bankBranchID = 2l;
    bankAccountDetails.bankSortCode = bankSortCode;
    bankAccountDetails.bic = "BIC-001";
    bankAccountDetails.endDate = curam.util.type.Date.fromISO8601("20300101");
    bankAccountDetails.iban = "IBAN-001";
    bankAccountDetails.jointAccountInd = false;
    return bankAccountDetails;
  }

}
