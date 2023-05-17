package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl;

import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceIOExceptionStub;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Suite of tests for the getAndSaveFolderTree() function in the
 * BDMCCTOutboundInterfaceImpl class.
 */
@Ignore
public class BDMCCTGetAndSaveFolderTreeTests
  extends BDMCCTOutboundInterfaceImplTest {

  /**
   * Test getAndSaveFolderTree() for a scenario where the inputs are valid and
   * as
   * such the operation should succeed.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetAndSaveFolderTree_success() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    templateReq.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final boolean successInd =
      interfaceObj.getAndSaveFolderTree(templateReq, new Date());

    assertTrue(successInd);
  }

  /**
   * Test getAndSaveFolderTree to see that it can be repeated (folders and
   * templates) can be removed and replenished without an error.
   *
   * @throws Exception
   */
  @Test
  public void testGetAndSaveFolderTree_replaceTemplates() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    templateReq.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    assertTrue(interfaceObj.getAndSaveFolderTree(templateReq, new Date()));

    assertTrue(interfaceObj.getAndSaveFolderTree(templateReq, new Date()));
  }

  /**
   * Test getAndSaveFolderTree for a scenario where the incorrect community name
   * is provided, causing the test to fail.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetAndSaveFolderTree_wrongCommunity() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    templateReq.setCommunity("wrong");
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final boolean successInd =
      interfaceObj.getAndSaveFolderTree(templateReq, new Date());

    assertFalse(successInd);
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where a userId is not provided
   * to
   * the request.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetAndSaveFolderTree_userIDMissing() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq.setUserID("");
    templateReq.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getAndSaveFolderTree(templateReq, new Date()));

    assertEquals(exception.getLocalizedMessage(),
      BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED.toString());
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where the community field
   * provided
   * is empty.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetAndSaveFolderTree_communityMissing() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    templateReq.setCommunity("");
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getAndSaveFolderTree(templateReq, new Date()));

    assertEquals(exception.getLocalizedMessage(),
      BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED.toString());
  }

  /**
   * Test getAndSaveFolderTree() for scenario where the includetemplates field
   * for
   * the request is false.
   *
   * NOTE: This has to be handled as currently it throws a
   * NPE.
   *
   * @throws Exception
   */
  @Override
  @Test(expected = Exception.class)
  public void testGetAndSaveFolderTree_includeTemplatesIsFalse()
    throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    templateReq.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    templateReq.setIncludeTemplatesInd(false);
    templateReq.setIncludeTemplateFieldsInd(true);

    interfaceObj.getAndSaveFolderTree(templateReq, new Date());
  }

  /**
   * Test getAndSaveFolderTree() for scenario where includetemplatefields is
   * false.
   *
   * NOTE: This has to be handled as currently it throws an NPE.
   *
   * @throws Exception
   */
  @Override
  @Test(expected = Exception.class)
  public void testGetAndSaveFolderTree_includeTemplateFieldsIsFalse()
    throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    templateReq.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(false);

    interfaceObj.getAndSaveFolderTree(templateReq, new Date());
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where an IOException is raised.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetAndSaveFolderTree_raiseIOException() throws Exception {

    final BDMCCTGetFolderTreeRequest request =
      new BDMCCTGetFolderTreeRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setIncludeTemplatesInd(true);
    request.setIncludeTemplateFieldsInd(false);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getAndSaveFolderTree(request, new Date()));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_CCT_NOT_AVAILABLE);
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where the templates and folders
   * effective date is not "CURRENT_DATE".
   *
   * @throws Exception
   */
  @Test
  public void testGetAndSaveFolderTree_effectiveDateNotCurrent()
    throws Exception {

    final BDMCCTGetFolderTreeRequest request =
      new BDMCCTGetFolderTreeRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setIncludeTemplatesInd(true);
    request.setIncludeTemplateFieldsInd(true);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl() {

        @Override
        public boolean getAndSaveFolderTree(
          final BDMCCTGetFolderTreeRequest templateReq,
          final Date processingDate)
          throws AppException, InformationalException {

          final String responseBody = "{ \"Status\": \"success\", \"Data\": "
            + "{ \"Id\": \"0\", \"Name\": null, "
            + "\"Folders\": [ { \"Id\": \"172541\", "
            + "\"Name\": \"HealthCheck\", \"Description\": "
            + "\"HealthCheck\", \"Folders\": [], \"Templates\""
            + ": [ { \"Id\": \"172574\", \"Name\": "
            + "\"R90081\", \"Description\": \"R90081\", "
            + "\"Path\": \"/HealthCheck\", "
            + "\"EffectiveDateSelection\": \"\", \"Version\": "
            + "\"1.0\", \"fields\": [ \"Returned_to_Sender\", "
            + "\"Client_First_Name\", \"Client_Address_line_2\", "
            + "\"Recovery_Office_Address\", \"CAC\", \"Recovery"
            + "_Office_Province\", \"Payment_Office_Address1\", "
            + "\"Payment_Office_Address3\", \"Certified_Mail\","
            + " \"Client_Address_line_3\", \"OFCR_Phone\", "
            + "\"Recovery_Office_City\", \"Payment_Office_Pos"
            + "tal_Code\", \"Client_Last_Name\", \"Payment_Offi"
            + "ce_Address2\", \"Payment_Office_Address4\", \"Cl"
            + "ient_Address_line_1\", \"Officer_Toll_Free_Number_En"
            + "g\", \"Client_Address_line_4\", \"Client_ID_Numb"
            + "er\", \"Officer_Name\", \"Recovery_Office_Posta"
            + "l_Code\", \"Correspondence_Key\", \"Correspondenc"
            + "e_Language\", \"TotalRecoverable\", \"Officer_Tol"
            + "l_Free_Number_Fr\" ] } ] } ], \"Templates\": [] } }";
          createTemplateAndFolder(responseBody, processingDate);
          return true;
        }
      };

    final boolean successInd =
      interfaceObj.getAndSaveFolderTree(request, Date.getCurrentDate());

    // This check is redundant, an error not being
    // thrown would show same result
    assertTrue(successInd);
  }
}
