package curam.bdmoas.test.evidence.cra;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASCRAMARITALSTATUS;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.ConcernRoleKey;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for CRA Marital Status Dynamic evidence.
 *
 */
public class BDMOASCRAMaritalStatusTest extends BDMOASCaseTest {

  /**
   * Test CRA Marital Status evidence creation.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testVerifyInsertCRAMaritalStatusDetails()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = person.registrationIDDetails.concernRoleID;

    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final long caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    final EvidenceKey evidenceKey = createPDCCRAMaritalStatusEvidence(
      person.registrationIDDetails.concernRoleID, caseID);

    assertNotEquals(0, evidenceKey.evidenceID);

  }

  private EvidenceKey createPDCCRAMaritalStatusEvidence(
    final long concernRoleID, final long caseID)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final HashMap<String, String> craMaritalStatusAttributes =
      new HashMap<>();
    craMaritalStatusAttributes.put("participant",
      String.valueOf(cprObj.caseParticipantRoleID));
    craMaritalStatusAttributes.put("maritalStatus",
      BDMOASCRAMARITALSTATUS.MARRIED);

    final ReturnEvidenceDetails createPDCDynamicEvidence = BDMEvidenceUtil
      .createPDCDynamicEvidence(concernRoleID, craMaritalStatusAttributes,
        CASEEVIDENCEEntry.OAS_CRA_MARITAL_STATUS.getCode(),
        EVIDENCECHANGEREASON.INITIAL);

    return createPDCDynamicEvidence.evidenceKey;
  }

}
