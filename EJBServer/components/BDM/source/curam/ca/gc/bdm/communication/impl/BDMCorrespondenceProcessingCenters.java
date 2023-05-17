package curam.ca.gc.bdm.communication.impl;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.facade.communication.struct.BDMProcessingLocation;
import curam.ca.gc.bdm.sl.communication.struct.BDMProcessingCenterPhoneNumbers;
import curam.core.struct.LocaleStruct;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author ka.chungchan
 *
 */
@ImplementedBy(BDMCorrespondenceProcessingCentersImpl.class)
public interface BDMCorrespondenceProcessingCenters {

  BDMProcessingLocation getProcessingCenterByPostalCode(String postal)
    throws AppException, InformationalException;

  BDMProcessingCenterPhoneNumbers readProcessingCenterPhoneNumbers();

  String getProcessingLocationByAddressID(long addressID)
    throws AppException, InformationalException;

  String getProcessingCenterName();

  Map<String, String> getAddressElementMap(long addressID)
    throws AppException, InformationalException;

  String mapAddrElmToCanadaPostAddressFormat(Map<String, String> addressElmMap,
    LocaleStruct locale) throws AppException, InformationalException;

  BDMProcessingLocation getProcessingCenterByCountryCode(String countryCode)
    throws AppException, InformationalException;

  ArrayList<BDMProcessingLocation> getAll()
    throws AppException, InformationalException;

  ArrayList<BDMProcessingLocation> readAllFromPropertyList()
    throws AppException, InformationalException;

  Map<String, BDMProcessingLocation> getPostalCodeMap()
    throws AppException, InformationalException;

  Map<String, BDMProcessingLocation> getCountryMap()
    throws AppException, InformationalException;

}
