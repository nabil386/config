package curam.ca.gc.bdm.application.impl;

import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationMappingEvents;

/**
 * BDM UA application events.
 *
 */
public class BDMIntakeApplicationMappingEvents
  extends IntakeApplicationMappingEvents {

  /*
   * Perform any required pre-mapping tasks.
   *
   * @param intakeApplication
   * The intake application in question.
   *
   * @throws AppException
   * Generic Exception Signature.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   */
  @Override
  public void preCreateCasesInCuram(final IntakeApplication intakeApplication)
    throws AppException, InformationalException {

    final long rootEntityID = intakeApplication.getRootEntityID();
    mapSINtoSSN(rootEntityID);

  }

  /**
   * Map the value captured in 'Person.sin' to the 'Person.ssn' datastore
   * attribute.
   *
   * The 'Person.ssn' attribute has a special meaning in Universal Access and
   * is used when creating the primary alternate ID for a person during the
   * application submission process.
   *
   * It is being set here so that 'sin' is mapped correctly as
   * the primary alternate ID on the Person record by downstream processing.
   *
   * See: curam.workspaceservices.applicationprocessing.impl.
   * IntakeClientManagementImpl.registerPerson(ProspectPersonMappingBean)
   *
   * @param rootEntityID The root datastore entity identifier.
   */
  private void mapSINtoSSN(final long rootEntityID) {

    final Entity[] personEntities = BDMApplicationEventsUtil
      .getEntities(rootEntityID, BDMDatastoreConstants.PERSON);

    for (final Entity personEntity : personEntities) {

      final String sin =
        getStringAttribute(personEntity, BDMDatastoreConstants.SIN);

      if (sin != null) {
        // SIN is stored formatted ex. "234 234 234" but it must be unformatted
        // for ssn
        final String unformattedSIN = sin.replace(" ", "");

        /*
         * Map the value captured in 'sin' to the 'ssn' field. The name of this
         * field is special in Universal Access and is used when creating the
         * primary alternate ID for a person during the application submission
         * process. It is being set here so that 'sin' is mapped correctly as
         * the primary alternate ID on the Person record.
         *
         * See: curam.workspaceservices.applicationprocessing.impl.
         * IntakeClientManagementImpl.registerPerson(ProspectPersonMappingBean)
         */
        personEntity.setAttribute(BDMDatastoreConstants.SSN, unformattedSIN);

      }
    }
  }

  /**
   * Retrieves the String attribute value from the entity for the key or null if
   * empty or not found.
   *
   * @param entity {@link Entity}
   * @param key String
   * @return String or null if not found.
   */
  private String getStringAttribute(final Entity entity, final String key) {

    final String value = entity.getAttribute(key);

    if (StringUtil.isNullOrEmpty(value) && Trace.atLeast(Trace.kTraceOn)) {

      final String warning = String.format("UA mapping: No '"
        + entity.getEntityType().getName() + "." + key
        + "' value found in datastore for Entity ID " + entity.getUniqueID());

      Trace.kTopLevelLogger.warn(warning);

      return null;

    }
    return value;
  }

  @Override
  public void
    preMappingValidationsDisabled(final IntakeApplication intakeApplication)
      throws AppException, InformationalException {

    // Nothing to do

  }

}
