package curam.ca.gc.bdm.test.aes;

import com.google.inject.Inject;
import curam.aes.codetable.AESSHARESETSTATUS;
import curam.aes.sl.callback.impl.AESCallbackImpl;
import curam.aes.sl.entity.struct.AESShareSetDtls;
import curam.aes.sl.entity.struct.AESShareSetKey;
import curam.aes.sl.events.impl.AdvancedEvidenceSharingEventHandler;
import curam.aes.sl.impl.AESLogging;
import curam.aes.sl.observe.impl.AESShareItemPreCommitAction;
import curam.aes.sl.observe.impl.AESShareItemTracker;
import curam.aes.sl.workflow.fact.AdvancedEvidenceSharingPushWorkflowFactory;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.commonintake.authorisation.impl.AuthoriseWorkflow;
import curam.commonintake.authorisation.struct.AuthorisationNotificationDetails;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKeyList;
import curam.core.struct.CaseHeaderKey;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import java.util.List;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

/**
 * Class responsible for carrying out the functions of the Evidence Broker
 * Workflow.
 *
 * This class extends CuramServerTestJUnit4 only to have access to a
 * transaction.
 */
public class AESSimulator extends CuramServerTestJUnit4 {

  /**
   * AESShareSetKey object
   */
  AESShareSetKey shareSetKey;

  /**
   * AESLogging object.
   */
  @Inject
  AESLogging aesLogger;

  /**
   * AuthoriseWorkflow object.
   */
  @Inject
  private AuthoriseWorkflow authoriseWorkflow;

  /**
   * Mocking out EvidenceBrokerEventHandler to prevent the evidence changes from
   * being recognized while running tests in a dev env.
   */
  @Mocked
  private AdvancedEvidenceSharingEventHandler mockEvidenceBrokerEventHandler;

  /**
   * AESShareItemTracker object.
   */
  @Inject
  private AESShareItemTracker evidencePropagationShareTracker;

  /**
   * AESShareItemPreCommitAction interface.
   */
  @Inject
  private AESShareItemPreCommitAction aesShareItemPreCommitAction;

  /**
   * Constructor.
   */
  public AESSimulator() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Return the Evidence Broker Share Set entity key.
   *
   * @param caseID
   * Case identifier
   *
   * @return Evidence Broker Share Set entity key
   *
   * @throws AppException
   * Generic Exception Message
   * @throws InformationalException
   * Generic Exception Message
   */
  protected AESShareSetKey getShareSetKey(final long caseID)
    throws AppException, InformationalException {

    final AESShareSetKey shareSetKey = new AESShareSetKey();

    // System.out.println("sourceCaseID: " + caseID);

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseID;
    final StringBuffer sqlStr = new StringBuffer();

    sqlStr.append(
      "SELECT DISTINCT AESShareSet.shareSetID, AESShareSet.createdDateTime "
        + "INTO :shareSetID, :createdDateTime FROM AESShareSet "
        + "INNER JOIN AESShareItem ON AESShareItem.shareSetID = AESShareSet.shareSetID "
        + "WHERE AESShareItem.sourceCaseID= :caseID AND AESShareSet.status ='"
        + AESSHARESETSTATUS.OPEN + "'" + "ORDER BY createdDateTime DESC");

    final CuramValueList<AESShareSetDtls> curamValueList =
      DynamicDataAccess.executeNsMulti(AESShareSetDtls.class, caseHeaderKey,
        false, sqlStr.toString());

    // System.out.println("ShareSetID: " + curamValueList.get(0).shareSetID);
    shareSetKey.shareSetID = curamValueList.get(0).shareSetID;
    return shareSetKey;
  }

