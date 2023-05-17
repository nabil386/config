package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters expected in the response from the GetCompletedPDF API call
 */
public class BDMCCTGetCompletedPDFResponse {

  @SerializedName(value = "documentBytes")
  private String documentBytes;

  /**
   * @return the documentBytes
   */
  public String getDocumentBytes() {

    return this.documentBytes;
  }

  /**
   * @param documentBytes the documentBytes to set
   */
  public void setDocumentBytes(final String documentBytes) {

    this.documentBytes = documentBytes;
  }

}
