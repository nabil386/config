package curam.ca.gc.bdm.wizard.datastore.impl;

import com.google.inject.AbstractModule;
import curam.wizard.datastore.impl.ProductWizardDatastore;

public class BDMProductWizardModule extends AbstractModule {

  @Override
  protected void configure() {

    binder().bind(ProductWizardDatastore.class)
      .to(BDMProductWizardDatastoreImpl.class);

  }
}
