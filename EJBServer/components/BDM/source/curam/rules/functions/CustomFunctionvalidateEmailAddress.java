package curam.rules.functions;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;

/**
 * BUG : 11176 Missing character limit error message for email address in
 * Preferred and Alternate contact information page
 *
 * Validate preferred email address and alernate email address.ID email addres
 * does not match pattern or length > 255 return false, true otherwise
 *
 */
public class CustomFunctionvalidateEmailAddress extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    boolean emailEntered = true;

    final String emailRegex =
      "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$";

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor emailAddressAdaptor = params.get(0);
    final Adaptor emailTypeAdaptor = params.get(1);

    if (null != emailAddressAdaptor && null != emailTypeAdaptor) {

      final String email =
        ((StringAdaptor) emailAddressAdaptor).getStringValue(rulesParameters);
      // Task-63402 if empty string then pass validation
      if (StringUtil.isNullOrEmpty(email)) {
        // email is optional
        emailEntered = true;
      } else {
        if (!email.matches(emailRegex))
          emailEntered = false;
        if (email.length() > 254)
          emailEntered = false;
      }
    }
    return AdaptorFactory.getBooleanAdaptor(emailEntered);
  }

}
