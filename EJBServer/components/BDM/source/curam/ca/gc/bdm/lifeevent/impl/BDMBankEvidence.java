package curam.ca.gc.bdm.lifeevent.impl;

import curam.ca.gc.bdm.entity.fact.BDMProductFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationKey;
import curam.ca.gc.bdm.entity.struct.BDMProductDtls;
import curam.ca.gc.bdm.entity.struct.BDMProductKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.ca.gc.bdm.sl.struct.BDMSubmitApplicationAddDDLinkDetails;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleBankAccountFactory;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.BankAccountRMDtls;
import curam.core.struct.BankAccountRMDtlsList;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @since ADO-21129
 *
 * The utility class for bank evidence.
 */
public class BDMBankEvidence {

  /**
   * Method to get an active bank evidence for the concern role and return the
   * bank evidence POJO with the evidence data.
   *
   * @param concernRoleID concern role identifier
   * @return return bank evidence POJO with the evidence data
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public List<BDMBankEvidenceVO> getBankEvidenceValueObjectList(
    final long concernRoleID) throws AppException, InformationalException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new BDMEvidenceUtil().getEvdDtlsByConcernroleIDandEvidenceType(
        concernRoleID, PDCConst.PDCBANKACCOUNT);

    final List<BDMBankEvidenceVO> bankEvidenceVOList =
      new ArrayList<BDMBankEvidenceVO>();

    for (final DynamicEvidenceDataDetails details : evidenceDataDetailsList) {

      final BDMBankEvidenceVO bankEvidenceVO = new BDMBankEvidenceVO();

      new BDMLifeEventUtil().setEvidenceData(bankEvidenceVO, details);

      bankEvidenceVOList.add(bankEvidenceVO);
    }

    return bankEvidenceVOList;
  }

  /**
   * Method to create or modify bank evidence with the evidence details in
   * POJO. This modifies the existing evidence if the evidence identifier is
   * present in the POJO otherwise creates a new evidence.
   *
   * @param concernRoleID concern role identifier
   * @param evidenceDetails bank evidence details
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  public ReturnEvidenceDetails createBankEvidence(final long concernRoleID,
    final BDMBankEvidenceVO evidenceDetails,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final long evidenceID = evidenceDetails.getEvidenceID();

    if (evidenceID == 0) {

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(evidenceDetails);

      final ReturnEvidenceDetails returnEvidenceDetails =
        BDMEvidenceUtil.createPDCDynamicEvidence(concernRoleID, evidenceData,
          PDCConst.PDCBANKACCOUNT, evidenceChangeReason);
      return returnEvidenceDetails;
    } else {
      // modify the bank evidence
      BDMEvidenceUtil.modifyEvidence(evidenceID, PDCConst.PDCBANKACCOUNT,
        evidenceDetails, evidenceChangeReason);
    }
    return null;
  }

  /**
   * This method will link bank account evidence in BDMPaymentDestination for
   * given concernRoleID.
   *
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   * @since 23197
   */
  public BDMPaymentDestinationKey createBDMPaymentDestinationLink(
    final long concernRoleID, final long productID)
    throws AppException, InformationalException {

    final BDMProductKey bdmProductKey = new BDMProductKey();
    bdmProductKey.productID = productID;
    final BDMProductDtls productDtls =
      BDMProductFactory.newInstance().read(bdmProductKey);

    long concernRoleBankAccountID = 0l;
    final Date startDate = Date.getCurrentDate();

    final List<BDMBankEvidenceVO> bdmBankEvidenceVOList =
      getBankEvidenceValueObjectList(concernRoleID);

    for (final BDMBankEvidenceVO bdmBankEvidenceVO : bdmBankEvidenceVOList) {
      if (bdmBankEvidenceVO.getPreferredInd()) {

        final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
          new ConcernRoleIDStatusCodeKey();
        concernRoleIDStatusCodeKey.concernRoleID = concernRoleID;
        concernRoleIDStatusCodeKey.statusCode = RECORDSTATUS.NORMAL;
        final BankAccountRMDtlsList bankAccountRMDtlsList =
          ConcernRoleBankAccountFactory.newInstance()
            .searchByConcernRoleIDAndStatus(concernRoleIDStatusCodeKey);

        for (final BankAccountRMDtls bankAccountRMDtls : bankAccountRMDtlsList.dtls) {

          if (bankAccountRMDtls.bankSortCode
            .equals(bdmBankEvidenceVO.getSortCode())
            && bankAccountRMDtls.accountNumber
              .equals(bdmBankEvidenceVO.getAccountNumber())) {
            concernRoleBankAccountID =
              bankAccountRMDtls.concernRoleBankAccountID;
            break;
          }
        }
      }
    }

    final BDMSubmitApplicationAddDDLinkDetails addDDLinkKey =
      new BDMSubmitApplicationAddDDLinkDetails();
    addDDLinkKey.concernRoleBankAccountID = concernRoleBankAccountID;
    addDDLinkKey.concernRoleID = concernRoleID;
    addDDLinkKey.startDate = startDate;
    addDDLinkKey.programType = productDtls.programType;

    final BDMMaintainPaymentDestination maintainPaymentDestinationObj =
      GuiceWrapper.getInjector()
        .getInstance(BDMMaintainPaymentDestination.class);

    final BDMPaymentDestinationKey paymentDestinationKey =
      maintainPaymentDestinationObj
        .addDestinationOnSubmitApplication(addDDLinkKey);

    return paymentDestinationKey;
  }
}
