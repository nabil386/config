package curam.ca.gc.bdm.facade.bdmhearing.impl;

import com.google.inject.Inject;
import curam.appeal.facade.struct.AppealCaseTabDetail;
import curam.appeal.sl.entity.struct.AppealCaseTabDetailKey;
import curam.ca.gc.bdm.sl.appeals.impl.BDMAppealTabHelper;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMAppealTab
  extends curam.ca.gc.bdm.facade.bdmhearing.base.BDMAppealTab {

  public BDMAppealTab() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  private BDMAppealTabHelper bdmAppealTabHelper;

  @Override
  public AppealCaseTabDetail
    readAppealCaseTabDetail(final AppealCaseTabDetailKey key)
      throws AppException, InformationalException {

    final AppealCaseTabDetail appCaseTabDtl = new AppealCaseTabDetail();

    appCaseTabDtl.dtls = bdmAppealTabHelper.readAppealCaseTabDetail(key);

    return appCaseTabDtl;
  }

}
