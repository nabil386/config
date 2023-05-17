package curam.ca.gc.bdmoas.test.facade.servicesupplier.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASSERVICESUPPLIERTYPE;
import curam.ca.gc.bdmoas.facade.servicesupplier.fact.BDMOASServiceSupplierFactory;
import curam.ca.gc.bdmoas.facade.servicesupplier.intf.BDMOASServiceSupplier;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASModifyServSuppDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASReadServSuppDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASReadServSuppHomeDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppRegistrationWizardSearchDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchKey;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchWizardKey;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppTypeDetails;
import curam.ca.gc.bdmoas.test.data.impl.BDMOASServiceSupplierTestData;
import curam.ca.gc.bdmoas.test.data.impl.BDMOASServiceSupplierTestDataDetails;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.ReadServiceSupplierDetailsKey;
import curam.core.facade.struct.ReadServiceSupplierHomeKey;
import curam.core.facade.struct.ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.WizardStateID;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Somnath Misal
 *
 */
public class BDMOASServiceSupplierTest extends CuramServerTestJUnit4 {

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  /**
   * Test method to test getRegisterServiceSupplierSearchCriteria();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testGetRegisterServiceSupplierSearchCriteria()
    throws AppException, InformationalException {

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();
    final WizardStateID wizardStateID = new WizardStateID();

    final BDMOASServSuppSearchWizardKey wizardKey = bdmOASServSupObj
      .getRegisterServiceSupplierSearchCriteria(wizardStateID);

    assertTrue(wizardKey.stateID.wizardStateID != 0);

  }

  /**
   * Test method to test setRegisterServiceSupplierSearchCriteriaDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterServiceSupplierSearchCriteriaDetails()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.getRegisterServiceSupplierSearchCriteria_Empty();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = servSuppTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kNextPageAction;

    final BDMOASServSuppSearchWizardKey searchWizardKey =
      new BDMOASServSuppSearchWizardKey();

    searchWizardKey.searchKey.concernRoleName = "TestServSupp";
    searchWizardKey.searchKey.typeCode =
      BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST;

    final BDMOASServSuppRegistrationWizardSearchDetails details =
      bdmOASServSupObj.setRegisterServiceSupplierSearchCriteriaDetails(
        searchWizardKey, wizardStateID, actionIDProperty);

    final WizardStateID wizardStateID2 = new WizardStateID();
    wizardStateID2.wizardStateID = details.wizardStateID.wizardStateID;

    final BDMOASServSuppSearchWizardKey searchWizardKey2 = bdmOASServSupObj
      .getRegisterServiceSupplierSearchCriteria(wizardStateID2);

    assertTrue(searchWizardKey2.searchKey.typeCode
      .equals(BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST));

    assertTrue(searchWizardKey2.searchKey.concernRoleName
      .equals(searchWizardKey.searchKey.concernRoleName));

  }

  /**
   * Test method to test setRegisterServiceSupplierSearchCriteriaDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterServiceSupplierSearchCriteriaDetails_ForSearch()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = servSuppTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSearchAction;

    final BDMOASServSuppSearchWizardKey searchWizardKey =
      new BDMOASServSuppSearchWizardKey();

    searchWizardKey.searchKey.concernRoleName =
      servSuppTestDataDetails.kServSuppName;
    searchWizardKey.searchKey.typeCode =
      BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST;

    final BDMOASServSuppRegistrationWizardSearchDetails details =
      bdmOASServSupObj.setRegisterServiceSupplierSearchCriteriaDetails(
        searchWizardKey, wizardStateID, actionIDProperty);

    assertTrue(details.searchResult.dtls.dtlsList.size() > 0);

  }

  /**
   * Test method to test setRegisterServiceSupplierDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSetRegisterServiceSupplierDetails()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDataObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDataObj.getRegisterServiceSupplierSearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = servSuppTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMOASServSuppTypeDetails type = new BDMOASServSuppTypeDetails();
    type.typeCode = BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST;

    final ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails registrationDtls =
      servSuppTestDataObj.getServiceSupplierRegistrationDetails_Dafault();

    final ParticipantRegistrationWizardResult wizardResult =
      bdmOASServSupObj.setRegisterServiceSupplierDetails(registrationDtls,
        wizardStateID, actionIDProperty, type);

    assertTrue(wizardResult.registrationResult.concernRoleID != 0);
  }

  /**
   * Test method to test readHomePageDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testReadHomePageDetails()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final ReadServiceSupplierHomeKey key = new ReadServiceSupplierHomeKey();
    key.concernRoleHomePageKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final BDMOASReadServSuppHomeDetails homeDetails =
      bdmOASServSupObj.readHomePageDetails(key);

    assertTrue(homeDetails.typeCode
      .equals(BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST));

  }

  /**
   * Test method to test readServiceSupplierDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testReadServiceSupplierDetails()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final ReadServiceSupplierDetailsKey key =
      new ReadServiceSupplierDetailsKey();
    key.maintainConcernRoleKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final BDMOASReadServSuppDetails readDetails =
      bdmOASServSupObj.readServiceSupplierDetails(key);

    assertTrue(readDetails.typeCode
      .equals(BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST));

  }

  /**
   * Test method to test modifyServiceSupplierDetails();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testModifyServiceSupplierDetails()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final ReadServiceSupplierDetailsKey key =
      new ReadServiceSupplierDetailsKey();
    key.maintainConcernRoleKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final BDMOASReadServSuppDetails readDetails =
      bdmOASServSupObj.readServiceSupplierDetails(key);

    final BDMOASModifyServSuppDetails modifyServSuppDetails =
      new BDMOASModifyServSuppDetails();
    modifyServSuppDetails.typeCode = readDetails.typeCode;
    modifyServSuppDetails.srvcSuppModifyDetails.serviceSupplierDetails
      .assign(readDetails.srvcSuppReadDetails.serviceSupplierDetails);

    final String modifiedName = "Modified"
      + modifyServSuppDetails.srvcSuppModifyDetails.serviceSupplierDetails.name;
    modifyServSuppDetails.srvcSuppModifyDetails.serviceSupplierDetails.name =
      modifiedName;
    bdmOASServSupObj.modifyServiceSupplierDetails(modifyServSuppDetails);

    final BDMOASReadServSuppDetails readDetails2 =
      bdmOASServSupObj.readServiceSupplierDetails(key);
    assertTrue(readDetails2.srvcSuppReadDetails.serviceSupplierDetails.name
      .equals(modifiedName));

  }

  /**
   * Test method to test searchServiceSupplier();
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchServiceSupplier()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDateObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDateObj.setRegisterServiceSupplierDetails();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final ReadServiceSupplierDetailsKey key =
      new ReadServiceSupplierDetailsKey();
    key.maintainConcernRoleKey.concernRoleID =
      servSuppTestDataDetails.servSuppCRoleID;

    final BDMOASReadServSuppDetails readDetails =
      bdmOASServSupObj.readServiceSupplierDetails(key);

    final BDMOASServSuppSearchKey searchKey = new BDMOASServSuppSearchKey();
    searchKey.key.concernRoleName =
      readDetails.srvcSuppReadDetails.serviceSupplierDetails.name;
    searchKey.key.typeCode = readDetails.typeCode;

    final BDMOASServSuppSearchDetails servSuppSearchDetails =
      bdmOASServSupObj.searchServiceSupplier(searchKey);

    assertTrue(servSuppSearchDetails.dtls.dtlsList.size() > 0);

  }
}
