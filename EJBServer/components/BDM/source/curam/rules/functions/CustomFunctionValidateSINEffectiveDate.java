package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.datastore.impl.Entity;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;

/**
 * Custom function to validate the SIN change effective date.
 *
 */
public class CustomFunctionValidateSINEffectiveDate extends BDMFunctor {

  /**
   * Instantiates a new custom function validate.
   */
  public CustomFunctionValidateSINEffectiveDate() {

    super();
  }

  /**
   * A custom function that will be called to validate the SIN change effective
   * date is after the current SIN evidence start date.
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the SIN effective date is valid.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    return validateSINChangeEffectiveDate(rulesParameters);

  }

  /**
   * Validate the SIN change effective date is after the current SIN evidence
   * start date.
   *
   * @param rulesParameters the rules sinRawDate parameters
   * @return the validation outcome
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private Adaptor
    validateSINChangeEffectiveDate(final RulesParameters rulesParameters)
      throws AppException, InformationalException {

    int index = 0;
    final Date sinRawDate = getOptionalDateParam(rulesParameters, index++);

    if (sinRawDate != null) {
      final Entity rootEntity = readRoot(rulesParameters);
      final Entity personEntity = BDMApplicationEventsUtil
        .retrieveChildEntity(rootEntity, BDMDatastoreConstants.PERSON);

      final long sinEvidenceID = personEntity
        .hasAttribute(BDMLifeEventDatastoreConstants.SIN_EVIDENCE_ID)
          ? Long.parseLong(personEntity
            .getAttribute(BDMLifeEventDatastoreConstants.SIN_EVIDENCE_ID))
          : 0;

      if (0 != sinEvidenceID
        && !isEffectiveDateValid(sinEvidenceID, sinRawDate)) {

        // effective date is on or before the current SIN start date
        return AdaptorFactory.getBooleanAdaptor(false);
      }
    } else {
      // sinRawDate is empty
      return AdaptorFactory.getBooleanAdaptor(true);
    }

    return AdaptorFactory.getBooleanAdaptor(true);
  }

  /**
   * Validate the SIN change effective date is after the current SIN evidence
   * start date.
   *
   * @param sinEvidenceID SIN evidence identifier
   * @param effectiveDate SIN change effective date
   *
   * @return true if the SIN change effective date is after current SIN start
   * date otherwise false
   */
  private boolean isEffectiveDateValid(final long sinEvidenceID,
    final Date effectiveDate) {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = sinEvidenceID;
    eiEvidenceKey.evidenceType = PDCConst.PDCIDENTIFICATION;

    EIEvidenceReadDtls evidenceReadDtls;

    try {
      evidenceReadDtls = evidenceControllerObj.readEvidence(eiEvidenceKey);

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final Date startDate = (Date) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute("fromDate"));

      if (startDate != null && !effectiveDate.after(startDate)) {

        return false;
      }

    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }

    return true;
  }
}
