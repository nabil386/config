package curam.ca.gc.bdm.hooks.task.impl;

import com.google.inject.AbstractModule;
import curam.core.hook.task.impl.TaskActionsImpl;

public class BDMTaskActionsImplModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(TaskActionsImpl.class).to(
      curam.ca.gc.bdm.hooks.task.impl.BDMTaskActionsImpl.class);
  }

}
