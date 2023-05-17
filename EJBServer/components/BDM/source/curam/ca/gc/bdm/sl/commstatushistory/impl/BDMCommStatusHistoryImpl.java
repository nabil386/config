package curam.ca.gc.bdm.sl.commstatushistory.impl;

import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.util.persistence.helper.SingleTableEntityImpl;
import curam.util.type.DateTime;

public class BDMCommStatusHistoryImpl
  extends SingleTableEntityImpl<BDMCommStatusHistoryDtls> {

  protected BDMCommStatusHistoryImpl() {// For use only by Guice

  }

  public long getCommunicationID() {

    return getDtls().communicationID;
  }

  public String getStatusCode() {

    return getDtls().statusCode;
  }

  public String getStatusCTTableName() {

    return getDtls().statusCTTableName;
  }

  public String getRecordStatus() {

    return getDtls().recordStatus;
  }

  public DateTime getStatusDataTime() {

    return getDtls().statusDateTime;
  }

  public String getUserName() {

    return getDtls().userName;
  }

  @Override
  public void crossEntityValidation() {

    // nothing needed
  }

  @Override
  public void crossFieldValidation() {

    // nothing needed
  }

  @Override
  public void mandatoryFieldValidation() {

    // nothing needed
  }

  @Override
  public void setNewInstanceDefaults() {

    // nothing needed
  }

}
