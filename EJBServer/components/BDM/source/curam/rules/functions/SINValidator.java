package curam.rules.functions;

import curam.ca.gc.bdm.sl.bdmintakeapplicationsin.fact.BDMIntakeApplicationSINFactory;
import curam.ca.gc.bdm.sl.bdmintakeapplicationsin.intf.BDMIntakeApplicationSIN;
import curam.ca.gc.bdm.sl.bdmintakeapplicationsin.struct.SINKey;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.GeneralConstants;
import static curam.rules.functions.CustomFunctionConstants.CONTINUE_ATTRIBUTE;

/**
 * Abstract class that can be extended by Custom Functions related to SIN
 * validation.
 *
 * @curam.exclude
 */
public abstract class SINValidator extends BDMFunctor {

  public static final int SIN_LENGTH = 9;

  public static final String SIN_MASK_SEPARATOR = GeneralConstants.kSpace;

  /**
   * Gets the continue SIN attribute, it is used as a flag to execute cascade
   * validations.
   *
   * @param rootEntity the root entity
   * @return the continue SIN
   */
  protected boolean getContinueSIN(final Entity rootEntity) {

    return (Boolean) rootEntity.getTypedAttribute(CONTINUE_ATTRIBUTE);
  }

  /**
   * Update continue SIN.
   *
   * @param rootEntity the root entity
   * @param continueSIN the continue SIN
   */
  protected void updateContinueSIN(final Entity rootEntity,
    final boolean continueSIN) {

    rootEntity.setTypedAttribute(CONTINUE_ATTRIBUTE, continueSIN);
    rootEntity.update();
  }

  /**
   * Checks if this SIN and IntakeApplicationType combination is already
   * registered.
   *
   * @param sin the sin
   * @param intakeApplicationTypeId the intake application type
   * @return true, if this SIN and IntakeApplicationType combination is already
   * registered
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  protected boolean isSINRegisteredIntakeApplication(final String sin,
    final long intakeApplicationTypeId)
    throws AppException, InformationalException {

    final BDMIntakeApplicationSIN sl =
      BDMIntakeApplicationSINFactory.newInstance();
    final SINKey key = new SINKey();
    key.sin = sin;
    key.intakeApplicationTypeID = intakeApplicationTypeId;
    return sl.isSINDuplicated(key).result;
  }

}
