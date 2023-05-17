package curam.ca.gc.bdm.correspondenceframework.impl;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMCorrespondenceTemplateDataInterface {

  public Object getData(
    curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    Object inputData) throws AppException, InformationalException;

}
