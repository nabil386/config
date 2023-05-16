/**
 *
 */
package curam.ca.gc.bdm.facade.taskquery.impl;

import curam.ca.gc.bdm.sl.taskquery.fact.BDMTaskQueryProcessFactory;
import curam.ca.gc.bdm.sl.taskquery.intf.BDMTaskQueryProcess;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryDetails;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryResult;
import curam.core.sl.entity.struct.QueryKey;
import curam.core.sl.fact.InboxFactory;
import curam.core.sl.infrastructure.impl.ClientActionConst;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.core.struct.InformationalMsgDtls;
import curam.message.BPOQUERY;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;

/**
 * This is a customized version of
 * the OOTB {@link curam.core.facade.impl.TaskQuery}.
 *
 * Customized the corresponding OOTB methods to handle
 * the custom attributes of case urgent flags and task escalation
 * level.
 *
 * @author donghua.jin
 *
 */
public class BDMTaskQuery
  extends curam.ca.gc.bdm.facade.taskquery.base.BDMTaskQuery {

  /**
   * Customized the OOTB createOrRunQuery method
   * to work with the new caseUrgentFlag and escalation level
   * fields.
   *
   * @param bdmTaskQueryDetails
   */
  @Override
  public BDMTaskQueryResult
    createOrRunBDMQuery(final BDMTaskQueryDetails bdmTaskQueryDetails)
      throws AppException, InformationalException {

    final BDMTaskQueryProcess bdmTaskQueryProcess =
      BDMTaskQueryProcessFactory.newInstance();

    BDMTaskQueryResult result = new BDMTaskQueryResult();

    if (bdmTaskQueryDetails.actionIDProperty
      .equals(ClientActionConst.kSave_Query)) {
      result = bdmTaskQueryProcess.createBDM(bdmTaskQueryDetails);
    } else if (bdmTaskQueryDetails.actionIDProperty
      .equals(ClientActionConst.kRun_Query)) {
      final ReadMultiOperationDetails readMulti =
        InboxFactory.newInstance().getInboxTaskReadMultiDetails();

      result = bdmTaskQueryProcess.runBDM(bdmTaskQueryDetails, readMulti);
    } else {
      final AppException e =
        new AppException(BPOQUERY.ERR_QUERY_FV_UNKNOWN_ACTION);

      e.arg(bdmTaskQueryDetails.actionIDProperty);
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(e,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    final InformationalManager infoMgr =
      TransactionInfo.getInformationalManager();
    // Obtain the informational(s) to be returned to the client
    final String[] warnings = infoMgr.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warnings[i];
      result.resultList.informationalMsgDetailsList.dtls
        .addRef(informationalMsgDtls);
    }

    return result;
  }

  /**
   * Modify the query criteria.
   *
   * This is to replace { @link modify() ) method.
   *
   * @param bdmTaskQueryDetails
   * @return result of the modification
   */
  @Override
  public BDMTaskQueryResult
    modifyBDM(final BDMTaskQueryDetails bdmTaskQueryDetails)
      throws AppException, InformationalException {

    return BDMTaskQueryProcessFactory.newInstance()
      .modifyBDM(bdmTaskQueryDetails);
  }

  /**
   * Read the details for the query.
   *
   * This is to replace { @link read() ) method.
   *
   * @param queryKey
   * @return task query details
   */
  @Override
  public BDMTaskQueryDetails readBDM(final QueryKey queryKey)
    throws AppException, InformationalException {

    return BDMTaskQueryProcessFactory.newInstance().readBDM(queryKey);
  }

  /**
   * Run the query.
   *
   * @param queryKey
   * @return task query details
   */
  @Override
  public BDMTaskQueryResult runBDM(final QueryKey queryKey)
    throws AppException, InformationalException {

    final ReadMultiOperationDetails readMulti =
      InboxFactory.newInstance().getInboxTaskReadMultiDetails();
    final BDMTaskQueryResult bdmResultList =
      BDMTaskQueryProcessFactory.newInstance().runBDM(queryKey, readMulti);

    final InformationalManager infoMgr =
      TransactionInfo.getInformationalManager();
    // Obtain the informational(s) to be returned to the client
    final String[] warnings = infoMgr.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warnings[i];
      bdmResultList.resultList.informationalMsgDetailsList.dtls
        .addRef(informationalMsgDtls);
    }

    return bdmResultList;
  }

}
