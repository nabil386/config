package curam.ca.gc.bdm.evidence.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCBankAccountReplicatorExtender;
import curam.pdc.struct.ParticipantBankAccountDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * TASK 16366 - Bank Account Evidence
 *
 * The
 * {@link #assignDynamicEvidenceToExtendedDetails(DynamicEvidenceDataDetails, ParticipantBankAccountDetails)}
 * method of this implementation of {@link PDCBankAccountReplicatorExtender}
 * will set the {@link ParticipantBankAccountDetails#bankSortCode} value to be
 * the concatenation of financial institution number and branch transit number.
 * {@link DynamicEvidenceDataDetails} providing the following conditions hold.
 *
 *
 * <ul>
 * <li>Both {@link ParticipantBankAccountDetails} and
 * {@link DynamicEvidenceDataDetails} are
 * not null.</li>
 * <li>Both the associated Evidence Type and Evidence Type Version can be
 * retrieved
 * from the {@link DynamicEvidenceDataDetails}
 * and have non-null values.</li>
 * <li>The associated Evidence Type has a type code of
 * {@link PDCConst.PDCBANKACCOUNT}</li>
 * <li>The ETV declares "financial institution number" and "branch transit
 * number" data attributes.</li>
 * <li>The "sortCode" on the {@link DynamicEvidenceDataDetails} is
 * neither null nor empty</li>
 * <li>The "bankSortCode" instance variable on the
 * {@link ParticipantBankAccountDetails} is not already set</li>
 * </ul>
 *
 */
public class BDMPDCBankAccountReplicatorExtenderImpl
  implements PDCBankAccountReplicatorExtender {

  /*
   * This method sets the {@link ParticipantBankAccountDetails#bankSortCode}
   * value to be the concatenation of financial institution number and branch
   * transit number.
   *
   * @param dynamicEvidenceDataDetails bank account evidence data details
   * @param participantBankAccountDetails participant bank account details
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Override
  public void assignDynamicEvidenceToExtendedDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final ParticipantBankAccountDetails participantBankAccountDetails)
    throws AppException, InformationalException {

    if (participantBankAccountDetails != null && BDMEvidenceUtil
      .hasBDMBankAccountCustomizations(dynamicEvidenceDataDetails)) {

      final DynamicEvidenceDataAttributeDetails fin =
        dynamicEvidenceDataDetails
          .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER);
      final DynamicEvidenceDataAttributeDetails transit =
        dynamicEvidenceDataDetails
          .getAttribute(BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER);
      String finValue = null;
      String transitValue = null;
      String bankSortCodeValue = null;

      // check that financial institution number and branch transit number are
      // not null or empty
      // and the bank sortCode is not already set
      if (fin != null && (finValue = fin.getValue()) != null
        && !finValue.trim().isEmpty() && transit != null
        && (transitValue = transit.getValue()) != null
        && !transitValue.trim().isEmpty()
        && ((bankSortCodeValue =
          participantBankAccountDetails.bankSortCode) == null
          || bankSortCodeValue.trim().isEmpty())) {
        participantBankAccountDetails.bankSortCode = finValue + transitValue;
      }
    }
  }

}
