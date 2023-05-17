package curam.ca.gc.bdm.sl.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.address.impl.BDMAddressFormatINTL;
import curam.ca.gc.bdm.codetable.BDMCOSLOCATION;
import curam.ca.gc.bdm.codetable.BDMSTATUSOFAGREEMENT;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.facade.externalparty.intf.BDMExternalParty;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyRegistrationDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartySearchWizardKey;
import curam.ca.gc.bdm.sl.address.fact.BDMPDCAddressFactory;
import curam.ca.gc.bdm.sl.address.intf.BDMPDCAddress;
import curam.ca.gc.bdm.sl.tab.externalparty.impl.BDMExternalPartyHomeMenuLoader;
import curam.ca.gc.bdm.sl.tab.impl.BDMTabLoaderConsts;
import curam.ca.gc.bdm.test.data.impl.BDMExternalPartyTestDataDetails;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.ConcernRoleIDKey;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.MenuState;
import curam.util.tab.impl.MenuStateFactory;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)

public class BDMExternalPartyHomeMenuLoaderTest
  extends CuramServerTestJUnit4 {

  final BDMExternalParty bdmExtPartyObj =
    BDMExternalPartyFactory.newInstance();

  @Inject
  private EvidenceTypeDefDAO evidenceTypeDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  public BDMExternalPartyHomeMenuLoaderTest() {

    super();

  }

  @Test
  public void testloadMenuState()
    throws AppException, InformationalException {

    final MenuState menuState = MenuStateFactory.getMenuStateInstance();
    final Map<String, String> pageParameters = new HashMap<String, String>();
    final String[] idsToUpdate = {"SSACountryHistory" };
    final boolean isSSACountryExtParty = false;
    menuState.setVisible(isSSACountryExtParty,
      BDMTabLoaderConsts.kSSACountryHistory);

    final ConcernRoleIDKey key = new ConcernRoleIDKey();

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      this.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      this.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      this.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExtPartyObj.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    extPartyTestDataDetails.extPartyCRoleID =
      wizardResult.registrationResult.concernRoleID;

    pageParameters.put("concernRoleID",
      String.valueOf(extPartyTestDataDetails.extPartyCRoleID));

    final BDMExternalPartyHomeMenuLoader personHomeMenuLoader =
      new BDMExternalPartyHomeMenuLoader();
    personHomeMenuLoader.loadMenuState(menuState, pageParameters,
      idsToUpdate);
    assertTrue(menuState.getTestResult().length > 0);

  }

  public ExternalPartyRegistrationDetails
    getExternalPartyRegistrationDetails_Default()
      throws AppException, InformationalException {

    final ExternalPartyRegistrationDetails extPartyRegistrationDetails =
      new ExternalPartyRegistrationDetails();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.name =
      "Angola";

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.SSACOUNTRY;
    extPartyRegistrationDetails.externalPartyRegistrationDetails.registrationDate =
      Date.getCurrentDate();

    final OtherAddressData otherAddressData =
      this.getInternationAddressForExtParty();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.addressData =
      otherAddressData.addressData;

    return extPartyRegistrationDetails;
  }

  public OtherAddressData getInternationAddressForExtParty()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final BDMAddressFormatINTL bdmAddressFormatINTLobj =
      new BDMAddressFormatINTL();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "116";
    addressFieldDetails.addressLine1 = "8614";
    addressFieldDetails.addressLine2 = "Catalpa Ave";
    addressFieldDetails.stateProvince = "IL";
    addressFieldDetails.city = "Chicago";
    addressFieldDetails.zipCode = "60656";
    addressFieldDetails.countryCode = "AO";
    final OtherAddressData otherAddressData =
      bdmAddressFormatINTLobj.parseFieldsToData(addressFieldDetails);
    return otherAddressData;
  }

  public BDMExternalPartyRegistrationDetails
    getExternalPartyRegistrationCustomDetails()
      throws AppException, InformationalException {

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      new BDMExternalPartyRegistrationDetails();
    bdmextPartyRegDetails.centerOfSpeclizn = BDMCOSLOCATION.ALBERTA;
    bdmextPartyRegDetails.totalizatnCountry = BDMYESNO.YES;
    bdmextPartyRegDetails.isProtectiveDate = BDMYESNO.YES;
    bdmextPartyRegDetails.statusOfAgreement = BDMSTATUSOFAGREEMENT.INFORCE;
    bdmextPartyRegDetails.ssaLink =
      "https://www.canada.ca/en/employment-social-development.html";

    return bdmextPartyRegDetails;
  }

  public BDMExternalPartyTestDataDetails
    getRegisterExtPartySearchCriteria_Empty()
      throws AppException, InformationalException {

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      new BDMExternalPartyTestDataDetails();

    final WizardStateID wizardStateID = new WizardStateID();

    // Pass just empty object and it should create wizard and return the wizard
    // state ID.
    final BDMExternalPartySearchWizardKey wizardKey =
      bdmExtPartyObj.getRegisterExternalPartySearchCriteria(wizardStateID);

    extPartyTestDataDetails.wizardStateID =
      wizardKey.key.stateID.wizardStateID;

    return extPartyTestDataDetails;
  }

  public void mockUserObjects() throws AppException, InformationalException {

    new MockUp<TransactionInfo>() {

      @Mock
      public String getProgramUser()
        throws AppException, InformationalException {

        return "caseworker";

      }

    };

  }

}
