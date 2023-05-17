/**
 *
 */
package curam.bdm.test.ca.gc.bdm.facade.communication.impl;

import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentDetailsForList;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.attachment.struct.BDMConcernRoleAttachmentLinkKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleAttachmentDetails;
import curam.ca.gc.bdm.sl.attachment.fact.BDMConcernRoleAttachmentFactory;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.struct.AddressDtls;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.ConcernDtls;
import curam.core.struct.ConcernRoleAttachmentLinkDtls;
import curam.core.struct.ConcernRoleAttachmentLinkKey;
import curam.core.struct.ConcernRoleAttachmentLinkReadmultiKey;
import curam.core.struct.ConcernRoleDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import mockit.integration.junit4.JMockit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Test cases for BDMConcernRoleAttachmentLink class
 *
 * @author jalpa.patel
 *
 */

@RunWith(JMockit.class)
public class BDMConcernRoleAttachmentLinkTest extends CuramServerTestJUnit4 {

  curam.ca.gc.bdm.sl.attachment.intf.BDMConcernRoleAttachment bdmConcernrRoleAttachmentObj;

  curam.ca.gc.bdm.facade.communication.intf.BDMConcernRoleAttachmentLink bdmConcernRoleAttachmentLinkObj;

  @Before
  public void setUp() throws AppException, InformationalException {

    super.setUpCuramServerTest();

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "NO");

    bdmConcernrRoleAttachmentObj =
      BDMConcernRoleAttachmentFactory.newInstance();

