package curam.rules.functions;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import java.util.List;

/**
 * TASK- 24443 Dependent Back Button is not Working
 *
 * @curam.exclude
 */
public class CustomFunctionValidateNumber extends CustomFunctor {

  /**
   * A custom function to validate Number size
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

    final boolean valid = true;

    final List<Adaptor> parameters = getParameters();
    if (null == parameters) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    int index = 0;
    int fieldValue = 0;

    fieldValue = getOptionalIntegerParam(rulesParameters, index++);
    final String expectedLength =
      getOptionalStringParam(rulesParameters, index++);

    final String type = getOptionalStringParam(rulesParameters, index++);

    if (!StringUtil.isNullOrEmpty(type)) {
      if (BDMConstants.kDEPENDENT_MAX.equalsIgnoreCase(type)) {

        if (fieldValue > BDMConstants.kDEPENDENT_MAX_VALUE) {
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
      if (BDMConstants.kDEPENDENT_MIN.equalsIgnoreCase(type)) {

        if (fieldValue <= BDMConstants.kDEPENDENT_MIN_VALUE) {
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
      // }
    }
    return AdaptorFactory.getBooleanAdaptor(true);

  }

  /**
   *
   * Gets an optional string parameter.
   * If the parameter is not present it returns null.
   *
   * @param rulesParameters the rules parameters
   * @param index the index
   * @return the optional string parameter
   * @throws InformationalException the informational exception
   * @throws AppException the app exception
   */
  public String getOptionalStringParam(final RulesParameters rulesParameters,
    final int index) throws InformationalException, AppException {

    final StringAdaptor stringAdaptor =
      (StringAdaptor) getParameters().get(index);
    return stringAdaptor == null ? null
      : stringAdaptor.getStringValue(rulesParameters);
  }

  public int getOptionalIntegerParam(final RulesParameters rulesParameters,
    final int index) throws InformationalException, AppException {

    final IntegerAdaptor integerAdaptor =
      (IntegerAdaptor) getParameters().get(index);
    return integerAdaptor == null ? null
      : integerAdaptor.getIntegerValue(rulesParameters);
  }

}