  /**
   * Propagates the given evidence from the giving source case as per the stored
   * Evidence Broker Configuration.
   *
   * NOTE: Any test calling this method should extend
   * {@link curam.aes.scenario.AESScenario} and the beforeTest method carries
   * out required mocking.
   *
   * @param list
   * List of evidence on the case to propagate
   * @param caseID
   * Source case identifier
   *
   * @return AESShareSetKey the AESShareSetKey
   * @throws AppException
   * Generic Exception Message
   * @throws InformationalException
   * Generic Exception Message
   */
  public AESShareSetKey createPlansAndMakeRecommendations(
    final EvidenceDescriptorKeyList list, final long caseID)
    throws AppException, InformationalException {

    new MockUp<curam.util.workflow.impl.EnactmentService>() {

      /**
       * Simulates deferred processing started by
       * {@link curam.aes.sl.callback.impl.EvidenceBrokerCallback#callBack}.
       *
       * @param processName
       * Name of process name to be enacted
       * @param enactmentStructs
       * Contains authorization notification details
       *
       * @return Process ID
       *
       * @throws AppException
       * Generic Exception Message
       * @throws InformationalException
       * Generic Exception Message
       */
      @Mock
      long startProcess(final String processName,
        final List<? extends Object> enactmentStructs)
        throws AppException, InformationalException {

        // System.out.println("processName: " + processName);

        // Check if process to complete Application Case
        if ("SUCCESSFULPROGRAMAUTHORIZATIONNOTIFICATION"
          .equals(processName)) {

          final AuthorisationNotificationDetails details =
            (AuthorisationNotificationDetails) enactmentStructs.get(0);
          getAuthoriseWorkflow().notificationAllocation(details);
        }

        return 0L;
      }
    };

    aesShareItemPreCommitAction.process(list);

    // 1. Retrieve the share set key
    final AESShareSetKey shareSetKey = getShareSetKey(caseID);

    // 2. Construct the delivery plan
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .calculateShareSetDeliverPlan(shareSetKey);

    // 3. giveActionEBChangeItem
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .recommendShareSetAction(shareSetKey);

    return shareSetKey;
  }

  /**
   * Propagates the given evidence from the giving source case as per the stored
   * Evidence Broker Configuration.
   *
   * NOTE: Any test calling this method should extend should extend
   * {@link curam.aes.scenario.AESScenario} and the beforeTest method carries
   * out required mocking.
   *
   * @param list
   * List of evidence on the case to propagate
   * @param caseID
   * Source case identifier
   *
   * @return AESShareSetKey the AESShareSetKey
   * @throws AppException
   * Generic Exception Message
   * @throws InformationalException
   * Generic Exception Message
   */
  public AESShareSetKey shareEvidence(final EvidenceDescriptorKeyList list,
    final long caseID) throws AppException, InformationalException {

    new MockUp<curam.util.workflow.impl.EnactmentService>() {

      /**
       * Simulates deferred processing started by
       * {@link curam.aes.sl.callback.impl.EvidenceBrokerCallback#callBack}.
       *
       * @param processName
       * Name of process name to be enacted
       * @param enactmentStructs
       * Contains authorization notification details
       *
       * @return Process ID
       *
       * @throws AppException
       * Generic Exception Message
       * @throws InformationalException
       * Generic Exception Message
       */
      @Mock
      long startProcess(final String processName,
        final List<? extends Object> enactmentStructs)
        throws AppException, InformationalException {

        // System.out.println("processName: " + processName);

        // Check if process to complete Application Case
        if ("SUCCESSFULPROGRAMAUTHORIZATIONNOTIFICATION"
          .equals(processName)) {

          final AuthorisationNotificationDetails details =
            (AuthorisationNotificationDetails) enactmentStructs.get(0);
          getAuthoriseWorkflow().notificationAllocation(details);
        }

        return 0L;
      }
    };

    evidencePropagationShareTracker.setSharingNotInProgress();

    aesShareItemPreCommitAction.process(list);
    commitDontCloseConnection();

    // 1. Retrieve the share set key
    final AESShareSetKey shareSetKey = getShareSetKey(caseID);

    // 2. Construct the delivery plan
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .calculateShareSetDeliverPlan(shareSetKey);
    commitDontCloseConnection();

    // 3. giveActionEBChangeItem
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .recommendShareSetAction(shareSetKey);
    commitDontCloseConnection();

    // 4. Delivery the share set
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .deliverShareSet(shareSetKey);
    commitDontCloseConnection();

    // 5. Activate the share set
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .activateShareSet(shareSetKey);
    commitDontCloseConnection();

    // 6. Complete changes
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .completeShareSet(shareSetKey);
    commitDontCloseConnection();

    // Seems like we dont need this step as it disposes the application but its
    // already disposed at this stage so this fails
    // 7. Call Back
    AdvancedEvidenceSharingPushWorkflowFactory.newInstance()
      .callback(shareSetKey);

    new AESCallbackImpl().callBack(shareSetKey);
    // commit();

    return shareSetKey;
  }

  /**
   * Get the AuthoriseWorkflow object.
   *
   * @return AuthoriseWorkflow object
   */
  public AuthoriseWorkflow getAuthoriseWorkflow() {

    return authoriseWorkflow;
  }

  /**
   * commit the database updates. This is the same as
   * curam.test.junit4.CuramServerTestJUnit4.commit() except we do not close the
   * connection after the commit.
   *
   * @return
   */
  protected final void commitDontCloseConnection() {

    final TransactionInfo ti = TransactionInfo.getInfo();

    if (ti != null) {
      ti.commit();
    }
  }

}
