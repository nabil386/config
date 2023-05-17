package curam.ca.gc.bdm.rest.bdmdojinboundapi.impl;

import curam.ca.gc.bdm.rest.bdmdojinboundapi.struct.BDMMetadataType;
import curam.ca.gc.bdm.rest.bdmdojinboundapi.struct.BDMObligationType;

public class BDMDoJInboundDetails {

  private BDMMetadataType metadata;

  private BDMObligationType obligation;

  private String status;

  private String personObligationID;

  private int personSINIdentification;

  public String getPersonObligationID() {

    return this.personObligationID;
  }

  public void setPersonObligationID(final String personObligationID) {

    this.personObligationID = personObligationID;
  }

  public int getPersonSINIdentification() {

    return this.personSINIdentification;
  }

  public void setPersonSINIdentification(final int personSINIdentification) {

    this.personSINIdentification = personSINIdentification;
  }

  public String getPersonObligationIDSuffix() {

    return this.personObligationIDSuffix;
  }

  public void
    setPersonObligationIDSuffix(final String personObligationIDSuffix) {

    this.personObligationIDSuffix = personObligationIDSuffix;
  }

  private String personObligationIDSuffix;

  public void setStatus(final String status) {

    this.status = status;
  }

  public void setRestActionType(final String restActionType) {

    this.restActionType = restActionType;
  }

  public void setConcernRoleID(final long concernRoleID) {

    this.concernRoleID = concernRoleID;
  }

  public void setDojInboundRecordID(final long dojInboundRecordID) {

    this.dojInboundRecordID = dojInboundRecordID;
  }

  public void setCreationDate(final curam.util.type.Date creationDate) {

    this.creationDate = creationDate;
  }

  private String restActionType;

  private long concernRoleID;

  private long dojInboundRecordID;

  private curam.util.type.Date creationDate;

  public BDMMetadataType getMetadata() {

    return this.metadata;
  }

  public void setMetadata(final BDMMetadataType metadata) {

    this.metadata = metadata;
  }

  public BDMObligationType getObligation() {

    return this.obligation;
  }

  public void setObligation(final BDMObligationType obligation) {

    this.obligation = obligation;
  }

  public String getStatus() {

    return this.status;
  }

  public String getRestActionType() {

    return this.restActionType;
  }

  public long getConcernRoleID() {

    return this.concernRoleID;
  }

  public long getDojInboundRecordID() {

    return this.dojInboundRecordID;
  }

  public curam.util.type.Date getCreationDate() {

    return this.creationDate;
  }

}
