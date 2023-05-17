package curam.rules.functions;

import curam.codetable.IEGYESNO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Contains a custom function to validate witness declaration signature
 * business rule.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateWitnessSigndDeclrn extends BDMFunctor {

  private static final String kZipCodePattern = "^[0-9]+-?([0-9])*$";

  /**
   * A custom function to validate authorized person declaration signature.
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
      final Adaptor didAplicntSignedWithMarkAdaptor = params.get(0);
      final Adaptor didSpouseOrCLPSignedWithMarkAdaptor = params.get(1);
      final Adaptor didTPSignedAsWitnessAdaptor = params.get(2);

      if (null != didTPSignedAsWitnessAdaptor) {

        final String didTPSignedAsWitness =
          ((StringAdaptor) didTPSignedAsWitnessAdaptor)
            .getStringValue(rulesParameters);

        String didSpouseOrCLPSignedWithMark = IEGYESNO.NO;

        if (null != didSpouseOrCLPSignedWithMarkAdaptor) {
          didSpouseOrCLPSignedWithMark =
            ((StringAdaptor) didSpouseOrCLPSignedWithMarkAdaptor)
              .getStringValue(rulesParameters);
        }

        String didAplicntSignedWithMark = IEGYESNO.NO;

        if (null != didAplicntSignedWithMarkAdaptor) {
          didAplicntSignedWithMark =
            ((StringAdaptor) didAplicntSignedWithMarkAdaptor)
              .getStringValue(rulesParameters);
        }

        if (didTPSignedAsWitness.equals(IEGYESNO.YES)
          && didAplicntSignedWithMark.equals(IEGYESNO.NO)
          && didSpouseOrCLPSignedWithMark.equals(IEGYESNO.NO)) {

          // Can only be selected if Yes is selected for either or both ''Did
          // the applicant sign the declaration with a mark?' and 'Did the
          // applicant's spouse/common-law partner sign the declaration with a
          // mark?
          return AdaptorFactory.getBooleanAdaptor(false);
        }
      }
      return AdaptorFactory.getBooleanAdaptor(true);
    } catch (final Exception e) {
      return AdaptorFactory.getBooleanAdaptor(false);
    }
  }

}
