package curam.rules.functions;

import curam.codetable.IEGYESNO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function to validate applicant or authorized person
 * declaration signature selection business rule.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateAplcntOrWitnessSigndDeclrn
  extends BDMFunctor {

  /**
   * A custom function to validate whether both applicant signature and
   * authorized person signature entered.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check.
   *
   * @return A rule adaptor indicating whether the object is valid or not.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    try {
      final Adaptor didAplicntSignedAdaptor = params.get(0);
      final Adaptor didTPSignedBehalfOfAplcntAdaptor = params.get(1);

      if (null != didAplicntSignedAdaptor
        && null != didTPSignedBehalfOfAplcntAdaptor) {
        final String didAplicntSigned =
          ((StringAdaptor) didAplicntSignedAdaptor)
            .getStringValue(rulesParameters);

        final String didTPSignedBehalfOfAplcnt =
          ((StringAdaptor) didTPSignedBehalfOfAplcntAdaptor)
            .getStringValue(rulesParameters);

        if (didAplicntSigned.equals(IEGYESNO.YES)
          && didTPSignedBehalfOfAplcnt.equals(IEGYESNO.YES)) {
          // Either applicant or authorized person signature should be selected.
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
      return AdaptorFactory.getBooleanAdaptor(true);
    } catch (final Exception e) {
      return AdaptorFactory.getBooleanAdaptor(false);
    }
  }
}