    bdmConcernRoleAttachmentLinkObj =
      curam.ca.gc.bdm.facade.communication.fact.BDMConcernRoleAttachmentLinkFactory
        .newInstance();

  }

  @After
  public void after() {

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "YES");
  }

  /** Test Create concernRole attachment */

  @Test
  public void testCreateConcernRoleAttachment()
    throws AppException, InformationalException {

    final long concerRoleID = createConcernRole(1001L, "Johnn Smith");

    final AttachmentDtls attachmentDtls = createAttachmentDtls();

    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDtls.attachmentID,
        concerRoleID);

    // Set details for modification
    final BDMConcernRoleAttachmentDetails bdmDetails =
      new BDMConcernRoleAttachmentDetails();

    bdmDetails.dtls.attachmentDtls.assign(attachmentDtls);
    bdmDetails.dtls.linkDtls.assign(concernRoleAttachmentLinkDtls);
    bdmDetails.fileSource = BDM_FILE_SOURCE.EMPLOYER;
    bdmDetails.dtls.linkDtls.attachmentLinkID =
      concernRoleAttachmentLinkDtls.attachmentLinkID;

    final BDMConcernRoleAttachmentLinkDtls bdmConcernRoleAttachmentLinkDtls =
      bdmConcernrRoleAttachmentObj.createConcernRoleAttachment(bdmDetails);

    final BDMConcernRoleAttachmentLinkKey key =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = bdmConcernRoleAttachmentLinkDtls.attachmentLinkID;

    final BDMConcernRoleAttachmentDetails readBDMConcernRoleAttachmentDetails =
      bdmConcernRoleAttachmentLinkObj.readConcernRoleAttachment(key);

    assertEquals(BDM_FILE_SOURCE.EMPLOYER,
      readBDMConcernRoleAttachmentDetails.fileSource);

  }

  @Test
  public void testlistConcernRoleAttachments()
    throws AppException, InformationalException {

    // Create ConcernRole
    final long concerRoleID = createConcernRole(1001L, "Johnn Smith");

    // Populate AttachemntDtls details
    final AttachmentDtls attachmentDtls = createAttachmentDtls();

    // Populate ConcernRoleAttachmentLinkDtls details
    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDtls.attachmentID,
        concerRoleID);

    // Set BDMConcernRoleAttachmentDetails
    final BDMConcernRoleAttachmentDetails bdmDetails =
      new BDMConcernRoleAttachmentDetails();

    bdmDetails.dtls.attachmentDtls.assign(attachmentDtls);
    bdmDetails.dtls.linkDtls.assign(concernRoleAttachmentLinkDtls);
    bdmDetails.fileSource = BDM_FILE_SOURCE.EMPLOYER;
    bdmDetails.dtls.linkDtls.attachmentLinkID =
      concernRoleAttachmentLinkDtls.attachmentLinkID;

    // Create createConcernRoleAttachmentLink

    bdmConcernRoleAttachmentLinkObj.createConcernRoleAttachment(bdmDetails);

    final ConcernRoleAttachmentLinkReadmultiKey key1 =
      new ConcernRoleAttachmentLinkReadmultiKey();

    key1.concernRoleID = concerRoleID;

    final BDMConcernRoleAttachmentDetailsForList list =
      bdmConcernRoleAttachmentLinkObj.listConcernRoleAttachments(key1);

    assertTrue(list.bdmCRDetails.size() == 1);

  }

  /** Cancel attachment */
  @Test
  public void testlcancelConcernRoleAttachment()
    throws AppException, InformationalException {

    final ConcernRoleAttachmentLinkKey concernRoleAttachmentLinkKey =
      new ConcernRoleAttachmentLinkKey();

    // Create ConcernRole
    final long concerRoleID = createConcernRole(1001L, "Johnn Smith");

    // Populate AttachemntDtls details
    final AttachmentDtls attachmentDtls = createAttachmentDtls();

    // Populate ConcernRoleAttachmentLinkDtls details
    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDtls.attachmentID,
        concerRoleID);

    // Set BDMConcernRoleAttachmentDetails
    final BDMConcernRoleAttachmentDetails bdmDetails =
      new BDMConcernRoleAttachmentDetails();

    bdmDetails.dtls.attachmentDtls.assign(attachmentDtls);
    bdmDetails.dtls.linkDtls.assign(concernRoleAttachmentLinkDtls);
    bdmDetails.fileSource = BDM_FILE_SOURCE.EMPLOYER;
    bdmDetails.dtls.linkDtls.attachmentLinkID =
      concernRoleAttachmentLinkDtls.attachmentLinkID;

    // Create createConcernRoleAttachmentLink
    final BDMConcernRoleAttachmentLinkDtls bdmConcernRoleAttachmentLinkDtls =
      bdmConcernrRoleAttachmentObj.createConcernRoleAttachment(bdmDetails);

    final BDMConcernRoleAttachmentLinkKey key =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = bdmConcernRoleAttachmentLinkDtls.attachmentLinkID;

    // Read BDMConcernRoleAttachmentDetails
    BDMConcernRoleAttachmentDetails readBDMConcernRoleAttachmentDetails =
      bdmConcernRoleAttachmentLinkObj.readConcernRoleAttachment(key);

    // assert
    assertEquals(BDM_FILE_SOURCE.EMPLOYER,
      readBDMConcernRoleAttachmentDetails.fileSource);

    // check if cancelled
    assertEquals(RECORDSTATUS.NORMAL,
      readBDMConcernRoleAttachmentDetails.dtls.linkDtls.statusCode);

    concernRoleAttachmentLinkKey.attachmentLinkID =
      bdmConcernRoleAttachmentLinkDtls.attachmentLinkID;

    // Cancel attachment
    bdmConcernRoleAttachmentLinkObj
      .cancelConcernRoleAttachment(concernRoleAttachmentLinkKey);

    // read attachment
    readBDMConcernRoleAttachmentDetails =
      bdmConcernRoleAttachmentLinkObj.readConcernRoleAttachment(key);

    // Verify attachmentlink is canceleld
    assertEquals(RECORDSTATUS.CANCELLED,
      readBDMConcernRoleAttachmentDetails.dtls.linkDtls.statusCode);

  }

  /** MODIFY ConcernRole attachemnt */

  @Test
  public void testModifyConcernRoleAttachment()
    throws AppException, InformationalException {

    // Create ConcernRole
    final long concerRoleID = createConcernRole(1001L, "Johnn Smith");

    // Populate AttachemntDtls details
    final AttachmentDtls attachmentDtls = createAttachmentDtls();

    // Populate ConcernRoleAttachmentLinkDtls details
    final ConcernRoleAttachmentLinkDtls concernRoleAttachmentLinkDtls =
      createConcernRoleAttachmentLinkDtls(attachmentDtls.attachmentID,
        concerRoleID);

    // Set BDMConcernRoleAttachmentDetails
    final BDMConcernRoleAttachmentDetails bdmDetails =
      new BDMConcernRoleAttachmentDetails();

    bdmDetails.dtls.attachmentDtls.assign(attachmentDtls);
    bdmDetails.dtls.linkDtls.assign(concernRoleAttachmentLinkDtls);
    bdmDetails.fileSource = BDM_FILE_SOURCE.EMPLOYER;
    bdmDetails.dtls.linkDtls.attachmentLinkID =
      concernRoleAttachmentLinkDtls.attachmentLinkID;

    // Create createConcernRoleAttachmentLink
    final BDMConcernRoleAttachmentLinkDtls bdmConcernRoleAttachmentLinkDtls =
      bdmConcernrRoleAttachmentObj.createConcernRoleAttachment(bdmDetails);

    final BDMConcernRoleAttachmentLinkKey key =
      new BDMConcernRoleAttachmentLinkKey();
    key.attachmentLinkID = bdmConcernRoleAttachmentLinkDtls.attachmentLinkID;

    // Read BDMConcernRoleAttachmentDetails
    BDMConcernRoleAttachmentDetails readBDMConcernRoleAttachmentDetails =
      bdmConcernRoleAttachmentLinkObj.readConcernRoleAttachment(key);

    // assert
    assertEquals(BDM_FILE_SOURCE.EMPLOYER,
      readBDMConcernRoleAttachmentDetails.fileSource);

    // Update file Source and modify

    bdmDetails.fileSource = BDM_FILE_SOURCE.CLIENT;

    bdmConcernRoleAttachmentLinkObj.modifyConcernRoleAttachment(bdmDetails);

    readBDMConcernRoleAttachmentDetails =
      bdmConcernRoleAttachmentLinkObj.readConcernRoleAttachment(key);

    // assert
    assertEquals(BDM_FILE_SOURCE.CLIENT,
      readBDMConcernRoleAttachmentDetails.fileSource);

  }

  /***** Set up attchment details */

  private AttachmentDtls createAttachmentDtls()
    throws AppException, InformationalException {

    final AttachmentDtls attachmentDetails = new AttachmentDtls();

    // attachmentDetails.attachmentName = "attachmentName";
    attachmentDetails.documentType = DOCUMENTTYPE.DIRECT_DEPOSIT;
    attachmentDetails.fileLocation = "FileLocation.doc";
    attachmentDetails.fileReference = "File reference";

    attachmentDetails.receiptDate = Date.getCurrentDate();
    attachmentDetails.attachmentStatus = RECORDSTATUS.NORMAL;
    attachmentDetails.versionNo = 1;
    attachmentDetails.attachmentID =
      UniqueIDFactory.newInstance().getNextID();
    attachmentDetails.statusCode = RECORDSTATUS.NORMAL;

    // AttachmentFactory.newInstance().insert(attachmentDetails);
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
    // concernRoleAttachmentLinkDtls.attachmentLinkID =
    // UniqueIDFactory.newInstance().getNextID();
    concernRoleAttachmentLinkDtls.concernRoleID = concernRoleID;
    concernRoleAttachmentLinkDtls.dateReceived = Date.getCurrentDate();
    concernRoleAttachmentLinkDtls.description = "link descritpion";
    concernRoleAttachmentLinkDtls.statusCode = RECORDSTATUS.NORMAL;
    concernRoleAttachmentLinkDtls.versionNo = 1;

    // ConcernRoleAttachmentLinkFactory.newInstance()
    // .insert(concernRoleAttachmentLinkDtls);

    return concernRoleAttachmentLinkDtls;

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

}
