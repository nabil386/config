package curam.ca.gc.bdmoas.facade.commonintake.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcommonintake.fact.BDMApplicationCaseProgramFactory;
import curam.ca.gc.bdm.facade.bdmcommonintake.intf.BDMApplicationCaseProgram;
import curam.ca.gc.bdmoas.message.impl.BDMOASAPPLICATIONCASEExceptionCreator;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.commonintake.facade.struct.ProgramReopenKey;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.participant.impl.ConcernRole;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.ValidationHelper;
import curam.util.type.Date;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.impl.IntakeProgramApplicationAdapter;
import curam.workspaceservices.intake.impl.IntakeProgramApplicationDAO;
import curam.workspaceservices.intake.impl.ProgramType;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author naveen.garg
 * OOTB class Customize as per Task 94709
 * Manually reopen the benefit type of an OAS Application
 */
public class BDMOASApplicationCaseProgram extends
  curam.ca.gc.bdmoas.facade.commonintake.base.BDMOASApplicationCaseProgram {

  private static final IntakeProgramApplicationAdapter adapter =
    new IntakeProgramApplicationAdapter();

  public BDMOASApplicationCaseProgram() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private IntakeProgramApplicationDAO intakeProgramApplicationDAO;

  private static final String GISPROGRAMTYPE =
    "GuaranteedIncomeSupplementProgram";

  /**
   * @decription: Task 94709 Manually reopen the benefit type of an OAS
   * Application. Add business validations
   * @param :@param key
   * @param :@return
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public void reopen(final ProgramReopenKey key)
    throws AppException, InformationalException {

    final IntakeProgramApplication program =
      this.intakeProgramApplicationDAO.get(key.intakeProgramApplicationID);

    if (GISPROGRAMTYPE
      .equals(program.getProgramType().getProgramTypeReference())) {
      validateOASBenefitStatus(program);
    }
    this.validateReopenDetails(program, key.reopenDate);
    this.checkForApprovedIntakeProgramApplication(program,
      program.getProgramType());
    ValidationHelper.failIfErrorsExist();

    final BDMApplicationCaseProgram bdmApplicationCaseProgram =
      BDMApplicationCaseProgramFactory.newInstance();
    bdmApplicationCaseProgram.reopen(key);
  }

  /**
   * System should not allow user to reopen benefit with a date before
   * application submission date.
   *
   * @param program
   * @param reopenDate
   * @throws InformationalException
   * @throws AppException
   */
  private void validateReopenDetails(final IntakeProgramApplication program,
    final Date reopenDate) throws InformationalException, AppException {

    final List<Long> cases = program.listAssociatedCases();
    final ApplicationCase aplicationCase = !cases.isEmpty()
      ? (ApplicationCase) this.applicationCaseDAO.get(cases.get(0)) : null;

    final Date submittedDate = aplicationCase != null
      ? new Date(aplicationCase.getSubmittedDateTime().asLong())
      : new Date(Date.kZeroDate.asLong());
    if (reopenDate.before(submittedDate)) {
      ValidationHelper
        .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
          .ERR_REOPEN_DATE_MUST_NOT_BE_BEFORE_APPLICATION_SUBMISSION_DATE());
    }

  }

  /**
   * System should not allow user to reopen GIS benefit if OAS Pension is in
   * either closed or withdrawn Status.
   *
   * @param programToReopen
   * @throws AppException
   * @throws InformationalException
   */
  private void
    validateOASBenefitStatus(final IntakeProgramApplication programToReopen)
      throws AppException, InformationalException {

    final List<Long> cases = programToReopen.listAssociatedCases();
    final ApplicationCase aplicationCase = !cases.isEmpty()
      ? (ApplicationCase) this.applicationCaseDAO.get(cases.get(0)) : null;
    final Iterator<IntakeProgramApplication> intakeProgAppIterator =
      null != aplicationCase ? aplicationCase.getPrograms().iterator()
        : new ArrayList<IntakeProgramApplication>().iterator();
    while (intakeProgAppIterator.hasNext()) {
      final IntakeProgramApplication intakeProgramApplication =
        intakeProgAppIterator.next();
      if (intakeProgramApplication.getID() != programToReopen.getID()) {
        if (intakeProgramApplication.getLifecycleState()
          .equals(IntakeProgramApplicationStatusEntry.WITHDRAWN)
          || intakeProgramApplication.getLifecycleState()
            .equals(IntakeProgramApplicationStatusEntry.DENIED)) {
          ValidationHelper
            .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
              .ERR_GIS_REOPEN_WHEN_OAS_BENEFIT_CLOSED());
        }

      }

    }
  }

  /**
   * System should not allow to reopen benefit if the client already has the
   * same benefit in approved status or the client is already receiving the
   * benefit.
   *
   * @param programToReopen
   * @param programType
   */
  private void checkForApprovedIntakeProgramApplication(
    final IntakeProgramApplication programToReopen,
    final ProgramType programType) {

    final List<Long> cases = programToReopen.listAssociatedCases();
    final ApplicationCase aplicationCase = !cases.isEmpty()
      ? (ApplicationCase) this.applicationCaseDAO.get(cases.get(0)) : null;

    final List<CaseParticipantRole> caseMembers = null != aplicationCase
      ? aplicationCase.listActiveCaseMembers() : new ArrayList<>();
    final Iterator caseMemIterator = caseMembers.iterator();

    while (caseMemIterator.hasNext()) {
      final CaseParticipantRole caseParticipantRole =
        (CaseParticipantRole) caseMemIterator.next();
      final ConcernRole concernRole = caseParticipantRole.getConcernRole();

      final IntakeProgramApplicationDtls[] dtl =
        adapter.searchByProgramTypeStatusAndConcernRole(programType.getID(),
          IntakeProgramApplicationStatusEntry.APPROVED.getCode(),
          concernRole.getID());

      for (final IntakeProgramApplicationDtls intakeProgramApplicationArray : dtl) {
        final IntakeProgramApplication intakeProgramApplication =
          this.intakeProgramApplicationDAO
            .get(intakeProgramApplicationArray.intakeProgramApplicationID);
        final Iterator<Long> assocCaseIterator =
          intakeProgramApplication.listAssociatedCases().iterator();
        while (assocCaseIterator.hasNext()) {
          final Long caseID = assocCaseIterator.next();
          final CaseHeader caseHeader = this.caseHeaderDAO.get(caseID);
          if (caseHeader.getCaseType()
            .equals(CASETYPECODEEntry.APPLICATION_CASE)) {
            ValidationHelper
              .addValidationError(BDMOASAPPLICATIONCASEExceptionCreator
                .ERR_REOPEN_WHEN_BENEFIT_ALREADY_APPROVED());
          }
        }
      }
    }
  }

}
