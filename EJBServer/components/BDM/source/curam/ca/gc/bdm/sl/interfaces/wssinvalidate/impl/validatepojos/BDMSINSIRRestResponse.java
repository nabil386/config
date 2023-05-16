package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

/**
 * Consolidated return object of SIN/SIR validation
 *
 * @author raghunath.govindaraj
 *
 */
public class BDMSINSIRRestResponse {

  private BDMSINSIRValidation validatedSINResults;

  private BDMSINSIRResponseError sinErrorResponseResults;

  private boolean isSINSIRValidatonSuccess = false;

  public BDMSINSIRValidation getValidatedSINResults() {

    return this.validatedSINResults;
  }

  public void
    setValidatedSINResults(final BDMSINSIRValidation validatedSINResults) {

    this.validatedSINResults = validatedSINResults;
  }

  public BDMSINSIRResponseError getSinErrorResponseResults() {

    return this.sinErrorResponseResults;
  }

  public void setSinErrorResponseResults(
    final BDMSINSIRResponseError sinErrorResponseResults) {

    this.sinErrorResponseResults = sinErrorResponseResults;
  }

  public boolean isSINSIRValidatonSuccess() {

    return this.isSINSIRValidatonSuccess;
  }

  public void
    setIsSINSIRValidatonSuccess(final boolean isSINSIRValidatonSuccess) {

    this.isSINSIRValidatonSuccess = isSINSIRValidatonSuccess;
  }

}
