package curam.ca.gc.bdm.util.integrity.impl;

import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSearchTable;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMSINIntegrityCheckResult {

  private String validationCheckResult = null;

  private String mismatchCheckResult = null;

  private BDMSINSIRRestResponse sirResponse = null;

  private BDMSINSIRSearchTable sirRequest = null;

  private BDMSINIntegrityCheckDetails details = null;

  private long sirResponseEvidenceID = 0;

  public BDMSINIntegrityCheckResult(final BDMSINSIRSearchTable sirRequest,
    final BDMSINSIRRestResponse sirResponse,
    final BDMSINIntegrityCheckDetails details)
    throws AppException, InformationalException {

    this.sirResponse = sirResponse;
    this.sirRequest = sirRequest;
    this.details = details;
  }

  public BDMSINSIRRestResponse getSIRResponse() {

    return sirResponse;
  }

  public BDMSINSIRSearchTable getSIRRequest() {

    return sirRequest;
  }

  public BDMSINIntegrityCheckDetails getDetails() {

    return details;
  }

  public String getValidationCheckResult() {

    if (validationCheckResult != null) {
      return validationCheckResult;
    }

    // TODO call dates check util
    validationCheckResult = BDMSINIntegrityCheckConstants.CHECK_RESULT_NA;
    return validationCheckResult;
  }

  public String getMismatchCheckResult()
    throws AppException, InformationalException {

    if (mismatchCheckResult != null) {
      return mismatchCheckResult;
    }

    mismatchCheckResult = BDMSINMismatchCheckUtil.mismatchCheck(this);
    return mismatchCheckResult;
  }

  public long getSirResponseEvidenceID() {

    return this.sirResponseEvidenceID;
  }

  public void setSirResponseEvidenceID(final long sirResponseEvidenceID) {

    this.sirResponseEvidenceID = sirResponseEvidenceID;
  }

}
