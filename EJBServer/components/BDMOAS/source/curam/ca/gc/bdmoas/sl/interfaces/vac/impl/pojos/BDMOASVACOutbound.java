/**
 * VACOutbound POJO
 */
package curam.ca.gc.bdmoas.sl.interfaces.vac.impl.pojos;

import com.google.gson.annotations.SerializedName;
import curam.util.type.Date;

public class BDMOASVACOutbound {

  @SerializedName(value = "oasRegionCode")
  private String oasRegionCode;

  @SerializedName(value = "oasAccountNumber")
  private String oasAccountNumber;

  @SerializedName(value = "wvaNumber")
  private String wvaNumber;

  @SerializedName(value = "statCode")
  private String statCode;

  @SerializedName(value = "clientCode")
  private String clientCode;

  @SerializedName(value = "typeCode")
  private String typeCode;

  @SerializedName(value = "wvaRegionCode")
  private String wvaRegionCode;

  @SerializedName(value = "maritalStatusCode")
  private String maritalStatusCode;

  @SerializedName(value = "exclusionCode")
  private String exclusionCode;

  @SerializedName(value = "fileNumber")
  private String fileNumber;

  @SerializedName(value = "surName")
  private String surName;

  @SerializedName(value = "givenName")
  private String givenName;

  @SerializedName(value = "wvaSin")
  private String wvaSin;

  @SerializedName(value = "wvaDob")
  private String wvaDob;

  @SerializedName(value = "spouseWVANumber")
  private String spouseWVANumber;

  @SerializedName(value = "spouseName")
  private String spouseName;

  @SerializedName(value = "spouseSin")
  private String spouseSin;

  @SerializedName(value = "spouseDob")
  private String spouseDob;

  @SerializedName(value = "controlAccNumber")
  private String controlAccNumber;

  @SerializedName(value = "spouseAccNumber")
  private String spouseAccNumber;

  @SerializedName(value = "clientSurNameID")
  private String clientSurNameID;

  @SerializedName(value = "namePartOne")
  private String namePartOne;

  @SerializedName(value = "accountType")
  private String accountType;

  @SerializedName(value = "accountStatus")
  private String accountStatus;

  @SerializedName(value = "effectiveStatusDate")
  private Date effectiveStatusDate;

  @SerializedName(value = "spaEntitlementDate")
  private Date spaEntitlementDate;

  @SerializedName(value = "oasEntitlementDate")
  private Date oasEntitlementDate;

  @SerializedName(value = "hwcDob")
  private String hwcDob;

  @SerializedName(value = "paymentMaritalStatus")
  private String paymentMaritalStatus;

  @SerializedName(value = "totalEligibleYears")
  private String totalEligibleYears;

  @SerializedName(value = "grossEntitlement")
  private String grossEntitlement;

  @SerializedName(value = "gisEntitlement")
  private String gisEntitlement;

  @SerializedName(value = "nonJointCode")
  private String nonJointCode;

  @SerializedName(value = "namePartTwo")
  private String namePartTwo;

  @SerializedName(value = "overpaymentCode")
  private String overpaymentCode;

  @SerializedName(value = "opRecoveryRate")
  private String opRecoveryRate;

  @SerializedName(value = "gisEntitlementDate")
  private Date gisEntitlementDate;

  @SerializedName(value = "optionCode")
  private String optionCode;

  @SerializedName(value = "deemedIncome")
  private String deemedIncome;

  @SerializedName(value = "incomeInd")
  private String incomeInd;

  @SerializedName(value = "totalIncome")
  private String totalIncome;

  @SerializedName(value = "cppIncome")
  private String cppIncome;

  @SerializedName(value = "pensionIncome")
  private String pensionIncome;

  @SerializedName(value = "uibIncome")
  private String uibIncome;

  @SerializedName(value = "interestIncome")
  private String interestIncome;

  @SerializedName(value = "dividendIncome")
  private String dividendIncome;

  @SerializedName(value = "rentalIncome")
  private String rentalIncome;

  @SerializedName(value = "empIncome")
  private String empIncome;

  @SerializedName(value = "selfEmpIncome")
  private String selfEmpIncome;

