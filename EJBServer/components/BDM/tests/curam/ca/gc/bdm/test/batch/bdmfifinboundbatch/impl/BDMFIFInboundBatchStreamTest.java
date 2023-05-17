package curam.ca.gc.bdm.test.batch.bdmfifinboundbatch.impl;

import curam.ca.gc.bdm.batch.bdmfifinboundbatch.fact.BDMFIFInboundBatchStreamFactory;
import curam.ca.gc.bdm.batch.bdmfifinboundbatch.intf.BDMFIFInboundBatchStream;
import curam.ca.gc.bdm.entity.fact.BDMBankBranchFactory;
import curam.ca.gc.bdm.entity.fact.BDMFIFStageInboundFactory;
import curam.ca.gc.bdm.entity.intf.BDMBankBranch;
import curam.ca.gc.bdm.entity.intf.BDMFIFStageInbound;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchDtls;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchSearchBySortCodeKey;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchSearchResultBySortCode;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchSearchResultBySortCodeList;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundDtls;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.BANKBRANCHSTATUS;
import curam.codetable.COUNTRY;
import curam.codetable.LOCALE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressFactory;
import curam.core.fact.BankBranchFactory;
import curam.core.fact.BankFactory;
import curam.core.fact.EmailAddressFactory;
import curam.core.fact.PhoneNumberFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.Bank;
import curam.core.intf.BankBranch;
import curam.core.intf.EmailAddress;
import curam.core.intf.PhoneNumber;
import curam.core.struct.AddressDtls;
import curam.core.struct.BankBranchDtls;
import curam.core.struct.BankBranchDtlsList;
import curam.core.struct.BankBranchKey;
import curam.core.struct.BankDtls;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.EmailAddressDtls;
import curam.core.struct.PhoneNumberDtls;
import curam.core.struct.SwiftBusinessIdentifierCode;
import curam.core.struct.UniqueIDKeySet;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BDMFIFInboundBatchStreamTest extends CuramServerTestJUnit4 {

  private static BDMFIFInboundBatchStream batchStream;

  private final String kBDMFIFinboundKeySetName = "BDMFIFIN";

  private UniqueIDKeySet uniqueIDKeySet;

  @Override
  protected boolean shouldCommit() {

    return !true;
  }

  @Override
  protected void setUpCuramServerTest() {

    batchStream = BDMFIFInboundBatchStreamFactory.newInstance();

    uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = kBDMFIFinboundKeySetName;

    super.setUpCuramServerTest();

  }

  public long
    insertBDMFIFStageInboundRecord(final BDMFIFStageInboundDtls inboundDtls)
      throws AppException, InformationalException {

    final BDMFIFStageInbound inboundObj =
      BDMFIFStageInboundFactory.newInstance();

    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();

    inboundDtls.recordID = uniqueID.getNextID();
    inboundObj.insert(inboundDtls);

    return inboundDtls.recordID;
  }

  public long insertBankRecord(final BankDtls bankDtls)
    throws AppException, InformationalException {

    final Bank bank = BankFactory.newInstance();

    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();

    bankDtls.bankID = uniqueIDObj.getNextID();

    bank.insert(bankDtls);

    return bankDtls.bankID;
  }

  public long insertPhoneNumberRecord(final PhoneNumberDtls phoneNumberDtls)
    throws AppException, InformationalException {

    final PhoneNumber phoneNumber = PhoneNumberFactory.newInstance();

    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();

    phoneNumberDtls.phoneNumberID = uniqueID.getNextID();

    phoneNumber.insert(phoneNumberDtls);

    return phoneNumberDtls.phoneNumberID;

  }

  public long
    insertEmailAddressRecord(final EmailAddressDtls emailAddressDtls)
      throws AppException, InformationalException {

    final EmailAddress emailAddress = EmailAddressFactory.newInstance();

    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();

    emailAddressDtls.emailAddressID = uniqueID.getNextID();

    emailAddress.insert(emailAddressDtls);

    return emailAddressDtls.emailAddressID;

  }

  public long insertBankBranchRecord(final BankBranchDtls bankBranchDtls)
    throws AppException, InformationalException {

    final BankBranch bankBranch = BankBranchFactory.newInstance();

    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();

    final PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();
    phoneNumberDtls.phoneCountryCode = "+1";
    phoneNumberDtls.phoneAreaCode = "905";
    phoneNumberDtls.phoneNumber = "1111111";
    phoneNumberDtls.statusCode = curam.codetable.RECORDSTATUS.CANCELLED;
    phoneNumberDtls.versionNo = 1;
    bankBranchDtls.phoneNumberID = insertPhoneNumberRecord(phoneNumberDtls);

    final PhoneNumberDtls faxNumberDtls = new PhoneNumberDtls();
    faxNumberDtls.phoneCountryCode = "+1";
    faxNumberDtls.phoneAreaCode = "905";
    faxNumberDtls.phoneNumber = "1111111";
    faxNumberDtls.statusCode = curam.codetable.RECORDSTATUS.CANCELLED;
    faxNumberDtls.versionNo = 1;
    bankBranchDtls.faxNumberID = insertPhoneNumberRecord(faxNumberDtls);

    final EmailAddressDtls emailAddressDtls = new EmailAddressDtls();
    emailAddressDtls.emailAddress = "test@email.com";
    emailAddressDtls.statusCode = curam.codetable.RECORDSTATUS.CANCELLED;
    emailAddressDtls.versionNo = 1;
    bankBranchDtls.emailAddressID =
      insertEmailAddressRecord(emailAddressDtls);

    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressDtls.countryCode = COUNTRY.CA;
    addressDtls.versionNo = 1;
    bankBranchDtls.addressID = insertAddressRecord(addressDtls);

    bankBranchDtls.bankBranchID = uniqueID.getNextID();

    bankBranch.insert(bankBranchDtls);

    return bankBranchDtls.bankBranchID;
  }

  public long
    insertBDMBankBranchRecord(final BDMBankBranchDtls bdmBankBranchDtls)
      throws AppException, InformationalException {

    final BDMBankBranch bdmBankBranch = BDMBankBranchFactory.newInstance();

    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();

    bdmBankBranchDtls.recordID = uniqueID.getNextID();

    bdmBankBranch.insert(bdmBankBranchDtls);

    return bdmBankBranchDtls.recordID;
  }

  public long insertAddressRecord(final AddressDtls addressDtls)
    throws AppException, InformationalException {

    final Address address = AddressFactory.newInstance();

    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = kBDMFIFinboundKeySetName;

    addressDtls.addressID = uniqueID.getNextIDFromKeySet(uniqueIDKeySet);

    addressDtls.addressData = "1\n" + addressDtls.addressID + "\n"
      + "BDMINTL\n" + "CA\n" + "1\n" + "1\n" + "POBOXNO=\n" + "APT=\n"
      + "ADD1=111 no name\n" + "ADD2=\n" + "CITY=Halifax\n" + "PROV=NS\n"
      + "STATEPROV=\n" + "BDMSTPROVX=NS\n" + "COUNTRY=CA\n"
      + "POSTCODE=L7G 3S8\n" + "ZIP=\n" + "BDMZIPX=\n" + "BDMUNPARSE=\n";

    address.insert(addressDtls);

    return addressDtls.addressID;

  }

  public BDMBankBranchSearchResultBySortCodeList getBDMBankBranchBySortCode(
    final String bic) throws AppException, InformationalException {

    final BDMBankBranch bdmBankBranch = BDMBankBranchFactory.newInstance();

    final BDMBankBranchSearchBySortCodeKey bdmBankBranchKey =
      new BDMBankBranchSearchBySortCodeKey();
    bdmBankBranchKey.bic = bic;
    return bdmBankBranch.searchBySortCode(bdmBankBranchKey);

  }

  private long getBankBranchIDFromBIC(final String bic)
    throws AppException, InformationalException {

    final BankBranch bpo = BankBranchFactory.newInstance();
    final SwiftBusinessIdentifierCode bicHolder =
      new SwiftBusinessIdentifierCode();
    bicHolder.bic = bic;
    final BankBranchDtlsList branches = bpo.searchByBic(bicHolder);

    if (branches.dtls.size() == 0) {
      return 0L;
    }

    for (final BankBranchDtls currentBranch : branches.dtls) {
      if (currentBranch.statusCode.equals(curam.codetable.RECORDSTATUS.NORMAL)
        && currentBranch.bankBranchStatus
          .equals(BANKBRANCHSTATUS.DEFAULTCODE)) {
        return currentBranch.bankBranchID;
      }
    }

    for (final BankBranchDtls currentBranch : branches.dtls) {
      if (currentBranch.statusCode
        .equals(curam.codetable.RECORDSTATUS.CANCELLED)
        || currentBranch.bankBranchStatus.equals(BANKBRANCHSTATUS.CLOSED)) {
        return currentBranch.bankBranchID;
      }
    }

    return 0L; // error condition: statusCode or bankBranchStatus invalid
  }

  @Test
  public void testProcessRecord_nullInput()
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info("testProcessRecordNullInput");

    final BatchProcessingID batchProcessingID = null;
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    assertEquals("Error, null batchProcessingID '" + null + "'.",
      skippedRecord.errorMessage);
  }

  @Test
  public void testProcessRecord_alternateFlow()
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info("testProcessRecord_AlternateFlow");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "00000001";
    inboundDtls.branchStatus = "Y";
    inboundDtls.branchNameEN = "Branch";
    inboundDtls.branchCivicAddress = "50";
    inboundDtls.branchPOAddress = "Joseph Howe Dr.";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "B1M 1A1";
    inboundDtls.bankBranchNo = "001";
    inboundDtls.bankSortCode = "120001";
    inboundDtls.branchForeignCountry = "";

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    assertNotNull(skippedRecord);
  }

  @Test
  public void testProcessRecord_invalidBankranchRecord()
    throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_openFIFRecordThatDoesNotExistAndIsOpen_xRefNumsProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = inboundDtls.branchNameEN;
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = "X";
    bankBranchDtls.bankBranchStatus = "X";
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.name = "Old BranchEN";
    bankBranchDtls.startDate = Date.getCurrentDate();
    insertBankBranchRecord(bankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    assertNotNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_openFIFRecordThatExistsAndIsOpenWithNoChanges_xRefNumsProvided()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_openFIFRecordThatExistsAndIsOpenWithNoChanges_xRefNumsProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = inboundDtls.branchNameEN;
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.upperBankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.upperBic = inboundDtls.bankSortCode;
    bankBranchDtls.name = inboundDtls.branchNameEN + "(02210)";
    bankBranchDtls.upperName = inboundDtls.branchNameEN + "(02210)";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);
    bdmBankBranchDtls.name = inboundDtls.branchNameFR;
    bdmBankBranchDtls.localeCode = LOCALE.FRENCH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(1L, bdmBankBranch.xRefCheckDigit);
      assertEquals(1L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(1L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_openFIFRecordThatExistsAndIsOpenWithNoChanges_xRefNumsNotProvided()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_openFIFRecordThatExistsAndIsOpenWithNoChanges_xRefNumsNotProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = inboundDtls.branchNameEN;
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressDtls.countryCode = COUNTRY.CA;
    addressDtls.addressData = "1\n" + addressDtls.addressID + "\n"
      + "BDMINTL\n" + "CA\n" + "1\n" + "1\n" + "POBOXNO=\n" + "APT=\n"
      + "ADD1=111 no name\n" + "ADD2=\n" + "CITY=Halifax\n" + "PROV=NS\n"
      + "STATEPROV=\n" + "BDMSTPROVX=NS\n" + "COUNTRY=CA\n"
      + "POSTCODE=L7G 3S8\n" + "ZIP=\n" + "BDMZIPX=\n" + "BDMUNPARSE=\n";
    addressDtls.versionNo = 1;
    final long addressID = insertAddressRecord(addressDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.upperBankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.upperBic = inboundDtls.bankSortCode;
    bankBranchDtls.name = inboundDtls.branchNameEN + "(02210)";
    bankBranchDtls.upperName = inboundDtls.branchNameEN + "(02210)";
    bankBranchDtls.startDate = Date.getCurrentDate();
    bankBranchDtls.addressID = addressID;
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);
    bdmBankBranchDtls.name = inboundDtls.branchNameFR;
    bdmBankBranchDtls.localeCode = LOCALE.FRENCH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(0L, bdmBankBranch.xRefCheckDigit);
      assertEquals(0L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(0L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_openFIFRecordThatExistsAndIsOpenWithChanges_xRefNumsProvided()
      throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_openFIFRecordThatExistsAndIsOpenWithChanges_xRefNumsProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchPOAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = "Old BranchEN";
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
    bankBranchDtls.bankSortCode = "old";
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.name = "Old BranchEN";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(1L, bdmBankBranch.xRefCheckDigit);
      assertEquals(1L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(1L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_openFIFRecordThatExistsAndIsOpenWithChanges_xRefNumsNotProvided()
      throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_openFIFRecordThatExistsAndIsOpenWithChanges_xRefNumsNotProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchPOAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = "Old BranchEN";
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
    bankBranchDtls.bankSortCode = "old";
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.name = "Old BranchEN";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(0L, bdmBankBranch.xRefCheckDigit);
      assertEquals(0L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(0L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

  @Test
  public void testProcessRecord_openFIFRecordThatExistsAndIsClosed()
    throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger
      .info("testProcessRecord_openFIFRecordThatExistsAndIsClosed");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = "Old BranchEN";
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.CANCELLED;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.CLOSED;
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.name = "Old BranchEN";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(1L, bdmBankBranch.xRefCheckDigit);
      assertEquals(1L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(1L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_openFIFRecordThatDoesNotExistAndIsOpen_xRefNumsProvided()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_openFIFRecordThatDoesNotExistAndIsOpen_xRefNumsProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = inboundDtls.branchNameEN;
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(1L, bdmBankBranch.xRefCheckDigit);
      assertEquals(1L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(1L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_openFIFRecordThatDoesNotExistAndIsOpen_xRefNumsNotProvided()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_openFIFRecordThatDoesNotExistAndIsOpen_xRefNumsNotProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "V";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = inboundDtls.branchNameEN;
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(0L, bdmBankBranch.xRefCheckDigit);
      assertEquals(0L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(0L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);
  }

  @Test
  public void
    testProcessRecord_closedFIFRecordThatExistsAndIsOpen_fifStatusC()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_ClosedFIFRecordThatExistsAndIsOpen_fifStatusC");

    Configuration.setProperty(EnvVars.BDM_ENV_BDM_BANKBRANCH_VALIDATION,
      EnvVars.ENV_VALUE_NO);

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.branchStatus = "C";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.financialInstNo =
      StringUtils.substring(inboundDtls.bankSortCode, 0, 3);
    inboundDtls.bankBranchNo =
      StringUtils.substring(inboundDtls.bankSortCode, 3, 8);
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    insertBDMFIFStageInboundRecord(inboundDtls);

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = inboundDtls.branchNameEN;
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.upperBankSortCode =
      inboundDtls.bankSortCode.toUpperCase(new Locale(LOCALE.DEFAULTCODE));
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.upperBic =
      inboundDtls.bankSortCode.toUpperCase(new Locale(LOCALE.DEFAULTCODE));
    bankBranchDtls.name = inboundDtls.branchNameEN + "(02210)";
    bankBranchDtls.upperName =
      inboundDtls.branchNameEN.toUpperCase(new Locale(LOCALE.DEFAULTCODE))
        + "(02210)";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    // final TransactionInfo ti = TransactionInfo.getInfo();
    // ti.commit();

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BankBranch bankBranch = BankBranchFactory.newInstance();
    final BankBranchKey bankBranchKey = new BankBranchKey();
    bankBranchKey.bankBranchID =
      getBankBranchIDFromBIC(inboundDtls.bankSortCode);
    final BankBranchDtls actualBankBranchDtls =
      bankBranch.read(bankBranchKey);

    assertEquals(RECORDSTATUS.CANCELLED, actualBankBranchDtls.statusCode);
    assertEquals(BANKBRANCHSTATUS.CLOSED,
      actualBankBranchDtls.bankBranchStatus);

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_closedFIFRecordThatExistsAndIsOpen_fifStatusX()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_ClosedFIFRecordThatExistsAndIsOpen_fifStatusX");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "X";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = inboundDtls.branchNameEN;
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.name = "Old BranchEN";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    bdmBankBranchDtls.name = inboundDtls.branchNameFR;
    bdmBankBranchDtls.localeCode = LOCALE.FRENCH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BankBranch bankBranch = BankBranchFactory.newInstance();
    final BankBranchKey bankBranchKey = new BankBranchKey();
    bankBranchKey.bankBranchID =
      getBankBranchIDFromBIC(inboundDtls.bankSortCode);
    final BankBranchDtls actualBankBranchDtls =
      bankBranch.read(bankBranchKey);

    assertEquals(RECORDSTATUS.CANCELLED, actualBankBranchDtls.statusCode);
    assertEquals(BANKBRANCHSTATUS.CLOSED,
      actualBankBranchDtls.bankBranchStatus);

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_closedFIFRecordThatExistsAndIsClosed_xRefNumsProvided()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_ClosedFIFRecordThatExistsAndIsClosed_xRefNumsProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "C";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";
    inboundDtls.xRefCheckDigit = 1L;
    inboundDtls.xRefFinancialInstNo = 1L;
    inboundDtls.xRefFINInstBranchNo = 1L;

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = "Old BranchEN";
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.CANCELLED;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.CLOSED;
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.name = "Old BranchEN";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(1L, bdmBankBranch.xRefCheckDigit);
      assertEquals(1L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(1L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

  @Test
  public void
    testProcessRecord_closedFIFRecordThatExistsAndIsClosed_xRefNumsNotProvided()
      throws AppException, InformationalException, InterruptedException {

    Trace.kTopLevelLogger.info(
      "testProcessRecord_ClosedFIFRecordThatExistsAndIsClosed_xRefNumsNotProvided");

    final BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    inboundDtls.financialInstNo = "0005";
    inboundDtls.branchStatus = "C";
    inboundDtls.branchNameEN = "Updated BranchEN";
    inboundDtls.branchNameFR = "actualisé Francais Bank";
    inboundDtls.branchCivicAddress = "111 Where the streets have no name";
    inboundDtls.branchCity = "Halifax";
    inboundDtls.branchProvinceCode = "NS";
    inboundDtls.branchPOCode = "L4E 3W5";
    inboundDtls.branchForeignCountry = "Canada";
    inboundDtls.bankBranchNo = "02210";
    inboundDtls.bankSortCode = "000502210";

    final BankDtls bankDtls = new BankDtls();
    bankDtls.bankStatus = curam.codetable.BANKSTATUS.DEFAULTCODE;
    bankDtls.statusCode = RECORDSTATUS.NORMAL;
    bankDtls.name = "Old BranchEN";
    bankDtls.startDate = Date.getCurrentDate();
    bankDtls.versionNo = 1;
    final long bankID = insertBankRecord(bankDtls);

    final BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls.bankID = bankID;
    bankBranchDtls.statusCode = curam.codetable.RECORDSTATUS.CANCELLED;
    bankBranchDtls.bankBranchStatus = BANKBRANCHSTATUS.CLOSED;
    bankBranchDtls.bankSortCode = inboundDtls.bankSortCode;
    bankBranchDtls.bic = inboundDtls.bankSortCode;
    bankBranchDtls.name = "Old BranchEN";
    bankBranchDtls.startDate = Date.getCurrentDate();
    final long bankBranchID = insertBankBranchRecord(bankBranchDtls);

    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    bdmBankBranchDtls.bankID = bankID;
    bdmBankBranchDtls.bankBranchID = bankBranchID;
    bdmBankBranchDtls.bic = inboundDtls.bankSortCode;
    bdmBankBranchDtls.name = inboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    insertBDMBankBranchRecord(bdmBankBranchDtls);

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    batchProcessingID.recordID = insertBDMFIFStageInboundRecord(inboundDtls);
    final BatchProcessingSkippedRecord skippedRecord =
      batchStream.processRecord(batchProcessingID);

    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      getBDMBankBranchBySortCode(inboundDtls.bankSortCode);
    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        assertEquals(inboundDtls.branchNameEN, bdmBankBranch.name);
      } else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        assertEquals(inboundDtls.branchNameFR, bdmBankBranch.name);
      }
      assertEquals(0L, bdmBankBranch.xRefCheckDigit);
      assertEquals(0L, bdmBankBranch.xRefFinancialInstNo);
      assertEquals(0L, bdmBankBranch.xRefFINInstBranchNo);
    }

    assertNull(skippedRecord);

  }

}
