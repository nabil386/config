package curam.ca.gc.bdm.rest.bdmdojinboundapi.impl;

import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMDOJRECORDSTATUS;
import curam.ca.gc.bdm.codetable.BDMRESTACTIONTYPE;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.fact.BDMDoJInboundStagingFactory;
import curam.ca.gc.bdm.entity.struct.BDMDoJInboundStagingDtls;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.rest.bdmdojinboundapi.struct.BDMObligationExchange;
import curam.ca.gc.bdm.rest.bdmdojinboundapi.struct.BDMObligationType;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckDOJLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMRegisterDOJLbyDetails;
import curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMInterfaceLogger;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.struct.AlternateIDTypeCodeKey;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)

public class BDMDOJInboundAPITestV2 {

  BDMDoJInboundAPI bdmDoJInboundAPI;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmDoJInboundAPI = new BDMDoJInboundAPI();

  }

  // BEGIN, ADO-55810, Gaps implementation for DOJ Inbound
  @Test
  public void testProcessDOJInbound(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype)
    throws AppException, InformationalException {

    new Expectations() {

      {

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability
          .registerLiability((BDMRegisterDOJLbyDetails) any);
        BDMBENEFITPROGRAMTYPE.getDefaultCode();
        result = "OAS";
        mockUpBDMDoJInboundAPI("100");
      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.obligation.person.personSINIdentification = 123456789;
    bdmDoJInboundAPI.processDOJInbound(dojInboundData);

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 1;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
        final BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        assertEquals(auditDetails.getRelatedID(),
          "b280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getCorrelationID(),
          "a280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getSource(), "TEST");
        assertEquals(auditDetails.getStatusCode(), HttpStatus.SC_NO_CONTENT);

        assertEquals(auditDetails.getInvokingMethod(),
          "BDMDoJInboundAPI.processDOJInbound");

        maintainDOJLiability
          .registerLiability((BDMRegisterDOJLbyDetails) any);
        times = 2;

      }
    };
  }

  @Test
  public void testProcessDOJInbound_zeroBalance(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype)
    throws AppException, InformationalException {

    new Expectations() {

      {

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability
          .registerLiability((BDMRegisterDOJLbyDetails) any);
        BDMBENEFITPROGRAMTYPE.getDefaultCode();
        result = "OAS";
        mockUpBDMDoJInboundAPI("0");
      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.obligation.person.personSINIdentification = 123456789;
    bdmDoJInboundAPI.processDOJInbound(dojInboundData);

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 1;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
        final BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        assertEquals(auditDetails.getRelatedID(),
          "b280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getCorrelationID(),
          "a280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getSource(), "TEST");
        assertEquals(auditDetails.getStatusCode(), HttpStatus.SC_NO_CONTENT);
        assertEquals(auditDetails.getInvokingMethod(),
          "BDMDoJInboundAPI.processDOJInbound");

        // register liability method will be called once as balance in this scenario is
        // set to 0
        maintainDOJLiability
          .registerLiability((BDMRegisterDOJLbyDetails) any);
        times = 1;

      }
    };
  }

  @Test
  public void testModifyDOJInbound(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype, @Mocked
  final BDMExternalLiabilityFactory externalLiabilityFactory)
    throws AppException, InformationalException {

    new Expectations() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability.modifyLiability((BDMModifyDOJLbyDetails) any);
        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        result = new BDMExternalLiabilityKey();

        BDMExternalLiabilityFactory.newInstance()
          .read(with(new Delegate<NotFoundIndicator>() {

            boolean delegate(final NotFoundIndicator notFoundIndicator) {

              notFoundIndicator.setNotFound(false);
              return true;
            }
          }), withAny(new BDMExternalLiabilityKey()));
        result = new BDMExternalLiabilityDtls();

        mockUpBDMDoJInboundAPI("100");
      }
    };

    BDMObligationExchange dojModifyOutput = new BDMObligationExchange();
    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";

    dojInboundData.obligation.person.personSINIdentification = 123456789;
    dojModifyOutput = bdmDoJInboundAPI.modifyDOJInbound(dojInboundData);

    assertEquals(dojModifyOutput.cloudEvent.id,
      "a280d89b-4760-4784-9968-fdafc8ac1944");
    assertEquals(dojModifyOutput.cloudEvent.source, "TEST");
    assertEquals(dojInboundData.metadata.metadataIdentificationID,
      "b280d89b-4760-4784-9968-fdafc8ac1944");

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 1;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
        final BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        assertEquals(auditDetails.getRelatedID(),
          "b280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getCorrelationID(),
          "a280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getSource(), "TEST");
        assertEquals(auditDetails.getStatusCode(), HttpStatus.SC_OK);

        assertEquals(auditDetails.getInvokingMethod(),
          "BDMDoJInboundAPI.modifyDOJInbound");

        maintainDOJLiability.modifyLiability((BDMModifyDOJLbyDetails) any);
        times = 2;

        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        times = 2;

      }
    };
  }

  @Test
  public void testModifyDOJInbound_extLiabilityNotFound(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype, @Mocked
  final BDMExternalLiabilityFactory externalLiabilityFactory)
    throws AppException, InformationalException {

    new Expectations() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability.modifyLiability((BDMModifyDOJLbyDetails) any);
        minTimes = 0;
        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        result = new BDMExternalLiabilityKey();

        BDMExternalLiabilityFactory.newInstance()
          .read(with(new Delegate<NotFoundIndicator>() {

            boolean delegate(final NotFoundIndicator notFoundIndicator) {

              notFoundIndicator.setNotFound(true);
              return true;
            }
          }), withAny(new BDMExternalLiabilityKey()));
        result = new BDMExternalLiabilityDtls();

        mockUpBDMDoJInboundAPI("100");
      }
    };

    BDMObligationExchange dojModifyOutput = new BDMObligationExchange();
    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";

    dojInboundData.obligation.person.personSINIdentification = 123456789;
    dojModifyOutput = bdmDoJInboundAPI.modifyDOJInbound(dojInboundData);

    assertEquals(dojModifyOutput.cloudEvent.id,
      "a280d89b-4760-4784-9968-fdafc8ac1944");
    assertEquals(dojModifyOutput.cloudEvent.source, "TEST");
    assertEquals(dojInboundData.metadata.metadataIdentificationID,
      "b280d89b-4760-4784-9968-fdafc8ac1944");

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 1;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
        final BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        assertEquals(auditDetails.getRelatedID(),
          "b280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getCorrelationID(),
          "a280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getSource(), "TEST");
        assertEquals(auditDetails.getStatusCode(), HttpStatus.SC_OK);

        assertEquals(auditDetails.getInvokingMethod(),
          "BDMDoJInboundAPI.modifyDOJInbound");

        maintainDOJLiability.modifyLiability((BDMModifyDOJLbyDetails) any);
        times = 0;

        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        times = 2;

      }
    };
  }

  @Test
  public void testDeleteDOJInbound(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype, @Mocked
  final BDMExternalLiabilityFactory externalLiabilityFactory)
    throws AppException, InformationalException {

    new Expectations() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability.deleteLiability((BDMExternalLiabilityKey) any);
        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        result = new BDMExternalLiabilityKey();

        mockUpBDMDoJInboundAPI("100");
      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";

    dojInboundData.obligation.person.personSINIdentification = 123456789;
    bdmDoJInboundAPI.deleteDOJInbound(dojInboundData);

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 1;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
        final BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        assertEquals(auditDetails.getRelatedID(),
          "b280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getCorrelationID(),
          "a280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getSource(), "TEST");
        assertEquals(auditDetails.getStatusCode(), HttpStatus.SC_NO_CONTENT);

        assertEquals(auditDetails.getInvokingMethod(),
          "BDMDoJInboundAPI.deleteDOJInbound");

        maintainDOJLiability.deleteLiability((BDMExternalLiabilityKey) any);
        times = 2;

        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        times = 2;

      }
    };
  }

  @Test
  public void testDeleteDOJInbound_extLiabilityNotFound(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype, @Mocked
  final BDMExternalLiabilityFactory externalLiabilityFactory)
    throws AppException, InformationalException {

    new Expectations() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability.deleteLiability((BDMExternalLiabilityKey) any);
        minTimes = 0;
        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        result = null;

        mockUpBDMDoJInboundAPI("100");
      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";

    dojInboundData.obligation.person.personSINIdentification = 123456789;
    bdmDoJInboundAPI.deleteDOJInbound(dojInboundData);

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 1;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
        final BDMAPIAuditDetails auditDetails;
        bdmapiAuditUtil.auditAPI(auditDetails = withCapture());
        assertEquals(auditDetails.getRelatedID(),
          "b280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getCorrelationID(),
          "a280d89b-4760-4784-9968-fdafc8ac1944");

        assertEquals(auditDetails.getSource(), "TEST");
        assertEquals(auditDetails.getStatusCode(), HttpStatus.SC_NO_CONTENT);

        assertEquals(auditDetails.getInvokingMethod(),
          "BDMDoJInboundAPI.deleteDOJInbound");

        maintainDOJLiability.deleteLiability((BDMExternalLiabilityKey) any);
        times = 0;

        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        times = 2;

      }
    };
  }

  @Test
  public void testGetDOJInboundDetails(@Mocked
  final ConcernRoleAlternateIDFactory concernRoleAlternateID)
    throws AppException, InformationalException {

    new Expectations() {

      {
        final ConcernRoleAlternateIDDtls concernRoleAlternateIDDtls =
          new ConcernRoleAlternateIDDtls();
        concernRoleAlternateIDDtls.concernRoleID = 256;
        ConcernRoleAlternateIDFactory.newInstance()
          .readByAltIDTypeCode(with(new Delegate<NotFoundIndicator>() {

            boolean delegate(final NotFoundIndicator notFoundIndicator) {

              notFoundIndicator.setNotFound(false);
              return true;
            }
          }), withAny(new AlternateIDTypeCodeKey()));
        result = concernRoleAlternateIDDtls;

      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.obligation.person.personSINIdentification = 123456789;
    dojInboundData.metadata.transactionControlIdentificationID = "100";
    dojInboundData.obligation.person.personObligationID = "A";
    dojInboundData.obligation.person.personObligationIDSuffix = "AA";

    BDMDoJInboundDetails bdmDoJInboundDetails = new BDMDoJInboundDetails();
    final String restActionType = BDMRESTACTIONTYPE.BDM_REST_POST;
    final BDMAPIAuditDetails bdmapiAuditDetails = null;
    bdmDoJInboundDetails = bdmDoJInboundAPI.getDOJInboundDetails(
      dojInboundData, restActionType, bdmapiAuditDetails);

    assertEquals(bdmDoJInboundDetails.getConcernRoleID(), 256);
    assertEquals(bdmDoJInboundDetails.getPersonSINIdentification(),
      123456789);
    assertEquals(bdmDoJInboundDetails.getPersonObligationIDSuffix(), "AA");
    assertEquals(bdmDoJInboundDetails.getPersonObligationID(), "A");

  }

  @Test
  public void testGetDOJInboundDetails_SINNotFound(@Mocked
  final ConcernRoleAlternateIDFactory concernRoleAlternateID)
    throws AppException, InformationalException {

    new Expectations() {

      {
        final ConcernRoleAlternateIDDtls concernRoleAlternateIDDtls =
          new ConcernRoleAlternateIDDtls();
        concernRoleAlternateIDDtls.concernRoleID = 256;
        ConcernRoleAlternateIDFactory.newInstance()
          .readByAltIDTypeCode(with(new Delegate<NotFoundIndicator>() {

            boolean delegate(final NotFoundIndicator notFoundIndicator) {

              notFoundIndicator.setNotFound(true);
              return true;
            }
          }), withAny(new AlternateIDTypeCodeKey()));
        result = concernRoleAlternateIDDtls;

        new MockUp<BDMRestUtil>() {

          @Mock
          public void throwHTTP404Status(final AppException appException,
            final BDMAPIAuditDetails bdmapiAuditDetails)
            throws AppException, InformationalException {

            assertEquals(appException.getMessage(),
              BDMRESTAPIERRORMESSAGE.HTTP_404_RESOURCE_NOT_FOUND_SINERROR_DOJ
                .getMessageText());
            throw appException;
          }

        };
      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.obligation.person.personSINIdentification = 123456789;
    dojInboundData.metadata.transactionControlIdentificationID = "100";
    dojInboundData.obligation.person.personObligationID = "A";
    dojInboundData.obligation.person.personObligationIDSuffix = "AA";

    final String restActionType = BDMRESTACTIONTYPE.BDM_REST_POST;
    final BDMAPIAuditDetails bdmapiAuditDetails = null;
    try {
      bdmDoJInboundAPI.getDOJInboundDetails(dojInboundData, restActionType,
        bdmapiAuditDetails);
    } catch (final Exception e) {
      assertTrue(true);
    }

  }

  @Test
  public void testProcessDOJDataIntoStagingTable(@Mocked
  final BDMDoJInboundStagingFactory bdmDoJInboundStagingFactory)
    throws AppException, InformationalException {

    new Expectations() {

      {
        bdmDoJInboundStagingFactory.insert((BDMDoJInboundStagingDtls) any);
      }
    };
    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    final String restActionType = BDMRESTACTIONTYPE.BDM_REST_POST;
    final long concernroleID = 100L;

    dojInboundData.obligation.person.personSINIdentification = 123456789;
    dojInboundData.metadata.transactionControlIdentificationID = "100";
    dojInboundData.obligation.person.personObligationID = "A";
    dojInboundData.obligation.person.personObligationIDSuffix = "AA";

    bdmDoJInboundAPI.processDOJDataIntoStagingTable(dojInboundData,
      restActionType, concernroleID);

    new Verifications() {

      {
        BDMDoJInboundStagingDtls bdmDoJInboundStagingDtls =
          new BDMDoJInboundStagingDtls();
        bdmDoJInboundStagingFactory
          .insert(bdmDoJInboundStagingDtls = withCapture());
        times = 1;

        assertEquals(bdmDoJInboundStagingDtls.concernRoleID, 100);
        assertEquals(bdmDoJInboundStagingDtls.restActionType,
          BDMRESTACTIONTYPE.BDM_REST_POST);
        assertEquals(bdmDoJInboundStagingDtls.status,
          BDMDOJRECORDSTATUS.PENDING);
        assertEquals(bdmDoJInboundStagingDtls.personObligationID, "A");
        assertEquals(bdmDoJInboundStagingDtls.personObligationIDSuffix, "AA");
        assertEquals(bdmDoJInboundStagingDtls.personSINIdentification,
          123456789);
      }
    };
  }

  @Test
  public void testProcessDOJInbound_exceptionScenario(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype, @Mocked
  final BDMRestUtil bdmRestUtil) throws AppException, InformationalException {

    new Expectations() {

      {

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability
          .registerLiability((BDMRegisterDOJLbyDetails) any);
        BDMBENEFITPROGRAMTYPE.getDefaultCode();
        result = "OAS";

        mockUpBDMDoJInboundAPI_exception("100");

        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);

      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.obligation.person.personSINIdentification = 123456789;
    try {
      bdmDoJInboundAPI.processDOJInbound(dojInboundData);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;

        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 1;
      }
    };
  }

  @Test
  public void testModifyDOJInbound_exceptionScenario(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype, @Mocked
  final BDMExternalLiabilityFactory externalLiabilityFactory, @Mocked
  final BDMRestUtil bdmRestUtil) throws AppException, InformationalException {

    new Expectations() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability.modifyLiability((BDMModifyDOJLbyDetails) any);
        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        result = new BDMExternalLiabilityKey();

        BDMExternalLiabilityFactory.newInstance()
          .read(with(new Delegate<NotFoundIndicator>() {

            boolean delegate(final NotFoundIndicator notFoundIndicator) {

              notFoundIndicator.setNotFound(false);
              return true;
            }
          }), withAny(new BDMExternalLiabilityKey()));
        result = new BDMExternalLiabilityDtls();

        mockUpBDMDoJInboundAPI_exception("100");

        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";

    dojInboundData.obligation.person.personSINIdentification = 123456789;

    try {
      bdmDoJInboundAPI.modifyDOJInbound(dojInboundData);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;
        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 1;

      }
    };
  }

  @Test
  public void testDeleteDOJInbound_exceptionScenario(@Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil, @Mocked
  final BDMInterfaceLogger bdmInterfaceLogger, @Mocked
  final MaintainDOJLiability maintainDOJLiability, @Mocked
  final BDMBENEFITPROGRAMTYPE bdmbenefitprogramtype, @Mocked
  final BDMExternalLiabilityFactory externalLiabilityFactory, @Mocked
  final BDMRestUtil bdmRestUtil) throws AppException, InformationalException {

    new Expectations() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        minTimes = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);

        maintainDOJLiability.deleteLiability((BDMExternalLiabilityKey) any);
        maintainDOJLiability
          .getLiabilityByExtRefNumber((BDMCheckDOJLiabilityExistDetails) any);
        result = new BDMExternalLiabilityKey();

        mockUpBDMDoJInboundAPI_exception("100");

        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
      }
    };

    final BDMObligationExchange dojInboundData = new BDMObligationExchange();
    dojInboundData.cloudEvent.id = "a280d89b-4760-4784-9968-fdafc8ac1944";
    dojInboundData.cloudEvent.source = "TEST";
    dojInboundData.metadata.metadataIdentificationID =
      "b280d89b-4760-4784-9968-fdafc8ac1944";

    dojInboundData.obligation.person.personSINIdentification = 123456789;

    try {
      bdmDoJInboundAPI.deleteDOJInbound(dojInboundData);
    } catch (final Exception e) {
      assertTrue(true);
    }

    new Verifications() {

      {
        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);
        times = 0;
        bdmInterfaceLogger.logRestAPIPerf(anyString, anyLong, anyString);
        times = 1;

        BDMRestUtil.auditErrorResponse((AppException) any, anyInt,
          (BDMAPIAuditDetails) any);
        times = 1;
      }
    };
  }

  void mockUpBDMDoJInboundAPI(final String balance) {

    new MockUp<BDMDoJInboundAPI>() {

      @Mock
      BDMDoJInboundDetails getDOJInboundDetails(
        final BDMObligationExchange dojInboundData,
        final String restActionType,
        final BDMAPIAuditDetails bdmapiAuditDetails)
        throws AppException, InformationalException {

        final BDMDoJInboundDetails bdmDoJInboundDetails =
          new BDMDoJInboundDetails();
        final BDMObligationType obligation = new BDMObligationType();
        obligation.obligationOutstandingFeeBalance = new Money(balance);
        bdmDoJInboundDetails.setObligation(obligation);
        return bdmDoJInboundDetails;
      }

      @Mock
      void processDOJDataIntoStagingTable(
        final BDMObligationExchange dojInboundData,
        final String restActionType, final long concernroleID)
        throws InformationalException, AppException {

      }
    };
  }

  void mockUpBDMDoJInboundAPI_exception(final String balance) {

    new MockUp<BDMDoJInboundAPI>() {

      @Mock
      BDMDoJInboundDetails getDOJInboundDetails(
        final BDMObligationExchange dojInboundData,
        final String restActionType,
        final BDMAPIAuditDetails bdmapiAuditDetails)
        throws AppException, InformationalException {

        final BDMDoJInboundDetails bdmDoJInboundDetails =
          new BDMDoJInboundDetails();
        final BDMObligationType obligation = new BDMObligationType();
        obligation.obligationOutstandingFeeBalance = new Money(balance);
        bdmDoJInboundDetails.setObligation(obligation);
        return bdmDoJInboundDetails;
      }

      @Mock
      void processDOJDataIntoStagingTable(
        final BDMObligationExchange dojInboundData,
        final String restActionType, final long concernroleID)
        throws AppException, InformationalException {

        throw new RuntimeException();
      }
    };
  }
  // END, ADO-55810, Gaps implementation for DOJ Inbound
}
