package curam.ca.gc.bdm.facade.help.impl;

import curam.ca.gc.bdm.facade.help.struct.BDMIOHelpTextDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;

public class BDMHelp extends curam.ca.gc.bdm.facade.help.base.BDMHelp {

  @Override
  public BDMIOHelpTextDetails getIOKnowledgeArticles()
    throws AppException, InformationalException {

    final BDMIOHelpTextDetails bdmHelpTextDetails =
      new BDMIOHelpTextDetails();

    final Integer timeout = Configuration
      .getIntProperty(EnvVars.kInternalTimeoutWarningDialogTimePeriod);

    final int minute = timeout / BDMConstants.kminsPerHour;
    bdmHelpTextDetails.helpText = String.valueOf(minute);

    return bdmHelpTextDetails;
  }

}
