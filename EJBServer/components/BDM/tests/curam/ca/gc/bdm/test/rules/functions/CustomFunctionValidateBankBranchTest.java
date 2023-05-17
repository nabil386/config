package curam.ca.gc.bdm.test.rules.functions;

import curam.core.fact.BankBranchFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.BankBranch;
import curam.core.struct.BankBranchDtls;
import curam.core.struct.BankBranchDtlsList;
import curam.core.struct.SortCodeStruct;
import curam.ieg.impl.IEG2Context;
import curam.rules.functions.CustomFunctionValidateBankBranch;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.functor.Adaptor;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link CustomFunctionValidateBankBranch} custom
 * function.
 */
@RunWith(JMockit.class)
public class CustomFunctionValidateBankBranchTest
  extends CustomFunctionTestJunit4 {

  /** The Constant IS_VALID. */
  private static final boolean IS_VALID = true;

  /** The bank branch. */
  @Mocked
  BankBranch bankBranch;

  /** The bank branch factory. */
  @Mocked
  private BankBranchFactory bankBranchFactory;

  /** The ieg 2 context. */
  private IEG2Context ieg2Context;

  /** The tested class. */
  @Tested
  private CustomFunctionValidateBankBranch validateBankBranch;

  /**
   * Instantiates a new CustomFunctionValidateBankBranchTest.
   */
  public CustomFunctionValidateBankBranchTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() {

    validateBankBranch = new CustomFunctionValidateBankBranch();
    ieg2Context = new IEG2Context();
  }

  /**
   * Test invalid bank branch, invalid inst num and invalid branch num length.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidBankBranch_invalidInstNumBranchNumLength()
    throws AppException, InformationalException {

    // Expectations
    super.expectationsEnvVars(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION,
      true);
    final String bankInstNum = "12";
    final String bankBranchNum = "12";
    super.expectationsParameters(bankInstNum, bankBranchNum);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateBankBranch
        .getAdaptorValue(ieg2Context);

    verificationsBankBranchDB(0);

    // Verifications
    assertEquals("The validation result should be false", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test invalid bank branch due to a invalid search by sort code.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_invalidBankBranch_invalidSearchBySortCode()
    throws AppException, InformationalException {

    final String bankInstNum = "123";
    final String bankBranchNum = "12345";
    // Expectations
    super.expectationsEnvVars(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION,
      true);
    super.expectationsParameters(bankInstNum, bankBranchNum);
    expectactionsBankBranchDB(bankInstNum, bankBranchNum, !IS_VALID);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateBankBranch
        .getAdaptorValue(ieg2Context);

    // Verify a search in the DB is done.
    verificationsBankBranchDB(1);

    // Verifications
    assertEquals("The validation result should be false", !IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid bank branch when BDM_ENV_BDM_BANKBRANCH_VALIDATION variable
   * disabled.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validBankBranch_EnvVariableDisabled()
    throws AppException, InformationalException {

    // Expectations
    super.expectationsEnvVars(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION,
      false);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateBankBranch
        .getAdaptorValue(ieg2Context);

    // Verify no search to the database are performed.
    verificationsBankBranchDB(0);

    // Verifications
    assertEquals("The validation result should be true", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid bank branch, when questions are not displayed on the screen a
   * true value is expected.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validBankBranch_QuestionNotDisplayed()
    throws AppException, InformationalException {

    // Expectations
    super.expectationsEnvVars(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION,
      true);
    // When questions are not displayed the function will receive null
    // parameters.
    super.expectationsParameters(null, null);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateBankBranch
        .getAdaptorValue(ieg2Context);

    verificationsBankBranchDB(0);

    // Verifications
    assertEquals("The validation result should be true", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Test valid bank branch valid search by sort code.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_validBankBranch_validSearchBySortCode()
    throws AppException, InformationalException {

    final String bankInstNum = "123";
    final String bankBranchNum = "12345";
    // Expectations
    super.expectationsEnvVars(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION,
      true);
    super.expectationsParameters(bankInstNum, bankBranchNum);
    expectactionsBankBranchDB(bankInstNum, bankBranchNum, IS_VALID);

    // Test
    final Adaptor.BooleanAdaptor adaptorValue =
      (Adaptor.BooleanAdaptor) validateBankBranch
        .getAdaptorValue(ieg2Context);

    // Verify a search in the DB is done.
    verificationsBankBranchDB(1);

    // Verifications
    assertEquals("The validation result should be true", IS_VALID,
      adaptorValue.getValue(ieg2Context));
  }

  /**
   * Expectactions bank branch DB.
   *
   * @param bankInstNum the bank inst num
   * @param bankBranchNum the bank branch num
   * @param searchResult the search result
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private void expectactionsBankBranchDB(final String bankInstNum,
    final String bankBranchNum, final boolean searchResult)
    throws AppException, InformationalException {

    final BankBranchDtlsList branchList = new BankBranchDtlsList();
    final BankBranchDtls e = null;
    if (searchResult) {
      branchList.dtls.add(e);
    }

    new Expectations() {

      {
        BankBranchFactory.newInstance();
        result = bankBranch;

        bankBranch.searchBySortCode((SortCodeStruct) any);
        result = branchList;
      }
    };

  }

  /**
   * Verifications bank branch DB.
   *
   * @param searchAttempts the search attempts
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private void verificationsBankBranchDB(final int searchAttempts)
    throws AppException, InformationalException {

    new Verifications() {

      {
        BankBranchFactory.newInstance();
        times = searchAttempts;

        bankBranch.searchBySortCode((SortCodeStruct) any);
        times = searchAttempts;
      }
    };
  }
}
