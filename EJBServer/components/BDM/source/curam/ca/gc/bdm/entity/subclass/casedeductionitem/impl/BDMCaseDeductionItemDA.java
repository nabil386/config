package curam.ca.gc.bdm.entity.subclass.casedeductionitem.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.PriorityVersionNo;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMCaseDeductionItemDA extends
  curam.ca.gc.bdm.entity.subclass.casedeductionitem.base.BDMCaseDeductionItemDA {

  @Inject
  MaintainDeductionDetails maintainDeductionDetailsObj;

  public BDMCaseDeductionItemDA() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  protected void autovalidate(final CaseDeductionItemDtls details)
    throws AppException, InformationalException {

    super.autovalidate(details);
  }

  @Override
  protected void preinsert(final CaseDeductionItemDtls details)
    throws AppException, InformationalException {

    super.preinsert(details);

    details.priority = checkPriorityRange(details.caseID,
      details.deductionName, details.priority);

  }

  /**
   * Adds an entry into BDMCaseDeductionItem
   */
  @Override
  protected void postinsert(final CaseDeductionItemDtls details)
    throws AppException, InformationalException {

    final BDMCaseDeductionItemDtls bdmCDIDtls =
      new BDMCaseDeductionItemDtls();
    bdmCDIDtls.caseDeductionItemID = details.caseDeductionItemID;
    BDMCaseDeductionItemFactory.newInstance().insert(bdmCDIDtls);

  }

  @Override
  protected void premodifyPriority(final CaseDeductionItemKey key,
    final PriorityVersionNo details)
    throws AppException, InformationalException {

    final CaseDeductionItemDtls dtls =
      CaseDeductionItemFactory.newInstance().read(key);
    details.priority =
      checkPriorityRange(dtls.caseID, dtls.deductionName, details.priority);

  }

  @Override
  protected void premodify(final CaseDeductionItemKey key,
    final CaseDeductionItemDtls details)
    throws AppException, InformationalException {

    super.premodify(key, details);
    details.priority = checkPriorityRange(details.caseID,
      details.deductionName, details.priority);
  }

  /**
   * Checks that the priority is not exceeding the provided range for a certain
   * deduction type. If it is, reset it to the max deduction priority value
   * allocated for that specific deduction type
   *
   * @param caseID
   * @param deductionName
   * @param priority
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private int checkPriorityRange(final long caseID,
    final String deductionName, int priority)
    throws AppException, InformationalException {

    final int basePriority =
      maintainDeductionDetailsObj.getBasePriority(caseID, deductionName);
    if (priority >= basePriority + BDMConstants.kPriorityMultiplier) {
      priority = basePriority + BDMConstants.kPriorityMultiplier - 1;
    }

    return priority;
  }

}
