package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.codetable.COUNTRYCODE;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * BDMOAS FEATURE 52093: Class Added
 * Custom function to set indicators to add or update address.
 *
 * @author SMisal
 *
 */
public class CustomFunctionSetAddOrUpdateAddressIndicatorsForPartner
  extends BDMFunctor {

  public CustomFunctionSetAddOrUpdateAddressIndicatorsForPartner() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Custom function to pre-populate the partner details.
   *
   * @param rulesParameters the rules parameters
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    // Get All parameters

    final IEG2Context ieg2context = (IEG2Context) rulesParameters;
    final long rootEntityID = ieg2context.getRootEntityID();

    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    final Datastore datastore = ieg2ExecutionContext.getDatastore();

    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity personEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON),
      "isPrimaryParticipant==false")[0];

    final Entity resiSrchCritEntity = personEntity.getChildEntities(datastore
      .getEntityType(BDMOASDatastoreConstants.kSearchCriteriaResi))[0];

    final String country =
      resiSrchCritEntity.getAttribute(BDMOASDatastoreConstants.kResidCountry);

    final String postalCode = resiSrchCritEntity
      .getAttribute(BDMOASDatastoreConstants.kResidPostalCode);

    if (StringUtil.isNullOrEmpty(postalCode)
      && COUNTRYCODE.CACODE.equalsIgnoreCase(country)) {
      // It means it is an existing address and no is selected for updating
      // address.
      personEntity.setTypedAttribute(
        BDMOASDatastoreConstants.kAddOrUpdateResidAddress, false);
      personEntity.update();

      resiSrchCritEntity.setTypedAttribute(
        BDMOASDatastoreConstants.kCaResiAddrSearchInd, false);

      resiSrchCritEntity.update();
    } else if (!StringUtil.isNullOrEmpty(postalCode)
      && COUNTRYCODE.CACODE.equalsIgnoreCase(country)) {
      // It means user would like to update the address or would like to add the
      // new address.
      personEntity.setTypedAttribute(
        BDMOASDatastoreConstants.kAddOrUpdateResidAddress, true);

      personEntity.setTypedAttribute(
        BDMDatastoreConstants.RESIDENTIAL_COUNTRY, country);

      personEntity.update();

      resiSrchCritEntity.setTypedAttribute(
        BDMOASDatastoreConstants.kCaResiAddrSearchInd, true);

      resiSrchCritEntity.update();
    } else if (StringUtil.isNullOrEmpty(postalCode)
      && !COUNTRYCODE.CACODE.equalsIgnoreCase(country)) {
      final String resCountry =
        personEntity.getAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY);
      if (!StringUtil.isNullOrEmpty(resCountry) && !resCountry.equals(country)
        || StringUtil.isNullOrEmpty(resCountry)
        || resCountry.equals(country)) {
        personEntity.setTypedAttribute(
          BDMOASDatastoreConstants.kAddOrUpdateResidAddress, true);

        personEntity.setTypedAttribute(
          BDMDatastoreConstants.RESIDENTIAL_COUNTRY, country);

        personEntity.update();

        resiSrchCritEntity.setTypedAttribute(
          BDMOASDatastoreConstants.kCaResiAddrSearchInd, false);

        resiSrchCritEntity.update();
      }
    }

    return AdaptorFactory.getBooleanAdaptor(true);

  }

}
