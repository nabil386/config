package curam.rules.functions;

import curam.core.impl.EnvVars;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.GeneralConstants;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import static curam.rules.functions.CustomFunctionConstants.ENTITY_INTAKE_APPLICATION_TYPE;
import static curam.rules.functions.CustomFunctionConstants.INTAKE_APPLICATION_TYPE_ID;

/**
 * Contains a custom function that will check if an application already
 * exists in SPM for that SIN.
 *
 * @curam.exclude
 */
public class CustomFunctionValidateSINSPM extends SINValidator {

  /**
   * A custom function that will check if an application already exists in SPM
   * for that SIN..
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the SIN is valid.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    if (!Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_SIN_SPM_VALIDATION)) {
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final Entity rootEntity = readRoot(rulesParameters);

    final boolean validSIN = getContinueSIN(rootEntity);

    if (!validSIN) {
      // previous sin check has failed, don't run this one
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    final String sin =
      ((StringAdaptor) getParameters().get(0)).getStringValue(rulesParameters)
        .replaceAll(GeneralConstants.kSpace, GeneralConstants.kEmpty);

    final Datastore ds = super.getDatastore(rulesParameters);
    final Entity intakeApplicationTypeEntity = rootEntity
      .getChildEntities(ds.getEntityType(ENTITY_INTAKE_APPLICATION_TYPE))[0];
    final long intakeApplicationTypeId = Long.parseLong(
      intakeApplicationTypeEntity.getAttribute(INTAKE_APPLICATION_TYPE_ID));

    final boolean returnValue =
      !super.isSINRegisteredIntakeApplication(sin, intakeApplicationTypeId);

    updateContinueSIN(rootEntity, returnValue);

    return AdaptorFactory.getBooleanAdaptor(returnValue);
  }

}
