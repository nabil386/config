package curam.ca.gc.bdm.test.facade.help.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.facade.help.fact.BDMHelpFactory;
import curam.ca.gc.bdm.facade.help.intf.BDMHelp;
import curam.ca.gc.bdm.facade.help.struct.BDMIOHelpTextDetails;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** Test class for BDmHelp */
public class BDMHelpTest extends CuramServerTestJUnit4 {

  BDMHelp bdmHelpObj = null;

  @Override
  protected boolean shouldCommit() {

    return false;
  }

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmHelpObj = BDMHelpFactory.newInstance();
  }

  @After
  public void tearDown() throws AppException, InformationalException {

    Configuration.setProperty(EnvVars.kInternalTimeoutWarningDialogTimePeriod,
      "120");
  }

  @Test
  public void testGetIOKnowledgeArticles()
    throws AppException, InformationalException {

    BDMIOHelpTextDetails bdmIOHelpTextDetails = new BDMIOHelpTextDetails();

    bdmIOHelpTextDetails = bdmHelpObj.getIOKnowledgeArticles();

    assertEquals("2", bdmIOHelpTextDetails.helpText);

    Configuration.setProperty(EnvVars.kInternalTimeoutWarningDialogTimePeriod,
      "300");

    bdmIOHelpTextDetails = bdmHelpObj.getIOKnowledgeArticles();

    assertEquals("5", bdmIOHelpTextDetails.helpText);

  }

}
