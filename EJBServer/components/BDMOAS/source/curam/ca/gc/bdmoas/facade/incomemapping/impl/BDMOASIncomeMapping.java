package curam.ca.gc.bdmoas.facade.incomemapping.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASINCOMEBLOCKNUMBER;
import curam.ca.gc.bdmoas.entity.incomemapping.fact.BDMOASIncomeMappingFactory;
import curam.ca.gc.bdmoas.entity.incomemapping.fact.BDMOASIncomeMappingItemFactory;
import curam.ca.gc.bdmoas.entity.incomemapping.intf.BDMOASIncomeMappingItem;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingDtls;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingDtlsList;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingItemDtls;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingItemDtlsList;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingItemKey;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingKey;
import curam.ca.gc.bdmoas.facade.incomemapping.struct.BDMOASIncomeMappingConfigDetails;
import curam.ca.gc.bdmoas.facade.incomemapping.struct.BDMOASIncomeMappingDetails;
import curam.ca.gc.bdmoas.facade.incomemapping.struct.BDMOASIncomeMappingDetailsList;
import curam.ca.gc.bdmoas.facade.incomemapping.struct.BDMOASIncomeMappingFullDetails;
import curam.ca.gc.bdmoas.facade.incomemapping.struct.BDMOASIncomeMappingItemDetails;
import curam.core.fact.UsersFactory;
import curam.core.intf.Users;
import curam.core.struct.UsersKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;

public class BDMOASIncomeMapping
  extends curam.ca.gc.bdmoas.facade.incomemapping.base.BDMOASIncomeMapping {

  @Override
  public BDMOASIncomeMappingDetails
    createMapping(final BDMOASIncomeMappingDetails key)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void activateMapping(final BDMOASIncomeMappingKey key)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public void cancelMapping(final BDMOASIncomeMappingKey key)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public BDMOASIncomeMappingDetails
    editMapping(final BDMOASIncomeMappingDetails details)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BDMOASIncomeMappingDetails
    cloneMapping(final BDMOASIncomeMappingDetails details)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BDMOASIncomeMappingDetails
    getMappingDetails(final BDMOASIncomeMappingKey key)
      throws AppException, InformationalException {

    final BDMOASIncomeMappingDetails incomeMappingDetails =
      new BDMOASIncomeMappingDetails();
    final curam.ca.gc.bdmoas.entity.incomemapping.intf.BDMOASIncomeMapping incomeMappingObj =
      BDMOASIncomeMappingFactory.newInstance();
    final BDMOASIncomeMappingDtls incomeMappingDtls =
      incomeMappingObj.read(key);

    incomeMappingDetails.taxYear = incomeMappingDtls.taxYear;

    return incomeMappingDetails;
  }

  @Override
  public BDMOASIncomeMappingFullDetails
    getMappingFullDetails(final BDMOASIncomeMappingKey key)
      throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addMappingItem(final BDMOASIncomeMappingItemDetails details)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public void removeMappingItem(final BDMOASIncomeMappingItemKey key)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public BDMOASIncomeMappingDetailsList listMappings()
    throws AppException, InformationalException {

    final BDMOASIncomeMappingDetailsList incomeMappingDetailsList =
      new BDMOASIncomeMappingDetailsList();
    final curam.ca.gc.bdmoas.entity.incomemapping.intf.BDMOASIncomeMapping incomeMappingObj =
      BDMOASIncomeMappingFactory.newInstance();
    final Users usersObj = UsersFactory.newInstance();
    final UsersKey usersKey = new UsersKey();

    final BDMOASIncomeMappingDtlsList incomeMappingDtlsList =
      incomeMappingObj.readAll();

    for (final BDMOASIncomeMappingDtls incomeMappingDtls : incomeMappingDtlsList.dtls) {
      final BDMOASIncomeMappingDetails incomeMappingDetails =
        new BDMOASIncomeMappingDetails();

      incomeMappingDetails.mappingID = incomeMappingDtls.mappingID;
      incomeMappingDetails.taxYear = incomeMappingDtls.taxYear;
      incomeMappingDetails.status = incomeMappingDtls.status;
      incomeMappingDetails.createdOn = incomeMappingDtls.createdOn;
      incomeMappingDetails.lastUpdatedOn = incomeMappingDtls.lastUpdatedOn;

      if (!StringUtil.isNullOrEmpty(incomeMappingDtls.createdBy)) {
        usersKey.userName = incomeMappingDtls.createdBy;
        incomeMappingDetails.createdByFullName =
          usersObj.getFullName(usersKey).fullname;
      }
      if (!StringUtil.isNullOrEmpty(incomeMappingDtls.lastUpdatedBy)) {
        usersKey.userName = incomeMappingDtls.lastUpdatedBy;
        incomeMappingDetails.createdByFullName =
          usersObj.getFullName(usersKey).fullname;
      }

      incomeMappingDetailsList.dtls.add(incomeMappingDetails);
    }

    return incomeMappingDetailsList;
  }

  @Override
  public BDMOASIncomeMappingConfigDetails
    getMappingConfigDetails(final BDMOASIncomeMappingKey key)
      throws AppException, InformationalException {

    final BDMOASIncomeMappingConfigDetails incomeMappingConfigDetails =
      new BDMOASIncomeMappingConfigDetails();

    final BDMOASIncomeMappingItem incomeMappingItemObj =
      BDMOASIncomeMappingItemFactory.newInstance();
    final BDMOASIncomeMappingItemDtlsList allIncomeMappingItems =
      incomeMappingItemObj.searchByMapping(key);

    for (final BDMOASIncomeMappingItemDtls incomeMappingItem : allIncomeMappingItems.dtls) {

      if (StringUtil.isNullOrEmpty(incomeMappingItem.incomeBlock))
        continue;

      final BDMOASIncomeMappingItemDetails incomeMappingItemDetails =
        new BDMOASIncomeMappingItemDetails();
      incomeMappingItemDetails.lineItem = incomeMappingItem.lineItem;

      if (BDMOASINCOMEBLOCKNUMBER.CPP_QPP
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block1.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.OTHER_PENSION_INCOME
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block2.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.EI_WORKERS_COMP
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block3.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.INTEREST_INVESTMENTS
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block4.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.DIVIDENDS_CAPITAL_GAINS
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block5.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.RENTAL_INCOME
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block6.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.EMPLOYMENT_INCOME
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block7.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.SELF_EMPLOYMENT_INCOME
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block8.dtls.add(incomeMappingItemDetails);
      } else if (BDMOASINCOMEBLOCKNUMBER.OTHER_INCOME
        .equals(incomeMappingItem.incomeBlock)) {
        incomeMappingConfigDetails.block9.dtls.add(incomeMappingItemDetails);
      }
    }

    return incomeMappingConfigDetails;
  }

}
