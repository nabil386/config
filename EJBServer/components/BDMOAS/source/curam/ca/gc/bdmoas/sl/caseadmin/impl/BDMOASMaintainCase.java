/**
 *
 */
package curam.ca.gc.bdmoas.sl.caseadmin.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdmoas.entity.caseuserrole.fact.BDMOASCaseUserRoleFactory;
import curam.ca.gc.bdmoas.entity.caseuserrole.intf.BDMOASCaseUserRole;
import curam.ca.gc.bdmoas.entity.caseuserrole.struct.BDMOASCaseUserRoleDtls;
import curam.ca.gc.bdmoas.entity.caseuserrole.struct.BDMOASCaseUserRoleKey;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASCaseReassignReasonAndCommentsDetails;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASCaseUserRoleList;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASCreateCaseOwnerDetails;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASListAdminCaseRoleDetails;
import curam.ca.gc.bdmoas.facade.caseadmin.struct.BDMOASReadAdminCaseRoleDetails;
import curam.core.facade.fact.CaseFactory;
import curam.core.facade.struct.CreateCaseOwnerDetails;
import curam.core.facade.struct.ListAdminCaseRoleDetails;
import curam.core.facade.struct.ListAdminCaseRoleKey;
import curam.core.facade.struct.ReadAdminCaseRoleDetails;
import curam.core.facade.struct.ReadAdminCaseRoleKey;
import curam.core.sl.entity.struct.CaseUserRoleKey;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseUserRole;
import curam.piwrapper.casemanager.impl.CaseUserRoleDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.NotFoundIndicator;
import java.util.List;

/**
 * @author abid.a.khan
 *
 */
