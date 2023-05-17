/**
 *
 */
package curam.ca.gc.bdm.facade.thirdparty.impl;

import curam.ca.gc.bdm.sl.fact.BDMThirdPartyParticipantSearchProcessFactory;
import curam.ca.gc.bdm.sl.struct.BDMThirdPartyParticipantSearchKey;
import curam.core.facade.struct.AllParticipantSearchDetails;
import curam.core.struct.InformationalMsgDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;

/**
 * Participant search for Third Party Contact wizard.
 *
 * @author donghua.jin
 *
 */
public class BDMThirdPartyParticipantSearch extends
  curam.ca.gc.bdm.facade.thirdparty.base.BDMThirdPartyParticipantSearch {

  /**
   * Search participants.
   *
   * @param key
   */
  @Override
  public AllParticipantSearchDetails
    searchParticipant(final BDMThirdPartyParticipantSearchKey key)
      throws AppException, InformationalException {

    final AllParticipantSearchDetails allParticipantSearchDetails =
      new AllParticipantSearchDetails();

    allParticipantSearchDetails.dtls =
      BDMThirdPartyParticipantSearchProcessFactory.newInstance()
        .searchParticipant(key);

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final String[] infos = informationalManager.obtainInformationalAsString();

    for (final String message : infos) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = message;

      allParticipantSearchDetails.informationalMsgDtls.dtls
        .addRef(informationalMsgDtls);
    }

    return allParticipantSearchDetails;
  }

}
