package curam.ca.gc.bdm.test.facade.application.impl;

import curam.ca.gc.bdm.facade.application.fact.BDMFeedbackFactory;
import curam.ca.gc.bdm.facade.application.intf.BDMFeedback;
import curam.ca.gc.bdm.facade.application.struct.BDMFeedbackTextDetails;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMFeedbackTest extends CuramServerTestJUnit4 {

  BDMFeedback bdmFeedBackObj = null;

  @Override
  protected boolean shouldCommit() {

    return false;
  }

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmFeedBackObj = BDMFeedbackFactory.newInstance();
  }

  /// *Test timeout /
  @Test
  public void getTimeoutMinutes()
    throws AppException, InformationalException {

    BDMFeedbackTextDetails bdmFeedbackTextDetails =
      new BDMFeedbackTextDetails();

    bdmFeedbackTextDetails = bdmFeedBackObj.getTimeoutMinutes();

    assertEquals("2", bdmFeedbackTextDetails.minuteText);

    Configuration.setProperty(EnvVars.kInternalTimeoutWarningDialogTimePeriod,
      "180");

    bdmFeedbackTextDetails = bdmFeedBackObj.getTimeoutMinutes();

    assertEquals("3", bdmFeedbackTextDetails.minuteText);

  }

}
