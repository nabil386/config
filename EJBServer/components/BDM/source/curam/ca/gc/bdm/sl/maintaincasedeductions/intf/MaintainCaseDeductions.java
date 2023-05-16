/**
 *
 */
package curam.ca.gc.bdm.sl.maintaincasedeductions.intf;

import curam.ca.gc.bdm.sl.struct.BDMPostAddressChangeKey;
import curam.ca.gc.bdm.sl.struct.CreateCaseDeductionDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * @author jigar.shah
 *
 */
public interface MaintainCaseDeductions {

  /**
   * This method will be used to record VTW Deduction By an Integrated Case
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void recordVTWDeductionByIC(BDMVTWDeductionKey key)
    throws AppException, InformationalException;

  /**
   * This method will be used to sync the VTW Deduction for Benefit Case.
   *
   * @param CaseHeaderKey
   * @throws AppException
   * @throws InformationalException
   */
  public void syncBenefitCaseVTWDeductions(CaseHeaderKey key)
    throws AppException, InformationalException;

  /**
   * This method will be used to create Tax Deductions.
   *
   * @param CaseHeaderKey
   * @throws AppException
   * @throws InformationalException
   */
  public void createTaxDeductions(CaseIDKey key)
    throws AppException, InformationalException;

  /**
   * This method will be used to sync the syncCaseDeductionsOnAddressChange for
   * Benefit Case.
   *
   * @param CaseHeaderKey
   * @throws AppException
   * @throws InformationalException
   */
  public void syncCaseDeductionsOnAddressChange(BDMPostAddressChangeKey key)
    throws AppException, InformationalException;

  /**
   * This method will be used to update Tax Deduction Status for
   * Benefit Case.
   *
   * @param CaseIDKey
   * @throws AppException
   * @throws InformationalException
   */
  public void updateTaxDeductionStatus(CaseIDKey key)
    throws AppException, InformationalException;

  /**
   * This method will be used to activate Tax Deduction Status for
   * Benefit Case.
   *
   * @param CaseIDKey
   * @throws AppException
   * @throws InformationalException
   */
  public void activateProvTaxDeduction(CaseIDKey key)
    throws AppException, InformationalException;

  /**
   * This method will be used to activate Tax Deduction Status for
   * Benefit Case.
   *
   * @param CaseIDKey
   * @throws AppException
   * @throws InformationalException
   */
  public void deactivateProvTaxDeduction(CaseIDKey key)
    throws AppException, InformationalException;

  /**
   * This method will be used to create Applied Deduction for Benefit Case.
   *
   * @param CaseHeaderKey
   * @throws AppException
   * @throws InformationalException
   */
  public void createAppliedCaseDeduction(final CreateCaseDeductionDetails key,
    final CaseIDKey caseIDKey) throws AppException, InformationalException;

  /**
   * This method will be used to regenerate Case Financials.
   *
   * @param CaseIDKey
   * @throws AppException
   * @throws InformationalException
   */
  public void regenerateCaseFinancials(final CaseIDKey caseIDKey)
    throws AppException, InformationalException;

  /**
   * This method will be used to regenerate Case Financials.
   *
   * @param CaseIDKey
   * @throws AppException
   * @throws InformationalException
   */
  public void processStatusDueVTWDeduction(final CaseDeductionItemKey key)
    throws AppException, InformationalException;
}
