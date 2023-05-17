
package curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmsystemeventpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMDoJOutboundSystemEvent {

  @SerializedName("BatchId")
  @Expose
  private String BatchId;

  @SerializedName("ProgramId")
  @Expose
  private String ProgramId;

  @SerializedName("TransactionCount")
  @Expose
  private Integer TransactionCount;

  @SerializedName("TimeStamp")
  @Expose
  private String TimeStamp;

  @SerializedName("EventSource")
  @Expose
  private String EventSource;

  @SerializedName("EventDestination")
  @Expose
  private String EventDestination;

  @SerializedName("EventType")
  @Expose
  private String EventType;

  @SerializedName("Status")
  @Expose
  private String Status;

  @SerializedName("Message")
  @Expose
  private String Message;

  public String getBatchId() {

    return BatchId;
  }

  public void setBatchId(final String batchId) {

    this.BatchId = batchId;
  }

  public String getProgramId() {

    return ProgramId;
  }

  public void setProgramId(final String programId) {

    this.ProgramId = programId;
  }

  public Integer getTransactionCount() {

    return TransactionCount;
  }

  public void setTransactionCount(final Integer transactionCount) {

    this.TransactionCount = transactionCount;
  }

  public String getTimeStamp() {

    return TimeStamp;
  }

  public void setTimeStamp(final String timeStamp) {

    this.TimeStamp = timeStamp;
  }

  public String getEventSource() {

    return EventSource;
  }

  public void setEventSource(final String eventSource) {

    this.EventSource = eventSource;
  }

  public String getEventDestination() {

    return EventDestination;
  }

  public void setEventDestination(final String eventDestination) {

    this.EventDestination = eventDestination;
  }

  public String getEventType() {

    return EventType;
  }

  public void setEventType(final String eventType) {

    this.EventType = eventType;
  }

  public String getStatus() {

    return Status;
  }

  public void setStatus(final String status) {

    this.Status = status;
  }

  public String getMessage() {

    return this.Message;
  }

  public void setMessage(final String message) {

    this.Message = message;
  }

  @Override
  public String toString() {

    return "BDMDoJOutboundSystemEvent [BatchId=" + this.BatchId
      + ", ProgramId=" + this.ProgramId + ", TransactionCount="
      + this.TransactionCount + ", TimeStamp=" + this.TimeStamp
      + ", EventSource=" + this.EventSource + ", EventDestination="
      + this.EventDestination + ", EventType=" + this.EventType + ", Status="
      + this.Status + ", Message=" + this.Message + ", getBatchId()="
      + this.getBatchId() + ", getProgramId()=" + this.getProgramId()
      + ", getTransactionCount()=" + this.getTransactionCount()
      + ", getTimeStamp()=" + this.getTimeStamp() + ", getEventSource()="
      + this.getEventSource() + ", getEventDestination()="
      + this.getEventDestination() + ", getEventType()=" + this.getEventType()
      + ", getStatus()=" + this.getStatus() + ", getClass()="
      + this.getClass() + ", hashCode()=" + this.hashCode() + ", toString()="
      + super.toString() + "]";
  }

}
