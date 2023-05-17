package curam.ca.gc.bdm.sl.organization.impl;

import com.google.inject.AbstractModule;
import curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers;
import curam.ca.gc.bdm.sl.organization.userskill.intf.BDMUserSkill;
import curam.ca.gc.bdm.sl.supervisor.intf.BDMMaintainSupervisorWorkQueuesProcess;

public class BDMOrganizationModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(BDMMaintainSupervisorUsers.class).to(
      curam.ca.gc.bdm.sl.organization.supervisor.impl.BDMMaintainSupervisorUsers.class);

    bind(BDMUserSkill.class)
      .to(curam.ca.gc.bdm.sl.organization.userskill.impl.BDMUserSkill.class);

    bind(BDMMaintainSupervisorWorkQueuesProcess.class).to(
      curam.ca.gc.bdm.sl.supervisor.impl.BDMMaintainSupervisorWorkQueuesProcess.class);
  }

}
