package curam.rules.functions;

import curam.codetable.IEGYESNO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function to validate spouse or authorized person
 * declaration signature selection business rule.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateSpouseOrWitnessSigndDeclrn
  extends BDMFunctor {

  /**
   * A custom function to validate whether both spouse signature and
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
      final Adaptor didSpouseOrCLPSignedAdaptor = params.get(0);
      final Adaptor didTPSignedBehalfOfSpouseAdaptor = params.get(1);

      if (null != didSpouseOrCLPSignedAdaptor
        && null != didTPSignedBehalfOfSpouseAdaptor) {
        final String didSpouseOrCLPSigned =
          ((StringAdaptor) didSpouseOrCLPSignedAdaptor)
            .getStringValue(rulesParameters);

        final String didTPSignedBehalfOfSpouse =
          ((StringAdaptor) didTPSignedBehalfOfSpouseAdaptor)
            .getStringValue(rulesParameters);

        if (didSpouseOrCLPSigned.equals(IEGYESNO.YES)
          && didTPSignedBehalfOfSpouse.equals(IEGYESNO.YES)) {
          // Either applicant's spouse/common-law partner or Authorized person
          // signature should be selected.
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
      return AdaptorFactory.getBooleanAdaptor(true);
    } catch (final Exception e) {
      return AdaptorFactory.getBooleanAdaptor(false);
    }
  }

}
