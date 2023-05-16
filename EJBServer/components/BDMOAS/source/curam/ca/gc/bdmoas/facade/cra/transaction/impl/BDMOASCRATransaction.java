package curam.ca.gc.bdmoas.facade.cra.transaction.impl;

import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKey;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRADataDetailsCP;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRAHistoryDtlsList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestCreateDetails;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestCreateOptions;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestSummaryList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRATaxYearSummaryList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASTaxYearDetails;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASTaxYearDetailsKey;
import curam.ca.gc.bdmoas.sl.cra.transaction.fact.BDMOASCRATransactionFactory;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMOASCRATransaction implements
  curam.ca.gc.bdmoas.facade.cra.transaction.intf.BDMOASCRATransaction {

  /**
   * Gets the data details.
   *
   * @param key the key
   * @return the data details
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRADataDetailsCP
    getDataDetails(final BDMOASCRATransactionKey key)
      throws AppException, InformationalException {

    BDMOASCRADataDetailsCP cpDetails = new BDMOASCRADataDetailsCP();
    cpDetails = BDMOASCRATransactionFactory.newInstance().getDataDetails(key);
    return cpDetails;
  }

  /**
   * Gets the request options for case.
   *
   * @param key the key
   * @return the request options for case
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRARequestCreateOptions getRequestOptionsForCase(
    final CaseKey key) throws AppException, InformationalException {

    BDMOASCRARequestCreateOptions list = new BDMOASCRARequestCreateOptions();
    list =
      BDMOASCRATransactionFactory.newInstance().getRequestOptionsForCase(key);
    return list;
  }

  /**
   * Creates the request.
   *
   * @param details the details
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public void createRequest(final BDMOASCRARequestCreateDetails details)
    throws AppException, InformationalException {

    BDMOASCRATransactionFactory.newInstance().createRequest(details);
  }

  /**
   * List in progress for case.
   *
   * @param key the key
   * @return the BDMOASCRA request summary list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRARequestSummaryList listInProgressForCase(final CaseKey key)
    throws AppException, InformationalException {

    BDMOASCRARequestSummaryList list = new BDMOASCRARequestSummaryList();
    list =
      BDMOASCRATransactionFactory.newInstance().listInProgressForCase(key);
    return list;

  }

  /**
   * List in progress for concern role.
   *
   * @param key the key
   * @return the BDMOASCRA request summary list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRARequestSummaryList listInProgressForConcernRole(
    final ConcernRoleKey key) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return new BDMOASCRARequestSummaryList();
  }

  /**
   * List tax years for case.
   *
   * @param key the key
   * @return the BDMOASCRA tax year summary list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRATaxYearSummaryList listTaxYearsForCase(final CaseKey key)
    throws AppException, InformationalException {

    BDMOASCRATaxYearSummaryList list = new BDMOASCRATaxYearSummaryList();
    list = BDMOASCRATransactionFactory.newInstance().listTaxYearsForCase(key);
    return list;
  }

  /**
   * List tax years for concen role.
   *
   * @param key the key
   * @return the BDMOASCRA tax year summary list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRATaxYearSummaryList listTaxYearsForConcenRole(
    final ConcernRoleKey key) throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return new BDMOASCRATaxYearSummaryList();
  }

  /**
   * Gets the tax year details.
   *
   * @param key the key
   * @return the tax year details
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASTaxYearDetails
    getTaxYearDetails(final BDMOASTaxYearDetailsKey key)
      throws AppException, InformationalException {

    BDMOASTaxYearDetails details = new BDMOASTaxYearDetails();
    details =
      BDMOASCRATransactionFactory.newInstance().getTaxYearDetails(key);
    return details;
  }

  @Override
  public BDMOASCRAHistoryDtlsList
    getHistoryDetails(final BDMOASCRATransactionKey key)
      throws AppException, InformationalException {

    final BDMOASCRAHistoryDtlsList histDlList =
      BDMOASCRATransactionFactory.newInstance().getHistoryDetails(key);
    return histDlList;
  }

}
