package curam.ca.gc.bdm.wizard.datastore.impl;

import curam.wizard.datastore.impl.ProductWizardDatastore;

public class BDMProductWizardDatastoreFactory {

  /*    */ public static ProductWizardDatastore newDatastoreInstance() {

    /* 37 */ final ProductWizardDatastore result =
      new BDMProductWizardDatastoreImpl();
    /*    */
    /* 39 */ return result;
    /*    */ }
  /*    */

}
