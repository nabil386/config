
package curam.bdm.test.ca.gc.bdm.foreignsource.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMForeignCountryValidationHelperRuleSet.impl.ForeignCountryValidationHelper;
import curam.creole.ruleclass.BDMForeignCountryValidationHelperRuleSet.impl.ForeignCountryValidationHelper_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Task 58892 DEV: Implement 'Add Source of Client Phone Number'
/**
 * Tests the validation scenarios for source information introduced regarding
 * foreign countries.
 */
public class ForeignCountryValidationHelperTest
  extends CuramServerTestJUnit4 {

  private Session session;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  @Test
  public void mandatorySourceFieldFailure()
    throws AppException, InformationalException {

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    final ForeignCountryValidationHelper helper =
      ForeignCountryValidationHelper_Factory.getFactory()
        .newInstance(this.session, receivedFrom, null, null);

    assertTrue(helper.mandatorySourceFieldFailure().getValue());

  }

  @Test
  public void mandatorySourceFieldPass()
    throws AppException, InformationalException {

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    final CodeTableItem receivedFromCountry =
      this.getReceivedFromCountry(BDMSOURCECOUNTRY.US);

    final CodeTableItem modeOfReceipt =
      this.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    final ForeignCountryValidationHelper helper =
      ForeignCountryValidationHelper_Factory.getFactory().newInstance(
        this.session, receivedFrom, receivedFromCountry, modeOfReceipt);

    assertFalse(helper.mandatorySourceFieldFailure().getValue());

  }

  @Test
  public void disallowedSourceFieldFailure()
    throws AppException, InformationalException {

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.CLIENT_REPORTED);

    final CodeTableItem receivedFromCountry =
      this.getReceivedFromCountry(BDMSOURCECOUNTRY.US);

    final CodeTableItem modeOfReceipt =
      this.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    final ForeignCountryValidationHelper helper =
      ForeignCountryValidationHelper_Factory.getFactory()
        .newInstance(this.session, receivedFrom, receivedFromCountry, null);

    assertTrue(helper.disallowedSourceFieldFailure().getValue());

  }

  @Test
  public void restrictedCountryFailure()
    throws AppException, InformationalException {

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    final CodeTableItem receivedFromCountry =
      this.getReceivedFromCountry(BDMSOURCECOUNTRY.US);

    final CodeTableItem modeOfReceipt =
      this.getModeOfReceipt(BDMMODEOFRECEIPT.LIAISON);

    final ForeignCountryValidationHelper helper =
      ForeignCountryValidationHelper_Factory.getFactory().newInstance(
        this.session, receivedFrom, receivedFromCountry, modeOfReceipt);

    assertTrue(helper.restrictedCountryFailure().getValue());

  }

  @Test
  public void restrictedCountryPass()
    throws AppException, InformationalException {

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    final CodeTableItem receivedFromCountry =
      this.getReceivedFromCountry(BDMSOURCECOUNTRY.IRELAND);

    final CodeTableItem modeOfReceipt =
      this.getModeOfReceipt(BDMMODEOFRECEIPT.LIAISON);

    final ForeignCountryValidationHelper helper =
      ForeignCountryValidationHelper_Factory.getFactory().newInstance(
        this.session, receivedFrom, receivedFromCountry, modeOfReceipt);

    assertFalse(helper.restrictedCountryFailure().getValue());

  }

  @Test
  public void restrictedCountryPassNonLiaison()
    throws AppException, InformationalException {

    final CodeTableItem receivedFrom =
      this.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    final CodeTableItem receivedFromCountry =
      this.getReceivedFromCountry(BDMSOURCECOUNTRY.US);

    final CodeTableItem modeOfReceipt =
      this.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    final ForeignCountryValidationHelper helper =
      ForeignCountryValidationHelper_Factory.getFactory().newInstance(
        this.session, receivedFrom, receivedFromCountry, modeOfReceipt);

    assertFalse(helper.restrictedCountryFailure().getValue());

  }

  @Test
  public void isRestrictedCountry()
    throws AppException, InformationalException {

    final CodeTableItem receivedFromCountry =
      this.getReceivedFromCountry(BDMSOURCECOUNTRY.US);

    final ForeignCountryValidationHelper helper =
      ForeignCountryValidationHelper_Factory.getFactory()
        .newInstance(this.session, null, receivedFromCountry, null);

    assertTrue(helper.isRestrictedCountry().getValue());

  }

  public CodeTableItem getReceivedFrom(final String code) {

    return new CodeTableItem(BDMRECEIVEDFROM.TABLENAME, code);

  }

  public CodeTableItem getReceivedFromCountry(final String code) {

    return new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, code);

  }

  public CodeTableItem getModeOfReceipt(final String code) {

    return new CodeTableItem(BDMMODEOFRECEIPT.TABLENAME, code);

  }

}