  @SerializedName(value = "otherIncome")
  private String otherIncome;

  @SerializedName(value = "spouseGISEntitlementDate")
  private Date spouseGISEntitlementDate;

  @SerializedName(value = "spouseOptionCode")
  private String spouseOptionCode;

  @SerializedName(value = "spouseDeemedIncome")
  private String spouseDeemedIncome;

  @SerializedName(value = "spouseIncomeInd")
  private String spouseIncomeInd;

  @SerializedName(value = "spouseTotalIncome")
  private String spouseTotalIncome;

  @SerializedName(value = "spouseCPPIncome")
  private String spouseCPPIncome;

  @SerializedName(value = "spousePensionIncome")
  private String spousePensionIncome;

  @SerializedName(value = "spouseUIBIncome")
  private String spouseUIBIncome;

  @SerializedName(value = "spouseInterestIncome")
  private String spouseInterestIncome;

  @SerializedName(value = "spouseDividendIncome")
  private String spouseDividendIncome;

  @SerializedName(value = "spouseRentalIncome")
  private String spouseRentalIncome;

  @SerializedName(value = "spouseEmpIncome")
  private String spouseEmpIncome;

  @SerializedName(value = "spouseSelfEmpIncome")
  private String spouseSelfEmpIncome;

  @SerializedName(value = "spouseOtherIncome")
  private String spouseOtherIncome;

  /**
   * Getter - oasRegionCode
   */
  public String getOASRegionCode() {

    return this.oasRegionCode;
  }

  /**
   * Setter - oasRegionCode
   */
  public void setOASRegionCode(final String oasRegionCode) {

    this.oasRegionCode = oasRegionCode;
  }

  /**
   * Getter - oasAccountNumber
   */
  public String getOASAccountNumber() {

    return this.oasAccountNumber;
  }

  /**
   * Setter - oasAccountNumber
   */
  public void setOASAccountNumber(final String oasAccountNumber) {

    this.oasAccountNumber = oasAccountNumber;
  }

  /**
   * Getter - wvaNumber
   */
  public String getWVANumber() {

    return this.wvaNumber;
  }

  /**
   * Setter - wvaNumber
   */
  public void setWVANumber(final String wvaNumber) {

    this.wvaNumber = wvaNumber;
  }

  /**
   * Getter - statCode
   */
  public String getStatCode() {

    return this.statCode;
  }

  /**
   * Setter - statCode
   */
  public void setStatCode(final String statCode) {

    this.statCode = statCode;
  }

  /**
   * Getter - clientCode
   */
  public String getClientCode() {

    return this.clientCode;
  }

  /**
   * Setter - oasRegionCode
   */
  public void setClientCode(final String clientCode) {

    this.clientCode = clientCode;
  }

  /**
   * Getter - typeCode
   */
  public String getTypeCode() {

    return this.typeCode;
  }

  /**
   * Setter - typeCode
   */
  public void setTypeCode(final String typeCode) {

    this.typeCode = typeCode;
  }

  /**
   * Getter - wvaRegionCode
   */
  public String getWVARegionCode() {

    return this.wvaRegionCode;
  }

  /**
   * Setter - wvaRegionCode
   */
  public void setWVARegionCode(final String wvaRegionCode) {

    this.wvaRegionCode = wvaRegionCode;
  }

  /**
   * Getter - maritalStatusCode
   */
  public String getMaritalStatusCode() {

    return this.maritalStatusCode;
  }

  /**
   * Setter - maritalStatusCode
   */
  public void setMaritalStatusCode(final String maritalStatusCode) {

    this.maritalStatusCode = maritalStatusCode;
  }

  /**
   * Getter - exclusionCode
   */
  public String getExclusionCode() {

    return this.exclusionCode;
  }

  /**
   * Setter - exclusionCode
   */
  public void setExclusionCode(final String exclusionCode) {

    this.exclusionCode = exclusionCode;
  }

  /**
   * Getter - fileNumber
   */
  public String getFileNumber() {

    return this.fileNumber;
  }

