package curam.ca.gc.bdmoas.test.facade.applicationcase.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMSIGNATURETYPE;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMApplicationCaseDetailsList;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.facade.commonintake.fact.BDMOASApplicationCaseFactory;
import curam.ca.gc.bdmoas.facade.commonintake.fact.BDMOASApplicationCaseProgramFactory;
import curam.ca.gc.bdmoas.facade.commonintake.intf.BDMOASApplicationCaseProgram;
import curam.ca.gc.bdmoas.facade.commonintake.struct.BDMOASProgramDetailsList;
import curam.ca.gc.bdmoas.facade.fact.BDMOASProgramAuthorisationFactory;
import curam.ca.gc.bdmoas.facade.intf.BDMOASProgramAuthorisation;
import curam.ca.gc.bdmoas.facade.struct.BDMOASCloseAllBenefitsDetails;
import curam.ca.gc.bdmoas.sl.tab.impl.BDMOASApplicationCaseMenuLoader;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.PROGAPPREOPENREASONEntry;
import curam.commonintake.authorisation.entity.struct.ProgramDenialDtls;
import curam.commonintake.authorisation.facade.struct.AuthorisationDetails;
import curam.commonintake.authorisation.facade.struct.DenyProgramDetails;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.struct.ApplicationCaseProgramDetails;
import curam.commonintake.facade.struct.ProgramDetails;
import curam.commonintake.facade.struct.ProgramReopenKey;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.MenuState;
import curam.util.tab.impl.MenuStateFactory;
import curam.workspaceservices.codetable.IntakeProgramApplicationStatus;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationDenialReasonEntry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Junit Test Case class for Application Case
 *
 * @author naveen.garg
 *
 */
public class BDMOASApplicationCaseTest extends BDMOASCaseTest {

  public static final String SIGNATURE_TYPE = "signatureType";

  public static final String DATE_SIGNED = "dateSigned";

  public static final String PARTICIPANT = "participant";

  public static final long APP_CASE_ADMIN_ID = 920000;

  public static final long OAS_PROG_ID = 920000;

  public static final long GIS_PROG_ID = 920001;

  public static final String DATE_OF_SIGN = "19980101";

  public static final String COMMENTS = "comments";

