package curam.ca.gc.bdmoas.sl.tab.impl;

import com.google.inject.Inject;
import curam.codetable.impl.CASESTATUSEntry;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.commonintake.tab.impl.ApplicationCaseMenuLoader;
import curam.core.impl.EnvVars;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class to populates the Tab Loader Menu for Application Case.
 *
 * @author naveen.garg
 */

public class BDMOASApplicationCaseMenuLoader
  implements DynamicMenuStateLoader {

  @Inject
  private ApplicationCaseMenuLoader applicationCaseMenuLoader;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  /**
   * Constructor.
   */
  public BDMOASApplicationCaseMenuLoader() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Method to returns the state of the given list of menu items.
   *
   * @param menuState The menuState instance to load states into.
   * @param pageParameters The context information, in our current case it is
   * the page parameters.
   * @param idsToUpdate The IDs requested by the client to be updated.
   * @return The same menuState instance with the updated states.
   */
  @Override
  public MenuState loadMenuState(final MenuState menuState,
    final Map<String, String> pageParameters, @SuppressWarnings("unused")
    final String[] idsToUpdate) {

    final MenuState returnState = applicationCaseMenuLoader
      .loadMenuState(menuState, pageParameters, idsToUpdate);
    returnState.setVisible(true, "CloseAllBenefits");
    returnState.setVisible(false, "Edit");
    final boolean addprogram =
      Configuration.getBooleanProperty(EnvVars.BDMOAS_ADD_BENEFITS_ENABLED);

    returnState.setVisible(addprogram, "AddProgram");
    returnState.setVisible(false, "Resubmit");

    final String applicationCaseIDParam = pageParameters.get("caseID");
    final ApplicationCase application =
      this.applicationCaseDAO.get(Long.parseLong(applicationCaseIDParam));
    final List<IntakeProgramApplication> programList =
      application.getPrograms();
    final Iterator<IntakeProgramApplication> iter = programList.iterator();
    boolean pendingStatus = true;
    while (iter.hasNext()) {
      final IntakeProgramApplication progApp = iter.next();
      if (!progApp.getLifecycleState()
        .equals(IntakeProgramApplicationStatusEntry.PENDING)) {
        pendingStatus = false;
      }
    }

    returnState
      .setEnabled(CASESTATUSEntry.OPEN.equals(application.getStatus())
        && pendingStatus && !programList.isEmpty(), "CloseAllBenefits");

    return returnState;
  }
}
