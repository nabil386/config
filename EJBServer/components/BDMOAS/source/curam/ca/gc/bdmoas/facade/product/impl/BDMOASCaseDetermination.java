package curam.ca.gc.bdmoas.facade.product.impl;

import curam.core.facade.infrastructure.assessment.fact.CaseDeterminationFactory;
import curam.core.facade.infrastructure.assessment.intf.CaseDetermination;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionDetailsList;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionListDetails;
import curam.core.facade.infrastructure.assessment.struct.CaseIDDeterminationIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMOASCaseDetermination
  extends curam.ca.gc.bdmoas.facade.product.base.BDMOASCaseDetermination {

  @Override
  public CaseDeterminationDecisionDetailsList
    listDecisionPeriodsForDetermination(final CaseIDDeterminationIDKey key)
      throws AppException, InformationalException {

    final CaseDeterminationDecisionDetailsList filteredList =
      new CaseDeterminationDecisionDetailsList();

    final CaseDetermination caseDetermination =
      CaseDeterminationFactory.newInstance();

    final CaseDeterminationDecisionDetailsList currentList =
      caseDetermination.listDecisionPeriodsForDetermination(key);

    filteredList.assign(currentList);
    filteredList.dtls.clear();

    for (final CaseDeterminationDecisionListDetails decisionDetails : currentList.dtls) {

      // TODO: Add back filter logic once display rules are merged.
      if (Date.getCurrentDate()
        .compareTo(decisionDetails.decisionFromDate) >= 0) {

        filteredList.dtls.addRef(decisionDetails);

      }

    }

    return filteredList;

  }

}
