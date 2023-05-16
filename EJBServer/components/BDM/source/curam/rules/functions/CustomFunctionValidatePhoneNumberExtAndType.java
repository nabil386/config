package curam.rules.functions;

import curam.codetable.PHONETYPE;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * Personal mobile phone number or Business mobile phone number cannot have
 * extensions.
 *
 */
public class CustomFunctionValidatePhoneNumberExtAndType
  extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor phoneNumberExtAdaptor = params.get(0);
    final Adaptor phoneTypeAdaptor = params.get(1);

    if (null != phoneNumberExtAdaptor && null != phoneTypeAdaptor) {
      final String phoneNumberExt = ((StringAdaptor) phoneNumberExtAdaptor)
        .getStringValue(rulesParameters);
      final String phoneType =
        ((StringAdaptor) phoneTypeAdaptor).getStringValue(rulesParameters);

      if (phoneNumberExt.length() > 0) {
        if (phoneType.equals(PHONETYPE.BUSINESS_MOBILE)
          || phoneType.equals(PHONETYPE.PERSONAL_MOBILE)) {
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }

}
