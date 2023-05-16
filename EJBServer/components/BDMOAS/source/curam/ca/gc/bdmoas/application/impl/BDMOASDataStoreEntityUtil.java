package curam.ca.gc.bdmoas.application.impl;

import curam.codetable.COUNTRY;
import curam.codetable.IEGYESNO;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;

/**
 * @author SMisal
 *
 * Class to Read Details from the Evidence and set to entity
 *
 */
public class BDMOASDataStoreEntityUtil {

  public BDMOASDataStoreEntityUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  public Entity getAndSetLegalStatusEntity(final Entity personEntity,
    final Datastore datastore, final long concernRoleID)
    throws AppException, InformationalException {

    final Entity[] legalStatusEntityList = personEntity.getChildEntities(
      datastore.getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY));

    Entity legalStatusEntity = null;

    if (legalStatusEntityList != null && legalStatusEntityList.length > 0) {
      legalStatusEntity = legalStatusEntityList[0];
    } else {
      legalStatusEntity = datastore.newEntity(datastore
        .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY));
      personEntity.addChildEntity(legalStatusEntity);
      personEntity.update();
    }

    final String countryOfBirth =
      personEntity.getAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH);

    if (!StringUtil.isNullOrEmpty(countryOfBirth)) {
      String isBornOutsideOfCanada = "";
      if (countryOfBirth.equals(COUNTRY.CA)) {
        isBornOutsideOfCanada = IEGYESNO.NO;
      } else if (!countryOfBirth.equals(COUNTRY.CA)) {
        isBornOutsideOfCanada = IEGYESNO.YES;
      }

      legalStatusEntity.setAttribute(
        BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE,
        isBornOutsideOfCanada);
      legalStatusEntity.update();
    }

    personEntity.update();
    return personEntity;
  }
}