  /**
   * Setter - fileNumber
   */
  public void setFileNumber(final String fileNumber) {

    this.fileNumber = fileNumber;
  }

  /**
   * Getter - surName
   */
  public String getSurName() {

    return this.surName;
  }

  /**
   * Setter - surName
   */
  public void setSurName(final String surName) {

    this.surName = surName;
  }

  /**
   * Getter - givenName
   */
  public String getGivenName() {

    return this.givenName;
  }

  /**
   * Setter - givenName
   */
  public void setGivenName(final String givenName) {

    this.givenName = givenName;
  }

  /**
   * Getter - wvaSin
   */
  public String getWVASin() {

    return this.wvaSin;
  }

  /**
   * Setter - wvaSin
   */
  public void setWVASin(final String wvaSin) {

    this.wvaSin = wvaSin;
  }

  /**
   * Getter - wvaDob
   */
  public String getWVADob() {

    return this.wvaDob;
  }

  /**
   * Setter - wvaDob
   */
  public void setWVADob(final String wvaDob) {

    this.wvaDob = wvaDob;
  }

  /**
   * Getter - spouseWVANumber
   */
  public String getSpouseWVANumber() {

    return this.spouseWVANumber;
  }

  /**
   * Setter - spouseWVANumber
   */
  public void setSpouseWVANumber(final String spouseWVANumber) {

    this.spouseWVANumber = spouseWVANumber;
  }

  /**
   * Getter - spouseName
   */
  public String getSpouseName() {

    return this.spouseName;
  }

  /**
   * Setter - spouseName
   */
  public void setSpouseName(final String spouseName) {

    this.spouseName = spouseName;
  }

  /**
   * Getter - spouseDob
   */
  public String getSpouseDob() {

    return this.spouseDob;
  }

  /**
   * Setter - spouseDob
   */
  public void setSpouseDob(final String spouseDob) {

    this.spouseDob = spouseDob;
  }

  /**
   * Getter - spouseSin
   */
  public String getSpouseSin() {

    return this.spouseSin;
  }

  /**
   * Setter - spouseSin
   */
  public void setSpouseSin(final String spouseSin) {

    this.spouseSin = spouseSin;
  }

  /**
   * Getter - controlAccNumber
   */
  public String getControlAccNumber() {

    return this.controlAccNumber;
  }

  /**
   * Setter - controlAccNumber
   */
  public void setControlAccNumber(final String controlAccNumber) {

    this.controlAccNumber = controlAccNumber;
  }

  /**
   * Getter - spouseAccNumber
   */
  public String getSpouseAccNumber() {

    return this.spouseAccNumber;
  }

  /**
   * Setter - spouseAccNumber
   */
  public void setSpouseAccNumber(final String spouseAccNumber) {

    this.spouseAccNumber = spouseAccNumber;
  }

  /**
   * Getter - clientSurNameID
   */
  public String getClientSurNameID() {

    return this.clientSurNameID;
  }

  /**
   * Setter - clientSurNameID
   */
  public void setClientSurNameID(final String clientSurNameID) {

    this.clientSurNameID = clientSurNameID;
  }

  /**
   * Getter - namePartOne
   */
  public String getNamePartOne() {

    return this.namePartOne;
  }

  /**
   * Setter - namePartOne
   */
  public void setNamePartOne(final String namePartOne) {

    this.namePartOne = namePartOne;
  }

  /**
   * Getter - accountType
   */
  public String getAccountType() {

    return this.accountType;
  }

  /**
   * Setter - accountType
   */
  public void setAccountType(final String accountType) {

    this.accountType = accountType;
  }

  /**
   * Getter - accountStatus
   */
  public String getAccountStatus() {

    return this.accountStatus;
  }

  /**
   * Setter - accountStatus
   */
  public void setAccountStatus(final String accountStatus) {

    this.accountStatus = accountStatus;
  }

  /**
   * Getter - effectiveStatusDate
   */
  public Date getEffectiveStatusDate() {

    return this.effectiveStatusDate;
  }

  /**
   * Setter - effectiveStatusDate
   */
  public void setEffectiveStatusDate(final Date effectiveStatusDate) {

    this.effectiveStatusDate = effectiveStatusDate;
  }

