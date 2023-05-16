package curam.ca.gc.bdm.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationKey;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidence;
import curam.ca.gc.bdm.sl.application.fact.BDMAuthoriseWorkflowFactory;
import curam.commonintake.authorisation.impl.ProgramAuthorisationEvents;
import curam.commonintake.authorisation.struct.AuthorisationKey;
import curam.commonintake.impl.ApplicationCase;
import curam.piwrapper.caseconfiguration.impl.Product;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.impl.ProgramType;

public class BDMProgramAuthorisationEventListener
  extends ProgramAuthorisationEvents {

  @Inject
  BDMBankEvidence bdmBankEvidence;

  @Override
  public void postAuthorisation(final ApplicationCase applicationCase,
    final IntakeProgramApplication intakeProgramApplication)
    throws InformationalException {

    try {
      final ProgramType programType =
        intakeProgramApplication.getProgramType();

      final Product product = programType.getProduct();

      final BDMPaymentDestinationKey paymentDestinationKey =
        bdmBankEvidence.createBDMPaymentDestinationLink(
          applicationCase.getConcernRole().getID(), product.getID());

      if (paymentDestinationKey.paymentDestinationID != 0L) {
        Trace.kTopLevelLogger
          .info("BDMPaymentDestination link record created.");
      } else {
        Trace.kTopLevelLogger.info(
          "No bank account found - BDMPaymentDestination link record not created.");
      }

    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e.getMessage());
    }

  }

  @Override
  public void preAuthorisation(final ApplicationCase applicationCase,
    final IntakeProgramApplication intakeProgramApplication)
    throws InformationalException {

    // BEGIN TASK-17646 - Denying program for ineligible members
    final AuthorisationKey authorisationKey = new AuthorisationKey();
    authorisationKey.caseID = applicationCase.getID();
    try {
      BDMAuthoriseWorkflowFactory.newInstance()
        .denyProgramForIneligibleMember(authorisationKey);
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }
    // END TASK-17646

  }
}
