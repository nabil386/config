package curam.ca.gc.bdmoas.sl.tab.impl;

import com.google.inject.Inject;
import curam.core.sl.tab.impl.CaseHomeMenuLoader;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.util.persistence.GuiceWrapper;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import java.util.Map;

/**
 * Class to populates the Tab Loader Menu for ProduceDeliveryCase.
 * 
 */
public class BDMOASProductDeliveryCaseHomeMenuLoader
  implements DynamicMenuStateLoader {

  @Inject
  private CaseHomeMenuLoader caseHomeMenuLoader;

  /**
   * Constructor.
   */
  public BDMOASProductDeliveryCaseHomeMenuLoader() {

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

    final MenuState returnState = caseHomeMenuLoader.loadMenuState(menuState,
      pageParameters, idsToUpdate);

    returnState.setVisible(false, TabLoaderConst.kEdit);
    returnState.setEnabled(false, TabLoaderConst.kEdit);
    returnState.setVisible(false, TabLoaderConst.kReactivate);
    returnState.setEnabled(false, TabLoaderConst.kReactivate);
    returnState.setVisible(false, TabLoaderConst.kSuspend);
    returnState.setEnabled(false, TabLoaderConst.kSuspend);
    returnState.setVisible(false, TabLoaderConst.kUnsuspend);
    returnState.setEnabled(false, TabLoaderConst.kUnsuspend);
    returnState.setVisible(false, TabLoaderConst.kClose);
    returnState.setEnabled(false, TabLoaderConst.kClose);
    returnState.setVisible(false, TabLoaderConst.kChangeClosure);
    returnState.setEnabled(false, TabLoaderConst.kChangeClosure);

    return returnState;
  }

}
