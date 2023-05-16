
package curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

  @SerializedName("MetadataIdentificationID")
  @Expose
  private String MetadataIdentificationID;

  @SerializedName("TransactionControlIdentificationID")
  @Expose
  private String TransactionControlIdentificationID;

  @SerializedName("DocumentSource")
  @Expose
  private String DocumentSource;

  @SerializedName("DocumentRecipient")
  @Expose
  private String DocumentRecipient;

  @SerializedName("DocumentFileDate")
  @Expose
  private String DocumentFileDate;

  @SerializedName("DocumentFileTime")
  @Expose
  private String DocumentFileTime;

  @SerializedName("DocumentFileName")
  @Expose
  private String DocumentFileName;

  @SerializedName("DocumentFileControlID")
  @Expose
  private Integer DocumentFileControlID;

  @SerializedName("DocumentFileWeekCode")
  @Expose
  private String DocumentFileWeekCode;

  @SerializedName("DocumentFileDayCode")
  @Expose
  private short DocumentFileDayCode;

  @SerializedName("DocumentFileEnvironmentType")
  @Expose
  private String DocumentFileEnvironmentType;

  public String getMetadataIdentificationID() {

    return MetadataIdentificationID;
  }

  public void
    setMetadataIdentificationID(final String metadataIdentificationID) {

    this.MetadataIdentificationID = metadataIdentificationID;
  }

  public String getTransactionControlIdentificationID() {

    return TransactionControlIdentificationID;
  }

  public void setTransactionControlIdentificationID(
    final String transactionControlIdentificationID) {

    this.TransactionControlIdentificationID =
      transactionControlIdentificationID;
  }

  public String getDocumentSource() {

    return DocumentSource;
  }

  public void setDocumentSource(final String documentSource) {

    this.DocumentSource = documentSource;
  }

  public String getDocumentRecipient() {

    return DocumentRecipient;
  }

  public void setDocumentRecipient(final String documentRecipient) {

    this.DocumentRecipient = documentRecipient;
  }

  public String getDocumentFileDate() {

    return DocumentFileDate;
  }

  public void setDocumentFileDate(final String documentFileDate) {

    this.DocumentFileDate = documentFileDate;
  }

  public String getDocumentFileTime() {

    return DocumentFileTime;
  }

  public void setDocumentFileTime(final String documentFileTime) {

    this.DocumentFileTime = documentFileTime;
  }

  public String getDocumentFileName() {

    return DocumentFileName;
  }

  public void setDocumentFileName(final String documentFileName) {

    this.DocumentFileName = documentFileName;
  }

  public Integer getDocumentFileControlID() {

    return DocumentFileControlID;
  }

  public void setDocumentFileControlID(final Integer documentFileControlID) {

    this.DocumentFileControlID = documentFileControlID;
  }

  public String getDocumentFileWeekCode() {

    return DocumentFileWeekCode;
  }

  public void setDocumentFileWeekCode(final String documentFileWeekCode) {

    this.DocumentFileWeekCode = documentFileWeekCode;
  }

  public short getDocumentFileDayCode() {

    return DocumentFileDayCode;
  }

  public void setDocumentFileDayCode(final short documentFileDayCode2) {

    this.DocumentFileDayCode = documentFileDayCode2;
  }

  public String getDocumentFileEnvironmentType() {

    return DocumentFileEnvironmentType;
  }

  public void
    setDocumentFileEnvironmentType(final String documentFileEnvironmentType) {

    this.DocumentFileEnvironmentType = documentFileEnvironmentType;
  }

}
