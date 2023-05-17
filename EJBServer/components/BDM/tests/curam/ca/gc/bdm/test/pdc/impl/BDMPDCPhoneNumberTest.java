package curam.ca.gc.bdm.test.pdc.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.pdc.impl.BDMPDCPhoneNumberReplicatorExtenderImpl;
import curam.ca.gc.bdm.sl.pdc.fact.BDMPDCPhoneNumberFactory;
import curam.ca.gc.bdm.sl.pdc.intf.BDMPDCPhoneNumber;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.struct.ConcernRolePhoneNumberKey;
import curam.core.struct.PhoneNumberDtls;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMPDCPhoneNumberTest extends CuramServerTestJUnit4 {

  @Inject
  BDMPDCPhoneNumberReplicatorExtenderImpl pdcPhoneNumberReplicator;

  public BDMPDCPhoneNumberTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Test
  public void testinsert() throws AppException, InformationalException {

    final ParticipantPhoneDetails participantPhoneAccountDetails =
      new ParticipantPhoneDetails();
    ConcernRolePhoneNumberKey concernRolePh = new ConcernRolePhoneNumberKey();

    final PhoneNumberDtls phone = new PhoneNumberDtls();
    phone.phoneCountryCode = "";
    phone.phoneAreaCode = "555";
    phone.phoneNumber = "9999999";
    phone.phoneNumberID = 12345;

    participantPhoneAccountDetails.assign(phone);

    final BDMPDCPhoneNumber bdmPDCPhoneObj =
      BDMPDCPhoneNumberFactory.newInstance();

    concernRolePh = bdmPDCPhoneObj.insert(participantPhoneAccountDetails);

    assertTrue(concernRolePh.concernRolePhoneNumberID != 0);
  }

  @Test
  public void testmodify() throws AppException, InformationalException {

    final ParticipantPhoneDetails participantPhoneAccountDetails =
      new ParticipantPhoneDetails();

    final PhoneNumberDtls phone = new PhoneNumberDtls();
    phone.phoneCountryCode = "";
    phone.phoneNumberID = 12345;

    participantPhoneAccountDetails.assign(phone);

    final BDMPDCPhoneNumber bdmPDCPhoneObj =
      BDMPDCPhoneNumberFactory.newInstance();

    bdmPDCPhoneObj.modify(participantPhoneAccountDetails);

  }
  //

}
