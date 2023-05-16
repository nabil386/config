package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.workspaceservices.applicationprocessing.impl.SubmitApplicationEvents;
import curam.workspaceservices.intake.impl.IntakeApplication;

public class BDMSubmitApplicationEventsListener
  implements SubmitApplicationEvents {

  @Inject
  private BDMUtil bdmUtil;

  @Override
  public void postSubmitApplication(final IntakeApplication intakeApplication)
    throws InformationalException, AppException {

    // check is International residential or mailing address is added, then map
    // the Intl datastore back to the respective address datastore
    final Entity rootEntity =
      BDMApplicationEventsUtil.getRootEntity(intakeApplication);
    bdmUtil.mapIntlAddressesToAddressDatastore(rootEntity);
  }

  @Override
  public void preSubmitApplication(final Long rootEntityID,
    final String schemaName) throws InformationalException, AppException {

    // TODO Auto-generated method stub

  }

}
