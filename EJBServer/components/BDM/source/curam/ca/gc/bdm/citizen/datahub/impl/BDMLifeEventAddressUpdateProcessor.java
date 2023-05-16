package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.citizen.datahub.impl.CustomUpdateProcessor;
import curam.citizen.datahub.impl.DifferenceCommand;
import curam.common.util.xml.dom.Document;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.workspaceservices.codetable.impl.DataHubUpdateProcessorTypeEntry;

public class BDMLifeEventAddressUpdateProcessor
  implements CustomUpdateProcessor {

  @Inject
  private ConcernRoleDAO concernroleDAO;

  @Inject
  private BDMUtil bdmUtil;

  protected BDMLifeEventAddressUpdateProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void processUpdate(
    final DataHubUpdateProcessorTypeEntry dataHubUpdateProcessorType,
    final String context, final Entity entity,
    final DifferenceCommand diffCommand) {

    final Document datastoreDifferenceXML = diffCommand.convertToXML();

    try {
      updateAddressInformation(entity, datastoreDifferenceXML);
    } catch (AppException | InformationalException
      | NoSuchSchemaException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param rootEntity
   * @param datastoreDifferenceXML
   * @throws AppException
   * @throws InformationalException
   * @throws NoSuchSchemaException
   */
  private void updateAddressInformation(final Entity rootEntity,
    final Document datastoreDifferenceXML)
    throws AppException, InformationalException, NoSuchSchemaException {

    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];

    final Long concernRoleID = Long.parseLong(
      personEntity.getAttribute(BDMLifeEventDatastoreConstants.LOCAL_ID));
    final ConcernRole concernRole = concernroleDAO.get(concernRoleID);

    // convert international datastore entities to address entities, then
    // process the update
    try {
      bdmUtil.mapIntlAddressesToAddressDatastore(rootEntity);
      bdmUtil.processAddressEvd(personEntity, concernRole);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

}