  /**
   * Getter - spaEntitlementDate
   */
  public Date getSpaEntitlementDate() {

    return this.spaEntitlementDate;
  }

  /**
   * Setter - spaEntitlementDate
   */
  public void setSpaEntitlementDate(final Date spaEntitlementDate) {

    this.spaEntitlementDate = spaEntitlementDate;
  }

  /**
   * Getter - oasEntitlementDate
   */
  public Date getOASEntitlementDate() {

    return this.oasEntitlementDate;
  }

  /**
   * Setter - oasEntitlementDate
   */
  public void setOASEntitlementDate(final Date oasEntitlementDate) {

    this.oasEntitlementDate = oasEntitlementDate;
  }

  /**
   * Getter - hwcDob
   */
  public String getHWCDob() {

    return this.hwcDob;
  }

  /**
   * Setter - hwcDob
   */
  public void setHWCDob(final String hwcDob) {

    this.hwcDob = hwcDob;
  }

  /**
   * Getter - paymentMaritalStatus
   */
  public String getPaymentMaritalStatus() {

    return this.paymentMaritalStatus;
  }

  /**
   * Setter - paymentMaritalStatus
   */
  public void setPaymentMaritalStatus(final String paymentMaritalStatus) {

    this.paymentMaritalStatus = paymentMaritalStatus;
  }

  /**
   * Getter - totalEligibleYears
   */
  public String getTotalEligibleYears() {

    return this.totalEligibleYears;
  }

  /**
   * Setter - totalEligibleYears
   */
  public void setTotalEligibleYears(final String totalEligibleYears) {

    this.totalEligibleYears = totalEligibleYears;
  }

  /**
   * Getter - grossEntitlement
   */
  public String getGrossEntitlement() {

    return this.grossEntitlement;
  }

  /**
   * Setter - grossEntitlement
   */
  public void setGrossEntitlement(final String grossEntitlement) {

    this.grossEntitlement = grossEntitlement;
  }

  /**
   * Getter - gisEntitlement
   */
  public String getGISEntitlement() {

    return this.gisEntitlement;
  }

  /**
   * Setter - gisEntitlement
   */
  public void setGISEntitlement(final String gisEntitlement) {

    this.gisEntitlement = gisEntitlement;
  }

  /**
   * Getter - nonJointCode
   */
  public String getNonJointCode() {

    return this.nonJointCode;
  }

  /**
   * Setter - nonJointCode
   */
  public void setNonJointCode(final String nonJointCode) {

    this.nonJointCode = nonJointCode;
  }

  /**
   * Getter - namePartTwo
   */
  public String getNamePartTwo() {

    return this.namePartTwo;
  }

  /**
   * Setter - namePartTwo
   */
  public void setNamePartTwo(final String namePartTwo) {

    this.namePartTwo = namePartTwo;
  }

  /**
   * Getter - overpaymentCode
   */
  public String getOverpaymentCode() {

    return this.overpaymentCode;
  }

  /**
   * Setter - overpaymentCode
   */
  public void setOverpaymentCode(final String overpaymentCode) {

    this.overpaymentCode = overpaymentCode;
  }

  /**
   * Getter - opRecoveryRate
   */
  public String getOPRecoveryRate() {

    return this.opRecoveryRate;
  }

  /**
   * Setter - opRecoveryRate
   */
  public void setOPRecoveryRate(final String opRecoveryRate) {

    this.opRecoveryRate = opRecoveryRate;
  }

  /**
   * Getter - gisEntitlementDate
   */
  public Date getGISEntitlementDate() {

    return this.gisEntitlementDate;
  }

  /**
   * Setter - gisEntitlementDate
   */
  public void setGISEntitlementDate(final Date gisEntitlementDate) {

    this.gisEntitlementDate = gisEntitlementDate;
  }

  /**
   * Getter - optionCode
   */
  public String getOptionCode() {

    return this.optionCode;
  }

  /**
   * Setter - optionCode
   */
  public void setOptionCode(final String optionCode) {

    this.optionCode = optionCode;
  }

