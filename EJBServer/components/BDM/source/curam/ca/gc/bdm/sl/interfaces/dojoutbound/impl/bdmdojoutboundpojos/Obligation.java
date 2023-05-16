
package curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Obligation {

  @SerializedName("RecordID")
  @Expose
  private Integer RecordID;

  @SerializedName("MetadataIdentificationID")
  @Expose
  private String MetadataIdentificationID;

  @SerializedName("ObligationArrearsBalance")
  @Expose
  private Double ObligationArrearsBalance;

  @SerializedName("ObligationFixedAmount")
  @Expose
  private Double ObligationFixedAmount;

  @SerializedName("ObligationDebtPercentage")
  @Expose
  private Integer ObligationDebtPercentage;

  @SerializedName("ObligationVendorCode")
  @Expose
  private String ObligationVendorCode;

  @SerializedName("ObligationFixedAmountIndicator")
  @Expose
  private Integer ObligationFixedAmountIndicator;

  @SerializedName("ObligationPaymentAmount")
  @Expose
  private Double ObligationPaymentAmount;

  @SerializedName("ObligationOutstandingFeeBalance")
  @Expose
  private Double ObligationOutstandingFeeBalance;

  @SerializedName("ObligationPaidAmount")
  @Expose
  private Double ObligationPaidAmount;

  @SerializedName("ObligationItcCode")
  @Expose
  private String ObligationItcCode;

  @SerializedName("ObligationOCONRegionalCode")
  @Expose
  private String ObligationOCONRegionalCode;

  @SerializedName("ObligationCanadianProvinceCode")
  @Expose
  private String ObligationCanadianProvinceCode;

  @SerializedName("ObligationNumberOfWeek")
  @Expose
  private Integer ObligationNumberOfWeek;

  @SerializedName("ObligationDebtSequenceNumber")
  @Expose
  private Integer ObligationDebtSequenceNumber;

  @SerializedName("ObligationPaymentDate")
  @Expose
  private String ObligationPaymentDate;

  @SerializedName("ObligationFundsAvailable")
  @Expose
  private Double ObligationFundsAvailable;

  @SerializedName("ObligationDebtRatioPercentage")
  @Expose
  private Double ObligationDebtRatioPercentage;

  @SerializedName("ObligationAmountPerDebt")
  @Expose
  private Double ObligationAmountPerDebt;

  @SerializedName("ObligationDivertAmount")
  @Expose
  private Double ObligationDivertAmount;

  @SerializedName("ObligationAmountOfFeeIncluded")
  @Expose
  private Double ObligationAmountOfFeeIncluded;

  @SerializedName("ObligationException")
  @Expose
  private String ObligationException;

  @SerializedName("Person")
  @Expose
  private Person Person;

  public Integer getRecordID() {

    return RecordID;
  }

  public void setRecordID(final Integer RecordID) {

    this.RecordID = RecordID;
  }

  public String getMetadataIdentificationID() {

    return MetadataIdentificationID;
  }

  public void
    setMetadataIdentificationID(final String metadataIdentificationID) {

    this.MetadataIdentificationID = metadataIdentificationID;
  }

  public Double getObligationArrearsBalance() {

    return ObligationArrearsBalance;
  }

  public void
    setObligationArrearsBalance(final Double obligationArrearsBalance) {

    this.ObligationArrearsBalance = obligationArrearsBalance;
  }

  public Double getObligationFixedAmount() {

    return ObligationFixedAmount;
  }

  public void setObligationFixedAmount(final Double obligationFixedAmount) {

    this.ObligationFixedAmount = obligationFixedAmount;
  }

  public Integer getObligationDebtPercentage() {

    return ObligationDebtPercentage;
  }

  public void
    setObligationDebtPercentage(final Integer obligationDebtPercentage) {

    this.ObligationDebtPercentage = obligationDebtPercentage;
  }

  public String getObligationVendorCode() {

    return ObligationVendorCode;
  }

  public void setObligationVendorCode(final String obligationVendorCode) {

    this.ObligationVendorCode = obligationVendorCode;
  }

  public Integer getObligationFixedAmountIndicator() {

    return ObligationFixedAmountIndicator;
  }

  public void setObligationFixedAmountIndicator(
    final Integer obligationFixedAmountIndicator) {

    this.ObligationFixedAmountIndicator = obligationFixedAmountIndicator;
  }

  public Double getObligationPaymentAmount() {

    return ObligationPaymentAmount;
  }

  public void
    setObligationPaymentAmount(final Double obligationPaymentAmount) {

    this.ObligationPaymentAmount = obligationPaymentAmount;
  }

  public Double getObligationOutstandingFeeBalance() {

    return ObligationOutstandingFeeBalance;
  }

  public void setObligationOutstandingFeeBalance(
    final Double obligationOutstandingFeeBalance) {

    this.ObligationOutstandingFeeBalance = obligationOutstandingFeeBalance;
  }

  public Double getObligationPaidAmount() {

    return ObligationPaidAmount;
  }

  public void setObligationPaidAmount(final Double obligationPaidAmount) {

    this.ObligationPaidAmount = obligationPaidAmount;
  }

  public String getObligationItcCode() {

    return ObligationItcCode;
  }

  public void setObligationItcCode(final String obligationItcCode) {

    this.ObligationItcCode = obligationItcCode;
  }

  public String getObligationOCONRegionalCode() {

    return ObligationOCONRegionalCode;
  }

  public void
    setObligationOCONRegionalCode(final String obligationOCONRegionalCode) {

    this.ObligationOCONRegionalCode = obligationOCONRegionalCode;
  }

  public String getObligationCanadianProvinceCode() {

    return ObligationCanadianProvinceCode;
  }

  public void setObligationCanadianProvinceCode(
    final String obligationCanadianProvinceCode) {

    this.ObligationCanadianProvinceCode = obligationCanadianProvinceCode;
  }

  public Integer getObligationNumberOfWeek() {

    return ObligationNumberOfWeek;
  }

  public void
    setObligationNumberOfWeek(final Integer obligationNumberOfWeek) {

    this.ObligationNumberOfWeek = obligationNumberOfWeek;
  }

  public Integer getObligationDebtSequenceNumber() {

    return ObligationDebtSequenceNumber;
  }

  public void setObligationDebtSequenceNumber(
    final Integer obligationDebtSequenceNumber) {

    this.ObligationDebtSequenceNumber = obligationDebtSequenceNumber;
  }

  public String getObligationPaymentDate() {

    return ObligationPaymentDate;
  }

  public void setObligationPaymentDate(final String obligationPaymentDate) {

    this.ObligationPaymentDate = obligationPaymentDate;
  }

  public Double getObligationFundsAvailable() {

    return ObligationFundsAvailable;
  }

  public void
    setObligationFundsAvailable(final Double obligationFundsAvailable) {

    this.ObligationFundsAvailable = obligationFundsAvailable;
  }

  public Double getObligationDebtRatioPercentage() {

    return ObligationDebtRatioPercentage;
  }

  public void setObligationDebtRatioPercentage(
    final Double obligationDebtRatioPercentage) {

    this.ObligationDebtRatioPercentage = obligationDebtRatioPercentage;
  }

  public Double getObligationAmountPerDebt() {

    return ObligationAmountPerDebt;
  }

  public void
    setObligationAmountPerDebt(final Double obligationAmountPerDebt) {

    this.ObligationAmountPerDebt = obligationAmountPerDebt;
  }

  public Double getObligationDivertAmount() {

    return ObligationDivertAmount;
  }

  public void setObligationDivertAmount(final Double obligationDivertAmount) {

    this.ObligationDivertAmount = obligationDivertAmount;
  }

  public Double getObligationAmountOfFeeIncluded() {

    return ObligationAmountOfFeeIncluded;
  }

  public void setObligationAmountOfFeeIncluded(
    final Double obligationAmountOfFeeIncluded) {

    this.ObligationAmountOfFeeIncluded = obligationAmountOfFeeIncluded;
  }

  public String getObligationException() {

    return ObligationException;
  }

  public void setObligationException(final String obligationException) {

    this.ObligationException = obligationException;
  }

  public Person getPerson() {

    return Person;
  }

  public void setPerson(final Person person) {

    this.Person = person;
  }

}
