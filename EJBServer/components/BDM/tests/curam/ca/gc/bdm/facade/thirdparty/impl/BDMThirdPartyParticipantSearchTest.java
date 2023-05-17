/**
 *
 */
package curam.ca.gc.bdm.facade.thirdparty.impl;

import curam.ca.gc.bdm.sl.struct.BDMThirdPartyParticipantSearchKey;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.core.facade.struct.AllParticipantSearchDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.ConcernRoleFactory;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Junit test class for
 * <code>curam.ca.gc.bdm.facade.thirdparty.impl.BDMThirdPartyParticipantSearch</code>.
 *
 * @author donghua.jin
 *
 */
public class BDMThirdPartyParticipantSearchTest extends BDMBaseTest {

  BDMThirdPartyParticipantSearch facade =
    new BDMThirdPartyParticipantSearch();

  /**
   * Test search from facade.
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

    final AllParticipantSearchDetails searchResults =
      facade.searchParticipant(key);

    assertEquals(1, searchResults.dtls.dtlsList.size());
  }

}
