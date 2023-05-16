package curam.ca.gc.bdmoas.batch.bdmoasvac.impl;

import curam.ca.gc.bdm.entity.bdmfiletofile.fact.BDMInboundFileToFileStagingFactory;
import curam.ca.gc.bdm.entity.bdmfiletofile.fact.BDMOutboundFileToFileStagingFactory;
import curam.ca.gc.bdm.entity.bdmfiletofile.struct.BDMInboundFileToFileStagingDtls;
import curam.ca.gc.bdm.entity.bdmfiletofile.struct.BDMInboundFileToFileStagingKey;
import curam.ca.gc.bdm.entity.bdmfiletofile.struct.BDMOutboundFileToFileStagingDtls;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.ca.gc.bdmoas.sl.interfaces.vac.impl.pojos.BDMOASVACInbound;
import curam.ca.gc.bdmoas.sl.interfaces.vac.impl.pojos.BDMOASVACOutbound;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.BatchStreamHelper;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.apache.commons.lang3.StringUtils;

public class BDMOASVACProcessBatchStream implements
  curam.ca.gc.bdmoas.batch.bdmoasvac.intf.BDMOASVACProcessBatchStream {

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID = BATCHPROCESSNAME.BDMOAS_VAC_BATCH;
    }
    final BDMOASVACProcessBatchStreamWrapper streamWrapper =
      new BDMOASVACProcessBatchStreamWrapper(this);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final BatchProcessingSkippedRecord skippedRecord = null;

    // read the VAC record from the inbound staging table
    final BDMInboundFileToFileStagingKey key =
      new BDMInboundFileToFileStagingKey();
    key.recordID = batchProcessingID.recordID;
    final BDMInboundFileToFileStagingDtls inboundDtls =
      BDMInboundFileToFileStagingFactory.newInstance().read(key);

    // convert the inbound JSON to POJO
    final BDMOASVACInbound vacInbound = BDMRestUtil
      .convertFromJSON(inboundDtls.recordData, BDMOASVACInbound.class);

    // Perform the SIN validation

    // process the VAC record
    final BDMOASVACOutbound vacOutbound = processVACRecord(vacInbound);

    // convert the outbound POJO to JSON
    final String outboundJSON = BDMRestUtil.convertToJSON(vacOutbound);

    // load the outbound JSON into the outbound staging table
    final BDMOutboundFileToFileStagingDtls outboundDtls =
      new BDMOutboundFileToFileStagingDtls();
    outboundDtls.assign(inboundDtls);
    outboundDtls.recordData = outboundJSON;
    BDMOutboundFileToFileStagingFactory.newInstance().insert(outboundDtls);

    return skippedRecord;
  }

  private BDMOASVACOutbound
    processVACRecord(final BDMOASVACInbound vacInbound)
      throws AppException, InformationalException {

    final BDMOASVACOutbound vacOutbound = new BDMOASVACOutbound();

    // set the outbound values which are same as inbound
    vacOutbound.setOASRegionCode(vacInbound.getOASRegionCode());
    vacOutbound.setOASAccountNumber(vacInbound.getOASAccountNumber());
    vacOutbound.setWVANumber(vacInbound.getWVANumber());
    vacOutbound.setStatCode(vacInbound.getStatCode());
    vacOutbound.setClientCode(vacInbound.getClientCode());
    vacOutbound.setTypeCode(vacInbound.getTypeCode());
    vacOutbound.setWVARegionCode(vacInbound.getWVARegionCode());
    vacOutbound.setMaritalStatusCode(vacInbound.getMaritalStatusCode());
    vacOutbound.setExclusionCode(vacInbound.getExclusionCode());
    vacOutbound.setFileNumber(vacInbound.getFileNumber());
    vacOutbound.setSurName(vacInbound.getSurName());
    vacOutbound.setGivenName(vacInbound.getGivenName());
    vacOutbound.setWVASin(vacInbound.getWVASin());
    vacOutbound.setWVADob(vacInbound.getWVADob());
    vacOutbound.setSpouseWVANumber(vacInbound.getSpouseWVANumber());
    vacOutbound.setSpouseName(vacInbound.getSpouseName());
    vacOutbound.setSpouseSin(vacInbound.getSpouseSin());
    vacOutbound.setSpouseDob(vacInbound.getSpouseDob());
    vacOutbound.setControlAccNumber(vacInbound.getWVASin());
    vacOutbound.setSpouseAccNumber(vacInbound.getSpouseSin());
    vacOutbound.setHWCDob(vacInbound.getWVADob());
    vacOutbound.setPaymentMaritalStatus(vacInbound.getMaritalStatusCode());

    // set the other values -- for now its hard coded
    vacOutbound.setClientSurNameID("SMITH");
    vacOutbound.setNamePartOne("James Smith");
    vacOutbound.setAccountType("12");
    vacOutbound.setAccountStatus("1");
    vacOutbound.setEffectiveStatusDate(Date.getCurrentDate());
    vacOutbound.setSpaEntitlementDate(Date.getCurrentDate());
    vacOutbound.setOASEntitlementDate(Date.getCurrentDate());
    vacOutbound.setTotalEligibleYears("10");
    vacOutbound.setGrossEntitlement("2000");
    vacOutbound.setGISEntitlement("3000");
    vacOutbound.setNonJointCode("1");
    vacOutbound.setNamePartTwo("");
    vacOutbound.setOverpaymentCode("13");
    vacOutbound.setOPRecoveryRate("20");
    vacOutbound.setGISEntitlementDate(Date.getCurrentDate());
    vacOutbound.setOptionCode("P1");
    vacOutbound.setDeemedIncome("100");
    vacOutbound.setIncomeInd("4");
    vacOutbound.setTotalIncome("500");
    vacOutbound.setCPPIncome("200");
    vacOutbound.setPensionIncome("100");
    vacOutbound.setUIBIncome("0");
    vacOutbound.setInterestIncome("0");
    vacOutbound.setDividendIncome("0");
    vacOutbound.setRentalIncome("50");
    vacOutbound.setEmpIncome("500");
    vacOutbound.setSelfEmpIncome("0");
    vacOutbound.setOtherIncome("0");
    vacOutbound.setSpouseGISEntitlementDate(Date.getCurrentDate());
    vacOutbound.setSpouseOptionCode("C1");
    vacOutbound.setSpouseDeemedIncome("100");
    vacOutbound.setSpouseIncomeInd("2");
    vacOutbound.setSpouseTotalIncome("1000");
    vacOutbound.setSpouseCPPIncome("200");
    vacOutbound.setSpousePensionIncome("0");
    vacOutbound.setSpouseUIBIncome("200");
    vacOutbound.setSpouseInterestIncome("0");
    vacOutbound.setSpouseDividendIncome("30");
    vacOutbound.setSpouseRentalIncome("600");
    vacOutbound.setSpouseEmpIncome("0");
    vacOutbound.setSpouseSelfEmpIncome("0");
    vacOutbound.setSpouseOtherIncome("0");

    return vacOutbound;

  }

}
