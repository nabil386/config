
package curam.ca.gc.bdm.correspondenceframework.impl;

public class BDMLetterTemplateTombstoneData {

  public String toCorrespondenceRecipientXML;

  public String ccCorrespondenceRecipientXML;

  public String clientXML;

  // Default it to @@documentIdentificationXML so that it does not replace to
  // empty string when staged and during the batch processing this will be updated
  public String documentIdentificationXML = "@@documentIdentificationXML";

}
