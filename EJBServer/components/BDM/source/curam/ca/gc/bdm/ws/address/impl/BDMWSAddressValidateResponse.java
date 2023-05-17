package curam.ca.gc.bdm.ws.address.impl;

import java.util.ArrayList;
import java.util.List;

public class BDMWSAddressValidateResponse extends BDMWSAddressTypedResponse {

  private boolean isValid = false;

  private List<String> correctionErrorMessages = new ArrayList<String>();

  public boolean isAddressValid() {

    return this.isValid;
  }

  public void setIsAddressValid(final boolean isValid) {

    this.isValid = isValid;
  }

  public List<String> getValidationErrorMessages() {

    return this.correctionErrorMessages;
  }

  public void
    setValidationErrorMessages(final List<String> correctionErrorMessages) {

    this.correctionErrorMessages = correctionErrorMessages;
  }

}
