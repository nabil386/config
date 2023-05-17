package curam.rules.functions;

import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import curam.util.transaction.TransactionInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Area code is mandatory when country code is +1.
 *
 */
public class CustomFunctionValidateAreaCodeMandatory extends CustomFunctor {

  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    final java.util.List<Adaptor> params = getParameters();

    if (null == params) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Adaptor countryCodeAdaptor = params.get(0);
    final Adaptor phoneAreaCodeAdaptor = params.get(1);

    if (null != countryCodeAdaptor) {
      final String countryCode =
        ((StringAdaptor) countryCodeAdaptor).getStringValue(rulesParameters);

      final CodeTableAdmin codeTableAdminObj =
        CodeTableAdminFactory.newInstance();
      final CodeTableItemUniqueKey codeTableItemUniqueKey =
        new CodeTableItemUniqueKey();

      codeTableItemUniqueKey.code = countryCode;
      codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
      codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

      final Boolean isPlusOneCountry = countryCode.isEmpty() ? false
        : codeTableAdminObj.readCTIDetailsForLocaleOrLanguage(
          codeTableItemUniqueKey).annotation.trim()
            .equals(BDMConstants.kphonePrefix);

      // validation: if country code is +1, then area code is mandatory
      if (isPlusOneCountry) {
        if (phoneAreaCodeAdaptor == null)
          return AdaptorFactory.getBooleanAdaptor(false);
        else {
          final String phoneAreaCode = ((StringAdaptor) phoneAreaCodeAdaptor)
            .getStringValue(rulesParameters);
          if (phoneAreaCode.isEmpty()) {
            return AdaptorFactory.getBooleanAdaptor(false);
          }
        }
      }
    }
    return AdaptorFactory.getBooleanAdaptor(true);
  }

  /**
   * Util Method to validate if given string is a number
   *
   * @since
   * @param phoneNumber
   * @return
   */
  private boolean isNumeric(final String phoneNumber) {

    if (phoneNumber.isEmpty()) {

      return true;

    }
    // regex to check for Numeric Values
    final Pattern pattern = Pattern.compile("^[0-9]*$");
    final Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

}
