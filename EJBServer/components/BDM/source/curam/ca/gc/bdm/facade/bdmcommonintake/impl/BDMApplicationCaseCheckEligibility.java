package curam.ca.gc.bdm.facade.bdmcommonintake.impl;

import com.google.inject.Inject;
import curam.commonintake.codetable.impl.ELIGIBILITYCHECKSTRATEGYEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.impl.ProgramType;
import curam.workspaceservices.intake.impl.ProgramTypeDAO;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BDMApplicationCaseCheckEligibility extends
  curam.ca.gc.bdm.facade.bdmcommonintake.base.BDMApplicationCaseCheckEligibility {

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private ProgramTypeDAO programTypeDAO;

  @Inject
  private curam.commonintake.impl.ApplicationCaseCheckEligibility applicationCaseCheckEligibility;

  protected BDMApplicationCaseCheckEligibility() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Checking the eligibility for the Programs. This method is overidden from
   * the superclass ApplicationCaseCheckEligibility
   *
   *
   */
  @Override
  public void checkEligibility(final ApplicationCaseKey key)
    throws AppException, InformationalException {

    final ApplicationCase applicationCase =
      this.applicationCaseDAO.get(Long.valueOf(key.applicationCaseID));
    List<ProgramType> programTypesList = new ArrayList<>();

    if (applicationCase.getApplicationCaseAdmin()
      .getEligibilityCheckStrategy()
      .equals(ELIGIBILITYCHECKSTRATEGYEntry.PROGRAMSAPPLIEDFORONLY)) {
      final Set<Long> programTypeIDSet = new HashSet<Long>();
      for (final IntakeProgramApplication program : applicationCase
        .getPrograms()) {

        final long programTypeID =
          getIntakeProgramApplicationRecord(program).programTypeID;
        if (!programTypeIDSet.contains(programTypeID)) {
          programTypesList.add(program.getProgramType());
          programTypeIDSet.add(programTypeID);
        }

      }

    } else if (applicationCase.getApplicationCaseAdmin()
      .getEligibilityCheckStrategy()
      .equals(ELIGIBILITYCHECKSTRATEGYEntry.ALLPROGRAMS)) {
      programTypesList = this.programTypeDAO.listActiveByCaseConfiguration(
        applicationCase.getApplicationCaseAdmin());
    }
    validateProgramRulesConfiguration(programTypesList, applicationCase);
    this.applicationCaseCheckEligibility.checkEligibility(applicationCase,
      programTypesList);
  }

  /**
   * This method returns the IntakeProgramApplication Record.
   *
   * @param intakeProgramApplication
   * @return
   */
  private IntakeProgramApplicationDtls getIntakeProgramApplicationRecord(
    final IntakeProgramApplication intakeProgramApplication) {

    final curam.workspaceservices.intake.intf.IntakeProgramApplication programApplicaton =
      IntakeProgramApplicationFactory.newInstance();
    final IntakeProgramApplicationKey programApplicationKey =
      new IntakeProgramApplicationKey();
    programApplicationKey.intakeProgramApplicationID =
      intakeProgramApplication.getID();

    IntakeProgramApplicationDtls intakeProgramApplicationDtls = null;
    try {
      intakeProgramApplicationDtls =
        programApplicaton.read(programApplicationKey);
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }

    return intakeProgramApplicationDtls;

  }

}
