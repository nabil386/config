package curam.ca.gc.bdm.correspondenceframework.impl;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;

public interface BDMCorrespondenceValidationInterface {

  public InformationalManager validateTemplateContext(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final InformationalManager informationalManager)
    throws AppException, InformationalException;

  public InformationalManager validateData(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final Object pojoObject,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final InformationalManager informationalManager)
    throws AppException, InformationalException;

}
