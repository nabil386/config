package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BDMSINSIRResponseError {

	@SerializedName("@context")
	@Expose
	private BDMSINSIRContext context;
	@SerializedName("fault:ErrorCode")
	@Expose
	private String faultErrorCode;
	@SerializedName("fault:Retryable")
	@Expose
	private Boolean faultRetryable;
	@SerializedName("fault:DescriptionText")
	@Expose
	private String faultDescriptionText;
	@SerializedName("fault:Details")
	@Expose
	private List<String> faultDetails = null;
	@SerializedName("fault:TransactionControlIdentificationID")
	@Expose
	private String faultTransactionControlIdentificationID;
	@SerializedName("fault:ZuluDateTime")
	@Expose
	private String faultZuluDateTime;

	public BDMSINSIRContext getContext() {
		return context;
	}

	public void setContext(BDMSINSIRContext context) {
		this.context = context;
	}

	public String getFaultErrorCode() {
		return faultErrorCode;
	}

	public void setFaultErrorCode(String faultErrorCode) {
		this.faultErrorCode = faultErrorCode;
	}

	public Boolean getFaultRetryable() {
		return faultRetryable;
	}

	public void setFaultRetryable(Boolean faultRetryable) {
		this.faultRetryable = faultRetryable;
	}

	public String getFaultDescriptionText() {
		return faultDescriptionText;
	}

	public void setFaultDescriptionText(String faultDescriptionText) {
		this.faultDescriptionText = faultDescriptionText;
	}

	public List<String> getFaultDetails() {
		return faultDetails;
	}

	public void setFaultDetails(List<String> faultDetails) {
		this.faultDetails = faultDetails;
	}

	public String getFaultTransactionControlIdentificationID() {
		return faultTransactionControlIdentificationID;
	}

	public void setFaultTransactionControlIdentificationID(String faultTransactionControlIdentificationID) {
		this.faultTransactionControlIdentificationID = faultTransactionControlIdentificationID;
	}

	public String getFaultZuluDateTime() {
		return faultZuluDateTime;
	}

	public void setFaultZuluDateTime(String faultZuluDateTime) {
		this.faultZuluDateTime = faultZuluDateTime;
	}

}