public class BDMOASMaintainCase
  extends curam.ca.gc.bdmoas.sl.caseadmin.base.BDMOASMaintainCase {

  @Inject
  CaseUserRoleDAO caseUserRoleDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  public BDMOASMaintainCase() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void createCaseOwner(final BDMOASCreateCaseOwnerDetails details)
    throws AppException, InformationalException {

    final CreateCaseOwnerDetails createCaseOwnerDetails =
      new CreateCaseOwnerDetails();
    createCaseOwnerDetails.assign(details.dtls);

    CaseFactory.newInstance().createCaseOwner(createCaseOwnerDetails);

    final CaseHeader caseHeader =
      caseHeaderDAO.get(createCaseOwnerDetails.caseHeaderKey.caseID);
    final List<CaseUserRole> listActiveByCase =
      caseUserRoleDAO.listActiveByCase(caseHeader);

    final BDMOASCaseUserRoleDtls bdmOASCaseUserRoleDetails =
      new BDMOASCaseUserRoleDtls();

    // Assumption: only 1 active case user role exists at a time.
    bdmOASCaseUserRoleDetails.caseUserRoleID =
      listActiveByCase.isEmpty() ? 0 : listActiveByCase.get(0).getID();

    if (bdmOASCaseUserRoleDetails.caseUserRoleID != 0) {
      bdmOASCaseUserRoleDetails.isManuallyChanged = details.isManuallyChanged;
      BDMOASCaseUserRoleFactory.newInstance()
        .insert(bdmOASCaseUserRoleDetails);
    }

  }

  @Override
  public BDMOASListAdminCaseRoleDetails
    listAdminCaseRole(final ListAdminCaseRoleKey key)
      throws AppException, InformationalException {

    final BDMOASListAdminCaseRoleDetails listDetails =
      new BDMOASListAdminCaseRoleDetails();

    final ListAdminCaseRoleDetails listAdminCaseRole =
      CaseFactory.newInstance().listAdminCaseRole(key);
    listDetails.dtls = listAdminCaseRole;

    final CaseHeader caseHeader =
      caseHeaderDAO.get(key.maintainAdminCaseRolesKey.caseID);

    final List<CaseUserRole> listByCase =
      caseUserRoleDAO.listByCase(caseHeader);

    final BDMOASCaseUserRoleList caseUserDtlsList =
      new BDMOASCaseUserRoleList();
    final BDMOASCaseUserRole bdmOASCaseUserRoleObj =
      BDMOASCaseUserRoleFactory.newInstance();
    BDMOASCaseUserRoleDtls bdmOASCaseUserRoleDtls;
    BDMOASCaseUserRoleKey bdmOASKey;

    NotFoundIndicator caseUserRoleIndicator;

    for (final CaseUserRole caseUserRole : listByCase) {
      caseUserRoleIndicator = new NotFoundIndicator();
      bdmOASKey = new BDMOASCaseUserRoleKey();
      bdmOASKey.caseUserRoleID = caseUserRole.getID();

      bdmOASCaseUserRoleDtls =
        bdmOASCaseUserRoleObj.read(caseUserRoleIndicator, bdmOASKey);

      if (!caseUserRoleIndicator.isNotFound())
        caseUserDtlsList.dtls.add(bdmOASCaseUserRoleDtls);
    }

    listDetails.caseUserDtls = caseUserDtlsList;

    return listDetails;
  }

  @Override
  public void modifyCaseOwnerReasonAndComments(final CaseUserRoleKey key,
    final BDMOASCaseReassignReasonAndCommentsDetails details)
    throws AppException, InformationalException {

    CaseFactory.newInstance().modifyCaseOwnerReasonAndComments(key,
      details.dtls);

    final BDMOASCaseUserRole bdmOASCaseUserObj =
      BDMOASCaseUserRoleFactory.newInstance();

    final BDMOASCaseUserRoleKey caseUserRoleKey = new BDMOASCaseUserRoleKey();
    caseUserRoleKey.caseUserRoleID = key.caseUserRoleID;

    // read dtls
    final NotFoundIndicator caseUserRoleIndicator = new NotFoundIndicator();

    final BDMOASCaseUserRoleDtls bdmOASCaseUserDtls =
      bdmOASCaseUserObj.read(caseUserRoleIndicator, caseUserRoleKey);

    if (!caseUserRoleIndicator.isNotFound()) {
      bdmOASCaseUserDtls.isManuallyChanged = details.isManuallyChanged;
      bdmOASCaseUserObj.modify(caseUserRoleKey, bdmOASCaseUserDtls);
    } else {

      final BDMOASCaseUserRoleDtls bdmOASCaseUserRoleDetails =
        new BDMOASCaseUserRoleDtls();
      bdmOASCaseUserRoleDetails.caseUserRoleID = key.caseUserRoleID;
      bdmOASCaseUserRoleDetails.isManuallyChanged = details.isManuallyChanged;
      bdmOASCaseUserObj.insert(bdmOASCaseUserRoleDetails);

    }

  }

  @Override
  public BDMOASReadAdminCaseRoleDetails
    readAdminCaseRole(final ReadAdminCaseRoleKey key)
      throws AppException, InformationalException {

    final ReadAdminCaseRoleDetails readAdminCaseRole =
      CaseFactory.newInstance().readAdminCaseRole(key);

    final BDMOASReadAdminCaseRoleDetails readAdminCaseRoleDetails =
      new BDMOASReadAdminCaseRoleDetails();
    readAdminCaseRoleDetails.dtls = readAdminCaseRole;

    final BDMOASCaseUserRole bdmOASCaseUserObj =
      BDMOASCaseUserRoleFactory.newInstance();
    final BDMOASCaseUserRoleKey caseUserRoleKey = new BDMOASCaseUserRoleKey();
    caseUserRoleKey.caseUserRoleID =
      key.maintainAdminCaseRoleIDKey.administrationCaseRoleID;

    // read dtls
    final NotFoundIndicator caseUserRoleIndicator = new NotFoundIndicator();

    final BDMOASCaseUserRoleDtls bdmOASCaseUserDtls =
      bdmOASCaseUserObj.read(caseUserRoleIndicator, caseUserRoleKey);

    if (!caseUserRoleIndicator.isNotFound())
      readAdminCaseRoleDetails.isManuallyChanged =
        bdmOASCaseUserDtls.isManuallyChanged;

    return readAdminCaseRoleDetails;
  }

}
