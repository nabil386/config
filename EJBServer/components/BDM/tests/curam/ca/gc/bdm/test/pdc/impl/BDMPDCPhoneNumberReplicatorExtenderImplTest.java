package curam.ca.gc.bdm.test.pdc.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.pdc.impl.BDMPDCPhoneNumberReplicatorExtenderImpl;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.struct.PhoneNumberDtls;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMPDCPhoneNumberReplicatorExtenderImplTest
  extends CuramServerTestJUnit4 {

  @Inject
  BDMPDCPhoneNumberReplicatorExtenderImpl pdcPhoneNumberReplicator;

  public BDMPDCPhoneNumberReplicatorExtenderImplTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Test
  public void testAssignDynamicEvidenceToExtendedDetails_removePlus()
    throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCPHONENUMBER,
        curam.util.type.Date.fromISO8601("20000101"));
    dynamicEvidenceDataDetails.getAttribute("phoneCountryCode")
      .setValue(BDMPHONECOUNTRY.AFGHANISTAN);
    final ParticipantPhoneDetails participantPhoneAccountDetails =
      new ParticipantPhoneDetails();

    final PhoneNumberDtls phone = new PhoneNumberDtls();
    phone.phoneCountryCode = "";
    phone.phoneAreaCode = "555";
    phone.phoneNumber = "9999999";
    phone.phoneNumberID = 12345;

    participantPhoneAccountDetails.assign(phone);

    pdcPhoneNumberReplicator.assignDynamicEvidenceToExtendedDetails(
      dynamicEvidenceDataDetails, participantPhoneAccountDetails);
    assertEquals("93", participantPhoneAccountDetails.phoneCountryCode);
  }

  @Test
  public void testAssignDynamicEvidenceToExtendedDetails_USA()
    throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(PDCConst.PDCPHONENUMBER,
        curam.util.type.Date.fromISO8601("20000101"));

    // * BDMPC80018. = +1-246 Barbados
    dynamicEvidenceDataDetails.getAttribute("phoneCountryCode")
      .setValue(BDMPHONECOUNTRY.USA);
    // dynamicEvidenceDataDetails
    final ParticipantPhoneDetails participantPhoneAccountDetails =
      new ParticipantPhoneDetails();

    final PhoneNumberDtls phone = new PhoneNumberDtls();
    phone.phoneCountryCode = "";
    phone.phoneNumberID = 12345;

    participantPhoneAccountDetails.assign(phone);

    pdcPhoneNumberReplicator.assignDynamicEvidenceToExtendedDetails(
      dynamicEvidenceDataDetails, participantPhoneAccountDetails);

    assertEquals("1", participantPhoneAccountDetails.phoneCountryCode);
  }
  //

}
