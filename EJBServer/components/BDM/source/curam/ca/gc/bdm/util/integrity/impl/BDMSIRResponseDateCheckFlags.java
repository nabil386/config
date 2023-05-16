package curam.ca.gc.bdm.util.integrity.impl;

public class BDMSIRResponseDateCheckFlags {

  private boolean appDateCheckFlag = false;

  private boolean reactivationDateCheckFlag = false;

  private boolean dobDateCheckFlag = false;

  private boolean valiadtionFlag = false;

  public boolean isAppDateCheckFlag() {

    return this.appDateCheckFlag;
  }

  public void setAppDateCheckFlag(final boolean appDateCheckFlag) {

    this.appDateCheckFlag = appDateCheckFlag;
  }

  public boolean isReactivationDateCheckFlag() {

    return this.reactivationDateCheckFlag;
  }

  public void
    setReactivationDateCheckFlag(final boolean reactivationDateCheckFlag) {

    this.reactivationDateCheckFlag = reactivationDateCheckFlag;
  }

  public boolean isDobDateCheckFlag() {

    return this.dobDateCheckFlag;
  }

  public void setDobDateCheckFlag(final boolean dobDateCheckFlag) {

    this.dobDateCheckFlag = dobDateCheckFlag;
  }

  public boolean isValiadtionFlag() {

    return this.valiadtionFlag;
  }

  public void setValiadtionFlag(final boolean valiadtionFlag) {

    this.valiadtionFlag = valiadtionFlag;
  }

}
