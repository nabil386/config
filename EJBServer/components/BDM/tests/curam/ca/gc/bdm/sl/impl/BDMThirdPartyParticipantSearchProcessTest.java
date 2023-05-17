/**
 *
 */
package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.sl.struct.BDMThirdPartyParticipantSearchKey;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.ConcernRoleFactory;
import curam.core.sl.struct.AllParticipantSearchDetails;
import curam.core.sl.struct.AllParticipantSearchResult;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit test for
 * <code>curam.ca.gc.bdm.sl.impl.BDMThirdPartyParticipantSearchProcess</code>.
 *
 * @author donghua.jin
 *
 */
public class BDMThirdPartyParticipantSearchProcessTest extends BDMBaseTest {

  BDMThirdPartyParticipantSearchProcess process =
    new BDMThirdPartyParticipantSearchProcess();

  /**
   * Test search by reference number.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchParticipant()
    throws AppException, InformationalException {

    final PersonRegistrationResult regPersonDetails = this.registerPerson();

    final ConcernRoleKey crKey = new ConcernRoleKey();

    crKey.concernRoleID =
      regPersonDetails.registrationIDDetails.concernRoleID;

    final ConcernRoleDtls crDtls =
      ConcernRoleFactory.newInstance().read(crKey);

    final BDMThirdPartyParticipantSearchKey key =
      new BDMThirdPartyParticipantSearchKey();

    key.referenceNumber = crDtls.primaryAlternateID;

    final AllParticipantSearchResult searchResults =
      process.searchParticipant(key);

    assertEquals(1, searchResults.dtlsList.size());
  }

  /**
   * Test with ootb all participant search.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPerformOOTBAllParticipantSearch()
    throws AppException, InformationalException {

    final PersonRegistrationResult regPersonDetails = this.registerPerson();

    final ConcernRoleKey crKey = new ConcernRoleKey();

    crKey.concernRoleID =
      regPersonDetails.registrationIDDetails.concernRoleID;

    final ConcernRoleDtls crDtls =
      ConcernRoleFactory.newInstance().read(crKey);

    final BDMThirdPartyParticipantSearchKey key =
      new BDMThirdPartyParticipantSearchKey();

    key.referenceNumber = crDtls.primaryAlternateID;
    key.concernRoleName = crDtls.concernRoleName;
    key.addressLine1 = "6511";
    key.addressLine2 = "GILBER RD";

    final AllParticipantSearchResult searchResults =
      process.performOOTBAllParticipantSearch(key);

    assertEquals(1, searchResults.dtlsList.size());
  }

  /**
   * Test Key preparation.
   */
  @Test
  public void testCleanseSearchKey() {

    final BDMThirdPartyParticipantSearchKey key =
      new BDMThirdPartyParticipantSearchKey();

    key.referenceNumber = " 12345 ";
    key.concernRoleName = " 12345 ";
    key.unitNumber = " 12345 ";
    key.addressLine1 = " 12345 ";
    key.addressLine2 = " 12345 ";
    key.city = " 12345 ";
    key.stateProvince = " 12345 ";
    key.countryCode = " 12345 ";
    key.postalCode = " 12345 ";

    process.cleanseSearchKey(key);

    assertEquals("12345", key.referenceNumber);
    assertEquals("12345", key.concernRoleName);
    assertEquals("12345", key.unitNumber);
    assertEquals("12345", key.addressLine1);
    assertEquals("12345", key.addressLine2);
    assertEquals("12345", key.city);
    assertEquals("12345", key.stateProvince);
    assertEquals("12345", key.countryCode);
    assertEquals("12345", key.postalCode);
  }

  /**
   * Test key validations.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testValidateKey() throws AppException, InformationalException {

    BDMThirdPartyParticipantSearchKey key =
      new BDMThirdPartyParticipantSearchKey();

    try {
      process.validateKey(key);
      fail();
    } catch (final InformationalException e) {
      assertTrue(
        e.getExceptionDetails(0).contains("ERR_FV_SEARCH_CRITERIA_MISSING"));
      TransactionInfo.setInformationalManager();
    }

    try {
      key.postalCode = "M1A R2D";
      process.validateKey(key);
      fail();
    } catch (final InformationalException e) {
      assertTrue(
        e.getExceptionDetails(0).contains("ERR_SEARCH_MANDATORY_MISSING"));
      TransactionInfo.setInformationalManager();
    }

    try {
      key = new BDMThirdPartyParticipantSearchKey();
      key.concernRoleName = "A";
      process.validateKey(key);
      fail();
    } catch (final InformationalException e) {
      assertTrue(e.getExceptionDetails(0)
        .contains("ERR_PARTICIPANTSEARCH_FV_NAME_SHORT"));
      TransactionInfo.setInformationalManager();
    }

    try {
      key = new BDMThirdPartyParticipantSearchKey();
      key.concernRoleName = "John Doe";
      key.city = "A";
      process.validateKey(key);
      fail();
    } catch (final InformationalException e) {
      assertTrue(e.getExceptionDetails(0).contains("ERR_CITY_2CHARS"));
      TransactionInfo.setInformationalManager();
    }

    try {
      key = new BDMThirdPartyParticipantSearchKey();
      key.concernRoleName = "John Doe";
      key.stateProvince = "ab";
      process.validateKey(key);
      fail();
    } catch (final InformationalException e) {
      assertTrue(e.getExceptionDetails(0).contains("ERR_PROVINCE_3CHARS"));
      TransactionInfo.setInformationalManager();
    }

    try {
      key = new BDMThirdPartyParticipantSearchKey();
      key.concernRoleName = "John Doe";
      key.postalCode = "T1";
      process.validateKey(key);
      fail();
    } catch (final InformationalException e) {
      assertTrue(e.getExceptionDetails(0).contains("ERR_POSTALCODE_3CHARS"));
      TransactionInfo.setInformationalManager();
    }

  }

  /**
   * Test limit size of search results to return.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testGetPerticipantSearchResult()
    throws AppException, InformationalException {

    final AllParticipantSearchResult participantSearchResult =
      new AllParticipantSearchResult();
    participantSearchResult.dtlsList.add(new AllParticipantSearchDetails());
    participantSearchResult.dtlsList.add(new AllParticipantSearchDetails());
    participantSearchResult.dtlsList.add(new AllParticipantSearchDetails());

    assertEquals(2,
      process.getPerticipantSearchResult(participantSearchResult, 2).dtlsList
        .size());
    assertEquals(3,
      process.getPerticipantSearchResult(participantSearchResult, 4).dtlsList
        .size());
  }
}
