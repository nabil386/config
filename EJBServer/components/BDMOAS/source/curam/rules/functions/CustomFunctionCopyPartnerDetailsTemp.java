package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * BDMOAS FEATURE 52093: Class Added
 * Custom function to store details temporary for displaying on the summary
 * screen.
 *
 * @author Nitish Singh
 *
 */
public class CustomFunctionCopyPartnerDetailsTemp extends BDMFunctor {

  /**
   * Custom function to pre populate the partner details.
   *
   * @param rulesParameters the rules parameters
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    // get the datastore root entity identifier from the rules parameters
    final IEG2Context ieg2context = (IEG2Context) rulesParameters;
    final long rootEntityID = ieg2context.getRootEntityID();

    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    final Datastore datastore = ieg2ExecutionContext.getDatastore();

    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity[] personEntities = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON));

    for (final Entity person : personEntities) {

      final Entity[] residentialAddressEntityList = person.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));

      if (residentialAddressEntityList.length > 0) {

        final Entity residentialAddressEntity =
          residentialAddressEntityList[0];

        final String address = buildAddress(residentialAddressEntity);
        person.setAttribute(BDMOASDatastoreConstants.kTempPartResAddress,
          address);
      }

      else {

        final Entity[] intResidentialAddressEntityList =
          person.getChildEntities(datastore
            .getEntityType(BDMDatastoreConstants.INTL_RESIDENTIAL_ADDRESS));

        for (final Entity residentialAddressEntity : intResidentialAddressEntityList) {

          final String address = buildAddress(residentialAddressEntity);
          person.setAttribute(BDMOASDatastoreConstants.kTempPartResAddress,
            address);
        }

      }

      final Entity[] mailingAddressEntityList = person.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));

      if (mailingAddressEntityList.length > 0) {

        final Entity mailingAddressEntity = mailingAddressEntityList[0];

        final String address = buildAddress(mailingAddressEntity);
        person.setAttribute(BDMOASDatastoreConstants.kTempPartMailAddress,
          address);
      }

      else {

        final Entity[] intMailingAddressEntityList =
          person.getChildEntities(datastore
            .getEntityType(BDMDatastoreConstants.INTL_MAILING_ADDRESS));

        for (final Entity mailingAddressEntity : intMailingAddressEntityList) {

          if (mailingAddressEntity
            .hasAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
            && !mailingAddressEntity
              .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY).equals("")
            && mailingAddressEntity.hasAttribute(BDMDatastoreConstants.CITY)
            && !mailingAddressEntity.getAttribute(BDMDatastoreConstants.CITY)
              .equals("")) {

            final String address = buildAddress(mailingAddressEntity);
            person.setAttribute(BDMOASDatastoreConstants.kTempPartMailAddress,
              address);

          }
        }
      }

      final Entity[] retirementIncomeEntityList =
        person.getChildEntities(datastore.getEntityType(
          BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_ENTITY));

      if (retirementIncomeEntityList != null
        && retirementIncomeEntityList.length > 0) {

        final Entity retirementIncomeEntity = retirementIncomeEntityList[0];
        person.setAttribute(
          BDMOASDatastoreConstants.kTempIsRetirdOrRetirng2Yrs,
          retirementIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIRED_ORRETIRING_ATTR));
        person.setAttribute(BDMOASDatastoreConstants.kTempRetirementDate,
          retirementIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIREDATE_ATTR));
        person.setAttribute(
          BDMOASDatastoreConstants.kTempIsPensionReduced2Yrs,
          retirementIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_REDUCED_ATTR));
        person.setAttribute(BDMOASDatastoreConstants.kTempReductionDate,
          retirementIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_DATE_ATTR));
      }

      final Entity[] foreignIncomeEntityList =
        person.getChildEntities(datastore
          .getEntityType(BDMOASDatastoreConstants.FOREIGN_INCOME_ENTITY));

      if (foreignIncomeEntityList != null
        && foreignIncomeEntityList.length > 0) {

        final Entity foreignIncomeEntity = foreignIncomeEntityList[0];
        person.setAttribute(
          BDMOASDatastoreConstants.kTempIsReceivingForeignIncome,
          foreignIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.FOREIGN_INCOME_ISRECEIVING_ATTR));
        person.setAttribute(BDMOASDatastoreConstants.kTempForeignIncomeType,
          foreignIncomeEntity
            .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_TYPE_ATTR));
        person.setAttribute(
          BDMOASDatastoreConstants.kTempAnnualForeignIncomeStr,
          foreignIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.FOREIGN_INCOME_ANNUAL_ATTR));
      }

      final Entity[] sponsorshipInfoEntity = person.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.SPONSORSHIPINFO_ENTITY));

      if (sponsorshipInfoEntity.length > 0
        && sponsorshipInfoEntity[0] != null) {

        person.setAttribute(
          BDMOASDatastoreConstants.kTempIsSpouseOrCLPartnrSponsored,
          sponsorshipInfoEntity[0].getAttribute(
            BDMOASDatastoreConstants.IS_SPOUSE_OR_CLP_SPONSORED_ATTR));
      }

      person.update();
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  private String buildAddress(final Entity addressEntity) {

    if (addressEntity.hasAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
      && !addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
        .equals("")
      && addressEntity.hasAttribute(BDMDatastoreConstants.CITY)
      && !addressEntity.getAttribute(BDMDatastoreConstants.CITY).equals("")) {

      String tempPartSuiteNum = "";
      String tempPartAddressLine1 = "";
      String tempPartAddressLine2 = "";
      String tempPartAddressLine3 = "";
      String tempPartAddressLine4 = "";
      String tempPartCity = "";
      String tempPartProvince = "";
      String tempPartState = "";
      String tempPartCountry = "";
      String tempPartZipCode = "";
      String tempPartPostalCode = "";

      if (addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
        .equals(BDMOASDatastoreConstants.kCA)) {

        tempPartPostalCode =
          addressEntity.getAttribute(BDMDatastoreConstants.POSTAL_CODE);
        tempPartProvince =
          addressEntity.getAttribute(BDMDatastoreConstants.PROVINCE);
      }

      else if (addressEntity
        .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
        .equals(BDMOASDatastoreConstants.kUS)) {

        tempPartAddressLine3 =
          addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE3);
        tempPartState =
          addressEntity.getAttribute(BDMDatastoreConstants.STATE);
        tempPartZipCode =
          addressEntity.getAttribute(BDMDatastoreConstants.ZIP_CODE);
      }

      else {
        tempPartAddressLine4 =
          addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE4);
        tempPartAddressLine3 =
          addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE3);
        tempPartState =
          addressEntity.getAttribute(BDMDatastoreConstants.STATE);
        tempPartZipCode =
          addressEntity.getAttribute(BDMDatastoreConstants.ZIP_CODE);
      }

      tempPartSuiteNum =
        addressEntity.getAttribute(BDMDatastoreConstants.SUITE_NUM);
      tempPartAddressLine1 =
        addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE1);
      tempPartAddressLine2 =
        addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE2);
      tempPartCity = addressEntity.getAttribute(BDMDatastoreConstants.CITY);
      tempPartCountry =
        addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);

      final String tempPartnerAddress = tempPartSuiteNum + " "
        + tempPartAddressLine1 + " " + tempPartAddressLine2 + " "
        + tempPartAddressLine3 + " " + tempPartAddressLine4 + " "
        + tempPartCity + " " + tempPartProvince + " " + tempPartState + " "
        + tempPartCountry + " " + tempPartZipCode + " " + tempPartPostalCode;

      return tempPartnerAddress;
    }
    return null;
  }

}
