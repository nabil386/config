package curam.ca.gc.bdm.sl.communication.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.bdm.test.junit4.MockLogin;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.facade.externalparty.impl.BDMExternalParty;
import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantFactory;
import curam.ca.gc.bdm.facade.participant.struct.BDMReadParticipantAddressDetails;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.core.base.Person;
import curam.core.facade.struct.ReadParticipantAddressListKey;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonFactory;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.message.BPOCOMMUNICATION;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for class BDMCommunication
 */
@RunWith(JMockit.class)
public class BDMCommunicationHelperMockTest extends CuramServerTestJUnit4 {

  BDMCommunicationHelper bdmCommunicationHelper =
    new BDMCommunicationHelper();

  BDMExternalParty bdmExternalPartyFacade = new BDMExternalParty();

  public BDMCommunicationHelperMockTest() {

    super();

  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testGetAddressIDForConcern_Success(@Mocked
  final BDMParticipantFactory participantObj) throws Exception {

    try {
      new Expectations() {

        BDMReadParticipantAddressDetails addressDetails =
          new BDMReadParticipantAddressDetails();

        {
          addressDetails.addressID = 100l;
          participantObj
            .readMailingAddress((ReadParticipantAddressListKey) any);
          result = addressDetails;

        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }
    MockLogin.getInst().login("unauthenticated");
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = 100L;
    try {
      final long mailingAddressID = bdmCommunicationHelper
        .getAddressIDforConcern(concernRoleKey.concernRoleID, true);
      assertEquals(100, mailingAddressID);
    } catch (final AppException ae) {
    }

  }

  @Test
  public void testGetAddressIDForConcern_3rdPartyNull(@Mocked
  final BDMParticipantFactory participantObj, @Mocked
  final ConcernRoleFactory concernRoleObj) throws Exception {

    try {
      new Expectations() {

        ConcernRoleDtls mockDtls = new ConcernRoleDtls();

        BDMReadParticipantAddressDetails addressDetails =
          new BDMReadParticipantAddressDetails();
        {
          participantObj
            .readMailingAddress((ReadParticipantAddressListKey) any);
          result = addressDetails;

          concernRoleObj.read((ConcernRoleKey) any);
          result = mockDtls;
        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }
    MockLogin.getInst().login("unauthenticated");
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = 100L;
    try {
      bdmCommunicationHelper
        .getAddressIDforConcern(concernRoleKey.concernRoleID, true);
    } catch (final AppException ae) {
      assertEquals(BDMBPOCCT.ERR_ADDRESS_IS_MISSING, ae.getCatEntry());
    }

  }

  @Test
  public void testGetAddressIDForConcern_PersonNull(@Mocked
  final BDMParticipantFactory participantObj, @Mocked
  final ConcernRoleFactory concernRoleObj) throws Exception {

    try {
      new Expectations() {

        ConcernRoleDtls mockDtls = new ConcernRoleDtls();

        BDMReadParticipantAddressDetails addressDetails =
          new BDMReadParticipantAddressDetails();
        {
          participantObj
            .readMailingAddress((ReadParticipantAddressListKey) any);
          result = addressDetails;

          concernRoleObj.read((ConcernRoleKey) any);
          result = mockDtls;
        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }
    MockLogin.getInst().login("unauthenticated");
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = 100L;
    try {
      bdmCommunicationHelper
        .getAddressIDforConcern(concernRoleKey.concernRoleID, false);
    } catch (final AppException ae) {
      assertEquals(BDMBPOCCT.ERR_ADDRESS_IS_MISSING, ae.getCatEntry());
    }

  }

  @Test
  public void testvalidatePersonNotDeceased(@Mocked
  final PersonFactory personObj) throws Exception {

    new Expectations() {

      PersonDtls personDtls = new PersonDtls();

      {
        personDtls.dateOfDeath = Date.getCurrentDate();
        personObj.read((NotFoundIndicator) any, (PersonKey) any);
        result = personDtls;
      }
    };
    try {
      bdmCommunicationHelper.validatePersonNotDeceased(100);
    } catch (final AppException ae) {
      assertEquals(BPOCOMMUNICATION.ERR_COMM_CORRESPONDENT__IS_DECEASED,
        ae.getCatEntry());
    }
  }

  @Test
  public void testvalidatePersonNotDeceased_DODEmpty(@Mocked
  final PersonFactory personObj) throws Exception {

    new Expectations() {

      PersonDtls personDtls = new PersonDtls();

      {
        personObj.read((NotFoundIndicator) any, (PersonKey) any);
        result = personDtls;
      }
    };
    try {
      bdmCommunicationHelper.validatePersonNotDeceased(100);
    } catch (final AppException ae) {
    }
  }

  @Test
  public void testvalidatePersonNotDeceased_notFound() throws Exception {

    new Expectations() {

      {
        new MockUp<Person>() {

          @Mock
          public PersonDtls read(final NotFoundIndicator nf,
            final PersonKey personKey) {

            nf.setNotFound(true);
            return new PersonDtls();
          }
        };
      }
    };
    try {
      bdmCommunicationHelper.validatePersonNotDeceased(100);
    } catch (final AppException ae) {

    }
  }

}
