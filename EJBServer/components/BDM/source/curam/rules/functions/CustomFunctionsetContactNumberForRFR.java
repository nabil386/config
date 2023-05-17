package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.PHONETYPE;
import curam.core.impl.CuramConst;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.transaction.TransactionInfo;

/**
 * Custom function to set full phone number.
 *
 * @since ADO-23496
 *
 */
public class CustomFunctionsetContactNumberForRFR extends BDMFunctor {

  public CustomFunctionsetContactNumberForRFR() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Custom function to pre populate the application details.
   *
   * @param rulesParameters the rules parameters
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    try {

      // get the datastore root entity identifier from the rules parameters
      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      final long rootEntityID = ieg2context.getRootEntityID();

      final IEG2ExecutionContext ieg2ExecutionContext =
        new IEG2ExecutionContext(rulesParameters);

      final Datastore datastore = ieg2ExecutionContext.getDatastore();

      final Entity applicationEntity = datastore.readEntity(rootEntityID);

      // BEGIN : assemble full phone number
      final Entity[] rfrDetailsEntityList = applicationEntity
        .getChildEntities(datastore.getEntityType("RFRDetails"));

      if (rfrDetailsEntityList != null && rfrDetailsEntityList.length > 0) {
        final Entity rfrDetailsEntity = rfrDetailsEntityList[0];
        final String countryCode =
          rfrDetailsEntity.getAttribute("countryCode");
        final CodeTableAdmin codeTableAdminObj =
          CodeTableAdminFactory.newInstance();
        final CodeTableItemUniqueKey codeTableItemUniqueKey =
          new CodeTableItemUniqueKey();

        codeTableItemUniqueKey.code = countryCode;
        codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
        codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

        final String phoneAreaCode =
          rfrDetailsEntity.getAttribute("phoneAreaCode");
        final String phoneNumber =
          rfrDetailsEntity.getAttribute("phoneNumber");
        final String phoneAreaCodeAndNumber = phoneAreaCode + phoneNumber;

        final String phoneExt = rfrDetailsEntity.getAttribute("phoneExt");
        final String phoneType = BDMUtil.getCodeTableDescriptionForUserLocale(
          PHONETYPE.TABLENAME, rfrDetailsEntity.getAttribute("phoneType"));
        final StringBuffer fullPhoneNumber = new StringBuffer();
        fullPhoneNumber
          .append(codeTableAdminObj.readCTIDetailsForLocaleOrLanguage(
            codeTableItemUniqueKey).annotation.trim())
          .append(BDMConstants.kStringSpace)
          .append(phoneAreaCodeAndNumber
            .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"))
          .append(BDMConstants.kStringSpace);
        if (!phoneExt.isEmpty()) {
          fullPhoneNumber.append("ext").append(phoneExt)
            .append(BDMConstants.kStringSpace);
        }
        fullPhoneNumber.append(CuramConst.gkRoundOpeningBracket)
          .append(phoneType).append(CuramConst.gkRoundClosingBracket);
        rfrDetailsEntityList[0].setAttribute("fullPhoneNumber",
          fullPhoneNumber.toString());
        rfrDetailsEntityList[0].update();
      }
      // END : assemble full phone number

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while prepopulating application details - "
        + e.getMessage());
      e.printStackTrace();
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

}
