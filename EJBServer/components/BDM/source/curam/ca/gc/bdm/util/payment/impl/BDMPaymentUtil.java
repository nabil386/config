package curam.ca.gc.bdm.util.payment.impl;

import curam.ca.gc.bdm.entity.fact.BDMRegionSequenceFactory;
import curam.ca.gc.bdm.entity.intf.BDMRegionSequence;
import curam.ca.gc.bdm.entity.struct.BDMRegionSequenceDtls;
import curam.ca.gc.bdm.entity.struct.BDMRegionSequenceDtlsStruct1;
import curam.ca.gc.bdm.entity.struct.BDMRegionSequenceKey;
import curam.ca.gc.bdm.entity.struct.BDMSeqType;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import java.util.UUID;

public class BDMPaymentUtil {

  /**
   * Util class to get Sequence Number, Requisition Number and GUID Number
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  /* Logic to generate Requisition number to track payment flow. */

  public String getRequisitionNumber(final String esdcCode,
    final String cdoCode, final String inputDate, final String regionCode,
    final int seqNum) {

    final String sequenceNum = createSeqNumPrefix(seqNum);

    final String outDate = BDMDateUtil.dateFormatToYYMM(inputDate);

    final String reqNum =
      esdcCode + cdoCode + outDate + regionCode + sequenceNum;

    return reqNum;

  }

  /* Start, Bug:55268, Add logic to 0 prefix for 0 to 9 number */
  private String createSeqNumPrefix(final int seqNum) {

    String with2digits = "";
    if (seqNum >= 0 && seqNum <= 9) {
      with2digits = String.format("%02d", seqNum);
    } else {
      with2digits = String.valueOf(seqNum);
    }

    return with2digits;

  }
  /* End, Bug:55268, Add logic to 0 prefix for 0 to 9 number */

  /* Logic to generate sequence number to track the transaction. */

  public int getSeqNumber(final String seqType)
    throws AppException, InformationalException {

    final BDMSeqType seqKey = new BDMSeqType();
    BDMRegionSequenceDtlsStruct1 sequenceDtls =
      new BDMRegionSequenceDtlsStruct1();

    BDMRegionSequenceDtls seqDtls = new BDMRegionSequenceDtls();
    final BDMRegionSequenceKey sequenceKey = new BDMRegionSequenceKey();

    seqKey.sequenceNumType = seqType;

    final BDMRegionSequence seqObj = BDMRegionSequenceFactory.newInstance();

    sequenceDtls = seqObj.readBySeqType(seqKey);
    sequenceKey.recordID = sequenceDtls.recordID;
    seqDtls = seqObj.read(sequenceKey);

    int seqNum = sequenceDtls.currentValue;

    if (seqNum == sequenceDtls.maxValue) {
      seqNum = sequenceDtls.startValue;
    } else {
      seqNum++;
    }

    seqDtls.currentValue = seqNum;
    seqDtls.createdBy = TransactionInfo.getProgramUser();
    seqDtls.createdOn = curam.util.type.Date.getCurrentDate().getDateTime();
    seqDtls.lastUpdatedBy = TransactionInfo.getProgramUser();
    seqObj.modify(sequenceKey, seqDtls);
    return seqNum;
  }

  /* Generating the GUID number for payment interface */

  public String getGuidNumber() {

    return UUID.randomUUID().toString();

  }
}
