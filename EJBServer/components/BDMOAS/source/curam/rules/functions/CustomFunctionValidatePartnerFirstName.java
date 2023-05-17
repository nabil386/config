package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Contains a custom function that will validate the ‘First name' of partner
 * information details.
 */

public class CustomFunctionValidatePartnerFirstName extends CustomFunctor {

  /**
   * Custom function to validate first name.
   * Validation Rule: "First name must be entered when Last name, Date of Birth
   * or 'Did the applicant provide a different residential address for
   * spouse/common-law partner?' has been entered."
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
      || null != provideDiffAddrParamAdapter
        && !((StringAdaptor) provideDiffAddrParamAdapter)
          .getStringValue(rulesParameters).isEmpty()) {

      if (null == firstNameParamAdaptor
        || ((StringAdaptor) firstNameParamAdaptor)
          .getStringValue(rulesParameters).isEmpty()) {
        validationPassed = false;
      }

    }
    return AdaptorFactory.getBooleanAdaptor(validationPassed);
  }

}
