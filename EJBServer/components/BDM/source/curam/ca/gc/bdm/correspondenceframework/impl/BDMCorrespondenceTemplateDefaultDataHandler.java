package curam.ca.gc.bdm.correspondenceframework.impl;

import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMCorrespondenceTemplateDefaultDataHandler
  implements BDMCorrespondenceTemplateDataInterface {

  @Override
  /**
   * Use SQL when the expectation is to only return one record
   */
  public Object getData(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final Object inputData) throws AppException, InformationalException {

    CuramValueList<?> curamValueList = null;
    try {

      curamValueList = DynamicDataAccess.executeNsMulti(
        Class.forName(configDtls.outputClassName), inputData, false, true,
        configDtls.sql);

    } catch (final ClassNotFoundException e) {
      e.printStackTrace();
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE,
        e);
    }
    if (curamValueList != null && !curamValueList.isEmpty()) {
      return curamValueList.get(0);
    }

    return null;
  }

}
