/**
 *
 */
package curam.ca.gc.bdmoas.facade.caseadmin.impl;

import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASCaseReassignReasonAndCommentsDetails;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASCreateCaseOwnerDetails;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASListAdminCaseRoleDetails;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASReadAdminCaseRoleDetails;
import curam.ca.gc.bdmoas.sl.caseadmin.fact.BDMOASMaintainCaseFactory;
import curam.core.facade.struct.ListAdminCaseRoleKey;
import curam.core.facade.struct.ReadAdminCaseRoleKey;
import curam.core.sl.entity.struct.CaseUserRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Supports office of control case work queue assignment.
 *
 * @author abid.a.khan
 *
 */
public class BDMOASCase
  extends curam.ca.gc.bdmoas.facade.caseadmin.base.BDMOASCase {

  @Override
  public void createCaseOwner(final BDMOASCreateCaseOwnerDetails details)
    throws AppException, InformationalException {

    BDMOASMaintainCaseFactory.newInstance().createCaseOwner(details);

  }

  @Override
  public BDMOASListAdminCaseRoleDetails
    listAdminCaseRole(final ListAdminCaseRoleKey key)
      throws AppException, InformationalException {

    return BDMOASMaintainCaseFactory.newInstance().listAdminCaseRole(key);
  }

  @Override
  public void modifyCaseOwnerReasonAndComments(final CaseUserRoleKey key,
    final BDMOASCaseReassignReasonAndCommentsDetails details)
    throws AppException, InformationalException {

    BDMOASMaintainCaseFactory.newInstance()
      .modifyCaseOwnerReasonAndComments(key, details);

  }

  @Override
  public BDMOASReadAdminCaseRoleDetails
    readAdminCaseRole(final ReadAdminCaseRoleKey key)
      throws AppException, InformationalException {

    return BDMOASMaintainCaseFactory.newInstance().readAdminCaseRole(key);
  }

}
