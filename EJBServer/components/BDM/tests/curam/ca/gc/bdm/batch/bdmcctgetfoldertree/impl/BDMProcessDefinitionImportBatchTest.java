package curam.ca.gc.bdm.batch.bdmcctgetfoldertree.impl;

import curam.ca.gc.bdm.batch.bdmprocessdefinitionimportbatch.fact.BDMProcessDefinitionImportBatchFactory;
import curam.ca.gc.bdm.batch.bdmprocessdefinitionimportbatch.intf.BDMProcessDefinitionImportBatch;
import curam.ca.gc.bdm.batch.bdmprocessdefinitionimportbatch.struct.BDMProcessDefinitionDetails;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTFolderFactory;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import org.junit.Test;

/**
 * Suite of tests to test the BDMCCTGetFolderTreeBatch class.
 */
public class BDMProcessDefinitionImportBatchTest
  extends CuramServerTestJUnit4 {

  /**
   * Test process() to check that it correctly loads templates and folders to
   * the
   * Curam tables.
   *
   * @throws Exception
   */
  @Test
  public void testProcess_success() throws Exception {

    final BDMProcessDefinitionDetails details =
      new BDMProcessDefinitionDetails();

    details.workflowDirectory = "/path/directory";

    final BDMProcessDefinitionImportBatch testSubject =
      BDMProcessDefinitionImportBatchFactory.newInstance();

    testSubject.process(details);

    // These commands will throw an exception if there is no data in the tables,
    // however if process() has run correctly then there should be data to
    // remove
    BDMCCTFolderFactory.newInstance().removeAll();
    BDMCCTTemplateFactory.newInstance().removeAll();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }
}
