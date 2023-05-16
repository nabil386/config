package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.entity.bdmcommonintake.fact.BDMProgramReopenInfoFactory;
import curam.ca.gc.bdm.entity.bdmcommonintake.intf.BDMProgramReopenInfo;
import curam.ca.gc.bdm.entity.bdmcommonintake.struct.BDMProgramReopenInfoDtls;
import curam.ca.gc.bdm.entity.bdmcommonintake.struct.BDMProgramReopenInfoDtlsList;
import curam.ca.gc.bdm.entity.bdmcommonintake.struct.BDMProgramReopenInfoKeyStruct1;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.commonintake.entity.fact.ProgramReopenInfoFactory;
import curam.commonintake.entity.struct.ProgramReopenInfoDtlsList;
import curam.commonintake.facade.fact.ApplicationCaseProgramFactory;
import curam.commonintake.facade.intf.ApplicationCaseProgram;
import curam.commonintake.facade.struct.ProgramReopenDetails;
import curam.commonintake.facade.struct.ProgramReopenDetailsList;
import curam.commonintake.impl.AbstractProgramReopenEvents;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.impl.EnvVars;
import curam.core.sl.impl.CaseTransactionLogIntf;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.participantmessages.persistence.impl.ParticipantMessageDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.List;

/**
 *
 * Event listener for program reopen events
 * added this event listener to send notification to the client when case is
 * reopened and open the application case if its closed.
 *
 * @author teja.konda
 * @since ADO-9417
 *
 */
public class BDMProgramReopenEvents extends AbstractProgramReopenEvents {

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ParticipantMessageDAO participantMessageDAO;

  @Inject
  private ConcernRoleDAO concernroleDAO;

  @Inject
  private Provider<CaseTransactionLogIntf> caseTransactionLogProvider;

  // BEGIN TASK 23392, 28419 - reopen application program
  /**
   * Implement the pre reopen application program to open the closed application
   * case. The OOTB reopen program implementation creates milestone deliveries
   * before reopening the application case if the program has milestone
   * deliveries.
   * The milestone delivery creation throws exception if the case status is
   * closed. This prevents the application program reopen for the applications
   * case which have milestone configured.
   */
  @Override
  public void
    preReopen(final IntakeProgramApplication intakeProgramApplication)
      throws InformationalException {

    final ApplicationCase applicationCase =
      getApplicationCase(intakeProgramApplication);

    String comments = "";

    try {
      final IntakeProgramApplicationKey intakeProgramApplicationKey =
        new IntakeProgramApplicationKey();
      intakeProgramApplicationKey.intakeProgramApplicationID =
        intakeProgramApplication.getID();

      final ProgramReopenInfoDtlsList programReopenInfoDtlsList =
        ProgramReopenInfoFactory.newInstance()
          .searchByProgramApplication(intakeProgramApplicationKey);

      if (programReopenInfoDtlsList.dtls.size() > 0) {
        comments = programReopenInfoDtlsList.dtls.get(0).comments;
      }

    } catch (final AppException appex) {
      appex.printStackTrace();
    }
  }
  // END TASK 23392,28419 - reopen application program

