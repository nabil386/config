package curam.ca.gc.bdm.rest.bdmcctapi.impl;

import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.rest.bdmcctapi.fact.BDMCCTAPIFactory;
import curam.ca.gc.bdm.rest.bdmcctapi.intf.BDMCCTAPI;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTCorrespondenceStatusDetails;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTWorkItemStatusDetails;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Suite of tests for the BDMCCTAPI class.
 */
public class BDMCCTAPITest extends CuramServerTestJUnit4 {

  private static BDMCCTAPI testSubject;

  private ConcernRoleCommunication concernRoleCommObj;

  private BDMConcernRoleCommunication BDMConcernRoleCommObj;

  /**
   * Class set up function.
   */
  @BeforeClass
  public static void setUpClass() {

    testSubject = BDMCCTAPIFactory.newInstance();
  }

  /**
   * Set up function for unit tests.
   */
  @Before
  public void setUp() {

    concernRoleCommObj = ConcernRoleCommunicationFactory.newInstance();
    BDMConcernRoleCommObj = BDMConcernRoleCommunicationFactory.newInstance();
  }

  /**
   * Test for updateWorkItemStatus() where the workItemId passed in is empty.
   */
  @Test
  public void testUpdateWorkItemStatus_emptyWorkItemId() {

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.status = "ACTIVE_INCOMPLETE";
    request.username = "caseworker";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateWorkItemStatus(request));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  /**
   * Test for updateWorkItemStatus() where the status passed in is empty.
   */
  @Test
  public void testUpdateWorkItemStatus_emptyStatus() {

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "44";
    request.username = "caseworker";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateWorkItemStatus(request));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  /**
   * Test for updateWorkItemStatus() where the status passed in is empty.
   */
  @Test
  public void testUpdateWorkItemStatus_emptyUserName() {

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "44";
    request.status = "ACTIVE_INCOMPLETE";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateWorkItemStatus(request));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
  }

  /**
   * Test for updateWorkItemStatus() where the workItemId passed in does not exist
   * in Curam.
   */
  @Test
  public void testUpdateWorkItemStatus_nonexistentWorkItem() {

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "373";
    request.status = "ACTIVE_INCOMPLETE";
    request.username = "caseworker";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateWorkItemStatus(request));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  /**
   * Test for updateWorkItemStatus() where the workItemId passed in does not exist
   * in Curam.
   */
  @Test
  public void testUpdateWorkItemStatus_nonexistentTrackingNumber() {

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "373";
    request.status = "FAILED_OVERSIZED";
    request.username = "caseworker";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateWorkItemStatus(request));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  /**
   * Test updateWorkItemStatus for a scenario where the status provided does not
   * exist.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testUpdateWorkItemStatus_nonExistentWorkItemStatus()
    throws AppException, InformationalException {

    createTestCommunication(1, 1, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "ACTIF_COMPLET";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test updateWorkItemStatus where the status update has changed but is not
   * FINISHED.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testUpdateWorkItemStatus_updateWorkItemStatus()
    throws AppException, InformationalException {

    createTestCommunication(1, 1, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "PENDING_DELIVERY";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test updateWorkItemStatus for a scenario where the status is the same as the
   * status in the Curam tables for a workitem.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testUpdateWorkItemStatus_sameWorkItemStatus()
    throws AppException, InformationalException {

    createTestCommunication(1, 1, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "ACTIVE_COMPLETE";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test updateWorkItemStatus to a workitem to a CANCELED status.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testUpdateWorkItemStatus_cancelWorkItemStatus()
    throws AppException, InformationalException {

    createTestCommunication(1, 1, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "CANCELED";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test updateWorkItemStatus to update a workitem to a FINISHED status, where
   * the communication has a caseId of 0.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testUpdateWorkItemStatus_finishedWorkItemStatusZeroCaseId()
    throws AppException, InformationalException {

    createTestCommunication(1, 0, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "FINISHED";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test updateWorkItemStatus to update a workitem to a FINISHED status, where
   * the communication has a non-zero caseId but has an empty template name.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testUpdateWorkItemStatus_finishedWorkItemStatusNonZeroCaseIdEmptyTemplate()
      throws AppException, InformationalException {

    createTestCommunication(1, 1, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "FINISHED";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test updateWorkItemStatus to update a workitem to a FINISHED status, where
   * the communication has a non-zero caseId and is an interim application.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testUpdateWorkItemStatus_finishedWorkItemStatusNonZeroCaseIdInterimTemplate()
      throws AppException, InformationalException {

    createTestCommunication(1, 1, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "ISS-6592");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "FINISHED";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test updateWorkItemStatus to update a workitem to a FINISHED status, where
   * the communication has a non-zero caseId and is not an interim application
   * template.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testUpdateWorkItemStatus_finishedWorkItemStatusNonZeroCaseIdOtherTemplate()
      throws AppException, InformationalException {

    createTestCommunication(1, 1, COMMUNICATIONSTATUS.DRAFT);
    createTestBDMCommunication(1, 1, "TEST");

    final BDMCCTWorkItemStatusDetails request =
      new BDMCCTWorkItemStatusDetails();
    request.community = "Test";
    request.date = "1990/01/01";
    request.workitemid = "1";
    request.status = "FINISHED";
    request.username = "caseworker";

    testSubject.updateWorkItemStatus(request);
  }

  /**
   * Test for updateCorrespondenceStatus() where the correspondence ID passed in
   * is empty.
   */
  @Test
  public void testUpdateCorrespondenceStatus_emptyCorrespondenceID() {

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  /**
   * Test for updateCorrespondenceStatus() where the status passed in
   * is empty.
   */
  @Test
  public void testUpdateCorrespondenceStatus_emptyStatus() {

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "SomeID";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  /**
   * Test for updateCorrespondenceStatus() where the user passed in
   * is empty.
   */
  @Test
  public void testUpdateCorrespondenceStatus_emptyUser() {

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "SomeID";
    details.status = "ACTIVE_COMPLETE";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
  }

  /**
   * Test for updateCorrespondenceStatus() where the status is mismatched.
   */
  @Test
  public void testUpdateCorrespondenceStatus_mismatchBulk() {

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "SomeID";
    details.status = "ACTIVE_COMPLETE";
    details.username = "User";
    details.type = "JOBID";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
  }

  /**
   * Test for updateCorrespondenceStatus() where the status is mismatched.
   */
  @Test
  public void testUpdateCorrespondenceStatus_mismatchStatusBulkSuccess() {

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "SomeID";
    details.status = "BULK_SUCCESS";
    details.username = "User";
    details.type = "TRACKINGNO";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
  }

  /**
   * Test for updateCorrespondenceStatus() where the status is mismatched.
   */
  @Test
  public void testUpdateCorrespondenceStatus_mismatchStatusBulkFailure() {

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "SomeID";
    details.status = "BULK_FAILURE";
    details.username = "User";
    details.type = "WORKITEMID";

    final AppException e = assertThrows(AppException.class,
      () -> testSubject.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
  }

  /**
   * Helper function to create a workitem in the ConcernRoleCommunication table
   * prior to executing
   * test.
   *
   * @param communicationID - communicationID of test item
   * @param caseId - caseId of test item
   * @param communicationStatus - previous status of work item
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createTestCommunication(final int communicationID,
    final int caseId, final String communicationStatus)
    throws AppException, InformationalException {

    final ConcernRoleCommunicationDtls CRCDtls =
      new ConcernRoleCommunicationDtls();
    CRCDtls.communicationID = communicationID;
    CRCDtls.proFormaInd = false;
    CRCDtls.proFormaVersionNo = 1;
    CRCDtls.userName = "test";
    CRCDtls.versionNo = 1;
    CRCDtls.incomingInd = false;
    CRCDtls.caseID = caseId;
    CRCDtls.communicationStatus = communicationStatus;
    concernRoleCommObj.insert(CRCDtls);
  }

  /**
   * Helper function to create a workitem in the BDMConcernRoleCommunication table
   * prior to executing
   * test.
   *
   * @param communicationID - communicationID of test item
   * @param workItemID - workItemID of test item
   * @param templateName - name of template for communication
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createTestBDMCommunication(final int workItemID,
    final int communicationID, final String templateName)
    throws AppException, InformationalException {

    final BDMConcernRoleCommunicationDtls BDMCRCDtls =
      new BDMConcernRoleCommunicationDtls();
    BDMCRCDtls.workItemID = workItemID;
    BDMCRCDtls.submittedInd = false;
    BDMCRCDtls.digitalInd = true;
    BDMCRCDtls.communicationID = communicationID;
    BDMCRCDtls.templateName = templateName; // isInterimApp ? "ISS-6592" : "TEST";
    BDMConcernRoleCommObj.insert(BDMCRCDtls);
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }
}
