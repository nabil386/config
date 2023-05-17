package curam.ca.gc.bdm.util.deduction.impl;

import curam.codetable.CASENOMINEESTATUS;
import curam.core.sl.fact.CaseNomineeFactory;
import curam.core.sl.struct.CaseNomineeAndStatusDetails;
import curam.core.sl.struct.CaseNomineeAndStatusDetailsList;
import curam.core.sl.struct.CaseNomineeCaseIDAndStatusKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.HashMap;
import java.util.Map;

public class BDMDeductionUtil {

  /**
   * Given an underpayment case, gets a mapping of nominees to concern roles
   *
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static Map<Long, Long>
    getUnderpaymentCaseNominees(final long underpaymentCaseID)
      throws AppException, InformationalException {

    final CaseNomineeCaseIDAndStatusKey caseNomineeKey =
      new CaseNomineeCaseIDAndStatusKey();
    caseNomineeKey.caseNomineeCaseIDAndStatusKey.caseID = underpaymentCaseID;
    caseNomineeKey.caseNomineeCaseIDAndStatusKey.nomineeStatus =
      CASENOMINEESTATUS.OPERATIONAL;
    final CaseNomineeAndStatusDetailsList nomineeList =
      CaseNomineeFactory.newInstance().listOperationalNominee(caseNomineeKey);

    final Map<Long, Long> concernRoleCaseNomineeMap =
      new HashMap<Long, Long>();

    for (final CaseNomineeAndStatusDetails nominee : nomineeList.caseNomineeAndStatusDetailsList) {
      concernRoleCaseNomineeMap.put(nominee.concernRoleID,
        nominee.caseNomineeID);
    }

    return concernRoleCaseNomineeMap;
  }
}
