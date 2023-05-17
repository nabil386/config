package curam.workspaceservices.applicationprocessing.impl;

/**
 * Custom OAS Module class for WorkSpaceServices component.
 *
 * @author abid.a.khan
 *
 * Using same OOTB package, Since the parent class is package
 * private we can not move this class to custom
 * package and few dependencies are also package private.
 *
 *
 */
public class BDMOASIntakeClientWorkspaceModule
  extends BDMIntakeClientWorkspaceModule {

  @SuppressWarnings("restriction")
  @Override
  protected void configure() {

    /**
     * binds BDM IntakeClientManagement to BDM OAS implementation
     */
    bind(BDMIntakeClientManagementImpl.class)
      .to(BDMOASIntakeClientManagementImpl.class);

  }
}
