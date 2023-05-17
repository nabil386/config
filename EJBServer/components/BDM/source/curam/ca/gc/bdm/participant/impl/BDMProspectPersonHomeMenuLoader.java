package curam.ca.gc.bdm.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcommonintake.impl.BDMMyApplicationsHelperImpl;
import curam.commonintake.participant.tab.impl.ProspectPersonMenuLoaderBase;
import curam.core.impl.EnvVars;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import java.util.Map;

/**
 * Task - 8904 - ProspectPerson tab configuration changes
 */
public class BDMProspectPersonHomeMenuLoader
  implements DynamicMenuStateLoader {

  @Inject
  private ProspectPersonMenuLoaderBase prospectPersonMenuLoaderBase;

  @Inject
  private BDMMyApplicationsHelperImpl myApplicationsHelper;

  public BDMProspectPersonHomeMenuLoader() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public MenuState loadMenuState(final MenuState menuState,
    final Map<String, String> pageParameters, final String[] idsToUpdate)
    throws AppException, InformationalException {

    final MenuState retMenuState = this.prospectPersonMenuLoaderBase
      .loadMenuState(menuState, pageParameters, idsToUpdate);

    final boolean pdcEnabled =
      Configuration.getBooleanProperty(EnvVars.ENV_PDC_ENABLED);

    retMenuState.setVisible(!pdcEnabled, TabLoaderConst.kPersonEdit);
    retMenuState.setVisible(pdcEnabled, TabLoaderConst.kPersonEditPDC);

    retMenuState.setVisible(false, TabLoaderConst.kPersonNewTriage);
    retMenuState.setVisible(false, TabLoaderConst.kPersonNewScreening);
    retMenuState.setVisible(false, TabLoaderConst.kReferralApplicationCase);

    try {
      final String concernRoleIDParam = pageParameters.get("concernRoleID");
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = Long.valueOf(concernRoleIDParam);
      retMenuState.setEnabled(myApplicationsHelper
        .allowApplicationFormCreationForConcernRoleInd(concernRoleKey),
        "ApplicationForm");
    } catch (final Exception e) {

    }

    return retMenuState;
  }

}
