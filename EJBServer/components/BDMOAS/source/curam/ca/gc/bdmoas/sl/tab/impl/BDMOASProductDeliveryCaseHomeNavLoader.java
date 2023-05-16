package curam.ca.gc.bdmoas.sl.tab.impl;

import com.google.inject.Inject;
import curam.core.impl.CuramConst;
import curam.core.sl.tab.impl.DefaultProductDeliveryNavStateLoader;
import curam.util.persistence.GuiceWrapper;
import curam.util.tab.impl.DynamicNavStateLoader;
import curam.util.tab.impl.NavigationState;
import java.util.Map;
/**
 * Class to populates Case navigation for ProduceDeliveryCase.
 * 
 */
public class BDMOASProductDeliveryCaseHomeNavLoader
  implements DynamicNavStateLoader {

  @Inject
  private DefaultProductDeliveryNavStateLoader defaultProductDeliveryNavStateLoader;

  /**
   * Constructor.
   */
  public BDMOASProductDeliveryCaseHomeNavLoader() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Returns the state of the given list of navigation items.
   *
   * @param navigation The navigation instance to load states into.
   * @param pageParameters The context information, in our current case it is
   * the page parameters.
   *
   * @param idsToUpdate The IDs requested by the client to be updated.
   * @return The same navigation instance with the updated states.
   */ 
  @Override
  public NavigationState loadNavState(final NavigationState navigation,
    final Map<String, String> pageParameters, final String[] idsToUpdate) {

    final NavigationState returnNavigation =
      defaultProductDeliveryNavStateLoader.loadNavState(navigation,
        pageParameters, idsToUpdate);
    returnNavigation.setVisible(false, CuramConst.kLegalStatus);
    returnNavigation.setVisible(false, CuramConst.kLegalActions);

    return returnNavigation;
  }

}
