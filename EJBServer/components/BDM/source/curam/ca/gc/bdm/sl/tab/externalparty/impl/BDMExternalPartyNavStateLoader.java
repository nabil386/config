package curam.ca.gc.bdm.sl.tab.externalparty.impl;

import curam.ca.gc.bdm.sl.tab.impl.BDMTabLoaderConsts;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.core.sl.entity.fact.ExternalPartyFactory;
import curam.core.sl.entity.struct.ExternalPartyDtls;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.DynamicNavStateLoader;
import curam.util.tab.impl.NavigationState;
import java.util.Map;

/**
 * Populates the External Party navigation.
 */
public class BDMExternalPartyNavStateLoader implements DynamicNavStateLoader {

  // ___________________________________________________________________________
  /**
   * Method to dynamically enable or disable the external party navigation
   * links.
   *
   * @param navigation The navigation instance to load states into.
   * @param pageParameters The context information, in our current case it is
   * the page parameters.
   *
   * @param idsToUpdate The IDs requested by the client to be updated.
   * @return The same navigation instance with the updated states.
   * @throws AppException generic exception signature
   * @throws InformationalException generic exception signature
   */
  @Override
  public NavigationState loadNavState(final NavigationState navigation,
    final Map<String, String> pageParameters, final String[] idsToUpdate)
    throws AppException, InformationalException {

    boolean isSSACountryExtParty = false;

    final String concernRoleIDParam =
      pageParameters.get(TabLoaderConst.kConcernRoleID);

    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID = Long.parseLong(concernRoleIDParam);

    final ExternalPartyDtls externalPartyDtls =
      ExternalPartyFactory.newInstance().read(externalPartyKey);

    if (externalPartyDtls.type.equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
      isSSACountryExtParty = true;
    }

    for (final String id : idsToUpdate) {

      if (id.equals(BDMTabLoaderConsts.kSSACountryHistory)) {

        // The ‘SSA Country History’ tab on the ‘Administration’ tab will only
        // load up if the External Party participant is of Type ‘SSA Country’.
        navigation.setVisible(isSSACountryExtParty,
          BDMTabLoaderConsts.kSSACountryHistory);
      }

    }

    return navigation;
  }

}
