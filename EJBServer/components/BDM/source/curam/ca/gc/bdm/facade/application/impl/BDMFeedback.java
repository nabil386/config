package curam.ca.gc.bdm.facade.application.impl;

import curam.ca.gc.bdm.facade.application.struct.BDMFeedbackTextDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;

public class BDMFeedback
  extends curam.ca.gc.bdm.facade.application.base.BDMFeedback {

  @Override
  public BDMFeedbackTextDetails getTimeoutMinutes()
    throws AppException, InformationalException {

    final BDMFeedbackTextDetails bdmFeedbackTextDetails =
      new BDMFeedbackTextDetails();

    final Integer timeout = Configuration
      .getIntProperty(EnvVars.kInternalTimeoutWarningDialogTimePeriod);

    final int minute = timeout / BDMConstants.kminsPerHour;
    bdmFeedbackTextDetails.minuteText = String.valueOf(minute);

    return bdmFeedbackTextDetails;
  }

}
