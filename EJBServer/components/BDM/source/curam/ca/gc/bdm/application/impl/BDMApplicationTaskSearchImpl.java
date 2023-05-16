package curam.ca.gc.bdm.application.impl;

import curam.core.impl.EnvVars;
import curam.intake.infrastructure.impl.ApplicationTaskSearchImpl;
import curam.util.resources.Configuration;

public class BDMApplicationTaskSearchImpl extends ApplicationTaskSearchImpl {

  @Override
  public boolean isTaskSearchByApplicationEnabled() {

    // TODO Auto-generated method stub
    final String isTaskSearchEnabled =
      Configuration.getProperty(EnvVars.ENV_TASK_SEARCH_ALLOWED);
    return Boolean.valueOf(isTaskSearchEnabled);
  }

}
