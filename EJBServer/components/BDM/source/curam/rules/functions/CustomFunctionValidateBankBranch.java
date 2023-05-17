package curam.rules.functions;

import curam.core.fact.BankBranchFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.BankBranch;
import curam.core.struct.BankBranchDtlsList;
import curam.core.struct.SortCodeStruct;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import java.util.List;

/**
 * Contains a custom function that will be called to validate bank branch
 * numbers.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateBankBranch extends CustomFunctor {

  /**
   * A custom function that will be called to validate validate bank branch
   * numbers.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the object is null or not.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    if (!Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION)) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final List<Adaptor> parameters = getParameters();
    if (parameters.size() == 2 && parameters.get(0) == null
      && parameters.get(1) == null) {
      // Parameters are null, the questions were not displayed so no need to
      // validate
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final String bankInstNum =
      ((StringAdaptor) parameters.get(0)).getStringValue(rulesParameters);

    final String bankBranchNum =
      ((StringAdaptor) parameters.get(1)).getStringValue(rulesParameters);

    final boolean valid =
      bankInstNum.length() == 3 && bankBranchNum.length() == 5;

    if (valid) {

      // search BankBranch table using sortCode
      final BankBranch bankBranch = BankBranchFactory.newInstance();
      final SortCodeStruct key = new SortCodeStruct();
      key.bankSortCode = bankInstNum + bankBranchNum;
      final BankBranchDtlsList branchList = bankBranch.searchBySortCode(key);
      return AdaptorFactory.getBooleanAdaptor(!branchList.dtls.isEmpty());
    }

    return AdaptorFactory.getBooleanAdaptor(valid);
  }
}
