package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs;

import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.BDMCCTOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCancelPrintRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusResponse;
import curam.ca.gc.bdm.util.rest.impl.BDMRestResponse;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.junit.Ignore;

/**
 * Stubbed class to test for variable response codes in the API calls in
 * BDMCCTOutboundInterfaceImpl
 */
@Ignore
public class BDMCCTOutboundInterfaceHttpStatusStub
  extends BDMCCTOutboundInterfaceImpl {

  private final StatusLine statusLine;

  /**
   * Constructor method.
   * Set the HTTP status to be used from the parameter.
   *
   * @param httpStatus - HTTP response code value (e.g. 200, 300)
   * @throws Exception
   */
  public BDMCCTOutboundInterfaceHttpStatusStub(final String httpStatus)
    throws Exception {

    super();
    statusLine =
      new StatusLine(String.format("HTTP/1.1 %s Test-Status", httpStatus));
  }

  /**
   * This method is to send the Create Correspondence Request to CCT and return
   * back the response
   *
   * @param templateReq
   * @return BDMCCTCreateCorrespondenceResponse
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMCCTCreateCorrespondenceResponse
    createCorrespondence(final BDMCCTCreateCorrespondenceRequest templateReq)
      throws AppException, InformationalException {

    final BDMRestResponse response = new BDMRestResponse();
    response.setStatusLine(statusLine);

    if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()
      && HttpStatus.SC_CREATED != response.getStatusLine().getStatusCode()) {
      throw processErrorResponse(response,
        BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    } else {
      return BDMRestUtil.convertFromJSON(response.getBody(),
        BDMCCTCreateCorrespondenceResponse.class);
    }
  }

  /**
   * Interface method to make /GetWorkItemStatus call to CCT to retrieve the
   * work
   * item status for a given work item.
   *
   * @param workItemStatusRequest - POJO representing parameters for
   * /GetWorkItemStatus
   * @return POJO representing response body of API call
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMCCTGetWorkItemStatusResponse getWorkItemStatus(
    final BDMCCTGetWorkItemStatusRequest workItemStatusRequest)
    throws AppException, InformationalException {

    final BDMRestResponse response = new BDMRestResponse();
    response.setStatusLine(statusLine);

    if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
      throw processErrorResponse(response, BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    } else {
      // On successful HTTP call, convert response body to POJO
      return BDMRestUtil.convertFromJSON(response.getBody(),
        BDMCCTGetWorkItemStatusResponse.class);
    }
  }

  /**
   * This method is to send the getCompletedPDF Request to CCT and return
   * back the response
   *
   * @param getCompletedPDFRequest
   * @return BDMCCTGetCompletedPDFResponse
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMCCTGetCompletedPDFResponse
    getCompletedPDF(final BDMCCTGetCompletedPDFRequest getCompletedPdfRequest)
      throws AppException, InformationalException {

    final BDMRestResponse response = new BDMRestResponse();
    response.setStatusLine(statusLine);

    if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
      throw processErrorResponse(response, BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    } else {
      // On successful HTTP call, deserialize response body to POJO
      return BDMRestUtil.convertFromJSON(response.getBody(),
        BDMCCTGetCompletedPDFResponse.class);
    }
  }

  /**
   * Interface method to make /CancelPrint call to CCT to cancel/void the
   * specified work item.
   *
   * @param cancelPrintRequest - POJO representing parameters for
   * /CancelPrint
   * @return Indicator of success of cancel print operation
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMRestResponse
    cancelPrint(final BDMCCTCancelPrintRequest cancelPrintRequest)
      throws AppException, InformationalException {

    final BDMRestResponse response = new BDMRestResponse();
    response.setStatusLine(statusLine);

    if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
      throw processErrorResponse(response,
        BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
    } else {
      // On successful HTTP call, deserialize response body to POJO
      return response;
    }
  }
}
