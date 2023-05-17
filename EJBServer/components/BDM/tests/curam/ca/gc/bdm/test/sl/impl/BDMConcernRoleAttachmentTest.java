package curam.ca.gc.bdm.test.sl.impl;

import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.attachment.fact.BDMConcernRoleAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.attachment.intf.BDMConcernRoleAttachmentLink;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentDetailsForList;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentLinkKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentDetails;
import curam.ca.gc.bdm.sl.attachment.fact.BDMConcernRoleAttachmentFactory;
import curam.ca.gc.bdm.sl.attachment.intf.BDMConcernRoleAttachment;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressFactory;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.ConcernFactory;
import curam.core.fact.ConcernRoleAttachmentLinkFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.PersonRegistration;
import curam.core.sl.impl.ConcernRoleAttachment;
import curam.core.struct.AddressDtls;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.ConcernDtls;
import curam.core.struct.ConcernRoleAttachmentLinkDtls;
import curam.core.struct.ConcernRoleAttachmentLinkKey;
import curam.core.struct.ConcernRoleAttachmentLinkReadmultiKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.core.struct.UniqueIDKeySet;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMConcernRoleAttachmentTest extends CuramServerTestJUnit4 {

  // private static long kapplicationCaseAdminID = 80001L;
  BDMConcernRoleAttachmentDetailsForList bdmConcernRoleAttachmentDetailsForList;

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  @Mocked
  ConcernRoleAttachment concernRoleAttachmentObjSL;

  BDMConcernRoleAttachment bdmConcernrRoleAttachmentObj;

  curam.ca.gc.bdm.facade.communication.intf.BDMConcernRoleAttachmentLink entityBDMConcernRoleAttachmentLink1;

  BDMConcernRoleAttachmentLink entityBDMConcernRoleAttachmentLink;

  @Before
  public void setUp() throws AppException, InformationalException {

    super.setUpCuramServerTest();

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "NO");
    bdmConcernrRoleAttachmentObj =
      BDMConcernRoleAttachmentFactory.newInstance();
    createConcernRole(100L, "John Smith");

    entityBDMConcernRoleAttachmentLink =
      BDMConcernRoleAttachmentLinkFactory.newInstance();

  }

  @After
  public void after() {

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "YES");
  }

  /***** Set up attchment details */

  private AttachmentDtls createAttachmentDtls()
    throws AppException, InformationalException {

    final AttachmentDtls attachmentDetails = new AttachmentDtls();

    attachmentDetails.attachmentName = "attachmentName";
    attachmentDetails.documentType = DOCUMENTTYPE.DIRECT_DEPOSIT;
    attachmentDetails.fileLocation = "FileLocation";
    attachmentDetails.receiptDate = Date.getCurrentDate();
    attachmentDetails.attachmentStatus = RECORDSTATUS.NORMAL;
    attachmentDetails.versionNo = 1;
    attachmentDetails.attachmentID =
      UniqueIDFactory.newInstance().getNextID();
    attachmentDetails.statusCode = RECORDSTATUS.NORMAL;

    AttachmentFactory.newInstance().insert(attachmentDetails);
    return attachmentDetails;

  }

  /**
   * Set up ConcernRoleAttachmentLinkDtls object
   */
  private ConcernRoleAttachmentLinkDtls createConcernRoleAttachmentLinkDtls(
    final long attachmentID, final long concernRoleID)
    throws AppException, InformationalException {

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      new ConcernRoleAttachmentLinkDtls();

    concernRoleAttachmentLinkDtls.attachmentID = attachmentID;
    concernRoleAttachmentLinkDtls.attachmentLinkID =
      UniqueIDFactory.newInstance().getNextID();
    concernRoleAttachmentLinkDtls.concernRoleID = concernRoleID;
    concernRoleAttachmentLinkDtls.dateReceived = Date.getCurrentDate();
    concernRoleAttachmentLinkDtls.description = "link descritpion";
    concernRoleAttachmentLinkDtls.statusCode = RECORDSTATUS.NORMAL;
    concernRoleAttachmentLinkDtls.versionNo = 1;

    ConcernRoleAttachmentLinkFactory.newInstance()
      .insert(concernRoleAttachmentLinkDtls);

    return concernRoleAttachmentLinkDtls;

  }

  public PersonDtls createPersonDtls()
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    return personDtls;

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
      "1\n" + "1\n" + "BDMINTL\n" + "IE\n" + "1\n" + "1\n" + "POBOXNO=\n"
        + "APT=\n" + "ADD1=1153\n" + "ADD2=Mole Street\n" + "CITY=Midway\n"
        + "PROV=\n" + "STATEPROV=\n" + "BDMSTPROVX=UT\n" + "COUNTRY=IE\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=60656\n" + "BDMUNPARSE=";
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

    return crDtls.concernRoleID;
  }

  /** Test Create concernRole attachment */

  @Test
  public void testCreateConcernRoleAttachment1()
    throws AppException, InformationalException {

    final long concerRoleID = createConcernRole(1001L, "Johnn Smith");

    final AttachmentDtls attachmentDetails = createAttachmentDtls();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        concerRoleID);
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();

    final BDMConcernRoleAttachmentLinkDtls insertObj =
      new BDMConcernRoleAttachmentLinkDtls();
    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();
    insertObj.fileSource = BDM_FILE_SOURCE.CLIENT;
    insertObj.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    // entityBDMConcernRoleAttachmentLink.insert(insertObj);

    // assertEquals(BDM_FILE_SOURCE.CLIENT, insertObj.fileSource);

    final BDMConcernRoleAttachmentLinkKey key =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    // Set details for modification
    final BDMConcernRoleAttachmentDetails bdmDetails =
      new BDMConcernRoleAttachmentDetails();

    bdmDetails.dtls.attachmentDtls.assign(attachmentDetails);
    bdmDetails.dtls.linkDtls.assign(concernRoleAttachmentLinkDtls);
    bdmDetails.fileSource = BDM_FILE_SOURCE.EMPLOYER;

    bdmConcernrRoleAttachmentObj.createConcernRoleAttachment(bdmDetails);

  }

  @Test
  public void testsearchConcernRoleAttachment()
    throws AppException, InformationalException {

    final long concerRoleID = createConcernRole(1001L, "John Smith");

    final AttachmentDtls attachmentDetails = createAttachmentDtls();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        concerRoleID);
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();

    final BDMConcernRoleAttachmentLinkDtls insertObj =
      new BDMConcernRoleAttachmentLinkDtls();

    insertObj.fileSource = BDM_FILE_SOURCE.CLIENT;
    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();
    insertObj.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    // assertEquals(BDM_FILE_SOURCE.CLIENT, insertObj.fileSource);

    final BDMConcernRoleAttachmentLinkKey key =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();
    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);
    final ConcernRoleAttachmentLinkReadmultiKey key1 =
      new ConcernRoleAttachmentLinkReadmultiKey();

    key1.concernRoleID = registrationIDDetails.concernRoleID;

    bdmConcernrRoleAttachmentObj.searchConcernRoleAttachment(key1);

  }

  @Test
  public void testreadConcernRoleAttachment()
    throws AppException, InformationalException {

    final long concerRoleID = createConcernRole(1001L, "John Smith");

    final AttachmentDtls attachmentDetails = createAttachmentDtls();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        concerRoleID);

    final BDMConcernRoleAttachmentLinkDtls insertObj =
      new BDMConcernRoleAttachmentLinkDtls();
    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();

    insertObj.fileSource = BDM_FILE_SOURCE.CLIENT;
    insertObj.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    entityBDMConcernRoleAttachmentLink.insert(insertObj);

    final BDMConcernRoleAttachmentLinkKey key =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    final BDMConcernRoleAttachmentLinkKey key1 =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    bdmConcernrRoleAttachmentObj.readConcernRoleAttachment(key1);

  }

  @Test
  public void testlistConcernRoleAttachments()
    throws AppException, InformationalException {

    final ConcernRoleAttachmentLinkReadmultiKey key =
      new ConcernRoleAttachmentLinkReadmultiKey();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    key.concernRoleID = pdcPersonDetails.concernRoleID;

    entityBDMConcernRoleAttachmentLink1.listConcernRoleAttachments(key);
    // assertTrue(key.caseID != 0);

  }

  @Test
  public void testlcreateConcernRoleAttachment()
    throws AppException, InformationalException {

    final BDMConcernRoleAttachmentDetails attchmentDetails =
      new BDMConcernRoleAttachmentDetails();
    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();

    attchmentDetails.dtls.attachmentDtls = attachment.read(attachmentKey);
    attchmentDetails.dtls.concernRoleAltID = "ID";
    attchmentDetails.dtls.concernRoleName = "CRM";

    entityBDMConcernRoleAttachmentLink1
      .createConcernRoleAttachment(attchmentDetails);

  }

  @Test
  public void testlcancelConcernRoleAttachment()
    throws AppException, InformationalException {

    final ConcernRoleAttachmentLinkKey key =
      new ConcernRoleAttachmentLinkKey();
    // Create concern role
    final long concerRoleID = createConcernRole(500L, "John");

    // create attachent
    final AttachmentDtls attachmentDetails = createAttachmentDtls();
    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();

    // create link between concerrole and attachment
    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        concerRoleID);
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();
    key.attachmentLinkID = UniqueIDFactory.newInstance().getNextID();

    entityBDMConcernRoleAttachmentLink1.cancelConcernRoleAttachment(key);

  }

  /** MODIFY ConcernRole attachemnt */

  @Test
  public void testModifyConcernRoleAttachment1()
    throws AppException, InformationalException {

    // Create concern role
    final long concerRoleID = createConcernRole(500L, "John");

    // create attachent
    final AttachmentDtls attachmentDetails = createAttachmentDtls();

    // create link between concerrole and attachment
    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDetails.attachmentID,
        concerRoleID);

    // Custom concernroleattacment link
    final BDMConcernRoleAttachmentLinkDtls insertObj =
      new BDMConcernRoleAttachmentLinkDtls();

    insertObj.fileSource = BDM_FILE_SOURCE.CLIENT;
    insertObj.attachmentLinkID =
      concernRoleAttachmentLinkDtls.attachmentLinkID;

    // Custom concernroleattacment link insert with file source
    entityBDMConcernRoleAttachmentLink.insert(insertObj);

    // assertEquals(100001L, insertObj.attachmentLinkID);

    final BDMConcernRoleAttachmentLinkKey key =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = concernRoleAttachmentLinkDtls.attachmentLinkID;

    // read custom concernroleattacment link file source
    BDMConcernRoleAttachmentLinkDtls readDtls =
      entityBDMConcernRoleAttachmentLink.read(key);

    assertEquals(BDM_FILE_SOURCE.CLIENT, readDtls.fileSource);

    // Set details for modification
    final BDMConcernRoleAttachmentDetails bdmDetails =
      new BDMConcernRoleAttachmentDetails();

    bdmDetails.dtls.attachmentDtls.assign(attachmentDetails);
    bdmDetails.dtls.linkDtls.assign(concernRoleAttachmentLinkDtls);
    bdmDetails.fileSource = BDM_FILE_SOURCE.EMPLOYER;

    // Call SL to modify concernroleattacment link file source
    bdmConcernrRoleAttachmentObj.modifyConcernRoleAttachment(bdmDetails);

    // read custom entity after modify
    readDtls = entityBDMConcernRoleAttachmentLink.read(key);

    // asseryt
    assertEquals(BDM_FILE_SOURCE.EMPLOYER, readDtls.fileSource);

  }

}
