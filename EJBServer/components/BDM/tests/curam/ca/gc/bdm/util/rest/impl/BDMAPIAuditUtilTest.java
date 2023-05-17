package curam.ca.gc.bdm.util.rest.impl;

import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.ca.gc.bdm.entity.fact.BDMRequestResponseFactory;
import curam.ca.gc.bdm.entity.intf.BDMRequestResponse;
import curam.ca.gc.bdm.entity.struct.BDMRequestResponseDtls;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationKey;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTWorkItemStatusDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails.BDMAPIAuditDetailsBuilder;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil.BDMAPIAuditUtilLocalTransaction;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.transaction.TransactionInfo.TransactionType;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMAPIAuditUtilTest {

  @Tested
  BDMAPIAuditUtil bdmcctAuditUtil;

  boolean insertCalled = false;

  @Test
  public void testAuditAPI_globalAuditProperty_disabled(@Mocked
  final Configuration configuration) throws Exception {

    new Expectations() {

      {
        Configuration
          .getBooleanProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED);
        result = false;

        new MockUp<BDMAPIAuditUtil>() {

          @Mock
          public void insertAuditDetailsToDB(
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            // as property is disabled, it shouldn't enter here
            insertCalled = true;
          }

        };

      }
    };

    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").build();
    bdmcctAuditUtil.auditAPI(bdmapiAuditDetails);

    new Verifications() {

      {
        Configuration
          .getBooleanProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED);
        times = 1;

        assertEquals(insertCalled, false);

      }
    };
  }

  @Test
  public void
    testAuditAPI_globalAuditPropertyEnabled_methodLevelDisabled(@Mocked
  final Configuration configuration) throws Exception {

    new Expectations() {

      {
        Configuration
          .getBooleanProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED);
        result = true;

        Configuration.getBooleanProperty(
          "curam.ca.gc.requestresponse.audit.enabled.BDMCCTAPI.updateWorkItemStatus");
        result = false;

        new MockUp<BDMAPIAuditUtil>() {

          @Mock
          public void insertAuditDetailsToDB(
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            // as property is disabled, it shouldn't enter here
            insertCalled = true;
          }

        };

      }
    };

    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").build();
    bdmcctAuditUtil.auditAPI(bdmapiAuditDetails);

    new Verifications() {

      {
        Configuration
          .getBooleanProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED);
        times = 1;

        Configuration.getBooleanProperty(
          "curam.ca.gc.requestresponse.audit.enabled.BDMCCTAPI.updateWorkItemStatus");
        times = 1;

        assertEquals(insertCalled, false);

      }
    };
  }

  @Test
  public void
    testAuditAPI_globalAuditPropertyEnabled_methodLevelEnabled(@Mocked
  final Configuration configuration) throws Exception {

    new Expectations() {

      {
        Configuration
          .getBooleanProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED);
        result = true;

        Configuration.getBooleanProperty(
          "curam.ca.gc.requestresponse.audit.enabled.BDMCCTAPI.updateWorkItemStatus");
        result = true;

        new MockUp<BDMAPIAuditUtil>() {

          @Mock
          public void insertAuditDetailsToDB(
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            // as both property is enabled, it will enter this condition.
            insertCalled = true;
          }

        };

      }
    };

    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").build();
    bdmcctAuditUtil.auditAPI(bdmapiAuditDetails);

    new Verifications() {

      {
        Configuration
          .getBooleanProperty(EnvVars.ENV_REQUESTRESPONSE_AUDIT_ENABLED);
        times = 1;

        Configuration.getBooleanProperty(
          "curam.ca.gc.requestresponse.audit.enabled.BDMCCTAPI.updateWorkItemStatus");
        times = 1;

        assertEquals(insertCalled, true);

      }
    };

    // reset the classlevel test variable
    insertCalled = false;
  }

  @Test
  public void testInsertAuditDetailsToDB(@Mocked
  final UniqueID uniqueID, @Mocked
  final TransactionInfo transactionInfo) throws Exception {

    new Expectations() {

      {
        UniqueID.nextUniqueID();
        result = 100000000L;

        TransactionInfo.getProgramUser();
        result = "caseworker";

        new MockUp<BDMAPIAuditUtil>() {

          @Mock
          public void performInsert(
            final BDMRequestResponse bdmRequestResponse,
            final BDMRequestResponseDtls bdmRequestResponseDtls) {

            assertEquals(bdmRequestResponseDtls.auditID, 100000000L);
            assertEquals(bdmRequestResponseDtls.userName, "caseworker");
            assertEquals(bdmRequestResponseDtls.relatedID, "100");
            assertEquals(bdmRequestResponseDtls.correlationID, "100");
            assertEquals(bdmRequestResponseDtls.source, "Source");
            assertEquals(bdmRequestResponseDtls.apiType,
              BDMAUDITAPITYPE.BDMINBOUND);
            // this will be executed
            insertCalled = true;
          }

        };

      }
    };
    final BDMCCTWorkItemStatusDetails requestObj =
      new BDMCCTWorkItemStatusDetails();
    final BDMCommunicationKey responseObj = new BDMCommunicationKey();
    final BDMAPIAuditDetails bdmapiAuditDetails =
      new BDMAPIAuditDetailsBuilder()
        .setMethod("BDMCCTAPI.updateWorkItemStatus").setRelatedID("100")
        .setRequestObject(requestObj).setApiType(BDMAUDITAPITYPE.BDMINBOUND)
        .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
        .setSource("Source").setCorrelationID("100")
        .setResponseObject(responseObj).build();
    bdmcctAuditUtil.insertAuditDetailsToDB(bdmapiAuditDetails);

    new Verifications() {

      {
        UniqueID.nextUniqueID();
        times = 1;

        TransactionInfo.getProgramUser();
        times = 1;

      }
    };

    // reset the classlevel test variable
    insertCalled = false;
  }

  @Test
  public void testPerformInsert(@Mocked
  final BDMRequestResponseFactory bdmRequestResponseObj) throws Exception {

    new Expectations() {

      {
        bdmRequestResponseObj.insert((BDMRequestResponseDtls) any);

        new MockUp<BDMAPIAuditUtil>() {

          @Mock
          private void commitLocalTransaction(
            final TransactionInfo localTransaction) throws Exception {

          }

          @Mock
          private void
            rollbackLocalTransaction(final TransactionInfo localTransaction) {

          }

          @Mock
          private TransactionInfo beginLocalTransaction() throws Exception {

            return new TransactionInfo(TransactionType.kOnline,
              new BDMAPIAuditUtilLocalTransaction(), null,
              TransactionInfo.getProgramLocale());
          }

        };

      }

    };

    final BDMRequestResponseDtls bdmRequestResponseDtls =
      new BDMRequestResponseDtls();
    bdmRequestResponseDtls.auditID = 100000000L;
    bdmRequestResponseDtls.userName = "caseworker";
    bdmRequestResponseDtls.relatedID = "100";
    bdmRequestResponseDtls.correlationID = "100";
    bdmRequestResponseDtls.source = "Source";
    bdmRequestResponseDtls.apiType = BDMAUDITAPITYPE.BDMINBOUND;
    bdmcctAuditUtil.performInsert(BDMRequestResponseFactory.newInstance(),
      bdmRequestResponseDtls);

    new Verifications() {

      {
        BDMRequestResponseDtls bdmRequestResponseDtls1 =
          new BDMRequestResponseDtls();
        bdmRequestResponseObj.insert(bdmRequestResponseDtls1 = withCapture());
        times = 1;

        assertEquals(bdmRequestResponseDtls1.auditID, 100000000L);
        assertEquals(bdmRequestResponseDtls1.userName, "caseworker");
        assertEquals(bdmRequestResponseDtls1.relatedID, "100");
        assertEquals(bdmRequestResponseDtls1.correlationID, "100");
        assertEquals(bdmRequestResponseDtls1.source, "Source");
        assertEquals(bdmRequestResponseDtls1.apiType,
          BDMAUDITAPITYPE.BDMINBOUND);
      }
    };
  }

}