  /**
   *
   * implement post event listner to trigger the notification to the client
   *
   */
  @Override
  public void
    postReopen(final IntakeProgramApplication intakeProgramApplication)
      throws InformationalException {

    // read ApplicationCaseID
    final long applicationCaseID =
      getApplicationCase(intakeProgramApplication).getID().longValue();

    // get Primary participant for the applicationCase
    final long concernRoleID = caseHeaderDAO.get(applicationCaseID)
      .getConcernRole().getID().longValue();

    final ConcernRole concernRole = concernroleDAO.get(concernRoleID);

    final ParticipantMessage participantMessage =
      participantMessageDAO.newInstance();

    // set Concern Role instance
    participantMessage.setConcernRole(concernRole);

    participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
    // read duration property and set the expiry day
    final int durationDay = Configuration.getIntProperty(
      EnvVars.BDM_ENV_NOTIFICATION_DURATION_APP_CASE_REOPENED);
    participantMessage
      .setExpiryDateTime(participantMessage.getEffectiveDateTime()
        .addTime(durationDay * BDMConstants.gkTwentyFour, 0, 0));
    // set property file, message title and body
    participantMessage.setMessageProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_APP_CASE_REOPENED));
    participantMessage.setPropertyFileName(Configuration.getProperty(
      EnvVars.BDM_ENV_NOTIFICATION_PROPERTY_FILENAME_APP_CASE_REOPENED));
    participantMessage.setTitleProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_TITLE_APP_CASE_REOPENED));
    // set message type to Application and display it on the notification card
    participantMessage
      .setMessageType(ParticipantMessageTypeEntry.APPLICATION);

    participantMessage.insert();

    // BEGIN TASK-28625 Display Time on Application Reopen Details tab
    final ApplicationCaseProgram applicationCaseProgram =
      ApplicationCaseProgramFactory.newInstance();

    final IntakeProgramApplicationKey paramIntakeProgramApplicationKey =
      new IntakeProgramApplicationKey();
    paramIntakeProgramApplicationKey.intakeProgramApplicationID =
      intakeProgramApplication.getID();
    try {

      // Read from OOTB entity
      final ProgramReopenDetailsList reopenDetailList = applicationCaseProgram
        .listProgramReopenings(paramIntakeProgramApplicationKey);
      final BDMProgramReopenInfo bdmProgramReopenInfo =
        BDMProgramReopenInfoFactory.newInstance();

      // Read from Customize entry
      final BDMProgramReopenInfoKeyStruct1 bdmProgramReopenInfoKeyStruct1 =
        new BDMProgramReopenInfoKeyStruct1();
      bdmProgramReopenInfoKeyStruct1.intakeProgramApplicationID =
        intakeProgramApplication.getID();
      final BDMProgramReopenInfoDtlsList bdmProgramReopenDetailsList =
        bdmProgramReopenInfo
          .searchByIntakeApplicationID(bdmProgramReopenInfoKeyStruct1);
      boolean noRecordFlag = false;
      if (bdmProgramReopenDetailsList == null) {
        noRecordFlag = true;
      }
      if (!noRecordFlag && bdmProgramReopenDetailsList.dtls.size() == 0) {
        noRecordFlag = true;
      }

      if (noRecordFlag) {
        // no records for any record in the parent table
        for (final ProgramReopenDetails reopenDetails : reopenDetailList.list) {
          final BDMProgramReopenInfoDtls bdmProgramReopenInfoDtls =
            new BDMProgramReopenInfoDtls();
          bdmProgramReopenInfoDtls.intakeProgramApplicationID =
            intakeProgramApplication.getID();
          bdmProgramReopenInfoDtls.programReopenInfoID =
            reopenDetails.programReopenInfoID;
          bdmProgramReopenInfoDtls.reopenDateTime =
            DateTime.getCurrentDateTime();
          bdmProgramReopenInfo.insert(bdmProgramReopenInfoDtls);
        }
      } else {

        for (final ProgramReopenDetails reopenDetails : reopenDetailList.list) {
          boolean recordMatched = false;
          for (final BDMProgramReopenInfoDtls bdmReopenDetails : bdmProgramReopenDetailsList.dtls) {
            if (reopenDetails.programReopenInfoID == bdmReopenDetails.programReopenInfoID) {
              recordMatched = true;
              break;
            }
          }
          if (!recordMatched) {
            final BDMProgramReopenInfoDtls bdmProgramReopenInfoDtls =
              new BDMProgramReopenInfoDtls();
            bdmProgramReopenInfoDtls.intakeProgramApplicationID =
              intakeProgramApplication.getID();
            bdmProgramReopenInfoDtls.programReopenInfoID =
              reopenDetails.programReopenInfoID;
            bdmProgramReopenInfoDtls.reopenDateTime =
              DateTime.getCurrentDateTime();
            bdmProgramReopenInfo.insert(bdmProgramReopenInfoDtls);
          }
        }
      }

    } catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // END TASK-28625
  }

  /**
   * util method to get Application caseID from intakeprogram
   *
   * @param intakeProgramApplication
   * @return
   */
  private ApplicationCase getApplicationCase(
    final IntakeProgramApplication intakeProgramApplication) {

    final List<Long> cases = intakeProgramApplication.listAssociatedCases();

    if (!cases.isEmpty()) {
      return this.applicationCaseDAO.get(cases.get(0));
    }
    return null;
  }

}
