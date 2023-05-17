package curam.ca.gc.bdm.facade.creole.rulesetadmin.impl;

import curam.ca.gc.bdm.facade.creole.rulesetadmin.struct.BDMRuleSetSearchKey;
import curam.ca.gc.bdm.facade.creole.rulesetadmin.struct.BDMRuleSetSearchResultDetails;
import curam.ca.gc.bdm.message.BDMBPOCREOLERULESETADMIN;
import curam.core.facade.infrastructure.creole.rulesetadmin.codetable.impl.CREOLERULESETDISPSTATUSEntry;
import curam.core.facade.infrastructure.creole.rulesetadmin.fact.CREOLERuleSetAdminFactory;
import curam.core.facade.infrastructure.creole.rulesetadmin.struct.CREOLERuleSetListDetailsList;
import curam.core.facade.pdt.struct.InformationalMessage;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.creole.struct.CREOLERuleSetCategoryKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;

public class BDMCREOLERuleSetAdmin extends
  curam.ca.gc.bdm.facade.creole.rulesetadmin.base.BDMCREOLERuleSetAdmin {

  @Override
  public BDMRuleSetSearchResultDetails
    listAllRuleSetsForCategory(final BDMRuleSetSearchKey key)
      throws AppException, InformationalException {

    final CREOLERuleSetCategoryKey creoleRuleSetCategoryKey =
      new CREOLERuleSetCategoryKey();
    creoleRuleSetCategoryKey.creoleRuleSetCategoryID = key.categoryID;

    // OOTB search by category
    final CREOLERuleSetListDetailsList creoleRuleSetListDetailsList =
      CREOLERuleSetAdminFactory.newInstance()
        .listAllRuleSetsForCategory(creoleRuleSetCategoryKey);

    final int size = creoleRuleSetListDetailsList.dtls.size();
    for (int i = size - 1; i >= 0; i--) {

      // filter out by display name
      if (key.displayName != null) {
        key.displayName = key.displayName.trim().toLowerCase();
        if (!key.displayName.isEmpty()
          && !creoleRuleSetListDetailsList.dtls.item(i).ruleSetDescription
            .toLowerCase().contains(key.displayName)) {
          creoleRuleSetListDetailsList.dtls.remove(i);
          continue;
        }
      }

      // filter out by status
      if (!StringUtil.isNullOrEmpty(key.statusCode)
        && !creoleRuleSetListDetailsList.dtls.item(i).displayStatus
          .equals(CREOLERULESETDISPSTATUSEntry.get(key.statusCode)
            .toUserLocaleString())) {
        creoleRuleSetListDetailsList.dtls.remove(i);
      }

    }

    final BDMRuleSetSearchResultDetails ret =
      new BDMRuleSetSearchResultDetails();
    ret.dtls = creoleRuleSetListDetailsList;

    if (ret.dtls.dtls.size() == 0) {
      final InformationalManager informationalManager =
        TransactionInfo.getInformationalManager();

      informationalManager.addInformationalMsg(
        new LocalisableString(BDMBPOCREOLERULESETADMIN.INF_NO_RULE_SET_FOUND),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kWarning);

      final String[] warnings =
        informationalManager.obtainInformationalAsString();

      for (int i = 0; i < warnings.length; i++) {
        final InformationalMessage informationalMessage =
          new InformationalMessage();

        informationalMessage.message = warnings[i];
        ret.msg.dtls.addRef(informationalMessage);
      }
    }

    return ret;
  }

}
