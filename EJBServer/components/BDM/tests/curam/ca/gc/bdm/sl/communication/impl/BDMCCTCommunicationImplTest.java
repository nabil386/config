package curam.ca.gc.bdm.sl.communication.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCCTSUBMITOPT;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceSearchKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCancelCommunicationDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceWizardKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMReadProFormaCommunicationCaseMember;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchDetailsList;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.core.facade.struct.ReadProFormaCommKey;
import curam.core.facade.struct.WizardStateID;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMCCTCommunicationImplTest extends CuramServerTestJUnit4 {

  final curam.ca.gc.bdm.sl.communication.intf.BDMCommunication bdmCommunicationObj =
    curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
      .newInstance();

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testSearchTemplates() throws Exception {

    // input parameter
    final BDMTemplateSearchKey key = new BDMTemplateSearchKey();
    key.templateIDName = BDMConstants.EMPTY_STRING;

    // BDMCommunication facade layer object
    final curam.ca.gc.bdm.facade.communication.intf.BDMCommunication bdmCommunicationObj =
      curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationFactory
        .newInstance();

    // return struct
    BDMTemplateSearchDetailsList bdmTemplateSearchDetails =
      new BDMTemplateSearchDetailsList();

    bdmTemplateSearchDetails = bdmCommunicationObj.searchTemplates(key);

    assertTrue(bdmTemplateSearchDetails.dtls.size() > 0);
  }

  @Test
  public void testreadProFormaAndCaseMember() throws Exception {

    // input parameter
    final ReadProFormaCommKey key = new ReadProFormaCommKey();
    key.proFormaCommKey.communicationID = createTestCommunication();

    // BDMCommunication facade layer object
    final curam.ca.gc.bdm.facade.communication.intf.BDMCommunication bdmCommunicationObj =
      curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationFactory
        .newInstance();

    // return struct
    BDMReadProFormaCommunicationCaseMember bdmreadProForma =
      new BDMReadProFormaCommunicationCaseMember();

    bdmreadProForma = bdmCommunicationObj.readProFormaAndCaseMember(key);

  }

  @Test
  public void testcancelCommunication() throws Exception {

    // input parameter
    final BDMCancelCommunicationDetails details =
      new BDMCancelCommunicationDetails();
    final BDMConcernRoleCommunicationKey bdmcrcKey =
      new BDMConcernRoleCommunicationKey();
    final BDMConcernRoleCommunicationDtls bdmCRCDtls =
      BDMConcernRoleCommunicationFactory.newInstance().read(bdmcrcKey);

    details.canceledInd = true;
    details.cancelReason = bdmCRCDtls.cancelReason;
    // BDMCommunication facade layer object
    final curam.ca.gc.bdm.facade.communication.intf.BDMCommunication bdmCommunicationObj =
      curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationFactory
        .newInstance();
    bdmCommunicationObj.cancelCommunication(details);

  }

  // @Test
  // public void testlistTemplateByTypeAndParticipant() throws Exception {
  //
  // // input parameter
  // final BDMListProFormaTemplateByTypeAndParticipantKey key =
  // new BDMListProFormaTemplateByTypeAndParticipantKey();
  // final PDCPersonDetails pdcPersonDetails =
  // bdmEvidenceUtilsTest.createPDCPerson();
  // final long concernRoleID = pdcPersonDetails.concernRoleID;
  //
  //
  // key.dtls.caseID = caseID;
  // key.dtls.participantRoleID = person.registrationIDDetails.concernRoleID;
  // key.dtls.templateType = BDMGCNotifyTemplateType.BDM_GC_NF;
  // // BDMCommunication facade layer object
  // final curam.ca.gc.bdm.facade.communication.intf.BDMCommunication
  // bdmCommunicationObj =
  // curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationFactory
  // .newInstance();
  // bdmCommunicationObj.listTemplateByTypeAndParticipant(key);
  //
  // }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    bdmUtil.createContactPreferenceEvidence(
      registrationIDDetails.concernRoleID, BDMLANGUAGE.ENGLISHL,
      BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  private long createTestCommunication()
    throws AppException, InformationalException {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.VIEW;

    final BDMConcernRoleCommunication comm =
      BDMConcernRoleCommunicationFactory.newInstance();

    final BDMTemplateDetails bdmTemplateDetails =
      bdmCommunicationObj.createCorrespondence(wizardKey);

    final BDMCorrespondenceSearchKey key = new BDMCorrespondenceSearchKey();
    key.workItemID = bdmTemplateDetails.workItemID;
    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
      comm.getCorrespondenceByWorkItemID(key);

    return bdmConcernRoleCommunicationDtls.communicationID;
  }

}
