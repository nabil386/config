package curam.ca.gc.bdm.facade.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.struct.BDMProgramAuthDenialDetails;
import curam.ca.gc.bdm.facade.struct.BDMProgramDetailsDenialList;
import curam.commonintake.authorisation.facade.fact.ProgramAuthorisationFactory;
import curam.commonintake.authorisation.facade.struct.AuthorisationDetails;
import curam.commonintake.authorisation.facade.struct.ProgramDenialDetails;
import curam.commonintake.impl.ApplicationCase;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.Map;

/**
 * Program authorisation
 *
 * @author vishal.madichetty
 *
 */
public class BDMProgramAuthorisation
  extends curam.ca.gc.bdm.facade.base.BDMProgramAuthorisation {

  @Inject
  private curam.commonintake.impl.ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private Map<Long, curam.ca.gc.bdm.sl.application.impl.BDMProgramAuthorisation> programAuthoriseMap;

  protected BDMProgramAuthorisation() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method list all the denied programs.
   *
   * @since : TASK 9428
   * @return : Program denial details list.
   */
  @Override
  public BDMProgramDetailsDenialList
    listProgramDenials(final IntakeProgramApplicationKey key)
      throws AppException, InformationalException {

    // Calling OOTB list method to get the username and the program denial id
    final curam.commonintake.authorisation.facade.struct.ProgramDenialDetailsList dtls =
      ProgramAuthorisationFactory.newInstance().listProgramDenials(key);

    final BDMProgramDetailsDenialList pgmDenialDetailsList =
      new BDMProgramDetailsDenialList();

    // Read intake program application details
    final curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls intakePgmDtls =
      IntakeProgramApplicationFactory.newInstance().read(key);

    // BEGIN TASK 27147 - denied program username update and return denial list
    // Setting the denial date to disposed datetime to capture & display the
    // timestamp as when the application got denied
    for (final ProgramDenialDetails denialDetails : dtls.details) {

      final BDMProgramAuthDenialDetails details =
        new BDMProgramAuthDenialDetails();

      details.programDenialID =
        denialDetails.programDenialDtls.programDenialID;
      details.username = denialDetails.username;
      details.createdBy = denialDetails.programDenialDtls.createdBy;
      details.denialReason = intakePgmDtls.denialReason;
      details.disposedDateTime = intakePgmDtls.disposedDateTime;
      pgmDenialDetailsList.dtls.add(details);
    }
    // END TASK 27147

    return pgmDenialDetailsList;
  }

  @Override
  public void authorise(final AuthorisationDetails details)
    throws AppException, InformationalException {

    final ApplicationCase applicationCase =
      applicationCaseDAO.get(details.applicationCaseID);

    final curam.ca.gc.bdm.sl.application.impl.BDMProgramAuthorisation programAuthorisation =
      programAuthoriseMap
        .get(applicationCase.getApplicationCaseAdmin().getID());

    if (programAuthorisation != null) {
      programAuthorisation.authoriseProgram(details);
    }

    ProgramAuthorisationFactory.newInstance().authorise(details);

  }

}
