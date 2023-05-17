package curam.ca.gc.bdm.sl.tab.task.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.sl.tab.impl.BDMTabLoaderConsts;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import java.util.Map;

public class BDMTaskHomeMenuLoader implements DynamicMenuStateLoader {

  BDMUtil bdmUtil = new BDMUtil();

  /**
   * {@inheritDoc}
   */
  @Override
  public MenuState loadMenuState(final MenuState menuState,
    final Map<String, String> pageParameters, @SuppressWarnings("unused")
    final String[] idsToUpdate) throws AppException, InformationalException {

    // configure menuState
    final MenuState returnState = menuState;

    final String taskID = pageParameters.get(BDMTabLoaderConsts.kTaskID);

    long taskIdentificationID = Long.parseLong(taskID);

    if (!bdmUtil.isAllowedToEditTaskDeadline(taskIdentificationID)) {

      returnState.setVisible(true, BDMTabLoaderConsts.kEditDeadline);
      returnState.setEnabled(false, BDMTabLoaderConsts.kEditDeadline);

    } else {
      returnState.setVisible(true, BDMTabLoaderConsts.kEditDeadline);
      returnState.setEnabled(true, BDMTabLoaderConsts.kEditDeadline);
    }

    return returnState;
  }

}
