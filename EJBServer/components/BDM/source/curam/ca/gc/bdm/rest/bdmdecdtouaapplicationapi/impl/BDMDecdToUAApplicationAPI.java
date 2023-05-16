package curam.ca.gc.bdm.rest.bdmdecdtouaapplicationapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.fact.BDMUsernameGuidLinkFactory;
import curam.ca.gc.bdm.entity.intf.BDMUsernameGuidLink;
import curam.ca.gc.bdm.entity.struct.BDMGuidKey;
import curam.ca.gc.bdm.entity.struct.BDMUserNameGuidKey;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDetails;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmdecdtouaapplicationapi.struct.BDMDecdToUAApplicationRequestDtls;
import curam.ca.gc.bdm.rest.bdmdecdtouaapplicationapi.struct.BDMDecdToUAApplicationResponseDtls;
import curam.citizenworkspace.security.impl.UserSession;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.NotFoundIndicator;

public class BDMDecdToUAApplicationAPI extends
  curam.ca.gc.bdm.rest.bdmdecdtouaapplicationapi.base.BDMDecdToUAApplicationAPI {

  @Inject
  UserSession userSession;

  @Override
  public BDMDecdToUAApplicationResponseDtls redirectToUAApplication(
    final BDMDecdToUAApplicationRequestDtls requestDtls)
    throws AppException, InformationalException {

    final String user = TransactionInfo.getProgramUser();

    // Set the following 4 fields into Storage / DB for use when pre-populating
    // an application
    final String country_of_residence = requestDtls.country_of_residence;
    final String current_marital_status = requestDtls.current_marital_status;
    final String decd_guid = requestDtls.decd_guid;
    final String net_income = requestDtls.net_income;

    final BDMDecdToUAApplicationResponseDtls responseDtls =
      new BDMDecdToUAApplicationResponseDtls();

    final BDMUsernameGuidLink bdmUserNameGuidLinkObj =
      BDMUsernameGuidLinkFactory.newInstance();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMUsernameGuidLinkKey bdmUsernameGuidLinkKey =
      new BDMUsernameGuidLinkKey();
    BDMUsernameGuidLinkDtls bdmUsernameGuidLinkDtls =
      new BDMUsernameGuidLinkDtls();

    final BDMGuidKey bdmGuidKey = new BDMGuidKey();
    bdmGuidKey.guid = decd_guid;
    BDMUsernameGuidLinkDetails bdmUsernameGuidLinkDetials =
      bdmUserNameGuidLinkObj.readUserNameByGuid(nfIndicator, bdmGuidKey);

    // if the returned guid does not match to the current user, then stop
    // processing
    if (!nfIndicator.isNotFound()
      && !bdmUsernameGuidLinkDetials.username.equals(user)) {
      return responseDtls;
    }
    responseDtls.dtls.add(requestDtls);

    // if no record found when searching by guid, also try search by username
    if (nfIndicator.isNotFound()) {
      final BDMUserNameGuidKey bdmUserNameGuidKey = new BDMUserNameGuidKey();
      bdmUserNameGuidKey.username = user;
      bdmUsernameGuidLinkDetials = bdmUserNameGuidLinkObj
        .readGuidByUserName(nfIndicator, bdmUserNameGuidKey);
    }

    // Make formated json to save GUID information
    String guidSavedInforamtionJSON = new String();
    guidSavedInforamtionJSON = BDMConstants.kleftBrace
      + BDMConstants.kcountry_of_residence_quote + BDMConstants.kquote
      + country_of_residence + BDMConstants.kquote + BDMConstants.kcomma
      + BDMConstants.kcurrent_marital_status_quote + BDMConstants.kquote
      + current_marital_status + BDMConstants.kquote + BDMConstants.kcomma
      + BDMConstants.knet_income_quote + BDMConstants.kquote + net_income
      + BDMConstants.kquote + BDMConstants.krightBrace;

    if (nfIndicator.isNotFound()) {
      // If the User does not have an Guid Link entry create one
      bdmUsernameGuidLinkDtls.guid = decd_guid;
      bdmUsernameGuidLinkDtls.username = user;
      bdmUsernameGuidLinkDtls.guidSavedInformation = guidSavedInforamtionJSON;
      bdmUsernameGuidLinkDtls.usernameGuidLinkID =
        curam.util.type.UniqueID.nextUniqueID();

      bdmUserNameGuidLinkObj.insert(bdmUsernameGuidLinkDtls);
    } else {
      // If the User has a Guid Link then update the saved information
      bdmUsernameGuidLinkKey.usernameGuidLinkID =
        bdmUsernameGuidLinkDetials.usernameGuidLinkID;
      bdmUsernameGuidLinkDtls =
        bdmUserNameGuidLinkObj.read(bdmUsernameGuidLinkKey);
      bdmUsernameGuidLinkDtls.guidSavedInformation = guidSavedInforamtionJSON;
      bdmUsernameGuidLinkDtls.guid = decd_guid;

      bdmUserNameGuidLinkObj.modify(bdmUsernameGuidLinkKey,
        bdmUsernameGuidLinkDtls);
    }

    return responseDtls;
  }

}