  /**
   * Getter - deemedIncome
   */
  public String getDeemedIncome() {

    return this.deemedIncome;
  }

  /**
   * Setter - deemedIncome
   */
  public void setDeemedIncome(final String deemedIncome) {

    this.deemedIncome = deemedIncome;
  }

  /**
   * Getter - incomeInd
   */
  public String getIncomeInd() {

    return this.incomeInd;
  }

  /**
   * Setter - incomeInd
   */
  public void setIncomeInd(final String incomeInd) {

    this.incomeInd = incomeInd;
  }

  /**
   * Getter - totalIncome
   */
  public String getTotalIncome() {

    return this.totalIncome;
  }

  /**
   * Setter - totalIncome
   */
  public void setTotalIncome(final String totalIncome) {

    this.totalIncome = totalIncome;
  }

  /**
   * Getter - cppIncome
   */
  public String getCPPIncome() {

    return this.cppIncome;
  }

  /**
   * Setter - cppIncome
   */
  public void setCPPIncome(final String cppIncome) {

    this.cppIncome = cppIncome;
  }

  /**
   * Getter - pensionIncome
   */
  public String getPensionIncome() {

    return this.pensionIncome;
  }

  /**
   * Setter - pensionIncome
   */
  public void setPensionIncome(final String pensionIncome) {

    this.pensionIncome = pensionIncome;
  }

  /**
   * Getter - uibIncome
   */
  public String getUIBIncome() {

    return this.uibIncome;
  }

  /**
   * Setter - uibIncome
   */
  public void setUIBIncome(final String uibIncome) {

    this.uibIncome = uibIncome;
  }

  /**
   * Getter - interestIncome
   */
  public String getInterestIncome() {

    return this.interestIncome;
  }

  /**
   * Setter - interestIncome
   */
  public void setInterestIncome(final String interestIncome) {

    this.interestIncome = interestIncome;
  }

  /**
   * Getter - dividendIncome
   */
  public String getDividendIncome() {

    return this.dividendIncome;
  }

  /**
   * Setter - dividendIncome
   */
  public void setDividendIncome(final String dividendIncome) {

    this.dividendIncome = dividendIncome;
  }

  /**
   * Getter - rentalIncome
   */
  public String getRentalIncome() {

    return this.rentalIncome;
  }

  /**
   * Setter - rentalIncome
   */
  public void setRentalIncome(final String rentalIncome) {

    this.rentalIncome = rentalIncome;
  }

  /**
   * Getter - empIncome
   */
  public String getEmpIncome() {

    return this.empIncome;
  }

  /**
   * Setter - empIncome
   */
  public void setEmpIncome(final String empIncome) {

    this.empIncome = empIncome;
  }

  /**
   * Getter - selfEmpIncome
   */
  public String getSelfEmpIncome() {

    return this.selfEmpIncome;
  }

  /**
   * Setter - selfEmpIncome
   */
  public void setSelfEmpIncome(final String selfEmpIncome) {

    this.selfEmpIncome = selfEmpIncome;
  }

  /**
   * Getter - otherIncome
   */
  public String getOtherIncome() {

    return this.otherIncome;
  }

  /**
   * Setter - otherIncome
   */
  public void setOtherIncome(final String otherIncome) {

    this.otherIncome = otherIncome;
  }

  /**
   * Getter - spouseGISEntitlementDate
   */
  public Date getSpouseGISEntitlementDate() {

    return this.spouseGISEntitlementDate;
  }

  /**
   * Setter - spouseGISEntitlementDate
   */
  public void
    setSpouseGISEntitlementDate(final Date spouseGISEntitlementDate) {

    this.spouseGISEntitlementDate = spouseGISEntitlementDate;
  }

  /**
   * Getter - spouseOptionCode
   */
  public String getSpouseOptionCode() {

    return this.spouseOptionCode;
  }

  /**
   * Setter - spouseOptionCode
   */
  public void setSpouseOptionCode(final String spouseOptionCode) {

    this.spouseOptionCode = spouseOptionCode;
  }

  /**
   * Getter - spouseDeemedIncome
   */
  public String getSpouseDeemedIncome() {

    return this.spouseDeemedIncome;
  }

