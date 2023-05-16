package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;
import java.util.Objects;

/**
 * @since ADO-21129
 *
 * The POJO for the bank evidence.
 */
public class BDMBankEvidenceVO {

  private String accountName;

  private String accountNumber;

  private String accountStatus;

  private String accountType;

  private String bdmBankAccountReason;

  private String branchTransitNumber;

  private String financialInstitutionNumber;

  private boolean jointAccountInd;

  private boolean preferredInd;

  private String sortCode;

  private Date fromDate;

  private Date toDate;

  private String comments;

  private long evidenceID;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  /**
   * Determines the equality of the bank evidence object and returns true if
   * both are equal otherwise return false.
   *
   * @param bankVOObj bank evidence object to compare
   * @return true if both are equal otherwise false
   */
  @Override
  public boolean equals(final Object bankVOObj) {

    if (this == bankVOObj) {
      return true;
    }

    if (bankVOObj == null || getClass() != bankVOObj.getClass()) {
      return false;
    }

    final BDMBankEvidenceVO bankVO = (BDMBankEvidenceVO) bankVOObj;

    // checks if the bank is same on both objects.
    return Objects.equals(accountName, bankVO.getAccountName());
  }

  public Date getToDate() {

    return toDate;
  }

  public void setToDate(final Date toDate) {

    this.toDate = toDate;
  }

  public String getSortCode() {

    return sortCode;
  }

  public void setSortCode(final String sortCode) {

    this.sortCode = sortCode;
  }

  public boolean getPreferredInd() {

    return preferredInd;
  }

  public void setPreferredInd(final boolean preferredInd) {

    this.preferredInd = preferredInd;
  }

  public boolean getJointAccountInd() {

    return jointAccountInd;
  }

  public void setJointAccountInd(final boolean jointAccountInd) {

    this.jointAccountInd = jointAccountInd;
  }

  public Date getFromDate() {

    return fromDate;
  }

  public void setFromDate(final Date fromDate) {

    this.fromDate = fromDate;
  }

  public String getFinancialInstitutionNumber() {

    return financialInstitutionNumber;
  }

  public void
    setFinancialInstitutionNumber(final String financialInstitutionNumber) {

    this.financialInstitutionNumber = financialInstitutionNumber;
  }

  public String getBranchTransitNumber() {

    return branchTransitNumber;
  }

  public void setBranchTransitNumber(final String branchTransitNumber) {

    this.branchTransitNumber = branchTransitNumber;
  }

  public String getBdmBankAccountReason() {

    return bdmBankAccountReason;
  }

  public void setBdmBankAccountReason(final String bdmBankAccountReason) {

    this.bdmBankAccountReason = bdmBankAccountReason;
  }

  public String getAccountType() {

    return accountType;
  }

  public void setAccountType(final String accountType) {

    this.accountType = accountType;
  }

  public String getAccountStatus() {

    return accountStatus;
  }

  public void setAccountStatus(final String accountStatus) {

    this.accountStatus = accountStatus;
  }

  public String getAccountNumber() {

    return accountNumber;
  }

  public void setAccountNumber(final String accountNumber) {

    this.accountNumber = accountNumber;
  }

  public String getAccountName() {

    return accountName;
  }

  public void setAccountName(final String accountName) {

    this.accountName = accountName;
  }

}
