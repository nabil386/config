package curam.ca.gc.bdm.test.evidence;

import com.google.inject.Inject;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.pdc.impl.PDCBankAccountReplicatorExtender;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.ParticipantBankAccountDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BDMPDCBankAccountReplicatorExtenderImplTest
  extends CuramServerTestJUnit4 {

  @Inject
  Set<PDCBankAccountReplicatorExtender> pdcBankAccountReplicatorExtenders;

  PDCBankAccountReplicatorExtender pdcBankAccountReplicatorExtender;

  public BDMPDCBankAccountReplicatorExtenderImplTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Before
  public void setUp() {

    assertNotNull(pdcBankAccountReplicatorExtenders);
    assertEquals(1, pdcBankAccountReplicatorExtenders.size());
    pdcBankAccountReplicatorExtender =
      pdcBankAccountReplicatorExtenders.iterator().next();
    assertNotNull(pdcBankAccountReplicatorExtender);
  }

  /**
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Test
  public void
    testAssignDynamicEvidenceToExtendedDetails_latestETV_bankSortCode_null()
      throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCBANKACCOUNT,
        curam.util.type.Date.fromISO8601("20010101"));
    final String expectedSortCodeValue = "11122222";
    dynamicEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER)
      .setValue("111");
    dynamicEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER).setValue("22222");
    final ParticipantBankAccountDetails participantBankAccountDetails =
      new ParticipantBankAccountDetails();
    participantBankAccountDetails
      .assign(BDMPDCBankAccountEvidencePopulatorImplTest
        .createBankAccountDetails(null));

    pdcBankAccountReplicatorExtender.assignDynamicEvidenceToExtendedDetails(
      dynamicEvidenceDataDetails, participantBankAccountDetails);

    assertEquals(expectedSortCodeValue,
      participantBankAccountDetails.bankSortCode);
  }

  /**
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testAssignDynamicEvidenceToExtendedDetails_latestETV_bankSortCode_allReadySet()
      throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCBANKACCOUNT,
        curam.util.type.Date.fromISO8601("20010101"));
    dynamicEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER)
      .setValue("111");
    dynamicEvidenceDataDetails
      .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER)
      .setValue("22222");
    final ParticipantBankAccountDetails participantBankAccountDetails =
      new ParticipantBankAccountDetails();
    final String bankSortCode = "77777777";
    participantBankAccountDetails
      .assign(BDMPDCBankAccountEvidencePopulatorImplTest
        .createBankAccountDetails(bankSortCode));

    pdcBankAccountReplicatorExtender.assignDynamicEvidenceToExtendedDetails(
      dynamicEvidenceDataDetails, participantBankAccountDetails);

    assertEquals(bankSortCode, participantBankAccountDetails.bankSortCode);
  }

  /**
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Test
  public void
    testAssignDynamicEvidenceToExtendedDetails_olderETV_bankSortCode_alreadySet()
      throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCBANKACCOUNT,
        curam.util.type.Date.fromISO8601("20000102"));

    final ParticipantBankAccountDetails participantBankAccountDetails =
      new ParticipantBankAccountDetails();
    final String bankSortCode = "77777777";
    participantBankAccountDetails
      .assign(BDMPDCBankAccountEvidencePopulatorImplTest
        .createBankAccountDetails(bankSortCode));

    pdcBankAccountReplicatorExtender.assignDynamicEvidenceToExtendedDetails(
      dynamicEvidenceDataDetails, participantBankAccountDetails);

    assertEquals(bankSortCode, participantBankAccountDetails.bankSortCode);
  }

  /**
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  @Test
  public void
    testAssignDynamicEvidenceToExtendedDetails_olderETV_bankSortCode_Null()
      throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCBANKACCOUNT,
        curam.util.type.Date.fromISO8601("20000102"));

    final ParticipantBankAccountDetails participantBankAccountDetails =
      new ParticipantBankAccountDetails();
    participantBankAccountDetails
      .assign(BDMPDCBankAccountEvidencePopulatorImplTest
        .createBankAccountDetails(null));

    pdcBankAccountReplicatorExtender.assignDynamicEvidenceToExtendedDetails(
      dynamicEvidenceDataDetails, participantBankAccountDetails);

    assertEquals(null, participantBankAccountDetails.bankSortCode);
  }

}
