package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs;

import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.BDMCCTOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCancelPrintRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTUpdateCorrespondenceRequest;
import curam.ca.gc.bdm.util.rest.impl.BDMRestRequest;
import org.junit.Ignore;

/**
 * Stubbed class to test for IOExceptions in the API calls in
 * BDMCCTOutboundInterfaceImpl
 */
@Ignore
public class BDMCCTOutboundInterfaceIOExceptionStub
  extends BDMCCTOutboundInterfaceImpl {

  /**
   * Overriding method for connection method, which creates a request object but
   * sets it's method type to null, thus inducing an IOException when callAPI()
   * is
   * called next.
   */
  @Override
  protected BDMRestRequest setCorrespondenceConnection(
    final BDMCCTCreateCorrespondenceRequest createCorrespondenceRequest) {

    final BDMRestRequest request = new BDMRestRequest();
    request.setMethod(null);
    return request;
  }

  /**
   * Overriding method for connection method, which creates a request object but
   * sets it's method type to null, thus inducing an IOException when callAPI()
   * is
   * called next.
   */
  @Override
  protected BDMRestRequest setGetFolderConnection(
    final BDMCCTGetFolderTreeRequest getFolderTreeRequest) {

    final BDMRestRequest request = new BDMRestRequest();
    request.setMethod(null);
    return request;
  }

  /**
   * Overriding method for connection method, which creates a request object but
   * sets it's method type to null, thus inducing an IOException when callAPI()
   * is
   * called next.
   */
  @Override
  protected BDMRestRequest setCancelPrintConnection(
    final BDMCCTCancelPrintRequest cancelPrintRequest) {

    final BDMRestRequest request = new BDMRestRequest();
    request.setMethod(null);
    return request;
  }

  /**
   * Overriding method for connection method, which creates a request object but
   * sets it's method type to null, thus inducing an IOException when callAPI()
   * is
   * called next.
   */
  @Override
  protected BDMRestRequest setWorkItemStatusConnection(
    final BDMCCTGetWorkItemStatusRequest getWorkItemStatusRequest) {

    final BDMRestRequest request = new BDMRestRequest();
    request.setMethod(null);
    return request;
  }

  /**
   * Overriding method for connection method, which creates a request object but
   * sets it's method type to null, thus inducing an IOException when callAPI()
   * is
   * called next.
   */
  @Override
  protected BDMRestRequest setGetCompletedPDFConnection(
    final BDMCCTGetCompletedPDFRequest getCompletedPDFRequest) {

    final BDMRestRequest request = new BDMRestRequest();
    request.setMethod(null);
    return request;
  }

  /**
   * Overriding method for connection method, which creates a request object but
   * sets it's method type to null, thus inducing an IOException when callAPI()
   * is
   * called next.
   */
  @Override
  protected BDMRestRequest setUpdateCorrespondenceConnection(
    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest) {

    final BDMRestRequest request = new BDMRestRequest();
    request.setMethod(null);
    return request;
  }
}
