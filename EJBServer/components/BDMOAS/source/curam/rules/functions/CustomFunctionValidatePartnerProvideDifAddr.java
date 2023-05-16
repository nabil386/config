package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function that will validate the "'Did the applicant provide
 * a different residential
 * address for spouse/common-law partner? of partner information details.
 */
public class CustomFunctionValidatePartnerProvideDifAddr
  extends CustomFunctor {

  /**
   * Custom function to validate last name.
   * Validation Rule: "'Did the applicant provide a different residential
   * address for spouse/common-law partner?' must be selected when First name,
   * Last name or Date of Birth has been provided.."
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether validation passes or fails.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    boolean validationPassed = true;

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor firstNameParamAdaptor = params.get(0);
    final Adaptor lastNameParamAdapter = params.get(1);
    final Adaptor dobParamAdapter = params.get(2);
    final Adaptor provideDiffAddrParamAdapter = params.get(3);

    if (null != lastNameParamAdapter
      && !((StringAdaptor) lastNameParamAdapter)
        .getStringValue(rulesParameters).isEmpty()
      || null != dobParamAdapter && !((DateAdaptor) dobParamAdapter)
        .getStringValue(rulesParameters).isEmpty()
      || null != firstNameParamAdaptor
        && !((StringAdaptor) firstNameParamAdaptor)
          .getStringValue(rulesParameters).isEmpty()) {

      if (null == provideDiffAddrParamAdapter
        || ((StringAdaptor) provideDiffAddrParamAdapter)
          .getStringValue(rulesParameters).isEmpty()) {
        validationPassed = false;
      }

    }
    return AdaptorFactory.getBooleanAdaptor(validationPassed);
  }

}
