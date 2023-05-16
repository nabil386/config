package curam.ca.gc.bdm.entity.bdmexternalliability.impl;

import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.fact.BDMExternalLiabilityHistoryFactory;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.struct.BDMExternalLiabilityHistoryDtls;
import curam.core.fact.UniqueIDFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.DateTime;

public class BDMExternalLiability extends
  curam.ca.gc.bdm.entity.bdmexternalliability.base.BDMExternalLiability {

  @Override
  protected void premodify(final BDMExternalLiabilityKey key,
    final BDMExternalLiabilityDtls details)
    throws AppException, InformationalException {

    final BDMExternalLiabilityDtls originalDtls =
      BDMExternalLiabilityFactory.newInstance().read(key);

    // if the original amount has not changed but the outstanding amount has,
    // then this does not need to be logged as history as the outstanding amount
    // has not changed
    if (originalDtls.liabilityAmount.equals(details.liabilityAmount)) {
      return;
    }

    // insert history record
    final BDMExternalLiabilityHistoryDtls historyDtls =
      new BDMExternalLiabilityHistoryDtls();

    historyDtls.bdmExternalLiabilityHistoryID =
      UniqueIDFactory.newInstance().getNextID();
    historyDtls.externalLiabilityID = originalDtls.externalLiabilityID;
    historyDtls.liabilityAmount = details.liabilityAmount;
    historyDtls.originalLiabilityAmount = originalDtls.liabilityAmount;
    historyDtls.originalOutstandingAmount = originalDtls.outstandingAmount;
    historyDtls.comments = details.comments;
    historyDtls.creationDateTime = DateTime.getCurrentDateTime();
    historyDtls.userName = TransactionInfo.getProgramUser();
    BDMExternalLiabilityHistoryFactory.newInstance().insert(historyDtls);

  }

}
