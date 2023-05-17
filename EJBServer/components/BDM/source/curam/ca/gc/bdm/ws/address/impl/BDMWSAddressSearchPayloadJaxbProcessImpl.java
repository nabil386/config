
package curam.ca.gc.bdm.ws.address.impl;

import curam.ca.gc.bdm.ws.address.gen.impl.Parm;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Output.AllResults.Results.Result;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Output.AllResults.Results.Result.AddressMatches.AddressMatch;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import curam.util.exception.AppException;

public class BDMWSAddressSearchPayloadJaxbProcessImpl
  implements BDMWSAddressXmlPayloadProcessJaxbStrategy {

  @Override
  public BDMWSAddressSearchResponse parse(final WSMessage wsMessage)
    throws AppException {

    final BDMWSAddressSearchResponse serchResponse =
      new BDMWSAddressSearchResponse();

    serchResponse.setResponseType(RequestResponseType.SEARCH);

    // FIXME holy nested loops batman this is an absolutely shocking On3 loop.
    // unfortunately we are constrained by the format of the XML response (e.g.
    // how nested it is and the number of one-to-many relationships with each
    // element) but there is likely better data structures that could reduce the
    // time-complexity cost
    for (final Result result : wsMessage.getOutput().getAllResults()
      .getResults().getResult()) {
      for (final AddressMatch addressMatch : result.getAddressMatches()
        .getAddressMatch()) {

        final BDMWSAddressDetail wsAddressResponse = new BDMWSAddressDetail();
        wsAddressResponse.setAddressType(addressMatch.getAddressType());

        for (final Parm parm : addressMatch.getParm()) {

          // TODO based on the address type, we should be using a specific
          // address
          // type object rather than a blanket search through all the fields
          mapResponseFieldToResponseObj(wsAddressResponse, parm);
        }
        serchResponse.getAddressMatchResults().add(wsAddressResponse);
      }
    }
    return serchResponse;
  }

  private void mapResponseFieldToResponseObj(
    final BDMWSAddressDetail wsAddressResponse, final Parm parm) {

    if (parm.getName().equalsIgnoreCase(BDMWSAddressConstants.kStreetName)) {
      wsAddressResponse.setStreetName(parm.getContent());
    }

    if (parm.getName().equalsIgnoreCase(BDMWSAddressConstants.kStreetType)) {
      wsAddressResponse.setStreetType(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kStreetDirection)) {
      wsAddressResponse.setStreetDirection(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kStreetAddressSequence)) {
      wsAddressResponse.setStreetAddressSequence(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kStreetNumberMinimum)) {
      wsAddressResponse.setStreetNumberMinimum(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kStreetNumberMaximum)) {
      wsAddressResponse.setStreetNumberMaximum(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kSuiteNumberMinimum)) {
      wsAddressResponse.setSuiteNumberMinimum(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kSuiteNumberMaximum)) {
      wsAddressResponse.setSuiteNumberMaximum(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kLockBoxNumberMinimum)) {
      wsAddressResponse.setLockboxNumberMinimum(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kLockBoxNumberMaximum)) {
      wsAddressResponse.setLockboxNumberMaximum(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kRouteServiceNumber)) {
      wsAddressResponse.setRouteServiceNumber(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kRouteServiceType)) {
      wsAddressResponse.setRouteServiceType(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kRuralRouteServiceNumber)) {
      wsAddressResponse.setRuralRouteServiceNumber(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kRuralRouteServiceType)) {
      wsAddressResponse.setRuralRouteServiceType(parm.getContent());
    }

    if (parm.getName().equalsIgnoreCase(
      BDMWSAddressConstants.kDeliveryInstallationDescription)) {
      wsAddressResponse.setDeliveryInstallationDescription(parm.getContent());
    }

    if (parm.getName()
      .equalsIgnoreCase(BDMWSAddressConstants.kDirectoryAreaName)) {
      wsAddressResponse.setDirectoryAreaName(parm.getContent());
    }

    if (parm.getName().equalsIgnoreCase(BDMWSAddressConstants.kCity)) {
      wsAddressResponse.setCity(parm.getContent());
    }

    if (parm.getName().equalsIgnoreCase(BDMWSAddressConstants.kProvince)) {
      wsAddressResponse.setProvince(parm.getContent());
    }

    if (parm.getName().equalsIgnoreCase(BDMWSAddressConstants.kCountry)) {
      wsAddressResponse.setCountry(parm.getContent());
    }

    if (parm.getName().equalsIgnoreCase(BDMWSAddressConstants.kPostalCode)) {
      wsAddressResponse.setPostalCode(parm.getContent());
    }
  }

}
