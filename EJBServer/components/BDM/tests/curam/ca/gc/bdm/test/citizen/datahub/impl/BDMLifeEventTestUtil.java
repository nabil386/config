package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.commonintake.codetable.METHODOFAPPLICATION;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.intf.ApplicationCase;
import curam.commonintake.facade.struct.ApplicationCaseCreateDetails;
import curam.core.fact.AddressFactory;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.ConcernFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonFactory;
import curam.core.struct.AddressDtls;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.ConcernDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.PersonDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

/*
 * The util for LifeEvent junits
 */
public class BDMLifeEventTestUtil {

  public BDMLifeEventTestUtil() {

    GuiceWrapper.getInjector().injectMembers(this);

  }

  /**
   * Creates a concern role with bare bones details
   *
   * @param internalID
   * @param concernRoleType
   * @param name
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createConcernRole(final long internalID, final String name)
    throws AppException, InformationalException {

    final ConcernDtls concernDtls = new ConcernDtls();
    concernDtls.concernID = internalID;
    concernDtls.creationDate = Date.getCurrentDate();
    concernDtls.name = "CRA";
    ConcernFactory.newInstance().insert(concernDtls);

    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = internalID;
    addressDtls.addressData =
      "1\n" + "100\n" + "BDMINTL\n" + "CA\n" + "1\n" + "1\n" + "COUNTRY=CA\n"
        + "POSTCODE=T3T 3T3\n" + "ZIP=\n" + "BDMZIPX=\n" + "APT=\n"
        + "POBOXNO=\n" + "ADD1=123\n" + "ADD2=Street\n" + "CITY=Edmonton\n"
        + "PROV=AB\n" + "STATEPROV=\n" + "BDMSTPROVX=";
    addressDtls.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressDtls.countryCode = COUNTRY.CA;
    AddressFactory.newInstance().insert(addressDtls);

    final ConcernRoleDtls crDtls = new ConcernRoleDtls();
    crDtls.concernID = concernDtls.concernID;
    crDtls.concernRoleID = internalID;
    crDtls.concernRoleName = name;
    crDtls.primaryAddressID = addressDtls.addressID;
    crDtls.concernRoleType = CONCERNROLETYPE.PERSON;
    crDtls.creationDate = Date.getCurrentDate();
    crDtls.registrationDate = Date.getCurrentDate();
    crDtls.startDate = Date.getCurrentDate();
    crDtls.sensitivity = "1";
    crDtls.regUserName = TransactionInfo.getProgramUser();

    ConcernRoleFactory.newInstance().insert(crDtls);

    final AlternateNameDtls alternateNameDtls = new AlternateNameDtls();
    alternateNameDtls.alternateNameID = internalID;
    alternateNameDtls.concernRoleID = internalID;
    alternateNameDtls.firstForename = name;
    alternateNameDtls.surname = name;
    AlternateNameFactory.newInstance().insert(alternateNameDtls);

    final PersonDtls personDtls = new PersonDtls();
    personDtls.concernRoleID = internalID;
    personDtls.personBirthName = name;
    personDtls.residencyAbroadInd = false;
    personDtls.dateOfBirthVerInd = false;
    personDtls.dateOfDeathVerInd = false;
    personDtls.primaryAlternateNameID = internalID;

    PersonFactory.newInstance().insert(personDtls);

    return crDtls.concernRoleID;
  }

  /**
   * Util method to create Application Case
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ApplicationCaseKey createApplicationCase(final long concernRoleID)
    throws AppException, InformationalException {

    // create Application case
    final ApplicationCase appCase = ApplicationCaseFactory.newInstance();

    final ApplicationCaseCreateDetails appCaseDetails =
      new ApplicationCaseCreateDetails();
    appCaseDetails.concernRoleID = concernRoleID;
    appCaseDetails.dtls.applicationCaseAdminID = 80001l;
    appCaseDetails.dtls.applicationDate = TransactionInfo.getSystemDate();
    appCaseDetails.dtls.methodOfApplication = METHODOFAPPLICATION.INPERSON;

    final curam.commonintake.entity.struct.ApplicationCaseKey appCaseKey =
      appCase.createApplicationCaseForConcernRole(appCaseDetails);

    return appCaseKey;
  }

}
