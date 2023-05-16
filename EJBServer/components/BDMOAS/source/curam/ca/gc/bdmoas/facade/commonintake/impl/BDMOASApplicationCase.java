package curam.ca.gc.bdmoas.facade.commonintake.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.facade.bdmcommonintake.fact.BDMApplicationCaseFactory;
import curam.ca.gc.bdm.facade.bdmcommonintake.intf.BDMApplicationCase;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMApplicationCaseDetailsList;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.ca.gc.bdmoas.facade.commonintake.struct.BDMOASProgramDetailsList;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.StackContainerBuilder;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.intf.ApplicationCase;
import curam.commonintake.facade.struct.ApplicationCaseContextPanelDetails;
import curam.commonintake.facade.struct.ProgramDetails;
import curam.commonintake.facade.struct.ProgramDetailsList;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.commonintake.message.APPLICATIONCASE;
import curam.core.impl.EnvVars;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.ValueList;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author naveen.garg
 * ApplictionCase class customized from OOTB to update flag values as per Task
 * 94706
 *
 */
public class BDMOASApplicationCase
  extends curam.ca.gc.bdmoas.facade.commonintake.base.BDMOASApplicationCase {

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private BDMOASApplicationCaseContextPanelHelperImpl applicationCaseContextPanelHelper;

  public BDMOASApplicationCase() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * @description: Task 94706 View an OAS Application
   * set programsExpiryDateNotExistInd as false and programsOnlyInd as true
   * @param :@param key
   * @param :@return
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public BDMApplicationCaseDetailsList listApplicationCaseByConcernRole(
    final ConcernRoleKey key) throws AppException, InformationalException {

    final BDMApplicationCase applicationCase =
      BDMApplicationCaseFactory.newInstance();
    final BDMApplicationCaseDetailsList applicationCaseDetailsList =
      applicationCase.listApplicationCaseByConcernRole(key);
    applicationCaseDetailsList.programsExpiryDateNotExistInd = false;
    applicationCaseDetailsList.programsOnlyInd = true;
    return applicationCaseDetailsList;
  }

  /**
   * @description: Task 94706 View an OAS Application
   * set addProgramsInd, dateAddedInd and withdrawInd as false
   * @param :@param key
   * @param :@return
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public BDMOASProgramDetailsList listPrograms(final ApplicationCaseKey key)
    throws AppException, InformationalException {

    final ApplicationCase applicationCase =
      ApplicationCaseFactory.newInstance();
    final ProgramDetailsList programDetailsList =
      applicationCase.listPrograms(key);
    final ValueList<ProgramDetails> list = programDetailsList.dtls;
    final Iterator<ProgramDetails> iterProgDtls = list.iterator();
    int programDtl = 0;
    final BDMOASProgramDetailsList bdmOASProgramDetailsList =
      new BDMOASProgramDetailsList();
    boolean pendingStatus = true;
    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();
      dtl.dateAddedInd = false;
      dtl.withdrawInd = false;
      pendingStatus = !dtl.status
        .equals(IntakeProgramApplicationStatusEntry.PENDING.getCode());

      programDetailsList.dtls.set(programDtl, dtl);
      programDtl++;
    }
    bdmOASProgramDetailsList.closeAllBenefitsInd =
      pendingStatus && !list.isEmpty();

    programDetailsList.addProgramsInd =
      Configuration.getBooleanProperty(EnvVars.BDMOAS_ADD_BENEFITS_ENABLED);

    bdmOASProgramDetailsList.dtls = programDetailsList;
    return bdmOASProgramDetailsList;
  }

  /**
   * @description: Application Case Context Panel
   * @param :@param key
   * @param :@return
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public ApplicationCaseContextPanelDetails readContextPanelDetails(
    final CaseHeaderKey key) throws AppException, InformationalException {

    final ApplicationCaseContextPanelDetails applicationCaseContextPanelDetails =
      new ApplicationCaseContextPanelDetails();

    final curam.commonintake.impl.ApplicationCase applicationCase =
      this.applicationCaseDAO.get(key.caseID);

    final LocalisableString tabTitleInfo = new LocalisableString(
      APPLICATIONCASE.INF_APPLICATION_CASE_TYPE_AND_REFERENCE);

    tabTitleInfo.arg(applicationCase.getApplicationCaseAdmin().getName());

    tabTitleInfo.arg(applicationCase.getCaseReference());

    applicationCaseContextPanelDetails.name =
      tabTitleInfo.toClientFormattedText();

    final ContentPanelBuilder containerPanel =
      ContentPanelBuilder.createPanel("container-panel-applicationcase");

    final List<CaseParticipantRole> actionClients =
      applicationCase.listActiveCaseMembers();
    ContentPanelBuilder middlePanel;
    ContentPanelBuilder rightPanel;
    if (actionClients.size() > 1) {

      final StackContainerBuilder stackContainerBuilder =
        this.applicationCaseContextPanelHelper
          .getCaseMemberThumbnailDetails(actionClients);

      containerPanel.addWidgetItem(stackContainerBuilder, "style",
        "stack-container");

      middlePanel = this.applicationCaseContextPanelHelper
        .getContextPanelDetails(applicationCase);

      containerPanel.addWidgetItem(middlePanel, "style", "content-panel",
        "content-panel-detail applicationcase-content-panel applicationcase-content-panel-blue-footer");

    } else {
      rightPanel = this.applicationCaseContextPanelHelper
        .getCaseMemberThumbnailDetails(actionClients.get(0));

      containerPanel.addWidgetItem(rightPanel, "style", "content-panel",
        "content-panel-detail case-participant-panel");

      middlePanel = this.applicationCaseContextPanelHelper
        .getContextPanelDetails(applicationCase);

      containerPanel.addWidgetItem(middlePanel, "style", "content-panel",
        "content-panel-detail applicationcase-content-panel-medium applicationcase-content-panel-blue-footer");

    }

    // RFR Changes : Add RFR message on the context panel
    for (int i = 0; i < actionClients.size(); ++i) {
      final CaseParticipantRole caseParticipantRole = actionClients.get(i);
      if (BDMUtil.countAppealCasesByParticipantID(
        caseParticipantRole.getConcernRole().getID()) > 0) {
        final LocalisableString apString =
          new LocalisableString(BDMRFRSISSUE.INFO_BDM_RFR_CONTEXT_MESSAGE);
        middlePanel.addlocalisableStringItem(apString.toClientFormattedText(),
          "content-bdm-rfr-info-dtls");
        // break from loop as RFR message is added
        break;
      }
    }
    rightPanel = this.applicationCaseContextPanelHelper
      .getContextRightPanelDetails(applicationCase);

    containerPanel.addWidgetItem(rightPanel, "style", "content-panel",
      "content-panel-detail applicationcase-right-panel");

    applicationCaseContextPanelDetails.xmlPanelData =
      containerPanel.toString();

    return applicationCaseContextPanelDetails;
  }

}
