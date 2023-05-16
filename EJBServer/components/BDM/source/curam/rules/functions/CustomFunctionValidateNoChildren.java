package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import java.util.List;

/**
 * Contains a custom function to validate number specified is not greater than
 * 99
 *
 * @curam.exclude
 */
public class CustomFunctionValidateNoChildren extends CustomFunctor {

  /**
   * A custom function to validate no of children
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

    boolean valid = true;

    final List<Adaptor> parameters = getParameters();
    if (null == parameters) {
      // Parameters are null, the questions were not displayed so no need to
      // validate
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final String province =
      ((StringAdaptor) parameters.get(0)).getStringValue(rulesParameters);

    if (!province.equals(CustomFunctionConstants.PROVINCE_SK)) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final long noOfChildern =
      ((LongAdaptor) parameters.get(1)).getLongValue(rulesParameters);

    // TASk :13986 - Fix No . of children be between 0 -99
    if (noOfChildern < 0 || noOfChildern > 99) {
      valid = false;
    }

    return AdaptorFactory.getBooleanAdaptor(valid);

  }

}
