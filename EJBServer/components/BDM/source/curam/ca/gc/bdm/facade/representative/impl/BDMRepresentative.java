package curam.ca.gc.bdm.facade.representative.impl;

import curam.ca.gc.bdm.facade.representative.struct.BDMRepresentativeHomePageDetails;
import curam.ca.gc.bdm.facade.representative.struct.BDMRepresentativeSummaryDetails;
import curam.core.facade.fact.RepresentativeFactory;
import curam.core.facade.struct.InformationalMsgDetailsList;
import curam.core.facade.struct.RepresentativeKey;
import curam.core.fact.ConcernRoleFactory;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Task 98009 - DEV: Case Management Code Changes For Preferred Language 
 * @author prashant.raut
 *
 */
public class BDMRepresentative  extends curam.ca.gc.bdm.facade.representative.base.BDMRepresentative{
  
  final curam.core.intf.ConcernRole concernRoleObj =
    curam.core.fact.ConcernRoleFactory.newInstance();

  /**
   * This method will modify representative.
   */
  @Override
  public InformationalMsgDetailsList modifyRepresentative(
    BDMRepresentativeSummaryDetails representativeSummaryDetails)
    throws AppException, InformationalException {
    
    InformationalMsgDetailsList msgList = RepresentativeFactory.newInstance().modifyRepresentative(representativeSummaryDetails.dtls);
    
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = representativeSummaryDetails.dtls.representativeSummaryDetails.concernRoleID;
    ConcernRoleDtls concernRoleDtls;
    concernRoleDtls = concernRoleObj.read(concernRoleKey);
    // update preferred language.
    concernRoleDtls.preferredLanguage=representativeSummaryDetails.preferredLanguage;
    ConcernRoleFactory.newInstance().modify(concernRoleKey, concernRoleDtls);

    return msgList;
  }

  /**
   * This method will view representative.
   */
  @Override
  public BDMRepresentativeHomePageDetails
    viewRepresentativeHomePage(RepresentativeKey representativeKey)
      throws AppException, InformationalException {

    BDMRepresentativeHomePageDetails bdmHomePageDetails = new BDMRepresentativeHomePageDetails();
    bdmHomePageDetails.dtls= RepresentativeFactory.newInstance().viewRepresentativeHomePage(representativeKey);
    final curam.core.intf.ConcernRole concernRoleObj =
      curam.core.fact.ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = representativeKey.representativeKey.representativeID;
    ConcernRoleDtls concernRoleDtls;
    concernRoleDtls = concernRoleObj.read(concernRoleKey);
    bdmHomePageDetails.preferredLanguage = concernRoleDtls.preferredLanguage;
    return bdmHomePageDetails;
  }

}
