package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.entity.financial.fact.BDMCodeTableComboFactory;
import curam.ca.gc.bdm.entity.financial.struct.ReadSubOrdCodeByGovernTableCodeSubOrdTable;
import curam.ca.gc.bdm.entity.financial.struct.SubOrdCodeDetails;
import curam.ca.gc.bdm.entity.financial.struct.SubOrdCodeDetailsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.PRODUCTTYPE;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

public class CustomFunctionSetRFCDetails extends BDMFunctor {

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

      final Entity[] callBackDetailsEntityList = applicationEntity
        .getChildEntities(datastore.getEntityType("CallBackDetails"));

      if (callBackDetailsEntityList != null
        && callBackDetailsEntityList.length > 0) {
        final Entity callBackDetailsEntity = callBackDetailsEntityList[0];
        // check whether the benefit details already configured (for the
        // scenario that user navi back to step 1 and proceed)
        final Entity[] benefitDetailsEntityList = callBackDetailsEntity
          .getChildEntities(datastore.getEntityType("BenefitDetails"));
        if (benefitDetailsEntityList == null
          || benefitDetailsEntityList.length == 0) {
          // prepopulate the benefit list based on on selected program
          final ReadSubOrdCodeByGovernTableCodeSubOrdTable subOrdTableKey =
            new ReadSubOrdCodeByGovernTableCodeSubOrdTable();

          subOrdTableKey.governTableName = BDMBENEFITPROGRAMTYPE.TABLENAME;
          subOrdTableKey.governCode =
            callBackDetailsEntity.getAttribute("programType");
          subOrdTableKey.subOrdTableName = PRODUCTTYPE.TABLENAME;
          final SubOrdCodeDetailsList subOrdCodeDetailsList =
            BDMCodeTableComboFactory.newInstance()
              .readSubOrdCodesByGovernTableCodeSubOrdTable(subOrdTableKey);

          // this is one person entity on life event

          for (final SubOrdCodeDetails subOrdCodeDetails : subOrdCodeDetailsList.dtls) {

            final Entity benefitDetails =
              datastore.newEntity("BenefitDetails");
            callBackDetailsEntity.addChildEntity(benefitDetails);
            benefitDetails.setTypedAttribute("benefitType",
              subOrdCodeDetails.subOrdCode);
            benefitDetails.setTypedAttribute("benefitName",
              BDMUtil.getCodeTableDescriptionForUserLocale(
                PRODUCTTYPE.TABLENAME, subOrdCodeDetails.subOrdCode));
            benefitDetails.setTypedAttribute("benefitSelected", false);
            benefitDetails.update();
            callBackDetailsEntity.update();
          }
        }
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while prepopulating application details - "
        + e.getMessage());
      e.printStackTrace();
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

}
