package curam.ca.gc.bdm.test.rest.bdmcitizenaccountapi.impl;

import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.impl.BDMCitizenAccountAPI;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfile;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfileTaxWithholds;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.fact.CaseDeductionItemFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMCitizenAccountAPITest extends CuramServerTestJUnit4 {

  @Mocked
  CaseDeductionItemFactory caseDeductionItemFactory;

  private BDMCitizenAccountAPI createTestSubject() {

    return new BDMCitizenAccountAPI();
  }

  private void setUpBDMReadProfile()
    throws AppException, InformationalException {

    new Expectations() {

      {

      }
    };

    new MockUp<BDMCitizenAccountAPI>() {

      @Mock
      public BDMUAProfile readProfile() {

        final BDMUAProfile profile = new BDMUAProfile();

        // add tax withhold
        final BDMUAProfileTaxWithholds taxWithhold =
          new BDMUAProfileTaxWithholds();
        taxWithhold.deduction = "$50";
        taxWithhold.effective_date = Date.getCurrentDate().toString();
        taxWithhold.end_date = "";
        taxWithhold.tax_withhold_id = "-5139733074736578560.0";
        taxWithhold.program = "Stimulus Program";
        profile.registeredPersonDetails.profileTaxWithholds.add(taxWithhold);

        return profile;
      }
    };

  }

  @MethodRef(name = "readProfile", signature = "readProfile")
  @Test
  public void testGetProfileTaxWithholds() throws Exception {

    setUpBDMReadProfile();
    curam.ca.gc.bdm.rest.bdmcitizenaccountapi.intf.BDMCitizenAccountAPI testSubject;
    BDMUAProfile result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.readProfile();

    BDMUAProfileTaxWithholds profileTaxWithholds =
      new BDMUAProfileTaxWithholds();
    profileTaxWithholds =
      result.registeredPersonDetails.profileTaxWithholds.get(0);

    assertEquals(profileTaxWithholds.deduction, "$50");
    assertEquals(profileTaxWithholds.effective_date,
      Date.getCurrentDate().toString());
    assertEquals(profileTaxWithholds.end_date, "");
    assertEquals(profileTaxWithholds.tax_withhold_id,
      "-5139733074736578560.0");
    assertEquals(profileTaxWithholds.program, "Stimulus Program");

  }

}
