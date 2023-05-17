package curam.ca.gc.bdm.communication.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import curam.core.impl.ConcernRoleDocumentGeneration;
import curam.core.impl.ConcernRoleDocuments;
import curam.core.sl.intf.Communication;

public class BDMCommunicationModule extends AbstractModule {

  @Override
  protected void configure() {

    // COMMUNICATION
    // final Multibinder<Communication> customCommunicationImpl =
    // Multibinder.newSetBinder(binder(), Communication.class);

    // customCommunicationImpl.addBinding()
    // .toProvider(BDMCommunicationImpl.class);

    // COMMUNICATION

    // CONCERNROLEDOCUMENT
    final Multibinder<ConcernRoleDocuments> customConcernRoleDocumentsImpl =
      Multibinder.newSetBinder(binder(), ConcernRoleDocuments.class);

    customConcernRoleDocumentsImpl.addBinding()
      .to(BDMConcernRoleDocumentsImpl.class);

    // CONCERNROLEDOCUMENT
    final Multibinder<ConcernRoleDocumentGeneration> customConcernRoleDocumentGenerationImpl =
      Multibinder.newSetBinder(binder(), ConcernRoleDocumentGeneration.class);

    customConcernRoleDocumentGenerationImpl.addBinding()
      .to(BDMConcernRoleDocumentGenerationImpl.class);

    bind(Communication.class).to(BDMCommunicationImpl.class);

  }

}
