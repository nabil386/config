package curam.ca.gc.bdm.pdc.impl;

import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.core.impl.CuramConst;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCPhoneNumberReplicatorExtender;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.CodeTable;

/**
 * A replicator reflects changes in evidence onto the relevant legacy tables
 * for the purposes of backward compatibility.
 **/
public class BDMPDCPhoneNumberReplicatorExtenderImpl
  implements PDCPhoneNumberReplicatorExtender {

  /**
   * A replicator reflects changes in evidence onto the relevant legacy tables
   * for the purposes of backward compatibility.The replicator takes
   * phoneCoutryCode codetableitem description and Strips off country name and
   * remove + an - and resulting number is stored as countrycode in legacy table
   *
   * @param DynamicEvidenceDataDetails
   * @param ParticipantPhoneDetails
   **/
  @Override
  public void assignDynamicEvidenceToExtendedDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final ParticipantPhoneDetails participantPhoneDetails)
    throws AppException, InformationalException {

    final String phoneCodeValue =
      dynamicEvidenceDataDetails.getAttribute("phoneCountryCode").getValue();

    final String description =
      CodeTable.getOneItem(BDMPHONECOUNTRY.TABLENAME, phoneCodeValue);

    // remove + and - from countryCode.
    String formattedCountryCode =
      description.substring(0, description.indexOf(CuramConst.gkSpace));

    if (formattedCountryCode.indexOf(CuramConst.gkDash) > 0) {

      // BUg Fix : 65766 -Phone Number with a country code starting with 1 -
      // numeric code is not reflected in the context panel of the person
      formattedCountryCode =
        formattedCountryCode.replace(CuramConst.gkDash, CuramConst.gkEmpty);

    }

    participantPhoneDetails.phoneCountryCode = formattedCountryCode;

  }

}
