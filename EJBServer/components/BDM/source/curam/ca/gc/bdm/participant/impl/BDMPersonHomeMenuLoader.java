package curam.ca.gc.bdm.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcommonintake.impl.BDMMyApplicationsHelperImpl;
import curam.citizenworkspace.entity.fact.CWExternalPartyLinkFactory;
import curam.citizenworkspace.entity.intf.CWExternalPartyLink;
import curam.citizenworkspace.entity.struct.CWExternalPartyLinkDtls;
import curam.citizenworkspace.entity.struct.ExternalPartySystemRecordStatusKey;
import curam.codetable.impl.RECORDSTATUSEntry;
import curam.commonintake.participant.tab.impl.PersonMenuLoaderBase;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.intf.ExternalUser;
import curam.core.sl.entity.struct.ExternalUserDtls;
import curam.core.sl.entity.struct.ExternalUserKey;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import curam.util.type.NotFoundIndicator;
import java.util.Map;

/**
 * Task - 8904 - Person tab configuration changes
 */
public class BDMPersonHomeMenuLoader implements DynamicMenuStateLoader {

  @Inject
  private PersonMenuLoaderBase personMenuLoaderBase;

  @Inject
  private BDMMyApplicationsHelperImpl myApplicationsHelper;

  public BDMPersonHomeMenuLoader() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public MenuState loadMenuState(final MenuState menuState,
    final Map<String, String> pageParameters, final String[] idsToUpdate)
    throws AppException, InformationalException {

    final String concernRoleIDParam = pageParameters.get("concernRoleID");

    final MenuState retMenuState = this.personMenuLoaderBase
      .loadMenuState(menuState, pageParameters, idsToUpdate);

    final boolean pdcEnabled =
      Configuration.getBooleanProperty(EnvVars.ENV_PDC_ENABLED);

    retMenuState.setVisible(!pdcEnabled, TabLoaderConst.kPersonEdit);
    retMenuState.setVisible(pdcEnabled, TabLoaderConst.kPersonEditPDC);

    retMenuState.setVisible(false, TabLoaderConst.kPersonNewTriage);
    retMenuState.setVisible(false, TabLoaderConst.kPersonNewScreening);
    retMenuState.setVisible(false, TabLoaderConst.kReferralApplicationCase);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = Long.valueOf(concernRoleIDParam);
    retMenuState.setEnabled(myApplicationsHelper
      .allowApplicationFormCreationForConcernRoleInd(concernRoleKey),
      "ApplicationForm");

    // Universal Access Menu State
    final CWExternalPartyLink externalPartyLink =
      CWExternalPartyLinkFactory.newInstance();
    final NotFoundIndicator externalPartyLinkInd = new NotFoundIndicator();
    final ExternalPartySystemRecordStatusKey recordStatusKey =
      new ExternalPartySystemRecordStatusKey();
    recordStatusKey.externalPartyID = concernRoleIDParam;
    recordStatusKey.externalSystemID = "CORE_CURAM";
    recordStatusKey.recordStatus = RECORDSTATUSEntry.NORMAL.getCode();
    final CWExternalPartyLinkDtls partyLinkDtls =
      externalPartyLink.readActiveByExternalPartyAndSystemRecordStatus(
        externalPartyLinkInd, recordStatusKey);
    if (externalPartyLinkInd.isNotFound()) {
      this.setMenuItemState(menuState, "CreateUniversalAccessAccount", true,
        true);
      this.setMenuItemState(menuState, "EnableUniversalAccessAccount", false,
        false);
      this.setMenuItemState(menuState, "DisableUniversalAccessAccount", false,
        false);
      this.setMenuItemState(menuState, "Case",
              true, false);
    } else {
      this.setMenuItemState(menuState, "CreateUniversalAccessAccount", false,
        false);
      final ExternalUser externalUser = ExternalUserFactory.newInstance();
      final ExternalUserKey key = new ExternalUserKey();
      key.userName = partyLinkDtls.cwUserName;
      final ExternalUserDtls userDtls = externalUser.read(key);
      if (userDtls.accountEnabled) {
        this.setMenuItemState(menuState, "EnableUniversalAccessAccount",
          false, false);
        this.setMenuItemState(menuState, "DisableUniversalAccessAccount",
          true, true);
      } else {
        this.setMenuItemState(menuState, "EnableUniversalAccessAccount", true,
          true);
        this.setMenuItemState(menuState, "DisableUniversalAccessAccount",
          false, false);
      }
    }
    // Universal Access Menu State

    return retMenuState;
  }

  void setMenuItemState(final MenuState returnState,
    final String menuItemIdentifier, final boolean visible,
    final boolean enabled) {

    returnState.setVisible(visible, new String[]{menuItemIdentifier });
    returnState.setEnabled(enabled, new String[]{menuItemIdentifier });
  }

}
