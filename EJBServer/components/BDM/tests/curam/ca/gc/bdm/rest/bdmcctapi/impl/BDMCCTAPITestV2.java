package curam.ca.gc.bdm.rest.bdmcctapi.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.communication.base.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMCorrespondenceStagingFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceSearchKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingJobID;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingKeyList;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTCorrespondenceStatusDetails;
import curam.ca.gc.bdm.rest.bdmcctapi.struct.BDMCCTWorkItemStatusDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMInterfaceLogger;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.codetable.BDMCCTCOMMUNICATIONSTATUS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import java.util.HashMap;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)

public class BDMCCTAPITestV2 {

  curam.ca.gc.bdm.rest.bdmcctapi.impl.BDMCCTAPI bdmcctapiObj;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmcctapiObj = new BDMCCTAPI();

  }

  @Test
  public void testUpdateWorkItemStatus_success(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger) throws Exception {

    new Expectations() {

      {
        bdmInterfaceLogger.logRequest(anyString, anyString, any);
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername)
            throws AppException, InformationalException {

          }

          @Mock
          protected void validateWorkItemStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

          }

          @Mock
          protected long updateCCTStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            return 100L;
          }
        };
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    req.workitemid = "100";
    req.cloudEvent.id = "100-100-100";
    bdmcctapiObj.updateWorkItemStatus(req);
    new Verifications() {

      {
        final BDMAPIAuditDetails bdmapiAuditDetails;
        bdmapiAuditUtil.auditAPI(bdmapiAuditDetails = withCapture());
        times = 1;

        assertEquals(bdmapiAuditDetails.getInvokingMethod(),
          "BDMCCTAPI.updateWorkItemStatus");
        assertEquals(bdmapiAuditDetails.getRelatedID(), "100");
        assertEquals(bdmapiAuditDetails.getCorrelationID(), "100-100-100");

        bdmInterfaceLogger.logRequest(anyString, anyString, any);
        times = 1;

        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
      }
    };
  }

  @Test
  public void
    testUpdateWorkItemStatus_unhandledExceptionInValidateWorkItemStatus(
      @Mocked
      final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
      final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
      final BDMRestUtil bdmRestUtil) throws Exception {

    new Expectations() {

      {
        bdmInterfaceLogger.logRequest(anyString, anyString, any);
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        minTimes = 0;
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);

        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername)
            throws AppException, InformationalException {

          }

          @Mock
          protected void validateWorkItemStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails) throws Exception {

            throw new Exception("Unhandled");
          }

          @Mock
          protected long updateCCTStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            return 100L;
          }
        };
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    req.workitemid = "100";
    req.cloudEvent.id = "100-100-100";
    try {
      bdmcctapiObj.updateWorkItemStatus(req);
    } catch (final Exception exp) {
      // it should reach here as we have simulated exception scenario
      assertTrue(true);
    }
    new Verifications() {

      {
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 1;// as the exception of type other than AppException audit should be called
      }
    };
  }

  @Test
  public void
    testUpdateWorkItemStatus_unhandledExceptionInUpdateCorrespondenceStatus(
      @Mocked
      final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
      final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
      final BDMRestUtil bdmRestUtil) throws Exception {

    new Expectations() {

      {
        bdmInterfaceLogger.logRequest(anyString, anyString, any);
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        minTimes = 0;
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);

        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername) throws Exception {

            throw new Exception("Unhandled");
          }

          @Mock
          protected void validateWorkItemStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

          }

          @Mock
          protected long updateCCTStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            return 100L;
          }
        };
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    req.workitemid = "100";
    req.cloudEvent.id = "100-100-100";
    try {
      bdmcctapiObj.updateWorkItemStatus(req);
    } catch (final Exception exp) {
      // it should reach here as we have simulated exception scenario
      assertTrue(true);
    }
    new Verifications() {

      {
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 1; // as the exception of type other than AppException audit should be called
      }
    };
  }

  @Test
  public void
    testUpdateWorkItemStatus_404exceptionInUpdateCorrespondenceStatus(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final BDMRestUtil bdmRestUtil) throws Exception {

    new Expectations() {

      {
        bdmInterfaceLogger.logRequest(anyString, anyString, any);
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        minTimes = 0;
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        minTimes = 0;

        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername)
            throws AppException, InformationalException {

            // simulate ERR_HTTP_404_RESOURCE_NOT_FOUND
            final AppException appException =
              new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
            throw appException;
          }

          @Mock
          protected void validateWorkItemStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

          }

          @Mock
          protected long updateCCTStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            return 100L;
          }
        };
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    req.workitemid = "100";
    req.cloudEvent.id = "100-100-100";
    try {
      bdmcctapiObj.updateWorkItemStatus(req);
    } catch (final AppException exp) {
      // it should reach here as we have simulated exception scenario;
      assertTrue(true);
    }
    new Verifications() {

      {
        // as the error is 404 , auditing entry would have been handled in the
        // individual
        // method at the point when exception was thrown and should not make call to
        // auditErrorResponse from BDMCCTAPI class
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 0;
      }
    };
  }

  @Test
  public void
    testUpdateWorkItemStatus_404exceptionInValidateWorkItemStatus(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final BDMRestUtil bdmRestUtil) throws Exception {

    new Expectations() {

      {
        bdmInterfaceLogger.logRequest(anyString, anyString, any);
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        minTimes = 0;
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        minTimes = 0;

        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername)
            throws AppException, InformationalException {

          }

          @Mock
          protected void validateWorkItemStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            // simulate ERR_HTTP_404_RESOURCE_NOT_FOUND
            final AppException appException =
              new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
            throw appException;
          }

          @Mock
          protected long updateCCTStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            return 100L;
          }
        };
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    req.workitemid = "100";
    req.cloudEvent.id = "100-100-100";
    try {
      bdmcctapiObj.updateWorkItemStatus(req);
    } catch (final AppException exp) {
      // it should reach here as we have simulated exception scenario;
      assertTrue(true);
    }
    new Verifications() {

      {
        // as the error is 404 , auditing entry would have been handled in the
        // individual
        // method at the point when exception was thrown and should not make call to
        // auditErrorResponse from BDMCCTAPI class
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 0;
      }
    };
  }

  @Test
  public void
    testUpdateWorkItemStatus_unhandledExceptionInUpdateCCTStatus(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final BDMRestUtil bdmRestUtil) throws Exception {

    new Expectations() {

      {
        bdmInterfaceLogger.logRequest(anyString, anyString, any);
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        minTimes = 0;
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        minTimes = 0;

        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername)
            throws AppException, InformationalException {

          }

          @Mock
          protected void validateWorkItemStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

          }

          @Mock
          protected long updateCCTStatus(
            final BDMCCTWorkItemStatusDetails req,
            final BDMAPIAuditDetails bdmapiAuditDetails) throws Exception {

            throw new Exception("Unhandled");

          }
        };
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    req.workitemid = "100";
    req.cloudEvent.id = "100-100-100";
    try {
      bdmcctapiObj.updateWorkItemStatus(req);
    } catch (final Exception exp) {
      // it should reach here as we have simulated exception scenario
      assertTrue(true);
    }
    new Verifications() {

      {
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 1;// as the exception of type other than AppException audit should be called
      }
    };
  }

  @Test
  public void testValidateWorkItemStatus_workItemEmpty() throws Exception {

    new Expectations() {

      {
        new MockUp<BDMRestUtil>() {

          @Mock
          public void throwHTTP404Status(final String argumentMsg,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            final AppException appException =
              new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);

            throw appException;
          }
        };

      }
    };

    try {
      final BDMCCTWorkItemStatusDetails req =
        new BDMCCTWorkItemStatusDetails();
      final BDMAPIAuditDetails bdmapiAuditDetails = null;
      bdmcctapiObj.validateWorkItemStatus(req, bdmapiAuditDetails);
    } catch (final AppException e) {
      // it should reach here as we have simulated exception scenario
      assertTrue(true);

    }
  }

  @Test
  public void testValidateWorkItemStatus_statusEmpty() throws Exception {

    new Expectations() {

      {
        new MockUp<BDMRestUtil>() {

          @Mock
          public void throwHTTP404Status(final String argumentMsg,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            final AppException appException =
              new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);

            throw appException;
          }
        };

      }
    };

    try {
      final BDMCCTWorkItemStatusDetails req =
        new BDMCCTWorkItemStatusDetails();
      req.workitemid = "100";
      final BDMAPIAuditDetails bdmapiAuditDetails = null;
      bdmcctapiObj.validateWorkItemStatus(req, bdmapiAuditDetails);
    } catch (final AppException e) {
      // it should reach here as we have simulated exception scenario
      assertTrue(true);

    }
  }

  @Test
  public void testUpdateCCTStatus_workitemId_notFound(@Mocked
  final BDMConcernRoleCommunicationFactory communicationFactory, @Mocked
  final CodeTable codetableObj) throws Exception {

    new Expectations() {

      {
        final BDMConcernRoleCommunicationDtls communicationDtls =
          new BDMConcernRoleCommunicationDtls();

        BDMConcernRoleCommunicationFactory.newInstance()
          .getCorrespondenceByWorkItemID(
            with(new Delegate<NotFoundIndicator>() {

              boolean delegate(final NotFoundIndicator notFoundIndicator) {

                notFoundIndicator.setNotFound(true);
                return true;
              }
            }), withAny(new BDMCorrespondenceSearchKey()));
        result = communicationDtls;

        CodeTable.getOneItem(anyString, anyString);
        result = BDMCCTCOMMUNICATIONSTATUS.ACTIVE_COMPLETE;

        new MockUp<BDMRestUtil>() {

          @Mock
          public void throwHTTP404Status(final String argumentMsg,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            final AppException appException =
              new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);

            throw appException;
          }
        };
      }
    };

    try {
      final BDMCCTWorkItemStatusDetails req =
        new BDMCCTWorkItemStatusDetails();
      req.workitemid = "100";
      req.status = "ACTIVE_COMPLETE";
      final BDMAPIAuditDetails bdmapiAuditDetails = null;
      bdmcctapiObj.updateCCTStatus(req, bdmapiAuditDetails);

    } catch (final AppException e) {
      // it should reach here as we have simulated exception scenario; workitem not
      // found in correspondence
      assertTrue(true);
    }

  }

  @Test
  public void testUpdateCCTStatus_workitemId_notFound(@Mocked
  final BDMConcernRoleCommunicationFactory communicationFactory, @Mocked
  final BDMRestUtil bdmRestUtil, @Mocked
  final BDMUtil bdmUtil, @Mocked
  final CodeTable codeTable) throws Exception {

    new Expectations() {

      {
        final BDMConcernRoleCommunicationDtls communicationDtls =
          new BDMConcernRoleCommunicationDtls();
        communicationDtls.communicationID = 200L;

        BDMConcernRoleCommunicationFactory.newInstance()
          .getCorrespondenceByWorkItemID(
            with(new Delegate<NotFoundIndicator>() {

              boolean delegate(final NotFoundIndicator notFoundIndicator) {

                // set value such the record is found
                notFoundIndicator.setNotFound(false);
                return true;
              }
            }), withAny(new BDMCorrespondenceSearchKey()));
        result = communicationDtls;

        final HashMap<String, String> hashMapOutput = new HashMap<>();
        hashMapOutput.put("statusInput", "statusOutput");
        BDMUtil.getCCTDescriptionToCodeMap();
        result = hashMapOutput;

        communicationFactory.modify((BDMConcernRoleCommunicationKey) any,
          (BDMConcernRoleCommunicationDtls) any);

        CodeTable.getOneItem(BDMCCTCOMMUNICATIONSTATUS.TABLENAME,
          BDMCCTCOMMUNICATIONSTATUS.FAILED_OVERSIZED);
        result = "test";
      }
    };

    final BDMCCTWorkItemStatusDetails req = new BDMCCTWorkItemStatusDetails();
    req.workitemid = "100";
    req.status = "statusInput";
    final BDMAPIAuditDetails bdmapiAuditDetails = null;
    final long outputCorrespondenceKey =
      bdmcctapiObj.updateCCTStatus(req, bdmapiAuditDetails);

    assertEquals(outputCorrespondenceKey, 200);

    new Verifications() {

      {
        BDMConcernRoleCommunicationFactory.newInstance()
          .getCorrespondenceByWorkItemID((NotFoundIndicator) any,
            (BDMCorrespondenceSearchKey) any);
        times = 1;

        BDMUtil.getCCTDescriptionToCodeMap();
        times = 1;
        BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
          new BDMConcernRoleCommunicationDtls();
        BDMConcernRoleCommunicationKey bdmConcernRoleCommunicationKey =
          new BDMConcernRoleCommunicationKey();
        communicationFactory.modify(
          bdmConcernRoleCommunicationKey = withCapture(),
          bdmConcernRoleCommunicationDtls = withCapture());
        times = 1;

        assertEquals(bdmConcernRoleCommunicationDtls.cctStatus,
          "statusOutput");

        assertEquals(bdmConcernRoleCommunicationKey.communicationID, 200);
      }
    };
  }

  @Test
  public void testUpdateCorrespondenceStatus_workItemNotFound()
    throws Exception {

    new Expectations() {

      {
        new MockUp<BDMConcernRoleCommunication>() {

          @Mock
          public BDMConcernRoleCommunicationDtls
            getCorrespondenceByWorkItemID(final NotFoundIndicator nf,
              final BDMCorrespondenceSearchKey searchKey) {

            nf.setNotFound(true);
            return new BDMConcernRoleCommunicationDtls();
          }
        };
      }
    };

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "500";
    details.status = "ACTIVE_COMPLETE";
    details.username = "SomeUser";
    details.type = "WORKITEMID";
    details.community = "Pensions";

    final AppException e = assertThrows(AppException.class,
      () -> bdmcctapiObj.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  @Test
  public void testUpdateCorrespondenceStatus_workItemBadStatus(@Mocked
  final BDMConcernRoleCommunicationFactory bdmObj, @Mocked
  final BDMUtil bdmUtilObj) throws Exception {

    final BDMConcernRoleCommunicationDtls bdmDtls =
      new BDMConcernRoleCommunicationDtls();
    new Expectations() {

      {
        bdmDtls.communicationID = 100;
        bdmObj.getCorrespondenceByWorkItemID((NotFoundIndicator) any,
          (BDMCorrespondenceSearchKey) any);
        result = bdmDtls;

        final HashMap<String, String> hashMapOutput = new HashMap<>();
        hashMapOutput.put("statusInput", "statusOutput");
        BDMUtil.getCCTDescriptionToCodeMap();
        result = hashMapOutput;
      }
    };

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "500";
    details.status = "ACTIVE_COMPLETE1";
    details.username = "SomeUser";
    details.type = "WORKITEMID";
    details.community = "Pensions";

    try {
      bdmcctapiObj.updateCorrespondenceStatus(details);
    } catch (final AppException ae) {
      assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST, ae.getCatEntry());
    }
  }

  @Test
  public void testUpdateCorrespondenceStatus_workItemSuccess(@Mocked
  final BDMConcernRoleCommunicationFactory bdmObj, @Mocked
  final BDMUtil bdmUtilObj, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

    final BDMConcernRoleCommunicationDtls bdmDtls =
      new BDMConcernRoleCommunicationDtls();
    new Expectations() {

      {
        bdmDtls.communicationID = 100;
        bdmDtls.workItemID = 200;
        bdmDtls.trackingNumber = 123;
        bdmObj.getCorrespondenceByWorkItemID((NotFoundIndicator) any,
          (BDMCorrespondenceSearchKey) any);
        result = bdmDtls;

        final HashMap<String, String> hashMapOutput = new HashMap<>();
        hashMapOutput.put("ACTIVE_COMPLETE", "BDMCCTST01");
        BDMUtil.getCCTDescriptionToCodeMap();
        result = hashMapOutput;

        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername)
            throws AppException, InformationalException {

          }

        };
      }
    };

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "500";
    details.status = "ACTIVE_COMPLETE";
    details.username = "SomeUser";
    details.type = "WORKITEMID";
    details.community = "Pensions";

    bdmcctapiObj.updateCorrespondenceStatus(details);
    new Verifications() {

      {
        final BDMAPIAuditDetails bdmapiAuditDetails;
        bdmapiAuditUtil.auditAPI(bdmapiAuditDetails = withCapture());
        times = 1;

      }
    };
  }

  @Test
  public void testUpdateCorrespondenceStatus_trackingNumberBadStatus(@Mocked
  final BDMConcernRoleCommunicationFactory bdmObj, @Mocked
  final BDMUtil bdmUtilObj) throws Exception {

    final BDMConcernRoleCommunicationDtls bdmDtls =
      new BDMConcernRoleCommunicationDtls();
    new Expectations() {

      {
        bdmDtls.communicationID = 100;
        bdmObj.getCorrespondenceByTrackingNumber((NotFoundIndicator) any,
          (BDMSearchByTrackingNumberKey) any);
        result = bdmDtls;

        final HashMap<String, String> hashMapOutput = new HashMap<>();
        hashMapOutput.put("statusInput", "statusOutput");
        BDMUtil.getCCTDescriptionToCodeMap();
        result = hashMapOutput;
      }
    };

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "500";
    details.status = "ACTIVE_COMPLETE1";
    details.username = "SomeUser";
    details.type = "TRACKINGNO";
    details.community = "Pensions";

    try {
      bdmcctapiObj.updateCorrespondenceStatus(details);
    } catch (final AppException ae) {
      assertEquals(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST, ae.getCatEntry());
    }
  }

  @Test
  public void testUpdateCorrespondenceStatus_trackingNumberNotFound()
    throws Exception {

    new Expectations() {

      {
        new MockUp<BDMConcernRoleCommunication>() {

          @Mock
          public BDMConcernRoleCommunicationDtls
            getCorrespondenceByTrackingNumber(final NotFoundIndicator nf,
              final BDMSearchByTrackingNumberKey searchKey) {

            nf.setNotFound(true);
            return new BDMConcernRoleCommunicationDtls();
          }
        };

        final HashMap<String, String> hashMapOutput = new HashMap<>();
        hashMapOutput.put("statusInput", "statusOutput");
        BDMUtil.getCCTDescriptionToCodeMap();
        result = hashMapOutput;
      }
    };

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "500";
    details.status = "ACTIVE_COMPLETE";
    details.username = "SomeUser";
    details.type = "TRACKINGNO";
    details.community = "Pensions";

    final AppException e = assertThrows(AppException.class,
      () -> bdmcctapiObj.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }

  @Test
  public void testUpdateCorrespondenceStatus_trackingNumberSuccess(@Mocked
  final BDMConcernRoleCommunicationFactory bdmObj, @Mocked
  final BDMUtil bdmUtilObj, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

    final BDMConcernRoleCommunicationDtls bdmDtls =
      new BDMConcernRoleCommunicationDtls();
    new Expectations() {

      {
        bdmDtls.communicationID = 100;
        bdmDtls.workItemID = 200;
        bdmDtls.trackingNumber = 123;
        bdmObj.getCorrespondenceByTrackingNumber((NotFoundIndicator) any,
          (BDMSearchByTrackingNumberKey) any);
        result = bdmDtls;

        final HashMap<String, String> hashMapOutput = new HashMap<>();
        hashMapOutput.put("FAILED_OVERSIZED", "BDMCCTST08");
        BDMUtil.getCCTDescriptionToCodeMap();
        result = hashMapOutput;

        new MockUp<BDMCCTAPI>() {

          @Mock
          protected void updateCorrespondenceStatus(
            final long communicationID, final String cctStatus,
            final String cctUsername)
            throws AppException, InformationalException {

          }

        };
      }
    };

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "500";
    details.status = "FAILED_OVERSIZED";
    details.username = "SomeUser";
    details.type = "TRACKINGNO";
    details.community = "Pensions";

    bdmcctapiObj.updateCorrespondenceStatus(details);
    new Verifications() {

      {
        final BDMAPIAuditDetails bdmapiAuditDetails;
        bdmapiAuditUtil.auditAPI(bdmapiAuditDetails = withCapture());
        times = 1;

      }
    };
  }

  @Test
  public void testUpdateCorrespondenceStatus_jobIDNotFound(@Mocked
  final BDMCorrespondenceStagingFactory stagingObj) throws Exception {

    new Expectations() {

      {
        stagingObj.searchByJobID((BDMCorrespondenceStagingJobID) any);
        result = new BDMCorrespondenceStagingKeyList();

        final HashMap<String, String> hashMapOutput = new HashMap<>();
        hashMapOutput.put("statusInput", "statusOutput");
        BDMUtil.getCCTDescriptionToCodeMap();
        result = hashMapOutput;

      }
    };

    final BDMCCTCorrespondenceStatusDetails details =
      new BDMCCTCorrespondenceStatusDetails();
    details.correspondenceID = "500";
    details.status = "BULK_SUCCESS";
    details.username = "SomeUser";
    details.type = "JOBID";
    details.community = "Pensions";

    final AppException e = assertThrows(AppException.class,
      () -> bdmcctapiObj.updateCorrespondenceStatus(details));
    assertEquals(e.getCatEntry(), BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
  }
}
