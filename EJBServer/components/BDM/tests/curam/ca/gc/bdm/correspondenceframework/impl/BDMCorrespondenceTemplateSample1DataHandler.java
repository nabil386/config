package curam.ca.gc.bdm.correspondenceframework.impl;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMCorrespondenceTemplateSample1DataHandler
  implements BDMCorrespondenceTemplateDataInterface {

  @Override
  /**
   * Get the required data for the letter template.
   */
  public Object getData(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final Object inputData) throws AppException, InformationalException {

    final BDMCorrespondenceMasterData correspondenceMasterData =
      new BDMCorrespondenceMasterData();
    final BDMCorrespondenceMasterInput input =
      (BDMCorrespondenceMasterInput) inputData;

    // TODO Retrieve the values based on input.clientConcernRoleID
    correspondenceMasterData.programCode = "OAS";
    correspondenceMasterData.applicationRecievedDate = Date.getCurrentDate();

    return correspondenceMasterData;
  }

}