  /**
   * Test that addProgramsInd and CloseAllBenefitsInd are false for an
   * application case
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void listProgramsTest() throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    BDMOASProgramDetailsList list = BDMOASApplicationCaseFactory.newInstance()
      .listPrograms(applicationCaseKey);

    assertFalse(list.closeAllBenefitsInd);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = OAS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);
    list = BDMOASApplicationCaseFactory.newInstance()
      .listPrograms(applicationCaseKey);

    assertFalse(list.dtls.addProgramsInd);

  }

  /**
   * Test for manually close Benefit type on Application Case
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void denyProgramTest() throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = OAS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = GIS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    final DenyProgramDetails deny = new DenyProgramDetails();
    final ProgramDenialDtls denialDtls = new ProgramDenialDtls();
    deny.denialDtls = denialDtls;
    denialDtls.comments = COMMENTS;
    denialDtls.denialDate = getToday();
    denialDtls.reason =
      IntakeProgramApplicationDenialReasonEntry.DUPLICATE.getCode();

    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();

    final Iterator<ProgramDetails> progDtlsIter = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();

    while (progDtlsIter.hasNext()) {
      final ProgramDetails dtl = progDtlsIter.next();

      deny.intakeProgramVersionNo = dtl.versionNo;
      denialDtls.intakeProgramApplicationID = dtl.programID;
      programAuth.denyProgram(deny);

    }

    while (progDtlsIter.hasNext()) {
      final ProgramDetails dtl = progDtlsIter.next();

      assertEquals(dtl.status, IntakeProgramApplicationStatus.DENIED);

      break;
    }

  }

  /**
   * Test for menuLoaderTest functionality on Application Case
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void menuLoaderTest() throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = OAS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = GIS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    final BDMOASApplicationCaseMenuLoader mm =
      new BDMOASApplicationCaseMenuLoader();
    MenuState ms = MenuStateFactory.getMenuStateInstance();
    final Map<String, String> map = new HashMap<>();
    map.put("caseID", Long.toString(applicationCaseKey.applicationCaseID));
    final String[] idsToUp = { };
    mm.loadMenuState(ms, map, idsToUp);
    int number = 0;
    for (int i = 0; i < ms.getTestResult().length; i++) {
      if (ms.getTestResult()[i].contains("CloseAllBenefits")) {
        number = i;
        break;
      }
    }
    assertEquals("CloseAllBenefits:true,true", ms.getTestResult()[number]);

    final DenyProgramDetails deny = new DenyProgramDetails();
    final ProgramDenialDtls denialDtls = new ProgramDenialDtls();
    deny.denialDtls = denialDtls;
    denialDtls.comments = COMMENTS;
    denialDtls.denialDate = getToday();
    denialDtls.reason =
      IntakeProgramApplicationDenialReasonEntry.DUPLICATE.getCode();

    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();

    final Iterator<ProgramDetails> iterProgDtls = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();

    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();

      deny.intakeProgramVersionNo = dtl.versionNo;
      denialDtls.intakeProgramApplicationID = dtl.programID;
      programAuth.denyProgram(deny);

    }

    ms = mm.loadMenuState(ms, map, idsToUp);
    number = 0;
    for (int i = 0; i < ms.getTestResult().length; i++) {
      if (ms.getTestResult()[i].contains("CloseAllBenefits")) {
        number = i;
        break;
      }
    }
    assertEquals("CloseAllBenefits:false,true", ms.getTestResult()[number]);
  }

  /**
   * Test for CloseAllBenefit functionality on Application Case
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void closeAllProgramTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = OAS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = GIS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    final BDMOASCloseAllBenefitsDetails closeAll =
      new BDMOASCloseAllBenefitsDetails();

    closeAll.comments = COMMENTS;
    closeAll.denialReason =
      IntakeProgramApplicationDenialReasonEntry.DUPLICATE.getCode();
    closeAll.caseID = det.applicationCaseID;

    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();
    programAuth.closeAllBenefits(closeAll);

    final Iterator<ProgramDetails> iterProgDtls = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();
    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();

      assertEquals(dtl.status, IntakeProgramApplicationStatus.DENIED);

    }

  }

  /**
   * Test for GIS Benefit type Application Case is authorized
   * successfully
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void authorizeGISWhenOASPendingTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = OAS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = GIS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCaseKey.applicationCaseID,
        primary.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<>();

    attributes.put(SIGNATURE_TYPE, BDMSIGNATURETYPE.SIGNED);
    attributes.put(DATE_SIGNED, DATE_OF_SIGN);
    attributes.put(PARTICIPANT, String.valueOf(cprObj.caseParticipantRoleID));

    this.createEvidence(applicationCaseKey.applicationCaseID,
      primary.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION, attributes, getToday());

    final AuthorisationDetails authorisationDetails =
      new AuthorisationDetails();

    final Iterator<ProgramDetails> iterProgDtls = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();
    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();
      authorisationDetails.intakeProgramApplicationID = dtl.programID;

    }

    authorisationDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    authorisationDetails.createNewInd = Boolean.TRUE;
    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        programAuth.authorise(authorisationDetails);
      });

    assertTrue(
      curam.ca.gc.bdmoas.message.BDMOASAPPLICATIONCASE.ERR_GIS_OAS_ISSUE
        .getMessageText().equals(exception.getMessage()));

  }

  /**
   * Test for GIS Benefit type Application Case is authorized
   * successfully
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void authorizeGISTest() throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = GIS_PROG_ID; // GIS
    ApplicationCaseFactory.newInstance().addProgram(det);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCaseKey.applicationCaseID,
        primary.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<>();

    attributes.put(SIGNATURE_TYPE, BDMSIGNATURETYPE.SIGNED);
    attributes.put(DATE_SIGNED, DATE_OF_SIGN);
    attributes.put(PARTICIPANT, String.valueOf(cprObj.caseParticipantRoleID));

    this.createEvidence(applicationCaseKey.applicationCaseID,
      primary.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION, attributes, getToday());

    final AuthorisationDetails authorisationDetails =
      new AuthorisationDetails();

    final Iterator<ProgramDetails> iterProgDtls = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();
    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();
      authorisationDetails.intakeProgramApplicationID = dtl.programID;
    }
    authorisationDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    authorisationDetails.createNewInd = Boolean.TRUE;
    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();
    programAuth.authorise(authorisationDetails);

    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();
      assertEquals(dtl.status,
        IntakeProgramApplicationStatus.AUTHORIZATIONINPROGRESS);
    }

  }

  /**
   * Test for OAS Benefit type Application Case is authorized
   * successfully
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void authorizeOASSTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = OAS_PROG_ID; // OAS
    ApplicationCaseFactory.newInstance().addProgram(det);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(applicationCaseKey.applicationCaseID,
        primary.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<>();

    attributes.put(SIGNATURE_TYPE, BDMSIGNATURETYPE.SIGNED);
    attributes.put(DATE_SIGNED, DATE_OF_SIGN);
    attributes.put(PARTICIPANT, String.valueOf(cprObj.caseParticipantRoleID));

    this.createEvidence(applicationCaseKey.applicationCaseID,
      primary.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION, attributes, getToday());

    final AuthorisationDetails authorisationDetails =
      new AuthorisationDetails();

    final Iterator<ProgramDetails> iterProgDtls = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();
    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();
      authorisationDetails.intakeProgramApplicationID = dtl.programID;
    }
    authorisationDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    authorisationDetails.createNewInd = Boolean.TRUE;
    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();
    programAuth.authorise(authorisationDetails);

    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();
      assertEquals(dtl.status,
        IntakeProgramApplicationStatus.AUTHORIZATIONINPROGRESS);
    }

  }

  /**
   * Test to validate reopen functionality for OAS Application Case
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void reopenTest() throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);

    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = OAS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = getToday();
    det.programTypeID = GIS_PROG_ID;
    ApplicationCaseFactory.newInstance().addProgram(det);

    final BDMOASCloseAllBenefitsDetails closeAll =
      new BDMOASCloseAllBenefitsDetails();

    closeAll.comments = COMMENTS;
    closeAll.denialReason =
      IntakeProgramApplicationDenialReasonEntry.DUPLICATE.getCode();
    closeAll.caseID = det.applicationCaseID;

    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();
    programAuth.closeAllBenefits(closeAll);

    final Iterator<ProgramDetails> progDtlsIter = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();
    final ProgramReopenKey key = new ProgramReopenKey();

    long oasProgramId = 0;
    long gisid = 0;
    while (progDtlsIter.hasNext()) {
      final ProgramDetails dtl = progDtlsIter.next();

      assertEquals(dtl.status, IntakeProgramApplicationStatus.DENIED);
      if (dtl.name.equals("GIS Benefit"))
        gisid = dtl.programID;
      else {
        oasProgramId = dtl.programID;
      }

    }

    final BDMOASApplicationCaseProgram bDMOASApplicationCaseProgram =
      BDMOASApplicationCaseProgramFactory.newInstance();

    key.comments = COMMENTS;
    key.reopenDate = getYesterday();
    key.reopenReason = PROGAPPREOPENREASONEntry.ADMINISTRATIVEERROR.getCode();
    key.intakeProgramApplicationID = gisid;

    try {
      bDMOASApplicationCaseProgram.reopen(key);
    } catch (final Exception e) {
      System.out.println(e.getMessage());
      // The Reopen date should not be before the application submission date.
    }

    key.comments = COMMENTS;
    key.reopenDate = getToday();
    key.reopenReason = PROGAPPREOPENREASONEntry.ADMINISTRATIVEERROR.getCode();
    key.intakeProgramApplicationID = oasProgramId;
    bDMOASApplicationCaseProgram.reopen(key);

    key.intakeProgramApplicationID = gisid;
    bDMOASApplicationCaseProgram.reopen(key);

    while (progDtlsIter.hasNext()) {
      final ProgramDetails dtl = progDtlsIter.next();

      assertEquals(dtl.status, IntakeProgramApplicationStatus.PENDING);

    }

  }

  /**
   * Test that indicators are correct
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void listApplicationCaseByConcernRoleTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult primary = this.registerPerson();

    this.createApplicationCase(primary.registrationIDDetails.concernRoleID,
      APP_CASE_ADMIN_ID);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID =
      primary.registrationIDDetails.concernRoleID;

    final BDMApplicationCaseDetailsList list = BDMOASApplicationCaseFactory
      .newInstance().listApplicationCaseByConcernRole(concernRoleKey);

    final boolean programsOnlyInd = list.programsOnlyInd;
    final boolean programsExpiryDateNotExistInd =
      list.programsExpiryDateNotExistInd;
    assertTrue(programsOnlyInd);
    assertFalse(programsExpiryDateNotExistInd);

  }

}
