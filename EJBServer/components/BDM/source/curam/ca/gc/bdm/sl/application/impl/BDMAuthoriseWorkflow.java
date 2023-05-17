package curam.ca.gc.bdm.sl.application.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASETYPECODE;
import curam.commonintake.authorisation.facade.fact.ProgramAuthorisationFactory;
import curam.commonintake.authorisation.facade.intf.ProgramAuthorisation;
import curam.commonintake.authorisation.facade.struct.DenyProgramDetails;
import curam.commonintake.authorisation.struct.AuthorisationKey;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleDtlsList;
import curam.core.sl.entity.struct.CaseParticipantRole_eoCaseIDKey;
import curam.core.sl.entity.struct.ParticipantRoleIDAndTypeCodeKey;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.workspaceservices.codetable.IntakeProgramApplicationDenialReason;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * This class contains method which are called duing the Straight Through
 * Workflow process.
 *
 * @author vaibhav.patil
 *
 */
public class BDMAuthoriseWorkflow
  extends curam.ca.gc.bdm.sl.application.base.BDMAuthoriseWorkflow {

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  private static final int SAMPLEBENEFITPROGRAMTYPEID =
    BDMConstants.SAMPLEBENEFITPROGRAMTYPEID;

  private static final int DEPENDANTROGRAMTYPEID =
    BDMConstants.DEPENDANTROGRAMTYPEID;

  final String ELIGIBLE_PROGRAMS =
    "/DecisionDetails/DependantBenefitProgram/eligiblePrograms/Item";

  protected BDMAuthoriseWorkflow() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method authorizes the Sample benefit Program. This method is called
   * from the Straight Through Workflow.
   *
   * @param key AuthorisationKey
   */
  @Override
  public void authorise(final AuthorisationKey key)
    throws AppException, InformationalException {

    // BEGIN TASK 17646 - Authorising Sample Benefit Program

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = key.caseID;
    final ProgramAuthorisation programAuthorisation =
      ProgramAuthorisationFactory.newInstance();

    final List<CaseParticipantRoleDtls> memberCaseParticipantRoleDtlsList =
      getCPRMemberByType(key, CASEPARTICIPANTROLETYPE.PRIMARY);

    long caseID = 0l;
    final Set<Long> concernRoleIDSet = new HashSet<Long>();
    long concernRoleID = 0l;
    for (final IntakeProgramApplication program : getProgramsForApplicationByType(
      key, SAMPLEBENEFITPROGRAMTYPEID)) {

      concernRoleID = memberCaseParticipantRoleDtlsList.isEmpty() ? 0
        : memberCaseParticipantRoleDtlsList.get(0).participantRoleID;

      final curam.commonintake.authorisation.facade.struct.AuthorisationDetails authorisationDetails =
        new curam.commonintake.authorisation.facade.struct.AuthorisationDetails();
      authorisationDetails.applicationCaseID = key.caseID;
      authorisationDetails.intakeProgramApplicationID = program.getID();

      final IntakeProgramApplicationDtls programApplicationDtls =
        getIntakeProgramApplicationRecord(program);

      authorisationDetails.createNewInd = false;
      authorisationDetails.createOPNewInd = false;

      if (programApplicationDtls.status
        .equals(IntakeProgramApplicationStatus.PENDING)) {

        authorisationDetails.primaryClientID = concernRoleID;
        CaseParticipantRoleDtlsList participantRoleDtlsList =
          new CaseParticipantRoleDtlsList();
        final ParticipantRoleIDAndTypeCodeKey roleIDAndTypeCodeKey =
          new ParticipantRoleIDAndTypeCodeKey();
        roleIDAndTypeCodeKey.participantRoleID =
          authorisationDetails.primaryClientID;

        // Adding to Set
        concernRoleIDSet.add(roleIDAndTypeCodeKey.participantRoleID);
        if (caseID == 0) {

          roleIDAndTypeCodeKey.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
          participantRoleDtlsList = CaseParticipantRoleFactory.newInstance()
            .searchByParticipantRoleIDTypeCode(roleIDAndTypeCodeKey);

          for (final CaseParticipantRoleDtls caseParticipantRoleDtls : participantRoleDtlsList.dtls) {
            if (caseHeaderDAO.get(caseParticipantRoleDtls.caseID)
              .getCaseType().toString().equals(CASETYPECODE.INTEGRATEDCASE)) {
              caseID = caseParticipantRoleDtls.caseID;
              break;
            }
          }
        }
        if (caseID != 0l) {
          authorisationDetails.caseTabList = new Long(caseID).toString();
          Trace.kTopLevelLogger
            .info("Authorizing the Sample Benefit Program for ConcernRoleID:"
              + authorisationDetails.primaryClientID
              + " And Reusing the IC Case :" + caseID);
          programAuthorisation.authorise(authorisationDetails);
          concernRoleIDSet.add(authorisationDetails.primaryClientID);
          break;
        } else {

          Trace.kTopLevelLogger
            .info("Authorizing the Sample Benefit Program for ConcernRoleID:"
              + authorisationDetails.primaryClientID);

          authorisationDetails.applicationCaseID = key.caseID;
          authorisationDetails.intakeProgramApplicationID = program.getID();

          authorisationDetails.primaryClientID = concernRoleID;
          authorisationDetails.createNewInd = true;
          authorisationDetails.createOPNewInd = false;
          programAuthorisation.authorise(authorisationDetails);
        }
      }
    }
  }

  /**
   * This method returns the list of Case Participant Roles by the Type i.e. PRI
   * or MEM.
   *
   * @param key
   * @param cprType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private List<CaseParticipantRoleDtls>

    getCPRMemberByType(final AuthorisationKey key, final String cprType)
      throws AppException, InformationalException {

    final CaseParticipantRole_eoCaseIDKey eoCaseIDKey =
      new CaseParticipantRole_eoCaseIDKey();
    eoCaseIDKey.caseID = key.caseID;
    final CaseParticipantRoleDtlsList dtlsList =
      CaseParticipantRoleFactory.newInstance().searchByCaseID(eoCaseIDKey);

    final List<CaseParticipantRoleDtls> primaryCaseParticipantRoleDtlsList =
      dtlsList.dtls.stream().filter(cpr -> cpr.typeCode.equals(cprType))
        .collect(Collectors.toList());
    return primaryCaseParticipantRoleDtlsList;
  }

  /**
   * This method authorizes the all the Dependant Programs.
   *
   * key ApplicationCaseKey
   */
  @Override
  public void authoriseDependantBenefitProgram(final AuthorisationKey key)
    throws AppException, InformationalException {

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = key.caseID;
    final ProgramAuthorisation programAuthorisation =
      ProgramAuthorisationFactory.newInstance();

    final List<CaseParticipantRoleDtls> memberCaseParticipantRoleDtlsList =
      getCPRMemberByType(key, CASEPARTICIPANTROLETYPE.MEMBER);

    long caseID = 0l;
    final Set<Long> concernRoleIDSet = new HashSet<Long>();

    Set<Long> eligibleMemberConcernRoleIDSet = new HashSet<Long>();

    final List<IntakeProgramApplication> applicationDtlsList =
      getProgramsForApplicationByType(key, DEPENDANTROGRAMTYPEID);

    if (!applicationDtlsList.isEmpty()) {
      eligibleMemberConcernRoleIDSet = getEligibleMemberList(key.caseID);
    }

    if (!memberCaseParticipantRoleDtlsList.isEmpty()) {

      for (final Long concernRoleID : eligibleMemberConcernRoleIDSet) {

        for (final IntakeProgramApplication program : applicationDtlsList) {

          final curam.commonintake.authorisation.facade.struct.AuthorisationDetails authorisationDetails =
            new curam.commonintake.authorisation.facade.struct.AuthorisationDetails();
          authorisationDetails.applicationCaseID = key.caseID;
          authorisationDetails.intakeProgramApplicationID = program.getID();

          final IntakeProgramApplicationDtls programApplicationDtls =
            getIntakeProgramApplicationRecord(program);

          authorisationDetails.createNewInd = false;
          authorisationDetails.createOPNewInd = false;

          if (programApplicationDtls.status
            .equals(IntakeProgramApplicationStatus.PENDING)) {

            authorisationDetails.primaryClientID = concernRoleID;
            CaseParticipantRoleDtlsList participantRoleDtlsList =
              new CaseParticipantRoleDtlsList();
            final ParticipantRoleIDAndTypeCodeKey roleIDAndTypeCodeKey =
              new ParticipantRoleIDAndTypeCodeKey();
            roleIDAndTypeCodeKey.participantRoleID =
              authorisationDetails.primaryClientID;

            // Adding to Set
            concernRoleIDSet.add(roleIDAndTypeCodeKey.participantRoleID);
            if (caseID == 0) {

              roleIDAndTypeCodeKey.typeCode = CASEPARTICIPANTROLETYPE.MEMBER;
              participantRoleDtlsList =
                CaseParticipantRoleFactory.newInstance()
                  .searchByParticipantRoleIDTypeCode(roleIDAndTypeCodeKey);

              for (final CaseParticipantRoleDtls caseParticipantRoleDtls : participantRoleDtlsList.dtls) {
                if (caseHeaderDAO.get(caseParticipantRoleDtls.caseID)
                  .getCaseType().toString()
                  .equals(CASETYPECODE.INTEGRATEDCASE)) {
                  caseID = caseParticipantRoleDtls.caseID;
                  break;
                }
              }
            }
            if (caseID != 0l) {
              authorisationDetails.caseTabList = new Long(caseID).toString();
              Trace.kTopLevelLogger
                .info("Authorizing the Dependant Program for ConcernRoleID:"
                  + authorisationDetails.primaryClientID);
              programAuthorisation.authorise(authorisationDetails);
              concernRoleIDSet.add(authorisationDetails.primaryClientID);
              break;
            }
          }
        }
      }
    }
  }

  /**
   * This method Denies the Application Program for the individuls who are not
   * Eligible to receive the benefit.
   *
   * @key AuthorisationKey
   *
   */
  @Override
  public void denyProgramForIneligibleMember(final AuthorisationKey key)
    throws AppException, InformationalException {

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = key.caseID;
    final ProgramAuthorisation programAuthorisation =
      ProgramAuthorisationFactory.newInstance();

    final List<CaseParticipantRoleDtls> memberCaseParticipantRoleDtlsList =
      getCPRMemberByType(key, CASEPARTICIPANTROLETYPE.MEMBER);

    Set<Long> eligibleMemberConcernRoleIDSet = new HashSet<Long>();
    final Set<Long> inEligibleMemberConcernRoleIDSet = new HashSet<Long>();

    final List<IntakeProgramApplication> applicationDtlsList =
      getProgramsForApplicationByType(key, DEPENDANTROGRAMTYPEID);

    if (!applicationDtlsList.isEmpty()) {
      eligibleMemberConcernRoleIDSet = getEligibleMemberList(key.caseID);

    }

    // Prepare ineligible member list
    for (final CaseParticipantRoleDtls participantRoleDtls : memberCaseParticipantRoleDtlsList) {

      if (!eligibleMemberConcernRoleIDSet
        .contains(participantRoleDtls.participantRoleID)) {
        inEligibleMemberConcernRoleIDSet
          .add(participantRoleDtls.participantRoleID);
      }
    }

    final int memberCount = inEligibleMemberConcernRoleIDSet.size();
    if (!memberCaseParticipantRoleDtlsList.isEmpty()) {

      // Bug 59157 Fixed to deny once
      int pendingCount = 0;
      for (final IntakeProgramApplication program : applicationDtlsList) {

        final IntakeProgramApplicationDtls programApplicationDtls =
          getIntakeProgramApplicationRecord(program);

        if (programApplicationDtls.status
          .equals(IntakeProgramApplicationStatus.PENDING)) {
          pendingCount++;
        }
      }

      // Bug 59157 no need to deny again
      final int denyCount =
        pendingCount - eligibleMemberConcernRoleIDSet.size();
      if (denyCount <= 0) {
        return;
      }

      for (int i = 0; i < denyCount; i++) {
        for (final IntakeProgramApplication program : applicationDtlsList) {

          final IntakeProgramApplicationDtls programApplicationDtls =
            getIntakeProgramApplicationRecord(program);

          if (programApplicationDtls.status
            .equals(IntakeProgramApplicationStatus.PENDING)) {

            // Deny Program and continue
            final DenyProgramDetails denyProgramDetails =
              new DenyProgramDetails();

            denyProgramDetails.denialDtls.reason =
              IntakeProgramApplicationDenialReason.CLIENTINELIGIBLE;
            denyProgramDetails.denialDtls.intakeProgramApplicationID =
              programApplicationDtls.intakeProgramApplicationID;

            denyProgramDetails.intakeProgramVersionNo =
              program.getVersionNo();
            Trace.kTopLevelLogger
              .info("Denied Dependant Program For IntProgAppID :"
                + denyProgramDetails.denialDtls.intakeProgramApplicationID);
            programAuthorisation.denyProgram(denyProgramDetails);
            break;
          }
        }
      }
    }
  }

  /**
   * This method builds the list of all the Ineligible Dependant Program
   * members.
   *
   * @param applicationCaseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Set<Long> getEligibleMemberList(final long applicationCaseID)
    throws AppException, InformationalException {

    // To get correct results, running the check ELigibility again as there
    // could evidence changes happened which may cause member be to eligible or
    // ineligible.
    // Run the Check Eligibility for the App Case.
    // final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    // applicationCaseKey.applicationCaseID = applicationCaseID;
    // BDMApplicationCaseCheckEligibilityFactory.newInstance()
    // .checkEligibility(applicationCaseKey);

    final Set<Long> concernRoleIDSet = new HashSet<Long>();

    final Document document =
      new BDMUtil().getDocumentFromCheckEligiblityResultsXML(
        applicationCaseID, BDMConstants.DEPENDANTBENEFITPROGRAMNAME);

    final NodeList eligiblePrograms =
      new BDMUtil().getNodeListValue(document, ELIGIBLE_PROGRAMS);

    if (eligiblePrograms == null) {
      return new HashSet<Long>();
    }

    final int count = eligiblePrograms.getLength();
    for (int i = 1; i < count + 1; i++) {

      final String xPathString =
        ELIGIBLE_PROGRAMS + "[" + i + "]/eligibleMemberConcernRoleID";

      final Node eligibleMemberConcernRoleID =
        new BDMUtil().getNodeValue(document, xPathString);

      if (eligibleMemberConcernRoleID != null) {
        concernRoleIDSet
          .add(new Long(eligibleMemberConcernRoleID.getTextContent()));
      }
    }

    return concernRoleIDSet;
  }

  /**
   * This method returns the IntakeProgramApplication List by the program Type.
   *
   * @param key
   * @param programTypeID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private List<IntakeProgramApplication> getProgramsForApplicationByType(
    final AuthorisationKey key, final long programTypeID)
    throws AppException, InformationalException {

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = key.caseID;

    final curam.commonintake.impl.ApplicationCase applicationCase =
      this.applicationCaseDAO
        .get(Long.valueOf(applicationCaseKey.applicationCaseID));

    final List<IntakeProgramApplication> allProgramsList =
      applicationCase.getPrograms();

    final List<IntakeProgramApplication> programDetailsList =
      allProgramsList.stream()
        .filter(program -> getIntakeProgramApplicationRecord(
          program).programTypeID == programTypeID)
        .collect(Collectors.toList());

    return programDetailsList;
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
