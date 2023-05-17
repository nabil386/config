package curam.ca.gc.bdm.sl.tab.externalparty.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.sl.tab.impl.BDMTabLoaderConsts;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.core.sl.entity.fact.ExternalPartyFactory;
import curam.core.sl.entity.struct.ExternalPartyDtls;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import java.util.Map;

/**
 * FEATURE 52455 - External Party Home tab configuration changes
 */
public class BDMExternalPartyHomeMenuLoader
  implements DynamicMenuStateLoader {

  /**
   * {@inheritDoc}
   */
  @Override
  public MenuState loadMenuState(final MenuState menuState,
    final Map<String, String> pageParameters, @SuppressWarnings("unused")
    final String[] idsToUpdate) throws AppException, InformationalException {

    // configure menuState
    final MenuState returnState = menuState;

    final String concernRoleIDParam =
      pageParameters.get(TabLoaderConst.kConcernRoleID);

    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID = Long.parseLong(concernRoleIDParam);

    final ExternalPartyDtls externalPartyDtls =
      ExternalPartyFactory.newInstance().read(externalPartyKey);

    final boolean isIOAdminUser = BDMUtil.isIOAdminUser();

    if (externalPartyDtls.type.equals(EXTERNALPARTYTYPE.SSACOUNTRY)
      && !isIOAdminUser) {

      returnState.setVisible(true, BDMTabLoaderConsts.kExtPrtyEditDetails);
      returnState.setEnabled(false, BDMTabLoaderConsts.kExtPrtyEditDetails);
      // Task 102943: DEV: Remove Notes functionality :Set visibility to false
      // JP -->
      returnState.setVisible(false,
        BDMTabLoaderConsts.kExtPrtyNewNoteDetails);
      returnState.setEnabled(false,
        BDMTabLoaderConsts.kExtPrtyNewNoteDetails);

      returnState.setVisible(true,
        BDMTabLoaderConsts.kExtPrtyNewOfficeDetails);
      returnState.setEnabled(false,
        BDMTabLoaderConsts.kExtPrtyNewOfficeDetails);
    } else {
      returnState.setVisible(true, BDMTabLoaderConsts.kExtPrtyEditDetails);
      returnState.setEnabled(true, BDMTabLoaderConsts.kExtPrtyEditDetails);
      // Task 102943: DEV: Remove Notes functionality :Set visibility to false
      // JP -->
      returnState.setVisible(false,
        BDMTabLoaderConsts.kExtPrtyNewNoteDetails);
      returnState.setEnabled(false,
        BDMTabLoaderConsts.kExtPrtyNewNoteDetails);

      returnState.setVisible(true,
        BDMTabLoaderConsts.kExtPrtyNewOfficeDetails);
      returnState.setEnabled(true,
        BDMTabLoaderConsts.kExtPrtyNewOfficeDetails);
    }

    return returnState;
  }

}