  /**
   * Setter - spouseDeemedIncome
   */
  public void setSpouseDeemedIncome(final String spouseDeemedIncome) {

    this.spouseDeemedIncome = spouseDeemedIncome;
  }

  /**
   * Getter - spouseIncomeInd
   */
  public String getSpouseIncomeInd() {

    return this.spouseIncomeInd;
  }

  /**
   * Setter - spouseIncomeInd
   */
  public void setSpouseIncomeInd(final String spouseIncomeInd) {

    this.spouseIncomeInd = spouseIncomeInd;
  }

  /**
   * Getter - spouseTotalIncome
   */
  public String getSpouseTotalIncome() {

    return this.spouseTotalIncome;
  }

  /**
   * Setter - spouseTotalIncome
   */
  public void setSpouseTotalIncome(final String spouseTotalIncome) {

    this.spouseTotalIncome = spouseTotalIncome;
  }

  /**
   * Getter - spouseCPPIncome
   */
  public String getspouseCPPInSome() {

    return this.spouseCPPIncome;
  }

  /**
   * Setter - spouseCPPIncome
   */
  public void setSpouseCPPIncome(final String spouseCPPIncome) {

    this.spouseCPPIncome = spouseCPPIncome;
  }

  /**
   * Getter - spousePensionIncome
   */
  public String getSpousePensionIncome() {

    return this.spousePensionIncome;
  }

  /**
   * Setter - spousePensionIncome
   */
  public void setSpousePensionIncome(final String spousePensionIncome) {

    this.spousePensionIncome = spousePensionIncome;
  }

  /**
   * Getter - spouseUIBIncome
   */
  public String getSpouseUIBIncome() {

    return this.spouseUIBIncome;
  }

  /**
   * Setter - spouseUIBIncome
   */
  public void setSpouseUIBIncome(final String spouseUIBIncome) {

    this.spouseUIBIncome = spouseUIBIncome;
  }

  /**
   * Getter - spouseInterestIncome
   */
  public String getSpouseInterestIncome() {

    return this.spouseInterestIncome;
  }

  /**
   * Setter - spouseInterestIncome
   */
  public void setSpouseInterestIncome(final String spouseInterestIncome) {

    this.spouseInterestIncome = spouseInterestIncome;
  }

  /**
   * Getter - spouseDividendIncome
   */
  public String getSpouseDividendIncome() {

    return this.spouseDividendIncome;
  }

  /**
   * Setter - spouseDividendIncome
   */
  public void setSpouseDividendIncome(final String spouseDividendIncome) {

    this.spouseDividendIncome = spouseDividendIncome;
  }

  /**
   * Getter - spouseRentalIncome
   */
  public String getSpouseRentalIncome() {

    return this.spouseRentalIncome;
  }

  /**
   * Setter - spouseRentalIncome
   */
  public void setSpouseRentalIncome(final String spouseRentalIncome) {

    this.spouseRentalIncome = spouseRentalIncome;
  }

  /**
   * Getter - spouseOtherIncome
   */
  public String getSpouseOtherIncome() {

    return this.spouseOtherIncome;
  }

  /**
   * Setter - spouseOtherIncome
   */
  public void setSpouseOtherIncome(final String spouseOtherIncome) {

    this.spouseUIBIncome = spouseOtherIncome;
  }

  /**
   * Getter - spouseEmpIncome
   */
  public String getSpouseEmpIncome() {

    return this.spouseEmpIncome;
  }

  /**
   * Setter - spouseEmpIncome
   */
  public void setSpouseEmpIncome(final String spouseEmpIncome) {

    this.spouseEmpIncome = spouseEmpIncome;
  }

  /**
   * Getter - spouseSelfEmpIncome
   */
  public String getSpouseSelfEmpIncome() {

    return this.spouseSelfEmpIncome;
  }

  /**
   * Setter - spouseSelfEmpIncome
   */
  public void setSpouseSelfEmpIncome(final String spouseSelfEmpIncome) {

    this.spouseSelfEmpIncome = spouseSelfEmpIncome;
  }

}
