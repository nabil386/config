package curam.ca.gc.bdm.batch.bdmcctgetfoldertree.impl;

import curam.ca.gc.bdm.batch.bdmcctgetfoldertree.fact.BDMCCTGetFolderTreeBatchFactory;
import curam.ca.gc.bdm.batch.bdmcctgetfoldertree.intf.BDMCCTGetFolderTreeBatch;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTFolderFactory;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.impl.EnvVars;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Configuration;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Suite of tests to test the BDMCCTGetFolderTreeBatch class.
 */
public class BDMCCTGetFolderTreeBatchTest extends CuramServerTestJUnit4 {

  /**
   * Test process() to check that it correctly loads templates and folders to the
   * Curam tables.
   *
   * @throws Exception
   */
  @Test
  public void testProcess_success() throws Exception {

    final BDMCCTGetFolderTreeBatch testSubject =
      BDMCCTGetFolderTreeBatchFactory.newInstance();

    testSubject.process();

    // These commands will throw an exception if there is no data in the tables,
    // however if process() has run correctly then there should be data to remove
    BDMCCTFolderFactory.newInstance().removeAll();
    BDMCCTTemplateFactory.newInstance().removeAll();
  }

  /**
   * Test process() in scenario where an invalid community is set for the
   * community query parameter.
   *
   * @throws Exception
   */
  @Test
  public void testProcess_invalidCommunity() throws Exception {

    final BDMCCTGetFolderTreeBatch testSubject =
      BDMCCTGetFolderTreeBatchFactory.newInstance();

    final String property =
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE);
    Configuration.setProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE,
      "IBM-TEST");

    testSubject.process();

    // Check that nothing was inserted into the tables
    assertThrows(RecordNotFoundException.class,
      () -> BDMCCTFolderFactory.newInstance().removeAll());
    assertThrows(RecordNotFoundException.class,
      () -> BDMCCTTemplateFactory.newInstance().removeAll());

    Configuration.setProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE, property);
  }

  /**
   * Test process() in scenario where the endpoint URL is incorrect and thus the
   * call can't be made.
   *
   * @throws Exception
   */
  @Test
  public void testProcess_invalidURL() throws Exception {

    final BDMCCTGetFolderTreeBatch testSubject =
      BDMCCTGetFolderTreeBatchFactory.newInstance();

    final String property =
      Configuration.getProperty(EnvVars.BDM_CCT_GET_FOLDER_TREE_URL);
    Configuration.setProperty(EnvVars.BDM_CCT_GET_FOLDER_TREE_URL, "");

    testSubject.process();

    // Check that nothing was inserted into the tables
    assertThrows(RecordNotFoundException.class,
      () -> BDMCCTFolderFactory.newInstance().removeAll());
    assertThrows(RecordNotFoundException.class,
      () -> BDMCCTTemplateFactory.newInstance().removeAll());

    Configuration.setProperty(EnvVars.BDM_CCT_GET_FOLDER_TREE_URL, property);
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }
}
