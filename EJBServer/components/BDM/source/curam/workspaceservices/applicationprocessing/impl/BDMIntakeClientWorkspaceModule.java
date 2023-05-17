package curam.workspaceservices.applicationprocessing.impl;

import com.google.inject.AbstractModule;

/**
 * Custom Module class for WorkSpaceServices component.
 *
 * @author NLynch
 *
 * Using same OOTB package, Since the parent class is package
 * private we can not move this class to custom
 * package and few dependencies are also package private.
 *
 * There are no hooks provided by Curam for this customization. So we
 * are keeping these class to the same package as the Curam OOTB
 * package.
 *
 */
public class BDMIntakeClientWorkspaceModule extends AbstractModule {

  @SuppressWarnings("restriction")
  @Override
  protected void configure() {

    /**
     * binds concern role mapping
     */
    bind(IntakeClientManagementImpl.class)
      .to(BDMIntakeClientManagementImpl.class);

  }
}
