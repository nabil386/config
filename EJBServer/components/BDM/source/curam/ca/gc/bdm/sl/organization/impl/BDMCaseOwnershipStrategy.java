package curam.ca.gc.bdm.sl.organization.impl;

import curam.codetable.ORGOBJECTTYPE;
import curam.core.fact.CachedCaseHeaderFactory;
import curam.core.intf.CachedCaseHeader;
import curam.core.sl.entity.struct.CaseUserRoleDtls;
import curam.core.sl.entity.struct.OrgObjectLinkDtls;
import curam.core.sl.entity.struct.OrgObjectLinkKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.ReadWorkQueueDetails;
import curam.core.sl.struct.ReadWorkQueueKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;

public class BDMCaseOwnershipStrategy
  extends curam.ca.gc.bdm.sl.organization.base.BDMCaseOwnershipStrategy {

  @Override
  public void assignCase(final CaseIDKey chkey)
    throws AppException, InformationalException {

    final ReadWorkQueueKey readWorkQueueKey = new ReadWorkQueueKey();
    final OrgObjectLinkDtls orgObjectLinkDtls = new OrgObjectLinkDtls();
    final CaseUserRoleDtls caseUserRoleDtls = new CaseUserRoleDtls();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    final curam.core.sl.intf.CaseUserRole caseUserRoleObj =
      curam.core.sl.fact.CaseUserRoleFactory.newInstance();

    final int workqueueID = Configuration
      .getIntProperty("curam.bdm.caseowner.workqueue").intValue();

    readWorkQueueKey.key.workQueueID = workqueueID;

    final curam.core.sl.intf.WorkQueue workqueue =
      curam.core.sl.fact.WorkQueueFactory.newInstance();

    final ReadWorkQueueDetails readWorkQueueDetails;

    readWorkQueueDetails = workqueue.read(readWorkQueueKey);

    orgObjectLinkDtls.userName = readWorkQueueDetails.dtls.name;
    orgObjectLinkDtls.orgObjectType = ORGOBJECTTYPE.WORKQUEUE;
    orgObjectLinkDtls.orgObjectReference = workqueueID;

    final OrgObjectLinkKey orgObjectLinkKey =
      caseUserRoleObj.createOrgObjectLink(orgObjectLinkDtls);

    caseUserRoleDtls.caseID = chkey.caseID;
    caseUserRoleDtls.orgObjectLinkID = orgObjectLinkKey.orgObjectLinkID;

    caseUserRoleObj.createOwnerCaseUserRole(caseUserRoleDtls);

    caseHeaderKey.caseID = chkey.caseID;
    final CachedCaseHeader cachedCaseHeader =
      CachedCaseHeaderFactory.newInstance();

    final CaseHeaderDtls caseHeaderDtls =
      cachedCaseHeader.read(caseHeaderKey);

    // Update the CaseHeader entity with the initial owner of the case.
    orgObjectLinkDtls.orgObjectLinkID = orgObjectLinkKey.orgObjectLinkID;
    caseHeaderDtls.ownerOrgObjectLinkID = orgObjectLinkDtls.orgObjectLinkID;

    cachedCaseHeader.modify(caseHeaderKey, caseHeaderDtls);

  }

}
