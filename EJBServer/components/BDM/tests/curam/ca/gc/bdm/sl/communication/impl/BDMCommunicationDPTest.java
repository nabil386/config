package curam.ca.gc.bdm.sl.communication.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.entity.communication.fact.BDMCorrespondenceStagingFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingJobID;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingKeyList;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingStatus;
import curam.core.base.WMInstanceData;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.struct.WMInstanceDataDtls;
import curam.core.struct.WMInstanceDataKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class BDMCommunicationDPTest extends CuramServerTestJUnit4 {

  BDMCommunicationDP commDPObj;

  @Before
  public void setUp() throws AppException, InformationalException {

    commDPObj = new BDMCommunicationDP();

  }

  @Test
  public void testUpdateJobStatusDP_instIDNotFound() throws Exception {

    new Expectations() {

      {
        new MockUp<WMInstanceData>() {

          @Mock
          public WMInstanceDataDtls read(final NotFoundIndicator nf,
            final WMInstanceDataKey wmInstanceDataKey) {

            nf.setNotFound(true);
            return new WMInstanceDataDtls();
          }
        };
      }
    };

    commDPObj.updateJobStatusDP(100, 200, false);
    new Verifications() {

      {
        BDMCorrespondenceStagingFactory.newInstance().modifyStatusByJobID(
          (BDMCorrespondenceStagingJobID) any,
          (BDMCorrespondenceStagingStatus) any);
        times = 0;
      }

    };
  }

  @Test
  public void testUpdateJobStatusDP_jobIDDoestExist(@Mocked
  final WMInstanceDataFactory wmInstObj, @Mocked
  final BDMCorrespondenceStagingFactory stagingObj) throws Exception {

    new Expectations() {

      {
        final WMInstanceDataDtls instanceDataDtls = new WMInstanceDataDtls();
        instanceDataDtls.comments = "123";
        wmInstObj.read((NotFoundIndicator) any, (WMInstanceDataKey) any);
        result = instanceDataDtls;

        final BDMCorrespondenceStagingKeyList keyList =
          new BDMCorrespondenceStagingKeyList();
        stagingObj.searchByJobID((BDMCorrespondenceStagingJobID) any);
        result = keyList;
      }
    };

    commDPObj.updateJobStatusDP(100, 200, false);
    new Verifications() {

      {
        BDMCorrespondenceStagingFactory.newInstance().modifyStatusByJobID(
          (BDMCorrespondenceStagingJobID) any,
          (BDMCorrespondenceStagingStatus) any);
        times = 0;
      }

    };
  }

}
